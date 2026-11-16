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

package net.fhirfactory.harmonia.model.ergon;

import net.fhirfactory.harmonia.model.core.naming.Topic;
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

    public static ErgonPayload fromFhirResource(Resource resource, Topic containerTopic, Topic contentTopic) {
        return fromFhirResource(resource, containerTopic, contentTopic, 0);
    }

    public static ErgonPayload fromFhirResource(Resource resource) {
        Topic container = resource != null ? Topic.fromFhir(resource.fhirType(), "CONTAINER") : null;
        Topic content = resource != null ? Topic.fromFhir(resource.fhirType(), "CONTENT") : null;
        return fromFhirResource(resource, container, content, 0);
    }

    public static ErgonPayload fromJsonObject(Object object, Topic containerTopic, Topic contentTopic, Integer order) {
        ErgonPayload p = new ErgonPayload();
        p.setPayloadOrder(order);
        p.setPayloadContainer(containerTopic);
        p.setPayloadContent(contentTopic);
        p.setJsonObject(object);
        return p;
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
