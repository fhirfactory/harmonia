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

package net.fhirfactory.hie.operationscli.formatter;

import net.fhirfactory.hie.operationscli.model.OperationResourceDto;
import net.fhirfactory.hie.operationscli.model.TaskSequenceDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OutputFormatterTest {

    private final OutputFormatter formatter = new OutputFormatter();

    @Test
    @DisplayName("Format empty TaskSequence list")
    void testFormatEmptySequences() {
        String result = formatter.formatTaskSequences(List.of(), "table", false);
        assertThat(result).isEqualTo("No TaskSequences found.");
    }

    @Test
    @DisplayName("Format TaskSequence list in table format")
    void testFormatSequencesTable() {
        TaskSequenceDto seq1 = new TaskSequenceDto("seq-admission-pipeline", "Admission Task Sequence");
        seq1.setEnabled(true);
        seq1.setTargetGatewayInstances(List.of("*"));
        seq1.setTargetTriggerTypes(List.of("A01", "A04"));
        seq1.setActivityIds(List.of("patient-identity-update", "patient-demographics-update"));

        String table = formatter.formatTaskSequences(List.of(seq1), "table", false);
        assertThat(table).contains("Found 1 TaskSequence(s):");
        assertThat(table).contains("seq-admission-pipeline");
        assertThat(table).contains("Admission Task Sequence");
        assertThat(table).contains("ENABLED");
        assertThat(table).contains("patient-identity-update -> patient-demographics-update");
    }

    @Test
    @DisplayName("Format TaskSequence list in JSON format")
    void testFormatSequencesJson() {
        TaskSequenceDto seq = new TaskSequenceDto("seq-admission-pipeline", "Admission Task Sequence");
        seq.setEnabled(true);

        String json = formatter.formatTaskSequences(List.of(seq), "json", true);
        assertThat(json).contains("\"sequenceId\" : \"seq-admission-pipeline\"");
        assertThat(json).contains("\"sequenceName\" : \"Admission Task Sequence\"");
    }

    @Test
    @DisplayName("Format TaskSequence detail view")
    void testFormatSequenceDetail() {
        TaskSequenceDto seq = new TaskSequenceDto("seq-orders", "Orders Sequence");
        seq.setVersion("1.0.0");
        seq.setSequenceDescription("Processes clinical orders");
        seq.setTargetGatewayInstances(List.of("gw-1"));
        seq.setTargetTriggerTypes(List.of("ORM^O01"));
        seq.setActivityIds(List.of("order-processor"));

        String detail = formatter.formatTaskSequenceDetail(seq, "table", false);
        assertThat(detail).contains("Task Sequence: Orders Sequence");
        assertThat(detail).contains("Sequence ID:      seq-orders");
        assertThat(detail).contains("Description:      Processes clinical orders");
        assertThat(detail).contains("Target Gateways:  gw-1");
        assertThat(detail).contains("Target Triggers:  ORM^O01");
        assertThat(detail).contains("1. order-processor");
    }

    @Test
    @DisplayName("Format OperationResource list in table format")
    void testFormatOperationResourcesTable() {
        OperationResourceDto res = new OperationResourceDto("tasksequence", "seq-1", "{\"id\":\"seq-1\"}");
        res.setId(101L);
        res.setVersionId(2L);
        res.setLastUpdated(Instant.parse("2026-09-09T08:00:00Z"));

        String table = formatter.formatOperationResources(List.of(res), "table", false);
        assertThat(table).contains("Found 1 Operational Resource(s):");
        assertThat(table).contains("101");
        assertThat(table).contains("tasksequence");
        assertThat(table).contains("seq-1");
    }

    @Test
    @DisplayName("Format JSON string with pretty printing")
    void testFormatJsonString() {
        String raw = "{\"key\":\"value\",\"num\":123}";
        String pretty = formatter.formatJsonString(raw, true);
        assertThat(pretty).contains("\n");
        assertThat(pretty).contains("\"key\" : \"value\"");
    }
}
