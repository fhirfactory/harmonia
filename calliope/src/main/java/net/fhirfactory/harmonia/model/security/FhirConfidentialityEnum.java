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

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.CodeableConcept;
import org.hl7.fhir.r5.model.Coding;
import org.hl7.fhir.r5.model.Resource;

import java.util.Arrays;
import java.util.Optional;

/**
 * Enumeration of HL7 v3 Confidentiality classification codes used for FHIR Release 5
 * security tagging ({@code meta.security}).
 * <p>
 * Standard vocabulary: {@code http://terminology.hl7.org/CodeSystem/v3-Confidentiality}
 */
public enum FhirConfidentialityEnum {

    /**
     * Normal: Normal confidentiality rules applied, routine clinical data.
     */
    N(
            "N",
            "Normal",
            "Normal confidentiality rules applied, routine clinical data."
    ),

    /**
     * Restricted: Restricted confidentiality rules applied.
     */
    R(
            "R",
            "Restricted",
            "Restricted confidentiality rules applied."
    ),

    /**
     * Very Restricted: Very restricted confidentiality rules applied.
     */
    V(
            "V",
            "Very Restricted",
            "Very restricted confidentiality rules applied."
    ),

    /**
     * Unrestricted: Unrestricted confidentiality rules applied.
     */
    U(
            "U",
            "Unrestricted",
            "Unrestricted confidentiality rules applied."
    ),

    /**
     * Low: Low confidentiality rules applied.
     */
    L(
            "L",
            "Low",
            "Low confidentiality rules applied."
    ),

    /**
     * Moderate: Moderate confidentiality rules applied.
     */
    M(
            "M",
            "Moderate",
            "Moderate confidentiality rules applied."
    );

    public static final String CONFIDENTIALITY_SYSTEM = "http://terminology.hl7.org/CodeSystem/v3-Confidentiality";

    private final String code;
    private final String display;
    private final String description;

    FhirConfidentialityEnum(String code, String display, String description) {
        this.code = code;
        this.display = display;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDisplay() {
        return display;
    }

    public String getDescription() {
        return description;
    }

    public String getSystem() {
        return CONFIDENTIALITY_SYSTEM;
    }

    /**
     * Converts this confidentiality code into a FHIR R5 {@link Coding}.
     *
     * @return populated {@link Coding} with standard HL7 v3 Confidentiality system, code, and display
     */
    public Coding toCoding() {
        Coding coding = new Coding();
        coding.setSystem(CONFIDENTIALITY_SYSTEM);
        coding.setCode(code);
        coding.setDisplay(display);
        return coding;
    }

    /**
     * Converts this confidentiality code into a FHIR R5 {@link CodeableConcept}.
     *
     * @return populated {@link CodeableConcept}
     */
    public CodeableConcept toCodeableConcept() {
        CodeableConcept concept = new CodeableConcept();
        concept.setText(display);
        concept.addCoding(toCoding());
        return concept;
    }

    /**
     * Applies this confidentiality coding to the {@code meta.security} list of the given FHIR resource
     * if not already present.
     *
     * @param resource the FHIR Resource to update
     * @return the updated Resource
     */
    public Resource applyTo(Resource resource) {
        if (resource == null) {
            return null;
        }
        if (!resource.hasMeta()) {
            resource.setMeta(new org.hl7.fhir.r5.model.Meta());
        }
        boolean alreadyPresent = resource.getMeta().getSecurity().stream().anyMatch(c ->
                CONFIDENTIALITY_SYSTEM.equalsIgnoreCase(c.getSystem()) && code.equalsIgnoreCase(c.getCode())
        );
        if (!alreadyPresent) {
            resource.getMeta().addSecurity(toCoding());
        }
        return resource;
    }

    /**
     * Finds a {@link FhirConfidentialityEnum} by its code, display name, or enum name.
     *
     * @param code text code or display name
     * @return optional containing the matching enum if found
     */
    public static Optional<FhirConfidentialityEnum> fromCode(String code) {
        if (StringUtils.isBlank(code)) {
            return Optional.empty();
        }
        String clean = code.trim();
        return Arrays.stream(values())
                .filter(c -> c.code.equalsIgnoreCase(clean)
                        || c.display.equalsIgnoreCase(clean)
                        || c.name().equalsIgnoreCase(clean))
                .findFirst();
    }

    /**
     * Finds a {@link FhirConfidentialityEnum} matching the given {@link Coding}.
     *
     * @param coding the FHIR Coding to inspect
     * @return optional containing matching enum if coding system matches and code is recognized
     */
    public static Optional<FhirConfidentialityEnum> fromCoding(Coding coding) {
        if (coding == null) {
            return Optional.empty();
        }
        if (StringUtils.isBlank(coding.getSystem()) || CONFIDENTIALITY_SYSTEM.equalsIgnoreCase(coding.getSystem())) {
            return fromCode(coding.getCode());
        }
        return Optional.empty();
    }

    @Override
    public String toString() {
        return code;
    }
}
