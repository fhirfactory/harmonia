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

package net.fhirfactory.harmonia.praxis.conduit;

import net.fhirfactory.harmonia.erga.base.ErgonBase;
import net.fhirfactory.harmonia.model.ergon.ErgonPayload;
import net.fhirfactory.harmonia.model.pragma.Pragma;
import net.fhirfactory.harmonia.model.pragma.PragmaStatus;
import net.fhirfactory.harmonia.model.security.ErgonSecurityDefinition;
import net.fhirfactory.harmonia.model.security.HarmoniaAuthorityEnum;
import net.fhirfactory.harmonia.model.security.HarmoniaRoleEnum;
import net.fhirfactory.harmonia.model.topic.Topic;
import net.fhirfactory.harmonia.praxis.cache.PragmaCacheService;
import net.fhirfactory.harmonia.praxis.sequence.PraxisImplementation;
import net.fhirfactory.harmonia.themis.api.ThemisService;
import net.fhirfactory.harmonia.themis.api.model.PrincipalType;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthority;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;
import net.fhirfactory.harmonia.themis.core.evaluator.DeterministicPolicyEvaluator;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Ponos PragmaWorkflowDispatcher Themis Security Gate Tests")
class PragmaWorkflowDispatcherSecurityTest {

    private CamelContext camelContext;
    private PragmaCacheService pragmaCacheService;
    private ThemisService themisService;
    private PragmaWorkflowDispatcher dispatcher;

    static class DummyErgon extends ErgonBase {
        public DummyErgon(String id, String name) {
            super(id, name);
            setSecurityDefinition(ErgonSecurityDefinition.forProviderRegistryChange(id, "Practitioner"));
        }

        @Override
        protected void processErgon(Pragma pragma, org.apache.camel.Exchange exchange) throws Exception {
            if (pragma != null) {
                pragma.setStatus(PragmaStatus.COMPLETED);
            }
        }
    }

    static class DummyPraxis extends PraxisImplementation {
        public DummyPraxis(String id, String name, ErgonBase activity) {
            super(id, name);
            addActivity(activity);
            configureChainedEndpoints();
        }
    }

    @BeforeEach
    void setUp() throws Exception {
        camelContext = new DefaultCamelContext();
        pragmaCacheService = Mockito.mock(PragmaCacheService.class);
        themisService = DeterministicPolicyEvaluator.withDefaultPolicies();

        dispatcher = new PragmaWorkflowDispatcher(camelContext, null, themisService, pragmaCacheService);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (camelContext != null) {
            camelContext.stop();
        }
    }

    @Test
    @DisplayName("Allows dispatch when originating submitter has PRV_SUB and Ergon has PRV_PROC (Governed Write)")
    void testAuthorizedDispatchAllowed() throws Exception {
        DummyErgon ergon = new DummyErgon("ergon:practitioner-change", "Practitioner Change Ergon");
        DummyPraxis praxis = new DummyPraxis("praxis:practitioner-change", "Practitioner Change Praxis", ergon);

        praxis.registerRoutes(camelContext);
        camelContext.addRoutes(praxis.createSequencePipelineRoute());
        camelContext.start();

        dispatcher.registerWorkflow(praxis);

        ThemisPrincipal principal = ThemisPrincipal.of("user:registry-submitter", PrincipalType.HUMAN, "hospital-east");
        ThemisAuthority submitAuth = HarmoniaAuthorityEnum.PROVIDER_CHANGE_SUBMIT.toThemisAuthority();

        Pragma pragma = Pragma.builder()
                .pragmaId("pragma-auth-001")
                .praxisId("praxis:practitioner-change")
                .status(PragmaStatus.ACCEPTED)
                .originatingPrincipal(principal)
                .addOriginatingAuthority(submitAuth)
                .originatingSecurityContext(ThemisSecurityContext.fromPrincipal(principal, "corr-001"))
                .policyVersion("1.0.0")
                .build();

        pragma.addInput(ErgonPayload.fromJson(0,
                new Topic("Health", "ProviderRegistry", "1.0", "Change", "Practitioner"),
                null,
                "{\"resourceType\":\"Practitioner\",\"id\":\"PR-1\"}"));

        boolean result = dispatcher.dispatchPragma(pragma);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Rejects dispatch when originating caller only has PRV_RDR (Originating Privilege Failure)")
    void testDeniedDispatchOriginatingPrivilegeFailure() throws Exception {
        DummyErgon ergon = new DummyErgon("ergon:practitioner-change", "Practitioner Change Ergon");
        DummyPraxis praxis = new DummyPraxis("praxis:practitioner-change", "Practitioner Change Praxis", ergon);

        praxis.registerRoutes(camelContext);
        camelContext.addRoutes(praxis.createSequencePipelineRoute());
        camelContext.start();

        dispatcher.registerWorkflow(praxis);

        ThemisPrincipal principal = ThemisPrincipal.of("user:reader-only", PrincipalType.HUMAN, "hospital-east");
        ThemisAuthority readAuth = HarmoniaAuthorityEnum.PROVIDER_READ.toThemisAuthority();

        Pragma pragma = Pragma.builder()
                .pragmaId("pragma-deny-001")
                .praxisId("praxis:practitioner-change")
                .status(PragmaStatus.ACCEPTED)
                .originatingPrincipal(principal)
                .addOriginatingAuthority(readAuth) // Only read, missing submit!
                .originatingSecurityContext(ThemisSecurityContext.fromPrincipal(principal, "corr-002"))
                .policyVersion("1.0.0")
                .build();

        pragma.addInput(ErgonPayload.fromJson(0,
                new Topic("Health", "ProviderRegistry", "1.0", "Change", "Practitioner"),
                null,
                "{\"resourceType\":\"Practitioner\",\"id\":\"PR-2\"}"));

        boolean result = dispatcher.dispatchPragma(pragma);
        assertThat(result).isFalse();
        assertThat(pragma.getStatus()).isEqualTo(PragmaStatus.FAILED);
        assertThat(pragma.getCheckpoints())
                .anyMatch(cp -> cp.getStageName().equals("THEMIS_EXECUTION_GATE")
                        && cp.getStatusMessage().contains("AUTHORITY_MISSING"));
    }

    @Test
    @DisplayName("Rejects dispatch when Ergon execution authority is revoked (Ergon Execution Gate Defence-in-Depth)")
    void testDeniedDispatchErgonExecutionAuthorityRevoked() throws Exception {
        DummyErgon ergon = new DummyErgon("ergon:practitioner-change", "Practitioner Change Ergon");
        // Explicitly revoke execution authority from Ergon security envelope
        ergon.setSecurityDefinition(ErgonSecurityDefinition.builder()
                .ergonId("ergon:practitioner-change")
                .requiredExecutionAuthorities(Set.of()) // Stripped execution authority!
                .permittedResourceType("Practitioner")
                .permittedSecurityDomain("PROVIDER_REGISTRY")
                .build());

        DummyPraxis praxis = new DummyPraxis("praxis:practitioner-change", "Practitioner Change Praxis", ergon);

        praxis.registerRoutes(camelContext);
        camelContext.addRoutes(praxis.createSequencePipelineRoute());
        camelContext.start();

        dispatcher.registerWorkflow(praxis);

        ThemisPrincipal principal = ThemisPrincipal.of("user:registry-submitter", PrincipalType.HUMAN, "hospital-east");
        ThemisAuthority submitAuth = HarmoniaAuthorityEnum.PROVIDER_CHANGE_SUBMIT.toThemisAuthority();

        Pragma pragma = Pragma.builder()
                .pragmaId("pragma-deny-002")
                .praxisId("praxis:practitioner-change")
                .status(PragmaStatus.ACCEPTED)
                .originatingPrincipal(principal)
                .addOriginatingAuthority(submitAuth) // Originating is valid
                .originatingSecurityContext(ThemisSecurityContext.fromPrincipal(principal, "corr-003"))
                .policyVersion("1.0.0")
                .build();

        pragma.addInput(ErgonPayload.fromJson(0,
                new Topic("Health", "ProviderRegistry", "1.0", "Change", "Practitioner"),
                null,
                "{\"resourceType\":\"Practitioner\",\"id\":\"PR-3\"}"));

        boolean result = dispatcher.dispatchPragma(pragma);
        assertThat(result).isFalse();
        assertThat(pragma.getStatus()).isEqualTo(PragmaStatus.FAILED);
        assertThat(pragma.getCheckpoints())
                .anyMatch(cp -> cp.getStageName().equals("THEMIS_EXECUTION_GATE")
                        && cp.getStatusMessage().contains("EXECUTION_AUTHORITY_MISSING"));
    }
}
