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
import { useSecurityStore } from '../stores/securityStore';
import type { AuditEvent } from '../models/fhir';
import { Plus, Search, Trash2, Eye, ShieldAlert, X } from 'lucide-vue-next';

const store = useSecurityStore();
const searchName = ref('');
const showCreateModal = ref(false);
const showDetailModal = ref(false);
const selectedResource = ref<any>(null);

const newAudit = ref({
  action: 'C' as 'C' | 'R' | 'U' | 'D' | 'E',
  code: 'REST Security Audit',
  agent: '',
  severity: 'informational' as 'emergency' | 'alert' | 'critical' | 'error' | 'warning' | 'notice' | 'informational' | 'debug'
});

onMounted(() => {
  store.fetchAuditEvents();
});

const handleSearch = () => {
  store.fetchAuditEvents(searchName.value);
};

const handleCreate = async () => {
  const audit: Partial<AuditEvent> = {
    resourceType: 'AuditEvent',
    action: newAudit.value.action,
    severity: newAudit.value.severity,
    recorded: new Date().toISOString(),
    code: { text: newAudit.value.code },
    agent: [{
      who: { display: newAudit.value.agent || 'Security Monitor' }
    }]
  };

  await store.createAuditEvent(audit);
  showCreateModal.value = false;
  newAudit.value = { action: 'C', code: 'REST Security Audit', agent: '', severity: 'informational' };
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
          <ShieldAlert class="text-rose-400" :size="24" />
          Security Audit Events
        </h1>
        <p class="subtitle mt-1">Audit log of security, access control, policy and clinical events in FHIR R5.</p>
      </div>

      <button @click="showCreateModal = true" class="btn btn-primary">
        <Plus :size="16" />
        <span>Emit Audit Event</span>
      </button>
    </div>

    <!-- Search Bar -->
    <div class="card p-4 flex flex-col md:flex-row items-center justify-between gap-4">
      <span class="text-xs text-slate-400 font-semibold uppercase">Total Audit Events: {{ store.auditEvents.length }}</span>
      <div class="flex items-center gap-2 w-full md:w-80">
        <input 
          v-model="searchName" 
          @keyup.enter="handleSearch"
          type="text" 
          placeholder="Filter action or agent..." 
          class="form-input flex-1 text-xs" 
        />
        <button @click="handleSearch" class="btn btn-secondary text-xs">
          <Search :size="14" />
        </button>
      </div>
    </div>

    <!-- AuditEvents Table -->
    <div class="table-container">
      <table class="table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Action</th>
            <th>Description / Code</th>
            <th>Severity</th>
            <th>Agent</th>
            <th>Recorded Date</th>
            <th class="text-right">Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="store.auditEvents.length === 0">
            <td colspan="7" class="text-center text-slate-500 py-6">No Audit Events recorded. Click Emit Audit Event above.</td>
          </tr>
          <tr v-for="audit in store.auditEvents" :key="audit.id">
            <td class="font-mono text-xs text-rose-400 font-semibold">{{ audit.id }}</td>
            <td>
              <span class="badge" :class="audit.action === 'D' ? 'badge-red' : audit.action === 'C' ? 'badge-green' : 'badge-blue'">
                {{ audit.action === 'C' ? 'CREATE' : audit.action === 'R' ? 'READ' : audit.action === 'U' ? 'UPDATE' : audit.action === 'D' ? 'DELETE' : 'EXECUTE' }}
              </span>
            </td>
            <td class="font-medium text-white">{{ audit.code?.text || 'Security Event' }}</td>
            <td>
              <span class="badge badge-purple">{{ audit.severity || 'informational' }}</span>
            </td>
            <td class="text-slate-300 text-xs">{{ audit.agent?.[0]?.who?.display || 'System' }}</td>
            <td class="text-slate-400 text-xs">{{ audit.recorded || '-' }}</td>
            <td class="text-right space-x-2">
              <button @click="viewDetails(audit)" class="btn btn-secondary py-1 px-2 text-xs" title="View FHIR JSON">
                <Eye :size="14" />
              </button>
              <button @click="audit.id && store.deleteAuditEvent(audit.id)" class="btn btn-danger py-1 px-2 text-xs" title="Delete">
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
          <h3 class="text-lg font-bold text-white">Emit Audit Event</h3>
          <button @click="showCreateModal = false" class="text-slate-400 hover:text-white">
            <X :size="20" />
          </button>
        </div>

        <div class="space-y-4">
          <div class="form-group">
            <label class="form-label">Event Description / Code *</label>
            <input v-model="newAudit.code" type="text" placeholder="e.g. Patient Record Accessed" class="form-input" required />
          </div>

          <div class="grid grid-cols-2 gap-3">
            <div class="form-group">
              <label class="form-label">Action Type</label>
              <select v-model="newAudit.action" class="form-select">
                <option value="C">CREATE</option>
                <option value="R">READ</option>
                <option value="U">UPDATE</option>
                <option value="D">DELETE</option>
                <option value="E">EXECUTE</option>
              </select>
            </div>
            <div class="form-group">
              <label class="form-label">Severity Level</label>
              <select v-model="newAudit.severity" class="form-select">
                <option value="informational">Informational</option>
                <option value="notice">Notice</option>
                <option value="warning">Warning</option>
                <option value="error">Error</option>
                <option value="critical">Critical</option>
              </select>
            </div>
          </div>

          <div class="form-group">
            <label class="form-label">Responsible Agent</label>
            <input v-model="newAudit.agent" type="text" placeholder="e.g. Audit Monitor or Practitioner/2001" class="form-input" />
          </div>

          <div class="flex justify-end gap-2 pt-4 border-t border-[#27344d]">
            <button @click="showCreateModal = false" class="btn btn-secondary">Cancel</button>
            <button @click="handleCreate" class="btn btn-primary">Save Audit Event</button>
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
