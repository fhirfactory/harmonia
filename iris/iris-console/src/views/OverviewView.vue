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
import { onMounted, onUnmounted, computed } from 'vue';
import { useOperationsStore } from '../stores/operationsStore';
import { useQueueStore } from '../stores/queueStore';
import { useWorkflowStore } from '../stores/workflowStore';
import { 
  IrisStatus, 
  IrisToolbar, 
  IrisSection, 
  IrisDataTable,
  type DataTableColumn
} from '@harmonia/iris-befe';
import { 
  HARMONIA_ARCHITECTURAL_AREAS, 
  AUTHORITATIVE_SUBSYSTEMS 
} from '../models/subsystemHierarchy';
import { 
  LayoutDashboard, 
  CheckCircle2, 
  AlertTriangle, 
  AlertOctagon, 
  ArrowRight, 
  RotateCw, 
  Clock,
  Layers
} from 'lucide-vue-next';

const operationsStore = useOperationsStore();
const queueStore = useQueueStore();
const workflowStore = useWorkflowStore();

let refreshTimer: any = null;

async function refreshAll() {
  await Promise.all([
    operationsStore.fetchStatus(),
    operationsStore.fetchSubsystems(),
    operationsStore.fetchAlerts(),
    queueStore.fetchQueues(true),
    workflowStore.fetchWorkflows(true)
  ]);
}

onMounted(() => {
  refreshAll();
  refreshTimer = setInterval(() => {
    refreshAll();
  }, 10000);
});

onUnmounted(() => {
  if (refreshTimer) {
    clearInterval(refreshTimer);
  }
});

const summary = computed(() => operationsStore.summary);
const platformStatus = computed(() => {
  if (summary.value?.platformStatus) return summary.value.platformStatus;
  if (operationsStore.subsystems.length === 0) return 'UNKNOWN';
  if (operationsStore.subsystems.some(subsystem => subsystem.state === 'UNAVAILABLE')) return 'UNAVAILABLE';
  if (operationsStore.subsystems.some(subsystem => subsystem.state === 'DEGRADED')) return 'DEGRADED';
  if (operationsStore.subsystems.some(subsystem => subsystem.state === 'UNKNOWN')) return 'UNKNOWN';
  return 'HEALTHY';
});

const totalSubsystems = computed<number | null>(() => {
  if (summary.value?.totalSubsystems != null) return summary.value.totalSubsystems;
  return operationsStore.subsystems.length > 0 ? operationsStore.subsystems.length : null;
});
const healthySubsystems = computed<number | null>(() => {
  if (summary.value?.totalSubsystems != null && summary.value?.degradedSubsystems != null) {
    return Math.max(0, summary.value.totalSubsystems - summary.value.degradedSubsystems);
  }
  if (operationsStore.subsystems.length === 0) return null;
  return operationsStore.subsystems.filter(subsystem => subsystem.state === 'HEALTHY').length;
});
const degradedSubsystems = computed<number | null>(() => {
  if (summary.value?.degradedSubsystems != null) return summary.value.degradedSubsystems;
  if (operationsStore.subsystems.length === 0) return null;
  return operationsStore.subsystems.filter(subsystem =>
    subsystem.state === 'DEGRADED' || subsystem.state === 'UNAVAILABLE'
  ).length;
});

const hasQueueTelemetry = computed(() => queueStore.queues.length > 0);
const hasWorkflowTelemetry = computed(() => workflowStore.workflows.length > 0);

function displayCount(value: number | null | undefined, available: boolean): string {
  return available && value != null ? value.toLocaleString() : '—';
}

function displayRate(value: number | null | undefined, available: boolean, unit: string): string {
  if (!available) return 'No live telemetry';
  if (value == null || value <= 0) return 'Telemetry initializing';
  return `${value.toFixed(1)} ${unit}`;
}

const queueCountText = computed(() => displayCount(queueStore.totalQueues, hasQueueTelemetry.value));
const queueDepthText = computed(() => displayCount(queueStore.messagesInFlight, hasQueueTelemetry.value));
const consumerCountText = computed(() => displayCount(queueStore.totalConsumers, hasQueueTelemetry.value));
const dlqDepthText = computed(() => displayCount(queueStore.totalDlqDepth, hasQueueTelemetry.value));
const activeExecutionText = computed(() => displayCount(workflowStore.totalActiveExecutions, hasWorkflowTelemetry.value));
const queuedWorkText = computed(() => displayCount(workflowStore.totalQueuedWork, hasWorkflowTelemetry.value));
const completedWorkText = computed(() => displayCount(workflowStore.totalCompletedWork, hasWorkflowTelemetry.value));
const workflowRateText = computed(() => displayRate(workflowStore.totalProcessingRate, hasWorkflowTelemetry.value, 'workflows/sec'));

const criticalAlerts = computed(() => operationsStore.criticalAlertsCount);
const warningAlerts = computed(() => operationsStore.warningAlertsCount);
const totalAlerts = computed(() => criticalAlerts.value + warningAlerts.value);

const lastRefreshedText = computed(() => {
  if (!operationsStore.lastRefreshed) return 'Not refreshed';
  return operationsStore.lastRefreshed.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', second: '2-digit' });
});

// Area summary column definitions
const areaColumns: DataTableColumn[] = [
  { field: 'area', header: 'Architectural Area' },
  { field: 'subsystems', header: 'Subsystems Included' },
  { field: 'status', header: 'Status', width: '130px' },
  { field: 'activeComponents', header: 'Active Components' },
  { field: 'throughput', header: 'Throughput / Rates', width: '200px', align: 'right' }
];

// Area telemetry mapping
const areaSummaries = computed(() => {
  return HARMONIA_ARCHITECTURAL_AREAS.map(area => {
    const subs = area.subsystemIds.map(id => {
      const live = operationsStore.subsystems.find(s => s.id === id);
      const auth = AUTHORITATIVE_SUBSYSTEMS[id];
      return {
        id,
        name: auth?.name || id,
        live,
        status: live?.state || 'UNKNOWN'
      };
    });

    const isDegraded = subs.some(s => s.status === 'DEGRADED');
    const isDown = subs.some(s => s.status === 'UNAVAILABLE');
    const isUnknown = subs.some(s => s.status === 'UNKNOWN');
    const status = isDown ? 'UNAVAILABLE' : (isDegraded ? 'DEGRADED' : (isUnknown ? 'UNKNOWN' : 'HEALTHY'));

    const reportedInstances = subs.reduce(
      (total, subsystem) => total + (subsystem.live?.state !== 'UNKNOWN' ? (subsystem.live?.instanceCount || 0) : 0),
      0
    );
    const hasLiveSubsystems = subs.some(
      subsystem => subsystem.live != null && subsystem.live.state !== 'UNKNOWN'
    );
    let activeComponents = hasLiveSubsystems
      ? `${reportedInstances.toLocaleString()} reported instance${reportedInstances === 1 ? '' : 's'}`
      : 'No live telemetry';
    let throughput = 'No live telemetry';

    switch (area.id) {
      case 'integration-transport':
        throughput = displayRate(
          queueStore.queues.reduce((acc, queue) => acc + (queue.enqueueRate || 0), 0),
          hasQueueTelemetry.value,
          'msg/s'
        );
        break;
      case 'execution-processing':
        activeComponents = hasWorkflowTelemetry.value
          ? `${workflowStore.totalWorkflows.toLocaleString()} reported Praxis sequence${workflowStore.totalWorkflows === 1 ? '' : 's'}`
          : 'No live telemetry';
        throughput = displayRate(workflowStore.totalProcessingRate, hasWorkflowTelemetry.value, 'tasks/s');
        break;
    }

    return {
      id: area.id,
      area,
      subsystems: subs,
      status,
      activeComponents,
      throughput
    };
  });
});
</script>

<template>
  <div class="overview-view space-y-4 font-sans">
    <!-- Top Action Toolbar -->
    <IrisToolbar
      :show-search="false"
      :show-refresh="true"
      :refreshing="operationsStore.refreshing"
      @refresh="refreshAll"
    >
      <template #left>
        <div class="flex items-center gap-2">
          <div class="p-1.5 rounded bg-sky-50 text-sky-700 border border-sky-100">
            <LayoutDashboard :size="16" />
          </div>
          <span class="text-xs font-bold text-slate-900 uppercase tracking-wider">Platform Operational Overview</span>
        </div>
      </template>

      <template #right>
        <div class="flex items-center gap-3">
          <div class="text-[11px] font-mono text-slate-500 hidden sm:flex items-center gap-1">
            <Clock :size="12" class="text-slate-400" />
            <span>Last refreshed: {{ lastRefreshedText }}</span>
          </div>
          <button 
            type="button"
            class="inline-flex items-center gap-1.5 px-3 py-1 rounded-md bg-white hover:bg-slate-50 text-slate-700 hover:text-slate-900 border border-slate-300 text-xs font-medium transition cursor-pointer shadow-xs disabled:opacity-50"
            :disabled="operationsStore.refreshing"
            @click="refreshAll"
          >
            <RotateCw :size="13" :class="{ 'animate-spin text-sky-600': operationsStore.refreshing }" />
            <span>{{ operationsStore.refreshing ? 'Refreshing...' : 'Refresh' }}</span>
          </button>
        </div>
      </template>
    </IrisToolbar>

    <!-- Platform Operational Status Strip -->
    <div class="bg-white border border-[var(--iris-border-default)] rounded-[var(--iris-border-radius)] p-3.5 shadow-subtle flex flex-wrap items-center justify-between gap-4">
      <div class="flex items-center gap-4 flex-wrap">
        <!-- Platform Status -->
        <div class="flex items-center gap-2.5 pr-4 border-r border-slate-200">
          <span class="text-xs font-bold text-slate-500 uppercase tracking-wider">PLATFORM STATUS:</span>
          <IrisStatus :status="platformStatus" size="md" :show-pulse="true" label-format="upper" />
        </div>

        <!-- Metric Pills -->
        <div class="flex items-center gap-3 flex-wrap text-xs font-mono">
          <!-- Subsystems -->
          <div class="flex items-center gap-1.5 text-slate-700">
            <span class="text-slate-400 font-sans">Subsystems:</span>
            <span class="font-bold text-slate-900">{{ healthySubsystems ?? '—' }}/{{ totalSubsystems ?? '—' }} Up</span>
            <span v-if="degradedSubsystems != null && degradedSubsystems > 0" class="text-amber-700 font-bold ml-1 font-sans">
              ({{ degradedSubsystems }} Degraded)
            </span>
          </div>

          <span class="text-slate-300">&bull;</span>

          <!-- Active Queues -->
          <div class="flex items-center gap-1.5 text-slate-700">
            <span class="text-slate-400 font-sans">Active Queues:</span>
            <span class="font-bold text-slate-900">{{ queueCountText }}</span>
          </div>

          <span class="text-slate-300">&bull;</span>

          <!-- Executing Tasks -->
          <div class="flex items-center gap-1.5 text-slate-700">
            <span class="text-slate-400 font-sans">Executing Tasks:</span>
            <span class="font-bold text-slate-900">{{ activeExecutionText }}</span>
          </div>

          <span class="text-slate-300">&bull;</span>

          <!-- Alerts -->
          <div class="flex items-center gap-1.5">
            <span class="text-slate-400 font-sans">Alerts:</span>
            <span 
              class="font-bold"
              :class="totalAlerts > 0 ? (criticalAlerts > 0 ? 'text-rose-700' : 'text-amber-700') : 'text-slate-900'"
            >
              {{ totalAlerts }}
            </span>
          </div>
        </div>
      </div>

      <!-- Quick Link to Subsystems -->
      <router-link
        to="/subsystems"
        class="inline-flex items-center gap-1 text-xs font-semibold text-sky-700 hover:text-sky-900 hover:underline cursor-pointer"
      >
        <span>Explore Subsystems</span>
        <ArrowRight :size="13" />
      </router-link>
    </div>

    <!-- The 3 Core Operational Perspectives / Triage Sections -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-4" aria-label="Operational Triage">
      <!-- 1. Is Harmonia healthy? -->
      <IrisSection title="Is Harmonia healthy?">
        <template #actions>
          <IrisStatus :status="platformStatus" size="sm" />
        </template>

        <div class="space-y-3">
          <p class="text-xs text-slate-600 leading-relaxed min-h-[3rem]">
            {{ platformStatus === 'UNKNOWN'
              ? 'Live subsystem health telemetry is not available.'
              : degradedSubsystems === 0
                ? `All ${totalSubsystems ?? 'known'} authoritative subsystems are operating nominally.`
                : `${degradedSubsystems} subsystem(s) currently degraded.` }}
          </p>

          <div class="pt-2.5 border-t border-slate-100 flex items-center justify-between">
            <span class="text-xs font-mono text-slate-600">
              {{ healthySubsystems ?? '—' }} of {{ totalSubsystems ?? '—' }} Healthy
            </span>
            <router-link 
              to="/subsystems" 
              class="text-xs font-semibold text-sky-700 hover:text-sky-900 inline-flex items-center gap-1"
            >
              <span>Subsystems</span>
              <ArrowRight :size="12" />
            </router-link>
          </div>
        </div>
      </IrisSection>

      <!-- 2. Is work moving? -->
      <IrisSection title="Is work moving?">
        <template #actions>
          <span class="text-xs font-mono font-bold text-emerald-700">{{ completedWorkText }} Completed</span>
        </template>

        <div class="space-y-3">
          <p class="text-xs text-slate-600 leading-relaxed min-h-[3rem]">
            Energeia task engine telemetry: {{ workflowRateText }}.
          </p>

          <div class="pt-2.5 border-t border-slate-100 flex items-center justify-between">
            <span class="text-xs font-mono text-slate-600">
              {{ activeExecutionText }} Active &bull; {{ queuedWorkText }} Queued
            </span>
            <router-link 
              to="/work" 
              class="text-xs font-semibold text-sky-700 hover:text-sky-900 inline-flex items-center gap-1"
            >
              <span>Work (Praxis)</span>
              <ArrowRight :size="12" />
            </router-link>
          </div>
        </div>
      </IrisSection>

      <!-- 3. Are messages backing up? -->
      <IrisSection title="Are messages backing up?">
        <template #actions>
          <span 
            class="text-xs font-mono font-bold"
            :class="queueStore.totalDlqDepth > 0 ? 'text-rose-700' : 'text-slate-600'"
          >
            {{ dlqDepthText }} DLQ
          </span>
        </template>

        <div class="space-y-3">
          <p class="text-xs text-slate-600 leading-relaxed min-h-[3rem]">
            Petasos Artemis broker queues report {{ queueDepthText }} messages across {{ queueCountText }} addresses.
          </p>

          <div class="pt-2.5 border-t border-slate-100 flex items-center justify-between">
            <span class="text-xs font-mono text-slate-600">
              {{ queueDepthText }} In Flight &bull; {{ consumerCountText }} Consumers
            </span>
            <router-link 
              to="/messages" 
              class="text-xs font-semibold text-sky-700 hover:text-sky-900 inline-flex items-center gap-1"
            >
              <span>Messages (Petasos)</span>
              <ArrowRight :size="12" />
            </router-link>
          </div>
        </div>
      </IrisSection>
    </div>

    <!-- Operational Exceptions / Alerts Summary Section -->
    <IrisSection title="Recent Operational Exceptions / Alerts">
      <template #actions>
        <router-link to="/alerts" class="text-xs text-sky-700 hover:text-sky-900 hover:underline font-semibold">
          View All Alerts ({{ totalAlerts }})
        </router-link>
      </template>

      <!-- If alerts exist -->
      <div v-if="operationsStore.alerts.length > 0" class="space-y-2">
        <div
          v-for="alert in operationsStore.alerts.slice(0, 3)"
          :key="alert.alertId"
          class="p-2.5 rounded-md border flex items-center justify-between text-xs transition"
          :class="alert.severity === 'CRITICAL' ? 'bg-rose-50 border-rose-200 text-rose-900' : 'bg-amber-50 border-amber-200 text-amber-900'"
        >
          <div class="flex items-center gap-2.5 min-w-0">
            <AlertOctagon v-if="alert.severity === 'CRITICAL'" :size="15" class="text-rose-600 shrink-0" />
            <AlertTriangle v-else :size="15" class="text-amber-600 shrink-0" />
            <span class="font-bold uppercase tracking-wider text-[10px] px-1.5 py-0.2 rounded" :class="alert.severity === 'CRITICAL' ? 'bg-rose-100 text-rose-800' : 'bg-amber-100 text-amber-800'">
              {{ alert.severity }}
            </span>
            <span class="font-semibold truncate">{{ alert.condition }}</span>
            <span class="text-slate-500 font-mono text-[11px] hidden sm:inline">&bull; {{ alert.subsystem }}</span>
          </div>

          <router-link to="/alerts" class="text-xs font-semibold underline shrink-0 ml-3">
            Inspect
          </router-link>
        </div>
      </div>

      <!-- No alerts detected -->
      <div v-else class="p-3 rounded-md bg-emerald-50/70 border border-emerald-200 text-xs text-emerald-900 flex items-center gap-2.5">
        <CheckCircle2 :size="16" class="text-emerald-700 shrink-0" />
        <span>All systems operating normally. Zero active critical or warning conditions detected in last 24h.</span>
      </div>
    </IrisSection>

    <!-- Subsystem Summary by Architectural Area Table Section -->
    <IrisSection
      title="Subsystem Summary by Architectural Area"
      description="6 Authoritative Areas across the Harmonia Health Integration Environment"
    >
      <template #actions>
        <span class="text-xs text-slate-500 font-mono">6 Authoritative Areas</span>
      </template>

      <IrisDataTable
        :value="areaSummaries"
        :columns="areaColumns"
        data-key="id"
        aria-label="Subsystem Summary by Area"
      >
        <!-- Area Name & Subtitle -->
        <template #area="{ data }">
          <div>
            <div class="font-bold text-slate-900">{{ data.area.name }}</div>
            <div class="text-[11px] text-slate-500 font-normal">{{ data.area.englishTitle }}</div>
          </div>
        </template>

        <!-- Subsystems Included with Live Status Pills -->
        <template #subsystems="{ data }">
          <div class="flex items-center gap-1.5 flex-wrap">
            <router-link
              v-for="sub in data.subsystems"
              :key="sub.id"
              :to="`/subsystems/${sub.id}`"
              class="inline-flex items-center gap-1 px-2 py-0.5 rounded text-[11px] font-mono font-medium transition"
              :class="sub.status === 'HEALTHY' 
                ? 'bg-slate-100 hover:bg-slate-200 text-slate-800 border border-slate-200' 
                : 'bg-amber-100 hover:bg-amber-200 text-amber-900 border border-amber-300 font-bold'"
              :title="`Inspect ${sub.name}`"
            >
              <span>{{ sub.name }}</span>
              <span 
                class="w-1.5 h-1.5 rounded-full" 
                :class="sub.status === 'HEALTHY' ? 'bg-emerald-500' : 'bg-amber-500'"
              ></span>
            </router-link>
          </div>
        </template>

        <!-- Status -->
        <template #status="{ data }">
          <IrisStatus :status="data.status" size="sm" />
        </template>

        <!-- Active Components -->
        <template #activeComponents="{ data }">
          <span class="text-slate-600 font-sans">{{ data.activeComponents }}</span>
        </template>

        <!-- Throughput / Rates -->
        <template #throughput="{ data }">
          <span class="font-mono font-semibold text-slate-800 whitespace-nowrap">{{ data.throughput }}</span>
        </template>
      </IrisDataTable>
    </IrisSection>
  </div>
</template>

<style scoped>
.overview-view {
  width: 100%;
}
</style>
