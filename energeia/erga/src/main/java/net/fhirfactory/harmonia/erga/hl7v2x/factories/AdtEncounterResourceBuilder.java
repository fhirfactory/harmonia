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

package net.fhirfactory.harmonia.erga.hl7v2x.factories;

import ca.uhn.hl7v2.util.Terser;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.*;

import java.util.*;

import static net.fhirfactory.harmonia.erga.hl7v2x.common.Hl7v2ParsingSupport.*;

/**
 * Builds FHIR {@link Encounter}, {@link Practitioner}, and {@link Location} resources from HL7 v2 messages.
 */
public class AdtEncounterResourceBuilder {

    /**
     * Builds Encounter resource from PV1 and PV2 segments.
     */
    public Encounter buildEncounter(Terser terser, String rawMessage, String messageControlId, String triggerEvent,
                                    String patientId, List<Practitioner> practitioners, List<Location> locations) {
        String visitNumber = extractVisitNumber(terser, rawMessage);
        String encounterId = StringUtils.isNotBlank(visitNumber) ? visitNumber : messageControlId;

        Encounter encounter = new Encounter();
        encounter.setId("Encounter/" + cleanId(encounterId));
        encounter.setStatus(mapEncounterStatus(triggerEvent));

        String patientClass = extractTerserOrRegex(terser, "/PV1-2", rawMessage, "PV1", 2, "I");
        CodeableConcept encClass = mapEncounterClass(patientClass);
        encounter.setClass_(List.of(encClass));

        encounter.setSubject(new Reference("Patient/" + cleanId(patientId)));

        if (StringUtils.isNotBlank(visitNumber)) {
            Identifier visitId = encounter.addIdentifier();
            visitId.setSystem("http://example.org/visit-numbers");
            visitId.setValue(visitNumber);
            visitId.setType(new CodeableConcept().setText("Visit Number"));
        }

        // Practitioners as participants
        if (practitioners != null) {
            for (Practitioner p : practitioners) {
                Encounter.EncounterParticipantComponent participant = encounter.addParticipant();
                participant.setActor(new Reference("Practitioner/" + p.getIdPart()).setDisplay(
                        p.hasName() ? p.getNameFirstRep().getNameAsSingleString() : p.getIdPart()));
            }
        }

        // Locations
        if (locations != null) {
            for (Location loc : locations) {
                Encounter.EncounterLocationComponent encLoc = encounter.addLocation();
                encLoc.setLocation(new Reference("Location/" + loc.getIdPart()).setDisplay(loc.getName()));
                encLoc.setStatus(Encounter.EncounterLocationStatus.ACTIVE);
            }
        }

        // Admission & Discharge Dates
        String admitDateTimeStr = extractTerserOrRegex(terser, "/PV1-44-1", rawMessage, "PV1", 44, null);
        String dischargeDateTimeStr = extractTerserOrRegex(terser, "/PV1-45-1", rawMessage, "PV1", 45, null);
        Date admitDate = parseHl7Date(admitDateTimeStr);
        Date dischargeDate = parseHl7Date(dischargeDateTimeStr);

        Period period = new Period();
        if (admitDate != null) period.setStart(admitDate);
        if (dischargeDate != null) period.setEnd(dischargeDate);
        if (period.hasStart() || period.hasEnd()) {
            encounter.setActualPeriod(period);
        }

        // Hospitalization details
        String admitSource = extractTerserOrRegex(terser, "/PV1-14", rawMessage, "PV1", 14, null);
        String dischargeDisp = extractTerserOrRegex(terser, "/PV1-36", rawMessage, "PV1", 36, null);
        if (StringUtils.isNotBlank(admitSource) || StringUtils.isNotBlank(dischargeDisp)) {
            Encounter.EncounterAdmissionComponent admission = new Encounter.EncounterAdmissionComponent();
            if (StringUtils.isNotBlank(admitSource)) {
                admission.setAdmitSource(new CodeableConcept().setText(admitSource));
            }
            if (StringUtils.isNotBlank(dischargeDisp)) {
                admission.setDischargeDisposition(new CodeableConcept().setText(dischargeDisp));
            }
            encounter.setAdmission(admission);
        }

        return encounter;
    }

    /**
     * Builds Practitioner resources from PV1, PD1, and EVN segments.
     */
    public List<Practitioner> buildPractitioners(Terser terser, String rawMessage) {
        List<Practitioner> list = new ArrayList<>();
        Map<String, Practitioner> map = new LinkedHashMap<>();

        // 1. Attending Doctor (PV1-7)
        addPractitioner(map, extractTerserOrRegex(terser, "/PV1-7-1", rawMessage, "PV1", 7, null),
                extractTerserOrRegex(terser, "/PV1-7-2", rawMessage, "PV1", 7, null),
                extractTerserOrRegex(terser, "/PV1-7-3", rawMessage, "PV1", 7, null), "Attending Physician");

        // 2. Referring Doctor (PV1-8)
        addPractitioner(map, extractTerserOrRegex(terser, "/PV1-8-1", rawMessage, "PV1", 8, null),
                extractTerserOrRegex(terser, "/PV1-8-2", rawMessage, "PV1", 8, null),
                extractTerserOrRegex(terser, "/PV1-8-3", rawMessage, "PV1", 8, null), "Referring Physician");

        // 3. Consulting Doctor (PV1-9 / PV1-17)
        addPractitioner(map, extractTerserOrRegex(terser, "/PV1-9-1", rawMessage, "PV1", 9, null),
                extractTerserOrRegex(terser, "/PV1-9-2", rawMessage, "PV1", 9, null),
                extractTerserOrRegex(terser, "/PV1-9-3", rawMessage, "PV1", 9, null), "Consulting Physician");

        // 4. Admitting Doctor (PV1-19)
        addPractitioner(map, extractTerserOrRegex(terser, "/PV1-19-1", rawMessage, "PV1", 19, null),
                extractTerserOrRegex(terser, "/PV1-19-2", rawMessage, "PV1", 19, null),
                extractTerserOrRegex(terser, "/PV1-19-3", rawMessage, "PV1", 19, null), "Admitting Physician");

        // 5. Primary Care Doctor (PD1-4)
        addPractitioner(map, extractTerserOrRegex(terser, "/PD1-4-1", rawMessage, "PD1", 4, null),
                extractTerserOrRegex(terser, "/PD1-4-2", rawMessage, "PD1", 4, null),
                extractTerserOrRegex(terser, "/PD1-4-3", rawMessage, "PD1", 4, null), "Primary Care Physician");

        // 6. Operator / Data Enterer (EVN-5)
        addPractitioner(map, extractTerserOrRegex(terser, "/EVN-5-1", rawMessage, "EVN", 5, null),
                extractTerserOrRegex(terser, "/EVN-5-2", rawMessage, "EVN", 5, null),
                extractTerserOrRegex(terser, "/EVN-5-3", rawMessage, "EVN", 5, null), "Operator");

        list.addAll(map.values());
        return list;
    }

    /**
     * Builds Location resources from PV1 assigned patient locations.
     */
    public List<Location> buildLocations(Terser terser, String rawMessage) {
        List<Location> list = new ArrayList<>();

        String poc = extractTerserOrRegex(terser, "/PV1-3-1", rawMessage, "PV1", 3, null);
        String room = extractTerserOrRegex(terser, "/PV1-3-2", rawMessage, null, 0, null);
        String bed = extractTerserOrRegex(terser, "/PV1-3-3", rawMessage, null, 0, null);
        String facility = extractTerserOrRegex(terser, "/PV1-3-4", rawMessage, null, 0, null);

        if (poc != null && poc.contains("^")) {
            String[] parts = poc.split("\\^");
            poc = parts.length > 0 ? parts[0] : "";
            if (parts.length > 1 && StringUtils.isBlank(room)) room = parts[1];
            if (parts.length > 2 && StringUtils.isBlank(bed)) bed = parts[2];
            if (parts.length > 3 && StringUtils.isBlank(facility)) facility = parts[3];
        }

        if (StringUtils.isNotBlank(poc) || StringUtils.isNotBlank(room) || StringUtils.isNotBlank(bed) || StringUtils.isNotBlank(facility)) {
            Location loc = new Location();
            String locId = "loc-" + cleanId(poc + (StringUtils.isNotBlank(room) ? "-" + room : "") + (StringUtils.isNotBlank(bed) ? "-" + bed : ""));
            if ("loc-".equals(locId)) locId = "loc-assigned-" + UUID.randomUUID().toString().substring(0, 8);

            loc.setId("Location/" + locId);
            loc.setStatus(Location.LocationStatus.ACTIVE);

            StringBuilder nameBuilder = new StringBuilder();
            if (StringUtils.isNotBlank(poc)) nameBuilder.append(poc);
            if (StringUtils.isNotBlank(room)) nameBuilder.append(" Room ").append(room);
            if (StringUtils.isNotBlank(bed)) nameBuilder.append(" Bed ").append(bed);
            if (StringUtils.isNotBlank(facility)) nameBuilder.append(" (").append(facility).append(")");
            loc.setName(nameBuilder.toString().trim());

            list.add(loc);
        }

        return list;
    }

    private void addPractitioner(Map<String, Practitioner> map, String id, String family, String given, String roleDisplay) {
        if (id != null && id.contains("^")) {
            String[] parts = id.split("\\^");
            id = parts.length > 0 ? parts[0] : null;
            if (parts.length > 1 && StringUtils.isBlank(family)) family = parts[1];
            if (parts.length > 2 && StringUtils.isBlank(given)) given = parts[2];
        }

        if (StringUtils.isBlank(id) && StringUtils.isBlank(family)) {
            return;
        }

        String cleanId = StringUtils.isNotBlank(id) ? cleanId(id) : UUID.randomUUID().toString().substring(0, 8);
        if (map.containsKey(cleanId)) {
            return;
        }

        Practitioner p = new Practitioner();
        p.setId("Practitioner/" + cleanId);
        p.setActive(true);

        if (StringUtils.isNotBlank(id)) {
            Identifier pId = p.addIdentifier();
            pId.setSystem("http://example.org/practitioners");
            pId.setValue(cleanId);
        }

        if (StringUtils.isNotBlank(family) || StringUtils.isNotBlank(given)) {
            HumanName name = p.addName();
            name.setUse(HumanName.NameUse.OFFICIAL);
            if (StringUtils.isNotBlank(family)) name.setFamily(family);
            if (StringUtils.isNotBlank(given)) name.addGiven(given);
            name.setText(buildFullName(given, "", family, "", ""));
        }

        map.put(cleanId, p);
    }
}
