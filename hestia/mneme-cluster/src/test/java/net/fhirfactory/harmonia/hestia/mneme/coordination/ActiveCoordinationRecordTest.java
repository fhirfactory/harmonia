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

package net.fhirfactory.harmonia.hestia.mneme.coordination;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ActiveCoordinationRecordTest {

    @Test
    @DisplayName("ActiveCoordinationRecord implements Serializable")
    void recordIsSerializable() {
        assertThat(Serializable.class.isAssignableFrom(ActiveCoordinationRecord.class)).isTrue();
    }

    @Test
    @DisplayName("Constructor rejects null ActiveHegemon")
    void constructorValidation() {
        assertThatThrownBy(() -> new ActiveCoordinationRecord(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("activeHegemon must not be null");
    }

    @Test
    @DisplayName("Encode and decode valid NO_HEGEMON state")
    void encodeAndDecodeNoHegemon() {
        ActiveCoordinationRecord record = ActiveCoordinationRecord.none();
        assertThat(record.activeHegemon().isPresent()).isFalse();
        assertThat(record.encode()).isEqualTo(ActiveCoordinationRecord.NO_HEGEMON_MARKER);

        ActiveCoordinationRecord decoded = ActiveCoordinationRecord.decode(ActiveCoordinationRecord.NO_HEGEMON_MARKER);
        assertThat(decoded.activeHegemon().isPresent()).isFalse();

        // Whitespace-tolerant
        ActiveCoordinationRecord trimmed = ActiveCoordinationRecord.decode("  NO_HEGEMON \t ");
        assertThat(trimmed.activeHegemon().isPresent()).isFalse();
    }

    @Test
    @DisplayName("Encode and decode valid HEGEMON:<uuid> state")
    void encodeAndDecodeValidHegemon() {
        UUID uuid = UUID.randomUUID();
        InstanceId id = InstanceId.of(uuid);
        ActiveCoordinationRecord record = ActiveCoordinationRecord.of(id);

        assertThat(record.activeHegemon().isPresent()).isTrue();
        assertThat(record.activeHegemon().instanceId()).isEqualTo(id);
        assertThat(record.encode()).isEqualTo("HEGEMON:" + uuid);

        ActiveCoordinationRecord decoded = ActiveCoordinationRecord.decode("HEGEMON:" + uuid);
        assertThat(decoded.activeHegemon().isPresent()).isTrue();
        assertThat(decoded.activeHegemon().instanceId().value()).isEqualTo(uuid);

        // Leading/trailing whitespace tolerant
        ActiveCoordinationRecord trimmed = ActiveCoordinationRecord.decode("  HEGEMON:" + uuid + "  ");
        assertThat(trimmed.activeHegemon().instanceId().value()).isEqualTo(uuid);
    }

    @Test
    @DisplayName("Decode blank or null value fails closed under AX-14/AX-15")
    void decodeBlankOrNullFailsClosed() {
        assertThatThrownBy(() -> ActiveCoordinationRecord.decode(null))
                .isInstanceOf(ActiveCoordinationCorruptException.class)
                .hasMessageContaining("blank or null");

        assertThatThrownBy(() -> ActiveCoordinationRecord.decode(""))
                .isInstanceOf(ActiveCoordinationCorruptException.class)
                .hasMessageContaining("blank or null");

        assertThatThrownBy(() -> ActiveCoordinationRecord.decode("   \t \n  "))
                .isInstanceOf(ActiveCoordinationCorruptException.class)
                .hasMessageContaining("blank or null");
    }

    @Test
    @DisplayName("Decode malformed InstanceId fails closed under AX-14/AX-15")
    void decodeMalformedInstanceIdFailsClosed() {
        assertThatThrownBy(() -> ActiveCoordinationRecord.decode("HEGEMON:not-a-valid-uuid"))
                .isInstanceOf(ActiveCoordinationCorruptException.class)
                .hasMessageContaining("Malformed InstanceId");

        assertThatThrownBy(() -> ActiveCoordinationRecord.decode("HEGEMON:123456"))
                .isInstanceOf(ActiveCoordinationCorruptException.class)
                .hasMessageContaining("Malformed InstanceId");

        assertThatThrownBy(() -> ActiveCoordinationRecord.decode("HEGEMON:"))
                .isInstanceOf(ActiveCoordinationCorruptException.class)
                .hasMessageContaining("Malformed InstanceId");
    }

    @Test
    @DisplayName("Decode unknown or legacy encoding fails closed under AX-14/AX-15")
    void decodeUnknownEncodingFailsClosed() {
        // Legacy "ACTIVE" marker must fail closed rather than assuming NO_HEGEMON
        assertThatThrownBy(() -> ActiveCoordinationRecord.decode("ACTIVE"))
                .isInstanceOf(ActiveCoordinationCorruptException.class)
                .hasMessageContaining("Unrecognised active coordination record encoding: ACTIVE");

        assertThatThrownBy(() -> ActiveCoordinationRecord.decode("RANDOM_COORDINATION_STATE"))
                .isInstanceOf(ActiveCoordinationCorruptException.class)
                .hasMessageContaining("Unrecognised active coordination record encoding");

        assertThatThrownBy(() -> ActiveCoordinationRecord.decode("HEGEMON"))
                .isInstanceOf(ActiveCoordinationCorruptException.class)
                .hasMessageContaining("Unrecognised active coordination record encoding");
    }
}
