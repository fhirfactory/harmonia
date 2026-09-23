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

import net.fhirfactory.harmonia.themis.api.model.ThemisAction;

/**
 * Fundamental actions recorded in audit events across Harmonia.
 */
public enum AuditAction {
    CREATE,
    READ,
    SEARCH,
    UPDATE,
    DELETE,
    EXECUTE,
    AUTHORIZE;

    /**
     * Converts a {@link ThemisAction} to the corresponding {@link AuditAction}.
     *
     * @param action the authorization action to convert
     * @return corresponding audit action, or null if input is null
     */
    public static AuditAction fromThemisAction(ThemisAction action) {
        if (action == null) {
            return null;
        }
        return switch (action) {
            case READ -> READ;
            case SEARCH -> SEARCH;
            case CREATE, SUBMIT_CREATE -> CREATE;
            case UPDATE, SUBMIT_UPDATE -> UPDATE;
            case DELETE -> DELETE;
            case EXECUTE, PROCESS, APPROVE, REJECT, REPLAY, ADMINISTER -> EXECUTE;
        };
    }
}
