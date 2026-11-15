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

package net.fhirfactory.harmonia.model.core.information;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.fhirfactory.harmonia.model.topic.Topic;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Encapsulates the immutable, persistent identity and canonical Topic of an Information Unit (IU).
 * <p>
 * <b>Architectural Context &amp; Authority:</b>
 * In accordance with the accepted Harmonia Architecture Continuum (1.1.22/1.1.24) and
 * {@code DRP-Information-Unit-Framework.Final.001.md} (§§3, 4; IU-01, IU-06):
 * <ul>
 *   <li>An Information Unit possesses its own persistent unique identity (a standard {@link UUID})
 *       and a corresponding canonical hierarchical <b>Topic</b>.</li>
 *   <li>The canonical Topic follows the agreed hierarchical naming pattern:
 *       <pre>{@code harmonia.mneme.iu.<hex-string-of-compressed-uuid>}</pre></li>
 *   <li>The 32-character hexadecimal representation is a deterministic and reversible encoding
 *       of the 128-bit UUID (lowercase hexadecimal without hyphens).</li>
 *   <li>IU identity is strictly distinct from represented-entity identity, native Content version,
 *       persistence-provider identifiers, transport routing keys, and correlation identifiers.</li>
 *   <li>The {@code harmonia.mneme.iu} namespace provides canonical classification and does not
 *       transfer ownership of the foundational Information Unit model to Mneme.</li>
 * </ul>
 * <p>
 * <b>Defensive Programming &amp; Invariants:</b>
 * <ul>
 *   <li>The encapsulated {@link UUID} and canonical topic string must never be null.</li>
 *   <li>Bi-directional mapping between UUID and Topic string is deterministic and lossless.</li>
 *   <li>Parsing from topic strings enforces the {@code harmonia.mneme.iu.} prefix and exact 32-hex format.</li>
 * </ul>
 */
public final class InformationUnitIdentifier implements Serializable, Comparable<InformationUnitIdentifier> {

    private static final long serialVersionUID = 1L;

    /**
     * Canonical topic prefix for all Information Unit topics.
     */
    public static final String TOPIC_PREFIX = "harmonia.mneme.iu.";

    /**
     * Expected length of the compressed hexadecimal representation of a 128-bit UUID (32 characters).
     */
    public static final int HEX_LENGTH = 32;

    private final UUID uuid;
    private final String topicName;

    /**
     * Private constructor establishing the validated UUID and its derived canonical topic.
     *
     * @param uuid the persistent UUID, must not be null
     */
    private InformationUnitIdentifier(UUID uuid) {
        this.uuid = Objects.requireNonNull(uuid, "uuid must not be null");
        this.topicName = TOPIC_PREFIX + uuidToCompressedHex(uuid);
    }

    /**
     * JSON creator constructor validating consistency between UUID and topic name.
     *
     * @param uuid      the UUID
     * @param topicName the optional topic name (if provided, must match canonical derivation)
     */
    @JsonCreator
    public static InformationUnitIdentifier of(
            @JsonProperty("uuid") UUID uuid,
            @JsonProperty("topicName") String topicName) {
        Objects.requireNonNull(uuid, "uuid must not be null");
        InformationUnitIdentifier identifier = new InformationUnitIdentifier(uuid);
        if (topicName != null && !topicName.isBlank()) {
            if (!identifier.topicName.equalsIgnoreCase(topicName)) {
                throw new IllegalArgumentException(
                        "Provided topicName '" + topicName + "' does not match derived canonical topic '" + identifier.topicName + "'");
            }
        }
        return identifier;
    }

    /**
     * Creates an {@code InformationUnitIdentifier} from a {@link UUID}.
     *
     * @param uuid the persistent unique identifier, must not be null
     * @return the InformationUnitIdentifier
     * @throws NullPointerException if {@code uuid} is null
     */
    public static InformationUnitIdentifier fromUuid(UUID uuid) {
        return new InformationUnitIdentifier(uuid);
    }

    /**
     * Creates an {@code InformationUnitIdentifier} from a UUID string representation.
     *
     * @param uuidString the standard UUID string (e.g. {@code "123e4567-e89b-12d3-a456-426614174000"}), must not be null or blank
     * @return the InformationUnitIdentifier
     * @throws IllegalArgumentException if {@code uuidString} is null, blank, or malformed
     */
    public static InformationUnitIdentifier fromUuidString(String uuidString) {
        if (uuidString == null || uuidString.isBlank()) {
            throw new IllegalArgumentException("uuidString must not be null or blank");
        }
        try {
            return new InformationUnitIdentifier(UUID.fromString(uuidString.trim()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid UUID string format: '" + uuidString + "'", e);
        }
    }

    /**
     * Creates an {@code InformationUnitIdentifier} by parsing a canonical topic name
     * (e.g. {@code "harmonia.mneme.iu.0123456789abcdef0123456789abcdef"}).
     *
     * @param topicName the canonical topic string, must not be null or blank
     * @return the derived InformationUnitIdentifier
     * @throws IllegalArgumentException if {@code topicName} is null, blank, does not start with {@value #TOPIC_PREFIX},
     *                                  or does not contain a valid 32-character hexadecimal UUID
     */
    public static InformationUnitIdentifier fromTopic(String topicName) {
        if (topicName == null || topicName.isBlank()) {
            throw new IllegalArgumentException("topicName must not be null or blank");
        }
        String trimmed = topicName.trim();
        if (!trimmed.startsWith(TOPIC_PREFIX)) {
            throw new IllegalArgumentException(
                    "Topic name '" + trimmed + "' must start with canonical prefix '" + TOPIC_PREFIX + "'");
        }
        String hex = trimmed.substring(TOPIC_PREFIX.length());
        if (hex.length() != HEX_LENGTH) {
            throw new IllegalArgumentException(
                    "Topic name hex segment '" + hex + "' must be exactly " + HEX_LENGTH + " hex characters, found " + hex.length());
        }
        UUID parsedUuid = compressedHexToUuid(hex);
        return new InformationUnitIdentifier(parsedUuid);
    }

    /**
     * Creates an {@code InformationUnitIdentifier} from a legacy Calliope {@link Topic} instance.
     *
     * @param topic the Calliope Topic, must not be null
     * @return the derived InformationUnitIdentifier
     * @throws IllegalArgumentException if the topic does not represent an Information Unit
     */
    public static InformationUnitIdentifier fromTopic(Topic topic) {
        Objects.requireNonNull(topic, "topic must not be null");
        String dataElement = topic.getDataElement();
        if ("harmonia".equalsIgnoreCase(topic.getDomain())
                && "mneme".equalsIgnoreCase(topic.getModel())
                && "iu".equalsIgnoreCase(topic.getModelVersion())
                && dataElement != null && dataElement.length() == HEX_LENGTH) {
            return new InformationUnitIdentifier(compressedHexToUuid(dataElement));
        }
        return fromTopic(topic.toTopicString());
    }

    /**
     * Generates a new {@code InformationUnitIdentifier} with a freshly allocated random UUIDv4.
     *
     * @return a new InformationUnitIdentifier
     */
    public static InformationUnitIdentifier random() {
        return new InformationUnitIdentifier(UUID.randomUUID());
    }

    /**
     * Returns the persistent {@link UUID} of the Information Unit.
     *
     * @return the UUID
     */
    @JsonProperty("uuid")
    public UUID uuid() {
        return uuid;
    }

    /**
     * Returns the canonical hierarchical Topic name (e.g. {@code "harmonia.mneme.iu.<hex>"}).
     *
     * @return the canonical topic name
     */
    @JsonProperty("topicName")
    public String topicName() {
        return topicName;
    }

    /**
     * Returns the 32-character lowercase hexadecimal representation of the UUID without hyphens.
     *
     * @return the 32-character hex string
     */
    @JsonIgnore
    public String hexString() {
        return uuidToCompressedHex(uuid);
    }

    /**
     * Reconciles this Information Unit identifier with a legacy Calliope {@link Topic} instance.
     *
     * @return a populated Calliope Topic representing this IU
     */
    public Topic toTopic() {
        Topic topic = new Topic();
        topic.setDomain("harmonia");
        topic.setModel("mneme");
        topic.setModelVersion("iu");
        topic.setDataElement(hexString());
        topic.setDataElementQualifier("");
        return topic;
    }

    // =========================================================================
    // Reversible Hex Compression Helpers
    // =========================================================================

    /**
     * Converts a 128-bit UUID to a 32-character lowercase hexadecimal string.
     *
     * @param uuid the UUID to encode
     * @return 32-character hex string
     */
    public static String uuidToCompressedHex(UUID uuid) {
        Objects.requireNonNull(uuid, "uuid must not be null");
        long msb = uuid.getMostSignificantBits();
        long lsb = uuid.getLeastSignificantBits();
        return String.format("%016x%016x", msb, lsb);
    }

    /**
     * Reconstitutes a 128-bit UUID from a 32-character hexadecimal string.
     *
     * @param hex 32-character hex string
     * @return parsed UUID
     * @throws IllegalArgumentException if hex is not exactly 32 hexadecimal characters
     */
    public static UUID compressedHexToUuid(String hex) {
        if (hex == null || hex.length() != HEX_LENGTH) {
            throw new IllegalArgumentException(
                    "Hex string must be exactly " + HEX_LENGTH + " characters, but was: '" + hex + "'");
        }
        try {
            long msb = Long.parseUnsignedLong(hex.substring(0, 16), 16);
            long lsb = Long.parseUnsignedLong(hex.substring(16, 32), 16);
            return new UUID(msb, lsb);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Hex string contains non-hexadecimal characters: '" + hex + "'", e);
        }
    }

    // =========================================================================
    // Standard Object Methods
    // =========================================================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InformationUnitIdentifier that = (InformationUnitIdentifier) o;
        return uuid.equals(that.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public String toString() {
        return topicName;
    }

    @Override
    public int compareTo(InformationUnitIdentifier o) {
        if (o == null) return 1;
        return this.uuid.compareTo(o.uuid);
    }
}
