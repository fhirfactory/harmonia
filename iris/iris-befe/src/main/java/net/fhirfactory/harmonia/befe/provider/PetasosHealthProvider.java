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
 * Health provider for Petasos messaging, event transport, and ActiveMQ Artemis broker.
 */
@ApplicationScoped
public class PetasosHealthProvider extends AbstractSubsystemHealthProvider {

    public PetasosHealthProvider() {
        super(
                "petasos",
                "Petasos",
                "Harmonia messaging, event transport, and ActiveMQ Artemis broker",
                "1.0.0-SNAPSHOT"
        );
    }

    @Override
    public OperationalHealth getOperationalHealth() {
        String status = determineState();
        List<DependencyHealth> dependencies = new ArrayList<>();
        if ("UNAVAILABLE".equalsIgnoreCase(status)) {
            dependencies.add(new DependencyHealth("Artemis Broker", "UNAVAILABLE", null, "Subsystem unavailable"));
            dependencies.add(new DependencyHealth("Calliope", "UNAVAILABLE", null, "Subsystem unavailable"));
        } else if ("UNKNOWN".equalsIgnoreCase(status)) {
            dependencies.add(new DependencyHealth("Artemis Broker", "UNKNOWN", null, "Broker telemetry probe not connected"));
            dependencies.add(new DependencyHealth("Calliope", "UNKNOWN", null, "Calliope status unverified"));
        } else {
            // Broker live probe is Step 2; report UNKNOWN without fake healthy metrics
            dependencies.add(new DependencyHealth("Artemis Broker", "UNKNOWN", null, "Broker live probe pending Step 2"));
            // Calliope is embedded model foundation in classpath
            dependencies.add(new DependencyHealth("Calliope", "HEALTHY", null, "Topic taxonomy loaded (embedded)"));
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
            health.getDetails().put("brokerPort", 61616);
            health.getDetails().put("brokerTopology", "HA Master-Slave Replicated");
        }
        return health;
    }

    @Override
    public Map<String, TimeSeries> getStatistics(String window) {
        Map<String, TimeSeries> stats = new LinkedHashMap<>();
        stats.put("enqueue_rate", createEmptyTimeSeries("Enqueue Rate", window, "msg/s"));
        stats.put("dequeue_rate", createEmptyTimeSeries("Dequeue Rate", window, "msg/s"));
        stats.put("messages_in_flight", createEmptyTimeSeries("Messages in Flight", window, "count"));
        stats.put("dlq_depth", createEmptyTimeSeries("DLQ Depth", window, "count"));
        return stats;
    }
}
