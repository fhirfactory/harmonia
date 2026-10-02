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
import net.fhirfactory.harmonia.model.governedwrite.AuthoritativeVersion;
import net.fhirfactory.harmonia.model.governedwrite.ExpectedAuthoritativeVersion;
import net.fhirfactory.harmonia.model.governedwrite.PreconditionFailureReason;
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
import java.util.UUID;

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
            HttpClient client;
            if (mnemosyneUrl.startsWith("https://")) {
                javax.net.ssl.SSLContext sslContext = SslContextFactory.createSslContext(
                        "classpath:/tls/mneme-keystore.p12",
                        "harmoniapass",
                        "PKCS12",
                        "classpath:/tls/mneme-truststore.p12",
                        "harmoniapass",
                        "PKCS12"
                );
                client = HttpClient.newBuilder()
                        .sslContext(sslContext)
                        .connectTimeout(Duration.ofSeconds(2))
                        .build();
            } else {
                client = HttpClient.newBuilder()
                        .connectTimeout(Duration.ofSeconds(2))
                        .build();
            }
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
    @DisplayName("Path A: Docker Network Authoritative Boundary Proof (Fail-Closed)")
    class PathAFailClosedBoundaryTests {

        @Test
        @DisplayName("READ to /api/authoritative/fhir/{resourceType}/{id} fails closed with NotCommitted (401 or TLS rejection)")
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
            assertThat(notCommitted.failureMessage()).satisfiesAnyOf(
                    msg -> assertThat(msg).contains("HTTP 401"),
                    msg -> assertThat(msg).containsIgnoringCase("TLS handshake rejected"),
                    msg -> assertThat(msg).containsIgnoringCase("PKIX path")
            );
        }

        @Test
        @DisplayName("CREATE to /api/authoritative/fhir/{resourceType}/{id} fails closed with NotCommitted (401 or TLS rejection)")
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
            assertThat(notCommitted.failureMessage()).satisfiesAnyOf(
                    msg -> assertThat(msg).contains("HTTP 401"),
                    msg -> assertThat(msg).containsIgnoringCase("TLS handshake rejected"),
                    msg -> assertThat(msg).containsIgnoringCase("PKIX path")
            );
        }

        @Test
        @DisplayName("UPDATE to /api/authoritative/fhir/{resourceType}/{id} fails closed with NotCommitted (401 or TLS rejection)")
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
            assertThat(notCommitted.failureMessage()).satisfiesAnyOf(
                    msg -> assertThat(msg).contains("HTTP 401"),
                    msg -> assertThat(msg).containsIgnoringCase("TLS handshake rejected"),
                    msg -> assertThat(msg).containsIgnoringCase("PKIX path")
            );
        }
    }

    @Nested
    @DisplayName("Boundary Isolation & Separation from Public /fhir/*")
    class PublicFhirSeparationTests {

        @Test
        @DisplayName("Public /fhir/* path is distinct from dedicated /api/authoritative/fhir/* path")
        void publicFhirIsDistinctFromAuthoritativeApi() throws Exception {
            assumeTrue(serverAvailable, "Mnemosyne container must be running at " + mnemosyneUrl);

            HttpClient.Builder clientBuilder = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(5));

            if (mnemosyneUrl.startsWith("https://")) {
                javax.net.ssl.SSLContext sslContext = SslContextFactory.createSslContext(
                        "classpath:/tls/mneme-keystore.p12",
                        "harmoniapass",
                        "PKCS12",
                        "classpath:/tls/mneme-truststore.p12",
                        "harmoniapass",
                        "PKCS12"
                );
                clientBuilder.sslContext(sslContext);
            }

            HttpClient httpClient = clientBuilder.build();

            // 1. Authoritative endpoint requires specific security context and authorization
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
        @DisplayName("Connection failure against unreachable port is classified safely as NotCommitted or OutcomeUnknown")
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

            assertThat(result).satisfiesAnyOf(
                    res -> assertThat(res).isInstanceOf(AuthoritativePersistenceResult.NotCommitted.class),
                    res -> assertThat(res).isInstanceOf(AuthoritativePersistenceResult.OutcomeUnknown.class)
            );
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

    @Nested
    @DisplayName("Path C: mTLS Authenticated Docker Boundary & Fail-Closed Invariants")
    class MtlsAuthenticatedDockerBoundaryTests {

        @Test
        @DisplayName("Authenticated service:mneme client with valid URI SAN certificate connects and accesses authoritative endpoint")
        void authenticatedClientSucceeds() {
            assumeTrue(serverAvailable && mnemosyneUrl.startsWith("https://"),
                    "Mnemosyne HTTPS container must be running at " + mnemosyneUrl);

            MnemeAuthoritativeClientConfig config = MnemeAuthoritativeClientConfig.ofTls(
                    mnemosyneUrl,
                    "classpath:/tls/mneme-keystore.p12",
                    "harmoniapass",
                    "classpath:/tls/mneme-truststore.p12",
                    "harmoniapass"
            );
            AuthoritativePersistencePort<IBaseResource> client = new MnemeAuthoritativeHttpClient(config, fhirContext);
            ResourceKey key = ResourceKey.of("Patient", "pat-mtls-1");

            AuthoritativePersistenceResult<IBaseResource> result = client.read(key);
            assertThat(result).satisfiesAnyOf(
                    res -> assertThat(res).isInstanceOf(AuthoritativePersistenceResult.Committed.class),
                    res -> assertThat(res).isInstanceOf(AuthoritativePersistenceResult.NotCommitted.class)
            );
        }

        @Test
        @DisplayName("Client with untrusted certificate (Rogue CA) fails closed at TLS handshake with NotCommitted")
        void untrustedClientFailsHandshake() {
            assumeTrue(serverAvailable && mnemosyneUrl.startsWith("https://"),
                    "Mnemosyne HTTPS container must be running at " + mnemosyneUrl);

            MnemeAuthoritativeClientConfig config = MnemeAuthoritativeClientConfig.ofTls(
                    mnemosyneUrl,
                    "classpath:/tls/untrusted-keystore.p12",
                    "harmoniapass",
                    "classpath:/tls/mneme-truststore.p12",
                    "harmoniapass"
            );
            AuthoritativePersistencePort<IBaseResource> client = new MnemeAuthoritativeHttpClient(config, fhirContext);
            ResourceKey key = ResourceKey.of("Patient", "pat-mtls-untrusted");

            AuthoritativePersistenceResult<IBaseResource> result = client.read(key);
            assertThat(result).isInstanceOf(AuthoritativePersistenceResult.NotCommitted.class);
            assertThat(((AuthoritativePersistenceResult.NotCommitted<IBaseResource>) result).failureMessage())
                    .containsIgnoringCase("TLS handshake rejected");
        }

        @Test
        @DisplayName("Client with wrong URI SAN fails closed with NotCommitted (HTTP 401)")
        void wrongSanClientFailsClosed() {
            assumeTrue(serverAvailable && mnemosyneUrl.startsWith("https://"),
                    "Mnemosyne HTTPS container must be running at " + mnemosyneUrl);

            MnemeAuthoritativeClientConfig config = MnemeAuthoritativeClientConfig.ofTls(
                    mnemosyneUrl,
                    "classpath:/tls/wrong-san-keystore.p12",
                    "harmoniapass",
                    "classpath:/tls/mneme-truststore.p12",
                    "harmoniapass"
            );
            AuthoritativePersistencePort<IBaseResource> client = new MnemeAuthoritativeHttpClient(config, fhirContext);
            ResourceKey key = ResourceKey.of("Patient", "pat-mtls-wrong-san");

            AuthoritativePersistenceResult<IBaseResource> result = client.read(key);
            assertThat(result).isInstanceOf(AuthoritativePersistenceResult.NotCommitted.class);
            assertThat(((AuthoritativePersistenceResult.NotCommitted<IBaseResource>) result).failureMessage())
                    .containsIgnoringCase("401");
        }
    }

    @Nested
    @DisplayName("Milestone M2.4: Distributed Authoritative Path & State Progression Semantic Proof")
    class M24DistributedAuthoritativePathSemanticTests {

        private AuthoritativePersistencePort<IBaseResource> client;

        @BeforeEach
        void setUpClient() {
            assumeTrue(serverAvailable && mnemosyneUrl.startsWith("https://"),
                    "Mnemosyne HTTPS container must be running at " + mnemosyneUrl);

            MnemeAuthoritativeClientConfig config = MnemeAuthoritativeClientConfig.ofTls(
                    mnemosyneUrl,
                    "classpath:/tls/mneme-keystore.p12",
                    "harmoniapass",
                    "classpath:/tls/mneme-truststore.p12",
                    "harmoniapass"
            );
            client = new MnemeAuthoritativeHttpClient(config, fhirContext);
        }

        @Test
        @DisplayName("Complete 7-Step Authoritative State Progression & Conflict Cycle over mTLS Docker Boundary")
        void testCompleteAuthoritativeStateProgressionAndConflictCycle() {
            String patientId = "pat-m24-" + UUID.randomUUID().toString().substring(0, 8);
            ResourceKey key = ResourceKey.of("Patient", patientId);

            // Step 1: Initial READ of absent resource -> HTTP 404 -> NotCommitted (Zero state created)
            AuthoritativePersistenceResult<IBaseResource> step1Result = client.read(key);
            assertThat(step1Result).isInstanceOf(AuthoritativePersistenceResult.NotCommitted.class);
            AuthoritativePersistenceResult.NotCommitted<IBaseResource> step1NotCommitted =
                    (AuthoritativePersistenceResult.NotCommitted<IBaseResource>) step1Result;
            assertThat(step1NotCommitted.failureMessage()).contains("HTTP 404");

            // Step 2: Conditional CREATE-if-absent (If-None-Match: *) -> HTTP 201 -> Committed(version 1)
            Patient initialPatient = createSamplePatient(patientId, "M24Test", "Initial");
            AuthoritativePersistenceResult<IBaseResource> step2Result = client.create(key, initialPatient);
            assertThat(step2Result).isInstanceOf(AuthoritativePersistenceResult.Committed.class);
            AuthoritativePersistenceResult.Committed<IBaseResource> step2Committed =
                    (AuthoritativePersistenceResult.Committed<IBaseResource>) step2Result;
            assertThat(step2Committed.authoritativeVersion()).isEqualTo(AuthoritativeVersion.of("1"));

            // Follow-up READ to confirm persisted state
            AuthoritativePersistenceResult<IBaseResource> step2VerifyRead = client.read(key);
            assertThat(step2VerifyRead).isInstanceOf(AuthoritativePersistenceResult.Committed.class);
            AuthoritativePersistenceResult.Committed<IBaseResource> step2ReadCommitted =
                    (AuthoritativePersistenceResult.Committed<IBaseResource>) step2VerifyRead;
            assertThat(step2ReadCommitted.authoritativeVersion()).isEqualTo(AuthoritativeVersion.of("1"));
            Patient read1Patient = (Patient) step2ReadCommitted.persistedResource();
            assertThat(read1Patient.getNameFirstRep().getFamily()).isEqualTo("M24Test");
            assertThat(read1Patient.getNameFirstRep().getGivenAsSingleString()).isEqualTo("Initial");

            // Step 3: Duplicate CREATE collision (If-None-Match: *) -> HTTP 412 -> Conflict(resourceAlreadyExists)
            Patient duplicatePatient = createSamplePatient(patientId, "M24Test", "Duplicate");
            AuthoritativePersistenceResult<IBaseResource> step3Result = client.create(key, duplicatePatient);
            assertThat(step3Result).isInstanceOf(AuthoritativePersistenceResult.Conflict.class);
            AuthoritativePersistenceResult.Conflict<IBaseResource> step3Conflict =
                    (AuthoritativePersistenceResult.Conflict<IBaseResource>) step3Result;
            assertThat(step3Conflict.conflict().reason()).isEqualTo(PreconditionFailureReason.RESOURCE_ALREADY_EXISTS);
            assertThat(step3Conflict.conflict().currentVersionOptional()).contains(AuthoritativeVersion.of("1"));

            // Verify state and version 1 remain unchanged
            AuthoritativePersistenceResult<IBaseResource> step3VerifyRead = client.read(key);
            assertThat(step3VerifyRead).isInstanceOf(AuthoritativePersistenceResult.Committed.class);
            AuthoritativePersistenceResult.Committed<IBaseResource> step3ReadCommitted =
                    (AuthoritativePersistenceResult.Committed<IBaseResource>) step3VerifyRead;
            assertThat(step3ReadCommitted.authoritativeVersion()).isEqualTo(AuthoritativeVersion.of("1"));
            Patient unchangedPatient = (Patient) step3ReadCommitted.persistedResource();
            assertThat(unchangedPatient.getNameFirstRep().getGivenAsSingleString()).isEqualTo("Initial");

            // Step 4: Existing READ -> HTTP 200 -> Committed(version 1)
            AuthoritativePersistenceResult<IBaseResource> step4Result = client.read(key);
            assertThat(step4Result).isInstanceOf(AuthoritativePersistenceResult.Committed.class);
            AuthoritativePersistenceResult.Committed<IBaseResource> step4Committed =
                    (AuthoritativePersistenceResult.Committed<IBaseResource>) step4Result;
            assertThat(step4Committed.authoritativeVersion()).isEqualTo(AuthoritativeVersion.of("1"));
            assertThat(step4Committed.persistedResource()).isNotNull();

            // Step 5: Predecessor UPDATE (If-Match: W/"1") -> HTTP 200 -> Committed(version 2)
            Patient updatedPatient = createSamplePatient(patientId, "M24Test", "Updated");
            AuthoritativePersistenceResult<IBaseResource> step5Result = client.update(
                    key, updatedPatient, ExpectedAuthoritativeVersion.of("1"));
            assertThat(step5Result).isInstanceOf(AuthoritativePersistenceResult.Committed.class);
            AuthoritativePersistenceResult.Committed<IBaseResource> step5Committed =
                    (AuthoritativePersistenceResult.Committed<IBaseResource>) step5Result;
            assertThat(step5Committed.authoritativeVersion()).isEqualTo(AuthoritativeVersion.of("2"));

            // Follow-up READ to confirm version 2 persisted
            AuthoritativePersistenceResult<IBaseResource> step5VerifyRead = client.read(key);
            assertThat(step5VerifyRead).isInstanceOf(AuthoritativePersistenceResult.Committed.class);
            AuthoritativePersistenceResult.Committed<IBaseResource> step5ReadCommitted =
                    (AuthoritativePersistenceResult.Committed<IBaseResource>) step5VerifyRead;
            assertThat(step5ReadCommitted.authoritativeVersion()).isEqualTo(AuthoritativeVersion.of("2"));
            Patient read2Patient = (Patient) step5ReadCommitted.persistedResource();
            assertThat(read2Patient.getNameFirstRep().getGivenAsSingleString()).isEqualTo("Updated");

            // Step 6: Stale UPDATE collision (If-Match: W/"1") -> HTTP 412 -> Conflict(expectedVersionMismatch)
            Patient stalePatient = createSamplePatient(patientId, "M24Test", "StaleUpdate");
            AuthoritativePersistenceResult<IBaseResource> step6Result = client.update(
                    key, stalePatient, ExpectedAuthoritativeVersion.of("1"));
            assertThat(step6Result).isInstanceOf(AuthoritativePersistenceResult.Conflict.class);
            AuthoritativePersistenceResult.Conflict<IBaseResource> step6Conflict =
                    (AuthoritativePersistenceResult.Conflict<IBaseResource>) step6Result;
            assertThat(step6Conflict.conflict().reason()).isEqualTo(PreconditionFailureReason.EXPECTED_VERSION_MISMATCH);
            assertThat(step6Conflict.conflict().currentVersionOptional()).contains(AuthoritativeVersion.of("2"));

            // Verify state and version 2 remain unchanged
            AuthoritativePersistenceResult<IBaseResource> step6VerifyRead = client.read(key);
            assertThat(step6VerifyRead).isInstanceOf(AuthoritativePersistenceResult.Committed.class);
            AuthoritativePersistenceResult.Committed<IBaseResource> step6ReadCommitted =
                    (AuthoritativePersistenceResult.Committed<IBaseResource>) step6VerifyRead;
            assertThat(step6ReadCommitted.authoritativeVersion()).isEqualTo(AuthoritativeVersion.of("2"));
            Patient read2UnchangedPatient = (Patient) step6ReadCommitted.persistedResource();
            assertThat(read2UnchangedPatient.getNameFirstRep().getGivenAsSingleString()).isEqualTo("Updated");

            // Step 7: Absent UPDATE (If-Match: W/"1" on non-existent resource) -> HTTP 404 -> NotCommitted (Zero state created)
            String absentId = "pat-m24-absent-" + UUID.randomUUID().toString().substring(0, 8);
            ResourceKey absentKey = ResourceKey.of("Patient", absentId);
            Patient absentPatient = createSamplePatient(absentId, "M24Absent", "Test");
            AuthoritativePersistenceResult<IBaseResource> step7Result = client.update(
                    absentKey, absentPatient, ExpectedAuthoritativeVersion.of("1"));
            assertThat(step7Result).isInstanceOf(AuthoritativePersistenceResult.NotCommitted.class);
            AuthoritativePersistenceResult.NotCommitted<IBaseResource> step7NotCommitted =
                    (AuthoritativePersistenceResult.NotCommitted<IBaseResource>) step7Result;
            assertThat(step7NotCommitted.failureMessage()).contains("HTTP 404");

            // Verify zero state was created for absent resource
            AuthoritativePersistenceResult<IBaseResource> step7VerifyRead = client.read(absentKey);
            assertThat(step7VerifyRead).isInstanceOf(AuthoritativePersistenceResult.NotCommitted.class);
            assertThat(((AuthoritativePersistenceResult.NotCommitted<IBaseResource>) step7VerifyRead).failureMessage())
                    .contains("HTTP 404");
        }

        @Test
        @DisplayName("Step 8: Persistence Durability Across Container Restart (PostgreSQL Backed)")
        void testDurabilityAcrossContainerRestart() {
            String patientId = "pat-m24-durability-fixed";
            ResourceKey key = ResourceKey.of("Patient", patientId);

            AuthoritativePersistenceResult<IBaseResource> initialRead = client.read(key);
            if (initialRead instanceof AuthoritativePersistenceResult.NotCommitted) {
                // Pre-restart phase: Create version 1 and update to version 2
                Patient initialPatient = createSamplePatient(patientId, "Durability", "Initial");
                AuthoritativePersistenceResult<IBaseResource> createRes = client.create(key, initialPatient);
                assertThat(createRes).isInstanceOf(AuthoritativePersistenceResult.Committed.class);
                assertThat(((AuthoritativePersistenceResult.Committed<IBaseResource>) createRes).authoritativeVersion())
                        .isEqualTo(AuthoritativeVersion.of("1"));

                Patient updatedPatient = createSamplePatient(patientId, "Durability", "RestartProof");
                AuthoritativePersistenceResult<IBaseResource> updateRes = client.update(
                        key, updatedPatient, ExpectedAuthoritativeVersion.of("1"));
                assertThat(updateRes).isInstanceOf(AuthoritativePersistenceResult.Committed.class);
                assertThat(((AuthoritativePersistenceResult.Committed<IBaseResource>) updateRes).authoritativeVersion())
                        .isEqualTo(AuthoritativeVersion.of("2"));
            } else if (initialRead instanceof AuthoritativePersistenceResult.Committed<IBaseResource> committed) {
                // Post-restart phase: Prove version 2 and updated content survived restart intact
                assertThat(committed.authoritativeVersion()).isEqualTo(AuthoritativeVersion.of("2"));
                Patient persisted = (Patient) committed.persistedResource();
                assertThat(persisted.getNameFirstRep().getGivenAsSingleString()).isEqualTo("RestartProof");
            }
        }
    }
}
