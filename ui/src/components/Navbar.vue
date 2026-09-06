<script setup lang="ts">
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import { Activity, Users, UserCheck, Building2, MapPin, Stethoscope, UsersRound, LayoutDashboard } from 'lucide-vue-next';

const route = useRoute();

const navItems = [
  { name: 'Dashboard', path: '/', icon: LayoutDashboard },
  { name: 'Persons', path: '/persons', icon: Users },
  { name: 'Practitioners', path: '/practitioners', icon: UserCheck },
  { name: 'Organizations', path: '/organizations', icon: Building2 },
  { name: 'Locations', path: '/locations', icon: MapPin },
  { name: 'Services', path: '/services', icon: Stethoscope },
  { name: 'Groups', path: '/groups', icon: UsersRound },
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
            <span class="font-extrabold text-white text-base tracking-tight">HIE Platform</span>
            <span class="badge badge-blue">FHIR R5</span>
          </div>
          <span class="text-xs text-slate-400">High-Availability In-Memory Health Grid</span>
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

      <!-- Architecture Tier Health Indicators -->
      <div class="hidden lg:flex items-center gap-2 text-xs">
        <span class="badge badge-green flex items-center gap-1">
          <span class="w-1.5 h-1.5 rounded-full bg-emerald-400 animate-pulse"></span>
          Cluster Active
        </span>
      </div>
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
