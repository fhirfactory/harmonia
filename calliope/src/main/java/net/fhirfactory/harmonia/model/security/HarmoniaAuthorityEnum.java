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

/**
 * Standard granular authority codes governed by Themis across Harmonia.
 */
public enum HarmoniaAuthorityEnum {
    PROVIDER_READ("provider.read", "Read Provider Registry resources"),
    PROVIDER_SEARCH("provider.search", "Search Provider Registry resources"),
    PROVIDER_CHANGE_SUBMIT("provider.change.submit", "Submit Provider Registry change tasks"),
    PROVIDER_CHANGE_PROCESS("provider.change.process", "Process Provider Registry change tasks"),
    PROVIDER_CHANGE_APPROVE("provider.change.approve", "Approve Provider Registry change requests"),
    PROVIDER_RESOURCE_CREATE("provider.resource.create", "Create Provider Registry entities in storage"),
    PROVIDER_RESOURCE_UPDATE("provider.resource.update", "Update Provider Registry entities in storage"),
    PROVIDER_RESOURCE_DELETE("provider.resource.delete", "Delete Provider Registry entities in storage"),
    PROVIDER_RESOURCE_VALIDATE("provider.resource.validate", "Validate Provider Registry resources"),
    PROVIDER_ADMIN("provider.admin", "Full administrative control over Provider Registry"),
    CLINICAL_READ("clinical.read", "Read Clinical resources"),
    CLINICAL_SEARCH("clinical.search", "Search Clinical resources"),
    CLINICAL_CREATE("clinical.create", "Create Clinical resources"),
    CLINICAL_UPDATE("clinical.update", "Update Clinical resources"),
    CLINICAL_ADMIN("clinical.admin", "Administrative control over Clinical resources"),
    AUDIT_READ("audit.read", "Read security decision audit records"),
    SYSTEM_INTEGRATION("system.integration", "Internal system-to-system integration tasks"),
    SYSTEM_ADMIN("system.admin", "System administrator access");

    private final String code;
    private final String description;

    HarmoniaAuthorityEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public ThemisAuthority toThemisAuthority() {
        return ThemisAuthority.of(code);
    }

    public static java.util.Optional<HarmoniaAuthorityEnum> fromCode(String code) {
        if (code == null) {
            return java.util.Optional.empty();
        }
        for (HarmoniaAuthorityEnum auth : values()) {
            if (auth.code.equalsIgnoreCase(code.trim())) {
                return java.util.Optional.of(auth);
            }
        }
        return java.util.Optional.empty();
    }
}
