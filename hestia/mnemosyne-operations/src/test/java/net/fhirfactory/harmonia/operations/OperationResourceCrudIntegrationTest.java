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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.fhirfactory.harmonia.operations;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OperationResourceCrudIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should create, retrieve, update, check existence, and delete tasksequence resource")
    void testTaskSequenceCrudOperations() throws Exception {
        String sequenceId = "seq-test-integration-1";
        String initialJson = """
                {
                  "sequenceId": "seq-test-integration-1",
                  "sequenceName": "Test Integration Sequence",
                  "version": "1.0.0",
                  "enabled": true,
                  "activityIds": ["patient-identity-update"]
                }
                """;

        // 1. PUT create
        mockMvc.perform(put("/api/operations/tasksequence/" + sequenceId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(initialJson))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.sequenceId").value(sequenceId))
                .andExpect(jsonPath("$.sequenceName").value("Test Integration Sequence"));

        // 2. GET retrieve
        mockMvc.perform(get("/api/operations/tasksequence/" + sequenceId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.sequenceId").value(sequenceId))
                .andExpect(jsonPath("$.version").value("1.0.0"));

        // 3. HEAD exists
        mockMvc.perform(head("/api/operations/tasksequence/" + sequenceId))
                .andExpect(status().isOk());

        // 4. PUT update
        String updatedJson = """
                {
                  "sequenceId": "seq-test-integration-1",
                  "sequenceName": "Updated Integration Sequence",
                  "version": "1.1.0",
                  "enabled": true,
                  "activityIds": ["patient-identity-update", "patient-demographics-update"]
                }
                """;

        mockMvc.perform(put("/api/operations/tasksequence/" + sequenceId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.version").value("1.1.0"));

        // Verify updated GET
        mockMvc.perform(get("/api/operations/tasksequence/" + sequenceId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sequenceName").value("Updated Integration Sequence"));

        // 5. List by type
        mockMvc.perform(get("/api/operations/tasksequence"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        // 6. DELETE
        mockMvc.perform(delete("/api/operations/tasksequence/" + sequenceId))
                .andExpect(status().isNoContent());

        // Verify GET after delete returns 404
        mockMvc.perform(get("/api/operations/tasksequence/" + sequenceId))
                .andExpect(status().isNotFound());

        // Verify HEAD after delete returns 404
        mockMvc.perform(head("/api/operations/tasksequence/" + sequenceId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should handle generic operational key-value resources")
    void testGenericOperationResourceOperations() throws Exception {
        String key = "gateway-config-node-1";
        String payload = "{\"host\":\"0.0.0.0\",\"port\":2575,\"active\":true}";

        // PUT
        mockMvc.perform(put("/api/operations/config/" + key)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk());

        // GET
        mockMvc.perform(get("/api/operations/config/" + key))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.port").value(2575));

        // DELETE
        mockMvc.perform(delete("/api/operations/config/" + key))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return 200 and ready status on /ready and /api/operations/ready")
    void testReadinessAndStatusEndpoints() throws Exception {
        mockMvc.perform(get("/ready"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.ready").value(true))
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.server").value("mnemosyne-operations"));

        mockMvc.perform(get("/api/operations/ready"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ready").value(true));

        mockMvc.perform(head("/ready"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.totalResources").isNumber());

        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    @DisplayName("Should store and retrieve messagequeue definitions")
    void testMessageQueueStorage() throws Exception {
        String queueId = "task.processing.queue";
        String payload = """
                {
                  "queueId": "task.processing.queue",
                  "queueName": "task.processing.queue",
                  "routingType": "ANYCAST",
                  "durable": false,
                  "enabled": true
                }
                """;

        mockMvc.perform(put("/api/operations/messagequeue/" + queueId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.queueName").value("task.processing.queue"));

        mockMvc.perform(get("/api/operations/messagequeue/" + queueId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.routingType").value("ANYCAST"));

        mockMvc.perform(delete("/api/operations/messagequeue/" + queueId))
                .andExpect(status().isNoContent());
    }
}
