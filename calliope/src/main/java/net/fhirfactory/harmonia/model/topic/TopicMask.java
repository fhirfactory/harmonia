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
import net.fhirfactory.harmonia.model.core.information.InformationUnitIdentifier;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Encapsulates a general-purpose Harmonia TopicMask providing element-wise regular-expression discrimination
 * across hierarchical dot-delimited Topic names.
 * <p>
 * <b>Architectural Context &amp; Authority:</b>
 * In accordance with {@code DRP-Information-Unit-Framework.Final.001.md} (§§10, 12; IU-05):
 * <ul>
 *   <li><b>TopicMask is a general-purpose Harmonia construct</b> usable by publication/subscription, routing,
 *       discovery, capability matching, and security.</li>
 *   <li><b>Element-Wise Regex Matching:</b> A TopicMask comprises regular-expression patterns applied separately
 *       to the corresponding individual dot-separated canonical-name elements.
 *       It is <b>not</b> one monolithic whole-name regex and not a precomputed enumeration of Topic IDs.</li>
 *   <li><b>Literal Dot Boundaries:</b> Literal dot-separated boundaries remain strict boundaries for matching;
 *       an element-level wildcard (such as {@code "*"} or {@code ".*"}) matches within its element and does not
 *       silently span adjacent elements.</li>
 *   <li><b>Shorthand Syntax:</b> The literal shorthand {@code "*"} within an element pattern is treated as
 *       matching any single element value (equivalent to {@code ".*"}).</li>
 * </ul>
 * <p>
 * <b>Defensive Programming:</b>
 * <ul>
 *   <li>Mask patterns are validated and pre-compiled into anchored element {@link Pattern} instances at construction time.</li>
 *   <li>Invalid regex syntax throws an informative {@link IllegalArgumentException}.</li>
 *   <li>Thread-safe and immutable value object design.</li>
 * </ul>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class TopicMask implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String DOT_SPLIT_REGEX = "\\.";
    private static final TopicMask ANY = new TopicMask(".*");

    private final String maskPattern;
    private final List<String> elementPatterns;
    private final transient List<Pattern> compiledPatterns;

    /**
     * Canonical constructor parsing and compiling the element-wise TopicMask.
     *
     * @param maskPattern dot-delimited element-wise regex mask pattern, must not be null or blank
     * @throws IllegalArgumentException if {@code maskPattern} is null, blank, or contains invalid regex syntax
     */
    @JsonCreator
    public TopicMask(@JsonProperty("maskPattern") String maskPattern) {
        if (maskPattern == null || maskPattern.isBlank()) {
            throw new IllegalArgumentException("maskPattern must not be null or blank");
        }
        this.maskPattern = maskPattern.trim();
        String[] rawElements = this.maskPattern.split(DOT_SPLIT_REGEX, -1);
        List<String> elementList = new ArrayList<>(rawElements.length);
        List<Pattern> patternList = new ArrayList<>(rawElements.length);

        for (int i = 0; i < rawElements.length; i++) {
            String elem = rawElements[i];
            elementList.add(elem);
            String regex = elem.equals("*") ? ".*" : elem;
            try {
                // Anchor each element pattern to ensure full element match
                patternList.add(Pattern.compile("^" + regex + "$"));
            } catch (PatternSyntaxException e) {
                throw new IllegalArgumentException(
                        "Invalid regular expression in mask element " + i + " ('" + elem + "'): " + e.getMessage(), e);
            }
        }
        this.elementPatterns = Collections.unmodifiableList(elementList);
        this.compiledPatterns = Collections.unmodifiableList(patternList);
    }

    /**
     * Factory method creating a {@code TopicMask} from a pattern string.
     *
     * @param pattern the mask pattern, must not be null or blank
     * @return compiled TopicMask
     */
    public static TopicMask of(String pattern) {
        return new TopicMask(pattern);
    }

    /**
     * Factory method creating a {@code TopicMask} (alias for {@link #of(String)}).
     *
     * @param pattern the mask pattern
     * @return compiled TopicMask
     */
    public static TopicMask compile(String pattern) {
        return new TopicMask(pattern);
    }

    /**
     * Returns a universal wildcard mask that matches any Topic.
     *
     * @return universal wildcard TopicMask
     */
    public static TopicMask any() {
        return ANY;
    }

    /**
     * Creates an exact-match {@code TopicMask} for a specific topic string.
     *
     * @param exactTopic the exact canonical topic to match
     * @return exact TopicMask
     */
    public static TopicMask exact(String exactTopic) {
        Objects.requireNonNull(exactTopic, "exactTopic must not be null");
        String[] elements = exactTopic.trim().split(DOT_SPLIT_REGEX, -1);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < elements.length; i++) {
            if (i > 0) sb.append(".");
            sb.append(Pattern.quote(elements[i]));
        }
        return new TopicMask(sb.toString());
    }

    // =========================================================================
    // Accessors
    // =========================================================================

    /**
     * Returns the raw mask pattern string.
     *
     * @return the mask pattern
     */
    @JsonProperty("maskPattern")
    public String maskPattern() {
        return maskPattern;
    }

    /**
     * Returns unmodifiable list of element regex pattern strings.
     *
     * @return unmodifiable list of element patterns
     */
    @JsonIgnore
    public List<String> elementPatterns() {
        return elementPatterns;
    }

    /**
     * Returns the number of dot-separated elements expected by this mask.
     *
     * @return element count
     */
    @JsonIgnore
    public int elementCount() {
        return elementPatterns.size();
    }

    // =========================================================================
    // Matching Operations
    // =========================================================================

    /**
     * Evaluates whether a candidate canonical Topic string matches this TopicMask under element-wise regex rules.
     *
     * @param candidateTopic candidate dot-separated topic string
     * @return true if candidate matches all element patterns, false otherwise
     */
    public boolean matches(String candidateTopic) {
        if (candidateTopic == null || candidateTopic.isBlank()) {
            return false;
        }
        String[] candidateElements = candidateTopic.trim().split(DOT_SPLIT_REGEX, -1);
        List<Pattern> patterns = getOrRecompilePatterns();

        // Exact element count matching
        if (candidateElements.length != patterns.size()) {
            return false;
        }

        for (int i = 0; i < patterns.size(); i++) {
            if (!patterns.get(i).matcher(candidateElements[i]).matches()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Evaluates whether a Calliope {@link Topic} instance matches this TopicMask.
     *
     * @param topic the Calliope Topic
     * @return true if topic matches, false otherwise
     */
    public boolean matches(Topic topic) {
        if (topic == null) {
            return false;
        }
        return matches(topic.toTopicString());
    }

    /**
     * Evaluates whether an {@link InformationUnitIdentifier} matches this TopicMask.
     *
     * @param identifier the InformationUnitIdentifier
     * @return true if topicName matches, false otherwise
     */
    public boolean matches(InformationUnitIdentifier identifier) {
        if (identifier == null) {
            return false;
        }
        return matches(identifier.topicName());
    }

    private List<Pattern> getOrRecompilePatterns() {
        if (compiledPatterns != null) {
            return compiledPatterns;
        }
        // Handle post-deserialization re-compilation
        List<Pattern> list = new ArrayList<>(elementPatterns.size());
        for (String elem : elementPatterns) {
            String regex = elem.equals("*") ? ".*" : elem;
            list.add(Pattern.compile("^" + regex + "$"));
        }
        return list;
    }

    // =========================================================================
    // Standard Object Methods
    // =========================================================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TopicMask topicMask = (TopicMask) o;
        return maskPattern.equals(topicMask.maskPattern);
    }

    @Override
    public int hashCode() {
        return maskPattern.hashCode();
    }

    @Override
    public String toString() {
        return maskPattern;
    }
}
