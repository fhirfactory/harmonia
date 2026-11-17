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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package net.fhirfactory.harmonia.model.core.information.operations.factory;

import net.fhirfactory.harmonia.model.core.information.operations.ManagementContext;
import net.fhirfactory.harmonia.model.core.information.operations.OperationalRevision;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Assembles a typed {@link ManagementContext} from its independent optional operational facts.
 * <p>
 * This builder does not create an Information Unit or infer authoritative persistence state.
 */
public final class ManagementContextBuilder {

    private OperationalRevision operationalRevision;
    private String activeGeneration;
    private Instant cachedAt;
    private final Map<String, String> transientProperties = new LinkedHashMap<>();

    /**
     * Sets the Mneme operational revision.
     *
     * @param operationalRevision cache conflict-detection revision
     * @return this builder
     */
    public ManagementContextBuilder operationalRevision(OperationalRevision operationalRevision) {
        this.operationalRevision = operationalRevision;
        return this;
    }

    /**
     * Sets the active cache manifestation identifier.
     *
     * @param activeGeneration identifier, or null when no active manifestation exists
     * @return this builder
     */
    public ManagementContextBuilder activeGeneration(String activeGeneration) {
        this.activeGeneration = activeGeneration;
        return this;
    }

    /**
     * Sets the time at which this working manifestation was cached.
     *
     * @param cachedAt cache timestamp
     * @return this builder
     */
    public ManagementContextBuilder cachedAt(Instant cachedAt) {
        this.cachedAt = cachedAt;
        return this;
    }

    /**
     * Replaces the transient operational properties.
     *
     * @param properties replacement map; null clears the properties
     * @return this builder
     */
    public ManagementContextBuilder transientProperties(Map<String, String> properties) {
        transientProperties.clear();
        if (properties != null) {
            transientProperties.putAll(properties);
        }
        return this;
    }

    /**
     * Adds or removes one transient property.
     *
     * @param key property name; null is ignored
     * @param value value, or null to remove the named property
     * @return this builder
     */
    public ManagementContextBuilder transientProperty(String key, String value) {
        if (key != null) {
            if (value == null) {
                transientProperties.remove(key);
            } else {
                transientProperties.put(key, value);
            }
        }
        return this;
    }

    /**
     * Creates an immutable management context from the supplied working facts.
     *
     * @return a validated ManagementContext
     */
    public ManagementContext build() {
        return new ManagementContext(operationalRevision, activeGeneration, cachedAt, transientProperties);
    }
}