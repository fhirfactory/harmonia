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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AlignmentAssessmentTest {

    @Test
    @DisplayName("AlignmentAssessment implements Serializable")
    void alignmentAssessmentIsSerializable() {
        assertThat(Serializable.class.isAssignableFrom(AlignmentAssessment.class)).isTrue();
    }

    @Test
    @DisplayName("ObjectsCoincident captures details and enforces non-null")
    void objectsCoincidentContract() {
        AlignmentAssessment.ObjectsCoincident coincident = new AlignmentAssessment.ObjectsCoincident("identical content");
        assertThat(coincident.details()).isEqualTo("identical content");

        assertThatThrownBy(() -> new AlignmentAssessment.ObjectsCoincident(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("details must not be null");
    }

    @Test
    @DisplayName("ObjectsOrthogonal captures summary and enforces non-null")
    void objectsOrthogonalContract() {
        AlignmentAssessment.ObjectsOrthogonal orthogonal = new AlignmentAssessment.ObjectsOrthogonal("independent telecom change");
        assertThat(orthogonal.summary()).isEqualTo("independent telecom change");

        assertThatThrownBy(() -> new AlignmentAssessment.ObjectsOrthogonal(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("summary must not be null");
    }

    @Test
    @DisplayName("ObjectsConflict captures reason and immutable conflictingPaths")
    void objectsConflictContract() {
        List<String> paths = new ArrayList<>();
        paths.add("telecom[0].value");
        paths.add("active");

        AlignmentAssessment.ObjectsConflict conflict = new AlignmentAssessment.ObjectsConflict("overlapping edits", paths);
        assertThat(conflict.reason()).isEqualTo("overlapping edits");
        assertThat(conflict.conflictingPaths()).containsExactly("telecom[0].value", "active");

        // Mutating source list should not affect record
        paths.add("name[0].family");
        assertThat(conflict.conflictingPaths()).hasSize(2);

        // Record conflictingPaths is unmodifiable
        assertThatThrownBy(() -> conflict.conflictingPaths().add("gender"))
                .isInstanceOf(UnsupportedOperationException.class);

        // Null paths safely defaults to empty list
        AlignmentAssessment.ObjectsConflict nullPaths = new AlignmentAssessment.ObjectsConflict("conflict", null);
        assertThat(nullPaths.conflictingPaths()).isEmpty();

        // Null reason rejected
        assertThatThrownBy(() -> new AlignmentAssessment.ObjectsConflict(null, List.of()))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("reason must not be null");
    }

    @Test
    @DisplayName("Exhaustive pattern matching on sealed AlignmentAssessment hierarchy")
    void exhaustivePatternMatching() {
        AlignmentAssessment coincident = new AlignmentAssessment.ObjectsCoincident("coincident");
        AlignmentAssessment orthogonal = new AlignmentAssessment.ObjectsOrthogonal("orthogonal");
        AlignmentAssessment conflict = new AlignmentAssessment.ObjectsConflict("conflict", List.of("path"));

        assertThat(describeAssessment(coincident)).isEqualTo("COINCIDENT: coincident");
        assertThat(describeAssessment(orthogonal)).isEqualTo("ORTHOGONAL: orthogonal");
        assertThat(describeAssessment(conflict)).isEqualTo("CONFLICT: conflict with 1 path(s)");
    }

    private String describeAssessment(AlignmentAssessment assessment) {
        return switch (assessment) {
            case AlignmentAssessment.ObjectsCoincident c -> "COINCIDENT: " + c.details();
            case AlignmentAssessment.ObjectsOrthogonal o -> "ORTHOGONAL: " + o.summary();
            case AlignmentAssessment.ObjectsConflict k -> "CONFLICT: " + k.reason() + " with " + k.conflictingPaths().size() + " path(s)";
        };
    }
}
