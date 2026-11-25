/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Supplier;
import net.fhirfactory.harmonia.model.core.naming.validation.NamingRuleViolation;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingFailureCategory;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingFieldKind;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingOperation;
import org.junit.jupiter.api.Test;

/** Verifies that implementation rule violations surface only as the supported exception. */
class NamingBoundaryTest {

    @Test
    void translationPreservesSafeMetadataAndRetainsNoCause() {
        NamingValidationException failure = assertThrows(NamingValidationException.class,
                () -> NamingBoundary.guard(() -> {
                    throw new NamingRuleViolation(NamingFailureCategory.PATTERN_REJECTED,
                            NamingOperation.MASK_PARSE, NamingFieldKind.VALUE_PATTERN, 4);
                }));
        assertEquals(NamingFailureCategory.PATTERN_REJECTED, failure.getCategory());
        assertEquals(NamingOperation.MASK_PARSE, failure.getOperation());
        assertEquals(NamingFieldKind.VALUE_PATTERN, failure.getFieldKind());
        assertEquals(4, failure.getEntryIndex().orElseThrow());
        assertNull(failure.getCause());

        NamingValidationException outOfRange = assertThrows(NamingValidationException.class,
                () -> NamingBoundary.check(() -> {
                    throw new NamingRuleViolation(NamingFailureCategory.INVALID_FIELD,
                            NamingOperation.TOPIC_BUILD, NamingFieldKind.VALUE, 99);
                }));
        assertTrue(outOfRange.getEntryIndex().isEmpty());
    }

    @Test
    void nonViolationFailuresAreDefectsAndPropagateUnchanged() {
        IllegalStateException defect = new IllegalStateException("defect");
        assertSame(defect, assertThrows(IllegalStateException.class,
                () -> NamingBoundary.guard(() -> {
                    throw defect;
                })));
    }

    @Test
    void everyDelegatingSupportedOperationSurfacesOnlyThePublicException() {
        byte[] malformedXml = "<topic><entry/></topic>".getBytes(StandardCharsets.UTF_8);
        List<Supplier<Object>> operations = List.of(
                () -> Topic.fromCanonicalName("Role=LA\\B"),
                () -> Topic.fromCanonicalNameWithVersion("Role:=LAB"),
                () -> TopicMask.fromCanonicalExpression("Role=**", false),
                () -> TopicMaskEntry.of("Role", "LAB", ">=x"),
                () -> TopicMaskEntry.forLiteralValue(" padded"),
                () -> TopicMaskEntry.ofLiterals("Role", "LAB", "bad\u0001"),
                () -> TopicMask.builder().addPatterns("**", "LAB"),
                () -> Topic.builder().addEntry("Bad\u0001", "LAB"),
                () -> NamingXmlCodec.readTopic(malformedXml),
                () -> NamingXmlCodec.readTopicMask(malformedXml));
        for (Supplier<Object> operation : operations) {
            NamingValidationException failure = assertThrows(NamingValidationException.class, operation::get);
            assertEquals(NamingValidationException.class, failure.getClass());
            assertNull(failure.getCause());
        }
    }
}
