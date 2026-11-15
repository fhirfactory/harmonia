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

import net.fhirfactory.harmonia.model.topic.Topic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link InformationUnitIdentifier}.
 * Verifies UUID-to-Topic deterministic mapping, reversibility, invariant enforcement, and unhappy path handling.
 */
class InformationUnitIdentifierTest {

    @Test
    @DisplayName("Should create identifier from UUID and derive canonical topic deterministically")
    void testFromUuid() {
        UUID uuid = UUID.fromString("01234567-89ab-cdef-0123-456789abcdef");
        InformationUnitIdentifier id = InformationUnitIdentifier.fromUuid(uuid);

        assertThat(id.uuid()).isEqualTo(uuid);
        assertThat(id.hexString()).isEqualTo("0123456789abcdef0123456789abcdef");
        assertThat(id.topicName()).isEqualTo("harmonia.mneme.iu.0123456789abcdef0123456789abcdef");
        assertThat(id.toString()).isEqualTo("harmonia.mneme.iu.0123456789abcdef0123456789abcdef");
    }

    @Test
    @DisplayName("Should parse identifier from valid canonical topic string reversibly")
    void testFromTopicString() {
        String topicStr = "harmonia.mneme.iu.0123456789abcdef0123456789abcdef";
        InformationUnitIdentifier id = InformationUnitIdentifier.fromTopic(topicStr);

        assertThat(id.topicName()).isEqualTo(topicStr);
        assertThat(id.hexString()).isEqualTo("0123456789abcdef0123456789abcdef");
        assertThat(id.uuid()).isEqualTo(UUID.fromString("01234567-89ab-cdef-0123-456789abcdef"));
    }

    @Test
    @DisplayName("Should handle boundary UUIDs (nil UUID, max UUID, leading zeroes)")
    void testBoundaryUuids() {
        // Nil UUID (all 0s)
        UUID nilUuid = new UUID(0L, 0L);
        InformationUnitIdentifier nilId = InformationUnitIdentifier.fromUuid(nilUuid);
        assertThat(nilId.hexString()).isEqualTo("00000000000000000000000000000000");
        assertThat(nilId.topicName()).isEqualTo("harmonia.mneme.iu.00000000000000000000000000000000");
        assertThat(InformationUnitIdentifier.fromTopic(nilId.topicName()).uuid()).isEqualTo(nilUuid);

        // Max UUID (all Fs)
        UUID maxUuid = new UUID(-1L, -1L);
        InformationUnitIdentifier maxId = InformationUnitIdentifier.fromUuid(maxUuid);
        assertThat(maxId.hexString()).isEqualTo("ffffffffffffffffffffffffffffffff");
        assertThat(maxId.topicName()).isEqualTo("harmonia.mneme.iu.ffffffffffffffffffffffffffffffff");
        assertThat(InformationUnitIdentifier.fromTopic(maxId.topicName()).uuid()).isEqualTo(maxUuid);

        // Leading zero in MSB/LSB
        UUID leadingZeroUuid = new UUID(0x0000123456789abcL, 0x0000def012345678L);
        InformationUnitIdentifier lzId = InformationUnitIdentifier.fromUuid(leadingZeroUuid);
        assertThat(lzId.hexString()).isEqualTo("0000123456789abc0000def012345678");
        assertThat(InformationUnitIdentifier.fromTopic(lzId.topicName()).uuid()).isEqualTo(leadingZeroUuid);
    }

    @Test
    @DisplayName("Should parse from UUID string representation")
    void testFromUuidString() {
        String uuidStr = "e0b12b50-6a84-48b2-b43a-73d8a609d935";
        InformationUnitIdentifier id = InformationUnitIdentifier.fromUuidString(uuidStr);

        assertThat(id.uuid()).isEqualTo(UUID.fromString(uuidStr));
        assertThat(id.hexString()).isEqualTo("e0b12b506a8448b2b43a73d8a609d935");
    }

    @Test
    @DisplayName("Should interoperate with legacy Calliope Topic representation")
    void testCalliopeTopicInteroperability() {
        UUID uuid = UUID.randomUUID();
        InformationUnitIdentifier id = InformationUnitIdentifier.fromUuid(uuid);

        Topic topic = id.toTopic();
        assertThat(topic.getDomain()).isEqualTo("harmonia");
        assertThat(topic.getModel()).isEqualTo("mneme");
        assertThat(topic.getModelVersion()).isEqualTo("iu");
        assertThat(topic.getDataElement()).isEqualTo(id.hexString());

        // Reconstruct from Topic
        InformationUnitIdentifier reconstructed = InformationUnitIdentifier.fromTopic(topic);
        assertThat(reconstructed).isEqualTo(id);
        assertThat(reconstructed.uuid()).isEqualTo(uuid);

        // Via Topic.forInformationUnit helper
        Topic topicViaHelper = Topic.forInformationUnit(uuid);
        assertThat(InformationUnitIdentifier.fromTopic(topicViaHelper)).isEqualTo(id);
    }

    @Test
    @DisplayName("Should support equality, hashCode, and sorting")
    void testEqualityAndComparison() {
        UUID u1 = UUID.fromString("00000000-0000-0000-0000-000000000001");
        UUID u2 = UUID.fromString("00000000-0000-0000-0000-000000000002");

        InformationUnitIdentifier id1a = InformationUnitIdentifier.fromUuid(u1);
        InformationUnitIdentifier id1b = InformationUnitIdentifier.fromUuid(u1);
        InformationUnitIdentifier id2 = InformationUnitIdentifier.fromUuid(u2);

        assertThat(id1a).isEqualTo(id1b);
        assertThat(id1a.hashCode()).isEqualTo(id1b.hashCode());
        assertThat(id1a).isNotEqualTo(id2);
        assertThat(id1a.compareTo(id2)).isLessThan(0);
        assertThat(id2.compareTo(id1a)).isGreaterThan(0);
        assertThat(id1a.compareTo(id1b)).isEqualTo(0);
    }

    // =========================================================================
    // Unhappy Paths & Defensive Validation
    // =========================================================================

    @Test
    @DisplayName("Should reject null UUID")
    void testNullUuid() {
        assertThatThrownBy(() -> InformationUnitIdentifier.fromUuid(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("uuid must not be null");
    }

    @Test
    @DisplayName("Should reject null, blank, or malformed UUID strings")
    void testInvalidUuidStrings() {
        assertThatThrownBy(() -> InformationUnitIdentifier.fromUuidString(null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> InformationUnitIdentifier.fromUuidString("   "))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> InformationUnitIdentifier.fromUuidString("not-a-uuid"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Should reject invalid canonical topic strings")
    void testInvalidTopicStrings() {
        // Null or blank
        assertThatThrownBy(() -> InformationUnitIdentifier.fromTopic((String) null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> InformationUnitIdentifier.fromTopic(""))
                .isInstanceOf(IllegalArgumentException.class);

        // Wrong prefix
        assertThatThrownBy(() -> InformationUnitIdentifier.fromTopic("other.domain.iu.0123456789abcdef0123456789abcdef"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("must start with canonical prefix");

        // Wrong hex length (too short)
        assertThatThrownBy(() -> InformationUnitIdentifier.fromTopic("harmonia.mneme.iu.0123456789abcdef"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("must be exactly 32 hex characters");

        // Wrong hex length (too long)
        assertThatThrownBy(() -> InformationUnitIdentifier.fromTopic("harmonia.mneme.iu.0123456789abcdef0123456789abcdef0000"))
                .isInstanceOf(IllegalArgumentException.class);

        // Non-hex characters
        assertThatThrownBy(() -> InformationUnitIdentifier.fromTopic("harmonia.mneme.iu.0123456789abcdef0123456789abcdeg"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("non-hexadecimal");
    }

    @Test
    @DisplayName("Should reject mismatching topicName in JSON factory")
    void testJsonCreatorMismatch() {
        UUID uuid = UUID.fromString("01234567-89ab-cdef-0123-456789abcdef");
        String mismatchTopic = "harmonia.mneme.iu.ffffffffffffffffffffffffffffffff";

        assertThatThrownBy(() -> InformationUnitIdentifier.of(uuid, mismatchTopic))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("does not match derived canonical topic");
    }
}
