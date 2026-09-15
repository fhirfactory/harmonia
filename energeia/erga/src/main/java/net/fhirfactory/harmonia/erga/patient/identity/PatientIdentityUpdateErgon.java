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

package net.fhirfactory.harmonia.erga.patient.identity;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.Dependent;
import net.fhirfactory.harmonia.erga.base.ErgonBase;
import net.fhirfactory.harmonia.model.ergon.ErgonEvent;
import net.fhirfactory.harmonia.model.ergon.ErgonPayload;
import net.fhirfactory.harmonia.model.pragma.Pragma;
import net.fhirfactory.harmonia.model.pragma.PragmaFhirConverter;
import net.fhirfactory.harmonia.model.security.FhirSecurityTagManager;
import net.fhirfactory.harmonia.model.topic.Topic;
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
 * Task Processing Activity for extracting, normalizing, and updating patient identity
 * from diverse incoming message formats (HL7 v2 PID segments, FHIR R5 Patient/Task resources,
 * JSON TaskEvent notifications).
 */
@Dependent
public class PatientIdentityUpdateErgon extends ErgonBase {

    private static final Logger log = LoggerFactory.getLogger(PatientIdentityUpdateErgon.class);

    public static final String DEFAULT_ACTIVITY_ID = "patient-identity-update";
    public static final String DEFAULT_ACTIVITY_NAME = "Patient Identity Update Activity";

    public static final String HEADER_PATIENT_ID = "HIE_PATIENT_ID";
    public static final String HEADER_PATIENT_NAME = "HIE_PATIENT_NAME";
    public static final String HEADER_PATIENT_MRN = "HIE_PATIENT_MRN";
    public static final String HEADER_PATIENT_UPDATED = "HIE_PATIENT_UPDATED";
    public static final String HEADER_RAW_MESSAGE = "HIE_RAW_MESSAGE";

    private final FhirContext fhirContext;
    private final ObjectMapper objectMapper;

    public PatientIdentityUpdateErgon() {
        super(DEFAULT_ACTIVITY_ID, DEFAULT_ACTIVITY_NAME);
        setActivityDescription("Parses incoming messages and extracts or updates patient identity");
        this.fhirContext = FhirContext.forR5();
        this.objectMapper = new ObjectMapper();
    }

    public PatientIdentityUpdateErgon(CamelContext context) {
        super(context, DEFAULT_ACTIVITY_ID, DEFAULT_ACTIVITY_NAME);
        setActivityDescription("Parses incoming messages and extracts or updates patient identity");
        this.fhirContext = FhirContext.forR5();
        this.objectMapper = new ObjectMapper();
    }

    public PatientIdentityUpdateErgon(String activityId, String activityName) {
        super(activityId, activityName);
        this.fhirContext = FhirContext.forR5();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void processActivity(Exchange exchange) throws Exception {
        processPatientIdentity(exchange);
    }

    @Override
    protected void processErgon(Pragma pragma, Exchange exchange) throws Exception {
        processPatientIdentity(exchange);
    }

    /**
     * Core processing logic that parses the exchange payload, extracts patient identity,
     * builds a normalized FHIR Patient resource, updates the Task resource with outputs and contained Patient,
     * and sets the Task resource in the exchange body for activity egress.
     *
     * @param exchange Camel Exchange
     */
    public void processPatientIdentity(Exchange exchange) {
        if (exchange == null || exchange.getMessage() == null) {
            log.warn("Null exchange or message received in PatientIdentityUpdate");
            return;
        }

        Object body = exchange.getMessage().getBody();
        if (body == null) {
            log.warn("Empty/null message body in PatientIdentityUpdate");
            return;
        }

        Task task = null;
        Patient patient = null;
        String rawPayload = extractRawPayload(body);
        String rawHeader = exchange.getMessage().getHeader(HEADER_RAW_MESSAGE, String.class);
        if (StringUtils.isBlank(rawHeader)) {
            rawHeader = (String) exchange.getProperty(HEADER_RAW_MESSAGE);
        }

        try {
            if (body instanceof Pragma) {
                Pragma pragma = (Pragma) body;
                task = PragmaFhirConverter.toFhirTask(pragma);
                if (task != null && task.hasContained()) {
                    for (Resource res : task.getContained()) {
                        if (res instanceof Patient) {
                            patient = (Patient) res;
                            break;
                        }
                    }
                }
                if (patient == null && StringUtils.isNotBlank(rawPayload)) {
                    String trimmed = rawPayload.trim();
                    if (isHl7Message(trimmed)) {
                        patient = parseHl7PidSegment(trimmed);
                    } else if (trimmed.startsWith("{") || trimmed.startsWith("[")) {
                        patient = parseJsonPayload(trimmed);
                    } else if (trimmed.startsWith("<")) {
                        patient = parseXmlPayload(trimmed);
                    }
                }
            } else if (body instanceof Task) {
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
                        patient = parseHl7PidSegment(rawHeader);
                    } else if (rawHeader.trim().startsWith("{")) {
                        patient = parseJsonPayload(rawHeader);
                    } else if (rawHeader.trim().startsWith("<")) {
                        patient = parseXmlPayload(rawHeader);
                    }
                }
                if (patient == null) {
                    patient = extractPatientFromTaskResource(task);
                }
            } else if (body instanceof Patient) {
                patient = (Patient) body;
            } else if (body instanceof ErgonEvent) {
                ErgonEvent event = (ErgonEvent) body;
                if (StringUtils.isNotBlank(event.getTaskId())) {
                    task = getTaskCacheService().getTask(event.getTaskId()).orElse(null);
                    if (task != null && task.hasContained()) {
                        for (Resource res : task.getContained()) {
                            if (res instanceof Patient) {
                                patient = (Patient) res;
                                break;
                            }
                        }
                    }
                }
            } else if (StringUtils.isNotBlank(rawPayload)) {
                String trimmed = rawPayload.trim();
                if (isHl7Message(trimmed)) {
                    patient = parseHl7PidSegment(trimmed);
                } else if (trimmed.startsWith("{") || trimmed.startsWith("[")) {
                    patient = parseJsonPayload(trimmed);
                } else if (trimmed.startsWith("<")) {
                    patient = parseXmlPayload(trimmed);
                } else {
                    patient = createFallbackPatient(trimmed);
                }
            }

            // If raw header contains HL7 message, enrich patient if available
            if (StringUtils.isNotBlank(rawHeader) && isHl7Message(rawHeader)) {
                Patient hl7Patient = parseHl7PidSegment(rawHeader);
                if (hl7Patient != null && (patient == null || "unknown".equalsIgnoreCase(patient.getIdPart())
                        || patient.getIdPart().startsWith("pat-") || patient.getIdPart().startsWith("TASK-") || patient.getIdPart().startsWith("task-"))) {
                    patient = hl7Patient;
                }
            }
        } catch (Exception e) {
            log.warn("Error parsing patient identity from payload: {}. Generating fallback patient.", e.getMessage());
            patient = createFallbackPatient(rawPayload != null ? rawPayload : "unknown");
        }

        if (patient == null) {
            patient = createFallbackPatient("unknown");
        }

        // Extract metadata for headers
        String patientId = cleanPatientId(patient.getIdPart());
        patient.setId("Patient/" + (patientId != null ? patientId : "unknown"));
        FhirSecurityTagManager.applyDefaultSecurityTag(patient);
        String mrn = extractMrn(patient);
        String fullName = extractFullName(patient);

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

        // Add to contained resources if not already present
        final String finalPatientId = patient.getIdPart();
        boolean containedExists = task.getContained().stream()
                .anyMatch(r -> r instanceof Patient && Objects.equals(r.getIdPart(), finalPatientId));
        if (!containedExists) {
            task.addContained(patient);
        }

        // Add discrete output component for Patient Identity Resource
        Task.TaskOutputComponent output = task.addOutput();
        output.getType().setText("Patient Identity Resource").addCoding()
                .setSystem("http://hl7.org/fhir/resource-types")
                .setCode("Patient")
                .setDisplay("Patient");
        output.setValue(new Reference("Patient/" + patient.getIdPart()).setDisplay(fullName));

        task.setLastModified(new Date());
        FhirSecurityTagManager.applyDefaultSecurityTag(task);

        // Set Task as OUT body for TaskProcessingActivity egress
        exchange.getMessage().setBody(task);

        // Set headers on exchange
        if (StringUtils.isNotBlank(rawPayload) && isHl7Message(rawPayload) && exchange.getMessage().getHeader(HEADER_RAW_MESSAGE) == null) {
            exchange.getMessage().setHeader(HEADER_RAW_MESSAGE, rawPayload);
        }
        if (StringUtils.isNotBlank(patientId)) {
            exchange.getMessage().setHeader(HEADER_PATIENT_ID, patientId);
        }
        if (StringUtils.isNotBlank(mrn)) {
            exchange.getMessage().setHeader(HEADER_PATIENT_MRN, mrn);
        }
        if (StringUtils.isNotBlank(fullName)) {
            exchange.getMessage().setHeader(HEADER_PATIENT_NAME, fullName);
        }
        exchange.getMessage().setHeader(HEADER_PATIENT_UPDATED, Boolean.TRUE);

        log.info("Patient identity updated on exchange: ID={}, MRN={}, Name={}", patientId, mrn, fullName);
    }

    private String cleanPatientId(String rawId) {
        if (rawId == null) {
            return null;
        }
        return rawId.replace("#", "").replace("Patient/", "").trim();
    }

    /**
     * Extracts raw string payload from message body.
     */
    private String extractRawPayload(Object body) {
        if (body instanceof Pragma) {
            Pragma pragma = (Pragma) body;
            for (ErgonPayload ep : pragma.getInput()) {
                if (ep.getJsonString() != null && isHl7Message(ep.getJsonString())) {
                    return ep.getJsonString();
                }
            }
            for (ErgonPayload ep : pragma.getInput()) {
                if (ep.getJsonString() != null) {
                    return ep.getJsonString();
                }
            }
        }
        if (body instanceof String) {
            return (String) body;
        } else if (body instanceof byte[]) {
            return new String((byte[]) body, StandardCharsets.UTF_8);
        }
        return body.toString();
    }

    /**
     * Checks if the payload contains HL7 v2 markers.
     */
    public boolean isHl7Message(String payload) {
        if (StringUtils.isBlank(payload)) {
            return false;
        }
        return payload.startsWith("MSH|") || payload.contains("\nMSH|") || payload.contains("\rMSH|")
                || payload.startsWith("PID|") || payload.contains("\nPID|") || payload.contains("\rPID|");
    }

    /**
     * Parses HL7 v2 message to extract patient demographics from the PID segment.
     *
     * @param hl7Message HL7 message string
     * @return constructed FHIR Patient resource
     */
    public Patient parseHl7PidSegment(String hl7Message) {
        if (StringUtils.isBlank(hl7Message)) {
            return createFallbackPatient("unknown");
        }

        String pidLine = null;
        String[] lines = hl7Message.split("\r\n|\r|\n");
        for (String line : lines) {
            if (line.trim().startsWith("PID|")) {
                pidLine = line.trim();
                break;
            }
        }

        if (pidLine == null) {
            log.debug("No PID segment found in HL7 message. Creating baseline patient.");
            return createFallbackPatient("hl7-anonymous");
        }

        String[] fields = pidLine.split("\\|", -1);

        // PID-3: Patient Identifier List (ID^^^AssigningAuthority)
        String pid3 = fields.length > 3 ? fields[3] : "";
        String patientId = "";
        String assigningAuthority = "";
        if (StringUtils.isNotBlank(pid3)) {
            String[] idComponents = pid3.split("\\^", -1);
            patientId = idComponents.length > 0 ? idComponents[0].trim() : "";
            assigningAuthority = idComponents.length > 3 ? idComponents[3].trim() : "";
        }
        if (StringUtils.isBlank(patientId) && fields.length > 2 && StringUtils.isNotBlank(fields[2])) {
            patientId = fields[2].split("\\^", -1)[0].trim();
        }
        if (StringUtils.isBlank(patientId) && fields.length > 4 && StringUtils.isNotBlank(fields[4])) {
            patientId = fields[4].split("\\^", -1)[0].trim();
        }
        if (StringUtils.isBlank(patientId)) {
            patientId = "PAT-" + UUID.randomUUID().toString().substring(0, 8);
        }

        // PID-5: Patient Name (Family^Given^Middle^Suffix^Prefix)
        String pid5 = fields.length > 5 ? fields[5] : "";
        String familyName = "";
        String givenName = "";
        String middleName = "";
        String suffix = "";
        String prefix = "";
        if (StringUtils.isNotBlank(pid5)) {
            String[] nameParts = pid5.split("\\^", -1);
            if (nameParts.length > 0) familyName = nameParts[0].trim();
            if (nameParts.length > 1) givenName = nameParts[1].trim();
            if (nameParts.length > 2) middleName = nameParts[2].trim();
            if (nameParts.length > 3) suffix = nameParts[3].trim();
            if (nameParts.length > 4) prefix = nameParts[4].trim();
        }

        // PID-7: Date of Birth
        String dob = fields.length > 7 ? fields[7].trim() : "";

        // PID-8: Administrative Sex
        String gender = fields.length > 8 ? fields[8].trim() : "";

        // PID-11: Patient Address (Street^Other^City^State^Zip^Country)
        String pid11 = fields.length > 11 ? fields[11] : "";
        String street = "";
        String otherAddr = "";
        String city = "";
        String state = "";
        String zip = "";
        String country = "";
        if (StringUtils.isNotBlank(pid11)) {
            String[] addrParts = pid11.split("\\^", -1);
            if (addrParts.length > 0) street = addrParts[0].trim();
            if (addrParts.length > 1) otherAddr = addrParts[1].trim();
            if (addrParts.length > 2) city = addrParts[2].trim();
            if (addrParts.length > 3) state = addrParts[3].trim();
            if (addrParts.length > 4) zip = addrParts[4].trim();
            if (addrParts.length > 5) country = addrParts[5].trim();
        }

        // PID-13: Phone - Home
        String homePhone = fields.length > 13 ? fields[13].split("\\^", -1)[0].trim() : "";
        // PID-14: Phone - Work
        String workPhone = fields.length > 14 ? fields[14].split("\\^", -1)[0].trim() : "";
        // PID-18: Patient Account Number
        String accountNum = fields.length > 18 ? fields[18].split("\\^", -1)[0].trim() : "";
        // PID-19: SSN
        String ssn = fields.length > 19 ? fields[19].split("\\^", -1)[0].trim() : "";

        return buildPatientResource(patientId, assigningAuthority, accountNum, ssn,
                familyName, givenName, middleName, prefix, suffix,
                dob, gender, street, otherAddr, city, state, zip, country,
                homePhone, workPhone);
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
        String extractedId = null;

        Pattern namePattern = Pattern.compile("patient\\s+([A-Za-z\\s\\^]+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = namePattern.matcher(description);
        if (matcher.find()) {
            extractedName = matcher.group(1).trim();
        }

        if (StringUtils.isNotBlank(taskId)) {
            extractedId = taskId;
        } else {
            extractedId = "event-" + UUID.randomUUID().toString().substring(0, 8);
        }

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

    /**
     * Builds a structured FHIR Patient resource from discrete identity fields.
     */
    public Patient buildPatientResource(String patientId, String assigningAuthority, String accountNum, String ssn,
                                        String familyName, String givenName, String middleName, String prefix, String suffix,
                                        String dob, String genderCode,
                                        String street, String otherAddr, String city, String state, String zip, String country,
                                        String homePhone, String workPhone) {
        Patient patient = new Patient();
        String cleanIdVal = cleanId(patientId);
        patient.setId("Patient/" + cleanIdVal);
        patient.setActive(true);

        // MRN / Identifier
        Identifier mrn = patient.addIdentifier();
        mrn.setUse(Identifier.IdentifierUse.USUAL);
        mrn.setType(new CodeableConcept().addCoding(
                new Coding("http://terminology.hl7.org/CodeSystem/v2-0203", "MR", "Medical Record Number")));
        if (StringUtils.isNotBlank(assigningAuthority)) {
            mrn.setSystem("http://example.org/patients/" + assigningAuthority.toLowerCase());
        } else {
            mrn.setSystem("http://example.org/patients");
        }
        mrn.setValue(cleanIdVal);

        // Account Number
        if (StringUtils.isNotBlank(accountNum)) {
            Identifier acc = patient.addIdentifier();
            acc.setType(new CodeableConcept().addCoding(
                    new Coding("http://terminology.hl7.org/CodeSystem/v2-0203", "AN", "Account Number")));
            acc.setSystem("http://example.org/accounts");
            acc.setValue(accountNum);
        }

        // SSN
        if (StringUtils.isNotBlank(ssn)) {
            Identifier ssnId = patient.addIdentifier();
            ssnId.setType(new CodeableConcept().addCoding(
                    new Coding("http://terminology.hl7.org/CodeSystem/v2-0203", "SS", "Social Security Number")));
            ssnId.setSystem("http://hl7.org/fhir/sid/us-ssn");
            ssnId.setValue(ssn);
        }

        // Human Name
        if (StringUtils.isNotBlank(familyName) || StringUtils.isNotBlank(givenName)) {
            HumanName name = patient.addName();
            name.setUse(HumanName.NameUse.OFFICIAL);
            if (StringUtils.isNotBlank(familyName)) name.setFamily(familyName);
            if (StringUtils.isNotBlank(givenName)) name.addGiven(givenName);
            if (StringUtils.isNotBlank(middleName)) name.addGiven(middleName);
            if (StringUtils.isNotBlank(prefix)) name.addPrefix(prefix);
            if (StringUtils.isNotBlank(suffix)) name.addSuffix(suffix);
            name.setText(buildFullName(givenName, middleName, familyName, prefix, suffix));
        }

        // Gender
        if (StringUtils.isNotBlank(genderCode)) {
            patient.setGender(mapAdministrativeGender(genderCode));
        }

        // Date of Birth
        if (StringUtils.isNotBlank(dob)) {
            Date parsedDob = parseHl7Date(dob);
            if (parsedDob != null) {
                patient.setBirthDate(parsedDob);
            }
        }

        // Postal Address
        if (StringUtils.isNotBlank(street) || StringUtils.isNotBlank(city) || StringUtils.isNotBlank(zip) || StringUtils.isNotBlank(state)) {
            Address address = patient.addAddress();
            address.setUse(Address.AddressUse.HOME);
            if (StringUtils.isNotBlank(street)) address.addLine(street);
            if (StringUtils.isNotBlank(otherAddr)) address.addLine(otherAddr);
            if (StringUtils.isNotBlank(city)) address.setCity(city);
            if (StringUtils.isNotBlank(state)) address.setState(state);
            if (StringUtils.isNotBlank(zip)) address.setPostalCode(zip);
            if (StringUtils.isNotBlank(country)) address.setCountry(country);
        }

        // Contact Points
        if (StringUtils.isNotBlank(homePhone)) {
            ContactPoint contact = patient.addTelecom();
            contact.setSystem(ContactPoint.ContactPointSystem.PHONE);
            contact.setUse(ContactPoint.ContactPointUse.HOME);
            contact.setValue(homePhone);
        }
        if (StringUtils.isNotBlank(workPhone)) {
            ContactPoint contact = patient.addTelecom();
            contact.setSystem(ContactPoint.ContactPointSystem.PHONE);
            contact.setUse(ContactPoint.ContactPointUse.WORK);
            contact.setValue(workPhone);
        }

        FhirSecurityTagManager.applyDefaultSecurityTag(patient);
        return patient;
    }

    /**
     * Constructs a fallback Patient resource.
     */
    public Patient createFallbackPatient(String idSeed) {
        Patient patient = new Patient();
        String cleanIdVal = cleanId(idSeed);
        if (StringUtils.isBlank(cleanIdVal) || "unknown".equalsIgnoreCase(cleanIdVal)) {
            cleanIdVal = "pat-" + UUID.randomUUID().toString().substring(0, 8);
        }
        patient.setId("Patient/" + cleanIdVal);
        patient.setActive(true);

        Identifier mrn = patient.addIdentifier();
        mrn.setUse(Identifier.IdentifierUse.USUAL);
        mrn.setSystem("http://example.org/patients");
        mrn.setValue(cleanIdVal);

        FhirSecurityTagManager.applyDefaultSecurityTag(patient);
        return patient;
    }

    /**
     * Formats full human name string from components.
     */
    public String buildFullName(String given, String middle, String family, String prefix, String suffix) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(prefix)) sb.append(prefix.trim()).append(" ");
        if (StringUtils.isNotBlank(given)) sb.append(given.trim()).append(" ");
        if (StringUtils.isNotBlank(middle)) sb.append(middle.trim()).append(" ");
        if (StringUtils.isNotBlank(family)) sb.append(family.trim()).append(" ");
        if (StringUtils.isNotBlank(suffix)) sb.append(suffix.trim()).append(" ");
        return sb.toString().trim();
    }

    /**
     * Extracts full name string from FHIR Patient resource.
     */
    public String extractFullName(Patient patient) {
        if (patient == null || !patient.hasName()) {
            return null;
        }
        HumanName name = patient.getNameFirstRep();
        if (StringUtils.isNotBlank(name.getText())) {
            return name.getText();
        }
        String given = name.hasGiven() ? String.join(" ", name.getGivenAsSingleString()) : "";
        String family = name.hasFamily() ? name.getFamily() : "";
        String prefix = name.hasPrefix() ? name.getPrefixAsSingleString() : "";
        String suffix = name.hasSuffix() ? name.getSuffixAsSingleString() : "";
        return buildFullName(given, "", family, prefix, suffix);
    }

    /**
     * Extracts MRN value from FHIR Patient resource.
     */
    public String extractMrn(Patient patient) {
        if (patient == null || !patient.hasIdentifier()) {
            return patient != null ? patient.getIdPart() : null;
        }
        for (Identifier id : patient.getIdentifier()) {
            if (id.hasType()) {
                for (Coding c : id.getType().getCoding()) {
                    if ("MR".equalsIgnoreCase(c.getCode())) {
                        return id.getValue();
                    }
                }
            }
            if (id.hasValue()) {
                return id.getValue();
            }
        }
        return patient.getIdPart();
    }

    /**
     * Maps HL7 administrative gender code to FHIR AdministrativeGender enum.
     */
    public Enumerations.AdministrativeGender mapAdministrativeGender(String genderCode) {
        if (StringUtils.isBlank(genderCode)) {
            return Enumerations.AdministrativeGender.UNKNOWN;
        }
        String g = genderCode.trim().toUpperCase();
        if ("M".equals(g) || "MALE".equals(g)) {
            return Enumerations.AdministrativeGender.MALE;
        } else if ("F".equals(g) || "FEMALE".equals(g)) {
            return Enumerations.AdministrativeGender.FEMALE;
        } else if ("O".equals(g) || "OTHER".equals(g)) {
            return Enumerations.AdministrativeGender.OTHER;
        }
        return Enumerations.AdministrativeGender.UNKNOWN;
    }

    /**
     * Robust HL7 date/time parser supporting multiple HL7 timestamp patterns.
     */
    public Date parseHl7Date(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        String clean = dateStr.trim();
        String[] patterns = {
                "yyyyMMddHHmmss",
                "yyyyMMddHHmm",
                "yyyyMMdd",
                "yyyy-MM-dd'T'HH:mm:ss",
                "yyyy-MM-dd'T'HH:mm:ss.SSS",
                "yyyy-MM-dd"
        };
        for (String pattern : patterns) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                sdf.setLenient(false);
                return sdf.parse(clean);
            } catch (ParseException ignored) {
            }
        }
        return null;
    }

    @Override
    protected String cleanId(String id) {
        if (id == null) return "";
        String clean = id.trim();
        if (clean.startsWith("#")) {
            clean = clean.substring(1);
        }
        return clean.replace("Patient/", "").replace("Task/", "").trim();
    }
}
