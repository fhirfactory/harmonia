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

package net.fhirfactory.harmonia.model.core.information.factory;

import net.fhirfactory.harmonia.model.core.information.InformationUnit;

/**
 * Validates invariant integrity for {@link InformationUnit} instances.
 */
public final class InformationUnitValidator {

    private InformationUnitValidator() {
        // Utility validator
    }

    public static boolean validate(InformationUnit unit) {
        if (unit == null) {
            throw new IllegalStateException("InformationUnit must not be null");
        }
        if (unit.identifier() == null) {
            throw new IllegalStateException("InformationUnit must have a non-null identifier");
        }
        if (unit.identifier().uuid() == null) {
            throw new IllegalStateException("InformationUnit identifier must have a non-null UUID");
        }
        if (unit.identifier().toTopic() == null) {
            throw new IllegalStateException("InformationUnit identifier must have a non-null Topic");
        }
        if (unit.metadata() == null) {
            throw new IllegalStateException("InformationUnit must have non-null metadata");
        }
        if (unit.representation() == null) {
            throw new IllegalStateException("InformationUnit must have a non-null representation");
        }
        return true;
    }
}
