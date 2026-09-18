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
import { Briefcase, RefreshCw, Send } from 'lucide-vue-next';

const adminStore = useProviderAdminStore();

onMounted(async () => {
  await adminStore.loadAllRegistryData();
});
</script>

<template>
  <div class="space-y-6">
    <div class="flex flex-col md:flex-row md:items-center justify-between gap-4">
      <div>
        <h1 class="text-2xl font-bold text-white">PractitionerRole Management</h1>
        <p class="text-slate-400 text-sm mt-1">
          Professional role associations connecting practitioners to organizations, specialty codes, and delivery locations.
        </p>
      </div>

      <div class="flex items-center gap-2">
        <button @click="adminStore.loadAllRegistryData()" class="btn btn-secondary btn-sm" :disabled="adminStore.loading">
          <RefreshCw :size="14" :class="{ 'animate-spin': adminStore.loading }" />
          <span>Refresh</span>
        </button>
        <router-link to="/self-service/request-change" class="btn btn-primary btn-sm">
          <Send :size="14" />
          <span>Submit Role Change</span>
        </router-link>
      </div>
    </div>

    <div class="table-container">
      <table class="table">
        <thead>
          <tr>
            <th>Role ID &amp; Code</th>
            <th>Practitioner</th>
            <th>Affiliated Organisation</th>
            <th>Specialties</th>
            <th>Locations</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="role in adminStore.roles" :key="role.id">
            <td>
              <div class="font-semibold text-white">{{ role.code }}</div>
              <div class="font-mono text-[11px] text-sky-400">{{ role.id }}</div>
            </td>
            <td>
              <div class="text-slate-200">{{ role.practitionerName || role.practitionerId }}</div>
              <div class="font-mono text-[10px] text-slate-500">{{ role.practitionerId }}</div>
            </td>
            <td>
              <div class="text-slate-200">{{ role.organizationName }}</div>
              <div class="font-mono text-[10px] text-slate-500">{{ role.organizationId }}</div>
            </td>
            <td class="text-xs">
              <span v-for="(s, sidx) in role.specialty" :key="sidx" class="badge badge-accent text-[10px] mr-1">
                {{ s }}
              </span>
            </td>
            <td class="text-xs text-slate-400">
              <span v-for="(l, lidx) in role.locationNames" :key="lidx">{{ l }}</span>
            </td>
            <td>
              <StatusBadge :status="role.active" />
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>
