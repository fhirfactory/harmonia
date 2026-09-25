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
import { useSecurityStore } from '../stores/securityStore';
import { fhirApi } from '../api/fhirClient';
import AuditEventView from '../views/AuditEventView.vue';
import type { AuditEvent } from '../models/fhir';
import viewContent from '../views/AuditEventView.vue?raw';
import storeContent from '../stores/securityStore.ts?raw';

vi.mock('../api/fhirClient', () => ({
  fhirApi: {
    search: vi.fn(),
    get: vi.fn(),
    create: vi.fn(),
    update: vi.fn(),
    delete: vi.fn()
  }
}));

describe('AuditEvent Read-Only UI & Store', () => {
  let store: ReturnType<typeof useSecurityStore>;

  const mockAuditEvents: AuditEvent[] = [
    {
      resourceType: 'AuditEvent',
      id: 'audit-001',
      action: 'R',
      severity: 'informational',
      recorded: '2026-09-24T12:00:00Z',
      code: { text: 'Patient Record Read' },
      agent: [{ who: { display: 'Dr. Jane Smith' } }]
    },
    {
      resourceType: 'AuditEvent',
      id: 'audit-002',
      action: 'C',
      severity: 'notice',
      recorded: '2026-09-24T12:05:00Z',
      code: { text: 'Consent Policy Granted' },
      agent: [{ who: { display: 'Themis Policy Engine' } }]
    },
    {
      resourceType: 'AuditEvent',
      id: 'audit-003',
      action: 'D',
      severity: 'warning',
      recorded: '2026-09-24T12:10:00Z',
      code: { text: 'Provenance Record Expired' },
      agent: [{ who: { display: 'Retention Cleaner' } }]
    }
  ];

  beforeEach(() => {
    setActivePinia(createPinia());
    store = useSecurityStore();
    vi.clearAllMocks();
    (fhirApi.search as any).mockResolvedValue([...mockAuditEvents]);
  });

  describe('AuditEventView.vue component', () => {
    it('fetches audit events on mount and renders read-only table', async () => {
      const wrapper = mount(AuditEventView);
      await flushPromises();

      expect(fhirApi.search).toHaveBeenCalledWith('AuditEvent', {});
      const text = wrapper.text();
      expect(text).toContain('Security Audit Events');
      expect(text).toContain('audit-001');
      expect(text).toContain('Patient Record Read');
      expect(text).toContain('Dr. Jane Smith');
      expect(text).toContain('READ');
      expect(text).toContain('CREATE');
      expect(text).toContain('DELETE');
    });

    it('asserts complete absence of mutation affordances (Emit/Create button, Create modal, Delete button)', async () => {
      const wrapper = mount(AuditEventView);
      await flushPromises();

      // No Emit or Create buttons
      expect(wrapper.find('button[class*="btn-primary"]').exists()).toBe(false);
      expect(wrapper.text()).not.toContain('Emit Audit Event');
      expect(wrapper.text()).not.toContain('Save Audit Event');

      // No Delete button or Trash icon in any row
      const deleteButtons = wrapper.findAll('button[title="Delete"]');
      expect(deleteButtons.length).toBe(0);
      expect(wrapper.findAll('.btn-danger').length).toBe(0);

      // No modal inputs for creating records
      expect(wrapper.find('input[placeholder*="Patient Record Accessed"]').exists()).toBe(false);
      expect(wrapper.find('select').exists()).toBe(false);
    });

    it('renders clean empty state text without mutation prompts when no records exist', async () => {
      (fhirApi.search as any).mockResolvedValue([]);
      const wrapper = mount(AuditEventView);
      await flushPromises();

      const text = wrapper.text();
      expect(text).toContain('No Audit Events recorded.');
      expect(text).not.toContain('Click Emit Audit Event');
    });

    it('renders loading indicator while fetching', async () => {
      (fhirApi.search as any).mockImplementation(() => new Promise(() => {})); // pending promise
      const wrapper = mount(AuditEventView);
      store.loading = true;
      await wrapper.vm.$nextTick();

      expect(wrapper.text()).toContain('Loading Audit Events...');
    });

    it('renders safe error banner when store has error', async () => {
      const wrapper = mount(AuditEventView);
      await flushPromises();

      store.error = 'Resource not accessible';
      await wrapper.vm.$nextTick();

      expect(wrapper.text()).toContain('Failed to load Audit Events:');
      expect(wrapper.text()).toContain('Resource not accessible');
    });

    it('executes search by _id and passes trimmed parameter to store', async () => {
      const wrapper = mount(AuditEventView);
      await flushPromises();

      const searchInput = wrapper.find('input[type="text"]');
      expect(searchInput.attributes('placeholder')).toContain('Filter by Audit ID (_id)...');

      await searchInput.setValue('audit-002');
      const searchButton = wrapper.find('button[aria-label="Search"]');
      await searchButton.trigger('click');
      await flushPromises();

      expect(fhirApi.search).toHaveBeenLastCalledWith('AuditEvent', { _id: 'audit-002' });
    });

    it('opens detail JSON modal on View Detail click and closes on close button click', async () => {
      const wrapper = mount(AuditEventView);
      await flushPromises();

      const viewDetailBtn = wrapper.find('button[title="View FHIR JSON"]');
      expect(viewDetailBtn.exists()).toBe(true);

      await viewDetailBtn.trigger('click');
      await wrapper.vm.$nextTick();

      const modal = wrapper.find('.modal-content');
      expect(modal.exists()).toBe(true);
      expect(modal.text()).toContain('AuditEvent/audit-001');
      expect(modal.text()).toContain('"action": "R"');

      // Close modal
      const closeBtn = wrapper.find('button[aria-label="Close"]');
      await closeBtn.trigger('click');
      await wrapper.vm.$nextTick();

      expect(wrapper.find('.modal-content').exists()).toBe(false);
    });
  });

  describe('securityStore.ts AuditEvent actions', () => {
    it('fetchAuditEvents queries fhirApi with _id when provided', async () => {
      await store.fetchAuditEvents('audit-999');
      expect(fhirApi.search).toHaveBeenCalledWith('AuditEvent', { _id: 'audit-999' });
      expect(store.auditEvents).toEqual(mockAuditEvents);
      expect(store.loading).toBe(false);
      expect(store.error).toBeNull();
    });

    it('fetchAuditEvents queries fhirApi with empty params when id is omitted', async () => {
      await store.fetchAuditEvents();
      expect(fhirApi.search).toHaveBeenCalledWith('AuditEvent', {});
      expect(store.auditEvents).toEqual(mockAuditEvents);
    });

    it('handles search failure safely', async () => {
      (fhirApi.search as any).mockRejectedValueOnce(new Error('Network error'));
      await store.fetchAuditEvents();

      expect(store.error).toBe('Network error');
      expect(store.loading).toBe(false);
    });

    it('does not export or define createAuditEvent or deleteAuditEvent', () => {
      const storeInstance = store as any;
      expect(storeInstance.createAuditEvent).toBeUndefined();
      expect(storeInstance.deleteAuditEvent).toBeUndefined();
    });
  });

  describe('Architectural Guardrails: Immutable Audit Log Assurance', () => {
    it('verifies AuditEventView.vue contains zero mutation handlers, forms, or deletion affordances', () => {
      // Assert no create or delete handlers/methods
      expect(viewContent).not.toMatch(/handleCreate/);
      expect(viewContent).not.toMatch(/createAuditEvent/);
      expect(viewContent).not.toMatch(/deleteAuditEvent/);

      // Assert no mutation icon imports or usages
      expect(viewContent).not.toMatch(/import\s*\{[^}]*\bPlus\b[^}]*\}\s*from\s*['"]lucide-vue-next['"]/);
      expect(viewContent).not.toMatch(/import\s*\{[^}]*\bTrash2\b[^}]*\}\s*from\s*['"]lucide-vue-next['"]/);

      // Assert no form submission or create modal markup
      expect(viewContent).not.toMatch(/Emit Audit Event/);
      expect(viewContent).not.toMatch(/Save Audit Event/);
      expect(viewContent).not.toMatch(/newAudit/);

      // Assert no legacy search parameters
      expect(viewContent).not.toMatch(/searchName/);
    });

    it('verifies securityStore.ts contains zero AuditEvent mutation calls and queries strictly via _id', () => {
      // Assert no mutation methods defined
      expect(storeContent).not.toMatch(/async\s+function\s+createAuditEvent/);
      expect(storeContent).not.toMatch(/async\s+function\s+deleteAuditEvent/);
      expect(storeContent).not.toMatch(/createAuditEvent\s*[,:]/);
      expect(storeContent).not.toMatch(/deleteAuditEvent\s*[,:]/);

      // Assert no fhirApi mutation calls for AuditEvent
      expect(storeContent).not.toMatch(/fhirApi\.create<AuditEvent>/);
      expect(storeContent).not.toMatch(/fhirApi\.create\(\s*['"]AuditEvent['"]/);
      expect(storeContent).not.toMatch(/fhirApi\.update<AuditEvent>/);
      expect(storeContent).not.toMatch(/fhirApi\.update\(\s*['"]AuditEvent['"]/);
      expect(storeContent).not.toMatch(/fhirApi\.delete\(\s*['"]AuditEvent['"]/);

      // Assert no legacy name or identifier params in AuditEvent queries
      const auditFetchSection = storeContent.substring(
        storeContent.indexOf('fetchAuditEvents'),
        storeContent.indexOf('fetchConsents')
      );
      expect(auditFetchSection).not.toMatch(/params\.name/);
      expect(auditFetchSection).not.toMatch(/params\.identifier/);
      expect(auditFetchSection).toMatch(/params\._id/);
    });
  });
});
