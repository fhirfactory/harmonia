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

package net.fhirfactory.harmonia.befe.service;

import net.fhirfactory.harmonia.model.praxis.PraxisDefinition;
import net.fhirfactory.harmonia.model.topic.TopicSubscription;
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
import static org.mockito.Mockito.*;

class TaskSequenceCacheServiceTest {

    private TaskSequenceCacheService service;
    private RemoteCacheManager mockCacheManager;
    private Map<String, String> mockStore;

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
        mockStore = new ConcurrentHashMap<>();

        when(mockCacheManager.isStarted()).thenReturn(true);
        when(mockCacheManager.getCache(anyString())).thenAnswer(inv -> {
            RemoteCache<String, String> mockCache = mock(RemoteCache.class);
            when(mockCache.get(anyString())).thenAnswer(i -> mockStore.get(i.getArgument(0)));
            when(mockCache.put(anyString(), anyString())).thenAnswer(i -> mockStore.put(i.getArgument(0), i.getArgument(1)));
            when(mockCache.remove(anyString())).thenAnswer(i -> mockStore.remove(i.getArgument(0)));

            CloseableIteratorCollection<String> mockValues = mock(CloseableIteratorCollection.class);
            when(mockValues.iterator()).thenAnswer(i -> toCloseableIterator(mockStore.values().iterator()));
            when(mockValues.stream()).thenAnswer(i -> mockStore.values().stream());
            when(mockValues.isEmpty()).thenAnswer(i -> mockStore.isEmpty());
            when(mockValues.size()).thenAnswer(i -> mockStore.size());
            doReturn(mockValues).when(mockCache).values();

            CloseableIteratorSet<String> mockKeys = mock(CloseableIteratorSet.class);
            when(mockKeys.iterator()).thenAnswer(i -> toCloseableIterator(mockStore.keySet().iterator()));
            when(mockKeys.stream()).thenAnswer(i -> mockStore.keySet().stream());
            when(mockKeys.isEmpty()).thenAnswer(i -> mockStore.isEmpty());
            when(mockKeys.size()).thenAnswer(i -> mockStore.size());
            doReturn(mockKeys).when(mockCache).keySet();

            when(mockCache.size()).thenAnswer(i -> mockStore.size());
            when(mockCache.isEmpty()).thenAnswer(i -> mockStore.isEmpty());
            return mockCache;
        });

        service = new TaskSequenceCacheService(mockCacheManager);
        service.init();
    }

    @Test
    @DisplayName("Verify saveSequence persists definition to RemoteCache and getSequence retrieves it")
    void testSaveAndGetSequence() {
        mockStore.clear();
        PraxisDefinition def = new PraxisDefinition("seq-test-1", "Test Sequence 1");
        def.setDescription("Test sequence description");
        def.setEnabled(true);
        def.setTopicSubscriptions(List.of(TopicSubscription.forAll()));

        PraxisDefinition saved = service.saveSequence(def);
        assertThat(saved).isNotNull();
        assertThat(mockStore).containsKey("seq-test-1");

        Optional<PraxisDefinition> fetched = service.getSequence("seq-test-1");
        assertThat(fetched).isPresent();
        assertThat(fetched.get().getPraxisName()).isEqualTo("Test Sequence 1");
    }

    @Test
    @DisplayName("Verify getAllSequences and getAllSequenceJsons returns definitions from RemoteCache")
    void testGetAllSequences() {
        mockStore.clear();
        PraxisDefinition def1 = new PraxisDefinition("seq-test-1", "Sequence 1");
        PraxisDefinition def2 = new PraxisDefinition("seq-test-2", "Sequence 2");

        service.saveSequence(def1);
        service.saveSequence(def2);

        List<String> jsons = service.getAllSequenceJsons();
        assertThat(jsons).hasSize(2);

        List<PraxisDefinition> defs = service.getAllSequences();
        assertThat(defs).hasSize(2);
    }

    @Test
    @DisplayName("Verify deleteSequence removes entry from RemoteCache")
    void testDeleteSequence() {
        mockStore.clear();
        PraxisDefinition def = new PraxisDefinition("seq-delete-me", "To Delete");
        service.saveSequence(def);
        assertThat(mockStore).containsKey("seq-delete-me");

        boolean deleted = service.deleteSequence("seq-delete-me");
        assertThat(deleted).isTrue();
        assertThat(mockStore).doesNotContainKey("seq-delete-me");
        assertThat(service.getSequence("seq-delete-me")).isEmpty();
    }

    @Test
    @DisplayName("Verify count delegates to RemoteCache size")
    void testCount() {
        mockStore.clear();
        assertThat(service.count()).isEqualTo(0);

        PraxisDefinition def = new PraxisDefinition("seq-count-1", "Count 1");
        service.saveSequence(def);

        assertThat(service.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("Verify valid cache miss returns Optional.empty() without error")
    void testValidCacheMissReturnsEmpty() {
        assertThat(service.getSequence("non-existent-seq")).isEmpty();
        assertThat(service.getSequenceJson("non-existent-seq")).isEmpty();
    }

    @Test
    @DisplayName("Verify ensureDefaultSequences seeds default sequence when RemoteCache is empty")
    void testEnsureDefaultSequencesWhenEmpty() {
        mockStore.clear();
        service.ensureDefaultSequences();

        assertThat(mockStore).containsKey("seq-patient-identity-pipeline");
        assertThat(service.getSequence("seq-patient-identity-pipeline")).isPresent();
    }

    @Test
    @DisplayName("Verify ensureDefaultSequences skips seeding when RemoteCache already has entries")
    void testEnsureDefaultSequencesWhenNotEmpty() {
        mockStore.clear();
        PraxisDefinition custom = new PraxisDefinition("custom-seq", "Custom Sequence");
        service.saveSequence(custom);

        service.ensureDefaultSequences();

        assertThat(mockStore).containsKey("custom-seq");
        assertThat(mockStore).doesNotContainKey("seq-patient-identity-pipeline");
    }

    @Test
    @DisplayName("Verify ensureDefaultSequences gracefully skips when RemoteCache is unavailable")
    void testEnsureDefaultSequencesWhenUnavailable() {
        TaskSequenceCacheService unconfigured = new TaskSequenceCacheService(null);
        unconfigured.init();

        // No exception during init/ensureDefaultSequences when cache is absent
        assertThat(unconfigured.getObjectMapper()).isNotNull();
    }

    @Test
    @DisplayName("Verify explicit IllegalStateException when RemoteCacheManager is null")
    void testExplicitFailureWhenRemoteCacheManagerNull() {
        TaskSequenceCacheService unconfigured = new TaskSequenceCacheService(null);
        unconfigured.init();

        PraxisDefinition def = new PraxisDefinition("seq-1", "Seq 1");

        assertThatThrownBy(() -> unconfigured.saveSequence(def))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("unavailable");

        assertThatThrownBy(() -> unconfigured.getSequence("seq-1"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("unavailable");

        assertThatThrownBy(() -> unconfigured.getSequenceJson("seq-1"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("unavailable");

        assertThatThrownBy(() -> unconfigured.getAllSequenceJsons())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("unavailable");

        assertThatThrownBy(() -> unconfigured.deleteSequence("seq-1"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("unavailable");

        assertThatThrownBy(() -> unconfigured.count())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("unavailable");
    }

    @Test
    @DisplayName("Verify explicit IllegalStateException when RemoteCacheManager is not started")
    void testExplicitFailureWhenRemoteCacheManagerNotStarted() {
        RemoteCacheManager unstartedManager = mock(RemoteCacheManager.class);
        when(unstartedManager.isStarted()).thenReturn(false);

        TaskSequenceCacheService unstartedService = new TaskSequenceCacheService(unstartedManager);
        unstartedService.init();

        PraxisDefinition def = new PraxisDefinition("seq-1", "Seq 1");

        assertThatThrownBy(() -> unstartedService.saveSequence(def))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("unavailable");
    }

    @Test
    @DisplayName("Verify explicit IllegalStateException when getCache returns null")
    void testExplicitFailureWhenNamedCacheNull() {
        RemoteCacheManager manager = mock(RemoteCacheManager.class);
        when(manager.isStarted()).thenReturn(true);
        when(manager.getCache(TaskSequenceCacheService.SEQUENCE_CACHE_NAME)).thenReturn(null);

        TaskSequenceCacheService nullCacheService = new TaskSequenceCacheService(manager);
        nullCacheService.init();

        assertThatThrownBy(() -> nullCacheService.getSequence("seq-1"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("unavailable");
    }

    @Test
    @DisplayName("Verify transport exceptions from RemoteCache propagate")
    void testRemoteCacheExceptionPropagates() {
        RemoteCacheManager manager = mock(RemoteCacheManager.class);
        when(manager.isStarted()).thenReturn(true);
        @SuppressWarnings("unchecked")
        RemoteCache<String, String> failingCache = mock(RemoteCache.class);
        when(failingCache.get("fail-seq")).thenThrow(new HotRodClientException("HotRod transport timeout"));
        doReturn(failingCache).when(manager).getCache(TaskSequenceCacheService.SEQUENCE_CACHE_NAME);

        TaskSequenceCacheService failingService = new TaskSequenceCacheService(manager);
        failingService.init();

        assertThatThrownBy(() -> failingService.getSequenceJson("fail-seq"))
                .isInstanceOf(HotRodClientException.class)
                .hasMessageContaining("HotRod transport timeout");
    }

    @Test
    @DisplayName("Verify recovery upon cache reconnection without stale fallback state")
    void testRecoveryUponCacheReconnection() {
        TaskSequenceCacheService reconnectingService = new TaskSequenceCacheService(null);
        reconnectingService.init();

        PraxisDefinition def = new PraxisDefinition("seq-rec-1", "Recovered Sequence");

        // Fails while disconnected
        assertThatThrownBy(() -> reconnectingService.saveSequence(def))
                .isInstanceOf(IllegalStateException.class);

        // Reconnects
        reconnectingService.setRemoteCacheManager(mockCacheManager);

        // Succeeds now
        PraxisDefinition saved = reconnectingService.saveSequence(def);
        assertThat(saved).isNotNull();
        Optional<PraxisDefinition> fetched = reconnectingService.getSequence("seq-rec-1");
        assertThat(fetched).isPresent();
        assertThat(fetched.get().getPraxisName()).isEqualTo("Recovered Sequence");
    }
}
