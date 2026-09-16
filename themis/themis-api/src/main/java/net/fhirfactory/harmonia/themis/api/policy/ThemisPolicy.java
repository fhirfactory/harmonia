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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.fhirfactory.harmonia.themis.api.policy;

import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationDecision;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationRequest;

/**
 * Contract for a discrete security policy evaluated by Themis.
 */
public interface ThemisPolicy {

    /**
     * Unique identifier for this policy (e.g. "provider-registry-read-policy").
     */
    String getPolicyId();

    /**
     * Human-readable description of what this policy governs.
     */
    String getDescription();

    /**
     * Evaluation order / priority (lower values evaluate earlier).
     */
    default int getOrder() {
        return 100;
    }

    /**
     * Determines whether this policy is applicable to the given authorization request.
     */
    boolean appliesTo(ThemisAuthorizationRequest request);

    /**
     * Evaluates the request and returns an authorization decision (ALLOW or DENY).
     */
    ThemisAuthorizationDecision evaluate(ThemisAuthorizationRequest request);

    /**
     * Indicates whether this policy is an explicit DENY policy.
     * Explicit deny policies are evaluated before any allow rules.
     */
    default boolean isExplicitDeny() {
        return false;
    }
}
