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
import java.util.ArrayList;
import java.util.List;

/**
 * Synthetic clinical result profile representing an ORU^R01 result message (Laboratory or Diagnostic Imaging).
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultProfile {

    private String placerOrderNumber;       // OBR-2
    private String fillerOrderNumber;       // OBR-3
    private String patientId;               // PID-3
    private String visitNumber;             // PV1-19
    private OrderType orderType;            // LABORATORY or DIAGNOSTIC_IMAGING
    private String universalServiceId;      // OBR-4.1
    private String universalServiceText;    // OBR-4.2
    private String resultStatus;            // OBR-25 (F=Final, C=Corrected, P=Preliminary)
    private String diagnosticReportNarrative; // For imaging studies (OBX narrative report / impression)
    private String performingTechnician;
    private String reviewingDoctor;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime observationDateTime;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime resultDateTime;

    private List<ObservationItem> observations = new ArrayList<>();

    public ResultProfile() {
        this.resultStatus = "F";
        this.resultDateTime = LocalDateTime.now();
    }

    public ResultProfile(String placerOrderNumber, String fillerOrderNumber, String patientId,
                         String visitNumber, OrderType orderType, String universalServiceId, String universalServiceText) {
        this.placerOrderNumber = placerOrderNumber;
        this.fillerOrderNumber = fillerOrderNumber;
        this.patientId = patientId;
        this.visitNumber = visitNumber;
        this.orderType = orderType;
        this.universalServiceId = universalServiceId;
        this.universalServiceText = universalServiceText;
        this.resultStatus = "F";
        this.observationDateTime = LocalDateTime.now().minusMinutes(15);
        this.resultDateTime = LocalDateTime.now();
    }

    public void addObservation(ObservationItem item) {
        if (item != null) {
            observations.add(item);
        }
    }

    // Getters and setters
    public String getPlacerOrderNumber() {
        return placerOrderNumber;
    }

    public void setPlacerOrderNumber(String placerOrderNumber) {
        this.placerOrderNumber = placerOrderNumber;
    }

    public String getFillerOrderNumber() {
        return fillerOrderNumber;
    }

    public void setFillerOrderNumber(String fillerOrderNumber) {
        this.fillerOrderNumber = fillerOrderNumber;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getVisitNumber() {
        return visitNumber;
    }

    public void setVisitNumber(String visitNumber) {
        this.visitNumber = visitNumber;
    }

    public OrderType getOrderType() {
        return orderType != null ? orderType : OrderType.fromUniversalServiceIdentifier(universalServiceId);
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public String getUniversalServiceId() {
        return universalServiceId;
    }

    public void setUniversalServiceId(String universalServiceId) {
        this.universalServiceId = universalServiceId;
    }

    public String getUniversalServiceText() {
        return universalServiceText;
    }

    public void setUniversalServiceText(String universalServiceText) {
        this.universalServiceText = universalServiceText;
    }

    public String getResultStatus() {
        return resultStatus != null ? resultStatus : "F";
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getDiagnosticReportNarrative() {
        return diagnosticReportNarrative;
    }

    public void setDiagnosticReportNarrative(String diagnosticReportNarrative) {
        this.diagnosticReportNarrative = diagnosticReportNarrative;
    }

    public String getPerformingTechnician() {
        return performingTechnician;
    }

    public void setPerformingTechnician(String performingTechnician) {
        this.performingTechnician = performingTechnician;
    }

    public String getReviewingDoctor() {
        return reviewingDoctor;
    }

    public void setReviewingDoctor(String reviewingDoctor) {
        this.reviewingDoctor = reviewingDoctor;
    }

    public LocalDateTime getObservationDateTime() {
        return observationDateTime;
    }

    public void setObservationDateTime(LocalDateTime observationDateTime) {
        this.observationDateTime = observationDateTime;
    }

    public LocalDateTime getResultDateTime() {
        return resultDateTime;
    }

    public void setResultDateTime(LocalDateTime resultDateTime) {
        this.resultDateTime = resultDateTime;
    }

    public List<ObservationItem> getObservations() {
        return observations;
    }

    public void setObservations(List<ObservationItem> observations) {
        this.observations = observations != null ? observations : new ArrayList<>();
    }

    @Override
    public String toString() {
        return "ResultProfile{" +
                "order='" + placerOrderNumber + '\'' +
                ", filler='" + fillerOrderNumber + '\'' +
                ", type=" + getOrderType() +
                ", code='" + universalServiceId + '\'' +
                ", obsCount=" + observations.size() +
                '}';
    }
}
