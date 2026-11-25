/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming;

/**
 * Bounded component spans for a numeric version, compared without numeric conversion.
 *
 * <p>Leading zeroes are ignored for comparison and absent trailing components
 * are equivalent to zero. The original string remains untouched for callers.</p>
 */
final class NumericVersion {
    private final String text;
    private final int[] starts;
    private final int[] ends;

    private NumericVersion(String text, int[] starts, int[] ends) {
        this.text = text;
        this.starts = starts;
        this.ends = ends;
    }

    /** Parses a constraint operand, returning null when it is outside the numeric grammar. */
    static NumericVersion parse(String text) {
        if (text == null || text.isEmpty()
                || text.length() > NamingLimits.MAX_NUMERIC_VERSION_OPERAND_CHARS) {
            return null;
        }
        int componentCount = 1;
        for (int offset = 0; offset < text.length(); offset++) {
            char current = text.charAt(offset);
            if (current == '.') {
                componentCount++;
            } else if (current < '0' || current > '9') {
                return null;
            }
        }
        if (componentCount > NamingLimits.MAX_NUMERIC_VERSION_COMPONENTS
                || text.charAt(0) == '.' || text.charAt(text.length() - 1) == '.') {
            return null;
        }

        int[] starts = new int[componentCount];
        int[] ends = new int[componentCount];
        int component = 0;
        starts[0] = 0;
        for (int offset = 0; offset < text.length(); offset++) {
            if (text.charAt(offset) == '.') {
                if (offset == starts[component]) {
                    return null;
                }
                ends[component] = offset;
                starts[++component] = offset + 1;
            }
        }
        if (starts[component] == text.length()) {
            return null;
        }
        ends[component] = text.length();
        return new NumericVersion(text, starts, ends);
    }

    /** Compares component values lexically, treating missing components as zero. */
    int compareTo(NumericVersion other) {
        int count = Math.max(starts.length, other.starts.length);
        for (int component = 0; component < count; component++) {
            int comparison = compareComponent(other, component);
            if (comparison != 0) {
                return comparison;
            }
        }
        return 0;
    }

    /** Compares one component after ignoring leading zeroes, with missing components as zero. */
    private int compareComponent(NumericVersion other, int component) {
        int thisStart = component < starts.length ? significantStart(component) : text.length();
        int thisEnd = component < ends.length ? ends[component] : text.length();
        int otherStart = component < other.starts.length
                ? other.significantStart(component) : other.text.length();
        int otherEnd = component < other.ends.length ? other.ends[component] : other.text.length();
        int thisLength = thisEnd - thisStart;
        int otherLength = otherEnd - otherStart;
        if (thisLength != otherLength) {
            return Integer.compare(thisLength, otherLength);
        }
        for (int offset = 0; offset < thisLength; offset++) {
            char left = text.charAt(thisStart + offset);
            char right = other.text.charAt(otherStart + offset);
            if (left != right) {
                return Character.compare(left, right);
            }
        }
        return 0;
    }

    /** Skips every leading zero; an empty significant span represents numeric zero. */
    private int significantStart(int component) {
        int start = starts[component];
        int end = ends[component];
        while (start < end && text.charAt(start) == '0') {
            start++;
        }
        return start;
    }
}