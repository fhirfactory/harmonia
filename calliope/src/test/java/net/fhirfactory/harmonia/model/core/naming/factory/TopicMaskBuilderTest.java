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

@DisplayName("Accepted TopicMaskBuilder validation and reuse")
class TopicMaskBuilderTest {

    @Test
    @DisplayName("Builder reuses prior entries while every build is an immutable snapshot")
    void reusableSnapshots() {
        TopicMaskBuilder builder = TopicMask.builder().addForValue("one");
        TopicMask first = builder.build();
        builder.addForValue("two");
        TopicMask second = builder.build();

        assertThat(first.getEntries()).hasSize(1);
        assertThat(second.getEntries()).hasSize(2);
        assertThat(first.matches(topic("Q", "one"))).isTrue();
        assertThat(first.matches(topic("Q", "two"))).isFalse();
        assertThatThrownBy(() -> first.getEntries().add(first.getEntries().getFirst()))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("Failed builds and additions leave prior builder state unchanged")
    void failedOperationsPreserveState() {
        TopicMaskBuilder empty = TopicMask.builder();
        assertThatThrownBy(empty::build).isInstanceOf(IllegalStateException.class);
        empty.addForValue("ok");
        TopicMaskBuilder populated = empty;

        assertThatThrownBy(() -> populated.addRegex("(", "bad"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> populated.add(null))
                .isInstanceOf(IllegalArgumentException.class);
        TopicMask result = populated.build();
        assertThat(result.getEntries()).hasSize(1);
        assertThat(result.matches(topic("Q", "ok"))).isTrue();
    }

    @Test
    @DisplayName("Fluent helpers are equivalent to validated literal and expression operations")
    void helperOperationsMatchTheirContracts() {
        Topic candidate = Topic.builder().addEntry("Q", "a.b").addEntry("R", "value").build();
        TopicMask literal = TopicMask.builder().addLiterals("Q", "a.b")
                .addForLiteralValue("value").includeContainedElements(false).build();
        TopicMask expression = TopicMask.builder().addRegex("Q", "a\\.b")
                .addForValue("value").build();

        assertThat(literal.matches(candidate)).isTrue();
        assertThat(expression.matches(candidate)).isTrue();
        assertThat(TopicMask.builder().addForLiteralQualifier("Q").addAny().build()
                .matches(candidate)).isTrue();
        assertThat(TopicMask.builder().addForQualifier("Q").addAny().build()
                .matches(candidate)).isTrue();
    }

    @Test
    @DisplayName("Containment changes are applied to later snapshots only")
    void containmentSnapshotIsStable() {
        TopicMaskBuilder builder = TopicMask.builder().addForValue("root");
        TopicMask exact = builder.build();
        builder.includeContainedElements(true);
        TopicMask inclusive = builder.build();
        Topic candidate = Topic.builder().addEntry("A", "root").addEntry("B", "child").build();

        assertThat(exact.isIncludeContainedElements()).isFalse();
        assertThat(inclusive.isIncludeContainedElements()).isTrue();
        assertThat(exact.matches(candidate)).isFalse();
        assertThat(inclusive.matches(candidate)).isTrue();
    }

    @Test
    @DisplayName("The supported entry depth is enforced without corrupting the builder")
    void enforcesDepthLimit() {
        TopicMaskBuilder builder = TopicMask.builder();
        for (int index = 0; index < 32; index++) {
            builder.addAny();
        }
        TopicMask snapshot = builder.build();
        assertThat(snapshot.getEntries()).hasSize(32);
        assertThatThrownBy(builder::addAny).isInstanceOf(IllegalArgumentException.class);
        assertThat(builder.build().getEntries()).hasSize(32);
    }

    private static Topic topic(String qualifier, String value) {
        return Topic.builder().addEntry(qualifier, value).build();
    }
}