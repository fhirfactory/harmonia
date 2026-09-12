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

package net.fhirfactory.hie.taskprocessor.service;

import net.fhirfactory.hie.model.sequence.TaskSequenceDefinition;
import net.fhirfactory.hie.taskprocessor.sequence.TaskSequence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskSequenceServiceTest {

    private TaskSequenceService sequenceService;

    @BeforeEach
    void setUp() {
        sequenceService = new TaskSequenceService();
        sequenceService.init();
        sequenceService.clear();
    }

    @Test
    @DisplayName("Should create, retrieve, update, and delete TaskSequence in cache")
    void testCrudOperations() {
        TaskSequenceDefinition seq = new TaskSequenceDefinition("seq-admission", "Admission Workflow");
        seq.setSequenceDescription("Processes patient admissions from PAS gateway");
        seq.setVersion("1.1.0");
        seq.setTargetGatewayInstances(List.of("pas-gw"));
        seq.setTargetTriggerTypes(List.of("A01", "A04"));
        seq.setActivityIds(List.of("act-validate", "act-enrich"));

        // Save
        TaskSequenceDefinition saved = sequenceService.save(seq);
        assertThat(saved).isNotNull();
        assertThat(sequenceService.count()).isEqualTo(1);

        // Get by ID
        Optional<TaskSequenceDefinition> retrieved = sequenceService.getById("seq-admission");
        assertThat(retrieved).isPresent();
        assertThat(retrieved.get().getSequenceName()).isEqualTo("Admission Workflow");
        assertThat(retrieved.get().getVersion()).isEqualTo("1.1.0");
        assertThat(retrieved.get().getTargetGatewayInstances()).containsExactly("pas-gw");
        assertThat(retrieved.get().getTargetTriggerTypes()).containsExactly("A01", "A04");
        assertThat(retrieved.get().getActivityIds()).containsEntry(0, "act-validate").containsEntry(1, "act-enrich");

        // Update
        seq.setSequenceDescription("Updated description");
        seq.setTargetTriggerTypes(List.of("A01", "A04", "A08"));
        sequenceService.update(seq);

        Optional<TaskSequenceDefinition> updated = sequenceService.getById("seq-admission");
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
        TaskSequenceDefinition original = new TaskSequenceDefinition("seq-discharge", "Discharge Workflow");
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

        TaskSequenceDefinition deserialized = sequenceService.fromJson(json);
        assertThat(deserialized).isNotNull();
        assertThat(deserialized.getSequenceId()).isEqualTo("seq-discharge");
        assertThat(deserialized.getSequenceName()).isEqualTo("Discharge Workflow");
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
        TaskSequenceDefinition seq1 = new TaskSequenceDefinition("seq-pas-admit", "PAS Admit");
        seq1.setTargetGatewayInstances(List.of("pas-gw"));
        seq1.setTargetTriggerTypes(List.of("A01", "A04"));

        TaskSequenceDefinition seq2 = new TaskSequenceDefinition("seq-pas-discharge", "PAS Discharge");
        seq2.setTargetGatewayInstances(List.of("pas-gw"));
        seq2.setTargetTriggerTypes(List.of("A03"));

        TaskSequenceDefinition seq3 = new TaskSequenceDefinition("seq-lab-result", "Lab Results");
        seq3.setTargetGatewayInstances(List.of("lims-gw"));
        seq3.setTargetTriggerTypes(List.of("R01", "ORU^R01"));

        TaskSequenceDefinition seq4 = new TaskSequenceDefinition("seq-audit-all", "Audit All");
        seq4.setMatchAllGateways(true);
        seq4.setMatchAllTriggers(true);

        sequenceService.save(seq1);
        sequenceService.save(seq2);
        sequenceService.save(seq3);
        sequenceService.save(seq4);

        // Matching PAS A01 -> seq1 and seq4
        List<TaskSequenceDefinition> matchesPasA01 = sequenceService.findMatching("pas-gw", "A01");
        assertThat(matchesPasA01).extracting(TaskSequenceDefinition::getSequenceId)
                .containsExactlyInAnyOrder("seq-pas-admit", "seq-audit-all");

        // Matching PAS A03 -> seq2 and seq4
        List<TaskSequenceDefinition> matchesPasA03 = sequenceService.findMatching("pas-gw", "A03");
        assertThat(matchesPasA03).extracting(TaskSequenceDefinition::getSequenceId)
                .containsExactlyInAnyOrder("seq-pas-discharge", "seq-audit-all");

        // Matching LIMS R01 -> seq3 and seq4
        List<TaskSequenceDefinition> matchesLimsR01 = sequenceService.findMatching("lims-gw", "ORU^R01");
        assertThat(matchesLimsR01).extracting(TaskSequenceDefinition::getSequenceId)
                .containsExactlyInAnyOrder("seq-lab-result", "seq-audit-all");

        // Find by Gateway
        List<TaskSequenceDefinition> pasGateways = sequenceService.findByGateway("pas-gw");
        assertThat(pasGateways).extracting(TaskSequenceDefinition::getSequenceId)
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
}
