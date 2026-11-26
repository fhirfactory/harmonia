/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.information.entity;

import static net.fhirfactory.harmonia.model.core.information.entity.InformationMetadataTest.expectFailure;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Modifier;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Duration;
import java.util.Arrays;
import java.util.HexFormat;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.Content;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.ContentDetail;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.ContentDetailBuilder;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.ContentMetadata;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.ContainerMetadata;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationRepresentation;
import net.fhirfactory.harmonia.model.core.information.entity.factory.InformationVocabulary;
import net.fhirfactory.harmonia.model.core.information.entity.valuesets.FhirR4ResourceType;
import net.fhirfactory.harmonia.model.core.information.entity.valuesets.InformationFailureCategory;
import net.fhirfactory.harmonia.model.core.naming.Topic;
import org.junit.jupiter.api.Test;

/** AC-4: schema versions, RAW fallback, exactly-one Content carrier, byte fidelity and integrity details. */
class InformationRepresentationTest {
    private static final String PATIENT_JSON = "{\"resourceType\":\"Patient\",\"id\":\"synthetic-1\"}";
    /** SHA-256 of PATIENT_JSON's UTF-8 bytes, computed independently with {@code sha256sum}. */
    private static final String PATIENT_JSON_SHA256 =
            "017e0aed42b4b37dd05ba7bc62a5c19c1e416c4b50dfa2cf1544699680bbb62e";

    @Test
    void contentSchemaCarriesItsVersionThroughTopicEntryVersions() {
        Topic schema = InformationVocabulary.fhirR4ResourceSchema(FhirR4ResourceType.Patient);
        ContentMetadata metadata = ContentMetadata.of(InformationFixtures.topic("ContentConcept", "Patient"),
                schema, "application/fhir+json", ContentDetail.empty());

        assertEquals("Domain=Health.Owner=HealthLevelSevenInternational.Standard=FHIR.Release:4\\.0\\.1=R4.Resource=Patient",
                metadata.getContentSchema().getCanonicalNameWithVersion());
        Topic otherVersion = Topic.fromCanonicalNameWithVersion(
                "Domain=Health.Owner=HealthLevelSevenInternational.Standard=FHIR.Release:4\\.0\\.2=R4.Resource=Patient");
        assertEquals(schema, otherVersion, "Topic identity is version-independent");
        assertNotEquals(metadata, ContentMetadata.of(metadata.getContentConcept(), otherVersion,
                metadata.getContentMediaType(), ContentDetail.empty()),
                "but a schema version change is a different representation");
    }

    @Test
    void containerWithoutDistinctSchemaUsesTheQualifiedRawTopicAndAlwaysHasAConcept() {
        ContainerMetadata container = InformationVocabulary.rawContainer(
                InformationFixtures.topic("Container", "File"), "application/octet-stream");

        assertEquals("Domain=ICT.Owner=fhirfactory.Standard=harmonia.Concept=InformationUnit.InformationContainer=RAW",
                container.getContainerSchema().getCanonicalNameWithVersion());
        assertEquals("application/octet-stream", container.getContainerMediaType());
        expectFailure(InformationFailureCategory.MISSING_INPUT, "container.containerConcept",
                () -> InformationVocabulary.rawContainer(null, "application/octet-stream"));
        expectFailure(InformationFailureCategory.MISSING_INPUT, "container.containerSchema",
                () -> ContainerMetadata.of(InformationFixtures.topic("Container", "File"), null,
                        "application/octet-stream"));
    }

    @Test
    void mediaTypesAreNormalisedAndRemainSeparateFromConceptAndSchema() {
        Topic concept = InformationFixtures.topic("ContentConcept", "Patient");
        Topic schema = InformationVocabulary.fhirR4ResourceSchema(FhirR4ResourceType.Patient);
        ContentMetadata content = ContentMetadata.of(concept, schema, "APPLICATION/FHIR+JSON", ContentDetail.empty());
        ContainerMetadata container = InformationVocabulary.rawContainer(
                InformationFixtures.topic("Container", "Payload"), "Application/Octet-Stream");

        assertEquals("application/fhir+json", content.getContentMediaType());
        assertEquals("application/octet-stream", container.getContainerMediaType());
        assertEquals(concept, content.getContentConcept());
        assertEquals(schema, content.getContentSchema());
    }

    @Test
    void mediaTypeInputsAreRequiredAndValidatedWithoutEchoingTheirValues() {
        Topic concept = InformationFixtures.topic("ContentConcept", "Patient");
        Topic schema = InformationVocabulary.fhirR4ResourceSchema(FhirR4ResourceType.Patient);
        for (String invalid : new String[] {"", "application", "/json", "application/", "application/fhir/json",
                "application/fhir+json; charset=UTF-8", "unknown/subtype", "application/bad value",
                "application/bad%value", "application/" + "a".repeat(128)}) {
            expectFailure(InformationFailureCategory.INVALID_INPUT, "contentMetadata.contentMediaType",
                    () -> ContentMetadata.of(concept, schema, invalid, ContentDetail.empty()));
            expectFailure(InformationFailureCategory.INVALID_INPUT, "container.containerMediaType",
                    () -> ContainerMetadata.of(concept, schema, invalid));
        }
        expectFailure(InformationFailureCategory.MISSING_INPUT, "contentMetadata.contentMediaType",
                () -> ContentMetadata.of(concept, schema, null, ContentDetail.empty()));
        expectFailure(InformationFailureCategory.MISSING_INPUT, "container.containerMediaType",
                () -> ContainerMetadata.of(concept, schema, null));
    }

    @Test
    void inlineTextAndBinaryContentConstructWithoutAnyLocator() {
        byte[] binary = {0, (byte) 0xFF, 0x7F, (byte) 0x80, 10, 13};
        Content text = Content.inlineText(PATIENT_JSON);
        Content bytes = Content.inline(binary);
        Content empty = Content.inline(new byte[0]);

        assertArrayEquals(PATIENT_JSON.getBytes(StandardCharsets.UTF_8), text.getData().orElseThrow());
        assertArrayEquals(binary, bytes.getData().orElseThrow());
        assertEquals(0, empty.getData().orElseThrow().length);
        assertTrue(text.isInline() && text.getReference().isEmpty() && bytes.getReference().isEmpty());
        InformationUnit unit = InformationFixtures.unit();
        assertTrue(unit.getRepresentation().getContent().getReference().isEmpty(),
                "an IU can exist before any persistence reference");
    }

    @Test
    void referenceOnlyContentKeepsTheOpaqueLocatorAndHasNoData() {
        URI locator = URI.create("https://store.example.org/objects/abc?version=2");
        Content content = Content.reference(locator);

        assertEquals(locator, content.getReference().orElseThrow());
        assertTrue(content.getData().isEmpty());
        assertFalse(content.isInline());
        assertEquals("Content{carrier=reference}", content.toString());
        expectFailure(InformationFailureCategory.INVALID_INPUT, "content.reference",
                () -> Content.reference(URI.create("objects/abc")));
        expectFailure(InformationFailureCategory.MISSING_INPUT, "content.reference", () -> Content.reference(null));
        expectFailure(InformationFailureCategory.MISSING_INPUT, "content.data", () -> Content.inline(null));
        expectFailure(InformationFailureCategory.MISSING_INPUT, "content.data", () -> Content.inlineText(null));
    }

    @Test
    void bothAndNeitherCarrierStatesAreUnrepresentable() {
        long publicConstructors = Content.class.getConstructors().length;
        long factories = Arrays.stream(Content.class.getDeclaredMethods())
                .filter(method -> Modifier.isStatic(method.getModifiers()))
                .filter(method -> method.getReturnType() == Content.class).count();

        assertEquals(0, publicConstructors);
        assertEquals(3, factories, "inline, inlineText and reference each populate exactly one carrier");
    }

    @Test
    void inlineBytesAreDefensivelyCopiedInBothDirections() {
        byte[] supplied = {1, 2, 3};
        Content content = Content.inline(supplied);
        supplied[0] = 9;
        byte[] returned = content.getData().orElseThrow();
        returned[1] = 9;

        assertArrayEquals(new byte[] {1, 2, 3}, content.getData().orElseThrow());
        assertEquals(Content.inline(new byte[] {1, 2, 3}), content);
        assertEquals("Content{carrier=inline}", content.toString());
    }

    @Test
    void providerIntegrityMetadataIsCarriedExactlyAndMatchesTheFixtureBytes() throws Exception {
        byte[] bytes = PATIENT_JSON.getBytes(StandardCharsets.UTF_8);
        String computed = HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(bytes));
        ContentDetail detail = ContentDetail.builder().size((long) bytes.length).language("en-AU")
                .checksum(computed, ContentDetail.DEFAULT_CHECKSUM_ALGORITHM).build();

        assertEquals(PATIENT_JSON_SHA256, computed, "fixture bytes match the externally computed digest");
        assertEquals(PATIENT_JSON_SHA256, detail.getChecksum().orElseThrow());
        assertEquals("SHA-256", detail.getChecksumAlgorithm().orElseThrow());
        assertEquals(45L, detail.getSize().orElseThrow());
        assertArrayEquals(bytes, Content.inlineText(PATIENT_JSON).getData().orElseThrow());
        assertEquals("ContentDetail{sizePresent=true, checksumPresent=true}", detail.toString());
    }

    @Test
    void checksumAndAlgorithmMustTravelTogetherAndAreNeverDefaultedSilently() {
        ContentDetailBuilder builder = ContentDetail.builder().checksum("abc", "SHA-256");

        expectFailure(InformationFailureCategory.INCONSISTENT_INPUT, "contentDetail.checksum",
                () -> builder.checksum("def", null));
        expectFailure(InformationFailureCategory.INCONSISTENT_INPUT, "contentDetail.checksum",
                () -> builder.checksum(null, "SHA-256"));
        expectFailure(InformationFailureCategory.INVALID_INPUT, "contentDetail.checksum",
                () -> builder.checksum("ab c", "SHA-256"));
        expectFailure(InformationFailureCategory.INVALID_INPUT, "contentDetail.checksumAlgorithm",
                () -> builder.checksum("abc", ""));
        assertEquals("abc", builder.build().getChecksum().orElseThrow(), "failed calls keep the accepted value");
        assertTrue(builder.checksum(null, null).build().getChecksumAlgorithm().isEmpty());
    }

    @Test
    void contentDetailValuesAreOptionalValidatedAndPreservedOnFailure() {
        ContentDetailBuilder builder = ContentDetail.builder().size(0L).height(480L).width(640L).frames(1L)
                .duration(Duration.ofSeconds(3)).pages(2L).language("zh-Hant-TW");

        expectFailure(InformationFailureCategory.INVALID_INPUT, "contentDetail.size", () -> builder.size(-1L));
        expectFailure(InformationFailureCategory.INVALID_INPUT, "contentDetail.height", () -> builder.height(0L));
        expectFailure(InformationFailureCategory.INVALID_INPUT, "contentDetail.width", () -> builder.width(-5L));
        expectFailure(InformationFailureCategory.INVALID_INPUT, "contentDetail.frames", () -> builder.frames(0L));
        expectFailure(InformationFailureCategory.INVALID_INPUT, "contentDetail.pages", () -> builder.pages(0L));
        expectFailure(InformationFailureCategory.INVALID_INPUT, "contentDetail.duration",
                () -> builder.duration(Duration.ofSeconds(-1)));
        for (String bad : new String[] {"", "en_AU", "-en", "en-", "en--AU", "abcdefghi"}) {
            expectFailure(InformationFailureCategory.INVALID_INPUT, "contentDetail.language",
                    () -> builder.language(bad));
        }
        ContentDetail detail = builder.build();
        assertEquals(0L, detail.getSize().orElseThrow());
        assertEquals(480L, detail.getHeight().orElseThrow());
        assertEquals("zh-Hant-TW", detail.getLanguage().orElseThrow());
        assertEquals(detail, builder.build());
        assertTrue(ContentDetail.empty().getSize().isEmpty() && ContentDetail.empty().getPages().isEmpty());
    }

    @Test
    void representationRequiresEveryConstituentAndComparesByValue() {
        InformationRepresentation representation = InformationFixtures.representation();

        assertEquals(representation, InformationFixtures.representation());
        expectFailure(InformationFailureCategory.MISSING_INPUT, "representation.content",
                () -> InformationRepresentation.of(representation.getContainerMetadata(),
                        representation.getContentMetadata(), null));
        expectFailure(InformationFailureCategory.MISSING_INPUT, "representation.containerMetadata",
                () -> InformationRepresentation.of(null, representation.getContentMetadata(),
                        representation.getContent()));
        expectFailure(InformationFailureCategory.MISSING_INPUT, "contentMetadata.contentDetail",
                () -> ContentMetadata.of(InformationFixtures.topic("C", "X"), InformationFixtures.topic("S", "Y"),
                        "application/octet-stream", null));
        assertFalse(representation.toString().contains("Observation\"}"), "payload is never rendered");
    }
}
