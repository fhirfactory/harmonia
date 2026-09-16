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

package net.fhirfactory.harmonia.paradeigma.pas.config;

import net.fhirfactory.harmonia.paradeigma.common.failure.FaultInjectionConfig;
import net.fhirfactory.harmonia.paradeigma.common.model.ExecutionProfile;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "paradeigma.pas")
public class PasConfig {

    private String harmoniaHost = "localhost";
    private int harmoniaPort = 2101; // PD-01 PAS-ADT-IN

    private boolean timerEnabled = false;
    private String timerMode = "FIXED"; // FIXED or RANDOM_RANGE
    private long fixedIntervalMs = 5000L;
    private long minIntervalMs = 2000L;
    private long maxIntervalMs = 10000L;

    private ExecutionProfile profile = ExecutionProfile.TEST;
    private long seed = 12345L;

    private FaultInjectionConfig faultInjection = new FaultInjectionConfig();

    public PasConfig() {
    }

    public String getHarmoniaHost() {
        return harmoniaHost;
    }

    public void setHarmoniaHost(String harmoniaHost) {
        this.harmoniaHost = harmoniaHost;
    }

    public int getHarmoniaPort() {
        return harmoniaPort;
    }

    public void setHarmoniaPort(int harmoniaPort) {
        this.harmoniaPort = harmoniaPort;
    }

    public boolean isTimerEnabled() {
        return timerEnabled;
    }

    public void setTimerEnabled(boolean timerEnabled) {
        this.timerEnabled = timerEnabled;
    }

    public String getTimerMode() {
        return timerMode;
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
        return profile;
    }

    public void setProfile(ExecutionProfile profile) {
        this.profile = profile;
    }

    public long getSeed() {
        return seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public FaultInjectionConfig getFaultInjection() {
        return faultInjection;
    }

    public void setFaultInjection(FaultInjectionConfig faultInjection) {
        this.faultInjection = faultInjection;
    }
}
