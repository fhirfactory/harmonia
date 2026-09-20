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
import type { BreadcrumbItem } from '../../types'

const props = withDefaults(
  defineProps<{
    items: (string | BreadcrumbItem)[]
    separator?: string
  }>(),
  {
    separator: '/'
  }
)

const normalizedItems = computed<BreadcrumbItem[]>(() => {
  return props.items.map((it) => {
    if (typeof it === 'string') {
      return { label: it }
    }
    return it
  })
})
</script>

<template>
  <nav class="iris-breadcrumbs" aria-label="Breadcrumb">
    <ol class="iris-breadcrumbs__list">
      <li
        v-for="(item, idx) in normalizedItems"
        :key="idx"
        class="iris-breadcrumbs__item"
        :aria-current="idx === normalizedItems.length - 1 ? 'page' : undefined"
      >
        <span v-if="idx > 0" class="iris-breadcrumbs__separator" aria-hidden="true">
          <slot name="separator">{{ separator }}</slot>
        </span>

        <component
          :is="item.icon"
          v-if="item.icon"
          :size="14"
          class="iris-breadcrumbs__icon"
          aria-hidden="true"
        />

        <RouterLink
          v-if="item.to && idx < normalizedItems.length - 1"
          :to="item.to"
          class="iris-breadcrumbs__link"
        >
          {{ item.label }}
        </RouterLink>

        <span
          v-else
          class="iris-breadcrumbs__text"
          :class="{ 'iris-breadcrumbs__text--current': idx === normalizedItems.length - 1 }"
        >
          {{ item.label }}
        </span>
      </li>
    </ol>
  </nav>
</template>

<style scoped>
.iris-breadcrumbs {
  display: flex;
  align-items: center;
  font-family: var(--iris-font-sans);
  font-size: 0.8125rem;
  color: var(--iris-text-secondary);
  line-height: 1;
}

.iris-breadcrumbs__list {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  list-style: none;
  margin: 0;
  padding: 0;
  gap: 0.375rem;
}

.iris-breadcrumbs__item {
  display: inline-flex;
  align-items: center;
  gap: 0.375rem;
}

.iris-breadcrumbs__separator {
  color: var(--iris-border-subtle);
  user-select: none;
  font-size: 0.75rem;
}

.iris-breadcrumbs__icon {
  color: var(--iris-text-muted);
  flex-shrink: 0;
}

.iris-breadcrumbs__link {
  color: var(--iris-text-secondary);
  text-decoration: none;
  transition: color 0.15s ease;
}

.iris-breadcrumbs__link:hover {
  color: #0284c7;
  text-decoration: underline;
}

.iris-breadcrumbs__text {
  color: var(--iris-text-secondary);
}

.iris-breadcrumbs__text--current {
  color: var(--iris-text-primary);
  font-weight: 600;
}
</style>
