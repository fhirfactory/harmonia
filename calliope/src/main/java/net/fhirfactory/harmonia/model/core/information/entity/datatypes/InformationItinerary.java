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
import net.fhirfactory.harmonia.model.core.information.entity.factory.InformationItineraryBuilder;
import net.fhirfactory.harmonia.model.core.naming.Topic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Encapsulates the distinct conceptual information path and component participation concern of an Information Unit.
 * <p>
 * <b>Architectural Context &amp; Authority:</b>
 * In accordance with the accepted 1.1.25 Architecture Continuum ({@code model.core.information.InformationUnit})
 * and {@code DRP-Information-Unit-Framework.Final.002.md} (§3.1):
 * <ul>
 *   <li>{@code InformationItinerary} represents the conceptual information path/participation as a distinct concern from
 *       {@link InformationCoordinates}, {@link InformationSecurityContext}, and {@link LifecycleContext}.</li>
 *   <li><b>Strongly Typed Topic References:</b> Itinerary properties ({@code sourceEntryComponent}, {@code creationComponent},
 *       {@code previousComponent}, {@code nextComponent}, {@code targetExitComponent}, and {@code participatingComponents})
 *       are strongly typed {@link Topic} references, preserving the complete ordered canonical path rather than relying
 *       on ambiguous unqualified names.</li>
 * </ul>
 * <p>
 * <b>Defensive Programming:</b>
 * <ul>
 *   <li>Immutable value object design.</li>
 *   <li>Participant lists are defensively copied and unmodifiable.</li>
 *   <li>Accessors return {@link Optional} for optional components.</li>
 *   <li>Invalid or malformed governed input is rejected rather than silently discarded or mutated.</li>
 * </ul>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class InformationItinerary implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final InformationItinerary EMPTY = new InformationItinerary(null, null, null, null, null, List.of());

    private final Topic sourceEntryComponent;
    private final Topic creationComponent;
    private final Topic previousComponent;
    private final Topic nextComponent;
    private final Topic targetExitComponent;
    private final List<Topic> participatingComponents;

    /**
     * Canonical constructor establishing validated information itinerary components.
     *
     * @param sourceEntryComponent   strongly typed Topic reference describing ingress/entry component (optional)
     * @param creationComponent      strongly typed Topic reference describing creation component (optional)
     * @param previousComponent      strongly typed Topic reference describing previous processing component (optional)
     * @param nextComponent          strongly typed Topic reference describing next target component (optional)
     * @param targetExitComponent    strongly typed Topic reference describing target exit/egress component (optional)
     * @param participatingComponents list of intermediate participating component Topic references (optional)
     */
    @JsonCreator
    public InformationItinerary(
            @JsonProperty("sourceEntryComponent") Topic sourceEntryComponent,
            @JsonProperty("creationComponent") Topic creationComponent,
            @JsonProperty("previousComponent") Topic previousComponent,
            @JsonProperty("nextComponent") Topic nextComponent,
            @JsonProperty("targetExitComponent") Topic targetExitComponent,
            @JsonProperty("participatingComponents") List<Topic> participatingComponents) {
        this.sourceEntryComponent = sourceEntryComponent;
        this.creationComponent = creationComponent;
        this.previousComponent = previousComponent;
        this.nextComponent = nextComponent;
        this.targetExitComponent = targetExitComponent;

        if (participatingComponents == null || participatingComponents.isEmpty()) {
            this.participatingComponents = List.of();
        } else {
            List<Topic> list = new ArrayList<>();
            for (Topic comp : participatingComponents) {
                if (comp == null) {
                    throw new IllegalArgumentException("participatingComponents must not contain null elements");
                }
                list.add(comp);
            }
            this.participatingComponents = Collections.unmodifiableList(list);
        }
    }

    /**
     * Returns an empty, unpopulated {@code InformationItinerary} instance.
     *
     * @return empty InformationItinerary
     */
    public static InformationItinerary empty() {
        return EMPTY;
    }

    /**
     * Creates a new builder for constructing {@code InformationItinerary} instances.
     *
     * @return a new Builder instance
     */
    public static InformationItineraryBuilder builder() {
        return new InformationItineraryBuilder();
    }

    // =========================================================================
    // Core Accessors (Strongly Typed Topic References)
    // =========================================================================

    public Optional<Topic> sourceEntryComponent() {
        return Optional.ofNullable(sourceEntryComponent);
    }

    public Topic getSourceEntryComponent() {
        return sourceEntryComponent;
    }

    public Optional<Topic> creationComponent() {
        return Optional.ofNullable(creationComponent);
    }

    public Topic getCreationComponent() {
        return creationComponent;
    }

    public Optional<Topic> previousComponent() {
        return Optional.ofNullable(previousComponent);
    }

    public Topic getPreviousComponent() {
        return previousComponent;
    }

    public Optional<Topic> nextComponent() {
        return Optional.ofNullable(nextComponent);
    }

    public Topic getNextComponent() {
        return nextComponent;
    }

    public Optional<Topic> targetExitComponent() {
        return Optional.ofNullable(targetExitComponent);
    }

    public Topic getTargetExitComponent() {
        return targetExitComponent;
    }

    public List<Topic> participatingComponents() {
        return participatingComponents;
    }

    public List<Topic> getParticipatingComponents() {
        return participatingComponents;
    }

    // =========================================================================
    // Derived Convenience Accessors
    // =========================================================================

    @JsonIgnore
    public Optional<String> sourceEntryComponentCanonicalName() {
        return sourceEntryComponent().map(Topic::getCanonicalName);
    }

    @JsonIgnore
    public Optional<String> creationComponentCanonicalName() {
        return creationComponent().map(Topic::getCanonicalName);
    }

    @JsonIgnore
    public Optional<String> previousComponentCanonicalName() {
        return previousComponent().map(Topic::getCanonicalName);
    }

    @JsonIgnore
    public Optional<String> nextComponentCanonicalName() {
        return nextComponent().map(Topic::getCanonicalName);
    }

    @JsonIgnore
    public Optional<String> targetExitComponentCanonicalName() {
        return targetExitComponent().map(Topic::getCanonicalName);
    }

    @JsonIgnore
    public Optional<String> sourceEntryComponentName() {
        return sourceEntryComponent().map(Topic::getName);
    }

    @JsonIgnore
    public Optional<String> creationComponentName() {
        return creationComponent().map(Topic::getName);
    }

    @JsonIgnore
    public Optional<String> previousComponentName() {
        return previousComponent().map(Topic::getName);
    }

    @JsonIgnore
    public Optional<String> nextComponentName() {
        return nextComponent().map(Topic::getName);
    }

    @JsonIgnore
    public Optional<String> targetExitComponentName() {
        return targetExitComponent().map(Topic::getName);
    }

    @JsonIgnore
    public boolean isEmpty() {
        return sourceEntryComponent == null
                && creationComponent == null
                && previousComponent == null
                && nextComponent == null
                && targetExitComponent == null
                && participatingComponents.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InformationItinerary that = (InformationItinerary) o;
        return Objects.equals(sourceEntryComponent, that.sourceEntryComponent) &&
                Objects.equals(creationComponent, that.creationComponent) &&
                Objects.equals(previousComponent, that.previousComponent) &&
                Objects.equals(nextComponent, that.nextComponent) &&
                Objects.equals(targetExitComponent, that.targetExitComponent) &&
                Objects.equals(participatingComponents, that.participatingComponents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceEntryComponent, creationComponent, previousComponent, nextComponent, targetExitComponent, participatingComponents);
    }

    @Override
    public String toString() {
        return "InformationItinerary{" +
                "sourceEntry=" + topicSummary(sourceEntryComponent) +
                ", creation=" + topicSummary(creationComponent) +
                ", previous=" + topicSummary(previousComponent) +
                ", next=" + topicSummary(nextComponent) +
                ", targetExit=" + topicSummary(targetExitComponent) +
                ", participating=" + participatingComponents.size() +
                '}';
    }

    private static String topicSummary(Topic topic) {
        return topic != null ? "Topic(depth=" + topic.getDepth() + ")" : "null";
    }

}
