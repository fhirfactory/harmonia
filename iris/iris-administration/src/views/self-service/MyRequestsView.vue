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
import { useSelfServiceStore } from '../../stores/selfServiceStore';
import StatusBadge from '../../components/StatusBadge.vue';
import ValidationAlert from '../../components/ValidationAlert.vue';
import { 
  GitPullRequest, Send, RefreshCw, Filter, 
  Clock, AlertCircle, FileText, ChevronRight 
} from 'lucide-vue-next';

const selfServiceStore = useSelfServiceStore();
const selectedStatusFilter = ref<string>('all');
const selectedRequest = ref<any | null>(null);

onMounted(async () => {
  await selfServiceStore.loadChangeRequests();
});

const filteredRequests = computed(() => {
  if (selectedStatusFilter.value === 'all') {
    return selfServiceStore.changeRequests;
  }
  return selfServiceStore.changeRequests.filter(
    r => r.status.toLowerCase() === selectedStatusFilter.value.toLowerCase()
  );
});

const formatDate = (iso: string) => {
  if (!iso) return '-';
  return new Date(iso).toLocaleString();
};
</script>

<template>
  <div class="space-y-6">
    <div class="flex flex-col md:flex-row md:items-center justify-between gap-4">
      <div>
        <h1 class="text-2xl font-bold text-white">My Change Requests &amp; Tracking</h1>
        <p class="text-slate-400 text-sm mt-1">
          Real-time asynchronous lifecycle tracking for registry modifications submitted to the Ponos change pipeline.
        </p>
      </div>

      <div class="flex items-center gap-2">
        <button @click="selfServiceStore.loadChangeRequests()" class="btn btn-secondary btn-sm">
          <RefreshCw :size="14" />
          <span>Refresh Status</span>
        </button>
        <router-link to="/self-service/request-change" class="btn btn-primary btn-sm">
          <Send :size="14" />
          <span>New Request</span>
        </router-link>
      </div>
    </div>

    <!-- Filter Pills -->
    <div class="flex flex-wrap items-center gap-2">
      <span class="text-xs text-slate-400 flex items-center gap-1 font-semibold">
        <Filter :size="13" />
        <span>Filter:</span>
      </span>
      <button 
        v-for="status in ['all', 'in_progress', 'completed', 'rejected']"
        :key="status"
        @click="selectedStatusFilter = status"
        class="btn btn-sm text-xs capitalize"
        :class="selectedStatusFilter === status ? 'btn-primary' : 'btn-secondary'"
      >
        {{ status.replace('_', ' ') }}
      </button>
    </div>

    <!-- Requests Table / List -->
    <div v-if="filteredRequests.length === 0" class="card text-center py-12 text-slate-400">
      <GitPullRequest :size="32" class="mx-auto text-slate-600 mb-2" />
      <p>No change requests match the current filter.</p>
    </div>

    <div v-else class="space-y-4">
      <div 
        v-for="req in filteredRequests" 
        :key="req.taskId"
        class="card space-y-4 hover:border-sky-500/40 cursor-pointer transition-all"
        @click="selectedRequest = selectedRequest?.taskId === req.taskId ? null : req"
      >
        <div class="flex flex-col md:flex-row md:items-center justify-between gap-2">
          <div class="flex items-center gap-3">
            <div class="w-10 h-10 rounded-lg bg-sky-500/10 border border-sky-500/30 flex items-center justify-center text-sky-400 font-mono text-xs font-bold">
              <GitPullRequest :size="20" />
            </div>
            <div>
              <div class="flex items-center gap-2">
                <h3 class="text-base font-bold text-white">{{ req.operation }} {{ req.resourceType }}</h3>
                <span v-if="req.resourceId" class="font-mono text-xs text-sky-400">({{ req.resourceId }})</span>
              </div>
              <div class="flex items-center gap-2 text-xs text-slate-400">
                <span class="font-mono">{{ req.taskId }}</span>
                <span>&bull;</span>
                <span>Submitted: {{ formatDate(req.submittedAt) }}</span>
              </div>
            </div>
          </div>

          <div class="flex items-center gap-3">
            <StatusBadge :status="req.status" />
            <ChevronRight :size="16" class="text-slate-500 transition-transform" :class="{ 'rotate-90': selectedRequest?.taskId === req.taskId }" />
          </div>
        </div>

        <!-- Expanded Details & Outcome Diagnostics -->
        <div v-if="selectedRequest?.taskId === req.taskId" class="pt-4 border-t border-slate-800 space-y-4 text-xs">
          <div class="grid grid-cols-1 md:grid-cols-2 gap-3 p-3 bg-slate-950/70 rounded border border-slate-800">
            <div>
              <span class="text-slate-400 font-semibold">Correlation ID:</span>
              <p class="font-mono text-slate-300 mt-0.5">{{ req.correlationId }}</p>
            </div>
            <div>
              <span class="text-slate-400 font-semibold">Authoritative Requester:</span>
              <p class="text-slate-300 mt-0.5">{{ req.requester }}</p>
            </div>
          </div>

          <div v-if="req.diagnostics" class="p-3 bg-red-950/20 border border-red-500/30 rounded text-red-300 space-y-1">
            <div class="font-bold flex items-center gap-1.5">
              <AlertCircle :size="14" />
              <span>Review Outcome Diagnostics:</span>
            </div>
            <p>{{ req.diagnostics }}</p>
          </div>

          <div v-if="req.outcomeIssues && req.outcomeIssues.length > 0">
            <ValidationAlert :issues="req.outcomeIssues" title="FHIR OperationOutcome Feedback" />
          </div>

          <div v-if="req.payload" class="space-y-1.5">
            <span class="font-bold text-slate-400 uppercase tracking-wider">Submitted Change Payload (FHIR R5)</span>
            <pre class="p-3 bg-slate-950 rounded font-mono text-[11px] text-slate-300 overflow-x-auto border border-slate-800">{{ JSON.stringify(req.payload, null, 2) }}</pre>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
