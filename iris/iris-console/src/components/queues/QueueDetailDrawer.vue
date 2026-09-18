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
import type { QueueSummary } from '../../models/operations';
import StatusBadge from '../common/StatusBadge.vue';
import SvgTimeSeriesChart from '../common/SvgTimeSeriesChart.vue';
import { 
  X, 
  Radio, 
  Activity, 
  Users, 
  ShieldCheck, 
  Clock, 
  AlertOctagon, 
  RefreshCw,
  Server,
  Layers
} from 'lucide-vue-next';

const props = defineProps<{
  queue: QueueSummary | null;
  isOpen: boolean;
}>();

const emit = defineEmits<{
  (e: 'close'): void;
}>();

function handleKeydown(e: KeyboardEvent) {
  if (e.key === 'Escape' && props.isOpen) {
    emit('close');
  }
}

onMounted(() => {
  window.addEventListener('keydown', handleKeydown);
});

onUnmounted(() => {
  window.removeEventListener('keydown', handleKeydown);
});

function formatRate(rate?: number | null): string {
  if (rate == null || isNaN(rate)) return 'N/A';
  return `${rate.toFixed(1)} msg/sec`;
}

function formatAge(seconds?: number | null): string {
  if (seconds == null || isNaN(seconds)) return 'N/A';
  if (seconds === 0) return '0s (no queued messages)';
  if (seconds < 60) return `${seconds}s`;
  const mins = Math.floor(seconds / 60);
  const secs = seconds % 60;
  if (mins < 60) return `${mins}m ${secs}s`;
  const hrs = Math.floor(mins / 60);
  return `${hrs}h ${mins % 60}m`;
}

function formatCount(val?: number | null): string {
  if (val == null || isNaN(val)) return 'N/A';
  return val.toLocaleString();
}

const depthPoints = computed(() => {
  if (!props.queue?.depthHistory || props.queue.depthHistory.length < 2) {
    return [];
  }
  return props.queue.depthHistory;
});
</script>

<template>
  <div v-if="isOpen && queue" class="fixed inset-0 z-50 overflow-hidden" role="dialog" aria-modal="true" :aria-label="`Queue Details: ${queue.queueName}`">
    <!-- Backdrop -->
    <div 
      class="fixed inset-0 bg-slate-950/70 backdrop-blur-sm transition-opacity" 
      aria-hidden="true" 
      @click="emit('close')"
    ></div>

    <div class="fixed inset-y-0 right-0 max-w-full flex pl-10">
      <div class="w-screen max-w-2xl bg-slate-900 border-l border-slate-800 shadow-2xl flex flex-col">
        <!-- Drawer Header -->
        <div class="p-6 border-b border-slate-800 bg-slate-950/60 flex items-start justify-between">
          <div class="space-y-1 pr-4">
            <div class="flex items-center gap-2">
              <Radio :size="20" class="text-sky-400" />
              <h2 class="text-lg font-bold text-white font-mono break-all">{{ queue.queueName }}</h2>
            </div>
            <div class="flex flex-wrap items-center gap-2 pt-1">
              <StatusBadge :status="queue.status || 'UNKNOWN'" size="sm" />
              <span v-if="queue.associatedCapability" class="px-2 py-0.5 rounded text-xs bg-slate-800 text-slate-300 font-sans border border-slate-700/60">
                {{ queue.associatedCapability }}
              </span>
              <span class="px-2 py-0.5 rounded text-xs font-mono bg-purple-500/10 text-purple-300 border border-purple-500/20">
                Petasos / Artemis
              </span>
            </div>
          </div>
          <button 
            type="button" 
            class="p-2 rounded-lg text-slate-400 hover:text-white hover:bg-slate-800 transition-colors"
            aria-label="Close queue details drawer"
            @click="emit('close')"
          >
            <X :size="20" />
          </button>
        </div>

        <!-- Drawer Content -->
        <div class="flex-1 overflow-y-auto p-6 space-y-6">
          <!-- Zero PHI Invariant Banner -->
          <div class="p-3 rounded-lg bg-emerald-500/10 border border-emerald-500/20 flex items-center gap-3">
            <ShieldCheck :size="22" class="text-emerald-400 shrink-0" />
            <div class="text-xs text-slate-300">
              <span class="font-bold text-emerald-400">Zero-PHI Safe Telemetry:</span>
              Message bodies and clinical payloads are strictly excluded per Harmonia Invariant 7. Only operational rates and queue depths are reported.
            </div>
          </div>

          <!-- Key Metrics Grid -->
          <div class="grid grid-cols-2 sm:grid-cols-3 gap-3">
            <div class="p-3 rounded-lg bg-slate-800/40 border border-slate-800">
              <span class="text-[11px] font-semibold text-slate-400 block uppercase">Current Depth</span>
              <div 
                class="text-xl font-bold font-mono mt-1"
                :class="queue.depth > 0 ? 'text-amber-400' : 'text-slate-200'"
              >
                {{ formatCount(queue.depth) }}
              </div>
              <span class="text-[10px] text-slate-500">Messages queued</span>
            </div>

            <div class="p-3 rounded-lg bg-slate-800/40 border border-slate-800">
              <span class="text-[11px] font-semibold text-slate-400 block uppercase">Consumers</span>
              <div 
                class="text-xl font-bold font-mono mt-1"
                :class="queue.consumerCount > 0 ? 'text-emerald-400' : 'text-slate-400'"
              >
                {{ formatCount(queue.consumerCount) }}
              </div>
              <span class="text-[10px] text-slate-500">Active listeners</span>
            </div>

            <div class="p-3 rounded-lg bg-slate-800/40 border border-slate-800">
              <span class="text-[11px] font-semibold text-slate-400 block uppercase">Producers</span>
              <div class="text-xl font-bold font-mono mt-1 text-slate-200">
                {{ formatCount(queue.producerCount) }}
              </div>
              <span class="text-[10px] text-slate-500">Attached senders</span>
            </div>

            <div class="p-3 rounded-lg bg-slate-800/40 border border-slate-800">
              <span class="text-[11px] font-semibold text-slate-400 block uppercase">Enqueue Rate</span>
              <div class="text-base font-bold font-mono mt-1 text-sky-400">
                {{ formatRate(queue.enqueueRate) }}
              </div>
              <span class="text-[10px] text-slate-500">Incoming throughput</span>
            </div>

            <div class="p-3 rounded-lg bg-slate-800/40 border border-slate-800">
              <span class="text-[11px] font-semibold text-slate-400 block uppercase">Dequeue Rate</span>
              <div class="text-base font-bold font-mono mt-1 text-sky-400">
                {{ formatRate(queue.dequeueRate) }}
              </div>
              <span class="text-[10px] text-slate-500">Consumption throughput</span>
            </div>

            <div class="p-3 rounded-lg bg-slate-800/40 border border-slate-800">
              <span class="text-[11px] font-semibold text-slate-400 block uppercase">DLQ Depth</span>
              <div 
                class="text-base font-bold font-mono mt-1"
                :class="queue.dlqDepth > 0 ? 'text-rose-400' : 'text-slate-400'"
              >
                {{ formatCount(queue.dlqDepth) }}
              </div>
              <span class="text-[10px] text-slate-500">Dead lettered messages</span>
            </div>
          </div>

          <!-- Historical Depth Trend -->
          <div class="card p-4 border border-slate-800 bg-slate-800/20 space-y-3">
            <div class="flex items-center justify-between">
              <span class="text-xs font-bold text-white uppercase tracking-wider flex items-center gap-1.5">
                <Activity :size="14" class="text-sky-400" />
                Historical Depth Trend
              </span>
              <span class="text-[11px] text-slate-400 font-mono">10m Window</span>
            </div>

            <div class="pt-2">
              <SvgTimeSeriesChart 
                :points="depthPoints" 
                stroke-color="#38bdf8" 
                fill-color="#38bdf8" 
                :height="80" 
                empty-text="No historical depth samples recorded"
                :show-min-max="true"
                unit=" msgs"
                metric-name="Queue Depth"
              />
            </div>
          </div>

          <!-- Extended Operational Diagnostics -->
          <div class="space-y-3">
            <h4 class="text-xs font-bold uppercase tracking-wider text-slate-400">
              Operational Attributes &amp; Latency
            </h4>

            <div class="divide-y divide-slate-800/60 rounded-lg border border-slate-800 bg-slate-950/40 text-xs">
              <div class="p-3 flex items-center justify-between">
                <span class="text-slate-400 flex items-center gap-1.5">
                  <Clock :size="13" />
                  Oldest Message Age
                </span>
                <span class="font-mono text-slate-200">{{ formatAge(queue.oldestMessageAgeSeconds) }}</span>
              </div>

              <div class="p-3 flex items-center justify-between">
                <span class="text-slate-400 flex items-center gap-1.5">
                  <RefreshCw :size="13" />
                  Redeliveries
                </span>
                <span 
                  class="font-mono"
                  :class="queue.redeliveryCount > 0 ? 'text-amber-400 font-bold' : 'text-slate-400'"
                >
                  {{ formatCount(queue.redeliveryCount) }}
                </span>
              </div>

              <div class="p-3 flex items-center justify-between">
                <span class="text-slate-400 flex items-center gap-1.5">
                  <AlertOctagon :size="13" />
                  Expired Messages
                </span>
                <span 
                  class="font-mono"
                  :class="queue.expiryCount > 0 ? 'text-rose-400 font-bold' : 'text-slate-400'"
                >
                  {{ formatCount(queue.expiryCount) }}
                </span>
              </div>

              <div class="p-3 flex items-center justify-between">
                <span class="text-slate-400 flex items-center gap-1.5">
                  <Layers :size="13" />
                  Broker Address Binding
                </span>
                <span class="font-mono text-sky-400 truncate max-w-xs">{{ queue.address || queue.queueName }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- Drawer Footer -->
        <div class="p-4 border-t border-slate-800 bg-slate-950/70 flex justify-end">
          <button 
            type="button" 
            class="px-4 py-2 rounded-lg bg-slate-800 hover:bg-slate-700 text-white text-xs font-medium transition-colors"
            @click="emit('close')"
          >
            Close Details
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
