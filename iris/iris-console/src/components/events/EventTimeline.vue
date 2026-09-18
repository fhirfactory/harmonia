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
import { ref, computed } from 'vue';
import type { OperationalEvent } from '../../models/operations';
import StatusBadge from '../common/StatusBadge.vue';
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

function getSubsystemMeta(subsystem?: string) {
  const s = (subsystem || '').toLowerCase();
  switch (s) {
    case 'pylai':
      return { label: 'PYLAI', color: 'text-purple-400 bg-purple-500/10 border-purple-500/20', icon: Radio };
    case 'petasos':
      return { label: 'PETASOS', color: 'text-amber-400 bg-amber-500/10 border-amber-500/20', icon: Radio };
    case 'energeia':
      return { label: 'ENERGEIA', color: 'text-emerald-400 bg-emerald-500/10 border-emerald-500/20', icon: Cpu };
    case 'mnemosyne':
      return { label: 'MNEMOSYNE', color: 'text-blue-400 bg-blue-500/10 border-blue-500/20', icon: Database };
    case 'mneme':
      return { label: 'MNEME', color: 'text-indigo-400 bg-indigo-500/10 border-indigo-500/20', icon: Layers };
    case 'agora':
      return { label: 'AGORA', color: 'text-pink-400 bg-pink-500/10 border-pink-500/20', icon: MessagesSquare };
    case 'themis':
      return { label: 'THEMIS', color: 'text-rose-400 bg-rose-500/10 border-rose-500/20', icon: Shield };
    case 'calliope':
      return { label: 'CALLIOPE', color: 'text-teal-400 bg-teal-500/10 border-teal-500/20', icon: FileCode };
    case 'iris':
      return { label: 'IRIS', color: 'text-sky-400 bg-sky-500/10 border-sky-500/20', icon: LayoutDashboard };
    default:
      return { label: (subsystem || 'UNKNOWN').toUpperCase(), color: 'text-slate-400 bg-slate-800 border-slate-700', icon: Activity };
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
  <div class="space-y-4" aria-label="Operational Events Timeline">
    <!-- View Mode Toggle & Event Summary Header -->
    <div class="flex items-center justify-between pb-1">
      <div class="flex items-center gap-2">
        <GitCommit :size="18" class="text-sky-400" />
        <h3 class="text-sm font-bold text-white tracking-wide">
          Interaction Sequence Flow
        </h3>
        <span class="px-2 py-0.5 rounded-full text-xs font-mono bg-slate-800 text-slate-300 border border-slate-700">
          {{ events.length }} {{ events.length === 1 ? 'Hop' : 'Hops' }}
        </span>
      </div>

      <div class="flex items-center gap-1 bg-slate-950 p-0.5 rounded-lg border border-slate-800">
        <button
          type="button"
          class="px-2.5 py-1 text-xs font-medium rounded-md flex items-center gap-1.5 transition-colors"
          :class="viewMode === 'flow' ? 'bg-sky-500 text-white font-semibold' : 'text-slate-400 hover:text-white'"
          @click="viewMode = 'flow'"
        >
          <GitCommit :size="13" />
          <span>Flowchart</span>
        </button>
        <button
          type="button"
          class="px-2.5 py-1 text-xs font-medium rounded-md flex items-center gap-1.5 transition-colors"
          :class="viewMode === 'table' ? 'bg-sky-500 text-white font-semibold' : 'text-slate-400 hover:text-white'"
          @click="viewMode = 'table'"
        >
          <List :size="13" />
          <span>Table</span>
        </button>
      </div>
    </div>

    <!-- Loading Skeleton -->
    <div v-if="loading" class="p-12 text-center space-y-3 card">
      <div class="inline-block animate-spin rounded-full h-8 w-8 border-2 border-sky-400 border-t-transparent"></div>
      <p class="text-xs text-slate-400 font-medium">Tracing interaction hops across Harmonia subsystems...</p>
    </div>

    <!-- Honest Empty State -->
    <div v-else-if="events.length === 0" class="card p-12 text-center space-y-3 border-dashed border-slate-800">
      <FileSearch :size="38" class="mx-auto text-slate-500" />
      <h4 class="text-sm font-bold text-white">No Diagnostic Events Recorded</h4>
      <p class="text-xs text-slate-400 max-w-md mx-auto leading-relaxed">
        No interaction events matched the specified search criteria or time window. Search by a known Correlation ID to trace cross-subsystem message flows.
      </p>
    </div>

    <!-- 1. Vertical Flowchart View -->
    <div v-else-if="viewMode === 'flow'" class="relative pl-6 sm:pl-8 space-y-4">
      <!-- Continuous vertical guideline -->
      <div class="absolute left-3 sm:left-4 top-4 bottom-4 w-0.5 bg-gradient-to-b from-sky-500 via-indigo-500/60 to-slate-800 -translate-x-1/2"></div>

      <div 
        v-for="(event, index) in events" 
        :key="event.eventId"
        class="relative"
      >
        <!-- Sequence Node Marker on the vertical line -->
        <div class="absolute -left-6 sm:-left-8 top-5 w-7 h-7 rounded-full bg-slate-950 border-2 border-sky-500 flex items-center justify-center -translate-x-1/2 z-10 shadow-md">
          <span class="text-[10px] font-mono font-bold text-sky-400">{{ index + 1 }}</span>
        </div>

        <!-- Event Hop Card -->
        <div 
          class="card p-4 hover:border-sky-500/60 transition-all cursor-pointer bg-slate-900/90 shadow-md group hover:bg-slate-900"
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

                <span class="text-xs font-bold text-white group-hover:text-sky-300 transition-colors">
                  {{ event.operation }}
                </span>

                <span class="text-[11px] font-mono text-slate-400 bg-slate-950 px-2 py-0.5 rounded border border-slate-800">
                  {{ event.eventType }}
                </span>
              </div>

              <!-- Identifiers summary row -->
              <div class="flex flex-wrap items-center gap-2 pt-1 text-[11px] font-mono text-slate-400">
                <span v-if="event.correlationId" class="text-sky-400">
                  corr: {{ event.correlationId }}
                </span>
                <span v-if="event.messageId" class="text-slate-500">
                  • msg: {{ event.messageId }}
                </span>
                <span v-if="event.pragmaId" class="text-slate-500">
                  • pragma: {{ event.pragmaId }}
                </span>
                <span v-if="event.ergonId" class="text-purple-400">
                  • ergon: {{ event.ergonId }}
                </span>
              </div>
            </div>

            <!-- Right Side: Status Badge, Timing, Duration -->
            <div class="flex flex-col items-end gap-1.5 text-right">
              <div class="flex items-center gap-2">
                <span 
                  class="text-[10px] font-mono px-1.5 py-0.5 rounded bg-slate-950 text-slate-300 border border-slate-800"
                  title="Execution Duration"
                >
                  {{ formatDuration(event.durationMs) }}
                </span>
                <StatusBadge :status="event.status" size="sm" />
              </div>

              <div class="flex items-center gap-1.5 text-[11px] font-mono text-slate-400">
                <Clock :size="11" class="text-slate-500" />
                <span>{{ formatTime(event.timestamp) }}</span>
                <span v-if="calculateDelta(index)" class="text-sky-400 font-semibold">
                  ({{ calculateDelta(index) }})
                </span>
              </div>
            </div>
          </div>
        </div>

        <!-- Directional Down Arrow between hops -->
        <div v-if="index < events.length - 1" class="flex items-center justify-start pl-4 py-1">
          <ArrowDown :size="14" class="text-sky-400/80 -translate-x-1/2" />
        </div>
      </div>
    </div>

    <!-- 2. Dense Tabular View -->
    <div v-else class="card p-0 overflow-hidden border border-slate-800">
      <div class="table-container">
        <table class="table" role="table" aria-label="Operational Events Table">
          <thead>
            <tr>
              <th scope="col" class="w-12">#</th>
              <th scope="col">Timestamp</th>
              <th scope="col">Subsystem</th>
              <th scope="col">Event Type</th>
              <th scope="col">Operation</th>
              <th scope="col">Correlation ID</th>
              <th scope="col">Duration</th>
              <th scope="col">Status</th>
              <th scope="col" class="w-10"></th>
            </tr>
          </thead>
          <tbody>
            <tr 
              v-for="(event, idx) in events" 
              :key="event.eventId"
              class="cursor-pointer hover:bg-slate-800/50 transition-colors"
              @click="emit('select', event)"
            >
              <td class="font-mono text-xs text-slate-500">{{ idx + 1 }}</td>
              <td class="font-mono text-xs text-slate-300 whitespace-nowrap">
                {{ formatTime(event.timestamp) }}
              </td>
              <td>
                <span 
                  class="px-1.5 py-0.5 rounded text-[10px] font-mono font-bold uppercase border"
                  :class="getSubsystemMeta(event.subsystem).color"
                >
                  {{ event.subsystem }}
                </span>
              </td>
              <td class="font-mono text-xs text-slate-300">{{ event.eventType }}</td>
              <td class="text-xs text-white font-medium">{{ event.operation }}</td>
              <td class="font-mono text-xs text-sky-400 truncate max-w-[140px]">
                {{ event.correlationId || 'N/A' }}
              </td>
              <td class="font-mono text-xs text-slate-300 whitespace-nowrap">
                {{ formatDuration(event.durationMs) }}
              </td>
              <td>
                <StatusBadge :status="event.status" size="sm" />
              </td>
              <td class="text-slate-500">
                <ChevronRight :size="15" />
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>
