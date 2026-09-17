/*
 * Copyright (c) 2026 Mark Hunter
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package net.fhirfactory.harmonia.agora.service.rest;

import net.fhirfactory.harmonia.agora.api.model.AgoraCollaborationEvent;
import net.fhirfactory.harmonia.agora.core.messaging.AgoraPetasosEventProducer;
import net.fhirfactory.harmonia.agora.core.persistence.AgoraTransactionEntity;
import net.fhirfactory.harmonia.agora.core.persistence.AgoraTransactionRepository;
import net.fhirfactory.harmonia.agora.service.config.AgoraProperties;
import net.fhirfactory.harmonia.petasos.api.exception.PetasosException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Timeout(value = 10, unit = TimeUnit.SECONDS)
class ApplicationServiceTransactionEndpointTest {

    private static final String HS_TOKEN = "secret_hs_token_12345";

    private AgoraProperties agoraProperties;
    private AgoraTransactionRepository transactionRepository;
    private AgoraPetasosEventProducer petasosEventProducer;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        agoraProperties = new AgoraProperties();
        agoraProperties.getSecurity().setHsToken(HS_TOKEN);

        transactionRepository = mock(AgoraTransactionRepository.class);
        when(transactionRepository.save(any(AgoraTransactionEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        petasosEventProducer = mock(AgoraPetasosEventProducer.class);

        ApplicationServiceTransactionEndpoint endpoint = new ApplicationServiceTransactionEndpoint(
                agoraProperties,
                transactionRepository,
                petasosEventProducer
        );

        mockMvc = MockMvcBuilders.standaloneSetup(endpoint).build();
    }

    @Test
    @DisplayName("Transaction endpoint rejects requests with missing Authorization header (401)")
    void testMissingAuthorizationHeaderRejects() throws Exception {
        mockMvc.perform(put("/_matrix/app/v1/transactions/txn_001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"events\":[]}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errcode").value("M_UNAUTHORIZED"))
                .andExpect(jsonPath("$.error").value("Invalid or missing homeserver token"));

        verifyNoInteractions(transactionRepository);
        verifyNoInteractions(petasosEventProducer);
    }

    @Test
    @DisplayName("Transaction endpoint rejects requests with invalid hs_token (401)")
    void testInvalidTokenRejects() throws Exception {
        mockMvc.perform(put("/_matrix/app/v1/transactions/txn_001")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer wrong_token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"events\":[]}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errcode").value("M_UNAUTHORIZED"));

        verifyNoInteractions(transactionRepository);
        verifyNoInteractions(petasosEventProducer);
    }

    @Test
    @DisplayName("Transaction endpoint accepts valid hs_token via access_token query param")
    void testQueryParamTokenAccepts() throws Exception {
        when(transactionRepository.findById("txn_query")).thenReturn(Optional.empty());

        mockMvc.perform(put("/_matrix/app/v1/transactions/txn_query")
                        .param("access_token", HS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"events\":[]}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{}"));

        verify(transactionRepository, atLeastOnce()).save(any(AgoraTransactionEntity.class));
    }

    @Test
    @DisplayName("Duplicate transaction returns 200 OK immediately without reprocessing")
    void testDuplicateTransactionIdempotency() throws Exception {
        when(transactionRepository.findById("txn_duplicate"))
                .thenReturn(Optional.of(new AgoraTransactionEntity("txn_duplicate", 1, AgoraTransactionEntity.STATUS_PROCESSED)));

        mockMvc.perform(put("/_matrix/app/v1/transactions/txn_duplicate")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + HS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"events\":[{\"event_id\":\"$e1\",\"type\":\"m.room.message\"}]}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{}"));

        verify(transactionRepository).findById("txn_duplicate");
        verify(transactionRepository, never()).save(any());
        verifyNoInteractions(petasosEventProducer);
    }

    @Test
    @DisplayName("Valid new transaction establishes RECEIVED state, dispatches to Petasos, and finalizes to PROCESSED")
    void testValidNewTransactionProcessing() throws Exception {
        when(transactionRepository.findById("txn_valid_001")).thenReturn(Optional.empty());
        List<String> capturedStatuses = new java.util.ArrayList<>();
        when(transactionRepository.save(any(AgoraTransactionEntity.class))).thenAnswer(invocation -> {
            AgoraTransactionEntity entity = invocation.getArgument(0);
            capturedStatuses.add(entity.getStatus());
            return entity;
        });

        String payload = """
                {
                  "events": [
                    {
                      "event_id": "$event_vital_1",
                      "type": "m.room.message",
                      "room_id": "!room_patient_1:harmonia.local",
                      "sender": "@_harmonia_p_dr_smith:harmonia.local",
                      "origin_server_ts": 1726581000000,
                      "content": {
                        "msgtype": "m.text",
                        "body": "HR 72, BP 120/80"
                      }
                    }
                  ]
                }
                """;

        mockMvc.perform(put("/_matrix/app/v1/transactions/txn_valid_001")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + HS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(content().json("{}"));

        ArgumentCaptor<AgoraCollaborationEvent> eventCaptor = ArgumentCaptor.forClass(AgoraCollaborationEvent.class);
        verify(petasosEventProducer).publishCollaborationEvent(eventCaptor.capture());

        AgoraCollaborationEvent capturedEvent = eventCaptor.getValue();
        assertThat(capturedEvent.getEventId()).isEqualTo("$event_vital_1");
        assertThat(capturedEvent.getEventType()).isEqualTo("m.room.message");
        assertThat(capturedEvent.getRoomId()).isEqualTo("!room_patient_1:harmonia.local");
        assertThat(capturedEvent.getSender()).isEqualTo("@_harmonia_p_dr_smith:harmonia.local");
        assertThat(capturedEvent.getContent()).isEqualTo("HR 72, BP 120/80");
        assertThat(capturedEvent.getCausationId()).isEqualTo("txn_valid_001");
        assertThat(capturedEvent.getSecurityContext()).isNotNull();
        assertThat(capturedEvent.getSecurityContext().requestingPrincipal().principalId())
                .isEqualTo("@_harmonia_p_dr_smith:harmonia.local");

        verify(transactionRepository, times(2)).save(any(AgoraTransactionEntity.class));
        assertThat(capturedStatuses).containsExactly(
                AgoraTransactionEntity.STATUS_RECEIVED,
                AgoraTransactionEntity.STATUS_PROCESSED
        );
    }

    @Test
    @DisplayName("Unknown event types are handled tolerantly and do not fail transaction")
    void testUnknownEventTypesTolerated() throws Exception {
        when(transactionRepository.findById("txn_unknown_types")).thenReturn(Optional.empty());

        String payload = """
                {
                  "events": [
                    {
                      "event_id": "$unknown_1",
                      "type": "org.matrix.msc9999.custom_event",
                      "room_id": "!room_1",
                      "content": {"foo": "bar"}
                    },
                    {
                      "event_id": "$msg_2",
                      "type": "m.room.message",
                      "room_id": "!room_1",
                      "sender": "@user:harmonia.local",
                      "content": {"body": "Known event"}
                    }
                  ]
                }
                """;

        mockMvc.perform(put("/_matrix/app/v1/transactions/txn_unknown_types")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + HS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(content().json("{}"));

        // Only the supported m.room.message event is dispatched to Petasos
        verify(petasosEventProducer, times(1)).publishCollaborationEvent(any(AgoraCollaborationEvent.class));
        verify(transactionRepository, atLeastOnce()).save(any(AgoraTransactionEntity.class));
    }

    @Test
    @DisplayName("Transaction endpoint rejects when initial transaction persistence fails (500) and dispatches zero events")
    void testPersistenceFailureRejectsWithoutDispatchingEvents() throws Exception {
        when(transactionRepository.findById("txn_persist_fail")).thenReturn(Optional.empty());
        doThrow(new RuntimeException("Database error")).when(transactionRepository).save(any(AgoraTransactionEntity.class));

        String payload = """
                {
                  "events": [
                    {
                      "event_id": "$event_fail_1",
                      "type": "m.room.message",
                      "room_id": "!room_1:harmonia.local",
                      "sender": "@user:harmonia.local",
                      "content": {"body": "Vital sign update"}
                    }
                  ]
                }
                """;

        mockMvc.perform(put("/_matrix/app/v1/transactions/txn_persist_fail")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + HS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errcode").value("M_INTERNAL_SERVER_ERROR"))
                .andExpect(jsonPath("$.error").value("Failed to persist transaction record"));

        // Crucial invariant: zero events published when durable record cannot be established
        verifyNoInteractions(petasosEventProducer);
    }

    @Test
    @DisplayName("Retry after initial persistence failure dispatches events exactly once across attempts")
    void testInitialPersistenceFailurePreventsDuplicateDispatchOnRetry() throws Exception {
        String txnId = "txn_retry_after_fail";
        when(transactionRepository.findById(txnId)).thenReturn(Optional.empty());

        // Attempt 1: Database error on initial save
        doThrow(new RuntimeException("Transient DB connection drop"))
                .when(transactionRepository).save(any(AgoraTransactionEntity.class));

        String payload = """
                {
                  "events": [
                    {
                      "event_id": "$event_vital_retry",
                      "type": "m.room.message",
                      "room_id": "!room_patient:harmonia.local",
                      "sender": "@_harmonia_p_dr_smith:harmonia.local",
                      "content": {"body": "Heart rate normal"}
                    }
                  ]
                }
                """;

        mockMvc.perform(put("/_matrix/app/v1/transactions/" + txnId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + HS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isInternalServerError());

        verifyNoInteractions(petasosEventProducer);

        // Attempt 2 (Synapse retry after DB restored): save now succeeds
        reset(transactionRepository);
        when(transactionRepository.findById(txnId)).thenReturn(Optional.empty());
        when(transactionRepository.save(any(AgoraTransactionEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(put("/_matrix/app/v1/transactions/" + txnId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + HS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(content().json("{}"));

        // Across both attempts, events were published exactly once
        verify(petasosEventProducer, times(1)).publishCollaborationEvent(any(AgoraCollaborationEvent.class));
    }

    @Test
    @DisplayName("Retry of previously failed transaction is handled intentionally and not silently dropped")
    void testRetryOfFailedTransactionReAttemptsDispatch() throws Exception {
        String txnId = "txn_failed_retry";
        AgoraTransactionEntity failedTxn = new AgoraTransactionEntity(
                txnId,
                1,
                AgoraTransactionEntity.STATUS_FAILED
        );
        when(transactionRepository.findById(txnId)).thenReturn(Optional.of(failedTxn));

        String payload = """
                {
                  "events": [
                    {
                      "event_id": "$event_failed_recovery",
                      "type": "m.room.message",
                      "room_id": "!room_recovery:harmonia.local",
                      "sender": "@user:harmonia.local",
                      "content": {"body": "Retry payload"}
                    }
                  ]
                }
                """;

        mockMvc.perform(put("/_matrix/app/v1/transactions/" + txnId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + HS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(content().json("{}"));

        // Event was dispatched on retry, not silently ignored
        verify(petasosEventProducer, times(1)).publishCollaborationEvent(any(AgoraCollaborationEvent.class));
        ArgumentCaptor<AgoraTransactionEntity> captor = ArgumentCaptor.forClass(AgoraTransactionEntity.class);
        verify(transactionRepository, atLeastOnce()).save(captor.capture());
        assertThat(captor.getValue().getStatus()).isEqualTo(AgoraTransactionEntity.STATUS_PROCESSED);
    }

    @Test
    @DisplayName("Broker failure during event dispatch marks transaction as FAILED and returns 500 for retry")
    void testDispatchFailureMarksTransactionAsFailedAndReturns500() throws Exception {
        String txnId = "txn_broker_fail";
        when(transactionRepository.findById(txnId)).thenReturn(Optional.empty());
        List<String> capturedStatuses = new java.util.ArrayList<>();
        when(transactionRepository.save(any(AgoraTransactionEntity.class))).thenAnswer(invocation -> {
            AgoraTransactionEntity entity = invocation.getArgument(0);
            capturedStatuses.add(entity.getStatus());
            return entity;
        });

        doThrow(new PetasosException("Artemis broker connection refused"))
                .when(petasosEventProducer).publishCollaborationEvent(any(AgoraCollaborationEvent.class));

        String payload = """
                {
                  "events": [
                    {
                      "event_id": "$event_broker_fail",
                      "type": "m.room.message",
                      "room_id": "!room_test:harmonia.local",
                      "sender": "@user:harmonia.local",
                      "content": {"body": "Urgent alert"}
                    }
                  ]
                }
                """;

        mockMvc.perform(put("/_matrix/app/v1/transactions/" + txnId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + HS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errcode").value("M_INTERNAL_SERVER_ERROR"))
                .andExpect(jsonPath("$.error").value("Event processing failed"));

        verify(transactionRepository, times(2)).save(any(AgoraTransactionEntity.class));
        assertThat(capturedStatuses).containsExactly(
                AgoraTransactionEntity.STATUS_RECEIVED,
                AgoraTransactionEntity.STATUS_FAILED
        );
    }

    @Test
    @DisplayName("Themis security authorization denial does not abort entire transaction batch")
    void testThemisDenialTolerated() throws Exception {
        when(transactionRepository.findById("txn_themis_denied")).thenReturn(Optional.empty());
        doThrow(new SecurityException("Themis policy authorization denied event publishing: DEFAULT_DENY"))
                .when(petasosEventProducer).publishCollaborationEvent(any(AgoraCollaborationEvent.class));

        String payload = """
                {
                  "events": [
                    {
                      "event_id": "$event_denied",
                      "type": "m.room.message",
                      "room_id": "!room_restricted",
                      "sender": "@unauthorized:harmonia.local",
                      "content": {"body": "Secret info"}
                    }
                  ]
                }
                """;

        mockMvc.perform(put("/_matrix/app/v1/transactions/txn_themis_denied")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + HS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(content().json("{}"));

        verify(petasosEventProducer).publishCollaborationEvent(any(AgoraCollaborationEvent.class));
        verify(transactionRepository, atLeastOnce()).save(any(AgoraTransactionEntity.class));
    }

    @Test
    @DisplayName("Empty or null transaction payload safely acknowledged")
    void testEmptyPayloadAcknowledged() throws Exception {
        when(transactionRepository.findById("txn_empty")).thenReturn(Optional.empty());

        mockMvc.perform(put("/_matrix/app/v1/transactions/txn_empty")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + HS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{}"));

        verify(transactionRepository, atLeastOnce()).save(any(AgoraTransactionEntity.class));
        verifyNoInteractions(petasosEventProducer);
    }
}
