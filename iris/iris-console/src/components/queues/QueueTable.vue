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
import type { QueueSummary } from '../../models/operations';
import StatusBadge from '../common/StatusBadge.vue';
import { Radio, ArrowRight, AlertTriangle, Layers, Inbox } from 'lucide-vue-next';

const props = withDefaults(defineProps<{
  queues: QueueSummary[];
  loading?: boolean;
}>(), {
  loading: false
});

const emit = defineEmits<{
  (e: 'select', queue: QueueSummary): void;
}>();

function formatRate(rate?: number | null): string {
  if (rate == null || isNaN(rate)) return 'N/A';
  return `${rate.toFixed(1)} /s`;
}

function formatAge(seconds?: number | null): string {
  if (seconds == null || isNaN(seconds)) return 'N/A';
  if (seconds === 0) return '0s';
  if (seconds < 60) return `${seconds}s`;
  const mins = Math.floor(seconds / 60);
  const secs = seconds % 60;
  if (mins < 60) return `${mins}m ${secs}s`;
  const hrs = Math.floor(mins / 60);
  return `${hrs}h ${mins % 60}m`;
}

function formatCount(val?: number | null): string {
  if (val == null || isNaN(val)) return 'N/A';
  return val.toLocaleString();
}
</script>

<template>
  <div class="card p-0 overflow-hidden border border-slate-800 bg-slate-900/60 shadow-xl rounded-xl">
    <div class="p-4 border-b border-slate-800/80 flex items-center justify-between bg-slate-950/40">
      <div class="flex items-center gap-2">
        <Radio :size="18" class="text-sky-400" />
        <h3 class="text-sm font-bold text-white tracking-wide uppercase">
          Petasos Message Queues &amp; Addresses
        </h3>
        <span class="text-xs font-mono px-2 py-0.5 rounded bg-slate-800 text-slate-300 font-semibold">
          {{ queues.length }}
        </span>
      </div>
      <span class="text-[11px] font-mono text-slate-400">
        Broker Protocol: tcp://0.0.0.0:61616 (Artemis Core)
      </span>
    </div>

    <!-- Loading State -->
    <div v-if="loading" class="p-12 text-center space-y-3">
      <div class="inline-block animate-spin rounded-full h-8 w-8 border-2 border-sky-400 border-t-transparent"></div>
      <p class="text-xs text-slate-400 font-mono">Querying Petasos messaging telemetry...</p>
    </div>

    <!-- Empty State -->
    <div v-else-if="queues.length === 0" class="p-12 text-center space-y-3">
      <Inbox :size="36" class="mx-auto text-slate-600" />
      <p class="text-sm font-semibold text-slate-300">No message queues found</p>
      <p class="text-xs text-slate-500 max-w-sm mx-auto">
        No Petasos queues match your current search query or status filter. Try clearing filters.
      </p>
    </div>

    <!-- Table -->
    <div v-else class="overflow-x-auto">
      <table class="w-full text-left border-collapse" role="table" aria-label="Petasos message queues list">
        <thead>
          <tr class="border-b border-slate-800 text-[11px] uppercase tracking-wider text-slate-400 bg-slate-950/70 font-semibold select-none">
            <th scope="col" class="py-3 px-4">Status</th>
            <th scope="col" class="py-3 px-4">Queue Name / Address</th>
            <th scope="col" class="py-3 px-3 text-right">Depth</th>
            <th scope="col" class="py-3 px-3 text-right">Consumers</th>
            <th scope="col" class="py-3 px-3 text-right">Producers</th>
            <th scope="col" class="py-3 px-3 text-right">Enqueue</th>
            <th scope="col" class="py-3 px-3 text-right">Dequeue</th>
            <th scope="col" class="py-3 px-3 text-right">Oldest Age</th>
            <th scope="col" class="py-3 px-3 text-right">DLQ</th>
            <th scope="col" class="py-3 px-4 text-center">Action</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-slate-800/60 text-xs font-mono">
          <tr 
            v-for="q in queues" 
            :key="q.queueId"
            class="hover:bg-slate-800/40 transition-colors cursor-pointer group"
            @click="emit('select', q)"
          >
            <!-- Status -->
            <td class="py-3 px-4 whitespace-nowrap">
              <StatusBadge :status="q.status || 'UNKNOWN'" size="sm" />
            </td>

            <!-- Queue Name / Capability -->
            <td class="py-3 px-4 max-w-xs">
              <div class="font-bold text-sky-400 truncate group-hover:text-sky-300 transition-colors" :title="q.queueName">
                {{ q.queueName }}
              </div>
              <div v-if="q.associatedCapability" class="text-[11px] text-slate-400 font-sans truncate" :title="q.associatedCapability">
                {{ q.associatedCapability }}
              </div>
            </td>

            <!-- Depth -->
            <td class="py-3 px-3 text-right whitespace-nowrap font-bold">
              <span 
                :class="[
                  q.depth == null ? 'text-slate-500' :
                  q.depth > 50 ? 'text-rose-400 bg-rose-500/10 px-1.5 py-0.5 rounded border border-rose-500/20' : 
                  q.depth > 0 ? 'text-amber-400 bg-amber-500/10 px-1.5 py-0.5 rounded border border-amber-500/20' : 
                  'text-slate-300'
                ]"
              >
                {{ formatCount(q.depth) }}
              </span>
            </td>

            <!-- Consumers -->
            <td class="py-3 px-3 text-right whitespace-nowrap">
              <span :class="q.consumerCount && q.consumerCount > 0 ? 'text-emerald-400 font-semibold' : 'text-slate-500'">
                {{ formatCount(q.consumerCount) }}
              </span>
            </td>

            <!-- Producers -->
            <td class="py-3 px-3 text-right whitespace-nowrap text-slate-300">
              {{ formatCount(q.producerCount) }}
            </td>

            <!-- Enqueue Rate -->
            <td class="py-3 px-3 text-right whitespace-nowrap text-slate-300">
              {{ formatRate(q.enqueueRate) }}
            </td>

            <!-- Dequeue Rate -->
            <td class="py-3 px-3 text-right whitespace-nowrap text-slate-300">
              {{ formatRate(q.dequeueRate) }}
            </td>

            <!-- Oldest Age -->
            <td class="py-3 px-3 text-right whitespace-nowrap text-slate-400">
              {{ formatAge(q.oldestMessageAgeSeconds) }}
            </td>

            <!-- DLQ Depth -->
            <td class="py-3 px-3 text-right whitespace-nowrap">
              <span 
                v-if="q.dlqDepth != null && q.dlqDepth > 0"
                class="px-2 py-0.5 rounded text-[11px] font-bold bg-rose-500/20 text-rose-400 border border-rose-500/30"
              >
                {{ q.dlqDepth }}
              </span>
              <span v-else class="text-slate-500">0</span>
            </td>

            <!-- Action -->
            <td class="py-3 px-4 text-center whitespace-nowrap">
              <button 
                type="button"
                class="p-1.5 rounded-lg text-slate-400 hover:text-white hover:bg-slate-700/60 transition-colors inline-flex items-center gap-1 text-[11px] font-sans"
                :aria-label="`Inspect details for queue ${q.queueName}`"
                @click.stop="emit('select', q)"
              >
                <span>Details</span>
                <ArrowRight :size="13" />
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>
