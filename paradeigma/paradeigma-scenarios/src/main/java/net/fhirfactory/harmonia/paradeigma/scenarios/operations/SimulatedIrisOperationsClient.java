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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package net.fhirfactory.harmonia.paradeigma.scenarios.operations;

import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory deterministic simulator of the Harmonia Operations Backend REST API (iris-befe :8090).
 * Configured per IrisOperationsScenarioProfile to validate console perspectives without external dependencies.
 */
public class SimulatedIrisOperationsClient implements IrisOperationsClient {

    private final IrisOperationsScenarioProfile profile;
    private final long seed;

    private IrisOperationsModels.OpsSummaryDto summary;
    private final Map<String, IrisOperationsModels.OpsSubsystemDto> subsystems = new ConcurrentHashMap<>();
    private final Map<String, List<IrisOperationsModels.OpsInstanceDto>> instances = new ConcurrentHashMap<>();
    private final Map<String, IrisOperationsModels.OpsHealthDto> health = new ConcurrentHashMap<>();
    private final Map<String, IrisOperationsModels.OpsQueueDto> queues = new ConcurrentHashMap<>();
    private final Map<String, IrisOperationsModels.OpsWorkflowDto> workflows = new ConcurrentHashMap<>();
    private final Map<String, IrisOperationsModels.OpsPragmaDto> pragmas = new ConcurrentHashMap<>();
    private final List<IrisOperationsModels.OpsEventDto> events = Collections.synchronizedList(new ArrayList<>());
    private final Map<String, IrisOperationsModels.OpsAlertDto> alerts = new ConcurrentHashMap<>();

    public SimulatedIrisOperationsClient() {
        this(IrisOperationsScenarioProfile.ALL_HEALTHY, 42L);
    }

    public SimulatedIrisOperationsClient(IrisOperationsScenarioProfile profile, long seed) {
        this.profile = (profile != null) ? profile : IrisOperationsScenarioProfile.ALL_HEALTHY;
        this.seed = seed;
        initDeterministicState();
    }

    private void initDeterministicState() {
        long now = System.currentTimeMillis();

        // 1. Subsystems inventory (9 canonical subsystems)
        String[][] sysDefs = {
                {"pylai", "Pylai", "Inbound/outbound protocol gateways (MLLP, FHIR REST)"},
                {"petasos", "Petasos", "Resilient messaging abstraction and ActiveMQ broker integration"},
                {"energeia", "Energeia", "Task processing and workflow orchestration (Ponos, Praxis)"},
                {"mneme", "Mneme", "Infinispan distributed operational and configuration cache grid"},
                {"mnemosyne", "Mnemosyne", "Authoritative HAPI FHIR R5 persistence and clinical datastore"},
                {"calliope", "Calliope", "Canonical clinical models, converters, and message schemas"},
                {"themis", "Themis", "Default-deny policy evaluation and audit governance engine"},
                {"agora", "Agora", "Matrix and Synapse clinical collaboration and room lifecycle"},
                {"iris", "Iris", "Presentation tier, BEFE gateway, and operations console"}
        };

        boolean petasosDegraded = (profile == IrisOperationsScenarioProfile.PETASOS_DEGRADED);

        for (String[] def : sysDefs) {
            String sysId = def[0];
            String state = (petasosDegraded && "petasos".equals(sysId)) ? "DEGRADED" : "HEALTHY";

            IrisOperationsModels.OpsSubsystemDto s = new IrisOperationsModels.OpsSubsystemDto();
            s.setId(sysId);
            s.setName(def[1]);
            s.setDescription(def[2]);
            s.setState(state);
            s.setInstanceCount(2);
            s.setVersion("1.0.0-SNAPSHOT");
            s.setLastUpdated(now - 15000);

            if ("energeia".equals(sysId)) {
                IrisOperationsModels.OpsSubsystemDto ponos = new IrisOperationsModels.OpsSubsystemDto();
                ponos.setId("ponos");
                ponos.setName("Ponos");
                ponos.setDescription("Activity execution unit");
                ponos.setState("HEALTHY");
                ponos.setInstanceCount(2);
                ponos.setVersion("1.0.0-SNAPSHOT");
                ponos.setLastUpdated(now - 15000);

                IrisOperationsModels.OpsSubsystemDto praxis = new IrisOperationsModels.OpsSubsystemDto();
                praxis.setId("praxis");
                praxis.setName("Praxis");
                praxis.setDescription("Workflow sequence orchestrator");
                praxis.setState("HEALTHY");
                praxis.setInstanceCount(2);
                praxis.setVersion("1.0.0-SNAPSHOT");
                praxis.setLastUpdated(now - 15000);

                s.setChildren(List.of(ponos, praxis));
            }

            subsystems.put(sysId, s);

            // Instances
            List<IrisOperationsModels.OpsInstanceDto> instList = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                IrisOperationsModels.OpsInstanceDto inst = new IrisOperationsModels.OpsInstanceDto();
                inst.setInstanceId(sysId + "-" + i);
                inst.setSubsystemId(sysId);
                inst.setRole(i == 0 ? "PRIMARY" : "WORKER");
                inst.setState("RUNNING");
                inst.setReady(true);
                inst.setRestartCount(0);
                inst.setUptime("3d 12h");
                inst.setStartedAt(now - 302400000L);
                inst.setCpuPercent(12.5 + i);
                inst.setMemoryMb(512 + i * 64);
                inst.setPodName(sysId + "-deployment-" + i);
                inst.setNamespace("harmonia");
                inst.setNodeName("microk8s-node-01");
                inst.setIpAddress("10.1.0." + (10 + instList.size()));
                inst.setContainerImage("harmonia/" + sysId + ":1.0.0-SNAPSHOT");
                inst.setAppVersion("1.0.0-SNAPSHOT");
                instList.add(inst);
            }
            instances.put(sysId, instList);

            // Health & Dependencies
            IrisOperationsModels.OpsHealthDto h = new IrisOperationsModels.OpsHealthDto();
            h.setSubsystemId(sysId);
            h.setStatus(state);
            h.setAvailabilityPercent(petasosDegraded && "petasos".equals(sysId) ? 96.5 : 99.98);
            h.setFailedOperations(petasosDegraded && "petasos".equals(sysId) ? 14 : 0);
            h.setRestartCount(0);
            h.setP95LatencyMs(petasosDegraded && "petasos".equals(sysId) ? 485 : 18);
            h.setDependenciesSummary("2 / 2 Healthy");

            IrisOperationsModels.OpsDependencyHealthDto dep1 = new IrisOperationsModels.OpsDependencyHealthDto();
            dep1.setName("mneme");
            dep1.setStatus("HEALTHY");
            dep1.setLatencyMs(4);
            dep1.setMessage("Hot Rod cache ping OK");

            IrisOperationsModels.OpsDependencyHealthDto dep2 = new IrisOperationsModels.OpsDependencyHealthDto();
            dep2.setName("themis");
            dep2.setStatus("HEALTHY");
            dep2.setLatencyMs(6);
            dep2.setMessage("Policy engine reachable");

            h.setDependencies(List.of(dep1, dep2));
            health.put(sysId, h);
        }

        // 2. Summary
        boolean hasDegraded = (profile == IrisOperationsScenarioProfile.PETASOS_DEGRADED || profile == IrisOperationsScenarioProfile.DLQ_GROWTH || profile == IrisOperationsScenarioProfile.FAILED_PRAGMA || profile == IrisOperationsScenarioProfile.CRITICAL_ALERT);
        summary = new IrisOperationsModels.OpsSummaryDto();
        summary.setPlatformStatus(hasDegraded ? "DEGRADED" : "HEALTHY");
        summary.setEnvironment("PROD / microk8s-01");
        summary.setCluster("harmonia-cluster-01");
        summary.setTimestamp(now);
        summary.setTotalSubsystems(9);
        summary.setDegradedSubsystems(petasosDegraded ? 1 : 0);
        summary.setCriticalAlerts(profile == IrisOperationsScenarioProfile.CRITICAL_ALERT ? 1 : 0);
        summary.setWarningAlerts((petasosDegraded || profile == IrisOperationsScenarioProfile.DLQ_GROWTH || profile == IrisOperationsScenarioProfile.FAILED_PRAGMA) ? 1 : 0);
        summary.setLastRefreshed(now);

        // 3. Queues (Petasos)
        boolean hasDlq = (profile == IrisOperationsScenarioProfile.DLQ_GROWTH);
        IrisOperationsModels.OpsQueueDto qInbound = new IrisOperationsModels.OpsQueueDto();
        qInbound.setQueueId("q-mllp-in");
        qInbound.setQueueName("harmonia.mllp.inbound.v2");
        qInbound.setAddress("harmonia.mllp.inbound.v2");
        qInbound.setStatus(hasDlq ? "DEGRADED" : "HEALTHY");
        qInbound.setDepth(hasDlq ? 88 : 4);
        qInbound.setConsumerCount(3);
        qInbound.setProducerCount(2);
        qInbound.setEnqueueRate(14.2);
        qInbound.setDequeueRate(14.1);
        qInbound.setOldestMessageAgeSeconds(hasDlq ? 180 : 2);
        qInbound.setRedeliveryCount(hasDlq ? 45 : 0);
        qInbound.setDlqDepth(hasDlq ? 24 : 0);
        qInbound.setExpiryCount(0);
        qInbound.setAssociatedCapability("MLLP Ingress");
        queues.put(qInbound.getQueueId(), qInbound);

        IrisOperationsModels.OpsQueueDto qDlq = new IrisOperationsModels.OpsQueueDto();
        qDlq.setQueueId("q-mllp-dlq");
        qDlq.setQueueName("harmonia.mllp.inbound.v2.DLQ");
        qDlq.setAddress("harmonia.mllp.inbound.v2.DLQ");
        qDlq.setStatus(hasDlq ? "UNHEALTHY" : "HEALTHY");
        qDlq.setDepth(hasDlq ? 24 : 0);
        qDlq.setConsumerCount(0);
        qDlq.setProducerCount(1);
        qDlq.setEnqueueRate(hasDlq ? 1.2 : 0.0);
        qDlq.setDequeueRate(0.0);
        qDlq.setOldestMessageAgeSeconds(hasDlq ? 420 : 0);
        qDlq.setRedeliveryCount(hasDlq ? 45 : 0);
        qDlq.setDlqDepth(hasDlq ? 24 : 0);
        qDlq.setExpiryCount(0);
        qDlq.setAssociatedCapability("Dead Letter Storage");
        queues.put(qDlq.getQueueId(), qDlq);

        // 4. Workflows & Pragmas
        boolean pragmaFailed = (profile == IrisOperationsScenarioProfile.FAILED_PRAGMA);
        IrisOperationsModels.OpsWorkflowDto wfAdt = new IrisOperationsModels.OpsWorkflowDto();
        wfAdt.setWorkflowId("seq-mllp-inbound-adt");
        wfAdt.setName("MLLP ADT Inbound Pipeline");
        wfAdt.setDescription("Parses HL7 v2 ADT, converts to FHIR Bundle, validates and commits");
        wfAdt.setActiveExecutions(pragmaFailed ? 0 : 2);
        wfAdt.setQueuedWork(0);
        wfAdt.setCompletedWork(280);
        wfAdt.setFailedWork(pragmaFailed ? 1 : 0);
        wfAdt.setRetryingWork(0);
        wfAdt.setProcessingRate(8.4);
        wfAdt.setP95DurationMs(120);
        wfAdt.setFailureRate(pragmaFailed ? 0.35 : 0.0);
        workflows.put(wfAdt.getWorkflowId(), wfAdt);

        IrisOperationsModels.OpsPragmaDto pragma = new IrisOperationsModels.OpsPragmaDto();
        pragma.setPragmaId("pragma-adt-01");
        pragma.setPraxisId("seq-mllp-inbound-adt");
        pragma.setStatus(pragmaFailed ? "FAILED" : "COMPLETED");
        pragma.setStartedAt(now - 60000);
        pragma.setDurationMs(pragmaFailed ? 310 : 85);
        pragma.setCurrentErgon(pragmaFailed ? "FhirValidationErgon" : "FhirCommitErgon");
        pragma.setCompletedErgaCount(pragmaFailed ? 2 : 4);
        pragma.setRetryCount(pragmaFailed ? 3 : 0);
        pragma.setCorrelationId("corr-scenario-" + seed);
        pragma.setCausationId("caus-scenario-" + seed);
        pragma.setFailureReasonCode(pragmaFailed ? "ERR-FHIR-VALIDATION-FAILED" : null);

        List<IrisOperationsModels.OpsErgonCheckpointDto> checkpoints = new ArrayList<>();
        checkpoints.add(new IrisOperationsModels.OpsErgonCheckpointDto("ergon-mllp-in", "MllpParserErgon", "SUCCESS", now - 60000, 15, null));
        checkpoints.add(new IrisOperationsModels.OpsErgonCheckpointDto("ergon-hl7-conv", "Hl7FhirConverterErgon", "SUCCESS", now - 59985, 40, null));
        if (pragmaFailed) {
            checkpoints.add(new IrisOperationsModels.OpsErgonCheckpointDto("ergon-fhir-val", "FhirValidationErgon", "FAILED", now - 59945, 255, "Validation failed: Missing mandatory Patient.identifier"));
        } else {
            checkpoints.add(new IrisOperationsModels.OpsErgonCheckpointDto("ergon-fhir-val", "FhirValidationErgon", "SUCCESS", now - 59945, 20, null));
            checkpoints.add(new IrisOperationsModels.OpsErgonCheckpointDto("ergon-fhir-com", "FhirCommitErgon", "SUCCESS", now - 59925, 10, null));
        }
        pragma.setCheckpoints(checkpoints);
        pragmas.put(pragma.getPragmaId(), pragma);

        // 5. Events timeline for correlation
        String corrId = "corr-scenario-" + seed;
        IrisOperationsModels.OpsEventDto ev1 = new IrisOperationsModels.OpsEventDto();
        ev1.setEventId("ev-1-" + seed);
        ev1.setTimestamp(now - 60000);
        ev1.setSubsystem("pylai");
        ev1.setEventType("MLLP_PACKET_RECEIVED");
        ev1.setOperation("Ingest HL7 ADT A01 from PAS");
        ev1.setStatus("SUCCESS");
        ev1.setDurationMs(12);
        ev1.setCorrelationId(corrId);
        ev1.setPragmaId(pragma.getPragmaId());
        events.add(ev1);

        IrisOperationsModels.OpsEventDto ev2 = new IrisOperationsModels.OpsEventDto();
        ev2.setEventId("ev-2-" + seed);
        ev2.setTimestamp(now - 59980);
        ev2.setSubsystem("petasos");
        ev2.setEventType("QUEUE_ENQUEUED");
        ev2.setOperation("Dispatch message to harmonia.mllp.inbound.v2");
        ev2.setStatus("SUCCESS");
        ev2.setDurationMs(8);
        ev2.setCorrelationId(corrId);
        ev2.setPragmaId(pragma.getPragmaId());
        events.add(ev2);

        IrisOperationsModels.OpsEventDto ev3 = new IrisOperationsModels.OpsEventDto();
        ev3.setEventId("ev-3-" + seed);
        ev3.setTimestamp(now - 59940);
        ev3.setSubsystem("energeia");
        ev3.setEventType("PRAGMA_CHECKPOINT");
        ev3.setOperation("Execute validation in FhirValidationErgon");
        ev3.setStatus(pragmaFailed ? "FAILURE" : "SUCCESS");
        ev3.setDurationMs(pragmaFailed ? 255 : 20);
        ev3.setCorrelationId(corrId);
        ev3.setPragmaId(pragma.getPragmaId());
        ev3.setReasonCode(pragmaFailed ? "ERR-FHIR-VALIDATION-FAILED" : null);
        events.add(ev3);

        if (!pragmaFailed) {
            IrisOperationsModels.OpsEventDto ev4 = new IrisOperationsModels.OpsEventDto();
            ev4.setEventId("ev-4-" + seed);
            ev4.setTimestamp(now - 59910);
            ev4.setSubsystem("mnemosyne");
            ev4.setEventType("FHIR_RESOURCE_COMMITTED");
            ev4.setOperation("Commit Patient and Encounter resources to PostgreSQL");
            ev4.setStatus("SUCCESS");
            ev4.setDurationMs(18);
            ev4.setCorrelationId(corrId);
            ev4.setPragmaId(pragma.getPragmaId());
            events.add(ev4);
        }

        // 6. Alerts
        if (profile == IrisOperationsScenarioProfile.CRITICAL_ALERT) {
            IrisOperationsModels.OpsAlertDto critAlert = new IrisOperationsModels.OpsAlertDto();
            critAlert.setAlertId("alert-crit-heap-" + seed);
            critAlert.setSeverity("CRITICAL");
            critAlert.setSubsystem("petasos");
            critAlert.setComponent("ActiveMQ-Artemis-Broker-0");
            critAlert.setCondition("ActiveMQ Artemis broker JVM heap utilization at 94%");
            critAlert.setFirstObserved(now - 180000);
            critAlert.setLastObserved(now - 5000);
            critAlert.setDuration("2m 55s");
            critAlert.setStatus("ACTIVE");
            critAlert.setRelatedResource("petasos-broker-0");
            critAlert.setOperatorGuidance("Drain non-critical queues and trigger emergency broker garbage collection or pod restart.");
            alerts.put(critAlert.getAlertId(), critAlert);
        } else if (petasosDegraded) {
            IrisOperationsModels.OpsAlertDto warnAlert = new IrisOperationsModels.OpsAlertDto();
            warnAlert.setAlertId("alert-warn-lat-" + seed);
            warnAlert.setSeverity("WARNING");
            warnAlert.setSubsystem("petasos");
            warnAlert.setComponent("MessagingLatencyProbe");
            warnAlert.setCondition("Petasos message broker P95 latency exceeds 400ms threshold");
            warnAlert.setFirstObserved(now - 240000);
            warnAlert.setLastObserved(now - 10000);
            warnAlert.setDuration("3m 50s");
            warnAlert.setStatus("ACTIVE");
            warnAlert.setRelatedResource("petasos");
            warnAlert.setOperatorGuidance("Inspect ActiveMQ broker thread pool and network interface saturation.");
            alerts.put(warnAlert.getAlertId(), warnAlert);
        } else if (hasDlq) {
            IrisOperationsModels.OpsAlertDto dlqAlert = new IrisOperationsModels.OpsAlertDto();
            dlqAlert.setAlertId("alert-warn-dlq-" + seed);
            dlqAlert.setSeverity("WARNING");
            dlqAlert.setSubsystem("petasos");
            dlqAlert.setComponent("DeadLetterQueueMonitor");
            dlqAlert.setCondition("Dead-letter queue accumulation on harmonia.mllp.inbound.v2.DLQ");
            dlqAlert.setFirstObserved(now - 120000);
            dlqAlert.setLastObserved(now - 15000);
            dlqAlert.setDuration("1m 45s");
            dlqAlert.setStatus("ACTIVE");
            dlqAlert.setRelatedResource("harmonia.mllp.inbound.v2.DLQ");
            dlqAlert.setOperatorGuidance("Review malformed FHIR payloads and trigger DLQ redelivery ergon after schema fix.");
            alerts.put(dlqAlert.getAlertId(), dlqAlert);
        } else if (pragmaFailed) {
            IrisOperationsModels.OpsAlertDto wfAlert = new IrisOperationsModels.OpsAlertDto();
            wfAlert.setAlertId("alert-warn-wf-" + seed);
            wfAlert.setSeverity("WARNING");
            wfAlert.setSubsystem("energeia");
            wfAlert.setComponent("PraxisExecutionEngine");
            wfAlert.setCondition("Workflow task stalled: ERR-FHIR-VALIDATION-FAILED on pragma-adt-01");
            wfAlert.setFirstObserved(now - 60000);
            wfAlert.setLastObserved(now - 10000);
            wfAlert.setDuration("50s");
            wfAlert.setStatus("ACTIVE");
            wfAlert.setRelatedResource("pragma-adt-01");
            wfAlert.setOperatorGuidance("Verify inbound HL7 ADT message schema and correct missing Patient.identifier values.");
            alerts.put(wfAlert.getAlertId(), wfAlert);
        }
    }

    private void validateSecurity(ThemisSecurityContext context) {
        if (context == null || context.requestingPrincipal() == null) {
            throw new SecurityException("Themis default-deny: missing security context or requesting principal");
        }

        Map<String, String> attrs = context.attributes();
        if (attrs == null) {
            throw new SecurityException("Themis default-deny: context has no attributes");
        }

        String authStr = String.valueOf(attrs.getOrDefault("authorities", ""));
        String roleStr = String.valueOf(attrs.getOrDefault("roleCodes", ""));

        boolean authorized = authStr.contains("operations.viewer")
                || authStr.contains("operations.admin")
                || authStr.contains("system.admin")
                || authStr.contains("system.integration")
                || authStr.contains("*")
                || roleStr.contains("SYS_ADM")
                || roleStr.contains("SYS_INT")
                || roleStr.contains("OPS_VIEW");

        if (!authorized) {
            throw new SecurityException("Themis default-deny: caller lacks required operations authority");
        }
    }

    @Override
    public IrisOperationsModels.OpsSummaryDto getSummary(ThemisSecurityContext context) {
        validateSecurity(context);
        return summary;
    }

    @Override
    public List<IrisOperationsModels.OpsSubsystemDto> getSubsystems(ThemisSecurityContext context) {
        validateSecurity(context);
        return new ArrayList<>(subsystems.values());
    }

    @Override
    public IrisOperationsModels.OpsSubsystemDto getSubsystem(String id, ThemisSecurityContext context) {
        validateSecurity(context);
        return subsystems.get(id);
    }

    @Override
    public List<IrisOperationsModels.OpsInstanceDto> getSubsystemInstances(String id, ThemisSecurityContext context) {
        validateSecurity(context);
        return instances.getOrDefault(id, Collections.emptyList());
    }

    @Override
    public IrisOperationsModels.OpsHealthDto getSubsystemHealth(String id, ThemisSecurityContext context) {
        validateSecurity(context);
        return health.get(id);
    }

    @Override
    public List<IrisOperationsModels.OpsQueueDto> getQueues(ThemisSecurityContext context) {
        validateSecurity(context);
        return new ArrayList<>(queues.values());
    }

    @Override
    public IrisOperationsModels.OpsQueueDto getQueue(String id, ThemisSecurityContext context) {
        validateSecurity(context);
        return queues.get(id);
    }

    @Override
    public List<IrisOperationsModels.OpsWorkflowDto> getWorkflows(ThemisSecurityContext context) {
        validateSecurity(context);
        return new ArrayList<>(workflows.values());
    }

    @Override
    public IrisOperationsModels.OpsWorkflowDto getWorkflow(String id, ThemisSecurityContext context) {
        validateSecurity(context);
        return workflows.get(id);
    }

    @Override
    public IrisOperationsModels.OpsPragmaDto getPragma(String id, ThemisSecurityContext context) {
        validateSecurity(context);
        return pragmas.get(id);
    }

    @Override
    public List<IrisOperationsModels.OpsEventDto> getEvents(Map<String, String> filters, ThemisSecurityContext context) {
        validateSecurity(context);
        String corrId = (filters != null) ? filters.get("correlationId") : null;
        String sys = (filters != null) ? filters.get("subsystem") : null;
        String status = (filters != null) ? filters.get("status") : null;

        return events.stream()
                .filter(e -> corrId == null || corrId.equalsIgnoreCase(e.getCorrelationId()))
                .filter(e -> sys == null || sys.equalsIgnoreCase(e.getSubsystem()))
                .filter(e -> status == null || status.equalsIgnoreCase(e.getStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public IrisOperationsModels.OpsEventDto getEvent(String id, ThemisSecurityContext context) {
        validateSecurity(context);
        return events.stream()
                .filter(e -> e.getEventId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<IrisOperationsModels.OpsAlertDto> getAlerts(Map<String, String> filters, ThemisSecurityContext context) {
        validateSecurity(context);
        String sev = (filters != null) ? filters.get("severity") : null;
        String stat = (filters != null) ? filters.get("status") : null;

        return alerts.values().stream()
                .filter(a -> sev == null || "ALL".equalsIgnoreCase(sev) || sev.equalsIgnoreCase(a.getSeverity()))
                .filter(a -> stat == null || "ALL".equalsIgnoreCase(stat) || stat.equalsIgnoreCase(a.getStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public IrisOperationsModels.OpsAlertDto acknowledgeAlert(String alertId, String operator, ThemisSecurityContext context) {
        validateSecurity(context);
        IrisOperationsModels.OpsAlertDto alert = alerts.get(alertId);
        if (alert != null) {
            alert.setStatus("ACKNOWLEDGED");
            return alert;
        }
        return null;
    }
}
