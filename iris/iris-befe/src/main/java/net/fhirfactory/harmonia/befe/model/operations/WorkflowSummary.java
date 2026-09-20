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

package net.fhirfactory.harmonia.befe.model.operations;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Workflow (Praxis) operational execution summary.
 * Tracks active, queued, completed, failed, and retrying executions along with latency percentiles.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkflowSummary implements Serializable {

    private String workflowId;
    private String name;
    private String description;
    private int activeExecutions;
    private int queuedWork;
    private int completedWork;
    private int failedWork;
    private int retryingWork;
    private double processingRate;       // workflows/sec
    private long p95DurationMs;
    private double failureRate;          // percentage 0.0 - 100.0

    public WorkflowSummary() {
    }

    public WorkflowSummary(String workflowId, String name, String description, int activeExecutions,
                           int queuedWork, int completedWork, int failedWork, int retryingWork,
                           double processingRate, long p95DurationMs, double failureRate) {
        this.workflowId = workflowId;
        this.name = name;
        this.description = description;
        this.activeExecutions = activeExecutions;
        this.queuedWork = queuedWork;
        this.completedWork = completedWork;
        this.failedWork = failedWork;
        this.retryingWork = retryingWork;
        this.processingRate = processingRate;
        this.p95DurationMs = p95DurationMs;
        this.failureRate = failureRate;
    }

    public String getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getActiveExecutions() {
        return activeExecutions;
    }

    public void setActiveExecutions(int activeExecutions) {
        this.activeExecutions = activeExecutions;
    }

    public int getQueuedWork() {
        return queuedWork;
    }

    public void setQueuedWork(int queuedWork) {
        this.queuedWork = queuedWork;
    }

    public int getCompletedWork() {
        return completedWork;
    }

    public void setCompletedWork(int completedWork) {
        this.completedWork = completedWork;
    }

    public int getFailedWork() {
        return failedWork;
    }

    public void setFailedWork(int failedWork) {
        this.failedWork = failedWork;
    }

    public int getRetryingWork() {
        return retryingWork;
    }

    public void setRetryingWork(int retryingWork) {
        this.retryingWork = retryingWork;
    }

    public double getProcessingRate() {
        return processingRate;
    }

    public void setProcessingRate(double processingRate) {
        this.processingRate = processingRate;
    }

    public long getP95DurationMs() {
        return p95DurationMs;
    }

    public void setP95DurationMs(long p95DurationMs) {
        this.p95DurationMs = p95DurationMs;
    }

    public double getFailureRate() {
        return failureRate;
    }

    public void setFailureRate(double failureRate) {
        this.failureRate = failureRate;
    }
}
