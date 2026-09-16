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

package net.fhirfactory.harmonia.hapifhir.service;

import net.fhirfactory.harmonia.hapifhir.model.FhirResourceEntity;
import net.fhirfactory.harmonia.hapifhir.repository.FhirResourceRepository;
import net.fhirfactory.harmonia.model.registry.ProviderRegistryConstants;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service validating referential integrity and target existence for FHIR Provider Registry resources.
 */
@Service
public class ProviderRegistryReferenceValidator {

    private static final Logger log = LoggerFactory.getLogger(ProviderRegistryReferenceValidator.class);

    private final FhirResourceRepository repository;

    public ProviderRegistryReferenceValidator(FhirResourceRepository repository) {
        this.repository = repository;
    }

    public static class ValidationResult {
        private final boolean valid;
        private final List<String> errorMessages;
        private final List<String> errorCodes;

        public ValidationResult(boolean valid, List<String> errorMessages, List<String> errorCodes) {
            this.valid = valid;
            this.errorMessages = errorMessages != null ? errorMessages : new ArrayList<>();
            this.errorCodes = errorCodes != null ? errorCodes : new ArrayList<>();
        }

        public static ValidationResult success() {
            return new ValidationResult(true, new ArrayList<>(), new ArrayList<>());
        }

        public static ValidationResult valid() {
            return success();
        }

        public static ValidationResult failure(String code, String message) {
            List<String> codes = new ArrayList<>();
            codes.add(code);
            List<String> messages = new ArrayList<>();
            messages.add(message);
            return new ValidationResult(false, messages, codes);
        }

        public boolean isValid() {
            return valid;
        }

        public List<String> getErrorMessages() {
            return errorMessages;
        }

        public List<String> getErrorCodes() {
            return errorCodes;
        }

        public OperationOutcome toOperationOutcome() {
            OperationOutcome outcome = new OperationOutcome();
            if (valid) {
                outcome.addIssue()
                        .setSeverity(OperationOutcome.IssueSeverity.INFORMATION)
                        .setCode(OperationOutcome.IssueType.INFORMATIONAL)
                        .setDiagnostics("Validation successful");
            } else {
                for (int i = 0; i < errorMessages.size(); i++) {
                    String msg = errorMessages.get(i);
                    String code = (i < errorCodes.size()) ? errorCodes.get(i) : ProviderRegistryConstants.VAL_CODE_REFERENCE_NOT_FOUND;
                    outcome.addIssue()
                            .setSeverity(OperationOutcome.IssueSeverity.ERROR)
                            .setCode(OperationOutcome.IssueType.INVALID)
                            .setDiagnostics("[" + code + "] " + msg);
                }
            }
            return outcome;
        }
    }

    /**
     * Validates referential integrity for a given Provider Registry resource.
     *
     * @param resource FHIR R5 resource to validate
     * @return ValidationResult indicating success or list of validation violations
     */
    public ValidationResult validateReferences(IBaseResource resource) {
        if (resource == null) {
            return ValidationResult.failure(ProviderRegistryConstants.VAL_CODE_STRUCTURAL_ERROR, "Resource is null");
        }

        List<String> errorMessages = new ArrayList<>();
        List<String> errorCodes = new ArrayList<>();

        if (resource instanceof PractitionerRole role) {
            validatePractitionerRole(role, errorMessages, errorCodes);
        } else if (resource instanceof Location location) {
            validateLocation(location, errorMessages, errorCodes);
        } else if (resource instanceof HealthcareService service) {
            validateHealthcareService(service, errorMessages, errorCodes);
        } else if (resource instanceof Endpoint endpoint) {
            validateEndpoint(endpoint, errorMessages, errorCodes);
        } else if (resource instanceof Group group) {
            validateGroup(group, errorMessages, errorCodes);
        }

        if (errorMessages.isEmpty()) {
            return ValidationResult.success();
        } else {
            return new ValidationResult(false, errorMessages, errorCodes);
        }
    }

    private void validatePractitionerRole(PractitionerRole role, List<String> errorMessages, List<String> errorCodes) {
        // Practitioner reference
        if (role.hasPractitioner()) {
            checkReference(role.getPractitioner(), "Practitioner", "Practitioner", errorMessages, errorCodes);
        }

        // Organization reference
        if (role.hasOrganization()) {
            checkReference(role.getOrganization(), "Organization", "Organization", errorMessages, errorCodes);
        }

        // Location references
        if (role.hasLocation()) {
            for (Reference locRef : role.getLocation()) {
                checkReference(locRef, "Location", "Location", errorMessages, errorCodes);
            }
        }

        // HealthcareService references
        if (role.hasHealthcareService()) {
            for (Reference svcRef : role.getHealthcareService()) {
                checkReference(svcRef, "HealthcareService", "HealthcareService", errorMessages, errorCodes);
            }
        }

        // Endpoint references
        if (role.hasEndpoint()) {
            for (Reference epRef : role.getEndpoint()) {
                checkReference(epRef, "Endpoint", "Endpoint", errorMessages, errorCodes);
            }
        }
    }

    private void validateLocation(Location location, List<String> errorMessages, List<String> errorCodes) {
        if (location.hasManagingOrganization()) {
            checkReference(location.getManagingOrganization(), "Organization", "Managing Organization", errorMessages, errorCodes);
        }
        if (location.hasPartOf()) {
            checkReference(location.getPartOf(), "Location", "Parent Location", errorMessages, errorCodes);
        }
        if (location.hasEndpoint()) {
            for (Reference epRef : location.getEndpoint()) {
                checkReference(epRef, "Endpoint", "Endpoint", errorMessages, errorCodes);
            }
        }
    }

    private void validateHealthcareService(HealthcareService service, List<String> errorMessages, List<String> errorCodes) {
        if (service.hasProvidedBy()) {
            checkReference(service.getProvidedBy(), "Organization", "Provided By Organization", errorMessages, errorCodes);
        }
        if (service.hasLocation()) {
            for (Reference locRef : service.getLocation()) {
                checkReference(locRef, "Location", "Location", errorMessages, errorCodes);
            }
        }
        if (service.hasEndpoint()) {
            for (Reference epRef : service.getEndpoint()) {
                checkReference(epRef, "Endpoint", "Endpoint", errorMessages, errorCodes);
            }
        }
    }

    private void validateEndpoint(Endpoint endpoint, List<String> errorMessages, List<String> errorCodes) {
        if (endpoint.hasManagingOrganization()) {
            checkReference(endpoint.getManagingOrganization(), "Organization", "Managing Organization", errorMessages, errorCodes);
        }
    }

    private void validateGroup(Group group, List<String> errorMessages, List<String> errorCodes) {
        if (group.hasManagingEntity()) {
            checkReference(group.getManagingEntity(), null, "Managing Entity", errorMessages, errorCodes);
        }
        if (group.hasMember()) {
            for (Group.GroupMemberComponent member : group.getMember()) {
                if (member.hasEntity()) {
                    checkReference(member.getEntity(), null, "Group Member Entity", errorMessages, errorCodes);
                }
            }
        }
    }

    private void checkReference(Reference reference, String expectedType, String fieldLabel, List<String> errorMessages, List<String> errorCodes) {
        if (reference == null || StringUtils.isBlank(reference.getReference())) {
            return;
        }

        String refString = reference.getReference().trim();
        // Ignore contained or urn references
        if (refString.startsWith("#") || refString.startsWith("urn:")) {
            return;
        }

        // Strip versioned history if present (e.g. Organization/123/_history/1 -> Organization/123)
        if (refString.contains("/_history/")) {
            refString = refString.substring(0, refString.indexOf("/_history/"));
        }

        // Strip base URL if absolute URL is used
        if (refString.startsWith("http://") || refString.startsWith("https://")) {
            int lastSlash = refString.lastIndexOf('/');
            int prevSlash = refString.lastIndexOf('/', lastSlash - 1);
            if (prevSlash >= 0) {
                refString = refString.substring(prevSlash + 1);
            }
        }

        String targetType;
        String targetId;

        if (refString.contains("/")) {
            String[] parts = refString.split("/");
            targetType = parts[parts.length - 2];
            targetId = parts[parts.length - 1];
        } else if (expectedType != null) {
            targetType = expectedType;
            targetId = refString;
        } else {
            // Cannot infer resource type
            return;
        }

        if (expectedType != null && !expectedType.equalsIgnoreCase(targetType)) {
            errorCodes.add(ProviderRegistryConstants.VAL_CODE_STRUCTURAL_ERROR);
            errorMessages.add(fieldLabel + " reference '" + refString + "' is expected to be of type " + expectedType + " but was " + targetType);
            return;
        }

        Optional<FhirResourceEntity> entityOpt = repository.findByResourceTypeAndFhirId(targetType, targetId);
        if (entityOpt.isEmpty()) {
            errorCodes.add(ProviderRegistryConstants.VAL_CODE_REFERENCE_NOT_FOUND);
            errorMessages.add(fieldLabel + " referenced target '" + targetType + "/" + targetId + "' does not exist in Provider Registry");
            return;
        }

        FhirResourceEntity entity = entityOpt.get();
        if (entity.isDeleted()) {
            errorCodes.add(ProviderRegistryConstants.VAL_CODE_REFERENCE_INACTIVE);
            errorMessages.add(fieldLabel + " referenced target '" + targetType + "/" + targetId + "' is deleted/inactive in Provider Registry");
        }
    }
}
