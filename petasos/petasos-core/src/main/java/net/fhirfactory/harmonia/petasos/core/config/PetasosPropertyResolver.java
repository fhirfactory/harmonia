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

package net.fhirfactory.harmonia.petasos.core.config;

import net.fhirfactory.harmonia.petasos.api.config.PetasosConfig;

import java.util.Arrays;
import java.util.Properties;

/**
 * Utility for resolving {@link PetasosConfig} from Java properties, system properties, and environment variables.
 */
public final class PetasosPropertyResolver {

    public static final String PROP_BROKER_URL = "petasos.broker.url";
    public static final String PROP_BROKER_URLS = "petasos.broker.urls";
    public static final String PROP_BROKER_USER = "petasos.broker.user";
    public static final String PROP_BROKER_PASSWORD = "petasos.broker.password";
    public static final String PROP_HA_ENABLED = "petasos.ha.enabled";
    public static final String PROP_RECONNECT_ATTEMPTS = "petasos.reconnect.attempts";
    public static final String PROP_RETRY_INTERVAL = "petasos.retry.interval";
    public static final String PROP_MAX_RETRY_INTERVAL = "petasos.retry.max-interval";
    public static final String PROP_CONNECTION_TTL = "petasos.connection.ttl";
    public static final String PROP_CALL_TIMEOUT = "petasos.call.timeout";
    public static final String PROP_DUPLICATE_DETECTION = "petasos.duplicate-detection.enabled";
    public static final String PROP_DLQ_ADDRESS = "petasos.address.dlq";
    public static final String PROP_EXPIRY_ADDRESS = "petasos.address.expiry";
    public static final String PROP_SSL_ENABLED = "petasos.ssl.enabled";

    public static final String ENV_BROKER_URL = "PETASOS_BROKER_URL";
    public static final String ENV_BROKER_URLS = "PETASOS_BROKER_URLS";
    public static final String ENV_ARTEMIS_URL = "ARTEMIS_BROKER_URL";
    public static final String ENV_BROKER_USER = "PETASOS_BROKER_USER";
    public static final String ENV_ARTEMIS_USER = "ARTEMIS_USER";
    public static final String ENV_BROKER_PASSWORD = "PETASOS_BROKER_PASSWORD";
    public static final String ENV_ARTEMIS_PASSWORD = "ARTEMIS_PASSWORD";
    public static final String ENV_HA_ENABLED = "PETASOS_HA_ENABLED";

    private PetasosPropertyResolver() {
    }

    public static PetasosConfig fromEnvironment() {
        return PetasosConfig.fromEnvironment();
    }

    public static PetasosConfig resolve() {
        return fromProperties(System.getProperties());
    }

    public static PetasosConfig fromProperties(Properties props) {
        PetasosConfig.Builder builder = PetasosConfig.builder();

        String urls = getPropertyOrEnv(props, PROP_BROKER_URL, null);
        if (urls == null || urls.isBlank()) {
            urls = getPropertyOrEnv(props, PROP_BROKER_URLS, null);
        }
        if (urls == null || urls.isBlank()) {
            urls = System.getenv(ENV_BROKER_URL);
        }
        if (urls == null || urls.isBlank()) {
            urls = System.getenv(ENV_BROKER_URLS);
        }
        if (urls == null || urls.isBlank()) {
            urls = System.getenv(ENV_ARTEMIS_URL);
        }
        if (urls != null && !urls.isBlank()) {
            builder.brokerUrls(Arrays.asList(urls.split(",")));
        }

        String user = getPropertyOrEnv(props, PROP_BROKER_USER, ENV_BROKER_USER);
        if (user == null || user.isBlank()) {
            user = System.getenv(ENV_ARTEMIS_USER);
        }
        if (user != null && !user.isBlank()) {
            builder.username(user.trim());
        }

        String pass = getPropertyOrEnv(props, PROP_BROKER_PASSWORD, ENV_BROKER_PASSWORD);
        if (pass == null || pass.isBlank()) {
            pass = System.getenv(ENV_ARTEMIS_PASSWORD);
        }
        if (pass != null && !pass.isBlank()) {
            builder.password(pass.trim());
        }

        String ha = getPropertyOrEnv(props, PROP_HA_ENABLED, ENV_HA_ENABLED);
        if (ha != null && !ha.isBlank()) {
            builder.haEnabled(Boolean.parseBoolean(ha.trim()));
        }

        String reconnect = getPropertyOrEnv(props, PROP_RECONNECT_ATTEMPTS, null);
        if (reconnect != null && !reconnect.isBlank()) {
            builder.reconnectAttempts(Integer.parseInt(reconnect.trim()));
        }

        String retry = getPropertyOrEnv(props, PROP_RETRY_INTERVAL, null);
        if (retry != null && !retry.isBlank()) {
            builder.retryInterval(Long.parseLong(retry.trim()));
        }

        String maxRetry = getPropertyOrEnv(props, PROP_MAX_RETRY_INTERVAL, null);
        if (maxRetry != null && !maxRetry.isBlank()) {
            builder.maxRetryInterval(Long.parseLong(maxRetry.trim()));
        }

        String ttl = getPropertyOrEnv(props, PROP_CONNECTION_TTL, null);
        if (ttl != null && !ttl.isBlank()) {
            builder.connectionTtl(Long.parseLong(ttl.trim()));
        }

        String timeout = getPropertyOrEnv(props, PROP_CALL_TIMEOUT, null);
        if (timeout != null && !timeout.isBlank()) {
            builder.callTimeout(Long.parseLong(timeout.trim()));
        }

        String dedup = getPropertyOrEnv(props, PROP_DUPLICATE_DETECTION, null);
        if (dedup != null && !dedup.isBlank()) {
            builder.duplicateDetectionEnabled(Boolean.parseBoolean(dedup.trim()));
        }

        String dlq = getPropertyOrEnv(props, PROP_DLQ_ADDRESS, null);
        if (dlq != null && !dlq.isBlank()) {
            builder.deadLetterAddress(dlq.trim());
        }

        String expiry = getPropertyOrEnv(props, PROP_EXPIRY_ADDRESS, null);
        if (expiry != null && !expiry.isBlank()) {
            builder.expiryAddress(expiry.trim());
        }

        String ssl = getPropertyOrEnv(props, PROP_SSL_ENABLED, null);
        if (ssl != null && !ssl.isBlank()) {
            builder.sslEnabled(Boolean.parseBoolean(ssl.trim()));
        }

        return builder.build();
    }

    private static String getPropertyOrEnv(Properties props, String propKey, String envKey) {
        if (props != null) {
            String val = props.getProperty(propKey);
            if (val != null && !val.isBlank()) {
                return val.trim();
            }
        }
        String sysVal = System.getProperty(propKey);
        if (sysVal != null && !sysVal.isBlank()) {
            return sysVal.trim();
        }
        if (envKey != null) {
            String envVal = System.getenv(envKey);
            if (envVal != null && !envVal.isBlank()) {
                return envVal.trim();
            }
        }
        return null;
    }
}
