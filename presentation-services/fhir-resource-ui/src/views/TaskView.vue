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
import type { Task } from '../models/fhir';
import { Plus, Search, Trash2, Eye, CheckSquare, X } from 'lucide-vue-next';

const store = useWorkflowStore();
const searchName = ref('');
const showCreateModal = ref(false);
const showDetailModal = ref(false);
const selectedResource = ref<any>(null);

const newTask = ref({
  description: '',
  status: 'requested' as 'draft' | 'requested' | 'received' | 'accepted' | 'rejected' | 'ready' | 'cancelled' | 'in-progress' | 'on-hold' | 'failed' | 'completed' | 'entered-in-error',
  intent: 'order' as 'unknown' | 'proposal' | 'plan' | 'order' | 'original-order' | 'reflex-order' | 'filler-order' | 'instance-order' | 'option',
  priority: 'routine' as 'routine' | 'urgent' | 'asap' | 'stat',
  forSubject: '',
  identifier: ''
});

onMounted(() => {
  store.fetchTasks();
});

const handleSearch = () => {
  store.fetchTasks(searchName.value);
};

const handleCreate = async () => {
  if (!newTask.value.description) return;
  const task: Partial<Task> = {
    resourceType: 'Task',
    description: newTask.value.description,
    status: newTask.value.status,
    intent: newTask.value.intent,
    priority: newTask.value.priority,
    authoredOn: new Date().toISOString(),
    for: newTask.value.forSubject ? { reference: newTask.value.forSubject } : undefined,
    identifier: newTask.value.identifier ? [{ system: 'urn:task', value: newTask.value.identifier }] : []
  };

  await store.createTask(task);
  showCreateModal.value = false;
  newTask.value = { description: '', status: 'requested', intent: 'order', priority: 'routine', forSubject: '', identifier: '' };
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
          <CheckSquare class="text-sky-400" :size="24" />
          Clinical & Operational Tasks
        </h1>
        <p class="subtitle mt-1">Manage clinical workflows, lab requests, task assignment, and action queues in FHIR R5.</p>
      </div>

      <button @click="showCreateModal = true" class="btn btn-primary">
        <Plus :size="16" />
        <span>Create Task</span>
      </button>
    </div>

    <!-- Search Bar -->
    <div class="card p-4 flex flex-col md:flex-row items-center justify-between gap-4">
      <span class="text-xs text-slate-400 font-semibold uppercase">Total Tasks: {{ store.tasks.length }}</span>
      <div class="flex items-center gap-2 w-full md:w-80">
        <input 
          v-model="searchName" 
          @keyup.enter="handleSearch"
          type="text" 
          placeholder="Filter description or ID..." 
          class="form-input flex-1 text-xs" 
        />
        <button @click="handleSearch" class="btn btn-secondary text-xs">
          <Search :size="14" />
        </button>
      </div>
    </div>

    <!-- Tasks Table -->
    <div class="table-container">
      <table class="table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Task Description</th>
            <th>Status</th>
            <th>Intent</th>
            <th>Priority</th>
            <th>For (Subject)</th>
            <th>Identifier</th>
            <th class="text-right">Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="store.tasks.length === 0">
            <td colspan="8" class="text-center text-slate-500 py-6">No Tasks found. Click Create Task above.</td>
          </tr>
          <tr v-for="t in store.tasks" :key="t.id">
            <td class="font-mono text-xs text-sky-400 font-semibold">{{ t.id }}</td>
            <td class="font-medium text-white">{{ t.description || '-' }}</td>
            <td>
              <span class="badge" :class="t.status === 'completed' ? 'badge-green' : t.status === 'in-progress' ? 'badge-purple' : 'badge-blue'">
                {{ t.status }}
              </span>
            </td>
            <td>
              <span class="badge badge-amber">{{ t.intent || 'order' }}</span>
            </td>
            <td>
              <span class="badge" :class="t.priority === 'stat' || t.priority === 'urgent' ? 'badge-red' : 'badge-green'">
                {{ t.priority || 'routine' }}
              </span>
            </td>
            <td class="font-mono text-xs text-slate-300">{{ t.for?.reference || '-' }}</td>
            <td class="font-mono text-xs text-slate-300">{{ t.identifier?.[0]?.value || '-' }}</td>
            <td class="text-right space-x-2">
              <button @click="viewDetails(t)" class="btn btn-secondary py-1 px-2 text-xs" title="View FHIR JSON">
                <Eye :size="14" />
              </button>
              <button @click="t.id && store.deleteTask(t.id)" class="btn btn-danger py-1 px-2 text-xs" title="Delete">
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
          <h3 class="text-lg font-bold text-white">Create Workflow Task</h3>
          <button @click="showCreateModal = false" class="text-slate-400 hover:text-white">
            <X :size="20" />
          </button>
        </div>

        <div class="space-y-4">
          <div class="form-group">
            <label class="form-label">Task Description *</label>
            <input v-model="newTask.description" type="text" placeholder="e.g. Review Diagnostic Pathology Report" class="form-input" required />
          </div>

          <div class="grid grid-cols-3 gap-3">
            <div class="form-group">
              <label class="form-label">Status</label>
              <select v-model="newTask.status" class="form-select">
                <option value="requested">Requested</option>
                <option value="in-progress">In Progress</option>
                <option value="completed">Completed</option>
                <option value="cancelled">Cancelled</option>
                <option value="on-hold">On Hold</option>
              </select>
            </div>
            <div class="form-group">
              <label class="form-label">Intent</label>
              <select v-model="newTask.intent" class="form-select">
                <option value="order">Order</option>
                <option value="proposal">Proposal</option>
                <option value="plan">Plan</option>
              </select>
            </div>
            <div class="form-group">
              <label class="form-label">Priority</label>
              <select v-model="newTask.priority" class="form-select">
                <option value="routine">Routine</option>
                <option value="urgent">Urgent</option>
                <option value="asap">ASAP</option>
                <option value="stat">STAT</option>
              </select>
            </div>
          </div>

          <div class="grid grid-cols-2 gap-3">
            <div class="form-group">
              <label class="form-label">For (Subject Reference)</label>
              <input v-model="newTask.forSubject" type="text" placeholder="e.g. Person/1001" class="form-input" />
            </div>
            <div class="form-group">
              <label class="form-label">Task Identifier</label>
              <input v-model="newTask.identifier" type="text" placeholder="e.g. TSK-901" class="form-input" />
            </div>
          </div>

          <div class="flex justify-end gap-2 pt-4 border-t border-[#27344d]">
            <button @click="showCreateModal = false" class="btn btn-secondary">Cancel</button>
            <button @click="handleCreate" class="btn btn-primary">Save Task</button>
          </div>
        </div>
      </div>
    </div>

    <!-- Detail JSON Modal -->
    <div v-if="showDetailModal" class="modal-overlay" @click.self="showDetailModal = false">
      <div class="modal-content max-w-2xl">
        <div class="flex items-center justify-between pb-3 border-b border-[#27344d] mb-4">
          <h3 class="text-lg font-bold text-white font-mono">
            {{ selectedResource?.resourceType }}/{{ selectedResource?.id }}
          </h3>
          <button @click="showDetailModal = false" class="text-slate-400 hover:text-white">
            <X :size="20" />
          </button>
        </div>
        <pre class="code-view">{{ JSON.stringify(selectedResource, null, 2) }}</pre>
      </div>
    </div>
  </div>
</template>
