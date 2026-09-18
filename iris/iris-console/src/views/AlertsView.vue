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
import { ref, computed, onMounted } from 'vue';
import { Bell, RefreshCw, AlertCircle, ShieldCheck } from 'lucide-vue-next';
import { useOperationsStore } from '../stores/operationsStore';
import AlertSeverityTabs from '../components/alerts/AlertSeverityTabs.vue';
import AlertTable from '../components/alerts/AlertTable.vue';

const store = useOperationsStore();

const severityFilter = ref<string>('ALL');
const statusFilter = ref<string>('ALL');
const subsystemFilter = ref<string>('ALL');

onMounted(async () => {
  await store.fetchAlerts();
});

async function handleRefresh() {
  await store.fetchAlerts(
    severityFilter.value !== 'ALL' ? severityFilter.value : undefined,
    statusFilter.value !== 'ALL' ? statusFilter.value : undefined,
    subsystemFilter.value !== 'ALL' ? subsystemFilter.value : undefined
  );
}

async function handleSeverityChange(sev: string) {
  severityFilter.value = sev;
  await store.fetchAlerts(
    sev !== 'ALL' ? sev : undefined,
    statusFilter.value !== 'ALL' ? statusFilter.value : undefined,
    subsystemFilter.value !== 'ALL' ? subsystemFilter.value : undefined
  );
}

async function handleStatusChange(st: string) {
  statusFilter.value = st;
  await store.fetchAlerts(
    severityFilter.value !== 'ALL' ? severityFilter.value : undefined,
    st !== 'ALL' ? st : undefined,
    subsystemFilter.value !== 'ALL' ? subsystemFilter.value : undefined
  );
}

async function handleSubsystemChange(sub: string) {
  subsystemFilter.value = sub;
  await store.fetchAlerts(
    severityFilter.value !== 'ALL' ? severityFilter.value : undefined,
    statusFilter.value !== 'ALL' ? statusFilter.value : undefined,
    sub !== 'ALL' ? sub : undefined
  );
}

async function handleAcknowledge(alertId: string) {
  try {
    await store.acknowledgeAlert(alertId, 'operator');
  } catch (err) {
    console.error('Failed to acknowledge alert:', err);
  }
}

// Client-side fallback filter if store.alerts contains multiple and backend returned full list
const filteredAlerts = computed(() => {
  return store.alerts.filter(a => {
    if (severityFilter.value !== 'ALL' && a.severity?.toUpperCase() !== severityFilter.value) {
      return false;
    }
    if (statusFilter.value !== 'ALL' && a.status?.toUpperCase() !== statusFilter.value) {
      return false;
    }
    if (subsystemFilter.value !== 'ALL' && a.subsystem?.toLowerCase() !== subsystemFilter.value.toLowerCase()) {
      return false;
    }
    return true;
  });
});

const criticalCount = computed(() => store.alerts.filter(a => a.severity?.toUpperCase() === 'CRITICAL').length);
const warningCount = computed(() => store.alerts.filter(a => a.severity?.toUpperCase() === 'WARNING').length);
const infoCount = computed(() => store.alerts.filter(a => a.severity?.toUpperCase() === 'INFORMATION').length);
const totalCount = computed(() => store.alerts.length);
</script>

<template>
  <div class="space-y-6">
    <!-- View Header -->
    <div class="flex flex-wrap items-center justify-between gap-4">
      <div>
        <h1 class="text-2xl font-extrabold text-white tracking-tight flex items-center gap-2">
          <Bell :size="24" class="text-amber-400" />
          <span>Operational Alerts</span>
        </h1>
        <p class="text-xs text-slate-400 mt-1">
          Actionable platform alerts classified by severity with operator remediation guidance.
        </p>
      </div>

      <div class="flex items-center gap-3">
        <!-- Alert Count Badges -->
        <div class="flex items-center gap-2">
          <span class="px-2.5 py-1 rounded-md text-xs font-mono font-bold bg-rose-500/10 border border-rose-500/25 text-rose-300">
            {{ criticalCount }} Critical
          </span>
          <span class="px-2.5 py-1 rounded-md text-xs font-mono font-bold bg-amber-500/10 border border-amber-500/25 text-amber-300">
            {{ warningCount }} Warning
          </span>
        </div>

        <!-- Manual Refresh Button -->
        <button
          type="button"
          class="btn-secondary flex items-center gap-1.5 text-xs py-1.5 px-3 border border-slate-700/80 hover:bg-slate-800 rounded-lg shadow-sm"
          :disabled="store.loading"
          @click="handleRefresh"
        >
          <RefreshCw :size="13" :class="{ 'animate-spin': store.loading }" />
          <span>Refresh</span>
        </button>
      </div>
    </div>

    <!-- Severity & Status Filters Tabs -->
    <AlertSeverityTabs
      :selected-severity="severityFilter"
      :selected-status="statusFilter"
      :selected-subsystem="subsystemFilter"
      :critical-count="criticalCount"
      :warning-count="warningCount"
      :info-count="infoCount"
      :total-count="totalCount"
      @update:severity="handleSeverityChange"
      @update:status="handleStatusChange"
      @update:subsystem="handleSubsystemChange"
    />

    <!-- Alerts Table -->
    <AlertTable
      :alerts="filteredAlerts"
      :loading="store.loading"
      @acknowledge="handleAcknowledge"
    />
  </div>
</template>
