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

package net.fhirfactory.harmonia.hestia.mnemosyne.information;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.fhirfactory.harmonia.hestia.mnemosyne.nonfhir.InformationUnitPersistenceRecord;
import net.fhirfactory.harmonia.model.core.information.entity.InformationUnit;

import java.io.IOException;
import java.util.Objects;

/**
 * Reconstitutes an IU only after verifying its canonical JSON digest and its independent UUID/topic
 * identity columns. The returned IU receives Mnemosyne's persisted version and no active-state context.
 */
public final class InformationUnitConstructor {

    private final ObjectMapper objectMapper;

    /** Creates a constructor using the same stable serialization rules as the disassembler. */
    public InformationUnitConstructor(ObjectMapper objectMapper) {
        this.objectMapper = InformationUnitDisassembler.canonicalMapper(objectMapper);
    }

    /**
     * Validates stored bytes and reconstructs the committed IU revision.
     *
     * @param record persisted identity, version, JSON and digest
     * @return reconstructed governed IU
     * @throws IllegalStateException when stored bytes, digest or identity are inconsistent
     */
    public InformationUnit construct(InformationUnitPersistenceRecord record) {
        Objects.requireNonNull(record, "record must not be null");
        if (record.version() < 1) {
            throw new IllegalStateException("Persisted InformationUnit version must be positive");
        }
        if (!record.sha256().equals(InformationUnitDisassembler.sha256(record.canonicalJson()))) {
            throw new IllegalStateException("Persisted InformationUnit integrity digest does not match its content");
        }

        InformationUnit decoded;
        try {
            decoded = objectMapper.readValue(record.canonicalJson(), InformationUnit.class);
        } catch (IOException exception) {
            throw new IllegalStateException("Persisted InformationUnit representation is invalid", exception);
        }
        if (!record.identifier().equals(decoded.identifier().uuid())
                || !record.topicName().equals(decoded.identifier().topicName())) {
            throw new IllegalStateException("Persisted InformationUnit identity does not match its stored key");
        }
        return decoded.withManagementContext(null).withVersion(Long.toString(record.version()));
    }
}