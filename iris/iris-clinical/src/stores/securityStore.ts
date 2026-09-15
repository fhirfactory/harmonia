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
import { ref } from 'vue';
import type { Provenance, AuditEvent, Consent } from '../models/fhir';
import { fhirApi } from '../api/fhirClient';

export const useSecurityStore = defineStore('security', () => {
  const provenances = ref<Provenance[]>([]);
  const auditEvents = ref<AuditEvent[]>([]);
  const consents = ref<Consent[]>([]);
  const loading = ref(false);
  const error = ref<string | null>(null);

  // Provenance
  async function fetchProvenances(name?: string) {
    loading.value = true;
    try {
      const params: Record<string, string> = {};
      if (name) params.name = name;
      provenances.value = await fhirApi.search<Provenance>('Provenance', params);
    } catch (err: any) {
      error.value = err.message || 'Failed to fetch Provenances';
    } finally {
      loading.value = false;
    }
  }

  async function createProvenance(prov: Partial<Provenance>) {
    loading.value = true;
    try {
      const created = await fhirApi.create<Provenance>('Provenance', prov);
      provenances.value.unshift(created);
      return created;
    } finally {
      loading.value = false;
    }
  }

  async function deleteProvenance(id: string) {
    loading.value = true;
    try {
      await fhirApi.delete('Provenance', id);
      provenances.value = provenances.value.filter(p => p.id !== id);
    } finally {
      loading.value = false;
    }
  }

  // AuditEvent
  async function fetchAuditEvents(name?: string) {
    loading.value = true;
    try {
      const params: Record<string, string> = {};
      if (name) params.name = name;
      auditEvents.value = await fhirApi.search<AuditEvent>('AuditEvent', params);
    } catch (err: any) {
      error.value = err.message || 'Failed to fetch AuditEvents';
    } finally {
      loading.value = false;
    }
  }

  async function createAuditEvent(audit: Partial<AuditEvent>) {
    loading.value = true;
    try {
      const created = await fhirApi.create<AuditEvent>('AuditEvent', audit);
      auditEvents.value.unshift(created);
      return created;
    } finally {
      loading.value = false;
    }
  }

  async function deleteAuditEvent(id: string) {
    loading.value = true;
    try {
      await fhirApi.delete('AuditEvent', id);
      auditEvents.value = auditEvents.value.filter(a => a.id !== id);
    } finally {
      loading.value = false;
    }
  }

  // Consent
  async function fetchConsents(name?: string) {
    loading.value = true;
    try {
      const params: Record<string, string> = {};
      if (name) params.name = name;
      consents.value = await fhirApi.search<Consent>('Consent', params);
    } catch (err: any) {
      error.value = err.message || 'Failed to fetch Consents';
    } finally {
      loading.value = false;
    }
  }

  async function createConsent(consent: Partial<Consent>) {
    loading.value = true;
    try {
      const created = await fhirApi.create<Consent>('Consent', consent);
      consents.value.unshift(created);
      return created;
    } finally {
      loading.value = false;
    }
  }

  async function deleteConsent(id: string) {
    loading.value = true;
    try {
      await fhirApi.delete('Consent', id);
      consents.value = consents.value.filter(c => c.id !== id);
    } finally {
      loading.value = false;
    }
  }

  return {
    provenances,
    auditEvents,
    consents,
    loading,
    error,
    fetchProvenances,
    createProvenance,
    deleteProvenance,
    fetchAuditEvents,
    createAuditEvent,
    deleteAuditEvent,
    fetchConsents,
    createConsent,
    deleteConsent
  };
});
