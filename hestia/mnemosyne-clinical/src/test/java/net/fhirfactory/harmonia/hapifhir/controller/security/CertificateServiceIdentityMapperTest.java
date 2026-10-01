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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Principal;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CertificateServiceIdentityMapper Unit Tests")
class CertificateServiceIdentityMapperTest {

    private CertificateServiceIdentityMapper mapper;
    private CertificateFactory certificateFactory;

    @BeforeEach
    void setUp() throws CertificateException {
        mapper = new CertificateServiceIdentityMapper();
        certificateFactory = CertificateFactory.getInstance("X.509");
    }

    private X509Certificate loadCert(String resourcePath) throws Exception {
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            assertThat(is).as("Resource not found: %s", resourcePath).isNotNull();
            return (X509Certificate) certificateFactory.generateCertificate(is);
        }
    }

    @Test
    @DisplayName("Maps valid Mneme certificate with normative URI SAN urn:harmonia:service:mneme to service:mneme")
    void mapsValidMnemeCertificate() throws Exception {
        X509Certificate cert = loadCert("/tls/mneme.crt");
        Optional<String> identity = mapper.mapToServiceIdentity(cert);

        assertThat(identity).isPresent().contains("service:mneme");
    }

    @Test
    @DisplayName("Fails closed on certificate with CN=service:mneme but missing URI SAN (CN non-reliance)")
    void failsClosedOnCertificateMissingUriSan() throws Exception {
        X509Certificate cert = loadCert("/tls/no-san.crt");
        Optional<String> identity = mapper.mapToServiceIdentity(cert);

        assertThat(identity).isEmpty();
    }

    @Test
    @DisplayName("Fails closed on certificate with unregistered Harmonia URI SAN urn:harmonia:service:other")
    void failsClosedOnUnregisteredHarmoniaUriSan() throws Exception {
        X509Certificate cert = loadCert("/tls/wrong-san.crt");
        Optional<String> identity = mapper.mapToServiceIdentity(cert);

        assertThat(identity).isEmpty();
    }

    @Test
    @DisplayName("Fails closed on null certificate")
    void failsClosedOnNullCertificate() {
        Optional<String> identity = mapper.mapToServiceIdentity(null);
        assertThat(identity).isEmpty();
    }

    @Test
    @DisplayName("Fails closed when certificate has multiple conflicting Harmonia service URI SANs")
    void failsClosedOnMultipleConflictingSanUris() {
        List<List<?>> sanEntries = List.of(
                List.of(CertificateServiceIdentityMapper.SAN_TYPE_URI, "urn:harmonia:service:mneme"),
                List.of(CertificateServiceIdentityMapper.SAN_TYPE_URI, "urn:harmonia:service:mnemosyne")
        );
        X509Certificate testCert = new StubX509Certificate(sanEntries, false);

        Optional<String> identity = mapper.mapToServiceIdentity(testCert);
        assertThat(identity).isEmpty();
    }

    @Test
    @DisplayName("Fails closed when getSubjectAlternativeNames throws CertificateParsingException")
    void failsClosedOnCertificateParsingException() {
        X509Certificate testCert = new StubX509Certificate(null, true);

        Optional<String> identity = mapper.mapToServiceIdentity(testCert);
        assertThat(identity).isEmpty();
    }

    private static class StubX509Certificate extends X509Certificate {
        private final Collection<List<?>> sans;
        private final boolean throwOnSan;

        StubX509Certificate(Collection<List<?>> sans, boolean throwOnSan) {
            this.sans = sans;
            this.throwOnSan = throwOnSan;
        }

        @Override
        public Collection<List<?>> getSubjectAlternativeNames() throws CertificateParsingException {
            if (throwOnSan) {
                throw new CertificateParsingException("Malformed ASN.1 SAN extension");
            }
            return sans;
        }

        @Override public void checkValidity() throws CertificateExpiredException, CertificateNotYetValidException {}
        @Override public void checkValidity(Date date) throws CertificateExpiredException, CertificateNotYetValidException {}
        @Override public int getVersion() { return 3; }
        @Override public BigInteger getSerialNumber() { return BigInteger.ONE; }
        @Override public Principal getIssuerDN() { return () -> "CN=Test"; }
        @Override public Principal getSubjectDN() { return () -> "CN=Test"; }
        @Override public Date getNotBefore() { return new Date(); }
        @Override public Date getNotAfter() { return new Date(); }
        @Override public byte[] getTBSCertificate() throws CertificateEncodingException { return new byte[0]; }
        @Override public byte[] getSignature() { return new byte[0]; }
        @Override public String getSigAlgName() { return "SHA256withRSA"; }
        @Override public String getSigAlgOID() { return "1.2.840.113549.1.1.11"; }
        @Override public byte[] getSigAlgParams() { return new byte[0]; }
        @Override public boolean[] getIssuerUniqueID() { return new boolean[0]; }
        @Override public boolean[] getSubjectUniqueID() { return new boolean[0]; }
        @Override public boolean[] getKeyUsage() { return new boolean[0]; }
        @Override public int getBasicConstraints() { return -1; }
        @Override public byte[] getEncoded() throws CertificateEncodingException { return new byte[0]; }
        @Override public void verify(PublicKey key) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {}
        @Override public void verify(PublicKey key, String sigProvider) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {}
        @Override public String toString() { return "StubX509Certificate"; }
        @Override public PublicKey getPublicKey() { return null; }
        @Override public boolean hasUnsupportedCriticalExtension() { return false; }
        @Override public Set<String> getCriticalExtensionOIDs() { return Set.of(); }
        @Override public Set<String> getNonCriticalExtensionOIDs() { return Set.of(); }
        @Override public byte[] getExtensionValue(String oid) { return new byte[0]; }
    }
}
