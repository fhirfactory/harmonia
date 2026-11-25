/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming;

import java.util.function.Supplier;
import net.fhirfactory.harmonia.model.core.naming.validation.NamingRuleViolation;

/**
 * Failure boundary between the supported Naming models and their implementation packages.
 *
 * <p>The validation, canonical, matching and interchange packages report a
 * rejected input as a {@link NamingRuleViolation}. Every supported operation
 * that delegates to them does so through this boundary, which converts the
 * violation into the supported {@link NamingValidationException} with the
 * same category, operation, field kind and entry index. Callers therefore see
 * only the public exception, while its construction stays package-private.</p>
 *
 * <p>The boundary translates only rule violations. Any other runtime failure
 * is a defect and propagates unchanged rather than being disguised as a
 * validation result.</p>
 */
final class NamingBoundary {

    /** Prevents construction of this stateless boundary. */
    private NamingBoundary() {
        throw new AssertionError("No instances");
    }

    /**
     * Runs a delegated computation and translates a rule violation into the public exception.
     *
     * @param action delegated computation
     * @param <T> result type
     * @return the computation's result
     * @throws NamingValidationException when the computation reports a rule violation
     */
    static <T> T guard(Supplier<T> action) {
        try {
            return action.get();
        } catch (NamingRuleViolation violation) {
            throw translate(violation);
        }
    }

    /**
     * Runs a delegated check and translates a rule violation into the public exception.
     *
     * @param check delegated validation
     * @throws NamingValidationException when the check reports a rule violation
     */
    static void check(Runnable check) {
        try {
            check.run();
        } catch (NamingRuleViolation violation) {
            throw translate(violation);
        }
    }

    /** Copies only the safe structural metadata; the violation itself is not retained as a cause. */
    private static NamingValidationException translate(NamingRuleViolation violation) {
        return NamingValidationException.of(violation.getCategory(), violation.getOperation(),
                violation.getFieldKind(), violation.getEntryIndex());
    }
}
