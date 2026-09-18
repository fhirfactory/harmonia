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

package net.fhirfactory.harmonia.befe.model.operations;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Execution checkpoint for an Ergon activity unit within a Pragma workflow execution.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErgonCheckpoint implements Serializable {

    private String ergonId;
    private String ergonName;
    private String status;               // PENDING, RUNNING, COMPLETED, FAILED, RETRYING
    private long startedAt;
    private long completedAt;
    private long durationMs;
    private String errorMessage;
    private Map<String, Object> details = new HashMap<>();

    public ErgonCheckpoint() {
    }

    public ErgonCheckpoint(String ergonId, String ergonName, String status, long startedAt,
                           long completedAt, long durationMs, String errorMessage) {
        this.ergonId = ergonId;
        this.ergonName = ergonName;
        this.status = status;
        this.startedAt = startedAt;
        this.completedAt = completedAt;
        this.durationMs = durationMs;
        this.errorMessage = errorMessage;
    }

    public String getErgonId() {
        return ergonId;
    }

    public void setErgonId(String ergonId) {
        this.ergonId = ergonId;
    }

    public String getErgonName() {
        return ergonName;
    }

    public void setErgonName(String ergonName) {
        this.ergonName = ergonName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(long startedAt) {
        this.startedAt = startedAt;
    }

    public long getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(long completedAt) {
        this.completedAt = completedAt;
    }

    public long getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(long durationMs) {
        this.durationMs = durationMs;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Object> details) {
        this.details = details != null ? new HashMap<>(details) : new HashMap<>();
    }
}
