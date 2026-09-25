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

import net.fhirfactory.harmonia.persistence.client.OperationsRestClient;
import net.fhirfactory.harmonia.persistence.config.OperationsStoreConfiguration;
import org.infinispan.commons.configuration.ConfiguredBy;
import org.infinispan.commons.marshall.WrappedByteArray;
import org.infinispan.commons.util.IntSet;
import org.infinispan.persistence.spi.InitializationContext;
import org.infinispan.persistence.spi.MarshallableEntry;
import org.infinispan.persistence.spi.MarshallableEntryFactory;
import org.infinispan.persistence.spi.NonBlockingStore;
import org.infinispan.persistence.spi.PersistenceException;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Predicate;

/**
 * Custom Infinispan {@link NonBlockingStore} implementation for non-FHIR operational data,
 * task sequences, and workflow definitions persisted via the HIE Operations JPA Server.
 */
@ConfiguredBy(OperationsStoreConfiguration.class)
public class OperationsRestCacheStore<K, V> implements NonBlockingStore<K, V> {

    private static final Logger log = LoggerFactory.getLogger(OperationsRestCacheStore.class);

    private InitializationContext ctx;
    private OperationsStoreConfiguration configuration;
    private OperationsRestClient restClient;
    private String cacheName;

    @Override
    public CompletionStage<Void> start(InitializationContext ctx) {
        this.ctx = ctx;
        this.configuration = ctx.getConfiguration();
        this.cacheName = ctx.getCache().getName();
        String configuredServerUrl = configuration != null
                ? configuration.serverUrl()
                : "http://mnemosyne-operations-1:8080/api/operations";
        String serverUrl = System.getProperty("ops.server.url", configuredServerUrl);
        int timeout = configuration != null ? configuration.timeoutSeconds() : 10;
        this.restClient = new OperationsRestClient(serverUrl, timeout);
        log.info("Initialized OperationsRestCacheStore for cache [{}] targeting server [{}]", cacheName, serverUrl);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletionStage<Void> stop() {
        log.info("Stopped OperationsRestCacheStore for cache [{}]", cacheName);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public Set<Characteristic> characteristics() {
        return EnumSet.noneOf(Characteristic.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public CompletionStage<MarshallableEntry<K, V>> load(int segment, Object key) {
        if (key == null) {
            return CompletableFuture.completedFuture(null);
        }

        OperationCoordinate coord = resolveCoordinate(key, cacheName);
        log.debug("Operations cache load triggered for {}/{}", coord.objectType(), coord.id());

        return restClient.getResourceJson(coord.objectType(), coord.id())
                .thenApply(json -> {
                    if (json == null) {
                        return null;
                    }
                    MarshallableEntryFactory<K, V> factory = (MarshallableEntryFactory<K, V>) ctx.getMarshallableEntryFactory();
                    return factory.create((K) key, (V) json);
                });
    }

    @Override
    public CompletionStage<Void> write(int segment, MarshallableEntry<? extends K, ? extends V> entry) {
        if (entry == null || entry.getKey() == null || entry.getValue() == null) {
            return CompletableFuture.completedFuture(null);
        }

        Object key = entry.getKey();
        Object val = entry.getValue();
        OperationCoordinate coord = resolveCoordinate(key, cacheName);
        String jsonPayload = valueToString(val);

        log.info("Write-behind operations persistence executing for {}/{}", coord.objectType(), coord.id());
        return restClient.saveResourceJson(coord.objectType(), coord.id(), jsonPayload)
                .thenCompose(success -> {
                    if (Boolean.TRUE.equals(success)) {
                        log.debug("Successfully persisted {}/{} to Operations JPA server", coord.objectType(), coord.id());
                        return CompletableFuture.completedFuture(null);
                    } else {
                        log.error("Failed to persist {}/{} to Operations JPA server", coord.objectType(), coord.id());
                        return CompletableFuture.failedFuture(
                                new PersistenceException("Failed to persist " + coord.objectType() + "/" + coord.id() + " to Operations JPA server"));
                    }
                });
    }

    @Override
    public CompletionStage<Boolean> delete(int segment, Object key) {
        if (key == null) {
            return CompletableFuture.completedFuture(false);
        }
        OperationCoordinate coord = resolveCoordinate(key, cacheName);
        log.debug("Write-behind operations delete executing for {}/{}", coord.objectType(), coord.id());
        return restClient.deleteResource(coord.objectType(), coord.id());
    }

    @Override
    public CompletionStage<Boolean> containsKey(int segment, Object key) {
        if (key == null) {
            return CompletableFuture.completedFuture(false);
        }
        OperationCoordinate coord = resolveCoordinate(key, cacheName);
        return restClient.containsResource(coord.objectType(), coord.id());
    }

    @Override
    public CompletionStage<Void> clear() {
        log.warn("Cache clear called on operations cache [{}] - in-memory cleared without wiping operations JPA store", cacheName);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletionStage<Long> size(IntSet segments) {
        if (restClient == null) {
            return CompletableFuture.completedFuture(0L);
        }
        String objType = mapCacheNameToObjectType(cacheName);
        return restClient.listResources(objType).thenApply(list -> (long) list.size());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Publisher<MarshallableEntry<K, V>> publishEntries(IntSet segments, Predicate<? super K> filter, boolean includeValues) {
        if (restClient == null) {
            return subscriber -> {
                subscriber.onSubscribe(new Subscription() {
                    @Override
                    public void request(long n) {}
                    @Override
                    public void cancel() {}
                });
                subscriber.onComplete();
            };
        }
        String objType = mapCacheNameToObjectType(cacheName);
        return subscriber -> {
            subscriber.onSubscribe(new Subscription() {
                private volatile boolean cancelled = false;

                @Override
                public void request(long n) {
                    if (cancelled) return;
                    restClient.listResources(objType).thenAccept(entries -> {
                        if (cancelled) return;
                        MarshallableEntryFactory<K, V> factory = (MarshallableEntryFactory<K, V>) ctx.getMarshallableEntryFactory();
                        for (OperationsRestClient.OperationResourceEntry entry : entries) {
                            if (cancelled) break;
                            K key = (K) entry.objectId();
                            if (filter == null || filter.test(key)) {
                                V val = (V) (includeValues ? entry.dataJson() : null);
                                subscriber.onNext(factory.create(key, val));
                            }
                        }
                        if (!cancelled) {
                            subscriber.onComplete();
                        }
                    }).exceptionally(t -> {
                        if (!cancelled) {
                            subscriber.onError(t);
                        }
                        return null;
                    });
                }

                @Override
                public void cancel() {
                    cancelled = true;
                }
            });
        };
    }

    @Override
    @SuppressWarnings("unchecked")
    public Publisher<K> publishKeys(IntSet segments, Predicate<? super K> filter) {
        if (restClient == null) {
            return subscriber -> {
                subscriber.onSubscribe(new Subscription() {
                    @Override
                    public void request(long n) {}
                    @Override
                    public void cancel() {}
                });
                subscriber.onComplete();
            };
        }
        String objType = mapCacheNameToObjectType(cacheName);
        return subscriber -> {
            subscriber.onSubscribe(new Subscription() {
                private volatile boolean cancelled = false;

                @Override
                public void request(long n) {
                    if (cancelled) return;
                    restClient.listResources(objType).thenAccept(entries -> {
                        if (cancelled) return;
                        for (OperationsRestClient.OperationResourceEntry entry : entries) {
                            if (cancelled) break;
                            K key = (K) entry.objectId();
                            if (filter == null || filter.test(key)) {
                                subscriber.onNext(key);
                            }
                        }
                        if (!cancelled) {
                            subscriber.onComplete();
                        }
                    }).exceptionally(t -> {
                        if (!cancelled) {
                            subscriber.onError(t);
                        }
                        return null;
                    });
                }

                @Override
                public void cancel() {
                    cancelled = true;
                }
            });
        };
    }

    public OperationsRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(OperationsRestClient restClient) {
        this.restClient = restClient;
    }

    public static OperationCoordinate resolveCoordinate(Object key, String cacheName) {
        String keyStr = valueToString(key);
        if (keyStr.contains("/")) {
            String[] parts = keyStr.split("/", 2);
            return new OperationCoordinate(parts[0], parts[1]);
        }
        String objType = mapCacheNameToObjectType(cacheName);
        return new OperationCoordinate(objType, keyStr);
    }

    private static String valueToString(Object value) {
        if (value == null) {
            return "";
        }
        if (value instanceof WrappedByteArray wrapped) {
            return new String(wrapped.getBytes(), StandardCharsets.UTF_8);
        }
        if (value instanceof byte[] bytes) {
            return new String(bytes, StandardCharsets.UTF_8);
        }
        return Objects.toString(value, "");
    }

    public static String mapCacheNameToObjectType(String cacheName) {
        if (cacheName == null || cacheName.isBlank()) {
            return "operations";
        }
        String normalized = cacheName.toLowerCase().replace("-cache", "").replace("_cache", "").replace("-", "");
        return switch (normalized) {
            case "tasksequence", "tasksequences", "sequence", "sequences" -> "tasksequence";
            case "config", "configuration", "configurations" -> "config";
            case "workflow", "workflows" -> "workflow";
            default -> cacheName.toLowerCase().replace("-cache", "").replace("_cache", "");
        };
    }

    public record OperationCoordinate(String objectType, String id) {}
}
