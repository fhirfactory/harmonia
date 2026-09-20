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
import type { WorkflowSummary } from '../../models/operations';
import { IrisDataTable, type DataTableColumn } from '@harmonia/iris-befe';
import { GitMerge, ArrowRight, Inbox } from 'lucide-vue-next';

withDefaults(defineProps<{
  workflows: WorkflowSummary[];
  loading?: boolean;
}>(), {
  loading: false
});

const emit = defineEmits<{
  (e: 'select', workflow: WorkflowSummary): void;
}>();

const tableColumns: DataTableColumn[] = [
  { field: 'name', header: 'Praxis Workflow', sortable: true },
  { field: 'activeExecutions', header: 'Active', width: '85px', sortable: true },
  { field: 'queuedWork', header: 'Queued', width: '85px', sortable: true },
  { field: 'completedWork', header: 'Completed', width: '95px', sortable: true },
  { field: 'failedWork', header: 'Failed', width: '85px', sortable: true },
  { field: 'retryingWork', header: 'Retrying', width: '85px', sortable: true },
  { field: 'processingRate', header: 'Rate', width: '95px', sortable: true },
  { field: 'p95DurationMs', header: 'P95 Duration', width: '105px', sortable: true },
  { field: 'actions', header: 'Action', width: '105px' }
];

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
  <div class="workflow-table-container bg-white border border-slate-200 rounded-lg overflow-hidden shadow-xs font-sans">
    <!-- Panel Header -->
    <div class="px-5 py-3.5 bg-slate-50/70 border-b border-slate-200 flex items-center justify-between">
      <div class="flex items-center gap-2.5">
        <div class="p-1.5 rounded bg-sky-50 text-sky-700 border border-sky-100">
          <GitMerge :size="15" />
        </div>
        <h3 class="text-xs font-bold text-slate-800 tracking-wider uppercase">
          Configured Praxis Workflow Sequences
        </h3>
        <span class="text-xs font-mono px-2 py-0.5 rounded bg-slate-100 text-slate-700 font-semibold border border-slate-200">
          {{ workflows.length }}
        </span>
      </div>
      <span class="text-xs text-slate-500 font-mono">
        Engine: Energeia / Praxis Orchestration
      </span>
    </div>

    <!-- High-Density IrisDataTable -->
    <IrisDataTable
      :value="workflows"
      :columns="tableColumns"
      :loading="loading"
      data-key="workflowId"
      empty-message="No workflows found"
      @row-click="emit('select', $event.data)"
    >
      <!-- Custom Empty State -->
      <template #empty>
        <div class="p-12 text-center space-y-3">
          <Inbox :size="36" class="mx-auto text-slate-400" />
          <p class="text-sm font-semibold text-slate-700">No workflows found</p>
          <p class="text-xs text-slate-500 max-w-sm mx-auto font-sans">
            No Praxis workflows match your current search criteria.
          </p>
        </div>
      </template>

      <!-- Workflow Name / Description -->
      <template #name="{ data }">
        <div class="max-w-sm">
          <div class="font-bold text-sky-700 truncate group-hover:text-sky-900 transition-colors" :title="data.name">
            {{ data.name }}
          </div>
          <div class="text-[11px] text-slate-500 font-sans truncate mt-0.5" :title="data.description || data.workflowId">
            {{ data.description || data.workflowId }}
          </div>
          <div class="text-[10px] text-slate-400 font-mono mt-0.5">
            {{ data.workflowId }}
          </div>
        </div>
      </template>

      <!-- Active Executions -->
      <template #activeExecutions="{ data }">
        <span 
          v-if="data.activeExecutions != null && data.activeExecutions > 0"
          class="px-2 py-0.5 rounded text-[11px] bg-sky-50 text-sky-700 border border-sky-200 font-bold font-mono"
        >
          {{ formatCount(data.activeExecutions) }}
        </span>
        <span v-else class="text-slate-400 font-mono">0</span>
      </template>

      <!-- Queued Work -->
      <template #queuedWork="{ data }">
        <span class="font-mono" :class="data.queuedWork && data.queuedWork > 0 ? 'text-amber-700 font-bold' : 'text-slate-600'">
          {{ formatCount(data.queuedWork) }}
        </span>
      </template>

      <!-- Completed Work -->
      <template #completedWork="{ data }">
        <span class="font-mono text-emerald-700 font-semibold">
          {{ formatCount(data.completedWork) }}
        </span>
      </template>

      <!-- Failed Work -->
      <template #failedWork="{ data }">
        <span 
          v-if="data.failedWork != null && data.failedWork > 0"
          class="px-2 py-0.5 rounded text-[11px] bg-rose-50 text-rose-700 border border-rose-200 font-bold font-mono"
        >
          {{ formatCount(data.failedWork) }}
        </span>
        <span v-else class="text-slate-400 font-mono">0</span>
      </template>

      <!-- Retrying Work -->
      <template #retryingWork="{ data }">
        <span 
          v-if="data.retryingWork != null && data.retryingWork > 0"
          class="px-2 py-0.5 rounded text-[11px] bg-amber-50 text-amber-700 border border-amber-200 font-bold font-mono"
        >
          {{ formatCount(data.retryingWork) }}
        </span>
        <span v-else class="text-slate-400 font-mono">0</span>
      </template>

      <!-- Processing Rate -->
      <template #processingRate="{ data }">
        <span class="font-mono text-slate-700">
          {{ formatRate(data.processingRate) }}
        </span>
      </template>

      <!-- P95 Duration -->
      <template #p95DurationMs="{ data }">
        <span class="font-mono text-slate-700">
          {{ formatDuration(data.p95DurationMs) }}
        </span>
      </template>

      <!-- Actions -->
      <template #actions="{ data }">
        <button 
          type="button"
          class="inline-flex items-center gap-1 px-2.5 py-1 rounded bg-slate-100 hover:bg-slate-200 text-slate-700 hover:text-slate-900 border border-slate-300 text-xs font-medium transition cursor-pointer font-sans"
          :aria-label="`Drill down into ${data.name}`"
          @click.stop="emit('select', data)"
        >
          <span>Drill Down</span>
          <ArrowRight :size="12" class="text-slate-400 group-hover:text-sky-600 transition" />
        </button>
      </template>
    </IrisDataTable>
  </div>
</template>

<style scoped>
:deep(.p-datatable-tbody > tr) {
  cursor: pointer;
}
</style>
