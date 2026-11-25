/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingFailureCategory;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingOperation;
import org.junit.jupiter.api.Test;

/**
 * Enforces the supported Naming API, the function-oriented package structure,
 * one-way package dependencies, source size and the dependency surface.
 */
class NamingStructureTest {
    private static final String ROOT = "net.fhirfactory.harmonia.model.core.naming";
    private static final Path SOURCE_ROOT = Path.of(
            "src/main/java/net/fhirfactory/harmonia/model/core/naming");
    private static final Set<String> SUPPORTED_PACKAGES = Set.of(ROOT, ROOT + ".valuesets");
    private static final Set<Class<?>> SUPPORTED_TYPES = Set.of(Topic.class, TopicEntry.class,
            TopicBuilder.class, TopicMask.class, TopicMaskEntry.class, TopicMaskBuilder.class,
            NamingXmlCodec.class, NamingValidationException.class);

    @Test
    void eachFunctionPackageDeclaresExactlyItsIntendedTypesAndVisibility() throws Exception {
        Map<String, Set<String>> publicTypes = new HashMap<>();
        Map<String, Set<String>> packagePrivateTypes = new HashMap<>();
        for (Path source : productionSources()) {
            String packageName = packageOf(source);
            if (source.getFileName().toString().equals("package-info.java")) {
                continue;
            }
            String simpleName = source.getFileName().toString().replace(".java", "");
            Class<?> type = Class.forName(packageName + "." + simpleName);
            (Modifier.isPublic(type.getModifiers()) ? publicTypes : packagePrivateTypes)
                    .computeIfAbsent(packageName, key -> new HashSet<>()).add(simpleName);
        }

        assertEquals(Map.of(
                ROOT, Set.of("Topic", "TopicEntry", "TopicBuilder", "TopicMask", "TopicMaskEntry",
                        "TopicMaskBuilder", "NamingXmlCodec", "NamingValidationException"),
                ROOT + ".valuesets", Set.of("NamingFailureCategory", "NamingOperation", "NamingFieldKind"),
                ROOT + ".validation", Set.of("NamingLimits", "TopicFieldRules", "NamingRuleViolation"),
                ROOT + ".canonical", Set.of("CanonicalPathSyntax", "TopicCanonicalParser",
                        "MaskExpressionParser", "CanonicalEntryFields"),
                ROOT + ".matching", Set.of("FieldPattern", "VersionConstraint", "EntryCriterion",
                        "MatchCandidate", "MaskEvaluator", "MatchProbe", "MatchDiagnosticSink",
                        "NamingDiagnostic", "DiagnosticSeverity", "MatchExitCategory"),
                ROOT + ".interchange", Set.of("NamingXmlFormat", "XmlEntryFields", "XmlMaskRecord")),
                publicTypes);
        assertEquals(Map.of(
                ROOT, Set.of("NamingBoundary", "NamingSerialization"),
                ROOT + ".matching", Set.of("NumericVersion"),
                ROOT + ".interchange", Set.of("XmlRecordParser")), packagePrivateTypes);
    }

    @Test
    void supportedTypesKeepTheAcceptedSignaturesAndHiddenConstruction() {
        assertSignatures(Topic.class, Set.of("builder()", "fromCanonicalName(String)",
                "fromCanonicalNameWithVersion(String)", "getEntries()", "getCanonicalName()",
                "getCanonicalNameWithVersion()", "getName()", "getRelativeName()", "getFriendlyName()",
                "getDisplayName()", "getDepth()", "getParent()", "getAncestor(int)",
                "isAncestorOf(Topic)", "compareTo(Topic)", "equals(Object)", "hashCode()", "toString()"));
        assertSignatures(TopicEntry.class, Set.of("getQualifier()", "getValue()", "getVersion()",
                "equals(Object)", "hashCode()"));
        assertSignatures(TopicBuilder.class, Set.of("addEntry(String,String)",
                "addEntry(String,String,String)", "addEntry(int,String,String)",
                "addEntry(int,String,String,String)", "build()"));
        assertSignatures(TopicMask.class, Set.of("builder()", "all()", "fromCanonicalExpression(String,boolean)",
                "isUniversal()", "getEntries()", "isIncludeContainedElements()", "getCanonicalExpression()",
                "matches(Topic)", "equals(Object)", "hashCode()", "toString()"));
        assertSignatures(TopicMaskEntry.class, Set.of("of(String,String)", "of(String,String,String)",
                "forValue(String)", "forQualifier(String)", "any()", "ofLiterals(String,String)",
                "ofLiterals(String,String,String)", "forLiteralValue(String)", "forLiteralQualifier(String)",
                "getQualifierPattern()", "getValuePattern()", "getVersionConstraint()",
                "equals(Object)", "hashCode()"));
        assertSignatures(TopicMaskBuilder.class, Set.of("add(TopicMaskEntry)", "addPatterns(String,String)",
                "addPatterns(String,String,String)", "addLiterals(String,String)", "addLiterals(String,String,String)",
                "addForValue(String)", "addForQualifier(String)", "addAny()", "addForLiteralValue(String)",
                "addForLiteralQualifier(String)", "includeContainedElements(boolean)", "build()"));
        assertSignatures(NamingXmlCodec.class, Set.of("readTopic(byte[])", "writeTopic(Topic)",
                "readTopicMask(byte[])", "writeTopicMask(TopicMask)"));
        assertSignatures(NamingValidationException.class, Set.of("getCategory()", "getOperation()",
                "getFieldKind()", "getEntryIndex()"));

        for (Class<?> type : SUPPORTED_TYPES) {
            for (Constructor<?> constructor : type.getDeclaredConstructors()) {
                assertFalse(Modifier.isPublic(constructor.getModifiers()), type.getName());
                assertFalse(Modifier.isProtected(constructor.getModifiers()), type.getName());
            }
        }
    }

    @Test
    void supportedSignaturesExposeOnlySupportedPackagesAndThePlatform() {
        for (Class<?> type : SUPPORTED_TYPES) {
            assertSupportedOrPlatform(type.getSuperclass(), type.getName());
            for (Class<?> implemented : type.getInterfaces()) {
                assertSupportedOrPlatform(implemented, type.getName());
            }
            for (Method method : type.getDeclaredMethods()) {
                if (Modifier.isPublic(method.getModifiers()) || Modifier.isProtected(method.getModifiers())) {
                    assertSupportedOrPlatform(method.getReturnType(), method.toString());
                    for (Class<?> parameter : method.getParameterTypes()) {
                        assertSupportedOrPlatform(parameter, method.toString());
                    }
                    for (Class<?> thrown : method.getExceptionTypes()) {
                        assertSupportedOrPlatform(thrown, method.toString());
                    }
                }
            }
            for (Field field : type.getDeclaredFields()) {
                if (Modifier.isPublic(field.getModifiers()) || Modifier.isProtected(field.getModifiers())) {
                    assertSupportedOrPlatform(field.getType(), field.toString());
                }
            }
        }
    }

    @Test
    void implementationPackagesDependOnlyDownwardAndNeverOnTheModels() throws IOException {
        Map<String, Set<String>> allowed = Map.of(
                ROOT + ".valuesets", Set.of(),
                ROOT + ".validation", Set.of(ROOT + ".valuesets"),
                ROOT + ".canonical", Set.of(ROOT + ".valuesets", ROOT + ".validation"),
                ROOT + ".matching", Set.of(ROOT + ".valuesets", ROOT + ".validation"),
                ROOT + ".interchange", Set.of(ROOT + ".valuesets", ROOT + ".validation"));
        Set<String> packages = new HashSet<>();
        for (Path source : productionSources()) {
            String packageName = packageOf(source);
            packages.add(packageName);
            assertFalse(packageName.contains("internal"), source.toString());
            Set<String> imported = Files.readAllLines(source).stream()
                    .filter(line -> line.startsWith("import " + ROOT))
                    .map(line -> line.substring("import ".length(), line.lastIndexOf('.')))
                    .collect(Collectors.toSet());
            if (!packageName.equals(ROOT)) {
                Set<String> unexpected = new HashSet<>(imported);
                unexpected.remove(packageName);
                unexpected.removeAll(allowed.get(packageName));
                assertEquals(Set.of(), unexpected, source.toString());
            }
        }
        assertEquals(allowed.keySet().stream().collect(Collectors.toCollection(HashSet::new)),
                withoutRoot(packages));
        for (String packageName : packages) {
            Path directory = SOURCE_ROOT.resolve(packageName.equals(ROOT) ? ""
                    : packageName.substring(ROOT.length() + 1));
            assertTrue(Files.exists(directory.resolve("package-info.java")), packageName);
        }
    }

    @Test
    void productionFilesStayCohesiveAndFreeOfRegexEnginesAndLoggingDependencies() throws IOException {
        for (Path source : productionSources()) {
            String contents = Files.readString(source);
            assertTrue(Files.readAllLines(source).size() < 500, source.toString());
            assertTrue(contents.startsWith("/* Copyright"), source.toString());
            assertFalse(contents.contains("java.util.regex"), source.toString());
            assertFalse(contents.contains("com.google.re2j"), source.toString());
            assertFalse(contents.contains("org.apache.logging"), source.toString());
            assertFalse(contents.contains("org.slf4j"), source.toString());
        }
        assertFalse(Files.exists(Path.of("src/main/java/module-info.java")));

        String pom = Files.readString(Path.of("pom.xml"));
        assertFalse(pom.contains("<artifactId>re2j</artifactId>"));
        int junitIndex = pom.indexOf("<artifactId>junit-jupiter</artifactId>");
        assertTrue(junitIndex >= 0);
        assertTrue(pom.substring(junitIndex, pom.indexOf("</dependency>", junitIndex))
                .contains("<scope>test</scope>"));
        assertFalse(Arrays.stream(NamingFailureCategory.values())
                .anyMatch(category -> category.name().contains("EXPRESSION")));
        assertFalse(Arrays.stream(NamingOperation.values())
                .anyMatch(operation -> operation.name().equals("MASK_MATCH")));
    }

    @Test
    void disabledDiagnosticsConstructNoRecordAndPublicMatchingSuppliesNoSink() throws IOException {
        String evaluator = Files.readString(SOURCE_ROOT.resolve("matching/MaskEvaluator.java"));
        int enabledBranch = evaluator.indexOf("if (sink != null)");
        int recordConstruction = evaluator.indexOf("new NamingDiagnostic(");
        assertTrue(enabledBranch >= 0);
        assertTrue(recordConstruction > enabledBranch);
        assertEquals(recordConstruction, evaluator.lastIndexOf("new NamingDiagnostic("));

        String mask = Files.readString(SOURCE_ROOT.resolve("TopicMask.java"));
        assertTrue(mask.contains("public boolean matches(Topic candidate) {\n        return matches(candidate, null, null);"));
        assertFalse(mask.contains("NamingDiagnostic"));
        assertFalse(mask.contains("public boolean matches(Topic candidate, MatchDiagnosticSink sink)"));
    }

    @Test
    void numericCandidatesUseOneBoundedSpanParseWithoutMachineNumericConversion() throws IOException {
        String constraints = Files.readString(SOURCE_ROOT.resolve("matching/VersionConstraint.java"));
        String numericVersion = Files.readString(SOURCE_ROOT.resolve("matching/NumericVersion.java"));

        assertEquals(1, countOccurrences(constraints, "NumericVersion.parse(candidate)"));
        for (String conversion : List.of("Integer.parseInt", "Long.parseLong", "Double.parseDouble",
                "BigInteger", "BigDecimal")) {
            assertFalse(numericVersion.contains(conversion), conversion);
        }
    }

    @Test
    void canonicalDepthGuardPrecedesBothPeriodAndFinalSegmentMaterialization() throws IOException {
        String syntax = Files.readString(SOURCE_ROOT.resolve("canonical/CanonicalPathSyntax.java"));
        int periodMaterialization = syntax.indexOf("entries.add(text.substring(start, offset));");
        int periodDepthGuard = syntax.lastIndexOf(
                "if (entries.size() >= NamingLimits.MAX_DEPTH)", periodMaterialization);
        int finalMaterialization = syntax.indexOf("entries.add(text.substring(start));");
        int finalDepthGuard = syntax.lastIndexOf(
                "if (entries.size() >= NamingLimits.MAX_DEPTH)", finalMaterialization);

        assertTrue(periodDepthGuard >= 0 && periodDepthGuard < periodMaterialization);
        assertTrue(finalDepthGuard > periodMaterialization && finalDepthGuard < finalMaterialization);
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

    private static Set<String> withoutRoot(Set<String> packages) {
        Set<String> result = new HashSet<>(packages);
        result.remove(ROOT);
        return result;
    }

    private static void assertSupportedOrPlatform(Class<?> type, String context) {
        if (type == null || type.isPrimitive()) {
            return;
        }
        Class<?> component = type.isArray() ? type.getComponentType() : type;
        if (component.isPrimitive()) {
            return;
        }
        String packageName = component.getPackageName();
        assertTrue(packageName.startsWith("java.") || SUPPORTED_PACKAGES.contains(packageName),
                context + " exposes " + component.getName());
    }

    private static void assertSignatures(Class<?> type, Set<String> expected) {
        Set<String> actual = Arrays.stream(type.getDeclaredMethods())
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .filter(method -> !method.isBridge() && !method.isSynthetic())
                .map(NamingStructureTest::signature)
                .collect(Collectors.toSet());
        assertEquals(expected, actual, type.getName());
    }

    private static String signature(Method method) {
        String parameters = Arrays.stream(method.getParameterTypes())
                .map(Class::getSimpleName)
                .collect(Collectors.joining(","));
        return method.getName() + "(" + parameters + ")";
    }

    private static int countOccurrences(String text, String value) {
        int count = 0;
        int offset = 0;
        while ((offset = text.indexOf(value, offset)) >= 0) {
            count++;
            offset += value.length();
        }
        return count;
    }
}
