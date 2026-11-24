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

package net.fhirfactory.harmonia.model.core.naming.factory;

import net.fhirfactory.harmonia.model.core.naming.Topic;
import net.fhirfactory.harmonia.model.core.naming.TopicMask;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Accepted TopicBuilder construction and recovery")
class TopicBuilderTest {

    @Test
    @DisplayName("Sequential and indexed construction generate the same positional qualifiers")
    void generatedQualifiersArePositionBased() {
        Topic sequential = Topic.builder().addEntry(null, "one").addEntry("", "two").build();
        Topic indexed = Topic.builder().addEntry(1, null, "two").addEntry(0, null, "one").build();
        Topic explicit = Topic.builder().addEntry("N0", "one").addEntry("N1", "two").build();

        assertThat(sequential).isEqualTo(indexed).isEqualTo(explicit);
        assertThat(sequential.compareTo(explicit)).isZero();
        assertThat(sequential.getCanonicalName()).isEqualTo(explicit.getCanonicalName());
        assertThat(sequential.getEntries()).extracting(entry -> entry.getQualifier()).containsExactly("N0", "N1");
        assertThat(TopicMask.builder().addLiterals("N0", "one").addLiterals("N1", "two").build()
                .matches(explicit)).isTrue();
    }

    @Test
    @DisplayName("Indexed entries can be replaced without growth and invalid replacement preserves old data")
    void indexedReplacementIsValidatedAndReusable() {
        TopicBuilder builder = Topic.builder().addEntry(1, "B", "second").addEntry(0, "A", "first");
        Topic firstSnapshot = builder.build();
        assertThatThrownBy(() -> builder.addEntry(0, "A", ""))
                .isInstanceOf(IllegalArgumentException.class);

        builder.addEntry(0, "A", "replacement");
        Topic secondSnapshot = builder.build();
        assertThat(firstSnapshot.getDepth()).isEqualTo(2);
        assertThat(firstSnapshot.getEntries().getFirst().getValue()).isEqualTo("first");
        assertThat(secondSnapshot.getEntries().getFirst().getValue()).isEqualTo("replacement");
        assertThat(secondSnapshot.getDepth()).isEqualTo(2);
    }

    @Test
    @DisplayName("Out-of-order gaps can be repaired after a failed build")
    void gapsAreReportedAndRecoverable() {
        TopicBuilder builder = Topic.builder().addEntry(2, null, "third");
        assertThatThrownBy(builder::build).isInstanceOf(IllegalStateException.class);
        builder.addEntry(0, "A", "first");
        assertThatThrownBy(builder::build).isInstanceOf(IllegalStateException.class);
        builder.addEntry(1, "B", "second");

        Topic result = builder.build();
        assertThat(result.getName()).isEqualTo("first.second.third");
        assertThat(result.getEntries().get(2).getQualifier()).isEqualTo("N2");
    }

    @Test
    @DisplayName("A failed first addition leaves mode undecided and failed builds consume no state")
    void failuresDoNotFixModeOrConsumeInputs() {
        TopicBuilder builder = Topic.builder();
        assertThatThrownBy(() -> builder.addEntry(null, ""))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(builder::build).isInstanceOf(IllegalStateException.class);
        builder.addEntry(0, "A", "valid");
        assertThat(builder.build().getName()).isEqualTo("valid");
    }

    @Test
    @DisplayName("Sequential and indexed modes are exclusive")
    void modesCannotBeMixed() {
        TopicBuilder sequential = Topic.builder().addEntry("A", "one");
        assertThatThrownBy(() -> sequential.addEntry(1, "B", "two"))
                .isInstanceOf(IllegalStateException.class);
        TopicBuilder indexed = Topic.builder().addEntry(0, "A", "one");
        assertThatThrownBy(() -> indexed.addEntry("B", "two"))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("Invalid positions, values, and depth are rejected before state changes")
    void rejectsInvalidInputAndDepth() {
        TopicBuilder builder = Topic.builder();
        assertThatThrownBy(() -> builder.addEntry(-1, "A", "value"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> builder.addEntry(32, "A", "value"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> builder.addEntry("A", "  "))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> builder.addEntry(" ", "value"))
                .isInstanceOf(IllegalArgumentException.class);
        builder.addEntry(0, null, "valid");
        assertThat(builder.build().getName()).isEqualTo("valid");

        TopicBuilder depthLimited = Topic.builder();
        for (int index = 0; index < 32; index++) {
            depthLimited.addEntry(null, "value" + index);
        }
        assertThat(depthLimited.build().getDepth()).isEqualTo(32);
        assertThat(depthLimited.build().getEntries().getLast().getQualifier()).isEqualTo("N31");
        assertThatThrownBy(() -> depthLimited.addEntry(null, "too deep"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Topic.builder().addEntry("A", null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Builder does not expose removal, renumbering, or Topic-editing operations")
    void noEditingConvenienceOperationsAreExposed() {
        assertThat(java.util.Arrays.stream(TopicBuilder.class.getMethods())
                .map(method -> method.getName())
                .noneMatch(name -> name.startsWith("remove") || name.startsWith("renumber")
                        || name.equals("fromTopic"))).isTrue();
    }
}
