/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming;

/** Synchronous, invocation-local destination for safe comparison metadata. */
@FunctionalInterface
interface MatchDiagnosticSink {
    /** Receives one safe exit record synchronously within the current match call. */
    void accept(NamingDiagnostic diagnostic);
}