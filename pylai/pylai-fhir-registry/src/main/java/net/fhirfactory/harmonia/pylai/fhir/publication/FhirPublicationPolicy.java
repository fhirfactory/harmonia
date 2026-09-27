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

package net.fhirfactory.harmonia.pylai.fhir.publication;

import net.fhirfactory.harmonia.model.security.FhirConfidentialityEnum;
import net.fhirfactory.harmonia.model.security.HarmoniaSecurityCodeSystem;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.Coding;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Encapsulates the external interoperability publication contract governing FHIR egress representations.
 * <p>
 * Implements a fail-closed, contract-driven policy that permits standard FHIR core, jurisdictional (AU Base / AU Core),
 * and explicitly approved interoperability extensions/profiles/security tags while stripping Harmonia internal
 * operational metadata (e.g. authoritative persistence versioning, Mneme active-state tokens, Praxis execution IDs,
 * internal checkpoints, and operational security labels).
 */
@Component
public class FhirPublicationPolicy {

    // Standard FHIR Core & Jurisdictional Extension / Profile Prefixes
    public static final String FHIR_CORE_STRUCTURE_DEFINITION_PREFIX = "http://hl7.org/fhir/StructureDefinition/";
    public static final String FHIR_CORE_PREFIX = "http://hl7.org/fhir/";
    public static final String AU_BASE_STRUCTURE_DEFINITION_PREFIX = "http://hl7.org.au/fhir/StructureDefinition/";
    public static final String AU_BASE_PREFIX = "http://hl7.org.au/fhir/";
    public static final String AU_DIGITAL_HEALTH_PREFIX = "http://ns.electronichealth.net.au/";

    // Harmonia Internal Operational Prefixes (excluded unless explicitly approved in contract)
    public static final String HARMONIA_STRUCTURE_PREFIX = "http://harmonia.fhirfactory.net/";
    public static final String HARMONIA_TASK_PREFIX = "http://fhirfactory.net/harmonia/";
    public static final String LEGACY_HIE_TASK_PREFIX = "http://fhirfactory.net/hie/";

    // Standard Security Classification Systems
    public static final String HL7_CONFIDENTIALITY_SYSTEM = FhirConfidentialityEnum.CONFIDENTIALITY_SYSTEM;
    public static final String HL7_V3_OBSERVATION_VALUE_SYSTEM = "http://terminology.hl7.org/CodeSystem/v3-ObservationValue";
    public static final String HL7_V3_ACT_CODE_SYSTEM = "http://terminology.hl7.org/CodeSystem/v3-ActCode";
    public static final String HL7_SECURITY_LABELS_VALUE_SET = "http://hl7.org/fhir/ValueSet/security-labels";

    private final Set<String> permittedExtensionPrefixes;
    private final Set<String> permittedExtensionUrls;
    private final Set<String> permittedProfilePrefixes;
    private final Set<String> permittedProfileUrls;
    private final Set<String> permittedSecuritySystems;
    private final Set<String> permittedSecurityCodings;
    private final Set<String> permittedTagSystems;
    private final Set<String> permittedTagCodings;

    public FhirPublicationPolicy() {
        this(builder());
    }

    private FhirPublicationPolicy(Builder builder) {
        this.permittedExtensionPrefixes = Set.copyOf(builder.permittedExtensionPrefixes);
        this.permittedExtensionUrls = Set.copyOf(builder.permittedExtensionUrls);
        this.permittedProfilePrefixes = Set.copyOf(builder.permittedProfilePrefixes);
        this.permittedProfileUrls = Set.copyOf(builder.permittedProfileUrls);
        this.permittedSecuritySystems = Set.copyOf(builder.permittedSecuritySystems);
        this.permittedSecurityCodings = Set.copyOf(builder.permittedSecurityCodings);
        this.permittedTagSystems = Set.copyOf(builder.permittedTagSystems);
        this.permittedTagCodings = Set.copyOf(builder.permittedTagCodings);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static FhirPublicationPolicy defaultPolicy() {
        return builder().build();
    }

    /**
     * Evaluates whether an extension URL is permitted by the external interoperability contract.
     * <p>
     * Fail-closed: returns {@code true} only if the URL is explicitly permitted or matches an approved
     * prefix (e.g. FHIR core, AU Base, approved third-party/Harmonia interop extensions). Returns {@code false}
     * for private operational extensions and unapproved/unknown URLs.
     *
     * @param url the extension URL
     * @return {@code true} if permitted by contract, {@code false} otherwise
     */
    public boolean isExtensionPermitted(String url) {
        if (StringUtils.isBlank(url)) {
            return false;
        }

        // Explicitly permitted exact URLs (takes priority, allowing contract-approved Harmonia/vendor extensions)
        if (permittedExtensionUrls.contains(url)) {
            return true;
        }

        // Deny known internal operational prefixes if not in explicit permitted URL set
        if (isHarmoniaOperationalPrefix(url)) {
            return false;
        }

        // Check permitted prefixes (FHIR core, AU Base, etc.)
        for (String prefix : permittedExtensionPrefixes) {
            if (url.startsWith(prefix)) {
                return true;
            }
        }

        // Fail-closed
        return false;
    }

    /**
     * Evaluates whether a security label Coding is permitted by the external interoperability contract.
     * <p>
     * Permits standard clinical confidentiality tags (HL7 v3 Confidentiality) and contract-approved security tags,
     * while stripping Harmonia operational security labels (e.g. {@code http://harmonia.fhirfactory.net/security/*}).
     *
     * @param coding the security Coding from {@code meta.security}
     * @return {@code true} if permitted by contract, {@code false} otherwise
     */
    public boolean isSecurityLabelPermitted(Coding coding) {
        if (coding == null) {
            return false;
        }

        String system = coding.getSystem();
        String code = coding.getCode();

        if (system != null && code != null) {
            String compositeKey = system + "|" + code;
            if (permittedSecurityCodings.contains(compositeKey)) {
                return true;
            }
        }

        // Explicitly strip Harmonia operational security labels unless specifically permitted
        if (isHarmoniaSecuritySystem(system)) {
            return false;
        }

        // Standard clinical confidentiality
        if (HL7_CONFIDENTIALITY_SYSTEM.equalsIgnoreCase(system)) {
            if (code != null && FhirConfidentialityEnum.fromCode(code).isPresent()) {
                return true;
            }
        }

        // Standard permitted security systems
        if (system != null && permittedSecuritySystems.contains(system)) {
            return true;
        }

        // Fail-closed
        return false;
    }

    /**
     * Evaluates whether a conformance profile URL is permitted by the external interoperability contract.
     *
     * @param profileUrl the profile canonical URL
     * @return {@code true} if permitted by contract, {@code false} otherwise
     */
    public boolean isProfilePermitted(String profileUrl) {
        if (StringUtils.isBlank(profileUrl)) {
            return false;
        }

        if (permittedProfileUrls.contains(profileUrl)) {
            return true;
        }

        if (isHarmoniaOperationalPrefix(profileUrl)) {
            return false;
        }

        for (String prefix : permittedProfilePrefixes) {
            if (profileUrl.startsWith(prefix)) {
                return true;
            }
        }

        // Fail-closed
        return false;
    }

    /**
     * Evaluates whether a meta.tag Coding is permitted by the external interoperability contract.
     * <p>
     * Strips Harmonia operational / private tags while preserving standard or contract-permitted tags.
     *
     * @param coding the tag Coding from {@code meta.tag}
     * @return {@code true} if permitted by contract, {@code false} otherwise
     */
    public boolean isTagPermitted(Coding coding) {
        if (coding == null) {
            return false;
        }

        String system = coding.getSystem();
        String code = coding.getCode();

        if (system != null && code != null) {
            String compositeKey = system + "|" + code;
            if (permittedTagCodings.contains(compositeKey)) {
                return true;
            }
        }

        // Strip Harmonia operational systems
        if (isHarmoniaOperationalPrefix(system) || isHarmoniaSecuritySystem(system)) {
            return false;
        }

        // If specific tag systems are configured, check against them; otherwise permit standard/non-operational tags
        if (!permittedTagSystems.isEmpty()) {
            return system != null && permittedTagSystems.contains(system);
        }

        return true;
    }

    private boolean isHarmoniaOperationalPrefix(String uri) {
        if (uri == null) {
            return false;
        }
        return uri.startsWith(HARMONIA_STRUCTURE_PREFIX)
                || uri.startsWith(HARMONIA_TASK_PREFIX)
                || uri.startsWith(LEGACY_HIE_TASK_PREFIX);
    }

    private boolean isHarmoniaSecuritySystem(String system) {
        if (system == null) {
            return false;
        }
        return system.startsWith("http://harmonia.fhirfactory.net/security")
                || system.startsWith("http://fhirfactory.net/harmonia/security")
                || system.startsWith("http://fhirfactory.net/hie/security")
                || HarmoniaSecurityCodeSystem.SECURITY_LABEL_SYSTEM.equalsIgnoreCase(system);
    }

    public Set<String> getPermittedExtensionPrefixes() {
        return permittedExtensionPrefixes;
    }

    public Set<String> getPermittedExtensionUrls() {
        return permittedExtensionUrls;
    }

    public Set<String> getPermittedProfilePrefixes() {
        return permittedProfilePrefixes;
    }

    public Set<String> getPermittedProfileUrls() {
        return permittedProfileUrls;
    }

    public Set<String> getPermittedSecuritySystems() {
        return permittedSecuritySystems;
    }

    public Set<String> getPermittedSecurityCodings() {
        return permittedSecurityCodings;
    }

    public Set<String> getPermittedTagSystems() {
        return permittedTagSystems;
    }

    public Set<String> getPermittedTagCodings() {
        return permittedTagCodings;
    }

    /**
     * Fluent Builder for customizing {@link FhirPublicationPolicy} interoperability contracts.
     */
    public static class Builder {
        private final Set<String> permittedExtensionPrefixes = new HashSet<>();
        private final Set<String> permittedExtensionUrls = new HashSet<>();
        private final Set<String> permittedProfilePrefixes = new HashSet<>();
        private final Set<String> permittedProfileUrls = new HashSet<>();
        private final Set<String> permittedSecuritySystems = new HashSet<>();
        private final Set<String> permittedSecurityCodings = new HashSet<>();
        private final Set<String> permittedTagSystems = new HashSet<>();
        private final Set<String> permittedTagCodings = new HashSet<>();

        public Builder() {
            // Standard Default Interoperability Contract Configuration
            permittedExtensionPrefixes.add(FHIR_CORE_STRUCTURE_DEFINITION_PREFIX);
            permittedExtensionPrefixes.add(FHIR_CORE_PREFIX);
            permittedExtensionPrefixes.add(AU_BASE_STRUCTURE_DEFINITION_PREFIX);
            permittedExtensionPrefixes.add(AU_BASE_PREFIX);
            permittedExtensionPrefixes.add(AU_DIGITAL_HEALTH_PREFIX);

            permittedProfilePrefixes.add(FHIR_CORE_STRUCTURE_DEFINITION_PREFIX);
            permittedProfilePrefixes.add(FHIR_CORE_PREFIX);
            permittedProfilePrefixes.add(AU_BASE_STRUCTURE_DEFINITION_PREFIX);
            permittedProfilePrefixes.add(AU_BASE_PREFIX);
            permittedProfilePrefixes.add(AU_DIGITAL_HEALTH_PREFIX);

            permittedSecuritySystems.add(HL7_CONFIDENTIALITY_SYSTEM);
            permittedSecuritySystems.add(HL7_V3_OBSERVATION_VALUE_SYSTEM);
            permittedSecuritySystems.add(HL7_V3_ACT_CODE_SYSTEM);
            permittedSecuritySystems.add(HL7_SECURITY_LABELS_VALUE_SET);
        }

        public Builder permitExtensionPrefix(String prefix) {
            if (StringUtils.isNotBlank(prefix)) {
                this.permittedExtensionPrefixes.add(prefix);
            }
            return this;
        }

        public Builder permitExtensionUrl(String url) {
            if (StringUtils.isNotBlank(url)) {
                this.permittedExtensionUrls.add(url);
            }
            return this;
        }

        public Builder permitProfilePrefix(String prefix) {
            if (StringUtils.isNotBlank(prefix)) {
                this.permittedProfilePrefixes.add(prefix);
            }
            return this;
        }

        public Builder permitProfileUrl(String url) {
            if (StringUtils.isNotBlank(url)) {
                this.permittedProfileUrls.add(url);
            }
            return this;
        }

        public Builder permitSecuritySystem(String system) {
            if (StringUtils.isNotBlank(system)) {
                this.permittedSecuritySystems.add(system);
            }
            return this;
        }

        public Builder permitSecurityLabel(String system, String code) {
            if (StringUtils.isNotBlank(system) && StringUtils.isNotBlank(code)) {
                this.permittedSecurityCodings.add(system + "|" + code);
            }
            return this;
        }

        public Builder permitTagSystem(String system) {
            if (StringUtils.isNotBlank(system)) {
                this.permittedTagSystems.add(system);
            }
            return this;
        }

        public Builder permitTag(String system, String code) {
            if (StringUtils.isNotBlank(system) && StringUtils.isNotBlank(code)) {
                this.permittedTagCodings.add(system + "|" + code);
            }
            return this;
        }

        public FhirPublicationPolicy build() {
            return new FhirPublicationPolicy(this);
        }
    }
}
