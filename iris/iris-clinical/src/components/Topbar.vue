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
  Menu, PanelLeftClose, PanelLeftOpen, 
  Activity, ExternalLink, Sparkles, Shield
} from 'lucide-vue-next';

const props = defineProps<{
  isSidebarCollapsed: boolean;
}>();

const emit = defineEmits<{
  (e: 'toggleMobile'): void;
  (e: 'toggleCollapse'): void;
}>();

const route = useRoute();

const routeMetaMap: Record<string, { title: string; category: string }> = {
  '/': { title: 'Dashboard', category: 'Overview' },
  '/persons': { title: 'Persons & Patients', category: 'Directory & Registry' },
  '/practitioners': { title: 'Practitioners & Roles', category: 'Directory & Registry' },
  '/organizations': { title: 'Organizations', category: 'Directory & Registry' },
  '/locations': { title: 'Location Hierarchy', category: 'Directory & Registry' },
  '/services': { title: 'Healthcare Services', category: 'Directory & Registry' },
  '/groups': { title: 'Patient & Care Groups', category: 'Directory & Registry' },
  '/tasks': { title: 'FHIR Tasks', category: 'Clinical Workflow' },
  '/communication': { title: 'Communications', category: 'Clinical Workflow' },
  '/documents': { title: 'Document References', category: 'Clinical Workflow' },
  '/provenance': { title: 'Lineage & Provenance', category: 'Lineage & Governance' },
  '/audit': { title: 'Audit Events', category: 'Lineage & Governance' },
  '/consent': { title: 'Consent Directives', category: 'Lineage & Governance' },
};

const currentPageInfo = computed(() => {
  const path = route.path;
  if (routeMetaMap[path]) {
    return routeMetaMap[path];
  }
  for (const [key, value] of Object.entries(routeMetaMap)) {
    if (key !== '/' && path.startsWith(key)) {
      return value;
    }
  }
  return { title: 'FHIR Resources', category: 'Repository' };
});
</script>

<template>
  <header class="topbar">
    <div class="topbar-container">
      <!-- Left side: Mobile Toggle, Desktop Collapse Toggle, Page Title / Breadcrumbs -->
      <div class="flex items-center gap-3">
        <!-- Mobile Menu Hamburger -->
        <button 
          class="topbar-btn flex md:hidden" 
          @click="emit('toggleMobile')"
          title="Open menu"
        >
          <Menu :size="18" />
        </button>

        <!-- Desktop Toggle -->
        <button 
          class="topbar-btn hidden md:flex" 
          @click="emit('toggleCollapse')"
          :title="isSidebarCollapsed ? 'Expand sidebar' : 'Collapse sidebar'"
        >
          <PanelLeftOpen v-if="isSidebarCollapsed" :size="18" class="text-slate-400" />
          <PanelLeftClose v-else :size="18" class="text-slate-400" />
        </button>

        <div class="topbar-breadcrumbs">
          <span class="text-xs text-slate-400 font-medium hidden sm:inline">{{ currentPageInfo.category }}</span>
          <span class="text-xs text-slate-600 hidden sm:inline">/</span>
          <h1 class="topbar-title">{{ currentPageInfo.title }}</h1>
        </div>
      </div>

      <!-- Right side: Status indicators, Operations UI link -->
      <div class="flex items-center gap-3">
        <!-- FHIR R5 Connected Pill -->
        <div class="hidden sm:flex items-center gap-2 px-2.5 py-1 rounded-full bg-sky-500/10 border border-sky-500/20 text-sky-400 text-xs font-semibold">
          <span class="w-1.5 h-1.5 rounded-full bg-sky-400 animate-pulse"></span>
          <span>FHIR R5 Connected</span>
        </div>

        <!-- Switch to Operations UI Button -->
        <a 
          href="http://localhost:3001" 
          target="_blank" 
          rel="noopener noreferrer"
          class="topbar-action-btn"
          title="Open Harmonia Operations Console (Port 3001)"
        >
          <Sparkles :size="14" class="text-emerald-400" />
          <span class="hidden md:inline text-xs font-medium text-slate-300">Operations UI</span>
          <ExternalLink :size="12" class="text-slate-400" />
        </a>
      </div>
    </div>
  </header>
</template>

<style scoped>
.topbar {
  position: sticky;
  top: 0;
  z-index: 30;
  height: 64px;
  background: rgba(17, 24, 39, 0.75);
  backdrop-filter: blur(12px);
  border-bottom: 1px solid #1f293d;
  display: flex;
  align-items: center;
}

.topbar-container {
  width: 100%;
  padding: 0 1.25rem;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.topbar-btn {
  background: transparent;
  border: 1px solid #27344d;
  color: #cbd5e1;
  padding: 0.45rem;
  border-radius: 0.45rem;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.15s;
}

.topbar-btn:hover {
  background: #1e293b;
  color: #ffffff;
  border-color: #3b4d6e;
}

.topbar-breadcrumbs {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.topbar-title {
  font-size: 1.05rem;
  font-weight: 700;
  color: #ffffff;
  letter-spacing: -0.015em;
  margin: 0;
}

.topbar-action-btn {
  display: flex;
  align-items: center;
  gap: 0.4rem;
  padding: 0.35rem 0.65rem;
  border-radius: 0.45rem;
  background: rgba(16, 185, 129, 0.08);
  border: 1px solid rgba(16, 185, 129, 0.25);
  text-decoration: none;
  transition: all 0.15s;
}

.topbar-action-btn:hover {
  background: rgba(16, 185, 129, 0.16);
  border-color: rgba(16, 185, 129, 0.45);
}
</style>
