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
import type { PragmaSummary } from '../../models/operations';
import { IrisStatus } from '@harmonia/iris-befe';
import { 
  X, 
  GitMerge, 
  Clock, 
  ShieldCheck, 
  Layers, 
  Activity, 
  CheckCircle2, 
  AlertCircle, 
  Copy,
  Check
} from 'lucide-vue-next';

const props = withDefaults(
  defineProps<{
    pragma: PragmaSummary | null;
    isOpen: boolean;
    teleport?: boolean;
  }>(),
  {
    pragma: null,
    isOpen: false,
    teleport: false
  }
);

const emit = defineEmits<{
  (e: 'close'): void;
}>();

const copiedField = ref<string | null>(null);

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

function formatTime(timestamp?: number | null): string {
  if (!timestamp) return 'N/A';
  return new Date(timestamp).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', second: '2-digit' });
}

function formatDuration(ms?: number | null): string {
  if (ms == null || isNaN(ms)) return 'N/A';
  if (ms < 1000) return `${ms}ms`;
  return `${(ms / 1000).toFixed(2)}s`;
}

async function copyText(text: string | undefined, field: string) {
  if (!text) return;
  try {
    await navigator.clipboard.writeText(text);
    copiedField.value = field;
    setTimeout(() => {
      if (copiedField.value === field) {
        copiedField.value = null;
      }
    }, 2000);
  } catch (err) {
    console.warn('Clipboard write failed:', err);
  }
}
</script>

<template>
  <teleport to="body" :disabled="!teleport">
    <div v-if="isOpen && pragma" class="fixed inset-0 z-50 overflow-hidden" role="dialog" aria-modal="true" :aria-label="`Pragma Details: ${pragma.pragmaId}`">
      <!-- Backdrop -->
      <div 
        class="fixed inset-0 bg-slate-900/40 backdrop-blur-xs transition-opacity" 
        aria-hidden="true" 
        @click="emit('close')"
      ></div>

      <aside class="fixed inset-y-0 right-0 max-w-full w-full sm:w-[500px] lg:w-[600px] bg-white border-l border-slate-200 z-50 flex flex-col shadow-2xl transition-transform transform duration-300 ease-in-out font-sans text-slate-800">
        <!-- Header -->
        <div class="px-6 py-4 bg-slate-50 border-b border-slate-200 flex items-center justify-between">
          <div class="flex items-center gap-3 min-w-0">
            <div class="p-2 rounded-md bg-sky-50 border border-sky-100 text-sky-700 shrink-0">
              <GitMerge :size="20" />
            </div>
            <div class="min-w-0 pr-2">
              <h2 class="text-base font-bold text-slate-900 tracking-tight font-mono break-all leading-tight">
                {{ pragma.pragmaId }}
              </h2>
              <div class="flex items-center gap-2 mt-0.5">
                <span class="text-xs text-slate-500 font-mono">
                  {{ pragma.praxisId || 'Praxis Workflow' }}
                </span>
                <span class="text-slate-300">&bull;</span>
                <span class="text-xs text-sky-700 font-sans font-medium">Pragma Instance</span>
              </div>
            </div>
          </div>

          <button 
            type="button"
            class="p-2 text-slate-400 hover:text-slate-700 hover:bg-slate-200/60 rounded-md transition cursor-pointer shrink-0"
            aria-label="Close pragma details drawer"
            title="Close drawer (ESC)"
            @click="emit('close')"
          >
            <X :size="18" />
          </button>
        </div>

        <!-- Body -->
        <div class="flex-1 overflow-y-auto p-6 space-y-6">
          <!-- Status & Execution Info -->
          <div class="p-4 rounded-lg bg-slate-50 border border-slate-200 flex items-center justify-between shadow-xs">
            <div>
              <span class="text-[11px] font-bold text-slate-500 uppercase tracking-wider block mb-1">Execution Status</span>
              <IrisStatus :status="pragma.status || 'UNKNOWN'" size="md" :show-pulse="true" label-format="upper" />
            </div>

            <div class="text-right">
              <span class="text-[11px] font-bold text-slate-500 uppercase tracking-wider block mb-1">Execution Time</span>
              <div class="flex items-center gap-1.5 text-xs font-mono font-semibold text-slate-800 justify-end">
                <Clock :size="13" class="text-slate-400" />
                <span>{{ formatDuration(pragma.durationMs) }}</span>
              </div>
              <span class="text-[10px] text-slate-500 font-mono">Started: {{ formatTime(pragma.startedAt) }}</span>
            </div>
          </div>

          <!-- Distributed Tracing Identifiers (Zero-PHI Safe) -->
          <div class="space-y-3">
            <h3 class="text-xs font-bold text-slate-800 uppercase tracking-wider flex items-center gap-1.5">
              <Activity :size="14" class="text-sky-600" />
              <span>Distributed Correlation Identifiers</span>
            </h3>

            <div class="bg-white border border-slate-200 rounded-lg p-4 space-y-2.5 text-xs font-mono shadow-xs">
              <!-- Correlation ID -->
              <div class="flex items-center justify-between py-1 border-b border-slate-100">
                <span class="text-slate-500 font-sans">Correlation ID:</span>
                <div class="flex items-center gap-1.5">
                  <span class="text-slate-900 font-semibold select-all">{{ pragma.correlationId || 'N/A' }}</span>
                  <button 
                    v-if="pragma.correlationId"
                    type="button"
                    class="p-1 rounded hover:bg-slate-100 text-slate-400 hover:text-slate-700 transition cursor-pointer"
                    title="Copy correlation ID"
                    @click="copyText(pragma.correlationId, 'corr')"
                  >
                    <Check v-if="copiedField === 'corr'" :size="12" class="text-emerald-600" />
                    <Copy v-else :size="12" />
                  </button>
                </div>
              </div>

              <!-- Causation ID -->
              <div class="flex items-center justify-between py-1 border-b border-slate-100">
                <span class="text-slate-500 font-sans">Causation ID:</span>
                <div class="flex items-center gap-1.5">
                  <span class="text-slate-900 font-semibold select-all">{{ pragma.causationId || 'N/A' }}</span>
                  <button 
                    v-if="pragma.causationId"
                    type="button"
                    class="p-1 rounded hover:bg-slate-100 text-slate-400 hover:text-slate-700 transition cursor-pointer"
                    title="Copy causation ID"
                    @click="copyText(pragma.causationId, 'caus')"
                  >
                    <Check v-if="copiedField === 'caus'" :size="12" class="text-emerald-600" />
                    <Copy v-else :size="12" />
                  </button>
                </div>
              </div>

              <!-- Current Ergon Activity -->
              <div class="flex items-center justify-between py-1">
                <span class="text-slate-500 font-sans">Active Ergon Activity:</span>
                <span class="px-2 py-0.5 rounded text-[11px] font-semibold bg-sky-50 text-sky-800 border border-sky-200">
                  {{ pragma.currentErgon || 'None' }}
                </span>
              </div>
            </div>
          </div>

          <!-- Checkpoints Timeline -->
          <div class="space-y-3">
            <h3 class="text-xs font-bold text-slate-800 uppercase tracking-wider flex items-center justify-between">
              <span class="flex items-center gap-1.5">
                <Layers :size="14" class="text-emerald-600" />
                <span>Ergon Checkpoint Progression</span>
              </span>
              <span class="text-[10px] text-slate-500 font-normal">
                {{ pragma.completedErgaCount }} of {{ (pragma.checkpoints || []).length }} Erga completed
              </span>
            </h3>

            <div class="p-4 bg-slate-50 border border-slate-200 rounded-lg shadow-xs">
              <div v-if="pragma.checkpoints && pragma.checkpoints.length > 0" class="space-y-4">
                <div 
                  v-for="(cp, idx) in pragma.checkpoints" 
                  :key="cp.checkpointId || idx"
                  class="flex items-start gap-3 relative"
                >
                  <!-- Line connector -->
                  <div 
                    v-if="idx < pragma.checkpoints.length - 1" 
                    class="absolute left-3 top-6 bottom-0 w-0.5 -ml-px"
                    :class="cp.status === 'COMPLETED' ? 'bg-emerald-300' : 'bg-slate-200'"
                  ></div>

                  <!-- Icon Status -->
                  <div class="shrink-0 mt-0.5">
                    <div 
                      v-if="cp.status === 'COMPLETED'" 
                      class="w-6 h-6 rounded-full bg-emerald-50 text-emerald-700 flex items-center justify-center border border-emerald-200"
                    >
                      <CheckCircle2 :size="14" />
                    </div>
                    <div 
                      v-else-if="cp.status === 'RUNNING'" 
                      class="w-6 h-6 rounded-full bg-sky-50 text-sky-700 flex items-center justify-center border border-sky-200 animate-pulse"
                    >
                      <Activity :size="14" />
                    </div>
                    <div 
                      v-else-if="cp.status === 'FAILED'" 
                      class="w-6 h-6 rounded-full bg-rose-50 text-rose-700 flex items-center justify-center border border-rose-200"
                    >
                      <AlertCircle :size="14" />
                    </div>
                    <div 
                      v-else 
                      class="w-6 h-6 rounded-full bg-slate-100 text-slate-400 flex items-center justify-center border border-slate-200"
                    >
                      <div class="w-2 h-2 rounded-full bg-slate-300"></div>
                    </div>
                  </div>

                  <!-- Checkpoint Details -->
                  <div class="flex-1 min-w-0 pb-3">
                    <div class="flex items-center justify-between gap-2">
                      <span class="text-xs font-bold text-slate-900 font-mono truncate">
                        {{ cp.ergonName }}
                      </span>
                      <span class="text-[10px] font-mono text-slate-500">
                        {{ formatDuration(cp.durationMs) }}
                      </span>
                    </div>

                    <p v-if="cp.detail" class="text-[11px] text-slate-600 font-sans mt-0.5">
                      {{ cp.detail }}
                    </p>

                    <div class="flex items-center gap-3 mt-1 text-[10px] text-slate-400 font-mono">
                      <span v-if="cp.startedAt">Time: {{ formatTime(cp.startedAt) }}</span>
                    </div>
                  </div>
                </div>
              </div>

              <div v-else class="py-6 text-center text-xs text-slate-500 italic">
                No checkpoints recorded yet for this Pragma envelope.
              </div>
            </div>
          </div>

          <!-- Zero-PHI Boundary Notice -->
          <div class="p-3 rounded-lg bg-sky-50 border border-sky-200 text-xs text-sky-900 flex items-start gap-2.5">
            <ShieldCheck :size="16" class="text-sky-700 shrink-0 mt-0.5" />
            <div class="space-y-0.5">
              <p class="font-bold">Zero-PHI Safe Telemetry</p>
              <p class="text-[11px] text-sky-800/80 leading-relaxed font-sans">
                Pragma monitoring inspects pipeline stage checkpoints and envelope correlation tokens. Patient identifiers, demographics, and clinical observation records are excluded from telemetry envelopes.
              </p>
            </div>
          </div>
        </div>
      </aside>
    </div>
  </teleport>
</template>
