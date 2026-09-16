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
import { onMounted, computed } from 'vue';
import { useProviderAdminStore } from '../../stores/providerAdminStore';
import StatusBadge from '../../components/StatusBadge.vue';
import { 
  Users, Building2, Briefcase, MapPin, 
  ClipboardList, CheckCircle2, AlertTriangle, ArrowRight,
  RefreshCw, Activity, Server
} from 'lucide-vue-next';

const adminStore = useProviderAdminStore();

onMounted(async () => {
  await adminStore.loadAllRegistryData();
});

const pendingQueueCount = computed(() => {
  return adminStore.workQueue.filter(
    q => ['REQUESTED', 'ACCEPTED', 'IN_PROGRESS', 'VALIDATING'].includes(q.status)
  ).length;
});
</script>

<template>
  <div class="space-y-6">
    <!-- Header -->
    <div class="flex flex-col md:flex-row md:items-center justify-between gap-4">
      <div>
        <h1 class="text-2xl font-bold text-white">Provider Administration Dashboard</h1>
        <p class="text-slate-400 text-sm mt-1">
          Master registry metrics, change pipeline queue volume, and referential integrity oversight.
        </p>
      </div>

      <button @click="adminStore.loadAllRegistryData()" class="btn btn-secondary btn-sm" :disabled="adminStore.loading">
        <RefreshCw :size="14" :class="{ 'animate-spin': adminStore.loading }" />
        <span>Refresh Registry</span>
      </button>
    </div>

    <!-- Metric Stat Cards -->
    <div class="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-6 gap-4">
      <!-- Practitioners -->
      <div class="card p-4 space-y-2">
        <div class="flex items-center justify-between text-slate-400">
          <span class="text-xs font-bold uppercase tracking-wider">Practitioners</span>
          <Users :size="16" class="text-sky-400" />
        </div>
        <div class="text-2xl font-bold text-white">{{ adminStore.practitioners.length }}</div>
        <router-link to="/admin/practitioners" class="text-[11px] text-sky-400 hover:text-sky-300 flex items-center gap-1 font-semibold">
          <span>Manage</span>
          <ArrowRight :size="10" />
        </router-link>
      </div>

      <!-- PractitionerRoles -->
      <div class="card p-4 space-y-2">
        <div class="flex items-center justify-between text-slate-400">
          <span class="text-xs font-bold uppercase tracking-wider">Roles</span>
          <Briefcase :size="16" class="text-indigo-400" />
        </div>
        <div class="text-2xl font-bold text-white">{{ adminStore.roles.length }}</div>
        <router-link to="/admin/roles" class="text-[11px] text-indigo-400 hover:text-indigo-300 flex items-center gap-1 font-semibold">
          <span>Manage</span>
          <ArrowRight :size="10" />
        </router-link>
      </div>

      <!-- Organizations -->
      <div class="card p-4 space-y-2">
        <div class="flex items-center justify-between text-slate-400">
          <span class="text-xs font-bold uppercase tracking-wider">Organisations</span>
          <Building2 :size="16" class="text-emerald-400" />
        </div>
        <div class="text-2xl font-bold text-white">{{ adminStore.organizations.length }}</div>
        <router-link to="/admin/organizations" class="text-[11px] text-emerald-400 hover:text-emerald-300 flex items-center gap-1 font-semibold">
          <span>Manage</span>
          <ArrowRight :size="10" />
        </router-link>
      </div>

      <!-- Locations -->
      <div class="card p-4 space-y-2">
        <div class="flex items-center justify-between text-slate-400">
          <span class="text-xs font-bold uppercase tracking-wider">Locations</span>
          <MapPin :size="16" class="text-amber-400" />
        </div>
        <div class="text-2xl font-bold text-white">{{ adminStore.locations.length }}</div>
        <router-link to="/admin/locations" class="text-[11px] text-amber-400 hover:text-amber-300 flex items-center gap-1 font-semibold">
          <span>Manage</span>
          <ArrowRight :size="10" />
        </router-link>
      </div>

      <!-- Healthcare Services -->
      <div class="card p-4 space-y-2">
        <div class="flex items-center justify-between text-slate-400">
          <span class="text-xs font-bold uppercase tracking-wider">Services</span>
          <Activity :size="16" class="text-purple-400" />
        </div>
        <div class="text-2xl font-bold text-white">{{ adminStore.services.length }}</div>
        <router-link to="/admin/services" class="text-[11px] text-purple-400 hover:text-purple-300 flex items-center gap-1 font-semibold">
          <span>Manage</span>
          <ArrowRight :size="10" />
        </router-link>
      </div>

      <!-- Work Queue -->
      <div class="card p-4 space-y-2 border-amber-500/40 bg-amber-950/10">
        <div class="flex items-center justify-between text-amber-300">
          <span class="text-xs font-bold uppercase tracking-wider">Active Queue</span>
          <ClipboardList :size="16" class="text-amber-400" />
        </div>
        <div class="text-2xl font-bold text-amber-400">{{ pendingQueueCount }}</div>
        <router-link to="/admin/work-queue" class="text-[11px] text-amber-300 hover:text-amber-200 flex items-center gap-1 font-semibold">
          <span>Triage Queue</span>
          <ArrowRight :size="10" />
        </router-link>
      </div>
    </div>

    <!-- Active Work Queue Preview & Quick Actions -->
    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
      <div class="lg:col-span-2 card space-y-4">
        <div class="flex items-center justify-between border-b border-slate-800 pb-3">
          <div class="flex items-center gap-2 text-amber-400 font-bold">
            <ClipboardList :size="18" />
            <h2 class="text-base text-white">Pending Change Requests (Triage)</h2>
          </div>
          <router-link to="/admin/work-queue" class="text-xs text-sky-400 hover:text-sky-300 font-semibold">
            View All ({{ adminStore.workQueue.length }})
          </router-link>
        </div>

        <div v-if="adminStore.workQueue.length === 0" class="text-center py-8 text-slate-500 text-xs">
          No pending change requests in work queue.
        </div>

        <div v-else class="space-y-2.5">
          <div 
            v-for="item in adminStore.workQueue.slice(0, 4)" 
            :key="item.taskId"
            class="p-3 bg-slate-950/70 rounded border border-slate-800 flex items-center justify-between hover:border-slate-700 text-xs"
          >
            <div class="space-y-1">
              <div class="flex items-center gap-2 font-semibold text-slate-200">
                <span>{{ item.operation }} {{ item.resourceType }}</span>
                <span class="font-mono text-slate-400">({{ item.taskId }})</span>
              </div>
              <div class="text-slate-400 text-[11px]">
                Requester: <span class="text-slate-300">{{ item.requester }}</span> &bull; {{ new Date(item.submittedAt).toLocaleDateString() }}
              </div>
            </div>

            <div class="flex items-center gap-3">
              <StatusBadge :status="item.status" />
              <router-link to="/admin/work-queue" class="btn btn-secondary btn-sm text-xs">
                Inspect
              </router-link>
            </div>
          </div>
        </div>
      </div>

      <!-- Quick Directory Search Launcher -->
      <div class="card space-y-4">
        <div class="flex items-center gap-2 text-sky-400 font-bold">
          <Users :size="18" />
          <h2 class="text-base text-white">Administrative Actions</h2>
        </div>

        <div class="space-y-2">
          <router-link to="/admin/search" class="w-full btn btn-primary justify-start gap-3">
            <Users :size="16" />
            <span>Search Provider Registry</span>
          </router-link>

          <router-link to="/admin/data-quality" class="w-full btn btn-secondary justify-start gap-3">
            <CheckCircle2 :size="16" class="text-emerald-400" />
            <span>Data Quality &amp; Integrity</span>
          </router-link>

          <router-link to="/self-service/request-change" class="w-full btn btn-secondary justify-start gap-3">
            <ClipboardList :size="16" class="text-amber-400" />
            <span>Submit Change Request</span>
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>
