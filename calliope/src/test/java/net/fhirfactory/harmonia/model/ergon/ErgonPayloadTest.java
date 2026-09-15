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

package net.fhirfactory.harmonia.model.ergon;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.fhirfactory.harmonia.model.topic.Topic;
import org.hl7.fhir.r5.model.Attachment;
import org.hl7.fhir.r5.model.Patient;
import org.hl7.fhir.r5.model.Reference;
import org.hl7.fhir.r5.model.StringType;
import org.hl7.fhir.r5.model.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ErgonPayloadTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Test default constructor and basic getters/setters")
    void testDefaultConstructorAndSetters() {
        ErgonPayload payload = new ErgonPayload();
        assertThat(payload.getPayloadOrder()).isNull();
        assertThat(payload.getPayloadContainer()).isNull();
        assertThat(payload.getPayloadContent()).isNull();
        assertThat(payload.getPayload()).isNotNull();

        Topic containerTopic = Topic.fromHl7("ADT", "A01", "pas-gw");
        Topic contentTopic = new Topic("Health", "FHIR", "R5", "Patient", "Resource");

        payload.setPayloadOrder(1);
        payload.setPayloadContainer(containerTopic);
        payload.setPayloadContent(contentTopic);

        assertThat(payload.getPayloadOrder()).isEqualTo(1);
        assertThat(payload.getPayloadContainer()).isEqualTo(containerTopic);
        assertThat(payload.getPayloadContent()).isEqualTo(contentTopic);
    }

    @Test
    @DisplayName("Test FHIR Resource Reference payload encapsulation")
    void testFhirResourcePayload() {
        Topic container = Topic.fromHl7("ADT", "A01", "gw-1");
        Topic content = new Topic("Health", "FHIR", "R5", "Patient", "Resource");
        Reference patientRef = new Reference("Patient/MRN-12345");

        ErgonPayload payload = new ErgonPayload(0, container, content, patientRef);

        assertThat(payload.isFhirResource()).isTrue();
        assertThat(payload.isJsonObject()).isFalse();
        assertThat(payload.getResourceReferenceString()).isEqualTo("Patient/MRN-12345");
        assertThat(payload.getResourceReference()).isNotNull();
        assertThat(payload.getResourceReference().getReference()).isEqualTo("Patient/MRN-12345");
        assertThat(payload.getJsonString()).isNull();
        assertThat(payload.getJsonNode()).isNull();

        // Test with Resource instance
        Patient patient = new Patient();
        patient.setId("Patient/PAT-999");
        ErgonPayload resourcePayload = ErgonPayload.fromFhirResource(1, container, content, patient);
        assertThat(resourcePayload.isFhirResource()).isTrue();
        assertThat(resourcePayload.getResourceReferenceString()).isEqualTo("Patient/PAT-999");

        // Test with reference string
        ErgonPayload refStringPayload = ErgonPayload.fromFhirResourceReference(2, container, content, "Bundle/BND-100");
        assertThat(refStringPayload.isFhirResource()).isTrue();
        assertThat(refStringPayload.getResourceReferenceString()).isEqualTo("Bundle/BND-100");
    }

    @Test
    @DisplayName("Test JSON Object payload encapsulation")
    void testJsonObjectPayload() {
        Topic container = Topic.fromHl7("ORM", "O01", "gw-2");
        Topic content = new Topic("Health", "JSON", "1.0", "OrderNotification", "Json");
        String jsonStr = "{\"orderId\":\"ORD-555\",\"patientName\":\"John Doe\",\"status\":\"SCHEDULED\"}";

        ErgonPayload payload = ErgonPayload.fromJson(0, container, content, jsonStr);

        assertThat(payload.isFhirResource()).isFalse();
        assertThat(payload.isJsonObject()).isTrue();
        assertThat(payload.getJsonString()).isEqualTo(jsonStr);
        assertThat(payload.getResourceReferenceString()).isNull();

        JsonNode jsonNode = payload.getJsonNode();
        assertThat(jsonNode).isNotNull();
        assertThat(jsonNode.get("orderId").asText()).isEqualTo("ORD-555");
        assertThat(jsonNode.get("patientName").asText()).isEqualTo("John Doe");

        // Test with Attachment-based payload
        Task.TaskInputComponent inputWithAtt = new Task.TaskInputComponent();
        Attachment att = new Attachment();
        att.setContentType("application/json");
        att.setData(jsonStr.getBytes(StandardCharsets.UTF_8));
        inputWithAtt.setValue(att);

        ErgonPayload attPayload = new ErgonPayload(1, container, content, inputWithAtt);
        assertThat(attPayload.isJsonObject()).isTrue();
        assertThat(attPayload.getJsonString()).isEqualTo(jsonStr);

        // Test from JsonNode
        ErgonPayload nodePayload = ErgonPayload.fromJson(2, container, content, jsonNode);
        assertThat(nodePayload.isJsonObject()).isTrue();
        assertThat(nodePayload.getJsonNode().get("status").asText()).isEqualTo("SCHEDULED");
    }

    @Test
    @DisplayName("Test conversion to and from Task.TaskInputComponent")
    void testTaskInputComponentConversion() {
        Topic container = Topic.fromHl7("ADT", "A08", "pas-gw");
        Topic content = new Topic("Health", "FHIR", "R5", "Patient", "Resource");

        ErgonPayload original = ErgonPayload.fromFhirResourceReference(1, container, content, "Patient/PAT-1234");
        Task.TaskInputComponent inputComponent = original.toTaskInput();

        assertThat(inputComponent).isNotNull();
        assertThat(inputComponent.hasValue()).isTrue();
        assertThat(inputComponent.getValue()).isInstanceOf(Reference.class);
        assertThat(((Reference) inputComponent.getValue()).getReference()).isEqualTo("Patient/PAT-1234");
        assertThat(inputComponent.hasExtension(ErgonPayload.EXTENSION_PAYLOAD_ORDER)).isTrue();
        assertThat(inputComponent.hasExtension(ErgonPayload.EXTENSION_PAYLOAD_CONTAINER)).isTrue();
        assertThat(inputComponent.hasExtension(ErgonPayload.EXTENSION_PAYLOAD_CONTENT)).isTrue();

        // Reconstruct TaskPayload from TaskInputComponent
        ErgonPayload reconstructed = ErgonPayload.fromTaskInput(inputComponent);
        assertThat(reconstructed.getPayloadOrder()).isEqualTo(1);
        assertThat(reconstructed.getPayloadContainer().getDataElement()).isEqualTo("ADT");
        assertThat(reconstructed.getPayloadContainer().getDataElementQualifier()).isEqualTo("A08");
        assertThat(reconstructed.getPayloadContent().getModel()).isEqualTo("FHIR");
        assertThat(reconstructed.getResourceReferenceString()).isEqualTo("Patient/PAT-1234");
        assertThat(reconstructed.isFhirResource()).isTrue();
    }

    @Test
    @DisplayName("Test conversion to and from Task.TaskOutputComponent")
    void testTaskOutputComponentConversion() {
        Topic container = Topic.fromHl7("ADT", "A01", "pas-gw");
        Topic content = new Topic("Health", "JSON", "1.0", "AckResponse", "Json");
        String ackJson = "{\"status\":\"ACK\",\"controlId\":\"MSG-001\"}";

        ErgonPayload original = ErgonPayload.fromJson(2, container, content, ackJson);
        Task.TaskOutputComponent outputComponent = original.toTaskOutput();

        assertThat(outputComponent).isNotNull();
        assertThat(outputComponent.hasValue()).isTrue();
        assertThat(outputComponent.getValue()).isInstanceOf(StringType.class);
        assertThat(outputComponent.hasExtension(ErgonPayload.EXTENSION_PAYLOAD_ORDER)).isTrue();

        ErgonPayload reconstructed = ErgonPayload.fromTaskOutput(outputComponent);
        assertThat(reconstructed.getPayloadOrder()).isEqualTo(2);
        assertThat(reconstructed.getPayloadContainer().getDataElement()).isEqualTo("ADT");
        assertThat(reconstructed.getPayloadContent().getDataElement()).isEqualTo("AckResponse");
        assertThat(reconstructed.getJsonString()).isEqualTo(ackJson);
        assertThat(reconstructed.isJsonObject()).isTrue();
    }

    @Test
    @DisplayName("Test bulk operations on Task: apply, extract, add payloads")
    void testBulkTaskOperations() {
        Task task = new Task();
        task.setId("Task/TASK-BULK-01");

        Topic container = Topic.fromHl7("ADT", "A01", "pas-gw");
        Topic content1 = new Topic("Health", "FHIR", "R5", "Communication", "RawHL7");
        Topic content2 = new Topic("Health", "FHIR", "R5", "Patient", "Resource");

        ErgonPayload p1 = ErgonPayload.fromFhirResourceReference(0, container, content1, "Communication/COMM-01");
        ErgonPayload p2 = ErgonPayload.fromFhirResourceReference(1, container, content2, "Patient/PAT-01");

        ErgonPayload.applyInputPayloads(task, Arrays.asList(p1, p2));
        assertThat(task.getInput()).hasSize(2);

        List<ErgonPayload> extractedInputs = ErgonPayload.extractInputPayloads(task);
        assertThat(extractedInputs).hasSize(2);
        assertThat(extractedInputs.get(0).getResourceReferenceString()).isEqualTo("Communication/COMM-01");
        assertThat(extractedInputs.get(0).getPayloadOrder()).isEqualTo(0);
        assertThat(extractedInputs.get(1).getResourceReferenceString()).isEqualTo("Patient/PAT-01");
        assertThat(extractedInputs.get(1).getPayloadOrder()).isEqualTo(1);

        // Add an additional payload to output
        Topic outContent = new Topic("Health", "FHIR", "R5", "Bundle", "ExportBundle");
        ErgonPayload outPayload = ErgonPayload.fromFhirResourceReference(0, container, outContent, "Bundle/BND-999");
        ErgonPayload.addOutputPayload(task, outPayload);

        assertThat(task.getOutput()).hasSize(1);
        List<ErgonPayload> extractedOutputs = ErgonPayload.extractOutputPayloads(task);
        assertThat(extractedOutputs).hasSize(1);
        assertThat(extractedOutputs.get(0).getResourceReferenceString()).isEqualTo("Bundle/BND-999");
    }

    @Test
    @DisplayName("Test Comparable ordering of TaskPayload")
    void testComparableOrdering() {
        Topic topic = Topic.fromHl7("ADT", "A01", "gw");
        ErgonPayload p3 = ErgonPayload.fromFhirResourceReference(3, topic, topic, "Ref/3");
        ErgonPayload p1 = ErgonPayload.fromFhirResourceReference(1, topic, topic, "Ref/1");
        ErgonPayload p2 = ErgonPayload.fromFhirResourceReference(2, topic, topic, "Ref/2");
        ErgonPayload pNull = ErgonPayload.fromFhirResourceReference(null, topic, topic, "Ref/null");

        List<ErgonPayload> list = Arrays.asList(p3, pNull, p1, p2);
        Collections.sort(list);

        assertThat(list.get(0).getPayloadOrder()).isEqualTo(1);
        assertThat(list.get(1).getPayloadOrder()).isEqualTo(2);
        assertThat(list.get(2).getPayloadOrder()).isEqualTo(3);
        assertThat(list.get(3).getPayloadOrder()).isNull();
    }

    @Test
    @DisplayName("Test Jackson JSON serialization and deserialization")
    void testJacksonSerialization() throws Exception {
        Topic container = Topic.fromHl7("ADT", "A01", "pas-gw");
        Topic content = new Topic("Health", "FHIR", "R5", "Patient", "Resource");

        ErgonPayload original = ErgonPayload.fromFhirResourceReference(1, container, content, "Patient/MRN-777");

        String json = objectMapper.writeValueAsString(original);
        assertThat(json).contains("\"payloadOrder\":1");
        assertThat(json).contains("\"resourceReference\":\"Patient/MRN-777\"");
        assertThat(json).contains("\"payloadContainer\"");
        assertThat(json).contains("\"payloadContent\"");

        ErgonPayload deserialized = objectMapper.readValue(json, ErgonPayload.class);
        assertThat(deserialized.getPayloadOrder()).isEqualTo(1);
        assertThat(deserialized.getResourceReferenceString()).isEqualTo("Patient/MRN-777");
        assertThat(deserialized.getPayloadContainer().getDataElement()).isEqualTo("ADT");
        assertThat(deserialized.getPayloadContent().getModel()).isEqualTo("FHIR");
    }

    @Test
    @DisplayName("Test copy constructor and equality")
    void testCopyAndEquality() {
        Topic container = Topic.fromHl7("ADT", "A01", "pas-gw");
        Topic content = new Topic("Health", "FHIR", "R5", "Patient", "Resource");

        ErgonPayload original = ErgonPayload.fromFhirResourceReference(1, container, content, "Patient/PAT-1");
        ErgonPayload copy = new ErgonPayload(original);

        assertThat(copy).isEqualTo(original);
        assertThat(copy.hashCode()).isEqualTo(original.hashCode());
        assertThat(copy.toString()).contains("payloadOrder=1");
    }
}
