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

import net.fhirfactory.harmonia.paradeigma.common.security.SecurityScenarioContext;
import net.fhirfactory.harmonia.paradeigma.scenarios.model.ParadeigmaScenario;
import net.fhirfactory.harmonia.paradeigma.scenarios.model.ScenarioExecutionStep;
import net.fhirfactory.harmonia.paradeigma.scenarios.model.ScenarioExpectation;
import net.fhirfactory.harmonia.themis.api.model.ThemisDecision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Paradeigma operational simulation scenario exercising the Harmonia Operations Console (iris-console)
 * and Operations Backend (iris-befe :8090) public REST API contracts across 5 operational perspectives:
 * 1. SUBSYSTEMS — Platform health, runtime instances, and dependency latencies.
 * 2. QUEUES — Petasos ActiveMQ queue depths, dequeue rates, and DLQ growth.
 * 3. WORKFLOWS — Praxis sequence executions and Pragma Ergon checkpoint inspections.
 * 4. EVENTS — Cross-subsystem interaction diagnostic flow tracing.
 * 5. ALERTS — Actionable operational conditions and operator acknowledgment.
 */
public class IrisOperationsScenario implements ParadeigmaScenario<IrisOperationsScenarioResult> {

    private static final Logger log = LoggerFactory.getLogger(IrisOperationsScenario.class);

    private final String scenarioId;
    private final String name;
    private final String description;
    private final long seed;
    private final SecurityScenarioContext actor;
    private final ScenarioExpectation expectation;
    private final IrisOperationsClient client;
    private final IrisOperationsScenarioProfile profile;

    public IrisOperationsScenario(
            String scenarioId,
            String name,
            String description,
            long seed,
            SecurityScenarioContext actor,
            ScenarioExpectation expectation,
            IrisOperationsClient client,
            IrisOperationsScenarioProfile profile) {
        this.scenarioId = (scenarioId != null) ? scenarioId : "scen-ops-" + UUID.randomUUID().toString().substring(0, 8);
        this.name = (name != null) ? name : "IrisOperationsScenario";
        this.description = (description != null) ? description : "Validates operational console perspectives and BEFE REST telemetry";
        this.seed = seed;
        this.actor = (actor != null) ? actor : SecurityScenarioContext.systemAdmin();
        this.expectation = (expectation != null) ? expectation : ScenarioExpectation.success();
        this.profile = (profile != null) ? profile : IrisOperationsScenarioProfile.ALL_HEALTHY;
        this.client = (client != null) ? client : new SimulatedIrisOperationsClient(this.profile, this.seed);
    }

    public static IrisOperationsScenario createDefault(long seed) {
        return new IrisOperationsScenario(null, null, null, seed, null, null, null, IrisOperationsScenarioProfile.ALL_HEALTHY);
    }

    public static IrisOperationsScenario createForProfile(IrisOperationsScenarioProfile profile, long seed) {
        return new IrisOperationsScenario(null, null, null, seed, null, null, null, profile);
    }

    @Override
    public String getScenarioId() {
        return scenarioId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public long getSeed() {
        return seed;
    }

    @Override
    public SecurityScenarioContext getActor() {
        return actor;
    }

    @Override
    public ScenarioExpectation getExpectation() {
        return expectation;
    }

    public IrisOperationsScenarioProfile getProfile() {
        return profile;
    }

    @Override
    public IrisOperationsScenarioResult execute() {
        log.info(">>> [Operations Scenario] STARTING [{}] Profile: {} Seed: {}", scenarioId, profile, seed);
        long start = System.currentTimeMillis();
        IrisOperationsScenarioResult result = new IrisOperationsScenarioResult();
        result.setScenarioId(scenarioId);
        result.setScenarioName(name);
        result.setSeed(seed);
        result.setProfile(profile);

        try {
            if (expectation.expectedSecurityDecision() == ThemisDecision.DENY) {
                executeNegativeSecurityPath(result);
                return result;
            }

            result.setSecurityDecision(ThemisDecision.ALLOW);

            // Phase 1: Platform Summary & Subsystem Inventory (SUBSYSTEMS Perspective)
            executePhase1PlatformSummary(result);

            // Phase 2: Subsystem Health & Instance Deep-Dive (SUBSYSTEMS Detail)
            executePhase2SubsystemHealth(result);

            // Phase 3: Message Queue & DLQ Surveillance (QUEUES Perspective)
            executePhase3QueuesSurveillance(result);

            // Phase 4: Workflow Execution & Pragma Checkpoint Inspection (WORKFLOWS Perspective)
            executePhase4WorkflowsInspection(result);

            // Phase 5: Cross-Subsystem Event Flow Diagnostic Tracing (EVENTS Perspective)
            executePhase5EventsDiagnosticTracing(result);

            // Phase 6: Operational Alert Governance & Remediation (ALERTS Perspective)
            executePhase6AlertsRemediation(result);

            result.setSuccess(true);
        } catch (SecurityException se) {
            log.warn("[Operations Scenario] Security exception during execution: {}", se.getMessage());
            result.setSecurityDecision(ThemisDecision.DENY);
            if (expectation.expectedSecurityDecision() == ThemisDecision.DENY) {
                result.setSuccess(true);
            } else {
                result.setSuccess(false);
                result.setErrorMessage(se.getMessage());
            }
        } catch (Exception e) {
            log.error("[Operations Scenario] Error executing scenario {}: {}", scenarioId, e.getMessage(), e);
            result.setSuccess(false);
            result.setErrorMessage(e.getMessage());
        } finally {
            result.setDurationMs(System.currentTimeMillis() - start);
            log.info("<<< [Operations Scenario] FINISHED [{}] (Success: {}, Duration: {} ms)",
                    scenarioId, result.isSuccess(), result.getDurationMs());
        }

        return result;
    }

    private void executeNegativeSecurityPath(IrisOperationsScenarioResult result) {
        ScenarioExecutionStep step = new ScenarioExecutionStep(1, "Enforce Themis Operations Security", "Themis-Engine", "DENY_CHECK");
        long start = System.currentTimeMillis();
        try {
            client.getSummary(actor.securityContext());
            step.setSuccess(false);
            step.setErrorMessage("Expected SecurityException was not thrown for unauthorized operations caller");
            result.setSuccess(false);
        } catch (SecurityException se) {
            step.setSuccess(true);
            step.setAckCode("403");
            result.setSecurityDecision(ThemisDecision.DENY);
            result.setSuccess(true);
        } catch (Exception e) {
            step.setSuccess(false);
            step.setErrorMessage(e.getMessage());
            result.setSuccess(false);
        } finally {
            step.setDurationMs(System.currentTimeMillis() - start);
            result.addStep(step);
        }
    }

    private void executePhase1PlatformSummary(IrisOperationsScenarioResult result) throws Exception {
        ScenarioExecutionStep step = new ScenarioExecutionStep(1, "Platform Summary & Inventory Query", "BEFE-Port-8090", "GET_SUMMARY");
        long start = System.currentTimeMillis();

        IrisOperationsModels.OpsSummaryDto summary = client.getSummary(actor.securityContext());
        List<IrisOperationsModels.OpsSubsystemDto> subs = client.getSubsystems(actor.securityContext());

        step.setDurationMs(System.currentTimeMillis() - start);

        if (summary != null && subs != null && !subs.isEmpty()) {
            result.setPlatformStatus(summary.getPlatformStatus());
            result.setTotalSubsystems(summary.getTotalSubsystems());
            result.setDegradedSubsystems(summary.getDegradedSubsystems());

            if (profile == IrisOperationsScenarioProfile.PETASOS_DEGRADED) {
                if (!"DEGRADED".equalsIgnoreCase(summary.getPlatformStatus())) {
                    throw new IllegalStateException("Expected platform status DEGRADED for profile " + profile);
                }
            } else if (profile == IrisOperationsScenarioProfile.ALL_HEALTHY) {
                if (!"HEALTHY".equalsIgnoreCase(summary.getPlatformStatus())) {
                    throw new IllegalStateException("Expected platform status HEALTHY for profile " + profile);
                }
            }

            step.setSuccess(true);
            step.setAckCode("200");
        } else {
            step.setSuccess(false);
            step.setErrorMessage("Platform summary or subsystem list returned empty");
        }
        result.addStep(step);
    }

    private void executePhase2SubsystemHealth(IrisOperationsScenarioResult result) throws Exception {
        ScenarioExecutionStep step = new ScenarioExecutionStep(2, "Subsystem Health & Instances Inspection", "BEFE-Port-8090", "GET_SUBSYSTEM_HEALTH");
        long start = System.currentTimeMillis();

        String targetSys = (profile == IrisOperationsScenarioProfile.PETASOS_DEGRADED) ? "petasos" : "energeia";
        IrisOperationsModels.OpsSubsystemDto sub = client.getSubsystem(targetSys, actor.securityContext());
        List<IrisOperationsModels.OpsInstanceDto> insts = client.getSubsystemInstances(targetSys, actor.securityContext());
        IrisOperationsModels.OpsHealthDto health = client.getSubsystemHealth(targetSys, actor.securityContext());

        step.setDurationMs(System.currentTimeMillis() - start);

        if (sub != null && insts != null && !insts.isEmpty() && health != null) {
            if (profile == IrisOperationsScenarioProfile.PETASOS_DEGRADED) {
                if (!"DEGRADED".equalsIgnoreCase(health.getStatus())) {
                    throw new IllegalStateException("Expected Petasos health status DEGRADED for profile " + profile);
                }
            }
            step.setSuccess(true);
            step.setAckCode("200");
        } else {
            step.setSuccess(false);
            step.setErrorMessage("Subsystem health or runtime instances missing for " + targetSys);
        }
        result.addStep(step);
    }

    private void executePhase3QueuesSurveillance(IrisOperationsScenarioResult result) throws Exception {
        ScenarioExecutionStep step = new ScenarioExecutionStep(3, "Petasos Queues Surveillance", "Petasos-Artemis", "GET_QUEUES");
        long start = System.currentTimeMillis();

        List<IrisOperationsModels.OpsQueueDto> queues = client.getQueues(actor.securityContext());
        step.setDurationMs(System.currentTimeMillis() - start);

        if (queues != null && !queues.isEmpty()) {
            result.setTotalQueues(queues.size());
            long totalDepth = queues.stream().mapToLong(IrisOperationsModels.OpsQueueDto::getDepth).sum();
            long totalDlq = queues.stream().mapToLong(IrisOperationsModels.OpsQueueDto::getDlqDepth).sum();
            result.setTotalQueueDepth(totalDepth);
            result.setTotalDlqDepth(totalDlq);

            if (profile == IrisOperationsScenarioProfile.DLQ_GROWTH) {
                if (totalDlq <= 0) {
                    throw new IllegalStateException("Expected positive DLQ depth for profile " + profile);
                }
            }

            step.setSuccess(true);
            step.setAckCode("200");
        } else {
            step.setSuccess(false);
            step.setErrorMessage("Queue inventory returned empty");
        }
        result.addStep(step);
    }

    private void executePhase4WorkflowsInspection(IrisOperationsScenarioResult result) throws Exception {
        ScenarioExecutionStep step = new ScenarioExecutionStep(4, "Workflows & Pragma Checkpoint Inspection", "Energeia-Ponos", "GET_WORKFLOWS");
        long start = System.currentTimeMillis();

        List<IrisOperationsModels.OpsWorkflowDto> wfs = client.getWorkflows(actor.securityContext());
        IrisOperationsModels.OpsPragmaDto pragma = client.getPragma("pragma-adt-01", actor.securityContext());

        step.setDurationMs(System.currentTimeMillis() - start);

        if (wfs != null && !wfs.isEmpty() && pragma != null) {
            result.setActiveWorkflows(wfs.size());
            int failedWork = wfs.stream().mapToInt(IrisOperationsModels.OpsWorkflowDto::getFailedWork).sum();
            result.setFailedWorkCount(failedWork);
            result.setInspectedPragmaId(pragma.getPragmaId());
            result.setInspectedPragmaStatus(pragma.getStatus());

            if (profile == IrisOperationsScenarioProfile.FAILED_PRAGMA) {
                if (!"FAILED".equalsIgnoreCase(pragma.getStatus())) {
                    throw new IllegalStateException("Expected Pragma status FAILED for profile " + profile);
                }
                result.setFailedErgon(pragma.getCurrentErgon());
            }

            step.setSuccess(true);
            step.setAckCode("200");
        } else {
            step.setSuccess(false);
            step.setErrorMessage("Workflows or Pragma inspection failed");
        }
        result.addStep(step);
    }

    private void executePhase5EventsDiagnosticTracing(IrisOperationsScenarioResult result) throws Exception {
        ScenarioExecutionStep step = new ScenarioExecutionStep(5, "Cross-Subsystem Diagnostic Timeline Tracing", "Audit-Event-Trace", "GET_EVENTS");
        long start = System.currentTimeMillis();

        String targetCorrId = "corr-scenario-" + seed;
        result.setInspectedCorrelationId(targetCorrId);

        List<IrisOperationsModels.OpsEventDto> eventList = client.getEvents(Map.of("correlationId", targetCorrId), actor.securityContext());
        step.setDurationMs(System.currentTimeMillis() - start);

        if (eventList != null && !eventList.isEmpty()) {
            result.setEventHopCount(eventList.size());
            step.setSuccess(true);
            step.setAckCode("200");
        } else {
            step.setSuccess(false);
            step.setErrorMessage("Cross-subsystem event timeline returned no hops for " + targetCorrId);
        }
        result.addStep(step);
    }

    private void executePhase6AlertsRemediation(IrisOperationsScenarioResult result) throws Exception {
        ScenarioExecutionStep step = new ScenarioExecutionStep(6, "Operational Alert Governance & Remediation", "Themis-Alerts", "ACKNOWLEDGE_ALERT");
        long start = System.currentTimeMillis();

        List<IrisOperationsModels.OpsAlertDto> alertList = client.getAlerts(null, actor.securityContext());
        int critCount = 0;
        int warnCount = 0;

        if (alertList != null) {
            for (IrisOperationsModels.OpsAlertDto a : alertList) {
                if ("CRITICAL".equalsIgnoreCase(a.getSeverity())) critCount++;
                if ("WARNING".equalsIgnoreCase(a.getSeverity())) warnCount++;
            }
        }
        result.setCriticalAlertCount(critCount);
        result.setWarningAlertCount(warnCount);

        if (profile == IrisOperationsScenarioProfile.CRITICAL_ALERT) {
            if (critCount == 0) {
                throw new IllegalStateException("Expected critical alert for profile " + profile);
            }
            IrisOperationsModels.OpsAlertDto targetAlert = alertList.stream()
                    .filter(a -> "CRITICAL".equalsIgnoreCase(a.getSeverity()))
                    .findFirst()
                    .orElseThrow();

            IrisOperationsModels.OpsAlertDto acked = client.acknowledgeAlert(targetAlert.getAlertId(), "operator-lead", actor.securityContext());
            if (acked != null && "ACKNOWLEDGED".equalsIgnoreCase(acked.getStatus())) {
                result.setAcknowledgedAlertId(acked.getAlertId());
                result.setAcknowledgedAlertStatus(acked.getStatus());
                step.setSuccess(true);
                step.setAckCode("200");
            } else {
                step.setSuccess(false);
                step.setErrorMessage("Alert acknowledge action failed to transition status to ACKNOWLEDGED");
            }
        } else {
            step.setSuccess(true);
            step.setAckCode("200");
        }

        step.setDurationMs(System.currentTimeMillis() - start);
        result.addStep(step);
    }
}
