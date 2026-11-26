/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.information.entity.datatypes;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import net.fhirfactory.harmonia.model.core.naming.Topic;

/**
 * Where an InformationUnit originally came from and when it entered Harmonia.
 *
 * <p>Draft 002.005 §1.3 records three facts, including for information that
 * Harmonia generates internally:</p>
 * <ul>
 *   <li>{@code sourceSystem} - the original source system;</li>
 *   <li>{@code entryComponent} - the first Harmonia component it entered;</li>
 *   <li>{@code entryInstant} - when it entered Harmonia.</li>
 * </ul>
 * <p>Each fact may start unknown and may be established once. After that it
 * is immutable: the {@code establish...} methods accept an idempotent
 * re-statement of the same value (for Topics, the same versioned canonical
 * form) and reject any replacement. Entry into Harmonia, source creation,
 * logical location and derivation are separate facts; routing and itinerary
 * belong to Pragma/Pragmatosis and are not recorded here.</p>
 */
public final class OriginMetadata {
    private static final OriginMetadata UNKNOWN = new OriginMetadata(null, null, null);

    private final Topic sourceSystem;
    private final Topic entryComponent;
    private final Instant entryInstant;

    private OriginMetadata(Topic sourceSystem, Topic entryComponent, Instant entryInstant) {
        this.sourceSystem = sourceSystem;
        this.entryComponent = entryComponent;
        this.entryInstant = entryInstant;
    }

    /**
     * Returns the initialised value with every origin fact still unknown.
     *
     * @return the initialised value with every origin fact still unknown
     */
    public static OriginMetadata unknown() {
        return UNKNOWN;
    }

    /**
     * Establishes the original source system.
     *
     * @param sourceSystem source system Topic
     * @return a new value with the fact established
     * @throws InformationModelException {@code MISSING_INPUT} when null;
     *         {@code IMMUTABILITY_VIOLATION} when a different value is already established
     */
    public OriginMetadata establishSourceSystem(Topic sourceSystem) {
        ModelChecks.required(sourceSystem, "origin.sourceSystem");
        return new OriginMetadata(ModelChecks.establishOnce(this.sourceSystem, sourceSystem,
                ModelChecks::sameVersionedTopic, "origin.sourceSystem"), entryComponent, entryInstant);
    }

    /**
     * Establishes the first Harmonia entry component.
     *
     * @param entryComponent entry component Topic
     * @return a new value with the fact established
     * @throws InformationModelException {@code MISSING_INPUT} when null;
     *         {@code IMMUTABILITY_VIOLATION} when a different value is already established
     */
    public OriginMetadata establishEntryComponent(Topic entryComponent) {
        ModelChecks.required(entryComponent, "origin.entryComponent");
        return new OriginMetadata(sourceSystem, ModelChecks.establishOnce(this.entryComponent, entryComponent,
                ModelChecks::sameVersionedTopic, "origin.entryComponent"), entryInstant);
    }

    /**
     * Establishes the instant of entry into Harmonia.
     *
     * @param entryInstant entry instant
     * @return a new value with the fact established
     * @throws InformationModelException {@code MISSING_INPUT} when null;
     *         {@code IMMUTABILITY_VIOLATION} when a different instant is already established
     */
    public OriginMetadata establishEntryInstant(Instant entryInstant) {
        ModelChecks.required(entryInstant, "origin.entryInstant");
        return new OriginMetadata(sourceSystem, entryComponent, ModelChecks.establishOnce(this.entryInstant,
                entryInstant, Instant::equals, "origin.entryInstant"));
    }

    /**
     * Verifies that a proposed replacement keeps every established fact unchanged.
     *
     * <p>Unknown facts may become known in the replacement; known facts may
     * not be removed or changed.</p>
     *
     * @param proposed proposed replacement
     * @throws InformationModelException {@code MISSING_INPUT} when null;
     *         {@code IMMUTABILITY_VIOLATION} when an established fact differs
     */
    public void requirePreservedBy(OriginMetadata proposed) {
        ModelChecks.required(proposed, "origin");
        ModelChecks.establishOnce(sourceSystem, proposed.sourceSystem, ModelChecks::sameVersionedTopic,
                "origin.sourceSystem");
        ModelChecks.establishOnce(entryComponent, proposed.entryComponent, ModelChecks::sameVersionedTopic,
                "origin.entryComponent");
        ModelChecks.establishOnce(entryInstant, proposed.entryInstant, Instant::equals, "origin.entryInstant");
    }

    /**
     * Returns the source system, when established.
     *
     * @return the source system, when established
     */
    public Optional<Topic> getSourceSystem() {
        return Optional.ofNullable(sourceSystem);
    }

    /**
     * Returns the first Harmonia entry component, when established.
     *
     * @return the first Harmonia entry component, when established
     */
    public Optional<Topic> getEntryComponent() {
        return Optional.ofNullable(entryComponent);
    }

    /**
     * Returns the Harmonia entry instant, when established.
     *
     * @return the Harmonia entry instant, when established
     */
    public Optional<Instant> getEntryInstant() {
        return Optional.ofNullable(entryInstant);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof OriginMetadata that)) {
            return false;
        }
        return ModelChecks.sameVersionedTopic(sourceSystem, that.sourceSystem)
                && ModelChecks.sameVersionedTopic(entryComponent, that.entryComponent)
                && Objects.equals(entryInstant, that.entryInstant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceSystem, entryComponent, entryInstant);
    }

    /** Reports which facts are established, not their values. */
    @Override
    public String toString() {
        return "OriginMetadata{sourceSystem=" + (sourceSystem != null) + ", entryComponent="
                + (entryComponent != null) + ", entryInstant=" + (entryInstant != null) + "}";
    }
}
