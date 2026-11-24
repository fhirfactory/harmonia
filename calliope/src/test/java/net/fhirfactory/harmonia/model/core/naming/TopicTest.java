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

package net.fhirfactory.harmonia.model.core.naming;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.fhirfactory.harmonia.model.core.naming.datatypes.TopicEntry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.TreeSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Accepted immutable Topic model")
class TopicTest {

    @Test
    @DisplayName("Identity, comparison, and hashing use ordered qualifier/value pairs but ignore versions")
    void identityAndOrderingIgnoreVersions() {
        Topic first = Topic.builder().addEntry("Region", "North", "v1")
                .addEntry("Region", "Clinic").build();
        Topic second = Topic.builder().addEntry("Region", "North", "v2")
                .addEntry("Region", "Clinic").build();
        Topic differentQualifier = Topic.builder().addEntry("Area", "North", "v1")
                .addEntry("Region", "Clinic").build();

        assertThat(first).isEqualTo(second);
        assertThat(first.hashCode()).isEqualTo(second.hashCode());
        assertThat(first.compareTo(second)).isZero();
        assertThat(new java.util.HashSet<>(java.util.List.of(first, second))).hasSize(1);
        assertThat(new TreeSet<>(java.util.List.of(first, second))).hasSize(1);
        assertThat(first).isNotEqualTo(differentQualifier);
        assertThat(first.getName()).isEqualTo(differentQualifier.getName());

        Topic reordered = Topic.builder().addEntry("Region", "Clinic").addEntry("Region", "North").build();
        Topic repeatedQualifier = Topic.builder().addEntry("Region", "North")
                .addEntry("Region", "Clinic").build();
        assertThat(repeatedQualifier).isEqualTo(first);
        assertThat(reordered).isNotEqualTo(first);
    }

    @Test
    @DisplayName("Natural ordering is field-wise and shorter complete prefixes sort first")
    void naturalOrderingIsDeterministic() {
        Topic prefix = Topic.builder().addEntry("A", "same").build();
        Topic child = Topic.builder().addEntry("A", "same").addEntry("B", "child").build();
        Topic qualifierOrder = Topic.builder().addEntry("B", "same").build();
        Topic valueOrder = Topic.builder().addEntry("A", "different").build();
        Topic caseVariant = Topic.builder().addEntry("A", "Same").build();
        Topic lowercaseVariant = Topic.builder().addEntry("A", "same").build();

        assertThat(prefix.compareTo(child)).isNegative();
        assertThat(valueOrder.compareTo(prefix)).isNegative();
        assertThat(prefix.compareTo(qualifierOrder)).isNegative();
        assertThat(caseVariant).isNotEqualTo(lowercaseVariant);
        assertThat(caseVariant.compareTo(lowercaseVariant)).isNegative();
    }

    @Test
    @DisplayName("Parent and ancestor navigation preserve retained versions and strict ancestry")
    void hierarchyPreservesVersions() {
        Topic root = Topic.builder().addEntry("R", "root", "one").build();
        Topic child = Topic.builder().addEntry("R", "root", "two")
                .addEntry("C", "child", "three").build();

        assertThat(root.isAncestorOf(child)).isTrue();
        assertThat(child.isAncestorOf(root)).isFalse();
        assertThat(root.isAncestorOf(null)).isFalse();
        assertThat(root.getParent()).isNull();
        assertThat(child.getParent().getCanonicalNameWithVersion()).isEqualTo("R:two=root");
        assertThat(child.getAncestor(child.getDepth())).isSameAs(child);
        assertThat(child.getAncestor(1).getEntries().getFirst().getVersion()).contains("two");
        assertThatThrownBy(() -> child.getAncestor(0)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> child.getAncestor(3)).isInstanceOf(IllegalArgumentException.class);
        assertThat(root.getDepth()).isEqualTo(1);
    }

    @Test
    @DisplayName("Canonical identity and versioned text round-trip escaped fields exactly")
    void canonicalFormsRoundTrip() {
        String literal = "value.with=delimiters:and\\slashes";
        Topic topic = Topic.builder().addEntry("qualifier.with=marks", literal, "ver:sion\\1")
                .addEntry(null, "leaf").build();

        String canonical = topic.getCanonicalName();
        String versioned = topic.getCanonicalNameWithVersion();
        assertThat(Topic.fromCanonicalName(canonical)).isEqualTo(topic);
        assertThat(Topic.fromCanonicalNameWithVersion(versioned).getCanonicalNameWithVersion())
                .isEqualTo(versioned);
        assertThat(topic.toString()).isEqualTo(versioned);
        assertThat(Topic.fromCanonicalNameWithVersion(versioned)).isEqualTo(topic);
        assertThat(topic.getEntries().getFirst().getVersion()).contains("ver:sion\\1");
        assertThat(topic.getRelativeName()).isEqualTo("N1=leaf");
        assertThat(topic.getFriendlyName()).isEqualTo("leaf");
        assertThat(topic.getDisplayName()).isEqualTo(literal + "-leaf");
        assertThat(Topic.fromCanonicalName(canonical).getEntries().getFirst().getVersion()).isEmpty();
    }

    @Test
    @DisplayName("Canonical parsing rejects missing structure, invalid escapes, and misplaced versions")
    void canonicalParserRejectsMalformedInput() {
        for (String invalid : java.util.List.of("", ".q=v", "q=v.", "=v", "q=", "q=v=x", "q=v\\q")) {
            assertThatThrownBy(() -> Topic.fromCanonicalName(invalid))
                    .as("invalid Topic syntax %s", invalid)
                    .isInstanceOf(IllegalArgumentException.class);
        }
        assertThatThrownBy(() -> Topic.fromCanonicalName("q:v=x"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Topic.fromCanonicalNameWithVersion("q:=x"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Topic.fromCanonicalNameWithVersion("q=v:late"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Field validation accepts Unicode letters, marks, symbols, and internal ASCII spaces")
    void fieldValidationUsesCodePointsAndPreservesText() {
        String decomposed = "e\u0301";
        String composed = "\u00e9";
        String supplementary = "🩺";
        Topic topic = Topic.builder().addEntry("A", "pre composed space post")
                .addEntry("B", decomposed + supplementary).build();
        assertThat(topic.getEntries().get(1).getValue()).isEqualTo(decomposed + supplementary);
        Topic composedTopic = Topic.builder().addEntry("B", composed).build();
        Topic decomposedTopic = Topic.builder().addEntry("B", decomposed).build();
        assertThat(composedTopic.getEntries().getFirst().getValue()).isEqualTo(composed);
        assertThat(decomposedTopic.getEntries().getFirst().getValue()).isEqualTo(decomposed);
        assertThat(composedTopic).isNotEqualTo(decomposedTopic);
        assertThat(new java.util.HashSet<>(java.util.List.of(composedTopic, decomposedTopic))).hasSize(2);
        assertThat(new TopicEntry("Q X", "letters 123 !€\u0301", "version 2").getValue())
                .isEqualTo("letters 123 !€\u0301");

        assertThatThrownBy(() -> new TopicEntry("A", " leading", null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new TopicEntry("A", "trailing ", null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new TopicEntry("A", "non\u00a0breaking", null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new TopicEntry("A", "control\u0001", null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new TopicEntry("é", "value", null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new TopicEntry("A", "bad" + String.valueOf((char) 0xd800), null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new TopicEntry("A\u007f", "value", null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new TopicEntry("A\u0001", "value", null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new TopicEntry("A", "value", "control\u0001"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new TopicEntry("A", "value", "delete\u007f"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new TopicEntry("A", "value", "non-ASCII é"))
                .isInstanceOf(IllegalArgumentException.class);
        for (String excluded : java.util.List.of("tab\tcontrol", "line\nseparator", "format\u200b",
                "private\ue000", "unassigned\u0378", "line\u2028separator", "paragraph\u2029separator")) {
            assertThatThrownBy(() -> new TopicEntry("A", excluded, null))
                    .as("excluded value %s", excluded)
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    @DisplayName("Candidate field bounds are enforced in Unicode code points before canonical escaping")
    void resourceLimitsApplyToDecodedFields() {
        TopicEntry maximumQualifier = new TopicEntry("q".repeat(64), "value", null);
        TopicEntry maximumValue = new TopicEntry("Q", "🩺".repeat(256), "v".repeat(64));
        assertThat(maximumQualifier.getQualifier()).hasSize(64);
        assertThat(maximumValue.getValue().codePointCount(0, maximumValue.getValue().length())).isEqualTo(256);
        assertThat(maximumValue.getVersion()).contains("v".repeat(64));

        assertThatThrownBy(() -> new TopicEntry("q".repeat(65), "value", null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new TopicEntry("Q", "v".repeat(257), null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new TopicEntry("Q", "value", "v".repeat(65)))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new TopicEntry("Q", "value", "é"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new TopicEntry("Q", "value", " version"))
                .isInstanceOf(IllegalArgumentException.class);

        Topic escapedMaximum = Topic.builder().addEntry("Q", ".".repeat(256), ":".repeat(64)).build();
        assertThat(escapedMaximum.getCanonicalNameWithVersion().length()).isGreaterThan(256);
        assertThat(Topic.fromCanonicalNameWithVersion(escapedMaximum.getCanonicalNameWithVersion()))
                .isEqualTo(escapedMaximum);
        assertThat(Topic.builder().addEntry("Q", "value", "").build().getEntries().getFirst().getVersion())
                .isEmpty();
        assertThatThrownBy(() -> new TopicEntry("Q", "🩺".repeat(257), null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Display projections follow depth-specific rules without altering canonical identity")
    void displayProjectionsArePresentationOnly() {
        Topic root = Topic.builder().addEntry("A", "root").build();
        Topic child = Topic.builder().addEntry("A", "root").addEntry("B", "child").build();
        Topic deep = Topic.builder().addEntry("A", "one").addEntry("B", "two")
                .addEntry("C", "three").addEntry("D", "four").build();

        assertThat(root.getDisplayName()).isEqualTo("root");
        assertThat(child.getDisplayName()).isEqualTo("root-child");
        assertThat(deep.getDisplayName()).isEqualTo("three-four");
        assertThat(deep.getName()).isEqualTo("one.two.three.four");
        assertThat(deep.getCanonicalName()).isEqualTo("A=one.B=two.C=three.D=four");
    }

    @Test
    @DisplayName("Topic and entry lists are immutable defensive snapshots")
    void valuesAreDefensivelyCopied() {
        java.util.ArrayList<TopicEntry> entries = new java.util.ArrayList<>();
        entries.add(new TopicEntry("A", "one", null));
        Topic topic = new Topic(entries);
        entries.add(new TopicEntry("B", "two", null));

        assertThat(topic.getEntries()).hasSize(1);
        assertThatThrownBy(() -> topic.getEntries().add(new TopicEntry("B", "two", null)))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> new Topic(java.util.List.of()))
                .isInstanceOf(IllegalArgumentException.class);

        Topic generated = Topic.builder().addEntry(null, "same").build();
        Topic explicit = Topic.builder().addEntry("N0", "same").build();
        Topic historical = Topic.builder().addEntry("Domain", "same").build();
        assertThat(generated.getEntries().getFirst().getQualifier()).isEqualTo("N0");
        assertThat(generated).isEqualTo(explicit);
        assertThat(generated).isNotEqualTo(historical);
    }

    @Test
    @DisplayName("Jackson uses the ordered logical entry form without a generated UUID")
    void jacksonRoundTripUsesLogicalForm() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Topic topic = Topic.builder().addEntry("A", "one", "v1").addEntry(null, "two").build();
        JsonNode json = mapper.readTree(mapper.writeValueAsBytes(topic));

        assertThat(json.has("entries")).isTrue();
        assertThat(json.has("id")).isFalse();
        assertThat(json.get("entries").get(0).get("value").asText()).isEqualTo("one");
        assertThat(json.get("entries").get(0).get("version").asText()).isEqualTo("v1");
        Topic restored = mapper.treeToValue(json, Topic.class);
        assertThat(restored).isEqualTo(topic);
        assertThat(restored.getEntries().getFirst().getVersion()).contains("v1");
    }

    @Test
    @DisplayName("Java serialization reconstructs a validated Topic snapshot")
    void javaSerializationRoundTrip() throws Exception {
        Topic topic = Topic.builder().addEntry("A", "one", "v1").addEntry("B", "two").build();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (ObjectOutputStream output = new ObjectOutputStream(bytes)) {
            output.writeObject(topic);
        }
        Topic restored;
        try (ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()))) {
            restored = (Topic) input.readObject();
        }
        assertThat(restored).isEqualTo(topic);
        assertThat(restored.getCanonicalNameWithVersion()).isEqualTo(topic.getCanonicalNameWithVersion());
    }
}
