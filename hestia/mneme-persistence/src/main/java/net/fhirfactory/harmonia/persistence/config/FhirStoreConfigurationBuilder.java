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

package net.fhirfactory.harmonia.persistence.config;

import org.infinispan.commons.configuration.Builder;
import org.infinispan.commons.configuration.attributes.AttributeSet;
import org.infinispan.configuration.cache.AbstractStoreConfigurationBuilder;
import org.infinispan.configuration.cache.PersistenceConfigurationBuilder;

import static net.fhirfactory.harmonia.persistence.config.FhirStoreConfiguration.*;

public class FhirStoreConfigurationBuilder extends AbstractStoreConfigurationBuilder<FhirStoreConfiguration, FhirStoreConfigurationBuilder> {

    public FhirStoreConfigurationBuilder(PersistenceConfigurationBuilder builder) {
        super(builder, FhirStoreConfiguration.attributeDefinitionSet());
    }

    public FhirStoreConfigurationBuilder(PersistenceConfigurationBuilder builder, AttributeSet attributeSet) {
        super(builder, attributeSet);
    }

    public FhirStoreConfigurationBuilder serverUrl(String url) {
        attributes.attribute(SERVER_URL).set(url);
        return this;
    }

    public FhirStoreConfigurationBuilder timeoutSeconds(int timeout) {
        attributes.attribute(TIMEOUT_SECONDS).set(timeout);
        return this;
    }

    public FhirStoreConfigurationBuilder maxConnections(int maxConnections) {
        attributes.attribute(MAX_CONNECTIONS).set(maxConnections);
        return this;
    }

    @Override
    public FhirStoreConfiguration create() {
        return new FhirStoreConfiguration(attributes.protect(), async.create());
    }

    @Override
    public FhirStoreConfigurationBuilder self() {
        return this;
    }
}
