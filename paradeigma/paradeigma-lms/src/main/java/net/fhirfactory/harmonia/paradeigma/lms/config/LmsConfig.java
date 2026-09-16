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

package net.fhirfactory.harmonia.paradeigma.lms.config;

import net.fhirfactory.harmonia.paradeigma.common.failure.FaultInjectionConfig;
import net.fhirfactory.harmonia.paradeigma.common.model.ExecutionProfile;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "paradeigma.lms")
public class LmsConfig {

    private int inboundAdtPort = 2202; // PD-06 LMS-ADT-OUT (inbound listener for ADT)
    private int inboundOrmPort = 2204; // PD-08 LMS-ORM-OUT (inbound listener for Lab ORM)
    private String harmoniaHost = "localhost";
    private int harmoniaOruPort = 2102; // PD-02 LMS-ORU-IN (outbound target)

    private boolean autoProduceResults = true;
    private long resultDelayMs = 1000L; // Delay before returning result

    private ExecutionProfile profile = ExecutionProfile.TEST;
    private long seed = 12345L;

    private FaultInjectionConfig faultInjection = new FaultInjectionConfig();

    public LmsConfig() {
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

    public boolean isAutoProduceResults() {
        return autoProduceResults;
    }

    public void setAutoProduceResults(boolean autoProduceResults) {
        this.autoProduceResults = autoProduceResults;
    }

    public long getResultDelayMs() {
        return resultDelayMs;
    }

    public void setResultDelayMs(long resultDelayMs) {
        this.resultDelayMs = resultDelayMs;
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
