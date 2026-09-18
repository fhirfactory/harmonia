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

import net.fhirfactory.harmonia.model.ergon.ErgonPayload;
import net.fhirfactory.harmonia.model.pragma.Pragma;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AdtDistributionErgonTest {

    private AdtDistributionErgon ergon;
    private CamelContext camelContext;

    @BeforeEach
    void setUp() {
        camelContext = new DefaultCamelContext();
        ergon = new AdtDistributionErgon(camelContext);
    }

    @Test
    @DisplayName("Fan out ADT message to EMR, LMS, and RIS-PAC queues")
    void testAdtFanOut() throws Exception {
        String adtHl7 = "MSH|^~\\&|PAS|FAC|HARMONIA|HIE|20260915120000||ADT^A01|MSG-101|P|2.4\r" +
                "PID|1||PAT-101^^^MRN||Smith^John\r";

        Pragma pragma = Pragma.builder().pragmaId("pragma-101").correlationId("task-101").build();
        ErgonPayload input = ErgonPayload.fromJson(0, null, null, adtHl7);
        pragma.addInput(input);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(pragma);

        ergon.processActivity(exchange);

        Pragma outputPragma = exchange.getMessage().getBody(Pragma.class);
        assertThat(outputPragma).isNotNull();
        assertThat(outputPragma.getOutput()).hasSize(3);
        assertThat(outputPragma.getCheckpoints()).hasSize(3);
        assertThat(outputPragma.getCheckpoints().get(0).getStageName()).isEqualTo("FANOUT_DISPATCH_INITIATED");
        assertThat(outputPragma.getCheckpoints().get(0).getMetadata().get("destinationQueue")).isEqualTo(AdtDistributionErgon.QUEUE_EMR_ADT);
        assertThat(outputPragma.getCheckpoints().get(1).getMetadata().get("destinationQueue")).isEqualTo(AdtDistributionErgon.QUEUE_LMS_ADT);
        assertThat(outputPragma.getCheckpoints().get(2).getMetadata().get("destinationQueue")).isEqualTo(AdtDistributionErgon.QUEUE_RIS_ADT);

        assertThat(exchange.getMessage().getHeader("HIE_FANOUT_COUNT")).isEqualTo(3);
        assertThat(exchange.getMessage().getHeader("HIE_FANOUT_DESTINATIONS", String.class))
                .contains(AdtDistributionErgon.QUEUE_EMR_ADT)
                .contains(AdtDistributionErgon.QUEUE_LMS_ADT)
                .contains(AdtDistributionErgon.QUEUE_RIS_ADT);
    }
}
