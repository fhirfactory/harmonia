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
import { useWorkflowStore } from '../stores/workflowStore';
import WorkflowTable from '../components/workflows/WorkflowTable.vue';
import PragmaDetailDrawer from '../components/workflows/PragmaDetailDrawer.vue';
import StatusBadge from '../components/common/StatusBadge.vue';
import type { PragmaSummary } from '../models/operations';
import { 
  GitMerge, 
  Layers, 
  Activity, 
  Clock, 
  AlertTriangle, 
  Search, 
  RefreshCw, 
  ChevronRight,
  ArrowLeft,
  ArrowRight,
  CheckCircle2,
  Inbox
} from 'lucide-vue-next';

const workflowStore = useWorkflowStore();
let refreshTimer: any = null;

const pragmaSearch = ref('');

onMounted(async () => {
  await workflowStore.fetchWorkflows();
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
  <div class="space-y-6">
    <!-- View Header -->
    <div class="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4">
      <div>
        <h1 class="text-2xl font-extrabold text-white tracking-tight flex items-center gap-2.5">
          <GitMerge :size="24" class="text-sky-400" />
          <span>Workflows &amp; Praxis Perspective</span>
        </h1>
        <p class="text-xs text-slate-400 mt-1">
          Active Praxis workflow execution sequences, Pragma envelopes, and Ergon execution checkpoints.
        </p>
      </div>

      <div class="flex items-center gap-2">
        <span v-if="workflowStore.isStale" class="inline-flex items-center gap-1 px-2.5 py-1 rounded-full text-xs font-medium bg-amber-500/10 border border-amber-500/30 text-amber-400 font-mono">
          <Clock :size="12" />
          STALE TELEMETRY
        </span>
        <button 
          type="button"
          class="btn btn-secondary text-xs flex items-center gap-1.5 px-3 py-1.5"
          :disabled="workflowStore.loading || workflowStore.refreshing"
          @click="workflowStore.fetchWorkflows()"
        >
          <RefreshCw :size="13" :class="{ 'animate-spin': workflowStore.loading || workflowStore.refreshing }" />
          <span>{{ workflowStore.refreshing ? 'Refreshing...' : 'Refresh Workflows' }}</span>
        </button>
      </div>
    </div>

    <!-- Summary Metrics Cards -->
    <div class="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-7 gap-3">
      <!-- Active Praxis -->
      <div class="card p-3 border border-slate-800 bg-slate-900/60 shadow-lg">
        <span class="text-[11px] font-semibold text-slate-400 uppercase tracking-wider block">Active</span>
        <div 
          class="text-2xl font-extrabold font-mono mt-1"
          :class="workflowStore.totalActiveExecutions > 0 ? 'text-sky-400' : 'text-slate-300'"
        >
          {{ formatCount(workflowStore.totalActiveExecutions) }}
        </div>
        <span class="text-[10px] text-slate-500">Running sequences</span>
      </div>

      <!-- Queued Work -->
      <div class="card p-3 border border-slate-800 bg-slate-900/60 shadow-lg">
        <span class="text-[11px] font-semibold text-slate-400 uppercase tracking-wider block">Queued</span>
        <div 
          class="text-2xl font-extrabold font-mono mt-1"
          :class="workflowStore.totalQueuedWork > 0 ? 'text-amber-400' : 'text-slate-400'"
        >
          {{ formatCount(workflowStore.totalQueuedWork) }}
        </div>
        <span class="text-[10px] text-slate-500">Pending initiation</span>
      </div>

      <!-- Completed Work -->
      <div class="card p-3 border border-slate-800 bg-slate-900/60 shadow-lg">
        <span class="text-[11px] font-semibold text-slate-400 uppercase tracking-wider block">Completed</span>
        <div class="text-2xl font-extrabold font-mono mt-1 text-emerald-400">
          {{ formatCount(workflowStore.totalCompletedWork) }}
        </div>
        <span class="text-[10px] text-slate-500">Successful executions</span>
      </div>

      <!-- Failed Work -->
      <div class="card p-3 border border-slate-800 bg-slate-900/60 shadow-lg">
        <span class="text-[11px] font-semibold text-slate-400 uppercase tracking-wider block">Failed</span>
        <div 
          class="text-2xl font-extrabold font-mono mt-1"
          :class="workflowStore.totalFailedWork > 0 ? 'text-rose-400' : 'text-slate-500'"
        >
          {{ formatCount(workflowStore.totalFailedWork) }}
        </div>
        <span class="text-[10px] text-slate-500">Unrecovered errors</span>
      </div>

      <!-- Retrying Work -->
      <div class="card p-3 border border-slate-800 bg-slate-900/60 shadow-lg">
        <span class="text-[11px] font-semibold text-slate-400 uppercase tracking-wider block">Retrying</span>
        <div 
          class="text-2xl font-extrabold font-mono mt-1"
          :class="workflowStore.totalRetryingWork > 0 ? 'text-amber-400' : 'text-slate-500'"
        >
          {{ formatCount(workflowStore.totalRetryingWork) }}
        </div>
        <span class="text-[10px] text-slate-500">Transient retry state</span>
      </div>

      <!-- Processing Rate -->
      <div class="card p-3 border border-slate-800 bg-slate-900/60 shadow-lg">
        <span class="text-[11px] font-semibold text-slate-400 uppercase tracking-wider block">Throughput</span>
        <div class="text-xl font-bold font-mono mt-1 text-sky-400">
          {{ formatRate(workflowStore.totalProcessingRate) }}
        </div>
        <span class="text-[10px] text-slate-500">Workflows per second</span>
      </div>

      <!-- P95 Duration -->
      <div class="card p-3 border border-slate-800 bg-slate-900/60 shadow-lg">
        <span class="text-[11px] font-semibold text-slate-400 uppercase tracking-wider block">P95 Duration</span>
        <div class="text-xl font-bold font-mono mt-1 text-slate-200">
          {{ formatDuration(workflowStore.p95Duration) }}
        </div>
        <span class="text-[10px] text-slate-500">95th percentile</span>
      </div>
    </div>

    <!-- Error Warning -->
    <div v-if="workflowStore.error" class="p-3 rounded-lg bg-rose-500/10 border border-rose-500/30 text-xs text-rose-400 flex items-center justify-between">
      <div class="flex items-center gap-2">
        <AlertTriangle :size="16" />
        <span>{{ workflowStore.error }}</span>
      </div>
      <button 
        type="button" 
        class="text-xs font-bold underline hover:text-rose-300 ml-4"
        @click="workflowStore.fetchWorkflows()"
      >
        Retry
      </button>
    </div>

    <!-- VIEW LEVEL 1: All Workflows Table -->
    <div v-if="!workflowStore.selectedWorkflow" class="space-y-4">
      <!-- Search Bar -->
      <div class="card p-4 border border-slate-800 bg-slate-900/60 shadow-lg flex items-center justify-between gap-3">
        <div class="relative w-full sm:w-96">
          <Search :size="15" class="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
          <input 
            type="text" 
            placeholder="Search workflows by name, ID, or description..." 
            class="w-full bg-slate-950/80 border border-slate-700/80 rounded-lg pl-9 pr-3 py-1.5 text-xs text-white placeholder-slate-500 focus:outline-none focus:border-sky-500 font-sans"
            :value="workflowStore.searchQuery"
            @input="workflowStore.setSearchQuery(($event.target as HTMLInputElement).value)"
          />
        </div>
        <span class="text-xs font-mono text-slate-400 hidden sm:inline">
          Showing {{ workflowStore.filteredWorkflows.length }} of {{ workflowStore.totalWorkflows }} Sequences
        </span>
      </div>

      <WorkflowTable 
        :workflows="workflowStore.filteredWorkflows" 
        :loading="workflowStore.loading"
        @select="workflowStore.selectWorkflow($event.workflowId)"
      />
    </div>

    <!-- VIEW LEVEL 2: Workflow Drill-Down (Praxis -> Pragmas) -->
    <div v-else class="space-y-6">
      <!-- Breadcrumb Navigation -->
      <div class="flex items-center gap-2 text-xs font-mono text-slate-400">
        <button 
          type="button" 
          class="hover:text-white flex items-center gap-1 font-semibold text-sky-400 hover:underline"
          @click="workflowStore.clearSelectedWorkflow()"
        >
          <ArrowLeft :size="14" />
          <span>All Workflows</span>
        </button>
        <ChevronRight :size="14" class="text-slate-600" />
        <span class="text-slate-200 font-bold truncate">{{ workflowStore.selectedWorkflow.name }}</span>
      </div>

      <!-- Selected Workflow Header Card -->
      <div class="card p-6 border border-slate-800 bg-slate-900/70 shadow-xl space-y-4">
        <div class="flex flex-col md:flex-row md:items-center justify-between gap-4">
          <div>
            <div class="flex items-center gap-2.5">
              <GitMerge :size="22" class="text-sky-400" />
              <h2 class="text-xl font-extrabold text-white tracking-tight">
                {{ workflowStore.selectedWorkflow.name }}
              </h2>
            </div>
            <p class="text-xs text-slate-400 mt-1 max-w-2xl font-sans">
              {{ workflowStore.selectedWorkflow.description || 'Configured Praxis workflow orchestration pipeline.' }}
            </p>
            <div class="flex items-center gap-2 mt-2">
              <span class="text-[11px] font-mono px-2 py-0.5 rounded bg-slate-800 text-sky-300 border border-slate-700">
                ID: {{ workflowStore.selectedWorkflow.workflowId }}
              </span>
              <span class="text-[11px] font-mono px-2 py-0.5 rounded bg-purple-500/10 text-purple-300 border border-purple-500/20">
                Praxis Definition
              </span>
            </div>
          </div>

          <button 
            type="button" 
            class="btn btn-secondary text-xs self-start md:self-auto"
            @click="workflowStore.clearSelectedWorkflow()"
          >
            Back to All Workflows
          </button>
        </div>

        <div class="grid grid-cols-2 sm:grid-cols-4 lg:grid-cols-6 gap-3 pt-2 border-t border-slate-800/80">
          <div>
            <span class="text-[10px] uppercase font-semibold text-slate-500 block">Active Work</span>
            <span class="text-base font-bold font-mono text-sky-400">{{ workflowStore.selectedWorkflow.activeExecutions }}</span>
          </div>
          <div>
            <span class="text-[10px] uppercase font-semibold text-slate-500 block">Queued</span>
            <span class="text-base font-bold font-mono text-slate-300">{{ workflowStore.selectedWorkflow.queuedWork }}</span>
          </div>
          <div>
            <span class="text-[10px] uppercase font-semibold text-slate-500 block">Completed</span>
            <span class="text-base font-bold font-mono text-emerald-400">{{ workflowStore.selectedWorkflow.completedWork }}</span>
          </div>
          <div>
            <span class="text-[10px] uppercase font-semibold text-slate-500 block">Failed</span>
            <span class="text-base font-bold font-mono text-rose-400">{{ workflowStore.selectedWorkflow.failedWork }}</span>
          </div>
          <div>
            <span class="text-[10px] uppercase font-semibold text-slate-500 block">Throughput</span>
            <span class="text-base font-bold font-mono text-slate-300">{{ formatRate(workflowStore.selectedWorkflow.processingRate) }}</span>
          </div>
          <div>
            <span class="text-[10px] uppercase font-semibold text-slate-500 block">P95 Duration</span>
            <span class="text-base font-bold font-mono text-slate-300">{{ formatDuration(workflowStore.selectedWorkflow.p95DurationMs) }}</span>
          </div>
        </div>
      </div>

      <!-- Pragmas Drill-Down Section -->
      <div class="card p-0 overflow-hidden border border-slate-800 bg-slate-900/60 shadow-xl rounded-xl">
        <div class="p-4 border-b border-slate-800/80 flex flex-col sm:flex-row items-start sm:items-center justify-between gap-3 bg-slate-950/40">
          <div class="flex items-center gap-2">
            <Layers :size="18" class="text-sky-400" />
            <h3 class="text-sm font-bold text-white tracking-wide uppercase">
              Pragma Execution Envelopes
            </h3>
            <span class="text-xs font-mono px-2 py-0.5 rounded bg-slate-800 text-slate-300 font-semibold">
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
              class="w-full bg-slate-950/80 border border-slate-700/80 rounded-md pl-8 pr-2.5 py-1 text-xs text-white placeholder-slate-500 focus:outline-none focus:border-sky-500 font-sans"
            />
          </div>
        </div>

        <!-- Empty Pragmas State -->
        <div v-if="filteredPragmas.length === 0" class="p-12 text-center space-y-3">
          <Inbox :size="36" class="mx-auto text-slate-600" />
          <p class="text-sm font-semibold text-slate-300">No Pragma envelopes found</p>
          <p class="text-xs text-slate-500 max-w-sm mx-auto font-sans">
            No active or recent Pragma envelopes match the query for this workflow sequence.
          </p>
        </div>

        <!-- Pragmas Table -->
        <div v-else class="overflow-x-auto">
          <table class="w-full text-left border-collapse" role="table" aria-label="Pragma executions">
            <thead>
              <tr class="border-b border-slate-800 text-[11px] uppercase tracking-wider text-slate-400 bg-slate-950/70 font-semibold select-none">
                <th scope="col" class="py-3 px-4">Status</th>
                <th scope="col" class="py-3 px-4">Pragma ID</th>
                <th scope="col" class="py-3 px-3">Started</th>
                <th scope="col" class="py-3 px-3">Duration</th>
                <th scope="col" class="py-3 px-3">Current Ergon</th>
                <th scope="col" class="py-3 px-3 text-right">Completed Erga</th>
                <th scope="col" class="py-3 px-3 text-right">Retries</th>
                <th scope="col" class="py-3 px-3">Correlation ID</th>
                <th scope="col" class="py-3 px-4 text-center">Action</th>
              </tr>
            </thead>
            <tbody class="divide-y divide-slate-800/60 text-xs font-mono">
              <tr 
                v-for="p in filteredPragmas" 
                :key="p.pragmaId"
                class="hover:bg-slate-800/40 transition-colors cursor-pointer group"
                @click="workflowStore.openPragmaDrawer(p)"
              >
                <!-- Status -->
                <td class="py-3 px-4 whitespace-nowrap">
                  <StatusBadge :status="p.status || 'UNKNOWN'" size="sm" />
                </td>

                <!-- Pragma ID -->
                <td class="py-3 px-4 whitespace-nowrap font-bold text-sky-400 group-hover:text-sky-300">
                  {{ p.pragmaId }}
                </td>

                <!-- Started -->
                <td class="py-3 px-3 whitespace-nowrap text-slate-300">
                  {{ formatTime(p.startedAt) }}
                </td>

                <!-- Duration -->
                <td class="py-3 px-3 whitespace-nowrap text-slate-300">
                  {{ formatDuration(p.durationMs) }}
                </td>

                <!-- Current Ergon -->
                <td class="py-3 px-3 whitespace-nowrap text-slate-200">
                  <span class="truncate max-w-[140px] block" :title="p.currentErgon || 'None'">
                    {{ p.currentErgon || 'None' }}
                  </span>
                </td>

                <!-- Completed Erga -->
                <td class="py-3 px-3 text-right whitespace-nowrap text-emerald-400 font-semibold">
                  {{ p.completedErgaCount }}
                </td>

                <!-- Retries -->
                <td class="py-3 px-3 text-right whitespace-nowrap">
                  <span 
                    v-if="p.retryCount > 0"
                    class="px-1.5 py-0.5 rounded text-[11px] font-bold bg-amber-500/20 text-amber-400 border border-amber-500/30"
                  >
                    {{ p.retryCount }}
                  </span>
                  <span v-else class="text-slate-500">0</span>
                </td>

                <!-- Correlation ID -->
                <td class="py-3 px-3 whitespace-nowrap text-slate-400 max-w-[120px] truncate" :title="p.correlationId || 'N/A'">
                  {{ p.correlationId || 'N/A' }}
                </td>

                <!-- Action -->
                <td class="py-3 px-4 text-center whitespace-nowrap">
                  <button 
                    type="button"
                    class="p-1.5 rounded-lg text-slate-400 hover:text-white hover:bg-slate-700/60 transition-colors inline-flex items-center gap-1 text-[11px] font-sans"
                    :aria-label="`Inspect Pragma ${p.pragmaId}`"
                    @click.stop="workflowStore.openPragmaDrawer(p)"
                  >
                    <span>Checkpoints</span>
                    <ArrowRight :size="13" />
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
      @close="workflowStore.closePragmaDrawer()" 
    />
  </div>
</template>
