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

package net.fhirfactory.harmonia.agora.core.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for durable Harmonia-to-Matrix resource mappings in Mnemosyne.
 */
@Repository
public interface AgoraMappingRepository extends JpaRepository<AgoraMappingEntity, UUID> {

    Optional<AgoraMappingEntity> findByHarmoniaResourceTypeAndHarmoniaResourceIdAndMatrixEntityType(
            String harmoniaResourceType,
            String harmoniaResourceId,
            String matrixEntityType
    );

    List<AgoraMappingEntity> findByHarmoniaResourceTypeAndHarmoniaResourceId(
            String harmoniaResourceType,
            String harmoniaResourceId
    );

    Optional<AgoraMappingEntity> findByMatrixEntityId(String matrixEntityId);

    List<AgoraMappingEntity> findByStatus(String status);

    boolean existsByHarmoniaResourceTypeAndHarmoniaResourceIdAndMatrixEntityType(
            String harmoniaResourceType,
            String harmoniaResourceId,
            String matrixEntityType
    );

    boolean existsByMatrixEntityId(String matrixEntityId);
}
