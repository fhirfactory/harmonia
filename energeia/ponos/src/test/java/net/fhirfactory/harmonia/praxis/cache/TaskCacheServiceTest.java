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

import ca.uhn.fhir.context.FhirContext;
import net.fhirfactory.harmonia.model.ergon.ErgonReasonEnum;
import net.fhirfactory.harmonia.model.pragma.Pragma;
import net.fhirfactory.harmonia.model.pragma.PragmaStatus;
import org.hl7.fhir.r5.model.Provenance;
import org.hl7.fhir.r5.model.Reference;
import org.hl7.fhir.r5.model.Task;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.exceptions.HotRodClientException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class TaskCacheServiceTest {

    private TaskCacheService taskCacheService;
    private RemoteCacheManager mockCacheManager;
    private RemoteCache<String, String> mockTaskCache;
    private RemoteCache<String, String> mockProvCache;
    private Map<String, String> remoteTaskStore;
    private Map<String, String> remoteProvStore;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        mockCacheManager = mock(RemoteCacheManager.class);
        mockTaskCache = mock(RemoteCache.class);
        mockProvCache = mock(RemoteCache.class);
        remoteTaskStore = new ConcurrentHashMap<>();
        remoteProvStore = new ConcurrentHashMap<>();

        when(mockCacheManager.isStarted()).thenReturn(true);
        doReturn(mockTaskCache).when(mockCacheManager).getCache(eq(TaskCacheService.TASK_CACHE_NAME));
        doReturn(mockProvCache).when(mockCacheManager).getCache(eq(TaskCacheService.PROVENANCE_CACHE_NAME));

        when(mockTaskCache.get(anyString())).thenAnswer(i -> remoteTaskStore.get(i.getArgument(0)));
        when(mockTaskCache.put(anyString(), anyString())).thenAnswer(i -> remoteTaskStore.put(i.getArgument(0), i.getArgument(1)));
        when(mockTaskCache.remove(anyString())).thenAnswer(i -> remoteTaskStore.remove(i.getArgument(0)));
        doAnswer(i -> {
            remoteTaskStore.clear();
            return null;
        }).when(mockTaskCache).clear();
        when(mockTaskCache.size()).thenAnswer(i -> remoteTaskStore.size());
        when(mockTaskCache.isEmpty()).thenAnswer(i -> remoteTaskStore.isEmpty());

        when(mockProvCache.get(anyString())).thenAnswer(i -> remoteProvStore.get(i.getArgument(0)));
        when(mockProvCache.put(anyString(), anyString())).thenAnswer(i -> remoteProvStore.put(i.getArgument(0), i.getArgument(1)));
        when(mockProvCache.remove(anyString())).thenAnswer(i -> remoteProvStore.remove(i.getArgument(0)));
        doAnswer(i -> {
            remoteProvStore.clear();
            return null;
        }).when(mockProvCache).clear();
        when(mockProvCache.size()).thenAnswer(i -> remoteProvStore.size());
        when(mockProvCache.isEmpty()).thenAnswer(i -> remoteProvStore.isEmpty());

        taskCacheService = new TaskCacheService(mockCacheManager, FhirContext.forR5());
        taskCacheService.init();
        taskCacheService.clear();
    }

    @Test
    @DisplayName("Should save and retrieve a Task from cache")
    void testSaveAndGetTask() {
        Task task = new Task();
        task.setId("Task/task-100");
        task.setStatus(Task.TaskStatus.REQUESTED);
        task.setIntent(Task.TaskIntent.ORDER);
        task.setDescription("Initial Lab Order Task");

        Task saved = taskCacheService.saveTask(task);
        assertThat(saved).isNotNull();
        assertThat(saved.getIdElement().getIdPart()).isEqualTo("task-100");
        assertThat(ErgonReasonEnum.hasReason(saved, ErgonReasonEnum.HIE_SYNTHETIC_TASK)).isTrue();

        Optional<Task> retrieved = taskCacheService.getTask("task-100");
        assertThat(retrieved).isPresent();
        assertThat(retrieved.get().getDescription()).isEqualTo("Initial Lab Order Task");
        assertThat(retrieved.get().getStatus()).isEqualTo(Task.TaskStatus.REQUESTED);
        assertThat(ErgonReasonEnum.hasReason(retrieved.get(), ErgonReasonEnum.HIE_SYNTHETIC_TASK)).isTrue();
    }

    @Test
    @DisplayName("Should mark a Task as processed and update status, notes, and business status")
    void testMarkTaskAsProcessed() {
        Task task = new Task();
        task.setId("Task/task-200");
        task.setStatus(Task.TaskStatus.INPROGRESS);
        task.setIntent(Task.TaskIntent.ORDER);
        task.setFor(new Reference("Patient/pat-1"));
        task.setDescription("ADT Ingestion Task");

        taskCacheService.saveTask(task);

        Task processed = taskCacheService.markTaskAsProcessed(task);
        assertThat(processed).isNotNull();
        assertThat(processed.getStatus()).isEqualTo(Task.TaskStatus.COMPLETED);
        assertThat(processed.getLastModified()).isNotNull();
        assertThat(processed.hasBusinessStatus()).isTrue();
        assertThat(processed.getBusinessStatus().getText()).isEqualTo("PROCESSED");
        assertThat(processed.getNote()).isNotEmpty();
        assertThat(processed.getNote().get(0).getText()).contains("Processed by HIE task-processor");

        // Verify updated in cache
        Optional<Task> cached = taskCacheService.getTask("task-200");
        assertThat(cached).isPresent();
        assertThat(cached.get().getStatus()).isEqualTo(Task.TaskStatus.COMPLETED);
        assertThat(cached.get().getBusinessStatus().getText()).isEqualTo("PROCESSED");
        assertThat(cached.get().getNote()).isNotEmpty();
    }

    @Test
    @DisplayName("Should mark task as processed by ID")
    void testMarkTaskAsProcessedById() {
        Task task = new Task();
        task.setId("Task/task-300");
        task.setStatus(Task.TaskStatus.ACCEPTED);
        taskCacheService.saveTask(task);

        Optional<Task> processedOpt = taskCacheService.markTaskAsProcessed("task-300");
        assertThat(processedOpt).isPresent();
        assertThat(processedOpt.get().getStatus()).isEqualTo(Task.TaskStatus.COMPLETED);
    }

    @Test
    @DisplayName("Should save, retrieve, and delete Provenance in cache")
    void testProvenanceOperations() {
        Provenance prov = new Provenance();
        prov.setId("Provenance/prov-100");
        prov.setRecorded(new java.util.Date());
        prov.addTarget(new Reference("Task/task-100"));

        Provenance saved = taskCacheService.saveProvenance(prov);
        assertThat(saved).isNotNull();
        assertThat(saved.getIdPart()).isEqualTo("prov-100");

        Optional<Provenance> retrieved = taskCacheService.getProvenance("prov-100");
        assertThat(retrieved).isPresent();
        assertThat(retrieved.get().getTarget()).hasSize(1);

        boolean deleted = taskCacheService.deleteProvenance("prov-100");
        assertThat(deleted).isTrue();
        assertThat(taskCacheService.getProvenance("prov-100")).isEmpty();
    }

    @Test
    @DisplayName("Should save and retrieve Pragma in cache")
    void testPragmaOperations() {
        Pragma pragma = new Pragma("PRAGMA-100", "workflow-100", PragmaStatus.IN_PROGRESS);
        Pragma saved = taskCacheService.savePragma(pragma);
        assertThat(saved).isNotNull();

        Optional<Pragma> retrieved = taskCacheService.getPragma("PRAGMA-100");
        assertThat(retrieved).isPresent();
        assertThat(retrieved.get().getPragmaId()).isEqualTo("PRAGMA-100");
    }

    @Test
    @DisplayName("Should delete task and clear cache")
    void testDeleteAndClear() {
        Task task = new Task();
        task.setId("Task/task-400");
        taskCacheService.saveTask(task);
        assertThat(taskCacheService.count()).isEqualTo(1);

        boolean deleted = taskCacheService.deleteTask("task-400");
        assertThat(deleted).isTrue();
        assertThat(taskCacheService.count()).isEqualTo(0);

        taskCacheService.saveTask(task);
        taskCacheService.clear();
        assertThat(taskCacheService.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should reject null tasks")
    void testNullTaskHandling() {
        assertThatThrownBy(() -> taskCacheService.saveTask(null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> taskCacheService.markTaskAsProcessed((Task) null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> taskCacheService.saveProvenance(null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> taskCacheService.savePragma(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("savePragma fails explicitly when cache is unavailable")
    void testSavePragmaFailureWhenCacheUnavailable() {
        when(mockCacheManager.isStarted()).thenReturn(false);
        Pragma pragma = new Pragma();
        pragma.setPragmaId("pragma-1");

        assertThatThrownBy(() -> taskCacheService.savePragma(pragma))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Mneme cache [" + TaskCacheService.TASK_CACHE_NAME + "] is unavailable");
    }

    @Test
    @DisplayName("savePragma succeeds when cache is available")
    void testSavePragmaSuccess() {
        Pragma pragma = new Pragma();
        pragma.setPragmaId("pragma-2");

        Pragma saved = taskCacheService.savePragma(pragma);
        assertThat(saved).isNotNull();
        assertThat(saved.getPragmaId()).isEqualTo("pragma-2");

        verify(mockTaskCache).put(eq("pragma-2"), anyString());
    }

    @Test
    @DisplayName("Explicit failure when RemoteCacheManager is null")
    void testExplicitFailureWhenCacheManagerNull() {
        TaskCacheService unconfigured = new TaskCacheService();
        Task task = new Task();
        task.setId("Task/task-unconf");

        assertThatThrownBy(() -> unconfigured.saveTask(task))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("task-cache");

        assertThatThrownBy(() -> unconfigured.getTask("task-unconf"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("task-cache");

        assertThatThrownBy(() -> unconfigured.deleteTask("task-unconf"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("task-cache");

        assertThatThrownBy(unconfigured::count)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("task-cache");

        assertThatThrownBy(unconfigured::clear)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("task-cache");

        Provenance prov = new Provenance();
        prov.setId("Provenance/prov-unconf");
        assertThatThrownBy(() -> unconfigured.saveProvenance(prov))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("provenance-cache");

        assertThatThrownBy(() -> unconfigured.getProvenance("prov-unconf"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("provenance-cache");

        assertThatThrownBy(() -> unconfigured.deleteProvenance("prov-unconf"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("provenance-cache");
    }

    @Test
    @DisplayName("Explicit failure when RemoteCacheManager is not started")
    void testExplicitFailureWhenCacheManagerNotStarted() {
        when(mockCacheManager.isStarted()).thenReturn(false);
        Task task = new Task();
        task.setId("Task/task-stopped");

        assertThatThrownBy(() -> taskCacheService.saveTask(task))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("task-cache");
    }

    @Test
    @DisplayName("Remote cache transport exception propagation")
    void testRemoteCacheExceptionPropagation() {
        when(mockTaskCache.put(anyString(), anyString())).thenThrow(new HotRodClientException("Connection lost"));
        Task task = new Task();
        task.setId("Task/task-err");

        assertThatThrownBy(() -> taskCacheService.saveTask(task))
                .isInstanceOf(HotRodClientException.class)
                .hasMessageContaining("Connection lost");
    }

    @Test
    @DisplayName("Zero local fallback state mutation when cache fails")
    void testZeroLocalFallbackStateMutation() {
        when(mockCacheManager.isStarted()).thenReturn(false);
        Task task = new Task();
        task.setId("Task/task-no-fallback");

        assertThatThrownBy(() -> taskCacheService.saveTask(task))
                .isInstanceOf(IllegalStateException.class);

        // Reconnect cache
        when(mockCacheManager.isStarted()).thenReturn(true);

        assertThat(taskCacheService.getTask("task-no-fallback")).isEmpty();
        assertThat(taskCacheService.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("Service recovery upon cache reconnection")
    void testRecoveryUponReconnect() {
        when(mockCacheManager.isStarted()).thenReturn(false);
        Task task = new Task();
        task.setId("Task/task-reconnect");

        assertThatThrownBy(() -> taskCacheService.saveTask(task))
                .isInstanceOf(IllegalStateException.class);

        // Reconnect
        when(mockCacheManager.isStarted()).thenReturn(true);

        Task saved = taskCacheService.saveTask(task);
        assertThat(saved).isNotNull();
        assertThat(taskCacheService.getTask("task-reconnect")).isPresent();
        assertThat(taskCacheService.count()).isEqualTo(1);
    }
}
