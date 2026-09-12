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
public class MfnBundleResourceBuilder {

    public Bundle buildBundle(String messageControlId,
                              List<Organization> organizations,
                              List<Location> locations,
                              List<Practitioner> practitioners,
                              List<PractitionerRole> practitionerRoles) {
        Bundle bundle = new Bundle();
        bundle.setId("bundle-" + messageControlId);
        bundle.setType(Bundle.BundleType.COLLECTION);
        bundle.setTimestamp(new Date());

        if (organizations != null) {
            for (Organization org : organizations) {
                addEntry(bundle, org);
            }
        }

        if (locations != null) {
            for (Location loc : locations) {
                addEntry(bundle, loc);
            }
        }

        if (practitioners != null) {
            for (Practitioner p : practitioners) {
                addEntry(bundle, p);
            }
        }

        if (practitionerRoles != null) {
            for (PractitionerRole pr : practitionerRoles) {
                addEntry(bundle, pr);
            }
        }

        return bundle;
    }

    private void addEntry(Bundle bundle, Resource resource) {
        if (bundle == null || resource == null) return;
        Bundle.BundleEntryComponent entry = bundle.addEntry();
        entry.setFullUrl(resource.getId());
        entry.setResource(resource);
    }
}
