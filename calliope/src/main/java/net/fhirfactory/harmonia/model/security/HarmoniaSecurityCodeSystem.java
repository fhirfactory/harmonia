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

/**
 * Authoritative URI definitions for Harmonia security CodeSystems and value sets.
 */
public final class HarmoniaSecurityCodeSystem {

    private HarmoniaSecurityCodeSystem() {}

    public static final String SECURITY_LABEL_SYSTEM = "http://harmonia.fhirfactory.net/security/labels";
    public static final String ROLE_CODE_SYSTEM = "http://harmonia.fhirfactory.net/security/roles";
    public static final String AUTHORITY_CODE_SYSTEM = "http://harmonia.fhirfactory.net/security/authorities";
    public static final String PURPOSE_OF_USE_SYSTEM = "http://harmonia.fhirfactory.net/security/purpose-of-use";
    public static final String POLICY_SYSTEM = "http://harmonia.fhirfactory.net/security/policies";
}
