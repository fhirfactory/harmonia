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

package net.fhirfactory.harmonia.agora.matrix.admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@Timeout(value = 10, unit = TimeUnit.SECONDS)
class SynapseAdministrationGatewayTest {

    private MockRestServiceServer mockServer;
    private SynapseAdministrationGateway adminGateway;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder()
                .baseUrl("http://synapse:8008")
                .defaultHeader("Authorization", "Bearer test_admin_token");
        this.mockServer = MockRestServiceServer.bindTo(builder).build();
        this.adminGateway = new SynapseAdministrationGateway(builder);
    }

    @Test
    @DisplayName("createOrUpdateUser creates local user via Synapse Admin API")
    void testCreateOrUpdateUser() {
        String responseJson = """
                {
                  "name": "@_harmonia_p_123:harmonia.local",
                  "displayname": "Dr. Sarah Connor",
                  "admin": false,
                  "deactivated": false
                }
                """;

        mockServer.expect(requestTo("http://synapse:8008/_synapse/admin/v2/users/%40_harmonia_p_123%3Aharmonia.local"))
                .andExpect(method(HttpMethod.PUT))
                .andExpect(jsonPath("$.displayname").value("Dr. Sarah Connor"))
                .andExpect(jsonPath("$.password").value("secret123"))
                .andExpect(jsonPath("$.admin").value(false))
                .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON));

        MatrixUserDto user = MatrixUserDto.builder()
                .userId("@_harmonia_p_123:harmonia.local")
                .displayName("Dr. Sarah Connor")
                .password("secret123")
                .admin(false)
                .deactivated(false)
                .build();

        MatrixUserDto result = adminGateway.createOrUpdateUser(user);

        assertThat(result.getUserId()).isEqualTo("@_harmonia_p_123:harmonia.local");
        assertThat(result.getDisplayName()).isEqualTo("Dr. Sarah Connor");
        mockServer.verify();
    }

    @Test
    @DisplayName("getUser retrieves user details")
    void testGetUser() {
        String responseJson = """
                {
                  "name": "@_harmonia_p_123:harmonia.local",
                  "displayname": "Dr. Sarah Connor",
                  "admin": false,
                  "deactivated": false
                }
                """;

        mockServer.expect(requestTo("http://synapse:8008/_synapse/admin/v2/users/%40_harmonia_p_123%3Aharmonia.local"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON));

        MatrixUserDto user = adminGateway.getUser("@_harmonia_p_123:harmonia.local");

        assertThat(user.getUserId()).isEqualTo("@_harmonia_p_123:harmonia.local");
        assertThat(user.getDisplayName()).isEqualTo("Dr. Sarah Connor");
        mockServer.verify();
    }

    @Test
    @DisplayName("deactivateUser calls admin deactivate endpoint")
    void testDeactivateUser() {
        mockServer.expect(requestTo("http://synapse:8008/_synapse/admin/v1/deactivate/%40_harmonia_p_123%3Aharmonia.local"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(jsonPath("$.erase").value(true))
                .andRespond(withSuccess("{\"id_server_unbind_result\":\"success\"}", MediaType.APPLICATION_JSON));

        adminGateway.deactivateUser("@_harmonia_p_123:harmonia.local", true);
        mockServer.verify();
    }

    @Test
    @DisplayName("purgeRoom calls admin room deletion endpoint")
    void testPurgeRoom() {
        mockServer.expect(requestTo("http://synapse:8008/_synapse/admin/v1/rooms/%21room_to_purge%3Aharmonia.local"))
                .andExpect(method(HttpMethod.DELETE))
                .andExpect(jsonPath("$.purge").value(true))
                .andRespond(withSuccess("{\"delete_id\":\"job_999\"}", MediaType.APPLICATION_JSON));

        String deleteId = adminGateway.purgeRoom("!room_to_purge:harmonia.local");

        assertThat(deleteId).isEqualTo("job_999");
        mockServer.verify();
    }

    @Test
    @DisplayName("Admin error translates to SynapseAdminException")
    void testAdminErrorTranslation() {
        String errorJson = """
                {
                  "errcode": "M_UNKNOWN",
                  "error": "User not found"
                }
                """;

        mockServer.expect(requestTo("http://synapse:8008/_synapse/admin/v2/users/%40unknown%3Aharmonia.local"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withResourceNotFound().body(errorJson).contentType(MediaType.APPLICATION_JSON));

        assertThatThrownBy(() -> adminGateway.getUser("@unknown:harmonia.local"))
                .isInstanceOf(SynapseAdminException.class)
                .satisfies(ex -> {
                    SynapseAdminException aex = (SynapseAdminException) ex;
                    assertThat(aex.getHttpStatus()).isEqualTo(404);
                    assertThat(aex.getErrcode()).isEqualTo("M_UNKNOWN");
                    assertThat(aex.getError()).contains("User not found");
                });

        mockServer.verify();
    }
}
