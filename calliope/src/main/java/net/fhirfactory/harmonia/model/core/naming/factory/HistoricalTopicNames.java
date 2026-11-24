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
import net.fhirfactory.harmonia.model.core.naming.support.TopicFieldRules;
import net.fhirfactory.harmonia.model.core.naming.support.TopicLimits;
import net.fhirfactory.harmonia.model.core.naming.valuesets.TopicValueSets;

import java.util.ArrayList;
import java.util.List;

/**
 * Compatibility parser for the verified pre-Topic-model name convention.
 * <p>
 * It keeps historical positional qualifiers and numeric dotted-version collapse
 * separate from current generated {@code N<position>} Topic defaults. Backslash,
 * empty segments, and surrounding whitespace are rejected rather than guessed.
 */
public final class HistoricalTopicNames {

    private static final String[] STANDARD_QUALIFIERS = {
            TopicValueSets.QUALIFIER_DOMAIN,
            TopicValueSets.QUALIFIER_MODEL,
            TopicValueSets.QUALIFIER_MODEL_VERSION,
            TopicValueSets.QUALIFIER_DATA_ELEMENT,
            TopicValueSets.QUALIFIER_DATA_ELEMENT_QUALIFIER
    };

    private HistoricalTopicNames() {
    }

    /**
     * Converts a historical dotted name using fixed qualifiers and legacy q=v
     * segments. Ambiguous escaped, empty, or whitespace-padded input is rejected.
     * A numeric dotted model version is collapsed only when no segment is qualified.
     *
     * @param name historical Topic name
     * @return validated Topic with historical qualifier identity
     */
    public static Topic fromName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Historical Topic name must not be blank");
        }
        TopicFieldRules.requireWellFormedUnicode(name, "historical Topic name");
        TopicLimits.requireLength(name, TopicLimits.MAX_TOPIC_TEXT_CODE_POINTS, "historical Topic name");
        if (name.indexOf('\\') >= 0 || !name.equals(name.strip())) {
            throw new IllegalArgumentException("Historical Topic name has unsupported boundary syntax");
        }

        String[] parts = name.split("\\.", -1);
        if (parts.length > TopicLimits.MAX_DEPTH + 1) {
            throw new IllegalArgumentException("Historical Topic name exceeds the supported depth");
        }
        List<String> segments = new ArrayList<>(List.of(parts));
        boolean qualified = name.indexOf('=') >= 0;
        if (!qualified && segments.size() >= 4
                && isNumeric(segments.get(2)) && isNumeric(segments.get(3))) {
            segments.set(2, segments.get(2) + '.' + segments.get(3));
            segments.remove(3);
        }
        TopicLimits.requireDepth(segments.size());

        TopicBuilder builder = Topic.builder();
        for (int index = 0; index < segments.size(); index++) {
            String segment = segments.get(index);
            if (segment.isEmpty() || !segment.equals(segment.strip())) {
                throw new IllegalArgumentException("Historical Topic name contains an empty or padded segment");
            }
            int separator = segment.indexOf('=');
            if (separator >= 0) {
                String qualifier = segment.substring(0, separator);
                String value = segment.substring(separator + 1);
                if (qualifier.isEmpty() || value.isEmpty()
                        || !qualifier.equals(qualifier.strip()) || !value.equals(value.strip())) {
                    throw new IllegalArgumentException("Historical Topic qualified segment is incomplete");
                }
                builder.addEntry(qualifier, value);
            } else {
                String qualifier = index < STANDARD_QUALIFIERS.length
                        ? STANDARD_QUALIFIERS[index] : "Level" + index;
                builder.addEntry(qualifier, segment);
            }
        }
        return builder.build();
    }

    /** Mirrors the predecessor's numeric-segment test used only for legacy version collapse. */
    private static boolean isNumeric(String value) {
        if (value.isEmpty()) {
            return false;
        }
        for (int index = 0; index < value.length(); index++) {
            if (!Character.isDigit(value.charAt(index))) {
                return false;
            }
        }
        return true;
    }
}