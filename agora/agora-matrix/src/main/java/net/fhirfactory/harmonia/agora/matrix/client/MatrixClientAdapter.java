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

package net.fhirfactory.harmonia.agora.matrix.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.fhirfactory.harmonia.agora.api.model.AgoraMembershipAction;
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
import java.util.*;

/**
 * Adapter for Matrix v1.11+ Client-Server REST APIs.
 * Supports Space and Room creation, Space parent-child hierarchy configuration,
 * message/notice publication, membership management, and power level updates
 * using bounded HTTP calls.
 */
public class MatrixClientAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MatrixClientAdapter.class);

    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(5);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final RestClient restClient;
    private final ObjectMapper mapper;

    public MatrixClientAdapter(String baseUrl, String accessToken) {
        this(baseUrl, accessToken, DEFAULT_TIMEOUT, DEFAULT_TIMEOUT);
    }

    public MatrixClientAdapter(String baseUrl, String accessToken, Duration connectTimeout, Duration readTimeout) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(connectTimeout);
        requestFactory.setReadTimeout(readTimeout);

        this.mapper = OBJECT_MAPPER;
        RestClient.Builder builder = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .requestFactory(requestFactory);
        this.restClient = applyDefaults(builder).build();
    }

    public MatrixClientAdapter(RestClient.Builder builder) {
        this(applyDefaults(builder).build(), OBJECT_MAPPER);
    }

    public MatrixClientAdapter(RestClient restClient) {
        this(restClient, OBJECT_MAPPER);
    }

    public MatrixClientAdapter(RestClient restClient, ObjectMapper mapper) {
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
                        // Keep raw body in message if not JSON
                    }
                    throw new MatrixRestException(status, errcode, error, bodyStr);
                });
    }

    /**
     * Creates a new Matrix Space (m.space).
     *
     * @param name      the display name of the space
     * @param topic     the topic or description of the space
     * @param isPrivate whether the space should have private visibility and preset
     * @return the created space room details
     */
    public MatrixRoomDto createSpace(String name, String topic, boolean isPrivate) {
        LOGGER.info("Creating Matrix Space isPrivate={}", isPrivate);

        Map<String, Object> requestBody = new LinkedHashMap<>();
        if (name != null) {
            requestBody.put("name", name);
        }
        if (topic != null) {
            requestBody.put("topic", topic);
        }
        requestBody.put("visibility", isPrivate ? "private" : "public");
        requestBody.put("preset", isPrivate ? "private_chat" : "public_chat");
        requestBody.put("creation_content", Map.of("type", MatrixRoomDto.ROOM_TYPE_SPACE));

        JsonNode response = restClient.post()
                .uri("/_matrix/client/v3/createRoom")
                .body(requestBody)
                .retrieve()
                .body(JsonNode.class);

        String roomId = response != null && response.has("room_id") ? response.get("room_id").asText() : null;
        LOGGER.info("Created Matrix Space roomId={}", roomId);

        return MatrixRoomDto.builder()
                .roomId(roomId)
                .name(name)
                .topic(topic)
                .space(true)
                .visibility(isPrivate ? "private" : "public")
                .build();
    }

    /**
     * Creates a new Matrix Room.
     *
     * @param name      the display name of the room
     * @param topic     the topic or description of the room
     * @param isPrivate whether the room should have private visibility
     * @return the created room details
     */
    public MatrixRoomDto createRoom(String name, String topic, boolean isPrivate) {
        LOGGER.info("Creating Matrix Room isPrivate={}", isPrivate);

        Map<String, Object> requestBody = new LinkedHashMap<>();
        if (name != null) {
            requestBody.put("name", name);
        }
        if (topic != null) {
            requestBody.put("topic", topic);
        }
        requestBody.put("visibility", isPrivate ? "private" : "public");
        requestBody.put("preset", isPrivate ? "private_chat" : "public_chat");

        JsonNode response = restClient.post()
                .uri("/_matrix/client/v3/createRoom")
                .body(requestBody)
                .retrieve()
                .body(JsonNode.class);

        String roomId = response != null && response.has("room_id") ? response.get("room_id").asText() : null;
        LOGGER.info("Created Matrix Room roomId={}", roomId);

        return MatrixRoomDto.builder()
                .roomId(roomId)
                .name(name)
                .topic(topic)
                .space(false)
                .visibility(isPrivate ? "private" : "public")
                .build();
    }

    /**
     * Links a child room to a parent Space by setting the m.space.child state event.
     *
     * @param spaceId     the parent Space room ID
     * @param childRoomId the child room ID
     * @param viaServers  routing homeserver names
     * @return the state event ID
     */
    public String linkChildRoom(String spaceId, String childRoomId, List<String> viaServers) {
        LOGGER.info("Linking child room to Space spaceId={}, childRoomId={}", spaceId, childRoomId);

        Map<String, Object> content = new LinkedHashMap<>();
        content.put("via", viaServers != null ? viaServers : Collections.emptyList());
        content.put("suggested", false);

        JsonNode response = restClient.put()
                .uri("/_matrix/client/v3/rooms/{spaceId}/state/m.space.child/{childRoomId}", spaceId, childRoomId)
                .body(content)
                .retrieve()
                .body(JsonNode.class);

        return response != null && response.has("event_id") ? response.get("event_id").asText() : null;
    }

    /**
     * Sends a message event into a Matrix room.
     *
     * @param roomId  the target room ID
     * @param msgType the Matrix message type (e.g. "m.text", "m.notice")
     * @param body    the message text content
     * @param txnId   the client transaction ID
     * @return the published event ID
     */
    public String sendMessage(String roomId, String msgType, String body, String txnId) {
        LOGGER.info("Sending message to room roomId={}, txnId={}", roomId, txnId);

        Map<String, Object> content = new LinkedHashMap<>();
        content.put("msgtype", msgType != null ? msgType : "m.text");
        content.put("body", body != null ? body : "");

        JsonNode response = restClient.put()
                .uri("/_matrix/client/v3/rooms/{roomId}/send/m.room.message/{txnId}", roomId, txnId)
                .body(content)
                .retrieve()
                .body(JsonNode.class);

        return response != null && response.has("event_id") ? response.get("event_id").asText() : null;
    }

    /**
     * Sends a notice message (m.notice) into a Matrix room.
     *
     * @param roomId the target room ID
     * @param body   the notice text content
     * @param txnId  the client transaction ID
     * @return the published event ID
     */
    public String sendNotice(String roomId, String body, String txnId) {
        return sendMessage(roomId, "m.notice", body, txnId);
    }

    /**
     * Sends a custom state or message event into a Matrix room.
     *
     * @param roomId    the target room ID
     * @param eventType the event type
     * @param txnId     the client transaction ID
     * @param content   the event content
     * @return the published event ID
     */
    public String sendCustomEvent(String roomId, String eventType, String txnId, Map<String, Object> content) {
        LOGGER.info("Sending custom event to room roomId={}, eventType={}, txnId={}", roomId, eventType, txnId);

        JsonNode response = restClient.put()
                .uri("/_matrix/client/v3/rooms/{roomId}/send/{eventType}/{txnId}", roomId, eventType, txnId)
                .body(content != null ? content : Collections.emptyMap())
                .retrieve()
                .body(JsonNode.class);

        return response != null && response.has("event_id") ? response.get("event_id").asText() : null;
    }

    /**
     * Invites a user to a room.
     *
     * @param roomId the room ID
     * @param userId the user ID to invite
     * @param reason optional reason
     */
    public void inviteUser(String roomId, String userId, String reason) {
        LOGGER.info("Inviting user to room roomId={}, userId={}", roomId, userId);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("user_id", userId);
        if (reason != null && !reason.isBlank()) {
            body.put("reason", reason);
        }

        restClient.post()
                .uri("/_matrix/client/v3/rooms/{roomId}/invite", roomId)
                .body(body)
                .retrieve()
                .toBodilessEntity();
    }

    /**
     * Joins the authenticated client/service user to a room.
     *
     * @param roomId the room ID
     */
    public void joinRoom(String roomId) {
        LOGGER.info("Joining room roomId={}", roomId);

        restClient.post()
                .uri("/_matrix/client/v3/rooms/{roomId}/join", roomId)
                .body(Collections.emptyMap())
                .retrieve()
                .toBodilessEntity();
    }

    /**
     * Kicks a user from a room.
     *
     * @param roomId the room ID
     * @param userId the user ID to kick
     * @param reason optional reason
     */
    public void kickUser(String roomId, String userId, String reason) {
        LOGGER.info("Kicking user from room roomId={}, userId={}", roomId, userId);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("user_id", userId);
        if (reason != null && !reason.isBlank()) {
            body.put("reason", reason);
        }

        restClient.post()
                .uri("/_matrix/client/v3/rooms/{roomId}/kick", roomId)
                .body(body)
                .retrieve()
                .toBodilessEntity();
    }

    /**
     * Bans a user from a room.
     *
     * @param roomId the room ID
     * @param userId the user ID to ban
     * @param reason optional reason
     */
    public void banUser(String roomId, String userId, String reason) {
        LOGGER.info("Banning user from room roomId={}, userId={}", roomId, userId);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("user_id", userId);
        if (reason != null && !reason.isBlank()) {
            body.put("reason", reason);
        }

        restClient.post()
                .uri("/_matrix/client/v3/rooms/{roomId}/ban", roomId)
                .body(body)
                .retrieve()
                .toBodilessEntity();
    }

    /**
     * Dispatches a membership action (INVITE, JOIN, KICK, etc.).
     *
     * @param roomId the room ID
     * @param userId the target user ID
     * @param action the membership action
     * @param reason optional reason
     */
    public void manageMembership(String roomId, String userId, AgoraMembershipAction action, String reason) {
        if (action == null) {
            throw new IllegalArgumentException("AgoraMembershipAction must not be null");
        }
        switch (action) {
            case INVITE -> inviteUser(roomId, userId, reason);
            case JOIN -> joinRoom(roomId);
            case KICK, LEAVE -> kickUser(roomId, userId, reason);
            case BAN -> banUser(roomId, userId, reason);
            default -> throw new UnsupportedOperationException("Unsupported membership action: " + action);
        }
    }

    /**
     * Retrieves member state events for a room.
     *
     * @param roomId the room ID
     * @return list of room members
     */
    public List<MatrixMemberDto> getRoomMembers(String roomId) {
        LOGGER.debug("Fetching room members for roomId={}", roomId);

        JsonNode response = restClient.get()
                .uri("/_matrix/client/v3/rooms/{roomId}/members", roomId)
                .retrieve()
                .body(JsonNode.class);

        List<MatrixMemberDto> members = new ArrayList<>();
        if (response != null && response.has("chunk") && response.get("chunk").isArray()) {
            for (JsonNode eventNode : response.get("chunk")) {
                String type = eventNode.has("type") ? eventNode.get("type").asText() : null;
                if ("m.room.member".equals(type)) {
                    String userId = eventNode.has("state_key") ? eventNode.get("state_key").asText() : null;
                    JsonNode content = eventNode.get("content");
                    String membership = content != null && content.has("membership") ? content.get("membership").asText() : null;
                    String displayName = content != null && content.has("displayname") ? content.get("displayname").asText() : null;
                    String avatarUrl = content != null && content.has("avatar_url") ? content.get("avatar_url").asText() : null;

                    MatrixMemberDto member = MatrixMemberDto.builder()
                            .userId(userId)
                            .membership(membership)
                            .displayName(displayName)
                            .avatarUrl(avatarUrl)
                            .build();
                    members.add(member);
                }
            }
        }
        return members;
    }

    /**
     * Retrieves power levels for a room.
     *
     * @param roomId the room ID
     * @return room power levels
     */
    public MatrixPowerLevelsDto getPowerLevels(String roomId) {
        LOGGER.debug("Fetching power levels for roomId={}", roomId);

        return restClient.get()
                .uri("/_matrix/client/v3/rooms/{roomId}/state/m.room.power_levels", roomId)
                .retrieve()
                .body(MatrixPowerLevelsDto.class);
    }

    /**
     * Sets power levels for a room.
     *
     * @param roomId      the room ID
     * @param powerLevels power levels to apply
     * @return the state event ID
     */
    public String setPowerLevels(String roomId, MatrixPowerLevelsDto powerLevels) {
        LOGGER.info("Setting power levels for roomId={}", roomId);

        JsonNode response = restClient.put()
                .uri("/_matrix/client/v3/rooms/{roomId}/state/m.room.power_levels", roomId)
                .body(powerLevels != null ? powerLevels : new MatrixPowerLevelsDto())
                .retrieve()
                .body(JsonNode.class);

        return response != null && response.has("event_id") ? response.get("event_id").asText() : null;
    }
}
