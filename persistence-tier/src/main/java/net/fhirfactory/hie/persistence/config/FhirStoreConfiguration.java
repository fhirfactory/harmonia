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
