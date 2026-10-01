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

import ca.uhn.fhir.context.FhirContext;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import net.fhirfactory.harmonia.hapifhir.persistence.model.AuthoritativePersistenceResult;
import net.fhirfactory.harmonia.model.governedwrite.AuthoritativeVersion;
import net.fhirfactory.harmonia.model.governedwrite.ResourceKey;
import net.fhirfactory.harmonia.persistence.config.MnemeAuthoritativeClientConfig;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.HumanName;
import org.hl7.fhir.r5.model.Practitioner;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("MnemeAuthoritativeHttpClient mTLS Scenarios")
class MnemeAuthoritativeHttpClientMtlsTest {

    private static WireMockServer httpsServer;
    private static int httpsPort;
    private static FhirContext fhirContext;
    private static Path tempTlsDir;

    @BeforeAll
    static void startWireMockHttps() throws Exception {
        fhirContext = FhirContext.forR5();
        tempTlsDir = Files.createTempDirectory("harmonia-mtls-test");

        File serverKeystore = copyResourceToTempFile("/tls/mnemosyne-keystore.p12", "mnemosyne-keystore.p12");
        File serverTruststore = copyResourceToTempFile("/tls/mnemosyne-truststore.p12", "mnemosyne-truststore.p12");

        httpsServer = new WireMockServer(WireMockConfiguration.options()
                .dynamicHttpsPort()
                .keystorePath(serverKeystore.getAbsolutePath())
                .keystorePassword("harmoniapass")
                .keyManagerPassword("harmoniapass")
                .keystoreType("PKCS12")
                .trustStorePath(serverTruststore.getAbsolutePath())
                .trustStorePassword("harmoniapass")
                .trustStoreType("PKCS12")
                .needClientAuth(true)
        );

        httpsServer.start();
        httpsPort = httpsServer.httpsPort();
    }

    @AfterAll
    static void stopWireMockHttps() {
        if (httpsServer != null) {
            httpsServer.stop();
        }
    }

    private static File copyResourceToTempFile(String resourcePath, String fileName) throws Exception {
        File file = tempTlsDir.resolve(fileName).toFile();
        try (InputStream is = MnemeAuthoritativeHttpClientMtlsTest.class.getResourceAsStream(resourcePath);
             FileOutputStream fos = new FileOutputStream(file)) {
            assertThat(is).as("Resource not found: %s", resourcePath).isNotNull();
            is.transferTo(fos);
        }
        return file;
    }

    @Test
    @DisplayName("Authenticated mTLS client with valid certificate successfully performs GET request")
    void validMtlsClientSucceeds() {
        String baseUrl = "https://localhost:" + httpsPort + "/api/authoritative/fhir";
        MnemeAuthoritativeClientConfig config = MnemeAuthoritativeClientConfig.ofTls(
                baseUrl,
                "classpath:/tls/mneme-keystore.p12",
                "harmoniapass",
                "classpath:/tls/mneme-truststore.p12",
                "harmoniapass"
        );

        Practitioner practitioner = new Practitioner();
        practitioner.setId("pr-100");
        practitioner.addName(new HumanName().setFamily("Smith").addGiven("John"));
        String json = fhirContext.newJsonParser().encodeResourceToString(practitioner);

        httpsServer.stubFor(get(urlEqualTo("/api/authoritative/fhir/Practitioner/pr-100"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("ETag", "W/\"1\"")
                        .withBody(json)));

        MnemeAuthoritativeHttpClient client = new MnemeAuthoritativeHttpClient(config, fhirContext);
        ResourceKey key = ResourceKey.of("Practitioner", "pr-100");
        AuthoritativePersistenceResult<IBaseResource> result = client.read(key);

        assertThat(result).isInstanceOf(AuthoritativePersistenceResult.Committed.class);
        AuthoritativePersistenceResult.Committed<IBaseResource> committed =
                (AuthoritativePersistenceResult.Committed<IBaseResource>) result;
        assertThat(committed.authoritativeVersion()).isEqualTo(AuthoritativeVersion.of("1"));
    }

    @Test
    @DisplayName("Client without client certificate fails at TLS handshake and returns NotCommitted")
    void clientWithoutCertFailsHandshake() {
        String baseUrl = "https://localhost:" + httpsPort + "/api/authoritative/fhir";
        // Truststore configured so server cert is trusted, but NO keystore provided
        MnemeAuthoritativeClientConfig config = MnemeAuthoritativeClientConfig.ofTls(
                baseUrl,
                null,
                null,
                "classpath:/tls/mneme-truststore.p12",
                "harmoniapass"
        );

        MnemeAuthoritativeHttpClient client = new MnemeAuthoritativeHttpClient(config, fhirContext);
        ResourceKey key = ResourceKey.of("Practitioner", "pr-100");
        AuthoritativePersistenceResult<IBaseResource> result = client.read(key);

        assertThat(result).isInstanceOf(AuthoritativePersistenceResult.NotCommitted.class);
        AuthoritativePersistenceResult.NotCommitted<IBaseResource> notCommitted =
                (AuthoritativePersistenceResult.NotCommitted<IBaseResource>) result;
        assertThat(notCommitted.failureMessage()).containsIgnoringCase("TLS handshake rejected");
    }

    @Test
    @DisplayName("Client with untrusted client certificate (Rogue CA) fails TLS handshake and returns NotCommitted")
    void clientWithUntrustedCertFailsHandshake() {
        String baseUrl = "https://localhost:" + httpsPort + "/api/authoritative/fhir";
        // Client presents untrusted cert signed by rogue CA
        MnemeAuthoritativeClientConfig config = MnemeAuthoritativeClientConfig.ofTls(
                baseUrl,
                "classpath:/tls/untrusted-keystore.p12",
                "harmoniapass",
                "classpath:/tls/mneme-truststore.p12",
                "harmoniapass"
        );

        MnemeAuthoritativeHttpClient client = new MnemeAuthoritativeHttpClient(config, fhirContext);
        ResourceKey key = ResourceKey.of("Practitioner", "pr-100");
        AuthoritativePersistenceResult<IBaseResource> result = client.read(key);

        assertThat(result).isInstanceOf(AuthoritativePersistenceResult.NotCommitted.class);
        AuthoritativePersistenceResult.NotCommitted<IBaseResource> notCommitted =
                (AuthoritativePersistenceResult.NotCommitted<IBaseResource>) result;
        assertThat(notCommitted.failureMessage()).containsIgnoringCase("TLS handshake rejected");
    }
}
