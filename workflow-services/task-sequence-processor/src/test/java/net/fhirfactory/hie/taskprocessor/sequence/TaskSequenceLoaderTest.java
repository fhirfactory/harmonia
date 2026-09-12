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

package net.fhirfactory.hie.taskprocessor.sequence;

import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.util.TypeLiteral;
import net.fhirfactory.hie.model.TaskEvent;
import net.fhirfactory.hie.model.topic.Topic;
import net.fhirfactory.hie.taskprocessor.service.TaskSequenceService;
import net.fhirfactory.hie.taskprocessors.base.TaskProcessingActivity;
import net.fhirfactory.hie.taskprocessors.patient.demographics.PatientDemographicsUpdate;
import net.fhirfactory.hie.taskprocessors.patient.identity.PatientIdentityUpdate;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

class TaskSequenceLoaderTest {

    private CamelContext camelContext;
    private ProducerTemplate producerTemplate;
    private TaskSequenceService sequenceService;
    private TaskSequenceLoader loader;

    @BeforeEach
    void setUp() {
        camelContext = new DefaultCamelContext();
        producerTemplate = camelContext.createProducerTemplate();
        sequenceService = new TaskSequenceService();
        sequenceService.init();
        sequenceService.clear();
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

    public static class Step1Activity extends TaskProcessingActivity {
        public static final AtomicInteger executionCount = new AtomicInteger(0);

        public Step1Activity() {
            super("act-step-1", "Step 1 Validation");
            setInputEndpoint("direct:act-step-1-in");
            setOutputEndpoint("mock:act-step-1-out");
        }

        @Override
        protected void configureActivity() {
            from(getInputEndpoint())
                    .routeId(generateRouteId())
                    .process(exchange -> executionCount.incrementAndGet())
                    .to(getOutputEndpoint());
        }
    }

    public static class Step2Activity extends TaskProcessingActivity {
        public static final AtomicInteger executionCount = new AtomicInteger(0);

        public Step2Activity() {
            super("act-step-2", "Step 2 Enrichment");
            setInputEndpoint("direct:act-step-2-in");
            setOutputEndpoint("mock:act-step-2-out");
        }

        @Override
        protected void configureActivity() {
            from(getInputEndpoint())
                    .routeId(generateRouteId())
                    .process(exchange -> executionCount.incrementAndGet())
                    .to(getOutputEndpoint());
        }
    }

    static class MockActivityInstance<T> implements Instance<T> {
        private final List<T> elements;

        public MockActivityInstance(List<T> elements) {
            this.elements = elements;
        }

        @Override
        public Instance<T> select(Annotation... qualifiers) { return this; }

        @Override
        public <U extends T> Instance<U> select(Class<U> subtype, Annotation... qualifiers) { return null; }

        @Override
        public <U extends T> Instance<U> select(TypeLiteral<U> subtype, Annotation... qualifiers) { return null; }

        @Override
        public boolean isUnsatisfied() { return elements == null || elements.isEmpty(); }

        @Override
        public boolean isAmbiguous() { return false; }

        @Override
        public void destroy(T instance) {}

        @Override
        public Handle<T> getHandle() { return null; }

        @Override
        public Iterable<? extends Handle<T>> handles() { return List.of(); }

        @Override
        public T get() { return elements != null && !elements.isEmpty() ? elements.get(0) : null; }

        @Override
        public Iterator<T> iterator() { return elements != null ? elements.iterator() : new ArrayList<T>().iterator(); }
    }

    @Test
    @DisplayName("Should seed default sequences if cache is empty and register routes in CamelContext")
    void testSeedAndRegisterOnEmptyCache() throws Exception {
        Step1Activity act1 = new Step1Activity();
        Step2Activity act2 = new Step2Activity();
        PatientIdentityUpdate patientIdAct = new PatientIdentityUpdate();
        Instance<TaskProcessingActivity> mockActivities = new MockActivityInstance<>(List.of(act1, act2, patientIdAct));

        loader = new TaskSequenceLoader(sequenceService, mockActivities);
        List<TaskSequence> loaded = loader.loadAndRegisterSequences(camelContext);
        camelContext.start();

        assertThat(loaded).isNotEmpty();
        assertThat(loaded).hasSize(1);
        assertThat(sequenceService.count()).isEqualTo(1);
        assertThat(sequenceService.getById("seq-patient-identity-pipeline")).isPresent();
        assertThat(loader.getLoadedSequences()).hasSize(loaded.size());

        // Check Camel routes registered
        assertThat(camelContext.getRoutes()).isNotEmpty();
    }

    @Test
    @DisplayName("Should execute PatientIdentityUpdate inside a configured TaskSequence pipeline")
    void testPatientIdentityUpdateSequencePipelineExecution() throws Exception {
        PatientIdentityUpdate patientIdAct = new PatientIdentityUpdate();
        Instance<TaskProcessingActivity> mockActivities = new MockActivityInstance<>(List.of(patientIdAct));

        TaskSequence patientSeq = new TaskSequence("seq-patient-identity-pipeline", "Patient Identity Update Sequence");
        patientSeq.setInputEndpoint("direct:patient-seq-in");
        patientSeq.setOutputEndpoint("mock:patient-seq-out");
        patientSeq.setTargetGatewayInstances(List.of("*"));
        patientSeq.setTargetTriggerTypes(List.of("*"));
        patientSeq.setActivityIds(List.of(PatientIdentityUpdate.DEFAULT_ACTIVITY_ID));

        sequenceService.save(patientSeq);

        loader = new TaskSequenceLoader(sequenceService, mockActivities);
        loader.loadAndRegisterSequences(camelContext);
        camelContext.start();

        MockEndpoint mockOut = camelContext.getEndpoint("mock:patient-seq-out", MockEndpoint.class);
        mockOut.expectedMessageCount(1);

        String hl7 = "MSH|^~\\&|PAS|HOSP|HIE|REC|20260908120000||ADT^A01|MSG-SEQ-01|P|2.4\r" +
                "PID|||MRN-SEQ-777^^^HOSP||WILLIAMS^ROBERT^L^JR^MR||19751125|M|||789 OAK ST^^CHICAGO^IL^60601^USA";

        producerTemplate.sendBodyAndHeaders("direct:patient-seq-in", hl7,
                java.util.Map.of("HIE_GATEWAY_INSTANCE", "pas-gw", "HIE_TRIGGER_TYPE", "A01"));

        mockOut.assertIsSatisfied(2000);

        org.apache.camel.Exchange exchange = mockOut.getExchanges().get(0);
        assertThat(exchange.getMessage().getHeader(PatientIdentityUpdate.HEADER_PATIENT_ID)).isEqualTo("MRN-SEQ-777");
        assertThat(exchange.getMessage().getHeader(PatientIdentityUpdate.HEADER_PATIENT_MRN)).isEqualTo("MRN-SEQ-777");
        assertThat(exchange.getMessage().getHeader(PatientIdentityUpdate.HEADER_PATIENT_NAME)).isEqualTo("MR ROBERT L WILLIAMS JR");
        assertThat(exchange.getMessage().getHeader(PatientIdentityUpdate.HEADER_PATIENT_UPDATED)).isEqualTo(true);

        TaskEvent taskEvent = exchange.getMessage().getBody(TaskEvent.class);
        assertThat(taskEvent).isNotNull();
        assertThat(taskEvent.getAction()).isEqualTo("PROCESS");
        assertThat(taskEvent.getDescription()).contains("WILLIAMS");

        org.hl7.fhir.r5.model.Task cachedTask = patientIdAct.getTaskCacheService().getTask(taskEvent.getTaskId()).orElse(null);
        assertThat(cachedTask).isNotNull();
        assertThat(cachedTask.getFor().getDisplay()).isEqualTo("MR ROBERT L WILLIAMS JR");
    }

    @Test
    @DisplayName("Should execute PatientIdentityUpdate followed by PatientDemographicsUpdate in sequence pipeline")
    void testPatientIdentityAndDemographicsSequencePipelineExecution() throws Exception {
        PatientIdentityUpdate patientIdAct = new PatientIdentityUpdate();
        PatientDemographicsUpdate demographicsAct = new PatientDemographicsUpdate();
        Instance<TaskProcessingActivity> mockActivities = new MockActivityInstance<>(List.of(patientIdAct, demographicsAct));

        TaskSequence patientSeq = new TaskSequence("seq-patient-identity-pipeline", "Patient Identity Update Sequence");
        patientSeq.setInputEndpoint("direct:patient-id-demo-seq-in");
        patientSeq.setOutputEndpoint("mock:patient-id-demo-seq-out");
        patientSeq.setTargetGatewayInstances(List.of("*"));
        patientSeq.setTargetTriggerTypes(List.of("*"));
        patientSeq.setActivityIds(List.of(PatientIdentityUpdate.DEFAULT_ACTIVITY_ID, PatientDemographicsUpdate.DEFAULT_ACTIVITY_ID));

        sequenceService.save(patientSeq);

        loader = new TaskSequenceLoader(sequenceService, mockActivities);
        loader.loadAndRegisterSequences(camelContext);
        camelContext.start();

        MockEndpoint mockOut = camelContext.getEndpoint("mock:patient-id-demo-seq-out", MockEndpoint.class);
        mockOut.expectedMessageCount(1);

        String hl7 = "MSH|^~\\&|PAS|HOSP|HIE|REC|20260908120000||ADT^A01|MSG-SEQ-02|P|2.4\r" +
                "PID|||MRN-DEMO-888^^^HOSP||DAVIS^SARAH^E^MS||19881201|F||2106-3^White|555 PINE ST^^AUSTIN^TX^78701^USA||(512)555-4321||ENG^English|M^Married\r" +
                "NK1|1|DAVIS^JAMES||SPO^Spouse|555 PINE ST^^AUSTIN^TX^78701^USA|(512)555-8765\r" +
                "PD1|||CLINIC-TX^Austin Health Center|DOC-77^BOWMAN^DAVID";

        producerTemplate.sendBodyAndHeaders("direct:patient-id-demo-seq-in", hl7,
                java.util.Map.of("HIE_GATEWAY_INSTANCE", "pas-gw", "HIE_TRIGGER_TYPE", "A01"));

        mockOut.assertIsSatisfied(2000);

        org.apache.camel.Exchange exchange = mockOut.getExchanges().get(0);
        assertThat(exchange.getMessage().getHeader(PatientIdentityUpdate.HEADER_PATIENT_ID)).isEqualTo("MRN-DEMO-888");
        assertThat(exchange.getMessage().getHeader(PatientIdentityUpdate.HEADER_PATIENT_MRN)).isEqualTo("MRN-DEMO-888");
        assertThat(exchange.getMessage().getHeader(PatientIdentityUpdate.HEADER_PATIENT_NAME)).isEqualTo("SARAH E DAVIS MS");
        assertThat(exchange.getMessage().getHeader(PatientDemographicsUpdate.HEADER_PATIENT_GENDER)).isEqualTo("female");
        assertThat(exchange.getMessage().getHeader(PatientDemographicsUpdate.HEADER_PATIENT_DOB)).isEqualTo("1988-12-01");
        assertThat(exchange.getMessage().getHeader(PatientDemographicsUpdate.HEADER_PATIENT_MARITAL_STATUS)).isEqualTo("Married");
        assertThat(exchange.getMessage().getHeader(PatientIdentityUpdate.HEADER_PATIENT_UPDATED)).isEqualTo(true);
        assertThat(exchange.getMessage().getHeader(PatientDemographicsUpdate.HEADER_PATIENT_DEMOGRAPHICS_UPDATED)).isEqualTo(true);

        TaskEvent event = exchange.getMessage().getBody(TaskEvent.class);
        assertThat(event).isNotNull();
        assertThat(event.getAction()).isEqualTo("PROCESS");
        assertThat(event.getDescription()).contains("DAVIS");

        org.hl7.fhir.r5.model.Task cachedTask = demographicsAct.getTaskCacheService().getTask(event.getTaskId()).orElse(null);
        assertThat(cachedTask).isNotNull();
        assertThat(cachedTask.getFor().getDisplay()).isEqualTo("SARAH E DAVIS MS");
    }

    @Test
    @DisplayName("Should load persisted sequence from cache, wire activities, and execute pipeline route on matching event")
    void testEndToEndPipelineExecutionWithMatchingEvent() throws Exception {
        Step1Activity.executionCount.set(0);
        Step2Activity.executionCount.set(0);

        Step1Activity act1 = new Step1Activity();
        Step2Activity act2 = new Step2Activity();
        Instance<TaskProcessingActivity> mockActivities = new MockActivityInstance<>(List.of(act1, act2));

        // Create and persist specific sequence in cache
        TaskSequence pasAdmitSequence = new TaskSequence("seq-pas-admissions", "PAS Admissions Sequence");
        pasAdmitSequence.setInputEndpoint("direct:pas-admit-in");
        pasAdmitSequence.setOutputEndpoint("mock:pipeline-out");
        pasAdmitSequence.setTargetGatewayInstances(List.of("pas-gw"));
        pasAdmitSequence.setTargetTriggerTypes(List.of("A01", "A04"));
        pasAdmitSequence.setActivityIds(List.of("act-step-1", "act-step-2"));

        sequenceService.save(pasAdmitSequence);

        loader = new TaskSequenceLoader(sequenceService, mockActivities);
        loader.loadAndRegisterSequences(camelContext);
        camelContext.start();

        MockEndpoint mockOut = camelContext.getEndpoint("mock:pipeline-out", MockEndpoint.class);
        mockOut.expectedMessageCount(1);

        // Send matching TaskEvent
        TaskEvent matchingEvent = new TaskEvent("task-matching-1", "PROCESS", "REQUESTED",
                "pas-gw", "ADT", "A01", "MSG-101", "mllp-gateway", "Admit event");

        producerTemplate.sendBodyAndHeaders("direct:pas-admit-in", matchingEvent,
                java.util.Map.of("HIE_GATEWAY_INSTANCE", "pas-gw", "HIE_TRIGGER_TYPE", "A01"));

        mockOut.assertIsSatisfied(2000);
        assertThat(Step1Activity.executionCount.get()).isEqualTo(1);
        assertThat(Step2Activity.executionCount.get()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should filter out non-matching events based on gateway instance or trigger type")
    void testFilteringNonMatchingEvent() throws Exception {
        Step1Activity.executionCount.set(0);
        Step2Activity.executionCount.set(0);

        Step1Activity act1 = new Step1Activity();
        Step2Activity act2 = new Step2Activity();
        Instance<TaskProcessingActivity> mockActivities = new MockActivityInstance<>(List.of(act1, act2));

        TaskSequence limsSequence = new TaskSequence("seq-lims-results", "LIMS Results Sequence");
        limsSequence.setInputEndpoint("direct:lims-in");
        limsSequence.setOutputEndpoint("mock:lims-out");
        limsSequence.setTargetGatewayInstances(List.of("lims-gw"));
        limsSequence.setTargetTriggerTypes(List.of("R01", "ORU^R01"));
        limsSequence.setActivityIds(List.of("act-step-1"));

        sequenceService.save(limsSequence);

        loader = new TaskSequenceLoader(sequenceService, mockActivities);
        loader.loadAndRegisterSequences(camelContext);
        camelContext.start();

        MockEndpoint mockOut = camelContext.getEndpoint("mock:lims-out", MockEndpoint.class);
        mockOut.expectedMessageCount(0);

        // Send non-matching event (PAS gateway with ADT A01 to LIMS route)
        TaskEvent nonMatchingEvent = new TaskEvent("task-nonmatch-1", "PROCESS", "REQUESTED",
                "pas-gw", "ADT", "A01", "MSG-102", "mllp-gateway", "PAS Admit");

        producerTemplate.sendBodyAndHeaders("direct:lims-in", nonMatchingEvent,
                java.util.Map.of("HIE_GATEWAY_INSTANCE", "pas-gw", "HIE_TRIGGER_TYPE", "A01"));

        mockOut.assertIsSatisfied(1000);
        assertThat(Step1Activity.executionCount.get()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should execute sequence starting with MessageQueueToExchangeConduit and chaining to patient updates")
    void testMessageQueueConduitInTaskSequencePipeline() throws Exception {
        net.fhirfactory.hie.taskprocessors.infrastructure.MessageQueueToExchangeConduit conduit =
                new net.fhirfactory.hie.taskprocessors.infrastructure.MessageQueueToExchangeConduit();
        PatientIdentityUpdate identityAct = new PatientIdentityUpdate();
        PatientDemographicsUpdate demographicsAct = new PatientDemographicsUpdate();

        Instance<TaskProcessingActivity> mockActivities = new MockActivityInstance<>(List.of(conduit, identityAct, demographicsAct));

        TaskSequence seq = new TaskSequence("seq-conduit-test", "Conduit Pipeline Sequence");
        seq.setInputEndpoint("direct:conduit-seq-in");
        seq.setOutputEndpoint("mock:conduit-seq-out");
        seq.setTargetGatewayInstances(List.of("*"));
        seq.setTargetTriggerTypes(List.of("*"));
        seq.setActivityIds(List.of(
                net.fhirfactory.hie.taskprocessors.infrastructure.MessageQueueToExchangeConduit.DEFAULT_ACTIVITY_ID,
                PatientIdentityUpdate.DEFAULT_ACTIVITY_ID,
                PatientDemographicsUpdate.DEFAULT_ACTIVITY_ID
        ));

        sequenceService.save(seq);

        loader = new TaskSequenceLoader(sequenceService, mockActivities);
        loader.loadAndRegisterSequences(camelContext);
        camelContext.start();

        MockEndpoint mockOut = camelContext.getEndpoint("mock:conduit-seq-out", MockEndpoint.class);
        mockOut.expectedMessageCount(1);

        String hl7 = "MSH|^~\\&|PAS|HOSP|HIE|REC|20260908120000||ADT^A01|MSG-CONDUIT-01|P|2.4\r" +
                "PID|||MRN-CONDUIT-999^^^HOSP||TAYLOR^EMMA^J^^MS||19920704|F|||500 BROADWAY^^SEATTLE^WA^98101^USA";

        producerTemplate.sendBodyAndHeaders("direct:conduit-seq-in", hl7,
                java.util.Map.of("HIE_GATEWAY_INSTANCE", "pas-gw", "HIE_TRIGGER_TYPE", "A01"));

        mockOut.assertIsSatisfied(2000);

        org.apache.camel.Exchange exchange = mockOut.getExchanges().get(0);
        assertThat(exchange.getMessage().getHeader(PatientIdentityUpdate.HEADER_PATIENT_ID)).isEqualTo("MRN-CONDUIT-999");
        assertThat(exchange.getMessage().getHeader(PatientIdentityUpdate.HEADER_PATIENT_NAME)).isEqualTo("MS EMMA J TAYLOR");
        assertThat(exchange.getMessage().getHeader(PatientDemographicsUpdate.HEADER_PATIENT_GENDER)).isEqualTo("female");
        assertThat(exchange.getMessage().getHeader(PatientDemographicsUpdate.HEADER_PATIENT_DOB)).isEqualTo("1992-07-04");
    }
}
