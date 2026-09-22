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

package net.fhirfactory.harmonia.befe.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class OidcTestTokenHelperTest {

    @Test
    @DisplayName("1. Valid token generation and signature verification")
    void testValidToken() {
        String token = OidcTestTokenHelper.generateValidToken("dr-alice");
        assertThat(token).isNotBlank();

        String[] parts = token.split("\\.");
        assertThat(parts).hasSize(3);

        boolean signatureValid = OidcTestTokenHelper.verifySignature(token, OidcTestTokenHelper.getDefaultPublicKey());
        assertThat(signatureValid).isTrue();

        Map<String, Object> claims = OidcTestTokenHelper.parseClaimsUnsafe(token);
        assertThat(claims.get("sub")).isEqualTo("dr-alice");
        assertThat(claims.get("iss")).isEqualTo(OidcTestTokenHelper.DEFAULT_ISSUER);
        assertThat(claims.get("aud")).isEqualTo(OidcTestTokenHelper.DEFAULT_AUDIENCE);
        assertThat(claims.get("exp")).isNotNull();
        assertThat(claims.get("iat")).isNotNull();
    }

    @Test
    @DisplayName("2. Expired token has exp in the past and valid signature")
    void testExpiredToken() {
        String token = OidcTestTokenHelper.generateExpiredToken("dr-bob");
        assertThat(token).isNotBlank();

        boolean signatureValid = OidcTestTokenHelper.verifySignature(token, OidcTestTokenHelper.getDefaultPublicKey());
        assertThat(signatureValid).isTrue();

        Map<String, Object> claims = OidcTestTokenHelper.parseClaimsUnsafe(token);
        assertThat(claims.get("sub")).isEqualTo("dr-bob");
        long exp = ((Number) claims.get("exp")).longValue();
        assertThat(exp).isLessThan(Instant.now().getEpochSecond());
    }

    @Test
    @DisplayName("3. Wrong issuer token contains the specified wrong issuer")
    void testWrongIssuerToken() {
        String wrongIssuer = "https://malicious-idp.example.com/auth";
        String token = OidcTestTokenHelper.generateWrongIssuerToken("attacker", wrongIssuer);

        Map<String, Object> claims = OidcTestTokenHelper.parseClaimsUnsafe(token);
        assertThat(claims.get("sub")).isEqualTo("attacker");
        assertThat(claims.get("iss")).isEqualTo(wrongIssuer);
    }

    @Test
    @DisplayName("4. Wrong audience token contains the specified wrong audience")
    void testWrongAudienceToken() {
        String wrongAudience = "external-billing-api";
        String token = OidcTestTokenHelper.generateWrongAudienceToken("nurse-carol", wrongAudience);

        Map<String, Object> claims = OidcTestTokenHelper.parseClaimsUnsafe(token);
        assertThat(claims.get("sub")).isEqualTo("nurse-carol");
        assertThat(claims.get("aud")).isEqualTo(wrongAudience);
    }

    @Test
    @DisplayName("5. Untrusted key token fails signature verification with default public key")
    void testUntrustedKeyToken() {
        String token = OidcTestTokenHelper.generateUntrustedKeyToken("untrusted-user");

        boolean verifiedWithDefaultKey = OidcTestTokenHelper.verifySignature(token, OidcTestTokenHelper.getDefaultPublicKey());
        assertThat(verifiedWithDefaultKey).isFalse();

        boolean verifiedWithUntrustedKey = OidcTestTokenHelper.verifySignature(token, OidcTestTokenHelper.getUntrustedPublicKey());
        assertThat(verifiedWithUntrustedKey).isTrue();
    }

    @Test
    @DisplayName("6. Corrupted signature token fails signature verification")
    void testCorruptedSignatureToken() {
        String token = OidcTestTokenHelper.generateCorruptedSignatureToken("dr-alice");

        boolean signatureValid = OidcTestTokenHelper.verifySignature(token, OidcTestTokenHelper.getDefaultPublicKey());
        assertThat(signatureValid).isFalse();
    }
}
