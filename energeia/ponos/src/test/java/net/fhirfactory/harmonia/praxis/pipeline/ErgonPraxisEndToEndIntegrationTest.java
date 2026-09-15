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
import net.fhirfactory.harmonia.erga.fhir.ExtractPatientFromBundle;
import net.fhirfactory.harmonia.erga.hl7v2x.Adt2FhirMapper;
import net.fhirfactory.harmonia.erga.patient.demographics.PatientDemographicsUpdateErgon;
import net.fhirfactory.harmonia.erga.patient.identity.PatientIdentityUpdateErgon;
import net.fhirfactory.harmonia.model.ergon.ErgonPayload;
import net.fhirfactory.harmonia.model.pragma.Pragma;
import net.fhirfactory.harmonia.model.pragma.PragmaCheckpoint;
import net.fhirfactory.harmonia.model.pragma.PragmaStatus;
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
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.hl7.fhir.r5.model.Patient;
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

        // 3. Prepare clinical HL7 ADT message inside Petasos envelope
        String hl7AdtMessage = "MSH|^~\\&|PAS|HOSPITAL_MAIN|HIE|REC|20260908120000||ADT^A01|MSG-CLINICAL-9001|P|2.4\r" +
                "EVN|A01|20260908120000\r" +
                "PID|||MRN-E2E-9001^^^HOSPITAL_MAIN||WILLIAMS^SARAH^ELIZABETH^^DR||19880425|F||2106-3^White|100 MAIN ST^^SYDNEY^NSW^2000^AUS||(02)9555-1234||ENG^English|M^Married\r" +
                "NK1|1|WILLIAMS^MARK||SPS^Spouse|100 MAIN ST^^SYDNEY^NSW^2000^AUS|(02)9555-5678\r" +
                "PV1||I|WARD-4B^BED-12^^HOSPITAL_MAIN||||1234^CURIE^MARIE^^^DR||||||||||ADM-9001";

        PetasosMessage petasosMessage = PetasosMessage.builder()
                .messageId("PETASOS-MSG-9001")
                .correlationId("CORR-CLINICAL-9001")
                .causationId("CAUSE-PAS-EVENT-1")
                .messageType("ADT^A01")
                .contentType("text/plain")
                .source("pylai-mllp-gateway")
                .destination(PetasosDestination.queue("petasos.queue.adt.ingress"))
                .priority(8)
                .payload(hl7AdtMessage)
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
    @DisplayName("Resilience & Recovery: Resumes workflow progression from checkpointed Pragma state")
    void testCheckpointResilienceAndStateRecovery() throws Exception {
        // 1. Stage an in-flight Pragma at checkpoint Step 2 (Extract complete)
        Pragma stagedPragma = new Pragma("PRAGMA-RECOVERY-001", "praxis-recovery-pipeline", PragmaStatus.IN_PROGRESS);
        stagedPragma.setCorrelationId("CORR-REC-100");

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
