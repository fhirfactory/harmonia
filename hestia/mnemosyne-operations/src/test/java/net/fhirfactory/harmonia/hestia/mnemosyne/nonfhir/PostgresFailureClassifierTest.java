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

package net.fhirfactory.harmonia.hestia.mnemosyne.nonfhir;

import net.fhirfactory.harmonia.hapifhir.persistence.model.AuthoritativePersistenceResult;
import net.fhirfactory.harmonia.model.core.information.access.AuthoritativeVersion;
import net.fhirfactory.harmonia.model.core.information.access.ExpectedAuthoritativeVersion;
import net.fhirfactory.harmonia.model.core.information.access.ResourceKey;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

class PostgresFailureClassifierTest {

    private final PostgresFailureClassifier classifier = new PostgresFailureClassifier();
    private final ResourceKey key = ResourceKey.of("InformationUnit", "d6dbd0c3-7db4-4c8f-a5e4-5856a4c266d0");

    @Test
    void uniqueViolationIsTypedConflict() {
        AuthoritativePersistenceResult<String> result = classifier.classify(
                new SQLException("duplicate", "23505"),
                key,
                ExpectedAuthoritativeVersion.none(),
                AuthoritativeVersion.of(1),
                false);

        assertThat(result).isInstanceOf(AuthoritativePersistenceResult.Conflict.class);
    }

    @Test
    void connectionLossDuringCommitIsIndeterminate() {
        AuthoritativePersistenceResult<String> result = classifier.classify(
                new SQLException("connection lost", "08006"),
                key,
                ExpectedAuthoritativeVersion.none(),
                null,
                true);

        assertThat(result).isInstanceOf(AuthoritativePersistenceResult.OutcomeUnknown.class);
    }

    @Test
    void connectionFailureBeforeCommitIsNotCommitted() {
        AuthoritativePersistenceResult<String> result = classifier.classify(
                new SQLException("connection refused", "08001"),
                key,
                ExpectedAuthoritativeVersion.none(),
                null,
                false);

        assertThat(result).isInstanceOf(AuthoritativePersistenceResult.NotCommitted.class);
    }
}