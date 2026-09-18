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

package net.fhirfactory.harmonia.paradeigma.test.agora;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.fhirfactory.harmonia.agora.api.model.*;
import net.fhirfactory.harmonia.agora.core.lifecycle.AgoraCollaborationLifecycleService;
import net.fhirfactory.harmonia.agora.core.messaging.AgoraPetasosEventProducer;
import net.fhirfactory.harmonia.agora.core.persistence.AgoraMappingEntity;
import net.fhirfactory.harmonia.agora.core.persistence.AgoraMappingRepository;
import net.fhirfactory.harmonia.agora.core.persistence.AgoraTransactionEntity;
import net.fhirfactory.harmonia.agora.core.persistence.AgoraTransactionRepository;
import net.fhirfactory.harmonia.agora.core.reconciliation.AgoraMembershipReconciliationService;
import net.fhirfactory.harmonia.agora.core.security.AgoraCollaborationPolicy;
import net.fhirfactory.harmonia.agora.matrix.appservice.AppServiceTransactionDto;
import net.fhirfactory.harmonia.agora.matrix.client.MatrixClientAdapter;
import net.fhirfactory.harmonia.agora.matrix.client.MatrixMemberDto;
import net.fhirfactory.harmonia.agora.matrix.client.MatrixRoomDto;
import net.fhirfactory.harmonia.agora.service.config.AgoraProperties;
import net.fhirfactory.harmonia.agora.service.rest.ApplicationServiceTransactionEndpoint;
import net.fhirfactory.harmonia.paradeigma.common.security.SecurityScenarioContext;
import net.fhirfactory.harmonia.paradeigma.scenarios.agora.AgoraCollaborationClient;
import net.fhirfactory.harmonia.paradeigma.scenarios.agora.AgoraCollaborationScenario;
import net.fhirfactory.harmonia.paradeigma.scenarios.agora.AgoraCollaborationScenarioResult;
import net.fhirfactory.harmonia.paradeigma.scenarios.model.ScenarioExpectation;
import net.fhirfactory.harmonia.themis.api.model.ThemisDecision;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;
import net.fhirfactory.harmonia.themis.core.evaluator.DeterministicPolicyEvaluator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@Timeout(value = 10, unit = TimeUnit.SECONDS)
@DisplayName("Paradeigma Agora Collaboration Scenario Integration Tests")
class AgoraCollaborationScenarioTest {

    private static final String HS_TOKEN = "paradeigma-hs-token-xyz";

    @Test
    @DisplayName("Scenario: Full 5-phase execution in simulated mode succeeds")
    void testAgoraCollaborationScenarioSimulation() {
        long seed = 77777L;
        AgoraCollaborationScenario scenario = AgoraCollaborationScenario.createDefault(seed);

        AgoraCollaborationScenarioResult result = scenario.execute();

        assertThat(result).isNotNull();
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getSecurityDecision()).isEqualTo(ThemisDecision.ALLOW);

        // Verification of 5 phases
        assertThat(result.getPatientSpaceId()).startsWith("!space_p_");
        assertThat(result.getPatientChildRoomIds()).hasSize(4);
        assertThat(result.getPractitionerSpaceId()).startsWith("!space_prac_");
        assertThat(result.getPractitionerRoleRoomId()).startsWith("!room_role_");
        assertThat(result.isMessageDelivered()).isTrue();
        assertThat(result.isDuplicateAcknowledged()).isTrue();
        assertThat(result.isDriftDetected()).isTrue();
        assertThat(result.isDriftResolved()).isTrue();
        assertThat(result.getKickedUserIds()).isNotEmpty();

        assertThat(result.getSteps()).hasSize(5);
        assertThat(result.getSteps()).allMatch(s -> s.isSuccess());
    }

    @Test
    @DisplayName("Scenario: Full 5-phase execution wired against production Agora core services succeeds")
    void testAgoraCollaborationScenarioWithProductionServicesWiring() throws Exception {
        long seed = 88888L;

        // 1. Setup production Themis Evaluator with AgoraCollaborationPolicy
        DeterministicPolicyEvaluator evaluator = DeterministicPolicyEvaluator.withDefaultPolicies();
        evaluator.registerPolicy(new AgoraCollaborationPolicy());

        // 2. Setup in-memory store for mappings
        Map<String, AgoraMappingEntity> mappingStore = new ConcurrentHashMap<>();
        AgoraMappingRepository mappingRepository = mock(AgoraMappingRepository.class);

        when(mappingRepository.save(any(AgoraMappingEntity.class))).thenAnswer(inv -> {
            AgoraMappingEntity entity = inv.getArgument(0);
            String key = entity.getHarmoniaResourceType() + ":" + entity.getHarmoniaResourceId() + ":" + entity.getMatrixEntityType();
            mappingStore.put(key, entity);
            return entity;
        });

        when(mappingRepository.findByHarmoniaResourceTypeAndHarmoniaResourceIdAndMatrixEntityType(anyString(), anyString(), anyString()))
                .thenAnswer(inv -> {
                    String key = inv.getArgument(0) + ":" + inv.getArgument(1) + ":" + inv.getArgument(2);
                    return Optional.ofNullable(mappingStore.get(key));
                });

        when(mappingRepository.findByHarmoniaResourceTypeAndHarmoniaResourceId(anyString(), anyString()))
                .thenAnswer(inv -> {
                    String prefix = inv.getArgument(0) + ":" + inv.getArgument(1) + ":";
                    List<AgoraMappingEntity> list = new ArrayList<>();
                    mappingStore.forEach((k, v) -> {
                        if (k.startsWith(prefix)) list.add(v);
                    });
                    return list;
                });

        // 3. Setup Mock Matrix Client Adapter tracking live state
        MatrixClientAdapter matrixClientAdapter = mock(MatrixClientAdapter.class);
        AtomicInteger roomCounter = new AtomicInteger(100);
        Map<String, Set<String>> liveRoomMembers = new ConcurrentHashMap<>();

        when(matrixClientAdapter.createSpace(anyString(), anyString(), anyBoolean()))
                .thenAnswer(inv -> {
                    String roomId = "!space-" + roomCounter.incrementAndGet() + ":synapse";
                    liveRoomMembers.put(roomId, new HashSet<>());
                    return MatrixRoomDto.builder().roomId(roomId).name(inv.getArgument(0)).space(true).build();
                });

        when(matrixClientAdapter.createRoom(anyString(), anyString(), anyBoolean()))
                .thenAnswer(inv -> {
                    String roomId = "!room-" + roomCounter.incrementAndGet() + ":synapse";
                    liveRoomMembers.put(roomId, new HashSet<>());
                    return MatrixRoomDto.builder().roomId(roomId).name(inv.getArgument(0)).space(false).build();
                });

        when(matrixClientAdapter.linkChildRoom(anyString(), anyString(), anyList()))
                .thenReturn("$event-link-ok");

        doAnswer(inv -> {
            String roomId = inv.getArgument(0);
            String userId = inv.getArgument(1);
            liveRoomMembers.computeIfAbsent(roomId, k -> new HashSet<>()).add(userId);
            return null;
        }).when(matrixClientAdapter).inviteUser(anyString(), anyString(), any());

        doAnswer(inv -> {
            String roomId = inv.getArgument(0);
            String userId = inv.getArgument(1);
            Set<String> members = liveRoomMembers.get(roomId);
            if (members != null) members.remove(userId);
            return null;
        }).when(matrixClientAdapter).kickUser(anyString(), anyString(), any());

        when(matrixClientAdapter.getRoomMembers(anyString()))
                .thenAnswer(inv -> {
                    String roomId = inv.getArgument(0);
                    Set<String> members = liveRoomMembers.getOrDefault(roomId, Set.of());
                    List<MatrixMemberDto> dtoList = new ArrayList<>();
                    for (String m : members) {
                        dtoList.add(new MatrixMemberDto(m, "join", m));
                    }
                    return dtoList;
                });

        // 4. Setup production Agora Core services
        AgoraCollaborationLifecycleService lifecycleService = new AgoraCollaborationLifecycleService(
                matrixClientAdapter,
                mappingRepository,
                evaluator
        );

        AgoraMembershipReconciliationService reconciliationService = new AgoraMembershipReconciliationService(
                matrixClientAdapter,
                mappingRepository,
                evaluator
        );

        // 5. Setup production AS Transaction Endpoint
        AgoraProperties agoraProps = new AgoraProperties();
        agoraProps.getSecurity().setHsToken(HS_TOKEN);

        Map<String, AgoraTransactionEntity> txnStore = new ConcurrentHashMap<>();
        AgoraTransactionRepository txnRepo = mock(AgoraTransactionRepository.class);

        when(txnRepo.findById(anyString())).thenAnswer(inv -> Optional.ofNullable(txnStore.get(inv.getArgument(0))));
        when(txnRepo.save(any(AgoraTransactionEntity.class))).thenAnswer(inv -> {
            AgoraTransactionEntity entity = inv.getArgument(0);
            txnStore.put(entity.getTransactionId(), entity);
            return entity;
        });

        AgoraPetasosEventProducer petasosProducer = mock(AgoraPetasosEventProducer.class);

        ApplicationServiceTransactionEndpoint asEndpoint = new ApplicationServiceTransactionEndpoint(
                agoraProps,
                txnRepo,
                petasosProducer
        );

        ObjectMapper objectMapper = new ObjectMapper();

        // 6. Implement client adapter delegating to the production services
        AgoraCollaborationClient client = new AgoraCollaborationClient() {
            @Override
            public AgoraPatientSpaceResponse provisionPatientSpace(AgoraSpaceRequest request, ThemisSecurityContext context) throws Exception {
                return lifecycleService.createPatientSpace(request);
            }

            @Override
            public String provisionPractitionerSpace(AgoraSpaceRequest request, ThemisSecurityContext context) throws Exception {
                return lifecycleService.createPractitionerSpace(request);
            }

            @Override
            public String provisionPractitionerRoleRoom(AgoraRoomRequest request, ThemisSecurityContext context) throws Exception {
                return lifecycleService.createPractitionerRoleRoom(request);
            }

            @Override
            public boolean submitApplicationServiceTransaction(String txnId, String hsToken, String jsonPayload) throws Exception {
                AppServiceTransactionDto dto = objectMapper.readValue(jsonPayload, AppServiceTransactionDto.class);
                ResponseEntity<Map<String, Object>> response = asEndpoint.handleTransaction(txnId, "Bearer " + hsToken, null, dto);
                return response.getStatusCode().is2xxSuccessful();
            }

            @Override
            public AgoraReconciliationResult reconcileHierarchyMembership(String resourceType, String resourceId, Set<String> desiredUserIds, ThemisSecurityContext context) throws Exception {
                List<AgoraReconciliationResult> results = reconciliationService.reconcileHierarchyMembership(resourceType, resourceId, desiredUserIds, context);
                Set<String> invited = new HashSet<>();
                Set<String> kicked = new HashSet<>();
                Set<String> retained = new HashSet<>();
                Set<String> denied = new HashSet<>();
                for (AgoraReconciliationResult r : results) {
                    invited.addAll(r.getInvitedUsers());
                    kicked.addAll(r.getKickedUsers());
                    retained.addAll(r.getRetainedUsers());
                    denied.addAll(r.getDeniedUsers());
                }
                return new AgoraReconciliationResult(resourceId, List.copyOf(invited), List.copyOf(kicked), List.copyOf(retained), List.copyOf(denied));
            }

            @Override
            public void injectLiveRoomMember(String roomId, String userId) throws Exception {
                liveRoomMembers.computeIfAbsent(roomId, k -> new HashSet<>()).add(userId);
            }
        };

        // 7. Execute the scenario with production wiring
        AgoraCollaborationScenario scenario = new AgoraCollaborationScenario(
                "scen-prod-agora",
                "ProductionWiredAgoraScenario",
                "Tests production Agora components via scenario engine",
                seed,
                SecurityScenarioContext.systemAdmin(),
                ScenarioExpectation.success(),
                client,
                HS_TOKEN
        );

        AgoraCollaborationScenarioResult result = scenario.execute();

        // 8. Verify complete scenario success
        assertThat(result).isNotNull();
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getSecurityDecision()).isEqualTo(ThemisDecision.ALLOW);

        // Verify child room hierarchy in result
        assertThat(result.getPatientSpaceId()).startsWith("!space-");
        assertThat(result.getPatientChildRoomIds()).hasSize(4);
        assertThat(result.getPractitionerSpaceId()).startsWith("!space-");
        assertThat(result.getPractitionerRoleRoomId()).startsWith("!room-");

        // Verify transaction deduplication in AS endpoint
        assertThat(result.isMessageDelivered()).isTrue();
        assertThat(result.isDuplicateAcknowledged()).isTrue();
        assertThat(txnStore).containsKey(result.getDuplicateTxnId());

        // Verify membership drift detection and eviction
        assertThat(result.isDriftDetected()).isTrue();
        assertThat(result.isDriftResolved()).isTrue();
        assertThat(result.getKickedUserIds()).isNotEmpty();

        // Verify all 5 execution steps succeeded
        assertThat(result.getSteps()).hasSize(5);
        assertThat(result.getSteps()).allMatch(s -> s.isSuccess());
    }

    @Test
    @DisplayName("Scenario: Themis default-deny blocks unauthorized scenario execution")
    void testAgoraCollaborationScenarioSecurityDefaultDeny() {
        long seed = 99999L;
        AgoraCollaborationScenario scenario = new AgoraCollaborationScenario(
                "scen-agora-deny",
                "DeniedAgoraScenario",
                "Verifies default-deny",
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
