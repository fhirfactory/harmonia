/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/** Enforces the supported public API, package boundary, source size, and dependency surface. */
class NamingStructureTest {
    private static final Path SOURCE_ROOT = Path.of(
            "src/main/java/net/fhirfactory/harmonia/model/core/naming");

    @Test
    void onlyTheApprovedModelsAndValidationVocabularyArePublic() throws IOException {
        Set<String> publicTypes = new java.util.HashSet<>();
        try (Stream<Path> sources = Files.walk(SOURCE_ROOT)) {
            for (Path source : sources.filter(path -> path.toString().endsWith(".java")).toList()) {
                String contents = Files.readString(source);
                for (String line : contents.lines().toList()) {
                    String declaration = line.trim();
                    if (declaration.startsWith("public ")
                            && (declaration.contains(" class ") || declaration.contains(" enum "))) {
                        publicTypes.add(source.getFileName().toString().replace(".java", ""));
                    }
                }
            }
        }
        assertEquals(Set.of("Topic", "TopicEntry", "TopicBuilder", "TopicMask", "TopicMaskEntry",
                "TopicMaskBuilder", "NamingXmlCodec", "NamingValidationException",
                "NamingFailureCategory", "NamingOperation", "NamingFieldKind"), publicTypes);

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

        for (Class<?> type : new Class<?>[]{Topic.class, TopicEntry.class, TopicBuilder.class, TopicMask.class,
                TopicMaskEntry.class, TopicMaskBuilder.class, NamingXmlCodec.class,
                NamingValidationException.class}) {
            for (Constructor<?> constructor : type.getDeclaredConstructors()) {
                assertFalse(Modifier.isPublic(constructor.getModifiers()), type.getName());
            }
        }
        assertFalse(Modifier.isPublic(NamingDiagnostic.class.getModifiers()));
        assertFalse(Modifier.isPublic(MatchDiagnosticSink.class.getModifiers()));
        assertFalse(Modifier.isPublic(MatchProbe.class.getModifiers()));
    }

    @Test
    void productionFilesStayCohesiveAndUseOnlyTheSingleNamingPackage() throws IOException {
        Set<String> publicDeclarations = new java.util.HashSet<>();
        try (Stream<Path> sources = Files.walk(SOURCE_ROOT)) {
            for (Path source : sources.filter(path -> path.toString().endsWith(".java")).toList()) {
                String contents = Files.readString(source);
                assertTrue(Files.readAllLines(source).size() < 500, source.toString());
                assertTrue(contents.startsWith("/* Copyright"), source.toString());
                assertTrue(contents.contains("package net.fhirfactory.harmonia.model.core.naming;"),
                        source.toString());
                assertFalse(contents.contains("java.util.regex"), source.toString());
                assertFalse(contents.contains("com.google.re2j"), source.toString());
                assertFalse(contents.contains("org.apache.logging"), source.toString());
                assertFalse(contents.contains("import net.fhirfactory.harmonia."), source.toString());
                for (String line : contents.lines().toList()) {
                    String declaration = line.trim();
                    if (declaration.startsWith("public ")
                            && (declaration.contains(" class ") || declaration.contains(" enum "))) {
                        publicDeclarations.add(source.getFileName().toString().replace(".java", ""));
                    }
                }
            }
        }
        assertEquals(11, publicDeclarations.size());
        assertFalse(Files.exists(SOURCE_ROOT.resolve("interchange/NamingXmlCodec.java")));
        assertFalse(Files.exists(SOURCE_ROOT.resolve("valuesets/NamingLimits.java")));

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
        String evaluator = Files.readString(SOURCE_ROOT.resolve("MaskEvaluator.java"));
        int enabledBranch = evaluator.indexOf("if (sink != null)");
        int recordConstruction = evaluator.indexOf("new NamingDiagnostic(");
        assertTrue(enabledBranch >= 0);
        assertTrue(recordConstruction > enabledBranch);
        assertEquals(recordConstruction, evaluator.lastIndexOf("new NamingDiagnostic("));

        String mask = Files.readString(SOURCE_ROOT.resolve("TopicMask.java"));
        assertTrue(mask.contains("MaskEvaluator.evaluate(this, candidate, null, null)"));
        assertFalse(mask.contains("NamingDiagnostic"));
        assertFalse(mask.contains("public boolean matches(Topic candidate, MatchDiagnosticSink sink)"));
    }

    @Test
    void numericCandidatesUseOneBoundedSpanParseWithoutMachineNumericConversion() throws IOException {
        String constraints = Files.readString(SOURCE_ROOT.resolve("VersionConstraint.java"));
        String numericVersion = Files.readString(SOURCE_ROOT.resolve("NumericVersion.java"));

        assertEquals(1, countOccurrences(constraints, "NumericVersion.parse(candidate)"));
        assertFalse(numericVersion.contains("Integer.parseInt"));
        assertFalse(numericVersion.contains("Long.parseLong"));
        assertFalse(numericVersion.contains("Double.parseDouble"));
        assertFalse(numericVersion.contains("BigInteger"));
        assertFalse(numericVersion.contains("BigDecimal"));
    }

    @Test
    void canonicalDepthGuardPrecedesBothPeriodAndFinalSegmentMaterialization() throws IOException {
        String syntax = Files.readString(SOURCE_ROOT.resolve("CanonicalPathSyntax.java"));
        int periodMaterialization = syntax.indexOf("entries.add(text.substring(start, offset));");
        int periodDepthGuard = syntax.lastIndexOf(
                "if (entries.size() >= NamingLimits.MAX_DEPTH)", periodMaterialization);
        int finalMaterialization = syntax.indexOf("entries.add(text.substring(start));");
        int finalDepthGuard = syntax.lastIndexOf(
                "if (entries.size() >= NamingLimits.MAX_DEPTH)", finalMaterialization);

        assertTrue(periodDepthGuard >= 0 && periodDepthGuard < periodMaterialization);
        assertTrue(finalDepthGuard > periodMaterialization && finalDepthGuard < finalMaterialization);
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