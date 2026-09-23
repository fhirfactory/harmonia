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

package net.fhirfactory.harmonia.befe.server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Destroyed;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import net.fhirfactory.harmonia.befe.config.BefeCorsConfig;
import net.fhirfactory.harmonia.befe.model.operations.*;
import net.fhirfactory.harmonia.befe.rest.SystemStatusResource;
import net.fhirfactory.harmonia.befe.rest.TaskSequenceResource;
import net.fhirfactory.harmonia.befe.security.ThemisOperationsAuthorizer;
import net.fhirfactory.harmonia.befe.service.ModuleStatusService;
import net.fhirfactory.harmonia.befe.service.OperationsAggregatorService;
import net.fhirfactory.harmonia.befe.service.TaskSequenceCacheService;
import net.fhirfactory.harmonia.model.status.ModuleStatus;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationDecision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Dedicated Operations HTTP Server for BEFE.
 * Exposes a distinct port (default 8090) specifically for Operations UI and administrative workflows,
 * isolating operational telemetry and pipeline control from clinical FHIR traffic.
 *
 * Implemented using core java.net.ServerSocket from java.base to guarantee 100% compatibility
 * across modular Jakarta EE classloaders without external or jdk.httpserver dependencies.
 */
@ApplicationScoped
public class OperationsServerManager {

    private static final Logger log = LoggerFactory.getLogger(OperationsServerManager.class);

    public static final int DEFAULT_OPERATIONS_PORT = 8090;
    public static final String DEFAULT_OPERATIONS_HOST = "0.0.0.0";

    @Inject
    private SystemStatusResource systemStatusResource;

    @Inject
    private TaskSequenceResource taskSequenceResource;

    @Inject
    private TaskSequenceCacheService sequenceCacheService;

    @Inject
    private ModuleStatusService moduleStatusService;

    @Inject
    private OperationsAggregatorService aggregatorService;

    @Inject
    private ThemisOperationsAuthorizer authorizer;

    private ObjectMapper objectMapper = new ObjectMapper();
    private static final ThreadLocal<String> CURRENT_ALLOWED_ORIGIN = new ThreadLocal<>();

    private ServerSocket serverSocket;
    private ExecutorService executor;
    private Thread acceptThread;
    private int port = DEFAULT_OPERATIONS_PORT;
    private String host = DEFAULT_OPERATIONS_HOST;
    private boolean portExplicitlySet = false;
    private boolean hostExplicitlySet = false;
    private volatile boolean running = false;

    public void onStartup(@Observes @Initialized(ApplicationScoped.class) Object init) {
        startServer();
    }

    @PostConstruct
    public void init() {
        startServer();
    }

    public synchronized void startServer() {
        if (running && serverSocket != null && !serverSocket.isClosed()) {
            return;
        }

        resolveConfiguration();

        try {
            log.info("Starting BEFE Operations HTTP Server on {}:{}...", host, port);
            InetAddress bindAddr = InetAddress.getByName(host);
            serverSocket = new ServerSocket(port, 100, bindAddr);
            this.port = serverSocket.getLocalPort();
            executor = Executors.newCachedThreadPool();
            running = true;

            acceptThread = new Thread(this::acceptConnections, "befe-operations-http");
            acceptThread.setDaemon(true);
            acceptThread.start();

            ModuleStatusService statusSvc = getModuleStatusService();
            if (statusSvc != null) {
                Map<String, Object> details = new HashMap<>();
                details.put("operationsPort", port);
                details.put("fhirPort", 8080);
                statusSvc.registerModule("befe", "BEFE Presentation Gateway", "PRESENTATION_SERVICE", "READY", true, details);
            }

            log.info("BEFE Operations HTTP Server started successfully on port {}.", this.port);
        } catch (Exception e) {
            log.error("Failed to start BEFE Operations HTTP Server on port {}: {}", port, e.getMessage(), e);
        }
    }

    private void acceptConnections() {
        while (running && serverSocket != null && !serverSocket.isClosed()) {
            try {
                Socket socket = serverSocket.accept();
                if (executor != null && !executor.isShutdown()) {
                    executor.submit(() -> handleClientSocket(socket));
                }
            } catch (Exception e) {
                if (running) {
                    log.debug("Operations HTTP Server accept exception: {}", e.getMessage());
                }
            }
        }
    }

    private void handleClientSocket(Socket socket) {
        try (socket;
             InputStream in = socket.getInputStream();
             OutputStream out = socket.getOutputStream()) {

            socket.setSoTimeout(10000);

            String requestLine = readLine(in);
            if (requestLine == null || requestLine.isBlank()) {
                return;
            }

            String[] reqParts = requestLine.trim().split("\\s+");
            if (reqParts.length < 2) {
                return;
            }

            String method = reqParts[0].toUpperCase();
            String fullUri = reqParts[1];

            // Parse headers
            Map<String, String> headers = new HashMap<>();
            String headerLine;
            while ((headerLine = readLine(in)) != null && !headerLine.isEmpty()) {
                int colonIdx = headerLine.indexOf(':');
                if (colonIdx > 0) {
                    String hKey = headerLine.substring(0, colonIdx).trim().toLowerCase();
                    String hVal = headerLine.substring(colonIdx + 1).trim();
                    headers.put(hKey, hVal);
                }
            }

            String originHeader = headers.get("origin");
            String allowedOrigin = null;
            if (originHeader != null && BefeCorsConfig.isOriginAllowed(originHeader)) {
                allowedOrigin = BefeCorsConfig.normalizeOrigin(originHeader);
            }
            CURRENT_ALLOWED_ORIGIN.set(allowedOrigin);
            try {
                // Read request body if Content-Length present
                int contentLength = 0;
                if (headers.containsKey("content-length")) {
                    try {
                        contentLength = Integer.parseInt(headers.get("content-length"));
                    } catch (NumberFormatException ignored) {}
                }

                String body = "";
                if (contentLength > 0) {
                    byte[] bodyBytes = readBody(in, contentLength);
                    body = new String(bodyBytes, StandardCharsets.UTF_8);
                }

                // Handle CORS OPTIONS preflight
                if ("OPTIONS".equals(method)) {
                    if (originHeader != null) {
                        if (allowedOrigin != null) {
                            sendPreflightResponse(out, 200, allowedOrigin);
                        } else {
                            sendResponse(out, 403, "{\"error\":\"Forbidden: Untrusted Origin\"}", "application/json");
                        }
                    } else {
                        sendResponse(out, 405, "{\"error\":\"Method Not Allowed\"}", "application/json");
                    }
                    return;
                }

            // Extract path without query parameters
            String path = fullUri;
            int qIdx = path.indexOf('?');
            if (qIdx >= 0) {
                path = path.substring(0, qIdx);
            }
            if (path.length() > 1 && path.endsWith("/")) {
                path = path.substring(0, path.length() - 1);
            }

            Map<String, String> queryParams = parseQueryParams(fullUri);

            // Route: Status endpoint
            if (path.equals("/api/operations/status") || path.equals("/operations/status") || path.equals("/status")) {
                handleStatus(out);
                return;
            }

            // Route: Modules status endpoint
            if (path.equals("/api/operations/modules") || path.equals("/operations/modules") || path.equals("/modules")) {
                handleModules(out);
                return;
            }

            // Route: Synchronize endpoint
            if (path.equals("/api/operations/sync") || path.equals("/operations/sync") || path.equals("/sync")
                    || path.equals("/api/operations/sequences/sync") || path.equals("/operations/sequences/sync")) {
                handleSync(out);
                return;
            }

            // Route: Task Sequences collection
            if (path.equals("/api/operations/sequences") || path.equals("/operations/sequences") || path.equals("/sequences")) {
                if ("GET".equals(method)) {
                    handleGetSequences(out);
                } else if ("POST".equals(method)) {
                    handleCreateSequence(out, body);
                } else {
                    sendResponse(out, 405, "{\"error\":\"Method Not Allowed\"}", "application/json");
                }
                return;
            }

            // Route: Task Sequences item by ID (/api/operations/sequences/{id})
            String sequencesPrefix1 = "/api/operations/sequences/";
            String sequencesPrefix2 = "/operations/sequences/";
            String sequencesPrefix3 = "/sequences/";

            String sequenceId = null;
            if (path.startsWith(sequencesPrefix1)) {
                sequenceId = path.substring(sequencesPrefix1.length());
            } else if (path.startsWith(sequencesPrefix2)) {
                sequenceId = path.substring(sequencesPrefix2.length());
            } else if (path.startsWith(sequencesPrefix3)) {
                sequenceId = path.substring(sequencesPrefix3.length());
            }

            if (sequenceId != null && !sequenceId.isBlank()) {
                handleSequenceItem(out, method, sequenceId, body);
                return;
            }

            // Route: Operational Resources collection (/api/operations/resources/{objectType})
            String resourcesPrefix1 = "/api/operations/resources/";
            String resourcesPrefix2 = "/operations/resources/";
            String resourcesPrefix3 = "/resources/";

            String resourceType = null;
            if (path.startsWith(resourcesPrefix1)) {
                resourceType = path.substring(resourcesPrefix1.length());
            } else if (path.startsWith(resourcesPrefix2)) {
                resourceType = path.substring(resourcesPrefix2.length());
            } else if (path.startsWith(resourcesPrefix3)) {
                resourceType = path.substring(resourcesPrefix3.length());
            } else if (path.equals("/api/operations/resources") || path.equals("/operations/resources") || path.equals("/resources")) {
                resourceType = "tasksequence";
            }

            if (resourceType != null) {
                handleGetOperationalResources(out, resourceType);
                return;
            }

            // Route: Operations Console 5 Perspectives (/api/operations/* and /operations/*)
            String normPath = path;
            if (normPath.startsWith("/api")) {
                normPath = normPath.substring(4);
            }

            if (isOperationsPerspectiveRoute(normPath)) {
                ThemisOperationsAuthorizer auth = getThemisAuthorizer();
                if (auth != null) {
                    ThemisAuthorizationDecision decision = auth.authorizeRequest(headers, path, method);
                    if (decision.isDenied()) {
                        String errJson = "{\"error\":\"Forbidden\",\"reason\":\"" + escapeJson(decision.reason().name()) + "\",\"message\":\"" + escapeJson(decision.message()) + "\"}";
                        sendResponse(out, 403, errJson, "application/json");
                        return;
                    }
                }
                handleOperationsRoute(out, method, normPath, queryParams, body);
                return;
            }

            // Root health check / fallback
            if (path.equals("/") || path.equals("/api") || path.equals("/api/operations") || path.equals("/operations")) {
                sendResponse(out, 200, "{\"service\":\"HIE BEFE Operations API\",\"status\":\"UP\",\"port\":" + port + "}", "application/json");
                return;
            }

            sendResponse(out, 404, "{\"error\":\"Not Found\"}", "application/json");

            } finally {
                CURRENT_ALLOWED_ORIGIN.remove();
            }
        } catch (Exception e) {
            log.error("Error processing operations socket request: {}", e.getMessage(), e);
        }
    }

    private void handleStatus(OutputStream out) throws IOException {
        SystemStatusResource statusRes = getSystemStatusResource();
        TaskSequenceCacheService cacheSvc = getSequenceCacheService();
        if (statusRes != null) {
            Response res = statusRes.getSystemStatus();
            Object entity = res.getEntity();
            String body = entity instanceof String ? (String) entity : entity != null ? entity.toString() : "{}";
            if (entity instanceof java.util.Map && cacheSvc != null) {
                try {
                    body = cacheSvc.getObjectMapper().writeValueAsString(entity);
                } catch (Exception ignored) {
                }
            }
            sendResponse(out, res.getStatus(), body, "application/json");
        } else {
            sendResponse(out, 200, "{\"status\":\"UP\",\"operationsPort\":" + port + "}", "application/json");
        }
    }

    private void handleModules(OutputStream out) throws IOException {
        ModuleStatusService statusSvc = getModuleStatusService();
        TaskSequenceCacheService cacheSvc = getSequenceCacheService();
        if (statusSvc != null && cacheSvc != null) {
            List<ModuleStatus> modules = statusSvc.getAllModuleStatuses();
            try {
                String body = cacheSvc.getObjectMapper().writeValueAsString(modules);
                sendResponse(out, 200, body, "application/json");
                return;
            } catch (Exception e) {
                log.debug("Failed to serialize modules: {}", e.getMessage());
            }
        }
        sendResponse(out, 200, "[]", "application/json");
    }

    private void handleSync(OutputStream out) throws IOException {
        TaskSequenceCacheService cacheSvc = getSequenceCacheService();
        if (cacheSvc != null) {
            try {
                cacheSvc.ensureDefaultSequences();
            } catch (Exception ignored) {
            }
        }

        // Forward to Task Sequence Processor sync endpoint
        String taskProcessorUrl = System.getenv().getOrDefault("TASK_PROCESSOR_URL", "http://task-processor:8080");
        String host = System.getenv().getOrDefault("TASK_PROCESSOR_HOST", "task-processor");
        String portStr = System.getenv().getOrDefault("TASK_PROCESSOR_PORT", "8080");
        if (System.getenv("TASK_PROCESSOR_URL") == null) {
            taskProcessorUrl = "http://" + host + ":" + portStr;
        }

        boolean processorNotified = false;
        try {
            java.net.http.HttpClient client = java.net.http.HttpClient.newBuilder()
                    .connectTimeout(java.time.Duration.ofSeconds(3))
                    .build();
            java.net.http.HttpRequest req = java.net.http.HttpRequest.newBuilder()
                    .uri(java.net.URI.create(taskProcessorUrl + "/workflow/sync"))
                    .header("Content-Type", "application/json")
                    .POST(java.net.http.HttpRequest.BodyPublishers.ofString("{}"))
                    .build();
            java.net.http.HttpResponse<String> resp = client.send(req, java.net.http.HttpResponse.BodyHandlers.ofString());
            processorNotified = (resp.statusCode() >= 200 && resp.statusCode() < 300);
        } catch (Exception e) {
            log.warn("Could not notify task-sequence-processor at {}: {}", taskProcessorUrl, e.getMessage());
        }

        String json = "{\"status\":\"SYNCHRONIZED\",\"timestamp\":\"" + java.time.Instant.now().toString() + "\",\"taskProcessorSynchronized\":" + processorNotified + ",\"message\":\"Task sequences and queues synchronization completed successfully.\"}";
        sendResponse(out, 200, json, "application/json");
    }

    private void handleGetSequences(OutputStream out) throws IOException {
        TaskSequenceResource seqRes = getTaskSequenceResource();
        TaskSequenceCacheService cacheSvc = getSequenceCacheService();
        if (seqRes != null) {
            Response res = seqRes.getAllSequences();
            String body = res.getEntity() != null ? res.getEntity().toString() : "[]";
            sendResponse(out, res.getStatus(), body, "application/json");
        } else if (cacheSvc != null) {
            var list = cacheSvc.getAllSequenceJsons();
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < list.size(); i++) {
                if (i > 0) sb.append(",");
                sb.append(list.get(i));
            }
            sb.append("]");
            sendResponse(out, 200, sb.toString(), "application/json");
        } else {
            sendResponse(out, 200, "[]", "application/json");
        }
    }

    private void handleCreateSequence(OutputStream out, String payload) throws IOException {
        TaskSequenceResource seqRes = getTaskSequenceResource();
        TaskSequenceCacheService cacheSvc = getSequenceCacheService();
        if (seqRes != null) {
            Response res = seqRes.createSequence(payload);
            String body = res.getEntity() != null ? res.getEntity().toString() : "{}";
            sendResponse(out, res.getStatus(), body, "application/json");
        } else if (cacheSvc != null) {
            String saved = cacheSvc.saveSequence(null, payload);
            sendResponse(out, 201, saved, "application/json");
        } else {
            sendResponse(out, 500, "{\"error\":\"Sequence service unavailable\"}", "application/json");
        }
    }

    private void handleSequenceItem(OutputStream out, String method, String id, String payload) throws IOException {
        TaskSequenceResource seqRes = getTaskSequenceResource();
        TaskSequenceCacheService cacheSvc = getSequenceCacheService();
        if ("GET".equals(method)) {
            if (seqRes != null) {
                Response res = seqRes.getSequenceById(id);
                String body = res.getEntity() != null ? res.getEntity().toString() : "{}";
                sendResponse(out, res.getStatus(), body, "application/json");
            } else if (cacheSvc != null) {
                var seq = cacheSvc.getSequenceJson(id);
                if (seq.isPresent()) {
                    sendResponse(out, 200, seq.get(), "application/json");
                } else {
                    sendResponse(out, 404, "{\"error\":\"TaskSequence not found\"}", "application/json");
                }
            } else {
                sendResponse(out, 404, "{\"error\":\"TaskSequence not found\"}", "application/json");
            }
        } else if ("PUT".equals(method)) {
            if (seqRes != null) {
                Response res = seqRes.updateSequence(id, payload);
                String body = res.getEntity() != null ? res.getEntity().toString() : "{}";
                sendResponse(out, res.getStatus(), body, "application/json");
            } else if (cacheSvc != null) {
                String saved = cacheSvc.saveSequence(id, payload);
                sendResponse(out, 200, saved, "application/json");
            } else {
                sendResponse(out, 500, "{\"error\":\"Sequence service unavailable\"}", "application/json");
            }
        } else if ("DELETE".equals(method)) {
            if (seqRes != null) {
                Response res = seqRes.deleteSequence(id);
                sendResponse(out, res.getStatus(), "", "application/json");
            } else if (cacheSvc != null) {
                boolean deleted = cacheSvc.deleteSequence(id);
                sendResponse(out, deleted ? 204 : 404, deleted ? "" : "{\"error\":\"TaskSequence not found\"}", "application/json");
            } else {
                sendResponse(out, 404, "{\"error\":\"TaskSequence not found\"}", "application/json");
            }
        } else {
            sendResponse(out, 405, "{\"error\":\"Method Not Allowed\"}", "application/json");
        }
    }

    private void handleGetOperationalResources(OutputStream out, String objectType) throws IOException {
        TaskSequenceCacheService cacheSvc = getSequenceCacheService();
        if ("tasksequence".equalsIgnoreCase(objectType) || "tasksequences".equalsIgnoreCase(objectType)) {
            List<String> list = cacheSvc != null ? cacheSvc.getAllSequenceJsons() : List.of();
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < list.size(); i++) {
                String json = list.get(i);
                String seqId = "seq-" + (i + 1);
                try {
                    if (cacheSvc != null) {
                        JsonNode node = cacheSvc.getObjectMapper().readTree(json);
                        if (node.has("sequenceId")) {
                            seqId = node.get("sequenceId").asText();
                        }
                    }
                } catch (Exception ignored) {}

                if (i > 0) sb.append(",");
                sb.append("{\"objectType\":\"tasksequence\",\"objectId\":\"").append(seqId).append("\",\"versionId\":1,\"dataJson\":");
                if (cacheSvc != null) {
                    sb.append(cacheSvc.getObjectMapper().writeValueAsString(json));
                } else {
                    sb.append("\"").append(json.replace("\"", "\\\"")).append("\"");
                }
                sb.append(",\"lastUpdated\":\"").append(java.time.Instant.now().toString()).append("\"}");
            }
            sb.append("]");
            sendResponse(out, 200, sb.toString(), "application/json");
        } else {
            sendResponse(out, 200, "[]", "application/json");
        }
    }

    private void sendPreflightResponse(OutputStream out, int statusCode, String allowedOrigin) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 ").append(statusCode).append(" OK\r\n");
        sb.append("Content-Length: 0\r\n");
        sb.append("Access-Control-Allow-Origin: ").append(allowedOrigin).append("\r\n");
        sb.append("Vary: Origin\r\n");
        sb.append("Access-Control-Allow-Methods: ").append(BefeCorsConfig.ALLOWED_METHODS).append("\r\n");
        sb.append("Access-Control-Allow-Headers: ").append(BefeCorsConfig.ALLOWED_HEADERS).append("\r\n");
        sb.append("Access-Control-Max-Age: ").append(BefeCorsConfig.MAX_AGE_SECONDS).append("\r\n");
        sb.append("Connection: close\r\n");
        sb.append("\r\n");

        out.write(sb.toString().getBytes(StandardCharsets.UTF_8));
        out.flush();
    }

    private void sendResponse(OutputStream out, int statusCode, String responseText, String contentType) throws IOException {
        byte[] bytes = responseText != null ? responseText.getBytes(StandardCharsets.UTF_8) : new byte[0];
        String statusText = switch (statusCode) {
            case 200 -> "OK";
            case 201 -> "Created";
            case 204 -> "No Content";
            case 400 -> "Bad Request";
            case 403 -> "Forbidden";
            case 404 -> "Not Found";
            case 405 -> "Method Not Allowed";
            default -> "Server Error";
        };

        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 ").append(statusCode).append(" ").append(statusText).append("\r\n");
        sb.append("Content-Type: ").append(contentType).append("\r\n");
        sb.append("Content-Length: ").append(bytes.length).append("\r\n");

        String allowedOrigin = CURRENT_ALLOWED_ORIGIN.get();
        if (allowedOrigin != null) {
            sb.append("Access-Control-Allow-Origin: ").append(allowedOrigin).append("\r\n");
            sb.append("Vary: Origin\r\n");
        }

        sb.append("Connection: close\r\n");
        sb.append("\r\n");

        out.write(sb.toString().getBytes(StandardCharsets.UTF_8));
        if (bytes.length > 0) {
            out.write(bytes);
        }
        out.flush();
    }

    private String readLine(InputStream in) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int b;
        while ((b = in.read()) != -1) {
            if (b == '\n') {
                break;
            }
            if (b != '\r') {
                baos.write(b);
            }
        }
        if (b == -1 && baos.size() == 0) {
            return null;
        }
        return baos.toString(StandardCharsets.UTF_8);
    }

    private byte[] readBody(InputStream in, int length) throws IOException {
        if (length <= 0) {
            return new byte[0];
        }
        byte[] buffer = new byte[length];
        int totalRead = 0;
        while (totalRead < length) {
            int read = in.read(buffer, totalRead, length - totalRead);
            if (read == -1) {
                break;
            }
            totalRead += read;
        }
        return buffer;
    }

    private void resolveConfiguration() {
        if (!portExplicitlySet) {
            String portStr = System.getProperty("befe.operations.port",
                    System.getenv().getOrDefault("OPERATIONS_PORT",
                            System.getenv().getOrDefault("BEFE_OPERATIONS_PORT", String.valueOf(DEFAULT_OPERATIONS_PORT))));
            try {
                this.port = Integer.parseInt(portStr.trim());
            } catch (NumberFormatException e) {
                log.warn("Invalid operations port '{}', falling back to {}", portStr, DEFAULT_OPERATIONS_PORT);
                this.port = DEFAULT_OPERATIONS_PORT;
            }
        }

        if (!hostExplicitlySet) {
            this.host = System.getProperty("befe.operations.host",
                    System.getenv().getOrDefault("OPERATIONS_HOST",
                            System.getenv().getOrDefault("BEFE_OPERATIONS_HOST", DEFAULT_OPERATIONS_HOST)));
        }
    }

    public void onShutdown(@Observes @Destroyed(ApplicationScoped.class) Object init) {
        stopServer();
    }

    @PreDestroy
    public void cleanup() {
        stopServer();
    }

    public synchronized void stopServer() {
        running = false;
        ModuleStatusService statusSvc = getModuleStatusService();
        if (statusSvc != null) {
            statusSvc.unregisterModule("befe");
        }
        if (serverSocket != null) {
            try {
                log.info("Stopping BEFE Operations HTTP Server on port {}...", port);
                serverSocket.close();
            } catch (Exception e) {
                log.warn("Error while closing BEFE Operations ServerSocket: {}", e.getMessage());
            }
        }
        if (executor != null) {
            try {
                executor.shutdownNow();
            } catch (Exception ignored) {
            }
        }
        log.info("BEFE Operations HTTP Server stopped.");
    }

    public boolean isRunning() {
        return running && serverSocket != null && !serverSocket.isClosed();
    }

    public int getPort() {
        if (serverSocket != null && !serverSocket.isClosed()) {
            return serverSocket.getLocalPort();
        }
        return port;
    }

    public void setPort(int port) {
        this.port = port;
        this.portExplicitlySet = true;
    }

    public void setHost(String host) {
        this.host = host;
        this.hostExplicitlySet = true;
    }

    public SystemStatusResource getSystemStatusResource() {
        if (systemStatusResource == null) {
            try {
                systemStatusResource = CDI.current().select(SystemStatusResource.class).get();
            } catch (Exception e) {
                log.debug("Could not resolve SystemStatusResource via CDI: {}", e.getMessage());
            }
        }
        return systemStatusResource;
    }

    public TaskSequenceResource getTaskSequenceResource() {
        if (taskSequenceResource == null) {
            try {
                taskSequenceResource = CDI.current().select(TaskSequenceResource.class).get();
            } catch (Exception e) {
                log.debug("Could not resolve TaskSequenceResource via CDI: {}", e.getMessage());
            }
        }
        return taskSequenceResource;
    }

    public TaskSequenceCacheService getSequenceCacheService() {
        if (sequenceCacheService == null) {
            try {
                sequenceCacheService = CDI.current().select(TaskSequenceCacheService.class).get();
            } catch (Exception e) {
                log.debug("Could not resolve TaskSequenceCacheService via CDI: {}", e.getMessage());
            }
        }
        return sequenceCacheService;
    }

    public ModuleStatusService getModuleStatusService() {
        if (moduleStatusService == null) {
            try {
                moduleStatusService = CDI.current().select(ModuleStatusService.class).get();
            } catch (Exception e) {
                log.debug("Could not resolve ModuleStatusService via CDI: {}", e.getMessage());
            }
        }
        return moduleStatusService;
    }

    public void setSystemStatusResource(SystemStatusResource systemStatusResource) {
        this.systemStatusResource = systemStatusResource;
    }

    public void setTaskSequenceResource(TaskSequenceResource taskSequenceResource) {
        this.taskSequenceResource = taskSequenceResource;
    }

    public void setSequenceCacheService(TaskSequenceCacheService sequenceCacheService) {
        this.sequenceCacheService = sequenceCacheService;
    }

    public void setModuleStatusService(ModuleStatusService moduleStatusService) {
        this.moduleStatusService = moduleStatusService;
    }

    public OperationsAggregatorService getAggregatorService() {
        if (aggregatorService == null) {
            try {
                aggregatorService = CDI.current().select(OperationsAggregatorService.class).get();
            } catch (Exception e) {
                log.debug("Could not resolve OperationsAggregatorService via CDI: {}", e.getMessage());
            }
            if (aggregatorService == null) {
                aggregatorService = new OperationsAggregatorService();
                aggregatorService.setModuleStatusService(getModuleStatusService());
                aggregatorService.setTaskSequenceCacheService(getSequenceCacheService());
                aggregatorService.init();
            }
        }
        return aggregatorService;
    }

    public void setAggregatorService(OperationsAggregatorService aggregatorService) {
        this.aggregatorService = aggregatorService;
    }

    public ThemisOperationsAuthorizer getThemisAuthorizer() {
        if (authorizer == null) {
            try {
                authorizer = CDI.current().select(ThemisOperationsAuthorizer.class).get();
            } catch (Exception e) {
                log.debug("Could not resolve ThemisOperationsAuthorizer via CDI: {}", e.getMessage());
            }
            if (authorizer == null) {
                authorizer = new ThemisOperationsAuthorizer();
            }
        }
        return authorizer;
    }

    public void setThemisAuthorizer(ThemisOperationsAuthorizer authorizer) {
        this.authorizer = authorizer;
    }

    public ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private boolean isOperationsPerspectiveRoute(String normPath) {
        return normPath.equals("/operations/summary") || normPath.equals("/summary")
                || normPath.equals("/operations/subsystems") || normPath.equals("/subsystems")
                || normPath.startsWith("/operations/subsystems/") || normPath.startsWith("/subsystems/")
                || normPath.equals("/operations/queues") || normPath.equals("/queues")
                || normPath.startsWith("/operations/queues/") || normPath.startsWith("/queues/")
                || normPath.equals("/operations/workflows") || normPath.equals("/workflows")
                || normPath.startsWith("/operations/workflows/") || normPath.startsWith("/workflows/")
                || normPath.startsWith("/operations/pragmas/") || normPath.startsWith("/pragmas/")
                || normPath.equals("/operations/events") || normPath.equals("/events")
                || normPath.startsWith("/operations/events/") || normPath.startsWith("/events/")
                || normPath.equals("/operations/alerts") || normPath.equals("/alerts")
                || normPath.startsWith("/operations/alerts/") || normPath.startsWith("/alerts/");
    }

    private void handleOperationsRoute(OutputStream out, String method, String normPath,
                                       Map<String, String> queryParams, String body) throws IOException {
        String opPath = normPath;
        if (opPath.startsWith("/operations")) {
            opPath = opPath.substring(11);
        }

        if (opPath.equals("/summary")) {
            handleOperationsSummary(out);
        } else if (opPath.equals("/subsystems")) {
            handleOperationsSubsystems(out);
        } else if (opPath.startsWith("/subsystems/")) {
            String remainder = opPath.substring("/subsystems/".length());
            if (remainder.contains("/")) {
                String[] parts = remainder.split("/", 2);
                String subId = parts[0];
                String subRes = parts[1];
                if (subRes.equals("instances")) {
                    handleOperationsSubsystemInstances(out, subId);
                } else if (subRes.equals("health")) {
                    handleOperationsSubsystemHealth(out, subId);
                } else if (subRes.equals("statistics")) {
                    handleOperationsSubsystemStatistics(out, subId, queryParams);
                } else {
                    sendResponse(out, 404, "{\"error\":\"Not Found\"}", "application/json");
                }
            } else {
                handleOperationsSubsystemItem(out, remainder);
            }
        } else if (opPath.equals("/queues")) {
            handleOperationsQueues(out, queryParams);
        } else if (opPath.startsWith("/queues/")) {
            String queueId = opPath.substring("/queues/".length());
            handleOperationsQueueItem(out, queueId);
        } else if (opPath.equals("/workflows")) {
            handleOperationsWorkflows(out, queryParams);
        } else if (opPath.startsWith("/workflows/")) {
            String remainder = opPath.substring("/workflows/".length());
            if (remainder.endsWith("/pragmas")) {
                String workflowId = remainder.substring(0, remainder.length() - "/pragmas".length());
                handleOperationsWorkflowPragmas(out, workflowId);
            } else {
                handleOperationsWorkflowItem(out, remainder);
            }
        } else if (opPath.startsWith("/pragmas/")) {
            String pragmaId = opPath.substring("/pragmas/".length());
            handleOperationsPragmaItem(out, pragmaId);
        } else if (opPath.equals("/events")) {
            handleOperationsEvents(out, queryParams);
        } else if (opPath.startsWith("/events/")) {
            String eventId = opPath.substring("/events/".length());
            handleOperationsEventItem(out, eventId);
        } else if (opPath.equals("/alerts")) {
            handleOperationsAlerts(out, queryParams);
        } else if (opPath.startsWith("/alerts/")) {
            String remainder = opPath.substring("/alerts/".length());
            if (remainder.endsWith("/acknowledge")) {
                String alertId = remainder.substring(0, remainder.length() - "/acknowledge".length());
                handleOperationsAlertAcknowledge(out, method, alertId, body);
            } else {
                sendResponse(out, 404, "{\"error\":\"Not Found\"}", "application/json");
            }
        } else {
            sendResponse(out, 404, "{\"error\":\"Not Found\"}", "application/json");
        }
    }

    private void handleOperationsSummary(OutputStream out) throws IOException {
        try {
            OperationalSummary summary = getAggregatorService().getOperationsSummary();
            sendResponse(out, 200, getObjectMapper().writeValueAsString(summary), "application/json");
        } catch (Exception e) {
            log.error("Failed to retrieve operations summary: {}", e.getMessage(), e);
            sendResponse(out, 500, "{\"error\":\"Internal Server Error\",\"message\":\"" + escapeJson(e.getMessage()) + "\"}", "application/json");
        }
    }

    private void handleOperationsSubsystems(OutputStream out) throws IOException {
        try {
            List<OperationalSubsystem> subsystems = getAggregatorService().getSubsystems();
            sendResponse(out, 200, getObjectMapper().writeValueAsString(subsystems), "application/json");
        } catch (Exception e) {
            log.error("Failed to retrieve operational subsystems: {}", e.getMessage(), e);
            sendResponse(out, 500, "{\"error\":\"Internal Server Error\",\"message\":\"" + escapeJson(e.getMessage()) + "\"}", "application/json");
        }
    }

    private void handleOperationsSubsystemItem(OutputStream out, String subId) throws IOException {
        try {
            Optional<OperationalSubsystem> sub = getAggregatorService().getSubsystem(subId);
            if (sub.isPresent()) {
                sendResponse(out, 200, getObjectMapper().writeValueAsString(sub.get()), "application/json");
            } else {
                sendResponse(out, 404, "{\"error\":\"Subsystem not found\",\"subsystemId\":\"" + escapeJson(subId) + "\"}", "application/json");
            }
        } catch (Exception e) {
            log.error("Failed to retrieve subsystem [{}]: {}", subId, e.getMessage(), e);
            sendResponse(out, 500, "{\"error\":\"Internal Server Error\",\"message\":\"" + escapeJson(e.getMessage()) + "\"}", "application/json");
        }
    }

    private void handleOperationsSubsystemInstances(OutputStream out, String subId) throws IOException {
        try {
            List<OperationalInstance> instances = getAggregatorService().getSubsystemInstances(subId);
            sendResponse(out, 200, getObjectMapper().writeValueAsString(instances), "application/json");
        } catch (Exception e) {
            log.error("Failed to retrieve instances for [{}]: {}", subId, e.getMessage(), e);
            sendResponse(out, 500, "{\"error\":\"Internal Server Error\",\"message\":\"" + escapeJson(e.getMessage()) + "\"}", "application/json");
        }
    }

    private void handleOperationsSubsystemHealth(OutputStream out, String subId) throws IOException {
        try {
            OperationalHealth health = getAggregatorService().getSubsystemHealth(subId);
            sendResponse(out, 200, getObjectMapper().writeValueAsString(health), "application/json");
        } catch (Exception e) {
            log.error("Failed to retrieve health for [{}]: {}", subId, e.getMessage(), e);
            sendResponse(out, 500, "{\"error\":\"Internal Server Error\",\"message\":\"" + escapeJson(e.getMessage()) + "\"}", "application/json");
        }
    }

    private void handleOperationsSubsystemStatistics(OutputStream out, String subId, Map<String, String> queryParams) throws IOException {
        try {
            String window = queryParams.getOrDefault("window", "15m");
            Map<String, TimeSeries> stats = getAggregatorService().getSubsystemStatistics(subId, window);
            sendResponse(out, 200, getObjectMapper().writeValueAsString(stats), "application/json");
        } catch (Exception e) {
            log.error("Failed to retrieve statistics for [{}]: {}", subId, e.getMessage(), e);
            sendResponse(out, 500, "{\"error\":\"Internal Server Error\",\"message\":\"" + escapeJson(e.getMessage()) + "\"}", "application/json");
        }
    }

    private void handleOperationsQueues(OutputStream out, Map<String, String> queryParams) throws IOException {
        try {
            String status = queryParams.get("status");
            String search = queryParams.get("search");
            List<QueueSummary> queues = getAggregatorService().getQueues(status, search);
            sendResponse(out, 200, getObjectMapper().writeValueAsString(queues), "application/json");
        } catch (Exception e) {
            log.error("Failed to retrieve queues: {}", e.getMessage(), e);
            sendResponse(out, 500, "{\"error\":\"Internal Server Error\",\"message\":\"" + escapeJson(e.getMessage()) + "\"}", "application/json");
        }
    }

    private void handleOperationsQueueItem(OutputStream out, String queueId) throws IOException {
        try {
            Optional<QueueSummary> q = getAggregatorService().getQueue(queueId);
            if (q.isPresent()) {
                sendResponse(out, 200, getObjectMapper().writeValueAsString(q.get()), "application/json");
            } else {
                sendResponse(out, 404, "{\"error\":\"Queue not found\",\"queueId\":\"" + escapeJson(queueId) + "\"}", "application/json");
            }
        } catch (Exception e) {
            log.error("Failed to retrieve queue [{}]: {}", queueId, e.getMessage(), e);
            sendResponse(out, 500, "{\"error\":\"Internal Server Error\",\"message\":\"" + escapeJson(e.getMessage()) + "\"}", "application/json");
        }
    }

    private void handleOperationsWorkflows(OutputStream out, Map<String, String> queryParams) throws IOException {
        try {
            String search = queryParams.get("search");
            List<WorkflowSummary> workflows = getAggregatorService().getWorkflows(search);
            sendResponse(out, 200, getObjectMapper().writeValueAsString(workflows), "application/json");
        } catch (Exception e) {
            log.error("Failed to retrieve workflows: {}", e.getMessage(), e);
            sendResponse(out, 500, "{\"error\":\"Internal Server Error\",\"message\":\"" + escapeJson(e.getMessage()) + "\"}", "application/json");
        }
    }

    private void handleOperationsWorkflowItem(OutputStream out, String workflowId) throws IOException {
        try {
            Optional<WorkflowSummary> w = getAggregatorService().getWorkflow(workflowId);
            if (w.isPresent()) {
                sendResponse(out, 200, getObjectMapper().writeValueAsString(w.get()), "application/json");
            } else {
                sendResponse(out, 404, "{\"error\":\"Workflow not found\",\"workflowId\":\"" + escapeJson(workflowId) + "\"}", "application/json");
            }
        } catch (Exception e) {
            log.error("Failed to retrieve workflow [{}]: {}", workflowId, e.getMessage(), e);
            sendResponse(out, 500, "{\"error\":\"Internal Server Error\",\"message\":\"" + escapeJson(e.getMessage()) + "\"}", "application/json");
        }
    }

    private void handleOperationsWorkflowPragmas(OutputStream out, String workflowId) throws IOException {
        try {
            List<PragmaSummary> pragmas = getAggregatorService().getPragmasForWorkflow(workflowId);
            sendResponse(out, 200, getObjectMapper().writeValueAsString(pragmas), "application/json");
        } catch (Exception e) {
            log.error("Failed to retrieve pragmas for workflow [{}]: {}", workflowId, e.getMessage(), e);
            sendResponse(out, 500, "{\"error\":\"Internal Server Error\",\"message\":\"" + escapeJson(e.getMessage()) + "\"}", "application/json");
        }
    }

    private void handleOperationsPragmaItem(OutputStream out, String pragmaId) throws IOException {
        try {
            Optional<PragmaSummary> p = getAggregatorService().getPragma(pragmaId);
            if (p.isPresent()) {
                sendResponse(out, 200, getObjectMapper().writeValueAsString(p.get()), "application/json");
            } else {
                sendResponse(out, 404, "{\"error\":\"Pragma not found\",\"pragmaId\":\"" + escapeJson(pragmaId) + "\"}", "application/json");
            }
        } catch (Exception e) {
            log.error("Failed to retrieve pragma [{}]: {}", pragmaId, e.getMessage(), e);
            sendResponse(out, 500, "{\"error\":\"Internal Server Error\",\"message\":\"" + escapeJson(e.getMessage()) + "\"}", "application/json");
        }
    }

    private void handleOperationsEvents(OutputStream out, Map<String, String> queryParams) throws IOException {
        try {
            String correlationId = queryParams.get("correlationId");
            String causationId = queryParams.get("causationId");
            String messageId = queryParams.get("messageId");
            String pragmaId = queryParams.get("pragmaId");
            String subsystem = queryParams.get("subsystem");
            String eventType = queryParams.get("eventType");
            String status = queryParams.get("status");
            Long from = parseLongSafe(queryParams.get("from"));
            Long to = parseLongSafe(queryParams.get("to"));
            int page = parseIntSafe(queryParams.get("page"), 0);
            int pageSize = parseIntSafe(queryParams.get("pageSize"), 50);

            List<OperationalEvent> events = getAggregatorService().getEvents(
                    correlationId, causationId, messageId, pragmaId, subsystem, eventType, status, from, to, page, pageSize
            );
            sendResponse(out, 200, getObjectMapper().writeValueAsString(events), "application/json");
        } catch (Exception e) {
            log.error("Failed to retrieve operational events: {}", e.getMessage(), e);
            sendResponse(out, 500, "{\"error\":\"Internal Server Error\",\"message\":\"" + escapeJson(e.getMessage()) + "\"}", "application/json");
        }
    }

    private void handleOperationsEventItem(OutputStream out, String eventId) throws IOException {
        try {
            Optional<OperationalEvent> ev = getAggregatorService().getEvent(eventId);
            if (ev.isPresent()) {
                sendResponse(out, 200, getObjectMapper().writeValueAsString(ev.get()), "application/json");
            } else {
                sendResponse(out, 404, "{\"error\":\"Event not found\",\"eventId\":\"" + escapeJson(eventId) + "\"}", "application/json");
            }
        } catch (Exception e) {
            log.error("Failed to retrieve event [{}]: {}", eventId, e.getMessage(), e);
            sendResponse(out, 500, "{\"error\":\"Internal Server Error\",\"message\":\"" + escapeJson(e.getMessage()) + "\"}", "application/json");
        }
    }

    private void handleOperationsAlerts(OutputStream out, Map<String, String> queryParams) throws IOException {
        try {
            String severity = queryParams.get("severity");
            String status = queryParams.get("status");
            String subsystem = queryParams.get("subsystem");
            List<OperationalAlert> alerts = getAggregatorService().getAlerts(severity, status, subsystem);
            sendResponse(out, 200, getObjectMapper().writeValueAsString(alerts), "application/json");
        } catch (Exception e) {
            log.error("Failed to retrieve operational alerts: {}", e.getMessage(), e);
            sendResponse(out, 500, "{\"error\":\"Internal Server Error\",\"message\":\"" + escapeJson(e.getMessage()) + "\"}", "application/json");
        }
    }

    private void handleOperationsAlertAcknowledge(OutputStream out, String method, String alertId, String body) throws IOException {
        if (!"POST".equalsIgnoreCase(method)) {
            sendResponse(out, 405, "{\"error\":\"Method Not Allowed\"}", "application/json");
            return;
        }
        try {
            String operator = "operator";
            if (body != null && !body.isBlank()) {
                try {
                    JsonNode node = getObjectMapper().readTree(body);
                    if (node.has("operator")) {
                        operator = node.get("operator").asText();
                    }
                } catch (Exception ignored) {}
            }
            boolean success = getAggregatorService().acknowledgeAlert(alertId, operator);
            if (success) {
                sendResponse(out, 200, "{\"alertId\":\"" + escapeJson(alertId) + "\",\"status\":\"ACKNOWLEDGED\",\"acknowledgedBy\":\"" + escapeJson(operator) + "\"}", "application/json");
            } else {
                sendResponse(out, 404, "{\"error\":\"Alert not found\",\"alertId\":\"" + escapeJson(alertId) + "\"}", "application/json");
            }
        } catch (Exception e) {
            log.error("Failed acknowledging alert [{}]: {}", alertId, e.getMessage(), e);
            sendResponse(out, 500, "{\"error\":\"Internal Server Error\",\"message\":\"" + escapeJson(e.getMessage()) + "\"}", "application/json");
        }
    }

    private Map<String, String> parseQueryParams(String fullUri) {
        Map<String, String> queryParams = new HashMap<>();
        int qIdx = fullUri.indexOf('?');
        if (qIdx >= 0 && qIdx < fullUri.length() - 1) {
            String qStr = fullUri.substring(qIdx + 1);
            for (String pair : qStr.split("&")) {
                int eqIdx = pair.indexOf('=');
                if (eqIdx > 0) {
                    try {
                        String key = URLDecoder.decode(pair.substring(0, eqIdx), StandardCharsets.UTF_8);
                        String val = URLDecoder.decode(pair.substring(eqIdx + 1), StandardCharsets.UTF_8);
                        queryParams.put(key, val);
                    } catch (Exception ignored) {}
                } else if (!pair.isBlank()) {
                    try {
                        queryParams.put(java.net.URLDecoder.decode(pair, StandardCharsets.UTF_8), "");
                    } catch (Exception ignored) {}
                }
            }
        }
        return queryParams;
    }

    private Long parseLongSafe(String s) {
        if (s == null || s.isBlank()) return null;
        try {
            return Long.parseLong(s.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private int parseIntSafe(String s, int defaultVal) {
        if (s == null || s.isBlank()) return defaultVal;
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    private String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }
}
