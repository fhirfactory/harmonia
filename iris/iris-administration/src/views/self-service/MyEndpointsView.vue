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
import { useSelfServiceStore } from '../../stores/selfServiceStore';
import StatusBadge from '../../components/StatusBadge.vue';
import { Server, Send, Globe, Shield } from 'lucide-vue-next';

const selfServiceStore = useSelfServiceStore();

onMounted(async () => {
  await selfServiceStore.loadProviderProfile();
});
</script>

<template>
  <div class="space-y-6">
    <div class="flex flex-col md:flex-row md:items-center justify-between gap-4">
      <div>
        <h1 class="text-2xl font-bold text-white">My Electronic Communication Endpoints</h1>
        <p class="text-slate-400 text-sm mt-1">
          Configured digital communication channels, Secure Message Delivery (SMD) endpoints, and direct integration URIs.
        </p>
      </div>

      <router-link to="/self-service/request-change" class="btn btn-primary btn-sm">
        <Send :size="14" />
        <span>Request Endpoint Update</span>
      </router-link>
    </div>

    <div v-if="selfServiceStore.endpoints.length === 0" class="card text-center py-12 text-slate-400">
      <Server :size="32" class="mx-auto text-slate-600 mb-2" />
      <p>No communication endpoints associated with your roles.</p>
    </div>

    <div v-else class="grid grid-cols-1 md:grid-cols-2 gap-4">
      <div 
        v-for="ep in selfServiceStore.endpoints" 
        :key="ep.id"
        class="card space-y-3"
      >
        <div class="flex justify-between items-start">
          <div class="flex items-center gap-3">
            <div class="w-10 h-10 rounded-lg bg-sky-500/10 border border-sky-500/30 flex items-center justify-center text-sky-400">
              <Server :size="20" />
            </div>
            <div>
              <h3 class="text-base font-bold text-white">{{ ep.name }}</h3>
              <span class="font-mono text-xs text-sky-400">{{ ep.id }}</span>
            </div>
          </div>
          <StatusBadge :status="ep.status || 'active'" />
        </div>

        <div class="space-y-2 text-xs text-slate-300 pt-2 border-t border-slate-800">
          <div class="flex justify-between">
            <span class="text-slate-400">Connection Protocol:</span>
            <span class="font-semibold text-slate-200">{{ ep.connectionType?.[0]?.text || 'Secure Message Delivery' }}</span>
          </div>

          <div class="space-y-1">
            <span class="text-slate-400">Address / URI:</span>
            <div class="p-2 bg-slate-950 rounded font-mono text-[11px] text-emerald-400 break-all border border-slate-800">
              {{ ep.address }}
            </div>
          </div>

          <div v-if="ep.managingOrganization?.display" class="flex justify-between">
            <span class="text-slate-400">Managing Entity:</span>
            <span>{{ ep.managingOrganization.display }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
