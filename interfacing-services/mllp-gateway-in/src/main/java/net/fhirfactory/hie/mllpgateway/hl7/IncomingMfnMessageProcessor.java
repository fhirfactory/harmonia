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

package net.fhirfactory.hie.mllpgateway.hl7;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.Terser;
import ca.uhn.hl7v2.util.idgenerator.NanoTimeGenerator;
import ca.uhn.hl7v2.validation.impl.NoValidation;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import net.fhirfactory.hie.mllpgateway.config.MllpConfig;
import net.fhirfactory.hie.mllpgateway.hl7.factories.*;
import net.fhirfactory.hie.mllpgateway.messaging.TaskEventProducerService;
import net.fhirfactory.hie.mllpgateway.service.CommunicationService;
import net.fhirfactory.hie.mllpgateway.service.ProvenanceService;
import net.fhirfactory.hie.mllpgateway.service.TaskService;
import net.fhirfactory.hie.model.TaskEvent;
import net.fhirfactory.hie.model.topic.Topic;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

/**
 * Orchestrates transforming inbound HL7 MFN^M02 messages into FHIR R5 resources (Communication, Practitioner,
 * PractitionerRole, Organization, Location, Bundle, Task, Provenance), caching them, and dispatching TaskEvents.
 */
@ApplicationScoped
public class IncomingMfnMessageProcessor {

    private static final Logger log = LoggerFactory.getLogger(IncomingMfnMessageProcessor.class);

    private TaskService taskService;
    private CommunicationService communicationService;
    private ProvenanceService provenanceService;
    private TaskEventProducerService taskEventProducerService;
    private MllpConfig mllpConfig;

    private final HapiContext hapiContext;
    private final PipeParser pipeParser;

    private MfnMessageExtractor extractor;
    private MfnPractitionerResourceBuilder practitionerBuilder;
    private MfnAdministrativeResourceBuilder administrativeBuilder;
    private MfnBundleResourceBuilder bundleBuilder;
    private MfnCommunicationResourceBuilder communicationBuilder;
    private MfnTaskResourceBuilder taskBuilder;
    private MfnProvenanceResourceBuilder provenanceBuilder;

    public IncomingMfnMessageProcessor() {
        this.hapiContext = new DefaultHapiContext();
        this.hapiContext.getParserConfiguration().setIdGenerator(new NanoTimeGenerator());
        this.hapiContext.setValidationContext(new NoValidation());
        this.pipeParser = hapiContext.getPipeParser();

        this.extractor = new MfnMessageExtractor();
        this.practitionerBuilder = new MfnPractitionerResourceBuilder(this.extractor);
        this.administrativeBuilder = new MfnAdministrativeResourceBuilder(this.extractor);
        this.bundleBuilder = new MfnBundleResourceBuilder();
        this.communicationBuilder = new MfnCommunicationResourceBuilder(this.extractor);
        this.taskBuilder = new MfnTaskResourceBuilder(this.extractor);
        this.provenanceBuilder = new MfnProvenanceResourceBuilder(this.extractor);
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
                                       MfnPractitionerResourceBuilder practitionerBuilder,
                                       MfnAdministrativeResourceBuilder administrativeBuilder,
                                       MfnBundleResourceBuilder bundleBuilder,
                                       MfnCommunicationResourceBuilder communicationBuilder,
                                       MfnTaskResourceBuilder taskBuilder,
                                       MfnProvenanceResourceBuilder provenanceBuilder) {
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
        this.practitionerBuilder = practitionerBuilder != null ? practitionerBuilder : new MfnPractitionerResourceBuilder(this.extractor);
        this.administrativeBuilder = administrativeBuilder != null ? administrativeBuilder : new MfnAdministrativeResourceBuilder(this.extractor);
        this.bundleBuilder = bundleBuilder != null ? bundleBuilder : new MfnBundleResourceBuilder();
        this.communicationBuilder = communicationBuilder != null ? communicationBuilder : new MfnCommunicationResourceBuilder(this.extractor);
        this.taskBuilder = taskBuilder != null ? taskBuilder : new MfnTaskResourceBuilder(this.extractor);
        this.provenanceBuilder = provenanceBuilder != null ? provenanceBuilder : new MfnProvenanceResourceBuilder(this.extractor);
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

    public MfnPractitionerResourceBuilder getPractitionerBuilder() {
        return practitionerBuilder;
    }

    public void setPractitionerBuilder(MfnPractitionerResourceBuilder practitionerBuilder) {
        this.practitionerBuilder = practitionerBuilder;
    }

    public MfnAdministrativeResourceBuilder getAdministrativeBuilder() {
        return administrativeBuilder;
    }

    public void setAdministrativeBuilder(MfnAdministrativeResourceBuilder administrativeBuilder) {
        this.administrativeBuilder = administrativeBuilder;
    }

    public MfnBundleResourceBuilder getBundleBuilder() {
        return bundleBuilder;
    }

    public void setBundleBuilder(MfnBundleResourceBuilder bundleBuilder) {
        this.bundleBuilder = bundleBuilder;
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

    public MfnProvenanceResourceBuilder getProvenanceBuilder() {
        return provenanceBuilder;
    }

    public void setProvenanceBuilder(MfnProvenanceResourceBuilder provenanceBuilder) {
        this.provenanceBuilder = provenanceBuilder;
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

            // Extract Practitioner / Staff info
            String practitionerId = extractor.extractPractitionerId(hl7MessageString, terser);
            String practitionerFullName = extractor.extractPractitionerFullName(hl7MessageString, terser);

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

            // 3. Discrete Resource Transformations
            List<Organization> organizations = administrativeBuilder.buildOrganizations(terser, hl7MessageString, sendingFacility, receivingFacility);
            List<Location> locations = administrativeBuilder.buildLocations(terser, hl7MessageString);
            List<Practitioner> practitioners = practitionerBuilder.buildPractitioners(terser, hl7MessageString, messageControlId);
            List<PractitionerRole> practitionerRoles = practitionerBuilder.buildPractitionerRoles(terser, hl7MessageString, practitioners, organizations, locations);

            // 4. Bundle all transformed resources
            Bundle bundle = bundleBuilder.buildBundle(messageControlId, organizations, locations, practitioners, practitionerRoles);

            // 5. Build FHIR R5 Task resource with the Topic, Bundle payload and reference to Communication
            Task task = taskBuilder.buildTask(terser, topic, messageControlId, practitionerId, practitionerFullName,
                    sendingApp, sendingFacility, messageTimestamp, bundle, savedCommunication);

            // 6. Persist Task to cache
            Task savedTask = task != null && taskService != null ? taskService.create(task) : task;

            // 7. Generate and persist Provenance resource IF AND ONLY IF Task was created & persisted
            Provenance savedProvenance = null;
            if (savedTask != null) {
                Provenance provenance = provenanceBuilder.buildProvenance(terser, topic, messageControlId,
                        practitionerId, practitionerFullName, sendingApp, sendingFacility, messageTimestamp,
                        savedTask, savedCommunication);
                savedProvenance = provenance != null && provenanceService != null
                        ? provenanceService.create(provenance) : provenance;
                if (savedProvenance != null) {
                    log.info("Persisted Provenance/{} linking Task/{} and Communication/{} to cache",
                            savedProvenance.getIdPart(),
                            savedTask.getIdPart(),
                            savedCommunication != null ? savedCommunication.getIdPart() : null);
                }
            }

            // 8. Send TaskEvent to task-processor AFTER writing Task to cache
            if (savedTask != null && taskEventProducerService != null) {
                try {
                    String taskId = savedTask.getIdElement() != null && !savedTask.getIdElement().isEmpty()
                            ? savedTask.getIdElement().getIdPart() : savedTask.getIdPart();
                    if (StringUtils.isBlank(taskId)) {
                        taskId = messageControlId;
                    }
                    String action = "PROCESS";
                    String status = savedTask.getStatus() != null ? savedTask.getStatus().toCode() : "REQUESTED";
                    String desc = "HL7 v2.4 " + messageType + "^" + triggerEvent + " event transformed to Task for practitioner " + practitionerFullName;
                    TaskEvent event = new TaskEvent(taskId, action, status, topic, messageControlId, desc);
                    taskEventProducerService.sendTaskEvent(event);
                    log.info("Sent TaskEvent for Task/{} [action={}, status={}, topic={}] to task-processor",
                            taskId, action, status, topic);
                } catch (Exception e) {
                    log.warn("Could not send TaskEvent to task-processor for Task/{}: {}",
                            savedTask.getIdPart(), e.getMessage());
                }
            }

            // Generate HL7 ACK
            Message ackMessage = hl7Message.generateACK();
            String ackString = pipeParser.encode(ackMessage);

            log.info("Successfully processed HL7 v2.4 {} event, messageId: {}, topic: {}, created Communication: {}, created Task: {}, created Provenance: {}",
                    triggerEvent, messageControlId, topic,
                    savedCommunication != null ? savedCommunication.getIdPart() : null,
                    savedTask != null ? savedTask.getIdPart() : null,
                    savedProvenance != null ? savedProvenance.getIdPart() : null);

            return MfnProcessingResult.success(messageControlId, triggerEvent, topic, practitionerId, practitionerFullName, savedCommunication, savedTask, savedProvenance, ackString);

        } catch (Exception e) {
            log.error("Error processing HL7 v2.4 MFN message: {}", e.getMessage(), e);
            String ackString = extractor.generateFallbackAck(messageControlId, triggerEvent, "AE", e.getMessage());
            return MfnProcessingResult.failure(messageControlId, triggerEvent, topic, ackString, e.getMessage());
        }
    }

    public List<Practitioner> buildPractitioners(Terser terser, String rawMessage, String messageControlId) {
        return practitionerBuilder.buildPractitioners(terser, rawMessage, messageControlId);
    }

    public List<PractitionerRole> buildPractitionerRoles(Terser terser, String rawMessage,
                                                         List<Practitioner> practitioners,
                                                         List<Organization> organizations,
                                                         List<Location> locations) {
        return practitionerBuilder.buildPractitionerRoles(terser, rawMessage, practitioners, organizations, locations);
    }

    public List<Organization> buildOrganizations(Terser terser, String rawMessage, String sendingFacility, String receivingFacility) {
        return administrativeBuilder.buildOrganizations(terser, rawMessage, sendingFacility, receivingFacility);
    }

    public List<Location> buildLocations(Terser terser, String rawMessage) {
        return administrativeBuilder.buildLocations(terser, rawMessage);
    }

    public Bundle buildBundle(String messageControlId, List<Organization> organizations, List<Location> locations,
                              List<Practitioner> practitioners, List<PractitionerRole> practitionerRoles) {
        return bundleBuilder.buildBundle(messageControlId, organizations, locations, practitioners, practitionerRoles);
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
                          String messageTimestamp, Bundle bundle, Communication communication) {
        return taskBuilder.buildTask(terser, topic, messageControlId, practitionerId, practitionerFullName,
                sendingApp, sendingFacility, messageTimestamp, bundle, communication);
    }

    public Provenance buildProvenance(Terser terser, Topic topic, String messageControlId,
                                      String practitionerId, String practitionerFullName,
                                      String sendingApp, String sendingFacility, String messageTimestamp,
                                      Task savedTask, Communication savedCommunication) {
        return provenanceBuilder.buildProvenance(terser, topic, messageControlId, practitionerId, practitionerFullName,
                sendingApp, sendingFacility, messageTimestamp, savedTask, savedCommunication);
    }
}
