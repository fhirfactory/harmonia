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
import type { OperationalEvent } from '../models/operations';
import { operationsApi } from '../api/operationsClient';

export type EventTimePreset = '15m' | '1h' | '6h' | '24h' | 'ALL';

export const useEventStore = defineStore('events', () => {
  // --------------------------------------------------------------------------
  // State
  // --------------------------------------------------------------------------
  const events = ref<OperationalEvent[]>([]);
  const selectedEvent = ref<OperationalEvent | null>(null);
  const isEventDrawerOpen = ref<boolean>(false);

  // Search and Filter criteria
  const correlationId = ref<string>('');
  const causationId = ref<string>('');
  const messageId = ref<string>('');
  const pragmaId = ref<string>('');
  const subsystem = ref<string>('ALL');
  const eventType = ref<string>('ALL');
  const status = ref<string>('ALL');
  const timePreset = ref<EventTimePreset>('1h');
  const customFrom = ref<number | null>(null);
  const customTo = ref<number | null>(null);

  // Status
  const loading = ref<boolean>(false);
  const error = ref<string | null>(null);
  const hasSearched = ref<boolean>(false);

  // --------------------------------------------------------------------------
  // Computed Properties
  // --------------------------------------------------------------------------
  // Chronological timeline view: sorted by timestamp ascending to trace forward progression
  const chronologicalEvents = computed(() => {
    return [...events.value].sort((a, b) => a.timestamp - b.timestamp);
  });

  const totalEvents = computed(() => events.value.length);
  const successCount = computed(() => events.value.filter(e => e.status?.toUpperCase() === 'SUCCESS').length);
  const warningCount = computed(() => events.value.filter(e => e.status?.toUpperCase() === 'WARNING').length);
  const failureCount = computed(() => events.value.filter(e => e.status?.toUpperCase() === 'FAILURE').length);

  // --------------------------------------------------------------------------
  // Actions
  // --------------------------------------------------------------------------
  function calculateFromTimestamp(): number | undefined {
    if (customFrom.value !== null) {
      return customFrom.value;
    }
    const now = Date.now();
    switch (timePreset.value) {
      case '15m': return now - 15 * 60 * 1000;
      case '1h': return now - 60 * 60 * 1000;
      case '6h': return now - 6 * 60 * 60 * 1000;
      case '24h': return now - 24 * 60 * 60 * 1000;
      case 'ALL': return undefined;
      default: return now - 60 * 60 * 1000;
    }
  }

  const fetchEvents = async () => {
    loading.value = true;
    error.value = null;
    try {
      const fromVal = calculateFromTimestamp();
      const toVal = customTo.value !== null ? customTo.value : undefined;

      const params: Parameters<typeof operationsApi.getEvents>[0] = {
        correlationId: correlationId.value.trim() || undefined,
        causationId: causationId.value.trim() || undefined,
        messageId: messageId.value.trim() || undefined,
        pragmaId: pragmaId.value.trim() || undefined,
        subsystem: subsystem.value !== 'ALL' ? subsystem.value : undefined,
        eventType: eventType.value !== 'ALL' ? eventType.value : undefined,
        status: status.value !== 'ALL' ? status.value : undefined,
        from: fromVal,
        to: toVal,
        pageSize: 100
      };

      events.value = await operationsApi.getEvents(params);
      hasSearched.value = true;
    } catch (err: any) {
      error.value = err.message || 'Failed to fetch operational events';
      events.value = [];
    } finally {
      loading.value = false;
    }
  };

  const search = async () => {
    await fetchEvents();
  };

  const resetFilters = () => {
    correlationId.value = '';
    causationId.value = '';
    messageId.value = '';
    pragmaId.value = '';
    subsystem.value = 'ALL';
    eventType.value = 'ALL';
    status.value = 'ALL';
    timePreset.value = '1h';
    customFrom.value = null;
    customTo.value = null;
    error.value = null;
  };

  const selectEvent = (event: OperationalEvent) => {
    selectedEvent.value = event;
    isEventDrawerOpen.value = true;
  };

  const closeEventDrawer = () => {
    isEventDrawerOpen.value = false;
    selectedEvent.value = null;
  };

  const traceCorrelation = async (corrId: string) => {
    if (!corrId) return;
    resetFilters();
    correlationId.value = corrId;
    timePreset.value = 'ALL';
    await fetchEvents();
  };

  return {
    events,
    selectedEvent,
    isEventDrawerOpen,
    correlationId,
    causationId,
    messageId,
    pragmaId,
    subsystem,
    eventType,
    status,
    timePreset,
    customFrom,
    customTo,
    loading,
    error,
    hasSearched,

    chronologicalEvents,
    totalEvents,
    successCount,
    warningCount,
    failureCount,

    fetchEvents,
    search,
    resetFilters,
    selectEvent,
    closeEventDrawer,
    traceCorrelation
  };
});
