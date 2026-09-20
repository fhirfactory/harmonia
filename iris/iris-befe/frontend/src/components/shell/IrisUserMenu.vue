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
import { User, ChevronDown } from 'lucide-vue-next'
import type { UserProfile } from '../../types'

withDefaults(
  defineProps<{
    user?: UserProfile
    showRole?: boolean
  }>(),
  {
    user: () => ({
      username: 'operator',
      name: 'System Operator',
      role: 'Operations'
    }),
    showRole: true
  }
)

const isOpen = ref(false)

function toggleMenu() {
  isOpen.value = !isOpen.value
}
</script>

<template>
  <div class="iris-user-menu">
    <button
      type="button"
      class="iris-user-menu__trigger"
      :aria-expanded="isOpen"
      aria-haspopup="menu"
      @click="toggleMenu"
    >
      <slot name="avatar">
        <div class="iris-user-menu__avatar">
          <img
            v-if="user?.avatarUrl"
            :src="user.avatarUrl"
            :alt="user?.name || 'User'"
            class="iris-user-menu__img"
          />
          <User v-else :size="14" aria-hidden="true" />
        </div>
      </slot>

      <div class="iris-user-menu__info">
        <span class="iris-user-menu__name">{{ user?.name || user?.username || 'User' }}</span>
        <span v-if="showRole && user?.role" class="iris-user-menu__role">
          {{ user.role }}
        </span>
      </div>

      <ChevronDown :size="12" class="iris-user-menu__chevron" aria-hidden="true" />
    </button>

    <div
      v-if="isOpen && $slots.menu"
      class="iris-user-menu__dropdown"
      role="menu"
      @click="isOpen = false"
    >
      <slot name="menu" />
    </div>
  </div>
</template>

<style scoped>
.iris-user-menu {
  position: relative;
  display: inline-flex;
  align-items: center;
  font-family: var(--iris-font-sans);
}

.iris-user-menu__trigger {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  padding: 3px 8px;
  background: transparent;
  border: 1px solid transparent;
  border-radius: var(--iris-border-radius);
  color: var(--iris-text-primary);
  cursor: pointer;
  transition: background-color 0.15s ease, border-color 0.15s ease;
}

.iris-user-menu__trigger:hover {
  background-color: var(--iris-bg-hover);
  border-color: var(--iris-border-default);
}

.iris-user-menu__avatar {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 26px;
  height: 26px;
  border-radius: 9999px;
  background-color: var(--iris-bg-subtle);
  border: 1px solid var(--iris-border-default);
  color: var(--iris-text-secondary);
  overflow: hidden;
  flex-shrink: 0;
}

.iris-user-menu__img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.iris-user-menu__info {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  text-align: left;
  line-height: 1.15;
}

.iris-user-menu__name {
  font-size: 0.8125rem;
  font-weight: 600;
  color: var(--iris-text-primary);
}

.iris-user-menu__role {
  font-size: 0.7rem;
  color: var(--iris-text-muted);
}

.iris-user-menu__chevron {
  color: var(--iris-text-muted);
  flex-shrink: 0;
}

.iris-user-menu__dropdown {
  position: absolute;
  top: 100%;
  right: 0;
  margin-top: 4px;
  min-width: 160px;
  background-color: var(--iris-bg-surface);
  border: 1px solid var(--iris-border-default);
  border-radius: var(--iris-border-radius);
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -2px rgba(0, 0, 0, 0.1);
  padding: 4px 0;
  z-index: 50;
}
</style>
