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

package net.fhirfactory.harmonia.model.ergon;

import org.hl7.fhir.r5.model.CodeableConcept;
import org.hl7.fhir.r5.model.CodeableReference;
import org.hl7.fhir.r5.model.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

class ErgonReasonEnumTest {

    @Test
    @DisplayName("Verify all required HIE Task reasons exist with correct codes")
    void testEnumCodes() {
        assertThat(ErgonReasonEnum.HIE_SYNTHETIC_TASK.getCode()).isEqualTo("HIE-Synthetic-Task");
        assertThat(ErgonReasonEnum.HIE_ADMINISTRATION_TASK.getCode()).isEqualTo("HIE-Administration-Task");
        assertThat(ErgonReasonEnum.HIE_APPROVAL_TASK.getCode()).isEqualTo("HIE-Approval-Task");
        assertThat(ErgonReasonEnum.HIE_INFORMATION_REQUEST_TASK.getCode()).isEqualTo("HIE-InformationRequest-Task");
    }

    @ParameterizedTest
    @EnumSource(ErgonReasonEnum.class)
    @DisplayName("Verify toCodeableConcept conversion has system, code, and display")
    void testToCodeableConcept(ErgonReasonEnum reason) {
        CodeableConcept concept = reason.toCodeableConcept();
        assertThat(concept).isNotNull();
        assertThat(concept.getText()).isEqualTo(reason.getCode());
        assertThat(concept.getCoding()).hasSize(1);
        assertThat(concept.getCoding().get(0).getSystem()).isEqualTo(ErgonReasonEnum.TASK_REASON_SYSTEM);
        assertThat(concept.getCoding().get(0).getCode()).isEqualTo(reason.getCode());
        assertThat(concept.getCoding().get(0).getDisplay()).isEqualTo(reason.getDisplay());
    }

    @ParameterizedTest
    @EnumSource(ErgonReasonEnum.class)
    @DisplayName("Verify toCodeableReference conversion wraps CodeableConcept")
    void testToCodeableReference(ErgonReasonEnum reason) {
        CodeableReference reference = reason.toCodeableReference();
        assertThat(reference).isNotNull();
        assertThat(reference.hasConcept()).isTrue();
        assertThat(reference.getConcept().getText()).isEqualTo(reason.getCode());
    }

    @Test
    @DisplayName("Apply reason to Task and verify hasReason and ensureSyntheticTaskReason")
    void testApplyToTask() {
        Task task = new Task();
        assertThat(ErgonReasonEnum.hasReason(task, ErgonReasonEnum.HIE_SYNTHETIC_TASK)).isFalse();

        ErgonReasonEnum.HIE_SYNTHETIC_TASK.applyTo(task);
        assertThat(task.hasReason()).isTrue();
        assertThat(ErgonReasonEnum.hasReason(task, ErgonReasonEnum.HIE_SYNTHETIC_TASK)).isTrue();
        assertThat(task.getReason().get(0).getConcept().getCodingFirstRep().getCode()).isEqualTo("HIE-Synthetic-Task");

        // Overwrite / change reason
        ErgonReasonEnum.HIE_APPROVAL_TASK.applyTo(task);
        assertThat(ErgonReasonEnum.hasReason(task, ErgonReasonEnum.HIE_APPROVAL_TASK)).isTrue();

        // ensureSyntheticTaskReason when reason already exists should not overwrite
        ErgonReasonEnum.ensureSyntheticTaskReason(task);
        assertThat(ErgonReasonEnum.hasReason(task, ErgonReasonEnum.HIE_APPROVAL_TASK)).isTrue();

        // ensureSyntheticTaskReason on empty task should set synthetic task
        Task emptyTask = new Task();
        ErgonReasonEnum.ensureSyntheticTaskReason(emptyTask);
        assertThat(ErgonReasonEnum.hasReason(emptyTask, ErgonReasonEnum.HIE_SYNTHETIC_TASK)).isTrue();
    }

    @Test
    @DisplayName("Test fromCode lookup")
    void testFromCode() {
        assertThat(ErgonReasonEnum.fromCode("HIE-Synthetic-Task")).contains(ErgonReasonEnum.HIE_SYNTHETIC_TASK);
        assertThat(ErgonReasonEnum.fromCode("HIE-Administration-Task")).contains(ErgonReasonEnum.HIE_ADMINISTRATION_TASK);
        assertThat(ErgonReasonEnum.fromCode("HIE-Approval-Task")).contains(ErgonReasonEnum.HIE_APPROVAL_TASK);
        assertThat(ErgonReasonEnum.fromCode("HIE-InformationRequest-Task")).contains(ErgonReasonEnum.HIE_INFORMATION_REQUEST_TASK);
        assertThat(ErgonReasonEnum.fromCode("hie_synthetic_task")).contains(ErgonReasonEnum.HIE_SYNTHETIC_TASK);
        assertThat(ErgonReasonEnum.fromCode("unknown")).isEmpty();
        assertThat(ErgonReasonEnum.fromCode(null)).isEmpty();
    }
}
