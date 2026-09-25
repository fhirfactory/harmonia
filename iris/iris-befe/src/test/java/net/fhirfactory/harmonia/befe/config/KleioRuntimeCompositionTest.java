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

package net.fhirfactory.harmonia.befe.config;

import ca.uhn.fhir.context.FhirContext;
import jakarta.annotation.Resource;
import jakarta.annotation.sql.DataSourceDefinition;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import jakarta.enterprise.util.AnnotationLiteral;
import net.fhirfactory.harmonia.kleio.audit.model.AuditAction;
import net.fhirfactory.harmonia.kleio.audit.model.AuditClassification;
import net.fhirfactory.harmonia.kleio.audit.model.AuditOutcome;
import net.fhirfactory.harmonia.kleio.audit.model.AuditTarget;
import net.fhirfactory.harmonia.kleio.audit.model.HarmoniaAuditEvent;
import net.fhirfactory.harmonia.kleio.audit.service.AuditIntegrityException;
import net.fhirfactory.harmonia.kleio.audit.service.AuditService;
import net.fhirfactory.harmonia.kleio.audit.service.InMemoryAuditService;
import net.fhirfactory.harmonia.kleio.fhir.mapper.HarmoniaAuditEventMapper;
import net.fhirfactory.harmonia.kleio.persistence.cdi.FhirContextProducer;
import net.fhirfactory.harmonia.kleio.persistence.qualifier.KleioAudit;
import net.fhirfactory.harmonia.kleio.persistence.repository.AppendOnlyAuditEventRepository;
import net.fhirfactory.harmonia.kleio.persistence.repository.JdbcAppendOnlyAuditEventRepository;
import net.fhirfactory.harmonia.kleio.persistence.service.DurableAuditService;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Verifies that the durable Kleio audit persistence subsystem composes unambiguously
 * into the Iris BEFE runtime container as a CDI service graph.
 */
@DisplayName("Kleio Runtime Composition and CDI Resolution Test")
public class KleioRuntimeCompositionTest {

    public static class KleioAuditLiteral extends AnnotationLiteral<KleioAudit> implements KleioAudit {}

    @ApplicationScoped
    public static class TestDataSourceProducer {
        private static DataSource testDs;

        public static void setDataSource(DataSource ds) {
            testDs = ds;
        }

        @Produces
        @KleioAudit
        @ApplicationScoped
        public DataSource produceDataSource() {
            return testDs;
        }
    }

    private SeContainer container;
    private DataSource dataSource;

    @BeforeEach
    void setUp() throws SQLException {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:kleio_composition_" + UUID.randomUUID().toString().replace("-", "") +
                ";MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH;DB_CLOSE_DELAY=-1");
        ds.setUser("sa");
        ds.setPassword("");
        this.dataSource = ds;

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("""
                CREATE TABLE hie_fhir_resources (
                    id BIGSERIAL PRIMARY KEY,
                    resource_type VARCHAR(64) NOT NULL,
                    fhir_id VARCHAR(128) NOT NULL,
                    version_id BIGINT NOT NULL DEFAULT 1,
                    resource_json TEXT NOT NULL,
                    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
                    last_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                    CONSTRAINT uk_resource_type_fhir_id UNIQUE (resource_type, fhir_id)
                )
            """);
        }

        TestDataSourceProducer.setDataSource(dataSource);

        container = SeContainerInitializer.newInstance()
                .disableDiscovery()
                .addBeanClasses(
                        DurableAuditService.class,
                        JdbcAppendOnlyAuditEventRepository.class,
                        FhirContextProducer.class,
                        TestDataSourceProducer.class
                )
                .initialize();
    }

    @AfterEach
    void tearDown() {
        if (container != null && container.isRunning()) {
            container.close();
        }
    }

    @Test
    @DisplayName("Scenario 1: CDI Graph Verification & Unambiguous Bean Resolution")
    void testCdiBeanResolution() throws Exception {
        // AuditService resolves unambiguously to DurableAuditService
        AuditService auditService = container.select(AuditService.class).get();
        assertThat(auditService)
                .isNotNull()
                .isInstanceOf(DurableAuditService.class);

        // AppendOnlyAuditEventRepository resolves unambiguously to JdbcAppendOnlyAuditEventRepository
        AppendOnlyAuditEventRepository repository = container.select(AppendOnlyAuditEventRepository.class).get();
        assertThat(repository)
                .isNotNull()
                .isInstanceOf(JdbcAppendOnlyAuditEventRepository.class);

        // @KleioAudit DataSource resolves
        DataSource resolvedDs = container.select(DataSource.class, new KleioAuditLiteral()).get();
        assertThat(resolvedDs).isNotNull();
        try (Connection conn = resolvedDs.getConnection()) {
            assertThat(conn).isNotNull();
            assertThat(conn.isValid(1)).isTrue();
        }

        // HarmoniaAuditEventMapper resolves
        HarmoniaAuditEventMapper mapper = container.select(HarmoniaAuditEventMapper.class).get();
        assertThat(mapper).isNotNull();

        // FhirContext resolves
        FhirContext fhirContext = container.select(FhirContext.class).get();
        assertThat(fhirContext).isNotNull();

        // InMemoryAuditService is NOT registered as a production CDI bean
        assertThat(container.select(InMemoryAuditService.class).isUnsatisfied()).isTrue();

        // AuditService resolution is not ambiguous
        assertThat(container.select(AuditService.class).isAmbiguous()).isFalse();
    }

    @Test
    @DisplayName("Scenario 2: Immutable Append-Only Audit Evidence Execution via Composed Service")
    void testAuditServiceAppendAndFindThroughCdi() {
        AuditService auditService = container.select(AuditService.class).get();
        assertThat(auditService).isNotNull();

        String eventId = "EVT-COMPOSITION-" + UUID.randomUUID();
        HarmoniaAuditEvent event = HarmoniaAuditEvent.builder()
                .eventId(eventId)
                .recordedAt(Instant.parse("2026-09-24T12:00:00Z"))
                .classification(AuditClassification.CLINICAL)
                .action(AuditAction.READ)
                .outcome(AuditOutcome.SUCCESS)
                .initiatingPrincipal(ThemisPrincipal.human("dr-composer"))
                .securityDomain("CLINICAL")
                .correlationId("corr-" + eventId)
                .operationId("op-" + eventId)
                .build();

        // 1. Append event
        auditService.append(event);

        // 2. Point read findById recovers exact event canonically
        Optional<HarmoniaAuditEvent> found = auditService.findById(eventId);
        assertThat(found).isPresent().contains(event);

        // 3. Idempotent append of identical event succeeds
        auditService.append(event);

        // 4. Divergent payload with identical ID throws AuditIntegrityException
        HarmoniaAuditEvent divergentEvent = HarmoniaAuditEvent.builder()
                .eventId(eventId)
                .recordedAt(Instant.parse("2026-09-24T12:00:00Z"))
                .classification(AuditClassification.SECURITY)
                .action(AuditAction.AUTHORIZE)
                .outcome(AuditOutcome.FAILURE)
                .initiatingPrincipal(ThemisPrincipal.human("attacker"))
                .securityDomain("CLINICAL")
                .correlationId("corr-" + eventId)
                .operationId("op-divergent-" + eventId)
                .build();

        assertThatThrownBy(() -> auditService.append(divergentEvent))
                .isInstanceOf(AuditIntegrityException.class)
                .hasMessageContaining(eventId);
    }

    @Test
    @DisplayName("AuditDataSourceProducer declaration and annotations validation")
    void testAuditDataSourceProducerAnnotations() throws NoSuchFieldException, NoSuchMethodException {
        Class<AuditDataSourceProducer> clazz = AuditDataSourceProducer.class;
        assertThat(clazz.isAnnotationPresent(ApplicationScoped.class)).isTrue();
        assertThat(clazz.isAnnotationPresent(DataSourceDefinition.class)).isTrue();

        DataSourceDefinition dsDef = clazz.getAnnotation(DataSourceDefinition.class);
        assertThat(dsDef.name()).isEqualTo("java:jboss/datasources/KleioAuditDS");
        assertThat(dsDef.className()).isEqualTo("org.postgresql.ds.PGSimpleDataSource");
        assertThat(dsDef.url()).contains("jdbc:postgresql://");

        java.lang.reflect.Field dsField = clazz.getDeclaredField("dataSource");
        assertThat(dsField.isAnnotationPresent(Resource.class)).isTrue();
        Resource resource = dsField.getAnnotation(Resource.class);
        assertThat(resource.lookup()).isEqualTo("java:jboss/datasources/KleioAuditDS");

        java.lang.reflect.Method produceMethod = clazz.getDeclaredMethod("produceAuditDataSource");
        assertThat(produceMethod.isAnnotationPresent(Produces.class)).isTrue();
        assertThat(produceMethod.isAnnotationPresent(KleioAudit.class)).isTrue();
        assertThat(produceMethod.isAnnotationPresent(ApplicationScoped.class)).isTrue();
    }
}
