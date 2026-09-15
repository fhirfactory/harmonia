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

package net.fhirfactory.harmonia.mllpgateway.config;

import jakarta.enterprise.context.ApplicationScoped;
import net.fhirfactory.harmonia.model.topic.Topic;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe registry for managing and resolving outbound MLLP destination endpoints.
 */
@ApplicationScoped
public class MllpDestinationRegistry implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(MllpDestinationRegistry.class);

    private final Map<String, MllpDestinationConfig> destinations = new ConcurrentHashMap<>();
    private volatile String defaultDestinationId;

    public MllpDestinationRegistry() {
    }

    /**
     * Registers or updates a destination configuration.
     *
     * @param config The destination configuration to register
     */
    public void registerDestination(MllpDestinationConfig config) {
        if (config == null || StringUtils.isBlank(config.getDestinationId())) {
            throw new IllegalArgumentException("Destination configuration and destinationId must not be null or blank");
        }
        String idKey = normalizeKey(config.getDestinationId());
        destinations.put(idKey, config);
        LOG.info("Registered MLLP outbound destination: id={}, host={}, port={}, queue={}",
                config.getDestinationId(), config.getHost(), config.getPort(), config.getEffectiveQueueName());

        if (defaultDestinationId == null) {
            defaultDestinationId = config.getDestinationId();
        }
    }

    /**
     * Unregisters a destination by ID.
     *
     * @param destinationId The destination ID to remove
     * @return The removed configuration, or null if not found
     */
    public MllpDestinationConfig unregisterDestination(String destinationId) {
        if (StringUtils.isBlank(destinationId)) {
            return null;
        }
        String idKey = normalizeKey(destinationId);
        MllpDestinationConfig removed = destinations.remove(idKey);
        if (removed != null) {
            LOG.info("Unregistered MLLP outbound destination: id={}", destinationId);
            if (StringUtils.equalsIgnoreCase(defaultDestinationId, destinationId)) {
                defaultDestinationId = destinations.keySet().stream().findFirst().orElse(null);
            }
        }
        return removed;
    }

    /**
     * Retrieves a destination configuration by ID.
     */
    public Optional<MllpDestinationConfig> getDestination(String destinationId) {
        if (StringUtils.isBlank(destinationId)) {
            return Optional.empty();
        }
        return Optional.ofNullable(destinations.get(normalizeKey(destinationId)));
    }

    /**
     * Checks if a destination ID is registered.
     */
    public boolean hasDestination(String destinationId) {
        if (StringUtils.isBlank(destinationId)) {
            return false;
        }
        return destinations.containsKey(normalizeKey(destinationId));
    }

    /**
     * Resolves a destination based on Topic metadata (destination, target, or facility).
     *
     * @param topic Topic metadata
     * @return Resolved destination configuration or default fallback
     */
    public Optional<MllpDestinationConfig> resolveDestination(Topic topic) {
        if (topic == null) {
            return getDefaultDestination();
        }

        // 1. Check topic destination
        if (StringUtils.isNotBlank(topic.getDestination())) {
            Optional<MllpDestinationConfig> byDest = resolveDestination(topic.getDestination());
            if (byDest.isPresent()) {
                return byDest;
            }
        }

        // 2. Check topic target
        if (StringUtils.isNotBlank(topic.getTarget())) {
            Optional<MllpDestinationConfig> byTarget = resolveDestination(topic.getTarget());
            if (byTarget.isPresent()) {
                return byTarget;
            }
        }

        // 3. Check topic origin / facility
        if (StringUtils.isNotBlank(topic.getOrigin())) {
            Optional<MllpDestinationConfig> byOrigin = resolveDestination(topic.getOrigin());
            if (byOrigin.isPresent()) {
                return byOrigin;
            }
        }

        return getDefaultDestination();
    }

    /**
     * Resolves a destination by identifier, facility code, or name.
     *
     * @param identifier ID, facility, or alias
     * @return Resolved destination configuration
     */
    public Optional<MllpDestinationConfig> resolveDestination(String identifier) {
        if (StringUtils.isBlank(identifier)) {
            return Optional.empty();
        }

        String key = normalizeKey(identifier);

        // Direct ID lookup
        MllpDestinationConfig config = destinations.get(key);
        if (config != null && config.isEnabled()) {
            return Optional.of(config);
        }

        // Lookup by facility code or name
        for (MllpDestinationConfig c : destinations.values()) {
            if (!c.isEnabled()) {
                continue;
            }
            if (StringUtils.equalsIgnoreCase(c.getFacility(), identifier)
                    || StringUtils.equalsIgnoreCase(c.getName(), identifier)) {
                return Optional.of(c);
            }
        }

        return Optional.empty();
    }

    /**
     * Resolves the configured default destination, or the first available enabled destination.
     */
    public Optional<MllpDestinationConfig> getDefaultDestination() {
        if (defaultDestinationId != null) {
            MllpDestinationConfig config = destinations.get(normalizeKey(defaultDestinationId));
            if (config != null && config.isEnabled()) {
                return Optional.of(config);
            }
        }
        return destinations.values().stream()
                .filter(MllpDestinationConfig::isEnabled)
                .findFirst();
    }

    /**
     * Sets the default destination ID.
     */
    public void setDefaultDestinationId(String defaultDestinationId) {
        this.defaultDestinationId = defaultDestinationId;
    }

    /**
     * Gets all registered destination configurations.
     */
    public List<MllpDestinationConfig> getAllDestinations() {
        return new ArrayList<>(destinations.values());
    }

    /**
     * Returns the total count of registered destinations.
     */
    public int size() {
        return destinations.size();
    }

    /**
     * Clears all registered destinations.
     */
    public void clear() {
        destinations.clear();
        defaultDestinationId = null;
    }

    private String normalizeKey(String key) {
        return key.trim().toLowerCase();
    }
}
