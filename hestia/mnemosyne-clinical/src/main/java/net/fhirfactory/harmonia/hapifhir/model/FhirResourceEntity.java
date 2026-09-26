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

package net.fhirfactory.harmonia.hapifhir.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(
    name = "hie_fhir_resources",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_resource_type_fhir_id", columnNames = {"resource_type", "fhir_id"})
    },
    indexes = {
        @Index(name = "idx_resource_type_fhir_id", columnList = "resource_type, fhir_id"),
        @Index(name = "idx_resource_type_deleted", columnList = "resource_type, is_deleted")
    }
)
public class FhirResourceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "resource_type", nullable = false, length = 64)
    private String resourceType;

    @Column(name = "fhir_id", nullable = false, length = 128)
    private String fhirId;

    @Column(name = "version_id", nullable = false)
    private Long versionId = 1L;

    @Column(name = "resource_json", nullable = false, columnDefinition = "TEXT")
    private String resourceJson;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted = false;

    @Column(name = "last_updated", nullable = false)
    private Instant lastUpdated = Instant.now();

    public FhirResourceEntity() {
    }

    public FhirResourceEntity(String resourceType, String fhirId, Long versionId, String resourceJson, boolean deleted, Instant lastUpdated) {
        this.resourceType = resourceType;
        this.fhirId = fhirId;
        this.versionId = versionId;
        this.resourceJson = resourceJson;
        this.deleted = deleted;
        this.lastUpdated = lastUpdated;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getFhirId() {
        return fhirId;
    }

    public void setFhirId(String fhirId) {
        this.fhirId = fhirId;
    }

    public Long getVersionId() {
        return versionId;
    }

    public void setVersionId(Long versionId) {
        this.versionId = versionId;
    }

    public String getResourceJson() {
        return resourceJson;
    }

    public void setResourceJson(String resourceJson) {
        this.resourceJson = resourceJson;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Instant getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
