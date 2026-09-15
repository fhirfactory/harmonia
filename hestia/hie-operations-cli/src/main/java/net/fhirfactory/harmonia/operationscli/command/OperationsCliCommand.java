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

package net.fhirfactory.harmonia.operationscli.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.fhirfactory.harmonia.model.petasos.PetasosQueueDefinition;
import net.fhirfactory.harmonia.operationscli.client.OperationsHttpClient;
import net.fhirfactory.harmonia.operationscli.formatter.OutputFormatter;
import net.fhirfactory.harmonia.operationscli.model.OperationResourceDto;
import net.fhirfactory.harmonia.operationscli.model.PraxisDto;
import picocli.CommandLine;
import picocli.CommandLine.*;

import java.io.File;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * Main command and CLI interface for the HIE Operations JPA Server.
 */
@Command(
        name = "hie-operations-cli",
        mixinStandardHelpOptions = true,
        version = "hie-operations-cli 1.0.0",
        description = "Command-line tool for interacting with the HIE Operations JPA Server and managing queues, task sequences, and operational resources.",
        subcommands = {
                OperationsCliCommand.ListSequencesCmd.class,
                OperationsCliCommand.GetSequenceCmd.class,
                OperationsCliCommand.CreateSequenceCmd.class,
                OperationsCliCommand.SaveSequenceCmd.class,
                OperationsCliCommand.DeleteSequenceCmd.class,
                OperationsCliCommand.ListQueuesCmd.class,
                OperationsCliCommand.GetQueueCmd.class,
                OperationsCliCommand.CreateQueueCmd.class,
                OperationsCliCommand.SaveQueueCmd.class,
                OperationsCliCommand.DeleteQueueCmd.class,
                OperationsCliCommand.InitConfigCmd.class,
                OperationsCliCommand.ListResourcesCmd.class,
                OperationsCliCommand.GetResourceCmd.class,
                OperationsCliCommand.SaveResourceCmd.class,
                OperationsCliCommand.DeleteResourceCmd.class,
                OperationsCliCommand.CheckResourceCmd.class
        }
)
public class OperationsCliCommand implements Callable<Integer> {

    @Option(names = {"-s", "--server-url", "--url"}, description = "Operations JPA Server base URL. Default: ${DEFAULT-VALUE} (or env OPS_SERVER_URL)")
    private String serverUrl;

    @Option(names = {"-H", "--host"}, description = "Operations JPA Server host. Default: localhost (or env OPS_HOST)")
    private String host;

    @Option(names = {"-p", "--port"}, description = "Operations JPA Server port. Default: 8085 (or env OPS_PORT)")
    private Integer port;

    @Option(names = {"--timeout"}, description = "HTTP request timeout in seconds. Default: ${DEFAULT-VALUE}", defaultValue = "10")
    private int timeout = 10;

    @Option(names = {"-o", "--output"}, description = "Output format: table, json, raw, summary. Default: ${DEFAULT-VALUE}", defaultValue = "table")
    private String outputFormat = "table";

    @Option(names = {"--pretty"}, description = "Format JSON output with indentation")
    private boolean pretty = false;

    @Option(names = {"-v", "--verbose"}, description = "Enable verbose logging")
    private boolean verbose = false;

    // Direct Action Flags for Task Sequences
    @Option(names = {"-l", "--list-sequences"}, description = "List all TaskSequences stored in the Operations JPA server")
    private boolean listSequences = false;

    @Option(names = {"--get-sequence"}, description = "Get a specific TaskSequence by sequence ID", paramLabel = "<sequenceId>")
    private String getSequenceId;

    @Option(names = {"--delete-sequence"}, description = "Delete a specific TaskSequence by sequence ID", paramLabel = "<sequenceId>")
    private String deleteSequenceId;

    @Option(names = {"--create-sequence"}, description = "Create/save a TaskSequence using parameters or payload")
    private boolean createSequence = false;

    // Direct Action Flags for Message Queues
    @Option(names = {"--list-queues"}, description = "List all MessageQueues stored in the Operations JPA server")
    private boolean listQueues = false;

    @Option(names = {"--get-queue"}, description = "Get a specific MessageQueue by queue ID/name", paramLabel = "<queueId>")
    private String getQueueId;

    @Option(names = {"--delete-queue"}, description = "Delete a specific MessageQueue by queue ID/name", paramLabel = "<queueId>")
    private String deleteQueueId;

    @Option(names = {"--create-queue", "--save-queue"}, description = "Create/save a MessageQueue using parameters or payload")
    private boolean createQueue = false;

    // Initial Config Flag
    @Option(names = {"--init-config", "--seed-initial-config"}, description = "Populate initial configuration for queues and task sequences from initial-config.json")
    private boolean initConfig = false;

    // Parameters for Queue Creation
    @Option(names = {"--queue-id"}, description = "Queue ID (defaults to queue name if omitted)")
    private String queueId;

    @Option(names = {"-q", "--queue-name"}, description = "Message Queue Name (e.g. task.event.queue.pas-gw)")
    private String queueName;

    @Option(names = {"--address"}, description = "Artemis broker address (defaults to queue name if omitted)")
    private String address;

    @Option(names = {"--routing-type"}, description = "Routing type: ANYCAST or MULTICAST. Default: ANYCAST", defaultValue = "ANYCAST")
    private String routingType = "ANYCAST";

    @Option(names = {"--durable"}, description = "Whether queue is durable on broker. Default: false")
    private boolean durable = false;

    @Option(names = {"--max-consumers"}, description = "Max concurrent consumers (-1 for unlimited)")
    private Integer maxConsumers = -1;

    @Option(names = {"--filter"}, description = "JMS message filter expression")
    private String filter;

    @Option(names = {"--gateway-id", "--gateway-instance-id"}, description = "Associated MLLP Gateway instance ID")
    private String gatewayInstanceId;

    @Option(names = {"--queue-description"}, description = "Queue description")
    private String queueDescription;

    // Parameters for Sequence Creation
    @Option(names = {"--sequence-id"}, description = "Sequence ID (e.g. seq-admission-pipeline)")
    private String sequenceId;

    @Option(names = {"--sequence-name"}, description = "Human-readable Sequence Name")
    private String sequenceName;

    @Option(names = {"--sequence-description"}, description = "Sequence Description")
    private String sequenceDescription;

    @Option(names = {"--activities", "--activity-ids"}, description = "Comma-separated list of activity IDs (e.g. message-queue-to-exchange,patient-identity-update)")
    private String activities;

    @Option(names = {"--gateways"}, description = "Comma-separated target gateway instances (e.g. pas-gw,*)")
    private String targetGateways;

    @Option(names = {"--triggers"}, description = "Comma-separated target trigger types (e.g. A01,A08,*)")
    private String targetTriggers;

    @Option(names = {"--source-queue"}, description = "Dedicated source queue name for the sequence")
    private String sourceQueueName;

    // Direct Action Flags for Operational Resources
    @Option(names = {"--list-resources"}, description = "List all operational resources (optionally filtered by --type)")
    private boolean listResources = false;

    @Option(names = {"-t", "--type", "--object-type"}, description = "Operational resource object type (e.g. tasksequence, messagequeue, config)")
    private String objectType;

    @Option(names = {"-i", "--id", "--object-id"}, description = "Operational resource object ID")
    private String objectId;

    @Option(names = {"--get"}, description = "Get resource by --type and --id")
    private boolean getResource = false;

    @Option(names = {"--delete"}, description = "Delete resource by --type and --id")
    private boolean deleteResource = false;

    @Option(names = {"--check", "--head", "--exists"}, description = "Check if resource exists by --type and --id")
    private boolean checkResource = false;

    @Option(names = {"-f", "--file"}, description = "Path to file containing JSON payload to save")
    private File file;

    @Option(names = {"-d", "--data", "--json-data"}, description = "Inline JSON payload string to save")
    private String jsonData;

    @Option(names = {"--save", "--put"}, description = "Save/create resource using --type, --id, and payload from --file or --data")
    private boolean saveResource = false;

    private PrintStream out = System.out;
    private PrintStream err = System.err;
    private OperationsHttpClient customClient;
    private OutputFormatter formatter = new OutputFormatter();

    public OperationsCliCommand() {
    }

    public OperationsCliCommand(PrintStream out, PrintStream err) {
        this.out = out;
        this.err = err;
    }

    public OperationsCliCommand(PrintStream out, PrintStream err, OperationsHttpClient customClient) {
        this.out = out;
        this.err = err;
        this.customClient = customClient;
    }

    public String resolveEffectiveServerUrl() {
        if (serverUrl != null && !serverUrl.isBlank()) {
            return serverUrl;
        }
        String envUrl = System.getenv("OPS_SERVER_URL");
        if (envUrl != null && !envUrl.isBlank()) {
            return envUrl;
        }
        String envHieUrl = System.getenv("HIE_OPERATIONS_SERVER_URL");
        if (envHieUrl != null && !envHieUrl.isBlank()) {
            return envHieUrl;
        }
        String h = host != null && !host.isBlank() ? host : System.getenv().getOrDefault("OPS_HOST", "localhost");
        int p = port != null ? port : Integer.parseInt(System.getenv().getOrDefault("OPS_PORT", "8085"));
        return "http://" + h + ":" + p + "/api/operations";
    }

    public OperationsHttpClient getHttpClient() {
        if (customClient != null) {
            return customClient;
        }
        String url = resolveEffectiveServerUrl();
        return new OperationsHttpClient(url, timeout);
    }

    @Override
    public Integer call() {
        OperationsHttpClient client = getHttpClient();

        try {
            if (initConfig) {
                return executeInitConfig(client);
            }
            if (listQueues) {
                return executeListQueues(client);
            }
            if (getQueueId != null && !getQueueId.isBlank()) {
                return executeGetQueue(client, getQueueId);
            }
            if (deleteQueueId != null && !deleteQueueId.isBlank()) {
                return executeDeleteQueue(client, deleteQueueId);
            }
            if (createQueue) {
                return executeCreateQueue(client);
            }
            if (listSequences) {
                return executeListSequences(client);
            }
            if (getSequenceId != null && !getSequenceId.isBlank()) {
                return executeGetSequence(client, getSequenceId);
            }
            if (deleteSequenceId != null && !deleteSequenceId.isBlank()) {
                return executeDeleteSequence(client, deleteSequenceId);
            }
            if (createSequence) {
                return executeCreateSequence(client);
            }
            if (listResources) {
                return executeListResources(client, objectType);
            }
            if (getResource) {
                if (objectType == null || objectId == null) {
                    err.println("Error: Both --type and --id are required when using --get");
                    return 1;
                }
                return executeGetResource(client, objectType, objectId);
            }
            if (deleteResource) {
                if (objectType == null || objectId == null) {
                    err.println("Error: Both --type and --id are required when using --delete");
                    return 1;
                }
                return executeDeleteResource(client, objectType, objectId);
            }
            if (checkResource) {
                if (objectType == null || objectId == null) {
                    err.println("Error: Both --type and --id are required when using --check");
                    return 1;
                }
                return executeCheckResource(client, objectType, objectId);
            }
            if (saveResource) {
                if (objectType == null || objectId == null) {
                    err.println("Error: Both --type and --id are required when saving a resource");
                    return 1;
                }
                String payload = loadPayload();
                return executeSaveResource(client, objectType, objectId, payload);
            }

            // If no action specified, show usage help
            CommandLine.usage(this, out);
            return 0;
        } catch (Exception e) {
            err.println("Error: " + e.getMessage());
            if (verbose) {
                e.printStackTrace(err);
            }
            return 1;
        }
    }

    private String loadPayload() throws Exception {
        if (jsonData != null && !jsonData.isBlank()) {
            return jsonData;
        }
        if (file != null) {
            if (!file.exists()) {
                throw new IllegalArgumentException("File not found: " + file.getAbsolutePath());
            }
            return Files.readString(file.toPath());
        }
        throw new IllegalArgumentException("Payload required via --file or --data");
    }

    private int executeListQueues(OperationsHttpClient client) throws Exception {
        if (verbose) {
            out.println("Connecting to Operations Server at " + client.getServerUrl() + " to list all MessageQueues...");
        }
        List<PetasosQueueDefinition> queues = client.listMessageQueues();
        out.println(formatter.formatMessageQueues(queues, outputFormat, pretty));
        return 0;
    }

    private int executeGetQueue(OperationsHttpClient client, String queueId) throws Exception {
        if (verbose) {
            out.println("Fetching MessageQueue '" + queueId + "' from " + client.getServerUrl() + "...");
        }
        Optional<PetasosQueueDefinition> qOpt = client.getMessageQueue(queueId);
        if (qOpt.isPresent()) {
            out.println(formatter.formatMessageQueueDetail(qOpt.get(), outputFormat, pretty));
            return 0;
        } else {
            err.println("MessageQueue not found: " + queueId);
            return 1;
        }
    }

    private int executeDeleteQueue(OperationsHttpClient client, String queueId) throws Exception {
        if (verbose) {
            out.println("Deleting MessageQueue '" + queueId + "' from " + client.getServerUrl() + "...");
        }
        boolean deleted = client.deleteMessageQueue(queueId);
        if (deleted) {
            out.println("Successfully deleted MessageQueue: " + queueId);
            return 0;
        } else {
            err.println("MessageQueue not found: " + queueId);
            return 1;
        }
    }

    private int executeCreateQueue(OperationsHttpClient client) throws Exception {
        PetasosQueueDefinition qDef;

        if ((jsonData != null && !jsonData.isBlank()) || file != null) {
            String payload = loadPayload();
            qDef = client.getObjectMapper().readValue(payload, PetasosQueueDefinition.class);
        } else {
            String effectiveName = queueName != null && !queueName.isBlank() ? queueName : queueId;
            if (effectiveName == null || effectiveName.isBlank()) {
                err.println("Error: --queue-name (or --queue-id, --file, --data) is required to create a queue");
                return 1;
            }
            String effectiveId = queueId != null && !queueId.isBlank() ? queueId : effectiveName;
            String effectiveAddress = address != null && !address.isBlank() ? address : effectiveName;

            qDef = new PetasosQueueDefinition();
            qDef.setQueueId(effectiveId);
            qDef.setQueueName(effectiveName);
            qDef.setAddress(effectiveAddress);
            qDef.setRoutingType(routingType != null ? routingType : "ANYCAST");
            qDef.setDurable(durable);
            qDef.setMaxConsumers(maxConsumers != null ? maxConsumers : -1);
            qDef.setFilter(filter);
            qDef.setGatewayInstanceId(gatewayInstanceId);
            qDef.setDescription(queueDescription);
            qDef.setEnabled(true);
        }

        if (queueId != null && !queueId.isBlank()) {
            qDef.setQueueId(queueId);
        }
        if (queueName != null && !queueName.isBlank()) {
            qDef.setQueueName(queueName);
        }

        if (verbose) {
            out.println("Saving MessageQueue '" + qDef.getQueueName() + "' to " + client.getServerUrl() + "...");
        }

        String result = client.saveMessageQueue(qDef);
        out.println("Successfully created/saved MessageQueue: " + qDef.getQueueName());
        if (verbose || "json".equalsIgnoreCase(outputFormat)) {
            out.println(formatter.formatJsonString(result, pretty));
        }
        return 0;
    }

    private int executeCreateSequence(OperationsHttpClient client) throws Exception {
        PraxisDto seq;

        if ((jsonData != null && !jsonData.isBlank()) || file != null) {
            String payload = loadPayload();
            seq = client.getObjectMapper().readValue(payload, PraxisDto.class);
        } else {
            if (sequenceId == null || sequenceId.isBlank()) {
                err.println("Error: --sequence-id (or --file, --data) is required to create a TaskSequence");
                return 1;
            }
            String effectiveName = sequenceName != null && !sequenceName.isBlank() ? sequenceName : sequenceId;

            seq = new PraxisDto();
            seq.setPraxisId(sequenceId);
            seq.setPraxisName(effectiveName);
            seq.setDescription(sequenceDescription);
            seq.setSourceQueueName(sourceQueueName);
            seq.setEnabled(true);

            if (targetGateways != null && !targetGateways.isBlank()) {
                seq.setTargetGatewayInstances(Arrays.stream(targetGateways.split(",")).map(String::trim).filter(s -> !s.isEmpty()).toList());
            } else {
                seq.setTargetGatewayInstances(List.of("*"));
            }

            if (targetTriggers != null && !targetTriggers.isBlank()) {
                seq.setTargetTriggerTypes(Arrays.stream(targetTriggers.split(",")).map(String::trim).filter(s -> !s.isEmpty()).toList());
            } else {
                seq.setTargetTriggerTypes(List.of("*"));
            }

            if (activities != null && !activities.isBlank()) {
                Map<Integer, String> actMap = new TreeMap<>();
                String[] parts = activities.split(",");
                for (int i = 0; i < parts.length; i++) {
                    String act = parts[i].trim();
                    if (!act.isEmpty()) {
                        actMap.put(i + 1, act);
                    }
                }
                seq.setActivityIds(actMap);
            }
        }

        if (sequenceId != null && !sequenceId.isBlank()) {
            seq.setPraxisId(sequenceId);
        }
        if (sequenceName != null && !sequenceName.isBlank()) {
            seq.setPraxisName(sequenceName);
        }

        if (verbose) {
            out.println("Saving TaskSequence '" + seq.getPraxisId() + "' to " + client.getServerUrl() + "...");
        }

        String result = client.saveTaskSequence(seq);
        out.println("Successfully created/saved TaskSequence: " + seq.getPraxisId());
        if (verbose || "json".equalsIgnoreCase(outputFormat)) {
            out.println(formatter.formatJsonString(result, pretty));
        }
        return 0;
    }

    private int executeInitConfig(OperationsHttpClient client) throws Exception {
        OperationsHttpClient.InitialConfigResult res;
        if (file != null) {
            if (!file.exists()) {
                throw new IllegalArgumentException("Config file not found: " + file.getAbsolutePath());
            }
            String content = Files.readString(file.toPath());
            out.println("Applying initial configuration from file: " + file.getAbsolutePath() + "...");
            res = client.applyInitialConfig(content);
        } else if (jsonData != null && !jsonData.isBlank()) {
            out.println("Applying initial configuration from inline JSON...");
            res = client.applyInitialConfig(jsonData);
        } else {
            out.println("Applying default initial configuration from classpath (initial-config.json)...");
            res = client.applyDefaultInitialConfig();
        }

        out.println("Successfully applied initial configuration:");
        out.println("  - Message Queues populated: " + res.getQueueCount());
        for (PetasosQueueDefinition q : res.getQueues()) {
            out.println("      * " + q.getQueueName() + " (" + q.getRoutingType() + ")");
        }
        out.println("  - Task Sequences populated: " + res.getSequenceCount());
        for (PraxisDto s : res.getSequences()) {
            out.println("      * " + s.getPraxisId() + " (" + s.getPraxisName() + ")");
        }
        return 0;
    }

    private int executeListSequences(OperationsHttpClient client) throws Exception {
        if (verbose) {
            out.println("Connecting to Operations Server at " + client.getServerUrl() + " to list all TaskSequences...");
        }
        List<PraxisDto> sequences = client.listTaskSequences();
        out.println(formatter.formatTaskSequences(sequences, outputFormat, pretty));
        return 0;
    }

    private int executeGetSequence(OperationsHttpClient client, String sequenceId) throws Exception {
        if (verbose) {
            out.println("Fetching TaskSequence '" + sequenceId + "' from " + client.getServerUrl() + "...");
        }
        Optional<PraxisDto> seqOpt = client.getTaskSequence(sequenceId);
        if (seqOpt.isPresent()) {
            out.println(formatter.formatTaskSequenceDetail(seqOpt.get(), outputFormat, pretty));
            return 0;
        } else {
            err.println("TaskSequence not found: " + sequenceId);
            return 1;
        }
    }

    private int executeDeleteSequence(OperationsHttpClient client, String sequenceId) throws Exception {
        if (verbose) {
            out.println("Deleting TaskSequence '" + sequenceId + "' from " + client.getServerUrl() + "...");
        }
        boolean deleted = client.deleteTaskSequence(sequenceId);
        if (deleted) {
            out.println("Successfully deleted TaskSequence: " + sequenceId);
            return 0;
        } else {
            err.println("TaskSequence not found: " + sequenceId);
            return 1;
        }
    }

    private int executeListResources(OperationsHttpClient client, String type) throws Exception {
        if (verbose) {
            out.println("Listing operational resources" + (type != null ? " of type '" + type + "'" : "") + " from " + client.getServerUrl() + "...");
        }
        List<OperationResourceDto> list = (type != null && !type.isBlank())
                ? client.listResourcesByType(type)
                : client.listAllResources();
        out.println(formatter.formatOperationResources(list, outputFormat, pretty));
        return 0;
    }

    private int executeGetResource(OperationsHttpClient client, String type, String id) throws Exception {
        if (verbose) {
            out.println("Fetching resource " + type + "/" + id + " from " + client.getServerUrl() + "...");
        }
        Optional<String> jsonOpt = client.getResourceJson(type, id);
        if (jsonOpt.isPresent()) {
            out.println(formatter.formatJsonString(jsonOpt.get(), pretty));
            return 0;
        } else {
            err.println("Resource not found: " + type + "/" + id);
            return 1;
        }
    }

    private int executeSaveResource(OperationsHttpClient client, String type, String id, String payload) throws Exception {
        if (verbose) {
            out.println("Saving resource " + type + "/" + id + " to " + client.getServerUrl() + "...");
        }
        String result = client.saveResourceJson(type, id, payload);
        out.println("Successfully saved resource: " + type + "/" + id);
        if (verbose || "json".equalsIgnoreCase(outputFormat)) {
            out.println(formatter.formatJsonString(result, pretty));
        }
        return 0;
    }

    private int executeDeleteResource(OperationsHttpClient client, String type, String id) throws Exception {
        if (verbose) {
            out.println("Deleting resource " + type + "/" + id + " from " + client.getServerUrl() + "...");
        }
        boolean deleted = client.deleteResource(type, id);
        if (deleted) {
            out.println("Successfully deleted resource: " + type + "/" + id);
            return 0;
        } else {
            err.println("Resource not found: " + type + "/" + id);
            return 1;
        }
    }

    private int executeCheckResource(OperationsHttpClient client, String type, String id) throws Exception {
        if (verbose) {
            out.println("Checking resource existence for " + type + "/" + id + "...");
        }
        boolean exists = client.containsResource(type, id);
        if (exists) {
            out.println("Resource exists: " + type + "/" + id);
            return 0;
        } else {
            out.println("Resource does not exist: " + type + "/" + id);
            return 1;
        }
    }

    // =========================================================================
    // Subcommands
    // =========================================================================

    @Command(name = "list-sequences", aliases = {"sequences", "seq-list"}, description = "List all TaskSequences stored in the Operations JPA server")
    public static class ListSequencesCmd implements Callable<Integer> {
        @ParentCommand
        private OperationsCliCommand parent;

        @Override
        public Integer call() {
            try {
                return parent.executeListSequences(parent.getHttpClient());
            } catch (Exception e) {
                parent.err.println("Error: " + e.getMessage());
                return 1;
            }
        }
    }

    @Command(name = "get-sequence", aliases = {"seq-get"}, description = "Get a TaskSequence definition by sequence ID")
    public static class GetSequenceCmd implements Callable<Integer> {
        @ParentCommand
        private OperationsCliCommand parent;

        @Parameters(index = "0", description = "TaskSequence identifier (e.g. seq-admission-pipeline)")
        private String sequenceId;

        @Override
        public Integer call() {
            try {
                return parent.executeGetSequence(parent.getHttpClient(), sequenceId);
            } catch (Exception e) {
                parent.err.println("Error: " + e.getMessage());
                return 1;
            }
        }
    }

    @Command(name = "create-sequence", aliases = {"seq-create", "add-sequence"}, description = "Create a new TaskSequence definition")
    public static class CreateSequenceCmd implements Callable<Integer> {
        @ParentCommand
        private OperationsCliCommand parent;

        @Option(names = {"-i", "--id", "--sequence-id"}, description = "Sequence ID (e.g. seq-admission-pipeline)")
        private String sequenceId;

        @Option(names = {"-n", "--name", "--sequence-name"}, description = "Human-readable Sequence Name")
        private String sequenceName;

        @Option(names = {"--desc", "--description", "--sequence-description"}, description = "Sequence Description")
        private String sequenceDescription;

        @Option(names = {"-a", "--activities", "--activity-ids"}, description = "Comma-separated list of activity IDs")
        private String activities;

        @Option(names = {"-g", "--gateways"}, description = "Comma-separated target gateway instances (e.g. pas-gw,*)")
        private String targetGateways;

        @Option(names = {"-t", "--triggers"}, description = "Comma-separated target trigger types (e.g. A01,A08,*)")
        private String targetTriggers;

        @Option(names = {"--source-queue"}, description = "Dedicated source queue name for the sequence")
        private String sourceQueueName;

        @Option(names = {"-f", "--file"}, description = "Path to JSON file defining the TaskSequence")
        private File file;

        @Option(names = {"-d", "--data"}, description = "Inline JSON string defining the TaskSequence")
        private String data;

        @Override
        public Integer call() {
            try {
                if (file != null) parent.file = file;
                if (data != null) parent.jsonData = data;
                if (sequenceId != null) parent.sequenceId = sequenceId;
                if (sequenceName != null) parent.sequenceName = sequenceName;
                if (sequenceDescription != null) parent.sequenceDescription = sequenceDescription;
                if (activities != null) parent.activities = activities;
                if (targetGateways != null) parent.targetGateways = targetGateways;
                if (targetTriggers != null) parent.targetTriggers = targetTriggers;
                if (sourceQueueName != null) parent.sourceQueueName = sourceQueueName;

                return parent.executeCreateSequence(parent.getHttpClient());
            } catch (Exception e) {
                parent.err.println("Error: " + e.getMessage());
                return 1;
            }
        }
    }

    @Command(name = "save-sequence", aliases = {"seq-save"}, description = "Save or update a TaskSequence from file or JSON string")
    public static class SaveSequenceCmd implements Callable<Integer> {
        @ParentCommand
        private OperationsCliCommand parent;

        @Option(names = {"-i", "--id"}, description = "Sequence ID (optional if defined in JSON payload)")
        private String sequenceId;

        @Option(names = {"-f", "--file"}, description = "Path to JSON file defining the TaskSequence")
        private File file;

        @Option(names = {"-d", "--data"}, description = "Inline JSON string defining the TaskSequence")
        private String data;

        @Override
        public Integer call() {
            try {
                String payload = data;
                if (file != null) {
                    payload = Files.readString(file.toPath());
                }
                if (payload == null || payload.isBlank()) {
                    parent.err.println("Error: Must provide sequence definition via --file or --data");
                    return 1;
                }
                ObjectMapper mapper = parent.getHttpClient().getObjectMapper();
                PraxisDto dto = mapper.readValue(payload, PraxisDto.class);
                if (sequenceId != null && !sequenceId.isBlank()) {
                    dto.setPraxisId(sequenceId);
                }
                if (dto.getPraxisId() == null || dto.getPraxisId().isBlank()) {
                    parent.err.println("Error: sequenceId must be specified");
                    return 1;
                }
                String result = parent.getHttpClient().saveTaskSequence(dto);
                parent.out.println("Successfully saved TaskSequence: " + dto.getPraxisId());
                if (parent.verbose) {
                    parent.out.println(parent.formatter.formatJsonString(result, parent.pretty));
                }
                return 0;
            } catch (Exception e) {
                parent.err.println("Error: " + e.getMessage());
                return 1;
            }
        }
    }

    @Command(name = "delete-sequence", aliases = {"seq-delete"}, description = "Delete a TaskSequence by sequence ID")
    public static class DeleteSequenceCmd implements Callable<Integer> {
        @ParentCommand
        private OperationsCliCommand parent;

        @Parameters(index = "0", description = "TaskSequence identifier to delete")
        private String sequenceId;

        @Override
        public Integer call() {
            try {
                return parent.executeDeleteSequence(parent.getHttpClient(), sequenceId);
            } catch (Exception e) {
                parent.err.println("Error: " + e.getMessage());
                return 1;
            }
        }
    }

    @Command(name = "list-queues", aliases = {"queues", "queue-list"}, description = "List all MessageQueues stored in the Operations JPA server")
    public static class ListQueuesCmd implements Callable<Integer> {
        @ParentCommand
        private OperationsCliCommand parent;

        @Override
        public Integer call() {
            try {
                return parent.executeListQueues(parent.getHttpClient());
            } catch (Exception e) {
                parent.err.println("Error: " + e.getMessage());
                return 1;
            }
        }
    }

    @Command(name = "get-queue", aliases = {"queue-get"}, description = "Get a MessageQueue definition by queue ID/name")
    public static class GetQueueCmd implements Callable<Integer> {
        @ParentCommand
        private OperationsCliCommand parent;

        @Parameters(index = "0", description = "Queue identifier (e.g. task.event.queue.pas-gw)")
        private String queueId;

        @Override
        public Integer call() {
            try {
                return parent.executeGetQueue(parent.getHttpClient(), queueId);
            } catch (Exception e) {
                parent.err.println("Error: " + e.getMessage());
                return 1;
            }
        }
    }

    @Command(name = "create-queue", aliases = {"add-queue", "queue-create"}, description = "Create a new MessageQueue definition")
    public static class CreateQueueCmd implements Callable<Integer> {
        @ParentCommand
        private OperationsCliCommand parent;

        @Option(names = {"-q", "--queue-name", "--name"}, description = "Message Queue Name (e.g. task.event.queue.pas-gw)")
        private String queueName;

        @Option(names = {"-i", "--queue-id", "--id"}, description = "Queue ID (defaults to queue name if omitted)")
        private String queueId;

        @Option(names = {"--address"}, description = "Artemis broker address (defaults to queue name if omitted)")
        private String address;

        @Option(names = {"-r", "--routing-type"}, description = "Routing type: ANYCAST or MULTICAST. Default: ANYCAST", defaultValue = "ANYCAST")
        private String routingType = "ANYCAST";

        @Option(names = {"--durable"}, description = "Whether queue is durable on broker. Default: false")
        private boolean durable = false;

        @Option(names = {"--max-consumers"}, description = "Max concurrent consumers (-1 for unlimited)")
        private Integer maxConsumers = -1;

        @Option(names = {"--filter"}, description = "JMS message filter expression")
        private String filter;

        @Option(names = {"-g", "--gateway-id", "--gateway-instance-id"}, description = "Associated MLLP Gateway instance ID")
        private String gatewayInstanceId;

        @Option(names = {"--desc", "--description", "--queue-description"}, description = "Queue description")
        private String queueDescription;

        @Option(names = {"-f", "--file"}, description = "Path to JSON file defining the MessageQueue")
        private File file;

        @Option(names = {"-d", "--data"}, description = "Inline JSON string defining the MessageQueue")
        private String data;

        @Override
        public Integer call() {
            try {
                if (file != null) parent.file = file;
                if (data != null) parent.jsonData = data;
                if (queueName != null) parent.queueName = queueName;
                if (queueId != null) parent.queueId = queueId;
                if (address != null) parent.address = address;
                if (routingType != null) parent.routingType = routingType;
                parent.durable = durable;
                if (maxConsumers != null) parent.maxConsumers = maxConsumers;
                if (filter != null) parent.filter = filter;
                if (gatewayInstanceId != null) parent.gatewayInstanceId = gatewayInstanceId;
                if (queueDescription != null) parent.queueDescription = queueDescription;

                return parent.executeCreateQueue(parent.getHttpClient());
            } catch (Exception e) {
                parent.err.println("Error: " + e.getMessage());
                return 1;
            }
        }
    }

    @Command(name = "save-queue", aliases = {"queue-save"}, description = "Save or update a MessageQueue from file or JSON string")
    public static class SaveQueueCmd implements Callable<Integer> {
        @ParentCommand
        private OperationsCliCommand parent;

        @Option(names = {"-i", "--id"}, description = "Queue ID (optional if defined in JSON payload)")
        private String queueId;

        @Option(names = {"-f", "--file"}, description = "Path to JSON file defining the MessageQueue")
        private File file;

        @Option(names = {"-d", "--data"}, description = "Inline JSON string defining the MessageQueue")
        private String data;

        @Override
        public Integer call() {
            try {
                String payload = data;
                if (file != null) {
                    payload = Files.readString(file.toPath());
                }
                if (payload == null || payload.isBlank()) {
                    parent.err.println("Error: Must provide queue definition via --file or --data");
                    return 1;
                }
                ObjectMapper mapper = parent.getHttpClient().getObjectMapper();
                PetasosQueueDefinition dto = mapper.readValue(payload, PetasosQueueDefinition.class);
                if (queueId != null && !queueId.isBlank()) {
                    dto.setQueueId(queueId);
                }
                if (dto.getQueueId() == null || dto.getQueueId().isBlank()) {
                    parent.err.println("Error: queueId must be specified");
                    return 1;
                }
                String result = parent.getHttpClient().saveMessageQueue(dto);
                parent.out.println("Successfully saved MessageQueue: " + dto.getQueueName());
                if (parent.verbose) {
                    parent.out.println(parent.formatter.formatJsonString(result, parent.pretty));
                }
                return 0;
            } catch (Exception e) {
                parent.err.println("Error: " + e.getMessage());
                return 1;
            }
        }
    }

    @Command(name = "delete-queue", aliases = {"queue-delete"}, description = "Delete a MessageQueue by queue ID/name")
    public static class DeleteQueueCmd implements Callable<Integer> {
        @ParentCommand
        private OperationsCliCommand parent;

        @Parameters(index = "0", description = "Queue identifier to delete")
        private String queueId;

        @Override
        public Integer call() {
            try {
                return parent.executeDeleteQueue(parent.getHttpClient(), queueId);
            } catch (Exception e) {
                parent.err.println("Error: " + e.getMessage());
                return 1;
            }
        }
    }

    @Command(name = "init-config", aliases = {"seed-config", "load-initial-config"}, description = "Populate initial configuration for queues and task sequences into the HIE database")
    public static class InitConfigCmd implements Callable<Integer> {
        @ParentCommand
        private OperationsCliCommand parent;

        @Option(names = {"-f", "--file"}, description = "Custom JSON file with initial config (defaults to packaged initial-config.json)")
        private File file;

        @Option(names = {"-d", "--data"}, description = "Inline JSON configuration string")
        private String data;

        @Override
        public Integer call() {
            try {
                if (file != null) parent.file = file;
                if (data != null) parent.jsonData = data;
                return parent.executeInitConfig(parent.getHttpClient());
            } catch (Exception e) {
                parent.err.println("Error: " + e.getMessage());
                return 1;
            }
        }
    }

    @Command(name = "list-resources", aliases = {"list", "resources"}, description = "List all operational resources, optionally filtered by type")
    public static class ListResourcesCmd implements Callable<Integer> {
        @ParentCommand
        private OperationsCliCommand parent;

        @Option(names = {"-t", "--type"}, description = "Object type filter (e.g. tasksequence, config)")
        private String type;

        @Override
        public Integer call() {
            try {
                return parent.executeListResources(parent.getHttpClient(), type);
            } catch (Exception e) {
                parent.err.println("Error: " + e.getMessage());
                return 1;
            }
        }
    }

    @Command(name = "get-resource", aliases = {"get"}, description = "Get an operational resource by object type and ID")
    public static class GetResourceCmd implements Callable<Integer> {
        @ParentCommand
        private OperationsCliCommand parent;

        @Parameters(index = "0", description = "Object type (e.g. tasksequence, config)")
        private String type;

        @Parameters(index = "1", description = "Object ID")
        private String id;

        @Override
        public Integer call() {
            try {
                return parent.executeGetResource(parent.getHttpClient(), type, id);
            } catch (Exception e) {
                parent.err.println("Error: " + e.getMessage());
                return 1;
            }
        }
    }

    @Command(name = "save-resource", aliases = {"save", "put"}, description = "Save or update an operational resource")
    public static class SaveResourceCmd implements Callable<Integer> {
        @ParentCommand
        private OperationsCliCommand parent;

        @Parameters(index = "0", description = "Object type (e.g. tasksequence, config)")
        private String type;

        @Parameters(index = "1", description = "Object ID")
        private String id;

        @Option(names = {"-f", "--file"}, description = "Path to file containing resource JSON payload")
        private File file;

        @Option(names = {"-d", "--data"}, description = "Inline resource JSON payload")
        private String data;

        @Override
        public Integer call() {
            try {
                String payload = data;
                if (file != null) {
                    payload = Files.readString(file.toPath());
                }
                if (payload == null) {
                    payload = "";
                }
                return parent.executeSaveResource(parent.getHttpClient(), type, id, payload);
            } catch (Exception e) {
                parent.err.println("Error: " + e.getMessage());
                return 1;
            }
        }
    }

    @Command(name = "delete-resource", aliases = {"delete"}, description = "Delete an operational resource by object type and ID")
    public static class DeleteResourceCmd implements Callable<Integer> {
        @ParentCommand
        private OperationsCliCommand parent;

        @Parameters(index = "0", description = "Object type (e.g. tasksequence, config)")
        private String type;

        @Parameters(index = "1", description = "Object ID")
        private String id;

        @Override
        public Integer call() {
            try {
                return parent.executeDeleteResource(parent.getHttpClient(), type, id);
            } catch (Exception e) {
                parent.err.println("Error: " + e.getMessage());
                return 1;
            }
        }
    }

    @Command(name = "check-resource", aliases = {"check", "head", "exists"}, description = "Check if an operational resource exists")
    public static class CheckResourceCmd implements Callable<Integer> {
        @ParentCommand
        private OperationsCliCommand parent;

        @Parameters(index = "0", description = "Object type (e.g. tasksequence, config)")
        private String type;

        @Parameters(index = "1", description = "Object ID")
        private String id;

        @Override
        public Integer call() {
            try {
                return parent.executeCheckResource(parent.getHttpClient(), type, id);
            } catch (Exception e) {
                parent.err.println("Error: " + e.getMessage());
                return 1;
            }
        }
    }

    // Getters and Setters for testing and configuration
    public void setOut(PrintStream out) {
        this.out = out;
    }

    public void setErr(PrintStream err) {
        this.err = err;
    }

    public void setCustomClient(OperationsHttpClient customClient) {
        this.customClient = customClient;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }

    public void setPretty(boolean pretty) {
        this.pretty = pretty;
    }
}
