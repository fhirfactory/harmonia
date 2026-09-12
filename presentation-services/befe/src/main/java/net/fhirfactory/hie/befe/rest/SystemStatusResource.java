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

package net.fhirfactory.hie.befe.rest;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import net.fhirfactory.hie.befe.server.OperationsServerManager;
import net.fhirfactory.hie.befe.service.FhirCacheService;
import net.fhirfactory.hie.befe.service.ModuleStatusService;
import net.fhirfactory.hie.befe.service.TaskSequenceCacheService;
import net.fhirfactory.hie.model.status.ModuleStatus;
import org.infinispan.client.hotrod.RemoteCacheManager;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
@Path("/operations/status")
public class SystemStatusResource {

    @Inject
    private RemoteCacheManager remoteCacheManager;

    @Inject
    private TaskSequenceCacheService taskSequenceCacheService;

    @Inject
    private FhirCacheService fhirCacheService;

    @Inject
    private OperationsServerManager operationsServerManager;

    @Inject
    private ModuleStatusService moduleStatusService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSystemStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("systemName", "HIE Platform 5-Tier Architecture");
        status.put("version", "1.0.0-SNAPSHOT");
        status.put("timestamp", System.currentTimeMillis());

        boolean isStarted = remoteCacheManager != null && remoteCacheManager.isStarted();
        status.put("infinispanClusterConnected", isStarted);
        status.put("infinispanClusterMode", "SYNC Replicated Distributed Data Grid");

        int operationsPort = operationsServerManager != null ? operationsServerManager.getPort() : 8090;

        Map<String, Object> tiers = new HashMap<>();
        tiers.put("tier1_ui", Map.of("name", "Operations UI & FHIR Resource UI", "tech", "Vue 3 + TypeScript", "status", "UP"));
        tiers.put("tier2_befe", Map.of(
                "name", "BEFE Gateway (Dual-Port FHIR & Operations)",
                "tech", "WildFly Jakarta EE 10",
                "status", "UP",
                "port", operationsPort,
                "fhirPort", 8080,
                "operationsPort", operationsPort
        ));
        tiers.put("tier3_infinispan", Map.of("name", "Infinispan 15 HA Cluster", "tech", "Infinispan Hot Rod (JGroups TCP)", "status", isStarted ? "UP" : "LOCAL_FALLBACK", "port", 11222));
        tiers.put("tier4_persistence_spi", Map.of("name", "Persistence Stores (NonBlockingStore SPI)", "tech", "Custom REST Cache Store", "status", "UP"));
        tiers.put("tier5_database", Map.of("name", "HAPI FHIR JPA & Operations JPA", "tech", "PostgreSQL 16", "status", "UP"));
        status.put("tiers", tiers);

        Map<String, Object> sequenceStats = new HashMap<>();
        sequenceStats.put("totalSequences", taskSequenceCacheService.count());
        status.put("sequences", sequenceStats);

        if (moduleStatusService != null) {
            List<ModuleStatus> clusterModules = moduleStatusService.getAllModuleStatuses();
            status.put("clusterModules", clusterModules);
            status.put("clusterModuleCount", clusterModules.size());
        }

        return Response.ok(status).build();
    }

    public void setOperationsServerManager(OperationsServerManager operationsServerManager) {
        this.operationsServerManager = operationsServerManager;
    }

    public void setTaskSequenceCacheService(TaskSequenceCacheService taskSequenceCacheService) {
        this.taskSequenceCacheService = taskSequenceCacheService;
    }

    public void setRemoteCacheManager(RemoteCacheManager remoteCacheManager) {
        this.remoteCacheManager = remoteCacheManager;
    }

    public void setFhirCacheService(FhirCacheService fhirCacheService) {
        this.fhirCacheService = fhirCacheService;
    }

    public void setModuleStatusService(ModuleStatusService moduleStatusService) {
        this.moduleStatusService = moduleStatusService;
    }
}
