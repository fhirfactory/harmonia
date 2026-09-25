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

package net.fhirfactory.harmonia.praxis.service;

import net.fhirfactory.harmonia.model.praxis.PraxisDefinition;
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

class PraxisServiceTest {

    private PraxisService sequenceService;
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
        doReturn(mockCache).when(mockCacheManager).getCache(eq(PraxisService.SEQUENCE_CACHE_NAME));

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
        when(mockCache.values()).thenReturn(mockValues);

        CloseableIteratorSet<String> mockKeySet = mock(CloseableIteratorSet.class);
        when(mockKeySet.iterator()).thenAnswer(i -> toCloseableIterator(remoteStore.keySet().iterator()));
        when(mockKeySet.stream()).thenAnswer(i -> remoteStore.keySet().stream());
        when(mockKeySet.isEmpty()).thenAnswer(i -> remoteStore.isEmpty());
        when(mockKeySet.size()).thenAnswer(i -> remoteStore.size());
        when(mockCache.keySet()).thenReturn(mockKeySet);

        sequenceService = new PraxisService(mockCacheManager);
        sequenceService.init();
        sequenceService.clear();
    }

    @Test
    @DisplayName("Should create, retrieve, update, and delete TaskSequence in cache")
    void testCrudOperations() {
        PraxisDefinition seq = new PraxisDefinition("seq-admission", "Admission Workflow");
        seq.setSequenceDescription("Processes patient admissions from PAS gateway");
        seq.setVersion("1.1.0");
        seq.setTargetGatewayInstances(List.of("pas-gw"));
        seq.setTargetTriggerTypes(List.of("A01", "A04"));
        seq.setActivityIds(List.of("act-validate", "act-enrich"));

        // Save
        PraxisDefinition saved = sequenceService.save(seq);
        assertThat(saved).isNotNull();
        assertThat(sequenceService.count()).isEqualTo(1);

        // Get by ID
        Optional<PraxisDefinition> retrieved = sequenceService.getById("seq-admission");
        assertThat(retrieved).isPresent();
        assertThat(retrieved.get().getPraxisName()).isEqualTo("Admission Workflow");
        assertThat(retrieved.get().getVersion()).isEqualTo("1.1.0");
        assertThat(retrieved.get().getTargetGatewayInstances()).containsExactly("pas-gw");
        assertThat(retrieved.get().getTargetTriggerTypes()).containsExactly("A01", "A04");
        assertThat(retrieved.get().getActivityIds()).containsEntry(0, "act-validate").containsEntry(1, "act-enrich");

        // Update
        seq.setSequenceDescription("Updated description");
        seq.setTargetTriggerTypes(List.of("A01", "A04", "A08"));
        sequenceService.update(seq);

        Optional<PraxisDefinition> updated = sequenceService.getById("seq-admission");
        assertThat(updated).isPresent();
        assertThat(updated.get().getSequenceDescription()).isEqualTo("Updated description");
        assertThat(updated.get().getTargetTriggerTypes()).containsExactly("A01", "A04", "A08");

        // Delete
        boolean deleted = sequenceService.delete("seq-admission");
        assertThat(deleted).isTrue();
        assertThat(sequenceService.getById("seq-admission")).isEmpty();
        assertThat(sequenceService.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should serialize to and from JSON preserving all filter criteria and activity references")
    void testJsonSerializationRoundTrip() {
        PraxisDefinition original = new PraxisDefinition("seq-discharge", "Discharge Workflow");
        original.setSequenceDescription("Handles patient discharges");
        original.setVersion("2.0.0");
        original.setSourceQueueName("task.event.queue.pas-gw");
        original.setTargetGatewayInstances(List.of("pas-gw", "emr-gw"));
        original.setTargetTriggerTypes(List.of("A03", "A13"));
        original.setActivityIds(List.of("act-discharge-check", "act-notify"));
        original.setActivityClassNames(List.of("com.example.DischargeCheckActivity", "com.example.NotifyActivity"));

        String json = sequenceService.toJson(original);
        assertThat(json).contains("seq-discharge");
        assertThat(json).contains("Discharge Workflow");
        assertThat(json).contains("task.event.queue.pas-gw");
        assertThat(json).contains("act-discharge-check");

        PraxisDefinition deserialized = sequenceService.fromJson(json);
        assertThat(deserialized).isNotNull();
        assertThat(deserialized.getPraxisId()).isEqualTo("seq-discharge");
        assertThat(deserialized.getPraxisName()).isEqualTo("Discharge Workflow");
        assertThat(deserialized.getSourceQueueName()).isEqualTo("task.event.queue.pas-gw");
        assertThat(deserialized.getTargetGatewayInstances()).containsExactly("pas-gw", "emr-gw");
        assertThat(deserialized.getTargetTriggerTypes()).containsExactly("A03", "A13");
        assertThat(deserialized.getActivityIds()).containsEntry(0, "act-discharge-check").containsEntry(1, "act-notify");
        assertThat(deserialized.getActivityClassNames()).containsEntry(0, "com.example.DischargeCheckActivity")
                .containsEntry(1, "com.example.NotifyActivity");
    }

    @Test
    @DisplayName("Should filter matching sequences by gateway instance and trigger type")
    void testFindMatchingSequences() {
        PraxisDefinition seq1 = new PraxisDefinition("seq-pas-admit", "PAS Admit");
        seq1.setTargetGatewayInstances(List.of("pas-gw"));
        seq1.setTargetTriggerTypes(List.of("A01", "A04"));

        PraxisDefinition seq2 = new PraxisDefinition("seq-pas-discharge", "PAS Discharge");
        seq2.setTargetGatewayInstances(List.of("pas-gw"));
        seq2.setTargetTriggerTypes(List.of("A03"));

        PraxisDefinition seq3 = new PraxisDefinition("seq-lab-result", "Lab Results");
        seq3.setTargetGatewayInstances(List.of("lims-gw"));
        seq3.setTargetTriggerTypes(List.of("R01", "ORU^R01"));

        PraxisDefinition seq4 = new PraxisDefinition("seq-audit-all", "Audit All");
        seq4.setMatchAllGateways(true);
        seq4.setMatchAllTriggers(true);

        sequenceService.save(seq1);
        sequenceService.save(seq2);
        sequenceService.save(seq3);
        sequenceService.save(seq4);

        // Matching PAS A01 -> seq1 and seq4
        List<PraxisDefinition> matchesPasA01 = sequenceService.findMatching("pas-gw", "A01");
        assertThat(matchesPasA01).extracting(PraxisDefinition::getPraxisId)
                .containsExactlyInAnyOrder("seq-pas-admit", "seq-audit-all");

        // Matching PAS A03 -> seq2 and seq4
        List<PraxisDefinition> matchesPasA03 = sequenceService.findMatching("pas-gw", "A03");
        assertThat(matchesPasA03).extracting(PraxisDefinition::getPraxisId)
                .containsExactlyInAnyOrder("seq-pas-discharge", "seq-audit-all");

        // Matching LIMS R01 -> seq3 and seq4
        List<PraxisDefinition> matchesLimsR01 = sequenceService.findMatching("lims-gw", "ORU^R01");
        assertThat(matchesLimsR01).extracting(PraxisDefinition::getPraxisId)
                .containsExactlyInAnyOrder("seq-lab-result", "seq-audit-all");

        // Find by Gateway
        List<PraxisDefinition> pasGateways = sequenceService.findByGateway("pas-gw");
        assertThat(pasGateways).extracting(PraxisDefinition::getPraxisId)
                .contains("seq-pas-admit", "seq-pas-discharge", "seq-audit-all");
    }

    @Test
    @DisplayName("Should handle null / invalid arguments safely")
    void testNullArguments() {
        assertThatThrownBy(() -> sequenceService.save(null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(sequenceService.getById(null)).isEmpty();
        assertThat(sequenceService.getById("")).isEmpty();
        assertThat(sequenceService.delete(null)).isFalse();
        assertThat(sequenceService.toJson(null)).isNull();
        assertThat(sequenceService.fromJson(null)).isNull();
        assertThat(sequenceService.fromJson("")).isNull();
    }

    @Test
    @DisplayName("Explicit failure when RemoteCacheManager is null")
    void testExplicitFailureWhenCacheManagerNull() {
        PraxisService unconfigured = new PraxisService();
        PraxisDefinition def = new PraxisDefinition("seq-test", "Test Sequence");

        assertThatThrownBy(() -> unconfigured.save(def))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("tasksequence-cache");

        assertThatThrownBy(() -> unconfigured.getById("seq-test"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("tasksequence-cache");

        assertThatThrownBy(unconfigured::getAll)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("tasksequence-cache");

        assertThatThrownBy(() -> unconfigured.delete("seq-test"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("tasksequence-cache");

        assertThatThrownBy(unconfigured::count)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("tasksequence-cache");

        assertThatThrownBy(unconfigured::clear)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("tasksequence-cache");
    }

    @Test
    @DisplayName("Explicit failure when RemoteCacheManager is not started")
    void testExplicitFailureWhenCacheManagerNotStarted() {
        when(mockCacheManager.isStarted()).thenReturn(false);
        PraxisDefinition def = new PraxisDefinition("seq-test", "Test Sequence");

        assertThatThrownBy(() -> sequenceService.save(def))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("tasksequence-cache");
    }

    @Test
    @DisplayName("Remote cache transport exception propagation")
    void testRemoteCacheExceptionPropagation() {
        when(mockCache.put(anyString(), anyString())).thenThrow(new HotRodClientException("Connection timed out"));
        PraxisDefinition def = new PraxisDefinition("seq-test", "Test Sequence");

        assertThatThrownBy(() -> sequenceService.save(def))
                .isInstanceOf(HotRodClientException.class)
                .hasMessageContaining("Connection timed out");
    }

    @Test
    @DisplayName("Zero local fallback state mutation when cache fails")
    void testZeroLocalFallbackStateMutation() {
        when(mockCacheManager.isStarted()).thenReturn(false);
        PraxisDefinition def = new PraxisDefinition("seq-fail", "Fail Sequence");

        assertThatThrownBy(() -> sequenceService.save(def))
                .isInstanceOf(IllegalStateException.class);

        // Reconnect cache
        when(mockCacheManager.isStarted()).thenReturn(true);

        assertThat(sequenceService.getById("seq-fail")).isEmpty();
        assertThat(sequenceService.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("Service recovery upon cache reconnection")
    void testRecoveryUponReconnect() {
        when(mockCacheManager.isStarted()).thenReturn(false);
        PraxisDefinition def = new PraxisDefinition("seq-reconnect", "Reconnect Sequence");

        assertThatThrownBy(() -> sequenceService.save(def))
                .isInstanceOf(IllegalStateException.class);

        // Reconnect
        when(mockCacheManager.isStarted()).thenReturn(true);

        PraxisDefinition saved = sequenceService.save(def);
        assertThat(saved).isNotNull();
        assertThat(sequenceService.getById("seq-reconnect")).isPresent();
        assertThat(sequenceService.count()).isEqualTo(1);
    }
}
