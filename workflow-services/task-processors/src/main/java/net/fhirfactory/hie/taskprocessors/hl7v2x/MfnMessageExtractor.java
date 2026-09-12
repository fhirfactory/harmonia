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

package net.fhirfactory.hie.taskprocessors.hl7v2x;

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.*;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static net.fhirfactory.hie.taskprocessors.hl7v2x.common.Hl7v2ParsingSupport.isHl7Message;

/**
 * Extracts HL7 v2 MFN messages and corresponding origin input components from FHIR {@link Task} resources.
 */
public class MfnMessageExtractor {

    /**
     * Extracts the MFN message and corresponding origin TaskInputComponent from a Task.
     *
     * @param task     input Task
     * @param fallback optional fallback message
     * @return ExtractedInputResult with raw message and origin input component if available
     */
    public ExtractedInputResult extractMfnMessageFromTask(Task task, String fallback) {
        if (task == null) {
            return new ExtractedInputResult(fallback, null);
        }

        // 1. Search task.getInput()
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
                } else if (value instanceof Reference) {
                    Reference ref = (Reference) value;
                    String refStr = ref.getReference();
                    if (refStr != null) {
                        String targetId = refStr.startsWith("#") ? refStr.substring(1) : refStr.replace("Communication/", "").replace("Bundle/", "");
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

        // 2. Search task.getContained()
        if (task.hasContained()) {
            for (Resource res : task.getContained()) {
                if (res instanceof Communication) {
                    String rawFromComm = extractRawFromCommunication((Communication) res);
                    if (isHl7Message(rawFromComm)) {
                        return new ExtractedInputResult(rawFromComm, null);
                    }
                }
            }
        }

        // 3. Fallback
        if (StringUtils.isNotBlank(fallback) && isHl7Message(fallback)) {
            return new ExtractedInputResult(fallback, null);
        }

        if (task.hasDescription() && isHl7Message(task.getDescription())) {
            return new ExtractedInputResult(task.getDescription(), null);
        }

        return new ExtractedInputResult(fallback, null);
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
