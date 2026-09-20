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
import { Inbox } from 'lucide-vue-next'

withDefaults(
  defineProps<{
    title?: string
    description?: string
  }>(),
  {
    title: 'No data available',
    description: ''
  }
)
</script>

<template>
  <div class="iris-empty-state" role="region" aria-label="Empty state">
    <div class="iris-empty-state__icon-box">
      <slot name="icon">
        <Inbox :size="28" class="iris-empty-state__icon" aria-hidden="true" />
      </slot>
    </div>

    <h3 class="iris-empty-state__title">
      <slot name="title">{{ title }}</slot>
    </h3>

    <p v-if="description || $slots.description" class="iris-empty-state__description">
      <slot name="description">{{ description }}</slot>
    </p>

    <div v-if="$slots.actions" class="iris-empty-state__actions">
      <slot name="actions" />
    </div>

    <slot />
  </div>
</template>

<style scoped>
.iris-empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
  padding: 2.5rem 1.5rem;
  background-color: var(--iris-bg-surface);
  border-radius: var(--iris-border-radius);
}

.iris-empty-state__icon-box {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 48px;
  height: 48px;
  border-radius: 9999px;
  background-color: var(--iris-bg-subtle);
  border: 1px solid var(--iris-border-subtle);
  color: var(--iris-text-muted);
  margin-bottom: 0.75rem;
}

.iris-empty-state__title {
  margin: 0;
  font-size: 0.9375rem;
  font-weight: 600;
  color: var(--iris-text-primary);
}

.iris-empty-state__description {
  margin: 0.35rem 0 0 0;
  font-size: 0.8125rem;
  color: var(--iris-text-secondary);
  max-width: 400px;
}

.iris-empty-state__actions {
  margin-top: 1rem;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}
</style>
