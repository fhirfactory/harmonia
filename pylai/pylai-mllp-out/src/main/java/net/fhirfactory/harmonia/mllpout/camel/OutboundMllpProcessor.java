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

package net.fhirfactory.harmonia.mllpout.camel;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.Terser;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import net.fhirfactory.harmonia.mllpgateway.config.MllpDestinationConfig;
import net.fhirfactory.harmonia.mllpgateway.config.MllpDestinationRegistry;
import net.fhirfactory.harmonia.mllpgateway.model.OutboundMllpRequest;
import net.fhirfactory.harmonia.model.topic.Topic;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Processor for preparing outbound HL7 exchanges, resolving destination endpoints,
 * and setting headers for Apache Camel MLLP dynamic producer routes.
 */
@ApplicationScoped
public class OutboundMllpProcessor implements Processor {

    private static final Logger LOG = LoggerFactory.getLogger(OutboundMllpProcessor.class);
    private static final Pattern MSH_10_PATTERN = Pattern.compile("^MSH\\|[^|]*\\|[^|]*\\|[^|]*\\|[^|]*\\|[^|]*\\|[^|]*\\|[^|]*\\|[^|]*\\|([^|\\r\\n]+)");

    @Inject
    private MllpDestinationRegistry destinationRegistry;

    private final HapiContext hapiContext;
    private final PipeParser pipeParser;

    public OutboundMllpProcessor() {
        this.hapiContext = new DefaultHapiContext();
        this.pipeParser = hapiContext.getPipeParser();
    }

    public OutboundMllpProcessor(MllpDestinationRegistry destinationRegistry) {
        this();
        this.destinationRegistry = destinationRegistry;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Object body = exchange.getIn().getBody();
        String rawMessage = null;
        String messageControlId = null;
        String destinationId = null;
        Topic topic = null;
        String requestId = null;
        String taskId = null;
        OutboundMllpRequest outboundRequest = null;

        if (body instanceof OutboundMllpRequest) {
            outboundRequest = (OutboundMllpRequest) body;
            rawMessage = outboundRequest.getRawMessage();
            messageControlId = outboundRequest.getMessageControlId();
            destinationId = outboundRequest.getDestinationId();
            topic = outboundRequest.getTopic();
            requestId = outboundRequest.getRequestId();
            taskId = outboundRequest.getTaskId();
        } else if (body instanceof String) {
            rawMessage = (String) body;
        } else if (body != null) {
            rawMessage = body.toString();
        }

        if (StringUtils.isBlank(rawMessage)) {
            throw new IllegalArgumentException("Outbound MLLP message payload must not be null or blank");
        }

        // Header overrides
        String headerDestId = exchange.getIn().getHeader("HIE_DESTINATION_ID", String.class);
        if (StringUtils.isNotBlank(headerDestId)) {
            destinationId = headerDestId;
        }

        String headerControlId = exchange.getIn().getHeader("HIE_MESSAGE_CONTROL_ID", String.class);
        if (StringUtils.isNotBlank(headerControlId)) {
            messageControlId = headerControlId;
        }

        String headerReqId = exchange.getIn().getHeader("HIE_REQUEST_ID", String.class);
        if (StringUtils.isNotBlank(headerReqId)) {
            requestId = headerReqId;
        } else if (requestId == null) {
            requestId = UUID.randomUUID().toString();
        }

        // Extract MSH-10 if still not set
        if (StringUtils.isBlank(messageControlId)) {
            messageControlId = extractMessageControlId(rawMessage);
        }

        // Resolve destination configuration
        MllpDestinationConfig destinationConfig = resolveDestinationConfig(destinationId, topic);

        // Normalize raw message line breaks to \r as per HL7 v2 standard
        String normalizedMessage = rawMessage.replace("\r\n", "\r").replace("\n", "\r");
        if (!normalizedMessage.endsWith("\r")) {
            normalizedMessage += "\r";
        }

        // Populate Exchange headers for Camel dynamic MLLP endpoint
        exchange.getMessage().setHeader("HIE_DEST_HOST", destinationConfig.getHost());
        exchange.getMessage().setHeader("HIE_DEST_PORT", destinationConfig.getPort());
        exchange.getMessage().setHeader("HIE_CONNECT_TIMEOUT", destinationConfig.getConnectTimeoutMs());
        exchange.getMessage().setHeader("HIE_READ_TIMEOUT", destinationConfig.getReadTimeoutMs());
        exchange.getMessage().setHeader("HIE_AUTO_ACK", destinationConfig.isAutoAck());
        exchange.getMessage().setHeader("HIE_CHARSET", destinationConfig.getCharset());
        exchange.getMessage().setHeader("HIE_MESSAGE_CONTROL_ID", messageControlId);
        exchange.getMessage().setHeader("HIE_REQUEST_ID", requestId);
        exchange.getMessage().setHeader("HIE_DESTINATION_ID", destinationConfig.getDestinationId());
        exchange.getMessage().setHeader("HIE_TARGET_QUEUE", destinationConfig.getEffectiveQueueName());
        exchange.getMessage().setHeader("HIE_DISPATCH_TIME", System.currentTimeMillis());
        if (taskId != null) {
            exchange.getMessage().setHeader("HIE_TASK_ID", taskId);
        }
        if (outboundRequest != null) {
            exchange.getMessage().setHeader("HIE_OUTBOUND_REQUEST", outboundRequest);
        }

        // Set the outgoing body to the normalized HL7 message string
        exchange.getMessage().setBody(normalizedMessage);
        LOG.debug("Prepared outbound MLLP exchange for destination '{}' ({}:{}) controlId={}",
                destinationConfig.getDestinationId(), destinationConfig.getHost(), destinationConfig.getPort(), messageControlId);
    }

    public String extractMessageControlId(String rawMessage) {
        if (StringUtils.isBlank(rawMessage)) {
            return null;
        }
        try {
            Message hl7Msg = pipeParser.parse(rawMessage.trim());
            Terser terser = new Terser(hl7Msg);
            String controlId = terser.get("/MSH-10");
            if (StringUtils.isNotBlank(controlId)) {
                return controlId.trim();
            }
        } catch (Exception e) {
            LOG.debug("HAPI Terser MSH-10 extraction failed: {}", e.getMessage());
        }

        Matcher matcher = MSH_10_PATTERN.matcher(rawMessage.trim());
        if (matcher.find()) {
            return matcher.group(1).trim();
        }

        return UUID.randomUUID().toString();
    }

    private MllpDestinationConfig resolveDestinationConfig(String destinationId, Topic topic) {
        if (destinationRegistry != null) {
            if (StringUtils.isNotBlank(destinationId)) {
                Optional<MllpDestinationConfig> byId = destinationRegistry.resolveDestination(destinationId);
                if (byId.isPresent()) {
                    return byId.get();
                }
            }
            if (topic != null) {
                Optional<MllpDestinationConfig> byTopic = destinationRegistry.resolveDestination(topic);
                if (byTopic.isPresent()) {
                    return byTopic.get();
                }
            }
            Optional<MllpDestinationConfig> defaultDest = destinationRegistry.getDefaultDestination();
            if (defaultDest.isPresent()) {
                return defaultDest.get();
            }
        }

        if (StringUtils.isNotBlank(destinationId)) {
            return new MllpDestinationConfig(destinationId, "localhost", 2575);
        }

        return new MllpDestinationConfig("default", "localhost", 2575);
    }

    public MllpDestinationRegistry getDestinationRegistry() {
        return destinationRegistry;
    }

    public void setDestinationRegistry(MllpDestinationRegistry destinationRegistry) {
        this.destinationRegistry = destinationRegistry;
    }
}
