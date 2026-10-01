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
import net.fhirfactory.harmonia.hapifhir.persistence.AuthoritativePersistencePort;
import net.fhirfactory.harmonia.hapifhir.persistence.model.AuthoritativePersistenceResult;
import net.fhirfactory.harmonia.model.governedwrite.ExpectedAuthoritativeVersion;
import net.fhirfactory.harmonia.model.governedwrite.ResourceKey;
import net.fhirfactory.harmonia.persistence.config.MnemeAuthoritativeClientConfig;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.HumanName;
import org.hl7.fhir.r5.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Milestone M2.2 Verification Harness: Distributed Authoritative Docker Path & Security Boundary Proof.
 * <p>
 * Proves that:
 * <ol>
 *   <li>Mneme authoritative HTTP client connects across the network boundary to Mnemosyne at {@code /api/authoritative/fhir/*}.</li>
 *   <li>Unauthenticated requests across the network are rejected fail-closed with HTTP 401 Unauthorized by {@code AuthoritativeSecurityInterceptor}.</li>
 *   <li>{@link MnemeAuthoritativeHttpClient} maps the 401 response safely to {@link AuthoritativePersistenceResult.NotCommitted} without throwing exceptions.</li>
 *   <li>Public {@code /fhir/*} is strictly isolated and not substituted.</li>
 *   <li>Offline transport failures are classified safely as {@link AuthoritativePersistenceResult.OutcomeUnknown} or {@link AuthoritativePersistenceResult.NotCommitted}.</li>
 * </ol>
 */
public class DistributedAuthoritativeDockerPathTest {

    private static final String DEFAULT_MNEMOSYNE_URL = "http://localhost:8081/api/authoritative/fhir";
    private static final String DEFAULT_PUBLIC_FHIR_URL = "http://localhost:8081/fhir";

    private FhirContext fhirContext;
    private String mnemosyneUrl;
    private boolean serverAvailable;

    @BeforeEach
    void setUp() {
        fhirContext = FhirContext.forR5();
        String envUrl = System.getenv("HARMONIA_MNEMOSYNE_AUTHORITATIVE_URL");
        mnemosyneUrl = (envUrl != null && !envUrl.isBlank()) ? envUrl : DEFAULT_MNEMOSYNE_URL;

        // Check if Mnemosyne is reachable on the configured endpoint
        serverAvailable = checkServerHealth();
    }

    private boolean checkServerHealth() {
        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(2))
                    .build();
            String healthUrl = mnemosyneUrl.replace("/api/authoritative/fhir", "/actuator/health");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(healthUrl))
                    .timeout(Duration.ofSeconds(2))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }

    private Patient createSamplePatient(String id, String family, String given) {
        Patient patient = new Patient();
        patient.setId(id);
        HumanName name = patient.addName();
        name.setFamily(family);
        name.addGiven(given);
        return patient;
    }

    @Nested
    @DisplayName("Path A: Docker Network Authoritative Boundary Proof (Fail-Closed 401)")
    class PathAFailClosedBoundaryTests {

        @Test
        @DisplayName("READ to /api/authoritative/fhir/{resourceType}/{id} fails closed with 401 NotCommitted")
        void readFailsClosedWith401NotCommitted() {
            assumeTrue(serverAvailable, "Mnemosyne container must be running at " + mnemosyneUrl);

            MnemeAuthoritativeClientConfig config = MnemeAuthoritativeClientConfig.of(
                    mnemosyneUrl,
                    Duration.ofSeconds(5),
                    Duration.ofSeconds(5)
            );
            AuthoritativePersistencePort<IBaseResource> client = new MnemeAuthoritativeHttpClient(config, fhirContext);
            ResourceKey key = ResourceKey.of("Patient", "pat-test-1");

            AuthoritativePersistenceResult<IBaseResource> result = client.read(key);

            assertThat(result).isInstanceOf(AuthoritativePersistenceResult.NotCommitted.class);
            AuthoritativePersistenceResult.NotCommitted<IBaseResource> notCommitted =
                    (AuthoritativePersistenceResult.NotCommitted<IBaseResource>) result;
            assertThat(notCommitted.failureMessage())
                    .contains("HTTP 401");
        }

        @Test
        @DisplayName("CREATE to /api/authoritative/fhir/{resourceType}/{id} fails closed with 401 NotCommitted")
        void createFailsClosedWith401NotCommitted() {
            assumeTrue(serverAvailable, "Mnemosyne container must be running at " + mnemosyneUrl);

            MnemeAuthoritativeClientConfig config = MnemeAuthoritativeClientConfig.of(
                    mnemosyneUrl,
                    Duration.ofSeconds(5),
                    Duration.ofSeconds(5)
            );
            AuthoritativePersistencePort<IBaseResource> client = new MnemeAuthoritativeHttpClient(config, fhirContext);
            ResourceKey key = ResourceKey.of("Patient", "pat-test-1");
            Patient patient = createSamplePatient("pat-test-1", "Smith", "Alice");

            AuthoritativePersistenceResult<IBaseResource> result = client.create(key, patient);

            assertThat(result).isInstanceOf(AuthoritativePersistenceResult.NotCommitted.class);
            AuthoritativePersistenceResult.NotCommitted<IBaseResource> notCommitted =
                    (AuthoritativePersistenceResult.NotCommitted<IBaseResource>) result;
            assertThat(notCommitted.failureMessage())
                    .contains("Security context rejected by Mnemosyne during CREATE (fail-closed): HTTP 401");
        }

        @Test
        @DisplayName("UPDATE to /api/authoritative/fhir/{resourceType}/{id} fails closed with 401 NotCommitted")
        void updateFailsClosedWith401NotCommitted() {
            assumeTrue(serverAvailable, "Mnemosyne container must be running at " + mnemosyneUrl);

            MnemeAuthoritativeClientConfig config = MnemeAuthoritativeClientConfig.of(
                    mnemosyneUrl,
                    Duration.ofSeconds(5),
                    Duration.ofSeconds(5)
            );
            AuthoritativePersistencePort<IBaseResource> client = new MnemeAuthoritativeHttpClient(config, fhirContext);
            ResourceKey key = ResourceKey.of("Patient", "pat-test-1");
            Patient patient = createSamplePatient("pat-test-1", "Smith", "Alice");

            AuthoritativePersistenceResult<IBaseResource> result = client.update(
                    key, patient, ExpectedAuthoritativeVersion.of("1"));

            assertThat(result).isInstanceOf(AuthoritativePersistenceResult.NotCommitted.class);
            AuthoritativePersistenceResult.NotCommitted<IBaseResource> notCommitted =
                    (AuthoritativePersistenceResult.NotCommitted<IBaseResource>) result;
            assertThat(notCommitted.failureMessage())
                    .contains("Security context rejected by Mnemosyne during UPDATE (fail-closed): HTTP 401");
        }
    }

    @Nested
    @DisplayName("Boundary Isolation & Separation from Public /fhir/*")
    class PublicFhirSeparationTests {

        @Test
        @DisplayName("Public /fhir/* path is distinct from dedicated /api/authoritative/fhir/* path")
        void publicFhirIsDistinctFromAuthoritativeApi() throws Exception {
            assumeTrue(serverAvailable, "Mnemosyne container must be running at " + mnemosyneUrl);

            HttpClient httpClient = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(5))
                    .build();

            // 1. Authoritative endpoint fails closed with 401
            HttpRequest authReq = HttpRequest.newBuilder()
                    .uri(URI.create(mnemosyneUrl + "/Patient/pat-test-1"))
                    .GET()
                    .build();
            HttpResponse<String> authResp = httpClient.send(authReq, HttpResponse.BodyHandlers.ofString());
            assertThat(authResp.statusCode()).isEqualTo(401);

            // 2. Public /fhir/metadata endpoint serves HAPI CapabilityStatement (not 401)
            String fhirBase = mnemosyneUrl.replace("/api/authoritative/fhir", "/fhir");
            HttpRequest publicReq = HttpRequest.newBuilder()
                    .uri(URI.create(fhirBase + "/metadata"))
                    .GET()
                    .build();
            HttpResponse<String> publicResp = httpClient.send(publicReq, HttpResponse.BodyHandlers.ofString());
            assertThat(publicResp.statusCode()).isEqualTo(200);
            assertThat(publicResp.body()).contains("CapabilityStatement");
        }
    }

    @Nested
    @DisplayName("Offline Resilience & Failure Classification")
    class OfflineResilienceTests {

        @Test
        @DisplayName("Connection failure against unreachable port is classified safely as OutcomeUnknown")
        void unreachableEndpointClassifiedAsOutcomeUnknown() {
            // Target an unallocated local port where no server is listening
            String offlineUrl = "http://127.0.0.1:59999/api/authoritative/fhir";
            MnemeAuthoritativeClientConfig config = MnemeAuthoritativeClientConfig.of(
                    offlineUrl,
                    Duration.ofMillis(500),
                    Duration.ofMillis(500)
            );
            AuthoritativePersistencePort<IBaseResource> client = new MnemeAuthoritativeHttpClient(config, fhirContext);
            ResourceKey key = ResourceKey.of("Patient", "pat-offline-1");

            AuthoritativePersistenceResult<IBaseResource> result = client.read(key);

            assertThat(result).isInstanceOf(AuthoritativePersistenceResult.OutcomeUnknown.class);
            AuthoritativePersistenceResult.OutcomeUnknown<IBaseResource> outcomeUnknown =
                    (AuthoritativePersistenceResult.OutcomeUnknown<IBaseResource>) result;
            assertThat(outcomeUnknown.message()).isNotEmpty();
        }

        @Test
        @DisplayName("DNS resolution failure on unknown host is classified as NotCommitted or OutcomeUnknown")
        void unresolvableDnsClassifiedSafely() {
            String nonExistentHostUrl = "http://unresolvable-domain-name-harmonia-test.invalid:8080/api/authoritative/fhir";
            MnemeAuthoritativeClientConfig config = MnemeAuthoritativeClientConfig.of(
                    nonExistentHostUrl,
                    Duration.ofMillis(500),
                    Duration.ofMillis(500)
            );
            AuthoritativePersistencePort<IBaseResource> client = new MnemeAuthoritativeHttpClient(config, fhirContext);
            ResourceKey key = ResourceKey.of("Patient", "pat-dns-1");

            AuthoritativePersistenceResult<IBaseResource> result = client.read(key);

            assertThat(result).satisfiesAnyOf(
                    res -> assertThat(res).isInstanceOf(AuthoritativePersistenceResult.NotCommitted.class),
                    res -> assertThat(res).isInstanceOf(AuthoritativePersistenceResult.OutcomeUnknown.class)
            );
        }
    }
}
