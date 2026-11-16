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

import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

/**
 * Fluent builder for constructing immutable {@link Topic} instances with strict validation.
 * <p>
 * Enforces the following invariants:
 * <ul>
 *   <li>Reject duplicate sequence positions without overwriting previous entries or corrupting builder state.</li>
 *   <li>Ensure sequence numbers are non-negative, start at 0, and form a continuous zero-indexed sequence with no gaps.</li>
 *   <li>Ensure every entry has a non-blank qualifier and non-blank name containing no '.' delimiter.</li>
 * </ul>
 */
public class TopicBuilder {

    private UUID id;
    private final SortedMap<Integer, TopicEntry> entries = new TreeMap<>();

    /**
     * Constructs a new builder with a generated UUID.
     */
    public TopicBuilder() {
        this(UUID.randomUUID());
    }

    /**
     * Constructs a new builder with the specified UUID.
     *
     * @param id non-null unique identifier
     */
    public TopicBuilder(UUID id) {
        this.id = Objects.requireNonNull(id, "Topic id must not be null");
    }

    /**
     * Sets the unique identifier for the Topic.
     *
     * @param id non-null identifier
     * @return this builder
     */
    public TopicBuilder id(UUID id) {
        this.id = Objects.requireNonNull(id, "Topic id must not be null");
        return this;
    }

    /**
     * Adds an entry at the next sequential sequence number.
     *
     * @param qualifier non-blank qualifier
     * @param name      non-blank name without dots
     * @return this builder
     */
    public TopicBuilder addEntry(String qualifier, String name) {
        int nextSeq = entries.isEmpty() ? 0 : entries.lastKey() + 1;
        return addEntry(nextSeq, qualifier, name);
    }

    /**
     * Adds an entry at the next sequential sequence number.
     *
     * @param entry non-null topic entry
     * @return this builder
     */
    public TopicBuilder addEntry(TopicEntry entry) {
        int nextSeq = entries.isEmpty() ? 0 : entries.lastKey() + 1;
        return addEntry(nextSeq, entry);
    }

    /**
     * Adds an entry at an explicit sequence number.
     * <p>
     * Rejects duplicate sequence positions immediately without overwriting existing entries.
     *
     * @param sequenceNumber zero-based sequence position
     * @param qualifier      non-blank qualifier
     * @param name           non-blank name without dots
     * @return this builder
     * @throws IllegalArgumentException if sequenceNumber is negative or already occupied
     */
    public TopicBuilder addEntry(int sequenceNumber, String qualifier, String name) {
        if (sequenceNumber < 0) {
            throw new IllegalArgumentException("Sequence number must be non-negative: " + sequenceNumber);
        }
        if (entries.containsKey(sequenceNumber)) {
            throw new IllegalArgumentException("Duplicate Topic sequence position rejected: " + sequenceNumber);
        }
        TopicEntry entry = new TopicEntry(qualifier, name);
        entries.put(sequenceNumber, entry);
        return this;
    }

    /**
     * Adds an entry at an explicit sequence number.
     * <p>
     * Rejects duplicate sequence positions immediately without overwriting existing entries.
     *
     * @param sequenceNumber zero-based sequence position
     * @param entry          non-null topic entry
     * @return this builder
     * @throws IllegalArgumentException if sequenceNumber is negative or already occupied
     */
    public TopicBuilder addEntry(int sequenceNumber, TopicEntry entry) {
        if (sequenceNumber < 0) {
            throw new IllegalArgumentException("Sequence number must be non-negative: " + sequenceNumber);
        }
        Objects.requireNonNull(entry, "TopicEntry must not be null");
        if (entries.containsKey(sequenceNumber)) {
            throw new IllegalArgumentException("Duplicate Topic sequence position rejected: " + sequenceNumber);
        }
        entries.put(sequenceNumber, entry);
        return this;
    }

    /**
     * Builds and returns an immutable {@link Topic}.
     *
     * @return constructed Topic
     * @throws IllegalStateException if no entries are present or sequence has gaps
     */
    public Topic build() {
        if (id == null) {
            id = UUID.randomUUID();
        }
        if (entries.isEmpty()) {
            throw new IllegalStateException("Cannot build Topic with no canonical name entries");
        }
        // Validate continuous sequence starting at 0 with no gaps
        for (int i = 0; i < entries.size(); i++) {
            if (!entries.containsKey(i)) {
                throw new IllegalStateException("Topic canonical name sequence has gap at position: " + i);
            }
        }
        return new Topic(id, entries);
    }
}
