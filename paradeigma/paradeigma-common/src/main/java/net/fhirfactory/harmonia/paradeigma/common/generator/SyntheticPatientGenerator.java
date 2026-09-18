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

package net.fhirfactory.harmonia.paradeigma.common.generator;

import net.fhirfactory.harmonia.paradeigma.common.model.Gender;
import net.fhirfactory.harmonia.paradeigma.common.model.PatientProfile;
import net.fhirfactory.harmonia.paradeigma.common.model.VisitProfile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Deterministic synthetic patient and visit generator for Paradeigma simulations and testing.
 */
public class SyntheticPatientGenerator {

    private static final String[] MALE_GIVEN_NAMES = {
            "James", "John", "Robert", "Michael", "William", "David", "Richard", "Joseph",
            "Thomas", "Charles", "Christopher", "Daniel", "Matthew", "Anthony", "Mark", "Alexander"
    };

    private static final String[] FEMALE_GIVEN_NAMES = {
            "Mary", "Patricia", "Jennifer", "Linda", "Elizabeth", "Barbara", "Susan", "Jessica",
            "Sarah", "Karen", "Nancy", "Lisa", "Betty", "Margaret", "Sandra", "Emily"
    };

    private static final String[] FAMILY_NAMES = {
            "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis",
            "Rodriguez", "Martinez", "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson", "Thomas",
            "Taylor", "Moore", "Jackson", "Martin", "Lee", "Perez", "Thompson", "White", "Harris"
    };

    private static final String[] STREET_NAMES = {
            "Main Street", "Oak Avenue", "Maple Boulevard", "Cedar Lane", "Pine Street",
            "Elm Street", "Washington Avenue", "Park Road", "Lakeview Drive", "Churchill Way"
    };

    private static final String[] CITIES = {
            "Metropolis", "Springfield", "Riverdale", "Gotham", "Starling", "Central City", "Keystone"
    };

    private static final String[] STATES = {
            "NSW", "VIC", "QLD", "WA", "SA", "TAS", "ACT"
    };

    private static final String[] WARDS = {
            "WARD-3A", "WARD-4B", "WARD-2C", "ICU-EAST", "CCU-1", "SURG-1", "MED-2", "ED-POD1"
    };

    private static final String[] DOCTORS = {
            "DOC-101^Dr. Alice Vance", "DOC-102^Dr. Bob Chen", "DOC-103^Dr. Clara Oswald",
            "DOC-104^Dr. Derek Shepherd", "DOC-105^Dr. Elena Rostova", "DOC-106^Dr. Frank Miller"
    };

    private final SeedRandom random;
    private final AtomicInteger patientSequence = new AtomicInteger(100000);
    private final AtomicInteger visitSequence = new AtomicInteger(200000);

    public SyntheticPatientGenerator() {
        this(12345L);
    }

    public SyntheticPatientGenerator(long seed) {
        this.random = new SeedRandom(seed);
    }

    public PatientProfile generatePatient() {
        int seq = patientSequence.incrementAndGet();
        String patientId = "PAT-" + seq;
        return generatePatient(patientId);
    }

    public PatientProfile generatePatient(String patientId) {
        Gender gender = random.nextBoolean() ? Gender.M : Gender.F;
        String givenName = gender == Gender.M ? random.pick(MALE_GIVEN_NAMES) : random.pick(FEMALE_GIVEN_NAMES);
        String familyName = random.pick(FAMILY_NAMES);

        int ageYears = random.nextInt(18, 85);
        int dayOfYear = random.nextInt(1, 365);
        LocalDate dob = LocalDate.now().minusYears(ageYears).minusDays(dayOfYear);

        PatientProfile patient = new PatientProfile(patientId, familyName, givenName, dob, gender);
        patient.setPrefix(gender == Gender.M ? "Mr." : "Ms.");
        patient.setMrn("MRN-" + patientId.substring(4));
        patient.setNationalId("MC-" + (seqNumber(patientId) * 7 + 100000));

        int streetNum = random.nextInt(10, 999);
        patient.setAddressLine1(streetNum + " " + random.pick(STREET_NAMES));
        patient.setCity(random.pick(CITIES));
        patient.setState(random.pick(STATES));
        patient.setPostalCode(String.valueOf(random.nextInt(2000, 4999)));
        patient.setCountry("AU");
        patient.setPhoneNumber("(555) " + String.format("%03d-%04d", random.nextInt(100, 999), random.nextInt(1000, 9999)));
        patient.setEmail(givenName.toLowerCase() + "." + familyName.toLowerCase() + "@example.org");

        return patient;
    }

    public VisitProfile generateVisit(PatientProfile patient) {
        String patientId = patient != null ? patient.getPatientId() : "PAT-" + patientSequence.get();
        int seq = visitSequence.incrementAndGet();
        String visitNumber = "VIS-" + seq;

        String ward = random.pick(WARDS);
        String room = String.valueOf(random.nextInt(100, 499));
        String bed = random.pick(new String[]{"A", "B", "C", "D"});

        VisitProfile visit = new VisitProfile(visitNumber, patientId, "I", ward, room, bed);
        visit.setFacility("CENTRAL-HOSPITAL");
        visit.setAdmitDateTime(LocalDateTime.now().minusHours(random.nextInt(1, 48)));

        String doc = random.pick(DOCTORS);
        String[] docParts = doc.split("\\^");
        visit.setAttendingDoctorId(docParts[0]);
        visit.setAttendingDoctorName(docParts[1]);
        visit.setAdmittingDoctorId(docParts[0]);
        visit.setAdmittingDoctorName(docParts[1]);

        return visit;
    }

    private int seqNumber(String id) {
        try {
            return Integer.parseInt(id.replaceAll("\\D+", ""));
        } catch (NumberFormatException e) {
            return 100000;
        }
    }

    public SeedRandom getRandom() {
        return random;
    }
}
