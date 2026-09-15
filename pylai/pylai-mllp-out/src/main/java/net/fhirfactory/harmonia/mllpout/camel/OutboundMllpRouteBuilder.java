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

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import net.fhirfactory.harmonia.mllpgateway.model.OutboundMllpResponse;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Apache Camel RouteBuilder for outbound MLLP transmission and HL7 acknowledgement processing.
 */
@Dependent
public class OutboundMllpRouteBuilder extends RouteBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(OutboundMllpRouteBuilder.class);

    @Inject
    private OutboundMllpProcessor outboundMllpProcessor;

    @Inject
    private Hl7AckProcessor hl7AckProcessor;

    public OutboundMllpRouteBuilder() {
    }

    public OutboundMllpRouteBuilder(OutboundMllpProcessor outboundMllpProcessor, Hl7AckProcessor hl7AckProcessor) {
        this.outboundMllpProcessor = outboundMllpProcessor;
        this.hl7AckProcessor = hl7AckProcessor;
    }

    @Override
    public void configure() throws Exception {
        // Handle connection and transmission exceptions gracefully
        onException(Exception.class)
                .handled(true)
                .maximumRedeliveries(2)
                .redeliveryDelay(500)
                .backOffMultiplier(2.0)
                .retryAttemptedLogLevel(LoggingLevel.WARN)
                .log(LoggingLevel.ERROR, LOG.getName(), "Error during outbound MLLP transmission to ${header.HIE_DEST_HOST}:${header.HIE_DEST_PORT}: ${exception.message}")
                .process(exchange -> {
                    Exception cause = exchange.getProperty(org.apache.camel.Exchange.EXCEPTION_CAUGHT, Exception.class);
                    String controlId = exchange.getIn().getHeader("HIE_MESSAGE_CONTROL_ID", String.class);
                    String destId = exchange.getIn().getHeader("HIE_DESTINATION_ID", String.class);
                    String reqId = exchange.getIn().getHeader("HIE_REQUEST_ID", String.class);
                    Long dispatchTime = exchange.getIn().getHeader("HIE_DISPATCH_TIME", Long.class);
                    long durationMs = dispatchTime != null ? (System.currentTimeMillis() - dispatchTime) : 0L;

                    OutboundMllpResponse failure = null;

                    // If exception contains MLLP acknowledgement payload
                    if (cause instanceof org.apache.camel.component.mllp.MllpException) {
                        org.apache.camel.component.mllp.MllpException mllpEx =
                                (org.apache.camel.component.mllp.MllpException) cause;
                        byte[] nackBytes = mllpEx.getHl7AcknowledgementBytes();
                        if (nackBytes != null && nackBytes.length > 0) {
                            String rawNack = new String(nackBytes, java.nio.charset.StandardCharsets.UTF_8);
                            failure = hl7AckProcessor.parseAndValidateAck(rawNack, controlId, destId, durationMs);
                            failure.setRequestId(reqId);
                        }
                    }

                    if (failure == null) {
                        String errorMsg = cause != null ? cause.getMessage() : "Unknown transmission error";
                        failure = OutboundMllpResponse.failure(reqId, controlId, destId, errorMsg, durationMs);
                    }

                    exchange.getMessage().setHeader("HIE_ACK_CODE", failure.getAckCode());
                    exchange.getMessage().setHeader("HIE_TRANSMISSION_SUCCESS", false);
                    exchange.getMessage().setHeader("HIE_ERROR_MESSAGE", failure.getErrorMessage());
                    exchange.getMessage().setBody(failure);
                });

        // Main Outbound MLLP Producer Route
        from("direct:mllp-outbound-send")
                .routeId("mllp-outbound-send-route")
                .log(LoggingLevel.INFO, LOG.getName(), "Initiating outbound MLLP dispatch for request ${header.HIE_REQUEST_ID}")
                .process(outboundMllpProcessor)
                .log(LoggingLevel.DEBUG, LOG.getName(), "Transmitting MLLP payload to ${header.HIE_DEST_HOST}:${header.HIE_DEST_PORT}")
                .toD("mllp://${header.HIE_DEST_HOST}:${header.HIE_DEST_PORT}?connectTimeout=${header.HIE_CONNECT_TIMEOUT}&readTimeout=${header.HIE_READ_TIMEOUT}&autoAck=${header.HIE_AUTO_ACK}&charsetName=${header.HIE_CHARSET}")
                .log(LoggingLevel.DEBUG, LOG.getName(), "Received raw MLLP response from destination ${header.HIE_DESTINATION_ID}")
                .process(hl7AckProcessor)
                .log(LoggingLevel.INFO, LOG.getName(), "Processed MLLP acknowledgement: code=${header.HIE_ACK_CODE}, success=${header.HIE_TRANSMISSION_SUCCESS}");
    }

    public OutboundMllpProcessor getOutboundMllpProcessor() {
        return outboundMllpProcessor;
    }

    public void setOutboundMllpProcessor(OutboundMllpProcessor outboundMllpProcessor) {
        this.outboundMllpProcessor = outboundMllpProcessor;
    }

    public Hl7AckProcessor getHl7AckProcessor() {
        return hl7AckProcessor;
    }

    public void setHl7AckProcessor(Hl7AckProcessor hl7AckProcessor) {
        this.hl7AckProcessor = hl7AckProcessor;
    }
}
