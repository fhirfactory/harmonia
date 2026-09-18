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

package net.fhirfactory.harmonia.erga.distribution;

import jakarta.enterprise.context.Dependent;
import net.fhirfactory.harmonia.erga.base.ErgonBase;
import net.fhirfactory.harmonia.model.ergon.ErgonPayload;
import net.fhirfactory.harmonia.model.pragma.Pragma;
import net.fhirfactory.harmonia.model.pragma.PragmaCheckpoint;
import net.fhirfactory.harmonia.model.pragma.PragmaStatus;
import net.fhirfactory.harmonia.model.topic.Topic;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Ergon Activity for internal fan-out of ingested PAS ADT trigger events to
 * destination systems (EMR, LMS, RIS-PAC) via dedicated Petasos outbound queues.
 */
@Dependent
public class AdtDistributionErgon extends ErgonBase {

    private static final Logger log = LoggerFactory.getLogger(AdtDistributionErgon.class);

    public static final String DEFAULT_ACTIVITY_ID = "adt-distribution";
    public static final String DEFAULT_ACTIVITY_NAME = "ADT Event Multi-Destination Distribution Activity";

    public static final String QUEUE_EMR_ADT = "petasos.queue.mllp.outbound.emr_adt";
    public static final String QUEUE_LMS_ADT = "petasos.queue.mllp.outbound.lms_adt";
    public static final String QUEUE_RIS_ADT = "petasos.queue.mllp.outbound.ris_adt";

    private List<String> targetQueues = new ArrayList<>(List.of(QUEUE_EMR_ADT, QUEUE_LMS_ADT, QUEUE_RIS_ADT));

    public AdtDistributionErgon() {
        super(DEFAULT_ACTIVITY_ID, DEFAULT_ACTIVITY_NAME);
    }

    public AdtDistributionErgon(CamelContext camelContext) {
        super(camelContext, DEFAULT_ACTIVITY_ID, DEFAULT_ACTIVITY_NAME);
    }

    @Override
    public void processActivity(Exchange exchange) throws Exception {
        Object body = exchange.getIn().getBody();
        String rawHl7 = extractRawPayload(body);

        log.info("[AdtDistributionErgon] Processing ADT fan-out for message (length: {} chars) to {} destinations",
                rawHl7 != null ? rawHl7.length() : 0, targetQueues.size());

        if (body instanceof Pragma) {
            Pragma pragma = (Pragma) body;

            int order = pragma.getOutput() != null ? pragma.getOutput().size() : 0;
            for (String targetQueue : targetQueues) {
                Topic egressTopic = Topic.forEgress("ADT", extractTriggerEvent(rawHl7), "harmonia", targetQueue, targetQueue);
                ErgonPayload dispatchPayload = ErgonPayload.fromJson(order++, egressTopic, egressTopic, rawHl7);
                pragma.addOutput(dispatchPayload);

                // REC-002: Record granular destination fan-out checkpoint sub-state
                PragmaCheckpoint cp = new PragmaCheckpoint(pragma.getPragmaId(), DEFAULT_ACTIVITY_ID, "FANOUT_DISPATCH_INITIATED", PragmaStatus.IN_PROGRESS, order);
                cp.addMetadata("destinationQueue", targetQueue);
                cp.addMetadata("destinationId", targetQueue);
                cp.addMetadata("status", "QUEUED");
                pragma.addCheckpoint(cp);
            }

            exchange.getMessage().setBody(pragma);
        }

        // Set distribution headers on Camel exchange
        exchange.getMessage().setHeader("HIE_FANOUT_DESTINATIONS", String.join(",", targetQueues));
        exchange.getMessage().setHeader("HIE_FANOUT_COUNT", targetQueues.size());
    }

    private String extractTriggerEvent(String rawHl7) {
        if (rawHl7 != null && rawHl7.contains("ADT^")) {
            int idx = rawHl7.indexOf("ADT^");
            int end = rawHl7.indexOf("|", idx);
            if (end > idx) {
                return rawHl7.substring(idx + 4, end).trim();
            }
        }
        return "A01";
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

    public List<String> getTargetQueues() {
        return targetQueues;
    }

    public void setTargetQueues(List<String> targetQueues) {
        this.targetQueues = targetQueues;
    }
}
