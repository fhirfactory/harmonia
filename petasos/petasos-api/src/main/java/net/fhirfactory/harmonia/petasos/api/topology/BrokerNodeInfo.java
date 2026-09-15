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
import java.util.Objects;

/**
 * Metadata descriptor for a single Artemis broker node in the Petasos HA cluster.
 */
public final class BrokerNodeInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String nodeId;
    private final String brokerName;
    private final String liveConnector;
    private final String backupConnector;
    private final boolean live;
    private final boolean backup;
    private final String clusterGroup;

    public BrokerNodeInfo(
            String nodeId,
            String brokerName,
            String liveConnector,
            String backupConnector,
            boolean live,
            boolean backup,
            String clusterGroup) {
        this.nodeId = nodeId;
        this.brokerName = brokerName;
        this.liveConnector = liveConnector;
        this.backupConnector = backupConnector;
        this.live = live;
        this.backup = backup;
        this.clusterGroup = clusterGroup;
    }

    public String getNodeId() {
        return nodeId;
    }

    public String getBrokerName() {
        return brokerName;
    }

    public String getLiveConnector() {
        return liveConnector;
    }

    public String getBackupConnector() {
        return backupConnector;
    }

    public boolean isLive() {
        return live;
    }

    public boolean isBackup() {
        return backup;
    }

    public String getClusterGroup() {
        return clusterGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BrokerNodeInfo that)) return false;
        return live == that.live && backup == that.backup &&
                Objects.equals(nodeId, that.nodeId) &&
                Objects.equals(brokerName, that.brokerName) &&
                Objects.equals(liveConnector, that.liveConnector) &&
                Objects.equals(backupConnector, that.backupConnector) &&
                Objects.equals(clusterGroup, that.clusterGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeId, brokerName, liveConnector, backupConnector, live, backup, clusterGroup);
    }

    @Override
    public String toString() {
        return "BrokerNodeInfo{" +
                "nodeId='" + nodeId + '\'' +
                ", brokerName='" + brokerName + '\'' +
                ", live=" + live +
                ", backup=" + backup +
                ", liveConnector='" + liveConnector + '\'' +
                ", backupConnector='" + backupConnector + '\'' +
                ", clusterGroup='" + clusterGroup + '\'' +
                '}';
    }
}
