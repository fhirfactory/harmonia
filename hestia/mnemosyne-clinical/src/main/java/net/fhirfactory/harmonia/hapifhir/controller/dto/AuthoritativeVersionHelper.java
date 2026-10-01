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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.fhirfactory.harmonia.hapifhir.controller.dto;

import net.fhirfactory.harmonia.model.governedwrite.AuthoritativeVersion;
import net.fhirfactory.harmonia.model.governedwrite.ExpectedAuthoritativeVersion;

import java.util.Optional;

/**
 * Utility helper for parsing and formatting HTTP headers associated with authoritative versioning.
 * <p>
 * Preserves strict domain separation between:
 * <ol>
 *   <li>FHIR {@code meta.versionId}</li>
 *   <li>HTTP {@code ETag}</li>
 *   <li>Mneme active-state token</li>
 *   <li>Mnemosyne {@link AuthoritativeVersion}</li>
 * </ol>
 */
public final class AuthoritativeVersionHelper {

    public static final String HEADER_ETAG = "ETag";
    public static final String HEADER_IF_NONE_MATCH = "If-None-Match";
    public static final String HEADER_IF_MATCH = "If-Match";
    public static final String HEADER_X_AUTHORITATIVE_VERSION = "X-Harmonia-Authoritative-Version";
    public static final String MIME_FHIR_JSON = "application/fhir+json; charset=UTF-8";

    private AuthoritativeVersionHelper() {
        // Utility class
    }

    /**
     * Cleans an ETag string by stripping weak indicator prefixes ({@code W/}, {@code w/})
     * and surrounding double quotes.
     *
     * @param raw the raw header value
     * @return cleaned version string, trimmed
     */
    public static String cleanEtag(String raw) {
        if (raw == null) {
            return "";
        }
        String val = raw.trim();
        if (val.startsWith("W/") || val.startsWith("w/")) {
            val = val.substring(2);
        }
        if (val.startsWith("\"") && val.endsWith("\"") && val.length() >= 2) {
            val = val.substring(1, val.length() - 1);
        }
        return val.trim();
    }

    /**
     * Formats an {@link AuthoritativeVersion} as a weak HTTP ETag header value (e.g. {@code W/"1"}).
     *
     * @param version the authoritative version
     * @return formatted ETag or {@code null} if version is null or empty
     */
    public static String formatEtag(AuthoritativeVersion version) {
        if (version == null || version.value() == null || version.value().isBlank()) {
            return null;
        }
        return "W/\"" + version.value() + "\"";
    }

    /**
     * Formats a raw version value string as a weak HTTP ETag header value.
     *
     * @param versionValue raw version value
     * @return formatted ETag or {@code null} if value is null or blank
     */
    public static String formatEtag(String versionValue) {
        if (versionValue == null || versionValue.isBlank()) {
            return null;
        }
        String clean = cleanEtag(versionValue);
        if (clean.isBlank()) {
            return null;
        }
        return "W/\"" + clean + "\"";
    }

    /**
     * Parses the {@code If-Match} HTTP precondition header into an {@link ExpectedAuthoritativeVersion}.
     *
     * @param rawIfMatch the raw {@code If-Match} header
     * @return optional containing the parsed expected version, or empty if header is missing, blank, or invalid
     */
    public static Optional<ExpectedAuthoritativeVersion> parseIfMatch(String rawIfMatch) {
        if (rawIfMatch == null || rawIfMatch.isBlank()) {
            return Optional.empty();
        }
        String trimmed = rawIfMatch.trim();
        if ("*".equals(trimmed)) {
            return Optional.empty();
        }
        String clean = cleanEtag(trimmed);
        if (clean.isBlank()) {
            return Optional.empty();
        }
        return Optional.of(ExpectedAuthoritativeVersion.of(clean));
    }
}
