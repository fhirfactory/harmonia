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

package net.fhirfactory.harmonia.befe.config;

import java.net.URI;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Authoritative CORS configuration manager for Iris BEFE.
 * <p>
 * Controls allowed browser origins, headers, methods, and preflight max age.
 * Restricts cross-origin requests exclusively to explicitly configured, normalized trusted origins.
 * Fails closed if no configuration is provided.
 * </p>
 */
public final class BefeCorsConfig {

    public static final String PROPERTY_ALLOWED_ORIGINS = "harmonia.befe.cors.allowed-origins";
    public static final String ENV_ALLOWED_ORIGINS = "HARMONIA_BEFE_CORS_ALLOWED_ORIGINS";
    public static final String ALLOWED_METHODS = "GET, POST, PUT, DELETE, PATCH, HEAD, OPTIONS";
    public static final String ALLOWED_HEADERS = "Authorization, Content-Type, Accept, X-Correlation-Id";
    public static final String MAX_AGE_SECONDS = "86400";

    private static volatile String overrideAllowedOrigins = null;
    private static volatile boolean overrideActive = false;

    private BefeCorsConfig() {
        // utility class
    }

    /**
     * Resolves the configured set of trusted normalized origins.
     * Evaluates in-memory test overrides, followed by system property, followed by environment variable.
     * Defaults to an empty set (fail closed).
     *
     * @return unmodifiable set of normalized trusted origins
     */
    public static Set<String> getAllowedOrigins() {
        String originsStr;
        if (overrideActive) {
            originsStr = overrideAllowedOrigins;
        } else {
            originsStr = System.getProperty(PROPERTY_ALLOWED_ORIGINS);
            if (originsStr == null || originsStr.isBlank()) {
                originsStr = System.getenv(ENV_ALLOWED_ORIGINS);
            }
        }

        if (originsStr == null || originsStr.isBlank()) {
            return Collections.emptySet();
        }

        Set<String> set = new LinkedHashSet<>();
        for (String token : originsStr.split(",")) {
            String normalized = normalizeOrigin(token);
            if (normalized != null) {
                set.add(normalized);
            }
        }
        return Collections.unmodifiableSet(set);
    }

    /**
     * Normalizes a raw origin string into standard (scheme://host[:port]) form.
     * Schemes and hosts are lowercased. Default ports (80 for http, 443 for https) are omitted.
     * Disallows user info, query parameters, fragments, and paths beyond empty or single slash.
     * Rejects wildcards, unknown schemes, and malformed URIs.
     *
     * @param rawOrigin raw origin string
     * @return normalized origin string, or null if invalid
     */
    public static String normalizeOrigin(String rawOrigin) {
        if (rawOrigin == null || rawOrigin.isBlank()) {
            return null;
        }
        String trimmed = rawOrigin.trim();
        // Wildcard is strictly forbidden as an origin
        if ("*".equals(trimmed)) {
            return null;
        }

        try {
            URI uri = URI.create(trimmed);
            String scheme = uri.getScheme();
            if (scheme == null) {
                return null;
            }
            scheme = scheme.toLowerCase(Locale.ROOT);
            if (!"http".equals(scheme) && !"https".equals(scheme)) {
                return null;
            }

            String host = uri.getHost();
            if (host == null || host.isBlank()) {
                return null;
            }
            host = host.toLowerCase(Locale.ROOT);
            if (host.contains(":") && !host.startsWith("[")) {
                host = "[" + host + "]";
            }

            // Disallow userInfo, query, fragment
            if (uri.getUserInfo() != null || uri.getRawQuery() != null || uri.getRawFragment() != null) {
                return null;
            }

            // Disallow path beyond empty or single slash
            String path = uri.getRawPath();
            if (path != null && !path.isEmpty() && !"/".equals(path)) {
                return null;
            }

            int port = uri.getPort();
            boolean isDefaultPort = ("http".equals(scheme) && port == 80) || ("https".equals(scheme) && port == 443);
            if (port == -1 || isDefaultPort) {
                return scheme + "://" + host;
            } else if (port > 0 && port <= 65535) {
                return scheme + "://" + host + ":" + port;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Checks whether the given origin is trusted according to configured allowed origins.
     *
     * @param origin raw or normalized origin string
     * @return true if origin matches an allowed origin, false otherwise
     */
    public static boolean isOriginAllowed(String origin) {
        if (origin == null || origin.isBlank()) {
            return false;
        }
        String normalized = normalizeOrigin(origin);
        if (normalized == null) {
            return false;
        }
        return getAllowedOrigins().contains(normalized);
    }

    /**
     * Sets an in-memory override for allowed origins for testing purposes.
     *
     * @param origins comma-separated allowed origins or null/empty to test fail-closed behavior
     */
    public static void setAllowedOriginsOverrideForTesting(String origins) {
        overrideAllowedOrigins = origins;
        overrideActive = true;
    }

    /**
     * Clears the in-memory override for allowed origins.
     */
    public static void resetAllowedOriginsForTesting() {
        overrideAllowedOrigins = null;
        overrideActive = false;
    }
}
