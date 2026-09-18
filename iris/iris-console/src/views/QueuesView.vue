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
import { onMounted, onUnmounted, ref } from 'vue';
import { useQueueStore } from '../stores/queueStore';
import QueueTable from '../components/queues/QueueTable.vue';
import QueueDetailDrawer from '../components/queues/QueueDetailDrawer.vue';
import { 
  Radio, 
  Server, 
  Users, 
  Layers, 
  AlertTriangle, 
  Search, 
  RefreshCw, 
  Clock, 
  ShieldAlert,
  Inbox
} from 'lucide-vue-next';

const queueStore = useQueueStore();
let refreshTimer: any = null;

onMounted(async () => {
  await queueStore.fetchQueues();
  refreshTimer = setInterval(() => {
    queueStore.fetchQueues(true);
  }, 10000);
});

onUnmounted(() => {
  if (refreshTimer) {
    clearInterval(refreshTimer);
  }
});

const statusOptions = ['ALL', 'HEALTHY', 'DEGRADED', 'UNHEALTHY'];
</script>

<template>
  <div class="space-y-6">
    <!-- View Header -->
    <div class="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4">
      <div>
        <h1 class="text-2xl font-extrabold text-white tracking-tight flex items-center gap-2.5">
          <Radio :size="24" class="text-sky-400" />
          <span>Petasos Queues Perspective</span>
        </h1>
        <p class="text-xs text-slate-400 mt-1">
          ActiveMQ Artemis message broker topology, message flow rates, consumer bindings, and DLQ depth.
        </p>
      </div>

      <div class="flex items-center gap-2">
        <span v-if="queueStore.isStale" class="inline-flex items-center gap-1 px-2.5 py-1 rounded-full text-xs font-medium bg-amber-500/10 border border-amber-500/30 text-amber-400 font-mono">
          <Clock :size="12" />
          STALE TELEMETRY
        </span>
        <button 
          type="button"
          class="btn btn-secondary text-xs flex items-center gap-1.5 px-3 py-1.5"
          :disabled="queueStore.loading || queueStore.refreshing"
          @click="queueStore.fetchQueues()"
        >
          <RefreshCw :size="13" :class="{ 'animate-spin': queueStore.loading || queueStore.refreshing }" />
          <span>{{ queueStore.refreshing ? 'Refreshing...' : 'Refresh Queues' }}</span>
        </button>
      </div>
    </div>

    <!-- Summary Cards -->
    <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-5 gap-3">
      <!-- Broker Topology -->
      <div class="card p-4 border border-slate-800 bg-slate-900/60 shadow-lg flex flex-col justify-between">
        <div>
          <span class="text-[11px] font-semibold text-slate-400 uppercase tracking-wider block">Broker Topology</span>
          <div class="text-sm font-bold text-white flex items-center gap-2 mt-1.5">
            <Server :size="16" class="text-sky-400 shrink-0" />
            <span class="truncate" title="ActiveMQ Artemis 2.33">Artemis 2.33 Core</span>
          </div>
        </div>
        <p class="text-[10px] text-slate-500 mt-2 font-mono truncate">tcp://0.0.0.0:61616</p>
      </div>

      <!-- Total Queues -->
      <div class="card p-4 border border-slate-800 bg-slate-900/60 shadow-lg flex flex-col justify-between">
        <div>
          <span class="text-[11px] font-semibold text-slate-400 uppercase tracking-wider block">Total Queues</span>
          <div class="text-2xl font-extrabold text-sky-400 mt-1 font-mono">
            {{ queueStore.totalQueues }}
          </div>
        </div>
        <p class="text-[10px] text-slate-500 mt-1 font-sans">Active addresses in cluster</p>
      </div>

      <!-- Messages In Flight -->
      <div class="card p-4 border border-slate-800 bg-slate-900/60 shadow-lg flex flex-col justify-between">
        <div>
          <span class="text-[11px] font-semibold text-slate-400 uppercase tracking-wider block">Messages In Flight</span>
          <div 
            class="text-2xl font-extrabold mt-1 font-mono"
            :class="queueStore.messagesInFlight > 50 ? 'text-amber-400' : 'text-slate-200'"
          >
            {{ queueStore.messagesInFlight }}
          </div>
        </div>
        <p class="text-[10px] text-slate-500 mt-1 font-sans">Accumulated queue depth</p>
      </div>

      <!-- Total Consumers -->
      <div class="card p-4 border border-slate-800 bg-slate-900/60 shadow-lg flex flex-col justify-between">
        <div>
          <span class="text-[11px] font-semibold text-slate-400 uppercase tracking-wider block">Active Consumers</span>
          <div 
            class="text-2xl font-extrabold mt-1 font-mono"
            :class="queueStore.totalConsumers > 0 ? 'text-emerald-400' : 'text-amber-400'"
          >
            {{ queueStore.totalConsumers }}
          </div>
        </div>
        <p class="text-[10px] text-slate-500 mt-1 font-sans">Active consumer bindings</p>
      </div>

      <!-- DLQ Depth -->
      <div class="card p-4 border border-slate-800 bg-slate-900/60 shadow-lg flex flex-col justify-between">
        <div>
          <span class="text-[11px] font-semibold text-slate-400 uppercase tracking-wider block">DLQ Depth</span>
          <div 
            class="text-2xl font-extrabold mt-1 font-mono"
            :class="queueStore.totalDlqDepth > 0 ? 'text-rose-400' : 'text-slate-400'"
          >
            {{ queueStore.totalDlqDepth }}
          </div>
        </div>
        <p class="text-[10px] text-slate-500 mt-1 font-sans">Dead lettered messages</p>
      </div>
    </div>

    <!-- Search & Filter Controls -->
    <div class="card p-4 border border-slate-800 bg-slate-900/60 shadow-lg flex flex-col sm:flex-row items-center justify-between gap-3">
      <!-- Search Input -->
      <div class="relative w-full sm:w-80">
        <Search :size="15" class="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
        <input 
          type="text" 
          placeholder="Filter by queue, address, capability..." 
          class="w-full bg-slate-950/80 border border-slate-700/80 rounded-lg pl-9 pr-3 py-1.5 text-xs text-white placeholder-slate-500 focus:outline-none focus:border-sky-500 font-sans"
          :value="queueStore.searchQuery"
          @input="queueStore.setSearchQuery(($event.target as HTMLInputElement).value)"
        />
      </div>

      <!-- Status Filter Tabs -->
      <div class="flex items-center gap-1.5 w-full sm:w-auto overflow-x-auto pb-1 sm:pb-0">
        <button
          v-for="status in statusOptions"
          :key="status"
          type="button"
          class="px-2.5 py-1 rounded-md text-xs font-semibold transition-colors uppercase tracking-wider"
          :class="queueStore.statusFilter === status ? 'bg-sky-500 text-white shadow-sm' : 'bg-slate-800 text-slate-400 hover:text-white hover:bg-slate-700'"
          @click="queueStore.setStatusFilter(status)"
        >
          {{ status }}
        </button>
      </div>
    </div>

    <!-- Error Warning -->
    <div v-if="queueStore.error" class="p-3 rounded-lg bg-rose-500/10 border border-rose-500/30 text-xs text-rose-400 flex items-center justify-between">
      <div class="flex items-center gap-2">
        <AlertTriangle :size="16" />
        <span>{{ queueStore.error }}</span>
      </div>
      <button 
        type="button" 
        class="text-xs font-bold underline hover:text-rose-300 ml-4"
        @click="queueStore.fetchQueues()"
      >
        Retry
      </button>
    </div>

    <!-- Queues Table -->
    <QueueTable 
      :queues="queueStore.filteredQueues" 
      :loading="queueStore.loading"
      @select="queueStore.selectQueue($event.queueId)"
    />

    <!-- Queue Detail Drawer -->
    <QueueDetailDrawer 
      :queue="queueStore.selectedQueue" 
      :is-open="queueStore.isDrawerOpen" 
      @close="queueStore.closeDrawer()" 
    />
  </div>
</template>
