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

package net.fhirfactory.harmonia.praxis.cache;

import net.fhirfactory.harmonia.model.pragma.Pragma;
import net.fhirfactory.harmonia.model.pragma.PragmaCheckpoint;
import net.fhirfactory.harmonia.model.pragma.PragmaStatus;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.exceptions.HotRodClientException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class PragmaCacheServiceTest {

    private PragmaCacheService pragmaCacheService;
    private RemoteCacheManager mockCacheManager;
    private RemoteCache<String, String> mockCache;
    private Map<String, String> remoteStore;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        mockCacheManager = mock(RemoteCacheManager.class);
        mockCache = mock(RemoteCache.class);
        remoteStore = new ConcurrentHashMap<>();

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

        pragmaCacheService = new PragmaCacheService(mockCacheManager);
        pragmaCacheService.init();
        pragmaCacheService.clear();
    }

    @Test
    @DisplayName("Should save and retrieve Pragma from remote cache")
    void testSaveAndGetPragma() {
        Pragma pragma = new Pragma("PRAGMA-001", "praxis-test-workflow", PragmaStatus.IN_PROGRESS);
        pragma.addCheckpoint(PragmaCheckpoint.builder()
                .pragmaId("PRAGMA-001")
                .ergonId("ergon-validate")
                .stageName("INGRESS")
                .status(PragmaStatus.IN_PROGRESS)
                .build());

        Pragma saved = pragmaCacheService.savePragma(pragma);
        assertThat(saved).isNotNull();
        assertThat(saved.getPragmaId()).isEqualTo("PRAGMA-001");
        assertThat(saved.getLastModified()).isNotNull();

        Optional<Pragma> retrieved = pragmaCacheService.getPragma("PRAGMA-001");
        assertThat(retrieved).isPresent();
        assertThat(retrieved.get().getPragmaId()).isEqualTo("PRAGMA-001");
        assertThat(retrieved.get().getStatus()).isEqualTo(PragmaStatus.IN_PROGRESS);
        assertThat(retrieved.get().getCheckpoints()).hasSize(1);
        assertThat(retrieved.get().getCheckpoints().get(0).getErgonId()).isEqualTo("ergon-validate");

        assertThat(pragmaCacheService.containsPragma("PRAGMA-001")).isTrue();
        assertThat(pragmaCacheService.containsPragma("NON-EXISTENT")).isFalse();
    }

    @Test
    @DisplayName("Should retrieve checkpoints for a Pragma")
    void testGetCheckpoints() {
        Pragma pragma = new Pragma("PRAGMA-CHK", "workflow-chk", PragmaStatus.IN_PROGRESS);
        pragma.addCheckpoint(PragmaCheckpoint.builder()
                .pragmaId("PRAGMA-CHK")
                .ergonId("ergon-1")
                .stageName("STEP1")
                .build());
        pragma.addCheckpoint(PragmaCheckpoint.builder()
                .pragmaId("PRAGMA-CHK")
                .ergonId("ergon-2")
                .stageName("STEP2")
                .build());

        pragmaCacheService.savePragma(pragma);

        List<PragmaCheckpoint> checkpoints = pragmaCacheService.getCheckpoints("PRAGMA-CHK");
        assertThat(checkpoints).hasSize(2);
        assertThat(checkpoints.get(0).getStageName()).isEqualTo("STEP1");
        assertThat(checkpoints.get(1).getStageName()).isEqualTo("STEP2");

        assertThat(pragmaCacheService.getCheckpoints("NON-EXISTENT")).isEmpty();
    }

    @Test
    @DisplayName("Should delete Pragma and clear cache")
    void testDeleteAndClear() {
        Pragma pragma = new Pragma("PRAGMA-DEL", "workflow-del", PragmaStatus.COMPLETED);
        pragmaCacheService.savePragma(pragma);
        assertThat(pragmaCacheService.containsPragma("PRAGMA-DEL")).isTrue();

        boolean deleted = pragmaCacheService.deletePragma("PRAGMA-DEL");
        assertThat(deleted).isTrue();
        assertThat(pragmaCacheService.containsPragma("PRAGMA-DEL")).isFalse();

        pragmaCacheService.savePragma(pragma);
        pragmaCacheService.clear();
        assertThat(pragmaCacheService.containsPragma("PRAGMA-DEL")).isFalse();
    }

    @Test
    @DisplayName("Should handle null / invalid arguments safely")
    void testNullArguments() {
        assertThatThrownBy(() -> pragmaCacheService.savePragma(null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(pragmaCacheService.getPragma(null)).isEmpty();
        assertThat(pragmaCacheService.getPragma("")).isEmpty();
        assertThat(pragmaCacheService.deletePragma(null)).isFalse();
        assertThat(pragmaCacheService.deletePragma("")).isFalse();
    }

    @Test
    @DisplayName("Explicit failure when RemoteCacheManager is null")
    void testExplicitFailureWhenCacheManagerNull() {
        PragmaCacheService unconfigured = new PragmaCacheService();
        Pragma pragma = new Pragma("PRAGMA-TEST", "workflow-test", PragmaStatus.IN_PROGRESS);

        assertThatThrownBy(() -> unconfigured.savePragma(pragma))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("task-cache");

        assertThatThrownBy(() -> unconfigured.getPragma("PRAGMA-TEST"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("task-cache");

        assertThatThrownBy(() -> unconfigured.deletePragma("PRAGMA-TEST"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("task-cache");

        assertThatThrownBy(unconfigured::clear)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("task-cache");
    }

    @Test
    @DisplayName("Explicit failure when RemoteCacheManager is not started")
    void testExplicitFailureWhenCacheManagerNotStarted() {
        when(mockCacheManager.isStarted()).thenReturn(false);
        Pragma pragma = new Pragma("PRAGMA-TEST", "workflow-test", PragmaStatus.IN_PROGRESS);

        assertThatThrownBy(() -> pragmaCacheService.savePragma(pragma))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("task-cache");
    }

    @Test
    @DisplayName("Remote cache transport exception propagation")
    void testRemoteCacheExceptionPropagation() {
        when(mockCache.put(anyString(), anyString())).thenThrow(new HotRodClientException("Transport failed"));
        Pragma pragma = new Pragma("PRAGMA-FAIL", "workflow-fail", PragmaStatus.IN_PROGRESS);

        assertThatThrownBy(() -> pragmaCacheService.savePragma(pragma))
                .isInstanceOf(HotRodClientException.class)
                .hasMessageContaining("Transport failed");
    }

    @Test
    @DisplayName("Zero local fallback state mutation when cache fails")
    void testZeroLocalFallbackStateMutation() {
        when(mockCacheManager.isStarted()).thenReturn(false);
        Pragma pragma = new Pragma("PRAGMA-NO-FALLBACK", "workflow-fail", PragmaStatus.IN_PROGRESS);

        assertThatThrownBy(() -> pragmaCacheService.savePragma(pragma))
                .isInstanceOf(IllegalStateException.class);

        // Reconnect cache
        when(mockCacheManager.isStarted()).thenReturn(true);

        assertThat(pragmaCacheService.getPragma("PRAGMA-NO-FALLBACK")).isEmpty();
    }

    @Test
    @DisplayName("Service recovery upon cache reconnection")
    void testRecoveryUponReconnect() {
        when(mockCacheManager.isStarted()).thenReturn(false);
        Pragma pragma = new Pragma("PRAGMA-RECONNECT", "workflow-reconnect", PragmaStatus.IN_PROGRESS);

        assertThatThrownBy(() -> pragmaCacheService.savePragma(pragma))
                .isInstanceOf(IllegalStateException.class);

        // Reconnect
        when(mockCacheManager.isStarted()).thenReturn(true);

        Pragma saved = pragmaCacheService.savePragma(pragma);
        assertThat(saved).isNotNull();
        assertThat(pragmaCacheService.getPragma("PRAGMA-RECONNECT")).isPresent();
    }

    @Test
    @DisplayName("Delegation to TaskCacheService when configured")
    void testDelegationToTaskCacheService() {
        TaskCacheService mockTaskCacheService = mock(TaskCacheService.class);
        pragmaCacheService.setTaskCacheService(mockTaskCacheService);

        Pragma pragma = new Pragma("PRAGMA-DELEGATE", "workflow-delegate", PragmaStatus.IN_PROGRESS);
        pragmaCacheService.savePragma(pragma);

        verify(mockTaskCacheService).savePragma(pragma);

        // When not found in primary cache, queries taskCacheService
        remoteStore.clear();
        when(mockTaskCacheService.getPragma("PRAGMA-DELEGATE")).thenReturn(Optional.of(pragma));

        Optional<Pragma> found = pragmaCacheService.getPragma("PRAGMA-DELEGATE");
        assertThat(found).isPresent();
        assertThat(found.get().getPragmaId()).isEqualTo("PRAGMA-DELEGATE");
    }
}
