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
import { computed } from 'vue'
import { RefreshCw, Download, Search, Loader2 } from 'lucide-vue-next'
import type { ToolbarAction } from '../../types'

const props = withDefaults(
  defineProps<{
    searchQuery?: string
    searchPlaceholder?: string
    showSearch?: boolean
    timeWindow?: string
    timeWindowOptions?: string[]
    showTimeWindow?: boolean
    showRefresh?: boolean
    refreshing?: boolean
    disabled?: boolean
    showExport?: boolean
    exporting?: boolean
    actions?: ToolbarAction[]
  }>(),
  {
    searchQuery: '',
    searchPlaceholder: 'Search...',
    showSearch: false,
    timeWindow: '1h',
    timeWindowOptions: () => ['15m', '1h', '6h', '24h'],
    showTimeWindow: false,
    showRefresh: true,
    refreshing: false,
    disabled: false,
    showExport: false,
    exporting: false,
    actions: () => []
  }
)

const emit = defineEmits<{
  (e: 'update:searchQuery', value: string): void
  (e: 'update:timeWindow', value: string): void
  (e: 'refresh'): void
  (e: 'export'): void
  (e: 'action', actionId: string): void
}>()

const searchValue = computed({
  get: () => props.searchQuery,
  set: (val: string) => emit('update:searchQuery', val)
})

function handleTimeWindowSelect(opt: string) {
  emit('update:timeWindow', opt)
}
</script>

<template>
  <div class="iris-toolbar" role="toolbar" aria-label="Page actions toolbar">
    <div class="iris-toolbar__left">
      <slot name="left" />

      <!-- Search input -->
      <div v-if="showSearch" class="iris-toolbar__search">
        <Search :size="14" class="iris-toolbar__search-icon" aria-hidden="true" />
        <input
          v-model="searchValue"
          type="search"
          :placeholder="searchPlaceholder"
          :disabled="disabled"
          class="iris-toolbar__search-input"
          aria-label="Search filter"
        />
      </div>

      <slot name="filter" />
    </div>

    <div class="iris-toolbar__right">
      <slot name="right" />

      <!-- Time Window Selector Group -->
      <div
        v-if="showTimeWindow && timeWindowOptions.length > 0"
        class="iris-toolbar__btn-group"
        role="group"
        aria-label="Time window selection"
      >
        <button
          v-for="opt in timeWindowOptions"
          :key="opt"
          type="button"
          class="iris-toolbar__group-btn"
          :class="{ 'iris-toolbar__group-btn--active': timeWindow === opt }"
          :disabled="disabled"
          @click="handleTimeWindowSelect(opt)"
        >
          {{ opt }}
        </button>
      </div>

      <!-- Action buttons passed via props -->
      <template v-if="actions && actions.length > 0">
        <button
          v-for="action in actions"
          :key="action.id"
          type="button"
          class="iris-toolbar__btn"
          :class="{
            'iris-toolbar__btn--primary': action.primary,
            'iris-toolbar__btn--loading': action.loading
          }"
          :disabled="disabled || action.disabled || action.loading"
          @click="action.onClick ? action.onClick() : emit('action', action.id)"
        >
          <component
            :is="action.loading ? Loader2 : action.icon"
            v-if="action.icon || action.loading"
            :size="14"
            class="iris-toolbar__btn-icon"
            :class="{ 'iris-spin': action.loading }"
            aria-hidden="true"
          />
          <span>{{ action.label }}</span>
        </button>
      </template>

      <slot name="actions" />

      <!-- Export button -->
      <button
        v-if="showExport"
        type="button"
        class="iris-toolbar__btn"
        :disabled="disabled || exporting"
        aria-label="Export data"
        @click="emit('export')"
      >
        <component
          :is="exporting ? Loader2 : Download"
          :size="14"
          class="iris-toolbar__btn-icon"
          :class="{ 'iris-spin': exporting }"
          aria-hidden="true"
        />
        <span>Export</span>
      </button>

      <!-- Refresh trigger -->
      <button
        v-if="showRefresh"
        type="button"
        class="iris-toolbar__btn"
        :class="{ 'iris-toolbar__btn--loading': refreshing }"
        :disabled="disabled || refreshing"
        aria-label="Refresh telemetry data"
        @click="emit('refresh')"
      >
        <component
          :is="refreshing ? Loader2 : RefreshCw"
          :size="14"
          class="iris-toolbar__btn-icon"
          :class="{ 'iris-spin': refreshing }"
          aria-hidden="true"
        />
        <span>Refresh</span>
      </button>
    </div>
  </div>
</template>

<style scoped>
.iris-toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 1rem;
  min-height: var(--iris-toolbar-height);
  padding: 0.375rem 0.75rem;
  background-color: var(--iris-bg-surface);
  border: 1px solid var(--iris-border-default);
  border-radius: var(--iris-border-radius);
  box-shadow: var(--iris-shadow-subtle);
  font-family: var(--iris-font-sans);
  flex-wrap: wrap;
}

.iris-toolbar__left,
.iris-toolbar__right {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  flex-wrap: wrap;
}

.iris-toolbar__search {
  position: relative;
  display: flex;
  align-items: center;
}

.iris-toolbar__search-icon {
  position: absolute;
  left: 8px;
  color: var(--iris-text-muted);
  pointer-events: none;
}

.iris-toolbar__search-input {
  padding: 4px 8px 4px 28px;
  font-size: 0.8125rem;
  font-family: var(--iris-font-sans);
  color: var(--iris-text-primary);
  background-color: var(--iris-bg-surface);
  border: 1px solid var(--iris-border-default);
  border-radius: var(--iris-border-radius);
  outline: none;
  min-width: 180px;
}

.iris-toolbar__search-input:focus {
  border-color: var(--iris-border-focus);
}

.iris-toolbar__btn-group {
  display: inline-flex;
  border: 1px solid var(--iris-border-default);
  border-radius: var(--iris-border-radius);
  overflow: hidden;
  background-color: var(--iris-bg-subtle);
}

.iris-toolbar__group-btn {
  padding: 3px 10px;
  font-size: 0.75rem;
  font-weight: 500;
  color: var(--iris-text-secondary);
  background: transparent;
  border: none;
  border-right: 1px solid var(--iris-border-default);
  cursor: pointer;
  transition: all 0.15s ease;
}

.iris-toolbar__group-btn:last-child {
  border-right: none;
}

.iris-toolbar__group-btn:hover:not(:disabled) {
  background-color: var(--iris-bg-hover);
  color: var(--iris-text-primary);
}

.iris-toolbar__group-btn--active {
  background-color: var(--iris-bg-surface);
  color: #0284c7;
  font-weight: 600;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
}

.iris-toolbar__btn {
  display: inline-flex;
  align-items: center;
  gap: 0.375rem;
  padding: 5px 12px;
  font-size: 0.8125rem;
  font-weight: 500;
  color: var(--iris-text-primary);
  background-color: var(--iris-bg-surface);
  border: 1px solid var(--iris-border-default);
  border-radius: var(--iris-border-radius);
  cursor: pointer;
  transition: background-color 0.15s ease, border-color 0.15s ease;
  user-select: none;
}

.iris-toolbar__btn:hover:not(:disabled) {
  background-color: var(--iris-bg-hover);
  border-color: var(--iris-border-subtle);
}

.iris-toolbar__btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.iris-toolbar__btn--primary {
  background-color: #0284c7;
  color: #ffffff;
  border-color: #0284c7;
}

.iris-toolbar__btn--primary:hover:not(:disabled) {
  background-color: #0369a1;
  border-color: #0369a1;
}

.iris-toolbar__btn-icon {
  flex-shrink: 0;
}

.iris-spin {
  animation: iris-spin 1s linear infinite;
}

@keyframes iris-spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}
</style>
