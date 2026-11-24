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
import net.fhirfactory.harmonia.model.core.naming.datatypes.TopicMaskEntry;
import net.fhirfactory.harmonia.model.core.naming.factory.TopicFactory;
import net.fhirfactory.harmonia.model.core.naming.factory.TopicMaskFactory;
import net.fhirfactory.harmonia.model.core.naming.valuesets.MaskType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Accepted TopicMask selector model")
class TopicMaskTest {

    @Test
    @DisplayName("Explicit universal selector matches every non-null Topic and has no ordinary accessors")
    void universalMaskIsExplicit() {
        TopicMask all = TopicMask.all();
        assertThat(all).isSameAs(TopicMaskFactory.unrestricted());
        assertThat(all.isUniversal()).isTrue();
        assertThat(all).isEqualTo(TopicMask.all());
        assertThat(all.hashCode()).isEqualTo(TopicMask.all().hashCode());
        assertThat(all.getEntries()).isEmpty();
        assertThat(all.matches(topic("R", "one"))).isTrue();
        assertThat(all.matches(topic("R", "one", "v1"))).isTrue();
        assertThat(all.matches(Topic.builder().addEntry("R", "one").addEntry("S", "two")
                .addEntry("T", "three").build())).isTrue();
        assertThat(all.matches(null)).isFalse();
        assertThatThrownBy(all::isIncludeContainedElements).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(all::getCanonicalExpression).isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("Ordinary masks match each position independently and default to exact depth")
    void ordinaryMatchingIsPositionalAndExactByDefault() {
        TopicMask exact = TopicMask.builder().addRegex("Region", "North")
                .addRegex("Service", "Clinic").build();
        Topic candidate = Topic.builder().addEntry("Region", "North").addEntry("Service", "Clinic").build();
        Topic extra = Topic.builder().addEntry("Region", "North").addEntry("Service", "Clinic")
                .addEntry("Kind", "Lab").build();
        Topic wrongPosition = Topic.builder().addEntry("Region", "South").addEntry("Region", "North")
                .addEntry("Service", "Clinic").build();

        assertThat(exact.matches(candidate)).isTrue();
        assertThat(exact.matches(extra)).isFalse();
        assertThat(exact.matches(wrongPosition)).isFalse();
        assertThat(TopicMask.builder().addForValue("North").addForValue("Clinic")
                .includeContainedElements(true).build().matches(extra)).isTrue();
    }

    @Test
    @DisplayName("Qualifier, value, and RE2/J expressions are independent full-field criteria")
    void expressionMatchingUsesRe2J() {
        TopicMask mask = TopicMask.builder().addRegex("Q[0-9]+", "value-(A|B)").build();
        assertThat(mask.matches(topic("Q7", "value-B"))).isTrue();
        assertThat(mask.matches(topic("Q7", "xvalue-B"))).isFalse();
        assertThat(mask.matches(topic("Qx", "value-B"))).isFalse();
        assertThatThrownBy(() -> TopicMaskEntry.of("(?=Q)", "value"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> TopicMaskEntry.of("(Q)\\1", "value"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> TopicMaskEntry.of(null, "value"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> TopicMaskEntry.of("Q", null))
                .isInstanceOf(IllegalArgumentException.class);

        TopicMask scoped = TopicMask.builder().addRegex("(?i:Zone[0-9]+)", "(LAB|MED)\\.[A:=]", "2\\.4")
                .build();
        Topic scopedCandidate = Topic.builder().addEntry("zone7", "LAB.=", "2.4").build();
        assertThat(scoped.matches(scopedCandidate)).isTrue();
        assertThat(TopicMask.fromCanonicalExpression(scoped.getCanonicalExpression(), false))
                .isEqualTo(scoped);
        assertThat(TopicMask.builder().addRegex("", "").build().matches(topic("Q", "value"))).isFalse();
    }

    @Test
    @DisplayName("Versions are optional constraints with independent presence and expression state")
    void versionPresenceIsStructural() {
        Topic absent = topic("Q", "value");
        Topic present = topic("Q", "value", "v2");
        TopicMask unconstrained = TopicMask.builder().addRegex("Q", "value").build();
        TopicMask anyPresent = TopicMask.builder().addRegex("Q", "value", ".*").build();
        TopicMask emptyExpression = TopicMask.builder().addRegex("Q", "value", "").build();
        TopicMask versionTwo = TopicMask.builder().addRegex("Q", "value", "v2").build();
        TopicMask explicitAbsent = TopicMask.builder().addRegex("Q", "value", null).build();

        assertThat(unconstrained.matches(absent)).isTrue();
        assertThat(unconstrained.matches(present)).isTrue();
        assertThat(absent).isEqualTo(present);
        assertThat(anyPresent.matches(absent)).isFalse();
        assertThat(anyPresent.matches(present)).isTrue();
        assertThat(versionTwo.matches(absent)).isFalse();
        assertThat(versionTwo.matches(present)).isTrue();
        assertThat(versionTwo.matches(topic("Q", "value", "v3"))).isFalse();
        assertThat(emptyExpression.getEntries().getFirst().getVersionRegex()).contains("");
        assertThat(emptyExpression.matches(absent)).isFalse();
        assertThat(emptyExpression.matches(present)).isFalse();
        assertThat(unconstrained).isEqualTo(explicitAbsent);
        assertThat(unconstrained).isNotEqualTo(emptyExpression);
    }

    @Test
    @DisplayName("Canonical mask expressions escape reserved characters and reconstruct original expressions")
    void canonicalExpressionRoundTrips() {
        TopicMask original = TopicMask.builder()
                .add(TopicMaskEntry.ofLiterals("Q.a", "value:=\\", "v:1"))
                .includeContainedElements(true).build();
        String expression = original.getCanonicalExpression();
        TopicMask reconstructed = TopicMask.fromCanonicalExpression(expression, true);
        Topic candidate = Topic.builder().addEntry("Q.a", "value:=\\", "v:1").addEntry("Extra", "tail").build();

        assertThat(reconstructed).isEqualTo(original);
        assertThat(reconstructed.getCanonicalExpression()).isEqualTo(expression);
        assertThat(reconstructed.matches(candidate)).isTrue();
        assertThat(TopicMask.fromCanonicalExpression("Q=", false).getCanonicalExpression()).isEqualTo("Q=");
        assertThat(TopicMask.fromCanonicalExpression("Q:=value", false)
                .getEntries().getFirst().getVersionRegex()).contains("");
        assertThat(TopicMask.fromCanonicalExpression("=", false).getCanonicalExpression()).isEqualTo("=");
        TopicMask noContainment = TopicMask.builder().addRegex("Q", "value").build();
        assertThat(TopicMask.fromCanonicalExpression(noContainment.getCanonicalExpression(), false))
                .isEqualTo(noContainment);
    }

    @Test
    @DisplayName("Canonical and legacy parsers reject malformed or ambiguous input instead of selecting universally")
    void invalidSelectorsFailClosed() {
        for (String invalid : List.of("", ".Q=v", "Q=v.", "Q=v=x", "Q=v\\q", "@all")) {
            assertThatThrownBy(() -> TopicMask.fromCanonicalExpression(invalid, true))
                    .as("invalid canonical mask %s", invalid)
                    .isInstanceOf(IllegalArgumentException.class);
        }
        for (String invalid : new String[]{null, "", " ", "*", ".*", "A..B", "A\\.B", "("}) {
            assertThatThrownBy(() -> TopicMaskFactory.fromNamePattern(invalid))
                    .as("invalid legacy selector %s", invalid)
                    .isInstanceOf(IllegalArgumentException.class);
        }
        String excessiveLegacyDepth = String.join(".", java.util.Collections.nCopies(34, "value"));
        assertThatThrownBy(() -> TopicMaskFactory.fromNamePattern(excessiveLegacyDepth))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> TopicMask.ordinary(List.of(), true))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> TopicMaskFactory.nameBased(null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> TopicMaskEntry.ofLiterals("", "value"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> TopicMaskEntry.ofLiterals("Q", ""))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> TopicMaskEntry.ofLiterals("Q", "value", ""))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("A structurally universal ordinary mask remains distinct from explicit universality")
    void ordinaryUniversalCoverageDoesNotChangeKind() {
        TopicMask ordinary = TopicMask.builder().addRegex("(?s:.*)", "(?s:.*)")
                .includeContainedElements(true).build();
        TopicMask reconstructed = TopicMask.fromCanonicalExpression(ordinary.getCanonicalExpression(), true);

        assertThat(ordinary.isUniversal()).isFalse();
        assertThat(reconstructed).isEqualTo(ordinary);
        assertThat(ordinary).isNotEqualTo(TopicMask.all());
        assertThat(ordinary.matches(Topic.builder().addEntry("Q", "value", "v1")
                .addEntry("Next", "tail").build())).isTrue();
    }

    @Test
    @DisplayName("Legacy Exact and Inclusive adapters preserve name-only and qualifier/value scope")
    void legacySelectorsPreserveScope() {
        Topic simple = TopicFactory.fromName("Health.HL7.2.4.ADT.A01");
        TopicMask inclusiveName = TopicMaskFactory.fromNamePattern("Health.HL7.2.4.*", MaskType.Inclusive);
        TopicMask exactName = TopicMaskFactory.fromNamePattern("Health.HL7.2.4.*.*", MaskType.Exact);
        Topic qualified = Topic.builder().addEntry("Mode", "FHIR").addEntry("Resource", "Practitioner").build();
        Topic extra = Topic.builder().addEntry("Mode", "FHIR").addEntry("Resource", "Practitioner")
                .addEntry("Interaction", "READ").build();
        TopicMask exactCanonical = TopicMaskFactory.fromNamePattern("Mode=FHIR.Resource=Practitioner", MaskType.Exact);
        TopicMask inclusiveCanonical = TopicMaskFactory.fromNamePattern(
                "Mode=FHIR.Resource=Practitioner", MaskType.Inclusive);

        assertThat(inclusiveName.matches(simple)).isTrue();
        assertThat(exactName.matches(simple)).isTrue();
        assertThat(exactCanonical.matches(qualified)).isTrue();
        assertThat(exactCanonical.matches(extra)).isFalse();
        assertThat(inclusiveCanonical.matches(extra)).isTrue();
    }

    @Test
    @DisplayName("Literal legacy name masks quote metacharacters and reject null Topics")
    void nameBasedUsesLiteralValues() {
        Topic original = Topic.builder().addEntry("First", "v.1").addEntry("Second", "x+y").build();
        TopicMask mask = TopicMaskFactory.nameBased(original);

        assertThat(mask.matches(Topic.builder().addEntry("Other", "v.1").addEntry("Other", "x+y")
                .addEntry("Extra", "tail").build())).isTrue();
        assertThat(mask.matches(Topic.builder().addEntry("Other", "vX1").addEntry("Other", "x+y").build()))
                .isFalse();
    }

    @Test
    @DisplayName("Jackson logical forms distinguish universal from ordinary masks")
    void jacksonRoundTripsLogicalKinds() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TopicMask universal = TopicMask.all();
        assertThat(mapper.writeValueAsString(universal)).isEqualTo("{\"universal\":true}");
        assertThat(mapper.readValue(mapper.writeValueAsBytes(universal), TopicMask.class)).isEqualTo(universal);

        TopicMask ordinary = TopicMask.builder().addLiterals("Q", "value").build();
        JsonNode json = mapper.readTree(mapper.writeValueAsBytes(ordinary));
        assertThat(json.has("universal")).isFalse();
        assertThat(json.has("entries")).isTrue();
        assertThat(json.get("includeContainedElements").asBoolean()).isFalse();
        assertThat(mapper.treeToValue(json, TopicMask.class)).isEqualTo(ordinary);
        assertThat(ordinary.toString()).isEqualTo(ordinary.getCanonicalExpression());
        assertThatThrownBy(() -> mapper.readValue("{\"universal\":true,\"entries\":[]}", TopicMask.class))
                .isInstanceOf(Exception.class);
        assertThatThrownBy(() -> mapper.readValue(
                "{\"universal\":true,\"includeContainedElements\":false}", TopicMask.class))
                .isInstanceOf(Exception.class);
        TopicMask emptyVersion = TopicMask.builder().addRegex("Q", "value", "").build();
        TopicMask restoredEmptyVersion = mapper.readValue(mapper.writeValueAsBytes(emptyVersion), TopicMask.class);
        assertThat(restoredEmptyVersion).isEqualTo(emptyVersion);
        assertThat(restoredEmptyVersion.getEntries().getFirst().getVersionRegex()).contains("");
    }

    @Test
    @DisplayName("Java serialization reconstructs both ordinary and universal masks")
    void javaSerializationRoundTrips() throws Exception {
        TopicMask ordinary = TopicMask.builder().addRegex("Q", "value", ".*")
                .includeContainedElements(true).build();
        assertThat(javaRoundTrip(ordinary)).isEqualTo(ordinary);
        assertThat(javaRoundTrip(TopicMask.all())).isSameAs(TopicMask.all());
    }

    private static Topic topic(String qualifier, String value, String version) {
        return Topic.builder().addEntry(qualifier, value, version).build();
    }

    private static Topic topic(String qualifier, String value) {
        return Topic.builder().addEntry(qualifier, value).build();
    }

    private static TopicMask javaRoundTrip(TopicMask mask) throws Exception {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (ObjectOutputStream output = new ObjectOutputStream(bytes)) {
            output.writeObject(mask);
        }
        try (ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()))) {
            return (TopicMask) input.readObject();
        }
    }
}
