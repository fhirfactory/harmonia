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

package net.fhirfactory.harmonia.model.core.information;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.fhirfactory.harmonia.model.security.FhirConfidentialityEnum;
import net.fhirfactory.harmonia.model.security.HarmoniaSecurityLabelEnum;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Encapsulates the Information Unit's required security criteria, compartments, and classification constraints.
 * <p>
 * <b>Architectural Context &amp; Authority:</b>
 * In accordance with {@code DRP-Information-Unit-Framework.Final.001.md} (§§5.3, 13; IU-05):
 * <ul>
 *   <li>{@code InformationSecurityContext} is the IU-side security requirements specification.</li>
 *   <li>It expresses the criteria, compartments, and action masks required to access or mutate this IU,
 *       expressed using <b>explicit Topic references or TopicMasks</b>.</li>
 *   <li><b>CRITICAL DISTINCTION:</b> {@code InformationSecurityContext} describes <i>what the Information Unit requires</i>,
 *       <b>not</b> what a user or principal is permitted to do. User/service permissions and Passports belong exclusively
 *       to Themis and {@code ThemisSecurityContext}.</li>
 *   <li><b>CRITICAL INDEPENDENCE:</b> {@code InformationSecurityContext} is <b>100% independent of {@link InformationCoordinates}</b>.
 *       Operational gateway, endpoint, location, or owner metadata does not establish security clearance or compartments.</li>
 * </ul>
 * <p>
 * <b>Defensive Programming:</b>
 * <ul>
 *   <li>All sets and maps are defensively copied upon construction and returned as unmodifiable collections.</li>
 *   <li>Null collections are safely normalized to empty unmodifiable sets/maps.</li>
 *   <li>Immutable value object design guarantees thread-safety.</li>
 * </ul>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class InformationSecurityContext implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final InformationSecurityContext EMPTY = new InformationSecurityContext(
            Set.of(), Set.of(), Set.of(), Set.of(), Set.of(), Set.of(), null, Map.of());

    private final Set<String> unitCriteria;
    private final Set<String> containerCriteria;
    private final Set<String> contentCriteria;
    private final Set<String> actionMasks;
    private final Set<String> compartmentMasks;
    private final Set<HarmoniaSecurityLabelEnum> securityLabels;
    private final FhirConfidentialityEnum confidentiality;
    private final Map<String, String> attributes;

    /**
     * Canonical constructor establishing validated security requirement criteria.
     *
     * @param unitCriteria      unit-level Topic references or TopicMasks (optional)
     * @param containerCriteria container-level Topic references or TopicMasks (optional)
     * @param contentCriteria   content-level Topic references or TopicMasks (optional)
     * @param actionMasks       required action masks (e.g. "ACCESS", "READ", "WRITE") (optional)
     * @param compartmentMasks  required compartment TopicMasks (optional)
     * @param securityLabels    governed security labels (optional)
     * @param confidentiality   FHIR/Harmonia confidentiality rating (optional)
     * @param attributes        additional security requirement attributes (optional)
     */
    @JsonCreator
    public InformationSecurityContext(
            @JsonProperty("unitCriteria") Collection<String> unitCriteria,
            @JsonProperty("containerCriteria") Collection<String> containerCriteria,
            @JsonProperty("contentCriteria") Collection<String> contentCriteria,
            @JsonProperty("actionMasks") Collection<String> actionMasks,
            @JsonProperty("compartmentMasks") Collection<String> compartmentMasks,
            @JsonProperty("securityLabels") Collection<HarmoniaSecurityLabelEnum> securityLabels,
            @JsonProperty("confidentiality") FhirConfidentialityEnum confidentiality,
            @JsonProperty("attributes") Map<String, String> attributes) {
        this.unitCriteria = sanitizeSet(unitCriteria);
        this.containerCriteria = sanitizeSet(containerCriteria);
        this.contentCriteria = sanitizeSet(contentCriteria);
        this.actionMasks = sanitizeSet(actionMasks);
        this.compartmentMasks = sanitizeSet(compartmentMasks);
        this.securityLabels = securityLabels == null || securityLabels.isEmpty()
                ? Set.of()
                : Collections.unmodifiableSet(new LinkedHashSet<>(securityLabels));
        this.confidentiality = confidentiality;
        this.attributes = attributes == null || attributes.isEmpty()
                ? Map.of()
                : Collections.unmodifiableMap(new LinkedHashMap<>(attributes));
    }

    private static Set<String> sanitizeSet(Collection<String> input) {
        if (input == null || input.isEmpty()) {
            return Set.of();
        }
        Set<String> set = new LinkedHashSet<>();
        for (String item : input) {
            if (item != null && !item.isBlank()) {
                set.add(item.trim());
            }
        }
        return Collections.unmodifiableSet(set);
    }

    /**
     * Returns an empty, unconstrained {@code InformationSecurityContext}.
     *
     * @return empty InformationSecurityContext
     */
    public static InformationSecurityContext empty() {
        return EMPTY;
    }

    /**
     * Creates a new builder for constructing {@code InformationSecurityContext} instances.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    // =========================================================================
    // Accessors
    // =========================================================================

    /**
     * Returns unmodifiable set of unit-level Topic references or TopicMasks.
     *
     * @return unmodifiable set of unit criteria
     */
    @JsonProperty("unitCriteria")
    public Set<String> unitCriteria() {
        return unitCriteria;
    }

    /**
     * Returns unmodifiable set of container-level Topic references or TopicMasks.
     *
     * @return unmodifiable set of container criteria
     */
    @JsonProperty("containerCriteria")
    public Set<String> containerCriteria() {
        return containerCriteria;
    }

    /**
     * Returns unmodifiable set of content-level Topic references or TopicMasks.
     *
     * @return unmodifiable set of content criteria
     */
    @JsonProperty("contentCriteria")
    public Set<String> contentCriteria() {
        return contentCriteria;
    }

    /**
     * Returns unmodifiable set of required action masks (e.g. "ACCESS", "READ", "WRITE").
     *
     * @return unmodifiable set of action masks
     */
    @JsonProperty("actionMasks")
    public Set<String> actionMasks() {
        return actionMasks;
    }

    /**
     * Returns unmodifiable set of required compartment TopicMasks.
     *
     * @return unmodifiable set of compartment masks
     */
    @JsonProperty("compartmentMasks")
    public Set<String> compartmentMasks() {
        return compartmentMasks;
    }

    /**
     * Returns unmodifiable set of governed security labels.
     *
     * @return unmodifiable set of security labels
     */
    @JsonProperty("securityLabels")
    public Set<HarmoniaSecurityLabelEnum> securityLabels() {
        return securityLabels;
    }

    /**
     * Returns the confidentiality classification for this Information Unit.
     *
     * @return optional confidentiality rating
     */
    @JsonIgnore
    public Optional<FhirConfidentialityEnum> confidentiality() {
        return Optional.ofNullable(confidentiality);
    }

    /**
     * Returns the raw confidentiality enum, or null if not set.
     *
     * @return confidentiality enum, or null
     */
    @JsonProperty("confidentiality")
    public FhirConfidentialityEnum getConfidentiality() {
        return confidentiality;
    }

    /**
     * Returns unmodifiable map of security requirement attributes.
     *
     * @return unmodifiable map of attributes
     */
    @JsonProperty("attributes")
    public Map<String, String> attributes() {
        return attributes;
    }

    // =========================================================================
    // Immutable Derivations ("With" Mutators)
    // =========================================================================

    public InformationSecurityContext withUnitCriteria(Collection<String> criteria) {
        return new InformationSecurityContext(criteria, this.containerCriteria, this.contentCriteria,
                this.actionMasks, this.compartmentMasks, this.securityLabels, this.confidentiality, this.attributes);
    }

    public InformationSecurityContext withContainerCriteria(Collection<String> criteria) {
        return new InformationSecurityContext(this.unitCriteria, criteria, this.contentCriteria,
                this.actionMasks, this.compartmentMasks, this.securityLabels, this.confidentiality, this.attributes);
    }

    public InformationSecurityContext withContentCriteria(Collection<String> criteria) {
        return new InformationSecurityContext(this.unitCriteria, this.containerCriteria, criteria,
                this.actionMasks, this.compartmentMasks, this.securityLabels, this.confidentiality, this.attributes);
    }

    public InformationSecurityContext withActionMasks(Collection<String> masks) {
        return new InformationSecurityContext(this.unitCriteria, this.containerCriteria, this.contentCriteria,
                masks, this.compartmentMasks, this.securityLabels, this.confidentiality, this.attributes);
    }

    public InformationSecurityContext withCompartmentMasks(Collection<String> masks) {
        return new InformationSecurityContext(this.unitCriteria, this.containerCriteria, this.contentCriteria,
                this.actionMasks, masks, this.securityLabels, this.confidentiality, this.attributes);
    }

    public InformationSecurityContext withSecurityLabels(Collection<HarmoniaSecurityLabelEnum> labels) {
        return new InformationSecurityContext(this.unitCriteria, this.containerCriteria, this.contentCriteria,
                this.actionMasks, this.compartmentMasks, labels, this.confidentiality, this.attributes);
    }

    public InformationSecurityContext withConfidentiality(FhirConfidentialityEnum confidentiality) {
        return new InformationSecurityContext(this.unitCriteria, this.containerCriteria, this.contentCriteria,
                this.actionMasks, this.compartmentMasks, this.securityLabels, confidentiality, this.attributes);
    }

    public InformationSecurityContext withAttributes(Map<String, String> attributes) {
        return new InformationSecurityContext(this.unitCriteria, this.containerCriteria, this.contentCriteria,
                this.actionMasks, this.compartmentMasks, this.securityLabels, this.confidentiality, attributes);
    }

    // =========================================================================
    // Standard Object Methods
    // =========================================================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InformationSecurityContext that = (InformationSecurityContext) o;
        return Objects.equals(unitCriteria, that.unitCriteria)
                && Objects.equals(containerCriteria, that.containerCriteria)
                && Objects.equals(contentCriteria, that.contentCriteria)
                && Objects.equals(actionMasks, that.actionMasks)
                && Objects.equals(compartmentMasks, that.compartmentMasks)
                && Objects.equals(securityLabels, that.securityLabels)
                && confidentiality == that.confidentiality
                && Objects.equals(attributes, that.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(unitCriteria, containerCriteria, contentCriteria, actionMasks, compartmentMasks,
                securityLabels, confidentiality, attributes);
    }

    @Override
    public String toString() {
        return "InformationSecurityContext{" +
                "unitCriteriaCount=" + unitCriteria.size() +
                ", containerCriteriaCount=" + containerCriteria.size() +
                ", contentCriteriaCount=" + contentCriteria.size() +
                ", actionMasksCount=" + actionMasks.size() +
                ", compartmentMasksCount=" + compartmentMasks.size() +
                ", securityLabels=" + securityLabels +
                ", confidentiality=" + confidentiality +
                '}';
    }

    // =========================================================================
    // Builder
    // =========================================================================

    /**
     * Fluent builder for {@link InformationSecurityContext}.
     */
    public static final class Builder {
        private final Set<String> unitCriteria = new LinkedHashSet<>();
        private final Set<String> containerCriteria = new LinkedHashSet<>();
        private final Set<String> contentCriteria = new LinkedHashSet<>();
        private final Set<String> actionMasks = new LinkedHashSet<>();
        private final Set<String> compartmentMasks = new LinkedHashSet<>();
        private final Set<HarmoniaSecurityLabelEnum> securityLabels = new LinkedHashSet<>();
        private FhirConfidentialityEnum confidentiality;
        private final Map<String, String> attributes = new LinkedHashMap<>();

        private Builder() {}

        public Builder addUnitCriterion(String criterion) {
            if (criterion != null && !criterion.isBlank()) this.unitCriteria.add(criterion.trim());
            return this;
        }

        public Builder unitCriteria(Collection<String> criteria) {
            this.unitCriteria.clear();
            if (criteria != null) this.unitCriteria.addAll(criteria);
            return this;
        }

        public Builder addContainerCriterion(String criterion) {
            if (criterion != null && !criterion.isBlank()) this.containerCriteria.add(criterion.trim());
            return this;
        }

        public Builder containerCriteria(Collection<String> criteria) {
            this.containerCriteria.clear();
            if (criteria != null) this.containerCriteria.addAll(criteria);
            return this;
        }

        public Builder addContentCriterion(String criterion) {
            if (criterion != null && !criterion.isBlank()) this.contentCriteria.add(criterion.trim());
            return this;
        }

        public Builder contentCriteria(Collection<String> criteria) {
            this.contentCriteria.clear();
            if (criteria != null) this.contentCriteria.addAll(criteria);
            return this;
        }

        public Builder addActionMask(String actionMask) {
            if (actionMask != null && !actionMask.isBlank()) this.actionMasks.add(actionMask.trim());
            return this;
        }

        public Builder actionMasks(Collection<String> actionMasks) {
            this.actionMasks.clear();
            if (actionMasks != null) this.actionMasks.addAll(actionMasks);
            return this;
        }

        public Builder addCompartmentMask(String compartmentMask) {
            if (compartmentMask != null && !compartmentMask.isBlank()) this.compartmentMasks.add(compartmentMask.trim());
            return this;
        }

        public Builder compartmentMasks(Collection<String> compartmentMasks) {
            this.compartmentMasks.clear();
            if (compartmentMasks != null) this.compartmentMasks.addAll(compartmentMasks);
            return this;
        }

        public Builder addSecurityLabel(HarmoniaSecurityLabelEnum label) {
            if (label != null) this.securityLabels.add(label);
            return this;
        }

        public Builder securityLabels(Collection<HarmoniaSecurityLabelEnum> labels) {
            this.securityLabels.clear();
            if (labels != null) this.securityLabels.addAll(labels);
            return this;
        }

        public Builder confidentiality(FhirConfidentialityEnum confidentiality) {
            this.confidentiality = confidentiality;
            return this;
        }

        public Builder attribute(String key, String value) {
            Objects.requireNonNull(key, "attribute key must not be null");
            if (value != null) {
                this.attributes.put(key, value);
            } else {
                this.attributes.remove(key);
            }
            return this;
        }

        public Builder attributes(Map<String, String> attributes) {
            this.attributes.clear();
            if (attributes != null) this.attributes.putAll(attributes);
            return this;
        }

        public InformationSecurityContext build() {
            return new InformationSecurityContext(unitCriteria, containerCriteria, contentCriteria,
                    actionMasks, compartmentMasks, securityLabels, confidentiality, attributes);
        }
    }
}
