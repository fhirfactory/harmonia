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
import net.fhirfactory.harmonia.model.ergon.ErgonReasonEnum;
import net.fhirfactory.harmonia.model.security.FhirConfidentialityEnum;
import net.fhirfactory.harmonia.model.security.FhirSecurityTagManager;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.Reference;
import org.hl7.fhir.r5.model.Task;
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

class TaskServiceTest {

    private DefaultTaskService taskService;
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
        doReturn(mockCache).when(mockCacheManager).getCache(eq(DefaultTaskService.TASK_CACHE_NAME));

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

        taskService = new DefaultTaskService(mockCacheManager, FhirContext.forR5());
    }

    @Test
    @DisplayName("Create, Read, Update, Delete Task resource in remote cache")
    void testCrudOperations() {
        Task task = new Task();
        task.setId("Task/task-001");
        task.setStatus(Task.TaskStatus.REQUESTED);
        task.setIntent(Task.TaskIntent.ORDER);
        task.setPriority(Enumerations.RequestPriority.ROUTINE);
        task.setDescription("Process HL7 ADT A01 message");
        task.setFor(new Reference("Patient/PAT100").setDisplay("John Doe"));

        Task created = taskService.create(task);
        assertThat(created).isNotNull();
        assertThat(created.getIdPart()).isEqualTo("task-001");
        assertThat(taskService.count()).isEqualTo(1);
        assertThat(ErgonReasonEnum.hasReason(created, ErgonReasonEnum.HIE_SYNTHETIC_TASK)).isTrue();
        assertThat(FhirSecurityTagManager.hasConfidentiality(created, FhirConfidentialityEnum.N)).isTrue();

        // Verify it was persisted to remote store
        assertThat(remoteStore).containsKey("task-001");

        Optional<Task> fetched = taskService.getById("task-001");
        assertThat(fetched).isPresent();
        assertThat(fetched.get().getDescription()).isEqualTo("Process HL7 ADT A01 message");
        assertThat(FhirSecurityTagManager.hasConfidentiality(fetched.get(), FhirConfidentialityEnum.N)).isTrue();

        // Update
        Task toUpdate = fetched.get();
        toUpdate.setStatus(Task.TaskStatus.INPROGRESS);
        taskService.update("task-001", toUpdate);

        Optional<Task> updated = taskService.getById("task-001");
        assertThat(updated).isPresent();
        assertThat(updated.get().getStatus()).isEqualTo(Task.TaskStatus.INPROGRESS);
        assertThat(FhirSecurityTagManager.hasConfidentiality(updated.get(), FhirConfidentialityEnum.N)).isTrue();

        // Delete
        boolean deleted = taskService.delete("task-001");
        assertThat(deleted).isTrue();
        assertThat(taskService.count()).isEqualTo(0);
        assertThat(taskService.getById("task-001")).isEmpty();
        assertThat(remoteStore).doesNotContainKey("task-001");
    }

    @Test
    @DisplayName("Search Tasks by ID, Status, Priority, Patient, Identifier, and Text in remote cache")
    void testSearchOperations() {
        Task t1 = new Task();
        t1.setId("Task/task-A01");
        t1.setStatus(Task.TaskStatus.REQUESTED);
        t1.setPriority(Enumerations.RequestPriority.STAT);
        t1.setDescription("Admit patient John Doe");
        t1.setFor(new Reference("Patient/PAT-100").setDisplay("John Doe"));
        t1.addIdentifier().setSystem("http://example.org/visit-number").setValue("VISIT-1001");

        Task t2 = new Task();
        t2.setId("Task/task-A03");
        t2.setStatus(Task.TaskStatus.COMPLETED);
        t2.setPriority(Enumerations.RequestPriority.ROUTINE);
        t2.setDescription("Discharge patient Jane Smith");
        t2.setFor(new Reference("Patient/PAT-200").setDisplay("Jane Smith"));
        t2.addIdentifier().setSystem("http://example.org/visit-number").setValue("VISIT-2002");

        taskService.create(t1);
        taskService.create(t2);

        // Search by Status
        List<Task> byStatus = taskService.search(null, "requested", null, null, null, null);
        assertThat(byStatus).hasSize(1);
        assertThat(byStatus.get(0).getIdPart()).isEqualTo("task-A01");

        // Search by Priority
        List<Task> byPriority = taskService.search(null, null, "stat", null, null, null);
        assertThat(byPriority).hasSize(1);
        assertThat(byPriority.get(0).getIdPart()).isEqualTo("task-A01");

        // Search by Patient
        List<Task> byPatient = taskService.search(null, null, null, "PAT-200", null, null);
        assertThat(byPatient).hasSize(1);
        assertThat(byPatient.get(0).getIdPart()).isEqualTo("task-A03");

        // Search by Identifier
        List<Task> byIdent = taskService.search(null, null, null, null, "VISIT-1001", null);
        assertThat(byIdent).hasSize(1);
        assertThat(byIdent.get(0).getIdPart()).isEqualTo("task-A01");

        // Search by Text
        List<Task> byText = taskService.search(null, null, null, null, null, "Discharge");
        assertThat(byText).hasSize(1);
        assertThat(byText.get(0).getIdPart()).isEqualTo("task-A03");
    }

    @Test
    @DisplayName("Valid cache miss returns empty Optional rather than exception")
    void testValidCacheMissReturnsEmpty() {
        Optional<Task> result = taskService.getById("non-existent-task");
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Throws IllegalStateException when RemoteCacheManager is null")
    void testExplicitFailureWhenRemoteCacheManagerNull() {
        DefaultTaskService unconfiguredService = new DefaultTaskService(null, FhirContext.forR5());
        Task task = new Task();
        task.setId("Task/task-null-mgr");

        assertThatThrownBy(() -> unconfiguredService.create(task))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Mneme cache [task-cache] is unavailable");

        assertThatThrownBy(() -> unconfiguredService.getById("task-null-mgr"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Mneme cache [task-cache] is unavailable");

        assertThatThrownBy(() -> unconfiguredService.update("task-null-mgr", task))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Mneme cache [task-cache] is unavailable");

        assertThatThrownBy(() -> unconfiguredService.delete("task-null-mgr"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Mneme cache [task-cache] is unavailable");

        assertThatThrownBy(unconfiguredService::getAll)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Mneme cache [task-cache] is unavailable");

        assertThatThrownBy(() -> unconfiguredService.search(null, null, null, null, null, null))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Mneme cache [task-cache] is unavailable");

        assertThatThrownBy(unconfiguredService::count)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Mneme cache [task-cache] is unavailable");

        assertThatThrownBy(unconfiguredService::clear)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Mneme cache [task-cache] is unavailable");
    }

    @Test
    @DisplayName("Throws IllegalStateException when RemoteCacheManager is not started")
    void testExplicitFailureWhenRemoteCacheManagerNotStarted() {
        when(mockCacheManager.isStarted()).thenReturn(false);
        Task task = new Task();
        task.setId("Task/task-unstarted");

        assertThatThrownBy(() -> taskService.create(task))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Mneme cache [task-cache] is unavailable");

        assertThatThrownBy(() -> taskService.getById("task-unstarted"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Mneme cache [task-cache] is unavailable");
    }

    @Test
    @DisplayName("Throws IllegalStateException when getCache returns null")
    void testExplicitFailureWhenNamedCacheNull() {
        when(mockCacheManager.getCache(eq(DefaultTaskService.TASK_CACHE_NAME))).thenReturn(null);
        Task task = new Task();
        task.setId("Task/task-no-cache");

        assertThatThrownBy(() -> taskService.create(task))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Mneme cache [task-cache] is unavailable");
    }

    @Test
    @DisplayName("Propagates HotRodClientException from RemoteCache")
    void testRemoteCacheExceptionPropagation() {
        when(mockCache.put(anyString(), anyString())).thenThrow(new HotRodClientException("Connection timed out"));
        Task task = new Task();
        task.setId("Task/task-fail");

        assertThatThrownBy(() -> taskService.create(task))
                .isInstanceOf(HotRodClientException.class)
                .hasMessageContaining("Connection timed out");
    }

    @Test
    @DisplayName("Zero local fallback state mutation when cache fails")
    void testZeroLocalFallbackStateMutation() {
        when(mockCacheManager.isStarted()).thenReturn(false);
        Task task = new Task();
        task.setId("Task/task-no-fallback");
        task.setDescription("Should not be stored locally");

        assertThatThrownBy(() -> taskService.create(task))
                .isInstanceOf(IllegalStateException.class);

        // Reconnect cache
        when(mockCacheManager.isStarted()).thenReturn(true);

        // Cache was never populated with task-no-fallback
        Optional<Task> fetched = taskService.getById("task-no-fallback");
        assertThat(fetched).isEmpty();
        assertThat(taskService.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("Service recovery upon cache reconnection")
    void testRecoveryUponReconnect() {
        when(mockCacheManager.isStarted()).thenReturn(false);
        Task task = new Task();
        task.setId("Task/task-reconnect");
        task.setDescription("Reconnect test task");

        assertThatThrownBy(() -> taskService.create(task))
                .isInstanceOf(IllegalStateException.class);

        // Reconnect
        when(mockCacheManager.isStarted()).thenReturn(true);

        Task created = taskService.create(task);
        assertThat(created).isNotNull();
        assertThat(taskService.getById("task-reconnect")).isPresent();
        assertThat(taskService.count()).isEqualTo(1);
    }
}
