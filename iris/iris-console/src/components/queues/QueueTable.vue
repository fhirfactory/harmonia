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
import type { QueueSummary } from '../../models/operations';
import { IrisDataTable, IrisStatus, type DataTableColumn } from '@harmonia/iris-befe';
import { Radio, ArrowRight, Inbox } from 'lucide-vue-next';

withDefaults(defineProps<{
  queues: QueueSummary[];
  loading?: boolean;
}>(), {
  loading: false
});

const emit = defineEmits<{
  (e: 'select', queue: QueueSummary): void;
}>();

const tableColumns: DataTableColumn[] = [
  { field: 'status', header: 'Status', width: '130px', sortable: true },
  { field: 'queueName', header: 'Queue Name / Address', sortable: true },
  { field: 'depth', header: 'Depth', width: '90px', sortable: true },
  { field: 'consumerCount', header: 'Consumers', width: '95px', sortable: true },
  { field: 'producerCount', header: 'Producers', width: '95px', sortable: true },
  { field: 'enqueueRate', header: 'Enqueue', width: '95px', sortable: true },
  { field: 'dequeueRate', header: 'Dequeue', width: '95px', sortable: true },
  { field: 'oldestMessageAgeSeconds', header: 'Oldest Age', width: '95px', sortable: true },
  { field: 'dlqDepth', header: 'DLQ', width: '80px', sortable: true },
  { field: 'actions', header: 'Action', width: '90px' }
];

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
  <div class="queue-table-container bg-white border border-slate-200 rounded-lg overflow-hidden shadow-xs font-sans">
    <!-- Panel Header -->
    <div class="px-5 py-3.5 bg-slate-50/70 border-b border-slate-200 flex items-center justify-between">
      <div class="flex items-center gap-2.5">
        <div class="p-1.5 rounded bg-sky-50 text-sky-700 border border-sky-100">
          <Radio :size="15" />
        </div>
        <h3 class="text-xs font-bold text-slate-800 tracking-wider uppercase">
          Petasos Message Queues &amp; Addresses
        </h3>
        <span class="text-xs font-mono px-2 py-0.5 rounded bg-slate-100 text-slate-700 font-semibold border border-slate-200">
          {{ queues.length }}
        </span>
      </div>
      <span class="text-xs text-slate-500 font-mono">
        ActiveMQ Artemis Port 61616
      </span>
    </div>

    <!-- High-Density IrisDataTable -->
    <IrisDataTable
      :value="queues"
      :columns="tableColumns"
      :loading="loading"
      data-key="queueId"
      empty-message="No message queues found"
      @row-click="emit('select', $event.data)"
    >
      <!-- Custom Empty State -->
      <template #empty>
        <div class="p-12 text-center space-y-3">
          <Inbox :size="36" class="mx-auto text-slate-400" />
          <p class="text-sm font-semibold text-slate-700">No message queues found</p>
          <p class="text-xs text-slate-500 max-w-sm mx-auto">
            No Petasos queues match your current search query or status filter. Try clearing filters.
          </p>
        </div>
      </template>

      <!-- Status Column -->
      <template #status="{ data }">
        <IrisStatus :status="data.status || 'UNKNOWN'" size="sm" label-format="upper" />
      </template>

      <!-- Queue Name & Capability Column -->
      <template #queueName="{ data }">
        <div class="max-w-xs">
          <div class="font-bold text-sky-700 truncate group-hover:text-sky-900 transition-colors font-mono" :title="data.queueName">
            {{ data.queueName }}
          </div>
          <div v-if="data.associatedCapability" class="text-[11px] text-slate-500 font-sans truncate" :title="data.associatedCapability">
            {{ data.associatedCapability }}
          </div>
        </div>
      </template>

      <!-- Depth Column -->
      <template #depth="{ data }">
        <span 
          class="font-mono font-bold"
          :class="[
            data.depth == null ? 'text-slate-400' :
            data.depth > 50 ? 'text-rose-700 bg-rose-50 px-1.5 py-0.5 rounded border border-rose-200' : 
            data.depth > 0 ? 'text-amber-700 bg-amber-50 px-1.5 py-0.5 rounded border border-amber-200' : 
            'text-slate-800'
          ]"
        >
          {{ formatCount(data.depth) }}
        </span>
      </template>

      <!-- Consumers Column -->
      <template #consumerCount="{ data }">
        <span class="font-mono" :class="data.consumerCount && data.consumerCount > 0 ? 'text-emerald-700 font-semibold' : 'text-slate-400'">
          {{ formatCount(data.consumerCount) }}
        </span>
      </template>

      <!-- Producers Column -->
      <template #producerCount="{ data }">
        <span class="font-mono text-slate-700">
          {{ formatCount(data.producerCount) }}
        </span>
      </template>

      <!-- Enqueue Rate Column -->
      <template #enqueueRate="{ data }">
        <span class="font-mono text-slate-700">
          {{ formatRate(data.enqueueRate) }}
        </span>
      </template>

      <!-- Dequeue Rate Column -->
      <template #dequeueRate="{ data }">
        <span class="font-mono text-slate-700">
          {{ formatRate(data.dequeueRate) }}
        </span>
      </template>

      <!-- Oldest Message Age Column -->
      <template #oldestMessageAgeSeconds="{ data }">
        <span class="font-mono text-slate-600">
          {{ formatAge(data.oldestMessageAgeSeconds) }}
        </span>
      </template>

      <!-- DLQ Depth Column -->
      <template #dlqDepth="{ data }">
        <span 
          v-if="data.dlqDepth != null && data.dlqDepth > 0"
          class="px-2 py-0.5 rounded text-[11px] font-bold bg-rose-50 text-rose-700 border border-rose-200 font-mono"
        >
          {{ data.dlqDepth }}
        </span>
        <span v-else class="text-slate-400 font-mono">0</span>
      </template>

      <!-- Actions Column -->
      <template #actions="{ data }">
        <button 
          type="button"
          class="inline-flex items-center gap-1 px-2.5 py-1 rounded bg-slate-100 hover:bg-slate-200 text-slate-700 hover:text-slate-900 border border-slate-300 text-xs font-medium transition cursor-pointer font-sans"
          :aria-label="`Inspect details for queue ${data.queueName}`"
          @click.stop="emit('select', data)"
        >
          <span>Details</span>
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
