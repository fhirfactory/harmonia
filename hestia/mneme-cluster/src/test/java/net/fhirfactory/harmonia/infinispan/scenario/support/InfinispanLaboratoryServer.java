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

package net.fhirfactory.harmonia.infinispan.scenario.support;

import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ClientIntelligence;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.server.hotrod.HotRodServer;
import org.infinispan.server.hotrod.configuration.HotRodServerConfigurationBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * In-process real Infinispan multi-node cluster and Hot Rod server fixture for the
 * Mneme Distributed Behaviour Laboratory.
 *
 * Creates two real clustered EmbeddedCacheManager nodes (Node-1 and Node-2) with REPL_SYNC caches,
 * starts independent HotRodServer instances on dynamic ports, and provides factory methods for
 * genuinely independent Hot Rod client participants (Client-A and Client-B).
 */
public class InfinispanLaboratoryServer implements AutoCloseable {

    private final String clusterName;
    private EmbeddedCacheManager node1;
    private EmbeddedCacheManager node2;
    private HotRodServer server1;
    private HotRodServer server2;

    public InfinispanLaboratoryServer() {
        this("mneme-lab-cluster-" + UUID.randomUUID().toString().substring(0, 8));
    }

    public InfinispanLaboratoryServer(String clusterName) {
        this.clusterName = clusterName;
    }

    public void startCluster() {
        ConfigurationBuilder cacheConfigBuilder = new ConfigurationBuilder();
        cacheConfigBuilder.clustering().cacheMode(CacheMode.REPL_SYNC);
        cacheConfigBuilder.encoding().key().mediaType("text/plain");
        cacheConfigBuilder.encoding().value().mediaType("text/plain");
        Configuration cacheConfig = cacheConfigBuilder.build();

        // Node 1
        GlobalConfigurationBuilder global1 = GlobalConfigurationBuilder.defaultClusteredBuilder();
        global1.transport().clusterName(clusterName).nodeName("node-1").defaultTransport();
        node1 = new DefaultCacheManager(global1.build(), false);
        node1.defineConfiguration("person-cache", cacheConfig);
        node1.defineConfiguration("task-cache", cacheConfig);
        node1.defineConfiguration("practitioner-cache", cacheConfig);
        node1.start();

        // Node 2
        GlobalConfigurationBuilder global2 = GlobalConfigurationBuilder.defaultClusteredBuilder();
        global2.transport().clusterName(clusterName).nodeName("node-2").defaultTransport();
        node2 = new DefaultCacheManager(global2.build(), false);
        node2.defineConfiguration("person-cache", cacheConfig);
        node2.defineConfiguration("task-cache", cacheConfig);
        node2.defineConfiguration("practitioner-cache", cacheConfig);
        node2.start();

        // Hot Rod Server 1 bound to Node 1
        HotRodServerConfigurationBuilder server1Config = new HotRodServerConfigurationBuilder();
        server1Config.host("127.0.0.1").port(0);
        server1 = new HotRodServer();
        server1.start(server1Config.build(), node1);

        // Hot Rod Server 2 bound to Node 2
        HotRodServerConfigurationBuilder server2Config = new HotRodServerConfigurationBuilder();
        server2Config.host("127.0.0.1").port(0);
        server2 = new HotRodServer();
        server2.start(server2Config.build(), node2);
    }

    public RemoteCacheManager createClientA() {
        return createClientForServer(server1.getPort(), "Client-A");
    }

    public RemoteCacheManager createClientB() {
        return createClientForServer(server2.getPort(), "Client-B");
    }

    public RemoteCacheManager createClientC() {
        return createClientForServer(server1.getPort(), "Client-C");
    }

    public RemoteCacheManager createClientForServer(int port, String clientName) {
        org.infinispan.client.hotrod.configuration.ConfigurationBuilder cb =
                new org.infinispan.client.hotrod.configuration.ConfigurationBuilder();
        cb.addServer().host("127.0.0.1").port(port);
        // ClientIntelligence.BASIC ensures client A and client B speak exclusively to their respective endpoints
        cb.clientIntelligence(ClientIntelligence.BASIC);
        cb.connectionPool().maxActive(10).minIdle(1);
        return new RemoteCacheManager(cb.build());
    }

    public EmbeddedCacheManager getNode1() {
        return node1;
    }

    public EmbeddedCacheManager getNode2() {
        return node2;
    }

    public HotRodServer getServer1() {
        return server1;
    }

    public HotRodServer getServer2() {
        return server2;
    }

    public int getServer1Port() {
        return server1 != null ? server1.getPort() : -1;
    }

    public int getServer2Port() {
        return server2 != null ? server2.getPort() : -1;
    }

    public void stopServer1() {
        if (server1 != null) {
            try {
                server1.stop();
            } finally {
                server1 = null;
            }
        }
    }

    public void startServer1OnPort(int port) {
        HotRodServerConfigurationBuilder server1Config = new HotRodServerConfigurationBuilder();
        server1Config.host("127.0.0.1").port(port);
        server1 = new HotRodServer();
        server1.start(server1Config.build(), node1);
    }

    public String getClusterName() {
        return clusterName;
    }

    public List<String> getClusterMembers() {
        if (node1 != null && node1.getMembers() != null) {
            return node1.getMembers().stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public void close() {
        if (server1 != null) {
            try {
                server1.stop();
            } catch (Exception ignored) {
            }
        }
        if (server2 != null) {
            try {
                server2.stop();
            } catch (Exception ignored) {
            }
        }
        if (node1 != null) {
            try {
                node1.stop();
            } catch (Exception ignored) {
            }
        }
        if (node2 != null) {
            try {
                node2.stop();
            } catch (Exception ignored) {
            }
        }
    }
}
