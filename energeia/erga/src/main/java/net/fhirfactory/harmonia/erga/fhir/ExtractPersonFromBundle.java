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

package net.fhirfactory.harmonia.erga.fhir;

import ca.uhn.fhir.context.FhirContext;
import jakarta.enterprise.context.Dependent;
import net.fhirfactory.harmonia.model.ergon.ErgonPayload;
import net.fhirfactory.harmonia.model.ergon.ErgonReasonEnum;
import net.fhirfactory.harmonia.erga.base.ErgonBase;
import net.fhirfactory.harmonia.model.pragma.Pragma;
import net.fhirfactory.harmonia.model.pragma.PragmaFhirConverter;
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
 * extracts all {@link Person} resources contained within the Bundle(s), and adds each
 * extracted Person individually to {@code Task.output}.
 */
@Dependent
public class ExtractPersonFromBundle extends ErgonBase {

    private static final Logger log = LoggerFactory.getLogger(ExtractPersonFromBundle.class);

    public static final String DEFAULT_ACTIVITY_ID = "extract-person-from-bundle";
    public static final String DEFAULT_ACTIVITY_NAME = "Extract Person From Bundle Activity";

    public static final String HEADER_PERSON_ID = "HIE_PERSON_ID";
    public static final String HEADER_PERSON_NAME = "HIE_PERSON_NAME";
    public static final String HEADER_PERSON_IDENTIFIER = "HIE_PERSON_IDENTIFIER";
    public static final String HEADER_PERSON_COUNT = "HIE_PERSON_COUNT";

    private final FhirContext fhirContext;

    public ExtractPersonFromBundle() {
        super(DEFAULT_ACTIVITY_ID, DEFAULT_ACTIVITY_NAME);
        setActivityDescription("Parses Task.input for Bundle resources and extracts contained Person resources to Task.output");
        this.fhirContext = FhirContext.forR5();
    }

    public ExtractPersonFromBundle(CamelContext context) {
        super(context, DEFAULT_ACTIVITY_ID, DEFAULT_ACTIVITY_NAME);
        setActivityDescription("Parses Task.input for Bundle resources and extracts contained Person resources to Task.output");
        this.fhirContext = FhirContext.forR5();
    }

    public ExtractPersonFromBundle(String activityId, String activityName) {
        super(activityId, activityName);
        this.fhirContext = FhirContext.forR5();
    }

    public ExtractPersonFromBundle(CamelContext context, String activityId, String activityName) {
        super(context, activityId, activityName);
        this.fhirContext = FhirContext.forR5();
    }

    public ExtractPersonFromBundle(FhirContext fhirContext) {
        super(DEFAULT_ACTIVITY_ID, DEFAULT_ACTIVITY_NAME);
        setActivityDescription("Parses Task.input for Bundle resources and extracts contained Person resources to Task.output");
        this.fhirContext = fhirContext != null ? fhirContext : FhirContext.forR5();
    }

    @Override
    protected void processActivity(Exchange exchange) throws Exception {
        super.processActivity(exchange);
    }

    @Override
    protected void processErgon(Pragma pragma, Exchange exchange) throws Exception {
        if (pragma == null) {
            return;
        }

        Task task = PragmaFhirConverter.toFhirTask(pragma);
        Task processedTask = extractPersonsToTaskOutput(task);

        pragma.getOutput().clear();
        if (processedTask != null && processedTask.hasOutput()) {
            for (Task.TaskOutputComponent outComp : processedTask.getOutput()) {
                pragma.addOutput(ErgonPayload.fromTaskOutput(outComp));
            }
        }

        List<Person> extractedPersons = extractPersonsFromTaskInputs(task);
        if (!extractedPersons.isEmpty()) {
            Person firstPerson = extractedPersons.get(0);
            String personId = firstPerson.getIdPart();
            String fullName = extractPersonFullName(firstPerson);
            String identifier = extractPersonIdentifier(firstPerson);

            if (StringUtils.isNotBlank(personId)) {
                exchange.getMessage().setHeader(HEADER_PERSON_ID, personId);
            }
            if (StringUtils.isNotBlank(fullName)) {
                exchange.getMessage().setHeader(HEADER_PERSON_NAME, fullName);
            }
            if (StringUtils.isNotBlank(identifier)) {
                exchange.getMessage().setHeader(HEADER_PERSON_IDENTIFIER, identifier);
            }
        }
        exchange.getMessage().setHeader(HEADER_PERSON_COUNT, extractedPersons.size());
    }

    /**
     * Core processing logic that extracts the incoming {@link Task}, inspects its {@code Task.input}
     * for any {@link Bundle}, extracts all {@link Person} resources, and populates {@code Task.output}.
     *
     * @param exchange Camel Exchange
     */
    public void processPersonResourceExtraction(Exchange exchange) {
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
            ErgonReasonEnum.HIE_SYNTHETIC_TASK.applyTo(task);
        }

        Task processedTask = extractPersonsToTaskOutput(task);

        // Update exchange headers from extracted person(s)
        List<Person> extractedPersons = extractPersonsFromTaskInputs(task);
        if (!extractedPersons.isEmpty()) {
            Person firstPerson = extractedPersons.get(0);
            String personId = firstPerson.getIdPart();
            String fullName = extractPersonFullName(firstPerson);
            String identifier = extractPersonIdentifier(firstPerson);

            if (StringUtils.isNotBlank(personId)) {
                exchange.getMessage().setHeader(HEADER_PERSON_ID, personId);
            }
            if (StringUtils.isNotBlank(fullName)) {
                exchange.getMessage().setHeader(HEADER_PERSON_NAME, fullName);
            }
            if (StringUtils.isNotBlank(identifier)) {
                exchange.getMessage().setHeader(HEADER_PERSON_IDENTIFIER, identifier);
            }
        }
        exchange.getMessage().setHeader(HEADER_PERSON_COUNT, extractedPersons.size());

        exchange.getMessage().setBody(processedTask);
        log.info("[{}] Extracted {} Person resource(s) from Bundle into Task/{} output",
                getActivityName(), extractedPersons.size(), processedTask.getIdPart());
    }

    /**
     * Parses the {@code Task.input} for any {@link Bundle}, extracts all {@link Person} resources,
     * and sets each as an individual {@code Task.output} component.
     *
     * @param task input Task
     * @return updated Task with extracted Person resources in Task.output
     */
    public Task extractPersonsToTaskOutput(Task task) {
        if (task == null) {
            task = new Task();
            task.setId("Task/" + UUID.randomUUID().toString());
            task.setStatus(Task.TaskStatus.INPROGRESS);
            task.setAuthoredOn(new Date());
        }

        List<Person> extractedPersons = extractPersonsFromTaskInputs(task);

        task.getOutput().clear();

        for (int i = 0; i < extractedPersons.size(); i++) {
            Person person = extractedPersons.get(i);
            Task.TaskOutputComponent outputComp = task.addOutput();
            outputComp.getType().setText("Person Resource").addCoding()
                    .setSystem("http://hl7.org/fhir/resource-types")
                    .setCode("Person")
                    .setDisplay("Person");

            String personJson = fhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(person);
            outputComp.setValue(new StringType(personJson));
        }

        if (!extractedPersons.isEmpty()) {
            Person firstPerson = extractedPersons.get(0);
            String fullName = extractPersonFullName(firstPerson);
            task.setFor(new Reference("Person/" + firstPerson.getIdPart()).setDisplay(fullName));
        }

        task.setLastModified(new Date());
        ErgonReasonEnum.ensureSyntheticTaskReason(task);

        return task;
    }

    /**
     * Extracts all {@link Person} resources from any {@link Bundle} found in {@code task.getInput()}
     * or contained resources.
     *
     * @param task input Task
     * @return list of extracted Person resources
     */
    public List<Person> extractPersonsFromTaskInputs(Task task) {
        List<Person> persons = new ArrayList<>();
        if (task == null) {
            return persons;
        }

        List<Bundle> bundles = extractBundlesFromTask(task);
        for (Bundle bundle : bundles) {
            persons.addAll(extractPersonsFromBundle(bundle));
        }

        return persons;
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
     * Recursively extracts all {@link Person} resources from a {@link Bundle}.
     *
     * @param bundle FHIR Bundle
     * @return list of Person resources
     */
    public List<Person> extractPersonsFromBundle(Bundle bundle) {
        List<Person> persons = new ArrayList<>();
        if (bundle == null || !bundle.hasEntry()) {
            return persons;
        }

        for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {
            if (entry.hasResource()) {
                Resource res = entry.getResource();
                if (res instanceof Person) {
                    persons.add((Person) res);
                } else if (res instanceof Bundle) {
                    persons.addAll(extractPersonsFromBundle((Bundle) res));
                }
            }
        }

        return persons;
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
     * Extracts full name representation from a {@link Person}.
     */
    public String extractPersonFullName(Person person) {
        if (person == null || !person.hasName()) {
            return "";
        }
        HumanName name = person.getNameFirstRep();
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
     * Extracts identifier representation from a {@link Person}.
     */
    public String extractPersonIdentifier(Person person) {
        if (person == null || !person.hasIdentifier()) {
            return "";
        }
        return person.getIdentifierFirstRep().getValue();
    }

    public FhirContext getFhirContext() {
        return fhirContext;
    }
}
