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
import org.hl7.fhir.r5.model.HumanName;
import org.hl7.fhir.r5.model.Identifier;
import org.hl7.fhir.r5.model.Practitioner;

import java.util.List;

/**
 * Task Processing Activity (Ergon) for validating, approving, and persisting Practitioner change requests.
 */
@Dependent
public class PractitionerChangeErgon extends AbstractProviderRegistryChangeErgon<Practitioner> {

    public static final String DEFAULT_ACTIVITY_ID = "practitioner-change-ergon";
    public static final String DEFAULT_ACTIVITY_NAME = "Practitioner Change Processing Activity";

    public PractitionerChangeErgon() {
        super(DEFAULT_ACTIVITY_ID, DEFAULT_ACTIVITY_NAME, Practitioner.class, ProviderRegistryConstants.RESOURCE_PRACTITIONER);
    }

    @Override
    protected void validateResource(Practitioner practitioner, Pragma pragma, List<String> errorMessages, List<String> errorCodes) {
        // Require identifying name or identifier
        boolean hasName = false;
        if (practitioner.hasName()) {
            for (HumanName name : practitioner.getName()) {
                if (StringUtils.isNotBlank(name.getFamily()) || StringUtils.isNotBlank(name.getText()) || !name.getGiven().isEmpty()) {
                    hasName = true;
                    break;
                }
            }
        }

        boolean hasIdentifier = practitioner.hasIdentifier() && !practitioner.getIdentifier().isEmpty();

        if (!hasName && !hasIdentifier) {
            errorCodes.add(ProviderRegistryConstants.VAL_CODE_MISSING_FIELD);
            errorMessages.add("Practitioner must contain at least one valid name or business identifier");
        }

        // Duplicate identifier check across existing practitioners
        if (getStorageService() != null && hasIdentifier) {
            String currentId = practitioner.hasIdElement() ? practitioner.getIdElement().getIdPart() : null;
            for (Identifier ident : practitioner.getIdentifier()) {
                if (StringUtils.isNotBlank(ident.getValue())) {
                    List<Practitioner> matches = getStorageService().searchResources(ProviderRegistryConstants.RESOURCE_PRACTITIONER, null, null, ident.getValue());
                    for (Practitioner match : matches) {
                        String matchId = match.getIdElement().getIdPart();
                        if (currentId == null || !currentId.equalsIgnoreCase(matchId)) {
                            // If same system and value match another practitioner -> duplicate
                            for (Identifier existingIdent : match.getIdentifier()) {
                                if (ident.getValue().equalsIgnoreCase(existingIdent.getValue()) &&
                                    (ident.getSystem() == null || ident.getSystem().equalsIgnoreCase(existingIdent.getSystem()))) {
                                    errorCodes.add(ProviderRegistryConstants.VAL_CODE_DUPLICATE_IDENTIFIER);
                                    errorMessages.add("Duplicate business identifier [" + ident.getValue() + "] already assigned to Practitioner/" + matchId);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
