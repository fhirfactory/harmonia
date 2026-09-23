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

package net.fhirfactory.harmonia.erga;

import net.fhirfactory.harmonia.erga.base.ErgonBase;
import net.fhirfactory.harmonia.model.ergon.ErgonEvent;
import net.fhirfactory.harmonia.model.ergon.ErgonPayload;
import net.fhirfactory.harmonia.model.pragma.Pragma;
import net.fhirfactory.harmonia.model.pragma.PragmaCheckpoint;
import net.fhirfactory.harmonia.model.pragma.PragmaFhirConverter;
import net.fhirfactory.harmonia.model.pragma.PragmaStatus;
import net.fhirfactory.harmonia.model.topic.Topic;
import net.fhirfactory.harmonia.praxis.cache.TaskCacheService;
import net.fhirfactory.harmonia.themis.api.model.PrincipalType;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthority;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.core.identities.HarmoniaServiceIdentities;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultCamelContext;
import org.hl7.fhir.r5.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ErgonBaseTest {

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

    static class SampleTaskActivity extends ErgonBase {
        private boolean activityConfigured = false;

        public SampleTaskActivity() {
            super("sample-activity-1", "Sample Task Activity");
            setInputEndpoint("direct:sample-input");
            setOutputEndpoint("mock:sample-output");
        }

        public SampleTaskActivity(CamelContext context) {
            super(context, "sample-activity-ctx", "Sample Context Activity");
        }

        @Override
        protected void configureActivity() throws Exception {
            this.activityConfigured = true;
            applyStandardActivityDecorations(
                    from(getInputEndpoint() != null ? getInputEndpoint() : "direct:default-in")
            ).transform().simple("Processed: ${body}")
             .to(getOutputEndpoint() != null ? getOutputEndpoint() : "mock:default-out");
        }

        public boolean isActivityConfigured() {
            return activityConfigured;
        }
    }

    static class DummyProcessorActivity extends ErgonBase {
        public DummyProcessorActivity() {
            super("dummy-processor", "Dummy Task Processor");
            setInputEndpoint("direct:dummy-in");
            setOutputEndpoint("mock:dummy-out");
        }

        @Override
        protected void processErgon(Pragma pragma, Exchange exchange) throws Exception {
            assertThat(pragma).isNotNull();

            // Attach Patient and add discrete outputs
            Patient patient = new Patient();
            patient.setId("Patient/PAT-TEST-001");
            patient.addName().setFamily("Smith").addGiven("John").setText("John Smith");

            Topic container = new Topic("Health", "FHIR", "R5", "Patient", null);
            Topic content = new Topic("Health", "Clinical", "1.0", "Patient", "Demographics");
            pragma.addOutput(ErgonPayload.fromFhirResource(0, container, content, patient));

            Topic obsContainer = new Topic("Health", "FHIR", "R5", "Observation", null);
            Topic obsContent = new Topic("Health", "Clinical", "1.0", "Observation", "Vitals");
            pragma.addOutput(ErgonPayload.fromFhirResource(1, obsContainer, obsContent,
                    new Reference("Observation/OBS-101").setDisplay("Vital Signs Observation")));

            Topic repContainer = new Topic("Health", "FHIR", "R5", "DiagnosticReport", null);
            Topic repContent = new Topic("Health", "Clinical", "1.0", "DiagnosticReport", "CBC");
            pragma.addOutput(ErgonPayload.fromFhirResource(2, repContainer, repContent,
                    new Reference("DiagnosticReport/REP-202").setDisplay("CBC Diagnostic Report")));

            pragma.setStatus(PragmaStatus.COMPLETED);
        }
    }

    @Test
    void testInheritanceAndProperties() {
        SampleTaskActivity activity = new SampleTaskActivity();
        assertThat(activity).isInstanceOf(RouteBuilder.class);
        assertThat(activity).isInstanceOf(ErgonBase.class);

        assertThat(activity.getActivityId()).isEqualTo("sample-activity-1");
        assertThat(activity.getActivityName()).isEqualTo("Sample Task Activity");
        assertThat(activity.getVersion()).isEqualTo("1.0.0");
        assertThat(activity.isEnabled()).isTrue();

        activity.setActivityDescription("Sample description");
        activity.setVersion("2.1.0");
        activity.setErrorEndpoint("direct:error-queue");

        assertThat(activity.getActivityDescription()).isEqualTo("Sample description");
        assertThat(activity.getVersion()).isEqualTo("2.1.0");
        assertThat(activity.getErrorEndpoint()).isEqualTo("direct:error-queue");
        assertThat(activity.generateRouteId()).isEqualTo("activity-sample-activity-1");
        assertThat(activity.toString()).contains("sample-activity-1").contains("2.1.0");
    }

    @Test
    void testDefaultConstructors() {
        ErgonBase defaultActivity = new ErgonBase() {
            @Override
            protected void configureActivity() {
            }
        };

        assertThat(defaultActivity.getActivityName()).isEqualTo(defaultActivity.getClass().getSimpleName());
        assertThat(defaultActivity.getActivityId()).isEqualTo(defaultActivity.getClass().getName());
        assertThat(defaultActivity.generateRouteId()).startsWith("activity-");
    }

    @Test
    void testContextConstructors() {
        SampleTaskActivity activity = new SampleTaskActivity(camelContext);
        assertThat(activity.getCamelContext()).isEqualTo(camelContext);
        assertThat(activity.getActivityId()).isEqualTo("sample-activity-ctx");
        assertThat(activity.getActivityName()).isEqualTo("Sample Context Activity");
    }

    @Test
    void testRouteExecutionInCamelContext() throws Exception {
        SampleTaskActivity activity = new SampleTaskActivity();
        camelContext.addRoutes(activity);
        camelContext.start();

        assertThat(activity.isActivityConfigured()).isTrue();
        assertThat(camelContext.getRoute("activity-sample-activity-1")).isNotNull();

        String result = producerTemplate.requestBody("direct:sample-input", "Task Payload 123", String.class);
        assertThat(result).isEqualTo("Processed: Task Payload 123");
    }

    @Test
    void testDisabledActivitySkipsConfiguration() throws Exception {
        SampleTaskActivity activity = new SampleTaskActivity();
        activity.setEnabled(false);
        camelContext.addRoutes(activity);
        camelContext.start();

        assertThat(activity.isActivityConfigured()).isFalse();
        assertThat(camelContext.getRoutes()).isEmpty();
    }

    @Test
    @DisplayName("Requirement 1: ErgonBase retrieves Pragma from cache on ingress and sets it as message body")
    void testTaskRetrievalFromCacheOnIngress() throws Exception {
        DummyProcessorActivity activity = new DummyProcessorActivity();
        TaskCacheService cacheService = activity.getTaskCacheService();

        // Seed an existing Pragma into the cache
        Pragma initialPragma = new Pragma("PRAGMA-CACHE-INGRESS-001", "praxis-test", PragmaStatus.REQUESTED);
        cacheService.savePragma(initialPragma);

        Exchange exchange = new org.apache.camel.support.DefaultExchange(camelContext);
        exchange.getMessage().setHeader(ErgonBase.HEADER_PRAGMA_ID, "PRAGMA-CACHE-INGRESS-001");
        exchange.getMessage().setHeader(ErgonBase.HEADER_TASK_ID, "PRAGMA-CACHE-INGRESS-001");

        // Run ingress
        activity.processIngress(exchange);

        // Verify that the retrieved Pragma is now the message body
        Object body = exchange.getMessage().getBody();
        assertThat(body).isInstanceOf(Pragma.class);
        Pragma ingressPragma = (Pragma) body;
        assertThat(ingressPragma.getPragmaId()).isEqualTo("PRAGMA-CACHE-INGRESS-001");
        assertThat(exchange.getProperty(ErgonBase.PROPERTY_PRAGMA)).isEqualTo(ingressPragma);
        assertThat(ingressPragma.getCheckpoints()).isNotEmpty();
    }

    @Test
    @DisplayName("Requirement 1: ErgonBase creates baseline Pragma if not already in cache on ingress")
    void testBaselineTaskCreatedIfNotInCacheOnIngress() throws Exception {
        DummyProcessorActivity activity = new DummyProcessorActivity();

        Exchange exchange = new org.apache.camel.support.DefaultExchange(camelContext);
        exchange.getMessage().setHeader(ErgonBase.HEADER_TASK_ID, "TASK-NEW-002");
        exchange.getMessage().setBody("Raw HL7 or JSON string payload");

        // Run ingress
        activity.processIngress(exchange);

        // Verify baseline Pragma was created and saved to cache
        Object body = exchange.getMessage().getBody();
        assertThat(body).isInstanceOf(Pragma.class);
        Pragma ingressPragma = (Pragma) body;
        assertThat(ingressPragma.getPragmaId()).isEqualTo("TASK-NEW-002");
        assertThat(ingressPragma.getStatus()).isEqualTo(PragmaStatus.IN_PROGRESS);

        Optional<Pragma> inCache = activity.getTaskCacheService().getPragma("TASK-NEW-002");
        assertThat(inCache).isPresent();
    }

    @Test
    @DisplayName("Requirement 2: ErgonBase egress persists Pragma and records checkpoint and FHIR compatibility")
    void testEgressProcessingAndProvenanceAndTaskEvents() throws Exception {
        DummyProcessorActivity activity = new DummyProcessorActivity();
        TaskCacheService cacheService = activity.getTaskCacheService();

        // Create Pragma with 2 discrete outputs
        Pragma processedPragma = new Pragma("PRAGMA-PARENT-888", "praxis-e2e", PragmaStatus.IN_PROGRESS);

        Topic obsTopic = new Topic("Health", "FHIR", "R5", "Observation", null);
        processedPragma.addOutput(ErgonPayload.fromFhirResource(0, obsTopic, obsTopic,
                new Reference("Observation/OBS-1").setDisplay("Blood Glucose")));

        Topic medTopic = new Topic("Health", "FHIR", "R5", "MedicationRequest", null);
        processedPragma.addOutput(ErgonPayload.fromFhirResource(1, medTopic, medTopic,
                new Reference("MedicationRequest/MED-2").setDisplay("Metformin")));

        Exchange exchange = new org.apache.camel.support.DefaultExchange(camelContext);
        exchange.getMessage().setBody(processedPragma);
        exchange.getMessage().setHeader(ErgonBase.HEADER_PRAGMA_ID, "PRAGMA-PARENT-888");
        exchange.getMessage().setHeader(ErgonBase.HEADER_GATEWAY_INSTANCE, "mllp-gw-test");
        exchange.getMessage().setHeader(ErgonBase.HEADER_TRIGGER_TYPE, "A01");

        // Execute egress processing
        activity.processEgress(exchange);

        // (a) Pragma is written to cache with checkpoint
        Optional<Pragma> cachedParent = cacheService.getPragma("PRAGMA-PARENT-888");
        assertThat(cachedParent).isPresent();
        assertThat(cachedParent.get().getCheckpoints()).isNotEmpty();

        // (b) New Task resource created for each discrete object contained within output
        Task fhirTask = PragmaFhirConverter.toFhirTask(processedPragma);
        List<Task> createdOutgoingTasks = activity.createOutgoingTasks(fhirTask);
        assertThat(createdOutgoingTasks).hasSize(2);

        // (c) Provenances created
        @SuppressWarnings("unchecked")
        List<Provenance> provenances = (List<Provenance>) exchange.getProperty(ErgonBase.PROPERTY_PROVENANCES);
        assertThat(provenances).hasSize(2);

        // (d) Retains ErgonEvent in message body and Pragma in property
        Object egressBody = exchange.getMessage().getBody();
        assertThat(egressBody).isInstanceOf(ErgonEvent.class);
        ErgonEvent egressEvent = (ErgonEvent) egressBody;
        assertThat(egressEvent.getTaskId()).startsWith("PRAGMA-PARENT-888-out-");
        assertThat(exchange.getProperty(ErgonBase.PROPERTY_PRAGMA)).isEqualTo(processedPragma);
        assertThat(exchange.getMessage().getHeader(ErgonBase.HEADER_TASK_PROCESSED)).isEqualTo(true);
    }

    @Test
    @DisplayName("Full Activity Route: ingress -> processErgon -> egress executes end-to-end")
    void testFullActivityRouteExecution() throws Exception {
        DummyProcessorActivity activity = new DummyProcessorActivity();
        camelContext.addRoutes(activity);
        camelContext.start();

        MockEndpoint mockOut = camelContext.getEndpoint("mock:dummy-out", MockEndpoint.class);
        mockOut.expectedMessageCount(1);

        Exchange resultExchange = producerTemplate.send("direct:dummy-in", exchange -> {
            exchange.getMessage().setHeader(ErgonBase.HEADER_PRAGMA_ID, "PRAGMA-E2E-001");
            exchange.getMessage().setHeader(ErgonBase.HEADER_TASK_ID, "PRAGMA-E2E-001");
            exchange.getMessage().setHeader(ErgonBase.HEADER_GATEWAY_INSTANCE, "mllp-gateway-default");
            exchange.getMessage().setHeader(ErgonBase.HEADER_TRIGGER_TYPE, "A01");
            Topic topic = new Topic("Health", "HL7", "2.4", "ADT", "A01");
            exchange.getMessage().setHeader(ErgonBase.HEADER_TOPIC, topic);
        });

        mockOut.assertIsSatisfied(2000);

        // Body out of route is ErgonEvent, property is Pragma
        ErgonEvent emittedEvent = mockOut.getExchanges().get(0).getMessage().getBody(ErgonEvent.class);
        assertThat(emittedEvent).isNotNull();
        assertThat(emittedEvent.getTaskId()).startsWith("PRAGMA-E2E-001-out-");

        Pragma emittedPragma = (Pragma) resultExchange.getProperty(ErgonBase.PROPERTY_PRAGMA);
        assertThat(emittedPragma).isNotNull();
        assertThat(emittedPragma.getPragmaId()).isEqualTo("PRAGMA-E2E-001");
        assertThat(emittedPragma.getStatus()).isEqualTo(PragmaStatus.COMPLETED);
        assertThat(emittedPragma.getOutput()).hasSize(3);

        // Verify Pragma in cache
        Optional<Pragma> cachedPragma = activity.getTaskCacheService().getPragma("PRAGMA-E2E-001");
        assertThat(cachedPragma).isPresent();
        assertThat(cachedPragma.get().getOutput()).hasSize(3);
        assertThat(cachedPragma.get().getCheckpoints()).hasSize(2); // INGRESS and EGRESS
    }

    @Test
    @DisplayName("Requirement 3: Child task creation propagates lineage, causation, and authoritative security context")
    void testChildTaskSecurityAndLineageContextPropagation() {
        DummyProcessorActivity activity = new DummyProcessorActivity();

        Pragma parentPragma = new Pragma("PRAGMA-PARENT-999", "praxis-security-test", PragmaStatus.IN_PROGRESS);
        parentPragma.setCorrelationId("CORR-ROOT-100");
        parentPragma.setCausationId("MSG-INGRESS-001");
        parentPragma.setPolicyVersion("1.0.0");

        ThemisPrincipal originatingPrincipal = ThemisPrincipal.of("dr-smith", PrincipalType.HUMAN, "clinical");
        parentPragma.setOriginatingPrincipal(originatingPrincipal);
        parentPragma.addOriginatingAuthority("provider.change.submit");
        parentPragma.addOriginatingAuthority("provider.read");
        parentPragma.setExecutingPrincipal(HarmoniaServiceIdentities.PRINCIPAL_PONOS_PROCESS);

        Topic obsTopic = new Topic("Health", "FHIR", "R5", "Observation", null);
        parentPragma.addOutput(ErgonPayload.fromFhirResource(0, obsTopic, obsTopic,
                new Reference("Observation/OBS-1").setDisplay("Blood Glucose")));
        parentPragma.addOutput(ErgonPayload.fromFhirResource(1, obsTopic, obsTopic,
                new Reference("Observation/OBS-2").setDisplay("Blood Pressure")));

        Task processedTask = PragmaFhirConverter.toFhirTask(parentPragma);
        List<Task> childTasks = activity.createOutgoingTasks(processedTask, parentPragma);

        assertThat(childTasks).hasSize(2);

        for (int i = 0; i < childTasks.size(); i++) {
            Task child = childTasks.get(i);
            String expectedChildId = "PRAGMA-PARENT-999-out-" + (i + 1);
            assertThat(child.getIdPart()).isEqualTo(expectedChildId);

            // Identifiers: Pragma ID, Correlation ID, Causation ID (parent task ID)
            assertThat(child.getIdentifier()).anyMatch(id ->
                    PragmaFhirConverter.IDENTIFIER_SYSTEM_PRAGMA_ID.equals(id.getSystem()) && expectedChildId.equals(id.getValue()));
            assertThat(child.getIdentifier()).anyMatch(id ->
                    PragmaFhirConverter.IDENTIFIER_SYSTEM_CORRELATION_ID.equals(id.getSystem()) && "CORR-ROOT-100".equals(id.getValue()));
            assertThat(child.getIdentifier()).anyMatch(id ->
                    PragmaFhirConverter.IDENTIFIER_SYSTEM_CAUSATION_ID.equals(id.getSystem()) && "PRAGMA-PARENT-999".equals(id.getValue()));

            // Parent link
            assertThat(child.getPartOfFirstRep().getReference()).isEqualTo("Task/PRAGMA-PARENT-999");

            // Authoritative Originating Principal extensions
            assertThat(child.getExtensionByUrl(PragmaFhirConverter.EXTENSION_SECURITY_PRINCIPAL_ID).getValue().toString()).isEqualTo("dr-smith");
            assertThat(child.getExtensionByUrl(PragmaFhirConverter.EXTENSION_SECURITY_PRINCIPAL_TYPE).getValue().toString()).isEqualTo("HUMAN");
            assertThat(child.getExtensionByUrl(PragmaFhirConverter.EXTENSION_SECURITY_SOURCE_DOMAIN).getValue().toString()).isEqualTo("clinical");

            // Executing Principal extensions (PROCESS:ponos-engine)
            assertThat(child.getExtensionByUrl(PragmaFhirConverter.EXTENSION_SECURITY_EXECUTING_PRINCIPAL_ID).getValue().toString()).isEqualTo("process:ponos-engine");
            assertThat(child.getExtensionByUrl(PragmaFhirConverter.EXTENSION_SECURITY_EXECUTING_PRINCIPAL_TYPE).getValue().toString()).isEqualTo("PROCESS");

            // Authority extensions
            List<String> authorityValues = child.getExtensionsByUrl(PragmaFhirConverter.EXTENSION_SECURITY_AUTHORITY).stream()
                    .map(e -> ((StringType) e.getValue()).getValue())
                    .toList();
            assertThat(authorityValues).containsExactlyInAnyOrder("provider.change.submit", "provider.read");

            // Policy version extension
            assertThat(child.getExtensionByUrl(PragmaFhirConverter.EXTENSION_SECURITY_POLICY_VERSION).getValue().toString()).isEqualTo("1.0.0");
        }
    }

    @Test
    @DisplayName("Requirement 4: Ingress fallback Pragma does not manufacture trusted security state")
    void testIngressFallbackHardenedAgainstSecurityManufacture() throws Exception {
        DummyProcessorActivity activity = new DummyProcessorActivity();

        Exchange exchange = new org.apache.camel.support.DefaultExchange(camelContext);
        exchange.getMessage().setHeader(ErgonBase.HEADER_TASK_ID, "TASK-UNAUTH-001");
        exchange.getMessage().setBody("{\"raw\":\"payload\"}");

        activity.processIngress(exchange);

        Pragma ingressPragma = (Pragma) exchange.getMessage().getBody();
        assertThat(ingressPragma).isNotNull();
        assertThat(ingressPragma.getOriginatingPrincipal()).isNull();
        assertThat(ingressPragma.getExecutingPrincipal()).isNull();
        assertThat(ingressPragma.getOriginatingAuthorities()).isEmpty();
        assertThat(ingressPragma.getOriginatingSecurityContext()).isNull();

        Task fhirTask = (Task) exchange.getProperty(ErgonBase.PROPERTY_INCOMING_TASK);
        assertThat(fhirTask).isNotNull();
        assertThat(fhirTask.getExtensionByUrl(PragmaFhirConverter.EXTENSION_SECURITY_PRINCIPAL_ID)).isNull();
        assertThat(fhirTask.getExtensionByUrl(PragmaFhirConverter.EXTENSION_SECURITY_EXECUTING_PRINCIPAL_ID)).isNull();
        assertThat(fhirTask.getExtensionsByUrl(PragmaFhirConverter.EXTENSION_SECURITY_AUTHORITY)).isEmpty();
    }

    @Test
    @DisplayName("Requirement 5: createOutgoingTasks without authoritative Pragma does not manufacture security credentials")
    void testOutgoingTaskWithoutAuthoritativePragmaDoesNotManufactureSecurityState() {
        DummyProcessorActivity activity = new DummyProcessorActivity();

        Task processedTask = new Task();
        processedTask.setId("Task/TASK-NO-AUTH-001");
        processedTask.addIdentifier(new Identifier()
                .setSystem(PragmaFhirConverter.IDENTIFIER_SYSTEM_CORRELATION_ID)
                .setValue("CORR-NO-AUTH-123"));

        List<Task> outgoingTasks = activity.createOutgoingTasks(processedTask, null);
        assertThat(outgoingTasks).hasSize(1);
        Task child = outgoingTasks.get(0);

        assertThat(child.getIdPart()).isEqualTo("TASK-NO-AUTH-001-out-1");
        assertThat(child.getIdentifier()).anyMatch(id ->
                PragmaFhirConverter.IDENTIFIER_SYSTEM_CAUSATION_ID.equals(id.getSystem()) && "TASK-NO-AUTH-001".equals(id.getValue()));
        assertThat(child.getIdentifier()).anyMatch(id ->
                PragmaFhirConverter.IDENTIFIER_SYSTEM_CORRELATION_ID.equals(id.getSystem()) && "CORR-NO-AUTH-123".equals(id.getValue()));

        // Ensure zero security extensions are manufactured
        assertThat(child.getExtensionByUrl(PragmaFhirConverter.EXTENSION_SECURITY_PRINCIPAL_ID)).isNull();
        assertThat(child.getExtensionByUrl(PragmaFhirConverter.EXTENSION_SECURITY_EXECUTING_PRINCIPAL_ID)).isNull();
        assertThat(child.getExtensionsByUrl(PragmaFhirConverter.EXTENSION_SECURITY_AUTHORITY)).isEmpty();
    }
}
