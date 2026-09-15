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
import { ref, onMounted } from 'vue';
import Sidebar from './components/Sidebar.vue';
import Topbar from './components/Topbar.vue';

const isSidebarCollapsed = ref(false);
const isMobileOpen = ref(false);

onMounted(() => {
  const saved = localStorage.getItem('hie-fhir-sidebar-collapsed');
  if (saved !== null) {
    isSidebarCollapsed.value = saved === 'true';
  }
});

const toggleCollapse = () => {
  isSidebarCollapsed.value = !isSidebarCollapsed.value;
  localStorage.setItem('hie-fhir-sidebar-collapsed', String(isSidebarCollapsed.value));
};

const toggleMobile = () => {
  isMobileOpen.value = !isMobileOpen.value;
};

const closeMobile = () => {
  isMobileOpen.value = false;
};
</script>

<template>
  <div class="app-root min-h-screen flex bg-[#0b0f19] text-slate-100 font-sans">
    <!-- Collapsible Sidebar -->
    <Sidebar 
      :collapsed="isSidebarCollapsed" 
      :mobile-open="isMobileOpen"
      @toggle-collapse="toggleCollapse"
      @close-mobile="closeMobile"
    />

    <!-- Main Content Area with Dynamic Margin for Sidebar -->
    <div 
      class="app-main-wrapper flex-1 flex flex-col min-w-0"
      :class="isSidebarCollapsed ? 'app-main--collapsed' : 'app-main--expanded'"
    >
      <!-- Topbar Header -->
      <Topbar 
        :is-sidebar-collapsed="isSidebarCollapsed"
        @toggle-mobile="toggleMobile"
        @toggle-collapse="toggleCollapse"
      />

      <!-- Page Content -->
      <main class="flex-1 w-full p-4 md:p-6 lg:p-8 max-w-7xl mx-auto">
        <router-view />
      </main>

      <!-- App Footer -->
      <footer class="border-t border-[#1f293d] bg-[#111827]/60 py-4 px-6 text-center md:text-left text-xs text-slate-500">
        <div class="max-w-7xl mx-auto flex flex-col md:flex-row items-center justify-between gap-2">
          <div>
            <span class="font-bold text-slate-300">HIE FHIR Resource Explorer</span>
            <span class="mx-2">&bull;</span>
            <span>FHIR R5 Clinical &amp; Administrative Data Repository</span>
          </div>
          <div class="flex items-center gap-3 text-slate-400">
            <span>BEFE (Port 8080)</span>
            <span>&bull;</span>
            <span>Infinispan 15</span>
            <span>&bull;</span>
            <span>HAPI FHIR JPA</span>
          </div>
        </div>
      </footer>
    </div>
  </div>
</template>

<style scoped>
.app-root {
  display: flex;
  min-height: 100vh;
  width: 100%;
}

.app-main-wrapper {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  transition: margin-left 0.25s cubic-bezier(0.4, 0, 0.2, 1);
}

@media (min-width: 768px) {
  .app-main--expanded {
    margin-left: 260px;
  }

  .app-main--collapsed {
    margin-left: 72px;
  }
}

@media (max-width: 767px) {
  .app-main-wrapper {
    margin-left: 0 !important;
  }
}
</style>
