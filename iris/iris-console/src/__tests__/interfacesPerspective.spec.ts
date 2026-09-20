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
import { useOperationsStore } from '../stores/operationsStore';
import { operationsApi } from '../api/operationsClient';
import InterfacesView from '../views/InterfacesView.vue';

vi.mock('../api/operationsClient', () => ({
  operationsApi: {
    getSubsystems: vi.fn(),
    getSubsystemInstances: vi.fn(),
    getSubsystemHealth: vi.fn(),
    getSubsystemStatistics: vi.fn(),
    getSummary: vi.fn()
  }
}));

describe('Interfaces Perspective (Pylai Gateways)', () => {
  let store: ReturnType<typeof useOperationsStore>;

  beforeEach(() => {
    setActivePinia(createPinia());
    store = useOperationsStore();
    vi.clearAllMocks();

    (operationsApi.getSubsystems as any).mockResolvedValue([
      {
        id: 'pylai',
        name: 'Pylai',
        description: 'Harmonia Inbound/Outbound protocol gateways (MLLP, FHIR REST Registry)',
        state: 'HEALTHY',
        instanceCount: 1,
        version: '1.0.0',
        lastUpdated: Date.now()
      }
    ]);
    (operationsApi.getSubsystemInstances as any).mockResolvedValue([]);
    (operationsApi.getSubsystemHealth as any).mockResolvedValue({
      subsystemId: 'pylai',
      state: 'HEALTHY',
      availabilityPercent: 100.0,
      failedOperations: 0,
      restartCount: 0,
      p95LatencyMs: 12.5,
      dependenciesSummary: 'All dependencies healthy'
    });
  });

  it('renders Pylai identity, subtitle, and compliance badges', async () => {
    const wrapper = mount(InterfacesView);
    await flushPromises();

    const text = wrapper.text();
    expect(text).toContain('Pylai');
    expect(text).toContain('Interface Gateways');
    expect(text).toContain('REC-001 Dual-Write Safety');
    expect(text).toContain('IRIS-API-GAP-001');
    expect(text).toContain('4 Interfaces');
  });

  it('renders compact operational summary metrics strip', async () => {
    const wrapper = mount(InterfacesView);
    await flushPromises();

    const text = wrapper.text();
    expect(text).toContain('Configured Interfaces:');
    expect(text).toContain('4 Interfaces');
    expect(text).toContain('Inbound Interfaces:');
    expect(text).toContain('2');
    expect(text).toContain('Outbound Interfaces:');
    expect(text).toContain('2');
    expect(text).toContain('Active Listeners:');
    expect(text).toContain('4');
  });

  it('renders all four authoritative Pylai gateways in the operational table', async () => {
    const wrapper = mount(InterfacesView);
    await flushPromises();

    const text = wrapper.text();
    expect(text).toContain('MLLP Inbound Gateway');
    expect(text).toContain('MLLP Outbound HIS');
    expect(text).toContain('MLLP Outbound LIS');
    expect(text).toContain('FHIR Provider Registry Gateway');

    // Listening and distribution ports
    expect(text).toContain(':2575');
    expect(text).toContain(':8087');
    expect(text).toContain(':8088');
    expect(text).toContain(':8089');
  });

  it('reveals secondary details (target Petasos queues, management ports, invariants) in InterfaceDetailDrawer upon inspection', async () => {
    const wrapper = mount(InterfacesView, {
      attachTo: document.body
    });
    await flushPromises();

    // Inspect first gateway (MLLP Inbound)
    const inspectButtons = wrapper.findAll('.iris-inspect-btn');
    expect(inspectButtons.length).toBeGreaterThan(0);
    await inspectButtons[0].trigger('click');
    await flushPromises();

    const bodyText = document.body.textContent || '';
    expect(bodyText).toContain('petasos.queue.pylai.mllp.in');
    expect(bodyText).toContain('REC-001 Dual-Write Safety');
    expect(bodyText).toContain(':8084');
    expect(bodyText).toContain('IRIS-API-GAP-001');

    wrapper.unmount();
  });

  it('closes InterfaceDetailDrawer on Escape key press', async () => {
    const wrapper = mount(InterfacesView, {
      attachTo: document.body
    });
    await flushPromises();

    const inspectButtons = wrapper.findAll('.iris-inspect-btn');
    await inspectButtons[0].trigger('click');
    await flushPromises();

    expect(document.body.querySelector('.interface-drawer__panel')).not.toBeNull();

    // Press Escape
    window.dispatchEvent(new KeyboardEvent('keydown', { key: 'Escape' }));
    await flushPromises();

    expect(document.body.querySelector('.interface-drawer__panel')).toBeNull();

    wrapper.unmount();
  });

  it('displays honest placeholders rather than invented throughput metrics per IRIS-API-GAP-001', async () => {
    const wrapper = mount(InterfacesView);
    await flushPromises();

    const text = wrapper.text();
    // Honest placeholder for throughput
    expect(text).toContain('Telemetry initializing');
  });

  it('filters gateways by text query', async () => {
    const wrapper = mount(InterfacesView);
    await flushPromises();

    const searchInput = wrapper.find('input[placeholder*="Filter interfaces"]');
    expect(searchInput.exists()).toBe(true);

    await searchInput.setValue('his');
    expect(wrapper.text()).toContain('MLLP Outbound HIS');
    expect(wrapper.text()).not.toContain('MLLP Outbound LIS');
    expect(wrapper.text()).not.toContain('FHIR Provider Registry Gateway');
  });

  it('filters gateways by direction buttons (Inbound / Outbound)', async () => {
    const wrapper = mount(InterfacesView);
    await flushPromises();

    const buttons = wrapper.findAll('button');
    const outboundBtn = buttons.find(b => b.text().includes('Outbound'));
    expect(outboundBtn).toBeDefined();

    await outboundBtn!.trigger('click');
    expect(wrapper.text()).toContain('MLLP Outbound HIS');
    expect(wrapper.text()).toContain('MLLP Outbound LIS');
    expect(wrapper.text()).not.toContain('MLLP Inbound Gateway');
    expect(wrapper.text()).not.toContain('FHIR Provider Registry Gateway');
  });

  it('binds gateway status to live Pylai subsystem state', async () => {
    (operationsApi.getSubsystems as any).mockResolvedValue([
      {
        id: 'pylai',
        name: 'Pylai',
        description: 'Gateways',
        state: 'DEGRADED',
        instanceCount: 1,
        version: '1.0.0',
        lastUpdated: Date.now()
      }
    ]);

    const wrapper = mount(InterfacesView);
    await flushPromises();

    expect(wrapper.text()).toContain('DEGRADED');
  });
});
