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
import { ref } from 'vue'
import { ChevronDown, ChevronRight } from 'lucide-vue-next'

const props = withDefaults(
  defineProps<{
    title?: string
    description?: string
    collapsible?: boolean
    defaultCollapsed?: boolean
  }>(),
  {
    title: '',
    description: '',
    collapsible: false,
    defaultCollapsed: false
  }
)

const emit = defineEmits<{
  (e: 'toggle', collapsed: boolean): void
}>()

const isCollapsed = ref(props.defaultCollapsed)

function toggleCollapse() {
  if (props.collapsible) {
    isCollapsed.value = !isCollapsed.value
    emit('toggle', isCollapsed.value)
  }
}
</script>

<template>
  <section class="iris-section">
    <div
      v-if="title || $slots.title || $slots.actions || description"
      class="iris-section__header"
      :class="{ 'iris-section__header--clickable': collapsible }"
      @click="collapsible ? toggleCollapse() : undefined"
    >
      <div class="iris-section__title-group">
        <button
          v-if="collapsible"
          type="button"
          class="iris-section__collapse-btn"
          :aria-expanded="!isCollapsed"
          @click.stop="toggleCollapse"
        >
          <component :is="isCollapsed ? ChevronRight : ChevronDown" :size="16" />
        </button>

        <div>
          <h2 class="iris-section__title">
            <slot name="title">{{ title }}</slot>
          </h2>
          <p v-if="description" class="iris-section__description">
            {{ description }}
          </p>
        </div>
      </div>

      <div v-if="$slots.actions" class="iris-section__actions" @click.stop>
        <slot name="actions" />
      </div>
    </div>

    <div v-if="!isCollapsed" class="iris-section__content">
      <slot />
    </div>
  </section>
</template>

<style scoped>
.iris-section {
  background-color: var(--iris-bg-surface);
  border: 1px solid var(--iris-border-default);
  border-radius: var(--iris-border-radius);
  box-shadow: var(--iris-shadow-subtle);
  overflow: hidden;
}

.iris-section__header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 1rem;
  padding: 0.75rem 1rem;
  background-color: var(--iris-bg-surface);
  border-bottom: 1px solid var(--iris-border-default);
}

.iris-section__header--clickable {
  cursor: pointer;
  user-select: none;
}

.iris-section__header--clickable:hover {
  background-color: var(--iris-bg-subtle);
}

.iris-section__title-group {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.iris-section__collapse-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 2px;
  background: transparent;
  border: none;
  color: var(--iris-text-secondary);
  cursor: pointer;
  border-radius: 2px;
}

.iris-section__collapse-btn:hover {
  color: var(--iris-text-primary);
  background-color: var(--iris-bg-hover);
}

.iris-section__title {
  margin: 0;
  font-size: 0.9375rem;
  font-weight: 600;
  color: var(--iris-text-primary);
  line-height: 1.3;
}

.iris-section__description {
  margin: 0.125rem 0 0 0;
  font-size: 0.8125rem;
  color: var(--iris-text-muted);
}

.iris-section__actions {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.iris-section__content {
  padding: 1rem;
}
</style>
