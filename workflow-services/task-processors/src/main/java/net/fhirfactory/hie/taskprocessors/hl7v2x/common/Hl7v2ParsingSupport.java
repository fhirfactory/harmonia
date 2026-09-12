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

package net.fhirfactory.hie.taskprocessors.hl7v2x.common;

import ca.uhn.hl7v2.util.Terser;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.CodeableConcept;
import org.hl7.fhir.r5.model.Coding;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.HumanName;
import org.hl7.fhir.r5.model.Patient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility functions for parsing HL7 v2 messages and mapping vocabulary to FHIR R5 concepts.
 */
public final class Hl7v2ParsingSupport {

    public static final String EXTENSION_RACE = "http://hl7.org/fhir/us/core/StructureDefinition/us-core-race";
    public static final String EXTENSION_ETHNICITY = "http://hl7.org/fhir/us/core/StructureDefinition/us-core-ethnicity";
    public static final String EXTENSION_RELIGION = "http://hl7.org/fhir/StructureDefinition/patient-religion";
    public static final String EXTENSION_BIRTH_PLACE = "http://hl7.org/fhir/StructureDefinition/patient-birthPlace";
    public static final String EXTENSION_CITIZENSHIP = "http://hl7.org/fhir/StructureDefinition/patient-citizenship";
    public static final String EXTENSION_MOTHER_MAIDEN_NAME = "http://hl7.org/fhir/StructureDefinition/patient-mothersMaidenName";

    private Hl7v2ParsingSupport() {
        // utility class
    }

    public static String extractPatientId(Terser terser, String rawMessage) {
        String pid3 = extractTerserOrRegex(terser, "/PID-3-1", rawMessage, "PID", 3, null);
        if (StringUtils.isNotBlank(pid3)) return pid3.split("\\^")[0];
        String pid2 = extractTerserOrRegex(terser, "/PID-2-1", rawMessage, "PID", 2, null);
        if (StringUtils.isNotBlank(pid2)) return pid2.split("\\^")[0];
        return null;
    }

    public static String extractVisitNumber(Terser terser, String rawMessage) {
        String pv1_19 = extractTerserOrRegex(terser, "/PV1-19-1", rawMessage, "PV1", 19, null);
        if (StringUtils.isNotBlank(pv1_19)) return pv1_19.split("\\^")[0];
        String pid18 = extractTerserOrRegex(terser, "/PID-18-1", rawMessage, "PID", 18, null);
        if (StringUtils.isNotBlank(pid18)) return pid18.split("\\^")[0];
        return null;
    }

    public static String extractTerserOrRegex(Terser terser, String terserPath, String rawMessage, String segment, int fieldIndex, String defaultValue) {
        if (terser != null && StringUtils.isNotBlank(terserPath)) {
            try {
                String val = terser.get(terserPath);
                if (StringUtils.isNotBlank(val)) return val.trim();
            } catch (Exception ignored) {
            }
            if (terserPath.endsWith("-1")) {
                try {
                    String alt = terser.get(terserPath.substring(0, terserPath.length() - 2));
                    if (StringUtils.isNotBlank(alt)) return alt.trim();
                } catch (Exception ignored) {
                }
            } else {
                try {
                    String alt = terser.get(terserPath + "-1");
                    if (StringUtils.isNotBlank(alt)) return alt.trim();
                } catch (Exception ignored) {
                }
            }
        }

        if (StringUtils.isNotBlank(rawMessage) && StringUtils.isNotBlank(segment)) {
            String[] lines = rawMessage.replace("\r\n", "\n").replace("\r", "\n").split("\n");
            for (String line : lines) {
                String trimmedLine = line.trim();
                if (trimmedLine.startsWith(segment + "|")) {
                    String[] fields = trimmedLine.split("\\|", -1);
                    if (fields.length > fieldIndex && StringUtils.isNotBlank(fields[fieldIndex])) {
                        return fields[fieldIndex].trim();
                    }
                }
            }
        }

        return defaultValue;
    }

    public static Enumerations.AdministrativeGender mapAdministrativeGender(String genderCode) {
        if (StringUtils.isBlank(genderCode)) return Enumerations.AdministrativeGender.UNKNOWN;
        String g = genderCode.trim().toUpperCase();
        if ("M".equals(g) || "MALE".equals(g)) return Enumerations.AdministrativeGender.MALE;
        if ("F".equals(g) || "FEMALE".equals(g)) return Enumerations.AdministrativeGender.FEMALE;
        if ("O".equals(g) || "OTHER".equals(g)) return Enumerations.AdministrativeGender.OTHER;
        return Enumerations.AdministrativeGender.UNKNOWN;
    }

    public static CodeableConcept mapMaritalStatus(String maritalCode) {
        CodeableConcept cc = new CodeableConcept();
        if (StringUtils.isBlank(maritalCode)) return cc;
        String code = maritalCode.trim().toUpperCase();
        String display = switch (code) {
            case "M", "MARRIED" -> "Married";
            case "S", "SINGLE" -> "Single";
            case "D", "DIVORCED" -> "Divorced";
            case "W", "WIDOWED" -> "Widowed";
            case "L", "LEGALLY_SEPARATED" -> "Legally Separated";
            default -> maritalCode;
        };
        cc.setText(display);
        cc.addCoding(new Coding("http://terminology.hl7.org/CodeSystem/v3-MaritalStatus", code.substring(0, 1), display));
        return cc;
    }

    public static Enumerations.EncounterStatus mapEncounterStatus(String triggerEvent) {
        if (StringUtils.isBlank(triggerEvent)) return Enumerations.EncounterStatus.INPROGRESS;
        String event = triggerEvent.toUpperCase().trim();
        return switch (event) {
            case "A01", "A02", "A04", "A05", "A06", "A07", "A08" -> Enumerations.EncounterStatus.INPROGRESS;
            case "A03" -> Enumerations.EncounterStatus.COMPLETED;
            case "A11", "A12", "A13", "A38" -> Enumerations.EncounterStatus.CANCELLED;
            case "A21", "A22" -> Enumerations.EncounterStatus.ONHOLD;
            default -> Enumerations.EncounterStatus.INPROGRESS;
        };
    }

    public static CodeableConcept mapEncounterClass(String patientClass) {
        CodeableConcept cc = new CodeableConcept();
        if (StringUtils.isBlank(patientClass)) {
            cc.addCoding(new Coding("http://terminology.hl7.org/CodeSystem/v3-ActCode", "IMP", "inpatient encounter"));
            return cc;
        }
        String pClass = patientClass.trim().toUpperCase();
        switch (pClass) {
            case "I", "INPATIENT" -> cc.addCoding(new Coding("http://terminology.hl7.org/CodeSystem/v3-ActCode", "IMP", "inpatient encounter"));
            case "O", "OUTPATIENT" -> cc.addCoding(new Coding("http://terminology.hl7.org/CodeSystem/v3-ActCode", "AMB", "ambulatory"));
            case "E", "EMERGENCY" -> cc.addCoding(new Coding("http://terminology.hl7.org/CodeSystem/v3-ActCode", "EMER", "emergency"));
            case "P", "PREADMIT" -> cc.addCoding(new Coding("http://terminology.hl7.org/CodeSystem/v3-ActCode", "PRENC", "pre-admission"));
            default -> cc.addCoding(new Coding("http://terminology.hl7.org/CodeSystem/v3-ActCode", "IMP", "inpatient encounter"));
        }
        return cc;
    }

    public static Date parseHl7Date(String dateStr) {
        if (StringUtils.isBlank(dateStr)) return null;
        String clean = dateStr.trim();
        String[] patterns = {
                "yyyyMMddHHmmss", "yyyyMMddHHmm", "yyyyMMdd",
                "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss.SSS", "yyyy-MM-dd"
        };
        for (String p : patterns) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(p);
                sdf.setLenient(false);
                return sdf.parse(clean);
            } catch (ParseException ignored) {
            }
        }
        return null;
    }

    public static String buildFullName(String given, String middle, String family, String prefix, String suffix) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(prefix)) sb.append(prefix.trim()).append(" ");
        if (StringUtils.isNotBlank(given)) sb.append(given.trim()).append(" ");
        if (StringUtils.isNotBlank(middle)) sb.append(middle.trim()).append(" ");
        if (StringUtils.isNotBlank(family)) sb.append(family.trim()).append(" ");
        if (StringUtils.isNotBlank(suffix)) sb.append(suffix.trim()).append(" ");
        return sb.toString().trim();
    }

    public static String extractFullName(Patient patient) {
        if (patient == null || !patient.hasName()) return null;
        HumanName name = patient.getNameFirstRep();
        if (StringUtils.isNotBlank(name.getText())) return name.getText();
        String given = name.hasGiven() ? String.join(" ", name.getGivenAsSingleString()) : "";
        String family = name.hasFamily() ? name.getFamily() : "";
        return buildFullName(given, "", family, "", "");
    }

    public static String cleanPhoneNumber(String phone) {
        if (phone == null) return null;
        String clean = phone.split("\\^")[0].trim();
        return clean.replaceAll("[^0-9+extEXT#() -]", "");
    }

    public static boolean isNumeric(String str) {
        if (str == null) return false;
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String cleanId(String id) {
        if (id == null) return "";
        String clean = id.trim();
        if (clean.startsWith("#")) clean = clean.substring(1);
        return clean.replace("Patient/", "")
                .replace("Task/", "")
                .replace("Encounter/", "")
                .replace("Practitioner/", "")
                .replace("PractitionerRole/", "")
                .replace("Organization/", "")
                .replace("Location/", "")
                .replace("Communication/", "")
                .replace("Provenance/", "")
                .trim();
    }

    public static boolean isHl7Message(String payload) {
        if (StringUtils.isBlank(payload)) return false;
        String trimmed = payload.trim();
        return trimmed.startsWith("MSH|") || trimmed.contains("\rMSH|") || trimmed.contains("\nMSH|");
    }
}
