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
import jakarta.inject.Inject;
import net.fhirfactory.harmonia.befe.model.operations.DependencyHealth;
import net.fhirfactory.harmonia.befe.model.operations.OperationalHealth;
import net.fhirfactory.harmonia.befe.model.operations.TimeSeries;
import org.infinispan.client.hotrod.RemoteCacheManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Health provider for Mneme (Infinispan clustered in-memory data grid and caching layer).
 */
@ApplicationScoped
public class MnemeHealthProvider extends AbstractSubsystemHealthProvider {

    @Inject
    private RemoteCacheManager remoteCacheManager;

    public MnemeHealthProvider() {
        super(
                "mneme",
                "Mneme",
                "Harmonia Infinispan clustered in-memory caching and state grid",
                "15.0.3.Final"
        );
    }

    @Override
    protected String determineState() {
        if (remoteCacheManager != null && remoteCacheManager.isStarted()) {
            return "HEALTHY";
        }
        return super.determineState();
    }

    @Override
    public OperationalHealth getOperationalHealth() {
        String status = determineState();
        boolean connected = remoteCacheManager != null && remoteCacheManager.isStarted();

        List<DependencyHealth> dependencies = new ArrayList<>();
        if ("UNAVAILABLE".equalsIgnoreCase(status)) {
            dependencies.add(new DependencyHealth(
                    "Infinispan Cluster Grid",
                    "UNAVAILABLE",
                    null,
                    "Mneme service unavailable; cache grid unreachable"
            ));
        } else if ("UNKNOWN".equalsIgnoreCase(status)) {
            dependencies.add(new DependencyHealth(
                    "Infinispan Cluster Grid",
                    connected ? "HEALTHY" : "UNKNOWN",
                    null,
                    connected ? "Hot Rod protocol 3.1 connected on port 11222" : "Cache grid connection unverified"
            ));
        } else {
            dependencies.add(new DependencyHealth(
                    "Infinispan Cluster Grid",
                    connected ? "HEALTHY" : "DEGRADED",
                    null,
                    connected ? "Hot Rod protocol 3.1 connected on port 11222" : "Running on local fallback cache"
            ));
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
            health.getDetails().put("hotRodPort", 11222);
            health.getDetails().put("cacheNames", List.of("modulestatus-cache", "task-cache", "tasksequence-cache", "messagequeue-cache"));
        }
        return health;
    }

    @Override
    public Map<String, TimeSeries> getStatistics(String window) {
        Map<String, TimeSeries> stats = new LinkedHashMap<>();
        stats.put("cache_reads", createEmptyTimeSeries("Cache Reads", window, "ops/s"));
        stats.put("cache_writes", createEmptyTimeSeries("Cache Writes", window, "ops/s"));
        stats.put("hit_ratio", createEmptyTimeSeries("Hit Ratio", window, "%"));
        stats.put("p95_latency", createEmptyTimeSeries("P95 Latency", window, "ms"));
        return stats;
    }

    public void setRemoteCacheManager(RemoteCacheManager remoteCacheManager) {
        this.remoteCacheManager = remoteCacheManager;
    }
}
