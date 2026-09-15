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
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import net.fhirfactory.harmonia.mllpgateway.config.MllpConfig;
import net.fhirfactory.harmonia.mllpgateway.hl7.factories.AdtCommunicationResourceBuilder;
import net.fhirfactory.harmonia.mllpgateway.hl7.factories.AdtTaskResourceBuilder;
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
 * Orchestrates transforming inbound HL7 ADT messages into a Communication resource encapsulating
 * the raw HL7 message and a synthetic routing Task (with Communication as Task.input), caching them,
 * and dispatching TaskEvents to the task-sequence-processor.
 */
@ApplicationScoped
public class IncomingAdtMessageProcessor {

    private static final Logger log = LoggerFactory.getLogger(IncomingAdtMessageProcessor.class);

    private TaskService taskService;
    private CommunicationService communicationService;
    private ProvenanceService provenanceService;
    private TaskEventProducerService taskEventProducerService;
    private MllpConfig mllpConfig;

    private final HapiContext hapiContext;
    private final PipeParser pipeParser;

    private AdtMessageExtractor extractor;
    private AdtCommunicationResourceBuilder communicationBuilder;
    private AdtTaskResourceBuilder taskBuilder;

    public IncomingAdtMessageProcessor() {
        this.hapiContext = new DefaultHapiContext();
        this.hapiContext.getParserConfiguration().setIdGenerator(new NanoTimeGenerator());
        this.pipeParser = hapiContext.getPipeParser();

        this.extractor = new AdtMessageExtractor();
        this.communicationBuilder = new AdtCommunicationResourceBuilder(this.extractor);
        this.taskBuilder = new AdtTaskResourceBuilder(this.extractor);
    }

    public IncomingAdtMessageProcessor(TaskService taskService) {
        this();
        this.taskService = taskService;
    }

    public IncomingAdtMessageProcessor(TaskService taskService, CommunicationService communicationService) {
        this();
        this.taskService = taskService;
        this.communicationService = communicationService;
    }

    public IncomingAdtMessageProcessor(TaskService taskService, CommunicationService communicationService,
                                       ProvenanceService provenanceService) {
        this();
        this.taskService = taskService;
        this.communicationService = communicationService;
        this.provenanceService = provenanceService;
    }

    public IncomingAdtMessageProcessor(TaskService taskService, TaskEventProducerService taskEventProducerService) {
        this();
        this.taskService = taskService;
        this.taskEventProducerService = taskEventProducerService;
    }

    public IncomingAdtMessageProcessor(TaskService taskService, CommunicationService communicationService,
                                       TaskEventProducerService taskEventProducerService) {
        this();
        this.taskService = taskService;
        this.communicationService = communicationService;
        this.taskEventProducerService = taskEventProducerService;
    }

    public IncomingAdtMessageProcessor(TaskService taskService, CommunicationService communicationService,
                                       ProvenanceService provenanceService,
                                       TaskEventProducerService taskEventProducerService) {
        this();
        this.taskService = taskService;
        this.communicationService = communicationService;
        this.provenanceService = provenanceService;
        this.taskEventProducerService = taskEventProducerService;
    }

    @Inject
    public IncomingAdtMessageProcessor(TaskService taskService, CommunicationService communicationService,
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

    public IncomingAdtMessageProcessor(TaskService taskService, CommunicationService communicationService,
                                       ProvenanceService provenanceService,
                                       TaskEventProducerService taskEventProducerService,
                                       MllpConfig mllpConfig,
                                       AdtMessageExtractor extractor,
                                       AdtCommunicationResourceBuilder communicationBuilder,
                                       AdtTaskResourceBuilder taskBuilder) {
        this.hapiContext = new DefaultHapiContext();
        this.hapiContext.getParserConfiguration().setIdGenerator(new NanoTimeGenerator());
        this.pipeParser = hapiContext.getPipeParser();

        this.taskService = taskService;
        this.communicationService = communicationService;
        this.provenanceService = provenanceService;
        this.taskEventProducerService = taskEventProducerService;
        this.mllpConfig = mllpConfig;

        this.extractor = extractor != null ? extractor : new AdtMessageExtractor();
        this.communicationBuilder = communicationBuilder != null ? communicationBuilder : new AdtCommunicationResourceBuilder(this.extractor);
        this.taskBuilder = taskBuilder != null ? taskBuilder : new AdtTaskResourceBuilder(this.extractor);
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

    public AdtMessageExtractor getExtractor() {
        return extractor;
    }

    public void setExtractor(AdtMessageExtractor extractor) {
        this.extractor = extractor;
    }

    public AdtCommunicationResourceBuilder getCommunicationBuilder() {
        return communicationBuilder;
    }

    public void setCommunicationBuilder(AdtCommunicationResourceBuilder communicationBuilder) {
        this.communicationBuilder = communicationBuilder;
    }

    public AdtTaskResourceBuilder getTaskBuilder() {
        return taskBuilder;
    }

    public void setTaskBuilder(AdtTaskResourceBuilder taskBuilder) {
        this.taskBuilder = taskBuilder;
    }

    public AdtProcessingResult processAdtMessage(String hl7MessageString) {
        if (StringUtils.isBlank(hl7MessageString)) {
            String nack = extractor.generateFallbackAck("UNKNOWN", "UNKNOWN", "AE", "Empty HL7 message payload");
            return AdtProcessingResult.failure("UNKNOWN", "UNKNOWN", nack, "Payload is blank");
        }

        String messageControlId = "UNKNOWN";
        String triggerEvent = "UNKNOWN";
        Topic topic = null;

        try {
            Message hl7Message = pipeParser.parse(hl7MessageString.trim());
            Terser terser = new Terser(hl7Message);

            String version = extractor.defaultIfBlank(extractor.getTerserValue(terser, "/MSH-12", "MSH-12"), "2.4");
            String messageType = extractor.defaultIfBlank(extractor.getTerserValue(terser, "/MSH-9-1", "MSH-9-1"), "ADT");
            triggerEvent = extractor.defaultIfBlank(extractor.getTerserValue(terser, "/MSH-9-2", "MSH-9-2"), "A01");
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

            // Extract Patient Information
            String patientId = extractor.extractPatientId(hl7MessageString, terser);
            String patientFamilyName = extractor.getTerserValue(terser, "/PID-5-1", "PID-5-1");
            String patientGivenName = extractor.getTerserValue(terser, "/PID-5-2", "PID-5-2");
            String patientFullName = extractor.buildFullName(patientGivenName, patientFamilyName);

            // Extract Visit / Encounter Information
            String patientClass = extractor.getTerserValue(terser, "/PV1-2", "PV1-2");
            String assignedLocationPointOfCare = extractor.getTerserValue(terser, "/PV1-3-1", "PV1-3-1", "/PV1-3", "PV1-3");
            String assignedLocationRoom = extractor.getTerserValue(terser, "/PV1-3-2", "PV1-3-2");
            String assignedLocationBed = extractor.getTerserValue(terser, "/PV1-3-3", "PV1-3-3");
            String visitNumber = extractor.extractVisitNumber(hl7MessageString, terser);
            String attendingDocId = extractor.getTerserValue(terser, "/PV1-7-1", "PV1-7-1", "/PV1-7", "PV1-7");
            String attendingDocFamilyName = extractor.getTerserValue(terser, "/PV1-7-2", "PV1-7-2");
            String attendingDocGivenName = extractor.getTerserValue(terser, "/PV1-7-3", "PV1-7-3");
            String attendingDocName = extractor.buildFullName(attendingDocGivenName, attendingDocFamilyName);

            // 1. Generate Communication resource containing raw ADT message & metadata using Topic
            Communication communication = communicationBuilder.buildCommunication(terser, topic, hl7MessageString, messageControlId,
                    patientId, patientFullName, sendingApp, sendingFacility, messageTimestamp);

            // 2. Persist Communication to cache BEFORE processing into a Task resource
            Communication savedCommunication = communication != null && communicationService != null
                    ? communicationService.create(communication) : communication;
            if (savedCommunication != null) {
                log.info("Persisted Communication/{} containing raw ADT message to cache before Task processing",
                        savedCommunication.getIdPart());
            }

            // 3. Build FHIR R5 Task resource with Communication in Task.input
            Task task = taskBuilder.buildTask(terser, topic, messageControlId, patientId, patientFullName,
                    visitNumber, patientClass, assignedLocationPointOfCare, assignedLocationRoom, assignedLocationBed,
                    attendingDocId, attendingDocName, sendingApp, sendingFacility, messageTimestamp, savedCommunication);

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
                    String desc = "HL7 v2.4 " + messageType + "^" + triggerEvent + " event transformed to Task for patient " + patientFullName;
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

            return AdtProcessingResult.success(messageControlId, triggerEvent, topic, patientId, patientFullName, savedCommunication, savedTask, ackString);

        } catch (Exception e) {
            log.error("Error processing HL7 v2.4 ADT message: {}", e.getMessage(), e);
            String ackString = extractor.generateFallbackAck(messageControlId, triggerEvent, "AE", e.getMessage());
            return AdtProcessingResult.failure(messageControlId, triggerEvent, topic, ackString, e.getMessage());
        }
    }

    // --- Delegation methods for backwards compatibility ---

    public Communication buildCommunication(Terser terser, Topic topic, String rawMessageString, String messageControlId,
                                            String patientId, String patientFullName,
                                            String sendingApp, String sendingFacility, String messageTimestamp) {
        return communicationBuilder.buildCommunication(terser, topic, rawMessageString, messageControlId, patientId, patientFullName, sendingApp, sendingFacility, messageTimestamp);
    }

    public Communication buildCommunication(Terser terser, String rawMessageString, String messageControlId,
                                            String triggerEvent, String patientId, String patientFullName,
                                            String sendingApp, String sendingFacility, String messageTimestamp) {
        return communicationBuilder.buildCommunication(terser, rawMessageString, messageControlId, triggerEvent, patientId, patientFullName, sendingApp, sendingFacility, messageTimestamp);
    }

    public Task buildTask(Terser terser, Topic topic, String messageControlId,
                          String patientId, String patientFullName, String visitNumber, String patientClass,
                          String assignedLocationPointOfCare, String assignedLocationRoom, String assignedLocationBed,
                          String attendingDocId, String attendingDocName, String sendingApp, String sendingFacility,
                          String messageTimestamp, Communication communication) {
        return taskBuilder.buildTask(terser, topic, messageControlId, patientId, patientFullName, visitNumber, patientClass,
                assignedLocationPointOfCare, assignedLocationRoom, assignedLocationBed, attendingDocId, attendingDocName,
                sendingApp, sendingFacility, messageTimestamp, communication);
    }

    public Task buildTask(Terser terser, String messageType, String triggerEvent, String messageControlId,
                          String patientId, String patientFullName, String visitNumber, String patientClass,
                          String assignedLocationPointOfCare, String assignedLocationRoom, String assignedLocationBed,
                          String attendingDocId, String attendingDocName, String sendingApp, String sendingFacility,
                          String messageTimestamp, Communication communication) {
        return taskBuilder.buildTask(terser, messageType, triggerEvent, messageControlId, patientId, patientFullName,
                visitNumber, patientClass, assignedLocationPointOfCare, assignedLocationRoom, assignedLocationBed,
                attendingDocId, attendingDocName, sendingApp, sendingFacility, messageTimestamp, communication);
    }

    public String extractPatientId(String rawMessage, Terser terser) {
        return extractor.extractPatientId(rawMessage, terser);
    }

    public String extractVisitNumber(String rawMessage, Terser terser) {
        return extractor.extractVisitNumber(rawMessage, terser);
    }

    public String buildFullName(String given, String family) {
        return extractor.buildFullName(given, family);
    }

    public Enumerations.AdministrativeGender mapAdministrativeGender(String hl7Gender) {
        return extractor.mapAdministrativeGender(hl7Gender);
    }

    public CodeableConcept mapMaritalStatus(String hl7Code) {
        return extractor.mapMaritalStatus(hl7Code);
    }

    public Task.TaskStatus determineTaskStatus(String triggerEvent) {
        return extractor.determineTaskStatus(triggerEvent);
    }

    public Enumerations.RequestPriority determinePriority(String patientClass) {
        return extractor.determinePriority(patientClass);
    }

    public String getTriggerEventDescription(String triggerEvent) {
        return extractor.getTriggerEventDescription(triggerEvent);
    }

    public Enumerations.EncounterStatus mapEncounterStatus(String triggerEvent) {
        return extractor.mapEncounterStatus(triggerEvent);
    }

    public CodeableConcept mapEncounterClass(String patientClass) {
        return extractor.mapEncounterClass(patientClass);
    }

    public Date parseHl7Date(String hl7DateStr) {
        return extractor.parseHl7Date(hl7DateStr);
    }

    public String cleanPhoneNumber(String raw) {
        return extractor.cleanPhoneNumber(raw);
    }
}
