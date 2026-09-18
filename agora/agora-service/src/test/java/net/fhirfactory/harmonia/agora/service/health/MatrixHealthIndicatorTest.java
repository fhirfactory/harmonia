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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@Timeout(value = 10, unit = TimeUnit.SECONDS)
class MatrixHealthIndicatorTest {

    private MockRestServiceServer mockServer;
    private MatrixHealthIndicator healthIndicator;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder().baseUrl("http://synapse:8008");
        mockServer = MockRestServiceServer.bindTo(builder).build();
        healthIndicator = new MatrixHealthIndicator("http://synapse:8008", builder.build());
    }

    @Test
    @DisplayName("Health is UP when Matrix Synapse responds 200 to versions probe")
    void testHealthUp() {
        mockServer.expect(requestTo("http://synapse:8008/_matrix/client/versions"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("{\"versions\":[\"v1.11\"]}", MediaType.APPLICATION_JSON));

        Health health = healthIndicator.health();

        assertThat(health.getStatus()).isEqualTo(Status.UP);
        assertThat(health.getDetails()).containsEntry("homeserver", "http://synapse:8008");
        assertThat(health.getDetails()).containsEntry("status", "AVAILABLE");
        mockServer.verify();
    }

    @Test
    @DisplayName("Health is DOWN when Matrix Synapse probe fails")
    void testHealthDown() {
        mockServer.expect(requestTo("http://synapse:8008/_matrix/client/versions"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withServerError());

        Health health = healthIndicator.health();

        assertThat(health.getStatus()).isEqualTo(Status.DOWN);
        assertThat(health.getDetails()).containsEntry("homeserver", "http://synapse:8008");
        assertThat(health.getDetails()).containsEntry("status", "UNAVAILABLE");
        mockServer.verify();
    }
}
