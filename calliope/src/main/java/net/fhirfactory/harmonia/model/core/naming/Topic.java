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

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.fhirfactory.harmonia.model.core.naming.datatypes.TopicEntry;
import net.fhirfactory.harmonia.model.core.naming.factory.TopicBuilder;
import net.fhirfactory.harmonia.model.core.naming.support.CanonicalText;
import net.fhirfactory.harmonia.model.core.naming.support.TopicFieldRules;
import net.fhirfactory.harmonia.model.core.naming.support.TopicLimits;
import net.fhirfactory.harmonia.model.core.naming.valuesets.TopicValueSets;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Immutable root-to-leaf Topic identity defined by its ordered qualifier/value path.
 * <p>
 * Entry versions are retained as data but do not change Topic identity or natural
 * ordering. The logical JSON form contains only ordered entries; no runtime UUID
 * or derived name is stored. This implementation follows the accepted
 * {@code DRP-Topic-and-TopicMask.Final.001.001.md} §§1–3 contract.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE,
        creatorVisibility = JsonAutoDetect.Visibility.NONE)
public final class Topic implements Serializable, Comparable<Topic> {

    private static final long serialVersionUID = 2L;
    private final List<TopicEntry> entries;

    /**
     * Reconstructs a Topic from a nonempty ordered collection of completed entries.
     *
     * @param entries root-to-leaf immutable entry values
     * @throws IllegalArgumentException when the path is empty, too deep, or contains null entries
     */
    @JsonCreator
    public Topic(@JsonProperty("entries") List<TopicEntry> entries) {
        if (entries == null || entries.isEmpty()) {
            throw new IllegalArgumentException("Topic entries must not be empty");
        }
        TopicLimits.requireDepth(entries.size());
        List<TopicEntry> copy = new ArrayList<>(entries.size());
        for (TopicEntry entry : entries) {
            if (entry == null) {
                throw new IllegalArgumentException("Topic entries must not contain null");
            }
            copy.add(entry);
        }
        this.entries = Collections.unmodifiableList(copy);
    }

    /**
     * Legacy five-position constructor retained for direct callers during migration.
     * The optional final qualifier value is omitted when null or empty; other
     * missing or invalid values are rejected rather than silently defaulted.
     *
     * @param domain legacy position zero
     * @param model legacy position one
     * @param modelVersion legacy position two
     * @param dataElement legacy position three
     * @param dataElementQualifier optional legacy position four
     */
    @Deprecated
    public Topic(String domain, String model, String modelVersion, String dataElement, String dataElementQualifier) {
        this(legacyEntries(domain, model, modelVersion, dataElement, dataElementQualifier));
    }

    /**
     * Converts the retained positional constructor into explicitly typed entries.
     * The optional fifth field remains absent when omitted; no former default value
     * or runtime identifier is synthesized.
     */
    private static List<TopicEntry> legacyEntries(String domain, String model, String modelVersion,
                                                   String dataElement, String dataElementQualifier) {
        List<TopicEntry> result = new ArrayList<>(5);
        result.add(new TopicEntry(TopicValueSets.QUALIFIER_DOMAIN, domain, null));
        result.add(new TopicEntry(TopicValueSets.QUALIFIER_MODEL, model, null));
        result.add(new TopicEntry(TopicValueSets.QUALIFIER_MODEL_VERSION, modelVersion, null));
        result.add(new TopicEntry(TopicValueSets.QUALIFIER_DATA_ELEMENT, dataElement, null));
        if (dataElementQualifier != null && !dataElementQualifier.isEmpty()) {
            result.add(new TopicEntry(TopicValueSets.QUALIFIER_DATA_ELEMENT_QUALIFIER, dataElementQualifier, null));
        }
        return result;
    }

    /** Creates the accepted reusable builder. */
    public static TopicBuilder builder() {
        return new TopicBuilder();
    }

    /**
     * Parses strict non-versioned canonical syntax. Every reconstructed entry has an absent version.
     *
     * @param text escaped canonical path
     * @return immutable Topic
     */
    public static Topic fromCanonicalName(String text) {
        return parseCanonical(text, false);
    }

    /**
     * Parses strict canonical syntax with optional per-entry versions.
     *
     * @param text escaped canonical path with optional `qualifier:version=value` segments
     * @return immutable Topic retaining every version
     */
    public static Topic fromCanonicalNameWithVersion(String text) {
        return parseCanonical(text, true);
    }

    /**
     * Performs the shared structural scan, then validates each decoded literal field.
     *
     * @param text complete canonical path
     * @param versioned whether the optional per-entry version segment is permitted
     * @return reconstructed immutable path
     */
    private static Topic parseCanonical(String text, boolean versioned) {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("Canonical Topic text must not be empty");
        }
        TopicFieldRules.requireWellFormedUnicode(text, "canonical Topic text");
        TopicLimits.requireLength(text, TopicLimits.MAX_TOPIC_TEXT_CODE_POINTS, "canonical Topic text");
        List<String> segments = CanonicalText.splitRaw(text, '.', TopicLimits.MAX_DEPTH);
        TopicLimits.requireDepth(segments.size());
        List<TopicEntry> parsed = new ArrayList<>(segments.size());
        for (String segment : segments) {
            if (segment.isEmpty()) {
                throw new IllegalArgumentException("Canonical Topic contains an empty segment");
            }
            List<Integer> equals = CanonicalText.unescapedPositions(segment, '=');
            List<Integer> colons = CanonicalText.unescapedPositions(segment, ':');
            if (equals.size() != 1) {
                throw new IllegalArgumentException("Canonical Topic segment requires one value separator");
            }
            int equalsAt = equals.get(0);
            if (!versioned && !colons.isEmpty()) {
                throw new IllegalArgumentException("Non-versioned canonical Topic cannot contain a version separator");
            }
            if (colons.size() > 1 || (!colons.isEmpty() && colons.get(0) > equalsAt)) {
                throw new IllegalArgumentException("Canonical Topic version separator is misplaced");
            }
            int qualifierEnd = colons.isEmpty() ? equalsAt : colons.get(0);
            String qualifier = CanonicalText.decode(segment.substring(0, qualifierEnd));
            String version = colons.isEmpty() ? null
                    : CanonicalText.decode(segment.substring(colons.get(0) + 1, equalsAt));
            String value = CanonicalText.decode(segment.substring(equalsAt + 1));
            if (version != null && version.isEmpty()) {
                throw new IllegalArgumentException("Canonical Topic version must not be empty");
            }
            parsed.add(new TopicEntry(qualifier, value, version));
        }
        return new Topic(parsed);
    }

    /** @return an immutable root-to-leaf entry list */
    @JsonProperty("entries")
    public List<TopicEntry> getEntries() {
        return entries;
    }

    /** @return escaped canonical identity, excluding entry versions */
    public String getCanonicalName() {
        return canonicalName(false);
    }

    /** @return escaped canonical path including each present entry version */
    public String getCanonicalNameWithVersion() {
        return canonicalName(true);
    }

    /** Builds a canonical projection from the same ordered entries with optional versions. */
    private String canonicalName(boolean includeVersions) {
        StringBuilder result = new StringBuilder();
        for (TopicEntry entry : entries) {
            if (result.length() > 0) {
                result.append('.');
            }
            result.append(CanonicalText.escape(entry.getQualifier()));
            Optional<String> version = entry.getVersion();
            if (includeVersions && version.isPresent()) {
                result.append(':').append(CanonicalText.escape(version.get()));
            }
            result.append('=').append(CanonicalText.escape(entry.getValue()));
        }
        return result.toString();
    }

    /** @return unqualified raw values joined by periods; this projection is not a parse format */
    public String getName() {
        return joinValues(entries.size());
    }

    /** @return escaped non-versioned representation of the final entry */
    public String getRelativeName() {
        TopicEntry leaf = entries.get(entries.size() - 1);
        return CanonicalText.escape(leaf.getQualifier()) + '=' + CanonicalText.escape(leaf.getValue());
    }

    /** @return raw value of the final entry */
    public String getFriendlyName() {
        return entries.get(entries.size() - 1).getValue();
    }

    /** @return final value, or the final two values joined with a hyphen */
    public String getDisplayName() {
        if (entries.size() == 1) {
            return getFriendlyName();
        }
        return entries.get(entries.size() - 2).getValue() + '-' + getFriendlyName();
    }

    /** @return count of ordered entries; a root has depth one */
    public int getDepth() {
        return entries.size();
    }

    /** @return parent path, or null when this Topic is a root */
    public Topic getParent() {
        return entries.size() == 1 ? null : new Topic(entries.subList(0, entries.size() - 1));
    }

    /**
     * Returns the path containing the first {@code depth} entries.
     *
     * @param depth retained entry count, from one through this Topic's depth
     * @return immutable ancestor or this Topic when the requested depth is unchanged
     * @throws IllegalArgumentException when depth is outside the valid range
     */
    public Topic getAncestor(int depth) {
        if (depth < 1 || depth > entries.size()) {
            throw new IllegalArgumentException("Ancestor depth is outside this Topic");
        }
        return depth == entries.size() ? this : new Topic(entries.subList(0, depth));
    }

    /**
     * Tests strict, version-independent literal ancestry.
     *
     * @param candidate possible descendant
     * @return true only when this complete path is a proper prefix of candidate
     */
    public boolean isAncestorOf(Topic candidate) {
        if (candidate == null || entries.size() >= candidate.entries.size()) {
            return false;
        }
        for (int index = 0; index < entries.size(); index++) {
            TopicEntry left = entries.get(index);
            TopicEntry right = candidate.entries.get(index);
            if (!left.getQualifier().equals(right.getQualifier()) || !left.getValue().equals(right.getValue())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Compares qualifier/value pairs lexicographically; versions do not participate.
     *
     * @param other Topic to compare
     * @return deterministic case-sensitive ordering result
     */
    @Override
    public int compareTo(Topic other) {
        Objects.requireNonNull(other, "other Topic");
        int sharedDepth = Math.min(entries.size(), other.entries.size());
        for (int index = 0; index < sharedDepth; index++) {
            TopicEntry left = entries.get(index);
            TopicEntry right = other.entries.get(index);
            int qualifier = left.getQualifier().compareTo(right.getQualifier());
            if (qualifier != 0) {
                return qualifier;
            }
            int value = left.getValue().compareTo(right.getValue());
            if (value != 0) {
                return value;
            }
        }
        return Integer.compare(entries.size(), other.entries.size());
    }

    /**
     * Positional compatibility projection for legacy five-tuple callers.
     *
     * @return entry at index, or null when absent
     */
    @Deprecated
    public TopicEntry getEntry(int index) {
        return index < 0 || index >= entries.size() ? null : entries.get(index);
    }

    /** @return legacy position-zero value, or null when absent */
    @Deprecated
    public String getDomain() { return legacyValue(0); }

    /** @return legacy position-one value, or null when absent */
    @Deprecated
    public String getModel() { return legacyValue(1); }

    /** @return legacy position-two value, or null when absent */
    @Deprecated
    public String getModelVersion() { return legacyValue(2); }

    /** @return legacy position-three value, or null when absent */
    @Deprecated
    public String getDataElement() { return legacyValue(3); }

    /** @return legacy position-four value, or null when absent */
    @Deprecated
    public String getDataElementQualifier() { return legacyValue(4); }

    /** @return legacy composite trigger projection */
    @Deprecated
    public String getCompositeTrigger() {
        String element = getDataElement();
        String qualifier = getDataElementQualifier();
        return element == null && qualifier == null ? "" : (element == null ? "" : element)
                + '^' + (qualifier == null ? "" : qualifier);
    }

    /** @return unqualified raw value projection retained for legacy callers */
    @Deprecated
    public String toTopicString() {
        return getName();
    }

    private String legacyValue(int index) {
        TopicEntry entry = getEntry(index);
        return entry == null ? null : entry.getValue();
    }

    private String joinValues(int count) {
        StringBuilder result = new StringBuilder();
        for (int index = 0; index < count; index++) {
            if (index > 0) {
                result.append('.');
            }
            result.append(entries.get(index).getValue());
        }
        return result.toString();
    }

    private Object writeReplace() {
        return new SerializationProxy(entries);
    }

    private void readObject(ObjectInputStream stream) throws InvalidObjectException {
        throw new InvalidObjectException("Serialization proxy required");
    }

    /**
     * Compares ordered qualifier/value pairs and deliberately ignores entry versions.
     *
     * @param other comparison value
     * @return true when the ordered non-versioned identities are equal
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Topic)) {
            return false;
        }
        Topic that = (Topic) other;
        if (entries.size() != that.entries.size()) {
            return false;
        }
        for (int index = 0; index < entries.size(); index++) {
            TopicEntry left = entries.get(index);
            TopicEntry right = that.entries.get(index);
            if (!left.getQualifier().equals(right.getQualifier()) || !left.getValue().equals(right.getValue())) {
                return false;
            }
        }
        return true;
    }

    /** @return hash derived from the ordered non-versioned identity path */
    @Override
    public int hashCode() {
        int result = 1;
        for (TopicEntry entry : entries) {
            result = 31 * result + entry.getQualifier().hashCode();
            result = 31 * result + entry.getValue().hashCode();
        }
        return result;
    }

    /** @return versioned canonical representation for diagnostics and display */
    @Override
    public String toString() {
        return getCanonicalNameWithVersion();
    }

    private static final class SerializationProxy implements Serializable {
        private static final long serialVersionUID = 1L;
        private final List<TopicEntry> entries;

        private SerializationProxy(List<TopicEntry> entries) {
            this.entries = new ArrayList<>(entries);
        }

        private Object readResolve() {
            return new Topic(entries);
        }
    }
}
