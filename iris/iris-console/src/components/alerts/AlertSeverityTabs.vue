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
import { computed } from 'vue';
import type { AlertSeverity, AlertStatus } from '../../models/operations';
import { AlertOctagon, AlertTriangle, Info, Bell, Filter } from 'lucide-vue-next';

const props = withDefaults(defineProps<{
  selectedSeverity?: string;
  selectedStatus?: string;
  selectedSubsystem?: string;
  criticalCount?: number;
  warningCount?: number;
  infoCount?: number;
  totalCount?: number;
}>(), {
  selectedSeverity: 'ALL',
  selectedStatus: 'ALL',
  selectedSubsystem: 'ALL',
  criticalCount: 0,
  warningCount: 0,
  infoCount: 0,
  totalCount: 0
});

const emit = defineEmits<{
  (e: 'update:severity', severity: string): void;
  (e: 'update:status', status: string): void;
  (e: 'update:subsystem', subsystem: string): void;
}>();

const subsystems = [
  { label: 'All Subsystems', value: 'ALL' },
  { label: 'Pylai', value: 'pylai' },
  { label: 'Petasos', value: 'petasos' },
  { label: 'Energeia', value: 'energeia' },
  { label: 'Mnemosyne', value: 'mnemosyne' },
  { label: 'Mneme', value: 'mneme' },
  { label: 'Themis', value: 'themis' },
  { label: 'Agora', value: 'agora' },
  { label: 'Calliope', value: 'calliope' },
  { label: 'Iris', value: 'iris' }
];

const statusOptions = [
  { label: 'All Statuses', value: 'ALL' },
  { label: 'Active', value: 'ACTIVE' },
  { label: 'Acknowledged', value: 'ACKNOWLEDGED' },
  { label: 'Resolved', value: 'RESOLVED' }
];

function selectSeverity(sev: string) {
  emit('update:severity', sev);
}
</script>

<template>
  <div class="alert-severity-tabs space-y-4 border border-[var(--iris-border-default)] bg-white rounded-[var(--iris-border-radius)] p-3 shadow-subtle font-sans" aria-label="Alert Filter Controls">
    <div class="flex flex-wrap items-center justify-between gap-4">
      <!-- Severity Filter Tabs -->
      <div class="inline-flex rounded-lg bg-slate-100 p-1 border border-slate-200" role="tablist" aria-label="Alert Severity Filter">
        <!-- ALL -->
        <button
          type="button"
          role="tab"
          :aria-selected="selectedSeverity === 'ALL'"
          class="px-3 py-1.5 text-xs font-semibold rounded-md flex items-center gap-1.5 transition-colors cursor-pointer"
          :class="selectedSeverity === 'ALL' ? 'bg-white text-slate-900 shadow-xs border border-slate-200' : 'text-slate-600 hover:text-slate-900 border border-transparent'"
          @click="selectSeverity('ALL')"
        >
          <Bell :size="13" class="text-slate-500" />
          <span>All Severities</span>
          <span class="px-1.5 py-0.2 rounded-full text-[10px] font-mono bg-slate-200 text-slate-700 ml-0.5">
            {{ totalCount }}
          </span>
        </button>

        <!-- CRITICAL -->
        <button
          type="button"
          role="tab"
          :aria-selected="selectedSeverity === 'CRITICAL'"
          class="px-3 py-1.5 text-xs font-semibold rounded-md flex items-center gap-1.5 transition-colors cursor-pointer"
          :class="selectedSeverity === 'CRITICAL' ? 'bg-rose-50 text-rose-800 border border-rose-300 shadow-xs' : 'text-slate-600 hover:text-rose-700 border border-transparent'"
          @click="selectSeverity('CRITICAL')"
        >
          <AlertOctagon :size="13" class="text-rose-600" />
          <span>Critical</span>
          <span 
            class="px-1.5 py-0.2 rounded-full text-[10px] font-mono ml-0.5"
            :class="criticalCount > 0 ? 'bg-rose-600 text-white font-bold' : 'bg-slate-200 text-slate-600'"
          >
            {{ criticalCount }}
          </span>
        </button>

        <!-- WARNING -->
        <button
          type="button"
          role="tab"
          :aria-selected="selectedSeverity === 'WARNING'"
          class="px-3 py-1.5 text-xs font-semibold rounded-md flex items-center gap-1.5 transition-colors cursor-pointer"
          :class="selectedSeverity === 'WARNING' ? 'bg-amber-50 text-amber-800 border border-amber-300 shadow-xs' : 'text-slate-600 hover:text-amber-700 border border-transparent'"
          @click="selectSeverity('WARNING')"
        >
          <AlertTriangle :size="13" class="text-amber-600" />
          <span>Warning</span>
          <span 
            class="px-1.5 py-0.2 rounded-full text-[10px] font-mono ml-0.5"
            :class="warningCount > 0 ? 'bg-amber-600 text-white font-bold' : 'bg-slate-200 text-slate-600'"
          >
            {{ warningCount }}
          </span>
        </button>

        <!-- INFORMATION -->
        <button
          type="button"
          role="tab"
          :aria-selected="selectedSeverity === 'INFORMATION'"
          class="px-3 py-1.5 text-xs font-semibold rounded-md flex items-center gap-1.5 transition-colors cursor-pointer"
          :class="selectedSeverity === 'INFORMATION' ? 'bg-sky-50 text-sky-800 border border-sky-300 shadow-xs' : 'text-slate-600 hover:text-sky-700 border border-transparent'"
          @click="selectSeverity('INFORMATION')"
        >
          <Info :size="13" class="text-sky-600" />
          <span>Info</span>
          <span class="px-1.5 py-0.2 rounded-full text-[10px] font-mono bg-slate-200 text-slate-600 ml-0.5">
            {{ infoCount }}
          </span>
        </button>
      </div>

      <!-- Secondary Filters: Status & Subsystem -->
      <div class="flex items-center gap-2.5">
        <!-- Status Dropdown -->
        <div class="flex items-center gap-1.5">
          <label for="alert-filter-status" class="text-xs text-slate-600 font-medium">Status:</label>
          <select
            id="alert-filter-status"
            :value="selectedStatus"
            class="input py-1.5 px-2.5 text-xs bg-white border border-[var(--iris-border-default)] text-slate-900 rounded-md focus:border-sky-500 focus:ring-1 focus:ring-sky-500"
            @change="emit('update:status', ($event.target as HTMLSelectElement).value)"
          >
            <option v-for="opt in statusOptions" :key="opt.value" :value="opt.value">
              {{ opt.label }}
            </option>
          </select>
        </div>

        <!-- Subsystem Dropdown -->
        <div class="flex items-center gap-1.5">
          <label for="alert-filter-subsystem" class="text-xs text-slate-600 font-medium">Subsystem:</label>
          <select
            id="alert-filter-subsystem"
            :value="selectedSubsystem"
            class="input py-1.5 px-2.5 text-xs bg-white border border-[var(--iris-border-default)] text-slate-900 rounded-md focus:border-sky-500 focus:ring-1 focus:ring-sky-500"
            @change="emit('update:subsystem', ($event.target as HTMLSelectElement).value)"
          >
            <option v-for="sub in subsystems" :key="sub.value" :value="sub.value">
              {{ sub.label }}
            </option>
          </select>
        </div>
      </div>
    </div>
  </div>
</template>
