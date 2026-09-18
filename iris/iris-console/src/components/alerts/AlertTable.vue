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
import type { OperationalAlert } from '../../models/operations';
import StatusBadge from '../common/StatusBadge.vue';
import { 
  AlertOctagon, 
  AlertTriangle, 
  Info, 
  CheckCircle2, 
  Check, 
  Clock, 
  HelpCircle,
  ExternalLink
} from 'lucide-vue-next';

const props = defineProps<{
  alerts: OperationalAlert[];
  loading?: boolean;
}>();

const emit = defineEmits<{
  (e: 'acknowledge', alertId: string): void;
}>();

function formatTime(timestamp?: number): string {
  if (!timestamp) return 'N/A';
  return new Date(timestamp).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', second: '2-digit' });
}

function getSeverityBadgeClass(sev: string) {
  switch (sev?.toUpperCase()) {
    case 'CRITICAL':
      return 'bg-rose-500/20 text-rose-300 border-rose-500/40';
    case 'WARNING':
      return 'bg-amber-500/20 text-amber-300 border-amber-500/40';
    case 'INFORMATION':
    default:
      return 'bg-sky-500/20 text-sky-300 border-sky-500/40';
  }
}
</script>

<template>
  <div class="card p-0 overflow-hidden border border-slate-800 bg-slate-900/90 shadow-xl" aria-label="Operational Alerts Section">
    <!-- Loading State -->
    <div v-if="loading" class="p-12 text-center space-y-3">
      <div class="inline-block animate-spin rounded-full h-8 w-8 border-2 border-sky-400 border-t-transparent"></div>
      <p class="text-xs text-slate-400 font-medium">Evaluating active platform alert conditions...</p>
    </div>

    <!-- Honest Empty State -->
    <div v-else-if="alerts.length === 0" class="p-12 text-center space-y-3">
      <CheckCircle2 :size="40" class="mx-auto text-emerald-400" />
      <h3 class="text-base font-bold text-white">No Active Alerts</h3>
      <p class="text-xs text-slate-400 max-w-md mx-auto leading-relaxed">
        All Harmonia platform subsystems are operating nominally. No active alert conditions require operator intervention.
      </p>
    </div>

    <!-- Alerts Table -->
    <div v-else class="table-container">
      <table class="table" role="table" aria-label="Operational Alerts Table">
        <thead>
          <tr>
            <th scope="col" class="w-28">Severity</th>
            <th scope="col">Subsystem</th>
            <th scope="col">Component</th>
            <th scope="col">Condition</th>
            <th scope="col">Observed</th>
            <th scope="col">Status</th>
            <th scope="col">Operator Guidance</th>
            <th scope="col" class="w-32 text-right">Action</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="alert in alerts" :key="alert.alertId" class="hover:bg-slate-800/40 transition-colors">
            <!-- Severity -->
            <td>
              <span 
                class="px-2 py-0.5 rounded text-[10px] font-bold uppercase tracking-wider border inline-flex items-center gap-1"
                :class="getSeverityBadgeClass(alert.severity)"
              >
                <AlertOctagon v-if="alert.severity === 'CRITICAL'" :size="11" />
                <AlertTriangle v-else-if="alert.severity === 'WARNING'" :size="11" />
                <Info v-else :size="11" />
                <span>{{ alert.severity }}</span>
              </span>
            </td>

            <!-- Subsystem -->
            <td>
              <span class="font-mono text-xs font-bold text-sky-400 uppercase bg-slate-950 px-2 py-0.5 rounded border border-slate-800">
                {{ (alert.subsystem || '').toUpperCase() }}
              </span>
            </td>

            <!-- Component -->
            <td class="font-mono text-xs text-slate-300 whitespace-nowrap">
              {{ alert.component }}
            </td>

            <!-- Condition -->
            <td>
              <div class="text-xs text-white font-medium max-w-xs leading-snug">
                {{ alert.condition }}
              </div>
              <div v-if="alert.relatedResource" class="text-[10px] font-mono text-slate-500 mt-0.5">
                Target: {{ alert.relatedResource }}
              </div>
            </td>

            <!-- Observed / Time -->
            <td class="whitespace-nowrap font-mono text-xs text-slate-400">
              <div>{{ formatTime(alert.lastObserved) }}</div>
              <div class="text-[10px] text-slate-500">{{ alert.duration || 'Active' }}</div>
            </td>

            <!-- Status -->
            <td>
              <StatusBadge :status="alert.status" size="sm" />
            </td>

            <!-- Operator Guidance -->
            <td class="max-w-md">
              <div class="p-2 bg-slate-950/80 rounded border border-slate-800/80 text-xs text-slate-300 leading-relaxed">
                <span class="font-bold text-slate-200">Guidance: </span>
                {{ alert.operatorGuidance || 'Inspect component logs, restart counts, and dependency connectivity.' }}
              </div>
            </td>

            <!-- Action -->
            <td class="text-right whitespace-nowrap">
              <button
                v-if="alert.status === 'ACTIVE'"
                type="button"
                data-testid="acknowledge-btn"
                class="btn-secondary text-xs py-1 px-2.5 flex items-center gap-1 ml-auto border border-amber-500/30 hover:border-amber-500/60 text-amber-300 hover:bg-amber-500/10 rounded-md transition-colors"
                title="Acknowledge this alert condition"
                @click="emit('acknowledge', alert.alertId)"
              >
                <Check :size="13" />
                <span>Acknowledge</span>
              </button>
              <span 
                v-else-if="alert.status === 'ACKNOWLEDGED'" 
                class="text-[11px] font-mono text-slate-500 italic"
              >
                Acknowledged
              </span>
              <span 
                v-else 
                class="text-[11px] font-mono text-emerald-500"
              >
                Resolved
              </span>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>
