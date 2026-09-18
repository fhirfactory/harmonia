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

package net.fhirfactory.harmonia.paradeigma.common.hl7;

import net.fhirfactory.harmonia.paradeigma.common.model.ObservationItem;
import net.fhirfactory.harmonia.paradeigma.common.model.OrderProfile;
import net.fhirfactory.harmonia.paradeigma.common.model.PatientProfile;
import net.fhirfactory.harmonia.paradeigma.common.model.ResultProfile;
import net.fhirfactory.harmonia.paradeigma.common.model.VisitProfile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * High-performance, deterministic HL7 v2.4 message generator for ADT, ORM, and ORU clinical events.
 */
public final class Hl7MessageBuilders {

    private static final DateTimeFormatter HL7_DATE_TIME = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final DateTimeFormatter HL7_DATE_ONLY = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final AtomicLong CONTROL_ID_SEQ = new AtomicLong(100000);

    private Hl7MessageBuilders() {
        // Utility class
    }

    public static String nextMessageControlId(String prefix) {
        String p = (prefix != null && !prefix.isBlank()) ? prefix : "MSG";
        return p + "-" + CONTROL_ID_SEQ.incrementAndGet();
    }

    // =========================================================================
    // ADT Message Builders
    // =========================================================================

    /**
     * ADT^A04 - Register a Patient
     */
    public static String buildAdtA04(PatientProfile patient, VisitProfile visit) {
        return buildAdtA04(patient, visit, "PARADEIGMA_PAS", "SIM_FACILITY", "HARMONIA", "HIE");
    }

    public static String buildAdtA04(PatientProfile patient, VisitProfile visit,
                                     String sendingApp, String sendingFac, String receivingApp, String receivingFac) {
        String controlId = nextMessageControlId("ADT-A04");
        return buildAdtMessage("A04", controlId, patient, visit, null, sendingApp, sendingFac, receivingApp, receivingFac);
    }

    /**
     * ADT^A01 - Admit / Visit Notification
     */
    public static String buildAdtA01(PatientProfile patient, VisitProfile visit) {
        return buildAdtA01(patient, visit, "PARADEIGMA_PAS", "SIM_FACILITY", "HARMONIA", "HIE");
    }

    public static String buildAdtA01(PatientProfile patient, VisitProfile visit,
                                     String sendingApp, String sendingFac, String receivingApp, String receivingFac) {
        String controlId = nextMessageControlId("ADT-A01");
        return buildAdtMessage("A01", controlId, patient, visit, null, sendingApp, sendingFac, receivingApp, receivingFac);
    }

    /**
     * ADT^A02 - Transfer a Patient
     */
    public static String buildAdtA02(PatientProfile patient, VisitProfile visit, String newWard, String newRoom, String newBed) {
        return buildAdtA02(patient, visit, newWard, newRoom, newBed, "PARADEIGMA_PAS", "SIM_FACILITY", "HARMONIA", "HIE");
    }

    public static String buildAdtA02(PatientProfile patient, VisitProfile visit, String newWard, String newRoom, String newBed,
                                     String sendingApp, String sendingFac, String receivingApp, String receivingFac) {
        String controlId = nextMessageControlId("ADT-A02");
        String priorLocation = visit != null ? visit.getAssignedPatientLocation() : "WARD-PREV^^^";
        if (visit != null && newWard != null) {
            visit.setPointOfCare(newWard);
            if (newRoom != null) visit.setRoom(newRoom);
            if (newBed != null) visit.setBed(newBed);
            visit.setTransferDateTime(LocalDateTime.now());
        }
        return buildAdtMessage("A02", controlId, patient, visit, priorLocation, sendingApp, sendingFac, receivingApp, receivingFac);
    }

    /**
     * ADT^A08 - Update Patient Information
     */
    public static String buildAdtA08(PatientProfile patient, VisitProfile visit) {
        return buildAdtA08(patient, visit, "PARADEIGMA_PAS", "SIM_FACILITY", "HARMONIA", "HIE");
    }

    public static String buildAdtA08(PatientProfile patient, VisitProfile visit,
                                     String sendingApp, String sendingFac, String receivingApp, String receivingFac) {
        String controlId = nextMessageControlId("ADT-A08");
        return buildAdtMessage("A08", controlId, patient, visit, null, sendingApp, sendingFac, receivingApp, receivingFac);
    }

    /**
     * ADT^A03 - Discharge / End Visit
     */
    public static String buildAdtA03(PatientProfile patient, VisitProfile visit) {
        return buildAdtA03(patient, visit, "PARADEIGMA_PAS", "SIM_FACILITY", "HARMONIA", "HIE");
    }

    public static String buildAdtA03(PatientProfile patient, VisitProfile visit,
                                     String sendingApp, String sendingFac, String receivingApp, String receivingFac) {
        String controlId = nextMessageControlId("ADT-A03");
        if (visit != null && visit.getDischargeDateTime() == null) {
            visit.setDischargeDateTime(LocalDateTime.now());
        }
        return buildAdtMessage("A03", controlId, patient, visit, null, sendingApp, sendingFac, receivingApp, receivingFac);
    }

    /**
     * ADT^A11 - Cancel Admit
     */
    public static String buildAdtA11(PatientProfile patient, VisitProfile visit) {
        String controlId = nextMessageControlId("ADT-A11");
        return buildAdtMessage("A11", controlId, patient, visit, null, "PARADEIGMA_PAS", "SIM_FACILITY", "HARMONIA", "HIE");
    }

    /**
     * ADT^A12 - Cancel Transfer
     */
    public static String buildAdtA12(PatientProfile patient, VisitProfile visit) {
        String controlId = nextMessageControlId("ADT-A12");
        return buildAdtMessage("A12", controlId, patient, visit, null, "PARADEIGMA_PAS", "SIM_FACILITY", "HARMONIA", "HIE");
    }

    /**
     * ADT^A13 - Cancel Discharge
     */
    public static String buildAdtA13(PatientProfile patient, VisitProfile visit) {
        String controlId = nextMessageControlId("ADT-A13");
        return buildAdtMessage("A13", controlId, patient, visit, null, "PARADEIGMA_PAS", "SIM_FACILITY", "HARMONIA", "HIE");
    }

    private static String buildAdtMessage(String triggerEvent, String messageControlId,
                                          PatientProfile patient, VisitProfile visit, String priorLocation,
                                          String sendingApp, String sendingFac, String receivingApp, String receivingFac) {
        StringBuilder sb = new StringBuilder(512);
        String now = LocalDateTime.now().format(HL7_DATE_TIME);

        // MSH
        sb.append("MSH|^~\\&|").append(sendingApp).append("|").append(sendingFac).append("|")
                .append(receivingApp).append("|").append(receivingFac).append("|")
                .append(now).append("||ADT^").append(triggerEvent).append("|")
                .append(messageControlId).append("|P|2.4\r");

        // EVN
        sb.append("EVN|").append(triggerEvent).append("|").append(now).append("\r");

        // PID
        appendPidSegment(sb, patient);

        // PV1
        appendPv1Segment(sb, visit, priorLocation);

        return sb.toString();
    }

    // =========================================================================
    // ORM Message Builders (Orders - Laboratory & Diagnostic Imaging)
    // =========================================================================

    /**
     * ORM^O01 - General Order Message (Laboratory or Diagnostic Imaging)
     */
    public static String buildOrmO01(OrderProfile order, PatientProfile patient, VisitProfile visit) {
        return buildOrmO01(order, patient, visit, "PARADEIGMA_EMR", "SIM_FACILITY", "HARMONIA", "HIE");
    }

    public static String buildOrmO01(OrderProfile order, PatientProfile patient, VisitProfile visit,
                                     String sendingApp, String sendingFac, String receivingApp, String receivingFac) {
        StringBuilder sb = new StringBuilder(512);
        String now = LocalDateTime.now().format(HL7_DATE_TIME);
        String controlId = nextMessageControlId("ORM-O01");

        // MSH
        sb.append("MSH|^~\\&|").append(sendingApp).append("|").append(sendingFac).append("|")
                .append(receivingApp).append("|").append(receivingFac).append("|")
                .append(now).append("||ORM^O01|").append(controlId).append("|P|2.4\r");

        // PID
        appendPidSegment(sb, patient);

        // PV1
        appendPv1Segment(sb, visit, null);

        // ORC
        String placerOrderNum = order != null ? order.getPlacerOrderNumber() : "ORD-" + UUID.randomUUID().toString().substring(0, 8);
        String fillerOrderNum = order != null && order.getFillerOrderNumber() != null ? order.getFillerOrderNumber() : "";
        String orderControl = order != null ? order.getOrderControl() : "NW";
        String orderStatus = order != null ? order.getOrderStatus() : "IP";
        String priority = order != null ? order.getPriority() : "R";
        String orderDateTime = (order != null && order.getOrderDateTime() != null)
                ? order.getOrderDateTime().format(HL7_DATE_TIME) : now;
        String providerId = order != null && order.getOrderingProviderId() != null ? order.getOrderingProviderId() : "DOC-101";
        String providerName = order != null && order.getOrderingProviderName() != null ? order.getOrderingProviderName() : "Dr. Alice Vance";

        sb.append("ORC|").append(orderControl).append("|").append(placerOrderNum).append("|").append(fillerOrderNum).append("||")
                .append(orderStatus).append("||^^^").append(priority).append("||").append(orderDateTime).append("|||")
                .append(providerId).append("^").append(providerName).append("\r");

        // OBR
        String univServiceId = order != null ? order.getUniversalServiceId() : "CBC";
        String univServiceText = order != null ? order.getUniversalServiceText() : "Complete Blood Count";
        String codingSystem = order != null && order.getOrderType() != null && order.getOrderType().name().contains("RAD") ? "RAD" : "LN";
        String obsDateTime = (order != null && order.getObservationDateTime() != null)
                ? order.getObservationDateTime().format(HL7_DATE_TIME) : now;

        sb.append("OBR|1|").append(placerOrderNum).append("|").append(fillerOrderNum).append("|")
                .append(univServiceId).append("^").append(univServiceText).append("^").append(codingSystem).append("|||")
                .append(obsDateTime).append("|||||||||")
                .append(providerId).append("^").append(providerName).append("\r");

        return sb.toString();
    }

    // =========================================================================
    // ORU Message Builders (Results - Laboratory & Diagnostic Imaging)
    // =========================================================================

    /**
     * ORU^R01 - Unsolicited Observation Message (Lab Results & Imaging Reports)
     */
    public static String buildOruR01(ResultProfile result, PatientProfile patient, VisitProfile visit) {
        String sendingApp = (result != null && result.getOrderType() != null && result.getOrderType().name().contains("RAD"))
                ? "PARADEIGMA_RISPAC" : "PARADEIGMA_LMS";
        return buildOruR01(result, patient, visit, sendingApp, "SIM_FACILITY", "HARMONIA", "HIE");
    }

    public static String buildOruR01(ResultProfile result, PatientProfile patient, VisitProfile visit,
                                     String sendingApp, String sendingFac, String receivingApp, String receivingFac) {
        StringBuilder sb = new StringBuilder(1024);
        String now = LocalDateTime.now().format(HL7_DATE_TIME);
        String controlId = nextMessageControlId("ORU-R01");

        // MSH
        sb.append("MSH|^~\\&|").append(sendingApp).append("|").append(sendingFac).append("|")
                .append(receivingApp).append("|").append(receivingFac).append("|")
                .append(now).append("||ORU^R01|").append(controlId).append("|P|2.4\r");

        // PID
        appendPidSegment(sb, patient);

        // PV1
        appendPv1Segment(sb, visit, null);

        // ORC
        String placerOrderNum = result != null ? result.getPlacerOrderNumber() : "ORD-000000";
        String fillerOrderNum = result != null ? result.getFillerOrderNumber() : "FIL-000000";
        sb.append("ORC|RE|").append(placerOrderNum).append("|").append(fillerOrderNum).append("||CM||||").append(now).append("\r");

        // OBR
        String univServiceId = result != null ? result.getUniversalServiceId() : "CBC";
        String univServiceText = result != null ? result.getUniversalServiceText() : "Complete Blood Count";
        String codingSystem = result != null && result.getOrderType() != null && result.getOrderType().name().contains("RAD") ? "RAD" : "LN";
        String obsDateTime = (result != null && result.getObservationDateTime() != null)
                ? result.getObservationDateTime().format(HL7_DATE_TIME) : now;
        String resultDateTime = (result != null && result.getResultDateTime() != null)
                ? result.getResultDateTime().format(HL7_DATE_TIME) : now;
        String resultStatus = result != null ? result.getResultStatus() : "F";

        sb.append("OBR|1|").append(placerOrderNum).append("|").append(fillerOrderNum).append("|")
                .append(univServiceId).append("^").append(univServiceText).append("^").append(codingSystem).append("|||")
                .append(obsDateTime).append("|||||||||||||||").append(resultDateTime).append("|||").append(resultStatus).append("\r");

        // OBX Segments
        if (result != null && !result.getObservations().isEmpty()) {
            int setSubId = 1;
            for (ObservationItem obs : result.getObservations()) {
                sb.append("OBX|").append(setSubId++).append("|")
                        .append(obs.getValueType()).append("|")
                        .append(obs.getObservationId()).append("^").append(obs.getObservationText()).append("^").append(obs.getCodingSystem()).append("||")
                        .append(obs.getValue() != null ? obs.getValue().replace("\n", "\\.br\\") : "").append("|")
                        .append(obs.getUnits() != null ? obs.getUnits() : "").append("|")
                        .append(obs.getReferenceRange() != null ? obs.getReferenceRange() : "").append("|")
                        .append(obs.getAbnormalFlags()).append("|||")
                        .append(obs.getResultStatus()).append("\r");
            }
        }

        return sb.toString();
    }

    // =========================================================================
    // Shared Segment Formatting Helpers
    // =========================================================================

    private static void appendPidSegment(StringBuilder sb, PatientProfile patient) {
        String patId = patient != null ? patient.getPatientId() : "PAT-100001";
        String mrn = patient != null ? patient.getMrn() : patId;
        String familyName = patient != null ? patient.getFamilyName() : "Smith";
        String givenName = patient != null ? patient.getGivenName() : "John";
        String middleName = patient != null && patient.getMiddleName() != null ? patient.getMiddleName() : "";
        String prefix = patient != null && patient.getPrefix() != null ? patient.getPrefix() : "";

        LocalDate dob = patient != null && patient.getDateOfBirth() != null ? patient.getDateOfBirth() : LocalDate.of(1980, 1, 1);
        String dobStr = dob.format(HL7_DATE_ONLY);
        String gender = patient != null && patient.getGender() != null ? patient.getGender().getHl7Code() : "U";

        String address1 = patient != null && patient.getAddressLine1() != null ? patient.getAddressLine1() : "100 Main St";
        String city = patient != null && patient.getCity() != null ? patient.getCity() : "Metropolis";
        String state = patient != null && patient.getState() != null ? patient.getState() : "NSW";
        String postCode = patient != null && patient.getPostalCode() != null ? patient.getPostalCode() : "2000";
        String country = patient != null && patient.getCountry() != null ? patient.getCountry() : "AU";
        String phone = patient != null && patient.getPhoneNumber() != null ? patient.getPhoneNumber() : "+61-400000000";
        String nationalId = patient != null && patient.getNationalId() != null ? patient.getNationalId() : "";

        sb.append("PID|1||").append(patId).append("^^^MRN||")
                .append(familyName).append("^").append(givenName).append("^").append(middleName).append("^").append(prefix).append("||")
                .append(dobStr).append("|").append(gender).append("|||")
                .append(address1).append("^^").append(city).append("^").append(state).append("^").append(postCode).append("^").append(country).append("||")
                .append(phone).append("|||||||").append(nationalId).append("\r");
    }

    private static void appendPv1Segment(StringBuilder sb, VisitProfile visit, String priorLocation) {
        String patientClass = visit != null ? visit.getPatientClass() : "I";
        String location = visit != null ? visit.getAssignedPatientLocation() : "WARD-3A^301^A^MAIN";
        String prior = priorLocation != null ? priorLocation : "";
        String docId = visit != null && visit.getAttendingDoctorId() != null ? visit.getAttendingDoctorId() : "DOC-101";
        String docName = visit != null && visit.getAttendingDoctorName() != null ? visit.getAttendingDoctorName() : "Dr. Alice Vance";
        String visitNumber = visit != null ? visit.getVisitNumber() : "VIS-200001";
        String admitTime = (visit != null && visit.getAdmitDateTime() != null) ? visit.getAdmitDateTime().format(HL7_DATE_TIME) : "";
        String dischargeTime = (visit != null && visit.getDischargeDateTime() != null) ? visit.getDischargeDateTime().format(HL7_DATE_TIME) : "";
        String dischargeDisp = (visit != null && visit.getDischargeDisposition() != null) ? visit.getDischargeDisposition() : "";

        sb.append("PV1|1|").append(patientClass).append("|").append(location).append("||||")
                .append(docId).append("^").append(docName).append("||||||||||")
                .append(prior).append("||").append(visitNumber).append("|||||||||||||||||||||||||")
                .append(admitTime).append("|").append(dischargeTime).append("||||").append(dischargeDisp).append("\r");
    }
}
