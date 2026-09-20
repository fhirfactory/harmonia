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
withDefaults(
  defineProps<{
    title?: string
    subtitle?: string
  }>(),
  {
    title: '',
    subtitle: ''
  }
)
</script>

<template>
  <header class="iris-page-header">
    <div class="iris-page-header__left">
      <div class="iris-page-header__headline">
        <h1 class="iris-page-header__title">
          <slot name="title">{{ title }}</slot>
        </h1>
        <div v-if="$slots.badges" class="iris-page-header__badges">
          <slot name="badges" />
        </div>
      </div>
      <p v-if="subtitle || $slots.subtitle" class="iris-page-header__subtitle">
        <slot name="subtitle">{{ subtitle }}</slot>
      </p>
    </div>

    <div v-if="$slots.actions" class="iris-page-header__actions">
      <slot name="actions" />
    </div>
  </header>
</template>

<style scoped>
.iris-page-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 1.5rem;
  padding: 0.5rem 0;
  font-family: var(--iris-font-sans);
}

.iris-page-header__left {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.iris-page-header__headline {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 0.75rem;
}

.iris-page-header__title {
  margin: 0;
  font-size: 1.25rem;
  font-weight: 700;
  color: var(--iris-text-primary);
  line-height: 1.25;
  letter-spacing: -0.01em;
}

.iris-page-header__badges {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.iris-page-header__subtitle {
  margin: 0;
  font-size: 0.8125rem;
  color: var(--iris-text-secondary);
  line-height: 1.4;
}

.iris-page-header__actions {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  flex-shrink: 0;
}
</style>
