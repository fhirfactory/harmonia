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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Architectural isolation tests enforcing that Harmonia Paradeigma remains strictly a leaf / simulation
 * module and that no production module, source file, or packaging configuration depends on or references it.
 */
public class ParadeigmaIsolationArchitectureTest {

    private static final String PARADEIGMA_PACKAGE = "net.fhirfactory.harmonia.paradeigma..";
    private static final String HARMONIA_ROOT_PACKAGE = "net.fhirfactory.harmonia..";

    @Test
    @DisplayName("ArchUnit: No production classes may depend on Paradeigma classes")
    void noProductionClassesShouldDependOnParadeigma() {
        JavaClasses classes = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("net.fhirfactory.harmonia");

        ArchRule rule = noClasses()
                .that().resideOutsideOfPackage(PARADEIGMA_PACKAGE)
                .should().dependOnClassesThat()
                .resideInAPackage(PARADEIGMA_PACKAGE);

        rule.check(classes);
    }

    @Test
    @DisplayName("POM Check: No production Maven POM may declare a dependency on Paradeigma")
    void noProductionPomsShouldDeclareParadeigmaDependency() throws IOException {
        Path projectRoot = findProjectRoot();
        List<Path> productionPoms = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(projectRoot)) {
            paths.filter(p -> p.getFileName().toString().equals("pom.xml"))
                    .filter(p -> !p.toString().contains("/target/"))
                    .filter(p -> !p.toString().contains("/paradeigma/"))
                    .filter(p -> !p.equals(projectRoot.resolve("pom.xml")))
                    .forEach(productionPoms::add);
        }

        assertThat(productionPoms).isNotEmpty();

        Pattern paradeigmaDepPattern = Pattern.compile("<artifactId>paradeigma[^<]*</artifactId>");

        for (Path pomPath : productionPoms) {
            String content = Files.readString(pomPath);
            boolean hasParadeigmaDependency = paradeigmaDepPattern.matcher(content).find();
            assertThat(hasParadeigmaDependency)
                    .as("Production POM %s must not declare dependency on Paradeigma", pomPath)
                    .isFalse();
        }
    }

    @Test
    @DisplayName("Static Source Check: No production Java source may import or contain simulation flags")
    void noProductionCodeShouldContainSimulationFlagsOrImports() throws IOException {
        Path projectRoot = findProjectRoot();
        List<Path> productionJavaFiles = new ArrayList<>();

        String[] productionModules = {"calliope", "themis", "hestia", "iris", "pylai", "energeia", "petasos", "agora"};
        for (String module : productionModules) {
            Path moduleSrc = projectRoot.resolve(module);
            if (Files.exists(moduleSrc)) {
                try (Stream<Path> paths = Files.walk(moduleSrc)) {
                    paths.filter(p -> p.toString().endsWith(".java"))
                            .filter(p -> p.toString().contains("/src/main/java/"))
                            .forEach(productionJavaFiles::add);
                }
            }
        }

        assertThat(productionJavaFiles).isNotEmpty();

        String[] forbiddenTokens = {
                "import net.fhirfactory.harmonia.paradeigma",
                "paradeigmaMode",
                "simulationMode",
                "syntheticRequest",
                "isParadeigmaGenerated",
                "harmonia.paradeigma.enabled"
        };

        for (Path javaFile : productionJavaFiles) {
            String content = Files.readString(javaFile);
            for (String token : forbiddenTokens) {
                assertThat(content.contains(token))
                        .as("Production source %s must not contain forbidden simulation token or import '%s'", javaFile, token)
                        .isFalse();
            }
        }
    }

    @Test
    @DisplayName("Deployment Packaging Check: Production artifact POMs do not package Paradeigma")
    void productionPackagingExcludesParadeigma() throws IOException {
        Path projectRoot = findProjectRoot();
        List<Path> deploymentPoms = List.of(
                projectRoot.resolve("iris/iris-console/pom.xml"),
                projectRoot.resolve("hestia/hie-operations-cli/pom.xml"),
                projectRoot.resolve("pylai/pylai-mllp-cli/pom.xml"),
                projectRoot.resolve("energeia/ponos-cli/pom.xml"),
                projectRoot.resolve("agora/agora-service/pom.xml")
        );

        for (Path pomPath : deploymentPoms) {
            if (Files.exists(pomPath)) {
                String content = Files.readString(pomPath);
                assertThat(content)
                        .as("Deployment POM %s must not contain paradeigma dependencies", pomPath)
                        .doesNotContain("<artifactId>paradeigma");
            }
        }
    }

    private Path findProjectRoot() {
        Path current = Paths.get(".").toAbsolutePath().normalize();
        while (current != null) {
            if (Files.exists(current.resolve("pom.xml"))
                    && Files.exists(current.resolve("paradeigma"))
                    && Files.exists(current.resolve("calliope"))) {
                return current;
            }
            current = current.getParent();
        }
        throw new IllegalStateException("Could not determine Harmonia repository root from working directory: "
                + Paths.get(".").toAbsolutePath().normalize());
    }
}
