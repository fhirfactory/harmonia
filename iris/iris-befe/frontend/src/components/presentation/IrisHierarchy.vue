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
import { ref, computed } from 'vue'
import { Search } from 'lucide-vue-next'
import IrisHierarchyItem from './IrisHierarchyItem.vue'
import type { HierarchyNode } from '../../types'

const props = withDefaults(
  defineProps<{
    nodes: HierarchyNode[]
    selectedId?: string | null
    searchable?: boolean
    searchPlaceholder?: string
    emptyMessage?: string
    defaultExpandedAll?: boolean
  }>(),
  {
    selectedId: null,
    searchable: true,
    searchPlaceholder: 'Search...',
    emptyMessage: 'No items match your filter.',
    defaultExpandedAll: true
  }
)

const emit = defineEmits<{
  (e: 'select', node: HierarchyNode): void
  (e: 'toggle', node: HierarchyNode, expanded: boolean): void
}>()

const searchQuery = ref('')
const expandedMap = ref<Record<string, boolean>>({})

function handleToggle(node: HierarchyNode, expanded: boolean) {
  expandedMap.value[node.id] = expanded
  emit('toggle', node, expanded)
}

function selectNode(node: HierarchyNode) {
  emit('select', node)
}

function filterNode(node: HierarchyNode, query: string): boolean {
  const matches =
    node.label.toLowerCase().includes(query) ||
    (node.subtitle && node.subtitle.toLowerCase().includes(query)) ||
    (node.status && String(node.status).toLowerCase().includes(query))

  if (matches) return true
  if (node.children && node.children.length > 0) {
    return node.children.some((child) => filterNode(child, query))
  }
  return false
}

function getFilteredNodes(nodes: HierarchyNode[], query: string): HierarchyNode[] {
  if (!query) return nodes
  return nodes
    .filter((n) => filterNode(n, query))
    .map((n) => {
      if (!n.children || n.children.length === 0) return n
      return {
        ...n,
        children: getFilteredNodes(n.children, query)
      }
    })
}

const visibleNodes = computed(() => {
  const q = searchQuery.value.trim().toLowerCase()
  return getFilteredNodes(props.nodes, q)
})
</script>

<template>
  <div class="iris-hierarchy">
    <div v-if="searchable" class="iris-hierarchy__search-box">
      <div class="iris-hierarchy__search-wrapper">
        <Search :size="14" class="iris-hierarchy__search-icon" aria-hidden="true" />
        <input
          v-model="searchQuery"
          type="search"
          :placeholder="searchPlaceholder"
          class="iris-hierarchy__search-input"
          aria-label="Filter tree items"
        />
      </div>
      <slot name="search-actions" />
    </div>

    <div class="iris-hierarchy__tree-body" role="tree">
      <div v-if="visibleNodes.length === 0" class="iris-hierarchy__empty">
        {{ emptyMessage }}
      </div>

      <ul v-else class="iris-hierarchy__list">
        <IrisHierarchyItem
          v-for="node in visibleNodes"
          :key="node.id"
          :node="node"
          :selected-id="selectedId"
          :level="0"
          :expanded-map="expandedMap"
          :default-expanded-all="defaultExpandedAll"
          @select="selectNode"
          @toggle="handleToggle"
        >
          <template #icon="slotProps">
            <slot name="icon" v-bind="slotProps" />
          </template>
          <template #badge="slotProps">
            <slot name="badge" v-bind="slotProps" />
          </template>
        </IrisHierarchyItem>
      </ul>
    </div>
  </div>
</template>

<style scoped>
.iris-hierarchy {
  display: flex;
  flex-direction: column;
  background-color: var(--iris-bg-surface);
  border: 1px solid var(--iris-border-default);
  border-radius: var(--iris-border-radius);
  box-shadow: var(--iris-shadow-subtle);
  overflow: hidden;
  height: 100%;
}

.iris-hierarchy__search-box {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 0.75rem;
  border-bottom: 1px solid var(--iris-border-default);
  background-color: var(--iris-bg-subtle);
}

.iris-hierarchy__search-wrapper {
  position: relative;
  display: flex;
  align-items: center;
  flex: 1;
}

.iris-hierarchy__search-icon {
  position: absolute;
  left: 8px;
  color: var(--iris-text-muted);
  pointer-events: none;
}

.iris-hierarchy__search-input {
  width: 100%;
  padding: 4px 8px 4px 28px;
  font-size: 0.8125rem;
  font-family: var(--iris-font-sans);
  color: var(--iris-text-primary);
  background-color: var(--iris-bg-surface);
  border: 1px solid var(--iris-border-default);
  border-radius: var(--iris-border-radius);
  outline: none;
}

.iris-hierarchy__search-input:focus {
  border-color: var(--iris-border-focus);
}

.iris-hierarchy__tree-body {
  flex: 1;
  overflow-y: auto;
  padding: 0.5rem 0;
}

.iris-hierarchy__empty {
  padding: 1.5rem;
  text-align: center;
  font-size: 0.8125rem;
  color: var(--iris-text-muted);
}

.iris-hierarchy__list {
  list-style: none;
  margin: 0;
  padding: 0;
}
</style>
