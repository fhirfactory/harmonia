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
import ca.uhn.fhir.parser.IParser;
import net.fhirfactory.harmonia.hapifhir.persistence.AuthoritativePersistencePort;
import net.fhirfactory.harmonia.hapifhir.persistence.model.AuthoritativePersistenceResult;
import net.fhirfactory.harmonia.model.governedwrite.AuthoritativePreconditionConflict;
import net.fhirfactory.harmonia.model.governedwrite.AuthoritativeVersion;
import net.fhirfactory.harmonia.model.governedwrite.ExpectedAuthoritativeVersion;
import net.fhirfactory.harmonia.model.governedwrite.ResourceKey;
import net.fhirfactory.harmonia.persistence.config.MnemeAuthoritativeClientConfig;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;

/**
 * Production HTTP client implementing {@link AuthoritativePersistencePort} for Mneme.
 * <p>
 * Connects synchronously to Mnemosyne's dedicated internal authoritative HTTP API boundary
 * ({@code /api/authoritative/fhir/*}) across process and container network boundaries.
 * <p>
 * Enforces:
 * <ul>
 *   <li>Point operations only: {@code READ}, {@code CREATE-if-absent}, {@code UPDATE-if-expected-predecessor}.</li>
 *   <li>Zero physical DELETE or search operations (ADR-020, Invariant 8).</li>
 *   <li>Zero transparent retries on mutating requests.</li>
 *   <li>Conservative transport failure classification (fail to {@code OutcomeUnknown} unless unequivocally pre-network).</li>
 *   <li>Fail-closed security on 401/403 responses.</li>
 *   <li>Strict domain separation between HTTP ETag and {@link AuthoritativeVersion} without fallbacks to {@code meta.versionId} or cache tokens.</li>
 * </ul>
 */
public class MnemeAuthoritativeHttpClient implements AuthoritativePersistencePort<IBaseResource> {

    private static final Logger log = LoggerFactory.getLogger(MnemeAuthoritativeHttpClient.class);

    public static final String HEADER_ETAG = "ETag";
    public static final String HEADER_IF_NONE_MATCH = "If-None-Match";
    public static final String HEADER_IF_MATCH = "If-Match";
    public static final String HEADER_ACCEPT = "Accept";
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_X_AUTHORITATIVE_VERSION = "X-Harmonia-Authoritative-Version";

    public static final String MIME_FHIR_JSON = "application/fhir+json; charset=UTF-8";
    public static final String ACCEPT_VALUE = "application/fhir+json, application/json";

    private final MnemeAuthoritativeClientConfig config;
    private final FhirContext fhirContext;
    private final HttpClient httpClient;

    public MnemeAuthoritativeHttpClient() {
        this(MnemeAuthoritativeClientConfig.fromEnvironment(), FhirContext.forR5());
    }

    public MnemeAuthoritativeHttpClient(MnemeAuthoritativeClientConfig config) {
        this(config, FhirContext.forR5());
    }

    public MnemeAuthoritativeHttpClient(MnemeAuthoritativeClientConfig config, FhirContext fhirContext) {
        this(config, fhirContext, createDefaultHttpClient(config));
    }

    private static HttpClient createDefaultHttpClient(MnemeAuthoritativeClientConfig config) {
        HttpClient.Builder builder = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(config.connectTimeout());
        if (config.isTlsConfigured()) {
            try {
                javax.net.ssl.SSLContext sslContext = SslContextFactory.createSslContext(
                        config.keyStorePath(),
                        config.keyStorePassword(),
                        config.keyStoreType(),
                        config.trustStorePath(),
                        config.trustStorePassword(),
                        config.trustStoreType()
                );
                builder.sslContext(sslContext);
            } catch (Exception e) {
                log.error("Failed to initialize SSLContext for MnemeAuthoritativeHttpClient: {}", e.getMessage(), e);
                throw new IllegalStateException("Failed to initialize TLS SSLContext for authoritative HTTP client", e);
            }
        }
        return builder.build();
    }

    public MnemeAuthoritativeHttpClient(
            MnemeAuthoritativeClientConfig config,
            FhirContext fhirContext,
            HttpClient httpClient) {
        this.config = Objects.requireNonNull(config, "config must not be null");
        this.fhirContext = Objects.requireNonNull(fhirContext, "fhirContext must not be null");
        this.httpClient = Objects.requireNonNull(httpClient, "httpClient must not be null");
    }

    public MnemeAuthoritativeClientConfig getConfig() {
        return config;
    }

    @Override
    public AuthoritativePersistenceResult<IBaseResource> read(ResourceKey key) {
        if (key == null || key.resourceType() == null || key.resourceType().isBlank()
                || key.id() == null || key.id().isBlank()) {
            return new AuthoritativePersistenceResult.NotCommitted<>(
                    "Invalid ResourceKey: key and components must be non-null and non-blank");
        }

        try {
            String uri = buildResourceUri(key);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .header(HEADER_ACCEPT, ACCEPT_VALUE)
                    .timeout(config.requestTimeout())
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            int statusCode = response.statusCode();

            if (statusCode == 200) {
                VersionResolution versionRes = extractAuthoritativeVersion(response);
                if (versionRes.isFailure()) {
                    return versionRes.failure();
                }

                String responseBody = response.body();
                if (responseBody == null || responseBody.isBlank()) {
                    return new AuthoritativePersistenceResult.OutcomeUnknown<>(
                            "Empty response body received on HTTP 200 for READ " + key);
                }

                IParser parser = fhirContext.newJsonParser();
                IBaseResource resource = parser.parseResource(responseBody);
                return new AuthoritativePersistenceResult.Committed<>(resource, versionRes.version());
            }

            if (statusCode == 404 || statusCode == 410) {
                return new AuthoritativePersistenceResult.NotCommitted<>(
                        "Resource not found on authoritative server: " + key + " (HTTP " + statusCode + ")");
            }

            if (statusCode == 400) {
                return new AuthoritativePersistenceResult.NotCommitted<>(
                        "Malformed request for READ " + key + ": HTTP 400");
            }

            if (statusCode == 401 || statusCode == 403) {
                return new AuthoritativePersistenceResult.NotCommitted<>(
                        "Authorization denied by Mnemosyne for READ (fail-closed): HTTP " + statusCode);
            }

            return new AuthoritativePersistenceResult.OutcomeUnknown<>(
                    "Unexpected response status from Mnemosyne for READ " + key + ": HTTP " + statusCode);

        } catch (Throwable t) {
            log.warn("Transport failure during authoritative READ of {}: {}", key, t.getMessage());
            return HttpTransportFailureClassifier.classify(t, "READ " + key);
        }
    }

    @Override
    public AuthoritativePersistenceResult<IBaseResource> create(ResourceKey key, IBaseResource proposedState) {
        if (key == null || key.resourceType() == null || key.resourceType().isBlank()
                || key.id() == null || key.id().isBlank() || proposedState == null) {
            return new AuthoritativePersistenceResult.NotCommitted<>(
                    "Invalid arguments for CREATE: key and proposedState must be non-null");
        }

        try {
            String uri = buildResourceUri(key);
            IParser parser = fhirContext.newJsonParser();
            String payload = parser.encodeResourceToString(proposedState);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .header(HEADER_CONTENT_TYPE, MIME_FHIR_JSON)
                    .header(HEADER_ACCEPT, ACCEPT_VALUE)
                    .header(HEADER_IF_NONE_MATCH, "*")
                    .timeout(config.requestTimeout())
                    .PUT(HttpRequest.BodyPublishers.ofString(payload, StandardCharsets.UTF_8))
                    .build();

            // Direct execution - zero transparent retries on mutating operation
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            int statusCode = response.statusCode();

            if (statusCode == 201 || statusCode == 200) {
                VersionResolution versionRes = extractAuthoritativeVersion(response);
                if (versionRes.isFailure()) {
                    return versionRes.failure();
                }

                String responseBody = response.body();
                IBaseResource persisted = proposedState;
                if (responseBody != null && !responseBody.isBlank()) {
                    try {
                        persisted = parser.parseResource(responseBody);
                    } catch (Exception e) {
                        log.debug("Using proposedState since response body could not be parsed: {}", e.getMessage());
                    }
                }
                return new AuthoritativePersistenceResult.Committed<>(persisted, versionRes.version());
            }

            if (statusCode == 412 || statusCode == 409) {
                AuthoritativeVersion currentVersion = tryParseVersionOnly(response).orElse(null);
                return new AuthoritativePersistenceResult.Conflict<>(
                        AuthoritativePreconditionConflict.resourceAlreadyExists(key, currentVersion));
            }

            if (statusCode == 400 || statusCode == 422) {
                return new AuthoritativePersistenceResult.NotCommitted<>(
                        "Malformed payload / validation failed during CREATE for " + key + ": HTTP " + statusCode);
            }

            if (statusCode == 401 || statusCode == 403) {
                return new AuthoritativePersistenceResult.NotCommitted<>(
                        "Security context rejected by Mnemosyne during CREATE (fail-closed): HTTP " + statusCode);
            }

            return new AuthoritativePersistenceResult.OutcomeUnknown<>(
                    "Unexpected response status from Mnemosyne for CREATE " + key + ": HTTP " + statusCode);

        } catch (Throwable t) {
            log.warn("Transport failure during authoritative CREATE of {}: {}", key, t.getMessage());
            return HttpTransportFailureClassifier.classify(t, "CREATE " + key);
        }
    }

    @Override
    public AuthoritativePersistenceResult<IBaseResource> update(
            ResourceKey key,
            IBaseResource proposedState,
            ExpectedAuthoritativeVersion expectedVersion) {
        if (key == null || key.resourceType() == null || key.resourceType().isBlank()
                || key.id() == null || key.id().isBlank() || proposedState == null || expectedVersion == null) {
            return new AuthoritativePersistenceResult.NotCommitted<>(
                    "Invalid arguments for UPDATE: key, proposedState, and expectedVersion must be non-null");
        }

        if (expectedVersion.isNone()) {
            return new AuthoritativePersistenceResult.Conflict<>(
                    new AuthoritativePreconditionConflict(
                            key,
                            net.fhirfactory.harmonia.model.governedwrite.PreconditionFailureReason.EXPECTED_VERSION_MISMATCH,
                            expectedVersion,
                            null,
                            "Expected version must not be NONE for UPDATE"));
        }

        try {
            String uri = buildResourceUri(key);
            IParser parser = fhirContext.newJsonParser();
            String payload = parser.encodeResourceToString(proposedState);

            String ifMatchHeader = "W/\"" + expectedVersion.value().orElse("") + "\"";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .header(HEADER_CONTENT_TYPE, MIME_FHIR_JSON)
                    .header(HEADER_ACCEPT, ACCEPT_VALUE)
                    .header(HEADER_IF_MATCH, ifMatchHeader)
                    .timeout(config.requestTimeout())
                    .PUT(HttpRequest.BodyPublishers.ofString(payload, StandardCharsets.UTF_8))
                    .build();

            // Direct execution - zero transparent retries on mutating operation
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            int statusCode = response.statusCode();

            if (statusCode == 200 || statusCode == 201) {
                VersionResolution versionRes = extractAuthoritativeVersion(response);
                if (versionRes.isFailure()) {
                    return versionRes.failure();
                }

                String responseBody = response.body();
                IBaseResource persisted = proposedState;
                if (responseBody != null && !responseBody.isBlank()) {
                    try {
                        persisted = parser.parseResource(responseBody);
                    } catch (Exception e) {
                        log.debug("Using proposedState since response body could not be parsed: {}", e.getMessage());
                    }
                }
                return new AuthoritativePersistenceResult.Committed<>(persisted, versionRes.version());
            }

            if (statusCode == 412 || statusCode == 409) {
                AuthoritativeVersion actualVersion = tryParseVersionOnly(response).orElse(null);
                return new AuthoritativePersistenceResult.Conflict<>(
                        AuthoritativePreconditionConflict.expectedVersionMismatch(
                                key, expectedVersion, actualVersion));
            }

            if (statusCode == 428) {
                return new AuthoritativePersistenceResult.Conflict<>(
                        new AuthoritativePreconditionConflict(
                                key,
                                net.fhirfactory.harmonia.model.governedwrite.PreconditionFailureReason.EXPECTED_VERSION_MISMATCH,
                                expectedVersion,
                                null,
                                "Precondition required by server (HTTP 428)"));
            }

            if (statusCode == 404) {
                return new AuthoritativePersistenceResult.NotCommitted<>(
                        "Target resource does not exist on authoritative server for UPDATE: " + key + " (HTTP 404)");
            }

            if (statusCode == 400 || statusCode == 422) {
                return new AuthoritativePersistenceResult.NotCommitted<>(
                        "Malformed payload / validation failed during UPDATE for " + key + ": HTTP " + statusCode);
            }

            if (statusCode == 401 || statusCode == 403) {
                return new AuthoritativePersistenceResult.NotCommitted<>(
                        "Security context rejected by Mnemosyne during UPDATE (fail-closed): HTTP " + statusCode);
            }

            return new AuthoritativePersistenceResult.OutcomeUnknown<>(
                    "Unexpected response status from Mnemosyne for UPDATE " + key + ": HTTP " + statusCode);

        } catch (Throwable t) {
            log.warn("Transport failure during authoritative UPDATE of {}: {}", key, t.getMessage());
            return HttpTransportFailureClassifier.classify(t, "UPDATE " + key);
        }
    }

    private String buildResourceUri(ResourceKey key) {
        return config.baseUrl() + "/" + key.resourceType() + "/" + key.id();
    }

    private VersionResolution extractAuthoritativeVersion(HttpResponse<?> response) {
        Optional<String> etagOpt = response.headers().firstValue(HEADER_ETAG);
        if (etagOpt.isEmpty() || etagOpt.get().isBlank()) {
            return VersionResolution.failure(new AuthoritativePersistenceResult.OutcomeUnknown<>(
                    "Missing authoritative ETag header in server response"));
        }

        String rawEtag = etagOpt.get().trim();
        String etagValue = cleanEtag(rawEtag);
        if (etagValue.isBlank()) {
            return VersionResolution.failure(new AuthoritativePersistenceResult.OutcomeUnknown<>(
                    "Malformed authoritative ETag header in server response: " + rawEtag));
        }

        AuthoritativeVersion etagVersion = AuthoritativeVersion.of(etagValue);

        // Check diagnostic header consistency if present
        Optional<String> xVersionOpt = response.headers().firstValue(HEADER_X_AUTHORITATIVE_VERSION);
        if (xVersionOpt.isPresent() && !xVersionOpt.get().isBlank()) {
            String xVersion = cleanEtag(xVersionOpt.get().trim());
            if (!xVersion.equals(etagVersion.value())) {
                return VersionResolution.failure(new AuthoritativePersistenceResult.OutcomeUnknown<>(
                        "Inconsistent authoritative version headers: ETag is " + etagVersion.value()
                                + " but X-Harmonia-Authoritative-Version is " + xVersion));
            }
        }

        return VersionResolution.success(etagVersion);
    }

    private Optional<AuthoritativeVersion> tryParseVersionOnly(HttpResponse<?> response) {
        Optional<String> etagOpt = response.headers().firstValue(HEADER_ETAG);
        if (etagOpt.isPresent() && !etagOpt.get().isBlank()) {
            String val = cleanEtag(etagOpt.get().trim());
            if (!val.isBlank()) {
                return Optional.of(AuthoritativeVersion.of(val));
            }
        }
        Optional<String> xOpt = response.headers().firstValue(HEADER_X_AUTHORITATIVE_VERSION);
        if (xOpt.isPresent() && !xOpt.get().isBlank()) {
            String val = cleanEtag(xOpt.get().trim());
            if (!val.isBlank()) {
                return Optional.of(AuthoritativeVersion.of(val));
            }
        }
        return Optional.empty();
    }

    private String cleanEtag(String raw) {
        String val = raw;
        if (val.startsWith("W/") || val.startsWith("w/")) {
            val = val.substring(2);
        }
        if (val.startsWith("\"") && val.endsWith("\"") && val.length() >= 2) {
            val = val.substring(1, val.length() - 1);
        }
        return val.trim();
    }

    private record VersionResolution(
            AuthoritativeVersion version,
            AuthoritativePersistenceResult<IBaseResource> failure
    ) {
        static VersionResolution success(AuthoritativeVersion version) {
            return new VersionResolution(version, null);
        }

        static VersionResolution failure(AuthoritativePersistenceResult<IBaseResource> failure) {
            return new VersionResolution(null, failure);
        }

        boolean isFailure() {
            return failure != null;
        }
    }
}
