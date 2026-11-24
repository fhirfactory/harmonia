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
import net.fhirfactory.harmonia.model.core.naming.datatypes.TopicMaskEntry;
import net.fhirfactory.harmonia.model.core.naming.support.TopicFieldRules;
import net.fhirfactory.harmonia.model.core.naming.support.TopicLimits;
import net.fhirfactory.harmonia.model.core.naming.valuesets.MaskType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Compatibility adapters for established TopicMask selector inputs.
 * <p>
 * Unrestricted intent is explicit, ordinary selectors preserve positional
 * criteria, and malformed or ambiguous legacy paths never become universal.
 */
public final class TopicMaskFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(TopicMaskFactory.class);

    private TopicMaskFactory() {
    }

    /** Returns the explicit universal mask; retained as a named legacy adapter. */
    public static TopicMask unrestricted() {
        return TopicMask.all();
    }

    /**
     * Builds an inclusive literal-value path mask from a Topic.
     *
     * @param topic non-null Topic supplying literal values
     * @return inclusive ordinary mask
     * @throws IllegalArgumentException when topic is null
     */
    public static TopicMask nameBased(Topic topic) {
        if (topic == null) {
            logConstructionFailure("literal-path");
            throw new IllegalArgumentException("topic must not be null");
        }
        List<String> values = topic.getEntries().stream().map(entry -> entry.getValue()).toList();
        return forLiteralValuePath(values);
    }

    /** Builds an inclusive positional mask from a legacy dot-delimited pattern. */
    public static TopicMask fromNamePattern(String dotSeparatedPattern) {
        return fromNamePattern(dotSeparatedPattern, MaskType.Inclusive);
    }

    /**
     * Maps legacy Exact/Inclusive scope to exact-depth or trailing-containment semantics.
     * Name-only segments become value criteria; `qualifier=value` segments remain
     * independent expression criteria. Escaped periods are rejected because a
     * legacy path cannot distinguish them from selector structure.
     *
     * @param dotSeparatedPattern legacy selector path
     * @param maskType Exact or Inclusive
     * @return immutable ordinary mask
     */
    public static TopicMask fromNamePattern(String dotSeparatedPattern, MaskType maskType) {
        try {
            return createLegacyMask(dotSeparatedPattern, maskType);
        } catch (IllegalArgumentException exception) {
            logConstructionFailure("legacy-selector");
            throw exception;
        }
    }

    /**
     * Converts the legacy Exact/Inclusive input without assigning universal meaning
     * to malformed paths or silently interpreting escaped periods as structure.
     */
    private static TopicMask createLegacyMask(String dotSeparatedPattern, MaskType maskType) {
        if (dotSeparatedPattern == null || dotSeparatedPattern.isEmpty()
                || dotSeparatedPattern.isBlank() || "*".equals(dotSeparatedPattern)
                || ".*".equals(dotSeparatedPattern)) {
            throw new IllegalArgumentException("Legacy selector must be a nonempty ordinary path");
        }
        if (maskType == null) {
            throw new IllegalArgumentException("Legacy selector depth mode must not be null");
        }
        TopicFieldRules.requireWellFormedUnicode(dotSeparatedPattern, "legacy selector");
        TopicLimits.requireLength(dotSeparatedPattern, TopicLimits.MAX_MASK_TEXT_CODE_POINTS,
                "legacy selector");
        List<String> segments = splitLegacyPath(dotSeparatedPattern);
        boolean nameOnly = segments.stream().noneMatch(segment -> segment.indexOf('=') >= 0);
        if (nameOnly && segments.size() >= 4
                && isAsciiDigits(segments.get(2)) && isAsciiDigits(segments.get(3))) {
            segments.set(2, segments.get(2) + "\\." + segments.get(3));
            segments.remove(3);
        }
        TopicLimits.requireDepth(segments.size());
        List<Segment> parsed = new ArrayList<>(segments.size());
        for (String segment : segments) {
            if (segment.isEmpty() || segment.isBlank()) {
                throw new IllegalArgumentException("Legacy selector contains an empty segment");
            }
            parsed.add(parseSegment(segment));
        }
        TopicMaskBuilder builder = TopicMask.builder()
                .includeContainedElements(maskType == MaskType.Inclusive);
        for (Segment segment : parsed) {
            if (segment.qualifierRegex == null) {
                builder.addForValue(wildcard(segment.valueRegex));
            } else {
                builder.addRegex(wildcard(segment.qualifierRegex), wildcard(segment.valueRegex));
            }
        }
        return builder.build();
    }

    /**
     * Builds an inclusive literal-value path for derived action Topics and similar adapters.
     *
     * @param values ordered literal Topic values
     * @return inclusive ordinary mask
     */
    public static TopicMask forLiteralValuePath(List<String> values) {
        try {
            return createLiteralValuePath(values);
        } catch (IllegalArgumentException exception) {
            logConstructionFailure("literal-path");
            throw exception;
        }
    }

    /**
     * Recognises only an ordinary inclusive mask composed of quoted literal-value
     * entries, as produced by this factory or the equivalent builder helpers.
     * Other expressions return empty rather than being interpreted as literals.
     *
     * @param mask candidate mask
     * @return the complete ordered literal value path when it can be proved
     */
    public static Optional<List<String>> literalValuePath(TopicMask mask) {
        if (mask == null || mask.isUniversal() || !mask.isIncludeContainedElements() || mask.getEntries().isEmpty()) {
            return Optional.empty();
        }
        List<String> values = new ArrayList<>(mask.getEntries().size());
        for (TopicMaskEntry entry : mask.getEntries()) {
            if (!".*".equals(entry.getQualifierRegex()) || entry.getVersionRegex().isPresent()) {
                return Optional.empty();
            }
            Optional<String> literal = unquote(entry.getValueRegex());
            if (literal.isEmpty()) {
                return Optional.empty();
            }
            try {
                TopicFieldRules.requireValue(literal.get());
            } catch (IllegalArgumentException exception) {
                return Optional.empty();
            }
            values.add(literal.get());
        }
        return Optional.of(List.copyOf(values));
    }

    private static TopicMask createLiteralValuePath(List<String> values) {
        if (values == null || values.isEmpty()) {
            throw new IllegalArgumentException("Literal value path must not be empty");
        }
        TopicLimits.requireDepth(values.size());
        TopicMaskBuilder builder = TopicMask.builder().includeContainedElements(true);
        for (String value : values) {
            builder.addForLiteralValue(value);
        }
        return builder.build();
    }

    private static void logConstructionFailure(String category) {
        try {
            LOGGER.error("TopicMask factory rejected input category={}", category);
        } catch (RuntimeException ignored) {
            // A diagnostic failure cannot change the invalid construction result.
        }
    }

    /**
     * Splits legacy path separators while preserving non-structural RE2/J escapes.
     * An escaped period is rejected because its old structural meaning is ambiguous.
     */
    private static List<String> splitLegacyPath(String text) {
        List<String> segments = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        for (int index = 0; index < text.length(); index++) {
            char character = text.charAt(index);
            if (character == '\\') {
                if (index + 1 >= text.length()) {
                    throw new IllegalArgumentException("Legacy selector has a dangling escape");
                }
                char escaped = text.charAt(++index);
                if (escaped == '.') {
                    throw new IllegalArgumentException("Escaped period is ambiguous in a legacy selector");
                }
                current.append('\\').append(escaped);
            } else if (character == '.') {
                if (segments.size() >= TopicLimits.MAX_DEPTH) {
                    throw new IllegalArgumentException("Legacy selector exceeds the supported depth");
                }
                segments.add(current.toString());
                current.setLength(0);
            } else {
                current.append(character);
            }
        }
        segments.add(current.toString());
        return segments;
    }

    /**
     * Recognises at most one unescaped qualifier/value separator in a legacy segment.
     * Multiple separators fail closed instead of changing the selector basis.
     */
    private static Segment parseSegment(String segment) {
        int separator = -1;
        boolean escaped = false;
        for (int index = 0; index < segment.length(); index++) {
            char character = segment.charAt(index);
            if (escaped) {
                escaped = false;
            } else if (character == '\\') {
                escaped = true;
            } else if (character == '=') {
                if (separator >= 0) {
                    throw new IllegalArgumentException("Legacy selector has ambiguous equals delimiters");
                }
                separator = index;
            }
        }
        if (escaped) {
            throw new IllegalArgumentException("Legacy selector has a dangling escape");
        }
        if (separator < 0) {
            return new Segment(null, segment);
        }
        String qualifier = segment.substring(0, separator);
        String value = segment.substring(separator + 1);
        if (qualifier.isEmpty() || value.isEmpty()) {
            throw new IllegalArgumentException("Legacy selector qualifier and value must be present");
        }
        return new Segment(qualifier, value);
    }

    private static String wildcard(String expression) {
        return "*".equals(expression) ? ".*" : expression;
    }

    /**
     * Inverts only RE2/J {@code Pattern.quote} output; arbitrary expressions are
     * not exposed as literals for action-name compatibility projection.
     */
    private static Optional<String> unquote(String expression) {
        if (expression == null) {
            return Optional.empty();
        }
        StringBuilder literal = new StringBuilder();
        for (int index = 0; index < expression.length();) {
            char current = expression.charAt(index);
            if (current == '\\') {
                if (index + 1 >= expression.length() || !isRegexMeta(expression.charAt(index + 1))) {
                    return Optional.empty();
                }
                literal.append(expression.charAt(index + 1));
                index += 2;
            } else if (isRegexMeta(current)) {
                return Optional.empty();
            } else {
                literal.append(current);
                index++;
            }
        }
        return Optional.of(literal.toString());
    }

    private static boolean isRegexMeta(char character) {
        return "\\.+*?()|[]{}^$".indexOf(character) >= 0;
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

    private static final class Segment {
        private final String qualifierRegex;
        private final String valueRegex;

        private Segment(String qualifierRegex, String valueRegex) {
            this.qualifierRegex = qualifierRegex;
            this.valueRegex = valueRegex;
        }
    }
}
