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

import net.fhirfactory.harmonia.themis.api.model.ThemisResource;

import java.io.Serializable;

/**
 * Immutable target projection capturing resource identity and security domain
 * for an audit event without clinical payload or sensitive attributes.
 */
public record AuditTarget(
        String resourceType,
        String resourceId,
        String securityDomain
) implements Serializable {

    public AuditTarget {
        resourceType = resourceType != null ? resourceType.trim() : null;
        resourceId = resourceId != null ? resourceId.trim() : null;
        securityDomain = securityDomain != null ? securityDomain.trim() : null;
    }

    public static AuditTarget of(String resourceType, String resourceId, String securityDomain) {
        return new AuditTarget(resourceType, resourceId, securityDomain);
    }

    public static AuditTarget of(String resourceType, String resourceId) {
        return new AuditTarget(resourceType, resourceId, null);
    }

    public static AuditTarget fromResource(ThemisResource resource) {
        if (resource == null) {
            return null;
        }
        return new AuditTarget(resource.resourceType(), resource.resourceId(), resource.securityDomain());
    }

    @Override
    public String toString() {
        return "AuditTarget[" +
                "resourceType=" + resourceType +
                ", resourceId=" + resourceId +
                ", securityDomain=" + securityDomain +
                "]";
    }
}
