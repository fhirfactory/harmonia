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

import net.fhirfactory.harmonia.agora.api.model.AgoraRoomType;
import net.fhirfactory.harmonia.paradeigma.common.security.SecurityScenarioContext;
import net.fhirfactory.harmonia.paradeigma.scenarios.agora.AgoraCollaborationScenario;
import net.fhirfactory.harmonia.paradeigma.scenarios.agora.AgoraCollaborationScenarioResult;
import net.fhirfactory.harmonia.paradeigma.scenarios.model.ScenarioExpectation;
import net.fhirfactory.harmonia.themis.api.model.ThemisDecision;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@Timeout(value = 10, unit = TimeUnit.SECONDS)
public class AgoraCollaborationScenarioUnitTest {

    @Test
    @DisplayName("Agora Scenario: Full 5-phase collaborative simulation execution succeeds")
    void testDefaultAgoraScenarioSuccess() {
        long seed = 424242L;
        AgoraCollaborationScenario scenario = AgoraCollaborationScenario.createDefault(seed);

        assertThat(scenario.getScenarioId()).startsWith("scen-agora-");
        assertThat(scenario.getName()).isEqualTo("AgoraCollaborationScenario");
        assertThat(scenario.getSeed()).isEqualTo(seed);

        AgoraCollaborationScenarioResult result = scenario.execute();

        assertThat(result).isNotNull();
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getSecurityDecision()).isEqualTo(ThemisDecision.ALLOW);

        // Phase 1: Patient Space
        assertThat(result.getPatientId()).isNotNull();
        assertThat(result.getPatientSpaceId()).startsWith("!space_p_");
        assertThat(result.getPatientChildRoomIds()).hasSize(4);
        assertThat(result.getPatientChildRoomIds()).containsKeys(
                AgoraRoomType.STATISTICS,
                AgoraRoomType.TASKS,
                AgoraRoomType.DISCUSSION,
                AgoraRoomType.DIAGNOSTICS
        );

        // Phase 2: Practitioner Space & Role Room
        assertThat(result.getPractitionerId()).isNotNull();
        assertThat(result.getPractitionerRoleId()).isNotNull();
        assertThat(result.getPractitionerSpaceId()).startsWith("!space_prac_");
        assertThat(result.getPractitionerRoleRoomId()).startsWith("!room_role_");

        // Phase 3 & 4: Messaging & Duplicate Transaction Idempotency
        assertThat(result.getMessageTxnId()).isNotNull();
        assertThat(result.isMessageDelivered()).isTrue();
        assertThat(result.getDuplicateTxnId()).isEqualTo(result.getMessageTxnId());
        assertThat(result.isDuplicateAcknowledged()).isTrue();

        // Phase 5: Membership Drift & Reconciliation
        assertThat(result.isDriftDetected()).isTrue();
        assertThat(result.isDriftResolved()).isTrue();
        assertThat(result.getKickedUserIds()).isNotEmpty();
        assertThat(result.getKickedUserIds()).anyMatch(u -> u.contains("intruder"));
        assertThat(result.getInvitedUserIds()).isNotEmpty();
        assertThat(result.getInvitedUserIds()).anyMatch(u -> u.contains("consultant"));

        // Step assertions
        assertThat(result.getSteps()).hasSize(5);
        assertThat(result.getSteps()).allMatch(s -> s.isSuccess());
    }

    @Test
    @DisplayName("Agora Scenario: Unauthorized caller rejected by Themis default-deny")
    void testAgoraScenarioUnauthorizedSecurityDenied() {
        long seed = 99999L;
        AgoraCollaborationScenario scenario = new AgoraCollaborationScenario(
                "scen-unauth",
                "AgoraUnauthScenario",
                "Tests default-deny",
                seed,
                SecurityScenarioContext.unauthorized(),
                ScenarioExpectation.securityDenied(),
                null,
                null
        );

        AgoraCollaborationScenarioResult result = scenario.execute();

        assertThat(result).isNotNull();
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getSecurityDecision()).isEqualTo(ThemisDecision.DENY);
        assertThat(result.getSteps()).isNotEmpty();
        assertThat(result.getSteps().get(0).getAckCode()).isEqualTo("403");
    }
}
