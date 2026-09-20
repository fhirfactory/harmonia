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
import IrisStatus from '../presentation/IrisStatus.vue'
import type { StatusState } from '../../types'

withDefaults(
  defineProps<{
    applicationTitle?: string
    applicationSubtitle?: string
    environment?: string
    cluster?: string
    healthState?: StatusState | string | null
    healthStale?: boolean
    hideMetaPills?: boolean
  }>(),
  {
    applicationTitle: 'HARMONIA',
    applicationSubtitle: '',
    environment: '',
    cluster: '',
    healthState: undefined,
    healthStale: false,
    hideMetaPills: false
  }
)
</script>

<template>
  <header class="iris-header">
    <div class="iris-header__left">
      <slot name="brand">
        <div class="iris-header__brand">
          <span class="iris-header__logo-badge">IRIS</span>
          <span class="iris-header__title">{{ applicationTitle }}</span>
          <span v-if="applicationSubtitle" class="iris-header__subtitle">
            {{ applicationSubtitle.startsWith('/') ? applicationSubtitle : `/ ${applicationSubtitle}` }}
          </span>
        </div>
      </slot>

      <!-- Global Platform Health Badge -->
      <div v-if="healthState !== undefined && healthState !== null" class="iris-header__health-badge">
        <IrisStatus
          :status="healthState"
          :stale="healthStale"
          size="sm"
          :show-pulse="true"
        />
      </div>

      <slot name="left" />
    </div>

    <div class="iris-header__center">
      <slot name="center" />
    </div>

    <div class="iris-header__right">
      <slot name="meta">
        <!-- Environment Indicator -->
        <div v-if="environment && !hideMetaPills" class="iris-header__pill" title="Environment">
          <span class="iris-header__pill-key">Env:</span>
          <span class="iris-header__pill-val">{{ environment }}</span>
        </div>

        <!-- Cluster Indicator -->
        <div v-if="cluster && !hideMetaPills" class="iris-header__pill" title="Cluster">
          <span class="iris-header__pill-key">Cluster:</span>
          <span class="iris-header__pill-val">{{ cluster }}</span>
        </div>
      </slot>

      <slot name="actions" />
      <slot name="right" />
    </div>
  </header>
</template>

<style scoped>
.iris-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: var(--iris-header-height);
  padding: 0 1rem;
  background-color: var(--iris-bg-surface);
  border-bottom: 1px solid var(--iris-border-default);
  font-family: var(--iris-font-sans);
  user-select: none;
}

.iris-header__left {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.iris-header__brand {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.iris-header__logo-badge {
  font-size: 0.6875rem;
  font-weight: 800;
  letter-spacing: 0.08em;
  background: linear-gradient(135deg, #0284c7 0%, #0369a1 100%);
  color: #ffffff;
  padding: 2px 6px;
  border-radius: var(--iris-border-radius);
}

.iris-header__title {
  font-size: 0.875rem;
  font-weight: 800;
  letter-spacing: 0.04em;
  color: var(--iris-text-primary);
}

.iris-header__subtitle {
  font-size: 0.8125rem;
  font-weight: 600;
  letter-spacing: 0.02em;
  color: var(--iris-text-secondary);
}

.iris-header__health-badge {
  display: flex;
  align-items: center;
  border-left: 1px solid var(--iris-border-default);
  padding-left: 0.75rem;
}

.iris-header__center {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.iris-header__right {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.iris-header__pill {
  display: inline-flex;
  align-items: center;
  gap: 0.35rem;
  padding: 2px 8px;
  background-color: var(--iris-bg-subtle);
  border: 1px solid var(--iris-border-default);
  border-radius: var(--iris-border-radius);
  font-size: 0.75rem;
  font-family: var(--iris-font-mono);
  color: var(--iris-text-secondary);
}

.iris-header__pill-key {
  font-weight: 600;
  color: var(--iris-text-muted);
}

.iris-header__pill-val {
  font-weight: 500;
  color: var(--iris-text-primary);
}
</style>
