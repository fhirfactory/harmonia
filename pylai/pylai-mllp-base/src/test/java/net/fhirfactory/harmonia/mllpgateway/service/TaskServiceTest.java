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

import net.fhirfactory.harmonia.model.ergon.ErgonReasonEnum;
import net.fhirfactory.harmonia.model.security.FhirConfidentialityEnum;
import net.fhirfactory.harmonia.model.security.FhirSecurityTagManager;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.Reference;
import org.hl7.fhir.r5.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class TaskServiceTest {

    private DefaultTaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new DefaultTaskService();
        taskService.clear();
    }

    @Test
    @DisplayName("Create, Read, Update, Delete Task resource")
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
    }

    @Test
    @DisplayName("Search Tasks by ID, Status, Priority, Patient, Identifier, and Text")
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
}
