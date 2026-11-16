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
import net.fhirfactory.harmonia.model.core.naming.datatypes.TopicMaskEntryPattern;
import net.fhirfactory.harmonia.model.core.naming.valuesets.MaskType;
import net.fhirfactory.harmonia.model.core.naming.valuesets.TopicMaskType;

import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

/**
 * Fluent builder for creating immutable {@link TopicMask} instances with strict validation.
 */
public class TopicMaskBuilder {

    private UUID id = UUID.randomUUID();
    private MaskType maskType = MaskType.Inclusive;
    private TopicMaskType maskBasis = null;
    private final SortedMap<Integer, String> namePatterns = new TreeMap<>();
    private final SortedMap<Integer, TopicMaskEntryPattern> canonicalPatterns = new TreeMap<>();

    public TopicMaskBuilder() {
    }

    public TopicMaskBuilder(UUID id) {
        this.id = Objects.requireNonNull(id, "id must not be null");
    }

    public TopicMaskBuilder id(UUID id) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        return this;
    }

    public TopicMaskBuilder maskType(MaskType maskType) {
        this.maskType = Objects.requireNonNull(maskType, "maskType must not be null");
        return this;
    }

    public TopicMaskBuilder exact() {
        this.maskType = MaskType.Exact;
        return this;
    }

    public TopicMaskBuilder inclusive() {
        this.maskType = MaskType.Inclusive;
        return this;
    }

    public TopicMaskBuilder addNamePattern(String regex) {
        int nextSeq = namePatterns.isEmpty() ? 0 : namePatterns.lastKey() + 1;
        return addNamePattern(nextSeq, regex);
    }

    public TopicMaskBuilder addNamePattern(int sequenceNumber, String regex) {
        if (sequenceNumber < 0) {
            throw new IllegalArgumentException("sequenceNumber must be non-negative: " + sequenceNumber);
        }
        if (maskBasis == TopicMaskType.TopicCanonicalNameBased) {
            throw new IllegalStateException("Cannot add name pattern to canonical-name-based mask");
        }
        if (namePatterns.containsKey(sequenceNumber)) {
            throw new IllegalArgumentException("Duplicate sequence position rejected: " + sequenceNumber);
        }
        this.maskBasis = TopicMaskType.TopicNameBased;
        this.namePatterns.put(sequenceNumber, regex);
        return this;
    }

    public TopicMaskBuilder addCanonicalPattern(String qualifierRegex, String nameRegex) {
        int nextSeq = canonicalPatterns.isEmpty() ? 0 : canonicalPatterns.lastKey() + 1;
        return addCanonicalPattern(nextSeq, qualifierRegex, nameRegex);
    }

    public TopicMaskBuilder addCanonicalPattern(int sequenceNumber, String qualifierRegex, String nameRegex) {
        return addCanonicalPattern(sequenceNumber, new TopicMaskEntryPattern(qualifierRegex, nameRegex));
    }

    public TopicMaskBuilder addCanonicalPattern(int sequenceNumber, TopicMaskEntryPattern pattern) {
        if (sequenceNumber < 0) {
            throw new IllegalArgumentException("sequenceNumber must be non-negative: " + sequenceNumber);
        }
        if (maskBasis == TopicMaskType.TopicNameBased) {
            throw new IllegalStateException("Cannot add canonical pattern to name-based mask");
        }
        Objects.requireNonNull(pattern, "pattern must not be null");
        if (canonicalPatterns.containsKey(sequenceNumber)) {
            throw new IllegalArgumentException("Duplicate sequence position rejected: " + sequenceNumber);
        }
        this.maskBasis = TopicMaskType.TopicCanonicalNameBased;
        this.canonicalPatterns.put(sequenceNumber, pattern);
        return this;
    }

    public TopicMask build() {
        if (maskBasis == null) {
            this.maskBasis = TopicMaskType.TopicNameBased;
        }
        if (maskBasis == TopicMaskType.TopicNameBased) {
            for (int i = 0; i < namePatterns.size(); i++) {
                if (!namePatterns.containsKey(i)) {
                    throw new IllegalStateException("Gap in sequence at: " + i);
                }
            }
            return new TopicMask(id, maskType, namePatterns, null);
        } else {
            for (int i = 0; i < canonicalPatterns.size(); i++) {
                if (!canonicalPatterns.containsKey(i)) {
                    throw new IllegalStateException("Gap in sequence at: " + i);
                }
            }
            return new TopicMask(id, maskType, null, canonicalPatterns);
        }
    }
}
