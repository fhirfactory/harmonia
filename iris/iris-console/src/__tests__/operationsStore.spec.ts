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
import { useOperationsStore, getDefaultSubsystems } from '../stores/operationsStore';
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
    expect(store.summaryState.kind).toBe('loaded');
  });

  it('flags the summary slice unavailable without discarding the last known value', async () => {
    const mockSummary = { platformStatus: 'HEALTHY' } as any;
    (operationsApi.getSummary as any).mockResolvedValue(mockSummary);
    await store.fetchSummary();

    (operationsApi.getSummary as any).mockRejectedValue(new Error('ECONNREFUSED'));
    await store.fetchSummary();

    expect(store.summary).toEqual(mockSummary);
    expect(store.summaryState.kind).toBe('unavailable');
    expect(store.summaryState.message).toContain('Operations API unavailable');
    expect(store.isOperationsApiUnavailable).toBe(true);
  });

  it('distinguishes an empty alerts result set from an unreachable operations API', async () => {
    (operationsApi.getAlerts as any).mockResolvedValue([]);
    await store.fetchAlerts();
    expect(store.alertsState.kind).toBe('empty');
    expect(store.isOperationsApiUnavailable).toBe(false);

    (operationsApi.getAlerts as any).mockRejectedValue(new Error('Network Error'));
    await store.fetchAlerts();
    expect(store.alertsState.kind).toBe('unavailable');
    expect(store.isOperationsApiUnavailable).toBe(true);
  });

  it('never exposes a stack trace in a recorded load state message', async () => {
    const err = new Error('Request failed');
    err.stack = 'Error: Request failed\n    at somewhere (file.ts:1:1)';
    (operationsApi.getSubsystemInstances as any).mockRejectedValue(err);

    await store.fetchInstances('petasos');

    expect(store.instancesState.kind).toBe('unavailable');
    expect(store.instancesState.message).not.toContain('at somewhere');
  });

  it('does not populate a fallback subsystem tree on empty or unavailable API responses', async () => {
    (operationsApi.getSubsystems as any).mockResolvedValue([]);

    await store.fetchSubsystems();
    expect(store.subsystems.length).toBe(0);
    expect(store.subsystemsAreFallback).toBe(false);
    expect(store.subsystemsState.kind).toBe('empty');

    (operationsApi.getSubsystems as any).mockRejectedValue(new Error('Network Error'));
    await store.fetchSubsystems();
    expect(store.subsystems.length).toBe(0);
    expect(store.subsystemsAreFallback).toBe(false);
    expect(store.subsystemsState.kind).toBe('unavailable');
    expect(store.isOperationsApiUnavailable).toBe(true);
  });

  it('retains getDefaultSubsystems explicitly for tests and fixtures without stale iris-monitor', () => {
    const demo = getDefaultSubsystems();
    expect(demo.length).toBe(9);
    expect(demo.map((s: any) => s.id)).toContain('petasos');
    expect(demo.map((s: any) => s.id)).toContain('energeia');

    const iris = demo.find((s: any) => s.id === 'iris');
    const childIds = (iris?.children || []).map((c: any) => c.id);
    expect(childIds).not.toContain('iris-monitor');
    expect(childIds).toContain('iris-befe');
  });

  it('allows explicit loading of demo subsystems for fixtures/demonstrations', () => {
    store.loadDemoSubsystems();
    expect(store.subsystems.length).toBe(9);
    expect(store.subsystemsAreFallback).toBe(true);
    expect(store.subsystemsState.kind).toBe('loaded');
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
