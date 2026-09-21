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
import { ref, computed } from 'vue';
import { 
  Search, 
  ChevronRight, 
  ChevronDown, 
  Layers, 
  Server, 
  Activity, 
  Database, 
  ShieldCheck, 
  MessageSquare, 
  Monitor, 
  Radio, 
  GitMerge, 
  Cpu 
} from 'lucide-vue-next';
import { useOperationsStore } from '../../stores/operationsStore';
import { 
  HARMONIA_ARCHITECTURAL_AREAS, 
  AUTHORITATIVE_SUBSYSTEMS,
  type AuthoritativeSubsystem 
} from '../../models/subsystemHierarchy';
import { IrisStatus } from '@harmonia/iris-befe';

const store = useOperationsStore();
const searchQuery = ref('');

const emit = defineEmits<{
  (e: 'select', subsystemId: string): void;
}>();

const selectedId = computed(() => store.selectedSubsystemId);

const areaIcons: Record<string, any> = {
  'integration-transport': Radio,
  'execution-processing': GitMerge,
  'information-state': Database,
  'security-policy': ShieldCheck,
  'collaboration': MessageSquare,
  'presentation': Monitor
};

const subsystemIcons: Record<string, any> = {
  pylai: Radio,
  petasos: Server,
  energeia: GitMerge,
  ponos: Cpu,
  praxis: Activity,
  ergon: GitMerge,
  pragma: Layers,
  mneme: Database,
  mnemosyne: Database,
  calliope: Layers,
  themis: ShieldCheck,
  agora: MessageSquare,
  iris: Monitor
};

// Track expanded state for each area (all default to true)
const expandedAreas = ref<Record<string, boolean>>({
  'integration-transport': true,
  'execution-processing': true,
  'information-state': true,
  'security-policy': true,
  'collaboration': true,
  'presentation': true
});

const toggleArea = (areaId: string) => {
  expandedAreas.value[areaId] = !expandedAreas.value[areaId];
};

// Find live telemetry state from store
const getSubsystemState = (subsystemId: string) => {
  const sub = store.subsystems.find(s => s.id === subsystemId);
  if (sub) {
    return { state: sub.state, stale: sub.stale, instanceCount: sub.instanceCount };
  }
  // Check child subsystems
  for (const parent of store.subsystems) {
    if (parent.children) {
      const child = parent.children.find(c => c.id === subsystemId);
      if (child) {
        return { state: child.state, stale: child.stale, instanceCount: child.instanceCount };
      }
    }
  }
  return { state: 'UNKNOWN', stale: false, instanceCount: 0 };
};

const handleSelect = (id: string) => {
  store.selectSubsystem(id);
  emit('select', id);
};

// Handle arrow-key navigation within the tree
const handleTreeKeydown = (event: KeyboardEvent, itemId: string, isChild: boolean, parentId?: string) => {
  const treeNav = document.querySelector('[role="tree"]');
  if (!treeNav) return;
  
  const items = Array.from(treeNav.querySelectorAll('[data-item-id]')) as HTMLElement[];
  const currentIndex = items.findIndex(el => el.getAttribute('data-item-id') === itemId);
  
  if (event.key === 'ArrowDown') {
    event.preventDefault();
    if (currentIndex < items.length - 1) {
      items[currentIndex + 1]?.focus();
    }
  } else if (event.key === 'ArrowUp') {
    event.preventDefault();
    if (currentIndex > 0) {
      items[currentIndex - 1]?.focus();
    }
  } else if (event.key === 'ArrowRight' && !isChild && parentId) {
    event.preventDefault();
    if (!expandedAreas.value[parentId]) {
      expandedAreas.value[parentId] = true;
    }
  } else if (event.key === 'ArrowLeft' && !isChild && parentId) {
    event.preventDefault();
    if (expandedAreas.value[parentId]) {
      expandedAreas.value[parentId] = false;
    }
  }
};

// Filtered areas and subsystems
const filteredAreas = computed(() => {
  const q = searchQuery.value.trim().toLowerCase();

  return HARMONIA_ARCHITECTURAL_AREAS.map(area => {
    const matchingSubsystems = area.subsystemIds
      .map(id => AUTHORITATIVE_SUBSYSTEMS[id])
      .filter((sub): sub is AuthoritativeSubsystem => {
        if (!sub) return false;
        if (!q) return true;
        
        const matchesSub = 
          sub.name.toLowerCase().includes(q) ||
          sub.englishTitle.toLowerCase().includes(q) ||
          sub.description.toLowerCase().includes(q);
        
        if (matchesSub) return true;

        if (sub.children && sub.children.length > 0) {
          return sub.children.some(c => 
            c.name.toLowerCase().includes(q) || 
            c.englishTitle.toLowerCase().includes(q)
          );
        }

        return false;
      });

    return {
      ...area,
      subsystems: matchingSubsystems
    };
  }).filter(area => area.subsystems.length > 0);
});
</script>

<template>
  <aside 
    class="subsystem-tree-navigator"
    aria-label="Harmonia Architectural Hierarchy"
  >
    <!-- Header -->
    <div class="subsystem-tree__header">
      <div class="subsystem-tree__header-top">
        <h2 class="subsystem-tree__title">
          <Layers :size="14" class="subsystem-tree__title-icon" />
          <span>Architectural Inventory</span>
        </h2>
        <span class="subsystem-tree__area-badge">
          6 Areas
        </span>
      </div>

      <!-- Search Input -->
      <div class="subsystem-tree__search-wrapper">
        <input
          v-model="searchQuery"
          type="text"
          placeholder="Filter subsystems or roles..."
          class="subsystem-tree__search-input"
          aria-label="Filter subsystems by name or keyword"
        />
        <Search :size="13" class="subsystem-tree__search-icon" />
      </div>
    </div>

    <!-- Tree Body -->
    <nav class="subsystem-tree__body" role="tree">
      <div 
        v-if="filteredAreas.length === 0" 
        class="subsystem-tree__empty"
      >
        No subsystems match "<span class="subsystem-tree__empty-query">{{ searchQuery }}</span>"
      </div>

      <!-- Architectural Areas -->
      <div
        v-for="area in filteredAreas"
        :key="area.id"
        class="subsystem-tree__area-group"
        role="group"
      >
        <!-- Area Header Row -->
        <button
          type="button"
          @click="toggleArea(area.id)"
          class="subsystem-tree__area-btn"
          :aria-expanded="expandedAreas[area.id]"
        >
          <div class="subsystem-tree__area-info">
            <component 
              :is="expandedAreas[area.id] ? ChevronDown : ChevronRight" 
              :size="13" 
              class="subsystem-tree__chevron" 
            />
            <component 
              :is="areaIcons[area.id] || Layers" 
              :size="14" 
              class="subsystem-tree__area-icon" 
            />
            <span class="subsystem-tree__area-name">{{ area.name }}</span>
          </div>

          <span class="subsystem-tree__area-count-badge">
            {{ area.subsystems.length }}
          </span>
        </button>

        <!-- Subsystems within Area -->
        <div 
          v-if="expandedAreas[area.id]"
          class="subsystem-tree__subsystems-container"
          role="group"
        >
          <div
            v-for="subsystem in area.subsystems"
            :key="subsystem.id"
            class="subsystem-tree__subsystem-block"
          >
            <!-- Subsystem Node Row -->
            <div
              :data-item-id="subsystem.id"
              @click="handleSelect(subsystem.id)"
              @keydown.enter="handleSelect(subsystem.id)"
              @keydown.space.prevent="handleSelect(subsystem.id)"
              @keydown="handleTreeKeydown($event, subsystem.id, false, area.id)"
              tabindex="0"
              class="subsystem-tree__node-item"
              :class="{ 'is-selected': selectedId === subsystem.id }"
              role="treeitem"
              :aria-selected="selectedId === subsystem.id"
            >
              <div class="subsystem-tree__node-content">
                <component 
                  :is="subsystemIcons[subsystem.id] || Server" 
                  :size="14" 
                  class="subsystem-tree__node-icon"
                />
                <div class="subsystem-tree__node-text">
                  <div class="subsystem-tree__node-title-row">
                    <span class="subsystem-tree__node-name">{{ subsystem.name }}</span>
                  </div>
                  <div class="subsystem-tree__node-subtitle">
                    {{ subsystem.englishTitle }}
                  </div>
                </div>
              </div>

              <!-- Status Badge -->
              <div class="subsystem-tree__node-status">
                <IrisStatus 
                  :status="getSubsystemState(subsystem.id).state"
                  :stale="getSubsystemState(subsystem.id).stale"
                  size="sm"
                />
              </div>
            </div>

            <!-- Subordinated Child Nodes (e.g. Ponos, Praxis, Ergon, Pragma under Energeia) -->
            <div 
              v-if="subsystem.children && subsystem.children.length > 0"
              class="subsystem-tree__children-container"
            >
              <div
                v-for="child in subsystem.children"
                :key="child.id"
                :data-item-id="child.id"
                @click="handleSelect(child.id)"
                @keydown.enter="handleSelect(child.id)"
                @keydown.space.prevent="handleSelect(child.id)"
                @keydown="handleTreeKeydown($event, child.id, true, area.id)"
                tabindex="0"
                class="subsystem-tree__child-item"
                :class="{ 'is-selected': selectedId === child.id }"
                role="treeitem"
                :aria-selected="selectedId === child.id"
              >
                <div class="subsystem-tree__child-content">
                  <span class="subsystem-tree__child-bullet"></span>
                  <div class="subsystem-tree__child-text">
                    <span class="subsystem-tree__child-name">{{ child.name }}</span>
                    <span class="subsystem-tree__child-subtitle">({{ child.englishTitle }})</span>
                  </div>
                </div>

                <div class="subsystem-tree__child-status">
                  <IrisStatus 
                    :status="getSubsystemState(child.id).state"
                    :stale="getSubsystemState(child.id).stale"
                    size="sm"
                  />
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </nav>
  </aside>
</template>

<style scoped>
.subsystem-tree-navigator {
  display: flex;
  flex-direction: column;
  height: 100%;
  background-color: var(--iris-bg-surface, #ffffff);
  border-right: 1px solid var(--iris-border-default, #e2e8f0);
  user-select: none;
  font-family: var(--iris-font-sans, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif);
}

.subsystem-tree__header {
  padding: 12px 14px;
  border-bottom: 1px solid var(--iris-border-default, #e2e8f0);
  background-color: var(--iris-bg-page, #f8fafc);
}

.subsystem-tree__header-top {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 8px;
}

.subsystem-tree__title {
  font-size: 11px;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  color: var(--iris-text-primary, #0f172a);
  display: flex;
  align-items: center;
  gap: 6px;
  margin: 0;
}

.subsystem-tree__title-icon {
  color: var(--iris-color-primary, #0284c7);
}

.subsystem-tree__area-badge {
  font-size: 10px;
  font-family: var(--iris-font-mono, monospace);
  font-weight: 600;
  padding: 2px 6px;
  border-radius: 4px;
  background-color: var(--iris-border-light, #f1f5f9);
  color: var(--iris-text-secondary, #475569);
  border: 1px solid var(--iris-border-default, #e2e8f0);
}

.subsystem-tree__search-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}

.subsystem-tree__search-input {
  width: 100%;
  background-color: var(--iris-bg-surface, #ffffff);
  border: 1px solid var(--iris-border-default, #cbd5e1);
  border-radius: 6px;
  padding: 6px 10px 6px 28px;
  font-size: 12px;
  color: var(--iris-text-primary, #0f172a);
  outline: none;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.04);
  transition: border-color 0.15s, box-shadow 0.15s;
}

.subsystem-tree__search-input:focus {
  border-color: var(--iris-color-primary, #0284c7);
  box-shadow: 0 0 0 2px var(--iris-focus-ring, rgba(2, 132, 199, 0.2));
}

.subsystem-tree__search-icon {
  position: absolute;
  left: 8px;
  color: var(--iris-text-muted, #94a3b8);
  pointer-events: none;
}

.subsystem-tree__body {
  flex: 1;
  overflow-y: auto;
  padding: 8px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.subsystem-tree__empty {
  text-align: center;
  padding: 24px 8px;
  font-size: 12px;
  color: var(--iris-text-muted, #64748b);
}

.subsystem-tree__empty-query {
  font-weight: 600;
  color: var(--iris-text-primary, #0f172a);
}

.subsystem-tree__area-group {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.subsystem-tree__area-btn {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 6px 8px;
  text-align: left;
  border-radius: 6px;
  font-size: 12px;
  font-weight: 700;
  letter-spacing: -0.01em;
  color: var(--iris-text-primary, #0f172a);
  background: transparent;
  border: none;
  cursor: pointer;
  transition: background-color 0.15s;
}

.subsystem-tree__area-btn:hover {
  background-color: var(--iris-bg-hover, #f1f5f9);
}

.subsystem-tree__area-info {
  display: flex;
  align-items: center;
  gap: 6px;
  min-width: 0;
}

.subsystem-tree__chevron {
  color: var(--iris-text-muted, #94a3b8);
  flex-shrink: 0;
}

.subsystem-tree__area-icon {
  color: var(--iris-text-secondary, #64748b);
  flex-shrink: 0;
}

.subsystem-tree__area-btn:hover .subsystem-tree__area-icon {
  color: var(--iris-color-primary, #0284c7);
}

.subsystem-tree__area-name {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.subsystem-tree__area-count-badge {
  font-size: 10px;
  font-family: var(--iris-font-mono, monospace);
  font-weight: 600;
  padding: 1px 5px;
  border-radius: 4px;
  background-color: var(--iris-border-light, #f1f5f9);
  color: var(--iris-text-secondary, #64748b);
  border: 1px solid var(--iris-border-default, #e2e8f0);
  flex-shrink: 0;
  margin-left: 4px;
}

.subsystem-tree__subsystems-container {
  padding-left: 14px;
  margin-left: 10px;
  border-left: 2px solid var(--iris-border-default, #e2e8f0);
  display: flex;
  flex-direction: column;
  gap: 3px;
  margin-top: 2px;
}

.subsystem-tree__subsystem-block {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.subsystem-tree__node-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 6px 8px;
  border-radius: 6px;
  font-size: 12px;
  cursor: pointer;
  border: 1px solid transparent;
  background-color: transparent;
  color: var(--iris-text-primary, #0f172a);
  transition: background-color 0.15s, border-color 0.15s, box-shadow 0.15s;
  outline: none;
}

.subsystem-tree__node-item:hover {
  background-color: var(--iris-bg-hover, #f8fafc);
  border-color: var(--iris-border-default, #e2e8f0);
}

.subsystem-tree__node-item:focus-visible {
  box-shadow: 0 0 0 2px var(--iris-focus-ring, rgba(2, 132, 199, 0.4));
}

.subsystem-tree__node-item.is-selected {
  background-color: var(--iris-bg-selected, #f0f9ff);
  border-color: #bae6fd;
  color: #0369a1;
  font-weight: 600;
  box-shadow: 0 1px 2px rgba(2, 132, 199, 0.08);
}

.subsystem-tree__node-item.is-selected .subsystem-tree__node-icon {
  color: var(--iris-color-primary, #0284c7);
}

.subsystem-tree__node-content {
  display: flex;
  align-items: center;
  gap: 8px;
  min-width: 0;
}

.subsystem-tree__node-icon {
  color: var(--iris-text-muted, #94a3b8);
  flex-shrink: 0;
}

.subsystem-tree__node-text {
  min-width: 0;
}

.subsystem-tree__node-title-row {
  display: flex;
  align-items: center;
  gap: 4px;
}

.subsystem-tree__node-name {
  font-weight: 700;
  letter-spacing: -0.01em;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.subsystem-tree__node-subtitle {
  font-size: 10px;
  font-weight: 400;
  color: var(--iris-text-secondary, #64748b);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.subsystem-tree__node-status {
  flex-shrink: 0;
  padding-left: 6px;
}

.subsystem-tree__children-container {
  padding-left: 12px;
  margin-left: 10px;
  border-left: 1px solid var(--iris-border-default, #e2e8f0);
  display: flex;
  flex-direction: column;
  gap: 2px;
  padding-top: 2px;
  padding-bottom: 2px;
}

.subsystem-tree__child-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 4px 6px;
  border-radius: 4px;
  font-size: 11px;
  cursor: pointer;
  border: 1px solid transparent;
  color: var(--iris-text-secondary, #475569);
  transition: background-color 0.15s, border-color 0.15s;
  outline: none;
}

.subsystem-tree__child-item:hover {
  background-color: var(--iris-bg-hover, #f8fafc);
  border-color: var(--iris-border-default, #e2e8f0);
  color: var(--iris-text-primary, #0f172a);
}

.subsystem-tree__child-item:focus-visible {
  box-shadow: 0 0 0 2px var(--iris-focus-ring, rgba(2, 132, 199, 0.4));
}

.subsystem-tree__child-item.is-selected {
  background-color: var(--iris-bg-selected, #f0f9ff);
  border-color: #bae6fd;
  color: #0369a1;
  font-weight: 600;
}

.subsystem-tree__child-content {
  display: flex;
  align-items: center;
  gap: 6px;
  min-width: 0;
}

.subsystem-tree__child-bullet {
  width: 5px;
  height: 5px;
  border-radius: 50%;
  background-color: var(--iris-border-default, #cbd5e1);
  flex-shrink: 0;
}

.subsystem-tree__child-item.is-selected .subsystem-tree__child-bullet,
.subsystem-tree__child-item:hover .subsystem-tree__child-bullet {
  background-color: var(--iris-color-primary, #0284c7);
}

.subsystem-tree__child-text {
  min-width: 0;
  display: flex;
  align-items: baseline;
  gap: 4px;
  overflow: hidden;
}

.subsystem-tree__child-name {
  font-weight: 600;
  white-space: nowrap;
}

.subsystem-tree__child-subtitle {
  font-size: 9px;
  color: var(--iris-text-muted, #94a3b8);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.subsystem-tree__child-status {
  flex-shrink: 0;
  padding-left: 4px;
}
</style>
