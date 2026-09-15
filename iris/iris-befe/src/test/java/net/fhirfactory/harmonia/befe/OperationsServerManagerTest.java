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

package net.fhirfactory.harmonia.befe;

import net.fhirfactory.harmonia.befe.rest.SystemStatusResource;
import net.fhirfactory.harmonia.befe.rest.TaskSequenceResource;
import net.fhirfactory.harmonia.befe.server.OperationsServerManager;
import net.fhirfactory.harmonia.befe.service.FhirCacheService;
import net.fhirfactory.harmonia.befe.service.ModuleStatusService;
import net.fhirfactory.harmonia.befe.service.TaskSequenceCacheService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class OperationsServerManagerTest {

    private OperationsServerManager serverManager;
    private TaskSequenceCacheService sequenceCacheService;
    private TaskSequenceResource sequenceResource;
    private SystemStatusResource statusResource;
    private ModuleStatusService moduleStatusService;
    private int testPort;

    @BeforeEach
    void setUp() throws Exception {
        sequenceCacheService = new TaskSequenceCacheService();
        sequenceCacheService.init();

        moduleStatusService = new ModuleStatusService();

        sequenceResource = new TaskSequenceResource();
        java.lang.reflect.Field seqField = TaskSequenceResource.class.getDeclaredField("sequenceCacheService");
        seqField.setAccessible(true);
        seqField.set(sequenceResource, sequenceCacheService);

        statusResource = new SystemStatusResource();
        statusResource.setTaskSequenceCacheService(sequenceCacheService);
        statusResource.setModuleStatusService(moduleStatusService);
        FhirCacheService fhirCacheService = new FhirCacheService();
        fhirCacheService.init();
        statusResource.setFhirCacheService(fhirCacheService);

        serverManager = new OperationsServerManager();
        serverManager.setPort(0);
        serverManager.setHost("127.0.0.1");
        serverManager.setTaskSequenceResource(sequenceResource);
        serverManager.setSystemStatusResource(statusResource);
        serverManager.setSequenceCacheService(sequenceCacheService);
        serverManager.setModuleStatusService(moduleStatusService);

        statusResource.setOperationsServerManager(serverManager);

        serverManager.startServer();
        testPort = serverManager.getPort();
    }

    @AfterEach
    void tearDown() {
        if (serverManager != null) {
            serverManager.stopServer();
        }
    }

    @Test
    @DisplayName("1. Operations HTTP Server starts and handles status endpoint")
    void testStatusEndpoint() throws Exception {
        assertThat(serverManager.isRunning()).isTrue();
        assertThat(serverManager.getPort()).isEqualTo(testPort);

        URI uri = URI.create("http://127.0.0.1:" + testPort + "/api/operations/status");
        HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        int responseCode = conn.getResponseCode();
        assertThat(responseCode).isEqualTo(200);

        try (InputStream is = conn.getInputStream()) {
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            assertThat(body).contains("HIE Platform 5-Tier Architecture");
            assertThat(body).contains("tier2_befe");
            assertThat(body).contains("\"operationsPort\":" + testPort);
        }

        // Test modules endpoint
        URI modUri = URI.create("http://127.0.0.1:" + testPort + "/api/operations/modules");
        HttpURLConnection modConn = (HttpURLConnection) modUri.toURL().openConnection();
        modConn.setRequestMethod("GET");
        modConn.connect();
        assertThat(modConn.getResponseCode()).isEqualTo(200);
        try (InputStream is = modConn.getInputStream()) {
            String modBody = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            assertThat(modBody).contains("befe");
        }
    }

    @Test
    @DisplayName("2. Operations HTTP Server handles TaskSequence CRUD")
    void testSequenceCrudOnOperationsPort() throws Exception {
        // 1. Create sequence via POST
        URI createUri = URI.create("http://127.0.0.1:" + testPort + "/api/operations/sequences");
        HttpURLConnection createConn = (HttpURLConnection) createUri.toURL().openConnection();
        createConn.setRequestMethod("POST");
        createConn.setDoOutput(true);
        createConn.setRequestProperty("Content-Type", "application/json");

        String newSeqJson = """
                {
                  "sequenceId": "seq-test-ops-1",
                  "sequenceName": "Test Operations Sequence",
                  "description": "Integration test sequence over operations port",
                  "enabled": true,
                  "targetGatewayInstances": ["mllp-gateway-1"],
                  "targetTriggerTypes": ["A01", "A08"]
                }
                """;

        try (OutputStream os = createConn.getOutputStream()) {
            os.write(newSeqJson.getBytes(StandardCharsets.UTF_8));
        }

        assertThat(createConn.getResponseCode()).isIn(200, 201);

        // 2. Read back created sequence via GET
        URI getUri = URI.create("http://127.0.0.1:" + testPort + "/api/operations/sequences/seq-test-ops-1");
        HttpURLConnection getConn = (HttpURLConnection) getUri.toURL().openConnection();
        getConn.setRequestMethod("GET");

        assertThat(getConn.getResponseCode()).isEqualTo(200);
        try (InputStream is = getConn.getInputStream()) {
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            assertThat(body).contains("seq-test-ops-1");
            assertThat(body).contains("Test Operations Sequence");
        }

        // 3. List all sequences
        URI listUri = URI.create("http://127.0.0.1:" + testPort + "/api/operations/sequences");
        HttpURLConnection listConn = (HttpURLConnection) listUri.toURL().openConnection();
        listConn.setRequestMethod("GET");

        assertThat(listConn.getResponseCode()).isEqualTo(200);
        try (InputStream is = listConn.getInputStream()) {
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            assertThat(body).contains("seq-test-ops-1");
        }

        // 4. Update sequence via PUT
        URI putUri = URI.create("http://127.0.0.1:" + testPort + "/api/operations/sequences/seq-test-ops-1");
        HttpURLConnection putConn = (HttpURLConnection) putUri.toURL().openConnection();
        putConn.setRequestMethod("PUT");
        putConn.setDoOutput(true);
        putConn.setRequestProperty("Content-Type", "application/json");

        String updateSeqJson = """
                {
                  "sequenceId": "seq-test-ops-1",
                  "sequenceName": "Updated Operations Sequence",
                  "enabled": false
                }
                """;

        try (OutputStream os = putConn.getOutputStream()) {
            os.write(updateSeqJson.getBytes(StandardCharsets.UTF_8));
        }

        assertThat(putConn.getResponseCode()).isEqualTo(200);

        // 5. Delete sequence via DELETE
        URI delUri = URI.create("http://127.0.0.1:" + testPort + "/api/operations/sequences/seq-test-ops-1");
        HttpURLConnection delConn = (HttpURLConnection) delUri.toURL().openConnection();
        delConn.setRequestMethod("DELETE");

        assertThat(delConn.getResponseCode()).isIn(200, 204);

        // 6. Verify 404
        HttpURLConnection verifyConn = (HttpURLConnection) delUri.toURL().openConnection();
        verifyConn.setRequestMethod("GET");
        assertThat(verifyConn.getResponseCode()).isEqualTo(404);
    }

    @Test
    @DisplayName("3. Operations HTTP Server supports CORS preflight")
    void testCorsPreflight() throws Exception {
        URI uri = URI.create("http://127.0.0.1:" + testPort + "/api/operations/sequences");
        HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
        conn.setRequestMethod("OPTIONS");
        conn.connect();

        assertThat(conn.getResponseCode()).isEqualTo(200);
        assertThat(conn.getHeaderField("Access-Control-Allow-Origin")).isEqualTo("*");
        assertThat(conn.getHeaderField("Access-Control-Allow-Methods")).contains("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }

    @Test
    @DisplayName("4. Operations HTTP Server supports operational resources query")
    void testOperationalResourcesQuery() throws Exception {
        URI uri = URI.create("http://127.0.0.1:" + testPort + "/api/operations/resources/tasksequence");
        HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        assertThat(conn.getResponseCode()).isEqualTo(200);
        try (InputStream is = conn.getInputStream()) {
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            assertThat(body).contains("tasksequence");
        }
    }
}
