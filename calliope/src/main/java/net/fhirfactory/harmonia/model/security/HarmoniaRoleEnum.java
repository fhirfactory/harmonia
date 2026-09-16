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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.fhirfactory.harmonia.model.security;

import net.fhirfactory.harmonia.themis.api.model.ThemisAuthority;
import net.fhirfactory.harmonia.themis.api.model.ThemisRole;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Standard mnemonic role catalogue mapped to granular Themis authorities.
 */
public enum HarmoniaRoleEnum {
    PRV_RDR("PRV_RDR", "Provider Registry Reader",
            HarmoniaAuthorityEnum.PROVIDER_READ,
            HarmoniaAuthorityEnum.PROVIDER_SEARCH),

    PRV_SUB("PRV_SUB", "Provider Registry Change Submitter",
            HarmoniaAuthorityEnum.PROVIDER_CHANGE_SUBMIT),

    PRV_PROC("PRV_PROC", "Provider Registry Change Processor",
            HarmoniaAuthorityEnum.PROVIDER_CHANGE_PROCESS,
            HarmoniaAuthorityEnum.PROVIDER_RESOURCE_VALIDATE),

    PRV_APR("PRV_APR", "Provider Registry Approver",
            HarmoniaAuthorityEnum.PROVIDER_CHANGE_APPROVE),

    PRV_ADM("PRV_ADM", "Provider Registry Administrator",
            HarmoniaAuthorityEnum.PROVIDER_ADMIN,
            HarmoniaAuthorityEnum.PROVIDER_READ,
            HarmoniaAuthorityEnum.PROVIDER_SEARCH,
            HarmoniaAuthorityEnum.PROVIDER_CHANGE_SUBMIT,
            HarmoniaAuthorityEnum.PROVIDER_CHANGE_PROCESS,
            HarmoniaAuthorityEnum.PROVIDER_CHANGE_APPROVE,
            HarmoniaAuthorityEnum.PROVIDER_RESOURCE_CREATE,
            HarmoniaAuthorityEnum.PROVIDER_RESOURCE_UPDATE,
            HarmoniaAuthorityEnum.PROVIDER_RESOURCE_DELETE),

    AUD_RDR("AUD_RDR", "Audit Reader",
            HarmoniaAuthorityEnum.AUDIT_READ),

    SYS_INT("SYS_INT", "System Integration Service",
            HarmoniaAuthorityEnum.SYSTEM_INTEGRATION),

    SYS_ADM("SYS_ADM", "System Administrator",
            HarmoniaAuthorityEnum.SYSTEM_ADMIN);

    private final String roleCode;
    private final String description;
    private final Set<HarmoniaAuthorityEnum> authorities;

    HarmoniaRoleEnum(String roleCode, String description, HarmoniaAuthorityEnum... authorities) {
        this.roleCode = roleCode;
        this.description = description;
        this.authorities = Set.of(authorities);
    }

    public String getRoleCode() {
        return roleCode;
    }

    public String getDescription() {
        return description;
    }

    public Set<HarmoniaAuthorityEnum> getAuthorities() {
        return authorities;
    }

    public Set<ThemisAuthority> getThemisAuthorities() {
        return authorities.stream()
                .map(HarmoniaAuthorityEnum::toThemisAuthority)
                .collect(Collectors.toUnmodifiableSet());
    }

    public ThemisRole toThemisRole() {
        return ThemisRole.of(roleCode, description, getThemisAuthorities());
    }

    public static Optional<HarmoniaRoleEnum> fromCode(String code) {
        if (code == null) {
            return Optional.empty();
        }
        return Arrays.stream(values())
                .filter(r -> r.roleCode.equalsIgnoreCase(code.trim()))
                .findFirst();
    }
}
