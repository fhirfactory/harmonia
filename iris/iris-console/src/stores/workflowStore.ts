/*
 * Copyright (c) 2026 Mark Hunter
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import type { WorkflowSummary, PragmaSummary, ErgonCheckpoint } from '../models/operations';
import { operationsApi } from '../api/operationsClient';

export const useWorkflowStore = defineStore('workflows', () => {
  // --------------------------------------------------------------------------
  // State
  // --------------------------------------------------------------------------
  const workflows = ref<WorkflowSummary[]>([]);
  const selectedWorkflow = ref<WorkflowSummary | null>(null);
  const selectedPragma = ref<PragmaSummary | null>(null);
  const isPragmaDrawerOpen = ref<boolean>(false);
  const workflowPragmas = ref<Record<string, PragmaSummary[]>>({});

  const loading = ref<boolean>(false);
  const refreshing = ref<boolean>(false);
  const error = ref<string | null>(null);
  const isStale = ref<boolean>(false);
  const lastRefreshed = ref<Date | null>(null);
  const searchQuery = ref<string>('');

  // --------------------------------------------------------------------------
  // Computed Properties
  // --------------------------------------------------------------------------
  const totalWorkflows = computed(() => workflows.value.length);

  const totalActiveExecutions = computed(() => {
    return workflows.value.reduce((acc, w) => acc + (w.activeExecutions != null ? w.activeExecutions : 0), 0);
  });

  const totalQueuedWork = computed(() => {
    return workflows.value.reduce((acc, w) => acc + (w.queuedWork != null ? w.queuedWork : 0), 0);
  });

  const totalCompletedWork = computed(() => {
    return workflows.value.reduce((acc, w) => acc + (w.completedWork != null ? w.completedWork : 0), 0);
  });

  const totalFailedWork = computed(() => {
    return workflows.value.reduce((acc, w) => acc + (w.failedWork != null ? w.failedWork : 0), 0);
  });

  const totalRetryingWork = computed(() => {
    return workflows.value.reduce((acc, w) => acc + (w.retryingWork != null ? w.retryingWork : 0), 0);
  });

  const totalProcessingRate = computed(() => {
    return workflows.value.reduce((acc, w) => acc + (w.processingRate != null ? w.processingRate : 0), 0);
  });

  const p95Duration = computed(() => {
    const list = workflows.value.filter(w => typeof w.p95DurationMs === 'number' && w.p95DurationMs > 0);
    if (list.length === 0) return null;
    return Math.max(...list.map(w => w.p95DurationMs));
  });

  const filteredWorkflows = computed(() => {
    let result = workflows.value;
    if (searchQuery.value && searchQuery.value.trim()) {
      const q = searchQuery.value.toLowerCase().trim();
      result = result.filter(w => {
        const idMatch = w.workflowId && w.workflowId.toLowerCase().includes(q);
        const nameMatch = w.name && w.name.toLowerCase().includes(q);
        const descMatch = w.description && w.description.toLowerCase().includes(q);
        return idMatch || nameMatch || descMatch;
      });
    }
    return result;
  });

  const currentWorkflowPragmas = computed<PragmaSummary[]>(() => {
    if (!selectedWorkflow.value) return [];
    const list = workflowPragmas.value[selectedWorkflow.value.workflowId];
    return list || [];
  });

  // --------------------------------------------------------------------------
  // Actions
  // --------------------------------------------------------------------------
  async function fetchWorkflows(background: boolean = false) {
    if (background) {
      refreshing.value = true;
    } else {
      loading.value = true;
    }
    error.value = null;

    try {
      const data = await operationsApi.getWorkflows();
      workflows.value = Array.isArray(data) ? data : [];
      isStale.value = false;
      lastRefreshed.value = new Date();

      if (selectedWorkflow.value) {
        const found = workflows.value.find(w => w.workflowId === selectedWorkflow.value?.workflowId);
        if (found) {
          selectedWorkflow.value = found;
          await fetchPragmasForWorkflow(found.workflowId);
        }
      }
    } catch (err: any) {
      console.warn('Failed to fetch workflows telemetry:', err);
      error.value = err.message || 'Failed to fetch workflows';
      isStale.value = true;
    } finally {
      loading.value = false;
      refreshing.value = false;
    }
  }

  async function fetchPragmasForWorkflow(workflowId: string) {
    if (!workflowId) return;
    try {
      const pragmas = await operationsApi.getWorkflowPragmas(workflowId);
      workflowPragmas.value[workflowId] = Array.isArray(pragmas) ? pragmas : [];
    } catch (err) {
      console.warn(`Failed to fetch pragmas for workflow ${workflowId}:`, err);
      workflowPragmas.value[workflowId] = [];
    }
  }

  async function selectWorkflow(workflowId: string) {
    const found = workflows.value.find(w => w.workflowId === workflowId);
    if (found) {
      selectedWorkflow.value = found;
    } else {
      try {
        const wf = await operationsApi.getWorkflow(workflowId);
        selectedWorkflow.value = wf;
      } catch {
        selectedWorkflow.value = null;
      }
    }

    if (workflowId) {
      await fetchPragmasForWorkflow(workflowId);
    }
  }

  function clearSelectedWorkflow() {
    selectedWorkflow.value = null;
  }

  async function openPragmaDrawer(pragmaOrId: string | PragmaSummary) {
    if (typeof pragmaOrId === 'object' && pragmaOrId !== null) {
      selectedPragma.value = pragmaOrId;
      isPragmaDrawerOpen.value = true;
      return;
    }

    const pragmaId = pragmaOrId;
    // Check locally first
    for (const pList of Object.values(workflowPragmas.value)) {
      const found = pList.find(p => p.pragmaId === pragmaId);
      if (found) {
        selectedPragma.value = found;
        isPragmaDrawerOpen.value = true;
        return;
      }
    }

    try {
      const fetched = await operationsApi.getPragma(pragmaId);
      selectedPragma.value = fetched;
      isPragmaDrawerOpen.value = true;
    } catch (err) {
      console.warn(`Pragma ${pragmaId} not found on backend`, err);
    }
  }

  function closePragmaDrawer() {
    isPragmaDrawerOpen.value = false;
    selectedPragma.value = null;
  }

  function setSearchQuery(q: string) {
    searchQuery.value = q;
  }

  return {
    workflows,
    selectedWorkflow,
    selectedPragma,
    isPragmaDrawerOpen,
    workflowPragmas,
    loading,
    refreshing,
    error,
    isStale,
    lastRefreshed,
    searchQuery,
    totalWorkflows,
    totalActiveExecutions,
    totalQueuedWork,
    totalCompletedWork,
    totalFailedWork,
    totalRetryingWork,
    totalProcessingRate,
    p95Duration,
    filteredWorkflows,
    currentWorkflowPragmas,
    fetchWorkflows,
    fetchPragmasForWorkflow,
    selectWorkflow,
    clearSelectedWorkflow,
    openPragmaDrawer,
    closePragmaDrawer,
    setSearchQuery
  };
});
