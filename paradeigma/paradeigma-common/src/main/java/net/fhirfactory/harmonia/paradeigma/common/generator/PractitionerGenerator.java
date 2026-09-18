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
 * Deterministic generator for FHIR R5 Practitioner resources with Australian Healthcare Identifiers (HPI-I).
 */
public class PractitionerGenerator {

    public static final String SYSTEM_HPI_I = "http://ns.electronichealth.net.au/id/hi/hpii/1.0";
    public static final String SYSTEM_AHPRA = "http://hl7.org.au/id/ahpra-registration-number";

    private static final String[] GIVEN_NAMES = {
            "Frank", "Sarah", "David", "Emma", "Michael", "Claire", "James", "Elena",
            "William", "Sophie", "Alexander", "Hannah", "Thomas", "Olivia", "Marcus"
    };

    private static final String[] FAMILY_NAMES = {
            "Bowman", "Chen", "Patel", "O'Connor", "Taylor", "Kowalski", "Williams",
            "MacDonald", "Singh", "Nguyen", "Davies", "Anderson", "Wright", "Mitchell"
    };

    private static final String[] QUALIFICATIONS = {
            "MBBS", "MD", "FRACP", "FRACS", "FACEM", "FRACGP", "FANZCA", "PhD"
    };

    private final SeedRandom random;

    public PractitionerGenerator() {
        this(new SeedRandom());
    }

    public PractitionerGenerator(long seed) {
        this(new SeedRandom(seed));
    }

    public PractitionerGenerator(SeedRandom random) {
        this.random = random != null ? random : new SeedRandom();
    }

    public Practitioner generate(String id) {
        return generateValid(id);
    }

    public Practitioner generateValid(String id) {
        String practId = id != null ? id : "pract-" + random.nextInt(10000, 99999);
        String given = random.pick(GIVEN_NAMES);
        String family = random.pick(FAMILY_NAMES);
        long hpiiNum = 8003610000000000L + random.nextInt(100000, 999999);

        Practitioner practitioner = new Practitioner();
        practitioner.setId(new IdType("Practitioner", practId));
        practitioner.setActive(true);

        HumanName name = new HumanName()
                .setFamily(family)
                .addGiven(given)
                .addPrefix("Dr.");
        practitioner.addName(name);

        practitioner.addIdentifier(new Identifier()
                .setSystem(SYSTEM_HPI_I)
                .setValue(String.valueOf(hpiiNum))
                .setUse(Identifier.IdentifierUse.OFFICIAL));

        practitioner.addIdentifier(new Identifier()
                .setSystem(SYSTEM_AHPRA)
                .setValue("MED" + random.nextInt(1000000, 9999999)));

        practitioner.addTelecom(new ContactPoint()
                .setSystem(ContactPoint.ContactPointSystem.EMAIL)
                .setValue(given.toLowerCase() + "." + family.toLowerCase() + "@health.example.org")
                .setUse(ContactPoint.ContactPointUse.WORK));

        practitioner.addTelecom(new ContactPoint()
                .setSystem(ContactPoint.ContactPointSystem.PHONE)
                .setValue("+61 2 " + random.nextInt(8000, 8999) + " " + random.nextInt(1000, 9999))
                .setUse(ContactPoint.ContactPointUse.WORK));

        Practitioner.PractitionerQualificationComponent qual = new Practitioner.PractitionerQualificationComponent();
        qual.getCode().setText(random.pick(QUALIFICATIONS));
        practitioner.addQualification(qual);

        practitioner.setGender(Enumerations.AdministrativeGender.fromCode(random.pick(new String[]{"male", "female"})));

        Meta meta = new Meta();
        meta.setVersionId("1");
        meta.setLastUpdated(new Date());
        practitioner.setMeta(meta);

        return practitioner;
    }

    public Practitioner generateIncomplete(String id) {
        String practId = id != null ? id : "pract-inc-" + random.nextInt(10000, 99999);
        Practitioner practitioner = new Practitioner();
        practitioner.setId(new IdType("Practitioner", practId));
        practitioner.setActive(true);
        // Missing name, missing official identifier
        return practitioner;
    }

    public Practitioner generateInvalid(String id) {
        String practId = id != null ? id : "pract-inv-" + random.nextInt(10000, 99999);
        Practitioner practitioner = new Practitioner();
        practitioner.setId(new IdType("Practitioner", practId));
        practitioner.setActive(false);
        // Deliberately broken identifier with invalid characters and empty family name
        practitioner.addIdentifier(new Identifier().setSystem("urn:invalid:identifier").setValue("???---INVALID---???"));
        practitioner.addName(new HumanName().setFamily(""));
        return practitioner;
    }

    public Practitioner generateDuplicateIdentifier(String id, String existingHpii) {
        Practitioner practitioner = generateValid(id);
        practitioner.getIdentifier().removeIf(i -> SYSTEM_HPI_I.equals(i.getSystem()));
        practitioner.getIdentifier().add(0, new Identifier()
                .setSystem(SYSTEM_HPI_I)
                .setValue(existingHpii)
                .setUse(Identifier.IdentifierUse.OFFICIAL));
        return practitioner;
    }

    public Practitioner generateInactive(String id) {
        Practitioner practitioner = generateValid(id);
        practitioner.setActive(false);
        return practitioner;
    }

    public Practitioner generateUpdate(Practitioner existing, int newVersion) {
        Practitioner updated = existing.copy();
        if (updated.getMeta() == null) {
            updated.setMeta(new Meta());
        }
        updated.getMeta().setVersionId(String.valueOf(newVersion));
        updated.getMeta().setLastUpdated(new Date());

        String newPhone = "+61 2 " + random.nextInt(9000, 9999) + " " + random.nextInt(1000, 9999);
        updated.getTelecom().removeIf(t -> ContactPoint.ContactPointSystem.PHONE.equals(t.getSystem()));
        updated.addTelecom(new ContactPoint()
                .setSystem(ContactPoint.ContactPointSystem.PHONE)
                .setValue(newPhone)
                .setUse(ContactPoint.ContactPointUse.WORK));

        return updated;
    }

    public SeedRandom getRandom() {
        return random;
    }
}
