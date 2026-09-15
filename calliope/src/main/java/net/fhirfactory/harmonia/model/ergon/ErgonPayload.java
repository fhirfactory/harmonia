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

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.fhirfactory.harmonia.model.topic.Topic;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.*;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Encapsulates the payload of a {@link Task} (Pragma) within the HIE platform.
 * <p>
 * <b>Pragma — Task Instance/State</b>: This particular piece of work as it currently exists - will be used
 * wherever Task (as a synthetic FHIR::Task resource) is used within the codebase.
 * <p>
 * Captured as a composite of:
 * <ul>
 *   <li><b>payloadOrder</b>: Integer sequence/order of the payload within the Task</li>
 *   <li><b>payloadContainer</b>: {@link Topic} specifying the container context (e.g. envelope or message type)</li>
 *   <li><b>payloadContent</b>: {@link Topic} specifying the content classification of the payload</li>
 *   <li><b>payload</b>: {@link Task.TaskInputComponent} containing either a FHIR {@link Reference}
 *       (to a resource previously saved in cache) or a JSON object</li>
 * </ul>
 * <p>
 * This object represents the actual data captured within {@link Task#getInput()} and {@link Task#getOutput()}
 * attributes of Tasks cached and distributed within the HIE.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErgonPayload implements Serializable, Comparable<ErgonPayload> {

    private static final long serialVersionUID = 1L;

    public static final String EXTENSION_PAYLOAD_ORDER = "http://fhirfactory.net/hie/task/payload-order";
    public static final String EXTENSION_PAYLOAD_CONTAINER = "http://fhirfactory.net/hie/task/payload-container";
    public static final String EXTENSION_PAYLOAD_CONTENT = "http://fhirfactory.net/hie/task/payload-content";

    public static final String TASK_PAYLOAD_TYPE_SYSTEM = "http://fhirfactory.net/hie/task/payload-type";
    public static final String TASK_PAYLOAD_TYPE_FHIR_RESOURCE = "fhir-resource";
    public static final String TASK_PAYLOAD_TYPE_JSON_OBJECT = "json-object";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @JsonProperty("payloadOrder")
    @JsonAlias({"payload_order", "order", "sequence"})
    private Integer payloadOrder;

    @JsonProperty("payloadContainer")
    @JsonAlias({"payload_container", "containerTopic", "container_topic", "container"})
    private Topic payloadContainer;

    @JsonProperty("payloadContent")
    @JsonAlias({"payload_content", "contentTopic", "content_topic", "content"})
    private Topic payloadContent;

    @JsonIgnore
    private Task.TaskInputComponent payload;

    /**
     * Default constructor.
     */
    public ErgonPayload() {
        this.payload = new Task.TaskInputComponent();
    }

    /**
     * Full constructor with explicit TaskInputComponent payload.
     *
     * @param payloadOrder     sequence index of the payload
     * @param payloadContainer Topic defining container envelope context
     * @param payloadContent   Topic defining content specification
     * @param payload          FHIR TaskInputComponent encapsulating the reference or JSON
     */
    public ErgonPayload(Integer payloadOrder, Topic payloadContainer, Topic payloadContent, Task.TaskInputComponent payload) {
        this.payloadOrder = payloadOrder;
        this.payloadContainer = payloadContainer;
        this.payloadContent = payloadContent;
        this.payload = payload != null ? payload : new Task.TaskInputComponent();
    }

    /**
     * Constructor for a FHIR Resource payload represented by a {@link Reference}.
     *
     * @param payloadOrder      sequence index of the payload
     * @param payloadContainer  Topic defining container envelope context
     * @param payloadContent    Topic defining content specification
     * @param resourceReference Reference to the FHIR resource in cache
     */
    public ErgonPayload(Integer payloadOrder, Topic payloadContainer, Topic payloadContent, Reference resourceReference) {
        this.payloadOrder = payloadOrder;
        this.payloadContainer = payloadContainer;
        this.payloadContent = payloadContent;
        this.payload = new Task.TaskInputComponent();
        setResourceReference(resourceReference);
    }

    /**
     * Constructor for a FHIR Resource payload represented by a resource instance.
     *
     * @param payloadOrder     sequence index of the payload
     * @param payloadContainer Topic defining container envelope context
     * @param payloadContent   Topic defining content specification
     * @param resource         FHIR resource instance
     */
    public ErgonPayload(Integer payloadOrder, Topic payloadContainer, Topic payloadContent, IBaseResource resource) {
        this.payloadOrder = payloadOrder;
        this.payloadContainer = payloadContainer;
        this.payloadContent = payloadContent;
        this.payload = new Task.TaskInputComponent();
        setResource(resource);
    }

    /**
     * Constructor for a JSON object payload represented by a JSON string or JsonNode.
     *
     * @param payloadOrder     sequence index of the payload
     * @param payloadContainer Topic defining container envelope context
     * @param payloadContent   Topic defining content specification
     * @param jsonNode         JSON node
     */
    public ErgonPayload(Integer payloadOrder, Topic payloadContainer, Topic payloadContent, JsonNode jsonNode) {
        this.payloadOrder = payloadOrder;
        this.payloadContainer = payloadContainer;
        this.payloadContent = payloadContent;
        this.payload = new Task.TaskInputComponent();
        setJsonObject(jsonNode);
    }

    /**
     * Copy constructor.
     *
     * @param other source TaskPayload
     */
    public ErgonPayload(ErgonPayload other) {
        if (other != null) {
            this.payloadOrder = other.payloadOrder;
            this.payloadContainer = other.payloadContainer != null ? new Topic(other.payloadContainer) : null;
            this.payloadContent = other.payloadContent != null ? new Topic(other.payloadContent) : null;
            this.payload = other.payload != null ? other.payload.copy() : new Task.TaskInputComponent();
        } else {
            this.payload = new Task.TaskInputComponent();
        }
    }

    // =========================================================================
    // Static Factory Methods
    // =========================================================================

    /**
     * Creates a TaskPayload encapsulating a FHIR Resource reference.
     *
     * @param payloadOrder      sequence order
     * @param payloadContainer  container Topic
     * @param payloadContent    content Topic
     * @param resourceReference FHIR Reference to cached resource
     * @return constructed TaskPayload
     */
    public static ErgonPayload fromFhirResource(Integer payloadOrder, Topic payloadContainer, Topic payloadContent, Reference resourceReference) {
        return new ErgonPayload(payloadOrder, payloadContainer, payloadContent, resourceReference);
    }

    /**
     * Creates a TaskPayload encapsulating a FHIR Resource reference string (e.g., "Patient/123", "Bundle/456").
     *
     * @param payloadOrder           sequence order
     * @param payloadContainer       container Topic
     * @param payloadContent         content Topic
     * @param resourceReferenceString reference path string
     * @return constructed TaskPayload
     */
    public static ErgonPayload fromFhirResourceReference(Integer payloadOrder, Topic payloadContainer, Topic payloadContent, String resourceReferenceString) {
        ErgonPayload ergonPayload = new ErgonPayload(payloadOrder, payloadContainer, payloadContent, (Task.TaskInputComponent) null);
        ergonPayload.setResourceReference(resourceReferenceString);
        return ergonPayload;
    }

    /**
     * Creates a TaskPayload encapsulating a FHIR Resource instance.
     *
     * @param payloadOrder     sequence order
     * @param payloadContainer container Topic
     * @param payloadContent   content Topic
     * @param resource         FHIR resource instance
     * @return constructed TaskPayload
     */
    public static ErgonPayload fromFhirResource(Integer payloadOrder, Topic payloadContainer, Topic payloadContent, IBaseResource resource) {
        return new ErgonPayload(payloadOrder, payloadContainer, payloadContent, resource);
    }

    /**
     * Creates a TaskPayload encapsulating a JSON object string.
     *
     * @param payloadOrder     sequence order
     * @param payloadContainer container Topic
     * @param payloadContent   content Topic
     * @param jsonString       JSON string
     * @return constructed TaskPayload
     */
    public static ErgonPayload fromJson(Integer payloadOrder, Topic payloadContainer, Topic payloadContent, String jsonString) {
        ErgonPayload ergonPayload = new ErgonPayload(payloadOrder, payloadContainer, payloadContent, (Task.TaskInputComponent) null);
        ergonPayload.setJsonObject(jsonString);
        return ergonPayload;
    }

    /**
     * Creates a TaskPayload encapsulating a Jackson JsonNode.
     *
     * @param payloadOrder     sequence order
     * @param payloadContainer container Topic
     * @param payloadContent   content Topic
     * @param jsonNode         JSON node
     * @return constructed TaskPayload
     */
    public static ErgonPayload fromJson(Integer payloadOrder, Topic payloadContainer, Topic payloadContent, JsonNode jsonNode) {
        return new ErgonPayload(payloadOrder, payloadContainer, payloadContent, jsonNode);
    }

    /**
     * Creates a TaskPayload encapsulating any Java object serialized to JSON.
     *
     * @param payloadOrder     sequence order
     * @param payloadContainer container Topic
     * @param payloadContent   content Topic
     * @param object           POJO object to serialize to JSON
     * @return constructed TaskPayload
     */
    public static ErgonPayload fromJsonObject(Integer payloadOrder, Topic payloadContainer, Topic payloadContent, Object object) {
        ErgonPayload ergonPayload = new ErgonPayload(payloadOrder, payloadContainer, payloadContent, (Task.TaskInputComponent) null);
        ergonPayload.setJsonObject(object);
        return ergonPayload;
    }

    // =========================================================================
    // FHIR Resource and JSON Object Helper Methods
    // =========================================================================

    /**
     * Checks if the payload contains a FHIR resource {@link Reference}.
     *
     * @return true if payload contains a Reference value
     */
    @JsonIgnore
    public boolean isFhirResource() {
        return payload != null && payload.hasValue() && (payload.getValue() instanceof Reference);
    }

    /**
     * Checks if the payload contains a JSON object (as StringType, Attachment, etc.).
     *
     * @return true if payload is not a FHIR Reference
     */
    @JsonIgnore
    public boolean isJsonObject() {
        return payload != null && payload.hasValue() && !(payload.getValue() instanceof Reference);
    }

    /**
     * Returns the FHIR {@link Reference} if this payload encapsulates a FHIR resource.
     *
     * @return Reference instance or null
     */
    @JsonIgnore
    public Reference getResourceReference() {
        if (isFhirResource()) {
            return (Reference) payload.getValue();
        }
        return null;
    }

    /**
     * Returns the FHIR resource reference string (e.g. "Patient/123") if present.
     *
     * @return resource reference string or null
     */
    @JsonProperty("resourceReference")
    public String getResourceReferenceString() {
        Reference ref = getResourceReference();
        return ref != null ? ref.getReference() : null;
    }

    /**
     * Sets the payload value as a FHIR {@link Reference}.
     *
     * @param reference FHIR Reference
     */
    public void setResourceReference(Reference reference) {
        ensurePayload();
        payload.setValue(reference);
        ensureTypeCoding(TASK_PAYLOAD_TYPE_FHIR_RESOURCE, "FHIR Resource Reference");
    }

    /**
     * Sets the payload value as a FHIR reference string.
     *
     * @param referenceString reference string (e.g. "Patient/123", "#comm-1")
     */
    @JsonProperty("resourceReference")
    public void setResourceReference(String referenceString) {
        if (StringUtils.isNotBlank(referenceString)) {
            Reference ref = new Reference(referenceString.trim());
            setResourceReference(ref);
        }
    }

    /**
     * Sets the payload value from a FHIR resource instance.
     *
     * @param resource FHIR resource
     */
    public void setResource(IBaseResource resource) {
        if (resource == null) {
            return;
        }
        ensurePayload();
        String refString = null;
        String display = null;
        if (resource instanceof Resource) {
            Resource r = (Resource) resource;
            String idPart = r.getIdPart();
            String fhirType = r.fhirType();
            if (StringUtils.isNotBlank(idPart)) {
                refString = fhirType + "/" + idPart;
            } else if (StringUtils.isNotBlank(r.getId())) {
                refString = r.getId();
            } else {
                refString = fhirType + "/" + UUID.randomUUID().toString();
            }
            display = fhirType + " resource";
        } else {
            refString = resource.getIdElement() != null && resource.getIdElement().hasIdPart()
                    ? resource.getIdElement().getResourceType() + "/" + resource.getIdElement().getIdPart()
                    : UUID.randomUUID().toString();
        }
        Reference ref = new Reference(refString);
        if (display != null) {
            ref.setDisplay(display);
        }
        setResourceReference(ref);
    }

    /**
     * Returns the payload content as a JSON string.
     *
     * @return JSON string representation or null
     */
    @JsonProperty("jsonData")
    public String getJsonString() {
        if (payload == null || !payload.hasValue()) {
            return null;
        }
        DataType val = payload.getValue();
        if (val instanceof StringType) {
            return ((StringType) val).getValue();
        } else if (val instanceof Attachment) {
            Attachment att = (Attachment) val;
            if (att.hasData()) {
                return new String(att.getData(), StandardCharsets.UTF_8);
            }
            if (att.hasTitle()) {
                return att.getTitle();
            }
        } else if (val instanceof Base64BinaryType) {
            byte[] bytes = ((Base64BinaryType) val).getValue();
            if (bytes != null) {
                return new String(bytes, StandardCharsets.UTF_8);
            }
        }
        return null;
    }

    /**
     * Returns the payload content parsed into a Jackson {@link JsonNode}.
     *
     * @return JsonNode or null if not valid JSON
     */
    @JsonIgnore
    public JsonNode getJsonNode() {
        String jsonStr = getJsonString();
        if (StringUtils.isBlank(jsonStr)) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readTree(jsonStr);
        } catch (Exception ignored) {
            return null;
        }
    }

    /**
     * Sets the payload value as a JSON string.
     *
     * @param jsonString JSON content string
     */
    @JsonProperty("jsonData")
    public void setJsonObject(String jsonString) {
        ensurePayload();
        if (jsonString != null) {
            payload.setValue(new StringType(jsonString));
            ensureTypeCoding(TASK_PAYLOAD_TYPE_JSON_OBJECT, "JSON Object Payload");
        } else {
            payload.setValue(null);
        }
    }

    /**
     * Sets the payload value as a Jackson {@link JsonNode}.
     *
     * @param jsonNode JsonNode instance
     */
    public void setJsonObject(JsonNode jsonNode) {
        if (jsonNode != null) {
            setJsonObject(jsonNode.toString());
        } else {
            setJsonObject((String) null);
        }
    }

    /**
     * Sets the payload value from an arbitrary object serialized to JSON.
     *
     * @param object POJO instance
     */
    public void setJsonObject(Object object) {
        if (object == null) {
            setJsonObject((String) null);
            return;
        }
        if (object instanceof String) {
            setJsonObject((String) object);
        } else if (object instanceof JsonNode) {
            setJsonObject((JsonNode) object);
        } else {
            try {
                setJsonObject(OBJECT_MAPPER.writeValueAsString(object));
            } catch (Exception e) {
                setJsonObject(String.valueOf(object));
            }
        }
    }

    private void ensurePayload() {
        if (this.payload == null) {
            this.payload = new Task.TaskInputComponent();
        }
    }

    private void ensureTypeCoding(String code, String display) {
        if (payload == null) return;
        CodeableConcept typeConcept = payload.getType();
        if (typeConcept == null) {
            typeConcept = new CodeableConcept();
            payload.setType(typeConcept);
        }
        if (StringUtils.isNotBlank(code)) {
            boolean hasCoding = typeConcept.hasCoding() && typeConcept.getCoding().stream()
                    .anyMatch(c -> Objects.equals(c.getSystem(), TASK_PAYLOAD_TYPE_SYSTEM) && Objects.equals(c.getCode(), code));
            if (!hasCoding) {
                Coding coding = typeConcept.addCoding();
                coding.setSystem(TASK_PAYLOAD_TYPE_SYSTEM);
                coding.setCode(code);
                coding.setDisplay(display);
            }
        }
        if (StringUtils.isNotBlank(display) && !typeConcept.hasText()) {
            typeConcept.setText(display);
        }
    }

    // =========================================================================
    // Task Input / Output Component Conversions
    // =========================================================================

    /**
     * Converts this TaskPayload into a fully populated {@link Task.TaskInputComponent},
     * encoding payloadOrder, payloadContainer, and payloadContent in standard FHIR extensions.
     *
     * @return populated TaskInputComponent
     */
    public Task.TaskInputComponent toTaskInput() {
        Task.TaskInputComponent inputComponent = payload != null ? payload.copy() : new Task.TaskInputComponent();

        if (payloadOrder != null) {
            inputComponent.removeExtension(EXTENSION_PAYLOAD_ORDER);
            inputComponent.addExtension(new Extension(EXTENSION_PAYLOAD_ORDER, new IntegerType(payloadOrder)));
        }

        if (payloadContainer != null) {
            inputComponent.removeExtension(EXTENSION_PAYLOAD_CONTAINER);
            inputComponent.addExtension(new Extension(EXTENSION_PAYLOAD_CONTAINER, new StringType(serializeTopic(payloadContainer))));
        }

        if (payloadContent != null) {
            inputComponent.removeExtension(EXTENSION_PAYLOAD_CONTENT);
            inputComponent.addExtension(new Extension(EXTENSION_PAYLOAD_CONTENT, new StringType(serializeTopic(payloadContent))));
        }

        return inputComponent;
    }

    /**
     * Alias for {@link #toTaskInput()}.
     */
    public Task.TaskInputComponent toTaskInputComponent() {
        return toTaskInput();
    }

    /**
     * Constructs a TaskPayload from a {@link Task.TaskInputComponent}, resolving extensions for
     * order, container topic, and content topic.
     *
     * @param inputComponent FHIR TaskInputComponent
     * @return populated TaskPayload
     */
    public static ErgonPayload fromTaskInput(Task.TaskInputComponent inputComponent) {
        if (inputComponent == null) {
            return new ErgonPayload();
        }

        ErgonPayload ergonPayload = new ErgonPayload();
        ergonPayload.setPayload(inputComponent.copy());

        if (inputComponent.hasExtension(EXTENSION_PAYLOAD_ORDER)) {
            Extension ext = inputComponent.getExtensionByUrl(EXTENSION_PAYLOAD_ORDER);
            if (ext != null && ext.getValue() instanceof IntegerType) {
                ergonPayload.setPayloadOrder(((IntegerType) ext.getValue()).getValue());
            }
        }

        if (inputComponent.hasExtension(EXTENSION_PAYLOAD_CONTAINER)) {
            Extension ext = inputComponent.getExtensionByUrl(EXTENSION_PAYLOAD_CONTAINER);
            if (ext != null && ext.getValue() instanceof StringType) {
                ergonPayload.setPayloadContainer(deserializeTopic(((StringType) ext.getValue()).getValue()));
            }
        }

        if (inputComponent.hasExtension(EXTENSION_PAYLOAD_CONTENT)) {
            Extension ext = inputComponent.getExtensionByUrl(EXTENSION_PAYLOAD_CONTENT);
            if (ext != null && ext.getValue() instanceof StringType) {
                ergonPayload.setPayloadContent(deserializeTopic(((StringType) ext.getValue()).getValue()));
            }
        }

        return ergonPayload;
    }

    /**
     * Converts this TaskPayload into a {@link Task.TaskOutputComponent},
     * encoding payloadOrder, payloadContainer, and payloadContent in standard FHIR extensions.
     *
     * @return populated TaskOutputComponent
     */
    public Task.TaskOutputComponent toTaskOutput() {
        Task.TaskOutputComponent outputComponent = new Task.TaskOutputComponent();
        if (payload != null) {
            if (payload.hasType()) {
                outputComponent.setType(payload.getType().copy());
            }
            if (payload.hasValue()) {
                outputComponent.setValue(payload.getValue().copy());
            }
        }

        if (payloadOrder != null) {
            outputComponent.addExtension(new Extension(EXTENSION_PAYLOAD_ORDER, new IntegerType(payloadOrder)));
        }

        if (payloadContainer != null) {
            outputComponent.addExtension(new Extension(EXTENSION_PAYLOAD_CONTAINER, new StringType(serializeTopic(payloadContainer))));
        }

        if (payloadContent != null) {
            outputComponent.addExtension(new Extension(EXTENSION_PAYLOAD_CONTENT, new StringType(serializeTopic(payloadContent))));
        }

        return outputComponent;
    }

    /**
     * Alias for {@link #toTaskOutput()}.
     */
    public Task.TaskOutputComponent toTaskOutputComponent() {
        return toTaskOutput();
    }

    /**
     * Constructs a TaskPayload from a {@link Task.TaskOutputComponent}.
     *
     * @param outputComponent FHIR TaskOutputComponent
     * @return populated TaskPayload
     */
    public static ErgonPayload fromTaskOutput(Task.TaskOutputComponent outputComponent) {
        if (outputComponent == null) {
            return new ErgonPayload();
        }

        ErgonPayload ergonPayload = new ErgonPayload();
        Task.TaskInputComponent input = new Task.TaskInputComponent();
        if (outputComponent.hasType()) {
            input.setType(outputComponent.getType().copy());
        }
        if (outputComponent.hasValue()) {
            input.setValue(outputComponent.getValue().copy());
        }
        ergonPayload.setPayload(input);

        if (outputComponent.hasExtension(EXTENSION_PAYLOAD_ORDER)) {
            Extension ext = outputComponent.getExtensionByUrl(EXTENSION_PAYLOAD_ORDER);
            if (ext != null && ext.getValue() instanceof IntegerType) {
                ergonPayload.setPayloadOrder(((IntegerType) ext.getValue()).getValue());
            }
        }

        if (outputComponent.hasExtension(EXTENSION_PAYLOAD_CONTAINER)) {
            Extension ext = outputComponent.getExtensionByUrl(EXTENSION_PAYLOAD_CONTAINER);
            if (ext != null && ext.getValue() instanceof StringType) {
                ergonPayload.setPayloadContainer(deserializeTopic(((StringType) ext.getValue()).getValue()));
            }
        }

        if (outputComponent.hasExtension(EXTENSION_PAYLOAD_CONTENT)) {
            Extension ext = outputComponent.getExtensionByUrl(EXTENSION_PAYLOAD_CONTENT);
            if (ext != null && ext.getValue() instanceof StringType) {
                ergonPayload.setPayloadContent(deserializeTopic(((StringType) ext.getValue()).getValue()));
            }
        }

        return ergonPayload;
    }

    // =========================================================================
    // Bulk Task Operations
    // =========================================================================

    /**
     * Extracts all {@link ErgonPayload} elements from {@link Task#getInput()}.
     *
     * @param task source Task
     * @return list of TaskPayload objects, ordered by payloadOrder
     */
    public static List<ErgonPayload> extractInputPayloads(Task task) {
        List<ErgonPayload> payloads = new ArrayList<>();
        if (task == null || !task.hasInput()) {
            return payloads;
        }
        int defaultOrder = 0;
        for (Task.TaskInputComponent input : task.getInput()) {
            ErgonPayload payload = fromTaskInput(input);
            if (payload.getPayloadOrder() == null) {
                payload.setPayloadOrder(defaultOrder++);
            }
            payloads.add(payload);
        }
        Collections.sort(payloads);
        return payloads;
    }

    /**
     * Extracts all {@link ErgonPayload} elements from {@link Task#getOutput()}.
     *
     * @param task source Task
     * @return list of TaskPayload objects, ordered by payloadOrder
     */
    public static List<ErgonPayload> extractOutputPayloads(Task task) {
        List<ErgonPayload> payloads = new ArrayList<>();
        if (task == null || !task.hasOutput()) {
            return payloads;
        }
        int defaultOrder = 0;
        for (Task.TaskOutputComponent output : task.getOutput()) {
            ErgonPayload payload = fromTaskOutput(output);
            if (payload.getPayloadOrder() == null) {
                payload.setPayloadOrder(defaultOrder++);
            }
            payloads.add(payload);
        }
        Collections.sort(payloads);
        return payloads;
    }

    /**
     * Applies a list of {@link ErgonPayload} objects to {@link Task#getInput()}.
     *
     * @param task     target Task
     * @param payloads list of TaskPayload objects
     */
    public static void applyInputPayloads(Task task, List<ErgonPayload> payloads) {
        if (task == null) return;
        task.getInput().clear();
        if (payloads != null) {
            for (int i = 0; i < payloads.size(); i++) {
                ErgonPayload p = payloads.get(i);
                if (p.getPayloadOrder() == null) {
                    p.setPayloadOrder(i);
                }
                task.addInput(p.toTaskInput());
            }
        }
    }

    /**
     * Applies a list of {@link ErgonPayload} objects to {@link Task#getOutput()}.
     *
     * @param task     target Task
     * @param payloads list of TaskPayload objects
     */
    public static void applyOutputPayloads(Task task, List<ErgonPayload> payloads) {
        if (task == null) return;
        task.getOutput().clear();
        if (payloads != null) {
            for (int i = 0; i < payloads.size(); i++) {
                ErgonPayload p = payloads.get(i);
                if (p.getPayloadOrder() == null) {
                    p.setPayloadOrder(i);
                }
                task.addOutput(p.toTaskOutput());
            }
        }
    }

    /**
     * Adds a {@link ErgonPayload} to {@link Task#getInput()}.
     *
     * @param task    target Task
     * @param payload TaskPayload to add
     * @return updated Task
     */
    public static Task addInputPayload(Task task, ErgonPayload payload) {
        if (task != null && payload != null) {
            if (payload.getPayloadOrder() == null) {
                payload.setPayloadOrder(task.getInput().size());
            }
            task.addInput(payload.toTaskInput());
        }
        return task;
    }

    /**
     * Adds a {@link ErgonPayload} to {@link Task#getOutput()}.
     *
     * @param task    target Task
     * @param payload TaskPayload to add
     * @return updated Task
     */
    public static Task addOutputPayload(Task task, ErgonPayload payload) {
        if (task != null && payload != null) {
            if (payload.getPayloadOrder() == null) {
                payload.setPayloadOrder(task.getOutput().size());
            }
            task.addOutput(payload.toTaskOutput());
        }
        return task;
    }

    // =========================================================================
    // Topic Serialization Helpers
    // =========================================================================

    private static String serializeTopic(Topic topic) {
        if (topic == null) return null;
        try {
            return OBJECT_MAPPER.writeValueAsString(topic);
        } catch (Exception e) {
            return topic.toTopicString();
        }
    }

    private static Topic deserializeTopic(String topicStr) {
        if (StringUtils.isBlank(topicStr)) return null;
        String clean = topicStr.trim();
        if (clean.startsWith("{")) {
            try {
                return OBJECT_MAPPER.readValue(clean, Topic.class);
            } catch (Exception ignored) {
            }
        }
        // Fallback: parse dot notation topic string e.g. "Health.HL7.2.4.ADT.A01"
        String[] parts = clean.split("\\.");
        if (parts.length >= 5) {
            return new Topic(parts[0], parts[1], parts[2], parts[3], parts[4]);
        }
        return Topic.fromHl7(clean, null);
    }

    // =========================================================================
    // Getters, Setters, Comparable, and Object methods
    // =========================================================================

    public Integer getPayloadOrder() {
        return payloadOrder;
    }

    public void setPayloadOrder(Integer payloadOrder) {
        this.payloadOrder = payloadOrder;
    }

    public Topic getPayloadContainer() {
        return payloadContainer;
    }

    public void setPayloadContainer(Topic payloadContainer) {
        this.payloadContainer = payloadContainer;
    }

    public Topic getPayloadContent() {
        return payloadContent;
    }

    public void setPayloadContent(Topic payloadContent) {
        this.payloadContent = payloadContent;
    }

    public Task.TaskInputComponent getPayload() {
        return payload;
    }

    public void setPayload(Task.TaskInputComponent payload) {
        this.payload = payload != null ? payload : new Task.TaskInputComponent();
    }

    @Override
    public int compareTo(ErgonPayload other) {
        if (other == null) return 1;
        if (this.payloadOrder == null && other.payloadOrder == null) return 0;
        if (this.payloadOrder == null) return 1;
        if (other.payloadOrder == null) return -1;
        return Integer.compare(this.payloadOrder, other.payloadOrder);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErgonPayload that = (ErgonPayload) o;
        return Objects.equals(payloadOrder, that.payloadOrder) &&
                Objects.equals(payloadContainer, that.payloadContainer) &&
                Objects.equals(payloadContent, that.payloadContent) &&
                Objects.equals(getResourceReferenceString(), that.getResourceReferenceString()) &&
                Objects.equals(getJsonString(), that.getJsonString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(payloadOrder, payloadContainer, payloadContent, getResourceReferenceString(), getJsonString());
    }

    @Override
    public String toString() {
        return "TaskPayload{" +
                "payloadOrder=" + payloadOrder +
                ", payloadContainer=" + (payloadContainer != null ? payloadContainer.toTopicString() : "null") +
                ", payloadContent=" + (payloadContent != null ? payloadContent.toTopicString() : "null") +
                ", isFhirResource=" + isFhirResource() +
                ", resourceReference='" + getResourceReferenceString() + '\'' +
                ", isJsonObject=" + isJsonObject() +
                '}';
    }
}
