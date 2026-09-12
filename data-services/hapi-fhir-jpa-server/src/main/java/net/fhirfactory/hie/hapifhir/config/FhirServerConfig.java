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

package net.fhirfactory.hie.hapifhir.config;

import ca.uhn.fhir.context.FhirContext;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FhirServerConfig {

    @Bean
    public FhirContext fhirContext() {
        return FhirContext.forR5();
    }

    @Bean
    public ServletRegistrationBean<JpaRestfulServer> fhirServletRegistration(ApplicationContext applicationContext, FhirContext fhirContext) {
        JpaRestfulServer servlet = new JpaRestfulServer(applicationContext, fhirContext);
        ServletRegistrationBean<JpaRestfulServer> registration = new ServletRegistrationBean<>(servlet, "/fhir/*");
        registration.setName("FhirRestfulServerServlet");
        registration.setLoadOnStartup(1);
        return registration;
    }
}
