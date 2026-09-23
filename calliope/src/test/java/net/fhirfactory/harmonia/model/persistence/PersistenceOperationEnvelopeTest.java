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

package net.fhirfactory.harmonia.model.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.fhirfactory.harmonia.themis.api.model.PrincipalType;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthority;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationDecision;
import net.fhirfactory.harmonia.themis.api.model.ThemisDecisionReason;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PersistenceOperationEnvelopeTest {

    private ThemisPrincipal humanRequester;
    private ThemisPrincipal irisBefeExecutor;
    private ThemisPrincipal mnemeExecutor;
    private ThemisPrincipal mnemosyneExecutor;
    private Set<ThemisAuthority> clinicalAuthorities;
    private ThemisAuthorizationDecision allowedDecision;
    private ThemisAuthorizationDecision deniedDecision;
    private Instant now;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        humanRequester = ThemisPrincipal.of("human:dr-watson", PrincipalType.HUMAN, "clinical-user");
        irisBefeExecutor = ThemisPrincipal.of("service:iris-befe", PrincipalType.SERVICE, "befe-gateway");
        mnemeExecutor = ThemisPrincipal.of("service:mneme", PrincipalType.SERVICE, "mneme-cache");
        mnemosyneExecutor = ThemisPrincipal.of("service:mnemosyne", PrincipalType.SERVICE, "mnemosyne-store");

        clinicalAuthorities = Set.of(
                ThemisAuthority.of("clinical.read"),
                ThemisAuthority.of("clinical.create"),
                ThemisAuthority.of("clinical.update"),
                ThemisAuthority.of("clinical.delete")
        );

        now = Instant.now().truncatedTo(ChronoUnit.MILLIS);
        allowedDecision = ThemisAuthorizationDecision.allow("policy-clinical-write-1.0", "corr-123", "Authorized write");
        deniedDecision = ThemisAuthorizationDecision.deny(ThemisDecisionReason.AUTHORITY_MISSING, "policy-clinical-write-1.0", "corr-123", "Unauthorized");

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testCreateOperationEnvelopeValid() {
        String payload = "{\"resourceType\":\"Person\",\"id\":\"p-123\",\"name\":[{\"family\":\"Watson\"}]}";

        PersistenceOperationEnvelope<String> envelope = PersistenceOperationEnvelope.<String>builder()
                .operationId("op-001")
                .operationType(PersistenceOperationType.CREATE)
                .resourceType("Person")
                .resourceId("p-123")
                .payload(payload)
                .originatingPrincipal(humanRequester)
                .originatingAuthorities(clinicalAuthorities)
                .securityDomain("CLINICAL")
                .correlationId("corr-123")
                .causationId("caus-123")
                .requestedAt(now)
                .authorizationDecision(allowedDecision)
                .executingPrincipal(irisBefeExecutor)
                .createdAt(now)
                .attribute("channel", "REST")
                .build();

        assertThat(envelope.getOperationId()).isEqualTo("op-001");
        assertThat(envelope.getOperationType()).isEqualTo(PersistenceOperationType.CREATE);
        assertThat(envelope.getResourceType()).isEqualTo("Person");
        assertThat(envelope.getResourceId()).isEqualTo("p-123");
        assertThat(envelope.getExpectedVersion()).isNull();
        assertThat(envelope.getPayload()).isEqualTo(payload);
        assertThat(envelope.getOriginatingPrincipal()).isEqualTo(humanRequester);
        assertThat(envelope.getOriginatingAuthorities()).containsExactlyInAnyOrderElementsOf(clinicalAuthorities);
        assertThat(envelope.getSecurityDomain()).isEqualTo("CLINICAL");
        assertThat(envelope.getCorrelationId()).isEqualTo("corr-123");
        assertThat(envelope.getCausationId()).isEqualTo("caus-123");
        assertThat(envelope.getRequestedAt()).isEqualTo(now);
        assertThat(envelope.getAuthorizationDecision()).isEqualTo(allowedDecision);
        assertThat(envelope.getExecutingPrincipal()).isEqualTo(irisBefeExecutor);
        assertThat(envelope.getCreatedAt()).isEqualTo(now);
        assertThat(envelope.getAttributes()).containsEntry("channel", "REST");

        // Verify immutability of collections
        assertThatThrownBy(() -> envelope.getOriginatingAuthorities().add(ThemisAuthority.of("admin")))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> envelope.getAttributes().put("tamper", "bad"))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void testUpdateOperationEnvelopeValid() {
        String payload = "{\"resourceType\":\"Consent\",\"id\":\"c-456\",\"status\":\"active\"}";

        PersistenceOperationEnvelope<String> envelope = PersistenceOperationEnvelope.<String>builder()
                .operationId("op-002")
                .operationType(PersistenceOperationType.UPDATE)
                .resourceType("Consent")
                .resourceId("c-456")
                .expectedVersion(2L)
                .payload(payload)
                .originatingPrincipal(humanRequester)
                .originatingAuthorities(clinicalAuthorities)
                .securityDomain("CLINICAL")
                .correlationId("corr-456")
                .causationId("caus-456")
                .requestedAt(now)
                .authorizationDecision(allowedDecision)
                .executingPrincipal(irisBefeExecutor)
                .build();

        assertThat(envelope.getOperationType()).isEqualTo(PersistenceOperationType.UPDATE);
        assertThat(envelope.getExpectedVersion()).isEqualTo(2L);
        assertThat(envelope.getPayload()).isEqualTo(payload);
    }

    @Test
    void testDeleteOperationEnvelopeValidWithNullPayload() {
        PersistenceOperationEnvelope<String> envelope = PersistenceOperationEnvelope.<String>builder()
                .operationId("op-003")
                .operationType(PersistenceOperationType.DELETE)
                .resourceType("Person")
                .resourceId("p-123")
                .expectedVersion(1L)
                .payload(null)
                .originatingPrincipal(humanRequester)
                .originatingAuthorities(clinicalAuthorities)
                .securityDomain("CLINICAL")
                .correlationId("corr-del-1")
                .causationId("caus-del-1")
                .requestedAt(now)
                .authorizationDecision(allowedDecision)
                .executingPrincipal(irisBefeExecutor)
                .build();

        assertThat(envelope.getOperationType()).isEqualTo(PersistenceOperationType.DELETE);
        assertThat(envelope.getPayload()).isNull();
        assertThat(envelope.getOriginatingPrincipal()).isEqualTo(humanRequester);
        assertThat(envelope.getCorrelationId()).isEqualTo("corr-del-1");
        assertThat(envelope.getAuthorizationDecision().isAllowed()).isTrue();
    }

    @Test
    void testFailClosedValidationMissingMandatoryFields() {
        // Missing operationId
        assertThatThrownBy(() -> PersistenceOperationEnvelope.<String>builder()
                .operationType(PersistenceOperationType.CREATE)
                .resourceType("Person")
                .resourceId("p-1")
                .payload("{}")
                .originatingPrincipal(humanRequester)
                .securityDomain("CLINICAL")
                .correlationId("c-1")
                .causationId("caus-1")
                .requestedAt(now)
                .authorizationDecision(allowedDecision)
                .executingPrincipal(irisBefeExecutor)
                .build())
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("operationId");

        // Blank operationId
        assertThatThrownBy(() -> PersistenceOperationEnvelope.<String>builder()
                .operationId("   ")
                .operationType(PersistenceOperationType.CREATE)
                .resourceType("Person")
                .resourceId("p-1")
                .payload("{}")
                .originatingPrincipal(humanRequester)
                .securityDomain("CLINICAL")
                .correlationId("c-1")
                .causationId("caus-1")
                .requestedAt(now)
                .authorizationDecision(allowedDecision)
                .executingPrincipal(irisBefeExecutor)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("operationId");

        // Missing operationType
        assertThatThrownBy(() -> PersistenceOperationEnvelope.<String>builder()
                .operationId("op-1")
                .resourceType("Person")
                .resourceId("p-1")
                .payload("{}")
                .originatingPrincipal(humanRequester)
                .securityDomain("CLINICAL")
                .correlationId("c-1")
                .causationId("caus-1")
                .requestedAt(now)
                .authorizationDecision(allowedDecision)
                .executingPrincipal(irisBefeExecutor)
                .build())
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("operationType");

        // Missing resourceType
        assertThatThrownBy(() -> PersistenceOperationEnvelope.<String>builder()
                .operationId("op-1")
                .operationType(PersistenceOperationType.CREATE)
                .resourceId("p-1")
                .payload("{}")
                .originatingPrincipal(humanRequester)
                .securityDomain("CLINICAL")
                .correlationId("c-1")
                .causationId("caus-1")
                .requestedAt(now)
                .authorizationDecision(allowedDecision)
                .executingPrincipal(irisBefeExecutor)
                .build())
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("resourceType");

        // Missing resourceId
        assertThatThrownBy(() -> PersistenceOperationEnvelope.<String>builder()
                .operationId("op-1")
                .operationType(PersistenceOperationType.CREATE)
                .resourceType("Person")
                .payload("{}")
                .originatingPrincipal(humanRequester)
                .securityDomain("CLINICAL")
                .correlationId("c-1")
                .causationId("caus-1")
                .requestedAt(now)
                .authorizationDecision(allowedDecision)
                .executingPrincipal(irisBefeExecutor)
                .build())
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("resourceId");

        // Missing originatingPrincipal
        assertThatThrownBy(() -> PersistenceOperationEnvelope.<String>builder()
                .operationId("op-1")
                .operationType(PersistenceOperationType.CREATE)
                .resourceType("Person")
                .resourceId("p-1")
                .payload("{}")
                .securityDomain("CLINICAL")
                .correlationId("c-1")
                .causationId("caus-1")
                .requestedAt(now)
                .authorizationDecision(allowedDecision)
                .executingPrincipal(irisBefeExecutor)
                .build())
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("originatingPrincipal");

        // Missing securityDomain
        assertThatThrownBy(() -> PersistenceOperationEnvelope.<String>builder()
                .operationId("op-1")
                .operationType(PersistenceOperationType.CREATE)
                .resourceType("Person")
                .resourceId("p-1")
                .payload("{}")
                .originatingPrincipal(humanRequester)
                .correlationId("c-1")
                .causationId("caus-1")
                .requestedAt(now)
                .authorizationDecision(allowedDecision)
                .executingPrincipal(irisBefeExecutor)
                .build())
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("securityDomain");

        // Missing correlationId
        assertThatThrownBy(() -> PersistenceOperationEnvelope.<String>builder()
                .operationId("op-1")
                .operationType(PersistenceOperationType.CREATE)
                .resourceType("Person")
                .resourceId("p-1")
                .payload("{}")
                .originatingPrincipal(humanRequester)
                .securityDomain("CLINICAL")
                .causationId("caus-1")
                .requestedAt(now)
                .authorizationDecision(allowedDecision)
                .executingPrincipal(irisBefeExecutor)
                .build())
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("correlationId");

        // Missing causationId
        assertThatThrownBy(() -> PersistenceOperationEnvelope.<String>builder()
                .operationId("op-1")
                .operationType(PersistenceOperationType.CREATE)
                .resourceType("Person")
                .resourceId("p-1")
                .payload("{}")
                .originatingPrincipal(humanRequester)
                .securityDomain("CLINICAL")
                .correlationId("c-1")
                .requestedAt(now)
                .authorizationDecision(allowedDecision)
                .executingPrincipal(irisBefeExecutor)
                .build())
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("causationId");

        // Missing requestedAt
        assertThatThrownBy(() -> PersistenceOperationEnvelope.<String>builder()
                .operationId("op-1")
                .operationType(PersistenceOperationType.CREATE)
                .resourceType("Person")
                .resourceId("p-1")
                .payload("{}")
                .originatingPrincipal(humanRequester)
                .securityDomain("CLINICAL")
                .correlationId("c-1")
                .causationId("caus-1")
                .authorizationDecision(allowedDecision)
                .executingPrincipal(irisBefeExecutor)
                .build())
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("requestedAt");

        // Missing authorizationDecision
        assertThatThrownBy(() -> PersistenceOperationEnvelope.<String>builder()
                .operationId("op-1")
                .operationType(PersistenceOperationType.CREATE)
                .resourceType("Person")
                .resourceId("p-1")
                .payload("{}")
                .originatingPrincipal(humanRequester)
                .securityDomain("CLINICAL")
                .correlationId("c-1")
                .causationId("caus-1")
                .requestedAt(now)
                .executingPrincipal(irisBefeExecutor)
                .build())
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("authorizationDecision");

        // Missing executingPrincipal
        assertThatThrownBy(() -> PersistenceOperationEnvelope.<String>builder()
                .operationId("op-1")
                .operationType(PersistenceOperationType.CREATE)
                .resourceType("Person")
                .resourceId("p-1")
                .payload("{}")
                .originatingPrincipal(humanRequester)
                .securityDomain("CLINICAL")
                .correlationId("c-1")
                .causationId("caus-1")
                .requestedAt(now)
                .authorizationDecision(allowedDecision)
                .build())
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("executingPrincipal");
    }

    @Test
    void testFailClosedValidationPayloadRules() {
        // CREATE with null payload
        assertThatThrownBy(() -> PersistenceOperationEnvelope.<String>builder()
                .operationId("op-1")
                .operationType(PersistenceOperationType.CREATE)
                .resourceType("Person")
                .resourceId("p-1")
                .payload(null)
                .originatingPrincipal(humanRequester)
                .securityDomain("CLINICAL")
                .correlationId("c-1")
                .causationId("caus-1")
                .requestedAt(now)
                .authorizationDecision(allowedDecision)
                .executingPrincipal(irisBefeExecutor)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("payload must not be null");

        // CREATE with blank string payload
        assertThatThrownBy(() -> PersistenceOperationEnvelope.<String>builder()
                .operationId("op-1")
                .operationType(PersistenceOperationType.CREATE)
                .resourceType("Person")
                .resourceId("p-1")
                .payload("   ")
                .originatingPrincipal(humanRequester)
                .securityDomain("CLINICAL")
                .correlationId("c-1")
                .causationId("caus-1")
                .requestedAt(now)
                .authorizationDecision(allowedDecision)
                .executingPrincipal(irisBefeExecutor)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("payload must not be blank");

        // UPDATE with null payload
        assertThatThrownBy(() -> PersistenceOperationEnvelope.<String>builder()
                .operationId("op-1")
                .operationType(PersistenceOperationType.UPDATE)
                .resourceType("Person")
                .resourceId("p-1")
                .payload(null)
                .originatingPrincipal(humanRequester)
                .securityDomain("CLINICAL")
                .correlationId("c-1")
                .causationId("caus-1")
                .requestedAt(now)
                .authorizationDecision(allowedDecision)
                .executingPrincipal(irisBefeExecutor)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("payload must not be null");

        // DELETE with non-null payload
        assertThatThrownBy(() -> PersistenceOperationEnvelope.<String>builder()
                .operationId("op-1")
                .operationType(PersistenceOperationType.DELETE)
                .resourceType("Person")
                .resourceId("p-1")
                .payload("{\"not\":\"allowed\"}")
                .originatingPrincipal(humanRequester)
                .securityDomain("CLINICAL")
                .correlationId("c-1")
                .causationId("caus-1")
                .requestedAt(now)
                .authorizationDecision(allowedDecision)
                .executingPrincipal(irisBefeExecutor)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("payload must be null for DELETE");
    }

    @Test
    void testFailClosedValidationDeniedDecision() {
        assertThatThrownBy(() -> PersistenceOperationEnvelope.<String>builder()
                .operationId("op-1")
                .operationType(PersistenceOperationType.CREATE)
                .resourceType("Person")
                .resourceId("p-1")
                .payload("{}")
                .originatingPrincipal(humanRequester)
                .securityDomain("CLINICAL")
                .correlationId("c-1")
                .causationId("caus-1")
                .requestedAt(now)
                .authorizationDecision(deniedDecision)
                .executingPrincipal(irisBefeExecutor)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("authorizationDecision must not be denied");
    }

    @Test
    void testExecutorTransitionPreservesProvenance() {
        String payload = "{\"resourceType\":\"Person\",\"id\":\"p-123\"}";

        PersistenceOperationEnvelope<String> befeEnvelope = PersistenceOperationEnvelope.<String>builder()
                .operationId("op-hop-1")
                .operationType(PersistenceOperationType.CREATE)
                .resourceType("Person")
                .resourceId("p-123")
                .payload(payload)
                .originatingPrincipal(humanRequester)
                .originatingAuthorities(clinicalAuthorities)
                .securityDomain("CLINICAL")
                .correlationId("corr-root-1")
                .causationId("caus-root-1")
                .requestedAt(now)
                .authorizationDecision(allowedDecision)
                .executingPrincipal(irisBefeExecutor)
                .createdAt(now)
                .attribute("orig", "ingress")
                .build();

        // Hop 1 -> Mneme
        PersistenceOperationEnvelope<String> mnemeEnvelope = befeEnvelope.withExecutingPrincipal(mnemeExecutor);

        assertThat(mnemeEnvelope.getExecutingPrincipal()).isEqualTo(mnemeExecutor);
        assertThat(mnemeEnvelope.getOriginatingPrincipal()).isEqualTo(humanRequester);
        assertThat(mnemeEnvelope.getOriginatingAuthorities()).containsExactlyInAnyOrderElementsOf(clinicalAuthorities);
        assertThat(mnemeEnvelope.getSecurityDomain()).isEqualTo("CLINICAL");
        assertThat(mnemeEnvelope.getCorrelationId()).isEqualTo("corr-root-1");
        assertThat(mnemeEnvelope.getCausationId()).isEqualTo("caus-root-1");
        assertThat(mnemeEnvelope.getRequestedAt()).isEqualTo(now);
        assertThat(mnemeEnvelope.getAuthorizationDecision()).isEqualTo(allowedDecision);
        assertThat(mnemeEnvelope.getOperationId()).isEqualTo("op-hop-1");
        assertThat(mnemeEnvelope.getOperationType()).isEqualTo(PersistenceOperationType.CREATE);
        assertThat(mnemeEnvelope.getResourceType()).isEqualTo("Person");
        assertThat(mnemeEnvelope.getResourceId()).isEqualTo("p-123");
        assertThat(mnemeEnvelope.getPayload()).isEqualTo(payload);

        // Hop 2 -> Mnemosyne
        PersistenceOperationEnvelope<String> mnemosyneEnvelope = mnemeEnvelope.withExecutingPrincipal(mnemosyneExecutor);
        assertThat(mnemosyneEnvelope.getExecutingPrincipal()).isEqualTo(mnemosyneExecutor);
        assertThat(mnemosyneEnvelope.getOriginatingPrincipal()).isEqualTo(humanRequester);
        assertThat(mnemosyneEnvelope.getCorrelationId()).isEqualTo("corr-root-1");
        assertThat(mnemosyneEnvelope.getCausationId()).isEqualTo("caus-root-1");

        // Null executor transition fails
        assertThatThrownBy(() -> befeEnvelope.withExecutingPrincipal(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testBidirectionalContextBridging() {
        ThemisSecurityContext context = new ThemisSecurityContext(
                humanRequester,
                irisBefeExecutor,
                "CLINICAL",
                clinicalAuthorities,
                "corr-bridge-1",
                "caus-bridge-1",
                "tenant-alpha",
                "10.0.0.1",
                now,
                Map.of("source", "hl7-ingress")
        );

        // fromSecurityContext
        PersistenceOperationEnvelope<String> envelope = PersistenceOperationEnvelope.fromSecurityContext(
                PersistenceOperationType.CREATE,
                "Person",
                "p-bridge-1",
                "{\"resourceType\":\"Person\"}",
                context,
                allowedDecision
        );

        assertThat(envelope.getOriginatingPrincipal()).isEqualTo(humanRequester);
        assertThat(envelope.getExecutingPrincipal()).isEqualTo(irisBefeExecutor);
        assertThat(envelope.getSecurityDomain()).isEqualTo("CLINICAL");
        assertThat(envelope.getOriginatingAuthorities()).containsExactlyInAnyOrderElementsOf(clinicalAuthorities);
        assertThat(envelope.getCorrelationId()).isEqualTo("corr-bridge-1");
        assertThat(envelope.getCausationId()).isEqualTo("caus-bridge-1");
        assertThat(envelope.getRequestedAt()).isEqualTo(now);
        assertThat(envelope.getAuthorizationDecision()).isEqualTo(allowedDecision);

        // toSecurityContext
        ThemisSecurityContext reconstructed = envelope.toSecurityContext();
        assertThat(reconstructed.originatingPrincipal()).isEqualTo(humanRequester);
        assertThat(reconstructed.executingPrincipal()).isEqualTo(irisBefeExecutor);
        assertThat(reconstructed.securityDomain()).isEqualTo("CLINICAL");
        assertThat(reconstructed.authorities()).containsExactlyInAnyOrderElementsOf(clinicalAuthorities);
        assertThat(reconstructed.correlationId()).isEqualTo("corr-bridge-1");
        assertThat(reconstructed.causationId()).isEqualTo("caus-bridge-1");
        assertThat(reconstructed.requestedAt()).isEqualTo(now);
    }

    @Test
    void testFactoryHelpers() {
        ThemisSecurityContext context = new ThemisSecurityContext(
                humanRequester,
                irisBefeExecutor,
                "CLINICAL",
                clinicalAuthorities,
                "corr-f-1",
                "caus-f-1",
                null,
                null,
                now,
                Map.of()
        );

        // ofCreate
        PersistenceOperationEnvelope<String> createEnv = PersistenceOperationEnvelope.ofCreate(
                "Person", "p-1", "{\"resourceType\":\"Person\"}", context, allowedDecision);
        assertThat(createEnv.getOperationType()).isEqualTo(PersistenceOperationType.CREATE);
        assertThat(createEnv.getPayload()).isNotNull();

        // ofUpdate with version
        PersistenceOperationEnvelope<String> updateEnv = PersistenceOperationEnvelope.ofUpdate(
                "Person", "p-1", 3L, "{\"resourceType\":\"Person\"}", context, allowedDecision);
        assertThat(updateEnv.getOperationType()).isEqualTo(PersistenceOperationType.UPDATE);
        assertThat(updateEnv.getExpectedVersion()).isEqualTo(3L);

        // ofDelete with version
        PersistenceOperationEnvelope<String> deleteEnv = PersistenceOperationEnvelope.ofDelete(
                "Person", "p-1", 3L, context, allowedDecision);
        assertThat(deleteEnv.getOperationType()).isEqualTo(PersistenceOperationType.DELETE);
        assertThat(deleteEnv.getPayload()).isNull();
        assertThat(deleteEnv.getExpectedVersion()).isEqualTo(3L);
    }

    @Test
    void testJacksonJsonSerializationRoundTrip() throws Exception {
        String payload = "{\"resourceType\":\"Person\",\"id\":\"p-json\",\"birthDate\":\"1980-01-01\"}";

        PersistenceOperationEnvelope<String> original = PersistenceOperationEnvelope.<String>builder()
                .operationId(UUID.randomUUID().toString())
                .operationType(PersistenceOperationType.CREATE)
                .resourceType("Person")
                .resourceId("p-json")
                .expectedVersion(1L)
                .payload(payload)
                .originatingPrincipal(humanRequester)
                .originatingAuthorities(clinicalAuthorities)
                .securityDomain("CLINICAL")
                .correlationId("corr-json-1")
                .causationId("caus-json-1")
                .requestedAt(now)
                .authorizationDecision(allowedDecision)
                .executingPrincipal(irisBefeExecutor)
                .createdAt(now)
                .attribute("trace", "t-1")
                .build();

        String json = objectMapper.writeValueAsString(original);
        assertThat(json).isNotBlank();

        PersistenceOperationEnvelope<String> deserialized = objectMapper.readValue(
                json, new TypeReference<PersistenceOperationEnvelope<String>>() {});

        assertThat(deserialized).isEqualTo(original);
        assertThat(deserialized.getOperationId()).isEqualTo(original.getOperationId());
        assertThat(deserialized.getOperationType()).isEqualTo(original.getOperationType());
        assertThat(deserialized.getResourceType()).isEqualTo(original.getResourceType());
        assertThat(deserialized.getResourceId()).isEqualTo(original.getResourceId());
        assertThat(deserialized.getExpectedVersion()).isEqualTo(1L);
        assertThat(deserialized.getPayload()).isEqualTo(payload);
        assertThat(deserialized.getOriginatingPrincipal()).isEqualTo(humanRequester);
        assertThat(deserialized.getOriginatingAuthorities()).containsExactlyInAnyOrderElementsOf(clinicalAuthorities);
        assertThat(deserialized.getSecurityDomain()).isEqualTo("CLINICAL");
        assertThat(deserialized.getCorrelationId()).isEqualTo("corr-json-1");
        assertThat(deserialized.getCausationId()).isEqualTo("caus-json-1");
        assertThat(deserialized.getRequestedAt()).isEqualTo(now);
        assertThat(deserialized.getAuthorizationDecision().decision()).isEqualTo(allowedDecision.decision());
        assertThat(deserialized.getExecutingPrincipal()).isEqualTo(irisBefeExecutor);
        assertThat(deserialized.getCreatedAt()).isEqualTo(now);
        assertThat(deserialized.getAttributes()).containsEntry("trace", "t-1");
    }

    @Test
    void testJacksonSerializationDeleteEnvelope() throws Exception {
        PersistenceOperationEnvelope<String> deleteEnv = PersistenceOperationEnvelope.<String>builder()
                .operationId("op-del-json")
                .operationType(PersistenceOperationType.DELETE)
                .resourceType("Location")
                .resourceId("loc-99")
                .expectedVersion(5L)
                .originatingPrincipal(humanRequester)
                .securityDomain("CLINICAL")
                .correlationId("corr-del-json")
                .causationId("caus-del-json")
                .requestedAt(now)
                .authorizationDecision(allowedDecision)
                .executingPrincipal(irisBefeExecutor)
                .createdAt(now)
                .build();

        String json = objectMapper.writeValueAsString(deleteEnv);
        PersistenceOperationEnvelope<String> deserialized = objectMapper.readValue(
                json, new TypeReference<PersistenceOperationEnvelope<String>>() {});

        assertThat(deserialized).isEqualTo(deleteEnv);
        assertThat(deserialized.getPayload()).isNull();
        assertThat(deserialized.getOperationType()).isEqualTo(PersistenceOperationType.DELETE);
    }

    @Test
    void testToStringDoesNotExposePayloadContent() {
        String sensitivePayload = "{\"resourceType\":\"Patient\",\"name\":\"Sensitive Clinical Data MRN-99999\"}";

        PersistenceOperationEnvelope<String> envelope = PersistenceOperationEnvelope.<String>builder()
                .operationId("op-sec-1")
                .operationType(PersistenceOperationType.CREATE)
                .resourceType("Patient")
                .resourceId("pat-1")
                .payload(sensitivePayload)
                .originatingPrincipal(humanRequester)
                .securityDomain("CLINICAL")
                .correlationId("corr-sec-1")
                .causationId("caus-sec-1")
                .requestedAt(now)
                .authorizationDecision(allowedDecision)
                .executingPrincipal(irisBefeExecutor)
                .build();

        String stringRepr = envelope.toString();
        assertThat(stringRepr).contains("op-sec-1");
        assertThat(stringRepr).contains("payloadPresent=true");
        assertThat(stringRepr).doesNotContain("Sensitive Clinical Data MRN-99999");
    }

    @Test
    void testEqualsAndHashCode() {
        PersistenceOperationEnvelope<String> env1 = PersistenceOperationEnvelope.<String>builder()
                .operationId("op-1")
                .operationType(PersistenceOperationType.CREATE)
                .resourceType("Person")
                .resourceId("p-1")
                .payload("{}")
                .originatingPrincipal(humanRequester)
                .securityDomain("CLINICAL")
                .correlationId("c-1")
                .causationId("caus-1")
                .requestedAt(now)
                .authorizationDecision(allowedDecision)
                .executingPrincipal(irisBefeExecutor)
                .createdAt(now)
                .build();

        PersistenceOperationEnvelope<String> env2 = PersistenceOperationEnvelope.<String>builder()
                .operationId("op-1")
                .operationType(PersistenceOperationType.CREATE)
                .resourceType("Person")
                .resourceId("p-1")
                .payload("{}")
                .originatingPrincipal(humanRequester)
                .securityDomain("CLINICAL")
                .correlationId("c-1")
                .causationId("caus-1")
                .requestedAt(now)
                .authorizationDecision(allowedDecision)
                .executingPrincipal(irisBefeExecutor)
                .createdAt(now)
                .build();

        assertThat(env1).isEqualTo(env2);
        assertThat(env1.hashCode()).isEqualTo(env2.hashCode());
    }
}
