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

import net.fhirfactory.harmonia.agora.core.AgoraCoreTestApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = AgoraCoreTestApplication.class)
@Transactional
@DisplayName("AgoraMappingRepository Persistence Tests")
@Timeout(10)
class AgoraMappingRepositoryTest {

    @Autowired
    private AgoraMappingRepository mappingRepository;

    @BeforeEach
    void setUp() {
        mappingRepository.deleteAll();
    }

    @Test
    @DisplayName("Should persist and retrieve mapping by composite key and matrix entity id")
    void testSaveAndRetrieve() {
        AgoraMappingEntity entity = new AgoraMappingEntity(
                "PATIENT", "pat-12345", "SPACE", "!space123:synapse", "ACTIVE"
        );

        AgoraMappingEntity saved = mappingRepository.saveAndFlush(entity);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();

        Optional<AgoraMappingEntity> foundByComposite = mappingRepository
                .findByHarmoniaResourceTypeAndHarmoniaResourceIdAndMatrixEntityType("PATIENT", "pat-12345", "SPACE");
        assertThat(foundByComposite).isPresent();
        assertThat(foundByComposite.get().getMatrixEntityId()).isEqualTo("!space123:synapse");
        assertThat(foundByComposite.get().getStatus()).isEqualTo("ACTIVE");

        Optional<AgoraMappingEntity> foundByMatrix = mappingRepository.findByMatrixEntityId("!space123:synapse");
        assertThat(foundByMatrix).isPresent();
        assertThat(foundByMatrix.get().getHarmoniaResourceId()).isEqualTo("pat-12345");

        boolean exists = mappingRepository.existsByMatrixEntityId("!space123:synapse");
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should enforce composite uniqueness on harmoniaResourceType + harmoniaResourceId + matrixEntityType")
    void testCompositeUniqueConstraint() {
        AgoraMappingEntity first = new AgoraMappingEntity(
                "PATIENT", "pat-unique", "ROOM", "!room1:synapse", "ACTIVE"
        );
        mappingRepository.saveAndFlush(first);

        AgoraMappingEntity duplicate = new AgoraMappingEntity(
                "PATIENT", "pat-unique", "ROOM", "!room2:synapse", "ACTIVE"
        );

        assertThatThrownBy(() -> mappingRepository.saveAndFlush(duplicate))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Should update status to ARCHIVED and update timestamp")
    void testUpdateStatus() {
        AgoraMappingEntity entity = new AgoraMappingEntity(
                "TASK", "task-789", "ROOM", "!taskroom:synapse", "ACTIVE"
        );
        AgoraMappingEntity saved = mappingRepository.saveAndFlush(entity);

        saved.setStatus("ARCHIVED");
        AgoraMappingEntity updated = mappingRepository.saveAndFlush(saved);

        assertThat(updated.getStatus()).isEqualTo("ARCHIVED");
        List<AgoraMappingEntity> archived = mappingRepository.findByStatus("ARCHIVED");
        assertThat(archived).hasSize(1);
        assertThat(archived.get(0).getHarmoniaResourceId()).isEqualTo("task-789");
    }
}
