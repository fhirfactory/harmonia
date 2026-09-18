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

import net.fhirfactory.harmonia.agora.core.persistence.AgoraTransactionEntity;
import net.fhirfactory.harmonia.agora.core.persistence.AgoraTransactionRepository;
import net.fhirfactory.harmonia.agora.service.AgoraApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = AgoraApplication.class)
@AutoConfigureMockMvc
@Timeout(value = 10, unit = TimeUnit.SECONDS)
class ApplicationServiceTransactionEndpointIntegrationTest {

    private static final String HS_TOKEN = "test-hs-token";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AgoraTransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        transactionRepository.deleteAll();
    }

    @Test
    @DisplayName("End-to-end integration: Valid AS transaction persists idempotency record into database")
    void testEndToEndTransactionPersistence() throws Exception {
        String txnId = "int_txn_001";
        String payload = """
                {
                  "events": [
                    {
                      "event_id": "$e_int_1",
                      "type": "m.room.message",
                      "room_id": "!room_int_1:harmonia.local",
                      "sender": "@_harmonia_p_practitioner_1:harmonia.local",
                      "origin_server_ts": 1726582000000,
                      "content": {
                        "msgtype": "m.text",
                        "body": "Care plan reviewed"
                      }
                    }
                  ]
                }
                """;

        // First call: processes and stores in database
        mockMvc.perform(put("/_matrix/app/v1/transactions/" + txnId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + HS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(content().json("{}"));

        Optional<AgoraTransactionEntity> saved = transactionRepository.findById(txnId);
        assertThat(saved).isPresent();
        assertThat(saved.get().getEventCount()).isEqualTo(1);
        assertThat(saved.get().getStatus()).isEqualTo("PROCESSED");

        // Second call with same txnId: deduplicated against database, returns 200 OK
        mockMvc.perform(put("/_matrix/app/v1/transactions/" + txnId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + HS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(content().json("{}"));
    }
}
