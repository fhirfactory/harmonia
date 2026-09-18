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

package net.fhirfactory.harmonia.agora.core.lifecycle;

import net.fhirfactory.harmonia.agora.api.model.*;
import net.fhirfactory.harmonia.agora.core.AgoraCoreTestApplication;
import net.fhirfactory.harmonia.agora.core.persistence.AgoraMappingEntity;
import net.fhirfactory.harmonia.agora.core.persistence.AgoraMappingRepository;
import net.fhirfactory.harmonia.agora.core.security.AgoraCollaborationPolicy;
import net.fhirfactory.harmonia.agora.core.security.AgoraSecurityUtils;
import net.fhirfactory.harmonia.agora.matrix.client.MatrixClientAdapter;
import net.fhirfactory.harmonia.agora.matrix.client.MatrixPowerLevelsDto;
import net.fhirfactory.harmonia.agora.matrix.client.MatrixRoomDto;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;
import net.fhirfactory.harmonia.themis.core.evaluator.DeterministicPolicyEvaluator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = AgoraCoreTestApplication.class)
@Transactional
@DisplayName("AgoraCollaborationLifecycleService Unit & Mock Tests")
@Timeout(10)
class AgoraCollaborationLifecycleServiceTest {

    @Autowired
    private AgoraMappingRepository mappingRepository;

    private MatrixClientAdapter matrixClientAdapter;
    private DeterministicPolicyEvaluator authorizer;
    private AgoraCollaborationLifecycleService lifecycleService;
    private ThemisSecurityContext collabContext;

    @BeforeEach
    void setUp() {
        mappingRepository.deleteAll();
        matrixClientAdapter = mock(MatrixClientAdapter.class);
        authorizer = DeterministicPolicyEvaluator.withDefaultPolicies();
        authorizer.registerPolicy(new AgoraCollaborationPolicy());

        lifecycleService = new AgoraCollaborationLifecycleService(
                matrixClientAdapter,
                mappingRepository,
                authorizer
        );

        collabContext = AgoraSecurityUtils.contextWithAuthorities(
                ThemisPrincipal.human("dr-coordinator"),
                "corr-collab-1",
                AgoraCollaborationPolicy.AUTH_AGORA_COLLABORATION
        );
    }

    @Test
    @DisplayName("Should create Patient Space with 4 child rooms and link hierarchy")
    void testCreatePatientSpaceHierarchySuccess() {
        String patientId = "pat-1001";
        AtomicInteger roomCounter = new AtomicInteger(100);

        when(matrixClientAdapter.createSpace(anyString(), anyString(), eq(true)))
                .thenAnswer(inv -> MatrixRoomDto.builder()
                        .roomId("!space-" + roomCounter.incrementAndGet() + ":synapse")
                        .name(inv.getArgument(0))
                        .space(true)
                        .build());

        when(matrixClientAdapter.createRoom(anyString(), anyString(), eq(true)))
                .thenAnswer(inv -> MatrixRoomDto.builder()
                        .roomId("!room-" + roomCounter.incrementAndGet() + ":synapse")
                        .name(inv.getArgument(0))
                        .space(false)
                        .build());

        when(matrixClientAdapter.linkChildRoom(anyString(), anyString(), anyList()))
                .thenReturn("$event-link-123");

        AgoraSpaceRequest request = AgoraSpaceRequest.builder()
                .harmoniaResourceType("PATIENT")
                .harmoniaResourceId(patientId)
                .displayName("Patient Space [pat-1001]")
                .topic("Care coordination space")
                .initialMembers(List.of("@nurse:synapse", "@doctor:synapse"))
                .securityContext(collabContext)
                .build();

        AgoraPatientSpaceResponse response = lifecycleService.createPatientSpace(request);

        assertThat(response).isNotNull();
        assertThat(response.getPatientId()).isEqualTo(patientId);
        assertThat(response.getSpaceId()).startsWith("!space-");
        assertThat(response.getChildRoomIds()).hasSize(4);
        assertThat(response.getStatisticsRoomId()).startsWith("!room-");
        assertThat(response.getTasksRoomId()).startsWith("!room-");
        assertThat(response.getDiscussionRoomId()).startsWith("!room-");
        assertThat(response.getDiagnosticsRoomId()).startsWith("!room-");

        // Verify parent Space created once and 4 child rooms created
        verify(matrixClientAdapter, times(1)).createSpace(contains("Patient Space"), anyString(), eq(true));
        verify(matrixClientAdapter, times(4)).createRoom(anyString(), anyString(), eq(true));
        verify(matrixClientAdapter, times(4)).linkChildRoom(eq(response.getSpaceId()), anyString(), anyList());

        // Verify initial members invited to Space and 4 child rooms (2 members * 5 rooms = 10 invites)
        verify(matrixClientAdapter, times(10)).inviteUser(anyString(), anyString(), anyString());

        // Verify durable mappings in Mnemosyne
        List<AgoraMappingEntity> mappings = mappingRepository
                .findByHarmoniaResourceTypeAndHarmoniaResourceId("PATIENT", patientId);
        assertThat(mappings).hasSize(5);

        Optional<AgoraMappingEntity> spaceMapping = mappingRepository
                .findByHarmoniaResourceTypeAndHarmoniaResourceIdAndMatrixEntityType("PATIENT", patientId, "SPACE");
        assertThat(spaceMapping).isPresent();
        assertThat(spaceMapping.get().getMatrixEntityId()).isEqualTo(response.getSpaceId());
        assertThat(spaceMapping.get().getStatus()).isEqualTo("ACTIVE");

        Optional<AgoraMappingEntity> tasksMapping = mappingRepository
                .findByHarmoniaResourceTypeAndHarmoniaResourceIdAndMatrixEntityType("PATIENT", patientId, "ROOM_TASKS");
        assertThat(tasksMapping).isPresent();
        assertThat(tasksMapping.get().getMatrixEntityId()).isEqualTo(response.getTasksRoomId());
    }

    @Test
    @DisplayName("Should be idempotent and return existing Patient Space hierarchy on duplicate request")
    void testCreatePatientSpaceIdempotent() {
        String patientId = "pat-idemp";

        when(matrixClientAdapter.createSpace(anyString(), anyString(), eq(true)))
                .thenReturn(MatrixRoomDto.builder().roomId("!space-idemp:synapse").space(true).build());
        when(matrixClientAdapter.createRoom(anyString(), anyString(), eq(true)))
                .thenReturn(
                        MatrixRoomDto.builder().roomId("!room-stat:synapse").build(),
                        MatrixRoomDto.builder().roomId("!room-task:synapse").build(),
                        MatrixRoomDto.builder().roomId("!room-disc:synapse").build(),
                        MatrixRoomDto.builder().roomId("!room-diag:synapse").build()
                );

        AgoraSpaceRequest request = AgoraSpaceRequest.builder()
                .harmoniaResourceType("PATIENT")
                .harmoniaResourceId(patientId)
                .securityContext(collabContext)
                .build();

        AgoraPatientSpaceResponse first = lifecycleService.createPatientSpace(request);
        AgoraPatientSpaceResponse second = lifecycleService.createPatientSpace(request);

        assertThat(first.getSpaceId()).isEqualTo(second.getSpaceId());
        assertThat(first.getChildRoomIds()).isEqualTo(second.getChildRoomIds());

        // Verify createSpace called only once
        verify(matrixClientAdapter, times(1)).createSpace(anyString(), anyString(), eq(true));
        verify(matrixClientAdapter, times(4)).createRoom(anyString(), anyString(), eq(true));
    }

    @Test
    @DisplayName("Should enforce Themis default-deny when creating space under production evaluator")
    void testCreatePatientSpaceThemisDenial() {
        // Using lifecycleService configured with production authorizer (DeterministicPolicyEvaluator + AgoraCollaborationPolicy)
        ThemisSecurityContext unauthorizedContext = ThemisSecurityContext.fromPrincipal(
                ThemisPrincipal.human("unauthorized-user"),
                "corr-denied"
        );

        AgoraSpaceRequest unauthorizedRequest = AgoraSpaceRequest.builder()
                .harmoniaResourceType("PATIENT")
                .harmoniaResourceId("pat-denied")
                .securityContext(unauthorizedContext)
                .build();

        // Unauthorized principal lacking agora.collaboration authority must be denied
        assertThatThrownBy(() -> lifecycleService.createPatientSpace(unauthorizedRequest))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("Themis security authorization denied collaboration lifecycle operation");

        // Null context (anonymous caller) must also be denied
        AgoraSpaceRequest anonRequest = AgoraSpaceRequest.builder()
                .harmoniaResourceType("PATIENT")
                .harmoniaResourceId("pat-denied-anon")
                .securityContext(null)
                .build();

        assertThatThrownBy(() -> lifecycleService.createPatientSpace(anonRequest))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("Themis security authorization denied collaboration lifecycle operation");

        verifyNoInteractions(matrixClientAdapter);
        assertThat(mappingRepository.findByHarmoniaResourceTypeAndHarmoniaResourceId("PATIENT", "pat-denied")).isEmpty();

        // But with authorized context carrying agora.collaboration, space creation succeeds
        when(matrixClientAdapter.createSpace(anyString(), anyString(), eq(true)))
                .thenReturn(MatrixRoomDto.builder().roomId("!space-allowed:synapse").space(true).build());
        when(matrixClientAdapter.createRoom(anyString(), anyString(), eq(true)))
                .thenReturn(MatrixRoomDto.builder().roomId("!room-stat:synapse").build());

        AgoraSpaceRequest allowedRequest = AgoraSpaceRequest.builder()
                .harmoniaResourceType("PATIENT")
                .harmoniaResourceId("pat-allowed")
                .securityContext(collabContext)
                .build();

        AgoraPatientSpaceResponse allowedResponse = lifecycleService.createPatientSpace(allowedRequest);
        assertThat(allowedResponse).isNotNull();
        assertThat(allowedResponse.getSpaceId()).isEqualTo("!space-allowed:synapse");
    }

    @Test
    @DisplayName("Should create Practitioner Space and PractitionerRole child room")
    void testCreatePractitionerSpaceAndRoleRoom() {
        String practitionerId = "prv-999";
        String roleId = "role-cardiology-1";

        when(matrixClientAdapter.createSpace(anyString(), anyString(), eq(true)))
                .thenReturn(MatrixRoomDto.builder().roomId("!space-prv:synapse").space(true).build());
        when(matrixClientAdapter.createRoom(anyString(), anyString(), eq(true)))
                .thenReturn(MatrixRoomDto.builder().roomId("!room-role:synapse").space(false).build());

        // 1. Create Practitioner Space
        AgoraSpaceRequest spaceRequest = AgoraSpaceRequest.builder()
                .harmoniaResourceType("PRACTITIONER")
                .harmoniaResourceId(practitionerId)
                .securityContext(collabContext)
                .build();

        String spaceId = lifecycleService.createPractitionerSpace(spaceRequest);
        assertThat(spaceId).isEqualTo("!space-prv:synapse");

        // 2. Create PractitionerRole child room
        AgoraRoomRequest roleRequest = AgoraRoomRequest.builder()
                .harmoniaResourceType("PRACTITIONER_ROLE")
                .harmoniaResourceId(roleId)
                .spaceId(spaceId)
                .roomType(AgoraRoomType.PRACTITIONER_ROLE)
                .securityContext(collabContext)
                .build();

        String roleRoomId = lifecycleService.createPractitionerRoleRoom(roleRequest);
        assertThat(roleRoomId).isEqualTo("!room-role:synapse");

        verify(matrixClientAdapter).linkChildRoom(eq(spaceId), eq(roleRoomId), anyList());

        // Verify mappings
        Optional<AgoraMappingEntity> spaceMapping = mappingRepository
                .findByHarmoniaResourceTypeAndHarmoniaResourceIdAndMatrixEntityType("PRACTITIONER", practitionerId, "SPACE");
        assertThat(spaceMapping).isPresent();

        Optional<AgoraMappingEntity> roleMapping = mappingRepository
                .findByHarmoniaResourceTypeAndHarmoniaResourceIdAndMatrixEntityType("PRACTITIONER_ROLE", roleId, "ROOM");
        assertThat(roleMapping).isPresent();
    }

    @Test
    @DisplayName("Should create Group collaboration room")
    void testCreateGroupRoom() {
        String groupId = "group-emergency-team";

        when(matrixClientAdapter.createRoom(anyString(), anyString(), eq(true)))
                .thenReturn(MatrixRoomDto.builder().roomId("!group-room:synapse").space(false).build());

        AgoraRoomRequest request = AgoraRoomRequest.builder()
                .harmoniaResourceType("GROUP")
                .harmoniaResourceId(groupId)
                .roomType(AgoraRoomType.GROUP)
                .initialMembers(List.of("@doc1:synapse"))
                .securityContext(collabContext)
                .build();

        String roomId = lifecycleService.createGroupRoom(request);
        assertThat(roomId).isEqualTo("!group-room:synapse");

        verify(matrixClientAdapter).inviteUser(eq("!group-room:synapse"), eq("@doc1:synapse"), anyString());

        Optional<AgoraMappingEntity> mapping = mappingRepository
                .findByHarmoniaResourceTypeAndHarmoniaResourceIdAndMatrixEntityType("GROUP", groupId, "ROOM");
        assertThat(mapping).isPresent();
        assertThat(mapping.get().getMatrixEntityId()).isEqualTo("!group-room:synapse");
    }

    @Test
    @DisplayName("Should archive Patient Space hierarchy with read-only power levels")
    void testArchivePatientSpace() {
        String patientId = "pat-archive";

        // Pre-populate mappings
        mappingRepository.save(new AgoraMappingEntity("PATIENT", patientId, "SPACE", "!space-arc:synapse", "ACTIVE"));
        mappingRepository.save(new AgoraMappingEntity("PATIENT", patientId, "ROOM_TASKS", "!room-arc-tasks:synapse", "ACTIVE"));

        lifecycleService.archivePatientSpace(patientId, collabContext);

        // Verify read-only power levels set on both rooms
        verify(matrixClientAdapter, times(2)).setPowerLevels(anyString(), argThat(dto ->
                dto.getEventsDefault() != null && dto.getEventsDefault() == 50 &&
                dto.getUsersDefault() != null && dto.getUsersDefault() == 0
        ));

        // Verify mappings marked as ARCHIVED
        List<AgoraMappingEntity> mappings = mappingRepository
                .findByHarmoniaResourceTypeAndHarmoniaResourceId("PATIENT", patientId);
        assertThat(mappings).allMatch(m -> "ARCHIVED".equals(m.getStatus()));
    }
}
