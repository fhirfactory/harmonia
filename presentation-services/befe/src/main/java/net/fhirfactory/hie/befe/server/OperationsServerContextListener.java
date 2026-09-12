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

package net.fhirfactory.hie.befe.server;

import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet Context Listener ensuring the BEFE Operations HTTP Server (port 8090)
 * is eagerly started when the BEFE web application is deployed in WildFly Undertow.
 */
@WebListener
public class OperationsServerContextListener implements ServletContextListener {

    private static final Logger log = LoggerFactory.getLogger(OperationsServerContextListener.class);

    @Inject
    private OperationsServerManager operationsServerManager;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("BEFE ServletContext initialized. Starting Operations HTTP Server...");
        try {
            OperationsServerManager manager = this.operationsServerManager;
            if (manager == null) {
                try {
                    manager = CDI.current().select(OperationsServerManager.class).get();
                } catch (Exception e) {
                    log.warn("Could not obtain OperationsServerManager via CDI.current(): {}", e.getMessage());
                }
            }

            if (manager != null) {
                manager.startServer();
                log.info("BEFE Operations HTTP Server started on port {} via ServletContextListener.", manager.getPort());
            } else {
                log.error("OperationsServerManager is null; could not start Operations HTTP Server.");
            }
        } catch (Exception e) {
            log.error("Error starting OperationsServerManager in ServletContextListener: {}", e.getMessage(), e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("BEFE ServletContext destroyed. Stopping Operations HTTP Server...");
        try {
            OperationsServerManager manager = this.operationsServerManager;
            if (manager == null) {
                try {
                    manager = CDI.current().select(OperationsServerManager.class).get();
                } catch (Exception ignored) {
                }
            }

            if (manager != null) {
                manager.stopServer();
            }
        } catch (Exception e) {
            log.warn("Error stopping OperationsServerManager in ServletContextListener: {}", e.getMessage());
        }
    }
}
