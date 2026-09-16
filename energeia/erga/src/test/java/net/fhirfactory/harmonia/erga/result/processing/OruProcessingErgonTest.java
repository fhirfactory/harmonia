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

import net.fhirfactory.harmonia.model.ergon.ErgonPayload;
import net.fhirfactory.harmonia.model.pragma.Pragma;
import net.fhirfactory.harmonia.model.pragma.PragmaStatus;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OruProcessingErgonTest {

    private OruProcessingErgon ergon;
    private CamelContext camelContext;

    @BeforeEach
    void setUp() {
        camelContext = new DefaultCamelContext();
        ergon = new OruProcessingErgon(camelContext);
    }

    @Test
    @DisplayName("Process ORU result and record completion")
    void testProcessOruResult() throws Exception {
        String oruHl7 = "MSH|^~\\&|LMS|FAC|HARMONIA|HIE|20260915120000||ORU^R01|MSG-201|P|2.4\r" +
                "PID|1||PAT-101^^^MRN||Smith^John\r" +
                "OBR|1|ORD-1001|LMS-2001|CBC^Complete Blood Count^LN|||20260915120000\r" +
                "OBX|1|NM|718-7^Haemoglobin^LN||140.0|g/L|130-180|N|||F\r";

        Pragma pragma = Pragma.builder().pragmaId("pragma-201").correlationId("task-201").build();
        ErgonPayload input = ErgonPayload.fromJson(0, null, null, oruHl7);
        pragma.addInput(input);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(pragma);

        ergon.processActivity(exchange);

        Pragma outputPragma = exchange.getMessage().getBody(Pragma.class);
        assertThat(outputPragma).isNotNull();
        assertThat(outputPragma.getStatus()).isEqualTo(PragmaStatus.COMPLETED);

        assertThat(exchange.getMessage().getHeader("HIE_RESULT_RECORDED")).isEqualTo(true);
        assertThat(exchange.getMessage().getHeader("HIE_FILLER_ORDER_NUMBER")).isEqualTo("LMS-2001");
        assertThat(exchange.getMessage().getHeader("HIE_UNIVERSAL_SERVICE_ID")).isEqualTo("CBC");
    }
}
