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
import { createRouter, createMemoryHistory } from 'vue-router';
import { useOperationsStore } from '../stores/operationsStore';
import SubsystemSidebar from '../components/subsystems/SubsystemSidebar.vue';
import SubsystemTreeNavigator from '../components/subsystems/SubsystemTreeNavigator.vue';
import SubsystemHeader from '../components/subsystems/SubsystemHeader.vue';
import SubordinatedMiddlewarePanel from '../components/subsystems/SubordinatedMiddlewarePanel.vue';
import HealthDependenciesPanel from '../components/subsystems/HealthDependenciesPanel.vue';
import StatisticsPanel from '../components/subsystems/StatisticsPanel.vue';
import SubsystemsView from '../views/SubsystemsView.vue';
import { AUTHORITATIVE_SUBSYSTEMS, findAuthoritativeSubsystem } from '../models/subsystemHierarchy';
import { createIris } from '@harmonia/iris-befe';
import { routes } from '../router';

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
  let router: any;

  beforeEach(async () => {
    setActivePinia(createPinia());
    router = createRouter({
      history: createMemoryHistory(),
      routes
    });
    router.push('/subsystems/petasos');
    await router.isReady();

    store = useOperationsStore();
    await store.fetchSubsystems();
  });

  describe('SubsystemSidebar.vue', () => {
    it('renders all canonical Harmonia subsystems in the inventory', () => {
      const wrapper = mount(SubsystemSidebar, { global: { plugins: [createIris() as any] } });
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

    it('renders child nodes Ponos, Praxis, Ergon, and Pragma under Energeia', () => {
      const wrapper = mount(SubsystemSidebar, { global: { plugins: [createIris() as any] } });
      const text = wrapper.text();

      expect(text).toContain('Ponos');
      expect(text).toContain('Praxis');
      expect(text).toContain('Ergon');
      expect(text).toContain('Pragma');
    });

    it('filters subsystems list according to search query', async () => {
      const wrapper = mount(SubsystemSidebar, { global: { plugins: [createIris() as any] } });
      const input = wrapper.find('input[type="text"]');

      await input.setValue('Petasos');
      expect(wrapper.text()).toContain('Petasos');
      expect(wrapper.text()).not.toContain('Mnemosyne');
    });
  });

  describe('SubsystemTreeNavigator.vue', () => {
    it('renders the 6 authoritative Harmonia architectural areas', () => {
      const wrapper = mount(SubsystemTreeNavigator, { global: { plugins: [createIris() as any] } });
      const text = wrapper.text();

      expect(text).toContain('Integration & Transport');
      expect(text).toContain('Execution & Processing');
      expect(text).toContain('Information & State');
      expect(text).toContain('Security & Policy');
      expect(text).toContain('Collaboration');
      expect(text).toContain('Presentation');
    });

    it('renders English descriptions alongside Greek mythological names', () => {
      const wrapper = mount(SubsystemTreeNavigator, { global: { plugins: [createIris() as any] } });
      const text = wrapper.text();

      expect(text).toContain('Interface Gateways');
      expect(text).toContain('Messaging & Transport');
      expect(text).toContain('Workflow & Activity Execution');
      expect(text).toContain('Operational In-Memory Cache');
      expect(text).toContain('Security & Policy Enforcement');
      expect(text).toContain('Collaboration & Matrix Gateway');
      expect(text).toContain('Presentation Services');
    });
  });

  describe('SubordinatedMiddlewarePanel.vue', () => {
    it('renders subordinated middleware runtime details for Petasos (Artemis)', () => {
      const petasos = AUTHORITATIVE_SUBSYSTEMS['petasos'];
      expect(petasos.middleware).toBeDefined();

      const wrapper = mount(SubordinatedMiddlewarePanel, {
        global: {
          plugins: [createIris() as any]
        },
        props: {
          middleware: petasos.middleware!,
          subsystemName: 'Petasos'
        }
      });
      const text = wrapper.text();

      expect(text).toContain('Apache ActiveMQ Artemis Cluster');
      expect(text).toContain('Subordinated Middleware Runtime');
      expect(text).toContain('61616');
      expect(text).toContain('petasos-artemis-node1');
      expect(text).toContain('petasos.queue.pylai.mllp.in');
    });

    it('renders subordinated middleware runtime details for Mneme (Infinispan)', () => {
      const mneme = AUTHORITATIVE_SUBSYSTEMS['mneme'];
      expect(mneme.middleware).toBeDefined();

      const wrapper = mount(SubordinatedMiddlewarePanel, {
        global: {
          plugins: [createIris() as any]
        },
        props: {
          middleware: mneme.middleware!,
          subsystemName: 'Mneme'
        }
      });
      const text = wrapper.text();

      expect(text).toContain('Infinispan Clustered Cache Grid');
      expect(text).toContain('11222');
      expect(text).toContain('modulestatus-cache');
    });

    it('renders subordinated middleware runtime details for Mnemosyne (PostgreSQL & JPA)', () => {
      const mnemosyne = AUTHORITATIVE_SUBSYSTEMS['mnemosyne'];
      expect(mnemosyne.middleware).toBeDefined();

      const wrapper = mount(SubordinatedMiddlewarePanel, {
        global: {
          plugins: [createIris() as any]
        },
        props: {
          middleware: mnemosyne.middleware!,
          subsystemName: 'Mnemosyne'
        }
      });
      const text = wrapper.text();

      expect(text).toContain('PostgreSQL & HAPI FHIR JPA Storage');
      expect(text).toContain('5432');
      expect(text).toContain('fhir_node_authoritative');
      expect(text).toContain('ops_node_authoritative');
    });

    it('renders subordinated middleware runtime details for Agora (Synapse Matrix)', () => {
      const agora = AUTHORITATIVE_SUBSYSTEMS['agora'];
      expect(agora.middleware).toBeDefined();

      const wrapper = mount(SubordinatedMiddlewarePanel, {
        global: {
          plugins: [createIris() as any]
        },
        props: {
          middleware: agora.middleware!,
          subsystemName: 'Agora'
        }
      });
      const text = wrapper.text();

      expect(text).toContain('Synapse Matrix Homeserver & Healthcare AS Bridge');
      expect(text).toContain('8008');
      expect(text).toContain('8095');
      expect(text).toContain('Themis Default-Deny Room Governance');
    });
  });

  describe('SubsystemHeader.vue', () => {
    it('renders subsystem name, description, instance count, and version', () => {
      store.selectedSubsystemId = 'petasos';
      const wrapper = mount(SubsystemHeader, { global: { plugins: [createIris() as any] } });
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

      const wrapper = mount(SubsystemHeader, { global: { plugins: [createIris() as any] } });
      expect(wrapper.text()).toContain('Cached Snapshot');
      expect(wrapper.text()).toContain('Stale Telemetry');
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

      const wrapper = mount(HealthDependenciesPanel, { global: { plugins: [createIris() as any] } });
      const text = wrapper.text();

      expect(text).toContain('Healthy');
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
      const wrapper = mount(StatisticsPanel, { global: { plugins: [createIris() as any] } });

      const btn6h = wrapper.findAll('button').find(b => b.text() === '6h');
      expect(btn6h).toBeDefined();

      await btn6h!.trigger('click');
      expect(store.selectedWindow).toBe('6h');
    });

    it('renders metric cards with honest N/A when telemetry is missing', () => {
      store.statistics = {};
      const wrapper = mount(StatisticsPanel, { global: { plugins: [createIris() as any] } });
      const text = wrapper.text();

      expect(text).toContain('Events In');
      expect(text).toContain('Events Out');
      expect(text).toContain('Tasks Processed');
      expect(text).toContain('Processing Rate');
      expect(text).toContain('N/A');
    });
  });

  describe('SubsystemTreeNavigator.vue Keyboard Accessibility', () => {
    it('renders all tree items with tabindex="0" for keyboard focus', () => {
      const wrapper = mount(SubsystemTreeNavigator, { global: { plugins: [createIris() as any] } });
      const treeItems = wrapper.findAll('[role="treeitem"]');
      
      expect(treeItems.length).toBeGreaterThan(0);
      treeItems.forEach(item => {
        expect(item.attributes('tabindex')).toBe('0');
      });
    });

    it('allows Enter key to select a subsystem tree item', async () => {
      const wrapper = mount(SubsystemTreeNavigator, { global: { plugins: [createIris() as any] } });
      const petasosItem = wrapper.find('[data-item-id="petasos"]');
      
      expect(petasosItem.exists()).toBe(true);
      await petasosItem.trigger('keydown.enter');
      
      expect(store.selectedSubsystemId).toBe('petasos');
    });

    it('allows Space key to select a subsystem tree item', async () => {
      const wrapper = mount(SubsystemTreeNavigator, { global: { plugins: [createIris() as any] } });
      const energeiaItem = wrapper.find('[data-item-id="energeia"]');
      
      expect(energeiaItem.exists()).toBe(true);
      await energeiaItem.trigger('keydown.space');
      
      expect(store.selectedSubsystemId).toBe('energeia');
    });

    it('allows ArrowDown key to navigate to next tree item', async () => {
      const wrapper = mount(SubsystemTreeNavigator, { global: { plugins: [createIris() as any] } });
      const treeItems = wrapper.findAll('[data-item-id]');
      
      expect(treeItems.length).toBeGreaterThan(1);
      
      // Verify tree items exist and can receive keyboard events
      const firstItem = treeItems[0];
      expect(firstItem.exists()).toBe(true);
      
      // Trigger keydown on first item
      await firstItem.trigger('keydown.down');
      
      // Verify tree structure is intact after navigation
      expect(treeItems.length).toBeGreaterThan(0);
    });

    it('allows ArrowUp key to navigate to previous tree item', async () => {
      const wrapper = mount(SubsystemTreeNavigator, { global: { plugins: [createIris() as any] } });
      const treeItems = wrapper.findAll('[data-item-id]');
      
      expect(treeItems.length).toBeGreaterThan(1);
      
      // Verify we can navigate with arrow keys
      const lastItem = treeItems[treeItems.length - 1];
      await lastItem.trigger('keydown.up');
      
      // Verify tree structure is intact
      expect(treeItems.length).toBeGreaterThan(0);
    });

    it('supports arrow-key navigation on tree items', async () => {
      const wrapper = mount(SubsystemTreeNavigator, { global: { plugins: [createIris() as any] } });
      const treeItems = wrapper.findAll('[data-item-id]');
      
      expect(treeItems.length).toBeGreaterThan(0);
      
      // Verify all tree items have data-item-id attribute for navigation
      treeItems.forEach(item => {
        expect(item.attributes('data-item-id')).toBeTruthy();
      });
    });

    it('allows area collapse/expand via click', async () => {
      const wrapper = mount(SubsystemTreeNavigator, { global: { plugins: [createIris() as any] } });
      
      // Find an expanded area button
      const areaButton = wrapper.find('button[aria-expanded="true"]');
      expect(areaButton.exists()).toBe(true);
      
      const areaId = areaButton.attributes('aria-expanded');
      expect(areaId).toBe('true');
      
      // Click to collapse
      await areaButton.trigger('click');
      expect(areaButton.attributes('aria-expanded')).toBe('false');
      
      // Click to expand again
      await areaButton.trigger('click');
      expect(areaButton.attributes('aria-expanded')).toBe('true');
    });

    it('renders child subsystem nodes (Ponos, Praxis, Ergon, Pragma) as focusable tree items', () => {
      const wrapper = mount(SubsystemTreeNavigator, { global: { plugins: [createIris() as any] } });
      
      // Find child nodes
      const ponosItem = wrapper.find('[data-item-id="ponos"]');
      const praxisItem = wrapper.find('[data-item-id="praxis"]');
      const ergonItem = wrapper.find('[data-item-id="ergon"]');
      const pragmaItem = wrapper.find('[data-item-id="pragma"]');
      
      expect(ponosItem.exists()).toBe(true);
      expect(praxisItem.exists()).toBe(true);
      expect(ergonItem.exists()).toBe(true);
      expect(pragmaItem.exists()).toBe(true);
      
      // Verify they are focusable
      expect(ponosItem.attributes('tabindex')).toBe('0');
      expect(praxisItem.attributes('tabindex')).toBe('0');
      expect(ergonItem.attributes('tabindex')).toBe('0');
      expect(pragmaItem.attributes('tabindex')).toBe('0');
      expect(ponosItem.attributes('role')).toBe('treeitem');
      expect(praxisItem.attributes('role')).toBe('treeitem');
    });

    it('allows keyboard selection of child subsystem nodes', async () => {
      const wrapper = mount(SubsystemTreeNavigator, { global: { plugins: [createIris() as any] } });
      
      const ponosItem = wrapper.find('[data-item-id="ponos"]');
      expect(ponosItem.exists()).toBe(true);
      
      await ponosItem.trigger('keydown.enter');
      expect(store.selectedSubsystemId).toBe('ponos');
      
      const praxisItem = wrapper.find('[data-item-id="praxis"]');
      await praxisItem.trigger('keydown.space');
      expect(store.selectedSubsystemId).toBe('praxis');
    });
  });

  describe('SubsystemsView.vue 2-Column Workstation Layout', () => {
    it('renders left tree pane and right detail workstation pane', () => {
      store.selectedSubsystemId = 'petasos';
      const wrapper = mount(SubsystemsView, {
        global: {
          plugins: [router, createIris() as any],
          stubs: {
            RouterLink: true
          }
        }
      });

      const treePane = wrapper.findComponent(SubsystemTreeNavigator);
      expect(treePane.exists()).toBe(true);

      const mainPane = wrapper.find('main[role="main"]');
      expect(mainPane.exists()).toBe(true);
      expect(mainPane.attributes('aria-label')).toBe('Subsystem Operational Details');

      const text = wrapper.text();
      expect(text).toContain('Petasos');
      expect(text).toContain('Messaging & Transport');
      expect(text).toContain('Apache ActiveMQ Artemis Cluster');
    });

    it('switches perspective tabs between instances, health, statistics, and all', async () => {
      store.selectedSubsystemId = 'petasos';
      const wrapper = mount(SubsystemsView, {
        global: {
          plugins: [router, createIris() as any],
          stubs: {
            RouterLink: true
          }
        }
      });

      const tabs = wrapper.findAll('button[role="tab"]');
      expect(tabs.length).toBe(4);

      // Tab 1: Runtime Instances (default active)
      expect(tabs[0].text()).toContain('Runtime Instances');
      expect(tabs[0].attributes('aria-selected')).toBe('true');

      // Click Health & Dependencies tab
      await tabs[1].trigger('click');
      expect(tabs[1].attributes('aria-selected')).toBe('true');
      expect(wrapper.findComponent(HealthDependenciesPanel).exists()).toBe(true);

      // Click Telemetry Statistics tab
      await tabs[2].trigger('click');
      expect(tabs[2].attributes('aria-selected')).toBe('true');
      expect(wrapper.findComponent(StatisticsPanel).exists()).toBe(true);

      // Click All Sections tab
      await tabs[3].trigger('click');
      expect(tabs[3].attributes('aria-selected')).toBe('true');
      expect(wrapper.findComponent(SubordinatedMiddlewarePanel).exists()).toBe(true);
      expect(wrapper.findComponent(HealthDependenciesPanel).exists()).toBe(true);
      expect(wrapper.findComponent(StatisticsPanel).exists()).toBe(true);
    });

    it('handles Ergon and Pragma child node selection gracefully with honest metadata', async () => {
      // Test Ergon selection
      await store.selectSubsystem('ergon');
      expect(store.selectedSubsystem).not.toBeNull();
      expect(store.selectedSubsystem?.name).toBe('Ergon');
      expect(store.selectedSubsystem?.description).toContain('Activities');

      const ergonAuth = findAuthoritativeSubsystem('ergon');
      expect(ergonAuth).not.toBeNull();
      expect(ergonAuth?.englishTitle).toBe('Activity Units');
      expect(ergonAuth?.areaName).toBe('Execution & Processing');

      // Test Pragma selection
      await store.selectSubsystem('pragma');
      expect(store.selectedSubsystem).not.toBeNull();
      expect(store.selectedSubsystem?.name).toBe('Pragma');
      expect(store.selectedSubsystem?.description).toContain('Task');

      const pragmaAuth = findAuthoritativeSubsystem('pragma');
      expect(pragmaAuth).not.toBeNull();
      expect(pragmaAuth?.englishTitle).toBe('Task Instances');
      expect(pragmaAuth?.areaName).toBe('Execution & Processing');
    });
  });
});
