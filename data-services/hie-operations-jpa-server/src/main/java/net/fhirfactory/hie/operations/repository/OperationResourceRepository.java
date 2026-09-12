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

package net.fhirfactory.hie.operations.repository;

import net.fhirfactory.hie.operations.model.OperationResourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OperationResourceRepository extends JpaRepository<OperationResourceEntity, Long> {

    Optional<OperationResourceEntity> findByObjectTypeAndObjectIdAndDeletedFalse(String objectType, String objectId);

    Optional<OperationResourceEntity> findByObjectTypeAndObjectId(String objectType, String objectId);

    List<OperationResourceEntity> findByObjectTypeAndDeletedFalse(String objectType);

    List<OperationResourceEntity> findByDeletedFalse();

    boolean existsByObjectTypeAndObjectIdAndDeletedFalse(String objectType, String objectId);

    long countByObjectTypeAndDeletedFalse(String objectType);

    long countByDeletedFalse();
}
