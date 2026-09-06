package net.fhirfactory.hie.persistence.config;

import org.infinispan.commons.configuration.Builder;
import org.infinispan.commons.configuration.attributes.AttributeSet;
import org.infinispan.configuration.cache.AbstractStoreConfigurationBuilder;
import org.infinispan.configuration.cache.PersistenceConfigurationBuilder;

import static net.fhirfactory.hie.persistence.config.FhirStoreConfiguration.*;

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
