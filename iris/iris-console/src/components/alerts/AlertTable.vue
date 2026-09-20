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
import { IrisDataTable, IrisStatus, type DataTableColumn } from '@harmonia/iris-befe';
import { 
  AlertOctagon, 
  AlertTriangle, 
  Info, 
  CheckCircle2, 
  Check 
} from 'lucide-vue-next';

withDefaults(defineProps<{
  alerts: OperationalAlert[];
  loading?: boolean;
}>(), {
  loading: false
});

const emit = defineEmits<{
  (e: 'acknowledge', alertId: string): void;
}>();

const tableColumns: DataTableColumn[] = [
  { field: 'severity', header: 'Severity', width: '120px', sortable: true },
  { field: 'subsystem', header: 'Subsystem', width: '130px', sortable: true },
  { field: 'component', header: 'Component', width: '160px', sortable: true },
  { field: 'condition', header: 'Condition' },
  { field: 'observed', header: 'Observed', width: '110px', sortable: true },
  { field: 'status', header: 'Status', width: '120px', sortable: true },
  { field: 'guidance', header: 'Operator Guidance' },
  { field: 'actions', header: 'Action', width: '130px', align: 'right' }
];

function formatTime(timestamp?: number): string {
  if (!timestamp) return 'N/A';
  return new Date(timestamp).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', second: '2-digit' });
}

function getSeverityBadgeClass(sev: string) {
  switch (sev?.toUpperCase()) {
    case 'CRITICAL':
      return 'bg-rose-50 text-rose-800 border-rose-200';
    case 'WARNING':
      return 'bg-amber-50 text-amber-800 border-amber-200';
    case 'INFORMATION':
    default:
      return 'bg-sky-50 text-sky-800 border-sky-200';
  }
}
</script>

<template>
  <div class="alert-table-container bg-white border border-[var(--iris-border-default)] rounded-[var(--iris-border-radius)] overflow-hidden shadow-subtle font-sans" aria-label="Operational Alerts Section">
    <IrisDataTable
      :value="alerts"
      :columns="tableColumns"
      :loading="loading"
      data-key="alertId"
      empty-message="No Active Alerts"
    >
      <!-- Honest Empty State -->
      <template #empty>
        <div class="p-12 text-center space-y-3">
          <CheckCircle2 :size="40" class="mx-auto text-emerald-600" />
          <h3 class="text-base font-bold text-slate-900">No Active Alerts</h3>
          <p class="text-xs text-slate-500 max-w-md mx-auto leading-relaxed">
            All Harmonia platform subsystems are operating nominally. No active alert conditions require operator intervention.
          </p>
        </div>
      </template>

      <!-- Severity Column -->
      <template #severity="{ data }">
        <span 
          class="px-2 py-0.5 rounded text-[10px] font-bold uppercase tracking-wider border inline-flex items-center gap-1 font-mono"
          :class="getSeverityBadgeClass(data.severity)"
        >
          <AlertOctagon v-if="data.severity === 'CRITICAL'" :size="11" />
          <AlertTriangle v-else-if="data.severity === 'WARNING'" :size="11" />
          <Info v-else :size="11" />
          <span>{{ data.severity }}</span>
        </span>
      </template>

      <!-- Subsystem Column -->
      <template #subsystem="{ data }">
        <span class="font-mono text-xs font-bold text-slate-700 uppercase bg-slate-100 px-2 py-0.5 rounded border border-slate-200">
          {{ (data.subsystem || '').toUpperCase() }}
        </span>
      </template>

      <!-- Component Column -->
      <template #component="{ data }">
        <span class="font-mono text-xs text-slate-800 whitespace-nowrap">
          {{ data.component }}
        </span>
      </template>

      <!-- Condition Column -->
      <template #condition="{ data }">
        <div class="text-xs text-slate-900 font-medium max-w-xs leading-snug">
          {{ data.condition }}
        </div>
        <div v-if="data.relatedResource" class="text-[10px] font-mono text-slate-500 mt-0.5">
          Target: {{ data.relatedResource }}
        </div>
      </template>

      <!-- Observed Column -->
      <template #observed="{ data }">
        <div class="font-mono text-xs text-slate-600 whitespace-nowrap">
          <div>{{ formatTime(data.lastObserved) }}</div>
          <div class="text-[10px] text-slate-400">{{ data.duration || 'Active' }}</div>
        </div>
      </template>

      <!-- Status Column -->
      <template #status="{ data }">
        <IrisStatus :status="data.status" label-format="upper" size="sm" />
      </template>

      <!-- Guidance Column -->
      <template #guidance="{ data }">
        <div class="p-2 bg-slate-50 rounded border border-slate-200 text-xs text-slate-800 leading-relaxed">
          <span class="font-bold text-slate-900">Guidance: </span>
          {{ data.operatorGuidance || 'Inspect component logs, restart counts, and dependency connectivity.' }}
        </div>
      </template>

      <!-- Action Column -->
      <template #actions="{ data }">
        <div class="text-right whitespace-nowrap">
          <button
            v-if="data.status === 'ACTIVE'"
            type="button"
            data-testid="acknowledge-btn"
            class="btn-secondary text-xs py-1 px-2.5 flex items-center gap-1 ml-auto bg-white border border-amber-300 hover:border-amber-400 text-amber-800 hover:bg-amber-50 rounded-md shadow-xs transition-colors cursor-pointer font-medium"
            title="Acknowledge this alert condition"
            @click="emit('acknowledge', data.alertId)"
          >
            <Check :size="13" />
            <span>Acknowledge</span>
          </button>
          <span 
            v-else-if="data.status === 'ACKNOWLEDGED'" 
            class="text-[11px] font-mono text-slate-500 italic"
          >
            Acknowledged
          </span>
          <span 
            v-else 
            class="text-[11px] font-mono text-emerald-700 font-bold"
          >
            Resolved
          </span>
        </div>
      </template>
    </IrisDataTable>
  </div>
</template>
