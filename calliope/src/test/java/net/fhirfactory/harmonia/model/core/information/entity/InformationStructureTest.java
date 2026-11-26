/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.information.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/**
 * AC-5: the InformationUnit packages stay a standalone, layered, format-neutral model library.
 *
 * <p>These source-level checks complement the absent Paradeigma ArchUnit suite; they do not
 * replace it.</p>
 */
class InformationStructureTest {
    private static final String ROOT = "net.fhirfactory.harmonia.model.core.information.entity";
    private static final String NAMING = "net.fhirfactory.harmonia.model.core.naming";
    private static final Path SOURCE_ROOT = Path.of(
            "src/main/java/net/fhirfactory/harmonia/model/core/information/entity");

    @Test
    void packagesDependOnlyDownwardOnSiblingsNamingAndTheJdk() throws IOException {
        Map<String, Set<String>> allowed = Map.of(
                ROOT + ".valuesets", Set.of(),
                ROOT + ".datatypes", Set.of(ROOT + ".valuesets", NAMING),
                ROOT + ".factory", Set.of(ROOT + ".valuesets", ROOT + ".datatypes", NAMING),
                ROOT, Set.of(ROOT + ".valuesets", ROOT + ".datatypes", ROOT + ".factory", NAMING));
        Set<String> packages = new HashSet<>();
        for (Path source : productionSources()) {
            String packageName = packageOf(source);
            packages.add(packageName);
            for (String line : Files.readAllLines(source)) {
                if (!line.startsWith("import ")) {
                    continue;
                }
                String imported = line.substring("import ".length(), line.lastIndexOf('.'))
                        .replace("static ", "");
                if (imported.startsWith("java.") || imported.equals(packageName)) {
                    continue;
                }
                assertTrue(allowed.get(packageName).contains(imported), source + " imports " + imported);
            }
        }
        assertEquals(allowed.keySet(), packages);
        for (String packageName : packages) {
            Path directory = SOURCE_ROOT.resolve(packageName.equals(ROOT) ? ""
                    : packageName.substring(ROOT.length() + 1));
            assertTrue(Files.exists(directory.resolve("package-info.java")), packageName);
        }
    }

    @Test
    void sourcesAreCohesiveLicensedAndFreeOfLoggingSimulationAndPersistenceDependencies() throws IOException {
        List<String> forbiddenImports = List.of("org.slf4j", "org.apache.logging", "java.util.logging",
                "net.fhirfactory.harmonia.paradeigma", "jakarta.", "javax.", "org.hibernate", "org.infinispan",
                "ca.uhn", "org.hl7", "java.util.regex", "java.io.Serializable");
        List<String> forbiddenCode = List.of("System.out", "System.err", "printStackTrace", "implements Serializable");
        for (Path source : productionSources()) {
            String contents = Files.readString(source);
            List<String> lines = Files.readAllLines(source);
            assertTrue(lines.size() < 500, source.toString());
            assertTrue(contents.startsWith("/* Copyright (c) 2026 Mark Hunter."), source.toString());
            for (String line : lines) {
                for (String token : forbiddenImports) {
                    assertFalse(line.startsWith("import ") && line.contains(token), source + " imports " + token);
                }
            }
            for (String token : forbiddenCode) {
                assertFalse(contents.contains(token), source + " contains " + token);
            }
        }
        String pom = Files.readString(Path.of("pom.xml"));
        assertEquals(1, pom.split("<dependency>", -1).length - 1, "Calliope keeps only its test dependency");
    }

    @Test
    void modelTypesAreFinalHaveNoPublicConstructorsAndAreNotJavaSerializable() throws Exception {
        for (Path source : productionSources()) {
            String simpleName = source.getFileName().toString().replace(".java", "");
            if (simpleName.equals("package-info")) {
                continue;
            }
            Class<?> type = Class.forName(packageOf(source) + "." + simpleName);
            assertTrue(Modifier.isPublic(type.getModifiers()), type.getName());
            if (type.isEnum()) {
                continue;
            }
            assertTrue(Modifier.isFinal(type.getModifiers()), type.getName());
            for (Constructor<?> constructor : type.getDeclaredConstructors()) {
                assertFalse(Modifier.isPublic(constructor.getModifiers()), type.getName());
            }
            if (!Throwable.class.isAssignableFrom(type)) {
                assertFalse(Serializable.class.isAssignableFrom(type),
                        type.getName() + " must stay format-neutral");
            }
        }
    }

    @Test
    void namingRegressionScopeIsUntouchedByTheInformationPackages() throws IOException {
        Path naming = Path.of("src/main/java/net/fhirfactory/harmonia/model/core/naming");
        try (Stream<Path> sources = Files.walk(naming)) {
            Set<String> offenders = sources.filter(path -> path.toString().endsWith(".java"))
                    .filter(path -> {
                        try {
                            return Files.readString(path).contains("model.core.information");
                        } catch (IOException unreadable) {
                            return true;
                        }
                    })
                    .map(Path::toString).collect(Collectors.toSet());
            assertEquals(Set.of(), offenders, "Naming must not depend on the InformationUnit model");
        }
    }

    private static List<Path> productionSources() throws IOException {
        try (Stream<Path> sources = Files.walk(SOURCE_ROOT)) {
            return sources.filter(path -> path.toString().endsWith(".java")).sorted().toList();
        }
    }

    private static String packageOf(Path source) throws IOException {
        return Files.readAllLines(source).stream()
                .filter(line -> line.startsWith("package "))
                .map(line -> line.substring("package ".length(), line.length() - 1))
                .findFirst()
                .orElseThrow();
    }
}
