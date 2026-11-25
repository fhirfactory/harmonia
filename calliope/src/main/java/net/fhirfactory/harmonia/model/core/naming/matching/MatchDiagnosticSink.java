/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming.matching;

/**
 * Synchronous, invocation-local destination for safe comparison metadata.
 *
 * <p>A sink is supplied per evaluation through the TopicMask model's
 * package-private overload; the supported {@code matches(Topic)} operation
 * accepts none. It receives at most one record, after the result is fixed.</p>
 *
 * <p>This type is implementation machinery of the Naming component and is not
 * part of the supported Naming API.</p>
 */
@FunctionalInterface
public interface MatchDiagnosticSink {

    /**
     * Receives one safe exit record synchronously within the current match call.
     *
     * @param diagnostic payload-free exit metadata
     */
    void accept(NamingDiagnostic diagnostic);
}
