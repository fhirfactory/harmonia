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

package net.fhirfactory.harmonia.hapifhir.persistence.model;

import net.fhirfactory.harmonia.hapifhir.persistence.model.AuthoritativePersistenceResult;
import net.fhirfactory.harmonia.model.core.information.access.AuthoritativeVersion;
import net.fhirfactory.harmonia.hapifhir.persistence.model.DurabilityAssurance;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DurabilityAssuranceTest {

    @Test
    void assuranceOrderingRequiresExplicitlyStrongerEvidence() {
        assertThat(DurabilityAssurance.CACHE_ACCEPTED.satisfies(DurabilityAssurance.LOCAL_TRANSACTION_COMMITTED)).isFalse();
        assertThat(DurabilityAssurance.LOCAL_TRANSACTION_COMMITTED.satisfies(DurabilityAssurance.LOCAL_TRANSACTION_COMMITTED)).isTrue();
        assertThat(DurabilityAssurance.LOCAL_TRANSACTION_COMMITTED.satisfies(DurabilityAssurance.CROSS_SITE_CONFIRMED)).isFalse();
    }

    @Test
    void legacyCommittedConstructorRemainsUnqualified() {
        AuthoritativePersistenceResult.Committed<String> committed =
                new AuthoritativePersistenceResult.Committed<>("value", AuthoritativeVersion.of(1));

        assertThat(committed.durabilityAssurance()).isEqualTo(DurabilityAssurance.UNQUALIFIED);
    }
}