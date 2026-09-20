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
import { Loader2 } from 'lucide-vue-next'

withDefaults(
  defineProps<{
    message?: string
    size?: 'sm' | 'md' | 'lg'
  }>(),
  {
    message: 'Loading...',
    size: 'md'
  }
)
</script>

<template>
  <div
    class="iris-loading-state"
    :class="`iris-loading-state--${size}`"
    role="status"
    aria-live="polite"
    aria-busy="true"
  >
    <Loader2 class="iris-loading-state__spinner" aria-hidden="true" />
    <span v-if="message || $slots.default" class="iris-loading-state__message">
      <slot>{{ message }}</slot>
    </span>
  </div>
</template>

<style scoped>
.iris-loading-state {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.625rem;
  padding: 1.5rem;
  color: var(--iris-text-secondary);
  font-family: var(--iris-font-sans);
}

.iris-loading-state--sm {
  padding: 0.5rem;
  font-size: 0.75rem;
}

.iris-loading-state--sm .iris-loading-state__spinner {
  width: 14px;
  height: 14px;
}

.iris-loading-state--md {
  padding: 1.5rem;
  font-size: 0.8125rem;
}

.iris-loading-state--md .iris-loading-state__spinner {
  width: 18px;
  height: 18px;
}

.iris-loading-state--lg {
  padding: 2.5rem;
  font-size: 0.9375rem;
}

.iris-loading-state--lg .iris-loading-state__spinner {
  width: 24px;
  height: 24px;
}

.iris-loading-state__spinner {
  animation: iris-spin 1s linear infinite;
  color: var(--iris-border-focus);
}

.iris-loading-state__message {
  font-weight: 500;
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
