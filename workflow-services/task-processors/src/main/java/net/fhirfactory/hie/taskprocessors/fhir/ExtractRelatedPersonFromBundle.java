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

package net.fhirfactory.hie.taskprocessors.fhir;

import ca.uhn.fhir.context.FhirContext;
import jakarta.enterprise.context.Dependent;
import net.fhirfactory.hie.model.task.HieTaskReason;
import net.fhirfactory.hie.taskprocessors.base.TaskProcessingActivity;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Task Processing Activity that parses {@code Task.input} for any FHIR {@link Bundle},
 * extracts all {@link RelatedPerson} resources contained within the Bundle(s), and adds each
 * extracted RelatedPerson individually to {@code Task.output}.
 */
@Dependent
public class ExtractRelatedPersonFromBundle extends TaskProcessingActivity {

    private static final Logger log = LoggerFactory.getLogger(ExtractRelatedPersonFromBundle.class);

    public static final String DEFAULT_ACTIVITY_ID = "extract-related-person-from-bundle";
    public static final String DEFAULT_ACTIVITY_NAME = "Extract RelatedPerson From Bundle Activity";

    public static final String HEADER_RELATED_PERSON_ID = "HIE_RELATED_PERSON_ID";
    public static final String HEADER_RELATED_PERSON_NAME = "HIE_RELATED_PERSON_NAME";
    public static final String HEADER_RELATED_PERSON_PATIENT = "HIE_RELATED_PERSON_PATIENT";
    public static final String HEADER_RELATED_PERSON_RELATIONSHIP = "HIE_RELATED_PERSON_RELATIONSHIP";
    public static final String HEADER_RELATED_PERSON_COUNT = "HIE_RELATED_PERSON_COUNT";

    private final FhirContext fhirContext;

    public ExtractRelatedPersonFromBundle() {
        super(DEFAULT_ACTIVITY_ID, DEFAULT_ACTIVITY_NAME);
        setActivityDescription("Parses Task.input for Bundle resources and extracts contained RelatedPerson resources to Task.output");
        this.fhirContext = FhirContext.forR5();
    }

    public ExtractRelatedPersonFromBundle(CamelContext context) {
        super(context, DEFAULT_ACTIVITY_ID, DEFAULT_ACTIVITY_NAME);
        setActivityDescription("Parses Task.input for Bundle resources and extracts contained RelatedPerson resources to Task.output");
        this.fhirContext = FhirContext.forR5();
    }

    public ExtractRelatedPersonFromBundle(String activityId, String activityName) {
        super(activityId, activityName);
        this.fhirContext = FhirContext.forR5();
    }

    public ExtractRelatedPersonFromBundle(CamelContext context, String activityId, String activityName) {
        super(context, activityId, activityName);
        this.fhirContext = FhirContext.forR5();
    }

    public ExtractRelatedPersonFromBundle(FhirContext fhirContext) {
        super(DEFAULT_ACTIVITY_ID, DEFAULT_ACTIVITY_NAME);
        setActivityDescription("Parses Task.input for Bundle resources and extracts contained RelatedPerson resources to Task.output");
        this.fhirContext = fhirContext != null ? fhirContext : FhirContext.forR5();
    }

    @Override
    protected void processActivity(Exchange exchange) throws Exception {
        processRelatedPersonResourceExtraction(exchange);
    }

    /**
     * Core processing logic that extracts the incoming {@link Task}, inspects its {@code Task.input}
     * for any {@link Bundle}, extracts all {@link RelatedPerson} resources, and populates {@code Task.output}.
     *
     * @param exchange Camel Exchange
     */
    public void processRelatedPersonResourceExtraction(Exchange exchange) {
        if (exchange == null || exchange.getMessage() == null) {
            log.warn("[{}] Null exchange or message received", getActivityName());
            return;
        }

        Object body = exchange.getMessage().getBody();
        Task task = null;

        if (body instanceof Task) {
            task = (Task) body;
        } else {
            Object incTask = exchange.getProperty(PROPERTY_INCOMING_TASK);
            if (incTask instanceof Task) {
                task = (Task) incTask;
            }
        }

        if (task == null) {
            task = new Task();
            String taskId = exchange.getMessage().getHeader(HEADER_TASK_ID, String.class);
            if (StringUtils.isBlank(taskId)) {
                taskId = "task-" + UUID.randomUUID().toString().substring(0, 8);
            }
            task.setId("Task/" + cleanId(taskId));
            task.setStatus(Task.TaskStatus.INPROGRESS);
            task.setAuthoredOn(new Date());
            HieTaskReason.HIE_SYNTHETIC_TASK.applyTo(task);
        }

        Task processedTask = extractRelatedPersonsToTaskOutput(task);

        // Update exchange headers from extracted related person(s)
        List<RelatedPerson> extractedRelatedPersons = extractRelatedPersonsFromTaskInputs(task);
        if (!extractedRelatedPersons.isEmpty()) {
            RelatedPerson firstRp = extractedRelatedPersons.get(0);
            String rpId = firstRp.getIdPart();
            String fullName = extractRelatedPersonFullName(firstRp);
            String patientRef = firstRp.hasPatient() ? firstRp.getPatient().getReference() : null;
            String relationship = extractRelatedPersonRelationship(firstRp);

            if (StringUtils.isNotBlank(rpId)) {
                exchange.getMessage().setHeader(HEADER_RELATED_PERSON_ID, rpId);
            }
            if (StringUtils.isNotBlank(fullName)) {
                exchange.getMessage().setHeader(HEADER_RELATED_PERSON_NAME, fullName);
            }
            if (StringUtils.isNotBlank(patientRef)) {
                exchange.getMessage().setHeader(HEADER_RELATED_PERSON_PATIENT, patientRef);
            }
            if (StringUtils.isNotBlank(relationship)) {
                exchange.getMessage().setHeader(HEADER_RELATED_PERSON_RELATIONSHIP, relationship);
            }
        }
        exchange.getMessage().setHeader(HEADER_RELATED_PERSON_COUNT, extractedRelatedPersons.size());

        exchange.getMessage().setBody(processedTask);
        log.info("[{}] Extracted {} RelatedPerson resource(s) from Bundle into Task/{} output",
                getActivityName(), extractedRelatedPersons.size(), processedTask.getIdPart());
    }

    /**
     * Parses the {@code Task.input} for any {@link Bundle}, extracts all {@link RelatedPerson} resources,
     * and sets each as an individual {@code Task.output} component.
     *
     * @param task input Task
     * @return updated Task with extracted RelatedPerson resources in Task.output
     */
    public Task extractRelatedPersonsToTaskOutput(Task task) {
        if (task == null) {
            task = new Task();
            task.setId("Task/" + UUID.randomUUID().toString());
            task.setStatus(Task.TaskStatus.INPROGRESS);
            task.setAuthoredOn(new Date());
        }

        List<RelatedPerson> extractedRelatedPersons = extractRelatedPersonsFromTaskInputs(task);

        task.getOutput().clear();

        for (int i = 0; i < extractedRelatedPersons.size(); i++) {
            RelatedPerson relatedPerson = extractedRelatedPersons.get(i);
            Task.TaskOutputComponent outputComp = task.addOutput();
            outputComp.getType().setText("RelatedPerson Resource").addCoding()
                    .setSystem("http://hl7.org/fhir/resource-types")
                    .setCode("RelatedPerson")
                    .setDisplay("RelatedPerson");

            String relatedPersonJson = fhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(relatedPerson);
            outputComp.setValue(new StringType(relatedPersonJson));
        }

        if (!extractedRelatedPersons.isEmpty()) {
            RelatedPerson firstRp = extractedRelatedPersons.get(0);
            if (firstRp.hasPatient() && StringUtils.isNotBlank(firstRp.getPatient().getReference())) {
                task.setFor(new Reference(firstRp.getPatient().getReference()).setDisplay(firstRp.getPatient().getDisplay()));
            }
        }

        task.setLastModified(new Date());
        HieTaskReason.ensureSyntheticTaskReason(task);

        return task;
    }

    /**
     * Extracts all {@link RelatedPerson} resources from any {@link Bundle} found in {@code task.getInput()}
     * or contained resources.
     *
     * @param task input Task
     * @return list of extracted RelatedPerson resources
     */
    public List<RelatedPerson> extractRelatedPersonsFromTaskInputs(Task task) {
        List<RelatedPerson> relatedPersons = new ArrayList<>();
        if (task == null) {
            return relatedPersons;
        }

        List<Bundle> bundles = extractBundlesFromTask(task);
        for (Bundle bundle : bundles) {
            relatedPersons.addAll(extractRelatedPersonsFromBundle(bundle));
        }

        return relatedPersons;
    }

    /**
     * Finds and parses all {@link Bundle} resources referenced or embedded in {@code task.getInput()}
     * and {@code task.getContained()}.
     *
     * @param task input Task
     * @return list of parsed Bundle resources
     */
    public List<Bundle> extractBundlesFromTask(Task task) {
        List<Bundle> bundles = new ArrayList<>();
        if (task == null) {
            return bundles;
        }

        if (task.hasInput()) {
            for (Task.TaskInputComponent input : task.getInput()) {
                if (input.hasValue()) {
                    DataType val = input.getValue();
                    if (val instanceof StringType) {
                        String strVal = ((StringType) val).getValue();
                        Bundle parsed = parseBundle(strVal);
                        if (parsed != null) {
                            bundles.add(parsed);
                        }
                    } else if (val instanceof Attachment) {
                        byte[] data = ((Attachment) val).getData();
                        if (data != null && data.length > 0) {
                            Bundle parsed = parseBundle(new String(data, StandardCharsets.UTF_8));
                            if (parsed != null) {
                                bundles.add(parsed);
                            }
                        }
                    } else if (val instanceof Base64BinaryType) {
                        byte[] data = ((Base64BinaryType) val).getValue();
                        if (data != null && data.length > 0) {
                            Bundle parsed = parseBundle(new String(data, StandardCharsets.UTF_8));
                            if (parsed != null) {
                                bundles.add(parsed);
                            }
                        }
                    } else if (val instanceof Reference) {
                        Reference ref = (Reference) val;
                        String refStr = ref.getReference();
                        if (StringUtils.isNotBlank(refStr)) {
                            String targetId = refStr.startsWith("#") ? refStr.substring(1) : refStr.replace("Bundle/", "").replace("Communication/", "");
                            for (Resource res : task.getContained()) {
                                if (Objects.equals(res.getIdPart(), targetId) || Objects.equals(res.getId(), refStr)) {
                                    if (res instanceof Bundle) {
                                        bundles.add((Bundle) res);
                                    } else if (res instanceof Communication) {
                                        String raw = extractRawFromCommunication((Communication) res);
                                        Bundle parsed = parseBundle(raw);
                                        if (parsed != null) {
                                            bundles.add(parsed);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Fallback: inspect contained resources for Bundles if not already added
        if (task.hasContained()) {
            for (Resource res : task.getContained()) {
                if (res instanceof Bundle && !bundles.contains(res)) {
                    bundles.add((Bundle) res);
                }
            }
        }

        return bundles;
    }

    /**
     * Recursively extracts all {@link RelatedPerson} resources from a {@link Bundle}.
     *
     * @param bundle FHIR Bundle
     * @return list of RelatedPerson resources
     */
    public List<RelatedPerson> extractRelatedPersonsFromBundle(Bundle bundle) {
        List<RelatedPerson> relatedPersons = new ArrayList<>();
        if (bundle == null || !bundle.hasEntry()) {
            return relatedPersons;
        }

        for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {
            if (entry.hasResource()) {
                Resource res = entry.getResource();
                if (res instanceof RelatedPerson) {
                    relatedPersons.add((RelatedPerson) res);
                } else if (res instanceof Bundle) {
                    relatedPersons.addAll(extractRelatedPersonsFromBundle((Bundle) res));
                }
            }
        }

        return relatedPersons;
    }

    /**
     * Parses a raw String payload into a FHIR {@link Bundle}.
     *
     * @param payload raw JSON or XML string
     * @return parsed Bundle, or {@code null} if parsing fails or payload is not a Bundle
     */
    public Bundle parseBundle(String payload) {
        if (StringUtils.isBlank(payload)) {
            return null;
        }

        String trimmed = payload.trim();
        try {
            if (trimmed.startsWith("{")) {
                IBaseResource res = fhirContext.newJsonParser().parseResource(trimmed);
                if (res instanceof Bundle) {
                    return (Bundle) res;
                }
            } else if (trimmed.startsWith("<")) {
                IBaseResource res = fhirContext.newXmlParser().parseResource(trimmed);
                if (res instanceof Bundle) {
                    return (Bundle) res;
                }
            } else {
                try {
                    IBaseResource res = fhirContext.newJsonParser().parseResource(trimmed);
                    if (res instanceof Bundle) {
                        return (Bundle) res;
                    }
                } catch (Exception e) {
                    IBaseResource res = fhirContext.newXmlParser().parseResource(trimmed);
                    if (res instanceof Bundle) {
                        return (Bundle) res;
                    }
                }
            }
        } catch (Exception e) {
            log.debug("[{}] Payload could not be parsed as FHIR Bundle: {}", getActivityName(), e.getMessage());
        }

        return null;
    }

    /**
     * Extracts raw text from a {@link Communication} payload.
     */
    private String extractRawFromCommunication(Communication communication) {
        if (communication == null || !communication.hasPayload()) {
            return null;
        }
        for (Communication.CommunicationPayloadComponent payload : communication.getPayload()) {
            if (payload.hasContent()) {
                DataType content = payload.getContent();
                if (content instanceof Attachment) {
                    byte[] bytes = ((Attachment) content).getData();
                    if (bytes != null) {
                        return new String(bytes, StandardCharsets.UTF_8);
                    }
                } else if (content instanceof StringType) {
                    return ((StringType) content).getValue();
                }
            }
        }
        return null;
    }

    /**
     * Extracts full name representation from a {@link RelatedPerson}.
     */
    public String extractRelatedPersonFullName(RelatedPerson relatedPerson) {
        if (relatedPerson == null || !relatedPerson.hasName()) {
            return "";
        }
        HumanName name = relatedPerson.getNameFirstRep();
        if (name.hasText()) {
            return name.getText();
        }
        List<String> parts = new ArrayList<>();
        if (name.hasGiven()) {
            for (StringType given : name.getGiven()) {
                parts.add(given.getValue());
            }
        }
        if (name.hasFamily()) {
            parts.add(name.getFamily());
        }
        return String.join(" ", parts).trim();
    }

    /**
     * Extracts relationship string description/code from a {@link RelatedPerson}.
     */
    public String extractRelatedPersonRelationship(RelatedPerson relatedPerson) {
        if (relatedPerson == null || !relatedPerson.hasRelationship()) {
            return "";
        }
        CodeableConcept cc = relatedPerson.getRelationshipFirstRep();
        if (cc.hasText()) {
            return cc.getText();
        }
        if (cc.hasCoding()) {
            Coding c = cc.getCodingFirstRep();
            if (c.hasDisplay()) {
                return c.getDisplay();
            }
            if (c.hasCode()) {
                return c.getCode();
            }
        }
        return "";
    }

    public FhirContext getFhirContext() {
        return fhirContext;
    }
}
