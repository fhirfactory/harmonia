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
import net.fhirfactory.harmonia.agora.core.persistence.AgoraMappingEntity;
import net.fhirfactory.harmonia.agora.core.persistence.AgoraMappingRepository;
import net.fhirfactory.harmonia.agora.core.security.AgoraCollaborationPolicy;
import net.fhirfactory.harmonia.agora.core.security.AgoraSecurityUtils;
import net.fhirfactory.harmonia.agora.matrix.client.MatrixClientAdapter;
import net.fhirfactory.harmonia.agora.matrix.client.MatrixPowerLevelsDto;
import net.fhirfactory.harmonia.agora.matrix.client.MatrixRoomDto;
import net.fhirfactory.harmonia.themis.api.ThemisAuthorizer;
import net.fhirfactory.harmonia.themis.api.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Service managing the lifecycle of Agora collaboration spaces and rooms.
 * Orchestrates hierarchical Patient Spaces with four standard child rooms
 * (Statistics, Tasks, Discussion, Diagnostics), Practitioner Spaces with PractitionerRole
 * child rooms, and Group collaboration rooms with minimal clinical metadata and durable Mnemosyne persistence.
 */
public class AgoraCollaborationLifecycleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AgoraCollaborationLifecycleService.class);

    public static final String RESOURCE_TYPE_PATIENT = "PATIENT";
    public static final String RESOURCE_TYPE_PRACTITIONER = "PRACTITIONER";
    public static final String RESOURCE_TYPE_PRACTITIONER_ROLE = "PRACTITIONER_ROLE";
    public static final String RESOURCE_TYPE_GROUP = "GROUP";

    public static final String MATRIX_ENTITY_SPACE = "SPACE";
    public static final String MATRIX_ENTITY_ROOM = "ROOM";

    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_ARCHIVED = "ARCHIVED";

    private final MatrixClientAdapter matrixClientAdapter;
    private final AgoraMappingRepository mappingRepository;
    private final ThemisAuthorizer themisAuthorizer;

    public AgoraCollaborationLifecycleService(
            MatrixClientAdapter matrixClientAdapter,
            AgoraMappingRepository mappingRepository,
            ThemisAuthorizer themisAuthorizer
    ) {
        this.matrixClientAdapter = Objects.requireNonNull(matrixClientAdapter, "MatrixClientAdapter must not be null");
        this.mappingRepository = Objects.requireNonNull(mappingRepository, "AgoraMappingRepository must not be null");
        this.themisAuthorizer = Objects.requireNonNull(themisAuthorizer, "ThemisAuthorizer must not be null");
    }

    /**
     * Creates or resolves a complete Patient Space hierarchy comprising the parent Space
     * and four child rooms: Statistics, Tasks, Discussion, and Diagnostics.
     *
     * @param request the space request
     * @return the provisioned Patient Space response
     */
    public AgoraPatientSpaceResponse createPatientSpace(AgoraSpaceRequest request) {
        Objects.requireNonNull(request, "AgoraSpaceRequest must not be null");
        String patientId = Objects.requireNonNull(request.getHarmoniaResourceId(), "patientId must not be null");

        evaluateAuthorization(RESOURCE_TYPE_PATIENT, patientId, ThemisAction.CREATE, request.getSecurityContext());

        LOGGER.info("Provisioning Patient Space hierarchy for patientId={}", patientId);

        // Idempotency: check if Space already exists
        Optional<AgoraMappingEntity> existingSpaceOpt = mappingRepository
                .findByHarmoniaResourceTypeAndHarmoniaResourceIdAndMatrixEntityType(
                        RESOURCE_TYPE_PATIENT, patientId, MATRIX_ENTITY_SPACE
                );

        String spaceRoomId;
        Map<AgoraRoomType, String> childRoomIds = new EnumMap<>(AgoraRoomType.class);

        if (existingSpaceOpt.isPresent() && STATUS_ACTIVE.equalsIgnoreCase(existingSpaceOpt.get().getStatus())) {
            spaceRoomId = existingSpaceOpt.get().getMatrixEntityId();
            LOGGER.info("Existing active Patient Space found spaceId={} for patientId={}", spaceRoomId, patientId);

            // Check existing child rooms
            for (AgoraRoomType type : List.of(AgoraRoomType.STATISTICS, AgoraRoomType.TASKS, AgoraRoomType.DISCUSSION, AgoraRoomType.DIAGNOSTICS)) {
                mappingRepository.findByHarmoniaResourceTypeAndHarmoniaResourceIdAndMatrixEntityType(
                        RESOURCE_TYPE_PATIENT, patientId, childRoomMappingType(type)
                ).ifPresent(entity -> childRoomIds.put(type, entity.getMatrixEntityId()));
            }

            if (childRoomIds.size() == 4) {
                return new AgoraPatientSpaceResponse(spaceRoomId, patientId, childRoomIds);
            }
        } else {
            // Create private parent Space
            String spaceTitle = "Patient Space [" + patientId + "]";
            String spaceTopic = (request.getTopic() != null && !request.getTopic().isBlank())
                    ? request.getTopic()
                    : "Patient collaboration space";

            MatrixRoomDto space = matrixClientAdapter.createSpace(spaceTitle, spaceTopic, true);
            spaceRoomId = space.getRoomId();

            saveOrUpdateMapping(RESOURCE_TYPE_PATIENT, patientId, MATRIX_ENTITY_SPACE, spaceRoomId, STATUS_ACTIVE);
            LOGGER.info("Created parent Patient Space spaceId={} for patientId={}", spaceRoomId, patientId);
        }

        // Create standard child rooms if missing
        createAndLinkChildRoomIfMissing(patientId, spaceRoomId, AgoraRoomType.STATISTICS, "Patient Statistics", "Patient statistics and vitals", childRoomIds);
        createAndLinkChildRoomIfMissing(patientId, spaceRoomId, AgoraRoomType.TASKS, "Patient Tasks", "Patient clinical workflow tasks", childRoomIds);
        createAndLinkChildRoomIfMissing(patientId, spaceRoomId, AgoraRoomType.DISCUSSION, "Patient Discussion", "Patient multidisciplinary discussion", childRoomIds);
        createAndLinkChildRoomIfMissing(patientId, spaceRoomId, AgoraRoomType.DIAGNOSTICS, "Patient Diagnostics", "Patient diagnostic results and reports", childRoomIds);

        // Invite initial members if provided
        if (request.getInitialMembers() != null && !request.getInitialMembers().isEmpty()) {
            inviteMembersToPatientHierarchy(spaceRoomId, childRoomIds.values(), request.getInitialMembers(), request.getSecurityContext());
        }

        return new AgoraPatientSpaceResponse(spaceRoomId, patientId, childRoomIds);
    }

    /**
     * Creates or resolves a Practitioner collaboration Space.
     *
     * @param request the space request
     * @return the created or existing Space room ID
     */
    public String createPractitionerSpace(AgoraSpaceRequest request) {
        Objects.requireNonNull(request, "AgoraSpaceRequest must not be null");
        String practitionerId = Objects.requireNonNull(request.getHarmoniaResourceId(), "practitionerId must not be null");

        evaluateAuthorization(RESOURCE_TYPE_PRACTITIONER, practitionerId, ThemisAction.CREATE, request.getSecurityContext());

        LOGGER.info("Provisioning Practitioner Space for practitionerId={}", practitionerId);

        Optional<AgoraMappingEntity> existing = mappingRepository
                .findByHarmoniaResourceTypeAndHarmoniaResourceIdAndMatrixEntityType(
                        RESOURCE_TYPE_PRACTITIONER, practitionerId, MATRIX_ENTITY_SPACE
                );

        if (existing.isPresent() && STATUS_ACTIVE.equalsIgnoreCase(existing.get().getStatus())) {
            return existing.get().getMatrixEntityId();
        }

        String spaceTitle = "Practitioner Space [" + practitionerId + "]";
        String topic = request.getTopic() != null ? request.getTopic() : "Practitioner collaboration space";

        MatrixRoomDto space = matrixClientAdapter.createSpace(spaceTitle, topic, true);
        String spaceId = space.getRoomId();

        saveOrUpdateMapping(RESOURCE_TYPE_PRACTITIONER, practitionerId, MATRIX_ENTITY_SPACE, spaceId, STATUS_ACTIVE);

        if (request.getInitialMembers() != null && !request.getInitialMembers().isEmpty()) {
            for (String member : request.getInitialMembers()) {
                matrixClientAdapter.inviteUser(spaceId, member, "Initial practitioner space member");
            }
        }

        return spaceId;
    }

    /**
     * Creates or resolves a PractitionerRole child collaboration room, optionally linking
     * it to a parent Practitioner Space.
     *
     * @param request the room request
     * @return the created or existing room ID
     */
    public String createPractitionerRoleRoom(AgoraRoomRequest request) {
        Objects.requireNonNull(request, "AgoraRoomRequest must not be null");
        String roleId = Objects.requireNonNull(request.getHarmoniaResourceId(), "practitionerRoleId must not be null");

        evaluateAuthorization(RESOURCE_TYPE_PRACTITIONER_ROLE, roleId, ThemisAction.CREATE, request.getSecurityContext());

        LOGGER.info("Provisioning PractitionerRole room for roleId={}", roleId);

        Optional<AgoraMappingEntity> existing = mappingRepository
                .findByHarmoniaResourceTypeAndHarmoniaResourceIdAndMatrixEntityType(
                        RESOURCE_TYPE_PRACTITIONER_ROLE, roleId, MATRIX_ENTITY_ROOM
                );

        if (existing.isPresent() && STATUS_ACTIVE.equalsIgnoreCase(existing.get().getStatus())) {
            return existing.get().getMatrixEntityId();
        }

        String roomTitle = (request.getDisplayName() != null && !request.getDisplayName().isBlank())
                ? request.getDisplayName()
                : "Practitioner Role [" + roleId + "]";
        String topic = request.getTopic() != null ? request.getTopic() : "Practitioner role collaboration room";

        MatrixRoomDto room = matrixClientAdapter.createRoom(roomTitle, topic, true);
        String roomId = room.getRoomId();

        // Hierarchical link if parent Space is specified or resolved
        String parentSpaceId = request.getSpaceId();
        if (parentSpaceId == null && request.getMetadata().containsKey("practitionerId")) {
            String practitionerId = String.valueOf(request.getMetadata().get("practitionerId"));
            parentSpaceId = mappingRepository.findByHarmoniaResourceTypeAndHarmoniaResourceIdAndMatrixEntityType(
                    RESOURCE_TYPE_PRACTITIONER, practitionerId, MATRIX_ENTITY_SPACE
            ).map(AgoraMappingEntity::getMatrixEntityId).orElse(null);
        }

        if (parentSpaceId != null && !parentSpaceId.isBlank()) {
            LOGGER.info("Linking PractitionerRole room roomId={} to parent Space spaceId={}", roomId, parentSpaceId);
            matrixClientAdapter.linkChildRoom(parentSpaceId, roomId, Collections.emptyList());
        }

        saveOrUpdateMapping(RESOURCE_TYPE_PRACTITIONER_ROLE, roleId, MATRIX_ENTITY_ROOM, roomId, STATUS_ACTIVE);

        if (request.getInitialMembers() != null && !request.getInitialMembers().isEmpty()) {
            for (String member : request.getInitialMembers()) {
                matrixClientAdapter.inviteUser(roomId, member, "Initial practitioner role room member");
            }
        }

        return roomId;
    }

    /**
     * Creates or resolves a Group collaboration room.
     *
     * @param request the room request
     * @return the created or existing room ID
     */
    public String createGroupRoom(AgoraRoomRequest request) {
        Objects.requireNonNull(request, "AgoraRoomRequest must not be null");
        String groupId = Objects.requireNonNull(request.getHarmoniaResourceId(), "groupId must not be null");

        evaluateAuthorization(RESOURCE_TYPE_GROUP, groupId, ThemisAction.CREATE, request.getSecurityContext());

        LOGGER.info("Provisioning Group collaboration room for groupId={}", groupId);

        Optional<AgoraMappingEntity> existing = mappingRepository
                .findByHarmoniaResourceTypeAndHarmoniaResourceIdAndMatrixEntityType(
                        RESOURCE_TYPE_GROUP, groupId, MATRIX_ENTITY_ROOM
                );

        if (existing.isPresent() && STATUS_ACTIVE.equalsIgnoreCase(existing.get().getStatus())) {
            return existing.get().getMatrixEntityId();
        }

        String title = (request.getDisplayName() != null && !request.getDisplayName().isBlank())
                ? request.getDisplayName()
                : "Group [" + groupId + "]";
        String topic = request.getTopic() != null ? request.getTopic() : "Group collaboration room";

        MatrixRoomDto room = matrixClientAdapter.createRoom(title, topic, true);
        String roomId = room.getRoomId();

        saveOrUpdateMapping(RESOURCE_TYPE_GROUP, groupId, MATRIX_ENTITY_ROOM, roomId, STATUS_ACTIVE);

        if (request.getInitialMembers() != null && !request.getInitialMembers().isEmpty()) {
            for (String member : request.getInitialMembers()) {
                matrixClientAdapter.inviteUser(roomId, member, "Initial group room member");
            }
        }

        return roomId;
    }

    /**
     * Soft-retires and archives collaboration rooms/spaces associated with a resource.
     * Applies read-only power levels and updates Mnemosyne mapping status to ARCHIVED.
     *
     * @param harmoniaResourceType the resource type (e.g. PATIENT)
     * @param harmoniaResourceId the resource ID
     * @param context the security context
     */
    public void archiveSpace(String harmoniaResourceType, String harmoniaResourceId, ThemisSecurityContext context) {
        Objects.requireNonNull(harmoniaResourceType, "harmoniaResourceType must not be null");
        Objects.requireNonNull(harmoniaResourceId, "harmoniaResourceId must not be null");

        evaluateAuthorization(harmoniaResourceType, harmoniaResourceId, ThemisAction.UPDATE, context);

        LOGGER.info("Archiving collaboration space for resourceType={}, resourceId={}",
                harmoniaResourceType, harmoniaResourceId);

        List<AgoraMappingEntity> mappings = mappingRepository
                .findByHarmoniaResourceTypeAndHarmoniaResourceId(harmoniaResourceType, harmoniaResourceId);

        MatrixPowerLevelsDto readOnlyLevels = MatrixPowerLevelsDto.builder()
                .eventsDefault(50)
                .usersDefault(0)
                .build();

        for (AgoraMappingEntity mapping : mappings) {
            String matrixId = mapping.getMatrixEntityId();
            try {
                LOGGER.info("Setting read-only power levels on roomId={}", matrixId);
                matrixClientAdapter.setPowerLevels(matrixId, readOnlyLevels);
            } catch (Exception e) {
                LOGGER.warn("Failed to set read-only power levels on roomId={}: {}", matrixId, e.getMessage());
            }

            mapping.setStatus(STATUS_ARCHIVED);
            mappingRepository.save(mapping);
        }
    }

    /**
     * Soft-retires and archives a Patient Space and all its child rooms.
     *
     * @param patientId the patient identifier
     */
    public void archivePatientSpace(String patientId) {
        archivePatientSpace(patientId, null);
    }

    /**
     * Soft-retires and archives a Patient Space and all its child rooms under Themis authorization.
     *
     * @param patientId the patient identifier
     * @param context the security context
     */
    public void archivePatientSpace(String patientId, ThemisSecurityContext context) {
        archiveSpace(RESOURCE_TYPE_PATIENT, patientId, context);
    }

    private void createAndLinkChildRoomIfMissing(
            String patientId,
            String spaceRoomId,
            AgoraRoomType roomType,
            String roomName,
            String roomTopic,
            Map<AgoraRoomType, String> childRoomIds
    ) {
        if (childRoomIds.containsKey(roomType)) {
            return;
        }

        String mappingType = childRoomMappingType(roomType);
        Optional<AgoraMappingEntity> existing = mappingRepository
                .findByHarmoniaResourceTypeAndHarmoniaResourceIdAndMatrixEntityType(
                        RESOURCE_TYPE_PATIENT, patientId, mappingType
                );

        String childRoomId;
        if (existing.isPresent() && STATUS_ACTIVE.equalsIgnoreCase(existing.get().getStatus())) {
            childRoomId = existing.get().getMatrixEntityId();
        } else {
            MatrixRoomDto childRoom = matrixClientAdapter.createRoom(roomName, roomTopic, true);
            childRoomId = childRoom.getRoomId();

            LOGGER.info("Linking child room type={}, childRoomId={} to Space spaceId={}",
                    roomType, childRoomId, spaceRoomId);
            matrixClientAdapter.linkChildRoom(spaceRoomId, childRoomId, Collections.emptyList());

            saveOrUpdateMapping(RESOURCE_TYPE_PATIENT, patientId, mappingType, childRoomId, STATUS_ACTIVE);
        }

        childRoomIds.put(roomType, childRoomId);
    }

    private void inviteMembersToPatientHierarchy(
            String spaceRoomId,
            Collection<String> childRoomIds,
            List<String> members,
            ThemisSecurityContext context
    ) {
        for (String member : members) {
            try {
                matrixClientAdapter.inviteUser(spaceRoomId, member, "Initial space provisioning");
                for (String childRoomId : childRoomIds) {
                    matrixClientAdapter.inviteUser(childRoomId, member, "Initial child room provisioning");
                }
            } catch (Exception e) {
                LOGGER.warn("Failed to invite member={} during space hierarchy creation: {}", member, e.getMessage());
            }
        }
    }

    private void saveOrUpdateMapping(
            String harmoniaResourceType,
            String harmoniaResourceId,
            String matrixEntityType,
            String matrixEntityId,
            String status
    ) {
        Optional<AgoraMappingEntity> existingOpt = mappingRepository
                .findByHarmoniaResourceTypeAndHarmoniaResourceIdAndMatrixEntityType(
                        harmoniaResourceType, harmoniaResourceId, matrixEntityType
                );

        if (existingOpt.isPresent()) {
            AgoraMappingEntity existing = existingOpt.get();
            existing.setMatrixEntityId(matrixEntityId);
            existing.setStatus(status);
            mappingRepository.save(existing);
        } else {
            AgoraMappingEntity entity = new AgoraMappingEntity(
                    harmoniaResourceType,
                    harmoniaResourceId,
                    matrixEntityType,
                    matrixEntityId,
                    status
            );
            mappingRepository.save(entity);
        }
    }

    private String childRoomMappingType(AgoraRoomType roomType) {
        return "ROOM_" + roomType.name();
    }

    private void evaluateAuthorization(
            String resourceType,
            String resourceId,
            ThemisAction action,
            ThemisSecurityContext context
    ) {
        if (themisAuthorizer == null) {
            return;
        }

        ThemisPrincipal principal = (context != null && context.requestingPrincipal() != null)
                ? context.requestingPrincipal()
                : ThemisPrincipal.system("agora-lifecycle-service");

        Set<ThemisAuthority> grantedAuthorities = AgoraSecurityUtils.extractAuthorities(context);

        ThemisSecurityContext secContext = (context != null)
                ? context
                : ThemisSecurityContext.fromPrincipal(principal, UUID.randomUUID().toString());

        ThemisResource target = ThemisResource.builder()
                .resourceType(resourceType)
                .resourceId(resourceId)
                .securityDomain(AgoraCollaborationPolicy.DOMAIN)
                .securityLabels(Set.of(ThemisSecurityLabel.of(AgoraCollaborationPolicy.DOMAIN)))
                .build();

        ThemisAuthorizationRequest authRequest = ThemisAuthorizationRequest.builder()
                .principal(principal)
                .authorities(grantedAuthorities)
                .action(action)
                .target(target)
                .context(secContext)
                .build();

        ThemisAuthorizationDecision decision = themisAuthorizer.authorize(authRequest);
        if (decision == null || decision.isDenied()) {
            String reason = decision != null ? decision.reason().name() : "DEFAULT_DENY";
            LOGGER.warn("Themis denied collaboration lifecycle action={} for resourceType={}, resourceId={}, reason={}",
                    action, resourceType, resourceId, reason);
            throw new SecurityException("Themis security authorization denied collaboration lifecycle operation: " + reason);
        }
    }
}
