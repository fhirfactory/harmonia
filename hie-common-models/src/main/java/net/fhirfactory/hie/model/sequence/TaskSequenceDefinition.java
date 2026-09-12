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

package net.fhirfactory.hie.model.sequence;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.JsonNode;
import net.fhirfactory.hie.model.topic.Topic;
import net.fhirfactory.hie.model.topic.TopicSubscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * Defines the configuration and metadata for a Task Sequence in the HIE platform.
 * <p>
 * Contains sequence identification, versioning, endpoint definitions, source queue names,
 * topic subscriptions (hierarchical domain/model/element/qualifier filtering and gateway source routing),
 * and ordered activity identifiers/class names.
 * This class is decoupled from Apache Camel and runtime activity instances, making it ideal
 * for serialization, persistence in Infinispan / JPA stores, and exchange across architectural layers.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskSequenceDefinition implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(TaskSequenceDefinition.class);

    private String sequenceId;
    private String sequenceName;

    @JsonAlias({"description", "sequenceDescription"})
    private String sequenceDescription;

    private String description;
    private String version = "1.0.0";
    private String inputEndpoint;
    private String outputEndpoint;
    private String errorEndpoint;
    private String sourceQueueName;
    private boolean enabled = true;

    private List<TopicSubscription> topicSubscriptions = new ArrayList<>();
    private List<String> targetGatewayInstances = new ArrayList<>();
    private List<String> targetTriggerTypes = new ArrayList<>();
    private boolean matchAllGateways = false;
    private boolean matchAllTriggers = false;

    private Map<Integer, String> activityIds = new TreeMap<>();
    private Map<Integer, String> activityClassNames = new TreeMap<>();

    /**
     * Default constructor.
     */
    public TaskSequenceDefinition() {
        this.sequenceName = getClass().getSimpleName();
        this.sequenceId = getClass().getName();
    }

    /**
     * Constructor with sequence identifier and name.
     *
     * @param sequenceId   Unique sequence identifier
     * @param sequenceName Human-readable sequence name
     */
    public TaskSequenceDefinition(String sequenceId, String sequenceName) {
        this.sequenceId = sequenceId;
        this.sequenceName = sequenceName;
    }

    /**
     * Copy constructor.
     *
     * @param other Existing TaskSequenceDefinition to copy from
     */
    public TaskSequenceDefinition(TaskSequenceDefinition other) {
        if (other != null) {
            this.sequenceId = other.sequenceId;
            this.sequenceName = other.sequenceName;
            this.sequenceDescription = other.sequenceDescription;
            this.description = other.description;
            this.version = other.version;
            this.inputEndpoint = other.inputEndpoint;
            this.outputEndpoint = other.outputEndpoint;
            this.errorEndpoint = other.errorEndpoint;
            this.sourceQueueName = other.sourceQueueName;
            this.enabled = other.enabled;
            this.topicSubscriptions = other.topicSubscriptions != null
                    ? new ArrayList<>(other.topicSubscriptions.stream().map(TopicSubscription::new).toList())
                    : new ArrayList<>();
            this.targetGatewayInstances = other.targetGatewayInstances != null ? new ArrayList<>(other.targetGatewayInstances) : new ArrayList<>();
            this.targetTriggerTypes = other.targetTriggerTypes != null ? new ArrayList<>(other.targetTriggerTypes) : new ArrayList<>();
            this.matchAllGateways = other.matchAllGateways;
            this.matchAllTriggers = other.matchAllTriggers;
            this.activityIds = other.activityIds != null ? new TreeMap<>(other.activityIds) : new TreeMap<>();
            this.activityClassNames = other.activityClassNames != null ? new TreeMap<>(other.activityClassNames) : new TreeMap<>();
        }
    }

    // =========================================================================
    // Matching Logic
    // =========================================================================

    /**
     * Evaluates whether this TaskSequenceDefinition matches the given {@link Topic}.
     *
     * @param topic Ingested message topic
     * @return true if topic matches any configured TopicSubscription, or fallback criteria
     */
    public boolean matches(Topic topic) {
        if (topic == null) {
            return false;
        }

        if (topicSubscriptions != null && !topicSubscriptions.isEmpty()) {
            for (TopicSubscription sub : topicSubscriptions) {
                if (sub != null && sub.matches(topic)) {
                    return true;
                }
            }
            return false;
        }

        return matches(topic.getSource(), topic.getCompositeTrigger());
    }

    /**
     * Evaluates whether this TaskSequenceDefinition matches the given gateway instance ID and trigger type.
     *
     * @param gatewayInstanceId Originating gateway instance identifier (null or "*" matches any)
     * @param triggerType       HL7 trigger type code (e.g., "A01", "ADT^A01", "R01", null or "*" matches any)
     * @return true if both gateway and trigger match criteria
     */
    public boolean matches(String gatewayInstanceId, String triggerType) {
        Topic topic = Topic.fromHl7(triggerType, gatewayInstanceId);
        if (topicSubscriptions != null && !topicSubscriptions.isEmpty()) {
            for (TopicSubscription sub : topicSubscriptions) {
                if (sub != null && sub.matches(topic)) {
                    return true;
                }
            }
            return false;
        }

        boolean gatewayMatches = matchAllGateways;
        if (!gatewayMatches) {
            if (gatewayInstanceId == null || "*".equals(gatewayInstanceId)
                    || targetGatewayInstances == null || targetGatewayInstances.isEmpty() || targetGatewayInstances.contains("*")) {
                gatewayMatches = true;
            } else {
                for (String targetGw : targetGatewayInstances) {
                    if (targetGw != null && (targetGw.equalsIgnoreCase(gatewayInstanceId) || "*".equals(targetGw))) {
                        gatewayMatches = true;
                        break;
                    }
                }
            }
        }

        boolean triggerMatches = matchAllTriggers;
        if (!triggerMatches) {
            if (triggerType == null || "*".equals(triggerType)
                    || targetTriggerTypes == null || targetTriggerTypes.isEmpty() || targetTriggerTypes.contains("*")) {
                triggerMatches = true;
            } else {
                String normalizedTrigger = triggerType.contains("^")
                        ? triggerType.substring(triggerType.indexOf('^') + 1)
                        : triggerType;
                for (String targetTrig : targetTriggerTypes) {
                    if (targetTrig != null) {
                        String normalizedTarget = targetTrig.contains("^")
                                ? targetTrig.substring(targetTrig.indexOf('^') + 1)
                                : targetTrig;
                        if (targetTrig.equalsIgnoreCase(triggerType)
                                || normalizedTarget.equalsIgnoreCase(normalizedTrigger)
                                || "*".equals(targetTrig)) {
                            triggerMatches = true;
                            break;
                        }
                    }
                }
            }
        }

        return gatewayMatches && triggerMatches;
    }

    /**
     * Validates configuration of the task sequence definition.
     *
     * @return true if valid, false otherwise
     */
    public boolean validate() {
        if (sequenceId == null || sequenceId.isBlank()) {
            log.warn("TaskSequenceDefinition validation failed: sequenceId is blank");
            return false;
        }
        return true;
    }

    /**
     * Generates a sanitized route ID component for this sequence.
     *
     * @return sanitized route ID string
     */
    public String generateSequenceRouteId() {
        if (sequenceId != null && !sequenceId.isBlank()) {
            return sequenceId.toLowerCase().replaceAll("[^a-z0-9-_]", "-");
        }
        return getClass().getSimpleName().toLowerCase();
    }

    // =========================================================================
    // Getters and Setters
    // =========================================================================

    public String getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(String sequenceId) {
        this.sequenceId = sequenceId;
    }

    public String getSequenceName() {
        return sequenceName;
    }

    public void setSequenceName(String sequenceName) {
        this.sequenceName = sequenceName;
    }

    public String getSequenceDescription() {
        return sequenceDescription != null ? sequenceDescription : description;
    }

    public void setSequenceDescription(String sequenceDescription) {
        this.sequenceDescription = sequenceDescription;
        if (this.description == null) {
            this.description = sequenceDescription;
        }
    }

    public String getDescription() {
        return description != null ? description : sequenceDescription;
    }

    public void setDescription(String description) {
        this.description = description;
        if (this.sequenceDescription == null) {
            this.sequenceDescription = description;
        }
    }

    public String getEffectiveDescription() {
        if (sequenceDescription != null && !sequenceDescription.isBlank()) {
            return sequenceDescription;
        }
        if (description != null && !description.isBlank()) {
            return description;
        }
        return "";
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getInputEndpoint() {
        return inputEndpoint;
    }

    public void setInputEndpoint(String inputEndpoint) {
        this.inputEndpoint = inputEndpoint;
    }

    public String getOutputEndpoint() {
        return outputEndpoint;
    }

    public void setOutputEndpoint(String outputEndpoint) {
        this.outputEndpoint = outputEndpoint;
    }

    public String getErrorEndpoint() {
        return errorEndpoint;
    }

    public void setErrorEndpoint(String errorEndpoint) {
        this.errorEndpoint = errorEndpoint;
    }

    public String getSourceQueueName() {
        return sourceQueueName;
    }

    public void setSourceQueueName(String sourceQueueName) {
        this.sourceQueueName = sourceQueueName;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<TopicSubscription> getTopicSubscriptions() {
        return topicSubscriptions != null ? topicSubscriptions : List.of();
    }

    public void setTopicSubscriptions(List<TopicSubscription> topicSubscriptions) {
        this.topicSubscriptions = topicSubscriptions != null ? new ArrayList<>(topicSubscriptions) : new ArrayList<>();
        syncLegacyFilterFields();
    }

    public void addTopicSubscription(TopicSubscription subscription) {
        if (this.topicSubscriptions == null) {
            this.topicSubscriptions = new ArrayList<>();
        }
        if (subscription != null) {
            this.topicSubscriptions.add(subscription);
            syncLegacyFilterFields();
        }
    }

    private void syncLegacyFilterFields() {
        if (this.topicSubscriptions != null && !this.topicSubscriptions.isEmpty()) {
            List<String> gws = new ArrayList<>();
            List<String> triggers = new ArrayList<>();
            for (TopicSubscription sub : this.topicSubscriptions) {
                if (sub.getSource() != null && !gws.contains(sub.getSource())) {
                    gws.add(sub.getSource());
                }
                String trig = sub.getDataElementQualifier() != null && !sub.getDataElementQualifier().isBlank()
                        ? sub.getDataElementQualifier()
                        : sub.getDataElement();
                if (trig != null && !triggers.contains(trig)) {
                    triggers.add(trig);
                }
            }
            if (!gws.isEmpty()) this.targetGatewayInstances = gws;
            if (!triggers.isEmpty()) this.targetTriggerTypes = triggers;
        }
    }

    public List<String> getTargetGatewayInstances() {
        return targetGatewayInstances != null ? targetGatewayInstances : List.of();
    }

    public void setTargetGatewayInstances(List<String> targetGatewayInstances) {
        this.targetGatewayInstances = targetGatewayInstances != null ? new ArrayList<>(targetGatewayInstances) : new ArrayList<>();
        rebuildTopicSubscriptionsFromLegacy();
    }

    public List<String> getTargetTriggerTypes() {
        return targetTriggerTypes != null ? targetTriggerTypes : List.of();
    }

    public void setTargetTriggerTypes(List<String> targetTriggerTypes) {
        this.targetTriggerTypes = targetTriggerTypes != null ? new ArrayList<>(targetTriggerTypes) : new ArrayList<>();
        rebuildTopicSubscriptionsFromLegacy();
    }

    private void rebuildTopicSubscriptionsFromLegacy() {
        List<String> gws = targetGatewayInstances != null && !targetGatewayInstances.isEmpty()
                ? targetGatewayInstances : List.of("*");
        List<String> trigs = targetTriggerTypes != null && !targetTriggerTypes.isEmpty()
                ? targetTriggerTypes : List.of("*");

        this.topicSubscriptions = new ArrayList<>();
        for (String gw : gws) {
            for (String trig : trigs) {
                this.topicSubscriptions.add(TopicSubscription.forHl7Gateway(gw, trig));
            }
        }
    }

    public boolean isMatchAllGateways() {
        return matchAllGateways;
    }

    public void setMatchAllGateways(boolean matchAllGateways) {
        this.matchAllGateways = matchAllGateways;
    }

    public boolean isMatchAllTriggers() {
        return matchAllTriggers;
    }

    public void setMatchAllTriggers(boolean matchAllTriggers) {
        this.matchAllTriggers = matchAllTriggers;
    }

    public Map<Integer, String> getActivityIds() {
        return activityIds != null ? activityIds : new TreeMap<>();
    }

    public void setActivityIds(Map<Integer, String> activityIds) {
        this.activityIds = activityIds != null ? new TreeMap<>(activityIds) : new TreeMap<>();
    }

    public void setActivityIds(List<String> activityIdList) {
        this.activityIds = new TreeMap<>();
        if (activityIdList != null) {
            for (int i = 0; i < activityIdList.size(); i++) {
                this.activityIds.put(i, activityIdList.get(i));
            }
        }
    }

    public List<String> getActivityIdList() {
        if (activityIds == null || activityIds.isEmpty()) {
            return Collections.emptyList();
        }
        return new ArrayList<>(new TreeMap<>(activityIds).values());
    }

    @JsonSetter("activityIds")
    public void setActivityIdsFromJson(JsonNode node) {
        this.activityIds = new TreeMap<>();
        if (node == null || node.isNull()) {
            return;
        }
        if (node.isArray()) {
            for (int i = 0; i < node.size(); i++) {
                this.activityIds.put(i, node.get(i).asText());
            }
        } else if (node.isObject()) {
            node.fields().forEachRemaining(entry -> {
                try {
                    int order = Integer.parseInt(entry.getKey());
                    this.activityIds.put(order, entry.getValue().asText());
                } catch (NumberFormatException e) {
                    this.activityIds.put(this.activityIds.size(), entry.getValue().asText());
                }
            });
        }
    }

    public Map<Integer, String> getActivityClassNames() {
        return activityClassNames != null ? activityClassNames : new TreeMap<>();
    }

    public void setActivityClassNames(Map<Integer, String> activityClassNames) {
        this.activityClassNames = activityClassNames != null ? new TreeMap<>(activityClassNames) : new TreeMap<>();
    }

    public void setActivityClassNames(List<String> activityClassNameList) {
        this.activityClassNames = new TreeMap<>();
        if (activityClassNameList != null) {
            for (int i = 0; i < activityClassNameList.size(); i++) {
                this.activityClassNames.put(i, activityClassNameList.get(i));
            }
        }
    }

    public List<String> getActivityClassNameList() {
        if (activityClassNames == null || activityClassNames.isEmpty()) {
            return Collections.emptyList();
        }
        return new ArrayList<>(new TreeMap<>(activityClassNames).values());
    }

    @JsonSetter("activityClassNames")
    public void setActivityClassNamesFromJson(JsonNode node) {
        this.activityClassNames = new TreeMap<>();
        if (node == null || node.isNull()) {
            return;
        }
        if (node.isArray()) {
            for (int i = 0; i < node.size(); i++) {
                this.activityClassNames.put(i, node.get(i).asText());
            }
        } else if (node.isObject()) {
            node.fields().forEachRemaining(entry -> {
                try {
                    int order = Integer.parseInt(entry.getKey());
                    this.activityClassNames.put(order, entry.getValue().asText());
                } catch (NumberFormatException e) {
                    this.activityClassNames.put(this.activityClassNames.size(), entry.getValue().asText());
                }
            });
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskSequenceDefinition that = (TaskSequenceDefinition) o;
        return Objects.equals(sequenceId, that.sequenceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sequenceId);
    }

    @Override
    public String toString() {
        return "TaskSequenceDefinition{" +
                "sequenceId='" + sequenceId + '\'' +
                ", sequenceName='" + sequenceName + '\'' +
                ", version='" + version + '\'' +
                ", enabled=" + enabled +
                ", topicSubscriptions=" + topicSubscriptions +
                ", targetGateways=" + targetGatewayInstances +
                ", targetTriggers=" + targetTriggerTypes +
                ", activityIds=" + activityIds +
                '}';
    }
}
