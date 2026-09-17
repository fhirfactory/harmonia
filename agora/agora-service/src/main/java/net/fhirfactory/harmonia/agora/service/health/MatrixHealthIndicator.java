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

package net.fhirfactory.harmonia.agora.service.health;

import net.fhirfactory.harmonia.agora.service.config.AgoraProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Duration;

/**
 * Actuator HealthIndicator probing Matrix Synapse homeserver availability
 * via GET /_matrix/client/versions.
 */
@Component
public class MatrixHealthIndicator implements HealthIndicator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MatrixHealthIndicator.class);
    private static final Duration PROBE_TIMEOUT = Duration.ofSeconds(3);

    private final String baseUrl;
    private final RestClient probeClient;

    @org.springframework.beans.factory.annotation.Autowired
    public MatrixHealthIndicator(AgoraProperties agoraProperties) {
        this.baseUrl = (agoraProperties != null && agoraProperties.getSynapse() != null)
                ? agoraProperties.getSynapse().getBaseUrl()
                : "http://synapse:8008";

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(PROBE_TIMEOUT);
        requestFactory.setReadTimeout(PROBE_TIMEOUT);

        this.probeClient = RestClient.builder()
                .baseUrl(this.baseUrl)
                .requestFactory(requestFactory)
                .build();
    }

    public MatrixHealthIndicator(String baseUrl, RestClient probeClient) {
        this.baseUrl = baseUrl;
        this.probeClient = probeClient;
    }

    @Override
    public Health health() {
        try {
            probeClient.get()
                    .uri("/_matrix/client/versions")
                    .retrieve()
                    .toBodilessEntity();

            return Health.up()
                    .withDetail("homeserver", baseUrl)
                    .withDetail("status", "AVAILABLE")
                    .build();
        } catch (Exception ex) {
            LOGGER.warn("Matrix homeserver health probe failed: {}", ex.getMessage());
            return Health.down()
                    .withDetail("homeserver", baseUrl)
                    .withDetail("status", "UNAVAILABLE")
                    .withDetail("error", ex.getMessage())
                    .build();
        }
    }
}
