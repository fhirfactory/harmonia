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
import net.fhirfactory.harmonia.agora.api.model.AgoraSpaceRequest;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;

import java.util.Set;

/**
 * Public client contract for interacting with Agora collaboration capabilities from Paradeigma scenarios.
 */
public interface AgoraCollaborationClient {

    /**
     * Provisions a Patient Space and child rooms.
     */
    AgoraPatientSpaceResponse provisionPatientSpace(AgoraSpaceRequest request, ThemisSecurityContext context) throws Exception;

    /**
     * Provisions a Practitioner Space.
     */
    String provisionPractitionerSpace(AgoraSpaceRequest request, ThemisSecurityContext context) throws Exception;

    /**
     * Provisions a PractitionerRole child room.
     */
    String provisionPractitionerRoleRoom(AgoraRoomRequest request, ThemisSecurityContext context) throws Exception;

    /**
     * Submits an inbound Matrix Application Service transaction to the AS endpoint.
     *
     * @param txnId homeserver transaction identifier
     * @param hsToken homeserver Bearer token
     * @param jsonPayload raw JSON transaction payload
     * @return true if accepted and processed / deduplicated
     */
    boolean submitApplicationServiceTransaction(String txnId, String hsToken, String jsonPayload) throws Exception;

    /**
     * Reconciles collaboration membership for a resource hierarchy against live Matrix state.
     */
    AgoraReconciliationResult reconcileHierarchyMembership(String resourceType, String resourceId, Set<String> desiredUserIds, ThemisSecurityContext context) throws Exception;

    /**
     * Injects an unauthorized intruder or drift member into a live Matrix room for drift testing.
     */
    void injectLiveRoomMember(String roomId, String userId) throws Exception;
}
