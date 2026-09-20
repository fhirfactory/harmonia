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

import { describe, it, expect, beforeEach, afterEach, vi } from 'vitest';
import { flushPromises, mount } from '@vue/test-utils';
import { createPinia, setActivePinia } from 'pinia';
import OverviewView from '../views/OverviewView.vue';
import { operationsApi } from '../api/operationsClient';

vi.mock('../api/operationsClient', () => ({
  operationsApi: {
    getSystemStatus: vi.fn(),
    getSubsystems: vi.fn(),
    getAlerts: vi.fn(),
    getQueues: vi.fn(),
    getWorkflows: vi.fn()
  }
}));

const RouterLinkStub = {
  template: '<a><slot /></a>'
};

describe('Overview perspective telemetry and visual layout', () => {
  let wrapper: ReturnType<typeof mount> | null = null;

  beforeEach(() => {
    setActivePinia(createPinia());
    vi.clearAllMocks();
    (operationsApi.getSystemStatus as any).mockResolvedValue(null);
    (operationsApi.getSubsystems as any).mockResolvedValue([
      {
        id: 'themis',
        name: 'Themis',
        description: 'Security & Policy',
        state: 'HEALTHY',
        instanceCount: 1,
        version: '1.0.0',
        lastUpdated: Date.now()
      },
      {
        id: 'petasos',
        name: 'Petasos',
        description: 'Messaging & Transport',
        state: 'HEALTHY',
        instanceCount: 2,
        version: '1.0.0',
        lastUpdated: Date.now()
      }
    ]);
    (operationsApi.getAlerts as any).mockResolvedValue([
      {
        alertId: 'alert-001',
        subsystem: 'themis',
        component: 'PolicyEngine',
        severity: 'WARNING',
        condition: 'High evaluation latency detected',
        firstObserved: Date.now() - 60000,
        lastObserved: Date.now(),
        status: 'ACTIVE'
      }
    ]);
    (operationsApi.getQueues as any).mockResolvedValue([]);
    (operationsApi.getWorkflows as any).mockResolvedValue([]);
  });

  afterEach(() => {
    wrapper?.unmount();
    wrapper = null;
  });

  it('renders platform operational status strip and triage sections', async () => {
    wrapper = mount(OverviewView, {
      global: {
        stubs: {
          RouterLink: RouterLinkStub
        }
      }
    });
    await flushPromises();

    const text = wrapper.text();
    expect(text).toContain('Platform Operational Overview');
    expect(text).toContain('PLATFORM STATUS:');
    expect(text).toContain('Subsystems:');
    expect(text).toContain('2/2 Up');
    expect(text).toContain('Active Queues:');
    expect(text).toContain('Executing Tasks:');
    expect(text).toContain('Alerts:');
    expect(text).toContain('1');

    // The 3 core operational questions in IrisSection
    expect(text).toContain('Is Harmonia healthy?');
    expect(text).toContain('Is work moving?');
    expect(text).toContain('Are messages backing up?');
  });

  it('renders all 6 Harmonia architectural areas in high-density IrisDataTable', async () => {
    wrapper = mount(OverviewView, {
      global: {
        stubs: {
          RouterLink: RouterLinkStub
        }
      }
    });
    await flushPromises();

    const text = wrapper.text();
    expect(text).toContain('Subsystem Summary by Architectural Area');
    expect(text).toContain('6 Authoritative Areas');

    // 6 Area titles
    expect(text).toContain('Security & Policy');
    expect(text).toContain('Integration & Transport');
    expect(text).toContain('Information & State');
    expect(text).toContain('Execution & Processing');
    expect(text).toContain('Collaboration');
    expect(text).toContain('Presentation');
  });

  it('uses honest placeholders when no queue or workflow telemetry is available', async () => {
    (operationsApi.getSubsystems as any).mockResolvedValue([]);
    (operationsApi.getAlerts as any).mockResolvedValue([]);
    wrapper = mount(OverviewView, {
      global: {
        stubs: {
          RouterLink: RouterLinkStub
        }
      }
    });
    await flushPromises();

    const text = wrapper.text();
    expect(text).toContain('No live telemetry');
    expect(text).not.toContain('184 msg/s');
    expect(text).not.toContain('42.0');
    expect(text).not.toContain('99.98% hit rate');
    expect(text).not.toContain('340 eval/s');
    expect(text).not.toContain('28 req/s');
  });

  it('does not turn genuine zero rates into fabricated non-zero throughput', async () => {
    (operationsApi.getQueues as any).mockResolvedValue([{
      queueId: 'petasos.queue.empty',
      queueName: 'petasos.queue.empty',
      status: 'HEALTHY',
      depth: 0,
      consumerCount: 0,
      producerCount: 0,
      enqueueRate: 0,
      dequeueRate: 0,
      oldestMessageAgeSeconds: 0,
      redeliveryCount: 0,
      dlqDepth: 0,
      expiryCount: 0
    }]);
    (operationsApi.getWorkflows as any).mockResolvedValue([{
      workflowId: 'sequence-without-traffic',
      name: 'Sequence Without Traffic',
      activeExecutions: 0,
      queuedWork: 0,
      completedWork: 0,
      failedWork: 0,
      retryingWork: 0,
      processingRate: 0,
      p95DurationMs: 0,
      failureRate: 0
    }]);

    wrapper = mount(OverviewView, {
      global: {
        stubs: {
          RouterLink: RouterLinkStub
        }
      }
    });
    await flushPromises();

    const text = wrapper.text();
    expect(text).toContain('Telemetry initializing');
    expect(text).not.toContain('184 msg/s');
    expect(text).not.toContain('42.0');
  });
});
