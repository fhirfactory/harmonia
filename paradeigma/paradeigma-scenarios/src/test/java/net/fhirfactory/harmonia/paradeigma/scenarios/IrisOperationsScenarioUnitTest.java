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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package net.fhirfactory.harmonia.paradeigma.scenarios;

import net.fhirfactory.harmonia.paradeigma.common.security.SecurityScenarioContext;
import net.fhirfactory.harmonia.paradeigma.scenarios.model.ScenarioExpectation;
import net.fhirfactory.harmonia.paradeigma.scenarios.operations.IrisOperationsScenario;
import net.fhirfactory.harmonia.paradeigma.scenarios.operations.IrisOperationsScenarioProfile;
import net.fhirfactory.harmonia.paradeigma.scenarios.operations.IrisOperationsScenarioResult;
import net.fhirfactory.harmonia.themis.api.model.ThemisDecision;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@Timeout(value = 10, unit = TimeUnit.SECONDS)
public class IrisOperationsScenarioUnitTest {

    @Test
    @DisplayName("Scenario: Nominal ALL_HEALTHY profile succeeds across all 6 operational phases")
    void testAllHealthyProfile() {
        long seed = 10001L;
        IrisOperationsScenario scenario = IrisOperationsScenario.createDefault(seed);

        assertThat(scenario.getScenarioId()).startsWith("scen-ops-");
        assertThat(scenario.getProfile()).isEqualTo(IrisOperationsScenarioProfile.ALL_HEALTHY);

        IrisOperationsScenarioResult result = scenario.execute();

        assertThat(result).isNotNull();
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getSecurityDecision()).isEqualTo(ThemisDecision.ALLOW);
        assertThat(result.getPlatformStatus()).isEqualTo("HEALTHY");
        assertThat(result.getTotalSubsystems()).isEqualTo(9);
        assertThat(result.getDegradedSubsystems()).isEqualTo(0);
        assertThat(result.getTotalDlqDepth()).isEqualTo(0);
        assertThat(result.getFailedWorkCount()).isEqualTo(0);
        assertThat(result.getInspectedPragmaStatus()).isEqualTo("COMPLETED");
        assertThat(result.getEventHopCount()).isGreaterThanOrEqualTo(4);
        assertThat(result.getCriticalAlertCount()).isEqualTo(0);

        assertThat(result.getSteps()).hasSize(6);
        assertThat(result.getSteps()).allMatch(s -> s.isSuccess());
    }

    @Test
    @DisplayName("Scenario: PETASOS_DEGRADED profile isolates broker degradation")
    void testPetasosDegradedProfile() {
        long seed = 20002L;
        IrisOperationsScenario scenario = IrisOperationsScenario.createForProfile(IrisOperationsScenarioProfile.PETASOS_DEGRADED, seed);

        IrisOperationsScenarioResult result = scenario.execute();

        assertThat(result).isNotNull();
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getPlatformStatus()).isEqualTo("DEGRADED");
        assertThat(result.getDegradedSubsystems()).isEqualTo(1);
        assertThat(result.getWarningAlertCount()).isGreaterThanOrEqualTo(1);

        assertThat(result.getSteps()).hasSize(6);
        assertThat(result.getSteps()).allMatch(s -> s.isSuccess());
    }

    @Test
    @DisplayName("Scenario: DLQ_GROWTH profile detects message dead-letter accumulation")
    void testDlqGrowthProfile() {
        long seed = 30003L;
        IrisOperationsScenario scenario = IrisOperationsScenario.createForProfile(IrisOperationsScenarioProfile.DLQ_GROWTH, seed);

        IrisOperationsScenarioResult result = scenario.execute();

        assertThat(result).isNotNull();
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getTotalDlqDepth()).isGreaterThan(0);
        assertThat(result.getWarningAlertCount()).isGreaterThanOrEqualTo(1);

        assertThat(result.getSteps()).hasSize(6);
        assertThat(result.getSteps()).allMatch(s -> s.isSuccess());
    }

    @Test
    @DisplayName("Scenario: FAILED_PRAGMA profile pinpoints workflow Ergon checkpoint failure")
    void testFailedPragmaProfile() {
        long seed = 40004L;
        IrisOperationsScenario scenario = IrisOperationsScenario.createForProfile(IrisOperationsScenarioProfile.FAILED_PRAGMA, seed);

        IrisOperationsScenarioResult result = scenario.execute();

        assertThat(result).isNotNull();
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getFailedWorkCount()).isGreaterThan(0);
        assertThat(result.getInspectedPragmaStatus()).isEqualTo("FAILED");
        assertThat(result.getFailedErgon()).isEqualTo("FhirValidationErgon");

        assertThat(result.getSteps()).hasSize(6);
        assertThat(result.getSteps()).allMatch(s -> s.isSuccess());
    }

    @Test
    @DisplayName("Scenario: CRITICAL_ALERT profile validates operator guidance and acknowledge action")
    void testCriticalAlertProfile() {
        long seed = 50005L;
        IrisOperationsScenario scenario = IrisOperationsScenario.createForProfile(IrisOperationsScenarioProfile.CRITICAL_ALERT, seed);

        IrisOperationsScenarioResult result = scenario.execute();

        assertThat(result).isNotNull();
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getCriticalAlertCount()).isEqualTo(1);
        assertThat(result.getAcknowledgedAlertId()).isNotNull();
        assertThat(result.getAcknowledgedAlertStatus()).isEqualTo("ACKNOWLEDGED");

        assertThat(result.getSteps()).hasSize(6);
        assertThat(result.getSteps()).allMatch(s -> s.isSuccess());
    }

    @Test
    @DisplayName("Scenario: Unauthorized caller rejected by Themis default-deny governance")
    void testUnauthorizedOperationsCallerRejected() {
        long seed = 99999L;
        IrisOperationsScenario scenario = new IrisOperationsScenario(
                "scen-ops-deny",
                "DeniedOperationsScenario",
                "Verifies default-deny on operations REST API",
                seed,
                SecurityScenarioContext.unauthorized(),
                ScenarioExpectation.securityDenied(),
                null,
                IrisOperationsScenarioProfile.ALL_HEALTHY
        );

        IrisOperationsScenarioResult result = scenario.execute();

        assertThat(result).isNotNull();
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getSecurityDecision()).isEqualTo(ThemisDecision.DENY);
        assertThat(result.getSteps()).isNotEmpty();
        assertThat(result.getSteps().get(0).getAckCode()).isEqualTo("403");
    }
}
