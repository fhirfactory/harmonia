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

import java.util.Date;
import java.util.List;

/**
 * Builds FHIR {@link Encounter} resources from HL7 v2 ADT PV1 segments.
 */
@ApplicationScoped
public class AdtEncounterResourceBuilder {

    private final AdtMessageExtractor extractor;

    public AdtEncounterResourceBuilder() {
        this(new AdtMessageExtractor());
    }

    @Inject
    public AdtEncounterResourceBuilder(AdtMessageExtractor extractor) {
        this.extractor = extractor != null ? extractor : new AdtMessageExtractor();
    }

    /**
     * Builds a FHIR Encounter resource from the ADT PV1 segment.
     *
     * @param terser           HAPI Terser
     * @param rawMessage       raw HL7 message string
     * @param messageControlId message control ID
     * @param triggerEvent     trigger event code (e.g., A01)
     * @param patientId        patient ID
     * @param visitNumber      visit number
     * @param patientClass     patient class (e.g. I, O, E)
     * @param practitioners    associated practitioners
     * @return populated Encounter resource
     */
    public Encounter buildEncounter(Terser terser, String rawMessage, String messageControlId,
                                    String triggerEvent, String patientId, String visitNumber,
                                    String patientClass, List<Practitioner> practitioners) {
        Encounter encounter = new Encounter();
        String encounterId = StringUtils.isNotBlank(visitNumber) ? visitNumber : messageControlId;
        encounter.setId("Encounter/" + encounterId);

        // Status based on ADT trigger event
        encounter.setStatus(extractor.mapEncounterStatus(triggerEvent));

        // Class (e.g., inpatient, outpatient, emergency)
        CodeableConcept encounterClass = extractor.mapEncounterClass(patientClass);
        encounter.setClass_(List.of(encounterClass));

        // Subject (Patient)
        encounter.setSubject(new Reference("Patient/" + patientId));

        // Identifier (Visit Number)
        if (StringUtils.isNotBlank(visitNumber)) {
            Identifier visitId = encounter.addIdentifier();
            visitId.setSystem("http://example.org/visit-numbers");
            visitId.setValue(visitNumber);
            visitId.setType(new CodeableConcept().setText("Visit Number"));
        }

        // Participants (Practitioners)
        if (practitioners != null) {
            for (Practitioner p : practitioners) {
                Encounter.EncounterParticipantComponent participant = encounter.addParticipant();
                participant.setActor(new Reference(p.getId()).setDisplay(
                        p.hasName() ? p.getNameFirstRep().getNameAsSingleString() : p.getId()));
            }
        }

        // Admission Date / Actual Period
        String admitDateTimeStr = extractor.getTerserValue(terser, "/PV1-44-1", "PV1-44-1", "/PV1-44", "PV1-44");
        String dischargeDateTimeStr = extractor.getTerserValue(terser, "/PV1-45-1", "PV1-45-1", "/PV1-45", "PV1-45");

        Date admitDate = extractor.parseHl7Date(admitDateTimeStr);
        Date dischargeDate = extractor.parseHl7Date(dischargeDateTimeStr);

        if (admitDate != null || dischargeDate != null) {
            Period period = new Period();
            if (admitDate != null) period.setStart(admitDate);
            if (dischargeDate != null) period.setEnd(dischargeDate);
            encounter.setActualPeriod(period);
        }

        return encounter;
    }
}
