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
  Network, 
  Radio, 
  Cpu, 
  HardDrive, 
  Database, 
  FileCode, 
  Shield, 
  MessageCircle, 
  Monitor,
  Workflow,
  Cog,
  Server
} from 'lucide-vue-next';
import { useOperationsStore } from '../../stores/operationsStore';
import type { OperationalSubsystem } from '../../models/operations';
import StatusBadge from '../common/StatusBadge.vue';

const store = useOperationsStore();

const emit = defineEmits<{
  (e: 'select', subsystemId: string): void;
}>();

const searchQuery = ref('');
const expandedMap = ref<Record<string, boolean>>({
  energeia: true
});

const getSubsystemIcon = (id: string) => {
  switch (id.toLowerCase()) {
    case 'pylai': return Network;
    case 'petasos': return Radio;
    case 'energeia': return Cpu;
    case 'ponos': return Cog;
    case 'praxis': return Workflow;
    case 'mneme': return HardDrive;
    case 'mnemosyne': return Database;
    case 'calliope': return FileCode;
    case 'themis': return Shield;
    case 'agora': return MessageCircle;
    case 'iris': return Monitor;
    default: return Server;
  }
};

const toggleExpand = (id: string, event: Event) => {
  event.stopPropagation();
  expandedMap.value[id] = !expandedMap.value[id];
};

const subsystems = computed(() => store.subsystems);

const filteredSubsystems = computed(() => {
  const q = searchQuery.value.trim().toLowerCase();
  if (!q) return subsystems.value;

  return subsystems.value.filter(sub => {
    const matchName = sub.name.toLowerCase().includes(q) || sub.description.toLowerCase().includes(q);
    const matchChild = sub.children?.some(c => 
      c.name.toLowerCase().includes(q) || c.description.toLowerCase().includes(q)
    );
    return matchName || matchChild;
  });
});

const selectSubsystem = (id: string) => {
  store.selectSubsystem(id);
  emit('select', id);
};
</script>

<template>
  <aside class="w-full md:w-64 lg:w-72 flex-shrink-0 bg-[#0f172a]/70 border-r border-[#1f293d] flex flex-col h-full min-h-[600px]">
    <!-- Search Bar -->
    <div class="p-3 border-b border-[#1f293d]">
      <div class="relative">
        <Search :size="14" class="absolute left-2.5 top-2.5 text-slate-500" />
        <input
          v-model="searchQuery"
          type="text"
          placeholder="Filter subsystems..."
          class="w-full pl-8 pr-3 py-1.5 text-xs bg-[#151c2c] border border-slate-700/80 rounded-md text-slate-200 placeholder-slate-500 focus:outline-none focus:border-sky-500 transition"
          aria-label="Filter subsystems inventory"
        />
      </div>
    </div>

    <!-- Inventory Header -->
    <div class="px-3 py-2 flex items-center justify-between text-[11px] font-semibold text-slate-400 uppercase tracking-wider bg-slate-900/40 border-b border-[#1f293d]/60">
      <span>Operational Inventory</span>
      <span class="font-mono text-slate-500">{{ subsystems.length }} Services</span>
    </div>

    <!-- Subsystems List -->
    <div class="flex-1 overflow-y-auto p-2 space-y-1">
      <div v-if="filteredSubsystems.length === 0" class="p-4 text-center text-xs text-slate-500 italic">
        No subsystems match filter.
      </div>

      <div 
        v-for="sub in filteredSubsystems"
        :key="sub.id"
        class="space-y-1"
      >
        <!-- Parent Subsystem Row -->
        <button
          @click="selectSubsystem(sub.id)"
          class="w-full flex items-center justify-between px-2.5 py-2 rounded-lg text-left transition text-xs group cursor-pointer"
          :class="store.selectedSubsystemId === sub.id 
            ? 'bg-sky-500/15 border border-sky-500/30 text-sky-200 shadow-sm' 
            : 'hover:bg-slate-800/60 text-slate-300 border border-transparent'"
          :aria-current="store.selectedSubsystemId === sub.id ? 'true' : undefined"
        >
          <div class="flex items-center gap-2.5 min-w-0">
            <!-- Icon with health indicator glow -->
            <div 
              class="p-1.5 rounded-md flex items-center justify-center shrink-0"
              :class="store.selectedSubsystemId === sub.id ? 'bg-sky-500/20 text-sky-300' : 'bg-slate-800 text-slate-400 group-hover:text-slate-200'"
            >
              <component :is="getSubsystemIcon(sub.id)" :size="15" />
            </div>

            <div class="min-w-0">
              <div class="flex items-center gap-1.5">
                <span class="font-bold truncate text-[13px] text-white">{{ sub.name }}</span>
                <span v-if="sub.children && sub.children.length > 0" class="text-[10px] text-slate-500 font-mono">
                  ({{ sub.children.length }})
                </span>
              </div>
              <p class="text-[10px] text-slate-400 truncate max-w-[140px]">{{ sub.description }}</p>
            </div>
          </div>

          <div class="flex items-center gap-1.5 shrink-0 ml-2">
            <!-- Health Badge -->
            <StatusBadge :status="sub.state" size="sm" :stale="sub.stale" />

            <!-- Expand/Collapse toggle for subsystems with children (Energeia) -->
            <button
              v-if="sub.children && sub.children.length > 0"
              @click="(e) => toggleExpand(sub.id, e)"
              class="p-1 text-slate-400 hover:text-white rounded transition"
              :title="expandedMap[sub.id] ? 'Collapse child nodes' : 'Expand child nodes'"
              :aria-label="expandedMap[sub.id] ? 'Collapse child nodes' : 'Expand child nodes'"
            >
              <ChevronDown v-if="expandedMap[sub.id]" :size="14" />
              <ChevronRight v-else :size="14" />
            </button>
          </div>
        </button>

        <!-- Children Rows (e.g. Ponos and Praxis under Energeia) -->
        <div 
          v-if="sub.children && sub.children.length > 0 && expandedMap[sub.id]"
          class="pl-4 pr-1 py-0.5 space-y-1 border-l-2 border-slate-800 ml-4"
        >
          <button
            v-for="child in sub.children"
            :key="child.id"
            @click="selectSubsystem(child.id)"
            class="w-full flex items-center justify-between px-2 py-1.5 rounded-md text-left transition text-xs group cursor-pointer"
            :class="store.selectedSubsystemId === child.id 
              ? 'bg-sky-500/15 border border-sky-500/30 text-sky-200' 
              : 'hover:bg-slate-800/40 text-slate-400 border border-transparent'"
          >
            <div class="flex items-center gap-2 min-w-0">
              <component :is="getSubsystemIcon(child.id)" :size="13" class="text-slate-400 shrink-0" />
              <div class="min-w-0">
                <span class="font-semibold text-slate-200 text-xs truncate block">{{ child.name }}</span>
                <span class="text-[10px] text-slate-500 truncate block max-w-[120px]">{{ child.description }}</span>
              </div>
            </div>

            <StatusBadge :status="child.state" size="sm" :stale="child.stale" />
          </button>
        </div>
      </div>
    </div>
  </aside>
</template>
