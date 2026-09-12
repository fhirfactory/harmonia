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

package net.fhirfactory.hie.befe.server;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Destroyed;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import net.fhirfactory.hie.befe.rest.SystemStatusResource;
import net.fhirfactory.hie.befe.rest.TaskSequenceResource;
import net.fhirfactory.hie.befe.service.ModuleStatusService;
import net.fhirfactory.hie.befe.service.TaskSequenceCacheService;
import net.fhirfactory.hie.model.status.ModuleStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
                sendResponse(out, 200, "", "application/json");
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

            // Root health check / fallback
            if (path.equals("/") || path.equals("/api") || path.equals("/api/operations") || path.equals("/operations")) {
                sendResponse(out, 200, "{\"service\":\"HIE BEFE Operations API\",\"status\":\"UP\",\"port\":" + port + "}", "application/json");
                return;
            }

            sendResponse(out, 404, "{\"error\":\"Not Found\"}", "application/json");

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

    private void sendResponse(OutputStream out, int statusCode, String responseText, String contentType) throws IOException {
        byte[] bytes = responseText != null ? responseText.getBytes(StandardCharsets.UTF_8) : new byte[0];
        String statusText = switch (statusCode) {
            case 200 -> "OK";
            case 201 -> "Created";
            case 204 -> "No Content";
            case 400 -> "Bad Request";
            case 404 -> "Not Found";
            case 405 -> "Method Not Allowed";
            default -> "Server Error";
        };

        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 ").append(statusCode).append(" ").append(statusText).append("\r\n");
        sb.append("Content-Type: ").append(contentType).append("\r\n");
        sb.append("Content-Length: ").append(bytes.length).append("\r\n");
        sb.append("Access-Control-Allow-Origin: *\r\n");
        sb.append("Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS, HEAD, PATCH\r\n");
        sb.append("Access-Control-Allow-Headers: origin, content-type, accept, authorization, x-requested-with\r\n");
        sb.append("Access-Control-Max-Age: 1209600\r\n");
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
}
