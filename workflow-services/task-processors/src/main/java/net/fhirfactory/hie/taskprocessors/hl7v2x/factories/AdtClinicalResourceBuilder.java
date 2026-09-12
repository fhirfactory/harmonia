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

package net.fhirfactory.hie.taskprocessors.hl7v2x.factories;

import ca.uhn.hl7v2.util.Terser;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.fhirfactory.hie.taskprocessors.hl7v2x.common.Hl7v2ParsingSupport.*;

/**
 * Builds clinical FHIR resources ({@link Condition}, {@link AllergyIntolerance}, {@link Observation}, {@link Coverage}) from HL7 v2 messages.
 */
public class AdtClinicalResourceBuilder {

    /**
     * Builds Condition / Diagnosis resources from DG1 segments.
     */
    public List<Condition> buildConditions(Terser terser, String rawMessage, String patientId, Encounter encounter) {
        List<Condition> list = new ArrayList<>();
        Pattern dg1Pattern = Pattern.compile("^DG1\\|(.*?)$", Pattern.MULTILINE);
        Matcher matcher = dg1Pattern.matcher(rawMessage);
        int idx = 1;
        while (matcher.find()) {
            String dg1Line = matcher.group(1);
            String[] fields = ("DG1|" + dg1Line).split("\\|", -1);

            String diagCodeField = fields.length > 3 ? fields[3] : "";
            String diagDescField = fields.length > 4 ? fields[4] : "";
            String diagDateField = fields.length > 5 ? fields[5] : "";

            Condition condition = new Condition();
            condition.setId("Condition/" + cleanId(patientId) + "-diag-" + idx);
            condition.setSubject(new Reference("Patient/" + cleanId(patientId)));

            if (encounter != null) {
                condition.setEncounter(new Reference("Encounter/" + encounter.getIdPart()));
            }

            CodeableConcept codeConcept = new CodeableConcept();
            if (StringUtils.isNotBlank(diagCodeField)) {
                String[] codeParts = diagCodeField.split("\\^");
                String code = codeParts[0];
                String text = codeParts.length > 1 ? codeParts[1] : (StringUtils.isNotBlank(diagDescField) ? diagDescField : code);
                codeConcept.setText(text);
                codeConcept.addCoding(new Coding("http://hl7.org/fhir/sid/icd-10", code, text));
            } else if (StringUtils.isNotBlank(diagDescField)) {
                codeConcept.setText(diagDescField);
            }
            condition.setCode(codeConcept);

            if (StringUtils.isNotBlank(diagDateField)) {
                Date dDate = parseHl7Date(diagDateField);
                if (dDate != null) {
                    condition.setRecordedDate(dDate);
                }
            }

            condition.setClinicalStatus(new CodeableConcept().addCoding(
                    new Coding("http://terminology.hl7.org/CodeSystem/condition-clinical", "active", "Active")));
            condition.setVerificationStatus(new CodeableConcept().addCoding(
                    new Coding("http://terminology.hl7.org/CodeSystem/condition-ver-status", "confirmed", "Confirmed")));

            list.add(condition);
            idx++;
        }

        return list;
    }

    /**
     * Builds AllergyIntolerance resources from AL1 segments.
     */
    public List<AllergyIntolerance> buildAllergies(Terser terser, String rawMessage, String patientId) {
        List<AllergyIntolerance> list = new ArrayList<>();
        Pattern al1Pattern = Pattern.compile("^AL1\\|(.*?)$", Pattern.MULTILINE);
        Matcher matcher = al1Pattern.matcher(rawMessage);
        int idx = 1;
        while (matcher.find()) {
            String al1Line = matcher.group(1);
            String[] fields = ("AL1|" + al1Line).split("\\|", -1);

            String allergenType = fields.length > 2 ? fields[2] : "";
            String allergenField = fields.length > 3 ? fields[3] : "";
            String reactionField = fields.length > 5 ? fields[5] : "";
            String identDateField = fields.length > 6 ? fields[6] : "";

            AllergyIntolerance allergy = new AllergyIntolerance();
            allergy.setId("AllergyIntolerance/" + cleanId(patientId) + "-all-" + idx);
            allergy.setPatient(new Reference("Patient/" + cleanId(patientId)));

            CodeableConcept codeConcept = new CodeableConcept();
            if (StringUtils.isNotBlank(allergenField)) {
                String[] parts = allergenField.split("\\^");
                String code = parts[0];
                String text = parts.length > 1 ? parts[1] : code;
                codeConcept.setText(text);
                codeConcept.addCoding(new Coding("http://snomed.info/sct", code, text));
            } else if (StringUtils.isNotBlank(allergenType)) {
                codeConcept.setText(allergenType);
            }
            allergy.setCode(codeConcept);

            if (StringUtils.isNotBlank(reactionField)) {
                AllergyIntolerance.AllergyIntoleranceReactionComponent reaction = allergy.addReaction();
                reaction.addManifestation(new CodeableReference(new CodeableConcept().setText(reactionField)));
            }

            if (StringUtils.isNotBlank(identDateField)) {
                Date rDate = parseHl7Date(identDateField);
                if (rDate != null) {
                    allergy.setRecordedDate(rDate);
                }
            }

            list.add(allergy);
            idx++;
        }

        return list;
    }

    /**
     * Builds Observation resources from OBX segments.
     */
    public List<Observation> buildObservations(Terser terser, String rawMessage, String patientId, Encounter encounter) {
        List<Observation> list = new ArrayList<>();
        Pattern obxPattern = Pattern.compile("^OBX\\|(.*?)$", Pattern.MULTILINE);
        Matcher matcher = obxPattern.matcher(rawMessage);
        int idx = 1;
        while (matcher.find()) {
            String obxLine = matcher.group(1);
            String[] fields = ("OBX|" + obxLine).split("\\|", -1);

            String valueType = fields.length > 2 ? fields[2] : "ST";
            String obsCodeField = fields.length > 3 ? fields[3] : "";
            String obsValueField = fields.length > 5 ? fields[5] : "";
            String obsUnitsField = fields.length > 6 ? fields[6] : "";
            String obsDateField = fields.length > 14 ? fields[14] : "";

            Observation obs = new Observation();
            obs.setId("Observation/" + cleanId(patientId) + "-obx-" + idx);
            obs.setSubject(new Reference("Patient/" + cleanId(patientId)));
            obs.setStatus(Enumerations.ObservationStatus.FINAL);

            if (encounter != null) {
                obs.setEncounter(new Reference("Encounter/" + encounter.getIdPart()));
            }

            CodeableConcept codeConcept = new CodeableConcept();
            if (StringUtils.isNotBlank(obsCodeField)) {
                String[] parts = obsCodeField.split("\\^");
                String code = parts[0];
                String text = parts.length > 1 ? parts[1] : code;
                codeConcept.setText(text);
                codeConcept.addCoding(new Coding("http://loinc.org", code, text));
            }
            obs.setCode(codeConcept);

            if (StringUtils.isNotBlank(obsValueField)) {
                if ("NM".equalsIgnoreCase(valueType) && isNumeric(obsValueField)) {
                    Quantity qty = new Quantity();
                    qty.setValue(Double.parseDouble(obsValueField));
                    if (StringUtils.isNotBlank(obsUnitsField)) {
                        qty.setUnit(obsUnitsField);
                    }
                    obs.setValue(qty);
                } else {
                    obs.setValue(new StringType(obsValueField));
                }
            }

            if (StringUtils.isNotBlank(obsDateField)) {
                Date oDate = parseHl7Date(obsDateField);
                if (oDate != null) {
                    obs.setEffective(new DateTimeType(oDate));
                }
            }

            list.add(obs);
            idx++;
        }

        return list;
    }

    /**
     * Builds Coverage resources from IN1 segments.
     */
    public List<Coverage> buildCoverages(Terser terser, String rawMessage, String patientId) {
        List<Coverage> list = new ArrayList<>();
        Pattern in1Pattern = Pattern.compile("^IN1\\|(.*?)$", Pattern.MULTILINE);
        Matcher matcher = in1Pattern.matcher(rawMessage);
        int idx = 1;
        while (matcher.find()) {
            String in1Line = matcher.group(1);
            String[] fields = ("IN1|" + in1Line).split("\\|", -1);

            String companyId = fields.length > 3 ? fields[3] : "";
            String companyName = fields.length > 4 ? fields[4] : "";
            String policyNum = fields.length > 36 ? fields[36] : "";

            Coverage coverage = new Coverage();
            coverage.setId("Coverage/" + cleanId(patientId) + "-cov-" + idx);
            coverage.setStatus(Enumerations.FinancialResourceStatusCodes.ACTIVE);
            coverage.setBeneficiary(new Reference("Patient/" + cleanId(patientId)));

            if (StringUtils.isNotBlank(companyName) || StringUtils.isNotBlank(companyId)) {
                Coverage.CoveragePaymentByComponent paymentBy = coverage.addPaymentBy();
                paymentBy.setParty(new Reference("Organization/" + cleanId(companyId)).setDisplay(
                        StringUtils.isNotBlank(companyName) ? companyName : companyId));
            }

            if (StringUtils.isNotBlank(policyNum)) {
                Identifier pId = coverage.addIdentifier();
                pId.setValue(policyNum);
                pId.setType(new CodeableConcept().setText("Policy Number"));
            }

            list.add(coverage);
            idx++;
        }

        return list;
    }
}
