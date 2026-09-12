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
import { useRoute } from 'vue-router';
import { 
  Activity, LayoutDashboard, Users, UserCheck, Building2, 
  MapPin, Stethoscope, UsersRound, CheckSquare, MessageSquare, 
  FileText, GitBranch, ShieldAlert, FileCheck, ChevronLeft, 
  ChevronRight, Search, X, ExternalLink, Sparkles
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
const searchQuery = ref('');

interface NavItem {
  name: string;
  path: string;
  icon: any;
  badge?: string;
  description: string;
}

interface NavSection {
  title: string;
  items: NavItem[];
}

const navSections: NavSection[] = [
  {
    title: 'Overview',
    items: [
      { name: 'Dashboard', path: '/', icon: LayoutDashboard, description: 'Clinical summary & resource statistics' }
    ]
  },
  {
    title: 'Directory & Registry',
    items: [
      { name: 'Persons & Patients', path: '/persons', icon: Users, description: 'Master patient identity and person records' },
      { name: 'Practitioners', path: '/practitioners', icon: UserCheck, description: 'Healthcare providers and specialist credentials' },
      { name: 'Organizations', path: '/organizations', icon: Building2, description: 'Healthcare institutions and clinics' },
      { name: 'Locations', path: '/locations', icon: MapPin, description: 'Facilities, hospital wings, and rooms' },
      { name: 'Services', path: '/services', icon: Stethoscope, description: 'Clinical and specialized healthcare services' },
      { name: 'Groups', path: '/groups', icon: UsersRound, description: 'Patient cohorts and provider teams' }
    ]
  },
  {
    title: 'Clinical Workflow',
    items: [
      { name: 'Tasks', path: '/tasks', icon: CheckSquare, badge: 'Synthetic', description: 'Actionable clinical workflows and activities' },
      { name: 'Communication', path: '/communication', icon: MessageSquare, description: 'Raw message logs and transmission payloads' },
      { name: 'Documents', path: '/documents', icon: FileText, description: 'Clinical summaries, reports, and CDA/PDF notes' }
    ]
  },
  {
    title: 'Lineage & Governance',
    items: [
      { name: 'Provenance', path: '/provenance', icon: GitBranch, description: 'Resource lifecycle tracing and lineage graph' },
      { name: 'Audit Events', path: '/audit', icon: ShieldAlert, description: 'Security access events and compliance log' },
      { name: 'Consent', path: '/consent', icon: FileCheck, description: 'Patient data sharing policies and directives' }
    ]
  }
];

const filteredSections = computed(() => {
  if (!searchQuery.value.trim()) {
    return navSections;
  }
  const q = searchQuery.value.trim().toLowerCase();
  return navSections
    .map(section => ({
      ...section,
      items: section.items.filter(item => 
        item.name.toLowerCase().includes(q) || 
        item.description.toLowerCase().includes(q) ||
        section.title.toLowerCase().includes(q)
      )
    }))
    .filter(section => section.items.length > 0);
});

const isCurrent = (path: string) => {
  if (path === '/') return route.path === '/';
  return route.path.startsWith(path);
};

const handleNavClick = () => {
  if (props.mobileOpen) {
    emit('closeMobile');
  }
};
</script>

<template>
  <div>
    <!-- Mobile Backdrop -->
    <div 
      v-if="mobileOpen" 
      class="sidebar-backdrop"
      @click="emit('closeMobile')"
    ></div>

    <!-- Sidebar Aside -->
    <aside 
      class="sidebar"
      :class="[
        collapsed ? 'sidebar--collapsed' : 'sidebar--expanded',
        mobileOpen ? 'sidebar--mobile-open' : ''
      ]"
    >
      <!-- Brand / Header -->
      <div class="sidebar-header">
        <router-link to="/" class="sidebar-brand" @click="handleNavClick">
          <div class="sidebar-logo-box">
            <Activity :size="20" class="text-sky-400" />
          </div>
          <div v-if="!collapsed" class="sidebar-brand-text">
            <div class="sidebar-brand-title">
              <span>FHIR Explorer</span>
              <span class="badge badge-blue text-[10px] py-0 px-1.5">R5</span>
            </div>
            <span class="sidebar-brand-sub">Clinical Data Hub</span>
          </div>
        </router-link>

        <!-- Toggle Collapse Button (Desktop) -->
        <button 
          class="sidebar-toggle-btn hidden md:flex" 
          @click="emit('toggleCollapse')"
          :title="collapsed ? 'Expand sidebar' : 'Collapse sidebar'"
        >
          <ChevronRight v-if="collapsed" :size="16" />
          <ChevronLeft v-else :size="16" />
        </button>

        <!-- Close Button (Mobile) -->
        <button 
          class="sidebar-close-btn flex md:hidden" 
          @click="emit('closeMobile')"
          title="Close navigation"
        >
          <X :size="18" />
        </button>
      </div>

      <!-- Quick Search / Filter in Sidebar (When expanded) -->
      <div v-if="!collapsed" class="sidebar-search">
        <div class="sidebar-search-box">
          <Search :size="14" class="sidebar-search-icon" />
          <input 
            v-model="searchQuery" 
            type="text" 
            placeholder="Filter resources..." 
            class="sidebar-search-input"
          />
          <button 
            v-if="searchQuery" 
            @click="searchQuery = ''" 
            class="sidebar-search-clear"
          >
            <X :size="12" />
          </button>
        </div>
      </div>

      <!-- Navigation Content -->
      <nav class="sidebar-nav custom-scrollbar">
        <div 
          v-for="section in filteredSections" 
          :key="section.title" 
          class="sidebar-section"
        >
          <div v-if="!collapsed" class="sidebar-section-title">
            {{ section.title }}
          </div>
          <div v-else class="sidebar-section-divider"></div>

          <div class="sidebar-menu">
            <router-link
              v-for="item in section.items"
              :key="item.path"
              :to="item.path"
              class="sidebar-item"
              :class="{ 'sidebar-item--active': isCurrent(item.path) }"
              @click="handleNavClick"
            >
              <div class="sidebar-item-icon">
                <component :is="item.icon" :size="18" />
              </div>

              <span v-if="!collapsed" class="sidebar-item-label">{{ item.name }}</span>

              <span 
                v-if="!collapsed && item.badge" 
                class="badge badge-blue text-[9px] py-0 px-1 ml-auto"
              >
                {{ item.badge }}
              </span>

              <!-- Hover Tooltip for Collapsed Mode -->
              <div v-if="collapsed" class="sidebar-tooltip">
                <div class="font-semibold text-white">{{ item.name }}</div>
                <div class="text-[11px] text-slate-400 font-normal mt-0.5">{{ item.description }}</div>
              </div>
            </router-link>
          </div>
        </div>

        <!-- No Results Fallback -->
        <div v-if="filteredSections.length === 0" class="p-4 text-center text-xs text-slate-400">
          No resources matching "{{ searchQuery }}"
        </div>
      </nav>

      <!-- Sidebar Footer -->
      <div class="sidebar-footer">
        <a 
          href="http://localhost:3001" 
          target="_blank" 
          rel="noopener noreferrer" 
          class="sidebar-switch-btn"
          :title="collapsed ? 'Switch to Operations UI (Port 3001)' : ''"
        >
          <div class="flex items-center gap-2">
            <Sparkles :size="16" class="text-emerald-400 flex-shrink-0" />
            <span v-if="!collapsed" class="text-xs font-semibold text-slate-200">Operations Center</span>
          </div>
          <ExternalLink v-if="!collapsed" :size="13" class="text-slate-400" />
          
          <div v-if="collapsed" class="sidebar-tooltip">
            <div class="font-semibold text-emerald-400">Operations Center</div>
            <div class="text-[11px] text-slate-400 font-normal">Switch to Cluster &amp; Task Sequences UI</div>
          </div>
        </a>

        <div v-if="!collapsed" class="sidebar-system-info">
          <div class="flex items-center justify-between text-[11px] text-slate-500">
            <span>HIE Platform v1.0</span>
            <span class="flex items-center gap-1 text-emerald-400">
              <span class="w-1.5 h-1.5 rounded-full bg-emerald-400 animate-pulse"></span>
              Online
            </span>
          </div>
        </div>
      </div>
    </aside>
  </div>
</template>

<style scoped>
.sidebar-backdrop {
  position: fixed;
  inset: 0;
  background-color: rgba(0, 0, 0, 0.65);
  backdrop-filter: blur(4px);
  z-index: 45;
  transition: opacity 0.3s ease;
}

.sidebar {
  position: fixed;
  top: 0;
  bottom: 0;
  left: 0;
  z-index: 50;
  display: flex;
  flex-direction: column;
  background: linear-gradient(180deg, #111827 0%, #0d131f 100%);
  border-right: 1px solid #27344d;
  box-shadow: 4px 0 24px rgba(0, 0, 0, 0.35);
  transition: width 0.25s cubic-bezier(0.4, 0, 0.2, 1), transform 0.25s cubic-bezier(0.4, 0, 0.2, 1);
}

.sidebar--expanded {
  width: 260px;
}

.sidebar--collapsed {
  width: 72px;
}

@media (max-width: 767px) {
  .sidebar {
    width: 270px;
    transform: translateX(-100%);
  }
  .sidebar--mobile-open {
    transform: translateX(0);
  }
}

.sidebar-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1.15rem 1rem;
  border-bottom: 1px solid #1f293d;
  min-height: 64px;
}

.sidebar-brand {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  text-decoration: none;
  overflow: hidden;
}

.sidebar-logo-box {
  padding: 0.5rem;
  border-radius: 0.6rem;
  background: rgba(56, 189, 248, 0.12);
  border: 1px solid rgba(56, 189, 248, 0.3);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  transition: transform 0.2s;
}

.sidebar-brand:hover .sidebar-logo-box {
  transform: scale(1.05);
  background: rgba(56, 189, 248, 0.2);
}

.sidebar-brand-text {
  display: flex;
  flex-direction: column;
  line-height: 1.2;
}

.sidebar-brand-title {
  display: flex;
  align-items: center;
  gap: 0.4rem;
  font-weight: 800;
  color: #ffffff;
  font-size: 0.95rem;
  letter-spacing: -0.02em;
}

.sidebar-brand-sub {
  font-size: 0.7rem;
  color: #94a3b8;
  font-weight: 500;
}

.sidebar-toggle-btn, .sidebar-close-btn {
  background: transparent;
  border: 1px solid transparent;
  color: #94a3b8;
  padding: 0.4rem;
  border-radius: 0.4rem;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.15s;
}

.sidebar-toggle-btn:hover, .sidebar-close-btn:hover {
  background: rgba(255, 255, 255, 0.08);
  color: #ffffff;
  border-color: #27344d;
}

.sidebar-search {
  padding: 0.75rem 0.85rem 0.25rem 0.85rem;
}

.sidebar-search-box {
  position: relative;
  display: flex;
  align-items: center;
}

.sidebar-search-icon {
  position: absolute;
  left: 0.65rem;
  color: #64748b;
  pointer-events: none;
}

.sidebar-search-input {
  width: 100%;
  background: #0b101c;
  border: 1px solid #27344d;
  color: #f1f5f9;
  font-size: 0.78rem;
  padding: 0.45rem 1.6rem 0.45rem 1.9rem;
  border-radius: 0.45rem;
  outline: none;
  transition: border-color 0.15s, box-shadow 0.15s;
}

.sidebar-search-input:focus {
  border-color: #38bdf8;
  box-shadow: 0 0 0 2px rgba(56, 189, 248, 0.15);
}

.sidebar-search-clear {
  position: absolute;
  right: 0.5rem;
  background: transparent;
  border: none;
  color: #64748b;
  cursor: pointer;
  padding: 0.2rem;
}

.sidebar-search-clear:hover {
  color: #f1f5f9;
}

.sidebar-nav {
  flex: 1;
  overflow-y: auto;
  padding: 0.75rem 0.6rem;
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.sidebar-section-title {
  font-size: 0.65rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.08em;
  color: #64748b;
  padding: 0.35rem 0.65rem;
}

.sidebar-section-divider {
  height: 1px;
  background-color: #1f293d;
  margin: 0.5rem 0.25rem;
}

.sidebar-menu {
  display: flex;
  flex-direction: column;
  gap: 0.2rem;
}

.sidebar-item {
  position: relative;
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 0.55rem 0.65rem;
  border-radius: 0.5rem;
  color: #94a3b8;
  text-decoration: none;
  font-size: 0.825rem;
  font-weight: 500;
  transition: all 0.15s ease;
}

.sidebar--collapsed .sidebar-item {
  justify-content: center;
  padding: 0.65rem;
}

.sidebar-item:hover {
  background-color: rgba(255, 255, 255, 0.05);
  color: #f8fafc;
}

.sidebar-item--active {
  background: rgba(56, 189, 248, 0.12);
  color: #38bdf8 !important;
  font-weight: 600;
  border: 1px solid rgba(56, 189, 248, 0.25);
}

.sidebar-item--active .sidebar-item-icon {
  color: #38bdf8;
}

.sidebar-item-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  color: #94a3b8;
  transition: color 0.15s;
}

.sidebar-item-label {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.sidebar-tooltip {
  position: absolute;
  left: calc(100% + 10px);
  top: 50%;
  transform: translateY(-50%);
  background: #1e293b;
  border: 1px solid #334155;
  border-radius: 0.5rem;
  padding: 0.5rem 0.75rem;
  white-space: nowrap;
  z-index: 100;
  opacity: 0;
  pointer-events: none;
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.5);
  transition: opacity 0.15s ease, transform 0.15s ease;
}

.sidebar-item:hover .sidebar-tooltip,
.sidebar-switch-btn:hover .sidebar-tooltip {
  opacity: 1;
  transform: translateY(-50%) translateX(2px);
}

.sidebar-footer {
  padding: 0.85rem 0.75rem;
  border-top: 1px solid #1f293d;
  background: rgba(13, 19, 31, 0.8);
  display: flex;
  flex-direction: column;
  gap: 0.65rem;
}

.sidebar-switch-btn {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0.6rem 0.75rem;
  background: rgba(16, 185, 129, 0.08);
  border: 1px solid rgba(16, 185, 129, 0.25);
  border-radius: 0.5rem;
  text-decoration: none;
  transition: all 0.2s ease;
}

.sidebar--collapsed .sidebar-switch-btn {
  justify-content: center;
  padding: 0.6rem;
}

.sidebar-switch-btn:hover {
  background: rgba(16, 185, 129, 0.15);
  border-color: rgba(16, 185, 129, 0.45);
}

.sidebar-system-info {
  padding: 0.2rem 0.25rem 0;
}

.custom-scrollbar::-webkit-scrollbar {
  width: 5px;
}
.custom-scrollbar::-webkit-scrollbar-track {
  background: transparent;
}
.custom-scrollbar::-webkit-scrollbar-thumb {
  background: #27344d;
  border-radius: 4px;
}
.custom-scrollbar::-webkit-scrollbar-thumb:hover {
  background: #3b4d6e;
}
</style>
