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
import java.util.Map;

/**
 * Request to transition or synchronize room membership in Matrix under Themis authorization.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AgoraMembershipRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("roomId")
    private final String roomId;

    @JsonProperty("userId")
    private final String userId;

    @JsonProperty("action")
    private final AgoraMembershipAction action;

    @JsonProperty("reason")
    private final String reason;

    @JsonProperty("securityContext")
    private final ThemisSecurityContext securityContext;

    @JsonProperty("correlationId")
    private final String correlationId;

    @JsonProperty("metadata")
    private final Map<String, Object> metadata;

    @JsonCreator
    public AgoraMembershipRequest(
            @JsonProperty("roomId") String roomId,
            @JsonProperty("userId") String userId,
            @JsonProperty("action") AgoraMembershipAction action,
            @JsonProperty("reason") String reason,
            @JsonProperty("securityContext") ThemisSecurityContext securityContext,
            @JsonProperty("correlationId") String correlationId,
            @JsonProperty("metadata") Map<String, Object> metadata) {

        this.roomId = roomId;
        this.userId = userId;
        this.action = action;
        this.reason = reason;
        this.securityContext = securityContext;
        this.correlationId = correlationId;
        this.metadata = (metadata != null)
                ? Collections.unmodifiableMap(new LinkedHashMap<>(metadata))
                : Collections.emptyMap();
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getRoomId() {
        return roomId;
    }

    public String getUserId() {
        return userId;
    }

    public AgoraMembershipAction getAction() {
        return action;
    }

    public String getReason() {
        return reason;
    }

    public ThemisSecurityContext getSecurityContext() {
        return securityContext;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public static class Builder {
        private String roomId;
        private String userId;
        private AgoraMembershipAction action = AgoraMembershipAction.INVITE;
        private String reason;
        private ThemisSecurityContext securityContext;
        private String correlationId;
        private Map<String, Object> metadata = new LinkedHashMap<>();

        public Builder roomId(String roomId) {
            this.roomId = roomId;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder action(AgoraMembershipAction action) {
            this.action = action;
            return this;
        }

        public Builder reason(String reason) {
            this.reason = reason;
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

        public AgoraMembershipRequest build() {
            return new AgoraMembershipRequest(
                    roomId, userId, action, reason,
                    securityContext, correlationId, metadata
            );
        }
    }
}
