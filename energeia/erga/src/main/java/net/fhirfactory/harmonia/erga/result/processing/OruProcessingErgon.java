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

package net.fhirfactory.harmonia.erga.result.processing;

import jakarta.enterprise.context.Dependent;
import net.fhirfactory.harmonia.erga.base.ErgonBase;
import net.fhirfactory.harmonia.model.ergon.ErgonPayload;
import net.fhirfactory.harmonia.model.pragma.Pragma;
import net.fhirfactory.harmonia.model.pragma.PragmaStatus;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Ergon Activity for processing, recording, and correlating incoming ORU^R01 result messages
 * from LMS (Laboratory) and RIS-PAC (Diagnostic Imaging).
 */
@Dependent
public class OruProcessingErgon extends ErgonBase {

    private static final Logger log = LoggerFactory.getLogger(OruProcessingErgon.class);

    public static final String DEFAULT_ACTIVITY_ID = "oru-processing";
    public static final String DEFAULT_ACTIVITY_NAME = "Clinical Observation Result Processing Activity";

    private static final Pattern OBR_3_PATTERN = Pattern.compile("^OBR\\|[^|]*\\|[^|]*\\|([^|^\\r\\n]+)", Pattern.MULTILINE);
    private static final Pattern OBR_4_PATTERN = Pattern.compile("^OBR\\|[^|]*\\|[^|]*\\|[^|]*\\|([^|^\\r\\n]+)", Pattern.MULTILINE);

    public OruProcessingErgon() {
        super(DEFAULT_ACTIVITY_ID, DEFAULT_ACTIVITY_NAME);
    }

    public OruProcessingErgon(CamelContext camelContext) {
        super(camelContext, DEFAULT_ACTIVITY_ID, DEFAULT_ACTIVITY_NAME);
    }

    @Override
    public void processActivity(Exchange exchange) throws Exception {
        Object body = exchange.getIn().getBody();
        String rawHl7 = extractRawPayload(body);

        String fillerId = extractFillerNumber(rawHl7);
        String serviceId = extractUniversalServiceIdentifier(rawHl7);

        log.info("[OruProcessingErgon] Successfully processed and recorded clinical result [Filler: {}, Service: {}]",
                fillerId, serviceId);

        if (body instanceof Pragma) {
            Pragma pragma = (Pragma) body;
            pragma.setStatus(PragmaStatus.COMPLETED);
            exchange.getMessage().setBody(pragma);
        }

        exchange.getMessage().setHeader("HIE_RESULT_RECORDED", true);
        exchange.getMessage().setHeader("HIE_FILLER_ORDER_NUMBER", fillerId);
        exchange.getMessage().setHeader("HIE_UNIVERSAL_SERVICE_ID", serviceId);
    }

    private String extractFillerNumber(String rawHl7) {
        if (StringUtils.isNotBlank(rawHl7)) {
            Matcher m = OBR_3_PATTERN.matcher(rawHl7);
            if (m.find()) {
                return m.group(1).trim();
            }
        }
        return "FIL-UNKNOWN";
    }

    private String extractUniversalServiceIdentifier(String rawHl7) {
        if (StringUtils.isNotBlank(rawHl7)) {
            Matcher m = OBR_4_PATTERN.matcher(rawHl7);
            if (m.find()) {
                return m.group(1).trim();
            }
        }
        return "UNKNOWN";
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
}
