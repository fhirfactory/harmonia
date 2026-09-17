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

package net.fhirfactory.harmonia.agora.matrix.appservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Model representing an Application Service registration definition in Matrix Synapse.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppServiceRegistration {

    @JsonProperty("id")
    private String id;

    @JsonProperty("url")
    private String url;

    @JsonProperty("as_token")
    private String asToken;

    @JsonProperty("hs_token")
    private String hsToken;

    @JsonProperty("sender_localpart")
    private String senderLocalpart;

    @JsonProperty("namespaces")
    private Namespaces namespaces = new Namespaces();

    @JsonProperty("rate_limited")
    private Boolean rateLimited = false;

    @JsonProperty("protocols")
    private List<String> protocols = new ArrayList<>();

    public AppServiceRegistration() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAsToken() {
        return asToken;
    }

    public void setAsToken(String asToken) {
        this.asToken = asToken;
    }

    public String getHsToken() {
        return hsToken;
    }

    public void setHsToken(String hsToken) {
        this.hsToken = hsToken;
    }

    public String getSenderLocalpart() {
        return senderLocalpart;
    }

    public void setSenderLocalpart(String senderLocalpart) {
        this.senderLocalpart = senderLocalpart;
    }

    public Namespaces getNamespaces() {
        return namespaces;
    }

    public void setNamespaces(Namespaces namespaces) {
        this.namespaces = namespaces;
    }

    public Boolean getRateLimited() {
        return rateLimited;
    }

    public void setRateLimited(Boolean rateLimited) {
        this.rateLimited = rateLimited;
    }

    public List<String> getProtocols() {
        return protocols;
    }

    public void setProtocols(List<String> protocols) {
        this.protocols = protocols;
    }

    /**
     * Serializes this registration definition into clean Matrix Synapse YAML format.
     *
     * @return YAML string representation
     */
    public String toYaml() {
        StringBuilder sb = new StringBuilder();
        sb.append("id: \"").append(escapeYaml(id)).append("\"\n");
        sb.append("url: \"").append(escapeYaml(url)).append("\"\n");
        sb.append("as_token: \"").append(escapeYaml(asToken)).append("\"\n");
        sb.append("hs_token: \"").append(escapeYaml(hsToken)).append("\"\n");
        sb.append("sender_localpart: \"").append(escapeYaml(senderLocalpart)).append("\"\n");
        sb.append("rate_limited: ").append(rateLimited != null && rateLimited).append("\n");
        sb.append("protocols:\n");
        if (protocols != null && !protocols.isEmpty()) {
            for (String proto : protocols) {
                sb.append("  - \"").append(escapeYaml(proto)).append("\"\n");
            }
        }
        sb.append("namespaces:\n");
        appendNamespaceList(sb, "users", namespaces != null ? namespaces.getUsers() : Collections.emptyList());
        appendNamespaceList(sb, "aliases", namespaces != null ? namespaces.getAliases() : Collections.emptyList());
        appendNamespaceList(sb, "rooms", namespaces != null ? namespaces.getRooms() : Collections.emptyList());
        return sb.toString();
    }

    private void appendNamespaceList(StringBuilder sb, String category, List<NamespaceRule> rules) {
        sb.append("  ").append(category).append(":\n");
        if (rules != null && !rules.isEmpty()) {
            for (NamespaceRule rule : rules) {
                sb.append("    - exclusive: ").append(rule.isExclusive()).append("\n");
                sb.append("      regex: \"").append(escapeYaml(rule.getRegex())).append("\"\n");
            }
        }
    }

    private String escapeYaml(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Namespaces {
        private List<NamespaceRule> users = new ArrayList<>();
        private List<NamespaceRule> aliases = new ArrayList<>();
        private List<NamespaceRule> rooms = new ArrayList<>();

        public List<NamespaceRule> getUsers() {
            return users;
        }

        public void setUsers(List<NamespaceRule> users) {
            this.users = users;
        }

        public List<NamespaceRule> getAliases() {
            return aliases;
        }

        public void setAliases(List<NamespaceRule> aliases) {
            this.aliases = aliases;
        }

        public List<NamespaceRule> getRooms() {
            return rooms;
        }

        public void setRooms(List<NamespaceRule> rooms) {
            this.rooms = rooms;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class NamespaceRule {
        private boolean exclusive;
        private String regex;

        public NamespaceRule() {
        }

        public NamespaceRule(boolean exclusive, String regex) {
            this.exclusive = exclusive;
            this.regex = regex;
        }

        public boolean isExclusive() {
            return exclusive;
        }

        public void setExclusive(boolean exclusive) {
            this.exclusive = exclusive;
        }

        public String getRegex() {
            return regex;
        }

        public void setRegex(String regex) {
            this.regex = regex;
        }
    }

    public static class Builder {
        private final AppServiceRegistration reg = new AppServiceRegistration();

        public Builder id(String id) {
            reg.setId(id);
            return this;
        }

        public Builder url(String url) {
            reg.setUrl(url);
            return this;
        }

        public Builder asToken(String asToken) {
            reg.setAsToken(asToken);
            return this;
        }

        public Builder hsToken(String hsToken) {
            reg.setHsToken(hsToken);
            return this;
        }

        public Builder senderLocalpart(String senderLocalpart) {
            reg.setSenderLocalpart(senderLocalpart);
            return this;
        }

        public Builder rateLimited(boolean rateLimited) {
            reg.setRateLimited(rateLimited);
            return this;
        }

        public Builder addUserNamespace(boolean exclusive, String regex) {
            reg.namespaces.getUsers().add(new NamespaceRule(exclusive, regex));
            return this;
        }

        public Builder addAliasNamespace(boolean exclusive, String regex) {
            reg.namespaces.getAliases().add(new NamespaceRule(exclusive, regex));
            return this;
        }

        public Builder addRoomNamespace(boolean exclusive, String regex) {
            reg.namespaces.getRooms().add(new NamespaceRule(exclusive, regex));
            return this;
        }

        public Builder protocol(String protocol) {
            reg.protocols.add(protocol);
            return this;
        }

        public AppServiceRegistration build() {
            return reg;
        }
    }
}
