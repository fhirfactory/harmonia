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

import net.fhirfactory.harmonia.paradeigma.common.model.ObservationItem;
import net.fhirfactory.harmonia.paradeigma.common.model.OrderProfile;
import net.fhirfactory.harmonia.paradeigma.common.model.OrderType;
import net.fhirfactory.harmonia.paradeigma.common.model.ResultProfile;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Deterministic synthetic result generator for Laboratory (LMS) and Diagnostic Imaging (RIS-PAC) observations.
 */
public class SyntheticResultGenerator {

    private final SeedRandom random;
    private final AtomicInteger fillerSequence = new AtomicInteger(400000);

    public SyntheticResultGenerator() {
        this(12345L);
    }

    public SyntheticResultGenerator(long seed) {
        this.random = new SeedRandom(seed);
    }

    public ResultProfile generateResult(OrderProfile order) {
        int seq = fillerSequence.incrementAndGet();
        String fillerOrderNumber = (order.getOrderType() == OrderType.DIAGNOSTIC_IMAGING ? "RIS-" : "LMS-") + seq;

        ResultProfile result = new ResultProfile(
                order.getPlacerOrderNumber(),
                fillerOrderNumber,
                order.getPatientId(),
                order.getVisitNumber(),
                order.getOrderType(),
                order.getUniversalServiceId(),
                order.getUniversalServiceText()
        );

        if (order.getOrderType() == OrderType.DIAGNOSTIC_IMAGING) {
            populateImagingResult(result, order.getUniversalServiceId());
        } else {
            populateLabResult(result, order.getUniversalServiceId());
        }

        return result;
    }

    private void populateLabResult(ResultProfile result, String code) {
        result.setPerformingTechnician("TECH-201^Alex Johnson");
        result.setReviewingDoctor("DOC-PATH-101^Dr. Sarah Connor");

        String upperCode = code != null ? code.toUpperCase() : "CBC";

        if (upperCode.contains("CBC") || upperCode.contains("FBC")) {
            // Full Blood Count
            double hb = 130.0 + random.nextInt(-20, 30);
            result.addObservation(createObservation(1, "718-7", "Haemoglobin", String.format(Locale.US, "%.1f", hb), "g/L", "130-180", hb < 130 ? "L" : (hb > 180 ? "H" : "N")));

            double wbc = 4.0 + random.nextDouble() * 7.0;
            result.addObservation(createObservation(2, "6690-2", "White Blood Cell Count", String.format(Locale.US, "%.1f", wbc), "10^9/L", "4.0-11.0", "N"));

            int plt = random.nextInt(150, 400);
            result.addObservation(createObservation(3, "777-3", "Platelet Count", String.valueOf(plt), "10^9/L", "150-400", "N"));

            double hct = 0.40 + random.nextDouble() * 0.12;
            result.addObservation(createObservation(4, "4544-3", "Haematocrit", String.format(Locale.US, "%.2f", hct), "L/L", "0.40-0.52", "N"));

        } else if (upperCode.contains("ELEC") || upperCode.contains("EUC") || upperCode.contains("CHEM")) {
            // Electrolytes & Creatinine
            int na = random.nextInt(135, 145);
            result.addObservation(createObservation(1, "2951-2", "Sodium", String.valueOf(na), "mmol/L", "135-145", "N"));

            double k = 3.5 + random.nextDouble() * 1.5;
            result.addObservation(createObservation(2, "2823-3", "Potassium", String.format(Locale.US, "%.1f", k), "mmol/L", "3.5-5.0", "N"));

            int cr = random.nextInt(60, 110);
            result.addObservation(createObservation(3, "2160-0", "Creatinine", String.valueOf(cr), "umol/L", "60-110", "N"));

            double urea = 3.0 + random.nextDouble() * 5.0;
            result.addObservation(createObservation(4, "3094-0", "Urea", String.format(Locale.US, "%.1f", urea), "mmol/L", "3.0-8.0", "N"));

            int egfr = random.nextInt(60, 95);
            result.addObservation(createObservation(5, "33914-3", "eGFR", ">" + egfr, "mL/min/1.73m2", ">60", "N"));

        } else if (upperCode.contains("LFT")) {
            // Liver Function Tests
            int alt = random.nextInt(10, 45);
            result.addObservation(createObservation(1, "1742-6", "Alanine Aminotransferase (ALT)", String.valueOf(alt), "U/L", "10-40", alt > 40 ? "H" : "N"));

            int ast = random.nextInt(10, 38);
            result.addObservation(createObservation(2, "1920-8", "Aspartate Aminotransferase (AST)", String.valueOf(ast), "U/L", "10-35", ast > 35 ? "H" : "N"));

            int bili = random.nextInt(3, 20);
            result.addObservation(createObservation(3, "1975-2", "Total Bilirubin", String.valueOf(bili), "umol/L", "3-20", "N"));

            int alp = random.nextInt(30, 120);
            result.addObservation(createObservation(4, "6768-6", "Alkaline Phosphatase (ALP)", String.valueOf(alp), "U/L", "30-120", "N"));

        } else if (upperCode.contains("GLU")) {
            // Blood Glucose
            double glu = 4.0 + random.nextDouble() * 2.5;
            result.addObservation(createObservation(1, "2345-7", "Fasting Glucose", String.format(Locale.US, "%.1f", glu), "mmol/L", "3.9-6.0", "N"));

        } else if (upperCode.contains("CRP")) {
            // CRP
            double crp = 1.0 + random.nextDouble() * 4.0;
            result.addObservation(createObservation(1, "1988-5", "C-Reactive Protein", String.format(Locale.US, "%.1f", crp), "mg/L", "<5.0", "N"));

        } else {
            // Default generic lab test
            result.addObservation(createObservation(1, upperCode, "Standard Test Result", "100.0", "units", "50-150", "N"));
        }
    }

    private void populateImagingResult(ResultProfile result, String code) {
        result.setPerformingTechnician("RADTECH-301^Sam Taylor");
        result.setReviewingDoctor("DOC-RAD-201^Dr. Fiona Gallagher");

        String upperCode = code != null ? code.toUpperCase() : "XR_CHEST";

        String narrative;
        if (upperCode.contains("CHEST") || upperCode.contains("XR")) {
            narrative = "CLINICAL INDICATION: Evaluation for infiltrate or consolidation.\n" +
                    "FINDINGS: The lungs are clear bilaterally. There is no focal airspace consolidation, pneumothorax, or pleural effusion. " +
                    "The cardiomediastinal silhouette and hilar contours are within normal limits. Normal osseous structures.\n" +
                    "IMPRESSION: Normal radiograph of the chest. No acute cardiopulmonary abnormality.";
        } else if (upperCode.contains("CT_HEAD") || upperCode.contains("BRAIN")) {
            narrative = "CLINICAL INDICATION: Headache, neurological evaluation.\n" +
                    "FINDINGS: Non-contrast axial CT images through the brain show no evidence of acute intracranial hemorrhage, midline shift, or mass effect. " +
                    "Ventricles and sulci are normal for age. Gray-white differentiation is preserved.\n" +
                    "IMPRESSION: Normal CT scan of the brain. No acute intracranial pathology.";
        } else if (upperCode.contains("ABDOMEN")) {
            narrative = "CLINICAL INDICATION: Abdominal pain evaluation.\n" +
                    "FINDINGS: Liver, gallbladder, spleen, pancreas, and adrenal glands demonstrate normal size, contour, and attenuation. " +
                    "No free intra-abdominal fluid or free air. Kidneys show symmetric enhancement without hydronephrosis.\n" +
                    "IMPRESSION: Normal abdominal examination. No acute inflammatory or obstructive process identified.";
        } else {
            narrative = "CLINICAL INDICATION: Routine imaging examination.\n" +
                    "FINDINGS: Visualized anatomical structures demonstrate normal architecture and signal characteristics. No focal lesion identified.\n" +
                    "IMPRESSION: Normal diagnostic imaging study.";
        }

        result.setDiagnosticReportNarrative(narrative);

        // Add a text observation item for the narrative report
        ObservationItem textObs = new ObservationItem(1, "TX", upperCode, result.getUniversalServiceText(), narrative, null, null, "N");
        result.addObservation(textObs);
    }

    private ObservationItem createObservation(int subId, String code, String name, String value, String units, String range, String flag) {
        return new ObservationItem(subId, "NM", code, name, value, units, range, flag);
    }

    public SeedRandom getRandom() {
        return random;
    }
}
