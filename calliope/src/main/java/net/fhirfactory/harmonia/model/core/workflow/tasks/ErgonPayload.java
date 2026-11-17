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

package net.fhirfactory.harmonia.model.core.workflow.tasks;
import net.fhirfactory.harmonia.model.core.domain.fhir.ErgonPayloadFhirConverter;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.fhirfactory.harmonia.model.core.naming.Topic;
import net.fhirfactory.harmonia.model.core.security.FhirSecurityTagManager;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.*;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static net.fhirfactory.harmonia.model.core.workflow.tasks.valuesets.ErgonPayloadConstants.*;

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
            this.payloadContainer = other.payloadContainer;
            this.payloadContent = other.payloadContent;
            this.payload = other.payload != null ? other.payload.copy() : new Task.TaskInputComponent();
        } else {
            this.payload = new Task.TaskInputComponent();
        }
    }


    // =========================================================================
    // FHIR Resource and JSON Object Helper Methods
    // =========================================================================

    @JsonIgnore
    public boolean isFhirResource() {
        return payload != null && payload.hasValue() && (payload.getValue() instanceof Reference);
    }

    @JsonIgnore
    public boolean isJsonObject() {
        return payload != null && payload.hasValue() && !(payload.getValue() instanceof Reference);
    }

    @JsonIgnore
    public Reference getResourceReference() {
        return isFhirResource() ? (Reference) payload.getValue() : null;
    }

    @JsonProperty("resourceReference")
    public String getResourceReferenceString() {
        Reference ref = getResourceReference();
        return ref != null ? ref.getReference() : null;
    }

    public void setResourceReference(Reference reference) {
        ensurePayload();
        payload.setValue(reference);
        ensureTypeCoding(TASK_PAYLOAD_TYPE_FHIR_RESOURCE, "FHIR Resource Reference");
    }

    @JsonProperty("resourceReference")
    public void setResourceReference(String referenceString) {
        if (StringUtils.isNotBlank(referenceString)) {
            setResourceReference(new Reference(referenceString.trim()));
        }
    }

    /**
     * Sets the payload value from a FHIR resource instance.
     *
     * @param resource FHIR resource
     */
    public void setResource(IBaseResource resource) {
        ErgonPayloadFhirConverter.setResource(this, resource);
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
        return ErgonPayloadFhirConverter.toTaskInput(this);
    }


    public Task.TaskOutputComponent toTaskOutput() {
        return ErgonPayloadFhirConverter.toTaskOutput(this);
    }


    // =========================================================================
    // Getters, Setters, Comparable, and Object methods
    // =========================================================================

    public Integer getPayloadOrder() { return payloadOrder; }
    public void setPayloadOrder(Integer payloadOrder) { this.payloadOrder = payloadOrder; }

    public Topic getPayloadContainer() { return payloadContainer; }
    public void setPayloadContainer(Topic payloadContainer) { this.payloadContainer = payloadContainer; }

    public Topic getPayloadContent() { return payloadContent; }
    public void setPayloadContent(Topic payloadContent) { this.payloadContent = payloadContent; }

    public Task.TaskInputComponent getPayload() { return payload; }
    public void setPayload(Task.TaskInputComponent payload) { this.payload = payload != null ? payload : new Task.TaskInputComponent(); }

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
                ", hasResourceReference=" + (getResourceReferenceString() != null) +
                ", isJsonObject=" + isJsonObject() +
                '}';
    }
}
