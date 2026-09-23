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

package net.fhirfactory.harmonia.befe.config;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.util.List;

/**
 * Hardened JAX-RS PreMatching CORS filter for Iris BEFE presentation endpoints.
 * <p>
 * Short-circuits preflight OPTIONS requests before resource matching, returns 403 Forbidden
 * for untrusted preflight origins, and decorates trusted responses with exact allowed origin
 * and {@code Vary: Origin}.
 * </p>
 */
@Provider
@PreMatching
public class CorsFilter implements ContainerRequestFilter, ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if ("OPTIONS".equalsIgnoreCase(requestContext.getMethod())) {
            String origin = requestContext.getHeaderString("Origin");
            if (origin != null && !origin.isBlank()) {
                if (BefeCorsConfig.isOriginAllowed(origin)) {
                    String normalizedOrigin = BefeCorsConfig.normalizeOrigin(origin);
                    Response preflightResponse = Response.ok()
                            .header("Access-Control-Allow-Origin", normalizedOrigin)
                            .header("Vary", "Origin")
                            .header("Access-Control-Allow-Methods", BefeCorsConfig.ALLOWED_METHODS)
                            .header("Access-Control-Allow-Headers", BefeCorsConfig.ALLOWED_HEADERS)
                            .header("Access-Control-Max-Age", BefeCorsConfig.MAX_AGE_SECONDS)
                            .build();
                    requestContext.abortWith(preflightResponse);
                } else {
                    requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
                }
            }
        }
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        String origin = requestContext.getHeaderString("Origin");
        if (origin != null && !origin.isBlank() && BefeCorsConfig.isOriginAllowed(origin)) {
            String normalizedOrigin = BefeCorsConfig.normalizeOrigin(origin);
            responseContext.getHeaders().putSingle("Access-Control-Allow-Origin", normalizedOrigin);

            List<Object> varyHeaders = responseContext.getHeaders().get("Vary");
            if (varyHeaders == null || varyHeaders.isEmpty()) {
                responseContext.getHeaders().putSingle("Vary", "Origin");
            } else {
                boolean hasOrigin = false;
                for (Object v : varyHeaders) {
                    if (v != null && v.toString().contains("Origin")) {
                        hasOrigin = true;
                        break;
                    }
                }
                if (!hasOrigin) {
                    responseContext.getHeaders().add("Vary", "Origin");
                }
            }

            if ("OPTIONS".equalsIgnoreCase(requestContext.getMethod())) {
                responseContext.getHeaders().putSingle("Access-Control-Allow-Methods", BefeCorsConfig.ALLOWED_METHODS);
                responseContext.getHeaders().putSingle("Access-Control-Allow-Headers", BefeCorsConfig.ALLOWED_HEADERS);
                responseContext.getHeaders().putSingle("Access-Control-Max-Age", BefeCorsConfig.MAX_AGE_SECONDS);
            }
        }
    }
}
