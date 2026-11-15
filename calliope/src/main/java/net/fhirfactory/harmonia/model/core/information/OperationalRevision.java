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
import com.fasterxml.jackson.annotation.JsonValue;
import net.fhirfactory.harmonia.model.governedwrite.AuthoritativeVersion;

import java.io.Serializable;
import java.util.Objects;

/**
 * Encapsulates the Mneme active cache operational revision used for optimistic concurrency,
 * conflict detection, and stale-update detection.
 * <p>
 * <b>Architectural Context &amp; Authority:</b>
 * In accordance with the accepted 1.1.25 Architecture Continuum ({@code model.core.information.InformationUnit})
 * and {@code DRP-OperationalMetadata-and-OperationalMetadataMask.Final.001.md} (§§0, 2):
 * <ul>
 *   <li><b>Distinct Versioning Dimension:</b> {@code OperationalRevision} represents active working-cache
 *       concurrency state maintained principally by Mneme.</li>
 *   <li>It is <b>strictly distinct</b> from {@link AuthoritativeVersion}, which is assigned and committed
 *       exclusively by the durable database boundary (Mnemosyne).</li>
 *   <li>It is also distinct from the schema/model definition version ({@link ContentMetadata#contentStructureVersion()})
 *       and the payload-native version (e.g. FHIR {@code meta.versionId}).</li>
 * </ul>
 * <p>
 * <b>Defensive Programming:</b>
 * <ul>
 *   <li>Immutable record design.</li>
 *   <li>Non-null, non-blank validation on creation.</li>
 * </ul>
 */
public record OperationalRevision(
        @JsonValue String value
) implements Serializable, Comparable<OperationalRevision> {

    public OperationalRevision {
        Objects.requireNonNull(value, "value must not be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("value must not be blank");
        }
        value = value.trim();
    }

    @JsonCreator
    public static OperationalRevision of(String value) {
        return new OperationalRevision(value);
    }

    public static OperationalRevision of(long revisionNumber) {
        return new OperationalRevision(String.valueOf(revisionNumber));
    }

    public long longValue() {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    @Override
    public int compareTo(OperationalRevision other) {
        if (other == null) {
            return 1;
        }
        try {
            long thisNum = Long.parseLong(this.value);
            long otherNum = Long.parseLong(other.value);
            return Long.compare(thisNum, otherNum);
        } catch (NumberFormatException e) {
            return this.value.compareTo(other.value);
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
