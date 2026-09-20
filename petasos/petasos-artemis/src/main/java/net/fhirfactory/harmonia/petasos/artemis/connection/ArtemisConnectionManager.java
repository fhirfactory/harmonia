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

package net.fhirfactory.harmonia.petasos.artemis.connection;

import jakarta.jms.Connection;
import jakarta.jms.ExceptionListener;
import jakarta.jms.JMSException;
import jakarta.jms.Session;
import net.fhirfactory.harmonia.petasos.api.config.PetasosConfig;
import net.fhirfactory.harmonia.petasos.api.exception.PetasosConnectionException;
import net.fhirfactory.harmonia.petasos.api.health.ConnectionState;
import net.fhirfactory.harmonia.petasos.api.health.HealthStatus;
import net.fhirfactory.harmonia.petasos.api.health.PetasosHealth;
import net.fhirfactory.harmonia.petasos.api.topology.BrokerNodeInfo;
import net.fhirfactory.harmonia.petasos.api.topology.PetasosBrokerTopology;
import net.fhirfactory.harmonia.petasos.core.metrics.PetasosMetricsCollector;
import org.apache.activemq.artemis.api.core.client.ClientSessionFactory;
import org.apache.activemq.artemis.api.core.client.ClusterTopologyListener;
import org.apache.activemq.artemis.api.core.client.FailoverEventListener;
import org.apache.activemq.artemis.api.core.client.FailoverEventType;
import org.apache.activemq.artemis.api.core.client.ServerLocator;
import org.apache.activemq.artemis.api.core.client.TopologyMember;
import org.apache.activemq.artemis.jms.client.ActiveMQConnection;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Manages the client connection lifecycle, HA failover detection, topology tracking,
 * and session management for Apache ActiveMQ Artemis.
 */
public class ArtemisConnectionManager implements AutoCloseable, ExceptionListener, FailoverEventListener {

    private static final Logger log = LoggerFactory.getLogger(ArtemisConnectionManager.class);

    private final PetasosConfig config;
    private final PetasosMetricsCollector metrics;
    private final AtomicReference<ConnectionState> connectionState = new AtomicReference<>(ConnectionState.DISCONNECTED);
    private final AtomicLong reconnectCount = new AtomicLong(0);
    private final AtomicBoolean closed = new AtomicBoolean(false);
    private final Map<String, BrokerNodeInfo> discoveredNodes = new ConcurrentHashMap<>();

    private ActiveMQConnectionFactory connectionFactory;
    private Connection connection;

    public ArtemisConnectionManager(PetasosConfig config, PetasosMetricsCollector metrics) {
        this.config = Objects.requireNonNull(config, "PetasosConfig must not be null");
        this.metrics = metrics != null ? metrics : new PetasosMetricsCollector();
    }

    public synchronized void start() throws PetasosConnectionException {
        if (closed.get()) {
            throw new IllegalStateException("ArtemisConnectionManager is closed");
        }
        if (connection != null) {
            return;
        }

        try {
            String connectionUrl = buildClusterConnectionUrl(config);
            log.info("Connecting Petasos to Artemis cluster using bootstrap URL: {}", connectionUrl);

            this.connectionFactory = new ActiveMQConnectionFactory(
                    connectionUrl,
                    config.getUsername(),
                    config.getPassword()
            );

            ServerLocator locator = this.connectionFactory.getServerLocator();
            if (locator != null) {
                locator.setReconnectAttempts(config.getReconnectAttempts());
                locator.setRetryInterval(config.getRetryInterval());
                locator.setMaxRetryInterval(config.getMaxRetryInterval());
                locator.setRetryIntervalMultiplier(config.getRetryIntervalMultiplier());
                locator.setConnectionTTL(config.getConnectionTtl());
                locator.setClientFailureCheckPeriod(config.getClientFailureCheckPeriod());
                locator.setCallTimeout(config.getCallTimeout());

                // Register cluster topology listener
                locator.addClusterTopologyListener(new ClusterTopologyListener() {
                    @Override
                    public void nodeUP(TopologyMember member, boolean last) {
                        if (member != null && member.getNodeId() != null) {
                            String liveConnector = member.getLive() != null ? member.getLive().toString() : "unknown";
                            String backupConnector = member.getBackup() != null ? member.getBackup().toString() : null;
                            BrokerNodeInfo info = new BrokerNodeInfo(
                                    member.getNodeId(),
                                    member.getNodeId(),
                                    liveConnector,
                                    backupConnector,
                                    true,
                                    member.getBackup() != null,
                                    "cluster"
                            );
                            discoveredNodes.put(member.getNodeId(), info);
                            log.debug("Discovered active Artemis cluster node: {} ({})", member.getNodeId(), liveConnector);
                        }
                    }

                    @Override
                    public void nodeDown(long eventUID, String nodeID) {
                        if (nodeID != null) {
                            discoveredNodes.computeIfPresent(nodeID, (id, info) ->
                                    new BrokerNodeInfo(info.getNodeId(), info.getBrokerName(), info.getLiveConnector(),
                                            info.getBackupConnector(), false, info.isBackup(), info.getClusterGroup()));
                            log.debug("Artemis cluster node down event for node ID: {}", nodeID);
                        }
                    }
                });
            }

            this.connection = connectionFactory.createConnection();
            this.connection.setExceptionListener(this);
            if (this.connection instanceof ActiveMQConnection amqConn) {
                amqConn.setFailoverListener(this);
            }
            this.connection.start();

            this.connectionState.set(ConnectionState.CONNECTED);
            log.info("Successfully established connection to Petasos / Artemis cluster");

        } catch (Exception e) {
            this.connectionState.set(ConnectionState.FAILED);
            log.error("Failed to connect to Petasos / Artemis cluster: {}", e.getMessage(), e);
            throw new PetasosConnectionException("Failed to connect to Artemis cluster: " + e.getMessage(), e);
        }
    }

    public synchronized Connection getConnection() throws PetasosConnectionException {
        if (connection == null && !closed.get()) {
            start();
        }
        return connection;
    }

    public synchronized Session createSession(boolean transacted, int acknowledgeMode) throws JMSException {
        try {
            Connection conn = getConnection();
            return conn.createSession(transacted, acknowledgeMode);
        } catch (JMSException e) {
            log.warn("Failed to create JMS session, attempting connection refresh: {}", e.getMessage());
            if (!closed.get()) {
                try {
                    if (connection != null) {
                        try {
                            connection.close();
                        } catch (Exception ignored) {
                        }
                        connection = null;
                    }
                    start();
                    Connection conn = getConnection();
                    return conn.createSession(transacted, acknowledgeMode);
                } catch (Exception retryEx) {
                    log.error("Failed to re-establish connection while creating session: {}", retryEx.getMessage());
                    throw e;
                }
            }
            throw e;
        }
    }

    public String buildClusterConnectionUrl(PetasosConfig config) {
        List<String> urls = config.getBrokerUrls();
        if (urls == null || urls.isEmpty()) {
            return PetasosConfig.DEFAULT_BROKER_URL;
        }

        StringBuilder sb = new StringBuilder();
        if (urls.size() > 1) {
            sb.append("(");
            for (int i = 0; i < urls.size(); i++) {
                if (i > 0) sb.append(",");
                sb.append(cleanUrl(urls.get(i)));
            }
            sb.append(")");
        } else {
            sb.append(cleanUrl(urls.get(0)));
        }

        // Query parameters
        Map<String, String> params = new LinkedHashMap<>();
        if (config.isHaEnabled()) {
            params.put("ha", "true");
        }
        params.put("reconnectAttempts", String.valueOf(config.getReconnectAttempts()));
        params.put("retryInterval", String.valueOf(config.getRetryInterval()));
        params.put("maxRetryInterval", String.valueOf(config.getMaxRetryInterval()));
        params.put("retryIntervalMultiplier", String.valueOf(config.getRetryIntervalMultiplier()));
        params.put("connectionTTL", String.valueOf(config.getConnectionTtl()));
        params.put("clientFailureCheckPeriod", String.valueOf(config.getClientFailureCheckPeriod()));
        params.put("callTimeout", String.valueOf(config.getCallTimeout()));

        if (config.isSslEnabled()) {
            params.put("sslEnabled", "true");
            if (config.getTrustStorePath() != null) {
                params.put("trustStorePath", config.getTrustStorePath());
                params.put("trustStorePassword", config.getTrustStorePassword());
            }
            if (config.getKeyStorePath() != null) {
                params.put("keyStorePath", config.getKeyStorePath());
                params.put("keyStorePassword", config.getKeyStorePassword());
            }
        }

        sb.append("?");
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!first) sb.append("&");
            sb.append(entry.getKey()).append("=").append(entry.getValue());
            first = false;
        }

        return sb.toString();
    }

    private String cleanUrl(String url) {
        if (url == null) return "";
        int qIdx = url.indexOf('?');
        return qIdx >= 0 ? url.substring(0, qIdx) : url;
    }

    public String getConnectedBroker() {
        if (connection instanceof ActiveMQConnection amqConn) {
            try {
                ClientSessionFactory sessionFactory = amqConn.getSessionFactory();
                if (sessionFactory != null && sessionFactory.getConnection() != null) {
                    return sessionFactory.getConnection().getRemoteAddress();
                }
            } catch (Exception ignored) {
            }
        }
        return config.getPrimaryBrokerUrl();
    }

    public String getActiveNodeId() {
        if (!discoveredNodes.isEmpty()) {
            return discoveredNodes.keySet().iterator().next();
        }
        return null;
    }

    public PetasosConfig getConfig() {
        return config;
    }

    public ConnectionState getConnectionState() {
        return connectionState.get();
    }

    public long getReconnectCount() {
        return reconnectCount.get();
    }

    public PetasosHealth health() {
        if (connection == null && !closed.get()) {
            try {
                getConnection();
            } catch (Exception e) {
                log.debug("Health probe could not establish initial connection: {}", e.getMessage());
            }
        }
        ConnectionState state = connectionState.get();
        String broker = getConnectedBroker();
        String nodeId = getActiveNodeId();
        long reconnects = reconnectCount.get();

        Map<String, Object> details = new LinkedHashMap<>();
        details.put("primaryUrl", config.getPrimaryBrokerUrl());
        details.put("configuredUrls", config.getBrokerUrls());
        details.put("haEnabled", config.isHaEnabled());
        details.put("reconnectCount", reconnects);
        details.put("discoveredClusterNodes", discoveredNodes.size());

        if (state == ConnectionState.CONNECTED) {
            return PetasosHealth.up(broker, nodeId, reconnects, details);
        } else if (state == ConnectionState.RECONNECTING) {
            return PetasosHealth.degraded(state, broker, nodeId, reconnects, details);
        } else {
            return PetasosHealth.down(state, broker, reconnects, details);
        }
    }

    public PetasosBrokerTopology brokerTopology() {
        List<BrokerNodeInfo> nodes = new ArrayList<>(discoveredNodes.values());
        String activeNodeId = getActiveNodeId();
        String activeBrokerName = getConnectedBroker();

        Map<String, List<String>> pairs = new LinkedHashMap<>();
        for (BrokerNodeInfo node : nodes) {
            if (node.getClusterGroup() != null) {
                pairs.computeIfAbsent(node.getClusterGroup(), k -> new ArrayList<>()).add(node.getNodeId());
            }
        }

        return new PetasosBrokerTopology(activeNodeId, activeBrokerName, nodes, pairs);
    }

    @Override
    public void onException(JMSException exception) {
        log.warn("JMS connection exception encountered in Petasos Artemis Connection Manager: {}", exception.getMessage());
        metrics.recordProcessingFailure();
    }

    @Override
    public void failoverEvent(FailoverEventType eventType) {
        log.info("Petasos HA failover event received: {}", eventType);
        switch (eventType) {
            case FAILURE_DETECTED -> {
                connectionState.set(ConnectionState.RECONNECTING);
                log.warn("Primary broker failure detected. Petasos initiating automatic client failover...");
            }
            case FAILOVER_COMPLETED -> {
                connectionState.set(ConnectionState.CONNECTED);
                reconnectCount.incrementAndGet();
                metrics.recordReconnect();
                log.info("Petasos client successfully failed over and reconnected to backup broker. Total reconnects: {}", reconnectCount.get());
            }
            case FAILOVER_FAILED -> {
                connectionState.set(ConnectionState.FAILED);
                metrics.recordProcessingFailure();
                log.error("Petasos client failover failed. No active or backup brokers available in cluster.");
            }
        }
    }

    @Override
    public synchronized void close() {
        if (closed.compareAndSet(false, true)) {
            connectionState.set(ConnectionState.CLOSED);
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    log.debug("Error closing JMS Connection: {}", e.getMessage());
                }
                connection = null;
            }
            if (connectionFactory != null) {
                try {
                    connectionFactory.close();
                } catch (Exception e) {
                    log.debug("Error closing ActiveMQConnectionFactory: {}", e.getMessage());
                }
                connectionFactory = null;
            }
            log.info("ArtemisConnectionManager successfully closed.");
        }
    }
}
