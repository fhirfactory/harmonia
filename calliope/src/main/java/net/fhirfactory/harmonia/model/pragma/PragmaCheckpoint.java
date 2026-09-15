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

package net.fhirfactory.harmonia.model.pragma;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Encapsulates an execution audit checkpoint recorded during the lifecycle of a {@link Pragma}.
 * <p>
 * Checkpoints capture point-in-time workflow progression, step indexing, stage names,
 * active Ergon / Praxis identifiers, and status transitions for resilience, auditability, and cache persistence.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PragmaCheckpoint implements Serializable, Comparable<PragmaCheckpoint> {

    private static final long serialVersionUID = 1L;

    @JsonProperty("checkpointId")
    @JsonAlias({"id", "checkpoint_id"})
    private String checkpointId;

    @JsonProperty("pragmaId")
    @JsonAlias({"taskId", "pragma_id", "task_id"})
    private String pragmaId;

    @JsonProperty("ergonId")
    @JsonAlias({"activityId", "ergon_id", "activity_id"})
    private String ergonId;

    @JsonProperty("praxisId")
    @JsonAlias({"sequenceId", "praxis_id", "workflowId"})
    private String praxisId;

    @JsonProperty("stageName")
    @JsonAlias({"stage", "stage_name", "checkpointType"})
    private String stageName;

    @JsonProperty("timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private Date timestamp;

    @JsonProperty("status")
    private PragmaStatus status;

    @JsonProperty("statusMessage")
    @JsonAlias({"message", "status_message", "detail"})
    private String statusMessage;

    @JsonProperty("stepIndex")
    @JsonAlias({"step", "step_index", "order"})
    private Integer stepIndex;

    @JsonProperty("metadata")
    private Map<String, String> metadata = new LinkedHashMap<>();

    /**
     * Default constructor.
     */
    public PragmaCheckpoint() {
        this.checkpointId = UUID.randomUUID().toString();
        this.timestamp = new Date();
        this.status = PragmaStatus.IN_PROGRESS;
    }

    /**
     * Parameterized constructor.
     *
     * @param pragmaId  identifier of the parent Pragma
     * @param ergonId   identifier of the active Ergon activity
     * @param stageName name or category of the execution stage
     * @param status    status at this checkpoint
     * @param stepIndex 0-based or 1-based sequential step index
     */
    public PragmaCheckpoint(String pragmaId, String ergonId, String stageName, PragmaStatus status, Integer stepIndex) {
        this();
        this.pragmaId = pragmaId;
        this.ergonId = ergonId;
        this.stageName = stageName;
        this.status = status != null ? status : PragmaStatus.IN_PROGRESS;
        this.stepIndex = stepIndex;
    }

    /**
     * Full constructor.
     *
     * @param checkpointId  unique checkpoint identifier
     * @param pragmaId      parent Pragma ID
     * @param ergonId       Ergon activity ID
     * @param praxisId      Praxis workflow sequence ID
     * @param stageName     stage descriptor
     * @param timestamp     recording timestamp
     * @param status        PragmaStatus
     * @param statusMessage optional descriptive message or error details
     * @param stepIndex     execution step index
     * @param metadata      arbitrary metadata attributes
     */
    public PragmaCheckpoint(String checkpointId, String pragmaId, String ergonId, String praxisId,
                            String stageName, Date timestamp, PragmaStatus status, String statusMessage,
                            Integer stepIndex, Map<String, String> metadata) {
        this.checkpointId = checkpointId != null ? checkpointId : UUID.randomUUID().toString();
        this.pragmaId = pragmaId;
        this.ergonId = ergonId;
        this.praxisId = praxisId;
        this.stageName = stageName;
        this.timestamp = timestamp != null ? new Date(timestamp.getTime()) : new Date();
        this.status = status != null ? status : PragmaStatus.IN_PROGRESS;
        this.statusMessage = statusMessage;
        this.stepIndex = stepIndex;
        if (metadata != null) {
            this.metadata.putAll(metadata);
        }
    }

    /**
     * Copy constructor.
     *
     * @param other checkpoint to copy
     */
    public PragmaCheckpoint(PragmaCheckpoint other) {
        if (other != null) {
            this.checkpointId = other.checkpointId;
            this.pragmaId = other.pragmaId;
            this.ergonId = other.ergonId;
            this.praxisId = other.praxisId;
            this.stageName = other.stageName;
            this.timestamp = other.timestamp != null ? new Date(other.timestamp.getTime()) : new Date();
            this.status = other.status;
            this.statusMessage = other.statusMessage;
            this.stepIndex = other.stepIndex;
            if (other.metadata != null) {
                this.metadata.putAll(other.metadata);
            }
        } else {
            this.checkpointId = UUID.randomUUID().toString();
            this.timestamp = new Date();
            this.status = PragmaStatus.IN_PROGRESS;
        }
    }

    // =========================================================================
    // Static Builder & Factory Methods
    // =========================================================================

    public static Builder builder() {
        return new Builder();
    }

    public static PragmaCheckpoint of(String pragmaId, String stageName, PragmaStatus status) {
        return new PragmaCheckpoint(pragmaId, null, stageName, status, null);
    }

    public static PragmaCheckpoint of(String pragmaId, String ergonId, String stageName, PragmaStatus status, Integer stepIndex) {
        return new PragmaCheckpoint(pragmaId, ergonId, stageName, status, stepIndex);
    }

    // =========================================================================
    // Getters and Setters
    // =========================================================================

    public String getCheckpointId() {
        return checkpointId;
    }

    public void setCheckpointId(String checkpointId) {
        this.checkpointId = checkpointId;
    }

    public String getPragmaId() {
        return pragmaId;
    }

    public void setPragmaId(String pragmaId) {
        this.pragmaId = pragmaId;
    }

    public String getErgonId() {
        return ergonId;
    }

    public void setErgonId(String ergonId) {
        this.ergonId = ergonId;
    }

    public String getPraxisId() {
        return praxisId;
    }

    public void setPraxisId(String praxisId) {
        this.praxisId = praxisId;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public Date getTimestamp() {
        return timestamp != null ? new Date(timestamp.getTime()) : null;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp != null ? new Date(timestamp.getTime()) : null;
    }

    public PragmaStatus getStatus() {
        return status;
    }

    public void setStatus(PragmaStatus status) {
        this.status = status;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public Integer getStepIndex() {
        return stepIndex;
    }

    public void setStepIndex(Integer stepIndex) {
        this.stepIndex = stepIndex;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata != null ? new LinkedHashMap<>(metadata) : new LinkedHashMap<>();
    }

    public PragmaCheckpoint addMetadata(String key, String value) {
        if (key != null) {
            this.metadata.put(key, value);
        }
        return this;
    }

    @Override
    public int compareTo(PragmaCheckpoint o) {
        if (o == null) {
            return 1;
        }
        if (this.timestamp != null && o.timestamp != null) {
            int timeCompare = this.timestamp.compareTo(o.timestamp);
            if (timeCompare != 0) {
                return timeCompare;
            }
        }
        if (this.stepIndex != null && o.stepIndex != null) {
            return this.stepIndex.compareTo(o.stepIndex);
        }
        if (this.checkpointId != null && o.checkpointId != null) {
            return this.checkpointId.compareTo(o.checkpointId);
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PragmaCheckpoint that = (PragmaCheckpoint) o;
        return Objects.equals(checkpointId, that.checkpointId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(checkpointId);
    }

    @Override
    public String toString() {
        return "PragmaCheckpoint{" +
                "checkpointId='" + checkpointId + '\'' +
                ", pragmaId='" + pragmaId + '\'' +
                ", ergonId='" + ergonId + '\'' +
                ", praxisId='" + praxisId + '\'' +
                ", stageName='" + stageName + '\'' +
                ", timestamp=" + timestamp +
                ", status=" + status +
                ", stepIndex=" + stepIndex +
                ", statusMessage='" + statusMessage + '\'' +
                '}';
    }

    // =========================================================================
    // Builder
    // =========================================================================

    public static class Builder {
        private String checkpointId;
        private String pragmaId;
        private String ergonId;
        private String praxisId;
        private String stageName;
        private Date timestamp;
        private PragmaStatus status = PragmaStatus.IN_PROGRESS;
        private String statusMessage;
        private Integer stepIndex;
        private final Map<String, String> metadata = new LinkedHashMap<>();

        public Builder checkpointId(String checkpointId) {
            this.checkpointId = checkpointId;
            return this;
        }

        public Builder pragmaId(String pragmaId) {
            this.pragmaId = pragmaId;
            return this;
        }

        public Builder ergonId(String ergonId) {
            this.ergonId = ergonId;
            return this;
        }

        public Builder praxisId(String praxisId) {
            this.praxisId = praxisId;
            return this;
        }

        public Builder stageName(String stageName) {
            this.stageName = stageName;
            return this;
        }

        public Builder timestamp(Date timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder status(PragmaStatus status) {
            this.status = status;
            return this;
        }

        public Builder statusMessage(String statusMessage) {
            this.statusMessage = statusMessage;
            return this;
        }

        public Builder stepIndex(Integer stepIndex) {
            this.stepIndex = stepIndex;
            return this;
        }

        public Builder metadata(Map<String, String> metadata) {
            if (metadata != null) {
                this.metadata.putAll(metadata);
            }
            return this;
        }

        public Builder addMetadata(String key, String value) {
            if (key != null) {
                this.metadata.put(key, value);
            }
            return this;
        }

        public PragmaCheckpoint build() {
            return new PragmaCheckpoint(checkpointId, pragmaId, ergonId, praxisId, stageName,
                    timestamp, status, statusMessage, stepIndex, metadata);
        }
    }
}
