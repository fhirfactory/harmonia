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

package net.fhirfactory.harmonia.paradeigma.common.generator;

import net.fhirfactory.harmonia.paradeigma.common.model.OrderProfile;
import net.fhirfactory.harmonia.paradeigma.common.model.OrderType;
import net.fhirfactory.harmonia.paradeigma.common.model.PatientProfile;
import net.fhirfactory.harmonia.paradeigma.common.model.VisitProfile;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Deterministic synthetic clinical order generator for Laboratory (LMS) and Diagnostic Imaging (RIS-PAC) orders.
 */
public class SyntheticOrderGenerator {

    public static final String[][] LAB_TEST_CATALOG = {
            {"CBC", "Complete Blood Count", "LAB"},
            {"ELEC", "Electrolytes, Urea, and Creatinine", "LAB"},
            {"LFT", "Liver Function Panel", "LAB"},
            {"GLU", "Fasting Blood Glucose", "LAB"},
            {"CRP", "C-Reactive Protein (High Sensitivity)", "LAB"},
            {"COAG", "Coagulation Profile (PT/INR, APTT)", "LAB"},
            {"TROP", "Troponin I", "LAB"},
            {"LIPID", "Lipid Profile Panel", "LAB"}
    };

    public static final String[][] IMAGING_STUDY_CATALOG = {
            {"XR_CHEST", "Chest X-Ray PA and Lateral", "RAD"},
            {"CT_HEAD", "Computed Tomography Brain / Head (Non-contrast)", "RAD"},
            {"CT_ABDOMEN", "CT Abdomen and Pelvis with IV Contrast", "RAD"},
            {"MRI_BRAIN", "Magnetic Resonance Imaging Brain with Contrast", "RAD"},
            {"US_ABDOMEN", "Ultrasound Abdomen Complete", "RAD"},
            {"XR_KNEE", "X-Ray Knee 3 Views", "RAD"}
    };

    private final SeedRandom random;
    private final AtomicInteger orderSequence = new AtomicInteger(300000);

    public SyntheticOrderGenerator() {
        this(12345L);
    }

    public SyntheticOrderGenerator(long seed) {
        this.random = new SeedRandom(seed);
    }

    public OrderProfile generateLabOrder(PatientProfile patient, VisitProfile visit) {
        return generateOrder(patient, visit, OrderType.LABORATORY, null);
    }

    public OrderProfile generateLabOrder(PatientProfile patient, VisitProfile visit, String testCode) {
        return generateOrder(patient, visit, OrderType.LABORATORY, testCode);
    }

    public OrderProfile generateImagingOrder(PatientProfile patient, VisitProfile visit) {
        return generateOrder(patient, visit, OrderType.DIAGNOSTIC_IMAGING, null);
    }

    public OrderProfile generateImagingOrder(PatientProfile patient, VisitProfile visit, String studyCode) {
        return generateOrder(patient, visit, OrderType.DIAGNOSTIC_IMAGING, studyCode);
    }

    public OrderProfile generateOrder(PatientProfile patient, VisitProfile visit, OrderType orderType, String specificCode) {
        String patientId = patient != null ? patient.getPatientId() : "PAT-100001";
        String visitNumber = visit != null ? visit.getVisitNumber() : "VIS-200001";
        int seq = orderSequence.incrementAndGet();
        String placerOrderNumber = "ORD-" + seq;

        String code;
        String text;

        if (orderType == OrderType.DIAGNOSTIC_IMAGING) {
            String[] study = findCatalogEntry(IMAGING_STUDY_CATALOG, specificCode);
            if (study == null) {
                study = random.pick(IMAGING_STUDY_CATALOG);
            }
            code = study[0];
            text = study[1];
        } else {
            String[] lab = findCatalogEntry(LAB_TEST_CATALOG, specificCode);
            if (lab == null) {
                lab = random.pick(LAB_TEST_CATALOG);
            }
            code = lab[0];
            text = lab[1];
        }

        OrderProfile order = new OrderProfile(placerOrderNumber, patientId, visitNumber, orderType, code, text);
        order.setOrderDateTime(LocalDateTime.now());
        order.setObservationDateTime(LocalDateTime.now().plusMinutes(random.nextInt(5, 30)));
        order.setPriority(random.nextBoolean(0.2) ? "S" : "R"); // 20% Stat, 80% Routine
        order.setOrderingProviderId(visit != null ? visit.getAttendingDoctorId() : "DOC-101");
        order.setOrderingProviderName(visit != null ? visit.getAttendingDoctorName() : "Dr. Alice Vance");

        return order;
    }

    private String[] findCatalogEntry(String[][] catalog, String code) {
        if (code == null) {
            return null;
        }
        for (String[] entry : catalog) {
            if (entry[0].equalsIgnoreCase(code.trim())) {
                return entry;
            }
        }
        return null;
    }

    public SeedRandom getRandom() {
        return random;
    }
}
