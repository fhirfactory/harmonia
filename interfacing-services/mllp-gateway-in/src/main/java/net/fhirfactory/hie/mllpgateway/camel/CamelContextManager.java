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

package net.fhirfactory.hie.mllpgateway.camel;

import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Destroyed;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import net.fhirfactory.hie.mllpgateway.config.MllpConfig;
import net.fhirfactory.hie.mllpgateway.service.ModuleStatusService;
import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class CamelContextManager {

    private static final Logger log = LoggerFactory.getLogger(CamelContextManager.class);

    @Inject
    private IncomingAdtMessageMllpRouteBuilder incomingAdtMessageMllpRouteBuilder;

    @Inject
    private IncomingMfnMessageMllpRouteBuilder incomingMfnMessageMllpRouteBuilder;

    @Inject
    private ModuleStatusService moduleStatusService;

    @Inject
    private MllpConfig mllpConfig;

    private CamelContext camelContext;
    private int awaitTimeoutSeconds = 30;

    public void onStartup(@Observes @Initialized(ApplicationScoped.class) Object init) {
        start();
    }

    public synchronized void start() {
        if (camelContext != null && camelContext.isStarted()) {
            return;
        }
        try {
            log.info("Starting Apache Camel Context for HIE MLLP Gateway...");

            // Await detection in cache that task-sequence-processor is up and ready before initializing routes
            if (moduleStatusService != null && awaitTimeoutSeconds > 0) {
                log.info("Interfacing service awaiting cluster readiness for module [task-sequence-processor] (timeout: {}s)...",
                        awaitTimeoutSeconds);
                boolean processorReady = moduleStatusService.waitForModuleReady("task-sequence-processor", awaitTimeoutSeconds);
                if (processorReady) {
                    log.info("Downstream [task-sequence-processor] is confirmed UP and READY. Proceeding to initialise MLLP routes.");
                } else {
                    log.warn("Downstream [task-sequence-processor] was not detected as READY within {}s. Initializing routes with fallback.",
                            awaitTimeoutSeconds);
                }
            }

            camelContext = new DefaultCamelContext();
            if (incomingAdtMessageMllpRouteBuilder != null) {
                camelContext.addRoutes(incomingAdtMessageMllpRouteBuilder);
            }
            if (incomingMfnMessageMllpRouteBuilder != null) {
                camelContext.addRoutes(incomingMfnMessageMllpRouteBuilder);
            }
            camelContext.start();
            log.info("Apache Camel Context started successfully. Routes: {}", camelContext.getRoutes().size());

            // Update module status in cache
            if (moduleStatusService != null) {
                Map<String, Object> details = new HashMap<>();
                if (mllpConfig != null) {
                    details.put("gatewayInstanceId", mllpConfig.getGatewayInstanceId());
                    details.put("mllpHost", mllpConfig.getHost());
                    details.put("mllpPort", mllpConfig.getPort());
                    details.put("mllpMfnPort", mllpConfig.getMfnPort());
                }
                details.put("routesCount", camelContext.getRoutes().size());
                moduleStatusService.registerModule("mllp-gateway-in", "MLLP Gateway Inbound", "INTERFACING_SERVICE", "READY", true, details);
            }
        } catch (Exception e) {
            log.error("Failed to start Camel Context: {}", e.getMessage(), e);
            if (moduleStatusService != null) {
                moduleStatusService.registerModule("mllp-gateway-in", "MLLP Gateway Inbound", "INTERFACING_SERVICE", "ERROR", false, Map.of("error", e.getMessage() != null ? e.getMessage() : "Startup error"));
            }
            throw new RuntimeException("Error starting Camel context", e);
        }
    }

    public synchronized void stop(@Observes @Destroyed(ApplicationScoped.class) Object destroy) {
        if (moduleStatusService != null) {
            moduleStatusService.unregisterModule("mllp-gateway-in");
        }
        if (camelContext != null) {
            try {
                log.info("Stopping Apache Camel Context...");
                camelContext.stop();
                camelContext.close();
                log.info("Apache Camel Context stopped.");
            } catch (Exception e) {
                log.warn("Error stopping Camel Context: {}", e.getMessage(), e);
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

    public IncomingAdtMessageMllpRouteBuilder getIncomingAdtMessageMllpRouteBuilder() {
        return incomingAdtMessageMllpRouteBuilder;
    }

    public void setAdtMllpRouteBuilder(IncomingAdtMessageMllpRouteBuilder incomingAdtMessageMllpRouteBuilder) {
        this.incomingAdtMessageMllpRouteBuilder = incomingAdtMessageMllpRouteBuilder;
    }

    public IncomingMfnMessageMllpRouteBuilder getIncomingMfnMessageMllpRouteBuilder() {
        return incomingMfnMessageMllpRouteBuilder;
    }

    public void setMfnMllpRouteBuilder(IncomingMfnMessageMllpRouteBuilder incomingMfnMessageMllpRouteBuilder) {
        this.incomingMfnMessageMllpRouteBuilder = incomingMfnMessageMllpRouteBuilder;
    }

    public ModuleStatusService getModuleStatusService() {
        return moduleStatusService;
    }

    public void setModuleStatusService(ModuleStatusService moduleStatusService) {
        this.moduleStatusService = moduleStatusService;
    }

    public MllpConfig getMllpConfig() {
        return mllpConfig;
    }

    public void setMllpConfig(MllpConfig mllpConfig) {
        this.mllpConfig = mllpConfig;
    }

    public int getAwaitTimeoutSeconds() {
        return awaitTimeoutSeconds;
    }

    public void setAwaitTimeoutSeconds(int awaitTimeoutSeconds) {
        this.awaitTimeoutSeconds = awaitTimeoutSeconds;
    }
}
