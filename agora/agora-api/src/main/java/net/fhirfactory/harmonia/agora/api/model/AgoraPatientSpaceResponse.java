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

package net.fhirfactory.harmonia.agora.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Encapsulates the provisioned hierarchy of a Patient Space and its four standard child rooms.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AgoraPatientSpaceResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("spaceId")
    private final String spaceId;

    @JsonProperty("patientId")
    private final String patientId;

    @JsonProperty("childRoomIds")
    private final Map<AgoraRoomType, String> childRoomIds;

    @JsonCreator
    public AgoraPatientSpaceResponse(
            @JsonProperty("spaceId") String spaceId,
            @JsonProperty("patientId") String patientId,
            @JsonProperty("childRoomIds") Map<AgoraRoomType, String> childRoomIds
    ) {
        this.spaceId = Objects.requireNonNull(spaceId, "spaceId must not be null");
        this.patientId = Objects.requireNonNull(patientId, "patientId must not be null");
        this.childRoomIds = (childRoomIds != null)
                ? Collections.unmodifiableMap(new LinkedHashMap<>(childRoomIds))
                : Collections.emptyMap();
    }

    public String getSpaceId() {
        return spaceId;
    }

    public String getPatientId() {
        return patientId;
    }

    public Map<AgoraRoomType, String> getChildRoomIds() {
        return childRoomIds;
    }

    public String getStatisticsRoomId() {
        return childRoomIds.get(AgoraRoomType.STATISTICS);
    }

    public String getTasksRoomId() {
        return childRoomIds.get(AgoraRoomType.TASKS);
    }

    public String getDiscussionRoomId() {
        return childRoomIds.get(AgoraRoomType.DISCUSSION);
    }

    public String getDiagnosticsRoomId() {
        return childRoomIds.get(AgoraRoomType.DIAGNOSTICS);
    }

    public String getChildRoomId(AgoraRoomType roomType) {
        return childRoomIds.get(roomType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgoraPatientSpaceResponse that = (AgoraPatientSpaceResponse) o;
        return Objects.equals(spaceId, that.spaceId) && Objects.equals(patientId, that.patientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(spaceId, patientId);
    }

    @Override
    public String toString() {
        return "AgoraPatientSpaceResponse{" +
                "spaceId='" + spaceId + '\'' +
                ", patientId='" + patientId + '\'' +
                ", childRoomIds=" + childRoomIds +
                '}';
    }
}
