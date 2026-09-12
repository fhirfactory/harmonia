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
import { usePractitionerStore } from '../stores/practitionerStore';
import type { Practitioner, PractitionerRole } from '../models/fhir';
import { Plus, Search, Trash2, Eye, UserCheck, Stethoscope, X } from 'lucide-vue-next';

const store = usePractitionerStore();
const activeTab = ref<'practitioners' | 'roles'>('practitioners');

const searchName = ref('');
const showCreateModal = ref(false);
const showDetailModal = ref(false);
const selectedResource = ref<any>(null);

const newPractitioner = ref({
  prefix: 'Dr.',
  given: '',
  family: '',
  gender: 'female' as 'male' | 'female' | 'other' | 'unknown',
  npi: '',
  qualification: 'MD - Internal Medicine'
});

const newRole = ref({
  specialty: 'Cardiology',
  identifier: 'ROLE-001',
  active: true
});

onMounted(() => {
  store.fetchPractitioners();
  store.fetchPractitionerRoles();
});

const handleSearch = () => {
  store.fetchPractitioners(searchName.value);
};

const handleCreatePractitioner = async () => {
  if (!newPractitioner.value.family) return;
  const p: Partial<Practitioner> = {
    resourceType: 'Practitioner',
    active: true,
    name: [{
      family: newPractitioner.value.family,
      given: newPractitioner.value.given ? [newPractitioner.value.given] : [],
      prefix: newPractitioner.value.prefix ? [newPractitioner.value.prefix] : []
    }],
    gender: newPractitioner.value.gender,
    identifier: newPractitioner.value.npi ? [{ system: 'http://hl7.org/fhir/sid/us-npi', value: newPractitioner.value.npi }] : [],
    qualification: [{
      code: { text: newPractitioner.value.qualification }
    }]
  };

  await store.createPractitioner(p);
  showCreateModal.value = false;
  newPractitioner.value = { prefix: 'Dr.', given: '', family: '', gender: 'female', npi: '', qualification: 'MD - Internal Medicine' };
};

const handleCreateRole = async () => {
  const role: Partial<PractitionerRole> = {
    resourceType: 'PractitionerRole',
    active: newRole.value.active,
    specialty: [{ text: newRole.value.specialty }],
    identifier: newRole.value.identifier ? [{ system: 'urn:roles', value: newRole.value.identifier }] : []
  };

  await store.createPractitionerRole(role);
  showCreateModal.value = false;
  newRole.value = { specialty: 'Cardiology', identifier: '', active: true };
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
          <UserCheck class="text-emerald-400" :size="24" />
          Practitioners & Clinical Roles
        </h1>
        <p class="subtitle mt-1">Manage physicians, medical staff credentials, and role assignments in FHIR R5.</p>
      </div>

      <button @click="showCreateModal = true" class="btn btn-primary">
        <Plus :size="16" />
        <span>Add {{ activeTab === 'practitioners' ? 'Practitioner' : 'Practitioner Role' }}</span>
      </button>
    </div>

    <!-- Tabs & Search -->
    <div class="card p-4 flex flex-col md:flex-row items-center justify-between gap-4">
      <div class="flex items-center gap-2 w-full md:w-auto">
        <button 
          @click="activeTab = 'practitioners'" 
          class="btn text-xs" 
          :class="activeTab === 'practitioners' ? 'btn-primary' : 'btn-secondary'"
        >
          <UserCheck :size="14" />
          <span>Practitioners ({{ store.practitioners.length }})</span>
        </button>
        <button 
          @click="activeTab = 'roles'" 
          class="btn text-xs" 
          :class="activeTab === 'roles' ? 'btn-primary' : 'btn-secondary'"
        >
          <Stethoscope :size="14" />
          <span>Roles ({{ store.practitionerRoles.length }})</span>
        </button>
      </div>

      <div class="flex items-center gap-2 w-full md:w-80">
        <input 
          v-model="searchName" 
          @keyup.enter="handleSearch"
          type="text" 
          placeholder="Filter practitioner name..." 
          class="form-input flex-1 text-xs" 
        />
        <button @click="handleSearch" class="btn btn-secondary text-xs">
          <Search :size="14" />
        </button>
      </div>
    </div>

    <!-- Practitioners Table -->
    <div v-if="activeTab === 'practitioners'" class="table-container">
      <table class="table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Name & Title</th>
            <th>Gender</th>
            <th>NPI Identifier</th>
            <th>Qualifications</th>
            <th class="text-right">Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="store.practitioners.length === 0">
            <td colspan="6" class="text-center text-slate-500 py-6">No Practitioners registered. Click Add Practitioner above.</td>
          </tr>
          <tr v-for="practitioner in store.practitioners" :key="practitioner.id">
            <td class="font-mono text-xs text-emerald-400 font-semibold">{{ practitioner.id }}</td>
            <td class="font-medium text-white">
              {{ practitioner.name?.[0]?.prefix?.join(' ') }} {{ practitioner.name?.[0]?.given?.join(' ') }} {{ practitioner.name?.[0]?.family }}
            </td>
            <td>
              <span class="badge badge-blue">{{ practitioner.gender || 'unknown' }}</span>
            </td>
            <td class="font-mono text-xs text-slate-300">{{ practitioner.identifier?.[0]?.value || '-' }}</td>
            <td>
              <span class="badge badge-green">{{ practitioner.qualification?.[0]?.code?.text || 'MD' }}</span>
            </td>
            <td class="text-right space-x-2">
              <button @click="viewDetails(practitioner)" class="btn btn-secondary py-1 px-2 text-xs" title="View FHIR JSON">
                <Eye :size="14" />
              </button>
              <button @click="practitioner.id && store.deletePractitioner(practitioner.id)" class="btn btn-danger py-1 px-2 text-xs" title="Delete">
                <Trash2 :size="14" />
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- PractitionerRoles Table -->
    <div v-if="activeTab === 'roles'" class="table-container">
      <table class="table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Specialty</th>
            <th>Status</th>
            <th>Role Identifier</th>
            <th class="text-right">Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="store.practitionerRoles.length === 0">
            <td colspan="5" class="text-center text-slate-500 py-6">No PractitionerRoles assigned. Click Add Practitioner Role above.</td>
          </tr>
          <tr v-for="role in store.practitionerRoles" :key="role.id">
            <td class="font-mono text-xs text-teal-400 font-semibold">{{ role.id }}</td>
            <td class="font-medium text-white">{{ role.specialty?.[0]?.text || 'General Practice' }}</td>
            <td>
              <span class="badge" :class="role.active !== false ? 'badge-green' : 'badge-amber'">
                {{ role.active !== false ? 'Active' : 'Inactive' }}
              </span>
            </td>
            <td class="font-mono text-xs text-slate-300">{{ role.identifier?.[0]?.value || '-' }}</td>
            <td class="text-right space-x-2">
              <button @click="viewDetails(role)" class="btn btn-secondary py-1 px-2 text-xs" title="View FHIR JSON">
                <Eye :size="14" />
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
          <h3 class="text-lg font-bold text-white">Add {{ activeTab === 'practitioners' ? 'Practitioner' : 'Practitioner Role' }}</h3>
          <button @click="showCreateModal = false" class="text-slate-400 hover:text-white">
            <X :size="20" />
          </button>
        </div>

        <div v-if="activeTab === 'practitioners'" class="space-y-4">
          <div class="grid grid-cols-3 gap-3">
            <div class="form-group">
              <label class="form-label">Prefix</label>
              <input v-model="newPractitioner.prefix" type="text" placeholder="Dr." class="form-input" />
            </div>
            <div class="form-group">
              <label class="form-label">Given Name</label>
              <input v-model="newPractitioner.given" type="text" placeholder="John" class="form-input" />
            </div>
            <div class="form-group">
              <label class="form-label">Family Name *</label>
              <input v-model="newPractitioner.family" type="text" placeholder="Watson" class="form-input" required />
            </div>
          </div>

          <div class="grid grid-cols-2 gap-3">
            <div class="form-group">
              <label class="form-label">Gender</label>
              <select v-model="newPractitioner.gender" class="form-select">
                <option value="male">Male</option>
                <option value="female">Female</option>
                <option value="other">Other</option>
              </select>
            </div>
            <div class="form-group">
              <label class="form-label">NPI Identifier</label>
              <input v-model="newPractitioner.npi" type="text" placeholder="NPI-100293" class="form-input" />
            </div>
          </div>

          <div class="form-group">
            <label class="form-label">Medical Qualification</label>
            <input v-model="newPractitioner.qualification" type="text" placeholder="MD - Cardiology" class="form-input" />
          </div>

          <div class="flex justify-end gap-2 pt-4 border-t border-[#27344d]">
            <button @click="showCreateModal = false" class="btn btn-secondary">Cancel</button>
            <button @click="handleCreatePractitioner" class="btn btn-primary">Save to Clustered Grid</button>
          </div>
        </div>

        <div v-if="activeTab === 'roles'" class="space-y-4">
          <div class="form-group">
            <label class="form-label">Medical Specialty</label>
            <input v-model="newRole.specialty" type="text" placeholder="e.g. Cardiovascular Surgery" class="form-input" />
          </div>
          <div class="form-group">
            <label class="form-label">Role Identifier</label>
            <input v-model="newRole.identifier" type="text" placeholder="e.g. ROLE-SURG-01" class="form-input" />
          </div>
          <div class="flex justify-end gap-2 pt-4 border-t border-[#27344d]">
            <button @click="showCreateModal = false" class="btn btn-secondary">Cancel</button>
            <button @click="handleCreateRole" class="btn btn-primary">Save Role</button>
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
