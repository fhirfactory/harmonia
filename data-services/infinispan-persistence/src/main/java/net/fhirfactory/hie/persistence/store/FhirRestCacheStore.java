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

package net.fhirfactory.hie.persistence.store;

import net.fhirfactory.hie.persistence.client.HapiFhirRestClient;
import net.fhirfactory.hie.persistence.config.FhirStoreConfiguration;
import org.infinispan.commons.configuration.ConfiguredBy;
import org.infinispan.commons.marshall.WrappedByteArray;
import org.infinispan.commons.util.IntSet;
import org.infinispan.persistence.spi.InitializationContext;
import org.infinispan.persistence.spi.MarshallableEntry;
import org.infinispan.persistence.spi.MarshallableEntryFactory;
import org.infinispan.persistence.spi.NonBlockingStore;
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

@ConfiguredBy(FhirStoreConfiguration.class)
public class FhirRestCacheStore<K, V> implements NonBlockingStore<K, V> {

    private static final Logger log = LoggerFactory.getLogger(FhirRestCacheStore.class);

    private InitializationContext ctx;
    private FhirStoreConfiguration configuration;
    private HapiFhirRestClient restClient;
    private String cacheName;

    @Override
    public CompletionStage<Void> start(InitializationContext ctx) {
        this.ctx = ctx;
        this.configuration = ctx.getConfiguration();
        this.cacheName = ctx.getCache().getName();
        String configuredServerUrl = configuration != null
                ? configuration.serverUrl()
                : "http://hapi-fhir-jpa-server-1:8080/fhir";
        String serverUrl = System.getProperty("fhir.server.url", configuredServerUrl);
        int timeout = configuration != null ? configuration.timeoutSeconds() : 10;
        this.restClient = new HapiFhirRestClient(serverUrl, timeout);
        log.info("Initialized FhirRestCacheStore for cache [{}] targeting server [{}]", cacheName, serverUrl);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletionStage<Void> stop() {
        log.info("Stopped FhirRestCacheStore for cache [{}]", cacheName);
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

        ResourceCoordinate coord = resolveCoordinate(key, cacheName);
        log.debug("Cache load triggered for {}/{}", coord.resourceType(), coord.id());

        return restClient.getResourceJson(coord.resourceType(), coord.id())
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
        ResourceCoordinate coord = resolveCoordinate(key, cacheName);
        String jsonPayload = valueToString(val);

        log.info("Write-behind persistence executing for {}/{}", coord.resourceType(), coord.id());
        return restClient.saveResourceJson(coord.resourceType(), coord.id(), jsonPayload)
                .thenAccept(success -> {
                    if (!success) {
                        log.warn("Write-behind failed to persist {}/{}", coord.resourceType(), coord.id());
                    } else {
                        log.info("Write-behind successfully persisted {}/{}", coord.resourceType(), coord.id());
                    }
                })
                .exceptionally(error -> {
                    log.error("Write-behind crashed while persisting {}/{}: {}", coord.resourceType(), coord.id(), error.getMessage(), error);
                    return null;
                });
    }

    @Override
    public CompletionStage<Boolean> delete(int segment, Object key) {
        if (key == null) {
            return CompletableFuture.completedFuture(false);
        }
        ResourceCoordinate coord = resolveCoordinate(key, cacheName);
        log.debug("Write-behind delete executing for {}/{}", coord.resourceType(), coord.id());
        return restClient.deleteResource(coord.resourceType(), coord.id());
    }

    @Override
    public CompletionStage<Boolean> containsKey(int segment, Object key) {
        if (key == null) {
            return CompletableFuture.completedFuture(false);
        }
        ResourceCoordinate coord = resolveCoordinate(key, cacheName);
        return restClient.containsResource(coord.resourceType(), coord.id());
    }

    @Override
    public CompletionStage<Void> clear() {
        // Clear is not propagated to disk FHIR persistence to prevent accidental catastrophic data loss
        log.warn("Cache clear called on cache [{}] - in-memory cleared without wiping FHIR store", cacheName);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletionStage<Long> size(IntSet segments) {
        return CompletableFuture.completedFuture(0L);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Publisher<MarshallableEntry<K, V>> publishEntries(IntSet segments, Predicate<? super K> filter, boolean includeValues) {
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

    @Override
    @SuppressWarnings("unchecked")
    public Publisher<K> publishKeys(IntSet segments, Predicate<? super K> filter) {
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

    public HapiFhirRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(HapiFhirRestClient restClient) {
        this.restClient = restClient;
    }

    public static ResourceCoordinate resolveCoordinate(Object key, String cacheName) {
        String keyStr = valueToString(key);
        if (keyStr.contains("/")) {
            String[] parts = keyStr.split("/", 2);
            return new ResourceCoordinate(parts[0], parts[1]);
        }
        String resType = mapCacheNameToResourceType(cacheName);
        return new ResourceCoordinate(resType, keyStr);
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

    public static String mapCacheNameToResourceType(String cacheName) {
        if (cacheName == null || cacheName.isBlank()) {
            return "Person";
        }
        String normalized = cacheName.toLowerCase().replace("-cache", "").replace("_cache", "").replace("-", "");
        return switch (normalized) {
            case "person" -> "Person";
            case "relatedperson" -> "RelatedPerson";
            case "practitioner" -> "Practitioner";
            case "practitionerrole" -> "PractitionerRole";
            case "organization", "organisation" -> "Organization";
            case "location" -> "Location";
            case "healthcareservice" -> "HealthcareService";
            case "group" -> "Group";
            case "provenance" -> "Provenance";
            case "auditevent" -> "AuditEvent";
            case "consent" -> "Consent";
            case "task" -> "Task";
            case "communication" -> "Communication";
            case "documentreference" -> "DocumentReference";
            default -> Character.toUpperCase(cacheName.charAt(0)) + cacheName.substring(1).replace("-cache", "");
        };
    }

    public record ResourceCoordinate(String resourceType, String id) {}
}
