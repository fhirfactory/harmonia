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

package net.fhirfactory.harmonia.mllpgateway.hl7;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.Terser;
import ca.uhn.hl7v2.util.idgenerator.NanoTimeGenerator;
import ca.uhn.hl7v2.validation.impl.NoValidation;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import net.fhirfactory.harmonia.logging.PhiLogger;
import net.fhirfactory.harmonia.logging.PhiLoggerFactory;
import net.fhirfactory.harmonia.mllpgateway.config.MllpConfig;
import net.fhirfactory.harmonia.mllpgateway.hl7.factories.MfnCommunicationResourceBuilder;
import net.fhirfactory.harmonia.mllpgateway.hl7.factories.MfnTaskResourceBuilder;
import net.fhirfactory.harmonia.mllpgateway.messaging.TaskEventProducerService;
import net.fhirfactory.harmonia.mllpgateway.service.CommunicationService;
import net.fhirfactory.harmonia.mllpgateway.service.ProvenanceService;
import net.fhirfactory.harmonia.mllpgateway.service.TaskService;
import net.fhirfactory.harmonia.model.ergon.ErgonEvent;
import net.fhirfactory.harmonia.model.topic.Topic;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.UUID;

/**
 * Orchestrates transforming inbound HL7 MFN^M02 messages into a Communication resource encapsulating
 * the raw HL7 message and a synthetic routing Task (with Communication as Task.input), caching them,
 * and dispatching TaskEvents to the task-sequence-processor.
 */
@ApplicationScoped
public class IncomingMfnMessageProcessor {

    private static final Logger log = LoggerFactory.getLogger(IncomingMfnMessageProcessor.class);
    private static final PhiLogger phiLog = PhiLoggerFactory.getLogger(IncomingMfnMessageProcessor.class);

    private TaskService taskService;
    private CommunicationService communicationService;
    private ProvenanceService provenanceService;
    private TaskEventProducerService taskEventProducerService;
    private MllpConfig mllpConfig;

    private final HapiContext hapiContext;
    private final PipeParser pipeParser;

    private MfnMessageExtractor extractor;
    private MfnCommunicationResourceBuilder communicationBuilder;
    private MfnTaskResourceBuilder taskBuilder;

    public IncomingMfnMessageProcessor() {
        this.hapiContext = new DefaultHapiContext();
        this.hapiContext.getParserConfiguration().setIdGenerator(new NanoTimeGenerator());
        this.hapiContext.setValidationContext(new NoValidation());
        this.pipeParser = hapiContext.getPipeParser();

        this.extractor = new MfnMessageExtractor();
        this.communicationBuilder = new MfnCommunicationResourceBuilder(this.extractor);
        this.taskBuilder = new MfnTaskResourceBuilder(this.extractor);
    }

    public IncomingMfnMessageProcessor(TaskService taskService) {
        this();
        this.taskService = taskService;
    }

    public IncomingMfnMessageProcessor(TaskService taskService, CommunicationService communicationService) {
        this();
        this.taskService = taskService;
        this.communicationService = communicationService;
    }

    public IncomingMfnMessageProcessor(TaskService taskService, CommunicationService communicationService,
                                       ProvenanceService provenanceService) {
        this();
        this.taskService = taskService;
        this.communicationService = communicationService;
        this.provenanceService = provenanceService;
    }

    public IncomingMfnMessageProcessor(TaskService taskService, TaskEventProducerService taskEventProducerService) {
        this();
        this.taskService = taskService;
        this.taskEventProducerService = taskEventProducerService;
    }

    public IncomingMfnMessageProcessor(TaskService taskService, CommunicationService communicationService,
                                       TaskEventProducerService taskEventProducerService) {
        this();
        this.taskService = taskService;
        this.communicationService = communicationService;
        this.taskEventProducerService = taskEventProducerService;
    }

    public IncomingMfnMessageProcessor(TaskService taskService, CommunicationService communicationService,
                                       ProvenanceService provenanceService,
                                       TaskEventProducerService taskEventProducerService) {
        this();
        this.taskService = taskService;
        this.communicationService = communicationService;
        this.provenanceService = provenanceService;
        this.taskEventProducerService = taskEventProducerService;
    }

    @Inject
    public IncomingMfnMessageProcessor(TaskService taskService, CommunicationService communicationService,
                                       ProvenanceService provenanceService,
                                       TaskEventProducerService taskEventProducerService,
                                       MllpConfig mllpConfig) {
        this();
        this.taskService = taskService;
        this.communicationService = communicationService;
        this.provenanceService = provenanceService;
        this.taskEventProducerService = taskEventProducerService;
        this.mllpConfig = mllpConfig;
    }

    public IncomingMfnMessageProcessor(TaskService taskService, CommunicationService communicationService,
                                       ProvenanceService provenanceService,
                                       TaskEventProducerService taskEventProducerService,
                                       MllpConfig mllpConfig,
                                       MfnMessageExtractor extractor,
                                       MfnCommunicationResourceBuilder communicationBuilder,
                                       MfnTaskResourceBuilder taskBuilder) {
        this.hapiContext = new DefaultHapiContext();
        this.hapiContext.getParserConfiguration().setIdGenerator(new NanoTimeGenerator());
        this.hapiContext.setValidationContext(new NoValidation());
        this.pipeParser = hapiContext.getPipeParser();

        this.taskService = taskService;
        this.communicationService = communicationService;
        this.provenanceService = provenanceService;
        this.taskEventProducerService = taskEventProducerService;
        this.mllpConfig = mllpConfig;

        this.extractor = extractor != null ? extractor : new MfnMessageExtractor();
        this.communicationBuilder = communicationBuilder != null ? communicationBuilder : new MfnCommunicationResourceBuilder(this.extractor);
        this.taskBuilder = taskBuilder != null ? taskBuilder : new MfnTaskResourceBuilder(this.extractor);
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    public TaskService getTaskService() {
        return taskService;
    }

    public void setCommunicationService(CommunicationService communicationService) {
        this.communicationService = communicationService;
    }

    public CommunicationService getCommunicationService() {
        return communicationService;
    }

    public void setProvenanceService(ProvenanceService provenanceService) {
        this.provenanceService = provenanceService;
    }

    public ProvenanceService getProvenanceService() {
        return provenanceService;
    }

    public void setTaskEventProducerService(TaskEventProducerService taskEventProducerService) {
        this.taskEventProducerService = taskEventProducerService;
    }

    public TaskEventProducerService getTaskEventProducerService() {
        return taskEventProducerService;
    }

    public void setMllpConfig(MllpConfig mllpConfig) {
        this.mllpConfig = mllpConfig;
    }

    public MllpConfig getMllpConfig() {
        return mllpConfig;
    }

    public MfnMessageExtractor getExtractor() {
        return extractor;
    }

    public void setExtractor(MfnMessageExtractor extractor) {
        this.extractor = extractor;
    }

    public MfnCommunicationResourceBuilder getCommunicationBuilder() {
        return communicationBuilder;
    }

    public void setCommunicationBuilder(MfnCommunicationResourceBuilder communicationBuilder) {
        this.communicationBuilder = communicationBuilder;
    }

    public MfnTaskResourceBuilder getTaskBuilder() {
        return taskBuilder;
    }

    public void setTaskBuilder(MfnTaskResourceBuilder taskBuilder) {
        this.taskBuilder = taskBuilder;
    }

    public MfnProcessingResult processMfnMessage(String hl7MessageString) {
        if (StringUtils.isBlank(hl7MessageString)) {
            String nack = extractor.generateFallbackAck("UNKNOWN", "UNKNOWN", "AE", "Empty HL7 message payload");
            return MfnProcessingResult.failure("UNKNOWN", "UNKNOWN", nack, "Payload is blank");
        }

        String messageControlId = "UNKNOWN";
        String triggerEvent = "M02";
        Topic topic = null;

        try {
            Message hl7Message = pipeParser.parse(hl7MessageString.trim());
            Terser terser = new Terser(hl7Message);

            String version = extractor.defaultIfBlank(extractor.getTerserValue(terser, "/MSH-12", "MSH-12"), "2.4");
            String messageType = extractor.defaultIfBlank(extractor.getTerserValue(terser, "/MSH-9-1", "MSH-9-1"), "MFN");
            triggerEvent = extractor.defaultIfBlank(extractor.getTerserValue(terser, "/MSH-9-2", "MSH-9-2"), "M02");
            messageControlId = extractor.defaultIfBlank(extractor.getTerserValue(terser, "/MSH-10", "MSH-10"), UUID.randomUUID().toString());
            String sendingApp = extractor.getTerserValue(terser, "/MSH-3", "MSH-3");
            String sendingFacility = extractor.getTerserValue(terser, "/MSH-4", "MSH-4");
            String receivingApp = extractor.getTerserValue(terser, "/MSH-5", "MSH-5");
            String receivingFacility = extractor.getTerserValue(terser, "/MSH-6", "MSH-6");
            String messageTimestamp = extractor.getTerserValue(terser, "/MSH-7", "MSH-7");
            String gatewayInstanceId = mllpConfig != null ? mllpConfig.getGatewayInstanceId() : "mllp-gateway-default";

            // Build explicit Topic to capture data type (message type) and routing parameters
            topic = Topic.fromHl7(version, messageType, triggerEvent, gatewayInstanceId, null,
                    sendingFacility != null ? sendingFacility : sendingApp,
                    receivingFacility != null ? receivingFacility : receivingApp);

            phiLog.debug("Inbound HL7 MFN message received [messageControlId={}, topic={}]: {}",
                    messageControlId, topic, hl7MessageString);

            // Extract Practitioner / Staff info
            String practitionerId = extractor.extractPractitionerId(hl7MessageString, terser);
            String practitionerFullName = extractor.extractPractitionerFullName(hl7MessageString, terser);

            phiLog.debug("Extracted practitioner details for message {}: id={}, name={}",
                    messageControlId, practitionerId, practitionerFullName);

            // 1. Generate Communication resource containing raw MFN message & metadata using Topic
            Communication communication = communicationBuilder.buildCommunication(terser, topic, hl7MessageString, messageControlId,
                    practitionerId, practitionerFullName, sendingApp, sendingFacility, messageTimestamp);

            // 2. Persist Communication to cache BEFORE processing into a Task resource
            Communication savedCommunication = communication != null && communicationService != null
                    ? communicationService.create(communication) : communication;
            if (savedCommunication != null) {
                log.info("Persisted Communication/{} containing raw MFN message to cache before Task processing",
                        savedCommunication.getIdPart());
            }

            // 3. Build FHIR R5 Task resource with Communication in Task.input
            Task task = taskBuilder.buildTask(terser, topic, messageControlId, practitionerId, practitionerFullName,
                    sendingApp, sendingFacility, messageTimestamp, savedCommunication);

            // 4. Persist Task to cache
            Task savedTask = task != null && taskService != null ? taskService.create(task) : task;

            // 5. Send TaskEvent to task-sequence-processor queue AFTER writing Task to cache
            if (savedTask != null && taskEventProducerService != null) {
                try {
                    String taskId = savedTask.getIdElement() != null && !savedTask.getIdElement().isEmpty()
                            ? savedTask.getIdElement().getIdPart() : savedTask.getIdPart();
                    if (StringUtils.isBlank(taskId)) {
                        taskId = messageControlId;
                    }
                    String action = "PROCESS";
                    String status = savedTask.getStatus() != null ? savedTask.getStatus().toCode() : "REQUESTED";
                    String desc = "HL7 v2.4 " + messageType + "^" + triggerEvent + " event transformed to Task";
                    ErgonEvent event = new ErgonEvent(taskId, action, status, topic, messageControlId, desc);
                    taskEventProducerService.sendTaskEvent(event);
                    log.info("Sent TaskEvent for Task/{} [action={}, status={}, topic={}] to task-sequence-processor",
                            taskId, action, status, topic);
                } catch (Exception e) {
                    log.warn("Could not send TaskEvent to task-sequence-processor for Task/{}: {}",
                            savedTask.getIdPart(), e.getMessage());
                }
            }

            // Generate HL7 ACK
            Message ackMessage = hl7Message.generateACK();
            String ackString = pipeParser.encode(ackMessage);

            log.info("Successfully processed HL7 v2.4 {} event, messageId: {}, topic: {}, created Communication: {}, created Task: {}",
                    triggerEvent, messageControlId, topic,
                    savedCommunication != null ? savedCommunication.getIdPart() : null,
                    savedTask != null ? savedTask.getIdPart() : null);

            return MfnProcessingResult.success(messageControlId, triggerEvent, topic, practitionerId, practitionerFullName, savedCommunication, savedTask, ackString);

        } catch (Exception e) {
            log.error("Error processing HL7 v2.4 MFN message: {}", e.getMessage(), e);
            String ackString = extractor.generateFallbackAck(messageControlId, triggerEvent, "AE", e.getMessage());
            return MfnProcessingResult.failure(messageControlId, triggerEvent, topic, ackString, e.getMessage());
        }
    }

    public Communication buildCommunication(Terser terser, Topic topic, String rawMessageString, String messageControlId,
                                            String practitionerId, String practitionerFullName,
                                            String sendingApp, String sendingFacility, String messageTimestamp) {
        return communicationBuilder.buildCommunication(terser, topic, rawMessageString, messageControlId,
                practitionerId, practitionerFullName, sendingApp, sendingFacility, messageTimestamp);
    }

    public Task buildTask(Terser terser, Topic topic, String messageControlId,
                          String practitionerId, String practitionerFullName,
                          String sendingApp, String sendingFacility,
                          String messageTimestamp, Communication communication) {
        return taskBuilder.buildTask(terser, topic, messageControlId, practitionerId, practitionerFullName,
                sendingApp, sendingFacility, messageTimestamp, communication);
    }

    public String extractPractitionerId(String rawMessage, Terser terser) {
        return extractor.extractPractitionerId(rawMessage, terser);
    }

    public String extractPractitionerFullName(String rawMessage, Terser terser) {
        return extractor.extractPractitionerFullName(rawMessage, terser);
    }

    public String extractMfeActionCode(String rawMessage, Terser terser) {
        return extractor.extractMfeActionCode(rawMessage, terser);
    }

    public Task.TaskStatus determineTaskStatus(String actionCode) {
        return extractor.determineTaskStatus(actionCode);
    }

    public String getTriggerEventDescription(String triggerEvent) {
        return extractor.getTriggerEventDescription(triggerEvent);
    }

    public Enumerations.AdministrativeGender mapAdministrativeGender(String genderCode) {
        return extractor.mapAdministrativeGender(genderCode);
    }

    public Date parseHl7Date(String hl7DateStr) {
        return extractor.parseHl7Date(hl7DateStr);
    }

    public String cleanPhoneNumber(String raw) {
        return extractor.cleanPhoneNumber(raw);
    }

    public String cleanId(String rawId) {
        return extractor.cleanId(rawId);
    }
}
