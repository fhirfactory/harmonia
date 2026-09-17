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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Architectural tests enforcing the strict boundary between iris-administration
 * (a presentation client) and the authoritative server-side Provider Registry
 * governance components (mnemosyne-clinical, erga, themis-core, pylai-fhir-registry).
 */
public class ProviderRegistryArchitectureTest {

    @Test
    @DisplayName("Boundary Check: iris-administration is a frontend SPA and contains no server-side persistence or DB drivers")
    void irisAdministrationMustNotContainServerPersistence() throws IOException {
        Path projectRoot = findProjectRoot();
        Path adminDir = projectRoot.resolve("iris/iris-administration");
        assertThat(Files.exists(adminDir)).isTrue();

        Path packageJson = adminDir.resolve("package.json");
        assertThat(Files.exists(packageJson)).isTrue();

        String packageContent = Files.readString(packageJson);
        assertThat(packageContent)
                .as("iris-administration must not depend on database or backend storage libraries")
                .doesNotContain("\"pg\"")
                .doesNotContain("\"mysql\"")
                .doesNotContain("\"typeorm\"")
                .doesNotContain("\"sequelize\"")
                .doesNotContain("\"@hapi/hapi\"");

        Path srcDir = adminDir.resolve("src");
        if (Files.exists(srcDir)) {
            try (Stream<Path> paths = Files.walk(srcDir)) {
                List<Path> codeFiles = paths.filter(p -> p.toString().endsWith(".ts") || p.toString().endsWith(".vue")).toList();
                for (Path codeFile : codeFiles) {
                    String content = Files.readString(codeFile);
                    assertThat(content)
                            .as("Source %s must not contain direct SQL execution", codeFile)
                            .doesNotContain("SELECT * FROM")
                            .doesNotContain("INSERT INTO")
                            .doesNotContain("UPDATE hie_");
                }
            }
        }
    }

    @Test
    @DisplayName("Structural Check: Authoritative Provider Registry logic resides in server-side modules")
    void authoritativeProviderRegistryLogicResidesServerSide() {
        Path projectRoot = findProjectRoot();

        // 1. Gateway REST layer exists in pylai-fhir-registry
        Path fhirRegSrc = projectRoot.resolve("pylai/pylai-fhir-registry/src/main/java/net/fhirfactory/harmonia/pylai/fhir");
        assertThat(Files.exists(fhirRegSrc)).isTrue();

        // 2. Ergon activity layer exists in erga
        Path ergaSrc = projectRoot.resolve("energeia/erga/src/main/java/net/fhirfactory/harmonia/erga/registry");
        assertThat(Files.exists(ergaSrc)).isTrue();

        // 3. Themis authorization policies exist in themis-core
        Path themisSrc = projectRoot.resolve("themis/themis-core/src/main/java/net/fhirfactory/harmonia/themis/core/policy");
        assertThat(Files.exists(themisSrc)).isTrue();

        // 4. JPA persistence engine exists in mnemosyne-clinical
        Path mnemosyneSrc = projectRoot.resolve("hestia/mnemosyne-clinical/src/main/java/net/fhirfactory/harmonia/hapifhir");
        assertThat(Files.exists(mnemosyneSrc)).isTrue();
    }

    private Path findProjectRoot() {
        Path current = Paths.get(".").toAbsolutePath().normalize();
        while (current != null) {
            if (Files.exists(current.resolve("pom.xml"))
                    && Files.exists(current.resolve("iris"))
                    && Files.exists(current.resolve("calliope"))) {
                return current;
            }
            current = current.getParent();
        }
        throw new IllegalStateException("Could not determine Harmonia repository root from working directory: "
                + Paths.get(".").toAbsolutePath().normalize());
    }
}
