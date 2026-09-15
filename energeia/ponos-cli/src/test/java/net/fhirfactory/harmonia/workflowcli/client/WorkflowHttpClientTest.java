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

package net.fhirfactory.harmonia.workflowcli.client;

import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class WorkflowHttpClientTest {

    private HttpServer server;
    private int port;
    private String serverUrl;
    private WorkflowHttpClient client;

    @BeforeEach
    void setUp() throws IOException {
        server = HttpServer.create(new InetSocketAddress(0), 0);
        port = server.getAddress().getPort();
        server.setExecutor(null);
        server.start();

        serverUrl = "http://localhost:" + port;
        client = new WorkflowHttpClient(serverUrl);
    }

    @AfterEach
    void tearDown() {
        if (server != null) {
            server.stop(0);
        }
    }

    @Test
    @DisplayName("reloadAll triggers reload and returns synchronized queues and sequences")
    void testReloadAll() throws Exception {
        String json = """
        {
          "status": "SYNCHRONIZED",
          "synchronizedQueues": ["task.event.queue.pas-gw", "task.processing.queue"],
          "synchronizedQueuesCount": 2,
          "sequencesReloaded": true,
          "activeSequencesCount": 1,
          "activeSequenceIds": ["seq-patient-identity-pipeline"]
        }
        """;

        server.createContext("/workflow/reload", exchange -> {
            assertThat(exchange.getRequestMethod()).isEqualTo("POST");
            byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        });

        Map<String, Object> result = client.reloadAll();
        assertThat(result.get("status")).isEqualTo("SYNCHRONIZED");
        assertThat(result.get("synchronizedQueuesCount")).isEqualTo(2);
        assertThat(result.get("activeSequenceIds")).asList().contains("seq-patient-identity-pipeline");
    }

    @Test
    @DisplayName("validate triggers validation and returns queue and sequence validation report")
    void testValidate() throws Exception {
        String json = """
        {
          "status": "VALID",
          "allValid": true,
          "queueValidation": {
            "totalQueues": 2,
            "validQueues": 2,
            "invalidQueues": 0
          },
          "sequenceValidation": {
            "totalSequences": 1,
            "validSequences": 1,
            "invalidSequences": 0
          }
        }
        """;

        server.createContext("/workflow/validate", exchange -> {
            assertThat(exchange.getRequestMethod()).isEqualTo("GET");
            byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        });

        Map<String, Object> result = client.validate();
        assertThat(result.get("status")).isEqualTo("VALID");
        assertThat(result.get("allValid")).isEqualTo(true);
    }

    @Test
    @DisplayName("getStatus retrieves runtime status of processor")
    void testGetStatus() throws Exception {
        String json = """
        {
          "module": "task-sequence-processor",
          "brokerRunning": true,
          "camelStarted": true,
          "activeSequencesCount": 2
        }
        """;

        server.createContext("/workflow/status", exchange -> {
            assertThat(exchange.getRequestMethod()).isEqualTo("GET");
            byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        });

        Map<String, Object> result = client.getStatus();
        assertThat(result.get("module")).isEqualTo("task-sequence-processor");
        assertThat(result.get("brokerRunning")).isEqualTo(true);
        assertThat(result.get("activeSequencesCount")).isEqualTo(2);
    }
}
