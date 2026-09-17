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

package net.fhirfactory.harmonia.agora.api;

import net.fhirfactory.harmonia.agora.api.model.AgoraMembershipAction;
import net.fhirfactory.harmonia.agora.api.model.AgoraRoomType;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Agora Request Models API Unit Tests")
@Timeout(10)
class AgoraRequestModelsTest {

    @Test
    @DisplayName("Should verify AgoraRoomType properties")
    void testRoomTypes() {
        assertThat(AgoraRoomType.STATISTICS.isChildRoom()).isTrue();
        assertThat(AgoraRoomType.TASKS.isChildRoom()).isTrue();
        assertThat(AgoraRoomType.DISCUSSION.isChildRoom()).isTrue();
        assertThat(AgoraRoomType.DIAGNOSTICS.isChildRoom()).isTrue();
        assertThat(AgoraRoomType.PRACTITIONER_ROLE.isChildRoom()).isTrue();
        assertThat(AgoraRoomType.GROUP.isChildRoom()).isFalse();
        assertThat(AgoraRoomType.SPACE.isChildRoom()).isFalse();

        assertThat(AgoraRoomType.STATISTICS.getSuffix()).isEqualTo("statistics");
        assertThat(AgoraRoomType.TASKS.getDisplayName()).isEqualTo("Patient Tasks");
    }

    @Test
    @DisplayName("Should verify AgoraMembershipAction enum values")
    void testMembershipActions() {
        assertThat(AgoraMembershipAction.valueOf("INVITE")).isEqualTo(AgoraMembershipAction.INVITE);
        assertThat(AgoraMembershipAction.valueOf("KICK")).isEqualTo(AgoraMembershipAction.KICK);
    }

    @Test
    @DisplayName("Should build and verify AgoraSpaceRequest")
    void testSpaceRequest() {
        ThemisSecurityContext secContext = ThemisSecurityContext.fromPrincipal(
                ThemisPrincipal.human("user:coordinator"), "corr-space-1");

        net.fhirfactory.harmonia.agora.api.model.AgoraSpaceRequest request =
                net.fhirfactory.harmonia.agora.api.model.AgoraSpaceRequest.builder()
                        .harmoniaResourceType("Patient")
                        .harmoniaResourceId("pat-uuid-001")
                        .displayName("Patient Space [pat-uuid-001]")
                        .topic("Care Coordination Space")
                        .correlationId("corr-space-1")
                        .securityContext(secContext)
                        .initialMembers(List.of("@doctor:synapse", "@nurse:synapse"))
                        .addMetadata("facility", "Hospital-Main")
                        .build();

        assertThat(request.getHarmoniaResourceType()).isEqualTo("Patient");
        assertThat(request.getHarmoniaResourceId()).isEqualTo("pat-uuid-001");
        assertThat(request.getDisplayName()).isEqualTo("Patient Space [pat-uuid-001]");
        assertThat(request.getInitialMembers()).containsExactly("@doctor:synapse", "@nurse:synapse");
        assertThat(request.getMetadata()).containsEntry("facility", "Hospital-Main");
    }

    @Test
    @DisplayName("Should build and verify AgoraRoomRequest")
    void testRoomRequest() {
        net.fhirfactory.harmonia.agora.api.model.AgoraRoomRequest request =
                net.fhirfactory.harmonia.agora.api.model.AgoraRoomRequest.builder()
                        .spaceId("!space123:synapse")
                        .roomType(AgoraRoomType.DIAGNOSTICS)
                        .harmoniaResourceType("Patient")
                        .harmoniaResourceId("pat-uuid-001")
                        .displayName("Diagnostics")
                        .topic("Lab and Imaging Results")
                        .correlationId("corr-room-1")
                        .build();

        assertThat(request.getSpaceId()).isEqualTo("!space123:synapse");
        assertThat(request.getRoomType()).isEqualTo(AgoraRoomType.DIAGNOSTICS);
        assertThat(request.getDisplayName()).isEqualTo("Diagnostics");
    }

    @Test
    @DisplayName("Should build and verify AgoraMembershipRequest")
    void testMembershipRequest() {
        net.fhirfactory.harmonia.agora.api.model.AgoraMembershipRequest request =
                net.fhirfactory.harmonia.agora.api.model.AgoraMembershipRequest.builder()
                        .roomId("!room123:synapse")
                        .userId("@specialist:synapse")
                        .action(AgoraMembershipAction.INVITE)
                        .reason("Clinical consult required")
                        .correlationId("corr-mem-1")
                        .build();

        assertThat(request.getRoomId()).isEqualTo("!room123:synapse");
        assertThat(request.getUserId()).isEqualTo("@specialist:synapse");
        assertThat(request.getAction()).isEqualTo(AgoraMembershipAction.INVITE);
        assertThat(request.getReason()).isEqualTo("Clinical consult required");
    }

    @Test
    @DisplayName("Should build and verify AgoraPatientSpaceResponse")
    void testPatientSpaceResponse() {
        java.util.Map<AgoraRoomType, String> rooms = java.util.Map.of(
                AgoraRoomType.STATISTICS, "!stat:synapse",
                AgoraRoomType.TASKS, "!task:synapse",
                AgoraRoomType.DISCUSSION, "!disc:synapse",
                AgoraRoomType.DIAGNOSTICS, "!diag:synapse"
        );
        net.fhirfactory.harmonia.agora.api.model.AgoraPatientSpaceResponse response =
                new net.fhirfactory.harmonia.agora.api.model.AgoraPatientSpaceResponse("!space:synapse", "pat-123", rooms);

        assertThat(response.getSpaceId()).isEqualTo("!space:synapse");
        assertThat(response.getPatientId()).isEqualTo("pat-123");
        assertThat(response.getStatisticsRoomId()).isEqualTo("!stat:synapse");
        assertThat(response.getTasksRoomId()).isEqualTo("!task:synapse");
        assertThat(response.getDiscussionRoomId()).isEqualTo("!disc:synapse");
        assertThat(response.getDiagnosticsRoomId()).isEqualTo("!diag:synapse");
        assertThat(response.getChildRoomId(AgoraRoomType.TASKS)).isEqualTo("!task:synapse");
    }

    @Test
    @DisplayName("Should build and verify AgoraReconciliationResult")
    void testReconciliationResult() {
        net.fhirfactory.harmonia.agora.api.model.AgoraReconciliationResult result =
                net.fhirfactory.harmonia.agora.api.model.AgoraReconciliationResult.builder("!room:synapse")
                        .invitedUsers(List.of("@doc1:synapse"))
                        .kickedUsers(List.of("@intruder:synapse"))
                        .retainedUsers(List.of("@doc2:synapse"))
                        .deniedUsers(List.of("@intruder:synapse"))
                        .build();

        assertThat(result.getRoomId()).isEqualTo("!room:synapse");
        assertThat(result.getInvitedUsers()).containsExactly("@doc1:synapse");
        assertThat(result.getKickedUsers()).containsExactly("@intruder:synapse");
        assertThat(result.getRetainedUsers()).containsExactly("@doc2:synapse");
        assertThat(result.getDeniedUsers()).containsExactly("@intruder:synapse");
        assertThat(result.hasDrift()).isTrue();

        net.fhirfactory.harmonia.agora.api.model.AgoraReconciliationResult noDrift =
                net.fhirfactory.harmonia.agora.api.model.AgoraReconciliationResult.builder("!room:synapse")
                        .retainedUsers(List.of("@doc2:synapse"))
                        .build();
        assertThat(noDrift.hasDrift()).isFalse();
    }
}
