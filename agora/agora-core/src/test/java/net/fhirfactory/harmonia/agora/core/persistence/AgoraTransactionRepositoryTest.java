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
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = AgoraCoreTestApplication.class)
@Transactional
@DisplayName("AgoraTransactionRepository Idempotency Tests")
@Timeout(10)
class AgoraTransactionRepositoryTest {

    @Autowired
    private AgoraTransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        transactionRepository.deleteAll();
    }

    @Test
    @DisplayName("Should persist and check existence of processed transaction for idempotency")
    void testTransactionIdempotencyCheck() {
        String txnId = "txn-unique-12345";
        assertThat(transactionRepository.existsByTransactionId(txnId)).isFalse();

        AgoraTransactionEntity entity = new AgoraTransactionEntity(txnId, 3, "PROCESSED");
        AgoraTransactionEntity saved = transactionRepository.saveAndFlush(entity);

        assertThat(saved.getTransactionId()).isEqualTo(txnId);
        assertThat(saved.getReceivedAt()).isNotNull();
        assertThat(saved.getProcessedAt()).isNotNull();
        assertThat(saved.getEventCount()).isEqualTo(3);
        assertThat(saved.getStatus()).isEqualTo("PROCESSED");

        assertThat(transactionRepository.existsByTransactionId(txnId)).isTrue();
        Optional<AgoraTransactionEntity> found = transactionRepository.findByTransactionId(txnId);
        assertThat(found).isPresent();
        assertThat(found.get().getEventCount()).isEqualTo(3);
    }

    @Test
    @DisplayName("Should return false for unknown transaction id")
    void testUnknownTransaction() {
        assertThat(transactionRepository.existsByTransactionId("non-existent-txn")).isFalse();
        assertThat(transactionRepository.findByTransactionId("non-existent-txn")).isEmpty();
    }
}
