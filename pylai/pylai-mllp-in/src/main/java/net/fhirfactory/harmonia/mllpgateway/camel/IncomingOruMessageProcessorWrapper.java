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
import net.fhirfactory.harmonia.mllpgateway.hl7.IncomingOruMessageProcessor;
import net.fhirfactory.harmonia.mllpgateway.hl7.OruProcessingResult;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

@ApplicationScoped
public class IncomingOruMessageProcessorWrapper implements Processor {

    private static final Logger log = LoggerFactory.getLogger(IncomingOruMessageProcessorWrapper.class);

    @Inject
    private IncomingOruMessageProcessor incomingOruMessageProcessor;

    public IncomingOruMessageProcessorWrapper() {
    }

    public IncomingOruMessageProcessorWrapper(IncomingOruMessageProcessor incomingOruMessageProcessor) {
        this.incomingOruMessageProcessor = incomingOruMessageProcessor;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Object body = exchange.getIn().getBody();
        String rawMessage = null;

        if (body instanceof String) {
            rawMessage = (String) body;
        } else if (body instanceof byte[]) {
            rawMessage = new String((byte[]) body, StandardCharsets.UTF_8);
        } else if (body != null) {
            rawMessage = body.toString();
        }

        OruProcessingResult result = incomingOruMessageProcessor.processOruMessage(rawMessage);

        exchange.getIn().setHeader("HIE_MESSAGE_CONTROL_ID", result.getMessageControlId());
        exchange.getIn().setHeader("HIE_TRIGGER_EVENT", result.getTriggerEvent());
        exchange.getIn().setHeader("HIE_PROCESSING_SUCCESS", result.isSuccess());

        if (result.getTopic() != null) {
            exchange.getIn().setHeader("HIE_TOPIC", result.getTopic().toTopicString());
        }

        if (result.getAckMessage() != null) {
            exchange.getMessage().setBody(result.getAckMessage());
        }
    }

    public IncomingOruMessageProcessor getIncomingOruMessageProcessor() {
        return incomingOruMessageProcessor;
    }

    public void setIncomingOruMessageProcessor(IncomingOruMessageProcessor incomingOruMessageProcessor) {
        this.incomingOruMessageProcessor = incomingOruMessageProcessor;
    }
}
