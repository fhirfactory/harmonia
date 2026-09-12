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
  Activity, LayoutDashboard, GitMerge, Database, 
  Radio, HardDrive, Layers, Server
} from 'lucide-vue-next';

const route = useRoute();

const navItems = [
  { name: 'Dashboard', path: '/', icon: LayoutDashboard },
  { name: 'Task Sequences', path: '/sequences', icon: GitMerge },
  { name: 'Operational Data', path: '/operations-data', icon: Database },
  { name: 'Messaging Queues', path: '/queues', icon: Radio },
  { name: 'Cache Cluster', path: '/caches', icon: HardDrive }
];

const currentPath = computed(() => route.path);
</script>

<template>
  <header class="border-b border-[#27344d] bg-[#111827]/80 backdrop-blur sticky top-0 z-40">
    <div class="container flex items-center justify-between" style="padding-top: 0.85rem; padding-bottom: 0.85rem;">
      <!-- Logo & Title -->
      <div class="flex items-center gap-3">
        <div class="p-2 rounded-lg bg-emerald-500/10 border border-emerald-500/30 text-emerald-400 flex items-center justify-center">
          <Activity :size="22" class="text-emerald-400" />
        </div>
        <div>
          <div class="flex items-center gap-2">
            <span class="font-extrabold text-white text-base tracking-tight">HIE Operations Center</span>
            <span class="badge badge-green">5-Tier HA</span>
          </div>
          <span class="text-xs text-slate-400">Cluster Architecture, Queues &amp; Task Sequences</span>
        </div>
      </div>

      <!-- Navigation Links -->
      <nav class="flex items-center gap-2">
        <router-link
          v-for="item in navItems"
          :key="item.path"
          :to="item.path"
          class="flex items-center gap-2 px-3 py-1.5 rounded-md text-xs font-semibold transition"
          :class="currentPath === item.path ? 'bg-emerald-500/20 text-emerald-400 border border-emerald-500/30' : 'text-slate-300 hover:text-white hover:bg-slate-800'"
        >
          <component :is="item.icon" :size="15" />
          <span>{{ item.name }}</span>
        </router-link>
      </nav>

      <!-- Architecture Status Indicator -->
      <div class="hidden lg:flex items-center gap-2 text-xs">
        <span class="badge badge-green flex items-center gap-1">
          <span class="w-1.5 h-1.5 rounded-full bg-emerald-400 animate-pulse"></span>
          Cluster Grid Active
        </span>
      </div>
    </div>
  </header>
</template>

<style scoped>
.router-link-active {
  background-color: rgba(52, 211, 153, 0.15);
  color: #34d399;
  border: 1px solid rgba(52, 211, 153, 0.3);
}
</style>
