import { defineStore } from 'pinia';
import { ref } from 'vue';
import type { Person, RelatedPerson } from '../models/fhir';
import { fhirApi } from '../api/fhirClient';

export const usePersonStore = defineStore('person', () => {
  const persons = ref<Person[]>([]);
  const relatedPersons = ref<RelatedPerson[]>([]);
  const loading = ref(false);
  const error = ref<string | null>(null);

  async function fetchPersons(searchName?: string) {
    loading.value = true;
    error.value = null;
    try {
      const params: Record<string, string> = {};
      if (searchName) params.name = searchName;
      persons.value = await fhirApi.search<Person>('Person', params);
    } catch (err: any) {
      error.value = err.message || 'Failed to fetch persons';
    } finally {
      loading.value = false;
    }
  }

  async function fetchRelatedPersons(searchName?: string) {
    loading.value = true;
    error.value = null;
    try {
      const params: Record<string, string> = {};
      if (searchName) params.name = searchName;
      relatedPersons.value = await fhirApi.search<RelatedPerson>('RelatedPerson', params);
    } catch (err: any) {
      error.value = err.message || 'Failed to fetch related persons';
    } finally {
      loading.value = false;
    }
  }

  async function createPerson(person: Partial<Person>) {
    loading.value = true;
    try {
      const created = await fhirApi.create<Person>('Person', person);
      persons.value.unshift(created);
      return created;
    } finally {
      loading.value = false;
    }
  }

  async function updatePerson(id: string, person: Partial<Person>) {
    loading.value = true;
    try {
      const updated = await fhirApi.update<Person>('Person', id, person);
      const index = persons.value.findIndex(p => p.id === id);
      if (index !== -1) persons.value[index] = updated;
      return updated;
    } finally {
      loading.value = false;
    }
  }

  async function deletePerson(id: string) {
    loading.value = true;
    try {
      await fhirApi.delete('Person', id);
      persons.value = persons.value.filter(p => p.id !== id);
    } finally {
      loading.value = false;
    }
  }

  async function createRelatedPerson(rp: Partial<RelatedPerson>) {
    loading.value = true;
    try {
      const created = await fhirApi.create<RelatedPerson>('RelatedPerson', rp);
      relatedPersons.value.unshift(created);
      return created;
    } finally {
      loading.value = false;
    }
  }

  async function deleteRelatedPerson(id: string) {
    loading.value = true;
    try {
      await fhirApi.delete('RelatedPerson', id);
      relatedPersons.value = relatedPersons.value.filter(rp => rp.id !== id);
    } finally {
      loading.value = false;
    }
  }

  return {
    persons,
    relatedPersons,
    loading,
    error,
    fetchPersons,
    fetchRelatedPersons,
    createPerson,
    updatePerson,
    deletePerson,
    createRelatedPerson,
    deleteRelatedPerson
  };
});
