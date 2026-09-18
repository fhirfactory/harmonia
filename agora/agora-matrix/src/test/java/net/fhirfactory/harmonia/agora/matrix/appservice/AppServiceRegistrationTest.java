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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@Timeout(value = 10, unit = TimeUnit.SECONDS)
class AppServiceRegistrationTest {

    @Test
    @DisplayName("generateDefaultRegistration produces valid Harmonia AS registration")
    void testDefaultRegistration() {
        AppServiceRegistration reg = AppServiceRegistrationGenerator.generateDefaultRegistration(
                "http://agora:8092",
                "test_as_token",
                "test_hs_token"
        );

        assertThat(reg.getId()).isEqualTo(AppServiceRegistrationGenerator.DEFAULT_APP_SERVICE_ID);
        assertThat(reg.getUrl()).isEqualTo("http://agora:8092");
        assertThat(reg.getAsToken()).isEqualTo("test_as_token");
        assertThat(reg.getHsToken()).isEqualTo("test_hs_token");
        assertThat(reg.getSenderLocalpart()).isEqualTo(AppServiceRegistrationGenerator.DEFAULT_SENDER_LOCALPART);
        assertThat(reg.getRateLimited()).isFalse();

        assertThat(reg.getNamespaces().getUsers()).hasSize(1);
        assertThat(reg.getNamespaces().getUsers().get(0).isExclusive()).isTrue();
        assertThat(reg.getNamespaces().getUsers().get(0).getRegex()).isEqualTo(AppServiceRegistrationGenerator.USER_NAMESPACE_REGEX);

        assertThat(reg.getNamespaces().getAliases()).hasSize(1);
        assertThat(reg.getNamespaces().getAliases().get(0).isExclusive()).isTrue();

        assertThat(reg.getNamespaces().getRooms()).hasSize(1);
        assertThat(reg.getNamespaces().getRooms().get(0).isExclusive()).isFalse();
    }

    @Test
    @DisplayName("toYaml generates well-formed YAML format matching Matrix Synapse specification")
    void testToYaml() {
        AppServiceRegistration reg = AppServiceRegistrationGenerator.generateDefaultRegistration(
                "http://agora:8092",
                "as_secret_123",
                "hs_secret_456"
        );

        String yaml = reg.toYaml();

        assertThat(yaml).contains("id: \"harmonia-agora\"");
        assertThat(yaml).contains("url: \"http://agora:8092\"");
        assertThat(yaml).contains("as_token: \"as_secret_123\"");
        assertThat(yaml).contains("hs_token: \"hs_secret_456\"");
        assertThat(yaml).contains("sender_localpart: \"_harmonia_bot\"");
        assertThat(yaml).contains("rate_limited: false");
        assertThat(yaml).contains("namespaces:");
        assertThat(yaml).contains("users:");
        assertThat(yaml).contains("exclusive: true");
        assertThat(yaml).contains("regex: \"@_harmonia_.*\"");
        assertThat(yaml).contains("aliases:");
        assertThat(yaml).contains("rooms:");
    }
}
