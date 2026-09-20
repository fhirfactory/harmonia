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
import { ChevronRight, ChevronDown } from 'lucide-vue-next'
import IrisStatus from './IrisStatus.vue'
import type { HierarchyNode } from '../../types'

defineOptions({
  name: 'IrisHierarchyItem'
})

defineSlots<{
  icon?: (props: { node: HierarchyNode }) => any
  badge?: (props: { node: HierarchyNode }) => any
}>()

const props = withDefaults(
  defineProps<{
    node: HierarchyNode
    selectedId?: string | null
    level?: number
    expandedMap: Record<string, boolean>
    defaultExpandedAll?: boolean
  }>(),
  {
    selectedId: null,
    level: 0,
    defaultExpandedAll: true
  }
)

const emit = defineEmits<{
  (e: 'select', node: HierarchyNode): void
  (e: 'toggle', node: HierarchyNode, expanded: boolean): void
}>()

function isExpanded(node: HierarchyNode): boolean {
  if (props.expandedMap[node.id] !== undefined) {
    return props.expandedMap[node.id]
  }
  return props.defaultExpandedAll
}

function toggleExpand(node: HierarchyNode) {
  const current = isExpanded(node)
  emit('toggle', node, !current)
}

function selectNode(node: HierarchyNode) {
  emit('select', node)
}
</script>

<template>
  <li
    class="iris-hierarchy__item"
    :class="`iris-hierarchy__item--level-${level}`"
    role="treeitem"
    :aria-level="level + 1"
    :aria-expanded="node.children && node.children.length > 0 ? isExpanded(node) : undefined"
    :aria-selected="selectedId === node.id"
  >
    <div
      class="iris-hierarchy__node-row"
      :class="{
        'iris-hierarchy__node-row--selected': selectedId === node.id,
        'iris-hierarchy__node-row--has-children': node.children && node.children.length > 0,
        'iris-hierarchy__node-row--child': level > 0,
        [`iris-hierarchy__node-row--level-${level}`]: true
      }"
      @click="selectNode(node)"
    >
      <button
        v-if="node.children && node.children.length > 0"
        type="button"
        class="iris-hierarchy__toggle-btn"
        :aria-label="isExpanded(node) ? 'Collapse' : 'Expand'"
        @click.stop="toggleExpand(node)"
      >
        <component
          :is="isExpanded(node) ? ChevronDown : ChevronRight"
          :size="14"
        />
      </button>
      <span v-else class="iris-hierarchy__toggle-spacer"></span>

      <slot name="icon" :node="node">
        <component
          :is="node.icon"
          v-if="node.icon"
          :size="level === 0 ? 15 : 14"
          class="iris-hierarchy__node-icon"
        />
      </slot>

      <div class="iris-hierarchy__node-text">
        <span class="iris-hierarchy__node-label">{{ node.label }}</span>
        <span v-if="node.subtitle" class="iris-hierarchy__node-subtitle">
          {{ node.subtitle }}
        </span>
      </div>

      <div class="iris-hierarchy__node-meta">
        <slot name="badge" :node="node">
          <IrisStatus
            v-if="node.status !== undefined"
            :status="node.status"
            :stale="node.stale"
            size="sm"
            :show-icon="false"
          />
          <span
            v-else-if="node.count !== undefined"
            class="iris-hierarchy__count-badge"
          >
            {{ node.count }}
          </span>
        </slot>
      </div>
    </div>

    <!-- Recursive Children -->
    <ul
      v-if="node.children && node.children.length > 0 && isExpanded(node)"
      class="iris-hierarchy__sublist"
      role="group"
    >
      <IrisHierarchyItem
        v-for="child in node.children"
        :key="child.id"
        :node="child"
        :selected-id="selectedId"
        :level="level + 1"
        :expanded-map="expandedMap"
        :default-expanded-all="defaultExpandedAll"
        @select="emit('select', $event)"
        @toggle="(n, exp) => emit('toggle', n, exp)"
      >
        <template #icon="{ node: childNode }">
          <slot name="icon" :node="childNode" />
        </template>
        <template #badge="{ node: childNode }">
          <slot name="badge" :node="childNode" />
        </template>
      </IrisHierarchyItem>
    </ul>
  </li>
</template>

<style scoped>
.iris-hierarchy__item {
  margin: 0;
  padding: 0;
  list-style: none;
}

.iris-hierarchy__sublist {
  list-style: none;
  margin: 0;
  padding: 0 0 0 0.875rem;
  border-left: 1px solid var(--iris-border-default);
  margin-left: 1.125rem;
}

.iris-hierarchy__node-row {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 6px 10px;
  cursor: pointer;
  user-select: none;
  border-radius: var(--iris-border-radius);
  margin: 1px 6px;
  border-left: 3px solid transparent;
  transition: background-color 0.15s ease, border-color 0.15s ease;
}

.iris-hierarchy__node-row:hover {
  background-color: var(--iris-bg-hover);
}

.iris-hierarchy__node-row--selected {
  background-color: var(--iris-bg-selected);
  border-left-color: var(--iris-border-focus);
  color: #0369a1;
  font-weight: 600;
}

.iris-hierarchy__node-row--selected .iris-hierarchy__node-label {
  color: #0369a1;
  font-weight: 600;
}

.iris-hierarchy__toggle-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 18px;
  height: 18px;
  padding: 0;
  background: transparent;
  border: none;
  color: var(--iris-text-secondary);
  cursor: pointer;
  border-radius: 2px;
  flex-shrink: 0;
}

.iris-hierarchy__toggle-btn:hover {
  color: var(--iris-text-primary);
  background-color: var(--iris-bg-hover);
}

.iris-hierarchy__toggle-spacer {
  width: 18px;
  height: 18px;
  flex-shrink: 0;
}

.iris-hierarchy__node-icon {
  flex-shrink: 0;
  color: var(--iris-text-secondary);
}

.iris-hierarchy__node-text {
  display: flex;
  flex-direction: column;
  flex: 1;
  min-width: 0;
  gap: 1px;
}

.iris-hierarchy__node-label {
  font-size: 0.84rem;
  color: var(--iris-text-primary);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  line-height: 1.25;
}

.iris-hierarchy__node-row--level-0 .iris-hierarchy__node-label {
  font-weight: 600;
  font-size: 0.85rem;
}

.iris-hierarchy__node-subtitle {
  font-size: 0.72rem;
  color: var(--iris-text-muted);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  line-height: 1.2;
}

.iris-hierarchy__node-meta {
  flex-shrink: 0;
  display: flex;
  align-items: center;
  margin-left: auto;
}

.iris-hierarchy__count-badge {
  font-size: 0.7rem;
  font-family: var(--iris-font-mono);
  font-weight: 500;
  padding: 1px 7px;
  background-color: var(--iris-bg-subtle);
  border: 1px solid var(--iris-border-default);
  color: var(--iris-text-secondary);
  border-radius: 9999px;
  min-width: 20px;
  text-align: center;
  line-height: 1.3;
}
</style>
