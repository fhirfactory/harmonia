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

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Architectural tests verifying the composed governed-write layer across Harmonia:
 * <ol>
 *   <li>GovernedWriter domain contracts reside in Calliope and remain pure</li>
 *   <li>DefaultGovernedWriter orchestrator resides in hestia/mnemosyne-clinical and avoids direct Infinispan / JPA leaks</li>
 *   <li>HotRodMnemeConvergence resides in hestia/mneme-cluster and does not depend on Mnemosyne persistence</li>
 *   <li>Zero DELETE / REMOVE / PURGE methods across the governed-write and persistence ports (ADR-020)</li>
 * </ol>
 */
public class GovernedWriteCompositionArchitectureTest {

    private static JavaClasses allProductionClasses;

    @BeforeAll
    static void loadClasses() {
        allProductionClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("net.fhirfactory.harmonia");
    }

    @Test
    @DisplayName("ArchUnit: GovernedWriter interface must reside in Calliope (net.fhirfactory.harmonia.model.governedwrite)")
    void governedWriterMustResideInCalliope() {
        ArchRule rule = classes()
                .that().haveSimpleName("GovernedWriter")
                .should().resideInAPackage("net.fhirfactory.harmonia.model.governedwrite..");

        rule.check(allProductionClasses);
    }

    @Test
    @DisplayName("ArchUnit: DefaultGovernedWriter must reside in mnemosyne-clinical (net.fhirfactory.harmonia.hapifhir.governed)")
    void defaultGovernedWriterMustResideInMnemosyneClinical() {
        ArchRule rule = classes()
                .that().haveSimpleName("DefaultGovernedWriter")
                .should().resideInAPackage("net.fhirfactory.harmonia.hapifhir.governed..");

        rule.check(allProductionClasses);
    }

    @Test
    @DisplayName("ArchUnit: DefaultGovernedWriter must not depend on Infinispan, JPA, or Hibernate")
    void defaultGovernedWriterMustNotDependOnInfinispanOrJpa() {
        ArchRule rule = noClasses()
                .that().haveSimpleName("DefaultGovernedWriter")
                .should().dependOnClassesThat()
                .resideInAnyPackage(
                        "org.infinispan..",
                        "jakarta.persistence..",
                        "javax.persistence..",
                        "org.hibernate..",
                        "ca.uhn.fhir.jpa.."
                );

        rule.check(allProductionClasses);
    }

    @Test
    @DisplayName("ArchUnit: HotRodMnemeConvergence must not depend on AuthoritativePersistencePort or JPA entities")
    void hotRodMnemeConvergenceMustNotDependOnPersistencePortOrJpa() {
        ArchRule rule = noClasses()
                .that().haveSimpleName("HotRodMnemeConvergence")
                .should().dependOnClassesThat()
                .resideInAnyPackage(
                        "net.fhirfactory.harmonia.hapifhir.persistence..",
                        "net.fhirfactory.harmonia.hapifhir.model..",
                        "jakarta.persistence..",
                        "org.hibernate.."
                );

        rule.check(allProductionClasses);
    }

    @Test
    @DisplayName("ArchUnit: GovernedWriter, ActiveStateCoordinator, and AuthoritativePersistencePort must not expose DELETE methods (ADR-020)")
    void governedWritePortsMustNotExposeDeleteMethods() {
        ArchRule rule = methods()
                .that().areDeclaredInClassesThat().haveSimpleNameStartingWith("GovernedWriter")
                .or().areDeclaredInClassesThat().haveSimpleNameStartingWith("ActiveStateCoordinator")
                .or().areDeclaredInClassesThat().haveSimpleNameStartingWith("ActiveStateConvergencePort")
                .or().areDeclaredInClassesThat().haveSimpleNameStartingWith("AuthoritativePersistencePort")
                .should().haveNameNotMatching(".*[dD]elete.*")
                .andShould().haveNameNotMatching(".*[rR]emove.*")
                .andShould().haveNameNotMatching(".*[pP]urge.*");

        rule.check(allProductionClasses);
    }

    @Test
    @DisplayName("Static Source Check: DefaultGovernedWriter Java source must not import Infinispan or JPA")
    void defaultGovernedWriterSourceMustNotContainForbiddenImports() throws IOException {
        Path projectRoot = findProjectRoot();
        Path sourceFile = projectRoot.resolve(
                "hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir/governed/DefaultGovernedWriter.java"
        );
        assertThat(Files.exists(sourceFile)).isTrue();

        String content = Files.readString(sourceFile);
        String[] forbidden = {
                "import org.infinispan",
                "import jakarta.persistence",
                "import javax.persistence",
                "import org.hibernate",
                "import ca.uhn.fhir.jpa"
        };

        for (String forbiddenImport : forbidden) {
            assertThat(content).doesNotContain(forbiddenImport);
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
