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

package net.fhirfactory.harmonia.model.core.domain.fhir;
import net.fhirfactory.harmonia.model.core.workflow.tasks.Pragma;

import net.fhirfactory.harmonia.themis.api.model.PrincipalType;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthority;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.Extension;
import org.hl7.fhir.r5.model.StringType;
import org.hl7.fhir.r5.model.Task;

import java.util.Map;
import java.util.Set;

import static net.fhirfactory.harmonia.model.core.domain.fhir.valuesets.PragmaFhirConstants.*;

/**
 * Handles bi-directional conversion between {@link ThemisSecurityContext} security attributes and FHIR {@link Task} extensions.
 */
public final class PragmaFhirSecurityConverter {

    private PragmaFhirSecurityConverter() {
        // Utility class
    }

    public static void applySecurityContext(Task task, Pragma pragma) {
        if (task == null || pragma == null) {
            return;
        }

        ThemisPrincipal principal = pragma.getOriginatingPrincipal();
        if (principal == null && pragma.getOriginatingSecurityContext() != null) {
            principal = pragma.getOriginatingSecurityContext().originatingPrincipal();
        }
        if (principal != null) {
            if (StringUtils.isNotBlank(principal.principalId())) {
                task.addExtension(new Extension(EXTENSION_SECURITY_PRINCIPAL_ID, new StringType(principal.principalId())));
            }
            if (principal.principalType() != null) {
                task.addExtension(new Extension(EXTENSION_SECURITY_PRINCIPAL_TYPE, new StringType(principal.principalType().name())));
            }
            if (StringUtils.isNotBlank(principal.sourceDomain())) {
                task.addExtension(new Extension(EXTENSION_SECURITY_SOURCE_DOMAIN, new StringType(principal.sourceDomain())));
            }
        }

        ThemisPrincipal execPrincipal = pragma.getExecutingPrincipal();
        if (execPrincipal == null && pragma.getOriginatingSecurityContext() != null) {
            execPrincipal = pragma.getOriginatingSecurityContext().executingPrincipal();
        }
        if (execPrincipal != null) {
            if (StringUtils.isNotBlank(execPrincipal.principalId())) {
                task.addExtension(new Extension(EXTENSION_SECURITY_EXECUTING_PRINCIPAL_ID, new StringType(execPrincipal.principalId())));
            }
            if (execPrincipal.principalType() != null) {
                task.addExtension(new Extension(EXTENSION_SECURITY_EXECUTING_PRINCIPAL_TYPE, new StringType(execPrincipal.principalType().name())));
            }
            if (StringUtils.isNotBlank(execPrincipal.sourceDomain())) {
                task.addExtension(new Extension(EXTENSION_SECURITY_EXECUTING_SOURCE_DOMAIN, new StringType(execPrincipal.sourceDomain())));
            }
        }

        Set<ThemisAuthority> authorities = pragma.getOriginatingAuthorities();
        if (authorities.isEmpty() && pragma.getOriginatingSecurityContext() != null) {
            authorities = pragma.getOriginatingSecurityContext().authorities();
        }
        for (ThemisAuthority auth : authorities) {
            if (auth != null && StringUtils.isNotBlank(auth.authorityCode())) {
                task.addExtension(new Extension(EXTENSION_SECURITY_AUTHORITY, new StringType(auth.authorityCode())));
            }
        }

        if (StringUtils.isNotBlank(pragma.getPolicyVersion())) {
            task.addExtension(new Extension(EXTENSION_SECURITY_POLICY_VERSION, new StringType(pragma.getPolicyVersion())));
        }
    }

    public static void extractSecurityContext(Task task, Pragma pragma) {
        if (task == null || pragma == null || !task.hasExtension()) {
            return;
        }

        String principalId = null;
        PrincipalType principalType = null;
        String sourceDomain = null;
        String executingPrincipalId = null;
        PrincipalType executingPrincipalType = null;
        String executingSourceDomain = null;
        String policyVersion = null;

        for (Extension ext : task.getExtension()) {
            if (ext.hasUrl() && ext.getValue() instanceof StringType stringType) {
                String url = ext.getUrl();
                String val = stringType.getValue();
                if (EXTENSION_SECURITY_PRINCIPAL_ID.equals(url) || (LEGACY_EXTENSION_SECURITY_PREFIX + "principal-id").equals(url)) {
                    principalId = val;
                } else if (EXTENSION_SECURITY_PRINCIPAL_TYPE.equals(url) || (LEGACY_EXTENSION_SECURITY_PREFIX + "principal-type").equals(url)) {
                    try {
                        principalType = PrincipalType.valueOf(val);
                    } catch (Exception ignored) {}
                } else if (EXTENSION_SECURITY_SOURCE_DOMAIN.equals(url) || (LEGACY_EXTENSION_SECURITY_PREFIX + "source-domain").equals(url)) {
                    sourceDomain = val;
                } else if (EXTENSION_SECURITY_EXECUTING_PRINCIPAL_ID.equals(url) || (LEGACY_EXTENSION_SECURITY_PREFIX + "executing-principal-id").equals(url)) {
                    executingPrincipalId = val;
                } else if (EXTENSION_SECURITY_EXECUTING_PRINCIPAL_TYPE.equals(url) || (LEGACY_EXTENSION_SECURITY_PREFIX + "executing-principal-type").equals(url)) {
                    try {
                        executingPrincipalType = PrincipalType.valueOf(val);
                    } catch (Exception ignored) {}
                } else if (EXTENSION_SECURITY_EXECUTING_SOURCE_DOMAIN.equals(url) || (LEGACY_EXTENSION_SECURITY_PREFIX + "executing-source-domain").equals(url)) {
                    executingSourceDomain = val;
                } else if (EXTENSION_SECURITY_AUTHORITY.equals(url) || (LEGACY_EXTENSION_SECURITY_PREFIX + "authority").equals(url)) {
                    pragma.addOriginatingAuthority(val);
                } else if (EXTENSION_SECURITY_POLICY_VERSION.equals(url) || (LEGACY_EXTENSION_SECURITY_PREFIX + "policy-version").equals(url)) {
                    policyVersion = val;
                }
            }
        }

        ThemisPrincipal originatingPrincipal = null;
        if (principalId != null) {
            originatingPrincipal = new ThemisPrincipal(
                    principalId,
                    principalType != null ? principalType : PrincipalType.HUMAN,
                    sourceDomain,
                    Map.of()
            );
            pragma.setOriginatingPrincipal(originatingPrincipal);
        }

        ThemisPrincipal executingPrincipal = null;
        if (executingPrincipalId != null) {
            executingPrincipal = new ThemisPrincipal(
                    executingPrincipalId,
                    executingPrincipalType != null ? executingPrincipalType : PrincipalType.PROCESS,
                    executingSourceDomain,
                    Map.of()
            );
            pragma.setExecutingPrincipal(executingPrincipal);
        }

        if (originatingPrincipal != null || executingPrincipal != null || !pragma.getOriginatingAuthorities().isEmpty()) {
            ThemisSecurityContext secCtx = ThemisSecurityContext.builder()
                    .originatingPrincipal(originatingPrincipal)
                    .executingPrincipal(executingPrincipal)
                    .authorities(pragma.getOriginatingAuthorities())
                    .correlationId(pragma.getCorrelationId())
                    .causationId(pragma.getCausationId())
                    .build();
            pragma.setOriginatingSecurityContext(secCtx);
        }

        if (policyVersion != null) {
            pragma.setPolicyVersion(policyVersion);
        }
    }
}
