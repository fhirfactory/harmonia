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
import IrisStatus from './IrisStatus.vue'
import type { StatusState } from '../../types'

withDefaults(
  defineProps<{
    name: string
    description?: string
    version?: string
    status?: StatusState | string | null
    stale?: boolean
  }>(),
  {
    description: '',
    version: '',
    status: undefined,
    stale: false
  }
)
</script>

<template>
  <div class="iris-subsystem-identity">
    <div class="iris-subsystem-identity__main">
      <div v-if="$slots.icon" class="iris-subsystem-identity__icon-box">
        <slot name="icon" />
      </div>

      <div class="iris-subsystem-identity__titles">
        <div class="iris-subsystem-identity__headline">
          <h1 class="iris-subsystem-identity__name">{{ name }}</h1>
          <IrisStatus
            v-if="status !== undefined"
            :status="status"
            :stale="stale"
            size="sm"
          />
          <span v-if="version" class="iris-subsystem-identity__version">
            v{{ version }}
          </span>
          <slot name="badges" />
        </div>

        <p v-if="description" class="iris-subsystem-identity__description">
          {{ description }}
        </p>

        <slot name="metadata" />
      </div>
    </div>

    <div v-if="$slots.actions" class="iris-subsystem-identity__actions">
      <slot name="actions" />
    </div>
  </div>
</template>

<style scoped>
.iris-subsystem-identity {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 1.5rem;
  padding: 1rem 1.25rem;
  background-color: var(--iris-bg-surface);
  border: 1px solid var(--iris-border-default);
  border-radius: var(--iris-border-radius);
  box-shadow: var(--iris-shadow-subtle);
}

.iris-subsystem-identity__main {
  display: flex;
  align-items: flex-start;
  gap: 1rem;
}

.iris-subsystem-identity__icon-box {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 44px;
  height: 44px;
  border-radius: var(--iris-border-radius);
  background-color: var(--iris-bg-subtle);
  border: 1px solid var(--iris-border-subtle);
  color: var(--iris-text-primary);
  flex-shrink: 0;
}

.iris-subsystem-identity__titles {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.iris-subsystem-identity__headline {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 0.75rem;
}

.iris-subsystem-identity__name {
  margin: 0;
  font-size: 1.375rem;
  font-weight: 700;
  color: var(--iris-text-primary);
  line-height: 1.25;
  letter-spacing: -0.01em;
}

.iris-subsystem-identity__version {
  font-size: 0.75rem;
  font-family: var(--iris-font-mono);
  font-weight: 500;
  padding: 2px 6px;
  border-radius: var(--iris-border-radius);
  background-color: var(--iris-bg-subtle);
  color: var(--iris-text-secondary);
  border: 1px solid var(--iris-border-default);
}

.iris-subsystem-identity__description {
  margin: 0;
  font-size: 0.875rem;
  color: var(--iris-text-secondary);
  line-height: 1.4;
}

.iris-subsystem-identity__actions {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  flex-shrink: 0;
}
</style>
