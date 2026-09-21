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

import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import type { QueueSummary, TimeSeriesPoint } from '../models/operations';
import { operationsApi } from '../api/operationsClient';
import {
  IDLE_STATE,
  describeFailure,
  empty as emptyState,
  loaded as loadedState,
  loading as loadingState,
  unavailable as unavailableState,
  type LoadState
} from '../models/loadState';

export const useQueueStore = defineStore('queues', () => {
  // --------------------------------------------------------------------------
  // State
  // --------------------------------------------------------------------------
  const queues = ref<QueueSummary[]>([]);
  const selectedQueue = ref<QueueSummary | null>(null);
  const isDrawerOpen = ref<boolean>(false);
  const loading = ref<boolean>(false);
  const refreshing = ref<boolean>(false);
  const error = ref<string | null>(null);
  const isStale = ref<boolean>(false);
  const lastRefreshed = ref<Date | null>(null);

  const searchQuery = ref<string>('');
  const statusFilter = ref<string>('ALL');

  const queuesState = ref<LoadState>(IDLE_STATE);
  const selectedQueueError = ref<string | null>(null);

  // --------------------------------------------------------------------------
  // Computed Properties
  // --------------------------------------------------------------------------
  const totalQueues = computed(() => queues.value.length);

  const messagesInFlight = computed(() => {
    return queues.value.reduce((acc, q) => acc + (q.depth != null ? q.depth : 0), 0);
  });

  const totalConsumers = computed(() => {
    return queues.value.reduce((acc, q) => acc + (q.consumerCount != null ? q.consumerCount : 0), 0);
  });

  const totalProducers = computed(() => {
    return queues.value.reduce((acc, q) => acc + (q.producerCount != null ? q.producerCount : 0), 0);
  });

  const totalDlqDepth = computed(() => {
    const dlqQueue = queues.value.find(q => q.queueId === 'petasos.queue.dlq' || q.queueName === 'petasos.queue.dlq');
    const directDlq = dlqQueue && dlqQueue.depth != null ? dlqQueue.depth : 0;
    const sumDlq = queues.value.reduce((acc, q) => acc + (q.dlqDepth != null ? q.dlqDepth : 0), 0);
    return Math.max(directDlq, sumDlq);
  });

  /**
   * Broker facts, derived only from what the queues payload actually supplies.
   *
   * The operations API does not currently return broker identity, version or
   * transport URL, so those remain null and the presentation tier must render
   * an explicit "unknown" rather than a plausible-looking topology string.
   */
  const brokerFacts = computed(() => {
    const payload = queues.value as unknown as Array<Record<string, any>>;
    const first = (field: string): string | null => {
      for (const q of payload) {
        const value = q?.[field];
        if (typeof value === 'string' && value.trim()) return value.trim();
      }
      return null;
    };
    const addresses = new Set(
      queues.value.map(q => q.address).filter((a): a is string => Boolean(a && a.trim()))
    );
    return {
      brokerName: first('brokerName'),
      brokerVersion: first('brokerVersion'),
      brokerUrl: first('brokerUrl'),
      addressCount: addresses.size
    };
  });

  /** Null when the backend supplied no broker identity. Never fabricated. */
  const brokerTopology = computed<string | null>(() => {
    const facts = brokerFacts.value;
    const parts = [facts.brokerName, facts.brokerVersion, facts.brokerUrl].filter(Boolean);
    return parts.length > 0 ? parts.join(' / ') : null;
  });

  const isBrokerTopologyKnown = computed(() => brokerTopology.value !== null);

  const filteredQueues = computed(() => {
    let result = queues.value;

    if (statusFilter.value && statusFilter.value !== 'ALL') {
      const sf = statusFilter.value.toUpperCase();
      result = result.filter(q => (q.status || '').toUpperCase() === sf);
    }

    if (searchQuery.value && searchQuery.value.trim()) {
      const q = searchQuery.value.toLowerCase().trim();
      result = result.filter(item => {
        const nameMatch = item.queueName && item.queueName.toLowerCase().includes(q);
        const addrMatch = item.address && item.address.toLowerCase().includes(q);
        const capMatch = item.associatedCapability && item.associatedCapability.toLowerCase().includes(q);
        const idMatch = item.queueId && item.queueId.toLowerCase().includes(q);
        return nameMatch || addrMatch || capMatch || idMatch;
      });
    }

    return result;
  });

  // --------------------------------------------------------------------------
  // Actions
  // --------------------------------------------------------------------------
  async function fetchQueues(background: boolean = false) {
    if (background) {
      refreshing.value = true;
    } else {
      loading.value = true;
    }
    error.value = null;
    queuesState.value = loadingState();

    try {
      const data = await operationsApi.getQueues();
      queues.value = Array.isArray(data) ? data : [];
      isStale.value = false;
      lastRefreshed.value = new Date();
      queuesState.value = queues.value.length > 0
        ? loadedState(lastRefreshed.value)
        : emptyState(lastRefreshed.value);

      // If drawer was open for a queue, refresh selectedQueue reference
      if (selectedQueue.value) {
        const found = queues.value.find(q => q.queueId === selectedQueue.value?.queueId);
        if (found) {
          selectedQueue.value = found;
        }
      }
    } catch (err: any) {
      const message = describeFailure(err, 'queue activity');
      queuesState.value = unavailableState(message);
      error.value = message;
      isStale.value = true;
    } finally {
      loading.value = false;
      refreshing.value = false;
    }
  }

  async function selectQueue(queueId: string) {
    selectedQueueError.value = null;
    const existing = queues.value.find(q => q.queueId === queueId || q.queueName === queueId);
    if (existing) {
      selectedQueue.value = existing;
      isDrawerOpen.value = true;
      return;
    }

    try {
      const item = await operationsApi.getQueue(queueId);
      selectedQueue.value = item;
      isDrawerOpen.value = true;
    } catch (err) {
      selectedQueueError.value = describeFailure(err, `queue ${queueId}`);
    }
  }

  function closeDrawer() {
    isDrawerOpen.value = false;
    selectedQueue.value = null;
    selectedQueueError.value = null;
  }

  function setSearchQuery(query: string) {
    searchQuery.value = query;
  }

  function setStatusFilter(filter: string) {
    statusFilter.value = filter;
  }

  return {
    queues,
    selectedQueue,
    isDrawerOpen,
    loading,
    refreshing,
    error,
    isStale,
    lastRefreshed,
    searchQuery,
    statusFilter,
    queuesState,
    selectedQueueError,
    brokerFacts,
    brokerTopology,
    isBrokerTopologyKnown,
    totalQueues,
    messagesInFlight,
    totalConsumers,
    totalProducers,
    totalDlqDepth,
    filteredQueues,
    fetchQueues,
    selectQueue,
    closeDrawer,
    setSearchQuery,
    setStatusFilter
  };
});
