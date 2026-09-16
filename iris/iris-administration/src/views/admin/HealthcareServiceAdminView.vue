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
import { onMounted } from 'vue';
import { useProviderAdminStore } from '../../stores/providerAdminStore';
import StatusBadge from '../../components/StatusBadge.vue';
import { Activity, RefreshCw } from 'lucide-vue-next';

const adminStore = useProviderAdminStore();

onMounted(async () => {
  await adminStore.loadAllRegistryData();
});
</script>

<template>
  <div class="space-y-6">
    <div class="flex flex-col md:flex-row md:items-center justify-between gap-4">
      <div>
        <h1 class="text-2xl font-bold text-white">Healthcare Service Management</h1>
        <p class="text-slate-400 text-sm mt-1">
          Clinical service lines, outpatient programs, and diagnostic services in the Provider Registry.
        </p>
      </div>

      <button @click="adminStore.loadAllRegistryData()" class="btn btn-secondary btn-sm" :disabled="adminStore.loading">
        <RefreshCw :size="14" :class="{ 'animate-spin': adminStore.loading }" />
        <span>Refresh</span>
      </button>
    </div>

    <div class="table-container">
      <table class="table">
        <thead>
          <tr>
            <th>Service Name</th>
            <th>Category &amp; Specialty</th>
            <th>Provider Organisation</th>
            <th>Locations</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="svc in adminStore.services" :key="svc.id">
            <td>
              <div class="font-semibold text-white">{{ svc.name }}</div>
              <div class="font-mono text-[11px] text-purple-400">{{ svc.id }}</div>
            </td>
            <td class="text-xs text-slate-300">
              <div>{{ svc.category }}</div>
              <div class="text-slate-400 text-[11px]">{{ svc.specialty.join(', ') }}</div>
            </td>
            <td class="text-xs text-slate-300">
              {{ svc.organizationName || '-' }}
            </td>
            <td class="text-xs text-slate-400">
              {{ svc.locationNames.join(', ') || '-' }}
            </td>
            <td>
              <StatusBadge :status="svc.active" />
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>
