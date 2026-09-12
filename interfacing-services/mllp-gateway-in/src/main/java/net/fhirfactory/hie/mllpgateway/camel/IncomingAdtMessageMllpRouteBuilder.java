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

package net.fhirfactory.hie.mllpgateway.camel;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import net.fhirfactory.hie.mllpgateway.config.MllpConfig;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Dependent
public class IncomingAdtMessageMllpRouteBuilder extends RouteBuilder {

    private static final Logger log = LoggerFactory.getLogger(IncomingAdtMessageMllpRouteBuilder.class);

    private MllpConfig mllpConfig;
    private IncomingAdtMessageProcessorWrapper incomingAdtMessageProcessorWrapper;

    public IncomingAdtMessageMllpRouteBuilder() {
    }

    @Inject
    public IncomingAdtMessageMllpRouteBuilder(MllpConfig mllpConfig, IncomingAdtMessageProcessorWrapper incomingAdtMessageProcessorWrapper) {
        this.mllpConfig = mllpConfig;
        this.incomingAdtMessageProcessorWrapper = incomingAdtMessageProcessorWrapper;
    }

    public void setMllpConfig(MllpConfig mllpConfig) {
        this.mllpConfig = mllpConfig;
    }

    public void setAdtTriggerEventProcessor(IncomingAdtMessageProcessorWrapper incomingAdtMessageProcessorWrapper) {
        this.incomingAdtMessageProcessorWrapper = incomingAdtMessageProcessorWrapper;
    }

    @Override
    public void configure() throws Exception {
        if (mllpConfig == null) {
            mllpConfig = new MllpConfig();
        }
        log.info("Configuring Camel MLLP Route on {}:{} (autoAck={})",
                mllpConfig.getHost(), mllpConfig.getPort(), mllpConfig.isAutoAck());

        // MLLP TCP Server Route for receiving HL7 v2.4 ADT messages
        fromF("mllp://%s:%d?autoAck=%b", mllpConfig.getHost(), mllpConfig.getPort(), mllpConfig.isAutoAck())
                .routeId("hl7-mllp-adt-receiver")
                .log("Received HL7 message via MLLP interface")
                .process(incomingAdtMessageProcessorWrapper)
                .log("Completed processing HL7 message, ACK prepared");

        // Direct Route for internal routing / testing
        from("direct:adt-events")
                .routeId("direct-adt-processor")
                .process(incomingAdtMessageProcessorWrapper);
    }
}
