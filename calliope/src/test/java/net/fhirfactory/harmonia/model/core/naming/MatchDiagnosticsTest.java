/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

/** Verifies ordered matching, single safe diagnostics, and fail-closed evaluator behavior. */
class MatchDiagnosticsTest {

    @Test
    void ordinaryChecksStopAtFirstMismatchAndSuccessReportsLastCheckedPosition() {
        TopicMask mask = TopicMask.builder().addPatterns("Role", "LAB", ">=1.2")
                .addPatterns("Component", "Calliope").build();
        List<NamingDiagnostic> events = new ArrayList<>();
        AtomicInteger qualifierChecks = new AtomicInteger();
        AtomicInteger valueChecks = new AtomicInteger();
        AtomicInteger versionChecks = new AtomicInteger();
        MatchProbe probe = counterProbe(qualifierChecks, valueChecks, versionChecks);

        Topic wrongQualifier = Topic.builder().addEntry("Wrong", "LAB", "2")
                .addEntry("Component", "Calliope").build();
        assertFalse(mask.matches(wrongQualifier, events::add, probe));
        assertEquals(1, qualifierChecks.get());
        assertEquals(0, valueChecks.get());
        assertEquals(0, versionChecks.get());
        assertEquals(1, events.size());
        assertEquals(MatchExitCategory.QUALIFIER_MISMATCH, events.get(0).category());
        assertEquals(NamingFieldKind.QUALIFIER_PATTERN, events.get(0).fieldKind());
        assertEquals(0, events.get(0).entryIndex());

        events.clear();
        assertTrue(mask.matches(Topic.builder().addEntry("Role", "LAB", "1.2")
                .addEntry("Component", "Calliope").build(), events::add));
        assertEquals(1, events.size());
        assertEquals(MatchExitCategory.MATCH, events.get(0).category());
        assertEquals(1, events.get(0).entryIndex());
        assertEquals(NamingFieldKind.VALUE_PATTERN, events.get(0).fieldKind());
        assertEquals(DiagnosticSeverity.TRACE, events.get(0).severity());
    }

    @Test
    void nullAndDepthMismatchHaveNoInvalidEntryIndex() {
        TopicMask mask = TopicMask.builder().addPatterns("Role", "LAB").build();
        List<NamingDiagnostic> events = new ArrayList<>();

        assertFalse(mask.matches(null, events::add));
        assertEquals(MatchExitCategory.NULL_CANDIDATE, events.get(0).category());
        assertEquals(0, events.get(0).depth());
        assertNull(events.get(0).entryIndex());

        events.clear();
        assertFalse(mask.matches(Topic.builder().addEntry("Role", "LAB")
                .addEntry("Component", "X").build(), events::add));
        assertEquals(MatchExitCategory.DEPTH_MISMATCH, events.get(0).category());
        assertNull(events.get(0).entryIndex());
    }

    @Test
    void absentRequiredVersionShortCircuitsBeforeVersionProbe() {
        TopicMask mask = TopicMask.builder().addPatterns("Role", "LAB", "").build();
        AtomicInteger versionChecks = new AtomicInteger();
        MatchProbe probe = counterProbe(new AtomicInteger(), new AtomicInteger(), versionChecks);
        List<NamingDiagnostic> events = new ArrayList<>();

        assertFalse(mask.matches(topic("Role", "LAB", null), events::add, probe));
        assertEquals(0, versionChecks.get());
        assertEquals(MatchExitCategory.REQUIRED_VERSION_ABSENT, events.get(0).category());
        assertEquals(NamingFieldKind.VERSION_CONSTRAINT, events.get(0).fieldKind());
        assertEquals(0, events.get(0).entryIndex());
    }

    @Test
    void unexpectedProbeAndThrowingHandlerCannotChangeOrRetryResult() {
        TopicMask mask = TopicMask.builder().addPatterns("Role", "LAB").build();
        Topic candidate = topic("Role", "LAB", null);
        AtomicInteger probeCalls = new AtomicInteger();
        MatchProbe failing = new MatchProbe() {
            @Override
            public boolean matchesQualifier(TopicMaskEntry entry, String value) {
                probeCalls.incrementAndGet();
                throw new IllegalStateException("private payload");
            }

            @Override
            public boolean matchesValue(TopicMaskEntry entry, String value) {
                throw new AssertionError("must short circuit");
            }

            @Override
            public boolean matchesVersion(TopicMaskEntry entry, String value) {
                throw new AssertionError("must short circuit");
            }
        };
        AtomicInteger emitted = new AtomicInteger();

        assertFalse(mask.matches(candidate, diagnostic -> {
            emitted.incrementAndGet();
            assertEquals(MatchExitCategory.EVALUATION_ERROR, diagnostic.category());
            assertEquals(DiagnosticSeverity.ERROR, diagnostic.severity());
            assertFalse(diagnostic.toString().contains("private payload"));
            throw new IllegalStateException("handler failure");
        }, failing));
        assertEquals(1, probeCalls.get());
        assertEquals(1, emitted.get());
        assertTrue(mask.matches(candidate, diagnostic -> {
            emitted.incrementAndGet();
            throw new IllegalStateException("handler failure");
        }));
        assertEquals(2, emitted.get());
        assertFalse(mask.matches(candidate, null, failing));
        assertEquals(2, probeCalls.get());
    }

    @Test
    void publicMatchSurfaceUsesNoHandlerAndDiagnosticTypesRemainInternal() throws Exception {
        assertEquals(1, TopicMask.class.getMethod("matches", Topic.class).getParameterCount());
        assertThrows(NoSuchMethodException.class,
                () -> TopicMask.class.getMethod("matches", Topic.class, MatchDiagnosticSink.class));
        assertFalse(java.lang.reflect.Modifier.isPublic(NamingDiagnostic.class.getModifiers()));
        assertFalse(java.lang.reflect.Modifier.isPublic(MatchDiagnosticSink.class.getModifiers()));
    }

    private static MatchProbe counterProbe(
            AtomicInteger qualifiers,
            AtomicInteger values,
            AtomicInteger versions) {
        return new MatchProbe() {
            @Override
            public boolean matchesQualifier(TopicMaskEntry entry, String value) {
                qualifiers.incrementAndGet();
                return entry.matchesQualifier(value);
            }

            @Override
            public boolean matchesValue(TopicMaskEntry entry, String value) {
                values.incrementAndGet();
                return entry.matchesValue(value);
            }

            @Override
            public boolean matchesVersion(TopicMaskEntry entry, String value) {
                versions.incrementAndGet();
                return entry.matchesVersion(value);
            }
        };
    }

    private static Topic topic(String qualifier, String value, String version) {
        return Topic.builder().addEntry(qualifier, value, version).build();
    }
}