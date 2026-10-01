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

package net.fhirfactory.harmonia.hapifhir.controller.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("X509CertificateAuthenticationFilter Unit Tests")
class X509CertificateAuthenticationFilterTest {

    private X509CertificateAuthenticationFilter filter;
    private CertificateFactory certificateFactory;

    @BeforeEach
    void setUp() throws Exception {
        CertificateServiceIdentityMapper mapper = new CertificateServiceIdentityMapper();
        filter = new X509CertificateAuthenticationFilter(mapper);
        certificateFactory = CertificateFactory.getInstance("X.509");
    }

    private X509Certificate loadCert(String resourcePath) throws Exception {
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            assertThat(is).as("Resource not found: %s", resourcePath).isNotNull();
            return (X509Certificate) certificateFactory.generateCertificate(is);
        }
    }

    @Test
    @DisplayName("Wraps request with Principal('service:mneme') when valid client certificate attribute is present")
    void authenticatesValidClientCert() throws Exception {
        X509Certificate cert = loadCert("/tls/mneme.crt");

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/authoritative/fhir/Patient/123");
        request.setAttribute(X509CertificateAuthenticationFilter.JAKARTA_CERTIFICATE_ATTR, new X509Certificate[]{cert});
        MockHttpServletResponse response = new MockHttpServletResponse();

        AtomicReference<Principal> capturedPrincipal = new AtomicReference<>();
        AtomicReference<String> capturedAuthType = new AtomicReference<>();

        filter.doFilter(request, response, (req, res) -> {
            HttpServletRequest httpReq = (HttpServletRequest) req;
            capturedPrincipal.set(httpReq.getUserPrincipal());
            capturedAuthType.set(httpReq.getAuthType());
        });

        assertThat(capturedPrincipal.get()).isNotNull();
        assertThat(capturedPrincipal.get().getName()).isEqualTo("service:mneme");
        assertThat(capturedAuthType.get()).isEqualTo("CLIENT_CERT");
    }

    @Test
    @DisplayName("Does not wrap request when certificate is missing or invalid URI SAN")
    void passesUnwrappedWhenCertHasNoValidSan() throws Exception {
        X509Certificate wrongCert = loadCert("/tls/wrong-san.crt");

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/authoritative/fhir/Patient/123");
        request.setAttribute(X509CertificateAuthenticationFilter.JAKARTA_CERTIFICATE_ATTR, new X509Certificate[]{wrongCert});
        MockHttpServletResponse response = new MockHttpServletResponse();

        AtomicReference<Principal> capturedPrincipal = new AtomicReference<>();

        filter.doFilter(request, response, (req, res) -> {
            HttpServletRequest httpReq = (HttpServletRequest) req;
            capturedPrincipal.set(httpReq.getUserPrincipal());
        });

        assertThat(capturedPrincipal.get()).isNull();
    }

    @Test
    @DisplayName("Does not wrap request when no certificate attribute is present on request")
    void passesUnwrappedWhenNoCertAttribute() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/authoritative/fhir/Patient/123");
        MockHttpServletResponse response = new MockHttpServletResponse();

        AtomicReference<Principal> capturedPrincipal = new AtomicReference<>();

        filter.doFilter(request, response, (req, res) -> {
            HttpServletRequest httpReq = (HttpServletRequest) req;
            capturedPrincipal.set(httpReq.getUserPrincipal());
        });

        assertThat(capturedPrincipal.get()).isNull();
    }
}
