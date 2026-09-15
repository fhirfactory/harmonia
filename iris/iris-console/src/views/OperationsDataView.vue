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
import { ref, computed, onMounted } from 'vue';
import { useOperationsStore } from '../stores/operationsStore';
import type { OperationResource } from '../models/operations';
import { Database, Search, Filter, Code, Eye, X, RefreshCw } from 'lucide-vue-next';

const operationsStore = useOperationsStore();
const selectedType = ref('tasksequence');
const searchQuery = ref('');
const showJsonModal = ref(false);
const activePayload = ref('');

onMounted(() => {
  operationsStore.fetchOperationalResources(selectedType.value);
});

const changeType = () => {
  operationsStore.fetchOperationalResources(selectedType.value);
};

const viewResourceJson = (res: OperationResource) => {
  activePayload.value = res.dataJson;
  showJsonModal.value = true;
};

const filteredResources = computed(() => {
  const list = Array.isArray(operationsStore.operationalResources) ? operationsStore.operationalResources : [];
  if (!searchQuery.value || !searchQuery.value.trim()) {
    return list;
  }
  const q = searchQuery.value.trim().toLowerCase();
  return list.filter(res => {
    if (!res) return false;
    const matchId = (res.objectId || '').toLowerCase().includes(q);
    const matchType = (res.objectType || '').toLowerCase().includes(q);
    const matchData = (res.dataJson || '').toLowerCase().includes(q);
    return matchId || matchType || matchData;
  });
});
</script>

<template>
  <div class="space-y-6">
    <!-- Header -->
    <div class="flex flex-col md:flex-row items-start md:items-center justify-between gap-4">
      <div>
        <div class="flex items-center gap-2">
          <h1 class="text-2xl font-bold text-white">Operational Data Store</h1>
          <span class="badge badge-amber">Non-FHIR Relational Entities</span>
        </div>
        <p class="subtitle mt-1">Direct view of operational definitions persisted via HIE Operations JPA Server &amp; Infinispan SPI.</p>
      </div>

      <button @click="changeType" class="btn btn-primary text-xs">
        <RefreshCw :size="14" /> Refresh Store
      </button>
    </div>

    <!-- Filter Toolbar -->
    <div class="card flex flex-col md:flex-row items-center justify-between gap-4 py-3">
      <div class="flex items-center gap-2 w-full md:w-96">
        <div class="relative w-full">
          <Search :size="16" class="absolute left-2.5 top-2.5 text-slate-400" />
          <input 
            v-model="searchQuery" 
            type="text" 
            placeholder="Search by Object ID or payload content..." 
            class="form-input w-full text-xs py-1.5 pl-8 pr-7"
          />
          <button 
            v-if="searchQuery" 
            @click="searchQuery = ''" 
            class="absolute right-2 top-2 text-slate-400 hover:text-white"
            title="Clear Search"
          >
            <X :size="14" />
          </button>
        </div>
      </div>

      <div class="flex items-center gap-2">
        <span class="text-xs text-slate-400 font-medium">Object Type:</span>
        <select v-model="selectedType" @change="changeType" class="form-select text-xs py-1">
          <option value="tasksequence">tasksequence</option>
          <option value="workflow">workflow</option>
          <option value="gateway_config">gateway_config</option>
          <option value="cluster_metrics">cluster_metrics</option>
        </select>
      </div>
    </div>

    <!-- Table -->
    <div class="table-container">
      <table class="table">
        <thead>
          <tr>
            <th>Object Type</th>
            <th>Object ID</th>
            <th>Version</th>
            <th>Data Preview</th>
            <th>Last Updated</th>
            <th class="text-right">Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="res in filteredResources" :key="res.objectId">
            <td>
              <span class="badge badge-blue font-mono text-[10px]">{{ res.objectType }}</span>
            </td>
            <td class="font-mono text-xs font-bold text-emerald-400">{{ res.objectId }}</td>
            <td class="font-mono text-xs text-slate-300">v{{ res.versionId || 1 }}</td>
            <td class="font-mono text-[11px] text-slate-400 max-w-xs truncate">
              {{ res.dataJson }}
            </td>
            <td class="text-xs text-slate-400">{{ new Date(res.lastUpdated || Date.now()).toLocaleString() }}</td>
            <td class="text-right">
              <button @click="viewResourceJson(res)" class="btn btn-secondary text-xs py-1 px-2 text-purple-400" title="View Payload">
                <Code :size="13" /> View Data
              </button>
            </td>
          </tr>
          <tr v-if="filteredResources.length === 0">
            <td colspan="6" class="text-center py-10 text-slate-400 space-y-2">
              <div class="text-sm font-medium">No operational resources found for type [{{ selectedType }}].</div>
              <div v-if="searchQuery" class="pt-1">
                <button @click="searchQuery = ''" class="btn btn-secondary text-xs py-1 px-3">
                  Clear Search Filter
                </button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- JSON Modal -->
    <div v-if="showJsonModal" class="modal-overlay" @click.self="showJsonModal = false">
      <div class="modal-content space-y-4 max-w-2xl">
        <div class="flex items-center justify-between border-b border-slate-700 pb-3">
          <h3 class="text-base font-bold text-white flex items-center gap-2">
            <Code :size="16" class="text-purple-400" />
            Operational Object JSON
          </h3>
          <button @click="showJsonModal = false" class="text-slate-400 hover:text-white">
            <X :size="18" />
          </button>
        </div>
        <pre class="code-view">{{ activePayload }}</pre>
        <div class="flex justify-end">
          <button @click="showJsonModal = false" class="btn btn-secondary">Close</button>
        </div>
      </div>
    </div>
  </div>
</template>
