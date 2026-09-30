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

import java.net.UnknownHostException;
import java.nio.channels.UnresolvedAddressException;

/**
 * Conservative transport failure classifier for authoritative persistence operations.
 * <p>
 * Governing rule (AX-01, AX-05): {@code NotCommitted} is returned ONLY when it is positively
 * established that no authoritative request could have reached Mnemosyne (e.g. client validation
 * or unequivocal pre-network failure such as DNS resolution failure).
 * <p>
 * All other connection, timeout, socket-reuse, stream, or transport failures fail conservatively
 * to {@code OutcomeUnknown}.
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

        Throwable rootCause = getRootCause(throwable);

        // Positively established pre-network DNS failures
        if (rootCause instanceof UnknownHostException || rootCause instanceof UnresolvedAddressException) {
            return new AuthoritativePersistenceResult.NotCommitted<>(
                    "DNS resolution failed before request transmission during " + operationName + ": " + rootCause.getMessage(),
                    throwable);
        }

        // Positively established client-side validation failures
        if (throwable instanceof IllegalArgumentException
                || throwable instanceof NullPointerException
                || throwable instanceof IllegalStateException) {
            return new AuthoritativePersistenceResult.NotCommitted<>(
                    "Local client-side pre-transmission error during " + operationName + ": " + throwable.getMessage(),
                    throwable);
        }

        // All ambiguous connection, timeout, mid-stream, reset, and IO failures fail conservatively to OutcomeUnknown
        return new AuthoritativePersistenceResult.OutcomeUnknown<>(
                "Indeterminate transport failure during " + operationName + " (state unknown): " + throwable.getMessage(),
                throwable);
    }

    private static Throwable getRootCause(Throwable throwable) {
        Throwable cause = throwable;
        while (cause.getCause() != null && cause.getCause() != cause) {
            cause = cause.getCause();
        }
        return cause;
    }
}
