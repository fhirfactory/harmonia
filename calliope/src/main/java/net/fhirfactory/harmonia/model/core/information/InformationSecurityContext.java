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
import net.fhirfactory.harmonia.model.core.naming.Topic;
import net.fhirfactory.harmonia.model.core.naming.TopicMask;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
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
 * In accordance with {@code DRP-Information-Unit-Framework.Final.001.md} (§§5.3, 13; IU-05) and
 * {@code DRP-Information-Unit-Framework.Final.002.md} (§3):
 * <ul>
 *   <li>{@code InformationSecurityContext} is the IU-side security requirements specification.</li>
 *   <li>It expresses the criteria, compartments, and action masks required to access or mutate this IU,
 *       expressed using <b>strongly typed {@link Topic} references or {@link TopicMask} instances</b>.</li>
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
 *   <li>Null collections are normalized to empty unmodifiable sets/maps.</li>
 *   <li>Immutable value object design guarantees thread-safety.</li>
 * </ul>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class InformationSecurityContext implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final InformationSecurityContext EMPTY = new InformationSecurityContext(
            Set.of(), Set.of(), Set.of(), Set.of(), Set.of(), Set.of(), null, Map.of());

    private final Set<Topic> unitCriteria;
    private final Set<Topic> containerCriteria;
    private final Set<Topic> contentCriteria;
    private final Set<String> actionMasks;
    private final Set<TopicMask> actionTopicMasks;
    private final Set<TopicMask> compartmentMasks;
    private final Set<HarmoniaSecurityLabelEnum> securityLabels;
    private final FhirConfidentialityEnum confidentiality;
    private final Map<String, String> attributes;

    /**
     * Canonical constructor establishing validated security requirement criteria.
     *
     * @param unitCriteria      unit-level Topic criteria (optional)
     * @param containerCriteria container-level Topic criteria (optional)
     * @param contentCriteria   content-level Topic criteria (optional)
     * @param actionMasks       required action names/masks (optional)
     * @param compartmentMasks  required compartment TopicMasks (optional)
     * @param securityLabels    governed security labels (optional)
     * @param confidentiality   FHIR/Harmonia confidentiality rating (optional)
     * @param attributes        additional security requirement attributes (optional)
     */
    @JsonCreator
    public InformationSecurityContext(
            @JsonProperty("unitCriteria") Collection<Topic> unitCriteria,
            @JsonProperty("containerCriteria") Collection<Topic> containerCriteria,
            @JsonProperty("contentCriteria") Collection<Topic> contentCriteria,
            @JsonProperty("actionMasks") Collection<?> actionMasks,
            @JsonProperty("compartmentMasks") Collection<TopicMask> compartmentMasks,
            @JsonProperty("securityLabels") Collection<HarmoniaSecurityLabelEnum> securityLabels,
            @JsonProperty("confidentiality") FhirConfidentialityEnum confidentiality,
            @JsonProperty("attributes") Map<String, String> attributes) {
        this.unitCriteria = sanitizeTopicSet(unitCriteria);
        this.containerCriteria = sanitizeTopicSet(containerCriteria);
        this.contentCriteria = sanitizeTopicSet(contentCriteria);

        Set<String> actions = new LinkedHashSet<>();
        Set<TopicMask> masks = new LinkedHashSet<>();
        if (actionMasks != null) {
            for (Object item : actionMasks) {
                if (item instanceof TopicMask tm) {
                    masks.add(tm);
                    String pattern = tm.getMaskPattern();
                    if (pattern != null && pattern.startsWith("Harmonia.Security.Action.")) {
                        actions.add(pattern.substring("Harmonia.Security.Action.".length()));
                    } else if (pattern != null && !pattern.isBlank()) {
                        actions.add(pattern);
                    } else {
                        actions.add(tm.getId().toString());
                    }
                } else if (item instanceof String s && !s.isBlank()) {
                    String trimmed = s.trim();
                    actions.add(trimmed);
                    masks.add(TopicMask.nameBased(Topic.fromName("Harmonia.Security.Action." + trimmed)));
                }
            }
        }
        this.actionMasks = Collections.unmodifiableSet(actions);
        this.actionTopicMasks = Collections.unmodifiableSet(masks);

        this.compartmentMasks = sanitizeMaskSet(compartmentMasks);
        this.securityLabels = securityLabels == null || securityLabels.isEmpty()
                ? Set.of()
                : Collections.unmodifiableSet(new LinkedHashSet<>(securityLabels));
        this.confidentiality = confidentiality;
        this.attributes = attributes == null || attributes.isEmpty()
                ? Map.of()
                : Collections.unmodifiableMap(new LinkedHashMap<>(attributes));
    }

    private static Set<Topic> sanitizeTopicSet(Collection<Topic> input) {
        if (input == null || input.isEmpty()) {
            return Set.of();
        }
        Set<Topic> result = new LinkedHashSet<>();
        for (Topic item : input) {
            if (item != null) {
                result.add(item);
            }
        }
        return Collections.unmodifiableSet(result);
    }

    private static Set<TopicMask> sanitizeMaskSet(Collection<TopicMask> input) {
        if (input == null || input.isEmpty()) {
            return Set.of();
        }
        Set<TopicMask> result = new LinkedHashSet<>();
        for (TopicMask item : input) {
            if (item != null) {
                result.add(item);
            }
        }
        return Collections.unmodifiableSet(result);
    }

    /**
     * Returns an empty, default {@code InformationSecurityContext} instance.
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
    // Core Accessors
    // =========================================================================

    public Set<Topic> unitCriteria() {
        return unitCriteria;
    }

    public Set<Topic> getUnitCriteria() {
        return unitCriteria;
    }

    public Set<Topic> containerCriteria() {
        return containerCriteria;
    }

    public Set<Topic> getContainerCriteria() {
        return containerCriteria;
    }

    public Set<Topic> contentCriteria() {
        return contentCriteria;
    }

    public Set<Topic> getContentCriteria() {
        return contentCriteria;
    }

    @JsonProperty("actionMasks")
    public Set<String> actionMasks() {
        return actionMasks;
    }

    public Set<String> getActionMasks() {
        return actionMasks;
    }

    @JsonIgnore
    public Set<TopicMask> actionTopicMasks() {
        return actionTopicMasks;
    }

    public Set<TopicMask> compartmentMasks() {
        return compartmentMasks;
    }

    public Set<TopicMask> getCompartmentMasks() {
        return compartmentMasks;
    }

    public Set<HarmoniaSecurityLabelEnum> securityLabels() {
        return securityLabels;
    }

    public Set<HarmoniaSecurityLabelEnum> getSecurityLabels() {
        return securityLabels;
    }

    public Optional<FhirConfidentialityEnum> confidentiality() {
        return Optional.ofNullable(confidentiality);
    }

    public FhirConfidentialityEnum getConfidentiality() {
        return confidentiality;
    }

    public Map<String, String> attributes() {
        return attributes;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public Optional<String> attribute(String key) {
        return Optional.ofNullable(attributes.get(key));
    }

    public boolean isEmpty() {
        return unitCriteria.isEmpty()
                && containerCriteria.isEmpty()
                && contentCriteria.isEmpty()
                && actionMasks.isEmpty()
                && compartmentMasks.isEmpty()
                && securityLabels.isEmpty()
                && confidentiality == null
                && attributes.isEmpty();
    }

    // =========================================================================
    // Standard Object Methods
    // =========================================================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InformationSecurityContext that = (InformationSecurityContext) o;
        return Objects.equals(unitCriteria, that.unitCriteria) &&
                Objects.equals(containerCriteria, that.containerCriteria) &&
                Objects.equals(contentCriteria, that.contentCriteria) &&
                Objects.equals(actionMasks, that.actionMasks) &&
                Objects.equals(compartmentMasks, that.compartmentMasks) &&
                Objects.equals(securityLabels, that.securityLabels) &&
                confidentiality == that.confidentiality &&
                Objects.equals(attributes, that.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(unitCriteria, containerCriteria, contentCriteria, actionMasks, compartmentMasks, securityLabels, confidentiality, attributes);
    }

    @Override
    public String toString() {
        return "InformationSecurityContext{" +
                "unitCriteria=" + unitCriteria.size() +
                ", containerCriteria=" + containerCriteria.size() +
                ", contentCriteria=" + contentCriteria.size() +
                ", actionMasks=" + actionMasks +
                ", compartmentMasks=" + compartmentMasks.size() +
                ", labels=" + securityLabels +
                ", confidentiality=" + confidentiality +
                '}';
    }

    // =========================================================================
    // Builder
    // =========================================================================

    public static final class Builder {

        private final Set<Topic> unitCriteria = new LinkedHashSet<>();
        private final Set<Topic> containerCriteria = new LinkedHashSet<>();
        private final Set<Topic> contentCriteria = new LinkedHashSet<>();
        private final Set<Object> actionMasks = new LinkedHashSet<>();
        private final Set<TopicMask> compartmentMasks = new LinkedHashSet<>();
        private final Set<HarmoniaSecurityLabelEnum> securityLabels = new LinkedHashSet<>();
        private FhirConfidentialityEnum confidentiality;
        private final Map<String, String> attributes = new LinkedHashMap<>();

        public Builder addUnitCriterion(Topic criterion) {
            if (criterion != null) {
                this.unitCriteria.add(criterion);
            }
            return this;
        }

        public Builder unitCriteria(Collection<Topic> criteria) {
            this.unitCriteria.clear();
            if (criteria != null) {
                this.unitCriteria.addAll(criteria);
            }
            return this;
        }

        public Builder addContainerCriterion(Topic criterion) {
            if (criterion != null) {
                this.containerCriteria.add(criterion);
            }
            return this;
        }

        public Builder containerCriteria(Collection<Topic> criteria) {
            this.containerCriteria.clear();
            if (criteria != null) {
                this.containerCriteria.addAll(criteria);
            }
            return this;
        }

        public Builder addContentCriterion(Topic criterion) {
            if (criterion != null) {
                this.contentCriteria.add(criterion);
            }
            return this;
        }

        public Builder contentCriteria(Collection<Topic> criteria) {
            this.contentCriteria.clear();
            if (criteria != null) {
                this.contentCriteria.addAll(criteria);
            }
            return this;
        }

        public Builder addActionMask(TopicMask actionMask) {
            if (actionMask != null) {
                this.actionMasks.add(actionMask);
            }
            return this;
        }

        public Builder addActionMask(String actionName) {
            if (actionName != null && !actionName.isBlank()) {
                this.actionMasks.add(actionName.trim());
            }
            return this;
        }

        public Builder actionMasks(Collection<?> actionMasks) {
            this.actionMasks.clear();
            if (actionMasks != null) {
                this.actionMasks.addAll(actionMasks);
            }
            return this;
        }

        public Builder addCompartmentMask(TopicMask compartmentMask) {
            if (compartmentMask != null) {
                this.compartmentMasks.add(compartmentMask);
            }
            return this;
        }

        public Builder compartmentMasks(Collection<TopicMask> compartmentMasks) {
            this.compartmentMasks.clear();
            if (compartmentMasks != null) {
                this.compartmentMasks.addAll(compartmentMasks);
            }
            return this;
        }

        public Builder addSecurityLabel(HarmoniaSecurityLabelEnum label) {
            if (label != null) {
                this.securityLabels.add(label);
            }
            return this;
        }

        public Builder securityLabels(Collection<HarmoniaSecurityLabelEnum> labels) {
            this.securityLabels.clear();
            if (labels != null) {
                this.securityLabels.addAll(labels);
            }
            return this;
        }

        public Builder confidentiality(FhirConfidentialityEnum confidentiality) {
            this.confidentiality = confidentiality;
            return this;
        }

        public Builder attributes(Map<String, String> attributes) {
            this.attributes.clear();
            if (attributes != null) {
                this.attributes.putAll(attributes);
            }
            return this;
        }

        public Builder putAttribute(String key, String value) {
            return attribute(key, value);
        }

        public Builder attribute(String key, String value) {
            if (key != null) {
                if (value == null) {
                    this.attributes.remove(key);
                } else {
                    this.attributes.put(key, value);
                }
            }
            return this;
        }

        public InformationSecurityContext build() {
            return new InformationSecurityContext(
                    unitCriteria,
                    containerCriteria,
                    contentCriteria,
                    actionMasks,
                    compartmentMasks,
                    securityLabels,
                    confidentiality,
                    attributes);
        }
    }
}
