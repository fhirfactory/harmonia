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

package net.fhirfactory.harmonia.paradeigma.scenarios;

import net.fhirfactory.harmonia.paradeigma.common.model.ExecutionProfile;
import net.fhirfactory.harmonia.paradeigma.common.rest.ManualTriggerResponse;
import net.fhirfactory.harmonia.paradeigma.scenarios.client.SimulatorRestClient;
import net.fhirfactory.harmonia.paradeigma.scenarios.config.ScenarioEngineConfig;
import net.fhirfactory.harmonia.paradeigma.scenarios.engine.ParadeigmaScenarioEngine;
import net.fhirfactory.harmonia.paradeigma.scenarios.journey.PatientJourneyScenario;
import net.fhirfactory.harmonia.paradeigma.scenarios.model.PatientJourneyResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PatientJourneyScenarioTest {

    private SimulatorRestClient mockClient;
    private ScenarioEngineConfig config;
    private PatientJourneyScenario scenario;
    private ParadeigmaScenarioEngine engine;

    @BeforeEach
    void setUp() throws Exception {
        mockClient = mock(SimulatorRestClient.class);
        config = new ScenarioEngineConfig();
        config.setProfile(ExecutionProfile.TEST);
        config.setStepPacingMs(1L);

        when(mockClient.pasRegisterPatient(any())).thenReturn(ManualTriggerResponse.success("MSG-01", "A04", "PAT-101", "AA", "Accept", 5));
        when(mockClient.pasAdmitPatient(anyString())).thenReturn(ManualTriggerResponse.success("MSG-02", "A01", "PAT-101", "AA", "Accept", 5));
        when(mockClient.emrPlaceLabOrder(anyString())).thenReturn(ManualTriggerResponse.success("MSG-03", "O01", "PAT-101", "AA", "Accept", 5));
        when(mockClient.lmsProduceResult(any(), anyString())).thenReturn(ManualTriggerResponse.success("MSG-04", "R01", "PAT-101", "AA", "Accept", 5));
        when(mockClient.emrPlaceImagingOrder(anyString())).thenReturn(ManualTriggerResponse.success("MSG-05", "O01", "PAT-101", "AA", "Accept", 5));
        when(mockClient.rispacProduceReport(any(), anyString())).thenReturn(ManualTriggerResponse.success("MSG-06", "R01", "PAT-101", "AA", "Accept", 5));
        when(mockClient.pasTransferPatient(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(ManualTriggerResponse.success("MSG-07", "A02", "PAT-101", "AA", "Accept", 5));
        when(mockClient.pasUpdatePatient(anyString())).thenReturn(ManualTriggerResponse.success("MSG-08", "A08", "PAT-101", "AA", "Accept", 5));
        when(mockClient.pasDischargePatient(anyString())).thenReturn(ManualTriggerResponse.success("MSG-09", "A03", "PAT-101", "AA", "Accept", 5));

        scenario = new PatientJourneyScenario(mockClient, config);
        engine = new ParadeigmaScenarioEngine(config, scenario);
    }

    @Test
    @DisplayName("Single patient journey executes all 9 clinical steps with correlation verified")
    void testSinglePatientJourney() {
        PatientJourneyResult result = scenario.executeJourney();
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.isCorrelationVerified()).isTrue();
        assertThat(result.getSteps()).hasSize(9);

        assertThat(result.getSteps().get(0).getTriggerEvent()).isEqualTo("A04");
        assertThat(result.getSteps().get(1).getTriggerEvent()).isEqualTo("A01");
        assertThat(result.getSteps().get(2).getTriggerEvent()).isEqualTo("O01");
        assertThat(result.getSteps().get(3).getTriggerEvent()).isEqualTo("R01");
        assertThat(result.getSteps().get(4).getTriggerEvent()).isEqualTo("O01");
        assertThat(result.getSteps().get(5).getTriggerEvent()).isEqualTo("R01");
        assertThat(result.getSteps().get(6).getTriggerEvent()).isEqualTo("A02");
        assertThat(result.getSteps().get(7).getTriggerEvent()).isEqualTo("A08");
        assertThat(result.getSteps().get(8).getTriggerEvent()).isEqualTo("A03");
    }

    @Test
    @DisplayName("Scenario engine executes concurrent patient journeys")
    void testConcurrentJourneys() {
        List<PatientJourneyResult> results = engine.runConcurrentJourneys(3, ExecutionProfile.TEST);
        assertThat(results).hasSize(3);
        assertThat(results).allMatch(PatientJourneyResult::isSuccess);
        assertThat(engine.getTotalJourneys()).isEqualTo(3);
        assertThat(engine.getSuccessfulJourneys()).isEqualTo(3);
    }
}
