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
import { usePersonStore } from '../stores/personStore';
import type { Person, RelatedPerson } from '../models/fhir';
import SecurityBadge from '../components/SecurityBadge.vue';
import { Plus, Search, Trash2, Eye, User, UsersRound, X } from 'lucide-vue-next';

const store = usePersonStore();
const activeTab = ref<'persons' | 'related'>('persons');

const searchName = ref('');
const showCreateModal = ref(false);
const showDetailModal = ref(false);
const selectedResource = ref<any>(null);

// Form state
const newPerson = ref({
  given: '',
  family: '',
  gender: 'female' as 'male' | 'female' | 'other' | 'unknown',
  identifier: '',
  birthDate: ''
});

const newRelatedPerson = ref({
  given: '',
  family: '',
  relationship: 'Mother',
  identifier: '',
  gender: 'female' as 'male' | 'female' | 'other' | 'unknown'
});

onMounted(() => {
  store.fetchPersons();
  store.fetchRelatedPersons();
});

const handleSearch = () => {
  if (activeTab.value === 'persons') {
    store.fetchPersons(searchName.value);
  } else {
    store.fetchRelatedPersons(searchName.value);
  }
};

const handleCreatePerson = async () => {
  if (!newPerson.value.family) return;
  const personPayload: Partial<Person> = {
    resourceType: 'Person',
    active: true,
    name: [{
      family: newPerson.value.family,
      given: newPerson.value.given ? [newPerson.value.given] : []
    }],
    gender: newPerson.value.gender,
    birthDate: newPerson.value.birthDate || undefined,
    identifier: newPerson.value.identifier ? [{ system: 'urn:mrn', value: newPerson.value.identifier }] : []
  };

  await store.createPerson(personPayload);
  showCreateModal.value = false;
  newPerson.value = { given: '', family: '', gender: 'female', identifier: '', birthDate: '' };
};

const handleCreateRelatedPerson = async () => {
  if (!newRelatedPerson.value.family) return;
  const rpPayload: Partial<RelatedPerson> = {
    resourceType: 'RelatedPerson',
    active: true,
    name: [{
      family: newRelatedPerson.value.family,
      given: newRelatedPerson.value.given ? [newRelatedPerson.value.given] : []
    }],
    relationship: [{ text: newRelatedPerson.value.relationship }],
    gender: newRelatedPerson.value.gender,
    identifier: newRelatedPerson.value.identifier ? [{ system: 'urn:id', value: newRelatedPerson.value.identifier }] : []
  };

  await store.createRelatedPerson(rpPayload);
  showCreateModal.value = false;
  newRelatedPerson.value = { given: '', family: '', relationship: 'Mother', identifier: '', gender: 'female' };
};

const viewDetails = (item: any) => {
  selectedResource.value = item;
  showDetailModal.value = true;
};
</script>

<template>
  <div class="space-y-6">
    <!-- Header -->
    <div class="flex flex-col md:flex-row md:items-center justify-between gap-4">
      <div>
        <h1 class="text-2xl font-bold text-white flex items-center gap-2">
          <User class="text-sky-400" :size="24" />
          Person & Kin Relationships
        </h1>
        <p class="subtitle mt-1">Manage demographic master index and family/guardian associations in FHIR R5.</p>
      </div>

      <button @click="showCreateModal = true" class="btn btn-primary">
        <Plus :size="16" />
        <span>Register {{ activeTab === 'persons' ? 'Person' : 'RelatedPerson' }}</span>
      </button>
    </div>

    <!-- Tabs & Search -->
    <div class="card p-4 flex flex-col md:flex-row items-center justify-between gap-4">
      <div class="flex items-center gap-2 w-full md:w-auto">
        <button 
          @click="activeTab = 'persons'" 
          class="btn text-xs" 
          :class="activeTab === 'persons' ? 'btn-primary' : 'btn-secondary'"
        >
          <User :size="14" />
          <span>Persons ({{ store.persons.length }})</span>
        </button>
        <button 
          @click="activeTab = 'related'" 
          class="btn text-xs" 
          :class="activeTab === 'related' ? 'btn-primary' : 'btn-secondary'"
        >
          <UsersRound :size="14" />
          <span>RelatedPersons ({{ store.relatedPersons.length }})</span>
        </button>
      </div>

      <div class="flex items-center gap-2 w-full md:w-80">
        <input 
          v-model="searchName" 
          @keyup.enter="handleSearch"
          type="text" 
          placeholder="Filter by name..." 
          class="form-input flex-1 text-xs" 
        />
        <button @click="handleSearch" class="btn btn-secondary text-xs">
          <Search :size="14" />
        </button>
      </div>
    </div>

    <!-- Persons Table -->
    <div v-if="activeTab === 'persons'" class="table-container">
      <table class="table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Full Name</th>
            <th>Gender</th>
            <th>Identifier / MRN</th>
            <th>Birth Date</th>
            <th>Confidentiality</th>
            <th class="text-right">Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="store.persons.length === 0">
            <td colspan="7" class="text-center text-slate-500 py-6">No Persons found. Click Register Person above to add.</td>
          </tr>
          <tr v-for="person in store.persons" :key="person.id">
            <td class="font-mono text-xs text-sky-400 font-semibold">{{ person.id }}</td>
            <td class="font-medium text-white">
              {{ person.name?.[0]?.given?.join(' ') }} {{ person.name?.[0]?.family }}
            </td>
            <td>
              <span class="badge" :class="person.gender === 'female' ? 'badge-purple' : 'badge-blue'">
                {{ person.gender || 'unknown' }}
              </span>
            </td>
            <td class="font-mono text-xs text-slate-300">
              {{ person.identifier?.[0]?.value || '-' }}
            </td>
            <td>{{ person.birthDate || '-' }}</td>
            <td>
              <SecurityBadge :meta="person.meta" />
            </td>
            <td class="text-right space-x-2">
              <button @click="viewDetails(person)" class="btn btn-secondary py-1 px-2 text-xs" title="View FHIR JSON">
                <Eye :size="14" />
              </button>
              <button @click="person.id && store.deletePerson(person.id)" class="btn btn-danger py-1 px-2 text-xs" title="Delete">
                <Trash2 :size="14" />
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- RelatedPersons Table -->
    <div v-if="activeTab === 'related'" class="table-container">
      <table class="table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Full Name</th>
            <th>Relationship</th>
            <th>Identifier</th>
            <th>Confidentiality</th>
            <th class="text-right">Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="store.relatedPersons.length === 0">
            <td colspan="6" class="text-center text-slate-500 py-6">No RelatedPersons found. Click Register RelatedPerson above to add.</td>
          </tr>
          <tr v-for="rp in store.relatedPersons" :key="rp.id">
            <td class="font-mono text-xs text-sky-400 font-semibold">{{ rp.id }}</td>
            <td class="font-medium text-white">
              {{ rp.name?.[0]?.given?.join(' ') }} {{ rp.name?.[0]?.family }}
            </td>
            <td>
              <span class="badge badge-amber">
                {{ rp.relationship?.[0]?.text || 'Associate' }}
              </span>
            </td>
            <td class="font-mono text-xs text-slate-300">
              {{ rp.identifier?.[0]?.value || '-' }}
            </td>
            <td>
              <SecurityBadge :meta="rp.meta" />
            </td>
            <td class="text-right space-x-2">
              <button @click="viewDetails(rp)" class="btn btn-secondary py-1 px-2 text-xs" title="View FHIR JSON">
                <Eye :size="14" />
              </button>
              <button @click="rp.id && store.deleteRelatedPerson(rp.id)" class="btn btn-danger py-1 px-2 text-xs" title="Delete">
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
          <h3 class="text-lg font-bold text-white">Register {{ activeTab === 'persons' ? 'New Person' : 'Related Person' }}</h3>
          <button @click="showCreateModal = false" class="text-slate-400 hover:text-white">
            <X :size="20" />
          </button>
        </div>

        <div v-if="activeTab === 'persons'" class="space-y-4">
          <div class="grid grid-cols-2 gap-3">
            <div class="form-group">
              <label class="form-label">Given Name</label>
              <input v-model="newPerson.given" type="text" placeholder="e.g. Alice" class="form-input" />
            </div>
            <div class="form-group">
              <label class="form-label">Family Name *</label>
              <input v-model="newPerson.family" type="text" placeholder="e.g. Smith" class="form-input" required />
            </div>
          </div>

          <div class="grid grid-cols-2 gap-3">
            <div class="form-group">
              <label class="form-label">Gender</label>
              <select v-model="newPerson.gender" class="form-select">
                <option value="female">Female</option>
                <option value="male">Male</option>
                <option value="other">Other</option>
                <option value="unknown">Unknown</option>
              </select>
            </div>
            <div class="form-group">
              <label class="form-label">Birth Date</label>
              <input v-model="newPerson.birthDate" type="date" class="form-input" />
            </div>
          </div>

          <div class="form-group">
            <label class="form-label">Identifier (MRN / National ID)</label>
            <input v-model="newPerson.identifier" type="text" placeholder="e.g. MRN-8849" class="form-input" />
          </div>

          <div class="flex justify-end gap-2 pt-4 border-t border-[#27344d]">
            <button @click="showCreateModal = false" class="btn btn-secondary">Cancel</button>
            <button @click="handleCreatePerson" class="btn btn-primary">Save to Clustered Grid</button>
          </div>
        </div>

        <div v-if="activeTab === 'related'" class="space-y-4">
          <div class="grid grid-cols-2 gap-3">
            <div class="form-group">
              <label class="form-label">Given Name</label>
              <input v-model="newRelatedPerson.given" type="text" placeholder="e.g. Charlie" class="form-input" />
            </div>
            <div class="form-group">
              <label class="form-label">Family Name *</label>
              <input v-model="newRelatedPerson.family" type="text" placeholder="e.g. Brown" class="form-input" required />
            </div>
          </div>

          <div class="grid grid-cols-2 gap-3">
            <div class="form-group">
              <label class="form-label">Relationship</label>
              <select v-model="newRelatedPerson.relationship" class="form-select">
                <option value="Mother">Mother</option>
                <option value="Father">Father</option>
                <option value="Spouse">Spouse</option>
                <option value="Sibling">Sibling</option>
                <option value="Guardian">Guardian</option>
                <option value="Emergency Contact">Emergency Contact</option>
              </select>
            </div>
            <div class="form-group">
              <label class="form-label">Identifier</label>
              <input v-model="newRelatedPerson.identifier" type="text" placeholder="e.g. RP-102" class="form-input" />
            </div>
          </div>

          <div class="flex justify-end gap-2 pt-4 border-t border-[#27344d]">
            <button @click="showCreateModal = false" class="btn btn-secondary">Cancel</button>
            <button @click="handleCreateRelatedPerson" class="btn btn-primary">Save to Clustered Grid</button>
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
