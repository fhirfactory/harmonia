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

package net.fhirfactory.harmonia.praxis.conduit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.fhirfactory.harmonia.erga.base.ErgonBase;
import net.fhirfactory.harmonia.erga.base.ErgonException;
import net.fhirfactory.harmonia.model.ergon.ErgonPayload;
import net.fhirfactory.harmonia.model.pragma.Pragma;
import net.fhirfactory.harmonia.model.pragma.PragmaFhirConverter;
import net.fhirfactory.harmonia.model.pragma.PragmaStatus;
import net.fhirfactory.harmonia.model.security.HarmoniaAuthorityEnum;
import net.fhirfactory.harmonia.model.topic.Topic;
import net.fhirfactory.harmonia.petasos.api.Petasos;
import net.fhirfactory.harmonia.petasos.api.consumer.PetasosMessageContext;
import net.fhirfactory.harmonia.petasos.api.consumer.PetasosMessageHandler;
import net.fhirfactory.harmonia.petasos.api.consumer.PetasosSubscription;
import net.fhirfactory.harmonia.petasos.api.destination.PetasosDestination;
import net.fhirfactory.harmonia.petasos.api.message.PetasosMessage;
import net.fhirfactory.harmonia.praxis.sequence.Praxis;
import net.fhirfactory.harmonia.themis.api.model.PrincipalType;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PetasosQueueToExchangeConduitTest {

    private CamelContext camelContext;
    private Petasos petasosMock;
    private PetasosSubscription petasosSubscriptionMock;
    private PetasosMessageHandler registeredHandler;

    @BeforeEach
    void setUp() throws Exception {
        camelContext = new DefaultCamelContext();
        petasosMock = mock(Petasos.class);
        petasosSubscriptionMock = mock(PetasosSubscription.class);

        when(petasosMock.receive(any(PetasosDestination.class), any(PetasosMessageHandler.class))).thenAnswer(invocation -> {
            registeredHandler = invocation.getArgument(1);
            return petasosSubscriptionMock;
        });
    }

    @AfterEach
    void tearDown() throws Exception {
        if (camelContext != null) {
            camelContext.stop();
        }
    }

    // Sample Simple Ergon
    static class SimplePassErgon extends ErgonBase {
        public SimplePassErgon() {
            super("ergon-simple-pass", "Simple Pass Ergon");
        }

        @Override
        protected void processErgon(Pragma pragma, Exchange exchange) throws Exception {
            Topic container = new Topic("Health", "HL7", "2.4", "ADT", "A01");
            Topic content = new Topic("Health", "Clinical", "1.0", "PatientId", null);
            pragma.addOutput(ErgonPayload.fromJson(0, container, content, "{\"result\":\"PROCESSED\"}"));
            pragma.setStatus(PragmaStatus.COMPLETED);
        }
    }

    // Sample Failing Ergon
    static class SimpleFailErgon extends ErgonBase {
        public SimpleFailErgon() {
            super("ergon-simple-fail", "Simple Fail Ergon");
        }

        @Override
        protected void processErgon(Pragma pragma, Exchange exchange) throws Exception {
            throw new ErgonException("ergon-simple-fail", pragma.getPragmaId(), "Processing exploded");
        }
    }

    @Test
    @DisplayName("Converts PetasosMessage envelope into canonical Pragma with metadata and leaves security context null when unauthenticated")
    void testPetasosMessageToPragmaConversion() {
        PetasosQueueToExchangeConduit conduit = new PetasosQueueToExchangeConduit(petasosMock, camelContext, "petasos.queue.tasks", null);

        PetasosMessage message = PetasosMessage.builder()
                .messageId("msg-petasos-001")
                .correlationId("corr-999")
                .causationId("cause-888")
                .messageType("ADT^A01")
                .contentType("text/plain")
                .source("pylai-mllp-inbound")
                .destination(PetasosDestination.queue("ponos-engine"))
                .priority(7)
                .payload("MSH|^~\\&|PAS|HOSP|HIE|REC|20260908120000||ADT^A01|MSG-001|P|2.4\rPID|||MRN-101")
                .metadata(java.util.Map.of("facility", "Central-Hospital"))
                .build();

        Pragma pragma = conduit.convertToPragma(message);

        assertThat(pragma).isNotNull();
        assertThat(pragma.getPragmaId()).isEqualTo("msg-petasos-001");
        assertThat(pragma.getCorrelationId()).isEqualTo("corr-999");
        assertThat(pragma.getCausationId()).isEqualTo("cause-888");
        assertThat(pragma.getSource()).isEqualTo("pylai-mllp-inbound");
        assertThat(pragma.getDestination()).isEqualTo("ponos-engine");
        assertThat(pragma.getPriority()).isEqualTo(7);
        assertThat(pragma.getMetadata()).containsEntry("facility", "Central-Hospital");
        assertThat(pragma.getInput()).hasSize(1);
        assertThat(pragma.getInput().get(0).getJsonString()).contains("MRN-101");

        // Missing security context remains null / unauthenticated and is NOT synthesized
        assertThat(pragma.getOriginatingPrincipal()).isNull();
        assertThat(pragma.getOriginatingSecurityContext()).isNull();
        assertThat(pragma.getOriginatingAuthorities()).isEmpty();
    }

    @Test
    @DisplayName("Authoritative Pragma payload preserved over conflicting Petasos transport metadata")
    void testAuthoritativePragmaPayloadPreservedOverConflictingTransportMetadata() throws Exception {
        PetasosQueueToExchangeConduit conduit = new PetasosQueueToExchangeConduit(petasosMock, camelContext, "petasos.queue.tasks", null);

        ThemisPrincipal domainPrincipal = ThemisPrincipal.of("user:dr.mark", PrincipalType.HUMAN, "hospital-east");
        ThemisSecurityContext domainContext = ThemisSecurityContext.builder()
                .principal(domainPrincipal)
                .authorities(Set.of(HarmoniaAuthorityEnum.PROVIDER_CHANGE_SUBMIT.toThemisAuthority()))
                .correlationId("domain-corr-111")
                .causationId("domain-cause-222")
                .build();

        Pragma authoritativePragma = Pragma.builder()
                .pragmaId("pragma-domain-001")
                .correlationId("domain-corr-111")
                .causationId("domain-cause-222")
                .source("domain-gateway-submitter")
                .destination("domain-destination-registry")
                .status(PragmaStatus.ACCEPTED)
                .originatingPrincipal(domainPrincipal)
                .addOriginatingAuthority(HarmoniaAuthorityEnum.PROVIDER_CHANGE_SUBMIT.toThemisAuthority())
                .originatingSecurityContext(domainContext)
                .policyVersion("1.0.0")
                .build();

        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        String pragmaJson = mapper.writeValueAsString(authoritativePragma);

        // Petasos message with CONFLICTING transport metadata
        PetasosMessage message = PetasosMessage.builder()
                .messageId("transport-msg-999")
                .correlationId("transport-corr-999")
                .causationId("transport-cause-999")
                .source("transport-source-untrusted")
                .destination(PetasosDestination.queue("transport-dest-untrusted"))
                .messageType("PRAGMA")
                .contentType("application/json")
                .payload(pragmaJson)
                .build();

        Pragma convertedPragma = conduit.convertToPragma(message);

        assertThat(convertedPragma).isNotNull();
        // Domain payload fields must WIN over transport metadata
        assertThat(convertedPragma.getPragmaId()).isEqualTo("pragma-domain-001");
        assertThat(convertedPragma.getCorrelationId()).isEqualTo("domain-corr-111");
        assertThat(convertedPragma.getCausationId()).isEqualTo("domain-cause-222");
        assertThat(convertedPragma.getSource()).isEqualTo("domain-gateway-submitter");
        assertThat(convertedPragma.getDestination()).isEqualTo("domain-destination-registry");

        // Security context must be preserved from authoritative domain payload
        assertThat(convertedPragma.getOriginatingPrincipal()).isEqualTo(domainPrincipal);
        assertThat(convertedPragma.getOriginatingAuthorities())
                .containsExactly(HarmoniaAuthorityEnum.PROVIDER_CHANGE_SUBMIT.toThemisAuthority());
        assertThat(convertedPragma.getOriginatingSecurityContext()).isEqualTo(domainContext);
    }

    @Test
    @DisplayName("Missing security context remains unauthenticated and does NOT synthesize service:petasos")
    void testMissingSecurityContextRemainsNullAndUntrusted() {
        PetasosQueueToExchangeConduit conduit = new PetasosQueueToExchangeConduit(petasosMock, camelContext, "petasos.queue.tasks", null);

        PetasosMessage message = PetasosMessage.builder()
                .messageId("msg-anon-001")
                .correlationId("corr-anon-001")
                .messageType("ADT^A01")
                .payload("RAW_UNAUTHENTICATED_PAYLOAD")
                .build();

        Pragma pragma = conduit.convertToPragma(message);

        assertThat(pragma).isNotNull();
        assertThat(pragma.getOriginatingPrincipal()).isNull();
        assertThat(pragma.getOriginatingAuthorities()).isEmpty();
        assertThat(pragma.getOriginatingSecurityContext()).isNull();
    }

    @Test
    @DisplayName("Petasos queue message ingress dispatches to Camel route and acknowledges message on success")
    void testPetasosIngressAndAcknowledgeOnSuccess() throws Exception {
        // Build Camel test route
        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                from("direct:petasos-test-target")
                        .routeId("petasos-test-target-route")
                        .process(exchange -> {
                            Pragma p = exchange.getMessage().getBody(Pragma.class);
                            assertThat(p).isNotNull();
                            p.setStatus(PragmaStatus.COMPLETED);
                        })
                        .to("mock:petasos-test-out");
            }
        });
        camelContext.start();

        MockEndpoint mockOut = camelContext.getEndpoint("mock:petasos-test-out", MockEndpoint.class);
        mockOut.expectedMessageCount(1);

        PetasosQueueToExchangeConduit conduit = new PetasosQueueToExchangeConduit(
                petasosMock, camelContext, "petasos.queue.tasks", "direct:petasos-test-target");

        conduit.start();
        assertThat(conduit.isRunning()).isTrue();
        assertThat(registeredHandler).isNotNull();

        PetasosMessage message = PetasosMessage.builder()
                .messageId("msg-ack-100")
                .correlationId("corr-100")
                .messageType("ADT^A01")
                .payload("HL7-DATA-ACK")
                .build();

        PetasosMessageContext contextMock = mock(PetasosMessageContext.class);

        // Simulate incoming message from Petasos queue
        registeredHandler.onMessage(message, contextMock);

        mockOut.assertIsSatisfied(2000);

        // Verify message was acknowledged
        verify(contextMock, times(1)).acknowledge();
        verify(contextMock, never()).reject();
        verify(contextMock, never()).reject(anyBoolean());

        conduit.stop();
        assertThat(conduit.isRunning()).isFalse();
    }

    @Test
    @DisplayName("Petasos queue message ingress rejects message on workflow execution failure")
    void testPetasosIngressAndRejectOnFailure() throws Exception {
        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                from("direct:petasos-fail-target")
                        .routeId("petasos-fail-target-route")
                        .process(exchange -> {
                            throw new RuntimeException("Workflow failure processing message");
                        });
            }
        });
        camelContext.start();

        PetasosQueueToExchangeConduit conduit = new PetasosQueueToExchangeConduit(
                petasosMock, camelContext, "petasos.queue.tasks", "direct:petasos-fail-target");

        conduit.start();
        assertThat(registeredHandler).isNotNull();

        PetasosMessage message = PetasosMessage.builder()
                .messageId("msg-nack-200")
                .correlationId("corr-200")
                .messageType("ADT^A01")
                .payload("HL7-DATA-NACK")
                .build();

        PetasosMessageContext contextMock = mock(PetasosMessageContext.class);

        // Simulate incoming message
        registeredHandler.onMessage(message, contextMock);

        // Verify message was rejected
        verify(contextMock, times(1)).reject();
        verify(contextMock, never()).acknowledge();

        conduit.stop();
    }

    @Test
    @DisplayName("PragmaWorkflowDispatcher resolves and dispatches Pragma into Praxis workflow")
    void testPragmaWorkflowDispatcher() throws Exception {
        Praxis praxis = new Praxis("praxis-dispatch-test", "Dispatcher Test Pipeline");
        SimplePassErgon ergon = new SimplePassErgon();
        praxis.addActivity(ergon);
        praxis.configureChainedEndpoints();
        praxis.registerRoutes(camelContext);
        camelContext.addRoutes(praxis.createSequencePipelineRoute());
        camelContext.start();

        PragmaWorkflowDispatcher dispatcher = new PragmaWorkflowDispatcher(camelContext, null);
        dispatcher.registerWorkflow(praxis);

        ThemisPrincipal principal = ThemisPrincipal.of("user:test-dispatcher", PrincipalType.HUMAN, "hospital-east");
        Pragma inputPragma = new Pragma("PRAGMA-DISP-001", "praxis-dispatch-test", PragmaStatus.REQUESTED);
        inputPragma.setOriginatingPrincipal(principal);
        inputPragma.addOriginatingAuthority(HarmoniaAuthorityEnum.PROVIDER_CHANGE_SUBMIT.toThemisAuthority());
        inputPragma.setOriginatingSecurityContext(ThemisSecurityContext.fromPrincipal(principal, "corr-disp-001"));
        inputPragma.addInput(ErgonPayload.fromJson(0,
                new Topic("Health", "HL7", "2.4", "ADT", "A01"), null, "{\"id\":\"123\"}"));

        boolean dispatched = dispatcher.dispatchPragma(inputPragma);
        assertThat(dispatched).isTrue();

        // Check workflow resolution
        Praxis resolved = (Praxis) dispatcher.resolveWorkflow(inputPragma);
        assertThat(resolved).isNotNull();
        assertThat(resolved.getPraxisId()).isEqualTo("praxis-dispatch-test");
    }
}
