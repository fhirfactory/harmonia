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

package net.fhirfactory.harmonia.workflowcli.formatter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.List;
import java.util.Map;

/**
 * Formats CLI outputs for workflow management actions (reload, validate, status).
 */
public class WorkflowOutputFormatter {

    private final ObjectMapper objectMapper;
    private final ObjectMapper prettyMapper;

    public WorkflowOutputFormatter() {
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        this.prettyMapper = new ObjectMapper().registerModule(new JavaTimeModule())
                .enable(SerializationFeature.INDENT_OUTPUT);
    }

    public WorkflowOutputFormatter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.prettyMapper = objectMapper.copy().enable(SerializationFeature.INDENT_OUTPUT);
    }

    /**
     * Formats reload / sync response.
     */
    @SuppressWarnings("unchecked")
    public String formatReloadResponse(Map<String, Object> response, String format, boolean pretty) {
        if ("json".equalsIgnoreCase(format) || pretty) {
            try {
                return (pretty ? prettyMapper : objectMapper).writeValueAsString(response);
            } catch (JsonProcessingException e) {
                return response.toString();
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("================================================================================\n");
        sb.append(" HIE Workflow Reload & Synchronization Report\n");
        sb.append("================================================================================\n");
        sb.append(String.format("  Status:                   %s\n", response.getOrDefault("status", "COMPLETED")));
        sb.append(String.format("  Timestamp:                %s\n", response.getOrDefault("timestamp", "-")));
        sb.append(String.format("  Embedded Broker Running:  %s\n", response.getOrDefault("brokerRunning", "unknown")));
        sb.append(String.format("  Synchronized Queues:      %s\n", response.getOrDefault("synchronizedQueuesCount", "0")));

        Object queuesObj = response.get("synchronizedQueues");
        if (queuesObj instanceof List) {
            List<String> queues = (List<String>) queuesObj;
            for (String q : queues) {
                sb.append(String.format("    - %s\n", q));
            }
        }

        sb.append(String.format("  Sequences Reloaded:       %s\n", response.getOrDefault("sequencesReloaded", "true")));
        sb.append(String.format("  Active Sequences:         %s\n", response.getOrDefault("activeSequencesCount", "0")));

        Object seqIdsObj = response.get("activeSequenceIds");
        if (seqIdsObj instanceof List) {
            List<String> seqIds = (List<String>) seqIdsObj;
            for (String s : seqIds) {
                sb.append(String.format("    * %s\n", s));
            }
        }

        sb.append("================================================================================\n");
        return sb.toString();
    }

    /**
     * Formats validation report.
     */
    @SuppressWarnings("unchecked")
    public String formatValidationReport(Map<String, Object> report, String format, boolean pretty) {
        if ("json".equalsIgnoreCase(format) || pretty) {
            try {
                return (pretty ? prettyMapper : objectMapper).writeValueAsString(report);
            } catch (JsonProcessingException e) {
                return report.toString();
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("================================================================================\n");
        sb.append(" HIE Workflow Configuration Validation Report\n");
        sb.append("================================================================================\n");
        sb.append(String.format("  Overall Validation:       %s\n", report.getOrDefault("status", "UNKNOWN")));
        sb.append(String.format("  Timestamp:                %s\n", report.getOrDefault("timestamp", "-")));
        sb.append("--------------------------------------------------------------------------------\n");

        // Queue Validation
        Object qValObj = report.get("queueValidation");
        if (qValObj instanceof Map) {
            Map<String, Object> qMap = (Map<String, Object>) qValObj;
            sb.append(String.format("  Message Queues:           Total: %s | Valid: %s | Invalid: %s\n",
                    qMap.getOrDefault("totalQueues", 0),
                    qMap.getOrDefault("validQueues", 0),
                    qMap.getOrDefault("invalidQueues", 0)));

            Object qListObj = qMap.get("queues");
            if (qListObj instanceof List) {
                List<Map<String, Object>> qList = (List<Map<String, Object>>) qListObj;
                for (Map<String, Object> q : qList) {
                    boolean v = Boolean.TRUE.equals(q.get("valid"));
                    sb.append(String.format("    [%s] %-36s (routing=%s, durable=%s)%s\n",
                            v ? "OK" : "FAIL",
                            q.get("queueName"),
                            q.get("routingType"),
                            q.get("durable"),
                            v ? "" : " -> " + q.get("error")));
                }
            }
        }

        sb.append("--------------------------------------------------------------------------------\n");

        // Sequence Validation
        Object sValObj = report.get("sequenceValidation");
        if (sValObj instanceof Map) {
            Map<String, Object> sMap = (Map<String, Object>) sValObj;
            sb.append(String.format("  Task Sequences:           Total: %s | Valid: %s | Invalid: %s\n",
                    sMap.getOrDefault("totalSequences", 0),
                    sMap.getOrDefault("validSequences", 0),
                    sMap.getOrDefault("invalidSequences", 0)));

            Object sListObj = sMap.get("sequences");
            if (sListObj instanceof List) {
                List<Map<String, Object>> sList = (List<Map<String, Object>>) sListObj;
                for (Map<String, Object> s : sList) {
                    boolean v = Boolean.TRUE.equals(s.get("valid"));
                    sb.append(String.format("    [%s] %-32s (%s)%s\n",
                            v ? "OK" : "FAIL",
                            s.get("sequenceId"),
                            s.get("sequenceName"),
                            v ? "" : " -> " + s.get("error")));
                }
            }
        }

        sb.append("================================================================================\n");
        return sb.toString();
    }

    /**
     * Formats status report.
     */
    @SuppressWarnings("unchecked")
    public String formatStatus(Map<String, Object> status, String format, boolean pretty) {
        if ("json".equalsIgnoreCase(format) || pretty) {
            try {
                return (pretty ? prettyMapper : objectMapper).writeValueAsString(status);
            } catch (JsonProcessingException e) {
                return status.toString();
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("================================================================================\n");
        sb.append(" HIE Workflow Processor Runtime Status\n");
        sb.append("================================================================================\n");
        sb.append(String.format("  Module:                   %s\n", status.getOrDefault("module", "task-sequence-processor")));
        sb.append(String.format("  Artemis Broker Running:   %s\n", status.getOrDefault("brokerRunning", "false")));
        sb.append(String.format("  Camel Context Started:    %s\n", status.getOrDefault("camelStarted", "false")));
        sb.append(String.format("  Active Sequences Count:   %s\n", status.getOrDefault("activeSequencesCount", "0")));

        Object seqListObj = status.get("activeSequences");
        if (seqListObj instanceof List) {
            List<Map<String, Object>> seqList = (List<Map<String, Object>>) seqListObj;
            sb.append("\n  Registered Pipeline Routes:\n");
            for (Map<String, Object> s : seqList) {
                sb.append(String.format("    * %-30s | %-28s | activities=%s | enabled=%s\n",
                        s.get("sequenceId"),
                        s.get("sequenceName"),
                        s.get("activityCount"),
                        s.get("enabled")));
            }
        }

        Object modulesObj = status.get("clusterModules");
        if (modulesObj instanceof List) {
            List<Map<String, Object>> modules = (List<Map<String, Object>>) modulesObj;
            sb.append("\n  Cluster Modules Running & Ready:\n");
            for (Map<String, Object> m : modules) {
                boolean r = Boolean.TRUE.equals(m.get("ready"));
                sb.append(String.format("    [%s] %-28s | %-24s | %s\n",
                        r ? "READY" : "START",
                        m.get("moduleId"),
                        m.get("moduleType"),
                        m.get("moduleName")));
            }
        }

        sb.append("================================================================================\n");
        return sb.toString();
    }

    /**
     * Formats cluster module status list.
     */
    public String formatModuleStatuses(List<Map<String, Object>> modules, String format, boolean pretty) {
        if ("json".equalsIgnoreCase(format) || pretty) {
            try {
                return (pretty ? prettyMapper : objectMapper).writeValueAsString(modules);
            } catch (JsonProcessingException e) {
                return modules.toString();
            }
        }
        if (modules == null || modules.isEmpty()) {
            return "No cluster modules registered.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Found %d Cluster Module(s):\n\n", modules.size()));
        sb.append(String.format("%-28s | %-30s | %-20s | %-10s | %s\n",
                "MODULE ID", "MODULE NAME", "TYPE", "STATUS", "READY"));
        sb.append("-".repeat(110)).append("\n");
        for (Map<String, Object> m : modules) {
            String id = String.valueOf(m.getOrDefault("moduleId", "-"));
            String name = String.valueOf(m.getOrDefault("moduleName", "-"));
            String type = String.valueOf(m.getOrDefault("moduleType", "-"));
            String status = String.valueOf(m.getOrDefault("status", "-"));
            boolean ready = Boolean.TRUE.equals(m.get("ready"));
            sb.append(String.format("%-28s | %-30s | %-20s | %-10s | %s\n",
                    id, name, type, status, ready ? "YES" : "NO"));
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
}
