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

package net.fhirfactory.hie.model.topic;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Objects;

/**
 * Defines subscription criteria for filtering and matching {@link Topic} instances.
 * <p>
 * Supports wildcards ({@code "*"}, empty, or {@code null}) against any parameter while respecting
 * hierarchical containment down the classification tree:
 * <pre>
 *   Domain --> Model --> Model Version --> Data Element --> Data Element Qualifier
 * </pre>
 * Along with routing parameters:
 * <pre>
 *   Source, Target, Origin, Destination
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TopicSubscription implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String WILDCARD = "*";

    private String domain = WILDCARD;
    private String model = WILDCARD;

    @JsonAlias({"modelVersion", "model_version", "Model Version"})
    private String modelVersion = WILDCARD;

    @JsonAlias({"dataElement", "data_element", "Data Element"})
    private String dataElement = WILDCARD;

    @JsonAlias({"dataElementQualifier", "data_element_qualifier", "Data Element Qualifier", "qualifier"})
    private String dataElementQualifier = WILDCARD;

    private String source = WILDCARD;
    private String target = WILDCARD;
    private String origin = WILDCARD;
    private String destination = WILDCARD;

    /**
     * Default constructor (subscribes to all topics with wildcards).
     */
    public TopicSubscription() {
    }

    /**
     * Constructor for hierarchical classification parameters.
     */
    public TopicSubscription(String domain, String model, String modelVersion, String dataElement, String dataElementQualifier) {
        this.domain = domain != null ? domain : WILDCARD;
        this.model = model != null ? model : WILDCARD;
        this.modelVersion = modelVersion != null ? modelVersion : WILDCARD;
        this.dataElement = dataElement != null ? dataElement : WILDCARD;
        this.dataElementQualifier = dataElementQualifier != null ? dataElementQualifier : WILDCARD;
    }

    /**
     * Constructor with hierarchical parameters and source.
     */
    public TopicSubscription(String domain, String model, String modelVersion, String dataElement, String dataElementQualifier, String source) {
        this(domain, model, modelVersion, dataElement, dataElementQualifier);
        this.source = source != null ? source : WILDCARD;
    }

    /**
     * Full constructor.
     */
    public TopicSubscription(String domain, String model, String modelVersion, String dataElement, String dataElementQualifier,
                             String source, String target, String origin, String destination) {
        this.domain = domain != null ? domain : WILDCARD;
        this.model = model != null ? model : WILDCARD;
        this.modelVersion = modelVersion != null ? modelVersion : WILDCARD;
        this.dataElement = dataElement != null ? dataElement : WILDCARD;
        this.dataElementQualifier = dataElementQualifier != null ? dataElementQualifier : WILDCARD;
        this.source = source != null ? source : WILDCARD;
        this.target = target != null ? target : WILDCARD;
        this.origin = origin != null ? origin : WILDCARD;
        this.destination = destination != null ? destination : WILDCARD;
    }

    /**
     * Copy constructor.
     */
    public TopicSubscription(TopicSubscription other) {
        if (other != null) {
            this.domain = other.domain;
            this.model = other.model;
            this.modelVersion = other.modelVersion;
            this.dataElement = other.dataElement;
            this.dataElementQualifier = other.dataElementQualifier;
            this.source = other.source;
            this.target = other.target;
            this.origin = other.origin;
            this.destination = other.destination;
        }
    }

    // =========================================================================
    // Factory Helpers
    // =========================================================================

    /**
     * Creates a wildcard subscription matching all topics.
     */
    public static TopicSubscription forAll() {
        return new TopicSubscription();
    }

    /**
     * Creates a subscription for HL7 messages of a specific trigger event (e.g. "A01" or "ADT^A01").
     */
    public static TopicSubscription forHl7(String triggerType) {
        return forHl7Gateway(WILDCARD, triggerType);
    }

    /**
     * Creates a subscription for HL7 messages of a specific data element and qualifier.
     */
    public static TopicSubscription forHl7(String dataElement, String dataElementQualifier) {
        return forHl7Gateway(WILDCARD, dataElement, dataElementQualifier);
    }

    /**
     * Creates a subscription for a specific gateway source and trigger type.
     */
    public static TopicSubscription forHl7Gateway(String source, String triggerType) {
        String msgType = "ADT";
        String trigger = triggerType;
        if (triggerType != null && triggerType.contains("^")) {
            String[] parts = triggerType.split("\\^");
            msgType = parts[0];
            trigger = parts.length > 1 ? parts[1] : WILDCARD;
        } else if (triggerType == null || triggerType.isBlank() || WILDCARD.equals(triggerType)) {
            msgType = WILDCARD;
            trigger = WILDCARD;
        }
        return forHl7Gateway(source, msgType, trigger);
    }

    /**
     * Creates a subscription for a specific gateway source, data element, and qualifier.
     */
    public static TopicSubscription forHl7Gateway(String source, String dataElement, String dataElementQualifier) {
        TopicSubscription sub = new TopicSubscription();
        sub.setDomain(Topic.DOMAIN_HEALTH);
        sub.setModel(Topic.MODEL_HL7);
        sub.setModelVersion(Topic.DEFAULT_HL7_VERSION);
        sub.setDataElement(dataElement != null ? dataElement : WILDCARD);
        sub.setDataElementQualifier(dataElementQualifier != null ? dataElementQualifier : WILDCARD);
        sub.setSource(source != null ? source : WILDCARD);
        return sub;
    }

    // =========================================================================
    // Matching & Containment Logic
    // =========================================================================

    /**
     * Evaluates whether a given {@link Topic} satisfies this subscription.
     * <p>
     * Evaluates hierarchical containment (Domain -> Model -> ModelVersion -> DataElement -> DataElementQualifier)
     * and routing attributes (Source, Target, Origin, Destination).
     *
     * @param topic Topic instance to evaluate
     * @return true if topic matches subscription criteria, false otherwise
     */
    public boolean matches(Topic topic) {
        if (topic == null) {
            return false;
        }

        // 1. Hierarchical containment check: Domain
        if (!matchesLevel(this.domain, topic.getDomain())) {
            return false;
        }

        // 2. Hierarchical containment check: Model
        if (!matchesLevel(this.model, topic.getModel())) {
            return false;
        }

        // 3. Hierarchical containment check: Model Version
        if (!matchesLevel(this.modelVersion, topic.getModelVersion())) {
            return false;
        }

        // 4. Hierarchical containment check: Data Element
        if (!matchesLevel(this.dataElement, topic.getDataElement())) {
            return false;
        }

        // 5. Hierarchical containment check: Data Element Qualifier
        if (!matchesQualifierLevel(this.dataElementQualifier, topic.getDataElementQualifier(), topic.getDataElement())) {
            return false;
        }

        // 6. Routing attributes check
        if (!matchesAttribute(this.source, topic.getSource())) {
            return false;
        }
        if (!matchesAttribute(this.target, topic.getTarget())) {
            return false;
        }
        if (!matchesAttribute(this.origin, topic.getOrigin())) {
            return false;
        }
        if (!matchesAttribute(this.destination, topic.getDestination())) {
            return false;
        }

        return true;
    }

    private boolean matchesLevel(String subPattern, String topicVal) {
        if (isWildcard(subPattern) || isWildcard(topicVal)) {
            return true;
        }
        return matchesTokenOrList(subPattern, topicVal);
    }

    private boolean matchesQualifierLevel(String subPattern, String topicQualifier, String topicDataElement) {
        if (isWildcard(subPattern) || isWildcard(topicQualifier)) {
            return true;
        }
        // Handle cases where subPattern contains composite like "ADT^A01"
        if (subPattern.contains("^")) {
            String compositeTopic = (topicDataElement != null ? topicDataElement : "") + "^" + topicQualifier;
            if (matchesTokenOrList(subPattern, compositeTopic)) {
                return true;
            }
            // Also test just the qualifier part
            String[] parts = subPattern.split("\\^");
            if (parts.length > 1 && matchesTokenOrList(parts[1], topicQualifier)) {
                return true;
            }
        }
        return matchesTokenOrList(subPattern, topicQualifier);
    }

    private boolean matchesAttribute(String subPattern, String topicVal) {
        if (isWildcard(subPattern) || isWildcard(topicVal)) {
            return true;
        }
        return matchesTokenOrList(subPattern, topicVal);
    }

    private boolean isWildcard(String pattern) {
        return pattern == null || pattern.isBlank() || WILDCARD.equals(pattern.trim());
    }

    private boolean matchesTokenOrList(String pattern, String value) {
        if (isWildcard(pattern)) {
            return true;
        }
        if (pattern.contains(",")) {
            String[] tokens = pattern.split(",");
            for (String token : tokens) {
                String trimmed = token.trim();
                if (isWildcard(trimmed) || trimmed.equalsIgnoreCase(value.trim())) {
                    return true;
                }
            }
            return false;
        }
        return pattern.trim().equalsIgnoreCase(value.trim());
    }

    // =========================================================================
    // Formatters
    // =========================================================================

    public String toSubscriptionString() {
        return (domain != null ? domain : WILDCARD) + "." +
                (model != null ? model : WILDCARD) + "." +
                (modelVersion != null ? modelVersion : WILDCARD) + "." +
                (dataElement != null ? dataElement : WILDCARD) + "." +
                (dataElementQualifier != null ? dataElementQualifier : WILDCARD) +
                (!isWildcard(source) ? " [source=" + source + "]" : "");
    }

    // =========================================================================
    // Getters and Setters
    // =========================================================================

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain != null ? domain : WILDCARD;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model != null ? model : WILDCARD;
    }

    public String getModelVersion() {
        return modelVersion;
    }

    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion != null ? modelVersion : WILDCARD;
    }

    public String getDataElement() {
        return dataElement;
    }

    public void setDataElement(String dataElement) {
        this.dataElement = dataElement != null ? dataElement : WILDCARD;
    }

    public String getDataElementQualifier() {
        return dataElementQualifier;
    }

    public void setDataElementQualifier(String dataElementQualifier) {
        this.dataElementQualifier = dataElementQualifier != null ? dataElementQualifier : WILDCARD;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source != null ? source : WILDCARD;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target != null ? target : WILDCARD;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin != null ? origin : WILDCARD;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination != null ? destination : WILDCARD;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TopicSubscription that = (TopicSubscription) o;
        return Objects.equals(domain, that.domain) &&
                Objects.equals(model, that.model) &&
                Objects.equals(modelVersion, that.modelVersion) &&
                Objects.equals(dataElement, that.dataElement) &&
                Objects.equals(dataElementQualifier, that.dataElementQualifier) &&
                Objects.equals(source, that.source) &&
                Objects.equals(target, that.target) &&
                Objects.equals(origin, that.origin) &&
                Objects.equals(destination, that.destination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(domain, model, modelVersion, dataElement, dataElementQualifier, source, target, origin, destination);
    }

    @Override
    public String toString() {
        return "TopicSubscription{" +
                "domain='" + domain + '\'' +
                ", model='" + model + '\'' +
                ", modelVersion='" + modelVersion + '\'' +
                ", dataElement='" + dataElement + '\'' +
                ", dataElementQualifier='" + dataElementQualifier + '\'' +
                ", source='" + source + '\'' +
                ", target='" + target + '\'' +
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                '}';
    }
}
