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

package net.fhirfactory.harmonia.paradeigma.scenarios.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PatientJourneyResult {

    private String journeyId;
    private String patientId;
    private String visitNumber;
    private String labPlacerOrderId;
    private String radPlacerOrderId;
    private boolean success;
    private boolean correlationVerified;
    private long totalDurationMs;
    private List<ScenarioExecutionStep> steps = new ArrayList<>();

    public PatientJourneyResult() {
    }

    public PatientJourneyResult(String journeyId, String patientId) {
        this.journeyId = journeyId;
        this.patientId = patientId;
    }

    public void addStep(ScenarioExecutionStep step) {
        if (step != null) {
            steps.add(step);
        }
    }

    public String getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(String journeyId) {
        this.journeyId = journeyId;
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

    public String getLabPlacerOrderId() {
        return labPlacerOrderId;
    }

    public void setLabPlacerOrderId(String labPlacerOrderId) {
        this.labPlacerOrderId = labPlacerOrderId;
    }

    public String getRadPlacerOrderId() {
        return radPlacerOrderId;
    }

    public void setRadPlacerOrderId(String radPlacerOrderId) {
        this.radPlacerOrderId = radPlacerOrderId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isCorrelationVerified() {
        return correlationVerified;
    }

    public void setCorrelationVerified(boolean correlationVerified) {
        this.correlationVerified = correlationVerified;
    }

    public long getTotalDurationMs() {
        return totalDurationMs;
    }

    public void setTotalDurationMs(long totalDurationMs) {
        this.totalDurationMs = totalDurationMs;
    }

    public List<ScenarioExecutionStep> getSteps() {
        return steps;
    }

    public void setSteps(List<ScenarioExecutionStep> steps) {
        this.steps = steps != null ? steps : new ArrayList<>();
    }
}
