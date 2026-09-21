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

package net.fhirfactory.harmonia.operationscli.formatter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.fhirfactory.harmonia.model.petasos.PetasosQueueDefinition;
import net.fhirfactory.harmonia.operationscli.model.OperationResourceDto;
import net.fhirfactory.harmonia.operationscli.model.PraxisDto;

import java.util.List;

/**
 * Formats CLI outputs into readable tables, JSON, or summary text.
 */
public class OutputFormatter {

    private final ObjectMapper objectMapper;
    private final ObjectMapper prettyMapper;

    public OutputFormatter() {
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        this.prettyMapper = new ObjectMapper().registerModule(new JavaTimeModule())
                .enable(SerializationFeature.INDENT_OUTPUT);
    }

    public OutputFormatter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.prettyMapper = objectMapper.copy().enable(SerializationFeature.INDENT_OUTPUT);
    }

    /**
     * Formats a list of TaskSequences according to the specified output mode.
     */
    public String formatTaskSequences(List<PraxisDto> sequences, String format, boolean pretty) {
        if ("json".equalsIgnoreCase(format) || pretty) {
            try {
                return (pretty ? prettyMapper : objectMapper).writeValueAsString(sequences);
            } catch (JsonProcessingException e) {
                return sequences.toString();
            }
        }

        if (sequences == null || sequences.isEmpty()) {
            return "No TaskSequences found.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Found %d TaskSequence(s):\n\n", sequences.size()));
        sb.append(String.format("%-30s | %-32s | %-8s | %-16s | %-24s | %s\n",
                "SEQUENCE ID", "SEQUENCE NAME", "STATUS", "GATEWAYS", "TRIGGERS", "ACTIVITIES"));
        sb.append("-".repeat(140)).append("\n");

        for (PraxisDto seq : sequences) {
            String id = truncate(seq.getPraxisId(), 30);
            String name = truncate(seq.getPraxisName() != null ? seq.getPraxisName() : "", 32);
            String status = seq.isEnabled() ? "ENABLED" : "DISABLED";
            String gateways = truncate(String.join(",", seq.getTargetGatewayInstances()), 16);
            String triggers = truncate(String.join(",", seq.getTargetTriggerTypes()), 24);
            String activities = String.join(" -> ", seq.getActivityIdList());

            sb.append(String.format("%-30s | %-32s | %-8s | %-16s | %-24s | %s\n",
                    id, name, status, gateways, triggers, activities));
        }

        return sb.toString();
    }

    /**
     * Formats a single TaskSequence in detail.
     */
    public String formatTaskSequenceDetail(PraxisDto seq, String format, boolean pretty) {
        if ("json".equalsIgnoreCase(format) || pretty) {
            try {
                return (pretty ? prettyMapper : objectMapper).writeValueAsString(seq);
            } catch (JsonProcessingException e) {
                return seq.toString();
            }
        }

        if (seq == null) {
            return "TaskSequence not found.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("================================================================================\n");
        sb.append(" Task Sequence: ").append(seq.getPraxisName()).append("\n");
        sb.append("================================================================================\n");
        sb.append(String.format("  Sequence ID:      %s\n", seq.getPraxisId()));
        sb.append(String.format("  Version:          %s\n", seq.getVersion()));
        sb.append(String.format("  Enabled:          %s\n", seq.isEnabled() ? "true" : "false"));
        sb.append(String.format("  Description:      %s\n", seq.getEffectiveDescription()));
        sb.append(String.format("  Target Gateways:  %s\n", String.join(", ", seq.getTargetGatewayInstances())));
        sb.append(String.format("  Target Triggers:  %s\n", String.join(", ", seq.getTargetTriggerTypes())));
        sb.append(String.format("  Activity Count:   %d\n", seq.getActivityIdList().size()));
        sb.append("  Activity Pipeline:\n");
        if (seq.getActivityIdList().isEmpty()) {
            sb.append("    (No activities configured)\n");
        } else {
            for (int i = 0; i < seq.getActivityIdList().size(); i++) {
                sb.append(String.format("    %d. %s\n", i + 1, seq.getActivityIdList().get(i)));
            }
        }
        sb.append("================================================================================\n");
        return sb.toString();
    }

    /**
     * Formats a list of MessageQueueDefinition according to the specified output mode.
     */
    public String formatMessageQueues(List<PetasosQueueDefinition> queues, String format, boolean pretty) {
        if ("json".equalsIgnoreCase(format) || pretty) {
            try {
                return (pretty ? prettyMapper : objectMapper).writeValueAsString(queues);
            } catch (JsonProcessingException e) {
                return queues.toString();
            }
        }

        if (queues == null || queues.isEmpty()) {
            return "No MessageQueues found.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Found %d MessageQueue(s):\n\n", queues.size()));
        sb.append(String.format("%-36s | %-10s | %-8s | %-8s | %-24s | %s\n",
                "QUEUE ID / NAME", "ROUTING", "DURABLE", "STATUS", "GATEWAY INSTANCE", "DESCRIPTION"));
        sb.append("-".repeat(130)).append("\n");

        for (PetasosQueueDefinition q : queues) {
            String id = truncate(q.getQueueName(), 36);
            String routing = q.getRoutingType() != null ? q.getRoutingType() : "ANYCAST";
            String durable = q.isDurable() ? "YES" : "NO";
            String status = q.isEnabled() ? "ENABLED" : "DISABLED";
            String gateway = truncate(q.getGatewayInstanceId() != null ? q.getGatewayInstanceId() : "-", 24);
            String desc = truncate(q.getDescription() != null ? q.getDescription() : "", 40);

            sb.append(String.format("%-36s | %-10s | %-8s | %-8s | %-24s | %s\n",
                    id, routing, durable, status, gateway, desc));
        }

        return sb.toString();
    }

    /**
     * Formats a single MessageQueueDefinition in detail.
     */
    public String formatMessageQueueDetail(PetasosQueueDefinition queue, String format, boolean pretty) {
        if ("json".equalsIgnoreCase(format) || pretty) {
            try {
                return (pretty ? prettyMapper : objectMapper).writeValueAsString(queue);
            } catch (JsonProcessingException e) {
                return queue.toString();
            }
        }

        if (queue == null) {
            return "MessageQueue not found.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("================================================================================\n");
        sb.append(" Message Queue: ").append(queue.getQueueName()).append("\n");
        sb.append("================================================================================\n");
        sb.append(String.format("  Queue ID:         %s\n", queue.getQueueId()));
        sb.append(String.format("  Queue Name:       %s\n", queue.getQueueName()));
        sb.append(String.format("  Address:          %s\n", queue.getAddress()));
        sb.append(String.format("  Routing Type:     %s\n", queue.getRoutingType()));
        sb.append(String.format("  Durable:          %s\n", queue.isDurable() ? "true" : "false"));
        sb.append(String.format("  Enabled:          %s\n", queue.isEnabled() ? "true" : "false"));
        sb.append(String.format("  Max Consumers:    %s\n", queue.getMaxConsumers() != null && queue.getMaxConsumers() > 0 ? queue.getMaxConsumers() : "unlimited"));
        sb.append(String.format("  Filter:           %s\n", queue.getFilter() != null ? queue.getFilter() : "none"));
        sb.append(String.format("  Gateway Instance: %s\n", queue.getGatewayInstanceId() != null ? queue.getGatewayInstanceId() : "all/shared"));
        sb.append(String.format("  Description:      %s\n", queue.getDescription() != null ? queue.getDescription() : ""));
        sb.append("================================================================================\n");
        return sb.toString();
    }

    /**
     * Formats generic operational resources list.
     */
    public String formatOperationResources(List<OperationResourceDto> resources, String format, boolean pretty) {
        if ("json".equalsIgnoreCase(format) || pretty) {
            try {
                return (pretty ? prettyMapper : objectMapper).writeValueAsString(resources);
            } catch (JsonProcessingException e) {
                return resources.toString();
            }
        }

        if (resources == null || resources.isEmpty()) {
            return "No operational resources found.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Found %d Operational Resource(s):\n\n", resources.size()));
        sb.append(String.format("%-6s | %-16s | %-36s | %-8s | %-24s | %s\n",
                "ID", "OBJECT TYPE", "OBJECT ID", "VERSION", "LAST UPDATED", "PAYLOAD PREVIEW"));
        sb.append("-".repeat(130)).append("\n");

        for (OperationResourceDto res : resources) {
            String id = res.getId() != null ? String.valueOf(res.getId()) : "-";
            String type = truncate(res.getObjectType() != null ? res.getObjectType() : "", 16);
            String objId = truncate(res.getObjectId() != null ? res.getObjectId() : "", 36);
            String ver = res.getVersionId() != null ? String.valueOf(res.getVersionId()) : "1";
            String updated = res.getLastUpdated() != null ? res.getLastUpdated().toString() : "-";
            String preview = truncate(res.getDataJson() != null ? res.getDataJson().replace("\n", " ").replace("\r", "") : "", 32);

            sb.append(String.format("%-6s | %-16s | %-36s | %-8s | %-24s | %s\n",
                    id, type, objId, ver, updated, preview));
        }

        return sb.toString();
    }

    /**
     * Formats raw JSON string with optional pretty printing.
     */
    public String formatJsonString(String json, boolean pretty) {
        if (json == null) {
            return "";
        }
        if (!pretty) {
            return json;
        }
        try {
            JsonNode node = objectMapper.readTree(json);
            return prettyMapper.writeValueAsString(node);
        } catch (Exception e) {
            return json;
        }
    }

    private String truncate(String val, int maxLen) {
        if (val == null) return "";
        if (val.length() <= maxLen) return val;
        return val.substring(0, maxLen - 3) + "...";
    }
}
