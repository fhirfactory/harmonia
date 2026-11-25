/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
/**
 * Bounded TopicMask matching: wildcard and version-constraint descriptions,
 * compiled criteria, the evaluator and optional invocation-local diagnostics.
 *
 * <p>Implementation package of the Naming component; not part of the supported
 * Naming API. Matching uses only JDK string operations and component-wise
 * decimal comparison, with no regular expressions, recursion or backtracking.
 * The evaluator reads candidates through a neutral view, so this package
 * depends only on {@code naming.validation} and {@code naming.valuesets}.
 * Diagnostic types and the test probe are reachable only through the
 * TopicMask model's package-private overloads.</p>
 */
package net.fhirfactory.harmonia.model.core.naming.matching;
