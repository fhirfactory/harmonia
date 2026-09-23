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

package net.fhirfactory.harmonia.themis.core.identities;

import net.fhirfactory.harmonia.themis.api.model.PrincipalType;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthority;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Standardized controlled service identities and their default least-privilege authorities across Harmonia.
 */
public final class HarmoniaServiceIdentities {

    public static final String ID_PYLAI = "service:pylai";
    public static final String ID_PETASOS = "service:petasos";
    public static final String ID_PONOS = "service:ponos";
    public static final String ID_PONOS_PROCESS = "process:ponos-engine";
    public static final String ID_PROVIDER_REGISTRY = "service:provider-registry";
    public static final String ID_THEMIS = "service:themis";
    public static final String ID_CALLIOPE = "service:calliope";
    public static final String ID_MNEME = "service:mneme";
    public static final String ID_MNEMOSYNE = "service:mnemosyne";
    public static final String ID_IRIS_BEFE = "service:iris-befe";

    public static final ThemisPrincipal PRINCIPAL_PYLAI = ThemisPrincipal.of(ID_PYLAI, PrincipalType.SERVICE, "pylai");
    public static final ThemisPrincipal PRINCIPAL_PETASOS = ThemisPrincipal.of(ID_PETASOS, PrincipalType.SERVICE, "petasos");
    public static final ThemisPrincipal PRINCIPAL_PONOS = ThemisPrincipal.of(ID_PONOS, PrincipalType.SERVICE, "ponos");
    public static final ThemisPrincipal PRINCIPAL_PONOS_PROCESS = ThemisPrincipal.of(ID_PONOS_PROCESS, PrincipalType.PROCESS, "ponos");
    public static final ThemisPrincipal PRINCIPAL_PROVIDER_REGISTRY = ThemisPrincipal.of(ID_PROVIDER_REGISTRY, PrincipalType.SERVICE, "hestia");
    public static final ThemisPrincipal PRINCIPAL_THEMIS = ThemisPrincipal.of(ID_THEMIS, PrincipalType.SERVICE, "themis");
    public static final ThemisPrincipal PRINCIPAL_CALLIOPE = ThemisPrincipal.of(ID_CALLIOPE, PrincipalType.SERVICE, "calliope");
    public static final ThemisPrincipal PRINCIPAL_MNEME = ThemisPrincipal.of(ID_MNEME, PrincipalType.SERVICE, "mneme");
    public static final ThemisPrincipal PRINCIPAL_MNEMOSYNE = ThemisPrincipal.of(ID_MNEMOSYNE, PrincipalType.SERVICE, "mnemosyne");
    public static final ThemisPrincipal PRINCIPAL_IRIS_BEFE = ThemisPrincipal.of(ID_IRIS_BEFE, PrincipalType.SERVICE, "iris");

    private static final Map<String, Set<ThemisAuthority>> SERVICE_AUTHORITIES = new ConcurrentHashMap<>();

    static {
        SERVICE_AUTHORITIES.put(ID_PYLAI, Set.of(
                ThemisAuthority.of("provider.read"),
                ThemisAuthority.of("provider.search"),
                ThemisAuthority.of("provider.change.submit"),
                ThemisAuthority.of("system.integration")
        ));

        SERVICE_AUTHORITIES.put(ID_PETASOS, Set.of(
                ThemisAuthority.of("system.integration")
        ));

        SERVICE_AUTHORITIES.put(ID_PONOS, Set.of(
                ThemisAuthority.of("provider.change.process"),
                ThemisAuthority.of("system.integration")
        ));

        SERVICE_AUTHORITIES.put(ID_PONOS_PROCESS, Set.of(
                ThemisAuthority.of("provider.change.process"),
                ThemisAuthority.of("system.integration")
        ));

        SERVICE_AUTHORITIES.put(ID_PROVIDER_REGISTRY, Set.of(
                ThemisAuthority.of("provider.resource.create"),
                ThemisAuthority.of("provider.resource.update"),
                ThemisAuthority.of("provider.resource.delete"),
                ThemisAuthority.of("provider.read"),
                ThemisAuthority.of("provider.search"),
                ThemisAuthority.of("provider.admin"),
                ThemisAuthority.of("system.integration")
        ));

        SERVICE_AUTHORITIES.put(ID_THEMIS, Set.of(
                ThemisAuthority.of("audit.read"),
                ThemisAuthority.of("system.admin")
        ));

        SERVICE_AUTHORITIES.put(ID_CALLIOPE, Set.of(
                ThemisAuthority.of("system.integration")
        ));

        SERVICE_AUTHORITIES.put(ID_MNEME, Set.of(
                ThemisAuthority.of("clinical.read"),
                ThemisAuthority.of("clinical.create"),
                ThemisAuthority.of("clinical.update"),
                ThemisAuthority.of("clinical.delete"),
                ThemisAuthority.of("system.integration")
        ));

        SERVICE_AUTHORITIES.put(ID_MNEMOSYNE, Set.of(
                ThemisAuthority.of("provider.resource.create"),
                ThemisAuthority.of("provider.resource.update"),
                ThemisAuthority.of("provider.resource.delete"),
                ThemisAuthority.of("provider.read"),
                ThemisAuthority.of("provider.search"),
                ThemisAuthority.of("provider.admin"),
                ThemisAuthority.of("system.integration")
        ));

        SERVICE_AUTHORITIES.put(ID_IRIS_BEFE, Set.of(
                ThemisAuthority.of("clinical.read"),
                ThemisAuthority.of("clinical.search"),
                ThemisAuthority.of("clinical.create"),
                ThemisAuthority.of("clinical.update"),
                ThemisAuthority.of("clinical.delete"),
                ThemisAuthority.of("system.integration")
        ));
    }

    private HarmoniaServiceIdentities() {
    }

    public static Set<ThemisAuthority> getAuthorities(String serviceId) {
        if (serviceId == null) {
            return Set.of();
        }
        return SERVICE_AUTHORITIES.getOrDefault(serviceId, Set.of());
    }

    public static Set<ThemisAuthority> getAuthorities(ThemisPrincipal principal) {
        if (principal == null || principal.principalId() == null) {
            return Set.of();
        }
        return getAuthorities(principal.principalId());
    }

    public static Map<String, Set<ThemisAuthority>> getAllServiceAuthorities() {
        return Collections.unmodifiableMap(SERVICE_AUTHORITIES);
    }
}
