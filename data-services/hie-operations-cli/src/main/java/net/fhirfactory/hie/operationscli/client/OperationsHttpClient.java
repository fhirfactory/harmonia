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

package net.fhirfactory.hie.operationscli.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.fhirfactory.hie.model.queue.MessageQueueDefinition;
import net.fhirfactory.hie.model.sequence.TaskSequenceDefinition;
import net.fhirfactory.hie.operationscli.model.OperationResourceDto;
import net.fhirfactory.hie.operationscli.model.TaskSequenceDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * HTTP Client for connecting to the HIE Operations JPA Server.
 */
public class OperationsHttpClient {

    private static final Logger log = LoggerFactory.getLogger(OperationsHttpClient.class);

    private final String serverUrl;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public OperationsHttpClient(String serverUrl) {
        this(serverUrl, 10);
    }

    public OperationsHttpClient(String serverUrl, int timeoutSeconds) {
        String base = serverUrl != null && !serverUrl.isBlank() ? serverUrl : "http://localhost:8085/api/operations";
        this.serverUrl = base.endsWith("/") ? base.substring(0, base.length() - 1) : base;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(timeoutSeconds))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public OperationsHttpClient(String serverUrl, HttpClient httpClient, ObjectMapper objectMapper) {
        String base = serverUrl != null && !serverUrl.isBlank() ? serverUrl : "http://localhost:8085/api/operations";
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
     * Lists all operational resources stored in the Operations JPA Server.
     */
    public List<OperationResourceDto> listAllResources() throws IOException, InterruptedException {
        URI uri = URI.create(serverUrl);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Accept", "application/json")
                .GET()
                .build();

        log.debug("Executing GET {}", uri);
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();

        if (status >= 200 && status < 300) {
            String body = response.body();
            if (body == null || body.isBlank()) {
                return Collections.emptyList();
            }
            return objectMapper.readValue(body, new TypeReference<List<OperationResourceDto>>() {});
        } else {
            throw new IOException("Failed to list all operational resources from " + uri + " (HTTP " + status + "): " + response.body());
        }
    }

    /**
     * Lists operational resources for a specific object type.
     */
    public List<OperationResourceDto> listResourcesByType(String objectType) throws IOException, InterruptedException {
        if (objectType == null || objectType.isBlank()) {
            return listAllResources();
        }

        URI uri = URI.create(serverUrl + "/" + objectType);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Accept", "application/json")
                .GET()
                .build();

        log.debug("Executing GET {}", uri);
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();

        if (status >= 200 && status < 300) {
            String body = response.body();
            if (body == null || body.isBlank()) {
                return Collections.emptyList();
            }
            return objectMapper.readValue(body, new TypeReference<List<OperationResourceDto>>() {});
        } else {
            throw new IOException("Failed to list operational resources of type '" + objectType + "' from " + uri + " (HTTP " + status + "): " + response.body());
        }
    }

    /**
     * Retrieves all TaskSequences by querying object type 'tasksequence' and deserializing each resource JSON.
     */
    public List<TaskSequenceDto> listTaskSequences() throws IOException, InterruptedException {
        List<OperationResourceDto> entities = listResourcesByType("tasksequence");
        List<TaskSequenceDto> sequences = new ArrayList<>();

        for (OperationResourceDto entity : entities) {
            if (entity != null && entity.getDataJson() != null && !entity.getDataJson().isBlank()) {
                try {
                    TaskSequenceDto seq = objectMapper.readValue(entity.getDataJson(), TaskSequenceDto.class);
                    if (seq.getSequenceId() == null || seq.getSequenceId().isBlank()) {
                        seq.setSequenceId(entity.getObjectId());
                    }
                    sequences.add(seq);
                } catch (Exception e) {
                    log.warn("Failed to parse TaskSequence JSON for ID {}: {}", entity.getObjectId(), e.getMessage());
                    TaskSequenceDto fallback = new TaskSequenceDto(entity.getObjectId(), entity.getObjectId());
                    fallback.setDescription(entity.getDataJson());
                    sequences.add(fallback);
                }
            }
        }
        return sequences;
    }

    /**
     * Retrieves the raw JSON content of a specific resource.
     */
    public Optional<String> getResourceJson(String objectType, String id) throws IOException, InterruptedException {
        URI uri = URI.create(serverUrl + "/" + objectType + "/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Accept", "application/json, text/plain")
                .GET()
                .build();

        log.debug("Executing GET {}", uri);
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();

        if (status == 200) {
            return Optional.ofNullable(response.body());
        } else if (status == 404 || status == 410) {
            return Optional.empty();
        } else {
            throw new IOException("Failed to get resource " + objectType + "/" + id + " from " + uri + " (HTTP " + status + "): " + response.body());
        }
    }

    /**
     * Retrieves a parsed TaskSequence by ID.
     */
    public Optional<TaskSequenceDto> getTaskSequence(String sequenceId) throws IOException, InterruptedException {
        Optional<String> jsonOpt = getResourceJson("tasksequence", sequenceId);
        if (jsonOpt.isEmpty()) {
            return Optional.empty();
        }
        TaskSequenceDto dto = objectMapper.readValue(jsonOpt.get(), TaskSequenceDto.class);
        if (dto.getSequenceId() == null || dto.getSequenceId().isBlank()) {
            dto.setSequenceId(sequenceId);
        }
        return Optional.of(dto);
    }

    /**
     * Saves or updates an operational resource.
     */
    public String saveResourceJson(String objectType, String id, String jsonPayload) throws IOException, InterruptedException {
        URI uri = URI.create(serverUrl + "/" + objectType + "/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json; charset=UTF-8")
                .header("Accept", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonPayload != null ? jsonPayload : ""))
                .build();

        log.debug("Executing PUT {}", uri);
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();

        if (status >= 200 && status < 300) {
            return response.body();
        } else {
            throw new IOException("Failed to save resource " + objectType + "/" + id + " to " + uri + " (HTTP " + status + "): " + response.body());
        }
    }

    /**
     * Saves or updates a TaskSequence.
     */
    public String saveTaskSequence(TaskSequenceDto sequence) throws IOException, InterruptedException {
        if (sequence == null || sequence.getSequenceId() == null || sequence.getSequenceId().isBlank()) {
            throw new IllegalArgumentException("TaskSequence and sequenceId must not be null/empty");
        }
        String json = objectMapper.writeValueAsString(sequence);
        return saveResourceJson("tasksequence", sequence.getSequenceId(), json);
    }

    /**
     * Deletes an operational resource.
     */
    public boolean deleteResource(String objectType, String id) throws IOException, InterruptedException {
        URI uri = URI.create(serverUrl + "/" + objectType + "/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Accept", "application/json")
                .DELETE()
                .build();

        log.debug("Executing DELETE {}", uri);
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();

        if (status == 204 || (status >= 200 && status < 300)) {
            return true;
        } else if (status == 404) {
            return false;
        } else {
            throw new IOException("Failed to delete resource " + objectType + "/" + id + " (HTTP " + status + "): " + response.body());
        }
    }

    /**
     * Deletes a TaskSequence.
     */
    public boolean deleteTaskSequence(String sequenceId) throws IOException, InterruptedException {
        return deleteResource("tasksequence", sequenceId);
    }

    /**
     * Lists all MessageQueueDefinitions stored in the Operations JPA Server.
     */
    public List<MessageQueueDefinition> listMessageQueues() throws IOException, InterruptedException {
        List<OperationResourceDto> entities = listResourcesByType("messagequeue");
        List<MessageQueueDefinition> queues = new ArrayList<>();

        for (OperationResourceDto entity : entities) {
            if (entity != null && entity.getDataJson() != null && !entity.getDataJson().isBlank()) {
                try {
                    MessageQueueDefinition queue = objectMapper.readValue(entity.getDataJson(), MessageQueueDefinition.class);
                    if (queue.getQueueId() == null || queue.getQueueId().isBlank()) {
                        queue.setQueueId(entity.getObjectId());
                    }
                    queues.add(queue);
                } catch (Exception e) {
                    log.warn("Failed to parse MessageQueue JSON for ID {}: {}", entity.getObjectId(), e.getMessage());
                    MessageQueueDefinition fallback = new MessageQueueDefinition(entity.getObjectId());
                    fallback.setDescription(entity.getDataJson());
                    queues.add(fallback);
                }
            }
        }
        return queues;
    }

    /**
     * Retrieves a parsed MessageQueueDefinition by queue ID.
     */
    public Optional<MessageQueueDefinition> getMessageQueue(String queueId) throws IOException, InterruptedException {
        Optional<String> jsonOpt = getResourceJson("messagequeue", queueId);
        if (jsonOpt.isEmpty()) {
            return Optional.empty();
        }
        MessageQueueDefinition dto = objectMapper.readValue(jsonOpt.get(), MessageQueueDefinition.class);
        if (dto.getQueueId() == null || dto.getQueueId().isBlank()) {
            dto.setQueueId(queueId);
        }
        return Optional.of(dto);
    }

    /**
     * Saves or updates a MessageQueueDefinition.
     */
    public String saveMessageQueue(MessageQueueDefinition queue) throws IOException, InterruptedException {
        if (queue == null || queue.getQueueId() == null || queue.getQueueId().isBlank()) {
            throw new IllegalArgumentException("MessageQueueDefinition and queueId must not be null/empty");
        }
        String json = objectMapper.writeValueAsString(queue);
        return saveResourceJson("messagequeue", queue.getQueueId(), json);
    }

    /**
     * Deletes a MessageQueueDefinition.
     */
    public boolean deleteMessageQueue(String queueId) throws IOException, InterruptedException {
        return deleteResource("messagequeue", queueId);
    }

    /**
     * Applies an initial configuration JSON string containing queues and sequences.
     */
    public InitialConfigResult applyInitialConfig(String configJson) throws IOException, InterruptedException {
        if (configJson == null || configJson.isBlank()) {
            throw new IllegalArgumentException("Initial configuration JSON must not be null or blank");
        }

        JsonNode root = objectMapper.readTree(configJson);
        List<MessageQueueDefinition> appliedQueues = new ArrayList<>();
        List<TaskSequenceDto> appliedSequences = new ArrayList<>();

        // Process queues
        if (root.has("queues") && root.get("queues").isArray()) {
            for (JsonNode qNode : root.get("queues")) {
                MessageQueueDefinition queue = objectMapper.treeToValue(qNode, MessageQueueDefinition.class);
                if (queue != null && queue.getQueueId() != null && !queue.getQueueId().isBlank()) {
                    saveMessageQueue(queue);
                    appliedQueues.add(queue);
                }
            }
        }

        // Process sequences
        if (root.has("sequences") && root.get("sequences").isArray()) {
            for (JsonNode sNode : root.get("sequences")) {
                TaskSequenceDto seq = objectMapper.treeToValue(sNode, TaskSequenceDto.class);
                if (seq != null && seq.getSequenceId() != null && !seq.getSequenceId().isBlank()) {
                    saveTaskSequence(seq);
                    appliedSequences.add(seq);
                }
            }
        }

        return new InitialConfigResult(appliedQueues, appliedSequences);
    }

    /**
     * Loads the default initial-config.json packaged in the classpath and populates it.
     */
    public InitialConfigResult applyDefaultInitialConfig() throws IOException, InterruptedException {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("initial-config.json")) {
            if (is == null) {
                throw new IOException("Default initial-config.json not found on classpath");
            }
            String json = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            return applyInitialConfig(json);
        }
    }

    /**
     * Result of applying an initial configuration.
     */
    public static class InitialConfigResult {
        private final List<MessageQueueDefinition> queues;
        private final List<TaskSequenceDto> sequences;

        public InitialConfigResult(List<MessageQueueDefinition> queues, List<TaskSequenceDto> sequences) {
            this.queues = queues != null ? queues : Collections.emptyList();
            this.sequences = sequences != null ? sequences : Collections.emptyList();
        }

        public List<MessageQueueDefinition> getQueues() {
            return queues;
        }

        public List<TaskSequenceDto> getSequences() {
            return sequences;
        }

        public int getQueueCount() {
            return queues.size();
        }

        public int getSequenceCount() {
            return sequences.size();
        }
    }

    /**
     * Checks if an operational resource exists via HEAD request.
     */
    public boolean containsResource(String objectType, String id) throws IOException, InterruptedException {
        URI uri = URI.create(serverUrl + "/" + objectType + "/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Accept", "application/json")
                .method("HEAD", HttpRequest.BodyPublishers.noBody())
                .build();

        log.debug("Executing HEAD {}", uri);
        HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
        int status = response.statusCode();
        return (status >= 200 && status < 300);
    }
}
