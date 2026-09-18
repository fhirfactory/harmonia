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
import type { WorkflowSummary } from '../../models/operations';
import { GitMerge, ArrowRight, Layers, Clock, AlertTriangle, Inbox } from 'lucide-vue-next';

const props = withDefaults(defineProps<{
  workflows: WorkflowSummary[];
  loading?: boolean;
}>(), {
  loading: false
});

const emit = defineEmits<{
  (e: 'select', workflow: WorkflowSummary): void;
}>();

function formatRate(rate?: number | null): string {
  if (rate == null || isNaN(rate)) return 'N/A';
  return `${rate.toFixed(1)} /s`;
}

function formatDuration(ms?: number | null): string {
  if (ms == null || isNaN(ms)) return 'N/A';
  if (ms < 1000) return `${ms}ms`;
  return `${(ms / 1000).toFixed(2)}s`;
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
        <GitMerge :size="18" class="text-sky-400" />
        <h3 class="text-sm font-bold text-white tracking-wide uppercase">
          Configured Praxis Workflow Sequences
        </h3>
        <span class="text-xs font-mono px-2 py-0.5 rounded bg-slate-800 text-slate-300 font-semibold">
          {{ workflows.length }}
        </span>
      </div>
      <span class="text-[11px] font-mono text-slate-400">
        Engine: Energeia / Praxis Orchestration
      </span>
    </div>

    <!-- Loading State -->
    <div v-if="loading" class="p-12 text-center space-y-3">
      <div class="inline-block animate-spin rounded-full h-8 w-8 border-2 border-sky-400 border-t-transparent"></div>
      <p class="text-xs text-slate-400 font-mono">Querying Praxis workflow telemetry...</p>
    </div>

    <!-- Empty State -->
    <div v-else-if="workflows.length === 0" class="p-12 text-center space-y-3">
      <Inbox :size="36" class="mx-auto text-slate-600" />
      <p class="text-sm font-semibold text-slate-300">No workflows found</p>
      <p class="text-xs text-slate-500 max-w-sm mx-auto">
        No Praxis workflows match your current search criteria.
      </p>
    </div>

    <!-- Table -->
    <div v-else class="overflow-x-auto">
      <table class="w-full text-left border-collapse" role="table" aria-label="Praxis workflow sequences">
        <thead>
          <tr class="border-b border-slate-800 text-[11px] uppercase tracking-wider text-slate-400 bg-slate-950/70 font-semibold select-none">
            <th scope="col" class="py-3 px-4">Praxis Workflow</th>
            <th scope="col" class="py-3 px-3 text-right">Active</th>
            <th scope="col" class="py-3 px-3 text-right">Queued</th>
            <th scope="col" class="py-3 px-3 text-right">Completed</th>
            <th scope="col" class="py-3 px-3 text-right">Failed</th>
            <th scope="col" class="py-3 px-3 text-right">Retrying</th>
            <th scope="col" class="py-3 px-3 text-right">Rate</th>
            <th scope="col" class="py-3 px-3 text-right">P95 Duration</th>
            <th scope="col" class="py-3 px-4 text-center">Action</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-slate-800/60 text-xs font-mono">
          <tr 
            v-for="w in workflows" 
            :key="w.workflowId"
            class="hover:bg-slate-800/40 transition-colors cursor-pointer group"
            @click="emit('select', w)"
          >
            <!-- Workflow Name / Description -->
            <td class="py-3 px-4 max-w-sm">
              <div class="font-bold text-sky-400 truncate group-hover:text-sky-300 transition-colors" :title="w.name">
                {{ w.name }}
              </div>
              <div class="text-[11px] text-slate-400 font-sans truncate mt-0.5" :title="w.description || w.workflowId">
                {{ w.description || w.workflowId }}
              </div>
              <div class="text-[10px] text-slate-500 font-mono mt-0.5">
                {{ w.workflowId }}
              </div>
            </td>

            <!-- Active Executions -->
            <td class="py-3 px-3 text-right whitespace-nowrap font-bold">
              <span 
                v-if="w.activeExecutions != null && w.activeExecutions > 0"
                class="px-2 py-0.5 rounded text-[11px] bg-sky-500/20 text-sky-400 border border-sky-500/30 font-bold"
              >
                {{ formatCount(w.activeExecutions) }}
              </span>
              <span v-else class="text-slate-500">0</span>
            </td>

            <!-- Queued Work -->
            <td class="py-3 px-3 text-right whitespace-nowrap">
              <span :class="w.queuedWork && w.queuedWork > 0 ? 'text-amber-400 font-bold' : 'text-slate-400'">
                {{ formatCount(w.queuedWork) }}
              </span>
            </td>

            <!-- Completed Work -->
            <td class="py-3 px-3 text-right whitespace-nowrap text-emerald-400 font-semibold">
              {{ formatCount(w.completedWork) }}
            </td>

            <!-- Failed Work -->
            <td class="py-3 px-3 text-right whitespace-nowrap">
              <span 
                v-if="w.failedWork != null && w.failedWork > 0"
                class="px-2 py-0.5 rounded text-[11px] bg-rose-500/20 text-rose-400 border border-rose-500/30 font-bold"
              >
                {{ formatCount(w.failedWork) }}
              </span>
              <span v-else class="text-slate-500">0</span>
            </td>

            <!-- Retrying Work -->
            <td class="py-3 px-3 text-right whitespace-nowrap">
              <span 
                v-if="w.retryingWork != null && w.retryingWork > 0"
                class="px-2 py-0.5 rounded text-[11px] bg-amber-500/20 text-amber-400 border border-amber-500/30 font-bold"
              >
                {{ formatCount(w.retryingWork) }}
              </span>
              <span v-else class="text-slate-500">0</span>
            </td>

            <!-- Processing Rate -->
            <td class="py-3 px-3 text-right whitespace-nowrap text-slate-300">
              {{ formatRate(w.processingRate) }}
            </td>

            <!-- P95 Duration -->
            <td class="py-3 px-3 text-right whitespace-nowrap text-slate-300">
              {{ formatDuration(w.p95DurationMs) }}
            </td>

            <!-- Action -->
            <td class="py-3 px-4 text-center whitespace-nowrap">
              <button 
                type="button"
                class="btn btn-secondary text-[11px] py-1 px-2.5 inline-flex items-center gap-1 font-sans"
                :aria-label="`Drill down into ${w.name}`"
                @click.stop="emit('select', w)"
              >
                <span>Drill Down</span>
                <ArrowRight :size="12" />
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>
