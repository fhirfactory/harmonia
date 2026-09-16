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
import net.fhirfactory.harmonia.mllpgateway.config.MllpConfig;
import net.fhirfactory.harmonia.mllpgateway.hl7.factories.OrmCommunicationResourceBuilder;
import net.fhirfactory.harmonia.mllpgateway.hl7.factories.OrmTaskResourceBuilder;
import net.fhirfactory.harmonia.mllpgateway.messaging.TaskEventProducerService;
import net.fhirfactory.harmonia.mllpgateway.service.CommunicationService;
import net.fhirfactory.harmonia.mllpgateway.service.ProvenanceService;
import net.fhirfactory.harmonia.mllpgateway.service.TaskService;
import net.fhirfactory.harmonia.model.ergon.ErgonEvent;
import net.fhirfactory.harmonia.model.topic.Topic;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.Communication;
import org.hl7.fhir.r5.model.Provenance;
import org.hl7.fhir.r5.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.UUID;

@ApplicationScoped
public class IncomingOrmMessageProcessor {

    private static final Logger log = LoggerFactory.getLogger(IncomingOrmMessageProcessor.class);

    private final HapiContext hapiContext;
    private final PipeParser pipeParser;

    private OrmMessageExtractor extractor;
    private OrmCommunicationResourceBuilder communicationBuilder;
    private OrmTaskResourceBuilder taskBuilder;

    @Inject
    private TaskService taskService;

    @Inject
    private CommunicationService communicationService;

    @Inject
    private ProvenanceService provenanceService;

    @Inject
    private TaskEventProducerService taskEventProducerService;

    @Inject
    private MllpConfig mllpConfig;

    @Inject
    public IncomingOrmMessageProcessor(TaskService taskService,
                                      CommunicationService communicationService,
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

    public IncomingOrmMessageProcessor() {
        this.hapiContext = new DefaultHapiContext();
        this.hapiContext.getParserConfiguration().setIdGenerator(new NanoTimeGenerator());
        this.hapiContext.setValidationContext(new NoValidation());
        this.pipeParser = hapiContext.getPipeParser();
        this.extractor = new OrmMessageExtractor();
        this.communicationBuilder = new OrmCommunicationResourceBuilder(extractor);
        this.taskBuilder = new OrmTaskResourceBuilder(extractor);
    }

    public OrmProcessingResult processOrmMessage(String rawHl7Message) {
        if (StringUtils.isBlank(rawHl7Message)) {
            String fallbackAck = extractor.generateFallbackAck("", "AE", "Empty HL7 message payload");
            return OrmProcessingResult.failure("UNKNOWN", "UNKNOWN", fallbackAck, "Empty HL7 message payload");
        }

        try {
            Message hl7Message = pipeParser.parse(rawHl7Message.trim());
            Terser terser = new Terser(hl7Message);

            String messageStructure = terser.get("/.MSH-9-3");
            String messageType = terser.get("/.MSH-9-1");
            String triggerEvent = terser.get("/.MSH-9-2");
            String messageControlId = terser.get("/.MSH-10");
            String sendingApp = terser.get("/.MSH-3");
            String sendingFacility = terser.get("/.MSH-4");
            String receivingApp = terser.get("/.MSH-5");
            String receivingFacility = terser.get("/.MSH-6");
            String messageTimestampStr = terser.get("/.MSH-7");

            if (StringUtils.isBlank(triggerEvent)) triggerEvent = "O01";
            if (StringUtils.isBlank(messageControlId)) messageControlId = UUID.randomUUID().toString();

            String gatewayInstanceId = mllpConfig != null ? mllpConfig.getGatewayInstanceId() : MllpConfig.DEFAULT_GATEWAY_INSTANCE_ID;
            Topic topic = Topic.fromHl7("2.4", messageType != null ? messageType : "ORM", triggerEvent,
                    gatewayInstanceId, null,
                    sendingFacility != null ? sendingFacility : sendingApp,
                    receivingFacility != null ? receivingFacility : receivingApp);

            String placerOrder = extractor.extractPlacerOrderNumber(rawHl7Message, terser);
            String universalServiceId = extractor.extractUniversalServiceId(rawHl7Message, terser);
            String patientId = extractor.extractPatientId(rawHl7Message, terser);
            String patientFullName = extractor.extractPatientFullName(rawHl7Message, terser);
            Date messageTimestamp = extractor.parseHl7Date(messageTimestampStr);

            Communication comm = communicationBuilder.buildCommunication(
                    rawHl7Message, messageControlId, triggerEvent, topic, terser,
                    gatewayInstanceId, sendingApp, sendingFacility, messageTimestamp
            );
            if (communicationService != null) {
                communicationService.create(comm);
            }

            Task task = taskBuilder.buildTask(
                    rawHl7Message, messageControlId, triggerEvent, topic, terser,
                    gatewayInstanceId, sendingApp, sendingFacility, messageTimestamp, comm
            );

            Task savedTask = task;
            if (taskService != null) {
                savedTask = taskService.create(task);
            }

            if (taskEventProducerService != null && savedTask != null) {
                try {
                    String action = "PROCESS";
                    String status = savedTask.getStatus() != null ? savedTask.getStatus().toCode() : "REQUESTED";
                    String taskId = savedTask.getIdPart() != null ? savedTask.getIdPart() : messageControlId;
                    String desc = "HL7 v2.4 ORM^" + triggerEvent + " order " + placerOrder + " for patient " + patientFullName;
                    ErgonEvent event = new ErgonEvent(taskId, action, status, topic, messageControlId, desc);
                    taskEventProducerService.sendTaskEvent(event);
                } catch (Exception e) {
                    log.warn("Could not dispatch TaskEvent to task-sequence-processor: {}", e.getMessage());
                }
            }

            Message ackMsg = hl7Message.generateACK();
            try {
                new Terser(ackMsg).set("/.MSA-2", messageControlId);
            } catch (Exception ignored) {
            }
            String ack = pipeParser.encode(ackMsg);
            log.info("Successfully ingested HL7 ORM^{} order {} [MSH-10: {}] with topic {}",
                    triggerEvent, placerOrder, messageControlId, topic);

            return OrmProcessingResult.success(messageControlId, triggerEvent, topic, placerOrder,
                    universalServiceId, patientId, comm, savedTask, null, ack);

        } catch (Exception e) {
            log.error("Failed to parse and process HL7 ORM message: {}", e.getMessage(), e);
            String ack = extractor.generateFallbackAck(rawHl7Message, "AE", e.getMessage());
            return OrmProcessingResult.failure("UNKNOWN", "O01", ack, e.getMessage());
        }
    }

    public OrmMessageExtractor getExtractor() {
        return extractor;
    }

    public void setExtractor(OrmMessageExtractor extractor) {
        this.extractor = extractor;
        this.communicationBuilder = new OrmCommunicationResourceBuilder(extractor);
        this.taskBuilder = new OrmTaskResourceBuilder(extractor);
    }
}
