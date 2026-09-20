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
import { useEventStore } from '../stores/eventStore';
import { operationsApi } from '../api/operationsClient';
import type { OperationalEvent } from '../models/operations';

vi.mock('../api/operationsClient', () => ({
  operationsApi: {
    getEvents: vi.fn(),
    getEvent: vi.fn()
  }
}));

describe('eventStore', () => {
  let store: ReturnType<typeof useEventStore>;

  const mockEvents: OperationalEvent[] = [
    {
      eventId: 'evt-2',
      timestamp: 1700000002000,
      subsystem: 'petasos',
      eventType: 'QUEUE_PUBLISH',
      operation: 'Artemis Enqueue',
      status: 'SUCCESS',
      durationMs: 15,
      correlationId: 'corr-101',
      messageId: 'msg-202'
    },
    {
      eventId: 'evt-1',
      timestamp: 1700000001000,
      subsystem: 'pylai',
      eventType: 'MLLP_INGRESS',
      operation: 'MLLP Receive HL7',
      status: 'SUCCESS',
      durationMs: 42,
      correlationId: 'corr-101',
      messageId: 'msg-101'
    },
    {
      eventId: 'evt-3',
      timestamp: 1700000003000,
      subsystem: 'energeia',
      eventType: 'ACTIVITY_EXEC',
      operation: 'PatientResolutionErgon',
      status: 'WARNING',
      durationMs: 95,
      correlationId: 'corr-101',
      pragmaId: 'pragma-303'
    },
    {
      eventId: 'evt-4',
      timestamp: 1700000004000,
      subsystem: 'mnemosyne',
      eventType: 'FHIR_COMMIT',
      operation: 'PostgreSQL Commit',
      status: 'FAILURE',
      durationMs: 110,
      correlationId: 'corr-101',
      reasonCode: 'ERR_POSTGRES_TIMEOUT'
    }
  ];

  beforeEach(() => {
    setActivePinia(createPinia());
    store = useEventStore();
    vi.clearAllMocks();
  });

  it('initializes with default filters and honest empty events', () => {
    expect(store.events).toEqual([]);
    expect(store.selectedEvent).toBeNull();
    expect(store.isEventDrawerOpen).toBe(false);
    expect(store.correlationId).toBe('');
    expect(store.subsystem).toBe('ALL');
    expect(store.status).toBe('ALL');
    expect(store.timePreset).toBe('1h');
    expect(store.loading).toBe(false);
    expect(store.hasSearched).toBe(false);
    expect(store.totalEvents).toBe(0);
  });

  it('fetches events and populates store without fabricated seeds', async () => {
    (operationsApi.getEvents as any).mockResolvedValueOnce(mockEvents);

    await store.fetchEvents();

    expect(operationsApi.getEvents).toHaveBeenCalledTimes(1);
    expect(store.events).toHaveLength(4);
    expect(store.totalEvents).toBe(4);
    expect(store.hasSearched).toBe(true);
  });

  it('computes chronological order ascending and status counters', async () => {
    (operationsApi.getEvents as any).mockResolvedValueOnce(mockEvents);

    await store.fetchEvents();

    // Chronological order should have evt-1 first (timestamp 1000) then evt-2 (2000), evt-3 (3000), evt-4 (4000)
    expect(store.chronologicalEvents[0].eventId).toBe('evt-1');
    expect(store.chronologicalEvents[1].eventId).toBe('evt-2');
    expect(store.chronologicalEvents[2].eventId).toBe('evt-3');
    expect(store.chronologicalEvents[3].eventId).toBe('evt-4');

    expect(store.successCount).toBe(2);
    expect(store.warningCount).toBe(1);
    expect(store.failureCount).toBe(1);
  });

  it('opens and closes event detail drawer', () => {
    store.selectEvent(mockEvents[0]);
    expect(store.selectedEvent?.eventId).toBe('evt-2');
    expect(store.isEventDrawerOpen).toBe(true);

    store.closeEventDrawer();
    expect(store.selectedEvent).toBeNull();
    expect(store.isEventDrawerOpen).toBe(false);
  });

  it('resets diagnostic filters correctly', () => {
    store.correlationId = 'test-corr';
    store.subsystem = 'petasos';
    store.status = 'FAILURE';
    store.timePreset = '15m';

    store.resetFilters();

    expect(store.correlationId).toBe('');
    expect(store.subsystem).toBe('ALL');
    expect(store.status).toBe('ALL');
    expect(store.timePreset).toBe('1h');
  });

  it('traces correlation ID across all time and executes search', async () => {
    (operationsApi.getEvents as any).mockResolvedValueOnce(mockEvents);

    await store.traceCorrelation('corr-target-999');

    expect(store.correlationId).toBe('corr-target-999');
    expect(store.timePreset).toBe('ALL');
    expect(operationsApi.getEvents).toHaveBeenCalledWith(expect.objectContaining({
      correlationId: 'corr-target-999',
      from: undefined
    }));
  });

  it('handles empty results and API errors honestly without inventing data', async () => {
    (operationsApi.getEvents as any).mockRejectedValueOnce(new Error('Network error'));

    await store.fetchEvents();

    expect(store.events).toEqual([]);
    expect(store.error).toBe('Network error');
    expect(store.loading).toBe(false);
  });
});
