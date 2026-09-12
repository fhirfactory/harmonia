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

package net.fhirfactory.hie.model.status;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Model representing the operational status, health, and readiness of an HIE module or subsystem.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModuleStatus implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final ObjectMapper MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

    @JsonProperty("moduleId")
    private String moduleId;

    @JsonProperty("moduleName")
    private String moduleName;

    @JsonProperty("moduleType")
    private String moduleType;

    @JsonProperty("status")
    private String status;

    @JsonProperty("ready")
    private boolean ready;

    @JsonProperty("startedAt")
    private String startedAt;

    @JsonProperty("lastUpdated")
    private String lastUpdated;

    @JsonProperty("instanceId")
    private String instanceId;

    @JsonProperty("host")
    private String host;

    @JsonProperty("port")
    private Integer port;

    @JsonProperty("endpointUrl")
    private String endpointUrl;

    @JsonProperty("details")
    private Map<String, Object> details;

    public ModuleStatus() {
        this.details = new HashMap<>();
        this.lastUpdated = Instant.now().toString();
    }

    public ModuleStatus(String moduleId, String moduleName, String moduleType, String status, boolean ready) {
        this.moduleId = moduleId;
        this.moduleName = moduleName;
        this.moduleType = moduleType;
        this.status = status;
        this.ready = ready;
        this.startedAt = Instant.now().toString();
        this.lastUpdated = Instant.now().toString();
        this.details = new HashMap<>();
    }

    public static ModuleStatus ready(String moduleId, String moduleName, String moduleType) {
        ModuleStatus status = new ModuleStatus(moduleId, moduleName, moduleType, "READY", true);
        return status;
    }

    public static ModuleStatus starting(String moduleId, String moduleName, String moduleType) {
        ModuleStatus status = new ModuleStatus(moduleId, moduleName, moduleType, "STARTING", false);
        return status;
    }

    public static ModuleStatus stopped(String moduleId, String moduleName, String moduleType) {
        ModuleStatus status = new ModuleStatus(moduleId, moduleName, moduleType, "STOPPED", false);
        return status;
    }

    public void touch() {
        this.lastUpdated = Instant.now().toString();
    }

    public void addDetail(String key, Object value) {
        if (this.details == null) {
            this.details = new HashMap<>();
        }
        this.details.put(key, value);
    }

    public String toJson() {
        try {
            return MAPPER.writeValueAsString(this);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize ModuleStatus to JSON", e);
        }
    }

    public static ModuleStatus fromJson(String json) {
        if (json == null || json.isBlank()) {
            return null;
        }
        try {
            return MAPPER.readValue(json, ModuleStatus.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize ModuleStatus from JSON: " + json, e);
        }
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public String getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(String startedAt) {
        this.startedAt = startedAt;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getEndpointUrl() {
        return endpointUrl;
    }

    public void setEndpointUrl(String endpointUrl) {
        this.endpointUrl = endpointUrl;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModuleStatus that = (ModuleStatus) o;
        return ready == that.ready &&
                Objects.equals(moduleId, that.moduleId) &&
                Objects.equals(moduleName, that.moduleName) &&
                Objects.equals(moduleType, that.moduleType) &&
                Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(moduleId, moduleName, moduleType, status, ready);
    }

    @Override
    public String toString() {
        return "ModuleStatus{" +
                "moduleId='" + moduleId + '\'' +
                ", moduleName='" + moduleName + '\'' +
                ", moduleType='" + moduleType + '\'' +
                ", status='" + status + '\'' +
                ", ready=" + ready +
                ", startedAt='" + startedAt + '\'' +
                ", lastUpdated='" + lastUpdated + '\'' +
                '}';
    }
}
