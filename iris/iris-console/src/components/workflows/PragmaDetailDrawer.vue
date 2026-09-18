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
import type { PragmaSummary } from '../../models/operations';
import StatusBadge from '../common/StatusBadge.vue';
import { 
  X, 
  GitMerge, 
  Clock, 
  ShieldCheck, 
  Layers, 
  Activity, 
  CheckCircle2, 
  AlertCircle, 
  RefreshCw,
  Copy,
  Check
} from 'lucide-vue-next';
import { ref } from 'vue';

const props = defineProps<{
  pragma: PragmaSummary | null;
  isOpen: boolean;
}>();

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
  <div v-if="isOpen && pragma" class="fixed inset-0 z-50 overflow-hidden" role="dialog" aria-modal="true" :aria-label="`Pragma Details: ${pragma.pragmaId}`">
    <!-- Backdrop -->
    <div 
      class="fixed inset-0 bg-slate-950/70 backdrop-blur-sm transition-opacity" 
      aria-hidden="true" 
      @click="emit('close')"
    ></div>

    <div class="fixed inset-y-0 right-0 max-w-full flex pl-10">
      <div class="w-screen max-w-2xl bg-slate-900 border-l border-slate-800 shadow-2xl flex flex-col">
        <!-- Header -->
        <div class="p-6 border-b border-slate-800 bg-slate-950/60 flex items-start justify-between">
          <div class="space-y-1 pr-4">
            <div class="flex items-center gap-2">
              <GitMerge :size="20" class="text-sky-400" />
              <h2 class="text-lg font-bold text-white font-mono break-all">{{ pragma.pragmaId }}</h2>
            </div>
            <div class="flex flex-wrap items-center gap-2 pt-1">
              <StatusBadge :status="pragma.status || 'UNKNOWN'" size="sm" />
              <span class="px-2 py-0.5 rounded text-xs font-mono bg-sky-500/10 text-sky-300 border border-sky-500/20">
                {{ pragma.praxisId || 'Praxis Workflow' }}
              </span>
              <span class="px-2 py-0.5 rounded text-xs font-mono bg-purple-500/10 text-purple-300 border border-purple-500/20">
                Pragma Envelope
              </span>
            </div>
          </div>
          <button 
            type="button" 
            class="p-2 rounded-lg text-slate-400 hover:text-white hover:bg-slate-800 transition-colors"
            aria-label="Close pragma details drawer"
            @click="emit('close')"
          >
            <X :size="20" />
          </button>
        </div>

        <!-- Content -->
        <div class="flex-1 overflow-y-auto p-6 space-y-6">
          <!-- Zero PHI Banner -->
          <div class="p-3 rounded-lg bg-emerald-500/10 border border-emerald-500/20 flex items-center gap-3">
            <ShieldCheck :size="22" class="text-emerald-400 shrink-0" />
            <div class="text-xs text-slate-300">
              <span class="font-bold text-emerald-400">Zero-PHI Safe Telemetry:</span>
              Clinical FHIR/HL7 content is omitted per Harmonia Invariant 7. Only execution state, timing, and correlation IDs are displayed.
            </div>
          </div>

          <!-- Safe Execution Metadata Grid -->
          <div class="grid grid-cols-2 sm:grid-cols-3 gap-3">
            <div class="p-3 rounded-lg bg-slate-800/40 border border-slate-800">
              <span class="text-[11px] font-semibold text-slate-400 block uppercase">Started At</span>
              <div class="text-sm font-bold font-mono mt-1 text-slate-200">
                {{ formatTime(pragma.startedAt) }}
              </div>
              <span class="text-[10px] text-slate-500">Initiation timestamp</span>
            </div>

            <div class="p-3 rounded-lg bg-slate-800/40 border border-slate-800">
              <span class="text-[11px] font-semibold text-slate-400 block uppercase">Execution Duration</span>
              <div class="text-sm font-bold font-mono mt-1 text-sky-400">
                {{ formatDuration(pragma.durationMs) }}
              </div>
              <span class="text-[10px] text-slate-500">Total elapsed time</span>
            </div>

            <div class="p-3 rounded-lg bg-slate-800/40 border border-slate-800">
              <span class="text-[11px] font-semibold text-slate-400 block uppercase">Current Ergon</span>
              <div class="text-sm font-bold font-mono mt-1 text-slate-200 truncate" :title="pragma.currentErgon || 'None'">
                {{ pragma.currentErgon || 'None' }}
              </div>
              <span class="text-[10px] text-slate-500">Active activity unit</span>
            </div>

            <div class="p-3 rounded-lg bg-slate-800/40 border border-slate-800">
              <span class="text-[11px] font-semibold text-slate-400 block uppercase">Completed Erga</span>
              <div class="text-sm font-bold font-mono mt-1 text-emerald-400">
                {{ pragma.completedErgaCount }}
              </div>
              <span class="text-[10px] text-slate-500">Finished checkpoints</span>
            </div>

            <div class="p-3 rounded-lg bg-slate-800/40 border border-slate-800">
              <span class="text-[11px] font-semibold text-slate-400 block uppercase">Retries</span>
              <div 
                class="text-sm font-bold font-mono mt-1"
                :class="pragma.retryCount > 0 ? 'text-amber-400 font-bold' : 'text-slate-400'"
              >
                {{ pragma.retryCount }}
              </div>
              <span class="text-[10px] text-slate-500">Retry attempts</span>
            </div>

            <div class="p-3 rounded-lg bg-slate-800/40 border border-slate-800">
              <span class="text-[11px] font-semibold text-slate-400 block uppercase">Reason Code</span>
              <div 
                class="text-sm font-bold font-mono mt-1 truncate"
                :class="pragma.failureReasonCode ? 'text-rose-400' : 'text-slate-500'"
                :title="pragma.failureReasonCode || 'N/A'"
              >
                {{ pragma.failureReasonCode || 'N/A' }}
              </div>
              <span class="text-[10px] text-slate-500">Error diagnostic code</span>
            </div>
          </div>

          <!-- Trace Identifiers -->
          <div class="space-y-2">
            <h4 class="text-xs font-bold uppercase tracking-wider text-slate-400">
              Trace &amp; Correlation Identifiers
            </h4>

            <div class="rounded-lg border border-slate-800 bg-slate-950/40 divide-y divide-slate-800/60 text-xs font-mono">
              <div class="p-3 flex items-center justify-between gap-2">
                <span class="text-slate-400 font-sans">Correlation ID:</span>
                <div class="flex items-center gap-2 truncate">
                  <span class="text-sky-400 truncate">{{ pragma.correlationId || 'N/A' }}</span>
                  <button 
                    v-if="pragma.correlationId"
                    type="button" 
                    class="p-1 rounded text-slate-400 hover:text-white hover:bg-slate-800 transition-colors"
                    title="Copy Correlation ID"
                    @click="copyText(pragma.correlationId, 'corr')"
                  >
                    <Check v-if="copiedField === 'corr'" :size="13" class="text-emerald-400" />
                    <Copy v-else :size="13" />
                  </button>
                </div>
              </div>

              <div class="p-3 flex items-center justify-between gap-2">
                <span class="text-slate-400 font-sans">Causation ID:</span>
                <div class="flex items-center gap-2 truncate">
                  <span class="text-slate-300 truncate">{{ pragma.causationId || 'N/A' }}</span>
                  <button 
                    v-if="pragma.causationId"
                    type="button" 
                    class="p-1 rounded text-slate-400 hover:text-white hover:bg-slate-800 transition-colors"
                    title="Copy Causation ID"
                    @click="copyText(pragma.causationId, 'cause')"
                  >
                    <Check v-if="copiedField === 'cause'" :size="13" class="text-emerald-400" />
                    <Copy v-else :size="13" />
                  </button>
                </div>
              </div>
            </div>
          </div>

          <!-- Ergon Execution Checkpoints -->
          <div class="space-y-3">
            <div class="flex items-center justify-between">
              <h4 class="text-xs font-bold uppercase tracking-wider text-white flex items-center gap-1.5">
                <Layers :size="14" class="text-sky-400" />
                Ergon Checkpoint Progression
              </h4>
              <span class="text-[11px] text-slate-400 font-mono">
                {{ pragma.checkpoints?.length || 0 }} Checkpoints
              </span>
            </div>

            <div v-if="!pragma.checkpoints || pragma.checkpoints.length === 0" class="p-6 text-center text-xs text-slate-500 bg-slate-950/30 rounded-lg border border-slate-800">
              No Ergon checkpoints recorded for this execution instance.
            </div>

            <div v-else class="space-y-2">
              <div 
                v-for="(cp, idx) in pragma.checkpoints" 
                :key="cp.checkpointId || idx"
                class="p-3 rounded-lg border border-slate-800 bg-slate-950/40 space-y-1.5"
              >
                <div class="flex items-center justify-between gap-2">
                  <div class="flex items-center gap-2">
                    <span class="w-5 h-5 rounded-full bg-slate-800 border border-slate-700 flex items-center justify-center text-[10px] font-mono text-slate-300 font-bold shrink-0">
                      {{ idx + 1 }}
                    </span>
                    <span class="font-bold text-xs text-slate-200">{{ cp.ergonName || cp.ergonId }}</span>
                  </div>
                  <StatusBadge :status="cp.status || 'UNKNOWN'" size="sm" />
                </div>

                <div class="flex items-center justify-between text-[11px] font-mono text-slate-400 pl-7">
                  <span class="text-slate-500">{{ cp.ergonId }}</span>
                  <span>{{ formatDuration(cp.durationMs) }}</span>
                </div>

                <div v-if="cp.detail || cp.errorMessage" class="text-xs text-slate-400 pl-7 pt-1 font-sans">
                  <span :class="cp.errorMessage ? 'text-rose-400' : 'text-slate-400'">
                    {{ cp.errorMessage || cp.detail }}
                  </span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Footer -->
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
