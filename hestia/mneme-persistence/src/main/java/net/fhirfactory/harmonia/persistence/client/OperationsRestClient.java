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

package net.fhirfactory.harmonia.persistence.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletionStage;

/**
 * HTTP REST client communicating with the HIE Operations JPA Server for non-FHIR operational data.
 */
public class OperationsRestClient {

    private static final Logger log = LoggerFactory.getLogger(OperationsRestClient.class);

    private final String serverUrl;
    private final HttpClient httpClient;

    public OperationsRestClient(String serverUrl, int timeoutSeconds) {
        this.serverUrl = serverUrl.endsWith("/") ? serverUrl.substring(0, serverUrl.length() - 1) : serverUrl;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(timeoutSeconds))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
    }

    public CompletionStage<String> getResourceJson(String objectType, String id) {
        URI uri = URI.create(serverUrl + "/" + objectType + "/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Accept", "application/json, text/plain")
                .GET()
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    int status = response.statusCode();
                    if (status == 200) {
                        return response.body();
                    } else if (status == 404 || status == 410) {
                        return null;
                    } else {
                        log.warn("GET {} failed with status {}", uri, status);
                        return null;
                    }
                });
    }

    public CompletionStage<Boolean> saveResourceJson(String objectType, String id, String jsonPayload) {
        URI uri = URI.create(serverUrl + "/" + objectType + "/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json; charset=UTF-8")
                .header("Accept", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonPayload != null ? jsonPayload : ""))
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    int status = response.statusCode();
                    boolean success = (status >= 200 && status < 300);
                    if (!success) {
                        log.error("PUT {} failed with status {}: {}", uri, status, response.body());
                    } else {
                        log.info("PUT {} succeeded with status {}", uri, status);
                    }
                    return success;
                });
    }

    public CompletionStage<Boolean> deleteResource(String objectType, String id) {
        URI uri = URI.create(serverUrl + "/" + objectType + "/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Accept", "application/json")
                .DELETE()
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    int status = response.statusCode();
                    boolean success = (status >= 200 && status < 300) || status == 404 || status == 410;
                    if (!success) {
                        log.error("DELETE {} failed with status {}: {}", uri, status, response.body());
                    } else {
                        log.debug("Successfully deleted operational resource {}/{}", objectType, id);
                    }
                    return success;
                });
    }

    public record OperationResourceEntry(String objectType, String objectId, String dataJson) {}

    public CompletionStage<List<OperationResourceEntry>> listResources(String objectType) {
        String endpoint = (objectType != null && !objectType.isBlank()) ? "/" + objectType : "";
        URI uri = URI.create(serverUrl + endpoint);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Accept", "application/json")
                .GET()
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    int status = response.statusCode();
                    if (status == 200) {
                        try {
                            ObjectMapper mapper = new ObjectMapper();
                            JsonNode root = mapper.readTree(response.body());
                            List<OperationResourceEntry> result = new ArrayList<>();
                            if (root.isArray()) {
                                for (JsonNode node : root) {
                                    String type = node.has("objectType") ? node.get("objectType").asText() : objectType;
                                    String id = node.has("objectId") ? node.get("objectId").asText() : null;
                                    String json = node.has("dataJson") ? node.get("dataJson").asText() : null;
                                    if (id != null) {
                                        result.add(new OperationResourceEntry(type, id, json));
                                    }
                                }
                            }
                            return result;
                        } catch (Exception e) {
                            log.error("Failed parsing list response from {}: {}", uri, e.getMessage());
                            return Collections.emptyList();
                        }
                    } else {
                        log.warn("GET {} failed with status {}", uri, status);
                        return Collections.emptyList();
                    }
                });
    }

    public CompletionStage<Boolean> containsResource(String objectType, String id) {
        URI uri = URI.create(serverUrl + "/" + objectType + "/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Accept", "application/json")
                .method("HEAD", HttpRequest.BodyPublishers.noBody())
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.discarding())
                .thenApply(response -> {
                    int status = response.statusCode();
                    return (status >= 200 && status < 300);
                });
    }

    public CompletionStage<Boolean> isServerReady() {
        String base = serverUrl.replaceAll("/api/operations/?$", "").replaceAll("/operations/?$", "");
        URI uri = URI.create(base + "/api/operations/ready");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Accept", "application/json")
                .GET()
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    int status = response.statusCode();
                    return (status >= 200 && status < 300);
                })
                .exceptionally(ex -> {
                    log.debug("Readiness check to {} failed: {}", uri, ex.getMessage());
                    return false;
                });
    }

    public CompletionStage<String> getServerStatus() {
        String base = serverUrl.replaceAll("/api/operations/?$", "").replaceAll("/operations/?$", "");
        URI uri = URI.create(base + "/api/operations/status");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Accept", "application/json")
                .GET()
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    int status = response.statusCode();
                    return (status == 200) ? response.body() : null;
                })
                .exceptionally(ex -> null);
    }

    public boolean isReady() {
        try {
            return isServerReady().toCompletableFuture().get(Duration.ofSeconds(3).toMillis(), java.util.concurrent.TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean waitForReady(int maxWaitSeconds, int pollIntervalMillis) {
        long deadline = System.currentTimeMillis() + (maxWaitSeconds * 1000L);
        while (System.currentTimeMillis() < deadline) {
            if (isReady()) {
                return true;
            }
            try {
                Thread.sleep(Math.max(50, pollIntervalMillis));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return isReady();
    }
}
