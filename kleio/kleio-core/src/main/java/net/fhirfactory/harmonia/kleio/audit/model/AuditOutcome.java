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

import net.fhirfactory.harmonia.themis.api.model.ThemisDecision;

/**
 * Categorized outcome of an audited activity or decision.
 */
public enum AuditOutcome {
    SUCCESS,
    FAILURE,
    DENIED;

    /**
     * Maps a {@link ThemisDecision} to an audit outcome.
     *
     * @param decision the authorization decision
     * @return SUCCESS for ALLOW, DENIED for DENY, or null if input is null
     */
    public static AuditOutcome fromDecision(ThemisDecision decision) {
        if (decision == null) {
            return null;
        }
        return switch (decision) {
            case ALLOW -> SUCCESS;
            case DENY -> DENIED;
        };
    }
}
