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
import { operationsApi } from '../api/operationsClient';
import InterfacesView from '../views/InterfacesView.vue';

vi.mock('../api/operationsClient', () => ({
  operationsApi: {
    getSubsystems: vi.fn(),
    getSubsystemInstances: vi.fn(),
    getSubsystemHealth: vi.fn(),
    getSubsystemStatistics: vi.fn()
  }
}));

const PYLAI_SUBSYSTEM = {
  id: 'pylai',
  name: 'Pylai',
  description: 'Harmonia Inbound/Outbound protocol gateways (MLLP, FHIR REST Registry)',
  state: 'HEALTHY',
  instanceCount: 1,
  version: '1.0.0',
  lastUpdated: Date.now()
};

describe('Interfaces Perspective (Pylai Gateways)', () => {
  beforeEach(() => {
    setActivePinia(createPinia());
    vi.clearAllMocks();

    (operationsApi.getSubsystems as any).mockResolvedValue([PYLAI_SUBSYSTEM]);
    (operationsApi.getSubsystemInstances as any).mockResolvedValue([]);
    (operationsApi.getSubsystemHealth as any).mockResolvedValue({
      subsystemId: 'pylai',
      status: 'HEALTHY',
      availabilityPercent: 100,
      failedOperations: 0,
      restartCount: 0,
      p95LatencyMs: 12.5,
      dependenciesSummary: 'All dependencies healthy'
    });
  });

  it('presents the page as configured inventory plus separately observed runtime', async () => {
    const wrapper = mount(InterfacesView);
    await flushPromises();

    const text = wrapper.text();
    expect(text).toContain('Interfaces');
    expect(text).toContain('Configured inventory');
    expect(text).toContain('Configured gateway inventory');
    expect(text).toContain('Observed Pylai runtime');
  });

  it('states that the inventory is configured, not runtime-discovered', async () => {
    const wrapper = mount(InterfacesView);
    await flushPromises();

    expect(wrapper.text()).toContain('configured in the Harmonia deployment, not discovered at runtime');
  });

  it('renders every configured gateway with only genuine configured facts', async () => {
    const wrapper = mount(InterfacesView);
    await flushPromises();

    const text = wrapper.text();
    expect(text).toContain('MLLP Inbound Gateway');
    expect(text).toContain('MLLP Outbound HIS');
    expect(text).toContain('MLLP Outbound LIS');
    expect(text).toContain('FHIR Provider Registry Gateway');

    expect(text).toContain(':2575');
    expect(text).toContain(':8087');
    expect(text).toContain(':8088');
    expect(text).toContain(':8089');

    // Target queues are configured facts and are shown as a column.
    expect(text).toContain('petasos.queue.pylai.mllp.in');

    const headers = wrapper.findAll('th').map(th => th.text());
    expect(headers).toContain('Interface');
    expect(headers).toContain('Direction');
    expect(headers).toContain('Protocol');
    expect(headers).toContain('Port');
    expect(headers).toContain('Target queue');
  });

  it('renders no fabricated runtime column or rollup (FR1)', async () => {
    const wrapper = mount(InterfacesView);
    await flushPromises();

    const text = wrapper.text();
    const headers = wrapper.findAll('th').map(th => th.text());

    expect(headers).not.toContain('Error State');
    expect(headers).not.toContain('Status');
    expect(headers).not.toContain('Throughput / Activity');
    expect(text).not.toContain('Error State');
    expect(text).not.toContain('Active Listeners');
    expect(text).not.toContain('Current Connections');
    expect(text).not.toContain('Telemetry initializing');
    expect(text).not.toContain('Nominal (0 err)');
  });

  it('declares per-interface runtime metrics as not measured rather than estimating them', async () => {
    const wrapper = mount(InterfacesView);
    await flushPromises();

    expect(wrapper.text()).toContain('not measured by any Harmonia API');
  });

  it('shows only the Pylai health facts the operations API returned', async () => {
    const wrapper = mount(InterfacesView);
    await flushPromises();

    const text = wrapper.text();
    expect(text).toContain('Availability');
    expect(text).toContain('100%');
    expect(text).toContain('12.5 ms');
    expect(text).toContain('All dependencies healthy');
    expect(text).toContain('The operations API reported no Pylai instances.');
  });

  it('marks unreported runtime facts as not measured', async () => {
    (operationsApi.getSubsystemHealth as any).mockResolvedValue({
      subsystemId: 'pylai',
      status: 'HEALTHY',
      availabilityPercent: null,
      failedOperations: 0,
      restartCount: 0,
      p95LatencyMs: null
    });

    const wrapper = mount(InterfacesView);
    await flushPromises();

    expect(wrapper.text()).toContain('Not measured');
  });

  it('distinguishes an unreachable operations API from an empty result', async () => {
    (operationsApi.getSubsystemInstances as any).mockRejectedValue(new Error('Network Error'));
    (operationsApi.getSubsystemHealth as any).mockRejectedValue(new Error('Network Error'));

    const wrapper = mount(InterfacesView);
    await flushPromises();

    const text = wrapper.text();
    expect(text).toContain('Pylai runtime observation unavailable');
    expect(text).not.toContain('No Pylai runtime state reported');
    // The configured inventory remains readable while runtime is unavailable.
    expect(text).toContain('MLLP Inbound Gateway');
  });

  it('reports an empty runtime result distinctly from unavailability', async () => {
    (operationsApi.getSubsystemInstances as any).mockResolvedValue([]);
    (operationsApi.getSubsystemHealth as any).mockResolvedValue(null);

    const wrapper = mount(InterfacesView);
    await flushPromises();

    const text = wrapper.text();
    expect(text).toContain('No Pylai runtime state reported');
    expect(text).not.toContain('Pylai runtime observation unavailable');
  });

  it('reports a partial runtime failure without hiding the loaded half', async () => {
    (operationsApi.getSubsystemInstances as any).mockRejectedValue(new Error('Network Error'));

    const wrapper = mount(InterfacesView);
    await flushPromises();

    const text = wrapper.text();
    expect(text).toContain('Pylai instances');
    expect(text).toContain('All dependencies healthy');
    expect(text).not.toContain('Pylai runtime observation unavailable');
  });

  it('never renders a stack trace', async () => {
    (operationsApi.getSubsystemInstances as any).mockRejectedValue(new Error('Network Error'));
    (operationsApi.getSubsystemHealth as any).mockRejectedValue(new Error('Network Error'));

    const wrapper = mount(InterfacesView);
    await flushPromises();

    const text = wrapper.text();
    expect(text).not.toContain('at Object.');
    expect(text).not.toContain('.spec.ts:');
  });

  it('filters the configured inventory by search query through the store', async () => {
    const wrapper = mount(InterfacesView);
    await flushPromises();

    const searchInput = wrapper.find('input[type="search"]');
    expect(searchInput.exists()).toBe(true);

    await searchInput.setValue('his');
    await flushPromises();

    expect(wrapper.text()).toContain('MLLP Outbound HIS');
    expect(wrapper.text()).not.toContain('MLLP Outbound LIS');
    expect(wrapper.text()).not.toContain('FHIR Provider Registry Gateway');
  });

  it('filters the configured inventory by direction', async () => {
    const wrapper = mount(InterfacesView);
    await flushPromises();

    const outboundBtn = wrapper
      .findAll('.interfaces-view__filter-btn')
      .find(b => b.text().startsWith('Outbound'));
    expect(outboundBtn).toBeDefined();

    await outboundBtn!.trigger('click');
    await flushPromises();

    const text = wrapper.text();
    expect(text).toContain('MLLP Outbound HIS');
    expect(text).toContain('MLLP Outbound LIS');
    expect(text).not.toContain('MLLP Inbound Gateway');
    expect(text).not.toContain('FHIR Provider Registry Gateway');
  });

  it('opens the detail drawer in place, without navigating away', async () => {
    const wrapper = mount(InterfacesView, { attachTo: document.body });
    await flushPromises();

    const inspectButtons = wrapper.findAll('.iris-inspect-btn');
    expect(inspectButtons.length).toBe(4);

    await inspectButtons[0].trigger('click');
    await flushPromises();

    const bodyText = document.body.textContent || '';
    expect(bodyText).toContain('petasos.queue.pylai.mllp.in');
    expect(bodyText).toContain('REC-001 Dual-Write Safety');
    expect(bodyText).toContain(':8084');
    expect(bodyText).toContain('Configured');
    // The drawer must not imply a measured per-interface runtime state.
    expect(bodyText).not.toContain('Telemetry initializing');

    wrapper.unmount();
  });

  it('closes the detail drawer on Escape', async () => {
    const wrapper = mount(InterfacesView, { attachTo: document.body });
    await flushPromises();

    await wrapper.findAll('.iris-inspect-btn')[0].trigger('click');
    await flushPromises();
    expect(document.body.querySelector('.interface-drawer__panel')).not.toBeNull();

    window.dispatchEvent(new KeyboardEvent('keydown', { key: 'Escape' }));
    await flushPromises();
    expect(document.body.querySelector('.interface-drawer__panel')).toBeNull();

    wrapper.unmount();
  });

  it('uses only the Pylai operations endpoints that genuinely exist', async () => {
    mount(InterfacesView);
    await flushPromises();

    expect(operationsApi.getSubsystemInstances).toHaveBeenCalledWith('pylai');
    expect(operationsApi.getSubsystemHealth).toHaveBeenCalledWith('pylai');
    expect(Object.keys(operationsApi)).not.toContain('getInterfaces');
  });
});
