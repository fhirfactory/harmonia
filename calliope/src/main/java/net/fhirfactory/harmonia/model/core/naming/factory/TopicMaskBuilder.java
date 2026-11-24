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

import net.fhirfactory.harmonia.model.core.naming.TopicMask;
import net.fhirfactory.harmonia.model.core.naming.datatypes.TopicMaskEntry;
import net.fhirfactory.harmonia.model.core.naming.support.TopicLimits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Reusable fluent builder for ordinary immutable TopicMasks.
 * <p>
 * Each operation validates and compiles its entry before changing builder state.
 * A built mask is a defensive snapshot; subsequent additions affect only later
 * results. Universal masks are created explicitly with {@link TopicMask#all()}.
 * The ordinary-mask contract is defined by {@code DRP-Topic-and-TopicMask.Final.001.001.md} §§4, 8.
 */
public final class TopicMaskBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(TopicMaskBuilder.class);
    private final List<TopicMaskEntry> entries = new ArrayList<>();
    private boolean includeContainedElements;

    /** Creates an empty ordinary-mask builder. */
    public TopicMaskBuilder() {
    }

    /** Adds a validated immutable entry. */
    public TopicMaskBuilder add(TopicMaskEntry entry) {
        if (entry == null) {
            logInvalidAddition();
            throw new IllegalArgumentException("entry must not be null");
        }
        try {
            TopicLimits.requireDepth(entries.size() + 1);
        } catch (IllegalArgumentException exception) {
            logInvalidAddition();
            throw exception;
        }
        entries.add(entry);
        return this;
    }

    /** Adds an independent qualifier/value expression pair. */
    public TopicMaskBuilder addRegex(String qualifierRegex, String valueRegex) {
        return add(TopicMaskEntry.of(qualifierRegex, valueRegex));
    }

    /** Adds qualifier, value, and optional version expressions. */
    public TopicMaskBuilder addRegex(String qualifierRegex, String valueRegex, String versionRegex) {
        return add(TopicMaskEntry.of(qualifierRegex, valueRegex, versionRegex));
    }

    /** Adds a value expression with an unrestricted qualifier. */
    public TopicMaskBuilder addForValue(String valueRegex) {
        return add(TopicMaskEntry.forValue(valueRegex));
    }

    /** Adds a qualifier expression with an unrestricted value. */
    public TopicMaskBuilder addForQualifier(String qualifierRegex) {
        return add(TopicMaskEntry.forQualifier(qualifierRegex));
    }

    /** Adds a positional entry that matches any qualifier and value. */
    public TopicMaskBuilder addAny() {
        return add(TopicMaskEntry.any());
    }

    /** Adds literal qualifier and value criteria. */
    public TopicMaskBuilder addLiterals(String qualifier, String value) {
        return add(TopicMaskEntry.ofLiterals(qualifier, value));
    }

    /** Adds literal qualifier, value, and version criteria. */
    public TopicMaskBuilder addLiterals(String qualifier, String value, String version) {
        return add(TopicMaskEntry.ofLiterals(qualifier, value, version));
    }

    /** Adds a literal value with an unrestricted qualifier. */
    public TopicMaskBuilder addForLiteralValue(String value) {
        return add(TopicMaskEntry.forLiteralValue(value));
    }

    /** Adds a literal qualifier with an unrestricted value. */
    public TopicMaskBuilder addForLiteralQualifier(String qualifier) {
        return add(TopicMaskEntry.forLiteralQualifier(qualifier));
    }

    /**
     * Configures whether matching accepts trailing Topic entries.
     *
     * @param include true for trailing-element containment, false for exact depth
     * @return this builder
     */
    public TopicMaskBuilder includeContainedElements(boolean include) {
        includeContainedElements = include;
        return this;
    }

    /**
     * Builds a snapshot of the configured ordinary mask.
     *
     * @return new immutable ordinary mask
     * @throws IllegalStateException when no criteria have been added
     */
    public TopicMask build() {
        if (entries.isEmpty()) {
            logInvalidAddition();
            throw new IllegalStateException("Cannot build an empty ordinary TopicMask");
        }
        return TopicMask.ordinary(new ArrayList<>(entries), includeContainedElements);
    }

    private static void logInvalidAddition() {
        try {
            LOGGER.error("TopicMask builder rejected an invalid operation");
        } catch (RuntimeException ignored) {
            // A diagnostic failure cannot alter the rejected builder operation.
        }
    }
}
