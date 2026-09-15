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

package net.fhirfactory.harmonia.erga.patient.demographics;

import ca.uhn.fhir.context.FhirContext;
import net.fhirfactory.harmonia.model.ergon.ErgonEvent;
import net.fhirfactory.harmonia.erga.base.ErgonBase;
import net.fhirfactory.harmonia.erga.patient.identity.PatientIdentityUpdateErgon;
import net.fhirfactory.harmonia.model.security.FhirConfidentialityEnum;
import net.fhirfactory.harmonia.model.security.FhirSecurityTagManager;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultCamelContext;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.Patient;
import org.hl7.fhir.r5.model.Resource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;

import static org.assertj.core.api.Assertions.assertThat;

class PatientDemographicsUpdateErgonTest {

    private CamelContext camelContext;
    private ProducerTemplate producerTemplate;
    private PatientDemographicsUpdateErgon activity;
    private FhirContext fhirContext;

    @BeforeEach
    void setUp() {
        camelContext = new DefaultCamelContext();
        producerTemplate = camelContext.createProducerTemplate();
        activity = new PatientDemographicsUpdateErgon();
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
    @DisplayName("Should verify activity metadata and inheritance")
    void testActivityMetadataAndInheritance() {
        assertThat(activity).isInstanceOf(ErgonBase.class);
        assertThat(activity).isInstanceOf(RouteBuilder.class);
        assertThat(activity.getActivityId()).isEqualTo(PatientDemographicsUpdateErgon.DEFAULT_ACTIVITY_ID);
        assertThat(activity.getActivityName()).isEqualTo(PatientDemographicsUpdateErgon.DEFAULT_ACTIVITY_NAME);
        assertThat(activity.generateRouteId()).isEqualTo("activity-patient-demographics-update");
    }

    @Test
    @DisplayName("Should extract comprehensive demographics from HL7 v2 ADT message (PID, NK1, PD1)")
    void testParseAndApplyHl7AdtDemographics() {
        String hl7Message = "MSH|^~\\&|PAS|HOSP|HIE|REC|20260908120000||ADT^A01|MSG-DEMO-001|P|2.4\r" +
                "PID|||MRN-DEMO-123^^^HOSP||SMITH^JANE^MARIE^MS^DR||19850412|F||2106-3^White^HL70005|100 MAIN ST^APT 4B^BOSTON^MA^02115^USA^H|SUFFOLK|(617)555-1111^PRN^PH|(617)555-2222^WPN^PH|ENG^English|M^Married|PROT^Protestant|ACC-7788|SSN-999-88-7777|||2186-5^Not Hispanic or Latino|BOSTON MA|Y|1|USA\r" +
                "NK1|1|SMITH^JOHN^EDWARD|SPO^Spouse|100 MAIN ST^APT 4B^BOSTON^MA^02115^USA|(617)555-3333^PRN^PH||NOK^Next of Kin\r" +
                "NK1|2|DOE^MARY|PAR^Mother|200 OAK AVE^^CAMBRIDGE^MA^02138^USA|(617)555-4444^PRN^PH||EMC^Emergency Contact\r" +
                "PD1|||FAC-01^General Hospital|DOC-99^WELBY^MARCUS^^DR\r" +
                "PV1||I|WARD-3^ROOM-302^1";

        Patient patient = new Patient();
        patient.setActive(true);
        activity.parseAndApplyHl7AdtDemographics(patient, hl7Message);

        assertThat(patient.getIdPart()).isEqualTo("MRN-DEMO-123");
        assertThat(activity.extractMrn(patient)).isEqualTo("MRN-DEMO-123");
        assertThat(patient.getGender()).isEqualTo(Enumerations.AdministrativeGender.FEMALE);

        // Name
        assertThat(patient.hasName()).isTrue();
        assertThat(patient.getNameFirstRep().getFamily()).isEqualTo("SMITH");
        assertThat(patient.getNameFirstRep().getGivenAsSingleString()).isEqualTo("JANE MARIE");
        assertThat(patient.getNameFirstRep().getPrefixAsSingleString()).isEqualTo("DR");
        assertThat(patient.getNameFirstRep().getSuffixAsSingleString()).isEqualTo("MS");

        // DOB
        assertThat(patient.hasBirthDate()).isTrue();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        assertThat(sdf.format(patient.getBirthDate())).isEqualTo("1985-04-12");

        // Marital Status
        assertThat(patient.hasMaritalStatus()).isTrue();
        assertThat(patient.getMaritalStatus().getText()).isEqualTo("Married");
        assertThat(patient.getMaritalStatus().getCodingFirstRep().getCode()).isEqualTo("M");

        // Language
        assertThat(patient.hasCommunication()).isTrue();
        assertThat(patient.getCommunicationFirstRep().getLanguage().getText()).isEqualTo("English");

        // Address
        assertThat(patient.hasAddress()).isTrue();
        assertThat(patient.getAddressFirstRep().getLine().get(0).getValue()).isEqualTo("100 MAIN ST");
        assertThat(patient.getAddressFirstRep().getLine().get(1).getValue()).isEqualTo("APT 4B");
        assertThat(patient.getAddressFirstRep().getCity()).isEqualTo("BOSTON");
        assertThat(patient.getAddressFirstRep().getState()).isEqualTo("MA");
        assertThat(patient.getAddressFirstRep().getPostalCode()).isEqualTo("02115");
        assertThat(patient.getAddressFirstRep().getDistrict()).isEqualTo("SUFFOLK");

        // Telecoms
        assertThat(patient.hasTelecom()).isTrue();
        assertThat(patient.getTelecom().stream().anyMatch(t -> t.getValue().contains("555-1111"))).isTrue();
        assertThat(patient.getTelecom().stream().anyMatch(t -> t.getValue().contains("555-2222"))).isTrue();

        // Race & Ethnicity Extensions
        assertThat(patient.getExtensionByUrl(PatientDemographicsUpdateErgon.EXTENSION_RACE)).isNotNull();
        assertThat(patient.getExtensionByUrl(PatientDemographicsUpdateErgon.EXTENSION_ETHNICITY)).isNotNull();
        assertThat(patient.getExtensionByUrl(PatientDemographicsUpdateErgon.EXTENSION_RELIGION)).isNotNull();

        // NK1 Contacts
        assertThat(patient.hasContact()).isTrue();
        assertThat(patient.getContact()).hasSize(2);
        Patient.ContactComponent contact1 = patient.getContact().get(0);
        assertThat(contact1.getName().getFamily()).isEqualTo("SMITH");
        assertThat(contact1.getName().getGivenAsSingleString()).isEqualTo("JOHN EDWARD");
        assertThat(contact1.getRelationshipFirstRep().getText()).isEqualTo("Spouse");
        assertThat(contact1.getAddress().getCity()).isEqualTo("BOSTON");

        Patient.ContactComponent contact2 = patient.getContact().get(1);
        assertThat(contact2.getName().getFamily()).isEqualTo("DOE");
        assertThat(contact2.getRelationshipFirstRep().getText()).isEqualTo("Mother");

        // PD1 Facility & Provider
        assertThat(patient.hasManagingOrganization()).isTrue();
        assertThat(patient.getManagingOrganization().getReference()).isEqualTo("Organization/FAC-01");
        assertThat(patient.getManagingOrganization().getDisplay()).isEqualTo("General Hospital");

        assertThat(patient.hasGeneralPractitioner()).isTrue();
        assertThat(patient.getGeneralPractitionerFirstRep().getReference()).isEqualTo("Practitioner/DOC-99");
        assertThat(patient.getGeneralPractitionerFirstRep().getDisplay()).contains("WELBY");
    }

    @Test
    @DisplayName("Should process exchange and populate demographics headers and JSON body")
    void testProcessPatientDemographicsRouteExecution() throws Exception {
        activity.setInputEndpoint("direct:demo-in");
        activity.setOutputEndpoint("mock:demo-out");

        camelContext.addRoutes(activity);
        camelContext.start();

        MockEndpoint mockOut = camelContext.getEndpoint("mock:demo-out", MockEndpoint.class);
        mockOut.expectedMessageCount(1);

        String hl7Message = "MSH|^~\\&|PAS|HOSP|HIE|REC|20260908120000||ADT^A08|MSG-DEMO-002|P|2.4\r" +
                "PID|||MRN-98765^^^HOSP||JOHNSON^ROBERT^D||19700820|M|||456 ELM ST^^DALLAS^TX^75201^USA||(214)555-9988||ENG^English|D^Divorced";

        producerTemplate.sendBody("direct:demo-in", hl7Message);

        mockOut.assertIsSatisfied(2000);

        Exchange exchange = mockOut.getExchanges().get(0);
        assertThat(exchange.getMessage().getHeader(PatientDemographicsUpdateErgon.HEADER_PATIENT_ID)).isEqualTo("MRN-98765");
        assertThat(exchange.getMessage().getHeader(PatientDemographicsUpdateErgon.HEADER_PATIENT_MRN)).isEqualTo("MRN-98765");
        assertThat(exchange.getMessage().getHeader(PatientDemographicsUpdateErgon.HEADER_PATIENT_NAME)).isEqualTo("ROBERT D JOHNSON");
        assertThat(exchange.getMessage().getHeader(PatientDemographicsUpdateErgon.HEADER_PATIENT_GENDER)).isEqualTo("male");
        assertThat(exchange.getMessage().getHeader(PatientDemographicsUpdateErgon.HEADER_PATIENT_DOB)).isEqualTo("1970-08-20");
        assertThat(exchange.getMessage().getHeader(PatientDemographicsUpdateErgon.HEADER_PATIENT_MARITAL_STATUS)).isEqualTo("Divorced");
        assertThat(exchange.getMessage().getHeader(PatientDemographicsUpdateErgon.HEADER_PATIENT_UPDATED)).isEqualTo(true);
        assertThat(exchange.getMessage().getHeader(PatientDemographicsUpdateErgon.HEADER_PATIENT_DEMOGRAPHICS_UPDATED)).isEqualTo(true);

        ErgonEvent event = exchange.getMessage().getBody(ErgonEvent.class);
        assertThat(event).isNotNull();
        assertThat(event.getAction()).isEqualTo("PROCESS");
        assertThat(event.getStatus()).isEqualTo("requested");
        assertThat(event.getDescription()).contains("ROBERT D JOHNSON");

        org.hl7.fhir.r5.model.Task cachedTask = activity.getTaskCacheService().getTask(event.getTaskId()).orElse(null);
        assertThat(cachedTask).isNotNull();
        assertThat(cachedTask.getFor().getDisplay()).isEqualTo("ROBERT D JOHNSON");
        assertThat(FhirSecurityTagManager.hasConfidentiality(cachedTask, FhirConfidentialityEnum.N)).isTrue();
    }

    @Test
    @DisplayName("Should execute after PatientIdentityUpdate and enrich existing Patient JSON")
    void testSequentialExecutionAfterPatientIdentityUpdate() throws Exception {
        PatientIdentityUpdateErgon identityActivity = new PatientIdentityUpdateErgon();
        identityActivity.setInputEndpoint("direct:pipeline-start");
        identityActivity.setOutputEndpoint("direct:pipeline-intermediate");

        PatientDemographicsUpdateErgon demographicsActivity = new PatientDemographicsUpdateErgon();
        demographicsActivity.setInputEndpoint("direct:pipeline-intermediate");
        demographicsActivity.setOutputEndpoint("mock:pipeline-final");

        camelContext.addRoutes(identityActivity);
        camelContext.addRoutes(demographicsActivity);
        camelContext.start();

        MockEndpoint mockFinal = camelContext.getEndpoint("mock:pipeline-final", MockEndpoint.class);
        mockFinal.expectedMessageCount(1);

        String hl7Message = "MSH|^~\\&|PAS|HOSP|HIE|REC|20260908120000||ADT^A01|MSG-PIPE-001|P|2.4\r" +
                "PID|||MRN-CHAIN-100^^^HOSP||TAYLOR^ALICE^M^MS||19920315|F||2106-3^White|789 MAPLE RD^^SEATTLE^WA^98101^USA||(206)555-7766||SPA^Spanish|S^Single\r" +
                "NK1|1|TAYLOR^BOB||PAR^Father|789 MAPLE RD^^SEATTLE^WA^98101^USA|(206)555-8899\r" +
                "PD1|||CLINIC-10^Westside Clinic|DR-44^OSLER^WILLIAM";

        producerTemplate.sendBody("direct:pipeline-start", hl7Message);

        mockFinal.assertIsSatisfied(2000);

        Exchange exchange = mockFinal.getExchanges().get(0);
        assertThat(exchange.getMessage().getHeader(PatientIdentityUpdateErgon.HEADER_PATIENT_ID)).isEqualTo("MRN-CHAIN-100");
        assertThat(exchange.getMessage().getHeader(PatientIdentityUpdateErgon.HEADER_PATIENT_MRN)).isEqualTo("MRN-CHAIN-100");
        assertThat(exchange.getMessage().getHeader(PatientIdentityUpdateErgon.HEADER_PATIENT_NAME)).isEqualTo("ALICE M TAYLOR MS");
        assertThat(exchange.getMessage().getHeader(PatientDemographicsUpdateErgon.HEADER_PATIENT_GENDER)).isEqualTo("female");
        assertThat(exchange.getMessage().getHeader(PatientDemographicsUpdateErgon.HEADER_PATIENT_DOB)).isEqualTo("1992-03-15");
        assertThat(exchange.getMessage().getHeader(PatientDemographicsUpdateErgon.HEADER_PATIENT_MARITAL_STATUS)).isEqualTo("Never Married");
        assertThat(exchange.getMessage().getHeader(PatientIdentityUpdateErgon.HEADER_PATIENT_UPDATED)).isEqualTo(true);
        assertThat(exchange.getMessage().getHeader(PatientDemographicsUpdateErgon.HEADER_PATIENT_DEMOGRAPHICS_UPDATED)).isEqualTo(true);

        ErgonEvent event = exchange.getMessage().getBody(ErgonEvent.class);
        assertThat(event).isNotNull();
        assertThat(event.getAction()).isEqualTo("PROCESS");
        assertThat(event.getStatus()).isEqualTo("requested");

        org.hl7.fhir.r5.model.Task cachedTask = demographicsActivity.getTaskCacheService().getTask(event.getTaskId()).orElse(null);
        assertThat(cachedTask).isNotNull();
        assertThat(cachedTask.getFor().getDisplay()).isEqualTo("ALICE M TAYLOR MS");
        assertThat(FhirSecurityTagManager.hasConfidentiality(cachedTask, FhirConfidentialityEnum.N)).isTrue();

        // Verify patient contained in task has all enriched demographics
        Patient parsed = null;
        for (Resource r : cachedTask.getContained()) {
            if (r instanceof Patient) {
                parsed = (Patient) r;
                break;
            }
        }
        assertThat(parsed).isNotNull();
        assertThat(FhirSecurityTagManager.hasConfidentiality(parsed, FhirConfidentialityEnum.N)).isTrue();
        String parsedId = parsed.getIdPart() != null ? parsed.getIdPart().replace("#", "") : "";
        assertThat(parsedId).isEqualTo("MRN-CHAIN-100");
        assertThat(parsed.getNameFirstRep().getFamily()).isEqualTo("TAYLOR");
        assertThat(parsed.getContact()).hasSize(1);
        assertThat(parsed.getContactFirstRep().getName().getFamily()).isEqualTo("TAYLOR");
        assertThat(parsed.getManagingOrganization().getDisplay()).isEqualTo("Westside Clinic");
        assertThat(parsed.getGeneralPractitionerFirstRep().getDisplay()).contains("OSLER");
    }

    @Test
    @DisplayName("Should handle deceased patient indicator and death date in PID-29/PID-30")
    void testDeceasedPatientDemographics() {
        String hl7Message = "MSH|^~\\&|PAS|HOSP|HIE|REC|20260908120000||ADT^A03|MSG-DEC-001|P|2.4\r" +
                "PID|||MRN-DEC-999^^^HOSP||WILLIS^BRUCE||19550319|M|||100 SUNSET BLVD^^LOS ANGELES^CA^90001||||||||||||||19550319||||20260908103000|Y";

        Patient patient = new Patient();
        activity.parseAndApplyHl7AdtDemographics(patient, hl7Message);

        assertThat(patient.hasDeceased()).isTrue();
        assertThat(patient.hasDeceasedDateTimeType()).isTrue();
    }
}
