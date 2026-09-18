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

package net.fhirfactory.harmonia.agora.core.security;

import net.fhirfactory.harmonia.model.security.HarmoniaAuthorityEnum;
import net.fhirfactory.harmonia.model.security.HarmoniaRoleEnum;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthority;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisRole;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;
import net.fhirfactory.harmonia.themis.core.constants.HarmoniaSecurityConstants;

import java.util.*;

/**
 * Security utilities for extracting granted authorities and constructing security contexts
 * for Agora collaboration services in compliance with Harmonia Themis conventions.
 */
public final class AgoraSecurityUtils {

    private AgoraSecurityUtils() {}

    /**
     * Extracts granted Themis authorities from the given security context attributes.
     * Parses both explicit "authorities" tokens and mnemonic "roleCodes"/"roles" tokens.
     *
     * @param context the security context
     * @return unmodifiable set of granted authorities (empty if context or attributes are null/missing)
     */
    public static Set<ThemisAuthority> extractAuthorities(ThemisSecurityContext context) {
        if (context == null || context.attributes() == null || context.attributes().isEmpty()) {
            return Collections.emptySet();
        }

        Set<ThemisAuthority> authorities = new HashSet<>();

        // 1. Extract from "authorities" attribute (comma, semicolon, or whitespace separated)
        String authStr = context.attributes().get("authorities");
        if (authStr != null && !authStr.isBlank()) {
            for (String token : authStr.split("[,;\\s]+")) {
                if (!token.isBlank()) {
                    String trimmed = token.trim();
                    Optional<HarmoniaAuthorityEnum> authEnum = HarmoniaAuthorityEnum.fromCode(trimmed);
                    if (authEnum.isPresent()) {
                        authorities.add(authEnum.get().toThemisAuthority());
                    } else {
                        authorities.add(ThemisAuthority.of(trimmed));
                    }
                }
            }
        }

        // 2. Extract from "roleCodes" or "roles" attribute
        String roleStr = context.attributes().get("roleCodes");
        if (roleStr == null || roleStr.isBlank()) {
            roleStr = context.attributes().get("roles");
        }
        if (roleStr != null && !roleStr.isBlank()) {
            for (String roleToken : roleStr.split("[,;\\s]+")) {
                if (!roleToken.isBlank()) {
                    String trimmed = roleToken.trim();
                    Optional<HarmoniaRoleEnum> roleEnum = HarmoniaRoleEnum.fromCode(trimmed);
                    if (roleEnum.isPresent()) {
                        authorities.addAll(roleEnum.get().getThemisAuthorities());
                    } else {
                        Optional<ThemisRole> themisRole = HarmoniaSecurityConstants.getRole(trimmed);
                        themisRole.ifPresent(r -> authorities.addAll(r.authorities()));
                    }
                }
            }
        }

        return Collections.unmodifiableSet(authorities);
    }

    /**
     * Creates a ThemisSecurityContext with explicit granted authorities.
     *
     * @param principal the requesting principal
     * @param correlationId correlation identifier
     * @param authorities authority codes to grant
     * @return populated security context
     */
    public static ThemisSecurityContext contextWithAuthorities(ThemisPrincipal principal, String correlationId, String... authorities) {
        String authStr = authorities != null ? String.join(",", authorities) : "";
        return ThemisSecurityContext.builder()
                .principal(principal)
                .correlationId(correlationId != null ? correlationId : UUID.randomUUID().toString())
                .attributes(Map.of("authorities", authStr))
                .build();
    }
}
