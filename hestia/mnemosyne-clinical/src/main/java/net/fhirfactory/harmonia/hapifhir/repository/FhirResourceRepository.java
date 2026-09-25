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

package net.fhirfactory.harmonia.hapifhir.repository;

import net.fhirfactory.harmonia.hapifhir.model.FhirResourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface FhirResourceRepository extends JpaRepository<FhirResourceEntity, Long> {

    Optional<FhirResourceEntity> findByResourceTypeAndFhirId(String resourceType, String fhirId);

    Optional<FhirResourceEntity> findByResourceTypeAndFhirIdAndDeletedFalse(String resourceType, String fhirId);

    List<FhirResourceEntity> findByResourceTypeAndDeletedFalse(String resourceType);

    List<FhirResourceEntity> findByResourceType(String resourceType);

    boolean existsByResourceTypeAndFhirIdAndDeletedFalse(String resourceType, String fhirId);

    long countByResourceTypeAndDeletedFalse(String resourceType);

    @Modifying
    @Query("UPDATE FhirResourceEntity e SET e.resourceJson = :resourceJson, e.versionId = :newVersion, e.lastUpdated = :lastUpdated WHERE e.resourceType = :resourceType AND e.fhirId = :fhirId AND e.versionId = :expectedVersion AND e.deleted = false")
    int updateIfVersionMatches(
            @Param("resourceType") String resourceType,
            @Param("fhirId") String fhirId,
            @Param("expectedVersion") Long expectedVersion,
            @Param("newVersion") Long newVersion,
            @Param("resourceJson") String resourceJson,
            @Param("lastUpdated") Instant lastUpdated);
}
