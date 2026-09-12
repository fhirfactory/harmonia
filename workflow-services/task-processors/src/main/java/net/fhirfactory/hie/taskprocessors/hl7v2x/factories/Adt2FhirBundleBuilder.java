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

package net.fhirfactory.hie.taskprocessors.hl7v2x.factories;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.Terser;
import ca.uhn.hl7v2.util.idgenerator.NanoTimeGenerator;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static net.fhirfactory.hie.taskprocessors.hl7v2x.common.Hl7v2ParsingSupport.*;

/**
 * Coordinates transforming HL7 v2.x ADT messages into a unified FHIR R5 {@link Bundle} of derived resources.
 */
public class Adt2FhirBundleBuilder {

    private static final Logger log = LoggerFactory.getLogger(Adt2FhirBundleBuilder.class);

    private final HapiContext hapiContext;
    private final PipeParser pipeParser;
    private final AdtPatientResourceBuilder patientBuilder;
    private final AdtEncounterResourceBuilder encounterBuilder;
    private final AdtAdministrativeResourceBuilder administrativeBuilder;
    private final AdtClinicalResourceBuilder clinicalBuilder;
    private final AdtMetadataResourceBuilder metadataBuilder;

    public Adt2FhirBundleBuilder() {
        this.hapiContext = new DefaultHapiContext();
        this.hapiContext.getParserConfiguration().setIdGenerator(new NanoTimeGenerator());
        this.pipeParser = hapiContext.getPipeParser();
        this.patientBuilder = new AdtPatientResourceBuilder();
        this.encounterBuilder = new AdtEncounterResourceBuilder();
        this.administrativeBuilder = new AdtAdministrativeResourceBuilder();
        this.clinicalBuilder = new AdtClinicalResourceBuilder();
        this.metadataBuilder = new AdtMetadataResourceBuilder();
    }

    public Adt2FhirBundleBuilder(HapiContext hapiContext,
                                 AdtPatientResourceBuilder patientBuilder,
                                 AdtEncounterResourceBuilder encounterBuilder,
                                 AdtAdministrativeResourceBuilder administrativeBuilder,
                                 AdtClinicalResourceBuilder clinicalBuilder,
                                 AdtMetadataResourceBuilder metadataBuilder) {
        this.hapiContext = hapiContext != null ? hapiContext : new DefaultHapiContext();
        this.pipeParser = this.hapiContext.getPipeParser();
        this.patientBuilder = patientBuilder != null ? patientBuilder : new AdtPatientResourceBuilder();
        this.encounterBuilder = encounterBuilder != null ? encounterBuilder : new AdtEncounterResourceBuilder();
        this.administrativeBuilder = administrativeBuilder != null ? administrativeBuilder : new AdtAdministrativeResourceBuilder();
        this.clinicalBuilder = clinicalBuilder != null ? clinicalBuilder : new AdtClinicalResourceBuilder();
        this.metadataBuilder = metadataBuilder != null ? metadataBuilder : new AdtMetadataResourceBuilder();
    }

    /**
     * Parses an ADT message string and derives all possible FHIR R5 resources into a single {@link Bundle}.
     *
     * @param rawHl7Message raw HL7 ADT message string
     * @param parentTask    parent Task resource for context
     * @param activityId    ID of the transformation activity
     * @param activityName  Name of the transformation activity
     * @return populated FHIR Bundle
     */
    public Bundle createBundleFromAdt(String rawHl7Message, Task parentTask, String activityId, String activityName) {
        Bundle bundle = new Bundle();
        String bundleId = "bundle-" + (parentTask != null && parentTask.getIdPart() != null ? parentTask.getIdPart() : UUID.randomUUID().toString());
        bundle.setId("Bundle/" + bundleId);
        bundle.setType(Bundle.BundleType.COLLECTION);
        bundle.setTimestamp(new Date());

        if (StringUtils.isBlank(rawHl7Message)) {
            Patient fallbackPatient = patientBuilder.createFallbackPatient(parentTask != null ? parentTask.getIdPart() : "unknown");
            addEntry(bundle, fallbackPatient);
            if (parentTask != null) {
                parentTask.setFor(new Reference("Patient/" + fallbackPatient.getIdPart()).setDisplay("Unknown Patient"));
            }
            return bundle;
        }

        try {
            Message hl7Message = null;
            Terser terser = null;
            try {
                hl7Message = pipeParser.parse(rawHl7Message.trim());
                terser = new Terser(hl7Message);
            } catch (Exception e) {
                log.debug("HAPI pipe parser encountered error: {}. Proceeding with fallback segment parsing.", e.getMessage());
            }

            // Extract core metadata
            String messageControlId = extractTerserOrRegex(terser, "/MSH-10", rawHl7Message, "MSH", 10,
                    parentTask != null ? parentTask.getIdPart() : UUID.randomUUID().toString());
            String messageType = extractTerserOrRegex(terser, "/MSH-9-1", rawHl7Message, "MSH", 9, "ADT");
            String triggerEvent = extractTerserOrRegex(terser, "/MSH-9-2", rawHl7Message, "MSH", 9, "A01");
            if (messageType.contains("^")) {
                String[] parts = messageType.split("\\^");
                messageType = parts[0];
                if (parts.length > 1) triggerEvent = parts[1];
            }
            String sendingApp = extractTerserOrRegex(terser, "/MSH-3", rawHl7Message, "MSH", 3, null);
            String sendingFacility = extractTerserOrRegex(terser, "/MSH-4", rawHl7Message, "MSH", 4, null);
            String messageTimestamp = extractTerserOrRegex(terser, "/MSH-7", rawHl7Message, "MSH", 7, null);

            // 1. Build Patient
            Patient patient = patientBuilder.buildPatient(terser, rawHl7Message, messageControlId);
            addEntry(bundle, patient);
            if (parentTask != null && patient != null) {
                String fullName = extractFullName(patient);
                parentTask.setFor(new Reference("Patient/" + patient.getIdPart()).setDisplay(fullName));
            }

            String patientId = patient != null ? patient.getIdPart() : "unknown";

            // 2. Build Practitioners
            List<Practitioner> practitioners = encounterBuilder.buildPractitioners(terser, rawHl7Message);
            for (Practitioner p : practitioners) {
                addEntry(bundle, p);
            }

            // 3. Build RelatedPerson resources (NK1, GT1)
            List<RelatedPerson> relatedPersons = administrativeBuilder.buildRelatedPersons(terser, rawHl7Message, patientId);
            for (RelatedPerson rp : relatedPersons) {
                addEntry(bundle, rp);
            }

            // 4. Build Location resources
            List<Location> locations = encounterBuilder.buildLocations(terser, rawHl7Message);
            for (Location loc : locations) {
                addEntry(bundle, loc);
            }

            // 5. Build Organization resources
            String receivingFacility = extractTerserOrRegex(terser, "/MSH-6", rawHl7Message, "MSH", 6, null);
            List<Organization> organizations = administrativeBuilder.buildOrganizations(terser, rawHl7Message, sendingFacility, receivingFacility);
            for (Organization org : organizations) {
                addEntry(bundle, org);
            }

            // 6. Build Encounter (PV1, PV2)
            Encounter encounter = encounterBuilder.buildEncounter(terser, rawHl7Message, messageControlId, triggerEvent, patientId, practitioners, locations);
            if (encounter != null) {
                addEntry(bundle, encounter);
                if (parentTask != null) {
                    parentTask.setFocus(new Reference("Encounter/" + encounter.getIdPart()).setDisplay("Encounter " + encounter.getIdPart()));
                }
            }

            // 7. Build Condition / Diagnoses (DG1)
            List<Condition> conditions = clinicalBuilder.buildConditions(terser, rawHl7Message, patientId, encounter);
            for (Condition cond : conditions) {
                addEntry(bundle, cond);
            }

            // 8. Build AllergyIntolerance resources (AL1)
            List<AllergyIntolerance> allergies = clinicalBuilder.buildAllergies(terser, rawHl7Message, patientId);
            for (AllergyIntolerance allergy : allergies) {
                addEntry(bundle, allergy);
            }

            // 9. Build Observation resources (OBX)
            List<Observation> observations = clinicalBuilder.buildObservations(terser, rawHl7Message, patientId, encounter);
            for (Observation obs : observations) {
                addEntry(bundle, obs);
            }

            // 10. Build Coverage resources (IN1, IN2)
            List<Coverage> coverages = clinicalBuilder.buildCoverages(terser, rawHl7Message, patientId);
            for (Coverage cov : coverages) {
                addEntry(bundle, cov);
            }

            // 11. Build Communication resource containing raw ADT message
            Communication comm = metadataBuilder.buildCommunication(terser, rawHl7Message, messageControlId, triggerEvent,
                    patientId, extractFullName(patient), sendingApp, sendingFacility, messageTimestamp);
            if (comm != null) {
                addEntry(bundle, comm);
            }

            // 12. Build Provenance resource
            Provenance provenance = metadataBuilder.buildProvenance(bundle, patient, messageControlId, triggerEvent,
                    sendingApp, sendingFacility, activityId, activityName);
            if (provenance != null) {
                addEntry(bundle, provenance);
            }

        } catch (Exception e) {
            log.warn("Error deriving FHIR resources from ADT message: {}. Creating baseline resources.", e.getMessage(), e);
            Patient fallback = patientBuilder.createFallbackPatient(parentTask != null ? parentTask.getIdPart() : "unknown");
            addEntry(bundle, fallback);
        }

        return bundle;
    }

    public void addEntry(Bundle bundle, Resource resource) {
        if (resource == null || bundle == null) return;
        Bundle.BundleEntryComponent entry = bundle.addEntry();
        entry.setFullUrl(resource.getId());
        entry.setResource(resource);
    }

    public AdtPatientResourceBuilder getPatientBuilder() {
        return patientBuilder;
    }

    public AdtEncounterResourceBuilder getEncounterBuilder() {
        return encounterBuilder;
    }

    public AdtAdministrativeResourceBuilder getAdministrativeBuilder() {
        return administrativeBuilder;
    }

    public AdtClinicalResourceBuilder getClinicalBuilder() {
        return clinicalBuilder;
    }

    public AdtMetadataResourceBuilder getMetadataBuilder() {
        return metadataBuilder;
    }
}
