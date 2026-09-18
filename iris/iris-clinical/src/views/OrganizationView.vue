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
import { useFacilityStore } from '../stores/facilityStore';
import type { Organization } from '../models/fhir';
import SecurityBadge from '../components/SecurityBadge.vue';
import { Plus, Search, Trash2, Eye, Building2, X } from 'lucide-vue-next';

const store = useFacilityStore();
const searchName = ref('');
const showCreateModal = ref(false);
const showDetailModal = ref(false);
const selectedResource = ref<any>(null);

const newOrg = ref({
  name: '',
  alias: '',
  type: 'prov',
  identifier: ''
});

onMounted(() => {
  store.fetchOrganizations();
});

const handleSearch = () => {
  store.fetchOrganizations(searchName.value);
};

const handleCreateOrg = async () => {
  if (!newOrg.value.name) return;
  const org: Partial<Organization> = {
    resourceType: 'Organization',
    active: true,
    name: newOrg.value.name,
    alias: newOrg.value.alias ? [newOrg.value.alias] : [],
    type: [{ coding: [{ code: newOrg.value.type, display: newOrg.value.type === 'prov' ? 'Healthcare Provider' : 'Insurance / Payer' }] }],
    identifier: newOrg.value.identifier ? [{ system: 'urn:org', value: newOrg.value.identifier }] : []
  };

  await store.createOrganization(org);
  showCreateModal.value = false;
  newOrg.value = { name: '', alias: '', type: 'prov', identifier: '' };
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
          <Building2 class="text-amber-400" :size="24" />
          Healthcare Organizations
        </h1>
        <p class="subtitle mt-1">Manage health networks, hospital authorities, clinical divisions, and external payers in FHIR R5.</p>
      </div>

      <button @click="showCreateModal = true" class="btn btn-primary">
        <Plus :size="16" />
        <span>Register Organization</span>
      </button>
    </div>

    <!-- Search Bar -->
    <div class="card p-4 flex flex-col md:flex-row items-center justify-between gap-4">
      <span class="text-xs text-slate-400 font-semibold uppercase">Total Organizations: {{ store.organizations.length }}</span>
      <div class="flex items-center gap-2 w-full md:w-80">
        <input 
          v-model="searchName" 
          @keyup.enter="handleSearch"
          type="text" 
          placeholder="Filter organization name..." 
          class="form-input flex-1 text-xs" 
        />
        <button @click="handleSearch" class="btn btn-secondary text-xs">
          <Search :size="14" />
        </button>
      </div>
    </div>

    <!-- Organizations Table -->
    <div class="table-container">
      <table class="table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Organization Name</th>
            <th>Alias</th>
            <th>Type</th>
            <th>Identifier</th>
            <th>Confidentiality</th>
            <th class="text-right">Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="store.organizations.length === 0">
            <td colspan="7" class="text-center text-slate-500 py-6">No Organizations registered. Click Register Organization above.</td>
          </tr>
          <tr v-for="org in store.organizations" :key="org.id">
            <td class="font-mono text-xs text-amber-400 font-semibold">{{ org.id }}</td>
            <td class="font-medium text-white">{{ org.name }}</td>
            <td class="text-slate-300 text-xs">{{ org.alias?.join(', ') || '-' }}</td>
            <td>
              <span class="badge badge-amber">{{ org.type?.[0]?.coding?.[0]?.display || 'Provider' }}</span>
            </td>
            <td class="font-mono text-xs text-slate-300">{{ org.identifier?.[0]?.value || '-' }}</td>
            <td>
              <SecurityBadge :meta="org.meta" />
            </td>
            <td class="text-right space-x-2">
              <button @click="viewDetails(org)" class="btn btn-secondary py-1 px-2 text-xs" title="View FHIR JSON">
                <Eye :size="14" />
              </button>
              <button @click="org.id && store.deleteOrganization(org.id)" class="btn btn-danger py-1 px-2 text-xs" title="Delete">
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
          <h3 class="text-lg font-bold text-white">Register Organization</h3>
          <button @click="showCreateModal = false" class="text-slate-400 hover:text-white">
            <X :size="20" />
          </button>
        </div>

        <div class="space-y-4">
          <div class="form-group">
            <label class="form-label">Organization Name *</label>
            <input v-model="newOrg.name" type="text" placeholder="e.g. Metro Health Authority" class="form-input" required />
          </div>

          <div class="grid grid-cols-2 gap-3">
            <div class="form-group">
              <label class="form-label">Alias / Abbreviation</label>
              <input v-model="newOrg.alias" type="text" placeholder="e.g. MHA" class="form-input" />
            </div>
            <div class="form-group">
              <label class="form-label">Classification Type</label>
              <select v-model="newOrg.type" class="form-select">
                <option value="prov">Healthcare Provider</option>
                <option value="dept">Hospital Department</option>
                <option value="ins">Health Insurance / Payer</option>
                <option value="govt">Government Agency</option>
              </select>
            </div>
          </div>

          <div class="form-group">
            <label class="form-label">Identifier</label>
            <input v-model="newOrg.identifier" type="text" placeholder="e.g. ORG-990" class="form-input" />
          </div>

          <div class="flex justify-end gap-2 pt-4 border-t border-[#27344d]">
            <button @click="showCreateModal = false" class="btn btn-secondary">Cancel</button>
            <button @click="handleCreateOrg" class="btn btn-primary">Save to Clustered Grid</button>
          </div>
        </div>
      </div>
    </div>

    <!-- Detail JSON Modal -->
    <div v-if="showDetailModal" class="modal-overlay" @click.self="showDetailModal = false">
      <div class="modal-content max-w-2xl">
        <div class="flex items-center justify-between pb-3 border-b border-[#27344d] mb-4">
          <div class="flex items-center gap-2">
            <h3 class="text-lg font-bold text-white font-mono">
              {{ selectedResource?.resourceType }}/{{ selectedResource?.id }}
            </h3>
            <SecurityBadge v-if="selectedResource" :meta="selectedResource.meta" />
          </div>
          <button @click="showDetailModal = false" class="text-slate-400 hover:text-white">
            <X :size="20" />
          </button>
        </div>
        <pre class="code-view">{{ JSON.stringify(selectedResource, null, 2) }}</pre>
      </div>
    </div>
  </div>
</template>
