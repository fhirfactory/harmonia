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

package net.fhirfactory.harmonia.model.core.workflow.tasks.factory;

import net.fhirfactory.harmonia.model.core.workflow.tasks.ErgonPayload;
import net.fhirfactory.harmonia.model.core.naming.Topic;
import net.fhirfactory.harmonia.model.core.naming.factory.TopicFactory;
import com.fasterxml.jackson.databind.JsonNode;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.Reference;
import org.hl7.fhir.r5.model.Resource;

/**
 * Factory for creating standard {@link ErgonPayload} instances from diverse payload representations.
 */
public final class ErgonPayloadFactory {

    private ErgonPayloadFactory() {
        // Utility factory
    }

    public static ErgonPayload fromFhirResource(Resource resource, Topic containerTopic, Topic contentTopic, Integer order) {
        ErgonPayload p = new ErgonPayload();
        p.setPayloadOrder(order);
        p.setPayloadContainer(containerTopic);
        p.setPayloadContent(contentTopic);
        p.setResource(resource);
        return p;
    }

    /** Creates an ordered FHIR resource payload while retaining the established caller argument order. */
    public static ErgonPayload fromFhirResource(Integer order, Topic containerTopic, Topic contentTopic, Resource resource) {
        return fromFhirResource(resource, containerTopic, contentTopic, order);
    }

    /** Creates a payload from any FHIR resource accepted by the Harmonia Task contract. */
    public static ErgonPayload fromFhirResource(
            Integer order,
            Topic containerTopic,
            Topic contentTopic,
            IBaseResource resource) {
        return new ErgonPayload(order, containerTopic, contentTopic, resource);
    }

    public static ErgonPayload fromFhirResource(Resource resource, Topic containerTopic, Topic contentTopic) {
        return fromFhirResource(resource, containerTopic, contentTopic, 0);
    }

    public static ErgonPayload fromFhirResource(Resource resource) {
        Topic container = resource != null ? TopicFactory.fromFhir(resource.fhirType(), "CONTAINER") : null;
        Topic content = resource != null ? TopicFactory.fromFhir(resource.fhirType(), "CONTENT") : null;
        return fromFhirResource(resource, container, content, 0);
    }

    /** Creates a payload from a FHIR reference without interpreting its target. */
    public static ErgonPayload fromReference(Reference reference, Topic containerTopic, Topic contentTopic, Integer order) {
        return new ErgonPayload(order, containerTopic, contentTopic, reference);
    }

    /** Creates an ordered payload from a FHIR reference without resolving its target. */
    public static ErgonPayload fromReference(Integer order, Topic containerTopic, Topic contentTopic, Reference reference) {
        return fromReference(reference, containerTopic, contentTopic, order);
    }

    /** Creates an ordered payload from a reference string without resolving its target. */
    public static ErgonPayload fromReference(Integer order, Topic containerTopic, Topic contentTopic, String reference) {
        return fromReference(reference, containerTopic, contentTopic, order);
    }

    /** Creates a payload from a reference string without resolving it. */
    public static ErgonPayload fromReference(String reference, Topic containerTopic, Topic contentTopic, Integer order) {
        ErgonPayload payload = new ErgonPayload();
        payload.setPayloadOrder(order);
        payload.setPayloadContainer(containerTopic);
        payload.setPayloadContent(contentTopic);
        if (reference != null && !reference.isBlank()) {
            payload.setResourceReference(reference.trim());
        }
        return payload;
    }

    /** Creates an ordered JSON payload from its serialized string. */
    public static ErgonPayload fromJson(String json, Topic containerTopic, Topic contentTopic, Integer order) {
        ErgonPayload payload = new ErgonPayload();
        payload.setPayloadOrder(order);
        payload.setPayloadContainer(containerTopic);
        payload.setPayloadContent(contentTopic);
        payload.setJsonObject(json);
        return payload;
    }

    /** Creates a JSON payload using the established order-first argument arrangement. */
    public static ErgonPayload fromJson(Integer order, Topic containerTopic, Topic contentTopic, String json) {
        return fromJson(json, containerTopic, contentTopic, order);
    }

    /** Creates a JSON payload from a parsed JSON tree. */
    public static ErgonPayload fromJson(JsonNode json, Topic containerTopic, Topic contentTopic, Integer order) {
        return new ErgonPayload(order, containerTopic, contentTopic, json);
    }

    /** Creates a JSON-tree payload using the established order-first argument arrangement. */
    public static ErgonPayload fromJson(Integer order, Topic containerTopic, Topic contentTopic, JsonNode json) {
        return fromJson(json, containerTopic, contentTopic, order);
    }

    public static ErgonPayload fromJsonObject(Object object, Topic containerTopic, Topic contentTopic, Integer order) {
        ErgonPayload p = new ErgonPayload();
        p.setPayloadOrder(order);
        p.setPayloadContainer(containerTopic);
        p.setPayloadContent(contentTopic);
        p.setJsonObject(object);
        return p;
    }

    /** Creates a JSON payload using the established order-first argument arrangement. */
    public static ErgonPayload fromJsonObject(Integer order, Topic containerTopic, Topic contentTopic, Object object) {
        return fromJsonObject(object, containerTopic, contentTopic, order);
    }

    public static ErgonPayload fromJsonObject(Object object, Topic containerTopic, Topic contentTopic) {
        return fromJsonObject(object, containerTopic, contentTopic, 0);
    }

    public static ErgonPayload fromJsonObject(Object object) {
        return fromJsonObject(object, null, null, 0);
    }

    public static ErgonPayload fromString(String text, Topic containerTopic, Topic contentTopic, Integer order) {
        ErgonPayload p = new ErgonPayload();
        p.setPayloadOrder(order);
        p.setPayloadContainer(containerTopic);
        p.setPayloadContent(contentTopic);
        p.setJsonObject(text);
        return p;
    }

    public static ErgonPayload fromString(String text, Topic containerTopic, Topic contentTopic) {
        return fromString(text, containerTopic, contentTopic, 0);
    }

    public static ErgonPayload fromString(String text) {
        return fromString(text, null, null, 0);
    }

    public static ErgonPayload fromBytes(byte[] bytes, Topic containerTopic, Topic contentTopic, Integer order) {
        ErgonPayload p = new ErgonPayload();
        p.setPayloadOrder(order);
        p.setPayloadContainer(containerTopic);
        p.setPayloadContent(contentTopic);
        if (bytes != null) {
            p.setJsonObject(new String(bytes, java.nio.charset.StandardCharsets.UTF_8));
        }
        return p;
    }

    public static ErgonPayload fromBytes(byte[] bytes, Topic containerTopic, Topic contentTopic) {
        return fromBytes(bytes, containerTopic, contentTopic, 0);
    }

    public static ErgonPayload fromBytes(byte[] bytes) {
        return fromBytes(bytes, null, null, 0);
    }
}
