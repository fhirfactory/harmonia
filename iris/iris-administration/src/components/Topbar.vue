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
import { useSecurityStore } from '../stores/securityStore';
import SecurityBadge from './SecurityBadge.vue';
import { 
  Menu, PanelLeftClose, PanelLeftOpen, 
  ExternalLink, Sparkles, Activity
} from 'lucide-vue-next';

const props = defineProps<{
  isSidebarCollapsed: boolean;
}>();

const emit = defineEmits<{
  (e: 'toggleMobile'): void;
  (e: 'toggleCollapse'): void;
}>();

const route = useRoute();
const securityStore = useSecurityStore();

const routeMetaMap: Record<string, { title: string; category: string }> = {
  '/': { title: 'Administration Home', category: 'Overview' },
  '/self-service/my-details': { title: 'My Provider Details', category: 'Provider Self-Service' },
  '/self-service/my-roles': { title: 'My Professional Roles', category: 'Provider Self-Service' },
  '/self-service/my-organizations': { title: 'My Organisations', category: 'Provider Self-Service' },
  '/self-service/my-locations-services': { title: 'Locations & Services', category: 'Provider Self-Service' },
  '/self-service/my-endpoints': { title: 'My Endpoints', category: 'Provider Self-Service' },
  '/self-service/my-requests': { title: 'My Requests & Tracking', category: 'Provider Self-Service' },
  '/self-service/request-change': { title: 'Request a Change', category: 'Provider Self-Service' },
  '/admin/dashboard': { title: 'Administration Dashboard', category: 'Provider Administration' },
  '/admin/work-queue': { title: 'Change Request Work Queue', category: 'Provider Administration' },
  '/admin/search': { title: 'Provider Search', category: 'Provider Administration' },
  '/admin/practitioners': { title: 'Practitioner Management', category: 'Provider Administration' },
  '/admin/roles': { title: 'Practitioner Roles Management', category: 'Provider Administration' },
  '/admin/organizations': { title: 'Organisation Management', category: 'Provider Administration' },
  '/admin/locations': { title: 'Location Hierarchy Management', category: 'Provider Administration' },
  '/admin/services': { title: 'Healthcare Services Management', category: 'Provider Administration' },
  '/admin/endpoints': { title: 'Endpoint Management', category: 'Provider Administration' },
  '/admin/groups': { title: 'Group Management', category: 'Provider Administration' },
  '/admin/data-quality': { title: 'Data Quality & Validation', category: 'Provider Administration' },
  '/services/audit': { title: 'Audit & Lineage Governance', category: 'Administrative Services' },
  '/services/reference-data': { title: 'Reference Data & ValueSets', category: 'Administrative Services' },
  '/system/security': { title: 'Themis Security Context', category: 'System Administration' },
  '/system/preferences': { title: 'Preferences & System Settings', category: 'System Administration' },
  '/access-denied': { title: 'Access Denied', category: 'Security Gateway' }
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
  return { title: 'Harmonia Administration', category: 'Workspace' };
});

const onPersonaChange = (event: Event) => {
  const target = event.target as HTMLSelectElement;
  securityStore.setPersona(target.value);
};
</script>

<template>
  <header class="topbar">
    <div class="topbar-container">
      <!-- Left side: Mobile Toggle, Desktop Collapse Toggle, Page Title / Breadcrumbs -->
      <div class="flex items-center gap-3 min-w-0">
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

        <div class="topbar-breadcrumbs min-w-0">
          <span class="text-xs text-slate-400 font-medium hidden sm:inline whitespace-nowrap">{{ currentPageInfo.category }}</span>
          <span class="text-xs text-slate-600 hidden sm:inline">/</span>
          <h1 class="topbar-title truncate">{{ currentPageInfo.title }}</h1>
        </div>
      </div>

      <!-- Right side: Persona Switcher, Security Badge, Iris Suite Navigation -->
      <div class="flex items-center gap-2.5">
        <!-- Persona Simulator (Allows easy verification of Provider vs Admin roles) -->
        <div class="hidden lg:flex items-center gap-1.5 bg-slate-950/70 border border-slate-800 rounded-lg px-2 py-1">
          <label for="persona-select" class="text-[11px] text-slate-400 font-medium whitespace-nowrap">Role Context:</label>
          <select 
            id="persona-select"
            :value="securityStore.currentPersonaId"
            @change="onPersonaChange"
            class="bg-transparent border-none text-xs text-sky-400 font-semibold focus:outline-none cursor-pointer"
          >
            <option 
              v-for="p in securityStore.personaProfiles" 
              :key="p.id" 
              :value="p.id"
              class="bg-slate-900 text-slate-200"
            >
              {{ p.name }}
            </option>
          </select>
        </div>

        <!-- Security Context Badge Modal Trigger -->
        <SecurityBadge />

        <!-- External Iris Suite Links -->
        <a 
          href="http://localhost:3000" 
          target="_blank" 
          rel="noopener noreferrer"
          class="topbar-action-btn hidden sm:flex"
          title="Open Iris Clinical Explorer (Port 3000)"
        >
          <Activity :size="13" class="text-sky-400" />
          <span class="text-xs font-medium text-slate-300">Clinical UI</span>
          <ExternalLink :size="11" class="text-slate-400" />
        </a>

        <a 
          href="http://localhost:3001" 
          target="_blank" 
          rel="noopener noreferrer"
          class="topbar-action-btn hidden sm:flex"
          title="Open Iris Operations Console (Port 3001)"
        >
          <Sparkles :size="13" class="text-emerald-400" />
          <span class="text-xs font-medium text-slate-300">Operations UI</span>
          <ExternalLink :size="11" class="text-slate-400" />
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
  background: rgba(13, 19, 34, 0.85);
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
  gap: 1rem;
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
  gap: 0.35rem;
  padding: 0.35rem 0.6rem;
  border-radius: 0.45rem;
  background: rgba(255, 255, 255, 0.04);
  border: 1px solid #27344d;
  text-decoration: none;
  transition: all 0.15s;
}

.topbar-action-btn:hover {
  background: rgba(255, 255, 255, 0.08);
  border-color: #38bdf8;
}
</style>
