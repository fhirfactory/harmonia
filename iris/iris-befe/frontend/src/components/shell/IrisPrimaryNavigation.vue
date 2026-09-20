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
import { computed, getCurrentInstance } from 'vue'
import type { NavPerspective } from '../../types'

const props = withDefaults(
  defineProps<{
    items: NavPerspective[]
    modelValue?: string
  }>(),
  {
    modelValue: ''
  }
)

const emit = defineEmits<{
  (e: 'update:modelValue', id: string): void
  (e: 'select', item: NavPerspective): void
}>()

const currentRoutePath = computed(() => {
  const instance = getCurrentInstance()
  const route = (instance?.proxy as any)?.$route
  return route?.path || ''
})

function isItemActive(item: NavPerspective, routerIsActive?: boolean, routerIsExactActive?: boolean): boolean {
  if (props.modelValue && props.modelValue === item.id) {
    return true
  }
  if (routerIsActive) {
    return true
  }
  if (item.to) {
    const toPath = typeof item.to === 'string' ? item.to : item.to?.path
    if (toPath && currentRoutePath.value) {
      if (toPath === '/subsystems' && (currentRoutePath.value === '/' || currentRoutePath.value === '/subsystems' || currentRoutePath.value.startsWith('/subsystems/'))) {
        return true
      }
      if (toPath === '/overview' && (currentRoutePath.value === '/overview' || currentRoutePath.value === '/dashboard')) {
        return true
      }
      if (toPath === '/messages' && (currentRoutePath.value.startsWith('/messages') || currentRoutePath.value.startsWith('/queues'))) {
        return true
      }
      if (toPath === '/work' && (currentRoutePath.value.startsWith('/work') || currentRoutePath.value.startsWith('/workflows'))) {
        return true
      }
      if (toPath === '/alerts' && (currentRoutePath.value.startsWith('/alerts') || currentRoutePath.value.startsWith('/health'))) {
        return true
      }
      if (toPath === '/' && currentRoutePath.value === '/') return true
      if (toPath !== '/' && currentRoutePath.value.startsWith(toPath)) return true
    }
  }
  return false
}

function handleSelect(item: NavPerspective) {
  emit('update:modelValue', item.id)
  emit('select', item)
}
</script>

<template>
  <nav class="iris-primary-nav" role="navigation" aria-label="Primary navigation">
    <ul class="iris-primary-nav__list" role="menubar">
      <li
        v-for="item in items"
        :key="item.id"
        class="iris-primary-nav__item"
        role="none"
      >
        <!-- RouterLink variant -->
        <RouterLink
          v-if="item.to"
          :to="item.to"
          v-slot="{ href, navigate, isActive, isExactActive }"
          custom
        >
          <a
            :href="href"
            class="iris-primary-nav__link"
            :class="{ 'iris-primary-nav__link--active': isItemActive(item, isActive, isExactActive) }"
            :aria-current="(isItemActive(item, isActive, isExactActive)) ? 'page' : undefined"
            role="menuitem"
            @click="(e) => { navigate(e); handleSelect(item); }"
          >
            <component
              :is="item.icon"
              v-if="item.icon"
              :size="15"
              class="iris-primary-nav__icon"
              aria-hidden="true"
            />
            <span class="iris-primary-nav__label">{{ item.label }}</span>
            <span v-if="item.subLabel" class="iris-primary-nav__sublabel">({{ item.subLabel }})</span>
            <span
              v-if="item.badge !== undefined && item.badge !== null && item.badge !== ''"
              class="iris-primary-nav__badge"
              :class="`iris-primary-nav__badge--${item.badgeSeverity || 'info'}`"
            >
              {{ item.badge }}
            </span>
          </a>
        </RouterLink>

        <!-- Direct button/anchor variant -->
        <a
          v-else-if="item.href"
          :href="item.href"
          class="iris-primary-nav__link"
          :class="{ 'iris-primary-nav__link--active': isItemActive(item) }"
          role="menuitem"
          @click="handleSelect(item)"
        >
          <component
            :is="item.icon"
            v-if="item.icon"
            :size="15"
            class="iris-primary-nav__icon"
            aria-hidden="true"
          />
          <span class="iris-primary-nav__label">{{ item.label }}</span>
          <span v-if="item.subLabel" class="iris-primary-nav__sublabel">({{ item.subLabel }})</span>
          <span
            v-if="item.badge !== undefined && item.badge !== null && item.badge !== ''"
            class="iris-primary-nav__badge"
            :class="`iris-primary-nav__badge--${item.badgeSeverity || 'info'}`"
          >
            {{ item.badge }}
          </span>
        </a>

        <!-- Interactive button variant -->
        <button
          v-else
          type="button"
          class="iris-primary-nav__link iris-primary-nav__button"
          :class="{ 'iris-primary-nav__link--active': isItemActive(item) }"
          role="menuitem"
          @click="handleSelect(item)"
        >
          <component
            :is="item.icon"
            v-if="item.icon"
            :size="15"
            class="iris-primary-nav__icon"
            aria-hidden="true"
          />
          <span class="iris-primary-nav__label">{{ item.label }}</span>
          <span v-if="item.subLabel" class="iris-primary-nav__sublabel">({{ item.subLabel }})</span>
          <span
            v-if="item.badge !== undefined && item.badge !== null && item.badge !== ''"
            class="iris-primary-nav__badge"
            :class="`iris-primary-nav__badge--${item.badgeSeverity || 'info'}`"
          >
            {{ item.badge }}
          </span>
        </button>
      </li>
    </ul>

    <div v-if="$slots.right" class="iris-primary-nav__right">
      <slot name="right" />
    </div>
  </nav>
</template>

<style scoped>
.iris-primary-nav {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: var(--iris-nav-height);
  background-color: var(--iris-bg-surface);
  border-bottom: 1px solid var(--iris-border-default);
  padding: 0 1rem;
  font-family: var(--iris-font-sans);
}

.iris-primary-nav__list {
  display: flex;
  align-items: center;
  gap: 0.25rem;
  list-style: none;
  margin: 0;
  padding: 0;
  height: 100%;
}

.iris-primary-nav__item {
  height: 100%;
  display: flex;
  align-items: center;
}

.iris-primary-nav__link {
  display: inline-flex;
  align-items: center;
  gap: 0.4rem;
  height: calc(var(--iris-nav-height) - 4px);
  padding: 0 0.75rem;
  font-size: 0.84rem;
  font-weight: 500;
  color: var(--iris-text-secondary);
  text-decoration: none;
  border-radius: var(--iris-border-radius);
  border-bottom: 2px solid transparent;
  transition: all 0.15s ease;
  user-select: none;
}

.iris-primary-nav__button {
  background: transparent;
  border: none;
  cursor: pointer;
  font-family: inherit;
}

.iris-primary-nav__link:hover {
  color: var(--iris-text-primary);
  background-color: var(--iris-bg-hover);
}

.iris-primary-nav__link--active {
  color: #0284c7;
  font-weight: 600;
  background-color: var(--iris-bg-selected);
  border-bottom-color: #0284c7;
}

.iris-primary-nav__icon {
  flex-shrink: 0;
}

.iris-primary-nav__label {
  letter-spacing: 0.01em;
}

.iris-primary-nav__sublabel {
  font-size: 0.6875rem;
  font-weight: 500;
  color: var(--iris-text-muted);
  text-transform: uppercase;
  letter-spacing: 0.02em;
}

.iris-primary-nav__link--active .iris-primary-nav__sublabel {
  color: #0369a1;
}

.iris-primary-nav__badge {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  font-size: 0.6875rem;
  font-weight: 600;
  min-width: 18px;
  height: 18px;
  padding: 0 5px;
  border-radius: 9999px;
  line-height: 1;
}

.iris-primary-nav__badge--info {
  background-color: #e0f2fe;
  color: #0369a1;
}

.iris-primary-nav__badge--warn {
  background-color: #fef3c7;
  color: #b45309;
}

.iris-primary-nav__badge--danger {
  background-color: #fee2e2;
  color: #b91c1c;
}

.iris-primary-nav__badge--success {
  background-color: #dcfce7;
  color: #15803d;
}

.iris-primary-nav__right {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}
</style>
