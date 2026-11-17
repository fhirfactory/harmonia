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

package net.fhirfactory.harmonia.model.core.workflow.tasks.factory;

import net.fhirfactory.harmonia.model.core.workflow.tasks.datatypes.PragmaCheckpoint;
import net.fhirfactory.harmonia.model.core.workflow.tasks.valuesets.PragmaStatus;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/** Assembles a typed checkpoint for an Ergon/Praxis execution. */
public final class PragmaCheckpointBuilder {

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

    /** Sets the checkpoint identifier. */
    public PragmaCheckpointBuilder checkpointId(String checkpointId) {
        this.checkpointId = checkpointId;
        return this;
    }

    /** Sets the owning Pragma identifier. */
    public PragmaCheckpointBuilder pragmaId(String pragmaId) {
        this.pragmaId = pragmaId;
        return this;
    }

    /** Sets the executing Ergon identifier. */
    public PragmaCheckpointBuilder ergonId(String ergonId) {
        this.ergonId = ergonId;
        return this;
    }

    /** Sets the governing Praxis identifier. */
    public PragmaCheckpointBuilder praxisId(String praxisId) {
        this.praxisId = praxisId;
        return this;
    }

    /** Sets the execution stage name. */
    public PragmaCheckpointBuilder stageName(String stageName) {
        this.stageName = stageName;
        return this;
    }

    /** Sets the observation timestamp. */
    public PragmaCheckpointBuilder timestamp(Date timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    /** Sets the checkpoint status. */
    public PragmaCheckpointBuilder status(PragmaStatus status) {
        this.status = status;
        return this;
    }

    /** Sets the optional status message. */
    public PragmaCheckpointBuilder statusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
        return this;
    }

    /** Sets the execution step index. */
    public PragmaCheckpointBuilder stepIndex(Integer stepIndex) {
        this.stepIndex = stepIndex;
        return this;
    }

    /** Adds or replaces checkpoint metadata. */
    public PragmaCheckpointBuilder metadata(Map<String, String> metadata) {
        if (metadata != null) {
            this.metadata.putAll(metadata);
        }
        return this;
    }

    /** Adds one metadata value. */
    public PragmaCheckpointBuilder addMetadata(String key, String value) {
        if (key != null) {
            metadata.put(key, value);
        }
        return this;
    }

    /** Builds a defensive checkpoint value. */
    public PragmaCheckpoint build() {
        return new PragmaCheckpoint(checkpointId, pragmaId, ergonId, praxisId, stageName,
                timestamp, status, statusMessage, stepIndex, metadata);
    }
}