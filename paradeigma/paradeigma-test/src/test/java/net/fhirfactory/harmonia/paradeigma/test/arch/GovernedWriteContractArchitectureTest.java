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

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Architectural tests enforcing that the foundational governed-write contracts
 * ({@code net.fhirfactory.harmonia.model.governedwrite..}) remain pure domain abstractions
 * completely free of Infinispan/Hot Rod, JPA/Hibernate, HTTP frameworks, and physical DELETE operations.
 */
public class GovernedWriteContractArchitectureTest {

    private static final String GOVERNED_WRITE_PACKAGE = "net.fhirfactory.harmonia.model.governedwrite..";

    private static JavaClasses importedClasses;

    @BeforeAll
    static void loadClasses() {
        importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("net.fhirfactory.harmonia.model.governedwrite");
    }

    @Test
    @DisplayName("ArchUnit: Governed-write contract classes must not depend on Infinispan or Hot Rod")
    void governedWriteMustNotDependOnInfinispan() {
        ArchRule rule = noClasses()
                .that().resideInAPackage(GOVERNED_WRITE_PACKAGE)
                .should().dependOnClassesThat()
                .resideInAnyPackage(
                        "org.infinispan..",
                        "org.infinispan.client.hotrod..",
                        "org.infinispan.commons.."
                );

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("ArchUnit: Governed-write contract classes must not depend on JPA, Hibernate, or database drivers")
    void governedWriteMustNotDependOnJpaOrDatabase() {
        ArchRule rule = noClasses()
                .that().resideInAPackage(GOVERNED_WRITE_PACKAGE)
                .should().dependOnClassesThat()
                .resideInAnyPackage(
                        "jakarta.persistence..",
                        "javax.persistence..",
                        "org.hibernate..",
                        "org.postgresql..",
                        "ca.uhn.fhir.jpa.."
                );

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("ArchUnit: Governed-write contract classes must not depend on HTTP/Web frameworks")
    void governedWriteMustNotDependOnHttpFrameworks() {
        ArchRule rule = noClasses()
                .that().resideInAPackage(GOVERNED_WRITE_PACKAGE)
                .should().dependOnClassesThat()
                .resideInAnyPackage(
                        "org.springframework.web..",
                        "jakarta.ws.rs..",
                        "javax.ws.rs..",
                        "jakarta.servlet..",
                        "javax.servlet..",
                        "org.apache.http..",
                        "org.apache.hc.."
                );

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("ArchUnit: Governed-write interfaces must not expose DELETE or REMOVE methods (ADR-020)")
    void governedWriteMustNotExposeDeleteMethods() {
        ArchRule rule = methods()
                .that().areDeclaredInClassesThat().resideInAPackage(GOVERNED_WRITE_PACKAGE)
                .should().haveNameNotMatching(".*[dD]elete.*")
                .andShould().haveNameNotMatching(".*[rR]emove.*");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("ArchUnit: ActiveStateTokenBridge is restricted strictly to Mneme infrastructure")
    void activeStateTokenBridgeAccessIsRestricted() {
        JavaClasses allProductionClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("net.fhirfactory.harmonia");

        ArchRule rule = noClasses()
                .that().resideOutsideOfPackages(
                        "net.fhirfactory.harmonia.model.governedwrite..",
                        "net.fhirfactory.harmonia.hestia.mneme.."
                )
                .should().dependOnClassesThat()
                .haveFullyQualifiedName("net.fhirfactory.harmonia.model.governedwrite.ActiveStateTokenBridge");

        rule.check(allProductionClasses);
    }

    @Test
    @DisplayName("ArchUnit: Active-state coordination must not use process-local concurrency or fallback mechanisms")
    void activeStateCoordinationMustNotUseProcessLocalFallback() {
        JavaClasses mnemeClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("net.fhirfactory.harmonia.hestia.mneme.coordination");

        ArchRule rule = noClasses()
                .that().resideInAPackage("net.fhirfactory.harmonia.hestia.mneme.coordination..")
                .should().dependOnClassesThat()
                .resideInAnyPackage(
                        "java.util.concurrent.atomic..",
                        "java.util.concurrent.locks.."
                );

        rule.check(mnemeClasses);

        ArchRule concurrentMapRule = noClasses()
                .that().resideInAPackage("net.fhirfactory.harmonia.hestia.mneme.coordination..")
                .should().dependOnClassesThat()
                .haveFullyQualifiedName("java.util.concurrent.ConcurrentHashMap");

        concurrentMapRule.check(mnemeClasses);
    }

    @Test
    @DisplayName("Static Source Check: Governed-write Java sources must not contain forbidden imports")
    void governedWriteSourcesMustNotContainForbiddenImports() throws IOException {
        Path projectRoot = findProjectRoot();
        Path sourceDir = projectRoot.resolve("calliope/src/main/java/net/fhirfactory/harmonia/model/governedwrite");
        assertThat(Files.exists(sourceDir)).isTrue();

        String[] forbiddenImports = {
                "import org.infinispan",
                "import jakarta.persistence",
                "import javax.persistence",
                "import org.hibernate",
                "import org.springframework.web",
                "import jakarta.ws.rs",
                "import javax.ws.rs",
                "import org.postgresql"
        };

        try (Stream<Path> paths = Files.walk(sourceDir)) {
            List<Path> javaFiles = paths.filter(p -> p.toString().endsWith(".java")).toList();
            assertThat(javaFiles).isNotEmpty();

            for (Path javaFile : javaFiles) {
                String content = Files.readString(javaFile);
                for (String forbidden : forbiddenImports) {
                    assertThat(content)
                            .as("File %s must not contain forbidden import '%s'", javaFile, forbidden)
                            .doesNotContain(forbidden);
                }
            }
        }
    }

    private Path findProjectRoot() {
        Path current = Paths.get(".").toAbsolutePath().normalize();
        while (current != null) {
            if (Files.exists(current.resolve("pom.xml"))
                    && Files.exists(current.resolve("calliope"))
                    && Files.exists(current.resolve("paradeigma"))) {
                return current;
            }
            current = current.getParent();
        }
        throw new IllegalStateException("Could not determine Harmonia repository root from working directory: "
                + Paths.get(".").toAbsolutePath().normalize());
    }
}
