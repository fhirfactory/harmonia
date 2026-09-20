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
 * Health provider for Pylai protocol gateways (MLLP Inbound/Outbound, FHIR REST Registry).
 */
@ApplicationScoped
public class PylaiHealthProvider extends AbstractSubsystemHealthProvider {

    public PylaiHealthProvider() {
        super(
                "pylai",
                "Pylai",
                "Harmonia Inbound/Outbound protocol gateways (MLLP, FHIR REST Registry)",
                "1.0.0-SNAPSHOT"
        );
    }

    @Override
    public OperationalHealth getOperationalHealth() {
        String status = determineState();
        List<DependencyHealth> dependencies = new ArrayList<>();
        dependencies.add(resolveDependencyHealth("petasos", "Petasos"));
        dependencies.add(resolveDependencyHealth("mnemosyne", "Mnemosyne"));
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
            health.getDetails().put("mllpInboundPort", 2575);
            health.getDetails().put("fhirRegistryPort", 8084);
        }
        return health;
    }

    @Override
    public Map<String, TimeSeries> getStatistics(String window) {
        Map<String, TimeSeries> stats = new LinkedHashMap<>();
        stats.put("events_in", createEmptyTimeSeries("Events In", window, "msg/s"));
        stats.put("events_out", createEmptyTimeSeries("Events Out", window, "msg/s"));
        stats.put("p95_latency", createEmptyTimeSeries("P95 Latency", window, "ms"));
        stats.put("active_connections", createEmptyTimeSeries("Active Connections", window, "count"));
        return stats;
    }
}
