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

import net.fhirfactory.harmonia.erga.base.ErgonBase;
import net.fhirfactory.harmonia.erga.base.ErgonException;
import net.fhirfactory.harmonia.model.ergon.ErgonPayload;
import net.fhirfactory.harmonia.model.pragma.Pragma;
import net.fhirfactory.harmonia.model.pragma.PragmaCheckpoint;
import net.fhirfactory.harmonia.model.pragma.PragmaStatus;
import net.fhirfactory.harmonia.model.topic.Topic;
import net.fhirfactory.harmonia.praxis.cache.PragmaCacheService;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultCamelContext;
import org.hl7.fhir.r5.model.Patient;
import org.hl7.fhir.r5.model.Reference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PraxisWorkflowTest {

    private CamelContext camelContext;
    private ProducerTemplate producerTemplate;
    private PragmaCacheService pragmaCacheService;
    private PraxisCheckpointManager checkpointManager;

    @BeforeEach
    void setUp() {
        camelContext = new DefaultCamelContext();
        producerTemplate = camelContext.createProducerTemplate();
        pragmaCacheService = new PragmaCacheService();
        pragmaCacheService.init();
        checkpointManager = new PraxisCheckpointManager(pragmaCacheService, null);
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

    // Step 1 Ergon: Extract HL7 Message
    static class ExtractHl7Ergon extends ErgonBase {
        public ExtractHl7Ergon() {
            super("ergon-extract", "Extract HL7 Message");
        }

        @Override
        protected void processErgon(Pragma pragma, Exchange exchange) throws Exception {
            Topic container = new Topic("Health", "HL7", "2.4", "ADT", "A01");
            Topic content = new Topic("Health", "Clinical", "1.0", "PatientId", null);
            pragma.addOutput(ErgonPayload.fromJson(0, container, content, "{\"extractedMrn\":\"MRN-PRAXIS-99\"}"));
        }
    }

    // Step 2 Ergon: Transform to FHIR Patient
    static class TransformPatientErgon extends ErgonBase {
        public TransformPatientErgon() {
            super("ergon-transform", "Transform Patient");
        }

        @Override
        protected void processErgon(Pragma pragma, Exchange exchange) throws Exception {
            Patient patient = new Patient();
            patient.setId("Patient/PAT-PRAXIS-99");
            patient.addName().setFamily("Smith").addGiven("Alice");

            Topic container = new Topic("Health", "FHIR", "R5", "Patient", null);
            Topic content = new Topic("Health", "Clinical", "1.0", "Patient", "Demographics");
            pragma.addOutput(ErgonPayload.fromFhirResource(1, container, content, patient));
        }
    }

    // Step 3 Ergon: Enrich and Load
    static class EnrichPatientErgon extends ErgonBase {
        public EnrichPatientErgon() {
            super("ergon-enrich", "Enrich Patient");
        }

        @Override
        protected void processErgon(Pragma pragma, Exchange exchange) throws Exception {
            Topic container = new Topic("Health", "FHIR", "R5", "Observation", null);
            Topic content = new Topic("Health", "Clinical", "1.0", "Observation", "Vitals");
            pragma.addOutput(ErgonPayload.fromFhirResource(2, container, content,
                    new Reference("Observation/OBS-99").setDisplay("Heart Rate 72 bpm")));
        }
    }

    // Failing Ergon
    static class FailingErgon extends ErgonBase {
        public FailingErgon() {
            super("ergon-failing", "Failing Ergon");
        }

        @Override
        protected void processErgon(Pragma pragma, Exchange exchange) throws Exception {
            throw new ErgonException("ergon-failing", pragma != null ? pragma.getPragmaId() : "unknown",
                    "Simulated database outage during workflow execution");
        }
    }

    @Test
    @DisplayName("Praxis sequential Ergon workflow execution and output accumulation")
    void testSequentialPraxisWorkflowExecution() throws Exception {
        Praxis praxis = new Praxis("praxis-patient-pipeline", "Patient Ingress Pipeline");
        praxis.setPragmaCacheService(pragmaCacheService);
        praxis.setCheckpointManager(checkpointManager);

        ExtractHl7Ergon step1 = new ExtractHl7Ergon();
        TransformPatientErgon step2 = new TransformPatientErgon();
        EnrichPatientErgon step3 = new EnrichPatientErgon();

        praxis.addActivity(step1);
        praxis.addActivity(step2);
        praxis.addActivity(step3);

        praxis.configureChainedEndpoints();
        praxis.registerRoutes(camelContext);
        camelContext.addRoutes(praxis.createSequencePipelineRoute());
        camelContext.start();

        // Create initial Ingress Pragma
        Pragma inputPragma = new Pragma("PRAGMA-SEQ-001", "praxis-patient-pipeline", PragmaStatus.REQUESTED);
        inputPragma.addInput(ErgonPayload.fromJson(0,
                new Topic("Health", "HL7", "2.4", "RAW", "INGRESS"),
                null,
                "MSH|^~\\&|PAS|HOSP|HIE|REC|20260908120000||ADT^A01|MSG-001|P|2.4"));
        pragmaCacheService.savePragma(inputPragma);

        // Execute pipeline
        Exchange resultExchange = producerTemplate.request(praxis.getPipelineInputEndpoint(), exchange -> {
            exchange.getMessage().setBody(inputPragma);
        });

        assertThat(resultExchange).isNotNull();

        // Check Pragma in Cache
        Optional<Pragma> finalPragmaOpt = pragmaCacheService.getPragma("PRAGMA-SEQ-001");
        assertThat(finalPragmaOpt).isPresent();
        Pragma finalPragma = finalPragmaOpt.get();

        assertThat(finalPragma.getStatus()).isEqualTo(PragmaStatus.COMPLETED);
        assertThat(finalPragma.getOutput()).hasSize(3);

        // Verify Checkpoints
        List<PragmaCheckpoint> checkpoints = finalPragma.getCheckpoints();
        assertThat(checkpoints).isNotEmpty();
        assertThat(checkpoints.get(0).getStageName()).isEqualTo(PraxisCheckpointManager.STAGE_PIPELINE_INGRESS);
        assertThat(checkpoints.get(checkpoints.size() - 1).getStageName()).isEqualTo(PraxisCheckpointManager.STAGE_PIPELINE_COMPLETE);
        assertThat(checkpoints.get(checkpoints.size() - 1).getStatus()).isEqualTo(PragmaStatus.COMPLETED);
    }

    @Test
    @DisplayName("Praxis checkpoint recording at every transition boundary")
    void testPraxisCheckpointRecording() {
        Pragma pragma = new Pragma("PRAGMA-CP-TEST", "praxis-audit", PragmaStatus.REQUESTED);
        pragmaCacheService.savePragma(pragma);

        // Ingress Checkpoint
        PragmaCheckpoint ingressCp = checkpointManager.recordPipelineIngress(pragma, "praxis-audit");
        assertThat(ingressCp.getStageName()).isEqualTo(PraxisCheckpointManager.STAGE_PIPELINE_INGRESS);
        assertThat(ingressCp.getStatus()).isEqualTo(PragmaStatus.IN_PROGRESS);

        // Step 1 Pre/Post
        checkpointManager.recordPreErgon(pragma, "praxis-audit", "ergon-1", 0);
        checkpointManager.recordPostErgon(pragma, "praxis-audit", "ergon-1", 0, 1);

        // Step 2 Pre/Post
        checkpointManager.recordPreErgon(pragma, "praxis-audit", "ergon-2", 1);
        checkpointManager.recordPostErgon(pragma, "praxis-audit", "ergon-2", 1, 2);

        // Complete
        PragmaCheckpoint completeCp = checkpointManager.recordPipelineComplete(pragma, "praxis-audit", 2);
        assertThat(completeCp.getStageName()).isEqualTo(PraxisCheckpointManager.STAGE_PIPELINE_COMPLETE);
        assertThat(completeCp.getStatus()).isEqualTo(PragmaStatus.COMPLETED);

        // Verify from Cache
        Optional<Pragma> cached = pragmaCacheService.getPragma("PRAGMA-CP-TEST");
        assertThat(cached).isPresent();
        assertThat(cached.get().getCheckpoints()).hasSize(6);
        assertThat(cached.get().getStatus()).isEqualTo(PragmaStatus.COMPLETED);
    }

    @Test
    @DisplayName("Praxis failure snapshot and error checkpoint handling")
    void testPraxisFailureSnapshotting() throws Exception {
        Praxis praxis = new Praxis("praxis-fail-test", "Failing Pipeline");
        praxis.setPragmaCacheService(pragmaCacheService);
        praxis.setCheckpointManager(checkpointManager);

        ExtractHl7Ergon step1 = new ExtractHl7Ergon();
        FailingErgon step2 = new FailingErgon();

        praxis.addActivity(step1);
        praxis.addActivity(step2);

        praxis.configureChainedEndpoints();
        praxis.registerRoutes(camelContext);
        camelContext.addRoutes(praxis.createSequencePipelineRoute());
        camelContext.start();

        Pragma inputPragma = new Pragma("PRAGMA-FAIL-001", "praxis-fail-test", PragmaStatus.REQUESTED);
        pragmaCacheService.savePragma(inputPragma);

        // Execution should fail and capture failure checkpoint
        Exchange failedExchange = producerTemplate.request(praxis.getPipelineInputEndpoint(), exchange -> {
            exchange.getMessage().setBody(inputPragma);
        });

        assertThat(failedExchange.isFailed() || failedExchange.getException() != null).isTrue();

        // Verify failure checkpoint in cache
        Optional<Pragma> cachedPragmaOpt = pragmaCacheService.getPragma("PRAGMA-FAIL-001");
        assertThat(cachedPragmaOpt).isPresent();
        Pragma cachedPragma = cachedPragmaOpt.get();

        assertThat(cachedPragma.getStatus()).isEqualTo(PragmaStatus.FAILED);
        assertThat(cachedPragma.getLatestCheckpoint()).isPresent();
        assertThat(cachedPragma.getLatestCheckpoint().get().getStageName()).isEqualTo(PraxisCheckpointManager.STAGE_PIPELINE_FAILED);
        assertThat(cachedPragma.getLatestCheckpoint().get().getStatus()).isEqualTo(PragmaStatus.FAILED);
        assertThat(cachedPragma.getLatestCheckpoint().get().getStatusMessage()).contains("Simulated database outage");
    }

    @Test
    @DisplayName("PragmaCacheService CRUD and checkpoint query operations")
    void testPragmaCacheServiceOperations() {
        Pragma pragma = new Pragma("PRAGMA-CRUD-1", "praxis-crud", PragmaStatus.DRAFT);
        pragma.addCheckpoint(PragmaCheckpoint.of("PRAGMA-CRUD-1", "STAGE_1", PragmaStatus.DRAFT));

        pragmaCacheService.savePragma(pragma);
        assertThat(pragmaCacheService.containsPragma("PRAGMA-CRUD-1")).isTrue();

        Optional<Pragma> retrieved = pragmaCacheService.getPragma("PRAGMA-CRUD-1");
        assertThat(retrieved).isPresent();
        assertThat(retrieved.get().getPraxisId()).isEqualTo("praxis-crud");

        List<PragmaCheckpoint> checkpoints = pragmaCacheService.getCheckpoints("PRAGMA-CRUD-1");
        assertThat(checkpoints).hasSize(1);

        boolean deleted = pragmaCacheService.deletePragma("PRAGMA-CRUD-1");
        assertThat(deleted).isTrue();
        assertThat(pragmaCacheService.containsPragma("PRAGMA-CRUD-1")).isFalse();
    }
}
