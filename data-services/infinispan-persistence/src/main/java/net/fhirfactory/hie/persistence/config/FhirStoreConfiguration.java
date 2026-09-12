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

package net.fhirfactory.hie.persistence.config;

import org.infinispan.commons.configuration.BuiltBy;
import org.infinispan.commons.configuration.ConfigurationFor;
import org.infinispan.commons.configuration.attributes.AttributeDefinition;
import org.infinispan.commons.configuration.attributes.AttributeSet;
import org.infinispan.configuration.cache.AbstractStoreConfiguration;
import org.infinispan.configuration.cache.AsyncStoreConfiguration;
import net.fhirfactory.hie.persistence.store.FhirRestCacheStore;
import org.infinispan.configuration.parsing.Element;

@BuiltBy(FhirStoreConfigurationBuilder.class)
@ConfigurationFor(FhirRestCacheStore.class)
public class FhirStoreConfiguration extends AbstractStoreConfiguration {

    public static final AttributeDefinition<String> SERVER_URL = AttributeDefinition.builder("serverUrl", "http://localhost:8080/fhir").immutable().build();
    public static final AttributeDefinition<Integer> TIMEOUT_SECONDS = AttributeDefinition.builder("timeoutSeconds", 10).immutable().build();
    public static final AttributeDefinition<Integer> MAX_CONNECTIONS = AttributeDefinition.builder("maxConnections", 50).immutable().build();

    public static AttributeSet attributeDefinitionSet() {
        return new AttributeSet(FhirStoreConfiguration.class, AbstractStoreConfiguration.attributeDefinitionSet(), SERVER_URL, TIMEOUT_SECONDS, MAX_CONNECTIONS);
    }

    public FhirStoreConfiguration(AttributeSet attributes, AsyncStoreConfiguration async) {
        super(Element.STORE, attributes, async);
    }

    public String serverUrl() {
        return attributes().attribute(SERVER_URL).get();
    }

    public int timeoutSeconds() {
        return attributes().attribute(TIMEOUT_SECONDS).get();
    }

    public int maxConnections() {
        return attributes().attribute(MAX_CONNECTIONS).get();
    }
}
