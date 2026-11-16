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

package net.fhirfactory.harmonia.model.core.naming.datatypes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * An immutable pattern specification for an explicitly sequenced entry in a canonical name mask.
 * <p>
 * Evaluates whether a candidate {@link TopicEntry} satisfies this level's qualifier and name criteria
 * independently using regular expressions.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TopicMaskEntryPattern implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String qualifierRegex;
    private final String nameRegex;

    @JsonIgnore
    private final transient Pattern compiledQualifierPattern;

    @JsonIgnore
    private final transient Pattern compiledNamePattern;

    /**
     * Constructs a pattern entry from qualifier and name regular expressions.
     *
     * @param qualifierRegex regular expression for matching entry qualifier; must not be blank
     * @param nameRegex      regular expression for matching entry name; must not be blank
     * @throws IllegalArgumentException if either regex is null, blank, or an invalid pattern
     */
    @JsonCreator
    public TopicMaskEntryPattern(
            @JsonProperty("qualifierRegex") String qualifierRegex,
            @JsonProperty("nameRegex") String nameRegex) {
        if (StringUtils.isBlank(qualifierRegex)) {
            throw new IllegalArgumentException("qualifierRegex must not be null or blank");
        }
        if (StringUtils.isBlank(nameRegex)) {
            throw new IllegalArgumentException("nameRegex must not be null or blank");
        }
        this.qualifierRegex = qualifierRegex.trim();
        this.nameRegex = nameRegex.trim();
        try {
            this.compiledQualifierPattern = Pattern.compile(this.qualifierRegex);
        } catch (PatternSyntaxException e) {
            throw new IllegalArgumentException("Invalid qualifierRegex pattern: " + qualifierRegex, e);
        }
        try {
            this.compiledNamePattern = Pattern.compile(this.nameRegex);
        } catch (PatternSyntaxException e) {
            throw new IllegalArgumentException("Invalid nameRegex pattern: " + nameRegex, e);
        }
    }

    /**
     * Returns the qualifier regular expression.
     *
     * @return qualifier regex string
     */
    public String getQualifierRegex() {
        return qualifierRegex;
    }

    /**
     * Returns the name regular expression.
     *
     * @return name regex string
     */
    public String getNameRegex() {
        return nameRegex;
    }

    /**
     * Evaluates whether a candidate {@link TopicEntry} matches both qualifier and name patterns.
     *
     * @param entry the candidate entry to evaluate
     * @return {@code true} if both qualifier and name match, otherwise {@code false}
     */
    public boolean matches(TopicEntry entry) {
        if (entry == null) {
            return false;
        }
        return matches(entry.getQualifier(), entry.getName());
    }

    /**
     * Evaluates whether a candidate qualifier and name match both patterns.
     *
     * @param candidateQualifier candidate qualifier string
     * @param candidateName      candidate name string
     * @return {@code true} if both match, otherwise {@code false}
     */
    public boolean matches(String candidateQualifier, String candidateName) {
        if (candidateQualifier == null || candidateName == null) {
            return false;
        }
        Pattern qPat = compiledQualifierPattern != null ? compiledQualifierPattern : Pattern.compile(qualifierRegex);
        Pattern nPat = compiledNamePattern != null ? compiledNamePattern : Pattern.compile(nameRegex);
        return qPat.matcher(candidateQualifier).matches() && nPat.matcher(candidateName).matches();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof TopicMaskEntryPattern)) return false;
        TopicMaskEntryPattern that = (TopicMaskEntryPattern) o;
        return Objects.equals(qualifierRegex, that.qualifierRegex) && Objects.equals(nameRegex, that.nameRegex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(qualifierRegex, nameRegex);
    }

    @Override
    public String toString() {
        return "TopicMaskEntryPattern{" +
                "qualifierRegex='" + qualifierRegex + '\'' +
                ", nameRegex='" + nameRegex + '\'' +
                '}';
    }
}
