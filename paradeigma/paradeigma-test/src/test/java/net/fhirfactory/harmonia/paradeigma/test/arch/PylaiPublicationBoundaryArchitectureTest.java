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

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Architectural tests enforcing the Pylai External FHIR Publication Boundary
 * (conforming to AX-05 Information Authority & Lifecycle, AX-13 Egress & Publication Boundary,
 * and AGENTS.md Invariant 9 External Interoperability Boundary).
 *
 * Key Architectural Invariants:
 * 1. Pylai is the external publication membrane responsible for constructing externally publishable
 *    FHIR representations governed by explicit interoperability contracts.
 * 2. Internal operational metadata, private extensions, and internal security labels must never leak across the boundary.
 * 3. Publication projection must be non-destructive (source in-memory instances remain unmodified).
 * 4. All managed-resource REST egress paths in Pylai (READ, SEARCH, CREATE/UPDATE Task responses, Task polling)
 *    must route through the publication projector before serialization.
 */
public class PylaiPublicationBoundaryArchitectureTest {

    private static final String PYLAI_PUBLICATION_PACKAGE = "net.fhirfactory.harmonia.pylai.fhir.publication..";
    private static final String PYLAI_CONTROLLER_PACKAGE = "net.fhirfactory.harmonia.pylai.fhir.controller..";

    private static JavaClasses importedClasses;

    @BeforeAll
    static void loadClasses() {
        importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("net.fhirfactory.harmonia");
    }

    @Test
    @DisplayName("ArchUnit: Publication projector and policy must not depend on JPA or persistence internals")
    void publicationMustNotDependOnJpaOrPersistenceInternals() {
        ArchRule rule = noClasses()
                .that().resideInAPackage(PYLAI_PUBLICATION_PACKAGE)
                .should().dependOnClassesThat()
                .resideInAnyPackage(
                        "jakarta.persistence..",
                        "javax.persistence..",
                        "org.hibernate..",
                        "ca.uhn.fhir.jpa..",
                        "net.fhirfactory.harmonia.hapifhir.persistence..",
                        "net.fhirfactory.harmonia.hapifhir.repository.."
                );

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("ArchUnit: Publication projector and policy must not depend on Ponos workflow engine or Iris presentation")
    void publicationMustNotDependOnPonosOrIris() {
        ArchRule rule = noClasses()
                .that().resideInAPackage(PYLAI_PUBLICATION_PACKAGE)
                .should().dependOnClassesThat()
                .resideInAnyPackage(
                        "net.fhirfactory.harmonia.energeia.ponos..",
                        "net.fhirfactory.harmonia.ponos..",
                        "net.fhirfactory.harmonia.iris..",
                        "net.fhirfactory.harmonia.paradeigma.."
                );

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("ArchUnit: FhirRestGatewayController must depend on PylaiFhirPublicationProjector")
    void gatewayControllerMustDependOnPublicationProjector() {
        ArchRule rule = classes()
                .that().haveSimpleName("FhirRestGatewayController")
                .should().dependOnClassesThat()
                .haveSimpleName("PylaiFhirPublicationProjector");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("ArchUnit: PylaiFhirPublicationProjector must depend on FhirPublicationPolicy")
    void publicationProjectorMustDependOnPublicationPolicy() {
        ArchRule rule = classes()
                .that().haveSimpleName("PylaiFhirPublicationProjector")
                .should().dependOnClassesThat()
                .haveSimpleName("FhirPublicationPolicy");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("ArchUnit: Pylai publication classes must not depend on Artemis messaging")
    void publicationMustNotDependOnArtemis() {
        ArchRule rule = noClasses()
                .that().resideInAPackage(PYLAI_PUBLICATION_PACKAGE)
                .should().dependOnClassesThat()
                .resideInAnyPackage("org.apache.activemq..");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Static Source Check: FhirRestGatewayController must project resources on all managed egress paths")
    void gatewayControllerMustProjectAllManagedEgressPaths() throws IOException {
        Path projectRoot = findProjectRoot();
        Path controllerFile = projectRoot.resolve(
                "pylai/pylai-fhir-registry/src/main/java/net/fhirfactory/harmonia/pylai/fhir/controller/FhirRestGatewayController.java"
        );
        assertThat(Files.exists(controllerFile)).isTrue();

        String content = Files.readString(controllerFile);

        // Verify publication projector dependency injection
        assertThat(content)
                .as("Controller must hold PylaiFhirPublicationProjector field")
                .contains("PylaiFhirPublicationProjector publicationProjector");

        // Verify READ egress projection
        assertThat(content)
                .as("readResource must project resource before serialization")
                .contains("publicationProjector.projectForPublication(resource)");

        // Verify SEARCH egress projection
        assertThat(content)
                .as("searchResources must project bundle before serialization")
                .contains("publicationProjector.projectForPublication(searchBundle)");

        // Verify CREATE Task egress projection
        assertThat(content)
                .as("createResource must project Task before emission")
                .contains("publicationProjector.projectForPublication(fhirTask)");

        // Verify UPDATE Task egress projection
        assertThat(content)
                .as("updateResource must project Task before emission")
                .contains("publicationProjector.projectForPublication(fhirTask)");

        // Verify Task polling egress projection
        assertThat(content)
                .as("getTaskStatus must project Task before emission")
                .contains("publicationProjector.projectForPublication(");

        // Verify synthetic response documentation
        assertThat(content)
                .as("Controller must document why CapabilityStatement and OperationOutcome do not require projection")
                .contains("Synthetic / Gateway-Generated Responses:")
                .contains("CapabilityStatement")
                .contains("OperationOutcome");
    }

    @Test
    @DisplayName("Static Source Check: PylaiFhirPublicationProjector must perform non-destructive deep copy")
    void publicationProjectorMustPerformNonDestructiveCopy() throws IOException {
        Path projectRoot = findProjectRoot();
        Path projectorFile = projectRoot.resolve(
                "pylai/pylai-fhir-registry/src/main/java/net/fhirfactory/harmonia/pylai/fhir/publication/PylaiFhirPublicationProjector.java"
        );
        assertThat(Files.exists(projectorFile)).isTrue();

        String content = Files.readString(projectorFile);

        assertThat(content)
                .as("Projector must call copy() on Resource to ensure non-destructive isolation")
                .contains(".copy()");

        assertThat(content)
                .as("Projector must sanitize meta.security according to publication policy")
                .contains("policy.isSecurityLabelPermitted(");

        assertThat(content)
                .as("Projector must filter extensions recursively according to publication policy")
                .contains("policy.isExtensionPermitted(");
    }

    @Test
    @DisplayName("Static Source Check: FhirPublicationPolicy must define fail-closed contract rules")
    void publicationPolicyMustEnforceFailClosedContractRules() throws IOException {
        Path projectRoot = findProjectRoot();
        Path policyFile = projectRoot.resolve(
                "pylai/pylai-fhir-registry/src/main/java/net/fhirfactory/harmonia/pylai/fhir/publication/FhirPublicationPolicy.java"
        );
        assertThat(Files.exists(policyFile)).isTrue();

        String content = Files.readString(policyFile);

        assertThat(content)
                .as("Policy must check extension permission")
                .contains("boolean isExtensionPermitted(");

        assertThat(content)
                .as("Policy must check security label permission")
                .contains("boolean isSecurityLabelPermitted(");

        assertThat(content)
                .as("Policy must check profile permission")
                .contains("boolean isProfilePermitted(");
    }

    private Path findProjectRoot() {
        Path current = Paths.get(".").toAbsolutePath().normalize();
        while (current != null) {
            if (Files.exists(current.resolve("pom.xml"))
                    && Files.exists(current.resolve("pylai"))
                    && Files.exists(current.resolve("paradeigma"))) {
                return current;
            }
            current = current.getParent();
        }
        throw new IllegalStateException("Could not determine Harmonia repository root from working directory: "
                + Paths.get(".").toAbsolutePath().normalize());
    }
}
