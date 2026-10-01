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
import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.net.http.HttpConnectTimeoutException;
import java.nio.channels.UnresolvedAddressException;
import java.security.cert.CertificateException;

/**
 * Precise, conservative transport failure classifier for authoritative persistence operations.
 * <p>
 * Governing rule (AX-01, AX-05): {@code NotCommitted} is returned ONLY when it is positively
 * established that no authoritative request could have reached Mnemosyne (e.g. client validation,
 * DNS resolution failure, connection refusal, or pre-transmission TLS handshake rejection).
 * <p>
 * All other ambiguous connection resets, mid-stream socket failures, read timeouts, or post-handshake
 * IO failures fail conservatively to {@code OutcomeUnknown}.
 */
public final class HttpTransportFailureClassifier {

    private HttpTransportFailureClassifier() {
        // utility class
    }

    /**
     * Classifies a transport or client-side exception into an explicit {@link AuthoritativePersistenceResult}.
     *
     * @param <T> resource type
     * @param throwable exception thrown during operation
     * @param operationName operation context (e.g. "READ", "CREATE", "UPDATE")
     * @return semantic persistence result (either NotCommitted if provably pre-transmission, or OutcomeUnknown)
     */
    public static <T extends IBaseResource> AuthoritativePersistenceResult<T> classify(
            Throwable throwable,
            String operationName) {
        if (throwable == null) {
            return new AuthoritativePersistenceResult.OutcomeUnknown<>(
                    "Unknown transport failure during " + operationName);
        }

        // 1. Positively established client-side validation failures
        if (throwable instanceof IllegalArgumentException
                || throwable instanceof NullPointerException
                || throwable instanceof IllegalStateException) {
            return new AuthoritativePersistenceResult.NotCommitted<>(
                    "Local client-side pre-transmission error during " + operationName + ": " + throwable.getMessage(),
                    throwable);
        }

        // 2. Check exception chain for provable pre-transmission network/TLS failures
        if (hasCause(throwable, UnknownHostException.class) || hasCause(throwable, UnresolvedAddressException.class)) {
            return new AuthoritativePersistenceResult.NotCommitted<>(
                    "DNS resolution failed before request transmission during " + operationName + ": " + throwable.getMessage(),
                    throwable);
        }
        if (hasCause(throwable, SSLHandshakeException.class)
                || hasCause(throwable, SSLPeerUnverifiedException.class)
                || hasCause(throwable, CertificateException.class)) {
            return new AuthoritativePersistenceResult.NotCommitted<>(
                    "TLS handshake rejected before request transmission during " + operationName + ": " + throwable.getMessage(),
                    throwable);
        }
        if (hasCause(throwable, HttpConnectTimeoutException.class)) {
            return new AuthoritativePersistenceResult.NotCommitted<>(
                    "TCP/TLS connection setup timed out before request transmission during " + operationName + ": " + throwable.getMessage(),
                    throwable);
        }
        if (hasCause(throwable, ConnectException.class)) {
            return new AuthoritativePersistenceResult.NotCommitted<>(
                    "Connection refused before request transmission during " + operationName + ": " + throwable.getMessage(),
                    throwable);
        }

        // 3. All ambiguous mid-stream, read timeout, reset, and IO failures fail conservatively to OutcomeUnknown
        return new AuthoritativePersistenceResult.OutcomeUnknown<>(
                "Indeterminate transport failure during " + operationName + " (state unknown): " + throwable.getMessage(),
                throwable);
    }

    private static boolean hasCause(Throwable throwable, Class<? extends Throwable> targetType) {
        Throwable current = throwable;
        while (current != null) {
            if (targetType.isInstance(current)) {
                return true;
            }
            if (current.getCause() == current) {
                break;
            }
            current = current.getCause();
        }
        return false;
    }
}
