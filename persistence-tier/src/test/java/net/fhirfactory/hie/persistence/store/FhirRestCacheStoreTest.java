package net.fhirfactory.hie.persistence.store;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import net.fhirfactory.hie.persistence.config.FhirStoreConfiguration;
import org.infinispan.commons.configuration.attributes.AttributeSet;
import org.infinispan.configuration.cache.AsyncStoreConfiguration;
import org.infinispan.configuration.cache.PersistenceConfigurationBuilder;
import org.infinispan.persistence.spi.InitializationContext;
import org.infinispan.persistence.spi.MarshallableEntry;
import org.infinispan.persistence.spi.MarshallableEntryFactory;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FhirRestCacheStoreTest {

    private static WireMockServer wireMockServer;
    private FhirRestCacheStore<String, String> store;
    private InitializationContext initContext;
    private MarshallableEntryFactory<String, String> entryFactory;

    @BeforeAll
    static void startWireMock() {
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());
    }

    @AfterAll
    static void stopWireMock() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() throws Exception {
        store = new FhirRestCacheStore<>();
        initContext = mock(InitializationContext.class);
        entryFactory = mock(MarshallableEntryFactory.class);

        org.infinispan.Cache cache = mock(org.infinispan.Cache.class);
        when(cache.getName()).thenReturn("person-cache");
        when(initContext.getCache()).thenReturn(cache);
        when(initContext.getMarshallableEntryFactory()).thenReturn((MarshallableEntryFactory) entryFactory);

        when(entryFactory.create(Mockito.<Object>any(), Mockito.<Object>any())).thenAnswer(invocation -> {
            MarshallableEntry<String, String> entry = mock(MarshallableEntry.class);
            when(entry.getKey()).thenReturn(invocation.getArgument(0));
            when(entry.getValue()).thenReturn(invocation.getArgument(1));
            return entry;
        });

        AttributeSet attributes = FhirStoreConfiguration.attributeDefinitionSet();
        attributes.attribute(FhirStoreConfiguration.SERVER_URL).set("http://localhost:" + wireMockServer.port() + "/fhir");
        attributes.attribute(FhirStoreConfiguration.TIMEOUT_SECONDS).set(5);
        FhirStoreConfiguration config = new FhirStoreConfiguration(attributes.protect(), mock(AsyncStoreConfiguration.class));
        when(initContext.getConfiguration()).thenReturn(config);

        store.start(initContext).toCompletableFuture().get(5, TimeUnit.SECONDS);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (store != null) {
            store.stop().toCompletableFuture().get(5, TimeUnit.SECONDS);
        }
    }

    @Test
    @DisplayName("Write should PUT resource payload to FHIR server")
    void testWrite() throws Exception {
        stubFor(put(urlEqualTo("/fhir/Person/101"))
                .willReturn(aResponse().withStatus(200).withBody("{\"resourceType\":\"Person\",\"id\":\"101\"}")));

        MarshallableEntry<String, String> entry = mock(MarshallableEntry.class);
        when(entry.getKey()).thenReturn("101");
        when(entry.getValue()).thenReturn("{\"resourceType\":\"Person\",\"id\":\"101\",\"name\":[{\"family\":\"Doe\"}]}");

        store.write(0, entry).toCompletableFuture().get(5, TimeUnit.SECONDS);

        verify(putRequestedFor(urlEqualTo("/fhir/Person/101"))
                .withHeader("Content-Type", containing("application/fhir+json")));
    }

    @Test
    @DisplayName("Load should fetch resource from FHIR server on cache miss")
    void testLoad() throws Exception {
        stubFor(get(urlEqualTo("/fhir/Person/102"))
                .willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/fhir+json")
                        .withBody("{\"resourceType\":\"Person\",\"id\":\"102\",\"name\":[{\"family\":\"Smith\"}]}")));

        MarshallableEntry<String, String> loaded = store.load(0, "102").toCompletableFuture().get(5, TimeUnit.SECONDS);
        assertThat(loaded).isNotNull();
        assertThat(loaded.getValue()).contains("Smith");
    }

    @Test
    @DisplayName("Delete should execute DELETE on FHIR server")
    void testDelete() throws Exception {
        stubFor(delete(urlEqualTo("/fhir/Person/103"))
                .willReturn(aResponse().withStatus(204)));

        Boolean deleted = store.delete(0, "103").toCompletableFuture().get(5, TimeUnit.SECONDS);
        assertThat(deleted).isTrue();

        verify(deleteRequestedFor(urlEqualTo("/fhir/Person/103")));
    }

    @Test
    @DisplayName("Coordinate resolution should map cache names correctly")
    void testCoordinateResolution() {
        assertThat(FhirRestCacheStore.mapCacheNameToResourceType("person-cache")).isEqualTo("Person");
        assertThat(FhirRestCacheStore.mapCacheNameToResourceType("practitioner-cache")).isEqualTo("Practitioner");
        assertThat(FhirRestCacheStore.mapCacheNameToResourceType("organization-cache")).isEqualTo("Organization");
        assertThat(FhirRestCacheStore.mapCacheNameToResourceType("organisation-cache")).isEqualTo("Organization");
        assertThat(FhirRestCacheStore.mapCacheNameToResourceType("location-cache")).isEqualTo("Location");
        assertThat(FhirRestCacheStore.mapCacheNameToResourceType("group-cache")).isEqualTo("Group");

        var coord1 = FhirRestCacheStore.resolveCoordinate("123", "person-cache");
        assertThat(coord1.resourceType()).isEqualTo("Person");
        assertThat(coord1.id()).isEqualTo("123");

        var coord2 = FhirRestCacheStore.resolveCoordinate("PractitionerRole/999", "custom-cache");
        assertThat(coord2.resourceType()).isEqualTo("PractitionerRole");
        assertThat(coord2.id()).isEqualTo("999");
    }
}
