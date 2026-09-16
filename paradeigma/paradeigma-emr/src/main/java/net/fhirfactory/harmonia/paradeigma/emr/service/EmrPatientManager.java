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

package net.fhirfactory.harmonia.paradeigma.emr.service;

import net.fhirfactory.harmonia.paradeigma.common.generator.SyntheticOrderGenerator;
import net.fhirfactory.harmonia.paradeigma.common.generator.SyntheticPatientGenerator;
import net.fhirfactory.harmonia.paradeigma.common.hl7.Hl7Parsers;
import net.fhirfactory.harmonia.paradeigma.common.model.Gender;
import net.fhirfactory.harmonia.paradeigma.common.model.PatientProfile;
import net.fhirfactory.harmonia.paradeigma.common.model.VisitProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages patients and visits received via ADT fan-out in EMR.
 */
@Service
public class EmrPatientManager {

    private static final Logger log = LoggerFactory.getLogger(EmrPatientManager.class);

    private final SyntheticPatientGenerator defaultGenerator;
    private final SyntheticOrderGenerator orderGenerator;
    private final Map<String, PatientProfile> patients = new ConcurrentHashMap<>();
    private final Map<String, VisitProfile> visits = new ConcurrentHashMap<>();

    public EmrPatientManager() {
        this.defaultGenerator = new SyntheticPatientGenerator(12345L);
        this.orderGenerator = new SyntheticOrderGenerator(12345L);
    }

    public EmrPatientManager(long seed) {
        this.defaultGenerator = new SyntheticPatientGenerator(seed);
        this.orderGenerator = new SyntheticOrderGenerator(seed);
    }

    /**
     * Ingests an ADT message received over MLLP from Harmonia and updates internal patient/visit state.
     */
    public void recordAdtMessage(String rawHl7) {
        if (rawHl7 == null || rawHl7.isBlank()) return;
        try {
            String rawPatientId = extractField(rawHl7, "PID", 3);
            String patientId = (rawPatientId != null && !rawPatientId.isBlank()) ? rawPatientId.split("\\^")[0].trim() : "PAT-UNKNOWN";
            String patientName = extractField(rawHl7, "PID", 5);
            String dobStr = extractField(rawHl7, "PID", 7);
            String genderStr = extractField(rawHl7, "PID", 8);

            String visitNumber = extractField(rawHl7, "PV1", 19);
            String location = extractField(rawHl7, "PV1", 3);

            PatientProfile p = patients.computeIfAbsent(patientId, id -> {
                PatientProfile profile = new PatientProfile();
                profile.setPatientId(id);
                return profile;
            });

            if (patientName != null) {
                String[] nameParts = patientName.split("\\^");
                if (nameParts.length > 0) p.setFamilyName(nameParts[0]);
                if (nameParts.length > 1) p.setGivenName(nameParts[1]);
            }
            if (genderStr != null) p.setGender(Gender.fromHl7Code(genderStr));

            if (visitNumber != null && !visitNumber.isBlank()) {
                VisitProfile v = visits.computeIfAbsent(patientId, id -> new VisitProfile(visitNumber, id, "I", location, "101", "A"));
                v.setVisitNumber(visitNumber);
                if (location != null) {
                    String[] locParts = location.split("\\^");
                    if (locParts.length > 0) v.setPointOfCare(locParts[0]);
                    if (locParts.length > 1) v.setRoom(locParts[1]);
                    if (locParts.length > 2) v.setBed(locParts[2]);
                }
            }

            log.info("[EMR] Recorded ADT patient {} ({}) in local EMR registry", patientId, p.getFullName());
        } catch (Exception e) {
            log.error("[EMR] Failed to record ADT message: {}", e.getMessage());
        }
    }

    public PatientProfile getAnyPatient() {
        if (!patients.isEmpty()) {
            return patients.values().iterator().next();
        }
        PatientProfile p = defaultGenerator.generatePatient();
        patients.put(p.getPatientId(), p);
        return p;
    }

    public VisitProfile getVisitForPatient(String patientId) {
        return visits.computeIfAbsent(patientId, id -> defaultGenerator.generateVisit(patients.get(id)));
    }

    public List<PatientProfile> getAllPatients() {
        return new ArrayList<>(patients.values());
    }

    public SyntheticOrderGenerator getOrderGenerator() {
        return orderGenerator;
    }

    private String extractField(String rawHl7, String segmentName, int fieldIndex) {
        String[] lines = rawHl7.split("\r\n|\r|\n");
        for (String line : lines) {
            if (line.startsWith(segmentName + "|")) {
                String[] fields = line.split("\\|", -1);
                int idx = "MSH".equals(segmentName) ? fieldIndex - 1 : fieldIndex;
                if (idx < fields.length) {
                    return fields[idx].trim();
                }
            }
        }
        return null;
    }
}
