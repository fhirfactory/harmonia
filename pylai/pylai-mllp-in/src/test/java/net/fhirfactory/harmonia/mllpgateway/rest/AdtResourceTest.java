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

package net.fhirfactory.harmonia.mllpgateway.rest;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import jakarta.ws.rs.core.Response;
import net.fhirfactory.harmonia.mllpgateway.hl7.IncomingAdtMessageProcessor;
import net.fhirfactory.harmonia.mllpgateway.service.DefaultCommunicationService;
import net.fhirfactory.harmonia.mllpgateway.service.DefaultTaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AdtResourceTest {

    private DefaultTaskService taskService;
    private DefaultCommunicationService communicationService;
    private IParser fhirParser;
    private AdtResource adtResource;

    @BeforeEach
    void setUp() {
        taskService = new DefaultTaskService();
        taskService.clear();
        communicationService = new DefaultCommunicationService();
        communicationService.clear();
        fhirParser = FhirContext.forR5().newJsonParser().setPrettyPrint(true);
        IncomingAdtMessageProcessor transformer = new IncomingAdtMessageProcessor(taskService, communicationService);
        adtResource = new AdtResource(transformer, fhirParser);
    }

    @Test
    @DisplayName("Ingest HL7 v2.4 ADT message via JAX-RS POST")
    void testIngestAdtSuccess() {
        String hl7A04 = "MSH|^~\\&|REG|CLINIC|HIE|HIE_IM|20260907140000||ADT^A04|MSG-A04-100|P|2.4\r" +
                "EVN|A04|20260907140000\r" +
                "PID|1||PAT55555^^^CLINIC^MR||WILLIAMS^ROBERT||19651130|M\r" +
                "PV1|1|O|OPD^CLINIC1^^CLINIC||||DOC05^GREEN^THOMAS^^DR|||||||||||V998877\r";

        Response response = adtResource.ingestAdt(hl7A04);
        assertThat(response.getStatus()).isEqualTo(200);

        String json = (String) response.getEntity();
        assertThat(json).contains("\"success\":true");
        assertThat(json).contains("\"triggerEvent\":\"A04\"");
        assertThat(json).contains("\"patientId\":\"PAT55555\"");
        assertThat(json).contains("\"patientName\":\"ROBERT WILLIAMS\"");
        assertThat(json).contains("MSA|AA|MSG-A04-100");
        assertThat(json).contains("Communication");
        assertThat(json).contains("comm-MSG-A04-100");
        assertThat(json).contains("Task");

        assertThat(communicationService.count()).isEqualTo(1);
        assertThat(communicationService.getById("comm-MSG-A04-100")).isPresent();
        assertThat(taskService.count()).isEqualTo(1);
        assertThat(taskService.getById("MSG-A04-100")).isPresent();
    }

    @Test
    @DisplayName("Ingest blank payload returns 400 Bad Request")
    void testIngestAdtBlankPayload() {
        Response response = adtResource.ingestAdt("");
        assertThat(response.getStatus()).isEqualTo(400);

        String json = (String) response.getEntity();
        assertThat(json).contains("\"success\":false");
    }
}
