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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.hl7.fhir.r5.model.Task;

import java.util.Objects;

/**
 * Execution and lifecycle status of a {@link Pragma} task instance in Harmonia.
 * <p>
 * Standard statuses correspond to the execution lifecycle of integration workflows
 * and map directly to HAPI FHIR {@link Task.TaskStatus} codes.
 */
public enum PragmaStatus {

    /**
     * The task instance has been created or staged in draft form and is not yet ready for processing.
     */
    DRAFT("draft"),

    /**
     * The task has been submitted and requested for execution, awaiting pickup by a Praxis workflow.
     */
    REQUESTED("requested"),

    /**
     * The task has been received and accepted by a workflow engine.
     */
    ACCEPTED("accepted"),

    /**
     * The task is actively being processed by one or more Ergon activities.
     */
    IN_PROGRESS("in-progress"),

    /**
     * The task execution is temporarily suspended or held awaiting prerequisite conditions.
     */
    ON_HOLD("on-hold"),

    /**
     * The task completed all execution stages successfully with outputs generated.
     */
    COMPLETED("completed"),

    /**
     * The task processing encountered an unrecoverable failure or exception.
     */
    FAILED("failed"),

    /**
     * The task was rejected before or during processing (e.g. invalid payload or duplicate).
     */
    REJECTED("rejected"),

    /**
     * The task was explicitly cancelled by the requesting system or operator.
     */
    CANCELLED("cancelled");

    private final String code;

    PragmaStatus(String code) {
        this.code = code;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    /**
     * Returns true if the status represents a final terminal state.
     *
     * @return true for COMPLETED, FAILED, REJECTED, CANCELLED
     */
    public boolean isTerminal() {
        return this == COMPLETED || this == FAILED || this == REJECTED || this == CANCELLED;
    }

    /**
     * Returns true if the status represents an active execution state.
     *
     * @return true for REQUESTED, ACCEPTED, IN_PROGRESS
     */
    public boolean isActive() {
        return this == REQUESTED || this == ACCEPTED || this == IN_PROGRESS;
    }

    /**
     * Maps this status to the equivalent HAPI FHIR R5 {@link Task.TaskStatus}.
     *
     * @return FHIR TaskStatus
     */
    public Task.TaskStatus toFhirTaskStatus() {
        return switch (this) {
            case DRAFT -> Task.TaskStatus.DRAFT;
            case REQUESTED -> Task.TaskStatus.REQUESTED;
            case ACCEPTED -> Task.TaskStatus.ACCEPTED;
            case IN_PROGRESS -> Task.TaskStatus.INPROGRESS;
            case ON_HOLD -> Task.TaskStatus.ONHOLD;
            case COMPLETED -> Task.TaskStatus.COMPLETED;
            case FAILED -> Task.TaskStatus.FAILED;
            case REJECTED -> Task.TaskStatus.REJECTED;
            case CANCELLED -> Task.TaskStatus.CANCELLED;
        };
    }

    /**
     * Converts a HAPI FHIR R5 {@link Task.TaskStatus} to {@link PragmaStatus}.
     *
     * @param fhirStatus FHIR status
     * @return PragmaStatus
     */
    public static PragmaStatus fromFhirTaskStatus(Task.TaskStatus fhirStatus) {
        if (fhirStatus == null) {
            return DRAFT;
        }
        return switch (fhirStatus) {
            case DRAFT -> DRAFT;
            case REQUESTED, RECEIVED -> REQUESTED;
            case ACCEPTED -> ACCEPTED;
            case INPROGRESS -> IN_PROGRESS;
            case ONHOLD -> ON_HOLD;
            case COMPLETED -> COMPLETED;
            case FAILED -> FAILED;
            case REJECTED -> REJECTED;
            case CANCELLED -> CANCELLED;
            case ENTEREDINERROR -> FAILED;
            default -> DRAFT;
        };
    }

    /**
     * Parses a string code or enum name into a {@link PragmaStatus}.
     *
     * @param value string value
     * @return matching PragmaStatus, or DRAFT if null/unknown
     */
    @JsonCreator
    public static PragmaStatus fromCode(String value) {
        if (value == null || value.trim().isEmpty()) {
            return DRAFT;
        }
        String normalized = value.trim().toLowerCase().replace("_", "-").replace(" ", "-");
        for (PragmaStatus status : values()) {
            if (Objects.equals(status.code, normalized) || Objects.equals(status.name().toLowerCase(), normalized.replace("-", "_"))) {
                return status;
            }
        }
        return DRAFT;
    }
}
