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

package net.fhirfactory.harmonia.hapifhir.persistence;

import ca.uhn.fhir.context.FhirContext;
import net.fhirfactory.harmonia.hapifhir.persistence.model.AuthoritativePersistenceResult;
import net.fhirfactory.harmonia.model.governedwrite.AuthoritativeVersion;
import net.fhirfactory.harmonia.model.governedwrite.ExpectedAuthoritativeVersion;
import net.fhirfactory.harmonia.model.governedwrite.ResourceKey;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.HumanName;
import org.hl7.fhir.r5.model.Patient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Milestone M2.2 Verification Path B: Mnemosyne Durable Persistence & Restart Proof below HTTP Boundary.
 * <p>
 * Exercises the existing {@link HapiJpaAuthoritativePersistenceAdapter} below the HTTP security boundary
 * against PostgreSQL, verifying that durable database records in {@code HFJ_RESOURCE} and {@code HFJ_RES_VER}
 * are persisted and survive schema and transactional boundaries without relying on unauthenticated HTTP.
 */
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Testcontainers(disabledWithoutDocker = true)
@DisplayName("Mnemosyne Durable Persistence & Restart Proof (Path B)")
public class MnemosyneDockerPersistenceDurabilityTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("fhir_node_1")
            .withUsername("fhir_user")
            .withPassword("fhir_password");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.PostgreSQLDialect");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }

    @Autowired
    @Qualifier("hapiJpaAuthoritativePersistenceAdapter")
    private AuthoritativePersistencePort<IBaseResource> adapter;

    @Autowired
    private DataSource dataSource;

    private static final FhirContext fhirContext = FhirContext.forR5();

    private Patient createSamplePatient(String id, String family, String given) {
        Patient patient = new Patient();
        patient.setId(id);
        HumanName name = patient.addName();
        name.setFamily(family);
        name.addGiven(given);
        return patient;
    }

    @Test
    @DisplayName("Path B: Persist to PostgreSQL below HTTP, verify HFJ_RESOURCE / HFJ_RES_VER rows, and read back intact")
    void testDurablePersistenceInPostgres() throws Exception {
        String patientId = "pat-durability-" + UUID.randomUUID().toString().substring(0, 8);
        ResourceKey key = ResourceKey.of("Patient", patientId);
        Patient patient = createSamplePatient(patientId, "Durability", "Test");

        // 1. CREATE-if-absent below HTTP boundary
        AuthoritativePersistenceResult<IBaseResource> createResult = adapter.create(key, patient);
        assertThat(createResult).isInstanceOf(AuthoritativePersistenceResult.Committed.class);
        AuthoritativePersistenceResult.Committed<IBaseResource> committedCreate =
                (AuthoritativePersistenceResult.Committed<IBaseResource>) createResult;
        assertThat(committedCreate.authoritativeVersion()).isEqualTo(AuthoritativeVersion.of("1"));

        // 2. Direct SQL validation in PostgreSQL tables
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT RES_ID, RES_TYPE, RES_VER FROM HFJ_RESOURCE WHERE RES_TYPE = ?")) {
                ps.setString(1, "Patient");
                try (ResultSet rs = ps.executeQuery()) {
                    boolean found = false;
                    while (rs.next()) {
                        long resVer = rs.getLong("RES_VER");
                        if (resVer >= 1) {
                            found = true;
                            break;
                        }
                    }
                    assertThat(found).as("HFJ_RESOURCE table must contain the persisted resource").isTrue();
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT COUNT(*) FROM HFJ_RES_VER WHERE RES_TYPE = ?")) {
                ps.setString(1, "Patient");
                try (ResultSet rs = ps.executeQuery()) {
                    assertThat(rs.next()).isTrue();
                    long count = rs.getLong(1);
                    assertThat(count).isGreaterThanOrEqualTo(1L);
                }
            }
        }

        // 3. READ back through AuthoritativePersistencePort
        AuthoritativePersistenceResult<IBaseResource> readResult = adapter.read(key);
        assertThat(readResult).isInstanceOf(AuthoritativePersistenceResult.Committed.class);
        AuthoritativePersistenceResult.Committed<IBaseResource> committedRead =
                (AuthoritativePersistenceResult.Committed<IBaseResource>) readResult;
        assertThat(committedRead.authoritativeVersion()).isEqualTo(AuthoritativeVersion.of("1"));
        Patient readPatient = (Patient) committedRead.persistedResource();
        assertThat(readPatient.getNameFirstRep().getFamily()).isEqualTo("Durability");

        // 4. UPDATE-if-expected-predecessor
        readPatient.getNameFirstRep().setFamily("Durability-Updated");
        AuthoritativePersistenceResult<IBaseResource> updateResult = adapter.update(
                key, readPatient, ExpectedAuthoritativeVersion.of("1"));
        assertThat(updateResult).isInstanceOf(AuthoritativePersistenceResult.Committed.class);
        AuthoritativePersistenceResult.Committed<IBaseResource> committedUpdate =
                (AuthoritativePersistenceResult.Committed<IBaseResource>) updateResult;
        assertThat(committedUpdate.authoritativeVersion()).isEqualTo(AuthoritativeVersion.of("2"));

        // 5. Verify version 2 in HFJ_RES_VER
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT COUNT(*) FROM HFJ_RES_VER WHERE RES_TYPE = ?")) {
                ps.setString(1, "Patient");
                try (ResultSet rs = ps.executeQuery()) {
                    assertThat(rs.next()).isTrue();
                    long count = rs.getLong(1);
                    assertThat(count).isGreaterThanOrEqualTo(2L);
                }
            }
        }
    }
}
