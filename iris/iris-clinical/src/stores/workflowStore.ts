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
import type { Task, Communication, DocumentReference } from '../models/fhir';
import { fhirApi } from '../api/fhirClient';

export const useWorkflowStore = defineStore('workflow', () => {
  const tasks = ref<Task[]>([]);
  const communications = ref<Communication[]>([]);
  const documentReferences = ref<DocumentReference[]>([]);
  const loading = ref(false);
  const error = ref<string | null>(null);

  // Task
  async function fetchTasks(name?: string) {
    loading.value = true;
    try {
      const params: Record<string, string> = {};
      if (name) params.name = name;
      tasks.value = await fhirApi.search<Task>('Task', params);
    } catch (err: any) {
      error.value = err.message || 'Failed to fetch Tasks';
    } finally {
      loading.value = false;
    }
  }

  async function createTask(task: Partial<Task>) {
    loading.value = true;
    try {
      const created = await fhirApi.create<Task>('Task', task);
      tasks.value.unshift(created);
      return created;
    } finally {
      loading.value = false;
    }
  }

  async function deleteTask(id: string) {
    loading.value = true;
    try {
      await fhirApi.delete('Task', id);
      tasks.value = tasks.value.filter(t => t.id !== id);
    } finally {
      loading.value = false;
    }
  }

  // Communication
  async function fetchCommunications(name?: string) {
    loading.value = true;
    try {
      const params: Record<string, string> = {};
      if (name) params.name = name;
      communications.value = await fhirApi.search<Communication>('Communication', params);
    } catch (err: any) {
      error.value = err.message || 'Failed to fetch Communications';
    } finally {
      loading.value = false;
    }
  }

  async function createCommunication(comm: Partial<Communication>) {
    loading.value = true;
    try {
      const created = await fhirApi.create<Communication>('Communication', comm);
      communications.value.unshift(created);
      return created;
    } finally {
      loading.value = false;
    }
  }

  async function deleteCommunication(id: string) {
    loading.value = true;
    try {
      await fhirApi.delete('Communication', id);
      communications.value = communications.value.filter(c => c.id !== id);
    } finally {
      loading.value = false;
    }
  }

  // DocumentReference
  async function fetchDocumentReferences(name?: string) {
    loading.value = true;
    try {
      const params: Record<string, string> = {};
      if (name) params.name = name;
      documentReferences.value = await fhirApi.search<DocumentReference>('DocumentReference', params);
    } catch (err: any) {
      error.value = err.message || 'Failed to fetch DocumentReferences';
    } finally {
      loading.value = false;
    }
  }

  async function createDocumentReference(doc: Partial<DocumentReference>) {
    loading.value = true;
    try {
      const created = await fhirApi.create<DocumentReference>('DocumentReference', doc);
      documentReferences.value.unshift(created);
      return created;
    } finally {
      loading.value = false;
    }
  }

  async function deleteDocumentReference(id: string) {
    loading.value = true;
    try {
      await fhirApi.delete('DocumentReference', id);
      documentReferences.value = documentReferences.value.filter(d => d.id !== id);
    } finally {
      loading.value = false;
    }
  }

  return {
    tasks,
    communications,
    documentReferences,
    loading,
    error,
    fetchTasks,
    createTask,
    deleteTask,
    fetchCommunications,
    createCommunication,
    deleteCommunication,
    fetchDocumentReferences,
    createDocumentReference,
    deleteDocumentReference
  };
});
