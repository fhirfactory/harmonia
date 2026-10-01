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

package net.fhirfactory.harmonia.hapifhir.config;

import net.fhirfactory.harmonia.hapifhir.controller.security.AuthoritativeSecurityInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Objects;

/**
 * Spring MVC configuration registering the fail-closed Themis security interceptor
 * for all authoritative routes under {@code /api/authoritative/fhir/**}.
 */
@Configuration
public class AuthoritativeWebMvcConfig implements WebMvcConfigurer {

    private final AuthoritativeSecurityInterceptor securityInterceptor;

    @Autowired
    public AuthoritativeWebMvcConfig(AuthoritativeSecurityInterceptor securityInterceptor) {
        this.securityInterceptor = Objects.requireNonNull(securityInterceptor, "securityInterceptor must not be null");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(securityInterceptor)
                .addPathPatterns("/api/authoritative/fhir/**");
    }
}
