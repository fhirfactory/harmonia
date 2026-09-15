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

package net.fhirfactory.harmonia.mllpout.camel;

import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Destroyed;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import net.fhirfactory.harmonia.mllpgateway.service.ModuleStatusService;
import net.fhirfactory.harmonia.mllpout.config.MllpOutboundConfig;
import net.fhirfactory.harmonia.mllpout.consumer.OutboundTaskQueueConsumer;
import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages the lifecycle of Apache Camel Context and routes for the MLLP Outbound Gateway.
 */
@ApplicationScoped
public class CamelContextManager {

    private static final Logger LOG = LoggerFactory.getLogger(CamelContextManager.class);

    @Inject
    private OutboundMllpRouteBuilder outboundMllpRouteBuilder;

    @Inject
    private OutboundTaskQueueConsumer outboundTaskQueueConsumer;

    @Inject
    private ModuleStatusService moduleStatusService;

    @Inject
    private MllpOutboundConfig outboundConfig;

    private CamelContext camelContext;

    public void onStartup(@Observes @Initialized(ApplicationScoped.class) Object init) {
        start();
    }

    public synchronized void start() {
        if (camelContext != null && camelContext.isStarted()) {
            return;
        }
        try {
            LOG.info("Starting Apache Camel Context for Harmonia MLLP Outbound Gateway...");

            camelContext = new DefaultCamelContext();
            if (outboundMllpRouteBuilder != null) {
                camelContext.addRoutes(outboundMllpRouteBuilder);
            }
            camelContext.start();
            LOG.info("Apache Camel Context started successfully. Routes: {}", camelContext.getRoutes().size());

            if (outboundTaskQueueConsumer != null) {
                outboundTaskQueueConsumer.setCamelContext(camelContext);
                outboundTaskQueueConsumer.start();
            }

            // Register module status in cache
            if (moduleStatusService != null && outboundConfig != null) {
                Map<String, Object> details = new HashMap<>();
                details.put("instanceId", outboundConfig.getInstanceId());
                details.put("targetEndpointId", outboundConfig.getTargetEndpointId());
                details.put("dedicatedQueue", outboundConfig.getDedicatedEventQueueName());
                details.put("restPort", outboundConfig.getRestPort());
                details.put("routesCount", camelContext.getRoutes().size());
                String moduleId = "mllp-gateway-out-" + outboundConfig.getInstanceId();
                moduleStatusService.registerModule(moduleId, "MLLP Gateway Outbound", "EGRESS_GATEWAY", "READY", true, details);
            }
        } catch (Exception e) {
            LOG.error("Failed to start Outbound Camel Context: {}", e.getMessage(), e);
            if (moduleStatusService != null && outboundConfig != null) {
                String moduleId = "mllp-gateway-out-" + outboundConfig.getInstanceId();
                moduleStatusService.registerModule(moduleId, "MLLP Gateway Outbound", "EGRESS_GATEWAY", "ERROR", false,
                        Map.of("error", e.getMessage() != null ? e.getMessage() : "Startup error"));
            }
            throw new RuntimeException("Error starting Outbound Camel Context", e);
        }
    }

    public synchronized void stop(@Observes @Destroyed(ApplicationScoped.class) Object destroy) {
        if (moduleStatusService != null && outboundConfig != null) {
            String moduleId = "mllp-gateway-out-" + outboundConfig.getInstanceId();
            moduleStatusService.unregisterModule(moduleId);
        }
        if (outboundTaskQueueConsumer != null) {
            outboundTaskQueueConsumer.stop();
        }
        if (camelContext != null) {
            try {
                LOG.info("Stopping Outbound Apache Camel Context...");
                camelContext.stop();
                camelContext.close();
                LOG.info("Outbound Apache Camel Context stopped.");
            } catch (Exception e) {
                LOG.warn("Error stopping Camel Context: {}", e.getMessage(), e);
            }
        }
    }

    @PreDestroy
    public void preDestroy() {
        stop(null);
    }

    public CamelContext getCamelContext() {
        return camelContext;
    }

    public OutboundMllpRouteBuilder getOutboundMllpRouteBuilder() {
        return outboundMllpRouteBuilder;
    }

    public void setOutboundMllpRouteBuilder(OutboundMllpRouteBuilder outboundMllpRouteBuilder) {
        this.outboundMllpRouteBuilder = outboundMllpRouteBuilder;
    }

    public OutboundTaskQueueConsumer getOutboundTaskQueueConsumer() {
        return outboundTaskQueueConsumer;
    }

    public void setOutboundTaskQueueConsumer(OutboundTaskQueueConsumer outboundTaskQueueConsumer) {
        this.outboundTaskQueueConsumer = outboundTaskQueueConsumer;
    }

    public ModuleStatusService getModuleStatusService() {
        return moduleStatusService;
    }

    public void setModuleStatusService(ModuleStatusService moduleStatusService) {
        this.moduleStatusService = moduleStatusService;
    }

    public MllpOutboundConfig getOutboundConfig() {
        return outboundConfig;
    }

    public void setOutboundConfig(MllpOutboundConfig outboundConfig) {
        this.outboundConfig = outboundConfig;
    }
}
