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

package net.fhirfactory.hie.operationscli.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import net.fhirfactory.hie.model.sequence.TaskSequenceDefinition;

/**
 * Data Transfer Object representing a TaskSequence definition in the CLI.
 * Extends {@link TaskSequenceDefinition} for unified representation across HIE layers.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskSequenceDto extends TaskSequenceDefinition {

    private static final long serialVersionUID = 1L;

    public TaskSequenceDto() {
        super();
    }

    public TaskSequenceDto(String sequenceId, String sequenceName) {
        super(sequenceId, sequenceName);
    }

    public TaskSequenceDto(TaskSequenceDefinition definition) {
        super(definition);
    }

    @Override
    public String toString() {
        return "TaskSequenceDto{" +
                "sequenceId='" + getSequenceId() + '\'' +
                ", sequenceName='" + getSequenceName() + '\'' +
                ", version='" + getVersion() + '\'' +
                ", enabled=" + isEnabled() +
                ", gateways=" + getTargetGatewayInstances() +
                ", triggers=" + getTargetTriggerTypes() +
                ", activities=" + getActivityIds() +
                '}';
    }
}
