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

package net.fhirfactory.hie.mllpgateway.hl7.factories;

import jakarta.enterprise.context.ApplicationScoped;
import org.hl7.fhir.r5.model.*;

import java.util.Date;
import java.util.List;

/**
 * Assembles transformed FHIR resources into a FHIR R5 {@link Bundle} of type COLLECTION.
 */
@ApplicationScoped
public class AdtBundleResourceBuilder {

    /**
     * Builds a FHIR Bundle (Collection) containing all extracted resources.
     *
     * @param messageControlId message control ID
     * @param patient          transformed Patient resource
     * @param relatedPersons   transformed RelatedPerson resources
     * @param practitioners    transformed Practitioner resources
     * @param encounter        transformed Encounter resource
     * @return populated Bundle
     */
    public Bundle buildBundle(String messageControlId, Patient patient, List<RelatedPerson> relatedPersons,
                              List<Practitioner> practitioners, Encounter encounter) {
        Bundle bundle = new Bundle();
        bundle.setId("bundle-" + messageControlId);
        bundle.setType(Bundle.BundleType.COLLECTION);
        bundle.setTimestamp(new Date());

        // Add Patient
        if (patient != null) {
            Bundle.BundleEntryComponent entry = bundle.addEntry();
            entry.setFullUrl(patient.getId());
            entry.setResource(patient);
        }

        // Add RelatedPersons
        if (relatedPersons != null) {
            for (RelatedPerson rp : relatedPersons) {
                Bundle.BundleEntryComponent entry = bundle.addEntry();
                entry.setFullUrl(rp.getId());
                entry.setResource(rp);
            }
        }

        // Add Practitioners
        if (practitioners != null) {
            for (Practitioner p : practitioners) {
                Bundle.BundleEntryComponent entry = bundle.addEntry();
                entry.setFullUrl(p.getId());
                entry.setResource(p);
            }
        }

        // Add Encounter
        if (encounter != null) {
            Bundle.BundleEntryComponent entry = bundle.addEntry();
            entry.setFullUrl(encounter.getId());
            entry.setResource(encounter);
        }

        return bundle;
    }
}
