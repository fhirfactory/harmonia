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

package net.fhirfactory.harmonia.model.core.information.entity.factory;

import net.fhirfactory.harmonia.model.core.information.entity.datatypes.ContentMetadata;
import net.fhirfactory.harmonia.model.core.naming.Topic;

import java.util.LinkedHashMap;
import java.util.Map;

/** Assembles the structure, format, reference, and integrity metadata for Content. */
public final class ContentMetadataBuilder {

    private Topic contentStructureTopic;
    private String contentStructureVersion;
    private String contentFormat;
    private String contentReference;
    private String contentDigest;
    private Long contentSize;
    private final Map<String, String> attributes = new LinkedHashMap<>();

    /** Sets the Topic that identifies the content structure. */
    public ContentMetadataBuilder contentStructureTopic(Topic topic) {
        contentStructureTopic = topic;
        return this;
    }

    /** Sets the version of the identified content structure. */
    public ContentMetadataBuilder contentStructureVersion(String version) {
        contentStructureVersion = version;
        return this;
    }

    /** Sets the media format of the represented content. */
    public ContentMetadataBuilder contentFormat(String format) {
        contentFormat = format;
        return this;
    }

    /** Sets the opaque reference for external or separately managed content. */
    public ContentMetadataBuilder contentReference(String reference) {
        contentReference = reference;
        return this;
    }

    /** Sets the content integrity digest. */
    public ContentMetadataBuilder contentDigest(String digest) {
        contentDigest = digest;
        return this;
    }

    /** Sets the content size in bytes. */
    public ContentMetadataBuilder contentSize(Long size) {
        contentSize = size;
        return this;
    }

    /** Adds a non-null metadata attribute. */
    public ContentMetadataBuilder putAttribute(String key, String value) {
        if (key != null && value != null) {
            attributes.put(key.trim(), value.trim());
        }
        return this;
    }

    /** Replaces all additional metadata attributes. */
    public ContentMetadataBuilder attributes(Map<String, String> attributes) {
        this.attributes.clear();
        if (attributes != null) {
            this.attributes.putAll(attributes);
        }
        return this;
    }

    /** Builds an immutable ContentMetadata value. */
    public ContentMetadata build() {
        return new ContentMetadata(contentStructureTopic, contentStructureVersion, contentFormat,
                contentReference, contentDigest, contentSize, attributes);
    }
}