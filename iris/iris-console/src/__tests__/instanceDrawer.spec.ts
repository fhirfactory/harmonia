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

import { describe, it, expect, beforeEach } from 'vitest';
import { mount } from '@vue/test-utils';
import { createPinia, setActivePinia } from 'pinia';
import { useOperationsStore } from '../stores/operationsStore';
import InstanceTable from '../components/subsystems/InstanceTable.vue';
import InstanceDetailDrawer from '../components/subsystems/InstanceDetailDrawer.vue';
import type { OperationalInstance } from '../models/operations';

describe('InstanceTable and InstanceDetailDrawer', () => {
  let store: any;

  const mockInstance: OperationalInstance = {
    instanceId: 'petasos-broker-0',
    subsystemId: 'petasos',
    role: 'Primary',
    state: 'HEALTHY',
    ready: true,
    restartCount: 2,
    uptime: '4d 12h',
    startedAt: 1700000000000,
    cpuPercent: null, // Test unmeasured/null metric
    memoryMb: 1024,
    podName: 'petasos-broker-0-pod',
    namespace: 'harmonia',
    nodeName: 'worker-node-02',
    ipAddress: '10.244.1.88',
    containerImage: 'docker.io/harmonia/petasos:1.0.0',
    appVersion: '1.0.0',
    recentErrors: ['Transient heartbeat timeout recovered in 150ms'],
    dependencies: ['artemis-cluster', 'mneme-cache']
  };

  beforeEach(() => {
    setActivePinia(createPinia());
    store = useOperationsStore();
    store.instances = [mockInstance];
  });

  it('renders instance row in InstanceTable with honest N/A for null CPU', () => {
    const wrapper = mount(InstanceTable);
    const text = wrapper.text();

    expect(text).toContain('petasos-broker-0');
    expect(text).toContain('Primary');
    expect(text).toContain('Ready');
    expect(text).toContain('2'); // Restarts
    expect(text).toContain('4d 12h');
    expect(text).toContain('1024 MB'); // Memory
    expect(text).toContain('N/A'); // CPU is null, must render N/A
  });

  it('triggers store.openInstanceDrawer when row is clicked', async () => {
    const wrapper = mount(InstanceTable);
    const row = wrapper.find('tbody tr');
    expect(row.exists()).toBe(true);

    await row.trigger('click');
    expect(store.isInstanceDrawerOpen).toBe(true);
    expect(store.selectedInstance).toEqual(mockInstance);
  });

  it('renders safe operational metadata in InstanceDetailDrawer without exposing secrets', async () => {
    store.selectedInstance = mockInstance;
    store.isInstanceDrawerOpen = true;

    const wrapper = mount(InstanceDetailDrawer, {
      attachTo: document.body
    });

    const bodyText = document.body.textContent || '';
    expect(bodyText).toContain('petasos-broker-0');
    expect(bodyText).toContain('harmonia');
    expect(bodyText).toContain('worker-node-02');
    expect(bodyText).toContain('10.244.1.88');
    expect(bodyText).toContain('docker.io/harmonia/petasos:1.0.0');
    expect(bodyText).toContain('Transient heartbeat timeout recovered in 150ms');

    // Asserts zero secrets or database passwords in DOM
    expect(bodyText).not.toContain('password');
    expect(bodyText).not.toContain('secret');
    expect(bodyText).not.toContain('postgres://');

    wrapper.unmount();
  });

  it('closes drawer when Close button is clicked', async () => {
    store.selectedInstance = mockInstance;
    store.isInstanceDrawerOpen = true;

    const wrapper = mount(InstanceDetailDrawer, {
      attachTo: document.body
    });

    const closeBtn = document.body.querySelector('button[aria-label="Close drawer"]') as HTMLButtonElement;
    expect(closeBtn).not.toBeNull();
    closeBtn.click();

    expect(store.isInstanceDrawerOpen).toBe(false);
    expect(store.selectedInstance).toBeNull();

    wrapper.unmount();
  });
});
