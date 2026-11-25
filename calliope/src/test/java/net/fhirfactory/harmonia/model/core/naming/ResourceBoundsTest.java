/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicInteger;
import net.fhirfactory.harmonia.model.core.naming.matching.EntryCriterion;
import net.fhirfactory.harmonia.model.core.naming.matching.MatchProbe;
import net.fhirfactory.harmonia.model.core.naming.validation.NamingLimits;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingFailureCategory;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingFieldKind;
import net.fhirfactory.harmonia.model.core.naming.valuesets.NamingOperation;
import org.junit.jupiter.api.Test;

/** Exercises depth, encoded-size, metadata, and maximum-work limits. */
class ResourceBoundsTest {

    @Test
    void topicAndMaskAcceptSixteenEntriesAndRejectTheSeventeenthWithoutStateGrowth() {
        TopicBuilder topicBuilder = Topic.builder();
        TopicMaskBuilder maskBuilder = TopicMask.builder();
        for (int index = 0; index < NamingLimits.MAX_DEPTH; index++) {
            topicBuilder.addEntry("N" + index, "V" + index);
            maskBuilder.addAny();
        }
        assertEquals(NamingLimits.MAX_DEPTH, topicBuilder.build().getDepth());
        assertEquals(NamingLimits.MAX_DEPTH, maskBuilder.build().getEntries().size());

        NamingValidationException topicOverflow = assertThrows(NamingValidationException.class,
                () -> topicBuilder.addEntry("Overflow", "V"));
        NamingValidationException maskOverflow = assertThrows(NamingValidationException.class,
                maskBuilder::addAny);
        assertEquals(NamingFailureCategory.LIMIT_EXCEEDED, topicOverflow.getCategory());
        assertEquals(NamingFailureCategory.LIMIT_EXCEEDED, maskOverflow.getCategory());
        assertEquals(NamingLimits.MAX_DEPTH, topicBuilder.build().getDepth());
        assertEquals(NamingLimits.MAX_DEPTH, maskBuilder.build().getEntries().size());

        String topicText = "N=V.".repeat(NamingLimits.MAX_DEPTH) + "N=V";
        assertEquals(NamingLimits.MAX_DEPTH, Topic.fromCanonicalName("N=V.".repeat(15) + "N=V").getDepth());
        assertEquals(NamingFailureCategory.LIMIT_EXCEEDED,
                assertThrows(NamingValidationException.class, () -> Topic.fromCanonicalName(topicText))
                        .getCategory());

        String oneEntry = "<entry><qualifier>Role</qualifier><value>LAB</value></entry>";
        String xmlText = "<topic>" + oneEntry.repeat(NamingLimits.MAX_DEPTH + 1) + "</topic>";
        NamingValidationException xmlOverflow = assertThrows(NamingValidationException.class,
                () -> NamingXmlCodec.readTopic(xmlText.getBytes(StandardCharsets.UTF_8)));
        assertEquals(NamingFailureCategory.LIMIT_EXCEEDED, xmlOverflow.getCategory());
        assertTrue(xmlOverflow.getEntryIndex().isEmpty());
    }

    @Test
    void canonicalLimitsUseUtf8BytesRatherThanUtf16Length() {
        String exactTopicBytes = "😀".repeat(8_192);
        NamingValidationException atLimit = assertThrows(NamingValidationException.class,
                () -> Topic.fromCanonicalName(exactTopicBytes));
        assertEquals(NamingFailureCategory.MALFORMED_TEXT, atLimit.getCategory());
        NamingValidationException aboveLimit = assertThrows(NamingValidationException.class,
                () -> Topic.fromCanonicalName(exactTopicBytes + "😀"));
        assertEquals(NamingFailureCategory.LIMIT_EXCEEDED, aboveLimit.getCategory());

        String oversizedMask = "é".repeat(NamingLimits.MAX_MASK_CANONICAL_UTF8_BYTES / 2 + 1);
        NamingValidationException maskLimit = assertThrows(NamingValidationException.class,
                () -> TopicMask.fromCanonicalExpression(oversizedMask, false));
        assertEquals(NamingFailureCategory.LIMIT_EXCEEDED, maskLimit.getCategory());
    }

    @Test
    void xmlRecordsAtTheByteLimitAreParsedAndOneByteBeyondIsRejected() throws IOException {
        byte[] fixture;
        try (InputStream input = getClass().getResourceAsStream(
                "/net/fhirfactory/harmonia/model/core/naming/interchange/topic.xml")) {
            if (input == null) {
                throw new IOException("Missing Topic fixture");
            }
            fixture = input.readAllBytes();
        }
        byte[] exact = Arrays.copyOf(fixture, NamingLimits.MAX_XML_RECORD_UTF8_BYTES);
        Arrays.fill(exact, fixture.length, exact.length, (byte) ' ');
        assertEquals(2, NamingXmlCodec.readTopic(exact).getDepth());
        byte[] oversized = Arrays.copyOf(exact, exact.length + 1);
        NamingValidationException failure = assertThrows(NamingValidationException.class,
                () -> NamingXmlCodec.readTopic(oversized));
        assertEquals(NamingFailureCategory.LIMIT_EXCEEDED, failure.getCategory());
    }

    @Test
    void base64FieldByteBoundsAreCheckedBeforeUtf8FieldConstruction() {
        String oversizedPattern = Base64.getEncoder().encodeToString(
                new byte[NamingLimits.MAX_PATTERN_UTF8_BYTES + 1]);
        String patternXml = "<topicMask kind=\"ordinary\" includeContainedElements=\"false\"><entry>"
                + "<qualifierPattern encoding=\"base64\">" + oversizedPattern + "</qualifierPattern>"
                + "<valuePattern encoding=\"base64\">TEFC</valuePattern></entry></topicMask>";
        NamingValidationException patternFailure = assertThrows(NamingValidationException.class,
                () -> NamingXmlCodec.readTopicMask(patternXml.getBytes(StandardCharsets.UTF_8)));
        assertEquals(NamingFailureCategory.LIMIT_EXCEEDED, patternFailure.getCategory());
        assertEquals(0, patternFailure.getEntryIndex().orElseThrow());

        String oversizedConstraint = Base64.getEncoder().encodeToString(
                new byte[NamingLimits.MAX_VERSION_CONSTRAINT_UTF8_BYTES + 1]);
        String constraintXml = "<topicMask kind=\"ordinary\" includeContainedElements=\"false\"><entry>"
                + "<qualifierPattern encoding=\"base64\">Um9sZQ==</qualifierPattern>"
                + "<valuePattern encoding=\"base64\">TEFC</valuePattern>"
                + "<versionConstraint encoding=\"base64\">" + oversizedConstraint
                + "</versionConstraint></entry></topicMask>";
        NamingValidationException constraintFailure = assertThrows(NamingValidationException.class,
                () -> NamingXmlCodec.readTopicMask(constraintXml.getBytes(StandardCharsets.UTF_8)));
        assertEquals(NamingFailureCategory.LIMIT_EXCEEDED, constraintFailure.getCategory());
        assertEquals(0, constraintFailure.getEntryIndex().orElseThrow());
    }

    @Test
    void invalidIndexesHaveSafeTypedMetadataWithoutInventedPositions() {
        NamingValidationException negative = assertThrows(NamingValidationException.class,
                () -> Topic.builder().addEntry(-1, "Role", "LAB"));
        assertEquals(NamingFailureCategory.INVALID_ARGUMENT, negative.getCategory());
        assertEquals(NamingOperation.TOPIC_BUILD, negative.getOperation());
        assertEquals(NamingFieldKind.INDEX, negative.getFieldKind());
        assertTrue(negative.getEntryIndex().isEmpty());

        NamingValidationException navigation = assertThrows(NamingValidationException.class,
                () -> Topic.builder().addEntry("Role", "LAB").build().getAncestor(0));
        assertEquals(NamingOperation.TOPIC_NAVIGATE, navigation.getOperation());
        assertEquals(NamingFieldKind.INDEX, navigation.getFieldKind());
        assertTrue(navigation.getEntryIndex().isEmpty());
        assertFalse(navigation.getMessage().contains("Role"));
    }

    @Test
    void maximumOrdinaryMaskWorkIsFortyEightOrderedFieldChecks() {
        TopicMaskBuilder maskBuilder = TopicMask.builder();
        TopicBuilder topicBuilder = Topic.builder();
        for (int index = 0; index < NamingLimits.MAX_DEPTH; index++) {
            maskBuilder.addPatterns("Role", "LAB", ">=1");
            topicBuilder.addEntry("Role", "LAB", index == NamingLimits.MAX_DEPTH - 1 ? "0" : "1");
        }
        AtomicInteger checks = new AtomicInteger();
        MatchProbe counting = new MatchProbe() {
            @Override
            public boolean matchesQualifier(EntryCriterion entry, String candidate) {
                checks.incrementAndGet();
                return entry.matchesQualifier(candidate);
            }

            @Override
            public boolean matchesValue(EntryCriterion entry, String candidate) {
                checks.incrementAndGet();
                return entry.matchesValue(candidate);
            }

            @Override
            public boolean matchesVersion(EntryCriterion entry, String candidate) {
                checks.incrementAndGet();
                return entry.matchesVersion(candidate);
            }
        };

        assertFalse(maskBuilder.build().matches(topicBuilder.build(), null, counting));
        assertEquals(NamingLimits.MAX_DEPTH * 3, checks.get());
        checks.set(0);
        TopicBuilder matchingBuilder = Topic.builder();
        for (int index = 0; index < NamingLimits.MAX_DEPTH; index++) {
            matchingBuilder.addEntry("Role", "LAB", "1");
        }
        assertTrue(maskBuilder.build().matches(matchingBuilder.build(), null, counting));
        assertEquals(NamingLimits.MAX_DEPTH * 3, checks.get());
    }
}