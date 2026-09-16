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

package net.fhirfactory.harmonia.themis.api.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Authorization request submitted to Themis for deterministic evaluation.
 */
public record ThemisAuthorizationRequest(
        ThemisPrincipal principal,
        Set<ThemisAuthority> authorities,
        ThemisAction action,
        ThemisResource target,
        ThemisSecurityContext context
) implements Serializable {

    public ThemisAuthorizationRequest {
        authorities = authorities == null ? Set.of() : Collections.unmodifiableSet(Set.copyOf(authorities));
        context = context == null ? ThemisSecurityContext.anonymous() : context;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static ThemisAuthorizationRequest of(ThemisPrincipal principal, Set<ThemisAuthority> authorities, ThemisAction action, ThemisResource target) {
        return new ThemisAuthorizationRequest(principal, authorities, action, target, ThemisSecurityContext.anonymous());
    }

    public static ThemisAuthorizationRequest of(ThemisPrincipal principal, Set<ThemisAuthority> authorities, ThemisAction action, ThemisResource target, ThemisSecurityContext context) {
        return new ThemisAuthorizationRequest(principal, authorities, action, target, context);
    }

    public boolean hasAuthority(String authorityCode) {
        if (authorityCode == null) {
            return false;
        }
        return authorities.stream().anyMatch(a -> a.authorityCode().equalsIgnoreCase(authorityCode));
    }

    public boolean hasAnyAuthority(String... authorityCodes) {
        if (authorityCodes == null || authorityCodes.length == 0) {
            return false;
        }
        for (String code : authorityCodes) {
            if (hasAuthority(code)) {
                return true;
            }
        }
        return false;
    }

    public static class Builder {
        private ThemisPrincipal principal;
        private final Set<ThemisAuthority> authorities = new HashSet<>();
        private ThemisAction action;
        private ThemisResource target;
        private ThemisSecurityContext context;

        public Builder principal(ThemisPrincipal principal) {
            this.principal = principal;
            return this;
        }

        public Builder authorities(Set<ThemisAuthority> authorities) {
            if (authorities != null) {
                this.authorities.addAll(authorities);
            }
            return this;
        }

        public Builder authority(ThemisAuthority authority) {
            if (authority != null) {
                this.authorities.add(authority);
            }
            return this;
        }

        public Builder authority(String authorityCode) {
            if (authorityCode != null && !authorityCode.isBlank()) {
                this.authorities.add(ThemisAuthority.of(authorityCode));
            }
            return this;
        }

        public Builder action(ThemisAction action) {
            this.action = action;
            return this;
        }

        public Builder target(ThemisResource target) {
            this.target = target;
            return this;
        }

        public Builder context(ThemisSecurityContext context) {
            this.context = context;
            return this;
        }

        public ThemisAuthorizationRequest build() {
            return new ThemisAuthorizationRequest(principal, authorities, action, target, context);
        }
    }
}
