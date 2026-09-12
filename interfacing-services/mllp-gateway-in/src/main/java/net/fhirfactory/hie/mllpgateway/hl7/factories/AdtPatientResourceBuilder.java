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

package net.fhirfactory.hie.mllpgateway.hl7.factories;

import ca.uhn.hl7v2.util.Terser;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import net.fhirfactory.hie.mllpgateway.hl7.AdtMessageExtractor;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.*;

import java.util.Date;

/**
 * Builds FHIR {@link Patient} resources from HL7 v2 ADT PID segments.
 */
@ApplicationScoped
public class AdtPatientResourceBuilder {

    private final AdtMessageExtractor extractor;

    public AdtPatientResourceBuilder() {
        this(new AdtMessageExtractor());
    }

    @Inject
    public AdtPatientResourceBuilder(AdtMessageExtractor extractor) {
        this.extractor = extractor != null ? extractor : new AdtMessageExtractor();
    }

    /**
     * Builds a FHIR Patient resource from the ADT PID segment.
     *
     * @param terser     HAPI Terser
     * @param rawMessage raw HL7 message string
     * @param patientId  patient ID
     * @return populated Patient resource
     */
    public Patient buildPatient(Terser terser, String rawMessage, String patientId) {
        Patient patient = new Patient();
        patient.setId("Patient/" + patientId);
        patient.setActive(true);

        // Identifier(s)
        Identifier mrn = patient.addIdentifier();
        mrn.setUse(Identifier.IdentifierUse.USUAL);
        mrn.setType(new CodeableConcept().addCoding(
                new Coding("http://terminology.hl7.org/CodeSystem/v2-0203", "MR", "Medical Record Number")));
        mrn.setSystem("http://example.org/patients");
        mrn.setValue(patientId);

        String assigningAuthority = extractor.getTerserValue(terser, "/PID-3-4", "PID-3-4");
        if (StringUtils.isNotBlank(assigningAuthority)) {
            mrn.setSystem("http://example.org/patients/" + assigningAuthority.toLowerCase());
        }

        // Account number
        String accountNum = extractor.getTerserValue(terser, "/PID-18-1", "PID-18-1", "/PID-18", "PID-18");
        if (StringUtils.isNotBlank(accountNum)) {
            Identifier accId = patient.addIdentifier();
            accId.setType(new CodeableConcept().addCoding(
                    new Coding("http://terminology.hl7.org/CodeSystem/v2-0203", "AN", "Account Number")));
            accId.setSystem("http://example.org/accounts");
            accId.setValue(accountNum);
        }

        // SSN
        String ssn = extractor.getTerserValue(terser, "/PID-19", "PID-19");
        if (StringUtils.isNotBlank(ssn)) {
            Identifier ssnId = patient.addIdentifier();
            ssnId.setType(new CodeableConcept().addCoding(
                    new Coding("http://terminology.hl7.org/CodeSystem/v2-0203", "SS", "Social Security Number")));
            ssnId.setSystem("http://hl7.org/fhir/sid/us-ssn");
            ssnId.setValue(ssn);
        }

        // Name
        String familyName = extractor.getTerserValue(terser, "/PID-5-1", "PID-5-1");
        String givenName = extractor.getTerserValue(terser, "/PID-5-2", "PID-5-2");
        String middleName = extractor.getTerserValue(terser, "/PID-5-3", "PID-5-3");
        String prefix = extractor.getTerserValue(terser, "/PID-5-5", "PID-5-5");
        String suffix = extractor.getTerserValue(terser, "/PID-5-4", "PID-5-4");

        if (StringUtils.isNotBlank(familyName) || StringUtils.isNotBlank(givenName)) {
            HumanName name = patient.addName();
            name.setUse(HumanName.NameUse.OFFICIAL);
            if (StringUtils.isNotBlank(familyName)) name.setFamily(familyName);
            if (StringUtils.isNotBlank(givenName)) name.addGiven(givenName);
            if (StringUtils.isNotBlank(middleName)) name.addGiven(middleName);
            if (StringUtils.isNotBlank(prefix)) name.addPrefix(prefix);
            if (StringUtils.isNotBlank(suffix)) name.addSuffix(suffix);
        }

        // Date of Birth
        String dob = extractor.getTerserValue(terser, "/PID-7-1", "PID-7-1", "/PID-7", "PID-7");
        if (StringUtils.isNotBlank(dob)) {
            Date birthDate = extractor.parseHl7Date(dob);
            if (birthDate != null) {
                patient.setBirthDate(birthDate);
            }
        }

        // Gender
        String gender = extractor.getTerserValue(terser, "/PID-8", "PID-8");
        if (StringUtils.isNotBlank(gender)) {
            patient.setGender(extractor.mapAdministrativeGender(gender));
        }

        // Address
        String street = extractor.getTerserValue(terser, "/PID-11-1", "PID-11-1");
        String otherDesignation = extractor.getTerserValue(terser, "/PID-11-2", "PID-11-2");
        String city = extractor.getTerserValue(terser, "/PID-11-3", "PID-11-3");
        String state = extractor.getTerserValue(terser, "/PID-11-4", "PID-11-4");
        String postalCode = extractor.getTerserValue(terser, "/PID-11-5", "PID-11-5");
        String country = extractor.getTerserValue(terser, "/PID-11-6", "PID-11-6");

        if (StringUtils.isNotBlank(street) || StringUtils.isNotBlank(city) || StringUtils.isNotBlank(postalCode)) {
            Address address = patient.addAddress();
            address.setUse(Address.AddressUse.HOME);
            if (StringUtils.isNotBlank(street)) address.addLine(street);
            if (StringUtils.isNotBlank(otherDesignation)) address.addLine(otherDesignation);
            if (StringUtils.isNotBlank(city)) address.setCity(city);
            if (StringUtils.isNotBlank(state)) address.setState(state);
            if (StringUtils.isNotBlank(postalCode)) address.setPostalCode(postalCode);
            if (StringUtils.isNotBlank(country)) address.setCountry(country);
        }

        // Phone Numbers
        String homePhone = extractor.getTerserValue(terser, "/PID-13-1", "PID-13-1", "/PID-13", "PID-13");
        if (StringUtils.isNotBlank(homePhone)) {
            ContactPoint phone = patient.addTelecom();
            phone.setSystem(ContactPoint.ContactPointSystem.PHONE);
            phone.setUse(ContactPoint.ContactPointUse.HOME);
            phone.setValue(extractor.cleanPhoneNumber(homePhone));
        }

        String workPhone = extractor.getTerserValue(terser, "/PID-14-1", "PID-14-1", "/PID-14", "PID-14");
        if (StringUtils.isNotBlank(workPhone)) {
            ContactPoint phone = patient.addTelecom();
            phone.setSystem(ContactPoint.ContactPointSystem.PHONE);
            phone.setUse(ContactPoint.ContactPointUse.WORK);
            phone.setValue(extractor.cleanPhoneNumber(workPhone));
        }

        // Marital Status
        String maritalStatus = extractor.getTerserValue(terser, "/PID-16", "PID-16");
        if (StringUtils.isNotBlank(maritalStatus)) {
            patient.setMaritalStatus(extractor.mapMaritalStatus(maritalStatus));
        }

        return patient;
    }
}
