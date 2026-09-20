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

package net.fhirfactory.harmonia.praxis.service;

import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import net.fhirfactory.harmonia.petasos.api.Petasos;
import net.fhirfactory.harmonia.petasos.api.health.ConnectionState;
import net.fhirfactory.harmonia.petasos.api.health.HealthStatus;
import net.fhirfactory.harmonia.petasos.api.health.PetasosHealth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Publishes live Petasos broker health into the shared modulestatus-cache so Iris BEFE
 * ({@code PetasosHealthProvider}) can surface broker status, connection state, and topology
 * without probing Artemis directly.
 */
@ApplicationScoped
public class PetasosModuleStatusPublisher {

    private static final Logger log = LoggerFactory.getLogger(PetasosModuleStatusPublisher.class);

    public static final String MODULE_ID = "petasos";
    public static final String MODULE_NAME = "Petasos Messaging";
    public static final String MODULE_TYPE = "MESSAGING";
    public static final long DEFAULT_HEARTBEAT_SECONDS = 30L;

    @Inject
    private Petasos petasos;

    @Inject
    private ModuleStatusService moduleStatusService;

    private final AtomicBoolean started = new AtomicBoolean(false);
    private ScheduledExecutorService scheduler;
    private long heartbeatSeconds = DEFAULT_HEARTBEAT_SECONDS;

    public void onStartup(@Observes @Initialized(ApplicationScoped.class) Object init) {
        start();
    }

    public synchronized void start() {
        if (!started.compareAndSet(false, true)) {
            return;
        }
        publishOnce();
        scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "petasos-module-status-publisher");
            t.setDaemon(true);
            return t;
        });
        scheduler.scheduleAtFixedRate(this::publishSafely, heartbeatSeconds, heartbeatSeconds, TimeUnit.SECONDS);
        log.info("Started Petasos module-status publisher (moduleId={}, heartbeat={}s)", MODULE_ID, heartbeatSeconds);
    }

    public synchronized void stop() {
        if (!started.compareAndSet(true, false)) {
            return;
        }
        if (scheduler != null) {
            scheduler.shutdownNow();
            scheduler = null;
        }
        if (moduleStatusService != null) {
            try {
                moduleStatusService.unregisterModule(MODULE_ID);
            } catch (Exception e) {
                log.debug("Failed to unregister petasos module status on shutdown: {}", e.getMessage());
            }
        }
    }

    @PreDestroy
    public void onShutdown() {
        stop();
    }

    public void publishOnce() {
        if (moduleStatusService == null) {
            log.debug("ModuleStatusService unavailable; skipping petasos health publish");
            return;
        }
        if (petasos == null) {
            moduleStatusService.registerModule(
                    MODULE_ID,
                    MODULE_NAME,
                    MODULE_TYPE,
                    "STARTING",
                    false,
                    Map.of(
                            "brokerStatus", "UNKNOWN",
                            "brokerMessage", "Petasos client not initialized",
                            "connectionState", ConnectionState.DISCONNECTED.name(),
                            "brokerTopology", "Standalone Single-Broker"
                    )
            );
            return;
        }

        PetasosHealth health = petasos.health();
        Map<String, Object> details = toModuleDetails(health);
        boolean ready = health != null && health.isHealthy();
        String status = mapStatus(health);

        moduleStatusService.registerModule(MODULE_ID, MODULE_NAME, MODULE_TYPE, status, ready, details);
        log.debug("Published petasos ModuleStatus status={} ready={} broker={}",
                status, ready, details.get("connectedBroker"));
    }

    static Map<String, Object> toModuleDetails(PetasosHealth health) {
        Map<String, Object> details = new LinkedHashMap<>();
        if (health == null) {
            details.put("brokerStatus", "UNKNOWN");
            details.put("brokerMessage", "Petasos health unavailable");
            details.put("connectionState", ConnectionState.DISCONNECTED.name());
            details.put("brokerTopology", "Standalone Single-Broker");
            return details;
        }

        String connectedBroker = health.getConnectedBroker();
        ConnectionState connectionState = health.getConnectionState();
        HealthStatus healthStatus = health.getStatus();

        details.put("brokerStatus", mapBrokerStatus(healthStatus));
        details.put("connectionState", connectionState != null ? connectionState.name() : ConnectionState.DISCONNECTED.name());
        details.put("brokerTopology", resolveTopology(health));
        details.put("reconnectCount", health.getReconnectCount());

        if (connectedBroker != null && !connectedBroker.isBlank()) {
            details.put("connectedBroker", connectedBroker);
            details.put("brokerMessage", "Connected to Artemis broker at " + connectedBroker);
        } else {
            details.put("brokerMessage", "Petasos broker connection state=" + details.get("connectionState"));
        }

        if (health.getActiveNodeId() != null) {
            details.put("activeNodeId", health.getActiveNodeId());
        }
        if (health.getDetails() != null) {
            Object primaryUrl = health.getDetails().get("primaryUrl");
            if (primaryUrl != null) {
                details.put("primaryUrl", primaryUrl);
            }
            Object haEnabled = health.getDetails().get("haEnabled");
            if (haEnabled != null) {
                details.put("haEnabled", haEnabled);
            }
            Object configuredUrls = health.getDetails().get("configuredUrls");
            if (configuredUrls != null) {
                details.put("configuredUrls", configuredUrls);
            }
            Object discovered = health.getDetails().get("discoveredClusterNodes");
            if (discovered != null) {
                details.put("discoveredClusterNodes", discovered);
            }
        }
        return details;
    }

    static String mapStatus(PetasosHealth health) {
        if (health == null || health.getStatus() == null) {
            return "ERROR";
        }
        return switch (health.getStatus()) {
            case UP -> "READY";
            case DEGRADED -> "DEGRADED";
            case DOWN -> "ERROR";
        };
    }

    static String mapBrokerStatus(HealthStatus status) {
        if (status == null) {
            return "UNKNOWN";
        }
        return switch (status) {
            case UP -> "HEALTHY";
            case DEGRADED -> "DEGRADED";
            case DOWN -> "UNAVAILABLE";
        };
    }

    static String resolveTopology(PetasosHealth health) {
        if (health != null && health.getDetails() != null) {
            Object haEnabled = health.getDetails().get("haEnabled");
            if (haEnabled instanceof Boolean enabled && enabled) {
                Object discovered = health.getDetails().get("discoveredClusterNodes");
                if (discovered instanceof Number n && n.intValue() > 1) {
                    return "HA Cluster (" + n.intValue() + " nodes)";
                }
                return "HA Enabled";
            }
        }
        return "Standalone Single-Broker";
    }

    private void publishSafely() {
        try {
            publishOnce();
        } catch (Exception e) {
            log.warn("Failed to publish petasos module status: {}", e.getMessage());
        }
    }

    public void setPetasos(Petasos petasos) {
        this.petasos = petasos;
    }

    public void setModuleStatusService(ModuleStatusService moduleStatusService) {
        this.moduleStatusService = moduleStatusService;
    }

    public void setHeartbeatSeconds(long heartbeatSeconds) {
        if (heartbeatSeconds > 0) {
            this.heartbeatSeconds = heartbeatSeconds;
        }
    }
}
