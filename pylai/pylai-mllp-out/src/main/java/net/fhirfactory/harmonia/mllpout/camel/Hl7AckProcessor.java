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
import net.fhirfactory.harmonia.mllpgateway.model.OutboundMllpResponse;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Processor for parsing HL7 v2 ACK/NACK messages and validating MSA segment details.
 */
@ApplicationScoped
public class Hl7AckProcessor implements Processor {

    private static final Logger LOG = LoggerFactory.getLogger(Hl7AckProcessor.class);

    private static final Pattern MSA_PATTERN = Pattern.compile("(?:^|\\r|\\n)MSA\\|([^|\\r\\n]*)(?:\\|([^|\\r\\n]*))?(?:\\|([^|\\r\\n]*))?");

    private final HapiContext hapiContext;
    private final PipeParser pipeParser;

    public Hl7AckProcessor() {
        this.hapiContext = new DefaultHapiContext();
        this.pipeParser = hapiContext.getPipeParser();
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Object body = exchange.getIn().getBody();
        String rawAck = null;

        // Check for Camel MLLP specific acknowledgement headers first
        Object mllpAckHeader = exchange.getIn().getHeader("CamelMllpAcknowledgement");
        if (mllpAckHeader == null) {
            mllpAckHeader = exchange.getIn().getHeader("CamelMllpAcknowledgementString");
        }

        if (mllpAckHeader instanceof String) {
            rawAck = (String) mllpAckHeader;
        } else if (mllpAckHeader instanceof byte[]) {
            rawAck = new String((byte[]) mllpAckHeader, java.nio.charset.StandardCharsets.UTF_8);
        } else if (body instanceof String) {
            rawAck = (String) body;
        } else if (body instanceof byte[]) {
            rawAck = new String((byte[]) body, java.nio.charset.StandardCharsets.UTF_8);
        } else if (body != null) {
            rawAck = body.toString();
        }

        String expectedControlId = exchange.getIn().getHeader("HIE_MESSAGE_CONTROL_ID", String.class);
        String destinationId = exchange.getIn().getHeader("HIE_DESTINATION_ID", String.class);
        String requestId = exchange.getIn().getHeader("HIE_REQUEST_ID", String.class);
        Long dispatchTime = exchange.getIn().getHeader("HIE_DISPATCH_TIME", Long.class);
        long durationMs = dispatchTime != null ? (System.currentTimeMillis() - dispatchTime) : 0L;

        OutboundMllpResponse response = parseAndValidateAck(rawAck, expectedControlId, destinationId, durationMs);
        if (requestId != null) {
            response.setRequestId(requestId);
        }

        exchange.getMessage().setHeader("HIE_ACK_CODE", response.getAckCode());
        exchange.getMessage().setHeader("HIE_TRANSMISSION_SUCCESS", response.isSuccessful());
        exchange.getMessage().setBody(response);
    }

    /**
     * Parses the raw ACK response, validates MSA acknowledgement code and control ID matching.
     */
    public OutboundMllpResponse parseAndValidateAck(String rawAck, String expectedControlId, String destinationId, long durationMs) {
        if (StringUtils.isBlank(rawAck)) {
            OutboundMllpResponse resp = OutboundMllpResponse.failure(expectedControlId, "Empty or null ACK received from destination", durationMs);
            resp.setDestinationId(destinationId);
            return resp;
        }

        String ackCode = null;
        String msaControlId = null;
        String ackText = null;

        // Attempt HAPI Parser first
        try {
            Message hl7Msg = pipeParser.parse(rawAck.trim());
            Terser terser = new Terser(hl7Msg);
            ackCode = terser.get("/MSA-1");
            msaControlId = terser.get("/MSA-2");
            ackText = terser.get("/MSA-3");
        } catch (Exception e) {
            LOG.debug("HAPI parsing failed, falling back to regex extraction: {}", e.getMessage());
            Matcher matcher = MSA_PATTERN.matcher(rawAck);
            if (matcher.find()) {
                ackCode = matcher.group(1);
                msaControlId = matcher.group(2);
                ackText = matcher.group(3);
            }
        }

        if (StringUtils.isBlank(ackCode)) {
            OutboundMllpResponse resp = OutboundMllpResponse.failure(expectedControlId,
                    "Malformed ACK received: missing MSA segment in response", durationMs);
            resp.setRawAckMessage(rawAck);
            resp.setDestinationId(destinationId);
            return resp;
        }

        ackCode = ackCode.trim().toUpperCase();
        boolean isAccept = "AA".equals(ackCode) || "CA".equals(ackCode);
        boolean isError = "AE".equals(ackCode) || "CE".equals(ackCode);
        boolean isReject = "AR".equals(ackCode) || "CR".equals(ackCode);

        // Validate Control ID if expectedControlId is present and msaControlId is present
        boolean controlIdMatches = true;
        if (StringUtils.isNotBlank(expectedControlId) && StringUtils.isNotBlank(msaControlId)) {
            if (!StringUtils.equals(expectedControlId.trim(), msaControlId.trim())) {
                controlIdMatches = false;
                LOG.warn("MSA-2 control ID mismatch: expected '{}' but received '{}'", expectedControlId, msaControlId);
            }
        }

        OutboundMllpResponse response = new OutboundMllpResponse();
        response.setMessageControlId(StringUtils.isNotBlank(msaControlId) ? msaControlId : expectedControlId);
        response.setDestinationId(destinationId);
        response.setAckCode(ackCode);
        response.setAckText(ackText);
        response.setRawAckMessage(rawAck);
        response.setDurationMs(durationMs);
        response.setAcknowledgedAt(new Date());

        if (isAccept && controlIdMatches) {
            response.setSuccessful(true);
        } else if (isAccept && !controlIdMatches) {
            response.setSuccessful(false);
            response.setErrorMessage("Control ID mismatch: expected " + expectedControlId + ", received " + msaControlId);
        } else if (isError) {
            response.setSuccessful(false);
            response.setErrorMessage(StringUtils.isNotBlank(ackText) ? ackText : "HL7 Application Error (" + ackCode + ")");
        } else if (isReject) {
            response.setSuccessful(false);
            response.setErrorMessage(StringUtils.isNotBlank(ackText) ? ackText : "HL7 Application Reject (" + ackCode + ")");
        } else {
            response.setSuccessful(false);
            response.setErrorMessage("Unknown ACK code: " + ackCode);
        }

        return response;
    }
}
