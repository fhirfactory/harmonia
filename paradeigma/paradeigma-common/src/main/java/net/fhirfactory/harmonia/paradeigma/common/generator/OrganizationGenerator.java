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
 * Deterministic generator for FHIR R5 Organization resources with Australian HPI-O and ABN identifiers.
 */
public class OrganizationGenerator {

    public static final String SYSTEM_HPI_O = "http://ns.electronichealth.net.au/id/hi/hpio/1.0";
    public static final String SYSTEM_ABN = "http://hl7.org.au/id/abn";
    public static final String SYSTEM_ORG_TYPE = "http://terminology.hl7.org/CodeSystem/organization-type";

    private static final String[] HEALTH_NETWORKS = {
            "St Vincent's Health Network", "Metro South Health Service", "Alfred Health Network",
            "Monash Health Consortium", "Royal North Shore Area Health", "Western Sydney Local Health"
    };

    private static final String[] SUBURBS = {
            "Darlinghurst", "Woolloongabba", "Melbourne", "Clayton", "St Leonards", "Westmead"
    };

    private static final String[] STATES = {
            "NSW", "QLD", "VIC", "VIC", "NSW", "NSW"
    };

    private final SeedRandom random;

    public OrganizationGenerator() {
        this(new SeedRandom());
    }

    public OrganizationGenerator(long seed) {
        this(new SeedRandom(seed));
    }

    public OrganizationGenerator(SeedRandom random) {
        this.random = random != null ? random : new SeedRandom();
    }

    public Organization generate(String id) {
        return generateValid(id);
    }

    public Organization generateValid(String id) {
        String orgId = id != null ? id : "org-" + random.nextInt(10000, 99999);
        int idx = random.nextInt(0, HEALTH_NETWORKS.length - 1);
        String name = HEALTH_NETWORKS[idx] + " (" + random.nextInt(10, 99) + ")";
        long hpioNum = 8003620000000000L + random.nextInt(100000, 999999);

        Organization org = new Organization();
        org.setId(new IdType("Organization", orgId));
        org.setName(name);
        org.setActive(true);

        org.addIdentifier(new Identifier()
                .setSystem(SYSTEM_HPI_O)
                .setValue(String.valueOf(hpioNum))
                .setUse(Identifier.IdentifierUse.OFFICIAL));

        org.addIdentifier(new Identifier()
                .setSystem(SYSTEM_ABN)
                .setValue(String.valueOf(random.nextLong(10000000000L, 99999999999L))));

        org.addType(new CodeableConcept().addCoding(
                new Coding(SYSTEM_ORG_TYPE, "prov", "Healthcare Provider")
        ));

        Address address = new Address()
                .addLine(random.nextInt(1, 200) + " Hospital Road")
                .setCity(SUBURBS[idx])
                .setState(STATES[idx])
                .setPostalCode(String.valueOf(random.nextInt(2000, 4999)))
                .setCountry("AU");

        org.addContact(new ExtendedContactDetail()
                .setAddress(address)
                .addTelecom(new ContactPoint()
                        .setSystem(ContactPoint.ContactPointSystem.PHONE)
                        .setValue("+61 2 " + random.nextInt(9000, 9999) + " 0000")
                        .setUse(ContactPoint.ContactPointUse.WORK)));

        Meta meta = new Meta();
        meta.setVersionId("1");
        meta.setLastUpdated(new Date());
        org.setMeta(meta);

        return org;
    }

    public Organization generateIncomplete(String id) {
        String orgId = id != null ? id : "org-inc-" + random.nextInt(10000, 99999);
        Organization org = new Organization();
        org.setId(new IdType("Organization", orgId));
        org.setActive(true);
        // Missing name and identifier
        return org;
    }

    public Organization generateInvalid(String id) {
        String orgId = id != null ? id : "org-inv-" + random.nextInt(10000, 99999);
        Organization org = new Organization();
        org.setId(new IdType("Organization", orgId));
        org.setActive(false);
        org.addIdentifier(new Identifier().setSystem("urn:invalid:hpio").setValue("INVALID_HPIO"));
        return org;
    }

    public Organization generateDuplicateIdentifier(String id, String existingHpio) {
        Organization org = generateValid(id);
        org.getIdentifier().removeIf(i -> SYSTEM_HPI_O.equals(i.getSystem()));
        org.getIdentifier().add(0, new Identifier()
                .setSystem(SYSTEM_HPI_O)
                .setValue(existingHpio)
                .setUse(Identifier.IdentifierUse.OFFICIAL));
        return org;
    }

    public Organization generateInactive(String id) {
        Organization org = generateValid(id);
        org.setActive(false);
        return org;
    }

    public Organization generateUpdate(Organization existing, int newVersion) {
        Organization updated = existing.copy();
        if (updated.getMeta() == null) {
            updated.setMeta(new Meta());
        }
        updated.getMeta().setVersionId(String.valueOf(newVersion));
        updated.getMeta().setLastUpdated(new Date());
        updated.setName(existing.getName() + " - Updated Dept");
        return updated;
    }

    public SeedRandom getRandom() {
        return random;
    }
}
