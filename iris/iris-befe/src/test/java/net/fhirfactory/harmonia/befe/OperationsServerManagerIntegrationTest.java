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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.fhirfactory.harmonia.befe.model.operations.*;
import net.fhirfactory.harmonia.befe.provider.AbstractSubsystemHealthProvider;
import net.fhirfactory.harmonia.befe.provider.KubernetesInstanceProvider;
import net.fhirfactory.harmonia.befe.rest.OperationsResource;
import net.fhirfactory.harmonia.befe.rest.SystemStatusResource;
import net.fhirfactory.harmonia.befe.rest.TaskSequenceResource;
import net.fhirfactory.harmonia.befe.security.ThemisOperationsAuthorizer;
import net.fhirfactory.harmonia.befe.server.OperationsServerManager;
import net.fhirfactory.harmonia.befe.service.ModuleStatusService;
import net.fhirfactory.harmonia.befe.service.OperationsAggregatorService;
import net.fhirfactory.harmonia.befe.service.TaskSequenceCacheService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class OperationsServerManagerIntegrationTest {

    private OperationsServerManager serverManager;
    private OperationsAggregatorService aggregatorService;
    private ThemisOperationsAuthorizer authorizer;
    private ModuleStatusService moduleStatusService;
    private TaskSequenceCacheService sequenceCacheService;
    private KubernetesInstanceProvider instanceProvider;
    private int testPort;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() throws Exception {
        moduleStatusService = new ModuleStatusService();
        instanceProvider = new KubernetesInstanceProvider(moduleStatusService);
        instanceProvider.init();

        sequenceCacheService = new TaskSequenceCacheService();
        sequenceCacheService.init();

        aggregatorService = new OperationsAggregatorService();
        aggregatorService.setModuleStatusService(moduleStatusService);
        aggregatorService.setInstanceProvider(instanceProvider);
        aggregatorService.setTaskSequenceCacheService(sequenceCacheService);
        aggregatorService.init();

        authorizer = new ThemisOperationsAuthorizer();

        TaskSequenceResource sequenceResource = new TaskSequenceResource();
        java.lang.reflect.Field seqField = TaskSequenceResource.class.getDeclaredField("sequenceCacheService");
        seqField.setAccessible(true);
        seqField.set(sequenceResource, sequenceCacheService);

        SystemStatusResource statusResource = new SystemStatusResource();
        statusResource.setTaskSequenceCacheService(sequenceCacheService);
        statusResource.setModuleStatusService(moduleStatusService);

        serverManager = new OperationsServerManager();
        serverManager.setPort(0);
        serverManager.setHost("127.0.0.1");
        serverManager.setModuleStatusService(moduleStatusService);
        serverManager.setSequenceCacheService(sequenceCacheService);
        serverManager.setSystemStatusResource(statusResource);
        serverManager.setTaskSequenceResource(sequenceResource);
        serverManager.setAggregatorService(aggregatorService);
        serverManager.setThemisAuthorizer(authorizer);

        serverManager.startServer();
        testPort = serverManager.getPort();
    }

    @AfterEach
    void tearDown() {
        if (serverManager != null) {
            serverManager.stopServer();
        }
    }

    private HttpURLConnection openConnection(String path, String method, Map<String, String> headers) throws Exception {
        URI uri = URI.create("http://127.0.0.1:" + testPort + path);
        HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
        conn.setRequestMethod(method);
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        return conn;
    }

    @Test
    @DisplayName("1. Operations Summary requires authentication (Default-Deny 403 on missing credentials)")
    void testSummaryUnauthenticatedFailsWith403() throws Exception {
        HttpURLConnection conn = openConnection("/api/operations/summary", "GET", Collections.emptyMap());
        int responseCode = conn.getResponseCode();
        assertThat(responseCode).isEqualTo(403);

        try (InputStream is = conn.getErrorStream()) {
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            assertThat(body).contains("Forbidden");
        }
    }

    @Test
    @DisplayName("2. Operations Summary rejects unauthorized role with 403")
    void testSummaryUnauthorizedRoleFailsWith403() throws Exception {
        Map<String, String> headers = Map.of(
                "X-Harmonia-User", "dr-smith",
                "X-Harmonia-Role", "CLINICAL_VIEWER"
        );
        HttpURLConnection conn = openConnection("/api/operations/summary", "GET", headers);
        int responseCode = conn.getResponseCode();
        assertThat(responseCode).isEqualTo(403);
    }

    @Test
    @DisplayName("3. Operations Summary succeeds with authorized OPS_VIEWER role")
    void testSummaryAuthorizedSucceeds() throws Exception {
        Map<String, String> headers = Map.of(
                "X-Harmonia-User", "ops-user",
                "X-Harmonia-Role", "OPS_VIEWER"
        );
        HttpURLConnection conn = openConnection("/api/operations/summary", "GET", headers);
        assertThat(conn.getResponseCode()).isEqualTo(200);

        try (InputStream is = conn.getInputStream()) {
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            JsonNode json = mapper.readTree(body);
            assertThat(json.has("platformStatus")).isTrue();
            assertThat(json.has("totalSubsystems")).isTrue();
            assertThat(json.get("totalSubsystems").asInt()).isEqualTo(9);
            assertThat(json.has("environment")).isTrue();
            assertThat(json.has("cluster")).isTrue();
        }
    }

    @Test
    @DisplayName("4. Subsystems perspective returns all 9 canonical subsystems and individual lookup")
    void testSubsystemsPerspective() throws Exception {
        Map<String, String> headers = Map.of("Authorization", "Bearer ops_admin_token");

        // List all subsystems
        HttpURLConnection conn = openConnection("/api/operations/subsystems", "GET", headers);
        assertThat(conn.getResponseCode()).isEqualTo(200);
        try (InputStream is = conn.getInputStream()) {
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            JsonNode list = mapper.readTree(body);
            assertThat(list.isArray()).isTrue();
            assertThat(list.size()).isEqualTo(9);
        }

        // Subsystem item lookup
        HttpURLConnection itemConn = openConnection("/api/operations/subsystems/petasos", "GET", headers);
        assertThat(itemConn.getResponseCode()).isEqualTo(200);
        try (InputStream is = itemConn.getInputStream()) {
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            JsonNode item = mapper.readTree(body);
            assertThat(item.get("id").asText()).isEqualTo("petasos");
            assertThat(item.get("name").asText()).isEqualTo("Petasos");
        }

        // Subsystem item 404
        HttpURLConnection notFoundConn = openConnection("/api/operations/subsystems/nonexistent-subsystem", "GET", headers);
        assertThat(notFoundConn.getResponseCode()).isEqualTo(404);
    }

    @Test
    @DisplayName("5. Subsystems instance discovery, operational health, and time-series statistics")
    void testSubsystemInstancesHealthAndStats() throws Exception {
        Map<String, String> headers = Map.of("X-Harmonia-Role", "OPS_ADM");

        // Instances
        HttpURLConnection instConn = openConnection("/api/operations/subsystems/petasos/instances", "GET", headers);
        assertThat(instConn.getResponseCode()).isEqualTo(200);
        try (InputStream is = instConn.getInputStream()) {
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            JsonNode instances = mapper.readTree(body);
            assertThat(instances.isArray()).isTrue();
            assertThat(instances.size()).isGreaterThanOrEqualTo(1);
        }

        // Health
        HttpURLConnection healthConn = openConnection("/api/operations/subsystems/petasos/health", "GET", headers);
        assertThat(healthConn.getResponseCode()).isEqualTo(200);
        try (InputStream is = healthConn.getInputStream()) {
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            JsonNode health = mapper.readTree(body);
            assertThat(health.has("status")).isTrue();
            assertThat(health.has("dependencies")).isTrue();
        }

        // Statistics with window
        HttpURLConnection statsConn = openConnection("/api/operations/subsystems/petasos/statistics?window=1h", "GET", headers);
        assertThat(statsConn.getResponseCode()).isEqualTo(200);
        try (InputStream is = statsConn.getInputStream()) {
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            JsonNode stats = mapper.readTree(body);
            assertThat(stats.has("enqueue_rate")).isTrue();
            assertThat(stats.get("enqueue_rate").get("window").asText()).isEqualTo("1h");
        }
    }

    @Test
    @DisplayName("6. Queues perspective lists queues, search filtering, and single queue item")
    void testQueuesPerspective() throws Exception {
        Map<String, String> headers = Map.of("Authorization", "Bearer ops_viewer_token");

        // List queues
        HttpURLConnection conn = openConnection("/api/operations/queues", "GET", headers);
        assertThat(conn.getResponseCode()).isEqualTo(200);
        try (InputStream is = conn.getInputStream()) {
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            JsonNode queues = mapper.readTree(body);
            assertThat(queues.isArray()).isTrue();
            assertThat(queues.size()).isGreaterThanOrEqualTo(5);
        }

        // Search queue
        HttpURLConnection searchConn = openConnection("/api/operations/queues?search=mllp", "GET", headers);
        assertThat(searchConn.getResponseCode()).isEqualTo(200);
        try (InputStream is = searchConn.getInputStream()) {
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            JsonNode queues = mapper.readTree(body);
            assertThat(queues.size()).isEqualTo(1);
            assertThat(queues.get(0).get("queueId").asText()).isEqualTo("petasos.queue.pylai.mllp.in");
        }

        // Single queue item
        HttpURLConnection itemConn = openConnection("/api/operations/queues/petasos.queue.ponos.dispatch", "GET", headers);
        assertThat(itemConn.getResponseCode()).isEqualTo(200);
        try (InputStream is = itemConn.getInputStream()) {
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            JsonNode queue = mapper.readTree(body);
            assertThat(queue.get("queueId").asText()).isEqualTo("petasos.queue.ponos.dispatch");
        }

        // Queue 404
        HttpURLConnection notFound = openConnection("/api/operations/queues/nonexistent-queue", "GET", headers);
        assertThat(notFound.getResponseCode()).isEqualTo(404);
    }

    @Test
    @DisplayName("7. Workflows & Pragmas perspective lists workflows and retrieves Pragma instances")
    void testWorkflowsAndPragmas() throws Exception {
        Map<String, String> headers = Map.of("X-Harmonia-Role", "SYS_ADM");

        // Workflows list
        HttpURLConnection conn = openConnection("/api/operations/workflows", "GET", headers);
        assertThat(conn.getResponseCode()).isEqualTo(200);
        try (InputStream is = conn.getInputStream()) {
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            JsonNode list = mapper.readTree(body);
            assertThat(list.isArray()).isTrue();
            assertThat(list.size()).isGreaterThanOrEqualTo(1);
            assertThat(list.get(0).get("workflowId").asText()).isEqualTo("seq-patient-identity-pipeline");
        }

        // Workflow item
        HttpURLConnection itemConn = openConnection("/api/operations/workflows/seq-patient-identity-pipeline", "GET", headers);
        assertThat(itemConn.getResponseCode()).isEqualTo(200);

        // Store a test Pragma and fetch it
        PragmaSummary pragma = new PragmaSummary(
                "pragma-test-101",
                "seq-patient-identity-pipeline",
                "COMPLETED",
                System.currentTimeMillis() - 500,
                500L,
                "completed",
                2,
                0,
                "corr-101",
                "caus-101",
                null
        );
        pragma.setCheckpoints(List.of(
                new ErgonCheckpoint("ergon-0", "Activity 0", "COMPLETED", System.currentTimeMillis() - 400, System.currentTimeMillis() - 200, 200L, null)
        ));
        aggregatorService.storePragma(pragma);

        HttpURLConnection pragmaConn = openConnection("/api/operations/pragmas/pragma-test-101", "GET", headers);
        assertThat(pragmaConn.getResponseCode()).isEqualTo(200);
        try (InputStream is = pragmaConn.getInputStream()) {
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            JsonNode pJson = mapper.readTree(body);
            assertThat(pJson.get("pragmaId").asText()).isEqualTo("pragma-test-101");
            assertThat(pJson.get("correlationId").asText()).isEqualTo("corr-101");
            assertThat(pJson.get("checkpoints").size()).isEqualTo(1);
        }

        // Workflow Pragmas drill-down list
        HttpURLConnection workflowPragmasConn = openConnection("/api/operations/workflows/seq-patient-identity-pipeline/pragmas", "GET", headers);
        assertThat(workflowPragmasConn.getResponseCode()).isEqualTo(200);
        try (InputStream is = workflowPragmasConn.getInputStream()) {
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            JsonNode pragmasList = mapper.readTree(body);
            assertThat(pragmasList.isArray()).isTrue();
            assertThat(pragmasList.size()).isEqualTo(1);
            assertThat(pragmasList.get(0).get("pragmaId").asText()).isEqualTo("pragma-test-101");
        }

        // Empty Workflow Pragmas drill-down list for workflow with no pragmas
        HttpURLConnection emptyPragmasConn = openConnection("/api/operations/workflows/seq-empty-pipeline/pragmas", "GET", headers);
        assertThat(emptyPragmasConn.getResponseCode()).isEqualTo(200);
        try (InputStream is = emptyPragmasConn.getInputStream()) {
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            JsonNode pragmasList = mapper.readTree(body);
            assertThat(pragmasList.isArray()).isTrue();
            assertThat(pragmasList.size()).isEqualTo(0);
        }

        // Pragma 404
        HttpURLConnection pragmaNotFound = openConnection("/api/operations/pragmas/nonexistent-pragma", "GET", headers);
        assertThat(pragmaNotFound.getResponseCode()).isEqualTo(404);
    }

    @Test
    @DisplayName("8. Events perspective with correlationId filtering, pagination, and single event lookup")
    void testEventsPerspective() throws Exception {
        Map<String, String> headers = Map.of("X-Harmonia-Role", "OPS_VIEWER");

        // Seed test operational events
        aggregatorService.recordEvent(new OperationalEvent(
                "evt-1", System.currentTimeMillis() - 3000, "pylai", "MLLP_INGRESS", "RECEIVE",
                "SUCCESS", 45L, "msg-1", "corr-abc-123", "caus-1", "pragma-1", "seq-1", "ergon-0", "mllp-in", null
        ));
        aggregatorService.recordEvent(new OperationalEvent(
                "evt-2", System.currentTimeMillis() - 2000, "petasos", "QUEUE_PUBLISH", "ENQUEUE",
                "SUCCESS", 12L, "msg-2", "corr-abc-123", "caus-1", "pragma-1", "seq-1", "ergon-0", "queue-in", null
        ));
        aggregatorService.recordEvent(new OperationalEvent(
                "evt-3", System.currentTimeMillis() - 1000, "energeia", "ACTIVITY_EXEC", "PROCESS",
                "SUCCESS", 80L, "msg-3", "corr-other-999", "caus-2", "pragma-2", "seq-2", "ergon-1", "activity-0", null
        ));

        // Filter by correlationId
        HttpURLConnection conn = openConnection("/api/operations/events?correlationId=corr-abc-123", "GET", headers);
        assertThat(conn.getResponseCode()).isEqualTo(200);
        try (InputStream is = conn.getInputStream()) {
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            JsonNode events = mapper.readTree(body);
            assertThat(events.size()).isEqualTo(2);
            assertThat(events.get(0).get("correlationId").asText()).isEqualTo("corr-abc-123");
        }

        // Pagination: page=0, pageSize=1
        HttpURLConnection pageConn = openConnection("/api/operations/events?pageSize=1", "GET", headers);
        assertThat(pageConn.getResponseCode()).isEqualTo(200);
        try (InputStream is = pageConn.getInputStream()) {
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            JsonNode events = mapper.readTree(body);
            assertThat(events.size()).isEqualTo(1);
        }

        // Single event lookup
        HttpURLConnection singleConn = openConnection("/api/operations/events/evt-1", "GET", headers);
        assertThat(singleConn.getResponseCode()).isEqualTo(200);
        try (InputStream is = singleConn.getInputStream()) {
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            JsonNode ev = mapper.readTree(body);
            assertThat(ev.get("eventId").asText()).isEqualTo("evt-1");
        }

        // Single event 404
        HttpURLConnection notFound = openConnection("/api/operations/events/evt-unknown", "GET", headers);
        assertThat(notFound.getResponseCode()).isEqualTo(404);
    }

    @Test
    @DisplayName("9. Alerts perspective and operator acknowledgement")
    void testAlertsAndAcknowledgement() throws Exception {
        Map<String, String> headers = Map.of("X-Harmonia-Role", "OPS_ADM");

        // Make petasos queue have DLQ messages to trigger an alert
        QueueSummary dlqQueue = new QueueSummary(
                "petasos.queue.dlq", "petasos.queue.dlq", "petasos.queue.dlq",
                "DEGRADED", 5L, 0, 0, 0.0, 0.0, 120L, 5L, 5L, 0L, "DLQ"
        );
        aggregatorService.registerQueue(dlqQueue);

        HttpURLConnection conn = openConnection("/api/operations/alerts", "GET", headers);
        assertThat(conn.getResponseCode()).isEqualTo(200);
        String alertIdToAck;
        try (InputStream is = conn.getInputStream()) {
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            JsonNode alerts = mapper.readTree(body);
            assertThat(alerts.size()).isGreaterThanOrEqualTo(1);
            alertIdToAck = alerts.get(0).get("alertId").asText();
        }

        // Acknowledge alert via POST
        HttpURLConnection ackConn = openConnection("/api/operations/alerts/" + alertIdToAck + "/acknowledge", "POST", headers);
        ackConn.setDoOutput(true);
        ackConn.setRequestProperty("Content-Type", "application/json");
        try (OutputStream os = ackConn.getOutputStream()) {
            os.write("{\"operator\":\"alice-sre\"}".getBytes(StandardCharsets.UTF_8));
        }

        assertThat(ackConn.getResponseCode()).isEqualTo(200);
        try (InputStream is = ackConn.getInputStream()) {
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            JsonNode ackJson = mapper.readTree(body);
            assertThat(ackJson.get("status").asText()).isEqualTo("ACKNOWLEDGED");
            assertThat(ackJson.get("acknowledgedBy").asText()).isEqualTo("alice-sre");
        }
    }

    @Test
    @DisplayName("10. Partial failure and graceful degradation returns UNKNOWN instead of HTTP 500")
    void testPartialFailureGracefulDegradation() throws Exception {
        // Register a faulty provider that throws an exception during getSubsystemOverview
        aggregatorService.registerProvider(new AbstractSubsystemHealthProvider(
                "faulty-subsystem", "Faulty", "Simulates telemetry failure", "1.0.0"
        ) {
            @Override
            public OperationalSubsystem getSubsystemOverview() {
                throw new RuntimeException("Simulated downstream timeout or failure");
            }

            @Override
            public OperationalHealth getOperationalHealth() {
                throw new RuntimeException("Downstream unreachable");
            }

            @Override
            public Map<String, TimeSeries> getStatistics(String window) {
                throw new RuntimeException("Downstream unreachable");
            }
        });

        Map<String, String> headers = Map.of("X-Harmonia-Role", "OPS_VIEWER");

        // The entire /subsystems list call must NOT crash with HTTP 500
        HttpURLConnection conn = openConnection("/api/operations/subsystems", "GET", headers);
        assertThat(conn.getResponseCode()).isEqualTo(200);

        try (InputStream is = conn.getInputStream()) {
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            JsonNode list = mapper.readTree(body);
            boolean foundFaulty = false;
            for (JsonNode sub : list) {
                if ("faulty-subsystem".equals(sub.get("id").asText())) {
                    foundFaulty = true;
                    // State must degrade gracefully to UNKNOWN
                    assertThat(sub.get("state").asText()).isEqualTo("UNKNOWN");
                }
            }
            assertThat(foundFaulty).isTrue();
        }

        // Direct lookup of faulty subsystem must also return UNKNOWN without 500
        HttpURLConnection faultyConn = openConnection("/api/operations/subsystems/faulty-subsystem", "GET", headers);
        assertThat(faultyConn.getResponseCode()).isEqualTo(200);
        try (InputStream is = faultyConn.getInputStream()) {
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            JsonNode sub = mapper.readTree(body);
            assertThat(sub.get("state").asText()).isEqualTo("UNKNOWN");
        }
    }

    @Test
    @DisplayName("11. Direct JAX-RS OperationsResource evaluation")
    void testOperationsResourceDirectly() {
        OperationsResource resource = new OperationsResource(aggregatorService, authorizer);

        // Test unauthenticated direct call -> 403
        var unauthResp = resource.getSummary(null);
        assertThat(unauthResp.getStatus()).isEqualTo(403);

        // Test with mock headers -> 200
        jakarta.ws.rs.core.HttpHeaders mockHeaders = new jakarta.ws.rs.core.HttpHeaders() {
            @Override
            public List<String> getRequestHeader(String name) {
                if ("X-Harmonia-Role".equalsIgnoreCase(name)) return List.of("OPS_VIEWER");
                if ("X-Harmonia-User".equalsIgnoreCase(name)) return List.of("ops-user");
                return Collections.emptyList();
            }

            @Override
            public String getHeaderString(String name) {
                List<String> vals = getRequestHeader(name);
                return (vals != null && !vals.isEmpty()) ? String.join(",", vals) : null;
            }

            @Override
            public jakarta.ws.rs.core.MultivaluedMap<String, String> getRequestHeaders() {
                var map = new jakarta.ws.rs.core.MultivaluedHashMap<String, String>();
                map.put("X-Harmonia-Role", List.of("OPS_VIEWER"));
                map.put("X-Harmonia-User", List.of("ops-user"));
                return map;
            }

            @Override public List<jakarta.ws.rs.core.MediaType> getAcceptableMediaTypes() { return List.of(); }
            @Override public List<java.util.Locale> getAcceptableLanguages() { return List.of(); }
            @Override public jakarta.ws.rs.core.MediaType getMediaType() { return null; }
            @Override public java.util.Locale getLanguage() { return null; }
            @Override public Map<String, jakarta.ws.rs.core.Cookie> getCookies() { return Map.of(); }
            @Override public java.util.Date getDate() { return null; }
            @Override public int getLength() { return 0; }
        };

        var authResp = resource.getSummary(mockHeaders);
        assertThat(authResp.getStatus()).isEqualTo(200);
        assertThat(authResp.getEntity()).isInstanceOf(OperationalSummary.class);
    }
}
