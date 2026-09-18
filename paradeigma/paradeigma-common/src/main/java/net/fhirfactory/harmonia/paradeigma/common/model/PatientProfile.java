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

package net.fhirfactory.harmonia.paradeigma.common.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Synthetic demographic profile representing a patient in Paradeigma simulations.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PatientProfile {

    private String patientId;          // e.g. PAT-100234
    private String mrn;                // Medical Record Number
    private String nationalId;         // e.g. Medicare/NHS/SSN
    private String familyName;
    private String givenName;
    private String middleName;
    private String prefix;             // Mr., Ms., Dr.

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    private Gender gender;
    private String addressLine1;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private String phoneNumber;
    private String email;

    public PatientProfile() {
    }

    public PatientProfile(String patientId, String familyName, String givenName, LocalDate dateOfBirth, Gender gender) {
        this.patientId = patientId;
        this.mrn = patientId;
        this.familyName = familyName;
        this.givenName = givenName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
    }

    public String getFullName() {
        StringBuilder sb = new StringBuilder();
        if (prefix != null && !prefix.isBlank()) {
            sb.append(prefix).append(" ");
        }
        if (givenName != null) {
            sb.append(givenName).append(" ");
        }
        if (middleName != null && !middleName.isBlank()) {
            sb.append(middleName).append(" ");
        }
        if (familyName != null) {
            sb.append(familyName);
        }
        return sb.toString().trim();
    }

    // Getters and setters
    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getMrn() {
        return mrn != null ? mrn : patientId;
    }

    public void setMrn(String mrn) {
        this.mrn = mrn;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Gender getGender() {
        return gender != null ? gender : Gender.U;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientProfile that = (PatientProfile) o;
        return Objects.equals(patientId, that.patientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patientId);
    }

    @Override
    public String toString() {
        return "PatientProfile{" +
                "patientId='" + patientId + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", dob=" + dateOfBirth +
                ", gender=" + gender +
                '}';
    }
}
