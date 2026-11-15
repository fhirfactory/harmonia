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

package net.fhirfactory.harmonia.model.topic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Encapsulates the immutable, general-purpose TopicMask construct with full UUID identity.
 * <p>
 * <b>Architectural Context &amp; Authority:</b>
 * In accordance with the accepted 1.1.25 Architecture Continuum ({@code model.core.information.TopicMask}):
 * <ul>
 *   <li><b>Immutable Selector Identity:</b> A TopicMask has its own stable {@link UUID} and an immutable definition.
 *       Its UUID does not influence matching.</li>
 *   <li><b>Single Matching Basis ({@link TopicMaskType}):</b>
 *     <ul>
 *       <li><b>TopicNameBased:</b> SequenceNumber &rarr; NameRegex applied against corresponding individual unqualified Topic names.</li>
 *       <li><b>TopicCanonicalNameBased:</b> SequenceNumber &rarr; EntryPattern(qualifierRegex, nameRegex) applied against qualifier and name independently.</li>
 *     </ul>
 *     One mask does not combine both bases.
 *   </li>
 *   <li><b>Element-Wise Regex Matching:</b> Every regex applies to one dot-separated element and cannot span adjacent path elements.</li>
 *   <li><b>Containment Depth Semantics ({@link MaskType}):</b>
 *     <ul>
 *       <li><b>Exact:</b> Every specified level matches and candidate Topic depth is exactly the mask depth.</li>
 *       <li><b>Inclusive:</b> Every specified level matches; further descendant levels in the candidate Topic are included.</li>
 *     </ul>
 *   </li>
 *   <li><b>Unrestricted Default Matching:</b> Default/unrestricted masks are inclusive with unrestricted regex ({@code ".*"}).</li>
 *   <li><b>Neutral Matching:</b> Matching is neutral and confers no disclosure, publication, execution, persistence, or routing authority.</li>
 * </ul>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class TopicMask implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final UUID UNRESTRICTED_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    private static final TopicMask UNRESTRICTED = TopicMask.builder()
            .id(UNRESTRICTED_UUID)
            .topicNameBased()
            .inclusive()
            .addNamePattern(0, ".*")
            .build();

    private final UUID id;
    private final TopicMaskType type;
    private final MaskType maskType;
    private final SortedMap<Integer, String> namePatterns;
    private final SortedMap<Integer, EntryPattern> canonicalPatterns;

    private final transient List<Pattern> compiledNamePatterns;
    private final transient List<EntryPattern> compiledCanonicalPatterns;

    /**
     * Canonical constructor establishing validated, immutable TopicMask state.
     *
     * @param id                the mask UUID identity, must not be null
     * @param type              the matching basis, must not be null
     * @param maskType          the containment depth semantics, must not be null
     * @param namePatterns      name regex patterns when {@code type == TopicNameBased}
     * @param canonicalPatterns canonical entry regex patterns when {@code type == TopicCanonicalNameBased}
     * @throws IllegalArgumentException if required parameters are missing, invalid, or contain sequence gaps
     */
    @JsonCreator
    public TopicMask(
            @JsonProperty("id") UUID id,
            @JsonProperty("type") TopicMaskType type,
            @JsonProperty("maskType") MaskType maskType,
            @JsonProperty("namePatterns") Map<Integer, String> namePatterns,
            @JsonProperty("canonicalPatterns") Map<Integer, EntryPattern> canonicalPatterns) {
        if (id == null) {
            throw new IllegalArgumentException("TopicMask id (UUID) must not be null");
        }
        this.id = id;
        this.type = type != null ? type : TopicMaskType.TopicNameBased;
        this.maskType = maskType != null ? maskType : MaskType.Inclusive;

        if (this.type == TopicMaskType.TopicNameBased) {
            if (namePatterns == null || namePatterns.isEmpty()) {
                throw new IllegalArgumentException("namePatterns must not be null or empty for TopicNameBased mask");
            }
            SortedMap<Integer, String> sorted = new TreeMap<>(namePatterns);
            validateSequence(sorted.keySet());

            List<Pattern> compiledList = new ArrayList<>(sorted.size());
            SortedMap<Integer, String> validatedMap = new TreeMap<>();
            for (Map.Entry<Integer, String> entry : sorted.entrySet()) {
                String patternStr = entry.getValue();
                if (patternStr == null || patternStr.isBlank()) {
                    throw new IllegalArgumentException("Pattern at sequence " + entry.getKey() + " must not be null or blank");
                }
                patternStr = patternStr.trim();
                validatedMap.put(entry.getKey(), patternStr);
                compiledList.add(compileElementPattern(patternStr));
            }
            this.namePatterns = Collections.unmodifiableSortedMap(validatedMap);
            this.canonicalPatterns = Collections.emptySortedMap();
            this.compiledNamePatterns = Collections.unmodifiableList(compiledList);
            this.compiledCanonicalPatterns = Collections.emptyList();
        } else {
            if (canonicalPatterns == null || canonicalPatterns.isEmpty()) {
                throw new IllegalArgumentException("canonicalPatterns must not be null or empty for TopicCanonicalNameBased mask");
            }
            SortedMap<Integer, EntryPattern> sorted = new TreeMap<>(canonicalPatterns);
            validateSequence(sorted.keySet());

            List<EntryPattern> compiledList = new ArrayList<>(sorted.size());
            SortedMap<Integer, EntryPattern> validatedMap = new TreeMap<>();
            for (Map.Entry<Integer, EntryPattern> entry : sorted.entrySet()) {
                EntryPattern ep = entry.getValue();
                if (ep == null) {
                    throw new IllegalArgumentException("EntryPattern at sequence " + entry.getKey() + " must not be null");
                }
                validatedMap.put(entry.getKey(), ep);
                compiledList.add(ep);
            }
            this.namePatterns = Collections.emptySortedMap();
            this.canonicalPatterns = Collections.unmodifiableSortedMap(validatedMap);
            this.compiledNamePatterns = Collections.emptyList();
            this.compiledCanonicalPatterns = Collections.unmodifiableList(compiledList);
        }
    }

    /**
     * Legacy constructor compiling a dot-delimited pattern string into a {@code TopicNameBased} {@code TopicMask}.
     *
     * @param maskPattern dot-delimited element-wise regex mask pattern
     */
    public TopicMask(String maskPattern) {
        this(fromPattern(maskPattern));
    }

    private TopicMask(TopicMask other) {
        this(other.id, other.type, other.maskType, other.namePatterns, other.canonicalPatterns);
    }

    private static void validateSequence(Iterable<Integer> keys) {
        int expected = 0;
        for (int key : keys) {
            if (key != expected) {
                throw new IllegalArgumentException("TopicMask sequence has gaps or invalid order: expected " + expected + " but found " + key);
            }
            expected++;
        }
    }

    private static Pattern compileElementPattern(String rawRegex) {
        if (rawRegex == null || rawRegex.isBlank()) {
            return Pattern.compile("^.*$");
        }
        String r = rawRegex.trim();
        if (r.equals("*")) {
            r = ".*";
        }
        try {
            return Pattern.compile("^" + r + "$");
        } catch (PatternSyntaxException e) {
            throw new IllegalArgumentException("Invalid regular expression syntax: '" + rawRegex + "': " + e.getMessage(), e);
        }
    }

    // =========================================================================
    // Factory Helpers
    // =========================================================================

    /**
     * Returns the singleton unrestricted {@code TopicMask} matching all Topics.
     *
     * @return unrestricted TopicMask
     */
    public static TopicMask unrestricted() {
        return UNRESTRICTED;
    }

    /**
     * Creates a {@code TopicMask} from a dot-delimited pattern string.
     *
     * @param maskPattern dot-delimited element patterns (e.g. {@code "Health.HL7.*"} or {@code "Australia.ACT.Canberra"})
     * @return compiled TopicMask
     */
    public static TopicMask of(String maskPattern) {
        return fromPattern(maskPattern);
    }

    /**
     * Creates a {@code TopicMask} matching an exact Topic name.
     *
     * @param topic Topic to match
     * @return compiled TopicMask
     */
    public static TopicMask nameBased(Topic topic) {
        if (topic == null) {
            throw new IllegalArgumentException("topic must not be null");
        }
        return fromPattern(topic.getName());
    }

    /**
     * Creates a {@code TopicMask} matching a Topic name pattern.
     *
     * @param pattern pattern string
     * @return compiled TopicMask
     */
    public static TopicMask nameBased(String pattern) {
        return fromPattern(pattern);
    }

    /**
     * Creates a {@code TopicMask} from a dot-delimited pattern string.
     *
     * @param maskPattern dot-delimited element patterns (e.g. {@code "Health.HL7.*"} or {@code "Australia.ACT.Canberra"})
     * @return compiled TopicMask
     */
    public static TopicMask fromPattern(String maskPattern) {
        if (maskPattern == null || maskPattern.isBlank()) {
            throw new IllegalArgumentException("maskPattern must not be null or blank");
        }
        String pattern = maskPattern.trim();
        UUID id = UUID.nameUUIDFromBytes(pattern.getBytes(StandardCharsets.UTF_8));

        String[] parts = pattern.split("\\.", -1);
        SortedMap<Integer, String> patterns = new TreeMap<>();
        MaskType maskType = MaskType.Exact;

        // If ends with .* or *, it is inclusive
        if (parts.length > 0 && (parts[parts.length - 1].equals("*") || parts[parts.length - 1].equals(".*"))) {
            maskType = MaskType.Inclusive;
            // Trim trailing wildcard if length > 1, or keep 1 level
            int len = parts.length > 1 ? parts.length - 1 : parts.length;
            for (int i = 0; i < len; i++) {
                patterns.put(i, parts[i].trim());
            }
        } else {
            for (int i = 0; i < parts.length; i++) {
                patterns.put(i, parts[i].trim());
            }
        }

        return new TopicMask(id, TopicMaskType.TopicNameBased, maskType, patterns, null);
    }

    /**
     * Creates a {@code TopicMask} matching all Information Units ({@code "harmonia.mneme.iu.*"}).
     *
     * @return TopicMask matching all Information Units
     */
    public static TopicMask forInformationUnits() {
        return TopicMask.builder()
                .id(UUID.nameUUIDFromBytes("harmonia.mneme.iu.*".getBytes(StandardCharsets.UTF_8)))
                .topicNameBased()
                .inclusive()
                .addNamePattern(0, "harmonia")
                .addNamePattern(1, "mneme")
                .addNamePattern(2, "iu")
                .addNamePattern(3, ".*")
                .build();
    }

    /**
     * Creates a {@code TopicMask} matching a specific Information Unit UUID ({@code "harmonia.mneme.iu.<hex>"}).
     *
     * @param iuUuid the IU UUID to match
     * @return exact TopicMask for the IU
     */
    public static TopicMask forInformationUnit(UUID iuUuid) {
        if (iuUuid == null) {
            throw new IllegalArgumentException("iuUuid must not be null");
        }
        String hex = iuUuid.toString().replace("-", "").toLowerCase();
        return TopicMask.builder()
                .id(UUID.nameUUIDFromBytes(("harmonia.mneme.iu." + hex).getBytes(StandardCharsets.UTF_8)))
                .topicNameBased()
                .exact()
                .addNamePattern(0, "harmonia")
                .addNamePattern(1, "mneme")
                .addNamePattern(2, "iu")
                .addNamePattern(3, hex)
                .build();
    }

    /**
     * Creates a fluent {@link Builder} for constructing a {@code TopicMask}.
     *
     * @return a new Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    // =========================================================================
    // Matching Logic
    // =========================================================================

    /**
     * Evaluates whether a candidate {@link Topic} satisfies this {@code TopicMask}.
     *
     * @param topic candidate Topic, may be null
     * @return {@code true} if candidate matches all element criteria and depth semantics, {@code false} otherwise
     */
    public boolean matches(Topic topic) {
        if (topic == null) {
            return false;
        }

        int maskDepth = getDepth();
        if (maskDepth == 0) {
            return true;
        }

        int topicDepth = topic.getDepth();
        if (maskType == MaskType.Exact) {
            if (topicDepth != maskDepth) {
                return false;
            }
        } else { // Inclusive
            if (topicDepth < maskDepth) {
                return false;
            }
        }

        if (type == TopicMaskType.TopicNameBased) {
            for (int i = 0; i < maskDepth; i++) {
                Topic.Entry entry = topic.getEntry(i);
                if (entry == null) {
                    return false;
                }
                Pattern p = getCompiledNamePattern(i);
                if (!p.matcher(entry.getName()).matches()) {
                    return false;
                }
            }
            return true;
        } else { // TopicCanonicalNameBased
            for (int i = 0; i < maskDepth; i++) {
                Topic.Entry entry = topic.getEntry(i);
                if (entry == null) {
                    return false;
                }
                EntryPattern ep = getCompiledCanonicalPattern(i);
                if (ep.getCompiledQualifierPattern() != null) {
                    String q = entry.getQualifier() != null ? entry.getQualifier() : ("N" + i);
                    if (!ep.getCompiledQualifierPattern().matcher(q).matches()) {
                        return false;
                    }
                }
                if (!ep.getCompiledNamePattern().matcher(entry.getName()).matches()) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Evaluates whether a dot-delimited topic name string satisfies this {@code TopicMask}.
     *
     * @param dotDelimitedTopicName candidate topic name string
     * @return {@code true} if candidate matches, {@code false} otherwise
     */
    public boolean matches(String dotDelimitedTopicName) {
        if (dotDelimitedTopicName == null || dotDelimitedTopicName.isBlank()) {
            return false;
        }
        try {
            Topic topic = Topic.fromName(dotDelimitedTopicName);
            return matches(topic);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    // =========================================================================
    // Core Accessors
    // =========================================================================

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

    public SortedMap<Integer, EntryPattern> getCanonicalPatterns() {
        return canonicalPatterns;
    }

    public int getDepth() {
        return type == TopicMaskType.TopicNameBased ? namePatterns.size() : canonicalPatterns.size();
    }

    @JsonIgnore
    public String getMaskPattern() {
        if (type == TopicMaskType.TopicNameBased) {
            return String.join(".", namePatterns.values());
        }
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (EntryPattern ep : canonicalPatterns.values()) {
            if (!first) sb.append(".");
            sb.append(ep.toString());
            first = false;
        }
        return sb.toString();
    }

    @JsonIgnore
    public List<String> getElementPatterns() {
        if (type == TopicMaskType.TopicNameBased) {
            return new ArrayList<>(namePatterns.values());
        }
        List<String> list = new ArrayList<>();
        for (EntryPattern ep : canonicalPatterns.values()) {
            list.add(ep.toString());
        }
        return list;
    }

    private Pattern getCompiledNamePattern(int seq) {
        if (compiledNamePatterns != null && seq < compiledNamePatterns.size()) {
            return compiledNamePatterns.get(seq);
        }
        String p = namePatterns.get(seq);
        return compileElementPattern(p);
    }

    private EntryPattern getCompiledCanonicalPattern(int seq) {
        if (compiledCanonicalPatterns != null && seq < compiledCanonicalPatterns.size()) {
            return compiledCanonicalPatterns.get(seq);
        }
        return canonicalPatterns.get(seq);
    }

    // =========================================================================
    // Formatting & Diagnostics
    // =========================================================================

    public String getShortId() {
        return id.toString().substring(0, 8);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
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
                ", pattern='" + getMaskPattern() + '\'' +
                '}';
    }

    // =========================================================================
    // Nested Classes: EntryPattern & Builder
    // =========================================================================

    /**
     * Pattern specification for an explicitly sequenced entry in a {@code TopicCanonicalNameBased} {@code TopicMask}.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class EntryPattern implements Serializable {

        private static final long serialVersionUID = 1L;

        private final String qualifierRegex;
        private final String nameRegex;
        private final transient Pattern compiledQualifierPattern;
        private final transient Pattern compiledNamePattern;

        @JsonCreator
        public EntryPattern(
                @JsonProperty("qualifierRegex") String qualifierRegex,
                @JsonProperty("nameRegex") String nameRegex) {
            if (nameRegex == null || nameRegex.isBlank()) {
                throw new IllegalArgumentException("nameRegex must not be null or blank");
            }
            this.nameRegex = nameRegex.trim();
            this.qualifierRegex = qualifierRegex != null && !qualifierRegex.isBlank() ? qualifierRegex.trim() : null;

            String effQualifier = this.qualifierRegex != null ? this.qualifierRegex : ".*";
            this.compiledQualifierPattern = compileElementPattern(effQualifier);
            this.compiledNamePattern = compileElementPattern(this.nameRegex);
        }

        public static EntryPattern of(String qualifierRegex, String nameRegex) {
            return new EntryPattern(qualifierRegex, nameRegex);
        }

        public static EntryPattern of(String nameRegex) {
            return new EntryPattern(null, nameRegex);
        }

        public String getQualifierRegex() {
            return qualifierRegex;
        }

        public String getNameRegex() {
            return nameRegex;
        }

        @JsonIgnore
        public Pattern getCompiledQualifierPattern() {
            if (compiledQualifierPattern != null) {
                return compiledQualifierPattern;
            }
            String effQualifier = this.qualifierRegex != null ? this.qualifierRegex : ".*";
            return compileElementPattern(effQualifier);
        }

        @JsonIgnore
        public Pattern getCompiledNamePattern() {
            if (compiledNamePattern != null) {
                return compiledNamePattern;
            }
            return compileElementPattern(this.nameRegex);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            EntryPattern that = (EntryPattern) o;
            return Objects.equals(qualifierRegex, that.qualifierRegex) && Objects.equals(nameRegex, that.nameRegex);
        }

        @Override
        public int hashCode() {
            return Objects.hash(qualifierRegex, nameRegex);
        }

        @Override
        public String toString() {
            return (qualifierRegex != null ? qualifierRegex + "=" : "") + nameRegex;
        }
    }

    /**
     * Fluent builder for constructing immutable {@link TopicMask} instances.
     */
    public static final class Builder {

        private UUID id;
        private TopicMaskType type = TopicMaskType.TopicNameBased;
        private MaskType maskType = MaskType.Inclusive;
        private final SortedMap<Integer, String> namePatterns = new TreeMap<>();
        private final SortedMap<Integer, EntryPattern> canonicalPatterns = new TreeMap<>();
        private int nextSeq = 0;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder type(TopicMaskType type) {
            this.type = type != null ? type : TopicMaskType.TopicNameBased;
            return this;
        }

        public Builder topicNameBased() {
            this.type = TopicMaskType.TopicNameBased;
            return this;
        }

        public Builder topicCanonicalNameBased() {
            this.type = TopicMaskType.TopicCanonicalNameBased;
            return this;
        }

        public Builder maskType(MaskType maskType) {
            this.maskType = maskType != null ? maskType : MaskType.Inclusive;
            return this;
        }

        public Builder exact() {
            this.maskType = MaskType.Exact;
            return this;
        }

        public Builder inclusive() {
            this.maskType = MaskType.Inclusive;
            return this;
        }

        public Builder addNamePattern(int sequenceNumber, String regex) {
            this.namePatterns.put(sequenceNumber, regex);
            if (sequenceNumber >= nextSeq) {
                nextSeq = sequenceNumber + 1;
            }
            return this;
        }

        public Builder addNamePattern(String regex) {
            return addNamePattern(nextSeq++, regex);
        }

        public Builder addCanonicalPattern(int sequenceNumber, String qualifierRegex, String nameRegex) {
            this.canonicalPatterns.put(sequenceNumber, EntryPattern.of(qualifierRegex, nameRegex));
            if (sequenceNumber >= nextSeq) {
                nextSeq = sequenceNumber + 1;
            }
            return this;
        }

        public Builder addCanonicalPattern(String qualifierRegex, String nameRegex) {
            return addCanonicalPattern(nextSeq++, qualifierRegex, nameRegex);
        }

        public TopicMask build() {
            UUID maskId = id != null ? id : UUID.randomUUID();
            return new TopicMask(maskId, type, maskType, namePatterns, canonicalPatterns);
        }
    }
}
