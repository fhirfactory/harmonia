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

package net.fhirfactory.harmonia.praxis.cache;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Service that checks and verifies that the Infinispan cluster and underlying JPA storage servers
 * are up, stable, and ready to accept client connections.
 */
@ApplicationScoped
public class ClusterReadinessService {

    private static final Logger log = LoggerFactory.getLogger(ClusterReadinessService.class);

    @Inject
    private RemoteCacheManager remoteCacheManager;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(3))
            .version(HttpClient.Version.HTTP_1_1)
            .build();

    public ClusterReadinessService() {
    }

    public ClusterReadinessService(RemoteCacheManager remoteCacheManager) {
        this.remoteCacheManager = remoteCacheManager;
    }

    /**
     * Checks whether the Infinispan Hot Rod client is connected and active.
     */
    public boolean isInfinispanReady() {
        if (remoteCacheManager == null) {
            return false;
        }
        try {
            if (!remoteCacheManager.isStarted()) {
                remoteCacheManager.start();
            }
            return remoteCacheManager.isStarted();
        } catch (Exception e) {
            log.debug("Infinispan Hot Rod client is not ready: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Checks if a specific Infinispan cache is accessible and ready.
     */
    public boolean isCacheAvailable(String cacheName) {
        if (!isInfinispanReady()) {
            return false;
        }
        try {
            RemoteCache<?, ?> cache = remoteCacheManager.getCache(cacheName);
            return cache != null;
        } catch (Exception e) {
            log.debug("Cache [{}] is not available: {}", cacheName, e.getMessage());
            return false;
        }
    }

    /**
     * Checks if the external Operations JPA server is ready to accept connections.
     */
    public boolean isOperationsServerReady(String serverUrl) {
        if (serverUrl == null || serverUrl.isBlank()) {
            return true;
        }
        try {
            String base = serverUrl.replaceAll("/api/operations/?$", "").replaceAll("/operations/?$", "");
            URI uri = URI.create(base + "/api/operations/ready");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Accept", "application/json")
                    .timeout(Duration.ofSeconds(2))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() >= 200 && response.statusCode() < 300;
        } catch (Exception e) {
            log.debug("Operations JPA server at {} is not reachable: {}", serverUrl, e.getMessage());
            return false;
        }
    }

    /**
     * Synchronously waits for the Infinispan framework and target cache to become ready.
     *
     * @param cacheName      the cache to verify
     * @param maxWaitSeconds maximum duration to wait
     * @return true if ready, false if timeout elapsed
     */
    public boolean waitForCacheReady(String cacheName, int maxWaitSeconds) {
        long deadline = System.currentTimeMillis() + (maxWaitSeconds * 1000L);
        while (System.currentTimeMillis() < deadline) {
            if (isCacheAvailable(cacheName)) {
                log.info("Infinispan cache [{}] is verified up and stable.", cacheName);
                return true;
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        log.warn("Timed out waiting for Infinispan cache [{}] to become ready (waited {}s).", cacheName, maxWaitSeconds);
        return isCacheAvailable(cacheName);
    }
}
