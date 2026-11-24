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
import net.fhirfactory.harmonia.model.core.naming.datatypes.TopicEntry;
import net.fhirfactory.harmonia.model.core.naming.support.TopicFieldRules;
import net.fhirfactory.harmonia.model.core.naming.support.TopicLimits;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Reusable builder for immutable Topics using either sequential or indexed input.
 * <p>
 * The first successful addition fixes the mode. Indexed additions replace an
 * occupied position, while builds require a complete zero-based path. Failed
 * operations do not alter mode, stored inputs, or previously built snapshots.
 * These rules implement {@code DRP-Topic-and-TopicMask.Final.001.001.md} §2.
 */
public final class TopicBuilder {

    private enum Mode { SEQUENTIAL, INDEXED }

    private Mode mode;
    private final List<Input> sequentialEntries = new ArrayList<>();
    private final Map<Integer, Input> indexedEntries = new TreeMap<>();

    /** Creates an empty builder. */
    public TopicBuilder() {
    }

    /**
     * Adds one entry at the end of sequential input.
     *
     * @param qualifier supplied qualifier, or null/empty for generated `N<position>`
     * @param value required value
     * @return this builder
     */
    public TopicBuilder addEntry(String qualifier, String value) {
        return addEntry(qualifier, value, null);
    }

    /**
     * Adds one versioned entry at the end of sequential input.
     *
     * @param qualifier supplied qualifier, or null/empty for generated `N<position>`
     * @param value required value
     * @param version optional version; null/empty means absent
     * @return this builder
     */
    public TopicBuilder addEntry(String qualifier, String value, String version) {
        requireMode(Mode.SEQUENTIAL);
        int index = sequentialEntries.size();
        TopicLimits.requireDepth(index + 1);
        Input input = validateInput(qualifier, value, version);
        if (mode == null) {
            mode = Mode.SEQUENTIAL;
        }
        sequentialEntries.add(input);
        return this;
    }

    /**
     * Adds or replaces an indexed entry. Out-of-order positions are allowed.
     *
     * @param index zero-based path position
     * @param qualifier supplied qualifier, or null/empty for generated `N<position>`
     * @param value required value
     * @return this builder
     */
    public TopicBuilder addEntry(int index, String qualifier, String value) {
        return addEntry(index, qualifier, value, null);
    }

    /**
     * Adds or replaces an indexed versioned entry. A failed replacement leaves
     * the prior input at that position intact.
     *
     * @param index zero-based path position
     * @param qualifier supplied qualifier, or null/empty for generated `N<position>`
     * @param value required value
     * @param version optional version; null/empty means absent
     * @return this builder
     */
    public TopicBuilder addEntry(int index, String qualifier, String value, String version) {
        requireMode(Mode.INDEXED);
        if (index < 0 || index >= TopicLimits.MAX_DEPTH) {
            throw new IllegalArgumentException("Topic index is outside the supported range");
        }
        Input input = validateInput(qualifier, value, version);
        if (mode == null) {
            mode = Mode.INDEXED;
        }
        indexedEntries.put(index, input);
        return this;
    }

    /**
     * Builds a new immutable Topic snapshot without consuming builder state.
     *
     * @return independent immutable Topic
     * @throws IllegalStateException if no entries exist or indexed positions have a gap
     */
    public Topic build() {
        List<Input> inputs;
        if (mode == Mode.SEQUENTIAL) {
            inputs = new ArrayList<>(sequentialEntries);
        } else if (mode == Mode.INDEXED) {
            if (indexedEntries.isEmpty()) {
                throw new IllegalStateException("Cannot build an empty Topic");
            }
            inputs = new ArrayList<>(indexedEntries.size());
            for (int index = 0; index < indexedEntries.size(); index++) {
                Input input = indexedEntries.get(index);
                if (input == null) {
                    throw new IllegalStateException("Topic indexed input has a gap");
                }
                inputs.add(input);
            }
        } else {
            throw new IllegalStateException("Cannot build a Topic before adding an entry");
        }
        TopicLimits.requireDepth(inputs.size());
        List<TopicEntry> entries = new ArrayList<>(inputs.size());
        for (int index = 0; index < inputs.size(); index++) {
            Input input = inputs.get(index);
            String qualifier = input.qualifier == null || input.qualifier.isEmpty()
                    ? "N" + index : input.qualifier;
            entries.add(new TopicEntry(qualifier, input.value, input.version));
        }
        return new Topic(entries);
    }

    private void requireMode(Mode requested) {
        if (mode != null && mode != requested) {
            throw new IllegalStateException("TopicBuilder input mode cannot be changed");
        }
    }

    private static Input validateInput(String qualifier, String value, String version) {
        TopicFieldRules.requireQualifierInput(qualifier);
        TopicFieldRules.requireValue(value);
        return new Input(qualifier, value, TopicFieldRules.normalizedVersion(version));
    }

    private static final class Input {
        private final String qualifier;
        private final String value;
        private final String version;

        private Input(String qualifier, String value, String version) {
            this.qualifier = qualifier;
            this.value = value;
            this.version = version;
        }
    }
}
