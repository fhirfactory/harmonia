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

package net.fhirfactory.harmonia.paradeigma.scenarios.operations;

/**
 * Operational execution profiles for deterministic simulation and validation
 * across the five Iris Operations Console perspectives:
 * 1. ALL_HEALTHY: Platform and all 9 subsystems healthy, queues clear, zero DLQ, zero critical alerts.
 * 2. PETASOS_DEGRADED: Petasos messaging broker degraded with elevated latency, platform degraded.
 * 3. DLQ_GROWTH: ActiveMQ dead-letter queue build-up and elevated message redelivery.
 * 4. FAILED_PRAGMA: Workflow execution stall with a failed Pragma and Ergon checkpoint error.
 * 5. CRITICAL_ALERT: Actionable critical alert requiring operator attention and remediation.
 */
public enum IrisOperationsScenarioProfile {

    /**
     * Nominal baseline: all subsystems healthy, queues processing normally, no failures.
     */
    ALL_HEALTHY("All Subsystems Healthy", "Nominal operational state with all platform components healthy"),

    /**
     * Subsystem perspective degradation: Petasos broker experiencing latency and degraded status.
     */
    PETASOS_DEGRADED("Petasos Subsystem Degraded", "Petasos messaging broker degraded with elevated round-trip latency"),

    /**
     * Queues perspective anomaly: Dead-letter queue messages accumulated requiring triage.
     */
    DLQ_GROWTH("Queue DLQ Growth", "Inbound FHIR delivery dead-letter queue accumulation and redelivery backpressure"),

    /**
     * Workflows perspective failure: Praxis sequence stopped with a failed Pragma checkpoint.
     */
    FAILED_PRAGMA("Workflow Pragma Failed", "Clinical ADT ingestion Pragma failed during FHIR validation checkpoint"),

    /**
     * Alerts perspective alert: Critical alert requiring operator guidance and acknowledge workflow.
     */
    CRITICAL_ALERT("Critical Operational Alert", "Broker heap exhaustion alert requiring immediate operator acknowledgment");

    private final String displayName;
    private final String description;

    IrisOperationsScenarioProfile(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}
