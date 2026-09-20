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

/**
 * Health and round-trip latency status of an upstream or downstream dependency.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DependencyHealth implements Serializable {

    private String name;                 // e.g. "Petasos", "Mneme"
    private String status;               // HEALTHY, DEGRADED, UNAVAILABLE, UNKNOWN
    private Long latencyMs;              // e.g. 12, or null if unmeasured / N/A
    private String message;

    public DependencyHealth() {
    }

    public DependencyHealth(String name, String status, Long latencyMs, String message) {
        this.name = name;
        this.status = status;
        this.latencyMs = latencyMs;
        this.message = message;
    }

    public DependencyHealth(String name, String status, long latencyMs, String message) {
        this(name, status, Long.valueOf(latencyMs), message);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getLatencyMs() {
        return latencyMs;
    }

    public void setLatencyMs(Long latencyMs) {
        this.latencyMs = latencyMs;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
