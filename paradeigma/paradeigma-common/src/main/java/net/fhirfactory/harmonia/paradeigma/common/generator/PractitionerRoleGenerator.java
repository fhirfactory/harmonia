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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package net.fhirfactory.harmonia.paradeigma.common.generator;

import org.hl7.fhir.r5.model.*;

import java.util.Date;
import java.util.List;

/**
 * Deterministic generator for FHIR R5 PractitionerRole resources linking Practitioners,
 * Organizations, Locations, HealthcareServices, and Endpoints.
 */
public class PractitionerRoleGenerator {

    public static final String SYSTEM_PRACTITIONER_ROLE = "http://terminology.hl7.org/CodeSystem/practitioner-role";
    public static final String SYSTEM_SNOMED = "http://snomed.info/sct";

    private static final String[] ROLE_CODES = {
            "doctor", "nurse", "pharmacist", "researcher", "teacher", "specialist"
    };

    private static final String[] SPECIALTY_SNOMED_CODES = {
            "394579002", "394582007", "394802001", "394583002", "394585009"
    };

    private static final String[] SPECIALTY_NAMES = {
            "Cardiology", "Dermatology", "General Medicine", "Endocrinology", "Obstetrics & Gynaecology"
    };

    private final SeedRandom random;

    public PractitionerRoleGenerator() {
        this(new SeedRandom());
    }

    public PractitionerRoleGenerator(long seed) {
        this(new SeedRandom(seed));
    }

    public PractitionerRoleGenerator(SeedRandom random) {
        this.random = random != null ? random : new SeedRandom();
    }

    public PractitionerRole generate(String id, String practId, String orgId, String locId, String svcId, String epId) {
        return generateValid(id, practId, orgId, locId, svcId, epId);
    }

    public PractitionerRole generateValid(String id, String practId, String orgId, String locId, String svcId, String epId) {
        String roleId = id != null ? id : "role-" + random.nextInt(10000, 99999);
        int specIdx = random.nextInt(0, SPECIALTY_NAMES.length - 1);
        String roleCode = random.pick(ROLE_CODES);

        PractitionerRole role = new PractitionerRole();
        role.setId(new IdType("PractitionerRole", roleId));
        role.setActive(true);

        if (practId != null) {
            String ref = practId.startsWith("Practitioner/") ? practId : "Practitioner/" + practId;
            role.setPractitioner(new Reference(ref));
        }

        if (orgId != null) {
            String ref = orgId.startsWith("Organization/") ? orgId : "Organization/" + orgId;
            role.setOrganization(new Reference(ref));
        }

        if (locId != null) {
            String ref = locId.startsWith("Location/") ? locId : "Location/" + locId;
            role.addLocation(new Reference(ref));
        }

        if (svcId != null) {
            String ref = svcId.startsWith("HealthcareService/") ? svcId : "HealthcareService/" + svcId;
            role.addHealthcareService(new Reference(ref));
        }

        if (epId != null) {
            String ref = epId.startsWith("Endpoint/") ? epId : "Endpoint/" + epId;
            role.addEndpoint(new Reference(ref));
        }

        role.addCode(new CodeableConcept().addCoding(
                new Coding(SYSTEM_PRACTITIONER_ROLE, roleCode, roleCode.substring(0, 1).toUpperCase() + roleCode.substring(1))
        ).setText("Lead Consultant " + SPECIALTY_NAMES[specIdx]));

        role.addSpecialty(new CodeableConcept().addCoding(
                new Coding(SYSTEM_SNOMED, SPECIALTY_SNOMED_CODES[specIdx], SPECIALTY_NAMES[specIdx])
        ).setText(SPECIALTY_NAMES[specIdx]));

        role.addIdentifier(new Identifier()
                .setSystem("urn:harmonia:role:identifier")
                .setValue("ROLE-" + random.nextInt(1000, 9999)));

        role.addContact(new ExtendedContactDetail()
                .addTelecom(new ContactPoint()
                        .setSystem(ContactPoint.ContactPointSystem.EMAIL)
                        .setValue("practitioner.role." + roleId + "@health.example.org")
                        .setUse(ContactPoint.ContactPointUse.WORK)));

        Meta meta = new Meta();
        meta.setVersionId("1");
        meta.setLastUpdated(new Date());
        role.setMeta(meta);

        return role;
    }

    public PractitionerRole generateIncomplete(String id) {
        String roleId = id != null ? id : "role-inc-" + random.nextInt(10000, 99999);
        PractitionerRole role = new PractitionerRole();
        role.setId(new IdType("PractitionerRole", roleId));
        role.setActive(true);
        // Missing practitioner and organization references
        return role;
    }

    public PractitionerRole generateInvalid(String id) {
        String roleId = id != null ? id : "role-inv-" + random.nextInt(10000, 99999);
        PractitionerRole role = new PractitionerRole();
        role.setId(new IdType("PractitionerRole", roleId));
        role.setActive(false);
        role.addIdentifier(new Identifier().setSystem("urn:invalid:role").setValue("INVALID_ROLE_ID"));
        return role;
    }

    public PractitionerRole generateBrokenReference(String id, String practId) {
        return generateValid(
                id,
                practId,
                "Organization/non-existent-org-99999",
                "Location/non-existent-loc-99999",
                "HealthcareService/non-existent-svc-99999",
                "Endpoint/non-existent-ep-99999"
        );
    }

    public PractitionerRole generateInactive(String id, String practId, String orgId) {
        PractitionerRole role = generateValid(id, practId, orgId, null, null, null);
        role.setActive(false);
        return role;
    }

    public PractitionerRole generateUpdate(PractitionerRole existing, int newVersion) {
        PractitionerRole updated = existing.copy();
        if (updated.getMeta() == null) {
            updated.setMeta(new Meta());
        }
        updated.getMeta().setVersionId(String.valueOf(newVersion));
        updated.getMeta().setLastUpdated(new Date());
        return updated;
    }

    public SeedRandom getRandom() {
        return random;
    }
}
