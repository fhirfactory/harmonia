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

import net.fhirfactory.harmonia.model.petasos.PetasosQueueDefinition;
import net.fhirfactory.harmonia.praxis.config.QueueConfig;
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

class MessageQueueServiceTest {

    private MessageQueueService service;
    private QueueConfig queueConfig;
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
        queueConfig = new QueueConfig();
        mockCacheManager = mock(RemoteCacheManager.class);
        mockCache = mock(RemoteCache.class);
        remoteStore = new ConcurrentHashMap<>();

        when(mockCacheManager.isStarted()).thenReturn(true);
        doReturn(mockCache).when(mockCacheManager).getCache(eq(MessageQueueService.QUEUE_CACHE_NAME));

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

        service = new MessageQueueService(mockCacheManager, queueConfig);
        service.init();
    }

    @Test
    @DisplayName("Should seed default queues and retrieve all from remote cache")
    void testSeedAndGetAll() {
        List<PetasosQueueDefinition> queues = service.getAll();
        assertThat(queues).isNotNull().isNotEmpty();
        assertThat(queues).anyMatch(q -> "task.processing.queue".equals(q.getQueueName()));
        assertThat(queues).anyMatch(q -> "task.event.queue".equals(q.getQueueName()));
        assertThat(service.count()).isGreaterThanOrEqualTo(4);
    }

    @Test
    @DisplayName("Should save and retrieve queue by ID from remote cache")
    void testSaveAndGetById() {
        PetasosQueueDefinition q = new PetasosQueueDefinition("custom.test.queue", "ANYCAST", true, "Custom Test Queue");
        service.save(q);

        Optional<PetasosQueueDefinition> retrieved = service.getById("custom.test.queue");
        assertThat(retrieved).isPresent();
        assertThat(retrieved.get().getQueueName()).isEqualTo("custom.test.queue");
        assertThat(retrieved.get().isDurable()).isTrue();
    }

    @Test
    @DisplayName("Should delete queue and clear cache")
    void testDeleteAndClear() {
        PetasosQueueDefinition q = new PetasosQueueDefinition("to.delete.queue");
        service.save(q);

        assertThat(service.getById("to.delete.queue")).isPresent();
        boolean deleted = service.delete("to.delete.queue");
        assertThat(deleted).isTrue();
        assertThat(service.getById("to.delete.queue")).isEmpty();

        service.save(q);
        service.clear();
        assertThat(service.getById("to.delete.queue")).isEmpty();
    }

    @Test
    @DisplayName("Should handle null / invalid arguments safely")
    void testNullArguments() {
        assertThatThrownBy(() -> service.save(null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> service.save(new PetasosQueueDefinition("")))
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(service.getById(null)).isEmpty();
        assertThat(service.getById("")).isEmpty();
        assertThat(service.delete(null)).isFalse();
        assertThat(service.delete("")).isFalse();
    }

    @Test
    @DisplayName("Explicit failure when RemoteCacheManager is null")
    void testExplicitFailureWhenCacheManagerNull() {
        MessageQueueService unconfigured = new MessageQueueService();
        PetasosQueueDefinition q = new PetasosQueueDefinition("queue-unconf");

        assertThatThrownBy(() -> unconfigured.save(q))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("messagequeue-cache");

        assertThatThrownBy(() -> unconfigured.getById("queue-unconf"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("messagequeue-cache");

        assertThatThrownBy(unconfigured::getAll)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("messagequeue-cache");

        assertThatThrownBy(() -> unconfigured.delete("queue-unconf"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("messagequeue-cache");

        assertThatThrownBy(unconfigured::count)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("messagequeue-cache");

        assertThatThrownBy(unconfigured::clear)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("messagequeue-cache");
    }

    @Test
    @DisplayName("Explicit failure when RemoteCacheManager is not started")
    void testExplicitFailureWhenCacheManagerNotStarted() {
        when(mockCacheManager.isStarted()).thenReturn(false);
        PetasosQueueDefinition q = new PetasosQueueDefinition("queue-test");

        assertThatThrownBy(() -> service.save(q))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("messagequeue-cache");
    }

    @Test
    @DisplayName("Remote cache transport exception propagation")
    void testRemoteCacheExceptionPropagation() {
        when(mockCache.put(anyString(), anyString())).thenThrow(new HotRodClientException("Connection dropped"));
        PetasosQueueDefinition q = new PetasosQueueDefinition("queue-fail");

        assertThatThrownBy(() -> service.save(q))
                .isInstanceOf(HotRodClientException.class)
                .hasMessageContaining("Connection dropped");
    }

    @Test
    @DisplayName("Zero local fallback state mutation when cache fails")
    void testZeroLocalFallbackStateMutation() {
        when(mockCacheManager.isStarted()).thenReturn(false);
        PetasosQueueDefinition q = new PetasosQueueDefinition("queue-fail-state");

        assertThatThrownBy(() -> service.save(q))
                .isInstanceOf(IllegalStateException.class);

        // Reconnect cache
        when(mockCacheManager.isStarted()).thenReturn(true);

        assertThat(service.getById("queue-fail-state")).isEmpty();
    }

    @Test
    @DisplayName("Service recovery upon cache reconnection")
    void testRecoveryUponReconnect() {
        when(mockCacheManager.isStarted()).thenReturn(false);
        PetasosQueueDefinition q = new PetasosQueueDefinition("queue-reconnect");

        assertThatThrownBy(() -> service.save(q))
                .isInstanceOf(IllegalStateException.class);

        // Reconnect
        when(mockCacheManager.isStarted()).thenReturn(true);

        PetasosQueueDefinition saved = service.save(q);
        assertThat(saved).isNotNull();
        assertThat(service.getById("queue-reconnect")).isPresent();
    }
}
