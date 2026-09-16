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

package net.fhirfactory.harmonia.model.pragma;

import net.fhirfactory.harmonia.model.registry.ProviderRegistryConstants;
import org.hl7.fhir.r5.model.HumanName;
import org.hl7.fhir.r5.model.OperationOutcome;
import org.hl7.fhir.r5.model.Practitioner;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProviderRegistryChangePragmaTest {

    @Test
    void testBuildChangeRequestPragma() {
        Practitioner practitioner = new Practitioner();
        practitioner.setId("pract-123");
        practitioner.addName(new HumanName().setFamily("Smith").addGiven("John"));

        Pragma pragma = ProviderRegistryChangePragma.buildChangeRequestPragma(
                ProviderRegistryConstants.OPERATION_CREATE,
                practitioner,
                "user-1",
                "test-system",
                "corr-999",
                "W/\"1\""
        );

        assertNotNull(pragma);
        assertEquals("corr-999", pragma.getCorrelationId());
        assertEquals(PragmaStatus.ACCEPTED, pragma.getStatus());
        assertEquals(ProviderRegistryConstants.PRAXIS_PROVIDER_REGISTRY_CHANGE_PIPELINE, pragma.getPraxisId());
        assertEquals("CREATE", pragma.getMetadata().get(ProviderRegistryConstants.METADATA_OPERATION));
        assertEquals("Practitioner", pragma.getMetadata().get(ProviderRegistryConstants.METADATA_RESOURCE_TYPE));
        assertEquals("pract-123", pragma.getMetadata().get(ProviderRegistryConstants.METADATA_RESOURCE_ID));
        assertEquals("W/\"1\"", pragma.getMetadata().get(ProviderRegistryConstants.METADATA_IF_MATCH));

        ProviderRegistryChangePragma wrapper = new ProviderRegistryChangePragma(pragma);
        assertEquals("CREATE", wrapper.getOperation());
        assertEquals("Practitioner", wrapper.getResourceType());
        assertEquals("pract-123", wrapper.getResourceId());
        assertEquals("user-1", wrapper.getRequester());
        assertEquals("test-system", wrapper.getSourceSystem());
        assertEquals("W/\"1\"", wrapper.getIfMatch());

        assertNotNull(wrapper.getRequestedResource());
        assertTrue(wrapper.getRequestedResource() instanceof Practitioner);
        Practitioner extracted = (Practitioner) wrapper.getRequestedResource();
        assertEquals("Smith", extracted.getNameFirstRep().getFamily());
    }

    @Test
    void testAttachOutcome() {
        Practitioner practitioner = new Practitioner();
        practitioner.setId("pract-456");

        Pragma pragma = ProviderRegistryChangePragma.buildChangeRequestPragma(
                ProviderRegistryConstants.OPERATION_UPDATE,
                practitioner,
                "user-2",
                "test-system",
                null,
                null
        );

        OperationOutcome outcome = new OperationOutcome();
        outcome.addIssue()
                .setSeverity(OperationOutcome.IssueSeverity.ERROR)
                .setCode(OperationOutcome.IssueType.NOTFOUND)
                .setDiagnostics("Referenced Organization does not exist");

        ProviderRegistryChangePragma.attachOutcome(
                pragma,
                outcome,
                PragmaStatus.REJECTED,
                "VALIDATE_REFERENCES",
                "Referential check failed"
        );

        assertEquals(PragmaStatus.REJECTED, pragma.getStatus());
        assertEquals(1, pragma.getOutput().size());
        assertTrue(pragma.getOutput().get(0).isFhirResource());
        assertEquals(2, pragma.getCheckpoints().size());
        assertEquals("VALIDATE_REFERENCES", pragma.getCheckpoints().get(1).getStageName());
        assertEquals(PragmaStatus.REJECTED, pragma.getCheckpoints().get(1).getStatus());
    }

    @Test
    void testSupportedResourceTypes() {
        assertTrue(ProviderRegistryConstants.isSupportedResourceType("Practitioner"));
        assertTrue(ProviderRegistryConstants.isSupportedResourceType("PractitionerRole"));
        assertTrue(ProviderRegistryConstants.isSupportedResourceType("Organization"));
        assertTrue(ProviderRegistryConstants.isSupportedResourceType("Location"));
        assertTrue(ProviderRegistryConstants.isSupportedResourceType("HealthcareService"));
        assertTrue(ProviderRegistryConstants.isSupportedResourceType("Endpoint"));
        assertTrue(ProviderRegistryConstants.isSupportedResourceType("Group"));
        assertFalse(ProviderRegistryConstants.isSupportedResourceType("Patient"));
    }
}
