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

package net.fhirfactory.harmonia.praxis.messaging;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Destroyed;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import net.fhirfactory.harmonia.model.petasos.PetasosQueueDefinition;
import net.fhirfactory.harmonia.praxis.cache.ClusterReadinessService;
import net.fhirfactory.harmonia.praxis.config.QueueConfig;
import net.fhirfactory.harmonia.praxis.service.MessageQueueService;
import org.apache.activemq.artemis.api.core.QueueConfiguration;
import org.apache.activemq.artemis.api.core.RoutingType;
import org.apache.activemq.artemis.api.core.TransportConfiguration;
import org.apache.activemq.artemis.core.config.Configuration;
import org.apache.activemq.artemis.core.config.impl.ConfigurationImpl;
import org.apache.activemq.artemis.core.remoting.impl.netty.NettyAcceptorFactory;
import org.apache.activemq.artemis.core.server.embedded.EmbeddedActiveMQ;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class ArtemisBrokerManager {

    private static final Logger log = LoggerFactory.getLogger(ArtemisBrokerManager.class);

    @Inject
    private QueueConfig queueConfig;

    @Inject
    private MessageQueueService messageQueueService;

    @Inject
    private ClusterReadinessService readinessService;

    private EmbeddedActiveMQ embeddedBroker;
    private boolean isRunning = false;

    public ArtemisBrokerManager() {
    }

    public ArtemisBrokerManager(QueueConfig queueConfig) {
        this.queueConfig = queueConfig;
    }

    public ArtemisBrokerManager(QueueConfig queueConfig, MessageQueueService messageQueueService) {
        this.queueConfig = queueConfig;
        this.messageQueueService = messageQueueService;
    }

    public ArtemisBrokerManager(QueueConfig queueConfig, MessageQueueService messageQueueService, ClusterReadinessService readinessService) {
        this.queueConfig = queueConfig;
        this.messageQueueService = messageQueueService;
        this.readinessService = readinessService;
    }

    public void onStartup(@Observes @Initialized(ApplicationScoped.class) Object init) {
        start();
    }

    @PostConstruct
    public synchronized void start() {
        if (isRunning) {
            return;
        }
        if (queueConfig == null || !queueConfig.isBrokerEnabled()) {
            log.info("Embedded Artemis broker is disabled by configuration.");
            return;
        }

        try {
            log.info("Starting Embedded Apache ActiveMQ Artemis broker...");
            Configuration config = new ConfigurationImpl();
            config.setPersistenceEnabled(false);
            config.setSecurityEnabled(false);
            config.setJournalDirectory("target/artemis-data/journal-" + System.nanoTime());
            config.setBindingsDirectory("target/artemis-data/bindings-" + System.nanoTime());
            config.setLargeMessagesDirectory("target/artemis-data/largemsg-" + System.nanoTime());
            config.setPagingDirectory("target/artemis-data/paging-" + System.nanoTime());

            // Acceptor for in-vm communication
            config.addAcceptorConfiguration("in-vm", "vm://0");

            // Acceptor for TCP clients (if port is available)
            String host = queueConfig.getBrokerHost();
            int port = queueConfig.getBrokerPort();
            if (isPortAvailable(host, port)) {
                Map<String, Object> params = new HashMap<>();
                params.put("host", host);
                params.put("port", port);
                config.addAcceptorConfiguration(new TransportConfiguration(NettyAcceptorFactory.class.getName(), params));
            } else {
                log.info("TCP port {}:{} is currently in use; embedded broker will run with in-vm transport only", host, port);
            }

            // Retrieve message queue definitions from cache / JPA store
            List<PetasosQueueDefinition> queueDefinitions = (messageQueueService != null)
                    ? messageQueueService.getAll()
                    : new ArrayList<>();

            List<String> configuredQueueNames = new ArrayList<>();
            if (!queueDefinitions.isEmpty()) {
                log.info("Retrieved {} message queue definition(s) from cache/JPA store", queueDefinitions.size());
                for (PetasosQueueDefinition qDef : queueDefinitions) {
                    if (qDef == null || !qDef.isEnabled()) {
                        continue;
                    }
                    String qName = qDef.getQueueName();
                    String address = qDef.getAddress();
                    RoutingType rt = "MULTICAST".equalsIgnoreCase(qDef.getRoutingType()) ? RoutingType.MULTICAST : RoutingType.ANYCAST;
                    QueueConfiguration qConfig = new QueueConfiguration(qName)
                            .setAddress(address)
                            .setRoutingType(rt)
                            .setDurable(qDef.isDurable());
                    if (qDef.getMaxConsumers() != null && qDef.getMaxConsumers() > 0) {
                        qConfig.setMaxConsumers(qDef.getMaxConsumers());
                    }
                    if (qDef.getFilter() != null && !qDef.getFilter().isBlank()) {
                        qConfig.setFilterString(qDef.getFilter());
                    }
                    config.addQueueConfiguration(qConfig);
                    configuredQueueNames.add(qName);
                }
            } else {
                // Fallback to QueueConfig defaults
                String queueName = queueConfig.getQueueName();
                QueueConfiguration queueConfiguration = new QueueConfiguration(queueName)
                        .setRoutingType(RoutingType.ANYCAST)
                        .setAddress(queueName)
                        .setDurable(false);
                config.addQueueConfiguration(queueConfiguration);
                configuredQueueNames.add(queueName);

                for (String eq : queueConfig.getGatewayEventQueues()) {
                    QueueConfiguration eqConfig = new QueueConfiguration(eq)
                            .setRoutingType(RoutingType.ANYCAST)
                            .setAddress(eq)
                            .setDurable(false);
                    config.addQueueConfiguration(eqConfig);
                    configuredQueueNames.add(eq);
                }
            }

            embeddedBroker = new EmbeddedActiveMQ();
            embeddedBroker.setConfiguration(config);
            embeddedBroker.start();
            isRunning = embeddedBroker.getActiveMQServer() != null && embeddedBroker.getActiveMQServer().isStarted();
            log.info("Embedded ActiveMQ Artemis broker started successfully on vm://0 and tcp://{}:{} with queues {}",
                    host, port, configuredQueueNames);
        } catch (Exception e) {
            log.error("Could not start embedded Artemis broker: {}", e.getMessage(), e);
            e.printStackTrace();
        }
    }

    /**
     * Synchronizes all message queue definitions from cache/JPA store with the running Artemis broker.
     *
     * @return list of synchronized queue names
     */
    public synchronized List<String> syncQueues() {
        List<String> synchronizedQueues = new ArrayList<>();
        if (!isRunning || embeddedBroker == null || embeddedBroker.getActiveMQServer() == null) {
            log.warn("Cannot sync queues: embedded Artemis broker is not running.");
            return synchronizedQueues;
        }

        try {
            List<PetasosQueueDefinition> queueDefinitions = (messageQueueService != null)
                    ? messageQueueService.getAll()
                    : new ArrayList<>();

            log.info("Synchronizing {} message queue definition(s) with active Artemis broker...", queueDefinitions.size());

            for (PetasosQueueDefinition qDef : queueDefinitions) {
                if (qDef != null && qDef.isEnabled()) {
                    boolean created = createOrUpdateQueue(qDef);
                    if (created) {
                        synchronizedQueues.add(qDef.getQueueName());
                    }
                }
            }

            // Also ensure default baseline queues exist
            if (queueConfig != null) {
                String defaultQueue = queueConfig.getQueueName();
                if (defaultQueue != null && !synchronizedQueues.contains(defaultQueue)) {
                    createOrUpdateQueue(new PetasosQueueDefinition(defaultQueue));
                    synchronizedQueues.add(defaultQueue);
                }
                for (String eq : queueConfig.getGatewayEventQueues()) {
                    if (eq != null && !synchronizedQueues.contains(eq)) {
                        createOrUpdateQueue(new PetasosQueueDefinition(eq));
                        synchronizedQueues.add(eq);
                    }
                }
            }

            log.info("Completed queue synchronization on Artemis broker. Synchronized queues: {}", synchronizedQueues);
        } catch (Exception e) {
            log.error("Error during queue synchronization on Artemis broker: {}", e.getMessage(), e);
        }
        return synchronizedQueues;
    }

    /**
     * Validates queue configurations against the active Artemis broker and persistence store.
     *
     * @return validation summary map
     */
    public Map<String, Object> validateQueues() {
        Map<String, Object> report = new HashMap<>();
        report.put("brokerRunning", isRunning);
        report.put("brokerHost", queueConfig != null ? queueConfig.getBrokerHost() : "localhost");
        report.put("brokerPort", queueConfig != null ? queueConfig.getBrokerPort() : 61616);

        List<PetasosQueueDefinition> queueDefinitions = (messageQueueService != null)
                ? messageQueueService.getAll()
                : new ArrayList<>();

        List<Map<String, Object>> queueReports = new ArrayList<>();
        int validCount = 0;
        int invalidCount = 0;

        for (PetasosQueueDefinition qDef : queueDefinitions) {
            Map<String, Object> qRep = new HashMap<>();
            qRep.put("queueName", qDef.getQueueName());
            qRep.put("address", qDef.getAddress());
            qRep.put("routingType", qDef.getRoutingType());
            qRep.put("durable", qDef.isDurable());
            qRep.put("enabled", qDef.isEnabled());
            qRep.put("gatewayInstanceId", qDef.getGatewayInstanceId());

            boolean isValid = qDef.getQueueName() != null && !qDef.getQueueName().isBlank();
            qRep.put("valid", isValid);
            if (isValid) {
                validCount++;
            } else {
                invalidCount++;
                qRep.put("error", "Queue name is blank or missing");
            }
            queueReports.add(qRep);
        }

        report.put("totalQueues", queueDefinitions.size());
        report.put("validQueues", validCount);
        report.put("invalidQueues", invalidCount);
        report.put("queues", queueReports);
        return report;
    }

    public synchronized boolean createOrUpdateQueue(PetasosQueueDefinition qDef) {
        if (qDef == null || !isRunning || embeddedBroker == null || embeddedBroker.getActiveMQServer() == null) {
            return false;
        }
        try {
            String qName = qDef.getQueueName();
            String address = qDef.getAddress();
            RoutingType rt = "MULTICAST".equalsIgnoreCase(qDef.getRoutingType()) ? RoutingType.MULTICAST : RoutingType.ANYCAST;
            QueueConfiguration qConfig = new QueueConfiguration(qName)
                    .setAddress(address)
                    .setRoutingType(rt)
                    .setDurable(qDef.isDurable());
            if (qDef.getMaxConsumers() != null && qDef.getMaxConsumers() > 0) {
                qConfig.setMaxConsumers(qDef.getMaxConsumers());
            }
            if (qDef.getFilter() != null && !qDef.getFilter().isBlank()) {
                qConfig.setFilterString(qDef.getFilter());
            }
            embeddedBroker.getActiveMQServer().createQueue(qConfig);
            log.info("Dynamically created/updated queue [{}] on active Artemis broker", qName);
            return true;
        } catch (Exception e) {
            log.warn("Failed creating queue [{}] on running broker: {}", qDef.getQueueName(), e.getMessage());
            return false;
        }
    }

    public void setMessageQueueService(MessageQueueService messageQueueService) {
        this.messageQueueService = messageQueueService;
    }

    public void setQueueConfig(QueueConfig queueConfig) {
        this.queueConfig = queueConfig;
    }

    public void setReadinessService(ClusterReadinessService readinessService) {
        this.readinessService = readinessService;
    }

    public void onShutdown(@Observes @Destroyed(ApplicationScoped.class) Object init) {
        stop();
    }

    @PreDestroy
    public synchronized void stop() {
        if (embeddedBroker != null && isRunning) {
            try {
                log.info("Stopping Embedded ActiveMQ Artemis broker...");
                embeddedBroker.stop();
                isRunning = false;
                log.info("Embedded ActiveMQ Artemis broker stopped.");
            } catch (Exception e) {
                log.warn("Error stopping embedded Artemis broker: {}", e.getMessage());
            }
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    private boolean isPortAvailable(String host, int port) {
        try (java.net.ServerSocket ss = new java.net.ServerSocket()) {
            ss.setReuseAddress(true);
            ss.bind(new java.net.InetSocketAddress(host, port));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
