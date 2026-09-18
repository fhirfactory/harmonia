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
 * Normalized platform-level operational summary for Harmonia.
 * Provides high-level health state, environment identifiers, subsystem counts, and alert totals.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OperationalSummary implements Serializable {

    private String platformStatus;       // HEALTHY, DEGRADED, UNAVAILABLE
    private String environment;          // e.g. "PROD / microk8s-01"
    private String cluster;              // cluster identifier
    private long timestamp;
    private int totalSubsystems;
    private int degradedSubsystems;
    private int criticalAlerts;
    private int warningAlerts;
    private long lastRefreshed;

    public OperationalSummary() {
    }

    public OperationalSummary(String platformStatus, String environment, String cluster,
                              long timestamp, int totalSubsystems, int degradedSubsystems,
                              int criticalAlerts, int warningAlerts, long lastRefreshed) {
        this.platformStatus = platformStatus;
        this.environment = environment;
        this.cluster = cluster;
        this.timestamp = timestamp;
        this.totalSubsystems = totalSubsystems;
        this.degradedSubsystems = degradedSubsystems;
        this.criticalAlerts = criticalAlerts;
        this.warningAlerts = warningAlerts;
        this.lastRefreshed = lastRefreshed;
    }

    public String getPlatformStatus() {
        return platformStatus;
    }

    public void setPlatformStatus(String platformStatus) {
        this.platformStatus = platformStatus;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getTotalSubsystems() {
        return totalSubsystems;
    }

    public void setTotalSubsystems(int totalSubsystems) {
        this.totalSubsystems = totalSubsystems;
    }

    public int getDegradedSubsystems() {
        return degradedSubsystems;
    }

    public void setDegradedSubsystems(int degradedSubsystems) {
        this.degradedSubsystems = degradedSubsystems;
    }

    public int getCriticalAlerts() {
        return criticalAlerts;
    }

    public void setCriticalAlerts(int criticalAlerts) {
        this.criticalAlerts = criticalAlerts;
    }

    public int getWarningAlerts() {
        return warningAlerts;
    }

    public void setWarningAlerts(int warningAlerts) {
        this.warningAlerts = warningAlerts;
    }

    public long getLastRefreshed() {
        return lastRefreshed;
    }

    public void setLastRefreshed(long lastRefreshed) {
        this.lastRefreshed = lastRefreshed;
    }
}
