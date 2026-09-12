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

package net.fhirfactory.hie.taskprocessors;

import ca.uhn.fhir.context.FhirContext;
import net.fhirfactory.hie.taskprocessors.base.TaskProcessingActivity;
import net.fhirfactory.hie.taskprocessors.patient.identity.PatientIdentityUpdate;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.Patient;
import org.hl7.fhir.r5.model.Reference;
import org.hl7.fhir.r5.model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class PatientIdentityUpdateTest {

    private CamelContext camelContext;
    private ProducerTemplate producerTemplate;
    private PatientIdentityUpdate activity;
    private FhirContext fhirContext;

    @BeforeEach
    void setUp() {
        camelContext = new DefaultCamelContext();
        producerTemplate = camelContext.createProducerTemplate();
        activity = new PatientIdentityUpdate();
        fhirContext = FhirContext.forR5();
    }

    @AfterEach
    void tearDown() throws Exception {
        if (producerTemplate != null) {
            producerTemplate.stop();
        }
        if (camelContext != null) {
            camelContext.stop();
        }
    }

    @Test
    void testActivityMetadataAndInheritance() {
        assertThat(activity).isInstanceOf(TaskProcessingActivity.class);
        assertThat(activity).isInstanceOf(RouteBuilder.class);
        assertThat(activity.getActivityId()).isEqualTo(PatientIdentityUpdate.DEFAULT_ACTIVITY_ID);
        assertThat(activity.getActivityName()).isEqualTo(PatientIdentityUpdate.DEFAULT_ACTIVITY_NAME);
        assertThat(activity.generateRouteId()).isEqualTo("activity-patient-identity-update");
    }

    @Test
    void testParseHl7PidSegment() {
        String hl7Message = "MSH|^~\\&|PAS|HOSP|HIE|REC|20260908120000||ADT^A01|MSG-001|P|2.4\r" +
                "PID|||MRN12345^^^HOSP||DOE^JOHN^A^JR^MR||19800101|M|||123 MAIN ST^^SPRINGFIELD^IL^62701^USA||(555)123-4567|(555)987-6543|||ACC-987|SSN-123\r" +
                "PV1||I|WARD-A^101^1";

        Patient patient = activity.parseHl7PidSegment(hl7Message);

        assertThat(patient).isNotNull();
        assertThat(patient.getIdPart()).isEqualTo("MRN12345");
        assertThat(patient.getGender()).isEqualTo(Enumerations.AdministrativeGender.MALE);
        assertThat(activity.extractMrn(patient)).isEqualTo("MRN12345");

        assertThat(patient.hasName()).isTrue();
        assertThat(patient.getNameFirstRep().getFamily()).isEqualTo("DOE");
        assertThat(patient.getNameFirstRep().getGivenAsSingleString()).isEqualTo("JOHN A");
        assertThat(patient.getNameFirstRep().getPrefixAsSingleString()).isEqualTo("MR");
        assertThat(patient.getNameFirstRep().getSuffixAsSingleString()).isEqualTo("JR");
        assertThat(activity.extractFullName(patient)).isEqualTo("MR JOHN A DOE JR");

        assertThat(patient.hasBirthDate()).isTrue();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        assertThat(sdf.format(patient.getBirthDate())).isEqualTo("1980-01-01");

        assertThat(patient.hasAddress()).isTrue();
        assertThat(patient.getAddressFirstRep().getLine().get(0).getValue()).isEqualTo("123 MAIN ST");
        assertThat(patient.getAddressFirstRep().getCity()).isEqualTo("SPRINGFIELD");
        assertThat(patient.getAddressFirstRep().getState()).isEqualTo("IL");
        assertThat(patient.getAddressFirstRep().getPostalCode()).isEqualTo("62701");
        assertThat(patient.getAddressFirstRep().getCountry()).isEqualTo("USA");

        assertThat(patient.hasTelecom()).isTrue();
        assertThat(patient.getTelecom().get(0).getValue()).isEqualTo("(555)123-4567");
    }

    @Test
    void testParseFhirPatientJson() {
        Patient original = new Patient();
        original.setId("Patient/PAT-999");
        original.addIdentifier().setSystem("http://example.org/patients").setValue("PAT-999");
        original.addName().setFamily("Smith").addGiven("Alice").setText("Alice Smith");
        original.setGender(Enumerations.AdministrativeGender.FEMALE);

        String json = fhirContext.newJsonParser().encodeResourceToString(original);
        Patient parsed = activity.parseJsonPayload(json);

        assertThat(parsed).isNotNull();
        assertThat(parsed.getIdPart()).isEqualTo("PAT-999");
        assertThat(parsed.getGender()).isEqualTo(Enumerations.AdministrativeGender.FEMALE);
        assertThat(activity.extractFullName(parsed)).contains("Alice");
    }

    @Test
    void testParseFhirTaskJson() {
        Task task = new Task();
        task.setId("Task/TASK-555");
        task.setStatus(Task.TaskStatus.REQUESTED);
        task.setFor(new Reference("Patient/PAT-888").setDisplay("Bob Builder"));

        String json = fhirContext.newJsonParser().encodeResourceToString(task);
        Patient parsed = activity.parseJsonPayload(json);

        assertThat(parsed).isNotNull();
        assertThat(parsed.getIdPart()).isEqualTo("PAT-888");
        assertThat(activity.extractFullName(parsed)).isEqualTo("Bob Builder");
    }

    @Test
    void testParseTaskEventJson() {
        String taskEventJson = "{" +
                "\"taskId\":\"TASK-777\"," +
                "\"action\":\"PROCESS\"," +
                "\"status\":\"REQUESTED\"," +
                "\"gatewayInstanceId\":\"pas-gw\"," +
                "\"messageType\":\"ADT\"," +
                "\"triggerType\":\"A01\"," +
                "\"description\":\"HL7 v2.4 ADT^A01 event transformed to Task for patient Charlie Brown\"" +
                "}";

        Patient parsed = activity.parseJsonPayload(taskEventJson);

        assertThat(parsed).isNotNull();
        assertThat(parsed.getIdPart()).isEqualTo("TASK-777");
        assertThat(activity.extractFullName(parsed)).isEqualTo("Charlie Brown");
    }

    @Test
    void testParseXmlPayload() {
        Patient original = new Patient();
        original.setId("Patient/XML-PAT");
        original.addName().setFamily("Tester").addGiven("XML");
        original.setGender(Enumerations.AdministrativeGender.OTHER);

        String xml = fhirContext.newXmlParser().encodeResourceToString(original);
        Patient parsed = activity.parseXmlPayload(xml);

        assertThat(parsed).isNotNull();
        assertThat(parsed.getIdPart()).isEqualTo("XML-PAT");
        assertThat(parsed.getGender()).isEqualTo(Enumerations.AdministrativeGender.OTHER);
    }

    @Test
    void testProcessPatientIdentityInCamelRoute() throws Exception {
        activity.setInputEndpoint("direct:patient-identity-input");
        activity.setOutputEndpoint("mock:patient-identity-output");

        camelContext.addRoutes(activity);
        camelContext.start();

        String hl7 = "MSH|^~\\&|PAS|HOSP|HIE|REC|20260908120000||ADT^A01|MSG-001|P|2.4\r" +
                "PID|||MRN-ROUTE-123^^^HOSP||TAYLOR^JANE^M^^MS||19920515|F|||456 ELM ST^^BOSTON^MA^02101^USA";

        Exchange resultExchange = producerTemplate.request("direct:patient-identity-input", exchange -> {
            exchange.getMessage().setBody(hl7);
        });

        assertThat(resultExchange).isNotNull();
        assertThat(resultExchange.getMessage().getHeader(PatientIdentityUpdate.HEADER_PATIENT_ID)).isEqualTo("MRN-ROUTE-123");
        assertThat(resultExchange.getMessage().getHeader(PatientIdentityUpdate.HEADER_PATIENT_MRN)).isEqualTo("MRN-ROUTE-123");
        assertThat(resultExchange.getMessage().getHeader(PatientIdentityUpdate.HEADER_PATIENT_NAME)).isEqualTo("MS JANE M TAYLOR");
        assertThat(resultExchange.getMessage().getHeader(PatientIdentityUpdate.HEADER_PATIENT_UPDATED)).isEqualTo(true);

        net.fhirfactory.hie.model.TaskEvent taskEvent = resultExchange.getMessage().getBody(net.fhirfactory.hie.model.TaskEvent.class);
        assertThat(taskEvent).isNotNull();
        assertThat(taskEvent.getAction()).isEqualTo("PROCESS");
        assertThat(taskEvent.getStatus()).isEqualTo("requested");
        assertThat(taskEvent.getDescription()).contains("MS JANE M TAYLOR");

        Optional<Task> cachedTask = activity.getTaskCacheService().getTask(taskEvent.getTaskId());
        assertThat(cachedTask).isPresent();
        assertThat(cachedTask.get().getFor().getDisplay()).isEqualTo("MS JANE M TAYLOR");

        // Verify discrete output components and Provenance
        assertThat(resultExchange.getProperty(TaskProcessingActivity.PROPERTY_PROVENANCE)).isNotNull();
        org.hl7.fhir.r5.model.Provenance prov = (org.hl7.fhir.r5.model.Provenance) resultExchange.getProperty(TaskProcessingActivity.PROPERTY_PROVENANCE);
        assertThat(prov.getTarget()).isNotEmpty();
        assertThat(prov.getEntity()).isNotEmpty();
    }

    @Test
    void testEdgeCasesAndFallbacks() {
        // 1. Empty payload
        Patient emptyPatient = activity.parseHl7PidSegment("");
        assertThat(emptyPatient).isNotNull();
        assertThat(emptyPatient.getIdPart()).isNotBlank();

        // 2. HL7 message missing PID
        String noPidHl7 = "MSH|^~\\&|PAS|HOSP|HIE|REC|20260908120000||ADT^A01|MSG-001|P|2.4";
        Patient noPidPatient = activity.parseHl7PidSegment(noPidHl7);
        assertThat(noPidPatient).isNotNull();

        // 3. Date parsing robustness
        Date d1 = activity.parseHl7Date("19850620");
        assertThat(d1).isNotNull();
        Date d2 = activity.parseHl7Date("19850620153000");
        assertThat(d2).isNotNull();
        Date d3 = activity.parseHl7Date("1985-06-20");
        assertThat(d3).isNotNull();
        Date dInvalid = activity.parseHl7Date("invalid-date-string");
        assertThat(dInvalid).isNull();

        // 4. Gender mapping
        assertThat(activity.mapAdministrativeGender("M")).isEqualTo(Enumerations.AdministrativeGender.MALE);
        assertThat(activity.mapAdministrativeGender("female")).isEqualTo(Enumerations.AdministrativeGender.FEMALE);
        assertThat(activity.mapAdministrativeGender("o")).isEqualTo(Enumerations.AdministrativeGender.OTHER);
        assertThat(activity.mapAdministrativeGender("xyz")).isEqualTo(Enumerations.AdministrativeGender.UNKNOWN);
        assertThat(activity.mapAdministrativeGender(null)).isEqualTo(Enumerations.AdministrativeGender.UNKNOWN);
    }
}
