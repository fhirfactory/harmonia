/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import net.fhirfactory.harmonia.model.core.naming.validation.TopicFieldRules;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingOperation;

/**
 * One immutable, typed segment of an ordered Topic path.
 *
 * <p>The qualifier describes the role of the value at its path position. A
 * version is optional data for that entry and is not part of Topic identity.</p>
 *
 * <p>Entries are serializable through a private proxy; deserialization
 * re-validates the completed fields and rejects streams that bypass it.</p>
 */
public final class TopicEntry implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /** Completed qualifier; serialized only through {@link SerialForm}. */
    private final transient String qualifier;
    /** Completed value; serialized only through {@link SerialForm}. */
    private final transient String value;
    /** Optional version, {@code null} when absent; serialized only through {@link SerialForm}. */
    private final transient String version;

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
        NamingBoundary.check(() -> TopicFieldRules.validateTopicCompletedFields(
                qualifier, value, version, operation, index));
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

    /**
     * Serializes through {@link SerialForm} so deserialization re-runs strict validation.
     *
     * @return the validating serialization proxy
     */
    @Serial
    private Object writeReplace() {
        return new SerialForm(qualifier, value, version);
    }

    /**
     * Rejects any stream that tries to restore an entry without its validating proxy.
     *
     * @param stream the object stream, which is never read
     * @throws InvalidObjectException always, because only the proxy may be deserialized
     */
    @Serial
    private void readObject(ObjectInputStream stream) throws InvalidObjectException {
        throw NamingSerialization.invalid(TopicEntry.class);
    }

    /** Serialized form of one entry; a {@code null} version records absence. */
    static final class SerialForm implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        private final String qualifier;
        private final String value;
        private final String version;

        /** Captures entry fields; also used by tests to exercise rejection of crafted streams. */
        SerialForm(String qualifier, String value, String version) {
            this.qualifier = qualifier;
            this.value = value;
            this.version = version;
        }

        /** Rebuilds the entry through strict completed-field validation. */
        @Serial
        private Object readResolve() throws ObjectStreamException {
            try {
                return create(qualifier, value, version, NamingOperation.TOPIC_BUILD, null);
            } catch (NamingValidationException rejected) {
                throw NamingSerialization.invalid(TopicEntry.class);
            }
        }
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