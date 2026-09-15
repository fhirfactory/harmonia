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

package net.fhirfactory.harmonia.praxis.sequence;

import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.util.TypeLiteral;
import net.fhirfactory.harmonia.erga.base.ErgonBase;
import net.fhirfactory.harmonia.erga.patient.demographics.PatientDemographicsUpdateErgon;
import net.fhirfactory.harmonia.erga.patient.identity.PatientIdentityUpdateErgon;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskSequenceTest {

    private CamelContext camelContext;
    private ProducerTemplate producerTemplate;

    @BeforeEach
    void setUp() {
        camelContext = new DefaultCamelContext();
        producerTemplate = camelContext.createProducerTemplate();
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

    static class SampleActivityA extends ErgonBase {
        public SampleActivityA() {
            super("activity-a", "Activity A");
            setInputEndpoint("direct:act-a-in");
            setOutputEndpoint("mock:act-a-out");
        }

        @Override
        protected void configureActivity() {
            from(getInputEndpoint())
                    .routeId(generateRouteId())
                    .transform().simple("${body} -> [A]")
                    .to(getOutputEndpoint());
        }
    }

    static class SampleActivityB extends ErgonBase {
        public SampleActivityB() {
            super("activity-b", "Activity B");
            setInputEndpoint("direct:act-b-in");
            setOutputEndpoint("mock:act-b-out");
        }

        @Override
        protected void configureActivity() {
            from(getInputEndpoint())
                    .routeId(generateRouteId())
                    .transform().simple("${body} -> [B]")
                    .to(getOutputEndpoint());
        }
    }

    static class MockInstance<T> implements Instance<T> {
        private final List<T> elements;

        public MockInstance(List<T> elements) {
            this.elements = elements;
        }

        @Override
        public Instance<T> select(Annotation... qualifiers) {
            return this;
        }

        @Override
        public <U extends T> Instance<U> select(Class<U> subtype, Annotation... qualifiers) {
            return null;
        }

        @Override
        public <U extends T> Instance<U> select(TypeLiteral<U> subtype, Annotation... qualifiers) {
            return null;
        }

        @Override
        public boolean isUnsatisfied() {
            return elements.isEmpty();
        }

        @Override
        public boolean isAmbiguous() {
            return elements.size() > 1;
        }

        @Override
        public void destroy(T instance) {
        }

        @Override
        public Iterator<T> iterator() {
            return elements.iterator();
        }

        @Override
        public T get() {
            return elements.isEmpty() ? null : elements.get(0);
        }

        @Override
        public Handle<T> getHandle() {
            return null;
        }

        @Override
        public Iterable<? extends Handle<T>> handles() {
            return null;
        }
    }

    @Test
    void testConstructorsAndProperties() {
        Praxis sequence = new Praxis("seq-1", "Admissions Processing Sequence");
        assertThat(sequence.getPraxisId()).isEqualTo("seq-1");
        assertThat(sequence.getPraxisName()).isEqualTo("Admissions Processing Sequence");
        assertThat(sequence.getVersion()).isEqualTo("1.0.0");
        assertThat(sequence.isEnabled()).isTrue();
        assertThat(sequence.isEmpty()).isTrue();
        assertThat(sequence.size()).isEqualTo(0);
        assertThat(sequence.getActivities()).isNotNull().isEmpty();
        assertThat(sequence.getActivityList()).isEmpty();

        sequence.setSequenceDescription("Processes patient admission events");
        sequence.setInputEndpoint("direct:admission-in");
        sequence.setOutputEndpoint("direct:admission-out");
        sequence.setErrorEndpoint("direct:admission-err");
        sequence.setVersion("2.0.0");

        assertThat(sequence.getSequenceDescription()).isEqualTo("Processes patient admission events");
        assertThat(sequence.getInputEndpoint()).isEqualTo("direct:admission-in");
        assertThat(sequence.getOutputEndpoint()).isEqualTo("direct:admission-out");
        assertThat(sequence.getErrorEndpoint()).isEqualTo("direct:admission-err");
        assertThat(sequence.getVersion()).isEqualTo("2.0.0");
        assertThat(sequence.generateSequenceRouteId()).isEqualTo("seq-1");
        assertThat(sequence.toString()).contains("seq-1").contains("Admissions Processing Sequence");
    }

    @Test
    void testArrayManagementOperations() {
        Praxis sequence = new Praxis("seq-array-test", "Array Management");

        SampleActivityA activityA = new SampleActivityA();
        SampleActivityB activityB = new SampleActivityB();

        // Add activity
        sequence.addActivity(activityA);
        assertThat(sequence.size()).isEqualTo(1);
        assertThat(sequence.getActivity(0)).isSameAs(activityA);
        assertThat(sequence.getActivityById("activity-a")).isSameAs(activityA);
        assertThat(sequence.containsActivity("activity-a")).isTrue();
        assertThat(sequence.containsActivity("non-existent")).isFalse();

        // Add multiple activities
        SampleActivityA activityA2 = new SampleActivityA();
        activityA2.setActivityId("activity-a2");
        sequence.addActivities(activityB, activityA2);
        assertThat(sequence.getActivityCount()).isEqualTo(3);

        // Insert activity at order
        SampleActivityB activityB2 = new SampleActivityB();
        activityB2.setActivityId("activity-b2");
        sequence.addActivity(3, activityB2);
        assertThat(sequence.getActivityCount()).isEqualTo(4);
        assertThat(sequence.getActivity(3)).isSameAs(activityB2);

        // Remove by ID
        boolean removed = sequence.removeActivityById("activity-b2");
        assertThat(removed).isTrue();
        assertThat(sequence.getActivityCount()).isEqualTo(3);
        assertThat(sequence.containsActivity("activity-b2")).isFalse();

        // Remove by instance
        boolean removedInstance = sequence.removeActivity(activityA2);
        assertThat(removedInstance).isTrue();
        assertThat(sequence.getActivityCount()).isEqualTo(2);

        // Get map copy
        java.util.Map<Integer, ErgonBase> actMap = sequence.getActivities();
        assertThat(actMap).hasSize(2);
        assertThat(actMap.get(0)).isSameAs(activityA);
        assertThat(actMap.get(1)).isSameAs(activityB);

        // Set activities via List
        sequence.setActivities(List.of(activityB));
        assertThat(sequence.getActivityCount()).isEqualTo(1);
        assertThat(sequence.getActivity(0)).isSameAs(activityB);

        // Clear
        sequence.clearActivities();
        assertThat(sequence.isEmpty()).isTrue();
        assertThat(sequence.getActivityCount()).isEqualTo(0);
    }

    @Test
    void testArrayBoundsAndExceptions() {
        Praxis sequence = new Praxis();
        assertThat(sequence.getActivity(0)).isNull();
        assertThat(sequence.removeActivity(0)).isNull();
        assertThatThrownBy(() -> sequence.addActivity(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testOrderEntryManagement() {
        Praxis sequence = new Praxis("seq-order-test", "Order Entry Test");
        SampleActivityA activityA = new SampleActivityA();
        SampleActivityB activityB = new SampleActivityB();

        sequence.addActivity(10, activityA);
        sequence.addActivity(5, activityB);

        assertThat(sequence.getActivity(10)).isSameAs(activityA);
        assertThat(sequence.getActivity(5)).isSameAs(activityB);
        assertThat(sequence.getActivityIds()).containsEntry(5, "activity-b").containsEntry(10, "activity-a");

        // getActivityList() should return ordered by key (5 then 10)
        List<ErgonBase> ordered = sequence.getActivityList();
        assertThat(ordered).containsExactly(activityB, activityA);
    }

    @Test
    void testCdiInjectionDiscovery() {
        Praxis sequence = new Praxis("seq-cdi", "CDI Sequence");
        SampleActivityA activityA = new SampleActivityA();
        SampleActivityB activityB = new SampleActivityB();

        Instance<ErgonBase> mockInstance = new MockInstance<>(List.of(activityA, activityB));
        sequence.setInjectedActivities(mockInstance);
        sequence.init();

        assertThat(sequence.getActivityCount()).isEqualTo(2);
        assertThat(sequence.getActivity(0)).isSameAs(activityA);
        assertThat(sequence.getActivity(1)).isSameAs(activityB);
    }

    @Test
    void testEnabledActivitiesFilter() {
        Praxis sequence = new Praxis();
        SampleActivityA activityA = new SampleActivityA();
        SampleActivityB activityB = new SampleActivityB();
        activityB.setEnabled(false);

        sequence.addActivities(activityA, activityB);
        List<ErgonBase> enabled = sequence.getEnabledActivities();
        assertThat(enabled).hasSize(1);
        assertThat(enabled.get(0)).isSameAs(activityA);
    }

    @Test
    void testCamelRouteRegistrationAndExecution() throws Exception {
        Praxis sequence = new Praxis("seq-exec", "Execution Sequence");
        SampleActivityA activityA = new SampleActivityA();
        SampleActivityB activityB = new SampleActivityB();
        sequence.addActivities(activityA, activityB);

        sequence.registerRoutes(camelContext);
        camelContext.start();

        assertThat(camelContext.getRoute("activity-activity-a")).isNotNull();
        assertThat(camelContext.getRoute("activity-activity-b")).isNotNull();

        String resultA = producerTemplate.requestBody("direct:act-a-in", "Input", String.class);
        assertThat(resultA).isEqualTo("Input -> [A]");

        String resultB = producerTemplate.requestBody("direct:act-b-in", "Input", String.class);
        assertThat(resultB).isEqualTo("Input -> [B]");
    }

    @Test
    void testConfigureChainedEndpoints() {
        Praxis sequence = new Praxis("seq-chain", "Chained Sequence");
        sequence.setInputEndpoint("direct:sequence-start");
        sequence.setOutputEndpoint("direct:sequence-end");
        sequence.setErrorEndpoint("direct:sequence-error");

        ErgonBase act1 = new ErgonBase("act1", "Act 1") {
            @Override
            protected void configureActivity() {}
        };
        ErgonBase act2 = new ErgonBase("act2", "Act 2") {
            @Override
            protected void configureActivity() {}
        };

        sequence.addActivities(act1, act2);
        sequence.configureChainedEndpoints();

        assertThat(act1.getInputEndpoint()).isEqualTo("direct:seq-seq-chain-step-1");
        assertThat(act1.getOutputEndpoint()).isEqualTo("direct:seq-seq-chain-step-2");
        assertThat(act2.getInputEndpoint()).isEqualTo("direct:seq-seq-chain-step-2");
        assertThat(act2.getOutputEndpoint()).isEqualTo("direct:sequence-end");
        assertThat(act1.getErrorEndpoint()).isEqualTo("direct:sequence-error");
        assertThat(act2.getErrorEndpoint()).isEqualTo("direct:sequence-error");
    }

    @Test
    void testValidationAndEquals() {
        Praxis sequence1 = new Praxis("seq-id", "Name 1");
        Praxis sequence2 = new Praxis("seq-id", "Name 2");
        Praxis sequence3 = new Praxis("other-id", "Name 1");

        assertThat(sequence1).isEqualTo(sequence2);
        assertThat(sequence1.hashCode()).isEqualTo(sequence2.hashCode());
        assertThat(sequence1).isNotEqualTo(sequence3);

        assertThat(sequence1.validate()).isTrue();

        Praxis invalid = new Praxis("", "Blank ID");
        assertThat(invalid.validate()).isFalse();
    }

    @Test
    void testMatchingCriteriaExactAndWildcards() {
        Praxis sequence = new Praxis("seq-filter", "Filter Test Sequence");
        sequence.setTargetGatewayInstances(List.of("pas-gw", "emr-gw"));
        sequence.setTargetTriggerTypes(List.of("A01", "A08", "ORU^R01"));

        // Exact match
        assertThat(sequence.matches("pas-gw", "A01")).isTrue();
        assertThat(sequence.matches("pas-gw", "ADT^A01")).isTrue();
        assertThat(sequence.matches("emr-gw", "A08")).isTrue();
        assertThat(sequence.matches("emr-gw", "R01")).isTrue();
        assertThat(sequence.matches("emr-gw", "ORU^R01")).isTrue();

        // Non-matching gateway or trigger
        assertThat(sequence.matches("lab-gw", "A01")).isFalse();
        assertThat(sequence.matches("pas-gw", "A03")).isFalse();
        assertThat(sequence.matches("other-gw", "ORM^O01")).isFalse();

        // Wildcard gateway
        Praxis wildcardGwSeq = new Praxis("seq-wc-gw", "Wildcard GW");
        wildcardGwSeq.setTargetGatewayInstances(List.of("*"));
        wildcardGwSeq.setTargetTriggerTypes(List.of("A03"));
        assertThat(wildcardGwSeq.matches("any-gw-1", "A03")).isTrue();
        assertThat(wildcardGwSeq.matches("any-gw-2", "A01")).isFalse();

        // Wildcard trigger
        Praxis wildcardTrigSeq = new Praxis("seq-wc-trig", "Wildcard Trigger");
        wildcardTrigSeq.setTargetGatewayInstances(List.of("lims-gw"));
        wildcardTrigSeq.setMatchAllTriggers(true);
        assertThat(wildcardTrigSeq.matches("lims-gw", "R01")).isTrue();
        assertThat(wildcardTrigSeq.matches("lims-gw", "O01")).isTrue();
        assertThat(wildcardTrigSeq.matches("pas-gw", "R01")).isFalse();

        // Match all
        Praxis matchAllSeq = new Praxis("seq-all", "Match All");
        matchAllSeq.setMatchAllGateways(true);
        matchAllSeq.setMatchAllTriggers(true);
        assertThat(matchAllSeq.matches("any-gw", "ANY^TRIGGER")).isTrue();
    }

    @Test
    void testActivityIdSynchronization() {
        Praxis sequence = new Praxis("seq-sync", "Sync Test");
        SampleActivityA activityA = new SampleActivityA();
        SampleActivityB activityB = new SampleActivityB();

        sequence.addActivities(activityA, activityB);

        assertThat(sequence.getActivityIds()).containsEntry(0, "activity-a").containsEntry(1, "activity-b");
        assertThat(sequence.getActivityClassNames()).containsEntry(0, SampleActivityA.class.getName())
                .containsEntry(1, SampleActivityB.class.getName());
    }

    @Test
    void testPatientIdentityUpdateInSequence() throws Exception {
        Praxis sequence = new Praxis("seq-patient-identity", "Patient Identity Pipeline");
        PatientIdentityUpdateErgon patientActivity = new PatientIdentityUpdateErgon();
        sequence.addActivity(patientActivity);
        sequence.setInputEndpoint("direct:patient-seq-test-in");
        sequence.setOutputEndpoint("mock:patient-seq-test-out");
        sequence.configureChainedEndpoints();

        sequence.registerRoutes(camelContext);
        camelContext.addRoutes(sequence.createSequencePipelineRoute());
        camelContext.start();

        assertThat(camelContext.getRoute("activity-patient-identity-update")).isNotNull();

        String hl7 = "MSH|^~\\&|PAS|HOSP|HIE|REC|20260908120000||ADT^A01|MSG-TEST|P|2.4\r" +
                "PID|||MRN-TEST-123^^^HOSP||JOHNSON^SARAH^E^^DR||19880312|F|||100 PARK AVE^^NEW YORK^NY^10001^USA";

        producerTemplate.sendBody("direct:patient-seq-test-in", hl7);

        org.apache.camel.component.mock.MockEndpoint mockEndpoint = camelContext.getEndpoint("mock:patient-seq-test-out", org.apache.camel.component.mock.MockEndpoint.class);
        mockEndpoint.expectedMessageCount(1);
        mockEndpoint.assertIsSatisfied(2000);

        org.apache.camel.Exchange exchange = mockEndpoint.getExchanges().get(0);
        assertThat(exchange.getMessage().getHeader(PatientIdentityUpdateErgon.HEADER_PATIENT_ID)).isEqualTo("MRN-TEST-123");
        assertThat(exchange.getMessage().getHeader(PatientIdentityUpdateErgon.HEADER_PATIENT_NAME)).isEqualTo("DR SARAH E JOHNSON");
    }

    @Test
    void testPatientIdentityAndDemographicsSequentialPipeline() throws Exception {
        Praxis sequence = new Praxis("seq-patient-pipeline", "Patient Identity & Demographics Pipeline");
        PatientIdentityUpdateErgon identityActivity = new PatientIdentityUpdateErgon();
        PatientDemographicsUpdateErgon demographicsActivity = new PatientDemographicsUpdateErgon();

        sequence.addActivities(identityActivity, demographicsActivity);
        sequence.setInputEndpoint("direct:patient-id-demo-test-in");
        sequence.setOutputEndpoint("mock:patient-id-demo-test-out");
        sequence.configureChainedEndpoints();

        sequence.registerRoutes(camelContext);
        camelContext.addRoutes(sequence.createSequencePipelineRoute());
        camelContext.start();

        assertThat(camelContext.getRoute("activity-patient-identity-update")).isNotNull();
        assertThat(camelContext.getRoute("activity-patient-demographics-update")).isNotNull();

        String hl7 = "MSH|^~\\&|PAS|HOSP|HIE|REC|20260908120000||ADT^A01|MSG-TEST-02|P|2.4\r" +
                "PID|||MRN-DEMO-456^^^HOSP||MILLER^DAVID^R^^MR||19820514|M|||200 PINE ST^^DENVER^CO^80202^USA||(303)555-4321||ENG^English|M^Married";

        producerTemplate.sendBody("direct:patient-id-demo-test-in", hl7);

        org.apache.camel.component.mock.MockEndpoint mockEndpoint = camelContext.getEndpoint("mock:patient-id-demo-test-out", org.apache.camel.component.mock.MockEndpoint.class);
        mockEndpoint.expectedMessageCount(1);
        mockEndpoint.assertIsSatisfied(2000);

        org.apache.camel.Exchange exchange = mockEndpoint.getExchanges().get(0);
        assertThat(exchange.getMessage().getHeader(PatientIdentityUpdateErgon.HEADER_PATIENT_ID)).isEqualTo("MRN-DEMO-456");
        assertThat(exchange.getMessage().getHeader(PatientIdentityUpdateErgon.HEADER_PATIENT_NAME)).isEqualTo("MR DAVID R MILLER");
        assertThat(exchange.getMessage().getHeader(PatientDemographicsUpdateErgon.HEADER_PATIENT_GENDER)).isEqualTo("male");
        assertThat(exchange.getMessage().getHeader(PatientDemographicsUpdateErgon.HEADER_PATIENT_DOB)).isEqualTo("1982-05-14");
        assertThat(exchange.getMessage().getHeader(PatientDemographicsUpdateErgon.HEADER_PATIENT_MARITAL_STATUS)).isEqualTo("Married");
        assertThat(exchange.getMessage().getHeader(PatientIdentityUpdateErgon.HEADER_PATIENT_UPDATED)).isEqualTo(true);
        assertThat(exchange.getMessage().getHeader(PatientDemographicsUpdateErgon.HEADER_PATIENT_DEMOGRAPHICS_UPDATED)).isEqualTo(true);
    }

    @Test
    void testMessageQueueConduitPipelineExecution() throws Exception {
        Praxis sequence = new Praxis("seq-conduit-chain", "Conduit Chain Pipeline");
        net.fhirfactory.harmonia.erga.infrastructure.MessageQueueToExchangeConduit conduit =
                new net.fhirfactory.harmonia.erga.infrastructure.MessageQueueToExchangeConduit();
        PatientIdentityUpdateErgon identityActivity = new PatientIdentityUpdateErgon();

        sequence.addActivities(conduit, identityActivity);
        sequence.setInputEndpoint("direct:conduit-pipeline-test-in");
        sequence.setOutputEndpoint("mock:conduit-pipeline-test-out");
        sequence.configureChainedEndpoints();

        sequence.registerRoutes(camelContext);
        camelContext.addRoutes(sequence.createSequencePipelineRoute());
        camelContext.start();

        assertThat(camelContext.getRoute("activity-message-queue-to-exchange")).isNotNull();
        assertThat(camelContext.getRoute("activity-patient-identity-update")).isNotNull();

        String hl7 = "MSH|^~\\&|PAS|HOSP|HIE|REC|20260908120000||ADT^A01|MSG-TEST-03|P|2.4\r" +
                "PID|||MRN-CONDUIT-777^^^HOSP||WILSON^ALICE^M^^MRS||19950420|F|||123 MAIN ST^^BOSTON^MA^02108^USA";

        producerTemplate.sendBody("direct:conduit-pipeline-test-in", hl7);

        org.apache.camel.component.mock.MockEndpoint mockEndpoint = camelContext.getEndpoint("mock:conduit-pipeline-test-out", org.apache.camel.component.mock.MockEndpoint.class);
        mockEndpoint.expectedMessageCount(1);
        mockEndpoint.assertIsSatisfied(2000);

        org.apache.camel.Exchange exchange = mockEndpoint.getExchanges().get(0);
        assertThat(exchange.getMessage().getHeader(PatientIdentityUpdateErgon.HEADER_PATIENT_ID)).isEqualTo("MRN-CONDUIT-777");
        assertThat(exchange.getMessage().getHeader(PatientIdentityUpdateErgon.HEADER_PATIENT_NAME)).isEqualTo("MRS ALICE M WILSON");
        assertThat(exchange.getMessage().getHeader(PatientIdentityUpdateErgon.HEADER_PATIENT_UPDATED)).isEqualTo(true);
    }

    @Test
    void testSequentialPipelineWithoutOutputEndpointCompletesSuccessfully() throws Exception {
        Praxis sequence = new Praxis("seq-patient-pipeline-no-out", "Default Pipeline Without Output");
        PatientIdentityUpdateErgon identityActivity = new PatientIdentityUpdateErgon();
        PatientDemographicsUpdateErgon demographicsActivity = new PatientDemographicsUpdateErgon();

        sequence.addActivities(identityActivity, demographicsActivity);
        sequence.setInputEndpoint("direct:patient-no-out-test-in");
        sequence.configureChainedEndpoints();

        sequence.registerRoutes(camelContext);
        camelContext.addRoutes(sequence.createSequencePipelineRoute());
        camelContext.start();

        String hl7 = "MSH|^~\\&|PAS|HOSP|HIE|REC|20260908120000||ADT^A01|MSG-TEST-04|P|2.4\r" +
                "PID|||MRN-DEMO-789^^^HOSP||DOE^JANE^A^^MS||19900101|F|||500 BROADWAY^^NEW YORK^NY^10012^USA||(212)555-1234||ENG^English|S^Single";

        org.apache.camel.Exchange exchange = producerTemplate.send("direct:patient-no-out-test-in", ex -> {
            ex.getMessage().setBody(hl7);
            ex.getMessage().setHeader("HIE_RAW_MESSAGE", hl7);
        });

        assertThat(exchange.getException()).isNull();
        assertThat(exchange.getMessage().getHeader(PatientIdentityUpdateErgon.HEADER_PATIENT_ID)).isEqualTo("MRN-DEMO-789");
        assertThat(exchange.getMessage().getHeader(PatientDemographicsUpdateErgon.HEADER_PATIENT_DEMOGRAPHICS_UPDATED)).isEqualTo(true);
    }
}
