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
import type { OperationalEvent } from '../../models/operations';
import { IrisDataTable, IrisStatus, type DataTableColumn } from '@harmonia/iris-befe';
import { 
  ArrowDown, 
  Clock, 
  Activity, 
  Database, 
  Radio, 
  Cpu, 
  Shield, 
  Layers, 
  FileCode, 
  LayoutDashboard, 
  MessagesSquare, 
  ChevronRight, 
  FileSearch,
  List,
  GitCommit
} from 'lucide-vue-next';

const props = defineProps<{
  events: OperationalEvent[];
  loading?: boolean;
}>();

const emit = defineEmits<{
  (e: 'select', event: OperationalEvent): void;
}>();

const viewMode = ref<'flow' | 'table'>('flow');

const tableColumns: DataTableColumn[] = [
  { field: 'index', header: '#', width: '48px' },
  { field: 'timestamp', header: 'Timestamp', width: '100px', sortable: true },
  { field: 'subsystem', header: 'Subsystem', width: '120px', sortable: true },
  { field: 'eventType', header: 'Event Type', width: '140px', sortable: true },
  { field: 'operation', header: 'Operation', sortable: true },
  { field: 'correlationId', header: 'Correlation ID', width: '160px' },
  { field: 'duration', header: 'Duration', width: '90px', sortable: true },
  { field: 'status', header: 'Status', width: '110px', sortable: true },
  { field: 'actions', header: '', width: '44px', align: 'right' }
];

function getSubsystemMeta(subsystem?: string) {
  const s = (subsystem || '').toLowerCase();
  switch (s) {
    case 'pylai':
      return { label: 'PYLAI', color: 'text-purple-800 bg-purple-50 border-purple-200', icon: Radio };
    case 'petasos':
      return { label: 'PETASOS', color: 'text-amber-800 bg-amber-50 border-amber-200', icon: Radio };
    case 'energeia':
      return { label: 'ENERGEIA', color: 'text-emerald-800 bg-emerald-50 border-emerald-200', icon: Cpu };
    case 'mnemosyne':
      return { label: 'MNEMOSYNE', color: 'text-blue-800 bg-blue-50 border-blue-200', icon: Database };
    case 'mneme':
      return { label: 'MNEME', color: 'text-indigo-800 bg-indigo-50 border-indigo-200', icon: Layers };
    case 'agora':
      return { label: 'AGORA', color: 'text-pink-800 bg-pink-50 border-pink-200', icon: MessagesSquare };
    case 'themis':
      return { label: 'THEMIS', color: 'text-rose-800 bg-rose-50 border-rose-200', icon: Shield };
    case 'calliope':
      return { label: 'CALLIOPE', color: 'text-teal-800 bg-teal-50 border-teal-200', icon: FileCode };
    case 'iris':
      return { label: 'IRIS', color: 'text-sky-800 bg-sky-50 border-sky-200', icon: LayoutDashboard };
    default:
      return { label: (subsystem || 'UNKNOWN').toUpperCase(), color: 'text-slate-700 bg-slate-100 border-slate-200', icon: Activity };
  }
}

function formatTime(timestamp: number): string {
  if (!timestamp) return 'N/A';
  const d = new Date(timestamp);
  return d.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', second: '2-digit' });
}

function formatDate(timestamp: number): string {
  if (!timestamp) return '';
  const d = new Date(timestamp);
  return d.toISOString().split('T')[0];
}

function formatDuration(ms?: number | null): string {
  if (ms == null || isNaN(ms)) return 'N/A';
  return `${ms}ms`;
}

function calculateDelta(index: number): string | null {
  if (index === 0 || !props.events[index] || !props.events[index - 1]) return null;
  const delta = props.events[index].timestamp - props.events[index - 1].timestamp;
  if (delta < 0) return null;
  if (delta < 1000) return `+${delta}ms`;
  return `+${(delta / 1000).toFixed(2)}s`;
}
</script>

<template>
  <div class="space-y-4 font-sans" aria-label="Operational Events Timeline">
    <!-- View Mode Toggle & Event Summary Header -->
    <div class="flex items-center justify-between pb-1">
      <div class="flex items-center gap-2">
        <GitCommit :size="18" class="text-sky-600" />
        <h3 class="text-sm font-bold text-slate-900 tracking-wide">
          Interaction Sequence Flow
        </h3>
        <span class="px-2 py-0.5 rounded-full text-xs font-mono bg-slate-100 text-slate-700 border border-slate-200">
          {{ events.length }} {{ events.length === 1 ? 'Hop' : 'Hops' }}
        </span>
      </div>

      <div class="flex items-center gap-1 bg-slate-100 p-0.5 rounded-md border border-slate-200">
        <button
          type="button"
          class="px-2.5 py-1 text-xs font-medium rounded transition-colors flex items-center gap-1.5 cursor-pointer"
          :class="viewMode === 'flow' ? 'bg-sky-50 text-sky-800 border border-sky-300 font-bold shadow-xs' : 'text-slate-600 hover:text-slate-900'"
          @click="viewMode = 'flow'"
        >
          <GitCommit :size="13" />
          <span>Flowchart</span>
        </button>
        <button
          type="button"
          class="px-2.5 py-1 text-xs font-medium rounded transition-colors flex items-center gap-1.5 cursor-pointer"
          :class="viewMode === 'table' ? 'bg-sky-50 text-sky-800 border border-sky-300 font-bold shadow-xs' : 'text-slate-600 hover:text-slate-900'"
          @click="viewMode = 'table'"
        >
          <List :size="13" />
          <span>Table</span>
        </button>
      </div>
    </div>

    <!-- Loading Skeleton -->
    <div v-if="loading" class="p-12 text-center space-y-3 bg-white border border-slate-200 rounded-lg shadow-xs">
      <div class="inline-block animate-spin rounded-full h-8 w-8 border-2 border-sky-600 border-t-transparent"></div>
      <p class="text-xs text-slate-500 font-medium">Tracing interaction hops across Harmonia subsystems...</p>
    </div>

    <!-- Honest Empty State -->
    <div v-else-if="events.length === 0" class="p-12 text-center space-y-3 bg-white border border-dashed border-slate-300 rounded-lg shadow-xs">
      <FileSearch :size="38" class="mx-auto text-slate-400" />
      <h4 class="text-sm font-bold text-slate-900">No Diagnostic Events Recorded</h4>
      <p class="text-xs text-slate-500 max-w-md mx-auto leading-relaxed">
        No interaction events matched the specified search criteria or time window. Search by a known Correlation ID to trace cross-subsystem message flows.
      </p>
    </div>

    <!-- 1. Vertical Flowchart View -->
    <div v-else-if="viewMode === 'flow'" class="relative pl-6 sm:pl-8 space-y-4">
      <!-- Continuous vertical guideline -->
      <div class="absolute left-3 sm:left-4 top-4 bottom-4 w-0.5 bg-gradient-to-b from-sky-400 via-indigo-400/50 to-slate-200 -translate-x-1/2"></div>

      <div 
        v-for="(event, index) in events" 
        :key="event.eventId"
        class="relative"
      >
        <!-- Sequence Node Marker on the vertical line -->
        <div class="absolute -left-6 sm:-left-8 top-5 w-7 h-7 rounded-full bg-white border-2 border-sky-600 flex items-center justify-center -translate-x-1/2 z-10 shadow-xs">
          <span class="text-[10px] font-mono font-bold text-sky-700">{{ index + 1 }}</span>
        </div>

        <!-- Event Hop Card -->
        <div 
          class="card p-4 hover:border-sky-400 transition-all cursor-pointer bg-white border border-slate-200 shadow-xs rounded-lg group hover:bg-slate-50/80"
          role="button"
          tabindex="0"
          :aria-label="`Hop ${index + 1}: ${event.subsystem} ${event.operation}`"
          @click="emit('select', event)"
          @keydown.enter="emit('select', event)"
        >
          <div class="flex flex-wrap items-start justify-between gap-2">
            <!-- Left Side: Subsystem Pill, Operation, Event Type -->
            <div class="space-y-1">
              <div class="flex flex-wrap items-center gap-2">
                <span 
                  class="px-2 py-0.5 rounded text-[11px] font-mono font-bold border flex items-center gap-1"
                  :class="getSubsystemMeta(event.subsystem).color"
                >
                  <component :is="getSubsystemMeta(event.subsystem).icon" :size="12" />
                  <span>{{ getSubsystemMeta(event.subsystem).label }}</span>
                </span>

                <span class="text-xs font-bold text-slate-900 group-hover:text-sky-700 transition-colors">
                  {{ event.operation }}
                </span>

                <span class="text-[11px] font-mono text-slate-600 bg-slate-100 px-2 py-0.5 rounded border border-slate-200">
                  {{ event.eventType }}
                </span>
              </div>

              <!-- Identifiers summary row -->
              <div class="flex flex-wrap items-center gap-2 pt-1 text-[11px] font-mono text-slate-500">
                <span v-if="event.correlationId" class="text-sky-700 font-semibold">
                  corr: {{ event.correlationId }}
                </span>
                <span v-if="event.messageId" class="text-slate-400">
                  &bull; msg: {{ event.messageId }}
                </span>
                <span v-if="event.pragmaId" class="text-slate-400">
                  &bull; pragma: {{ event.pragmaId }}
                </span>
                <span v-if="event.ergonId" class="text-purple-700">
                  &bull; ergon: {{ event.ergonId }}
                </span>
              </div>
            </div>

            <!-- Right Side: Status Badge, Timing, Duration -->
            <div class="flex flex-col items-end gap-1.5 text-right">
              <div class="flex items-center gap-2">
                <span 
                  class="text-[10px] font-mono px-1.5 py-0.5 rounded bg-slate-100 text-slate-700 border border-slate-200"
                  title="Execution Duration"
                >
                  {{ formatDuration(event.durationMs) }}
                </span>
                <IrisStatus :status="event.status" label-format="upper" size="sm" />
              </div>

              <div class="flex items-center gap-1.5 text-[11px] font-mono text-slate-500">
                <Clock :size="11" class="text-slate-400" />
                <span>{{ formatTime(event.timestamp) }}</span>
                <span v-if="calculateDelta(index)" class="text-sky-700 font-semibold">
                  ({{ calculateDelta(index) }})
                </span>
              </div>
            </div>
          </div>
        </div>

        <!-- Directional Down Arrow between hops -->
        <div v-if="index < events.length - 1" class="flex items-center justify-start pl-4 py-1">
          <ArrowDown :size="14" class="text-sky-500/80 -translate-x-1/2" />
        </div>
      </div>
    </div>

    <!-- 2. Dense Tabular View -->
    <div v-else class="event-table-container bg-white border border-slate-200 rounded-lg overflow-hidden shadow-xs">
      <IrisDataTable
        :value="events"
        :columns="tableColumns"
        :loading="loading"
        data-key="eventId"
        empty-message="No Diagnostic Events Recorded"
        @row-click="emit('select', $event.data)"
      >
        <template #index="{ index }">
          <span class="font-mono text-xs text-slate-400">{{ index + 1 }}</span>
        </template>

        <template #timestamp="{ data }">
          <span class="font-mono text-xs text-slate-600 whitespace-nowrap">
            {{ formatTime(data.timestamp) }}
          </span>
        </template>

        <template #subsystem="{ data }">
          <span 
            class="px-1.5 py-0.5 rounded text-[10px] font-mono font-bold uppercase border"
            :class="getSubsystemMeta(data.subsystem).color"
          >
            {{ data.subsystem }}
          </span>
        </template>

        <template #eventType="{ data }">
          <span class="font-mono text-xs text-slate-700">{{ data.eventType }}</span>
        </template>

        <template #operation="{ data }">
          <span class="text-xs text-slate-900 font-medium">{{ data.operation }}</span>
        </template>

        <template #correlationId="{ data }">
          <span class="font-mono text-xs text-sky-700 truncate max-w-[140px] block">
            {{ data.correlationId || 'N/A' }}
          </span>
        </template>

        <template #duration="{ data }">
          <span class="font-mono text-xs text-slate-600 whitespace-nowrap">
            {{ formatDuration(data.durationMs) }}
          </span>
        </template>

        <template #status="{ data }">
          <IrisStatus :status="data.status" label-format="upper" size="sm" />
        </template>

        <template #actions="{ data }">
          <button
            type="button"
            class="text-slate-400 hover:text-sky-600 transition-colors cursor-pointer p-1"
            title="Inspect Event"
            @click.stop="emit('select', data)"
          >
            <ChevronRight :size="15" />
          </button>
        </template>
      </IrisDataTable>
    </div>
  </div>
</template>
