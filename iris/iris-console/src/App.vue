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
import { onMounted, onUnmounted } from 'vue';
import { useOperationsStore } from './stores/operationsStore';
import GlobalOperationsHeader from './components/common/GlobalOperationsHeader.vue';
import NavigationTopBar from './components/common/NavigationTopBar.vue';

const store = useOperationsStore();

onMounted(async () => {
  await store.fetchSummary();
  store.startPolling(10000);
});

onUnmounted(() => {
  store.stopPolling();
});
</script>

<template>
  <div class="app-root min-h-screen flex flex-col bg-[#0b0f19] text-slate-100 font-sans selection:bg-sky-500/30 selection:text-white">
    <!-- Persistent Global Operations Header -->
    <GlobalOperationsHeader />

    <!-- Persistent Top Navigation Bar (5 Perspectives) -->
    <NavigationTopBar />

    <!-- Perspective Main Content -->
    <main class="flex-1 flex flex-col min-w-0">
      <router-view />
    </main>

    <!-- Operational Platform Footer -->
    <footer class="border-t border-[#1f293d] bg-[#0d1322] py-3 px-6 text-xs text-slate-500 font-mono">
      <div class="max-w-7xl mx-auto flex flex-col md:flex-row items-center justify-between gap-2">
        <div class="flex items-center gap-2">
          <span class="font-bold text-slate-300 font-sans">Harmonia Operations Console</span>
          <span>&bull;</span>
          <span>Operations Backend :8090</span>
          <span>&bull;</span>
          <span class="text-sky-400 font-sans font-semibold">Themis Default-Deny RBAC</span>
        </div>
        <div class="flex items-center gap-3 text-slate-400">
          <span>Infinispan 15</span>
          <span>&bull;</span>
          <span>ActiveMQ Artemis</span>
          <span>&bull;</span>
          <span class="text-emerald-400 font-sans font-semibold">Zero-PHI Presentation Boundary</span>
        </div>
      </div>
    </footer>
  </div>
</template>

<style scoped>
.app-root {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  width: 100%;
}
</style>
