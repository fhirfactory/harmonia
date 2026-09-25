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

package net.fhirfactory.harmonia.persistence.store;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import net.fhirfactory.harmonia.persistence.client.OperationsRestClient;
import net.fhirfactory.harmonia.persistence.config.OperationsStoreConfiguration;
import org.infinispan.commons.configuration.attributes.AttributeSet;
import org.infinispan.configuration.cache.AsyncStoreConfiguration;
import org.infinispan.persistence.spi.InitializationContext;
import org.infinispan.persistence.spi.MarshallableEntry;
import org.infinispan.persistence.spi.MarshallableEntryFactory;
import org.infinispan.persistence.spi.PersistenceException;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OperationsRestCacheStoreTest {

    private static WireMockServer wireMockServer;
    private OperationsRestCacheStore<String, String> store;
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
        store = new OperationsRestCacheStore<>();
        initContext = mock(InitializationContext.class);
        entryFactory = mock(MarshallableEntryFactory.class);

        org.infinispan.Cache cache = mock(org.infinispan.Cache.class);
        when(cache.getName()).thenReturn("tasksequence-cache");
        when(initContext.getCache()).thenReturn(cache);
        when(initContext.getMarshallableEntryFactory()).thenReturn((MarshallableEntryFactory) entryFactory);

        when(entryFactory.create(Mockito.<Object>any(), Mockito.<Object>any())).thenAnswer(invocation -> {
            MarshallableEntry<String, String> entry = mock(MarshallableEntry.class);
            when(entry.getKey()).thenReturn(invocation.getArgument(0));
            when(entry.getValue()).thenReturn(invocation.getArgument(1));
            return entry;
        });

        AttributeSet attributes = OperationsStoreConfiguration.attributeDefinitionSet();
        attributes.attribute(OperationsStoreConfiguration.SERVER_URL).set("http://localhost:" + wireMockServer.port() + "/api/operations");
        attributes.attribute(OperationsStoreConfiguration.TIMEOUT_SECONDS).set(5);
        OperationsStoreConfiguration config = new OperationsStoreConfiguration(attributes.protect(), mock(AsyncStoreConfiguration.class));
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
    @DisplayName("Write should PUT operational payload to Operations JPA server")
    void testWrite() throws Exception {
        stubFor(put(urlEqualTo("/api/operations/tasksequence/seq-001"))
                .willReturn(aResponse().withStatus(200).withBody("{\"sequenceId\":\"seq-001\"}")));

        MarshallableEntry<String, String> entry = mock(MarshallableEntry.class);
        when(entry.getKey()).thenReturn("seq-001");
        when(entry.getValue()).thenReturn("{\"sequenceId\":\"seq-001\",\"sequenceName\":\"Admissions\"}");

        store.write(0, entry).toCompletableFuture().get(5, TimeUnit.SECONDS);

        verify(putRequestedFor(urlEqualTo("/api/operations/tasksequence/seq-001"))
                .withHeader("Content-Type", containing("application/json")));
    }

    @Test
    @DisplayName("Write should fail exceptionally when Operations server returns HTTP error (false result)")
    void testWriteFailureOnServerError() {
        stubFor(put(urlEqualTo("/api/operations/tasksequence/seq-001"))
                .willReturn(aResponse().withStatus(500).withBody("Internal Server Error")));

        MarshallableEntry<String, String> entry = mock(MarshallableEntry.class);
        when(entry.getKey()).thenReturn("seq-001");
        when(entry.getValue()).thenReturn("{\"sequenceId\":\"seq-001\",\"sequenceName\":\"Admissions\"}");

        assertThatThrownBy(() -> store.write(0, entry).toCompletableFuture().get(5, TimeUnit.SECONDS))
                .isInstanceOf(ExecutionException.class)
                .hasCauseInstanceOf(PersistenceException.class)
                .hasMessageContaining("Failed to persist tasksequence/seq-001");
    }

    @Test
    @DisplayName("Write should propagate exception when REST client fails exceptionally")
    void testWriteFailureOnClientException() {
        OperationsRestClient mockClient = mock(OperationsRestClient.class);
        RuntimeException error = new RuntimeException("Operations endpoint unreachable");
        when(mockClient.saveResourceJson(any(), any(), any()))
                .thenReturn(CompletableFuture.failedFuture(error));
        store.setRestClient(mockClient);

        MarshallableEntry<String, String> entry = mock(MarshallableEntry.class);
        when(entry.getKey()).thenReturn("seq-001");
        when(entry.getValue()).thenReturn("{\"sequenceId\":\"seq-001\",\"sequenceName\":\"Admissions\"}");

        assertThatThrownBy(() -> store.write(0, entry).toCompletableFuture().get(5, TimeUnit.SECONDS))
                .isInstanceOf(ExecutionException.class)
                .hasCause(error);
    }

    @Test
    @DisplayName("Write should complete normally for null entry, key, or value")
    void testWriteNullHandling() throws Exception {
        assertThat(store.write(0, null).toCompletableFuture().get(5, TimeUnit.SECONDS)).isNull();

        MarshallableEntry<String, String> nullKeyEntry = mock(MarshallableEntry.class);
        when(nullKeyEntry.getKey()).thenReturn(null);
        when(nullKeyEntry.getValue()).thenReturn("{}");
        assertThat(store.write(0, nullKeyEntry).toCompletableFuture().get(5, TimeUnit.SECONDS)).isNull();

        MarshallableEntry<String, String> nullValueEntry = mock(MarshallableEntry.class);
        when(nullValueEntry.getKey()).thenReturn("seq-001");
        when(nullValueEntry.getValue()).thenReturn(null);
        assertThat(store.write(0, nullValueEntry).toCompletableFuture().get(5, TimeUnit.SECONDS)).isNull();
    }

    @Test
    @DisplayName("Load should fetch operational resource from Operations JPA server on cache miss")
    void testLoad() throws Exception {
        stubFor(get(urlEqualTo("/api/operations/tasksequence/seq-002"))
                .willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"sequenceId\":\"seq-002\",\"sequenceName\":\"Discharge\"}")));

        MarshallableEntry<String, String> loaded = store.load(0, "seq-002").toCompletableFuture().get(5, TimeUnit.SECONDS);
        assertThat(loaded).isNotNull();
        assertThat(loaded.getValue()).contains("Discharge");
    }

    @Test
    @DisplayName("Delete should execute DELETE on Operations JPA server")
    void testDelete() throws Exception {
        stubFor(delete(urlEqualTo("/api/operations/tasksequence/seq-003"))
                .willReturn(aResponse().withStatus(204)));

        Boolean deleted = store.delete(0, "seq-003").toCompletableFuture().get(5, TimeUnit.SECONDS);
        assertThat(deleted).isTrue();

        verify(deleteRequestedFor(urlEqualTo("/api/operations/tasksequence/seq-003")));
    }

    @Test
    @DisplayName("Contains key should check existence on Operations JPA server")
    void testContainsKey() throws Exception {
        stubFor(head(urlEqualTo("/api/operations/tasksequence/seq-004"))
                .willReturn(aResponse().withStatus(200)));

        Boolean exists = store.containsKey(0, "seq-004").toCompletableFuture().get(5, TimeUnit.SECONDS);
        assertThat(exists).isTrue();

        verify(headRequestedFor(urlEqualTo("/api/operations/tasksequence/seq-004")));
    }

    @Test
    @DisplayName("Publish entries should stream items from Operations JPA server")
    void testPublishEntriesAndSize() throws Exception {
        stubFor(get(urlEqualTo("/api/operations/tasksequence"))
                .willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[{\"objectType\":\"tasksequence\",\"objectId\":\"seq-100\",\"dataJson\":\"{\\\"sequenceId\\\":\\\"seq-100\\\"}\"},{\"objectType\":\"tasksequence\",\"objectId\":\"seq-200\",\"dataJson\":\"{\\\"sequenceId\\\":\\\"seq-200\\\"}\"}]")));

        Long count = store.size(null).toCompletableFuture().get(5, TimeUnit.SECONDS);
        assertThat(count).isEqualTo(2L);

        java.util.List<String> keys = new java.util.ArrayList<>();
        java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
        store.publishKeys(null, null).subscribe(new org.reactivestreams.Subscriber<String>() {
            @Override
            public void onSubscribe(org.reactivestreams.Subscription s) {
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(String s) {
                keys.add(s);
            }

            @Override
            public void onError(Throwable t) {
                latch.countDown();
            }

            @Override
            public void onComplete() {
                latch.countDown();
            }
        });

        assertThat(latch.await(5, TimeUnit.SECONDS)).isTrue();
        assertThat(keys).containsExactlyInAnyOrder("seq-100", "seq-200");
    }

    @Test
    @DisplayName("Coordinate resolution should map cache names correctly")
    void testCoordinateResolution() {
        assertThat(OperationsRestCacheStore.mapCacheNameToObjectType("tasksequence-cache")).isEqualTo("tasksequence");
        assertThat(OperationsRestCacheStore.mapCacheNameToObjectType("task-sequence-cache")).isEqualTo("tasksequence");
        assertThat(OperationsRestCacheStore.mapCacheNameToObjectType("configuration-cache")).isEqualTo("config");
        assertThat(OperationsRestCacheStore.mapCacheNameToObjectType("custom-cache")).isEqualTo("custom");

        var coord1 = OperationsRestCacheStore.resolveCoordinate("seq-101", "tasksequence-cache");
        assertThat(coord1.objectType()).isEqualTo("tasksequence");
        assertThat(coord1.id()).isEqualTo("seq-101");

        var coord2 = OperationsRestCacheStore.resolveCoordinate("workflow/flow-999", "ignored-cache");
        assertThat(coord2.objectType()).isEqualTo("workflow");
        assertThat(coord2.id()).isEqualTo("flow-999");
    }
}
