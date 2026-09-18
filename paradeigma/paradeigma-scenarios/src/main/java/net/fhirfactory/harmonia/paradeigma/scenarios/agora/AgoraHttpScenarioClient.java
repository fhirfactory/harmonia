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

package net.fhirfactory.harmonia.paradeigma.scenarios.agora;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.fhirfactory.harmonia.agora.api.model.AgoraPatientSpaceResponse;
import net.fhirfactory.harmonia.agora.api.model.AgoraReconciliationResult;
import net.fhirfactory.harmonia.agora.api.model.AgoraRoomRequest;
import net.fhirfactory.harmonia.agora.api.model.AgoraSpaceRequest;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Set;

/**
 * Public HTTP REST client for invoking Agora service endpoints during live or deployed simulations.
 * Enforces explicit bounded timeouts (<= 5s connect, <= 10s execution).
 */
public class AgoraHttpScenarioClient implements AgoraCollaborationClient {

    private static final Logger log = LoggerFactory.getLogger(AgoraHttpScenarioClient.class);

    private final String agoraBaseUrl;
    private final String hsToken;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public AgoraHttpScenarioClient(String agoraBaseUrl, String hsToken) {
        this.agoraBaseUrl = agoraBaseUrl.endsWith("/") ? agoraBaseUrl.substring(0, agoraBaseUrl.length() - 1) : agoraBaseUrl;
        this.hsToken = hsToken;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Override
    public AgoraPatientSpaceResponse provisionPatientSpace(AgoraSpaceRequest request, ThemisSecurityContext context) throws Exception {
        String url = agoraBaseUrl + "/api/agora/spaces/patient";
        String body = objectMapper.writeValueAsString(request);
        HttpRequest.Builder reqBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(10))
                .POST(HttpRequest.BodyPublishers.ofString(body));

        applySecurityHeaders(reqBuilder, context);

        HttpResponse<String> response = httpClient.send(reqBuilder.build(), HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200 || response.statusCode() == 201) {
            return objectMapper.readValue(response.body(), AgoraPatientSpaceResponse.class);
        } else if (response.statusCode() == 401 || response.statusCode() == 403) {
            throw new SecurityException("Themis authorization denied: HTTP " + response.statusCode());
        } else {
            throw new IllegalStateException("Failed to provision patient space: HTTP " + response.statusCode() + " - " + response.body());
        }
    }

    @Override
    public String provisionPractitionerSpace(AgoraSpaceRequest request, ThemisSecurityContext context) throws Exception {
        String url = agoraBaseUrl + "/api/agora/spaces/practitioner";
        String body = objectMapper.writeValueAsString(request);
        HttpRequest.Builder reqBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(10))
                .POST(HttpRequest.BodyPublishers.ofString(body));

        applySecurityHeaders(reqBuilder, context);

        HttpResponse<String> response = httpClient.send(reqBuilder.build(), HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200 || response.statusCode() == 201) {
            return response.body();
        } else if (response.statusCode() == 401 || response.statusCode() == 403) {
            throw new SecurityException("Themis authorization denied: HTTP " + response.statusCode());
        } else {
            throw new IllegalStateException("Failed to provision practitioner space: HTTP " + response.statusCode());
        }
    }

    @Override
    public String provisionPractitionerRoleRoom(AgoraRoomRequest request, ThemisSecurityContext context) throws Exception {
        String url = agoraBaseUrl + "/api/agora/rooms/role";
        String body = objectMapper.writeValueAsString(request);
        HttpRequest.Builder reqBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(10))
                .POST(HttpRequest.BodyPublishers.ofString(body));

        applySecurityHeaders(reqBuilder, context);

        HttpResponse<String> response = httpClient.send(reqBuilder.build(), HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200 || response.statusCode() == 201) {
            return response.body();
        } else if (response.statusCode() == 401 || response.statusCode() == 403) {
            throw new SecurityException("Themis authorization denied: HTTP " + response.statusCode());
        } else {
            throw new IllegalStateException("Failed to provision practitioner role room: HTTP " + response.statusCode());
        }
    }

    @Override
    public boolean submitApplicationServiceTransaction(String txnId, String hsToken, String jsonPayload) throws Exception {
        String url = agoraBaseUrl + "/_matrix/app/v1/transactions/" + txnId;
        String tokenToUse = (hsToken != null) ? hsToken : this.hsToken;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tokenToUse)
                .timeout(Duration.ofSeconds(10))
                .PUT(HttpRequest.BodyPublishers.ofString(jsonPayload != null ? jsonPayload : "{\"events\":[]}"))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return true;
        } else if (response.statusCode() == 401 || response.statusCode() == 403) {
            throw new SecurityException("Matrix AS transaction rejected: HTTP " + response.statusCode());
        } else {
            log.warn("Application Service transaction {} returned HTTP {}", txnId, response.statusCode());
            return false;
        }
    }

    @Override
    public AgoraReconciliationResult reconcileHierarchyMembership(String resourceType, String resourceId, Set<String> desiredUserIds, ThemisSecurityContext context) throws Exception {
        String url = agoraBaseUrl + "/api/agora/reconciliation/hierarchy/" + resourceType + "/" + resourceId;
        String body = objectMapper.writeValueAsString(desiredUserIds);

        HttpRequest.Builder reqBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(10))
                .POST(HttpRequest.BodyPublishers.ofString(body));

        applySecurityHeaders(reqBuilder, context);

        HttpResponse<String> response = httpClient.send(reqBuilder.build(), HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), AgoraReconciliationResult.class);
        } else if (response.statusCode() == 401 || response.statusCode() == 403) {
            throw new SecurityException("Themis authorization denied: HTTP " + response.statusCode());
        } else {
            throw new IllegalStateException("Hierarchy reconciliation failed: HTTP " + response.statusCode());
        }
    }

    @Override
    public void injectLiveRoomMember(String roomId, String userId) throws Exception {
        // Mock / simulation hook for external drift injection
        log.info("Simulating live room drift: adding user {} to room {}", userId, roomId);
    }

    private void applySecurityHeaders(HttpRequest.Builder builder, ThemisSecurityContext context) {
        if (context != null && context.requestingPrincipal() != null) {
            builder.header("X-Harmonia-Principal", context.requestingPrincipal().principalId());
            builder.header("X-Harmonia-Correlation-Id", context.correlationId() != null ? context.correlationId() : "");
        }
    }
}
