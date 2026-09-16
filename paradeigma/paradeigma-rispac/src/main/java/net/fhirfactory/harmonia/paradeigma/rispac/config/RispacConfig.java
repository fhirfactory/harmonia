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

package net.fhirfactory.harmonia.paradeigma.rispac.config;

import net.fhirfactory.harmonia.paradeigma.common.failure.FaultInjectionConfig;
import net.fhirfactory.harmonia.paradeigma.common.model.ExecutionProfile;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "paradeigma.rispac")
public class RispacConfig {

    private int inboundAdtPort = 2203; // PD-07 RISPAC-ADT-OUT (inbound listener for ADT)
    private int inboundOrmPort = 2205; // PD-09 RISPAC-ORM-OUT (inbound listener for Imaging ORM)
    private String harmoniaHost = "localhost";
    private int harmoniaOruPort = 2103; // PD-03 RISPAC-ORU-IN (outbound target)

    private boolean autoProduceReports = true;
    private long reportingDelayMs = 1500L; // Delay before returning imaging report

    private ExecutionProfile profile = ExecutionProfile.TEST;
    private long seed = 12345L;

    private FaultInjectionConfig faultInjection = new FaultInjectionConfig();

    public RispacConfig() {
    }

    public int getInboundAdtPort() {
        return inboundAdtPort;
    }

    public void setInboundAdtPort(int inboundAdtPort) {
        this.inboundAdtPort = inboundAdtPort;
    }

    public int getInboundOrmPort() {
        return inboundOrmPort;
    }

    public void setInboundOrmPort(int inboundOrmPort) {
        this.inboundOrmPort = inboundOrmPort;
    }

    public String getHarmoniaHost() {
        return harmoniaHost;
    }

    public void setHarmoniaHost(String harmoniaHost) {
        this.harmoniaHost = harmoniaHost;
    }

    public int getHarmoniaOruPort() {
        return harmoniaOruPort;
    }

    public void setHarmoniaOruPort(int harmoniaOruPort) {
        this.harmoniaOruPort = harmoniaOruPort;
    }

    public boolean isAutoProduceReports() {
        return autoProduceReports;
    }

    public void setAutoProduceReports(boolean autoProduceReports) {
        this.autoProduceReports = autoProduceReports;
    }

    public long getReportingDelayMs() {
        return reportingDelayMs;
    }

    public void setReportingDelayMs(long reportingDelayMs) {
        this.reportingDelayMs = reportingDelayMs;
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
