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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package net.fhirfactory.harmonia.model.core.domain.fhir;

import net.fhirfactory.harmonia.model.core.naming.factory.TopicFactory;
import net.fhirfactory.harmonia.model.core.workflow.tasks.ErgonPayload;
import net.fhirfactory.harmonia.model.core.workflow.tasks.valuesets.ErgonReasonEnum;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.fhirfactory.harmonia.model.core.naming.Topic;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static net.fhirfactory.harmonia.model.core.workflow.tasks.valuesets.ErgonPayloadConstants.*;

/**
 * Bi-directional converter between {@link ErgonPayload} domain models and FHIR R5 {@link Task} input/output components.
 */
public final class ErgonPayloadFhirConverter {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private ErgonPayloadFhirConverter() {
        // Utility class
    }

    public static void setResource(ErgonPayload ergonPayload, IBaseResource resource) {
        if (ergonPayload == null || resource == null) {
            return;
        }
        String refString = null;
        String display = null;
        if (resource instanceof Resource r) {
            String idPart = r.getIdPart();
            String fhirType = r.fhirType();
            if (StringUtils.isNotBlank(idPart)) {
                refString = fhirType + "/" + idPart;
            } else if (StringUtils.isNotBlank(r.getId())) {
                refString = r.getId();
            } else {
                refString = fhirType + "/" + java.util.UUID.randomUUID();
            }
            display = fhirType + " resource";
            net.fhirfactory.harmonia.model.core.security.FhirSecurityTagManager.applyDefaultSecurityTag(r);
        } else {
            refString = resource.getIdElement() != null && resource.getIdElement().hasIdPart()
                    ? resource.getIdElement().getResourceType() + "/" + resource.getIdElement().getIdPart()
                    : java.util.UUID.randomUUID().toString();
        }
        Reference ref = new Reference(refString);
        if (display != null) {
            ref.setDisplay(display);
        }
        if (resource instanceof Resource r) {
            ref.setResource(r);
        }
        ergonPayload.setResourceReference(ref);
    }

    public static Task.TaskInputComponent toTaskInput(ErgonPayload ergonPayload) {
        if (ergonPayload == null) {
            return new Task.TaskInputComponent();
        }
        Task.TaskInputComponent inputComponent = new Task.TaskInputComponent();
        Task.TaskInputComponent payload = ergonPayload.getPayload();
        if (payload != null) {
            if (payload.hasType()) {
                inputComponent.setType(payload.getType().copy());
            }
            if (payload.hasValue()) {
                DataType val = payload.getValue();
                if (val instanceof Reference) {
                    Reference ref = (Reference) val;
                    Reference copy = ref.copy();
                    if (ref.getResource() != null) {
                        copy.setResource(ref.getResource());
                    }
                    inputComponent.setValue(copy);
                } else {
                    inputComponent.setValue(val.copy());
                }
            }
        }

        if (ergonPayload.getPayloadOrder() != null) {
            inputComponent.addExtension(new Extension(EXTENSION_PAYLOAD_ORDER, new IntegerType(ergonPayload.getPayloadOrder())));
        }

        if (ergonPayload.getPayloadContainer() != null) {
            inputComponent.addExtension(new Extension(EXTENSION_PAYLOAD_CONTAINER, new StringType(serializeTopic(ergonPayload.getPayloadContainer()))));
        }

        if (ergonPayload.getPayloadContent() != null) {
            inputComponent.addExtension(new Extension(EXTENSION_PAYLOAD_CONTENT, new StringType(serializeTopic(ergonPayload.getPayloadContent()))));
        }

        return inputComponent;
    }

    public static ErgonPayload fromTaskInput(Task.TaskInputComponent inputComponent) {
        if (inputComponent == null) {
            return new ErgonPayload();
        }

        ErgonPayload ergonPayload = new ErgonPayload();
        Task.TaskInputComponent copy = new Task.TaskInputComponent();
        if (inputComponent.hasType()) {
            copy.setType(inputComponent.getType().copy());
        }
        if (inputComponent.hasValue()) {
            DataType val = inputComponent.getValue();
            if (val instanceof Reference) {
                Reference ref = (Reference) val;
                Reference refCopy = ref.copy();
                if (ref.getResource() != null) {
                    refCopy.setResource(ref.getResource());
                }
                copy.setValue(refCopy);
            } else {
                copy.setValue(val.copy());
            }
        }
        ergonPayload.setPayload(copy);

        if (inputComponent.hasExtension(EXTENSION_PAYLOAD_ORDER)) {
            Extension ext = inputComponent.getExtensionByUrl(EXTENSION_PAYLOAD_ORDER);
            if (ext != null && ext.getValue() instanceof IntegerType) {
                ergonPayload.setPayloadOrder(((IntegerType) ext.getValue()).getValue());
            }
        } else if (inputComponent.hasExtension(LEGACY_EXTENSION_PAYLOAD_ORDER)) {
            Extension ext = inputComponent.getExtensionByUrl(LEGACY_EXTENSION_PAYLOAD_ORDER);
            if (ext != null && ext.getValue() instanceof IntegerType) {
                ergonPayload.setPayloadOrder(((IntegerType) ext.getValue()).getValue());
            }
        }

        if (inputComponent.hasExtension(EXTENSION_PAYLOAD_CONTAINER)) {
            Extension ext = inputComponent.getExtensionByUrl(EXTENSION_PAYLOAD_CONTAINER);
            if (ext != null && ext.getValue() instanceof StringType) {
                ergonPayload.setPayloadContainer(deserializeTopic(((StringType) ext.getValue()).getValue()));
            }
        } else if (inputComponent.hasExtension(LEGACY_EXTENSION_PAYLOAD_CONTAINER)) {
            Extension ext = inputComponent.getExtensionByUrl(LEGACY_EXTENSION_PAYLOAD_CONTAINER);
            if (ext != null && ext.getValue() instanceof StringType) {
                ergonPayload.setPayloadContainer(deserializeTopic(((StringType) ext.getValue()).getValue()));
            }
        }

        if (inputComponent.hasExtension(EXTENSION_PAYLOAD_CONTENT)) {
            Extension ext = inputComponent.getExtensionByUrl(EXTENSION_PAYLOAD_CONTENT);
            if (ext != null && ext.getValue() instanceof StringType) {
                ergonPayload.setPayloadContent(deserializeTopic(((StringType) ext.getValue()).getValue()));
            }
        } else if (inputComponent.hasExtension(LEGACY_EXTENSION_PAYLOAD_CONTENT)) {
            Extension ext = inputComponent.getExtensionByUrl(LEGACY_EXTENSION_PAYLOAD_CONTENT);
            if (ext != null && ext.getValue() instanceof StringType) {
                ergonPayload.setPayloadContent(deserializeTopic(((StringType) ext.getValue()).getValue()));
            }
        }

        return ergonPayload;
    }

    public static Task.TaskOutputComponent toTaskOutput(ErgonPayload ergonPayload) {
        Task.TaskOutputComponent outputComponent = new Task.TaskOutputComponent();
        if (ergonPayload == null) {
            return outputComponent;
        }
        Task.TaskInputComponent payload = ergonPayload.getPayload();
        if (payload != null) {
            if (payload.hasType()) {
                outputComponent.setType(payload.getType().copy());
            }
            if (payload.hasValue()) {
                DataType val = payload.getValue();
                if (val instanceof Reference) {
                    Reference ref = (Reference) val;
                    Reference copy = ref.copy();
                    if (ref.getResource() != null) {
                        copy.setResource(ref.getResource());
                    }
                    outputComponent.setValue(copy);
                } else {
                    outputComponent.setValue(val.copy());
                }
            }
        }

        if (ergonPayload.getPayloadOrder() != null) {
            outputComponent.addExtension(new Extension(EXTENSION_PAYLOAD_ORDER, new IntegerType(ergonPayload.getPayloadOrder())));
        }

        if (ergonPayload.getPayloadContainer() != null) {
            outputComponent.addExtension(new Extension(EXTENSION_PAYLOAD_CONTAINER, new StringType(serializeTopic(ergonPayload.getPayloadContainer()))));
        }

        if (ergonPayload.getPayloadContent() != null) {
            outputComponent.addExtension(new Extension(EXTENSION_PAYLOAD_CONTENT, new StringType(serializeTopic(ergonPayload.getPayloadContent()))));
        }

        return outputComponent;
    }

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
            DataType val = outputComponent.getValue();
            if (val instanceof Reference) {
                Reference ref = (Reference) val;
                Reference copy = ref.copy();
                if (ref.getResource() != null) {
                    copy.setResource(ref.getResource());
                }
                input.setValue(copy);
            } else {
                input.setValue(val.copy());
            }
        }
        ergonPayload.setPayload(input);

        if (outputComponent.hasExtension(EXTENSION_PAYLOAD_ORDER)) {
            Extension ext = outputComponent.getExtensionByUrl(EXTENSION_PAYLOAD_ORDER);
            if (ext != null && ext.getValue() instanceof IntegerType) {
                ergonPayload.setPayloadOrder(((IntegerType) ext.getValue()).getValue());
            }
        } else if (outputComponent.hasExtension(LEGACY_EXTENSION_PAYLOAD_ORDER)) {
            Extension ext = outputComponent.getExtensionByUrl(LEGACY_EXTENSION_PAYLOAD_ORDER);
            if (ext != null && ext.getValue() instanceof IntegerType) {
                ergonPayload.setPayloadOrder(((IntegerType) ext.getValue()).getValue());
            }
        }

        if (outputComponent.hasExtension(EXTENSION_PAYLOAD_CONTAINER)) {
            Extension ext = outputComponent.getExtensionByUrl(EXTENSION_PAYLOAD_CONTAINER);
            if (ext != null && ext.getValue() instanceof StringType) {
                ergonPayload.setPayloadContainer(deserializeTopic(((StringType) ext.getValue()).getValue()));
            }
        } else if (outputComponent.hasExtension(LEGACY_EXTENSION_PAYLOAD_CONTAINER)) {
            Extension ext = outputComponent.getExtensionByUrl(LEGACY_EXTENSION_PAYLOAD_CONTAINER);
            if (ext != null && ext.getValue() instanceof StringType) {
                ergonPayload.setPayloadContainer(deserializeTopic(((StringType) ext.getValue()).getValue()));
            }
        }

        if (outputComponent.hasExtension(EXTENSION_PAYLOAD_CONTENT)) {
            Extension ext = outputComponent.getExtensionByUrl(EXTENSION_PAYLOAD_CONTENT);
            if (ext != null && ext.getValue() instanceof StringType) {
                ergonPayload.setPayloadContent(deserializeTopic(((StringType) ext.getValue()).getValue()));
            }
        } else if (outputComponent.hasExtension(LEGACY_EXTENSION_PAYLOAD_CONTENT)) {
            Extension ext = outputComponent.getExtensionByUrl(LEGACY_EXTENSION_PAYLOAD_CONTENT);
            if (ext != null && ext.getValue() instanceof StringType) {
                ergonPayload.setPayloadContent(deserializeTopic(((StringType) ext.getValue()).getValue()));
            }
        }

        return ergonPayload;
    }

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

    public static Task addInputPayload(Task task, ErgonPayload payload) {
        if (task != null && payload != null) {
            if (payload.getPayloadOrder() == null) {
                payload.setPayloadOrder(task.getInput().size());
            }
            task.addInput(payload.toTaskInput());
        }
        return task;
    }

    public static Task addOutputPayload(Task task, ErgonPayload payload) {
        if (task != null && payload != null) {
            if (payload.getPayloadOrder() == null) {
                payload.setPayloadOrder(task.getOutput().size());
            }
            task.addOutput(payload.toTaskOutput());
        }
        return task;
    }

    public static String serializeTopic(Topic topic) {
        if (topic == null) return null;
        try {
            return OBJECT_MAPPER.writeValueAsString(topic);
        } catch (Exception e) {
            return topic.toTopicString();
        }
    }

    public static Topic deserializeTopic(String str) {
        if (StringUtils.isBlank(str)) return null;
        try {
            return OBJECT_MAPPER.readValue(str, Topic.class);
        } catch (Exception e) {
            return TopicFactory.fromName(str);
        }
    }
}
