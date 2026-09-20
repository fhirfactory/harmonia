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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Normalized operational domain models reflecting the public REST contracts exposed by iris-befe on port 8090.
 */
public final class IrisOperationsModels {

    private IrisOperationsModels() {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class OpsSummaryDto {
        private String platformStatus;
        private String environment;
        private String cluster;
        private long timestamp;
        private int totalSubsystems;
        private int degradedSubsystems;
        private int criticalAlerts;
        private int warningAlerts;
        private long lastRefreshed;

        public OpsSummaryDto() {}

        public String getPlatformStatus() { return platformStatus; }
        public void setPlatformStatus(String platformStatus) { this.platformStatus = platformStatus; }
        public String getEnvironment() { return environment; }
        public void setEnvironment(String environment) { this.environment = environment; }
        public String getCluster() { return cluster; }
        public void setCluster(String cluster) { this.cluster = cluster; }
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
        public int getTotalSubsystems() { return totalSubsystems; }
        public void setTotalSubsystems(int totalSubsystems) { this.totalSubsystems = totalSubsystems; }
        public int getDegradedSubsystems() { return degradedSubsystems; }
        public void setDegradedSubsystems(int degradedSubsystems) { this.degradedSubsystems = degradedSubsystems; }
        public int getCriticalAlerts() { return criticalAlerts; }
        public void setCriticalAlerts(int criticalAlerts) { this.criticalAlerts = criticalAlerts; }
        public int getWarningAlerts() { return warningAlerts; }
        public void setWarningAlerts(int warningAlerts) { this.warningAlerts = warningAlerts; }
        public long getLastRefreshed() { return lastRefreshed; }
        public void setLastRefreshed(long lastRefreshed) { this.lastRefreshed = lastRefreshed; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class OpsSubsystemDto {
        private String id;
        private String name;
        private String description;
        private String state;
        private int instanceCount;
        private String version;
        private long lastUpdated;
        private List<OpsSubsystemDto> children = new ArrayList<>();

        public OpsSubsystemDto() {}

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getState() { return state; }
        public void setState(String state) { this.state = state; }
        public int getInstanceCount() { return instanceCount; }
        public void setInstanceCount(int instanceCount) { this.instanceCount = instanceCount; }
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        public long getLastUpdated() { return lastUpdated; }
        public void setLastUpdated(long lastUpdated) { this.lastUpdated = lastUpdated; }
        public List<OpsSubsystemDto> getChildren() { return children; }
        public void setChildren(List<OpsSubsystemDto> children) { this.children = children; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class OpsInstanceDto {
        private String instanceId;
        private String subsystemId;
        private String role;
        private String state;
        private boolean ready;
        private int restartCount;
        private String uptime;
        private long startedAt;
        private double cpuPercent;
        private long memoryMb;
        private String podName;
        private String namespace;
        private String nodeName;
        private String ipAddress;
        private String containerImage;
        private String appVersion;
        private List<String> recentErrors = new ArrayList<>();
        private List<String> dependencies = new ArrayList<>();

        public OpsInstanceDto() {}

        public String getInstanceId() { return instanceId; }
        public void setInstanceId(String instanceId) { this.instanceId = instanceId; }
        public String getSubsystemId() { return subsystemId; }
        public void setSubsystemId(String subsystemId) { this.subsystemId = subsystemId; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public String getState() { return state; }
        public void setState(String state) { this.state = state; }
        public boolean isReady() { return ready; }
        public void setReady(boolean ready) { this.ready = ready; }
        public int getRestartCount() { return restartCount; }
        public void setRestartCount(int restartCount) { this.restartCount = restartCount; }
        public String getUptime() { return uptime; }
        public void setUptime(String uptime) { this.uptime = uptime; }
        public long getStartedAt() { return startedAt; }
        public void setStartedAt(long startedAt) { this.startedAt = startedAt; }
        public double getCpuPercent() { return cpuPercent; }
        public void setCpuPercent(double cpuPercent) { this.cpuPercent = cpuPercent; }
        public long getMemoryMb() { return memoryMb; }
        public void setMemoryMb(long memoryMb) { this.memoryMb = memoryMb; }
        public String getPodName() { return podName; }
        public void setPodName(String podName) { this.podName = podName; }
        public String getNamespace() { return namespace; }
        public void setNamespace(String namespace) { this.namespace = namespace; }
        public String getNodeName() { return nodeName; }
        public void setNodeName(String nodeName) { this.nodeName = nodeName; }
        public String getIpAddress() { return ipAddress; }
        public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
        public String getContainerImage() { return containerImage; }
        public void setContainerImage(String containerImage) { this.containerImage = containerImage; }
        public String getAppVersion() { return appVersion; }
        public void setAppVersion(String appVersion) { this.appVersion = appVersion; }
        public List<String> getRecentErrors() { return recentErrors; }
        public void setRecentErrors(List<String> recentErrors) { this.recentErrors = recentErrors; }
        public List<String> getDependencies() { return dependencies; }
        public void setDependencies(List<String> dependencies) { this.dependencies = dependencies; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class OpsHealthDto {
        private String subsystemId;
        private String status;
        private double availabilityPercent;
        private int failedOperations;
        private int restartCount;
        private long p95LatencyMs;
        private String dependenciesSummary;
        private List<OpsDependencyHealthDto> dependencies = new ArrayList<>();
        private Map<String, Object> details = new HashMap<>();

        public OpsHealthDto() {}

        public String getSubsystemId() { return subsystemId; }
        public void setSubsystemId(String subsystemId) { this.subsystemId = subsystemId; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public double getAvailabilityPercent() { return availabilityPercent; }
        public void setAvailabilityPercent(double availabilityPercent) { this.availabilityPercent = availabilityPercent; }
        public int getFailedOperations() { return failedOperations; }
        public void setFailedOperations(int failedOperations) { this.failedOperations = failedOperations; }
        public int getRestartCount() { return restartCount; }
        public void setRestartCount(int restartCount) { this.restartCount = restartCount; }
        public long getP95LatencyMs() { return p95LatencyMs; }
        public void setP95LatencyMs(long p95LatencyMs) { this.p95LatencyMs = p95LatencyMs; }
        public String getDependenciesSummary() { return dependenciesSummary; }
        public void setDependenciesSummary(String dependenciesSummary) { this.dependenciesSummary = dependenciesSummary; }
        public List<OpsDependencyHealthDto> getDependencies() { return dependencies; }
        public void setDependencies(List<OpsDependencyHealthDto> dependencies) { this.dependencies = dependencies; }
        public Map<String, Object> getDetails() { return details; }
        public void setDetails(Map<String, Object> details) { this.details = details; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class OpsDependencyHealthDto {
        private String name;
        private String status;
        private long latencyMs;
        private String message;

        public OpsDependencyHealthDto() {}

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public long getLatencyMs() { return latencyMs; }
        public void setLatencyMs(long latencyMs) { this.latencyMs = latencyMs; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class OpsQueueDto {
        private String queueId;
        private String queueName;
        private String address;
        private String status;
        private long depth;
        private int consumerCount;
        private int producerCount;
        private double enqueueRate;
        private double dequeueRate;
        private long oldestMessageAgeSeconds;
        private long redeliveryCount;
        private long dlqDepth;
        private long expiryCount;
        private String associatedCapability;

        public OpsQueueDto() {}

        public String getQueueId() { return queueId; }
        public void setQueueId(String queueId) { this.queueId = queueId; }
        public String getQueueName() { return queueName; }
        public void setQueueName(String queueName) { this.queueName = queueName; }
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public long getDepth() { return depth; }
        public void setDepth(long depth) { this.depth = depth; }
        public int getConsumerCount() { return consumerCount; }
        public void setConsumerCount(int consumerCount) { this.consumerCount = consumerCount; }
        public int getProducerCount() { return producerCount; }
        public void setProducerCount(int producerCount) { this.producerCount = producerCount; }
        public double getEnqueueRate() { return enqueueRate; }
        public void setEnqueueRate(double enqueueRate) { this.enqueueRate = enqueueRate; }
        public double getDequeueRate() { return dequeueRate; }
        public void setDequeueRate(double dequeueRate) { this.dequeueRate = dequeueRate; }
        public long getOldestMessageAgeSeconds() { return oldestMessageAgeSeconds; }
        public void setOldestMessageAgeSeconds(long oldestMessageAgeSeconds) { this.oldestMessageAgeSeconds = oldestMessageAgeSeconds; }
        public long getRedeliveryCount() { return redeliveryCount; }
        public void setRedeliveryCount(long redeliveryCount) { this.redeliveryCount = redeliveryCount; }
        public long getDlqDepth() { return dlqDepth; }
        public void setDlqDepth(long dlqDepth) { this.dlqDepth = dlqDepth; }
        public long getExpiryCount() { return expiryCount; }
        public void setExpiryCount(long expiryCount) { this.expiryCount = expiryCount; }
        public String getAssociatedCapability() { return associatedCapability; }
        public void setAssociatedCapability(String associatedCapability) { this.associatedCapability = associatedCapability; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class OpsWorkflowDto {
        private String workflowId;
        private String name;
        private String description;
        private int activeExecutions;
        private int queuedWork;
        private int completedWork;
        private int failedWork;
        private int retryingWork;
        private double processingRate;
        private long p95DurationMs;
        private double failureRate;

        public OpsWorkflowDto() {}

        public String getWorkflowId() { return workflowId; }
        public void setWorkflowId(String workflowId) { this.workflowId = workflowId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public int getActiveExecutions() { return activeExecutions; }
        public void setActiveExecutions(int activeExecutions) { this.activeExecutions = activeExecutions; }
        public int getQueuedWork() { return queuedWork; }
        public void setQueuedWork(int queuedWork) { this.queuedWork = queuedWork; }
        public int getCompletedWork() { return completedWork; }
        public void setCompletedWork(int completedWork) { this.completedWork = completedWork; }
        public int getFailedWork() { return failedWork; }
        public void setFailedWork(int failedWork) { this.failedWork = failedWork; }
        public int getRetryingWork() { return retryingWork; }
        public void setRetryingWork(int retryingWork) { this.retryingWork = retryingWork; }
        public double getProcessingRate() { return processingRate; }
        public void setProcessingRate(double processingRate) { this.processingRate = processingRate; }
        public long getP95DurationMs() { return p95DurationMs; }
        public void setP95DurationMs(long p95DurationMs) { this.p95DurationMs = p95DurationMs; }
        public double getFailureRate() { return failureRate; }
        public void setFailureRate(double failureRate) { this.failureRate = failureRate; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class OpsPragmaDto {
        private String pragmaId;
        private String praxisId;
        private String status;
        private long startedAt;
        private long durationMs;
        private String currentErgon;
        private int completedErgaCount;
        private int retryCount;
        private String correlationId;
        private String causationId;
        private String failureReasonCode;
        private List<OpsErgonCheckpointDto> checkpoints = new ArrayList<>();

        public OpsPragmaDto() {}

        public String getPragmaId() { return pragmaId; }
        public void setPragmaId(String pragmaId) { this.pragmaId = pragmaId; }
        public String getPraxisId() { return praxisId; }
        public void setPraxisId(String praxisId) { this.praxisId = praxisId; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public long getStartedAt() { return startedAt; }
        public void setStartedAt(long startedAt) { this.startedAt = startedAt; }
        public long getDurationMs() { return durationMs; }
        public void setDurationMs(long durationMs) { this.durationMs = durationMs; }
        public String getCurrentErgon() { return currentErgon; }
        public void setCurrentErgon(String currentErgon) { this.currentErgon = currentErgon; }
        public int getCompletedErgaCount() { return completedErgaCount; }
        public void setCompletedErgaCount(int completedErgaCount) { this.completedErgaCount = completedErgaCount; }
        public int getRetryCount() { return retryCount; }
        public void setRetryCount(int retryCount) { this.retryCount = retryCount; }
        public String getCorrelationId() { return correlationId; }
        public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }
        public String getCausationId() { return causationId; }
        public void setCausationId(String causationId) { this.causationId = causationId; }
        public String getFailureReasonCode() { return failureReasonCode; }
        public void setFailureReasonCode(String failureReasonCode) { this.failureReasonCode = failureReasonCode; }
        public List<OpsErgonCheckpointDto> getCheckpoints() { return checkpoints; }
        public void setCheckpoints(List<OpsErgonCheckpointDto> checkpoints) { this.checkpoints = checkpoints; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class OpsErgonCheckpointDto {
        private String ergonId;
        private String ergonType;
        private String status;
        private long startedAt;
        private long durationMs;
        private String errorDetail;

        public OpsErgonCheckpointDto() {}

        public OpsErgonCheckpointDto(String ergonId, String ergonType, String status, long startedAt, long durationMs, String errorDetail) {
            this.ergonId = ergonId;
            this.ergonType = ergonType;
            this.status = status;
            this.startedAt = startedAt;
            this.durationMs = durationMs;
            this.errorDetail = errorDetail;
        }

        public String getErgonId() { return ergonId; }
        public void setErgonId(String ergonId) { this.ergonId = ergonId; }
        public String getErgonType() { return ergonType; }
        public void setErgonType(String ergonType) { this.ergonType = ergonType; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public long getStartedAt() { return startedAt; }
        public void setStartedAt(long startedAt) { this.startedAt = startedAt; }
        public long getDurationMs() { return durationMs; }
        public void setDurationMs(long durationMs) { this.durationMs = durationMs; }
        public String getErrorDetail() { return errorDetail; }
        public void setErrorDetail(String errorDetail) { this.errorDetail = errorDetail; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class OpsEventDto {
        private String eventId;
        private long timestamp;
        private String subsystem;
        private String eventType;
        private String operation;
        private String status;
        private long durationMs;
        private String messageId;
        private String correlationId;
        private String causationId;
        private String pragmaId;
        private String praxisId;
        private String ergonId;
        private String interfaceId;
        private String reasonCode;

        public OpsEventDto() {}

        public String getEventId() { return eventId; }
        public void setEventId(String eventId) { this.eventId = eventId; }
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
        public String getSubsystem() { return subsystem; }
        public void setSubsystem(String subsystem) { this.subsystem = subsystem; }
        public String getEventType() { return eventType; }
        public void setEventType(String eventType) { this.eventType = eventType; }
        public String getOperation() { return operation; }
        public void setOperation(String operation) { this.operation = operation; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public long getDurationMs() { return durationMs; }
        public void setDurationMs(long durationMs) { this.durationMs = durationMs; }
        public String getMessageId() { return messageId; }
        public void setMessageId(String messageId) { this.messageId = messageId; }
        public String getCorrelationId() { return correlationId; }
        public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }
        public String getCausationId() { return causationId; }
        public void setCausationId(String causationId) { this.causationId = causationId; }
        public String getPragmaId() { return pragmaId; }
        public void setPragmaId(String pragmaId) { this.pragmaId = pragmaId; }
        public String getPraxisId() { return praxisId; }
        public void setPraxisId(String praxisId) { this.praxisId = praxisId; }
        public String getErgonId() { return ergonId; }
        public void setErgonId(String ergonId) { this.ergonId = ergonId; }
        public String getInterfaceId() { return interfaceId; }
        public void setInterfaceId(String interfaceId) { this.interfaceId = interfaceId; }
        public String getReasonCode() { return reasonCode; }
        public void setReasonCode(String reasonCode) { this.reasonCode = reasonCode; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class OpsAlertDto {
        private String alertId;
        private String severity;
        private String subsystem;
        private String component;
        private String condition;
        private long firstObserved;
        private long lastObserved;
        private String duration;
        private String status;
        private String relatedResource;
        private String correlationInfo;
        private String operatorGuidance;

        public OpsAlertDto() {}

        public String getAlertId() { return alertId; }
        public void setAlertId(String alertId) { this.alertId = alertId; }
        public String getSeverity() { return severity; }
        public void setSeverity(String severity) { this.severity = severity; }
        public String getSubsystem() { return subsystem; }
        public void setSubsystem(String subsystem) { this.subsystem = subsystem; }
        public String getComponent() { return component; }
        public void setComponent(String component) { this.component = component; }
        public String getCondition() { return condition; }
        public void setCondition(String condition) { this.condition = condition; }
        public long getFirstObserved() { return firstObserved; }
        public void setFirstObserved(long firstObserved) { this.firstObserved = firstObserved; }
        public long getLastObserved() { return lastObserved; }
        public void setLastObserved(long lastObserved) { this.lastObserved = lastObserved; }
        public String getDuration() { return duration; }
        public void setDuration(String duration) { this.duration = duration; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getRelatedResource() { return relatedResource; }
        public void setRelatedResource(String relatedResource) { this.relatedResource = relatedResource; }
        public String getCorrelationInfo() { return correlationInfo; }
        public void setCorrelationInfo(String correlationInfo) { this.correlationInfo = correlationInfo; }
        public String getOperatorGuidance() { return operatorGuidance; }
        public void setOperatorGuidance(String operatorGuidance) { this.operatorGuidance = operatorGuidance; }
    }
}
