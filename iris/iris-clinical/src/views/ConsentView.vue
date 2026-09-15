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
import type { Consent } from '../models/fhir';
import { Plus, Search, Trash2, Eye, FileCheck, X } from 'lucide-vue-next';

const store = useSecurityStore();
const searchName = ref('');
const showCreateModal = ref(false);
const showDetailModal = ref(false);
const selectedResource = ref<any>(null);

const newConsent = ref({
  category: 'Medical Research Consent',
  status: 'active' as 'draft' | 'active' | 'inactive' | 'not-done' | 'entered-in-error' | 'unknown',
  decision: 'permit' as 'deny' | 'permit',
  subject: '',
  identifier: ''
});

onMounted(() => {
  store.fetchConsents();
});

const handleSearch = () => {
  store.fetchConsents(searchName.value);
};

const handleCreate = async () => {
  const consent: Partial<Consent> = {
    resourceType: 'Consent',
    status: newConsent.value.status,
    decision: newConsent.value.decision,
    date: new Date().toISOString().split('T')[0],
    category: [{ text: newConsent.value.category }],
    subject: newConsent.value.subject ? { reference: newConsent.value.subject } : undefined,
    identifier: newConsent.value.identifier ? [{ system: 'urn:consent', value: newConsent.value.identifier }] : []
  };

  await store.createConsent(consent);
  showCreateModal.value = false;
  newConsent.value = { category: 'Medical Research Consent', status: 'active', decision: 'permit', subject: '', identifier: '' };
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
          <FileCheck class="text-emerald-400" :size="24" />
          Patient Consent & Directives
        </h1>
        <p class="subtitle mt-1">Manage health data sharing authorizations, research consent, and clinical disclosures in FHIR R5.</p>
      </div>

      <button @click="showCreateModal = true" class="btn btn-primary">
        <Plus :size="16" />
        <span>Register Consent</span>
      </button>
    </div>

    <!-- Search Bar -->
    <div class="card p-4 flex flex-col md:flex-row items-center justify-between gap-4">
      <span class="text-xs text-slate-400 font-semibold uppercase">Total Consents: {{ store.consents.length }}</span>
      <div class="flex items-center gap-2 w-full md:w-80">
        <input 
          v-model="searchName" 
          @keyup.enter="handleSearch"
          type="text" 
          placeholder="Filter category or identifier..." 
          class="form-input flex-1 text-xs" 
        />
        <button @click="handleSearch" class="btn btn-secondary text-xs">
          <Search :size="14" />
        </button>
      </div>
    </div>

    <!-- Consents Table -->
    <div class="table-container">
      <table class="table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Consent Category</th>
            <th>Status</th>
            <th>Decision</th>
            <th>Subject</th>
            <th>Identifier</th>
            <th class="text-right">Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="store.consents.length === 0">
            <td colspan="7" class="text-center text-slate-500 py-6">No Consents found. Click Register Consent above.</td>
          </tr>
          <tr v-for="c in store.consents" :key="c.id">
            <td class="font-mono text-xs text-emerald-400 font-semibold">{{ c.id }}</td>
            <td class="font-medium text-white">{{ c.category?.[0]?.text || 'Patient Consent' }}</td>
            <td>
              <span class="badge" :class="c.status === 'active' ? 'badge-green' : 'badge-amber'">{{ c.status }}</span>
            </td>
            <td>
              <span class="badge" :class="c.decision === 'permit' ? 'badge-blue' : 'badge-red'">{{ c.decision || 'permit' }}</span>
            </td>
            <td class="font-mono text-xs text-slate-300">{{ c.subject?.reference || '-' }}</td>
            <td class="font-mono text-xs text-slate-300">{{ c.identifier?.[0]?.value || '-' }}</td>
            <td class="text-right space-x-2">
              <button @click="viewDetails(c)" class="btn btn-secondary py-1 px-2 text-xs" title="View FHIR JSON">
                <Eye :size="14" />
              </button>
              <button @click="c.id && store.deleteConsent(c.id)" class="btn btn-danger py-1 px-2 text-xs" title="Delete">
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
          <h3 class="text-lg font-bold text-white">Register Patient Consent</h3>
          <button @click="showCreateModal = false" class="text-slate-400 hover:text-white">
            <X :size="20" />
          </button>
        </div>

        <div class="space-y-4">
          <div class="form-group">
            <label class="form-label">Category / Directive Name *</label>
            <input v-model="newConsent.category" type="text" placeholder="e.g. Health Information Exchange Sharing" class="form-input" required />
          </div>

          <div class="grid grid-cols-2 gap-3">
            <div class="form-group">
              <label class="form-label">Consent State</label>
              <select v-model="newConsent.status" class="form-select">
                <option value="active">Active</option>
                <option value="draft">Draft</option>
                <option value="inactive">Inactive</option>
              </select>
            </div>
            <div class="form-group">
              <label class="form-label">Policy Decision</label>
              <select v-model="newConsent.decision" class="form-select">
                <option value="permit">Permit (Opt-in)</option>
                <option value="deny">Deny (Opt-out)</option>
              </select>
            </div>
          </div>

          <div class="grid grid-cols-2 gap-3">
            <div class="form-group">
              <label class="form-label">Subject Reference</label>
              <input v-model="newConsent.subject" type="text" placeholder="e.g. Person/1001" class="form-input" />
            </div>
            <div class="form-group">
              <label class="form-label">Consent Identifier</label>
              <input v-model="newConsent.identifier" type="text" placeholder="e.g. CONSENT-2026-99" class="form-input" />
            </div>
          </div>

          <div class="flex justify-end gap-2 pt-4 border-t border-[#27344d]">
            <button @click="showCreateModal = false" class="btn btn-secondary">Cancel</button>
            <button @click="handleCreate" class="btn btn-primary">Save Consent</button>
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
