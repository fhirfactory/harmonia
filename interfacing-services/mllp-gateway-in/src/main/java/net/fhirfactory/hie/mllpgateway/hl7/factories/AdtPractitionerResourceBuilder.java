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
import org.hl7.fhir.r5.model.HumanName;
import org.hl7.fhir.r5.model.Identifier;
import org.hl7.fhir.r5.model.Practitioner;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Builds FHIR {@link Practitioner} resources from attending, referring, and consulting doctors in HL7 v2 ADT PV1 segments.
 */
@ApplicationScoped
public class AdtPractitionerResourceBuilder {

    private final AdtMessageExtractor extractor;

    public AdtPractitionerResourceBuilder() {
        this(new AdtMessageExtractor());
    }

    @Inject
    public AdtPractitionerResourceBuilder(AdtMessageExtractor extractor) {
        this.extractor = extractor != null ? extractor : new AdtMessageExtractor();
    }

    /**
     * Builds FHIR Practitioner resources from attending, referring, and consulting doctors in PV1.
     *
     * @param terser     HAPI Terser
     * @param rawMessage raw HL7 message string
     * @return list of Practitioner resources
     */
    public List<Practitioner> buildPractitioners(Terser terser, String rawMessage) {
        List<Practitioner> practitioners = new ArrayList<>();

        // Attending Doctor (PV1-7)
        String attendingId = extractor.getTerserValue(terser, "/PV1-7-1", "PV1-7-1");
        String attendingFamily = extractor.getTerserValue(terser, "/PV1-7-2", "PV1-7-2");
        String attendingGiven = extractor.getTerserValue(terser, "/PV1-7-3", "PV1-7-3");

        if (StringUtils.isNotBlank(attendingId) || StringUtils.isNotBlank(attendingFamily)) {
            Practitioner p = new Practitioner();
            String cleanId = StringUtils.isNotBlank(attendingId) ? attendingId : UUID.randomUUID().toString().substring(0, 8);
            p.setId("Practitioner/" + cleanId);
            p.setActive(true);

            if (StringUtils.isNotBlank(attendingId)) {
                Identifier id = p.addIdentifier();
                id.setSystem("http://example.org/practitioners");
                id.setValue(attendingId);
            }

            HumanName name = p.addName();
            name.setUse(HumanName.NameUse.OFFICIAL);
            if (StringUtils.isNotBlank(attendingFamily)) name.setFamily(attendingFamily);
            if (StringUtils.isNotBlank(attendingGiven)) name.addGiven(attendingGiven);

            practitioners.add(p);
        }

        // Referring Doctor (PV1-8)
        String referringId = extractor.getTerserValue(terser, "/PV1-8-1", "PV1-8-1");
        String referringFamily = extractor.getTerserValue(terser, "/PV1-8-2", "PV1-8-2");
        String referringGiven = extractor.getTerserValue(terser, "/PV1-8-3", "PV1-8-3");

        if ((StringUtils.isNotBlank(referringId) || StringUtils.isNotBlank(referringFamily))
                && !Objects.equals(referringId, attendingId)) {
            Practitioner p = new Practitioner();
            String cleanId = StringUtils.isNotBlank(referringId) ? referringId : UUID.randomUUID().toString().substring(0, 8);
            p.setId("Practitioner/" + cleanId);
            p.setActive(true);

            if (StringUtils.isNotBlank(referringId)) {
                Identifier id = p.addIdentifier();
                id.setSystem("http://example.org/practitioners");
                id.setValue(referringId);
            }

            HumanName name = p.addName();
            name.setUse(HumanName.NameUse.OFFICIAL);
            if (StringUtils.isNotBlank(referringFamily)) name.setFamily(referringFamily);
            if (StringUtils.isNotBlank(referringGiven)) name.addGiven(referringGiven);

            practitioners.add(p);
        }

        // Consulting / Other Doctor (PV1-9, PV1-17)
        String otherId = extractor.getTerserValue(terser, "/PV1-9-1", "PV1-9-1");
        String otherFamily = extractor.getTerserValue(terser, "/PV1-9-2", "PV1-9-2");
        String otherGiven = extractor.getTerserValue(terser, "/PV1-9-3", "PV1-9-3");
        if (StringUtils.isBlank(otherId) && StringUtils.isBlank(otherFamily)) {
            otherId = extractor.getTerserValue(terser, "/PV1-17-1", "PV1-17-1");
            otherFamily = extractor.getTerserValue(terser, "/PV1-17-2", "PV1-17-2");
            otherGiven = extractor.getTerserValue(terser, "/PV1-17-3", "PV1-17-3");
        }

        if ((StringUtils.isNotBlank(otherId) || StringUtils.isNotBlank(otherFamily))
                && !Objects.equals(otherId, attendingId) && !Objects.equals(otherId, referringId)) {
            Practitioner p = new Practitioner();
            String cleanId = StringUtils.isNotBlank(otherId) ? otherId : UUID.randomUUID().toString().substring(0, 8);
            p.setId("Practitioner/" + cleanId);
            p.setActive(true);

            if (StringUtils.isNotBlank(otherId)) {
                Identifier id = p.addIdentifier();
                id.setSystem("http://example.org/practitioners");
                id.setValue(otherId);
            }

            HumanName name = p.addName();
            name.setUse(HumanName.NameUse.OFFICIAL);
            if (StringUtils.isNotBlank(otherFamily)) name.setFamily(otherFamily);
            if (StringUtils.isNotBlank(otherGiven)) name.addGiven(otherGiven);

            practitioners.add(p);
        }

        return practitioners;
    }
}
