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
import { createPinia, setActivePinia } from 'pinia';
import { useQueueStore } from '../stores/queueStore';
import { operationsApi } from '../api/operationsClient';
import type { QueueSummary } from '../models/operations';

vi.mock('../api/operationsClient', () => ({
  operationsApi: {
    getQueues: vi.fn(),
    getQueue: vi.fn()
  }
}));

describe('useQueueStore', () => {
  let store: any;

  beforeEach(() => {
    setActivePinia(createPinia());
    store = useQueueStore();
    vi.clearAllMocks();
  });

  it('initializes with default values and handles empty queues telemetry honestly', async () => {
    (operationsApi.getQueues as any).mockResolvedValue([]);
    await store.fetchQueues();

    expect(store.queues.length).toBe(0);
    expect(store.totalQueues).toBe(0);
    expect(store.brokerTopology).toContain('Artemis');
    expect(store.isDrawerOpen).toBe(false);
    expect(store.selectedQueue).toBeNull();
    expect(store.error).toBeNull();
  });

  it('populates queues when operationsApi returns queue telemetry', async () => {
    const mockData: QueueSummary[] = [
      {
        queueId: 'petasos.queue.pylai.mllp.in',
        queueName: 'petasos.queue.pylai.mllp.in',
        address: 'petasos.queue.pylai.mllp.in',
        status: 'HEALTHY',
        depth: 5,
        consumerCount: 2,
        producerCount: 1,
        enqueueRate: 10.0,
        dequeueRate: 10.0,
        oldestMessageAgeSeconds: 0,
        redeliveryCount: 0,
        dlqDepth: 0,
        expiryCount: 0
      }
    ];
    (operationsApi.getQueues as any).mockResolvedValue(mockData);
    await store.fetchQueues();

    expect(store.queues.length).toBe(1);
    expect(store.totalQueues).toBe(1);
    expect(store.messagesInFlight).toBe(5);
  });

  it('computes total in-flight messages, consumers, and DLQ depth', () => {
    store.queues = [
      {
        queueId: 'q1',
        queueName: 'q1',
        depth: 25,
        consumerCount: 2,
        producerCount: 1,
        dlqDepth: 0
      },
      {
        queueId: 'q2',
        queueName: 'q2',
        depth: 35,
        consumerCount: 3,
        producerCount: 2,
        dlqDepth: 5
      }
    ] as QueueSummary[];

    expect(store.messagesInFlight).toBe(60);
    expect(store.totalConsumers).toBe(5);
    expect(store.totalProducers).toBe(3);
    expect(store.totalDlqDepth).toBe(5);
  });

  it('filters queues by search query and status filter', () => {
    store.queues = [
      { queueId: 'petasos.queue.pylai.mllp.in', queueName: 'petasos.queue.pylai.mllp.in', status: 'HEALTHY' },
      { queueId: 'petasos.queue.ponos.dispatch', queueName: 'petasos.queue.ponos.dispatch', status: 'DEGRADED' },
      { queueId: 'petasos.queue.agora.inbound', queueName: 'petasos.queue.agora.inbound', status: 'HEALTHY' }
    ] as QueueSummary[];

    store.setSearchQuery('pylai');
    expect(store.filteredQueues.length).toBe(1);
    expect(store.filteredQueues[0].queueId).toBe('petasos.queue.pylai.mllp.in');

    store.setSearchQuery('');
    store.setStatusFilter('DEGRADED');
    expect(store.filteredQueues.length).toBe(1);
    expect(store.filteredQueues[0].queueId).toBe('petasos.queue.ponos.dispatch');

    store.setStatusFilter('ALL');
    expect(store.filteredQueues.length).toBe(3);
  });

  it('selects queue and opens drawer, and closes drawer', async () => {
    store.queues = [
      { queueId: 'test-q', queueName: 'test-q', depth: 10 }
    ] as QueueSummary[];

    await store.selectQueue('test-q');
    expect(store.selectedQueue).toEqual(store.queues[0]);
    expect(store.isDrawerOpen).toBe(true);

    store.closeDrawer();
    expect(store.isDrawerOpen).toBe(false);
    expect(store.selectedQueue).toBeNull();
  });
});
