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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package net.fhirfactory.harmonia.paradeigma.scenarios.operations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * Public HTTP REST client invoking live iris-befe operations endpoints on port 8090.
 * Enforces explicit bounded timeouts (connect <= 5s, execution <= 10s) and propagates Themis security headers.
 */
public class IrisOperationsHttpClient implements IrisOperationsClient {

    private static final Logger log = LoggerFactory.getLogger(IrisOperationsHttpClient.class);

    private final String baseUrl;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public IrisOperationsHttpClient(String baseUrl) {
        this.baseUrl = (baseUrl != null && baseUrl.endsWith("/")) ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private void applySecurityHeaders(HttpRequest.Builder builder, ThemisSecurityContext context) {
        builder.header("Accept", "application/json");
        if (context != null && context.requestingPrincipal() != null) {
            builder.header("X-Harmonia-Principal", context.requestingPrincipal().principalId());
            if (context.correlationId() != null) {
                builder.header("X-Harmonia-Correlation-Id", context.correlationId());
            }
        }
    }

    private <T> T get(String path, TypeReference<T> typeRef, ThemisSecurityContext context) throws Exception {
        String url = baseUrl + path;
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(10))
                .GET();

        applySecurityHeaders(builder, context);

        HttpResponse<String> response = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), typeRef);
        } else if (response.statusCode() == 401 || response.statusCode() == 403) {
            throw new SecurityException("Themis authorization denied: HTTP " + response.statusCode());
        } else if (response.statusCode() == 404) {
            return null;
        } else {
            throw new IllegalStateException("Operations API error on " + path + ": HTTP " + response.statusCode() + " " + response.body());
        }
    }

    private <T> T post(String path, String body, TypeReference<T> typeRef, ThemisSecurityContext context) throws Exception {
        String url = baseUrl + path;
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(10))
                .POST(HttpRequest.BodyPublishers.ofString(body != null ? body : ""));

        applySecurityHeaders(builder, context);

        HttpResponse<String> response = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), typeRef);
        } else if (response.statusCode() == 401 || response.statusCode() == 403) {
            throw new SecurityException("Themis authorization denied: HTTP " + response.statusCode());
        } else {
            throw new IllegalStateException("Operations API POST error on " + path + ": HTTP " + response.statusCode() + " " + response.body());
        }
    }

    @Override
    public IrisOperationsModels.OpsSummaryDto getSummary(ThemisSecurityContext context) throws Exception {
        return get("/api/operations/summary", new TypeReference<>() {}, context);
    }

    @Override
    public List<IrisOperationsModels.OpsSubsystemDto> getSubsystems(ThemisSecurityContext context) throws Exception {
        return get("/api/operations/subsystems", new TypeReference<>() {}, context);
    }

    @Override
    public IrisOperationsModels.OpsSubsystemDto getSubsystem(String id, ThemisSecurityContext context) throws Exception {
        return get("/api/operations/subsystems/" + URLEncoder.encode(id, StandardCharsets.UTF_8), new TypeReference<>() {}, context);
    }

    @Override
    public List<IrisOperationsModels.OpsInstanceDto> getSubsystemInstances(String id, ThemisSecurityContext context) throws Exception {
        return get("/api/operations/subsystems/" + URLEncoder.encode(id, StandardCharsets.UTF_8) + "/instances", new TypeReference<>() {}, context);
    }

    @Override
    public IrisOperationsModels.OpsHealthDto getSubsystemHealth(String id, ThemisSecurityContext context) throws Exception {
        return get("/api/operations/subsystems/" + URLEncoder.encode(id, StandardCharsets.UTF_8) + "/health", new TypeReference<>() {}, context);
    }

    @Override
    public List<IrisOperationsModels.OpsQueueDto> getQueues(ThemisSecurityContext context) throws Exception {
        return get("/api/operations/queues", new TypeReference<>() {}, context);
    }

    @Override
    public IrisOperationsModels.OpsQueueDto getQueue(String id, ThemisSecurityContext context) throws Exception {
        return get("/api/operations/queues/" + URLEncoder.encode(id, StandardCharsets.UTF_8), new TypeReference<>() {}, context);
    }

    @Override
    public List<IrisOperationsModels.OpsWorkflowDto> getWorkflows(ThemisSecurityContext context) throws Exception {
        return get("/api/operations/workflows", new TypeReference<>() {}, context);
    }

    @Override
    public IrisOperationsModels.OpsWorkflowDto getWorkflow(String id, ThemisSecurityContext context) throws Exception {
        return get("/api/operations/workflows/" + URLEncoder.encode(id, StandardCharsets.UTF_8), new TypeReference<>() {}, context);
    }

    @Override
    public IrisOperationsModels.OpsPragmaDto getPragma(String id, ThemisSecurityContext context) throws Exception {
        return get("/api/operations/pragmas/" + URLEncoder.encode(id, StandardCharsets.UTF_8), new TypeReference<>() {}, context);
    }

    @Override
    public List<IrisOperationsModels.OpsEventDto> getEvents(Map<String, String> filters, ThemisSecurityContext context) throws Exception {
        StringBuilder query = new StringBuilder("/api/operations/events");
        if (filters != null && !filters.isEmpty()) {
            query.append("?");
            filters.forEach((k, v) -> {
                if (v != null && !v.isBlank()) {
                    query.append(URLEncoder.encode(k, StandardCharsets.UTF_8))
                            .append("=")
                            .append(URLEncoder.encode(v, StandardCharsets.UTF_8))
                            .append("&");
                }
            });
            if (query.charAt(query.length() - 1) == '&') {
                query.deleteCharAt(query.length() - 1);
            }
        }
        return get(query.toString(), new TypeReference<>() {}, context);
    }

    @Override
    public IrisOperationsModels.OpsEventDto getEvent(String id, ThemisSecurityContext context) throws Exception {
        return get("/api/operations/events/" + URLEncoder.encode(id, StandardCharsets.UTF_8), new TypeReference<>() {}, context);
    }

    @Override
    public List<IrisOperationsModels.OpsAlertDto> getAlerts(Map<String, String> filters, ThemisSecurityContext context) throws Exception {
        StringBuilder query = new StringBuilder("/api/operations/alerts");
        if (filters != null && !filters.isEmpty()) {
            query.append("?");
            filters.forEach((k, v) -> {
                if (v != null && !v.isBlank()) {
                    query.append(URLEncoder.encode(k, StandardCharsets.UTF_8))
                            .append("=")
                            .append(URLEncoder.encode(v, StandardCharsets.UTF_8))
                            .append("&");
                }
            });
            if (query.charAt(query.length() - 1) == '&') {
                query.deleteCharAt(query.length() - 1);
            }
        }
        return get(query.toString(), new TypeReference<>() {}, context);
    }

    @Override
    public IrisOperationsModels.OpsAlertDto acknowledgeAlert(String alertId, String operator, ThemisSecurityContext context) throws Exception {
        String path = "/api/operations/alerts/" + URLEncoder.encode(alertId, StandardCharsets.UTF_8) + "/acknowledge";
        String body = String.format("{\"operator\":\"%s\"}", operator != null ? operator : "operator");
        return post(path, body, new TypeReference<>() {}, context);
    }
}
