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

package net.fhirfactory.harmonia.model.core.naming;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.fhirfactory.harmonia.model.core.naming.datatypes.TopicEntry;
import net.fhirfactory.harmonia.model.core.naming.factory.TopicBuilder;
import net.fhirfactory.harmonia.model.core.naming.factory.TopicFactory;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.*;

/**
 * An immutable naming and identity construct within Harmonia's Architecture Continuum.
 * <p>
 * A Topic represents the classification and identity of data elements or information units
 * independently of any message transport or routing envelope.
 * <p>
 * In accordance with the accepted Architecture Continuum:
 * <ul>
 *   <li>A Topic is identified by a unique {@link UUID}.</li>
 *   <li>A Topic contains an explicitly sequenced zero-based {@link SortedMap} of {@link TopicEntry}
 *       elements with no gaps.</li>
 *   <li>The unqualified dot-delimited name is derived from the sequence of entry names.</li>
 *   <li>A Topic is a pure naming/identity model and does not contain routing metadata such as
 *       source, target, destination, or transport timestamps.</li>
 * </ul>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Topic implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String DEFAULT_DOMAIN = "Health";
    public static final String QUALIFIER_DOMAIN = "Domain";
    public static final String QUALIFIER_MODEL = "Model";
    public static final String QUALIFIER_MODEL_VERSION = "ModelVersion";
    public static final String QUALIFIER_DATA_ELEMENT = "DataElement";
    public static final String QUALIFIER_DATA_ELEMENT_QUALIFIER = "DataElementQualifier";

    private final UUID id;
    private final SortedMap<Integer, TopicEntry> canonicalName;
    private final String name;
    private final String friendlyName;
    private final String relativeName;

    /**
     * Constructs an immutable Topic from an explicit identifier and sequenced entries.
     *
     * @param id            non-null unique identifier
     * @param canonicalName non-null, non-empty, zero-based gap-free sequence of entries
     * @throws IllegalArgumentException if id is null, canonicalName is empty or contains gaps
     */
    @JsonCreator
    public Topic(
            @JsonProperty("id") UUID id,
            @JsonProperty("canonicalName") SortedMap<Integer, TopicEntry> canonicalName) {
        if (id == null) {
            throw new IllegalArgumentException("Topic id must not be null");
        }
        if (canonicalName == null || canonicalName.isEmpty()) {
            throw new IllegalArgumentException("Topic canonicalName must not be null or empty");
        }
        SortedMap<Integer, TopicEntry> copy = new TreeMap<>(canonicalName);
        for (int i = 0; i < copy.size(); i++) {
            if (!copy.containsKey(i)) {
                throw new IllegalArgumentException("Topic canonicalName sequence has gap at position: " + i);
            }
        }
        this.id = id;
        this.canonicalName = Collections.unmodifiableSortedMap(copy);

        StringBuilder sb = new StringBuilder();
        for (TopicEntry entry : this.canonicalName.values()) {
            if (sb.length() > 0) {
                sb.append(".");
            }
            sb.append(entry.getName());
        }
        this.name = sb.toString();

        TopicEntry leaf = this.canonicalName.get(this.canonicalName.lastKey());
        this.friendlyName = leaf.getName();
        this.relativeName = leaf.toCanonicalString();
    }

    /**
     * Constructs a canonical 5-tuple Health Topic with the given explicit UUID.
     *
     * @param id                   non-null identifier
     * @param domain               domain name (e.g. Health)
     * @param model                model name (e.g. HL7, FHIR)
     * @param modelVersion         model version (e.g. 2.4, R5)
     * @param dataElement          data element (e.g. ADT, Practitioner)
     * @param dataElementQualifier data element qualifier (e.g. A01, CREATE)
     */
    public Topic(UUID id, String domain, String model, String modelVersion,
                 String dataElement, String dataElementQualifier) {
        this(id, build5TupleEntries(domain, model, modelVersion, dataElement, dataElementQualifier));
    }

    /**
     * Constructs a canonical 5-tuple Health Topic with a random UUID.
     *
     * @param domain               domain name (e.g. Health)
     * @param model                model name (e.g. HL7, FHIR)
     * @param modelVersion         model version (e.g. 2.4, R5)
     * @param dataElement          data element (e.g. ADT, Practitioner)
     * @param dataElementQualifier data element qualifier (e.g. A01, CREATE)
     */
    public Topic(String domain, String model, String modelVersion,
                 String dataElement, String dataElementQualifier) {
        this(UUID.randomUUID(), domain, model, modelVersion, dataElement, dataElementQualifier);
    }

    private static SortedMap<Integer, TopicEntry> build5TupleEntries(
            String domain, String model, String modelVersion,
            String dataElement, String dataElementQualifier) {
        SortedMap<Integer, TopicEntry> map = new TreeMap<>();
        map.put(0, new TopicEntry(QUALIFIER_DOMAIN, StringUtils.defaultIfBlank(domain, DEFAULT_DOMAIN)));
        map.put(1, new TopicEntry(QUALIFIER_MODEL, StringUtils.defaultIfBlank(model, "FHIR")));
        map.put(2, new TopicEntry(QUALIFIER_MODEL_VERSION, StringUtils.defaultIfBlank(modelVersion, "R5")));
        map.put(3, new TopicEntry(QUALIFIER_DATA_ELEMENT, StringUtils.defaultIfBlank(dataElement, "General")));
        map.put(4, new TopicEntry(QUALIFIER_DATA_ELEMENT_QUALIFIER, StringUtils.defaultIfBlank(dataElementQualifier, "Default")));
        return map;
    }

    /**
     * Returns the unique identifier of this Topic.
     *
     * @return non-null UUID
     */
    public UUID getId() {
        return id;
    }

    /**
     * Returns an unmodifiable view of the canonical name map.
     *
     * @return unmodifiable map of sequence numbers to TopicEntry
     */
    public SortedMap<Integer, TopicEntry> getCanonicalName() {
        return canonicalName;
    }

    /**
     * Returns the unqualified dot-delimited name of this Topic.
     *
     * @return dot-delimited name string
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the friendly name (leaf entry name).
     *
     * @return leaf entry name string
     */
    public String getFriendlyName() {
        return friendlyName;
    }

    /**
     * Returns the relative name (leaf entry as {@code qualifier=name}).
     *
     * @return leaf entry formatted string
     */
    public String getRelativeName() {
        return relativeName;
    }

    /**
     * Returns the depth (number of levels) of this Topic's canonical name.
     *
     * @return positive integer depth
     */
    public int getDepth() {
        return canonicalName.size();
    }

    /**
     * Returns the entry at the specified zero-based sequence position.
     *
     * @param sequenceNumber sequence number
     * @return TopicEntry at that position, or {@code null} if out of bounds
     */
    public TopicEntry getEntry(int sequenceNumber) {
        return canonicalName.get(sequenceNumber);
    }

    /**
     * Returns the first 8 characters of the Topic UUID.
     *
     * @return 8-character string
     */
    public String getShortId() {
        return id.toString().substring(0, 8);
    }

    /**
     * Returns the Domain name (entry 0).
     */
    public String getDomain() {
        TopicEntry entry = canonicalName.get(0);
        return entry != null ? entry.getName() : null;
    }

    /**
     * Returns the Model name (entry 1).
     */
    public String getModel() {
        TopicEntry entry = canonicalName.get(1);
        return entry != null ? entry.getName() : null;
    }

    /**
     * Returns the ModelVersion (entry 2).
     */
    public String getModelVersion() {
        TopicEntry entry = canonicalName.get(2);
        return entry != null ? entry.getName() : null;
    }

    /**
     * Returns the DataElement (entry 3).
     */
    public String getDataElement() {
        TopicEntry entry = canonicalName.get(3);
        return entry != null ? entry.getName() : null;
    }

    /**
     * Returns the DataElementQualifier (entry 4).
     */
    public String getDataElementQualifier() {
        TopicEntry entry = canonicalName.get(4);
        return entry != null ? entry.getName() : null;
    }

    /**
     * Returns the composite trigger string (e.g. {@code ADT^A01}).
     *
     * @return composite trigger or empty string
     */
    public String getCompositeTrigger() {
        String elem = getDataElement();
        String qual = getDataElementQualifier();
        if (elem == null && qual == null) {
            return "";
        }
        return (elem != null ? elem : "") + "^" + (qual != null ? qual : "");
    }

    /**
     * Formats this Topic as its canonical string: {@code qualifier=name.qualifier=name...}.
     *
     * @return canonical string representation
     */
    public String toCanonicalString() {
        StringBuilder sb = new StringBuilder();
        for (TopicEntry entry : canonicalName.values()) {
            if (sb.length() > 0) {
                sb.append(".");
            }
            sb.append(entry.toCanonicalString());
        }
        return sb.toString();
    }

    /**
     * Formats this Topic as its dot-delimited name string.
     *
     * @return name string
     */
    public String toTopicString() {
        return name;
    }

    /**
     * Formats this Topic with its name and short identifier.
     *
     * @return formatted diagnostic string
     */
    public String toFormattedString() {
        return name + " [id=" + id + "]";
    }

    // --- Static Factory and Builder Entry Points ---

    public static TopicBuilder builder() {
        return new TopicBuilder();
    }

    public static TopicBuilder builder(UUID id) {
        return new TopicBuilder(id);
    }

    public static Topic fromName(String dotSeparatedName) {
        return TopicFactory.fromName(dotSeparatedName);
    }

    public static Topic fromName(UUID id, String dotSeparatedName) {
        return TopicFactory.fromName(id, dotSeparatedName);
    }

    public static Topic forInformationUnit(UUID informationUnitId) {
        return TopicFactory.forInformationUnit(informationUnitId);
    }

    public static Topic forInformationUnit(String informationUnitId) {
        return TopicFactory.forInformationUnit(informationUnitId);
    }

    public static Topic fromHl7(String messageType, String triggerEvent) {
        return TopicFactory.fromHl7(messageType, triggerEvent);
    }

    public static Topic fromHl7(String version, String messageType, String triggerEvent) {
        return TopicFactory.fromHl7(version, messageType, triggerEvent);
    }

    public static Topic fromHl7(String triggerType) {
        return TopicFactory.fromHl7(triggerType);
    }

    public static Topic fromFhir(String resourceType, String interaction) {
        return TopicFactory.fromFhir(resourceType, interaction);
    }

    // --- Nested Aliases for Backward Compatibility ---

    public static class Entry extends TopicEntry {
        public Entry(String qualifier, String name) {
            super(qualifier, name);
        }
    }

    public static class Builder extends TopicBuilder {
        public Builder() {
            super();
        }

        public Builder(UUID id) {
            super(id);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Topic)) return false;
        Topic topic = (Topic) o;
        return Objects.equals(canonicalName, topic.canonicalName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(canonicalName);
    }

    @Override
    public String toString() {
        return toCanonicalString();
    }
}
