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
import { mount } from '@vue/test-utils';
import { createPinia, setActivePinia } from 'pinia';
import { useOperationsStore } from '../stores/operationsStore';
import SubsystemSidebar from '../components/subsystems/SubsystemSidebar.vue';
import SubsystemHeader from '../components/subsystems/SubsystemHeader.vue';
import HealthDependenciesPanel from '../components/subsystems/HealthDependenciesPanel.vue';
import StatisticsPanel from '../components/subsystems/StatisticsPanel.vue';

vi.mock('../api/operationsClient', () => ({
  operationsApi: {
    getSubsystems: vi.fn().mockResolvedValue([]),
    getSubsystem: vi.fn(),
    getSubsystemInstances: vi.fn().mockResolvedValue([]),
    getSubsystemHealth: vi.fn().mockResolvedValue(null),
    getSubsystemStatistics: vi.fn().mockResolvedValue({}),
    getSummary: vi.fn().mockResolvedValue(null),
    getAlerts: vi.fn().mockResolvedValue([])
  }
}));

describe('Subsystems Perspective Components', () => {
  let store: any;

  beforeEach(async () => {
    setActivePinia(createPinia());
    store = useOperationsStore();
    await store.fetchSubsystems();
  });

  describe('SubsystemSidebar.vue', () => {
    it('renders all canonical Harmonia subsystems in the inventory', () => {
      const wrapper = mount(SubsystemSidebar);
      const text = wrapper.text();

      expect(text).toContain('Pylai');
      expect(text).toContain('Petasos');
      expect(text).toContain('Energeia');
      expect(text).toContain('Mneme');
      expect(text).toContain('Mnemosyne');
      expect(text).toContain('Calliope');
      expect(text).toContain('Themis');
      expect(text).toContain('Agora');
      expect(text).toContain('Iris');
    });

    it('renders child nodes Ponos and Praxis under Energeia', () => {
      const wrapper = mount(SubsystemSidebar);
      const text = wrapper.text();

      expect(text).toContain('Ponos');
      expect(text).toContain('Praxis');
    });

    it('filters subsystems list according to search query', async () => {
      const wrapper = mount(SubsystemSidebar);
      const input = wrapper.find('input[type="text"]');

      await input.setValue('Petasos');
      expect(wrapper.text()).toContain('Petasos');
      expect(wrapper.text()).not.toContain('Mnemosyne');
    });
  });

  describe('SubsystemHeader.vue', () => {
    it('renders subsystem name, description, instance count, and version', () => {
      store.selectedSubsystemId = 'petasos';
      const wrapper = mount(SubsystemHeader);
      const text = wrapper.text();

      expect(text).toContain('Petasos');
      expect(text).toContain('Resilient Messaging Abstraction');
      expect(text).toContain('Instance');
      expect(text).toContain('v1.0.0');
    });

    it('renders stale cached snapshot badge when subsystem is stale', () => {
      store.subsystems = [
        {
          id: 'petasos',
          name: 'Petasos',
          description: 'Messaging',
          state: 'HEALTHY',
          instanceCount: 1,
          version: '1.0.0',
          lastUpdated: Date.now(),
          stale: true
        }
      ];
      store.selectedSubsystemId = 'petasos';

      const wrapper = mount(SubsystemHeader);
      expect(wrapper.text()).toContain('Cached Snapshot');
      expect(wrapper.text()).toContain('STALE TELEMETRY');
    });
  });

  describe('HealthDependenciesPanel.vue', () => {
    it('renders operational health summary, dependencies, and honest N/A values', () => {
      store.currentHealth = {
        subsystemId: 'petasos',
        status: 'HEALTHY',
        availabilityPercent: 99.98,
        failedOperations: 0,
        restartCount: 1,
        p95LatencyMs: null, // Null should render N/A
        dependenciesSummary: '2 / 2 Healthy',
        stale: false,
        dependencies: [
          { name: 'Mneme', status: 'HEALTHY', latencyMs: 8, message: 'Hot Rod cache responsive' },
          { name: 'Themis', status: 'HEALTHY', latencyMs: 14, message: 'Policy check nominal' }
        ]
      };

      const wrapper = mount(HealthDependenciesPanel);
      const text = wrapper.text();

      expect(text).toContain('HEALTHY');
      expect(text).toContain('99.98%');
      expect(text).toContain('1'); // Restarts
      expect(text).toContain('N/A'); // P95 latency is null
      expect(text).toContain('Mneme');
      expect(text).toContain('8 ms');
      expect(text).toContain('Themis');
      expect(text).toContain('14 ms');
    });
  });

  describe('StatisticsPanel.vue', () => {
    it('renders time window selectors and updates selectedWindow on click', async () => {
      const wrapper = mount(StatisticsPanel);

      const btn6h = wrapper.findAll('button').find(b => b.text() === '6h');
      expect(btn6h).toBeDefined();

      await btn6h!.trigger('click');
      expect(store.selectedWindow).toBe('6h');
    });

    it('renders metric cards with honest N/A when telemetry is missing', () => {
      store.statistics = {};
      const wrapper = mount(StatisticsPanel);
      const text = wrapper.text();

      expect(text).toContain('Events In');
      expect(text).toContain('Events Out');
      expect(text).toContain('Tasks Processed');
      expect(text).toContain('Processing Rate');
      expect(text).toContain('N/A');
    });
  });
});
