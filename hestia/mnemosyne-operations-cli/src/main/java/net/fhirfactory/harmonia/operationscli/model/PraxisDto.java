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

package net.fhirfactory.harmonia.operationscli.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import net.fhirfactory.harmonia.model.praxis.PraxisDefinition;

/**
 * Data Transfer Object representing a Praxis task sequence definition in the CLI.
 * Extends {@link PraxisDefinition} for unified representation across Harmonia layers.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PraxisDto extends PraxisDefinition {

    private static final long serialVersionUID = 1L;

    public PraxisDto() {
        super();
    }

    public PraxisDto(String sequenceId, String sequenceName) {
        super(sequenceId, sequenceName);
    }

    public PraxisDto(PraxisDefinition definition) {
        super(definition);
    }

    @Override
    public String toString() {
        return "PraxisDto{" +
                "praxisId='" + getPraxisId() + '\'' +
                ", praxisName='" + getPraxisName() + '\'' +
                ", version='" + getVersion() + '\'' +
                ", enabled=" + isEnabled() +
                ", gateways=" + getTargetGatewayInstances() +
                ", triggers=" + getTargetTriggerTypes() +
                ", activities=" + getActivityIds() +
                '}';
    }
}
