/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.information.entity.datatypes;

import java.util.Objects;
import java.util.Optional;
import net.fhirfactory.harmonia.model.core.information.entity.valuesets.InformationFailureCategory;
import net.fhirfactory.harmonia.model.core.naming.Topic;

/**
 * Active-state facts that Mneme governs for one cache manifestation of an IU.
 *
 * <p>Draft 002.005 §1.2 makes this a direct constituent of
 * {@link InformationMetadata}, superseding the earlier peer allocation. It
 * holds:</p>
 * <ul>
 *   <li>{@code cacheState} - an optional Topic naming the Mneme cache state;</li>
 *   <li>{@code activeGeneration} - an opaque identifier of the cache
 *       manifestation, supplied by Mneme.</li>
 * </ul>
 * <p>These facts describe active state, which is never authoritative
 * (Invariant 8). The Active Generation is distinct from the Managed Revision
 * in {@link PersistenceLifecycleMetadata}, and Calliope never derives one from
 * the other. Calliope does not interpret either value.</p>
 */
public final class CacheLifecycleMetadata {
    private static final int MAX_GENERATION_LENGTH = 256;
    private static final CacheLifecycleMetadata NOT_CACHED = new CacheLifecycleMetadata(null, null);

    private final Topic cacheState;
    private final String activeGeneration;

    private CacheLifecycleMetadata(Topic cacheState, String activeGeneration) {
        this.cacheState = cacheState;
        this.activeGeneration = activeGeneration;
    }

    /**
     * Returns the initialised value with no cache state and no Active Generation.
     *
     * @return the initialised value with no cache state and no Active Generation
     */
    public static CacheLifecycleMetadata notCached() {
        return NOT_CACHED;
    }

    /**
     * Records the Mneme cache state.
     *
     * @param cacheState cache-state Topic, or null to clear
     * @return a new value
     */
    public CacheLifecycleMetadata withCacheState(Topic cacheState) {
        return new CacheLifecycleMetadata(cacheState, activeGeneration);
    }

    /**
     * Records the opaque Active Generation supplied by Mneme.
     *
     * @param activeGeneration non-blank opaque identifier of at most 256 characters, or null to clear
     * @return a new value
     * @throws InformationModelException {@code INVALID_INPUT} when blank or too long
     */
    public CacheLifecycleMetadata withActiveGeneration(String activeGeneration) {
        if (activeGeneration != null) {
            ModelChecks.requiredText(activeGeneration, "cache.activeGeneration");
            if (activeGeneration.length() > MAX_GENERATION_LENGTH) {
                throw InformationModelException.of(InformationFailureCategory.INVALID_INPUT,
                        "cache.activeGeneration");
            }
        }
        return new CacheLifecycleMetadata(cacheState, activeGeneration);
    }

    /**
     * Returns the cache-state Topic, when recorded.
     *
     * @return the cache-state Topic, when recorded
     */
    public Optional<Topic> getCacheState() {
        return Optional.ofNullable(cacheState);
    }

    /**
     * Returns the Active Generation, when recorded.
     *
     * @return the Active Generation, when recorded
     */
    public Optional<String> getActiveGeneration() {
        return Optional.ofNullable(activeGeneration);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CacheLifecycleMetadata that)) {
            return false;
        }
        return ModelChecks.sameVersionedTopic(cacheState, that.cacheState)
                && Objects.equals(activeGeneration, that.activeGeneration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cacheState, activeGeneration);
    }

    @Override
    public String toString() {
        return "CacheLifecycleMetadata{cacheStatePresent=" + (cacheState != null)
                + ", activeGenerationPresent=" + (activeGeneration != null) + "}";
    }
}
