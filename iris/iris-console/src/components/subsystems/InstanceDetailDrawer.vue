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
import { computed, onMounted, onUnmounted } from 'vue';
import { 
  X, 
  Server, 
  Box, 
  Layers, 
  Network, 
  Clock, 
  Activity, 
  AlertOctagon, 
  CheckCircle, 
  Cpu, 
  HardDrive,
  ShieldCheck,
  ShieldAlert
} from 'lucide-vue-next';
import { useOperationsStore } from '../../stores/operationsStore';
import StatusBadge from '../common/StatusBadge.vue';

const store = useOperationsStore();

const isOpen = computed(() => store.isInstanceDrawerOpen);
const instance = computed(() => store.selectedInstance);

const close = () => {
  store.closeInstanceDrawer();
};

const handleKeydown = (e: KeyboardEvent) => {
  if (e.key === 'Escape' && isOpen.value) {
    close();
  }
};

onMounted(() => {
  window.addEventListener('keydown', handleKeydown);
});

onUnmounted(() => {
  window.removeEventListener('keydown', handleKeydown);
});

const startedAtText = computed(() => {
  if (!instance.value?.startedAt) return 'Unknown';
  return new Date(instance.value.startedAt).toLocaleString();
});
</script>

<template>
  <teleport to="body">
    <!-- Backdrop -->
    <div
      v-if="isOpen"
      class="fixed inset-0 bg-black/60 backdrop-blur-sm z-50 transition-opacity"
      @click="close"
      aria-hidden="true"
    ></div>

    <!-- Drawer Panel -->
    <aside
      v-if="isOpen && instance"
      class="fixed inset-y-0 right-0 max-w-full w-full sm:w-[480px] lg:w-[540px] bg-[#0d1322] border-l border-[#27344d] z-50 flex flex-col shadow-2xl transition-transform transform duration-300 ease-in-out font-sans text-slate-200"
      role="dialog"
      aria-modal="true"
      :aria-label="`Instance Details: ${instance.instanceId}`"
    >
      <!-- Header -->
      <div class="px-6 py-4 bg-[#111827] border-b border-[#1f293d] flex items-center justify-between">
        <div class="flex items-center gap-3 min-w-0">
          <div class="p-2 rounded-lg bg-sky-500/10 border border-sky-500/30 text-sky-400 shrink-0">
            <Server :size="20" />
          </div>
          <div class="min-w-0">
            <h2 class="text-base font-bold text-white tracking-tight truncate font-mono">
              {{ instance.instanceId }}
            </h2>
            <p class="text-xs text-slate-400 capitalize">
              Role: <span class="text-slate-200 font-medium">{{ instance.role || 'Primary' }}</span>
              &bull; Subsystem: <span class="text-sky-400 uppercase font-mono">{{ instance.subsystemId }}</span>
            </p>
          </div>
        </div>

        <button
          @click="close"
          class="p-2 text-slate-400 hover:text-white hover:bg-slate-800 rounded-lg transition"
          title="Close drawer (ESC)"
          aria-label="Close drawer"
        >
          <X :size="18" />
        </button>
      </div>

      <!-- Drawer Body -->
      <div class="flex-1 overflow-y-auto p-6 space-y-6">
        <!-- Status & Readiness Banner -->
        <div class="p-4 rounded-xl bg-[#151c2c] border border-[#27344d] flex items-center justify-between">
          <div>
            <span class="text-[11px] font-semibold text-slate-400 uppercase tracking-wider block mb-1">State</span>
            <StatusBadge :status="instance.state" size="md" :show-pulse="true" />
          </div>

          <div class="text-right">
            <span class="text-[11px] font-semibold text-slate-400 uppercase tracking-wider block mb-1">Readiness Probe</span>
            <span 
              class="inline-flex items-center gap-1.5 text-xs font-semibold"
              :class="instance.ready ? 'text-emerald-400' : 'text-rose-400'"
            >
              <CheckCircle v-if="instance.ready" :size="14" />
              <AlertOctagon v-else :size="14" />
              <span>{{ instance.ready ? 'Ready (Serving)' : 'Not Ready' }}</span>
            </span>
          </div>
        </div>

        <!-- Kubernetes Pod Metadata -->
        <div class="space-y-3">
          <h3 class="text-xs font-bold text-slate-300 uppercase tracking-wider flex items-center gap-1.5">
            <Box :size="14" class="text-sky-400" />
            <span>Kubernetes Pod Spec</span>
          </h3>

          <div class="bg-[#151c2c] border border-[#27344d] rounded-xl p-4 space-y-2.5 text-xs font-mono">
            <div class="flex justify-between py-1 border-b border-slate-800">
              <span class="text-slate-400 font-sans">Pod Name:</span>
              <span class="text-slate-200 select-all">{{ instance.podName || instance.instanceId }}</span>
            </div>
            <div class="flex justify-between py-1 border-b border-slate-800">
              <span class="text-slate-400 font-sans">Namespace:</span>
              <span class="text-sky-400">{{ instance.namespace || 'harmonia' }}</span>
            </div>
            <div class="flex justify-between py-1 border-b border-slate-800">
              <span class="text-slate-400 font-sans">Node:</span>
              <span class="text-slate-200">{{ instance.nodeName || 'microk8s-node-01' }}</span>
            </div>
            <div class="flex justify-between py-1 border-b border-slate-800">
              <span class="text-slate-400 font-sans">Pod IP:</span>
              <span class="text-slate-300">{{ instance.ipAddress || '10.1.0.42' }}</span>
            </div>
            <div class="flex justify-between py-1 border-b border-slate-800">
              <span class="text-slate-400 font-sans">Container Image:</span>
              <span class="text-slate-300 truncate max-w-[280px]" :title="instance.containerImage || 'docker.io/fhirfactory/harmonia'">
                {{ instance.containerImage || 'harmonia/' + instance.subsystemId + ':1.0.0-SNAPSHOT' }}
              </span>
            </div>
            <div class="flex justify-between py-1">
              <span class="text-slate-400 font-sans">App Version:</span>
              <span class="text-emerald-400">{{ instance.appVersion || '1.0.0-SNAPSHOT' }}</span>
            </div>
          </div>
        </div>

        <!-- Runtime Metrics & Restarts -->
        <div class="space-y-3">
          <h3 class="text-xs font-bold text-slate-300 uppercase tracking-wider flex items-center gap-1.5">
            <Activity :size="14" class="text-emerald-400" />
            <span>Runtime Resource Telemetry</span>
          </h3>

          <div class="grid grid-cols-3 gap-2">
            <!-- Restarts -->
            <div class="p-3 bg-[#151c2c] border border-[#27344d] rounded-xl text-center">
              <span class="text-[10px] text-slate-400 uppercase font-semibold block mb-0.5">Restarts</span>
              <span 
                class="text-lg font-bold font-mono block"
                :class="instance.restartCount > 0 ? 'text-amber-400' : 'text-slate-300'"
              >
                {{ instance.restartCount }}
              </span>
            </div>

            <!-- CPU -->
            <div class="p-3 bg-[#151c2c] border border-[#27344d] rounded-xl text-center">
              <span class="text-[10px] text-slate-400 uppercase font-semibold block mb-0.5">CPU</span>
              <span class="text-lg font-bold font-mono text-sky-400 block">
                {{ instance.cpuPercent !== null && instance.cpuPercent !== undefined ? `${instance.cpuPercent}%` : 'N/A' }}
              </span>
            </div>

            <!-- Memory -->
            <div class="p-3 bg-[#151c2c] border border-[#27344d] rounded-xl text-center">
              <span class="text-[10px] text-slate-400 uppercase font-semibold block mb-0.5">Memory</span>
              <span class="text-lg font-bold font-mono text-slate-200 block">
                {{ instance.memoryMb !== null && instance.memoryMb !== undefined ? `${instance.memoryMb}M` : 'N/A' }}
              </span>
            </div>
          </div>

          <div class="bg-[#151c2c] border border-[#27344d] rounded-xl p-3 text-xs flex justify-between font-mono">
            <span class="text-slate-400 font-sans">Started At:</span>
            <span class="text-slate-300">{{ startedAtText }}</span>
          </div>
        </div>

        <!-- Recent Operational Errors (Zero PHI) -->
        <div class="space-y-3">
          <h3 class="text-xs font-bold text-slate-300 uppercase tracking-wider flex items-center gap-1.5">
            <ShieldAlert :size="14" class="text-amber-400" />
            <span>Recent Operational Errors</span>
          </h3>

          <div class="bg-[#151c2c] border border-[#27344d] rounded-xl p-4">
            <div 
              v-if="instance.recentErrors && instance.recentErrors.length > 0"
              class="space-y-2"
            >
              <div 
                v-for="(err, i) in instance.recentErrors"
                :key="i"
                class="p-2.5 rounded bg-rose-500/10 border border-rose-500/20 text-rose-300 text-xs font-mono break-all"
              >
                {{ err }}
              </div>
            </div>
            <div v-else class="text-xs text-slate-400 flex items-center gap-2">
              <CheckCircle :size="14" class="text-emerald-400 shrink-0" />
              <span>No recent operational errors recorded on this runtime instance.</span>
            </div>
          </div>
        </div>

        <!-- Dependencies -->
        <div v-if="instance.dependencies && instance.dependencies.length > 0" class="space-y-3">
          <h3 class="text-xs font-bold text-slate-300 uppercase tracking-wider flex items-center gap-1.5">
            <Network :size="14" class="text-sky-400" />
            <span>Associated Endpoints / Dependencies</span>
          </h3>

          <div class="flex flex-wrap gap-1.5">
            <span 
              v-for="dep in instance.dependencies"
              :key="dep"
              class="px-2.5 py-1 rounded bg-slate-800 border border-slate-700 text-xs text-slate-300 font-mono"
            >
              {{ dep }}
            </span>
          </div>
        </div>
      </div>

      <!-- Drawer Footer -->
      <div class="p-4 bg-[#111827] border-t border-[#1f293d] flex items-center justify-between text-xs text-slate-500 font-mono">
        <span>Zero-PHI Presentation Tier</span>
        <button
          @click="close"
          class="px-3 py-1.5 rounded bg-slate-800 hover:bg-slate-700 text-slate-300 hover:text-white border border-slate-700 font-sans transition cursor-pointer"
        >
          Close Drawer
        </button>
      </div>
    </aside>
  </teleport>
</template>
