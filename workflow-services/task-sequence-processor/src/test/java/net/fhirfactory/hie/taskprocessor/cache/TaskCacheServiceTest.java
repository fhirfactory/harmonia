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

package net.fhirfactory.hie.taskprocessor.cache;

import ca.uhn.fhir.context.FhirContext;
import net.fhirfactory.hie.model.task.HieTaskReason;
import org.hl7.fhir.r5.model.CodeableConcept;
import org.hl7.fhir.r5.model.Reference;
import org.hl7.fhir.r5.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskCacheServiceTest {

    private TaskCacheService taskCacheService;

    @BeforeEach
    void setUp() {
        taskCacheService = new TaskCacheService();
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
        assertThat(HieTaskReason.hasReason(saved, HieTaskReason.HIE_SYNTHETIC_TASK)).isTrue();

        Optional<Task> retrieved = taskCacheService.getTask("task-100");
        assertThat(retrieved).isPresent();
        assertThat(retrieved.get().getDescription()).isEqualTo("Initial Lab Order Task");
        assertThat(retrieved.get().getStatus()).isEqualTo(Task.TaskStatus.REQUESTED);
        assertThat(HieTaskReason.hasReason(retrieved.get(), HieTaskReason.HIE_SYNTHETIC_TASK)).isTrue();
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
    }
}
