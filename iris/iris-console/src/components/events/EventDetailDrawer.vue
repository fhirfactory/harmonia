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
import { ref, onMounted, onUnmounted } from 'vue';
import type { OperationalEvent } from '../../models/operations';
import { IrisStatus } from '@harmonia/iris-befe';
import { 
  X, 
  Activity, 
  Copy, 
  Check, 
  GitCommit, 
  ShieldCheck, 
  Clock, 
  Layers, 
  AlertTriangle,
  ExternalLink
} from 'lucide-vue-next';

const props = withDefaults(
  defineProps<{
    event: OperationalEvent | null;
    isOpen: boolean;
    teleport?: boolean;
  }>(),
  {
    event: null,
    isOpen: false,
    teleport: false
  }
);

const emit = defineEmits<{
  (e: 'close'): void;
  (e: 'trace', correlationId: string): void;
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

function copyToClipboard(text?: string, fieldName?: string) {
  if (!text) return;
  navigator.clipboard.writeText(text);
  copiedField.value = fieldName || 'value';
  setTimeout(() => {
    copiedField.value = null;
  }, 2000);
}

function formatTime(timestamp?: number): string {
  if (!timestamp) return 'N/A';
  return new Date(timestamp).toLocaleString();
}
</script>

<template>
  <teleport to="body" :disabled="!teleport">
    <div v-if="isOpen && event" class="fixed inset-0 z-50 overflow-hidden font-sans" role="dialog" aria-modal="true" :aria-label="`Event Details: ${event.eventId}`">
      <!-- Backdrop -->
      <div 
        class="fixed inset-0 bg-slate-900/40 backdrop-blur-xs transition-opacity" 
        aria-hidden="true" 
        @click="emit('close')"
      ></div>

      <div class="fixed inset-y-0 right-0 max-w-full flex pl-10">
        <div class="w-screen max-w-2xl bg-white border-l border-slate-200 shadow-2xl flex flex-col">
          <!-- Drawer Header -->
          <div class="p-6 border-b border-slate-200 bg-slate-50 flex items-start justify-between">
            <div class="space-y-1 pr-4">
              <div class="flex items-center gap-2">
                <Activity :size="20" class="text-sky-600" />
                <h2 class="text-lg font-bold text-slate-900 font-mono break-all">{{ event.operation }}</h2>
              </div>
              <div class="flex flex-wrap items-center gap-2 pt-1">
                <IrisStatus :status="event.status || 'UNKNOWN'" label-format="upper" size="sm" />
                <span class="px-2 py-0.5 rounded text-xs font-mono font-bold bg-sky-50 text-sky-800 border border-sky-200 uppercase">
                  {{ (event.subsystem || '').toUpperCase() }}
                </span>
                <span class="px-2 py-0.5 rounded text-xs font-mono bg-slate-100 text-slate-700 border border-slate-200">
                  {{ event.eventType }}
                </span>
                <span v-if="event.durationMs !== undefined" class="px-2 py-0.5 rounded text-xs font-mono bg-purple-50 text-purple-800 border border-purple-200">
                  {{ event.durationMs }}ms
                </span>
              </div>
            </div>
            <button 
              type="button" 
              class="text-slate-400 hover:text-slate-700 p-1 rounded-lg hover:bg-slate-200 transition-colors" 
              aria-label="Close drawer" 
              @click="emit('close')"
            >
              <X :size="20" />
            </button>
          </div>

          <!-- Drawer Content -->
          <div class="flex-1 overflow-y-auto p-6 space-y-6">
            <!-- Zero-PHI Invariant 7 Notice Banner -->
            <div class="rounded-lg bg-emerald-50 border border-emerald-200 p-3.5 flex items-start gap-3">
              <ShieldCheck :size="18" class="text-emerald-600 shrink-0 mt-0.5" />
              <div class="text-xs text-emerald-800 leading-relaxed">
                <span class="font-bold text-emerald-900">Diagnostic Zero-PHI Boundary:</span>
                Clinical message bodies, FHIR resource contents, and HL7 segments are strictly omitted. Only technical identifiers and operational metrics are displayed.
              </div>
            </div>

            <!-- Trace Timeline Action Card (if correlationId present) -->
            <div v-if="event.correlationId" class="card p-4 bg-sky-50 border border-sky-200 rounded-lg flex items-center justify-between gap-4">
              <div class="space-y-0.5">
                <h4 class="text-xs font-bold text-sky-900 flex items-center gap-1.5">
                  <GitCommit :size="14" class="text-sky-600" />
                  <span>Cross-Subsystem Correlation</span>
                </h4>
                <p class="text-[11px] text-slate-600">
                  Trace all events sharing this Correlation ID across gateways, queues, and workflows.
                </p>
              </div>
              <button
                type="button"
                class="btn-primary text-xs py-1.5 px-3 bg-sky-600 hover:bg-sky-700 text-white font-semibold rounded-md shadow-xs flex items-center gap-1.5 shrink-0 transition-colors"
                @click="emit('trace', event.correlationId); emit('close')"
              >
                <span>Trace Flow</span>
                <ExternalLink :size="12" />
              </button>
            </div>

            <!-- Technical Identifiers Section -->
            <div class="space-y-3">
              <h3 class="text-xs font-bold uppercase tracking-wider text-slate-500 flex items-center gap-1.5">
                <Layers :size="14" class="text-sky-600" />
                <span>Technical Identifiers</span>
              </h3>

              <div class="grid grid-cols-1 gap-2.5">
                <!-- Event ID -->
                <div class="p-3 bg-slate-50 rounded-lg border border-slate-200 flex items-center justify-between">
                  <div>
                    <div class="text-[11px] text-slate-500 font-medium">Event ID</div>
                    <div class="text-xs font-mono font-bold text-slate-800">{{ event.eventId }}</div>
                  </div>
                  <button
                    type="button"
                    class="p-1.5 text-slate-400 hover:text-slate-700 rounded hover:bg-slate-200 transition-colors"
                    title="Copy Event ID"
                    @click="copyToClipboard(event.eventId, 'eventId')"
                  >
                    <Check v-if="copiedField === 'eventId'" :size="14" class="text-emerald-600" />
                    <Copy v-else :size="14" />
                  </button>
                </div>

                <!-- Correlation ID -->
                <div class="p-3 bg-slate-50 rounded-lg border border-slate-200 flex items-center justify-between">
                  <div>
                    <div class="text-[11px] text-slate-500 font-medium">Correlation ID</div>
                    <div class="text-xs font-mono font-bold text-sky-700">{{ event.correlationId || 'N/A' }}</div>
                  </div>
                  <button
                    v-if="event.correlationId"
                    type="button"
                    class="p-1.5 text-slate-400 hover:text-slate-700 rounded hover:bg-slate-200 transition-colors"
                    title="Copy Correlation ID"
                    @click="copyToClipboard(event.correlationId, 'correlationId')"
                  >
                    <Check v-if="copiedField === 'correlationId'" :size="14" class="text-emerald-600" />
                    <Copy v-else :size="14" />
                  </button>
                </div>

                <!-- Causation ID -->
                <div v-if="event.causationId" class="p-3 bg-slate-50 rounded-lg border border-slate-200 flex items-center justify-between">
                  <div>
                    <div class="text-[11px] text-slate-500 font-medium">Causation ID</div>
                    <div class="text-xs font-mono font-bold text-slate-800">{{ event.causationId }}</div>
                  </div>
                  <button
                    type="button"
                    class="p-1.5 text-slate-400 hover:text-slate-700 rounded hover:bg-slate-200 transition-colors"
                    title="Copy Causation ID"
                    @click="copyToClipboard(event.causationId, 'causationId')"
                  >
                    <Check v-if="copiedField === 'causationId'" :size="14" class="text-emerald-600" />
                    <Copy v-else :size="14" />
                  </button>
                </div>

                <!-- Message ID -->
                <div v-if="event.messageId" class="p-3 bg-slate-50 rounded-lg border border-slate-200 flex items-center justify-between">
                  <div>
                    <div class="text-[11px] text-slate-500 font-medium">Message ID</div>
                    <div class="text-xs font-mono font-bold text-slate-800">{{ event.messageId }}</div>
                  </div>
                  <button
                    type="button"
                    class="p-1.5 text-slate-400 hover:text-slate-700 rounded hover:bg-slate-200 transition-colors"
                    title="Copy Message ID"
                    @click="copyToClipboard(event.messageId, 'messageId')"
                  >
                    <Check v-if="copiedField === 'messageId'" :size="14" class="text-emerald-600" />
                    <Copy v-else :size="14" />
                  </button>
                </div>

                <!-- Pragma ID -->
                <div v-if="event.pragmaId" class="p-3 bg-slate-50 rounded-lg border border-slate-200 flex items-center justify-between">
                  <div>
                    <div class="text-[11px] text-slate-500 font-medium">Pragma ID</div>
                    <div class="text-xs font-mono font-bold text-slate-800">{{ event.pragmaId }}</div>
                  </div>
                  <button
                    type="button"
                    class="p-1.5 text-slate-400 hover:text-slate-700 rounded hover:bg-slate-200 transition-colors"
                    title="Copy Pragma ID"
                    @click="copyToClipboard(event.pragmaId, 'pragmaId')"
                  >
                    <Check v-if="copiedField === 'pragmaId'" :size="14" class="text-emerald-600" />
                    <Copy v-else :size="14" />
                  </button>
                </div>

                <!-- Ergon ID -->
                <div v-if="event.ergonId" class="p-3 bg-slate-50 rounded-lg border border-slate-200">
                  <div class="text-[11px] text-slate-500 font-medium">Ergon Activity ID</div>
                  <div class="text-xs font-mono font-bold text-purple-700">{{ event.ergonId }}</div>
                </div>

                <!-- Interface ID -->
                <div v-if="event.interfaceId" class="p-3 bg-slate-50 rounded-lg border border-slate-200">
                  <div class="text-[11px] text-slate-500 font-medium">Interface / Gateway Binding</div>
                  <div class="text-xs font-mono text-slate-700">{{ event.interfaceId }}</div>
                </div>
              </div>
            </div>

            <!-- Execution Metrics & Timing -->
            <div class="space-y-3">
              <h3 class="text-xs font-bold uppercase tracking-wider text-slate-500 flex items-center gap-1.5">
                <Clock :size="14" class="text-sky-600" />
                <span>Execution Timing</span>
              </h3>

              <div class="grid grid-cols-2 gap-3">
                <div class="p-3 bg-slate-50 rounded-lg border border-slate-200">
                  <div class="text-[11px] text-slate-500 font-medium">Observed Timestamp</div>
                  <div class="text-xs font-mono text-slate-800 mt-1">{{ formatTime(event.timestamp) }}</div>
                </div>

                <div class="p-3 bg-slate-50 rounded-lg border border-slate-200">
                  <div class="text-[11px] text-slate-500 font-medium">Operation Duration</div>
                  <div class="text-xs font-mono font-bold text-sky-700 mt-1">{{ event.durationMs }}ms</div>
                </div>
              </div>
            </div>

            <!-- Error Reason Code (if failed or warning) -->
            <div v-if="event.reasonCode" class="space-y-2">
              <h3 class="text-xs font-bold uppercase tracking-wider text-rose-700 flex items-center gap-1.5">
                <AlertTriangle :size="14" class="text-rose-600" />
                <span>Failure Reason Code</span>
              </h3>
              <div class="p-3 bg-rose-50 border border-rose-200 rounded-lg text-xs font-mono text-rose-800 leading-relaxed">
                {{ event.reasonCode }}
              </div>
            </div>
          </div>

          <!-- Drawer Footer -->
          <div class="p-4 border-t border-slate-200 bg-slate-50 flex items-center justify-end">
            <button 
              type="button" 
              class="btn-secondary text-xs px-4 py-2 bg-white border border-slate-300 text-slate-700 hover:bg-slate-100 rounded-md shadow-xs transition-colors"
              @click="emit('close')"
            >
              Close
            </button>
          </div>
        </div>
      </div>
    </div>
  </teleport>
</template>
