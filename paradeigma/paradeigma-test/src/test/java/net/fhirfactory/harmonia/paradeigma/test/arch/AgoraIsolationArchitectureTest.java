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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Architectural tests enforcing Agora boundary and isolation rules:
 * 1. Agora must not directly depend on Ponos workflow engine.
 * 2. Matrix protocol types and DTOs must not leak outside Agora.
 * 3. Agora production code must not depend on Paradeigma simulation framework.
 * 4. Themis authorization governance must be integrated on Agora ingress and lifecycle services.
 */
public class AgoraIsolationArchitectureTest {

    private static final String AGORA_ROOT_PACKAGE = "net.fhirfactory.harmonia.agora..";
    private static final String AGORA_API_PACKAGE = "net.fhirfactory.harmonia.agora.api..";
    private static final String AGORA_MATRIX_PACKAGE = "net.fhirfactory.harmonia.agora.matrix..";
    private static final String PARADEIGMA_PACKAGE = "net.fhirfactory.harmonia.paradeigma..";

    private static JavaClasses importedClasses;

    @BeforeAll
    static void loadClasses() {
        importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("net.fhirfactory.harmonia");
    }

    @Test
    @DisplayName("ArchUnit: Agora must not directly depend on Ponos task execution classes")
    void agoraMustNotDependOnPonos() {
        ArchRule rule = noClasses()
                .that().resideInAPackage(AGORA_ROOT_PACKAGE)
                .should().dependOnClassesThat()
                .resideInAnyPackage(
                        "net.fhirfactory.harmonia.energeia.ponos..",
                        "net.fhirfactory.harmonia.ponos.."
                );

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("ArchUnit: Matrix DTOs and client adapters must not leak outside Agora")
    void matrixTypesMustNotLeakOutsideAgora() {
        ArchRule rule = noClasses()
                .that().resideInAnyPackage(
                        "net.fhirfactory.harmonia.calliope..",
                        "net.fhirfactory.harmonia.themis..",
                        "net.fhirfactory.harmonia.petasos..",
                        "net.fhirfactory.harmonia.energeia..",
                        "net.fhirfactory.harmonia.pylai..",
                        "net.fhirfactory.harmonia.iris.."
                )
                .should().dependOnClassesThat()
                .resideInAPackage(AGORA_MATRIX_PACKAGE);

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("ArchUnit: Agora API public contracts must not depend on Matrix internal types")
    void agoraApiMustNotDependOnMatrixInternalTypes() {
        ArchRule rule = noClasses()
                .that().resideInAPackage(AGORA_API_PACKAGE)
                .should().dependOnClassesThat()
                .resideInAPackage(AGORA_MATRIX_PACKAGE);

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("ArchUnit: Agora production classes must not depend on Paradeigma simulation")
    void agoraMustNotDependOnParadeigma() {
        ArchRule rule = noClasses()
                .that().resideInAPackage(AGORA_ROOT_PACKAGE)
                .should().dependOnClassesThat()
                .resideInAPackage(PARADEIGMA_PACKAGE);

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("ArchUnit: Agora core lifecycle, identity, and reconciliation services must enforce Themis governance")
    void agoraCoreMustEnforceThemisGovernance() {
        ArchRule rule = classes()
                .that().resideInAPackage("net.fhirfactory.harmonia.agora.core.lifecycle..")
                .or().resideInAPackage("net.fhirfactory.harmonia.agora.core.reconciliation..")
                .or().resideInAPackage("net.fhirfactory.harmonia.agora.core.identity..")
                .should().dependOnClassesThat()
                .resideInAPackage("net.fhirfactory.harmonia.themis..");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("POM Check: Agora POMs must not declare dependency on Ponos or Paradeigma")
    void noPonosOrParadeigmaInAgoraPoms() throws IOException {
        Path projectRoot = findProjectRoot();
        Path agoraDir = projectRoot.resolve("agora");
        assertThat(Files.exists(agoraDir)).isTrue();

        List<Path> agoraPoms = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(agoraDir)) {
            paths.filter(p -> p.getFileName().toString().equals("pom.xml"))
                    .filter(p -> !p.toString().contains("/target/"))
                    .forEach(agoraPoms::add);
        }

        assertThat(agoraPoms).isNotEmpty();

        Pattern forbiddenDepPattern = Pattern.compile("<artifactId>(paradeigma[^<]*|ponos[^<]*)</artifactId>");

        for (Path pomPath : agoraPoms) {
            String content = Files.readString(pomPath);
            boolean hasForbiddenDep = forbiddenDepPattern.matcher(content).find();
            assertThat(hasForbiddenDep)
                    .as("Agora POM %s must not declare dependency on Ponos or Paradeigma", pomPath)
                    .isFalse();
        }
    }

    @Test
    @DisplayName("Static Source Check: Agora production sources must not import Ponos, Paradeigma, or contain simulation flags")
    void noForbiddenImportsOrSimulationFlagsInAgora() throws IOException {
        Path projectRoot = findProjectRoot();
        Path agoraDir = projectRoot.resolve("agora");
        assertThat(Files.exists(agoraDir)).isTrue();

        List<Path> javaFiles = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(agoraDir)) {
            paths.filter(p -> p.toString().endsWith(".java"))
                    .filter(p -> p.toString().contains("/src/main/java/"))
                    .forEach(javaFiles::add);
        }

        assertThat(javaFiles).isNotEmpty();

        String[] forbiddenTokens = {
                "import net.fhirfactory.harmonia.paradeigma",
                "import net.fhirfactory.harmonia.ponos",
                "import net.fhirfactory.harmonia.energeia.ponos",
                "paradeigmaMode",
                "simulationMode",
                "syntheticRequest",
                "isParadeigmaGenerated"
        };

        for (Path javaFile : javaFiles) {
            String content = Files.readString(javaFile);
            for (String token : forbiddenTokens) {
                assertThat(content.contains(token))
                        .as("Agora production source %s must not contain forbidden token '%s'", javaFile, token)
                        .isFalse();
            }
        }
    }

    private Path findProjectRoot() {
        Path current = Paths.get(".").toAbsolutePath().normalize();
        while (current != null) {
            if (Files.exists(current.resolve("pom.xml"))
                    && Files.exists(current.resolve("paradeigma"))
                    && Files.exists(current.resolve("agora"))) {
                return current;
            }
            current = current.getParent();
        }
        throw new IllegalStateException("Could not determine Harmonia repository root from working directory: "
                + Paths.get(".").toAbsolutePath().normalize());
    }
}
