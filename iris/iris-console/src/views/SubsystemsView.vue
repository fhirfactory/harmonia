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
import { onMounted, computed } from 'vue';
import { useOperationsStore } from '../stores/operationsStore';
import SubsystemSidebar from '../components/subsystems/SubsystemSidebar.vue';
import SubsystemHeader from '../components/subsystems/SubsystemHeader.vue';
import InstanceTable from '../components/subsystems/InstanceTable.vue';
import InstanceDetailDrawer from '../components/subsystems/InstanceDetailDrawer.vue';
import HealthDependenciesPanel from '../components/subsystems/HealthDependenciesPanel.vue';
import StatisticsPanel from '../components/subsystems/StatisticsPanel.vue';
import { AlertCircle, RefreshCw } from 'lucide-vue-next';

const store = useOperationsStore();

onMounted(async () => {
  if (store.subsystems.length === 0) {
    await store.fetchSubsystems();
  }
  const targetSubsystem = store.selectedSubsystemId || 'petasos';
  await store.selectSubsystem(targetSubsystem);
});

const onSelectSubsystem = (id: string) => {
  // Handled by store
};
</script>

<template>
  <div class="flex-1 flex flex-col md:flex-row min-h-screen bg-[#0b0f19]">
    <!-- Left Navigation: Subsystems Inventory Hierarchy -->
    <SubsystemSidebar @select="onSelectSubsystem" />

    <!-- Main Operational Perspective Area -->
    <div class="flex-1 flex flex-col min-w-0 overflow-y-auto">
      <!-- Subsystem Header -->
      <SubsystemHeader />

      <!-- Error banner if present -->
      <div 
        v-if="store.error" 
        class="m-4 p-3 rounded-lg bg-rose-500/15 border border-rose-500/30 text-rose-300 text-xs flex items-center gap-2 font-mono"
        role="alert"
      >
        <AlertCircle :size="16" class="shrink-0 text-rose-400" />
        <span>{{ store.error }}</span>
      </div>

      <!-- Content Panes (Top, Middle, Bottom) -->
      <div class="p-4 lg:p-6 space-y-6 flex-1">
        <!-- Top Pane: Domain-neutral Runtime Instances -->
        <section aria-labelledby="instances-heading">
          <InstanceTable />
        </section>

        <!-- Middle Pane: Operational Health & Upstream Dependencies -->
        <section aria-labelledby="health-heading">
          <HealthDependenciesPanel />
        </section>

        <!-- Bottom Pane: Operational Statistics & Micro-Charts -->
        <section aria-labelledby="statistics-heading">
          <StatisticsPanel />
        </section>
      </div>

      <!-- Slide-Out Instance Detail Drawer -->
      <InstanceDetailDrawer />
    </div>
  </div>
</template>
