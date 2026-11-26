/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.information.entity.datatypes;

import java.util.Objects;
import java.util.Optional;
import java.util.OptionalLong;
import net.fhirfactory.harmonia.model.core.information.entity.valuesets.InformationFailureCategory;
import net.fhirfactory.harmonia.model.core.naming.Topic;

/**
 * Durable-lifecycle facts that Mnemosyne governs for one IU revision.
 *
 * <p>Draft 002.005 §1.2 places three facts here, as a direct constituent of
 * {@link InformationMetadata}:</p>
 * <ul>
 *   <li>{@code managedRevision} - the authoritative Managed Revision that
 *       Mnemosyne durably established. It is absent until Mnemosyne
 *       establishes it; callers must never invent it. It is distinct from
 *       Mneme's operational revision, the cache Active Generation, the Data
 *       Version, any schema Topic version and any native payload version;</li>
 *   <li>{@code disposition} - an optional retention/disposition Topic;</li>
 *   <li>{@code legalHold} - whether a legal hold applies.</li>
 * </ul>
 * <p>Calliope only records these facts. It does not decide disposition,
 * enforce retention, or perform the persistence handshake. Structural rules
 * enforced here: a Managed Revision is positive and never regresses within a
 * revision snapshot's history. A local candidate update retains its originating
 * Managed Revision as the expected revision for the next governed write; only
 * Mnemosyne establishes the successor number after persistence succeeds.</p>
 */
public final class PersistenceLifecycleMetadata {
    private static final PersistenceLifecycleMetadata NOT_ESTABLISHED =
            new PersistenceLifecycleMetadata(null, null, false);

    private final Long managedRevision;
    private final Topic disposition;
    private final boolean legalHold;

    private PersistenceLifecycleMetadata(Long managedRevision, Topic disposition, boolean legalHold) {
        this.managedRevision = managedRevision;
        this.disposition = disposition;
        this.legalHold = legalHold;
    }

    /**
     * Returns the initialised value: no Managed Revision, no disposition, no legal hold.
     *
     * @return the initialised value: no Managed Revision, no disposition, no legal hold
     */
    public static PersistenceLifecycleMetadata notEstablished() {
        return NOT_ESTABLISHED;
    }

    /**
     * Records the Managed Revision that Mnemosyne actually established.
     *
     * @param managedRevision positive revision number, not lower than any current value
     * @return a new value
     * @throws InformationModelException {@code INVALID_INPUT} when not positive;
     *         {@code IMMUTABILITY_VIOLATION} when lower than the current Managed Revision
     */
    public PersistenceLifecycleMetadata withManagedRevision(long managedRevision) {
        if (managedRevision <= 0) {
            throw InformationModelException.of(InformationFailureCategory.INVALID_INPUT,
                    "persistence.managedRevision");
        }
        if (this.managedRevision != null && managedRevision < this.managedRevision) {
            throw InformationModelException.of(InformationFailureCategory.IMMUTABILITY_VIOLATION,
                    "persistence.managedRevision");
        }
        return new PersistenceLifecycleMetadata(managedRevision, disposition, legalHold);
    }


    /**
     * Records the disposition Topic.
     *
     * @param disposition disposition Topic, or null to record none
     * @return a new value
     */
    public PersistenceLifecycleMetadata withDisposition(Topic disposition) {
        return new PersistenceLifecycleMetadata(managedRevision, disposition, legalHold);
    }

    /**
     * Records whether a legal hold applies.
     *
     * @param legalHold true when a legal hold applies
     * @return a new value
     */
    public PersistenceLifecycleMetadata withLegalHold(boolean legalHold) {
        return new PersistenceLifecycleMetadata(managedRevision, disposition, legalHold);
    }

    /**
     * Verifies that a proposed replacement does not regress the Managed Revision.
     *
     * @param proposed proposed replacement
     * @throws InformationModelException {@code MISSING_INPUT} when null;
     *         {@code IMMUTABILITY_VIOLATION} when it removes or lowers the Managed Revision
     */
    public void requireNoRegressionBy(PersistenceLifecycleMetadata proposed) {
        ModelChecks.required(proposed, "persistence");
        if (managedRevision != null
                && (proposed.managedRevision == null || proposed.managedRevision < managedRevision)) {
            throw InformationModelException.of(InformationFailureCategory.IMMUTABILITY_VIOLATION,
                    "persistence.managedRevision");
        }
    }

    /**
     * Returns the established Managed Revision, or empty before Mnemosyne establishes it.
     *
     * @return the established Managed Revision, or empty before Mnemosyne establishes it
     */
    public OptionalLong getManagedRevision() {
        return managedRevision == null ? OptionalLong.empty() : OptionalLong.of(managedRevision);
    }

    /**
     * Returns the disposition Topic, when recorded.
     *
     * @return the disposition Topic, when recorded
     */
    public Optional<Topic> getDisposition() {
        return Optional.ofNullable(disposition);
    }

    /**
     * Returns true when a legal hold applies.
     *
     * @return true when a legal hold applies
     */
    public boolean isLegalHold() {
        return legalHold;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof PersistenceLifecycleMetadata that)) {
            return false;
        }
        return Objects.equals(managedRevision, that.managedRevision) && legalHold == that.legalHold
                && ModelChecks.sameVersionedTopic(disposition, that.disposition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(managedRevision, disposition, legalHold);
    }

    @Override
    public String toString() {
        return "PersistenceLifecycleMetadata{managedRevision=" + managedRevision + ", dispositionPresent="
                + (disposition != null) + ", legalHold=" + legalHold + "}";
    }
}
