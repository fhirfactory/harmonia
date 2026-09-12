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

import ca.uhn.hl7v2.util.Terser;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import net.fhirfactory.hie.mllpgateway.hl7.AdtMessageExtractor;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Builds FHIR {@link RelatedPerson} resources from HL7 v2 ADT NK1 segments.
 */
@ApplicationScoped
public class AdtRelatedPersonResourceBuilder {

    private final AdtMessageExtractor extractor;

    public AdtRelatedPersonResourceBuilder() {
        this(new AdtMessageExtractor());
    }

    @Inject
    public AdtRelatedPersonResourceBuilder(AdtMessageExtractor extractor) {
        this.extractor = extractor != null ? extractor : new AdtMessageExtractor();
    }

    /**
     * Builds FHIR RelatedPerson resources from ADT NK1 segments.
     *
     * @param terser     HAPI Terser
     * @param rawMessage raw HL7 message string
     * @param patientId  patient ID
     * @return list of RelatedPerson resources
     */
    public List<RelatedPerson> buildRelatedPersons(Terser terser, String rawMessage, String patientId) {
        List<RelatedPerson> relatedPersons = new ArrayList<>();

        int nk1Index = 0;
        while (true) {
            String pathPrefix = nk1Index == 0 ? "/NK1" : "/NK1(" + nk1Index + ")";
            String nk1SetId = extractor.getTerserValue(terser, pathPrefix + "-1", null);

            if (StringUtils.isBlank(nk1SetId) && nk1Index > 0) {
                break;
            }
            if (StringUtils.isBlank(nk1SetId) && nk1Index == 0) {
                // Try checking if NK1 exists at all
                String name = extractor.getTerserValue(terser, "/NK1-2-1", null);
                if (StringUtils.isBlank(name)) {
                    break;
                }
            }

            RelatedPerson rp = new RelatedPerson();
            String rpId = "RelatedPerson/" + patientId + "-nk" + (nk1Index + 1);
            rp.setId(rpId);
            rp.setActive(true);
            rp.setPatient(new Reference("Patient/" + patientId));

            // Name
            String family = extractor.getTerserValue(terser, pathPrefix + "-2-1", null);
            String given = extractor.getTerserValue(terser, pathPrefix + "-2-2", null);
            if (StringUtils.isNotBlank(family) || StringUtils.isNotBlank(given)) {
                HumanName humanName = rp.addName();
                humanName.setUse(HumanName.NameUse.OFFICIAL);
                if (StringUtils.isNotBlank(family)) humanName.setFamily(family);
                if (StringUtils.isNotBlank(given)) humanName.addGiven(given);
            }

            // Relationship
            String relCode = extractor.getTerserValue(terser, pathPrefix + "-3-1", null);
            String relText = extractor.getTerserValue(terser, pathPrefix + "-3-2", null);
            if (StringUtils.isNotBlank(relCode) || StringUtils.isNotBlank(relText)) {
                CodeableConcept relConcept = rp.addRelationship();
                relConcept.setText(relText != null ? relText : relCode);
                if (StringUtils.isNotBlank(relCode)) {
                    relConcept.addCoding(new Coding("http://terminology.hl7.org/CodeSystem/v2-0131", relCode, relText));
                }
            }

            // Telecom
            String phone = extractor.getTerserValue(terser, pathPrefix + "-5-1", null, pathPrefix + "-5", null);
            if (StringUtils.isNotBlank(phone)) {
                ContactPoint cp = rp.addTelecom();
                cp.setSystem(ContactPoint.ContactPointSystem.PHONE);
                cp.setUse(ContactPoint.ContactPointUse.HOME);
                cp.setValue(extractor.cleanPhoneNumber(phone));
            }

            relatedPersons.add(rp);
            nk1Index++;
            if (nk1Index > 10) break; // safeguard
        }

        return relatedPersons;
    }
}
