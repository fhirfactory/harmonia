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
import { onMounted } from 'vue';
import { Activity, ShieldCheck, RefreshCw, AlertCircle } from 'lucide-vue-next';
import { useEventStore } from '../stores/eventStore';
import EventSearchFilter from '../components/events/EventSearchFilter.vue';
import EventTimeline from '../components/events/EventTimeline.vue';
import EventDetailDrawer from '../components/events/EventDetailDrawer.vue';

const eventStore = useEventStore();

onMounted(async () => {
  await eventStore.fetchEvents();
});

function handleRefresh() {
  eventStore.fetchEvents();
}
</script>

<template>
  <div class="space-y-6 font-sans">
    <!-- View Header -->
    <div class="flex flex-wrap items-center justify-between gap-4 border-b border-slate-200 pb-4">
      <div>
        <h1 class="text-2xl font-extrabold text-slate-900 tracking-tight flex items-center gap-2">
          <Activity :size="24" class="text-sky-600" />
          <span>Events Diagnostic Timeline</span>
        </h1>
        <p class="text-xs text-slate-500 mt-1">
          Cross-subsystem interaction diagnostic tracing across gateways, message brokers, workflows, and persistence.
        </p>
      </div>

      <div class="flex items-center gap-3">
        <!-- Zero-PHI Boundary Indicator -->
        <span class="px-2.5 py-1 rounded-full text-xs font-mono bg-emerald-50 border border-emerald-200 text-emerald-800 flex items-center gap-1.5 shadow-xs">
          <ShieldCheck :size="13" class="text-emerald-600" />
          <span>Zero-PHI Diagnostic Boundary</span>
        </span>

        <!-- Manual Refresh Button -->
        <button
          type="button"
          class="btn-secondary flex items-center gap-1.5 text-xs py-1.5 px-3 bg-white border border-slate-300 hover:bg-slate-50 text-slate-700 hover:text-slate-900 rounded-md shadow-xs transition-colors"
          :disabled="eventStore.loading"
          @click="handleRefresh"
        >
          <RefreshCw :size="13" :class="{ 'animate-spin': eventStore.loading }" />
          <span>Refresh</span>
        </button>
      </div>
    </div>

    <!-- Diagnostic Search & Filter Controls -->
    <EventSearchFilter />

    <!-- Summary Metrics Cards (when events exist) -->
    <div v-if="eventStore.totalEvents > 0" class="grid grid-cols-2 sm:grid-cols-4 gap-3">
      <div class="card p-3 border border-[var(--iris-border-default)] bg-white rounded-[var(--iris-border-radius)] shadow-subtle">
        <div class="text-[11px] font-semibold text-slate-500 uppercase tracking-wider">Total Diagnostic Events</div>
        <div class="text-xl font-bold font-mono text-slate-900 mt-1">{{ eventStore.totalEvents }}</div>
      </div>
      <div class="card p-3 border border-[var(--iris-border-default)] bg-white rounded-[var(--iris-border-radius)] shadow-subtle">
        <div class="text-[11px] font-semibold text-emerald-700 uppercase tracking-wider">Successful Operations</div>
        <div class="text-xl font-bold font-mono text-emerald-800 mt-1">{{ eventStore.successCount }}</div>
      </div>
      <div class="card p-3 border border-[var(--iris-border-default)] bg-white rounded-[var(--iris-border-radius)] shadow-subtle">
        <div class="text-[11px] font-semibold text-amber-700 uppercase tracking-wider">Warnings Observed</div>
        <div class="text-xl font-bold font-mono text-amber-800 mt-1">{{ eventStore.warningCount }}</div>
      </div>
      <div class="card p-3 border border-[var(--iris-border-default)] bg-white rounded-[var(--iris-border-radius)] shadow-subtle">
        <div class="text-[11px] font-semibold text-rose-700 uppercase tracking-wider">Failed Operations</div>
        <div class="text-xl font-bold font-mono text-rose-800 mt-1">{{ eventStore.failureCount }}</div>
      </div>
    </div>

    <!-- Error Banner -->
    <div v-if="eventStore.error" class="p-4 bg-rose-50 border border-rose-200 text-rose-800 rounded-lg text-xs flex items-center gap-2">
      <AlertCircle :size="16" class="shrink-0 text-rose-600" />
      <span>{{ eventStore.error }}</span>
    </div>

    <!-- Operational Event Flowchart / Timeline -->
    <EventTimeline 
      :events="eventStore.chronologicalEvents" 
      :loading="eventStore.loading" 
      @select="eventStore.selectEvent" 
    />

    <!-- Event Detail Drawer -->
    <EventDetailDrawer
      :event="eventStore.selectedEvent"
      :is-open="eventStore.isEventDrawerOpen"
      :teleport="true"
      @close="eventStore.closeEventDrawer"
      @trace="eventStore.traceCorrelation"
    />
  </div>
</template>
