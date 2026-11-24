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

package net.fhirfactory.harmonia.model.core.information.entity.datatypes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.fhirfactory.harmonia.model.core.information.entity.factory.InformationSecurityContextBuilder;
import net.fhirfactory.harmonia.model.core.naming.Topic;
import net.fhirfactory.harmonia.model.core.naming.TopicMask;
import net.fhirfactory.harmonia.model.core.naming.factory.TopicMaskFactory;
import net.fhirfactory.harmonia.model.core.security.valuesets.FhirConfidentialityEnum;
import net.fhirfactory.harmonia.model.core.security.valuesets.HarmoniaSecurityLabelEnum;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
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
 *   <li>Typed action TopicMasks are serialized as semantic criteria and participate in equality and emptiness checks.
 *       Legacy action display strings reject selector syntax rather than changing the scope of a typed criterion.</li>
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
            Set.of(), Set.of(), Set.of(), Set.of(), Set.of(), Set.of(), Set.of(), null, Map.of());

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
     * @param actionMasks       required action names (optional)
     * @param actionTopicMasks  typed action TopicMasks (optional)
     * @param compartmentMasks  required compartment TopicMasks (optional)
     * @param securityLabels    governed security labels (optional)
     * @param confidentiality   FHIR/Harmonia confidentiality rating (optional)
     * @param attributes        additional security requirement attributes (optional)
     * @throws IllegalArgumentException if a mask collection contains null, or a legacy action
     *         name contains path, escape, or RegEx syntax that could change its selector scope
     */
    @JsonCreator
    public InformationSecurityContext(
            @JsonProperty("unitCriteria") Collection<Topic> unitCriteria,
            @JsonProperty("containerCriteria") Collection<Topic> containerCriteria,
            @JsonProperty("contentCriteria") Collection<Topic> contentCriteria,
            @JsonProperty("actionMasks") Collection<String> actionMasks,
            @JsonProperty("actionTopicMasks") Collection<TopicMask> actionTopicMasks,
            @JsonProperty("compartmentMasks") Collection<TopicMask> compartmentMasks,
            @JsonProperty("securityLabels") Collection<HarmoniaSecurityLabelEnum> securityLabels,
            @JsonProperty("confidentiality") FhirConfidentialityEnum confidentiality,
            @JsonProperty("attributes") Map<String, String> attributes) {
        this.unitCriteria = sanitizeTopicSet(unitCriteria);
        this.containerCriteria = sanitizeTopicSet(containerCriteria);
        this.contentCriteria = sanitizeTopicSet(contentCriteria);

        Set<String> actions = new LinkedHashSet<>();
        Set<TopicMask> masks = new LinkedHashSet<>(sanitizeMaskSet(actionTopicMasks));
        // Derive display names only from the exact inclusive literal path that a legacy
        // name itself produces, so name -> mask -> name is idempotent across JSON round
        // trips. Exact-depth, versioned, or expression masks never yield a name and are
        // carried only as typed criteria, keeping their narrower scope intact.
        for (TopicMask mask : masks) {
            TopicMaskFactory.literalValuePath(mask)
                    .filter(path -> path.size() == 4
                            && path.subList(0, 3).equals(List.of("Harmonia", "Security", "Action")))
                    .map(path -> path.get(3))
                    .filter(InformationSecurityContext::isUnambiguousLegacyAction)
                    .ifPresent(actions::add);
        }
        if (actionMasks != null) {
            for (String action : actionMasks) {
                if (action != null && !action.isBlank()) {
                    String normalized = action.trim();
                    requireUnambiguousLegacyAction(normalized);
                    actions.add(normalized);
                    masks.add(TopicMaskFactory.forLiteralValuePath(
                            List.of("Harmonia", "Security", "Action", normalized)));
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

    /** Copies Topic criteria into an immutable set, retaining the existing null-element normalization. */
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

    /** Copies mask criteria and rejects null elements because they cannot express a valid selector. */
    private static Set<TopicMask> sanitizeMaskSet(Collection<TopicMask> input) {
        if (input == null || input.isEmpty()) {
            return Set.of();
        }
        Set<TopicMask> result = new LinkedHashSet<>();
        for (TopicMask item : input) {
            if (item == null) {
                throw new IllegalArgumentException("Security TopicMask criteria must not contain null");
            }
            result.add(item);
        }
        return Collections.unmodifiableSet(result);
    }

    /** Rejects legacy names that could be interpreted as path, escape, or RegEx syntax. */
    private static void requireUnambiguousLegacyAction(String action) {
        if (!isUnambiguousLegacyAction(action)) {
            throw new IllegalArgumentException("Legacy action name contains selector syntax");
        }
    }

    /** Checks whether a legacy display name can safely represent one literal action value. */
    private static boolean isUnambiguousLegacyAction(String action) {
        if (action == null || action.isEmpty()) {
            return false;
        }
        for (int index = 0; index < action.length(); index++) {
            if (".=\\^$|?*+()[]{}".indexOf(action.charAt(index)) >= 0) {
                return false;
            }
        }
        return true;
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
    public static InformationSecurityContextBuilder builder() {
        return new InformationSecurityContextBuilder();
    }

    // =========================================================================
    // Core Accessors
    // =========================================================================

    /** @return immutable unit-level Topic criteria; never null */
    public Set<Topic> unitCriteria() {
        return unitCriteria;
    }

    /** @return immutable unit-level Topic criteria (bean-style alias) */
    public Set<Topic> getUnitCriteria() {
        return unitCriteria;
    }

    /** @return immutable container-level Topic criteria; never null */
    public Set<Topic> containerCriteria() {
        return containerCriteria;
    }

    /** @return immutable container-level Topic criteria (bean-style alias) */
    public Set<Topic> getContainerCriteria() {
        return containerCriteria;
    }

    /** @return immutable content-level Topic criteria; never null */
    public Set<Topic> contentCriteria() {
        return contentCriteria;
    }

    /** @return immutable content-level Topic criteria (bean-style alias) */
    public Set<Topic> getContentCriteria() {
        return contentCriteria;
    }

    /**
     * Returns legacy action display names. These are a derived convenience view; the
     * authoritative selector semantics are {@link #actionTopicMasks()}.
     *
     * @return immutable legacy action names; never null
     */
    @JsonProperty("actionMasks")
    public Set<String> actionMasks() {
        return actionMasks;
    }

    /** @return immutable legacy action names (bean-style alias) */
    public Set<String> getActionMasks() {
        return actionMasks;
    }

    /** @return immutable typed action selectors, preserved by JSON interchange */
    @JsonProperty("actionTopicMasks")
    public Set<TopicMask> actionTopicMasks() {
        return actionTopicMasks;
    }

    /** @return immutable compartment selectors; never null */
    public Set<TopicMask> compartmentMasks() {
        return compartmentMasks;
    }

    /** @return immutable compartment selectors (bean-style alias) */
    public Set<TopicMask> getCompartmentMasks() {
        return compartmentMasks;
    }

    /** @return immutable governed security labels; never null */
    public Set<HarmoniaSecurityLabelEnum> securityLabels() {
        return securityLabels;
    }

    /** @return immutable governed security labels (bean-style alias) */
    public Set<HarmoniaSecurityLabelEnum> getSecurityLabels() {
        return securityLabels;
    }

    /** @return confidentiality rating, or empty when none is required */
    public Optional<FhirConfidentialityEnum> confidentiality() {
        return Optional.ofNullable(confidentiality);
    }

    /** @return confidentiality rating, or null when none is required */
    public FhirConfidentialityEnum getConfidentiality() {
        return confidentiality;
    }

    /** @return immutable additional requirement attributes; never null */
    public Map<String, String> attributes() {
        return attributes;
    }

    /** @return immutable additional requirement attributes (bean-style alias) */
    public Map<String, String> getAttributes() {
        return attributes;
    }

    /**
     * @param key attribute key
     * @return attribute value, or empty when absent
     */
    public Optional<String> attribute(String key) {
        return Optional.ofNullable(attributes.get(key));
    }

    /**
     * Reports whether no requirement of any kind is expressed. Typed action masks count
     * as requirements even when they have no legacy display name.
     *
     * @return true only when every criterion, label, rating, and attribute is absent
     */
    @JsonIgnore
    public boolean isEmpty() {
        return unitCriteria.isEmpty()
                && containerCriteria.isEmpty()
                && contentCriteria.isEmpty()
                && actionMasks.isEmpty()
                && actionTopicMasks.isEmpty()
                && compartmentMasks.isEmpty()
                && securityLabels.isEmpty()
                && confidentiality == null
                && attributes.isEmpty();
    }

    // =========================================================================
    // Standard Object Methods
    // =========================================================================

    /**
     * Compares all requirement state, including typed action masks, so contexts whose
     * selectors differ only in exactness or version constraints are not equal.
     *
     * @param o comparison value
     * @return true when all requirement state is equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InformationSecurityContext that = (InformationSecurityContext) o;
        return Objects.equals(unitCriteria, that.unitCriteria) &&
                Objects.equals(containerCriteria, that.containerCriteria) &&
                Objects.equals(contentCriteria, that.contentCriteria) &&
                Objects.equals(actionMasks, that.actionMasks) &&
                Objects.equals(actionTopicMasks, that.actionTopicMasks) &&
                Objects.equals(compartmentMasks, that.compartmentMasks) &&
                Objects.equals(securityLabels, that.securityLabels) &&
                confidentiality == that.confidentiality &&
                Objects.equals(attributes, that.attributes);
    }

    /** @return hash consistent with {@link #equals(Object)} */
    @Override
    public int hashCode() {
        return Objects.hash(unitCriteria, containerCriteria, contentCriteria, actionMasks, actionTopicMasks,
                compartmentMasks, securityLabels, confidentiality, attributes);
    }

    /** @return count-only summary that discloses no criteria, attributes, or expressions */
    @Override
    public String toString() {
        return "InformationSecurityContext{" +
                "unitCriteria=" + unitCriteria.size() +
                ", containerCriteria=" + containerCriteria.size() +
                ", contentCriteria=" + contentCriteria.size() +
                ", actionMaskCount=" + actionMasks.size() +
                ", actionTopicMaskCount=" + actionTopicMasks.size() +
                ", compartmentMasks=" + compartmentMasks.size() +
                ", labelCount=" + securityLabels.size() +
                ", hasConfidentiality=" + (confidentiality != null) +
                '}';
    }

}
