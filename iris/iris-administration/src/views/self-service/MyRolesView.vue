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
import { Briefcase, Building2, MapPin, Activity, Mail, Phone, Send, Plus } from 'lucide-vue-next';

const selfServiceStore = useSelfServiceStore();

onMounted(async () => {
  await selfServiceStore.loadProviderProfile();
});
</script>

<template>
  <div class="space-y-6">
    <div class="flex flex-col md:flex-row md:items-center justify-between gap-4">
      <div>
        <h1 class="text-2xl font-bold text-white">My Professional Roles</h1>
        <p class="text-slate-400 text-sm mt-1">
          Active practitioner affiliations, healthcare specialties, service locations, and direct contact channels.
        </p>
      </div>

      <router-link to="/self-service/request-change" class="btn btn-primary btn-sm">
        <Send :size="14" />
        <span>Request Role Change</span>
      </router-link>
    </div>

    <div v-if="selfServiceStore.roles.length === 0" class="card text-center py-12 text-slate-400">
      <Briefcase :size="32" class="mx-auto text-slate-600 mb-2" />
      <p>No active professional roles found for your practitioner identity.</p>
    </div>

    <div v-else class="space-y-4">
      <div 
        v-for="role in selfServiceStore.roles" 
        :key="role.id"
        class="card space-y-4 hover:border-sky-500/40 transition-all"
      >
        <div class="flex flex-col md:flex-row md:items-center justify-between gap-2 border-b border-slate-800 pb-3">
          <div class="flex items-center gap-3">
            <div class="w-10 h-10 rounded-lg bg-indigo-500/10 border border-indigo-500/30 flex items-center justify-center text-indigo-400">
              <Briefcase :size="20" />
            </div>
            <div>
              <h2 class="text-base font-bold text-white">{{ role.code }}</h2>
              <span class="font-mono text-xs text-sky-400">{{ role.id }}</span>
            </div>
          </div>
          <StatusBadge :status="role.active" />
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 text-xs">
          <!-- Organization -->
          <div class="space-y-1">
            <span class="font-bold text-slate-400 uppercase tracking-wider flex items-center gap-1">
              <Building2 :size="13" class="text-sky-400" />
              <span>Affiliated Organisation</span>
            </span>
            <p class="text-slate-200 font-semibold text-sm">{{ role.organizationName }}</p>
            <span class="font-mono text-[10px] text-slate-500">{{ role.organizationId }}</span>
          </div>

          <!-- Specialty -->
          <div class="space-y-1">
            <span class="font-bold text-slate-400 uppercase tracking-wider flex items-center gap-1">
              <Activity :size="13" class="text-indigo-400" />
              <span>Specialties &amp; Focus</span>
            </span>
            <div class="flex flex-wrap gap-1">
              <span 
                v-for="(spec, sidx) in role.specialty" 
                :key="sidx"
                class="badge badge-accent text-[11px]"
              >
                {{ spec }}
              </span>
              <span v-if="role.specialty.length === 0" class="text-slate-500 italic">General Practice</span>
            </div>
          </div>

          <!-- Locations & Services -->
          <div class="space-y-1">
            <span class="font-bold text-slate-400 uppercase tracking-wider flex items-center gap-1">
              <MapPin :size="13" class="text-emerald-400" />
              <span>Delivery Sites &amp; Clinics</span>
            </span>
            <div v-if="role.locationNames.length" class="text-slate-300">
              <p v-for="(loc, lidx) in role.locationNames" :key="lidx">{{ loc }}</p>
            </div>
            <div v-if="role.serviceNames.length" class="text-slate-400 text-[11px]">
              <p v-for="(svc, svidx) in role.serviceNames" :key="svidx">&bull; {{ svc }}</p>
            </div>
            <span v-if="!role.locationNames.length && !role.serviceNames.length" class="text-slate-500 italic">Multiple sites</span>
          </div>

          <!-- Role Direct Telecom -->
          <div class="space-y-1">
            <span class="font-bold text-slate-400 uppercase tracking-wider">Role Telecom</span>
            <div v-for="(t, tidx) in role.telecom" :key="tidx" class="flex items-center gap-1.5 text-slate-200">
              <Mail v-if="t.system === 'email'" :size="12" class="text-sky-400" />
              <Phone v-else :size="12" class="text-emerald-400" />
              <span class="font-mono">{{ t.value }}</span>
            </div>
            <span v-if="!role.telecom.length" class="text-slate-500 italic">Uses central organization contact</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
