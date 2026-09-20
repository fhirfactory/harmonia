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
import { useQueueStore } from '../stores/queueStore';
import { operationsApi } from '../api/operationsClient';
import QueueTable from '../components/queues/QueueTable.vue';
import QueueDetailDrawer from '../components/queues/QueueDetailDrawer.vue';
import QueuesView from '../views/QueuesView.vue';
import type { QueueSummary } from '../models/operations';

vi.mock('../api/operationsClient', () => ({
  operationsApi: {
    getQueues: vi.fn(),
    getQueue: vi.fn(),
    getSubsystems: vi.fn()
  }
}));

describe('Queues Perspective Components', () => {
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
    setActivePinia(createPinia());
    store = useQueueStore();
    vi.clearAllMocks();
    (operationsApi.getQueues as any).mockResolvedValue([]);
    (operationsApi.getSubsystems as any).mockResolvedValue([]);
  });

  describe('QueueTable.vue', () => {
    it('renders queue rows with status badge, rates, depth, and consumer counts', () => {
      const wrapper = mount(QueueTable, {
        props: {
          queues: mockQueues,
          loading: false
        }
      });

      const text = wrapper.text();
      expect(text).toContain('petasos.queue.pylai.mllp.in');
      expect(text).toContain('Pylai Inbound MLLP Gateway');
      expect(text).toContain('12');
      expect(text).toContain('14.5 /s');
      expect(text).toContain('5s');

      expect(text).toContain('petasos.queue.ponos.dispatch');
      expect(text).toContain('75');
      expect(text).toContain('DEGRADED');
      expect(text).toContain('4'); // DLQ
    });

    it('emits select event when a row or details button is clicked', async () => {
      const wrapper = mount(QueueTable, {
        props: {
          queues: mockQueues,
          loading: false
        }
      });

      const rows = wrapper.findAll('tbody tr');
      expect(rows.length).toBe(3);

      await rows[0].trigger('click');
      expect(wrapper.emitted('select')).toBeTruthy();
      expect(wrapper.emitted('select')![0][0]).toEqual(mockQueues[0]);
    });

    it('renders honest empty state when queues array is empty', () => {
      const wrapper = mount(QueueTable, {
        props: {
          queues: [],
          loading: false
        }
      });

      expect(wrapper.text()).toContain('No message queues found');
    });
  });

  describe('QueueDetailDrawer.vue', () => {
    it('renders safe queue operational telemetry and zero-PHI boundary notice', () => {
      const wrapper = mount(QueueDetailDrawer, {
        props: {
          queue: mockQueues[0],
          isOpen: true
        }
      });

      const text = wrapper.text();
      expect(text).toContain('petasos.queue.pylai.mllp.in');
      expect(text).toContain('Pylai Inbound MLLP Gateway');
      expect(text).toContain('Zero-PHI Safe Telemetry');
      expect(text).toContain('12'); // Current depth
      expect(text).toContain('14.5 msg/sec'); // Enqueue rate
      expect(text).toContain('14.0 msg/sec'); // Dequeue rate
      expect(text).toContain('5s'); // Oldest age
      expect(text).toContain('Historical Depth Trend');
    });

    it('emits close event when the close button is clicked', async () => {
      const wrapper = mount(QueueDetailDrawer, {
        props: {
          queue: mockQueues[0],
          isOpen: true
        }
      });

      const closeBtn = wrapper.find('button[aria-label="Close queue details drawer"]');
      expect(closeBtn.exists()).toBe(true);

      await closeBtn.trigger('click');
      expect(wrapper.emitted('close')).toBeTruthy();
    });

    it('emits close event when Escape key is pressed', async () => {
      const wrapper = mount(QueueDetailDrawer, {
        props: {
          queue: mockQueues[0],
          isOpen: true
        }
      });

      window.dispatchEvent(new KeyboardEvent('keydown', { key: 'Escape' }));
      expect(wrapper.emitted('close')).toBeTruthy();
    });
  });

  describe('QueuesView.vue', () => {
    it('derives the Petasos identity status from live subsystem telemetry', async () => {
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

      const wrapper = mount(QueuesView);
      await flushPromises();

      const identityStatus = wrapper.findAll('[role="status"]')[0];
      expect(identityStatus.text()).toContain('Degraded');
      expect(identityStatus.attributes('aria-label')).toBe('Degraded operational warning');
    });

    it('renders summary cards with broker topology, queues count, in-flight messages, and DLQ depth', async () => {
      (operationsApi.getQueues as any).mockResolvedValue(mockQueues);
      const wrapper = mount(QueuesView);
      await flushPromises();

      const text = wrapper.text();
      expect(text).toContain('Broker Topology');
      expect(text).toContain('Artemis 2.33 Core');
      expect(text).toContain('Total Queues');
      expect(text).toContain('3');
      expect(text).toContain('Messages In Flight');
      expect(text).toContain('87'); // 12 + 75
      expect(text).toContain('Active Consumers');
      expect(text).toContain('2');
      expect(text).toContain('DLQ Depth');
      expect(text).toContain('4');
    });

    it('filters queues by search query input', async () => {
      (operationsApi.getQueues as any).mockResolvedValue(mockQueues);
      const wrapper = mount(QueuesView);
      await flushPromises();

      const input = wrapper.find('input[type="text"]');
      await input.setValue('mllp');

      expect(store.searchQuery).toBe('mllp');
      expect(wrapper.text()).toContain('petasos.queue.pylai.mllp.in');
      expect(wrapper.text()).not.toContain('petasos.queue.ponos.dispatch');
    });

    it('filters queues by status tab buttons', async () => {
      (operationsApi.getQueues as any).mockResolvedValue(mockQueues);
      const wrapper = mount(QueuesView);
      await flushPromises();

      const degradedBtn = wrapper.findAll('button').find(b => b.text() === 'DEGRADED');
      expect(degradedBtn).toBeDefined();

      await degradedBtn!.trigger('click');
      expect(store.statusFilter).toBe('DEGRADED');
      expect(wrapper.text()).toContain('petasos.queue.ponos.dispatch');
      expect(wrapper.text()).not.toContain('petasos.queue.pylai.mllp.in');
    });
  });
});
