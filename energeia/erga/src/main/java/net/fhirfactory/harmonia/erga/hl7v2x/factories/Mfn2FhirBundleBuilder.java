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

package net.fhirfactory.harmonia.erga.hl7v2x.factories;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.Terser;
import ca.uhn.hl7v2.util.idgenerator.NanoTimeGenerator;
import net.fhirfactory.harmonia.model.security.FhirSecurityTagManager;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static net.fhirfactory.harmonia.erga.hl7v2x.common.Hl7v2ParsingSupport.extractTerserOrRegex;

/**
 * Coordinates transforming HL7 v2.x MFN^M02 messages into a unified FHIR R5 {@link Bundle} of derived resources.
 */
public class Mfn2FhirBundleBuilder {

    private static final Logger log = LoggerFactory.getLogger(Mfn2FhirBundleBuilder.class);

    private final HapiContext hapiContext;
    private final PipeParser pipeParser;
    private final MfnPractitionerResourceBuilder practitionerBuilder;
    private final MfnAdministrativeResourceBuilder administrativeBuilder;
    private final MfnMetadataResourceBuilder metadataBuilder;

    public Mfn2FhirBundleBuilder() {
        this.hapiContext = new DefaultHapiContext();
        this.hapiContext.getParserConfiguration().setIdGenerator(new NanoTimeGenerator());
        this.pipeParser = hapiContext.getPipeParser();
        this.practitionerBuilder = new MfnPractitionerResourceBuilder();
        this.administrativeBuilder = new MfnAdministrativeResourceBuilder();
        this.metadataBuilder = new MfnMetadataResourceBuilder();
    }

    public Mfn2FhirBundleBuilder(HapiContext hapiContext,
                                 MfnPractitionerResourceBuilder practitionerBuilder,
                                 MfnAdministrativeResourceBuilder administrativeBuilder,
                                 MfnMetadataResourceBuilder metadataBuilder) {
        this.hapiContext = hapiContext != null ? hapiContext : new DefaultHapiContext();
        this.pipeParser = this.hapiContext.getPipeParser();
        this.practitionerBuilder = practitionerBuilder != null ? practitionerBuilder : new MfnPractitionerResourceBuilder();
        this.administrativeBuilder = administrativeBuilder != null ? administrativeBuilder : new MfnAdministrativeResourceBuilder();
        this.metadataBuilder = metadataBuilder != null ? metadataBuilder : new MfnMetadataResourceBuilder();
    }

    /**
     * Parses an MFN^M02 message string and derives all possible FHIR R5 resources into a single {@link Bundle}.
     *
     * @param rawHl7Message raw HL7 MFN message string
     * @param parentTask    parent Task resource for context
     * @param activityId    ID of the transformation activity
     * @param activityName  Name of the transformation activity
     * @return populated FHIR Bundle
     */
    public Bundle createBundleFromMfn(String rawHl7Message, Task parentTask, String activityId, String activityName) {
        Bundle bundle = new Bundle();
        String bundleId = "bundle-" + (parentTask != null && parentTask.getIdPart() != null ? parentTask.getIdPart() : UUID.randomUUID().toString());
        bundle.setId("Bundle/" + bundleId);
        bundle.setType(Bundle.BundleType.COLLECTION);
        bundle.setTimestamp(new Date());

        if (StringUtils.isBlank(rawHl7Message)) {
            Practitioner fallbackPractitioner = practitionerBuilder.createFallbackPractitioner(parentTask != null ? parentTask.getIdPart() : "unknown");
            addEntry(bundle, fallbackPractitioner);
            if (parentTask != null) {
                parentTask.setFor(new Reference("Practitioner/" + fallbackPractitioner.getIdPart()).setDisplay("Unknown Practitioner"));
            }
            FhirSecurityTagManager.applySecurityTags(bundle);
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
            String messageType = extractTerserOrRegex(terser, "/MSH-9-1", rawHl7Message, "MSH", 9, "MFN");
            String triggerEvent = extractTerserOrRegex(terser, "/MSH-9-2", rawHl7Message, "MSH", 9, "M02");
            if (messageType.contains("^")) {
                String[] parts = messageType.split("\\^");
                messageType = parts[0];
                if (parts.length > 1) triggerEvent = parts[1];
            }
            String sendingApp = extractTerserOrRegex(terser, "/MSH-3", rawHl7Message, "MSH", 3, null);
            String sendingFacility = extractTerserOrRegex(terser, "/MSH-4", rawHl7Message, "MSH", 4, null);
            String receivingFacility = extractTerserOrRegex(terser, "/MSH-6", rawHl7Message, "MSH", 6, null);
            String messageTimestamp = extractTerserOrRegex(terser, "/MSH-7", rawHl7Message, "MSH", 7, null);

            // 1. Build Organization resources
            List<Organization> organizations = administrativeBuilder.buildOrganizations(terser, rawHl7Message, sendingFacility, receivingFacility);
            for (Organization org : organizations) {
                addEntry(bundle, org);
            }

            // 2. Build Location resources
            List<Location> locations = administrativeBuilder.buildLocations(terser, rawHl7Message);
            for (Location loc : locations) {
                addEntry(bundle, loc);
            }

            // 3. Build Practitioner resources
            List<Practitioner> practitioners = practitionerBuilder.buildPractitioners(terser, rawHl7Message, messageControlId);
            for (Practitioner practitioner : practitioners) {
                addEntry(bundle, practitioner);
            }

            Practitioner primaryPractitioner = !practitioners.isEmpty() ? practitioners.get(0) : null;
            String practitionerId = primaryPractitioner != null ? primaryPractitioner.getIdPart() : "unknown";
            String practitionerFullName = primaryPractitioner != null ? practitionerBuilder.extractPractitionerFullName(primaryPractitioner) : "Unknown Practitioner";

            if (parentTask != null && primaryPractitioner != null) {
                parentTask.setFor(new Reference("Practitioner/" + primaryPractitioner.getIdPart()).setDisplay(practitionerFullName));
            }

            // 4. Build PractitionerRole resources
            List<PractitionerRole> practitionerRoles = practitionerBuilder.buildPractitionerRoles(terser, rawHl7Message, practitioners, organizations, locations);
            for (PractitionerRole role : practitionerRoles) {
                addEntry(bundle, role);
            }

            if (parentTask != null && !practitionerRoles.isEmpty()) {
                PractitionerRole primaryRole = practitionerRoles.get(0);
                parentTask.setFocus(new Reference("PractitionerRole/" + primaryRole.getIdPart()).setDisplay("PractitionerRole " + primaryRole.getIdPart()));
            }

            // 5. Build Communication resource containing raw MFN message
            Communication comm = metadataBuilder.buildCommunication(terser, rawHl7Message, messageControlId, triggerEvent,
                    practitionerId, practitionerFullName, sendingApp, sendingFacility, messageTimestamp);
            if (comm != null) {
                addEntry(bundle, comm);
            }

            // 6. Build Provenance resource
            Provenance provenance = metadataBuilder.buildProvenance(bundle, primaryPractitioner, messageControlId, triggerEvent,
                    sendingApp, sendingFacility, activityId, activityName);
            if (provenance != null) {
                addEntry(bundle, provenance);
            }

        } catch (Exception e) {
            log.warn("Error deriving FHIR resources from MFN message: {}. Creating baseline resources.", e.getMessage(), e);
            Practitioner fallback = practitionerBuilder.createFallbackPractitioner(parentTask != null ? parentTask.getIdPart() : "unknown");
            addEntry(bundle, fallback);
        }

        FhirSecurityTagManager.applySecurityTags(bundle);
        return bundle;
    }

    public void addEntry(Bundle bundle, Resource resource) {
        if (resource == null || bundle == null) return;
        FhirSecurityTagManager.applyDefaultSecurityTag(resource);
        Bundle.BundleEntryComponent entry = bundle.addEntry();
        entry.setFullUrl(resource.getId());
        entry.setResource(resource);
    }

    public MfnPractitionerResourceBuilder getPractitionerBuilder() {
        return practitionerBuilder;
    }

    public MfnAdministrativeResourceBuilder getAdministrativeBuilder() {
        return administrativeBuilder;
    }

    public MfnMetadataResourceBuilder getMetadataBuilder() {
        return metadataBuilder;
    }

    public HapiContext getHapiContext() {
        return hapiContext;
    }

    public PipeParser getPipeParser() {
        return pipeParser;
    }
}
