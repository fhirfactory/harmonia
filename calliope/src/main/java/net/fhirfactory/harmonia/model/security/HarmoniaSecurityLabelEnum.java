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
import org.hl7.fhir.r5.model.Coding;

import java.util.Optional;

/**
 * Enumeration of standardized data security labels across Harmonia.
 * Maps directly to FHIR {@code meta.security} Codings and {@link ThemisSecurityLabel} instances.
 */
public enum HarmoniaSecurityLabelEnum {
    PROVIDER_REGISTRY("PROVIDER_REGISTRY", "Provider Registry Resource"),
    INTERNAL("INTERNAL", "Internal Platform Resource"),
    AUDIT("AUDIT", "Security Audit Record"),
    RESTRICTED("RESTRICTED", "Restricted Clinical Resource"),
    CLINICAL("CLINICAL", "General Clinical Resource"),
    ADMINISTRATIVE("ADMINISTRATIVE", "Administrative Resource");

    private final String code;
    private final String display;

    HarmoniaSecurityLabelEnum(String code, String display) {
        this.code = code;
        this.display = display;
    }

    public String getCode() {
        return code;
    }

    public String getDisplay() {
        return display;
    }

    public String getSystem() {
        return HarmoniaSecurityCodeSystem.SECURITY_LABEL_SYSTEM;
    }

    public ThemisSecurityLabel toThemisSecurityLabel() {
        return ThemisSecurityLabel.of(getSystem(), code);
    }

    public ThemisSecurityLabel toThemisLabel() {
        return toThemisSecurityLabel();
    }

    public Coding toCoding() {
        return new Coding(getSystem(), code, display);
    }

    public static Optional<HarmoniaSecurityLabelEnum> fromCode(String code) {
        if (code == null) {
            return Optional.empty();
        }
        for (HarmoniaSecurityLabelEnum label : values()) {
            if (label.code.equalsIgnoreCase(code.trim())) {
                return Optional.of(label);
            }
        }
        return Optional.empty();
    }

    public static Optional<HarmoniaSecurityLabelEnum> fromCoding(Coding coding) {
        if (coding == null || coding.getCode() == null) {
            return Optional.empty();
        }
        if (coding.getSystem() != null && !HarmoniaSecurityCodeSystem.SECURITY_LABEL_SYSTEM.equalsIgnoreCase(coding.getSystem())) {
            return Optional.empty();
        }
        return fromCode(coding.getCode());
    }
}
