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
import { Building2, Phone, Mail, MapPin, ShieldCheck, Send } from 'lucide-vue-next';

const selfServiceStore = useSelfServiceStore();

onMounted(async () => {
  await selfServiceStore.loadProviderProfile();
});
</script>

<template>
  <div class="space-y-6">
    <div class="flex flex-col md:flex-row md:items-center justify-between gap-4">
      <div>
        <h1 class="text-2xl font-bold text-white">My Affiliated Organisations</h1>
        <p class="text-slate-400 text-sm mt-1">
          Healthcare organizations and health services where your practitioner credentials are actively associated.
        </p>
      </div>

      <router-link to="/self-service/request-change" class="btn btn-primary btn-sm">
        <Send :size="14" />
        <span>Request Affiliation Update</span>
      </router-link>
    </div>

    <div v-if="selfServiceStore.organizations.length === 0" class="card text-center py-12 text-slate-400">
      <Building2 :size="32" class="mx-auto text-slate-600 mb-2" />
      <p>No affiliated organizations found for your practitioner record.</p>
    </div>

    <div v-else class="grid grid-cols-1 md:grid-cols-2 gap-5">
      <div 
        v-for="org in selfServiceStore.organizations" 
        :key="org.id"
        class="card space-y-4 hover:border-sky-500/40"
      >
        <div class="flex items-start justify-between">
          <div class="flex items-center gap-3">
            <div class="w-10 h-10 rounded-lg bg-sky-500/10 border border-sky-500/30 flex items-center justify-center text-sky-400">
              <Building2 :size="20" />
            </div>
            <div>
              <h2 class="text-base font-bold text-white">{{ org.name }}</h2>
              <span class="font-mono text-xs text-sky-400">{{ org.id }}</span>
            </div>
          </div>
          <StatusBadge :status="org.active !== false" />
        </div>

        <div class="space-y-2.5 text-xs pt-2 border-t border-slate-800">
          <div v-if="org.identifier?.length" class="flex justify-between">
            <span class="text-slate-400">Identifier:</span>
            <span class="font-mono text-slate-200">{{ org.identifier[0].value }} ({{ org.identifier[0].type?.text || 'HPI-O' }})</span>
          </div>

          <div v-if="org.type?.length" class="flex justify-between">
            <span class="text-slate-400">Type:</span>
            <span class="text-slate-200">{{ org.type[0].text || org.type[0].coding?.[0]?.display }}</span>
          </div>

          <div v-if="org.address?.length" class="flex items-start justify-between gap-2">
            <span class="text-slate-400">Address:</span>
            <span class="text-right text-slate-200">
              {{ org.address[0].line?.join(', ') }}, {{ org.address[0].city }} {{ org.address[0].state }} {{ org.address[0].postalCode }}
            </span>
          </div>

          <div v-if="org.telecom?.length" class="flex items-center justify-between">
            <span class="text-slate-400">Contact:</span>
            <div class="flex items-center gap-2 text-slate-200 font-mono">
              <span v-for="(t, tidx) in org.telecom" :key="tidx">
                {{ t.value }}
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
