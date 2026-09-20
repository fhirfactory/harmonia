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
import net.fhirfactory.harmonia.model.status.ModuleStatus;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        Optional<ModuleStatus> statusOpt = findModuleStatus();

        if ("UNAVAILABLE".equalsIgnoreCase(status)) {
            dependencies.add(new DependencyHealth("Artemis Broker", "UNAVAILABLE", null, "Subsystem unavailable"));
            dependencies.add(new DependencyHealth("Calliope", "UNAVAILABLE", null, "Subsystem unavailable"));
        } else if ("UNKNOWN".equalsIgnoreCase(status)) {
            dependencies.add(new DependencyHealth("Artemis Broker", "UNKNOWN", null, "Broker telemetry probe not connected"));
            dependencies.add(new DependencyHealth("Calliope", "UNKNOWN", null, "Calliope status unverified"));
        } else {
            String brokerStatus = "HEALTHY";
            String brokerMsg = "Connected to Artemis standalone broker at port 61616";
            if (statusOpt.isPresent()) {
                ModuleStatus ms = statusOpt.get();
                if (ms.getDetails() != null) {
                    if (ms.getDetails().containsKey("brokerStatus")) {
                        brokerStatus = String.valueOf(ms.getDetails().get("brokerStatus"));
                    }
                    if (ms.getDetails().containsKey("brokerMessage")) {
                        brokerMsg = String.valueOf(ms.getDetails().get("brokerMessage"));
                    } else if (ms.getDetails().containsKey("connectedBroker")) {
                        brokerMsg = "Connected to Artemis broker at " + ms.getDetails().get("connectedBroker");
                    }
                }
                if (!ms.isReady()) {
                    brokerStatus = "DEGRADED";
                }
            }
            if ("DEGRADED".equalsIgnoreCase(status) && "HEALTHY".equalsIgnoreCase(brokerStatus)) {
                brokerStatus = "DEGRADED";
                brokerMsg = "Broker connection degraded or heartbeat delayed";
            }
            dependencies.add(new DependencyHealth("Artemis Broker", brokerStatus, null, brokerMsg));
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
            String topology = "Standalone Single-Broker";
            if (statusOpt.isPresent() && statusOpt.get().getDetails() != null) {
                Object topObj = statusOpt.get().getDetails().get("brokerTopology");
                if (topObj != null && !String.valueOf(topObj).isBlank()) {
                    topology = String.valueOf(topObj);
                }
                Object brokerObj = statusOpt.get().getDetails().get("connectedBroker");
                if (brokerObj != null) {
                    health.getDetails().put("connectedBroker", brokerObj);
                }
                Object stateObj = statusOpt.get().getDetails().get("connectionState");
                if (stateObj != null) {
                    health.getDetails().put("connectionState", stateObj);
                }
            }
            health.getDetails().put("brokerTopology", topology);
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
