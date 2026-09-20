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
import QueueTable from '../components/queues/QueueTable.vue';
import QueueDetailDrawer from '../components/queues/QueueDetailDrawer.vue';
import { IrisSubsystemIdentity, IrisStatus, IrisToolbar } from '@harmonia/iris-befe';
import { 
  Radio, 
  Server, 
  Search, 
  AlertTriangle, 
  Clock,
  Layers
} from 'lucide-vue-next';

const queueStore = useQueueStore();
const operationsStore = useOperationsStore();
let refreshTimer: any = null;

const petasosSubsystem = computed(() => operationsStore.subsystems.find(subsystem => subsystem.id === 'petasos'));
const petasosStatus = computed(() => petasosSubsystem.value?.state || 'UNKNOWN');
const petasosStale = computed(() => Boolean(petasosSubsystem.value?.stale));

onMounted(async () => {
  await Promise.all([queueStore.fetchQueues(), operationsStore.fetchSubsystems()]);
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
  <div class="messages-view space-y-5 font-sans">
    <!-- Subsystem Identity & Subordinated Artemis Metadata -->
    <IrisSubsystemIdentity 
      name="Petasos" 
      description="ActiveMQ Artemis message broker topology, message flow rates, consumer bindings, and DLQ depth."
      :status="petasosStatus"
      :stale="petasosStale"
    >
      <template #icon>
        <Radio :size="22" class="text-sky-700" />
      </template>
      <template #badges>
        <span class="px-2 py-0.5 rounded text-xs font-semibold bg-slate-100 text-slate-700 border border-slate-200">
          Messaging &amp; Transport
        </span>
        <span v-if="queueStore.isStale" class="inline-flex items-center gap-1 px-2 py-0.5 rounded text-[11px] font-mono font-medium bg-amber-50 text-amber-800 border border-amber-300">
          <Clock :size="12" />
          STALE TELEMETRY
        </span>
      </template>
      <template #actions>
        <!-- Subordinated Middleware Pill: Artemis Broker -->
        <div class="p-2.5 rounded-md bg-slate-50 border border-slate-200 text-xs text-slate-700 flex items-center gap-3 shrink-0">
          <div class="p-1.5 rounded bg-sky-50 text-sky-700 border border-sky-100">
            <Server :size="16" />
          </div>
          <div>
            <div class="flex items-center gap-2">
              <span class="text-[10px] font-bold text-slate-500 uppercase tracking-wider">Subordinated Middleware</span>
              <span class="px-1.5 py-0.2 rounded text-[10px] font-mono bg-sky-100 text-sky-800 font-semibold">ActiveMQ Artemis</span>
            </div>
            <div class="font-mono text-slate-900 font-bold text-xs mt-0.5">
              Port 61616 &bull; Core JMS Cluster
            </div>
          </div>
        </div>
      </template>
    </IrisSubsystemIdentity>

    <!-- Metric Strip: Answering "Are messages backing up?" -->
    <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-5 gap-3">
      <!-- Broker Topology -->
      <div class="p-3.5 bg-white border border-slate-200 rounded-lg shadow-xs flex flex-col justify-between">
        <div>
          <span class="text-[11px] font-bold text-slate-500 uppercase tracking-wider block">Broker Topology</span>
          <div class="text-sm font-bold text-slate-900 flex items-center gap-2 mt-1">
            <Server :size="15" class="text-sky-700 shrink-0" />
            <span class="truncate" title="ActiveMQ Artemis 2.33 Core">Artemis 2.33 Core</span>
          </div>
        </div>
        <p class="text-[11px] text-slate-500 mt-2 font-mono truncate">tcp://0.0.0.0:61616</p>
      </div>

      <!-- Total Queues -->
      <div class="p-3.5 bg-white border border-slate-200 rounded-lg shadow-xs flex flex-col justify-between">
        <div>
          <span class="text-[11px] font-bold text-slate-500 uppercase tracking-wider block">Total Queues</span>
          <div class="text-2xl font-extrabold text-sky-700 mt-1 font-mono">
            {{ queueStore.totalQueues }}
          </div>
        </div>
        <p class="text-[11px] text-slate-500 mt-1 font-sans">Active addresses in cluster</p>
      </div>

      <!-- Messages In Flight -->
      <div class="p-3.5 bg-white border border-slate-200 rounded-lg shadow-xs flex flex-col justify-between">
        <div>
          <span class="text-[11px] font-bold text-slate-500 uppercase tracking-wider block">Messages In Flight</span>
          <div 
            class="text-2xl font-extrabold mt-1 font-mono"
            :class="queueStore.messagesInFlight > 50 ? 'text-amber-700' : 'text-slate-800'"
          >
            {{ queueStore.messagesInFlight }}
          </div>
        </div>
        <p class="text-[11px] text-slate-500 mt-1 font-sans">Accumulated queue depth</p>
      </div>

      <!-- Total Consumers -->
      <div class="p-3.5 bg-white border border-slate-200 rounded-lg shadow-xs flex flex-col justify-between">
        <div>
          <span class="text-[11px] font-bold text-slate-500 uppercase tracking-wider block">Active Consumers</span>
          <div 
            class="text-2xl font-extrabold mt-1 font-mono"
            :class="queueStore.totalConsumers > 0 ? 'text-emerald-700' : 'text-amber-700'"
          >
            {{ queueStore.totalConsumers }}
          </div>
        </div>
        <p class="text-[11px] text-slate-500 mt-1 font-sans">Active consumer bindings</p>
      </div>

      <!-- DLQ Depth -->
      <div class="p-3.5 bg-white border border-slate-200 rounded-lg shadow-xs flex flex-col justify-between">
        <div>
          <span class="text-[11px] font-bold text-slate-500 uppercase tracking-wider block">DLQ Depth</span>
          <div 
            class="text-2xl font-extrabold mt-1 font-mono"
            :class="queueStore.totalDlqDepth > 0 ? 'text-rose-700' : 'text-slate-400'"
          >
            {{ queueStore.totalDlqDepth }}
          </div>
        </div>
        <p class="text-[11px] text-slate-500 mt-1 font-sans">Dead lettered messages</p>
      </div>
    </div>

    <!-- Standardized Page Toolbar -->
    <IrisToolbar 
      :show-search="false"
      :show-refresh="true"
      :refreshing="queueStore.loading || queueStore.refreshing"
      @refresh="queueStore.fetchQueues()"
    >
      <template #left>
        <!-- Search Input -->
        <div class="relative w-full sm:w-80">
          <Search :size="14" class="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
          <input 
            type="text" 
            placeholder="Filter by queue, address, capability..." 
            class="w-full bg-slate-50 border border-slate-300 rounded-md pl-9 pr-3 py-1.5 text-xs text-slate-900 placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-sky-500/20 focus:border-sky-500 font-sans"
            :value="queueStore.searchQuery"
            @input="queueStore.setSearchQuery(($event.target as HTMLInputElement).value)"
            aria-label="Filter queues input"
          />
        </div>
      </template>

      <template #filter>
        <!-- Status Filter Tabs -->
        <div class="flex items-center gap-1.5 overflow-x-auto">
          <button
            v-for="status in statusOptions"
            :key="status"
            type="button"
            class="px-2.5 py-1 rounded-md text-xs font-semibold transition-colors uppercase tracking-wider cursor-pointer"
            :class="queueStore.statusFilter === status 
              ? 'bg-sky-50 text-sky-800 border border-sky-300 font-bold shadow-xs' 
              : 'bg-white text-slate-600 hover:text-slate-900 hover:bg-slate-100 border border-slate-200'"
            @click="queueStore.setStatusFilter(status)"
          >
            {{ status }}
          </button>
        </div>
      </template>
    </IrisToolbar>

    <!-- Error Warning -->
    <div v-if="queueStore.error" class="p-3 rounded-lg bg-rose-50 border border-rose-200 text-xs text-rose-800 flex items-center justify-between">
      <div class="flex items-center gap-2">
        <AlertTriangle :size="16" class="text-rose-600 shrink-0" />
        <span>{{ queueStore.error }}</span>
      </div>
      <button 
        type="button" 
        class="text-xs font-bold underline hover:text-rose-900 ml-4 cursor-pointer"
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
      :teleport="true"
      @close="queueStore.closeDrawer()" 
    />
  </div>
</template>
