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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.hl7v2.util.Terser;
import jakarta.enterprise.context.Dependent;
import net.fhirfactory.harmonia.model.ergon.ErgonPayload;
import net.fhirfactory.harmonia.model.ergon.ErgonReasonEnum;
import net.fhirfactory.harmonia.erga.base.ErgonBase;
import net.fhirfactory.harmonia.erga.hl7v2x.common.Hl7v2ParsingSupport;
import net.fhirfactory.harmonia.erga.hl7v2x.factories.Adt2FhirBundleBuilder;
import net.fhirfactory.harmonia.model.pragma.Pragma;
import net.fhirfactory.harmonia.model.pragma.PragmaFhirConverter;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Task Processing Activity that receives a {@link Task} containing an ADT-based message
 * as its {@code Task.input}, transforms the message into a single FHIR {@link Bundle} of all
 * derivable FHIR resources (Patient, Encounter, Practitioner, RelatedPerson, Condition,
 * AllergyIntolerance, Observation, Location, Organization, Coverage, Communication, Provenance),
 * and adds this Bundle as one {@code Task.output} entry AND the origin {@code Task.input} as another entry.
 */
@Dependent
public class Adt2FhirMapper extends ErgonBase {

    private static final Logger log = LoggerFactory.getLogger(Adt2FhirMapper.class);

    public static final String DEFAULT_ACTIVITY_ID = "adt2fhir-mapper";
    public static final String DEFAULT_ACTIVITY_NAME = "ADT to FHIR Mapper Activity";

    public static final String HEADER_PATIENT_ID = "HIE_PATIENT_ID";
    public static final String HEADER_PATIENT_NAME = "HIE_PATIENT_NAME";
    public static final String HEADER_PATIENT_MRN = "HIE_PATIENT_MRN";
    public static final String HEADER_BUNDLE_ID = "HIE_BUNDLE_ID";
    public static final String HEADER_MESSAGE_TYPE = "HIE_MESSAGE_TYPE";
    public static final String HEADER_TRIGGER_TYPE = "HIE_TRIGGER_TYPE";
    public static final String HEADER_CONTROL_ID = "HIE_CONTROL_ID";
    public static final String HEADER_RAW_MESSAGE = "HIE_RAW_MESSAGE";

    public static final String EXTENSION_RACE = Hl7v2ParsingSupport.EXTENSION_RACE;
    public static final String EXTENSION_ETHNICITY = Hl7v2ParsingSupport.EXTENSION_ETHNICITY;
    public static final String EXTENSION_RELIGION = Hl7v2ParsingSupport.EXTENSION_RELIGION;
    public static final String EXTENSION_BIRTH_PLACE = Hl7v2ParsingSupport.EXTENSION_BIRTH_PLACE;
    public static final String EXTENSION_CITIZENSHIP = Hl7v2ParsingSupport.EXTENSION_CITIZENSHIP;
    public static final String EXTENSION_MOTHER_MAIDEN_NAME = Hl7v2ParsingSupport.EXTENSION_MOTHER_MAIDEN_NAME;

    private final AdtMessageExtractor messageExtractor;
    private final Adt2FhirBundleBuilder bundleBuilder;
    private final FhirContext fhirContext;

    public Adt2FhirMapper() {
        super(DEFAULT_ACTIVITY_ID, DEFAULT_ACTIVITY_NAME);
        setActivityDescription("Transforms HL7 v2.x ADT messages in Task.input to FHIR R5 Resource Bundles");
        this.messageExtractor = new AdtMessageExtractor();
        this.bundleBuilder = new Adt2FhirBundleBuilder();
        this.fhirContext = FhirContext.forR5();
    }

    public Adt2FhirMapper(CamelContext context) {
        super(context, DEFAULT_ACTIVITY_ID, DEFAULT_ACTIVITY_NAME);
        setActivityDescription("Transforms HL7 v2.x ADT messages in Task.input to FHIR R5 Resource Bundles");
        this.messageExtractor = new AdtMessageExtractor();
        this.bundleBuilder = new Adt2FhirBundleBuilder();
        this.fhirContext = FhirContext.forR5();
    }

    public Adt2FhirMapper(String activityId, String activityName) {
        super(activityId, activityName);
        this.messageExtractor = new AdtMessageExtractor();
        this.bundleBuilder = new Adt2FhirBundleBuilder();
        this.fhirContext = FhirContext.forR5();
    }

    public Adt2FhirMapper(Adt2FhirBundleBuilder bundleBuilder, AdtMessageExtractor messageExtractor, FhirContext fhirContext) {
        super(DEFAULT_ACTIVITY_ID, DEFAULT_ACTIVITY_NAME);
        this.bundleBuilder = bundleBuilder != null ? bundleBuilder : new Adt2FhirBundleBuilder();
        this.messageExtractor = messageExtractor != null ? messageExtractor : new AdtMessageExtractor();
        this.fhirContext = fhirContext != null ? fhirContext : FhirContext.forR5();
    }

    @Override
    protected void processActivity(Exchange exchange) throws Exception {
        processAdt2FhirMapping(exchange);
    }

    @Override
    protected void processErgon(Pragma pragma, Exchange exchange) throws Exception {
        processAdt2FhirMapping(exchange);
    }

    /**
     * Core mapping activity execution: extracts the incoming Task, identifies the ADT message in Task.input,
     * maps all clinical and administrative data to a single FHIR Bundle, and updates the Task with the Bundle
     * and origin Task.input as output entries.
     *
     * @param exchange Camel Exchange
     */
    public void processAdt2FhirMapping(Exchange exchange) {
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

        String rawHeader = exchange.getMessage().getHeader(HEADER_RAW_MESSAGE, String.class);
        if (StringUtils.isBlank(rawHeader)) {
            rawHeader = (String) exchange.getProperty(HEADER_RAW_MESSAGE);
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

        Task processedTask = mapAdtToFhir(task, rawHeader);

        // Update exchange headers from processed task
        if (processedTask.hasFor() && processedTask.getFor().hasReference()) {
            String pRef = processedTask.getFor().getReference();
            String patId = pRef.startsWith("Patient/") ? pRef.substring(8) : pRef;
            exchange.getMessage().setHeader(HEADER_PATIENT_ID, patId);
            if (processedTask.getFor().hasDisplay()) {
                exchange.getMessage().setHeader(HEADER_PATIENT_NAME, processedTask.getFor().getDisplay());
            }
        }

        exchange.getMessage().setBody(processedTask);
        log.info("[{}] Successfully mapped ADT message for Task/{} into FHIR Bundle with {} output entries",
                getActivityName(), processedTask.getIdPart(), processedTask.getOutput().size());
    }

    /**
     * Transforms an ADT-based message contained within {@code task.getInput()} into a FHIR Bundle,
     * adding the Bundle and origin input to {@code task.getOutput()}.
     *
     * @param task input Task
     * @return updated Task with Bundle and origin input outputs
     */
    public Task mapAdtToFhir(Task task) {
        return mapAdtToFhir(task, null);
    }

    /**
     * Transforms an ADT-based message contained within {@code task.getInput()} (or fallback payload) into a FHIR Bundle,
     * adding the Bundle and origin input to {@code task.getOutput()}.
     *
     * @param task           input Task
     * @param fallbackRawMsg optional fallback raw HL7 string
     * @return updated Task with Bundle and origin input outputs
     */
    public Task mapAdtToFhir(Task task, String fallbackRawMsg) {
        if (task == null) {
            task = new Task();
            task.setId("Task/" + UUID.randomUUID().toString());
            task.setStatus(Task.TaskStatus.INPROGRESS);
            task.setAuthoredOn(new Date());
        }

        // 1. Locate ADT message and origin Task.input component
        AdtMessageExtractor.ExtractedInputResult inputResult = messageExtractor.extractAdtMessageFromTask(task, fallbackRawMsg);
        String rawHl7 = inputResult.getRawMessage();
        Task.TaskInputComponent originInput = inputResult.getOriginInput();

        if (originInput == null && StringUtils.isNotBlank(rawHl7)) {
            // If task had no input yet, create the origin input component
            originInput = new Task.TaskInputComponent();
            originInput.getType().setText("ADT Message Payload").addCoding()
                    .setSystem("http://terminology.hl7.org/CodeSystem/task-input-type")
                    .setCode("input-adt")
                    .setDisplay("ADT Message Payload");
            originInput.setValue(new StringType(rawHl7));
            task.addInput(originInput);
        }

        // 2. Generate FHIR Bundle containing all derived clinical and administrative resources
        Bundle bundle = createBundleFromAdt(rawHl7, task);

        // 3. Update Task.output: Add Bundle entry (as JSON) AND origin Task.input entry
        task.getOutput().clear();

        // Output entry 1: The FHIR Bundle of all derived resources (as JSON)
        Task.TaskOutputComponent bundleOutput = task.addOutput();
        bundleOutput.getType().setText("ADT Derived FHIR Bundle").addCoding()
                .setSystem("http://hl7.org/fhir/resource-types")
                .setCode("Bundle")
                .setDisplay("Bundle");
        String bundleJson = fhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle);
        bundleOutput.setValue(new StringType(bundleJson));

        // Output entry 2: The origin Task.Input entry
        Task.TaskOutputComponent originInputOutput = task.addOutput();
        originInputOutput.getType().setText("Origin Task Input").addCoding()
                .setSystem("http://terminology.hl7.org/CodeSystem/task-input-type")
                .setCode("origin-input")
                .setDisplay("Origin Task Input");

        if (originInput != null && originInput.hasValue()) {
            originInputOutput.setValue(originInput.getValue().copy());
        } else if (StringUtils.isNotBlank(rawHl7)) {
            originInputOutput.setValue(new StringType(rawHl7));
        } else {
            originInputOutput.setValue(new StringType(""));
        }

        task.setLastModified(new Date());
        ErgonReasonEnum.ensureSyntheticTaskReason(task);

        return task;
    }

    /**
     * Parses an ADT message string and derives all possible FHIR R5 resources into a single {@link Bundle}.
     *
     * @param rawHl7Message raw HL7 ADT message string
     * @param parentTask    parent Task resource for context
     * @return populated FHIR Bundle
     */
    public Bundle createBundleFromAdt(String rawHl7Message, Task parentTask) {
        return bundleBuilder.createBundleFromAdt(rawHl7Message, parentTask, getActivityId(), getActivityName());
    }

    public Patient buildPatient(Terser terser, String rawMessage, String messageControlId) {
        return bundleBuilder.getPatientBuilder().buildPatient(terser, rawMessage, messageControlId);
    }

    public Encounter buildEncounter(Terser terser, String rawMessage, String messageControlId, String triggerEvent,
                                    String patientId, List<Practitioner> practitioners, List<Location> locations) {
        return bundleBuilder.getEncounterBuilder().buildEncounter(terser, rawMessage, messageControlId, triggerEvent, patientId, practitioners, locations);
    }

    public List<Practitioner> buildPractitioners(Terser terser, String rawMessage) {
        return bundleBuilder.getEncounterBuilder().buildPractitioners(terser, rawMessage);
    }

    public List<Location> buildLocations(Terser terser, String rawMessage) {
        return bundleBuilder.getEncounterBuilder().buildLocations(terser, rawMessage);
    }

    public List<RelatedPerson> buildRelatedPersons(Terser terser, String rawMessage, String patientId) {
        return bundleBuilder.getAdministrativeBuilder().buildRelatedPersons(terser, rawMessage, patientId);
    }

    public List<Organization> buildOrganizations(Terser terser, String rawMessage, String sendingFacility, String receivingFacility) {
        return bundleBuilder.getAdministrativeBuilder().buildOrganizations(terser, rawMessage, sendingFacility, receivingFacility);
    }

    public List<Condition> buildConditions(Terser terser, String rawMessage, String patientId, Encounter encounter) {
        return bundleBuilder.getClinicalBuilder().buildConditions(terser, rawMessage, patientId, encounter);
    }

    public List<AllergyIntolerance> buildAllergies(Terser terser, String rawMessage, String patientId) {
        return bundleBuilder.getClinicalBuilder().buildAllergies(terser, rawMessage, patientId);
    }

    public List<Observation> buildObservations(Terser terser, String rawMessage, String patientId, Encounter encounter) {
        return bundleBuilder.getClinicalBuilder().buildObservations(terser, rawMessage, patientId, encounter);
    }

    public List<Coverage> buildCoverages(Terser terser, String rawMessage, String patientId) {
        return bundleBuilder.getClinicalBuilder().buildCoverages(terser, rawMessage, patientId);
    }

    public Communication buildCommunication(Terser terser, String rawMessageString, String messageControlId,
                                            String triggerEvent, String patientId, String patientFullName,
                                            String sendingApp, String sendingFacility, String messageTimestamp) {
        return bundleBuilder.getMetadataBuilder().buildCommunication(terser, rawMessageString, messageControlId, triggerEvent,
                patientId, patientFullName, sendingApp, sendingFacility, messageTimestamp);
    }

    public Provenance buildProvenance(Bundle bundle, Patient patient, String messageControlId, String triggerEvent,
                                      String sendingApp, String sendingFacility) {
        return bundleBuilder.getMetadataBuilder().buildProvenance(bundle, patient, messageControlId, triggerEvent,
                sendingApp, sendingFacility, getActivityId(), getActivityName());
    }

    public AdtMessageExtractor.ExtractedInputResult extractAdtMessageFromTask(Task task, String fallback) {
        return messageExtractor.extractAdtMessageFromTask(task, fallback);
    }

    public Communication extractCommunicationFromTask(Task task) {
        return messageExtractor.extractCommunicationFromTask(task);
    }

    public String extractRawFromCommunication(Communication communication) {
        return messageExtractor.extractRawFromCommunication(communication);
    }

    public Patient createFallbackPatient(String idSeed) {
        return bundleBuilder.getPatientBuilder().createFallbackPatient(idSeed);
    }

    public Enumerations.AdministrativeGender mapAdministrativeGender(String genderCode) {
        return Hl7v2ParsingSupport.mapAdministrativeGender(genderCode);
    }

    public CodeableConcept mapMaritalStatus(String maritalCode) {
        return Hl7v2ParsingSupport.mapMaritalStatus(maritalCode);
    }

    public Enumerations.EncounterStatus mapEncounterStatus(String triggerEvent) {
        return Hl7v2ParsingSupport.mapEncounterStatus(triggerEvent);
    }

    public CodeableConcept mapEncounterClass(String patientClass) {
        return Hl7v2ParsingSupport.mapEncounterClass(patientClass);
    }

    public Date parseHl7Date(String dateStr) {
        return Hl7v2ParsingSupport.parseHl7Date(dateStr);
    }

    public String buildFullName(String given, String middle, String family, String prefix, String suffix) {
        return Hl7v2ParsingSupport.buildFullName(given, middle, family, prefix, suffix);
    }

    public String extractFullName(Patient patient) {
        return Hl7v2ParsingSupport.extractFullName(patient);
    }

    public AdtMessageExtractor getMessageExtractor() {
        return messageExtractor;
    }

    public Adt2FhirBundleBuilder getBundleBuilder() {
        return bundleBuilder;
    }

    public static class ExtractedInputResult extends AdtMessageExtractor.ExtractedInputResult {
        public ExtractedInputResult(String rawMessage, Task.TaskInputComponent originInput) {
            super(rawMessage, originInput);
        }
    }
}
