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

package net.fhirfactory.harmonia.paradeigma.scenarios.config;

import net.fhirfactory.harmonia.paradeigma.common.model.ExecutionProfile;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "paradeigma.scenarios")
public class ScenarioEngineConfig {

    private String pasBaseUrl = "http://localhost:8091";
    private String emrBaseUrl = "http://localhost:8092";
    private String lmsBaseUrl = "http://localhost:8093";
    private String rispacBaseUrl = "http://localhost:8094";

    private ExecutionProfile profile = ExecutionProfile.TEST;
    private long seed = 12345L;
    private int concurrentJourneys = 1;
    private long stepPacingMs = 50L;

    public ScenarioEngineConfig() {
    }

    public String getPasBaseUrl() {
        return pasBaseUrl;
    }

    public void setPasBaseUrl(String pasBaseUrl) {
        this.pasBaseUrl = pasBaseUrl;
    }

    public String getEmrBaseUrl() {
        return emrBaseUrl;
    }

    public void setEmrBaseUrl(String emrBaseUrl) {
        this.emrBaseUrl = emrBaseUrl;
    }

    public String getLmsBaseUrl() {
        return lmsBaseUrl;
    }

    public void setLmsBaseUrl(String lmsBaseUrl) {
        this.lmsBaseUrl = lmsBaseUrl;
    }

    public String getRispacBaseUrl() {
        return rispacBaseUrl;
    }

    public void setRispacBaseUrl(String rispacBaseUrl) {
        this.rispacBaseUrl = rispacBaseUrl;
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

    public int getConcurrentJourneys() {
        return concurrentJourneys;
    }

    public void setConcurrentJourneys(int concurrentJourneys) {
        this.concurrentJourneys = concurrentJourneys;
    }

    public long getStepPacingMs() {
        return stepPacingMs;
    }

    public void setStepPacingMs(long stepPacingMs) {
        this.stepPacingMs = stepPacingMs;
    }
}
