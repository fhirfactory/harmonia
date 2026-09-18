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
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import { 
  Server, 
  Radio, 
  GitMerge, 
  Activity, 
  AlertTriangle 
} from 'lucide-vue-next';
import { useOperationsStore } from '../../stores/operationsStore';

const route = useRoute();
const store = useOperationsStore();

interface NavPerspective {
  name: string;
  path: string;
  icon: any;
  badge?: () => number | string | null;
  badgeClass?: () => string;
  question: string;
}

const perspectives: NavPerspective[] = [
  {
    name: 'Subsystems',
    path: '/subsystems',
    icon: Server,
    badge: () => store.summary?.degradedSubsystems ? `${store.summary.degradedSubsystems}` : null,
    badgeClass: () => 'bg-amber-500/20 text-amber-300 border border-amber-500/40',
    question: 'Is Harmonia healthy?'
  },
  {
    name: 'Queues',
    path: '/queues',
    icon: Radio,
    question: 'Is work moving?'
  },
  {
    name: 'Workflows',
    path: '/workflows',
    icon: GitMerge,
    question: 'What work is Harmonia performing?'
  },
  {
    name: 'Events',
    path: '/events',
    icon: Activity,
    question: 'What happened to an interaction?'
  },
  {
    name: 'Alerts',
    path: '/alerts',
    icon: AlertTriangle,
    badge: () => {
      const crit = store.criticalAlertsCount;
      const warn = store.warningAlertsCount;
      const total = crit + warn;
      return total > 0 ? String(total) : null;
    },
    badgeClass: () => {
      return store.criticalAlertsCount > 0 
        ? 'bg-rose-500/20 text-rose-300 border border-rose-500/40' 
        : 'bg-amber-500/20 text-amber-300 border border-amber-500/40';
    },
    question: 'What requires attention?'
  }
];

const currentPath = computed(() => route.path);

const isActive = (path: string) => {
  if (path === '/subsystems' && (currentPath.value === '/' || currentPath.value === '/subsystems')) {
    return true;
  }
  return currentPath.value.startsWith(path);
};
</script>

<template>
  <nav class="border-b border-[#1f293d] bg-[#111827]/90 px-4 py-2 flex items-center justify-between text-xs font-medium" aria-label="Operational Perspectives Navigation">
    <div class="flex items-center gap-1.5 sm:gap-2 overflow-x-auto py-0.5 no-scrollbar">
      <router-link
        v-for="item in perspectives"
        :key="item.path"
        :to="item.path"
        class="group relative flex items-center gap-2 px-3 py-1.5 rounded-md transition-all whitespace-nowrap"
        :class="isActive(item.path) 
          ? 'bg-sky-500/15 text-sky-300 border border-sky-500/35 font-semibold shadow-sm' 
          : 'text-slate-300 hover:text-white hover:bg-slate-800/80 border border-transparent'"
        :title="`${item.name} — ${item.question}`"
      >
        <component :is="item.icon" :size="15" :class="isActive(item.path) ? 'text-sky-400' : 'text-slate-400 group-hover:text-slate-200'" />
        <span class="tracking-wide uppercase text-[11px] font-bold">{{ item.name }}</span>
        
        <span 
          v-if="item.badge && item.badge()" 
          class="px-1.5 py-0.2 rounded-full text-[10px] font-mono leading-tight"
          :class="item.badgeClass ? item.badgeClass() : 'bg-slate-700 text-slate-200'"
        >
          {{ item.badge() }}
        </span>
      </router-link>
    </div>

    <!-- Secondary Links (Legacy tools) -->
    <div class="hidden md:flex items-center gap-3 text-slate-400 text-xs">
      <router-link to="/sequences" class="hover:text-slate-200 transition">Task Sequences</router-link>
      <span class="text-slate-600">&bull;</span>
      <router-link to="/caches" class="hover:text-slate-200 transition">Caches</router-link>
    </div>
  </nav>
</template>

<style scoped>
.no-scrollbar::-webkit-scrollbar {
  display: none;
}
.no-scrollbar {
  -ms-overflow-style: none;
  scrollbar-width: none;
}
</style>
