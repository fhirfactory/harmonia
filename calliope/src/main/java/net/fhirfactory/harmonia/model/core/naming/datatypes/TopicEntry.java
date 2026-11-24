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

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.fhirfactory.harmonia.model.core.naming.support.TopicFieldRules;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/**
 * One immutable, typed position in a Topic path.
 * <p>
 * Qualifier and value retain their exact validated text. An optional version
 * belongs to this entry and is distinct from Topic identity, as specified by
 * {@code DRP-Topic-and-TopicMask.Final.001.001.md} §1.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE,
        creatorVisibility = JsonAutoDetect.Visibility.NONE)
public final class TopicEntry implements Serializable, Comparable<TopicEntry> {

    private static final long serialVersionUID = 2L;

    private final String qualifier;
    private final String value;
    private final String version;

    /**
     * Reconstructs one validated logical Topic entry.
     *
     * @param qualifier printable ASCII qualifier
     * @param value valid nonempty Unicode value
     * @param version optional printable ASCII version; empty is interpreted as absent
     */
    @JsonCreator
    public TopicEntry(
            @JsonProperty("qualifier") String qualifier,
            @JsonProperty("value") String value,
            @JsonProperty("version") String version) {
        TopicFieldRules.requireQualifier(qualifier);
        TopicFieldRules.requireValue(value);
        this.qualifier = qualifier;
        this.value = value;
        this.version = TopicFieldRules.normalizedVersion(version);
    }

    /**
     * Returns the semantic qualifier of this entry.
     *
     * @return non-blank qualifier string
     */
    @JsonProperty("qualifier")
    public String getQualifier() {
        return qualifier;
    }

    /**
     * Returns the literal value at this path position.
     *
     * @return non-blank name string
     */
    @JsonProperty("value")
    public String getValue() {
        return value;
    }

    /**
     * Returns this entry's optional version.
     *
     * @return empty when the version is absent
     */
    public Optional<String> getVersion() {
        return Optional.ofNullable(version);
    }

    /**
     * Exposes the nullable interchange field while keeping the Java API optional.
     *
     * @return version text or null when absent
     */
    @JsonProperty("version")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String versionForJson() {
        return version;
    }

    @Override
    public int compareTo(TopicEntry other) {
        if (other == null) {
            return 1;
        }
        int cmp = qualifier.compareTo(other.qualifier);
        if (cmp != 0) {
            return cmp;
        }
        cmp = value.compareTo(other.value);
        if (cmp != 0) {
            return cmp;
        }
        if (version == null) {
            return other.version == null ? 0 : -1;
        }
        return other.version == null ? 1 : version.compareTo(other.version);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TopicEntry that = (TopicEntry) o;
        return Objects.equals(qualifier, that.qualifier)
                && Objects.equals(value, that.value)
                && Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(qualifier, value, version);
    }

    @Override
    public String toString() {
        return "TopicEntry{qualifier, value, version=" + (version == null ? "absent" : "present") + '}';
    }

    private Object writeReplace() {
        return new SerializationProxy(qualifier, value, version);
    }

    private void readObject(ObjectInputStream stream) throws InvalidObjectException {
        throw new InvalidObjectException("Serialization proxy required");
    }

    private static final class SerializationProxy implements Serializable {
        private static final long serialVersionUID = 1L;
        private final String qualifier;
        private final String value;
        private final String version;

        private SerializationProxy(String qualifier, String value, String version) {
            this.qualifier = qualifier;
            this.value = value;
            this.version = version;
        }

        private Object readResolve() {
            return new TopicEntry(qualifier, value, version);
        }
    }
}
