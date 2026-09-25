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
 * Architectural tests enforcing that Mnemosyne authoritative persistence
 * ({@code net.fhirfactory.harmonia.hapifhir.persistence..}) remains isolated from
 * Mneme/Infinispan active-state coordination, does not leak JPA into domain contracts,
 * and does not expose physical DELETE operations (ADR-020).
 */
public class MnemosyneAuthoritativePersistenceArchitectureTest {

    private static final String MNEMOSYNE_PERSISTENCE_PACKAGE = "net.fhirfactory.harmonia.hapifhir.persistence..";

    private static JavaClasses persistenceClasses;

    @BeforeAll
    static void loadClasses() {
        persistenceClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("net.fhirfactory.harmonia.hapifhir.persistence");
    }

    @Test
    @DisplayName("ArchUnit: Mnemosyne persistence must not depend on Infinispan or Hot Rod")
    void persistenceMustNotDependOnInfinispan() {
        ArchRule rule = noClasses()
                .that().resideInAPackage(MNEMOSYNE_PERSISTENCE_PACKAGE)
                .should().dependOnClassesThat()
                .resideInAnyPackage(
                        "org.infinispan..",
                        "org.infinispan.client.hotrod..",
                        "org.infinispan.commons.."
                );

        rule.check(persistenceClasses);
    }

    @Test
    @DisplayName("ArchUnit: Mnemosyne persistence must not depend on Mneme active-state coordination")
    void persistenceMustNotDependOnMnemeCoordination() {
        ArchRule rule = noClasses()
                .that().resideInAPackage(MNEMOSYNE_PERSISTENCE_PACKAGE)
                .should().dependOnClassesThat()
                .resideInAnyPackage("net.fhirfactory.harmonia.hestia.mneme..");

        rule.check(persistenceClasses);
    }

    @Test
    @DisplayName("ArchUnit: Mnemosyne persistence must not depend on ActiveStateToken or ActiveStateTokenBridge")
    void persistenceMustNotDependOnActiveStateToken() {
        ArchRule rule = noClasses()
                .that().resideInAPackage(MNEMOSYNE_PERSISTENCE_PACKAGE)
                .should().dependOnClassesThat()
                .haveFullyQualifiedName("net.fhirfactory.harmonia.model.governedwrite.ActiveStateToken")
                .orShould().dependOnClassesThat()
                .haveFullyQualifiedName("net.fhirfactory.harmonia.model.governedwrite.ActiveStateTokenBridge");

        rule.check(persistenceClasses);
    }

    @Test
    @DisplayName("ArchUnit: AuthoritativePersistencePort must not expose DELETE or REMOVE methods (ADR-020)")
    void persistencePortMustNotExposeDeleteMethods() {
        ArchRule rule = methods()
                .that().areDeclaredInClassesThat().haveSimpleName("AuthoritativePersistencePort")
                .should().haveNameNotMatching(".*[dD]elete.*")
                .andShould().haveNameNotMatching(".*[rR]emove.*");

        rule.check(persistenceClasses);
    }

    @Test
    @DisplayName("ArchUnit: AuthoritativePersistencePort must not leak JPA/Entity types in its contract")
    void persistencePortMustNotLeakJpaTypes() {
        ArchRule rule = noClasses()
                .that().haveSimpleName("AuthoritativePersistencePort")
                .should().dependOnClassesThat()
                .resideInAnyPackage(
                        "jakarta.persistence..",
                        "javax.persistence..",
                        "org.hibernate..",
                        "net.fhirfactory.harmonia.hapifhir.model.."
                );

        rule.check(persistenceClasses);
    }

    @Test
    @DisplayName("Static Source Check: Mnemosyne persistence Java sources must not import Infinispan or Hot Rod")
    void persistenceSourcesMustNotContainForbiddenImports() throws IOException {
        Path projectRoot = findProjectRoot();
        Path sourceDir = projectRoot.resolve("hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/persistence");
        assertThat(Files.exists(sourceDir)).isTrue();

        String[] forbiddenImports = {
                "import org.infinispan",
                "import net.fhirfactory.harmonia.hestia.mneme",
                "import net.fhirfactory.harmonia.model.governedwrite.ActiveStateToken"
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
                    && Files.exists(current.resolve("hestia"))
                    && Files.exists(current.resolve("paradeigma"))) {
                return current;
            }
            current = current.getParent();
        }
        throw new IllegalStateException("Could not determine Harmonia repository root from working directory: "
                + Paths.get(".").toAbsolutePath().normalize());
    }
}
