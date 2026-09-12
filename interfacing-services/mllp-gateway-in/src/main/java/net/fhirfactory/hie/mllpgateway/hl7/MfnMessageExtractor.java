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
 * Extraction and helper class for HL7 v2.x MFN^M02 messages.
 */
@ApplicationScoped
public class MfnMessageExtractor {

    public String extractPractitionerId(String rawMessage, Terser terser) {
        String stf1 = getTerserValue(terser, "/STF-1-1", "STF-1-1");
        if (StringUtils.isNotBlank(stf1)) return cleanId(stf1);
        stf1 = getTerserValue(terser, "/STF-1", "STF-1");
        if (StringUtils.isNotBlank(stf1)) return cleanId(stf1.split("\\^")[0]);

        String stf2 = getTerserValue(terser, "/STF-2-1", "STF-2-1");
        if (StringUtils.isNotBlank(stf2)) return cleanId(stf2);
        stf2 = getTerserValue(terser, "/STF-2", "STF-2");
        if (StringUtils.isNotBlank(stf2)) return cleanId(stf2.split("\\^")[0]);

        String mfe4 = getTerserValue(terser, "/MFE-4-1", "MFE-4-1");
        if (StringUtils.isNotBlank(mfe4)) return cleanId(mfe4);
        mfe4 = getTerserValue(terser, "/MFE-4", "MFE-4");
        if (StringUtils.isNotBlank(mfe4)) return cleanId(mfe4.split("\\^")[0]);

        String pra1 = getTerserValue(terser, "/PRA-1-1", "PRA-1-1");
        if (StringUtils.isNotBlank(pra1)) return cleanId(pra1);
        pra1 = getTerserValue(terser, "/PRA-1", "PRA-1");
        if (StringUtils.isNotBlank(pra1)) return cleanId(pra1.split("\\^")[0]);

        if (StringUtils.isNotBlank(rawMessage)) {
            for (String line : rawMessage.replace("\r\n", "\n").replace("\r", "\n").split("\n")) {
                String trimmed = line.trim();
                if (trimmed.startsWith("STF|")) {
                    String[] fields = trimmed.split("\\|", -1);
                    if (fields.length > 1 && StringUtils.isNotBlank(fields[1])) {
                        return cleanId(fields[1].split("\\^")[0]);
                    }
                    if (fields.length > 2 && StringUtils.isNotBlank(fields[2])) {
                        return cleanId(fields[2].split("\\^")[0]);
                    }
                }
                if (trimmed.startsWith("MFE|")) {
                    String[] fields = trimmed.split("\\|", -1);
                    if (fields.length > 4 && StringUtils.isNotBlank(fields[4])) {
                        return cleanId(fields[4].split("\\^")[0]);
                    }
                }
            }
        }

        return "UNKNOWN";
    }

    public String extractPractitionerFullName(String rawMessage, Terser terser) {
        String family = getTerserValue(terser, "/STF-3-1", "STF-3-1");
        String given = getTerserValue(terser, "/STF-3-2", "STF-3-2");
        String middle = getTerserValue(terser, "/STF-3-3", "STF-3-3");
        String prefix = getTerserValue(terser, "/STF-3-5", "STF-3-5");
        String suffix = getTerserValue(terser, "/STF-3-4", "STF-3-4");

        if (StringUtils.isNotBlank(family) || StringUtils.isNotBlank(given)) {
            return buildFullName(given, middle, family, prefix, suffix);
        }

        String mfeName = getTerserValue(terser, "/MFE-4-2", "MFE-4-2");
        String mfeGiven = getTerserValue(terser, "/MFE-4-3", "MFE-4-3");
        if (StringUtils.isNotBlank(mfeName) || StringUtils.isNotBlank(mfeGiven)) {
            return buildFullName(mfeGiven, null, mfeName, null, null);
        }

        if (StringUtils.isNotBlank(rawMessage)) {
            for (String line : rawMessage.replace("\r\n", "\n").replace("\r", "\n").split("\n")) {
                String trimmed = line.trim();
                if (trimmed.startsWith("STF|")) {
                    String[] fields = trimmed.split("\\|", -1);
                    if (fields.length > 3 && StringUtils.isNotBlank(fields[3])) {
                        String[] nameParts = fields[3].split("\\^");
                        String f = nameParts.length > 0 ? nameParts[0] : "";
                        String g = nameParts.length > 1 ? nameParts[1] : "";
                        String m = nameParts.length > 2 ? nameParts[2] : "";
                        String sfx = nameParts.length > 3 ? nameParts[3] : "";
                        String pfx = nameParts.length > 4 ? nameParts[4] : "";
                        return buildFullName(g, m, f, pfx, sfx);
                    }
                }
            }
        }

        return "Unknown Practitioner";
    }

    public String buildFullName(String given, String middle, String family, String prefix, String suffix) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(prefix)) sb.append(prefix.trim()).append(" ");
        if (StringUtils.isNotBlank(given)) sb.append(given.trim()).append(" ");
        if (StringUtils.isNotBlank(middle)) sb.append(middle.trim()).append(" ");
        if (StringUtils.isNotBlank(family)) sb.append(family.trim()).append(" ");
        if (StringUtils.isNotBlank(suffix)) sb.append(suffix.trim()).append(" ");
        String res = sb.toString().trim();
        return StringUtils.isNotBlank(res) ? res : "Unknown Practitioner";
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

    public String extractMfeActionCode(String rawMessage, Terser terser) {
        String val = getTerserValue(terser, "/.MFE-1", "MFE-1");
        if (StringUtils.isNotBlank(val)) return val;
        val = getTerserValue(terser, "/MFE-1", "MFE-1");
        if (StringUtils.isNotBlank(val)) return val;
        val = getTerserValue(terser, "/MF_STAFF/MFE-1", "MFE-1");
        if (StringUtils.isNotBlank(val)) return val;
        if (StringUtils.isNotBlank(rawMessage)) {
            for (String line : rawMessage.replace("\r\n", "\n").replace("\r", "\n").split("\n")) {
                String trimmed = line.trim();
                if (trimmed.startsWith("MFE|")) {
                    String[] fields = trimmed.split("\\|", -1);
                    if (fields.length > 1 && StringUtils.isNotBlank(fields[1])) {
                        return fields[1].trim();
                    }
                }
            }
        }
        return "MAD";
    }

    public Task.TaskStatus determineTaskStatus(String actionCode) {
        if (actionCode == null) return Task.TaskStatus.REQUESTED;
        return switch (actionCode.trim().toUpperCase()) {
            case "MAD" -> Task.TaskStatus.REQUESTED;
            case "MUP" -> Task.TaskStatus.INPROGRESS;
            case "MDL", "MDC" -> Task.TaskStatus.COMPLETED;
            default -> Task.TaskStatus.REQUESTED;
        };
    }

    public String getTriggerEventDescription(String triggerEvent) {
        if (triggerEvent == null) return "Master File Notification - Staff/Practitioner";
        return switch (triggerEvent.toUpperCase()) {
            case "M02" -> "Master File Notification - Staff/Practitioner";
            case "M01" -> "Master File Notification - Generic";
            case "M03" -> "Master File Notification - Test/Observation";
            case "M04" -> "Master File Notification - Charge/Price";
            case "M05" -> "Master File Notification - Location Services";
            default -> "HL7 MFN " + triggerEvent + " Event";
        };
    }

    public Date parseHl7Date(String hl7DateStr) {
        if (StringUtils.isBlank(hl7DateStr)) return null;
        String clean = hl7DateStr.trim();
        String[] patterns = {
                "yyyyMMddHHmmss.SSSZ", "yyyyMMddHHmmssZ", "yyyyMMddHHmmss",
                "yyyyMMddHHmm", "yyyyMMdd", "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd"
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
        String val = raw.split("\\^")[0].trim();
        return val.replaceAll("[^0-9+()\\- ]", "").trim();
    }

    public String cleanId(String id) {
        if (id == null) return "";
        String clean = id.trim();
        if (clean.startsWith("#")) clean = clean.substring(1);
        return clean.replace("Practitioner/", "")
                .replace("PractitionerRole/", "")
                .replace("Organization/", "")
                .replace("Location/", "")
                .replace("Task/", "")
                .replace("Communication/", "")
                .replace("Provenance/", "")
                .replace("Bundle/", "");
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
