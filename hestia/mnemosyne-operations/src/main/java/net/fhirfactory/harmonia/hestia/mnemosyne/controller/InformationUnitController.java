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

package net.fhirfactory.harmonia.hestia.mnemosyne.controller;

import net.fhirfactory.harmonia.hapifhir.persistence.model.AuthoritativePersistenceResult;
import net.fhirfactory.harmonia.hestia.mnemosyne.api.GovernedInformationUnitPersistence;
import net.fhirfactory.harmonia.hestia.mnemosyne.framework.GovernedPersistenceFramework;
import net.fhirfactory.harmonia.hestia.mnemosyne.governance.MnemosyneAuthorizationGate;
import net.fhirfactory.harmonia.hestia.mnemosyne.information.InformationUnitConstructor;
import net.fhirfactory.harmonia.hestia.mnemosyne.information.InformationUnitDisassembler;
import net.fhirfactory.harmonia.hestia.mnemosyne.nonfhir.InformationUnitPersistenceRecord;
import net.fhirfactory.harmonia.model.core.information.access.AuthoritativeVersion;
import net.fhirfactory.harmonia.model.core.information.access.ExpectedAuthoritativeVersion;
import net.fhirfactory.harmonia.model.core.information.access.ResourceKey;
import net.fhirfactory.harmonia.model.core.information.entity.InformationUnit;
import net.fhirfactory.harmonia.themis.api.model.ThemisAction;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;

import java.util.Objects;
import java.util.UUID;

/**
 * Mnemosyne InformationUnit PEP/controller. It validates IU identity, authorizes before persistence,
 * disassembles governed state, delegates through the GPF, then verifies and reconstructs reads.
 */
public final class InformationUnitController implements GovernedInformationUnitPersistence {

    private static final String RESOURCE_TYPE = "InformationUnit";

    private final MnemosyneAuthorizationGate authorizationGate;
    private final GovernedPersistenceFramework persistenceFramework;
    private final InformationUnitDisassembler disassembler;
    private final InformationUnitConstructor constructor;

    /** Creates the controller using the established Themis, GPF and IU conversion collaborators. */
    public InformationUnitController(
            MnemosyneAuthorizationGate authorizationGate,
            GovernedPersistenceFramework persistenceFramework,
            InformationUnitDisassembler disassembler,
            InformationUnitConstructor constructor) {
        this.authorizationGate = Objects.requireNonNull(authorizationGate, "authorizationGate must not be null");
        this.persistenceFramework = Objects.requireNonNull(persistenceFramework, "persistenceFramework must not be null");
        this.disassembler = Objects.requireNonNull(disassembler, "disassembler must not be null");
        this.constructor = Objects.requireNonNull(constructor, "constructor must not be null");
    }

    /** Establishes a new IU only after Themis permits CREATE. */
    @Override
    public AuthoritativePersistenceResult<InformationUnit> create(
            ResourceKey key,
            InformationUnit proposed,
            ThemisSecurityContext securityContext) {
        validateIdentity(key, proposed);
        authorizationGate.requireAuthorized(ThemisAction.CREATE, key, securityContext);
        return toInformationUnit(persistenceFramework.create(disassembler.disassemble(proposed)));
    }

    /** Establishes the next IU revision only after Themis permits UPDATE and the expected version is present. */
    @Override
    public AuthoritativePersistenceResult<InformationUnit> update(
            ResourceKey key,
            InformationUnit proposed,
            ExpectedAuthoritativeVersion expectedVersion,
            ThemisSecurityContext securityContext) {
        validateIdentity(key, proposed);
        if (expectedVersion == null || expectedVersion.isNone()) {
            throw new IllegalArgumentException("expectedVersion must identify an authoritative predecessor");
        }
        authorizationGate.requireAuthorized(ThemisAction.UPDATE, key, securityContext);
        return toInformationUnit(persistenceFramework.update(disassembler.disassemble(proposed), expectedVersion));
    }

    /** Retrieves and integrity-checks an IU only after Themis permits READ. */
    @Override
    public AuthoritativePersistenceResult<InformationUnit> read(ResourceKey key, ThemisSecurityContext securityContext) {
        UUID identifier = validateKey(key);
        authorizationGate.requireAuthorized(ThemisAction.READ, key, securityContext);
        return toInformationUnit(persistenceFramework.read(identifier));
    }

    /** Retrieves the authoritative IU version only after Themis permits READ. */
    @Override
    public AuthoritativePersistenceResult<AuthoritativeVersion> readVersion(
            ResourceKey key,
            ThemisSecurityContext securityContext) {
        UUID identifier = validateKey(key);
        authorizationGate.requireAuthorized(ThemisAction.READ, key, securityContext);
        return persistenceFramework.readVersion(identifier);
    }

    /** Validates that resource key and IU payload describe the same canonical identity. */
    private void validateIdentity(ResourceKey key, InformationUnit proposed) {
        Objects.requireNonNull(proposed, "proposed must not be null");
        UUID identifier = validateKey(key);
        if (!identifier.equals(proposed.identifier().uuid())) {
            throw new IllegalArgumentException("Resource key does not match InformationUnit identity");
        }
    }

    /** Parses a canonical IU resource key and rejects other resource types. */
    private UUID validateKey(ResourceKey key) {
        Objects.requireNonNull(key, "key must not be null");
        if (!RESOURCE_TYPE.equals(key.resourceType())) {
            throw new IllegalArgumentException("Resource key type must be " + RESOURCE_TYPE);
        }
        try {
            UUID identifier = UUID.fromString(key.id());
            if (!identifier.toString().equalsIgnoreCase(key.id())) {
                throw new IllegalArgumentException("Resource key UUID must use canonical form");
            }
            return identifier;
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Resource key must contain a UUID identifier", exception);
        }
    }

    /** Maps storage results without losing conflicts, absence, failure, uncertainty or durability. */
    private AuthoritativePersistenceResult<InformationUnit> toInformationUnit(
            AuthoritativePersistenceResult<InformationUnitPersistenceRecord> result) {
        if (result instanceof AuthoritativePersistenceResult.Committed<InformationUnitPersistenceRecord> committed) {
            try {
                return new AuthoritativePersistenceResult.Committed<>(
                        constructor.construct(committed.persistedResource()),
                        committed.authoritativeVersion(),
                        committed.durabilityAssurance());
            } catch (RuntimeException exception) {
                return new AuthoritativePersistenceResult.NotCommitted<>(
                        "Persisted InformationUnit failed integrity or reconstruction checks", exception);
            }
        }
        if (result instanceof AuthoritativePersistenceResult.Conflict<InformationUnitPersistenceRecord> conflict) {
            return new AuthoritativePersistenceResult.Conflict<>(conflict.conflict());
        }
        if (result instanceof AuthoritativePersistenceResult.Absent<InformationUnitPersistenceRecord> absent) {
            return new AuthoritativePersistenceResult.Absent<>(absent.message());
        }
        if (result instanceof AuthoritativePersistenceResult.OutcomeUnknown<InformationUnitPersistenceRecord> unknown) {
            return new AuthoritativePersistenceResult.OutcomeUnknown<>(unknown.message(), unknown.cause(), unknown.observedAssurance());
        }
        if (result instanceof AuthoritativePersistenceResult.NotCommitted<InformationUnitPersistenceRecord> notCommitted) {
            return new AuthoritativePersistenceResult.NotCommitted<>(notCommitted.failureMessage(), notCommitted.cause());
        }
        return new AuthoritativePersistenceResult.OutcomeUnknown<>("Unrecognized persistence result");
    }
}