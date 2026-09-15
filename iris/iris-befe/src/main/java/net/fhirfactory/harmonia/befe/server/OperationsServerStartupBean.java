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

package net.fhirfactory.harmonia.befe.server;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Eager Startup EJB Singleton ensuring the BEFE Operations HTTP Server (port 8090)
 * is initialized immediately upon container deployment in WildFly.
 */
@Singleton
@Startup
public class OperationsServerStartupBean {

    private static final Logger log = LoggerFactory.getLogger(OperationsServerStartupBean.class);

    @Inject
    private OperationsServerManager operationsServerManager;

    @PostConstruct
    public void init() {
        log.info("OperationsServerStartupBean initializing Operations HTTP Server...");
        try {
            if (operationsServerManager != null) {
                operationsServerManager.startServer();
                log.info("OperationsServerStartupBean: Operations HTTP Server active on port {}.", operationsServerManager.getPort());
            } else {
                log.warn("OperationsServerStartupBean: OperationsServerManager injection was null.");
            }
        } catch (Exception e) {
            log.error("OperationsServerStartupBean: Failed to start Operations HTTP Server: {}", e.getMessage(), e);
        }
    }

    @PreDestroy
    public void cleanup() {
        log.info("OperationsServerStartupBean shutting down Operations HTTP Server...");
        try {
            if (operationsServerManager != null) {
                operationsServerManager.stopServer();
            }
        } catch (Exception e) {
            log.warn("OperationsServerStartupBean: Error during cleanup: {}", e.getMessage());
        }
    }
}
