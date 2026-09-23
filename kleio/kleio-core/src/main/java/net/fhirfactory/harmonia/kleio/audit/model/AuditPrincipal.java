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

package net.fhirfactory.harmonia.kleio.audit.model;

import net.fhirfactory.harmonia.themis.api.model.PrincipalType;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;

import java.io.Serializable;

/**
 * Immutable audit principal capturing authenticated identity and domain boundary
 * for an audit event without arbitrary runtime attributes.
 */
public record AuditPrincipal(
        String principalId,
        PrincipalType principalType,
        String sourceDomain
) implements Serializable {

    public static AuditPrincipal of(String principalId, PrincipalType principalType, String sourceDomain) {
        return new AuditPrincipal(principalId, principalType, sourceDomain);
    }

    public static AuditPrincipal of(String principalId, PrincipalType principalType) {
        return new AuditPrincipal(principalId, principalType, null);
    }

    public static AuditPrincipal from(ThemisPrincipal principal) {
        if (principal == null) {
            return null;
        }
        return new AuditPrincipal(principal.principalId(), principal.principalType(), principal.sourceDomain());
    }

    public ThemisPrincipal toThemisPrincipal() {
        return ThemisPrincipal.of(principalId, principalType, sourceDomain);
    }

    @Override
    public String toString() {
        return "AuditPrincipal[" +
                "principalId=" + principalId +
                ", principalType=" + principalType +
                ", sourceDomain=" + sourceDomain +
                "]";
    }
}
