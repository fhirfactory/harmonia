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
import { useSecurityStore } from '../stores/securityStore';
import { Search, Eye, ShieldAlert, X, AlertCircle, RotateCw } from 'lucide-vue-next';

const store = useSecurityStore();
const searchId = ref('');
const showDetailModal = ref(false);
const selectedResource = ref<any>(null);

onMounted(() => {
  store.fetchAuditEvents();
});

const handleSearch = () => {
  store.fetchAuditEvents(searchId.value.trim() || undefined);
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
          <ShieldAlert class="text-rose-400" :size="24" />
          Security Audit Events
        </h1>
        <p class="subtitle mt-1">Audit log of security, access control, policy and clinical events in FHIR R5.</p>
      </div>
    </div>

    <!-- Error Banner -->
    <div v-if="store.error" class="card bg-rose-950/30 border-rose-500/40 p-4 text-rose-200 flex items-center gap-3">
      <AlertCircle class="text-rose-400 shrink-0" :size="18" />
      <div class="text-xs">
        <span class="font-semibold">Failed to load Audit Events: </span>
        <span>{{ store.error }}</span>
      </div>
    </div>

    <!-- Search Bar -->
    <div class="card p-4 flex flex-col md:flex-row items-center justify-between gap-4">
      <span class="text-xs text-slate-400 font-semibold uppercase">Total Audit Events: {{ store.auditEvents.length }}</span>
      <div class="flex items-center gap-2 w-full md:w-80">
        <input 
          v-model="searchId" 
          @keyup.enter="handleSearch"
          type="text" 
          placeholder="Filter by Audit ID (_id)..." 
          class="form-input flex-1 text-xs" 
        />
        <button @click="handleSearch" class="btn btn-secondary text-xs" aria-label="Search">
          <Search :size="14" />
        </button>
      </div>
    </div>

    <!-- AuditEvents Table -->
    <div class="table-container">
      <table class="table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Action</th>
            <th>Description / Code</th>
            <th>Severity</th>
            <th>Agent</th>
            <th>Recorded Date</th>
            <th class="text-right">Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="store.loading">
            <td colspan="7" class="text-center text-slate-400 py-6">
              <div class="flex items-center justify-center gap-2">
                <RotateCw :size="16" class="animate-spin text-sky-400" />
                <span>Loading Audit Events...</span>
              </div>
            </td>
          </tr>
          <tr v-else-if="store.auditEvents.length === 0">
            <td colspan="7" class="text-center text-slate-500 py-6">No Audit Events recorded.</td>
          </tr>
          <tr v-for="audit in store.auditEvents" :key="audit.id" v-else>
            <td class="font-mono text-xs text-rose-400 font-semibold">{{ audit.id }}</td>
            <td>
              <span class="badge" :class="audit.action === 'D' ? 'badge-red' : audit.action === 'C' ? 'badge-green' : 'badge-blue'">
                {{ audit.action === 'C' ? 'CREATE' : audit.action === 'R' ? 'READ' : audit.action === 'U' ? 'UPDATE' : audit.action === 'D' ? 'DELETE' : 'EXECUTE' }}
              </span>
            </td>
            <td class="font-medium text-white">{{ audit.code?.text || 'Security Event' }}</td>
            <td>
              <span class="badge badge-purple">{{ audit.severity || 'informational' }}</span>
            </td>
            <td class="text-slate-300 text-xs">{{ audit.agent?.[0]?.who?.display || 'System' }}</td>
            <td class="text-slate-400 text-xs">{{ audit.recorded || '-' }}</td>
            <td class="text-right">
              <button @click="viewDetails(audit)" class="btn btn-secondary py-1 px-2 text-xs" title="View FHIR JSON">
                <Eye :size="14" />
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Detail JSON Modal -->
    <div v-if="showDetailModal" class="modal-overlay" @click.self="showDetailModal = false">
      <div class="modal-content max-w-2xl">
        <div class="flex items-center justify-between pb-3 border-b border-[#27344d] mb-4">
          <h3 class="text-lg font-bold text-white font-mono">
            {{ selectedResource?.resourceType }}/{{ selectedResource?.id }}
          </h3>
          <button @click="showDetailModal = false" class="text-slate-400 hover:text-white" aria-label="Close">
            <X :size="20" />
          </button>
        </div>
        <pre class="code-view">{{ JSON.stringify(selectedResource, null, 2) }}</pre>
      </div>
    </div>
  </div>
</template>
