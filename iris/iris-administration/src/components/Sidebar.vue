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
import { 
  User, Briefcase, Building2, MapPin, Activity, 
  Send, GitPullRequest, ClipboardList, Search, Users, 
  FolderTree, Shield, Settings, Server, ChevronRight,
  Database, Award, Layers, Sparkles, X, SlidersHorizontal, CheckSquare
} from 'lucide-vue-next';

const props = defineProps<{
  collapsed: boolean;
  mobileOpen: boolean;
}>();

const emit = defineEmits<{
  (e: 'toggleCollapse'): void;
  (e: 'closeMobile'): void;
}>();

const route = useRoute();
const securityStore = useSecurityStore();

const isSelfServiceVisible = computed(() => securityStore.canSelfService);
const isProviderAdminVisible = computed(() => securityStore.canAdminister);
const isSystemAdminVisible = computed(() => securityStore.hasAuthority('provider.admin'));

const isActive = (path: string) => {
  if (path === '/' && route.path === '/') return true;
  if (path !== '/' && route.path.startsWith(path)) return true;
  return false;
};
</script>

<template>
  <div>
    <!-- Mobile Backdrop -->
    <div 
      v-if="mobileOpen" 
      class="fixed inset-0 bg-black/60 z-40 md:hidden backdrop-blur-sm"
      @click="emit('closeMobile')"
    ></div>

    <!-- Sidebar Container -->
    <aside 
      class="sidebar-root"
      :class="[
        collapsed ? 'sidebar--collapsed' : 'sidebar--expanded',
        mobileOpen ? 'sidebar--mobile-open' : 'sidebar--mobile-closed'
      ]"
    >
      <!-- App Brand / Header -->
      <div class="sidebar-header">
        <div class="flex items-center gap-3 overflow-hidden">
          <div class="brand-icon">
            <Shield :size="20" class="text-sky-400" />
          </div>
          <div v-if="!collapsed" class="flex flex-col min-w-0">
            <span class="text-sm font-bold tracking-tight text-white flex items-center gap-1.5 truncate">
              Iris Admin
              <span class="text-[10px] font-semibold px-1.5 py-0.5 rounded bg-sky-500/20 text-sky-400 border border-sky-500/30 uppercase tracking-widest">v1.0</span>
            </span>
            <span class="text-[11px] text-slate-400 truncate">Harmonia Administration</span>
          </div>
        </div>
        <button 
          class="md:hidden text-slate-400 hover:text-white p-1"
          @click="emit('closeMobile')"
        >
          <X :size="18" />
        </button>
      </div>

      <!-- Navigation Links Groups -->
      <div class="sidebar-nav custom-scrollbar">
        <!-- Tier 1: Self Service Workspace -->
        <div v-if="isSelfServiceVisible" class="nav-group">
          <div v-if="!collapsed" class="nav-group-title">
            <span>Provider Self-Service</span>
          </div>
          <router-link 
            to="/self-service/my-details" 
            class="nav-link"
            :class="{ 'nav-link--active': isActive('/self-service/my-details') }"
            title="My Provider Details"
            @click="emit('closeMobile')"
          >
            <User :size="17" class="nav-icon" />
            <span v-if="!collapsed" class="nav-label">My Details</span>
          </router-link>

          <router-link 
            to="/self-service/my-roles" 
            class="nav-link"
            :class="{ 'nav-link--active': isActive('/self-service/my-roles') }"
            title="My Professional Roles"
            @click="emit('closeMobile')"
          >
            <Briefcase :size="17" class="nav-icon" />
            <span v-if="!collapsed" class="nav-label">My Roles</span>
          </router-link>

          <router-link 
            to="/self-service/my-organizations" 
            class="nav-link"
            :class="{ 'nav-link--active': isActive('/self-service/my-organizations') }"
            title="My Affiliated Organisations"
            @click="emit('closeMobile')"
          >
            <Building2 :size="17" class="nav-icon" />
            <span v-if="!collapsed" class="nav-label">My Organisations</span>
          </router-link>

          <router-link 
            to="/self-service/my-locations-services" 
            class="nav-link"
            :class="{ 'nav-link--active': isActive('/self-service/my-locations-services') }"
            title="My Locations &amp; Services"
            @click="emit('closeMobile')"
          >
            <MapPin :size="17" class="nav-icon" />
            <span v-if="!collapsed" class="nav-label">Locations &amp; Services</span>
          </router-link>

          <router-link 
            to="/self-service/my-endpoints" 
            class="nav-link"
            :class="{ 'nav-link--active': isActive('/self-service/my-endpoints') }"
            title="My Endpoints"
            @click="emit('closeMobile')"
          >
            <Server :size="17" class="nav-icon" />
            <span v-if="!collapsed" class="nav-label">My Endpoints</span>
          </router-link>

          <router-link 
            to="/self-service/my-requests" 
            class="nav-link"
            :class="{ 'nav-link--active': isActive('/self-service/my-requests') }"
            title="My Change Requests"
            @click="emit('closeMobile')"
          >
            <GitPullRequest :size="17" class="nav-icon" />
            <span v-if="!collapsed" class="nav-label">My Requests</span>
          </router-link>

          <router-link 
            to="/self-service/request-change" 
            class="nav-link highlight-link"
            :class="{ 'nav-link--active': isActive('/self-service/request-change') }"
            title="Submit Governed Change Request"
            @click="emit('closeMobile')"
          >
            <Send :size="17" class="nav-icon text-sky-400" />
            <span v-if="!collapsed" class="nav-label font-semibold text-sky-300">Request a Change</span>
          </router-link>
        </div>

        <!-- Tier 2: Provider Administration -->
        <div v-if="isProviderAdminVisible" class="nav-group">
          <div v-if="!collapsed" class="nav-group-title">
            <span>Provider Administration</span>
          </div>

          <router-link 
            to="/admin/dashboard" 
            class="nav-link"
            :class="{ 'nav-link--active': isActive('/admin/dashboard') }"
            title="Administration Dashboard"
            @click="emit('closeMobile')"
          >
            <Layers :size="17" class="nav-icon" />
            <span v-if="!collapsed" class="nav-label">Dashboard</span>
          </router-link>

          <router-link 
            to="/admin/work-queue" 
            class="nav-link"
            :class="{ 'nav-link--active': isActive('/admin/work-queue') }"
            title="Change Request Work Queue"
            @click="emit('closeMobile')"
          >
            <ClipboardList :size="17" class="nav-icon text-amber-400" />
            <span v-if="!collapsed" class="nav-label flex items-center justify-between flex-1">
              <span>Work Queue</span>
              <span class="text-[10px] px-1.5 py-0.2 rounded bg-amber-500/20 text-amber-300 font-bold">Queue</span>
            </span>
          </router-link>

          <router-link 
            to="/admin/search" 
            class="nav-link"
            :class="{ 'nav-link--active': isActive('/admin/search') }"
            title="Provider Multi-Param Search"
            @click="emit('closeMobile')"
          >
            <Search :size="17" class="nav-icon" />
            <span v-if="!collapsed" class="nav-label">Provider Search</span>
          </router-link>

          <router-link 
            to="/admin/practitioners" 
            class="nav-link"
            :class="{ 'nav-link--active': isActive('/admin/practitioners') }"
            title="Practitioner Registry Management"
            @click="emit('closeMobile')"
          >
            <User :size="17" class="nav-icon" />
            <span v-if="!collapsed" class="nav-label">Practitioners</span>
          </router-link>

          <router-link 
            to="/admin/roles" 
            class="nav-link"
            :class="{ 'nav-link--active': isActive('/admin/roles') }"
            title="PractitionerRole Management"
            @click="emit('closeMobile')"
          >
            <Briefcase :size="17" class="nav-icon" />
            <span v-if="!collapsed" class="nav-label">Practitioner Roles</span>
          </router-link>

          <router-link 
            to="/admin/organizations" 
            class="nav-link"
            :class="{ 'nav-link--active': isActive('/admin/organizations') }"
            title="Organization Directory"
            @click="emit('closeMobile')"
          >
            <Building2 :size="17" class="nav-icon" />
            <span v-if="!collapsed" class="nav-label">Organisations</span>
          </router-link>

          <router-link 
            to="/admin/locations" 
            class="nav-link"
            :class="{ 'nav-link--active': isActive('/admin/locations') }"
            title="Location Hierarchy"
            @click="emit('closeMobile')"
          >
            <MapPin :size="17" class="nav-icon" />
            <span v-if="!collapsed" class="nav-label">Locations</span>
          </router-link>

          <router-link 
            to="/admin/services" 
            class="nav-link"
            :class="{ 'nav-link--active': isActive('/admin/services') }"
            title="Healthcare Services"
            @click="emit('closeMobile')"
          >
            <Activity :size="17" class="nav-icon" />
            <span v-if="!collapsed" class="nav-label">Healthcare Services</span>
          </router-link>

          <router-link 
            to="/admin/endpoints" 
            class="nav-link"
            :class="{ 'nav-link--active': isActive('/admin/endpoints') }"
            title="Electronic Endpoints"
            @click="emit('closeMobile')"
          >
            <Server :size="17" class="nav-icon" />
            <span v-if="!collapsed" class="nav-label">Endpoints</span>
          </router-link>

          <router-link 
            to="/admin/groups" 
            class="nav-link"
            :class="{ 'nav-link--active': isActive('/admin/groups') }"
            title="Provider &amp; Care Groups"
            @click="emit('closeMobile')"
          >
            <Users :size="17" class="nav-icon" />
            <span v-if="!collapsed" class="nav-label">Groups</span>
          </router-link>

          <router-link 
            to="/admin/data-quality" 
            class="nav-link"
            :class="{ 'nav-link--active': isActive('/admin/data-quality') }"
            title="Data Quality &amp; Referential Integrity"
            @click="emit('closeMobile')"
          >
            <CheckSquare :size="17" class="nav-icon text-emerald-400" />
            <span v-if="!collapsed" class="nav-label">Data Quality</span>
          </router-link>
        </div>

        <!-- Tier 3: Administrative Services (Extensible Foundation) -->
        <div class="nav-group">
          <div v-if="!collapsed" class="nav-group-title">
            <span>Administrative Services</span>
          </div>

          <router-link 
            to="/services/audit" 
            class="nav-link"
            :class="{ 'nav-link--active': isActive('/services/audit') }"
            title="Audit &amp; Lineage Governance"
            @click="emit('closeMobile')"
          >
            <FolderTree :size="17" class="nav-icon text-slate-400" />
            <span v-if="!collapsed" class="nav-label">Audit &amp; Governance</span>
          </router-link>

          <router-link 
            to="/services/reference-data" 
            class="nav-link"
            :class="{ 'nav-link--active': isActive('/services/reference-data') }"
            title="Reference Data &amp; ValueSets"
            @click="emit('closeMobile')"
          >
            <Database :size="17" class="nav-icon text-slate-400" />
            <span v-if="!collapsed" class="nav-label">Reference Data</span>
          </router-link>
        </div>

        <!-- Tier 4: Administration & System -->
        <div class="nav-group">
          <div v-if="!collapsed" class="nav-group-title">
            <span>System Administration</span>
          </div>

          <router-link 
            to="/system/security" 
            class="nav-link"
            :class="{ 'nav-link--active': isActive('/system/security') }"
            title="Themis Security Context"
            @click="emit('closeMobile')"
          >
            <Shield :size="17" class="nav-icon text-slate-400" />
            <span v-if="!collapsed" class="nav-label">Themis Context</span>
          </router-link>

          <router-link 
            to="/system/preferences" 
            class="nav-link"
            :class="{ 'nav-link--active': isActive('/system/preferences') }"
            title="System Preferences &amp; Config"
            @click="emit('closeMobile')"
          >
            <SlidersHorizontal :size="17" class="nav-icon text-slate-400" />
            <span v-if="!collapsed" class="nav-label">Preferences</span>
          </router-link>
        </div>
      </div>
    </aside>
  </div>
</template>

<style scoped>
.sidebar-root {
  position: fixed;
  top: 0;
  left: 0;
  bottom: 0;
  z-index: 50;
  background-color: #0d1322;
  border-right: 1px solid #1f293d;
  display: flex;
  flex-direction: column;
  transition: width 0.25s cubic-bezier(0.4, 0, 0.2, 1), transform 0.25s ease;
}

.sidebar--expanded {
  width: 260px;
}

.sidebar--collapsed {
  width: 72px;
}

@media (max-width: 767px) {
  .sidebar-root {
    width: 260px !important;
  }
  .sidebar--mobile-closed {
    transform: translateX(-100%);
  }
  .sidebar--mobile-open {
    transform: translateX(0);
  }
}

.sidebar-header {
  height: 64px;
  padding: 0 1rem;
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid #1f293d;
  background-color: #0b101c;
}

.brand-icon {
  width: 36px;
  height: 36px;
  border-radius: 8px;
  background: linear-gradient(135deg, rgba(56, 189, 248, 0.15), rgba(129, 140, 248, 0.15));
  border: 1px solid rgba(56, 189, 248, 0.3);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.sidebar-nav {
  flex: 1;
  overflow-y: auto;
  padding: 1rem 0.65rem;
  display: flex;
  flex-direction: column;
  gap: 1.25rem;
}

.nav-group {
  display: flex;
  flex-direction: column;
  gap: 0.2rem;
}

.nav-group-title {
  font-size: 0.675rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.08em;
  color: #64748b;
  padding: 0.25rem 0.75rem 0.35rem 0.75rem;
}

.nav-link {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 0.55rem 0.75rem;
  border-radius: 0.5rem;
  color: #94a3b8;
  text-decoration: none;
  font-size: 0.85rem;
  font-weight: 500;
  transition: all 0.15s ease;
  white-space: nowrap;
}

.nav-link:hover {
  color: #ffffff;
  background-color: rgba(255, 255, 255, 0.04);
}

.nav-link--active {
  color: #ffffff !important;
  background: linear-gradient(90deg, rgba(56, 189, 248, 0.15), rgba(56, 189, 248, 0.05)) !important;
  border-left: 3px solid #38bdf8;
  font-weight: 600;
}

.highlight-link {
  background-color: rgba(56, 189, 248, 0.08);
  border: 1px dashed rgba(56, 189, 248, 0.3);
}

.highlight-link:hover {
  background-color: rgba(56, 189, 248, 0.15);
  border-color: rgba(56, 189, 248, 0.5);
}

.nav-icon {
  flex-shrink: 0;
}

.nav-label {
  overflow: hidden;
  text-overflow: ellipsis;
}

.custom-scrollbar::-webkit-scrollbar {
  width: 4px;
}
.custom-scrollbar::-webkit-scrollbar-track {
  background: transparent;
}
.custom-scrollbar::-webkit-scrollbar-thumb {
  background: #1f293d;
  border-radius: 4px;
}
</style>
