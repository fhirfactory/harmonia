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

package net.fhirfactory.harmonia.themis.api.model;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Instant;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ThemisSecurityContextTest {

    @Test
    void testSecurityContextImmutabilityAndDualPrincipal() {
        ThemisPrincipal human = ThemisPrincipal.of("dr.mark", PrincipalType.HUMAN, "CLINICAL");
        ThemisPrincipal process = ThemisPrincipal.of("process:ponos-worker", PrincipalType.PROCESS, "WORKFLOW");
        ThemisAuthority auth1 = ThemisAuthority.of("clinical.read");
        ThemisAuthority auth2 = ThemisAuthority.of("clinical.create");
        Instant now = Instant.now();

        ThemisSecurityContext ctx = ThemisSecurityContext.builder()
                .originatingPrincipal(human)
                .executingPrincipal(process)
                .securityDomain("CLINICAL")
                .authorities(Set.of(auth1, auth2))
                .correlationId("corr-123")
                .causationId("cause-456")
                .tenantId("tenant-primary")
                .clientIp("10.0.0.1")
                .requestedAt(now)
                .addAttribute("sourceGateway", "iris-befe")
                .build();

        assertThat(ctx.requestingPrincipal()).isEqualTo(human);
        assertThat(ctx.originatingPrincipal()).isEqualTo(human);
        assertThat(ctx.executingPrincipal()).isEqualTo(process);
        assertThat(ctx.securityDomain()).isEqualTo("CLINICAL");
        assertThat(ctx.authorities()).containsExactlyInAnyOrder(auth1, auth2);
        assertThat(ctx.correlationId()).isEqualTo("corr-123");
        assertThat(ctx.causationId()).isEqualTo("cause-456");
        assertThat(ctx.tenantId()).isEqualTo("tenant-primary");
        assertThat(ctx.clientIp()).isEqualTo("10.0.0.1");
        assertThat(ctx.requestedAt()).isEqualTo(now);
        assertThat(ctx.attributes()).containsEntry("sourceGateway", "iris-befe");

        // Immutability checks
        assertThatThrownBy(() -> ctx.authorities().add(ThemisAuthority.of("admin")))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> ctx.attributes().put("foo", "bar"))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void testWithExecutingPrincipalPreservesInitiator() {
        ThemisPrincipal human = ThemisPrincipal.of("dr.mark", PrincipalType.HUMAN, "CLINICAL");
        ThemisPrincipal ergonWorker = ThemisPrincipal.of("service:ergon-assembler", PrincipalType.SERVICE, "INTEGRATION");

        ThemisSecurityContext rootCtx = ThemisSecurityContext.builder()
                .originatingPrincipal(human)
                .securityDomain("CLINICAL")
                .addAuthority("clinical.create")
                .correlationId("corr-root")
                .build();

        ThemisSecurityContext delegatedCtx = rootCtx.withExecutingPrincipal(ergonWorker)
                .withCausationId("task-parent-1");

        assertThat(delegatedCtx.originatingPrincipal()).isEqualTo(human);
        assertThat(delegatedCtx.requestingPrincipal()).isEqualTo(human);
        assertThat(delegatedCtx.executingPrincipal()).isEqualTo(ergonWorker);
        assertThat(delegatedCtx.correlationId()).isEqualTo("corr-root");
        assertThat(delegatedCtx.causationId()).isEqualTo("task-parent-1");
        assertThat(delegatedCtx.securityDomain()).isEqualTo("CLINICAL");
        assertThat(delegatedCtx.authorities()).containsExactly(ThemisAuthority.of("clinical.create"));
    }

    @Test
    void testBackwardCompatibleConstructors() {
        ThemisPrincipal human = ThemisPrincipal.of("user-1", PrincipalType.HUMAN, "OPERATIONS");
        ThemisSecurityContext ctx = ThemisSecurityContext.fromPrincipal(human, "corr-abc");

        assertThat(ctx.requestingPrincipal()).isEqualTo(human);
        assertThat(ctx.originatingPrincipal()).isEqualTo(human);
        assertThat(ctx.securityDomain()).isEqualTo("OPERATIONS");
        assertThat(ctx.correlationId()).isEqualTo("corr-abc");
        assertThat(ctx.executingPrincipal()).isNull();
        assertThat(ctx.authorities()).isEmpty();
        assertThat(ctx.attributes()).isEmpty();
    }

    @Test
    void testThemisPrincipalSafeToStringSuppressesAttributesAndRetainsMetadata() {
        String secretToken = "TOKEN-SECRET-MARKER-81742";
        String phiMarker = "PATIENT-PHI-MARKER-92831";

        ThemisPrincipal principal = new ThemisPrincipal(
                "user:dr-smith",
                PrincipalType.HUMAN,
                "CLINICAL-EMR",
                Map.of(
                        "credential", secretToken,
                        "patientName", phiMarker,
                        "sessionKey", "SESSION-ABC-999"
                )
        );

        String str = principal.toString();

        // Safe operational metadata must be present
        assertThat(str).contains("ThemisPrincipal[");
        assertThat(str).contains("principalId=user:dr-smith");
        assertThat(str).contains("principalType=HUMAN");
        assertThat(str).contains("sourceDomain=CLINICAL-EMR");
        assertThat(str).contains("attributeCount=3");

        // Raw attributes and sensitive markers must be absent
        assertThat(str).doesNotContain(secretToken);
        assertThat(str).doesNotContain(phiMarker);
        assertThat(str).doesNotContain("SESSION-ABC-999");
        assertThat(str).doesNotContain("credential");
        assertThat(str).doesNotContain("patientName");
        assertThat(str).doesNotContain("sessionKey");

        // Record accessors and contracts remain intact
        assertThat(principal.principalId()).isEqualTo("user:dr-smith");
        assertThat(principal.principalType()).isEqualTo(PrincipalType.HUMAN);
        assertThat(principal.sourceDomain()).isEqualTo("CLINICAL-EMR");
        assertThat(principal.attributes()).hasSize(3);
    }

    @Test
    void testThemisSecurityContextSafeToStringSuppressesSensitiveDataAndRetainsMetadata() {
        String secretToken = "TOKEN-SECRET-MARKER-81742";
        String phiMarker = "PATIENT-PHI-MARKER-92831";
        String secretAuthority = "AUTHORITY-SECRET-81742";

        ThemisPrincipal originating = new ThemisPrincipal(
                "user:initiator",
                PrincipalType.HUMAN,
                "CLINICAL-PORTAL",
                Map.of("principalSecret", secretToken)
        );

        ThemisPrincipal executing = ThemisPrincipal.of(
                "service:ponos-engine",
                PrincipalType.SERVICE,
                "WORKFLOW"
        );

        Instant requestedAt = Instant.parse("2026-09-23T08:00:00Z");

        ThemisSecurityContext context = ThemisSecurityContext.builder()
                .originatingPrincipal(originating)
                .executingPrincipal(executing)
                .securityDomain("CLINICAL")
                .addAuthority(secretAuthority)
                .addAuthority("clinical.read")
                .correlationId("corr-safe-777")
                .causationId("cause-safe-888")
                .tenantId("tenant-primary")
                .clientIp("192.168.1.100")
                .requestedAt(requestedAt)
                .addAttribute("bearerAuth", secretToken)
                .addAttribute("patientSummary", phiMarker)
                .addAttribute("sourceGateway", "iris-befe")
                .build();

        String str = context.toString();

        // Safe operational metadata must be present
        assertThat(str).contains("ThemisSecurityContext[");
        assertThat(str).contains("originatingPrincipal=ThemisPrincipal[");
        assertThat(str).contains("principalId=user:initiator");
        assertThat(str).contains("executingPrincipal=ThemisPrincipal[");
        assertThat(str).contains("principalId=service:ponos-engine");
        assertThat(str).contains("securityDomain=CLINICAL");
        assertThat(str).contains("correlationId=corr-safe-777");
        assertThat(str).contains("causationId=cause-safe-888");
        assertThat(str).contains("tenantId=tenant-primary");
        assertThat(str).contains("clientIp=192.168.1.100");
        assertThat(str).contains("requestedAt=" + requestedAt);
        assertThat(str).contains("authoritiesCount=2");
        assertThat(str).contains("attributeCount=3");

        // Raw authority codes, attribute keys, and attribute values must be absent
        assertThat(str).doesNotContain(secretToken);
        assertThat(str).doesNotContain(phiMarker);
        assertThat(str).doesNotContain(secretAuthority);
        assertThat(str).doesNotContain("clinical.read");
        assertThat(str).doesNotContain("bearerAuth");
        assertThat(str).doesNotContain("patientSummary");
        assertThat(str).doesNotContain("principalSecret");

        // Nested originating principal must also suppress its attributes
        assertThat(str).contains("originatingPrincipal=ThemisPrincipal[principalId=user:initiator, principalType=HUMAN, sourceDomain=CLINICAL-PORTAL, attributeCount=1]");

        // Accessors and contracts remain intact
        assertThat(context.originatingPrincipal()).isEqualTo(originating);
        assertThat(context.executingPrincipal()).isEqualTo(executing);
        assertThat(context.securityDomain()).isEqualTo("CLINICAL");
        assertThat(context.correlationId()).isEqualTo("corr-safe-777");
        assertThat(context.causationId()).isEqualTo("cause-safe-888");
        assertThat(context.tenantId()).isEqualTo("tenant-primary");
        assertThat(context.clientIp()).isEqualTo("192.168.1.100");
        assertThat(context.requestedAt()).isEqualTo(requestedAt);
        assertThat(context.authorities()).hasSize(2);
        assertThat(context.attributes()).hasSize(3);
    }

    @Test
    void testThemisPrincipalAndSecurityContextSerializationPreserved() throws IOException, ClassNotFoundException {
        ThemisPrincipal principal = new ThemisPrincipal(
                "user:mark",
                PrincipalType.HUMAN,
                "CLINICAL",
                Map.of("token", "TOKEN-SECRET-MARKER-81742")
        );

        ThemisSecurityContext context = ThemisSecurityContext.builder()
                .originatingPrincipal(principal)
                .securityDomain("CLINICAL")
                .correlationId("corr-ser-1")
                .causationId("cause-ser-2")
                .tenantId("tenant-1")
                .clientIp("127.0.0.1")
                .addAuthority("clinical.read")
                .addAttribute("phi", "PATIENT-PHI-MARKER-92831")
                .build();

        // Serialize and deserialize ThemisPrincipal
        ByteArrayOutputStream principalBaos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(principalBaos)) {
            oos.writeObject(principal);
        }
        ThemisPrincipal deserializedPrincipal;
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(principalBaos.toByteArray()))) {
            deserializedPrincipal = (ThemisPrincipal) ois.readObject();
        }

        assertThat(deserializedPrincipal).isEqualTo(principal);
        assertThat(deserializedPrincipal.hashCode()).isEqualTo(principal.hashCode());
        assertThat(deserializedPrincipal.toString()).isEqualTo(principal.toString());
        assertThat(deserializedPrincipal.attributes()).containsEntry("token", "TOKEN-SECRET-MARKER-81742");

        // Serialize and deserialize ThemisSecurityContext
        ByteArrayOutputStream contextBaos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(contextBaos)) {
            oos.writeObject(context);
        }
        ThemisSecurityContext deserializedContext;
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(contextBaos.toByteArray()))) {
            deserializedContext = (ThemisSecurityContext) ois.readObject();
        }

        assertThat(deserializedContext).isEqualTo(context);
        assertThat(deserializedContext.hashCode()).isEqualTo(context.hashCode());
        assertThat(deserializedContext.toString()).isEqualTo(context.toString());
        assertThat(deserializedContext.attributes()).containsEntry("phi", "PATIENT-PHI-MARKER-92831");
        assertThat(deserializedContext.authorities()).contains(ThemisAuthority.of("clinical.read"));
    }

    @Test
    void testEmptyAndNullComponentsSafeToString() {
        ThemisSecurityContext anonymous = ThemisSecurityContext.anonymous();
        String str = anonymous.toString();

        assertThat(str).contains("ThemisSecurityContext[");
        assertThat(str).contains("originatingPrincipal=null");
        assertThat(str).contains("executingPrincipal=null");
        assertThat(str).contains("securityDomain=null");
        assertThat(str).contains("correlationId=null");
        assertThat(str).contains("causationId=null");
        assertThat(str).contains("tenantId=null");
        assertThat(str).contains("clientIp=null");
        assertThat(str).contains("authoritiesCount=0");
        assertThat(str).contains("attributeCount=0");

        ThemisPrincipal minimalPrincipal = new ThemisPrincipal(null, null, null, null);
        String principalStr = minimalPrincipal.toString();
        assertThat(principalStr).isEqualTo("ThemisPrincipal[principalId=null, principalType=null, sourceDomain=null, attributeCount=0]");
    }
}
