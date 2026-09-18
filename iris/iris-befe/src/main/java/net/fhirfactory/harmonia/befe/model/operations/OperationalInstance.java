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
import java.util.List;

/**
 * Domain-neutral runtime instance representation (mapping to Kubernetes Pods, Docker containers,
 * or cluster member processes).
 * Contains safe operational telemetry, probe state, resource usage, and error summaries without secrets.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OperationalInstance implements Serializable {

    private String instanceId;           // e.g. "petasos-0"
    private String subsystemId;          // e.g. "petasos"
    private String role;                 // Primary, Standby, Worker
    private String state;                // Running, Standby, Terminated, Failed
    private boolean ready;
    private int restartCount;
    private String uptime;               // e.g. "2d 14h"
    private long startedAt;
    private double cpuPercent;           // e.g. 18.0 (-1.0 if N/A)
    private long memoryMb;               // e.g. 768
    // Detail attributes (safe operational metadata)
    private String podName;
    private String namespace;
    private String nodeName;
    private String ipAddress;
    private String containerImage;
    private String appVersion;
    private List<String> recentErrors = new ArrayList<>();
    private List<String> dependencies = new ArrayList<>();

    public OperationalInstance() {
    }

    public OperationalInstance(String instanceId, String subsystemId, String role, String state,
                               boolean ready, int restartCount, String uptime, long startedAt,
                               double cpuPercent, long memoryMb) {
        this.instanceId = instanceId;
        this.subsystemId = subsystemId;
        this.role = role;
        this.state = state;
        this.ready = ready;
        this.restartCount = restartCount;
        this.uptime = uptime;
        this.startedAt = startedAt;
        this.cpuPercent = cpuPercent;
        this.memoryMb = memoryMb;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getSubsystemId() {
        return subsystemId;
    }

    public void setSubsystemId(String subsystemId) {
        this.subsystemId = subsystemId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public int getRestartCount() {
        return restartCount;
    }

    public void setRestartCount(int restartCount) {
        this.restartCount = restartCount;
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public long getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(long startedAt) {
        this.startedAt = startedAt;
    }

    public double getCpuPercent() {
        return cpuPercent;
    }

    public void setCpuPercent(double cpuPercent) {
        this.cpuPercent = cpuPercent;
    }

    public long getMemoryMb() {
        return memoryMb;
    }

    public void setMemoryMb(long memoryMb) {
        this.memoryMb = memoryMb;
    }

    public String getPodName() {
        return podName;
    }

    public void setPodName(String podName) {
        this.podName = podName;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getContainerImage() {
        return containerImage;
    }

    public void setContainerImage(String containerImage) {
        this.containerImage = containerImage;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public List<String> getRecentErrors() {
        return recentErrors;
    }

    public void setRecentErrors(List<String> recentErrors) {
        this.recentErrors = recentErrors != null ? new ArrayList<>(recentErrors) : new ArrayList<>();
    }

    public List<String> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<String> dependencies) {
        this.dependencies = dependencies != null ? new ArrayList<>(dependencies) : new ArrayList<>();
    }
}
