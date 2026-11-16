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

package net.fhirfactory.harmonia.model.core.naming;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Objects;

/**
 * Defines subscription criteria for filtering and matching {@link Topic} instances.
 * <p>
 * Supports wildcards ({@code "*"}, empty, or {@code null}) against any level while respecting
 * hierarchical containment down the classification tree:
 * <pre>
 *   Domain &rarr; Model &rarr; Model Version &rarr; Data Element &rarr; Data Element Qualifier
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

    public static TopicSubscription forAll() {
        return new TopicSubscription();
    }

    public static TopicSubscription forDomain(String domain) {
        return new TopicSubscription(domain, WILDCARD, WILDCARD, WILDCARD, WILDCARD);
    }

    public static TopicSubscription forModel(String domain, String model) {
        return new TopicSubscription(domain, model, WILDCARD, WILDCARD, WILDCARD);
    }

    public static TopicSubscription forDataElement(String domain, String model, String modelVersion, String dataElement) {
        return new TopicSubscription(domain, model, modelVersion, dataElement, WILDCARD);
    }

    public static TopicSubscription forHl7(String triggerType) {
        if (StringUtils.isBlank(triggerType) || WILDCARD.equals(triggerType)) {
            return new TopicSubscription("Health", "HL7", WILDCARD, WILDCARD, WILDCARD);
        }
        if (triggerType.contains("^")) {
            String[] parts = triggerType.split("\\^", 2);
            return new TopicSubscription("Health", "HL7", WILDCARD, parts[0], parts[1]);
        }
        return new TopicSubscription("Health", "HL7", WILDCARD, WILDCARD, triggerType.trim());
    }

    public static TopicSubscription forHl7Gateway(String gateway, String triggerType) {
        TopicSubscription sub = forHl7(triggerType);
        if (gateway != null) {
            sub.setSource(gateway);
        }
        return sub;
    }

    public static TopicSubscription forHl7Gateway(String gateway, String messageType, String triggerType) {
        TopicSubscription sub = new TopicSubscription("Health", "HL7", WILDCARD,
                messageType != null ? messageType : WILDCARD,
                triggerType != null ? triggerType : WILDCARD);
        if (gateway != null) {
            sub.setSource(gateway);
        }
        return sub;
    }

    public static TopicSubscription forFhir(String resourceType) {
        return new TopicSubscription("Health", "FHIR", "R5", StringUtils.defaultIfBlank(resourceType, WILDCARD), WILDCARD);
    }

    public static TopicSubscription forFhir(String resourceType, String interaction) {
        return new TopicSubscription("Health", "FHIR", "R5",
                StringUtils.defaultIfBlank(resourceType, WILDCARD),
                StringUtils.defaultIfBlank(interaction, WILDCARD));
    }

    /**
     * Evaluates whether a candidate {@link Topic} matches this subscription criteria.
     *
     * @param topic candidate Topic
     * @return {@code true} if matching, otherwise {@code false}
     */
    public boolean matches(Topic topic) {
        if (topic == null) {
            return false;
        }
        return matchesField(this.domain, topic.getDomain())
                && matchesField(this.model, topic.getModel())
                && matchesField(this.modelVersion, topic.getModelVersion())
                && matchesField(this.dataElement, topic.getDataElement())
                && matchesField(this.dataElementQualifier, topic.getDataElementQualifier());
    }

    /**
     * Evaluates whether candidate classification coordinates match this subscription.
     */
    public boolean matches(String candidateDomain, String candidateModel, String candidateModelVersion,
                           String candidateDataElement, String candidateDataElementQualifier) {
        return matchesField(this.domain, candidateDomain)
                && matchesField(this.model, candidateModel)
                && matchesField(this.modelVersion, candidateModelVersion)
                && matchesField(this.dataElement, candidateDataElement)
                && matchesField(this.dataElementQualifier, candidateDataElementQualifier);
    }

    private boolean matchesField(String filterPattern, String candidateValue) {
        if (isWildcard(filterPattern)) {
            return true;
        }
        if (candidateValue == null) {
            return false;
        }
        return filterPattern.equalsIgnoreCase(candidateValue.trim());
    }

    private boolean isWildcard(String str) {
        return str == null || str.isBlank() || WILDCARD.equals(str.trim());
    }

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
        if (o == null || !(o instanceof TopicSubscription)) return false;
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
                '}';
    }
}
