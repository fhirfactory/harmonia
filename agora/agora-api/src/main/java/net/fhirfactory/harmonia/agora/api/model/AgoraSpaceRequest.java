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

package net.fhirfactory.harmonia.agora.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Request to provision or configure an Agora collaboration Space in Matrix.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AgoraSpaceRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("harmoniaResourceType")
    private final String harmoniaResourceType;

    @JsonProperty("harmoniaResourceId")
    private final String harmoniaResourceId;

    @JsonProperty("displayName")
    private final String displayName;

    @JsonProperty("topic")
    private final String topic;

    @JsonProperty("securityContext")
    private final ThemisSecurityContext securityContext;

    @JsonProperty("correlationId")
    private final String correlationId;

    @JsonProperty("initialMembers")
    private final List<String> initialMembers;

    @JsonProperty("metadata")
    private final Map<String, Object> metadata;

    @JsonCreator
    public AgoraSpaceRequest(
            @JsonProperty("harmoniaResourceType") String harmoniaResourceType,
            @JsonProperty("harmoniaResourceId") String harmoniaResourceId,
            @JsonProperty("displayName") String displayName,
            @JsonProperty("topic") String topic,
            @JsonProperty("securityContext") ThemisSecurityContext securityContext,
            @JsonProperty("correlationId") String correlationId,
            @JsonProperty("initialMembers") List<String> initialMembers,
            @JsonProperty("metadata") Map<String, Object> metadata) {

        this.harmoniaResourceType = harmoniaResourceType;
        this.harmoniaResourceId = harmoniaResourceId;
        this.displayName = displayName;
        this.topic = topic;
        this.securityContext = securityContext;
        this.correlationId = correlationId;
        this.initialMembers = (initialMembers != null) ? List.copyOf(initialMembers) : List.of();
        this.metadata = (metadata != null)
                ? Collections.unmodifiableMap(new LinkedHashMap<>(metadata))
                : Collections.emptyMap();
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getHarmoniaResourceType() {
        return harmoniaResourceType;
    }

    public String getHarmoniaResourceId() {
        return harmoniaResourceId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getTopic() {
        return topic;
    }

    public ThemisSecurityContext getSecurityContext() {
        return securityContext;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public List<String> getInitialMembers() {
        return initialMembers;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public static class Builder {
        private String harmoniaResourceType;
        private String harmoniaResourceId;
        private String displayName;
        private String topic;
        private ThemisSecurityContext securityContext;
        private String correlationId;
        private List<String> initialMembers = List.of();
        private Map<String, Object> metadata = new LinkedHashMap<>();

        public Builder harmoniaResourceType(String harmoniaResourceType) {
            this.harmoniaResourceType = harmoniaResourceType;
            return this;
        }

        public Builder harmoniaResourceId(String harmoniaResourceId) {
            this.harmoniaResourceId = harmoniaResourceId;
            return this;
        }

        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder topic(String topic) {
            this.topic = topic;
            return this;
        }

        public Builder securityContext(ThemisSecurityContext securityContext) {
            this.securityContext = securityContext;
            return this;
        }

        public Builder correlationId(String correlationId) {
            this.correlationId = correlationId;
            return this;
        }

        public Builder initialMembers(List<String> initialMembers) {
            this.initialMembers = initialMembers != null ? List.copyOf(initialMembers) : List.of();
            return this;
        }

        public Builder metadata(Map<String, Object> metadata) {
            if (metadata != null) {
                this.metadata = new LinkedHashMap<>(metadata);
            }
            return this;
        }

        public Builder addMetadata(String key, Object value) {
            this.metadata.put(key, value);
            return this;
        }

        public AgoraSpaceRequest build() {
            return new AgoraSpaceRequest(
                    harmoniaResourceType, harmoniaResourceId, displayName, topic,
                    securityContext, correlationId, initialMembers, metadata
            );
        }
    }
}
