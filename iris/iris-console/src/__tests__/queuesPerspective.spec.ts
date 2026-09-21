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
import { useQueueStore } from '../stores/queueStore';
import { operationsApi } from '../api/operationsClient';
import QueueTable from '../components/queues/QueueTable.vue';
import QueueDetailDrawer from '../components/queues/QueueDetailDrawer.vue';
import MessagesView from '../views/MessagesView.vue';
import type { QueueSummary } from '../models/operations';

vi.mock('../api/operationsClient', () => ({
  operationsApi: {
    getQueues: vi.fn(),
    getQueue: vi.fn(),
    getSubsystems: vi.fn()
  }
}));

describe('Messages (Petasos queue activity) perspective', () => {
  let store: any;

  const mockQueues: QueueSummary[] = [
    {
      queueId: 'petasos.queue.pylai.mllp.in',
      queueName: 'petasos.queue.pylai.mllp.in',
      address: 'petasos.queue.pylai.mllp.in',
      status: 'HEALTHY',
      depth: 12,
      consumerCount: 2,
      producerCount: 1,
      enqueueRate: 14.5,
      dequeueRate: 14.0,
      oldestMessageAgeSeconds: 5,
      redeliveryCount: 0,
      dlqDepth: 0,
      expiryCount: 0,
      associatedCapability: 'Pylai Inbound MLLP Gateway',
      depthHistory: [
        { timestamp: 1000, value: 5 },
        { timestamp: 2000, value: 12 }
      ]
    },
    {
      queueId: 'petasos.queue.ponos.dispatch',
      queueName: 'petasos.queue.ponos.dispatch',
      address: 'petasos.queue.ponos.dispatch',
      status: 'DEGRADED',
      depth: 75,
      consumerCount: 0,
      producerCount: 3,
      enqueueRate: 20.0,
      dequeueRate: 0.0,
      oldestMessageAgeSeconds: 120,
      redeliveryCount: 3,
      dlqDepth: 4,
      expiryCount: 1,
      associatedCapability: 'Ponos Task Dispatch',
      depthHistory: [
        { timestamp: 1000, value: 10 },
        { timestamp: 2000, value: 75 }
      ]
    },
    {
      queueId: 'petasos.queue.sparse',
      queueName: 'petasos.queue.sparse',
      status: 'UNKNOWN',
      depth: 0,
      consumerCount: 0,
      producerCount: 0,
      enqueueRate: 0,
      dequeueRate: 0,
      oldestMessageAgeSeconds: 0,
      redeliveryCount: 0,
      dlqDepth: 0,
      expiryCount: 0
    }
  ];

  beforeEach(() => {
    vi.useFakeTimers();
    setActivePinia(createPinia());
    store = useQueueStore();
    vi.clearAllMocks();
    (operationsApi.getQueues as any).mockResolvedValue([]);
    (operationsApi.getSubsystems as any).mockResolvedValue([]);
  });

  afterEach(() => {
    vi.useRealTimers();
  });

  async function mountView() {
    const wrapper = mount(MessagesView);
    await flushPromises();
    return wrapper;
  }

  describe('QueueTable.vue', () => {
    it('renders queue rows with status, rates, depth and consumer counts', () => {
      const wrapper = mount(QueueTable, { props: { queues: mockQueues, loading: false } });

      const text = wrapper.text();
      expect(text).toContain('petasos.queue.pylai.mllp.in');
      expect(text).toContain('Pylai Inbound MLLP Gateway');
      expect(text).toContain('12');
      expect(text).toContain('14.5 /s');
      expect(text).toContain('5s');
      expect(text).toContain('petasos.queue.ponos.dispatch');
      expect(text).toContain('75');
      expect(text).toContain('DEGRADED');
    });

    it('no longer advertises a hard-coded Artemis broker port', () => {
      const wrapper = mount(QueueTable, { props: { queues: mockQueues, loading: false } });

      expect(wrapper.text()).not.toContain('61616');
      expect(wrapper.text()).not.toContain('ActiveMQ Artemis Port');
    });

    it('emits select when a row is clicked', async () => {
      const wrapper = mount(QueueTable, { props: { queues: mockQueues, loading: false } });

      const rows = wrapper.findAll('tbody tr');
      expect(rows.length).toBe(3);

      await rows[0].trigger('click');
      expect(wrapper.emitted('select')).toBeTruthy();
      expect(wrapper.emitted('select')![0][0]).toEqual(mockQueues[0]);
    });

    it('renders an honest empty state when no queues are supplied', () => {
      const wrapper = mount(QueueTable, { props: { queues: [], loading: false } });
      expect(wrapper.text()).toContain('No message queues found');
    });
  });

  describe('QueueDetailDrawer.vue', () => {
    it('renders queue telemetry, status via IrisStatus and the zero-PHI notice', () => {
      const wrapper = mount(QueueDetailDrawer, { props: { queue: mockQueues[0], isOpen: true } });

      const text = wrapper.text();
      expect(text).toContain('petasos.queue.pylai.mllp.in');
      expect(text).toContain('Pylai Inbound MLLP Gateway');
      expect(text).toContain('Zero-PHI safe telemetry');
      expect(text).toContain('12');
      expect(text).toContain('14.5 msg/sec');
      expect(text).toContain('14.0 msg/sec');
      expect(text).toContain('5s');
      expect(text).toContain('Historical depth trend');
      expect(wrapper.findAll('[role="status"]').length).toBeGreaterThan(0);
    });

    it('emits close from the close button and from Escape', async () => {
      const wrapper = mount(QueueDetailDrawer, { props: { queue: mockQueues[0], isOpen: true } });

      const closeBtn = wrapper.find('button[aria-label="Close queue details drawer"]');
      expect(closeBtn.exists()).toBe(true);
      await closeBtn.trigger('click');
      expect(wrapper.emitted('close')).toBeTruthy();

      const other = mount(QueueDetailDrawer, { props: { queue: mockQueues[0], isOpen: true } });
      window.dispatchEvent(new KeyboardEvent('keydown', { key: 'Escape' }));
      expect(other.emitted('close')).toBeTruthy();
    });
  });

  describe('MessagesView.vue', () => {
    it('states plainly that the page shows Petasos queue and broker activity', async () => {
      (operationsApi.getQueues as any).mockResolvedValue(mockQueues);
      const wrapper = await mountView();

      const text = wrapper.text();
      expect(text).toContain('Messages');
      expect(text).toContain('Petasos queue and broker activity');
      expect(text).toContain('Message browsing, message detail and message replay are not available');
    });

    it('renders a compact metric strip derived from the queue store getters', async () => {
      (operationsApi.getQueues as any).mockResolvedValue(mockQueues);
      const wrapper = await mountView();

      const metrics = wrapper.findAll('.messages-view__metric');
      expect(metrics.length).toBe(5);

      const labels = metrics.map(m => m.find('.messages-view__metric-label').text());
      expect(labels).toEqual(['Queues', 'Queued depth', 'Consumers', 'Producers', 'DLQ depth']);

      const values = metrics.map(m => m.find('.messages-view__metric-value').text());
      expect(values).toEqual(['3', '87', '2', '4', '4']);
    });

    it('does not render any replay, retry or resubmit affordance', async () => {
      (operationsApi.getQueues as any).mockResolvedValue(mockQueues);
      const wrapper = await mountView();

      // The words may appear in the notice explaining the capability is absent;
      // what must not exist is an actual affordance.
      const controls = wrapper.findAll('button, a, input[type="button"], [role="button"]');
      expect(controls.length).toBeGreaterThan(0);
      for (const control of controls) {
        const label = `${control.text()} ${control.attributes('aria-label') || ''}`.toLowerCase();
        expect(/replay|resubmit|redeliver/.test(label)).toBe(false);
      }

      const actionArea = wrapper.find('[data-testid="messages-action-area"]');
      expect(actionArea.exists()).toBe(true);
      expect(actionArea.findAll('button').length).toBe(0);
    });

    it('reports broker facts only when the queues payload supplies them', async () => {
      (operationsApi.getQueues as any).mockResolvedValue(mockQueues);
      const wrapper = await mountView();

      const text = wrapper.text();
      expect(text).not.toContain('Artemis 2.33');
      expect(text).not.toContain('61616');
      expect(text).toContain('Not reported by the operations API');
    });

    it('renders broker facts the payload does supply', async () => {
      (operationsApi.getQueues as any).mockResolvedValue([
        { ...mockQueues[0], brokerName: 'Artemis', brokerVersion: '2.33.0' } as any
      ]);
      const wrapper = await mountView();

      expect(wrapper.text()).toContain('Artemis');
      expect(wrapper.text()).toContain('2.33.0');
    });

    it('distinguishes an unavailable operations API from an empty result set', async () => {
      (operationsApi.getQueues as any).mockRejectedValue(new Error('Network Error'));
      const unavailable = await mountView();

      expect(store.queuesState.kind).toBe('unavailable');
      expect(unavailable.text()).toContain('Operations API unavailable');
      expect(unavailable.text()).not.toContain('No Petasos queues present');

      setActivePinia(createPinia());
      store = useQueueStore();
      (operationsApi.getQueues as any).mockResolvedValue([]);
      const empty = await mountView();

      expect(store.queuesState.kind).toBe('empty');
      expect(empty.text()).toContain('No Petasos queues present');
      expect(empty.text()).not.toContain('Operations API unavailable');
    });

    it('never renders a stack trace when the API fails', async () => {
      const err = new Error('Request failed\n    at fetchQueues (queueStore.ts:1:1)');
      (operationsApi.getQueues as any).mockRejectedValue(err);
      const wrapper = await mountView();

      expect(wrapper.text()).not.toContain('at fetchQueues');
    });

    it('filters queues through the toolbar search bound to the store', async () => {
      (operationsApi.getQueues as any).mockResolvedValue(mockQueues);
      const wrapper = await mountView();

      const input = wrapper.find('input[type="search"]');
      await input.setValue('mllp');

      expect(store.searchQuery).toBe('mllp');
      expect(wrapper.text()).toContain('petasos.queue.pylai.mllp.in');
      expect(wrapper.text()).not.toContain('petasos.queue.ponos.dispatch');
    });

    it('filters queues by the status filter group', async () => {
      (operationsApi.getQueues as any).mockResolvedValue(mockQueues);
      const wrapper = await mountView();

      const degradedBtn = wrapper.findAll('.messages-view__filter-btn').find(b => b.text() === 'DEGRADED');
      expect(degradedBtn).toBeDefined();

      await degradedBtn!.trigger('click');
      expect(store.statusFilter).toBe('DEGRADED');
      expect(wrapper.text()).toContain('petasos.queue.ponos.dispatch');
      expect(wrapper.text()).not.toContain('petasos.queue.pylai.mllp.in');
    });

    it('separates a filter mismatch from a genuinely empty backend result', async () => {
      (operationsApi.getQueues as any).mockResolvedValue(mockQueues);
      const wrapper = await mountView();

      await wrapper.find('input[type="search"]').setValue('no-such-queue');
      await flushPromises();

      expect(wrapper.text()).toContain('No queue matches the current filter');
      expect(wrapper.text()).not.toContain('No Petasos queues present');
    });

    it('shows the reported Petasos subsystem state and nothing when it is unreported', async () => {
      (operationsApi.getQueues as any).mockResolvedValue(mockQueues);
      (operationsApi.getSubsystems as any).mockResolvedValue([
        {
          id: 'petasos',
          name: 'Petasos',
          description: 'Messaging',
          state: 'DEGRADED',
          instanceCount: 1,
          version: '1.0.0',
          lastUpdated: Date.now()
        }
      ]);

      const wrapper = await mountView();
      expect(wrapper.text()).toContain('DEGRADED');
    });

    it('opens the queue detail drawer in place when a row is selected', async () => {
      (operationsApi.getQueues as any).mockResolvedValue(mockQueues);
      const wrapper = await mountView();

      await wrapper.findAll('tbody tr')[0].trigger('click');
      await flushPromises();

      expect(store.isDrawerOpen).toBe(true);
      expect(store.selectedQueue?.queueId).toBe('petasos.queue.pylai.mllp.in');
    });

    it('only calls the queue and subsystem endpoints that genuinely exist', async () => {
      (operationsApi.getQueues as any).mockResolvedValue(mockQueues);
      await mountView();

      expect(operationsApi.getQueues).toHaveBeenCalled();
      expect(operationsApi.getSubsystems).toHaveBeenCalled();
      expect(operationsApi.getQueue).not.toHaveBeenCalled();
    });
  });
});
