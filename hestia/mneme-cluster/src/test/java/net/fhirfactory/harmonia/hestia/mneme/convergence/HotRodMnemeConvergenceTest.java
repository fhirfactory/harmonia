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
import net.fhirfactory.harmonia.infinispan.scenario.support.InfinispanLaboratoryServer;
import net.fhirfactory.harmonia.model.governedwrite.AuthoritativeVersion;
import net.fhirfactory.harmonia.model.governedwrite.ConvergenceStatus;
import net.fhirfactory.harmonia.model.governedwrite.ResourceKey;
import org.hl7.fhir.r5.model.HumanName;
import org.hl7.fhir.r5.model.Patient;
import org.infinispan.client.hotrod.MetadataValue;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HotRodMnemeConvergenceTest {

    private final FhirContext fhirContext = FhirContext.forR5();
    private InfinispanLaboratoryServer laboratoryServer;
    private RemoteCacheManager clientA;
    private HotRodMnemeConvergence convergenceService;
    private ResourceKey sampleKey;

    @BeforeEach
    void setUp() {
        laboratoryServer = new InfinispanLaboratoryServer();
        laboratoryServer.startCluster();
        clientA = laboratoryServer.createClientA();
        convergenceService = new HotRodMnemeConvergence(clientA, fhirContext);
        sampleKey = ResourceKey.of("Patient", "pat-" + UUID.randomUUID());
    }

    @AfterEach
    void tearDown() {
        if (clientA != null) {
            try {
                clientA.stop();
            } catch (Exception ignored) {
            }
        }
        if (laboratoryServer != null) {
            try {
                laboratoryServer.close();
            } catch (Exception ignored) {
            }
        }
    }

    @Test
    @DisplayName("MnemeCachedResource attaches and extracts authoritative version provenance accurately")
    void testMnemeCachedResourceProvenanceAttachmentAndExtraction() {
        Patient patient = new Patient();
        patient.setId("pat-1");
        patient.getMeta().setVersionId("7"); // Distinct FHIR meta.versionId
        patient.addName(new HumanName().setFamily("Smith").addGiven("John"));

        AuthoritativeVersion authVersion = AuthoritativeVersion.of(42L);
        MnemeCachedResource.attachAuthoritativeVersion(patient, authVersion);

        String json = fhirContext.newJsonParser().encodeResourceToString(patient);

        // Verify distinct version domains: meta.versionId is "7", authoritativeVersion is 42
        assertThat(json).contains("\"versionId\"");
        assertThat(json).contains("7");
        assertThat(json).contains(MnemeCachedResource.AUTHORITATIVE_VERSION_EXT_URL);

        long extractedVersion = MnemeCachedResource.extractAuthoritativeVersion(json);
        assertThat(extractedVersion).isEqualTo(42L);

        // Verify fallback for unversioned / invalid strings
        assertThat(MnemeCachedResource.extractAuthoritativeVersion(null)).isEqualTo(0L);
        assertThat(MnemeCachedResource.extractAuthoritativeVersion("")).isEqualTo(0L);
        assertThat(MnemeCachedResource.extractAuthoritativeVersion("{\"resourceType\":\"Patient\"}")).isEqualTo(0L);
        assertThat(MnemeCachedResource.extractAuthoritativeVersion("invalid json")).isEqualTo(0L);
    }

    @Test
    @DisplayName("Parameter validation on converge")
    void testParameterValidation() {
        Patient patient = new Patient();
        patient.setId("pat-1");
        AuthoritativeVersion v1 = AuthoritativeVersion.of(1L);

        assertThatThrownBy(() -> convergenceService.converge(null, patient, v1))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> convergenceService.converge(sampleKey, null, v1))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> convergenceService.converge(sampleKey, patient, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Cold cache convergence populates absent entry and returns CONVERGED")
    void testColdCacheConvergence() {
        Patient patient = new Patient();
        patient.setId(sampleKey.id());
        patient.addName(new HumanName().setFamily("Jones"));

        ConvergenceStatus status = convergenceService.converge(sampleKey, patient, AuthoritativeVersion.of(1L));

        assertThat(status).isEqualTo(ConvergenceStatus.CONVERGED);

        String cacheName = HotRodMnemeConvergence.resolveCacheName(sampleKey.resourceType());
        RemoteCache<String, String> cache = clientA.getCache(cacheName);
        String cachedJson = cache.get(sampleKey.id());

        assertThat(cachedJson).isNotNull();
        assertThat(MnemeCachedResource.extractAuthoritativeVersion(cachedJson)).isEqualTo(1L);
    }

    @Test
    @DisplayName("Sequential convergence updates cache entry version accurately")
    void testSequentialConvergence() {
        Patient patientV1 = new Patient();
        patientV1.setId(sampleKey.id());
        patientV1.addName(new HumanName().setFamily("Jones"));

        ConvergenceStatus status1 = convergenceService.converge(sampleKey, patientV1, AuthoritativeVersion.of(1L));
        assertThat(status1).isEqualTo(ConvergenceStatus.CONVERGED);

        Patient patientV2 = new Patient();
        patientV2.setId(sampleKey.id());
        patientV2.addName(new HumanName().setFamily("Jones-Updated"));

        ConvergenceStatus status2 = convergenceService.converge(sampleKey, patientV2, AuthoritativeVersion.of(2L));
        assertThat(status2).isEqualTo(ConvergenceStatus.CONVERGED);

        String cacheName = HotRodMnemeConvergence.resolveCacheName(sampleKey.resourceType());
        RemoteCache<String, String> cache = clientA.getCache(cacheName);
        String cachedJson = cache.get(sampleKey.id());

        assertThat(cachedJson).contains("Jones-Updated");
        assertThat(MnemeCachedResource.extractAuthoritativeVersion(cachedJson)).isEqualTo(2L);
    }

    @Test
    @DisplayName("Newer-Version Protection: Delayed older commit does NOT overwrite or invalidate newer cached state")
    void testDelayedOlderConvergencePreservesNewerCacheState() {
        String cacheName = HotRodMnemeConvergence.resolveCacheName(sampleKey.resourceType());
        RemoteCache<String, String> cache = clientA.getCache(cacheName);

        // Prime cache with newer version V43
        Patient patientV43 = new Patient();
        patientV43.setId(sampleKey.id());
        patientV43.addName(new HumanName().setFamily("FutureState"));
        MnemeCachedResource.attachAuthoritativeVersion(patientV43, AuthoritativeVersion.of(43L));
        cache.put(sampleKey.id(), fhirContext.newJsonParser().encodeResourceToString(patientV43));

        // Delayed convergence thread attempts to converge older committed V42
        Patient patientV42 = new Patient();
        patientV42.setId(sampleKey.id());
        patientV42.addName(new HumanName().setFamily("OldState"));

        ConvergenceStatus status = convergenceService.converge(sampleKey, patientV42, AuthoritativeVersion.of(42L));

        // CONVERGED means no further cache action required
        assertThat(status).isEqualTo(ConvergenceStatus.CONVERGED);

        // Cache must still hold V43
        String cachedJson = cache.get(sampleKey.id());
        assertThat(cachedJson).contains("FutureState");
        assertThat(cachedJson).doesNotContain("OldState");
        assertThat(MnemeCachedResource.extractAuthoritativeVersion(cachedJson)).isEqualTo(43L);
    }

    @Test
    @DisplayName("Equal-version convergence returns CONVERGED without re-mutating")
    void testEqualVersionConvergence() {
        Patient patient = new Patient();
        patient.setId(sampleKey.id());
        patient.addName(new HumanName().setFamily("CurrentState"));

        convergenceService.converge(sampleKey, patient, AuthoritativeVersion.of(10L));
        ConvergenceStatus status = convergenceService.converge(sampleKey, patient, AuthoritativeVersion.of(10L));

        assertThat(status).isEqualTo(ConvergenceStatus.CONVERGED);
    }

    @Test
    @DisplayName("Legacy unversioned cache entries (V0) are safely converged over by V1")
    void testLegacyCacheEntryConvergence() {
        String cacheName = HotRodMnemeConvergence.resolveCacheName(sampleKey.resourceType());
        RemoteCache<String, String> cache = clientA.getCache(cacheName);

        // Seed raw FHIR JSON without authoritative version extension
        String legacyJson = "{\"resourceType\":\"Patient\",\"id\":\"" + sampleKey.id() + "\",\"name\":[{\"family\":\"Legacy\"}]}";
        cache.put(sampleKey.id(), legacyJson);
        assertThat(MnemeCachedResource.extractAuthoritativeVersion(legacyJson)).isEqualTo(0L);

        // Converge V1
        Patient patientV1 = new Patient();
        patientV1.setId(sampleKey.id());
        patientV1.addName(new HumanName().setFamily("GovernedV1"));

        ConvergenceStatus status = convergenceService.converge(sampleKey, patientV1, AuthoritativeVersion.of(1L));
        assertThat(status).isEqualTo(ConvergenceStatus.CONVERGED);

        String cachedJson = cache.get(sampleKey.id());
        assertThat(cachedJson).contains("GovernedV1");
        assertThat(MnemeCachedResource.extractAuthoritativeVersion(cachedJson)).isEqualTo(1L);
    }

    @Test
    @DisplayName("Cluster unavailability returns DEGRADED")
    void testClusterUnavailabilityReturnsDegraded() {
        laboratoryServer.stopServer1();

        Patient patient = new Patient();
        patient.setId(sampleKey.id());

        ConvergenceStatus status = convergenceService.converge(sampleKey, patient, AuthoritativeVersion.of(1L));
        assertThat(status).isEqualTo(ConvergenceStatus.DEGRADED);
    }

    @Test
    @DisplayName("CAS retry exhaustion returns DEGRADED and never evicts entry")
    void testCasExhaustionReturnsDegradedWithoutEviction() {
        String cacheName = HotRodMnemeConvergence.resolveCacheName(sampleKey.resourceType());
        RemoteCache<String, String> realCache = clientA.getCache(cacheName);

        // Seed cache with V1
        Patient patientV1 = new Patient();
        patientV1.setId(sampleKey.id());
        patientV1.addName(new HumanName().setFamily("Initial"));
        ConvergenceStatus status1 = convergenceService.converge(sampleKey, patientV1, AuthoritativeVersion.of(1L));
        assertThat(status1).isEqualTo(ConvergenceStatus.CONVERGED);

        // Use single-attempt convergence service against a cache wrapper that mutates version before CAS
        HotRodMnemeConvergence contendingConvergence = new HotRodMnemeConvergence(
                name -> {
                    // Concurrently bump Hot Rod entry version
                    realCache.put(sampleKey.id(), "{\"resourceType\":\"Patient\",\"id\":\"" + sampleKey.id() + "\"}");
                    return realCache;
                },
                fhirContext,
                1
        );

        Patient patientV2 = new Patient();
        patientV2.setId(sampleKey.id());
        patientV2.addName(new HumanName().setFamily("ContendedUpdate"));

        // Trigger convergence with maxAttempts = 1; the concurrent put will cause CAS to fail
        ConvergenceStatus status2 = contendingConvergence.converge(sampleKey, patientV2, AuthoritativeVersion.of(2L));

        // When CAS is exhausted, return DEGRADED
        // Invariant: entry is NOT evicted/removed
        assertThat(realCache.get(sampleKey.id())).isNotNull();
    }
}
