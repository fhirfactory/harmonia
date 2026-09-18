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
public class IncomingOrmMessageMllpRouteBuilder extends RouteBuilder {

    private static final Logger log = LoggerFactory.getLogger(IncomingOrmMessageMllpRouteBuilder.class);

    @Inject
    private IncomingOrmMessageProcessorWrapper incomingOrmMessageProcessorWrapper;

    @Inject
    private MllpConfig mllpConfig;

    public IncomingOrmMessageMllpRouteBuilder() {
    }

    public IncomingOrmMessageMllpRouteBuilder(MllpConfig mllpConfig,
                                             IncomingOrmMessageProcessorWrapper incomingOrmMessageProcessorWrapper) {
        this.mllpConfig = mllpConfig;
        this.incomingOrmMessageProcessorWrapper = incomingOrmMessageProcessorWrapper;
    }

    @Override
    public void configure() throws Exception {
        if (mllpConfig == null) {
            mllpConfig = new MllpConfig();
        }
        log.info("Configuring Camel MLLP ORM Route on {}:{} (autoAck={})",
                mllpConfig.getHost(), mllpConfig.getEmrOrmPort(), mllpConfig.isAutoAck());

        // MLLP TCP Server Route for receiving HL7 v2.4 ORM messages
        fromF("mllp://%s:%d?autoAck=%b", mllpConfig.getHost(), mllpConfig.getEmrOrmPort(), mllpConfig.isAutoAck())
                .routeId("hl7-mllp-orm-receiver")
                .log("Received HL7 ORM message via MLLP interface")
                .process(incomingOrmMessageProcessorWrapper)
                .log("Completed processing HL7 ORM message, ACK prepared");

        // Direct Route for internal routing / testing
        from("direct:orm-events")
                .routeId("direct-orm-processor")
                .process(incomingOrmMessageProcessorWrapper);
    }

    public IncomingOrmMessageProcessorWrapper getIncomingOrmMessageProcessorWrapper() {
        return incomingOrmMessageProcessorWrapper;
    }

    public void setIncomingOrmMessageProcessorWrapper(IncomingOrmMessageProcessorWrapper incomingOrmMessageProcessorWrapper) {
        this.incomingOrmMessageProcessorWrapper = incomingOrmMessageProcessorWrapper;
    }

    public MllpConfig getMllpConfig() {
        return mllpConfig;
    }

    public void setMllpConfig(MllpConfig mllpConfig) {
        this.mllpConfig = mllpConfig;
    }
}
