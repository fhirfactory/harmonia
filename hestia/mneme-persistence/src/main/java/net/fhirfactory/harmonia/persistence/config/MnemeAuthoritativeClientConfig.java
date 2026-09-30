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

package net.fhirfactory.harmonia.persistence.config;

import java.time.Duration;
import java.util.Objects;

/**
 * Immutable configuration for the Mneme Authoritative HTTP Client.
 * <p>
 * Supports configuration via environment variables or system properties without hardcoding
 * localhost or developer workstation IP addresses.
 */
public record MnemeAuthoritativeClientConfig(
        String baseUrl,
        Duration connectTimeout,
        Duration requestTimeout
) {
    public static final String DEFAULT_BASE_URL = "http://mnemosyne-clinical:8080/api/authoritative/fhir";
    public static final Duration DEFAULT_CONNECT_TIMEOUT = Duration.ofSeconds(5);
    public static final Duration DEFAULT_REQUEST_TIMEOUT = Duration.ofSeconds(15);

    public static final String ENV_BASE_URL = "HARMONIA_MNEMOSYNE_AUTHORITATIVE_URL";
    public static final String PROP_BASE_URL = "mnemosyne.authoritative.url";

    public static final String ENV_CONNECT_TIMEOUT_SEC = "HARMONIA_MNEMOSYNE_CONNECT_TIMEOUT_SEC";
    public static final String PROP_CONNECT_TIMEOUT_SEC = "mnemosyne.connect.timeout.seconds";

    public static final String ENV_REQUEST_TIMEOUT_SEC = "HARMONIA_MNEMOSYNE_REQUEST_TIMEOUT_SEC";
    public static final String PROP_REQUEST_TIMEOUT_SEC = "mnemosyne.request.timeout.seconds";

    public MnemeAuthoritativeClientConfig {
        Objects.requireNonNull(baseUrl, "baseUrl must not be null");
        Objects.requireNonNull(connectTimeout, "connectTimeout must not be null");
        Objects.requireNonNull(requestTimeout, "requestTimeout must not be null");
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
    }

    public static MnemeAuthoritativeClientConfig of(String baseUrl) {
        return new MnemeAuthoritativeClientConfig(baseUrl, DEFAULT_CONNECT_TIMEOUT, DEFAULT_REQUEST_TIMEOUT);
    }

    public static MnemeAuthoritativeClientConfig of(String baseUrl, Duration connectTimeout, Duration requestTimeout) {
        return new MnemeAuthoritativeClientConfig(baseUrl, connectTimeout, requestTimeout);
    }

    public static MnemeAuthoritativeClientConfig fromEnvironment() {
        String url = getEnvOrProp(ENV_BASE_URL, PROP_BASE_URL, DEFAULT_BASE_URL);

        String connectSec = getEnvOrProp(ENV_CONNECT_TIMEOUT_SEC, PROP_CONNECT_TIMEOUT_SEC, null);
        Duration connectTimeout = connectSec != null ? Duration.ofSeconds(Long.parseLong(connectSec)) : DEFAULT_CONNECT_TIMEOUT;

        String requestSec = getEnvOrProp(ENV_REQUEST_TIMEOUT_SEC, PROP_REQUEST_TIMEOUT_SEC, null);
        Duration requestTimeout = requestSec != null ? Duration.ofSeconds(Long.parseLong(requestSec)) : DEFAULT_REQUEST_TIMEOUT;

        return new MnemeAuthoritativeClientConfig(url, connectTimeout, requestTimeout);
    }

    private static String getEnvOrProp(String envKey, String propKey, String defaultValue) {
        String val = System.getenv(envKey);
        if (val != null && !val.isBlank()) {
            return val.trim();
        }
        val = System.getProperty(propKey);
        if (val != null && !val.isBlank()) {
            return val.trim();
        }
        return defaultValue;
    }
}
