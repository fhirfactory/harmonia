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
import IrisPageHeader from './IrisPageHeader.vue'

withDefaults(
  defineProps<{
    title?: string
    subtitle?: string
    fluid?: boolean
    padded?: boolean
  }>(),
  {
    title: '',
    subtitle: '',
    fluid: false,
    padded: true
  }
)
</script>

<template>
  <main
    class="iris-page"
    :class="{
      'iris-page--fluid': fluid,
      'iris-page--padded': padded
    }"
  >
    <!-- Breadcrumbs row -->
    <div v-if="$slots.breadcrumbs" class="iris-page__breadcrumbs">
      <slot name="breadcrumbs" />
    </div>

    <!-- Header / Title -->
    <slot name="header">
      <IrisPageHeader v-if="title" :title="title" :subtitle="subtitle">
        <template #badges v-if="$slots['header-badges']">
          <slot name="header-badges" />
        </template>
        <template #actions v-if="$slots['header-actions']">
          <slot name="header-actions" />
        </template>
      </IrisPageHeader>
    </slot>

    <!-- Toolbar slot -->
    <div v-if="$slots.toolbar" class="iris-page__toolbar">
      <slot name="toolbar" />
    </div>

    <!-- Main Page Body -->
    <div class="iris-page__content">
      <slot />
    </div>
  </main>
</template>

<style scoped>
.iris-page {
  display: flex;
  flex-direction: column;
  flex: 1;
  width: 100%;
  min-height: 100%;
  box-sizing: border-box;
  font-family: var(--iris-font-sans);
  background-color: var(--iris-bg-page);
  gap: 0.75rem;
}

.iris-page--padded {
  padding: 1rem 1.5rem;
}

.iris-page:not(.iris-page--fluid) {
  max-width: 1600px;
  margin: 0 auto;
}

.iris-page__breadcrumbs {
  display: flex;
  align-items: center;
}

.iris-page__toolbar {
  display: flex;
  flex-direction: column;
}

.iris-page__content {
  display: flex;
  flex-direction: column;
  flex: 1;
  gap: 1rem;
}
</style>
