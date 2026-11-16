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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.fhirfactory.harmonia.model.core.naming.datatypes.TopicEntry;
import net.fhirfactory.harmonia.model.core.naming.datatypes.TopicMaskEntryPattern;
import net.fhirfactory.harmonia.model.core.naming.factory.TopicMaskBuilder;
import net.fhirfactory.harmonia.model.core.naming.factory.TopicMaskFactory;
import net.fhirfactory.harmonia.model.core.naming.valuesets.MaskType;
import net.fhirfactory.harmonia.model.core.naming.valuesets.TopicMaskType;

import java.io.Serializable;
import java.util.*;

/**
 * An immutable selector and filter construct within Harmonia's Architecture Continuum.
 * <p>
 * A {@code TopicMask} evaluates candidate {@link Topic} instances according to either:
 * <ul>
 *   <li><b>TopicNameBased:</b> Explicitly sequenced regex patterns against unqualified Topic entry names.</li>
 *   <li><b>TopicCanonicalNameBased:</b> Explicitly sequenced entry patterns matching entry qualifier and
 *       name independently.</li>
 * </ul>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TopicMask implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final TopicMaskType type;
    private final MaskType maskType;
    private final SortedMap<Integer, String> namePatterns;
    private final SortedMap<Integer, TopicMaskEntryPattern> canonicalPatterns;

    /**
     * Constructs an immutable TopicMask.
     *
     * @param id                non-null identifier
     * @param maskType          containment depth semantics; defaults to {@link MaskType#Inclusive}
     * @param namePatterns      name-based patterns, or {@code null} if canonical-based
     * @param canonicalPatterns canonical entry patterns, or {@code null} if name-based
     */
    @JsonCreator
    public TopicMask(
            @JsonProperty("id") UUID id,
            @JsonProperty("maskType") MaskType maskType,
            @JsonProperty("namePatterns") SortedMap<Integer, String> namePatterns,
            @JsonProperty("canonicalPatterns") SortedMap<Integer, TopicMaskEntryPattern> canonicalPatterns) {
        this.id = id != null ? id : UUID.randomUUID();
        this.maskType = maskType != null ? maskType : MaskType.Inclusive;

        if (canonicalPatterns != null && !canonicalPatterns.isEmpty()) {
            this.type = TopicMaskType.TopicCanonicalNameBased;
            this.canonicalPatterns = Collections.unmodifiableSortedMap(new TreeMap<>(canonicalPatterns));
            this.namePatterns = Collections.emptySortedMap();
        } else if (namePatterns != null && !namePatterns.isEmpty()) {
            this.type = TopicMaskType.TopicNameBased;
            this.namePatterns = Collections.unmodifiableSortedMap(new TreeMap<>(namePatterns));
            this.canonicalPatterns = Collections.emptySortedMap();
        } else {
            this.type = TopicMaskType.TopicNameBased;
            this.namePatterns = Collections.emptySortedMap();
            this.canonicalPatterns = Collections.emptySortedMap();
        }
    }

    public UUID getId() {
        return id;
    }

    public TopicMaskType getType() {
        return type;
    }

    public MaskType getMaskType() {
        return maskType;
    }

    public SortedMap<Integer, String> getNamePatterns() {
        return namePatterns;
    }

    public SortedMap<Integer, TopicMaskEntryPattern> getCanonicalPatterns() {
        return canonicalPatterns;
    }

    public int getDepth() {
        return type == TopicMaskType.TopicCanonicalNameBased ? canonicalPatterns.size() : namePatterns.size();
    }

    public boolean isCanonical() {
        return type == TopicMaskType.TopicCanonicalNameBased;
    }

    public boolean isNameBased() {
        return type == TopicMaskType.TopicNameBased;
    }

    public boolean isExact() {
        return maskType == MaskType.Exact;
    }

    public boolean isInclusive() {
        return maskType == MaskType.Inclusive;
    }

    /**
     * Evaluates whether a candidate {@link Topic} satisfies this mask.
     *
     * @param topic candidate Topic
     * @return {@code true} if candidate matches, otherwise {@code false}
     */
    public boolean matches(Topic topic) {
        if (topic == null) {
            return false;
        }

        if (type == TopicMaskType.TopicNameBased) {
            if (namePatterns.isEmpty()) {
                return maskType != MaskType.Exact || topic.getDepth() == 0;
            }
            if (maskType == MaskType.Exact && topic.getDepth() != namePatterns.size()) {
                return false;
            }
            if (topic.getDepth() < namePatterns.size()) {
                return false;
            }
            for (Map.Entry<Integer, String> entry : namePatterns.entrySet()) {
                TopicEntry topicEntry = topic.getEntry(entry.getKey());
                if (topicEntry == null) {
                    return false;
                }
                String regex = entry.getValue();
                if (!".*".equals(regex) && !topicEntry.getName().matches(regex)) {
                    return false;
                }
            }
            return true;
        } else {
            if (canonicalPatterns.isEmpty()) {
                return maskType != MaskType.Exact || topic.getDepth() == 0;
            }
            if (maskType == MaskType.Exact && topic.getDepth() != canonicalPatterns.size()) {
                return false;
            }
            if (topic.getDepth() < canonicalPatterns.size()) {
                return false;
            }
            for (Map.Entry<Integer, TopicMaskEntryPattern> entry : canonicalPatterns.entrySet()) {
                TopicEntry topicEntry = topic.getEntry(entry.getKey());
                if (topicEntry == null || !entry.getValue().matches(topicEntry)) {
                    return false;
                }
            }
            return true;
        }
    }

    public String toCanonicalString() {
        StringBuilder sb = new StringBuilder();
        if (type == TopicMaskType.TopicCanonicalNameBased) {
            for (Map.Entry<Integer, TopicMaskEntryPattern> entry : canonicalPatterns.entrySet()) {
                if (sb.length() > 0) sb.append(".");
                sb.append(entry.getValue().getQualifierRegex()).append("=").append(entry.getValue().getNameRegex());
            }
        } else {
            for (Map.Entry<Integer, String> entry : namePatterns.entrySet()) {
                if (sb.length() > 0) sb.append(".");
                sb.append(entry.getValue());
            }
        }
        return sb.toString();
    }

    public String getMaskPattern() {
        return toCanonicalString();
    }

    public String getShortId() {
        return id.toString().substring(0, 8);
    }

    public boolean matches(String topicName) {
        if (topicName == null) {
            return false;
        }
        return matches(Topic.fromName(topicName));
    }

    // --- Static Factory and Builder Entry Points ---

    public static TopicMaskBuilder builder() {
        return new TopicMaskBuilder();
    }

    public static TopicMaskBuilder builder(UUID id) {
        return new TopicMaskBuilder(id);
    }

    public static TopicMask unrestricted() {
        return TopicMaskFactory.unrestricted();
    }

    public static TopicMask nameBased(Topic topic) {
        if (topic == null) {
            return TopicMaskFactory.unrestricted();
        }
        return TopicMaskFactory.fromNamePattern(topic.getName());
    }

    public static TopicMask fromNamePattern(String dotSeparatedPattern) {
        return TopicMaskFactory.fromNamePattern(dotSeparatedPattern);
    }

    public static TopicMask fromPattern(String pattern) {
        return TopicMaskFactory.fromNamePattern(pattern);
    }

    public static TopicMask fromNamePattern(String dotSeparatedPattern, MaskType maskType) {
        return TopicMaskFactory.fromNamePattern(dotSeparatedPattern, maskType);
    }

    // --- Nested Aliases for Backward Compatibility ---

    public static class EntryPattern extends TopicMaskEntryPattern {
        public EntryPattern(String qualifierRegex, String nameRegex) {
            super(qualifierRegex, nameRegex);
        }
    }

    public static class Builder extends TopicMaskBuilder {
        public Builder() {
            super();
        }

        public Builder(UUID id) {
            super(id);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof TopicMask)) return false;
        TopicMask topicMask = (TopicMask) o;
        return Objects.equals(id, topicMask.id) &&
                type == topicMask.type &&
                maskType == topicMask.maskType &&
                Objects.equals(namePatterns, topicMask.namePatterns) &&
                Objects.equals(canonicalPatterns, topicMask.canonicalPatterns);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, maskType, namePatterns, canonicalPatterns);
    }

    @Override
    public String toString() {
        return "TopicMask{" +
                "id=" + id +
                ", type=" + type +
                ", maskType=" + maskType +
                ", pattern=" + toCanonicalString() +
                '}';
    }
}
