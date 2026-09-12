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

package net.fhirfactory.hie.mllpgateway.hl7;

import ca.uhn.hl7v2.util.Terser;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.CodeableConcept;
import org.hl7.fhir.r5.model.Coding;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.Task;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility and extraction class for HL7 v2.x ADT messages.
 */
@ApplicationScoped
public class AdtMessageExtractor {

    public String extractPatientId(String rawMessage, Terser terser) {
        String pid = getTerserValue(terser, "/PID-3-1", "PID-3-1");
        if (StringUtils.isNotBlank(pid)) return pid;
        pid = getTerserValue(terser, "/PID-2-1", "PID-2-1");
        if (StringUtils.isNotBlank(pid)) return pid;
        pid = getTerserValue(terser, "/PID-3", "PID-3");
        if (StringUtils.isNotBlank(pid)) return pid;
        return "UNKNOWN";
    }

    public String extractVisitNumber(String rawMessage, Terser terser) {
        String vn = getTerserValue(terser, "/PV1-19-1", "PV1-19-1");
        if (StringUtils.isNotBlank(vn)) return vn;
        vn = getTerserValue(terser, "/PV1-19", "PV1-19");
        if (StringUtils.isNotBlank(vn)) return vn;
        return "";
    }

    public String buildFullName(String given, String family) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(given)) sb.append(given);
        if (StringUtils.isNotBlank(family)) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(family);
        }
        return sb.length() > 0 ? sb.toString() : "Unknown Patient";
    }

    public String getTerserValue(Terser terser, String spec, String fallbackSegment) {
        return getTerserValue(terser, spec, fallbackSegment, null, null);
    }

    public String getTerserValue(Terser terser, String spec, String fallbackSegment, String altSpec, String altFallback) {
        if (terser != null && spec != null) {
            try {
                String val = terser.get(spec);
                if (StringUtils.isNotBlank(val)) return val.trim();
            } catch (Exception ignored) {}
        }
        if (terser != null && altSpec != null) {
            try {
                String val = terser.get(altSpec);
                if (StringUtils.isNotBlank(val)) return val.trim();
            } catch (Exception ignored) {}
        }
        return "";
    }

    public Enumerations.AdministrativeGender mapAdministrativeGender(String hl7Gender) {
        if (hl7Gender == null) return Enumerations.AdministrativeGender.UNKNOWN;
        return switch (hl7Gender.trim().toUpperCase()) {
            case "M", "MALE" -> Enumerations.AdministrativeGender.MALE;
            case "F", "FEMALE" -> Enumerations.AdministrativeGender.FEMALE;
            case "O", "OTHER" -> Enumerations.AdministrativeGender.OTHER;
            default -> Enumerations.AdministrativeGender.UNKNOWN;
        };
    }

    public CodeableConcept mapMaritalStatus(String hl7Code) {
        CodeableConcept cc = new CodeableConcept();
        if (hl7Code == null) return cc;
        String trimmed = hl7Code.trim().toUpperCase();
        String display = switch (trimmed) {
            case "M" -> "Married";
            case "S" -> "Never Married";
            case "D" -> "Divorced";
            case "W" -> "Widowed";
            case "A" -> "Separated";
            default -> "Unknown";
        };
        cc.addCoding(new Coding("http://terminology.hl7.org/CodeSystem/v3-MaritalStatus", trimmed, display));
        cc.setText(display);
        return cc;
    }

    public Task.TaskStatus determineTaskStatus(String triggerEvent) {
        if (triggerEvent == null) return Task.TaskStatus.REQUESTED;
        return switch (triggerEvent.toUpperCase()) {
            case "A01", "A04" -> Task.TaskStatus.REQUESTED;
            case "A02", "A08" -> Task.TaskStatus.INPROGRESS;
            case "A03" -> Task.TaskStatus.COMPLETED;
            default -> Task.TaskStatus.REQUESTED;
        };
    }

    public Enumerations.RequestPriority determinePriority(String patientClass) {
        if (patientClass == null) return Enumerations.RequestPriority.ROUTINE;
        return switch (patientClass.trim().toUpperCase()) {
            case "E", "EMERGENCY" -> Enumerations.RequestPriority.STAT;
            case "U", "URGENT" -> Enumerations.RequestPriority.URGENT;
            default -> Enumerations.RequestPriority.ROUTINE;
        };
    }

    public String getTriggerEventDescription(String triggerEvent) {
        if (triggerEvent == null) return "HL7 ADT Trigger Event";
        return switch (triggerEvent.toUpperCase()) {
            case "A01" -> "Admit/Visit Notification";
            case "A02" -> "Transfer a Patient";
            case "A03" -> "Discharge/End Visit";
            case "A04" -> "Register a Patient";
            case "A05" -> "Pre-admit a Patient";
            case "A08" -> "Update Patient Information";
            case "A11" -> "Cancel Admit/Visit Notification";
            case "A12" -> "Cancel Transfer";
            case "A13" -> "Cancel Discharge/End Visit";
            case "A28" -> "Add Person Information";
            case "A31" -> "Update Person Information";
            case "A40" -> "Merge Patient - Patient Identifier List";
            default -> "HL7 ADT " + triggerEvent + " Event";
        };
    }

    public Enumerations.EncounterStatus mapEncounterStatus(String triggerEvent) {
        if (triggerEvent == null) return Enumerations.EncounterStatus.INPROGRESS;
        return switch (triggerEvent.toUpperCase()) {
            case "A01", "A04", "A02", "A08" -> Enumerations.EncounterStatus.INPROGRESS;
            case "A03" -> Enumerations.EncounterStatus.COMPLETED;
            case "A05" -> Enumerations.EncounterStatus.PLANNED;
            case "A11", "A12", "A13" -> Enumerations.EncounterStatus.CANCELLED;
            default -> Enumerations.EncounterStatus.INPROGRESS;
        };
    }

    public CodeableConcept mapEncounterClass(String patientClass) {
        CodeableConcept cc = new CodeableConcept();
        if (patientClass == null) {
            cc.addCoding(new Coding("http://terminology.hl7.org/CodeSystem/v3-ActCode", "IMP", "inpatient encounter"));
            return cc;
        }
        String trimmed = patientClass.trim().toUpperCase();
        return switch (trimmed) {
            case "I", "INPATIENT" -> cc.addCoding(new Coding("http://terminology.hl7.org/CodeSystem/v3-ActCode", "IMP", "inpatient encounter"));
            case "O", "OUTPATIENT" -> cc.addCoding(new Coding("http://terminology.hl7.org/CodeSystem/v3-ActCode", "AMB", "ambulatory"));
            case "E", "EMERGENCY" -> cc.addCoding(new Coding("http://terminology.hl7.org/CodeSystem/v3-ActCode", "EMER", "emergency"));
            case "P", "PREADMIT" -> cc.addCoding(new Coding("http://terminology.hl7.org/CodeSystem/v3-ActCode", "PRENC", "pre-admission"));
            default -> cc.addCoding(new Coding("http://terminology.hl7.org/CodeSystem/v3-ActCode", "IMP", "inpatient encounter"));
        };
    }

    public Date parseHl7Date(String hl7DateStr) {
        if (StringUtils.isBlank(hl7DateStr)) return null;
        String clean = hl7DateStr.trim();
        String[] patterns = {
                "yyyyMMddHHmmss.SSSZ", "yyyyMMddHHmmssZ", "yyyyMMddHHmmss",
                "yyyyMMddHHmm", "yyyyMMdd"
        };
        for (String pattern : patterns) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                sdf.setLenient(true);
                return sdf.parse(clean);
            } catch (Exception ignored) {}
        }
        return null;
    }

    public String cleanPhoneNumber(String raw) {
        if (raw == null) return "";
        return raw.replaceAll("[^0-9+()\\- ]", "").trim();
    }

    public String defaultIfBlank(String val, String def) {
        return StringUtils.isNotBlank(val) ? val.trim() : def;
    }

    public String generateFallbackAck(String controlId, String triggerEvent, String ackCode, String errorMsg) {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        return "MSH|^~\\&|HIE_MLLP_GATEWAY|HIE|CLIENT|CLIENT|" + timestamp + "||ACK^" + triggerEvent + "|" + controlId + "|P|2.4\r" +
                "MSA|" + ackCode + "|" + controlId + "|" + (errorMsg != null ? errorMsg : "") + "\r";
    }
}
