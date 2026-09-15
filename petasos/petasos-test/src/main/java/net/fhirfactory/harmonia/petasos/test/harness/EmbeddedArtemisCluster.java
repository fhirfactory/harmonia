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

package net.fhirfactory.harmonia.petasos.test.harness;

import org.apache.activemq.artemis.api.core.QueueConfiguration;
import org.apache.activemq.artemis.api.core.RoutingType;
import org.apache.activemq.artemis.api.core.SimpleString;
import org.apache.activemq.artemis.api.core.TransportConfiguration;
import org.apache.activemq.artemis.core.config.ClusterConnectionConfiguration;
import org.apache.activemq.artemis.core.config.Configuration;
import org.apache.activemq.artemis.core.config.HAPolicyConfiguration;
import org.apache.activemq.artemis.core.config.ha.ReplicaPolicyConfiguration;
import org.apache.activemq.artemis.core.config.ha.ReplicatedPolicyConfiguration;
import org.apache.activemq.artemis.core.config.impl.ConfigurationImpl;
import org.apache.activemq.artemis.core.remoting.impl.netty.NettyAcceptorFactory;
import org.apache.activemq.artemis.core.remoting.impl.netty.NettyConnectorFactory;
import org.apache.activemq.artemis.core.server.ActiveMQServer;
import org.apache.activemq.artemis.core.server.ActiveMQServers;
import org.apache.activemq.artemis.core.server.cluster.impl.MessageLoadBalancingType;
import org.apache.activemq.artemis.core.settings.impl.AddressFullMessagePolicy;
import org.apache.activemq.artemis.core.settings.impl.AddressSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Harness for launching real in-process Artemis broker instances with file journals,
 * replication HA policies, and server-side clustering for automated tests.
 */
public class EmbeddedArtemisCluster implements AutoCloseable {

    private static final Logger log = LoggerFactory.getLogger(EmbeddedArtemisCluster.class);

    private final Path tempDir;
    private final Map<String, ActiveMQServer> servers = new ConcurrentHashMap<>();
    private final Map<String, Integer> serverPorts = new ConcurrentHashMap<>();

    public EmbeddedArtemisCluster() {
        try {
            this.tempDir = Files.createTempDirectory("petasos-artemis-test-");
        } catch (IOException e) {
            throw new RuntimeException("Failed to create temporary directory for test brokers", e);
        }
    }

    public synchronized ActiveMQServer startStandaloneBroker(String name, int port, boolean persistent) throws Exception {
        Configuration config = createBaseConfig(name, port, persistent);
        ActiveMQServer server = ActiveMQServers.newActiveMQServer(config);
        server.start();
        servers.put(name, server);
        serverPorts.put(name, port);
        log.info("Started standalone test Artemis broker: {} on port {}", name, port);
        return server;
    }

    public synchronized ActiveMQServer startReplicationPrimary(String name, int port, String groupName, int backupPort) throws Exception {
        Configuration config = createBaseConfig(name, port, true);

        Map<String, Object> selfParams = new HashMap<>();
        selfParams.put("host", "127.0.0.1");
        selfParams.put("port", port);
        config.addConnectorConfiguration("primary-connector", new TransportConfiguration(NettyConnectorFactory.class.getName(), selfParams));

        Map<String, Object> backupParams = new HashMap<>();
        backupParams.put("host", "127.0.0.1");
        backupParams.put("port", backupPort);
        config.addConnectorConfiguration("backup-connector", new TransportConfiguration(NettyConnectorFactory.class.getName(), backupParams));

        config.setClusterUser("artemisCluster");
        config.setClusterPassword("artemisClusterPassword");

        ClusterConnectionConfiguration cluster = new ClusterConnectionConfiguration()
                .setName("petasos-replication-cluster")
                .setConnectorName("primary-connector")
                .setMessageLoadBalancingType(MessageLoadBalancingType.ON_DEMAND)
                .setMaxHops(1)
                .setStaticConnectors(List.of("backup-connector"));
        config.addClusterConfiguration(cluster);

        ReplicatedPolicyConfiguration haPolicy = new ReplicatedPolicyConfiguration();
        haPolicy.setGroupName(groupName);
        haPolicy.setCheckForActiveServer(true);
        config.setHAPolicyConfiguration(haPolicy);

        ActiveMQServer server = ActiveMQServers.newActiveMQServer(config);
        server.start();
        servers.put(name, server);
        serverPorts.put(name, port);
        log.info("Started Replication Primary broker: {} on port {} (group: {})", name, port, groupName);
        return server;
    }

    public synchronized ActiveMQServer startReplicationBackup(String name, int port, String groupName, int primaryPort) throws Exception {
        Configuration config = createBaseConfig(name, port, true);

        Map<String, Object> selfParams = new HashMap<>();
        selfParams.put("host", "127.0.0.1");
        selfParams.put("port", port);
        config.addConnectorConfiguration("backup-connector", new TransportConfiguration(NettyConnectorFactory.class.getName(), selfParams));

        Map<String, Object> primaryParams = new HashMap<>();
        primaryParams.put("host", "127.0.0.1");
        primaryParams.put("port", primaryPort);
        config.addConnectorConfiguration("primary-connector", new TransportConfiguration(NettyConnectorFactory.class.getName(), primaryParams));

        config.setClusterUser("artemisCluster");
        config.setClusterPassword("artemisClusterPassword");

        ClusterConnectionConfiguration cluster = new ClusterConnectionConfiguration()
                .setName("petasos-replication-cluster")
                .setConnectorName("backup-connector")
                .setMessageLoadBalancingType(MessageLoadBalancingType.ON_DEMAND)
                .setMaxHops(1)
                .setStaticConnectors(List.of("primary-connector"));
        config.addClusterConfiguration(cluster);

        ReplicaPolicyConfiguration haPolicy = new ReplicaPolicyConfiguration();
        haPolicy.setGroupName(groupName);
        haPolicy.setAllowFailBack(true);
        config.setHAPolicyConfiguration(haPolicy);

        ActiveMQServer server = ActiveMQServers.newActiveMQServer(config);
        server.start();
        servers.put(name, server);
        serverPorts.put(name, port);
        log.info("Started Replication Backup broker: {} on port {} (protecting group: {})", name, port, groupName);
        return server;
    }

    public synchronized void startClusteredPair(
            String nameA, int portA,
            String nameB, int portB) throws Exception {

        // Setup Cluster connections between A and B
        Configuration configA = createBaseConfig(nameA, portA, true);
        Configuration configB = createBaseConfig(nameB, portB, true);

        Map<String, Object> paramsA = new HashMap<>();
        paramsA.put("host", "127.0.0.1");
        paramsA.put("port", portA);
        TransportConfiguration tcA = new TransportConfiguration(NettyConnectorFactory.class.getName(), paramsA);

        Map<String, Object> paramsB = new HashMap<>();
        paramsB.put("host", "127.0.0.1");
        paramsB.put("port", portB);
        TransportConfiguration tcB = new TransportConfiguration(NettyConnectorFactory.class.getName(), paramsB);

        configA.addConnectorConfiguration(nameA + "-connector", tcA);
        configA.addConnectorConfiguration(nameB + "-connector", tcB);

        configB.addConnectorConfiguration(nameA + "-connector", tcA);
        configB.addConnectorConfiguration(nameB + "-connector", tcB);

        configA.setClusterUser("artemisCluster");
        configA.setClusterPassword("artemisClusterPassword");
        configB.setClusterUser("artemisCluster");
        configB.setClusterPassword("artemisClusterPassword");

        ClusterConnectionConfiguration clusterA = new ClusterConnectionConfiguration()
                .setName("petasos-cluster")
                .setConnectorName(nameA + "-connector")
                .setMessageLoadBalancingType(MessageLoadBalancingType.ON_DEMAND)
                .setMaxHops(1)
                .setStaticConnectors(List.of(nameB + "-connector"));

        ClusterConnectionConfiguration clusterB = new ClusterConnectionConfiguration()
                .setName("petasos-cluster")
                .setConnectorName(nameB + "-connector")
                .setMessageLoadBalancingType(MessageLoadBalancingType.ON_DEMAND)
                .setMaxHops(1)
                .setStaticConnectors(List.of(nameA + "-connector"));

        configA.addClusterConfiguration(clusterA);
        configB.addClusterConfiguration(clusterB);

        ActiveMQServer serverA = ActiveMQServers.newActiveMQServer(configA);
        ActiveMQServer serverB = ActiveMQServers.newActiveMQServer(configB);

        serverA.start();
        serverB.start();

        servers.put(nameA, serverA);
        servers.put(nameB, serverB);
        serverPorts.put(nameA, portA);
        serverPorts.put(nameB, portB);

        log.info("Started Clustered Artemis pair: {} (port {}) <===> {} (port {})", nameA, portA, nameB, portB);
    }

    public synchronized void stopBroker(String name) throws Exception {
        ActiveMQServer server = servers.remove(name);
        if (server != null) {
            log.info("Stopping test Artemis broker: {}", name);
            server.stop();
        }
    }

    public synchronized void restartBroker(String name, int port, boolean persistent) throws Exception {
        stopBroker(name);
        startStandaloneBroker(name, port, persistent);
    }

    public ActiveMQServer getBroker(String name) {
        return servers.get(name);
    }

    public boolean isBrokerLive(String name) {
        ActiveMQServer server = servers.get(name);
        return server != null && server.isActive();
    }

    private Configuration createBaseConfig(String name, int port, boolean persistent) {
        Configuration config = new ConfigurationImpl();
        config.setName(name);
        config.setPersistenceEnabled(persistent);
        config.setSecurityEnabled(false);
        config.setJournalType(org.apache.activemq.artemis.core.server.JournalType.NIO);
        config.setMessageExpiryScanPeriod(100L);

        Path brokerDir = tempDir.resolve(name);
        config.setBindingsDirectory(brokerDir.resolve("bindings").toString());
        config.setJournalDirectory(brokerDir.resolve("journal").toString());
        config.setPagingDirectory(brokerDir.resolve("paging").toString());
        config.setLargeMessagesDirectory(brokerDir.resolve("largemsg").toString());

        Map<String, Object> nettyParams = new HashMap<>();
        nettyParams.put("host", "127.0.0.1");
        nettyParams.put("port", port);
        config.addAcceptorConfiguration(new TransportConfiguration(NettyAcceptorFactory.class.getName(), nettyParams));
        config.addConnectorConfiguration("netty-connector", new TransportConfiguration(NettyConnectorFactory.class.getName(), nettyParams));

        // Address settings
        AddressSettings addressSettings = new AddressSettings();
        addressSettings.setDeadLetterAddress(SimpleString.toSimpleString("DLQ"));
        addressSettings.setExpiryAddress(SimpleString.toSimpleString("ExpiryQueue"));
        addressSettings.setRedeliveryDelay(50);
        addressSettings.setMaxDeliveryAttempts(3);
        addressSettings.setRedistributionDelay(0);
        addressSettings.setAddressFullMessagePolicy(AddressFullMessagePolicy.PAGE);
        addressSettings.setAutoCreateAddresses(true);
        addressSettings.setAutoCreateQueues(true);
        addressSettings.setAutoDeleteQueues(false);
        addressSettings.setAutoDeleteAddresses(false);

        config.addAddressSetting("#", addressSettings);

        // Pre-create DLQ and ExpiryQueue
        config.addQueueConfiguration(new QueueConfiguration("DLQ").setRoutingType(RoutingType.ANYCAST));
        config.addQueueConfiguration(new QueueConfiguration("ExpiryQueue").setRoutingType(RoutingType.ANYCAST));

        return config;
    }

    @Override
    public synchronized void close() {
        for (Map.Entry<String, ActiveMQServer> entry : servers.entrySet()) {
            try {
                log.info("Closing test Artemis server: {}", entry.getKey());
                entry.getValue().stop();
            } catch (Exception e) {
                log.debug("Error stopping broker {}: {}", entry.getKey(), e.getMessage());
            }
        }
        servers.clear();
        serverPorts.clear();

        // Cleanup temporary directory
        try {
            deleteDirectory(tempDir.toFile());
        } catch (Exception ignored) {
        }
    }

    private void deleteDirectory(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    deleteDirectory(f);
                }
            }
        }
        file.delete();
    }
}
