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
 * Deterministic generator for FHIR R5 Location resources representing healthcare facilities, wings, and clinics.
 */
public class LocationGenerator {

    private static final String[] LOCATION_NAMES = {
            "Cardiology Outpatient Clinic", "Intensive Care Unit - Level 4", "Emergency Assessment Bay 3",
            "Surgical Suite - Theatre 2", "Radiology Imaging Wing", "Oncology Day Treatment Centre",
            "Pathology Specimen Collection Lab", "Pediatric Ambulatory Care"
    };

    private static final String[] PHYSICAL_TYPES = {
            "wi", "ro", "bu", "ca", "area"
    };

    private final SeedRandom random;

    public LocationGenerator() {
        this(new SeedRandom());
    }

    public LocationGenerator(long seed) {
        this(new SeedRandom(seed));
    }

    public LocationGenerator(SeedRandom random) {
        this.random = random != null ? random : new SeedRandom();
    }

    public Location generate(String id, String managingOrgId, String endpointId) {
        return generateValid(id, managingOrgId, endpointId);
    }

    public Location generateValid(String id, String managingOrgId, String endpointId) {
        String locId = id != null ? id : "loc-" + random.nextInt(10000, 99999);
        String name = random.pick(LOCATION_NAMES) + " (" + random.nextInt(10, 99) + ")";

        Location location = new Location();
        location.setId(new IdType("Location", locId));
        location.setName(name);
        location.setStatus(Location.LocationStatus.ACTIVE);
        location.setMode(Location.LocationMode.INSTANCE);

        location.addIdentifier(new Identifier()
                .setSystem("urn:harmonia:location:identifier")
                .setValue("LOC-" + random.nextInt(1000, 9999)));

        location.setForm(new CodeableConcept().addCoding(
                new Coding("http://terminology.hl7.org/CodeSystem/location-physical-type", random.pick(PHYSICAL_TYPES), "Physical Type")
        ));

        if (managingOrgId != null) {
            String ref = managingOrgId.startsWith("Organization/") ? managingOrgId : "Organization/" + managingOrgId;
            location.setManagingOrganization(new Reference(ref));
        }

        if (endpointId != null) {
            String ref = endpointId.startsWith("Endpoint/") ? endpointId : "Endpoint/" + endpointId;
            location.addEndpoint(new Reference(ref));
        }

        Meta meta = new Meta();
        meta.setVersionId("1");
        meta.setLastUpdated(new Date());
        location.setMeta(meta);

        return location;
    }

    public Location generateIncomplete(String id) {
        String locId = id != null ? id : "loc-inc-" + random.nextInt(10000, 99999);
        Location location = new Location();
        location.setId(new IdType("Location", locId));
        location.setStatus(Location.LocationStatus.ACTIVE);
        // Missing name, physical type, and managing organization
        return location;
    }

    public Location generateInvalid(String id) {
        String locId = id != null ? id : "loc-inv-" + random.nextInt(10000, 99999);
        Location location = new Location();
        location.setId(new IdType("Location", locId));
        location.setStatus(Location.LocationStatus.NULL);
        location.setName("");
        return location;
    }

    public Location generateBrokenReference(String id) {
        return generateValid(id, "Organization/non-existent-org-99999", "Endpoint/non-existent-ep-99999");
    }

    public Location generateInactive(String id, String managingOrgId) {
        Location location = generateValid(id, managingOrgId, null);
        location.setStatus(Location.LocationStatus.INACTIVE);
        return location;
    }

    public Location generateUpdate(Location existing, int newVersion) {
        Location updated = existing.copy();
        if (updated.getMeta() == null) {
            updated.setMeta(new Meta());
        }
        updated.getMeta().setVersionId(String.valueOf(newVersion));
        updated.getMeta().setLastUpdated(new Date());
        updated.setName(existing.getName() + " - Renovated Wing");
        return updated;
    }

    public SeedRandom getRandom() {
        return random;
    }
}
