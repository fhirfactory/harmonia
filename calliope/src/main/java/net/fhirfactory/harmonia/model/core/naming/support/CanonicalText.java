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

package net.fhirfactory.harmonia.model.core.naming.support;

import java.util.ArrayList;
import java.util.List;

/**
 * Shared outer-layer escaping and scanning for Topic and TopicMask canonical text.
 * <p>
 * Only `.`, `=`, `:`, and backslash may be escaped. Callers parse the decoded
 * field text according to their own grammar after structural delimiters have
 * been located here.
 */
public final class CanonicalText {

    private CanonicalText() {
    }

    /**
     * Escapes canonical structural characters in one decoded field.
     *
     * @param text field text
     * @return text suitable for insertion into canonical syntax
     */
    public static String escape(String text) {
        if (text == null) {
            throw new IllegalArgumentException("canonical field must not be null");
        }
        StringBuilder escaped = new StringBuilder(text.length());
        for (int index = 0; index < text.length(); index++) {
            char current = text.charAt(index);
            if (isStructural(current)) {
                escaped.append('\\');
            }
            escaped.append(current);
        }
        return escaped.toString();
    }

    /**
     * Splits raw canonical text at unescaped occurrences of one delimiter.
     * Escape pairs remain in each returned part for later field decoding.
     *
     * @param text canonical text
     * @param delimiter structural delimiter to split on
     * @return ordered raw segments
     * @throws IllegalArgumentException for malformed or unknown escapes
     */
    public static List<String> splitRaw(String text, char delimiter) {
        return splitRaw(text, delimiter, Integer.MAX_VALUE);
    }

    /**
     * Splits canonical text while enforcing a maximum number of parts during scanning.
     *
     * @param text canonical text
     * @param delimiter structural delimiter
     * @param maximumParts maximum returned segment count
     * @return ordered raw segments
     * @throws IllegalArgumentException for malformed escapes or excess parts
     */
    public static List<String> splitRaw(String text, char delimiter, int maximumParts) {
        if (text == null) {
            throw new IllegalArgumentException("canonical text must not be null");
        }
        if (maximumParts < 1) {
            throw new IllegalArgumentException("maximumParts must be positive");
        }
        List<String> parts = new ArrayList<>();
        int start = 0;
        for (int index = 0; index < text.length(); index++) {
            char current = text.charAt(index);
            if (current == '\\') {
                if (index + 1 >= text.length() || !isStructural(text.charAt(index + 1))) {
                    throw new IllegalArgumentException("canonical text contains a malformed escape");
                }
                index++;
            } else if (current == delimiter) {
                parts.add(text.substring(start, index));
                if (parts.size() >= maximumParts) {
                    throw new IllegalArgumentException("canonical path exceeds the supported depth");
                }
                start = index + 1;
            }
        }
        parts.add(text.substring(start));
        if (parts.size() > maximumParts) {
            throw new IllegalArgumentException("canonical path exceeds the supported depth");
        }
        return parts;
    }

    /**
     * Returns the unescaped positions of a structural character in raw text.
     *
     * @param text one raw canonical segment
     * @param target delimiter to locate
     * @return all matching indices in ascending order
     * @throws IllegalArgumentException for malformed or unknown escapes
     */
    public static List<Integer> unescapedPositions(String text, char target) {
        if (text == null) {
            throw new IllegalArgumentException("canonical segment must not be null");
        }
        List<Integer> positions = new ArrayList<>();
        for (int index = 0; index < text.length(); index++) {
            char current = text.charAt(index);
            if (current == '\\') {
                if (index + 1 >= text.length() || !isStructural(text.charAt(index + 1))) {
                    throw new IllegalArgumentException("canonical text contains a malformed escape");
                }
                index++;
            } else if (current == target) {
                positions.add(index);
            }
        }
        return positions;
    }

    /**
     * Removes exactly one canonical escaping layer from a raw field.
     *
     * @param raw raw field text
     * @return decoded field text
     * @throws IllegalArgumentException for malformed or unknown escapes
     */
    public static String decode(String raw) {
        if (raw == null) {
            throw new IllegalArgumentException("canonical field must not be null");
        }
        StringBuilder decoded = new StringBuilder(raw.length());
        for (int index = 0; index < raw.length(); index++) {
            char current = raw.charAt(index);
            if (current != '\\') {
                decoded.append(current);
                continue;
            }
            if (index + 1 >= raw.length() || !isStructural(raw.charAt(index + 1))) {
                throw new IllegalArgumentException("canonical text contains a malformed escape");
            }
            decoded.append(raw.charAt(++index));
        }
        return decoded.toString();
    }

    private static boolean isStructural(char character) {
        return character == '.' || character == '=' || character == ':' || character == '\\';
    }
}