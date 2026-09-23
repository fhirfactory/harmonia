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

package net.fhirfactory.harmonia.model.pragma;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.fhirfactory.harmonia.model.ergon.ErgonPayload;
import net.fhirfactory.harmonia.model.topic.Topic;
import net.fhirfactory.harmonia.themis.api.model.PrincipalType;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthority;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;
import org.hl7.fhir.r5.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PragmaTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("Pragma initialization with default and builder values")
    void testPragmaInitialization() {
        Pragma defaultPragma = new Pragma();
        assertThat(defaultPragma.getPragmaId()).isNotNull();
        assertThat(defaultPragma.getCorrelationId()).isNotNull();
        assertThat(defaultPragma.getStatus()).isEqualTo(PragmaStatus.DRAFT);
        assertThat(defaultPragma.getAuthoredOn()).isNotNull();
        assertThat(defaultPragma.getInput()).isEmpty();
        assertThat(defaultPragma.getOutput()).isEmpty();
        assertThat(defaultPragma.getCheckpoints()).isEmpty();

        String id = UUID.randomUUID().toString();
        Pragma built = Pragma.builder()
                .pragmaId(id)
                .praxisId("praxis-patient-sync")
                .correlationId("corr-123")
                .causationId("cause-456")
                .status(PragmaStatus.REQUESTED)
                .priority(80)
                .priorityCode("urgent")
                .source("system-a")
                .destination("system-b")
                .addMetadata("facility", "Hospital-Main")
                .build();

        assertThat(built.getPragmaId()).isEqualTo(id);
        assertThat(built.getPraxisId()).isEqualTo("praxis-patient-sync");
        assertThat(built.getCorrelationId()).isEqualTo("corr-123");
        assertThat(built.getCausationId()).isEqualTo("cause-456");
        assertThat(built.getStatus()).isEqualTo(PragmaStatus.REQUESTED);
        assertThat(built.getPriority()).isEqualTo(80);
        assertThat(built.getPriorityCode()).isEqualTo("urgent");
        assertThat(built.getSource()).isEqualTo("system-a");
        assertThat(built.getDestination()).isEqualTo("system-b");
        assertThat(built.getMetadata()).containsEntry("facility", "Hospital-Main");
    }

    @Test
    @DisplayName("Pragma copy constructor creates deep copy")
    void testPragmaCopyConstructor() {
        Topic container = new Topic("Health", "HL7", "2.4", "ADT", "A01");
        Topic content = new Topic("Health", "Clinical", "1.0", "Demographics", "Identity");
        ErgonPayload inputPayload = ErgonPayload.fromJson(0, container, content, "{\"patientId\":\"P123\"}");

        Pragma original = Pragma.builder()
                .pragmaId("pragma-orig")
                .praxisId("praxis-1")
                .status(PragmaStatus.IN_PROGRESS)
                .addInput(inputPayload)
                .addCheckpoint(PragmaCheckpoint.of("pragma-orig", "ergon-1", "INGRESS", PragmaStatus.IN_PROGRESS, 0))
                .build();

        Pragma copy = new Pragma(original);
        assertThat(copy.getPragmaId()).isEqualTo(original.getPragmaId());
        assertThat(copy.getStatus()).isEqualTo(original.getStatus());
        assertThat(copy.getInput()).hasSize(1);
        assertThat(copy.getCheckpoints()).hasSize(1);

        // Modifying copy should not mutate original
        copy.setStatus(PragmaStatus.COMPLETED);
        assertThat(original.getStatus()).isEqualTo(PragmaStatus.IN_PROGRESS);
    }

    @Test
    @DisplayName("Pragma input and output manipulation and lookups")
    void testInputOutputOperations() {
        Pragma pragma = new Pragma("p-1", "praxis-test", PragmaStatus.IN_PROGRESS);

        Topic hl7Container = new Topic("Health", "HL7", "2.4", "ADT", "A01");
        Topic adtContent = new Topic("Health", "Clinical", "1.0", "AdtMessage", null);
        Topic fhirContainer = new Topic("Health", "FHIR", "R5", "Bundle", null);
        Topic fhirContent = new Topic("Health", "FHIR", "R5", "Patient", null);

        ErgonPayload input1 = ErgonPayload.fromJson(null, hl7Container, adtContent, "MSH|^~\\&|...");
        ErgonPayload output1 = ErgonPayload.fromJson(null, fhirContainer, fhirContent, "{\"resourceType\":\"Patient\"}");

        pragma.addInput(input1);
        pragma.addOutput(output1);

        assertThat(pragma.getInput()).hasSize(1);
        assertThat(pragma.getInput().get(0).getPayloadOrder()).isEqualTo(0);
        assertThat(pragma.getOutput()).hasSize(1);
        assertThat(pragma.getOutput().get(0).getPayloadOrder()).isEqualTo(0);

        Optional<ErgonPayload> foundInput = pragma.findInput(hl7Container, adtContent);
        assertThat(foundInput).isPresent();
        assertThat(foundInput.get().getJsonString()).isEqualTo("MSH|^~\\&|...");

        Optional<ErgonPayload> foundOutput = pragma.findOutput(fhirContainer, fhirContent);
        assertThat(foundOutput).isPresent();
        assertThat(foundOutput.get().getJsonString()).contains("resourceType");

        assertThat(pragma.findInputByOrder(0)).isPresent();
        assertThat(pragma.findOutputByOrder(0)).isPresent();
        assertThat(pragma.findInputByOrder(99)).isEmpty();
    }

    @Test
    @DisplayName("Pragma checkpoint recording updates status and history")
    void testPragmaCheckpoints() {
        Pragma pragma = new Pragma("pragma-100", "praxis-seq", PragmaStatus.REQUESTED);
        assertThat(pragma.getCheckpoints()).isEmpty();

        PragmaCheckpoint cp1 = PragmaCheckpoint.builder()
                .pragmaId("pragma-100")
                .ergonId("ergon-extract")
                .praxisId("praxis-seq")
                .stageName("EXTRACT_INGRESS")
                .status(PragmaStatus.IN_PROGRESS)
                .stepIndex(0)
                .statusMessage("Extracting HL7 ADT message")
                .build();

        pragma.addCheckpoint(cp1);
        assertThat(pragma.getStatus()).isEqualTo(PragmaStatus.IN_PROGRESS);
        assertThat(pragma.getCheckpoints()).hasSize(1);
        assertThat(pragma.getLatestCheckpoint()).isPresent();
        assertThat(pragma.getLatestCheckpoint().get().getStageName()).isEqualTo("EXTRACT_INGRESS");

        PragmaCheckpoint cp2 = PragmaCheckpoint.builder()
                .pragmaId("pragma-100")
                .ergonId("ergon-transform")
                .stageName("TRANSFORM_COMPLETE")
                .status(PragmaStatus.COMPLETED)
                .stepIndex(1)
                .statusMessage("Successfully transformed to FHIR")
                .build();

        pragma.addCheckpoint(cp2);
        assertThat(pragma.getStatus()).isEqualTo(PragmaStatus.COMPLETED);
        assertThat(pragma.getCheckpoints()).hasSize(2);
        assertThat(pragma.getLatestCheckpoint().get().getStatus()).isEqualTo(PragmaStatus.COMPLETED);
    }

    @Test
    @DisplayName("PragmaStatus transitions, codes, and FHIR mapping")
    void testPragmaStatus() {
        assertThat(PragmaStatus.DRAFT.getCode()).isEqualTo("draft");
        assertThat(PragmaStatus.REQUESTED.isActive()).isTrue();
        assertThat(PragmaStatus.IN_PROGRESS.isActive()).isTrue();
        assertThat(PragmaStatus.COMPLETED.isTerminal()).isTrue();
        assertThat(PragmaStatus.FAILED.isTerminal()).isTrue();
        assertThat(PragmaStatus.CANCELLED.isTerminal()).isTrue();

        assertThat(PragmaStatus.fromCode("in-progress")).isEqualTo(PragmaStatus.IN_PROGRESS);
        assertThat(PragmaStatus.fromCode("IN_PROGRESS")).isEqualTo(PragmaStatus.IN_PROGRESS);
        assertThat(PragmaStatus.fromCode("COMPLETED")).isEqualTo(PragmaStatus.COMPLETED);
        assertThat(PragmaStatus.fromCode("unknown")).isEqualTo(PragmaStatus.DRAFT);

        assertThat(PragmaStatus.IN_PROGRESS.toFhirTaskStatus()).isEqualTo(Task.TaskStatus.INPROGRESS);
        assertThat(PragmaStatus.COMPLETED.toFhirTaskStatus()).isEqualTo(Task.TaskStatus.COMPLETED);
        assertThat(PragmaStatus.fromFhirTaskStatus(Task.TaskStatus.INPROGRESS)).isEqualTo(PragmaStatus.IN_PROGRESS);
        assertThat(PragmaStatus.fromFhirTaskStatus(Task.TaskStatus.COMPLETED)).isEqualTo(PragmaStatus.COMPLETED);
    }

    @Test
    @DisplayName("Pragma and PragmaCheckpoint JSON serialization and deserialization")
    void testJsonSerialization() throws Exception {
        Pragma pragma = Pragma.builder()
                .pragmaId("json-pragma-1")
                .correlationId("corr-999")
                .praxisId("praxis-json")
                .status(PragmaStatus.IN_PROGRESS)
                .priority(50)
                .priorityCode("routine")
                .authoredOn(new Date())
                .source("source-gateway")
                .destination("dest-ops")
                .addInput(ErgonPayload.fromJson(0, new Topic("Health", "HL7", "2.4", "ADT", null), null, "{\"msg\":\"data\"}"))
                .addCheckpoint(PragmaCheckpoint.of("json-pragma-1", "ergon-1", "STEP1", PragmaStatus.IN_PROGRESS, 0))
                .addMetadata("env", "prod")
                .build();

        String json = objectMapper.writeValueAsString(pragma);
        assertThat(json).contains("json-pragma-1");
        assertThat(json).contains("corr-999");
        assertThat(json).contains("praxis-json");
        assertThat(json).contains("in-progress");

        Pragma deserialized = objectMapper.readValue(json, Pragma.class);
        assertThat(deserialized.getPragmaId()).isEqualTo("json-pragma-1");
        assertThat(deserialized.getCorrelationId()).isEqualTo("corr-999");
        assertThat(deserialized.getPraxisId()).isEqualTo("praxis-json");
        assertThat(deserialized.getStatus()).isEqualTo(PragmaStatus.IN_PROGRESS);
        assertThat(deserialized.getInput()).hasSize(1);
        assertThat(deserialized.getCheckpoints()).hasSize(1);
        assertThat(deserialized.getMetadata()).containsEntry("env", "prod");
    }

    @Test
    @DisplayName("Bi-directional Pragma to FHIR Task conversion")
    void testPragmaFhirConverter() {
        Topic container = new Topic("Health", "FHIR", "R5", "Bundle", "Transaction");
        Topic content = new Topic("Health", "Clinical", "1.0", "Patient", "Demographics");
        ErgonPayload inputPayload = ErgonPayload.fromJson(0, container, content, "{\"resourceType\":\"Patient\",\"id\":\"P1\"}");
        ErgonPayload outputPayload = ErgonPayload.fromJson(0, container, content, "{\"resourceType\":\"Patient\",\"id\":\"P1\",\"active\":true}");

        Pragma original = Pragma.builder()
                .pragmaId("pragma-fhir-test-123")
                .correlationId("corr-abc")
                .causationId("cause-xyz")
                .praxisId("http://fhirfactory.net/praxis/PatientIdentityWorkflow")
                .status(PragmaStatus.IN_PROGRESS)
                .priority(75)
                .priorityCode("asap")
                .authoredOn(new Date())
                .source("Hl7IngressGateway")
                .destination("EhrEgressGateway")
                .addInput(inputPayload)
                .addOutput(outputPayload)
                .addCheckpoint(PragmaCheckpoint.builder()
                        .pragmaId("pragma-fhir-test-123")
                        .ergonId("ergon-patient-id")
                        .stageName("PATIENT_IDENTIFIED")
                        .status(PragmaStatus.IN_PROGRESS)
                        .statusMessage("Patient matched in EMPI")
                        .build())
                .addMetadata("channel", "HL7_MLLP")
                .build();

        // 1. Convert Pragma -> FHIR Task
        Task fhirTask = PragmaFhirConverter.toFhirTask(original);
        assertThat(fhirTask).isNotNull();
        assertThat(fhirTask.getId()).isEqualTo("pragma-fhir-test-123");
        assertThat(fhirTask.getStatus()).isEqualTo(Task.TaskStatus.INPROGRESS);
        assertThat(fhirTask.getInstantiatesCanonical()).isEqualTo("http://fhirfactory.net/praxis/PatientIdentityWorkflow");
        assertThat(fhirTask.getInput()).hasSize(1);
        assertThat(fhirTask.getOutput()).hasSize(1);
        assertThat(fhirTask.getNote()).hasSize(1);
        assertThat(fhirTask.getNote().get(0).getText()).contains("PATIENT_IDENTIFIED");
        assertThat(fhirTask.hasMeta()).isTrue();
        assertThat(fhirTask.getMeta().getSecurity()).hasSize(1);
        assertThat(fhirTask.getMeta().getSecurity().get(0).getCode()).isEqualTo("N");

        // 2. Convert FHIR Task -> Pragma
        Pragma reconstructed = PragmaFhirConverter.fromFhirTask(fhirTask);
        assertThat(reconstructed).isNotNull();
        assertThat(reconstructed.getPragmaId()).isEqualTo("pragma-fhir-test-123");
        assertThat(reconstructed.getCorrelationId()).isEqualTo("corr-abc");
        assertThat(reconstructed.getCausationId()).isEqualTo("cause-xyz");
        assertThat(reconstructed.getPraxisId()).isEqualTo("http://fhirfactory.net/praxis/PatientIdentityWorkflow");
        assertThat(reconstructed.getStatus()).isEqualTo(PragmaStatus.IN_PROGRESS);
        assertThat(reconstructed.getSource()).isEqualTo("Hl7IngressGateway");
        assertThat(reconstructed.getDestination()).isEqualTo("EhrEgressGateway");
        assertThat(reconstructed.getInput()).hasSize(1);
        assertThat(reconstructed.getOutput()).hasSize(1);
        assertThat(reconstructed.getCheckpoints()).hasSize(1);
        assertThat(reconstructed.getMetadata()).containsEntry("channel", "HL7_MLLP");
        assertThat(reconstructed.getMetadata()).containsEntry("confidentiality", "N");
    }

    @Test
    @DisplayName("Pragma to FHIR Task with explicit confidentiality security tagging")
    void testPragmaFhirConverterWithExplicitConfidentiality() {
        Pragma pragma = Pragma.builder()
                .pragmaId("pragma-sec-1")
                .addMetadata("confidentiality", "R")
                .build();

        Task task = PragmaFhirConverter.toFhirTask(pragma);
        assertThat(task.getMeta().getSecurity()).hasSize(1);
        assertThat(task.getMeta().getSecurity().get(0).getCode()).isEqualTo("R");

        Pragma convertedBack = PragmaFhirConverter.fromFhirTask(task);
        assertThat(convertedBack.getMetadata()).containsEntry("confidentiality", "R");
    }

    @Test
    @DisplayName("Bi-directional Pragma to FHIR Task round-trip with security context, executing principal, and causation ID")
    void testPragmaFhirConverterRoundTripWithExecutingPrincipalAndCausation() {
        ThemisPrincipal originatingPrincipal = ThemisPrincipal.of("dr-smith", PrincipalType.HUMAN, "clinical");
        ThemisPrincipal executingPrincipal = ThemisPrincipal.of("process:ponos-engine", PrincipalType.PROCESS, "ponos");

        Pragma pragma = Pragma.builder()
                .pragmaId("PRAGMA-E2E-SEC-001")
                .correlationId("CORR-ROOT-999")
                .causationId("CAUSE-PARENT-888")
                .praxisId("praxis-sec-pipeline")
                .status(PragmaStatus.IN_PROGRESS)
                .policyVersion("2.1.0")
                .originatingPrincipal(originatingPrincipal)
                .executingPrincipal(executingPrincipal)
                .addOriginatingAuthority("provider.change.submit")
                .addOriginatingAuthority("provider.read")
                .build();

        // 1. Convert to FHIR Task
        Task task = PragmaFhirConverter.toFhirTask(pragma);
        assertThat(task).isNotNull();
        assertThat(task.getId()).isEqualTo("PRAGMA-E2E-SEC-001");

        // Causation and Correlation identifiers
        assertThat(task.getIdentifier()).anyMatch(id ->
                PragmaFhirConverter.IDENTIFIER_SYSTEM_CORRELATION_ID.equals(id.getSystem()) && "CORR-ROOT-999".equals(id.getValue()));
        assertThat(task.getIdentifier()).anyMatch(id ->
                PragmaFhirConverter.IDENTIFIER_SYSTEM_CAUSATION_ID.equals(id.getSystem()) && "CAUSE-PARENT-888".equals(id.getValue()));

        // Originating principal extensions
        assertThat(task.getExtensionByUrl(PragmaFhirConverter.EXTENSION_SECURITY_PRINCIPAL_ID).getValue().toString()).isEqualTo("dr-smith");
        assertThat(task.getExtensionByUrl(PragmaFhirConverter.EXTENSION_SECURITY_PRINCIPAL_TYPE).getValue().toString()).isEqualTo("HUMAN");
        assertThat(task.getExtensionByUrl(PragmaFhirConverter.EXTENSION_SECURITY_SOURCE_DOMAIN).getValue().toString()).isEqualTo("clinical");

        // Executing principal extensions
        assertThat(task.getExtensionByUrl(PragmaFhirConverter.EXTENSION_SECURITY_EXECUTING_PRINCIPAL_ID).getValue().toString()).isEqualTo("process:ponos-engine");
        assertThat(task.getExtensionByUrl(PragmaFhirConverter.EXTENSION_SECURITY_EXECUTING_PRINCIPAL_TYPE).getValue().toString()).isEqualTo("PROCESS");
        assertThat(task.getExtensionByUrl(PragmaFhirConverter.EXTENSION_SECURITY_EXECUTING_SOURCE_DOMAIN).getValue().toString()).isEqualTo("ponos");

        // Authorities
        List<String> auths = task.getExtensionsByUrl(PragmaFhirConverter.EXTENSION_SECURITY_AUTHORITY).stream()
                .map(e -> ((org.hl7.fhir.r5.model.StringType) e.getValue()).getValue())
                .toList();
        assertThat(auths).containsExactlyInAnyOrder("provider.change.submit", "provider.read");

        // Policy version
        assertThat(task.getExtensionByUrl(PragmaFhirConverter.EXTENSION_SECURITY_POLICY_VERSION).getValue().toString()).isEqualTo("2.1.0");

        // 2. Convert back to Pragma
        Pragma reconstructed = PragmaFhirConverter.fromFhirTask(task);
        assertThat(reconstructed).isNotNull();
        assertThat(reconstructed.getPragmaId()).isEqualTo("PRAGMA-E2E-SEC-001");
        assertThat(reconstructed.getCorrelationId()).isEqualTo("CORR-ROOT-999");
        assertThat(reconstructed.getCausationId()).isEqualTo("CAUSE-PARENT-888");
        assertThat(reconstructed.getPolicyVersion()).isEqualTo("2.1.0");

        assertThat(reconstructed.getOriginatingPrincipal()).isEqualTo(originatingPrincipal);
        assertThat(reconstructed.getExecutingPrincipal()).isEqualTo(executingPrincipal);
        assertThat(reconstructed.getOriginatingAuthorities()).containsExactlyInAnyOrder(
                ThemisAuthority.of("provider.change.submit"),
                ThemisAuthority.of("provider.read")
        );

        ThemisSecurityContext secCtx = reconstructed.getOriginatingSecurityContext();
        assertThat(secCtx).isNotNull();
        assertThat(secCtx.originatingPrincipal()).isEqualTo(originatingPrincipal);
        assertThat(secCtx.executingPrincipal()).isEqualTo(executingPrincipal);
        assertThat(secCtx.correlationId()).isEqualTo("CORR-ROOT-999");
        assertThat(secCtx.causationId()).isEqualTo("CAUSE-PARENT-888");
        assertThat(secCtx.authorities()).containsExactlyInAnyOrder(
                ThemisAuthority.of("provider.change.submit"),
                ThemisAuthority.of("provider.read")
        );
    }
}
