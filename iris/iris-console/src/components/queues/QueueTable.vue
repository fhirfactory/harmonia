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
  <div class="queue-table">
    <!-- Panel Header -->
    <div class="queue-table__header">
      <Radio :size="15" class="queue-table__header-icon" aria-hidden="true" />
      <h3 class="queue-table__title">Petasos message queues &amp; addresses</h3>
      <span class="queue-table__count">{{ queues.length }}</span>
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
        <div class="queue-table__empty">
          <Inbox :size="32" aria-hidden="true" />
          <p class="queue-table__empty-title">No message queues found</p>
          <p class="queue-table__empty-detail">
            No Petasos queues match the current search query or status filter.
          </p>
        </div>
      </template>

      <!-- Status Column -->
      <template #status="{ data }">
        <IrisStatus :status="data.status || 'UNKNOWN'" size="sm" label-format="upper" />
      </template>

      <!-- Queue Name & Capability Column -->
      <template #queueName="{ data }">
        <div class="queue-table__name">
          <span class="queue-table__name-primary" :title="data.queueName">{{ data.queueName }}</span>
          <span
            v-if="data.associatedCapability"
            class="queue-table__name-secondary"
            :title="data.associatedCapability"
          >
            {{ data.associatedCapability }}
          </span>
        </div>
      </template>

      <!-- Depth Column -->
      <template #depth="{ data }">
        <span
          class="queue-table__metric queue-table__metric--strong"
          :class="{
            'queue-table__metric--muted': data.depth == null,
            'queue-table__metric--bad': data.depth != null && data.depth > 50,
            'queue-table__metric--warn': data.depth != null && data.depth > 0 && data.depth <= 50
          }"
        >
          {{ formatCount(data.depth) }}
        </span>
      </template>

      <!-- Consumers Column -->
      <template #consumerCount="{ data }">
        <span
          class="queue-table__metric"
          :class="data.consumerCount && data.consumerCount > 0
            ? 'queue-table__metric--ok'
            : 'queue-table__metric--muted'"
        >
          {{ formatCount(data.consumerCount) }}
        </span>
      </template>

      <!-- Producers Column -->
      <template #producerCount="{ data }">
        <span class="queue-table__metric">{{ formatCount(data.producerCount) }}</span>
      </template>

      <!-- Enqueue Rate Column -->
      <template #enqueueRate="{ data }">
        <span class="queue-table__metric">{{ formatRate(data.enqueueRate) }}</span>
      </template>

      <!-- Dequeue Rate Column -->
      <template #dequeueRate="{ data }">
        <span class="queue-table__metric">{{ formatRate(data.dequeueRate) }}</span>
      </template>

      <!-- Oldest Message Age Column -->
      <template #oldestMessageAgeSeconds="{ data }">
        <span class="queue-table__metric">{{ formatAge(data.oldestMessageAgeSeconds) }}</span>
      </template>

      <!-- DLQ Depth Column -->
      <template #dlqDepth="{ data }">
        <span
          v-if="data.dlqDepth != null && data.dlqDepth > 0"
          class="queue-table__metric queue-table__metric--strong queue-table__metric--bad"
        >
          {{ data.dlqDepth }}
        </span>
        <span v-else class="queue-table__metric queue-table__metric--muted">0</span>
      </template>

      <!-- Actions Column -->
      <template #actions="{ data }">
        <button
          type="button"
          class="queue-table__details-btn"
          :aria-label="`Inspect details for queue ${data.queueName}`"
          @click.stop="emit('select', data)"
        >
          <span>Details</span>
          <ArrowRight :size="12" aria-hidden="true" />
        </button>
      </template>
    </IrisDataTable>
  </div>
</template>

<style scoped>
.queue-table {
  overflow: hidden;
  background-color: var(--iris-bg-surface);
  border: 1px solid var(--iris-border-default);
  border-radius: var(--iris-border-radius);
  font-family: var(--iris-font-sans);
}

.queue-table__header {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 0.75rem;
  background-color: var(--iris-bg-subtle);
  border-bottom: 1px solid var(--iris-border-default);
}

.queue-table__header-icon {
  flex-shrink: 0;
  color: var(--iris-text-accent);
}

.queue-table__title {
  margin: 0;
  font-size: 0.75rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.04em;
  color: var(--iris-text-secondary);
}

.queue-table__count {
  padding: 1px 6px;
  font-family: var(--iris-font-mono);
  font-size: 0.6875rem;
  font-weight: 700;
  color: var(--iris-text-primary);
  background-color: var(--iris-bg-surface);
  border: 1px solid var(--iris-border-default);
  border-radius: var(--iris-border-radius);
}

.queue-table__empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.375rem;
  padding: 2rem 1rem;
  text-align: center;
  color: var(--iris-text-muted);
}

.queue-table__empty-title {
  margin: 0;
  font-size: 0.875rem;
  font-weight: 600;
  color: var(--iris-text-secondary);
}

.queue-table__empty-detail {
  margin: 0;
  max-width: 24rem;
  font-size: 0.75rem;
}

.queue-table__name {
  display: flex;
  flex-direction: column;
  min-width: 0;
}

.queue-table__name-primary {
  font-family: var(--iris-font-mono);
  font-weight: 700;
  color: var(--iris-text-accent);
  word-break: break-all;
}

.queue-table__name-secondary {
  font-size: 0.6875rem;
  color: var(--iris-text-muted);
}

.queue-table__metric {
  font-family: var(--iris-font-mono);
  font-size: 0.75rem;
  color: var(--iris-text-secondary);
}

.queue-table__metric--strong {
  font-weight: 700;
  color: var(--iris-text-primary);
}

.queue-table__metric--muted {
  color: var(--iris-text-muted);
}

.queue-table__metric--ok {
  font-weight: 600;
  color: var(--iris-status-healthy-text);
}

.queue-table__metric--warn {
  color: var(--iris-status-degraded-text);
}

.queue-table__metric--bad {
  color: var(--iris-status-unavailable-text);
}

.queue-table__details-btn {
  display: inline-flex;
  align-items: center;
  gap: 0.25rem;
  padding: 0.1875rem 0.5rem;
  font-family: inherit;
  font-size: 0.75rem;
  font-weight: 600;
  color: var(--iris-text-accent);
  background-color: var(--iris-bg-surface);
  border: 1px solid var(--iris-border-default);
  border-radius: var(--iris-border-radius);
  cursor: pointer;
}

.queue-table__details-btn:hover {
  background-color: var(--iris-bg-hover);
}

:deep(.p-datatable-tbody > tr) {
  cursor: pointer;
}
</style>
