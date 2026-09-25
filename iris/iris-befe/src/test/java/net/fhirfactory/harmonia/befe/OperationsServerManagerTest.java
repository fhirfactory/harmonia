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

import net.fhirfactory.harmonia.befe.config.BefeCorsConfig;
import net.fhirfactory.harmonia.befe.rest.SystemStatusResource;
import net.fhirfactory.harmonia.befe.rest.TaskSequenceResource;
import net.fhirfactory.harmonia.befe.server.OperationsServerManager;
import net.fhirfactory.harmonia.befe.service.FhirCacheService;
import net.fhirfactory.harmonia.befe.service.ModuleStatusService;
import net.fhirfactory.harmonia.befe.service.TaskSequenceCacheService;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.commons.util.CloseableIterator;
import org.infinispan.commons.util.CloseableIteratorCollection;
import org.infinispan.commons.util.CloseableIteratorSet;
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
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class OperationsServerManagerTest {

    static {
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
    }

    private OperationsServerManager serverManager;
    private TaskSequenceCacheService sequenceCacheService;
    private TaskSequenceResource sequenceResource;
    private SystemStatusResource statusResource;
    private ModuleStatusService moduleStatusService;
    private RemoteCacheManager mockCacheManager;
    private Map<String, Map<String, String>> mockStore;
    private int testPort;

    private static <T> CloseableIterator<T> toCloseableIterator(Iterator<T> iterator) {
        return new CloseableIterator<T>() {
            @Override
            public void close() {}

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public T next() {
                return iterator.next();
            }
        };
    }

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() throws Exception {
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        BefeCorsConfig.setAllowedOriginsOverrideForTesting("https://clinical.harmonia.local,https://console.harmonia.local");

        mockStore = new ConcurrentHashMap<>();
        mockCacheManager = mock(RemoteCacheManager.class);
        when(mockCacheManager.isStarted()).thenReturn(true);
        when(mockCacheManager.getCache(anyString())).thenAnswer(inv -> {
            String cacheName = inv.getArgument(0);
            Map<String, String> cacheMap = mockStore.computeIfAbsent(cacheName, k -> new ConcurrentHashMap<>());
            RemoteCache<String, String> mockCache = mock(RemoteCache.class);
            when(mockCache.get(anyString())).thenAnswer(i -> cacheMap.get(i.getArgument(0)));
            when(mockCache.put(anyString(), anyString())).thenAnswer(i -> cacheMap.put(i.getArgument(0), i.getArgument(1)));
            when(mockCache.remove(anyString())).thenAnswer(i -> cacheMap.remove(i.getArgument(0)));

            CloseableIteratorCollection<String> mockValues = mock(CloseableIteratorCollection.class);
            when(mockValues.iterator()).thenAnswer(i -> toCloseableIterator(cacheMap.values().iterator()));
            when(mockValues.stream()).thenAnswer(i -> cacheMap.values().stream());
            when(mockValues.isEmpty()).thenAnswer(i -> cacheMap.isEmpty());
            when(mockValues.size()).thenAnswer(i -> cacheMap.size());
            doReturn(mockValues).when(mockCache).values();

            CloseableIteratorSet<String> mockKeys = mock(CloseableIteratorSet.class);
            when(mockKeys.iterator()).thenAnswer(i -> toCloseableIterator(cacheMap.keySet().iterator()));
            when(mockKeys.stream()).thenAnswer(i -> cacheMap.keySet().stream());
            when(mockKeys.isEmpty()).thenAnswer(i -> cacheMap.isEmpty());
            when(mockKeys.size()).thenAnswer(i -> cacheMap.size());
            doReturn(mockKeys).when(mockCache).keySet();

            when(mockCache.size()).thenAnswer(i -> cacheMap.size());
            when(mockCache.isEmpty()).thenAnswer(i -> cacheMap.isEmpty());
            return mockCache;
        });

        sequenceCacheService = new TaskSequenceCacheService(mockCacheManager);
        sequenceCacheService.init();

        moduleStatusService = new ModuleStatusService(mockCacheManager);

        sequenceResource = new TaskSequenceResource();
        java.lang.reflect.Field seqField = TaskSequenceResource.class.getDeclaredField("sequenceCacheService");
        seqField.setAccessible(true);
        seqField.set(sequenceResource, sequenceCacheService);

        statusResource = new SystemStatusResource();
        statusResource.setTaskSequenceCacheService(sequenceCacheService);
        statusResource.setModuleStatusService(moduleStatusService);
        FhirCacheService fhirCacheService = new FhirCacheService(mockCacheManager);
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
        BefeCorsConfig.resetAllowedOriginsForTesting();
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
    @DisplayName("3. Operations HTTP Server supports CORS preflight and hardened origin validation")
    void testCorsPreflight() throws Exception {
        // 1. Trusted origin preflight
        URI uri = URI.create("http://127.0.0.1:" + testPort + "/api/operations/sequences");
        HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
        conn.setRequestMethod("OPTIONS");
        conn.setRequestProperty("Origin", "https://clinical.harmonia.local");
        conn.connect();

        assertThat(conn.getResponseCode()).isEqualTo(200);
        assertThat(conn.getHeaderField("Access-Control-Allow-Origin")).isEqualTo("https://clinical.harmonia.local");
        assertThat(conn.getHeaderField("Vary")).isEqualTo("Origin");
        assertThat(conn.getHeaderField("Access-Control-Allow-Methods")).contains("GET", "POST", "PUT", "DELETE", "OPTIONS");
        assertThat(conn.getHeaderField("Access-Control-Allow-Headers")).contains("Authorization", "Content-Type");
        assertThat(conn.getHeaderField("Access-Control-Max-Age")).isEqualTo("86400");
        assertThat(conn.getHeaderField("Access-Control-Allow-Credentials")).isNull();

        // 2. Untrusted origin preflight is rejected with 403 Forbidden
        HttpURLConnection untrustedConn = (HttpURLConnection) uri.toURL().openConnection();
        untrustedConn.setRequestMethod("OPTIONS");
        untrustedConn.setRequestProperty("Origin", "https://attacker.example");
        untrustedConn.connect();

        assertThat(untrustedConn.getResponseCode()).isEqualTo(403);
        assertThat(untrustedConn.getHeaderField("Access-Control-Allow-Origin")).isNull();

        // 3. Non-CORS OPTIONS request receives 405 Method Not Allowed
        HttpURLConnection nonCorsConn = (HttpURLConnection) uri.toURL().openConnection();
        nonCorsConn.setRequestMethod("OPTIONS");
        nonCorsConn.connect();

        assertThat(nonCorsConn.getResponseCode()).isEqualTo(405);
        assertThat(nonCorsConn.getHeaderField("Access-Control-Allow-Origin")).isNull();

        // 4. Trusted origin GET receives exact origin and Vary: Origin
        URI statusUri = URI.create("http://127.0.0.1:" + testPort + "/api/operations/status");
        HttpURLConnection getTrustedConn = (HttpURLConnection) statusUri.toURL().openConnection();
        getTrustedConn.setRequestMethod("GET");
        getTrustedConn.setRequestProperty("Origin", "https://clinical.harmonia.local");
        getTrustedConn.connect();

        assertThat(getTrustedConn.getResponseCode()).isEqualTo(200);
        assertThat(getTrustedConn.getHeaderField("Access-Control-Allow-Origin")).isEqualTo("https://clinical.harmonia.local");
        assertThat(getTrustedConn.getHeaderField("Vary")).isEqualTo("Origin");
        assertThat(getTrustedConn.getHeaderField("Access-Control-Allow-Credentials")).isNull();

        // 5. Untrusted origin GET receives response without Access-Control-Allow-Origin
        HttpURLConnection getUntrustedConn = (HttpURLConnection) statusUri.toURL().openConnection();
        getUntrustedConn.setRequestMethod("GET");
        getUntrustedConn.setRequestProperty("Origin", "https://attacker.example");
        getUntrustedConn.connect();

        assertThat(getUntrustedConn.getResponseCode()).isEqualTo(200);
        assertThat(getUntrustedConn.getHeaderField("Access-Control-Allow-Origin")).isNull();

        // 6. Non-CORS GET receives response without Access-Control-Allow-Origin
        HttpURLConnection getNonCorsConn = (HttpURLConnection) statusUri.toURL().openConnection();
        getNonCorsConn.setRequestMethod("GET");
        getNonCorsConn.connect();

        assertThat(getNonCorsConn.getResponseCode()).isEqualTo(200);
        assertThat(getNonCorsConn.getHeaderField("Access-Control-Allow-Origin")).isNull();
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
