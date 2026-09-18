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

class OrmRoutingErgonTest {

    private OrmRoutingErgon ergon;
    private CamelContext camelContext;

    @BeforeEach
    void setUp() {
        camelContext = new DefaultCamelContext();
        ergon = new OrmRoutingErgon(camelContext);
    }

    @Test
    @DisplayName("Deterministic routing of Laboratory ORM to LMS queue")
    void testRouteLabOrder() throws Exception {
        String labOrm = "MSH|^~\\&|EMR|FAC|HARMONIA|HIE|20260915120000||ORM^O01|MSG-101|P|2.4\r" +
                "PID|1||PAT-101^^^MRN||Smith^John\r" +
                "ORC|NW|ORD-1001|||IP||^^^R||20260915120000\r" +
                "OBR|1|ORD-1001||CBC^Complete Blood Count^LN\r";

        Pragma pragma = Pragma.builder().pragmaId("pragma-101").correlationId("task-101").build();
        ErgonPayload input = ErgonPayload.fromJson(0, null, null, labOrm);
        pragma.addInput(input);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(pragma);

        ergon.processActivity(exchange);

        assertThat(exchange.getMessage().getHeader("HIE_ROUTED_DESTINATION")).isEqualTo("LMS");
        assertThat(exchange.getMessage().getHeader("HIE_TARGET_QUEUE")).isEqualTo(OrmRoutingErgon.QUEUE_LMS_ORM);
    }

    @Test
    @DisplayName("Deterministic routing of Diagnostic Imaging ORM to RIS-PAC queue")
    void testRouteImagingOrder() throws Exception {
        String radOrm = "MSH|^~\\&|EMR|FAC|HARMONIA|HIE|20260915120000||ORM^O01|MSG-102|P|2.4\r" +
                "PID|1||PAT-102^^^MRN||Smith^Jane\r" +
                "ORC|NW|ORD-1002|||IP||^^^R||20260915120000\r" +
                "OBR|1|ORD-1002||XR_CHEST^Chest X-Ray PA and Lateral^RAD\r";

        Pragma pragma = Pragma.builder().pragmaId("pragma-102").correlationId("task-102").build();
        ErgonPayload input = ErgonPayload.fromJson(0, null, null, radOrm);
        pragma.addInput(input);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(pragma);

        ergon.processActivity(exchange);

        assertThat(exchange.getMessage().getHeader("HIE_ROUTED_DESTINATION")).isEqualTo("RISPAC");
        assertThat(exchange.getMessage().getHeader("HIE_TARGET_QUEUE")).isEqualTo(OrmRoutingErgon.QUEUE_RIS_ORM);
    }
}
