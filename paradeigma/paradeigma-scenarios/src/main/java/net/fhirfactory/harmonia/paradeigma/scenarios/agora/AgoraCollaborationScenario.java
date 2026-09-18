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

package net.fhirfactory.harmonia.paradeigma.scenarios.agora;

import net.fhirfactory.harmonia.agora.api.model.AgoraPatientSpaceResponse;
import net.fhirfactory.harmonia.agora.api.model.AgoraReconciliationResult;
import net.fhirfactory.harmonia.agora.api.model.AgoraRoomRequest;
import net.fhirfactory.harmonia.agora.api.model.AgoraRoomType;
import net.fhirfactory.harmonia.agora.api.model.AgoraSpaceRequest;
import net.fhirfactory.harmonia.paradeigma.common.generator.SyntheticPatientGenerator;
import net.fhirfactory.harmonia.paradeigma.common.model.PatientProfile;
import net.fhirfactory.harmonia.paradeigma.common.security.SecurityScenarioContext;
import net.fhirfactory.harmonia.paradeigma.scenarios.model.ParadeigmaScenario;
import net.fhirfactory.harmonia.paradeigma.scenarios.model.ScenarioExecutionStep;
import net.fhirfactory.harmonia.paradeigma.scenarios.model.ScenarioExpectation;
import net.fhirfactory.harmonia.themis.api.model.ThemisDecision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Paradeigma synthetic collaboration scenario exercising Agora public contracts and interfaces:
 * 1. Patient Collaboration Space provisioning (Space + 4 child rooms).
 * 2. Practitioner Space & PractitionerRole room setup.
 * 3. Synthetic practitioner messaging event push.
 * 4. Duplicate Matrix Application Service transaction submission (idempotency & deduplication).
 * 5. Membership drift injection and Themis-governed reconciliation.
 */
public class AgoraCollaborationScenario implements ParadeigmaScenario<AgoraCollaborationScenarioResult> {

    private static final Logger log = LoggerFactory.getLogger(AgoraCollaborationScenario.class);

    private final String scenarioId;
    private final String name;
    private final String description;
    private final long seed;
    private final SecurityScenarioContext actor;
    private final ScenarioExpectation expectation;
    private final AgoraCollaborationClient client;
    private final String hsToken;

    public AgoraCollaborationScenario(
            String scenarioId,
            String name,
            String description,
            long seed,
            SecurityScenarioContext actor,
            ScenarioExpectation expectation,
            AgoraCollaborationClient client,
            String hsToken) {
        this.scenarioId = (scenarioId != null) ? scenarioId : "scen-agora-" + UUID.randomUUID().toString().substring(0, 8);
        this.name = (name != null) ? name : "AgoraCollaborationScenario";
        this.description = (description != null) ? description : "Simulates patient space lifecycle, practitioner role setup, messaging, AS transaction idempotency, and drift reconciliation";
        this.seed = seed;
        this.actor = (actor != null) ? actor : SecurityScenarioContext.systemAdmin();
        this.expectation = (expectation != null) ? expectation : ScenarioExpectation.success();
        this.hsToken = (hsToken != null) ? hsToken : "test-hs-token";
        this.client = (client != null) ? client : new SimulatedAgoraCollaborationClient(this.hsToken);
    }

    public AgoraCollaborationScenario(long seed, AgoraCollaborationClient client) {
        this(null, null, null, seed, null, null, client, null);
    }

    public AgoraCollaborationScenario(long seed) {
        this(seed, null);
    }

    public static AgoraCollaborationScenario createDefault(long seed) {
        return new AgoraCollaborationScenario(seed);
    }

    @Override
    public String getScenarioId() {
        return scenarioId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public long getSeed() {
        return seed;
    }

    @Override
    public SecurityScenarioContext getActor() {
        return actor;
    }

    @Override
    public ScenarioExpectation getExpectation() {
        return expectation;
    }

    public AgoraCollaborationClient getClient() {
        return client;
    }

    @Override
    public AgoraCollaborationScenarioResult execute() {
        long start = System.currentTimeMillis();
        AgoraCollaborationScenarioResult result = new AgoraCollaborationScenarioResult(scenarioId, name, seed);
        result.setCorrelationId(actor.correlationId());
        result.setSecurityDecision(ThemisDecision.ALLOW);

        log.info(">>> [Agora Scenario] START [{}] (Seed: {}, Actor: {})", scenarioId, seed,
                actor.principal() != null ? actor.principal().principalId() : "unauthenticated");

        try {
            // Check security expectation upfront
            if (expectation.expectedSecurityDecision() == ThemisDecision.DENY) {
                executeNegativeSecurityPath(result);
                return result;
            }

            // Step 1: Patient Space Provisioning (Space + 4 Child Rooms)
            SyntheticPatientGenerator patientGen = new SyntheticPatientGenerator(seed);
            PatientProfile patient = patientGen.generatePatient();
            String patientId = patient.getPatientId();
            result.setPatientId(patientId);

            Set<String> careTeamMembers = Set.of(
                    "@_harmonia_p_dr_" + patientId + ":synapse",
                    "@_harmonia_p_rn_" + patientId + ":synapse",
                    "@_harmonia_p_coord_" + patientId + ":synapse"
            );

            ScenarioExecutionStep s1 = new ScenarioExecutionStep(1, "Patient Space Provisioning", "Agora-Core", "PROVISION_SPACE");
            long stepStart = System.currentTimeMillis();
            AgoraSpaceRequest spaceRequest = AgoraSpaceRequest.builder()
                    .harmoniaResourceType("PATIENT")
                    .harmoniaResourceId(patientId)
                    .displayName("Patient Space [" + patientId + "]")
                    .topic("Collaboration space for patient " + patientId)
                    .initialMembers(List.copyOf(careTeamMembers))
                    .securityContext(actor.securityContext())
                    .build();

            AgoraPatientSpaceResponse spaceResp = client.provisionPatientSpace(spaceRequest, actor.securityContext());
            s1.setDurationMs(System.currentTimeMillis() - stepStart);

            if (spaceResp != null && spaceResp.getSpaceId() != null
                    && spaceResp.getChildRoomIds() != null
                    && spaceResp.getChildRoomIds().containsKey(AgoraRoomType.STATISTICS)
                    && spaceResp.getChildRoomIds().containsKey(AgoraRoomType.TASKS)
                    && spaceResp.getChildRoomIds().containsKey(AgoraRoomType.DISCUSSION)
                    && spaceResp.getChildRoomIds().containsKey(AgoraRoomType.DIAGNOSTICS)) {
                s1.setSuccess(true);
                s1.setAckCode("201");
                result.setPatientSpaceId(spaceResp.getSpaceId());
                result.setPatientChildRoomIds(spaceResp.getChildRoomIds());
            } else {
                s1.setSuccess(false);
                s1.setErrorMessage("Incomplete child room hierarchy in patient space response");
            }
            result.addStep(s1);

            // Step 2: Practitioner Space & PractitionerRole Child Room Setup
            String practitionerId = "prac-" + UUID.nameUUIDFromBytes(("practitioner-" + seed).getBytes()).toString().substring(0, 8);
            String roleId = "role-cardiology-" + practitionerId;
            result.setPractitionerId(practitionerId);
            result.setPractitionerRoleId(roleId);

            ScenarioExecutionStep s2 = new ScenarioExecutionStep(2, "PractitionerRole Room Setup", "Agora-Core", "PROVISION_ROLE_ROOM");
            stepStart = System.currentTimeMillis();

            AgoraSpaceRequest pracSpaceRequest = AgoraSpaceRequest.builder()
                    .harmoniaResourceType("PRACTITIONER")
                    .harmoniaResourceId(practitionerId)
                    .displayName("Practitioner Space [" + practitionerId + "]")
                    .topic("Clinical space for practitioner")
                    .securityContext(actor.securityContext())
                    .build();
            String pracSpaceId = client.provisionPractitionerSpace(pracSpaceRequest, actor.securityContext());
            result.setPractitionerSpaceId(pracSpaceId);

            AgoraRoomRequest roleRoomRequest = AgoraRoomRequest.builder()
                    .harmoniaResourceType("PRACTITIONER_ROLE")
                    .harmoniaResourceId(roleId)
                    .roomType(AgoraRoomType.TASKS)
                    .displayName("Cardiology Role Tasks")
                    .spaceId(pracSpaceId)
                    .securityContext(actor.securityContext())
                    .build();
            String roleRoomId = client.provisionPractitionerRoleRoom(roleRoomRequest, actor.securityContext());
            result.setPractitionerRoleRoomId(roleRoomId);

            s2.setDurationMs(System.currentTimeMillis() - stepStart);
            s2.setSuccess(pracSpaceId != null && roleRoomId != null);
            s2.setAckCode(s2.isSuccess() ? "201" : "500");
            result.addStep(s2);

            // Step 3: Synthetic Practitioner Messaging via Matrix AS Ingress
            String txnId = "txn-" + UUID.randomUUID();
            result.setMessageTxnId(txnId);

            ScenarioExecutionStep s3 = new ScenarioExecutionStep(3, "Synthetic Practitioner Messaging", "Agora-AS", "PUSH_EVENT");
            stepStart = System.currentTimeMillis();

            String tasksRoomId = spaceResp != null && spaceResp.getChildRoomIds() != null
                    ? spaceResp.getChildRoomIds().get(AgoraRoomType.TASKS)
                    : "!dummy_tasks:synapse";

            String jsonPayload = String.format(
                    "{\"events\":[{\"event_id\":\"$ev_%s\",\"type\":\"m.room.message\",\"room_id\":\"%s\",\"sender\":\"@_harmonia_p_%s:synapse\",\"origin_server_ts\":%d,\"content\":{\"msgtype\":\"m.text\",\"body\":\"Task review initiated for patient %s\"}}]}",
                    UUID.randomUUID().toString().substring(0, 8),
                    tasksRoomId,
                    practitionerId,
                    System.currentTimeMillis(),
                    patientId
            );

            boolean msgOk = client.submitApplicationServiceTransaction(txnId, hsToken, jsonPayload);
            s3.setDurationMs(System.currentTimeMillis() - stepStart);
            s3.setSuccess(msgOk);
            s3.setAckCode(msgOk ? "200" : "500");
            result.setMessageDelivered(msgOk);
            result.addStep(s3);

            // Step 4: Duplicate AS Transaction Submission (Idempotency & Deduplication)
            result.setDuplicateTxnId(txnId);
            ScenarioExecutionStep s4 = new ScenarioExecutionStep(4, "Duplicate Transaction Idempotency", "Agora-AS", "DEDUPLICATE_TRANSACTION");
            stepStart = System.currentTimeMillis();

            boolean dupOk = client.submitApplicationServiceTransaction(txnId, hsToken, jsonPayload);
            s4.setDurationMs(System.currentTimeMillis() - stepStart);
            s4.setSuccess(dupOk);
            s4.setAckCode(dupOk ? "200" : "500");
            result.setDuplicateAcknowledged(dupOk);
            result.addStep(s4);

            // Step 5: Membership Drift Simulation & Reconciliation
            String discussionRoomId = spaceResp != null && spaceResp.getChildRoomIds() != null
                    ? spaceResp.getChildRoomIds().get(AgoraRoomType.DISCUSSION)
                    : "!dummy_disc:synapse";

            String intruderUserId = "@intruder_bot_" + seed + ":synapse";
            client.injectLiveRoomMember(discussionRoomId, intruderUserId);

            ScenarioExecutionStep s5 = new ScenarioExecutionStep(5, "Membership Drift Resolution", "Agora-Reconciliation", "RECONCILE_DRIFT");
            stepStart = System.currentTimeMillis();

            // Authoritative desired members: careTeamMembers + an additional consulting physician
            String consultingDoctor = "@_harmonia_p_consultant_" + patientId + ":synapse";
            Set<String> desiredMembers = new HashSet<>(careTeamMembers);
            desiredMembers.add(consultingDoctor);

            AgoraReconciliationResult reconResult = client.reconcileHierarchyMembership(
                    "PATIENT",
                    patientId,
                    desiredMembers,
                    actor.securityContext()
            );

            s5.setDurationMs(System.currentTimeMillis() - stepStart);
            boolean driftResolved = reconResult != null
                    && reconResult.getKickedUsers().contains(intruderUserId)
                    && reconResult.getInvitedUsers().contains(consultingDoctor);

            s5.setSuccess(driftResolved);
            s5.setAckCode(driftResolved ? "200" : "409");

            result.setDriftDetected(reconResult != null && reconResult.hasDrift());
            result.setDriftResolved(driftResolved);
            if (reconResult != null) {
                result.setKickedUserIds(new HashSet<>(reconResult.getKickedUsers()));
                result.setInvitedUserIds(new HashSet<>(reconResult.getInvitedUsers()));
                result.setRetainedUserIds(new HashSet<>(reconResult.getRetainedUsers()));
            }
            result.addStep(s5);

            boolean allSuccess = result.getSteps().stream().allMatch(ScenarioExecutionStep::isSuccess);
            result.setSuccess(allSuccess);

        } catch (SecurityException se) {
            log.warn("[Agora Scenario] Security rejection encountered: {}", se.getMessage());
            result.setSecurityDecision(ThemisDecision.DENY);
            if (expectation.expectedSecurityDecision() == ThemisDecision.DENY) {
                result.setSuccess(true);
            } else {
                result.setSuccess(false);
                result.setErrorMessage(se.getMessage());
            }
        } catch (Exception e) {
            log.error("[Agora Scenario] Error executing scenario {}: {}", scenarioId, e.getMessage(), e);
            result.setSuccess(false);
            result.setErrorMessage(e.getMessage());
        } finally {
            result.setDurationMs(System.currentTimeMillis() - start);
            log.info("<<< [Agora Scenario] FINISHED [{}] (Success: {}, Duration: {} ms)",
                    scenarioId, result.isSuccess(), result.getDurationMs());
        }

        return result;
    }

    private void executeNegativeSecurityPath(AgoraCollaborationScenarioResult result) {
        ScenarioExecutionStep step = new ScenarioExecutionStep(1, "Enforce Security Governance", "Themis-Engine", "DENY_CHECK");
        long start = System.currentTimeMillis();
        try {
            AgoraSpaceRequest req = AgoraSpaceRequest.builder()
                    .harmoniaResourceType("PATIENT")
                    .harmoniaResourceId("p-unauthorized")
                    .displayName("Unauthorized Space")
                    .topic("Should fail")
                    .securityContext(actor.securityContext())
                    .build();
            client.provisionPatientSpace(req, actor.securityContext());
            step.setSuccess(false);
            step.setErrorMessage("Expected SecurityException was not thrown");
            result.setSuccess(false);
        } catch (SecurityException se) {
            step.setSuccess(true);
            step.setAckCode("403");
            result.setSecurityDecision(ThemisDecision.DENY);
            result.setSuccess(true);
        } catch (Exception e) {
            step.setSuccess(false);
            step.setErrorMessage(e.getMessage());
            result.setSuccess(false);
        } finally {
            step.setDurationMs(System.currentTimeMillis() - start);
            result.addStep(step);
        }
    }
}
