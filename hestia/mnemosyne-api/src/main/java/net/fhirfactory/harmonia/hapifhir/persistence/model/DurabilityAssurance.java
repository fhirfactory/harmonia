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

package net.fhirfactory.harmonia.hapifhir.persistence.model;

/**
 * Evidence level established for a persistence operation. The ordering is deliberately explicit:
 * a cache acknowledgement cannot stand in for a database commit, and a local commit cannot prove
 * cross-site durability.
 */
public enum DurabilityAssurance {
    UNQUALIFIED(0),
    CACHE_ACCEPTED(1),
    LOCAL_TRANSACTION_COMMITTED(2),
    AUTHORITATIVE_CONFIRMED(3),
    CROSS_SITE_CONFIRMED(4);

    private final int level;

    DurabilityAssurance(int level) {
        this.level = level;
    }

    /**
     * Determines whether this evidence level meets a required minimum assurance.
     *
     * @param required minimum assurance; must not be null
     * @return true when this assurance is at least the required level
     */
    public boolean satisfies(DurabilityAssurance required) {
        if (required == null) {
            throw new IllegalArgumentException("required durability assurance must not be null");
        }
        return level >= required.level;
    }
}