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

package net.fhirfactory.harmonia.erga.registry;

import jakarta.enterprise.context.Dependent;
import net.fhirfactory.harmonia.model.pragma.Pragma;
import net.fhirfactory.harmonia.model.registry.ProviderRegistryConstants;
import org.hl7.fhir.r5.model.PractitionerRole;

import java.util.List;

/**
 * Task Processing Activity (Ergon) for validating, approving, and persisting PractitionerRole change requests.
 */
@Dependent
public class PractitionerRoleChangeErgon extends AbstractProviderRegistryChangeErgon<PractitionerRole> {

    public static final String DEFAULT_ACTIVITY_ID = "practitioner-role-change-ergon";
    public static final String DEFAULT_ACTIVITY_NAME = "PractitionerRole Change Processing Activity";

    public PractitionerRoleChangeErgon() {
        super(DEFAULT_ACTIVITY_ID, DEFAULT_ACTIVITY_NAME, PractitionerRole.class, ProviderRegistryConstants.RESOURCE_PRACTITIONER_ROLE);
    }

    @Override
    protected void validateResource(PractitionerRole role, Pragma pragma, List<String> errorMessages, List<String> errorCodes) {
        // PractitionerRole must specify at least practitioner or organization
        if (!role.hasPractitioner() && !role.hasOrganization()) {
            errorCodes.add(ProviderRegistryConstants.VAL_CODE_MISSING_FIELD);
            errorMessages.add("PractitionerRole must reference either a Practitioner or an Organization");
        }
    }
}
