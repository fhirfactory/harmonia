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

package net.fhirfactory.harmonia.model.topic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

/**
 * Encapsulates the immutable, hierarchically contained Topic model with full UUID identity.
 * <p>
 * <b>Architectural Context &amp; Authority:</b>
 * In accordance with the accepted 1.1.25 Architecture Continuum ({@code model.core.information.Topic}):
 * <ul>
 *   <li><b>Immutable Identity:</b> A Topic is immutable, independently identifiable, typed, and hierarchically contained.
 *       Its full TopicID is a distinct {@link UUID}.</li>
 *   <li><b>Explicit Canonical Sequencing:</b> {@code canonicalName} is an explicitly sequenced root-to-leaf mapping of
 *       {@code SequenceNumber &rarr; Entry(qualifier, name)}. Gaps, duplicate positions, and ambiguous orders are strictly rejected.</li>
 *   <li><b>Default Qualifiers:</b> An absent qualifier normalises to {@code N0}, {@code N1}, &hellip; by absolute position.</li>
 *   <li><b>Derived Names:</b>
 *     <ul>
 *       <li>{@code name}: joins unqualified entry names in sequence with dots (e.g. {@code "Australia.ACT.Canberra"}).</li>
 *       <li>{@code friendlyName}: the leaf entry name (e.g. {@code "Canberra"}).</li>
 *       <li>{@code relativeName}: the qualified leaf entry (e.g. {@code "City=Canberra"}).</li>
 *     </ul>
 *   </li>
 *   <li><b>No Topic.Version:</b> Versioning belongs to authoritative persisted revisions, operational revisions, and Content Structure versions.</li>
 * </ul>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Topic implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String DOMAIN_HEALTH = "Health";
    public static final String MODEL_HL7 = "HL7";
    public static final String MODEL_FHIR = "FHIR";
    public static final String DEFAULT_HL7_VERSION = "2.4";
    public static final String DEFAULT_FHIR_VERSION = "R5";

    private final UUID id;
    private final SortedMap<Integer, Entry> canonicalName;
    private final String name;
    private final String friendlyName;
    private final String relativeName;

    // Optional routing and source metadata for integration backwards compatibility
    private final String source;
    private final String target;
    private final String origin;
    private final String destination;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private final Date receivedDate;

    /**
     * Canonical constructor establishing validated, immutable Topic state.
     *
     * @param id            the distinct UUID identity of the Topic, must not be null
     * @param canonicalName explicitly sequenced root-to-leaf map of sequence numbers to entries, must not be null or empty
     * @param source        optional source gateway/system identifier
     * @param target        optional target subsystem identifier
     * @param origin        optional sending application/facility
     * @param destination   optional receiving application/facility
     * @param receivedDate  optional ingestion timestamp
     * @throws IllegalArgumentException if {@code id} is null, {@code canonicalName} is null/empty, or sequence contains gaps
     */
    @JsonCreator
    public Topic(
            @JsonProperty("id") UUID id,
            @JsonProperty("canonicalName") Map<Integer, Entry> canonicalName,
            @JsonProperty("source") String source,
            @JsonProperty("target") String target,
            @JsonProperty("origin") String origin,
            @JsonProperty("destination") String destination,
            @JsonProperty("receivedDate") Date receivedDate) {
        if (id == null) {
            throw new IllegalArgumentException("Topic id (UUID) must not be null");
        }
        if (canonicalName == null || canonicalName.isEmpty()) {
            throw new IllegalArgumentException("Topic canonicalName must not be null or empty");
        }

        SortedMap<Integer, Entry> sorted = new TreeMap<>(canonicalName);
        int expectedSeq = 0;
        StringBuilder nameBuilder = new StringBuilder();

        SortedMap<Integer, Entry> validatedMap = new TreeMap<>();
        for (Map.Entry<Integer, Entry> mapEntry : sorted.entrySet()) {
            int seq = mapEntry.getKey();
            if (seq != expectedSeq) {
                throw new IllegalArgumentException(
                        "Topic canonicalName sequence has gaps or invalid order: expected sequence "
                                + expectedSeq + " but found " + seq);
            }
            Entry rawEntry = mapEntry.getValue();
            if (rawEntry == null) {
                throw new IllegalArgumentException("Topic entry at sequence " + seq + " must not be null");
            }
            // Normalize qualifier if absent
            String qualifier = rawEntry.getQualifier();
            if (qualifier == null || qualifier.isBlank()) {
                qualifier = "N" + seq;
            } else {
                qualifier = qualifier.trim();
            }
            Entry normalizedEntry = new Entry(qualifier, rawEntry.getName());
            validatedMap.put(seq, normalizedEntry);

            if (expectedSeq > 0) {
                nameBuilder.append(".");
            }
            nameBuilder.append(normalizedEntry.getName());
            expectedSeq++;
        }

        this.id = id;
        this.canonicalName = Collections.unmodifiableSortedMap(validatedMap);
        this.name = nameBuilder.toString();

        Entry leafEntry = validatedMap.get(validatedMap.size() - 1);
        this.friendlyName = leafEntry.getName();
        this.relativeName = leafEntry.getQualifier() + "=" + leafEntry.getName();

        this.source = source != null && !source.isBlank() ? source.trim() : null;
        this.target = target != null && !target.isBlank() ? target.trim() : null;
        this.origin = origin != null && !origin.isBlank() ? origin.trim() : null;
        this.destination = destination != null && !destination.isBlank() ? destination.trim() : null;
        this.receivedDate = receivedDate != null ? new Date(receivedDate.getTime()) : new Date();
    }

    /**
     * Constructs a Topic with specified ID and canonical entries.
     *
     * @param id            Topic UUID, must not be null
     * @param canonicalName sequence-to-entry map, must not be null or empty
     */
    public Topic(UUID id, Map<Integer, Entry> canonicalName) {
        this(id, canonicalName, null, null, null, null, new Date());
    }

    /**
     * Default constructor creating a default Health.HL7.2.4 topic with random UUID.
     */
    public Topic() {
        this(UUID.randomUUID(), defaultEntries("Health", "HL7", "2.4", "ADT", "A01"), null, null, null, null, new Date());
    }

    /**
     * Convenience constructor for legacy 5-tuple topic definitions.
     */
    public Topic(String domain, String model, String modelVersion, String dataElement, String dataElementQualifier) {
        this(UUID.randomUUID(), defaultEntries(domain, model, modelVersion, dataElement, dataElementQualifier), null, null, null, null, new Date());
    }

    /**
     * Convenience constructor for legacy 5-tuple topic definitions with source.
     */
    public Topic(String domain, String model, String modelVersion, String dataElement, String dataElementQualifier, String source) {
        this(UUID.randomUUID(), defaultEntries(domain, model, modelVersion, dataElement, dataElementQualifier), source, null, null, null, new Date());
    }

    /**
     * Convenience constructor for legacy 5-tuple topic definitions with full routing.
     */
    public Topic(String domain, String model, String modelVersion, String dataElement, String dataElementQualifier,
                 String source, String target, String origin, String destination, Date receivedDate) {
        this(UUID.randomUUID(), defaultEntries(domain, model, modelVersion, dataElement, dataElementQualifier), source, target, origin, destination, receivedDate);
    }

    /**
     * Copy constructor.
     *
     * @param other other Topic to copy from
     */
    public Topic(Topic other) {
        this(
                other != null ? other.id : UUID.randomUUID(),
                other != null ? other.canonicalName : defaultEntries("Health", "HL7", "2.4", "ADT", "A01"),
                other != null ? other.source : null,
                other != null ? other.target : null,
                other != null ? other.origin : null,
                other != null ? other.destination : null,
                other != null && other.receivedDate != null ? new Date(other.receivedDate.getTime()) : new Date()
        );
    }

    private static Map<Integer, Entry> defaultEntries(String domain, String model, String modelVersion, String dataElement, String dataElementQualifier) {
        SortedMap<Integer, Entry> map = new TreeMap<>();
        int seq = 0;
        if (domain != null && !domain.isBlank()) {
            map.put(seq++, new Entry("Domain", domain));
        } else {
            map.put(seq++, new Entry("Domain", DOMAIN_HEALTH));
        }
        if (model != null && !model.isBlank()) {
            map.put(seq++, new Entry("Model", model));
        } else {
            map.put(seq++, new Entry("Model", MODEL_HL7));
        }
        if (modelVersion != null && !modelVersion.isBlank()) {
            map.put(seq++, new Entry("ModelVersion", modelVersion));
        } else {
            map.put(seq++, new Entry("ModelVersion", DEFAULT_HL7_VERSION));
        }
        if (dataElement != null && !dataElement.isBlank()) {
            map.put(seq++, new Entry("DataElement", dataElement));
        } else {
            map.put(seq++, new Entry("DataElement", "ADT"));
        }
        if (dataElementQualifier != null && !dataElementQualifier.isBlank()) {
            map.put(seq++, new Entry("DataElementQualifier", dataElementQualifier));
        } else {
            map.put(seq++, new Entry("DataElementQualifier", "*"));
        }
        return map;
    }

    // =========================================================================
    // Factory Helpers
    // =========================================================================

    /**
     * Creates a Topic from an explicit UUID and dot-delimited name.
     *
     * @param id               Topic UUID, must not be null
     * @param dotDelimitedName dot-separated element names, must not be null or blank
     * @return constructed immutable Topic
     */
    public static Topic fromName(UUID id, String dotDelimitedName) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        if (dotDelimitedName == null || dotDelimitedName.isBlank()) {
            throw new IllegalArgumentException("dotDelimitedName must not be null or blank");
        }
        String[] parts = dotDelimitedName.trim().split("\\.", -1);
        SortedMap<Integer, Entry> map = new TreeMap<>();
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i].trim();
            if (part.isEmpty()) {
                throw new IllegalArgumentException("Empty element in topic name: '" + dotDelimitedName + "'");
            }
            map.put(i, Entry.of(i, part));
        }
        return new Topic(id, map);
    }

    /**
     * Creates a Topic from a dot-delimited name with a deterministically derived UUID.
     *
     * @param dotDelimitedName dot-separated element names, must not be null or blank
     * @return constructed immutable Topic
     */
    public static Topic fromName(String dotDelimitedName) {
        if (dotDelimitedName == null || dotDelimitedName.isBlank()) {
            throw new IllegalArgumentException("dotDelimitedName must not be null or blank");
        }
        UUID deterministicUuid = UUID.nameUUIDFromBytes(dotDelimitedName.trim().getBytes(StandardCharsets.UTF_8));
        return fromName(deterministicUuid, dotDelimitedName);
    }

    /**
     * Creates a canonical Information Unit Topic for a given IU UUID.
     * <p>
     * Pattern: {@code harmonia.mneme.iu.<32-hex-compressed-uuid>}
     *
     * @param iuUuid the Information Unit UUID, must not be null
     * @return canonical Topic for the IU
     */
    public static Topic forInformationUnit(UUID iuUuid) {
        if (iuUuid == null) {
            throw new IllegalArgumentException("iuUuid must not be null");
        }
        String hex = iuUuid.toString().replace("-", "").toLowerCase();
        return forInformationUnit(hex);
    }

    /**
     * Creates a canonical Information Unit Topic for a 32-character hex compressed UUID string.
     *
     * @param hexCompressedUuid 32-character hexadecimal string, must not be null or blank
     * @return canonical Topic for the IU
     */
    public static Topic forInformationUnit(String hexCompressedUuid) {
        if (hexCompressedUuid == null || hexCompressedUuid.isBlank()) {
            throw new IllegalArgumentException("hexCompressedUuid must not be null or blank");
        }
        String hex = hexCompressedUuid.trim().replace("-", "").toLowerCase();
        if (hex.length() != 32) {
            throw new IllegalArgumentException("hexCompressedUuid must be exactly 32 hex characters, got: '" + hexCompressedUuid + "'");
        }
        String fullName = "harmonia.mneme.iu." + hex;
        UUID topicId = UUID.nameUUIDFromBytes(fullName.getBytes(StandardCharsets.UTF_8));

        SortedMap<Integer, Entry> map = new TreeMap<>();
        map.put(0, new Entry("Domain", "harmonia"));
        map.put(1, new Entry("Subsystem", "mneme"));
        map.put(2, new Entry("Entity", "iu"));
        map.put(3, new Entry("Identifier", hex));

        return new Topic(topicId, map);
    }

    /**
     * Creates a Topic for an HL7 v2 message.
     */
    public static Topic fromHl7(String messageType, String triggerEvent, String source) {
        return fromHl7(DEFAULT_HL7_VERSION, messageType, triggerEvent, source, null, null, null);
    }

    /**
     * Creates a Topic from a composite trigger string (e.g. "ADT^A01", "A01", "ORU^R01").
     */
    public static Topic fromHl7(String triggerType, String source) {
        String msgType = "*";
        String trigger = triggerType;
        if (triggerType != null && triggerType.contains("^")) {
            String[] parts = triggerType.split("\\^");
            msgType = parts[0];
            trigger = parts.length > 1 ? parts[1] : "*";
        } else if (triggerType == null || triggerType.isBlank() || "*".equals(triggerType)) {
            msgType = "*";
            trigger = "*";
        } else if (triggerType.startsWith("A")) {
            msgType = "ADT";
        } else if (triggerType.startsWith("R")) {
            msgType = "ORU";
        } else if (triggerType.startsWith("O")) {
            msgType = "ORM";
        } else if (triggerType.startsWith("T")) {
            msgType = "MDM";
        } else if (triggerType.startsWith("S")) {
            msgType = "SIU";
        }
        return fromHl7(DEFAULT_HL7_VERSION, msgType, trigger, source != null && !source.isBlank() ? source : "*", null, null, null);
    }

    /**
     * Creates a Topic for an HL7 message with full origin and destination routing.
     */
    public static Topic fromHl7(String version, String messageType, String triggerEvent,
                                String source, String target, String origin, String destination) {
        SortedMap<Integer, Entry> map = new TreeMap<>();
        map.put(0, new Entry("Domain", DOMAIN_HEALTH));
        map.put(1, new Entry("Model", MODEL_HL7));
        map.put(2, new Entry("ModelVersion", version != null && !version.isBlank() ? version : DEFAULT_HL7_VERSION));
        map.put(3, new Entry("DataElement", messageType != null && !messageType.isBlank() ? messageType : "ADT"));
        map.put(4, new Entry("DataElementQualifier", triggerEvent != null && !triggerEvent.isBlank() ? triggerEvent : "*"));

        return new Topic(UUID.randomUUID(), map, source, target, origin, destination, new Date());
    }

    /**
     * Creates a Topic for FHIR resource events.
     */
    public static Topic fromFhir(String resourceType, String interaction, String source) {
        SortedMap<Integer, Entry> map = new TreeMap<>();
        map.put(0, new Entry("Domain", DOMAIN_HEALTH));
        map.put(1, new Entry("Model", MODEL_FHIR));
        map.put(2, new Entry("ModelVersion", DEFAULT_FHIR_VERSION));
        map.put(3, new Entry("DataElement", resourceType != null && !resourceType.isBlank() ? resourceType : "Resource"));
        map.put(4, new Entry("DataElementQualifier", interaction != null ? interaction : "create"));

        return new Topic(UUID.randomUUID(), map, source, null, null, null, new Date());
    }

    /**
     * Creates an egress topic configured for routing out of a gateway.
     */
    public static Topic forEgress(String messageType, String triggerEvent, String source, String target, String destination) {
        return fromHl7(DEFAULT_HL7_VERSION, messageType, triggerEvent, source, target, null, destination);
    }

    /**
     * Creates a fluent {@link Builder} for constructing a {@code Topic}.
     *
     * @return a new Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    // =========================================================================
    // Core Accessors
    // =========================================================================

    public UUID getId() {
        return id;
    }

    public SortedMap<Integer, Entry> getCanonicalName() {
        return canonicalName;
    }

    public String getName() {
        return name;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public String getRelativeName() {
        return relativeName;
    }

    public int getDepth() {
        return canonicalName.size();
    }

    public Entry getEntry(int sequenceNumber) {
        return canonicalName.get(sequenceNumber);
    }

    // =========================================================================
    // Formatting & Diagnostic Helpers
    // =========================================================================

    /**
     * Returns a short diagnostic display of the Topic UUID (first 8 characters).
     *
     * @return 8-character UUID prefix
     */
    public String getShortId() {
        return id.toString().substring(0, 8);
    }

    /**
     * Returns a human-readable canonical string displaying explicitly sequenced entries.
     *
     * @return formatted canonical sequence string, e.g. {@code "[0: Country=Australia, 1: StateTerritory=ACT, 2: City=Canberra]"}
     */
    public String toCanonicalString() {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Map.Entry<Integer, Entry> e : canonicalName.entrySet()) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(e.getValue().toCanonicalString(e.getKey()));
            first = false;
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Returns a diagnostic logging string containing canonical sequence and Topic UUID.
     *
     * @return diagnostic string with short ID and canonical entries
     */
    public String toFormattedString() {
        return toCanonicalString() + " (id: " + id + ")";
    }

    /**
     * Alias for {@link #getName()} for backwards compatibility.
     *
     * @return derived dot-delimited topic name
     */
    public String toTopicString() {
        return name;
    }

    // =========================================================================
    // Legacy / Backwards Compatibility Accessors
    // =========================================================================

    public String getDomain() {
        Entry e = getEntryByQualifierOrSeq("Domain", 0);
        return e != null ? e.getName() : DOMAIN_HEALTH;
    }

    public String getModel() {
        Entry e = getEntryByQualifierOrSeq("Model", 1);
        return e != null ? e.getName() : MODEL_HL7;
    }

    public String getModelVersion() {
        Entry e = getEntryByQualifierOrSeq("ModelVersion", 2);
        return e != null ? e.getName() : DEFAULT_HL7_VERSION;
    }

    public String getDataElement() {
        Entry e = getEntryByQualifierOrSeq("DataElement", 3);
        return e != null ? e.getName() : "";
    }

    public String getDataElementQualifier() {
        Entry e = getEntryByQualifierOrSeq("DataElementQualifier", 4);
        return e != null ? e.getName() : "";
    }

    public String getCompositeTrigger() {
        String de = getDataElement();
        String deq = getDataElementQualifier();
        if (deq != null && !deq.isBlank()) {
            return de + "^" + deq;
        }
        return de;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public Date getReceivedDate() {
        return receivedDate != null ? new Date(receivedDate.getTime()) : null;
    }

    private Entry getEntryByQualifierOrSeq(String qualifier, int seq) {
        for (Entry e : canonicalName.values()) {
            if (qualifier.equalsIgnoreCase(e.getQualifier())) {
                return e;
            }
        }
        return canonicalName.get(seq);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Topic topic = (Topic) o;
        return Objects.equals(id, topic.id) &&
                Objects.equals(canonicalName, topic.canonicalName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, canonicalName);
    }

    @Override
    public String toString() {
        return "Topic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", canonical=" + toCanonicalString() +
                (source != null ? ", source='" + source + '\'' : "") +
                (destination != null ? ", destination='" + destination + '\'' : "") +
                '}';
    }

    // =========================================================================
    // Nested Classes: Entry & Builder
    // =========================================================================

    /**
     * An immutable element of a Topic's canonical name, pairing an optional typed qualifier with an unqualified element name.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Entry implements Serializable, Comparable<Entry> {

        private static final long serialVersionUID = 1L;

        private final String qualifier;
        private final String name;

        @JsonCreator
        public Entry(
                @JsonProperty("qualifier") String qualifier,
                @JsonProperty("name") String name) {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("Topic entry name must not be null or blank");
            }
            this.name = name.trim();
            this.qualifier = qualifier != null && !qualifier.isBlank() ? qualifier.trim() : null;
        }

        public static Entry of(int sequenceNumber, String qualifier, String name) {
            String effQualifier = (qualifier != null && !qualifier.isBlank()) ? qualifier.trim() : ("N" + sequenceNumber);
            return new Entry(effQualifier, name);
        }

        public static Entry of(int sequenceNumber, String name) {
            return of(sequenceNumber, null, name);
        }

        public String getQualifier() {
            return qualifier;
        }

        public String getName() {
            return name;
        }

        public String toCanonicalString(int sequenceNumber) {
            String q = qualifier != null ? qualifier : ("N" + sequenceNumber);
            return sequenceNumber + ": " + q + "=" + name;
        }

        public String toRelativeString() {
            return (qualifier != null ? qualifier : "") + "=" + name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Entry entry = (Entry) o;
            return Objects.equals(qualifier, entry.qualifier) && Objects.equals(name, entry.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(qualifier, name);
        }

        @Override
        public String toString() {
            return (qualifier != null ? qualifier + "=" : "") + name;
        }

        @Override
        public int compareTo(Entry o) {
            int cmp = Objects.compare(this.qualifier, o.qualifier, Comparator.nullsFirst(String::compareTo));
            if (cmp != 0) return cmp;
            return this.name.compareTo(o.name);
        }
    }

    /**
     * Fluent builder for constructing immutable {@link Topic} instances.
     */
    public static final class Builder {

        private UUID id;
        private final SortedMap<Integer, Entry> entries = new TreeMap<>();
        private int nextSeq = 0;
        private String source;
        private String target;
        private String origin;
        private String destination;
        private Date receivedDate;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder addEntry(int sequenceNumber, String qualifier, String name) {
            entries.put(sequenceNumber, Entry.of(sequenceNumber, qualifier, name));
            if (sequenceNumber >= nextSeq) {
                nextSeq = sequenceNumber + 1;
            }
            return this;
        }

        public Builder addEntry(int sequenceNumber, String name) {
            return addEntry(sequenceNumber, null, name);
        }

        public Builder addEntry(String qualifier, String name) {
            int seq = nextSeq++;
            entries.put(seq, Entry.of(seq, qualifier, name));
            return this;
        }

        public Builder addEntry(String name) {
            return addEntry(null, name);
        }

        public Builder source(String source) {
            this.source = source;
            return this;
        }

        public Builder target(String target) {
            this.target = target;
            return this;
        }

        public Builder origin(String origin) {
            this.origin = origin;
            return this;
        }

        public Builder destination(String destination) {
            this.destination = destination;
            return this;
        }

        public Builder receivedDate(Date receivedDate) {
            this.receivedDate = receivedDate;
            return this;
        }

        public Topic build() {
            UUID topicId = id != null ? id : UUID.randomUUID();
            return new Topic(topicId, entries, source, target, origin, destination, receivedDate);
        }
    }
}
