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
import { IrisStatus } from '@harmonia/iris-befe';
import SvgTimeSeriesChart from '../common/SvgTimeSeriesChart.vue';
import { 
  X, 
  Radio, 
  Activity, 
  ShieldCheck, 
  Clock 
} from 'lucide-vue-next';

const props = withDefaults(
  defineProps<{
    queue: QueueSummary | null;
    isOpen: boolean;
    teleport?: boolean;
  }>(),
  {
    queue: null,
    isOpen: false,
    teleport: false
  }
);

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
  <teleport to="body" :disabled="!teleport">
    <div v-if="isOpen && queue" class="fixed inset-0 z-50 overflow-hidden" role="dialog" aria-modal="true" :aria-label="`Queue Details: ${queue.queueName}`">
      <!-- Backdrop -->
      <div 
        class="fixed inset-0 bg-slate-900/40 backdrop-blur-xs transition-opacity" 
        aria-hidden="true" 
        @click="emit('close')"
      ></div>

      <aside class="fixed inset-y-0 right-0 max-w-full w-full sm:w-[500px] lg:w-[580px] bg-white border-l border-slate-200 z-50 flex flex-col shadow-2xl transition-transform transform duration-300 ease-in-out font-sans text-slate-800">
        <!-- Drawer Header -->
        <div class="px-6 py-4 bg-slate-50 border-b border-slate-200 flex items-center justify-between">
          <div class="flex items-center gap-3 min-w-0">
            <div class="p-2 rounded-md bg-sky-50 border border-sky-100 text-sky-700 shrink-0">
              <Radio :size="20" />
            </div>
            <div class="min-w-0 pr-2">
              <h2 class="text-base font-bold text-slate-900 tracking-tight font-mono break-all leading-tight">
                {{ queue.queueName }}
              </h2>
              <p v-if="queue.associatedCapability" class="text-xs text-slate-500 mt-0.5 truncate font-sans">
                {{ queue.associatedCapability }}
              </p>
            </div>
          </div>

          <button 
            type="button"
            class="p-2 text-slate-400 hover:text-slate-700 hover:bg-slate-200/60 rounded-md transition cursor-pointer shrink-0"
            aria-label="Close queue details drawer"
            title="Close drawer (ESC)"
            @click="emit('close')"
          >
            <X :size="18" />
          </button>
        </div>

        <!-- Drawer Body -->
        <div class="flex-1 overflow-y-auto p-6 space-y-6">
          <!-- Status & Capability Banner -->
          <div class="p-4 rounded-lg bg-slate-50 border border-slate-200 flex items-center justify-between shadow-xs">
            <div>
              <span class="text-[11px] font-bold text-slate-500 uppercase tracking-wider block mb-1">Queue Status</span>
              <IrisStatus :status="queue.status || 'UNKNOWN'" size="md" :show-pulse="true" label-format="upper" />
            </div>

            <div class="text-right">
              <span class="text-[11px] font-bold text-slate-500 uppercase tracking-wider block mb-1">Protocol / Address</span>
              <span class="text-xs font-mono font-semibold text-slate-800">
                {{ queue.address || queue.queueName }}
              </span>
            </div>
          </div>

          <!-- Metrics Grid -->
          <div class="space-y-3">
            <h3 class="text-xs font-bold text-slate-800 uppercase tracking-wider flex items-center gap-1.5">
              <Activity :size="14" class="text-sky-600" />
              <span>Messaging Telemetry</span>
            </h3>

            <div class="grid grid-cols-2 sm:grid-cols-4 gap-2">
              <!-- Current Depth -->
              <div class="p-3 bg-slate-50 border border-slate-200 rounded-lg text-center shadow-xs">
                <span class="text-[10px] text-slate-500 uppercase font-bold block mb-0.5">Current Depth</span>
                <span 
                  class="text-lg font-bold font-mono block"
                  :class="queue.depth && queue.depth > 50 ? 'text-rose-700' : 'text-slate-900'"
                >
                  {{ formatCount(queue.depth) }}
                </span>
              </div>

              <!-- Consumers -->
              <div class="p-3 bg-slate-50 border border-slate-200 rounded-lg text-center shadow-xs">
                <span class="text-[10px] text-slate-500 uppercase font-bold block mb-0.5">Consumers</span>
                <span class="text-lg font-bold font-mono text-emerald-700 block">
                  {{ formatCount(queue.consumerCount) }}
                </span>
              </div>

              <!-- Producers -->
              <div class="p-3 bg-slate-50 border border-slate-200 rounded-lg text-center shadow-xs">
                <span class="text-[10px] text-slate-500 uppercase font-bold block mb-0.5">Producers</span>
                <span class="text-lg font-bold font-mono text-slate-800 block">
                  {{ formatCount(queue.producerCount) }}
                </span>
              </div>

              <!-- DLQ Messages -->
              <div class="p-3 bg-slate-50 border border-slate-200 rounded-lg text-center shadow-xs">
                <span class="text-[10px] text-slate-500 uppercase font-bold block mb-0.5">DLQ Messages</span>
                <span 
                  class="text-lg font-bold font-mono block"
                  :class="queue.dlqDepth && queue.dlqDepth > 0 ? 'text-rose-700' : 'text-slate-500'"
                >
                  {{ formatCount(queue.dlqDepth) }}
                </span>
              </div>
            </div>
          </div>

          <!-- Throughput Rates & Age -->
          <div class="bg-white border border-slate-200 rounded-lg p-4 space-y-2 text-xs font-mono shadow-xs">
            <div class="flex justify-between py-1 border-b border-slate-100">
              <span class="text-slate-500 font-sans">Enqueue Rate:</span>
              <span class="text-sky-700 font-semibold">{{ formatRate(queue.enqueueRate) }}</span>
            </div>
            <div class="flex justify-between py-1 border-b border-slate-100">
              <span class="text-slate-500 font-sans">Dequeue Rate:</span>
              <span class="text-emerald-700 font-semibold">{{ formatRate(queue.dequeueRate) }}</span>
            </div>
            <div class="flex justify-between py-1 border-b border-slate-100">
              <span class="text-slate-500 font-sans">Oldest Message Age:</span>
              <span class="text-slate-800">{{ formatAge(queue.oldestMessageAgeSeconds) }}</span>
            </div>
            <div class="flex justify-between py-1">
              <span class="text-slate-500 font-sans">Associated Capability:</span>
              <span class="text-slate-800 font-sans">{{ queue.associatedCapability || 'Petasos Internal Transport' }}</span>
            </div>
          </div>

          <!-- Historical Sparkline Chart -->
          <div class="space-y-3">
            <h3 class="text-xs font-bold text-slate-800 uppercase tracking-wider flex items-center justify-between">
              <span class="flex items-center gap-1.5">
                <Clock :size="14" class="text-emerald-600" />
                <span>Historical Depth Trend</span>
              </span>
              <span class="text-[10px] text-slate-500 font-normal">Last 30 data points</span>
            </h3>

            <div class="p-4 bg-slate-50 border border-slate-200 rounded-lg shadow-xs">
              <div v-if="depthPoints.length > 0">
                <SvgTimeSeriesChart 
                  :data="depthPoints" 
                  color="#0284c7" 
                  :height="100" 
                  unit=" msgs"
                />
              </div>
              <div v-else class="py-6 text-center text-xs text-slate-500 italic">
                Awaiting time-series depth polling data...
              </div>
            </div>
          </div>

          <!-- Zero-PHI Boundary Notice -->
          <div class="p-3 rounded-lg bg-sky-50 border border-sky-200 text-xs text-sky-900 flex items-start gap-2.5">
            <ShieldCheck :size="16" class="text-sky-700 shrink-0 mt-0.5" />
            <div class="space-y-0.5">
              <p class="font-bold">Zero-PHI Safe Telemetry</p>
              <p class="text-[11px] text-sky-800/80 leading-relaxed font-sans">
                Petasos treats payload buffers as opaque streams. No patient names, MRNs, or clinical observations are logged or inspected.
              </p>
            </div>
          </div>
        </div>
      </aside>
    </div>
  </teleport>
</template>
