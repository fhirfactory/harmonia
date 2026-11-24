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

package net.fhirfactory.harmonia.hestia.mnemosyne.framework;

import net.fhirfactory.harmonia.hapifhir.persistence.model.AuthoritativePersistenceResult;
import net.fhirfactory.harmonia.hapifhir.persistence.model.DurabilityAssurance;
import net.fhirfactory.harmonia.hestia.mnemosyne.nonfhir.InformationUnitPersistenceRecord;
import net.fhirfactory.harmonia.hestia.mnemosyne.nonfhir.NonFhirPersistenceService;
import net.fhirfactory.harmonia.model.core.information.access.AuthoritativeVersion;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GovernedPersistenceFrameworkTest {

    private final InformationUnitPersistenceRecord proposal = new InformationUnitPersistenceRecord(
            UUID.randomUUID(), "harmonia.mneme.iu.test", 0, "{}", "0".repeat(64));

    @Test
    void developmentProfileAcceptsLocalTransactionCommit() {
        NonFhirPersistenceService service = mock(NonFhirPersistenceService.class);
        when(service.create(proposal)).thenReturn(new AuthoritativePersistenceResult.Committed<>(
                proposal.withVersion(1),
                AuthoritativeVersion.of(1),
                DurabilityAssurance.LOCAL_TRANSACTION_COMMITTED));

        AuthoritativePersistenceResult<InformationUnitPersistenceRecord> result =
                new GovernedPersistenceFramework(service, PersistenceDurabilityProfile.DEVELOPMENT_LOCAL_TRANSACTION)
                        .create(proposal);

        assertThat(result).isInstanceOf(AuthoritativePersistenceResult.Committed.class);
        assertThat(((AuthoritativePersistenceResult.Committed<?>) result).durabilityAssurance())
                .isEqualTo(DurabilityAssurance.LOCAL_TRANSACTION_COMMITTED);
    }

    @Test
    void productionProfilePreservesKnownLocalCommitAsIndeterminateForCrossSiteRequirement() {
        NonFhirPersistenceService service = mock(NonFhirPersistenceService.class);
        when(service.create(proposal)).thenReturn(new AuthoritativePersistenceResult.Committed<>(
                proposal.withVersion(1),
                AuthoritativeVersion.of(1),
                DurabilityAssurance.LOCAL_TRANSACTION_COMMITTED));

        AuthoritativePersistenceResult<InformationUnitPersistenceRecord> result =
                new GovernedPersistenceFramework(service, PersistenceDurabilityProfile.PRODUCTION_CROSS_SITE)
                        .create(proposal);

        assertThat(result).isInstanceOf(AuthoritativePersistenceResult.OutcomeUnknown.class);
        assertThat(((AuthoritativePersistenceResult.OutcomeUnknown<?>) result).observedAssurance())
                .isEqualTo(DurabilityAssurance.LOCAL_TRANSACTION_COMMITTED);
    }

    @Test
    void unexpectedPersistenceExceptionIsNotReportedAsSuccess() {
        NonFhirPersistenceService service = mock(NonFhirPersistenceService.class);
        when(service.create(proposal)).thenThrow(new IllegalStateException("unexpected adapter failure"));

        AuthoritativePersistenceResult<InformationUnitPersistenceRecord> result =
                new GovernedPersistenceFramework(service, PersistenceDurabilityProfile.DEVELOPMENT_LOCAL_TRANSACTION)
                        .create(proposal);

        assertThat(result).isInstanceOf(AuthoritativePersistenceResult.OutcomeUnknown.class);
    }
}