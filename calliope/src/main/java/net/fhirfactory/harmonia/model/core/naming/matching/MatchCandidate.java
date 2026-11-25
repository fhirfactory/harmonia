/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming.matching;

/**
 * Read-only positional view of a completed candidate path.
 *
 * <p>The evaluator reads candidates only through this view, so the matching
 * package does not depend on the Topic model. The TopicMask model adapts a
 * Topic to this view for the duration of one evaluation. Implementations
 * must describe an already completed, immutable path whose depth is within
 * the accepted bound.</p>
 *
 * <p>This type is implementation machinery of the Naming component and is not
 * part of the supported Naming API.</p>
 */
public interface MatchCandidate {

    /**
     * Returns the number of ordered entries.
     *
     * @return the candidate depth
     */
    int depth();

    /**
     * Returns the qualifier at a position.
     *
     * @param index zero-based position below {@link #depth()}
     * @return the completed qualifier
     */
    String qualifierAt(int index);

    /**
     * Returns the value at a position.
     *
     * @param index zero-based position below {@link #depth()}
     * @return the completed value
     */
    String valueAt(int index);

    /**
     * Returns the optional version at a position.
     *
     * @param index zero-based position below {@link #depth()}
     * @return the opaque version, or {@code null} when absent
     */
    String versionAt(int index);
}
