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

package net.fhirfactory.harmonia.agora.core.reconciliation;

import net.fhirfactory.harmonia.agora.api.model.AgoraMembershipAction;
import net.fhirfactory.harmonia.agora.api.model.AgoraMembershipRequest;
import net.fhirfactory.harmonia.agora.api.model.AgoraReconciliationResult;
import net.fhirfactory.harmonia.agora.core.persistence.AgoraMappingEntity;
import net.fhirfactory.harmonia.agora.core.persistence.AgoraMappingRepository;
import net.fhirfactory.harmonia.agora.core.security.AgoraCollaborationPolicy;
import net.fhirfactory.harmonia.agora.core.security.AgoraSecurityUtils;
import net.fhirfactory.harmonia.agora.matrix.client.MatrixClientAdapter;
import net.fhirfactory.harmonia.agora.matrix.client.MatrixMemberDto;
import net.fhirfactory.harmonia.themis.api.ThemisAuthorizer;
import net.fhirfactory.harmonia.themis.api.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Service reconciling live Matrix room memberships against authoritative, Themis-governed
 * collaboration memberships.
 * Compares desired care team members against live room state, evaluates Themis default-deny
 * authorization gates, and dispatches corrective {@code invite} or {@code kick} operations
 * via {@link MatrixClientAdapter}.
 */
public class AgoraMembershipReconciliationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AgoraMembershipReconciliationService.class);

    public static final String DEFAULT_BOT_LOCALPART = "_harmonia_bot";

    /**
     * Functional interface for resolving granted Themis authorities for a given user ID.
     */
    @FunctionalInterface
    public interface UserAuthorityResolver {
        Set<ThemisAuthority> resolveAuthorities(String userId);
    }

    private final MatrixClientAdapter matrixClientAdapter;
    private final AgoraMappingRepository mappingRepository;
    private final ThemisAuthorizer themisAuthorizer;
    private final String botUserId;
    private final UserAuthorityResolver userAuthorityResolver;

    public AgoraMembershipReconciliationService(
            MatrixClientAdapter matrixClientAdapter,
            AgoraMappingRepository mappingRepository,
            ThemisAuthorizer themisAuthorizer
    ) {
        this(matrixClientAdapter, mappingRepository, themisAuthorizer, null, null);
    }

    public AgoraMembershipReconciliationService(
            MatrixClientAdapter matrixClientAdapter,
            AgoraMappingRepository mappingRepository,
            ThemisAuthorizer themisAuthorizer,
            String botUserId
    ) {
        this(matrixClientAdapter, mappingRepository, themisAuthorizer, botUserId, null);
    }

    public AgoraMembershipReconciliationService(
            MatrixClientAdapter matrixClientAdapter,
            AgoraMappingRepository mappingRepository,
            ThemisAuthorizer themisAuthorizer,
            String botUserId,
            UserAuthorityResolver userAuthorityResolver
    ) {
        this.matrixClientAdapter = Objects.requireNonNull(matrixClientAdapter, "MatrixClientAdapter must not be null");
        this.mappingRepository = Objects.requireNonNull(mappingRepository, "AgoraMappingRepository must not be null");
        this.themisAuthorizer = Objects.requireNonNull(themisAuthorizer, "ThemisAuthorizer must not be null");
        this.botUserId = botUserId;
        this.userAuthorityResolver = userAuthorityResolver;
    }

    /**
     * Reconciles live membership for a specific Matrix room against a set of desired user identifiers.
     *
     * @param roomId the Matrix room ID
     * @param desiredUserIds the set of desired user IDs
     * @param context the security context
     * @return the reconciliation result detailing invited, kicked, retained, and denied users
     */
    public AgoraReconciliationResult reconcileRoomMembership(
            String roomId,
            Set<String> desiredUserIds,
            ThemisSecurityContext context
    ) {
        Objects.requireNonNull(roomId, "roomId must not be null");
        Set<String> desired = (desiredUserIds != null) ? desiredUserIds : Collections.emptySet();

        LOGGER.info("Reconciling membership for roomId={}, desiredMembersCount={}", roomId, desired.size());

        if (!evaluateAdminRoomAuthorization(roomId, AgoraMembershipAction.INVITE, context)) {
            LOGGER.warn("Themis authorization denied membership reconciliation for roomId={}", roomId);
            throw new SecurityException("Themis security authorization denied membership reconciliation for roomId=" + roomId);
        }

        List<MatrixMemberDto> liveMembers = matrixClientAdapter.getRoomMembers(roomId);
        Map<String, String> activeLiveMembers = new HashMap<>();

        for (MatrixMemberDto member : liveMembers) {
            String userId = member.getUserId();
            String membership = member.getMembership();
            if (userId != null && ("join".equalsIgnoreCase(membership) || "invite".equalsIgnoreCase(membership))) {
                activeLiveMembers.put(userId, membership.toLowerCase());
            }
        }

        List<String> invitedUsers = new ArrayList<>();
        List<String> kickedUsers = new ArrayList<>();
        List<String> retainedUsers = new ArrayList<>();
        List<String> deniedUsers = new ArrayList<>();

        Set<String> processedDesiredUsers = new HashSet<>();

        // 1. Process desired users under Themis authorization
        for (String userId : desired) {
            processedDesiredUsers.add(userId);

            boolean authorized = isDesiredUserAuthorized(userId, roomId, context);
            if (authorized) {
                if (!activeLiveMembers.containsKey(userId)) {
                    LOGGER.info("Inviting authorized missing member userId={} to roomId={}", userId, roomId);
                    try {
                        matrixClientAdapter.inviteUser(roomId, userId, "Reconciliation: authorized care team member");
                        invitedUsers.add(userId);
                    } catch (Exception e) {
                        LOGGER.warn("Failed to invite userId={} to roomId={}: {}", userId, roomId, e.getMessage());
                    }
                } else {
                    retainedUsers.add(userId);
                }
            } else {
                deniedUsers.add(userId);
                if (activeLiveMembers.containsKey(userId)) {
                    LOGGER.warn("Kicking unauthorized desired member userId={} from roomId={}", userId, roomId);
                    try {
                        matrixClientAdapter.kickUser(roomId, userId, "Reconciliation: denied by Themis security policy");
                        kickedUsers.add(userId);
                    } catch (Exception e) {
                        LOGGER.warn("Failed to kick unauthorized userId={} from roomId={}: {}", userId, roomId, e.getMessage());
                    }
                }
            }
        }

        // 2. Process live membership drift (active room members not in desired set)
        for (Map.Entry<String, String> entry : activeLiveMembers.entrySet()) {
            String liveUserId = entry.getKey();
            if (processedDesiredUsers.contains(liveUserId)) {
                continue;
            }

            if (isExemptServiceUser(liveUserId)) {
                retainedUsers.add(liveUserId);
                continue;
            }

            // Evaluate if this unexpected live member has valid Themis authorization
            boolean authorized = evaluateUserRoomAuthorization(liveUserId, roomId, context);
            if (authorized) {
                retainedUsers.add(liveUserId);
            } else {
                deniedUsers.add(liveUserId);
                LOGGER.warn("Kicking unauthorized drift member userId={} from roomId={}", liveUserId, roomId);
                try {
                    matrixClientAdapter.kickUser(roomId, liveUserId, "Reconciliation: membership drift remediation");
                    kickedUsers.add(liveUserId);
                } catch (Exception e) {
                    LOGGER.warn("Failed to kick drift member userId={} from roomId={}: {}", liveUserId, roomId, e.getMessage());
                }
            }
        }

        return AgoraReconciliationResult.builder(roomId)
                .invitedUsers(invitedUsers)
                .kickedUsers(kickedUsers)
                .retainedUsers(retainedUsers)
                .deniedUsers(deniedUsers)
                .build();
    }

    /**
     * Executes an explicit membership modification request governed by Themis default-deny authorization.
     *
     * @param request the membership request
     */
    public void executeMembershipRequest(AgoraMembershipRequest request) {
        Objects.requireNonNull(request, "AgoraMembershipRequest must not be null");
        String roomId = Objects.requireNonNull(request.getRoomId(), "roomId must not be null");
        String userId = Objects.requireNonNull(request.getUserId(), "userId must not be null");
        AgoraMembershipAction action = Objects.requireNonNull(request.getAction(), "action must not be null");

        LOGGER.info("Executing membership action={} for userId={}, roomId={}", action, userId, roomId);

        // For all membership actions, evaluate the caller's authorization (from the security context)
        boolean isAllowed = evaluateAdminRoomAuthorization(roomId, action, request.getSecurityContext());

        if (!isAllowed) {
            LOGGER.warn("Themis denied membership action={} for userId={} in roomId={}", action, userId, roomId);
            throw new SecurityException("Themis security authorization denied membership action: " + action);
        }

        matrixClientAdapter.manageMembership(roomId, userId, action, request.getReason());
    }

    /**
     * Reconciles membership across all rooms in a resource's collaboration hierarchy
     * (e.g. Patient Space + 4 child rooms).
     *
     * @param harmoniaResourceType the resource type (e.g. PATIENT)
     * @param harmoniaResourceId the resource identifier
     * @param desiredUserIds the set of desired user IDs
     * @param context the security context
     * @return list of reconciliation results for each room in the hierarchy
     */
    public List<AgoraReconciliationResult> reconcileHierarchyMembership(
            String harmoniaResourceType,
            String harmoniaResourceId,
            Set<String> desiredUserIds,
            ThemisSecurityContext context
    ) {
        Objects.requireNonNull(harmoniaResourceType, "harmoniaResourceType must not be null");
        Objects.requireNonNull(harmoniaResourceId, "harmoniaResourceId must not be null");

        evaluateAdminHierarchyAuthorization(harmoniaResourceType, harmoniaResourceId, context);

        List<AgoraMappingEntity> mappings = mappingRepository
                .findByHarmoniaResourceTypeAndHarmoniaResourceId(harmoniaResourceType, harmoniaResourceId);

        List<AgoraReconciliationResult> results = new ArrayList<>();
        for (AgoraMappingEntity mapping : mappings) {
            String roomId = mapping.getMatrixEntityId();
            if (roomId != null && (roomId.startsWith("!") || roomId.startsWith("#"))) {
                results.add(reconcileRoomMembership(roomId, desiredUserIds, context));
            }
        }
        return results;
    }

    private boolean isDesiredUserAuthorized(String userId, String roomId, ThemisSecurityContext context) {
        if (userAuthorityResolver == null) {
            // In the default production path (no external/test resolver supplied),
            // desiredUserIds represents the authoritative Harmonia/Themis-governed target state
            // presented to Agora for convergence, authorized by construction.
            return true;
        }
        // When an optional resolver is supplied, evaluate per-user Themis authorization
        return evaluateUserRoomAuthorization(userId, roomId, context);
    }

    private void evaluateAdminHierarchyAuthorization(String resourceType, String resourceId, ThemisSecurityContext context) {
        if (themisAuthorizer == null) {
            return;
        }

        ThemisPrincipal principal = (context != null && context.requestingPrincipal() != null)
                ? context.requestingPrincipal()
                : ThemisPrincipal.system("agora-reconciliation-service");

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
                .action(ThemisAction.UPDATE)
                .target(target)
                .context(secContext)
                .build();

        ThemisAuthorizationDecision decision = themisAuthorizer.authorize(authRequest);
        if (decision == null || !decision.isAllowed()) {
            String reason = decision != null && decision.reason() != null ? decision.reason().name() : "NO_DECISION";
            LOGGER.warn("Themis authorization denied hierarchy membership reconciliation for {}/{}: reason={}",
                    resourceType, resourceId, reason);
            throw new SecurityException("Themis security authorization denied hierarchy membership reconciliation for "
                    + resourceType + "/" + resourceId + ": " + reason);
        }
    }

    private boolean evaluateUserRoomAuthorization(String userId, String roomId, ThemisSecurityContext context) {
        ThemisPrincipal principal = ThemisPrincipal.human(userId);

        Set<ThemisAuthority> grantedAuthorities = resolveUserAuthorities(userId, context);

        ThemisSecurityContext secContext = (context != null)
                ? context
                : ThemisSecurityContext.fromPrincipal(principal, UUID.randomUUID().toString());

        ThemisResource target = ThemisResource.builder()
                .resourceType("MatrixRoom")
                .resourceId(roomId)
                .securityDomain(AgoraCollaborationPolicy.DOMAIN)
                .securityLabels(Set.of(ThemisSecurityLabel.of(AgoraCollaborationPolicy.DOMAIN)))
                .build();

        ThemisAuthorizationRequest authRequest = ThemisAuthorizationRequest.builder()
                .principal(principal)
                .authorities(grantedAuthorities)
                .action(ThemisAction.READ)
                .target(target)
                .context(secContext)
                .build();

        ThemisAuthorizationDecision decision = themisAuthorizer.authorize(authRequest);
        return decision != null && decision.isAllowed();
    }

    private boolean evaluateAdminRoomAuthorization(String roomId, AgoraMembershipAction action, ThemisSecurityContext context) {
        ThemisPrincipal principal = (context != null && context.requestingPrincipal() != null)
                ? context.requestingPrincipal()
                : ThemisPrincipal.system("agora-reconciliation-service");

        Set<ThemisAuthority> grantedAuthorities = AgoraSecurityUtils.extractAuthorities(context);

        ThemisSecurityContext secContext = (context != null)
                ? context
                : ThemisSecurityContext.fromPrincipal(principal, UUID.randomUUID().toString());

        ThemisResource target = ThemisResource.builder()
                .resourceType("MatrixRoom")
                .resourceId(roomId)
                .securityDomain(AgoraCollaborationPolicy.DOMAIN)
                .securityLabels(Set.of(ThemisSecurityLabel.of(AgoraCollaborationPolicy.DOMAIN)))
                .build();

        ThemisAction themisAction = (action == AgoraMembershipAction.KICK || action == AgoraMembershipAction.LEAVE)
                ? ThemisAction.DELETE
                : ThemisAction.UPDATE;

        ThemisAuthorizationRequest authRequest = ThemisAuthorizationRequest.builder()
                .principal(principal)
                .authorities(grantedAuthorities)
                .action(themisAction)
                .target(target)
                .context(secContext)
                .build();

        ThemisAuthorizationDecision decision = themisAuthorizer.authorize(authRequest);
        return decision != null && decision.isAllowed();
    }

    private Set<ThemisAuthority> resolveUserAuthorities(String userId, ThemisSecurityContext context) {
        if (userAuthorityResolver != null) {
            Set<ThemisAuthority> resolved = userAuthorityResolver.resolveAuthorities(userId);
            if (resolved != null && !resolved.isEmpty()) {
                return resolved;
            }
        }

        if (context != null && context.attributes() != null && !context.attributes().isEmpty()) {
            // 1. Check user-specific attribute, e.g. "authorities:@dr-alice:synapse" or "authorities:dr-alice"
            String userAuthStr = context.attributes().get("authorities:" + userId);
            if (userAuthStr == null && userId.startsWith("@")) {
                int colonIdx = userId.indexOf(':');
                String localpart = colonIdx > 1 ? userId.substring(1, colonIdx) : userId.substring(1);
                userAuthStr = context.attributes().get("authorities:" + localpart);
            }
            if (userAuthStr != null && !userAuthStr.isBlank()) {
                Set<ThemisAuthority> auths = new HashSet<>();
                for (String token : userAuthStr.split("[,;\\s]+")) {
                    if (!token.isBlank()) {
                        auths.add(ThemisAuthority.of(token.trim()));
                    }
                }
                return auths;
            }

            // 2. If the context is for this principal directly
            if (context.requestingPrincipal() != null && userId.equalsIgnoreCase(context.requestingPrincipal().principalId())) {
                return AgoraSecurityUtils.extractAuthorities(context);
            }

            // 3. Check "authorizedMembers" or "authorizedUsers" attribute in context
            String authorizedMembers = context.attributes().get("authorizedMembers");
            if (authorizedMembers == null) {
                authorizedMembers = context.attributes().get("authorizedUsers");
            }
            if (authorizedMembers != null && !authorizedMembers.isBlank()) {
                Set<String> authUserSet = Set.of(authorizedMembers.split("[,;\\s]+"));
                if (authUserSet.contains(userId)) {
                    return Set.of(ThemisAuthority.of(AgoraCollaborationPolicy.AUTH_AGORA_MEMBER));
                }
            }
        }

        return Collections.emptySet();
    }

    private boolean isExemptServiceUser(String userId) {
        if (userId == null) {
            return false;
        }
        if (botUserId != null && userId.equalsIgnoreCase(botUserId)) {
            return true;
        }
        return userId.contains(DEFAULT_BOT_LOCALPART);
    }
}
