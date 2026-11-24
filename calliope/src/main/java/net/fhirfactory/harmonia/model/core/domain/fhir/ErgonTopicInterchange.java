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

package net.fhirfactory.harmonia.model.core.domain.fhir;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.fhirfactory.harmonia.model.core.naming.Topic;
import net.fhirfactory.harmonia.model.core.naming.factory.HistoricalTopicNames;
import net.fhirfactory.harmonia.model.core.naming.factory.TopicBuilder;
import net.fhirfactory.harmonia.model.core.naming.support.TopicLimits;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Explicit interchange boundary for Topic values embedded in Ergon Task extensions.
 * <p>
 * Three representations can appear in stored Task extensions, and each has its own
 * validated reader selected by shape rather than by trial-and-error fallback:
 * <ul>
 *   <li>current logical JSON ({@code {"entries":[...]}}), the only form now written;</li>
 *   <li>the predecessor's Jackson JSON ({@code id}, integer-keyed {@code canonicalName}
 *       and derived getter projections), whose derived members are verified against
 *       the reconstructed entries rather than trusted;</li>
 *   <li>the predecessor's plain dotted-name fallback, which is always interpreted with
 *       the historical naming convention in {@link HistoricalTopicNames}.</li>
 * </ul>
 * A predecessor UUID is syntax-checked but cannot become current Topic identity,
 * because the current model's identity is its ordered entries. No identity or
 * qualifier migration is inferred. Every rejection uses one fixed message and never
 * chains the parser exception, so stored payload text cannot reach diagnostics.
 */
final class ErgonTopicInterchange {

    private static final Set<String> CURRENT_FIELDS = Set.of("entries");
    private static final Set<String> HISTORICAL_FIELDS = Set.of("id", "canonicalName", "name",
            "friendlyName", "relativeName", "depth", "shortId", "domain", "model", "modelVersion",
            "dataElement", "dataElementQualifier", "compositeTrigger");
    /** Predecessor getters that projected the entry name at a fixed position (0..4). */
    private static final List<String> HISTORICAL_POSITIONAL_FIELDS =
            List.of("domain", "model", "modelVersion", "dataElement", "dataElementQualifier");
    private static final Set<String> HISTORICAL_ENTRY_FIELDS = Set.of("qualifier", "name");
    private static final ObjectMapper MAPPER = new ObjectMapper(
            new JsonFactory().enable(JsonParser.Feature.STRICT_DUPLICATE_DETECTION));

    private ErgonTopicInterchange() {
    }

    /**
     * Writes only the current ordered logical JSON representation; never degrades to a
     * name projection that would lose qualifiers or versions.
     *
     * @param topic Topic to write, or null
     * @return current logical JSON, or null for a null Topic
     * @throws IllegalStateException if JSON generation fails
     */
    static String serialize(Topic topic) {
        if (topic == null) {
            return null;
        }
        try {
            return MAPPER.writeValueAsString(topic);
        } catch (IOException exception) {
            throw new IllegalStateException("Topic JSON serialization failed");
        }
    }

    /**
     * Dispatches by explicit representation shape and rejects malformed or ambiguous values.
     * Text that looks like JSON is never re-read as a name after a JSON failure; plain text
     * can only have been written by the predecessor's fallback and is read historically.
     *
     * @param value stored extension text; null or blank means no Topic
     * @return reconstructed Topic, or null when no value was stored
     * @throws IllegalArgumentException for oversized, malformed, inconsistent, or unsupported input
     */
    static Topic deserialize(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            TopicLimits.requireLength(value, TopicLimits.MAX_TOPIC_TEXT_CODE_POINTS, "Ergon Topic interchange");
            String firstContent = value.stripLeading();
            if (firstContent.startsWith("{") || firstContent.startsWith("[")) {
                return deserializeJson(value);
            }
            return HistoricalTopicNames.fromName(value);
        } catch (IllegalArgumentException | IllegalStateException exception) {
            throw invalidTopic();
        }
    }

    /** Parses JSON with duplicate-member detection and dispatches only recognised Topic shapes. */
    private static Topic deserializeJson(String value) {
        JsonNode root;
        try {
            root = MAPPER.readTree(value);
        } catch (IOException exception) {
            throw invalidTopic();
        }
        if (root == null || !root.isObject()) {
            throw invalidTopic();
        }
        if (root.has("entries")) {
            return readCurrent(root);
        }
        if (root.has("id") || root.has("canonicalName")) {
            return readHistorical(root);
        }
        throw invalidTopic();
    }

    /** Validates current ordered entries before passing the logical data to the configured model binder. */
    private static Topic readCurrent(JsonNode root) {
        if (!hasOnlyFields(root, CURRENT_FIELDS)) {
            throw invalidTopic();
        }
        JsonNode entries = root.get("entries");
        if (!entries.isArray() || entries.isEmpty() || entries.size() > TopicLimits.MAX_DEPTH) {
            throw invalidTopic();
        }
        for (JsonNode entry : entries) {
            if (!entry.isObject() || !hasOnlyFields(entry, Set.of("qualifier", "value", "version"))
                    || !entry.has("qualifier") || !entry.get("qualifier").isTextual()
                    || !entry.has("value") || !entry.get("value").isTextual()
                    || (entry.has("version") && !entry.get("version").isTextual())) {
                throw invalidTopic();
            }
        }
        try {
            return MAPPER.treeToValue(root, Topic.class);
        } catch (IOException | RuntimeException exception) {
            throw invalidTopic();
        }
    }

    /**
     * Reconstructs a predecessor path from its contiguous integer-keyed entries;
     * derived fields are consistency checks, not independent identity sources.
     */
    private static Topic readHistorical(JsonNode root) {
        if (!hasOnlyFields(root, HISTORICAL_FIELDS) || !root.has("id") || !root.get("id").isTextual()
                || !validUuid(root.get("id").textValue()) || !root.has("canonicalName")) {
            throw invalidTopic();
        }
        JsonNode canonicalName = root.get("canonicalName");
        if (!canonicalName.isObject() || canonicalName.isEmpty() || canonicalName.size() > TopicLimits.MAX_DEPTH) {
            throw invalidTopic();
        }
        TopicBuilder builder = Topic.builder();
        StringBuilder derivedName = new StringBuilder();
        String leafName = null;
        String leafQualifier = null;
        int depth = canonicalName.size();
        String[] names = new String[depth];
        for (int index = 0; index < depth; index++) {
            JsonNode entry = canonicalName.get(Integer.toString(index));
            if (entry == null || !entry.isObject() || !hasOnlyFields(entry, HISTORICAL_ENTRY_FIELDS)
                    || !entry.has("qualifier") || !entry.get("qualifier").isTextual()
                    || !entry.has("name") || !entry.get("name").isTextual()) {
                throw invalidTopic();
            }
            String qualifier = entry.get("qualifier").textValue();
            String name = entry.get("name").textValue();
            try {
                builder.addEntry(qualifier, name);
            } catch (IllegalArgumentException exception) {
                throw invalidTopic();
            }
            if (index > 0) {
                derivedName.append('.');
            }
            derivedName.append(name);
            names[index] = name;
            leafName = name;
            leafQualifier = qualifier;
        }
        Topic topic;
        try {
            topic = builder.build();
        } catch (IllegalArgumentException | IllegalStateException exception) {
            throw invalidTopic();
        }
        validateDerived(root, "name", derivedName.toString());
        validateDerived(root, "friendlyName", leafName);
        validateDerived(root, "relativeName", leafQualifier + "=" + leafName);
        validateDerived(root, "shortId", root.get("id").textValue().substring(0, 8));
        for (int position = 0; position < HISTORICAL_POSITIONAL_FIELDS.size(); position++) {
            validateDerived(root, HISTORICAL_POSITIONAL_FIELDS.get(position),
                    position < depth ? names[position] : null);
        }
        String element = depth > 3 ? names[3] : null;
        String elementQualifier = depth > 4 ? names[4] : null;
        validateDerived(root, "compositeTrigger", element == null && elementQualifier == null ? ""
                : (element == null ? "" : element) + "^" + (elementQualifier == null ? "" : elementQualifier));
        if (root.has("depth") && (!root.get("depth").isIntegralNumber() || root.get("depth").intValue() != depth)) {
            throw invalidTopic();
        }
        return topic;
    }

    /**
     * Checks an optional predecessor projection against the value reconstructed from
     * entries. A null expectation requires an explicit JSON null, matching what the
     * predecessor wrote for positions beyond the Topic's depth.
     */
    private static void validateDerived(JsonNode root, String field, String expected) {
        if (!root.has(field)) {
            return;
        }
        JsonNode node = root.get(field);
        boolean consistent = expected == null ? node.isNull()
                : node.isTextual() && expected.equals(node.textValue());
        if (!consistent) {
            throw invalidTopic();
        }
    }

    /** Rejects fields outside one representation's explicitly supported schema. */
    private static boolean hasOnlyFields(JsonNode node, Set<String> allowed) {
        Iterator<String> fields = node.fieldNames();
        while (fields.hasNext()) {
            if (!allowed.contains(fields.next())) {
                return false;
            }
        }
        return true;
    }

    /** Validates canonical UUID syntax without retaining the predecessor identifier. */
    private static boolean validUuid(String value) {
        if (value == null || value.length() != 36 || value.charAt(8) != '-' || value.charAt(13) != '-'
                || value.charAt(18) != '-' || value.charAt(23) != '-') {
            return false;
        }
        for (int index = 0; index < value.length(); index++) {
            if (index == 8 || index == 13 || index == 18 || index == 23) {
                continue;
            }
            char character = value.charAt(index);
            if (!((character >= '0' && character <= '9') || (character >= 'a' && character <= 'f')
                    || (character >= 'A' && character <= 'F'))) {
                return false;
            }
        }
        try {
            UUID.fromString(value);
            return true;
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }

    /** Produces a payload-safe failure without attaching parser or user-input details. */
    private static IllegalArgumentException invalidTopic() {
        return new IllegalArgumentException("Invalid or ambiguous Ergon Topic representation");
    }
}