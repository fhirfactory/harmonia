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
import net.fhirfactory.harmonia.befe.model.operations.OperationalSubsystem;
import net.fhirfactory.harmonia.befe.model.operations.TimeSeries;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Health provider for Energeia workflow and activity services,
 * encapsulating Ponos (WorkEngine & Erga) and Praxis (Workflow Orchestration).
 */
@ApplicationScoped
public class EnergeiaHealthProvider extends AbstractSubsystemHealthProvider {

    public EnergeiaHealthProvider() {
        super(
                "energeia",
                "Energeia",
                "Harmonia task processing (Ponos), Ergon activity units, and Workflow orchestration (Praxis)",
                "1.0.0-SNAPSHOT"
        );
    }

    @Override
    protected List<OperationalSubsystem> getChildSubsystems() {
        List<OperationalSubsystem> children = new ArrayList<>();
        children.add(new OperationalSubsystem(
                "ponos",
                "Ponos WorkEngine",
                "Task processing and Ergon activity execution engine",
                determineState(),
                1,
                "1.0.0-SNAPSHOT",
                System.currentTimeMillis()
        ));
        children.add(new OperationalSubsystem(
                "praxis",
                "Praxis Workflow",
                "Workflow sequence definitions and Pragma envelope lifecycle",
                determineState(),
                1,
                "1.0.0-SNAPSHOT",
                System.currentTimeMillis()
        ));
        return children;
    }

    @Override
    public OperationalHealth getOperationalHealth() {
        String status = determineState();
        List<DependencyHealth> dependencies = new ArrayList<>();
        dependencies.add(resolveDependencyHealth("petasos", "Petasos"));
        dependencies.add(resolveDependencyHealth("mneme", "Mneme"));
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
            health.getDetails().put("ponosActuatorPort", 8083);
        }
        return health;
    }

    @Override
    public Map<String, TimeSeries> getStatistics(String window) {
        Map<String, TimeSeries> stats = new LinkedHashMap<>();
        stats.put("active_executions", createEmptyTimeSeries("Active Executions", window, "count"));
        stats.put("tasks_processed", createEmptyTimeSeries("Tasks Processed", window, "tasks/s"));
        stats.put("tasks_queued", createEmptyTimeSeries("Tasks Queued", window, "count"));
        stats.put("p95_duration", createEmptyTimeSeries("P95 Duration", window, "ms"));
        return stats;
    }
}
