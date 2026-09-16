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
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.Location;

import java.util.List;

/**
 * Task Processing Activity (Ergon) for validating, approving, and persisting Location change requests.
 */
@Dependent
public class LocationChangeErgon extends AbstractProviderRegistryChangeErgon<Location> {

    public static final String DEFAULT_ACTIVITY_ID = "location-change-ergon";
    public static final String DEFAULT_ACTIVITY_NAME = "Location Change Processing Activity";

    public LocationChangeErgon() {
        super(DEFAULT_ACTIVITY_ID, DEFAULT_ACTIVITY_NAME, Location.class, ProviderRegistryConstants.RESOURCE_LOCATION);
    }

    @Override
    protected void validateResource(Location location, Pragma pragma, List<String> errorMessages, List<String> errorCodes) {
        boolean hasName = StringUtils.isNotBlank(location.getName());
        boolean hasIdentifier = location.hasIdentifier() && !location.getIdentifier().isEmpty();

        if (!hasName && !hasIdentifier) {
            errorCodes.add(ProviderRegistryConstants.VAL_CODE_MISSING_FIELD);
            errorMessages.add("Location must have either a name or a business identifier");
        }
    }
}
