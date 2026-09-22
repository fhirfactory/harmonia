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

package net.fhirfactory.harmonia.themis.core.constants;

import net.fhirfactory.harmonia.themis.api.model.ThemisAuthority;
import net.fhirfactory.harmonia.themis.api.model.ThemisRole;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityLabel;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Standard Harmonia security constants, roles, and role-to-authority definitions.
 */
public final class HarmoniaSecurityConstants {

    private HarmoniaSecurityConstants() {}

    // Security Label Codes
    public static final String LABEL_PROVIDER_REGISTRY = "PROVIDER_REGISTRY";
    public static final String LABEL_INTERNAL = "INTERNAL";
    public static final String LABEL_AUDIT = "AUDIT";
    public static final String LABEL_RESTRICTED = "RESTRICTED";
    public static final String LABEL_CLINICAL = "CLINICAL";
    public static final String LABEL_OPERATIONS = "OPERATIONS";

    // Authority Codes
    public static final String AUTH_PROVIDER_READ = "provider.read";
    public static final String AUTH_PROVIDER_SEARCH = "provider.search";
    public static final String AUTH_PROVIDER_CHANGE_SUBMIT = "provider.change.submit";
    public static final String AUTH_PROVIDER_CHANGE_PROCESS = "provider.change.process";
    public static final String AUTH_PROVIDER_CHANGE_APPROVE = "provider.change.approve";
    public static final String AUTH_PROVIDER_RESOURCE_CREATE = "provider.resource.create";
    public static final String AUTH_PROVIDER_RESOURCE_UPDATE = "provider.resource.update";
    public static final String AUTH_PROVIDER_RESOURCE_DELETE = "provider.resource.delete";
    public static final String AUTH_PROVIDER_RESOURCE_VALIDATE = "provider.resource.validate";
    public static final String AUTH_PROVIDER_ADMIN = "provider.admin";
    public static final String AUTH_CLINICAL_READ = "clinical.read";
    public static final String AUTH_CLINICAL_SEARCH = "clinical.search";
    public static final String AUTH_CLINICAL_CREATE = "clinical.create";
    public static final String AUTH_CLINICAL_UPDATE = "clinical.update";
    public static final String AUTH_CLINICAL_ADMIN = "clinical.admin";

    public static final String AUTH_OPERATIONS_READ = "operations.read";
    public static final String AUTH_OPERATIONS_ADMIN = "operations.admin";

    public static final String AUTH_AUDIT_READ = "audit.read";
    public static final String AUTH_SYSTEM_INTEGRATION = "system.integration";
    public static final String AUTH_SYSTEM_ADMIN = "system.admin";

    // Role Codes
    public static final String ROLE_PRV_RDR = "PRV_RDR";
    public static final String ROLE_PRV_SUB = "PRV_SUB";
    public static final String ROLE_PRV_PROC = "PRV_PROC";
    public static final String ROLE_PRV_APR = "PRV_APR";
    public static final String ROLE_PRV_ADM = "PRV_ADM";
    public static final String ROLE_CLINICAL_READ = "CLINICAL_READ";
    public static final String ROLE_CLINICAL_WRITE = "CLINICAL_WRITE";
    public static final String ROLE_CLINICAL_ADMIN = "CLINICAL_ADMIN";
    public static final String ROLE_OPS_VIEWER = "OPS_VIEWER";
    public static final String ROLE_OPS_ADM = "OPS_ADM";
    public static final String ROLE_AUD_RDR = "AUD_RDR";
    public static final String ROLE_SYS_INT = "SYS_INT";
    public static final String ROLE_SYS_ADM = "SYS_ADM";

    // Pre-defined Roles
    public static final ThemisRole PRV_RDR = ThemisRole.of(
            ROLE_PRV_RDR,
            "Provider Registry Reader",
            Set.of(ThemisAuthority.of(AUTH_PROVIDER_READ), ThemisAuthority.of(AUTH_PROVIDER_SEARCH))
    );

    public static final ThemisRole PRV_SUB = ThemisRole.of(
            ROLE_PRV_SUB,
            "Provider Registry Change Submitter",
            Set.of(ThemisAuthority.of(AUTH_PROVIDER_CHANGE_SUBMIT))
    );

    public static final ThemisRole PRV_PROC = ThemisRole.of(
            ROLE_PRV_PROC,
            "Provider Registry Change Processor",
            Set.of(ThemisAuthority.of(AUTH_PROVIDER_CHANGE_PROCESS), ThemisAuthority.of(AUTH_PROVIDER_RESOURCE_VALIDATE))
    );

    public static final ThemisRole PRV_APR = ThemisRole.of(
            ROLE_PRV_APR,
            "Provider Registry Approver",
            Set.of(ThemisAuthority.of(AUTH_PROVIDER_CHANGE_APPROVE))
    );

    public static final ThemisRole PRV_ADM = ThemisRole.of(
            ROLE_PRV_ADM,
            "Provider Registry Administrator",
            Set.of(
                    ThemisAuthority.of(AUTH_PROVIDER_ADMIN),
                    ThemisAuthority.of(AUTH_PROVIDER_READ),
                    ThemisAuthority.of(AUTH_PROVIDER_SEARCH),
                    ThemisAuthority.of(AUTH_PROVIDER_CHANGE_SUBMIT),
                    ThemisAuthority.of(AUTH_PROVIDER_CHANGE_PROCESS),
                    ThemisAuthority.of(AUTH_PROVIDER_CHANGE_APPROVE),
                    ThemisAuthority.of(AUTH_PROVIDER_RESOURCE_CREATE),
                    ThemisAuthority.of(AUTH_PROVIDER_RESOURCE_UPDATE),
                    ThemisAuthority.of(AUTH_PROVIDER_RESOURCE_DELETE)
            )
    );

    public static final ThemisRole CLINICAL_READ = ThemisRole.of(
            ROLE_CLINICAL_READ,
            "Clinical Reader",
            Set.of(ThemisAuthority.of(AUTH_CLINICAL_READ), ThemisAuthority.of(AUTH_CLINICAL_SEARCH))
    );

    public static final ThemisRole CLINICAL_WRITE = ThemisRole.of(
            ROLE_CLINICAL_WRITE,
            "Clinical Writer",
            Set.of(
                    ThemisAuthority.of(AUTH_CLINICAL_READ),
                    ThemisAuthority.of(AUTH_CLINICAL_SEARCH),
                    ThemisAuthority.of(AUTH_CLINICAL_CREATE),
                    ThemisAuthority.of(AUTH_CLINICAL_UPDATE)
            )
    );

    public static final ThemisRole CLINICAL_ADMIN = ThemisRole.of(
            ROLE_CLINICAL_ADMIN,
            "Clinical Administrator",
            Set.of(
                    ThemisAuthority.of(AUTH_CLINICAL_READ),
                    ThemisAuthority.of(AUTH_CLINICAL_SEARCH),
                    ThemisAuthority.of(AUTH_CLINICAL_CREATE),
                    ThemisAuthority.of(AUTH_CLINICAL_UPDATE),
                    ThemisAuthority.of(AUTH_CLINICAL_ADMIN)
            )
    );

    public static final ThemisRole OPS_VIEWER = ThemisRole.of(
            ROLE_OPS_VIEWER,
            "Operations Viewer",
            Set.of(ThemisAuthority.of(AUTH_OPERATIONS_READ))
    );

    public static final ThemisRole OPS_ADM = ThemisRole.of(
            ROLE_OPS_ADM,
            "Operations Administrator",
            Set.of(
                    ThemisAuthority.of(AUTH_OPERATIONS_READ),
                    ThemisAuthority.of(AUTH_OPERATIONS_ADMIN)
            )
    );

    public static final ThemisRole AUD_RDR = ThemisRole.of(
            ROLE_AUD_RDR,
            "Audit Reader",
            Set.of(ThemisAuthority.of(AUTH_AUDIT_READ))
    );

    public static final ThemisRole SYS_INT = ThemisRole.of(
            ROLE_SYS_INT,
            "System Integration Service",
            Set.of(ThemisAuthority.of(AUTH_SYSTEM_INTEGRATION))
    );

    public static final ThemisRole SYS_ADM = ThemisRole.of(
            ROLE_SYS_ADM,
            "System Administrator",
            Set.of(ThemisAuthority.of(AUTH_SYSTEM_ADMIN))
    );

    private static final Map<String, ThemisRole> ROLES_BY_CODE = new HashMap<>();

    static {
        ROLES_BY_CODE.put(ROLE_PRV_RDR, PRV_RDR);
        ROLES_BY_CODE.put(ROLE_PRV_SUB, PRV_SUB);
        ROLES_BY_CODE.put(ROLE_PRV_PROC, PRV_PROC);
        ROLES_BY_CODE.put(ROLE_PRV_APR, PRV_APR);
        ROLES_BY_CODE.put(ROLE_PRV_ADM, PRV_ADM);
        ROLES_BY_CODE.put(ROLE_CLINICAL_READ, CLINICAL_READ);
        ROLES_BY_CODE.put(ROLE_CLINICAL_WRITE, CLINICAL_WRITE);
        ROLES_BY_CODE.put(ROLE_CLINICAL_ADMIN, CLINICAL_ADMIN);
        ROLES_BY_CODE.put(ROLE_OPS_VIEWER, OPS_VIEWER);
        ROLES_BY_CODE.put(ROLE_OPS_ADM, OPS_ADM);
        ROLES_BY_CODE.put(ROLE_AUD_RDR, AUD_RDR);
        ROLES_BY_CODE.put(ROLE_SYS_INT, SYS_INT);
        ROLES_BY_CODE.put(ROLE_SYS_ADM, SYS_ADM);
    }

    public static Optional<ThemisRole> getRole(String roleCode) {
        if (roleCode == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(ROLES_BY_CODE.get(roleCode.toUpperCase()));
    }

    public static Map<String, ThemisRole> getAllRoles() {
        return Collections.unmodifiableMap(ROLES_BY_CODE);
    }
}
