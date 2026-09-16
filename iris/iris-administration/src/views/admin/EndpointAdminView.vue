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
import { Server, RefreshCw } from 'lucide-vue-next';

const adminStore = useProviderAdminStore();

onMounted(async () => {
  await adminStore.loadAllRegistryData();
});
</script>

<template>
  <div class="space-y-6">
    <div class="flex flex-col md:flex-row md:items-center justify-between gap-4">
      <div>
        <h1 class="text-2xl font-bold text-white">Endpoint Directory Management</h1>
        <p class="text-slate-400 text-sm mt-1">
          Electronic communication channels, Secure Message Delivery (SMD) URIs, and direct protocol gateways.
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
            <th>Endpoint</th>
            <th>Protocol / Connection</th>
            <th>Target URI</th>
            <th>Managing Organisation</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="ep in adminStore.endpoints" :key="ep.id">
            <td>
              <div class="font-semibold text-white">{{ ep.name }}</div>
              <div class="font-mono text-[11px] text-sky-400">{{ ep.id }}</div>
            </td>
            <td class="text-xs text-slate-300">
              {{ ep.connectionType }}
            </td>
            <td class="font-mono text-[11px] text-emerald-400 max-w-sm truncate">
              {{ ep.address }}
            </td>
            <td class="text-xs text-slate-300">
              {{ ep.organizationName || '-' }}
            </td>
            <td>
              <StatusBadge :status="ep.status" />
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>
