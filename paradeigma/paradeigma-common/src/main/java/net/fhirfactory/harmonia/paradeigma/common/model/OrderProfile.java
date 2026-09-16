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
 * Synthetic clinical order profile representing an ORM^O01 order in Paradeigma.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderProfile {

    private String placerOrderNumber;       // ORC-2 / OBR-2, e.g. ORD-100456
    private String fillerOrderNumber;       // ORC-3 / OBR-3
    private String patientId;               // PID-3
    private String visitNumber;             // PV1-19
    private OrderType orderType;            // LABORATORY or DIAGNOSTIC_IMAGING
    private String universalServiceId;      // OBR-4.1, e.g. CBC, XR_CHEST
    private String universalServiceText;    // OBR-4.2, e.g. Complete Blood Count, Chest X-Ray
    private String orderControl;            // ORC-1, e.g. NW (New Order)
    private String orderStatus;             // ORC-5, e.g. IP (In Process), CM (Completed)
    private String priority;                // TQ1-9 / ORC-7.6, e.g. R (Routine), S (Stat)
    private String orderingProviderId;      // ORC-12.1
    private String orderingProviderName;    // ORC-12.2

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime orderDateTime;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime observationDateTime;

    public OrderProfile() {
    }

    public OrderProfile(String placerOrderNumber, String patientId, String visitNumber,
                        OrderType orderType, String universalServiceId, String universalServiceText) {
        this.placerOrderNumber = placerOrderNumber;
        this.patientId = patientId;
        this.visitNumber = visitNumber;
        this.orderType = orderType;
        this.universalServiceId = universalServiceId;
        this.universalServiceText = universalServiceText;
        this.orderControl = "NW";
        this.orderStatus = "IP";
        this.priority = "R";
        this.orderDateTime = LocalDateTime.now();
    }

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

    public String getOrderControl() {
        return orderControl != null ? orderControl : "NW";
    }

    public void setOrderControl(String orderControl) {
        this.orderControl = orderControl;
    }

    public String getOrderStatus() {
        return orderStatus != null ? orderStatus : "IP";
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPriority() {
        return priority != null ? priority : "R";
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getOrderingProviderId() {
        return orderingProviderId;
    }

    public void setOrderingProviderId(String orderingProviderId) {
        this.orderingProviderId = orderingProviderId;
    }

    public String getOrderingProviderName() {
        return orderingProviderName;
    }

    public void setOrderingProviderName(String orderingProviderName) {
        this.orderingProviderName = orderingProviderName;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(LocalDateTime orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    public LocalDateTime getObservationDateTime() {
        return observationDateTime;
    }

    public void setObservationDateTime(LocalDateTime observationDateTime) {
        this.observationDateTime = observationDateTime;
    }

    @Override
    public String toString() {
        return "OrderProfile{" +
                "orderNumber='" + placerOrderNumber + '\'' +
                ", type=" + getOrderType() +
                ", code='" + universalServiceId + '\'' +
                ", text='" + universalServiceText + '\'' +
                ", patientId='" + patientId + '\'' +
                '}';
    }
}
