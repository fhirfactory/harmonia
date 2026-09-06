import { defineStore } from 'pinia';
import { ref } from 'vue';
import type { Practitioner, PractitionerRole } from '../models/fhir';
import { fhirApi } from '../api/fhirClient';

export const usePractitionerStore = defineStore('practitioner', () => {
  const practitioners = ref<Practitioner[]>([]);
  const practitionerRoles = ref<PractitionerRole[]>([]);
  const loading = ref(false);
  const error = ref<string | null>(null);

  async function fetchPractitioners(name?: string) {
    loading.value = true;
    error.value = null;
    try {
      const params: Record<string, string> = {};
      if (name) params.name = name;
      practitioners.value = await fhirApi.search<Practitioner>('Practitioner', params);
    } catch (err: any) {
      error.value = err.message || 'Failed to fetch practitioners';
    } finally {
      loading.value = false;
    }
  }

  async function fetchPractitionerRoles() {
    loading.value = true;
    error.value = null;
    try {
      practitionerRoles.value = await fhirApi.search<PractitionerRole>('PractitionerRole');
    } catch (err: any) {
      error.value = err.message || 'Failed to fetch practitioner roles';
    } finally {
      loading.value = false;
    }
  }

  async function createPractitioner(p: Partial<Practitioner>) {
    loading.value = true;
    try {
      const created = await fhirApi.create<Practitioner>('Practitioner', p);
      practitioners.value.unshift(created);
      return created;
    } finally {
      loading.value = false;
    }
  }

  async function createPractitionerRole(role: Partial<PractitionerRole>) {
    loading.value = true;
    try {
      const created = await fhirApi.create<PractitionerRole>('PractitionerRole', role);
      practitionerRoles.value.unshift(created);
      return created;
    } finally {
      loading.value = false;
    }
  }

  async function deletePractitioner(id: string) {
    loading.value = true;
    try {
      await fhirApi.delete('Practitioner', id);
      practitioners.value = practitioners.value.filter(p => p.id !== id);
    } finally {
      loading.value = false;
    }
  }

  return {
    practitioners,
    practitionerRoles,
    loading,
    error,
    fetchPractitioners,
    fetchPractitionerRoles,
    createPractitioner,
    createPractitionerRole,
    deletePractitioner
  };
});
