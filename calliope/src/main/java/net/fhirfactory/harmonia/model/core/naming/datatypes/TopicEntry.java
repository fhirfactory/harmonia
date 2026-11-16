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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Objects;

/**
 * An immutable element of a {@link net.fhirfactory.harmonia.model.core.naming.Topic}'s canonical name,
 * consisting of a qualifier and a name.
 * <p>
 * In accordance with the accepted Architecture Continuum:
 * <ul>
 *   <li>The qualifier identifies the semantic role of the entry (e.g. {@code Domain}, {@code MessageType},
 *       {@code TriggerEvent}, {@code ResourceType}, {@code Interaction}).</li>
 *   <li>The name specifies the concrete value at this hierarchical level (e.g. {@code Health}, {@code ADT},
 *       {@code A01}, {@code Practitioner}, {@code CREATE}).</li>
 *   <li>Neither qualifier nor name may be null or blank, and name must not contain the delimiter dot ({@code .}).</li>
 * </ul>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TopicEntry implements Serializable, Comparable<TopicEntry> {

    private static final long serialVersionUID = 1L;

    private final String qualifier;
    private final String name;

    /**
     * Constructs an immutable canonical name entry.
     *
     * @param qualifier the semantic qualifier for this level; must not be blank
     * @param name      the name value for this level; must not be blank and must not contain dots
     * @throws IllegalArgumentException if qualifier or name is null or blank, or if name contains a dot
     */
    @JsonCreator
    public TopicEntry(
            @JsonProperty("qualifier") String qualifier,
            @JsonProperty("name") String name) {
        if (StringUtils.isBlank(qualifier)) {
            throw new IllegalArgumentException("TopicEntry qualifier must not be null or blank");
        }
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("TopicEntry name must not be null or blank");
        }
        String trimmedQualifier = qualifier.trim();
        String trimmedName = name.trim();
        this.qualifier = trimmedQualifier;
        this.name = trimmedName;
    }

    /**
     * Returns the semantic qualifier of this entry.
     *
     * @return non-blank qualifier string
     */
    public String getQualifier() {
        return qualifier;
    }

    /**
     * Returns the name value of this entry.
     *
     * @return non-blank name string
     */
    public String getName() {
        return name;
    }

    /**
     * Formats this entry as a canonical pair: {@code qualifier=name}.
     *
     * @return formatted canonical entry string
     */
    public String toCanonicalString() {
        return qualifier + "=" + name;
    }

    /**
     * Formats this entry as its unqualified name representation.
     *
     * @return unqualified name string
     */
    public String toTopicString() {
        return name;
    }

    @Override
    public int compareTo(TopicEntry other) {
        if (other == null) {
            return 1;
        }
        int cmp = this.qualifier.compareTo(other.qualifier);
        if (cmp != 0) {
            return cmp;
        }
        return this.name.compareTo(other.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof TopicEntry)) return false;
        TopicEntry that = (TopicEntry) o;
        return Objects.equals(qualifier, that.qualifier) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(qualifier, name);
    }

    @Override
    public String toString() {
        return toCanonicalString();
    }
}
