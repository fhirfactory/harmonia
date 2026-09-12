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

package net.fhirfactory.hie.taskprocessors.hl7v2x.factories;

import ca.uhn.hl7v2.util.Terser;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.*;

import java.util.Date;
import java.util.UUID;

import static net.fhirfactory.hie.taskprocessors.hl7v2x.common.Hl7v2ParsingSupport.*;

/**
 * Builds FHIR {@link Patient} resources from HL7 v2 PID and PD1 segments.
 */
public class AdtPatientResourceBuilder {

    /**
     * Builds a Patient resource from PID and PD1 segments.
     *
     * @param terser           HAPI Terser
     * @param rawMessage       raw HL7 message string
     * @param messageControlId message control ID for identifier generation
     * @return populated Patient resource
     */
    public Patient buildPatient(Terser terser, String rawMessage, String messageControlId) {
        Patient patient = new Patient();
        String patientId = extractPatientId(terser, rawMessage);
        if (StringUtils.isBlank(patientId)) {
            patientId = "pat-" + cleanId(messageControlId);
        }
        patient.setId("Patient/" + cleanId(patientId));
        patient.setActive(true);

        // Identifiers
        Identifier mrn = patient.addIdentifier();
        mrn.setUse(Identifier.IdentifierUse.USUAL);
        mrn.setType(new CodeableConcept().addCoding(new Coding("http://terminology.hl7.org/CodeSystem/v2-0203", "MR", "Medical Record Number")));
        mrn.setSystem("http://example.org/patients");
        mrn.setValue(cleanId(patientId));

        String accountNum = extractTerserOrRegex(terser, "/PID-18-1", rawMessage, "PID", 18, null);
        if (StringUtils.isNotBlank(accountNum)) {
            Identifier acc = patient.addIdentifier();
            acc.setType(new CodeableConcept().addCoding(new Coding("http://terminology.hl7.org/CodeSystem/v2-0203", "AN", "Account Number")));
            acc.setSystem("http://example.org/accounts");
            acc.setValue(accountNum.split("\\^")[0]);
        }

        String ssn = extractTerserOrRegex(terser, "/PID-19", rawMessage, "PID", 19, null);
        if (StringUtils.isNotBlank(ssn)) {
            Identifier ssnId = patient.addIdentifier();
            ssnId.setType(new CodeableConcept().addCoding(new Coding("http://terminology.hl7.org/CodeSystem/v2-0203", "SS", "Social Security Number")));
            ssnId.setSystem("http://hl7.org/fhir/sid/us-ssn");
            ssnId.setValue(ssn.split("\\^")[0]);
        }

        // Names
        String familyName = extractTerserOrRegex(terser, "/PID-5-1", rawMessage, "PID", 5, null);
        String givenName = extractTerserOrRegex(terser, "/PID-5-2", rawMessage, "PID", 5, null);
        String middleName = extractTerserOrRegex(terser, "/PID-5-3", rawMessage, "PID", 5, null);
        String prefix = extractTerserOrRegex(terser, "/PID-5-5", rawMessage, "PID", 5, null);
        String suffix = extractTerserOrRegex(terser, "/PID-5-4", rawMessage, "PID", 5, null);

        if (familyName != null && familyName.contains("^")) {
            String[] parts = familyName.split("\\^");
            familyName = parts.length > 0 ? parts[0] : "";
            if (parts.length > 1 && StringUtils.isBlank(givenName)) givenName = parts[1];
            if (parts.length > 2 && StringUtils.isBlank(middleName)) middleName = parts[2];
            if (parts.length > 3 && StringUtils.isBlank(suffix)) suffix = parts[3];
            if (parts.length > 4 && StringUtils.isBlank(prefix)) prefix = parts[4];
        }

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
        String gender = extractTerserOrRegex(terser, "/PID-8", rawMessage, "PID", 8, null);
        if (StringUtils.isNotBlank(gender)) {
            patient.setGender(mapAdministrativeGender(gender));
        }

        // Date of Birth
        String dob = extractTerserOrRegex(terser, "/PID-7-1", rawMessage, "PID", 7, null);
        if (StringUtils.isNotBlank(dob)) {
            Date parsedDob = parseHl7Date(dob);
            if (parsedDob != null) {
                patient.setBirthDate(parsedDob);
            }
        }

        // Address
        String street = extractTerserOrRegex(terser, "/PID-11-1", rawMessage, "PID", 11, null);
        String otherDesignation = extractTerserOrRegex(terser, "/PID-11-2", rawMessage, "PID", 11, null);
        String city = extractTerserOrRegex(terser, "/PID-11-3", rawMessage, "PID", 11, null);
        String state = extractTerserOrRegex(terser, "/PID-11-4", rawMessage, "PID", 11, null);
        String zip = extractTerserOrRegex(terser, "/PID-11-5", rawMessage, "PID", 11, null);
        String country = extractTerserOrRegex(terser, "/PID-11-6", rawMessage, "PID", 11, null);

        if (street != null && street.contains("^")) {
            String[] parts = street.split("\\^");
            street = parts.length > 0 ? parts[0] : "";
            if (parts.length > 1 && StringUtils.isBlank(otherDesignation)) otherDesignation = parts[1];
            if (parts.length > 2 && StringUtils.isBlank(city)) city = parts[2];
            if (parts.length > 3 && StringUtils.isBlank(state)) state = parts[3];
            if (parts.length > 4 && StringUtils.isBlank(zip)) zip = parts[4];
            if (parts.length > 5 && StringUtils.isBlank(country)) country = parts[5];
        }

        if (StringUtils.isNotBlank(street) || StringUtils.isNotBlank(city) || StringUtils.isNotBlank(zip) || StringUtils.isNotBlank(state)) {
            Address address = patient.addAddress();
            address.setUse(Address.AddressUse.HOME);
            if (StringUtils.isNotBlank(street)) address.addLine(street);
            if (StringUtils.isNotBlank(otherDesignation)) address.addLine(otherDesignation);
            if (StringUtils.isNotBlank(city)) address.setCity(city);
            if (StringUtils.isNotBlank(state)) address.setState(state);
            if (StringUtils.isNotBlank(zip)) address.setPostalCode(zip);
            if (StringUtils.isNotBlank(country)) address.setCountry(country);
        }

        // Telecoms
        String homePhone = extractTerserOrRegex(terser, "/PID-13-1", rawMessage, "PID", 13, null);
        if (StringUtils.isNotBlank(homePhone)) {
            ContactPoint cp = patient.addTelecom();
            cp.setSystem(ContactPoint.ContactPointSystem.PHONE);
            cp.setUse(ContactPoint.ContactPointUse.HOME);
            cp.setValue(cleanPhoneNumber(homePhone));
        }

        String workPhone = extractTerserOrRegex(terser, "/PID-14-1", rawMessage, "PID", 14, null);
        if (StringUtils.isNotBlank(workPhone)) {
            ContactPoint cp = patient.addTelecom();
            cp.setSystem(ContactPoint.ContactPointSystem.PHONE);
            cp.setUse(ContactPoint.ContactPointUse.WORK);
            cp.setValue(cleanPhoneNumber(workPhone));
        }

        // Marital Status
        String maritalStatus = extractTerserOrRegex(terser, "/PID-16", rawMessage, "PID", 16, null);
        if (StringUtils.isNotBlank(maritalStatus)) {
            patient.setMaritalStatus(mapMaritalStatus(maritalStatus));
        }

        // Deceased Info
        String deceasedInd = extractTerserOrRegex(terser, "/PID-30", rawMessage, "PID", 30, null);
        String deceasedDateStr = extractTerserOrRegex(terser, "/PID-29", rawMessage, "PID", 29, null);
        if ("Y".equalsIgnoreCase(deceasedInd)) {
            patient.setDeceased(new BooleanType(true));
        }
        if (StringUtils.isNotBlank(deceasedDateStr)) {
            Date dDate = parseHl7Date(deceasedDateStr);
            if (dDate != null) {
                patient.setDeceased(new DateTimeType(dDate));
            }
        }

        // Multiple Birth
        String multipleBirth = extractTerserOrRegex(terser, "/PID-24", rawMessage, "PID", 24, null);
        String birthOrder = extractTerserOrRegex(terser, "/PID-25", rawMessage, "PID", 25, null);
        if ("Y".equalsIgnoreCase(multipleBirth)) {
            if (StringUtils.isNotBlank(birthOrder) && StringUtils.isNumeric(birthOrder)) {
                patient.setMultipleBirth(new IntegerType(Integer.parseInt(birthOrder)));
            } else {
                patient.setMultipleBirth(new BooleanType(true));
            }
        }

        // Demographics Extensions
        String race = extractTerserOrRegex(terser, "/PID-10", rawMessage, "PID", 10, null);
        if (StringUtils.isNotBlank(race)) {
            Extension ext = patient.addExtension();
            ext.setUrl(EXTENSION_RACE);
            ext.setValue(new StringType(race));
        }

        String ethnicity = extractTerserOrRegex(terser, "/PID-22", rawMessage, "PID", 22, null);
        if (StringUtils.isNotBlank(ethnicity)) {
            Extension ext = patient.addExtension();
            ext.setUrl(EXTENSION_ETHNICITY);
            ext.setValue(new StringType(ethnicity));
        }

        String religion = extractTerserOrRegex(terser, "/PID-17", rawMessage, "PID", 17, null);
        if (StringUtils.isNotBlank(religion)) {
            Extension ext = patient.addExtension();
            ext.setUrl(EXTENSION_RELIGION);
            ext.setValue(new StringType(religion));
        }

        String motherMaiden = extractTerserOrRegex(terser, "/PID-6", rawMessage, "PID", 6, null);
        if (StringUtils.isNotBlank(motherMaiden)) {
            Extension ext = patient.addExtension();
            ext.setUrl(EXTENSION_MOTHER_MAIDEN_NAME);
            ext.setValue(new StringType(motherMaiden));
        }

        String primaryCareDoc = extractTerserOrRegex(terser, "/PD1-4-1", rawMessage, "PD1", 4, null);
        if (StringUtils.isNotBlank(primaryCareDoc)) {
            patient.addGeneralPractitioner(new Reference("Practitioner/" + cleanId(primaryCareDoc)));
        }

        return patient;
    }

    /**
     * Creates a fallback baseline Patient when message cannot be parsed.
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

        return patient;
    }
}
