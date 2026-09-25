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

package net.fhirfactory.harmonia.infinispan;

import net.fhirfactory.harmonia.persistence.config.FhirStoreConfigurationBuilder;
import net.fhirfactory.harmonia.persistence.config.OperationsStoreConfigurationBuilder;
import net.fhirfactory.harmonia.persistence.store.FhirRestCacheStore;
import net.fhirfactory.harmonia.persistence.store.OperationsRestCacheStore;
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
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;

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
                .shared(false);

        Configuration config = builder.build();

        ConfigurationBuilder opsBuilder = new ConfigurationBuilder();
        opsBuilder.clustering().cacheMode(CacheMode.REPL_SYNC);
        opsBuilder.persistence()
                .passivation(false)
                .addStore(OperationsStoreConfigurationBuilder.class)
                .segmented(false)
                .serverUrl("http://localhost:8080/api/operations")
                .timeoutSeconds(5)
                .shared(false);

        Configuration opsConfig = opsBuilder.build();

        cacheManager = new DefaultCacheManager(global.build(), false);
        cacheManager.defineConfiguration("person-cache", config);
        cacheManager.defineConfiguration("practitioner-cache", config);
        cacheManager.defineConfiguration("organization-cache", config);
        cacheManager.defineConfiguration("tasksequence-cache", opsConfig);
        cacheManager.defineConfiguration("messagequeue-cache", opsConfig);
        cacheManager.defineConfiguration("modulestatus-cache", opsConfig);
        cacheManager.start();
    }

    @AfterEach
    void tearDown() {
        if (cacheManager != null) {
            cacheManager.stop();
        }
    }

    @Test
    @DisplayName("Verify cache definitions exist and can be instantiated with synchronous persistence")
    void testCacheInitialization() {
        Cache<String, String> personCache = cacheManager.getCache("person-cache");
        assertThat(personCache).isNotNull();
        assertThat(personCache.getName()).isEqualTo("person-cache");
        assertThat(personCache.getCacheConfiguration().persistence().stores()).isNotEmpty();
        assertThat(personCache.getCacheConfiguration().persistence().stores().get(0).async().enabled()).isFalse();

        Cache<String, String> practitionerCache = cacheManager.getCache("practitioner-cache");
        assertThat(practitionerCache).isNotNull();
        assertThat(practitionerCache.getName()).isEqualTo("practitioner-cache");
        assertThat(practitionerCache.getCacheConfiguration().persistence().stores()).isNotEmpty();
        assertThat(practitionerCache.getCacheConfiguration().persistence().stores().get(0).async().enabled()).isFalse();

        Cache<String, String> orgCache = cacheManager.getCache("organization-cache");
        assertThat(orgCache).isNotNull();
        assertThat(orgCache.getName()).isEqualTo("organization-cache");
        assertThat(orgCache.getCacheConfiguration().persistence().stores()).isNotEmpty();
        assertThat(orgCache.getCacheConfiguration().persistence().stores().get(0).async().enabled()).isFalse();

        Cache<String, String> seqCache = cacheManager.getCache("tasksequence-cache");
        assertThat(seqCache).isNotNull();
        assertThat(seqCache.getName()).isEqualTo("tasksequence-cache");
        assertThat(seqCache.getCacheConfiguration().persistence().stores()).isNotEmpty();
        assertThat(seqCache.getCacheConfiguration().persistence().stores().get(0).async().enabled()).isFalse();

        Cache<String, String> qCache = cacheManager.getCache("messagequeue-cache");
        assertThat(qCache).isNotNull();
        assertThat(qCache.getName()).isEqualTo("messagequeue-cache");
        assertThat(qCache.getCacheConfiguration().persistence().stores()).isNotEmpty();
        assertThat(qCache.getCacheConfiguration().persistence().stores().get(0).async().enabled()).isFalse();

        Cache<String, String> statusCache = cacheManager.getCache("modulestatus-cache");
        assertThat(statusCache).isNotNull();
        assertThat(statusCache.getName()).isEqualTo("modulestatus-cache");
        assertThat(statusCache.getCacheConfiguration().persistence().stores()).isNotEmpty();
        assertThat(statusCache.getCacheConfiguration().persistence().stores().get(0).async().enabled()).isFalse();
    }

    @Test
    @DisplayName("Verify production infinispan.xml defines synchronous persistence for all 17 caches")
    void testProductionXmlConfiguration() throws Exception {
        InputStream xmlStream = getClass().getClassLoader().getResourceAsStream("infinispan.xml");
        assertThat(xmlStream).isNotNull();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        Document doc = factory.newDocumentBuilder().parse(xmlStream);

        NodeList writeBehindNodes = doc.getElementsByTagName("write-behind");
        assertThat(writeBehindNodes.getLength()).isEqualTo(0);

        NodeList replicatedCaches = doc.getElementsByTagName("replicated-cache");
        assertThat(replicatedCaches.getLength()).isEqualTo(17);
    }
}
