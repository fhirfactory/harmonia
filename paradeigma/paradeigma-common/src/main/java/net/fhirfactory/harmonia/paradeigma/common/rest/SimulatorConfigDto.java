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

package net.fhirfactory.harmonia.paradeigma.common.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import net.fhirfactory.harmonia.paradeigma.common.failure.FaultInjectionConfig;
import net.fhirfactory.harmonia.paradeigma.common.model.ExecutionProfile;

/**
 * Runtime configuration parameters for Paradeigma simulators.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SimulatorConfigDto {

    private boolean timerEnabled = false;
    private String timerMode = "FIXED"; // FIXED or RANDOM_RANGE
    private long fixedIntervalMs = 5000L;
    private long minIntervalMs = 2000L;
    private long maxIntervalMs = 10000L;
    private ExecutionProfile profile = ExecutionProfile.TEST;
    private FaultInjectionConfig faultInjection;

    public SimulatorConfigDto() {
    }

    public boolean isTimerEnabled() {
        return timerEnabled;
    }

    public void setTimerEnabled(boolean timerEnabled) {
        this.timerEnabled = timerEnabled;
    }

    public String getTimerMode() {
        return timerMode != null ? timerMode : "FIXED";
    }

    public void setTimerMode(String timerMode) {
        this.timerMode = timerMode;
    }

    public long getFixedIntervalMs() {
        return fixedIntervalMs;
    }

    public void setFixedIntervalMs(long fixedIntervalMs) {
        this.fixedIntervalMs = fixedIntervalMs;
    }

    public long getMinIntervalMs() {
        return minIntervalMs;
    }

    public void setMinIntervalMs(long minIntervalMs) {
        this.minIntervalMs = minIntervalMs;
    }

    public long getMaxIntervalMs() {
        return maxIntervalMs;
    }

    public void setMaxIntervalMs(long maxIntervalMs) {
        this.maxIntervalMs = maxIntervalMs;
    }

    public ExecutionProfile getProfile() {
        return profile != null ? profile : ExecutionProfile.TEST;
    }

    public void setProfile(ExecutionProfile profile) {
        this.profile = profile;
    }

    public FaultInjectionConfig getFaultInjection() {
        return faultInjection;
    }

    public void setFaultInjection(FaultInjectionConfig faultInjection) {
        this.faultInjection = faultInjection;
    }
}
