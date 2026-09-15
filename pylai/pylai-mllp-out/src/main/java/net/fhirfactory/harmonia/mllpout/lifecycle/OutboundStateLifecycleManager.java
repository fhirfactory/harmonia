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

package net.fhirfactory.harmonia.mllpout.lifecycle;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import net.fhirfactory.harmonia.mllpgateway.messaging.TaskEventProducerService;
import net.fhirfactory.harmonia.mllpgateway.model.OutboundMllpRequest;
import net.fhirfactory.harmonia.mllpgateway.model.OutboundMllpResponse;
import net.fhirfactory.harmonia.mllpgateway.service.CommunicationService;
import net.fhirfactory.harmonia.mllpgateway.service.OutboundCommunicationResourceBuilder;
import net.fhirfactory.harmonia.mllpgateway.service.OutboundProvenanceResourceBuilder;
import net.fhirfactory.harmonia.mllpgateway.service.OutboundTaskResourceBuilder;
import net.fhirfactory.harmonia.mllpgateway.service.ProvenanceService;
import net.fhirfactory.harmonia.mllpgateway.service.TaskService;
import net.fhirfactory.harmonia.mllpout.config.MllpOutboundConfig;
import org.hl7.fhir.r5.model.Communication;
import org.hl7.fhir.r5.model.Provenance;
import org.hl7.fhir.r5.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages the full lifecycle of FHIR Communication, Task (Pragma), and Provenance resources
 * across outbound MLLP transmissions, committing state changes to Mneme Infinispan grid
 * and publishing completion events to Petasos.
 */
@ApplicationScoped
public class OutboundStateLifecycleManager {

    private static final Logger LOG = LoggerFactory.getLogger(OutboundStateLifecycleManager.class);

    @Inject
    private CommunicationService communicationService;

    @Inject
    private TaskService taskService;

    @Inject
    private ProvenanceService provenanceService;

    @Inject
    private TaskEventProducerService taskEventProducerService;

    @Inject
    private OutboundCommunicationResourceBuilder communicationBuilder;

    @Inject
    private OutboundTaskResourceBuilder taskBuilder;

    @Inject
    private OutboundProvenanceResourceBuilder provenanceBuilder;

    @Inject
    private MllpOutboundConfig outboundConfig;

    public OutboundStateLifecycleManager() {
        this.communicationBuilder = new OutboundCommunicationResourceBuilder();
        this.taskBuilder = new OutboundTaskResourceBuilder();
        this.provenanceBuilder = new OutboundProvenanceResourceBuilder();
    }

    public OutboundStateLifecycleManager(CommunicationService communicationService,
                                         TaskService taskService,
                                         ProvenanceService provenanceService,
                                         TaskEventProducerService taskEventProducerService) {
        this();
        this.communicationService = communicationService;
        this.taskService = taskService;
        this.provenanceService = provenanceService;
        this.taskEventProducerService = taskEventProducerService;
    }

    /**
     * Initializes Communication and Task resources before dispatch and saves to Mneme cache.
     */
    public Communication onDispatchInitiated(OutboundMllpRequest request) {
        if (request == null) {
            return null;
        }

        Communication communication = null;
        try {
            communication = communicationBuilder.buildInitialCommunication(request, request.getDestinationId());
            if (communicationService != null) {
                communication = communicationService.create(communication);
                LOG.debug("Persisted initial outbound Communication: {}", communication.getId());
            }

            Task task = taskBuilder.buildInitialTask(request, communication);
            if (taskService != null) {
                task = taskService.create(task);
                request.setTaskId(task.getId());
                LOG.debug("Persisted initial outbound Task: {}", task.getId());
            }
        } catch (Exception e) {
            LOG.warn("Error during pre-dispatch state lifecycle initialization: {}", e.getMessage());
        }

        return communication;
    }

    /**
     * Finalizes Communication, Task, Provenance resources after ACK receipt,
     * updates Mneme cache, and emits completion ErgonEvent to Petasos broker.
     */
    public OutboundMllpResponse onDispatchCompleted(OutboundMllpRequest request, OutboundMllpResponse response) {
        if (response == null) {
            return null;
        }

        String commId = null;
        String taskId = request != null ? request.getTaskId() : null;

        // 1. Update Communication resource in Mneme cache
        try {
            Communication comm = communicationBuilder.buildInitialCommunication(request,
                    request != null ? request.getDestinationId() : response.getDestinationId());
            comm = communicationBuilder.updateCommunicationWithResponse(comm, response);
            if (communicationService != null) {
                if (communicationService.getById(comm.getId()).isPresent()) {
                    communicationService.update(comm.getId(), comm);
                } else {
                    communicationService.create(comm);
                }
            }
            commId = comm.getId();
            response.setCommunicationId(commId);
        } catch (Exception e) {
            LOG.warn("Error updating Communication state in cache: {}", e.getMessage());
        }

        // 2. Update Task (Pragma) resource in Mneme cache
        try {
            Task task = taskBuilder.buildInitialTask(request, null);
            task = taskBuilder.updateTaskWithResponse(task, response);
            if (taskService != null) {
                if (taskService.getById(task.getId()).isPresent()) {
                    taskService.update(task.getId(), task);
                } else {
                    taskService.create(task);
                }
            }
            taskId = task.getId();
            response.setTaskId(taskId);
        } catch (Exception e) {
            LOG.warn("Error updating Task state in cache: {}", e.getMessage());
        }

        // 3. Create Provenance resource in Mneme cache
        try {
            String agentName = outboundConfig != null ? "Harmonia MLLP Egress (" + outboundConfig.getInstanceId() + ")" : "Harmonia MLLP Egress Gateway";
            Provenance provenance = provenanceBuilder.buildProvenance(commId, request, response, agentName);
            if (provenanceService != null) {
                provenanceService.create(provenance);
            }
            response.setProvenanceId(provenance.getId());
        } catch (Exception e) {
            LOG.warn("Error recording Provenance in cache: {}", e.getMessage());
        }

        // 4. Emit completion ErgonEvent to Petasos
        if (taskEventProducerService != null && request != null) {
            try {
                String action = "MLLP_OUTBOUND_DISPATCH";
                String status = response.isSuccessful() ? "COMPLETED" : "FAILED";
                String desc = response.isSuccessful() ? "MLLP dispatch completed with ACK " + response.getAckCode()
                        : "MLLP dispatch failed: " + response.getErrorMessage();
                taskEventProducerService.sendTaskEvent(taskId != null ? taskId : request.getRequestId(),
                        action, status, request.getTopic(), request.getMessageControlId(), desc);
                LOG.debug("Published Petasos task event for request {}", request.getRequestId());
            } catch (Exception e) {
                LOG.warn("Error publishing Petasos completion event: {}", e.getMessage());
            }
        }

        return response;
    }

    public CommunicationService getCommunicationService() {
        return communicationService;
    }

    public void setCommunicationService(CommunicationService communicationService) {
        this.communicationService = communicationService;
    }

    public TaskService getTaskService() {
        return taskService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    public ProvenanceService getProvenanceService() {
        return provenanceService;
    }

    public void setProvenanceService(ProvenanceService provenanceService) {
        this.provenanceService = provenanceService;
    }

    public TaskEventProducerService getTaskEventProducerService() {
        return taskEventProducerService;
    }

    public void setTaskEventProducerService(TaskEventProducerService taskEventProducerService) {
        this.taskEventProducerService = taskEventProducerService;
    }
}
