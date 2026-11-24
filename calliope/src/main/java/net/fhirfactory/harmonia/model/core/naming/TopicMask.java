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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import net.fhirfactory.harmonia.model.core.naming.datatypes.TopicMaskEntry;
import net.fhirfactory.harmonia.model.core.naming.factory.TopicMaskBuilder;
import net.fhirfactory.harmonia.model.core.naming.support.CanonicalText;
import net.fhirfactory.harmonia.model.core.naming.support.TopicFieldRules;
import net.fhirfactory.harmonia.model.core.naming.support.TopicMaskJsonDecoder;
import net.fhirfactory.harmonia.model.core.naming.support.TopicLimits;
import net.fhirfactory.harmonia.model.core.naming.support.TopicMaskEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Immutable neutral positional selector with distinct ordinary and universal kinds.
 * <p>
 * A successful match conveys no access, disclosure, routing, publication, or
 * persistence authority. Security decisions remain the responsibility of Themis.
 * Equality and hashing distinguish ordinary expression state from explicit
 * universality; ordinary masks retain ordered expressions and containment.
 * Logical JSON is either exactly {@code {"universal":true}} or a complete
 * ordinary object with entries and an explicit containment flag.
 * This implementation follows {@code DRP-Topic-and-TopicMask.Final.001.001.md} §§4–7.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE,
        creatorVisibility = JsonAutoDetect.Visibility.NONE)
@JsonDeserialize(using = TopicMaskJsonDecoder.class)
public final class TopicMask implements Serializable {

    private static final long serialVersionUID = 2L;
    private static final Logger LOGGER = LoggerFactory.getLogger(TopicMask.class);
    private static final TopicMask UNIVERSAL = new TopicMask(true, List.of(), false);

    private final boolean universal;
    private final List<TopicMaskEntry> entries;
    private final boolean includeContainedElements;

    private TopicMask(boolean universal, List<TopicMaskEntry> entries, boolean includeContainedElements) {
        this.universal = universal;
        this.entries = Collections.unmodifiableList(new ArrayList<>(entries));
        this.includeContainedElements = includeContainedElements;
    }

    /**
     * Creates a builder for an ordinary mask. Builders never produce the universal kind.
     *
     * @return new mutable, single-thread builder
     */
    public static TopicMaskBuilder builder() {
        return new TopicMaskBuilder();
    }

    /**
     * Returns the explicit universal kind, which matches every non-null Topic.
     * This is the only route to universal selection; empty ordinary criteria are invalid.
     *
     * @return shared immutable universal mask
     */
    public static TopicMask all() {
        return UNIVERSAL;
    }

    /**
     * Reconstructs an ordinary mask from validated entries. Intended for builders
     * and logical adapters; an empty list never means universal.
     *
     * @param entries nonempty ordered criteria
     * @param includeContainedElements whether trailing Topic entries may be present
     * @return immutable ordinary mask
     */
    public static TopicMask ordinary(List<TopicMaskEntry> entries, boolean includeContainedElements) {
        try {
            if (entries == null || entries.isEmpty()) {
                throw new IllegalArgumentException("Ordinary TopicMask requires at least one entry");
            }
            TopicLimits.requireDepth(entries.size());
            List<TopicMaskEntry> copy = new ArrayList<>(entries.size());
            for (TopicMaskEntry entry : entries) {
                if (entry == null) {
                    throw new IllegalArgumentException("Ordinary TopicMask entries must not contain null");
                }
                copy.add(entry);
            }
            return new TopicMask(false, copy, includeContainedElements);
        } catch (IllegalArgumentException exception) {
            logConstructionFailure();
            throw exception;
        }
    }

    /**
     * Parses a strict ordinary canonical expression and compiles every entry.
     * No text token, including `@all`, means universal.
     *
     * @param expression escaped canonical mask expression
     * @param includeContainedElements whether trailing Topic entries are allowed
     * @return immutable ordinary mask
     * @throws IllegalArgumentException for empty, malformed, oversized, or uncompilable expressions
     */
    public static TopicMask fromCanonicalExpression(String expression, boolean includeContainedElements) {
        try {
            if (expression == null || expression.isEmpty()) {
                throw new IllegalArgumentException("Canonical mask expression must not be empty");
            }
            TopicFieldRules.requireWellFormedUnicode(expression, "canonical mask expression");
            TopicLimits.requireLength(expression, TopicLimits.MAX_MASK_TEXT_CODE_POINTS,
                    "canonical mask expression");
            List<String> segments = CanonicalText.splitRaw(expression, '.', TopicLimits.MAX_DEPTH);
            TopicLimits.requireDepth(segments.size());
            List<TopicMaskEntry> parsed = new ArrayList<>(segments.size());
            for (String segment : segments) {
                if (segment.isEmpty()) {
                    throw new IllegalArgumentException("Canonical mask contains an empty segment");
                }
                List<Integer> equals = CanonicalText.unescapedPositions(segment, '=');
                List<Integer> colons = CanonicalText.unescapedPositions(segment, ':');
                if (equals.size() != 1) {
                    throw new IllegalArgumentException("Canonical mask segment requires one value separator");
                }
                int equalsAt = equals.get(0);
                if (colons.size() > 1 || (!colons.isEmpty() && colons.get(0) > equalsAt)) {
                    throw new IllegalArgumentException("Canonical mask version separator is misplaced");
                }
                int qualifierEnd = colons.isEmpty() ? equalsAt : colons.get(0);
                String qualifier = CanonicalText.decode(segment.substring(0, qualifierEnd));
                String version = colons.isEmpty() ? null
                        : CanonicalText.decode(segment.substring(colons.get(0) + 1, equalsAt));
                String value = CanonicalText.decode(segment.substring(equalsAt + 1));
                parsed.add(TopicMaskEntry.of(qualifier, value, version));
            }
            return ordinary(parsed, includeContainedElements);
        } catch (IllegalArgumentException exception) {
            logConstructionFailure();
            throw exception;
        }
    }

    /** @return true only for the explicit universal mask kind */
    public boolean isUniversal() {
        return universal;
    }

    /** @return immutable ordered mask entries; empty for the universal kind */
    public List<TopicMaskEntry> getEntries() {
        return entries;
    }

    /**
     * Returns the ordinary mask's trailing-element policy.
     *
     * @throws IllegalStateException when called on the universal kind
     */
    public boolean isIncludeContainedElements() {
        requireOrdinary();
        return includeContainedElements;
    }

    /**
     * Returns the ordinary canonical expression.
     *
     * @throws IllegalStateException when called on the universal kind
     */
    public String getCanonicalExpression() {
        requireOrdinary();
        StringBuilder expression = new StringBuilder();
        for (TopicMaskEntry entry : entries) {
            if (expression.length() > 0) {
                expression.append('.');
            }
            expression.append(CanonicalText.escape(entry.getQualifierRegex()));
            if (entry.getVersionRegex().isPresent()) {
                expression.append(':').append(CanonicalText.escape(entry.getVersionRegex().get()));
            }
            expression.append('=').append(CanonicalText.escape(entry.getValueRegex()));
        }
        return expression.toString();
    }

    /**
     * Evaluates this selector without conferring any operational authority.
     * Null candidates and any unexpected evaluation error fail closed.
     *
     * @param candidate candidate Topic
     * @return true only when the explicit kind and all positional criteria match
     */
    public boolean matches(Topic candidate) {
        if (candidate == null) {
            return false;
        }
        if (universal) {
            return true;
        }
        return TopicMaskEvaluator.defaultEvaluator().matches(entries, includeContainedElements, candidate);
    }

    /** Prevents universal masks from being read as ordinary expression data. */
    private void requireOrdinary() {
        if (universal) {
            throw new IllegalStateException("Universal TopicMask has no ordinary expression data");
        }
    }

    /**
     * Emits a payload-free diagnostic for rejected construction. Logging faults
     * are contained so they cannot replace the original validation failure.
     */
    private static void logConstructionFailure() {
        try {
            LOGGER.error("TopicMask construction or reconstruction rejected");
        } catch (RuntimeException ignored) {
            // A diagnostic failure cannot change the invalid construction result.
        }
    }

    /*
     * Logical JSON writers. Universal masks emit only {"universal":true}; ordinary
     * masks emit only entries and containment, so the two kinds never overlap.
     * Reading is performed exclusively by TopicMaskJsonDecoder.
     */
    @JsonProperty("universal")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean universalForJson() {
        return universal ? Boolean.TRUE : null;
    }

    @JsonProperty("entries")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<TopicMaskEntry> entriesForJson() {
        return universal ? null : entries;
    }

    @JsonProperty("includeContainedElements")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean containmentForJson() {
        return universal ? null : includeContainedElements;
    }

    /** Creates the validated Java serialization proxy rather than serializing fields directly. */
    private Object writeReplace() {
        return new SerializationProxy(universal, entries, includeContainedElements);
    }

    /** Rejects direct Java deserialization so all instances pass through the proxy invariants. */
    private void readObject(ObjectInputStream stream) throws InvalidObjectException {
        throw new InvalidObjectException("Serialization proxy required");
    }

    /**
     * Compares explicit kind, ordered original expressions, version presence,
     * and containment; all universal instances are equivalent.
     *
     * @param other comparison value
     * @return true when both masks have identical logical state
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TopicMask)) {
            return false;
        }
        TopicMask that = (TopicMask) other;
        return universal == that.universal
                && (universal || (includeContainedElements == that.includeContainedElements
                && entries.equals(that.entries)));
    }

    /** @return hash consistent with the mask kind and its equality-relevant state */
    @Override
    public int hashCode() {
        return universal ? 1 : Objects.hash(entries, includeContainedElements);
    }

    /** @return canonical ordinary expression, or descriptive universal text */
    @Override
    public String toString() {
        return universal ? "TopicMask{universal}" : getCanonicalExpression();
    }

    /**
     * Java serialization form. Reading resolves through {@link #all()} or
     * {@link #ordinary(List, boolean)}, so a stream cannot create a universal mask
     * carrying ordinary data or an ordinary mask without criteria.
     */
    private static final class SerializationProxy implements Serializable {
        private static final long serialVersionUID = 1L;
        private final boolean universal;
        private final List<TopicMaskEntry> entries;
        private final boolean includeContainedElements;

        private SerializationProxy(boolean universal, List<TopicMaskEntry> entries,
                                   boolean includeContainedElements) {
            this.universal = universal;
            this.entries = new ArrayList<>(entries);
            this.includeContainedElements = includeContainedElements;
        }

        private Object readResolve() throws InvalidObjectException {
            if (universal) {
                if (entries == null || !entries.isEmpty() || includeContainedElements) {
                    throw new InvalidObjectException("Universal TopicMask proxy contains ordinary data");
                }
                return TopicMask.all();
            }
            try {
                return TopicMask.ordinary(entries, includeContainedElements);
            } catch (IllegalArgumentException exception) {
                // The cause is deliberately not chained: it may describe expression content.
                throw new InvalidObjectException("Invalid ordinary TopicMask proxy");
            }
        }
    }
}
