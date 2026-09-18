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

import java.time.LocalDateTime;

/**
 * Synthetic visit / encounter profile representing an inpatient or outpatient stay in Paradeigma.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VisitProfile {

    private String visitNumber;        // e.g. VIS-200456
    private String patientId;          // Reference to PatientProfile.patientId
    private String patientClass;       // I (Inpatient), O (Outpatient), E (Emergency)
    private String pointOfCare;        // e.g. WARD-3A, ICU, ED
    private String room;               // e.g. 302
    private String bed;                // e.g. A
    private String facility;           // e.g. MAIN-HOSPITAL
    private String attendingDoctorId;  // e.g. DOC-001
    private String attendingDoctorName;// e.g. Dr. Alice Smith
    private String admittingDoctorId;
    private String admittingDoctorName;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime admitDateTime;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime transferDateTime;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dischargeDateTime;

    private String dischargeDisposition; // e.g. HOME, TRANSFER

    public VisitProfile() {
    }

    public VisitProfile(String visitNumber, String patientId, String patientClass, String pointOfCare, String room, String bed) {
        this.visitNumber = visitNumber;
        this.patientId = patientId;
        this.patientClass = patientClass;
        this.pointOfCare = pointOfCare;
        this.room = room;
        this.bed = bed;
        this.facility = "MAIN-HOSPITAL";
        this.admitDateTime = LocalDateTime.now();
    }

    public String getAssignedPatientLocation() {
        StringBuilder sb = new StringBuilder();
        if (pointOfCare != null) sb.append(pointOfCare);
        sb.append("^");
        if (room != null) sb.append(room);
        sb.append("^");
        if (bed != null) sb.append(bed);
        sb.append("^");
        if (facility != null) sb.append(facility);
        return sb.toString();
    }

    // Getters and setters
    public String getVisitNumber() {
        return visitNumber;
    }

    public void setVisitNumber(String visitNumber) {
        this.visitNumber = visitNumber;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientClass() {
        return patientClass != null ? patientClass : "I";
    }

    public void setPatientClass(String patientClass) {
        this.patientClass = patientClass;
    }

    public String getPointOfCare() {
        return pointOfCare;
    }

    public void setPointOfCare(String pointOfCare) {
        this.pointOfCare = pointOfCare;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getBed() {
        return bed;
    }

    public void setBed(String bed) {
        this.bed = bed;
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    public String getAttendingDoctorId() {
        return attendingDoctorId;
    }

    public void setAttendingDoctorId(String attendingDoctorId) {
        this.attendingDoctorId = attendingDoctorId;
    }

    public String getAttendingDoctorName() {
        return attendingDoctorName;
    }

    public void setAttendingDoctorName(String attendingDoctorName) {
        this.attendingDoctorName = attendingDoctorName;
    }

    public String getAdmittingDoctorId() {
        return admittingDoctorId;
    }

    public void setAdmittingDoctorId(String admittingDoctorId) {
        this.admittingDoctorId = admittingDoctorId;
    }

    public String getAdmittingDoctorName() {
        return admittingDoctorName;
    }

    public void setAdmittingDoctorName(String admittingDoctorName) {
        this.admittingDoctorName = admittingDoctorName;
    }

    public LocalDateTime getAdmitDateTime() {
        return admitDateTime;
    }

    public void setAdmitDateTime(LocalDateTime admitDateTime) {
        this.admitDateTime = admitDateTime;
    }

    public LocalDateTime getTransferDateTime() {
        return transferDateTime;
    }

    public void setTransferDateTime(LocalDateTime transferDateTime) {
        this.transferDateTime = transferDateTime;
    }

    public LocalDateTime getDischargeDateTime() {
        return dischargeDateTime;
    }

    public void setDischargeDateTime(LocalDateTime dischargeDateTime) {
        this.dischargeDateTime = dischargeDateTime;
    }

    public String getDischargeDisposition() {
        return dischargeDisposition;
    }

    public void setDischargeDisposition(String dischargeDisposition) {
        this.dischargeDisposition = dischargeDisposition;
    }

    @Override
    public String toString() {
        return "VisitProfile{" +
                "visitNumber='" + visitNumber + '\'' +
                ", patientId='" + patientId + '\'' +
                ", location='" + getAssignedPatientLocation() + '\'' +
                ", class='" + patientClass + '\'' +
                '}';
    }
}
