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
 * Operational representation of a Harmonia subsystem (e.g., Petasos, Energeia, Mneme, etc.).
 * Includes operational health state, instance count, version, and optional hierarchical child subsystems.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OperationalSubsystem implements Serializable {

    private String id;                   // e.g. "petasos", "energeia"
    private String name;                 // e.g. "Petasos"
    private String description;          // e.g. "Harmonia messaging and transport"
    private String state;                // HEALTHY, DEGRADED, UNAVAILABLE, UNKNOWN
    private int instanceCount;
    private String version;              // e.g. "1.0.0"
    private long lastUpdated;
    private List<OperationalSubsystem> children = new ArrayList<>();

    public OperationalSubsystem() {
    }

    public OperationalSubsystem(String id, String name, String description, String state,
                                int instanceCount, String version, long lastUpdated) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.state = state;
        this.instanceCount = instanceCount;
        this.version = version;
        this.lastUpdated = lastUpdated;
    }

    public OperationalSubsystem(String id, String name, String description, String state,
                                int instanceCount, String version, long lastUpdated,
                                List<OperationalSubsystem> children) {
        this(id, name, description, state, instanceCount, version, lastUpdated);
        if (children != null) {
            this.children = new ArrayList<>(children);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getInstanceCount() {
        return instanceCount;
    }

    public void setInstanceCount(int instanceCount) {
        this.instanceCount = instanceCount;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public List<OperationalSubsystem> getChildren() {
        return children;
    }

    public void setChildren(List<OperationalSubsystem> children) {
        this.children = children != null ? new ArrayList<>(children) : new ArrayList<>();
    }
}
