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

package net.fhirfactory.harmonia.erga.hl7v2x;

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.*;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static net.fhirfactory.harmonia.erga.hl7v2x.common.Hl7v2ParsingSupport.isHl7Message;

/**
 * Extracts HL7 v2 ADT messages and corresponding origin input components from FHIR {@link Task} resources.
 */
public class AdtMessageExtractor {

    /**
     * Extracts the ADT message and corresponding origin TaskInputComponent from a Task.
     * Prioritizes first resolving the {@link Communication} resource contained/referenced in the Task,
     * and then extracting the raw HL7 v2 message from the Communication payload.
     *
     * @param task     input Task
     * @param fallback optional fallback message
     * @return ExtractedInputResult with raw message and origin input component if available
     */
    public ExtractedInputResult extractAdtMessageFromTask(Task task, String fallback) {
        if (task == null) {
            return new ExtractedInputResult(fallback, null);
        }

        // 1. First, search task.getInput() for Reference to contained Communication resource
        if (task.hasInput()) {
            for (Task.TaskInputComponent input : task.getInput()) {
                if (input.hasValue() && input.getValue() instanceof Reference) {
                    Reference ref = (Reference) input.getValue();
                    String refStr = ref.getReference();
                    if (StringUtils.isNotBlank(refStr)) {
                        String targetId = refStr.startsWith("#") ? refStr.substring(1) : refStr.replace("Communication/", "").replace("Bundle/", "");
                        if (task.hasContained()) {
                            for (Resource res : task.getContained()) {
                                if (Objects.equals(res.getIdPart(), targetId) || Objects.equals(res.getId(), refStr)) {
                                    if (res instanceof Communication) {
                                        String rawFromComm = extractRawFromCommunication((Communication) res);
                                        if (isHl7Message(rawFromComm)) {
                                            return new ExtractedInputResult(rawFromComm, input);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 2. Search task.getContained() directly for any contained Communication resource
        if (task.hasContained()) {
            for (Resource res : task.getContained()) {
                if (res instanceof Communication) {
                    String rawFromComm = extractRawFromCommunication((Communication) res);
                    if (isHl7Message(rawFromComm)) {
                        Task.TaskInputComponent matchingInput = findMatchingInputForResource(task, res);
                        return new ExtractedInputResult(rawFromComm, matchingInput);
                    }
                }
            }
        }

        // 3. Search other task.getInput() value types (StringType, Attachment, Base64BinaryType)
        if (task.hasInput()) {
            for (Task.TaskInputComponent input : task.getInput()) {
                DataType value = input.getValue();
                if (value instanceof StringType) {
                    String str = ((StringType) value).getValue();
                    if (isHl7Message(str)) {
                        return new ExtractedInputResult(str, input);
                    }
                } else if (value instanceof Attachment) {
                    byte[] data = ((Attachment) value).getData();
                    if (data != null) {
                        String str = new String(data, StandardCharsets.UTF_8);
                        if (isHl7Message(str)) {
                            return new ExtractedInputResult(str, input);
                        }
                    }
                } else if (value instanceof Base64BinaryType) {
                    byte[] data = ((Base64BinaryType) value).getValue();
                    if (data != null) {
                        String str = new String(data, StandardCharsets.UTF_8);
                        if (isHl7Message(str)) {
                            return new ExtractedInputResult(str, input);
                        }
                    }
                }
            }
        }

        // 4. Fallback
        if (StringUtils.isNotBlank(fallback) && isHl7Message(fallback)) {
            return new ExtractedInputResult(fallback, null);
        }

        if (task.hasDescription() && isHl7Message(task.getDescription())) {
            return new ExtractedInputResult(task.getDescription(), null);
        }

        return new ExtractedInputResult(fallback, null);
    }

    /**
     * Extracts the {@link Communication} resource referenced by Task.input or contained within the Task.
     *
     * @param task input Task
     * @return Communication resource if found, or null
     */
    public Communication extractCommunicationFromTask(Task task) {
        if (task == null) {
            return null;
        }

        // 1. Check Task.input references to contained Communication
        if (task.hasInput()) {
            for (Task.TaskInputComponent input : task.getInput()) {
                if (input.hasValue() && input.getValue() instanceof Reference) {
                    Reference ref = (Reference) input.getValue();
                    String refStr = ref.getReference();
                    if (StringUtils.isNotBlank(refStr)) {
                        String targetId = refStr.startsWith("#") ? refStr.substring(1) : refStr.replace("Communication/", "").replace("Bundle/", "");
                        if (task.hasContained()) {
                            for (Resource res : task.getContained()) {
                                if (res instanceof Communication && (Objects.equals(res.getIdPart(), targetId) || Objects.equals(res.getId(), refStr))) {
                                    return (Communication) res;
                                }
                            }
                        }
                    }
                }
            }
        }

        // 2. Fallback: Search contained resources directly for any Communication
        if (task.hasContained()) {
            for (Resource res : task.getContained()) {
                if (res instanceof Communication) {
                    return (Communication) res;
                }
            }
        }

        return null;
    }

    /**
     * Helper to find matching Task.TaskInputComponent referencing a given contained resource.
     */
    private Task.TaskInputComponent findMatchingInputForResource(Task task, Resource resource) {
        if (task == null || !task.hasInput() || resource == null) {
            return null;
        }
        for (Task.TaskInputComponent input : task.getInput()) {
            if (input.hasValue() && input.getValue() instanceof Reference) {
                Reference ref = (Reference) input.getValue();
                String refStr = ref.getReference();
                if (StringUtils.isNotBlank(refStr)) {
                    String targetId = refStr.startsWith("#") ? refStr.substring(1) : refStr.replace("Communication/", "").replace("Bundle/", "");
                    if (Objects.equals(resource.getIdPart(), targetId) || Objects.equals(resource.getId(), refStr)) {
                        return input;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Extracts raw message text from a Communication payload.
     */
    public String extractRawFromCommunication(Communication communication) {
        if (communication == null || !communication.hasPayload()) return null;
        for (Communication.CommunicationPayloadComponent payload : communication.getPayload()) {
            if (payload.hasContent()) {
                DataType content = payload.getContent();
                if (content instanceof Attachment) {
                    byte[] bytes = ((Attachment) content).getData();
                    if (bytes != null) return new String(bytes, StandardCharsets.UTF_8);
                } else if (content instanceof StringType) {
                    return ((StringType) content).getValue();
                }
            }
        }
        return null;
    }

    /**
     * Helper holder for extracted input result.
     */
    public static class ExtractedInputResult {
        private final String rawMessage;
        private final Task.TaskInputComponent originInput;

        public ExtractedInputResult(String rawMessage, Task.TaskInputComponent originInput) {
            this.rawMessage = rawMessage;
            this.originInput = originInput;
        }

        public String getRawMessage() {
            return rawMessage;
        }

        public Task.TaskInputComponent getOriginInput() {
            return originInput;
        }
    }
}
