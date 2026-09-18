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

package net.fhirfactory.harmonia.agora.matrix.appservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@Timeout(value = 10, unit = TimeUnit.SECONDS)
class AppServiceTransactionDtoTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("AppServiceTransactionDto deserializes transaction payload from Matrix JSON")
    void testDeserialization() throws Exception {
        String json = """
                {
                  "events": [
                    {
                      "event_id": "$event_001",
                      "type": "m.room.message",
                      "room_id": "!room_123:harmonia.local",
                      "sender": "@_harmonia_p_999:harmonia.local",
                      "origin_server_ts": 1726580000000,
                      "content": {
                        "msgtype": "m.text",
                        "body": "Patient vital signs updated"
                      },
                      "unsigned": {
                        "age": 120
                      }
                    }
                  ]
                }
                """;

        AppServiceTransactionDto txn = mapper.readValue(json, AppServiceTransactionDto.class);

        assertThat(txn.getEvents()).hasSize(1);
        AppServiceEventDto event = txn.getEvents().get(0);
        assertThat(event.getEventId()).isEqualTo("$event_001");
        assertThat(event.getType()).isEqualTo("m.room.message");
        assertThat(event.getRoomId()).isEqualTo("!room_123:harmonia.local");
        assertThat(event.getSender()).isEqualTo("@_harmonia_p_999:harmonia.local");
        assertThat(event.getOriginServerTs()).isEqualTo(1726580000000L);
        assertThat(event.getContent()).containsEntry("msgtype", "m.text");
        assertThat(event.getContent()).containsEntry("body", "Patient vital signs updated");
    }

    @Test
    @DisplayName("AppServiceTransactionDto round-trip serializes correctly")
    void testSerializationRoundTrip() throws Exception {
        AppServiceEventDto event = AppServiceEventDto.builder()
                .eventId("$e1")
                .type("m.room.member")
                .roomId("!r1")
                .sender("@s1")
                .originServerTs(123456789L)
                .content(Map.of("membership", "join"))
                .stateKey("@s1")
                .build();

        AppServiceTransactionDto txn = AppServiceTransactionDto.builder()
                .event(event)
                .build();

        String json = mapper.writeValueAsString(txn);
        AppServiceTransactionDto roundTripped = mapper.readValue(json, AppServiceTransactionDto.class);

        assertThat(roundTripped.getEvents()).hasSize(1);
        assertThat(roundTripped.getEvents().get(0).getEventId()).isEqualTo("$e1");
        assertThat(roundTripped.getEvents().get(0).getStateKey()).isEqualTo("@s1");
    }
}
