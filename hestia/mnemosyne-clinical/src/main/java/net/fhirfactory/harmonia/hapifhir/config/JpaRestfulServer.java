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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.HardcodedServerAddressStrategy;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.CorsInterceptor;
import ca.uhn.fhir.rest.server.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;
import jakarta.servlet.ServletException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.Collection;

public class JpaRestfulServer extends RestfulServer {

    private final ApplicationContext applicationContext;

    public JpaRestfulServer(ApplicationContext applicationContext, FhirContext fhirContext) {
        super(fhirContext);
        this.applicationContext = applicationContext;
    }

    @Override
    protected void initialize() throws ServletException {
        super.initialize();

        // Register Resource Providers
        Collection<IResourceProvider> providers = applicationContext.getBeansOfType(IResourceProvider.class).values();
        setResourceProviders(providers);

        // Configure Address Strategy
        setServerAddressStrategy(new HardcodedServerAddressStrategy("http://localhost:8080/fhir"));

        // Logging Interceptor
        LoggingInterceptor loggingInterceptor = new LoggingInterceptor();
        loggingInterceptor.setMessageFormat("Source[${remoteAddr}] Operation[${operationType} ${idOrResourceName}] Status[${servletResponseStatusCode}] ProcessingTime[${processingTimeMillis}ms]");
        registerInterceptor(loggingInterceptor);

        // Response Highlighter
        registerInterceptor(new ResponseHighlighterInterceptor());

        // CORS Interceptor
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept", "Origin", "User-Agent", "DNT", "Cache-Control", "X-Mx-ReqToken", "Keep-Alive", "X-Requested-With", "If-Modified-Since"));
        corsConfiguration.setMaxAge(300L);
        registerInterceptor(new CorsInterceptor(corsConfiguration));
    }
}
