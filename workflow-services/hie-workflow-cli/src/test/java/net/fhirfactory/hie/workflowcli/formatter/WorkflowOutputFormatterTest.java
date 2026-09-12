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

package net.fhirfactory.hie.workflowcli.formatter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class WorkflowOutputFormatterTest {

    private final WorkflowOutputFormatter formatter = new WorkflowOutputFormatter();

    @Test
    @DisplayName("formatReloadResponse formats text table summary")
    void testFormatReloadResponseTable() {
        Map<String, Object> response = Map.of(
                "status", "SYNCHRONIZED",
                "timestamp", "2026-09-10T12:00:00Z",
                "brokerRunning", true,
                "synchronizedQueuesCount", 2,
                "synchronizedQueues", List.of("task.event.queue.pas-gw", "task.processing.queue"),
                "sequencesReloaded", true,
                "activeSequencesCount", 1,
                "activeSequenceIds", List.of("seq-patient-identity-pipeline")
        );

        String text = formatter.formatReloadResponse(response, "table", false);
        assertThat(text).contains("HIE Workflow Reload & Synchronization Report");
        assertThat(text).contains("Status:                   SYNCHRONIZED");
        assertThat(text).contains("task.event.queue.pas-gw");
        assertThat(text).contains("seq-patient-identity-pipeline");
    }

    @Test
    @DisplayName("formatValidationReport formats text validation breakdown")
    void testFormatValidationReportTable() {
        Map<String, Object> report = Map.of(
                "status", "VALID",
                "timestamp", "2026-09-10T12:00:00Z",
                "queueValidation", Map.of(
                        "totalQueues", 1,
                        "validQueues", 1,
                        "invalidQueues", 0,
                        "queues", List.of(Map.of("queueName", "task.event.queue", "valid", true, "routingType", "ANYCAST", "durable", false))
                ),
                "sequenceValidation", Map.of(
                        "totalSequences", 1,
                        "validSequences", 1,
                        "invalidSequences", 0,
                        "sequences", List.of(Map.of("sequenceId", "seq-patient-identity-pipeline", "sequenceName", "Patient Identity", "valid", true))
                )
        );

        String text = formatter.formatValidationReport(report, "table", false);
        assertThat(text).contains("HIE Workflow Configuration Validation Report");
        assertThat(text).contains("Overall Validation:       VALID");
        assertThat(text).contains("[OK] task.event.queue");
        assertThat(text).contains("[OK] seq-patient-identity-pipeline");
    }

    @Test
    @DisplayName("formatStatus formats processor status")
    void testFormatStatus() {
        Map<String, Object> status = Map.of(
                "module", "task-sequence-processor",
                "brokerRunning", true,
                "camelStarted", true,
                "activeSequencesCount", 1,
                "activeSequences", List.of(Map.of("sequenceId", "seq-1", "sequenceName", "Seq 1", "activityCount", 2, "enabled", true))
        );

        String text = formatter.formatStatus(status, "table", false);
        assertThat(text).contains("HIE Workflow Processor Runtime Status");
        assertThat(text).contains("Artemis Broker Running:   true");
        assertThat(text).contains("seq-1");
    }
}
