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
import { flushPromises, mount } from '@vue/test-utils';
import { createPinia, setActivePinia } from 'pinia';
import { useWorkflowStore } from '../stores/workflowStore';
import { operationsApi } from '../api/operationsClient';
import WorkflowTable from '../components/workflows/WorkflowTable.vue';
import PragmaDetailDrawer from '../components/workflows/PragmaDetailDrawer.vue';
import WorkflowsView from '../views/WorkflowsView.vue';
import type { WorkflowSummary, PragmaSummary } from '../models/operations';

vi.mock('../api/operationsClient', () => ({
  operationsApi: {
    getWorkflows: vi.fn(),
    getWorkflow: vi.fn(),
    getWorkflowPragmas: vi.fn(),
    getPragma: vi.fn()
  }
}));

describe('Workflows Perspective Components', () => {
  let store: any;

  const mockWorkflows: WorkflowSummary[] = [
    {
      workflowId: 'seq-patient-identity-pipeline',
      name: 'Patient Identity Update Sequence',
      description: 'Extracts, normalizes, and updates patient identity across all clinical messages',
      activeExecutions: 2,
      queuedWork: 1,
      completedWork: 1420,
      failedWork: 0,
      retryingWork: 0,
      processingRate: 12.4,
      p95DurationMs: 48,
      failureRate: 0.0
    },
    {
      workflowId: 'seq-adt-fanout-pipeline',
      name: 'ADT Demographics Fan-Out Sequence',
      description: 'Multi-destination ADT notification pipeline distributing demographics',
      activeExecutions: 1,
      queuedWork: 0,
      completedWork: 890,
      failedWork: 1,
      retryingWork: 1,
      processingRate: 8.6,
      p95DurationMs: 65,
      failureRate: 0.1
    }
  ];

  const mockPragmas: Record<string, PragmaSummary[]> = {
    'seq-patient-identity-pipeline': [
      {
        pragmaId: 'pragma-pid-8821',
        praxisId: 'seq-patient-identity-pipeline',
        status: 'RUNNING',
        startedAt: 1700000000000,
        durationMs: 32000,
        currentErgon: 'PatientResolutionErgon',
        completedErgaCount: 1,
        retryCount: 0,
        correlationId: 'corr-8821-4a9f',
        causationId: 'msg-pylai-1102',
        checkpoints: [
          {
            checkpointId: 'cp-1',
            ergonId: 'IngressValidationErgon',
            ergonName: 'Ingress Validation & Security Tagging',
            status: 'COMPLETED',
            startedAt: 1700000000000,
            completedAt: 1700000000012,
            durationMs: 12,
            detail: 'Verified Themis token and HL7 MSH structure'
          },
          {
            checkpointId: 'cp-2',
            ergonId: 'PatientResolutionErgon',
            ergonName: 'Master Patient Index Lookup & Resolution',
            status: 'RUNNING',
            startedAt: 1700000000012,
            durationMs: 31988,
            detail: 'Executing MPI query in Mneme cluster'
          }
        ]
      }
    ]
  };

  beforeEach(() => {
    setActivePinia(createPinia());
    store = useWorkflowStore();
    vi.clearAllMocks();
    (operationsApi.getWorkflows as any).mockResolvedValue([]);
    (operationsApi.getWorkflowPragmas as any).mockResolvedValue([]);
  });

  describe('WorkflowTable.vue', () => {
    it('renders workflows list with active, queued, completed, failed, rate, and P95 latency', () => {
      const wrapper = mount(WorkflowTable, {
        props: {
          workflows: mockWorkflows,
          loading: false
        }
      });

      const text = wrapper.text();
      expect(text).toContain('Patient Identity Update Sequence');
      expect(text).toContain('seq-patient-identity-pipeline');
      expect(text).toContain('1,420'); // completed
      expect(text).toContain('12.4 /s'); // rate
      expect(text).toContain('48ms'); // duration

      expect(text).toContain('ADT Demographics Fan-Out Sequence');
      expect(text).toContain('65ms');
    });

    it('emits select event when drill down button or row is clicked', async () => {
      const wrapper = mount(WorkflowTable, {
        props: {
          workflows: mockWorkflows,
          loading: false
        }
      });

      const drillDownBtn = wrapper.findAll('button').find(b => b.text().includes('Drill Down'));
      expect(drillDownBtn).toBeDefined();

      await drillDownBtn!.trigger('click');
      expect(wrapper.emitted('select')).toBeTruthy();
      expect(wrapper.emitted('select')![0][0]).toEqual(mockWorkflows[0]);
    });

    it('renders empty state when workflows list is empty', () => {
      const wrapper = mount(WorkflowTable, {
        props: {
          workflows: [],
          loading: false
        }
      });

      expect(wrapper.text()).toContain('No workflows found');
    });
  });

  describe('PragmaDetailDrawer.vue', () => {
    it('renders execution metadata, correlation IDs, and checkpoint progression timeline', () => {
      const wrapper = mount(PragmaDetailDrawer, {
        props: {
          pragma: mockPragmas['seq-patient-identity-pipeline'][0],
          isOpen: true
        }
      });

      const text = wrapper.text();
      expect(text).toContain('pragma-pid-8821');
      expect(text).toContain('RUNNING');
      expect(text).toContain('corr-8821-4a9f');
      expect(text).toContain('msg-pylai-1102');
      expect(text).toContain('PatientResolutionErgon');
      expect(text).toContain('Ingress Validation & Security Tagging');
      expect(text).toContain('Master Patient Index Lookup & Resolution');
      expect(text).toContain('Zero-PHI Safe Telemetry');
    });

    it('emits close event when close button is clicked', async () => {
      const wrapper = mount(PragmaDetailDrawer, {
        props: {
          pragma: mockPragmas['seq-patient-identity-pipeline'][0],
          isOpen: true
        }
      });

      const closeBtn = wrapper.find('button[aria-label="Close pragma details drawer"]');
      expect(closeBtn.exists()).toBe(true);

      await closeBtn.trigger('click');
      expect(wrapper.emitted('close')).toBeTruthy();
    });

    it('emits close event when Escape key is pressed', async () => {
      const wrapper = mount(PragmaDetailDrawer, {
        props: {
          pragma: mockPragmas['seq-patient-identity-pipeline'][0],
          isOpen: true
        }
      });

      window.dispatchEvent(new KeyboardEvent('keydown', { key: 'Escape' }));
      expect(wrapper.emitted('close')).toBeTruthy();
    });
  });

  describe('WorkflowsView.vue', () => {
    it('renders summary cards with active, queued, completed, failed, and throughput rate', async () => {
      (operationsApi.getWorkflows as any).mockResolvedValue(mockWorkflows);
      const wrapper = mount(WorkflowsView);
      await flushPromises();

      const text = wrapper.text();
      expect(text).toContain('Active');
      expect(text).toContain('3'); // 2 + 1
      expect(text).toContain('Queued');
      expect(text).toContain('1');
      expect(text).toContain('Completed');
      expect(text).toContain('2,310'); // 1420 + 890
      expect(text).toContain('Failed');
      expect(text).toContain('1');
      expect(text).toContain('Throughput');
      expect(text).toContain('21.0 /s');
    });

    it('filters workflows by search input', async () => {
      (operationsApi.getWorkflows as any).mockResolvedValue(mockWorkflows);
      const wrapper = mount(WorkflowsView);
      await flushPromises();

      const input = wrapper.find('input[type="text"]');
      await input.setValue('fanout');

      expect(store.searchQuery).toBe('fanout');
      expect(wrapper.text()).toContain('ADT Demographics Fan-Out Sequence');
      expect(wrapper.text()).not.toContain('Patient Identity Update Sequence');
    });

    it('supports drill-down into workflow to inspect Pragmas and returns via breadcrumbs', async () => {
      (operationsApi.getWorkflows as any).mockResolvedValue(mockWorkflows);
      (operationsApi.getWorkflowPragmas as any).mockResolvedValue(
        mockPragmas['seq-patient-identity-pipeline']
      );
      const wrapper = mount(WorkflowsView);
      await flushPromises();

      // Level 1: Click drill down on first workflow
      const drillDownBtn = wrapper.findAll('button').find(b => b.text().includes('Drill Down'));
      expect(drillDownBtn).toBeDefined();
      await drillDownBtn!.trigger('click');
      await flushPromises();

      // Now at Level 2: Selected workflow details and Pragmas list shown
      expect(wrapper.text()).toContain('Pragma Execution Envelopes');
      expect(wrapper.text()).toContain('pragma-pid-8821');
      expect(wrapper.text()).toContain('PatientResolutionErgon');

      // Return via breadcrumb "All Workflows"
      const backBtn = wrapper.findAll('button').find(b => b.text().includes('All Workflows'));
      expect(backBtn).toBeDefined();
      await backBtn!.trigger('click');

      // Now back at Level 1
      expect(store.selectedWorkflow).toBeNull();
      expect(wrapper.text()).toContain('Configured Praxis Workflow Sequences');
    });
  });
});
