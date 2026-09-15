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

package net.fhirfactory.harmonia.petasos.api.health;

import java.io.Serializable;
import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Health and operational status report for Petasos messaging subsystem.
 */
public final class PetasosHealth implements Serializable {

    private static final long serialVersionUID = 1L;

    private final HealthStatus status;
    private final ConnectionState connectionState;
    private final String connectedBroker;
    private final String activeNodeId;
    private final long reconnectCount;
    private final Instant timestamp;
    private final Map<String, Object> details;

    public PetasosHealth(
            HealthStatus status,
            ConnectionState connectionState,
            String connectedBroker,
            String activeNodeId,
            long reconnectCount,
            Instant timestamp,
            Map<String, Object> details) {
        this.status = Objects.requireNonNull(status, "status must not be null");
        this.connectionState = Objects.requireNonNull(connectionState, "connectionState must not be null");
        this.connectedBroker = connectedBroker;
        this.activeNodeId = activeNodeId;
        this.reconnectCount = reconnectCount;
        this.timestamp = timestamp != null ? timestamp : Instant.now();
        this.details = details != null ? Collections.unmodifiableMap(new LinkedHashMap<>(details)) : Collections.emptyMap();
    }

    public static PetasosHealth up(String connectedBroker, String activeNodeId, long reconnectCount, Map<String, Object> details) {
        return new PetasosHealth(HealthStatus.UP, ConnectionState.CONNECTED, connectedBroker, activeNodeId, reconnectCount, Instant.now(), details);
    }

    public static PetasosHealth degraded(ConnectionState state, String connectedBroker, String activeNodeId, long reconnectCount, Map<String, Object> details) {
        return new PetasosHealth(HealthStatus.DEGRADED, state, connectedBroker, activeNodeId, reconnectCount, Instant.now(), details);
    }

    public static PetasosHealth down(ConnectionState state, String lastKnownBroker, long reconnectCount, Map<String, Object> details) {
        return new PetasosHealth(HealthStatus.DOWN, state, lastKnownBroker, null, reconnectCount, Instant.now(), details);
    }

    public HealthStatus getStatus() {
        return status;
    }

    public ConnectionState getConnectionState() {
        return connectionState;
    }

    public String getConnectedBroker() {
        return connectedBroker;
    }

    public String getActiveNodeId() {
        return activeNodeId;
    }

    public long getReconnectCount() {
        return reconnectCount;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public boolean isHealthy() {
        return status == HealthStatus.UP;
    }

    @Override
    public String toString() {
        return "PetasosHealth{" +
                "status=" + status +
                ", connectionState=" + connectionState +
                ", connectedBroker='" + connectedBroker + '\'' +
                ", activeNodeId='" + activeNodeId + '\'' +
                ", reconnectCount=" + reconnectCount +
                ", timestamp=" + timestamp +
                ", details=" + details +
                '}';
    }
}
