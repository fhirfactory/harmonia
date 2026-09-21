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

import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import type { OperationalHealth, OperationalInstance } from '../models/operations';
import type { ConfiguredInterface, InterfaceRuntimeObservation } from '../models/interfaces';
import { CONFIGURED_INTERFACES, CONFIGURED_INVENTORY_NOTICE } from '../config/configuredInterfaces';
import { operationsApi } from '../api/operationsClient';
import {
  IDLE_STATE,
  describeFailure,
  empty as emptyState,
  loaded as loadedState,
  loading as loadingState,
  partial as partialState,
  unavailable as unavailableState,
  type LoadState
} from '../models/loadState';

const RUNTIME_SUBSYSTEM_ID = 'pylai';

/**
 * Interfaces store.
 *
 * Holds two strictly separated bodies of knowledge:
 *   - the CONFIGURED gateway inventory, declared in `configuredInterfaces.ts`;
 *   - the OBSERVED Pylai runtime facts the operations API actually returned.
 *
 * The two are never merged into a single record, so a view cannot accidentally
 * present configuration as measurement. When a runtime interfaces API arrives,
 * only `fetchRuntime` changes.
 */
export const useInterfacesStore = defineStore('interfaces', () => {
  // Configured inventory ------------------------------------------------------
  const configured = ref<ConfiguredInterface[]>([...CONFIGURED_INTERFACES]);
  const provenanceNotice = CONFIGURED_INVENTORY_NOTICE;

  // Observed runtime ----------------------------------------------------------
  const instances = ref<OperationalInstance[]>([]);
  const health = ref<OperationalHealth | null>(null);
  const runtimeState = ref<LoadState>(IDLE_STATE);
  const lastRefreshed = ref<Date | null>(null);

  // Filters -------------------------------------------------------------------
  const searchQuery = ref<string>('');
  const directionFilter = ref<'ALL' | 'INBOUND' | 'OUTBOUND'>('ALL');

  const configuredCount = computed(() => configured.value.length);
  const inboundCount = computed(() => configured.value.filter(i => i.direction === 'INBOUND').length);
  const outboundCount = computed(() => configured.value.filter(i => i.direction === 'OUTBOUND').length);

  const filteredInterfaces = computed<ConfiguredInterface[]>(() => {
    const q = searchQuery.value.trim().toLowerCase();
    return configured.value.filter(item => {
      if (directionFilter.value !== 'ALL' && item.direction !== directionFilter.value) {
        return false;
      }
      if (!q) {
        return true;
      }
      return (
        item.name.toLowerCase().includes(q) ||
        item.id.toLowerCase().includes(q) ||
        item.englishTitle.toLowerCase().includes(q) ||
        item.description.toLowerCase().includes(q) ||
        item.protocol.toLowerCase().includes(q) ||
        item.targetQueue.toLowerCase().includes(q) ||
        String(item.port).includes(q)
      );
    });
  });

  /**
   * Genuinely observed Pylai facts only. An empty array means the operations
   * API reported no instances; it never implies a configured gateway is up.
   */
  const runtimeObservations = computed<InterfaceRuntimeObservation[]>(() => {
    return instances.value.map(instance => ({
      subsystemId: RUNTIME_SUBSYSTEM_ID,
      instanceState: instance.state ?? null,
      lastUpdated: instance.startedAt ? new Date(instance.startedAt).toISOString() : null,
      provenance: 'OBSERVED'
    }));
  });

  const isRuntimeMeasured = computed(() => runtimeState.value.kind === 'loaded');
  const isRuntimeUnavailable = computed(() => runtimeState.value.kind === 'unavailable');

  async function fetchRuntime() {
    runtimeState.value = loadingState();

    const [instancesResult, healthResult] = await Promise.allSettled([
      operationsApi.getSubsystemInstances(RUNTIME_SUBSYSTEM_ID),
      operationsApi.getSubsystemHealth(RUNTIME_SUBSYSTEM_ID)
    ]);

    const failures: string[] = [];

    if (instancesResult.status === 'fulfilled') {
      instances.value = Array.isArray(instancesResult.value) ? instancesResult.value : [];
    } else {
      failures.push(describeFailure(instancesResult.reason, 'Pylai instances'));
    }

    if (healthResult.status === 'fulfilled') {
      health.value = healthResult.value ?? null;
    } else {
      failures.push(describeFailure(healthResult.reason, 'Pylai health'));
    }

    const now = new Date();
    if (failures.length === 2) {
      runtimeState.value = unavailableState(failures[0], now);
    } else if (failures.length === 1) {
      runtimeState.value = partialState(failures[0], now);
    } else if (instances.value.length === 0 && !health.value) {
      runtimeState.value = emptyState(now);
    } else {
      runtimeState.value = loadedState(now);
    }
    lastRefreshed.value = now;
  }

  function setSearchQuery(query: string) {
    searchQuery.value = query;
  }

  function setDirectionFilter(filter: 'ALL' | 'INBOUND' | 'OUTBOUND') {
    directionFilter.value = filter;
  }

  return {
    configured,
    provenanceNotice,
    instances,
    health,
    runtimeState,
    runtimeObservations,
    isRuntimeMeasured,
    isRuntimeUnavailable,
    lastRefreshed,
    searchQuery,
    directionFilter,
    configuredCount,
    inboundCount,
    outboundCount,
    filteredInterfaces,
    fetchRuntime,
    setSearchQuery,
    setDirectionFilter
  };
});
