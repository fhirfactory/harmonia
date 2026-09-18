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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package net.fhirfactory.harmonia.paradeigma.scenarios.operations;

import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;

import java.util.List;
import java.util.Map;

/**
 * Public client abstraction for interacting with the Harmonia Operations Backend (iris-befe :8090)
 * during live or simulated Paradeigma scenario executions.
 */
public interface IrisOperationsClient {

    /**
     * Retrieves the overall platform operational summary.
     */
    IrisOperationsModels.OpsSummaryDto getSummary(ThemisSecurityContext context) throws Exception;

    /**
     * Retrieves the inventory of all operational subsystems.
     */
    List<IrisOperationsModels.OpsSubsystemDto> getSubsystems(ThemisSecurityContext context) throws Exception;

    /**
     * Retrieves a single subsystem overview by identifier.
     */
    IrisOperationsModels.OpsSubsystemDto getSubsystem(String id, ThemisSecurityContext context) throws Exception;

    /**
     * Retrieves runtime instances for a given subsystem.
     */
    List<IrisOperationsModels.OpsInstanceDto> getSubsystemInstances(String id, ThemisSecurityContext context) throws Exception;

    /**
     * Retrieves health and dependency latencies for a given subsystem.
     */
    IrisOperationsModels.OpsHealthDto getSubsystemHealth(String id, ThemisSecurityContext context) throws Exception;

    /**
     * Retrieves all ActiveMQ Artemis queues and metrics.
     */
    List<IrisOperationsModels.OpsQueueDto> getQueues(ThemisSecurityContext context) throws Exception;

    /**
     * Retrieves a single queue by identifier.
     */
    IrisOperationsModels.OpsQueueDto getQueue(String id, ThemisSecurityContext context) throws Exception;

    /**
     * Retrieves all registered Praxis workflows and execution metrics.
     */
    List<IrisOperationsModels.OpsWorkflowDto> getWorkflows(ThemisSecurityContext context) throws Exception;

    /**
     * Retrieves a single workflow by identifier.
     */
    IrisOperationsModels.OpsWorkflowDto getWorkflow(String id, ThemisSecurityContext context) throws Exception;

    /**
     * Retrieves Pragma execution details and Ergon checkpoints.
     */
    IrisOperationsModels.OpsPragmaDto getPragma(String id, ThemisSecurityContext context) throws Exception;

    /**
     * Searches cross-subsystem interaction diagnostic events.
     */
    List<IrisOperationsModels.OpsEventDto> getEvents(Map<String, String> filters, ThemisSecurityContext context) throws Exception;

    /**
     * Retrieves a single operational event by ID.
     */
    IrisOperationsModels.OpsEventDto getEvent(String id, ThemisSecurityContext context) throws Exception;

    /**
     * Retrieves actionable operational alerts.
     */
    List<IrisOperationsModels.OpsAlertDto> getAlerts(Map<String, String> filters, ThemisSecurityContext context) throws Exception;

    /**
     * Acknowledges an active operational alert.
     */
    IrisOperationsModels.OpsAlertDto acknowledgeAlert(String alertId, String operator, ThemisSecurityContext context) throws Exception;
}
