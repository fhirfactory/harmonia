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

import { describe, it, expect, beforeEach, vi } from 'vitest';
// Raw module text, so the removed fabricated fields cannot quietly reappear.
import CONFIG_SOURCE from '../config/configuredInterfaces.ts?raw';
import { createPinia, setActivePinia } from 'pinia';
import { CONFIGURED_INTERFACES, CONFIGURED_INVENTORY_NOTICE } from '../config/configuredInterfaces';
import { useInterfacesStore } from '../stores/interfacesStore';
import { operationsApi } from '../api/operationsClient';

vi.mock('../api/operationsClient', () => ({
  operationsApi: {
    getSubsystemInstances: vi.fn(),
    getSubsystemHealth: vi.fn()
  }
}));

/** Field names that would imply a runtime measurement Harmonia does not take. */
const RUNTIME_SOUNDING_FIELDS = [
  'activeListeners',
  'currentConnections',
  'throughput',
  'messageRate',
  'errorCount',
  'errorState',
  'errorRate',
  'bytesIn',
  'bytesOut',
  'uptime',
  'lastMessageAt'
];


describe('Interface data provenance', () => {
  beforeEach(() => {
    setActivePinia(createPinia());
    vi.clearAllMocks();
  });

  it('marks every configured interface as CONFIGURED', () => {
    expect(CONFIGURED_INTERFACES.length).toBeGreaterThan(0);
    for (const item of CONFIGURED_INTERFACES) {
      expect(item.provenance).toBe('CONFIGURED');
    }
  });

  it('carries no runtime-sounding field on any configured record', () => {
    for (const item of CONFIGURED_INTERFACES) {
      for (const field of RUNTIME_SOUNDING_FIELDS) {
        expect(Object.prototype.hasOwnProperty.call(item, field)).toBe(false);
      }
    }
  });

  it('does not mention the removed fabricated fields anywhere in the configuration module', () => {
    expect(CONFIG_SOURCE).not.toMatch(/activeListeners\s*:/);
    expect(CONFIG_SOURCE).not.toMatch(/currentConnections\s*:/);
    expect(CONFIG_SOURCE).not.toMatch(/errorState\s*:/);
  });

  it('states plainly that the inventory is configured rather than discovered', () => {
    expect(CONFIGURED_INVENTORY_NOTICE).toContain('not discovered at runtime');
  });

  it('keeps observed runtime facts separate and sourced only from the operations API', async () => {
    (operationsApi.getSubsystemInstances as any).mockResolvedValue([
      { instanceId: 'pylai-0', subsystemId: 'pylai', state: 'HEALTHY', ready: true, restartCount: 0 }
    ]);
    (operationsApi.getSubsystemHealth as any).mockResolvedValue({
      subsystemId: 'pylai',
      status: 'HEALTHY',
      failedOperations: 0,
      restartCount: 0
    });

    const store = useInterfacesStore();
    await store.fetchRuntime();

    expect(store.runtimeState.kind).toBe('loaded');
    expect(store.runtimeObservations).toHaveLength(1);
    expect(store.runtimeObservations[0].provenance).toBe('OBSERVED');
    expect(store.runtimeObservations[0].instanceState).toBe('HEALTHY');
    // Configured records are never mutated by observation.
    expect(store.configured.every(i => i.provenance === 'CONFIGURED')).toBe(true);
  });

  it('reports the runtime section unavailable when the operations API cannot be reached', async () => {
    (operationsApi.getSubsystemInstances as any).mockRejectedValue(new Error('ECONNREFUSED'));
    (operationsApi.getSubsystemHealth as any).mockRejectedValue(new Error('ECONNREFUSED'));

    const store = useInterfacesStore();
    await store.fetchRuntime();

    expect(store.runtimeState.kind).toBe('unavailable');
    expect(store.isRuntimeUnavailable).toBe(true);
    expect(store.isRuntimeMeasured).toBe(false);
    // The configured inventory remains fully available and clearly configured.
    expect(store.configuredCount).toBe(CONFIGURED_INTERFACES.length);
  });

  it('reports a partial runtime state when only one operations call fails', async () => {
    (operationsApi.getSubsystemInstances as any).mockResolvedValue([]);
    (operationsApi.getSubsystemHealth as any).mockRejectedValue(new Error('timeout'));

    const store = useInterfacesStore();
    await store.fetchRuntime();

    expect(store.runtimeState.kind).toBe('partial');
  });

  it('reports an empty runtime state when the API returns nothing', async () => {
    (operationsApi.getSubsystemInstances as any).mockResolvedValue([]);
    (operationsApi.getSubsystemHealth as any).mockResolvedValue(null);

    const store = useInterfacesStore();
    await store.fetchRuntime();

    expect(store.runtimeState.kind).toBe('empty');
    expect(store.runtimeObservations).toHaveLength(0);
  });
});
