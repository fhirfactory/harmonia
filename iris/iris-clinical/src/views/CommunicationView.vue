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
import { ref, onMounted } from 'vue';
import { useWorkflowStore } from '../stores/workflowStore';
import type { Communication } from '../models/fhir';
import SecurityBadge from '../components/SecurityBadge.vue';
import { Plus, Search, Trash2, Eye, MessageSquare, X } from 'lucide-vue-next';

const store = useWorkflowStore();
const searchName = ref('');
const showCreateModal = ref(false);
const showDetailModal = ref(false);
const selectedResource = ref<any>(null);

const newComm = ref({
  subjectDisplay: '',
  subjectRef: '',
  note: '',
  status: 'completed' as 'preparation' | 'in-progress' | 'not-done' | 'on-hold' | 'stopped' | 'completed' | 'entered-in-error' | 'unknown',
  priority: 'routine' as 'routine' | 'urgent' | 'asap' | 'stat',
  identifier: ''
});

onMounted(() => {
  store.fetchCommunications();
});

const handleSearch = () => {
  store.fetchCommunications(searchName.value);
};

const handleCreate = async () => {
  const comm: Partial<Communication> = {
    resourceType: 'Communication',
    status: newComm.value.status,
    priority: newComm.value.priority,
    sent: new Date().toISOString(),
    subject: newComm.value.subjectRef ? { reference: newComm.value.subjectRef, display: newComm.value.subjectDisplay || undefined } : undefined,
    note: newComm.value.note ? [{ text: newComm.value.note }] : [],
    identifier: newComm.value.identifier ? [{ system: 'urn:comm', value: newComm.value.identifier }] : []
  };

  await store.createCommunication(comm);
  showCreateModal.value = false;
  newComm.value = { subjectDisplay: '', subjectRef: '', note: '', status: 'completed', priority: 'routine', identifier: '' };
};

const viewDetails = (item: any) => {
  selectedResource.value = item;
  showDetailModal.value = true;
};
</script>

<template>
  <div class="space-y-6">
    <div class="flex flex-col md:flex-row md:items-center justify-between gap-4">
      <div>
        <h1 class="text-2xl font-bold text-white flex items-center gap-2">
          <MessageSquare class="text-teal-400" :size="24" />
          Clinical Communications
        </h1>
        <p class="subtitle mt-1">Convey patient notifications, provider messaging, reminders, and alerts in FHIR R5.</p>
      </div>

      <button @click="showCreateModal = true" class="btn btn-primary">
        <Plus :size="16" />
        <span>Log Communication</span>
      </button>
    </div>

    <!-- Search Bar -->
    <div class="card p-4 flex flex-col md:flex-row items-center justify-between gap-4">
      <span class="text-xs text-slate-400 font-semibold uppercase">Total Communications: {{ store.communications.length }}</span>
      <div class="flex items-center gap-2 w-full md:w-80">
        <input 
          v-model="searchName" 
          @keyup.enter="handleSearch"
          type="text" 
          placeholder="Filter subject or message..." 
          class="form-input flex-1 text-xs" 
        />
        <button @click="handleSearch" class="btn btn-secondary text-xs">
          <Search :size="14" />
        </button>
      </div>
    </div>

    <!-- Communications Table -->
    <div class="table-container">
      <table class="table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Subject</th>
            <th>Status</th>
            <th>Priority</th>
            <th>Note / Message Summary</th>
            <th>Sent At</th>
            <th>Identifier</th>
            <th>Confidentiality</th>
            <th class="text-right">Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="store.communications.length === 0">
            <td colspan="9" class="text-center text-slate-500 py-6">No Communications logged. Click Log Communication above.</td>
          </tr>
          <tr v-for="c in store.communications" :key="c.id">
            <td class="font-mono text-xs text-teal-400 font-semibold">{{ c.id }}</td>
            <td class="font-medium text-white">{{ c.subject?.display || c.subject?.reference || '-' }}</td>
            <td>
              <span class="badge" :class="c.status === 'completed' ? 'badge-green' : 'badge-amber'">
                {{ c.status }}
              </span>
            </td>
            <td>
              <span class="badge" :class="c.priority === 'urgent' || c.priority === 'stat' ? 'badge-red' : 'badge-blue'">
                {{ c.priority || 'routine' }}
              </span>
            </td>
            <td class="text-slate-300 text-xs">{{ c.note?.[0]?.text || '-' }}</td>
            <td class="text-slate-400 text-xs">{{ c.sent || '-' }}</td>
            <td class="font-mono text-xs text-slate-300">{{ c.identifier?.[0]?.value || '-' }}</td>
            <td>
              <SecurityBadge :meta="c.meta" />
            </td>
            <td class="text-right space-x-2">
              <button @click="viewDetails(c)" class="btn btn-secondary py-1 px-2 text-xs" title="View FHIR JSON">
                <Eye :size="14" />
              </button>
              <button @click="c.id && store.deleteCommunication(c.id)" class="btn btn-danger py-1 px-2 text-xs" title="Delete">
                <Trash2 :size="14" />
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Create Modal -->
    <div v-if="showCreateModal" class="modal-overlay" @click.self="showCreateModal = false">
      <div class="modal-content">
        <div class="flex items-center justify-between pb-3 border-b border-[#27344d] mb-4">
          <h3 class="text-lg font-bold text-white">Log Communication</h3>
          <button @click="showCreateModal = false" class="text-slate-400 hover:text-white">
            <X :size="20" />
          </button>
        </div>

        <div class="space-y-4">
          <div class="grid grid-cols-2 gap-3">
            <div class="form-group">
              <label class="form-label">Subject Reference</label>
              <input v-model="newComm.subjectRef" type="text" placeholder="e.g. Person/1001" class="form-input" />
            </div>
            <div class="form-group">
              <label class="form-label">Subject Name / Display</label>
              <input v-model="newComm.subjectDisplay" type="text" placeholder="e.g. Alice Smith" class="form-input" />
            </div>
          </div>

          <div class="grid grid-cols-2 gap-3">
            <div class="form-group">
              <label class="form-label">Status</label>
              <select v-model="newComm.status" class="form-select">
                <option value="completed">Completed</option>
                <option value="in-progress">In Progress</option>
                <option value="preparation">Preparation</option>
                <option value="on-hold">On Hold</option>
              </select>
            </div>
            <div class="form-group">
              <label class="form-label">Priority</label>
              <select v-model="newComm.priority" class="form-select">
                <option value="routine">Routine</option>
                <option value="urgent">Urgent</option>
                <option value="asap">ASAP</option>
                <option value="stat">STAT</option>
              </select>
            </div>
          </div>

          <div class="form-group">
            <label class="form-label">Message Content / Note *</label>
            <input v-model="newComm.note" type="text" placeholder="e.g. Patient appointment follow-up and discharge guidance sent." class="form-input" required />
          </div>

          <div class="form-group">
            <label class="form-label">Communication Identifier</label>
            <input v-model="newComm.identifier" type="text" placeholder="e.g. COMM-8891" class="form-input" />
          </div>

          <div class="flex justify-end gap-2 pt-4 border-t border-[#27344d]">
            <button @click="showCreateModal = false" class="btn btn-secondary">Cancel</button>
            <button @click="handleCreate" class="btn btn-primary">Save Communication</button>
          </div>
        </div>
      </div>
    </div>

    <!-- Detail JSON Modal -->
    <div v-if="showDetailModal" class="modal-overlay" @click.self="showDetailModal = false">
      <div class="modal-content max-w-2xl">
        <div class="flex items-center justify-between pb-3 border-b border-[#27344d] mb-4">
          <div class="flex items-center gap-2">
            <h3 class="text-lg font-bold text-white font-mono">
              {{ selectedResource?.resourceType }}/{{ selectedResource?.id }}
            </h3>
            <SecurityBadge v-if="selectedResource" :meta="selectedResource.meta" />
          </div>
          <button @click="showDetailModal = false" class="text-slate-400 hover:text-white">
            <X :size="20" />
          </button>
        </div>
        <pre class="code-view">{{ JSON.stringify(selectedResource, null, 2) }}</pre>
      </div>
    </div>
  </div>
</template>
