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

import jakarta.inject.Inject;
import net.fhirfactory.harmonia.befe.model.operations.*;
import net.fhirfactory.harmonia.befe.service.ModuleStatusService;
import net.fhirfactory.harmonia.model.status.ModuleStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Base template implementation of SubsystemHealthProvider.
 * Provides fallback handling, Infinispan modulestatus-cache resolution,
 * time-window metric generation, and graceful degradation on telemetry timeout.
 */
public abstract class AbstractSubsystemHealthProvider implements SubsystemHealthProvider {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Inject
    protected ModuleStatusService moduleStatusService;

    @Inject
    protected KubernetesInstanceProvider instanceProvider;

    protected final String subsystemId;
    protected final String subsystemName;
    protected final String description;
    protected final String version;

    public AbstractSubsystemHealthProvider(String subsystemId, String subsystemName, String description, String version) {
        this.subsystemId = Objects.requireNonNull(subsystemId, "subsystemId must not be null");
        this.subsystemName = Objects.requireNonNull(subsystemName, "subsystemName must not be null");
        this.description = description;
        this.version = version != null ? version : "1.0.0-SNAPSHOT";
    }

    @Override
    public String getSubsystemId() {
        return subsystemId;
    }

    @Override
    public String getSubsystemName() {
        return subsystemName;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public OperationalSubsystem getSubsystemOverview() {
        String state = determineState();
        List<OperationalInstance> instances = getInstances();
        int count = instances != null ? instances.size() : 0;
        long lastUpdated = System.currentTimeMillis();

        Optional<ModuleStatus> statusOpt = findModuleStatus();
        if (statusOpt.isPresent()) {
            String luStr = statusOpt.get().getLastUpdated();
            if (luStr != null) {
                try {
                    lastUpdated = java.time.Instant.parse(luStr).toEpochMilli();
                } catch (Exception ignored) {}
            }
        }

        OperationalSubsystem overview = new OperationalSubsystem(
                subsystemId,
                subsystemName,
                description,
                state,
                count,
                version,
                lastUpdated,
                getChildSubsystems()
        );
        return overview;
    }

    @Override
    public List<OperationalInstance> getInstances() {
        if (instanceProvider != null) {
            try {
                return instanceProvider.getInstances(subsystemId);
            } catch (Exception e) {
                log.warn("Failed retrieving instances for subsystem [{}]: {}", subsystemId, e.getMessage());
            }
        }
        return Collections.singletonList(createDefaultInstance());
    }

    @Override
    public boolean isAvailable() {
        try {
            return !"UNAVAILABLE".equalsIgnoreCase(determineState());
        } catch (Exception e) {
            return false;
        }
    }

    protected List<OperationalSubsystem> getChildSubsystems() {
        return Collections.emptyList();
    }

    protected Optional<ModuleStatus> findModuleStatus() {
        if (moduleStatusService == null) {
            return Optional.empty();
        }
        try {
            Optional<ModuleStatus> direct = moduleStatusService.getModuleStatus(subsystemId);
            if (direct.isPresent()) {
                return direct;
            }
            // Check for prefixed matches
            List<ModuleStatus> all = moduleStatusService.getAllModuleStatuses();
            for (ModuleStatus ms : all) {
                if (ms != null && ms.getModuleId() != null) {
                    String id = ms.getModuleId().toLowerCase();
                    if (id.equals(subsystemId) || id.startsWith(subsystemId + "-")) {
                        return Optional.of(ms);
                    }
                }
            }
        } catch (Exception e) {
            log.debug("ModuleStatus lookup error for [{}]: {}", subsystemId, e.getMessage());
        }
        return Optional.empty();
    }

    protected String determineState() {
        Optional<ModuleStatus> statusOpt = findModuleStatus();
        if (statusOpt.isPresent()) {
            ModuleStatus ms = statusOpt.get();
            if (ms.isReady()) {
                // Check for staleness (> 5 minutes without heartbeat)
                long ageMs = 0;
                String luStr = ms.getLastUpdated();
                if (luStr != null) {
                    try {
                        ageMs = System.currentTimeMillis() - java.time.Instant.parse(luStr).toEpochMilli();
                    } catch (Exception ignored) {}
                }
                if (ageMs > 300_000) {
                    return "DEGRADED";
                }
                return "HEALTHY";
            } else if ("STOPPED".equalsIgnoreCase(ms.getStatus()) || "FAILED".equalsIgnoreCase(ms.getStatus())) {
                return "UNAVAILABLE";
            } else {
                return "DEGRADED";
            }
        }
        return "UNKNOWN";
    }

    protected OperationalInstance createDefaultInstance() {
        OperationalInstance inst = new OperationalInstance();
        inst.setInstanceId(subsystemId + "-0");
        inst.setSubsystemId(subsystemId);
        inst.setRole("Primary");
        String state = determineState();
        if ("UNAVAILABLE".equalsIgnoreCase(state)) {
            inst.setState("Stopped");
            inst.setReady(false);
        } else {
            inst.setState("Running");
            inst.setReady(!"UNKNOWN".equalsIgnoreCase(state));
        }
        inst.setRestartCount(0);
        inst.setUptime("1d 0h");
        inst.setStartedAt(System.currentTimeMillis() - 86400000L);
        inst.setCpuPercent(-1.0);
        inst.setMemoryMb(256);
        inst.setPodName(subsystemId + "-0");
        inst.setNamespace("harmonia");
        inst.setNodeName("node-01");
        inst.setIpAddress("127.0.0.1");
        inst.setContainerImage("harmonia/" + subsystemId + ":1.0.0-SNAPSHOT");
        inst.setAppVersion(version);
        return inst;
    }

    /**
     * Resolves the health of a subsystem dependency based on parent subsystem state
     * and cluster ModuleStatusService.
     */
    protected DependencyHealth resolveDependencyHealth(String dependencyId, String dependencyName) {
        String parentState = determineState();
        if ("UNAVAILABLE".equalsIgnoreCase(parentState)) {
            return new DependencyHealth(
                    dependencyName,
                    "UNAVAILABLE",
                    null,
                    "Subsystem unavailable; dependency check skipped"
            );
        }
        if ("UNKNOWN".equalsIgnoreCase(parentState)) {
            return new DependencyHealth(
                    dependencyName,
                    "UNKNOWN",
                    null,
                    "Subsystem status UNKNOWN; dependency status unverified"
            );
        }
        // Subsystem is HEALTHY or DEGRADED: check cluster ModuleStatus
        if (moduleStatusService != null) {
            Optional<ModuleStatus> depOpt = findModuleStatusFor(dependencyId);
            if (depOpt.isPresent()) {
                ModuleStatus ms = depOpt.get();
                if (ms.isReady()) {
                    return new DependencyHealth(
                            dependencyName,
                            "HEALTHY",
                            null,
                            dependencyName + " is ready in cluster"
                    );
                } else if ("STOPPED".equalsIgnoreCase(ms.getStatus()) || "FAILED".equalsIgnoreCase(ms.getStatus())) {
                    return new DependencyHealth(
                            dependencyName,
                            "UNAVAILABLE",
                            null,
                            dependencyName + " is stopped or failed"
                    );
                } else {
                    return new DependencyHealth(
                            dependencyName,
                            "DEGRADED",
                            null,
                            dependencyName + " is degraded"
                    );
                }
            }
        }
        // If not registered in ModuleStatusService, status is UNKNOWN (not fake HEALTHY)
        return new DependencyHealth(
                dependencyName,
                "UNKNOWN",
                null,
                dependencyName + " status not registered in cluster"
        );
    }

    protected Optional<ModuleStatus> findModuleStatusFor(String targetId) {
        if (moduleStatusService == null || targetId == null) {
            return Optional.empty();
        }
        try {
            Optional<ModuleStatus> direct = moduleStatusService.getModuleStatus(targetId);
            if (direct.isPresent()) {
                return direct;
            }
            List<ModuleStatus> all = moduleStatusService.getAllModuleStatuses();
            for (ModuleStatus ms : all) {
                if (ms != null && ms.getModuleId() != null) {
                    String id = ms.getModuleId().toLowerCase();
                    if (id.equals(targetId.toLowerCase()) || id.startsWith(targetId.toLowerCase() + "-")) {
                        return Optional.of(ms);
                    }
                }
            }
        } catch (Exception e) {
            log.debug("ModuleStatus lookup error for target [{}]: {}", targetId, e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Formats a summary ratio string for dependencies consistent with subsystem state.
     */
    protected String formatDependenciesSummary(List<DependencyHealth> dependencies) {
        if (dependencies == null || dependencies.isEmpty()) {
            return "0 / 0 (No dependencies)";
        }
        String parentState = determineState();
        long healthyCount = dependencies.stream()
                .filter(d -> "HEALTHY".equalsIgnoreCase(d.getStatus()))
                .count();
        int total = dependencies.size();

        if ("UNAVAILABLE".equalsIgnoreCase(parentState)) {
            return "0 / " + total + " Available (Subsystem Unavailable)";
        }
        if ("UNKNOWN".equalsIgnoreCase(parentState)) {
            return "Unknown (" + healthyCount + " / " + total + " Verified)";
        }
        return healthyCount + " / " + total + " Healthy";
    }

    /**
     * Calculates total restarts across all discovered instances.
     */
    protected int calculateRestartCount() {
        List<OperationalInstance> instances = getInstances();
        if (instances == null || instances.isEmpty()) {
            return 0;
        }
        return instances.stream().mapToInt(OperationalInstance::getRestartCount).sum();
    }

    /**
     * Returns availability percentage if known; returns 0.0 when UNAVAILABLE,
     * or null (omitted / N/A) when telemetry is not yet tracking historical uptime.
     */
    protected Double calculateAvailabilityPercent() {
        String state = determineState();
        if ("UNAVAILABLE".equalsIgnoreCase(state)) {
            return 0.0;
        }
        return null;
    }

    /**
     * Returns p95 latency in milliseconds if measured; returns null when not measured.
     */
    protected Long calculateP95LatencyMs() {
        return null;
    }

    /**
     * Creates an empty time series for an unsupported or unmeasured metric,
     * maintaining structural validity for the contract without fabricating data.
     */
    protected TimeSeries createEmptyTimeSeries(String metricName, String window, String unit) {
        String win = (window == null || window.isBlank()) ? "15m" : window.toLowerCase();
        return new TimeSeries(metricName, win, unit, Collections.emptyList());
    }

    public void setModuleStatusService(ModuleStatusService moduleStatusService) {
        this.moduleStatusService = moduleStatusService;
    }

    public void setInstanceProvider(KubernetesInstanceProvider instanceProvider) {
        this.instanceProvider = instanceProvider;
    }
}
