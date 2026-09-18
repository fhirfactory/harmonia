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
import AlertSeverityTabs from '../components/alerts/AlertSeverityTabs.vue';
import AlertTable from '../components/alerts/AlertTable.vue';
import AlertsView from '../views/AlertsView.vue';
import type { OperationalAlert } from '../models/operations';

vi.mock('../api/operationsClient', () => ({
  operationsApi: {
    getAlerts: vi.fn(),
    acknowledgeAlert: vi.fn(),
    getSummary: vi.fn(),
    getSubsystems: vi.fn(),
    getSubsystemInstances: vi.fn(),
    getSubsystemHealth: vi.fn(),
    getSubsystemStatistics: vi.fn()
  }
}));

describe('Alerts Perspective Components', () => {
  let store: ReturnType<typeof useOperationsStore>;

  const mockAlerts: OperationalAlert[] = [
    {
      alertId: 'alert-crit-1',
      severity: 'CRITICAL',
      subsystem: 'petasos',
      component: 'petasos.queue.dlq',
      condition: 'Dead Letter Queue depth exceeds threshold (> 50 messages)',
      firstObserved: 1700000000000,
      lastObserved: 1700000005000,
      duration: '5m',
      status: 'ACTIVE',
      relatedResource: 'Queue/petasos.queue.dlq',
      operatorGuidance: 'Check consumer logs and verify Artemis broker connection.'
    },
    {
      alertId: 'alert-warn-1',
      severity: 'WARNING',
      subsystem: 'energeia',
      component: 'ponos-worker-0',
      condition: 'High task retry rate detected on IngressValidationErgon',
      firstObserved: 1700000001000,
      lastObserved: 1700000006000,
      duration: '2m',
      status: 'ACKNOWLEDGED',
      relatedResource: 'TaskSequence/seq-1',
      operatorGuidance: 'Inspect payload validation rules in Calliope.'
    },
    {
      alertId: 'alert-info-1',
      severity: 'INFORMATION',
      subsystem: 'iris',
      component: 'iris-befe',
      condition: 'Cache sync completed successfully',
      firstObserved: 1700000002000,
      lastObserved: 1700000007000,
      duration: '1m',
      status: 'RESOLVED',
      operatorGuidance: 'No action required.'
    }
  ];

  beforeEach(() => {
    setActivePinia(createPinia());
    store = useOperationsStore();
    vi.clearAllMocks();
    (operationsApi.getAlerts as any).mockResolvedValue([]);
    (operationsApi.getSummary as any).mockResolvedValue({
      platformStatus: 'HEALTHY',
      criticalAlerts: 1,
      warningAlerts: 1
    });
  });

  describe('AlertSeverityTabs.vue', () => {
    it('renders severity tabs with count badges', () => {
      const wrapper = mount(AlertSeverityTabs, {
        props: {
          selectedSeverity: 'ALL',
          criticalCount: 1,
          warningCount: 1,
          infoCount: 1,
          totalCount: 3
        }
      });

      const text = wrapper.text();
      expect(text).toContain('All Severities');
      expect(text).toContain('Critical');
      expect(text).toContain('Warning');
      expect(text).toContain('Info');
      expect(text).toContain('3'); // total
    });

    it('emits update:severity when a tab is clicked', async () => {
      const wrapper = mount(AlertSeverityTabs, {
        props: {
          selectedSeverity: 'ALL'
        }
      });

      const critTab = wrapper.findAll('button[role="tab"]').find(b => b.text().includes('Critical'));
      expect(critTab).toBeDefined();
      await critTab!.trigger('click');

      expect(wrapper.emitted('update:severity')).toBeTruthy();
      expect(wrapper.emitted('update:severity')![0][0]).toBe('CRITICAL');
    });

    it('emits update:status when status dropdown changes', async () => {
      const wrapper = mount(AlertSeverityTabs);

      const statusSelect = wrapper.find('select#alert-filter-status');
      await statusSelect.setValue('ACTIVE');

      expect(wrapper.emitted('update:status')).toBeTruthy();
      expect(wrapper.emitted('update:status')![0][0]).toBe('ACTIVE');
    });
  });

  describe('AlertTable.vue', () => {
    it('renders honest empty state when no alerts exist', () => {
      const wrapper = mount(AlertTable, {
        props: {
          alerts: [],
          loading: false
        }
      });

      expect(wrapper.text()).toContain('No Active Alerts');
      expect(wrapper.text()).toContain('All Harmonia platform subsystems are operating nominally.');
    });

    it('renders alert rows with severity badge, guidance, and status', () => {
      const wrapper = mount(AlertTable, {
        props: {
          alerts: mockAlerts,
          loading: false
        }
      });

      const text = wrapper.text();
      expect(text).toContain('CRITICAL');
      expect(text).toContain('PETASOS');
      expect(text).toContain('Dead Letter Queue depth exceeds threshold');
      expect(text).toContain('Check consumer logs and verify Artemis broker connection.');
      expect(text).toContain('WARNING');
      expect(text).toContain('ENERGEIA');
    });

    it('emits acknowledge when Acknowledge button is clicked for ACTIVE alert', async () => {
      const wrapper = mount(AlertTable, {
        props: {
          alerts: mockAlerts,
          loading: false
        }
      });

      const ackBtn = wrapper.find('button.btn-secondary');
      expect(ackBtn.exists()).toBe(true);
      expect(ackBtn.text()).toContain('Acknowledge');

      await ackBtn.trigger('click');

      expect(wrapper.emitted('acknowledge')).toBeTruthy();
      expect(wrapper.emitted('acknowledge')![0][0]).toBe('alert-crit-1');
    });

    it('displays acknowledged text for already acknowledged alerts', () => {
      const wrapper = mount(AlertTable, {
        props: {
          alerts: [mockAlerts[1]], // ACKNOWLEDGED alert
          loading: false
        }
      });

      expect(wrapper.text()).toContain('Acknowledged');
      expect(wrapper.find('button.btn-secondary').exists()).toBe(false);
    });
  });

  describe('AlertsView.vue', () => {
    it('mounts, fetches alerts, and renders critical and warning count header badges', async () => {
      (operationsApi.getAlerts as any).mockResolvedValue(mockAlerts);

      const wrapper = mount(AlertsView);
      await flushPromises();

      const text = wrapper.text();
      expect(text).toContain('Operational Alerts');
      expect(text).toContain('1 Critical');
      expect(text).toContain('1 Warning');
      expect(text).toContain('Dead Letter Queue depth exceeds threshold');
    });

    it('filters alerts when severity is changed', async () => {
      (operationsApi.getAlerts as any).mockResolvedValue(mockEventsAlerts());

      const wrapper = mount(AlertsView);
      await flushPromises();

      // Click Critical tab
      const critTab = wrapper.findAll('button[role="tab"]').find(b => b.text().includes('Critical'));
      await critTab!.trigger('click');
      await flushPromises();

      expect(wrapper.text()).toContain('CRITICAL');
      expect(wrapper.text()).not.toContain('High task retry rate detected');
    });

    it('acknowledges an alert and updates status', async () => {
      (operationsApi.getAlerts as any).mockResolvedValue([mockAlerts[0]]);
      (operationsApi.acknowledgeAlert as any).mockResolvedValue({
        status: 'ACKNOWLEDGED',
        alertId: 'alert-crit-1'
      });

      const wrapper = mount(AlertsView);
      await flushPromises();

      const ackBtn = wrapper.find('button[data-testid="acknowledge-btn"]');
      await ackBtn.trigger('click');
      await flushPromises();

      expect(operationsApi.acknowledgeAlert).toHaveBeenCalledWith('alert-crit-1');
      expect(store.alerts[0].status).toBe('ACKNOWLEDGED');
    });
  });

  function mockEventsAlerts(): OperationalAlert[] {
    return [
      mockAlerts[0], // CRITICAL
      mockAlerts[1]  // WARNING
    ];
  }
});
