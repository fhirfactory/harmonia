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

package net.fhirfactory.harmonia.operations.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(
    name = "hie_operations_resources",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_ops_object_type_object_id", columnNames = {"object_type", "object_id"})
    },
    indexes = {
        @Index(name = "idx_ops_object_type_object_id", columnList = "object_type, object_id"),
        @Index(name = "idx_ops_object_type_deleted", columnList = "object_type, is_deleted")
    }
)
public class OperationResourceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "object_type", nullable = false, length = 64)
    private String objectType;

    @Column(name = "object_id", nullable = false, length = 128)
    private String objectId;

    @Column(name = "version_id", nullable = false)
    private Long versionId = 1L;

    @Lob
    @Column(name = "data_json", nullable = false, columnDefinition = "TEXT")
    private String dataJson;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted = false;

    @Column(name = "created_date", nullable = false)
    private Instant createdDate = Instant.now();

    @Column(name = "last_updated", nullable = false)
    private Instant lastUpdated = Instant.now();

    public OperationResourceEntity() {
    }

    public OperationResourceEntity(String objectType, String objectId, Long versionId, String dataJson, boolean deleted, Instant createdDate, Instant lastUpdated) {
        this.objectType = objectType;
        this.objectId = objectId;
        this.versionId = versionId;
        this.dataJson = dataJson;
        this.deleted = deleted;
        this.createdDate = createdDate != null ? createdDate : Instant.now();
        this.lastUpdated = lastUpdated != null ? lastUpdated : Instant.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Long getVersionId() {
        return versionId;
    }

    public void setVersionId(Long versionId) {
        this.versionId = versionId;
    }

    public String getDataJson() {
        return dataJson;
    }

    public void setDataJson(String dataJson) {
        this.dataJson = dataJson;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
