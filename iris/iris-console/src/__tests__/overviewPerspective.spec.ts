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
    getSummary: vi.fn(),
    getSubsystems: vi.fn(),
    getAlerts: vi.fn(),
    getQueues: vi.fn(),
    getSubsystemStatistics: vi.fn()
  }
}));

const RouterLinkStub = {
  props: ['to'],
  template: '<a :href="to"><slot /></a>'
};

function mountOverview() {
  return mount(OverviewView, {
    global: {
      stubs: {
        RouterLink: RouterLinkStub
      }
    }
  });
}

describe('Overview perspective', () => {
  let wrapper: ReturnType<typeof mount> | null = null;

  beforeEach(() => {
    setActivePinia(createPinia());
    vi.clearAllMocks();
    (operationsApi.getSummary as any).mockResolvedValue({
      platformStatus: 'DEGRADED',
      environment: 'PROD',
      cluster: 'harmonia-cluster-01',
      timestamp: Date.now(),
      totalSubsystems: 2,
      degradedSubsystems: 1,
      criticalAlerts: 0,
      warningAlerts: 1,
      lastRefreshed: Date.now()
    });
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
        state: 'DEGRADED',
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
    (operationsApi.getSubsystemStatistics as any).mockResolvedValue({});
  });

  afterEach(() => {
    wrapper?.unmount();
    wrapper = null;
  });

  it('renders a compact platform status strip driven by the summary payload', async () => {
    wrapper = mountOverview();
    await flushPromises();

    const text = wrapper.text();
    expect(text).toContain('Overview');
    expect(text).toContain('Platform status');
    expect(text).toContain('Subsystems healthy');
    expect(text).toContain('1 of 2');
    expect(text).toContain('Environment');
    expect(text).toContain('PROD');
    expect(text).toContain('harmonia-cluster-01');
  });

  it('renders a dense subsystem grid linking to each subsystem detail page', async () => {
    wrapper = mountOverview();
    await flushPromises();

    const links = wrapper.findAll('.overview-view__grid-link');
    expect(links).toHaveLength(2);
    expect(links.map(l => l.attributes('href'))).toEqual(['/subsystems/themis', '/subsystems/petasos']);
    expect(wrapper.text()).toContain('subsystem(s) need attention: Petasos');
  });

  it('links the alert and queue summaries onward to their detail pages', async () => {
    wrapper = mountOverview();
    await flushPromises();

    const hrefs = wrapper.findAll('a').map(a => a.attributes('href'));
    expect(hrefs).toContain('/subsystems');
    expect(hrefs).toContain('/alerts');
    expect(hrefs).toContain('/messages');
    expect(wrapper.text()).toContain('High evaluation latency detected');
  });

  it('uses only the four permitted operations endpoints', async () => {
    wrapper = mountOverview();
    await flushPromises();

    expect(operationsApi.getSummary).toHaveBeenCalled();
    expect(operationsApi.getSubsystems).toHaveBeenCalled();
    expect(operationsApi.getAlerts).toHaveBeenCalled();
    expect(operationsApi.getQueues).toHaveBeenCalled();
    expect((operationsApi as any).getSystemStatus).toBeUndefined();
    expect((operationsApi as any).getWorkflows).toBeUndefined();
  });

  it('distinguishes an empty queue result from an unreachable API', async () => {
    wrapper = mountOverview();
    await flushPromises();

    const text = wrapper.text();
    expect(text).toContain('No queues present');
    expect(text).toContain('The operations API responded successfully but reported no queues.');
    expect(text).not.toContain('Queue activity unavailable');
  });

  it('never fabricates a broker topology when the payload does not supply one', async () => {
    (operationsApi.getQueues as any).mockResolvedValue([{
      queueId: 'petasos.queue.task',
      queueName: 'petasos.queue.task',
      status: 'IDLE',
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

    wrapper = mountOverview();
    await flushPromises();

    const text = wrapper.text();
    expect(text).toContain('Broker topology:');
    expect(text).toContain('Not reported by the operations API');
    expect(text).not.toContain('Artemis');
  });

  it('states plainly when the whole operations API is unreachable', async () => {
    (operationsApi.getSummary as any).mockRejectedValue(new Error('Network Error'));
    (operationsApi.getSubsystems as any).mockRejectedValue(new Error('Network Error'));
    (operationsApi.getAlerts as any).mockRejectedValue(new Error('Network Error'));
    (operationsApi.getQueues as any).mockRejectedValue(new Error('Network Error'));

    wrapper = mountOverview();
    await flushPromises();

    const text = wrapper.text();
    expect(text).toContain('Operations API unavailable');
    expect(text).not.toContain('No queues present');
    expect(text).not.toContain('No active alerts present');
  });

  it('reports a partial failure without hiding the sections that did load', async () => {
    (operationsApi.getQueues as any).mockRejectedValue(new Error('Network Error'));

    wrapper = mountOverview();
    await flushPromises();

    const text = wrapper.text();
    expect(text).toContain('Partial data');
    expect(text).toContain('Queue activity');
    expect(text).toContain('Queue activity unavailable');
    // Sections that loaded are still shown
    expect(text).toContain('Themis');
    expect(text).toContain('High evaluation latency detected');
  });

  it('never renders a stack trace for a failed slice', async () => {
    const err = new Error('Request failed\n    at XMLHttpRequest.handleError');
    (operationsApi.getAlerts as any).mockRejectedValue(err);

    wrapper = mountOverview();
    await flushPromises();

    expect(wrapper.text()).not.toContain('at XMLHttpRequest');
  });

  it('renders no fabricated throughput or workflow telemetry', async () => {
    wrapper = mountOverview();
    await flushPromises();

    const text = wrapper.text();
    expect(text).not.toContain('msg/s');
    expect(text).not.toContain('Is work moving?');
    expect(text).not.toContain('Telemetry initializing');
  });
});
