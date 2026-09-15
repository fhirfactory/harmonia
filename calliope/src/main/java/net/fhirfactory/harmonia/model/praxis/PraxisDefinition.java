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

package net.fhirfactory.harmonia.model.praxis;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.JsonNode;
import net.fhirfactory.harmonia.model.topic.Topic;
import net.fhirfactory.harmonia.model.topic.TopicSubscription;
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
 * Defines the configuration, metadata, and structured execution blueprint for a Task Sequence (Praxis) in the Harmonia platform.
 * <p>
 * <b>Praxis — Task Sequence Definitions &amp; Structure</b>: <i>Praxis</i> (&pi;&rho;&#fb06;&xi;&iota;&sigmaf;) represents
 * the process by which a theory, lesson, or skill is enacted, embodied, or realized through structured action.
 * In Harmonia, Praxis defines the formal structure, configuration, topic subscriptions, and ordered execution stages
 * for clinical and operational task sequences.
 * <p>
 * Contains sequence identification, versioning, endpoint definitions, source queue names,
 * topic subscriptions (hierarchical domain/model/element/qualifier filtering and gateway source routing),
 * and ordered activity identifiers/class names.
 * This class is decoupled from Apache Camel and runtime activity instances, making it ideal
 * for serialization, persistence in Infinispan / JPA stores, and exchange across architectural layers.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PraxisDefinition implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(PraxisDefinition.class);

    /**
     * Unique identifier for the Praxis task sequence (e.g. FQCN or unique key).
     */
    @JsonProperty("praxisId")
    @JsonAlias({"sequenceId", "id"})
    private String praxisId;

    /**
     * Human-readable display name of the Praxis task sequence.
     */
    @JsonProperty("praxisName")
    @JsonAlias({"sequenceName", "name"})
    private String praxisName;


    /**
     * Description of the task sequence workflow.
     */
    @JsonProperty("description")
    @JsonAlias({"sequenceDescription", "praxisDescription"})
    private String description;

    /**
     * Version string of the Praxis definition (defaults to "1.0.0").
     */
    @JsonProperty("version")
    private String version = "1.0.0";

    /**
     * Camel input endpoint URI for the sequence (e.g., "direct:in").
     */
    @JsonProperty("inputEndpoint")
    private String inputEndpoint;

    /**
     * Camel destination output endpoint URI upon successful sequence completion.
     */
    @JsonProperty("outputEndpoint")
    private String outputEndpoint;

    /**
     * Camel error / dead-letter endpoint URI for sequence processing failures.
     */
    @JsonProperty("errorEndpoint")
    private String errorEndpoint;

    /**
     * Name of the source message queue (Petasos / ActiveMQ Artemis) consumed by this sequence.
     */
    @JsonProperty("sourceQueueName")
    private String sourceQueueName;

    /**
     * Flag indicating whether this Praxis sequence is enabled for execution.
     */
    @JsonProperty("enabled")
    private boolean enabled = true;

    /**
     * List of topic subscriptions defining criteria for messages routed to this sequence.
     */
    @JsonProperty("topicSubscriptions")
    private List<TopicSubscription> topicSubscriptions = new ArrayList<>();

    /**
     * List of target gateway instance identifiers (legacy/fallback routing criteria).
     */
    @JsonProperty("targetGatewayInstances")
    private List<String> targetGatewayInstances = new ArrayList<>();

    /**
     * List of target message trigger type codes (legacy/fallback routing criteria).
     */
    @JsonProperty("targetTriggerTypes")
    private List<String> targetTriggerTypes = new ArrayList<>();

    /**
     * Flag indicating whether all gateway instances are matched unconditionally.
     */
    @JsonProperty("matchAllGateways")
    private boolean matchAllGateways = false;

    /**
     * Flag indicating whether all message trigger types are matched unconditionally.
     */
    @JsonProperty("matchAllTriggers")
    private boolean matchAllTriggers = false;

    /**
     * Ordered map of activity sequence index to activity identifier.
     */
    @JsonProperty("activityIds")
    private Map<Integer, String> activityIds = new TreeMap<>();

    /**
     * Ordered map of activity sequence index to activity fully qualified class name.
     */
    @JsonProperty("activityClassNames")
    private Map<Integer, String> activityClassNames = new TreeMap<>();

    /**
     * Default constructor initializing default identifier and name from class metadata.
     */
    public PraxisDefinition() {
        this.praxisName = getClass().getSimpleName();
        this.praxisId = getClass().getName();
    }

    /**
     * Constructor initializing sequence identifier and name.
     *
     * @param praxisId   Unique sequence identifier
     * @param praxisName Human-readable sequence name
     */
    public PraxisDefinition(String praxisId, String praxisName) {
        this.praxisId = praxisId;
        this.praxisName = praxisName;
    }

    /**
     * Copy constructor creating a deep copy of an existing {@link PraxisDefinition}.
     *
     * @param other Existing PraxisDefinition to copy from
     */
    public PraxisDefinition(PraxisDefinition other) {
        if (other != null) {
            this.praxisId = other.praxisId;
            this.praxisName = other.praxisName;
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
     * Evaluates whether this PraxisDefinition matches the given {@link Topic}.
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
     * Evaluates whether this PraxisDefinition matches the given gateway instance ID and trigger type.
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
     * Validates configuration of the Praxis task sequence definition.
     *
     * @return true if valid (praxisId is present), false otherwise
     */
    public boolean validate() {
        if (praxisId == null || praxisId.isBlank()) {
            log.warn("PraxisDefinition validation failed: praxisId is blank");
            return false;
        }
        return true;
    }

    /**
     * Generates a sanitized route ID component for this Praxis sequence.
     *
     * @return sanitized route ID string
     */
    public String generateSequenceRouteId() {
        if (praxisId != null && !praxisId.isBlank()) {
            return praxisId.toLowerCase().replaceAll("[^a-z0-9-_]", "-");
        }
        return getClass().getSimpleName().toLowerCase();
    }

    /**
     * Generates a sanitized route ID component for this Praxis sequence.
     *
     * @return sanitized route ID string
     */
    public String generatePraxisRouteId() {
        return generateSequenceRouteId();
    }

    // =========================================================================
    // Getters and Setters
    // =========================================================================

    /**
     * Returns the unique Praxis identifier.
     *
     * @return unique sequence identifier
     */
    public String getPraxisId() {
        return praxisId;
    }

    /**
     * Sets the unique Praxis identifier.
     *
     * @param praxisId unique sequence identifier
     */
    public void setPraxisId(String praxisId) {
        this.praxisId = praxisId;
    }

    /**
     * Alias for {@link #getPraxisId()} for backwards compatibility.
     *
     * @return unique sequence identifier
     */
    @JsonIgnore
    public String getSequenceId() {
        return getPraxisId();
    }

    /**
     * Alias for {@link #setPraxisId(String)} for backwards compatibility.
     *
     * @param sequenceId unique sequence identifier
     */
    public void setSequenceId(String sequenceId) {
        setPraxisId(sequenceId);
    }

    /**
     * Returns the human-readable Praxis sequence name.
     *
     * @return human-readable sequence name
     */
    public String getPraxisName() {
        return praxisName;
    }

    /**
     * Sets the human-readable Praxis sequence name.
     *
     * @param praxisName human-readable sequence name
     */
    public void setPraxisName(String praxisName) {
        this.praxisName = praxisName;
    }

    /**
     * Alias for {@link #getPraxisName()} for backwards compatibility.
     *
     * @return human-readable sequence name
     */
    @JsonIgnore
    public String getSequenceName() {
        return getPraxisName();
    }

    /**
     * Alias for {@link #setPraxisName(String)} for backwards compatibility.
     *
     * @param sequenceName human-readable sequence name
     */
    public void setSequenceName(String sequenceName) {
        setPraxisName(sequenceName);
    }


    /**
     * Returns the description of the sequence.
     *
     * @return description string
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the sequence.
     *
     * @param description description string
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Alias for {@link #getDescription()} for backwards compatibility.
     *
     * @return description string
     */
    @JsonIgnore
    public String getSequenceDescription() {
        return getDescription();
    }

    /**
     * Alias for {@link #setDescription(String)} for backwards compatibility.
     *
     * @param sequenceDescription description string
     */
    public void setSequenceDescription(String sequenceDescription) {
        setDescription(sequenceDescription);
    }

    /**
     * Returns the effective description from either description field.
     *
     * @return non-blank effective description or empty string
     */
    @JsonIgnore
    public String getEffectiveDescription() {
        if (description != null && !description.isBlank()) {
            return description;
        }
        return "";
    }

    /**
     * Returns the version of the Praxis sequence.
     *
     * @return version string
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the version of the Praxis sequence.
     *
     * @param version version string
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Returns the Camel input endpoint URI.
     *
     * @return input endpoint URI
     */
    public String getInputEndpoint() {
        return inputEndpoint;
    }

    /**
     * Sets the Camel input endpoint URI.
     *
     * @param inputEndpoint input endpoint URI
     */
    public void setInputEndpoint(String inputEndpoint) {
        this.inputEndpoint = inputEndpoint;
    }

    /**
     * Returns the Camel output endpoint URI.
     *
     * @return output endpoint URI
     */
    public String getOutputEndpoint() {
        return outputEndpoint;
    }

    /**
     * Sets the Camel output endpoint URI.
     *
     * @param outputEndpoint output endpoint URI
     */
    public void setOutputEndpoint(String outputEndpoint) {
        this.outputEndpoint = outputEndpoint;
    }

    /**
     * Returns the Camel error endpoint URI.
     *
     * @return error endpoint URI
     */
    public String getErrorEndpoint() {
        return errorEndpoint;
    }

    /**
     * Sets the Camel error endpoint URI.
     *
     * @param errorEndpoint error endpoint URI
     */
    public void setErrorEndpoint(String errorEndpoint) {
        this.errorEndpoint = errorEndpoint;
    }

    /**
     * Returns the source message queue name consumed by this sequence.
     *
     * @return source queue name
     */
    public String getSourceQueueName() {
        return sourceQueueName;
    }

    /**
     * Sets the source message queue name consumed by this sequence.
     *
     * @param sourceQueueName source queue name
     */
    public void setSourceQueueName(String sourceQueueName) {
        this.sourceQueueName = sourceQueueName;
    }

    /**
     * Returns whether this Praxis sequence is enabled.
     *
     * @return true if enabled, false otherwise
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets whether this Praxis sequence is enabled.
     *
     * @param enabled true to enable, false to disable
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Returns the list of configured topic subscriptions.
     *
     * @return list of topic subscriptions
     */
    public List<TopicSubscription> getTopicSubscriptions() {
        return topicSubscriptions != null ? topicSubscriptions : List.of();
    }

    /**
     * Sets the list of configured topic subscriptions.
     *
     * @param topicSubscriptions list of topic subscriptions
     */
    public void setTopicSubscriptions(List<TopicSubscription> topicSubscriptions) {
        this.topicSubscriptions = topicSubscriptions != null ? new ArrayList<>(topicSubscriptions) : new ArrayList<>();
        syncLegacyFilterFields();
    }

    /**
     * Adds a single topic subscription to this sequence.
     *
     * @param subscription topic subscription to add
     */
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

    /**
     * Returns the target gateway instance IDs for fallback filtering.
     *
     * @return list of target gateway instance IDs
     */
    public List<String> getTargetGatewayInstances() {
        return targetGatewayInstances != null ? targetGatewayInstances : List.of();
    }

    /**
     * Sets the target gateway instance IDs for fallback filtering.
     *
     * @param targetGatewayInstances list of target gateway instance IDs
     */
    public void setTargetGatewayInstances(List<String> targetGatewayInstances) {
        this.targetGatewayInstances = targetGatewayInstances != null ? new ArrayList<>(targetGatewayInstances) : new ArrayList<>();
        rebuildTopicSubscriptionsFromLegacy();
    }

    /**
     * Returns the target trigger types for fallback filtering.
     *
     * @return list of target trigger types
     */
    public List<String> getTargetTriggerTypes() {
        return targetTriggerTypes != null ? targetTriggerTypes : List.of();
    }

    /**
     * Sets the target trigger types for fallback filtering.
     *
     * @param targetTriggerTypes list of target trigger types
     */
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

    /**
     * Returns whether this sequence matches all gateway instances.
     *
     * @return true if matching all gateways
     */
    public boolean isMatchAllGateways() {
        return matchAllGateways;
    }

    /**
     * Sets whether this sequence matches all gateway instances.
     *
     * @param matchAllGateways true to match all gateways
     */
    public void setMatchAllGateways(boolean matchAllGateways) {
        this.matchAllGateways = matchAllGateways;
    }

    /**
     * Returns whether this sequence matches all message triggers.
     *
     * @return true if matching all triggers
     */
    public boolean isMatchAllTriggers() {
        return matchAllTriggers;
    }

    /**
     * Sets whether this sequence matches all message triggers.
     *
     * @param matchAllTriggers true to match all triggers
     */
    public void setMatchAllTriggers(boolean matchAllTriggers) {
        this.matchAllTriggers = matchAllTriggers;
    }

    /**
     * Returns the ordered map of activity IDs in the sequence.
     *
     * @return map of order index to activity ID
     */
    public Map<Integer, String> getActivityIds() {
        return activityIds != null ? activityIds : new TreeMap<>();
    }

    /**
     * Sets the ordered map of activity IDs in the sequence.
     *
     * @param activityIds map of order index to activity ID
     */
    public void setActivityIds(Map<Integer, String> activityIds) {
        this.activityIds = activityIds != null ? new TreeMap<>(activityIds) : new TreeMap<>();
    }

    /**
     * Sets the activity IDs from an ordered list.
     *
     * @param activityIdList list of activity IDs in execution order
     */
    public void setActivityIds(List<String> activityIdList) {
        this.activityIds = new TreeMap<>();
        if (activityIdList != null) {
            for (int i = 0; i < activityIdList.size(); i++) {
                this.activityIds.put(i, activityIdList.get(i));
            }
        }
    }

    /**
     * Returns the activity IDs as an ordered list.
     *
     * @return ordered list of activity IDs
     */
    @JsonIgnore
    public List<String> getActivityIdList() {
        if (activityIds == null || activityIds.isEmpty()) {
            return Collections.emptyList();
        }
        return new ArrayList<>(new TreeMap<>(activityIds).values());
    }

    /**
     * Jackson custom deserializer setter for activity IDs supporting both JSON arrays and objects.
     *
     * @param node JSON node representing activity IDs
     */
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

    /**
     * Returns the ordered map of activity class names in the sequence.
     *
     * @return map of order index to activity class name
     */
    public Map<Integer, String> getActivityClassNames() {
        return activityClassNames != null ? activityClassNames : new TreeMap<>();
    }

    /**
     * Sets the ordered map of activity class names in the sequence.
     *
     * @param activityClassNames map of order index to activity class name
     */
    public void setActivityClassNames(Map<Integer, String> activityClassNames) {
        this.activityClassNames = activityClassNames != null ? new TreeMap<>(activityClassNames) : new TreeMap<>();
    }

    /**
     * Sets the activity class names from an ordered list.
     *
     * @param activityClassNameList list of activity class names in execution order
     */
    public void setActivityClassNames(List<String> activityClassNameList) {
        this.activityClassNames = new TreeMap<>();
        if (activityClassNameList != null) {
            for (int i = 0; i < activityClassNameList.size(); i++) {
                this.activityClassNames.put(i, activityClassNameList.get(i));
            }
        }
    }

    /**
     * Returns the activity class names as an ordered list.
     *
     * @return ordered list of activity class names
     */
    @JsonIgnore
    public List<String> getActivityClassNameList() {
        if (activityClassNames == null || activityClassNames.isEmpty()) {
            return Collections.emptyList();
        }
        return new ArrayList<>(new TreeMap<>(activityClassNames).values());
    }

    /**
     * Jackson custom deserializer setter for activity class names supporting both JSON arrays and objects.
     *
     * @param node JSON node representing activity class names
     */
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
        PraxisDefinition that = (PraxisDefinition) o;
        return Objects.equals(praxisId, that.praxisId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(praxisId);
    }

    @Override
    public String toString() {
        return "PraxisDefinition{" +
                "praxisId='" + praxisId + '\'' +
                ", praxisName='" + praxisName + '\'' +
                ", version='" + version + '\'' +
                ", enabled=" + enabled +
                ", topicSubscriptions=" + topicSubscriptions +
                ", targetGateways=" + targetGatewayInstances +
                ", targetTriggers=" + targetTriggerTypes +
                ", activityIds=" + activityIds +
                '}';
    }
}
