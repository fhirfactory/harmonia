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

package net.fhirfactory.harmonia.petasos.api.topology;

import java.io.Serializable;
import java.util.*;

/**
 * Snapshot representation of the active Petasos / Artemis cluster and replication topology.
 */
public final class PetasosBrokerTopology implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String activeNodeId;
    private final String activeBrokerName;
    private final List<BrokerNodeInfo> nodes;
    private final Map<String, List<String>> replicationPairs;

    public PetasosBrokerTopology(
            String activeNodeId,
            String activeBrokerName,
            List<BrokerNodeInfo> nodes,
            Map<String, List<String>> replicationPairs) {
        this.activeNodeId = activeNodeId;
        this.activeBrokerName = activeBrokerName;
        this.nodes = nodes != null ? Collections.unmodifiableList(new ArrayList<>(nodes)) : Collections.emptyList();
        this.replicationPairs = replicationPairs != null
                ? Collections.unmodifiableMap(new LinkedHashMap<>(replicationPairs))
                : Collections.emptyMap();
    }

    public String getActiveNodeId() {
        return activeNodeId;
    }

    public String getActiveBrokerName() {
        return activeBrokerName;
    }

    public List<BrokerNodeInfo> getNodes() {
        return nodes;
    }

    public Map<String, List<String>> getReplicationPairs() {
        return replicationPairs;
    }

    public int getActiveNodeCount() {
        return (int) nodes.stream().filter(BrokerNodeInfo::isLive).count();
    }

    public Optional<BrokerNodeInfo> getActiveNode() {
        if (activeNodeId == null) {
            return Optional.empty();
        }
        return nodes.stream()
                .filter(n -> activeNodeId.equals(n.getNodeId()))
                .findFirst();
    }

    @Override
    public String toString() {
        return "PetasosBrokerTopology{" +
                "activeNodeId='" + activeNodeId + '\'' +
                ", activeBrokerName='" + activeBrokerName + '\'' +
                ", totalNodes=" + nodes.size() +
                ", liveNodes=" + getActiveNodeCount() +
                ", replicationPairs=" + replicationPairs +
                '}';
    }
}
