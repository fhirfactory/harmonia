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

package net.fhirfactory.harmonia.erga.order.routing;

import jakarta.enterprise.context.Dependent;
import net.fhirfactory.harmonia.erga.base.ErgonBase;
import net.fhirfactory.harmonia.model.ergon.ErgonPayload;
import net.fhirfactory.harmonia.model.pragma.Pragma;
import net.fhirfactory.harmonia.model.topic.Topic;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Ergon Activity for deterministic routing of ORM^O01 clinical orders to LMS (Lab) or RIS-PAC (Imaging)
 * based on OBR-4 Universal Service Identifier.
 */
@Dependent
public class OrmRoutingErgon extends ErgonBase {

    private static final Logger log = LoggerFactory.getLogger(OrmRoutingErgon.class);

    public static final String DEFAULT_ACTIVITY_ID = "orm-routing";
    public static final String DEFAULT_ACTIVITY_NAME = "Deterministic ORM Order Routing Activity";

    public static final String QUEUE_LMS_ORM = "petasos.queue.mllp.outbound.lms_orm";
    public static final String QUEUE_RIS_ORM = "petasos.queue.mllp.outbound.ris_orm";

    private static final Pattern OBR_4_PATTERN = Pattern.compile("^OBR\\|[^|]*\\|[^|]*\\|[^|]*\\|([^|^\\r\\n]+)", Pattern.MULTILINE);

    private String lmsOrmQueue = QUEUE_LMS_ORM;
    private String rispacOrmQueue = QUEUE_RIS_ORM;

    public OrmRoutingErgon() {
        super(DEFAULT_ACTIVITY_ID, DEFAULT_ACTIVITY_NAME);
    }

    public OrmRoutingErgon(CamelContext camelContext) {
        super(camelContext, DEFAULT_ACTIVITY_ID, DEFAULT_ACTIVITY_NAME);
    }

    @Override
    public void processActivity(Exchange exchange) throws Exception {
        Object body = exchange.getIn().getBody();
        String rawHl7 = extractRawPayload(body);
        String obr4 = extractUniversalServiceIdentifier(rawHl7);

        String targetQueue = determineTargetQueue(obr4);
        String destinationSystem = targetQueue.contains("ris") ? "RISPAC" : "LMS";

        log.info("[OrmRoutingErgon] Routing ORM order [OBR-4: {}] -> {} ({})", obr4, destinationSystem, targetQueue);

        if (body instanceof Pragma) {
            Pragma pragma = (Pragma) body;
            int order = pragma.getOutput() != null ? pragma.getOutput().size() : 0;
            Topic egressTopic = Topic.forEgress("ORM", "O01", "harmonia", targetQueue, destinationSystem);
            ErgonPayload dispatchPayload = ErgonPayload.fromJson(order, egressTopic, egressTopic, rawHl7);
            pragma.addOutput(dispatchPayload);
            exchange.getMessage().setBody(pragma);
        }

        exchange.getMessage().setHeader("HIE_ROUTED_DESTINATION", destinationSystem);
        exchange.getMessage().setHeader("HIE_TARGET_QUEUE", targetQueue);
        exchange.getMessage().setHeader("HIE_UNIVERSAL_SERVICE_ID", obr4);
    }

    public String determineTargetQueue(String universalServiceIdentifier) {
        if (universalServiceIdentifier == null) {
            return lmsOrmQueue;
        }
        String id = universalServiceIdentifier.trim().toUpperCase();
        if (id.startsWith("RAD") || id.startsWith("IMG") || id.startsWith("XR") ||
                id.startsWith("CT") || id.startsWith("MRI") || id.startsWith("US") ||
                id.contains("CHEST") || id.contains("HEAD") || id.contains("ABDOMEN") || id.contains("BRAIN") || id.contains("KNEE")) {
            return rispacOrmQueue;
        }
        return lmsOrmQueue;
    }

    private String extractUniversalServiceIdentifier(String rawHl7) {
        if (StringUtils.isNotBlank(rawHl7)) {
            Matcher m = OBR_4_PATTERN.matcher(rawHl7);
            if (m.find()) {
                return m.group(1).trim();
            }
        }
        return "CBC";
    }

    private String extractRawPayload(Object body) {
        if (body instanceof Pragma) {
            Pragma pragma = (Pragma) body;
            for (ErgonPayload ep : pragma.getInput()) {
                if (ep.getJsonString() != null && isHl7Message(ep.getJsonString())) {
                    return ep.getJsonString();
                }
            }
            for (ErgonPayload ep : pragma.getInput()) {
                if (ep.getJsonString() != null) {
                    return ep.getJsonString();
                }
            }
        }
        if (body instanceof String) {
            return (String) body;
        } else if (body instanceof byte[]) {
            return new String((byte[]) body, StandardCharsets.UTF_8);
        }
        return body != null ? body.toString() : null;
    }

    private boolean isHl7Message(String payload) {
        if (StringUtils.isBlank(payload)) {
            return false;
        }
        return payload.startsWith("MSH|") || payload.contains("\nMSH|") || payload.contains("\rMSH|");
    }

    public String getLmsOrmQueue() {
        return lmsOrmQueue;
    }

    public void setLmsOrmQueue(String lmsOrmQueue) {
        this.lmsOrmQueue = lmsOrmQueue;
    }

    public String getRispacOrmQueue() {
        return rispacOrmQueue;
    }

    public void setRispacOrmQueue(String rispacOrmQueue) {
        this.rispacOrmQueue = rispacOrmQueue;
    }
}
