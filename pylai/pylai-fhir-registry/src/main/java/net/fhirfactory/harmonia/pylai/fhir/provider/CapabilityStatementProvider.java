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

package net.fhirfactory.harmonia.pylai.fhir.provider;

import net.fhirfactory.harmonia.model.registry.ProviderRegistryConstants;
import org.hl7.fhir.r5.model.CapabilityStatement;
import org.hl7.fhir.r5.model.Enumerations;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Generates the official FHIR R5 {@link CapabilityStatement} describing the Provider Registry REST API.
 */
@Component
public class CapabilityStatementProvider {

    /**
     * Builds a compliant FHIR R5 CapabilityStatement for Harmonia Provider Registry.
     */
    public CapabilityStatement buildCapabilityStatement() {
        CapabilityStatement cs = new CapabilityStatement();
        cs.setId("harmonia-provider-registry-capability");
        cs.setUrl("http://fhirfactory.net/harmonia/fhir/CapabilityStatement/provider-registry");
        cs.setVersion("1.0.0");
        cs.setName("HarmoniaProviderRegistryCapabilityStatement");
        cs.setTitle("Harmonia FHIR R5 Provider Registry Capability Statement");
        cs.setStatus(Enumerations.PublicationStatus.ACTIVE);
        cs.setExperimental(false);
        cs.setDate(new Date());
        cs.setPublisher("Harmonia HIE Integration Platform");
        cs.setDescription("Authoritative HL7 FHIR Release 5 Provider Directory and Governed Change Management Server");
        cs.setKind(Enumerations.CapabilityStatementKind.INSTANCE);
        cs.setFhirVersion(Enumerations.FHIRVersion._5_0_0);
        cs.addFormat("json");
        cs.addFormat("xml");

        CapabilityStatement.CapabilityStatementSoftwareComponent software = new CapabilityStatement.CapabilityStatementSoftwareComponent();
        software.setName("Harmonia Provider Registry");
        software.setVersion("1.0.0");
        software.setReleaseDate(new Date());
        cs.setSoftware(software);

        CapabilityStatement.CapabilityStatementRestComponent rest = new CapabilityStatement.CapabilityStatementRestComponent();
        rest.setMode(CapabilityStatement.RestfulCapabilityMode.SERVER);

        // Security declaration
        CapabilityStatement.CapabilityStatementRestSecurityComponent security = new CapabilityStatement.CapabilityStatementRestSecurityComponent();
        security.setCors(true);
        security.setDescription("Resource and Interaction-level RBAC authorization with OAuth2 / Token authentication");
        rest.setSecurity(security);

        // 1. Practitioner
        rest.addResource(createResourceComponent(
                "Practitioner",
                "Individual healthcare professional directory entry",
                List.of(
                        createSearchParam("_id", "_id", Enumerations.SearchParamType.TOKEN, "Logical resource identifier"),
                        createSearchParam("name", "Practitioner.name", Enumerations.SearchParamType.STRING, "Matches family, given, or text name"),
                        createSearchParam("identifier", "Practitioner.identifier", Enumerations.SearchParamType.TOKEN, "Business identifier (HPI-I, NPI)"),
                        createSearchParam("active", "Practitioner.active", Enumerations.SearchParamType.TOKEN, "Active status flag")
                )
        ));

        // 2. PractitionerRole
        rest.addResource(createResourceComponent(
                "PractitionerRole",
                "Roles and provider relationships performed by a Practitioner for an Organization",
                List.of(
                        createSearchParam("_id", "_id", Enumerations.SearchParamType.TOKEN, "Logical resource identifier"),
                        createSearchParam("identifier", "PractitionerRole.identifier", Enumerations.SearchParamType.TOKEN, "Business identifier"),
                        createSearchParam("practitioner", "PractitionerRole.practitioner", Enumerations.SearchParamType.REFERENCE, "Practitioner reference"),
                        createSearchParam("organization", "PractitionerRole.organization", Enumerations.SearchParamType.REFERENCE, "Organization reference"),
                        createSearchParam("location", "PractitionerRole.location", Enumerations.SearchParamType.REFERENCE, "Location reference"),
                        createSearchParam("service", "PractitionerRole.healthcareService", Enumerations.SearchParamType.REFERENCE, "HealthcareService reference"),
                        createSearchParam("active", "PractitionerRole.active", Enumerations.SearchParamType.TOKEN, "Active status flag")
                )
        ));

        // 3. Organization
        rest.addResource(createResourceComponent(
                "Organization",
                "Healthcare provider organization or clinical facility",
                List.of(
                        createSearchParam("_id", "_id", Enumerations.SearchParamType.TOKEN, "Logical resource identifier"),
                        createSearchParam("name", "Organization.name", Enumerations.SearchParamType.STRING, "Organization name or alias"),
                        createSearchParam("identifier", "Organization.identifier", Enumerations.SearchParamType.TOKEN, "Business identifier (HPI-O)"),
                        createSearchParam("active", "Organization.active", Enumerations.SearchParamType.TOKEN, "Active status flag")
                )
        ));

        // 4. Location
        rest.addResource(createResourceComponent(
                "Location",
                "Physical or logical healthcare service delivery location",
                List.of(
                        createSearchParam("_id", "_id", Enumerations.SearchParamType.TOKEN, "Logical resource identifier"),
                        createSearchParam("name", "Location.name", Enumerations.SearchParamType.STRING, "Location name or alias"),
                        createSearchParam("identifier", "Location.identifier", Enumerations.SearchParamType.TOKEN, "Business identifier"),
                        createSearchParam("organization", "Location.managingOrganization", Enumerations.SearchParamType.REFERENCE, "Managing organization"),
                        createSearchParam("status", "Location.status", Enumerations.SearchParamType.TOKEN, "Location status")
                )
        ));

        // 5. HealthcareService
        rest.addResource(createResourceComponent(
                "HealthcareService",
                "Clinical healthcare service offered by an organization and/or location",
                List.of(
                        createSearchParam("_id", "_id", Enumerations.SearchParamType.TOKEN, "Logical resource identifier"),
                        createSearchParam("name", "HealthcareService.name", Enumerations.SearchParamType.STRING, "Healthcare service name"),
                        createSearchParam("identifier", "HealthcareService.identifier", Enumerations.SearchParamType.TOKEN, "Business identifier"),
                        createSearchParam("organization", "HealthcareService.providedBy", Enumerations.SearchParamType.REFERENCE, "Providing organization"),
                        createSearchParam("location", "HealthcareService.location", Enumerations.SearchParamType.REFERENCE, "Service location"),
                        createSearchParam("active", "HealthcareService.active", Enumerations.SearchParamType.TOKEN, "Active status flag")
                )
        ));

        // 6. Endpoint
        rest.addResource(createResourceComponent(
                "Endpoint",
                "Electronic communication and interoperability technical destination",
                List.of(
                        createSearchParam("_id", "_id", Enumerations.SearchParamType.TOKEN, "Logical resource identifier"),
                        createSearchParam("name", "Endpoint.name", Enumerations.SearchParamType.STRING, "Endpoint description"),
                        createSearchParam("identifier", "Endpoint.identifier", Enumerations.SearchParamType.TOKEN, "Business identifier"),
                        createSearchParam("organization", "Endpoint.managingOrganization", Enumerations.SearchParamType.REFERENCE, "Managing organization"),
                        createSearchParam("status", "Endpoint.status", Enumerations.SearchParamType.TOKEN, "Endpoint operational status"),
                        createSearchParam("connection-type", "Endpoint.connectionType", Enumerations.SearchParamType.TOKEN, "Technical connection standard")
                )
        ));

        // 7. Group
        rest.addResource(createResourceComponent(
                "Group",
                "Logical grouping of providers or healthcare entities",
                List.of(
                        createSearchParam("_id", "_id", Enumerations.SearchParamType.TOKEN, "Logical resource identifier"),
                        createSearchParam("name", "Group.name", Enumerations.SearchParamType.STRING, "Group descriptive name"),
                        createSearchParam("identifier", "Group.identifier", Enumerations.SearchParamType.TOKEN, "Group business identifier"),
                        createSearchParam("type", "Group.type", Enumerations.SearchParamType.TOKEN, "Group entity type"),
                        createSearchParam("actual", "Group.membership", Enumerations.SearchParamType.TOKEN, "Definitional or enumerated membership")
                )
        ));

        // 8. Task (Read & Search only)
        CapabilityStatement.CapabilityStatementRestResourceComponent taskRes = new CapabilityStatement.CapabilityStatementRestResourceComponent();
        taskRes.setType("Task");
        taskRes.setProfile("http://hl7.org/fhir/StructureDefinition/Task");
        taskRes.addInteraction().setCode(CapabilityStatement.TypeRestfulInteraction.READ);
        taskRes.addInteraction().setCode(CapabilityStatement.TypeRestfulInteraction.SEARCHTYPE);
        taskRes.addSearchParam(createSearchParam("_id", "_id", Enumerations.SearchParamType.TOKEN, "Task logical identifier / PragmaId"));
        taskRes.addSearchParam(createSearchParam("identifier", "Task.identifier", Enumerations.SearchParamType.TOKEN, "Correlation or causation identifier"));
        rest.addResource(taskRes);

        cs.addRest(rest);
        return cs;
    }

    private CapabilityStatement.CapabilityStatementRestResourceComponent createResourceComponent(
            String type, String profileDescription, List<CapabilityStatement.CapabilityStatementRestResourceSearchParamComponent> searchParams) {

        CapabilityStatement.CapabilityStatementRestResourceComponent comp = new CapabilityStatement.CapabilityStatementRestResourceComponent();
        comp.setType(type);
        comp.setProfile("http://hl7.org/fhir/StructureDefinition/" + type);

        comp.addInteraction().setCode(CapabilityStatement.TypeRestfulInteraction.READ);
        comp.addInteraction().setCode(CapabilityStatement.TypeRestfulInteraction.SEARCHTYPE);
        comp.addInteraction().setCode(CapabilityStatement.TypeRestfulInteraction.CREATE);
        comp.addInteraction().setCode(CapabilityStatement.TypeRestfulInteraction.UPDATE);

        if (searchParams != null) {
            for (CapabilityStatement.CapabilityStatementRestResourceSearchParamComponent sp : searchParams) {
                comp.addSearchParam(sp);
            }
        }

        return comp;
    }

    private CapabilityStatement.CapabilityStatementRestResourceSearchParamComponent createSearchParam(
            String name, String definition, Enumerations.SearchParamType type, String documentation) {

        CapabilityStatement.CapabilityStatementRestResourceSearchParamComponent sp =
                new CapabilityStatement.CapabilityStatementRestResourceSearchParamComponent();
        sp.setName(name);
        sp.setDefinition(definition);
        sp.setType(type);
        sp.setDocumentation(documentation);
        return sp;
    }
}
