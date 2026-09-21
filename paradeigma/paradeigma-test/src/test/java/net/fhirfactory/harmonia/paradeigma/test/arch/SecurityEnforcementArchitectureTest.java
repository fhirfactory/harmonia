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

import net.fhirfactory.harmonia.model.security.FhirSecurityTagManager;
import net.fhirfactory.harmonia.themis.api.ThemisService;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;
import net.fhirfactory.harmonia.themis.api.policy.ThemisPolicy;
import net.fhirfactory.harmonia.themis.audit.service.ThemisAuditService;
import net.fhirfactory.harmonia.themis.core.evaluator.DeterministicPolicyEvaluator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(ThemisAuditService.class).isInterface();
    }

    @Test
    @DisplayName("Architecture Check: Security context and FHIR security tag managers are accessible")
    void securityContextAndTagManagersDefined() {
        assertThat(ThemisSecurityContext.class).isNotNull();
        assertThat(FhirSecurityTagManager.class).isNotNull();
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
