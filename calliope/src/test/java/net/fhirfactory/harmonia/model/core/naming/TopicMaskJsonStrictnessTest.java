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
import net.fhirfactory.harmonia.model.core.information.operations.OperationalMetadataMask;
import net.fhirfactory.harmonia.model.core.naming.datatypes.TopicMaskEntry;
import net.fhirfactory.harmonia.model.core.naming.support.TopicLimits;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Strict TopicMask logical JSON decoding")
class TopicMaskJsonStrictnessTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Only an exact explicit discriminator represents a universal mask")
    void universalFormIsExplicitAndExclusive() throws Exception {
        assertThat(objectMapper.readValue("{\"universal\":true}", TopicMask.class).isUniversal()).isTrue();
        assertRejected("{\"universal\":null,\"entries\":[{\"qualifierRegex\":\"Q\",\"valueRegex\":\"v\"}],"
                + "\"includeContainedElements\":false}");
        assertRejected("{\"universal\":\"true\"}");
        assertRejected("{\"universal\":1}");
        assertRejected("{\"universal\":false}");
        assertRejected("{\"universal\":true,\"entries\":null}");
        assertRejected("{\"universal\":true,\"includeContainedElements\":null}");
        assertRejected("{\"universal\":true,\"entries\":[],\"includeContainedElements\":false}");
        assertRejected("{}");
        assertRejected("{\"entries\":null,\"includeContainedElements\":false}");
    }

    @Test
    @DisplayName("Duplicate, unknown, null, and coerced logical properties are rejected")
    void ambiguousPropertiesAreRejected() {
        assertRejected("{\"universal\":true,\"universal\":true}");
        assertRejected("{\"entries\":[],\"entries\":[],\"includeContainedElements\":false}");
        assertRejected("{\"entries\":[{\"qualifierRegex\":\"Q\",\"valueRegex\":\"v\","
                + "\"valueRegex\":\"other\"}],\"includeContainedElements\":false}");
        assertRejected("{\"entries\":[{\"qualifierRegex\":\"Q\",\"valueRegex\":7}],"
                + "\"includeContainedElements\":false}");
        assertRejected("{\"entries\":[{\"qualifierRegex\":\"Q\",\"valueRegex\":\"v\","
                + "\"versionRegex\":7}],\"includeContainedElements\":false}");
        assertRejected("{\"entries\":[{\"qualifierRegex\":\"Q\",\"valueRegex\":\"v\","
                + "\"unexpected\":\"value\"}],\"includeContainedElements\":false}");
        assertRejected("{\"entries\":[null],\"includeContainedElements\":false}");
        assertRejected("{\"entries\":[{\"qualifierRegex\":\"Q\",\"valueRegex\":\"[\"}],"
                + "\"includeContainedElements\":false}");
        assertThatThrownBy(() -> objectMapper.readValue(
                "{\"qualifierRegex\":\"SENSITIVE-REGEX(\",\"valueRegex\":\"v\"}", TopicMaskEntry.class))
                .isInstanceOf(Exception.class)
                .hasNoCause()
                .hasMessageNotContaining("SENSITIVE-REGEX");
    }

    @Test
    @DisplayName("Nested consumer models use the same strict TopicMask decoder")
    void nestedRepresentationsRejectMalformedMasks() {
        String malformed = "{\"universal\":null,\"entries\":[{\"qualifierRegex\":\"Q\",\"valueRegex\":\"v\"}],"
                + "\"includeContainedElements\":false}";
        assertThatThrownBy(() -> objectMapper.readValue("{\"actionTopicMasks\":[" + malformed + "]}",
                InformationSecurityContext.class)).isInstanceOf(Exception.class);
        assertThatThrownBy(() -> objectMapper.readValue(
                "{\"id\":\"123e4567-e89b-12d3-a456-426614174000\",\"subjectMask\":" + malformed + "}",
                OperationalMetadataMask.class)).isInstanceOf(Exception.class);
        assertThatThrownBy(() -> objectMapper.readValue("{\"actionTopicMasks\":[null]}",
                InformationSecurityContext.class)).isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("Ordinary breadth and optional version state survive logical round trips")
    void ordinaryKindAndVersionPresenceSurviveRoundTrip() throws Exception {
        TopicMask broad = objectMapper.readValue("{\"entries\":[{\"qualifierRegex\":\".*\",\"valueRegex\":\".*\"}],"
                + "\"includeContainedElements\":true}", TopicMask.class);
        assertThat(broad.isUniversal()).isFalse();
        assertThat(broad.matches(Topic.builder().addEntry("Q", "v").addEntry("Other", "tail").build())).isTrue();

        List<String> logicalForms = List.of(
                "{\"entries\":[{\"qualifierRegex\":\"Q\",\"valueRegex\":\"v\"}],\"includeContainedElements\":false}",
                "{\"entries\":[{\"qualifierRegex\":\"Q\",\"valueRegex\":\"v\",\"versionRegex\":\"\"}],"
                        + "\"includeContainedElements\":false}",
                "{\"entries\":[{\"qualifierRegex\":\"Q\",\"valueRegex\":\"v\",\"versionRegex\":\".*\"}],"
                        + "\"includeContainedElements\":false}");
        assertThat(objectMapper.readValue(logicalForms.get(0), TopicMask.class).getEntries().getFirst()
                .getVersionRegex()).isEmpty();
        assertThat(objectMapper.readValue(logicalForms.get(1), TopicMask.class).getEntries().getFirst()
                .getVersionRegex()).contains("");
        assertThat(objectMapper.readValue(logicalForms.get(2), TopicMask.class).getEntries().getFirst()
                .getVersionRegex()).contains(".*");
    }

    @Test
    @DisplayName("Logical entry count is bounded while the JSON array is being read")
    void rejectsExcessiveDepth() {
        List<String> entries = new ArrayList<>();
        for (int index = 0; index <= TopicLimits.MAX_DEPTH; index++) {
            entries.add("{\"qualifierRegex\":\"Q\",\"valueRegex\":\"v\"}");
        }
        assertRejected("{\"entries\":[" + String.join(",", entries)
                + "],\"includeContainedElements\":false}");
    }

    private void assertRejected(String json) {
        assertThatThrownBy(() -> objectMapper.readValue(json, TopicMask.class))
                .isInstanceOf(Exception.class);
    }
}