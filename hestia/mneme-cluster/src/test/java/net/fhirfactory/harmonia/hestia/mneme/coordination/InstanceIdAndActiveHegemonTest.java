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

class InstanceIdAndActiveHegemonTest {

    @Test
    @DisplayName("InstanceId enforces non-null UUID value and implements Serializable")
    void instanceIdContract() {
        assertThat(Serializable.class.isAssignableFrom(InstanceId.class)).isTrue();

        InstanceId randomId = InstanceId.random();
        assertThat(randomId.value()).isNotNull();

        UUID fixedUuid = UUID.randomUUID();
        InstanceId fixedId = InstanceId.of(fixedUuid);
        assertThat(fixedId.value()).isEqualTo(fixedUuid);
        assertThat(fixedId).isEqualTo(new InstanceId(fixedUuid));

        assertThatThrownBy(() -> InstanceId.of(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("value must not be null");

        assertThatThrownBy(() -> new InstanceId(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("value must not be null");
    }

    @Test
    @DisplayName("ActiveHegemon when absent conveys no active hegemon designated")
    void activeHegemonNone() {
        assertThat(Serializable.class.isAssignableFrom(ActiveHegemon.class)).isTrue();

        ActiveHegemon none = ActiveHegemon.none();
        assertThat(none.isPresent()).isFalse();
        assertThat(none.instanceId()).isNull();
        assertThat(none.instanceIdOptional()).isEmpty();
    }

    @Test
    @DisplayName("ActiveHegemon when present wraps designated instanceId")
    void activeHegemonPresent() {
        InstanceId id = InstanceId.random();
        ActiveHegemon hegemon = ActiveHegemon.of(id);

        assertThat(hegemon.isPresent()).isTrue();
        assertThat(hegemon.instanceId()).isEqualTo(id);
        assertThat(hegemon.instanceIdOptional()).contains(id);

        assertThatThrownBy(() -> ActiveHegemon.of(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("instanceId must not be null");
    }

    @Test
    @DisplayName("ActiveHegemon equality and value semantics")
    void activeHegemonEquality() {
        UUID uuid = UUID.randomUUID();
        ActiveHegemon h1 = ActiveHegemon.of(InstanceId.of(uuid));
        ActiveHegemon h2 = ActiveHegemon.of(InstanceId.of(uuid));
        ActiveHegemon h3 = ActiveHegemon.none();

        assertThat(h1).isEqualTo(h2);
        assertThat(h1.hashCode()).isEqualTo(h2.hashCode());
        assertThat(h1).isNotEqualTo(h3);
    }
}
