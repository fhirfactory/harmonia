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

package net.fhirfactory.hie.mllpgateway.rest;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import jakarta.ws.rs.core.Response;
import net.fhirfactory.hie.mllpgateway.hl7.IncomingMfnMessageProcessor;
import net.fhirfactory.hie.mllpgateway.service.DefaultCommunicationService;
import net.fhirfactory.hie.mllpgateway.service.DefaultProvenanceService;
import net.fhirfactory.hie.mllpgateway.service.DefaultTaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MfnResourceTest {

    private DefaultTaskService taskService;
    private DefaultCommunicationService communicationService;
    private DefaultProvenanceService provenanceService;
    private IParser fhirParser;
    private MfnResource mfnResource;

    @BeforeEach
    void setUp() {
        taskService = new DefaultTaskService();
        taskService.clear();
        communicationService = new DefaultCommunicationService();
        communicationService.clear();
        provenanceService = new DefaultProvenanceService();
        provenanceService.clear();
        fhirParser = FhirContext.forR5().newJsonParser().setPrettyPrint(true);
        IncomingMfnMessageProcessor transformer = new IncomingMfnMessageProcessor(taskService, communicationService, provenanceService);
        mfnResource = new MfnResource(transformer, fhirParser);
    }

    @Test
    @DisplayName("Ingest HL7 v2.4 MFN message via JAX-RS POST")
    void testIngestMfnSuccess() {
        String hl7Mfn = "MSH|^~\\&|STAFF_APP|HOSPITAL_A|HIE_APP|HIE_DEST|20260910120000||MFN^M02|MSG-MFN-REST-01|P|2.4\r" +
                "MFI|PRA^Practitioner Master File|STAFF_APP|UPD|20260910120000|20260910120000|NE\r" +
                "MFE|MUP|ENTRY-01|20260910120000|DOC8888^TAYLOR^SARAH|CE\r" +
                "STF|DOC8888^TAYLOR^SARAH||TAYLOR^SARAH||F|19790315|Y|CARD|||||||||||||||||||||||||||||\r";

        Response response = mfnResource.ingestMfn(hl7Mfn);
        assertThat(response.getStatus()).isEqualTo(200);

        String json = (String) response.getEntity();
        assertThat(json).contains("\"success\":true");
        assertThat(json).contains("\"triggerEvent\":\"M02\"");
        assertThat(json).contains("\"practitionerId\":\"DOC8888\"");
        assertThat(json).contains("SARAH TAYLOR");
        assertThat(json).contains("MSA|AA|MSG-MFN-REST-01");
        assertThat(json).contains("Communication");
        assertThat(json).contains("comm-MSG-MFN-REST-01");
        assertThat(json).contains("Task");
        assertThat(json).contains("Provenance");
        assertThat(json).contains("prov-MSG-MFN-REST-01");

        assertThat(communicationService.count()).isEqualTo(1);
        assertThat(communicationService.getById("comm-MSG-MFN-REST-01")).isPresent();
        assertThat(provenanceService.count()).isEqualTo(1);
        assertThat(provenanceService.getById("prov-MSG-MFN-REST-01")).isPresent();
    }

    @Test
    @DisplayName("Ingest blank payload returns 400 Bad Request")
    void testIngestMfnBlankPayload() {
        Response response = mfnResource.ingestMfn("");
        assertThat(response.getStatus()).isEqualTo(400);

        String json = (String) response.getEntity();
        assertThat(json).contains("\"success\":false");
    }
}
