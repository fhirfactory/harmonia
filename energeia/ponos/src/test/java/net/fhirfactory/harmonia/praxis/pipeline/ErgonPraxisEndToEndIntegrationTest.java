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

package net.fhirfactory.harmonia.praxis.pipeline;

import ca.uhn.fhir.context.FhirContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.fhirfactory.harmonia.erga.base.ErgonBase;
import net.fhirfactory.harmonia.erga.fhir.ExtractPatientFromBundle;
import net.fhirfactory.harmonia.erga.hl7v2x.Adt2FhirMapper;
import net.fhirfactory.harmonia.erga.patient.demographics.PatientDemographicsUpdateErgon;
import net.fhirfactory.harmonia.erga.patient.identity.PatientIdentityUpdateErgon;
import net.fhirfactory.harmonia.model.ergon.ErgonPayload;
import net.fhirfactory.harmonia.model.pragma.Pragma;
import net.fhirfactory.harmonia.model.pragma.PragmaCheckpoint;
import net.fhirfactory.harmonia.model.pragma.PragmaFhirConverter;
import net.fhirfactory.harmonia.model.pragma.PragmaStatus;
import net.fhirfactory.harmonia.model.security.HarmoniaAuthorityEnum;
import net.fhirfactory.harmonia.model.topic.Topic;
import net.fhirfactory.harmonia.petasos.api.Petasos;
import net.fhirfactory.harmonia.petasos.api.consumer.PetasosConsumer;
import net.fhirfactory.harmonia.petasos.api.consumer.PetasosMessageContext;
import net.fhirfactory.harmonia.petasos.api.consumer.PetasosMessageHandler;
import net.fhirfactory.harmonia.petasos.api.consumer.PetasosSubscription;
import net.fhirfactory.harmonia.petasos.api.destination.PetasosDestination;
import net.fhirfactory.harmonia.petasos.api.message.PetasosMessage;
import net.fhirfactory.harmonia.praxis.cache.PragmaCacheService;
import net.fhirfactory.harmonia.praxis.conduit.PetasosQueueToExchangeConduit;
import net.fhirfactory.harmonia.praxis.conduit.PragmaWorkflowDispatcher;
import net.fhirfactory.harmonia.praxis.sequence.Praxis;
import net.fhirfactory.harmonia.praxis.sequence.PraxisCheckpointManager;
import net.fhirfactory.harmonia.themis.api.model.PrincipalType;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;
import net.fhirfactory.harmonia.themis.core.identities.HarmoniaServiceIdentities;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.hl7.fhir.r5.model.Patient;
import org.hl7.fhir.r5.model.StringType;
import org.hl7.fhir.r5.model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ErgonPraxisEndToEndIntegrationTest {

    private CamelContext camelContext;
    private Petasos petasosMock;
    private PetasosSubscription petasosSubscriptionMock;
    private PetasosMessageHandler registeredHandler;
    private PragmaCacheService pragmaCacheService;
    private PraxisCheckpointManager checkpointManager;
    private PragmaWorkflowDispatcher workflowDispatcher;
    private FhirContext fhirContext;

    @BeforeEach
    void setUp() throws Exception {
        camelContext = new DefaultCamelContext();
        fhirContext = FhirContext.forR5();

        petasosMock = mock(Petasos.class);
        petasosSubscriptionMock = mock(PetasosSubscription.class);

        when(petasosMock.receive(any(PetasosDestination.class), any(PetasosMessageHandler.class))).thenAnswer(invocation -> {
            registeredHandler = invocation.getArgument(1);
            return petasosSubscriptionMock;
        });

        pragmaCacheService = new PragmaCacheService();
        pragmaCacheService.init();

        checkpointManager = new PraxisCheckpointManager(pragmaCacheService, null);
        workflowDispatcher = new PragmaWorkflowDispatcher(camelContext, null);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (camelContext != null) {
            camelContext.stop();
        }
    }

    @Test
    @DisplayName("End-to-end clinical workflow: Petasos queue -> Conduit -> Praxis (Adt2Fhir -> ExtractPatient -> Identity -> Demographics) -> Checkpointed in Mneme")
    void testEndToEndClinicalWorkflowPipeline() throws Exception {
        // 1. Compose 4-stage sequential clinical Praxis workflow
        Praxis praxis = new Praxis("praxis-adt-e2e-pipeline", "ADT End-to-End Clinical Processing");
        praxis.setPragmaCacheService(pragmaCacheService);
        praxis.setCheckpointManager(checkpointManager);

        Adt2FhirMapper ergon1 = new Adt2FhirMapper();
        ExtractPatientFromBundle ergon2 = new ExtractPatientFromBundle(fhirContext);
        PatientIdentityUpdateErgon ergon3 = new PatientIdentityUpdateErgon();
        PatientDemographicsUpdateErgon ergon4 = new PatientDemographicsUpdateErgon();

        praxis.addActivity(ergon1);
        praxis.addActivity(ergon2);
        praxis.addActivity(ergon3);
        praxis.addActivity(ergon4);

        praxis.configureChainedEndpoints();
        praxis.registerRoutes(camelContext);
        camelContext.addRoutes(praxis.createSequencePipelineRoute());
        camelContext.start();

        // 2. Register with dispatcher & configure Petasos ingress conduit
        workflowDispatcher.registerWorkflow(praxis);

        PetasosQueueToExchangeConduit conduit = new PetasosQueueToExchangeConduit(
                petasosMock, camelContext, "petasos.queue.adt.ingress", null);
        conduit.setWorkflowDispatcher(workflowDispatcher);
        conduit.start();

        // 3. Prepare clinical HL7 ADT message inside Pragma domain payload in Petasos envelope
        String hl7AdtMessage = "MSH|^~\\&|PAS|HOSPITAL_MAIN|HIE|REC|20260908120000||ADT^A01|MSG-CLINICAL-9001|P|2.4\r" +
                "EVN|A01|20260908120000\r" +
                "PID|||MRN-E2E-9001^^^HOSPITAL_MAIN||WILLIAMS^SARAH^ELIZABETH^^DR||19880425|F||2106-3^White|100 MAIN ST^^SYDNEY^NSW^2000^AUS||(02)9555-1234||ENG^English|M^Married\r" +
                "NK1|1|WILLIAMS^MARK||SPS^Spouse|100 MAIN ST^^SYDNEY^NSW^2000^AUS|(02)9555-5678\r" +
                "PV1||I|WARD-4B^BED-12^^HOSPITAL_MAIN||||1234^CURIE^MARIE^^^DR||||||||||ADM-9001";

        ThemisPrincipal principal = ThemisPrincipal.of("system:pylai-gateway", PrincipalType.SYSTEM, "clinical");
        Pragma inputPragma = Pragma.builder()
                .pragmaId("PETASOS-MSG-9001")
                .praxisId("praxis-adt-e2e-pipeline")
                .correlationId("CORR-CLINICAL-9001")
                .causationId("CAUSE-PAS-EVENT-1")
                .source("pylai-mllp-gateway")
                .status(PragmaStatus.REQUESTED)
                .originatingPrincipal(principal)
                .addOriginatingAuthority(HarmoniaAuthorityEnum.PROVIDER_CHANGE_SUBMIT.toThemisAuthority())
                .originatingSecurityContext(ThemisSecurityContext.fromPrincipal(principal, "CORR-CLINICAL-9001"))
                .policyVersion("1.0.0")
                .build();
        inputPragma.addInput(ErgonPayload.fromJson(0,
                new Topic("Health", "HL7", "2.4", "ADT", "A01"),
                new Topic("Health", "Clinical", "1.0", "PatientId", null),
                hl7AdtMessage));

        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        String pragmaPayload = mapper.writeValueAsString(inputPragma);

        PetasosMessage petasosMessage = PetasosMessage.builder()
                .messageId("PETASOS-MSG-9001")
                .correlationId("CORR-CLINICAL-9001")
                .causationId("CAUSE-PAS-EVENT-1")
                .messageType("ADT^A01")
                .contentType("application/json")
                .source("pylai-mllp-gateway")
                .destination(PetasosDestination.queue("petasos.queue.adt.ingress"))
                .priority(8)
                .payload(pragmaPayload)
                .metadata(java.util.Map.of("facility", "Hospital-Main", "tenant", "nsw-health"))
                .build();

        PetasosMessageContext contextMock = mock(PetasosMessageContext.class);

        // 4. Ingress message from Petasos queue
        registeredHandler.onMessage(petasosMessage, contextMock);

        // 5. Verify Petasos message was acknowledged upon successful pipeline completion
        verify(contextMock, times(1)).acknowledge();
        verify(contextMock, never()).reject();

        // 6. Inspect Pragma in Mneme cache
        Optional<Pragma> cachedPragmaOpt = pragmaCacheService.getPragma("PETASOS-MSG-9001");
        assertThat(cachedPragmaOpt).isPresent();
        Pragma pragma = cachedPragmaOpt.get();

        assertThat(pragma.getStatus()).isEqualTo(PragmaStatus.COMPLETED);
        assertThat(pragma.getCorrelationId()).isEqualTo("CORR-CLINICAL-9001");
        assertThat(pragma.getCausationId()).isEqualTo("CAUSE-PAS-EVENT-1");
        assertThat(pragma.getSource()).isEqualTo("pylai-mllp-gateway");
        assertThat(pragma.getMetadata()).containsEntry("facility", "Hospital-Main");

        // Verify originating requester identity is preserved and executing process reflects Ponos
        assertThat(pragma.getOriginatingPrincipal()).isEqualTo(principal);
        assertThat(pragma.getExecutingPrincipal()).isEqualTo(HarmoniaServiceIdentities.PRINCIPAL_PONOS_PROCESS);
        assertThat(pragma.getOriginatingSecurityContext()).isNotNull();
        assertThat(pragma.getOriginatingSecurityContext().originatingPrincipal()).isEqualTo(principal);
        assertThat(pragma.getOriginatingSecurityContext().executingPrincipal()).isEqualTo(HarmoniaServiceIdentities.PRINCIPAL_PONOS_PROCESS);
        assertThat(pragma.getOriginatingSecurityContext().correlationId()).isEqualTo("CORR-CLINICAL-9001");
        assertThat(pragma.getOriginatingSecurityContext().causationId()).isEqualTo("CAUSE-PAS-EVENT-1");

        // Verify accumulated outputs across all 4 Ergon stages
        assertThat(pragma.getOutput()).isNotEmpty();

        // Checkpoints audit trail verification
        List<PragmaCheckpoint> checkpoints = pragma.getCheckpoints();
        assertThat(checkpoints).isNotEmpty();
        assertThat(checkpoints.get(0).getStageName()).isEqualTo(PraxisCheckpointManager.STAGE_PIPELINE_INGRESS);
        assertThat(checkpoints.get(checkpoints.size() - 1).getStageName()).isEqualTo(PraxisCheckpointManager.STAGE_PIPELINE_COMPLETE);
        assertThat(checkpoints.get(checkpoints.size() - 1).getStatus()).isEqualTo(PragmaStatus.COMPLETED);

        conduit.stop();
    }

    @Test
    @DisplayName("End-to-end security & child task lineage: Human requester provenance preserved, executor is Ponos, child tasks inherit security extensions with parent causation")
    void testEndToEndWorkflowSecurityPropagationHumanRequester() throws Exception {
        // 1. Compose clinical pipeline
        Praxis praxis = new Praxis("praxis-human-security-e2e", "Human Requester Security Pipeline");
        praxis.setPragmaCacheService(pragmaCacheService);
        praxis.setCheckpointManager(checkpointManager);

        Adt2FhirMapper ergon1 = new Adt2FhirMapper();
        ExtractPatientFromBundle ergon2 = new ExtractPatientFromBundle(fhirContext);
        PatientIdentityUpdateErgon ergon3 = new PatientIdentityUpdateErgon();

        praxis.addActivity(ergon1);
        praxis.addActivity(ergon2);
        praxis.addActivity(ergon3);

        praxis.configureChainedEndpoints();
        praxis.registerRoutes(camelContext);
        camelContext.addRoutes(praxis.createSequencePipelineRoute());
        camelContext.start();

        workflowDispatcher.registerWorkflow(praxis);

        PetasosQueueToExchangeConduit conduit = new PetasosQueueToExchangeConduit(
                petasosMock, camelContext, "petasos.queue.human.security.ingress", null);
        conduit.setWorkflowDispatcher(workflowDispatcher);
        conduit.start();

        // 2. Prepare human requester with originating credentials and correlation
        ThemisPrincipal humanPrincipal = ThemisPrincipal.of("user:dr-smith", PrincipalType.HUMAN, "hospital-east");
        ThemisSecurityContext secContext = ThemisSecurityContext.builder()
                .requestingPrincipal(humanPrincipal)
                .authorities(java.util.Set.of(
                        HarmoniaAuthorityEnum.PROVIDER_CHANGE_SUBMIT.toThemisAuthority(),
                        HarmoniaAuthorityEnum.PROVIDER_READ.toThemisAuthority()))
                .correlationId("CORR-HUMAN-7001")
                .causationId("PAS-EVENT-7001")
                .securityDomain("PROVIDER_REGISTRY")
                .build();

        String hl7AdtMessage = "MSH|^~\\&|PAS|HOSPITAL_MAIN|HIE|REC|20260908120000||ADT^A01|MSG-HUMAN-7001|P|2.4\r" +
                "EVN|A01|20260908120000\r" +
                "PID|||MRN-HUMAN-7001^^^HOSPITAL_MAIN||SMITH^JOHN^^^DR||19750512|M||2106-3^White|200 GEORGE ST^^SYDNEY^NSW^2000^AUS\r" +
                "PV1||I|WARD-2A^BED-01^^HOSPITAL_MAIN||||1234^CURIE^MARIE^^^DR||||||||||ADM-7001";

        Pragma inputPragma = Pragma.builder()
                .pragmaId("PRAGMA-HUMAN-7001")
                .praxisId("praxis-human-security-e2e")
                .correlationId("CORR-HUMAN-7001")
                .causationId("PAS-EVENT-7001")
                .source("iris-befe-gateway")
                .status(PragmaStatus.REQUESTED)
                .originatingPrincipal(humanPrincipal)
                .addOriginatingAuthority(HarmoniaAuthorityEnum.PROVIDER_CHANGE_SUBMIT.toThemisAuthority())
                .addOriginatingAuthority(HarmoniaAuthorityEnum.PROVIDER_READ.toThemisAuthority())
                .originatingSecurityContext(secContext)
                .policyVersion("1.0.0")
                .build();
        inputPragma.addInput(ErgonPayload.fromJson(0,
                new Topic("Health", "HL7", "2.4", "ADT", "A01"),
                new Topic("Health", "Clinical", "1.0", "PatientId", null),
                hl7AdtMessage));

        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        String pragmaPayload = mapper.writeValueAsString(inputPragma);

        PetasosMessage petasosMessage = PetasosMessage.builder()
                .messageId("PRAGMA-HUMAN-7001")
                .correlationId("CORR-HUMAN-7001")
                .causationId("PAS-EVENT-7001")
                .messageType("ADT^A01")
                .contentType("application/json")
                .source("iris-befe-gateway")
                .destination(PetasosDestination.queue("petasos.queue.human.security.ingress"))
                .payload(pragmaPayload)
                .build();

        PetasosMessageContext contextMock = mock(PetasosMessageContext.class);

        // 3. Dispatch through conduit and pipeline
        registeredHandler.onMessage(petasosMessage, contextMock);

        // 4. Verify message acknowledged
        verify(contextMock, times(1)).acknowledge();
        verify(contextMock, never()).reject();

        // 5. Inspect cached Pragma
        Optional<Pragma> cachedOpt = pragmaCacheService.getPragma("PRAGMA-HUMAN-7001");
        assertThat(cachedOpt).isPresent();
        Pragma completedPragma = cachedOpt.get();

        assertThat(completedPragma.getStatus()).isEqualTo(PragmaStatus.COMPLETED);
        assertThat(completedPragma.getOriginatingPrincipal()).isEqualTo(humanPrincipal);
        assertThat(completedPragma.getExecutingPrincipal()).isEqualTo(HarmoniaServiceIdentities.PRINCIPAL_PONOS_PROCESS);
        assertThat(completedPragma.getCorrelationId()).isEqualTo("CORR-HUMAN-7001");
        assertThat(completedPragma.getCausationId()).isEqualTo("PAS-EVENT-7001");

        // Verify security context preservation
        ThemisSecurityContext finalContext = completedPragma.getOriginatingSecurityContext();
        assertThat(finalContext).isNotNull();
        assertThat(finalContext.originatingPrincipal()).isEqualTo(humanPrincipal);
        assertThat(finalContext.executingPrincipal()).isEqualTo(HarmoniaServiceIdentities.PRINCIPAL_PONOS_PROCESS);
        assertThat(finalContext.correlationId()).isEqualTo("CORR-HUMAN-7001");
        assertThat(finalContext.causationId()).isEqualTo("PAS-EVENT-7001");
        assertThat(finalContext.authorities()).contains(
                HarmoniaAuthorityEnum.PROVIDER_CHANGE_SUBMIT.toThemisAuthority(),
                HarmoniaAuthorityEnum.PROVIDER_READ.toThemisAuthority());

        // 6. Verify child tasks generated at egress carry originating security extensions and parent causation
        Task processedTask = PragmaFhirConverter.toFhirTask(completedPragma);
        List<Task> childTasks = ergon3.createOutgoingTasks(processedTask, completedPragma);
        assertThat(childTasks).isNotEmpty();

        for (int i = 0; i < childTasks.size(); i++) {
            Task child = childTasks.get(i);
            assertThat(child.getIdPart()).isEqualTo("PRAGMA-HUMAN-7001-out-" + (i + 1));

            // Lineage identifiers
            assertThat(child.getIdentifier()).anyMatch(id ->
                    PragmaFhirConverter.IDENTIFIER_SYSTEM_CORRELATION_ID.equals(id.getSystem())
                            && "CORR-HUMAN-7001".equals(id.getValue()));
            assertThat(child.getIdentifier()).anyMatch(id ->
                    PragmaFhirConverter.IDENTIFIER_SYSTEM_CAUSATION_ID.equals(id.getSystem())
                            && "PRAGMA-HUMAN-7001".equals(id.getValue()));

            // Parent reference
            assertThat(child.getPartOfFirstRep().getReference()).isEqualTo("Task/PRAGMA-HUMAN-7001");

            // Originating Security Extensions
            assertThat(child.getExtensionByUrl(PragmaFhirConverter.EXTENSION_SECURITY_PRINCIPAL_ID).getValue().toString())
                    .isEqualTo("user:dr-smith");
            assertThat(child.getExtensionByUrl(PragmaFhirConverter.EXTENSION_SECURITY_PRINCIPAL_TYPE).getValue().toString())
                    .isEqualTo("HUMAN");
            assertThat(child.getExtensionByUrl(PragmaFhirConverter.EXTENSION_SECURITY_SOURCE_DOMAIN).getValue().toString())
                    .isEqualTo("hospital-east");

            // Executing Process Extensions
            assertThat(child.getExtensionByUrl(PragmaFhirConverter.EXTENSION_SECURITY_EXECUTING_PRINCIPAL_ID).getValue().toString())
                    .isEqualTo(HarmoniaServiceIdentities.ID_PONOS_PROCESS);
            assertThat(child.getExtensionByUrl(PragmaFhirConverter.EXTENSION_SECURITY_EXECUTING_PRINCIPAL_TYPE).getValue().toString())
                    .isEqualTo("PROCESS");

            // Authority Extensions
            List<String> authorityValues = child.getExtensionsByUrl(PragmaFhirConverter.EXTENSION_SECURITY_AUTHORITY).stream()
                    .map(e -> ((StringType) e.getValue()).getValue())
                    .toList();
            assertThat(authorityValues).contains("provider.change.submit", "provider.read");
        }

        // 7. Verify zero-PHI in audit checkpoints
        for (PragmaCheckpoint cp : completedPragma.getCheckpoints()) {
            assertThat(cp.getStageName()).doesNotContain("SMITH", "JOHN", "19750512");
            if (cp.getStatusMessage() != null) {
                assertThat(cp.getStatusMessage()).doesNotContain("SMITH", "JOHN", "19750512");
            }
        }

        conduit.stop();
    }

    @Test
    @DisplayName("End-to-end dual-principal denial: Rejects ingress when originating principal lacks submit privilege")
    void testEndToEndDualPrincipalDenialWhenOriginatingRequesterUnauthorized() throws Exception {
        Praxis praxis = new Praxis("praxis-unauth-e2e", "Unauthorized Requester Pipeline");
        praxis.setPragmaCacheService(pragmaCacheService);
        praxis.setCheckpointManager(checkpointManager);

        Adt2FhirMapper ergon1 = new Adt2FhirMapper();
        praxis.addActivity(ergon1);

        praxis.configureChainedEndpoints();
        praxis.registerRoutes(camelContext);
        camelContext.addRoutes(praxis.createSequencePipelineRoute());
        camelContext.start();

        workflowDispatcher.registerWorkflow(praxis);

        PetasosQueueToExchangeConduit conduit = new PetasosQueueToExchangeConduit(
                petasosMock, camelContext, "petasos.queue.unauth.ingress", null);
        conduit.setWorkflowDispatcher(workflowDispatcher);
        conduit.start();

        // Originating principal only has read authority (missing submit authority!)
        ThemisPrincipal unauthorizedUser = ThemisPrincipal.of("user:unauthorized-nurse", PrincipalType.HUMAN, "hospital-east");
        ThemisSecurityContext secContext = ThemisSecurityContext.builder()
                .requestingPrincipal(unauthorizedUser)
                .authorities(java.util.Set.of(HarmoniaAuthorityEnum.PROVIDER_READ.toThemisAuthority()))
                .correlationId("CORR-UNAUTH-001")
                .causationId("CAUSE-PAS-UNAUTH")
                .securityDomain("PROVIDER_REGISTRY")
                .build();

        Pragma inputPragma = Pragma.builder()
                .pragmaId("PRAGMA-UNAUTH-001")
                .praxisId("praxis-unauth-e2e")
                .correlationId("CORR-UNAUTH-001")
                .causationId("CAUSE-PAS-UNAUTH")
                .status(PragmaStatus.REQUESTED)
                .originatingPrincipal(unauthorizedUser)
                .addOriginatingAuthority(HarmoniaAuthorityEnum.PROVIDER_READ.toThemisAuthority())
                .originatingSecurityContext(secContext)
                .policyVersion("1.0.0")
                .build();
        inputPragma.addInput(ErgonPayload.fromJson(0,
                new Topic("Health", "HL7", "2.4", "ADT", "A01"),
                new Topic("Health", "Clinical", "1.0", "PatientId", null),
                "MSH|^~\\&|PAS|HOSP|HIE|REC|20260908120000||ADT^A01|MSG-001|P|2.4\rPID|||MRN-001^^^HOSP||DOE^J||19900101|M"));

        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        String pragmaPayload = mapper.writeValueAsString(inputPragma);

        PetasosMessage petasosMessage = PetasosMessage.builder()
                .messageId("PRAGMA-UNAUTH-001")
                .correlationId("CORR-UNAUTH-001")
                .causationId("CAUSE-PAS-UNAUTH")
                .messageType("ADT^A01")
                .contentType("application/json")
                .source("pylai-mllp-gateway")
                .destination(PetasosDestination.queue("petasos.queue.unauth.ingress"))
                .payload(pragmaPayload)
                .build();

        PetasosMessageContext contextMock = mock(PetasosMessageContext.class);

        registeredHandler.onMessage(petasosMessage, contextMock);

        // Verify message was rejected due to dual-authority gate denial
        verify(contextMock, times(1)).reject();
        verify(contextMock, never()).acknowledge();

        // Cached/dispatched pragma should reflect FAILED status with security gate checkpoint
        Optional<Pragma> cachedOpt = pragmaCacheService.getPragma("PRAGMA-UNAUTH-001");
        if (cachedOpt.isPresent()) {
            Pragma cachedPragma = cachedOpt.get();
            assertThat(cachedPragma.getStatus()).isEqualTo(PragmaStatus.FAILED);
            assertThat(cachedPragma.getCheckpoints())
                    .anyMatch(cp -> cp.getStageName().equals("THEMIS_EXECUTION_GATE")
                            && cp.getStatusMessage().contains("AUTHORITY_MISSING"));
        }

        conduit.stop();
    }

    @Test
    @DisplayName("End-to-end service-originated workflow: service:pylai executes cleanly with Ponos process executor")
    void testEndToEndServiceOriginatedWorkflow() throws Exception {
        Praxis praxis = new Praxis("praxis-service-e2e", "Service-Originated Pipeline");
        praxis.setPragmaCacheService(pragmaCacheService);
        praxis.setCheckpointManager(checkpointManager);

        Adt2FhirMapper ergon1 = new Adt2FhirMapper();
        praxis.addActivity(ergon1);

        praxis.configureChainedEndpoints();
        praxis.registerRoutes(camelContext);
        camelContext.addRoutes(praxis.createSequencePipelineRoute());
        camelContext.start();

        workflowDispatcher.registerWorkflow(praxis);

        PetasosQueueToExchangeConduit conduit = new PetasosQueueToExchangeConduit(
                petasosMock, camelContext, "petasos.queue.service.ingress", null);
        conduit.setWorkflowDispatcher(workflowDispatcher);
        conduit.start();

        ThemisPrincipal servicePrincipal = HarmoniaServiceIdentities.PRINCIPAL_PYLAI;
        ThemisSecurityContext serviceContext = ThemisSecurityContext.builder()
                .requestingPrincipal(servicePrincipal)
                .authorities(java.util.Set.of(
                        HarmoniaAuthorityEnum.PROVIDER_CHANGE_SUBMIT.toThemisAuthority(),
                        HarmoniaAuthorityEnum.SYSTEM_INTEGRATION.toThemisAuthority()))
                .correlationId("CORR-SERVICE-8001")
                .causationId("MSG-SERVICE-8001")
                .securityDomain("PROVIDER_REGISTRY")
                .build();

        Pragma inputPragma = Pragma.builder()
                .pragmaId("PRAGMA-SERVICE-8001")
                .praxisId("praxis-service-e2e")
                .correlationId("CORR-SERVICE-8001")
                .causationId("MSG-SERVICE-8001")
                .source("service:pylai")
                .status(PragmaStatus.REQUESTED)
                .originatingPrincipal(servicePrincipal)
                .addOriginatingAuthority(HarmoniaAuthorityEnum.PROVIDER_CHANGE_SUBMIT.toThemisAuthority())
                .addOriginatingAuthority(HarmoniaAuthorityEnum.SYSTEM_INTEGRATION.toThemisAuthority())
                .originatingSecurityContext(serviceContext)
                .policyVersion("1.0.0")
                .build();
        inputPragma.addInput(ErgonPayload.fromJson(0,
                new Topic("Health", "HL7", "2.4", "ADT", "A01"),
                new Topic("Health", "Clinical", "1.0", "PatientId", null),
                "MSH|^~\\&|PAS|HOSP|HIE|REC|20260908120000||ADT^A01|MSG-001|P|2.4\rPID|||MRN-SVC-001^^^HOSP||TEST^P||19950101|F"));

        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        String pragmaPayload = mapper.writeValueAsString(inputPragma);

        PetasosMessage petasosMessage = PetasosMessage.builder()
                .messageId("PRAGMA-SERVICE-8001")
                .correlationId("CORR-SERVICE-8001")
                .causationId("MSG-SERVICE-8001")
                .messageType("ADT^A01")
                .contentType("application/json")
                .source("service:pylai")
                .destination(PetasosDestination.queue("petasos.queue.service.ingress"))
                .payload(pragmaPayload)
                .build();

        PetasosMessageContext contextMock = mock(PetasosMessageContext.class);

        registeredHandler.onMessage(petasosMessage, contextMock);

        verify(contextMock, times(1)).acknowledge();
        verify(contextMock, never()).reject();

        Optional<Pragma> cachedOpt = pragmaCacheService.getPragma("PRAGMA-SERVICE-8001");
        assertThat(cachedOpt).isPresent();
        Pragma completed = cachedOpt.get();

        assertThat(completed.getStatus()).isEqualTo(PragmaStatus.COMPLETED);
        assertThat(completed.getOriginatingPrincipal()).isEqualTo(servicePrincipal);
        assertThat(completed.getExecutingPrincipal()).isEqualTo(HarmoniaServiceIdentities.PRINCIPAL_PONOS_PROCESS);
        assertThat(completed.getCorrelationId()).isEqualTo("CORR-SERVICE-8001");
        assertThat(completed.getCausationId()).isEqualTo("MSG-SERVICE-8001");

        conduit.stop();
    }

    @Test
    @DisplayName("End-to-end missing context: Fails closed when message lacks originating principal and security context")
    void testEndToEndMissingContextFailsClosed() throws Exception {
        Praxis praxis = new Praxis("praxis-missing-ctx-e2e", "Missing Context Pipeline");
        praxis.setPragmaCacheService(pragmaCacheService);
        praxis.setCheckpointManager(checkpointManager);

        Adt2FhirMapper ergon1 = new Adt2FhirMapper();
        praxis.addActivity(ergon1);

        praxis.configureChainedEndpoints();
        praxis.registerRoutes(camelContext);
        camelContext.addRoutes(praxis.createSequencePipelineRoute());
        camelContext.start();

        workflowDispatcher.registerWorkflow(praxis);

        PetasosQueueToExchangeConduit conduit = new PetasosQueueToExchangeConduit(
                petasosMock, camelContext, "petasos.queue.missing.ctx.ingress", null);
        conduit.setWorkflowDispatcher(workflowDispatcher);
        conduit.start();

        // Pragma lacking principal and security context
        Pragma inputPragma = Pragma.builder()
                .pragmaId("PRAGMA-MISSING-CTX")
                .praxisId("praxis-missing-ctx-e2e")
                .correlationId("CORR-MISSING-001")
                .status(PragmaStatus.REQUESTED)
                .source("service:unauthenticated-gateway")
                .build();
        inputPragma.addInput(ErgonPayload.fromJson(0,
                new Topic("Health", "HL7", "2.4", "ADT", "A01"),
                new Topic("Health", "Clinical", "1.0", "PatientId", null),
                "MSH|^~\\&|PAS|HOSP|HIE|REC|20260908120000||ADT^A01|MSG-001|P|2.4\rPID|||MRN-ANON-001^^^HOSP||ANON^U||20000101|U"));

        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        String pragmaPayload = mapper.writeValueAsString(inputPragma);

        PetasosMessage petasosMessage = PetasosMessage.builder()
                .messageId("PRAGMA-MISSING-CTX")
                .correlationId("CORR-MISSING-001")
                .messageType("ADT^A01")
                .contentType("application/json")
                .source("service:unauthenticated-gateway")
                .destination(PetasosDestination.queue("petasos.queue.missing.ctx.ingress"))
                .payload(pragmaPayload)
                .build();

        PetasosMessageContext contextMock = mock(PetasosMessageContext.class);

        registeredHandler.onMessage(petasosMessage, contextMock);

        verify(contextMock, times(1)).reject();
        verify(contextMock, never()).acknowledge();

        conduit.stop();
    }

    @Test
    @DisplayName("Resilience & Recovery: Resumes workflow progression from checkpointed Pragma state")
    void testCheckpointResilienceAndStateRecovery() throws Exception {
        // 1. Stage an in-flight Pragma at checkpoint Step 2 (Extract complete)
        ThemisPrincipal recPrincipal = ThemisPrincipal.of("system:recovery-service", PrincipalType.SYSTEM, "clinical");
        Pragma stagedPragma = new Pragma("PRAGMA-RECOVERY-001", "praxis-recovery-pipeline", PragmaStatus.IN_PROGRESS);
        stagedPragma.setCorrelationId("CORR-REC-100");
        stagedPragma.setOriginatingPrincipal(recPrincipal);
        stagedPragma.addOriginatingAuthority(HarmoniaAuthorityEnum.PROVIDER_CHANGE_SUBMIT.toThemisAuthority());
        stagedPragma.setOriginatingSecurityContext(ThemisSecurityContext.fromPrincipal(recPrincipal, "CORR-REC-100"));

        Topic hl7Container = new Topic("Health", "HL7", "2.4", "ADT", "A01");
        Topic adtContent = new Topic("Health", "Clinical", "1.0", "PatientId", null);
        stagedPragma.addInput(ErgonPayload.fromJson(0, hl7Container, adtContent,
                "MSH|^~\\&|PAS|HOSP|HIE|REC|20260908120000||ADT^A01|MSG-001|P|2.4\rPID|||MRN-REC-100^^^HOSP||DOE^JOHN||19800101|M"));

        // Add pre-existing outputs from step 1 & 2
        Patient patient = new Patient();
        patient.setId("Patient/MRN-REC-100");
        patient.addName().setFamily("DOE").addGiven("JOHN");
        Topic fhirContainer = new Topic("Health", "FHIR", "R5", "Patient", null);
        Topic fhirContent = new Topic("Health", "Clinical", "1.0", "Patient", "Demographics");
        stagedPragma.addOutput(ErgonPayload.fromFhirResource(0, fhirContainer, fhirContent, patient));

        // Save staged state into cache
        pragmaCacheService.savePragma(stagedPragma);
        checkpointManager.recordPostErgon(stagedPragma, "praxis-recovery-pipeline", "ergon-extract", 1, 1);

        // 2. Set up recovery pipeline executing remaining steps 3 & 4 (Identity & Demographics)
        Praxis recoveryPraxis = new Praxis("praxis-recovery-pipeline", "Recovery Workflow Pipeline");
        recoveryPraxis.setPragmaCacheService(pragmaCacheService);
        recoveryPraxis.setCheckpointManager(checkpointManager);

        PatientIdentityUpdateErgon step3 = new PatientIdentityUpdateErgon();
        PatientDemographicsUpdateErgon step4 = new PatientDemographicsUpdateErgon();

        recoveryPraxis.addActivity(step3);
        recoveryPraxis.addActivity(step4);

        recoveryPraxis.configureChainedEndpoints();
        recoveryPraxis.registerRoutes(camelContext);
        camelContext.addRoutes(recoveryPraxis.createSequencePipelineRoute());
        camelContext.start();

        workflowDispatcher.registerWorkflow(recoveryPraxis);

        // 3. Dispatch resumed Pragma from its latest checkpoint
        boolean success = workflowDispatcher.dispatchPragma(stagedPragma);
        assertThat(success).isTrue();

        // 4. Verify recovered Pragma reaches COMPLETED in cache with updated audit checkpoints
        Optional<Pragma> recoveredPragmaOpt = pragmaCacheService.getPragma("PRAGMA-RECOVERY-001");
        assertThat(recoveredPragmaOpt).isPresent();
        Pragma recoveredPragma = recoveredPragmaOpt.get();

        assertThat(recoveredPragma.getStatus()).isEqualTo(PragmaStatus.COMPLETED);
        assertThat(recoveredPragma.getCheckpoints()).isNotEmpty();
        assertThat(recoveredPragma.getLatestCheckpoint().get().getStageName()).isEqualTo(PraxisCheckpointManager.STAGE_PIPELINE_COMPLETE);
    }
}
