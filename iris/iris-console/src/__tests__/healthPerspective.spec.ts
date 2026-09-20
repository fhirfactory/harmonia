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
import HealthView from '../views/HealthView.vue';
import { operationsApi } from '../api/operationsClient';
import type { OperationalHealth } from '../models/operations';

vi.mock('../api/operationsClient', () => ({
  operationsApi: {
    getSubsystems: vi.fn(),
    getSubsystemHealth: vi.fn()
  }
}));

const mockPush = vi.fn();
vi.mock('vue-router', () => ({
  useRouter: () => ({
    push: mockPush
  })
}));

const RouterLinkStub = {
  template: '<a><slot /></a>'
};

describe('Health Perspective (Architecture Health & Dependency Matrix)', () => {
  let wrapper: ReturnType<typeof mount> | null = null;

  beforeEach(() => {
    setActivePinia(createPinia());
    vi.clearAllMocks();

    (operationsApi.getSubsystems as any).mockResolvedValue([
      { id: 'themis', name: 'Themis', state: 'HEALTHY', instanceCount: 1, version: '1.0.0', lastUpdated: Date.now() },
      { id: 'calliope', name: 'Calliope', state: 'HEALTHY', instanceCount: 1, version: '1.0.0', lastUpdated: Date.now() },
      { id: 'petasos', name: 'Petasos', state: 'HEALTHY', instanceCount: 2, version: '1.0.0', lastUpdated: Date.now() },
      { id: 'energeia', name: 'Energeia', state: 'HEALTHY', instanceCount: 4, version: '1.0.0', lastUpdated: Date.now() },
      { id: 'pylai', name: 'Pylai', state: 'HEALTHY', instanceCount: 2, version: '1.0.0', lastUpdated: Date.now() },
      { id: 'iris', name: 'Iris', state: 'HEALTHY', instanceCount: 1, version: '1.0.0', lastUpdated: Date.now() },
      { id: 'agora', name: 'Agora', state: 'HEALTHY', instanceCount: 1, version: '1.0.0', lastUpdated: Date.now() },
      { id: 'mneme', name: 'Mneme', state: 'HEALTHY', instanceCount: 2, version: '1.0.0', lastUpdated: Date.now() },
      { id: 'mnemosyne', name: 'Mnemosyne', state: 'HEALTHY', instanceCount: 2, version: '1.0.0', lastUpdated: Date.now() }
    ]);

    (operationsApi.getSubsystemHealth as any).mockImplementation((id: string): Promise<OperationalHealth> => {
      switch (id) {
        case 'themis':
          return Promise.resolve({
            subsystemId: 'themis',
            status: 'HEALTHY',
            availabilityPercent: 99.99,
            failedOperations: 0,
            restartCount: 0,
            p95LatencyMs: 8,
            dependenciesSummary: '1 Connected',
            dependencies: [
              { name: 'Policy Repository', status: 'HEALTHY', latencyMs: 3, message: 'Active RBAC contracts' }
            ]
          });
        case 'calliope':
          return Promise.resolve({
            subsystemId: 'calliope',
            status: 'HEALTHY',
            availabilityPercent: 100.0,
            failedOperations: 0,
            restartCount: 0,
            p95LatencyMs: 2,
            dependenciesSummary: '0 Connected',
            dependencies: []
          });
        case 'petasos':
          return Promise.resolve({
            subsystemId: 'petasos',
            status: 'HEALTHY',
            availabilityPercent: 99.95,
            failedOperations: 0,
            restartCount: 0,
            p95LatencyMs: 11,
            dependenciesSummary: '2 Connected',
            dependencies: [
              { name: 'ActiveMQ Primary Node', status: 'HEALTHY', latencyMs: 2, message: 'Broker connected' },
              { name: 'ActiveMQ Backup Node', status: 'HEALTHY', latencyMs: 2, message: 'Replica sync nominal' }
            ]
          });
        case 'energeia':
          return Promise.resolve({
            subsystemId: 'energeia',
            status: 'HEALTHY',
            availabilityPercent: 99.98,
            failedOperations: 0,
            restartCount: 0,
            p95LatencyMs: 15,
            dependenciesSummary: '2 Connected',
            dependencies: [
              { name: 'Petasos Dispatch Queue', status: 'HEALTHY', latencyMs: 4, message: 'Queue drain normal' },
              { name: 'Mneme Task Cache', status: 'HEALTHY', latencyMs: 2, message: 'Hot Rod cache responsive' }
            ]
          });
        case 'pylai':
          return Promise.resolve({
            subsystemId: 'pylai',
            status: 'HEALTHY',
            availabilityPercent: 99.97,
            failedOperations: 0,
            restartCount: 0,
            p95LatencyMs: 12,
            dependenciesSummary: '2 Connected',
            dependencies: [
              { name: 'MLLP Socket Listener :2575', status: 'HEALTHY', latencyMs: 1, message: 'Dual-write ACK verified' },
              { name: 'Petasos Ingress Target', status: 'HEALTHY', latencyMs: 3, message: 'Target queue reachable' }
            ]
          });
        case 'iris':
          return Promise.resolve({
            subsystemId: 'iris',
            status: 'HEALTHY',
            availabilityPercent: 100.0,
            failedOperations: 0,
            restartCount: 0,
            p95LatencyMs: 6,
            dependenciesSummary: '1 Connected',
            dependencies: [
              { name: 'Operations Aggregator Service :8090', status: 'HEALTHY', latencyMs: 2, message: 'BEFE Gateway active' }
            ]
          });
        case 'agora':
          return Promise.resolve({
            subsystemId: 'agora',
            status: 'HEALTHY',
            availabilityPercent: 99.91,
            failedOperations: 0,
            restartCount: 0,
            p95LatencyMs: 24,
            dependenciesSummary: '2 Connected',
            dependencies: [
              { name: 'Synapse Homeserver :8008', status: 'HEALTHY', latencyMs: 18, message: 'Matrix CS-API connected' },
              { name: 'Petasos Agora Inbound', status: 'HEALTHY', latencyMs: 4, message: 'Inbound queue ready' }
            ]
          });
        case 'mneme':
          return Promise.resolve({
            subsystemId: 'mneme',
            status: 'HEALTHY',
            availabilityPercent: 99.99,
            failedOperations: 0,
            restartCount: 0,
            p95LatencyMs: 4,
            dependenciesSummary: '1 Connected',
            dependencies: [
              { name: 'Infinispan Cluster Grid', status: 'HEALTHY', latencyMs: 2, message: 'Hot Rod mesh synchronized' }
            ]
          });
        case 'mnemosyne':
          return Promise.resolve({
            subsystemId: 'mnemosyne',
            status: 'HEALTHY',
            availabilityPercent: 99.98,
            failedOperations: 0,
            restartCount: 0,
            p95LatencyMs: 16,
            dependenciesSummary: '2 Connected',
            dependencies: [
              { name: 'PostgreSQL Authoritative DB', status: 'HEALTHY', latencyMs: 3, message: 'Connection pool nominal' },
              { name: 'Themis Authorization', status: 'HEALTHY', latencyMs: 2, message: 'Security contract active' }
            ]
          });
        case 'paradeigma':
          return Promise.resolve({
            subsystemId: 'paradeigma',
            status: 'HEALTHY',
            availabilityPercent: 100.0,
            failedOperations: 0,
            restartCount: 0,
            p95LatencyMs: null,
            dependenciesSummary: 'Production Isolation (Leaf)',
            dependencies: []
          });
        default:
          return Promise.resolve({
            subsystemId: id,
            status: 'UNKNOWN',
            availabilityPercent: null,
            failedOperations: 0,
            restartCount: 0,
            p95LatencyMs: null,
            dependenciesSummary: 'Unknown'
          });
      }
    });
  });

  afterEach(() => {
    wrapper?.unmount();
    wrapper = null;
  });

  it('renders the architecture health matrix covering all 9 Harmonia subsystems', async () => {
    wrapper = mount(HealthView, {
      global: {
        stubs: {
          RouterLink: RouterLinkStub
        }
      }
    });
    await flushPromises();

    const text = wrapper.text();
    // Masthead & summary
    expect(text).toContain('Harmonia Architecture Health & Dependency Matrix');
    expect(text).toContain('FLEET HEALTH:');
    expect(text).toContain('9/9 Nominal');
    expect(text).toContain('Fleet SLA:');

    // All 9 Harmonia subprojects
    expect(text).toContain('Themis');
    expect(text).toContain('Calliope');
    expect(text).toContain('Hestia');
    expect(text).toContain('Petasos');
    expect(text).toContain('Energeia');
    expect(text).toContain('Pylai');
    expect(text).toContain('Iris');
    expect(text).toContain('Agora');
    expect(text).toContain('Paradeigma');
  });

  it('displays availability percentages, P95 latencies, and restart metrics', async () => {
    wrapper = mount(HealthView, {
      global: {
        stubs: {
          RouterLink: RouterLinkStub
        }
      }
    });
    await flushPromises();

    const text = wrapper.text();
    // Availability
    expect(text).toContain('99.99%');
    expect(text).toContain('99.95%');
    expect(text).toContain('100.00%');

    // P95 Latency
    expect(text).toContain('8 ms');
    expect(text).toContain('11 ms');
    expect(text).toContain('15 ms');
    expect(text).toContain('24 ms');

    // Dependencies
    expect(text).toContain('Connected');
    expect(text).toContain('Production Isolation (Leaf)');
  });

  it('filters subsystems by live search input', async () => {
    wrapper = mount(HealthView, {
      global: {
        stubs: {
          RouterLink: RouterLinkStub
        }
      }
    });
    await flushPromises();

    const searchInput = wrapper.find('#health-search');
    expect(searchInput.exists()).toBe(true);

    // Filter by 'Pylai'
    await searchInput.setValue('Pylai');
    await flushPromises();

    const filteredText = wrapper.text();
    expect(filteredText).toContain('Pylai');
    expect(filteredText).not.toContain('Themis');
    expect(filteredText).not.toContain('Agora');

    // Clear search
    await searchInput.setValue('');
    await flushPromises();
    expect(wrapper.text()).toContain('Themis');
    expect(wrapper.text()).toContain('Agora');
  });

  it('filters subsystems by area dropdown', async () => {
    wrapper = mount(HealthView, {
      global: {
        stubs: {
          RouterLink: RouterLinkStub
        }
      }
    });
    await flushPromises();

    const select = wrapper.find('select');
    await select.setValue('security-policy');
    await flushPromises();

    const text = wrapper.text();
    expect(text).toContain('Themis');
    expect(text).not.toContain('Petasos');
    expect(text).not.toContain('Pylai');
  });

  it('opens and closes the secondary dependency disclosure drawer', async () => {
    wrapper = mount(HealthView, {
      global: {
        stubs: {
          RouterLink: RouterLinkStub,
          teleport: true
        }
      }
    });
    await flushPromises();

    // Find dependency button for Petasos
    const buttons = wrapper.findAll('button');
    const petasosDepBtn = buttons.find(b => b.text().includes('2 Connected'));
    expect(petasosDepBtn).toBeDefined();

    await petasosDepBtn!.trigger('click');
    await flushPromises();

    // Drawer should now be visible
    expect(wrapper.find('aside[role="dialog"]').exists()).toBe(true);
    const drawerText = wrapper.find('aside[role="dialog"]').text();
    expect(drawerText).toContain('Dependencies');
    expect(drawerText).toContain('Upstream & Downstream Target Probes');

    // Close button
    const closeBtn = wrapper.find('button[aria-label="Close drawer"]');
    await closeBtn.trigger('click');
    await flushPromises();

    expect(wrapper.find('aside[role="dialog"]').exists()).toBe(false);
  });

  it('navigates to Subsystems workstation when clicking inspect', async () => {
    wrapper = mount(HealthView, {
      global: {
        stubs: {
          RouterLink: RouterLinkStub
        }
      }
    });
    await flushPromises();

    const inspectButtons = wrapper.findAll('button[aria-label*="Inspect"]');
    expect(inspectButtons.length).toBeGreaterThan(0);

    await inspectButtons[0].trigger('click');
    expect(mockPush).toHaveBeenCalled();
  });

  it('handles probe failures gracefully with Promise.allSettled', async () => {
    (operationsApi.getSubsystemHealth as any).mockImplementation((id: string) => {
      if (id === 'themis') {
        return Promise.reject(new Error('Network timeout contacting Themis health endpoint'));
      }
      return Promise.resolve({
        subsystemId: id,
        status: 'HEALTHY',
        availabilityPercent: 99.9,
        failedOperations: 0,
        restartCount: 0,
        p95LatencyMs: 5,
        dependenciesSummary: '1 Connected',
        dependencies: []
      });
    });

    wrapper = mount(HealthView, {
      global: {
        stubs: {
          RouterLink: RouterLinkStub
        }
      }
    });
    await flushPromises();

    // The entire view should still render successfully despite the single subsystem probe failure
    const text = wrapper.text();
    expect(text).toContain('Harmonia Architecture Health & Dependency Matrix');
    expect(text).toContain('Themis');
    expect(text).toContain('Calliope');
    expect(text).toContain('Petasos');
  });
});
