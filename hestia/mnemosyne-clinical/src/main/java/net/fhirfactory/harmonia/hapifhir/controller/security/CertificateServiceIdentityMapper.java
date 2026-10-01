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

import net.fhirfactory.harmonia.themis.core.identities.HarmoniaServiceIdentities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Deterministic, bounded mapping adapter establishing Harmonia canonical service identity
 * exclusively from authenticated X.509 Subject Alternative Name (SAN) URI extensions (type 6).
 *
 * <p>Architectural Invariant (M2.3):
 * <ul>
 *   <li>The normative service identity source is the URI Subject Alternative Name (SAN): {@code urn:harmonia:service:mneme}.</li>
 *   <li>Subject Common Name (CN) is descriptive/diagnostic only and MUST NOT independently establish service identity.</li>
 *   <li>Generic wildcard mapping (e.g. {@code urn:harmonia:service:*} -&gt; {@code service:*}) is strictly forbidden.</li>
 *   <li>Missing, unknown, malformed, or multiple conflicting SAN identities fail closed (return {@link Optional#empty()}).</li>
 * </ul>
 */
@Component
public class CertificateServiceIdentityMapper {

    private static final Logger log = LoggerFactory.getLogger(CertificateServiceIdentityMapper.class);

    /**
     * General name type 6 corresponds to uniformResourceIdentifier in RFC 5280 / X.509.
     */
    public static final int SAN_TYPE_URI = 6;

    /**
     * Standard Harmonia SAN URI prefix for service identities.
     */
    public static final String HARMONIA_SERVICE_URI_PREFIX = "urn:harmonia:service:";

    /**
     * Explicit bounded mapping table for trusted Harmonia service URI SANs to canonical service identities.
     */
    private static final Map<String, String> TRUSTED_SERVICE_URI_MAPPINGS = Map.of(
            "urn:harmonia:service:mneme", HarmoniaServiceIdentities.ID_MNEME,
            "urn:harmonia:service:mnemosyne", HarmoniaServiceIdentities.ID_MNEMOSYNE
    );

    /**
     * Maps an authenticated X.509 client certificate to a canonical Harmonia service identity.
     *
     * @param certificate the validated X.509 client certificate (may be null)
     * @return an {@link Optional} containing the canonical service identity if bounded and trusted, or {@link Optional#empty()}
     */
    public Optional<String> mapToServiceIdentity(X509Certificate certificate) {
        if (certificate == null) {
            log.debug("Certificate is null; cannot map service identity");
            return Optional.empty();
        }

        Collection<List<?>> sanEntries;
        try {
            sanEntries = certificate.getSubjectAlternativeNames();
        } catch (CertificateParsingException e) {
            log.warn("Failed to parse Subject Alternative Names from certificate: {}", e.getMessage());
            return Optional.empty();
        }

        if (sanEntries == null || sanEntries.isEmpty()) {
            log.debug("No Subject Alternative Names present in certificate: subject={}", certificate.getSubjectX500Principal());
            return Optional.empty();
        }

        List<String> matchedServiceIdentities = new ArrayList<>();
        List<String> unknownHarmoniaUris = new ArrayList<>();

        for (List<?> entry : sanEntries) {
            if (entry != null && entry.size() >= 2) {
                Object typeObj = entry.get(0);
                Object valObj = entry.get(1);

                if (typeObj instanceof Integer && ((Integer) typeObj) == SAN_TYPE_URI && valObj instanceof String) {
                    String uri = ((String) valObj).trim();
                    if (TRUSTED_SERVICE_URI_MAPPINGS.containsKey(uri)) {
                        matchedServiceIdentities.add(TRUSTED_SERVICE_URI_MAPPINGS.get(uri));
                    } else if (uri.startsWith(HARMONIA_SERVICE_URI_PREFIX)) {
                        unknownHarmoniaUris.add(uri);
                    }
                }
            }
        }

        // Fail-closed rule: If any unknown Harmonia service URI SAN was encountered, reject
        if (!unknownHarmoniaUris.isEmpty()) {
            log.warn("Rejected certificate with unregistered Harmonia service URI SAN(s): {}", unknownHarmoniaUris);
            return Optional.empty();
        }

        // Fail-closed rule: Exactly one unique trusted service identity must be matched
        if (matchedServiceIdentities.isEmpty()) {
            log.debug("Certificate contains no trusted Harmonia service URI SAN: subject={}", certificate.getSubjectX500Principal());
            return Optional.empty();
        }

        long distinctCount = matchedServiceIdentities.stream().distinct().count();
        if (distinctCount > 1) {
            log.warn("Rejected certificate with multiple conflicting Harmonia service URI SANs: {}", matchedServiceIdentities);
            return Optional.empty();
        }

        String canonicalIdentity = matchedServiceIdentities.get(0);
        log.debug("Mapped certificate URI SAN to Harmonia service identity: '{}' (subject={})",
                canonicalIdentity, certificate.getSubjectX500Principal());
        return Optional.of(canonicalIdentity);
    }

    /**
     * Returns an unmodifiable view of trusted URI mappings for diagnostic and testing purposes.
     */
    public Map<String, String> getTrustedServiceUriMappings() {
        return Collections.unmodifiableMap(TRUSTED_SERVICE_URI_MAPPINGS);
    }
}
