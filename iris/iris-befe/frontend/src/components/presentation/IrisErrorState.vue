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
import { AlertCircle, RotateCcw } from 'lucide-vue-next'

withDefaults(
  defineProps<{
    title?: string
    message?: string
    detail?: string
    retryable?: boolean
    retryLabel?: string
  }>(),
  {
    title: 'An error occurred',
    message: '',
    detail: '',
    retryable: false,
    retryLabel: 'Retry'
  }
)

defineEmits<{
  (e: 'retry'): void
}>()
</script>

<template>
  <div class="iris-error-state" role="alert" aria-live="assertive">
    <div class="iris-error-state__icon-box">
      <slot name="icon">
        <AlertCircle :size="24" class="iris-error-state__icon" aria-hidden="true" />
      </slot>
    </div>

    <div class="iris-error-state__body">
      <h3 class="iris-error-state__title">
        <slot name="title">{{ title }}</slot>
      </h3>
      <p v-if="message || $slots.message" class="iris-error-state__message">
        <slot name="message">{{ message }}</slot>
      </p>
      <p v-if="detail" class="iris-error-state__detail">{{ detail }}</p>
      <slot />
    </div>

    <div v-if="retryable || $slots.actions" class="iris-error-state__actions">
      <slot name="actions">
        <button
          v-if="retryable"
          type="button"
          class="iris-error-state__retry-btn"
          @click="$emit('retry')"
        >
          <RotateCcw :size="13" aria-hidden="true" />
          <span>{{ retryLabel }}</span>
        </button>
      </slot>
    </div>
  </div>
</template>

<style scoped>
.iris-error-state {
  display: flex;
  align-items: flex-start;
  gap: 0.875rem;
  padding: 1rem 1.25rem;
  background-color: var(--iris-status-unavailable-bg);
  border: 1px solid var(--iris-status-unavailable-border);
  border-radius: var(--iris-border-radius);
  color: var(--iris-status-unavailable-text);
  font-family: var(--iris-font-sans);
}

.iris-error-state__icon-box {
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  padding-top: 1px;
}

.iris-error-state__icon {
  color: var(--iris-status-unavailable-text);
}

.iris-error-state__body {
  flex: 1;
}

.iris-error-state__title {
  margin: 0;
  font-size: 0.875rem;
  font-weight: 600;
  line-height: 1.3;
}

.iris-error-state__message {
  margin: 0.25rem 0 0 0;
  font-size: 0.8125rem;
  line-height: 1.4;
  opacity: 0.95;
}

.iris-error-state__detail {
  margin: 0.25rem 0 0 0;
  font-size: 0.75rem;
  line-height: 1.4;
  font-family: var(--iris-font-mono);
  opacity: 0.85;
}

.iris-error-state__actions {
  flex-shrink: 0;
  display: flex;
  align-items: center;
}

.iris-error-state__retry-btn {
  display: inline-flex;
  align-items: center;
  gap: 0.375rem;
  padding: 4px 10px;
  background-color: var(--iris-bg-surface);
  color: var(--iris-status-unavailable-text);
  border: 1px solid var(--iris-status-unavailable-border);
  border-radius: var(--iris-border-radius);
  font-size: 0.75rem;
  font-weight: 600;
  cursor: pointer;
  transition: background-color 0.15s ease;
}

.iris-error-state__retry-btn:hover {
  background-color: #fee2e2;
}
</style>
