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
import java.util.ArrayList;
import java.util.List;

/**
 * Detailed operational summary of a Pragma workflow envelope execution instance.
 * Captures lifecycle state, correlation tracking IDs, Ergon checkpoints, and failure reason codes.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PragmaSummary implements Serializable {

    private String pragmaId;
    private String praxisId;
    private String status;               // RUNNING, COMPLETED, FAILED, RETRYING
    private long startedAt;
    private long durationMs;
    private String currentErgon;
    private int completedErgaCount;
    private int retryCount;
    private String correlationId;
    private String causationId;
    private String failureReasonCode;
    private List<ErgonCheckpoint> checkpoints = new ArrayList<>();

    public PragmaSummary() {
    }

    public PragmaSummary(String pragmaId, String praxisId, String status, long startedAt,
                         long durationMs, String currentErgon, int completedErgaCount,
                         int retryCount, String correlationId, String causationId,
                         String failureReasonCode) {
        this.pragmaId = pragmaId;
        this.praxisId = praxisId;
        this.status = status;
        this.startedAt = startedAt;
        this.durationMs = durationMs;
        this.currentErgon = currentErgon;
        this.completedErgaCount = completedErgaCount;
        this.retryCount = retryCount;
        this.correlationId = correlationId;
        this.causationId = causationId;
        this.failureReasonCode = failureReasonCode;
    }

    public String getPragmaId() {
        return pragmaId;
    }

    public void setPragmaId(String pragmaId) {
        this.pragmaId = pragmaId;
    }

    public String getPraxisId() {
        return praxisId;
    }

    public void setPraxisId(String praxisId) {
        this.praxisId = praxisId;
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

    public long getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(long durationMs) {
        this.durationMs = durationMs;
    }

    public String getCurrentErgon() {
        return currentErgon;
    }

    public void setCurrentErgon(String currentErgon) {
        this.currentErgon = currentErgon;
    }

    public int getCompletedErgaCount() {
        return completedErgaCount;
    }

    public void setCompletedErgaCount(int completedErgaCount) {
        this.completedErgaCount = completedErgaCount;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getCausationId() {
        return causationId;
    }

    public void setCausationId(String causationId) {
        this.causationId = causationId;
    }

    public String getFailureReasonCode() {
        return failureReasonCode;
    }

    public void setFailureReasonCode(String failureReasonCode) {
        this.failureReasonCode = failureReasonCode;
    }

    public List<ErgonCheckpoint> getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoints(List<ErgonCheckpoint> checkpoints) {
        this.checkpoints = checkpoints != null ? new ArrayList<>(checkpoints) : new ArrayList<>();
    }
}
