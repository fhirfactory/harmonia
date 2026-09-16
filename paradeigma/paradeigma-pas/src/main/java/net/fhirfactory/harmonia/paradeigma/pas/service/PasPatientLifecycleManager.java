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

package net.fhirfactory.harmonia.paradeigma.pas.service;

import net.fhirfactory.harmonia.paradeigma.common.generator.SyntheticPatientGenerator;
import net.fhirfactory.harmonia.paradeigma.common.hl7.Hl7MessageBuilders;
import net.fhirfactory.harmonia.paradeigma.common.model.PatientProfile;
import net.fhirfactory.harmonia.paradeigma.common.model.VisitProfile;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages stateful patient lifecycle states across the simulated PAS.
 * Generates coherent clinical transition sequences: A04 -> A01 -> A02 -> A08 -> A03.
 */
@Service
public class PasPatientLifecycleManager {

    private final SyntheticPatientGenerator generator;
    private final Map<String, PatientProfile> registeredPatients = new ConcurrentHashMap<>();
    private final Map<String, PatientProfile> admittedPatients = new ConcurrentHashMap<>();
    private final Map<String, VisitProfile> activeVisits = new ConcurrentHashMap<>();
    private final Map<String, PatientProfile> dischargedPatients = new ConcurrentHashMap<>();

    public PasPatientLifecycleManager() {
        this.generator = new SyntheticPatientGenerator(12345L);
    }

    public PasPatientLifecycleManager(long seed) {
        this.generator = new SyntheticPatientGenerator(seed);
    }

    /**
     * Step 1: Register a new patient (A04).
     */
    public String registerPatient(PatientProfile customPatient) {
        PatientProfile patient = (customPatient != null) ? customPatient : generator.generatePatient();
        VisitProfile visit = generator.generateVisit(patient);
        registeredPatients.put(patient.getPatientId(), patient);
        activeVisits.put(patient.getPatientId(), visit);

        return Hl7MessageBuilders.buildAdtA04(patient, visit);
    }

    /**
     * Step 2: Admit a registered patient (A01).
     */
    public String admitPatient(String patientId) {
        PatientProfile patient = registeredPatients.remove(patientId);
        if (patient == null) {
            patient = admittedPatients.get(patientId);
            if (patient == null) {
                patient = generator.generatePatient(patientId != null ? patientId : "PAT-" + System.currentTimeMillis() % 1000000);
            }
        }
        final PatientProfile targetPatient = patient;
        admittedPatients.put(targetPatient.getPatientId(), targetPatient);
        VisitProfile visit = activeVisits.computeIfAbsent(targetPatient.getPatientId(), id -> generator.generateVisit(targetPatient));

        return Hl7MessageBuilders.buildAdtA01(targetPatient, visit);
    }

    /**
     * Step 3: Transfer an admitted patient (A02).
     */
    public String transferPatient(String patientId, String newWard, String newRoom, String newBed) {
        PatientProfile patient = admittedPatients.get(patientId);
        if (patient == null) {
            patient = generator.generatePatient(patientId);
            admittedPatients.put(patient.getPatientId(), patient);
        }
        final PatientProfile targetPatient = patient;
        VisitProfile visit = activeVisits.computeIfAbsent(targetPatient.getPatientId(), id -> generator.generateVisit(targetPatient));
        String ward = newWard != null ? newWard : "ICU-EAST";

        return Hl7MessageBuilders.buildAdtA02(targetPatient, visit, ward, newRoom, newBed);
    }

    /**
     * Step 4: Update patient demographics (A08).
     */
    public String updatePatient(String patientId) {
        PatientProfile patient = admittedPatients.get(patientId);
        if (patient == null) {
            patient = registeredPatients.get(patientId);
            if (patient == null) {
                patient = generator.generatePatient(patientId);
                admittedPatients.put(patient.getPatientId(), patient);
            }
        }
        VisitProfile visit = activeVisits.get(patient.getPatientId());
        patient.setPhoneNumber("(555) 019-" + (1000 + (int)(Math.random() * 8999)));

        return Hl7MessageBuilders.buildAdtA08(patient, visit);
    }

    /**
     * Step 5: Discharge an admitted patient (A03).
     */
    public String dischargePatient(String patientId) {
        PatientProfile patient = admittedPatients.remove(patientId);
        if (patient == null) {
            patient = generator.generatePatient(patientId);
        }
        dischargedPatients.put(patient.getPatientId(), patient);
        VisitProfile visit = activeVisits.remove(patient.getPatientId());

        return Hl7MessageBuilders.buildAdtA03(patient, visit);
    }

    /**
     * Advances the next logical lifecycle step for automated simulations.
     */
    public String nextLifecycleEvent() {
        if (!registeredPatients.isEmpty()) {
            String nextId = registeredPatients.keySet().iterator().next();
            return admitPatient(nextId);
        }
        if (!admittedPatients.isEmpty() && Math.random() < 0.4) {
            String nextId = admittedPatients.keySet().iterator().next();
            return transferPatient(nextId, "WARD-4B", "402", "A");
        }
        if (!admittedPatients.isEmpty() && Math.random() < 0.3) {
            String nextId = admittedPatients.keySet().iterator().next();
            return dischargePatient(nextId);
        }
        return registerPatient(null);
    }

    public Map<String, PatientProfile> getRegisteredPatients() {
        return registeredPatients;
    }

    public Map<String, PatientProfile> getAdmittedPatients() {
        return admittedPatients;
    }

    public Map<String, PatientProfile> getDischargedPatients() {
        return dischargedPatients;
    }

    public Map<String, VisitProfile> getActiveVisits() {
        return activeVisits;
    }

    public SyntheticPatientGenerator getGenerator() {
        return generator;
    }
}
