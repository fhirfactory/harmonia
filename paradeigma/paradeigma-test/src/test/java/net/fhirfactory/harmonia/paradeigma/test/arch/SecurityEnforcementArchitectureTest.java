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

package net.fhirfactory.harmonia.paradeigma.test.arch;

import net.fhirfactory.harmonia.logging.PhiLoggingConfig;
import net.fhirfactory.harmonia.model.persistence.PersistenceOperationEnvelope;
import net.fhirfactory.harmonia.model.persistence.PersistenceOperationType;
import net.fhirfactory.harmonia.model.security.FhirSecurityTagManager;
import net.fhirfactory.harmonia.themis.api.ThemisService;
import net.fhirfactory.harmonia.themis.api.model.PrincipalType;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthority;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;
import net.fhirfactory.harmonia.themis.api.policy.ThemisPolicy;
import net.fhirfactory.harmonia.kleio.audit.model.AuditAction;
import net.fhirfactory.harmonia.kleio.audit.model.AuditAuthorizationEvidence;
import net.fhirfactory.harmonia.kleio.audit.model.AuditClassification;
import net.fhirfactory.harmonia.kleio.audit.model.AuditOutcome;
import net.fhirfactory.harmonia.kleio.audit.model.AuditPrincipal;
import net.fhirfactory.harmonia.kleio.audit.model.AuditSource;
import net.fhirfactory.harmonia.kleio.audit.model.AuditTarget;
import net.fhirfactory.harmonia.kleio.audit.model.HarmoniaAuditEvent;
import net.fhirfactory.harmonia.kleio.audit.service.AuditService;
import net.fhirfactory.harmonia.themis.core.evaluator.DeterministicPolicyEvaluator;
import net.fhirfactory.harmonia.themis.core.identities.HarmoniaServiceIdentities;
import net.fhirfactory.harmonia.themis.core.policy.AuditImmutabilityDenyPolicy;
import net.fhirfactory.harmonia.themis.core.policy.AuditReadPolicy;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Architectural tests enforcing security governance, default-deny policy contracts,
 * audit management, and security context propagation invariants.
 */
public class SecurityEnforcementArchitectureTest {

    @Test
    @DisplayName("Architecture Check: Themis security contracts and engines are properly defined")
    void themisSecurityContractsAndEngineDefined() {
        assertThat(ThemisService.class).isInterface();
        assertThat(ThemisPolicy.class).isInterface();
        assertThat(DeterministicPolicyEvaluator.class).isNotNull();
    }

    @Test
    @DisplayName("Architecture Check: Kleio audit service contract is properly defined")
    void kleioAuditServiceContractDefined() {
        assertThat(AuditService.class).isInterface();
    }

    @Test
    @DisplayName("Architecture Check: Kleio-FHIR boundary is strictly enforced")
    void kleioFhirBoundaryEnforcement() {
        ArchRule rule = classes()
                .that().resideInAPackage("net.fhirfactory.harmonia.kleio.fhir..")
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage(
                        "net.fhirfactory.harmonia.kleio.audit.model..",
                        "net.fhirfactory.harmonia.themis.api.model..",
                        "net.fhirfactory.harmonia.kleio.fhir..",
                        "net.fhirfactory.harmonia.model..",
                        "ca.uhn.hapi.fhir..",
                        "org.hl7.fhir..",
                        "java..",
                        "org.slf4j.."
                );
        rule.check(new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("net.fhirfactory.harmonia"));

        ArchRule noPersistence = noClasses()
                .that().resideInAPackage("net.fhirfactory.harmonia.kleio.fhir..")
                .should().dependOnClassesThat()
                .resideInAnyPackage(
                        "jakarta.persistence..",
                        "ca.uhn.fhir.jpa..",
                        "net.fhirfactory.harmonia.hestia..",
                        "net.fhirfactory.harmonia.iris.."
                );
        noPersistence.check(new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("net.fhirfactory.harmonia"));
    }

    @Test
    @DisplayName("Architecture Check: Themis subproject has zero dependencies on kleio-fhir")
    void themisMustNotDependOnKleioFhir() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("net.fhirfactory.harmonia.themis..")
                .should().dependOnClassesThat()
                .resideInAPackage("net.fhirfactory.harmonia.kleio.fhir..");
        rule.check(new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("net.fhirfactory.harmonia"));
    }

    @Test
    @DisplayName("Architecture Check: Security context and FHIR security tag managers are accessible")
    void securityContextAndTagManagersDefined() {
        assertThat(ThemisSecurityContext.class).isNotNull();
        assertThat(FhirSecurityTagManager.class).isNotNull();
    }

    @Test
    @DisplayName("Architecture Check: Ponos canonical executing process identity and least-privilege authorities are registered")
    void ponosCanonicalProcessIdentityAndAuthoritiesDefined() {
        assertThat(HarmoniaServiceIdentities.ID_PONOS_PROCESS).isEqualTo("process:ponos-engine");

        ThemisPrincipal ponosProcess = HarmoniaServiceIdentities.PRINCIPAL_PONOS_PROCESS;
        assertThat(ponosProcess).isNotNull();
        assertThat(ponosProcess.principalId()).isEqualTo("process:ponos-engine");
        assertThat(ponosProcess.principalType()).isEqualTo(PrincipalType.PROCESS);
        assertThat(ponosProcess.sourceDomain()).isEqualTo("ponos");

        Set<ThemisAuthority> ponosAuthorities = HarmoniaServiceIdentities.getAuthorities(HarmoniaServiceIdentities.ID_PONOS_PROCESS);
        assertThat(ponosAuthorities).contains(
                ThemisAuthority.of("provider.change.process"),
                ThemisAuthority.of("system.integration"));
    }

    @Test
    @DisplayName("Architecture Check: Mneme canonical executing service identity and least-privilege authorities are registered")
    void mnemeCanonicalServiceIdentityAndAuthoritiesDefined() {
        assertThat(HarmoniaServiceIdentities.ID_MNEME).isEqualTo("service:mneme");

        ThemisPrincipal mnemeService = HarmoniaServiceIdentities.PRINCIPAL_MNEME;
        assertThat(mnemeService).isNotNull();
        assertThat(mnemeService.principalId()).isEqualTo("service:mneme");
        assertThat(mnemeService.principalType()).isEqualTo(PrincipalType.SERVICE);
        assertThat(mnemeService.sourceDomain()).isEqualTo("mneme");

        Set<ThemisAuthority> mnemeAuthorities = HarmoniaServiceIdentities.getAuthorities(HarmoniaServiceIdentities.ID_MNEME);
        assertThat(mnemeAuthorities)
                .extracting(ThemisAuthority::authorityCode)
                .contains(
                        "clinical.read",
                        "clinical.create",
                        "clinical.update",
                        "clinical.delete",
                        "system.integration"
                );
    }

    @Test
    @DisplayName("Architecture Check: Canonical persistence contract types reside strictly in calliope model.persistence")
    void persistenceContractArchitectureInvariants() {
        assertThat(PersistenceOperationType.class.isEnum()).isTrue();
        assertThat(PersistenceOperationType.class.getPackageName())
                .isEqualTo("net.fhirfactory.harmonia.model.persistence");

        assertThat(PersistenceOperationEnvelope.class).isNotNull();
        assertThat(PersistenceOperationEnvelope.class.getPackageName())
                .isEqualTo("net.fhirfactory.harmonia.model.persistence");

        // Verify zero dependencies on JPA, Hibernate, or storage modules
        for (var field : PersistenceOperationEnvelope.class.getDeclaredFields()) {
            String typeName = field.getType().getName();
            assertThat(typeName)
                    .doesNotStartWith("jakarta.persistence")
                    .doesNotStartWith("javax.persistence")
                    .doesNotStartWith("org.hibernate")
                    .doesNotStartWith("ca.uhn.fhir.jpa")
                    .doesNotStartWith("net.fhirfactory.harmonia.hapifhir");
        }
    }

    @Test
    @DisplayName("Architecture Check: PragmaWorkflowDispatcher and ErgonBase enforce canonical Ponos process identity")
    void workflowDispatcherAndErgonBaseReferencePonosProcessIdentity() throws IOException {
        Path projectRoot = findProjectRoot();

        // 1. Verify PragmaWorkflowDispatcher uses HarmoniaServiceIdentities.PRINCIPAL_PONOS_PROCESS
        Path dispatcherPath = projectRoot.resolve("energeia/ponos/src/main/java/net/fhirfactory/harmonia/praxis/conduit/PragmaWorkflowDispatcher.java");
        assertThat(Files.exists(dispatcherPath)).isTrue();
        String dispatcherContent = Files.readString(dispatcherPath);
        assertThat(dispatcherContent).contains("HarmoniaServiceIdentities.PRINCIPAL_PONOS_PROCESS");

        // 2. Verify ErgonBase uses HarmoniaServiceIdentities.PRINCIPAL_PONOS_PROCESS for child task security propagation
        Path ergonBasePath = projectRoot.resolve("energeia/erga/src/main/java/net/fhirfactory/harmonia/erga/base/ErgonBase.java");
        assertThat(Files.exists(ergonBasePath)).isTrue();
        String ergonBaseContent = Files.readString(ergonBasePath);
        assertThat(ergonBaseContent).contains("HarmoniaServiceIdentities.PRINCIPAL_PONOS_PROCESS");
    }

    @Test
    @DisplayName("Guardrail Check: All Iris BEFE Clinical FHIR resources reside within the centralized /api/fhir/* Themis security perimeter")
    void irisClinicalFhirEndpointsMustBeProtectedByCommonAuthorizationFilter() throws IOException {
        Path projectRoot = findProjectRoot();

        // 1. Verify ThemisClinicalAuthorizationFilter exists and declares provider & pre-matching contracts
        Path filterPath = projectRoot.resolve("iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/security/ThemisClinicalAuthorizationFilter.java");
        assertThat(Files.exists(filterPath))
                .as("ThemisClinicalAuthorizationFilter must exist to protect /api/fhir/*")
                .isTrue();

        String filterContent = Files.readString(filterPath);
        assertThat(filterContent)
                .as("ThemisClinicalAuthorizationFilter must implement ContainerRequestFilter")
                .contains("implements ContainerRequestFilter");
        assertThat(filterContent)
                .as("ThemisClinicalAuthorizationFilter must be annotated with @Provider")
                .contains("@Provider");
        assertThat(filterContent)
                .as("ThemisClinicalAuthorizationFilter must be annotated with @PreMatching")
                .contains("@PreMatching");

        // 2. Inspect all BEFE REST resource classes
        Path restDir = projectRoot.resolve("iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/rest");
        if (Files.exists(restDir)) {
            Pattern pathPattern = Pattern.compile("@Path\\(\"([^\"]+)\"\\)");
            try (Stream<Path> paths = Files.walk(restDir)) {
                List<Path> javaFiles = paths.filter(p -> p.toString().endsWith("Resource.java")).toList();
                assertThat(javaFiles).isNotEmpty();

                for (Path javaFile : javaFiles) {
                    String fileName = javaFile.getFileName().toString();
                    if (fileName.equals("OperationsResource.java") || fileName.equals("SystemStatusResource.java") || fileName.equals("TaskSequenceResource.java")) {
                        continue; // Non-clinical / operations endpoints
                    }

                    String content = Files.readString(javaFile);
                    Matcher matcher = pathPattern.matcher(content);
                    assertThat(matcher.find())
                            .as("Resource class %s must declare a @Path annotation", fileName)
                            .isTrue();

                    String pathValue = matcher.group(1);
                    assertThat(pathValue)
                            .as("Clinical FHIR resource %s must be routed under /fhir/* to ensure centralized Themis authorization enforcement", fileName)
                            .startsWith("/fhir/");
                }
            }
        }
    }

    @Test
    @DisplayName("Architecture Check: Service-only modules must not register CORS filters or interceptors and BEFE must not permit wildcard CORS")
    void serviceOnlyModulesMustNotRegisterCorsAndBefeMustNotUseWildcardCors() throws IOException {
        Path projectRoot = findProjectRoot();

        // 1. Verify CorsFilter files do not exist in service-only modules
        Path pylaiInCorsFilter = projectRoot.resolve("pylai/pylai-mllp-in/src/main/java/net/fhirfactory/harmonia/mllpgateway/config/CorsFilter.java");
        assertThat(Files.exists(pylaiInCorsFilter))
                .as("pylai-mllp-in must not contain CorsFilter.java")
                .isFalse();

        Path pylaiOutCorsFilter = projectRoot.resolve("pylai/pylai-mllp-out/src/main/java/net/fhirfactory/harmonia/mllpout/config/CorsFilter.java");
        assertThat(Files.exists(pylaiOutCorsFilter))
                .as("pylai-mllp-out must not contain CorsFilter.java")
                .isFalse();

        Path ponosCorsFilter = projectRoot.resolve("energeia/ponos/src/main/java/net/fhirfactory/harmonia/praxis/rest/CorsFilter.java");
        assertThat(Files.exists(ponosCorsFilter))
                .as("energeia-ponos must not contain CorsFilter.java")
                .isFalse();

        // 2. Verify no CORS providers or filters exist across service-only source trees
        List<Path> serviceDirs = List.of(
                projectRoot.resolve("pylai/pylai-mllp-in/src/main/java"),
                projectRoot.resolve("pylai/pylai-mllp-out/src/main/java"),
                projectRoot.resolve("energeia/ponos/src/main/java")
        );

        for (Path serviceDir : serviceDirs) {
            if (Files.exists(serviceDir)) {
                try (Stream<Path> paths = Files.walk(serviceDir)) {
                    List<Path> javaFiles = paths.filter(p -> p.toString().endsWith(".java")).toList();
                    for (Path javaFile : javaFiles) {
                        String content = Files.readString(javaFile);
                        assertThat(content)
                                .as("Service-only class %s must not declare CORS filter logic or CORS headers", javaFile.getFileName())
                                .doesNotContain("Access-Control-Allow-Origin")
                                .doesNotContain("CorsFilter");
                    }
                }
            }
        }

        // 3. Mnemosyne clinical JpaRestfulServer must not register CorsInterceptor or CorsConfiguration
        Path jpaServerPath = projectRoot.resolve("hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/config/JpaRestfulServer.java");
        assertThat(Files.exists(jpaServerPath)).isTrue();
        String jpaServerContent = Files.readString(jpaServerPath);
        assertThat(jpaServerContent)
                .as("JpaRestfulServer must not register CorsInterceptor or CorsConfiguration")
                .doesNotContain("CorsInterceptor")
                .doesNotContain("CorsConfiguration");

        // 4. Iris BEFE must not permit unconstrained wildcard CORS or wildcard-plus-credentials once hardened
        Path befeCorsConfigPath = projectRoot.resolve("iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/config/BefeCorsConfig.java");
        Path befeCorsFilterPath = projectRoot.resolve("iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/config/CorsFilter.java");
        Path operationsServerPath = projectRoot.resolve("iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/server/OperationsServerManager.java");

        if (Files.exists(befeCorsConfigPath)) {
            if (Files.exists(befeCorsFilterPath)) {
                String filterContent = Files.readString(befeCorsFilterPath);
                assertThat(filterContent)
                        .as("BEFE CorsFilter must not emit wildcard CORS origin")
                        .doesNotContain("\"*\"")
                        .doesNotContain("Access-Control-Allow-Origin\", \"*\"")
                        .doesNotContain("Access-Control-Allow-Credentials\", \"true\"");
            }
            if (Files.exists(operationsServerPath)) {
                String opsContent = Files.readString(operationsServerPath);
                assertThat(opsContent)
                        .as("BEFE OperationsServerManager must not emit wildcard CORS origin")
                        .doesNotContain("Access-Control-Allow-Origin: *");
            }
        }
    }

    @Test
    @DisplayName("Architecture Check: Production message processing classes must not use System.out or System.err")
    void productionProcessingClassesMustNotUseSystemOutOrErr() throws IOException {
        Path projectRoot = findProjectRoot();
        List<Path> targetDirs = List.of(
                projectRoot.resolve("energeia/ponos/src/main/java"),
                projectRoot.resolve("pylai/pylai-mllp-in/src/main/java")
        );

        for (Path targetDir : targetDirs) {
            assertThat(Files.exists(targetDir))
                    .as("Processing directory %s must exist", targetDir)
                    .isTrue();

            try (Stream<Path> paths = Files.walk(targetDir)) {
                List<Path> javaFiles = paths.filter(p -> p.toString().endsWith(".java")).toList();
                assertThat(javaFiles).as("Must find Java source files in %s", targetDir).isNotEmpty();

                for (Path javaFile : javaFiles) {
                    String content = Files.readString(javaFile);
                    assertThat(content)
                            .as("Production class %s must not invoke System.out", javaFile.getFileName())
                            .doesNotContain("System.out.println")
                            .doesNotContain("System.out.print");
                    assertThat(content)
                            .as("Production class %s must not invoke System.err", javaFile.getFileName())
                            .doesNotContain("System.err.println")
                            .doesNotContain("System.err.print");
                }
            }
        }
    }

    @Test
    @DisplayName("Architecture Check: ThemisPrincipal and ThemisSecurityContext must use safe string representations")
    void securityContextAndPrincipalMustUseSafeStringRepresentation() throws IOException {
        Path projectRoot = findProjectRoot();

        // 1. Source-level check: Verify explicit toString() overrides without raw collection dumps
        Path principalSourcePath = projectRoot.resolve("themis/themis-api/src/main/java/net/fhirfactory/harmonia/themis/api/model/ThemisPrincipal.java");
        Path contextSourcePath = projectRoot.resolve("themis/themis-api/src/main/java/net/fhirfactory/harmonia/themis/api/model/ThemisSecurityContext.java");

        assertThat(Files.exists(principalSourcePath)).isTrue();
        assertThat(Files.exists(contextSourcePath)).isTrue();

        String principalSource = Files.readString(principalSourcePath);
        assertThat(principalSource)
                .as("ThemisPrincipal must explicitly override toString() to prevent record attribute dump")
                .contains("@Override")
                .contains("public String toString()")
                .contains("attributeCount=")
                .doesNotContain("+ attributes +")
                .doesNotContain("+ attributes.toString()");

        String contextSource = Files.readString(contextSourcePath);
        assertThat(contextSource)
                .as("ThemisSecurityContext must explicitly override toString() to prevent record authority/attribute dump")
                .contains("@Override")
                .contains("public String toString()")
                .contains("authoritiesCount=")
                .contains("attributeCount=")
                .doesNotContain("+ authorities +")
                .doesNotContain("+ attributes +");

        // 2. Behavioral check: Synthetic secrets and authority codes must not leak into toString()
        ThemisPrincipal principal = new ThemisPrincipal(
                "user:test-subject",
                PrincipalType.HUMAN,
                "clinical-domain",
                Map.of("secret-token", "TOKEN-SECRET-MARKER-81742", "patient-id", "PATIENT-PHI-MARKER-92831")
        );
        String principalString = principal.toString();
        assertThat(principalString)
                .contains("user:test-subject")
                .contains("attributeCount=2")
                .doesNotContain("TOKEN-SECRET-MARKER-81742")
                .doesNotContain("PATIENT-PHI-MARKER-92831")
                .doesNotContain("secret-token");

        ThemisSecurityContext context = ThemisSecurityContext.builder()
                .requestingPrincipal(principal)
                .executingPrincipal(ThemisPrincipal.service("service:worker"))
                .securityDomain("test-domain")
                .correlationId("corr-123")
                .causationId("caus-456")
                .tenantId("tenant-789")
                .authorities(Set.of(ThemisAuthority.of("SECRET-AUTHORITY-CODE-9999")))
                .addAttribute("context-token", "TOKEN-SECRET-MARKER-81742")
                .build();
        String contextString = context.toString();
        assertThat(contextString)
                .contains("corr-123")
                .contains("authoritiesCount=1")
                .contains("attributeCount=1")
                .doesNotContain("SECRET-AUTHORITY-CODE-9999")
                .doesNotContain("TOKEN-SECRET-MARKER-81742")
                .doesNotContain("context-token");
    }

    @Test
    @DisplayName("Architecture Check: OperationsRestClient must not log raw response body content")
    void operationsRestClientMustNotLogRawResponseBody() throws IOException {
        Path projectRoot = findProjectRoot();
        Path clientSourcePath = projectRoot.resolve("hestia/mneme-persistence/src/main/java/net/fhirfactory/harmonia/persistence/client/OperationsRestClient.java");
        assertThat(Files.exists(clientSourcePath)).isTrue();

        String clientSource = Files.readString(clientSourcePath);

        // Verify response.body() is never passed to log statements
        Matcher matcher = Pattern.compile("log\\.(trace|debug|info|warn|error)\\([^;]*response\\.body\\(\\)", Pattern.DOTALL).matcher(clientSource);
        assertThat(matcher.find())
                .as("OperationsRestClient must not log raw HTTP response.body() content")
                .isFalse();

        // Operational logs emit structured category/status metadata instead
        assertThat(clientSource)
                .contains("categorizeHttpStatus(")
                .contains("status=")
                .contains("category=");
    }

    @Test
    @DisplayName("Architecture Check: Production Logback configurations must maintain safe defaults and isolated PHI diagnostic logger")
    void productionLogbackConfigurationsMaintainSafeDefaultsAndIsolatedPhiLogger() throws IOException {
        Path projectRoot = findProjectRoot();
        List<Path> logbackConfigs = List.of(
                projectRoot.resolve("pylai/pylai-mllp-cli/src/main/resources/logback.xml"),
                projectRoot.resolve("energeia/ponos-cli/src/main/resources/logback.xml"),
                projectRoot.resolve("hestia/mnemosyne-operations-cli/src/main/resources/logback.xml")
        );

        for (Path configPath : logbackConfigs) {
            assertThat(Files.exists(configPath))
                    .as("Logback config %s must exist", configPath)
                    .isTrue();

            String configContent = Files.readString(configPath);

            // PHI diagnostic logger must be configured at DEBUG with additivity="false"
            assertThat(configContent)
                    .as("Config %s must define org.harmonia.phi logger with additivity='false'", configPath.getFileName())
                    .contains("<logger name=\"org.harmonia.phi\" level=\"DEBUG\" additivity=\"false\">");

            // Must reference dedicated PHI_DIAGNOSTIC appender
            assertThat(configContent)
                    .as("Config %s must attach PHI_DIAGNOSTIC appender to org.harmonia.phi", configPath.getFileName())
                    .contains("<appender-ref ref=\"PHI_DIAGNOSTIC\" />");

            // Root level must not be verbose (DEBUG or TRACE)
            assertThat(configContent)
                    .as("Config %s must not set root logger to DEBUG or TRACE", configPath.getFileName())
                    .doesNotContain("<root level=\"DEBUG\">")
                    .doesNotContain("<root level=\"TRACE\">");

            // Must not contain verbose wire or payload logging for frameworks
            assertThat(configContent)
                    .as("Config %s must not enable verbose framework payload/wire logging", configPath.getFileName())
                    .doesNotContain("org.apache.activemq")
                    .doesNotContain("org.apache.camel")
                    .doesNotContain("org.hibernate")
                    .doesNotContain("org.infinispan")
                    .doesNotContain("ca.uhn.fhir")
                    .doesNotContain("net.fhirfactory.harmonia.agora.matrix");
        }
    }

    @Test
    @DisplayName("Architecture Check: PhiLoggingConfig defaults strictly to disabled in runtime environment")
    void phiLoggingConfigDefaultsToDisabled() {
        PhiLoggingConfig.reset();
        assertThat(PhiLoggingConfig.isPhiEnabled())
                .as("PhiLoggingConfig.isPhiEnabled() must default strictly to false")
                .isFalse();
    }

    private static final String KLEIO_AUDIT_MODEL_PACKAGE = "net.fhirfactory.harmonia.kleio.audit.model..";

    private static final List<Class<?>> KLEIO_AUDIT_MODEL_CLASSES = List.of(
            HarmoniaAuditEvent.class,
            AuditClassification.class,
            AuditAction.class,
            AuditOutcome.class,
            AuditTarget.class,
            AuditSource.class,
            AuditAuthorizationEvidence.class,
            AuditPrincipal.class
    );

    @Test
    @DisplayName("Architecture Check: Canonical audit model resides under package net.fhirfactory.harmonia.kleio.audit.model")
    void canonicalAuditModelResidesUnderKleioPackage() {
        for (Class<?> clazz : KLEIO_AUDIT_MODEL_CLASSES) {
            assertThat(clazz.getPackageName())
                    .as("Canonical audit model class %s must reside in net.fhirfactory.harmonia.kleio.audit.model", clazz.getSimpleName())
                    .isEqualTo("net.fhirfactory.harmonia.kleio.audit.model");
        }
        assertThat(AuditService.class.getPackageName())
                .as("AuditService interface must reside in net.fhirfactory.harmonia.kleio.audit.service")
                .isEqualTo("net.fhirfactory.harmonia.kleio.audit.service");
    }

    @Test
    @DisplayName("Architecture Check: HarmoniaAuditEvent and audit models must have zero dependencies on HAPI/FHIR")
    void kleioAuditModelMustNotDependOnHapiOrFhir() throws IOException {
        JavaClasses classes = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("net.fhirfactory.harmonia.kleio.audit.model");
        assertThat(classes).isNotEmpty();

        ArchRule rule = noClasses()
                .that().resideInAPackage(KLEIO_AUDIT_MODEL_PACKAGE)
                .should().dependOnClassesThat()
                .resideInAnyPackage("ca.uhn.fhir..", "org.hl7.fhir..");

        rule.check(classes);

        assertAuditModelSourceFilesDoNotContainImports(
                "import ca.uhn.fhir",
                "import org.hl7.fhir"
        );
        assertAuditModelReflectionDoesNotReferencePrefixes(
                "ca.uhn.fhir",
                "org.hl7.fhir"
        );
    }

    @Test
    @DisplayName("Architecture Check: HarmoniaAuditEvent and audit models must have zero dependencies on JPA")
    void kleioAuditModelMustNotDependOnJpa() throws IOException {
        JavaClasses classes = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("net.fhirfactory.harmonia.kleio.audit.model");
        assertThat(classes).isNotEmpty();

        ArchRule rule = noClasses()
                .that().resideInAPackage(KLEIO_AUDIT_MODEL_PACKAGE)
                .should().dependOnClassesThat()
                .resideInAnyPackage("jakarta.persistence..", "javax.persistence..");

        rule.check(classes);

        assertAuditModelSourceFilesDoNotContainImports(
                "import jakarta.persistence",
                "import javax.persistence"
        );
        assertAuditModelReflectionDoesNotReferencePrefixes(
                "jakarta.persistence",
                "javax.persistence"
        );
    }

    @Test
    @DisplayName("Architecture Check: HarmoniaAuditEvent and audit models must have zero dependencies on JMS or ActiveMQ Artemis")
    void kleioAuditModelMustNotDependOnJmsOrArtemis() throws IOException {
        JavaClasses classes = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("net.fhirfactory.harmonia.kleio.audit.model");
        assertThat(classes).isNotEmpty();

        ArchRule rule = noClasses()
                .that().resideInAPackage(KLEIO_AUDIT_MODEL_PACKAGE)
                .should().dependOnClassesThat()
                .resideInAnyPackage("jakarta.jms..", "javax.jms..", "org.apache.activemq..");

        rule.check(classes);

        assertAuditModelSourceFilesDoNotContainImports(
                "import jakarta.jms",
                "import javax.jms",
                "import org.apache.activemq"
        );
        assertAuditModelReflectionDoesNotReferencePrefixes(
                "jakarta.jms",
                "javax.jms",
                "org.apache.activemq"
        );
    }

    @Test
    @DisplayName("Architecture Check: HarmoniaAuditEvent and audit models must have zero dependencies on Jakarta SecurityContext")
    void kleioAuditModelMustNotDependOnJakartaSecurityContext() throws IOException {
        JavaClasses classes = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("net.fhirfactory.harmonia.kleio.audit.model");
        assertThat(classes).isNotEmpty();

        ArchRule rule = noClasses()
                .that().resideInAPackage(KLEIO_AUDIT_MODEL_PACKAGE)
                .should().dependOnClassesThat()
                .resideInAnyPackage("jakarta.security.enterprise..", "jakarta.ws.rs..");

        rule.check(classes);

        assertAuditModelSourceFilesDoNotContainImports(
                "import jakarta.security.enterprise",
                "import jakarta.ws.rs.core.SecurityContext",
                "import jakarta.ws.rs"
        );
        assertAuditModelReflectionDoesNotReferencePrefixes(
                "jakarta.security.enterprise",
                "jakarta.ws.rs.core.SecurityContext",
                "jakarta.ws.rs"
        );
    }

    @Test
    @DisplayName("Architecture Check: AuditService must enforce append-only semantics without mutation methods")
    void auditServiceMustNotContainMutationMethods() {
        assertThat(AuditService.class).isInterface();

        Method[] methods = AuditService.class.getMethods();
        assertThat(methods).isNotEmpty();

        List<String> mutationPrefixes = List.of("update", "delete", "replace", "patch");
        for (Method method : methods) {
            String name = method.getName();
            for (String prefix : mutationPrefixes) {
                assertThat(name)
                        .as("AuditService interface method '%s' must not start with mutation prefix '%s'", name, prefix)
                        .doesNotStartWith(prefix);
            }
            assertThat(name)
                    .as("AuditService interface must not declare 'clear' mutation method")
                    .isNotEqualTo("clear");
        }
    }

    @Test
    @DisplayName("Architecture Check: HarmoniaAuditEvent must use safe string representation suppressing raw authorities and secrets")
    void harmoniaAuditEventMustUseSafeStringRepresentation() throws IOException {
        Path projectRoot = findProjectRoot();
        Path eventSourcePath = projectRoot.resolve("kleio/kleio-core/src/main/java/net/fhirfactory/harmonia/kleio/audit/model/HarmoniaAuditEvent.java");
        assertThat(Files.exists(eventSourcePath)).isTrue();

        String eventSource = Files.readString(eventSourcePath);
        assertThat(eventSource)
                .as("HarmoniaAuditEvent must explicitly override toString() to prevent raw authority and attribute dump")
                .contains("@Override")
                .contains("public String toString()")
                .contains("authoritiesCount=")
                .contains("attributeCount=")
                .doesNotContain("+ authorities +")
                .doesNotContain("+ attributes +");

        HarmoniaAuditEvent event = HarmoniaAuditEvent.builder()
                .eventId("arch-event-001")
                .recordedAt(java.time.Instant.now())
                .classification(AuditClassification.SECURITY)
                .action(AuditAction.READ)
                .outcome(AuditOutcome.SUCCESS)
                .authorities(Set.of(ThemisAuthority.of("SECRET-AUTHORITY-CODE-9999")))
                .attributes(Map.of(
                        "secret-token", "TOKEN-SECRET-MARKER-81742",
                        "patient-id", "PATIENT-PHI-MARKER-92831"
                ))
                .build();

        String eventString = event.toString();
        assertThat(eventString)
                .contains("arch-event-001")
                .contains("authoritiesCount=1")
                .contains("attributeCount=2")
                .doesNotContain("SECRET-AUTHORITY-CODE-9999")
                .doesNotContain("TOKEN-SECRET-MARKER-81742")
                .doesNotContain("PATIENT-PHI-MARKER-92831")
                .doesNotContain("secret-token");
    }

    private static JavaClasses harmoniaClasses;

    private static synchronized JavaClasses getHarmoniaClasses() {
        if (harmoniaClasses == null) {
            harmoniaClasses = new ClassFileImporter()
                    .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                    .importPackages("net.fhirfactory.harmonia");
        }
        return harmoniaClasses;
    }

    @Test
    @DisplayName("Architecture Check: No canonical ThemisAuditEvent or ThemisAuditService classes exist in the repository")
    void noCanonicalThemisAuditClassesExist() {
        JavaClasses classes = getHarmoniaClasses();

        ArchRule rule = noClasses()
                .should().haveSimpleName("ThemisAuditEvent")
                .orShould().haveSimpleName("ThemisAuditService")
                .orShould().haveSimpleName("InMemoryThemisAuditService")
                .orShould().haveSimpleName("ThemisAuditIntegrityException");

        rule.check(classes);

        assertThatThrownBy(() -> Class.forName("net.fhirfactory.harmonia.themis.audit.model.ThemisAuditEvent"))
                .isInstanceOf(ClassNotFoundException.class);
        assertThatThrownBy(() -> Class.forName("net.fhirfactory.harmonia.themis.audit.service.ThemisAuditService"))
                .isInstanceOf(ClassNotFoundException.class);

        Path projectRoot = findProjectRoot();
        Path themisAuditDir = projectRoot.resolve("themis/themis-audit");
        assertThat(Files.exists(themisAuditDir))
                .as("themis/themis-audit module directory must not exist")
                .isFalse();
    }

    @Test
    @DisplayName("Architecture Check: Themis API and Themis Core must not depend on Kleio")
    void themisMustNotDependOnKleio() {
        JavaClasses classes = getHarmoniaClasses();

        ArchRule rule = noClasses()
                .that().resideInAnyPackage(
                        "net.fhirfactory.harmonia.themis.api..",
                        "net.fhirfactory.harmonia.themis.core.."
                )
                .should().dependOnClassesThat()
                .resideInAPackage("net.fhirfactory.harmonia.kleio..");

        rule.check(classes);
    }

    @Test
    @DisplayName("Architecture Check: Kleio depends only on Themis API and has zero dependencies on Themis Core or other subsystems")
    void kleioMustOnlyDependOnThemisApiAndNotThemisCore() {
        JavaClasses classes = getHarmoniaClasses();

        ArchRule noThemisCoreRule = noClasses()
                .that().resideInAPackage("net.fhirfactory.harmonia.kleio..")
                .should().dependOnClassesThat()
                .resideInAPackage("net.fhirfactory.harmonia.themis.core..");

        noThemisCoreRule.check(classes);

        ArchRule noOtherHarmoniaSubprojectsRule = noClasses()
                .that().resideInAPackage("net.fhirfactory.harmonia.kleio..")
                .should().dependOnClassesThat()
                .resideInAnyPackage(
                        "net.fhirfactory.harmonia.themis.core..",
                        "net.fhirfactory.harmonia.calliope..",
                        "net.fhirfactory.harmonia.hestia..",
                        "net.fhirfactory.harmonia.petasos..",
                        "net.fhirfactory.harmonia.erga..",
                        "net.fhirfactory.harmonia.praxis..",
                        "net.fhirfactory.harmonia.ponos..",
                        "net.fhirfactory.harmonia.mllpgateway..",
                        "net.fhirfactory.harmonia.gateway..",
                        "net.fhirfactory.harmonia.iris..",
                        "net.fhirfactory.harmonia.agora..",
                        "net.fhirfactory.harmonia.paradeigma.."
                );

        noOtherHarmoniaSubprojectsRule.check(classes);
    }

    @Test
    @DisplayName("Architecture Check: Zero duplicate canonical audit models exist across Themis and Kleio")
    void zeroDuplicateCanonicalAuditModelsAcrossThemisAndKleio() {
        JavaClasses classes = getHarmoniaClasses();

        ArchRule noThemisAuditPackageRule = noClasses()
                .should().resideInAPackage("net.fhirfactory.harmonia.themis.audit..");
        noThemisAuditPackageRule.check(classes);

        ArchRule canonicalAuditModelExclusivityRule = classes()
                .that().haveSimpleName("HarmoniaAuditEvent")
                .or().haveSimpleName("AuditClassification")
                .or().haveSimpleName("AuditAction")
                .or().haveSimpleName("AuditOutcome")
                .or().haveSimpleName("AuditTarget")
                .or().haveSimpleName("AuditSource")
                .or().haveSimpleName("AuditAuthorizationEvidence")
                .or().haveSimpleName("AuditPrincipal")
                .or().haveSimpleName("AuditService")
                .or().haveSimpleName("InMemoryAuditService")
                .or().haveSimpleName("AuditIntegrityException")
                .should().resideInAnyPackage(
                        "net.fhirfactory.harmonia.kleio.audit.model..",
                        "net.fhirfactory.harmonia.kleio.audit.service.."
                );
        canonicalAuditModelExclusivityRule.check(classes);

        ArchRule noAuditModelInThemis = noClasses()
                .that().resideInAPackage("net.fhirfactory.harmonia.themis..")
                .should().haveSimpleName("HarmoniaAuditEvent")
                .orShould().haveSimpleName("ThemisAuditEvent")
                .orShould().haveSimpleName("AuditClassification")
                .orShould().haveSimpleName("ThemisAuditClassification")
                .orShould().haveSimpleName("AuditAction")
                .orShould().haveSimpleName("ThemisAuditAction")
                .orShould().haveSimpleName("AuditOutcome")
                .orShould().haveSimpleName("ThemisAuditOutcome")
                .orShould().haveSimpleName("AuditTarget")
                .orShould().haveSimpleName("ThemisAuditTarget")
                .orShould().haveSimpleName("AuditSource")
                .orShould().haveSimpleName("ThemisAuditSource")
                .orShould().haveSimpleName("AuditAuthorizationEvidence")
                .orShould().haveSimpleName("ThemisAuditAuthorizationEvidence")
                .orShould().haveSimpleName("AuditPrincipal")
                .orShould().haveSimpleName("ThemisAuditPrincipal")
                .orShould().haveSimpleName("AuditService")
                .orShould().haveSimpleName("ThemisAuditService");
        noAuditModelInThemis.check(classes);
    }

    @Test
    @DisplayName("Architecture Check: Kleio subproject boundary isolation and persistence layering")
    void kleioSubprojectIsolationAndPersistenceLayering() {
        JavaClasses classes = getHarmoniaClasses();

        // Rule 1: Kleio Core Independence
        ArchRule kleioCoreIndependenceRule = noClasses()
                .that().resideInAPackage("net.fhirfactory.harmonia.kleio.audit..")
                .should().dependOnClassesThat()
                .resideInAnyPackage(
                        "jakarta.persistence..",
                        "java.sql..",
                        "javax.sql..",
                        "org.springframework..",
                        "ca.uhn.fhir..",
                        "org.hl7.fhir.."
                );
        kleioCoreIndependenceRule.check(classes);

        // Rule 2: Kleio FHIR Independence
        ArchRule kleioFhirIndependenceRule = noClasses()
                .that().resideInAPackage("net.fhirfactory.harmonia.kleio.fhir..")
                .should().dependOnClassesThat()
                .resideInAnyPackage(
                        "jakarta.persistence..",
                        "java.sql..",
                        "javax.sql..",
                        "org.springframework.."
                );
        kleioFhirIndependenceRule.check(classes);

        // Rule 3: Kleio Persistence Layering & Jakarta EE Compliance
        ArchRule kleioPersistenceLayeringRule = noClasses()
                .that().resideInAPackage("net.fhirfactory.harmonia.kleio.persistence..")
                .should().dependOnClassesThat()
                .resideInAnyPackage(
                        "org.springframework..",
                        "net.fhirfactory.harmonia.hestia..",
                        "net.fhirfactory.harmonia.iris..",
                        "net.fhirfactory.harmonia.petasos..",
                        "net.fhirfactory.harmonia.themis.core.."
                );
        kleioPersistenceLayeringRule.check(classes);

        // Rule 4: No External Dependents on Kleio Persistence from Themis
        ArchRule noThemisDependOnKleioPersistenceRule = noClasses()
                .that().resideInAPackage("net.fhirfactory.harmonia.themis..")
                .should().dependOnClassesThat()
                .resideInAPackage("net.fhirfactory.harmonia.kleio.persistence..");
        noThemisDependOnKleioPersistenceRule.check(classes);
    }

    @Test
    @DisplayName("Architecture Check: AuditImmutabilityDenyPolicy is registered in default evaluator and enforces explicit-deny for AUDIT domain")
    void auditImmutabilityDenyPolicyRegisteredInDefaultEvaluator() {
        DeterministicPolicyEvaluator evaluator = DeterministicPolicyEvaluator.withDefaultPolicies();
        List<ThemisPolicy> registered = evaluator.getRegisteredPolicies();

        assertThat(registered)
                .anyMatch(p -> p instanceof AuditImmutabilityDenyPolicy);

        AuditImmutabilityDenyPolicy policy = registered.stream()
                .filter(p -> p instanceof AuditImmutabilityDenyPolicy)
                .map(p -> (AuditImmutabilityDenyPolicy) p)
                .findFirst()
                .orElseThrow();

        assertThat(policy.isExplicitDeny()).isTrue();
        assertThat(policy.getOrder()).isEqualTo(10);
        assertThat(policy.getPolicyId()).isEqualTo("audit-immutability-deny-policy");
    }

    @Test
    @DisplayName("Guardrail Check: Iris BEFE AuditEvent endpoint is protected by ThemisClinicalAuthorizationFilter without local bypasses")
    void irisBefeAuditEventProtectedByThemisClinicalAuthorizationFilter() throws IOException {
        Path projectRoot = findProjectRoot();

        // 1. AuditEventResource must declare @Path("/fhir/AuditEvent")
        Path auditResourcePath = projectRoot.resolve("iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/rest/AuditEventResource.java");
        assertThat(Files.exists(auditResourcePath))
                .as("AuditEventResource must exist")
                .isTrue();

        String auditResourceContent = Files.readString(auditResourcePath);
        assertThat(auditResourceContent)
                .contains("@Path(\"/fhir/AuditEvent\")");

        // 2. ThemisClinicalAuthorizationFilter must classify AuditEvent as AUDIT domain/label
        Path filterPath = projectRoot.resolve("iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/security/ThemisClinicalAuthorizationFilter.java");
        assertThat(Files.exists(filterPath)).isTrue();
        String filterContent = Files.readString(filterPath);
        assertThat(filterContent)
                .contains("\"AuditEvent\".equalsIgnoreCase(resourceType)")
                .contains("HarmoniaSecurityLabelEnum.AUDIT")
                .contains("HarmoniaSecurityLabelEnum.CLINICAL");

        // 3. Iris BEFE package must not contain local ThemisPolicy implementations or audit authorization bypasses
        Path befeSrc = projectRoot.resolve("iris/iris-befe/src/main/java");
        assertThat(Files.exists(befeSrc)).isTrue();
        try (Stream<Path> paths = Files.walk(befeSrc)) {
            List<Path> javaFiles = paths.filter(p -> p.toString().endsWith(".java")).toList();
            assertThat(javaFiles).isNotEmpty();
            for (Path javaFile : javaFiles) {
                String content = Files.readString(javaFile);
                assertThat(content)
                        .as("iris-befe source %s must not implement ThemisPolicy directly", javaFile.getFileName())
                        .doesNotContain("implements ThemisPolicy")
                        .doesNotContain("implements net.fhirfactory.harmonia.themis.api.policy.ThemisPolicy");
            }
        }
    }

    @Test
    @DisplayName("Architecture Check: Mnemosyne must not expose AuditEvent resource provider or mutable AuditEvent methods")
    void mnemosyneMustNotExposeMutableAuditEventProviders() throws IOException {
        JavaClasses classes = getHarmoniaClasses();

        // 1. ArchUnit: No AuditEventResourceProvider class in hapifhir package
        ArchRule noAuditEventProviderRule = noClasses()
                .that().resideInAPackage("net.fhirfactory.harmonia.hapifhir..")
                .should().haveSimpleName("AuditEventResourceProvider");
        noAuditEventProviderRule.check(classes);

        // 2. ArchUnit: No IResourceProvider in hapifhir package depends on AuditEvent
        ArchRule noIResourceProviderForAuditEvent = noClasses()
                .that().resideInAPackage("net.fhirfactory.harmonia.hapifhir..")
                .and().implement("ca.uhn.fhir.rest.server.IResourceProvider")
                .should().dependOnClassesThat()
                .haveFullyQualifiedName("org.hl7.fhir.r5.model.AuditEvent");
        noIResourceProviderForAuditEvent.check(classes);

        // 3. Class.forName verification
        assertThatThrownBy(() -> Class.forName("net.fhirfactory.harmonia.hapifhir.provider.AuditEventResourceProvider"))
                .isInstanceOf(ClassNotFoundException.class);

        // 4. File-level check verifying AuditEventResourceProvider.java is absent
        Path projectRoot = findProjectRoot();
        Path providerPath = projectRoot.resolve("hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/provider/AuditEventResourceProvider.java");
        assertThat(Files.exists(providerPath))
                .as("AuditEventResourceProvider.java must not exist in mnemosyne-clinical")
                .isFalse();

        // 5. Verification that no provider classes in hapifhir expose mutable annotations (@Create, @Update, @Delete, @Patch) for AuditEvent
        Path mnemosyneJavaSrc = projectRoot.resolve("hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/provider");
        assertThat(Files.exists(mnemosyneJavaSrc)).isTrue();
        try (Stream<Path> paths = Files.walk(mnemosyneJavaSrc)) {
            List<Path> javaFiles = paths.filter(p -> p.toString().endsWith(".java")).toList();
            assertThat(javaFiles).isNotEmpty();
            for (Path javaFile : javaFiles) {
                String content = Files.readString(javaFile);
                assertThat(content)
                        .as("Provider source %s must not reference AuditEvent", javaFile.getFileName())
                        .doesNotContain("AuditEvent");
            }
        }
    }

    @Test
    @DisplayName("Architecture Check: Mnemosyne clinical and hapifhir packages must have zero dependencies on Kleio")
    void mnemosyneMustNotDependOnKleio() throws IOException {
        JavaClasses classes = getHarmoniaClasses();

        // 1. ArchUnit rule: hapifhir / hestia packages must not depend on kleio
        ArchRule noKleioRule = noClasses()
                .that().resideInAPackage("net.fhirfactory.harmonia.hapifhir..")
                .or().resideInAPackage("net.fhirfactory.harmonia.hestia..")
                .should().dependOnClassesThat()
                .resideInAPackage("net.fhirfactory.harmonia.kleio..");
        noKleioRule.check(classes);

        // 2. Source-level check: mnemosyne-clinical source files must not import kleio
        Path projectRoot = findProjectRoot();
        Path mnemosyneSrc = projectRoot.resolve("hestia/mnemosyne-clinical/src/main/java");
        assertThat(Files.exists(mnemosyneSrc)).isTrue();
        try (Stream<Path> paths = Files.walk(mnemosyneSrc)) {
            List<Path> javaFiles = paths.filter(p -> p.toString().endsWith(".java")).toList();
            assertThat(javaFiles).isNotEmpty();
            for (Path javaFile : javaFiles) {
                String content = Files.readString(javaFile);
                assertThat(content)
                        .as("mnemosyne-clinical source %s must not import net.fhirfactory.harmonia.kleio", javaFile.getFileName())
                        .doesNotContain("net.fhirfactory.harmonia.kleio");
            }
        }
    }

    private void assertAuditModelSourceFilesDoNotContainImports(String... forbiddenImports) throws IOException {
        Path projectRoot = findProjectRoot();
        Path modelSrcDir = projectRoot.resolve("kleio/kleio-core/src/main/java/net/fhirfactory/harmonia/kleio/audit/model");
        assertThat(Files.exists(modelSrcDir))
                .as("kleio-core model directory %s must exist", modelSrcDir)
                .isTrue();

        try (Stream<Path> paths = Files.walk(modelSrcDir)) {
            List<Path> javaFiles = paths.filter(p -> p.toString().endsWith(".java")).toList();
            assertThat(javaFiles).isNotEmpty();

            for (Path javaFile : javaFiles) {
                String content = Files.readString(javaFile);
                for (String forbidden : forbiddenImports) {
                    assertThat(content)
                            .as("Audit model file %s must not contain forbidden import '%s'", javaFile.getFileName(), forbidden)
                            .doesNotContain(forbidden);
                }
            }
        }
    }

    private void assertAuditModelReflectionDoesNotReferencePrefixes(String... forbiddenPrefixes) {
        for (Class<?> clazz : KLEIO_AUDIT_MODEL_CLASSES) {
            assertThat(clazz.getPackageName()).isEqualTo("net.fhirfactory.harmonia.kleio.audit.model");

            for (var field : clazz.getDeclaredFields()) {
                String typeName = field.getType().getName();
                for (String prefix : forbiddenPrefixes) {
                    assertThat(typeName)
                            .as("Class %s field %s has forbidden type %s", clazz.getSimpleName(), field.getName(), typeName)
                            .doesNotStartWith(prefix);
                }
            }

            for (Method method : clazz.getDeclaredMethods()) {
                String returnTypeName = method.getReturnType().getName();
                for (String prefix : forbiddenPrefixes) {
                    assertThat(returnTypeName)
                            .as("Class %s method %s has forbidden return type %s", clazz.getSimpleName(), method.getName(), returnTypeName)
                            .doesNotStartWith(prefix);
                }
                for (Class<?> paramType : method.getParameterTypes()) {
                    String paramTypeName = paramType.getName();
                    for (String prefix : forbiddenPrefixes) {
                        assertThat(paramTypeName)
                                .as("Class %s method %s has forbidden parameter type %s", clazz.getSimpleName(), method.getName(), paramTypeName)
                                .doesNotStartWith(prefix);
                    }
                }
            }

            for (var constructor : clazz.getDeclaredConstructors()) {
                for (Class<?> paramType : constructor.getParameterTypes()) {
                    String paramTypeName = paramType.getName();
                    for (String prefix : forbiddenPrefixes) {
                        assertThat(paramTypeName)
                                .as("Class %s constructor has forbidden parameter type %s", clazz.getSimpleName(), paramTypeName)
                                .doesNotStartWith(prefix);
                    }
                }
            }
        }
    }

    private Path findProjectRoot() {
        Path current = Paths.get(".").toAbsolutePath().normalize();
        while (current != null) {
            if (Files.exists(current.resolve("pom.xml"))
                    && Files.exists(current.resolve("iris"))
                    && Files.exists(current.resolve("themis"))) {
                return current;
            }
            current = current.getParent();
        }
        throw new IllegalStateException("Could not determine Harmonia repository root from working directory: "
                + Paths.get(".").toAbsolutePath().normalize());
    }
}
