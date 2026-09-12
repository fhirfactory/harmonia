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
public class IncomingMfnMessageMllpRouteBuilder extends RouteBuilder {

    private static final Logger log = LoggerFactory.getLogger(IncomingMfnMessageMllpRouteBuilder.class);

    private MllpConfig mllpConfig;
    private IncomingMfnMessageProcessorWrapper incomingMfnMessageProcessorWrapper;

    public IncomingMfnMessageMllpRouteBuilder() {
    }

    @Inject
    public IncomingMfnMessageMllpRouteBuilder(MllpConfig mllpConfig, IncomingMfnMessageProcessorWrapper incomingMfnMessageProcessorWrapper) {
        this.mllpConfig = mllpConfig;
        this.incomingMfnMessageProcessorWrapper = incomingMfnMessageProcessorWrapper;
    }

    public void setMllpConfig(MllpConfig mllpConfig) {
        this.mllpConfig = mllpConfig;
    }

    public MllpConfig getMllpConfig() {
        return mllpConfig;
    }

    public void setIncomingMfnMessageProcessorWrapper(IncomingMfnMessageProcessorWrapper incomingMfnMessageProcessorWrapper) {
        this.incomingMfnMessageProcessorWrapper = incomingMfnMessageProcessorWrapper;
    }

    public void setMfnTriggerEventProcessor(IncomingMfnMessageProcessorWrapper incomingMfnMessageProcessorWrapper) {
        this.incomingMfnMessageProcessorWrapper = incomingMfnMessageProcessorWrapper;
    }

    public IncomingMfnMessageProcessorWrapper getIncomingMfnMessageProcessorWrapper() {
        return incomingMfnMessageProcessorWrapper;
    }

    @Override
    public void configure() throws Exception {
        if (mllpConfig == null) {
            mllpConfig = new MllpConfig();
        }
        log.info("Configuring Camel MLLP MFN Route on {}:{} (autoAck={})",
                mllpConfig.getHost(), mllpConfig.getMfnPort(), mllpConfig.isAutoAck());

        // MLLP TCP Server Route for receiving HL7 v2.4 MFN messages
        fromF("mllp://%s:%d?autoAck=%b", mllpConfig.getHost(), mllpConfig.getMfnPort(), mllpConfig.isAutoAck())
                .routeId("hl7-mllp-mfn-receiver")
                .log("Received HL7 MFN message via MLLP interface")
                .process(incomingMfnMessageProcessorWrapper)
                .log("Completed processing HL7 MFN message, ACK prepared");

        // Direct Route for internal routing / testing
        from("direct:mfn-events")
                .routeId("direct-mfn-processor")
                .process(incomingMfnMessageProcessorWrapper);
    }
}
