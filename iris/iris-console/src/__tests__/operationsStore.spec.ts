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

import { describe, it, expect, beforeEach, vi, afterEach } from 'vitest';
import { createPinia, setActivePinia } from 'pinia';
import { useOperationsStore } from '../stores/operationsStore';
import { operationsApi } from '../api/operationsClient';

vi.mock('../api/operationsClient', () => ({
  operationsApi: {
    getSummary: vi.fn(),
    getSubsystems: vi.fn(),
    getSubsystem: vi.fn(),
    getSubsystemInstances: vi.fn(),
    getSubsystemHealth: vi.fn(),
    getSubsystemStatistics: vi.fn(),
    getAlerts: vi.fn(),
    getQueues: vi.fn(),
    getWorkflows: vi.fn()
  }
}));

describe('useOperationsStore', () => {
  let store: any;

  beforeEach(() => {
    setActivePinia(createPinia());
    store = useOperationsStore();
    vi.clearAllMocks();
  });

  afterEach(() => {
    store.stopPolling();
  });

  it('fetchSummary loads summary from API or creates graceful fallback', async () => {
    const mockSummary = {
      platformStatus: 'HEALTHY',
      environment: 'TEST / k8s',
      cluster: 'test-cluster',
      timestamp: 1000,
      totalSubsystems: 9,
      degradedSubsystems: 0,
      criticalAlerts: 0,
      warningAlerts: 1,
      lastRefreshed: 1000
    };
    (operationsApi.getSummary as any).mockResolvedValue(mockSummary);

    await store.fetchSummary();
    expect(store.summary).toEqual(mockSummary);
  });

  it('fetchSubsystems populates 9 canonical subsystems', async () => {
    (operationsApi.getSubsystems as any).mockResolvedValue([]);

    await store.fetchSubsystems();
    expect(store.subsystems.length).toBe(9);
    expect(store.subsystems.map((s: any) => s.id)).toContain('petasos');
    expect(store.subsystems.map((s: any) => s.id)).toContain('energeia');
  });

  it('selectSubsystem coordinates instances, health, and statistics fetches', async () => {
    (operationsApi.getSubsystemInstances as any).mockResolvedValue([
      { instanceId: 'petasos-0', subsystemId: 'petasos', state: 'HEALTHY', ready: true, restartCount: 0 }
    ]);
    (operationsApi.getSubsystemHealth as any).mockResolvedValue({
      subsystemId: 'petasos',
      status: 'HEALTHY',
      failedOperations: 0,
      restartCount: 0,
      dependenciesSummary: '1/1 Healthy',
      dependencies: []
    });
    (operationsApi.getSubsystemStatistics as any).mockResolvedValue({
      events_in: { metricName: 'events_in', points: [{ timestamp: 100, value: 50 }] }
    });

    await store.selectSubsystem('petasos');

    expect(store.selectedSubsystemId).toBe('petasos');
    expect(store.instances.length).toBe(1);
    expect(store.currentHealth?.status).toBe('HEALTHY');
    expect(store.statistics.events_in).toBeDefined();
    expect(store.lastRefreshed).not.toBeNull();
  });

  it('setWindow updates selectedWindow and fetches statistics for that window', async () => {
    store.selectedSubsystemId = 'pylai';
    (operationsApi.getSubsystemStatistics as any).mockResolvedValue({});

    await store.setWindow('24h');
    expect(store.selectedWindow).toBe('24h');
    expect(operationsApi.getSubsystemStatistics).toHaveBeenCalledWith('pylai', '24h');
  });

  it('manages instance detail drawer open and close state', () => {
    const inst = { instanceId: 'mneme-0', subsystemId: 'mneme', state: 'HEALTHY', ready: true, restartCount: 0 };
    
    expect(store.isInstanceDrawerOpen).toBe(false);
    expect(store.selectedInstance).toBeNull();

    store.openInstanceDrawer(inst);
    expect(store.isInstanceDrawerOpen).toBe(true);
    expect(store.selectedInstance).toEqual(inst);

    store.closeInstanceDrawer();
    expect(store.isInstanceDrawerOpen).toBe(false);
    expect(store.selectedInstance).toBeNull();
  });

  it('computes alert counts accurately from summary and alerts list', () => {
    store.summary = {
      criticalAlerts: 2,
      warningAlerts: 3
    } as any;

    expect(store.criticalAlertsCount).toBe(2);
    expect(store.warningAlertsCount).toBe(3);
  });
});
