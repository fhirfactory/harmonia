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
import { computed } from 'vue';
import { 
  HeartPulse, 
  Network, 
  AlertTriangle, 
  RotateCcw, 
  Clock, 
  Activity, 
  Layers,
  ArrowRight
} from 'lucide-vue-next';
import { useOperationsStore } from '../../stores/operationsStore';
import StatusBadge from '../common/StatusBadge.vue';

const store = useOperationsStore();

const health = computed(() => store.currentHealth);
const isStale = computed(() => store.isStale || health.value?.stale);

const dependencies = computed(() => health.value?.dependencies || []);
</script>

<template>
  <div class="bg-[#151c2c] border border-[#27344d] rounded-xl overflow-hidden shadow-sm">
    <!-- Panel Header -->
    <div class="px-4 py-3 bg-slate-900/60 border-b border-[#27344d] flex items-center justify-between">
      <div class="flex items-center gap-2">
        <HeartPulse :size="16" class="text-emerald-400" />
        <h2 class="text-sm font-bold text-white tracking-tight uppercase">Operational Health &amp; Dependencies</h2>
      </div>
      
      <div v-if="isStale" class="flex items-center gap-1.5 text-xs text-purple-300 font-mono">
        <Clock :size="13" class="text-purple-400 animate-pulse" />
        <span>Telemetry Snapshot (Stale)</span>
      </div>
      <div v-else class="text-xs text-slate-400">
        Live Health Probes &amp; Upstream Latencies
      </div>
    </div>

    <!-- Health Overview Cards -->
    <div class="p-4 grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-6 gap-3 border-b border-[#27344d]/60 bg-slate-900/20">
      <!-- Status -->
      <div class="p-3 rounded-lg bg-[#0f172a] border border-slate-800">
        <span class="text-[10px] font-semibold text-slate-400 uppercase tracking-wider block mb-1">Service Status</span>
        <StatusBadge :status="health?.status || 'UNKNOWN'" size="sm" :stale="isStale" />
      </div>

      <!-- Dependencies Ratio -->
      <div class="p-3 rounded-lg bg-[#0f172a] border border-slate-800">
        <span class="text-[10px] font-semibold text-slate-400 uppercase tracking-wider block mb-0.5">Dependencies</span>
        <span class="text-sm font-bold text-white font-mono block">
          {{ health?.dependenciesSummary || (dependencies.length > 0 ? `${dependencies.length} Connected` : 'None') }}
        </span>
      </div>

      <!-- Availability % -->
      <div class="p-3 rounded-lg bg-[#0f172a] border border-slate-800">
        <span class="text-[10px] font-semibold text-slate-400 uppercase tracking-wider block mb-0.5">Availability</span>
        <span class="text-sm font-bold font-mono block" :class="health?.availabilityPercent && health.availabilityPercent < 99.0 ? 'text-amber-400' : 'text-emerald-400'">
          {{ health?.availabilityPercent !== null && health?.availabilityPercent !== undefined ? `${health.availabilityPercent}%` : 'N/A' }}
        </span>
      </div>

      <!-- P95 Latency -->
      <div class="p-3 rounded-lg bg-[#0f172a] border border-slate-800">
        <span class="text-[10px] font-semibold text-slate-400 uppercase tracking-wider block mb-0.5">P95 Latency</span>
        <span class="text-sm font-bold text-sky-400 font-mono block">
          {{ health?.p95LatencyMs !== null && health?.p95LatencyMs !== undefined ? `${health.p95LatencyMs} ms` : 'N/A' }}
        </span>
      </div>

      <!-- Failed Operations -->
      <div class="p-3 rounded-lg bg-[#0f172a] border border-slate-800">
        <span class="text-[10px] font-semibold text-slate-400 uppercase tracking-wider block mb-0.5">Failed Ops</span>
        <span 
          class="text-sm font-bold font-mono block"
          :class="(health?.failedOperations || 0) > 0 ? 'text-rose-400' : 'text-slate-300'"
        >
          {{ health?.failedOperations ?? 0 }}
        </span>
      </div>

      <!-- Restarts -->
      <div class="p-3 rounded-lg bg-[#0f172a] border border-slate-800">
        <span class="text-[10px] font-semibold text-slate-400 uppercase tracking-wider block mb-0.5">Restarts</span>
        <span 
          class="text-sm font-bold font-mono block"
          :class="(health?.restartCount || 0) > 0 ? 'text-amber-400' : 'text-slate-300'"
        >
          {{ health?.restartCount ?? 0 }}
        </span>
      </div>
    </div>

    <!-- Dependencies Table -->
    <div class="p-4 space-y-3">
      <div class="flex items-center justify-between">
        <h3 class="text-xs font-bold text-slate-300 uppercase tracking-wider flex items-center gap-1.5">
          <Network :size="14" class="text-sky-400" />
          <span>Subsystem Dependencies &amp; Round-Trip Latency</span>
        </h3>
        <span class="text-[11px] text-slate-500 font-mono">{{ dependencies.length }} Downstream Link{{ dependencies.length === 1 ? '' : 's' }}</span>
      </div>

      <div v-if="dependencies.length === 0" class="p-6 text-center text-xs text-slate-500 italic bg-slate-900/30 rounded-lg border border-slate-800">
        No external subsystem dependencies registered for this component.
      </div>

      <div v-else class="overflow-x-auto rounded-lg border border-slate-800">
        <table class="w-full text-left text-xs border-collapse" role="table" aria-label="Subsystem Dependencies">
          <thead>
            <tr class="bg-slate-900/80 text-slate-400 border-b border-slate-800 text-[11px] font-semibold tracking-wider uppercase">
              <th class="py-2.5 px-3">Target Subsystem</th>
              <th class="py-2.5 px-3">Status</th>
              <th class="py-2.5 px-3 text-right">Round-Trip Latency</th>
              <th class="py-2.5 px-3">Diagnostic Message</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-slate-800/80 text-slate-200">
            <tr 
              v-for="dep in dependencies" 
              :key="dep.name"
              class="hover:bg-slate-800/40 transition"
            >
              <td class="py-2.5 px-3 font-semibold text-white flex items-center gap-1.5">
                <ArrowRight :size="12" class="text-sky-400" />
                <span>{{ dep.name }}</span>
              </td>
              <td class="py-2.5 px-3">
                <StatusBadge :status="dep.status" size="sm" />
              </td>
              <td class="py-2.5 px-3 text-right font-mono">
                <span v-if="dep.latencyMs !== null && dep.latencyMs !== undefined" class="text-sky-300">
                  {{ dep.latencyMs }} ms
                </span>
                <span v-else class="text-slate-500 italic">N/A</span>
              </td>
              <td class="py-2.5 px-3 text-slate-400 font-mono text-[11px] truncate max-w-xs" :title="dep.message || 'Operational check passed'">
                {{ dep.message || 'Operational check passed' }}
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>
