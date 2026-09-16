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

package net.fhirfactory.harmonia.model.registry;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Canonical constants and validation codes for the Harmonia FHIR Provider Registry.
 */
public final class ProviderRegistryConstants {

    private ProviderRegistryConstants() {
        // Constants class
    }

    // Queue and Topic Definitions
    public static final String QUEUE_PROVIDER_REGISTRY_CHANGE_REQUEST = "harmonia.provider.registry.change.request";
    public static final String TOPIC_PROVIDER_REGISTRY_CHANGE_REQUEST = "harmonia.provider.registry.change.request";
    public static final String TOPIC_PROVIDER_REGISTRY_CHANGE_EVENT = "harmonia.provider.registry.change.event";

    // Praxis Sequence Identifier
    public static final String PRAXIS_PROVIDER_REGISTRY_CHANGE_PIPELINE = "seq-provider-registry-change-pipeline";

    // Operations
    public static final String OPERATION_CREATE = "CREATE";
    public static final String OPERATION_UPDATE = "UPDATE";

    // Metadata Keys
    public static final String METADATA_OPERATION = "operation";
    public static final String METADATA_RESOURCE_TYPE = "resourceType";
    public static final String METADATA_RESOURCE_ID = "resourceId";
    public static final String METADATA_REQUESTER = "requester";
    public static final String METADATA_SOURCE_SYSTEM = "sourceSystem";
    public static final String METADATA_SUBMITTED_AT = "submittedAt";
    public static final String METADATA_CORRELATION_ID = "correlationId";
    public static final String METADATA_VALIDATION_OUTCOME = "validationOutcome";
    public static final String METADATA_PROCESSING_OUTCOME = "processingOutcome";
    public static final String METADATA_RESULTING_VERSION = "resultingVersion";
    public static final String METADATA_IF_MATCH = "ifMatch";

    // Validation & Error Codes
    public static final String VAL_CODE_STRUCTURAL_ERROR = "PR-VAL-001";
    public static final String VAL_CODE_MISSING_FIELD = "PR-VAL-002";
    public static final String VAL_CODE_DUPLICATE_IDENTIFIER = "PR-VAL-003";
    public static final String VAL_CODE_REFERENCE_NOT_FOUND = "PR-VAL-004";
    public static final String VAL_CODE_REFERENCE_INACTIVE = "PR-VAL-005";
    public static final String VAL_CODE_CONCURRENCY_CONFLICT = "PR-VAL-006";
    public static final String VAL_CODE_INVALID_ENDPOINT = "PR-VAL-007";
    public static final String VAL_CODE_INVALID_GROUP = "PR-VAL-008";
    public static final String VAL_CODE_UNSUPPORTED_RESOURCE = "PR-VAL-009";
    public static final String VAL_CODE_BUSINESS_RULE_FAILURE = "PR-VAL-010";

    // Supported FHIR R5 Resource Types
    public static final String RESOURCE_PRACTITIONER = "Practitioner";
    public static final String RESOURCE_PRACTITIONER_ROLE = "PractitionerRole";
    public static final String RESOURCE_ORGANIZATION = "Organization";
    public static final String RESOURCE_LOCATION = "Location";
    public static final String RESOURCE_HEALTHCARE_SERVICE = "HealthcareService";
    public static final String RESOURCE_ENDPOINT = "Endpoint";
    public static final String RESOURCE_GROUP = "Group";

    public static final List<String> SUPPORTED_RESOURCE_TYPES = Collections.unmodifiableList(Arrays.asList(
            RESOURCE_PRACTITIONER,
            RESOURCE_PRACTITIONER_ROLE,
            RESOURCE_ORGANIZATION,
            RESOURCE_LOCATION,
            RESOURCE_HEALTHCARE_SERVICE,
            RESOURCE_ENDPOINT,
            RESOURCE_GROUP
    ));

    public static boolean isSupportedResourceType(String resourceType) {
        return resourceType != null && SUPPORTED_RESOURCE_TYPES.contains(resourceType);
    }
}
