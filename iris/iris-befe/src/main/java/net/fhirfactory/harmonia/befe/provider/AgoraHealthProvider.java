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
 * Health provider for Agora (Matrix/Synapse collaboration projection, AS transaction ingestion, room lifecycle).
 */
@ApplicationScoped
public class AgoraHealthProvider extends AbstractSubsystemHealthProvider {

    public AgoraHealthProvider() {
        super(
                "agora",
                "Agora",
                "Harmonia Matrix/Synapse collaboration projection, AS transaction ingestion, room/space lifecycle",
                "1.0.0-SNAPSHOT"
        );
    }

    @Override
    public OperationalHealth getOperationalHealth() {
        String status = determineState();
        List<DependencyHealth> dependencies = new ArrayList<>();
        if ("UNAVAILABLE".equalsIgnoreCase(status)) {
            dependencies.add(new DependencyHealth("Synapse Homeserver", "UNAVAILABLE", null, "Subsystem unavailable"));
        } else if ("UNKNOWN".equalsIgnoreCase(status)) {
            dependencies.add(new DependencyHealth("Synapse Homeserver", "UNKNOWN", null, "Synapse connection unverified"));
        } else {
            dependencies.add(new DependencyHealth("Synapse Homeserver", "UNKNOWN", null, "Synapse live probe pending Step 2"));
        }
        dependencies.add(resolveDependencyHealth("petasos", "Petasos"));
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
            health.getDetails().put("asServicePort", 8092);
            health.getDetails().put("matrixServer", "matrix.harmonia.local");
        }
        return health;
    }

    @Override
    public Map<String, TimeSeries> getStatistics(String window) {
        Map<String, TimeSeries> stats = new LinkedHashMap<>();
        stats.put("matrix_tx_rate", createEmptyTimeSeries("Matrix Transaction Rate", window, "tx/s"));
        stats.put("room_events", createEmptyTimeSeries("Room Events", window, "events/s"));
        stats.put("active_rooms", createEmptyTimeSeries("Active Rooms", window, "count"));
        stats.put("p95_latency", createEmptyTimeSeries("P95 Latency", window, "ms"));
        return stats;
    }
}
