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
import { ref, onMounted, computed, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useOperationsStore } from '../stores/operationsStore';
import { findAuthoritativeSubsystem } from '../models/subsystemHierarchy';
import SubsystemTreeNavigator from '../components/subsystems/SubsystemTreeNavigator.vue';
import SubsystemHeader from '../components/subsystems/SubsystemHeader.vue';
import SubordinatedMiddlewarePanel from '../components/subsystems/SubordinatedMiddlewarePanel.vue';
import InstanceTable from '../components/subsystems/InstanceTable.vue';
import InstanceDetailDrawer from '../components/subsystems/InstanceDetailDrawer.vue';
import HealthDependenciesPanel from '../components/subsystems/HealthDependenciesPanel.vue';
import StatisticsPanel from '../components/subsystems/StatisticsPanel.vue';
import { IrisBreadcrumbs, IrisToolbar, IrisErrorState } from '@harmonia/iris-befe';
import { Server, HeartPulse, BarChart2, Layers } from 'lucide-vue-next';

const store = useOperationsStore();
const route = useRoute();
const router = useRouter();

type DetailTab = 'instances' | 'health' | 'statistics' | 'all';
const activeTab = ref<DetailTab>('instances');

const currentSubsystemId = computed(() => store.selectedSubsystemId);
const subsystem = computed(() => store.selectedSubsystem);

const authoritativeSubsystem = computed(() => {
  const id = currentSubsystemId.value?.toLowerCase();
  return id ? findAuthoritativeSubsystem(id) : null;
});

const breadcrumbs = computed(() => {
  const area = authoritativeSubsystem.value?.areaName || 'Platform Hierarchy';
  const name = subsystem.value?.name || 'Subsystem';
  const subtitle = authoritativeSubsystem.value?.englishTitle ? ` (${authoritativeSubsystem.value.englishTitle})` : '';
  return ['Harmonia', area, `${name}${subtitle}`];
});

const getTargetSubsystemId = () => {
  return (route?.params?.id as string) || (route?.query?.subsystem as string) || undefined;
};

const syncRouteWithStore = async () => {
  if (store.subsystems.length === 0) {
    await store.fetchSubsystems();
  }
  const targetId = getTargetSubsystemId() || store.selectedSubsystemId || 'petasos';
  await store.selectSubsystem(targetId);
};

onMounted(async () => {
  await syncRouteWithStore();
});

watch(
  () => [route?.params?.id, route?.query?.subsystem],
  async () => {
    const targetId = getTargetSubsystemId();
    if (targetId && targetId !== store.selectedSubsystemId) {
      await store.selectSubsystem(targetId);
    }
  }
);

const onSelectSubsystem = (id: string) => {
  if (route?.params?.id !== id) {
    router?.push?.({ path: `/subsystems/${id}` });
  }
};

const refreshCurrent = async () => {
  if (currentSubsystemId.value) {
    await Promise.all([
      store.fetchInstances(currentSubsystemId.value),
      store.fetchHealth(currentSubsystemId.value),
      store.fetchStatistics(currentSubsystemId.value, store.selectedWindow)
    ]);
  }
};
</script>

<template>
  <div class="subsystems-view">
    <!-- Left Pane: Split-Tree Master Hierarchy Navigator (~25-35% desktop width) -->
    <SubsystemTreeNavigator 
      class="subsystems-view__tree-pane" 
      @select="onSelectSubsystem" 
    />

    <!-- Right Pane: Subsystem Detail Inspection Workstation (~65-75% desktop width) -->
    <main 
      class="subsystems-view__detail-pane"
      role="main"
      aria-label="Subsystem Operational Details"
    >
      <!-- Top Breadcrumbs Bar -->
      <div class="subsystems-view__breadcrumbs-bar">
        <IrisBreadcrumbs :items="breadcrumbs" />
      </div>

      <!-- Selected Subsystem Identity Header -->
      <SubsystemHeader />

      <!-- Action Toolbar -->
      <div class="subsystems-view__toolbar-wrapper">
        <IrisToolbar
          :show-refresh="true"
          :refreshing="store.refreshing || store.loading"
          :show-time-window="true"
          :time-window="store.selectedWindow"
          @refresh="refreshCurrent"
          @update:time-window="store.setWindow($event as any)"
        >
          <template #left>
            <!-- Detail Tab Switcher Buttons -->
            <div 
              class="subsystems-view__tablist"
              role="tablist"
              aria-label="Subsystem inspection perspectives"
            >
              <button
                type="button"
                role="tab"
                :aria-selected="activeTab === 'instances'"
                @click="activeTab = 'instances'"
                class="subsystems-view__tab-btn"
                :class="{ 'is-active': activeTab === 'instances' }"
              >
                <Server :size="13" />
                <span>Runtime Instances</span>
                <span class="subsystems-view__tab-count">
                  {{ store.instances.length }}
                </span>
              </button>

              <button
                type="button"
                role="tab"
                :aria-selected="activeTab === 'health'"
                @click="activeTab = 'health'"
                class="subsystems-view__tab-btn"
                :class="{ 'is-active': activeTab === 'health' }"
              >
                <HeartPulse :size="13" />
                <span>Health &amp; Dependencies</span>
              </button>

              <button
                type="button"
                role="tab"
                :aria-selected="activeTab === 'statistics'"
                @click="activeTab = 'statistics'"
                class="subsystems-view__tab-btn"
                :class="{ 'is-active': activeTab === 'statistics' }"
              >
                <BarChart2 :size="13" />
                <span>Telemetry Statistics</span>
              </button>

              <button
                type="button"
                role="tab"
                :aria-selected="activeTab === 'all'"
                @click="activeTab = 'all'"
                class="subsystems-view__tab-btn"
                :class="{ 'is-active': activeTab === 'all' }"
              >
                <Layers :size="13" />
                <span>All Sections</span>
              </button>
            </div>
          </template>
        </IrisToolbar>
      </div>

      <!-- Error banner if present -->
      <div v-if="store.error" class="subsystems-view__error-banner">
        <IrisErrorState
          title="Subsystem data could not be loaded"
          :message="store.error"
          :retryable="true"
          @retry="refreshCurrent"
        />
      </div>

      <!-- Tab Content Area -->
      <div class="subsystems-view__content">
        <!-- Subordinated Middleware Panel (if present on subsystem) -->
        <SubordinatedMiddlewarePanel
          v-if="authoritativeSubsystem?.middleware && (activeTab === 'instances' || activeTab === 'all')"
          :middleware="authoritativeSubsystem.middleware"
          :subsystem-name="subsystem?.name || 'Subsystem'"
        />

        <!-- Tab 1: Runtime Instances -->
        <section 
          v-if="activeTab === 'instances' || activeTab === 'all'" 
          aria-labelledby="instances-heading"
          class="subsystems-view__section"
        >
          <InstanceTable />
        </section>

        <!-- Tab 2: Operational Health & Dependencies -->
        <section 
          v-if="activeTab === 'health' || activeTab === 'all'" 
          aria-labelledby="health-heading"
          class="subsystems-view__section"
        >
          <HealthDependenciesPanel />
        </section>

        <!-- Tab 3: Telemetry Statistics -->
        <section 
          v-if="activeTab === 'statistics' || activeTab === 'all'" 
          aria-labelledby="statistics-heading"
          class="subsystems-view__section"
        >
          <StatisticsPanel />
        </section>
      </div>

      <!-- Slide-Out Instance Detail Drawer -->
      <InstanceDetailDrawer />
    </main>
  </div>
</template>

<style scoped>
.subsystems-view {
  display: flex;
  flex-direction: row;
  height: 100%;
  min-height: calc(100vh - 88px);
  width: 100%;
  background-color: var(--iris-bg-page, #f8fafc);
  font-family: var(--iris-font-sans, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif);
  overflow: hidden;
}

.subsystems-view__tree-pane {
  width: 320px;
  min-width: 280px;
  max-width: 360px;
  flex-shrink: 0;
  height: 100%;
}

.subsystems-view__detail-pane {
  flex: 1 1 0%;
  min-width: 0;
  height: 100%;
  display: flex;
  flex-direction: column;
  overflow-y: auto;
  background-color: var(--iris-bg-page, #f8fafc);
}

.subsystems-view__breadcrumbs-bar {
  padding: 8px 20px;
  background-color: var(--iris-bg-surface, #ffffff);
  border-bottom: 1px solid var(--iris-border-default, #e2e8f0);
}

.subsystems-view__toolbar-wrapper {
  padding: 8px 20px;
  background-color: var(--iris-bg-surface, #ffffff);
  border-bottom: 1px solid var(--iris-border-default, #e2e8f0);
}

.subsystems-view__tablist {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  background-color: var(--iris-bg-page, #f1f5f9);
  padding: 3px;
  border-radius: 6px;
  border: 1px solid var(--iris-border-default, #e2e8f0);
}

.subsystems-view__tab-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 5px 10px;
  font-size: 12px;
  font-weight: 600;
  border-radius: 4px;
  border: 1px solid transparent;
  background-color: transparent;
  color: var(--iris-text-secondary, #475569);
  cursor: pointer;
  transition: all 0.15s ease-in-out;
  outline: none;
}

.subsystems-view__tab-btn:hover {
  color: var(--iris-text-primary, #0f172a);
  background-color: rgba(255, 255, 255, 0.6);
}

.subsystems-view__tab-btn.is-active {
  background-color: var(--iris-bg-surface, #ffffff);
  color: var(--iris-color-primary, #0284c7);
  border-color: var(--iris-border-default, #e2e8f0);
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
}

.subsystems-view__tab-count {
  font-size: 10px;
  font-family: var(--iris-font-mono, monospace);
  font-weight: 700;
  padding: 1px 5px;
  border-radius: 3px;
  background-color: var(--iris-border-light, #e2e8f0);
  color: var(--iris-text-secondary, #475569);
}

.subsystems-view__tab-btn.is-active .subsystems-view__tab-count {
  background-color: #e0f2fe;
  color: #0369a1;
}

.subsystems-view__error-banner {
  margin: 16px 20px 0 20px;
}

.subsystems-view__content {
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 20px;
  flex: 1;
}

.subsystems-view__section {
  display: flex;
  flex-direction: column;
}

@media (max-width: 768px) {
  .subsystems-view {
    flex-direction: column;
    height: auto;
    overflow: visible;
  }
  .subsystems-view__tree-pane {
    width: 100%;
    max-width: none;
    height: 300px;
  }
}
</style>
