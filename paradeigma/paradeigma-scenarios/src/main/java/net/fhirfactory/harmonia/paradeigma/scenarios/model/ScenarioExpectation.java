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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package net.fhirfactory.harmonia.paradeigma.scenarios.model;

import net.fhirfactory.harmonia.model.pragma.PragmaStatus;
import net.fhirfactory.harmonia.themis.api.model.ThemisDecision;

/**
 * Encapsulates expected outcomes, security decisions, lifecycle statuses, and logging assertions
 * for a simulated scenario execution.
 */
public record ScenarioExpectation(
        PragmaStatus expectedPragmaStatus,
        ThemisDecision expectedSecurityDecision,
        boolean expectOperationOutcome,
        String expectedIssueCode,
        boolean expectPhiLogged,
        boolean expectSecretsBlocked
) {

    public static ScenarioExpectation success() {
        return new ScenarioExpectation(
                PragmaStatus.COMPLETED,
                ThemisDecision.ALLOW,
                false,
                null,
                false,
                true
        );
    }

    public static ScenarioExpectation successWithPhiDiagnostic() {
        return new ScenarioExpectation(
                PragmaStatus.COMPLETED,
                ThemisDecision.ALLOW,
                false,
                null,
                true,
                true
        );
    }

    public static ScenarioExpectation validationRejected(String expectedIssueCode) {
        return new ScenarioExpectation(
                PragmaStatus.REJECTED,
                ThemisDecision.ALLOW,
                true,
                expectedIssueCode,
                false,
                true
        );
    }

    public static ScenarioExpectation securityDenied() {
        return new ScenarioExpectation(
                null,
                ThemisDecision.DENY,
                true,
                "SECURITY_DENIED",
                false,
                true
        );
    }

    public static ScenarioExpectation conflictPreconditionFailed() {
        return new ScenarioExpectation(
                PragmaStatus.FAILED,
                ThemisDecision.ALLOW,
                true,
                "ETAG_CONFLICT",
                false,
                true
        );
    }

    public static ScenarioExpectation persistenceFailed() {
        return new ScenarioExpectation(
                PragmaStatus.FAILED,
                ThemisDecision.ALLOW,
                true,
                "STORAGE_ERROR",
                false,
                true
        );
    }
}
