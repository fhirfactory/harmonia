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

package net.fhirfactory.harmonia.paradeigma.scenarios.registry;

import net.fhirfactory.harmonia.paradeigma.common.generator.SyntheticProviderRegistryGenerator;
import net.fhirfactory.harmonia.paradeigma.common.security.SecurityScenarioContext;
import net.fhirfactory.harmonia.paradeigma.scenarios.model.ProviderRegistryScenarioResult;
import net.fhirfactory.harmonia.paradeigma.scenarios.model.ScenarioExpectation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Orchestration engine for running, tracking, and reporting Provider Registry simulation scenarios.
 */
@Service
public class ProviderRegistryScenarioEngine {

    private static final Logger LOG = LoggerFactory.getLogger(ProviderRegistryScenarioEngine.class);

    private final AtomicLong executedScenarioCount = new AtomicLong(0);
    private final AtomicLong successfulScenarioCount = new AtomicLong(0);
    private final AtomicLong failedScenarioCount = new AtomicLong(0);

    public ProviderRegistryScenarioResult runSoloPractitionerScenario(long seed) {
        LOG.info("Running Solo Practitioner scenario: seed={}", seed);
        ProviderRegistryJourneyScenario scenario = new ProviderRegistryJourneyScenario(
                "scen-solo-" + seed,
                "Solo Practitioner Scenario",
                "Simulates creation and lifecycle of a solo practitioner with organization and role",
                seed,
                SecurityScenarioContext.providerSteward(),
                ScenarioExpectation.success(),
                new SyntheticProviderRegistryGenerator(seed).generateSoloPractitioner(seed)
        );
        return recordExecution(scenario.execute());
    }

    public ProviderRegistryScenarioResult runProviderNetworkScenario(long seed) {
        LOG.info("Running Connected Provider Network scenario: seed={}", seed);
        ProviderRegistryJourneyScenario scenario = new ProviderRegistryJourneyScenario(
                "scen-network-" + seed,
                "Connected Provider Network Scenario",
                "Simulates creation of an entire connected healthcare network with organizations, services, locations, practitioners, roles, and groups",
                seed,
                SecurityScenarioContext.providerSteward(),
                ScenarioExpectation.success(),
                new SyntheticProviderRegistryGenerator(seed).generateProviderNetwork(seed)
        );
        return recordExecution(scenario.execute());
    }

    public ProviderRegistryScenarioResult runScenario(ProviderRegistryJourneyScenario scenario) {
        if (scenario == null) {
            throw new IllegalArgumentException("Scenario must not be null");
        }
        return recordExecution(scenario.execute());
    }

    private ProviderRegistryScenarioResult recordExecution(ProviderRegistryScenarioResult result) {
        executedScenarioCount.incrementAndGet();
        if (result.isSuccess()) {
            successfulScenarioCount.incrementAndGet();
        } else {
            failedScenarioCount.incrementAndGet();
        }
        return result;
    }

    public long getExecutedScenarioCount() {
        return executedScenarioCount.get();
    }

    public long getSuccessfulScenarioCount() {
        return successfulScenarioCount.get();
    }

    public long getFailedScenarioCount() {
        return failedScenarioCount.get();
    }
}
