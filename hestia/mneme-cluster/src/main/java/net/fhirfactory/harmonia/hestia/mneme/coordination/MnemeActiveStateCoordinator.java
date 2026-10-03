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

package net.fhirfactory.harmonia.hestia.mneme.coordination;

import net.fhirfactory.harmonia.model.governedwrite.ActiveStateCoordinator;
import net.fhirfactory.harmonia.model.governedwrite.ActiveStateToken;
import net.fhirfactory.harmonia.model.governedwrite.ResourceKey;

/**
 * Mneme-side extension of {@link ActiveStateCoordinator} providing Active Hegemon observation
 * and transactional pre-persistence Hegemony acquisition/confirmation.
 */
public interface MnemeActiveStateCoordinator extends ActiveStateCoordinator {

    /**
     * Cheaply and observationally evaluates whether the supplied {@code instanceId} is the Active Hegemon
     * for the given {@code key}.
     * <p>
     * Strictly observational: does not mutate cache, advance token/generation, or register instances.
     *
     * @param key the resource key
     * @param instanceId the instance identity to check
     * @return {@link ResourceHegemonStatus#NO_RESOURCE_IS_HEGEMON}, {@link ResourceHegemonStatus#RESOURCE_IS_HEGEMON},
     *         or {@link ResourceHegemonStatus#RESOURCE_IS_NOT_HEGEMON}
     */
    ResourceHegemonStatus checkHegemonStatus(ResourceKey key, InstanceId instanceId);

    /**
     * Transactionally acquires or confirms Active Hegemony for {@code instanceId} before persistence.
     * <p>
     * - If currently NO_HEGEMON: atomically transitions NO_HEGEMON -> HEGEMON:instanceId via CAS, advancing token generation.
     * - If currently HEGEMON:instanceId: read-validates currency without writing cache or advancing token generation.
     * - If currently HEGEMON:other: rejects with {@link HegemonyCoordinationResult.NotHegemon}.
     * - If token is stale: rejects with {@link HegemonyCoordinationResult.Stale}.
     * - If coordination entry is corrupt: throws {@link ActiveCoordinationCorruptException}.
     *
     * @param key the resource key
     * @param instanceId the submitting instance identity
     * @param observedToken the observed active-state token
     * @return the coordination result
     */
    HegemonyCoordinationResult acquireOrConfirmHegemony(ResourceKey key, InstanceId instanceId, ActiveStateToken observedToken);
}
