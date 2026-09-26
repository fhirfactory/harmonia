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

package net.fhirfactory.harmonia.hestia.mneme.convergence;

import ca.uhn.fhir.context.FhirContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.fhirfactory.harmonia.model.governedwrite.AuthoritativeVersion;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.Extension;
import org.hl7.fhir.r5.model.Integer64Type;
import org.hl7.fhir.r5.model.Meta;
import org.hl7.fhir.r5.model.Resource;

import java.util.Objects;

/**
 * Helper for attaching and extracting authoritative-version provenance metadata
 * to/from cached FHIR resources in Mneme.
 * <p>
 * Embeds provenance directly into standard FHIR {@code Resource.meta.extension}
 * using canonical URI {@code http://harmonia.fhirfactory.net/structure/authoritative-version}.
 * This preserves 100% compatibility with existing FHIR parsers and cache consumers
 * while guaranteeing single-entry atomic updates without dual-cache consistency hazards.
 */
public final class MnemeCachedResource {

    public static final String AUTHORITATIVE_VERSION_EXT_URL =
            "http://harmonia.fhirfactory.net/structure/authoritative-version";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private MnemeCachedResource() {
        // Utility class
    }

    /**
     * Attaches explicit authoritative version provenance to the given FHIR resource's {@code meta.extension}.
     *
     * @param resource the FHIR resource
     * @param version  the authoritative version
     */
    public static void attachAuthoritativeVersion(IBaseResource resource, AuthoritativeVersion version) {
        Objects.requireNonNull(resource, "resource must not be null");
        Objects.requireNonNull(version, "version must not be null");

        if (resource instanceof Resource r5Resource) {
            Meta meta = r5Resource.getMeta();
            if (meta == null) {
                meta = new Meta();
                r5Resource.setMeta(meta);
            }
            meta.removeExtension(AUTHORITATIVE_VERSION_EXT_URL);
            meta.addExtension(new Extension(AUTHORITATIVE_VERSION_EXT_URL, new Integer64Type(version.longValue())));
        }
    }

    /**
     * Converts the committed resource into a JSON payload string with authoritative version attached.
     *
     * @param resource    the committed resource (either {@link IBaseResource} or JSON string)
     * @param version     the authoritative version
     * @param fhirContext the HAPI FHIR context
     * @param <T>         the resource type
     * @return the encoded FHIR JSON string
     */
    public static <T> String toPayloadJson(T resource, AuthoritativeVersion version, FhirContext fhirContext) {
        Objects.requireNonNull(resource, "resource must not be null");
        Objects.requireNonNull(version, "version must not be null");
        Objects.requireNonNull(fhirContext, "fhirContext must not be null");

        if (resource instanceof IBaseResource baseResource) {
            attachAuthoritativeVersion(baseResource, version);
            return fhirContext.newJsonParser().encodeResourceToString(baseResource);
        } else if (resource instanceof String jsonStr) {
            try {
                IBaseResource parsed = fhirContext.newJsonParser().parseResource(jsonStr);
                attachAuthoritativeVersion(parsed, version);
                return fhirContext.newJsonParser().encodeResourceToString(parsed);
            } catch (Exception e) {
                return jsonStr;
            }
        } else {
            throw new IllegalArgumentException("Unsupported resource payload type: " + resource.getClass().getName());
        }
    }

    /**
     * Extracts the authoritative version from a cached JSON resource string.
     * <p>
     * If the entry lacks authoritative version extension (e.g. legacy cache entries)
     * or is unparseable, returns {@code 0L}.
     *
     * @param json the cached JSON string
     * @return the extracted authoritative version, or 0 if absent/invalid
     */
    public static long extractAuthoritativeVersion(String json) {
        if (json == null || json.isBlank()) {
            return 0L;
        }
        try {
            JsonNode root = OBJECT_MAPPER.readTree(json);
            JsonNode meta = root.get("meta");
            if (meta != null && meta.has("extension")) {
                JsonNode extensions = meta.get("extension");
                if (extensions.isArray()) {
                    for (JsonNode ext : extensions) {
                        if (ext.has("url") && AUTHORITATIVE_VERSION_EXT_URL.equals(ext.get("url").asText())) {
                            if (ext.has("valueInteger64")) {
                                return ext.get("valueInteger64").asLong();
                            } else if (ext.has("valueInteger")) {
                                return ext.get("valueInteger").asLong();
                            } else if (ext.has("valueDecimal")) {
                                return ext.get("valueDecimal").asLong();
                            } else if (ext.has("valueString")) {
                                return Long.parseLong(ext.get("valueString").asText());
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {
            // Legacy, invalid, or non-FHIR entries default to version 0
            return 0L;
        }
        return 0L;
    }
}
