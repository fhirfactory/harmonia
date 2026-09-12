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
import type { Group } from '../models/fhir';
import { fhirApi } from '../api/fhirClient';

export const useGroupStore = defineStore('group', () => {
  const groups = ref<Group[]>([]);
  const loading = ref(false);
  const error = ref<string | null>(null);

  async function fetchGroups(name?: string) {
    loading.value = true;
    try {
      const params: Record<string, string> = {};
      if (name) params.name = name;
      groups.value = await fhirApi.search<Group>('Group', params);
    } catch (err: any) {
      error.value = err.message || 'Failed to fetch groups';
    } finally {
      loading.value = false;
    }
  }

  async function createGroup(group: Partial<Group>) {
    loading.value = true;
    try {
      const created = await fhirApi.create<Group>('Group', group);
      groups.value.unshift(created);
      return created;
    } finally {
      loading.value = false;
    }
  }

  async function updateGroup(id: string, group: Partial<Group>) {
    loading.value = true;
    try {
      const updated = await fhirApi.update<Group>('Group', id, group);
      const index = groups.value.findIndex(g => g.id === id);
      if (index !== -1) groups.value[index] = updated;
      return updated;
    } finally {
      loading.value = false;
    }
  }

  async function deleteGroup(id: string) {
    loading.value = true;
    try {
      await fhirApi.delete('Group', id);
      groups.value = groups.value.filter(g => g.id !== id);
    } finally {
      loading.value = false;
    }
  }

  return {
    groups,
    loading,
    error,
    fetchGroups,
    createGroup,
    updateGroup,
    deleteGroup
  };
});
