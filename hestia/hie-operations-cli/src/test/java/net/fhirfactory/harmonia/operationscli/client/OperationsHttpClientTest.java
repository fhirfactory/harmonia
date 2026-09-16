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

package net.fhirfactory.harmonia.operationscli.client;

import com.sun.net.httpserver.HttpServer;
import net.fhirfactory.harmonia.operationscli.model.OperationResourceDto;
import net.fhirfactory.harmonia.operationscli.model.PraxisDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OperationsHttpClientTest {

    private HttpServer server;
    private OperationsHttpClient client;
    private int port;

    @BeforeEach
    void setUp() throws IOException {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        port = server.getAddress().getPort();
        server.setExecutor(null);
        server.start();

        client = new OperationsHttpClient("http://127.0.0.1:" + port + "/api/operations", 5);
    }

    @AfterEach
    void tearDown() {
        if (server != null) {
            server.stop(0);
        }
    }

    @Test
    @DisplayName("listAllResources returns deserialized list of operational resources")
    void testListAllResources() throws Exception {
        String json = """
        [
          {
            "id": 1,
            "objectType": "tasksequence",
            "objectId": "seq-1",
            "versionId": 1,
            "dataJson": "{\\"sequenceId\\":\\"seq-1\\",\\"sequenceName\\":\\"Test Seq\\"}",
            "deleted": false
          }
        ]
        """;

        server.createContext("/api/operations", exchange -> {
            byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        });

        List<OperationResourceDto> list = client.listAllResources();
        assertThat(list).hasSize(1);
        assertThat(list.get(0).getId()).isEqualTo(1L);
        assertThat(list.get(0).getObjectType()).isEqualTo("tasksequence");
        assertThat(list.get(0).getObjectId()).isEqualTo("seq-1");
    }

    @Test
    @DisplayName("listTaskSequences deserializes nested sequence JSON into TaskSequenceDto list")
    void testListTaskSequences() throws Exception {
        String json = """
        [
          {
            "id": 1,
            "objectType": "tasksequence",
            "objectId": "seq-admission",
            "versionId": 1,
            "dataJson": "{\\"sequenceId\\":\\"seq-admission\\",\\"sequenceName\\":\\"Admission\\",\\"enabled\\":true,\\"targetGatewayInstances\\":[\\"*\\"],\\"targetTriggerTypes\\":[\\"A01\\"],\\"activityIds\\":[\\"act-1\\"]}",
            "deleted": false
          }
        ]
        """;

        server.createContext("/api/operations/tasksequence", exchange -> {
            byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        });

        List<PraxisDto> sequences = client.listTaskSequences();
        assertThat(sequences).hasSize(1);
        PraxisDto seq = sequences.get(0);
        assertThat(seq.getPraxisId()).isEqualTo("seq-admission");
        assertThat(seq.getPraxisName()).isEqualTo("Admission");
        assertThat(seq.isEnabled()).isTrue();
        assertThat(seq.getTargetTriggerTypes()).containsExactly("A01");
        assertThat(seq.getActivityIdList()).containsExactly("act-1");
    }

    @Test
    @DisplayName("getResourceJson returns content for existing resource and empty for 404")
    void testGetResourceJson() throws Exception {
        server.createContext("/api/operations/tasksequence/seq-found", exchange -> {
            byte[] bytes = "{\"sequenceId\":\"seq-found\"}".getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        });

        server.createContext("/api/operations/tasksequence/seq-missing", exchange -> {
            exchange.sendResponseHeaders(404, -1);
        });

        Optional<String> found = client.getResourceJson("tasksequence", "seq-found");
        assertThat(found).isPresent();
        assertThat(found.get()).contains("seq-found");

        Optional<String> missing = client.getResourceJson("tasksequence", "seq-missing");
        assertThat(missing).isEmpty();
    }

    @Test
    @DisplayName("saveTaskSequence sends PUT request and returns saved JSON")
    void testSaveTaskSequence() throws Exception {
        server.createContext("/api/operations/tasksequence/seq-new", exchange -> {
            assertThat(exchange.getRequestMethod()).isEqualTo("PUT");
            byte[] bytes = "{\"sequenceId\":\"seq-new\",\"sequenceName\":\"New Seq\"}".getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        });

        PraxisDto dto = new PraxisDto("seq-new", "New Seq");
        String result = client.saveTaskSequence(dto);
        assertThat(result).contains("seq-new");
    }

    @Test
    @DisplayName("deleteTaskSequence returns true on 204 and false on 404")
    void testDeleteTaskSequence() throws Exception {
        server.createContext("/api/operations/tasksequence/seq-delete-me", exchange -> {
            assertThat(exchange.getRequestMethod()).isEqualTo("DELETE");
            exchange.sendResponseHeaders(204, -1);
        });

        server.createContext("/api/operations/tasksequence/seq-not-there", exchange -> {
            exchange.sendResponseHeaders(404, -1);
        });

        boolean deleted = client.deleteTaskSequence("seq-delete-me");
        assertThat(deleted).isTrue();

        boolean notDeleted = client.deleteTaskSequence("seq-not-there");
        assertThat(notDeleted).isFalse();
    }

    @Test
    @DisplayName("containsResource sends HEAD request and returns boolean")
    void testContainsResource() throws Exception {
        server.createContext("/api/operations/config/sys-config", exchange -> {
            assertThat(exchange.getRequestMethod()).isEqualTo("HEAD");
            exchange.sendResponseHeaders(200, -1);
        });

        server.createContext("/api/operations/config/missing-config", exchange -> {
            exchange.sendResponseHeaders(404, -1);
        });

        assertThat(client.containsResource("config", "sys-config")).isTrue();
        assertThat(client.containsResource("config", "missing-config")).isFalse();
    }
}
