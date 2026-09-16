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

package net.fhirfactory.harmonia.model.security;

import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityLabel;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.Coding;
import org.hl7.fhir.r5.model.Meta;
import org.hl7.fhir.r5.model.Resource;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Centralized utility class for inspecting, applying, and enforcing standard HL7 FHIR Release 5
 * security tags ({@code meta.security}) across Harmonia platform resources and bundles.
 */
public final class FhirSecurityTagManager {

    public static final String CONFIDENTIALITY_SYSTEM = FhirConfidentialityEnum.CONFIDENTIALITY_SYSTEM;
    public static final FhirConfidentialityEnum DEFAULT_CONFIDENTIALITY = FhirConfidentialityEnum.N;

    private FhirSecurityTagManager() {
        // Utility class
    }

    /**
     * Applies the default HL7 confidentiality security tag ({@link FhirConfidentialityEnum#N} - Normal)
     * to the given FHIR resource if no confidentiality security tag is already present.
     * Existing security tags from upstream clients or systems are preserved and not overwritten.
     *
     * @param <T> resource type
     * @param resource the FHIR Resource to inspect and tag
     * @return the tagged Resource, or null if input is null
     */
    public static <T extends Resource> T applyDefaultSecurityTag(T resource) {
        if (resource == null) {
            return null;
        }
        if (!hasConfidentialitySecurityTag(resource)) {
            applySecurityTag(resource, DEFAULT_CONFIDENTIALITY);
        }
        return resource;
    }

    /**
     * Applies the specified {@link FhirConfidentialityEnum} security tag to the given FHIR resource.
     * If the resource already has this security tag, no duplicate is added.
     *
     * @param <T> resource type
     * @param resource the FHIR Resource to update
     * @param confidentiality the confidentiality classification
     * @return the updated Resource, or null if input is null
     */
    public static <T extends Resource> T applySecurityTag(T resource, FhirConfidentialityEnum confidentiality) {
        if (resource == null) {
            return null;
        }
        if (confidentiality == null) {
            confidentiality = DEFAULT_CONFIDENTIALITY;
        }
        if (!resource.hasMeta()) {
            resource.setMeta(new Meta());
        }
        boolean alreadyPresent = false;
        if (resource.getMeta().hasSecurity()) {
            for (Coding c : resource.getMeta().getSecurity()) {
                if (CONFIDENTIALITY_SYSTEM.equalsIgnoreCase(c.getSystem())
                        && confidentiality.getCode().equalsIgnoreCase(c.getCode())) {
                    alreadyPresent = true;
                    break;
                }
            }
        }
        if (!alreadyPresent) {
            resource.getMeta().addSecurity(confidentiality.toCoding());
        }
        return resource;
    }

    /**
     * Applies the specified confidentiality code string to the given FHIR resource.
     * If invalid or blank, falls back to {@link #DEFAULT_CONFIDENTIALITY}.
     *
     * @param <T> resource type
     * @param resource the FHIR Resource to update
     * @param confidentialityCode the confidentiality code string (e.g., "N", "R", "V")
     * @return the updated Resource
     */
    public static <T extends Resource> T applySecurityTag(T resource, String confidentialityCode) {
        FhirConfidentialityEnum confidentiality = FhirConfidentialityEnum.fromCode(confidentialityCode)
                .orElse(DEFAULT_CONFIDENTIALITY);
        return applySecurityTag(resource, confidentiality);
    }

    /**
     * Applies default confidentiality security tags to a FHIR {@link Bundle} and all of its constituent
     * entry resources.
     *
     * @param bundle the FHIR Bundle to tag
     * @return the tagged Bundle, or null if input is null
     */
    public static Bundle applySecurityTags(Bundle bundle) {
        return applySecurityTags(bundle, DEFAULT_CONFIDENTIALITY);
    }

    /**
     * Applies the specified confidentiality security tag to a FHIR {@link Bundle} and all of its constituent
     * entry resources.
     *
     * @param bundle the FHIR Bundle to tag
     * @param confidentiality the confidentiality classification to apply
     * @return the tagged Bundle, or null if input is null
     */
    public static Bundle applySecurityTags(Bundle bundle, FhirConfidentialityEnum confidentiality) {
        if (bundle == null) {
            return null;
        }
        applySecurityTag(bundle, confidentiality);
        if (bundle.getEntry() != null) {
            for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {
                if (entry != null && entry.getResource() != null) {
                    applyDefaultSecurityTag(entry.getResource());
                }
            }
        }
        return bundle;
    }

    /**
     * Checks if a FHIR resource contains any security tags in {@code meta.security}.
     *
     * @param resource the FHIR Resource to check
     * @return true if resource has at least one security coding
     */
    public static boolean hasSecurityTag(Resource resource) {
        return resource != null
                && resource.hasMeta()
                && resource.getMeta().hasSecurity()
                && !resource.getMeta().getSecurity().isEmpty();
    }

    /**
     * Checks if a FHIR resource contains a confidentiality security tag from the HL7 v3 Confidentiality system.
     *
     * @param resource the FHIR Resource to check
     * @return true if resource has a confidentiality security coding
     */
    public static boolean hasConfidentialitySecurityTag(Resource resource) {
        if (!hasSecurityTag(resource)) {
            return false;
        }
        return resource.getMeta().getSecurity().stream().anyMatch(c ->
                CONFIDENTIALITY_SYSTEM.equalsIgnoreCase(c.getSystem()) && StringUtils.isNotBlank(c.getCode())
        );
    }

    /**
     * Checks if a FHIR resource contains a specific confidentiality security tag.
     *
     * @param resource the FHIR Resource to check
     * @param confidentiality the confidentiality classification
     * @return true if resource has the specified confidentiality tag
     */
    public static boolean hasConfidentiality(Resource resource, FhirConfidentialityEnum confidentiality) {
        if (confidentiality == null || !hasSecurityTag(resource)) {
            return false;
        }
        return resource.getMeta().getSecurity().stream().anyMatch(c ->
                CONFIDENTIALITY_SYSTEM.equalsIgnoreCase(c.getSystem())
                        && confidentiality.getCode().equalsIgnoreCase(c.getCode())
        );
    }

    /**
     * Extracts the primary {@link FhirConfidentialityEnum} from the resource's {@code meta.security} if present.
     *
     * @param resource the FHIR Resource to inspect
     * @return optional containing the confidentiality enum if present
     */
    public static Optional<FhirConfidentialityEnum> getConfidentiality(Resource resource) {
        if (!hasSecurityTag(resource)) {
            return Optional.empty();
        }
        for (Coding coding : resource.getMeta().getSecurity()) {
            Optional<FhirConfidentialityEnum> conf = FhirConfidentialityEnum.fromCoding(coding);
            if (conf.isPresent()) {
                return conf;
            }
        }
        return Optional.empty();
    }

    /**
     * Extracts the primary {@link FhirConfidentialityEnum} from the resource's {@code meta.security},
     * or returns the default value if not present.
     *
     * @param resource the FHIR Resource to inspect
     * @param defaultVal the fallback confidentiality value
     * @return the confidentiality enum or defaultVal
     */
    public static FhirConfidentialityEnum getConfidentialityOrDefault(Resource resource, FhirConfidentialityEnum defaultVal) {
        return getConfidentiality(resource).orElse(defaultVal);
    }

    // =========================================================================
    // Harmonia Security Label Management
    // =========================================================================

    /**
     * Applies a Harmonia security label (e.g. {@code PROVIDER_REGISTRY}, {@code AUDIT}, {@code INTERNAL})
     * to the resource's {@code meta.security}. Does not duplicate if already present.
     */
    public static <T extends Resource> T applyHarmoniaSecurityLabel(T resource, HarmoniaSecurityLabelEnum label) {
        if (resource == null || label == null) {
            return resource;
        }
        return applyHarmoniaSecurityLabel(resource, label.toThemisSecurityLabel(), label.getDisplay());
    }

    /**
     * Applies a Themis security label to the resource's {@code meta.security}.
     */
    public static <T extends Resource> T applyHarmoniaSecurityLabel(T resource, ThemisSecurityLabel label, String display) {
        if (resource == null || label == null) {
            return resource;
        }
        Meta meta = resource.getMeta();
        if (meta == null) {
            meta = new Meta();
            resource.setMeta(meta);
        }

        String sys = label.system() != null ? label.system() : HarmoniaSecurityCodeSystem.SECURITY_LABEL_SYSTEM;
        String code = label.code();

        boolean alreadyPresent = meta.getSecurity().stream().anyMatch(c ->
                sys.equalsIgnoreCase(c.getSystem()) && code.equalsIgnoreCase(c.getCode())
        );

        if (!alreadyPresent) {
            Coding coding = new Coding(sys, code, display);
            meta.addSecurity(coding);
        }

        return resource;
    }

    /**
     * Checks if the resource has the specified Harmonia security label.
     */
    public static boolean hasHarmoniaSecurityLabel(Resource resource, HarmoniaSecurityLabelEnum label) {
        if (resource == null || label == null) {
            return false;
        }
        return hasHarmoniaSecurityLabel(resource, label.getCode());
    }

    /**
     * Checks if the resource has a Harmonia security label matching the given code.
     */
    public static boolean hasHarmoniaSecurityLabel(Resource resource, String code) {
        if (resource == null || code == null || !hasSecurityTag(resource)) {
            return false;
        }
        return resource.getMeta().getSecurity().stream().anyMatch(c ->
                (c.getSystem() == null || HarmoniaSecurityCodeSystem.SECURITY_LABEL_SYSTEM.equalsIgnoreCase(c.getSystem()))
                        && code.equalsIgnoreCase(c.getCode())
        );
    }

    /**
     * Extracts all Harmonia security labels from the resource's {@code meta.security}.
     */
    public static Set<ThemisSecurityLabel> getHarmoniaSecurityLabels(Resource resource) {
        if (!hasSecurityTag(resource)) {
            return Set.of();
        }
        Set<ThemisSecurityLabel> labels = new HashSet<>();
        for (Coding coding : resource.getMeta().getSecurity()) {
            if (coding.getSystem() != null && HarmoniaSecurityCodeSystem.SECURITY_LABEL_SYSTEM.equalsIgnoreCase(coding.getSystem()) && coding.hasCode()) {
                labels.add(ThemisSecurityLabel.of(coding.getSystem(), coding.getCode()));
            } else if (coding.hasCode() && HarmoniaSecurityLabelEnum.fromCode(coding.getCode()).isPresent()) {
                labels.add(ThemisSecurityLabel.of(HarmoniaSecurityCodeSystem.SECURITY_LABEL_SYSTEM, coding.getCode()));
            }
        }
        return labels;
    }
}
