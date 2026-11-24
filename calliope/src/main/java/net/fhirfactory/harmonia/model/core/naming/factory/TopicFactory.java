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
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

/**
 * Constructs compatibility Topics for established HL7, FHIR, IU, and name-based call sites.
 * <p>
 * Name input is never trimmed. Unqualified legacy names use generated positional
 * qualifiers; canonical qualified input is delegated to the strict Topic parser.
 */
public final class TopicFactory {

    private TopicFactory() {
    }

    /**
     * Builds a Topic from a legacy unqualified dot path or strict canonical path.
     *
     * @param name dot-delimited values or `qualifier=value` canonical text
     * @return immutable Topic
     * @throws IllegalArgumentException when the path is empty, ambiguous, or invalid
     */
    public static Topic fromName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Topic name must not be empty");
        }
        if (name.indexOf('=') >= 0) {
            return Topic.fromCanonicalName(name);
        }
        if (name.indexOf('\\') >= 0) {
            throw new IllegalArgumentException("Escaped delimiters require canonical Topic syntax");
        }
        TopicFieldRules.requireWellFormedUnicode(name, "Topic name");
        TopicLimits.requireLength(name, TopicLimits.MAX_TOPIC_TEXT_CODE_POINTS, "Topic name");
        requireLegacySegmentUpperBound(name);
        List<String> values = new ArrayList<>(List.of(name.split("\\.", -1)));
        if (values.size() >= 4 && isAsciiDigits(values.get(2)) && isAsciiDigits(values.get(3))) {
            values.set(2, values.get(2) + '.' + values.get(3));
            values.remove(3);
        }
        TopicBuilder builder = Topic.builder();
        for (String value : values) {
            builder.addEntry(null, value);
        }
        return builder.build();
    }

    /** Constructs the established unqualified IU designation from its independent UUID. */
    public static Topic forInformationUnit(UUID informationUnitId) {
        Objects.requireNonNull(informationUnitId, "informationUnitId must not be null");
        return forInformationUnit(informationUnitId.toString().replace("-", ""));
    }

    /**
     * Constructs `harmonia.mneme.iu.<compressed-uuid>` while using generated
     * qualifiers; no unadjudicated fully qualified IU vocabulary is introduced.
     *
     * @param informationUnitId 32 hexadecimal digits, optionally in UUID dashed form
     * @return immutable four-entry Topic
     */
    public static Topic forInformationUnit(String informationUnitId) {
        if (informationUnitId == null || informationUnitId.isEmpty()) {
            throw new IllegalArgumentException("InformationUnit UUID must not be empty");
        }
        String hex;
        if (informationUnitId.length() == 32 && isAsciiHex(informationUnitId)) {
            hex = informationUnitId;
        } else if (informationUnitId.length() == 36
                && informationUnitId.charAt(8) == '-'
                && informationUnitId.charAt(13) == '-'
                && informationUnitId.charAt(18) == '-'
                && informationUnitId.charAt(23) == '-') {
            hex = informationUnitId.replace("-", "");
        } else {
            throw new IllegalArgumentException("InformationUnit UUID must be compressed or standard dashed form");
        }
        if (hex.length() != 32 || !isAsciiHex(hex)) {
            throw new IllegalArgumentException("InformationUnit UUID must contain 32 hexadecimal digits");
        }
        TopicBuilder builder = Topic.builder();
        builder.addEntry(null, "harmonia");
        builder.addEntry(null, "mneme");
        builder.addEntry(null, "iu");
        builder.addEntry(null, hex.toLowerCase(Locale.ROOT));
        return builder.build();
    }

    /** Creates the established HL7 v2.4 composite-trigger Topic. */
    public static Topic fromHl7(String messageType, String triggerEvent) {
        return fromHl7("2.4", messageType, triggerEvent);
    }

    /** Creates an HL7 Topic using the legacy five-position qualifier projection. */
    public static Topic fromHl7(String version, String messageType, String triggerEvent) {
        return new Topic(TopicValueSets.DEFAULT_DOMAIN, "HL7", defaultIfEmpty(version, "2.4"),
                defaultIfEmpty(messageType, "ADT"), defaultIfEmpty(triggerEvent, "A01"));
    }

    /** Creates an HL7 Topic from an `ADT^A01` trigger or a message type. */
    public static Topic fromHl7(String triggerType) {
        if (triggerType == null || triggerType.isEmpty()) {
            return fromHl7("ADT", "A01");
        }
        int separator = triggerType.indexOf('^');
        if (separator < 0) {
            return fromHl7(triggerType, "ALL");
        }
        if (separator != triggerType.lastIndexOf('^') || separator == 0 || separator == triggerType.length() - 1) {
            throw new IllegalArgumentException("HL7 trigger contains an ambiguous separator");
        }
        return fromHl7(triggerType.substring(0, separator), triggerType.substring(separator + 1));
    }

    /** Creates an FHIR R5 interaction Topic. */
    public static Topic fromFhir(String resourceType, String interaction) {
        return new Topic(TopicValueSets.DEFAULT_DOMAIN, "FHIR", "R5",
                defaultIfEmpty(resourceType, "Practitioner"), defaultIfEmpty(interaction, "CREATE"));
    }

    private static String defaultIfEmpty(String value, String fallback) {
        return value == null || value.isEmpty() ? fallback : value;
    }

    private static boolean isAsciiDigits(String value) {
        if (value.isEmpty()) {
            return false;
        }
        for (int index = 0; index < value.length(); index++) {
            if (value.charAt(index) < '0' || value.charAt(index) > '9') {
                return false;
            }
        }
        return true;
    }

    private static boolean isAsciiHex(String value) {
        for (int index = 0; index < value.length(); index++) {
            char character = value.charAt(index);
            if (!((character >= '0' && character <= '9') || (character >= 'a' && character <= 'f')
                    || (character >= 'A' && character <= 'F'))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Bounds allocation before splitting a legacy path. One extra segment is
     * temporarily allowed for the established numeric model-version collapse.
     */
    private static void requireLegacySegmentUpperBound(String name) {
        int segmentCount = 1;
        for (int index = 0; index < name.length(); index++) {
            if (name.charAt(index) == '.' && ++segmentCount > TopicLimits.MAX_DEPTH + 1) {
                throw new IllegalArgumentException("Legacy Topic name exceeds the supported depth");
            }
        }
    }
}
