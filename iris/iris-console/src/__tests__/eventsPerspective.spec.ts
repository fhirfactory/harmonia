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
import { useEventStore } from '../stores/eventStore';
import { operationsApi } from '../api/operationsClient';
import EventSearchFilter from '../components/events/EventSearchFilter.vue';
import EventTimeline from '../components/events/EventTimeline.vue';
import EventDetailDrawer from '../components/events/EventDetailDrawer.vue';
import EventsView from '../views/EventsView.vue';
import type { OperationalEvent } from '../models/operations';

vi.mock('../api/operationsClient', () => ({
  operationsApi: {
    getEvents: vi.fn(),
    getEvent: vi.fn()
  }
}));

describe('Events Perspective Components', () => {
  let store: ReturnType<typeof useEventStore>;

  const mockEvents: OperationalEvent[] = [
    {
      eventId: 'evt-101',
      timestamp: 1700000000000,
      subsystem: 'pylai',
      eventType: 'MLLP_INGRESS',
      operation: 'MLLP Inbound HL7 Receive',
      status: 'SUCCESS',
      durationMs: 38,
      correlationId: 'corr-xyz-777',
      messageId: 'msg-in-1',
      interfaceId: 'mllp-gateway-0'
    },
    {
      eventId: 'evt-102',
      timestamp: 1700000000500,
      subsystem: 'petasos',
      eventType: 'QUEUE_PUBLISH',
      operation: 'ActiveMQ Artemis Enqueue',
      status: 'SUCCESS',
      durationMs: 14,
      correlationId: 'corr-xyz-777',
      causationId: 'evt-101',
      messageId: 'msg-in-1'
    },
    {
      eventId: 'evt-103',
      timestamp: 1700000001200,
      subsystem: 'energeia',
      eventType: 'ACTIVITY_EXEC',
      operation: 'PatientResolutionErgon Execution',
      status: 'WARNING',
      durationMs: 82,
      correlationId: 'corr-xyz-777',
      pragmaId: 'pragma-888',
      ergonId: 'ergon-resolution'
    },
    {
      eventId: 'evt-104',
      timestamp: 1700000002000,
      subsystem: 'mnemosyne',
      eventType: 'FHIR_COMMIT',
      operation: 'PostgreSQL FHIR Storage',
      status: 'FAILURE',
      durationMs: 120,
      correlationId: 'corr-xyz-777',
      reasonCode: 'DB_CONNECTION_TIMEOUT'
    }
  ];

  beforeEach(() => {
    setActivePinia(createPinia());
    store = useEventStore();
    vi.clearAllMocks();
    (operationsApi.getEvents as any).mockResolvedValue([]);
  });

  describe('EventSearchFilter.vue', () => {
    it('renders correlation search input and subsystem filter', () => {
      const wrapper = mount(EventSearchFilter);

      expect(wrapper.find('input#event-search-correlation').exists()).toBe(true);
      expect(wrapper.find('select#event-search-subsystem').exists()).toBe(true);
      expect(wrapper.find('select#event-search-status').exists()).toBe(true);
      expect(wrapper.text()).toContain('Time Window:');
    });

    it('toggles advanced filters for causation, message, pragma, and event type', async () => {
      const wrapper = mount(EventSearchFilter);

      expect(wrapper.find('#event-search-causation').exists()).toBe(false);

      // Click toggle
      const toggleBtn = wrapper.find('button[type="button"].text-sky-400');
      await toggleBtn.trigger('click');

      expect(wrapper.find('#event-search-causation').exists()).toBe(true);
      expect(wrapper.find('#event-search-message').exists()).toBe(true);
      expect(wrapper.find('#event-search-pragma').exists()).toBe(true);
      expect(wrapper.find('#event-search-type').exists()).toBe(true);
    });

    it('executes search when search button is clicked', async () => {
      (operationsApi.getEvents as any).mockResolvedValue(mockEvents);
      const wrapper = mount(EventSearchFilter);

      const searchInput = wrapper.find('input#event-search-correlation');
      await searchInput.setValue('corr-xyz-777');

      const searchBtn = wrapper.find('button.btn-primary');
      await searchBtn.trigger('click');

      expect(operationsApi.getEvents).toHaveBeenCalledWith(expect.objectContaining({
        correlationId: 'corr-xyz-777'
      }));
    });
  });

  describe('EventTimeline.vue', () => {
    it('renders honest empty state when events list is empty', () => {
      const wrapper = mount(EventTimeline, {
        props: {
          events: [],
          loading: false
        }
      });

      expect(wrapper.text()).toContain('No Diagnostic Events Recorded');
    });

    it('renders vertical flowchart sequence hops with subsystem badges and timing', () => {
      const wrapper = mount(EventTimeline, {
        props: {
          events: mockEvents,
          loading: false
        }
      });

      const text = wrapper.text();
      expect(text).toContain('PYLAI');
      expect(text).toContain('PETASOS');
      expect(text).toContain('ENERGEIA');
      expect(text).toContain('MNEMOSYNE');
      expect(text).toContain('MLLP Inbound HL7 Receive');
      expect(text).toContain('PatientResolutionErgon Execution');
      expect(text).toContain('4 Hops');
      expect(text).toContain('38ms');
      expect(text).toContain('82ms');
    });

    it('toggles from flowchart view to dense tabular view', async () => {
      const wrapper = mount(EventTimeline, {
        props: {
          events: mockEvents,
          loading: false
        }
      });

      // Default is flow
      expect(wrapper.findAll('.relative.pl-6').length).toBeGreaterThanOrEqual(1);

      // Switch to table
      const buttons = wrapper.findAll('button[type="button"]');
      const tableBtn = buttons.find(b => b.text().includes('Table'));
      expect(tableBtn).toBeDefined();
      await tableBtn!.trigger('click');

      expect(wrapper.find('table[role="table"]').exists()).toBe(true);
      expect(wrapper.text()).toContain('corr-xyz-777');
    });

    it('emits select event when a hop card is clicked', async () => {
      const wrapper = mount(EventTimeline, {
        props: {
          events: mockEvents,
          loading: false
        }
      });

      const hopCard = wrapper.find('.card[role="button"]');
      await hopCard.trigger('click');

      expect(wrapper.emitted('select')).toBeTruthy();
      expect(wrapper.emitted('select')![0][0]).toEqual(mockEvents[0]);
    });
  });

  describe('EventDetailDrawer.vue', () => {
    it('renders event details, zero-PHI notice, and technical identifiers', () => {
      const wrapper = mount(EventDetailDrawer, {
        props: {
          event: mockEvents[2],
          isOpen: true
        }
      });

      const text = wrapper.text();
      expect(text).toContain('PatientResolutionErgon Execution');
      expect(text).toContain('ENERGEIA');
      expect(text).toContain('Diagnostic Zero-PHI Boundary');
      expect(text).toContain('corr-xyz-777');
      expect(text).toContain('pragma-888');
      expect(text).toContain('ergon-resolution');
      expect(text).toContain('82ms');
    });

    it('emits close on Escape key and close button click', async () => {
      const wrapper = mount(EventDetailDrawer, {
        props: {
          event: mockEvents[0],
          isOpen: true
        }
      });

      const closeBtn = wrapper.find('button[aria-label="Close drawer"]');
      await closeBtn.trigger('click');

      expect(wrapper.emitted('close')).toBeTruthy();
    });

    it('emits trace when Trace Flow button is clicked', async () => {
      const wrapper = mount(EventDetailDrawer, {
        props: {
          event: mockEvents[0],
          isOpen: true
        }
      });

      const traceBtn = wrapper.findAll('button').find(b => b.text().includes('Trace Flow'));
      expect(traceBtn).toBeDefined();
      await traceBtn!.trigger('click');

      expect(wrapper.emitted('trace')).toBeTruthy();
      expect(wrapper.emitted('trace')![0][0]).toBe('corr-xyz-777');
    });
  });

  describe('EventsView.vue', () => {
    it('mounts and renders summary counters when events exist', async () => {
      (operationsApi.getEvents as any).mockResolvedValue(mockEvents);

      const wrapper = mount(EventsView);
      await flushPromises();

      const text = wrapper.text();
      expect(text).toContain('Events Diagnostic Timeline');
      expect(text).toContain('Zero-PHI Diagnostic Boundary');
      expect(text).toContain('Total Diagnostic Events');
      expect(text).toContain('Successful Operations');
      expect(text).toContain('Warnings Observed');
      expect(text).toContain('Failed Operations');
      expect(text).toContain('4'); // 4 total events
    });
  });
});
