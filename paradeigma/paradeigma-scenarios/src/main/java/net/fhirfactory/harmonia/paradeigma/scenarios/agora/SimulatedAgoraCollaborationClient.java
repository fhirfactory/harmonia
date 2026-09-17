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
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory deterministic simulation of Agora endpoints for test scenario execution.
 */
public class SimulatedAgoraCollaborationClient implements AgoraCollaborationClient {

    private final String expectedHsToken;
    private final Map<String, AgoraPatientSpaceResponse> patientSpaces = new ConcurrentHashMap<>();
    private final Map<String, String> practitionerSpaces = new ConcurrentHashMap<>();
    private final Map<String, String> practitionerRoleRooms = new ConcurrentHashMap<>();
    private final Set<String> processedTransactions = Collections.synchronizedSet(new HashSet<>());
    private final Map<String, Set<String>> roomMembers = new ConcurrentHashMap<>();

    public SimulatedAgoraCollaborationClient() {
        this("test-hs-token");
    }

    public SimulatedAgoraCollaborationClient(String expectedHsToken) {
        this.expectedHsToken = expectedHsToken;
    }

    @Override
    public AgoraPatientSpaceResponse provisionPatientSpace(AgoraSpaceRequest request, ThemisSecurityContext context) throws Exception {
        validateSecurity(context, "agora.collaboration");

        String patientId = request.getHarmoniaResourceId();
        if (patientSpaces.containsKey(patientId)) {
            return patientSpaces.get(patientId);
        }

        String spaceId = "!space_p_" + patientId + ":synapse";
        Map<AgoraRoomType, String> rooms = new HashMap<>();
        rooms.put(AgoraRoomType.STATISTICS, "!room_stat_p_" + patientId + ":synapse");
        rooms.put(AgoraRoomType.TASKS, "!room_tasks_p_" + patientId + ":synapse");
        rooms.put(AgoraRoomType.DISCUSSION, "!room_disc_p_" + patientId + ":synapse");
        rooms.put(AgoraRoomType.DIAGNOSTICS, "!room_diag_p_" + patientId + ":synapse");

        Set<String> initialMembers = new HashSet<>();
        if (request.getInitialMembers() != null) {
            initialMembers.addAll(request.getInitialMembers());
        }

        // Initialize room memberships
        rooms.values().forEach(rId -> roomMembers.put(rId, new HashSet<>(initialMembers)));
        roomMembers.put(spaceId, new HashSet<>(initialMembers));

        AgoraPatientSpaceResponse response = new AgoraPatientSpaceResponse(spaceId, patientId, rooms);
        patientSpaces.put(patientId, response);
        return response;
    }

    @Override
    public String provisionPractitionerSpace(AgoraSpaceRequest request, ThemisSecurityContext context) throws Exception {
        validateSecurity(context, "agora.collaboration");
        String practitionerId = request.getHarmoniaResourceId();
        String spaceId = "!space_prac_" + practitionerId + ":synapse";
        practitionerSpaces.put(practitionerId, spaceId);
        roomMembers.put(spaceId, new HashSet<>());
        return spaceId;
    }

    @Override
    public String provisionPractitionerRoleRoom(AgoraRoomRequest request, ThemisSecurityContext context) throws Exception {
        validateSecurity(context, "agora.collaboration");
        String roleId = request.getHarmoniaResourceId();
        String roomId = "!room_role_" + roleId + ":synapse";
        practitionerRoleRooms.put(roleId, roomId);
        roomMembers.put(roomId, new HashSet<>());
        return roomId;
    }

    @Override
    public boolean submitApplicationServiceTransaction(String txnId, String hsToken, String jsonPayload) throws Exception {
        if (hsToken == null || !expectedHsToken.equals(hsToken)) {
            throw new SecurityException("Invalid homeserver token");
        }
        if (processedTransactions.contains(txnId)) {
            // Idempotent duplicate return 200 OK
            return true;
        }
        processedTransactions.add(txnId);
        return true;
    }

    @Override
    public AgoraReconciliationResult reconcileHierarchyMembership(String resourceType, String resourceId, Set<String> desiredUserIds, ThemisSecurityContext context) throws Exception {
        validateSecurity(context, "agora.admin");

        Set<String> allInvited = new HashSet<>();
        Set<String> allKicked = new HashSet<>();
        Set<String> allRetained = new HashSet<>();

        Set<String> roomsToReconcile = new HashSet<>();
        if ("PATIENT".equalsIgnoreCase(resourceType)) {
            AgoraPatientSpaceResponse spaceResp = patientSpaces.get(resourceId);
            if (spaceResp != null) {
                roomsToReconcile.add(spaceResp.getSpaceId());
                roomsToReconcile.addAll(spaceResp.getChildRoomIds().values());
            }
        }

        for (String rId : roomsToReconcile) {
            Set<String> live = roomMembers.computeIfAbsent(rId, k -> new HashSet<>());

            // Check desired members
            for (String desired : desiredUserIds) {
                if (live.contains(desired)) {
                    allRetained.add(desired);
                } else {
                    live.add(desired);
                    allInvited.add(desired);
                }
            }

            // Check live drift members
            Set<String> toEvict = new HashSet<>();
            for (String current : live) {
                if (!desiredUserIds.contains(current) && !current.startsWith("@_harmonia_bot")) {
                    toEvict.add(current);
                    allKicked.add(current);
                }
            }
            live.removeAll(toEvict);
        }

        return new AgoraReconciliationResult(
                resourceId,
                List.copyOf(allInvited),
                List.copyOf(allKicked),
                List.copyOf(allRetained),
                List.of()
        );
    }

    @Override
    public void injectLiveRoomMember(String roomId, String userId) throws Exception {
        roomMembers.computeIfAbsent(roomId, k -> new HashSet<>()).add(userId);
    }

    public Set<String> getLiveRoomMembers(String roomId) {
        return Collections.unmodifiableSet(roomMembers.getOrDefault(roomId, Collections.emptySet()));
    }

    public Set<String> getProcessedTransactions() {
        return Collections.unmodifiableSet(processedTransactions);
    }

    private void validateSecurity(ThemisSecurityContext context, String requiredAuthority) {
        if (context == null || context.requestingPrincipal() == null) {
            throw new SecurityException("Themis default-deny: missing security context or requesting principal");
        }

        Map<String, String> attrs = context.attributes();
        if (attrs == null) {
            throw new SecurityException("Themis default-deny: context has no attributes");
        }

        String authStr = String.valueOf(attrs.getOrDefault("authorities", ""));
        String roleStr = String.valueOf(attrs.getOrDefault("roleCodes", ""));

        boolean authorized = authStr.contains(requiredAuthority)
                || authStr.contains("agora.admin")
                || authStr.contains("system.admin")
                || authStr.contains("system.integration")
                || authStr.contains("*")
                || roleStr.contains("SYS_ADM")
                || roleStr.contains("SYS_INT");

        if (!authorized) {
            throw new SecurityException("Themis default-deny: caller lacks required authority " + requiredAuthority);
        }
    }
}
