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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.fhirfactory.harmonia.mllpgateway.service;

import ca.uhn.fhir.context.FhirContext;
import net.fhirfactory.harmonia.model.security.FhirConfidentialityEnum;
import net.fhirfactory.harmonia.model.security.FhirSecurityTagManager;
import org.hl7.fhir.r5.model.Provenance;
import org.hl7.fhir.r5.model.Reference;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.exceptions.HotRodClientException;
import org.infinispan.commons.util.CloseableIterator;
import org.infinispan.commons.util.CloseableIteratorCollection;
import org.infinispan.commons.util.CloseableIteratorSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ProvenanceServiceTest {

    private DefaultProvenanceService provenanceService;
    private RemoteCacheManager mockCacheManager;
    private RemoteCache<String, String> mockCache;
    private Map<String, String> remoteStore;

    private static <T> CloseableIterator<T> toCloseableIterator(Iterator<T> iterator) {
        return new CloseableIterator<T>() {
            @Override
            public void close() {}

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public T next() {
                return iterator.next();
            }
        };
    }

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        mockCacheManager = mock(RemoteCacheManager.class);
        mockCache = mock(RemoteCache.class);
        remoteStore = new ConcurrentHashMap<>();

        when(mockCacheManager.isStarted()).thenReturn(true);
        doReturn(mockCache).when(mockCacheManager).getCache(eq(DefaultProvenanceService.PROVENANCE_CACHE_NAME));

        when(mockCache.get(anyString())).thenAnswer(i -> remoteStore.get(i.getArgument(0)));
        when(mockCache.put(anyString(), anyString())).thenAnswer(i -> remoteStore.put(i.getArgument(0), i.getArgument(1)));
        when(mockCache.remove(anyString())).thenAnswer(i -> remoteStore.remove(i.getArgument(0)));
        doAnswer(i -> {
            remoteStore.clear();
            return null;
        }).when(mockCache).clear();
        when(mockCache.size()).thenAnswer(i -> remoteStore.size());
        when(mockCache.isEmpty()).thenAnswer(i -> remoteStore.isEmpty());

        CloseableIteratorCollection<String> mockValues = mock(CloseableIteratorCollection.class);
        when(mockValues.iterator()).thenAnswer(i -> toCloseableIterator(remoteStore.values().iterator()));
        when(mockValues.stream()).thenAnswer(i -> remoteStore.values().stream());
        when(mockValues.isEmpty()).thenAnswer(i -> remoteStore.isEmpty());
        when(mockValues.size()).thenAnswer(i -> remoteStore.size());
        doReturn(mockValues).when(mockCache).values();

        CloseableIteratorSet<String> mockKeys = mock(CloseableIteratorSet.class);
        when(mockKeys.iterator()).thenAnswer(i -> toCloseableIterator(remoteStore.keySet().iterator()));
        when(mockKeys.stream()).thenAnswer(i -> remoteStore.keySet().stream());
        when(mockKeys.isEmpty()).thenAnswer(i -> remoteStore.isEmpty());
        when(mockKeys.size()).thenAnswer(i -> remoteStore.size());
        doReturn(mockKeys).when(mockCache).keySet();

        provenanceService = new DefaultProvenanceService(mockCacheManager, FhirContext.forR5());
    }

    @Test
    @DisplayName("Create, Read, Update, Delete Provenance resource in remote cache")
    void testCrudOperations() {
        Provenance prov = new Provenance();
        prov.setId("Provenance/prov-001");
        prov.addTarget(new Reference("Task/MSG-1001").setDisplay("Task for ADT A01"));
        prov.setRecorded(new Date());

        Provenance.ProvenanceAgentComponent agent = prov.addAgent();
        agent.setWho(new Reference("Device/mllp-gateway").setDisplay("HIE MLLP Gateway"));

        Provenance created = provenanceService.create(prov);
        assertThat(created).isNotNull();
        assertThat(created.getIdPart()).isEqualTo("prov-001");
        assertThat(provenanceService.count()).isEqualTo(1);
        assertThat(FhirSecurityTagManager.hasConfidentiality(created, FhirConfidentialityEnum.N)).isTrue();

        // Verify it was persisted to remote store
        assertThat(remoteStore).containsKey("prov-001");

        Optional<Provenance> fetched = provenanceService.getById("prov-001");
        assertThat(fetched).isPresent();
        assertThat(fetched.get().getTarget()).hasSize(1);
        assertThat(fetched.get().getTargetFirstRep().getReference()).isEqualTo("Task/MSG-1001");
        assertThat(FhirSecurityTagManager.hasConfidentiality(fetched.get(), FhirConfidentialityEnum.N)).isTrue();

        // Update
        Provenance toUpdate = fetched.get();
        Provenance.ProvenanceEntityComponent entity = toUpdate.addEntity();
        entity.setRole(Provenance.ProvenanceEntityRole.SOURCE);
        entity.setWhat(new Reference("Communication/comm-1001"));
        provenanceService.update("prov-001", toUpdate);

        Optional<Provenance> updated = provenanceService.getById("prov-001");
        assertThat(updated).isPresent();
        assertThat(updated.get().getEntity()).hasSize(1);
        assertThat(updated.get().getEntityFirstRep().getWhat().getReference()).isEqualTo("Communication/comm-1001");
        assertThat(FhirSecurityTagManager.hasConfidentiality(updated.get(), FhirConfidentialityEnum.N)).isTrue();

        // Delete
        boolean deleted = provenanceService.delete("prov-001");
        assertThat(deleted).isTrue();
        assertThat(provenanceService.count()).isEqualTo(0);
        assertThat(provenanceService.getById("prov-001")).isEmpty();
        assertThat(remoteStore).doesNotContainKey("prov-001");
    }

    @Test
    @DisplayName("Search Provenance by Target, Agent, and Patient in remote cache")
    void testSearchOperations() {
        Provenance prov1 = new Provenance();
        prov1.setId("Provenance/prov-A01");
        prov1.addTarget(new Reference("Task/TASK-1001"));
        Provenance.ProvenanceAgentComponent agent1 = prov1.addAgent();
        agent1.setWho(new Reference("Practitioner/DOC-1").setDisplay("Dr. House"));
        prov1.setPatient(new Reference("Patient/PAT-100").setDisplay("John Doe"));

        Provenance prov2 = new Provenance();
        prov2.setId("Provenance/prov-A08");
        prov2.addTarget(new Reference("Task/TASK-2002"));
        Provenance.ProvenanceAgentComponent agent2 = prov2.addAgent();
        agent2.setWho(new Reference("Device/gateway-2").setDisplay("Gateway Two"));
        prov2.setPatient(new Reference("Patient/PAT-200").setDisplay("Jane Smith"));

        provenanceService.create(prov1);
        provenanceService.create(prov2);

        // Search by Target
        List<Provenance> byTarget = provenanceService.search(null, "TASK-1001", null, null);
        assertThat(byTarget).hasSize(1);
        assertThat(byTarget.get(0).getIdPart()).isEqualTo("prov-A01");

        // Search by Agent
        List<Provenance> byAgent = provenanceService.search(null, null, "House", null);
        assertThat(byAgent).hasSize(1);
        assertThat(byAgent.get(0).getIdPart()).isEqualTo("prov-A01");

        // Search by Patient
        List<Provenance> byPatient = provenanceService.search(null, null, null, "PAT-200");
        assertThat(byPatient).hasSize(1);
        assertThat(byPatient.get(0).getIdPart()).isEqualTo("prov-A08");
    }

    @Test
    @DisplayName("Valid cache miss returns empty Optional rather than exception")
    void testValidCacheMissReturnsEmpty() {
        Optional<Provenance> result = provenanceService.getById("non-existent-prov");
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Throws IllegalStateException when RemoteCacheManager is null")
    void testExplicitFailureWhenRemoteCacheManagerNull() {
        DefaultProvenanceService unconfiguredService = new DefaultProvenanceService(null, FhirContext.forR5());
        Provenance prov = new Provenance();
        prov.setId("Provenance/prov-null-mgr");

        assertThatThrownBy(() -> unconfiguredService.create(prov))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Mneme cache [provenance-cache] is unavailable");

        assertThatThrownBy(() -> unconfiguredService.getById("prov-null-mgr"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Mneme cache [provenance-cache] is unavailable");

        assertThatThrownBy(() -> unconfiguredService.update("prov-null-mgr", prov))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Mneme cache [provenance-cache] is unavailable");

        assertThatThrownBy(() -> unconfiguredService.delete("prov-null-mgr"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Mneme cache [provenance-cache] is unavailable");

        assertThatThrownBy(unconfiguredService::getAll)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Mneme cache [provenance-cache] is unavailable");

        assertThatThrownBy(() -> unconfiguredService.search(null, null, null, null))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Mneme cache [provenance-cache] is unavailable");

        assertThatThrownBy(unconfiguredService::count)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Mneme cache [provenance-cache] is unavailable");

        assertThatThrownBy(unconfiguredService::clear)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Mneme cache [provenance-cache] is unavailable");
    }

    @Test
    @DisplayName("Throws IllegalStateException when RemoteCacheManager is not started")
    void testExplicitFailureWhenRemoteCacheManagerNotStarted() {
        when(mockCacheManager.isStarted()).thenReturn(false);
        Provenance prov = new Provenance();
        prov.setId("Provenance/prov-unstarted");

        assertThatThrownBy(() -> provenanceService.create(prov))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Mneme cache [provenance-cache] is unavailable");

        assertThatThrownBy(() -> provenanceService.getById("prov-unstarted"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Mneme cache [provenance-cache] is unavailable");
    }

    @Test
    @DisplayName("Throws IllegalStateException when getCache returns null")
    void testExplicitFailureWhenNamedCacheNull() {
        when(mockCacheManager.getCache(eq(DefaultProvenanceService.PROVENANCE_CACHE_NAME))).thenReturn(null);
        Provenance prov = new Provenance();
        prov.setId("Provenance/prov-no-cache");

        assertThatThrownBy(() -> provenanceService.create(prov))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Mneme cache [provenance-cache] is unavailable");
    }

    @Test
    @DisplayName("Propagates HotRodClientException from RemoteCache")
    void testRemoteCacheExceptionPropagation() {
        when(mockCache.put(anyString(), anyString())).thenThrow(new HotRodClientException("HotRod transport error"));
        Provenance prov = new Provenance();
        prov.setId("Provenance/prov-fail");

        assertThatThrownBy(() -> provenanceService.create(prov))
                .isInstanceOf(HotRodClientException.class)
                .hasMessageContaining("HotRod transport error");
    }

    @Test
    @DisplayName("Zero local fallback state mutation when cache fails")
    void testZeroLocalFallbackStateMutation() {
        when(mockCacheManager.isStarted()).thenReturn(false);
        Provenance prov = new Provenance();
        prov.setId("Provenance/prov-no-fallback");

        assertThatThrownBy(() -> provenanceService.create(prov))
                .isInstanceOf(IllegalStateException.class);

        // Reconnect cache
        when(mockCacheManager.isStarted()).thenReturn(true);

        // Cache was never populated with prov-no-fallback
        Optional<Provenance> fetched = provenanceService.getById("prov-no-fallback");
        assertThat(fetched).isEmpty();
        assertThat(provenanceService.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("Service recovery upon cache reconnection")
    void testRecoveryUponReconnect() {
        when(mockCacheManager.isStarted()).thenReturn(false);
        Provenance prov = new Provenance();
        prov.setId("Provenance/prov-reconnect");

        assertThatThrownBy(() -> provenanceService.create(prov))
                .isInstanceOf(IllegalStateException.class);

        // Reconnect
        when(mockCacheManager.isStarted()).thenReturn(true);

        Provenance created = provenanceService.create(prov);
        assertThat(created).isNotNull();
        assertThat(provenanceService.getById("prov-reconnect")).isPresent();
        assertThat(provenanceService.count()).isEqualTo(1);
    }
}
