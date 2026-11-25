/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Immutable ordered root-to-leaf identity for a named Harmonia concept.
 *
 * <p>Identity is the case-sensitive path of qualifier/value pairs. Optional
 * entry versions remain available as data but do not change equality, hashing,
 * or natural ordering.</p>
 *
 * <p>Names and renderings may contain sensitive input and are not sanitized for
 * operational diagnostics or logging.</p>
 */
public final class Topic implements Comparable<Topic> {
    private final List<TopicEntry> entries;

    /** Copies a completed ordered path after checking its nonempty bounded depth. */
    private Topic(List<TopicEntry> entries) {
        if (entries.isEmpty() || entries.size() > NamingLimits.MAX_DEPTH) {
            throw TopicFieldRules.failure(entries.isEmpty() ? NamingFailureCategory.MISSING_INPUT
                            : NamingFailureCategory.LIMIT_EXCEEDED,
                    NamingOperation.TOPIC_BUILD, NamingFieldKind.DEPTH, null);
        }
        this.entries = List.copyOf(entries);
    }

    /**
     * Returns a new sequential or indexed builder for a completed Topic.
     *
     * @return an empty caller-confined TopicBuilder
     */
    public static TopicBuilder builder() {
        return new TopicBuilder();
    }

    /**
     * Parses the strict version-independent canonical identity form; all parsed versions are absent.
     *
     * @param text escaped ordered qualifier/value path under the completed-field grammar
     * @return an immutable Topic with no entry versions
     * @throws NamingValidationException when input is absent, malformed or above the UTF-8 byte limit
     */
    public static Topic fromCanonicalName(String text) {
        return TopicCanonicalParser.parse(text, false);
    }

    /**
     * Parses the strict version-capable canonical form, preserving opaque versions.
     *
     * @param text escaped ordered qualifier/version/value path under the completed-field grammar
     * @return an immutable Topic preserving the original optional version spellings
     * @throws NamingValidationException when input is absent, malformed or above the UTF-8 byte limit
     */
    public static Topic fromCanonicalNameWithVersion(String text) {
        return TopicCanonicalParser.parse(text, true);
    }

    /** Creates a defensive snapshot from already completed immutable entries. */
    static Topic fromEntries(List<TopicEntry> entries) {
        return new Topic(entries);
    }

    /**
     * Returns the immutable ordered entry snapshot.
     *
     * @return the ordered completed entries
     */
    public List<TopicEntry> getEntries() {
        return entries;
    }

    /**
     * Returns the escaped canonical path that establishes Topic identity.
     *
     * <p>The rendering may contain sensitive field text and is not sanitized for
     * operational diagnostics or logging.</p>
     *
     * @return ordered escaped qualifier/value path without versions
     */
    public String getCanonicalName() {
        return canonicalName(false);
    }

    /**
     * Returns the escaped canonical path including each present entry version.
     *
     * <p>The rendering may contain sensitive field text and is not sanitized for
     * operational diagnostics or logging.</p>
     *
     * @return ordered escaped qualifier/version/value path
     */
    public String getCanonicalNameWithVersion() {
        return canonicalName(true);
    }

    /**
     * Returns the raw value projection joined with periods.
     *
     * @return values in path order, joined with a period
     */
    public String getName() {
        return joinValues(0, entries.size());
    }

    /**
     * Returns the escaped qualified representation of the final entry.
     *
     * @return the final entry's qualifier/value representation
     */
    public String getRelativeName() {
        TopicEntry last = entries.get(entries.size() - 1);
        return CanonicalPathSyntax.encode(last.getQualifier()) + "="
                + CanonicalPathSyntax.encode(last.getValue());
    }

    /**
     * Returns the raw value of the final entry.
     *
     * @return the final entry value
     */
    public String getFriendlyName() {
        return entries.get(entries.size() - 1).getValue();
    }

    /**
     * Returns the final value, or the final two values joined with a hyphen.
     *
     * @return the display projection for this path
     */
    public String getDisplayName() {
        if (entries.size() == 1) {
            return getFriendlyName();
        }
        return entries.get(entries.size() - 2).getValue() + "-" + getFriendlyName();
    }

    /**
     * Returns the number of ordered path entries.
     *
     * @return the Topic depth, from one through sixteen
     */
    public int getDepth() {
        return entries.size();
    }

    /**
     * Returns the strict parent path, or {@code null} for a root Topic.
     *
     * @return the immutable parent path, or null when this Topic has one entry
     */
    public Topic getParent() {
        return entries.size() == 1 ? null : fromEntries(entries.subList(0, entries.size() - 1));
    }

    /**
     * Returns the path prefix at the requested depth, retaining entry versions.
     *
     * @param depth requested prefix depth, from one through the current depth
     * @return the immutable Topic prefix
     * @throws NamingValidationException when depth is outside the accepted range
     */
    public Topic getAncestor(int depth) {
        if (depth < 1 || depth > entries.size()) {
            throw TopicFieldRules.failure(NamingFailureCategory.INVALID_ARGUMENT,
                    NamingOperation.TOPIC_NAVIGATE, NamingFieldKind.INDEX, null);
        }
        return depth == entries.size() ? this : fromEntries(entries.subList(0, depth));
    }

    /**
     * Returns whether this path is a strict, version-independent ancestor of candidate.
     *
     * @param candidate path to compare; null never matches
     * @return true only for a matching strict prefix
     */
    public boolean isAncestorOf(Topic candidate) {
        if (candidate == null || entries.size() >= candidate.entries.size()) {
            return false;
        }
        for (int index = 0; index < entries.size(); index++) {
            TopicEntry left = entries.get(index);
            TopicEntry right = candidate.entries.get(index);
            if (!left.getQualifier().equals(right.getQualifier())
                    || !left.getValue().equals(right.getValue())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Orders paths by each case-sensitive qualifier/value pair, then proper prefix.
     *
     * @param other path to compare
     * @return negative, zero, or positive according to lexicographic path order
     * @throws NullPointerException when other is null
     */
    @Override
    public int compareTo(Topic other) {
        Objects.requireNonNull(other, "other");
        int common = Math.min(entries.size(), other.entries.size());
        for (int index = 0; index < common; index++) {
            TopicEntry left = entries.get(index);
            TopicEntry right = other.entries.get(index);
            int qualifierOrder = left.getQualifier().compareTo(right.getQualifier());
            if (qualifierOrder != 0) {
                return qualifierOrder;
            }
            int valueOrder = left.getValue().compareTo(right.getValue());
            if (valueOrder != 0) {
                return valueOrder;
            }
        }
        return Integer.compare(entries.size(), other.entries.size());
    }

    /** Compares ordered qualifier/value identity and deliberately ignores versions. */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Topic topic) || entries.size() != topic.entries.size()) {
            return false;
        }
        for (int index = 0; index < entries.size(); index++) {
            TopicEntry left = entries.get(index);
            TopicEntry right = topic.entries.get(index);
            if (!left.getQualifier().equals(right.getQualifier())
                    || !left.getValue().equals(right.getValue())) {
                return false;
            }
        }
        return true;
    }

    /** Hashes the same ordered qualifier/value identity used by {@link #equals(Object)}. */
    @Override
    public int hashCode() {
        int result = 1;
        for (TopicEntry entry : entries) {
            result = 31 * result + Objects.hash(entry.getQualifier(), entry.getValue());
        }
        return result;
    }

    /**
     * Returns the complete version-capable canonical path as a contract rendering.
     *
     * <p>This may contain sensitive field text and is not sanitized diagnostic output.</p>
     *
     * @return escaped canonical path including present entry versions
     */
    @Override
    public String toString() {
        return getCanonicalNameWithVersion();
    }

    /** Emits one of the two strict canonical projections with the shared escaping rules. */
    private String canonicalName(boolean withVersion) {
        List<String> segments = new ArrayList<>(entries.size());
        for (TopicEntry entry : entries) {
            StringBuilder segment = new StringBuilder(CanonicalPathSyntax.encode(entry.getQualifier()));
            if (withVersion && entry.versionOrNull() != null) {
                segment.append(':').append(CanonicalPathSyntax.encode(entry.versionOrNull()));
            }
            segment.append('=').append(CanonicalPathSyntax.encode(entry.getValue()));
            segments.add(segment.toString());
        }
        return String.join(".", segments);
    }

    /** Joins the selected raw values without claiming canonical identity syntax. */
    private String joinValues(int start, int end) {
        List<String> values = new ArrayList<>(end - start);
        for (int index = start; index < end; index++) {
            values.add(entries.get(index).getValue());
        }
        return String.join(".", values);
    }
}