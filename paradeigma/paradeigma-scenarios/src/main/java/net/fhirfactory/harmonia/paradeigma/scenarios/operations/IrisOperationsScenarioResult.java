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

import com.fasterxml.jackson.annotation.JsonInclude;
import net.fhirfactory.harmonia.paradeigma.scenarios.model.ScenarioExecutionStep;
import net.fhirfactory.harmonia.themis.api.model.ThemisDecision;

import java.util.ArrayList;
import java.util.List;

/**
 * Result model capturing execution details and verification metrics of an Iris Operations scenario.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IrisOperationsScenarioResult {

    private String scenarioId;
    private String scenarioName;
    private long seed;
    private IrisOperationsScenarioProfile profile;
    private ThemisDecision securityDecision;
    private boolean success;
    private long durationMs;
    private String errorMessage;

    // Platform Overview & Subsystem Metrics
    private String platformStatus;
    private int totalSubsystems;
    private int degradedSubsystems;

    // Queues Perspective Metrics
    private int totalQueues;
    private long totalQueueDepth;
    private long totalDlqDepth;

    // Workflows Perspective Metrics
    private int activeWorkflows;
    private int failedWorkCount;
    private String inspectedPragmaId;
    private String inspectedPragmaStatus;
    private String failedErgon;

    // Events Perspective Metrics
    private String inspectedCorrelationId;
    private int eventHopCount;

    // Alerts Perspective Metrics
    private int criticalAlertCount;
    private int warningAlertCount;
    private String acknowledgedAlertId;
    private String acknowledgedAlertStatus;

    // Execution Steps
    private List<ScenarioExecutionStep> steps = new ArrayList<>();

    public IrisOperationsScenarioResult() {}

    public String getScenarioId() { return scenarioId; }
    public void setScenarioId(String scenarioId) { this.scenarioId = scenarioId; }

    public String getScenarioName() { return scenarioName; }
    public void setScenarioName(String scenarioName) { this.scenarioName = scenarioName; }

    public long getSeed() { return seed; }
    public void setSeed(long seed) { this.seed = seed; }

    public IrisOperationsScenarioProfile getProfile() { return profile; }
    public void setProfile(IrisOperationsScenarioProfile profile) { this.profile = profile; }

    public ThemisDecision getSecurityDecision() { return securityDecision; }
    public void setSecurityDecision(ThemisDecision securityDecision) { this.securityDecision = securityDecision; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public long getDurationMs() { return durationMs; }
    public void setDurationMs(long durationMs) { this.durationMs = durationMs; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public String getPlatformStatus() { return platformStatus; }
    public void setPlatformStatus(String platformStatus) { this.platformStatus = platformStatus; }

    public int getTotalSubsystems() { return totalSubsystems; }
    public void setTotalSubsystems(int totalSubsystems) { this.totalSubsystems = totalSubsystems; }

    public int getDegradedSubsystems() { return degradedSubsystems; }
    public void setDegradedSubsystems(int degradedSubsystems) { this.degradedSubsystems = degradedSubsystems; }

    public int getTotalQueues() { return totalQueues; }
    public void setTotalQueues(int totalQueues) { this.totalQueues = totalQueues; }

    public long getTotalQueueDepth() { return totalQueueDepth; }
    public void setTotalQueueDepth(long totalQueueDepth) { this.totalQueueDepth = totalQueueDepth; }

    public long getTotalDlqDepth() { return totalDlqDepth; }
    public void setTotalDlqDepth(long totalDlqDepth) { this.totalDlqDepth = totalDlqDepth; }

    public int getActiveWorkflows() { return activeWorkflows; }
    public void setActiveWorkflows(int activeWorkflows) { this.activeWorkflows = activeWorkflows; }

    public int getFailedWorkCount() { return failedWorkCount; }
    public void setFailedWorkCount(int failedWorkCount) { this.failedWorkCount = failedWorkCount; }

    public String getInspectedPragmaId() { return inspectedPragmaId; }
    public void setInspectedPragmaId(String inspectedPragmaId) { this.inspectedPragmaId = inspectedPragmaId; }

    public String getInspectedPragmaStatus() { return inspectedPragmaStatus; }
    public void setInspectedPragmaStatus(String inspectedPragmaStatus) { this.inspectedPragmaStatus = inspectedPragmaStatus; }

    public String getFailedErgon() { return failedErgon; }
    public void setFailedErgon(String failedErgon) { this.failedErgon = failedErgon; }

    public String getInspectedCorrelationId() { return inspectedCorrelationId; }
    public void setInspectedCorrelationId(String inspectedCorrelationId) { this.inspectedCorrelationId = inspectedCorrelationId; }

    public int getEventHopCount() { return eventHopCount; }
    public void setEventHopCount(int eventHopCount) { this.eventHopCount = eventHopCount; }

    public int getCriticalAlertCount() { return criticalAlertCount; }
    public void setCriticalAlertCount(int criticalAlertCount) { this.criticalAlertCount = criticalAlertCount; }

    public int getWarningAlertCount() { return warningAlertCount; }
    public void setWarningAlertCount(int warningAlertCount) { this.warningAlertCount = warningAlertCount; }

    public String getAcknowledgedAlertId() { return acknowledgedAlertId; }
    public void setAcknowledgedAlertId(String acknowledgedAlertId) { this.acknowledgedAlertId = acknowledgedAlertId; }

    public String getAcknowledgedAlertStatus() { return acknowledgedAlertStatus; }
    public void setAcknowledgedAlertStatus(String acknowledgedAlertStatus) { this.acknowledgedAlertStatus = acknowledgedAlertStatus; }

    public List<ScenarioExecutionStep> getSteps() { return steps; }
    public void setSteps(List<ScenarioExecutionStep> steps) { this.steps = steps; }
    public void addStep(ScenarioExecutionStep step) { this.steps.add(step); }
}
