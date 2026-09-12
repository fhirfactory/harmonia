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

package net.fhirfactory.hie.taskprocessors.patient.demographics;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.Dependent;
import net.fhirfactory.hie.taskprocessors.base.TaskProcessingActivity;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Task Processing Activity for extracting demographic information from incoming ADT event trigger
 * based messages (HL7 v2 PID, NK1, and PD1 segments) and updating the corresponding Patient resource
 * and Task outputs within the HIE.
 */
@Dependent
public class PatientDemographicsUpdate extends TaskProcessingActivity {

    private static final Logger log = LoggerFactory.getLogger(PatientDemographicsUpdate.class);

    public static final String DEFAULT_ACTIVITY_ID = "patient-demographics-update";
    public static final String DEFAULT_ACTIVITY_NAME = "Patient Demographics Update Activity";

    public static final String HEADER_PATIENT_ID = "HIE_PATIENT_ID";
    public static final String HEADER_PATIENT_NAME = "HIE_PATIENT_NAME";
    public static final String HEADER_PATIENT_MRN = "HIE_PATIENT_MRN";
    public static final String HEADER_PATIENT_UPDATED = "HIE_PATIENT_UPDATED";
    public static final String HEADER_PATIENT_DEMOGRAPHICS_UPDATED = "HIE_PATIENT_DEMOGRAPHICS_UPDATED";
    public static final String HEADER_PATIENT_GENDER = "HIE_PATIENT_GENDER";
    public static final String HEADER_PATIENT_DOB = "HIE_PATIENT_DOB";
    public static final String HEADER_PATIENT_MARITAL_STATUS = "HIE_PATIENT_MARITAL_STATUS";
    public static final String HEADER_RAW_MESSAGE = "HIE_RAW_MESSAGE";
    public static final String HEADER_TRIGGER_TYPE = "HIE_TRIGGER_TYPE";

    public static final String EXTENSION_RACE = "http://hl7.org/fhir/us/core/StructureDefinition/us-core-race";
    public static final String EXTENSION_ETHNICITY = "http://hl7.org/fhir/us/core/StructureDefinition/us-core-ethnicity";
    public static final String EXTENSION_RELIGION = "http://hl7.org/fhir/StructureDefinition/patient-religion";
    public static final String EXTENSION_BIRTH_PLACE = "http://hl7.org/fhir/StructureDefinition/patient-birthPlace";
    public static final String EXTENSION_CITIZENSHIP = "http://hl7.org/fhir/StructureDefinition/patient-citizenship";
    public static final String EXTENSION_MOTHER_MAIDEN_NAME = "http://hl7.org/fhir/StructureDefinition/patient-mothersMaidenName";

    private final FhirContext fhirContext;
    private final ObjectMapper objectMapper;

    public PatientDemographicsUpdate() {
        super(DEFAULT_ACTIVITY_ID, DEFAULT_ACTIVITY_NAME);
        setActivityDescription("Extracts ADT demographic information and updates the corresponding Patient resource within the HIE");
        this.fhirContext = FhirContext.forR5();
        this.objectMapper = new ObjectMapper();
    }

    public PatientDemographicsUpdate(CamelContext context) {
        super(context, DEFAULT_ACTIVITY_ID, DEFAULT_ACTIVITY_NAME);
        setActivityDescription("Extracts ADT demographic information and updates the corresponding Patient resource within the HIE");
        this.fhirContext = FhirContext.forR5();
        this.objectMapper = new ObjectMapper();
    }

    public PatientDemographicsUpdate(String activityId, String activityName) {
        super(activityId, activityName);
        this.fhirContext = FhirContext.forR5();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void processActivity(Exchange exchange) throws Exception {
        processPatientDemographics(exchange);
    }

    /**
     * Core processing logic that extracts demographic information from incoming ADT event trigger messages,
     * updates the corresponding Patient resource and Task output components, and sets the Task on the exchange body for egress.
     *
     * @param exchange Camel Exchange
     */
    public void processPatientDemographics(Exchange exchange) {
        if (exchange == null || exchange.getMessage() == null) {
            log.warn("Null exchange or message received in PatientDemographicsUpdate");
            return;
        }

        Object body = exchange.getMessage().getBody();
        String rawHeader = exchange.getMessage().getHeader(HEADER_RAW_MESSAGE, String.class);
        if (StringUtils.isBlank(rawHeader)) {
            rawHeader = (String) exchange.getProperty(HEADER_RAW_MESSAGE);
        }

        Task task = null;
        Patient patient = null;
        String rawPayload = extractRawPayload(body);
        boolean adtParsed = false;

        try {
            // 1. Resolve existing Task or Patient instance from exchange
            if (body instanceof Task) {
                task = (Task) body;
                if (task.hasContained()) {
                    for (Resource res : task.getContained()) {
                        if (res instanceof Patient) {
                            patient = (Patient) res;
                            break;
                        }
                    }
                }
                if (patient == null && task.hasFor() && task.getFor().hasReference() && !task.getFor().getReference().contains("task-patient")) {
                    patient = extractPatientFromTaskResource(task);
                }
                if (patient == null && StringUtils.isNotBlank(rawHeader)) {
                    if (isHl7Message(rawHeader)) {
                        patient = new Patient();
                        patient.setActive(true);
                        parseAndApplyHl7AdtDemographics(patient, rawHeader);
                        adtParsed = true;
                    } else if (rawHeader.trim().startsWith("{")) {
                        patient = parseJsonPayload(rawHeader);
                    }
                }
                if (patient == null) {
                    patient = extractPatientFromTaskResource(task);
                }
            } else if (body instanceof Patient) {
                patient = (Patient) body;
            } else if (StringUtils.isNotBlank(rawPayload)) {
                String trimmed = rawPayload.trim();
                if (trimmed.startsWith("{") || trimmed.startsWith("[")) {
                    patient = parseJsonPayload(trimmed);
                } else if (trimmed.startsWith("<")) {
                    patient = parseXmlPayload(trimmed);
                } else if (isHl7Message(trimmed)) {
                    patient = new Patient();
                    patient.setActive(true);
                    parseAndApplyHl7AdtDemographics(patient, trimmed);
                    adtParsed = true;
                }
            }

            // 2. If we have an ADT HL7 message (in header or body) and have not parsed it yet, enrich with ADT demographics
            if (!adtParsed) {
                String adtSource = null;
                if (StringUtils.isNotBlank(rawHeader) && isHl7Message(rawHeader)) {
                    adtSource = rawHeader;
                } else if (StringUtils.isNotBlank(rawPayload) && isHl7Message(rawPayload)) {
                    adtSource = rawPayload;
                }

                if (adtSource != null) {
                    if (patient == null) {
                        patient = new Patient();
                        patient.setActive(true);
                    }
                    parseAndApplyHl7AdtDemographics(patient, adtSource);
                }
            }
        } catch (Exception e) {
            log.warn("Error parsing or updating patient demographics: {}. Preserving baseline patient.", e.getMessage(), e);
        }

        if (patient == null) {
            log.debug("No patient resource could be derived or updated. Creating fallback patient.");
            patient = createFallbackPatient(rawPayload != null ? rawPayload : "unknown");
        }

        // Set last updated timestamp
        patient.getMeta().setLastUpdated(new Date());

        // Extract metadata for headers
        String patientId = patient.getIdPart();
        String mrn = extractMrn(patient);
        String fullName = extractFullName(patient);
        String gender = patient.hasGender() ? patient.getGender().toCode() : null;
        String dob = null;
        if (patient.hasBirthDate()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            dob = sdf.format(patient.getBirthDate());
        }
        String maritalStatus = null;
        if (patient.hasMaritalStatus() && patient.getMaritalStatus().hasText()) {
            maritalStatus = patient.getMaritalStatus().getText();
        } else if (patient.hasMaritalStatus() && patient.getMaritalStatus().hasCoding()) {
            maritalStatus = patient.getMaritalStatus().getCodingFirstRep().getDisplay();
        }

        if (task == null) {
            Object incTask = exchange.getProperty(PROPERTY_INCOMING_TASK);
            if (incTask instanceof Task) {
                task = (Task) incTask;
            } else {
                task = new Task();
                String tId = exchange.getMessage().getHeader(HEADER_TASK_ID, String.class);
                if (StringUtils.isBlank(tId)) {
                    tId = patientId != null ? patientId : UUID.randomUUID().toString();
                }
                task.setId("Task/" + cleanId(tId));
                task.setStatus(Task.TaskStatus.INPROGRESS);
                task.setAuthoredOn(new Date());
            }
        }

        // Attach Patient to Task
        task.setFor(new Reference("Patient/" + patient.getIdPart()).setDisplay(fullName));

        // Update contained resources
        boolean containedExists = false;
        for (int i = 0; i < task.getContained().size(); i++) {
            Resource r = task.getContained().get(i);
            if (r instanceof Patient && (Objects.equals(r.getIdPart(), patient.getIdPart()) || !r.hasId())) {
                task.getContained().set(i, patient);
                containedExists = true;
                break;
            }
        }
        if (!containedExists) {
            task.addContained(patient);
        }

        // Add discrete output component for Patient Demographics Resource
        Task.TaskOutputComponent output = task.addOutput();
        output.getType().setText("Patient Demographics Resource").addCoding()
                .setSystem("http://hl7.org/fhir/resource-types")
                .setCode("Patient")
                .setDisplay("Patient");
        output.setValue(new Reference("Patient/" + patient.getIdPart()).setDisplay(fullName));

        task.setLastModified(new Date());

        // Set updated Task resource on exchange body for TaskProcessingActivity egress
        exchange.getMessage().setBody(task);

        // Set / update headers
        if (StringUtils.isNotBlank(patientId)) {
            exchange.getMessage().setHeader(HEADER_PATIENT_ID, patientId);
        }
        if (StringUtils.isNotBlank(mrn)) {
            exchange.getMessage().setHeader(HEADER_PATIENT_MRN, mrn);
        }
        if (StringUtils.isNotBlank(fullName)) {
            exchange.getMessage().setHeader(HEADER_PATIENT_NAME, fullName);
        }
        if (StringUtils.isNotBlank(gender)) {
            exchange.getMessage().setHeader(HEADER_PATIENT_GENDER, gender);
        }
        if (StringUtils.isNotBlank(dob)) {
            exchange.getMessage().setHeader(HEADER_PATIENT_DOB, dob);
        }
        if (StringUtils.isNotBlank(maritalStatus)) {
            exchange.getMessage().setHeader(HEADER_PATIENT_MARITAL_STATUS, maritalStatus);
        }
        exchange.getMessage().setHeader(HEADER_PATIENT_UPDATED, Boolean.TRUE);
        exchange.getMessage().setHeader(HEADER_PATIENT_DEMOGRAPHICS_UPDATED, Boolean.TRUE);

        log.info("Patient demographics updated on exchange: ID={}, MRN={}, Name={}, Gender={}, DOB={}, MaritalStatus={}",
                patientId, mrn, fullName, gender, dob, maritalStatus);
    }

    /**
     * Parses all demographic segments from an HL7 v2 ADT message and updates the Patient resource.
     *
     * @param patient    target Patient resource to enrich/update
     * @param hl7Message raw HL7 v2 ADT message string
     */
    public void parseAndApplyHl7AdtDemographics(Patient patient, String hl7Message) {
        if (patient == null || StringUtils.isBlank(hl7Message)) {
            return;
        }

        String[] lines = hl7Message.split("\r\n|\r|\n");
        List<String> nk1Lines = new ArrayList<>();
        String pidLine = null;
        String pd1Line = null;

        for (String rawLine : lines) {
            String line = rawLine.trim();
            if (line.startsWith("PID|")) {
                pidLine = line;
            } else if (line.startsWith("NK1|")) {
                nk1Lines.add(line);
            } else if (line.startsWith("PD1|")) {
                pd1Line = line;
            }
        }

        if (pidLine != null) {
            parsePidDemographics(patient, pidLine);
        }

        if (!nk1Lines.isEmpty()) {
            parseNk1Contacts(patient, nk1Lines);
        }

        if (pd1Line != null) {
            parsePd1Demographics(patient, pd1Line);
        }
    }

    /**
     * Extracts detailed demographic fields from an HL7 PID segment and applies them to the Patient resource.
     */
    public void parsePidDemographics(Patient patient, String pidLine) {
        if (patient == null || StringUtils.isBlank(pidLine)) {
            return;
        }

        String[] fields = pidLine.split("\\|", -1);

        // PID-3: Patient Identifier List
        String pid3 = fields.length > 3 ? fields[3] : "";
        if (StringUtils.isNotBlank(pid3)) {
            String[] idComponents = pid3.split("\\^", -1);
            String idVal = idComponents.length > 0 ? idComponents[0].trim() : "";
            String authority = idComponents.length > 3 ? idComponents[3].trim() : "";
            if (StringUtils.isNotBlank(idVal)) {
                if (!patient.hasId()) {
                    patient.setId("Patient/" + cleanId(idVal));
                }
                boolean hasMrn = patient.getIdentifier().stream()
                        .anyMatch(id -> id.hasValue() && id.getValue().equalsIgnoreCase(idVal));
                if (!hasMrn) {
                    Identifier mrn = patient.addIdentifier();
                    mrn.setUse(Identifier.IdentifierUse.USUAL);
                    mrn.setType(new CodeableConcept().addCoding(
                            new Coding("http://terminology.hl7.org/CodeSystem/v2-0203", "MR", "Medical Record Number")));
                    if (StringUtils.isNotBlank(authority)) {
                        mrn.setSystem("http://example.org/patients/" + authority.toLowerCase());
                    } else {
                        mrn.setSystem("http://example.org/patients");
                    }
                    mrn.setValue(cleanId(idVal));
                }
            }
        }

        // PID-5: Patient Name
        String pid5 = fields.length > 5 ? fields[5] : "";
        if (StringUtils.isNotBlank(pid5)) {
            String[] nameParts = pid5.split("\\^", -1);
            String family = nameParts.length > 0 ? nameParts[0].trim() : "";
            String given = nameParts.length > 1 ? nameParts[1].trim() : "";
            String middle = nameParts.length > 2 ? nameParts[2].trim() : "";
            String suffix = nameParts.length > 3 ? nameParts[3].trim() : "";
            String prefix = nameParts.length > 4 ? nameParts[4].trim() : "";

            if (!patient.hasName() || patient.getNameFirstRep().isEmpty()) {
                HumanName name = patient.addName();
                name.setUse(HumanName.NameUse.OFFICIAL);
                if (StringUtils.isNotBlank(family)) name.setFamily(family);
                if (StringUtils.isNotBlank(given)) name.addGiven(given);
                if (StringUtils.isNotBlank(middle)) name.addGiven(middle);
                if (StringUtils.isNotBlank(prefix)) name.addPrefix(prefix);
                if (StringUtils.isNotBlank(suffix)) name.addSuffix(suffix);
                name.setText(buildFullName(given, middle, family, prefix, suffix));
            } else {
                HumanName existingName = patient.getNameFirstRep();
                if (StringUtils.isNotBlank(family) && !existingName.hasFamily()) existingName.setFamily(family);
                if (StringUtils.isNotBlank(given) && existingName.getGiven().isEmpty()) existingName.addGiven(given);
                if (StringUtils.isNotBlank(middle) && existingName.getGiven().size() < 2) existingName.addGiven(middle);
                if (StringUtils.isNotBlank(prefix) && existingName.getPrefix().isEmpty()) existingName.addPrefix(prefix);
                if (StringUtils.isNotBlank(suffix) && existingName.getSuffix().isEmpty()) existingName.addSuffix(suffix);
                if (!existingName.hasText() || StringUtils.isBlank(existingName.getText())) {
                    existingName.setText(buildFullName(given, middle, family, prefix, suffix));
                }
            }
        }

        // PID-6: Mother's Maiden Name
        String pid6 = fields.length > 6 ? fields[6].trim() : "";
        if (StringUtils.isNotBlank(pid6)) {
            String maidenName = pid6.replaceAll("\\^", " ").trim();
            removeExtension(patient, EXTENSION_MOTHER_MAIDEN_NAME);
            patient.addExtension(new Extension(EXTENSION_MOTHER_MAIDEN_NAME, new StringType(maidenName)));
        }

        // PID-7: Date of Birth
        String pid7 = fields.length > 7 ? fields[7].trim() : "";
        if (StringUtils.isNotBlank(pid7)) {
            Date dob = parseDate(pid7);
            if (dob != null) {
                patient.setBirthDate(dob);
            }
        }

        // PID-8: Administrative Sex / Gender
        String pid8 = fields.length > 8 ? fields[8].trim() : "";
        if (StringUtils.isNotBlank(pid8)) {
            patient.setGender(parseAdministrativeGender(pid8));
        }

        // PID-10: Race
        String pid10 = fields.length > 10 ? fields[10].trim() : "";
        if (StringUtils.isNotBlank(pid10)) {
            applyRaceExtension(patient, pid10);
        }

        // PID-11: Patient Address(es)
        String pid11 = fields.length > 11 ? fields[11].trim() : "";
        if (StringUtils.isNotBlank(pid11)) {
            parseAndApplyAddresses(patient, pid11);
        }

        // PID-12: County Code
        String pid12 = fields.length > 12 ? fields[12].trim() : "";
        if (StringUtils.isNotBlank(pid12) && patient.hasAddress()) {
            patient.getAddressFirstRep().setDistrict(pid12);
        }

        // PID-13: Phone - Home
        String pid13 = fields.length > 13 ? fields[13].trim() : "";
        if (StringUtils.isNotBlank(pid13)) {
            parseAndApplyTelecoms(patient, pid13, ContactPoint.ContactPointUse.HOME);
        }

        // PID-14: Phone - Business
        String pid14 = fields.length > 14 ? fields[14].trim() : "";
        if (StringUtils.isNotBlank(pid14)) {
            parseAndApplyTelecoms(patient, pid14, ContactPoint.ContactPointUse.WORK);
        }

        // PID-15: Primary Language
        String pid15 = fields.length > 15 ? fields[15].trim() : "";
        if (StringUtils.isNotBlank(pid15)) {
            applyLanguage(patient, pid15);
        }

        // PID-16: Marital Status
        String pid16 = fields.length > 16 ? fields[16].trim() : "";
        if (StringUtils.isNotBlank(pid16)) {
            patient.setMaritalStatus(parseMaritalStatus(pid16));
        }

        // PID-17: Religion
        String pid17 = fields.length > 17 ? fields[17].trim() : "";
        if (StringUtils.isNotBlank(pid17)) {
            removeExtension(patient, EXTENSION_RELIGION);
            patient.addExtension(new Extension(EXTENSION_RELIGION,
                    new CodeableConcept().setText(pid17.replaceAll("\\^", " ").trim())));
        }

        // PID-18: Patient Account Number
        String pid18 = fields.length > 18 ? fields[18].trim() : "";
        if (StringUtils.isNotBlank(pid18)) {
            String accNum = pid18.split("\\^", -1)[0].trim();
            boolean hasAcc = patient.getIdentifier().stream()
                    .anyMatch(id -> id.hasType() && id.getType().hasCoding("http://terminology.hl7.org/CodeSystem/v2-0203", "AN"));
            if (!hasAcc && StringUtils.isNotBlank(accNum)) {
                Identifier acc = patient.addIdentifier();
                acc.setType(new CodeableConcept().addCoding(
                        new Coding("http://terminology.hl7.org/CodeSystem/v2-0203", "AN", "Account Number")));
                acc.setSystem("http://example.org/accounts");
                acc.setValue(accNum);
            }
        }

        // PID-19: SSN
        String pid19 = fields.length > 19 ? fields[19].trim() : "";
        if (StringUtils.isNotBlank(pid19)) {
            String ssnVal = pid19.split("\\^", -1)[0].trim();
            boolean hasSsn = patient.getIdentifier().stream()
                    .anyMatch(id -> id.hasSystem() && id.getSystem().contains("us-ssn"));
            if (!hasSsn && StringUtils.isNotBlank(ssnVal)) {
                Identifier ssn = patient.addIdentifier();
                ssn.setType(new CodeableConcept().addCoding(
                        new Coding("http://terminology.hl7.org/CodeSystem/v2-0203", "SS", "Social Security Number")));
                ssn.setSystem("http://hl7.org/fhir/sid/us-ssn");
                ssn.setValue(ssnVal);
            }
        }

        // PID-22: Ethnic Group
        String pid22 = fields.length > 22 ? fields[22].trim() : "";
        if (StringUtils.isNotBlank(pid22)) {
            applyEthnicityExtension(patient, pid22);
        }

        // PID-23: Birth Place
        String pid23 = fields.length > 23 ? fields[23].trim() : "";
        if (StringUtils.isNotBlank(pid23)) {
            removeExtension(patient, EXTENSION_BIRTH_PLACE);
            patient.addExtension(new Extension(EXTENSION_BIRTH_PLACE, new StringType(pid23.replaceAll("\\^", " ").trim())));
        }

        // PID-24: Multiple Birth Indicator
        String pid24 = fields.length > 24 ? fields[24].trim() : "";
        if (StringUtils.isNotBlank(pid24)) {
            patient.setMultipleBirth(new BooleanType("Y".equalsIgnoreCase(pid24)));
        }

        // PID-25: Birth Order
        String pid25 = fields.length > 25 ? fields[25].trim() : "";
        if (StringUtils.isNotBlank(pid25)) {
            try {
                int order = Integer.parseInt(pid25);
                patient.setMultipleBirth(new IntegerType(order));
            } catch (NumberFormatException ignored) {
            }
        }

        // PID-26: Citizenship
        String pid26 = fields.length > 26 ? fields[26].trim() : "";
        if (StringUtils.isNotBlank(pid26)) {
            removeExtension(patient, EXTENSION_CITIZENSHIP);
            patient.addExtension(new Extension(EXTENSION_CITIZENSHIP,
                    new CodeableConcept().setText(pid26.replaceAll("\\^", " ").trim())));
        }

        // PID-29: Patient Death Date in Time
        String pid29 = fields.length > 29 ? fields[29].trim() : "";
        if (StringUtils.isNotBlank(pid29)) {
            Date deathDate = parseDate(pid29);
            if (deathDate != null) {
                patient.setDeceased(new DateTimeType(deathDate));
            }
        }

        // PID-30: Patient Death Indicator
        String pid30 = fields.length > 30 ? fields[30].trim() : "";
        if ("Y".equalsIgnoreCase(pid30) && !patient.hasDeceased()) {
            patient.setDeceased(new BooleanType(true));
        }
    }

    /**
     * Extracts contact and next-of-kin information from NK1 segments and adds them to Patient contacts.
     */
    public void parseNk1Contacts(Patient patient, List<String> nk1Lines) {
        if (patient == null || nk1Lines == null || nk1Lines.isEmpty()) {
            return;
        }

        for (String nk1Line : nk1Lines) {
            String[] fields = nk1Line.split("\\|", -1);
            if (fields.length < 3) {
                continue;
            }

            Patient.ContactComponent contact = new Patient.ContactComponent();

            // NK1-2: Contact Name
            String nk12 = fields.length > 2 ? fields[2].trim() : "";
            if (StringUtils.isNotBlank(nk12)) {
                String[] nameParts = nk12.split("\\^", -1);
                HumanName name = new HumanName();
                name.setUse(HumanName.NameUse.OFFICIAL);
                if (nameParts.length > 0 && StringUtils.isNotBlank(nameParts[0])) name.setFamily(nameParts[0].trim());
                if (nameParts.length > 1 && StringUtils.isNotBlank(nameParts[1])) name.addGiven(nameParts[1].trim());
                if (nameParts.length > 2 && StringUtils.isNotBlank(nameParts[2])) name.addGiven(nameParts[2].trim());
                if (nameParts.length > 3 && StringUtils.isNotBlank(nameParts[3])) name.addSuffix(nameParts[3].trim());
                if (nameParts.length > 4 && StringUtils.isNotBlank(nameParts[4])) name.addPrefix(nameParts[4].trim());
                name.setText(buildFullName(
                        nameParts.length > 1 ? nameParts[1] : "",
                        nameParts.length > 2 ? nameParts[2] : "",
                        nameParts.length > 0 ? nameParts[0] : "",
                        nameParts.length > 4 ? nameParts[4] : "",
                        nameParts.length > 3 ? nameParts[3] : ""));
                contact.setName(name);
            }

            // NK1-3: Relationship
            String nk13 = fields.length > 3 ? fields[3].trim() : "";
            if (StringUtils.isNotBlank(nk13)) {
                String[] relParts = nk13.split("\\^", -1);
                String relCode = relParts.length > 0 ? relParts[0].trim() : "";
                String relDisplay = relParts.length > 1 ? relParts[1].trim() : relCode;
                CodeableConcept relConcept = new CodeableConcept();
                relConcept.addCoding(new Coding("http://terminology.hl7.org/CodeSystem/v2-0131", relCode, relDisplay));
                relConcept.setText(relDisplay);
                contact.addRelationship(relConcept);
            }

            // NK1-4: Address
            String nk14 = fields.length > 4 ? fields[4].trim() : "";
            if (StringUtils.isNotBlank(nk14)) {
                Address addr = parseSingleAddress(nk14, Address.AddressUse.HOME);
                if (addr != null) {
                    contact.setAddress(addr);
                }
            }

            // NK1-5: Phone Number
            String nk15 = fields.length > 5 ? fields[5].trim() : "";
            if (StringUtils.isNotBlank(nk15)) {
                for (String phoneToken : nk15.split("~")) {
                    ContactPoint cp = parseSingleTelecom(phoneToken, ContactPoint.ContactPointUse.HOME);
                    if (cp != null) {
                        contact.addTelecom(cp);
                    }
                }
            }

            // NK1-6: Business Phone
            String nk16 = fields.length > 6 ? fields[6].trim() : "";
            if (StringUtils.isNotBlank(nk16)) {
                for (String phoneToken : nk16.split("~")) {
                    ContactPoint cp = parseSingleTelecom(phoneToken, ContactPoint.ContactPointUse.WORK);
                    if (cp != null) {
                        contact.addTelecom(cp);
                    }
                }
            }

            // NK1-7: Contact Role
            String nk17 = fields.length > 7 ? fields[7].trim() : "";
            if (StringUtils.isNotBlank(nk17)) {
                String[] roleParts = nk17.split("\\^", -1);
                String roleCode = roleParts.length > 0 ? roleParts[0].trim() : "";
                String roleDisplay = roleParts.length > 1 ? roleParts[1].trim() : roleCode;
                CodeableConcept roleConcept = new CodeableConcept();
                roleConcept.addCoding(new Coding("http://terminology.hl7.org/CodeSystem/v2-0131", roleCode, roleDisplay));
                roleConcept.setText(roleDisplay);
                contact.addRelationship(roleConcept);
            }

            boolean duplicate = patient.getContact().stream()
                    .anyMatch(c -> c.hasName() && contact.hasName() &&
                            Objects.equals(c.getName().getText(), contact.getName().getText()));
            if (!duplicate) {
                patient.addContact(contact);
            }
        }
    }

    /**
     * Extracts facility and general practitioner references from PD1 segment.
     */
    public void parsePd1Demographics(Patient patient, String pd1Line) {
        if (patient == null || StringUtils.isBlank(pd1Line)) {
            return;
        }

        String[] fields = pd1Line.split("\\|", -1);

        // PD1-3: Patient Primary Facility
        String pd13 = fields.length > 3 ? fields[3].trim() : "";
        if (StringUtils.isNotBlank(pd13)) {
            String[] facParts = pd13.split("\\^", -1);
            String facId = facParts.length > 0 ? facParts[0].trim() : "";
            String facName = facParts.length > 1 ? facParts[1].trim() : facId;
            if (StringUtils.isNotBlank(facId)) {
                Reference orgRef = new Reference("Organization/" + cleanId(facId));
                orgRef.setDisplay(facName);
                patient.setManagingOrganization(orgRef);
            }
        }

        // PD1-4: Patient Primary Care Provider
        String pd14 = fields.length > 4 ? fields[4].trim() : "";
        if (StringUtils.isNotBlank(pd14)) {
            String[] provParts = pd14.split("\\^", -1);
            String provId = provParts.length > 0 ? provParts[0].trim() : "";
            String provFamily = provParts.length > 1 ? provParts[1].trim() : "";
            String provGiven = provParts.length > 2 ? provParts[2].trim() : "";
            String provName = StringUtils.isNotBlank(provGiven) || StringUtils.isNotBlank(provFamily)
                    ? (provGiven + " " + provFamily).trim()
                    : provId;
            if (StringUtils.isNotBlank(provId)) {
                Reference practitionerRef = new Reference("Practitioner/" + cleanId(provId));
                practitionerRef.setDisplay(provName);
                patient.addGeneralPractitioner(practitionerRef);
            }
        }
    }

    /**
     * Parses and applies multiple addresses separated by '~' to the Patient resource.
     */
    private void parseAndApplyAddresses(Patient patient, String rawAddressField) {
        String[] addressTokens = rawAddressField.split("~");
        for (String addrToken : addressTokens) {
            Address address = parseSingleAddress(addrToken, Address.AddressUse.HOME);
            if (address != null && !isDuplicateAddress(patient, address)) {
                patient.addAddress(address);
            }
        }
    }

    private Address parseSingleAddress(String addrToken, Address.AddressUse defaultUse) {
        if (StringUtils.isBlank(addrToken)) {
            return null;
        }
        String[] parts = addrToken.split("\\^", -1);
        String street = parts.length > 0 ? parts[0].trim() : "";
        String otherAddr = parts.length > 1 ? parts[1].trim() : "";
        String city = parts.length > 2 ? parts[2].trim() : "";
        String state = parts.length > 3 ? parts[3].trim() : "";
        String zip = parts.length > 4 ? parts[4].trim() : "";
        String country = parts.length > 5 ? parts[5].trim() : "";
        String addrType = parts.length > 6 ? parts[6].trim() : "";

        if (StringUtils.isAllBlank(street, otherAddr, city, state, zip, country)) {
            return null;
        }

        Address address = new Address();
        address.setUse(mapAddressUse(addrType, defaultUse));
        if (StringUtils.isNotBlank(street)) address.addLine(street);
        if (StringUtils.isNotBlank(otherAddr)) address.addLine(otherAddr);
        if (StringUtils.isNotBlank(city)) address.setCity(city);
        if (StringUtils.isNotBlank(state)) address.setState(state);
        if (StringUtils.isNotBlank(zip)) address.setPostalCode(zip);
        if (StringUtils.isNotBlank(country)) address.setCountry(country);

        return address;
    }

    private boolean isDuplicateAddress(Patient patient, Address newAddr) {
        if (!patient.hasAddress()) return false;
        for (Address existing : patient.getAddress()) {
            if (StringUtils.equalsIgnoreCase(existing.getPostalCode(), newAddr.getPostalCode())
                    && StringUtils.equalsIgnoreCase(existing.getCity(), newAddr.getCity())
                    && Objects.equals(existing.getLine(), newAddr.getLine())) {
                return true;
            }
        }
        return false;
    }

    private Address.AddressUse mapAddressUse(String hl7AddrType, Address.AddressUse defaultUse) {
        if (StringUtils.isBlank(hl7AddrType)) {
            return defaultUse != null ? defaultUse : Address.AddressUse.HOME;
        }
        return switch (hl7AddrType.toUpperCase()) {
            case "H", "P" -> Address.AddressUse.HOME;
            case "B", "O" -> Address.AddressUse.WORK;
            case "C" -> Address.AddressUse.TEMP;
            case "M" -> Address.AddressUse.BILLING;
            default -> defaultUse != null ? defaultUse : Address.AddressUse.HOME;
        };
    }

    /**
     * Parses and applies telecoms to Patient resource.
     */
    private void parseAndApplyTelecoms(Patient patient, String rawTelecomField, ContactPoint.ContactPointUse defaultUse) {
        String[] tokens = rawTelecomField.split("~");
        for (String token : tokens) {
            ContactPoint cp = parseSingleTelecom(token, defaultUse);
            if (cp != null && !isDuplicateTelecom(patient, cp)) {
                patient.addTelecom(cp);
            }
        }
    }

    private ContactPoint parseSingleTelecom(String token, ContactPoint.ContactPointUse defaultUse) {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        String[] parts = token.split("\\^", -1);
        String val = parts.length > 0 ? parts[0].trim() : "";
        String teleUse = parts.length > 1 ? parts[1].trim() : "";
        String teleType = parts.length > 2 ? parts[2].trim() : "";
        String email = parts.length > 3 ? parts[3].trim() : "";

        if (token.contains("@") && StringUtils.isBlank(email)) {
            email = val;
        }

        if (StringUtils.isNotBlank(email) && email.contains("@")) {
            ContactPoint cp = new ContactPoint();
            cp.setSystem(ContactPoint.ContactPointSystem.EMAIL);
            cp.setValue(email);
            cp.setUse(defaultUse != null ? defaultUse : ContactPoint.ContactPointUse.HOME);
            return cp;
        }

        if (StringUtils.isNotBlank(val)) {
            ContactPoint cp = new ContactPoint();
            cp.setSystem(mapTelecomSystem(teleType));
            cp.setValue(val);
            cp.setUse(mapTelecomUse(teleUse, defaultUse));
            return cp;
        }

        return null;
    }

    private boolean isDuplicateTelecom(Patient patient, ContactPoint cp) {
        if (!patient.hasTelecom()) return false;
        for (ContactPoint existing : patient.getTelecom()) {
            if (StringUtils.equalsIgnoreCase(existing.getValue(), cp.getValue())) {
                return true;
            }
        }
        return false;
    }

    private ContactPoint.ContactPointSystem mapTelecomSystem(String type) {
        if (StringUtils.isBlank(type)) return ContactPoint.ContactPointSystem.PHONE;
        return switch (type.toUpperCase()) {
            case "NET", "INTERNET", "EMAIL" -> ContactPoint.ContactPointSystem.EMAIL;
            case "FAX", "FX" -> ContactPoint.ContactPointSystem.FAX;
            case "CP", "CELL", "MOBILE" -> ContactPoint.ContactPointSystem.SMS;
            default -> ContactPoint.ContactPointSystem.PHONE;
        };
    }

    private ContactPoint.ContactPointUse mapTelecomUse(String use, ContactPoint.ContactPointUse defaultUse) {
        if (StringUtils.isBlank(use)) return defaultUse != null ? defaultUse : ContactPoint.ContactPointUse.HOME;
        return switch (use.toUpperCase()) {
            case "PRN", "ORN", "HOME", "H" -> ContactPoint.ContactPointUse.HOME;
            case "WPN", "WORK", "B", "W" -> ContactPoint.ContactPointUse.WORK;
            case "MOB", "MOBILE", "MC" -> ContactPoint.ContactPointUse.MOBILE;
            case "TMP", "TEMP" -> ContactPoint.ContactPointUse.TEMP;
            default -> defaultUse != null ? defaultUse : ContactPoint.ContactPointUse.HOME;
        };
    }

    /**
     * Maps HL7 language code to FHIR Patient communication component.
     */
    private void applyLanguage(Patient patient, String languageField) {
        String[] parts = languageField.split("\\^", -1);
        String code = parts.length > 0 ? parts[0].trim() : "";
        String display = parts.length > 1 ? parts[1].trim() : code;

        if (StringUtils.isBlank(code) && StringUtils.isBlank(display)) return;

        boolean alreadyHas = patient.getCommunication().stream()
                .anyMatch(c -> c.hasLanguage() && (StringUtils.equalsIgnoreCase(c.getLanguage().getText(), display)
                        || c.getLanguage().hasCoding("urn:ietf:bcp:47", code)));

        if (!alreadyHas) {
            Patient.PatientCommunicationComponent comm = patient.addCommunication();
            comm.setPreferred(true);
            CodeableConcept concept = new CodeableConcept();
            if (StringUtils.isNotBlank(code)) {
                concept.addCoding(new Coding("urn:ietf:bcp:47", code, display));
            }
            concept.setText(StringUtils.isNotBlank(display) ? display : code);
            comm.setLanguage(concept);
        }
    }

    /**
     * Applies US Core Race extension.
     */
    private void applyRaceExtension(Patient patient, String raceField) {
        String[] parts = raceField.split("\\^", -1);
        String code = parts.length > 0 ? parts[0].trim() : "";
        String display = parts.length > 1 ? parts[1].trim() : code;

        removeExtension(patient, EXTENSION_RACE);
        Extension raceExt = new Extension(EXTENSION_RACE);
        if (StringUtils.isNotBlank(code)) {
            raceExt.addExtension(new Extension("ombCategory", new Coding("urn:oid:2.16.840.1.113883.6.238", code, display)));
        }
        raceExt.addExtension(new Extension("text", new StringType(StringUtils.isNotBlank(display) ? display : code)));
        patient.addExtension(raceExt);
    }

    /**
     * Applies US Core Ethnicity extension.
     */
    private void applyEthnicityExtension(Patient patient, String ethnicityField) {
        String[] parts = ethnicityField.split("\\^", -1);
        String code = parts.length > 0 ? parts[0].trim() : "";
        String display = parts.length > 1 ? parts[1].trim() : code;

        removeExtension(patient, EXTENSION_ETHNICITY);
        Extension ethExt = new Extension(EXTENSION_ETHNICITY);
        if (StringUtils.isNotBlank(code)) {
            ethExt.addExtension(new Extension("ombCategory", new Coding("urn:oid:2.16.840.1.113883.6.238", code, display)));
        }
        ethExt.addExtension(new Extension("text", new StringType(StringUtils.isNotBlank(display) ? display : code)));
        patient.addExtension(ethExt);
    }

    private void removeExtension(Patient patient, String url) {
        if (patient != null && patient.hasExtension() && url != null) {
            patient.getExtension().removeIf(ext -> url.equals(ext.getUrl()));
        }
    }

    /**
     * Maps HL7 PID-16 marital status code to FHIR MaritalStatus CodeableConcept.
     */
    public CodeableConcept parseMaritalStatus(String maritalCode) {
        String code = maritalCode.split("\\^", -1)[0].trim().toUpperCase();
        String display;
        String fhirCode;

        switch (code) {
            case "M" -> {
                fhirCode = "M";
                display = "Married";
            }
            case "S" -> {
                fhirCode = "S";
                display = "Never Married";
            }
            case "D" -> {
                fhirCode = "D";
                display = "Divorced";
            }
            case "W" -> {
                fhirCode = "W";
                display = "Widowed";
            }
            case "A" -> {
                fhirCode = "L";
                display = "Legally Separated";
            }
            case "P" -> {
                fhirCode = "T";
                display = "Partner";
            }
            case "U" -> {
                fhirCode = "UNK";
                display = "Unknown";
            }
            default -> {
                fhirCode = code;
                display = maritalCode.contains("^") ? maritalCode.split("\\^")[1].trim() : code;
            }
        }

        CodeableConcept concept = new CodeableConcept();
        concept.addCoding(new Coding("http://terminology.hl7.org/CodeSystem/v3-MaritalStatus", fhirCode, display));
        concept.setText(display);
        return concept;
    }

    /**
     * Maps HL7 Administrative Sex code to FHIR AdministrativeGender.
     */
    public Enumerations.AdministrativeGender parseAdministrativeGender(String genderCode) {
        if (StringUtils.isBlank(genderCode)) {
            return Enumerations.AdministrativeGender.UNKNOWN;
        }
        return switch (genderCode.trim().toUpperCase()) {
            case "M", "MALE" -> Enumerations.AdministrativeGender.MALE;
            case "F", "FEMALE" -> Enumerations.AdministrativeGender.FEMALE;
            case "O", "OTHER" -> Enumerations.AdministrativeGender.OTHER;
            default -> Enumerations.AdministrativeGender.UNKNOWN;
        };
    }

    /**
     * Parses diverse HL7 timestamp formats into Date.
     */
    public Date parseDate(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        String clean = dateStr.trim().replaceAll("[^0-9]", "");
        String[] formats = {
                "yyyyMMddHHmmss",
                "yyyyMMddHHmm",
                "yyyyMMdd",
                "yyyyMM",
                "yyyy"
        };
        for (String fmt : formats) {
            if (clean.length() >= fmt.length()) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat(fmt);
                    sdf.setLenient(false);
                    return sdf.parse(clean.substring(0, fmt.length()));
                } catch (ParseException ignored) {
                }
            }
        }
        return null;
    }

    /**
     * Parses JSON payloads which can be FHIR Patient, FHIR Task, or TaskEvent.
     */
    public Patient parseJsonPayload(String json) {
        if (StringUtils.isBlank(json)) {
            return createFallbackPatient("unknown");
        }
        try {
            JsonNode root = objectMapper.readTree(json);
            String resourceType = root.path("resourceType").asText(null);

            if ("Patient".equalsIgnoreCase(resourceType)) {
                return fhirContext.newJsonParser().parseResource(Patient.class, json);
            } else if ("Task".equalsIgnoreCase(resourceType)) {
                Task task = fhirContext.newJsonParser().parseResource(Task.class, json);
                return extractPatientFromTaskResource(task);
            } else if (root.has("taskId") || root.has("action")) {
                return extractPatientFromTaskEventNode(root);
            } else if (root.has("name") || root.has("identifier") || root.has("gender")) {
                return fhirContext.newJsonParser().parseResource(Patient.class, json);
            }
        } catch (Exception e) {
            log.debug("JSON payload could not be parsed as structured resource: {}", e.getMessage());
        }
        return createFallbackPatient("json-payload");
    }

    /**
     * Parses XML payloads (FHIR Patient or Task).
     */
    public Patient parseXmlPayload(String xml) {
        if (StringUtils.isBlank(xml)) {
            return createFallbackPatient("unknown");
        }
        try {
            IParser parser = fhirContext.newXmlParser();
            if (xml.contains("<Patient") || xml.contains("<patient")) {
                return parser.parseResource(Patient.class, xml);
            } else if (xml.contains("<Task") || xml.contains("<task")) {
                Task task = parser.parseResource(Task.class, xml);
                return extractPatientFromTaskResource(task);
            }
        } catch (Exception e) {
            log.debug("XML payload could not be parsed as FHIR resource: {}", e.getMessage());
        }
        return createFallbackPatient("xml-payload");
    }

    /**
     * Extracts or constructs a Patient from a FHIR Task resource.
     */
    private Patient extractPatientFromTaskResource(Task task) {
        if (task == null) {
            return createFallbackPatient("task-null");
        }

        // Check contained resources first
        if (task.hasContained()) {
            for (Resource res : task.getContained()) {
                if (res instanceof Patient) {
                    return (Patient) res;
                }
            }
        }

        String patientId = null;
        if (task.hasFor() && task.getFor().hasReference()) {
            String ref = task.getFor().getReference();
            patientId = ref.startsWith("Patient/") ? ref.substring(8) : ref;
        }

        String patientName = null;
        if (task.hasFor() && task.getFor().hasDisplay()) {
            patientName = task.getFor().getDisplay();
        }

        if (StringUtils.isBlank(patientId)) {
            patientId = task.getIdPart() != null ? task.getIdPart() : "task-patient";
        }

        Patient patient = new Patient();
        patient.setId("Patient/" + cleanId(patientId));
        patient.setActive(true);

        Identifier mrn = patient.addIdentifier();
        mrn.setUse(Identifier.IdentifierUse.USUAL);
        mrn.setSystem("http://example.org/patients");
        mrn.setValue(cleanId(patientId));

        if (StringUtils.isNotBlank(patientName)) {
            HumanName name = patient.addName();
            name.setUse(HumanName.NameUse.OFFICIAL);
            name.setText(patientName);
            String[] parts = patientName.split(" ");
            if (parts.length > 0) name.addGiven(parts[0]);
            if (parts.length > 1) name.setFamily(parts[parts.length - 1]);
        }

        return patient;
    }

    /**
     * Extracts patient identity info from a TaskEvent JSON node.
     */
    private Patient extractPatientFromTaskEventNode(JsonNode root) {
        String taskId = root.path("taskId").asText(null);
        String description = root.path("description").asText("");

        String extractedName = null;
        Pattern namePattern = Pattern.compile("patient\\s+([A-Za-z\\s\\^]+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = namePattern.matcher(description);
        if (matcher.find()) {
            extractedName = matcher.group(1).trim();
        }

        String extractedId = StringUtils.isNotBlank(taskId) ? taskId : "event-" + UUID.randomUUID().toString().substring(0, 8);

        Patient patient = new Patient();
        patient.setId("Patient/" + cleanId(extractedId));
        patient.setActive(true);

        Identifier mrn = patient.addIdentifier();
        mrn.setUse(Identifier.IdentifierUse.USUAL);
        mrn.setSystem("http://example.org/patients");
        mrn.setValue(cleanId(extractedId));

        if (StringUtils.isNotBlank(extractedName)) {
            HumanName name = patient.addName();
            name.setUse(HumanName.NameUse.OFFICIAL);
            name.setText(extractedName);
            String[] parts = extractedName.split("[\\s\\^]+");
            if (parts.length == 1) {
                name.setFamily(parts[0]);
            } else if (parts.length >= 2) {
                name.addGiven(parts[0]);
                name.setFamily(parts[parts.length - 1]);
            }
        }

        return patient;
    }

    public Patient createFallbackPatient(String sourceId) {
        Patient patient = new Patient();
        String idVal = "PAT-" + cleanId(sourceId != null ? sourceId : UUID.randomUUID().toString().substring(0, 8));
        patient.setId("Patient/" + idVal);
        patient.setActive(true);

        Identifier mrn = patient.addIdentifier();
        mrn.setUse(Identifier.IdentifierUse.USUAL);
        mrn.setSystem("http://example.org/patients");
        mrn.setValue(idVal);

        HumanName name = patient.addName();
        name.setUse(HumanName.NameUse.OFFICIAL);
        name.setFamily("Unknown");
        name.addGiven("Patient");
        name.setText("Patient Unknown");

        patient.setGender(Enumerations.AdministrativeGender.UNKNOWN);
        return patient;
    }

    public String extractMrn(Patient patient) {
        if (patient == null || !patient.hasIdentifier()) {
            return patient != null ? patient.getIdPart() : null;
        }
        for (Identifier id : patient.getIdentifier()) {
            if (id.hasType() && id.getType().hasCoding()) {
                for (Coding coding : id.getType().getCoding()) {
                    if ("MR".equalsIgnoreCase(coding.getCode())) {
                        return id.getValue();
                    }
                }
            }
        }
        return patient.getIdentifierFirstRep().getValue();
    }

    public String extractFullName(Patient patient) {
        if (patient == null || !patient.hasName()) {
            return null;
        }
        HumanName name = patient.getNameFirstRep();
        if (name.hasText() && StringUtils.isNotBlank(name.getText())) {
            return name.getText();
        }
        String given = name.getGivenAsSingleString();
        String family = name.getFamily();
        String prefix = name.getPrefixAsSingleString();
        String suffix = name.getSuffixAsSingleString();
        return buildFullName(given, "", family, prefix, suffix);
    }

    public String buildFullName(String given, String middle, String family, String prefix, String suffix) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(prefix)) sb.append(prefix.trim()).append(" ");
        if (StringUtils.isNotBlank(given)) sb.append(given.trim()).append(" ");
        if (StringUtils.isNotBlank(middle)) sb.append(middle.trim()).append(" ");
        if (StringUtils.isNotBlank(family)) sb.append(family.trim()).append(" ");
        if (StringUtils.isNotBlank(suffix)) sb.append(suffix.trim()).append(" ");
        return sb.toString().trim();
    }

    private String extractRawPayload(Object body) {
        if (body instanceof String) {
            return (String) body;
        } else if (body instanceof byte[]) {
            return new String((byte[]) body, StandardCharsets.UTF_8);
        }
        return body != null ? body.toString() : null;
    }

    public boolean isHl7Message(String payload) {
        if (StringUtils.isBlank(payload)) {
            return false;
        }
        return payload.startsWith("MSH|") || payload.contains("\nMSH|") || payload.contains("\rMSH|")
                || payload.startsWith("PID|") || payload.contains("\nPID|") || payload.contains("\rPID|");
    }

    @Override
    protected String cleanId(String id) {
        if (id == null) return "UNKNOWN";
        String clean = id.trim();
        if (clean.startsWith("#")) {
            clean = clean.substring(1);
        }
        clean = clean.replace("Patient/", "").replace("Task/", "").trim();
        return clean.replaceAll("[^A-Za-z0-9-_.]", "-");
    }
}
