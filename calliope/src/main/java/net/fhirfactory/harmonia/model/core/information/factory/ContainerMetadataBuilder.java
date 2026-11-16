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

package net.fhirfactory.harmonia.model.core.information.factory;

import net.fhirfactory.harmonia.model.core.information.ContainerMetadata;
import net.fhirfactory.harmonia.model.core.naming.Topic;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Fluent builder for creating {@link ContainerMetadata} instances.
 */
public class ContainerMetadataBuilder {

    protected Topic type;
    protected Topic sourceTopic;
    protected Topic destinationTopic;
    protected Topic authorTopic;
    protected Topic recipientTopic;
    protected String assemblyContext;
    protected String interactionMechanism;
    protected String envelopeStructure;
    protected final Map<String, String> attributes = new LinkedHashMap<>();

    public ContainerMetadataBuilder() {
    }

    public ContainerMetadataBuilder type(Topic type) {
        this.type = type;
        return this;
    }

    public ContainerMetadataBuilder containerType(String containerType) {
        this.type = containerType != null && !containerType.isBlank() ? Topic.fromName(containerType.trim()) : null;
        return this;
    }

    public ContainerMetadataBuilder sourceTopic(Topic sourceTopic) {
        this.sourceTopic = sourceTopic;
        return this;
    }

    public ContainerMetadataBuilder source(String source) {
        this.sourceTopic = source != null && !source.isBlank() ? Topic.fromName(source.trim()) : null;
        return this;
    }

    public ContainerMetadataBuilder destinationTopic(Topic destinationTopic) {
        this.destinationTopic = destinationTopic;
        return this;
    }

    public ContainerMetadataBuilder destination(String destination) {
        this.destinationTopic = destination != null && !destination.isBlank() ? Topic.fromName(destination.trim()) : null;
        return this;
    }

    public ContainerMetadataBuilder authorTopic(Topic authorTopic) {
        this.authorTopic = authorTopic;
        return this;
    }

    public ContainerMetadataBuilder author(String author) {
        this.authorTopic = author != null && !author.isBlank() ? Topic.fromName(author.trim()) : null;
        return this;
    }

    public ContainerMetadataBuilder recipientTopic(Topic recipientTopic) {
        this.recipientTopic = recipientTopic;
        return this;
    }

    public ContainerMetadataBuilder recipient(String recipient) {
        this.recipientTopic = recipient != null && !recipient.isBlank() ? Topic.fromName(recipient.trim()) : null;
        return this;
    }

    public ContainerMetadataBuilder assemblyContext(String assemblyContext) {
        this.assemblyContext = assemblyContext;
        return this;
    }

    public ContainerMetadataBuilder interactionMechanism(String interactionMechanism) {
        this.interactionMechanism = interactionMechanism;
        return this;
    }

    public ContainerMetadataBuilder envelopeStructure(String envelopeStructure) {
        this.envelopeStructure = envelopeStructure;
        return this;
    }

    public ContainerMetadataBuilder attributes(Map<String, String> attributes) {
        this.attributes.clear();
        if (attributes != null) {
            this.attributes.putAll(attributes);
        }
        return this;
    }

    public ContainerMetadataBuilder putAttribute(String key, String value) {
        return attribute(key, value);
    }

    public ContainerMetadataBuilder attribute(String key, String value) {
        if (key != null) {
            if (value == null) {
                this.attributes.remove(key);
            } else {
                this.attributes.put(key, value);
            }
        }
        return this;
    }

    public ContainerMetadata build() {
        return new ContainerMetadata(type, sourceTopic, destinationTopic, authorTopic, recipientTopic,
                assemblyContext, interactionMechanism, envelopeStructure, attributes);
    }
}
