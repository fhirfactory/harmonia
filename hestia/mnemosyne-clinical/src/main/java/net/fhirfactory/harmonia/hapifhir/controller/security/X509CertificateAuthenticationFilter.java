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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Principal;
import java.security.cert.X509Certificate;
import java.util.Objects;
import java.util.Optional;

/**
 * High-precedence Servlet Filter extracting authenticated X.509 client certificates
 * injected by embedded Tomcat JSSE into {@code jakarta.servlet.request.X509Certificate},
 * resolving the canonical Harmonia service identity via {@link CertificateServiceIdentityMapper},
 * and wrapping the request so {@link HttpServletRequest#getUserPrincipal()} returns the verified service principal.
 */
@Component
public class X509CertificateAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(X509CertificateAuthenticationFilter.class);

    public static final String JAKARTA_CERTIFICATE_ATTR = "jakarta.servlet.request.X509Certificate";
    public static final String JAVAX_CERTIFICATE_ATTR = "javax.servlet.request.X509Certificate";

    private final CertificateServiceIdentityMapper identityMapper;

    @Autowired
    public X509CertificateAuthenticationFilter(CertificateServiceIdentityMapper identityMapper) {
        this.identityMapper = Objects.requireNonNull(identityMapper, "identityMapper must not be null");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        X509Certificate clientCert = extractClientCertificate(request);

        if (clientCert != null) {
            Optional<String> serviceIdentity = identityMapper.mapToServiceIdentity(clientCert);
            if (serviceIdentity.isPresent()) {
                String identityName = serviceIdentity.get();
                log.debug("Authenticating request with X.509 client certificate identity: {}", identityName);
                HttpServletRequest wrappedRequest = new AuthenticatedServiceRequestWrapper(request, identityName);
                filterChain.doFilter(wrappedRequest, response);
                return;
            } else {
                log.debug("Client certificate presented but could not be mapped to a trusted Harmonia service identity: subject={}",
                        clientCert.getSubjectX500Principal());
            }
        }

        filterChain.doFilter(request, response);
    }

    private X509Certificate extractClientCertificate(HttpServletRequest request) {
        Object certsObj = request.getAttribute(JAKARTA_CERTIFICATE_ATTR);
        if (certsObj == null) {
            certsObj = request.getAttribute(JAVAX_CERTIFICATE_ATTR);
        }

        if (certsObj instanceof X509Certificate[]) {
            X509Certificate[] certs = (X509Certificate[]) certsObj;
            if (certs.length > 0 && certs[0] != null) {
                return certs[0];
            }
        }
        return null;
    }

    /**
     * Request wrapper binding the verified service identity to {@link HttpServletRequest#getUserPrincipal()}.
     */
    public static class AuthenticatedServiceRequestWrapper extends HttpServletRequestWrapper {

        private final Principal principal;

        public AuthenticatedServiceRequestWrapper(HttpServletRequest request, String serviceIdentity) {
            super(request);
            Objects.requireNonNull(serviceIdentity, "serviceIdentity must not be null");
            this.principal = new ServicePrincipal(serviceIdentity);
        }

        @Override
        public Principal getUserPrincipal() {
            return this.principal;
        }

        @Override
        public String getRemoteUser() {
            return this.principal.getName();
        }

        @Override
        public String getAuthType() {
            return "CLIENT_CERT";
        }
    }

    /**
     * Simple immutable {@link Principal} implementation representing the authenticated machine service identity.
     */
    public static record ServicePrincipal(String name) implements Principal {
        public ServicePrincipal {
            Objects.requireNonNull(name, "name must not be null");
        }

        @Override
        public String getName() {
            return name;
        }
    }
}
