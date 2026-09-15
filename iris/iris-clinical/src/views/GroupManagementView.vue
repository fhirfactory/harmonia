<!--
  Copyright (c) 2026 Mark Hunter

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program.  If not, see <https://www.gnu.org/licenses/>.
-->

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useGroupStore } from '../stores/groupStore';
import type { Group } from '../models/fhir';
import { Plus, Search, Trash2, Eye, UsersRound, X } from 'lucide-vue-next';

const store = useGroupStore();
const searchName = ref('');
const showCreateModal = ref(false);
const showDetailModal = ref(false);
const selectedResource = ref<any>(null);

const newGroup = ref({
  name: '',
  description: '',
  type: 'person' as any,
  membership: 'definitional' as any,
  quantity: 1,
  identifier: ''
});

onMounted(() => {
  store.fetchGroups();
});

const handleSearch = () => {
  store.fetchGroups(searchName.value);
};

const handleCreateGroup = async () => {
  if (!newGroup.value.name) return;
  const grp: Partial<Group> = {
    resourceType: 'Group',
    active: true,
    name: newGroup.value.name,
    description: newGroup.value.description || undefined,
    type: newGroup.value.type,
    membership: newGroup.value.membership,
    quantity: newGroup.value.quantity,
    identifier: newGroup.value.identifier ? [{ system: 'urn:group', value: newGroup.value.identifier }] : []
  };

  await store.createGroup(grp);
  showCreateModal.value = false;
  newGroup.value = { name: '', description: '', type: 'person', membership: 'definitional', quantity: 1, identifier: '' };
};

const viewDetails = (item: any) => {
  selectedResource.value = item;
  showDetailModal.value = true;
};
</script>

<template>
  <div class="space-y-6">
    <div class="flex flex-col md:flex-row md:items-center justify-between gap-4">
      <div>
        <h1 class="text-2xl font-bold text-white flex items-center gap-2">
          <UsersRound class="text-cyan-400" :size="24" />
          Patient Groups & Clinical Cohorts
        </h1>
        <p class="subtitle mt-1">Manage clinical research cohorts, public health tracking groups, and chronic disease registries in FHIR R5.</p>
      </div>

      <button @click="showCreateModal = true" class="btn btn-primary">
        <Plus :size="16" />
        <span>Create Cohort Group</span>
      </button>
    </div>

    <!-- Search Bar -->
    <div class="card p-4 flex flex-col md:flex-row items-center justify-between gap-4">
      <span class="text-xs text-slate-400 font-semibold uppercase">Total Cohorts: {{ store.groups.length }}</span>
      <div class="flex items-center gap-2 w-full md:w-80">
        <input 
          v-model="searchName" 
          @keyup.enter="handleSearch"
          type="text" 
          placeholder="Filter group name..." 
          class="form-input flex-1 text-xs" 
        />
        <button @click="handleSearch" class="btn btn-secondary text-xs">
          <Search :size="14" />
        </button>
      </div>
    </div>

    <!-- Groups Table -->
    <div class="table-container">
      <table class="table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Cohort / Group Name</th>
            <th>Type</th>
            <th>Membership</th>
            <th>Cohort Size</th>
            <th>Identifier</th>
            <th class="text-right">Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="store.groups.length === 0">
            <td colspan="7" class="text-center text-slate-500 py-6">No Cohort Groups found. Click Create Cohort Group above.</td>
          </tr>
          <tr v-for="grp in store.groups" :key="grp.id">
            <td class="font-mono text-xs text-cyan-400 font-semibold">{{ grp.id }}</td>
            <td class="font-medium text-white">
              <div>{{ grp.name }}</div>
              <div v-if="grp.description" class="text-xs text-slate-400 font-normal">{{ grp.description }}</div>
            </td>
            <td>
              <span class="badge badge-blue">{{ grp.type }}</span>
            </td>
            <td>
              <span class="badge badge-amber">{{ grp.membership }}</span>
            </td>
            <td class="font-semibold text-white">{{ grp.quantity || '-' }} members</td>
            <td class="font-mono text-xs text-slate-300">{{ grp.identifier?.[0]?.value || '-' }}</td>
            <td class="text-right space-x-2">
              <button @click="viewDetails(grp)" class="btn btn-secondary py-1 px-2 text-xs" title="View FHIR JSON">
                <Eye :size="14" />
              </button>
              <button @click="grp.id && store.deleteGroup(grp.id)" class="btn btn-danger py-1 px-2 text-xs" title="Delete">
                <Trash2 :size="14" />
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Create Modal -->
    <div v-if="showCreateModal" class="modal-overlay" @click.self="showCreateModal = false">
      <div class="modal-content">
        <div class="flex items-center justify-between pb-3 border-b border-[#27344d] mb-4">
          <h3 class="text-lg font-bold text-white">Create Cohort Group</h3>
          <button @click="showCreateModal = false" class="text-slate-400 hover:text-white">
            <X :size="20" />
          </button>
        </div>

        <div class="space-y-4">
          <div class="form-group">
            <label class="form-label">Cohort / Group Name *</label>
            <input v-model="newGroup.name" type="text" placeholder="e.g. Diabetes Mellitus Type 2 Cohort" class="form-input" required />
          </div>

          <div class="grid grid-cols-2 gap-3">
            <div class="form-group">
              <label class="form-label">Group Type</label>
              <select v-model="newGroup.type" class="form-select">
                <option value="person">Person</option>
                <option value="practitioner">Practitioner</option>
                <option value="careteam">Care Team</option>
                <option value="organization">Organization</option>
              </select>
            </div>
            <div class="form-group">
              <label class="form-label">Membership Basis</label>
              <select v-model="newGroup.membership" class="form-select">
                <option value="definitional">Definitional</option>
                <option value="enumerated">Enumerated</option>
              </select>
            </div>
          </div>

          <div class="grid grid-cols-2 gap-3">
            <div class="form-group">
              <label class="form-label">Estimated Cohort Size</label>
              <input v-model.number="newGroup.quantity" type="number" min="1" class="form-input" />
            </div>
            <div class="form-group">
              <label class="form-label">Identifier</label>
              <input v-model="newGroup.identifier" type="text" placeholder="e.g. GRP-DIAB-26" class="form-input" />
            </div>
          </div>

          <div class="form-group">
            <label class="form-label">Cohort Description</label>
            <input v-model="newGroup.description" type="text" placeholder="e.g. Monitored for glycemic control and annual eye screenings" class="form-input" />
          </div>

          <div class="flex justify-end gap-2 pt-4 border-t border-[#27344d]">
            <button @click="showCreateModal = false" class="btn btn-secondary">Cancel</button>
            <button @click="handleCreateGroup" class="btn btn-primary">Create Cohort</button>
          </div>
        </div>
      </div>
    </div>

    <!-- Detail JSON Modal -->
    <div v-if="showDetailModal" class="modal-overlay" @click.self="showDetailModal = false">
      <div class="modal-content max-w-2xl">
        <div class="flex items-center justify-between pb-3 border-b border-[#27344d] mb-4">
          <h3 class="text-lg font-bold text-white font-mono">
            {{ selectedResource?.resourceType }}/{{ selectedResource?.id }}
          </h3>
          <button @click="showDetailModal = false" class="text-slate-400 hover:text-white">
            <X :size="20" />
          </button>
        </div>
        <pre class="code-view">{{ JSON.stringify(selectedResource, null, 2) }}</pre>
      </div>
    </div>
  </div>
</template>
