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
import IrisHeader from './IrisHeader.vue'
import IrisEnvironmentBar from './IrisEnvironmentBar.vue'
import IrisPrimaryNavigation from './IrisPrimaryNavigation.vue'
import IrisUserMenu from './IrisUserMenu.vue'
import type { NavPerspective, StatusState, UserProfile } from '../../types'

const props = withDefaults(
  defineProps<{
    application?: string
    title?: string
    environment?: string
    cluster?: string
    namespace?: string
    healthState?: StatusState | string | null
    healthStale?: boolean
    navigationItems?: NavPerspective[]
    activeNavId?: string
    user?: UserProfile
    lastUpdated?: Date | string | null
    refreshing?: boolean
    showEnvironmentBar?: boolean
    showRefresh?: boolean
    footerText?: string
  }>(),
  {
    application: '',
    title: 'HARMONIA',
    environment: '',
    cluster: '',
    namespace: '',
    healthState: undefined,
    healthStale: false,
    navigationItems: () => [],
    activeNavId: '',
    user: undefined,
    lastUpdated: null,
    refreshing: false,
    showEnvironmentBar: true,
    showRefresh: true,
    footerText: ''
  }
)

const emit = defineEmits<{
  (e: 'update:activeNavId', id: string): void
  (e: 'nav-select', item: NavPerspective): void
  (e: 'refresh'): void
}>()
</script>

<template>
  <div class="iris-shell">
    <!-- Top Persistent Application Header -->
    <slot name="header">
      <IrisHeader
        :application-title="title"
        :application-subtitle="application"
        :environment="environment"
        :cluster="cluster"
        :health-state="healthState"
        :health-stale="healthStale"
        :hide-meta-pills="showEnvironmentBar"
      >
        <template #left v-if="$slots['header-left']">
          <slot name="header-left" />
        </template>

        <template #brand v-if="$slots['header-brand']">
          <slot name="header-brand" />
        </template>

        <template #center v-if="$slots['header-center']">
          <slot name="header-center" />
        </template>

        <template #meta v-if="$slots['header-meta']">
          <slot name="header-meta" />
        </template>

        <template #actions v-if="$slots['header-actions']">
          <slot name="header-actions" />
        </template>

        <template #right>
          <slot name="header-right" />
          <slot name="user-menu">
            <IrisUserMenu v-if="user" :user="user" />
          </slot>
        </template>
      </IrisHeader>
    </slot>

    <!-- Environment / Cluster context bar -->
    <slot name="environment-bar">
      <IrisEnvironmentBar
        v-if="showEnvironmentBar && (environment || cluster || namespace || lastUpdated)"
        :environment="environment"
        :cluster="cluster"
        :namespace="namespace"
        :last-updated="lastUpdated"
        :refreshing="refreshing"
        :show-refresh="showRefresh"
        :status="healthState"
        :stale="healthStale"
        @refresh="emit('refresh')"
      >
        <template #left v-if="$slots['env-left']">
          <slot name="env-left" />
        </template>
        <template #right v-if="$slots['env-right']">
          <slot name="env-right" />
        </template>
      </IrisEnvironmentBar>
    </slot>

    <!-- Top Primary Navigation -->
    <slot name="nav">
      <IrisPrimaryNavigation
        v-if="navigationItems && navigationItems.length > 0"
        :items="navigationItems"
        :model-value="activeNavId"
        @update:model-value="emit('update:activeNavId', $event)"
        @select="emit('nav-select', $event)"
      >
        <template #right v-if="$slots['nav-right']">
          <slot name="nav-right" />
        </template>
      </IrisPrimaryNavigation>
    </slot>

    <!-- Main Workspace Container -->
    <div class="iris-shell__body">
      <slot>
        <router-view />
      </slot>
    </div>

    <!-- Application Footer -->
    <footer class="iris-shell__footer">
      <slot name="footer">
        <div v-if="footerText" class="iris-shell__footer-text">
          <span>{{ footerText }}</span>
        </div>
      </slot>
    </footer>
  </div>
</template>

<style scoped>
.iris-shell {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  width: 100%;
  background-color: var(--iris-bg-page);
  color: var(--iris-text-primary);
  font-family: var(--iris-font-sans);
}

.iris-shell__body {
  display: flex;
  flex-direction: column;
  flex: 1;
  width: 100%;
  position: relative;
}

.iris-shell__footer {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0.5rem 1rem;
  background-color: var(--iris-bg-surface);
  border-top: 1px solid var(--iris-border-default);
  font-size: 0.75rem;
  color: var(--iris-text-muted);
}

.iris-shell__footer-text {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}
</style>
