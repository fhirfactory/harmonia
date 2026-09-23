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

package net.fhirfactory.harmonia.agora.matrix.admin;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Administration gateway for Matrix Synapse administrative operations.
 * Communicates over Synapse-specific Admin REST APIs with dedicated admin token authentication.
 */
public class SynapseAdministrationGateway {

    private static final Logger LOGGER = LoggerFactory.getLogger(SynapseAdministrationGateway.class);

    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(5);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final RestClient restClient;
    private final ObjectMapper mapper;

    public SynapseAdministrationGateway(String baseUrl, String adminToken) {
        this(baseUrl, adminToken, DEFAULT_TIMEOUT, DEFAULT_TIMEOUT);
    }

    public SynapseAdministrationGateway(String baseUrl, String adminToken, Duration connectTimeout, Duration readTimeout) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(connectTimeout);
        requestFactory.setReadTimeout(readTimeout);

        this.mapper = OBJECT_MAPPER;
        RestClient.Builder builder = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                .requestFactory(requestFactory);
        this.restClient = applyDefaults(builder).build();
    }

    public SynapseAdministrationGateway(RestClient.Builder builder) {
        this(applyDefaults(builder).build(), OBJECT_MAPPER);
    }

    public SynapseAdministrationGateway(RestClient restClient) {
        this(restClient, OBJECT_MAPPER);
    }

    public SynapseAdministrationGateway(RestClient restClient, ObjectMapper mapper) {
        this.restClient = Objects.requireNonNull(restClient, "RestClient must not be null");
        this.mapper = mapper != null ? mapper : OBJECT_MAPPER;
    }

    public static RestClient.Builder applyDefaults(RestClient.Builder builder) {
        return builder
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultStatusHandler(HttpStatusCode::isError, (request, response) -> {
                    int status = response.getStatusCode().value();
                    byte[] body = response.getBody().readAllBytes();
                    String bodyStr = new String(body, StandardCharsets.UTF_8);
                    String errcode = null;
                    String error = null;
                    try {
                        JsonNode node = OBJECT_MAPPER.readTree(bodyStr);
                        if (node.has("errcode")) {
                            errcode = node.get("errcode").asText();
                        }
                        if (node.has("error")) {
                            error = node.get("error").asText();
                        }
                    } catch (IOException ignored) {
                        // Non-JSON error body; do not leak raw body into exception message
                    }
                    throw new SynapseAdminException(status, errcode, error);
                });
    }

    /**
     * Creates or updates a local user in Synapse via the Admin API.
     *
     * @param user the user details to provision
     * @return the created or updated user details
     */
    public MatrixUserDto createOrUpdateUser(MatrixUserDto user) {
        Objects.requireNonNull(user, "MatrixUserDto must not be null");
        Objects.requireNonNull(user.getUserId(), "userId must not be null");

        LOGGER.info("Provisioning Synapse local user userId={}", user.getUserId());

        Map<String, Object> body = new LinkedHashMap<>();
        if (user.getDisplayName() != null) {
            body.put("displayname", user.getDisplayName());
        }
        if (user.getPassword() != null) {
            body.put("password", user.getPassword());
        }
        if (user.getAdmin() != null) {
            body.put("admin", user.getAdmin());
        }
        if (user.getDeactivated() != null) {
            body.put("deactivated", user.getDeactivated());
        }
        if (user.getAvatarUrl() != null) {
            body.put("avatar_url", user.getAvatarUrl());
        }
        if (user.getUserType() != null) {
            body.put("user_type", user.getUserType());
        }

        MatrixUserDto response = restClient.put()
                .uri("/_synapse/admin/v2/users/{userId}", user.getUserId())
                .body(body)
                .retrieve()
                .body(MatrixUserDto.class);

        LOGGER.info("Successfully provisioned Synapse local user userId={}", user.getUserId());
        return response != null ? response : user;
    }

    /**
     * Retrieves local user details from Synapse via the Admin API.
     *
     * @param userId the local user ID
     * @return the user details
     */
    public MatrixUserDto getUser(String userId) {
        Objects.requireNonNull(userId, "userId must not be null");

        LOGGER.debug("Fetching Synapse user details userId={}", userId);

        return restClient.get()
                .uri("/_synapse/admin/v2/users/{userId}", userId)
                .retrieve()
                .body(MatrixUserDto.class);
    }

    /**
     * Deactivates a local user account in Synapse.
     *
     * @param userId the local user ID
     * @param erase  whether to erase user data
     */
    public void deactivateUser(String userId, boolean erase) {
        Objects.requireNonNull(userId, "userId must not be null");

        LOGGER.info("Deactivating Synapse local user userId={}, erase={}", userId, erase);

        Map<String, Object> body = Map.of("erase", erase);

        restClient.post()
                .uri("/_synapse/admin/v1/deactivate/{userId}", userId)
                .body(body)
                .retrieve()
                .toBodilessEntity();
    }

    /**
     * Purges and permanently deletes a room in Synapse.
     *
     * @param roomId the room ID to purge
     * @return the purge job deletion ID if returned by Synapse
     */
    public String purgeRoom(String roomId) {
        Objects.requireNonNull(roomId, "roomId must not be null");

        LOGGER.info("Purging room in Synapse roomId={}", roomId);

        Map<String, Object> body = Map.of("purge", true);

        JsonNode response = restClient.method(org.springframework.http.HttpMethod.DELETE)
                .uri("/_synapse/admin/v1/rooms/{roomId}", roomId)
                .body(body)
                .retrieve()
                .body(JsonNode.class);

        return response != null && response.has("delete_id") ? response.get("delete_id").asText() : null;
    }
}
