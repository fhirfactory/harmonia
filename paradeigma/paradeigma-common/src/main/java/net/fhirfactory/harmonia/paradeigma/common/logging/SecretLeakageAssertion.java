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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package net.fhirfactory.harmonia.paradeigma.common.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Assertion helpers and synthetic sentinel constants ensuring that credentials,
 * passwords, JWTs, and authentication secrets are never exposed in any logging channel.
 */
public final class SecretLeakageAssertion {

    private SecretLeakageAssertion() {
        // utility class
    }

    public static final String SYNTHETIC_OAUTH_TOKEN = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IlN5bnRoZXRpYyBBZG1pbiIsImlhdCI6MTUxNjIzOTAyMn0.syntheticSignatureSentinel";
    public static final String SYNTHETIC_JWT_BEARER = "Bearer eyJhbGciOiJIUzI1NiJ9.syntheticBearerPayload.syntheticSignature";
    public static final String SYNTHETIC_API_KEY = "harmonia_sec_live_9f83a7c02b8d4e5f91a2b3c4d5e6f7a8";
    public static final String SYNTHETIC_DB_PASSWORD = "pg_super_secret_db_pass_9988!";
    public static final String SYNTHETIC_USER_PASSWORD = "SuperSecretClinicianPassword#2026!";
    public static final String SYNTHETIC_ARTEMIS_SECRET = "artemis_queue_auth_token_xyz789";
    public static final String SYNTHETIC_PRIVATE_KEY = "-----BEGIN PRIVATE KEY-----\nMIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQD3Synthetic...\n-----END PRIVATE KEY-----";

    public static final List<String> STANDARD_SYNTHETIC_SECRETS = List.of(
            SYNTHETIC_OAUTH_TOKEN,
            SYNTHETIC_JWT_BEARER,
            SYNTHETIC_API_KEY,
            SYNTHETIC_DB_PASSWORD,
            SYNTHETIC_USER_PASSWORD,
            SYNTHETIC_ARTEMIS_SECRET,
            SYNTHETIC_PRIVATE_KEY
    );

    public static List<String> getStandardSyntheticSecrets() {
        return STANDARD_SYNTHETIC_SECRETS;
    }

    /**
     * Asserts that none of the provided secret sentinels appear in any of the captured logging events.
     */
    public static void assertNoSecretsPresent(List<ILoggingEvent> events, List<String> secretSentinels) {
        if (events == null || events.isEmpty() || secretSentinels == null || secretSentinels.isEmpty()) {
            return;
        }

        for (ILoggingEvent event : events) {
            String formatted = event.getFormattedMessage();
            for (String secret : secretSentinels) {
                if (secret != null && !secret.isBlank()) {
                    assertThat(formatted)
                            .as("Logging event must NEVER contain secret sentinel '%s'", secret)
                            .doesNotContain(secret);
                }
            }

            if (event.getThrowableProxy() != null) {
                String exMsg = event.getThrowableProxy().getMessage();
                if (exMsg != null) {
                    for (String secret : secretSentinels) {
                        if (secret != null && !secret.isBlank()) {
                            assertThat(exMsg)
                                    .as("Exception message in log must NEVER contain secret sentinel '%s'", secret)
                                    .doesNotContain(secret);
                        }
                    }
                }
            }
        }
    }

    /**
     * Asserts that a serialized string (e.g. audit message, response body, error output) contains no secrets.
     */
    public static void assertNoSecretsInString(String content, List<String> secretSentinels) {
        if (content == null || content.isEmpty() || secretSentinels == null || secretSentinels.isEmpty()) {
            return;
        }
        for (String secret : secretSentinels) {
            if (secret != null && !secret.isBlank()) {
                assertThat(content)
                        .as("Content string must NEVER contain secret sentinel '%s'", secret)
                        .doesNotContain(secret);
            }
        }
    }
}
