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

/**
 * Candidate-specific resource bounds for immutable Topic and TopicMask values.
 * <p>
 * Final.001.001 requires release-specific bounds but does not allocate numeric
 * values. These values bound the implementation candidate pending Solution
 * Architect review; they do not establish an accepted software release policy.
 */
public final class TopicLimits {

    /** Maximum number of ordered entries in either a Topic or ordinary mask. */
    public static final int MAX_DEPTH = 32;
    /** Maximum decoded code-point length of a completed qualifier. */
    public static final int MAX_QUALIFIER_CODE_POINTS = 64;
    /** Maximum decoded code-point length of a Topic value. */
    public static final int MAX_VALUE_CODE_POINTS = 256;
    /** Maximum decoded code-point length of a present Topic version. */
    public static final int MAX_VERSION_CODE_POINTS = 64;
    /** Maximum original code-point length of one RE2/J expression. */
    public static final int MAX_REGEX_CODE_POINTS = 1024;
    /** Maximum total code-point length of a canonical Topic representation. */
    public static final int MAX_TOPIC_TEXT_CODE_POINTS = MAX_DEPTH * 772;
    /** Maximum total code-point length of an encoded ordinary mask expression. */
    public static final int MAX_MASK_TEXT_CODE_POINTS = MAX_DEPTH * (6 * MAX_REGEX_CODE_POINTS + 8);

    private TopicLimits() {
    }

    /**
     * Rejects a path depth outside the candidate's supported range.
     *
     * @param depth requested number of entries
     * @throws IllegalArgumentException when the depth is negative or too large
     */
    public static void requireDepth(int depth) {
        if (depth < 0 || depth > MAX_DEPTH) {
            throw new IllegalArgumentException("Topic depth is outside the supported range");
        }
    }

    /**
     * Counts Unicode code points and enforces a positive field-specific bound.
     *
     * @param value non-null text to measure
     * @param maximum accepted code-point count
     * @param field field label used in the exception
     * @throws IllegalArgumentException when text is null or exceeds the bound
     */
    public static void requireLength(String value, int maximum, String field) {
        if (value == null) {
            throw new IllegalArgumentException(field + " must not be null");
        }
        int length = value.codePointCount(0, value.length());
        if (length > maximum) {
            throw new IllegalArgumentException(field + " exceeds the supported length");
        }
    }
}