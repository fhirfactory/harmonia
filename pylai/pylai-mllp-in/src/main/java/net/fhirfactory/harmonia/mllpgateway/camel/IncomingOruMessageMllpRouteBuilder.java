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

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import net.fhirfactory.harmonia.mllpgateway.config.MllpConfig;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Dependent
public class IncomingOruMessageMllpRouteBuilder extends RouteBuilder {

    private static final Logger log = LoggerFactory.getLogger(IncomingOruMessageMllpRouteBuilder.class);

    @Inject
    private IncomingOruMessageProcessorWrapper incomingOruMessageProcessorWrapper;

    @Inject
    private MllpConfig mllpConfig;

    public IncomingOruMessageMllpRouteBuilder() {
    }

    public IncomingOruMessageMllpRouteBuilder(MllpConfig mllpConfig,
                                             IncomingOruMessageProcessorWrapper incomingOruMessageProcessorWrapper) {
        this.mllpConfig = mllpConfig;
        this.incomingOruMessageProcessorWrapper = incomingOruMessageProcessorWrapper;
    }

    @Override
    public void configure() throws Exception {
        if (mllpConfig == null) {
            mllpConfig = new MllpConfig();
        }
        log.info("Configuring Camel MLLP ORU Routes on {}:{} (LMS) and {}:{} (RIS-PAC) (autoAck={})",
                mllpConfig.getHost(), mllpConfig.getLmsOruPort(),
                mllpConfig.getHost(), mllpConfig.getRispacOruPort(),
                mllpConfig.isAutoAck());

        // LMS ORU Inbound MLLP Route (:2102)
        fromF("mllp://%s:%d?autoAck=%b", mllpConfig.getHost(), mllpConfig.getLmsOruPort(), mllpConfig.isAutoAck())
                .routeId("hl7-mllp-lms-oru-receiver")
                .log("Received HL7 ORU (LMS) message via MLLP interface")
                .process(incomingOruMessageProcessorWrapper)
                .log("Completed processing HL7 ORU message, ACK prepared");

        // RIS-PAC ORU Inbound MLLP Route (:2103)
        fromF("mllp://%s:%d?autoAck=%b", mllpConfig.getHost(), mllpConfig.getRispacOruPort(), mllpConfig.isAutoAck())
                .routeId("hl7-mllp-rispac-oru-receiver")
                .log("Received HL7 ORU (RIS-PAC) message via MLLP interface")
                .process(incomingOruMessageProcessorWrapper)
                .log("Completed processing HL7 ORU message, ACK prepared");

        // Direct Route for internal routing / testing
        from("direct:oru-events")
                .routeId("direct-oru-processor")
                .process(incomingOruMessageProcessorWrapper);
    }

    public IncomingOruMessageProcessorWrapper getIncomingOruMessageProcessorWrapper() {
        return incomingOruMessageProcessorWrapper;
    }

    public void setIncomingOruMessageProcessorWrapper(IncomingOruMessageProcessorWrapper incomingOruMessageProcessorWrapper) {
        this.incomingOruMessageProcessorWrapper = incomingOruMessageProcessorWrapper;
    }

    public MllpConfig getMllpConfig() {
        return mllpConfig;
    }

    public void setMllpConfig(MllpConfig mllpConfig) {
        this.mllpConfig = mllpConfig;
    }
}
