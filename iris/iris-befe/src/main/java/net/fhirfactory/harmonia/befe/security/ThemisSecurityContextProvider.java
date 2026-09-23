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

package net.fhirfactory.harmonia.befe.security;

import jakarta.enterprise.context.RequestScoped;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/**
 * CDI {@link RequestScoped} provider holding the canonical {@link ThemisSecurityContext}
 * established at the Iris BEFE ingress boundary after successful container authentication
 * and Themis authorization evaluation.
 *
 * Enforces Invariant 6 (Default-Deny Security Governance) and prevents context spoofing/tampering.
 */
@RequestScoped
public class ThemisSecurityContextProvider implements Serializable {

    private static final long serialVersionUID = 1L;

    private ThemisSecurityContext securityContext;

    public ThemisSecurityContextProvider() {
    }

    /**
     * Binds the canonical {@link ThemisSecurityContext} to the active request scope.
     * Fails if an attempt is made to overwrite an already bound, different security context.
     *
     * @param securityContext the canonical security context to bind
     * @throws NullPointerException if securityContext is null
     * @throws IllegalStateException if a different security context is already bound
     */
    public void setSecurityContext(ThemisSecurityContext securityContext) {
        Objects.requireNonNull(securityContext, "securityContext must not be null");
        if (this.securityContext != null && !Objects.equals(this.securityContext, securityContext)) {
            throw new IllegalStateException("ThemisSecurityContext is already bound to current request context and cannot be overridden");
        }
        this.securityContext = securityContext;
    }

    /**
     * Retrieves the active {@link ThemisSecurityContext} wrapped in an {@link Optional}.
     *
     * @return Optional containing the active security context, or empty if unbound
     */
    public Optional<ThemisSecurityContext> getSecurityContext() {
        return Optional.ofNullable(this.securityContext);
    }

    /**
     * Direct accessor for the active {@link ThemisSecurityContext}.
     *
     * @return the active security context, or null if unbound
     */
    public ThemisSecurityContext get() {
        return this.securityContext;
    }

    /**
     * Retrieves the active {@link ThemisSecurityContext}, failing closed if none is bound.
     *
     * @return the active security context
     * @throws SecurityException if no security context is bound to the current request
     */
    public ThemisSecurityContext requireSecurityContext() {
        if (this.securityContext == null) {
            throw new SecurityException("No ThemisSecurityContext bound to current request context [FAIL CLOSED]");
        }
        return this.securityContext;
    }

    /**
     * Checks whether an active security context is present in the current request.
     *
     * @return true if a security context is present, false otherwise
     */
    public boolean hasSecurityContext() {
        return this.securityContext != null;
    }

    /**
     * Clears the active security context from the current request scope.
     */
    public void clear() {
        this.securityContext = null;
    }
}
