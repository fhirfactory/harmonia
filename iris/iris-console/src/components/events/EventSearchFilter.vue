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
import { ref } from 'vue';
import { Search, RotateCcw, Filter, Clock, ChevronDown, ChevronUp } from 'lucide-vue-next';
import { useEventStore, type EventTimePreset } from '../../stores/eventStore';

const eventStore = useEventStore();
const isAdvancedOpen = ref(false);

const timePresets: { label: string; value: EventTimePreset }[] = [
  { label: '15m', value: '15m' },
  { label: '1h', value: '1h' },
  { label: '6h', value: '6h' },
  { label: '24h', value: '24h' },
  { label: 'All Time', value: 'ALL' }
];

const subsystems = [
  { label: 'All Subsystems', value: 'ALL' },
  { label: 'Pylai (Gateways)', value: 'pylai' },
  { label: 'Petasos (Messaging)', value: 'petasos' },
  { label: 'Energeia (Workflows)', value: 'energeia' },
  { label: 'Mnemosyne (Persistence)', value: 'mnemosyne' },
  { label: 'Mneme (Cache Grid)', value: 'mneme' },
  { label: 'Themis (Security)', value: 'themis' },
  { label: 'Agora (Collaboration)', value: 'agora' },
  { label: 'Calliope (Contracts)', value: 'calliope' },
  { label: 'Iris (Operations)', value: 'iris' }
];

const statuses = [
  { label: 'All Statuses', value: 'ALL' },
  { label: 'Success', value: 'SUCCESS' },
  { label: 'Warning', value: 'WARNING' },
  { label: 'Failure', value: 'FAILURE' }
];

const commonEventTypes = [
  { label: 'All Event Types', value: 'ALL' },
  { label: 'MLLP Ingress (Receive)', value: 'MLLP_INGRESS' },
  { label: 'Queue Publish (Enqueue)', value: 'QUEUE_PUBLISH' },
  { label: 'Queue Delivery (Dequeue)', value: 'QUEUE_DELIVERY' },
  { label: 'Activity Execution', value: 'ACTIVITY_EXEC' },
  { label: 'Ergon Checkpoint', value: 'ERGON_CHECKPOINT' },
  { label: 'FHIR Transaction Commit', value: 'FHIR_COMMIT' },
  { label: 'Egress Dispatched', value: 'EGRESS_DISPATCH' },
  { label: 'Collaboration Event', value: 'COLLABORATION_EVENT' }
];

function handleSearch() {
  eventStore.search();
}

function handleReset() {
  eventStore.resetFilters();
  eventStore.search();
}
</script>

<template>
  <div class="card p-5 space-y-4 border border-slate-800 bg-slate-900/90 shadow-xl" role="search" aria-label="Events Diagnostic Search">
    <!-- Primary Filter Bar: Correlation ID & Quick Controls -->
    <div class="grid grid-cols-1 md:grid-cols-12 gap-3 items-end">
      <!-- Correlation ID Search (Dominant filter) -->
      <div class="md:col-span-5 space-y-1.5">
        <label for="event-search-correlation" class="block text-xs font-semibold text-slate-300">
          Correlation ID <span class="text-slate-500 font-normal">(Primary Cross-Subsystem Trace)</span>
        </label>
        <div class="relative">
          <input
            id="event-search-correlation"
            v-model="eventStore.correlationId"
            type="text"
            placeholder="e.g. corr-1234-abcd or interaction UUID..."
            class="input pl-9 pr-3 py-2 text-xs font-mono w-full bg-slate-950 border-slate-700/80 focus:border-sky-500 text-white placeholder-slate-500 rounded-lg"
            @keydown.enter="handleSearch"
          />
          <Search :size="15" class="absolute left-3 top-2.5 text-slate-500" />
        </div>
      </div>

      <!-- Subsystem Filter -->
      <div class="md:col-span-3 space-y-1.5">
        <label for="event-search-subsystem" class="block text-xs font-semibold text-slate-300">
          Subsystem
        </label>
        <select
          id="event-search-subsystem"
          v-model="eventStore.subsystem"
          class="input py-2 text-xs w-full bg-slate-950 border-slate-700/80 text-white rounded-lg focus:border-sky-500"
          @change="handleSearch"
        >
          <option v-for="sub in subsystems" :key="sub.value" :value="sub.value">
            {{ sub.label }}
          </option>
        </select>
      </div>

      <!-- Status Filter -->
      <div class="md:col-span-2 space-y-1.5">
        <label for="event-search-status" class="block text-xs font-semibold text-slate-300">
          Status
        </label>
        <select
          id="event-search-status"
          v-model="eventStore.status"
          class="input py-2 text-xs w-full bg-slate-950 border-slate-700/80 text-white rounded-lg focus:border-sky-500"
          @change="handleSearch"
        >
          <option v-for="st in statuses" :key="st.value" :value="st.value">
            {{ st.label }}
          </option>
        </select>
      </div>

      <!-- Action Buttons -->
      <div class="md:col-span-2 flex items-center gap-2">
        <button
          type="button"
          class="btn-primary flex-1 flex items-center justify-center gap-1.5 text-xs py-2 px-3 shadow-md"
          :disabled="eventStore.loading"
          @click="handleSearch"
        >
          <Search :size="14" />
          <span>{{ eventStore.loading ? 'Searching...' : 'Search' }}</span>
        </button>
        <button
          type="button"
          class="btn-secondary flex items-center justify-center p-2 text-xs border border-slate-700/80 hover:bg-slate-800 rounded-lg"
          title="Reset all filters"
          aria-label="Reset all filters"
          @click="handleReset"
        >
          <RotateCcw :size="14" class="text-slate-400" />
        </button>
      </div>
    </div>

    <!-- Secondary Row: Time Window Presets & Advanced Filter Toggle -->
    <div class="flex flex-wrap items-center justify-between gap-3 pt-2 border-t border-slate-800/80">
      <div class="flex items-center gap-2">
        <span class="text-xs text-slate-400 flex items-center gap-1.5 font-medium">
          <Clock :size="13" class="text-slate-500" />
          <span>Time Window:</span>
        </span>
        <div class="inline-flex rounded-lg bg-slate-950 p-0.5 border border-slate-800" role="group" aria-label="Time Window Presets">
          <button
            v-for="preset in timePresets"
            :key="preset.value"
            type="button"
            class="px-2.5 py-1 text-[11px] font-medium rounded-md transition-colors"
            :class="eventStore.timePreset === preset.value ? 'bg-sky-500 text-white font-semibold shadow-sm' : 'text-slate-400 hover:text-white'"
            @click="eventStore.timePreset = preset.value; handleSearch()"
          >
            {{ preset.label }}
          </button>
        </div>
      </div>

      <button
        type="button"
        class="text-xs text-sky-400 hover:text-sky-300 font-medium flex items-center gap-1.5 transition-colors"
        @click="isAdvancedOpen = !isAdvancedOpen"
      >
        <Filter :size="13" />
        <span>{{ isAdvancedOpen ? 'Hide Advanced Filters' : 'More Diagnostic Identifiers' }}</span>
        <ChevronUp v-if="isAdvancedOpen" :size="13" />
        <ChevronDown v-else :size="13" />
      </button>
    </div>

    <!-- Advanced Collapsible Filters: Causation, Message, Pragma, Event Type -->
    <div v-if="isAdvancedOpen" class="grid grid-cols-1 md:grid-cols-4 gap-3 pt-3 border-t border-slate-800/60">
      <!-- Causation ID -->
      <div class="space-y-1">
        <label for="event-search-causation" class="block text-[11px] font-medium text-slate-400">
          Causation ID
        </label>
        <input
          id="event-search-causation"
          v-model="eventStore.causationId"
          type="text"
          placeholder="e.g. caus-msg-..."
          class="input py-1.5 text-xs font-mono w-full bg-slate-950 border-slate-800 text-white rounded-md"
          @keydown.enter="handleSearch"
        />
      </div>

      <!-- Message ID -->
      <div class="space-y-1">
        <label for="event-search-message" class="block text-[11px] font-medium text-slate-400">
          Message ID
        </label>
        <input
          id="event-search-message"
          v-model="eventStore.messageId"
          type="text"
          placeholder="e.g. msg-hl7-..."
          class="input py-1.5 text-xs font-mono w-full bg-slate-950 border-slate-800 text-white rounded-md"
          @keydown.enter="handleSearch"
        />
      </div>

      <!-- Pragma ID -->
      <div class="space-y-1">
        <label for="event-search-pragma" class="block text-[11px] font-medium text-slate-400">
          Pragma ID (Envelope)
        </label>
        <input
          id="event-search-pragma"
          v-model="eventStore.pragmaId"
          type="text"
          placeholder="e.g. pragma-..."
          class="input py-1.5 text-xs font-mono w-full bg-slate-950 border-slate-800 text-white rounded-md"
          @keydown.enter="handleSearch"
        />
      </div>

      <!-- Event Type -->
      <div class="space-y-1">
        <label for="event-search-type" class="block text-[11px] font-medium text-slate-400">
          Event Type
        </label>
        <select
          id="event-search-type"
          v-model="eventStore.eventType"
          class="input py-1.5 text-xs w-full bg-slate-950 border-slate-800 text-white rounded-md"
          @change="handleSearch"
        >
          <option v-for="t in commonEventTypes" :key="t.value" :value="t.value">
            {{ t.label }}
          </option>
        </select>
      </div>
    </div>
  </div>
</template>
