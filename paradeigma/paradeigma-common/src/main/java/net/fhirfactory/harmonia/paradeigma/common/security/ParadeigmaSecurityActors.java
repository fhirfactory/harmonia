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

package net.fhirfactory.harmonia.paradeigma.common.security;

import net.fhirfactory.harmonia.model.security.HarmoniaAuthorityEnum;
import net.fhirfactory.harmonia.model.security.HarmoniaRoleEnum;
import net.fhirfactory.harmonia.themis.api.model.PrincipalType;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthority;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisRole;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Standardized security actor fixtures and context factories representing canonical
 * Harmonia roles and Themis permissions for simulation and scenario validation.
 */
public final class ParadeigmaSecurityActors {

    private ParadeigmaSecurityActors() {
        // utility class
    }

    // --- Pre-configured Principals ---

    public static final ThemisPrincipal PROVIDER_STEWARD_PRINCIPAL = new ThemisPrincipal(
            "user-provider-steward-01",
            PrincipalType.HUMAN,
            "harmonia.health.example.org",
            Map.of("displayName", "Dr. Eleanor Vance (Provider Steward)", "department", "Provider Data Governance")
    );

    public static final ThemisPrincipal CLINICIAN_PRINCIPAL = new ThemisPrincipal(
            "user-clinician-01",
            PrincipalType.HUMAN,
            "harmonia.health.example.org",
            Map.of("displayName", "Dr. Frank Bowman (Cardiologist)", "department", "Cardiology")
    );

    public static final ThemisPrincipal SYSTEM_ADMIN_PRINCIPAL = new ThemisPrincipal(
            "user-sysadmin-01",
            PrincipalType.HUMAN,
            "harmonia.health.example.org",
            Map.of("displayName", "Alex Mercer (System Administrator)", "department", "IT Infrastructure")
    );

    public static final ThemisPrincipal INTEGRATION_SERVICE_PRINCIPAL = new ThemisPrincipal(
            "svc-pylai-fhir-ingress",
            PrincipalType.SERVICE,
            "internal.harmonia",
            Map.of("serviceName", "Pylai FHIR Gateway Service", "environment", "simulation")
    );

    public static final ThemisPrincipal READ_ONLY_USER_PRINCIPAL = new ThemisPrincipal(
            "user-auditor-readonly-01",
            PrincipalType.HUMAN,
            "harmonia.health.example.org",
            Map.of("displayName", "Jordan Bell (Compliance Auditor)", "department", "Internal Audit")
    );

    public static final ThemisPrincipal UNAUTHORIZED_PRINCIPAL = new ThemisPrincipal(
            "user-unauthorized-external-01",
            PrincipalType.HUMAN,
            "untrusted.external.org",
            Map.of("displayName", "Eve Malory (Untrusted External User)", "department", "External Guest")
    );

    // --- Role and Authority Sets ---

    public static final Set<HarmoniaRoleEnum> PROVIDER_STEWARD_ROLES = Set.of(
            HarmoniaRoleEnum.PRV_ADM,
            HarmoniaRoleEnum.PRV_SUB,
            HarmoniaRoleEnum.PRV_APR,
            HarmoniaRoleEnum.PRV_RDR
    );

    public static final Set<HarmoniaRoleEnum> CLINICIAN_ROLES = Set.of(
            HarmoniaRoleEnum.PRV_RDR
    );

    public static final Set<HarmoniaRoleEnum> SYSTEM_ADMIN_ROLES = Set.of(
            HarmoniaRoleEnum.SYS_ADM,
            HarmoniaRoleEnum.PRV_ADM
    );

    public static final Set<HarmoniaRoleEnum> INTEGRATION_SERVICE_ROLES = Set.of(
            HarmoniaRoleEnum.SYS_INT,
            HarmoniaRoleEnum.PRV_PROC
    );

    public static final Set<HarmoniaRoleEnum> READ_ONLY_ROLES = Set.of(
            HarmoniaRoleEnum.PRV_RDR
    );

    // --- Context Factories ---

    public static ThemisSecurityContext providerStewardContext() {
        return providerStewardContext(generateCorrelationId());
    }

    public static ThemisSecurityContext providerStewardContext(String correlationId) {
        return ThemisSecurityContext.builder()
                .principal(PROVIDER_STEWARD_PRINCIPAL)
                .correlationId(correlationId)
                .tenantId("tenant-stvincents")
                .clientIp("10.10.10.20")
                .attributes(Map.of(
                        "roleCodes", getRoleCodesString(PROVIDER_STEWARD_ROLES),
                        "authorities", getAuthoritiesString(PROVIDER_STEWARD_ROLES)
                ))
                .build();
    }

    public static ThemisSecurityContext clinicianContext() {
        return clinicianContext(generateCorrelationId());
    }

    public static ThemisSecurityContext clinicianContext(String correlationId) {
        return ThemisSecurityContext.builder()
                .principal(CLINICIAN_PRINCIPAL)
                .correlationId(correlationId)
                .tenantId("tenant-stvincents")
                .clientIp("10.10.10.45")
                .attributes(Map.of(
                        "roleCodes", getRoleCodesString(CLINICIAN_ROLES),
                        "authorities", getAuthoritiesString(CLINICIAN_ROLES)
                ))
                .build();
    }

    public static ThemisSecurityContext systemAdminContext() {
        return systemAdminContext(generateCorrelationId());
    }

    public static ThemisSecurityContext systemAdminContext(String correlationId) {
        return ThemisSecurityContext.builder()
                .principal(SYSTEM_ADMIN_PRINCIPAL)
                .correlationId(correlationId)
                .tenantId("tenant-system")
                .clientIp("127.0.0.1")
                .attributes(Map.of(
                        "roleCodes", getRoleCodesString(SYSTEM_ADMIN_ROLES),
                        "authorities", getAuthoritiesString(SYSTEM_ADMIN_ROLES)
                ))
                .build();
    }

    public static ThemisSecurityContext integrationServiceContext() {
        return integrationServiceContext(generateCorrelationId());
    }

    public static ThemisSecurityContext integrationServiceContext(String correlationId) {
        return ThemisSecurityContext.builder()
                .principal(INTEGRATION_SERVICE_PRINCIPAL)
                .correlationId(correlationId)
                .tenantId("tenant-system")
                .clientIp("10.0.0.1")
                .attributes(Map.of(
                        "roleCodes", getRoleCodesString(INTEGRATION_SERVICE_ROLES),
                        "authorities", getAuthoritiesString(INTEGRATION_SERVICE_ROLES)
                ))
                .build();
    }

    public static ThemisSecurityContext readOnlyContext() {
        return readOnlyContext(generateCorrelationId());
    }

    public static ThemisSecurityContext readOnlyContext(String correlationId) {
        return ThemisSecurityContext.builder()
                .principal(READ_ONLY_USER_PRINCIPAL)
                .correlationId(correlationId)
                .tenantId("tenant-stvincents")
                .clientIp("10.10.10.99")
                .attributes(Map.of(
                        "roleCodes", getRoleCodesString(READ_ONLY_ROLES),
                        "authorities", getAuthoritiesString(READ_ONLY_ROLES)
                ))
                .build();
    }

    public static ThemisSecurityContext unauthorizedContext() {
        return unauthorizedContext(generateCorrelationId());
    }

    public static ThemisSecurityContext unauthorizedContext(String correlationId) {
        return ThemisSecurityContext.builder()
                .principal(UNAUTHORIZED_PRINCIPAL)
                .correlationId(correlationId)
                .tenantId("untrusted-tenant")
                .clientIp("198.51.100.42")
                .attributes(Map.of("roleCodes", "", "authorities", ""))
                .build();
    }

    public static ThemisSecurityContext unauthenticatedContext() {
        return ThemisSecurityContext.anonymous();
    }

    public static ThemisSecurityContext expiredContext() {
        return ThemisSecurityContext.builder()
                .principal(PROVIDER_STEWARD_PRINCIPAL)
                .correlationId(generateCorrelationId())
                .requestedAt(Instant.now().minus(2, ChronoUnit.HOURS))
                .attributes(Map.of(
                        "expired", "true",
                        "roleCodes", getRoleCodesString(PROVIDER_STEWARD_ROLES)
                ))
                .build();
    }

    public static ThemisSecurityContext missingRoleContext(ThemisPrincipal principal, HarmoniaRoleEnum missingRole) {
        Set<HarmoniaRoleEnum> filtered = PROVIDER_STEWARD_ROLES.stream()
                .filter(r -> r != missingRole)
                .collect(Collectors.toSet());
        return ThemisSecurityContext.builder()
                .principal(principal != null ? principal : PROVIDER_STEWARD_PRINCIPAL)
                .correlationId(generateCorrelationId())
                .attributes(Map.of(
                        "roleCodes", getRoleCodesString(filtered),
                        "authorities", getAuthoritiesString(filtered)
                ))
                .build();
    }

    public static ThemisSecurityContext revokedAuthorityContext(ThemisPrincipal principal, HarmoniaAuthorityEnum revokedAuth) {
        Set<ThemisAuthority> remainingAuths = getThemisAuthoritiesForRoles(PROVIDER_STEWARD_ROLES).stream()
                .filter(a -> !a.authorityCode().equalsIgnoreCase(revokedAuth.getCode()))
                .collect(Collectors.toSet());
        String authStr = remainingAuths.stream().map(ThemisAuthority::authorityCode).collect(Collectors.joining(","));
        return ThemisSecurityContext.builder()
                .principal(principal != null ? principal : PROVIDER_STEWARD_PRINCIPAL)
                .correlationId(generateCorrelationId())
                .attributes(Map.of(
                        "roleCodes", getRoleCodesString(PROVIDER_STEWARD_ROLES),
                        "authorities", authStr
                ))
                .build();
    }

    // --- Helper Methods ---

    public static Set<ThemisAuthority> getThemisAuthoritiesForRoles(Set<HarmoniaRoleEnum> roles) {
        if (roles == null) return Collections.emptySet();
        return roles.stream()
                .flatMap(r -> r.getThemisAuthorities().stream())
                .collect(Collectors.toUnmodifiableSet());
    }

    public static Set<ThemisRole> getThemisRoles(Set<HarmoniaRoleEnum> roles) {
        if (roles == null) return Collections.emptySet();
        return roles.stream()
                .map(HarmoniaRoleEnum::toThemisRole)
                .collect(Collectors.toUnmodifiableSet());
    }

    private static String getRoleCodesString(Set<HarmoniaRoleEnum> roles) {
        return roles.stream().map(HarmoniaRoleEnum::getRoleCode).collect(Collectors.joining(","));
    }

    private static String getAuthoritiesString(Set<HarmoniaRoleEnum> roles) {
        return getThemisAuthoritiesForRoles(roles).stream()
                .map(ThemisAuthority::authorityCode)
                .sorted()
                .collect(Collectors.joining(","));
    }

    private static String generateCorrelationId() {
        return "corr-paradeigma-" + UUID.randomUUID().toString().substring(0, 8);
    }
}
