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

package net.fhirfactory.harmonia.agora.core.identity;

import net.fhirfactory.harmonia.agora.core.persistence.AgoraMappingEntity;
import net.fhirfactory.harmonia.agora.core.persistence.AgoraMappingRepository;
import net.fhirfactory.harmonia.agora.core.security.AgoraCollaborationPolicy;
import net.fhirfactory.harmonia.agora.core.security.AgoraSecurityUtils;
import net.fhirfactory.harmonia.agora.matrix.admin.MatrixUserDto;
import net.fhirfactory.harmonia.agora.matrix.admin.SynapseAdminException;
import net.fhirfactory.harmonia.agora.matrix.admin.SynapseAdministrationGateway;
import net.fhirfactory.harmonia.themis.api.ThemisAuthorizer;
import net.fhirfactory.harmonia.themis.api.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Service for deterministic identity resolution and local Matrix account provisioning.
 * Derives opaque, PHI-minimized localparts (e.g. {@code @_harmonia_p_<uuid>:synapse})
 * from Harmonia principal identifiers, provisions local accounts via {@link SynapseAdministrationGateway},
 * and persists durable mappings in Mnemosyne.
 */
public class AgoraIdentityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AgoraIdentityService.class);

    public static final String DEFAULT_SERVER_NAME = "synapse";
    public static final String LOCALPART_PREFIX = "_harmonia_p_";
    public static final String RESOURCE_TYPE_PRACTITIONER = "PRACTITIONER";
    public static final String MATRIX_ENTITY_USER = "USER";
    public static final int DEFAULT_MAX_RETRIES = 3;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final SynapseAdministrationGateway adminGateway;
    private final AgoraMappingRepository mappingRepository;
    private final ThemisAuthorizer themisAuthorizer;
    private final String serverName;
    private final int maxRetries;

    public AgoraIdentityService(
            SynapseAdministrationGateway adminGateway,
            AgoraMappingRepository mappingRepository
    ) {
        this(adminGateway, mappingRepository, null, DEFAULT_SERVER_NAME, DEFAULT_MAX_RETRIES);
    }

    public AgoraIdentityService(
            SynapseAdministrationGateway adminGateway,
            AgoraMappingRepository mappingRepository,
            String serverName
    ) {
        this(adminGateway, mappingRepository, null, serverName, DEFAULT_MAX_RETRIES);
    }

    public AgoraIdentityService(
            SynapseAdministrationGateway adminGateway,
            AgoraMappingRepository mappingRepository,
            ThemisAuthorizer themisAuthorizer,
            String serverName
    ) {
        this(adminGateway, mappingRepository, themisAuthorizer, serverName, DEFAULT_MAX_RETRIES);
    }

    public AgoraIdentityService(
            SynapseAdministrationGateway adminGateway,
            AgoraMappingRepository mappingRepository,
            ThemisAuthorizer themisAuthorizer,
            String serverName,
            int maxRetries
    ) {
        this.adminGateway = Objects.requireNonNull(adminGateway, "SynapseAdministrationGateway must not be null");
        this.mappingRepository = Objects.requireNonNull(mappingRepository, "AgoraMappingRepository must not be null");
        this.themisAuthorizer = themisAuthorizer;
        this.serverName = (serverName != null && !serverName.isBlank()) ? serverName : DEFAULT_SERVER_NAME;
        this.maxRetries = Math.max(1, maxRetries);
    }

    /**
     * Deterministically derives an opaque localpart for a given Harmonia principal identifier.
     *
     * @param principalId the Harmonia principal identifier
     * @return the localpart, formatted as {@code _harmonia_p_<uuid>}
     */
    public String deriveLocalpart(String principalId) {
        UUID uuid = deriveDeterministicUuid(principalId);
        return LOCALPART_PREFIX + uuid;
    }

    /**
     * Deterministically derives a fully-qualified Matrix user identifier for a given Harmonia principal identifier.
     *
     * @param principalId the Harmonia principal identifier
     * @return the Matrix user identifier, formatted as {@code @_harmonia_p_<uuid>:<serverName>}
     */
    public String deriveMatrixUserId(String principalId) {
        return deriveMatrixUserId(principalId, this.serverName);
    }

    /**
     * Deterministically derives a fully-qualified Matrix user identifier for a given Harmonia principal identifier
     * and homeserver domain.
     *
     * @param principalId the Harmonia principal identifier
     * @param customServerName the homeserver domain
     * @return the Matrix user identifier, formatted as {@code @_harmonia_p_<uuid>:<customServerName>}
     */
    public String deriveMatrixUserId(String principalId, String customServerName) {
        String sName = (customServerName != null && !customServerName.isBlank()) ? customServerName : this.serverName;
        return "@" + deriveLocalpart(principalId) + ":" + sName;
    }

    /**
     * Converts or deterministically hashes a Harmonia principal identifier into a UUID.
     * If the identifier contains or is already a valid UUID string, that UUID is returned.
     * Otherwise, a deterministic type-3 (MD5-based) UUID is generated from UTF-8 bytes.
     *
     * @param principalId the principal identifier
     * @return deterministic UUID
     */
    public static UUID deriveDeterministicUuid(String principalId) {
        if (principalId == null || principalId.isBlank()) {
            throw new IllegalArgumentException("principalId must not be null or blank");
        }
        String clean = principalId.trim();
        if (clean.contains("/")) {
            clean = clean.substring(clean.lastIndexOf('/') + 1);
        }
        try {
            return UUID.fromString(clean);
        } catch (IllegalArgumentException ignored) {
            return UUID.nameUUIDFromBytes(clean.getBytes(StandardCharsets.UTF_8));
        }
    }

    /**
     * Provisions a local Matrix account for the specified practitioner principal.
     *
     * @param principalId the practitioner identifier
     * @param displayName sanitized display name
     * @return the provisioned user details
     */
    public MatrixUserDto provisionUser(String principalId, String displayName) {
        return provisionUser(RESOURCE_TYPE_PRACTITIONER, principalId, displayName, null);
    }

    /**
     * Provisions a local Matrix account for the specified practitioner principal under Themis authorization.
     *
     * @param principalId the practitioner identifier
     * @param displayName sanitized display name
     * @param context the security context
     * @return the provisioned user details
     */
    public MatrixUserDto provisionUser(String principalId, String displayName, ThemisSecurityContext context) {
        return provisionUser(RESOURCE_TYPE_PRACTITIONER, principalId, displayName, context);
    }

    /**
     * Provisions a local Matrix account for the specified principal under Themis authorization.
     *
     * @param harmoniaResourceType the Harmonia resource type (e.g. PRACTITIONER)
     * @param principalId the principal identifier
     * @param displayName sanitized display name
     * @param context the security context
     * @return the provisioned user details
     */
    public MatrixUserDto provisionUser(
            String harmoniaResourceType,
            String principalId,
            String displayName,
            ThemisSecurityContext context
    ) {
        Objects.requireNonNull(principalId, "principalId must not be null");
        String resType = (harmoniaResourceType != null && !harmoniaResourceType.isBlank())
                ? harmoniaResourceType
                : RESOURCE_TYPE_PRACTITIONER;

        evaluateThemisAuthorization(resType, principalId, context);

        String matrixUserId = deriveMatrixUserId(principalId);
        LOGGER.info("Provisioning Matrix local user for resourceType={}, principalId={}, userId={}",
                resType, principalId, matrixUserId);

        String securePassword = generateSecurePassword();

        MatrixUserDto userRequest = MatrixUserDto.builder()
                .userId(matrixUserId)
                .displayName(displayName != null ? displayName : deriveLocalpart(principalId))
                .password(securePassword)
                .admin(false)
                .deactivated(false)
                .build();

        MatrixUserDto provisioned = executeWithRetry(userRequest);

        // Record or update durable mapping
        Optional<AgoraMappingEntity> existingOpt = mappingRepository
                .findByHarmoniaResourceTypeAndHarmoniaResourceIdAndMatrixEntityType(resType, principalId, MATRIX_ENTITY_USER);

        if (existingOpt.isPresent()) {
            AgoraMappingEntity existing = existingOpt.get();
            existing.setMatrixEntityId(matrixUserId);
            existing.setStatus("ACTIVE");
            mappingRepository.save(existing);
        } else {
            AgoraMappingEntity mapping = new AgoraMappingEntity(
                    resType,
                    principalId,
                    MATRIX_ENTITY_USER,
                    matrixUserId,
                    "ACTIVE"
            );
            mappingRepository.save(mapping);
        }

        LOGGER.info("Durable mapping persisted for principalId={}, userId={}", principalId, matrixUserId);
        return provisioned;
    }

    /**
     * Resolves the mapped Matrix user ID for a principal if already provisioned.
     *
     * @param principalId the principal identifier
     * @return optional containing the Matrix user ID if mapped
     */
    public Optional<String> getMappedUserId(String principalId) {
        return getMappedUserId(RESOURCE_TYPE_PRACTITIONER, principalId);
    }

    /**
     * Resolves the mapped Matrix user ID for a specific resource type and identifier.
     *
     * @param harmoniaResourceType the resource type
     * @param principalId the principal identifier
     * @return optional containing the Matrix user ID if mapped
     */
    public Optional<String> getMappedUserId(String harmoniaResourceType, String principalId) {
        if (principalId == null || principalId.isBlank()) {
            return Optional.empty();
        }
        String resType = (harmoniaResourceType != null && !harmoniaResourceType.isBlank())
                ? harmoniaResourceType
                : RESOURCE_TYPE_PRACTITIONER;

        return mappingRepository.findByHarmoniaResourceTypeAndHarmoniaResourceIdAndMatrixEntityType(
                resType, principalId, MATRIX_ENTITY_USER
        ).map(AgoraMappingEntity::getMatrixEntityId);
    }

    /**
     * Retrieves an existing mapped Matrix user ID or provisions a new account if none exists.
     *
     * @param principalId the principal identifier
     * @param displayName display name
     * @return the Matrix user ID
     */
    public String getOrCreateUser(String principalId, String displayName) {
        return getOrCreateUser(principalId, displayName, null);
    }

    /**
     * Retrieves an existing mapped Matrix user ID or provisions a new account if none exists under Themis authorization.
     *
     * @param principalId the principal identifier
     * @param displayName display name
     * @param context the security context
     * @return the Matrix user ID
     */
    public String getOrCreateUser(String principalId, String displayName, ThemisSecurityContext context) {
        return getMappedUserId(principalId)
                .orElseGet(() -> provisionUser(principalId, displayName, context).getUserId());
    }

    /**
     * Deactivates a provisioned local Matrix account.
     *
     * @param principalId the principal identifier
     * @param erase whether to erase user data
     */
    public void deactivateUser(String principalId, boolean erase) {
        deactivateUser(principalId, erase, null);
    }

    /**
     * Deactivates a provisioned local Matrix account under Themis authorization.
     *
     * @param principalId the principal identifier
     * @param erase whether to erase user data
     * @param context the security context
     */
    public void deactivateUser(String principalId, boolean erase, ThemisSecurityContext context) {
        Objects.requireNonNull(principalId, "principalId must not be null");
        evaluateDeactivationAuthorization(RESOURCE_TYPE_PRACTITIONER, principalId, context);
        String matrixUserId = deriveMatrixUserId(principalId);

        LOGGER.info("Deactivating user for principalId={}, userId={}, erase={}", principalId, matrixUserId, erase);
        adminGateway.deactivateUser(matrixUserId, erase);

        mappingRepository.findByHarmoniaResourceTypeAndHarmoniaResourceIdAndMatrixEntityType(
                RESOURCE_TYPE_PRACTITIONER, principalId, MATRIX_ENTITY_USER
        ).ifPresent(entity -> {
            entity.setStatus("DEACTIVATED");
            mappingRepository.save(entity);
        });
    }

    private MatrixUserDto executeWithRetry(MatrixUserDto userRequest) {
        SynapseAdminException lastException = null;
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                return adminGateway.createOrUpdateUser(userRequest);
            } catch (SynapseAdminException e) {
                lastException = e;
                LOGGER.warn("Synapse user provisioning attempt {}/{} failed for userId={}, status={}: {}",
                        attempt, maxRetries, userRequest.getUserId(), e.getHttpStatus(), e.getMessage());
                if (attempt < maxRetries) {
                    try {
                        Thread.sleep(50L * attempt);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new IllegalStateException("Interrupted during provisioning retry", ie);
                    }
                }
            }
        }
        throw new IllegalStateException("Failed to provision Synapse user after " + maxRetries + " attempts", lastException);
    }

    private void evaluateThemisAuthorization(
            String resourceType,
            String principalId,
            ThemisSecurityContext context
    ) {
        if (themisAuthorizer == null) {
            return;
        }

        ThemisPrincipal principal = (context != null && context.requestingPrincipal() != null)
                ? context.requestingPrincipal()
                : ThemisPrincipal.system("agora-identity-service");

        Set<ThemisAuthority> grantedAuthorities = AgoraSecurityUtils.extractAuthorities(context);

        ThemisSecurityContext secContext = (context != null)
                ? context
                : ThemisSecurityContext.fromPrincipal(principal, UUID.randomUUID().toString());

        ThemisResource target = ThemisResource.builder()
                .resourceType(resourceType)
                .resourceId(principalId)
                .securityDomain(AgoraCollaborationPolicy.DOMAIN)
                .securityLabels(Set.of(ThemisSecurityLabel.of(AgoraCollaborationPolicy.DOMAIN)))
                .build();

        ThemisAuthorizationRequest authRequest = ThemisAuthorizationRequest.builder()
                .principal(principal)
                .authorities(grantedAuthorities)
                .action(ThemisAction.CREATE)
                .target(target)
                .context(secContext)
                .build();

        ThemisAuthorizationDecision decision = themisAuthorizer.authorize(authRequest);
        if (decision == null || decision.isDenied()) {
            String reason = decision != null ? decision.reason().name() : "DEFAULT_DENY";
            LOGGER.warn("Themis denied identity provisioning for resourceType={}, principalId={}, reason={}",
                    resourceType, principalId, reason);
            throw new SecurityException("Themis security authorization denied user provisioning: " + reason);
        }
    }

    private void evaluateDeactivationAuthorization(
            String resourceType,
            String principalId,
            ThemisSecurityContext context
    ) {
        if (themisAuthorizer == null) {
            return;
        }

        ThemisPrincipal principal = (context != null && context.requestingPrincipal() != null)
                ? context.requestingPrincipal()
                : ThemisPrincipal.system("agora-identity-service");

        Set<ThemisAuthority> grantedAuthorities = AgoraSecurityUtils.extractAuthorities(context);

        ThemisSecurityContext secContext = (context != null)
                ? context
                : ThemisSecurityContext.fromPrincipal(principal, UUID.randomUUID().toString());

        ThemisResource target = ThemisResource.builder()
                .resourceType(resourceType)
                .resourceId(principalId)
                .securityDomain(AgoraCollaborationPolicy.DOMAIN)
                .securityLabels(Set.of(ThemisSecurityLabel.of(AgoraCollaborationPolicy.DOMAIN)))
                .build();

        ThemisAuthorizationRequest authRequest = ThemisAuthorizationRequest.builder()
                .principal(principal)
                .authorities(grantedAuthorities)
                .action(ThemisAction.DELETE)
                .target(target)
                .context(secContext)
                .build();

        ThemisAuthorizationDecision decision = themisAuthorizer.authorize(authRequest);
        if (decision == null || decision.isDenied()) {
            String reason = decision != null ? decision.reason().name() : "DEFAULT_DENY";
            LOGGER.warn("Themis denied identity deactivation for resourceType={}, principalId={}, reason={}",
                    resourceType, principalId, reason);
            throw new SecurityException("Themis security authorization denied user deactivation: " + reason);
        }
    }

    private String generateSecurePassword() {
        byte[] bytes = new byte[24];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public String getServerName() {
        return serverName;
    }

    public int getMaxRetries() {
        return maxRetries;
    }
}
