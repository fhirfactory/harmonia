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

package net.fhirfactory.harmonia.befe.provider;

import jakarta.enterprise.context.ApplicationScoped;
import net.fhirfactory.harmonia.befe.model.operations.OperationalHealth;
import net.fhirfactory.harmonia.befe.model.operations.TimeSeries;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Health provider for Calliope (Harmonia canonical schemas, common models, and converters).
 * Calliope is an embedded foundational library that operates within all platform runtimes.
 */
@ApplicationScoped
public class CalliopeHealthProvider extends AbstractSubsystemHealthProvider {

    public CalliopeHealthProvider() {
        super(
                "calliope",
                "Calliope",
                "Harmonia canonical schemas, common models, and HL7/FHIR transformation engine",
                "1.0.0-SNAPSHOT"
        );
    }

    @Override
    protected String determineState() {
        // Embedded domain models are always healthy when JVM is executing
        return "HEALTHY";
    }

    @Override
    public OperationalHealth getOperationalHealth() {
        OperationalHealth health = new OperationalHealth(
                subsystemId,
                "HEALTHY",
                null,
                0,
                0,
                null,
                "0 / 0 Dependencies (Foundation Layer)"
        );
        health.setDependencies(Collections.emptyList());
        health.getDetails().put("embedded", true);
        health.getDetails().put("supportedProfiles", List.of("FHIR R5", "HL7 v2.3/v2.5", "CDA"));
        return health;
    }

    @Override
    public Map<String, TimeSeries> getStatistics(String window) {
        Map<String, TimeSeries> stats = new LinkedHashMap<>();
        stats.put("conversions_rate", createEmptyTimeSeries("Conversions Rate", window, "tx/s"));
        stats.put("schema_validations", createEmptyTimeSeries("Schema Validations", window, "ops/s"));
        stats.put("validation_success", createEmptyTimeSeries("Validation Success", window, "%"));
        stats.put("p95_latency", createEmptyTimeSeries("P95 Latency", window, "ms"));
        return stats;
    }
}
