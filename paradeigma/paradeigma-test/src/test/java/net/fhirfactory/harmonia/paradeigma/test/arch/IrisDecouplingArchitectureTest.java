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
 * Architectural tests enforcing that the Iris presentation tier (SPAs and iris-befe)
 * remains strictly decoupled from backend persistent storage, direct database access,
 * JPA/Hibernate, and database drivers.
 */
public class IrisDecouplingArchitectureTest {

    @Test
    @DisplayName("POM Check: Iris module POMs must not declare PostgreSQL, JPA, or Hibernate dependencies")
    void irisPomsMustNotDeclareDatabaseOrJpaDependencies() throws IOException {
        Path projectRoot = findProjectRoot();
        Path irisDir = projectRoot.resolve("iris");
        assertThat(Files.exists(irisDir)).isTrue();

        try (Stream<Path> paths = Files.walk(irisDir)) {
            List<Path> poms = paths.filter(p -> p.getFileName().toString().equals("pom.xml"))
                    .filter(p -> !p.toString().contains("/target/"))
                    .toList();

            assertThat(poms).isNotEmpty();

            for (Path pom : poms) {
                String content = Files.readString(pom);
                assertThat(content)
                        .as("Iris POM %s must not contain PostgreSQL driver dependency", pom)
                        .doesNotContain("<artifactId>postgresql</artifactId>");
                assertThat(content)
                        .as("Iris POM %s must not contain Hibernate JPA dependency", pom)
                        .doesNotContain("<artifactId>hibernate-core</artifactId>")
                        .doesNotContain("<artifactId>hapi-fhir-jpaserver-base</artifactId>");
            }
        }
    }

    @Test
    @DisplayName("Static Source Check: iris-befe Java sources must not import JPA or PostgreSQL classes")
    void irisBefeSourcesMustNotImportJpaOrPostgres() throws IOException {
        Path projectRoot = findProjectRoot();
        Path befeSrc = projectRoot.resolve("iris/iris-befe/src/main/java");
        if (!Files.exists(befeSrc)) {
            return;
        }

        String[] forbiddenTokens = {
                "import jakarta.persistence",
                "import javax.persistence",
                "import org.hibernate",
                "import org.postgresql",
                "import java.sql.DriverManager",
                "import ca.uhn.fhir.jpa"
        };

        try (Stream<Path> paths = Files.walk(befeSrc)) {
            List<Path> javaFiles = paths.filter(p -> p.toString().endsWith(".java")).toList();
            for (Path javaFile : javaFiles) {
                String content = Files.readString(javaFile);
                for (String token : forbiddenTokens) {
                    assertThat(content)
                            .as("iris-befe source %s must not contain forbidden token '%s'", javaFile, token)
                            .doesNotContain(token);
                }
            }
        }
    }

    @Test
    @DisplayName("Frontend Check: Iris SPAs must communicate via REST/HTTP and contain no backend DB drivers")
    void irisSpasMustNotContainBackendDatabaseDrivers() throws IOException {
        Path projectRoot = findProjectRoot();
        String[] spaDirs = {"iris/iris-clinical", "iris/iris-console", "iris/iris-administration"};

        for (String spaDir : spaDirs) {
            Path spaPath = projectRoot.resolve(spaDir);
            if (Files.exists(spaPath)) {
                Path packageJson = spaPath.resolve("package.json");
                if (Files.exists(packageJson)) {
                    String content = Files.readString(packageJson);
                    assertThat(content)
                            .as("%s/package.json must not depend on database drivers", spaDir)
                            .doesNotContain("\"pg\"")
                            .doesNotContain("\"mysql\"")
                            .doesNotContain("\"sqlite3\"")
                            .doesNotContain("\"typeorm\"")
                            .doesNotContain("\"prisma\"");
                }
            }
        }
    }

    @Test
    @DisplayName("Frontend Architecture Check: iris-befe/frontend must not depend on or import from application SPAs")
    void irisBefeFrontendMustNotDependOnSpas() throws IOException {
        Path projectRoot = findProjectRoot();
        Path befeFrontend = projectRoot.resolve("iris/iris-befe/frontend");
        if (!Files.exists(befeFrontend)) {
            return;
        }

        Path packageJson = befeFrontend.resolve("package.json");
        if (Files.exists(packageJson)) {
            String content = Files.readString(packageJson);
            assertThat(content)
                    .as("iris-befe/frontend/package.json must have zero dependencies on application SPAs")
                    .doesNotContain("iris-console")
                    .doesNotContain("iris-clinical")
                    .doesNotContain("iris-administration");
        }

        Path srcDir = befeFrontend.resolve("src");
        if (Files.exists(srcDir)) {
            try (Stream<Path> paths = Files.walk(srcDir)) {
                List<Path> sourceFiles = paths
                        .filter(p -> !p.toString().contains("/__tests__/"))
                        .filter(p -> p.toString().endsWith(".ts") || p.toString().endsWith(".vue"))
                        .toList();

                for (Path src : sourceFiles) {
                    String content = Files.readString(src);
                    assertThat(content)
                            .as("iris-befe source %s must not import from application SPAs", src)
                            .doesNotContain("iris-console")
                            .doesNotContain("iris-clinical")
                            .doesNotContain("iris-administration");
                }
            }
        }
    }

    @Test
    @DisplayName("AuditEventResource Dependency Check: AuditEventResource must not depend on cache or persistence")
    void auditEventResourceMustNotDependOnCacheOrPersistence() throws IOException {
        Path projectRoot = findProjectRoot();
        Path auditEventResource = projectRoot.resolve("iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/rest/AuditEventResource.java");

        assertThat(Files.exists(auditEventResource)).isTrue();

        String content = Files.readString(auditEventResource);

        // Assert forbidden dependencies are NOT present
        String[] forbidden = {
                "FhirCacheService",
                "AppendOnlyAuditEventRepository",
                "JdbcAppendOnlyAuditEventRepository",
                "DataSource",
                "java.sql",
                "jakarta.persistence",
                "org.hibernate"
        };

        for (String token : forbidden) {
            assertThat(content)
                    .as("AuditEventResource must not contain forbidden dependency: %s", token)
                    .doesNotContain(token);
        }

        // Assert required dependencies ARE present
        assertThat(content).contains("AuditService");
        assertThat(content).contains("HarmoniaAuditEventMapper");
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
