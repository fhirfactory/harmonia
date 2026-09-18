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
import java.util.UUID;

/**
 * Mnemosyne relational entity mapping authoritative Harmonia resources to live Matrix entities.
 */
@Entity
@Table(
        name = "agora_resource_mappings",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_agora_res_mapping",
                        columnNames = {"harmonia_resource_type", "harmonia_resource_id", "matrix_entity_type"}
                )
        },
        indexes = {
                @Index(name = "idx_agora_res_mapping_lookup", columnList = "harmonia_resource_type, harmonia_resource_id"),
                @Index(name = "idx_agora_matrix_entity_id", columnList = "matrix_entity_id"),
                @Index(name = "idx_agora_mapping_status", columnList = "status")
        }
)
public class AgoraMappingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "harmonia_resource_type", nullable = false, length = 64)
    private String harmoniaResourceType;

    @Column(name = "harmonia_resource_id", nullable = false, length = 128)
    private String harmoniaResourceId;

    @Column(name = "matrix_entity_type", nullable = false, length = 64)
    private String matrixEntityType;

    @Column(name = "matrix_entity_id", nullable = false, length = 256)
    private String matrixEntityId;

    @Column(name = "status", nullable = false, length = 32)
    private String status = "ACTIVE";

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public AgoraMappingEntity() {
    }

    public AgoraMappingEntity(String harmoniaResourceType, String harmoniaResourceId,
                              String matrixEntityType, String matrixEntityId, String status) {
        this.harmoniaResourceType = harmoniaResourceType;
        this.harmoniaResourceId = harmoniaResourceId;
        this.matrixEntityType = matrixEntityType;
        this.matrixEntityId = matrixEntityId;
        this.status = (status != null && !status.isBlank()) ? status : "ACTIVE";
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public AgoraMappingEntity(UUID id, String harmoniaResourceType, String harmoniaResourceId,
                              String matrixEntityType, String matrixEntityId, String status,
                              Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.harmoniaResourceType = harmoniaResourceType;
        this.harmoniaResourceId = harmoniaResourceId;
        this.matrixEntityType = matrixEntityType;
        this.matrixEntityId = matrixEntityId;
        this.status = (status != null && !status.isBlank()) ? status : "ACTIVE";
        this.createdAt = (createdAt != null) ? createdAt : Instant.now();
        this.updatedAt = (updatedAt != null) ? updatedAt : Instant.now();
    }

    @PrePersist
    public void onPrePersist() {
        Instant now = Instant.now();
        if (this.createdAt == null) {
            this.createdAt = now;
        }
        if (this.updatedAt == null) {
            this.updatedAt = now;
        }
    }

    @PreUpdate
    public void onPreUpdate() {
        this.updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getHarmoniaResourceType() {
        return harmoniaResourceType;
    }

    public void setHarmoniaResourceType(String harmoniaResourceType) {
        this.harmoniaResourceType = harmoniaResourceType;
    }

    public String getHarmoniaResourceId() {
        return harmoniaResourceId;
    }

    public void setHarmoniaResourceId(String harmoniaResourceId) {
        this.harmoniaResourceId = harmoniaResourceId;
    }

    public String getMatrixEntityType() {
        return matrixEntityType;
    }

    public void setMatrixEntityType(String matrixEntityType) {
        this.matrixEntityType = matrixEntityType;
    }

    public String getMatrixEntityId() {
        return matrixEntityId;
    }

    public void setMatrixEntityId(String matrixEntityId) {
        this.matrixEntityId = matrixEntityId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgoraMappingEntity that = (AgoraMappingEntity) o;
        return Objects.equals(harmoniaResourceType, that.harmoniaResourceType) &&
                Objects.equals(harmoniaResourceId, that.harmoniaResourceId) &&
                Objects.equals(matrixEntityType, that.matrixEntityType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(harmoniaResourceType, harmoniaResourceId, matrixEntityType);
    }

    @Override
    public String toString() {
        return "AgoraMappingEntity{" +
                "id=" + id +
                ", harmoniaResourceType='" + harmoniaResourceType + '\'' +
                ", harmoniaResourceId='" + harmoniaResourceId + '\'' +
                ", matrixEntityType='" + matrixEntityType + '\'' +
                ", matrixEntityId='" + matrixEntityId + '\'' +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
