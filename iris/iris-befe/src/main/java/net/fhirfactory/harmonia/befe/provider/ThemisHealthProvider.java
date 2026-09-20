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
 * Health provider for Themis (security governance, RBAC/ABAC evaluation, and security auditing).
 */
@ApplicationScoped
public class ThemisHealthProvider extends AbstractSubsystemHealthProvider {

    public ThemisHealthProvider() {
        super(
                "themis",
                "Themis",
                "Harmonia policy evaluation, RBAC/ABAC authorization, and non-PHI security auditing",
                "1.0.0-SNAPSHOT"
        );
    }

    @Override
    public OperationalHealth getOperationalHealth() {
        String status = determineState();
        List<DependencyHealth> dependencies = new ArrayList<>();
        if ("UNAVAILABLE".equalsIgnoreCase(status)) {
            dependencies.add(new DependencyHealth("Calliope", "UNAVAILABLE", null, "Subsystem unavailable"));
        } else if ("UNKNOWN".equalsIgnoreCase(status)) {
            dependencies.add(new DependencyHealth("Calliope", "UNKNOWN", null, "Calliope status unverified"));
        } else {
            dependencies.add(new DependencyHealth("Calliope", "HEALTHY", null, "Security labels and schema definitions bound (embedded)"));
        }

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
            health.getDetails().put("engine", "DeterministicPolicyEvaluator");
            health.getDetails().put("defaultPolicy", "DEFAULT-DENY");
        }
        return health;
    }

    @Override
    public Map<String, TimeSeries> getStatistics(String window) {
        Map<String, TimeSeries> stats = new LinkedHashMap<>();
        stats.put("evaluations_rate", createEmptyTimeSeries("Evaluations Rate", window, "eval/s"));
        stats.put("denials_rate", createEmptyTimeSeries("Denials Rate", window, "count/s"));
        stats.put("policy_cache_hit_rate", createEmptyTimeSeries("Policy Cache Hit Rate", window, "%"));
        stats.put("p95_latency", createEmptyTimeSeries("P95 Latency", window, "ms"));
        return stats;
    }
}
