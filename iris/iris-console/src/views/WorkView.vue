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
import { onMounted, onUnmounted, ref, computed } from 'vue';
import { useOperationsStore } from '../stores/operationsStore';
import { useWorkflowStore } from '../stores/workflowStore';
import WorkflowTable from '../components/workflows/WorkflowTable.vue';
import PragmaDetailDrawer from '../components/workflows/PragmaDetailDrawer.vue';
import { IrisSubsystemIdentity, IrisStatus, IrisToolbar } from '@harmonia/iris-befe';
import { 
  GitMerge, 
  Layers, 
  Activity, 
  Clock, 
  AlertTriangle, 
  Search, 
  ChevronRight,
  ArrowLeft,
  ArrowRight,
  Inbox,
  Cpu
} from 'lucide-vue-next';

const workflowStore = useWorkflowStore();
const operationsStore = useOperationsStore();
let refreshTimer: any = null;

const pragmaSearch = ref('');
const energeiaSubsystem = computed(() => operationsStore.subsystems.find(subsystem => subsystem.id === 'energeia'));
const energeiaStatus = computed(() => energeiaSubsystem.value?.state || 'UNKNOWN');
const energeiaStale = computed(() => Boolean(energeiaSubsystem.value?.stale));

onMounted(async () => {
  await Promise.all([workflowStore.fetchWorkflows(), operationsStore.fetchSubsystems()]);
  refreshTimer = setInterval(() => {
    workflowStore.fetchWorkflows(true);
  }, 10000);
});

onUnmounted(() => {
  if (refreshTimer) {
    clearInterval(refreshTimer);
  }
});

function formatDuration(ms?: number | null): string {
  if (ms == null || isNaN(ms)) return 'N/A';
  if (ms < 1000) return `${ms}ms`;
  return `${(ms / 1000).toFixed(2)}s`;
}

function formatRate(rate?: number | null): string {
  if (rate == null || isNaN(rate)) return 'N/A';
  return `${rate.toFixed(1)} /s`;
}

function formatCount(val?: number | null): string {
  if (val == null || isNaN(val)) return 'N/A';
  return val.toLocaleString();
}

function formatTime(timestamp?: number | null): string {
  if (!timestamp) return 'N/A';
  return new Date(timestamp).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', second: '2-digit' });
}

const filteredPragmas = computed(() => {
  const list = workflowStore.currentWorkflowPragmas;
  if (!pragmaSearch.value.trim()) return list;
  const q = pragmaSearch.value.toLowerCase().trim();
  return list.filter(p => 
    (p.pragmaId && p.pragmaId.toLowerCase().includes(q)) ||
    (p.correlationId && p.correlationId.toLowerCase().includes(q)) ||
    (p.currentErgon && p.currentErgon.toLowerCase().includes(q)) ||
    (p.status && p.status.toLowerCase().includes(q))
  );
});
</script>

<template>
  <div class="work-view space-y-5 font-sans">
    <!-- Subsystem Identity & Energeia Model Header -->
    <IrisSubsystemIdentity 
      name="Energeia" 
      description="Ponos task workers, Ergon activity units, stateful Praxis sequence orchestration, and Pragma runtime checkpoint progression."
      :status="energeiaStatus"
      :stale="energeiaStale"
    >
      <template #icon>
        <GitMerge :size="22" class="text-sky-700" />
      </template>
      <template #badges>
        <span class="px-2 py-0.5 rounded text-xs font-semibold bg-slate-100 text-slate-700 border border-slate-200">
          Workflow Orchestration &amp; Task Execution
        </span>
        <span class="px-2 py-0.5 rounded text-xs font-mono font-semibold bg-sky-50 text-sky-800 border border-sky-200">
          Ponos + Praxis
        </span>
        <span v-if="workflowStore.isStale" class="inline-flex items-center gap-1 px-2 py-0.5 rounded text-[11px] font-mono font-medium bg-amber-50 text-amber-800 border border-amber-300">
          <Clock :size="12" />
          STALE TELEMETRY
        </span>
      </template>
      <template #actions>
        <!-- Execution Subsystems Pill -->
        <div class="p-2.5 rounded-md bg-slate-50 border border-slate-200 text-xs text-slate-700 flex items-center gap-3 shrink-0">
          <div class="p-1.5 rounded bg-sky-50 text-sky-700 border border-sky-100">
            <Cpu :size="16" />
          </div>
          <div>
            <div class="flex items-center gap-2">
              <span class="text-[10px] font-bold text-slate-500 uppercase tracking-wider">Engine Subsystems</span>
              <span class="px-1.5 py-0.2 rounded text-[10px] font-mono bg-sky-100 text-sky-800 font-semibold">Ponos + Praxis</span>
            </div>
            <div class="font-mono text-slate-900 font-bold text-xs mt-0.5">
              4 Worker Pools &bull; Erga Activity Pipeline
            </div>
          </div>
        </div>
      </template>
    </IrisSubsystemIdentity>

    <!-- Summary Metrics Cards: Answering "Is work moving?" -->
    <div class="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-7 gap-3">
      <!-- Active Praxis -->
      <div class="p-3.5 bg-white border border-slate-200 rounded-lg shadow-xs flex flex-col justify-between">
        <span class="text-[11px] font-bold text-slate-500 uppercase tracking-wider block">Active</span>
        <div 
          class="text-2xl font-extrabold font-mono mt-1"
          :class="workflowStore.totalActiveExecutions > 0 ? 'text-sky-700' : 'text-slate-800'"
        >
          {{ formatCount(workflowStore.totalActiveExecutions) }}
        </div>
        <span class="text-[10px] text-slate-500 font-sans mt-1">Running sequences</span>
      </div>

      <!-- Queued Work -->
      <div class="p-3.5 bg-white border border-slate-200 rounded-lg shadow-xs flex flex-col justify-between">
        <span class="text-[11px] font-bold text-slate-500 uppercase tracking-wider block">Queued</span>
        <div 
          class="text-2xl font-extrabold font-mono mt-1"
          :class="workflowStore.totalQueuedWork > 0 ? 'text-amber-700' : 'text-slate-500'"
        >
          {{ formatCount(workflowStore.totalQueuedWork) }}
        </div>
        <span class="text-[10px] text-slate-500 font-sans mt-1">Pending initiation</span>
      </div>

      <!-- Completed Work -->
      <div class="p-3.5 bg-white border border-slate-200 rounded-lg shadow-xs flex flex-col justify-between">
        <span class="text-[11px] font-bold text-slate-500 uppercase tracking-wider block">Completed</span>
        <div class="text-2xl font-extrabold font-mono mt-1 text-emerald-700">
          {{ formatCount(workflowStore.totalCompletedWork) }}
        </div>
        <span class="text-[10px] text-slate-500 font-sans mt-1">Successful executions</span>
      </div>

      <!-- Failed Work -->
      <div class="p-3.5 bg-white border border-slate-200 rounded-lg shadow-xs flex flex-col justify-between">
        <span class="text-[11px] font-bold text-slate-500 uppercase tracking-wider block">Failed</span>
        <div 
          class="text-2xl font-extrabold font-mono mt-1"
          :class="workflowStore.totalFailedWork > 0 ? 'text-rose-700' : 'text-slate-400'"
        >
          {{ formatCount(workflowStore.totalFailedWork) }}
        </div>
        <span class="text-[10px] text-slate-500 font-sans mt-1">Unrecovered errors</span>
      </div>

      <!-- Retrying Work -->
      <div class="p-3.5 bg-white border border-slate-200 rounded-lg shadow-xs flex flex-col justify-between">
        <span class="text-[11px] font-bold text-slate-500 uppercase tracking-wider block">Retrying</span>
        <div 
          class="text-2xl font-extrabold font-mono mt-1"
          :class="workflowStore.totalRetryingWork > 0 ? 'text-amber-700' : 'text-slate-400'"
        >
          {{ formatCount(workflowStore.totalRetryingWork) }}
        </div>
        <span class="text-[10px] text-slate-500 font-sans mt-1">Transient retry state</span>
      </div>

      <!-- Processing Rate -->
      <div class="p-3.5 bg-white border border-slate-200 rounded-lg shadow-xs flex flex-col justify-between">
        <span class="text-[11px] font-bold text-slate-500 uppercase tracking-wider block">Throughput</span>
        <div class="text-xl font-bold font-mono mt-1 text-sky-700">
          {{ formatRate(workflowStore.totalProcessingRate) }}
        </div>
        <span class="text-[10px] text-slate-500 font-sans mt-1">Workflows per second</span>
      </div>

      <!-- P95 Duration -->
      <div class="p-3.5 bg-white border border-slate-200 rounded-lg shadow-xs flex flex-col justify-between">
        <span class="text-[11px] font-bold text-slate-500 uppercase tracking-wider block">P95 Duration</span>
        <div class="text-xl font-bold font-mono mt-1 text-slate-800">
          {{ formatDuration(workflowStore.p95Duration) }}
        </div>
        <span class="text-[10px] text-slate-500 font-sans mt-1">95th percentile</span>
      </div>
    </div>

    <!-- Error Warning -->
    <div v-if="workflowStore.error" class="p-3 rounded-lg bg-rose-50 border border-rose-200 text-xs text-rose-800 flex items-center justify-between">
      <div class="flex items-center gap-2">
        <AlertTriangle :size="16" class="text-rose-600 shrink-0" />
        <span>{{ workflowStore.error }}</span>
      </div>
      <button 
        type="button" 
        class="text-xs font-bold underline hover:text-rose-900 ml-4 cursor-pointer"
        @click="workflowStore.fetchWorkflows()"
      >
        Retry
      </button>
    </div>

    <!-- VIEW LEVEL 1: All Workflows Table -->
    <div v-if="!workflowStore.selectedWorkflow" class="space-y-4">
      <!-- Standardized Toolbar -->
      <IrisToolbar 
        :show-search="false"
        :show-refresh="true"
        :refreshing="workflowStore.loading || workflowStore.refreshing"
        @refresh="workflowStore.fetchWorkflows()"
      >
        <template #left>
          <div class="relative w-full sm:w-96">
            <Search :size="14" class="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
            <input 
              type="text" 
              placeholder="Search workflows by name, ID, or description..." 
              class="w-full bg-slate-50 border border-slate-300 rounded-md pl-9 pr-3 py-1.5 text-xs text-slate-900 placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-sky-500/20 focus:border-sky-500 font-sans"
              :value="workflowStore.searchQuery"
              @input="workflowStore.setSearchQuery(($event.target as HTMLInputElement).value)"
              aria-label="Search workflows input"
            />
          </div>
        </template>

        <template #right>
          <span class="text-xs font-mono text-slate-500 hidden sm:inline mr-3">
            Showing {{ workflowStore.filteredWorkflows.length }} of {{ workflowStore.totalWorkflows }} Sequences
          </span>
        </template>
      </IrisToolbar>

      <WorkflowTable 
        :workflows="workflowStore.filteredWorkflows" 
        :loading="workflowStore.loading"
        @select="workflowStore.selectWorkflow($event.workflowId)"
      />
    </div>

    <!-- VIEW LEVEL 2: Workflow Drill-Down (Praxis -> Pragmas) -->
    <div v-else class="space-y-5">
      <!-- Breadcrumb Navigation -->
      <div class="flex items-center gap-2 text-xs font-mono text-slate-500">
        <button 
          type="button" 
          class="hover:text-slate-900 flex items-center gap-1 font-semibold text-sky-700 hover:underline cursor-pointer"
          @click="workflowStore.clearSelectedWorkflow()"
        >
          <ArrowLeft :size="14" />
          <span>All Workflows</span>
        </button>
        <ChevronRight :size="14" class="text-slate-400" />
        <span class="text-slate-900 font-bold truncate">{{ workflowStore.selectedWorkflow.name }}</span>
      </div>

      <!-- Selected Workflow Header Card -->
      <div class="bg-white border border-slate-200 rounded-lg p-5 shadow-xs space-y-4">
        <div class="flex flex-col md:flex-row md:items-center justify-between gap-4">
          <div>
            <div class="flex items-center gap-2.5">
              <div class="p-1.5 rounded bg-sky-50 text-sky-700 border border-sky-100">
                <GitMerge :size="18" />
              </div>
              <h2 class="text-lg font-extrabold text-slate-900 tracking-tight">
                {{ workflowStore.selectedWorkflow.name }}
              </h2>
            </div>
            <p class="text-xs text-slate-500 mt-1 max-w-2xl font-sans">
              {{ workflowStore.selectedWorkflow.description || 'Configured Praxis workflow orchestration pipeline.' }}
            </p>
            <div class="flex items-center gap-2 mt-2">
              <span class="text-[11px] font-mono px-2 py-0.5 rounded bg-slate-100 text-sky-800 border border-slate-200 font-semibold">
                ID: {{ workflowStore.selectedWorkflow.workflowId }}
              </span>
              <span class="text-[11px] font-mono px-2 py-0.5 rounded bg-purple-50 text-purple-700 border border-purple-200 font-semibold">
                Praxis Definition
              </span>
            </div>
          </div>

          <button 
            type="button" 
            class="inline-flex items-center gap-1 px-3 py-1.5 rounded bg-slate-100 hover:bg-slate-200 text-slate-700 hover:text-slate-900 border border-slate-300 text-xs font-medium transition cursor-pointer self-start md:self-auto"
            @click="workflowStore.clearSelectedWorkflow()"
          >
            <span>Back to All Workflows</span>
          </button>
        </div>

        <div class="grid grid-cols-2 sm:grid-cols-4 lg:grid-cols-6 gap-3 pt-3 border-t border-slate-100">
          <div>
            <span class="text-[10px] uppercase font-bold text-slate-500 block">Active Work</span>
            <span class="text-base font-bold font-mono text-sky-700">{{ workflowStore.selectedWorkflow.activeExecutions }}</span>
          </div>
          <div>
            <span class="text-[10px] uppercase font-bold text-slate-500 block">Queued</span>
            <span class="text-base font-bold font-mono text-slate-700">{{ workflowStore.selectedWorkflow.queuedWork }}</span>
          </div>
          <div>
            <span class="text-[10px] uppercase font-bold text-slate-500 block">Completed</span>
            <span class="text-base font-bold font-mono text-emerald-700">{{ workflowStore.selectedWorkflow.completedWork }}</span>
          </div>
          <div>
            <span class="text-[10px] uppercase font-bold text-slate-500 block">Failed</span>
            <span class="text-base font-bold font-mono text-rose-700">{{ workflowStore.selectedWorkflow.failedWork }}</span>
          </div>
          <div>
            <span class="text-[10px] uppercase font-bold text-slate-500 block">Throughput</span>
            <span class="text-base font-bold font-mono text-slate-800">{{ formatRate(workflowStore.selectedWorkflow.processingRate) }}</span>
          </div>
          <div>
            <span class="text-[10px] uppercase font-bold text-slate-500 block">P95 Duration</span>
            <span class="text-base font-bold font-mono text-slate-800">{{ formatDuration(workflowStore.selectedWorkflow.p95DurationMs) }}</span>
          </div>
        </div>
      </div>

      <!-- Pragmas Drill-Down Section -->
      <div class="bg-white border border-slate-200 rounded-lg overflow-hidden shadow-xs">
        <div class="px-5 py-3.5 bg-slate-50/70 border-b border-slate-200 flex flex-col sm:flex-row items-start sm:items-center justify-between gap-3">
          <div class="flex items-center gap-2.5">
            <div class="p-1.5 rounded bg-sky-50 text-sky-700 border border-sky-100">
              <Layers :size="15" />
            </div>
            <h3 class="text-xs font-bold text-slate-800 tracking-wider uppercase">
              Pragma Execution Envelopes
            </h3>
            <span class="text-xs font-mono px-2 py-0.5 rounded bg-slate-100 text-slate-700 font-semibold border border-slate-200">
              {{ filteredPragmas.length }}
            </span>
          </div>

          <!-- Pragma Search Input -->
          <div class="relative w-full sm:w-64">
            <Search :size="13" class="absolute left-2.5 top-1/2 -translate-y-1/2 text-slate-400" />
            <input 
              v-model="pragmaSearch"
              type="text" 
              placeholder="Filter Pragmas by ID, correlation..." 
              class="w-full bg-white border border-slate-300 rounded-md pl-8 pr-2.5 py-1 text-xs text-slate-900 placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-sky-500/20 focus:border-sky-500 font-sans"
            />
          </div>
        </div>

        <!-- Empty Pragmas State -->
        <div v-if="filteredPragmas.length === 0" class="p-12 text-center space-y-3">
          <Inbox :size="36" class="mx-auto text-slate-400" />
          <p class="text-sm font-semibold text-slate-700">No Pragma envelopes found</p>
          <p class="text-xs text-slate-500 max-w-sm mx-auto font-sans">
            No active or recent Pragma envelopes match the query for this workflow sequence.
          </p>
        </div>

        <!-- Pragmas Table -->
        <div v-else class="overflow-x-auto">
          <table class="w-full text-left text-xs border-collapse" role="table" aria-label="Pragma executions">
            <thead>
              <tr class="bg-slate-50 text-slate-600 border-b border-slate-200 text-[11px] font-bold tracking-wider uppercase select-none">
                <th scope="col" class="py-2.5 px-4">Status</th>
                <th scope="col" class="py-2.5 px-4">Pragma ID</th>
                <th scope="col" class="py-2.5 px-3">Started</th>
                <th scope="col" class="py-2.5 px-3">Duration</th>
                <th scope="col" class="py-2.5 px-3">Current Ergon</th>
                <th scope="col" class="py-2.5 px-3 text-right">Completed Erga</th>
                <th scope="col" class="py-2.5 px-3 text-right">Retries</th>
                <th scope="col" class="py-2.5 px-3">Correlation ID</th>
                <th scope="col" class="py-2.5 px-4 text-center">Action</th>
              </tr>
            </thead>
            <tbody class="divide-y divide-slate-100 text-xs font-mono text-slate-700">
              <tr 
                v-for="p in filteredPragmas" 
                :key="p.pragmaId"
                class="hover:bg-sky-50/50 transition cursor-pointer group"
                @click="workflowStore.openPragmaDrawer(p)"
              >
                <!-- Status -->
                <td class="py-2.5 px-4 whitespace-nowrap">
                  <IrisStatus :status="p.status || 'UNKNOWN'" size="sm" label-format="upper" />
                </td>

                <!-- Pragma ID -->
                <td class="py-2.5 px-4 whitespace-nowrap font-bold text-sky-700 group-hover:text-sky-900">
                  {{ p.pragmaId }}
                </td>

                <!-- Started -->
                <td class="py-2.5 px-3 whitespace-nowrap text-slate-700">
                  {{ formatTime(p.startedAt) }}
                </td>

                <!-- Duration -->
                <td class="py-2.5 px-3 whitespace-nowrap text-slate-700">
                  {{ formatDuration(p.durationMs) }}
                </td>

                <!-- Current Ergon -->
                <td class="py-2.5 px-3 whitespace-nowrap text-slate-800">
                  <span class="truncate max-w-[140px] block font-sans" :title="p.currentErgon || 'None'">
                    {{ p.currentErgon || 'None' }}
                  </span>
                </td>

                <!-- Completed Erga -->
                <td class="py-2.5 px-3 text-right whitespace-nowrap text-emerald-700 font-semibold">
                  {{ p.completedErgaCount }}
                </td>

                <!-- Retries -->
                <td class="py-2.5 px-3 text-right whitespace-nowrap">
                  <span 
                    v-if="p.retryCount > 0"
                    class="px-1.5 py-0.5 rounded text-[11px] font-bold bg-amber-50 text-amber-700 border border-amber-200"
                  >
                    {{ p.retryCount }}
                  </span>
                  <span v-else class="text-slate-400">0</span>
                </td>

                <!-- Correlation ID -->
                <td class="py-2.5 px-3 whitespace-nowrap text-slate-500 max-w-[120px] truncate" :title="p.correlationId || 'N/A'">
                  {{ p.correlationId || 'N/A' }}
                </td>

                <!-- Action -->
                <td class="py-2.5 px-4 text-center whitespace-nowrap">
                  <button 
                    type="button"
                    class="inline-flex items-center gap-1 px-2.5 py-1 rounded bg-slate-100 hover:bg-slate-200 text-slate-700 hover:text-slate-900 border border-slate-300 text-xs font-medium transition cursor-pointer font-sans"
                    :aria-label="`Inspect Pragma ${p.pragmaId}`"
                    @click.stop="workflowStore.openPragmaDrawer(p)"
                  >
                    <span>Checkpoints</span>
                    <ArrowRight :size="12" class="text-slate-400 group-hover:text-sky-600 transition" />
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <!-- Pragma Detail Drawer -->
    <PragmaDetailDrawer 
      :pragma="workflowStore.selectedPragma" 
      :is-open="workflowStore.isPragmaDrawerOpen" 
      :teleport="true"
      @close="workflowStore.closePragmaDrawer()" 
    />
  </div>
</template>
