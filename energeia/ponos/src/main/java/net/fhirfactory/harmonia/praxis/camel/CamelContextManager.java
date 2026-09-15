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

package net.fhirfactory.harmonia.praxis.camel;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Destroyed;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.jms.ConnectionFactory;
import net.fhirfactory.harmonia.praxis.service.ModuleStatusService;
import net.fhirfactory.harmonia.praxis.sequence.TaskSequenceLoader;
import org.apache.camel.CamelContext;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class CamelContextManager {

    private static final Logger log = LoggerFactory.getLogger(CamelContextManager.class);

    @Inject
    private TaskProcessorRouteBuilder taskProcessorRouteBuilder;

    @Inject
    private TaskSequenceLoader taskSequenceLoader;

    @Inject
    private ConnectionFactory connectionFactory;

    @Inject
    private ModuleStatusService moduleStatusService;

    private CamelContext camelContext;

    public void onStartup(@Observes @Initialized(ApplicationScoped.class) Object init) {
        startCamel();
    }

    @PostConstruct
    public void init() {
        startCamel();
    }

    public synchronized void startCamel() {
        if (camelContext != null && camelContext.isStarted()) {
            return;
        }
        try {
            log.info("Starting Apache Camel Context for HIE Task Processor...");
            camelContext = new DefaultCamelContext();

            if (connectionFactory != null) {
                JmsComponent jmsComponent = JmsComponent.jmsComponentAutoAcknowledge(connectionFactory);
                camelContext.addComponent("jms", jmsComponent);
                log.info("Configured Camel JMS component with Jakarta ConnectionFactory");
            }

            if (taskProcessorRouteBuilder != null) {
                camelContext.addRoutes(taskProcessorRouteBuilder);
            }

            if (taskSequenceLoader != null) {
                try {
                    taskSequenceLoader.loadAndRegisterSequences(camelContext);
                } catch (Exception e) {
                    log.warn("Error loading persisted TaskSequences during startup: {}", e.getMessage(), e);
                }
            }

            camelContext.start();
            log.info("Apache Camel Context started successfully for HIE Task Processor with routes: {}",
                    camelContext.getRoutes());

            if (moduleStatusService != null) {
                Map<String, Object> details = new HashMap<>();
                details.put("routesCount", camelContext.getRoutes().size());
                if (taskSequenceLoader != null) {
                    details.put("loadedSequencesCount", taskSequenceLoader.getLoadedSequences().size());
                }
                moduleStatusService.registerModule("task-sequence-processor", "Task Sequence Processor", "WORKFLOW_SERVICE", "READY", true, details);
            }
        } catch (Exception e) {
            log.error("Failed to start Camel Context for Task Processor", e);
            if (moduleStatusService != null) {
                moduleStatusService.registerModule("task-sequence-processor", "Task Sequence Processor", "WORKFLOW_SERVICE", "ERROR", false, Map.of("error", e.getMessage() != null ? e.getMessage() : "Startup error"));
            }
        }
    }

    public synchronized boolean reloadSequences() {
        if (camelContext == null || !camelContext.isStarted() || taskSequenceLoader == null) {
            log.warn("Cannot reload sequences: CamelContext is not started or TaskSequenceLoader is null.");
            return false;
        }
        try {
            log.info("Reloading TaskSequences in active CamelContext...");
            taskSequenceLoader.reloadSequences(camelContext);
            log.info("TaskSequences successfully reloaded in CamelContext. Active routes: {}", camelContext.getRoutes().size());
            return true;
        } catch (Exception e) {
            log.error("Failed to reload TaskSequences in CamelContext: {}", e.getMessage(), e);
            return false;
        }
    }

    public void setTaskProcessorRouteBuilder(TaskProcessorRouteBuilder taskProcessorRouteBuilder) {
        this.taskProcessorRouteBuilder = taskProcessorRouteBuilder;
    }

    public void setTaskSequenceLoader(TaskSequenceLoader taskSequenceLoader) {
        this.taskSequenceLoader = taskSequenceLoader;
    }

    public void setConnectionFactory(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public void onShutdown(@Observes @Destroyed(ApplicationScoped.class) Object init) {
        stopCamel();
    }

    @PreDestroy
    public void cleanup() {
        stopCamel();
    }

    public synchronized void stopCamel() {
        if (moduleStatusService != null) {
            moduleStatusService.unregisterModule("task-sequence-processor");
        }
        if (camelContext != null) {
            try {
                log.info("Stopping Apache Camel Context for Task Processor...");
                camelContext.stop();
                camelContext.close();
                log.info("Apache Camel Context stopped.");
            } catch (Exception e) {
                log.warn("Error while stopping Camel Context: {}", e.getMessage());
            }
        }
    }

    public ModuleStatusService getModuleStatusService() {
        return moduleStatusService;
    }

    public void setModuleStatusService(ModuleStatusService moduleStatusService) {
        this.moduleStatusService = moduleStatusService;
    }

    public CamelContext getCamelContext() {
        return camelContext;
    }
}
