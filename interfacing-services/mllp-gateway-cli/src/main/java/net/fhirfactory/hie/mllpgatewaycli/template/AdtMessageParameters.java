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

package net.fhirfactory.hie.mllpgatewaycli.template;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

/**
 * Encapsulates parameters used to populate HL7 v2.4 ADT templates.
 */
public class AdtMessageParameters {

    public static final String DEFAULT_MRN = "PAT10001";
    public static final String DEFAULT_FIRST_NAME = "JOHN";
    public static final String DEFAULT_LAST_NAME = "DOE";
    public static final String DEFAULT_DOB = "19800101";
    public static final String DEFAULT_GENDER = "M";
    public static final String DEFAULT_SENDING_APP = "HIE_CLI";
    public static final String DEFAULT_SENDING_FACILITY = "FACILITY_CLI";
    public static final String DEFAULT_RECEIVING_APP = "HIE";
    public static final String DEFAULT_RECEIVING_FACILITY = "HIE_IM";
    public static final String DEFAULT_ASSIGNING_AUTH = "HOSPITAL";
    public static final String DEFAULT_IDENTIFIER_TYPE = "MR";
    public static final String DEFAULT_LOCATION = "WARD1^RM01^BED1";
    public static final String DEFAULT_ATTENDING_DOC = "DOC01^SMITH^JOHN^^DR";

    private String mrn;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String gender;
    private String messageControlId;
    private String eventTimestamp;
    private String sendingApp;
    private String sendingFacility;
    private String receivingApp;
    private String receivingFacility;
    private String assigningAuth;
    private String identifierType;
    private String patientClass;
    private String visitNumber;
    private String priorVisitNumber;
    private String currentLocation;
    private String priorLocation;
    private String attendingDoctor;
    private String streetAddress;
    private String city;
    private String state;
    private String postalCode;
    private String phoneNumber;

    public AdtMessageParameters() {
        this.mrn = DEFAULT_MRN;
        this.firstName = DEFAULT_FIRST_NAME;
        this.lastName = DEFAULT_LAST_NAME;
        this.dateOfBirth = DEFAULT_DOB;
        this.gender = DEFAULT_GENDER;
        this.sendingApp = DEFAULT_SENDING_APP;
        this.sendingFacility = DEFAULT_SENDING_FACILITY;
        this.receivingApp = DEFAULT_RECEIVING_APP;
        this.receivingFacility = DEFAULT_RECEIVING_FACILITY;
        this.assigningAuth = DEFAULT_ASSIGNING_AUTH;
        this.identifierType = DEFAULT_IDENTIFIER_TYPE;
        this.currentLocation = DEFAULT_LOCATION;
        this.attendingDoctor = DEFAULT_ATTENDING_DOC;
        this.streetAddress = "123 Main Street";
        this.city = "Metropolis";
        this.state = "NY";
        this.postalCode = "10001";
        this.phoneNumber = "555-0100";
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getMrn() {
        return mrn != null && !mrn.isBlank() ? mrn.trim() : DEFAULT_MRN;
    }

    public void setMrn(String mrn) {
        this.mrn = mrn;
    }

    public String getFirstName() {
        return firstName != null && !firstName.isBlank() ? firstName.trim().toUpperCase() : DEFAULT_FIRST_NAME;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName != null && !lastName.isBlank() ? lastName.trim().toUpperCase() : DEFAULT_LAST_NAME;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDateOfBirth() {
        return normalizeDate(this.dateOfBirth, DEFAULT_DOB);
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        if (gender != null && !gender.isBlank()) {
            String g = gender.trim().toUpperCase();
            if (g.startsWith("M")) return "M";
            if (g.startsWith("F")) return "F";
            if (g.startsWith("O")) return "O";
            if (g.startsWith("U")) return "U";
            return g.substring(0, 1);
        }
        return DEFAULT_GENDER;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMessageControlId() {
        if (messageControlId != null && !messageControlId.isBlank()) {
            return messageControlId.trim();
        }
        return "MSG-CLI-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }

    public void setMessageControlId(String messageControlId) {
        this.messageControlId = messageControlId;
    }

    public String getEventTimestamp() {
        if (eventTimestamp != null && !eventTimestamp.isBlank()) {
            return eventTimestamp.trim();
        }
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    public void setEventTimestamp(String eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
    }

    public String getSendingApp() {
        return sendingApp != null && !sendingApp.isBlank() ? sendingApp.trim() : DEFAULT_SENDING_APP;
    }

    public void setSendingApp(String sendingApp) {
        this.sendingApp = sendingApp;
    }

    public String getSendingFacility() {
        return sendingFacility != null && !sendingFacility.isBlank() ? sendingFacility.trim() : DEFAULT_SENDING_FACILITY;
    }

    public void setSendingFacility(String sendingFacility) {
        this.sendingFacility = sendingFacility;
    }

    public String getReceivingApp() {
        return receivingApp != null && !receivingApp.isBlank() ? receivingApp.trim() : DEFAULT_RECEIVING_APP;
    }

    public void setReceivingApp(String receivingApp) {
        this.receivingApp = receivingApp;
    }

    public String getReceivingFacility() {
        return receivingFacility != null && !receivingFacility.isBlank() ? receivingFacility.trim() : DEFAULT_RECEIVING_FACILITY;
    }

    public void setReceivingFacility(String receivingFacility) {
        this.receivingFacility = receivingFacility;
    }

    public String getAssigningAuth() {
        return assigningAuth != null && !assigningAuth.isBlank() ? assigningAuth.trim() : DEFAULT_ASSIGNING_AUTH;
    }

    public void setAssigningAuth(String assigningAuth) {
        this.assigningAuth = assigningAuth;
    }

    public String getIdentifierType() {
        return identifierType != null && !identifierType.isBlank() ? identifierType.trim() : DEFAULT_IDENTIFIER_TYPE;
    }

    public void setIdentifierType(String identifierType) {
        this.identifierType = identifierType;
    }

    public String getPatientClass() {
        return patientClass;
    }

    public void setPatientClass(String patientClass) {
        this.patientClass = patientClass;
    }

    public String getVisitNumber() {
        if (visitNumber != null && !visitNumber.isBlank()) {
            return visitNumber.trim();
        }
        return "VN" + (System.currentTimeMillis() % 1000000);
    }

    public void setVisitNumber(String visitNumber) {
        this.visitNumber = visitNumber;
    }

    public String getPriorVisitNumber() {
        return priorVisitNumber != null && !priorVisitNumber.isBlank() ? priorVisitNumber.trim() : "VN-PRIOR";
    }

    public void setPriorVisitNumber(String priorVisitNumber) {
        this.priorVisitNumber = priorVisitNumber;
    }

    public String getCurrentLocation() {
        return currentLocation != null && !currentLocation.isBlank() ? currentLocation.trim() : DEFAULT_LOCATION;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getPriorLocation() {
        return priorLocation != null && !priorLocation.isBlank() ? priorLocation.trim() : "ED^BAY01^^HOSPITAL";
    }

    public void setPriorLocation(String priorLocation) {
        this.priorLocation = priorLocation;
    }

    public String getAttendingDoctor() {
        return attendingDoctor != null && !attendingDoctor.isBlank() ? attendingDoctor.trim() : DEFAULT_ATTENDING_DOC;
    }

    public void setAttendingDoctor(String attendingDoctor) {
        this.attendingDoctor = attendingDoctor;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Normalizes various date formats (e.g. YYYY-MM-DD, YYYYMMDD, YYYY/MM/DD) into HL7 YYYYMMDD.
     */
    public static String normalizeDate(String input, String fallback) {
        if (input == null || input.isBlank()) {
            return fallback;
        }
        String cleaned = input.trim();
        if (cleaned.matches("^\\d{8}$")) {
            return cleaned;
        }
        // Try parsing ISO LocalDate (YYYY-MM-DD)
        try {
            LocalDate date = LocalDate.parse(cleaned, DateTimeFormatter.ISO_LOCAL_DATE);
            return date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        } catch (DateTimeParseException ignored) {
        }
        // Try parsing YYYY/MM/DD
        try {
            LocalDate date = LocalDate.parse(cleaned, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            return date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        } catch (DateTimeParseException ignored) {
        }
        // Try parsing MM/DD/YYYY
        try {
            LocalDate date = LocalDate.parse(cleaned, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            return date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        } catch (DateTimeParseException ignored) {
        }
        // If it starts with 8 digits, extract them
        String digitsOnly = cleaned.replaceAll("\\D", "");
        if (digitsOnly.length() >= 8) {
            return digitsOnly.substring(0, 8);
        }
        return fallback;
    }

    public static class Builder {
        private final AdtMessageParameters params = new AdtMessageParameters();

        public Builder mrn(String mrn) {
            params.setMrn(mrn);
            return this;
        }

        public Builder firstName(String firstName) {
            params.setFirstName(firstName);
            return this;
        }

        public Builder lastName(String lastName) {
            params.setLastName(lastName);
            return this;
        }

        public Builder dateOfBirth(String dateOfBirth) {
            params.setDateOfBirth(dateOfBirth);
            return this;
        }

        public Builder gender(String gender) {
            params.setGender(gender);
            return this;
        }

        public Builder messageControlId(String messageControlId) {
            params.setMessageControlId(messageControlId);
            return this;
        }

        public Builder eventTimestamp(String eventTimestamp) {
            params.setEventTimestamp(eventTimestamp);
            return this;
        }

        public Builder sendingApp(String sendingApp) {
            params.setSendingApp(sendingApp);
            return this;
        }

        public Builder sendingFacility(String sendingFacility) {
            params.setSendingFacility(sendingFacility);
            return this;
        }

        public Builder receivingApp(String receivingApp) {
            params.setReceivingApp(receivingApp);
            return this;
        }

        public Builder receivingFacility(String receivingFacility) {
            params.setReceivingFacility(receivingFacility);
            return this;
        }

        public Builder patientClass(String patientClass) {
            params.setPatientClass(patientClass);
            return this;
        }

        public Builder visitNumber(String visitNumber) {
            params.setVisitNumber(visitNumber);
            return this;
        }

        public Builder currentLocation(String currentLocation) {
            params.setCurrentLocation(currentLocation);
            return this;
        }

        public Builder priorLocation(String priorLocation) {
            params.setPriorLocation(priorLocation);
            return this;
        }

        public Builder attendingDoctor(String attendingDoctor) {
            params.setAttendingDoctor(attendingDoctor);
            return this;
        }

        public AdtMessageParameters build() {
            return params;
        }
    }
}
