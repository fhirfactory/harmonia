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

package net.fhirfactory.harmonia.model.core.information.entity.datatypes;

import net.fhirfactory.harmonia.model.core.information.entity.valuesets.ContentMode;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Verifies portable content modes, defensive validation, and safe diagnostics.
 */
class ContentTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void portableContentModesRoundTripWithoutChangingTheirMeaning() throws Exception {
        Content text = Content.text("portable text");
        Content binary = Content.binary(new byte[]{1, 2, 3});
        Content reference = Content.byReference("urn:content:opaque-123");

        assertThat(objectMapper.readValue(objectMapper.writeValueAsBytes(text), Content.class)).isEqualTo(text);
        assertThat(objectMapper.readValue(objectMapper.writeValueAsBytes(binary), Content.class)).isEqualTo(binary);
        assertThat(objectMapper.readValue(objectMapper.writeValueAsBytes(reference), Content.class)).isEqualTo(reference);
    }

    @Test
    void portableContentModesRoundTripThroughJavaSerialization() throws Exception {
        List<Content> contents = List.of(
                Content.text("portable text"),
                Content.binary(new byte[]{1, 2, 3}),
                Content.byReference("urn:content:opaque-123"));

        for (Content content : contents) {
            assertThat(javaSerializeRoundTrip(content)).isEqualTo(content);
        }
    }

    @Test
    void workingManifestationsCannotBeWrittenAsJson() {
        Content working = Content.working(new Object());

        assertThatThrownBy(() -> objectMapper.writeValueAsString(working))
                .isInstanceOf(JsonProcessingException.class);
    }

    @Test
    void workingManifestationsCannotBeWrittenAsJavaSerialization() {
        Content working = Content.working(new Object());

        assertThatThrownBy(() -> writeJavaObject(working))
                .isInstanceOf(NotSerializableException.class);
    }

    @Test
    void textModeRejectsMissingTextPayload() {
        assertThatThrownBy(() -> new Content(ContentMode.TEXT, null, null, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void binaryModeRejectsConflictingTextPayload() {
        assertThatThrownBy(() -> new Content(ContentMode.BINARY, "text", new byte[]{1}, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void referenceModeRejectsBlankReference() {
        assertThatThrownBy(() -> new Content(ContentMode.REFERENCE, null, null, " "))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void workingModeCannotBeConstructedWithoutItsManifestation() {
        assertThatThrownBy(() -> new Content(ContentMode.WORKING, null, null, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void diagnosticsDoNotExposeTextOrByReferenceIdentifiers() {
        String text = "patient-secret-42";
        String reference = "urn:clinical-content:patient-secret-42";

        assertThat(Content.text(text).toString()).doesNotContain(text);
        assertThat(Content.byReference(reference).toString()).doesNotContain(reference).doesNotContain(text);
    }

    private static void writeJavaObject(Object object) throws Exception {
        try (ObjectOutputStream output = new ObjectOutputStream(new ByteArrayOutputStream())) {
            output.writeObject(object);
        }
    }

    private static Content javaSerializeRoundTrip(Content content) throws Exception {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (ObjectOutputStream output = new ObjectOutputStream(bytes)) {
            output.writeObject(content);
        }
        try (ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()))) {
            return (Content) input.readObject();
        }
    }
}