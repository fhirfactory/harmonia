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

package net.fhirfactory.harmonia.persistence.client;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.net.ssl.SSLContext;
import java.io.FileNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("SslContextFactory Unit Tests")
class SslContextFactoryTest {

    @Test
    @DisplayName("Successfully creates SSLContext with client keystore and truststore from classpath")
    void createsSslContextFromClasspath() throws Exception {
        SSLContext sslContext = SslContextFactory.createSslContext(
                "classpath:/tls/mneme-keystore.p12",
                "harmoniapass",
                "PKCS12",
                "classpath:/tls/mneme-truststore.p12",
                "harmoniapass",
                "PKCS12"
        );

        assertThat(sslContext).isNotNull();
        assertThat(sslContext.getProtocol()).isEqualTo("TLS");
    }

    @Test
    @DisplayName("Successfully creates SSLContext with null keystore and truststore (defaults)")
    void createsDefaultSslContext() throws Exception {
        SSLContext sslContext = SslContextFactory.createSslContext(
                null,
                null,
                null,
                null,
                null,
                null
        );

        assertThat(sslContext).isNotNull();
    }

    @Test
    @DisplayName("Throws FileNotFoundException on non-existent keystore path")
    void throwsOnMissingKeystore() {
        assertThatThrownBy(() -> SslContextFactory.createSslContext(
                "classpath:/tls/nonexistent-keystore.p12",
                "password",
                "PKCS12",
                null,
                null,
                null
        )).isInstanceOf(FileNotFoundException.class);
    }
}
