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
import { 
  User, Mail, Phone, MapPin, Award, 
  Send, RefreshCw, Calendar, ShieldCheck
} from 'lucide-vue-next';

const selfServiceStore = useSelfServiceStore();

onMounted(async () => {
  await selfServiceStore.loadProviderProfile();
});
</script>

<template>
  <div class="space-y-6">
    <!-- Header with Action -->
    <div class="flex flex-col md:flex-row md:items-center justify-between gap-4">
      <div>
        <h1 class="text-2xl font-bold text-white">My Provider Details</h1>
        <p class="text-slate-400 text-sm mt-1">
          Authoritative Practitioner demographics, national identifiers, and qualifications in the Harmonia Provider Registry.
        </p>
      </div>

      <div class="flex items-center gap-2">
        <button @click="selfServiceStore.loadProviderProfile()" class="btn btn-secondary btn-sm" :disabled="selfServiceStore.loading">
          <RefreshCw :size="14" :class="{ 'animate-spin': selfServiceStore.loading }" />
          <span>Refresh</span>
        </button>
        <router-link to="/self-service/request-change" class="btn btn-primary btn-sm">
          <Send :size="14" />
          <span>Request Details Update</span>
        </router-link>
      </div>
    </div>

    <div v-if="selfServiceStore.loading && !selfServiceStore.practitioner" class="card text-center py-12 text-slate-400">
      <RefreshCw :size="24" class="animate-spin mx-auto text-sky-400 mb-2" />
      <span>Loading provider credentials...</span>
    </div>

    <div v-else-if="selfServiceStore.practitioner" class="grid grid-cols-1 lg:grid-cols-3 gap-6">
      <!-- Left Column: Identity Card -->
      <div class="card space-y-6">
        <div class="flex items-center gap-3">
          <div class="w-14 h-14 rounded-full bg-sky-500/15 border border-sky-500/30 flex items-center justify-center text-sky-400 font-bold text-xl">
            <User :size="28" />
          </div>
          <div>
            <h2 class="text-lg font-bold text-white">{{ selfServiceStore.practitionerSummary?.name }}</h2>
            <p class="text-xs font-mono text-sky-400">{{ selfServiceStore.practitioner.id }}</p>
          </div>
        </div>

        <div class="space-y-3 pt-4 border-t border-slate-800 text-sm">
          <div class="flex justify-between items-center">
            <span class="text-slate-400">Status:</span>
            <StatusBadge :status="selfServiceStore.practitioner.active !== false" />
          </div>

          <div class="flex justify-between items-center">
            <span class="text-slate-400">Gender:</span>
            <span class="capitalize text-slate-200">{{ selfServiceStore.practitioner.gender || 'Not Specified' }}</span>
          </div>

          <div class="flex justify-between items-center">
            <span class="text-slate-400">Date of Birth:</span>
            <span class="font-mono text-slate-200">{{ selfServiceStore.practitioner.birthDate || 'Not Recorded' }}</span>
          </div>

          <div class="flex justify-between items-center">
            <span class="text-slate-400">Active Roles:</span>
            <span class="badge badge-info">{{ selfServiceStore.roles.length }} Assigned</span>
          </div>

          <div class="flex justify-between items-center">
            <span class="text-slate-400">Last Version:</span>
            <span class="font-mono text-xs text-slate-300">v{{ selfServiceStore.practitioner.meta?.versionId || '1' }}</span>
          </div>
        </div>
      </div>

      <!-- Center & Right: Identifiers, Telecom, Qualifications, Address -->
      <div class="lg:col-span-2 space-y-6">
        <!-- National & Healthcare Identifiers -->
        <div class="card space-y-4">
          <div class="flex items-center gap-2 text-sky-400">
            <ShieldCheck :size="18" />
            <h3 class="text-base font-bold text-white">Registered Healthcare Identifiers</h3>
          </div>

          <div class="grid grid-cols-1 md:grid-cols-2 gap-3">
            <div 
              v-for="(id, idx) in selfServiceStore.practitioner.identifier" 
              :key="idx"
              class="p-3 bg-slate-950/70 rounded border border-slate-800 space-y-1"
            >
              <div class="flex justify-between items-center">
                <span class="text-xs font-bold text-slate-400 uppercase">{{ id.type?.text || 'National Identifier' }}</span>
                <span class="badge badge-success text-[10px]">VERIFIED</span>
              </div>
              <div class="font-mono text-sm font-semibold text-slate-100">{{ id.value }}</div>
              <div class="text-[10px] text-slate-500 truncate">{{ id.system }}</div>
            </div>
          </div>
        </div>

        <!-- Qualifications & Specialties -->
        <div class="card space-y-4">
          <div class="flex items-center gap-2 text-indigo-400">
            <Award :size="18" />
            <h3 class="text-base font-bold text-white">Qualifications &amp; Degrees</h3>
          </div>

          <div class="space-y-2">
            <div 
              v-for="(q, idx) in selfServiceStore.practitioner.qualification" 
              :key="idx"
              class="p-3 bg-slate-950/70 rounded border border-slate-800 flex items-center justify-between"
            >
              <div>
                <span class="font-semibold text-sm text-slate-200">{{ q.code?.text || q.code?.coding?.[0]?.display }}</span>
                <p v-if="q.code?.coding?.[0]?.code" class="text-xs font-mono text-slate-400">
                  SNOMED CT: {{ q.code.coding[0].code }}
                </p>
              </div>
              <span class="badge badge-accent text-xs">Primary</span>
            </div>
          </div>
        </div>

        <!-- Contact Points & Address -->
        <div class="card space-y-4">
          <div class="flex items-center gap-2 text-emerald-400">
            <Mail :size="18" />
            <h3 class="text-base font-bold text-white">Contact Information &amp; Address</h3>
          </div>

          <div class="grid grid-cols-1 md:grid-cols-2 gap-4 text-sm">
            <div class="space-y-2">
              <span class="text-xs font-bold uppercase text-slate-400">Electronic Telecom</span>
              <div 
                v-for="(t, idx) in selfServiceStore.practitioner.telecom" 
                :key="idx"
                class="flex items-center gap-2 text-slate-200"
              >
                <Mail v-if="t.system === 'email'" :size="14" class="text-sky-400" />
                <Phone v-else :size="14" class="text-emerald-400" />
                <span class="font-mono text-xs">{{ t.value }}</span>
                <span v-if="t.use" class="text-[10px] uppercase text-slate-500">({{ t.use }})</span>
              </div>
            </div>

            <div class="space-y-2">
              <span class="text-xs font-bold uppercase text-slate-400">Registered Practice Address</span>
              <div v-if="selfServiceStore.practitioner.address?.length" class="text-slate-200 text-xs flex items-start gap-2">
                <MapPin :size="14" class="text-amber-400 shrink-0 mt-0.5" />
                <div>
                  <p v-for="(line, lidx) in selfServiceStore.practitioner.address[0].line" :key="lidx">{{ line }}</p>
                  <p>{{ selfServiceStore.practitioner.address[0].city }}, {{ selfServiceStore.practitioner.address[0].state }} {{ selfServiceStore.practitioner.address[0].postalCode }}</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
