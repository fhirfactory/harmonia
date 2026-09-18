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

package net.fhirfactory.harmonia.befe;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.fhirfactory.harmonia.befe.model.operations.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class OperationalModelSerializationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("1. OperationalSummary serializes and deserializes cleanly")
    void testOperationalSummarySerialization() throws Exception {
        OperationalSummary summary = new OperationalSummary(
                "HEALTHY",
                "PROD / microk8s-01",
                "harmonia-cluster-01",
                System.currentTimeMillis(),
                9,
                0,
                0,
                2,
                System.currentTimeMillis()
        );

        String json = objectMapper.writeValueAsString(summary);
        assertThat(json).contains("HEALTHY").contains("microk8s-01");

        OperationalSummary deserialized = objectMapper.readValue(json, OperationalSummary.class);
        assertThat(deserialized.getPlatformStatus()).isEqualTo("HEALTHY");
        assertThat(deserialized.getEnvironment()).isEqualTo("PROD / microk8s-01");
        assertThat(deserialized.getTotalSubsystems()).isEqualTo(9);
        assertThat(deserialized.getWarningAlerts()).isEqualTo(2);
    }

    @Test
    @DisplayName("2. OperationalSubsystem with children serializes and deserializes")
    void testOperationalSubsystemSerialization() throws Exception {
        OperationalSubsystem child = new OperationalSubsystem(
                "ponos", "Ponos WorkEngine", "Task processing engine", "HEALTHY",
                1, "1.0.0-SNAPSHOT", System.currentTimeMillis()
        );
        OperationalSubsystem parent = new OperationalSubsystem(
                "energeia", "Energeia", "Workflow services", "HEALTHY",
                2, "1.0.0-SNAPSHOT", System.currentTimeMillis(),
                List.of(child)
        );

        String json = objectMapper.writeValueAsString(parent);
        assertThat(json).contains("energeia").contains("ponos");

        OperationalSubsystem deserialized = objectMapper.readValue(json, OperationalSubsystem.class);
        assertThat(deserialized.getId()).isEqualTo("energeia");
        assertThat(deserialized.getChildren()).hasSize(1);
        assertThat(deserialized.getChildren().get(0).getId()).isEqualTo("ponos");
    }

    @Test
    @DisplayName("3. OperationalInstance serializes safe operational telemetry without PHI")
    void testOperationalInstanceSerialization() throws Exception {
        OperationalInstance instance = new OperationalInstance(
                "petasos-0", "petasos", "Primary", "Running", true,
                0, "3d 4h", System.currentTimeMillis() - 270000000L,
                14.5, 768
        );
        instance.setPodName("petasos-0");
        instance.setNamespace("harmonia");
        instance.setNodeName("microk8s-worker-1");
        instance.setIpAddress("10.1.0.45");
        instance.setContainerImage("harmonia/petasos:1.0.0-SNAPSHOT");
        instance.setAppVersion("1.0.0-SNAPSHOT");
        instance.setRecentErrors(List.of("Transient connection timeout to broker, recovered"));
        instance.setDependencies(List.of("Artemis", "Calliope"));

        String json = objectMapper.writeValueAsString(instance);
        assertThat(json).contains("petasos-0").contains("10.1.0.45");

        OperationalInstance deserialized = objectMapper.readValue(json, OperationalInstance.class);
        assertThat(deserialized.getInstanceId()).isEqualTo("petasos-0");
        assertThat(deserialized.getMemoryMb()).isEqualTo(768);
        assertThat(deserialized.getCpuPercent()).isEqualTo(14.5);
        assertThat(deserialized.getRecentErrors()).hasSize(1);
        assertThat(deserialized.getDependencies()).containsExactly("Artemis", "Calliope");
    }

    @Test
    @DisplayName("4. OperationalHealth and DependencyHealth serialize correctly")
    void testOperationalHealthSerialization() throws Exception {
        DependencyHealth dep = new DependencyHealth("Petasos", "HEALTHY", 4, "Topic published");
        OperationalHealth health = new OperationalHealth(
                "pylai", "HEALTHY", 99.98, 0, 0, 12, "1 / 1 Healthy"
        );
        health.setDependencies(List.of(dep));
        health.getDetails().put("inboundCount", 1200);

        String json = objectMapper.writeValueAsString(health);
        assertThat(json).contains("pylai").contains("1200");

        OperationalHealth deserialized = objectMapper.readValue(json, OperationalHealth.class);
        assertThat(deserialized.getSubsystemId()).isEqualTo("pylai");
        assertThat(deserialized.getDependencies()).hasSize(1);
        assertThat(deserialized.getDependencies().get(0).getName()).isEqualTo("Petasos");
        assertThat(deserialized.getDetails()).containsEntry("inboundCount", 1200);
    }

    @Test
    @DisplayName("5. QueueSummary serializes depth history and zero PHI")
    void testQueueSummarySerialization() throws Exception {
        QueueSummary queue = new QueueSummary(
                "queue.inbound.adt", "pylai.mllp.inbound.adt", "pylai.mllp.inbound.adt",
                "HEALTHY", 12, 4, 2, 45.2, 44.8, 2, 0, 0, 0, "Ingress"
        );
        queue.getDepthHistory().add(new TimeSeriesPoint(System.currentTimeMillis() - 60000, 10.0));
        queue.getDepthHistory().add(new TimeSeriesPoint(System.currentTimeMillis(), 12.0));

        String json = objectMapper.writeValueAsString(queue);
        assertThat(json).contains("queue.inbound.adt").contains("depthHistory");

        QueueSummary deserialized = objectMapper.readValue(json, QueueSummary.class);
        assertThat(deserialized.getQueueId()).isEqualTo("queue.inbound.adt");
        assertThat(deserialized.getDepth()).isEqualTo(12);
        assertThat(deserialized.getDepthHistory()).hasSize(2);
    }

    @Test
    @DisplayName("6. WorkflowSummary, PragmaSummary, and ErgonCheckpoint serialize properly")
    void testWorkflowAndPragmaSerialization() throws Exception {
        WorkflowSummary workflow = new WorkflowSummary(
                "praxis.adt.distribution", "ADT Distribution", "Distributes ADT events to EHR and LMS",
                3, 0, 1420, 1, 0, 24.5, 32, 0.07
        );
        String wfJson = objectMapper.writeValueAsString(workflow);
        WorkflowSummary deserializedWf = objectMapper.readValue(wfJson, WorkflowSummary.class);
        assertThat(deserializedWf.getWorkflowId()).isEqualTo("praxis.adt.distribution");

        ErgonCheckpoint checkpoint = new ErgonCheckpoint(
                "ergon.validate", "Validate FHIR Bundle", "COMPLETED",
                System.currentTimeMillis() - 50, System.currentTimeMillis(), 50, null
        );
        PragmaSummary pragma = new PragmaSummary(
                "pragma-12345", "praxis.adt.distribution", "COMPLETED",
                System.currentTimeMillis() - 120, 120, "ergon.dispatch", 4, 0,
                "corr-999", "caus-888", null
        );
        pragma.getCheckpoints().add(checkpoint);

        String pragmaJson = objectMapper.writeValueAsString(pragma);
        PragmaSummary deserializedPragma = objectMapper.readValue(pragmaJson, PragmaSummary.class);
        assertThat(deserializedPragma.getPragmaId()).isEqualTo("pragma-12345");
        assertThat(deserializedPragma.getCheckpoints()).hasSize(1);
        assertThat(deserializedPragma.getCheckpoints().get(0).getErgonName()).isEqualTo("Validate FHIR Bundle");
    }

    @Test
    @DisplayName("7. OperationalEvent and OperationalAlert serialize cleanly")
    void testOperationalEventAndAlertSerialization() throws Exception {
        OperationalEvent event = new OperationalEvent(
                "evt-001", System.currentTimeMillis(), "pylai", "MLLP_INGRESS", "RECEIVE_ADT_A01",
                "SUCCESS", 15, "msg-001", "corr-100", "caus-100", "pragma-100", "praxis.adt",
                "ergon-01", "mllp:2575", null
        );
        String evtJson = objectMapper.writeValueAsString(event);
        OperationalEvent deserializedEvt = objectMapper.readValue(evtJson, OperationalEvent.class);
        assertThat(deserializedEvt.getCorrelationId()).isEqualTo("corr-100");

        OperationalAlert alert = new OperationalAlert(
                "alert-001", "CRITICAL", "petasos", "queue.deadletter",
                "Dead letter queue count exceeded threshold (> 0)",
                System.currentTimeMillis() - 600000, System.currentTimeMillis(),
                "10m", "ACTIVE", "petasos.dlq", "corr-dlq",
                "Check consumer error logs and replay failed messages using ops CLI"
        );
        String alertJson = objectMapper.writeValueAsString(alert);
        OperationalAlert deserializedAlert = objectMapper.readValue(alertJson, OperationalAlert.class);
        assertThat(deserializedAlert.getSeverity()).isEqualTo("CRITICAL");
        assertThat(deserializedAlert.getOperatorGuidance()).contains("Check consumer error logs");
    }

    @Test
    @DisplayName("8. TimeSeries calculates summary min, max, avg, current")
    void testTimeSeriesCalculations() throws Exception {
        TimeSeries ts = new TimeSeries("CPU Utilization", "15m", "%");
        ts.setPoints(List.of(
                new TimeSeriesPoint(1000L, 10.0),
                new TimeSeriesPoint(2000L, 30.0),
                new TimeSeriesPoint(3000L, 20.0)
        ));

        assertThat(ts.getSummary()).containsEntry("min", 10.0);
        assertThat(ts.getSummary()).containsEntry("max", 30.0);
        assertThat(ts.getSummary()).containsEntry("avg", 20.0);
        assertThat(ts.getSummary()).containsEntry("current", 20.0);

        String json = objectMapper.writeValueAsString(ts);
        TimeSeries deserialized = objectMapper.readValue(json, TimeSeries.class);
        assertThat(deserialized.getPoints()).hasSize(3);
        assertThat(deserialized.getSummary().get("max")).isEqualTo(30.0);
    }
}
