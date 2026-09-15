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

    public static PetasosConfig fromProperties(Properties props) {
        if (props == null || props.isEmpty()) {
            return PetasosConfig.defaultLocal();
        }

        PetasosConfig.Builder builder = PetasosConfig.builder();

        String urls = props.getProperty(PROP_BROKER_URLS);
        if (urls != null && !urls.isBlank()) {
            builder.brokerUrls(Arrays.asList(urls.split(",")));
        }

        String user = props.getProperty(PROP_BROKER_USER);
        if (user != null && !user.isBlank()) {
            builder.username(user);
        }

        String pass = props.getProperty(PROP_BROKER_PASSWORD);
        if (pass != null && !pass.isBlank()) {
            builder.password(pass);
        }

        String ha = props.getProperty(PROP_HA_ENABLED);
        if (ha != null && !ha.isBlank()) {
            builder.haEnabled(Boolean.parseBoolean(ha.trim()));
        }

        String reconnect = props.getProperty(PROP_RECONNECT_ATTEMPTS);
        if (reconnect != null && !reconnect.isBlank()) {
            builder.reconnectAttempts(Integer.parseInt(reconnect.trim()));
        }

        String retry = props.getProperty(PROP_RETRY_INTERVAL);
        if (retry != null && !retry.isBlank()) {
            builder.retryInterval(Long.parseLong(retry.trim()));
        }

        String maxRetry = props.getProperty(PROP_MAX_RETRY_INTERVAL);
        if (maxRetry != null && !maxRetry.isBlank()) {
            builder.maxRetryInterval(Long.parseLong(maxRetry.trim()));
        }

        String ttl = props.getProperty(PROP_CONNECTION_TTL);
        if (ttl != null && !ttl.isBlank()) {
            builder.connectionTtl(Long.parseLong(ttl.trim()));
        }

        String timeout = props.getProperty(PROP_CALL_TIMEOUT);
        if (timeout != null && !timeout.isBlank()) {
            builder.callTimeout(Long.parseLong(timeout.trim()));
        }

        String dedup = props.getProperty(PROP_DUPLICATE_DETECTION);
        if (dedup != null && !dedup.isBlank()) {
            builder.duplicateDetectionEnabled(Boolean.parseBoolean(dedup.trim()));
        }

        String dlq = props.getProperty(PROP_DLQ_ADDRESS);
        if (dlq != null && !dlq.isBlank()) {
            builder.deadLetterAddress(dlq.trim());
        }

        String expiry = props.getProperty(PROP_EXPIRY_ADDRESS);
        if (expiry != null && !expiry.isBlank()) {
            builder.expiryAddress(expiry.trim());
        }

        String ssl = props.getProperty(PROP_SSL_ENABLED);
        if (ssl != null && !ssl.isBlank()) {
            builder.sslEnabled(Boolean.parseBoolean(ssl.trim()));
        }

        return builder.build();
    }
}
