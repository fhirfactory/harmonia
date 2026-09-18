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
import { Building2, RefreshCw, Send } from 'lucide-vue-next';

const adminStore = useProviderAdminStore();

onMounted(async () => {
  await adminStore.loadAllRegistryData();
});
</script>

<template>
  <div class="space-y-6">
    <div class="flex flex-col md:flex-row md:items-center justify-between gap-4">
      <div>
        <h1 class="text-2xl font-bold text-white">Organisation Directory Management</h1>
        <p class="text-slate-400 text-sm mt-1">
          Master registry of healthcare organizations, network health services, HPI-O identifiers, and parent hierarchies.
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
            <th>Organisation</th>
            <th>HPI-O Identifier</th>
            <th>Type</th>
            <th>Contact Details</th>
            <th>Address</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="org in adminStore.organizations" :key="org.id">
            <td>
              <div class="font-semibold text-white">{{ org.name }}</div>
              <div class="font-mono text-[11px] text-sky-400">{{ org.id }}</div>
            </td>
            <td class="font-mono text-xs text-slate-300">
              {{ org.identifier }}
            </td>
            <td class="text-xs text-slate-300">
              {{ org.type }}
            </td>
            <td class="text-xs text-slate-300">
              <div>{{ org.phone || '-' }}</div>
              <div class="text-slate-400 text-[11px]">{{ org.email || '' }}</div>
            </td>
            <td class="text-xs text-slate-400 max-w-xs truncate">
              {{ org.address || '-' }}
            </td>
            <td>
              <StatusBadge :status="org.active" />
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>
