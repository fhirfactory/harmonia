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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Architectural tests enforcing that the Petasos public API abstraction layer
 * ({@code net.fhirfactory.harmonia.petasos.api..}) is strictly isolated from
 * JMS, ActiveMQ Artemis broker implementation details, and provider adapters.
 */
public class PetasosApiIsolationArchitectureTest {

    private static final String PETASOS_API_PACKAGE = "net.fhirfactory.harmonia.petasos.api..";

    @Test
    @DisplayName("ArchUnit: Petasos API classes must not depend on ActiveMQ Artemis or JMS")
    void petasosApiShouldNotDependOnArtemisOrJms() {
        JavaClasses classes = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("net.fhirfactory.harmonia.petasos.api");

        ArchRule rule = noClasses()
                .that().resideInAPackage(PETASOS_API_PACKAGE)
                .should().dependOnClassesThat()
                .resideInAnyPackage(
                        "org.apache.activemq..",
                        "jakarta.jms..",
                        "javax.jms..",
                        "net.fhirfactory.harmonia.petasos.artemis.."
                );

        rule.check(classes);
    }

    @Test
    @DisplayName("POM Check: petasos-api pom.xml must not declare Artemis or JMS dependencies")
    void petasosApiPomShouldNotDeclareArtemisOrJmsDependencies() throws IOException {
        Path projectRoot = findProjectRoot();
        Path pomPath = projectRoot.resolve("petasos/petasos-api/pom.xml");
        assertThat(Files.exists(pomPath)).isTrue();

        String pomContent = Files.readString(pomPath);
        assertThat(pomContent)
                .as("petasos-api POM must not declare activemq-artemis dependencies")
                .doesNotContain("artemis")
                .doesNotContain("activemq")
                .doesNotContain("jakarta.jms")
                .doesNotContain("spring-boot-starter-artemis");
    }

    @Test
    @DisplayName("Static Source Check: petasos-api Java files must not import Artemis, JMS, or internal adapters")
    void petasosApiSourceFilesMustNotImportArtemisOrJms() throws IOException {
        Path projectRoot = findProjectRoot();
        Path apiSrc = projectRoot.resolve("petasos/petasos-api/src/main/java");
        assertThat(Files.exists(apiSrc)).isTrue();

        String[] forbiddenImports = {
                "import org.apache.activemq",
                "import jakarta.jms",
                "import javax.jms",
                "import net.fhirfactory.harmonia.petasos.artemis"
        };

        try (Stream<Path> paths = Files.walk(apiSrc)) {
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
                    && Files.exists(current.resolve("petasos"))
                    && Files.exists(current.resolve("calliope"))) {
                return current;
            }
            current = current.getParent();
        }
        throw new IllegalStateException("Could not determine Harmonia repository root from working directory: "
                + Paths.get(".").toAbsolutePath().normalize());
    }
}
