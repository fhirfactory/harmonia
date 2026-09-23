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

package net.fhirfactory.harmonia.befe.config;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Comprehensive unit tests for {@link BefeCorsConfig} and {@link CorsFilter}.
 * <p>
 * Verifies trusted origin allow-listing, untrusted origin rejection, non-CORS pass-through,
 * preflight OPTIONS short-circuiting, non-reflection, fail-closed default behavior,
 * and the strict absence of wildcard origins and credentials.
 * </p>
 */
class BefeCorsFilterTest {

    private CorsFilter filter;
    private ContainerRequestContext requestContext;
    private ContainerResponseContext responseContext;
    private MultivaluedMap<String, Object> responseHeaders;

    @BeforeEach
    void setUp() {
        filter = new CorsFilter();
        requestContext = mock(ContainerRequestContext.class);
        responseContext = mock(ContainerResponseContext.class);
        responseHeaders = new MultivaluedHashMap<>();
        when(responseContext.getHeaders()).thenReturn(responseHeaders);

        BefeCorsConfig.resetAllowedOriginsForTesting();
        System.clearProperty(BefeCorsConfig.PROPERTY_ALLOWED_ORIGINS);
    }

    @AfterEach
    void tearDown() {
        BefeCorsConfig.resetAllowedOriginsForTesting();
        System.clearProperty(BefeCorsConfig.PROPERTY_ALLOWED_ORIGINS);
    }

    // =========================================================================
    // 1. BefeCorsConfig Tests
    // =========================================================================

    @Test
    @DisplayName("BefeCorsConfig: Empty or absent configuration fails closed")
    void testEmptyConfigFailsClosed() {
        Set<String> origins = BefeCorsConfig.getAllowedOrigins();
        assertThat(origins).isEmpty();

        assertThat(BefeCorsConfig.isOriginAllowed("https://clinical.harmonia.local")).isFalse();
        assertThat(BefeCorsConfig.isOriginAllowed("http://localhost:8080")).isFalse();
        assertThat(BefeCorsConfig.isOriginAllowed("https://attacker.example")).isFalse();
        assertThat(BefeCorsConfig.isOriginAllowed(null)).isFalse();
        assertThat(BefeCorsConfig.isOriginAllowed("")).isFalse();
        assertThat(BefeCorsConfig.isOriginAllowed("   ")).isFalse();
    }

    @Test
    @DisplayName("BefeCorsConfig: System property configuration loads allowed origins")
    void testSystemPropertyConfiguration() {
        System.setProperty(BefeCorsConfig.PROPERTY_ALLOWED_ORIGINS, "https://clinical.harmonia.local");

        assertThat(BefeCorsConfig.getAllowedOrigins()).containsExactly("https://clinical.harmonia.local");
        assertThat(BefeCorsConfig.isOriginAllowed("https://clinical.harmonia.local")).isTrue();
        assertThat(BefeCorsConfig.isOriginAllowed("https://attacker.example")).isFalse();
    }

    @Test
    @DisplayName("BefeCorsConfig: In-memory test override takes precedence and resets cleanly")
    void testTestOverrideLifecycle() {
        System.setProperty(BefeCorsConfig.PROPERTY_ALLOWED_ORIGINS, "https://sysprop.harmonia.local");
        assertThat(BefeCorsConfig.isOriginAllowed("https://sysprop.harmonia.local")).isTrue();

        BefeCorsConfig.setAllowedOriginsOverrideForTesting("https://override.harmonia.local");
        assertThat(BefeCorsConfig.isOriginAllowed("https://override.harmonia.local")).isTrue();
        assertThat(BefeCorsConfig.isOriginAllowed("https://sysprop.harmonia.local")).isFalse();

        BefeCorsConfig.resetAllowedOriginsForTesting();
        assertThat(BefeCorsConfig.isOriginAllowed("https://sysprop.harmonia.local")).isTrue();
        assertThat(BefeCorsConfig.isOriginAllowed("https://override.harmonia.local")).isFalse();
    }

    @Test
    @DisplayName("BefeCorsConfig: Multiple comma-separated origins are parsed and normalized")
    void testMultipleOriginsParsedAndNormalized() {
        BefeCorsConfig.setAllowedOriginsOverrideForTesting(
                "https://clinical.harmonia.local, https://console.harmonia.local, https://admin.harmonia.local:8443, http://localhost:3000"
        );

        Set<String> allowed = BefeCorsConfig.getAllowedOrigins();
        assertThat(allowed).containsExactlyInAnyOrder(
                "https://clinical.harmonia.local",
                "https://console.harmonia.local",
                "https://admin.harmonia.local:8443",
                "http://localhost:3000"
        );

        assertThat(BefeCorsConfig.isOriginAllowed("https://clinical.harmonia.local")).isTrue();
        assertThat(BefeCorsConfig.isOriginAllowed("https://console.harmonia.local")).isTrue();
        assertThat(BefeCorsConfig.isOriginAllowed("https://admin.harmonia.local:8443")).isTrue();
        assertThat(BefeCorsConfig.isOriginAllowed("http://localhost:3000")).isTrue();
        assertThat(BefeCorsConfig.isOriginAllowed("https://attacker.example")).isFalse();
    }

    @Test
    @DisplayName("BefeCorsConfig: Origin normalization handles case, default ports, and trailing slashes")
    void testOriginNormalization() {
        // Case-insensitivity
        assertThat(BefeCorsConfig.normalizeOrigin("HTTPS://CLINICAL.HARMONIA.LOCAL"))
                .isEqualTo("https://clinical.harmonia.local");

        // Default port stripping
        assertThat(BefeCorsConfig.normalizeOrigin("https://clinical.harmonia.local:443"))
                .isEqualTo("https://clinical.harmonia.local");
        assertThat(BefeCorsConfig.normalizeOrigin("http://clinical.harmonia.local:80"))
                .isEqualTo("http://clinical.harmonia.local");

        // Non-default port preserved
        assertThat(BefeCorsConfig.normalizeOrigin("https://clinical.harmonia.local:8443"))
                .isEqualTo("https://clinical.harmonia.local:8443");
        assertThat(BefeCorsConfig.normalizeOrigin("http://localhost:8080"))
                .isEqualTo("http://localhost:8080");

        // Trailing slash stripped
        assertThat(BefeCorsConfig.normalizeOrigin("https://clinical.harmonia.local/"))
                .isEqualTo("https://clinical.harmonia.local");
    }

    @Test
    @DisplayName("BefeCorsConfig: Rejects invalid or dangerous origins and subdomain suffix attacks")
    void testRejectionOfInvalidOrigins() {
        BefeCorsConfig.setAllowedOriginsOverrideForTesting("https://clinical.harmonia.local");

        // Subdomain suffix attack (attacker owns clinical.harmonia.local.attacker.com)
        assertThat(BefeCorsConfig.isOriginAllowed("https://clinical.harmonia.local.attacker.com")).isFalse();
        assertThat(BefeCorsConfig.isOriginAllowed("https://attacker-clinical.harmonia.local")).isFalse();

        // Scheme mismatch
        assertThat(BefeCorsConfig.isOriginAllowed("http://clinical.harmonia.local")).isFalse();

        // Port mismatch
        assertThat(BefeCorsConfig.isOriginAllowed("https://clinical.harmonia.local:8443")).isFalse();

        // Path suffix
        assertThat(BefeCorsConfig.normalizeOrigin("https://clinical.harmonia.local/api")).isNull();
        assertThat(BefeCorsConfig.isOriginAllowed("https://clinical.harmonia.local/api")).isFalse();

        // Query parameters, fragments, user info
        assertThat(BefeCorsConfig.normalizeOrigin("https://clinical.harmonia.local?foo=bar")).isNull();
        assertThat(BefeCorsConfig.normalizeOrigin("https://clinical.harmonia.local#section")).isNull();
        assertThat(BefeCorsConfig.normalizeOrigin("https://admin:pass@clinical.harmonia.local")).isNull();

        // Wildcard, null string, non-http schemes
        assertThat(BefeCorsConfig.normalizeOrigin("*")).isNull();
        assertThat(BefeCorsConfig.normalizeOrigin("null")).isNull();
        assertThat(BefeCorsConfig.normalizeOrigin("javascript:alert(1)")).isNull();
        assertThat(BefeCorsConfig.normalizeOrigin("file:///etc/passwd")).isNull();
        assertThat(BefeCorsConfig.normalizeOrigin("")).isNull();
        assertThat(BefeCorsConfig.normalizeOrigin(null)).isNull();
    }

    // =========================================================================
    // 2. CorsFilter Request Filter (Preflight & PreMatching) Tests
    // =========================================================================

    @Test
    @DisplayName("CorsFilter: Preflight OPTIONS for trusted origin short-circuits with 200 and exact headers")
    void testTrustedPreflightOptionsShortCircuitsWith200() throws IOException {
        BefeCorsConfig.setAllowedOriginsOverrideForTesting("https://clinical.harmonia.local");

        when(requestContext.getMethod()).thenReturn("OPTIONS");
        when(requestContext.getHeaderString("Origin")).thenReturn("https://clinical.harmonia.local");

        filter.filter(requestContext);

        ArgumentCaptor<Response> responseCaptor = ArgumentCaptor.forClass(Response.class);
        verify(requestContext).abortWith(responseCaptor.capture());

        Response response = responseCaptor.getValue();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getHeaderString("Access-Control-Allow-Origin")).isEqualTo("https://clinical.harmonia.local");
        assertThat(response.getHeaderString("Vary")).isEqualTo("Origin");
        assertThat(response.getHeaderString("Access-Control-Allow-Methods")).isEqualTo(BefeCorsConfig.ALLOWED_METHODS);
        assertThat(response.getHeaderString("Access-Control-Allow-Headers")).isEqualTo(BefeCorsConfig.ALLOWED_HEADERS);
        assertThat(response.getHeaderString("Access-Control-Max-Age")).isEqualTo(BefeCorsConfig.MAX_AGE_SECONDS);
        assertThat(response.getHeaderString("Access-Control-Allow-Credentials")).isNull();
    }

    @Test
    @DisplayName("CorsFilter: Preflight OPTIONS for untrusted origin aborts with 403 Forbidden and no CORS headers")
    void testUntrustedPreflightOptionsAbortsWith403() throws IOException {
        BefeCorsConfig.setAllowedOriginsOverrideForTesting("https://clinical.harmonia.local");

        when(requestContext.getMethod()).thenReturn("OPTIONS");
        when(requestContext.getHeaderString("Origin")).thenReturn("https://attacker.example");

        filter.filter(requestContext);

        ArgumentCaptor<Response> responseCaptor = ArgumentCaptor.forClass(Response.class);
        verify(requestContext).abortWith(responseCaptor.capture());

        Response response = responseCaptor.getValue();
        assertThat(response.getStatus()).isEqualTo(403);
        assertThat(response.getHeaderString("Access-Control-Allow-Origin")).isNull();
        assertThat(response.getHeaderString("Vary")).isNull();
        assertThat(response.getHeaderString("Access-Control-Allow-Methods")).isNull();
        assertThat(response.getHeaderString("Access-Control-Allow-Headers")).isNull();
        assertThat(response.getHeaderString("Access-Control-Allow-Credentials")).isNull();
    }

    @Test
    @DisplayName("CorsFilter: Preflight OPTIONS under empty config aborts with 403 Forbidden")
    void testPreflightOptionsEmptyConfigAbortsWith403() throws IOException {
        // No allowed origins configured (fail closed)
        when(requestContext.getMethod()).thenReturn("OPTIONS");
        when(requestContext.getHeaderString("Origin")).thenReturn("https://clinical.harmonia.local");

        filter.filter(requestContext);

        ArgumentCaptor<Response> responseCaptor = ArgumentCaptor.forClass(Response.class);
        verify(requestContext).abortWith(responseCaptor.capture());

        Response response = responseCaptor.getValue();
        assertThat(response.getStatus()).isEqualTo(403);
        assertThat(response.getHeaderString("Access-Control-Allow-Origin")).isNull();
    }

    @Test
    @DisplayName("CorsFilter: Non-CORS request (no Origin) is not aborted and processes normally")
    void testNonCorsRequestDoesNotAbort() throws IOException {
        BefeCorsConfig.setAllowedOriginsOverrideForTesting("https://clinical.harmonia.local");

        when(requestContext.getMethod()).thenReturn("GET");
        when(requestContext.getHeaderString("Origin")).thenReturn(null);

        filter.filter(requestContext);

        verify(requestContext, never()).abortWith(any());
    }

    @Test
    @DisplayName("CorsFilter: Non-CORS OPTIONS request (no Origin) is not aborted by CORS filter")
    void testNonCorsOptionsDoesNotAbort() throws IOException {
        BefeCorsConfig.setAllowedOriginsOverrideForTesting("https://clinical.harmonia.local");

        when(requestContext.getMethod()).thenReturn("OPTIONS");
        when(requestContext.getHeaderString("Origin")).thenReturn(null);

        filter.filter(requestContext);

        verify(requestContext, never()).abortWith(any());
    }

    // =========================================================================
    // 3. CorsFilter Response Filter Tests
    // =========================================================================

    @Test
    @DisplayName("CorsFilter: Normal response for trusted origin includes exact origin and Vary: Origin")
    void testTrustedOriginResponseFilter() throws IOException {
        BefeCorsConfig.setAllowedOriginsOverrideForTesting("https://clinical.harmonia.local");

        when(requestContext.getMethod()).thenReturn("GET");
        when(requestContext.getHeaderString("Origin")).thenReturn("https://clinical.harmonia.local");

        filter.filter(requestContext, responseContext);

        assertThat(responseHeaders.getFirst("Access-Control-Allow-Origin")).isEqualTo("https://clinical.harmonia.local");
        assertThat(responseHeaders.getFirst("Vary")).isEqualTo("Origin");
        assertThat(responseHeaders.getFirst("Access-Control-Allow-Credentials")).isNull();
        assertThat(responseHeaders.getFirst("Access-Control-Allow-Methods")).isNull();
    }

    @Test
    @DisplayName("CorsFilter: Non-reflection — untrusted origin receives no Access-Control-Allow-Origin header")
    void testUntrustedOriginResponseFilterNonReflection() throws IOException {
        BefeCorsConfig.setAllowedOriginsOverrideForTesting("https://clinical.harmonia.local");

        when(requestContext.getMethod()).thenReturn("GET");
        when(requestContext.getHeaderString("Origin")).thenReturn("https://attacker.example");

        filter.filter(requestContext, responseContext);

        assertThat(responseHeaders.getFirst("Access-Control-Allow-Origin")).isNull();
        assertThat(responseHeaders.getFirst("Vary")).isNull();
        assertThat(responseHeaders.getFirst("Access-Control-Allow-Credentials")).isNull();
    }

    @Test
    @DisplayName("CorsFilter: Non-CORS request receives no CORS response headers")
    void testNonCorsResponseFilter() throws IOException {
        BefeCorsConfig.setAllowedOriginsOverrideForTesting("https://clinical.harmonia.local");

        when(requestContext.getMethod()).thenReturn("GET");
        when(requestContext.getHeaderString("Origin")).thenReturn(null);

        filter.filter(requestContext, responseContext);

        assertThat(responseHeaders.getFirst("Access-Control-Allow-Origin")).isNull();
        assertThat(responseHeaders.getFirst("Vary")).isNull();
        assertThat(responseHeaders.getFirst("Access-Control-Allow-Credentials")).isNull();
    }

    @Test
    @DisplayName("CorsFilter: Preflight response filter also applies method and header restrictions")
    void testPreflightResponseFilterSetsAllHeaders() throws IOException {
        BefeCorsConfig.setAllowedOriginsOverrideForTesting("https://clinical.harmonia.local");

        when(requestContext.getMethod()).thenReturn("OPTIONS");
        when(requestContext.getHeaderString("Origin")).thenReturn("https://clinical.harmonia.local");

        filter.filter(requestContext, responseContext);

        assertThat(responseHeaders.getFirst("Access-Control-Allow-Origin")).isEqualTo("https://clinical.harmonia.local");
        assertThat(responseHeaders.getFirst("Vary")).isEqualTo("Origin");
        assertThat(responseHeaders.getFirst("Access-Control-Allow-Methods")).isEqualTo(BefeCorsConfig.ALLOWED_METHODS);
        assertThat(responseHeaders.getFirst("Access-Control-Allow-Headers")).isEqualTo(BefeCorsConfig.ALLOWED_HEADERS);
        assertThat(responseHeaders.getFirst("Access-Control-Max-Age")).isEqualTo(BefeCorsConfig.MAX_AGE_SECONDS);
        assertThat(responseHeaders.getFirst("Access-Control-Allow-Credentials")).isNull();
    }

    @Test
    @DisplayName("CorsFilter: Wildcard origin and credentials are never emitted")
    void testWildcardAndCredentialsNeverEmitted() throws IOException {
        BefeCorsConfig.setAllowedOriginsOverrideForTesting("https://clinical.harmonia.local");

        // Test with trusted origin
        when(requestContext.getMethod()).thenReturn("GET");
        when(requestContext.getHeaderString("Origin")).thenReturn("https://clinical.harmonia.local");
        filter.filter(requestContext, responseContext);

        assertThat(responseHeaders.getFirst("Access-Control-Allow-Origin")).isNotEqualTo("*");
        assertThat(responseHeaders.getFirst("Access-Control-Allow-Credentials")).isNull();

        // Test with untrusted origin
        responseHeaders.clear();
        when(requestContext.getHeaderString("Origin")).thenReturn("https://attacker.example");
        filter.filter(requestContext, responseContext);

        assertThat(responseHeaders.getFirst("Access-Control-Allow-Origin")).isNull();
        assertThat(responseHeaders.getFirst("Access-Control-Allow-Credentials")).isNull();
    }
}
