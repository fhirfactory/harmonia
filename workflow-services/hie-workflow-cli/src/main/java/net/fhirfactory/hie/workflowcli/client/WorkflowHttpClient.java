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

package net.fhirfactory.hie.workflowcli.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * HTTP Client connecting to the Task Sequence Processor runtime.
 */
public class WorkflowHttpClient {

    private static final Logger log = LoggerFactory.getLogger(WorkflowHttpClient.class);

    private final String serverUrl;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public WorkflowHttpClient(String serverUrl) {
        this(serverUrl, 10);
    }

    public WorkflowHttpClient(String serverUrl, int timeoutSeconds) {
        String base = serverUrl != null && !serverUrl.isBlank() ? serverUrl : "http://localhost:8083";
        this.serverUrl = base.endsWith("/") ? base.substring(0, base.length() - 1) : base;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(timeoutSeconds))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    public WorkflowHttpClient(String serverUrl, HttpClient httpClient, ObjectMapper objectMapper) {
        String base = serverUrl != null && !serverUrl.isBlank() ? serverUrl : "http://localhost:8083";
        this.serverUrl = base.endsWith("/") ? base.substring(0, base.length() - 1) : base;
        this.httpClient = httpClient;
        this.objectMapper = objectMapper != null ? objectMapper : new ObjectMapper().registerModule(new JavaTimeModule());
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    /**
     * Triggers full reload and synchronization of message queues and task sequences.
     */
    public Map<String, Object> reloadAll() throws IOException, InterruptedException {
        return sendPostRequest("/workflow/reload", "/api/workflow/reload", "/queue/reload", "/api/queue/reload");
    }

    /**
     * Triggers validation of queue names and task-sequence definitions.
     */
    public Map<String, Object> validate() throws IOException, InterruptedException {
        return sendGetRequest("/workflow/validate", "/api/workflow/validate", "/queue/validate");
    }

    /**
     * Retrieves current runtime workflow status.
     */
    public Map<String, Object> getStatus() throws IOException, InterruptedException {
        return sendGetRequest("/workflow/status", "/api/workflow/status", "/queue/status", "/api/queue/status");
    }

    /**
     * Reloads queues on the Artemis broker only.
     */
    public Map<String, Object> reloadQueues() throws IOException, InterruptedException {
        return sendPostRequest("/workflow/queues/reload", "/api/workflow/queues/reload");
    }

    /**
     * Reloads TaskSequences in CamelContext only.
     */
    public Map<String, Object> reloadSequences() throws IOException, InterruptedException {
        return sendPostRequest("/workflow/sequences/reload", "/api/workflow/sequences/reload");
    }

    /**
     * Retrieves the running set of all cluster modules and their statuses.
     */
    public List<Map<String, Object>> getClusterModules() throws IOException, InterruptedException {
        IOException lastEx = null;
        for (String path : java.util.List.of("/workflow/modules", "/api/workflow/modules")) {
            URI uri = URI.create(serverUrl + path);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            try {
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                int status = response.statusCode();
                if (status >= 200 && status < 300) {
                    return objectMapper.readValue(response.body(), new TypeReference<List<Map<String, Object>>>() {});
                } else if (status != 404) {
                    throw new IOException("HTTP request to " + uri + " failed with status " + status);
                }
            } catch (IOException e) {
                lastEx = e;
            }
        }
        if (lastEx != null) {
            throw lastEx;
        }
        return java.util.Collections.emptyList();
    }

    private Map<String, Object> sendPostRequest(String... candidatePaths) throws IOException, InterruptedException {
        IOException lastEx = null;
        for (String path : candidatePaths) {
            URI uri = URI.create(serverUrl + path);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString("{}"))
                    .build();

            try {
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                int status = response.statusCode();
                if (status >= 200 && status < 300) {
                    String body = response.body();
                    return parseResponse(body);
                } else if (status != 404) {
                    throw new IOException("HTTP request to " + uri + " failed with status " + status + ": " + response.body());
                }
            } catch (IOException e) {
                lastEx = e;
            }
        }
        if (lastEx != null) {
            throw lastEx;
        }
        throw new IOException("Failed to execute POST request to " + serverUrl + candidatePaths[0] + " (endpoint not found or service unreachable)");
    }

    private Map<String, Object> sendGetRequest(String... candidatePaths) throws IOException, InterruptedException {
        IOException lastEx = null;
        for (String path : candidatePaths) {
            URI uri = URI.create(serverUrl + path);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            try {
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                int status = response.statusCode();
                if (status >= 200 && status < 300) {
                    String body = response.body();
                    return parseResponse(body);
                } else if (status != 404) {
                    throw new IOException("HTTP request to " + uri + " failed with status " + status + ": " + response.body());
                }
            } catch (IOException e) {
                lastEx = e;
            }
        }
        if (lastEx != null) {
            throw lastEx;
        }
        throw new IOException("Failed to execute GET request to " + serverUrl + candidatePaths[0] + " (endpoint not found or service unreachable)");
    }

    private Map<String, Object> parseResponse(String body) throws IOException {
        if (body == null || body.isBlank()) {
            return Map.of("status", "SUCCESS");
        }
        try {
            return objectMapper.readValue(body, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            return Map.of("rawResponse", body);
        }
    }
}
