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
import { useSequenceStore } from '../stores/sequenceStore';
import type { TaskSequence } from '../models/operations';
import { 
  GitMerge, Plus, Search, Filter, Trash2, Edit2, 
  Eye, Check, X, ArrowRight, PlayCircle, PauseCircle, Code, RefreshCw
} from 'lucide-vue-next';

const sequenceStore = useSequenceStore();

const searchQuery = ref('');
const filterStatus = ref('ALL');
const filterGateway = ref('ALL');
const showModal = ref(false);
const showJsonModal = ref(false);
const activeJson = ref('');

const editingSequence = ref<Partial<TaskSequence>>({
  sequenceId: '',
  sequenceName: '',
  description: '',
  version: '1.0.0',
  enabled: true,
  sourceQueueName: '',
  targetGatewayInstances: [],
  targetTriggerTypes: [],
  activityIds: []
});

const gatewayInput = ref('');
const triggerInput = ref('');
const activitiesInput = ref('');

onMounted(() => {
  sequenceStore.fetchSequences();
});

const getActivityList = (actIds: any): string[] => {
  if (!actIds) return [];
  if (Array.isArray(actIds)) return actIds;
  if (typeof actIds === 'object') {
    return Object.keys(actIds)
      .map(k => Number(k))
      .sort((a, b) => a - b)
      .map(k => actIds[k]);
  }
  return [];
};

const openCreateModal = () => {
  editingSequence.value = {
    sequenceId: 'seq-' + Math.random().toString(36).substring(2, 9),
    sequenceName: '',
    description: '',
    version: '1.0.0',
    enabled: true,
    sourceQueueName: '',
    targetGatewayInstances: ['pas-gw'],
    targetTriggerTypes: ['ADT^A01', 'ADT^A08'],
    activityIds: { 0: 'patient-identity-update', 1: 'patient-demographics-update' }
  };
  gatewayInput.value = 'pas-gw';
  triggerInput.value = 'ADT^A01, ADT^A08';
  activitiesInput.value = 'patient-identity-update, patient-demographics-update';
  showModal.value = true;
};

const openEditModal = (seq: TaskSequence) => {
  editingSequence.value = JSON.parse(JSON.stringify(seq));
  gatewayInput.value = (seq.targetGatewayInstances || []).join(', ');
  triggerInput.value = (seq.targetTriggerTypes || []).join(', ');
  activitiesInput.value = getActivityList(seq.activityIds).join(', ');
  showModal.value = true;
};

const saveSequenceForm = async () => {
  if (!editingSequence.value.sequenceName) {
    alert('Please enter a Sequence Name');
    return;
  }

  editingSequence.value.targetGatewayInstances = gatewayInput.value
    .split(',')
    .map(s => s.trim())
    .filter(Boolean);

  editingSequence.value.targetTriggerTypes = triggerInput.value
    .split(',')
    .map(s => s.trim())
    .filter(Boolean);

  const actList = activitiesInput.value
    .split(',')
    .map(s => s.trim())
    .filter(Boolean);
  const actMap: Record<number, string> = {};
  actList.forEach((act, idx) => {
    actMap[idx] = act;
  });

  editingSequence.value.activityIds = actMap;

  await sequenceStore.saveSequence(editingSequence.value);
  showModal.value = false;
};

const viewJson = (seq: TaskSequence) => {
  activeJson.value = JSON.stringify(seq, null, 2);
  showJsonModal.value = true;
};

const clearSearch = () => {
  searchQuery.value = '';
  filterStatus.value = 'ALL';
  filterGateway.value = 'ALL';
};

const filteredSequences = computed(() => {
  const list = Array.isArray(sequenceStore.sequences) ? sequenceStore.sequences : [];
  return list.filter(seq => {
    if (!seq) return false;

    // Status Filter
    if (filterStatus.value === 'ACTIVE' && !seq.enabled) return false;
    if (filterStatus.value === 'DISABLED' && seq.enabled) return false;

    // Gateway Filter
    if (filterGateway.value !== 'ALL') {
      const gws = Array.isArray(seq.targetGatewayInstances) ? seq.targetGatewayInstances : [];
      if (!gws.includes(filterGateway.value) && !gws.includes('*') && gws.length > 0) {
        return false;
      }
    }

    // Search Query
    if (searchQuery.value && searchQuery.value.trim()) {
      const q = searchQuery.value.trim().toLowerCase();
      const matchId = (seq.sequenceId || '').toLowerCase().includes(q);
      const matchName = (seq.sequenceName || '').toLowerCase().includes(q);
      const matchDesc = (seq.description || seq.sequenceDescription || '').toLowerCase().includes(q);
      const matchGws = (Array.isArray(seq.targetGatewayInstances) ? seq.targetGatewayInstances : []).some(g => (g || '').toLowerCase().includes(q));
      const matchTriggers = (Array.isArray(seq.targetTriggerTypes) ? seq.targetTriggerTypes : []).some(t => (t || '').toLowerCase().includes(q));
      const matchActivities = getActivityList(seq.activityIds).some(a => (a || '').toLowerCase().includes(q));
      if (!matchId && !matchName && !matchDesc && !matchGws && !matchTriggers && !matchActivities) return false;
    }

    return true;
  });
});
</script>

<template>
  <div class="space-y-6">
    <!-- Header -->
    <div class="flex flex-col md:flex-row items-start md:items-center justify-between gap-4">
      <div>
        <div class="flex items-center gap-2">
          <h1 class="text-2xl font-bold text-white">Task Sequences</h1>
          <span class="badge badge-green">{{ sequenceStore.sequences.length }} Registered</span>
        </div>
        <p class="subtitle mt-1">Configure event-driven activity pipelines, per-gateway filtering, and execution order.</p>
      </div>

      <div class="flex items-center gap-2">
        <button 
          @click="sequenceStore.syncSequences()" 
          :disabled="sequenceStore.syncing"
          class="btn btn-secondary flex items-center gap-1.5"
          title="Synchronise queues and task sequences with Task Sequence Processor"
        >
          <RefreshCw :size="15" :class="{ 'animate-spin': sequenceStore.syncing }" />
          <span>{{ sequenceStore.syncing ? 'Synchronising...' : 'Synchronise' }}</span>
        </button>

        <button @click="openCreateModal" class="btn btn-primary">
          <Plus :size="16" />
          New Task Sequence
        </button>
      </div>
    </div>

    <!-- Sync Feedback Notification -->
    <div v-if="sequenceStore.syncMessage" class="p-3 bg-emerald-950/80 border border-emerald-500/50 rounded-lg text-emerald-300 text-xs flex items-center justify-between">
      <div class="flex items-center gap-2">
        <Check :size="16" class="text-emerald-400" />
        <span>{{ sequenceStore.syncMessage }}</span>
      </div>
      <button @click="sequenceStore.syncMessage = null" class="text-emerald-400 hover:text-white">
        <X :size="14" />
      </button>
    </div>

    <div v-if="sequenceStore.error" class="p-3 bg-rose-950/80 border border-rose-500/50 rounded-lg text-rose-300 text-xs flex items-center justify-between">
      <div class="flex items-center gap-2">
        <X :size="16" class="text-rose-400" />
        <span>{{ sequenceStore.error }}</span>
      </div>
      <button @click="sequenceStore.error = null" class="text-rose-400 hover:text-white">
        <X :size="14" />
      </button>
    </div>

    <!-- Filters & Search Toolbar -->
    <div class="card flex flex-col md:flex-row items-center justify-between gap-4 py-3">
      <div class="flex items-center gap-2 w-full md:w-96">
        <div class="relative w-full">
          <Search :size="16" class="absolute left-2.5 top-2.5 text-slate-400" />
          <input 
            v-model="searchQuery" 
            type="text" 
            placeholder="Search by ID, name, activities..." 
            class="form-input w-full text-xs py-1.5 pl-8 pr-7"
          />
          <button 
            v-if="searchQuery" 
            @click="searchQuery = ''" 
            class="absolute right-2 top-2 text-slate-400 hover:text-white"
            title="Clear Search"
          >
            <X :size="14" />
          </button>
        </div>
      </div>

      <div class="flex items-center gap-3 w-full md:w-auto justify-end">
        <div class="flex items-center gap-1.5 text-xs">
          <Filter :size="14" class="text-slate-400" />
          <span class="text-slate-400 font-medium">Status:</span>
          <select v-model="filterStatus" class="form-select text-xs py-1">
            <option value="ALL">All Statuses</option>
            <option value="ACTIVE">Active Only</option>
            <option value="DISABLED">Disabled Only</option>
          </select>
        </div>

        <div class="flex items-center gap-1.5 text-xs">
          <span class="text-slate-400 font-medium">Gateway:</span>
          <select v-model="filterGateway" class="form-select text-xs py-1">
            <option value="ALL">All Gateways</option>
            <option value="pas-gw">pas-gw (PAS)</option>
            <option value="lims-gw">lims-gw (LIMS)</option>
            <option value="mllp-gateway-default">mllp-gateway-default</option>
          </select>
        </div>

        <button @click="sequenceStore.fetchSequences()" class="btn btn-secondary text-xs py-1 px-2.5" title="Refresh Sequences">
          <RefreshCw :size="13" />
          <span>Refresh</span>
        </button>
      </div>
    </div>

    <!-- Sequences Table -->
    <div class="table-container">
      <table class="table">
        <thead>
          <tr>
            <th style="width: 80px;">State</th>
            <th>Sequence ID &amp; Name</th>
            <th>Source Queue</th>
            <th>Target Gateways</th>
            <th>Trigger Events</th>
            <th>Chained Activities</th>
            <th class="text-right">Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="seq in filteredSequences" :key="seq.sequenceId">
            <!-- State Toggle -->
            <td>
              <button 
                @click="sequenceStore.toggleSequence(seq)"
                class="btn text-xs py-1 px-2"
                :class="seq.enabled ? 'btn-secondary text-emerald-400' : 'btn-secondary text-amber-400'"
                :title="seq.enabled ? 'Click to Disable' : 'Click to Enable'"
              >
                <component :is="seq.enabled ? PlayCircle : PauseCircle" :size="15" />
                <span>{{ seq.enabled ? 'Active' : 'Off' }}</span>
              </button>
            </td>

            <!-- Sequence ID & Name -->
            <td>
              <router-link :to="'/sequences/' + seq.sequenceId" class="font-semibold text-sky-400 hover:underline block">
                {{ seq.sequenceName }}
              </router-link>
              <div class="font-mono text-[11px] text-slate-400 mt-0.5">{{ seq.sequenceId }}</div>
              <div v-if="seq.description || seq.sequenceDescription" class="text-xs text-slate-400 mt-1 line-clamp-1">{{ seq.description || seq.sequenceDescription }}</div>
            </td>

            <!-- Source Queue -->
            <td class="font-mono text-xs text-slate-300">
              {{ seq.sourceQueueName || 'jms:queue:task.event.queue.*' }}
            </td>

            <!-- Gateways -->
            <td>
              <div class="flex flex-wrap gap-1">
                <span v-for="gw in seq.targetGatewayInstances" :key="gw" class="badge badge-blue text-[10px]">
                  {{ gw }}
                </span>
                <span v-if="!seq.targetGatewayInstances || seq.targetGatewayInstances.length === 0" class="badge badge-purple text-[10px]">
                  * (All)
                </span>
              </div>
            </td>

            <!-- Triggers -->
            <td>
              <div class="flex flex-wrap gap-1">
                <span v-for="trig in seq.targetTriggerTypes" :key="trig" class="badge badge-amber text-[10px]">
                  {{ trig }}
                </span>
                <span v-if="!seq.targetTriggerTypes || seq.targetTriggerTypes.length === 0" class="badge badge-purple text-[10px]">
                  * (All)
                </span>
              </div>
            </td>

            <!-- Chained Activities -->
            <td>
              <div class="flex items-center flex-wrap gap-1">
                <div 
                  v-for="(act, idx) in getActivityList(seq.activityIds)" 
                  :key="act"
                  class="flex items-center gap-1"
                >
                  <span class="badge badge-green text-[10px] font-mono">{{ act }}</span>
                  <span v-if="idx < (getActivityList(seq.activityIds).length - 1)" class="text-slate-500 text-xs">&rarr;</span>
                </div>
                <span v-if="getActivityList(seq.activityIds).length === 0" class="text-xs text-slate-500 italic">
                  No activities
                </span>
              </div>
            </td>

            <!-- Actions -->
            <td class="text-right">
              <div class="flex items-center justify-end gap-1.5">
                <router-link :to="'/sequences/' + seq.sequenceId" class="btn btn-secondary text-xs py-1 px-2 text-sky-400" title="Visual Pipeline Flow">
                  <Eye :size="13" />
                </router-link>
                <button @click="viewJson(seq)" class="btn btn-secondary text-xs py-1 px-2 text-purple-400" title="View JSON">
                  <Code :size="13" />
                </button>
                <button @click="openEditModal(seq)" class="btn btn-secondary text-xs py-1 px-2 text-amber-400" title="Edit Sequence">
                  <Edit2 :size="13" />
                </button>
                <button @click="sequenceStore.deleteSequence(seq.sequenceId)" class="btn btn-danger text-xs py-1 px-2" title="Delete Sequence">
                  <Trash2 :size="13" />
                </button>
              </div>
            </td>
          </tr>
          <tr v-if="filteredSequences.length === 0">
            <td colspan="7" class="text-center py-10 text-slate-400 space-y-2">
              <div class="text-sm font-medium">No matching task sequences found.</div>
              <div v-if="searchQuery || filterStatus !== 'ALL' || filterGateway !== 'ALL'" class="pt-1">
                <button @click="clearSearch" class="btn btn-secondary text-xs py-1 px-3">
                  Clear Filters &amp; Reset Search
                </button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Create / Edit Modal -->
    <div v-if="showModal" class="modal-overlay" @click.self="showModal = false">
      <div class="modal-content space-y-4">
        <div class="flex items-center justify-between border-b border-slate-700 pb-3">
          <h3 class="text-lg font-bold text-white flex items-center gap-2">
            <GitMerge :size="18" class="text-emerald-400" />
            {{ editingSequence.sequenceId ? 'Edit Task Sequence' : 'Create Task Sequence' }}
          </h3>
          <button @click="showModal = false" class="text-slate-400 hover:text-white">
            <X :size="18" />
          </button>
        </div>

        <div class="space-y-3">
          <div class="grid grid-cols-2 gap-3">
            <div class="form-group">
              <label class="form-label">Sequence ID</label>
              <input v-model="editingSequence.sequenceId" type="text" class="form-input font-mono text-xs" />
            </div>
            <div class="form-group">
              <label class="form-label">Version</label>
              <input v-model="editingSequence.version" type="text" class="form-input text-xs" placeholder="1.0.0" />
            </div>
          </div>

          <div class="form-group">
            <label class="form-label">Sequence Name</label>
            <input v-model="editingSequence.sequenceName" type="text" class="form-input" placeholder="e.g. Patient Demographics & Identity Pipeline" />
          </div>

          <div class="form-group">
            <label class="form-label">Description</label>
            <textarea v-model="editingSequence.description" class="form-textarea" rows="2" placeholder="Sequence purpose and routing details..."></textarea>
          </div>

          <div class="form-group">
            <label class="form-label">Source Dedicated Queue (Optional)</label>
            <input v-model="editingSequence.sourceQueueName" type="text" class="form-input font-mono text-xs" placeholder="e.g. jms:queue:task.event.queue.pas-gw" />
          </div>

          <div class="grid grid-cols-2 gap-3">
            <div class="form-group">
              <label class="form-label">Target Gateways (comma separated)</label>
              <input v-model="gatewayInput" type="text" class="form-input text-xs" placeholder="pas-gw, lims-gw, *" />
            </div>
            <div class="form-group">
              <label class="form-label">Target Trigger Types (comma separated)</label>
              <input v-model="triggerInput" type="text" class="form-input text-xs" placeholder="ADT^A01, ADT^A08, *" />
            </div>
          </div>

          <div class="form-group">
            <label class="form-label">Chained Activities (comma separated, in execution order)</label>
            <input v-model="activitiesInput" type="text" class="form-input font-mono text-xs" placeholder="patient-identity-update, patient-demographics-update" />
            <span class="text-[11px] text-slate-400">Activities will execute sequentially in the exact order listed.</span>
          </div>

          <div class="flex items-center gap-2 pt-2">
            <input id="seq-active" v-model="editingSequence.enabled" type="checkbox" class="w-4 h-4 rounded border-slate-700 text-emerald-500" />
            <label for="seq-active" class="text-sm font-semibold text-white">Enable Sequence Pipeline on Save</label>
          </div>
        </div>

        <div class="flex items-center justify-end gap-3 border-t border-slate-700 pt-3">
          <button @click="showModal = false" class="btn btn-secondary">Cancel</button>
          <button @click="saveSequenceForm" class="btn btn-primary">Save Task Sequence</button>
        </div>
      </div>
    </div>

    <!-- JSON Preview Modal -->
    <div v-if="showJsonModal" class="modal-overlay" @click.self="showJsonModal = false">
      <div class="modal-content space-y-4 max-w-2xl">
        <div class="flex items-center justify-between border-b border-slate-700 pb-3">
          <h3 class="text-base font-bold text-white flex items-center gap-2">
            <Code :size="16" class="text-purple-400" />
            TaskSequence JSON Definition
          </h3>
          <button @click="showJsonModal = false" class="text-slate-400 hover:text-white">
            <X :size="18" />
          </button>
        </div>
        <pre class="code-view">{{ activeJson }}</pre>
        <div class="flex justify-end">
          <button @click="showJsonModal = false" class="btn btn-secondary">Close</button>
        </div>
      </div>
    </div>
  </div>
</template>
