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
import { createPinia, setActivePinia } from 'pinia';
import { useWorkflowStore } from '../stores/workflowStore';
import { operationsApi } from '../api/operationsClient';
import type { WorkflowSummary, PragmaSummary } from '../models/operations';

vi.mock('../api/operationsClient', () => ({
  operationsApi: {
    getWorkflows: vi.fn(),
    getWorkflow: vi.fn(),
    getWorkflowPragmas: vi.fn(),
    getPragma: vi.fn()
  }
}));

describe('useWorkflowStore', () => {
  let store: any;

  beforeEach(() => {
    setActivePinia(createPinia());
    store = useWorkflowStore();
    vi.clearAllMocks();
  });

  it('initializes with default values and handles empty workflows telemetry honestly', async () => {
    (operationsApi.getWorkflows as any).mockResolvedValue([]);
    await store.fetchWorkflows();

    expect(store.workflows.length).toBe(0);
    expect(store.totalWorkflows).toBe(0);
    expect(store.selectedWorkflow).toBeNull();
    expect(store.isPragmaDrawerOpen).toBe(false);
    expect(store.error).toBeNull();
  });

  it('populates workflows when operationsApi returns workflow telemetry', async () => {
    const mockData: WorkflowSummary[] = [
      {
        workflowId: 'seq-pid',
        name: 'Patient Identity Pipeline',
        activeExecutions: 1,
        queuedWork: 0,
        completedWork: 10,
        failedWork: 0,
        retryingWork: 0,
        processingRate: 5.0,
        p95DurationMs: 40,
        failureRate: 0.0
      }
    ];
    (operationsApi.getWorkflows as any).mockResolvedValue(mockData);
    await store.fetchWorkflows();

    expect(store.workflows.length).toBe(1);
    expect(store.totalWorkflows).toBe(1);
    expect(store.totalActiveExecutions).toBe(1);
  });

  it('computes total active, queued, completed, and failed work counts', () => {
    store.workflows = [
      {
        workflowId: 'w1',
        name: 'Workflow 1',
        activeExecutions: 2,
        queuedWork: 1,
        completedWork: 100,
        failedWork: 1,
        retryingWork: 0,
        processingRate: 10.0,
        p95DurationMs: 40,
        failureRate: 0.0
      },
      {
        workflowId: 'w2',
        name: 'Workflow 2',
        activeExecutions: 3,
        queuedWork: 0,
        completedWork: 200,
        failedWork: 0,
        retryingWork: 1,
        processingRate: 15.0,
        p95DurationMs: 50,
        failureRate: 0.1
      }
    ] as WorkflowSummary[];

    expect(store.totalActiveExecutions).toBe(5);
    expect(store.totalQueuedWork).toBe(1);
    expect(store.totalCompletedWork).toBe(300);
    expect(store.totalFailedWork).toBe(1);
    expect(store.totalRetryingWork).toBe(1);
    expect(store.totalProcessingRate).toBe(25.0);
    expect(store.p95Duration).toBe(50);
  });

  it('selects workflow and loads associated pragmas', () => {
    store.workflows = [
      { workflowId: 'seq-pid', name: 'Patient Identity Pipeline', failureRate: 0.0 }
    ] as WorkflowSummary[];

    store.workflowPragmas = {
      'seq-pid': [
        { pragmaId: 'pragma-01', praxisId: 'seq-pid', status: 'RUNNING' }
      ]
    };

    store.selectWorkflow('seq-pid');
    expect(store.selectedWorkflow.workflowId).toBe('seq-pid');
    expect(store.currentWorkflowPragmas.length).toBe(1);
    expect(store.currentWorkflowPragmas[0].pragmaId).toBe('pragma-01');

    store.clearSelectedWorkflow();
    expect(store.selectedWorkflow).toBeNull();
    expect(store.currentWorkflowPragmas.length).toBe(0);
  });

  it('opens and closes pragma detail drawer', async () => {
    const pragma: PragmaSummary = {
      pragmaId: 'pragma-99',
      praxisId: 'seq-pid',
      status: 'COMPLETED',
      startedAt: Date.now(),
      durationMs: 45,
      completedErgaCount: 3,
      retryCount: 0
    };

    await store.openPragmaDrawer(pragma);
    expect(store.isPragmaDrawerOpen).toBe(true);
    expect(store.selectedPragma).toEqual(pragma);

    store.closePragmaDrawer();
    expect(store.isPragmaDrawerOpen).toBe(false);
    expect(store.selectedPragma).toBeNull();
  });
});
