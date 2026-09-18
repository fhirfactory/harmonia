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
  Server, 
  CheckCircle, 
  XCircle, 
  RotateCcw, 
  Clock, 
  Cpu, 
  HardDrive,
  ChevronRight,
  ShieldAlert
} from 'lucide-vue-next';
import { useOperationsStore } from '../../stores/operationsStore';
import type { OperationalInstance } from '../../models/operations';
import StatusBadge from '../common/StatusBadge.vue';

const store = useOperationsStore();

const instances = computed(() => store.instances);

const onSelectInstance = (instance: OperationalInstance) => {
  store.openInstanceDrawer(instance);
};
</script>

<template>
  <div class="bg-[#151c2c] border border-[#27344d] rounded-xl overflow-hidden shadow-sm">
    <!-- Panel Header -->
    <div class="px-4 py-3 bg-slate-900/60 border-b border-[#27344d] flex items-center justify-between">
      <div class="flex items-center gap-2">
        <Server :size="16" class="text-sky-400" />
        <h2 class="text-sm font-bold text-white tracking-tight uppercase">Runtime Instances</h2>
        <span class="text-xs text-slate-400 font-mono">({{ instances.length }})</span>
      </div>
      <span class="text-xs text-slate-400">Kubernetes Pod &amp; Cluster Telemetry</span>
    </div>

    <!-- Empty State -->
    <div v-if="instances.length === 0" class="p-8 text-center text-slate-400 space-y-2">
      <Server :size="32" class="mx-auto text-slate-600 mb-2" />
      <p class="text-sm font-medium text-slate-300">No runtime instances currently discovered</p>
      <p class="text-xs text-slate-500 max-w-md mx-auto">
        Awaiting Pod lifecycle reports or cluster module status registration for this subsystem.
      </p>
    </div>

    <!-- Table View -->
    <div v-else class="overflow-x-auto">
      <table class="w-full text-left text-xs border-collapse" role="table" aria-label="Subsystem Instances">
        <thead>
          <tr class="bg-slate-900/40 text-slate-400 border-b border-[#27344d] text-[11px] font-semibold tracking-wider uppercase">
            <th class="py-2.5 px-4 font-semibold">Instance ID</th>
            <th class="py-2.5 px-4 font-semibold">Role</th>
            <th class="py-2.5 px-4 font-semibold">State</th>
            <th class="py-2.5 px-4 font-semibold">Readiness</th>
            <th class="py-2.5 px-4 font-semibold text-right">Restarts</th>
            <th class="py-2.5 px-4 font-semibold">Uptime</th>
            <th class="py-2.5 px-4 font-semibold text-right">CPU</th>
            <th class="py-2.5 px-4 font-semibold text-right">Memory</th>
            <th class="py-2.5 px-4 font-semibold text-right">Action</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-[#1f293d] text-slate-200">
          <tr
            v-for="inst in instances"
            :key="inst.instanceId"
            @click="onSelectInstance(inst)"
            class="hover:bg-slate-800/50 transition cursor-pointer group"
          >
            <!-- Instance Name / ID -->
            <td class="py-3 px-4 font-mono font-medium text-white flex items-center gap-2">
              <span class="w-2 h-2 rounded-full" :class="inst.ready ? 'bg-emerald-400' : 'bg-rose-400'"></span>
              <span>{{ inst.instanceId }}</span>
            </td>

            <!-- Role -->
            <td class="py-3 px-4">
              <span class="px-2 py-0.5 rounded text-[10px] font-semibold tracking-wide uppercase bg-slate-800 text-slate-300 border border-slate-700/60">
                {{ inst.role || 'Primary' }}
              </span>
            </td>

            <!-- State -->
            <td class="py-3 px-4">
              <StatusBadge :status="inst.state" size="sm" />
            </td>

            <!-- Readiness -->
            <td class="py-3 px-4">
              <span 
                class="inline-flex items-center gap-1.5 font-medium"
                :class="inst.ready ? 'text-emerald-400' : 'text-rose-400'"
              >
                <CheckCircle v-if="inst.ready" :size="13" />
                <XCircle v-else :size="13" />
                <span>{{ inst.ready ? 'Ready' : 'Not Ready' }}</span>
              </span>
            </td>

            <!-- Restarts -->
            <td class="py-3 px-4 text-right font-mono" :class="inst.restartCount > 0 ? 'text-amber-400 font-bold' : 'text-slate-400'">
              {{ inst.restartCount }}
            </td>

            <!-- Uptime -->
            <td class="py-3 px-4 font-mono text-slate-300">
              {{ inst.uptime || 'N/A' }}
            </td>

            <!-- CPU % (Honest N/A if null/unmeasured) -->
            <td class="py-3 px-4 text-right font-mono">
              <span v-if="inst.cpuPercent !== null && inst.cpuPercent !== undefined" class="text-sky-300">
                {{ inst.cpuPercent }}%
              </span>
              <span v-else class="text-slate-500 italic">N/A</span>
            </td>

            <!-- Memory MB (Honest N/A if null/unmeasured) -->
            <td class="py-3 px-4 text-right font-mono">
              <span v-if="inst.memoryMb !== null && inst.memoryMb !== undefined" class="text-slate-200">
                {{ inst.memoryMb }} MB
              </span>
              <span v-else class="text-slate-500 italic">N/A</span>
            </td>

            <!-- Inspect Action Button -->
            <td class="py-3 px-4 text-right">
              <button
                @click.stop="onSelectInstance(inst)"
                class="inline-flex items-center gap-1 px-2.5 py-1 rounded bg-slate-800 hover:bg-slate-700 text-slate-300 hover:text-white border border-slate-700 text-xs font-medium transition cursor-pointer"
                title="Inspect instance details"
                aria-label="Inspect instance details"
              >
                <span>Inspect</span>
                <ChevronRight :size="12" class="text-slate-400 group-hover:text-sky-400 transition" />
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>
