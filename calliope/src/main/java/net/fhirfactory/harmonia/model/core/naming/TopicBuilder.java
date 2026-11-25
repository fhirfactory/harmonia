/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import net.fhirfactory.harmonia.model.core.naming.validation.NamingLimits;
import net.fhirfactory.harmonia.model.core.naming.validation.TopicFieldRules;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingFailureCategory;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingFieldKind;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingOperation;

/**
 * Reusable, caller-confined builder for sequential or explicitly indexed paths.
 *
 * <p>The first successful addition selects one mode. Failed additions and
 * failed builds leave all accumulated inputs unchanged, allowing callers to
 * correct a gap or invalid replacement and retry.</p>
 */
public final class TopicBuilder {
    /** Construction mode is selected only by the first successful addition. */
    private enum Mode { SEQUENTIAL, INDEXED }

    private Mode mode;
    private final List<Input> sequential = new ArrayList<>();
    private final Map<Integer, Input> indexed = new TreeMap<>();

    /** Creates an empty builder whose construction mode is not yet selected. */
    TopicBuilder() {
    }

    /**
     * Appends one entry and generates {@code N<position>} for a null or empty qualifier.
     *
     * @param qualifier supplied printable-ASCII qualifier, or null/empty for generation
     * @param value required Topic value
     * @return this builder for fluent construction
     * @throws NamingValidationException when input is invalid, mode is indexed, or depth is full
     */
    public TopicBuilder addEntry(String qualifier, String value) {
        return addEntry(qualifier, value, null);
    }

    /**
     * Appends one entry; null and empty version values both mean absence.
     *
     * @param qualifier supplied printable-ASCII qualifier, or null/empty for generation
     * @param value required Topic value
     * @param version optional printable-ASCII version
     * @return this builder for fluent construction
     * @throws NamingValidationException when input is invalid, mode is indexed, or depth is full
     */
    public TopicBuilder addEntry(String qualifier, String value, String version) {
        requireMode(Mode.SEQUENTIAL);
        if (sequential.size() >= NamingLimits.MAX_DEPTH) {
            throw NamingValidationException.of(NamingFailureCategory.LIMIT_EXCEEDED,
                    NamingOperation.TOPIC_BUILD, NamingFieldKind.DEPTH, null);
        }
        int index = sequential.size();
        Input input = validateInput(qualifier, value, version, index);
        sequential.add(input);
        mode = Mode.SEQUENTIAL;
        return this;
    }

    /**
     * Adds or replaces an entry at a zero-based final index.
     *
     * <p>Adding a new position can increase the eventual depth. Indexed build
     * requires every position from zero through the highest added index.</p>
     *
     * @param index zero-based final position
     * @param qualifier supplied qualifier, or null/empty for generation at this position
     * @param value required Topic value
     * @return this builder for fluent construction
     * @throws NamingValidationException when input, index, mode, or depth is invalid
     */
    public TopicBuilder addEntry(int index, String qualifier, String value) {
        return addEntry(index, qualifier, value, null);
    }

    /**
     * Adds or replaces an indexed entry; null and empty versions mean absence.
     *
     * <p>Adding a new position can increase the eventual depth. A failed
     * validation does not replace an occupied position.</p>
     *
     * @param index zero-based final position
     * @param qualifier supplied qualifier, or null/empty for generation at this position
     * @param value required Topic value
     * @param version optional printable-ASCII version
     * @return this builder for fluent construction
     * @throws NamingValidationException when input, index, mode, or depth is invalid
     */
    public TopicBuilder addEntry(int index, String qualifier, String value, String version) {
        if (index < 0) {
            throw NamingValidationException.of(NamingFailureCategory.INVALID_ARGUMENT,
                    NamingOperation.TOPIC_BUILD, NamingFieldKind.INDEX, null);
        }
        if (index >= NamingLimits.MAX_DEPTH) {
            throw NamingValidationException.of(NamingFailureCategory.LIMIT_EXCEEDED,
                    NamingOperation.TOPIC_BUILD, NamingFieldKind.DEPTH, index);
        }
        requireMode(Mode.INDEXED);
        Input input = validateInput(qualifier, value, version, index);
        indexed.put(index, input);
        mode = Mode.INDEXED;
        return this;
    }

    /**
     * Builds an independent immutable snapshot without consuming or repairing stored inputs.
     *
     * @return a completed immutable Topic
     * @throws NamingValidationException when no entry exists or indexed positions contain a gap
     */
    public Topic build() {
        if (mode == null) {
            throw NamingValidationException.of(NamingFailureCategory.MISSING_INPUT,
                    NamingOperation.TOPIC_BUILD, NamingFieldKind.DEPTH, null);
        }
        List<Input> inputs = mode == Mode.SEQUENTIAL ? List.copyOf(sequential) : contiguousInputs();
        if (inputs.isEmpty()) {
            throw NamingValidationException.of(NamingFailureCategory.MISSING_INPUT,
                    NamingOperation.TOPIC_BUILD, NamingFieldKind.DEPTH, null);
        }
        List<TopicEntry> entries = new ArrayList<>(inputs.size());
        for (int index = 0; index < inputs.size(); index++) {
            Input input = inputs.get(index);
            String qualifier = input.qualifier == null || input.qualifier.isEmpty()
                    ? "N" + index : input.qualifier;
            entries.add(TopicEntry.create(qualifier, input.value, input.version,
                    NamingOperation.TOPIC_BUILD, index));
        }
        return Topic.fromEntries(entries);
    }

    /** Validates a proposed entry completely before any list or map mutation. */
    private Input validateInput(String qualifier, String value, String version, int index) {
        return NamingBoundary.guard(() -> {
            TopicFieldRules.validateQualifierInput(qualifier, true, NamingOperation.TOPIC_BUILD, index);
            TopicFieldRules.validateValue(value, NamingOperation.TOPIC_BUILD, index);
            String normalizedVersion = TopicFieldRules.normalizeVersion(
                    version, NamingOperation.TOPIC_BUILD, index);
            return new Input(qualifier, value, normalizedVersion);
        });
    }

    /** Rejects a mode change without altering the previously selected mode. */
    private void requireMode(Mode requested) {
        if (mode != null && mode != requested) {
            throw NamingValidationException.of(NamingFailureCategory.INVALID_STATE,
                    NamingOperation.TOPIC_BUILD, NamingFieldKind.NONE, null);
        }
    }

    /** Builds a temporary ordered view only when indexed positions are contiguous. */
    private List<Input> contiguousInputs() {
        if (indexed.isEmpty()) {
            return List.of();
        }
        if (indexed.size() > NamingLimits.MAX_DEPTH) {
            throw NamingValidationException.of(NamingFailureCategory.LIMIT_EXCEEDED,
                    NamingOperation.TOPIC_BUILD, NamingFieldKind.DEPTH, null);
        }
        List<Input> inputs = new ArrayList<>(indexed.size());
        for (int expected = 0; expected < indexed.size(); expected++) {
            Input input = indexed.get(expected);
            if (input == null) {
                throw NamingValidationException.of(NamingFailureCategory.INVALID_FIELD,
                        NamingOperation.TOPIC_BUILD, NamingFieldKind.DEPTH, expected);
            }
            inputs.add(input);
        }
        return inputs;
    }

    /** Stores a validated proposal until the builder can assemble a complete path. */
    private record Input(String qualifier, String value, String version) {
    }
}