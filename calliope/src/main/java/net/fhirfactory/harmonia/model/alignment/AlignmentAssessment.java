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

package net.fhirfactory.harmonia.model.alignment;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Sealed hierarchy defining the semantic relationship between two resource instances.
 * <p>
 * Preserves relationship-oriented semantics without Boolean reduction:
 * <ul>
 *   <li>{@link ObjectsCoincident}: The variance is semantically coincident; no merge is required.</li>
 *   <li>{@link ObjectsOrthogonal}: Meaningful variance exists without conflict; compatible changes can be safely merged.</li>
 *   <li>{@link ObjectsConflict}: Meaningful variance exists and intersects incompatibly; automatic merge cannot proceed.</li>
 * </ul>
 */
public sealed interface AlignmentAssessment extends Serializable
        permits AlignmentAssessment.ObjectsCoincident,
                AlignmentAssessment.ObjectsOrthogonal,
                AlignmentAssessment.ObjectsConflict {

    record ObjectsCoincident(String details) implements AlignmentAssessment {
        public ObjectsCoincident {
            Objects.requireNonNull(details, "details must not be null");
        }
    }

    record ObjectsOrthogonal(String summary) implements AlignmentAssessment {
        public ObjectsOrthogonal {
            Objects.requireNonNull(summary, "summary must not be null");
        }
    }

    record ObjectsConflict(String reason, List<String> conflictingPaths) implements AlignmentAssessment {
        public ObjectsConflict {
            Objects.requireNonNull(reason, "reason must not be null");
            conflictingPaths = conflictingPaths == null ? List.of() : List.copyOf(conflictingPaths);
        }
    }
}
