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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Detailed operational health, reliability metrics, and downstream dependencies for a subsystem.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OperationalHealth implements Serializable {

    private String subsystemId;
    private String status;               // HEALTHY, DEGRADED, UNAVAILABLE, UNKNOWN
    private Double availabilityPercent;  // e.g. 99.98, or null if unmeasured / N/A
    private int failedOperations;
    private int restartCount;
    private Long p95LatencyMs;           // null if unmeasured / N/A
    private String dependenciesSummary;  // e.g. "4 / 4 Healthy"
    private List<DependencyHealth> dependencies = new ArrayList<>();
    private Map<String, Object> details = new HashMap<>();

    public OperationalHealth() {
    }

    public OperationalHealth(String subsystemId, String status, Double availabilityPercent,
                             int failedOperations, int restartCount, Long p95LatencyMs,
                             String dependenciesSummary) {
        this.subsystemId = subsystemId;
        this.status = status;
        this.availabilityPercent = availabilityPercent;
        this.failedOperations = failedOperations;
        this.restartCount = restartCount;
        this.p95LatencyMs = p95LatencyMs;
        this.dependenciesSummary = dependenciesSummary;
    }

    public OperationalHealth(String subsystemId, String status, double availabilityPercent,
                             int failedOperations, int restartCount, long p95LatencyMs,
                             String dependenciesSummary) {
        this(subsystemId, status, Double.valueOf(availabilityPercent), failedOperations, restartCount, Long.valueOf(p95LatencyMs), dependenciesSummary);
    }

    public String getSubsystemId() {
        return subsystemId;
    }

    public void setSubsystemId(String subsystemId) {
        this.subsystemId = subsystemId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getAvailabilityPercent() {
        return availabilityPercent;
    }

    public void setAvailabilityPercent(Double availabilityPercent) {
        this.availabilityPercent = availabilityPercent;
    }

    public int getFailedOperations() {
        return failedOperations;
    }

    public void setFailedOperations(int failedOperations) {
        this.failedOperations = failedOperations;
    }

    public int getRestartCount() {
        return restartCount;
    }

    public void setRestartCount(int restartCount) {
        this.restartCount = restartCount;
    }

    public Long getP95LatencyMs() {
        return p95LatencyMs;
    }

    public void setP95LatencyMs(Long p95LatencyMs) {
        this.p95LatencyMs = p95LatencyMs;
    }

    public String getDependenciesSummary() {
        return dependenciesSummary;
    }

    public void setDependenciesSummary(String dependenciesSummary) {
        this.dependenciesSummary = dependenciesSummary;
    }

    public List<DependencyHealth> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<DependencyHealth> dependencies) {
        this.dependencies = dependencies != null ? new ArrayList<>(dependencies) : new ArrayList<>();
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Object> details) {
        this.details = details != null ? new HashMap<>(details) : new HashMap<>();
    }
}
