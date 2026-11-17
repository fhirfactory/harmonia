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

package net.fhirfactory.harmonia.paradeigma.test.security;

import net.fhirfactory.harmonia.model.core.security.valuesets.HarmoniaRoleEnum;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.security.Principal;
import java.util.Set;

/** Supplies trusted servlet-container identity to security integration tests. */
public final class TrustedServletRequests {

    private TrustedServletRequests() {
    }

    /** Creates a servlet request whose principal and roles come from the simulated trusted container. */
    public static MockHttpServletRequest forPrincipal(String principalId, Set<HarmoniaRoleEnum> roles) {
        return configure(new MockHttpServletRequest(), principalId, roles);
    }

    /** Creates a MockMvc request post-processor with the same trusted-container semantics. */
    public static RequestPostProcessor withPrincipal(String principalId, Set<HarmoniaRoleEnum> roles) {
        return request -> configure(request, principalId, roles);
    }

    private static MockHttpServletRequest configure(
            MockHttpServletRequest request,
            String principalId,
            Set<HarmoniaRoleEnum> roles) {
        if (principalId == null || principalId.isBlank()) {
            throw new IllegalArgumentException("principalId must not be null or blank");
        }
        request.setUserPrincipal((Principal) () -> principalId.trim());
        if (roles != null) {
            for (HarmoniaRoleEnum role : roles) {
                if (role != null) {
                    request.addUserRole(role.getRoleCode());
                }
            }
        }
        return request;
    }
}