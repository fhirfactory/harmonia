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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.fhirfactory.harmonia.hestia.mnemosyne.nonfhir.InformationUnitPersistenceRecord;
import net.fhirfactory.harmonia.model.core.information.entity.InformationUnit;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Objects;

/**
 * Converts a Calliope InformationUnit to the engine-neutral persistence form. The durable JSON
 * contains governed identity, metadata and representation, but excludes Mneme's optional
 * operational ManagementContext and caller-supplied version; Mnemosyne assigns the version.
 */
public final class InformationUnitDisassembler {

    private final ObjectMapper objectMapper;

    /** Creates a deterministic JSON mapper with Java time support. */
    public InformationUnitDisassembler(ObjectMapper objectMapper) {
        this.objectMapper = canonicalMapper(objectMapper);
    }

    /**
     * Disassembles the governed IU value and calculates an integrity digest over its canonical JSON.
     *
     * @param informationUnit IU proposal
     * @return version-zero persistence proposal
     * @throws IllegalArgumentException when the IU cannot be represented safely
     */
    public InformationUnitPersistenceRecord disassemble(InformationUnit informationUnit) {
        Objects.requireNonNull(informationUnit, "informationUnit must not be null");
        InformationUnit durableValue = informationUnit.withVersion(null).withManagementContext(null);
        try {
            String json = objectMapper.writeValueAsString(durableValue);
            return new InformationUnitPersistenceRecord(
                    durableValue.identifier().uuid(),
                    durableValue.identifier().topicName(),
                    0,
                    json,
                    sha256(json));
        } catch (JsonProcessingException exception) {
            throw new IllegalArgumentException("InformationUnit could not be serialized for persistence", exception);
        }
    }

    /** Creates the stable mapper used by both disassembly and reconstitution. */
    static ObjectMapper canonicalMapper(ObjectMapper source) {
        Objects.requireNonNull(source, "objectMapper must not be null");
        return source.copy()
                .registerModule(new JavaTimeModule())
                .enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
                .enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);
    }

    /** Computes the lowercase SHA-256 digest used to detect persisted representation corruption. */
    static String sha256(String value) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("The Java runtime does not provide SHA-256", exception);
        }
    }
}