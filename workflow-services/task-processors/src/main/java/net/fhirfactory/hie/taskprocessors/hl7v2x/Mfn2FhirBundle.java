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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.hl7v2.util.Terser;
import jakarta.enterprise.context.Dependent;
import net.fhirfactory.hie.model.task.HieTaskReason;
import net.fhirfactory.hie.taskprocessors.base.TaskProcessingActivity;
import net.fhirfactory.hie.taskprocessors.hl7v2x.common.Hl7v2ParsingSupport;
import net.fhirfactory.hie.taskprocessors.hl7v2x.factories.Mfn2FhirBundleBuilder;
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
 * Task Processing Activity that receives a {@link Task} containing an MFN^M02 message
 * as its {@code Task.input}, transforms the message into a single FHIR {@link Bundle} of all
 * derivable FHIR resources (Practitioner, PractitionerRole, Organization, Location, Communication, Provenance),
 * and adds this Bundle as one {@code Task.output} entry AND the origin {@code Task.input} as another entry.
 */
@Dependent
public class Mfn2FhirBundle extends TaskProcessingActivity {

    private static final Logger log = LoggerFactory.getLogger(Mfn2FhirBundle.class);

    public static final String DEFAULT_ACTIVITY_ID = "mfn2fhir-bundle";
    public static final String DEFAULT_ACTIVITY_NAME = "MFN to FHIR Bundle Activity";

    public static final String HEADER_PRACTITIONER_ID = "HIE_PRACTITIONER_ID";
    public static final String HEADER_PRACTITIONER_NAME = "HIE_PRACTITIONER_NAME";
    public static final String HEADER_BUNDLE_ID = "HIE_BUNDLE_ID";
    public static final String HEADER_MESSAGE_TYPE = "HIE_MESSAGE_TYPE";
    public static final String HEADER_TRIGGER_TYPE = "HIE_TRIGGER_TYPE";
    public static final String HEADER_CONTROL_ID = "HIE_CONTROL_ID";
    public static final String HEADER_RAW_MESSAGE = "HIE_RAW_MESSAGE";

    public static final String EXTENSION_ETHNICITY = Hl7v2ParsingSupport.EXTENSION_ETHNICITY;
    public static final String EXTENSION_CITIZENSHIP = Hl7v2ParsingSupport.EXTENSION_CITIZENSHIP;
    public static final String EXTENSION_RELIGION = Hl7v2ParsingSupport.EXTENSION_RELIGION;

    private final MfnMessageExtractor messageExtractor;
    private final Mfn2FhirBundleBuilder bundleBuilder;
    private final FhirContext fhirContext;

    public Mfn2FhirBundle() {
        super(DEFAULT_ACTIVITY_ID, DEFAULT_ACTIVITY_NAME);
        setActivityDescription("Transforms HL7 v2.x MFN^M02 messages in Task.input to FHIR R5 Resource Bundles");
        this.messageExtractor = new MfnMessageExtractor();
        this.bundleBuilder = new Mfn2FhirBundleBuilder();
        this.fhirContext = FhirContext.forR5();
    }

    public Mfn2FhirBundle(CamelContext context) {
        super(context, DEFAULT_ACTIVITY_ID, DEFAULT_ACTIVITY_NAME);
        setActivityDescription("Transforms HL7 v2.x MFN^M02 messages in Task.input to FHIR R5 Resource Bundles");
        this.messageExtractor = new MfnMessageExtractor();
        this.bundleBuilder = new Mfn2FhirBundleBuilder();
        this.fhirContext = FhirContext.forR5();
    }

    public Mfn2FhirBundle(String activityId, String activityName) {
        super(activityId, activityName);
        this.messageExtractor = new MfnMessageExtractor();
        this.bundleBuilder = new Mfn2FhirBundleBuilder();
        this.fhirContext = FhirContext.forR5();
    }

    public Mfn2FhirBundle(CamelContext context, String activityId, String activityName) {
        super(context, activityId, activityName);
        this.messageExtractor = new MfnMessageExtractor();
        this.bundleBuilder = new Mfn2FhirBundleBuilder();
        this.fhirContext = FhirContext.forR5();
    }

    public Mfn2FhirBundle(Mfn2FhirBundleBuilder bundleBuilder, MfnMessageExtractor messageExtractor, FhirContext fhirContext) {
        super(DEFAULT_ACTIVITY_ID, DEFAULT_ACTIVITY_NAME);
        this.bundleBuilder = bundleBuilder != null ? bundleBuilder : new Mfn2FhirBundleBuilder();
        this.messageExtractor = messageExtractor != null ? messageExtractor : new MfnMessageExtractor();
        this.fhirContext = fhirContext != null ? fhirContext : FhirContext.forR5();
    }

    @Override
    protected void processActivity(Exchange exchange) throws Exception {
        processMfn2FhirBundle(exchange);
    }

    /**
     * Core mapping activity execution: extracts the incoming Task, identifies the MFN message in Task.input,
     * maps all practitioner and administrative data to a single FHIR Bundle, and updates the Task with the Bundle
     * and origin Task.input as output entries.
     *
     * @param exchange Camel Exchange
     */
    public void processMfn2FhirBundle(Exchange exchange) {
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
            HieTaskReason.HIE_SYNTHETIC_TASK.applyTo(task);
        }

        Task processedTask = mapMfnToFhir(task, rawHeader);

        // Update exchange headers from processed task
        if (processedTask.hasFor() && processedTask.getFor().hasReference()) {
            String pRef = processedTask.getFor().getReference();
            String practId = pRef.startsWith("Practitioner/") ? pRef.substring(13) : pRef;
            exchange.getMessage().setHeader(HEADER_PRACTITIONER_ID, practId);
            if (processedTask.getFor().hasDisplay()) {
                exchange.getMessage().setHeader(HEADER_PRACTITIONER_NAME, processedTask.getFor().getDisplay());
            }
        }

        exchange.getMessage().setBody(processedTask);
        log.info("[{}] Successfully mapped MFN message for Task/{} into FHIR Bundle with {} output entries",
                getActivityName(), processedTask.getIdPart(), processedTask.getOutput().size());
    }

    /**
     * Transforms an MFN-based message contained within {@code task.getInput()} into a FHIR Bundle,
     * adding the Bundle and origin input to {@code task.getOutput()}.
     *
     * @param task input Task
     * @return updated Task with Bundle and origin input outputs
     */
    public Task mapMfnToFhir(Task task) {
        return mapMfnToFhir(task, null);
    }

    /**
     * Transforms an MFN-based message contained within {@code task.getInput()} (or fallback payload) into a FHIR Bundle,
     * adding the Bundle and origin input to {@code task.getOutput()}.
     *
     * @param task           input Task
     * @param fallbackRawMsg optional fallback raw HL7 string
     * @return updated Task with Bundle and origin input outputs
     */
    public Task mapMfnToFhir(Task task, String fallbackRawMsg) {
        if (task == null) {
            task = new Task();
            task.setId("Task/" + UUID.randomUUID().toString());
            task.setStatus(Task.TaskStatus.INPROGRESS);
            task.setAuthoredOn(new Date());
        }

        // 1. Locate MFN message and origin Task.input component
        MfnMessageExtractor.ExtractedInputResult inputResult = messageExtractor.extractMfnMessageFromTask(task, fallbackRawMsg);
        String rawHl7 = inputResult.getRawMessage();
        Task.TaskInputComponent originInput = inputResult.getOriginInput();

        if (originInput == null && StringUtils.isNotBlank(rawHl7)) {
            // If task had no input yet, create the origin input component
            originInput = new Task.TaskInputComponent();
            originInput.getType().setText("MFN Message Payload").addCoding()
                    .setSystem("http://terminology.hl7.org/CodeSystem/task-input-type")
                    .setCode("input-mfn")
                    .setDisplay("MFN Message Payload");
            originInput.setValue(new StringType(rawHl7));
            task.addInput(originInput);
        }

        // 2. Generate FHIR Bundle containing all derived practitioner and administrative resources
        Bundle bundle = createBundleFromMfn(rawHl7, task);

        // 3. Update Task.output: Add Bundle entry (as JSON) AND origin Task.input entry
        task.getOutput().clear();

        // Output entry 1: The FHIR Bundle of all derived resources (as JSON)
        Task.TaskOutputComponent bundleOutput = task.addOutput();
        bundleOutput.getType().setText("MFN Derived FHIR Bundle").addCoding()
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
        HieTaskReason.ensureSyntheticTaskReason(task);

        return task;
    }

    /**
     * Parses an MFN message string and derives all possible FHIR R5 resources into a single {@link Bundle}.
     *
     * @param rawHl7Message raw HL7 MFN message string
     * @param parentTask    parent Task resource for context
     * @return populated FHIR Bundle
     */
    public Bundle createBundleFromMfn(String rawHl7Message, Task parentTask) {
        return bundleBuilder.createBundleFromMfn(rawHl7Message, parentTask, getActivityId(), getActivityName());
    }

    public List<Practitioner> buildPractitioners(Terser terser, String rawMessage, String messageControlId) {
        return bundleBuilder.getPractitionerBuilder().buildPractitioners(terser, rawMessage, messageControlId);
    }

    public List<PractitionerRole> buildPractitionerRoles(Terser terser, String rawMessage,
                                                         List<Practitioner> practitioners,
                                                         List<Organization> organizations,
                                                         List<Location> locations) {
        return bundleBuilder.getPractitionerBuilder().buildPractitionerRoles(terser, rawMessage, practitioners, organizations, locations);
    }

    public List<Organization> buildOrganizations(Terser terser, String rawMessage, String sendingFacility, String receivingFacility) {
        return bundleBuilder.getAdministrativeBuilder().buildOrganizations(terser, rawMessage, sendingFacility, receivingFacility);
    }

    public List<Location> buildLocations(Terser terser, String rawMessage) {
        return bundleBuilder.getAdministrativeBuilder().buildLocations(terser, rawMessage);
    }

    public Communication buildCommunication(Terser terser, String rawMessageString, String messageControlId,
                                            String triggerEvent, String practitionerId, String practitionerFullName,
                                            String sendingApp, String sendingFacility, String messageTimestamp) {
        return bundleBuilder.getMetadataBuilder().buildCommunication(terser, rawMessageString, messageControlId, triggerEvent,
                practitionerId, practitionerFullName, sendingApp, sendingFacility, messageTimestamp);
    }

    public Provenance buildProvenance(Bundle bundle, Practitioner practitioner, String messageControlId, String triggerEvent,
                                      String sendingApp, String sendingFacility) {
        return bundleBuilder.getMetadataBuilder().buildProvenance(bundle, practitioner, messageControlId, triggerEvent,
                sendingApp, sendingFacility, getActivityId(), getActivityName());
    }

    public MfnMessageExtractor.ExtractedInputResult extractMfnMessageFromTask(Task task, String fallback) {
        return messageExtractor.extractMfnMessageFromTask(task, fallback);
    }

    public Practitioner createFallbackPractitioner(String idSeed) {
        return bundleBuilder.getPractitionerBuilder().createFallbackPractitioner(idSeed);
    }

    public Enumerations.AdministrativeGender mapAdministrativeGender(String genderCode) {
        return Hl7v2ParsingSupport.mapAdministrativeGender(genderCode);
    }

    public Date parseHl7Date(String dateStr) {
        return Hl7v2ParsingSupport.parseHl7Date(dateStr);
    }

    public String buildFullName(String given, String middle, String family, String prefix, String suffix) {
        return Hl7v2ParsingSupport.buildFullName(given, middle, family, prefix, suffix);
    }

    public String extractPractitionerFullName(Practitioner practitioner) {
        return bundleBuilder.getPractitionerBuilder().extractPractitionerFullName(practitioner);
    }

    public MfnMessageExtractor getMessageExtractor() {
        return messageExtractor;
    }

    public Mfn2FhirBundleBuilder getBundleBuilder() {
        return bundleBuilder;
    }

    public static class ExtractedInputResult extends MfnMessageExtractor.ExtractedInputResult {
        public ExtractedInputResult(String rawMessage, Task.TaskInputComponent originInput) {
            super(rawMessage, originInput);
        }
    }
}
