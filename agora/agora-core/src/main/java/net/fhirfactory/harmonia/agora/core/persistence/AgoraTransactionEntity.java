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

import jakarta.persistence.*;
import java.time.Instant;
import java.util.Objects;

/**
 * Mnemosyne relational entity tracking processed Application Service transactions for idempotency.
 */
@Entity
@Table(
        name = "agora_as_transactions",
        indexes = {
                @Index(name = "idx_agora_txn_received", columnList = "received_at"),
                @Index(name = "idx_agora_txn_status", columnList = "status")
        }
)
public class AgoraTransactionEntity {

    public static final String STATUS_RECEIVED = "RECEIVED";
    public static final String STATUS_PROCESSED = "PROCESSED";
    public static final String STATUS_FAILED = "FAILED";
    public static final String STATUS_IGNORED = "IGNORED";

    @Id
    @Column(name = "transaction_id", nullable = false, length = 128)
    private String transactionId;

    @Column(name = "received_at", nullable = false)
    private Instant receivedAt;

    @Column(name = "processed_at", nullable = false)
    private Instant processedAt;

    @Column(name = "event_count", nullable = false)
    private Integer eventCount = 0;

    @Column(name = "status", nullable = false, length = 32)
    private String status = "PROCESSED";

    public AgoraTransactionEntity() {
    }

    public AgoraTransactionEntity(String transactionId, Integer eventCount, String status) {
        this.transactionId = transactionId;
        this.receivedAt = Instant.now();
        this.processedAt = Instant.now();
        this.eventCount = (eventCount != null) ? eventCount : 0;
        this.status = (status != null && !status.isBlank()) ? status : "PROCESSED";
    }

    public AgoraTransactionEntity(String transactionId, Instant receivedAt, Instant processedAt,
                                  Integer eventCount, String status) {
        this.transactionId = transactionId;
        this.receivedAt = (receivedAt != null) ? receivedAt : Instant.now();
        this.processedAt = (processedAt != null) ? processedAt : Instant.now();
        this.eventCount = (eventCount != null) ? eventCount : 0;
        this.status = (status != null && !status.isBlank()) ? status : "PROCESSED";
    }

    @PrePersist
    public void onPrePersist() {
        Instant now = Instant.now();
        if (this.receivedAt == null) {
            this.receivedAt = now;
        }
        if (this.processedAt == null) {
            this.processedAt = now;
        }
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Instant getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(Instant receivedAt) {
        this.receivedAt = receivedAt;
    }

    public Instant getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(Instant processedAt) {
        this.processedAt = processedAt;
    }

    public Integer getEventCount() {
        return eventCount;
    }

    public void setEventCount(Integer eventCount) {
        this.eventCount = eventCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgoraTransactionEntity that = (AgoraTransactionEntity) o;
        return Objects.equals(transactionId, that.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId);
    }

    @Override
    public String toString() {
        return "AgoraTransactionEntity{" +
                "transactionId='" + transactionId + '\'' +
                ", receivedAt=" + receivedAt +
                ", processedAt=" + processedAt +
                ", eventCount=" + eventCount +
                ", status='" + status + '\'' +
                '}';
    }
}
