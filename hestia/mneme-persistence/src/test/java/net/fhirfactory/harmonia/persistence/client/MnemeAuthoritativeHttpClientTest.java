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
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.http.Fault;
import net.fhirfactory.harmonia.hapifhir.persistence.AuthoritativePersistencePort;
import net.fhirfactory.harmonia.hapifhir.persistence.model.AuthoritativePersistenceResult;
import net.fhirfactory.harmonia.model.governedwrite.AuthoritativeCommitOutcome;
import net.fhirfactory.harmonia.model.governedwrite.AuthoritativeVersion;
import net.fhirfactory.harmonia.model.governedwrite.ExpectedAuthoritativeVersion;
import net.fhirfactory.harmonia.model.governedwrite.PreconditionFailureReason;
import net.fhirfactory.harmonia.model.governedwrite.ResourceKey;
import net.fhirfactory.harmonia.persistence.config.MnemeAuthoritativeClientConfig;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.HumanName;
import org.hl7.fhir.r5.model.Practitioner;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.time.Duration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

class MnemeAuthoritativeHttpClientTest {

    private static WireMockServer wireMockServer;
    private static FhirContext fhirContext;

    private MnemeAuthoritativeHttpClient client;
    private String baseUrl;

    @BeforeAll
    static void startWireMock() {
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());
        fhirContext = FhirContext.forR5();
    }

    @AfterAll
    static void stopWireMock() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    @BeforeEach
    void setUp() {
        wireMockServer.resetAll();
        baseUrl = "http://localhost:" + wireMockServer.port() + "/api/authoritative/fhir";
        MnemeAuthoritativeClientConfig config = MnemeAuthoritativeClientConfig.of(
                baseUrl,
                Duration.ofSeconds(2),
                Duration.ofSeconds(2)
        );
        client = new MnemeAuthoritativeHttpClient(config, fhirContext);
    }

    private Practitioner samplePractitioner(String id, String family, String given) {
        Practitioner p = new Practitioner();
        p.setId(id);
        HumanName name = p.addName();
        name.setFamily(family);
        name.addGiven(given);
        return p;
    }

    // =========================================================================
    // 1. Authoritative READ Scenarios
    // =========================================================================
    @Nested
    @DisplayName("Authoritative READ Scenarios")
    class ReadScenarios {

        @Test
        @DisplayName("Scenario 1.1: 200 OK with valid ETag returns Committed result")
        void read200OkWithValidETagReturnsCommitted() {
            ResourceKey key = ResourceKey.of("Practitioner", "pr-100");
            Practitioner expectedResource = samplePractitioner("pr-100", "Smith", "John");
            String resourceJson = fhirContext.newJsonParser().encodeResourceToString(expectedResource);

            stubFor(get(urlEqualTo("/api/authoritative/fhir/Practitioner/pr-100"))
                    .withHeader("Accept", containing("application/fhir+json"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "application/fhir+json; charset=UTF-8")
                            .withHeader("ETag", "W/\"1\"")
                            .withBody(resourceJson)));

            AuthoritativePersistenceResult<IBaseResource> result = client.read(key);

            assertThat(result).isInstanceOf(AuthoritativePersistenceResult.Committed.class);
            AuthoritativePersistenceResult.Committed<IBaseResource> committed =
                    (AuthoritativePersistenceResult.Committed<IBaseResource>) result;
            assertThat(committed.outcome()).isEqualTo(AuthoritativeCommitOutcome.COMMITTED);
            assertThat(committed.authoritativeVersion()).isEqualTo(AuthoritativeVersion.of("1"));
            assertThat(committed.persistedResource()).isInstanceOf(Practitioner.class);
            Practitioner p = (Practitioner) committed.persistedResource();
            assertThat(p.getNameFirstRep().getFamily()).isEqualTo("Smith");

            verify(getRequestedFor(urlEqualTo("/api/authoritative/fhir/Practitioner/pr-100")));
        }

        @Test
        @DisplayName("Scenario 1.2: 200 OK with missing ETag fails to OutcomeUnknown (no fallback to meta.versionId)")
        void read200OkWithMissingETagReturnsOutcomeUnknown() {
            ResourceKey key = ResourceKey.of("Practitioner", "pr-100");
            Practitioner resource = samplePractitioner("pr-100", "Smith", "John");
            resource.getMeta().setVersionId("99"); // Deliberately set to verify no fallback
            String resourceJson = fhirContext.newJsonParser().encodeResourceToString(resource);

            stubFor(get(urlEqualTo("/api/authoritative/fhir/Practitioner/pr-100"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "application/fhir+json; charset=UTF-8")
                            .withBody(resourceJson)));

            AuthoritativePersistenceResult<IBaseResource> result = client.read(key);

            assertThat(result).isInstanceOf(AuthoritativePersistenceResult.OutcomeUnknown.class);
            AuthoritativePersistenceResult.OutcomeUnknown<IBaseResource> unknown =
                    (AuthoritativePersistenceResult.OutcomeUnknown<IBaseResource>) result;
            assertThat(unknown.outcome()).isEqualTo(AuthoritativeCommitOutcome.UNKNOWN);
            assertThat(unknown.message()).containsIgnoringCase("Missing authoritative ETag header");
        }

        @Test
        @DisplayName("Scenario 1.3: 200 OK with malformed ETag fails to OutcomeUnknown")
        void read200OkWithMalformedETagReturnsOutcomeUnknown() {
            ResourceKey key = ResourceKey.of("Practitioner", "pr-100");
            Practitioner resource = samplePractitioner("pr-100", "Smith", "John");
            String resourceJson = fhirContext.newJsonParser().encodeResourceToString(resource);

            stubFor(get(urlEqualTo("/api/authoritative/fhir/Practitioner/pr-100"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader("ETag", "W/\"   \"")
                            .withBody(resourceJson)));

            AuthoritativePersistenceResult<IBaseResource> result = client.read(key);

            assertThat(result).isInstanceOf(AuthoritativePersistenceResult.OutcomeUnknown.class);
        }

        @Test
        @DisplayName("Scenario 1.4: 404 Not Found returns Absent")
        void read404ReturnsAbsent() {
            ResourceKey key = ResourceKey.of("Practitioner", "pr-missing");

            stubFor(get(urlEqualTo("/api/authoritative/fhir/Practitioner/pr-missing"))
                    .willReturn(aResponse().withStatus(404)));

            AuthoritativePersistenceResult<IBaseResource> result = client.read(key);

            assertThat(result).isInstanceOf(AuthoritativePersistenceResult.Absent.class);
            AuthoritativePersistenceResult.Absent<IBaseResource> absent =
                    (AuthoritativePersistenceResult.Absent<IBaseResource>) result;
            assertThat(absent.message()).contains("Resource not found");
            assertThat(result.isCommitted()).isFalse();
        }

        @Test
        @DisplayName("Scenario 1.5: 410 Gone returns NotCommitted")
        void read410ReturnsNotCommitted() {
            ResourceKey key = ResourceKey.of("Practitioner", "pr-gone");

            stubFor(get(urlEqualTo("/api/authoritative/fhir/Practitioner/pr-gone"))
                    .willReturn(aResponse().withStatus(410)));

            AuthoritativePersistenceResult<IBaseResource> result = client.read(key);

            assertThat(result).isInstanceOf(AuthoritativePersistenceResult.NotCommitted.class);
            AuthoritativePersistenceResult.NotCommitted<IBaseResource> notCommitted =
                    (AuthoritativePersistenceResult.NotCommitted<IBaseResource>) result;
            assertThat(notCommitted.failureMessage()).contains("HTTP 410");
        }

        @Test
        @DisplayName("Absent is structurally distinguishable from all NotCommitted failure modes without string parsing")
        void readAbsentIsDistinguishableFromAllNotCommitted() {
            // 404 -> Absent
            stubFor(get(urlEqualTo("/api/authoritative/fhir/Practitioner/pr-absent"))
                    .willReturn(aResponse().withStatus(404)));
            AuthoritativePersistenceResult<IBaseResource> absentRes = client.read(ResourceKey.of("Practitioner", "pr-absent"));
            assertThat(absentRes).isInstanceOf(AuthoritativePersistenceResult.Absent.class);
            assertThat(absentRes instanceof AuthoritativePersistenceResult.NotCommitted).isFalse();

            // 410 -> NotCommitted
            stubFor(get(urlEqualTo("/api/authoritative/fhir/Practitioner/pr-gone-dist"))
                    .willReturn(aResponse().withStatus(410)));
            AuthoritativePersistenceResult<IBaseResource> goneRes = client.read(ResourceKey.of("Practitioner", "pr-gone-dist"));
            assertThat(goneRes).isInstanceOf(AuthoritativePersistenceResult.NotCommitted.class);
            assertThat(goneRes instanceof AuthoritativePersistenceResult.Absent).isFalse();

            // 400 -> NotCommitted
            stubFor(get(urlEqualTo("/api/authoritative/fhir/Practitioner/pr-bad-dist"))
                    .willReturn(aResponse().withStatus(400)));
            AuthoritativePersistenceResult<IBaseResource> badRes = client.read(ResourceKey.of("Practitioner", "pr-bad-dist"));
            assertThat(badRes).isInstanceOf(AuthoritativePersistenceResult.NotCommitted.class);
            assertThat(badRes instanceof AuthoritativePersistenceResult.Absent).isFalse();

            // 401 -> NotCommitted
            stubFor(get(urlEqualTo("/api/authoritative/fhir/Practitioner/pr-unauth-dist"))
                    .willReturn(aResponse().withStatus(401)));
            AuthoritativePersistenceResult<IBaseResource> unauthRes = client.read(ResourceKey.of("Practitioner", "pr-unauth-dist"));
            assertThat(unauthRes).isInstanceOf(AuthoritativePersistenceResult.NotCommitted.class);
            assertThat(unauthRes instanceof AuthoritativePersistenceResult.Absent).isFalse();

            // 403 -> NotCommitted
            stubFor(get(urlEqualTo("/api/authoritative/fhir/Practitioner/pr-forbid-dist"))
                    .willReturn(aResponse().withStatus(403)));
            AuthoritativePersistenceResult<IBaseResource> forbidRes = client.read(ResourceKey.of("Practitioner", "pr-forbid-dist"));
            assertThat(forbidRes).isInstanceOf(AuthoritativePersistenceResult.NotCommitted.class);
            assertThat(forbidRes instanceof AuthoritativePersistenceResult.Absent).isFalse();

            // Local validation failure -> NotCommitted
            AuthoritativePersistenceResult<IBaseResource> nullKeyRes = client.read(null);
            assertThat(nullKeyRes).isInstanceOf(AuthoritativePersistenceResult.NotCommitted.class);
            assertThat(nullKeyRes instanceof AuthoritativePersistenceResult.Absent).isFalse();
        }

        @Test
        @DisplayName("Scenario 1.6: 401 Unauthorized returns NotCommitted (fail-closed)")
        void read401ReturnsNotCommittedFailClosed() {
            ResourceKey key = ResourceKey.of("Practitioner", "pr-secure");

            stubFor(get(urlEqualTo("/api/authoritative/fhir/Practitioner/pr-secure"))
                    .willReturn(aResponse().withStatus(401)));

            AuthoritativePersistenceResult<IBaseResource> result = client.read(key);

            assertThat(result).isInstanceOf(AuthoritativePersistenceResult.NotCommitted.class);
            AuthoritativePersistenceResult.NotCommitted<IBaseResource> notCommitted =
                    (AuthoritativePersistenceResult.NotCommitted<IBaseResource>) result;
            assertThat(notCommitted.failureMessage()).containsIgnoringCase("Authorization denied");
        }

        @Test
        @DisplayName("Scenario 1.7: 403 Forbidden returns NotCommitted (fail-closed)")
        void read403ReturnsNotCommittedFailClosed() {
            ResourceKey key = ResourceKey.of("Practitioner", "pr-forbidden");

            stubFor(get(urlEqualTo("/api/authoritative/fhir/Practitioner/pr-forbidden"))
                    .willReturn(aResponse().withStatus(403)));

            AuthoritativePersistenceResult<IBaseResource> result = client.read(key);

            assertThat(result).isInstanceOf(AuthoritativePersistenceResult.NotCommitted.class);
        }

        @Test
        @DisplayName("Scenario 1.8: 500 Server Error returns OutcomeUnknown")
        void read500ReturnsOutcomeUnknown() {
            ResourceKey key = ResourceKey.of("Practitioner", "pr-err");

            stubFor(get(urlEqualTo("/api/authoritative/fhir/Practitioner/pr-err"))
                    .willReturn(aResponse().withStatus(500)));

            AuthoritativePersistenceResult<IBaseResource> result = client.read(key);

            assertThat(result).isInstanceOf(AuthoritativePersistenceResult.OutcomeUnknown.class);
        }

        @Test
        @DisplayName("Scenario 1.9: Consistent ETag and X-Harmonia-Authoritative-Version headers return Committed")
        void readConsistentVersionHeadersReturnsCommitted() {
            ResourceKey key = ResourceKey.of("Practitioner", "pr-100");
            Practitioner expectedResource = samplePractitioner("pr-100", "Smith", "John");
            String resourceJson = fhirContext.newJsonParser().encodeResourceToString(expectedResource);

            stubFor(get(urlEqualTo("/api/authoritative/fhir/Practitioner/pr-100"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader("ETag", "W/\"3\"")
                            .withHeader("X-Harmonia-Authoritative-Version", "3")
                            .withBody(resourceJson)));

            AuthoritativePersistenceResult<IBaseResource> result = client.read(key);

            assertThat(result).isInstanceOf(AuthoritativePersistenceResult.Committed.class);
            assertThat(((AuthoritativePersistenceResult.Committed<IBaseResource>) result).authoritativeVersion())
                    .isEqualTo(AuthoritativeVersion.of("3"));
        }

        @Test
        @DisplayName("Scenario 1.10: Inconsistent ETag and X-Harmonia-Authoritative-Version headers fail to OutcomeUnknown")
        void readInconsistentVersionHeadersFailsToOutcomeUnknown() {
            ResourceKey key = ResourceKey.of("Practitioner", "pr-100");
            Practitioner expectedResource = samplePractitioner("pr-100", "Smith", "John");
            String resourceJson = fhirContext.newJsonParser().encodeResourceToString(expectedResource);

            stubFor(get(urlEqualTo("/api/authoritative/fhir/Practitioner/pr-100"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader("ETag", "W/\"3\"")
                            .withHeader("X-Harmonia-Authoritative-Version", "4") // Disagrees with ETag!
                            .withBody(resourceJson)));

            AuthoritativePersistenceResult<IBaseResource> result = client.read(key);

            assertThat(result).isInstanceOf(AuthoritativePersistenceResult.OutcomeUnknown.class);
            assertThat(((AuthoritativePersistenceResult.OutcomeUnknown<IBaseResource>) result).message())
                    .containsIgnoringCase("Inconsistent authoritative version headers");
        }

        @Test
        @DisplayName("Scenario 1.11: Local validation error returns NotCommitted")
        void readNullKeyReturnsNotCommitted() {
            AuthoritativePersistenceResult<IBaseResource> result = client.read(null);
            assertThat(result).isInstanceOf(AuthoritativePersistenceResult.NotCommitted.class);
        }
    }

    // =========================================================================
    // 2. Authoritative CREATE Scenarios
    // =========================================================================
    @Nested
    @DisplayName("Authoritative CREATE Scenarios")
    class CreateScenarios {

        @Test
        @DisplayName("Scenario 2.1: 201 Created with valid ETag returns Committed and sends If-None-Match: *")
        void create201CreatedReturnsCommitted() {
            ResourceKey key = ResourceKey.of("Practitioner", "pr-200");
            Practitioner proposed = samplePractitioner("pr-200", "Taylor", "Alice");
            String resourceJson = fhirContext.newJsonParser().encodeResourceToString(proposed);

            stubFor(put(urlEqualTo("/api/authoritative/fhir/Practitioner/pr-200"))
                    .withHeader("If-None-Match", equalTo("*"))
                    .withHeader("Content-Type", containing("application/fhir+json"))
                    .willReturn(aResponse()
                            .withStatus(201)
                            .withHeader("ETag", "W/\"1\"")
                            .withBody(resourceJson)));

            AuthoritativePersistenceResult<IBaseResource> result = client.create(key, proposed);

            assertThat(result).isInstanceOf(AuthoritativePersistenceResult.Committed.class);
            AuthoritativePersistenceResult.Committed<IBaseResource> committed =
                    (AuthoritativePersistenceResult.Committed<IBaseResource>) result;
            assertThat(committed.authoritativeVersion()).isEqualTo(AuthoritativeVersion.of("1"));

            verify(putRequestedFor(urlEqualTo("/api/authoritative/fhir/Practitioner/pr-200"))
                    .withHeader("If-None-Match", equalTo("*")));
        }

        @Test
        @DisplayName("Scenario 2.2: 412 Precondition Failed returns Conflict (Resource Already Exists)")
        void create412ReturnsConflict() {
            ResourceKey key = ResourceKey.of("Practitioner", "pr-existing");
            Practitioner proposed = samplePractitioner("pr-existing", "Taylor", "Alice");

            stubFor(put(urlEqualTo("/api/authoritative/fhir/Practitioner/pr-existing"))
                    .willReturn(aResponse()
                            .withStatus(412)
                            .withHeader("ETag", "W/\"2\"")));

            AuthoritativePersistenceResult<IBaseResource> result = client.create(key, proposed);

            assertThat(result).isInstanceOf(AuthoritativePersistenceResult.Conflict.class);
            AuthoritativePersistenceResult.Conflict<IBaseResource> conflict =
                    (AuthoritativePersistenceResult.Conflict<IBaseResource>) result;
            assertThat(conflict.conflict().reason()).isEqualTo(PreconditionFailureReason.RESOURCE_ALREADY_EXISTS);
            assertThat(conflict.conflict().currentVersion()).isEqualTo(AuthoritativeVersion.of("2"));
        }

        @Test
        @DisplayName("Scenario 2.3: 409 Conflict returns Conflict (Resource Already Exists)")
        void create409ReturnsConflict() {
            ResourceKey key = ResourceKey.of("Practitioner", "pr-existing");
            Practitioner proposed = samplePractitioner("pr-existing", "Taylor", "Alice");

            stubFor(put(urlEqualTo("/api/authoritative/fhir/Practitioner/pr-existing"))
                    .willReturn(aResponse().withStatus(409)));

            AuthoritativePersistenceResult<IBaseResource> result = client.create(key, proposed);

            assertThat(result).isInstanceOf(AuthoritativePersistenceResult.Conflict.class);
            assertThat(((AuthoritativePersistenceResult.Conflict<IBaseResource>) result).conflict().reason())
                    .isEqualTo(PreconditionFailureReason.RESOURCE_ALREADY_EXISTS);
        }

        @Test
        @DisplayName("Scenario 2.4: 400 Bad Request / 422 Unprocessable returns NotCommitted")
        void create400ReturnsNotCommitted() {
            ResourceKey key = ResourceKey.of("Practitioner", "pr-bad");
            Practitioner proposed = samplePractitioner("pr-bad", "Taylor", "Alice");

            stubFor(put(urlEqualTo("/api/authoritative/fhir/Practitioner/pr-bad"))
                    .willReturn(aResponse().withStatus(400)));

            AuthoritativePersistenceResult<IBaseResource> result = client.create(key, proposed);

            assertThat(result).isInstanceOf(AuthoritativePersistenceResult.NotCommitted.class);
        }

        @Test
        @DisplayName("Scenario 2.5: 201 Created with missing ETag returns OutcomeUnknown")
        void create201WithMissingETagReturnsOutcomeUnknown() {
            ResourceKey key = ResourceKey.of("Practitioner", "pr-200");
            Practitioner proposed = samplePractitioner("pr-200", "Taylor", "Alice");

            stubFor(put(urlEqualTo("/api/authoritative/fhir/Practitioner/pr-200"))
                    .willReturn(aResponse().withStatus(201)));

            AuthoritativePersistenceResult<IBaseResource> result = client.create(key, proposed);

            assertThat(result).isInstanceOf(AuthoritativePersistenceResult.OutcomeUnknown.class);
        }

        @Test
        @DisplayName("Scenario 2.6: Local argument validation returns NotCommitted")
        void createNullKeyReturnsNotCommitted() {
            Practitioner proposed = samplePractitioner("pr-200", "Taylor", "Alice");
            AuthoritativePersistenceResult<IBaseResource> result = client.create(null, proposed);
            assertThat(result).isInstanceOf(AuthoritativePersistenceResult.NotCommitted.class);
        }
    }

    // =========================================================================
    // 3. Authoritative UPDATE Scenarios
    // =========================================================================
    @Nested
    @DisplayName("Authoritative UPDATE Scenarios")
    class UpdateScenarios {

        @Test
        @DisplayName("Scenario 3.1: 200 OK Update returns Committed and transmits If-Match: W/\"1\"")
        void update200OkReturnsCommitted() {
            ResourceKey key = ResourceKey.of("Practitioner", "pr-300");
            Practitioner proposed = samplePractitioner("pr-300", "Smith", "Jane");
            String resourceJson = fhirContext.newJsonParser().encodeResourceToString(proposed);
            ExpectedAuthoritativeVersion expectedVersion = ExpectedAuthoritativeVersion.of("1");

            stubFor(put(urlEqualTo("/api/authoritative/fhir/Practitioner/pr-300"))
                    .withHeader("If-Match", equalTo("W/\"1\""))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader("ETag", "W/\"2\"")
                            .withBody(resourceJson)));

            AuthoritativePersistenceResult<IBaseResource> result = client.update(key, proposed, expectedVersion);

            assertThat(result).isInstanceOf(AuthoritativePersistenceResult.Committed.class);
            AuthoritativePersistenceResult.Committed<IBaseResource> committed =
                    (AuthoritativePersistenceResult.Committed<IBaseResource>) result;
            assertThat(committed.authoritativeVersion()).isEqualTo(AuthoritativeVersion.of("2"));

            verify(putRequestedFor(urlEqualTo("/api/authoritative/fhir/Practitioner/pr-300"))
                    .withHeader("If-Match", equalTo("W/\"1\"")));
        }

        @Test
        @DisplayName("Scenario 3.2: 412 Precondition Failed returns Conflict (EXPECTED_VERSION_MISMATCH)")
        void update412ReturnsConflict() {
            ResourceKey key = ResourceKey.of("Practitioner", "pr-300");
            Practitioner proposed = samplePractitioner("pr-300", "Smith", "Jane");
            ExpectedAuthoritativeVersion expectedVersion = ExpectedAuthoritativeVersion.of("1");

            stubFor(put(urlEqualTo("/api/authoritative/fhir/Practitioner/pr-300"))
                    .willReturn(aResponse()
                            .withStatus(412)
                            .withHeader("ETag", "W/\"3\"")));

            AuthoritativePersistenceResult<IBaseResource> result = client.update(key, proposed, expectedVersion);

            assertThat(result).isInstanceOf(AuthoritativePersistenceResult.Conflict.class);
            AuthoritativePersistenceResult.Conflict<IBaseResource> conflict =
                    (AuthoritativePersistenceResult.Conflict<IBaseResource>) result;
            assertThat(conflict.conflict().reason()).isEqualTo(PreconditionFailureReason.EXPECTED_VERSION_MISMATCH);
            assertThat(conflict.conflict().expectedVersion()).isEqualTo(expectedVersion);
            assertThat(conflict.conflict().currentVersion()).isEqualTo(AuthoritativeVersion.of("3"));
        }

        @Test
        @DisplayName("Scenario 3.3: 428 Precondition Required returns Conflict")
        void update428ReturnsConflict() {
            ResourceKey key = ResourceKey.of("Practitioner", "pr-300");
            Practitioner proposed = samplePractitioner("pr-300", "Smith", "Jane");
            ExpectedAuthoritativeVersion expectedVersion = ExpectedAuthoritativeVersion.of("1");

            stubFor(put(urlEqualTo("/api/authoritative/fhir/Practitioner/pr-300"))
                    .willReturn(aResponse().withStatus(428)));

            AuthoritativePersistenceResult<IBaseResource> result = client.update(key, proposed, expectedVersion);

            assertThat(result).isInstanceOf(AuthoritativePersistenceResult.Conflict.class);
        }

        @Test
        @DisplayName("Scenario 3.4: 404 Not Found on update returns NotCommitted (target absent)")
        void update404ReturnsNotCommitted() {
            ResourceKey key = ResourceKey.of("Practitioner", "pr-300");
            Practitioner proposed = samplePractitioner("pr-300", "Smith", "Jane");
            ExpectedAuthoritativeVersion expectedVersion = ExpectedAuthoritativeVersion.of("1");

            stubFor(put(urlEqualTo("/api/authoritative/fhir/Practitioner/pr-300"))
                    .willReturn(aResponse().withStatus(404)));

            AuthoritativePersistenceResult<IBaseResource> result = client.update(key, proposed, expectedVersion);

            assertThat(result).isInstanceOf(AuthoritativePersistenceResult.NotCommitted.class);
            AuthoritativePersistenceResult.NotCommitted<IBaseResource> notCommitted =
                    (AuthoritativePersistenceResult.NotCommitted<IBaseResource>) result;
            assertThat(notCommitted.failureMessage()).contains("HTTP 404");
        }

        @Test
        @DisplayName("Scenario 3.5: UPDATE with ExpectedAuthoritativeVersion.none() is rejected locally as Conflict")
        void updateNoneVersionRejectedLocally() {
            ResourceKey key = ResourceKey.of("Practitioner", "pr-300");
            Practitioner proposed = samplePractitioner("pr-300", "Smith", "Jane");

            AuthoritativePersistenceResult<IBaseResource> result =
                    client.update(key, proposed, ExpectedAuthoritativeVersion.none());

            assertThat(result).isInstanceOf(AuthoritativePersistenceResult.Conflict.class);
            // Verify no network request was made
            verify(0, putRequestedFor(anyUrl()));
        }
    }

    // =========================================================================
    // 4. Transport Failure & Uncertainty Scenarios
    // =========================================================================
    @Nested
    @DisplayName("Transport Failure & Uncertainty Scenarios")
    class TransportFailureScenarios {

        @Test
        @DisplayName("Scenario 4.1: DNS resolution failure on unresolvable host returns NotCommitted")
        void unknownHostReturnsNotCommitted() {
            MnemeAuthoritativeClientConfig invalidHostConfig = MnemeAuthoritativeClientConfig.of(
                    "http://unresolvable.invalid-host-name-harmonia.test:8080/api/authoritative/fhir",
                    Duration.ofSeconds(1),
                    Duration.ofSeconds(1)
            );
            MnemeAuthoritativeHttpClient badHostClient = new MnemeAuthoritativeHttpClient(invalidHostConfig, fhirContext);

            ResourceKey key = ResourceKey.of("Practitioner", "pr-100");
            AuthoritativePersistenceResult<IBaseResource> result = badHostClient.read(key);

            assertThat(result).isInstanceOf(AuthoritativePersistenceResult.NotCommitted.class);
            AuthoritativePersistenceResult.NotCommitted<IBaseResource> notCommitted =
                    (AuthoritativePersistenceResult.NotCommitted<IBaseResource>) result;
            assertThat(notCommitted.failureMessage()).containsIgnoringCase("DNS resolution failed");
        }

        @Test
        @DisplayName("Scenario 4.2: Response timeout maps conservatively to OutcomeUnknown")
        void requestTimeoutReturnsOutcomeUnknown() {
            ResourceKey key = ResourceKey.of("Practitioner", "pr-timeout");
            Practitioner proposed = samplePractitioner("pr-timeout", "Delay", "Test");

            stubFor(put(urlEqualTo("/api/authoritative/fhir/Practitioner/pr-timeout"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withFixedDelay(3000) // Exceeds 2s request timeout
                            .withHeader("ETag", "W/\"1\"")));

            AuthoritativePersistenceResult<IBaseResource> result =
                    client.create(key, proposed);

            assertThat(result).isInstanceOf(AuthoritativePersistenceResult.OutcomeUnknown.class);
            AuthoritativePersistenceResult.OutcomeUnknown<IBaseResource> unknown =
                    (AuthoritativePersistenceResult.OutcomeUnknown<IBaseResource>) result;
            assertThat(unknown.outcome()).isEqualTo(AuthoritativeCommitOutcome.UNKNOWN);
        }

        @Test
        @DisplayName("Scenario 4.3: Mid-stream socket reset / fault injection maps to OutcomeUnknown")
        void socketResetReturnsOutcomeUnknown() {
            ResourceKey key = ResourceKey.of("Practitioner", "pr-reset");
            Practitioner proposed = samplePractitioner("pr-reset", "Reset", "Test");

            stubFor(put(urlEqualTo("/api/authoritative/fhir/Practitioner/pr-reset"))
                    .willReturn(aResponse()
                            .withFault(Fault.CONNECTION_RESET_BY_PEER)));

            AuthoritativePersistenceResult<IBaseResource> result = client.create(key, proposed);

            assertThat(result).isInstanceOf(AuthoritativePersistenceResult.OutcomeUnknown.class);
        }

        @Test
        @DisplayName("Scenario 4.4: Mutating requests (CREATE, UPDATE) enforce zero transparent retries")
        void mutatingRequestsEnforceZeroRetries() {
            ResourceKey key = ResourceKey.of("Practitioner", "pr-no-retry");
            Practitioner proposed = samplePractitioner("pr-no-retry", "NoRetry", "Test");

            stubFor(put(urlEqualTo("/api/authoritative/fhir/Practitioner/pr-no-retry"))
                    .willReturn(aResponse().withStatus(500)));

            AuthoritativePersistenceResult<IBaseResource> result = client.create(key, proposed);

            assertThat(result).isInstanceOf(AuthoritativePersistenceResult.OutcomeUnknown.class);
            // Verify exactly 1 request was transmitted - zero retries!
            verify(1, putRequestedFor(urlEqualTo("/api/authoritative/fhir/Practitioner/pr-no-retry")));
        }
    }

    // =========================================================================
    // 5. Architectural Guardrails
    // =========================================================================
    @Nested
    @DisplayName("Architectural Guardrail Scenarios")
    class ArchitecturalGuardrails {

        @Test
        @DisplayName("AuthoritativePersistencePort must not declare physical DELETE or REMOVE methods")
        void portHasNoDeleteMethods() {
            Method[] methods = AuthoritativePersistencePort.class.getDeclaredMethods();
            for (Method m : methods) {
                assertThat(m.getName().toLowerCase()).doesNotContain("delete");
                assertThat(m.getName().toLowerCase()).doesNotContain("remove");
                assertThat(m.getName().toLowerCase()).doesNotContain("purge");
                assertThat(m.getName().toLowerCase()).doesNotContain("search");
            }
        }

        @Test
        @DisplayName("MnemeAuthoritativeHttpClient must not declare physical DELETE or REMOVE methods")
        void clientHasNoDeleteMethods() {
            Method[] methods = MnemeAuthoritativeHttpClient.class.getDeclaredMethods();
            for (Method m : methods) {
                assertThat(m.getName().toLowerCase()).doesNotContain("delete");
                assertThat(m.getName().toLowerCase()).doesNotContain("remove");
                assertThat(m.getName().toLowerCase()).doesNotContain("purge");
                assertThat(m.getName().toLowerCase()).doesNotContain("search");
            }
        }
    }
}
