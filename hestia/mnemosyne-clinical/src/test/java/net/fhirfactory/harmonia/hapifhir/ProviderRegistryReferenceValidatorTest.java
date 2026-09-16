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

package net.fhirfactory.harmonia.hapifhir;

import net.fhirfactory.harmonia.hapifhir.service.FhirStorageService;
import net.fhirfactory.harmonia.hapifhir.service.ProviderRegistryReferenceValidator;
import net.fhirfactory.harmonia.hapifhir.service.ProviderRegistryReferenceValidator.ValidationResult;
import net.fhirfactory.harmonia.model.registry.ProviderRegistryConstants;
import org.hl7.fhir.r5.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ProviderRegistryReferenceValidatorTest {

    @Autowired
    private FhirStorageService storageService;

    @Autowired
    private ProviderRegistryReferenceValidator validator;

    @Test
    @DisplayName("Referential validation passes when all referenced targets exist and are active")
    void testValidReferences() {
        Practitioner pr = new Practitioner();
        pr.addName(new HumanName().setFamily("Hawking").addGiven("Stephen"));
        pr = storageService.createResource(pr);
        String prId = pr.getIdElement().getIdPart();

        Organization org = new Organization();
        org.setName("Cambridge Health");
        org = storageService.createResource(org);
        String orgId = org.getIdElement().getIdPart();

        PractitionerRole role = new PractitionerRole();
        role.setPractitioner(new Reference("Practitioner/" + prId));
        role.setOrganization(new Reference("Organization/" + orgId));

        ValidationResult result = validator.validateReferences(role);
        assertThat(result.isValid()).isTrue();
        assertThat(result.getErrorMessages()).isEmpty();
    }

    @Test
    @DisplayName("Referential validation fails when referenced Practitioner does not exist")
    void testMissingPractitionerReference() {
        Organization org = new Organization();
        org.setName("Oxford Health");
        org = storageService.createResource(org);
        String orgId = org.getIdElement().getIdPart();

        PractitionerRole role = new PractitionerRole();
        role.setPractitioner(new Reference("Practitioner/non-existent-id-999"));
        role.setOrganization(new Reference("Organization/" + orgId));

        ValidationResult result = validator.validateReferences(role);
        assertThat(result.isValid()).isFalse();
        assertThat(result.getErrorCodes()).contains(ProviderRegistryConstants.VAL_CODE_REFERENCE_NOT_FOUND);
        assertThat(result.getErrorMessages().get(0)).contains("non-existent-id-999");
    }

    @Test
    @DisplayName("Referential validation fails when referenced Organization is deleted")
    void testDeletedOrganizationReference() {
        Practitioner pr = new Practitioner();
        pr.addName(new HumanName().setFamily("Curie").addGiven("Marie"));
        pr = storageService.createResource(pr);
        String prId = pr.getIdElement().getIdPart();

        Organization org = new Organization();
        org.setName("Temporary Clinic");
        org = storageService.createResource(org);
        String orgId = org.getIdElement().getIdPart();
        storageService.deleteResource("Organization", orgId);

        PractitionerRole role = new PractitionerRole();
        role.setPractitioner(new Reference("Practitioner/" + prId));
        role.setOrganization(new Reference("Organization/" + orgId));

        ValidationResult result = validator.validateReferences(role);
        assertThat(result.isValid()).isFalse();
        assertThat(result.getErrorCodes()).contains(ProviderRegistryConstants.VAL_CODE_REFERENCE_INACTIVE);
    }
}
