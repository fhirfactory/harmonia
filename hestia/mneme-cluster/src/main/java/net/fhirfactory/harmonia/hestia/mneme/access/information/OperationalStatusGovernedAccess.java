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

package net.fhirfactory.harmonia.hestia.mneme.access.information;

import net.fhirfactory.harmonia.hapifhir.persistence.model.AuthoritativePersistenceResult;
import net.fhirfactory.harmonia.hestia.mnemosyne.api.GovernedOperationalStatusPersistence;
import net.fhirfactory.harmonia.hestia.mnemosyne.api.OperationalStatusKey;
import net.fhirfactory.harmonia.hestia.mnemosyne.api.OperationalStatusRecord;
import net.fhirfactory.harmonia.model.core.information.access.ExpectedAuthoritativeVersion;
import net.fhirfactory.harmonia.themis.api.ThemisAuthorizer;
import net.fhirfactory.harmonia.themis.api.model.ThemisAction;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationDecision;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationRequest;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisResource;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;

import java.util.Objects;
import java.util.Set;

/**
 * Mneme access facade for owner-defined operational Status. It applies Themis at the caller boundary
 * and delegates the opaque record to Mnemosyne without converting or interpreting it as an IU.
 */
public final class OperationalStatusGovernedAccess {

    private final ThemisAuthorizer themisAuthorizer;
    private final GovernedOperationalStatusPersistence persistence;

    /** Creates the Status facade from the existing Themis authorizer and Mnemosyne port. */
    public OperationalStatusGovernedAccess(
            ThemisAuthorizer themisAuthorizer,
            GovernedOperationalStatusPersistence persistence) {
        this.themisAuthorizer = Objects.requireNonNull(themisAuthorizer, "themisAuthorizer must not be null");
        this.persistence = Objects.requireNonNull(persistence, "persistence must not be null");
    }

    /** Records a new or versioned opaque Status payload after caller-side authorization. */
    public AuthoritativePersistenceResult<OperationalStatusRecord> record(
            OperationalStatusKey key,
            String payload,
            ExpectedAuthoritativeVersion expectedVersion,
            ThemisSecurityContext securityContext) {
        Objects.requireNonNull(key, "key must not be null");
        Objects.requireNonNull(payload, "payload must not be null");
        Objects.requireNonNull(expectedVersion, "expectedVersion must not be null");
        ThemisAction action = expectedVersion.isNone() ? ThemisAction.CREATE : ThemisAction.UPDATE;
        if (!isAllowed(action, key, securityContext)) {
            return new AuthoritativePersistenceResult.NotCommitted<>("Themis denied Operational Status " + action);
        }
        return persistence.record(OperationalStatusRecord.proposed(key, payload), expectedVersion, securityContext);
    }

    /** Reads an owner's Status after caller-side authorization. */
    public AuthoritativePersistenceResult<OperationalStatusRecord> read(
            OperationalStatusKey key,
            ThemisSecurityContext securityContext) {
        Objects.requireNonNull(key, "key must not be null");
        if (!isAllowed(ThemisAction.READ, key, securityContext)) {
            return new AuthoritativePersistenceResult.NotCommitted<>("Themis denied Operational Status READ");
        }
        return persistence.read(key, securityContext);
    }

    /** Evaluates the requested operation and fails closed on missing context, denial, or PDP error. */
    private boolean isAllowed(ThemisAction action, OperationalStatusKey key, ThemisSecurityContext context) {
        if (context == null) {
            return false;
        }
        ThemisPrincipal principal = context.requestingPrincipal() != null
                ? context.requestingPrincipal()
                : ThemisPrincipal.system("anonymous");
        ThemisAuthorizationRequest request = ThemisAuthorizationRequest.of(
                principal,
                context.authorities() == null ? Set.of() : context.authorities(),
                action,
                ThemisResource.of(key.resourceKey().resourceType(), key.resourceKey().id()),
                context);
        try {
            ThemisAuthorizationDecision decision = themisAuthorizer.authorize(request);
            return decision != null && decision.isAllowed();
        } catch (RuntimeException exception) {
            return false;
        }
    }
}