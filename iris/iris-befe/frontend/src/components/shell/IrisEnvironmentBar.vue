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
import { RefreshCw, Server, Clock } from 'lucide-vue-next'
import IrisStatus from '../presentation/IrisStatus.vue'
import type { StatusState } from '../../types'

const props = withDefaults(
  defineProps<{
    environment?: string
    cluster?: string
    namespace?: string
    lastUpdated?: Date | string | null
    refreshing?: boolean
    showRefresh?: boolean
    status?: StatusState | string | null
    stale?: boolean
  }>(),
  {
    environment: '',
    cluster: '',
    namespace: '',
    lastUpdated: null,
    refreshing: false,
    showRefresh: true,
    status: undefined,
    stale: false
  }
)

const emit = defineEmits<{
  (e: 'refresh'): void
}>()

const formattedLastUpdated = computed(() => {
  if (!props.lastUpdated) return ''
  if (typeof props.lastUpdated === 'string') return props.lastUpdated
  if (props.lastUpdated instanceof Date) {
    return props.lastUpdated.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', second: '2-digit' })
  }
  return String(props.lastUpdated)
})
</script>

<template>
  <div class="iris-env-bar" role="region" aria-label="Environment and Cluster Status Bar">
    <div class="iris-env-bar__left">
      <slot name="left">
        <!-- Environment Indicator -->
        <div v-if="environment" class="iris-env-bar__item iris-env-bar__env">
          <Server :size="13" class="iris-env-bar__icon" aria-hidden="true" />
          <span class="iris-env-bar__label">Env:</span>
          <span class="iris-env-bar__value">{{ environment }}</span>
        </div>

        <!-- Cluster Indicator -->
        <div v-if="cluster" class="iris-env-bar__item iris-env-bar__cluster">
          <span class="iris-env-bar__label">Cluster:</span>
          <span class="iris-env-bar__value">{{ cluster }}</span>
        </div>

        <!-- Namespace Indicator -->
        <div v-if="namespace" class="iris-env-bar__item iris-env-bar__namespace">
          <span class="iris-env-bar__label">NS:</span>
          <span class="iris-env-bar__value">{{ namespace }}</span>
        </div>

        <!-- Status Indicator -->
        <div v-if="status !== undefined" class="iris-env-bar__item iris-env-bar__status">
          <IrisStatus :status="status" :stale="stale" size="sm" />
        </div>
      </slot>
    </div>

    <div class="iris-env-bar__right">
      <slot name="right">
        <!-- Updated Timestamp -->
        <div v-if="formattedLastUpdated" class="iris-env-bar__item iris-env-bar__timestamp">
          <Clock :size="12" class="iris-env-bar__icon" aria-hidden="true" />
          <span class="iris-env-bar__label">Updated:</span>
          <span class="iris-env-bar__value">{{ formattedLastUpdated }}</span>
        </div>

        <!-- Refresh Trigger -->
        <button
          v-if="showRefresh"
          type="button"
          class="iris-env-bar__refresh-btn"
          :class="{ 'iris-env-bar__refresh-btn--loading': refreshing }"
          :disabled="refreshing"
          aria-label="Refresh environment telemetry"
          @click="emit('refresh')"
        >
          <RefreshCw :size="12" class="iris-env-bar__refresh-icon" aria-hidden="true" />
          <span>Refresh</span>
        </button>
      </slot>
    </div>
  </div>
</template>

<style scoped>
.iris-env-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 1rem;
  padding: 0.25rem 1rem;
  background-color: var(--iris-bg-subtle);
  border-bottom: 1px solid var(--iris-border-default);
  font-family: var(--iris-font-sans);
  font-size: 0.75rem;
  color: var(--iris-text-secondary);
  user-select: none;
  min-height: 28px;
}

.iris-env-bar__left,
.iris-env-bar__right {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 0.75rem;
}

.iris-env-bar__item {
  display: inline-flex;
  align-items: center;
  gap: 0.35rem;
  font-family: var(--iris-font-mono);
  font-size: 0.71875rem;
}

.iris-env-bar__icon {
  color: var(--iris-text-muted);
  flex-shrink: 0;
}

.iris-env-bar__label {
  font-weight: 600;
  color: var(--iris-text-muted);
  text-transform: uppercase;
  letter-spacing: 0.02em;
}

.iris-env-bar__value {
  font-weight: 500;
  color: var(--iris-text-primary);
}

.iris-env-bar__refresh-btn {
  display: inline-flex;
  align-items: center;
  gap: 0.25rem;
  padding: 2px 8px;
  background-color: var(--iris-bg-surface);
  border: 1px solid var(--iris-border-default);
  border-radius: var(--iris-border-radius);
  font-family: var(--iris-font-sans);
  font-size: 0.71875rem;
  font-weight: 500;
  color: var(--iris-text-secondary);
  cursor: pointer;
  transition: background-color 0.15s ease, border-color 0.15s ease, color 0.15s ease;
}

.iris-env-bar__refresh-btn:hover:not(:disabled) {
  background-color: var(--iris-bg-hover);
  color: var(--iris-text-primary);
  border-color: var(--iris-border-subtle);
}

.iris-env-bar__refresh-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.iris-env-bar__refresh-btn--loading .iris-env-bar__refresh-icon {
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
