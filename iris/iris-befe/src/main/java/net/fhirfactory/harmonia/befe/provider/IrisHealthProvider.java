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

package net.fhirfactory.harmonia.befe.provider;

import jakarta.enterprise.context.ApplicationScoped;
import net.fhirfactory.harmonia.befe.model.operations.DependencyHealth;
import net.fhirfactory.harmonia.befe.model.operations.OperationalHealth;
import net.fhirfactory.harmonia.befe.model.operations.TimeSeries;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Health provider for Iris presentation services (BEFE WildFly presentation gateway and console SPA).
 */
@ApplicationScoped
public class IrisHealthProvider extends AbstractSubsystemHealthProvider {

    public IrisHealthProvider() {
        super(
                "iris",
                "Iris",
                "Harmonia presentation services (BEFE WildFly gateway and Vue 3 operations console)",
                "1.0.0-SNAPSHOT"
        );
    }

    @Override
    protected String determineState() {
        // Iris BEFE is self-monitoring
        return "HEALTHY";
    }

    @Override
    public OperationalHealth getOperationalHealth() {
        String status = determineState();
        List<DependencyHealth> dependencies = new ArrayList<>();
        dependencies.add(resolveDependencyHealth("mneme", "Mneme"));
        dependencies.add(resolveDependencyHealth("themis", "Themis"));

        OperationalHealth health = new OperationalHealth(
                subsystemId,
                status,
                calculateAvailabilityPercent(),
                0,
                calculateRestartCount(),
                calculateP95LatencyMs(),
                formatDependenciesSummary(dependencies)
        );
        health.setDependencies(dependencies);
        if ("HEALTHY".equalsIgnoreCase(status) || "DEGRADED".equalsIgnoreCase(status)) {
            health.getDetails().put("fhirPort", 8080);
            health.getDetails().put("operationsPort", 8090);
            health.getDetails().put("serverRuntime", "WildFly 31 / Jakarta EE 10");
        }
        return health;
    }

    @Override
    public Map<String, TimeSeries> getStatistics(String window) {
        Map<String, TimeSeries> stats = new LinkedHashMap<>();
        stats.put("api_requests", createEmptyTimeSeries("API Requests", window, "req/s"));
        stats.put("active_sessions", createEmptyTimeSeries("Active Sessions", window, "count"));
        stats.put("error_rate", createEmptyTimeSeries("Error Rate", window, "%"));
        stats.put("p95_latency", createEmptyTimeSeries("P95 Latency", window, "ms"));
        return stats;
    }
}
