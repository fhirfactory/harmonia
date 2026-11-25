/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming;

import java.util.Objects;
import java.util.Optional;

/**
 * One immutable, typed segment of an ordered Topic path.
 *
 * <p>The qualifier describes the role of the value at its path position. A
 * version is optional data for that entry and is not part of Topic identity.</p>
 */
public final class TopicEntry {
    private final String qualifier;
    private final String value;
    private final String version;

    private TopicEntry(String qualifier, String value, String version) {
        this.qualifier = qualifier;
        this.value = value;
        this.version = version;
    }

    /** Validates and constructs one completed immutable entry without applying defaults. */
    static TopicEntry create(
            String qualifier,
            String value,
            String version,
            NamingOperation operation,
            Integer index) {
        TopicFieldRules.validateTopicCompletedFields(qualifier, value, version, operation, index);
        return new TopicEntry(qualifier, value, version);
    }

    /**
     * Returns the completed qualifier for this path entry.
     *
     * @return the printable-ASCII qualifier
     */
    public String getQualifier() {
        return qualifier;
    }

    /**
     * Returns the validated value for this path entry.
     *
     * @return the preserved well-formed Unicode value
     */
    public String getValue() {
        return value;
    }

    /**
     * Returns the optional entry version, preserving absence explicitly.
     *
     * @return a nonempty printable-ASCII version, or an empty Optional
     */
    public Optional<String> getVersion() {
        return Optional.ofNullable(version);
    }

    /** Returns the nullable internal representation used for canonical omission. */
    String versionOrNull() {
        return version;
    }

    /** Compares complete entry data, including optional version presence and value. */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TopicEntry entry)) {
            return false;
        }
        return qualifier.equals(entry.qualifier)
                && value.equals(entry.value)
                && Objects.equals(version, entry.version);
    }

    /** Hashes the same complete entry data used by {@link #equals(Object)}. */
    @Override
    public int hashCode() {
        return Objects.hash(qualifier, value, version);
    }
}