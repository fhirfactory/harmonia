/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
/**
 * Naming field rules, fixed resource limits and the payload-free rule violation.
 *
 * <p>Implementation package of the Naming component; not part of the supported
 * Naming API. It depends only on {@code naming.valuesets}. Every other Naming
 * package uses these rules and limits, so all construction and parsing routes
 * share one definition of a valid field and one set of bounds.</p>
 */
package net.fhirfactory.harmonia.model.core.naming.validation;
