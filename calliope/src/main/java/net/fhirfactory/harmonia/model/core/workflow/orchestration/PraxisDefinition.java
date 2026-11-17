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

package net.fhirfactory.harmonia.model.core.workflow.orchestration;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.JsonNode;
import net.fhirfactory.harmonia.model.core.naming.Topic;
import net.fhirfactory.harmonia.model.core.naming.TopicSubscription;
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
        return PraxisDefinitionMatcher.matches(this, topic);
    }

    public boolean matches(String gatewayInstanceId, String triggerType) {
        return PraxisDefinitionMatcher.matches(this, gatewayInstanceId, triggerType);
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



    public String getPraxisId() { return praxisId; }
    public void setPraxisId(String praxisId) { this.praxisId = praxisId; }
    @JsonIgnore
    public String getSequenceId() { return getPraxisId(); }
    public void setSequenceId(String sequenceId) { setPraxisId(sequenceId); }

    public String getPraxisName() { return praxisName; }
    public void setPraxisName(String praxisName) { this.praxisName = praxisName; }
    @JsonIgnore
    public String getSequenceName() { return getPraxisName(); }
    public void setSequenceName(String sequenceName) { setPraxisName(sequenceName); }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    @JsonIgnore
    public String getSequenceDescription() { return getDescription(); }
    public void setSequenceDescription(String sequenceDescription) { setDescription(sequenceDescription); }
    @JsonIgnore
    public String getEffectiveDescription() { return (description != null && !description.isBlank()) ? description : ""; }

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }

    public String getInputEndpoint() { return inputEndpoint; }
    public void setInputEndpoint(String inputEndpoint) { this.inputEndpoint = inputEndpoint; }

    public String getOutputEndpoint() { return outputEndpoint; }
    public void setOutputEndpoint(String outputEndpoint) { this.outputEndpoint = outputEndpoint; }

    public String getErrorEndpoint() { return errorEndpoint; }
    public void setErrorEndpoint(String errorEndpoint) { this.errorEndpoint = errorEndpoint; }

    public String getSourceQueueName() { return sourceQueueName; }
    public void setSourceQueueName(String sourceQueueName) { this.sourceQueueName = sourceQueueName; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public List<TopicSubscription> getTopicSubscriptions() {
        return topicSubscriptions != null ? topicSubscriptions : List.of();
    }

    public void setTopicSubscriptions(List<TopicSubscription> topicSubscriptions) {
        this.topicSubscriptions = topicSubscriptions != null ? new ArrayList<>(topicSubscriptions) : new ArrayList<>();
        syncLegacyFilterFields();
    }

    public void addTopicSubscription(TopicSubscription subscription) {
        if (this.topicSubscriptions == null) this.topicSubscriptions = new ArrayList<>();
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
                String elem = sub.getDataElement();
                String qual = sub.getDataElementQualifier();
                String trig = null;
                if (qual != null && !qual.isBlank() && !"*".equals(qual)) {
                    trig = (elem != null && !elem.isBlank() && !"*".equals(elem)) ? elem + "^" + qual : qual;
                } else if (elem != null && !elem.isBlank() && !"*".equals(elem)) {
                    trig = elem;
                }
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
        List<String> gws = targetGatewayInstances != null && !targetGatewayInstances.isEmpty() ? targetGatewayInstances : List.of("*");
        List<String> trigs = targetTriggerTypes != null && !targetTriggerTypes.isEmpty() ? targetTriggerTypes : List.of("*");
        this.topicSubscriptions = new ArrayList<>();
        for (String gw : gws) {
            for (String trig : trigs) {
                this.topicSubscriptions.add(TopicSubscription.forHl7Gateway(gw, trig));
            }
        }
    }

    public boolean isMatchAllGateways() { return matchAllGateways; }
    public void setMatchAllGateways(boolean matchAllGateways) { this.matchAllGateways = matchAllGateways; }

    public boolean isMatchAllTriggers() { return matchAllTriggers; }
    public void setMatchAllTriggers(boolean matchAllTriggers) { this.matchAllTriggers = matchAllTriggers; }

    public Map<Integer, String> getActivityIds() { return activityIds != null ? activityIds : new TreeMap<>(); }
    public void setActivityIds(Map<Integer, String> activityIds) { this.activityIds = activityIds != null ? new TreeMap<>(activityIds) : new TreeMap<>(); }
    public void setActivityIds(List<String> activityIdList) {
        this.activityIds = new TreeMap<>();
        if (activityIdList != null) {
            for (int i = 0; i < activityIdList.size(); i++) {
                this.activityIds.put(i, activityIdList.get(i));
            }
        }
    }
    @JsonIgnore
    public List<String> getActivityIdList() {
        if (activityIds == null || activityIds.isEmpty()) return Collections.emptyList();
        return new ArrayList<>(new TreeMap<>(activityIds).values());
    }

    @JsonSetter("activityIds")
    public void setActivityIdsFromJson(JsonNode node) {
        this.activityIds = new TreeMap<>();
        if (node == null || node.isNull()) return;
        if (node.isArray()) {
            for (int i = 0; i < node.size(); i++) this.activityIds.put(i, node.get(i).asText());
        } else if (node.isObject()) {
            node.fields().forEachRemaining(entry -> {
                try {
                    this.activityIds.put(Integer.parseInt(entry.getKey()), entry.getValue().asText());
                } catch (NumberFormatException e) {
                    this.activityIds.put(this.activityIds.size(), entry.getValue().asText());
                }
            });
        }
    }

    public Map<Integer, String> getActivityClassNames() { return activityClassNames != null ? activityClassNames : new TreeMap<>(); }
    public void setActivityClassNames(Map<Integer, String> activityClassNames) { this.activityClassNames = activityClassNames != null ? new TreeMap<>(activityClassNames) : new TreeMap<>(); }
    public void setActivityClassNames(List<String> activityClassNameList) {
        this.activityClassNames = new TreeMap<>();
        if (activityClassNameList != null) {
            for (int i = 0; i < activityClassNameList.size(); i++) {
                this.activityClassNames.put(i, activityClassNameList.get(i));
            }
        }
    }
    @JsonIgnore
    public List<String> getActivityClassNameList() {
        if (activityClassNames == null || activityClassNames.isEmpty()) return Collections.emptyList();
        return new ArrayList<>(new TreeMap<>(activityClassNames).values());
    }

    @JsonSetter("activityClassNames")
    public void setActivityClassNamesFromJson(JsonNode node) {
        this.activityClassNames = new TreeMap<>();
        if (node == null || node.isNull()) return;
        if (node.isArray()) {
            for (int i = 0; i < node.size(); i++) this.activityClassNames.put(i, node.get(i).asText());
        } else if (node.isObject()) {
            node.fields().forEachRemaining(entry -> {
                try {
                    this.activityClassNames.put(Integer.parseInt(entry.getKey()), entry.getValue().asText());
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
