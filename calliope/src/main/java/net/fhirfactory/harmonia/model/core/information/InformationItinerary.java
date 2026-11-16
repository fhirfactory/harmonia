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
import net.fhirfactory.harmonia.model.core.naming.Topic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

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
 *       are strongly typed {@link Topic} references, preserving full immutable {@link UUID} identity rather than discarding
 *       identity for derived names.</li>
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
    public static Builder builder() {
        return new Builder();
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
    public Optional<UUID> sourceEntryComponentId() {
        return sourceEntryComponent().map(Topic::getId);
    }

    @JsonIgnore
    public Optional<UUID> creationComponentId() {
        return creationComponent().map(Topic::getId);
    }

    @JsonIgnore
    public Optional<UUID> previousComponentId() {
        return previousComponent().map(Topic::getId);
    }

    @JsonIgnore
    public Optional<UUID> nextComponentId() {
        return nextComponent().map(Topic::getId);
    }

    @JsonIgnore
    public Optional<UUID> targetExitComponentId() {
        return targetExitComponent().map(Topic::getId);
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
                "sourceEntry=" + (sourceEntryComponent != null ? sourceEntryComponent.getShortId() : "null") +
                ", creation=" + (creationComponent != null ? creationComponent.getShortId() : "null") +
                ", previous=" + (previousComponent != null ? previousComponent.getShortId() : "null") +
                ", next=" + (nextComponent != null ? nextComponent.getShortId() : "null") +
                ", targetExit=" + (targetExitComponent != null ? targetExitComponent.getShortId() : "null") +
                ", participating=" + participatingComponents.size() +
                '}';
    }

    /**
     * Fluent builder for {@link InformationItinerary}.
     */
    public static final class Builder {

        private Topic sourceEntryComponent;
        private Topic creationComponent;
        private Topic previousComponent;
        private Topic nextComponent;
        private Topic targetExitComponent;
        private final List<Topic> participatingComponents = new ArrayList<>();

        public Builder sourceEntryComponent(Topic sourceEntryComponent) {
            this.sourceEntryComponent = sourceEntryComponent;
            return this;
        }

        public Builder creationComponent(Topic creationComponent) {
            this.creationComponent = creationComponent;
            return this;
        }

        public Builder previousComponent(Topic previousComponent) {
            this.previousComponent = previousComponent;
            return this;
        }

        public Builder nextComponent(Topic nextComponent) {
            this.nextComponent = nextComponent;
            return this;
        }

        public Builder targetExitComponent(Topic targetExitComponent) {
            this.targetExitComponent = targetExitComponent;
            return this;
        }

        public Builder addParticipatingComponent(Topic participatingComponent) {
            if (participatingComponent != null) {
                this.participatingComponents.add(participatingComponent);
            }
            return this;
        }

        public Builder participatingComponents(List<Topic> participatingComponents) {
            this.participatingComponents.clear();
            if (participatingComponents != null) {
                for (Topic t : participatingComponents) {
                    if (t == null) {
                        throw new IllegalArgumentException("participatingComponents must not contain null elements");
                    }
                    this.participatingComponents.add(t);
                }
            }
            return this;
        }

        public InformationItinerary build() {
            return new InformationItinerary(
                    sourceEntryComponent,
                    creationComponent,
                    previousComponent,
                    nextComponent,
                    targetExitComponent,
                    participatingComponents);
        }
    }
}
