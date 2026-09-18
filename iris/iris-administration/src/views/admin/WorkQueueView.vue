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
import { ref, onMounted, computed } from 'vue';
import { useProviderAdminStore } from '../../stores/providerAdminStore';
import { useSecurityStore } from '../../stores/securityStore';
import StatusBadge from '../../components/StatusBadge.vue';
import ValidationAlert from '../../components/ValidationAlert.vue';
import { computeResourceDiff } from '../../utils/fhirMapper';
import type { ChangeRequestSummaryView, ResourceDiffEntry } from '../../models/provider';
import { 
  ClipboardList, Check, X, RefreshCw, 
  Filter, AlertTriangle, GitCompare, Eye, 
  CheckCircle2, Clock, ShieldAlert, ArrowRight
} from 'lucide-vue-next';

const adminStore = useProviderAdminStore();
const securityStore = useSecurityStore();

const statusFilter = ref<string>('all');
const selectedRequest = ref<ChangeRequestSummaryView | null>(null);
const rejectionModalOpen = ref<boolean>(false);
const rejectionReason = ref<string>('');
const actionInProgress = ref<boolean>(false);

onMounted(async () => {
  await adminStore.loadWorkQueue();
});

const filteredQueue = computed(() => {
  if (statusFilter.value === 'all') {
    return adminStore.workQueue;
  }
  return adminStore.workQueue.filter(
    item => item.status.toLowerCase() === statusFilter.value.toLowerCase()
  );
});

const computedDiffs = computed<ResourceDiffEntry[]>(() => {
  if (!selectedRequest.value) return [];
  return computeResourceDiff(
    selectedRequest.value.targetExistingResource,
    selectedRequest.value.payload
  );
});

const handleApprove = async (taskId: string) => {
  actionInProgress.value = true;
  try {
    await adminStore.approveRequest(taskId);
    if (selectedRequest.value?.taskId === taskId) {
      selectedRequest.value.status = 'COMPLETED';
    }
  } finally {
    actionInProgress.value = false;
  }
};

const handleOpenReject = (req: ChangeRequestSummaryView) => {
  selectedRequest.value = req;
  rejectionReason.value = 'Incomplete or inconsistent information provided.';
  rejectionModalOpen.value = true;
};

const handleConfirmReject = async () => {
  if (!selectedRequest.value) return;
  actionInProgress.value = true;
  try {
    await adminStore.rejectRequest(selectedRequest.value.taskId, rejectionReason.value);
    selectedRequest.value.status = 'REJECTED';
    selectedRequest.value.diagnostics = rejectionReason.value;
    rejectionModalOpen.value = false;
  } finally {
    actionInProgress.value = false;
  }
};
</script>

<template>
  <div class="space-y-6">
    <div class="flex flex-col md:flex-row md:items-center justify-between gap-4">
      <div>
        <h1 class="text-2xl font-bold text-white">Departmental Work Queue</h1>
        <p class="text-slate-400 text-sm mt-1">
          Review, triage, validation outcome inspection, and governed approval/rejection of Provider Registry change requests.
        </p>
      </div>

      <button @click="adminStore.loadWorkQueue()" class="btn btn-secondary btn-sm" :disabled="adminStore.loading">
        <RefreshCw :size="14" :class="{ 'animate-spin': adminStore.loading }" />
        <span>Refresh Queue</span>
      </button>
    </div>

    <!-- Filter Buttons -->
    <div class="flex flex-wrap items-center gap-2">
      <span class="text-xs text-slate-400 flex items-center gap-1 font-semibold">
        <Filter :size="13" />
        <span>Queue Status:</span>
      </span>
      <button 
        v-for="st in ['all', 'in_progress', 'accepted', 'completed', 'rejected']"
        :key="st"
        @click="statusFilter = st"
        class="btn btn-sm text-xs capitalize"
        :class="statusFilter === st ? 'btn-primary' : 'btn-secondary'"
      >
        {{ st.replace('_', ' ') }}
      </button>
    </div>

    <!-- Main Content Area: Queue List (Left) and Inspector Panel (Right) -->
    <div class="grid grid-cols-1 lg:grid-cols-12 gap-6">
      <!-- Queue Table / List -->
      <div class="lg:col-span-5 space-y-3">
        <div v-if="filteredQueue.length === 0" class="card text-center py-12 text-slate-500 text-sm">
          No items found in work queue.
        </div>

        <div 
          v-for="item in filteredQueue" 
          :key="item.taskId"
          class="card p-4 space-y-3 cursor-pointer transition-all hover:border-slate-600"
          :class="selectedRequest?.taskId === item.taskId ? 'border-sky-500 bg-slate-900/90' : ''"
          @click="selectedRequest = item"
        >
          <div class="flex items-start justify-between">
            <div>
              <div class="font-bold text-sm text-white flex items-center gap-1.5">
                <span>{{ item.operation }} {{ item.resourceType }}</span>
                <span v-if="item.resourceId" class="font-mono text-xs text-sky-400">#{{ item.resourceId }}</span>
              </div>
              <div class="font-mono text-[11px] text-slate-400">{{ item.taskId }}</div>
            </div>
            <StatusBadge :status="item.status" />
          </div>

          <div class="flex items-center justify-between text-xs text-slate-400 pt-2 border-t border-slate-800/80">
            <span>By: <strong class="text-slate-300">{{ item.requester }}</strong></span>
            <span>{{ new Date(item.submittedAt).toLocaleDateString() }}</span>
          </div>
        </div>
      </div>

      <!-- Inspector & Diff Panel -->
      <div class="lg:col-span-7">
        <div v-if="!selectedRequest" class="card text-center py-20 text-slate-500">
          <Eye :size="32" class="mx-auto text-slate-600 mb-2" />
          <p>Select a change request from the queue to inspect payload, validation feedback, and record diff.</p>
        </div>

        <div v-else class="card space-y-6">
          <!-- Request Header & Action Bar -->
          <div class="flex flex-col sm:flex-row sm:items-center justify-between gap-3 border-b border-slate-800 pb-4">
            <div>
              <div class="flex items-center gap-2">
                <h2 class="text-lg font-bold text-white">{{ selectedRequest.operation }} {{ selectedRequest.resourceType }}</h2>
                <span class="font-mono text-xs text-sky-400">{{ selectedRequest.taskId }}</span>
              </div>
              <p class="text-xs text-slate-400">
                Submitted by <strong>{{ selectedRequest.requester }}</strong> on {{ new Date(selectedRequest.submittedAt).toLocaleString() }}
              </p>
            </div>

            <div v-if="['REQUESTED', 'ACCEPTED', 'IN_PROGRESS', 'VALIDATING'].includes(selectedRequest.status)" class="flex items-center gap-2">
              <button 
                @click="handleOpenReject(selectedRequest)" 
                class="btn btn-danger btn-sm"
                :disabled="actionInProgress"
              >
                <X :size="14" />
                <span>Reject</span>
              </button>
              <button 
                @click="handleApprove(selectedRequest.taskId)" 
                class="btn btn-success btn-sm"
                :disabled="actionInProgress"
              >
                <Check :size="14" />
                <span>Approve &amp; Commit</span>
              </button>
            </div>
            <div v-else>
              <StatusBadge :status="selectedRequest.status" />
            </div>
          </div>

          <!-- Validation Feedback Section -->
          <div v-if="selectedRequest.outcomeIssues && selectedRequest.outcomeIssues.length > 0">
            <ValidationAlert :issues="selectedRequest.outcomeIssues" title="Authoritative Validation Report" />
          </div>

          <div v-if="selectedRequest.diagnostics" class="p-3 bg-red-950/20 border border-red-500/30 rounded text-red-300 text-xs">
            <strong>Rejection Reason / Outcome Note:</strong> {{ selectedRequest.diagnostics }}
          </div>

          <!-- Diff Viewer -->
          <div class="space-y-2">
            <div class="flex items-center gap-2 text-sky-400 font-bold text-xs uppercase tracking-wider">
              <GitCompare :size="15" />
              <span>Registry Master Data Diff View</span>
            </div>

            <div v-if="computedDiffs.length === 0" class="p-4 bg-slate-950 rounded border border-slate-800 text-slate-400 text-xs text-center">
              New entity creation payload (No existing baseline record).
            </div>

            <div v-else class="table-container text-xs">
              <table class="table">
                <thead>
                  <tr>
                    <th>Field</th>
                    <th>Existing Registry State</th>
                    <th>Proposed New State</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(diff, idx) in computedDiffs" :key="idx">
                    <td class="font-semibold text-slate-300">{{ diff.label }}</td>
                    <td class="text-slate-400 font-mono text-[11px]">
                      <span :class="diff.status === 'removed' || diff.status === 'modified' ? 'text-red-400' : ''">
                        {{ diff.oldValue }}
                      </span>
                    </td>
                    <td class="font-mono text-[11px]">
                      <span :class="diff.status === 'added' || diff.status === 'modified' ? 'text-emerald-400 font-bold' : 'text-slate-300'">
                        {{ diff.newValue }}
                      </span>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>

          <!-- Raw FHIR Resource Tab for Technical Officers -->
          <div v-if="selectedRequest.payload" class="space-y-2 pt-2 border-t border-slate-800">
            <span class="text-xs font-bold text-slate-400 uppercase tracking-wider">Proposed FHIR R5 Resource Payload</span>
            <pre class="p-3 bg-slate-950 rounded font-mono text-[11px] text-slate-300 overflow-x-auto border border-slate-800 max-h-60">{{ JSON.stringify(selectedRequest.payload, null, 2) }}</pre>
          </div>
        </div>
      </div>
    </div>

    <!-- Rejection Modal -->
    <div v-if="rejectionModalOpen" class="modal-overlay" @click.self="rejectionModalOpen = false">
      <div class="modal-content">
        <div class="modal-header">
          <h3 class="text-base font-bold text-white flex items-center gap-2">
            <AlertTriangle :size="18" class="text-red-400" />
            <span>Reject Change Request ({{ selectedRequest?.taskId }})</span>
          </h3>
          <button @click="rejectionModalOpen = false" class="btn btn-secondary btn-sm">
            <X :size="14" />
          </button>
        </div>

        <div class="modal-body space-y-4 text-xs">
          <p class="text-slate-300">
            Provide the authoritative justification for rejecting this change request. The reason will be recorded in the Task statusReason and visible to the provider.
          </p>

          <div class="form-group">
            <label class="form-label">Rejection Reason / Explanation *</label>
            <textarea v-model="rejectionReason" class="form-textarea" rows="3" required placeholder="Explain why the request cannot be committed..."></textarea>
          </div>
        </div>

        <div class="modal-footer">
          <button @click="rejectionModalOpen = false" class="btn btn-secondary">
            Cancel
          </button>
          <button @click="handleConfirmReject" class="btn btn-danger" :disabled="actionInProgress || !rejectionReason">
            <span>Confirm Rejection</span>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
