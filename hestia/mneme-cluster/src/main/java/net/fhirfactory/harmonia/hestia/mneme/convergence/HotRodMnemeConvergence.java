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

package net.fhirfactory.harmonia.hestia.mneme.convergence;

import ca.uhn.fhir.context.FhirContext;
import net.fhirfactory.harmonia.model.governedwrite.ActiveStateConvergencePort;
import net.fhirfactory.harmonia.model.governedwrite.AuthoritativeVersion;
import net.fhirfactory.harmonia.model.governedwrite.ConvergenceStatus;
import net.fhirfactory.harmonia.model.governedwrite.ResourceKey;
import org.infinispan.client.hotrod.MetadataValue;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.function.Function;

/**
 * Production Hot Rod active-state convergence port implementation for Mneme.
 * <p>
 * Executes bounded CAS loop convergence against Infinispan Hot Rod caches with explicit
 * authoritative-version provenance metadata comparison and newer-version protection.
 * <p>
 * Invariants:
 * <ul>
 *   <li>Older authoritative outcomes never overwrite or invalidate newer cached representations.</li>
 *   <li>{@link ConvergenceStatus#CONVERGED} signifies no further cache action is required (i.e.
 *       the cache already reflects this or a newer authoritative version).</li>
 *   <li>If CAS loop retries are exhausted or cluster is unavailable, {@link ConvergenceStatus#DEGRADED}
 *       is returned without evicting the cache entry.</li>
 * </ul>
 */
public class HotRodMnemeConvergence implements ActiveStateConvergencePort {

    private static final Logger log = LoggerFactory.getLogger(HotRodMnemeConvergence.class);
    private static final int DEFAULT_MAX_ATTEMPTS = 3;

    private final Function<String, RemoteCache<String, String>> cacheResolver;
    private final FhirContext fhirContext;
    private final int maxAttempts;

    public HotRodMnemeConvergence(RemoteCacheManager cacheManager) {
        this(cacheManager, FhirContext.forR5());
    }

    public HotRodMnemeConvergence(RemoteCacheManager cacheManager, FhirContext fhirContext) {
        this(
                cacheName -> {
                    if (cacheManager == null || !cacheManager.isStarted()) {
                        return null;
                    }
                    try {
                        return cacheManager.getCache(cacheName);
                    } catch (Exception e) {
                        log.debug("Failed to acquire cache '{}': {}", cacheName, e.getMessage());
                        return null;
                    }
                },
                fhirContext,
                DEFAULT_MAX_ATTEMPTS
        );
    }

    public HotRodMnemeConvergence(Function<String, RemoteCache<String, String>> cacheResolver, FhirContext fhirContext) {
        this(cacheResolver, fhirContext, DEFAULT_MAX_ATTEMPTS);
    }

    public HotRodMnemeConvergence(
            Function<String, RemoteCache<String, String>> cacheResolver,
            FhirContext fhirContext,
            int maxAttempts
    ) {
        this.cacheResolver = Objects.requireNonNull(cacheResolver, "cacheResolver must not be null");
        this.fhirContext = Objects.requireNonNull(fhirContext, "fhirContext must not be null");
        this.maxAttempts = maxAttempts > 0 ? maxAttempts : DEFAULT_MAX_ATTEMPTS;
    }

    /**
     * Resolves the canonical Mneme cache name for a given FHIR resource type.
     *
     * @param resourceType the resource type name
     * @return the resolved cache name
     */
    public static String resolveCacheName(String resourceType) {
        if (resourceType == null || resourceType.isBlank()) {
            return "person-cache";
        }
        String normalized = resourceType.toLowerCase().replace("-", "");
        return switch (normalized) {
            case "person", "patient" -> "person-cache";
            case "relatedperson" -> "relatedperson-cache";
            case "practitioner" -> "practitioner-cache";
            case "practitionerrole" -> "practitionerrole-cache";
            case "organization", "organisation" -> "organization-cache";
            case "location" -> "location-cache";
            case "healthcareservice" -> "healthcareservice-cache";
            case "group" -> "group-cache";
            case "provenance" -> "provenance-cache";
            case "auditevent" -> "auditevent-cache";
            case "consent" -> "consent-cache";
            case "task" -> "task-cache";
            case "communication" -> "communication-cache";
            case "documentreference" -> "documentreference-cache";
            default -> normalized + "-cache";
        };
    }

    @Override
    public <T> ConvergenceStatus converge(ResourceKey key, T committedResource, AuthoritativeVersion committedVersion) {
        if (key == null) {
            throw new IllegalArgumentException("ResourceKey must not be null");
        }
        if (committedResource == null) {
            throw new IllegalArgumentException("committedResource must not be null");
        }
        if (committedVersion == null) {
            throw new IllegalArgumentException("committedVersion must not be null");
        }

        String cacheName = resolveCacheName(key.resourceType());
        RemoteCache<String, String> cache;
        try {
            cache = cacheResolver.apply(cacheName);
        } catch (Exception e) {
            log.warn("Mneme cache resolution failed for '{}': {}", cacheName, e.getMessage());
            return ConvergenceStatus.DEGRADED;
        }

        if (cache == null) {
            log.warn("Mneme cache '{}' is unavailable for convergence of {}", cacheName, key);
            return ConvergenceStatus.DEGRADED;
        }

        String payloadJson;
        try {
            payloadJson = MnemeCachedResource.toPayloadJson(committedResource, committedVersion, fhirContext);
        } catch (Exception e) {
            log.error("Failed to serialize committed resource for convergence of {}: {}", key, e.getMessage());
            return ConvergenceStatus.DEGRADED;
        }

        long targetVer = committedVersion.longValue();

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                MetadataValue<String> currentMeta = cache.getWithMetadata(key.id());
                if (currentMeta != null) {
                    long cachedVer = MnemeCachedResource.extractAuthoritativeVersion(currentMeta.getValue());
                    // Newer-Version Invariant: never overwrite newer or equal cache state.
                    // CONVERGED means no further cache action is required.
                    if (cachedVer >= targetVer) {
                        log.debug("Cache entry for {} already reflects authoritative version {} (target: {}). Convergence satisfied.",
                                key, cachedVer, targetVer);
                        return ConvergenceStatus.CONVERGED;
                    }

                    // Hot Rod opaque CAS token replacement
                    boolean casSuccess = cache.replaceWithVersion(key.id(), payloadJson, currentMeta.getVersion());
                    if (casSuccess) {
                        log.debug("Converged {} in Mneme cache '{}' to authoritative version {} on attempt {}",
                                key, cacheName, targetVer, attempt);
                        return ConvergenceStatus.CONVERGED;
                    }
                    log.debug("CAS replacement lost for {} on attempt {}/{}", key, attempt, maxAttempts);
                } else {
                    // Cold cache / entry absent
                    String existing = cache.putIfAbsent(key.id(), payloadJson);
                    if (existing == null) {
                        log.debug("Seeded cold cache for {} in Mneme cache '{}' with authoritative version {}",
                                key, cacheName, targetVer);
                        return ConvergenceStatus.CONVERGED;
                    }
                    log.debug("putIfAbsent raced for {} on attempt {}/{}", key, attempt, maxAttempts);
                }
            } catch (Exception e) {
                log.warn("Exception during Mneme convergence for {} on attempt {}/{}: {}",
                        key, attempt, maxAttempts, e.getMessage());
                return ConvergenceStatus.DEGRADED;
            }
        }

        log.warn("Mneme convergence for {} degraded after {} CAS attempts. Cache entry not evicted.",
                key, maxAttempts);
        return ConvergenceStatus.DEGRADED;
    }
}
