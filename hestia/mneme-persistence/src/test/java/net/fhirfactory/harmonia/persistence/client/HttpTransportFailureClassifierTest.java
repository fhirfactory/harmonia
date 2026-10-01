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

import net.fhirfactory.harmonia.hapifhir.persistence.model.AuthoritativePersistenceResult;
import org.hl7.fhir.r5.model.Patient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.net.http.HttpConnectTimeoutException;
import java.net.http.HttpTimeoutException;
import java.nio.channels.UnresolvedAddressException;
import java.security.cert.CertificateException;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("HttpTransportFailureClassifier Unit Tests")
class HttpTransportFailureClassifierTest {

    @Test
    @DisplayName("Classifies direct and wrapped SSLHandshakeException as NotCommitted")
    void classifiesSslHandshakeExceptionAsNotCommitted() {
        SSLHandshakeException direct = new SSLHandshakeException("Received fatal alert: bad_certificate");
        AuthoritativePersistenceResult<Patient> result1 = HttpTransportFailureClassifier.classify(direct, "CREATE");
        assertThat(result1).isInstanceOf(AuthoritativePersistenceResult.NotCommitted.class);

        IOException wrapped = new IOException("TLS error", new SSLHandshakeException("PKIX path building failed"));
        AuthoritativePersistenceResult<Patient> result2 = HttpTransportFailureClassifier.classify(wrapped, "CREATE");
        assertThat(result2).isInstanceOf(AuthoritativePersistenceResult.NotCommitted.class);
    }

    @Test
    @DisplayName("Classifies SSLPeerUnverifiedException and CertificateException as NotCommitted")
    void classifiesPeerUnverifiedAndCertificateExceptionAsNotCommitted() {
        SSLPeerUnverifiedException unverified = new SSLPeerUnverifiedException("Peer not authenticated");
        AuthoritativePersistenceResult<Patient> result1 = HttpTransportFailureClassifier.classify(unverified, "READ");
        assertThat(result1).isInstanceOf(AuthoritativePersistenceResult.NotCommitted.class);

        CertificateException certEx = new CertificateException("Untrusted root");
        AuthoritativePersistenceResult<Patient> result2 = HttpTransportFailureClassifier.classify(certEx, "READ");
        assertThat(result2).isInstanceOf(AuthoritativePersistenceResult.NotCommitted.class);
    }

    @Test
    @DisplayName("Classifies HttpConnectTimeoutException and ConnectException as NotCommitted")
    void classifiesConnectTimeoutAndRefusedAsNotCommitted() {
        HttpConnectTimeoutException timeout = new HttpConnectTimeoutException("Connect timed out");
        AuthoritativePersistenceResult<Patient> result1 = HttpTransportFailureClassifier.classify(timeout, "UPDATE");
        assertThat(result1).isInstanceOf(AuthoritativePersistenceResult.NotCommitted.class);

        ConnectException refused = new ConnectException("Connection refused");
        AuthoritativePersistenceResult<Patient> result2 = HttpTransportFailureClassifier.classify(refused, "UPDATE");
        assertThat(result2).isInstanceOf(AuthoritativePersistenceResult.NotCommitted.class);
    }

    @Test
    @DisplayName("Classifies DNS failures (UnknownHostException, UnresolvedAddressException) as NotCommitted")
    void classifiesDnsFailuresAsNotCommitted() {
        UnknownHostException uhe = new UnknownHostException("mnemosyne-clinical");
        AuthoritativePersistenceResult<Patient> result1 = HttpTransportFailureClassifier.classify(uhe, "CREATE");
        assertThat(result1).isInstanceOf(AuthoritativePersistenceResult.NotCommitted.class);

        UnresolvedAddressException uae = new UnresolvedAddressException();
        AuthoritativePersistenceResult<Patient> result2 = HttpTransportFailureClassifier.classify(uae, "CREATE");
        assertThat(result2).isInstanceOf(AuthoritativePersistenceResult.NotCommitted.class);
    }

    @Test
    @DisplayName("Classifies local client-side validation errors as NotCommitted")
    void classifiesLocalValidationErrorsAsNotCommitted() {
        IllegalArgumentException iae = new IllegalArgumentException("Invalid resource ID");
        AuthoritativePersistenceResult<Patient> result1 = HttpTransportFailureClassifier.classify(iae, "READ");
        assertThat(result1).isInstanceOf(AuthoritativePersistenceResult.NotCommitted.class);

        NullPointerException npe = new NullPointerException("Resource cannot be null");
        AuthoritativePersistenceResult<Patient> result2 = HttpTransportFailureClassifier.classify(npe, "CREATE");
        assertThat(result2).isInstanceOf(AuthoritativePersistenceResult.NotCommitted.class);
    }

    @Test
    @DisplayName("Classifies indeterminate post-handshake IO, read timeouts, and resets conservatively as OutcomeUnknown")
    void classifiesIndeterminateIoAsOutcomeUnknown() {
        SSLException resetSsl = new SSLException("Connection reset by peer");
        AuthoritativePersistenceResult<Patient> result1 = HttpTransportFailureClassifier.classify(resetSsl, "UPDATE");
        assertThat(result1).isInstanceOf(AuthoritativePersistenceResult.OutcomeUnknown.class);

        HttpTimeoutException readTimeout = new HttpTimeoutException("request timed out");
        AuthoritativePersistenceResult<Patient> result2 = HttpTransportFailureClassifier.classify(readTimeout, "CREATE");
        assertThat(result2).isInstanceOf(AuthoritativePersistenceResult.OutcomeUnknown.class);

        SocketTimeoutException socketTimeout = new SocketTimeoutException("Read timed out");
        AuthoritativePersistenceResult<Patient> result3 = HttpTransportFailureClassifier.classify(socketTimeout, "CREATE");
        assertThat(result3).isInstanceOf(AuthoritativePersistenceResult.OutcomeUnknown.class);

        SocketException socketEx = new SocketException("Broken pipe");
        AuthoritativePersistenceResult<Patient> result4 = HttpTransportFailureClassifier.classify(socketEx, "UPDATE");
        assertThat(result4).isInstanceOf(AuthoritativePersistenceResult.OutcomeUnknown.class);

        IOException genericIo = new IOException("Unexpected end of stream");
        AuthoritativePersistenceResult<Patient> result5 = HttpTransportFailureClassifier.classify(genericIo, "CREATE");
        assertThat(result5).isInstanceOf(AuthoritativePersistenceResult.OutcomeUnknown.class);
    }

    @Test
    @DisplayName("Classifies null throwable conservatively as OutcomeUnknown")
    void classifiesNullThrowableAsOutcomeUnknown() {
        AuthoritativePersistenceResult<Patient> result = HttpTransportFailureClassifier.classify(null, "READ");
        assertThat(result).isInstanceOf(AuthoritativePersistenceResult.OutcomeUnknown.class);
    }
}
