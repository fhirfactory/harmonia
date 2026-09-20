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
import { IrisToolbar } from '@harmonia/iris-befe';
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
  <div class="event-search-filter space-y-2 font-sans" role="search" aria-label="Events Diagnostic Search">
    <!-- IrisToolbar for primary filter controls -->
    <IrisToolbar
      :show-search="false"
      :show-refresh="false"
    >
      <template #left>
        <div class="flex flex-wrap items-center gap-3">
          <!-- Correlation ID Search (Dominant filter) -->
          <div class="flex items-center gap-2">
            <label for="event-search-correlation" class="text-xs font-semibold text-slate-700 whitespace-nowrap">
              Correlation ID:
            </label>
            <div class="relative">
              <input
                id="event-search-correlation"
                v-model="eventStore.correlationId"
                type="text"
                placeholder="e.g. corr-1234-abcd..."
                class="input pl-8 pr-3 py-1.5 text-xs font-mono w-56 md:w-64 bg-white border border-slate-300 focus:border-sky-500 focus:ring-1 focus:ring-sky-500 text-slate-900 placeholder-slate-400 rounded-md"
                @keydown.enter="handleSearch"
              />
              <Search :size="13" class="absolute left-2.5 top-2 text-slate-400" />
            </div>
          </div>

          <!-- Subsystem Filter -->
          <div class="flex items-center gap-2">
            <label for="event-search-subsystem" class="text-xs font-semibold text-slate-700 whitespace-nowrap">
              Subsystem:
            </label>
            <select
              id="event-search-subsystem"
              v-model="eventStore.subsystem"
              class="input py-1.5 px-2.5 text-xs bg-white border border-slate-300 text-slate-900 rounded-md focus:border-sky-500 focus:ring-1 focus:ring-sky-500"
              @change="handleSearch"
            >
              <option v-for="sub in subsystems" :key="sub.value" :value="sub.value">
                {{ sub.label }}
              </option>
            </select>
          </div>

          <!-- Status Filter -->
          <div class="flex items-center gap-2">
            <label for="event-search-status" class="text-xs font-semibold text-slate-700 whitespace-nowrap">
              Status:
            </label>
            <select
              id="event-search-status"
              v-model="eventStore.status"
              class="input py-1.5 px-2.5 text-xs bg-white border border-slate-300 text-slate-900 rounded-md focus:border-sky-500 focus:ring-1 focus:ring-sky-500"
              @change="handleSearch"
            >
              <option v-for="st in statuses" :key="st.value" :value="st.value">
                {{ st.label }}
              </option>
            </select>
          </div>
        </div>
      </template>

      <template #right>
        <div class="flex items-center gap-2">
          <button
            type="button"
            class="btn-primary flex items-center justify-center gap-1.5 text-xs py-1.5 px-3 bg-sky-600 hover:bg-sky-700 text-white font-semibold rounded-md shadow-xs transition-colors cursor-pointer"
            :disabled="eventStore.loading"
            @click="handleSearch"
          >
            <Search :size="13" />
            <span>{{ eventStore.loading ? 'Searching...' : 'Search' }}</span>
          </button>
          <button
            type="button"
            class="btn-secondary flex items-center justify-center p-1.5 text-xs bg-white border border-slate-300 hover:bg-slate-50 text-slate-700 hover:text-slate-900 rounded-md shadow-xs transition-colors cursor-pointer"
            title="Reset all filters"
            aria-label="Reset all filters"
            @click="handleReset"
          >
            <RotateCcw :size="13" class="text-slate-500" />
          </button>
        </div>
      </template>
    </IrisToolbar>

    <!-- Secondary Row: Time Window Presets & Advanced Filter Toggle -->
    <div class="bg-white border border-[var(--iris-border-default)] rounded-[var(--iris-border-radius)] p-3 shadow-subtle space-y-3">
      <div class="flex flex-wrap items-center justify-between gap-3">
        <div class="flex items-center gap-2">
          <span class="text-xs text-slate-600 flex items-center gap-1.5 font-medium">
            <Clock :size="13" class="text-slate-400" />
            <span>Time Window:</span>
          </span>
          <div class="inline-flex rounded-md bg-slate-100 p-0.5 border border-slate-200" role="group" aria-label="Time Window Presets">
            <button
              v-for="preset in timePresets"
              :key="preset.value"
              type="button"
              class="px-2.5 py-1 text-[11px] font-medium rounded transition-colors cursor-pointer"
              :class="eventStore.timePreset === preset.value ? 'bg-sky-50 text-sky-800 border border-sky-300 font-bold shadow-xs' : 'text-slate-600 hover:text-slate-900'"
              @click="eventStore.timePreset = preset.value; handleSearch()"
            >
              {{ preset.label }}
            </button>
          </div>
        </div>

        <button
          type="button"
          class="text-xs text-sky-600 hover:text-sky-700 text-sky-400 font-medium flex items-center gap-1.5 transition-colors cursor-pointer"
          @click="isAdvancedOpen = !isAdvancedOpen"
        >
          <Filter :size="13" />
          <span>{{ isAdvancedOpen ? 'Hide Advanced Filters' : 'More Diagnostic Identifiers' }}</span>
          <ChevronUp v-if="isAdvancedOpen" :size="13" />
          <ChevronDown v-else :size="13" />
        </button>
      </div>

      <!-- Advanced Collapsible Filters: Causation, Message, Pragma, Event Type -->
      <div v-if="isAdvancedOpen" class="grid grid-cols-1 md:grid-cols-4 gap-3 pt-3 border-t border-slate-200">
        <!-- Causation ID -->
        <div class="space-y-1">
          <label for="event-search-causation" class="block text-[11px] font-medium text-slate-600">
            Causation ID
          </label>
          <input
            id="event-search-causation"
            v-model="eventStore.causationId"
            type="text"
            placeholder="e.g. caus-msg-..."
            class="input py-1.5 px-2.5 text-xs font-mono w-full bg-white border border-slate-300 text-slate-900 rounded-md focus:border-sky-500 focus:ring-1 focus:ring-sky-500"
            @keydown.enter="handleSearch"
          />
        </div>

        <!-- Message ID -->
        <div class="space-y-1">
          <label for="event-search-message" class="block text-[11px] font-medium text-slate-600">
            Message ID
          </label>
          <input
            id="event-search-message"
            v-model="eventStore.messageId"
            type="text"
            placeholder="e.g. msg-hl7-..."
            class="input py-1.5 px-2.5 text-xs font-mono w-full bg-white border border-slate-300 text-slate-900 rounded-md focus:border-sky-500 focus:ring-1 focus:ring-sky-500"
            @keydown.enter="handleSearch"
          />
        </div>

        <!-- Pragma ID -->
        <div class="space-y-1">
          <label for="event-search-pragma" class="block text-[11px] font-medium text-slate-600">
            Pragma ID (Envelope)
          </label>
          <input
            id="event-search-pragma"
            v-model="eventStore.pragmaId"
            type="text"
            placeholder="e.g. pragma-..."
            class="input py-1.5 px-2.5 text-xs font-mono w-full bg-white border border-slate-300 text-slate-900 rounded-md focus:border-sky-500 focus:ring-1 focus:ring-sky-500"
            @keydown.enter="handleSearch"
          />
        </div>

        <!-- Event Type -->
        <div class="space-y-1">
          <label for="event-search-type" class="block text-[11px] font-medium text-slate-600">
            Event Type
          </label>
          <select
            id="event-search-type"
            v-model="eventStore.eventType"
            class="input py-1.5 px-2 text-xs w-full bg-white border border-slate-300 text-slate-900 rounded-md focus:border-sky-500 focus:ring-1 focus:ring-sky-500"
            @change="handleSearch"
          >
            <option v-for="t in commonEventTypes" :key="t.value" :value="t.value">
              {{ t.label }}
            </option>
          </select>
        </div>
      </div>
    </div>
  </div>
</template>
