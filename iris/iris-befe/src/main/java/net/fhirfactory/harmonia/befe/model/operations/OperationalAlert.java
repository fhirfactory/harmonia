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
 * Actionable operational alert representation.
 * Classifies operational issues by severity (CRITICAL, WARNING, INFORMATION) and lifecycle status
 * (ACTIVE, ACKNOWLEDGED, RESOLVED) with operator remediation guidance.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OperationalAlert implements Serializable {

    private String alertId;
    private String severity;             // CRITICAL, WARNING, INFORMATION
    private String subsystem;
    private String component;
    private String condition;
    private long firstObserved;
    private long lastObserved;
    private String duration;
    private String status;               // ACTIVE, ACKNOWLEDGED, RESOLVED
    private String relatedResource;
    private String correlationInfo;
    private String operatorGuidance;

    public OperationalAlert() {
    }

    public OperationalAlert(String alertId, String severity, String subsystem, String component,
                            String condition, long firstObserved, long lastObserved,
                            String duration, String status, String relatedResource,
                            String correlationInfo, String operatorGuidance) {
        this.alertId = alertId;
        this.severity = severity;
        this.subsystem = subsystem;
        this.component = component;
        this.condition = condition;
        this.firstObserved = firstObserved;
        this.lastObserved = lastObserved;
        this.duration = duration;
        this.status = status;
        this.relatedResource = relatedResource;
        this.correlationInfo = correlationInfo;
        this.operatorGuidance = operatorGuidance;
    }

    public String getAlertId() {
        return alertId;
    }

    public void setAlertId(String alertId) {
        this.alertId = alertId;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getSubsystem() {
        return subsystem;
    }

    public void setSubsystem(String subsystem) {
        this.subsystem = subsystem;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public long getFirstObserved() {
        return firstObserved;
    }

    public void setFirstObserved(long firstObserved) {
        this.firstObserved = firstObserved;
    }

    public long getLastObserved() {
        return lastObserved;
    }

    public void setLastObserved(long lastObserved) {
        this.lastObserved = lastObserved;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRelatedResource() {
        return relatedResource;
    }

    public void setRelatedResource(String relatedResource) {
        this.relatedResource = relatedResource;
    }

    public String getCorrelationInfo() {
        return correlationInfo;
    }

    public void setCorrelationInfo(String correlationInfo) {
        this.correlationInfo = correlationInfo;
    }

    public String getOperatorGuidance() {
        return operatorGuidance;
    }

    public void setOperatorGuidance(String operatorGuidance) {
        this.operatorGuidance = operatorGuidance;
    }
}
