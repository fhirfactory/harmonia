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
import DataTable from 'primevue/datatable'
import Column from 'primevue/column'
import IrisEmptyState from './IrisEmptyState.vue'
import IrisLoadingState from './IrisLoadingState.vue'
import type { DataTableColumn } from '../../types'

withDefaults(
  defineProps<{
    value: any[]
    columns?: DataTableColumn[]
    loading?: boolean
    emptyMessage?: string
    rowHover?: boolean
    stripedRows?: boolean
    selectionMode?: 'single' | 'multiple'
    selection?: any
    dataKey?: string
  }>(),
  {
    columns: () => [],
    loading: false,
    emptyMessage: 'No records found.',
    rowHover: true,
    stripedRows: false,
    selectionMode: undefined,
    selection: null,
    dataKey: 'id'
  }
)

defineEmits<{
  (e: 'update:selection', value: any): void
  (e: 'row-click', event: any): void
  (e: 'row-select', event: any): void
  (e: 'row-unselect', event: any): void
}>()

function getFieldValue(data: any, field: any): any {
  if (!field) return ''
  if (typeof field === 'function') return field(data)
  return data[field]
}
</script>

<template>
  <div class="iris-data-table-container">
    <DataTable
      :value="value"
      :loading="loading"
      :row-hover="rowHover"
      :striped-rows="stripedRows"
      :selection-mode="selectionMode"
      :selection="selection"
      :data-key="dataKey"
      class="iris-data-table"
      @update:selection="$emit('update:selection', $event)"
      @row-click="$emit('row-click', $event)"
      @row-select="$emit('row-select', $event)"
      @row-unselect="$emit('row-unselect', $event)"
    >
      <template #header v-if="$slots.header">
        <slot name="header" />
      </template>

      <template #empty>
        <slot name="empty">
          <IrisEmptyState :title="emptyMessage" />
        </slot>
      </template>

      <template #loading>
        <slot name="loading">
          <IrisLoadingState message="Loading data..." />
        </slot>
      </template>

      <template v-if="columns && columns.length > 0">
        <Column
          v-for="col in columns"
          :key="col.field"
          :field="col.field"
          :header="col.header"
          :sortable="col.sortable"
          :style="{
            ...(col.width ? { width: col.width } : {}),
            ...(col.align ? { textAlign: col.align } : {})
          }"
        >
          <template #body="{ data, field, index }">
            <slot :name="String(field)" :data="data" :field="field" :index="index">
              {{ getFieldValue(data, field) }}
            </slot>
          </template>
        </Column>
      </template>

      <slot v-else />

      <template #footer v-if="$slots.footer">
        <slot name="footer" />
      </template>
    </DataTable>
  </div>
</template>

<style>
/* Scoped & global overrides for Iris high-density styling */
.iris-data-table-container {
  width: 100%;
  background-color: var(--iris-bg-surface);
  border: 1px solid var(--iris-border-default);
  border-radius: var(--iris-border-radius);
  overflow: hidden;
  box-shadow: var(--iris-shadow-subtle);
}

.iris-data-table {
  font-family: var(--iris-font-sans);
  font-size: 0.84rem;
}

.iris-data-table .p-datatable-header {
  background-color: var(--iris-bg-subtle);
  border-bottom: 1px solid var(--iris-border-default);
  padding: 0.5rem 0.75rem;
  color: var(--iris-text-primary);
}

.iris-data-table .p-datatable-thead > tr > th {
  background-color: var(--iris-bg-subtle);
  color: var(--iris-text-secondary);
  font-weight: 600;
  font-size: 0.75rem;
  text-transform: uppercase;
  letter-spacing: 0.04em;
  padding: 0.4rem 0.75rem;
  border-bottom: 1px solid var(--iris-border-default);
  height: var(--iris-table-row-height);
  white-space: nowrap;
}

.iris-data-table .p-datatable-tbody > tr {
  background-color: var(--iris-bg-surface);
  color: var(--iris-text-primary);
  height: var(--iris-table-row-height);
  transition: background-color 0.1s ease;
}

.iris-data-table .p-datatable-tbody > tr > td {
  padding: 0.35rem 0.75rem;
  border-bottom: 1px solid var(--iris-border-default);
  font-size: 0.8125rem;
  vertical-align: middle;
}

.iris-data-table .p-datatable-tbody > tr:hover {
  background-color: var(--iris-bg-hover);
}

.iris-data-table .p-datatable-tbody > tr.p-highlight {
  background-color: var(--iris-bg-selected);
  color: #0369a1;
}

.iris-data-table .p-datatable-footer {
  background-color: var(--iris-bg-subtle);
  border-top: 1px solid var(--iris-border-default);
  padding: 0.4rem 0.75rem;
  font-size: 0.75rem;
  color: var(--iris-text-muted);
}
</style>
