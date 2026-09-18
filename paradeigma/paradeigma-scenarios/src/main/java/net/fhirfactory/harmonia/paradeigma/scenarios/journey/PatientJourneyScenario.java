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

package net.fhirfactory.harmonia.paradeigma.scenarios.journey;

import net.fhirfactory.harmonia.paradeigma.common.generator.SyntheticPatientGenerator;
import net.fhirfactory.harmonia.paradeigma.common.model.PatientProfile;
import net.fhirfactory.harmonia.paradeigma.common.rest.ManualTriggerResponse;
import net.fhirfactory.harmonia.paradeigma.scenarios.client.SimulatorRestClient;
import net.fhirfactory.harmonia.paradeigma.scenarios.config.ScenarioEngineConfig;
import net.fhirfactory.harmonia.paradeigma.scenarios.model.PatientJourneyResult;
import net.fhirfactory.harmonia.paradeigma.scenarios.model.ScenarioExecutionStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Executes a coherent, multi-system synthetic patient journey across PAS, EMR, LMS, and RIS-PAC.
 */
@Component
public class PatientJourneyScenario {

    private static final Logger log = LoggerFactory.getLogger(PatientJourneyScenario.class);

    private final SimulatorRestClient client;
    private final ScenarioEngineConfig config;
    private final SyntheticPatientGenerator patientGenerator;

    public PatientJourneyScenario(SimulatorRestClient client, ScenarioEngineConfig config) {
        this.client = client;
        this.config = config;
        this.patientGenerator = new SyntheticPatientGenerator(config.getSeed());
    }

    /**
     * Executes the complete 9-step clinical patient journey.
     */
    public PatientJourneyResult executeJourney() {
        String journeyId = "JOURNEY-" + UUID.randomUUID().toString().substring(0, 8);
        PatientProfile patient = patientGenerator.generatePatient();
        String patientId = patient.getPatientId();

        log.info(">>> [Scenario Engine] START Patient Journey [{}] for Patient: {} ({})",
                journeyId, patientId, patient.getFullName());

        PatientJourneyResult result = new PatientJourneyResult(journeyId, patientId);
        long journeyStart = System.currentTimeMillis();

        try {
            // Step 1: Register Patient (PAS -> Harmonia A04)
            ScenarioExecutionStep s1 = executeStep(1, "Patient Registration", "PAS", "A04", () -> client.pasRegisterPatient(patient));
            result.addStep(s1);
            pace();

            // Step 2: Admit Patient (PAS -> Harmonia A01)
            ScenarioExecutionStep s2 = executeStep(2, "Patient Admission", "PAS", "A01", () -> client.pasAdmitPatient(patientId));
            result.addStep(s2);
            pace();

            // Step 3: EMR places Pathology Order (EMR -> Harmonia ORM^O01 CBC)
            ScenarioExecutionStep s3 = executeStep(3, "Pathology Order Placement", "EMR", "O01", () -> client.emrPlaceLabOrder("CBC"));
            result.addStep(s3);
            result.setLabPlacerOrderId(s3.getOrderNumber());
            pace();

            // Step 4: LMS produces Pathology Result (LMS -> Harmonia ORU^R01 CBC)
            ScenarioExecutionStep s4 = executeStep(4, "Pathology Result Ingestion", "LMS", "R01", () -> client.lmsProduceResult(s3.getOrderNumber(), "CBC"));
            result.addStep(s4);
            pace();

            // Step 5: EMR places Diagnostic Imaging Order (EMR -> Harmonia ORM^O01 XR_CHEST)
            ScenarioExecutionStep s5 = executeStep(5, "Diagnostic Imaging Order Placement", "EMR", "O01", () -> client.emrPlaceImagingOrder("XR_CHEST"));
            result.addStep(s5);
            result.setRadPlacerOrderId(s5.getOrderNumber());
            pace();

            // Step 6: RIS-PAC produces Diagnostic Imaging Result (RIS-PAC -> Harmonia ORU^R01 XR_CHEST)
            ScenarioExecutionStep s6 = executeStep(6, "Diagnostic Imaging Result Ingestion", "RISPAC", "R01", () -> client.rispacProduceReport(s5.getOrderNumber(), "XR_CHEST"));
            result.addStep(s6);
            pace();

            // Step 7: Transfer Patient (PAS -> Harmonia A02)
            ScenarioExecutionStep s7 = executeStep(7, "Patient Transfer", "PAS", "A02", () -> client.pasTransferPatient(patientId, "WARD-4B", "401", "A"));
            result.addStep(s7);
            pace();

            // Step 8: Update Patient Demographics (PAS -> Harmonia A08)
            ScenarioExecutionStep s8 = executeStep(8, "Patient Information Update", "PAS", "A08", () -> client.pasUpdatePatient(patientId));
            result.addStep(s8);
            pace();

            // Step 9: Discharge Patient (PAS -> Harmonia A03)
            ScenarioExecutionStep s9 = executeStep(9, "Patient Discharge", "PAS", "A03", () -> client.pasDischargePatient(patientId));
            result.addStep(s9);

            boolean allSuccess = result.getSteps().stream().allMatch(ScenarioExecutionStep::isSuccess);
            result.setSuccess(allSuccess);
            result.setCorrelationVerified(allSuccess);

        } catch (Exception e) {
            log.error("[Scenario Engine] Journey {} failed with error: {}", journeyId, e.getMessage(), e);
            result.setSuccess(false);
            result.setCorrelationVerified(false);
        } finally {
            result.setTotalDurationMs(System.currentTimeMillis() - journeyStart);
            log.info("<<< [Scenario Engine] FINISHED Patient Journey [{}] (Success: {}, Duration: {} ms)",
                    journeyId, result.isSuccess(), result.getTotalDurationMs());
        }

        return result;
    }

    private ScenarioExecutionStep executeStep(int stepNum, String stepName, String systemName, String triggerEvent, StepAction action) {
        ScenarioExecutionStep step = new ScenarioExecutionStep(stepNum, stepName, systemName, triggerEvent);
        long start = System.currentTimeMillis();
        try {
            ManualTriggerResponse resp = action.run();
            step.setDurationMs(System.currentTimeMillis() - start);
            if (resp != null) {
                step.setMessageControlId(resp.getMessageControlId());
                step.setAckCode(resp.getAckCode());
                step.setOrderNumber(resp.getOrderNumber());
                step.setPatientId(resp.getPatientId());
                step.setSuccess(resp.isSuccess() && "AA".equalsIgnoreCase(resp.getAckCode()));
                if (!step.isSuccess()) {
                    step.setErrorMessage(resp.getError() != null ? resp.getError() : "NACK: " + resp.getAckCode());
                }
            } else {
                step.setSuccess(false);
                step.setErrorMessage("Null response from simulator");
            }
        } catch (Exception e) {
            step.setDurationMs(System.currentTimeMillis() - start);
            step.setSuccess(false);
            step.setErrorMessage(e.getMessage());
            log.warn("[Scenario Engine] Step {} [{}] failed: {}", stepNum, stepName, e.getMessage());
        }
        return step;
    }

    private void pace() {
        long delay = config.getStepPacingMs() > 0 ? config.getStepPacingMs() : 50L;
        if (config.getProfile() != null) {
            delay = config.getProfile().getMinSendIntervalMs();
        }
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @FunctionalInterface
    private interface StepAction {
        ManualTriggerResponse run() throws Exception;
    }
}
