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
import type { Location } from '../models/fhir';
import { Plus, Search, Trash2, Eye, MapPin, X } from 'lucide-vue-next';

const store = useFacilityStore();
const searchName = ref('');
const showCreateModal = ref(false);
const showDetailModal = ref(false);
const selectedResource = ref<any>(null);

const newLoc = ref({
  name: '',
  description: '',
  status: 'active' as 'active' | 'suspended' | 'inactive',
  identifier: ''
});

onMounted(() => {
  store.fetchLocations();
});

const handleSearch = () => {
  store.fetchLocations(searchName.value);
};

const handleCreateLocation = async () => {
  if (!newLoc.value.name) return;
  const loc: Partial<Location> = {
    resourceType: 'Location',
    status: newLoc.value.status,
    name: newLoc.value.name,
    description: newLoc.value.description || undefined,
    mode: 'instance',
    identifier: newLoc.value.identifier ? [{ system: 'urn:location', value: newLoc.value.identifier }] : []
  };

  await store.createLocation(loc);
  showCreateModal.value = false;
  newLoc.value = { name: '', description: '', status: 'active', identifier: '' };
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
          <MapPin class="text-rose-400" :size="24" />
          Location & Facility Hierarchy
        </h1>
        <p class="subtitle mt-1">Manage physical hospital campuses, buildings, wards, rooms, and clinic sites in FHIR R5.</p>
      </div>

      <button @click="showCreateModal = true" class="btn btn-primary">
        <Plus :size="16" />
        <span>Add Location</span>
      </button>
    </div>

    <!-- Search Bar -->
    <div class="card p-4 flex flex-col md:flex-row items-center justify-between gap-4">
      <span class="text-xs text-slate-400 font-semibold uppercase">Total Facilities: {{ store.locations.length }}</span>
      <div class="flex items-center gap-2 w-full md:w-80">
        <input 
          v-model="searchName" 
          @keyup.enter="handleSearch"
          type="text" 
          placeholder="Filter location name..." 
          class="form-input flex-1 text-xs" 
        />
        <button @click="handleSearch" class="btn btn-secondary text-xs">
          <Search :size="14" />
        </button>
      </div>
    </div>

    <!-- Locations Table -->
    <div class="table-container">
      <table class="table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Location / Ward Name</th>
            <th>Status</th>
            <th>Description</th>
            <th>Identifier</th>
            <th class="text-right">Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="store.locations.length === 0">
            <td colspan="6" class="text-center text-slate-500 py-6">No Locations found. Click Add Location above.</td>
          </tr>
          <tr v-for="loc in store.locations" :key="loc.id">
            <td class="font-mono text-xs text-rose-400 font-semibold">{{ loc.id }}</td>
            <td class="font-medium text-white">{{ loc.name }}</td>
            <td>
              <span class="badge" :class="loc.status === 'active' ? 'badge-green' : 'badge-amber'">
                {{ loc.status || 'active' }}
              </span>
            </td>
            <td class="text-slate-300 text-xs">{{ loc.description || '-' }}</td>
            <td class="font-mono text-xs text-slate-300">{{ loc.identifier?.[0]?.value || '-' }}</td>
            <td class="text-right space-x-2">
              <button @click="viewDetails(loc)" class="btn btn-secondary py-1 px-2 text-xs" title="View FHIR JSON">
                <Eye :size="14" />
              </button>
              <button @click="loc.id && store.deleteLocation(loc.id)" class="btn btn-danger py-1 px-2 text-xs" title="Delete">
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
          <h3 class="text-lg font-bold text-white">Add Physical Location</h3>
          <button @click="showCreateModal = false" class="text-slate-400 hover:text-white">
            <X :size="20" />
          </button>
        </div>

        <div class="space-y-4">
          <div class="form-group">
            <label class="form-label">Location / Facility Name *</label>
            <input v-model="newLoc.name" type="text" placeholder="e.g. Building A - Trauma ICU" class="form-input" required />
          </div>

          <div class="grid grid-cols-2 gap-3">
            <div class="form-group">
              <label class="form-label">Operational Status</label>
              <select v-model="newLoc.status" class="form-select">
                <option value="active">Active</option>
                <option value="suspended">Suspended</option>
                <option value="inactive">Inactive</option>
              </select>
            </div>
            <div class="form-group">
              <label class="form-label">Identifier</label>
              <input v-model="newLoc.identifier" type="text" placeholder="e.g. LOC-ICU-01" class="form-input" />
            </div>
          </div>

          <div class="form-group">
            <label class="form-label">Facility Description</label>
            <input v-model="newLoc.description" type="text" placeholder="e.g. 24-bed critical care intensive care unit" class="form-input" />
          </div>

          <div class="flex justify-end gap-2 pt-4 border-t border-[#27344d]">
            <button @click="showCreateModal = false" class="btn btn-secondary">Cancel</button>
            <button @click="handleCreateLocation" class="btn btn-primary">Save Location</button>
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
