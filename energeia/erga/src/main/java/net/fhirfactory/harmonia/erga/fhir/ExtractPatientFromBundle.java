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
 * extracts all {@link Patient} resources contained within the Bundle(s), and adds each
 * extracted Patient individually to {@code Task.output}.
 */
@Dependent
public class ExtractPatientFromBundle extends ErgonBase {

    private static final Logger log = LoggerFactory.getLogger(ExtractPatientFromBundle.class);

    public static final String DEFAULT_ACTIVITY_ID = "extract-patient-from-bundle";
    public static final String DEFAULT_ACTIVITY_NAME = "Extract Patient From Bundle Activity";

    public static final String HEADER_PATIENT_ID = "HIE_PATIENT_ID";
    public static final String HEADER_PATIENT_NAME = "HIE_PATIENT_NAME";
    public static final String HEADER_PATIENT_MRN = "HIE_PATIENT_MRN";
    public static final String HEADER_PATIENT_COUNT = "HIE_PATIENT_COUNT";

    private final FhirContext fhirContext;

    public ExtractPatientFromBundle() {
        super(DEFAULT_ACTIVITY_ID, DEFAULT_ACTIVITY_NAME);
        setActivityDescription("Parses Task.input for Bundle resources and extracts contained Patient resources to Task.output");
        this.fhirContext = FhirContext.forR5();
    }

    public ExtractPatientFromBundle(CamelContext context) {
        super(context, DEFAULT_ACTIVITY_ID, DEFAULT_ACTIVITY_NAME);
        setActivityDescription("Parses Task.input for Bundle resources and extracts contained Patient resources to Task.output");
        this.fhirContext = FhirContext.forR5();
    }

    public ExtractPatientFromBundle(String activityId, String activityName) {
        super(activityId, activityName);
        this.fhirContext = FhirContext.forR5();
    }

    public ExtractPatientFromBundle(CamelContext context, String activityId, String activityName) {
        super(context, activityId, activityName);
        this.fhirContext = FhirContext.forR5();
    }

    public ExtractPatientFromBundle(FhirContext fhirContext) {
        super(DEFAULT_ACTIVITY_ID, DEFAULT_ACTIVITY_NAME);
        setActivityDescription("Parses Task.input for Bundle resources and extracts contained Patient resources to Task.output");
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
        Task processedTask = extractPatientsToTaskOutput(task);

        pragma.getOutput().clear();
        if (processedTask != null && processedTask.hasOutput()) {
            for (Task.TaskOutputComponent outComp : processedTask.getOutput()) {
                pragma.addOutput(ErgonPayload.fromTaskOutput(outComp));
            }
        }

        List<Patient> extractedPatients = extractPatientsFromTaskInputs(task);
        if (!extractedPatients.isEmpty()) {
            Patient firstPat = extractedPatients.get(0);
            String patientId = firstPat.getIdPart();
            String fullName = extractPatientFullName(firstPat);
            String mrn = extractPatientMrn(firstPat);

            if (StringUtils.isNotBlank(patientId)) {
                exchange.getMessage().setHeader(HEADER_PATIENT_ID, patientId);
            }
            if (StringUtils.isNotBlank(fullName)) {
                exchange.getMessage().setHeader(HEADER_PATIENT_NAME, fullName);
            }
            if (StringUtils.isNotBlank(mrn)) {
                exchange.getMessage().setHeader(HEADER_PATIENT_MRN, mrn);
            }
        }
        exchange.getMessage().setHeader(HEADER_PATIENT_COUNT, extractedPatients.size());
    }

    /**
     * Core processing logic that extracts the incoming {@link Task}, inspects its {@code Task.input}
     * for any {@link Bundle}, extracts all {@link Patient} resources, and populates {@code Task.output}.
     *
     * @param exchange Camel Exchange
     */
    public void processPatientResourceExtraction(Exchange exchange) {
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

        Task processedTask = extractPatientsToTaskOutput(task);

        // Update exchange headers from extracted patient(s)
        List<Patient> extractedPatients = extractPatientsFromTaskInputs(task);
        if (!extractedPatients.isEmpty()) {
            Patient firstPat = extractedPatients.get(0);
            String patientId = firstPat.getIdPart();
            String fullName = extractPatientFullName(firstPat);
            String mrn = extractPatientMrn(firstPat);

            if (StringUtils.isNotBlank(patientId)) {
                exchange.getMessage().setHeader(HEADER_PATIENT_ID, patientId);
            }
            if (StringUtils.isNotBlank(fullName)) {
                exchange.getMessage().setHeader(HEADER_PATIENT_NAME, fullName);
            }
            if (StringUtils.isNotBlank(mrn)) {
                exchange.getMessage().setHeader(HEADER_PATIENT_MRN, mrn);
            }
        }
        exchange.getMessage().setHeader(HEADER_PATIENT_COUNT, extractedPatients.size());

        exchange.getMessage().setBody(processedTask);
        log.info("[{}] Extracted {} Patient resource(s) from Bundle into Task/{} output",
                getActivityName(), extractedPatients.size(), processedTask.getIdPart());
    }

    /**
     * Parses the {@code Task.input} for any {@link Bundle}, extracts all {@link Patient} resources,
     * and sets each as an individual {@code Task.output} component.
     *
     * @param task input Task
     * @return updated Task with extracted Patient resources in Task.output
     */
    public Task extractPatientsToTaskOutput(Task task) {
        if (task == null) {
            task = new Task();
            task.setId("Task/" + UUID.randomUUID().toString());
            task.setStatus(Task.TaskStatus.INPROGRESS);
            task.setAuthoredOn(new Date());
        }

        List<Patient> extractedPatients = extractPatientsFromTaskInputs(task);

        task.getOutput().clear();

        for (int i = 0; i < extractedPatients.size(); i++) {
            Patient patient = extractedPatients.get(i);
            Task.TaskOutputComponent outputComp = task.addOutput();
            outputComp.getType().setText("Patient Resource").addCoding()
                    .setSystem("http://hl7.org/fhir/resource-types")
                    .setCode("Patient")
                    .setDisplay("Patient");

            String patientJson = fhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient);
            outputComp.setValue(new StringType(patientJson));
        }

        if (!extractedPatients.isEmpty()) {
            Patient firstPatient = extractedPatients.get(0);
            String fullName = extractPatientFullName(firstPatient);
            task.setFor(new Reference("Patient/" + firstPatient.getIdPart()).setDisplay(fullName));
        }

        task.setLastModified(new Date());
        ErgonReasonEnum.ensureSyntheticTaskReason(task);

        return task;
    }

    /**
     * Extracts all {@link Patient} resources from any {@link Bundle} found in {@code task.getInput()}
     * or contained resources.
     *
     * @param task input Task
     * @return list of extracted Patient resources
     */
    public List<Patient> extractPatientsFromTaskInputs(Task task) {
        List<Patient> patients = new ArrayList<>();
        if (task == null) {
            return patients;
        }

        List<Bundle> bundles = extractBundlesFromTask(task);
        for (Bundle bundle : bundles) {
            patients.addAll(extractPatientsFromBundle(bundle));
        }

        return patients;
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
     * Recursively extracts all {@link Patient} resources from a {@link Bundle}.
     *
     * @param bundle FHIR Bundle
     * @return list of Patient resources
     */
    public List<Patient> extractPatientsFromBundle(Bundle bundle) {
        List<Patient> patients = new ArrayList<>();
        if (bundle == null || !bundle.hasEntry()) {
            return patients;
        }

        for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {
            if (entry.hasResource()) {
                Resource res = entry.getResource();
                if (res instanceof Patient) {
                    patients.add((Patient) res);
                } else if (res instanceof Bundle) {
                    patients.addAll(extractPatientsFromBundle((Bundle) res));
                }
            }
        }

        return patients;
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
     * Extracts full name representation from a {@link Patient}.
     */
    public String extractPatientFullName(Patient patient) {
        if (patient == null || !patient.hasName()) {
            return "";
        }
        HumanName name = patient.getNameFirstRep();
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
     * Extracts MRN value from a {@link Patient}.
     */
    public String extractPatientMrn(Patient patient) {
        if (patient == null || !patient.hasIdentifier()) {
            return "";
        }
        for (Identifier id : patient.getIdentifier()) {
            if (id.hasType()) {
                for (Coding c : id.getType().getCoding()) {
                    if ("MR".equalsIgnoreCase(c.getCode())) {
                        return id.getValue();
                    }
                }
            }
        }
        return patient.getIdentifierFirstRep().getValue();
    }

    public FhirContext getFhirContext() {
        return fhirContext;
    }
}
