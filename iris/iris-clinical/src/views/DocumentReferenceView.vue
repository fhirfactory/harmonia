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
import type { DocumentReference } from '../models/fhir';
import { Plus, Search, Trash2, Eye, FileText, X } from 'lucide-vue-next';

const store = useWorkflowStore();
const searchName = ref('');
const showCreateModal = ref(false);
const showDetailModal = ref(false);
const selectedResource = ref<any>(null);

const newDoc = ref({
  description: '',
  status: 'current' as 'current' | 'superseded' | 'entered-in-error',
  docStatus: 'final' as 'registered' | 'partial' | 'preliminary' | 'final' | 'amended' | 'corrected' | 'appended' | 'cancelled' | 'entered-in-error' | 'deprecated' | 'unknown',
  subject: '',
  title: '',
  mimeType: 'application/pdf',
  identifier: ''
});

onMounted(() => {
  store.fetchDocumentReferences();
});

const handleSearch = () => {
  store.fetchDocumentReferences(searchName.value);
};

const handleCreate = async () => {
  if (!newDoc.value.description) return;
  const doc: Partial<DocumentReference> = {
    resourceType: 'DocumentReference',
    description: newDoc.value.description,
    status: newDoc.value.status,
    docStatus: newDoc.value.docStatus,
    date: new Date().toISOString(),
    subject: newDoc.value.subject ? { reference: newDoc.value.subject } : undefined,
    content: [{
      attachment: {
        title: newDoc.value.title || newDoc.value.description,
        contentType: newDoc.value.mimeType
      }
    }],
    identifier: newDoc.value.identifier ? [{ system: 'urn:doc', value: newDoc.value.identifier }] : []
  };

  await store.createDocumentReference(doc);
  showCreateModal.value = false;
  newDoc.value = { description: '', status: 'current', docStatus: 'final', subject: '', title: '', mimeType: 'application/pdf', identifier: '' };
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
          <FileText class="text-indigo-400" :size="24" />
          Clinical Document Registry
        </h1>
        <p class="subtitle mt-1">Index discharge summaries, pathology reports, imaging attachments, and CDA documents in FHIR R5.</p>
      </div>

      <button @click="showCreateModal = true" class="btn btn-primary">
        <Plus :size="16" />
        <span>Register Document</span>
      </button>
    </div>

    <!-- Search Bar -->
    <div class="card p-4 flex flex-col md:flex-row items-center justify-between gap-4">
      <span class="text-xs text-slate-400 font-semibold uppercase">Total Documents: {{ store.documentReferences.length }}</span>
      <div class="flex items-center gap-2 w-full md:w-80">
        <input 
          v-model="searchName" 
          @keyup.enter="handleSearch"
          type="text" 
          placeholder="Filter description or title..." 
          class="form-input flex-1 text-xs" 
        />
        <button @click="handleSearch" class="btn btn-secondary text-xs">
          <Search :size="14" />
        </button>
      </div>
    </div>

    <!-- DocumentReferences Table -->
    <div class="table-container">
      <table class="table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Description</th>
            <th>Status</th>
            <th>Doc Status</th>
            <th>Subject</th>
            <th>Attachment Title</th>
            <th>Identifier</th>
            <th class="text-right">Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="store.documentReferences.length === 0">
            <td colspan="8" class="text-center text-slate-500 py-6">No Documents registered. Click Register Document above.</td>
          </tr>
          <tr v-for="d in store.documentReferences" :key="d.id">
            <td class="font-mono text-xs text-indigo-400 font-semibold">{{ d.id }}</td>
            <td class="font-medium text-white">{{ d.description }}</td>
            <td>
              <span class="badge" :class="d.status === 'current' ? 'badge-green' : 'badge-amber'">{{ d.status }}</span>
            </td>
            <td>
              <span class="badge badge-purple">{{ d.docStatus || 'final' }}</span>
            </td>
            <td class="font-mono text-xs text-slate-300">{{ d.subject?.reference || '-' }}</td>
            <td class="text-slate-300 text-xs">{{ d.content?.[0]?.attachment?.title || '-' }}</td>
            <td class="font-mono text-xs text-slate-300">{{ d.identifier?.[0]?.value || '-' }}</td>
            <td class="text-right space-x-2">
              <button @click="viewDetails(d)" class="btn btn-secondary py-1 px-2 text-xs" title="View FHIR JSON">
                <Eye :size="14" />
              </button>
              <button @click="d.id && store.deleteDocumentReference(d.id)" class="btn btn-danger py-1 px-2 text-xs" title="Delete">
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
          <h3 class="text-lg font-bold text-white">Register Clinical Document</h3>
          <button @click="showCreateModal = false" class="text-slate-400 hover:text-white">
            <X :size="20" />
          </button>
        </div>

        <div class="space-y-4">
          <div class="form-group">
            <label class="form-label">Document Description / Summary *</label>
            <input v-model="newDoc.description" type="text" placeholder="e.g. Inpatient Discharge Clinical Summary" class="form-input" required />
          </div>

          <div class="grid grid-cols-2 gap-3">
            <div class="form-group">
              <label class="form-label">Attachment Title</label>
              <input v-model="newDoc.title" type="text" placeholder="e.g. Discharge_Summary_2026.pdf" class="form-input" />
            </div>
            <div class="form-group">
              <label class="form-label">Content MIME Type</label>
              <select v-model="newDoc.mimeType" class="form-select">
                <option value="application/pdf">PDF Document (application/pdf)</option>
                <option value="text/plain">Plain Text (text/plain)</option>
                <option value="application/json">JSON (application/json)</option>
                <option value="text/xml">CDA XML (text/xml)</option>
              </select>
            </div>
          </div>

          <div class="grid grid-cols-2 gap-3">
            <div class="form-group">
              <label class="form-label">Lifecycle Status</label>
              <select v-model="newDoc.status" class="form-select">
                <option value="current">Current</option>
                <option value="superseded">Superseded</option>
              </select>
            </div>
            <div class="form-group">
              <label class="form-label">Clinical Document Status</label>
              <select v-model="newDoc.docStatus" class="form-select">
                <option value="final">Final</option>
                <option value="preliminary">Preliminary</option>
                <option value="amended">Amended</option>
              </select>
            </div>
          </div>

          <div class="grid grid-cols-2 gap-3">
            <div class="form-group">
              <label class="form-label">Subject Reference</label>
              <input v-model="newDoc.subject" type="text" placeholder="e.g. Person/1001" class="form-input" />
            </div>
            <div class="form-group">
              <label class="form-label">Document Identifier</label>
              <input v-model="newDoc.identifier" type="text" placeholder="e.g. DOC-DISCH-001" class="form-input" />
            </div>
          </div>

          <div class="flex justify-end gap-2 pt-4 border-t border-[#27344d]">
            <button @click="showCreateModal = false" class="btn btn-secondary">Cancel</button>
            <button @click="handleCreate" class="btn btn-primary">Save Document</button>
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
