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

/**
 * Standard constants, type systems, and extension URLs for {@link ErgonPayload} handling.
 */
public final class ErgonPayloadConstants {

    public static final String TASK_PAYLOAD_TYPE_SYSTEM = "http://fhirfactory.net/harmonia/task/payload-type";
    public static final String TASK_PAYLOAD_TYPE_JSON_OBJECT = "json-object";
    public static final String TASK_PAYLOAD_TYPE_STRING = "string";
    public static final String TASK_PAYLOAD_TYPE_BYTES = "bytes";
    public static final String TASK_PAYLOAD_TYPE_RESOURCE = "fhir-resource";

    public static final String EXTENSION_PAYLOAD_ORDER = "http://fhirfactory.net/harmonia/task/payload-order";
    public static final String EXTENSION_PAYLOAD_CONTAINER = "http://fhirfactory.net/harmonia/task/payload-container";
    public static final String EXTENSION_PAYLOAD_CONTENT = "http://fhirfactory.net/harmonia/task/payload-content";

    public static final String LEGACY_EXTENSION_PAYLOAD_ORDER = "http://fhirfactory.net/hie/task/payload-order";
    public static final String LEGACY_EXTENSION_PAYLOAD_CONTAINER = "http://fhirfactory.net/hie/task/payload-container";
    public static final String LEGACY_EXTENSION_PAYLOAD_CONTENT = "http://fhirfactory.net/hie/task/payload-content";

    private ErgonPayloadConstants() {
        // Constants class
    }
}
