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
import { computed, ref, onMounted, onUnmounted } from 'vue';
import { 
  Activity, 
  RotateCw, 
  Server, 
  AlertOctagon, 
  AlertTriangle, 
  Layers, 
  Clock, 
  ExternalLink 
} from 'lucide-vue-next';
import { useOperationsStore } from '../../stores/operationsStore';
import StatusBadge from './StatusBadge.vue';

const store = useOperationsStore();

const currentTime = ref(new Date().toLocaleTimeString());
let clockTimer: any = null;

onMounted(() => {
  clockTimer = setInterval(() => {
    currentTime.value = new Date().toLocaleTimeString();
  }, 1000);
});

onUnmounted(() => {
  if (clockTimer) clearInterval(clockTimer);
});

const summary = computed(() => store.summary);

const environment = computed(() => summary.value?.environment || 'PROD / microk8s-01');
const cluster = computed(() => summary.value?.cluster || 'harmonia-cluster');
const platformStatus = computed(() => summary.value?.platformStatus || 'HEALTHY');

const totalSubsystems = computed(() => summary.value?.totalSubsystems ?? store.subsystems.length ?? 9);
const degradedSubsystems = computed(() => summary.value?.degradedSubsystems ?? 0);

const criticalAlerts = computed(() => store.criticalAlertsCount);
const warningAlerts = computed(() => store.warningAlertsCount);

const lastRefreshedText = computed(() => {
  if (!store.lastRefreshed) return 'Just now';
  return store.lastRefreshed.toLocaleTimeString();
});

const handleRefresh = async () => {
  await store.refreshAll();
};
</script>

<template>
  <header class="border-b border-[#1f293d] bg-[#0d1322]/95 backdrop-blur sticky top-0 z-40 text-slate-200">
    <div class="px-4 py-2.5 flex flex-wrap items-center justify-between gap-3">
      <!-- Left: Logo & Environment / Cluster Summary -->
      <div class="flex items-center gap-3 min-w-0">
        <div class="flex items-center gap-2.5">
          <div class="p-1.5 rounded-lg bg-sky-500/10 border border-sky-500/30 text-sky-400 flex items-center justify-center shadow-inner">
            <Activity :size="20" class="text-sky-400" />
          </div>
          <div class="flex flex-col">
            <div class="flex items-center gap-2">
              <span class="font-extrabold text-white text-sm tracking-tight leading-none">HARMONIA</span>
              <span class="text-xs text-slate-400 font-semibold tracking-wider uppercase">Ops Console</span>
            </div>
            <div class="flex items-center gap-2 text-[11px] text-slate-400 mt-0.5 font-mono">
              <span class="text-sky-400 font-medium">{{ environment }}</span>
              <span class="text-slate-600">&bull;</span>
              <span class="text-slate-400">{{ cluster }}</span>
            </div>
          </div>
        </div>

        <!-- Overall Platform Health Badge -->
        <div class="hidden sm:flex items-center border-l border-slate-700/60 pl-3 ml-1">
          <StatusBadge :status="platformStatus" size="md" :show-pulse="true" />
        </div>
      </div>

      <!-- Center / Metric Pills: Subsystems & Alerts Summary -->
      <div class="flex items-center gap-2 flex-wrap">
        <!-- Subsystems count pill -->
        <div 
          class="flex items-center gap-1.5 px-2.5 py-1 rounded-md bg-slate-800/80 border border-slate-700/60 text-xs font-mono"
          title="Subsystem operational status"
        >
          <Layers :size="13" class="text-slate-400" />
          <span class="text-slate-300 font-semibold">{{ totalSubsystems }}</span>
          <span class="text-slate-500">Subsystems</span>
          <span v-if="degradedSubsystems > 0" class="text-amber-400 font-semibold ml-1">
            ({{ degradedSubsystems }} Degraded)
          </span>
        </div>

        <!-- Critical Alerts pill -->
        <router-link
          to="/alerts"
          class="flex items-center gap-1.5 px-2.5 py-1 rounded-md border text-xs font-mono transition-colors"
          :class="criticalAlerts > 0 ? 'bg-rose-500/15 border-rose-500/40 text-rose-300 hover:bg-rose-500/25' : 'bg-slate-800/50 border-slate-700/40 text-slate-400 hover:bg-slate-800'"
          title="Active Critical Alerts"
        >
          <AlertOctagon :size="13" :class="criticalAlerts > 0 ? 'text-rose-400 animate-pulse' : 'text-slate-500'" />
          <span class="font-bold" :class="criticalAlerts > 0 ? 'text-rose-300' : 'text-slate-300'">{{ criticalAlerts }}</span>
          <span>Critical</span>
        </router-link>

        <!-- Warning Alerts pill -->
        <router-link
          to="/alerts"
          class="flex items-center gap-1.5 px-2.5 py-1 rounded-md border text-xs font-mono transition-colors"
          :class="warningAlerts > 0 ? 'bg-amber-500/15 border-amber-500/40 text-amber-300 hover:bg-amber-500/25' : 'bg-slate-800/50 border-slate-700/40 text-slate-400 hover:bg-slate-800'"
          title="Active Warning Alerts"
        >
          <AlertTriangle :size="13" :class="warningAlerts > 0 ? 'text-amber-400' : 'text-slate-500'" />
          <span class="font-bold" :class="warningAlerts > 0 ? 'text-amber-300' : 'text-slate-300'">{{ warningAlerts }}</span>
          <span>Warn</span>
        </router-link>
      </div>

      <!-- Right: Clock, Last Updated & Refresh Trigger -->
      <div class="flex items-center gap-3">
        <!-- Live Clock & Last Updated -->
        <div class="hidden lg:flex flex-col items-end text-[11px] font-mono leading-tight">
          <div class="flex items-center gap-1 text-slate-300">
            <Clock :size="11" class="text-slate-500" />
            <span>{{ currentTime }}</span>
          </div>
          <span class="text-slate-500 text-[10px]">Updated {{ lastRefreshedText }}</span>
        </div>

        <!-- Manual Refresh Button -->
        <button
          @click="handleRefresh"
          :disabled="store.refreshing"
          class="flex items-center gap-1.5 px-2.5 py-1.5 rounded-md bg-slate-800 hover:bg-slate-700/80 active:bg-slate-900 border border-slate-700 text-xs font-medium text-slate-200 transition disabled:opacity-50 cursor-pointer shadow-sm"
          title="Refresh operational telemetry"
          aria-label="Refresh operational telemetry"
        >
          <RotateCw :size="13" :class="store.refreshing ? 'animate-spin text-sky-400' : 'text-slate-400'" />
          <span class="hidden sm:inline">{{ store.refreshing ? 'Refreshing...' : 'Refresh' }}</span>
        </button>

        <!-- Link to Clinical FHIR Explorer (Port 3000) -->
        <a 
          href="http://localhost:3000" 
          target="_blank" 
          rel="noopener noreferrer"
          class="hidden xl:flex items-center gap-1.5 px-2 py-1.5 rounded-md bg-sky-500/10 hover:bg-sky-500/20 border border-sky-500/20 text-xs font-medium text-sky-300 transition"
          title="Open Clinical FHIR Resource Explorer"
        >
          <span>FHIR UI</span>
          <ExternalLink :size="11" class="text-sky-400" />
        </a>
      </div>
    </div>
  </header>
</template>
