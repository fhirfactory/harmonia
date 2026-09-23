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

import java.io.Serializable;

/**
 * Immutable source descriptor identifying the subsystem and component emitting an audit event.
 */
public record AuditSource(
        String subsystem,
        String component
) implements Serializable {

    public AuditSource {
        subsystem = subsystem != null ? subsystem.trim() : null;
        component = component != null ? component.trim() : null;
    }

    public static AuditSource of(String subsystem, String component) {
        return new AuditSource(subsystem, component);
    }

    @Override
    public String toString() {
        return "AuditSource[" +
                "subsystem=" + subsystem +
                ", component=" + component +
                "]";
    }
}
