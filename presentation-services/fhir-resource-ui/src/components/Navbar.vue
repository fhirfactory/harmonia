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
import { Activity, Users, UserCheck, Building2, MapPin, Stethoscope, UsersRound, LayoutDashboard, GitBranch, ShieldAlert, FileCheck, CheckSquare, MessageSquare, FileText } from 'lucide-vue-next';

const route = useRoute();

const navItems = [
  { name: 'Dashboard', path: '/', icon: LayoutDashboard },
  { name: 'Persons', path: '/persons', icon: Users },
  { name: 'Practitioners', path: '/practitioners', icon: UserCheck },
  { name: 'Organizations', path: '/organizations', icon: Building2 },
  { name: 'Locations', path: '/locations', icon: MapPin },
  { name: 'Services', path: '/services', icon: Stethoscope },
  { name: 'Groups', path: '/groups', icon: UsersRound },
  { name: 'Provenance', path: '/provenance', icon: GitBranch },
  { name: 'Audit', path: '/audit', icon: ShieldAlert },
  { name: 'Consent', path: '/consent', icon: FileCheck },
  { name: 'Tasks', path: '/tasks', icon: CheckSquare },
  { name: 'Communication', path: '/communication', icon: MessageSquare },
  { name: 'Documents', path: '/documents', icon: FileText },
];

const currentPath = computed(() => route.path);
</script>

<template>
  <header class="border-b border-[#27344d] bg-[#111827]/80 backdrop-blur sticky top-0 z-40">
    <div class="container flex items-center justify-between" style="padding-top: 0.85rem; padding-bottom: 0.85rem;">
      <!-- Logo & Title -->
      <div class="flex items-center gap-3">
        <div class="p-2 rounded-lg bg-sky-500/10 border border-sky-500/30 text-sky-400 flex items-center justify-center">
          <Activity :size="22" class="text-sky-400" />
        </div>
        <div>
          <div class="flex items-center gap-2">
            <span class="font-extrabold text-white text-base tracking-tight">FHIR Resource Explorer</span>
            <span class="badge badge-blue">FHIR R5</span>
          </div>
          <span class="text-xs text-slate-400">Clinical & Administrative Resource Repository</span>
        </div>
      </div>

      <!-- Navigation Links -->
      <nav class="flex items-center gap-1 overflow-x-auto">
        <router-link
          v-for="item in navItems"
          :key="item.path"
          :to="item.path"
          class="flex items-center gap-2 px-3 py-1.5 rounded-md text-xs font-semibold transition"
          :class="currentPath === item.path ? 'bg-sky-500/20 text-sky-400 border border-sky-500/30' : 'text-slate-300 hover:text-white hover:bg-slate-800'"
        >
          <component :is="item.icon" :size="15" />
          <span>{{ item.name }}</span>
        </router-link>
      </nav>
    </div>
  </header>
</template>

<style scoped>
.router-link-active {
  background-color: rgba(56, 189, 248, 0.15);
  color: #38bdf8;
  border: 1px solid rgba(56, 189, 248, 0.3);
}
</style>
