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
 * Health provider for Mnemosyne (authoritative persistent storage, FHIR R5 persistence, operations datastore).
 * Complies strictly with Invariant 3 (Iris Decoupling): Monitors persistence via REST and clustered telemetry
 * with zero JPA, Hibernate, or PostgreSQL driver imports.
 */
@ApplicationScoped
public class MnemosyneHealthProvider extends AbstractSubsystemHealthProvider {

    public MnemosyneHealthProvider() {
        super(
                "mnemosyne",
                "Mnemosyne",
                "Harmonia persistent authoritative storage (HAPI FHIR R5 & operations persistence)",
                "1.0.0-SNAPSHOT"
        );
    }

    @Override
    public OperationalHealth getOperationalHealth() {
        String status = determineState();
        List<DependencyHealth> dependencies = new ArrayList<>();
        if ("UNAVAILABLE".equalsIgnoreCase(status)) {
            dependencies.add(new DependencyHealth("PostgreSQL Clinical DB", "UNAVAILABLE", null, "Subsystem unavailable"));
            dependencies.add(new DependencyHealth("PostgreSQL Ops DB", "UNAVAILABLE", null, "Subsystem unavailable"));
        } else if ("UNKNOWN".equalsIgnoreCase(status)) {
            dependencies.add(new DependencyHealth("PostgreSQL Clinical DB", "UNKNOWN", null, "Database connection unverified"));
            dependencies.add(new DependencyHealth("PostgreSQL Ops DB", "UNKNOWN", null, "Database connection unverified"));
        } else {
            dependencies.add(new DependencyHealth("PostgreSQL Clinical DB", "UNKNOWN", null, "Database pool live probe pending Step 2"));
            dependencies.add(new DependencyHealth("PostgreSQL Ops DB", "UNKNOWN", null, "Database pool live probe pending Step 2"));
        }
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
            health.getDetails().put("fhirEndpointPort", 8081);
            health.getDetails().put("operationsPort", 8085);
            health.getDetails().put("fhirVersion", "R5 (5.0.0)");
        }
        return health;
    }

    @Override
    public Map<String, TimeSeries> getStatistics(String window) {
        Map<String, TimeSeries> stats = new LinkedHashMap<>();
        stats.put("fhir_transactions", createEmptyTimeSeries("FHIR Transactions", window, "tx/s"));
        stats.put("db_query_latency", createEmptyTimeSeries("DB Query Latency", window, "ms"));
        stats.put("commit_rate", createEmptyTimeSeries("Commit Rate", window, "ops/s"));
        stats.put("p95_latency", createEmptyTimeSeries("P95 Latency", window, "ms"));
        return stats;
    }
}
