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

package net.fhirfactory.harmonia.model.core.naming.factory;

import net.fhirfactory.harmonia.model.core.naming.Topic;
import net.fhirfactory.harmonia.model.core.naming.valuesets.TopicValueSets;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Factory responsible for creating standard {@link Topic} instances across Harmonia.
 * <p>
 * Encapsulates the construction logic for standard domain naming structures such as HL7 v2 messages,
 * FHIR resources/interactions, and InformationUnit identifiers.
 */
public final class TopicFactory {

    private TopicFactory() {
        // Utility factory
    }

    /**
     * Builds a Topic from a dot-separated string name (e.g. {@code Health.HL7.2.4.ADT.A01} or
     * {@code Domain=Health.Model=HL7.ModelVersion=2.4.DataElement=ADT.DataElementQualifier=A01}).
     *
     * @param dotSeparatedName dot-delimited topic name string; must not be blank
     * @return constructed Topic
     * @throws IllegalArgumentException if dotSeparatedName is null or blank
     */
    public static Topic fromName(String dotSeparatedName) {
        return fromName(UUID.randomUUID(), dotSeparatedName);
    }

    /**
     * Builds a Topic with an explicit identifier from a dot-separated string name.
     *
     * @param id               non-null Topic identifier
     * @param dotSeparatedName dot-delimited topic name string; must not be blank
     * @return constructed Topic
     * @throws IllegalArgumentException if id is null or dotSeparatedName is null or blank
     */
    public static Topic fromName(UUID id, String dotSeparatedName) {
        Objects.requireNonNull(id, "Topic id must not be null");
        if (StringUtils.isBlank(dotSeparatedName)) {
            throw new IllegalArgumentException("dotSeparatedName must not be null or blank");
        }
        String[] parts = dotSeparatedName.trim().split("\\.");
        List<String> segments = new ArrayList<>();
        if (!dotSeparatedName.contains("=") && parts.length >= 4 && StringUtils.isNumeric(parts[2]) && StringUtils.isNumeric(parts[3])) {
            segments.add(parts[0]);
            segments.add(parts[1]);
            segments.add(parts[2] + "." + parts[3]);
            for (int i = 4; i < parts.length; i++) {
                segments.add(parts[i]);
            }
        } else {
            segments.addAll(Arrays.asList(parts));
        }

        TopicBuilder builder = new TopicBuilder(id);

        String[] standardQualifiers = new String[]{
                TopicValueSets.QUALIFIER_DOMAIN,
                TopicValueSets.QUALIFIER_MODEL,
                TopicValueSets.QUALIFIER_MODEL_VERSION,
                TopicValueSets.QUALIFIER_DATA_ELEMENT,
                TopicValueSets.QUALIFIER_DATA_ELEMENT_QUALIFIER
        };

        for (int i = 0; i < segments.size(); i++) {
            String part = segments.get(i).trim();
            if (part.contains("=")) {
                String[] qn = part.split("=", 2);
                builder.addEntry(i, qn[0].trim(), qn[1].trim());
            } else {
                String qualifier = i < standardQualifiers.length ? standardQualifiers[i] : "Level" + i;
                builder.addEntry(i, qualifier, part);
            }
        }
        return builder.build();
    }

    /**
     * Constructs a Topic representing an InformationUnit.
     *
     * @param informationUnitId non-null identifier
     * @return constructed Topic
     */
    public static Topic forInformationUnit(UUID informationUnitId) {
        Objects.requireNonNull(informationUnitId, "informationUnitId must not be null");
        return forInformationUnit(informationUnitId.toString().replace("-", "").toLowerCase());
    }

    public static Topic forInformationUnit(String informationUnitId) {
        if (StringUtils.isBlank(informationUnitId)) {
            throw new IllegalArgumentException("informationUnitId must not be blank");
        }
        String hex = informationUnitId.trim().replace("-", "").toLowerCase();
        return new TopicBuilder()
                .addEntry(0, TopicValueSets.QUALIFIER_DOMAIN, "harmonia")
                .addEntry(1, TopicValueSets.QUALIFIER_MODEL, "mneme")
                .addEntry(2, TopicValueSets.QUALIFIER_MODEL_VERSION, "iu")
                .addEntry(3, TopicValueSets.QUALIFIER_DATA_ELEMENT, hex)
                .build();
    }

    /**
     * Constructs a Topic representing an HL7 v2.4 message event.
     *
     * @param messageType  HL7 message type (e.g. ADT, ORM, ORU)
     * @param triggerEvent HL7 trigger event (e.g. A01, O01, R01)
     * @return constructed Topic
     */
    public static Topic fromHl7(String messageType, String triggerEvent) {
        return fromHl7("2.4", messageType, triggerEvent);
    }

    /**
     * Constructs a Topic representing an HL7 v2 message event with explicit version.
     *
     * @param version      HL7 version (e.g. 2.4)
     * @param messageType  HL7 message type (e.g. ADT, ORM, ORU)
     * @param triggerEvent HL7 trigger event (e.g. A01, O01, R01)
     * @return constructed Topic
     */
    public static Topic fromHl7(String version, String messageType, String triggerEvent) {
        String cleanVersion = StringUtils.defaultIfBlank(version, "2.4");
        String cleanType = StringUtils.defaultIfBlank(messageType, "ADT");
        String cleanTrigger = StringUtils.defaultIfBlank(triggerEvent, "A01");
        return new Topic(TopicValueSets.DEFAULT_DOMAIN, "HL7", cleanVersion, cleanType, cleanTrigger);
    }

    /**
     * Constructs a Topic representing an HL7 v2 event from a composite trigger (e.g. {@code ADT^A01}).
     *
     * @param triggerType composite trigger string
     * @return constructed Topic
     */
    public static Topic fromHl7(String triggerType) {
        if (StringUtils.isBlank(triggerType)) {
            return fromHl7("ADT", "A01");
        }
        if (triggerType.contains("^")) {
            String[] parts = triggerType.split("\\^", 2);
            return fromHl7(parts[0], parts[1]);
        }
        return fromHl7(triggerType, "ALL");
    }

    /**
     * Constructs a Topic representing a FHIR R5 interaction.
     *
     * @param resourceType FHIR resource type (e.g. Practitioner, Organization)
     * @param interaction  interaction type (e.g. CREATE, UPDATE, DELETE)
     * @return constructed Topic
     */
    public static Topic fromFhir(String resourceType, String interaction) {
        String cleanResource = StringUtils.defaultIfBlank(resourceType, "Practitioner");
        String cleanInteraction = StringUtils.defaultIfBlank(interaction, "CREATE");
        return new Topic(TopicValueSets.DEFAULT_DOMAIN, "FHIR", "R5", cleanResource, cleanInteraction);
    }
}
