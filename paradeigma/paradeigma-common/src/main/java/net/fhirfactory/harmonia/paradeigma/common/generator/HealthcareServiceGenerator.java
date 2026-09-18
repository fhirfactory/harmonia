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

/**
 * Deterministic generator for FHIR R5 HealthcareService resources.
 */
public class HealthcareServiceGenerator {

    private static final String[] SERVICE_NAMES = {
            "Comprehensive Cardiology Consultation", "Adult Emergency Medicine Service",
            "General Surgical Pre-Admission Clinic", "Diagnostic Radiography & MRI Service",
            "Clinical Hematology & Oncology Outpatients", "Respiratory Function Assessment Service"
    };

    private static final String[] CATEGORIES = {
            "Cardiology", "Emergency", "Surgical", "Radiology", "Oncology", "Respiratory"
    };

    private final SeedRandom random;

    public HealthcareServiceGenerator() {
        this(new SeedRandom());
    }

    public HealthcareServiceGenerator(long seed) {
        this(new SeedRandom(seed));
    }

    public HealthcareServiceGenerator(SeedRandom random) {
        this.random = random != null ? random : new SeedRandom();
    }

    public HealthcareService generate(String id, String orgId, String locId, String epId) {
        return generateValid(id, orgId, locId, epId);
    }

    public HealthcareService generateValid(String id, String orgId, String locId, String epId) {
        String svcId = id != null ? id : "svc-" + random.nextInt(10000, 99999);
        int idx = random.nextInt(0, SERVICE_NAMES.length - 1);
        String name = SERVICE_NAMES[idx] + " (" + random.nextInt(10, 99) + ")";

        HealthcareService service = new HealthcareService();
        service.setId(new IdType("HealthcareService", svcId));
        service.setName(name);
        service.setActive(true);

        service.addIdentifier(new Identifier()
                .setSystem("urn:harmonia:service:identifier")
                .setValue("SVC-" + random.nextInt(1000, 9999)));

        service.addCategory(new CodeableConcept().setText(CATEGORIES[idx]));

        if (orgId != null) {
            String ref = orgId.startsWith("Organization/") ? orgId : "Organization/" + orgId;
            service.setProvidedBy(new Reference(ref));
        }

        if (locId != null) {
            String ref = locId.startsWith("Location/") ? locId : "Location/" + locId;
            service.addLocation(new Reference(ref));
        }

        if (epId != null) {
            String ref = epId.startsWith("Endpoint/") ? epId : "Endpoint/" + epId;
            service.addEndpoint(new Reference(ref));
        }

        service.addContact(new ExtendedContactDetail()
                .addTelecom(new ContactPoint()
                        .setSystem(ContactPoint.ContactPointSystem.PHONE)
                        .setValue("+61 2 " + random.nextInt(9000, 9999) + " 1111")
                        .setUse(ContactPoint.ContactPointUse.WORK)));

        Meta meta = new Meta();
        meta.setVersionId("1");
        meta.setLastUpdated(new Date());
        service.setMeta(meta);

        return service;
    }

    public HealthcareService generateIncomplete(String id) {
        String svcId = id != null ? id : "svc-inc-" + random.nextInt(10000, 99999);
        HealthcareService service = new HealthcareService();
        service.setId(new IdType("HealthcareService", svcId));
        service.setActive(true);
        // Missing name and organization reference
        return service;
    }

    public HealthcareService generateInvalid(String id) {
        String svcId = id != null ? id : "svc-inv-" + random.nextInt(10000, 99999);
        HealthcareService service = new HealthcareService();
        service.setId(new IdType("HealthcareService", svcId));
        service.setName("");
        service.setActive(false);
        return service;
    }

    public HealthcareService generateBrokenReference(String id) {
        return generateValid(id, "Organization/non-existent-org-99999", "Location/non-existent-loc-99999", "Endpoint/non-existent-ep-99999");
    }

    public HealthcareService generateInactive(String id, String orgId) {
        HealthcareService service = generateValid(id, orgId, null, null);
        service.setActive(false);
        return service;
    }

    public HealthcareService generateUpdate(HealthcareService existing, int newVersion) {
        HealthcareService updated = existing.copy();
        if (updated.getMeta() == null) {
            updated.setMeta(new Meta());
        }
        updated.getMeta().setVersionId(String.valueOf(newVersion));
        updated.getMeta().setLastUpdated(new Date());
        updated.setName(existing.getName() + " - Expanded Clinic Hours");
        return updated;
    }

    public SeedRandom getRandom() {
        return random;
    }
}
