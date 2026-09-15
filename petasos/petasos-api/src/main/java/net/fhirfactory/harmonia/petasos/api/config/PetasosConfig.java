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

package net.fhirfactory.harmonia.petasos.api.config;

import java.io.Serializable;
import java.util.*;

/**
 * Main configuration for connecting to the Petasos / Artemis messaging subsystem.
 */
public final class PetasosConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String DEFAULT_BROKER_URL = "tcp://127.0.0.1:61616";
    public static final String DEFAULT_USERNAME = "artemis";
    public static final String DEFAULT_PASSWORD = "artemisPassword";
    public static final String DEFAULT_DLQ_ADDRESS = "DLQ";
    public static final String DEFAULT_EXPIRY_ADDRESS = "ExpiryQueue";

    private final List<String> brokerUrls;
    private final String username;
    private final String password;
    private final boolean haEnabled;
    private final int reconnectAttempts;
    private final long retryInterval;
    private final long maxRetryInterval;
    private final double retryIntervalMultiplier;
    private final long connectionTtl;
    private final long clientFailureCheckPeriod;
    private final long callTimeout;
    private final boolean duplicateDetectionEnabled;
    private final String deadLetterAddress;
    private final String expiryAddress;
    private final boolean sslEnabled;
    private final String trustStorePath;
    private final String trustStorePassword;
    private final String keyStorePath;
    private final String keyStorePassword;

    public PetasosConfig(
            List<String> brokerUrls,
            String username,
            String password,
            boolean haEnabled,
            int reconnectAttempts,
            long retryInterval,
            long maxRetryInterval,
            double retryIntervalMultiplier,
            long connectionTtl,
            long clientFailureCheckPeriod,
            long callTimeout,
            boolean duplicateDetectionEnabled,
            String deadLetterAddress,
            String expiryAddress,
            boolean sslEnabled,
            String trustStorePath,
            String trustStorePassword,
            String keyStorePath,
            String keyStorePassword) {
        this.brokerUrls = brokerUrls != null && !brokerUrls.isEmpty()
                ? Collections.unmodifiableList(new ArrayList<>(brokerUrls))
                : List.of(DEFAULT_BROKER_URL);
        this.username = username != null ? username : DEFAULT_USERNAME;
        this.password = password != null ? password : DEFAULT_PASSWORD;
        this.haEnabled = haEnabled;
        this.reconnectAttempts = reconnectAttempts;
        this.retryInterval = retryInterval > 0 ? retryInterval : 500L;
        this.maxRetryInterval = maxRetryInterval > 0 ? maxRetryInterval : 2000L;
        this.retryIntervalMultiplier = retryIntervalMultiplier > 0 ? retryIntervalMultiplier : 1.5;
        this.connectionTtl = connectionTtl > 0 ? connectionTtl : 60000L;
        this.clientFailureCheckPeriod = clientFailureCheckPeriod > 0 ? clientFailureCheckPeriod : 10000L;
        this.callTimeout = callTimeout > 0 ? callTimeout : 30000L;
        this.duplicateDetectionEnabled = duplicateDetectionEnabled;
        this.deadLetterAddress = deadLetterAddress != null ? deadLetterAddress : DEFAULT_DLQ_ADDRESS;
        this.expiryAddress = expiryAddress != null ? expiryAddress : DEFAULT_EXPIRY_ADDRESS;
        this.sslEnabled = sslEnabled;
        this.trustStorePath = trustStorePath;
        this.trustStorePassword = trustStorePassword;
        this.keyStorePath = keyStorePath;
        this.keyStorePassword = keyStorePassword;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static PetasosConfig defaultLocal() {
        return builder().build();
    }

    public static PetasosConfig fromEnvironment() {
        Builder builder = builder();
        String urls = System.getenv("PETASOS_BROKER_URLS");
        if (urls == null || urls.isBlank()) {
            urls = System.getenv("ARTEMIS_BROKER_URL");
        }
        if (urls != null && !urls.isBlank()) {
            builder.brokerUrls(Arrays.asList(urls.split(",")));
        }

        String user = System.getenv("PETASOS_BROKER_USER");
        if (user == null || user.isBlank()) {
            user = System.getenv("ARTEMIS_USER");
        }
        if (user != null && !user.isBlank()) {
            builder.username(user);
        }

        String pass = System.getenv("PETASOS_BROKER_PASSWORD");
        if (pass == null || pass.isBlank()) {
            pass = System.getenv("ARTEMIS_PASSWORD");
        }
        if (pass != null && !pass.isBlank()) {
            builder.password(pass);
        }

        String ha = System.getenv("PETASOS_HA_ENABLED");
        if (ha != null && !ha.isBlank()) {
            builder.haEnabled(Boolean.parseBoolean(ha.trim()));
        }

        return builder.build();
    }

    public List<String> getBrokerUrls() {
        return brokerUrls;
    }

    public String getPrimaryBrokerUrl() {
        return brokerUrls.get(0);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isHaEnabled() {
        return haEnabled;
    }

    public int getReconnectAttempts() {
        return reconnectAttempts;
    }

    public long getRetryInterval() {
        return retryInterval;
    }

    public long getMaxRetryInterval() {
        return maxRetryInterval;
    }

    public double getRetryIntervalMultiplier() {
        return retryIntervalMultiplier;
    }

    public long getConnectionTtl() {
        return connectionTtl;
    }

    public long getClientFailureCheckPeriod() {
        return clientFailureCheckPeriod;
    }

    public long getCallTimeout() {
        return callTimeout;
    }

    public boolean isDuplicateDetectionEnabled() {
        return duplicateDetectionEnabled;
    }

    public String getDeadLetterAddress() {
        return deadLetterAddress;
    }

    public String getExpiryAddress() {
        return expiryAddress;
    }

    public boolean isSslEnabled() {
        return sslEnabled;
    }

    public String getTrustStorePath() {
        return trustStorePath;
    }

    public String getTrustStorePassword() {
        return trustStorePassword;
    }

    public String getKeyStorePath() {
        return keyStorePath;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public static final class Builder {
        private final List<String> brokerUrls = new ArrayList<>();
        private String username = DEFAULT_USERNAME;
        private String password = DEFAULT_PASSWORD;
        private boolean haEnabled = true;
        private int reconnectAttempts = -1; // Infinite reconnects for HA client
        private long retryInterval = 500L;
        private long maxRetryInterval = 2000L;
        private double retryIntervalMultiplier = 1.5;
        private long connectionTtl = 60000L;
        private long clientFailureCheckPeriod = 10000L;
        private long callTimeout = 30000L;
        private boolean duplicateDetectionEnabled = true;
        private String deadLetterAddress = DEFAULT_DLQ_ADDRESS;
        private String expiryAddress = DEFAULT_EXPIRY_ADDRESS;
        private boolean sslEnabled = false;
        private String trustStorePath;
        private String trustStorePassword;
        private String keyStorePath;
        private String keyStorePassword;

        public Builder brokerUrls(List<String> urls) {
            this.brokerUrls.clear();
            if (urls != null) {
                for (String u : urls) {
                    if (u != null && !u.isBlank()) {
                        this.brokerUrls.add(u.trim());
                    }
                }
            }
            return this;
        }

        public Builder addBrokerUrl(String url) {
            if (url != null && !url.isBlank()) {
                this.brokerUrls.add(url.trim());
            }
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder haEnabled(boolean haEnabled) {
            this.haEnabled = haEnabled;
            return this;
        }

        public Builder reconnectAttempts(int reconnectAttempts) {
            this.reconnectAttempts = reconnectAttempts;
            return this;
        }

        public Builder retryInterval(long retryInterval) {
            this.retryInterval = retryInterval;
            return this;
        }

        public Builder maxRetryInterval(long maxRetryInterval) {
            this.maxRetryInterval = maxRetryInterval;
            return this;
        }

        public Builder retryIntervalMultiplier(double retryIntervalMultiplier) {
            this.retryIntervalMultiplier = retryIntervalMultiplier;
            return this;
        }

        public Builder connectionTtl(long connectionTtl) {
            this.connectionTtl = connectionTtl;
            return this;
        }

        public Builder clientFailureCheckPeriod(long clientFailureCheckPeriod) {
            this.clientFailureCheckPeriod = clientFailureCheckPeriod;
            return this;
        }

        public Builder callTimeout(long callTimeout) {
            this.callTimeout = callTimeout;
            return this;
        }

        public Builder duplicateDetectionEnabled(boolean duplicateDetectionEnabled) {
            this.duplicateDetectionEnabled = duplicateDetectionEnabled;
            return this;
        }

        public Builder deadLetterAddress(String deadLetterAddress) {
            this.deadLetterAddress = deadLetterAddress;
            return this;
        }

        public Builder expiryAddress(String expiryAddress) {
            this.expiryAddress = expiryAddress;
            return this;
        }

        public Builder sslEnabled(boolean sslEnabled) {
            this.sslEnabled = sslEnabled;
            return this;
        }

        public Builder trustStorePath(String trustStorePath) {
            this.trustStorePath = trustStorePath;
            return this;
        }

        public Builder trustStorePassword(String trustStorePassword) {
            this.trustStorePassword = trustStorePassword;
            return this;
        }

        public Builder keyStorePath(String keyStorePath) {
            this.keyStorePath = keyStorePath;
            return this;
        }

        public Builder keyStorePassword(String keyStorePassword) {
            this.keyStorePassword = keyStorePassword;
            return this;
        }

        public PetasosConfig build() {
            List<String> urls = brokerUrls.isEmpty() ? List.of(DEFAULT_BROKER_URL) : brokerUrls;
            return new PetasosConfig(
                    urls,
                    username,
                    password,
                    haEnabled,
                    reconnectAttempts,
                    retryInterval,
                    maxRetryInterval,
                    retryIntervalMultiplier,
                    connectionTtl,
                    clientFailureCheckPeriod,
                    callTimeout,
                    duplicateDetectionEnabled,
                    deadLetterAddress,
                    expiryAddress,
                    sslEnabled,
                    trustStorePath,
                    trustStorePassword,
                    keyStorePath,
                    keyStorePassword
            );
        }
    }
}
