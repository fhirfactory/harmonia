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

import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.*;

/**
 * Deterministic test JWT token signing utility for testing and documentation.
 *
 * Uses standard JDK {@link java.security} cryptographic primitives (RSA 2048 / SHA256withRSA)
 * and Jackson JSON serialization to generate standards-compliant RFC 7519 JWTs (valid, expired,
 * wrong issuer, wrong audience, corrupt signature, untrusted key) for test fixtures without
 * introducing vendor-specific identity SDKs or creating prohibited in-application JWT validators.
 *
 * Boundary Note:
 * In production, JWT signature verification, issuer validation, audience verification, and expiry
 * checks are performed exclusively by the WildFly container layer (via elytron-oidc-client).
 * This utility provides test tokens for container smoke testing and unit/filter test harnesses.
 */
public final class OidcTestTokenHelper {

    public static final String DEFAULT_ISSUER = "https://auth.harmonia.local/realms/harmonia";
    public static final String DEFAULT_AUDIENCE = "iris-befe";
    public static final String DEFAULT_KEY_ID = "harmonia-test-key-1";
    public static final String UNTRUSTED_KEY_ID = "untrusted-test-key-99";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final KeyPair DEFAULT_KEY_PAIR = generateRsaKeyPair(2048);
    private static final KeyPair UNTRUSTED_KEY_PAIR = generateRsaKeyPair(2048);

    private OidcTestTokenHelper() {
        // Utility class
    }

    /**
     * Generates an RSA 2048 KeyPair using standard JDK security providers.
     */
    public static KeyPair generateRsaKeyPair(int keySize) {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(keySize);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("RSA KeyPairGenerator not available in JDK", e);
        }
    }

    /**
     * Returns the default test public key (trusted by the test harness).
     */
    public static PublicKey getDefaultPublicKey() {
        return DEFAULT_KEY_PAIR.getPublic();
    }

    /**
     * Returns the default test private key for signing valid tokens.
     */
    public static PrivateKey getDefaultPrivateKey() {
        return DEFAULT_KEY_PAIR.getPrivate();
    }

    /**
     * Returns an untrusted public key for testing untrusted token rejections.
     */
    public static PublicKey getUntrustedPublicKey() {
        return UNTRUSTED_KEY_PAIR.getPublic();
    }

    /**
     * Returns an untrusted private key for testing untrusted token rejections.
     */
    public static PrivateKey getUntrustedPrivateKey() {
        return UNTRUSTED_KEY_PAIR.getPrivate();
    }

    /**
     * Generates a valid OIDC test token with default issuer, audience, 1-hour expiry, and the given subject.
     */
    public static String generateValidToken(String subject) {
        return generateValidToken(subject, DEFAULT_ISSUER, DEFAULT_AUDIENCE);
    }

    /**
     * Generates a valid OIDC test token with specified issuer, audience, and subject.
     */
    public static String generateValidToken(String subject, String issuer, String audience) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(3600);
        return generateToken(subject, issuer, audience, now, exp, null, null, DEFAULT_KEY_PAIR.getPrivate(), DEFAULT_KEY_ID);
    }

    /**
     * Generates an expired OIDC test token (expired 1 hour ago).
     */
    public static String generateExpiredToken(String subject) {
        Instant now = Instant.now();
        Instant iat = now.minusSeconds(7200);
        Instant exp = now.minusSeconds(3600);
        return generateToken(subject, DEFAULT_ISSUER, DEFAULT_AUDIENCE, iat, exp, null, null, DEFAULT_KEY_PAIR.getPrivate(), DEFAULT_KEY_ID);
    }

    /**
     * Generates an OIDC test token that is not yet valid (nbf in the future).
     */
    public static String generateNotBeforeToken(String subject) {
        Instant now = Instant.now();
        Instant iat = now;
        Instant nbf = now.plusSeconds(1800);
        Instant exp = now.plusSeconds(3600);
        return generateToken(subject, DEFAULT_ISSUER, DEFAULT_AUDIENCE, iat, exp, nbf, null, DEFAULT_KEY_PAIR.getPrivate(), DEFAULT_KEY_ID);
    }

    /**
     * Generates an OIDC test token with a wrong/untrusted issuer claim.
     */
    public static String generateWrongIssuerToken(String subject, String wrongIssuer) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(3600);
        return generateToken(subject, wrongIssuer, DEFAULT_AUDIENCE, now, exp, null, null, DEFAULT_KEY_PAIR.getPrivate(), DEFAULT_KEY_ID);
    }

    /**
     * Generates an OIDC test token with a wrong/mismatched audience claim.
     */
    public static String generateWrongAudienceToken(String subject, String wrongAudience) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(3600);
        return generateToken(subject, DEFAULT_ISSUER, wrongAudience, now, exp, null, null, DEFAULT_KEY_PAIR.getPrivate(), DEFAULT_KEY_ID);
    }

    /**
     * Generates an OIDC test token signed with an untrusted key (not trusted by the test harness).
     */
    public static String generateUntrustedKeyToken(String subject) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(3600);
        return generateToken(subject, DEFAULT_ISSUER, DEFAULT_AUDIENCE, now, exp, null, null, UNTRUSTED_KEY_PAIR.getPrivate(), UNTRUSTED_KEY_ID);
    }

    /**
     * Generates an OIDC test token whose cryptographic signature has been corrupted.
     */
    public static String generateCorruptedSignatureToken(String subject) {
        String validToken = generateValidToken(subject);
        int lastDotIndex = validToken.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return validToken + "corrupted";
        }
        String headerAndPayload = validToken.substring(0, lastDotIndex);
        String signature = validToken.substring(lastDotIndex + 1);
        // Flip characters in signature
        String corruptedSignature = signature.startsWith("A") ? "B" + signature.substring(1) : "A" + signature.substring(1);
        return headerAndPayload + "." + corruptedSignature;
    }

    /**
     * Generates a signed RFC 7519 / RFC 7515 RS256 JWT string.
     */
    public static String generateToken(String subject,
                                       String issuer,
                                       String audience,
                                       Instant issuedAt,
                                       Instant expiresAt,
                                       Instant notBefore,
                                       Map<String, Object> additionalClaims,
                                       PrivateKey signingKey,
                                       String keyId) {
        try {
            // Header
            Map<String, Object> headerMap = new LinkedHashMap<>();
            headerMap.put("alg", "RS256");
            headerMap.put("typ", "JWT");
            if (keyId != null) {
                headerMap.put("kid", keyId);
            }
            String headerJson = OBJECT_MAPPER.writeValueAsString(headerMap);
            String encodedHeader = Base64.getUrlEncoder().withoutPadding().encodeToString(headerJson.getBytes(StandardCharsets.UTF_8));

            // Payload
            Map<String, Object> payloadMap = new LinkedHashMap<>();
            if (issuer != null) {
                payloadMap.put("iss", issuer);
            }
            if (subject != null) {
                payloadMap.put("sub", subject);
            }
            if (audience != null) {
                payloadMap.put("aud", audience);
            }
            if (issuedAt != null) {
                payloadMap.put("iat", issuedAt.getEpochSecond());
            }
            if (expiresAt != null) {
                payloadMap.put("exp", expiresAt.getEpochSecond());
            }
            if (notBefore != null) {
                payloadMap.put("nbf", notBefore.getEpochSecond());
            }
            payloadMap.put("jti", UUID.randomUUID().toString());

            if (additionalClaims != null && !additionalClaims.isEmpty()) {
                payloadMap.putAll(additionalClaims);
            }

            String payloadJson = OBJECT_MAPPER.writeValueAsString(payloadMap);
            String encodedPayload = Base64.getUrlEncoder().withoutPadding().encodeToString(payloadJson.getBytes(StandardCharsets.UTF_8));

            // Signing input
            String signingInput = encodedHeader + "." + encodedPayload;

            // Sign with RS256 (SHA256withRSA)
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(signingKey);
            signature.update(signingInput.getBytes(StandardCharsets.UTF_8));
            byte[] signatureBytes = signature.sign();
            String encodedSignature = Base64.getUrlEncoder().withoutPadding().encodeToString(signatureBytes);

            return signingInput + "." + encodedSignature;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to generate test JWT token", e);
        }
    }

    /**
     * Verifies the RSA signature of a JWT against a given public key (for testing utility verification).
     */
    public static boolean verifySignature(String jwtToken, PublicKey publicKey) {
        try {
            String[] parts = jwtToken.split("\\.");
            if (parts.length != 3) {
                return false;
            }
            String signingInput = parts[0] + "." + parts[1];
            byte[] signatureBytes = Base64.getUrlDecoder().decode(parts[2]);

            Signature verifier = Signature.getInstance("SHA256withRSA");
            verifier.initVerify(publicKey);
            verifier.update(signingInput.getBytes(StandardCharsets.UTF_8));
            return verifier.verify(signatureBytes);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Parses the claims payload of a JWT for test inspection (without cryptographic validation).
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> parseClaimsUnsafe(String jwtToken) {
        try {
            String[] parts = jwtToken.split("\\.");
            if (parts.length < 2) {
                throw new IllegalArgumentException("Invalid JWT format");
            }
            byte[] payloadBytes = Base64.getUrlDecoder().decode(parts[1]);
            return OBJECT_MAPPER.readValue(payloadBytes, Map.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to parse JWT payload", e);
        }
    }
}
