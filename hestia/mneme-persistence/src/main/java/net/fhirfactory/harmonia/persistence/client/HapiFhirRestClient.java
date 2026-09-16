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

import net.fhirfactory.harmonia.logging.PhiLogger;
import net.fhirfactory.harmonia.logging.PhiLoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletionStage;

public class HapiFhirRestClient {

    private static final Logger log = LoggerFactory.getLogger(HapiFhirRestClient.class);
    private static final PhiLogger phiLog = PhiLoggerFactory.getLogger(HapiFhirRestClient.class);

    private final String serverUrl;
    private final HttpClient httpClient;
    // Removed FhirContext field

    public HapiFhirRestClient(String serverUrl, int timeoutSeconds) {
        this.serverUrl = serverUrl.endsWith("/") ? serverUrl.substring(0, serverUrl.length() - 1) : serverUrl;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(timeoutSeconds))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        // Removed FhirContext initialization
    }

    // Removed getJsonParser method

    public CompletionStage<String> getResourceJson(String resourceType, String id) {
        URI uri = URI.create(serverUrl + "/" + resourceType + "/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Accept", "application/fhir+json, application/json")
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

    public CompletionStage<Boolean> saveResourceJson(String resourceType, String id, String jsonPayload) {
        URI uri = URI.create(serverUrl + "/" + resourceType + "/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/fhir+json; charset=UTF-8")
                .header("Accept", "application/fhir+json, application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    int status = response.statusCode();
                    boolean success = (status >= 200 && status < 300);
                    if (!success) {
                        log.error("PUT {} failed with status {}", uri, status);
                        phiLog.debug("PUT {} failure response body: {}", uri, response.body());
                    } else {
                        log.info("PUT {} succeeded with status {}", uri, status);
                    }
                    return success;
                });
    }

    public CompletionStage<Boolean> deleteResource(String resourceType, String id) {
        URI uri = URI.create(serverUrl + "/" + resourceType + "/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Accept", "application/fhir+json, application/json")
                .DELETE()
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    int status = response.statusCode();
                    boolean success = (status >= 200 && status < 300) || status == 404 || status == 410;
                    if (!success) {
                        log.error("DELETE {} failed with status {}", uri, status);
                        phiLog.debug("DELETE {} failure response body: {}", uri, response.body());
                    } else {
                        log.debug("Successfully deleted {}/{} on FHIR JPA server", resourceType, id);
                    }
                    return success;
                });
    }

    public CompletionStage<Boolean> containsResource(String resourceType, String id) {
        URI uri = URI.create(serverUrl + "/" + resourceType + "/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Accept", "application/fhir+json, application/json")
                .method("HEAD", HttpRequest.BodyPublishers.noBody())
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.discarding())
                .thenApply(response -> {
                    int status = response.statusCode();
                    return (status >= 200 && status < 300);
                });
    }

    public CompletionStage<Boolean> isServerReady() {
        URI uri = URI.create(serverUrl + "/metadata");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Accept", "application/fhir+json, application/json")
                .GET()
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.discarding())
                .thenApply(response -> {
                    int status = response.statusCode();
                    return (status >= 200 && status < 300);
                })
                .exceptionally(ex -> false);
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
