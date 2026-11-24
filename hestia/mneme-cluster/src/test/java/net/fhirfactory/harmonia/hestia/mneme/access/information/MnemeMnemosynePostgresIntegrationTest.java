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

import com.fasterxml.jackson.databind.ObjectMapper;
import net.fhirfactory.harmonia.hapifhir.persistence.model.AuthoritativePersistenceResult;
import net.fhirfactory.harmonia.model.core.information.access.AuthoritativeVersion;
import net.fhirfactory.harmonia.hapifhir.persistence.model.DurabilityAssurance;
import net.fhirfactory.harmonia.hestia.mnemosyne.api.OperationalStatusKey;
import net.fhirfactory.harmonia.hestia.mnemosyne.api.OperationalStatusRecord;
import net.fhirfactory.harmonia.hestia.mnemosyne.controller.InformationUnitController;
import net.fhirfactory.harmonia.hestia.mnemosyne.controller.OperationalStatusController;
import net.fhirfactory.harmonia.hestia.mnemosyne.framework.GovernedPersistenceFramework;
import net.fhirfactory.harmonia.hestia.mnemosyne.framework.PersistenceDurabilityProfile;
import net.fhirfactory.harmonia.hestia.mnemosyne.governance.MnemosyneAuthorizationGate;
import net.fhirfactory.harmonia.hestia.mnemosyne.information.InformationUnitConstructor;
import net.fhirfactory.harmonia.hestia.mnemosyne.information.InformationUnitDisassembler;
import net.fhirfactory.harmonia.hestia.mnemosyne.nonfhir.NonFhirSchemaInitializer;
import net.fhirfactory.harmonia.hestia.mnemosyne.nonfhir.PostgresNonFhirPersistenceService;
import net.fhirfactory.harmonia.model.core.information.access.ActiveStateCoordinationResult;
import net.fhirfactory.harmonia.model.core.information.access.ActiveStateCoordinator;
import net.fhirfactory.harmonia.model.core.information.access.ActiveStateToken;
import net.fhirfactory.harmonia.model.core.information.access.ActiveStateTokenBridge;
import net.fhirfactory.harmonia.model.core.information.access.AuthoritativePreconditionConflict;
import net.fhirfactory.harmonia.model.core.information.access.ExpectedAuthoritativeVersion;
import net.fhirfactory.harmonia.model.core.information.access.GovernedRead;
import net.fhirfactory.harmonia.model.core.information.access.ResourceKey;
import net.fhirfactory.harmonia.model.core.information.access.WriteResult;
import net.fhirfactory.harmonia.model.core.information.entity.InformationMetadata;
import net.fhirfactory.harmonia.model.core.information.entity.InformationUnit;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.Content;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.ContentMetadata;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.ContainerMetadata;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationRepresentation;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationUnitIdentifier;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationUnitRelationship;
import net.fhirfactory.harmonia.model.core.information.entity.valuesets.InformationUnitValueSets;
import net.fhirfactory.harmonia.model.core.naming.factory.TopicFactory;
import net.fhirfactory.harmonia.model.core.information.operations.ManagementContext;
import net.fhirfactory.harmonia.themis.api.ThemisAuthorizer;
import net.fhirfactory.harmonia.themis.api.model.ThemisAction;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationDecision;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationRequest;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;
import net.fhirfactory.harmonia.themis.api.policy.ThemisPolicy;
import net.fhirfactory.harmonia.themis.core.evaluator.DeterministicPolicyEvaluator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.postgresql.ds.PGSimpleDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * End-to-end V001 proof using an ephemeral PostgreSQL database and test-scoped Themis policy.
 * The in-memory coordinator below is a test fixture only, not a production fallback.
 */
@Testcontainers(disabledWithoutDocker = true)
class MnemeMnemosynePostgresIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("harmonia_mneme_v001")
            .withUsername("harmonia_test")
            .withPassword("harmonia_test");

    private static DataSource dataSource;

    @BeforeAll
    static void initializeDatabase() throws SQLException {
        PGSimpleDataSource postgresDataSource = new PGSimpleDataSource();
        postgresDataSource.setURL(POSTGRES.getJdbcUrl());
        postgresDataSource.setUser(POSTGRES.getUsername());
        postgresDataSource.setPassword(POSTGRES.getPassword());
        dataSource = postgresDataSource;
        new NonFhirSchemaInitializer().initializeDevelopmentSchema(dataSource);
    }

    @BeforeEach
    void clearTestRows() throws SQLException {
        try (Connection connection = dataSource.getConnection(); var statement = connection.createStatement()) {
            statement.execute("TRUNCATE TABLE mnemosyne_operational_status_versions, mnemosyne_iu_versions, mnemosyne_iu_identity");
        }
    }

    @Test
    void governedWriterReaderPreserveContentVersionHistoryAndServiceRestart() throws SQLException {
        DeterministicPolicyEvaluator authorizer = authorisedTestEvaluator();
        RuntimeStack stack = runtimeStack(authorizer, new TestActiveStateCoordinator());
        InformationUnit original = informationUnit("first representation");
        ResourceKey key = resourceKey(original);

        WriteResult<InformationUnit> created = stack.writer().create(key, original, securityContext());

        assertThat(created).isInstanceOf(WriteResult.Committed.class);
        WriteResult.Committed<InformationUnit> committedCreate = (WriteResult.Committed<InformationUnit>) created;
        assertThat(committedCreate.version()).isEqualTo(AuthoritativeVersion.of(1));
        assertThat(committedCreate.convergenceStatus().name()).isEqualTo("DEGRADED");
        assertThat(committedCreate.degradationReason())
                .hasValueSatisfying(reason -> assertThat(reason).contains("production cross-site durability"));
        assertThat(committedCreate.resource().version()).isEqualTo("1");

        WriteResult<InformationUnit> replay = stack.writer().create(key, original, securityContext());
        assertThat(replay.committedVersion()).contains(AuthoritativeVersion.of(1));
        assertThat(count("SELECT COUNT(*) FROM mnemosyne_iu_versions WHERE iu_uuid = ?", original.identifier().uuid().toString()))
                .isEqualTo(1);

        Optional<GovernedRead<InformationUnit>> read = stack.reader().read(key, securityContext());
        assertThat(read).isPresent();
        InformationUnit retrieved = read.orElseThrow().resource();
        assertThat(retrieved.identifier()).isEqualTo(original.identifier());
        assertThat(retrieved.metadata()).isEqualTo(original.metadata());
        assertThat(retrieved.metadata().relationships()).containsExactlyElementsOf(original.metadata().relationships());
        assertThat(retrieved.representation()).isEqualTo(original.representation());
        assertThat(retrieved.version()).isEqualTo("1");
        assertThat(retrieved.getManagementContext()).isNull();

        InformationUnit updated = original.withRepresentation(new InformationRepresentation(
                original.representation().containerMetadata(),
                original.representation().contentMetadata(),
                Content.text("second representation")));
        WriteResult<InformationUnit> update = stack.writer().update(read.orElseThrow(), updated, securityContext());
        assertThat(update).isInstanceOf(WriteResult.Committed.class);
        assertThat(update.committedVersion()).contains(AuthoritativeVersion.of(2));

        AuthoritativePersistenceResult<InformationUnit> staleUpdate = stack.mnemosyne().update(
                key,
                updated,
                ExpectedAuthoritativeVersion.of(1),
                securityContext());
        assertThat(staleUpdate).isInstanceOf(AuthoritativePersistenceResult.Conflict.class);
        assertThat(((AuthoritativePersistenceResult.Conflict<InformationUnit>) staleUpdate).conflict().reason())
                .isEqualTo(AuthoritativePreconditionConflict.expectedVersionMismatch(
                        key,
                        ExpectedAuthoritativeVersion.of(1),
                        AuthoritativeVersion.of(2)).reason());

        RuntimeStack restartedStack = runtimeStack(authorizer, new TestActiveStateCoordinator());
        Optional<GovernedRead<InformationUnit>> afterRestartRead = restartedStack.reader().read(key, securityContext());
        InformationUnit afterRestart = afterRestartRead.orElseThrow().resource();
        assertThat(afterRestart.version()).isEqualTo("2");
        assertThat(afterRestart.representation().content().textContent()).contains("second representation");
        assertThat(count("SELECT COUNT(*) FROM mnemosyne_iu_versions WHERE iu_uuid = ?", original.identifier().uuid().toString()))
                .isEqualTo(2);
    }

    @Test
    void ownerDefinedStatusRoundTripsWithoutCreatingAnInformationUnit() throws SQLException {
        RuntimeStack stack = runtimeStack(authorisedTestEvaluator(), new TestActiveStateCoordinator());
        OperationalStatusKey key = new OperationalStatusKey("workflow-owner", "subject-opaque-01", "execution-state");

        AuthoritativePersistenceResult<OperationalStatusRecord> created = stack.statusAccess().record(
                key,
                "ready",
                ExpectedAuthoritativeVersion.none(),
                securityContext());
        assertThat(created).isInstanceOf(AuthoritativePersistenceResult.Committed.class);
        assertThat(((AuthoritativePersistenceResult.Committed<OperationalStatusRecord>) created).durabilityAssurance())
                .isEqualTo(DurabilityAssurance.LOCAL_TRANSACTION_COMMITTED);

        AuthoritativePersistenceResult<OperationalStatusRecord> retrieved = stack.statusAccess().read(key, securityContext());
        assertThat(retrieved).isInstanceOf(AuthoritativePersistenceResult.Committed.class);
        OperationalStatusRecord status = ((AuthoritativePersistenceResult.Committed<OperationalStatusRecord>) retrieved)
                .persistedResource();
        assertThat(status.key()).isEqualTo(key);
        assertThat(status.payload()).isEqualTo("ready");
        assertThat(status.version()).isEqualTo(AuthoritativeVersion.of(1));

        AuthoritativePersistenceResult<OperationalStatusRecord> updated = stack.statusAccess().record(
                key,
                "complete",
                ExpectedAuthoritativeVersion.of(1),
                securityContext());
        assertThat(updated).isInstanceOf(AuthoritativePersistenceResult.Committed.class);
        assertThat(((AuthoritativePersistenceResult.Committed<OperationalStatusRecord>) updated).authoritativeVersion())
                .isEqualTo(AuthoritativeVersion.of(2));
        assertThat(count("SELECT COUNT(*) FROM mnemosyne_iu_identity", null)).isZero();
    }

    @Test
    void missingThemisPolicyDeniesAtMnemeAndMnemosyneBoundaries() throws SQLException {
        DeterministicPolicyEvaluator defaultDeny = new DeterministicPolicyEvaluator();
        RuntimeStack stack = runtimeStack(defaultDeny, new TestActiveStateCoordinator());
        InformationUnit unit = informationUnit("must not persist");
        ResourceKey key = resourceKey(unit);

        WriteResult<InformationUnit> mnemeResult = stack.writer().create(key, unit, securityContext());
        assertThat(mnemeResult).isInstanceOf(WriteResult.NotCommitted.class);
        assertThat(count("SELECT COUNT(*) FROM mnemosyne_iu_identity", null)).isZero();

        assertThatThrownBy(() -> stack.mnemosyne().create(key, unit, securityContext()))
                .isInstanceOf(SecurityException.class);
        assertThat(count("SELECT COUNT(*) FROM mnemosyne_iu_identity", null)).isZero();
    }

    @Test
    void databaseConnectionFailureDoesNotBecomeSuccessfulWrite() {
        PGSimpleDataSource unavailable = new PGSimpleDataSource();
        unavailable.setURL("jdbc:postgresql://127.0.0.1:1/unavailable?connectTimeout=1");
        unavailable.setUser("unavailable");
        unavailable.setPassword("unavailable");
        PostgresNonFhirPersistenceService service = new PostgresNonFhirPersistenceService(unavailable);
        InformationUnit proposal = informationUnit("unavailable database");
        InformationUnitDisassembler disassembler = new InformationUnitDisassembler(new ObjectMapper());

        AuthoritativePersistenceResult<?> result = service.create(disassembler.disassemble(proposal));

        assertThat(result).isInstanceOf(AuthoritativePersistenceResult.NotCommitted.class);
        assertThat(service.read(proposal.identifier().uuid())).isInstanceOf(AuthoritativePersistenceResult.NotCommitted.class);
    }

    private RuntimeStack runtimeStack(ThemisAuthorizer authorizer, ActiveStateCoordinator coordinator) {
        PostgresNonFhirPersistenceService service = new PostgresNonFhirPersistenceService(dataSource);
        GovernedPersistenceFramework framework = new GovernedPersistenceFramework(
                service,
                PersistenceDurabilityProfile.DEVELOPMENT_LOCAL_TRANSACTION);
        InformationUnitDisassembler disassembler = new InformationUnitDisassembler(new ObjectMapper());
        InformationUnitController informationUnitController = new InformationUnitController(
                new MnemosyneAuthorizationGate(authorizer),
                framework,
                disassembler,
                new InformationUnitConstructor(new ObjectMapper()));
        OperationalStatusController statusController = new OperationalStatusController(
                new MnemosyneAuthorizationGate(authorizer), framework);
        return new RuntimeStack(
                informationUnitController,
                new InformationUnitGovernedWriter(authorizer, coordinator, informationUnitController),
                new InformationUnitGovernedReader(authorizer, coordinator, informationUnitController),
                new OperationalStatusGovernedAccess(authorizer, statusController));
    }

    private DeterministicPolicyEvaluator authorisedTestEvaluator() {
        DeterministicPolicyEvaluator evaluator = new DeterministicPolicyEvaluator();
        evaluator.registerPolicy(new V001TestPolicy());
        return evaluator;
    }

    private InformationUnit informationUnit(String payload) {
        InformationUnitIdentifier identifier = InformationUnitIdentifier.random();
        InformationMetadata metadata = InformationMetadata.builder()
                .origin(TopicFactory.fromName("Hospital.Site.Canberra"))
                .relationship(new InformationUnitRelationship(
                        InformationUnitValueSets.TYPE_RELATES_TO,
                        "related-information-unit"))
                .build();
        ContentMetadata contentMetadata = ContentMetadata.builder()
                .contentStructureTopic(TopicFactory.fromName("Health.FHIR.R5.Patient"))
                .contentStructureVersion("R5")
                .build();
        InformationRepresentation representation = new InformationRepresentation(
                ContainerMetadata.empty(),
                contentMetadata,
                Content.text(payload));
        return new InformationUnit(identifier, "caller-version", metadata, ManagementContext.empty(), representation);
    }

    private ResourceKey resourceKey(InformationUnit informationUnit) {
        return ResourceKey.of("InformationUnit", informationUnit.identifier().uuid().toString());
    }

    private ThemisSecurityContext securityContext() {
        return ThemisSecurityContext.builder()
                .requestingPrincipal(ThemisPrincipal.human("v001-test"))
                .correlationId("v001-test-correlation")
                .tenantId("HarmoniaTest")
                .build();
    }

    private long count(String sql, String uuid) throws SQLException {
        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            if (uuid != null) {
                statement.setObject(1, java.util.UUID.fromString(uuid));
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getLong(1);
            }
        }
    }

    private record RuntimeStack(
            InformationUnitController mnemosyne,
            InformationUnitGovernedWriter writer,
            InformationUnitGovernedReader reader,
            OperationalStatusGovernedAccess statusAccess) {
    }

    private static final class V001TestPolicy implements ThemisPolicy {
        @Override
        public String getPolicyId() {
            return "v001-test-policy";
        }

        @Override
        public String getDescription() {
            return "Test-only policy for the bounded Mneme/Mnemosyne V001 integration fixture";
        }

        @Override
        public boolean appliesTo(ThemisAuthorizationRequest request) {
            return request.principal() != null
                    && "v001-test".equals(request.principal().principalId())
                    && request.target() != null
                    && ("InformationUnit".equals(request.target().resourceType())
                    || "OperationalStatus".equals(request.target().resourceType()))
                    && (request.action() == ThemisAction.CREATE
                    || request.action() == ThemisAction.READ
                    || request.action() == ThemisAction.UPDATE);
        }

        @Override
        public ThemisAuthorizationDecision evaluate(ThemisAuthorizationRequest request) {
            return ThemisAuthorizationDecision.allow(getPolicyId(), request.context().correlationId());
        }
    }

    private static final class TestActiveStateCoordinator implements ActiveStateCoordinator {
        private final Map<ResourceKey, ActiveStateToken> tokens = new HashMap<>();

        @Override
        public synchronized ActiveStateToken observe(ResourceKey key) {
            return tokens.computeIfAbsent(key, ignored -> ActiveStateTokenBridge.create(0));
        }

        @Override
        public synchronized ActiveStateCoordinationResult consume(ResourceKey key, ActiveStateToken observedToken) {
            ActiveStateToken current = tokens.computeIfAbsent(key, ignored -> ActiveStateTokenBridge.create(0));
            if (!current.equals(observedToken)) {
                return ActiveStateCoordinationResult.STALE;
            }
            tokens.put(key, ActiveStateTokenBridge.create(ActiveStateTokenBridge.extractVersion(current) + 1));
            return ActiveStateCoordinationResult.CONSUMED;
        }
    }
}