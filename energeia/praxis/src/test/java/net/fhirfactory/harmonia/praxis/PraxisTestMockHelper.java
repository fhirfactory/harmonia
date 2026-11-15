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

package net.fhirfactory.harmonia.praxis;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.fhirfactory.harmonia.praxis.cache.PragmaCacheService;
import net.fhirfactory.harmonia.praxis.service.PraxisService;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.commons.util.CloseableIterator;
import org.infinispan.commons.util.CloseableIteratorCollection;
import org.infinispan.commons.util.CloseableIteratorSet;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class PraxisTestMockHelper {

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

    @SuppressWarnings("unchecked")
    public static PraxisService createMockPraxisService() {
        RemoteCacheManager mockCacheManager = mock(RemoteCacheManager.class);
        RemoteCache<String, String> mockCache = mock(RemoteCache.class);
        Map<String, String> remoteStore = new ConcurrentHashMap<>();

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
        doReturn(mockValues).when(mockCache).values();

        CloseableIteratorSet<String> mockKeys = mock(CloseableIteratorSet.class);
        when(mockKeys.iterator()).thenAnswer(i -> toCloseableIterator(remoteStore.keySet().iterator()));
        when(mockKeys.stream()).thenAnswer(i -> remoteStore.keySet().stream());
        when(mockKeys.isEmpty()).thenAnswer(i -> remoteStore.isEmpty());
        when(mockKeys.size()).thenAnswer(i -> remoteStore.size());
        doReturn(mockKeys).when(mockCache).keySet();

        return new PraxisService(mockCacheManager, new ObjectMapper());
    }

    @SuppressWarnings("unchecked")
    public static PragmaCacheService createMockPragmaCacheService() {
        RemoteCacheManager mockCacheManager = mock(RemoteCacheManager.class);
        RemoteCache<String, String> mockCache = mock(RemoteCache.class);
        Map<String, String> remoteStore = new ConcurrentHashMap<>();

        when(mockCacheManager.isStarted()).thenReturn(true);
        doReturn(mockCache).when(mockCacheManager).getCache(eq(PragmaCacheService.PRAGMA_CACHE_NAME));

        when(mockCache.get(anyString())).thenAnswer(i -> remoteStore.get(i.getArgument(0)));
        when(mockCache.put(anyString(), anyString())).thenAnswer(i -> remoteStore.put(i.getArgument(0), i.getArgument(1)));
        when(mockCache.remove(anyString())).thenAnswer(i -> remoteStore.remove(i.getArgument(0)));
        doAnswer(i -> {
            remoteStore.clear();
            return null;
        }).when(mockCache).clear();
        when(mockCache.size()).thenAnswer(i -> remoteStore.size());
        when(mockCache.isEmpty()).thenAnswer(i -> remoteStore.isEmpty());

        return new PragmaCacheService(mockCacheManager);
    }
}
