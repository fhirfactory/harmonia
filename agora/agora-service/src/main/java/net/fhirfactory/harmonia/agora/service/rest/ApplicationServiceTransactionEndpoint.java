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
import net.fhirfactory.harmonia.agora.matrix.appservice.AppServiceEventDto;
import net.fhirfactory.harmonia.agora.matrix.appservice.AppServiceTransactionDto;
import net.fhirfactory.harmonia.agora.service.config.AgoraProperties;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;

/**
 * Matrix Application Service Transaction Endpoint.
 * Handles incoming push transactions from Matrix Synapse at PUT /_matrix/app/v1/transactions/{txnId}.
 * Enforces Bearer hs_token authentication, durable transaction deduplication against Mnemosyne,
 * tolerant handling of unknown event types, and dispatch to Petasos under Themis security governance.
 */
@RestController
public class ApplicationServiceTransactionEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationServiceTransactionEndpoint.class);

    private static final Set<String> SUPPORTED_EVENT_TYPES = Set.of(
            "m.room.message",
            "m.room.member",
            "m.space.child",
            "m.space.parent",
            "m.room.topic",
            "m.room.name",
            "m.room.power_levels"
    );

    private final AgoraProperties agoraProperties;
    private final AgoraTransactionRepository transactionRepository;
    private final AgoraPetasosEventProducer petasosEventProducer;

    @Autowired
    public ApplicationServiceTransactionEndpoint(
            AgoraProperties agoraProperties,
            AgoraTransactionRepository transactionRepository,
            AgoraPetasosEventProducer petasosEventProducer) {
        this.agoraProperties = Objects.requireNonNull(agoraProperties, "AgoraProperties must not be null");
        this.transactionRepository = Objects.requireNonNull(transactionRepository, "AgoraTransactionRepository must not be null");
        this.petasosEventProducer = Objects.requireNonNull(petasosEventProducer, "AgoraPetasosEventProducer must not be null");
    }

    /**
     * Processes an inbound Matrix Application Service transaction.
     *
     * @param txnId          the homeserver transaction ID
     * @param authHeader     the Authorization header containing Bearer hs_token
     * @param queryToken     optional query parameter access_token
     * @param transactionDto the transaction payload containing event batch
     * @return empty JSON object {} on success or error response
     */
    @PutMapping(
            value = "/_matrix/app/v1/transactions/{txnId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Map<String, Object>> handleTransaction(
            @PathVariable("txnId") String txnId,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader,
            @RequestParam(value = "access_token", required = false) String queryToken,
            @RequestBody(required = false) AppServiceTransactionDto transactionDto
    ) {
        // Step 1: Validate homeserver Bearer hs_token authentication
        if (!authenticateHomeserver(authHeader, queryToken)) {
            LOGGER.warn("Rejected unauthorized AS transaction request txnId={}", txnId);
            Map<String, Object> errorBody = Map.of(
                    "errcode", "M_UNAUTHORIZED",
                    "error", "Invalid or missing homeserver token"
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorBody);
        }

        List<AppServiceEventDto> events = (transactionDto != null && transactionDto.getEvents() != null)
                ? transactionDto.getEvents()
                : Collections.emptyList();

        // Step 2: Check existing transaction record and lifecycle state
        Optional<AgoraTransactionEntity> existingTxnOpt = transactionRepository.findById(txnId);
        AgoraTransactionEntity transactionEntity;

        if (existingTxnOpt.isPresent()) {
            AgoraTransactionEntity existing = existingTxnOpt.get();
            String currentStatus = existing.getStatus();

            if (AgoraTransactionEntity.STATUS_PROCESSED.equalsIgnoreCase(currentStatus)
                    || AgoraTransactionEntity.STATUS_IGNORED.equalsIgnoreCase(currentStatus)) {
                LOGGER.info("Deduplicated already-processed AS transaction txnId={}, status={}", txnId, currentStatus);
                return ResponseEntity.ok(Collections.emptyMap());
            }

            LOGGER.info("Handling retry for incomplete/failed AS transaction txnId={}, previousStatus={}",
                    txnId, currentStatus);
            existing.setEventCount(events.size());
            existing.setStatus(AgoraTransactionEntity.STATUS_RECEIVED);
            existing.setReceivedAt(Instant.now());
            transactionEntity = existing;
        } else {
            transactionEntity = new AgoraTransactionEntity(
                    txnId,
                    events.size(),
                    AgoraTransactionEntity.STATUS_RECEIVED
            );
        }

        // Step 3: Establish durable transaction record BEFORE event dispatch
        try {
            transactionEntity = transactionRepository.save(transactionEntity);
        } catch (Exception ex) {
            LOGGER.error("Failed to persist initial transaction record for txnId={}: {}", txnId, ex.getMessage());
            Map<String, Object> errorBody = Map.of(
                    "errcode", "M_INTERNAL_SERVER_ERROR",
                    "error", "Failed to persist transaction record"
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorBody);
        }

        LOGGER.info("Processing inbound AS transaction txnId={}, eventCount={}", txnId, events.size());

        // Step 4: Dispatch events under Themis security governance
        boolean dispatchFailed = false;
        try {
            for (AppServiceEventDto event : events) {
                processEventTolerantly(txnId, event);
            }
        } catch (Exception ex) {
            LOGGER.error("Failed while dispatching events for txnId={}: {}", txnId, ex.getMessage());
            dispatchFailed = true;
        }

        if (dispatchFailed) {
            transactionEntity.setStatus(AgoraTransactionEntity.STATUS_FAILED);
            transactionEntity.setProcessedAt(Instant.now());
            try {
                transactionRepository.save(transactionEntity);
            } catch (Exception saveEx) {
                LOGGER.error("Failed to mark transaction as FAILED for txnId={}: {}", txnId, saveEx.getMessage());
            }
            Map<String, Object> errorBody = Map.of(
                    "errcode", "M_INTERNAL_SERVER_ERROR",
                    "error", "Event processing failed"
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorBody);
        }

        // Step 5: Update transaction record to PROCESSED
        transactionEntity.setStatus(AgoraTransactionEntity.STATUS_PROCESSED);
        transactionEntity.setProcessedAt(Instant.now());
        try {
            transactionRepository.save(transactionEntity);
        } catch (Exception ex) {
            LOGGER.error("Failed to mark transaction as PROCESSED for txnId={}: {}", txnId, ex.getMessage());
            Map<String, Object> errorBody = Map.of(
                    "errcode", "M_INTERNAL_SERVER_ERROR",
                    "error", "Failed to update transaction status"
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorBody);
        }

        return ResponseEntity.ok(Collections.emptyMap());
    }

    private void processEventTolerantly(String txnId, AppServiceEventDto event) throws Exception {
        if (event == null) {
            return;
        }

        String eventType = event.getType();
        String eventId = event.getEventId();
        String roomId = event.getRoomId();

        if (eventType == null || !SUPPORTED_EVENT_TYPES.contains(eventType)) {
            LOGGER.debug("Ignoring unrecognized or unsupported Matrix event type: eventId={}, eventType={}, roomId={}",
                    eventId, eventType, roomId);
            return;
        }

        try {
            String contentStr = "";
            if (event.getContent() != null) {
                Object bodyObj = event.getContent().get("body");
                contentStr = (bodyObj != null) ? bodyObj.toString() : event.getContent().toString();
            }

            AgoraCollaborationEvent collaborationEvent = AgoraCollaborationEvent.builder()
                    .eventId(eventId)
                    .eventType(eventType)
                    .roomId(roomId)
                    .sender(event.getSender())
                    .timestamp(event.getOriginServerTs() != null
                            ? Instant.ofEpochMilli(event.getOriginServerTs())
                            : Instant.now())
                    .content(contentStr)
                    .metadata(event.getContent() != null ? event.getContent() : Collections.emptyMap())
                    .correlationId(eventId != null ? eventId : UUID.randomUUID().toString())
                    .causationId(txnId)
                    .securityContext(event.getSender() != null
                            ? ThemisSecurityContext.fromPrincipal(ThemisPrincipal.human(event.getSender()), eventId)
                            : null)
                    .build();

            petasosEventProducer.publishCollaborationEvent(collaborationEvent);
        } catch (SecurityException secEx) {
            LOGGER.warn("Themis security governance denied publishing eventId={}, roomId={}: {}",
                    eventId, roomId, secEx.getMessage());
        } catch (Exception ex) {
            LOGGER.error("Failed to publish collaboration event to Petasos: eventId={}, roomId={}",
                    eventId, roomId, ex);
            throw ex;
        }
    }

    private boolean authenticateHomeserver(String authHeader, String queryToken) {
        String configuredHsToken = agoraProperties.getSecurity().getHsToken();
        if (configuredHsToken == null || configuredHsToken.isBlank()) {
            LOGGER.error("No homeserver token configured (agora.security.hsToken is null or empty) - failing closed");
            return false;
        }

        String presentedToken = null;
        if (authHeader != null && authHeader.toLowerCase().startsWith("bearer ")) {
            presentedToken = authHeader.substring(7).trim();
        } else if (queryToken != null && !queryToken.isBlank()) {
            presentedToken = queryToken.trim();
        }

        if (presentedToken == null || presentedToken.isBlank()) {
            return false;
        }

        return constantTimeEquals(configuredHsToken, presentedToken);
    }

    private boolean constantTimeEquals(String a, String b) {
        if (a == null || b == null) {
            return false;
        }
        if (a.length() != b.length()) {
            return false;
        }
        int result = 0;
        for (int i = 0; i < a.length(); i++) {
            result |= a.charAt(i) ^ b.charAt(i);
        }
        return result == 0;
    }
}
