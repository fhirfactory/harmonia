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

package net.fhirfactory.harmonia.agora.matrix.client;

import net.fhirfactory.harmonia.agora.api.model.AgoraMembershipAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@Timeout(value = 10, unit = TimeUnit.SECONDS)
class MatrixClientAdapterTest {

    private MockRestServiceServer mockServer;
    private MatrixClientAdapter clientAdapter;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder()
                .baseUrl("http://synapse:8008")
                .defaultHeader("Authorization", "Bearer test_as_token");
        this.mockServer = MockRestServiceServer.bindTo(builder).build();
        this.clientAdapter = new MatrixClientAdapter(builder);
    }

    @Test
    @DisplayName("createSpace creates private Matrix space room")
    void testCreateSpace() {
        mockServer.expect(requestTo("http://synapse:8008/_matrix/client/v3/createRoom"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.creation_content.type").value("m.space"))
                .andExpect(jsonPath("$.visibility").value("private"))
                .andExpect(jsonPath("$.preset").value("private_chat"))
                .andExpect(jsonPath("$.name").value("Patient Space"))
                .andRespond(withSuccess("{\"room_id\":\"!space123:harmonia.local\"}", MediaType.APPLICATION_JSON));

        MatrixRoomDto space = clientAdapter.createSpace("Patient Space", "Patient care space", true);

        assertThat(space.getRoomId()).isEqualTo("!space123:harmonia.local");
        assertThat(space.isSpace()).isTrue();
        assertThat(space.getVisibility()).isEqualTo("private");
        mockServer.verify();
    }

    @Test
    @DisplayName("createRoom creates regular child room")
    void testCreateRoom() {
        mockServer.expect(requestTo("http://synapse:8008/_matrix/client/v3/createRoom"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Tasks Room"))
                .andExpect(jsonPath("$.visibility").value("private"))
                .andRespond(withSuccess("{\"room_id\":\"!room456:harmonia.local\"}", MediaType.APPLICATION_JSON));

        MatrixRoomDto room = clientAdapter.createRoom("Tasks Room", "Clinical tasks", true);

        assertThat(room.getRoomId()).isEqualTo("!room456:harmonia.local");
        assertThat(room.isSpace()).isFalse();
        mockServer.verify();
    }

    @Test
    @DisplayName("linkChildRoom sets m.space.child state event")
    void testLinkChildRoom() {
        mockServer.expect(requestTo("http://synapse:8008/_matrix/client/v3/rooms/%21space123%3Aharmonia.local/state/m.space.child/%21room456%3Aharmonia.local"))
                .andExpect(method(HttpMethod.PUT))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.via[0]").value("harmonia.local"))
                .andRespond(withSuccess("{\"event_id\":\"$event001\"}", MediaType.APPLICATION_JSON));

        String eventId = clientAdapter.linkChildRoom("!space123:harmonia.local", "!room456:harmonia.local", List.of("harmonia.local"));

        assertThat(eventId).isEqualTo("$event001");
        mockServer.verify();
    }

    @Test
    @DisplayName("sendMessage and sendNotice send message event to room")
    void testSendMessageAndNotice() {
        mockServer.expect(requestTo("http://synapse:8008/_matrix/client/v3/rooms/%21room456/send/m.room.message/txn-1"))
                .andExpect(method(HttpMethod.PUT))
                .andExpect(jsonPath("$.msgtype").value("m.text"))
                .andExpect(jsonPath("$.body").value("Hello Matrix"))
                .andRespond(withSuccess("{\"event_id\":\"$msg001\"}", MediaType.APPLICATION_JSON));

        mockServer.expect(requestTo("http://synapse:8008/_matrix/client/v3/rooms/%21room456/send/m.room.message/txn-2"))
                .andExpect(method(HttpMethod.PUT))
                .andExpect(jsonPath("$.msgtype").value("m.notice"))
                .andExpect(jsonPath("$.body").value("Notice event"))
                .andRespond(withSuccess("{\"event_id\":\"$msg002\"}", MediaType.APPLICATION_JSON));

        String msgEventId = clientAdapter.sendMessage("!room456", "m.text", "Hello Matrix", "txn-1");
        String noticeEventId = clientAdapter.sendNotice("!room456", "Notice event", "txn-2");

        assertThat(msgEventId).isEqualTo("$msg001");
        assertThat(noticeEventId).isEqualTo("$msg002");
        mockServer.verify();
    }

    @Test
    @DisplayName("sendCustomEvent dispatches arbitrary state or room event")
    void testSendCustomEvent() {
        mockServer.expect(requestTo("http://synapse:8008/_matrix/client/v3/rooms/%21room456/send/org.example.task/txn-3"))
                .andExpect(method(HttpMethod.PUT))
                .andExpect(jsonPath("$.task_id").value("TASK-100"))
                .andRespond(withSuccess("{\"event_id\":\"$custom001\"}", MediaType.APPLICATION_JSON));

        String eventId = clientAdapter.sendCustomEvent("!room456", "org.example.task", "txn-3", Map.of("task_id", "TASK-100"));

        assertThat(eventId).isEqualTo("$custom001");
        mockServer.verify();
    }

    @Test
    @DisplayName("manageMembership dispatches invite, join, kick, ban correctly")
    void testManageMembership() {
        mockServer.expect(requestTo("http://synapse:8008/_matrix/client/v3/rooms/%21room456/invite"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(jsonPath("$.user_id").value("@user:harmonia.local"))
                .andRespond(withSuccess());

        mockServer.expect(requestTo("http://synapse:8008/_matrix/client/v3/rooms/%21room456/join"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess());

        mockServer.expect(requestTo("http://synapse:8008/_matrix/client/v3/rooms/%21room456/kick"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(jsonPath("$.user_id").value("@user:harmonia.local"))
                .andRespond(withSuccess());

        mockServer.expect(requestTo("http://synapse:8008/_matrix/client/v3/rooms/%21room456/ban"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(jsonPath("$.user_id").value("@user:harmonia.local"))
                .andRespond(withSuccess());

        clientAdapter.manageMembership("!room456", "@user:harmonia.local", AgoraMembershipAction.INVITE, "Care team addition");
        clientAdapter.manageMembership("!room456", "@user:harmonia.local", AgoraMembershipAction.JOIN, null);
        clientAdapter.manageMembership("!room456", "@user:harmonia.local", AgoraMembershipAction.KICK, "Care team removal");
        clientAdapter.manageMembership("!room456", "@user:harmonia.local", AgoraMembershipAction.BAN, "Policy revocation");

        mockServer.verify();
    }

    @Test
    @DisplayName("getRoomMembers parses member list from chunk")
    void testGetRoomMembers() {
        String chunkJson = """
                {
                  "chunk": [
                    {
                      "type": "m.room.member",
                      "state_key": "@dr_smith:harmonia.local",
                      "content": {
                        "membership": "join",
                        "displayname": "Dr. Smith",
                        "avatar_url": "mxc://harmonia.local/abc"
                      }
                    },
                    {
                      "type": "m.room.member",
                      "state_key": "@nurse_jones:harmonia.local",
                      "content": {
                        "membership": "invite",
                        "displayname": "Nurse Jones"
                      }
                    }
                  ]
                }
                """;

        mockServer.expect(requestTo("http://synapse:8008/_matrix/client/v3/rooms/%21room456/members"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(chunkJson, MediaType.APPLICATION_JSON));

        List<MatrixMemberDto> members = clientAdapter.getRoomMembers("!room456");

        assertThat(members).hasSize(2);
        assertThat(members.get(0).getUserId()).isEqualTo("@dr_smith:harmonia.local");
        assertThat(members.get(0).getMembership()).isEqualTo("join");
        assertThat(members.get(0).getDisplayName()).isEqualTo("Dr. Smith");
        assertThat(members.get(1).getUserId()).isEqualTo("@nurse_jones:harmonia.local");
        assertThat(members.get(1).getMembership()).isEqualTo("invite");
        mockServer.verify();
    }

    @Test
    @DisplayName("getPowerLevels and setPowerLevels manage room power levels")
    void testPowerLevels() {
        String powerLevelsJson = """
                {
                  "users": {
                    "@admin:harmonia.local": 100
                  },
                  "users_default": 0,
                  "events": {},
                  "events_default": 0,
                  "state_default": 50,
                  "ban": 50,
                  "kick": 50,
                  "redact": 50,
                  "invite": 50
                }
                """;

        mockServer.expect(requestTo("http://synapse:8008/_matrix/client/v3/rooms/%21room456/state/m.room.power_levels"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(powerLevelsJson, MediaType.APPLICATION_JSON));

        mockServer.expect(requestTo("http://synapse:8008/_matrix/client/v3/rooms/%21room456/state/m.room.power_levels"))
                .andExpect(method(HttpMethod.PUT))
                .andExpect(jsonPath("$.users['@admin:harmonia.local']").value(100))
                .andRespond(withSuccess("{\"event_id\":\"$pl001\"}", MediaType.APPLICATION_JSON));

        MatrixPowerLevelsDto levels = clientAdapter.getPowerLevels("!room456");
        assertThat(levels.getUsers()).containsEntry("@admin:harmonia.local", 100);

        String eventId = clientAdapter.setPowerLevels("!room456", levels);
        assertThat(eventId).isEqualTo("$pl001");
        mockServer.verify();
    }

    @Test
    @DisplayName("Matrix client translates HTTP error to MatrixRestException with errcode")
    void testErrorTranslation() {
        String errorJson = """
                {
                  "errcode": "M_FORBIDDEN",
                  "error": "You do not have permission to invite users to this room"
                }
                """;

        mockServer.expect(requestTo("http://synapse:8008/_matrix/client/v3/rooms/%21room456/invite"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(org.springframework.http.HttpStatus.FORBIDDEN).body(errorJson).contentType(MediaType.APPLICATION_JSON));

        assertThatThrownBy(() -> clientAdapter.inviteUser("!room456", "@user:harmonia.local", "Invite"))
                .isInstanceOf(MatrixRestException.class)
                .satisfies(ex -> {
                    MatrixRestException rex = (MatrixRestException) ex;
                    assertThat(rex.getHttpStatus()).isEqualTo(403);
                    assertThat(rex.getErrcode()).isEqualTo("M_FORBIDDEN");
                    assertThat(rex.getError()).contains("permission");
                });

        mockServer.verify();
    }

    @Test
    @DisplayName("Matrix client suppresses raw response body and PHI/secret markers in MatrixRestException")
    void testErrorSanitizationSuppressesPhiAndSecret() {
        String errorJson = """
                {
                  "errcode": "M_UNKNOWN",
                  "error": "Generic error",
                  "echoed_payload": "PATIENT-PHI-MARKER-92831",
                  "auth_header": "Bearer TOKEN-SECRET-MARKER-81742"
                }
                """;

        mockServer.expect(requestTo("http://synapse:8008/_matrix/client/v3/rooms/%21room456/invite"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(org.springframework.http.HttpStatus.BAD_REQUEST).body(errorJson).contentType(MediaType.APPLICATION_JSON));

        assertThatThrownBy(() -> clientAdapter.inviteUser("!room456", "@user:harmonia.local", "Invite"))
                .isInstanceOf(MatrixRestException.class)
                .satisfies(ex -> {
                    MatrixRestException rex = (MatrixRestException) ex;
                    assertThat(rex.getMessage())
                            .doesNotContain("PATIENT-PHI-MARKER-92831")
                            .doesNotContain("TOKEN-SECRET-MARKER-81742")
                            .doesNotContain("echoed_payload");
                    assertThat(rex.getHttpStatus()).isEqualTo(400);
                    assertThat(rex.getErrcode()).isEqualTo("M_UNKNOWN");
                });

        mockServer.verify();
    }

    @Test
    @DisplayName("Matrix client non-JSON error body suppresses raw body and PHI/secret markers")
    void testNonJsonErrorSuppressesRawBodyAndPhi() {
        String htmlError = "<html><body>Error 502 Bad Gateway: PATIENT-PHI-MARKER-92831 TOKEN-SECRET-MARKER-81742</body></html>";

        mockServer.expect(requestTo("http://synapse:8008/_matrix/client/v3/rooms/%21room456/invite"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(org.springframework.http.HttpStatus.BAD_GATEWAY).body(htmlError).contentType(MediaType.TEXT_HTML));

        assertThatThrownBy(() -> clientAdapter.inviteUser("!room456", "@user:harmonia.local", "Invite"))
                .isInstanceOf(MatrixRestException.class)
                .satisfies(ex -> {
                    MatrixRestException rex = (MatrixRestException) ex;
                    assertThat(rex.getMessage())
                            .doesNotContain("PATIENT-PHI-MARKER-92831")
                            .doesNotContain("TOKEN-SECRET-MARKER-81742")
                            .doesNotContain("<html>");
                    assertThat(rex.getHttpStatus()).isEqualTo(502);
                    assertThat(rex.getErrcode()).isNull();
                    assertThat(rex.getError()).isNull();
                });

        mockServer.verify();
    }
}
