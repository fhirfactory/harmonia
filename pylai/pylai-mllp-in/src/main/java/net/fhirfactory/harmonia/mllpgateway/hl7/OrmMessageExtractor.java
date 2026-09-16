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

package net.fhirfactory.harmonia.mllpgateway.hl7;

import ca.uhn.hl7v2.util.Terser;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Robust extraction utility for HL7 v2.4 ORM^O01 order messages.
 */
@ApplicationScoped
public class OrmMessageExtractor {

    private static final Pattern PATIENT_ID_PATTERN = Pattern.compile("^PID\\|[^|]*\\|[^|]*\\|([^|^\\r\\n]+)", Pattern.MULTILINE);
    private static final Pattern PLACER_ORDER_PATTERN = Pattern.compile("^ORC\\|[^|]*\\|([^|^\\r\\n]+)", Pattern.MULTILINE);
    private static final Pattern OBR_4_PATTERN = Pattern.compile("^OBR\\|[^|]*\\|[^|]*\\|[^|]*\\|([^|^\\r\\n]+)", Pattern.MULTILINE);
    private static final Pattern MSH_10_PATTERN = Pattern.compile("^MSH\\|[^|]*\\|[^|]*\\|[^|]*\\|[^|]*\\|[^|]*\\|[^|]*\\|[^|]*\\|[^|]*\\|([^|\\r\\n]+)");

    public String extractPlacerOrderNumber(String rawMessage, Terser terser) {
        if (terser != null) {
            try {
                String id = terser.get("/.ORC-2-1");
                if (StringUtils.isNotBlank(id)) return id.trim();
                id = terser.get("/.OBR-2-1");
                if (StringUtils.isNotBlank(id)) return id.trim();
            } catch (Exception ignored) {
            }
        }
        if (StringUtils.isNotBlank(rawMessage)) {
            Matcher m = PLACER_ORDER_PATTERN.matcher(rawMessage);
            if (m.find()) {
                return m.group(1).trim();
            }
        }
        return "ORD-UNKNOWN";
    }

    public String extractUniversalServiceId(String rawMessage, Terser terser) {
        if (terser != null) {
            try {
                String code = terser.get("/.OBR-4-1");
                if (StringUtils.isNotBlank(code)) return code.trim();
            } catch (Exception ignored) {
            }
        }
        if (StringUtils.isNotBlank(rawMessage)) {
            Matcher m = OBR_4_PATTERN.matcher(rawMessage);
            if (m.find()) {
                return m.group(1).trim();
            }
        }
        return "UNKNOWN";
    }

    public String extractUniversalServiceText(String rawMessage, Terser terser) {
        if (terser != null) {
            try {
                String text = terser.get("/.OBR-4-2");
                if (StringUtils.isNotBlank(text)) return text.trim();
            } catch (Exception ignored) {
            }
        }
        return extractUniversalServiceId(rawMessage, terser);
    }

    public String extractPatientId(String rawMessage, Terser terser) {
        if (terser != null) {
            try {
                String id = terser.get("/.PID-3-1");
                if (StringUtils.isNotBlank(id)) return id.trim();
            } catch (Exception ignored) {
            }
        }
        if (StringUtils.isNotBlank(rawMessage)) {
            Matcher m = PATIENT_ID_PATTERN.matcher(rawMessage);
            if (m.find()) {
                return m.group(1).trim();
            }
        }
        return "PAT-UNKNOWN";
    }

    public String extractPatientFullName(String rawMessage, Terser terser) {
        if (terser != null) {
            try {
                String family = terser.get("/.PID-5-1");
                String given = terser.get("/.PID-5-2");
                if (StringUtils.isNotBlank(family) || StringUtils.isNotBlank(given)) {
                    return ((StringUtils.defaultString(given) + " " + StringUtils.defaultString(family))).trim();
                }
            } catch (Exception ignored) {
            }
        }
        return "Unknown Patient";
    }

    public String extractOrderingProviderId(String rawMessage, Terser terser) {
        if (terser != null) {
            try {
                String doc = terser.get("/.ORC-12-1");
                if (StringUtils.isNotBlank(doc)) return doc.trim();
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    public String extractOrderingProviderName(String rawMessage, Terser terser) {
        if (terser != null) {
            try {
                String family = terser.get("/.ORC-12-2");
                String given = terser.get("/.ORC-12-3");
                if (StringUtils.isNotBlank(family) || StringUtils.isNotBlank(given)) {
                    return ((StringUtils.defaultString(given) + " " + StringUtils.defaultString(family))).trim();
                }
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    public String extractOrderControl(String rawMessage, Terser terser) {
        if (terser != null) {
            try {
                String ctl = terser.get("/.ORC-1");
                if (StringUtils.isNotBlank(ctl)) return ctl.trim();
            } catch (Exception ignored) {
            }
        }
        return "NW";
    }

    public Task.TaskStatus determineTaskStatus(String orderControl) {
        if (StringUtils.isBlank(orderControl)) {
            return Task.TaskStatus.REQUESTED;
        }
        return switch (orderControl.trim().toUpperCase()) {
            case "CA", "DC" -> Task.TaskStatus.CANCELLED;
            case "HD" -> Task.TaskStatus.ONHOLD;
            case "CM" -> Task.TaskStatus.COMPLETED;
            case "IP" -> Task.TaskStatus.INPROGRESS;
            default -> Task.TaskStatus.REQUESTED;
        };
    }

    public Enumerations.RequestPriority determinePriority(String priorityCode) {
        if (StringUtils.isBlank(priorityCode)) {
            return Enumerations.RequestPriority.ROUTINE;
        }
        return switch (priorityCode.trim().toUpperCase()) {
            case "S", "STAT" -> Enumerations.RequestPriority.STAT;
            case "A", "ASAP" -> Enumerations.RequestPriority.ASAP;
            case "U", "URGENT" -> Enumerations.RequestPriority.URGENT;
            default -> Enumerations.RequestPriority.ROUTINE;
        };
    }

    public String generateFallbackAck(String rawMessage, String ackCode, String errorMessage) {
        String controlId = "UNKNOWN";
        if (StringUtils.isNotBlank(rawMessage)) {
            Matcher matcher = MSH_10_PATTERN.matcher(rawMessage.trim());
            if (matcher.find()) {
                controlId = matcher.group(1).trim();
            }
        }
        return "MSH|^~\\&|HARMONIA|HIE|SIMULATOR|FACILITY|20260915120000||ACK|" + java.util.UUID.randomUUID().toString().substring(0, 8) + "|P|2.4\r"
                + "MSA|" + (ackCode != null ? ackCode : "AE") + "|" + controlId + "|" + (errorMessage != null ? errorMessage : "Error") + "\r";
    }

    public Date parseHl7Date(String hl7DateStr) {
        if (StringUtils.isBlank(hl7DateStr)) return null;
        String clean = hl7DateStr.trim();
        String[] patterns = {"yyyyMMddHHmmss", "yyyyMMddHHmm", "yyyyMMdd"};
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

    public String cleanId(String rawId) {
        if (rawId == null) return "UNKNOWN";
        return rawId.replace("#", "").trim().replaceAll("[^A-Za-z0-9-_.]", "-");
    }
}
