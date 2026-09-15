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

package net.fhirfactory.harmonia.mllpgateway.camel;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import net.fhirfactory.harmonia.mllpgateway.hl7.AdtProcessingResult;
import net.fhirfactory.harmonia.mllpgateway.hl7.IncomingAdtMessageProcessor;
import net.fhirfactory.harmonia.model.topic.Topic;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

@ApplicationScoped
public class IncomingAdtMessageProcessorWrapper implements Processor {

    private static final Logger log = LoggerFactory.getLogger(IncomingAdtMessageProcessorWrapper.class);

    public static final String HEADER_COMMUNICATION_ID = "HieCommunicationId";
    public static final String HEADER_TASK_ID = "HieTaskId";
    public static final String HEADER_PROVENANCE_ID = "HieProvenanceId";
    public static final String HEADER_TRIGGER_EVENT = "HieTriggerEvent";
    public static final String HEADER_PATIENT_ID = "HiePatientId";
    public static final String HEADER_MESSAGE_CONTROL_ID = "HieMessageControlId";
    public static final String HEADER_TOPIC = "HieTopic";
    public static final String HEADER_TOPIC_STRING = "HieTopicString";
    public static final String HEADER_TOPIC_DOMAIN = "HieTopicDomain";
    public static final String HEADER_TOPIC_MODEL = "HieTopicModel";
    public static final String HEADER_TOPIC_MODEL_VERSION = "HieTopicModelVersion";
    public static final String HEADER_TOPIC_DATA_ELEMENT = "HieTopicDataElement";
    public static final String HEADER_TOPIC_QUALIFIER = "HieTopicQualifier";
    public static final String HEADER_TOPIC_SOURCE = "HieTopicSource";
    public static final String PROPERTY_PROCESSING_RESULT = "HieProcessingResult";

    private IncomingAdtMessageProcessor transformer;

    public IncomingAdtMessageProcessorWrapper() {
    }

    @Inject
    public IncomingAdtMessageProcessorWrapper(IncomingAdtMessageProcessor transformer) {
        this.transformer = transformer;
    }

    public void setTransformer(IncomingAdtMessageProcessor transformer) {
        this.transformer = transformer;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Object body = exchange.getMessage().getBody();
        String hl7Message = convertToString(body);

        log.debug("Received HL7 message for processing:\n{}", hl7Message);

        AdtProcessingResult result = transformer.processAdtMessage(hl7Message);

        exchange.setProperty(PROPERTY_PROCESSING_RESULT, result);
        exchange.getMessage().setHeader(HEADER_MESSAGE_CONTROL_ID, result.getMessageControlId());
        exchange.getMessage().setHeader(HEADER_TRIGGER_EVENT, result.getTriggerEvent());
        if (result.getTopic() != null) {
            Topic topic = result.getTopic();
            exchange.getMessage().setHeader(HEADER_TOPIC, topic);
            exchange.getMessage().setHeader(HEADER_TOPIC_STRING, topic.toTopicString());
            if (topic.getDomain() != null) exchange.getMessage().setHeader(HEADER_TOPIC_DOMAIN, topic.getDomain());
            if (topic.getModel() != null) exchange.getMessage().setHeader(HEADER_TOPIC_MODEL, topic.getModel());
            if (topic.getModelVersion() != null) exchange.getMessage().setHeader(HEADER_TOPIC_MODEL_VERSION, topic.getModelVersion());
            if (topic.getDataElement() != null) exchange.getMessage().setHeader(HEADER_TOPIC_DATA_ELEMENT, topic.getDataElement());
            if (topic.getDataElementQualifier() != null) exchange.getMessage().setHeader(HEADER_TOPIC_QUALIFIER, topic.getDataElementQualifier());
            if (topic.getSource() != null) exchange.getMessage().setHeader(HEADER_TOPIC_SOURCE, topic.getSource());
        }

        if (result.isSuccess()) {
            if (result.getCommunication() != null) {
                exchange.getMessage().setHeader(HEADER_COMMUNICATION_ID, result.getCommunication().getIdPart());
            }
            if (result.getTask() != null) {
                exchange.getMessage().setHeader(HEADER_TASK_ID, result.getTask().getIdPart());
                exchange.getMessage().setHeader(HEADER_PATIENT_ID, result.getPatientId());
            }
            if (result.getProvenance() != null) {
                exchange.getMessage().setHeader(HEADER_PROVENANCE_ID, result.getProvenance().getIdPart());
            }
        }

        // Set ACK message as the response body and in MLLP acknowledgement header
        if (result.getAckMessage() != null) {
            exchange.getMessage().setBody(result.getAckMessage());
            exchange.getMessage().setHeader("CamelMllpAcknowledgement", result.getAckMessage());
            exchange.getMessage().setHeader("CamelMllpAcknowledgementString", result.getAckMessage());
        }

        if (!result.isSuccess()) {
            log.warn("ADT trigger event processing failed for control ID: {}, error: {}",
                    result.getMessageControlId(), result.getErrorMessage());
        }
    }

    private String convertToString(Object body) {
        if (body == null) {
            return "";
        }
        if (body instanceof String str) {
            return str;
        }
        if (body instanceof byte[] bytes) {
            return new String(bytes, StandardCharsets.UTF_8);
        }
        return body.toString();
    }
}
