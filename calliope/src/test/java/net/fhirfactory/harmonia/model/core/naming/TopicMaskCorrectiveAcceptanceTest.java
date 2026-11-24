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

import com.fasterxml.jackson.databind.ObjectMapper;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationSecurityContext;
import net.fhirfactory.harmonia.model.core.naming.datatypes.TopicMaskEntry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Iteration 008 corrective acceptance vectors (independent review C-05) that are not
 * already exercised by the Topic, TopicMask, evaluator, and strict-decoding suites.
 */
@DisplayName("TopicMask corrective acceptance vectors")
class TopicMaskCorrectiveAcceptanceTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Hash collections distinguish mask kind, containment, and version presence")
    void hashCollectionsUseSemanticMaskState() {
        TopicMask exact = TopicMask.builder().addRegex("Q", "v").build();
        TopicMask exactAgain = TopicMask.builder().addRegex("Q", "v").build();
        TopicMask inclusive = TopicMask.builder().addRegex("Q", "v").includeContainedElements(true).build();
        TopicMask emptyVersion = TopicMask.ordinary(List.of(TopicMaskEntry.of("Q", "v", "")), false);
        TopicMask broad = TopicMask.builder().addRegex(".*", ".*").includeContainedElements(true).build();

        Set<TopicMask> masks = new HashSet<>(List.of(exact, exactAgain, inclusive, emptyVersion, broad,
                TopicMask.all(), TopicMask.all()));
        assertThat(masks).hasSize(5);

        Map<TopicMask, String> routes = new HashMap<>();
        routes.put(exact, "exact");
        routes.put(TopicMask.all(), "universal");
        assertThat(routes.get(exactAgain)).isEqualTo("exact");
        assertThat(routes.get(broad)).isNull();
        assertThat(routes.get(emptyVersion)).isNull();
    }

    @Test
    @DisplayName("Java serialization retains each entry's version expression and presence")
    void javaSerializationRetainsVersionExpressions() throws Exception {
        TopicMask mask = TopicMask.ordinary(List.of(
                TopicMaskEntry.of("Q", "v", "2\\.4"),
                TopicMaskEntry.of("R", "w", ""),
                TopicMaskEntry.of("S", "x")), true);

        TopicMask restored = javaRoundTrip(mask);

        assertThat(restored.getEntries()).extracting(entry -> entry.getVersionRegex())
                .containsExactly(java.util.Optional.of("2\\.4"), java.util.Optional.of(""),
                        java.util.Optional.empty());
        assertThat(restored.isIncludeContainedElements()).isTrue();
        assertThat(javaRoundTrip(TopicMask.all())).isSameAs(TopicMask.all());
    }

    @Test
    @DisplayName("Composed and decomposed Unicode literals remain distinct through logical JSON")
    void unicodeFormsArePreservedThroughJson() throws Exception {
        String composed = "\u00e9";
        String decomposed = "e\u0301";
        TopicMask mask = TopicMask.builder().addLiterals("Q", decomposed).build();

        TopicMask restored = objectMapper.readValue(objectMapper.writeValueAsString(mask), TopicMask.class);

        assertThat(restored).isEqualTo(mask);
        assertThat(restored.matches(Topic.builder().addEntry("Q", decomposed).build())).isTrue();
        assertThat(restored.matches(Topic.builder().addEntry("Q", composed).build())).isFalse();
    }

    @Test
    @DisplayName("Universal and broad ordinary security masks keep their kinds through JSON")
    void securityContextKeepsMaskKinds() throws Exception {
        TopicMask broad = TopicMask.builder().addRegex(".*", ".*").includeContainedElements(true).build();
        InformationSecurityContext context = InformationSecurityContext.builder()
                .addActionMask(broad)
                .addCompartmentMask(TopicMask.all())
                .build();

        InformationSecurityContext restored = objectMapper.readValue(
                objectMapper.writeValueAsString(context), InformationSecurityContext.class);

        assertThat(restored).isEqualTo(context);
        assertThat(restored.actionTopicMasks()).singleElement()
                .satisfies(mask -> assertThat(mask.isUniversal()).isFalse());
        assertThat(restored.compartmentMasks()).singleElement()
                .satisfies(mask -> assertThat(mask.isUniversal()).isTrue());
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
