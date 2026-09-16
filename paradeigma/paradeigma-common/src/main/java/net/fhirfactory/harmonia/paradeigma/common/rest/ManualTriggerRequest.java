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

package net.fhirfactory.harmonia.paradeigma.common.rest;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Request payload for manual clinical event trigger endpoints on simulator REST APIs.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ManualTriggerRequest {

    private String patientId;
    private String visitNumber;
    private String orderNumber;
    private String triggerEvent; // A04, A01, A02, A08, A03, O01, R01
    private String testCode;     // e.g. CBC, XR_CHEST
    private String newWard;      // for A02 transfers
    private String newRoom;
    private String newBed;

    public ManualTriggerRequest() {
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

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getTriggerEvent() {
        return triggerEvent;
    }

    public void setTriggerEvent(String triggerEvent) {
        this.triggerEvent = triggerEvent;
    }

    public String getTestCode() {
        return testCode;
    }

    public void setTestCode(String testCode) {
        this.testCode = testCode;
    }

    public String getNewWard() {
        return newWard;
    }

    public void setNewWard(String newWard) {
        this.newWard = newWard;
    }

    public String getNewRoom() {
        return newRoom;
    }

    public void setNewRoom(String newRoom) {
        this.newRoom = newRoom;
    }

    public String getNewBed() {
        return newBed;
    }

    public void setNewBed(String newBed) {
        this.newBed = newBed;
    }
}
