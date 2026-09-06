package net.fhirfactory.hie.infinispan;

import net.fhirfactory.hie.persistence.config.FhirStoreConfigurationBuilder;
import net.fhirfactory.hie.persistence.store.FhirRestCacheStore;
import org.infinispan.Cache;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InfinispanClusterConfigTest {

    private EmbeddedCacheManager cacheManager;

    @BeforeEach
    void setUp() {
        GlobalConfigurationBuilder global = GlobalConfigurationBuilder.defaultClusteredBuilder();
        global.transport().clusterName("hie-test-cluster").defaultTransport();

        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.clustering().cacheMode(CacheMode.REPL_SYNC);
        builder.persistence()
                .passivation(false)
                .addStore(FhirStoreConfigurationBuilder.class)
                .segmented(false)
                .serverUrl("http://localhost:8080/fhir")
                .timeoutSeconds(5)
                .shared(false)
                .async()
                .enable()
                .modificationQueueSize(1024);

        Configuration config = builder.build();

        cacheManager = new DefaultCacheManager(global.build(), false);
        cacheManager.defineConfiguration("person-cache", config);
        cacheManager.defineConfiguration("practitioner-cache", config);
        cacheManager.defineConfiguration("organization-cache", config);
        cacheManager.start();
    }

    @AfterEach
    void tearDown() {
        if (cacheManager != null) {
            cacheManager.stop();
        }
    }

    @Test
    @DisplayName("Verify cache definitions exist and can be instantiated")
    void testCacheInitialization() {
        Cache<String, String> personCache = cacheManager.getCache("person-cache");
        assertThat(personCache).isNotNull();
        assertThat(personCache.getName()).isEqualTo("person-cache");

        Cache<String, String> practitionerCache = cacheManager.getCache("practitioner-cache");
        assertThat(practitionerCache).isNotNull();
        assertThat(practitionerCache.getName()).isEqualTo("practitioner-cache");

        Cache<String, String> orgCache = cacheManager.getCache("organization-cache");
        assertThat(orgCache).isNotNull();
        assertThat(orgCache.getName()).isEqualTo("organization-cache");
    }
}
