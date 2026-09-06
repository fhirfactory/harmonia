<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useFacilityStore } from '../stores/facilityStore';
import type { HealthcareService } from '../models/fhir';
import { Plus, Search, Trash2, Eye, Stethoscope, X } from 'lucide-vue-next';

const store = useFacilityStore();
const searchName = ref('');
const showCreateModal = ref(false);
const showDetailModal = ref(false);
const selectedResource = ref<any>(null);

const newService = ref({
  name: '',
  specialty: 'Cardiology',
  comment: '',
  identifier: ''
});

onMounted(() => {
  store.fetchHealthcareServices();
});

const handleSearch = () => {
  store.fetchHealthcareServices(searchName.value);
};

const handleCreateService = async () => {
  if (!newService.value.name) return;
  const hs: Partial<HealthcareService> = {
    resourceType: 'HealthcareService',
    active: true,
    name: newService.value.name,
    comment: newService.value.comment || undefined,
    specialty: [{ text: newService.value.specialty }],
    identifier: newService.value.identifier ? [{ system: 'urn:service', value: newService.value.identifier }] : []
  };

  await store.createHealthcareService(hs);
  showCreateModal.value = false;
  newService.value = { name: '', specialty: 'Cardiology', comment: '', identifier: '' };
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
          <Stethoscope class="text-purple-400" :size="24" />
          Healthcare Services Directory
        </h1>
        <p class="subtitle mt-1">Catalog clinical departments, outpatient programs, and diagnostic services in FHIR R5.</p>
      </div>

      <button @click="showCreateModal = true" class="btn btn-primary">
        <Plus :size="16" />
        <span>List Service</span>
      </button>
    </div>

    <!-- Search Bar -->
    <div class="card p-4 flex flex-col md:flex-row items-center justify-between gap-4">
      <span class="text-xs text-slate-400 font-semibold uppercase">Total Services: {{ store.healthcareServices.length }}</span>
      <div class="flex items-center gap-2 w-full md:w-80">
        <input 
          v-model="searchName" 
          @keyup.enter="handleSearch"
          type="text" 
          placeholder="Filter service name..." 
          class="form-input flex-1 text-xs" 
        />
        <button @click="handleSearch" class="btn btn-secondary text-xs">
          <Search :size="14" />
        </button>
      </div>
    </div>

    <!-- HealthcareServices Table -->
    <div class="table-container">
      <table class="table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Service Name</th>
            <th>Specialty</th>
            <th>Notes / Hours</th>
            <th>Identifier</th>
            <th class="text-right">Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="store.healthcareServices.length === 0">
            <td colspan="6" class="text-center text-slate-500 py-6">No Healthcare Services found. Click List Service above.</td>
          </tr>
          <tr v-for="hs in store.healthcareServices" :key="hs.id">
            <td class="font-mono text-xs text-purple-400 font-semibold">{{ hs.id }}</td>
            <td class="font-medium text-white">{{ hs.name }}</td>
            <td>
              <span class="badge badge-purple">{{ hs.specialty?.[0]?.text || 'General' }}</span>
            </td>
            <td class="text-slate-300 text-xs">{{ hs.comment || '-' }}</td>
            <td class="font-mono text-xs text-slate-300">{{ hs.identifier?.[0]?.value || '-' }}</td>
            <td class="text-right space-x-2">
              <button @click="viewDetails(hs)" class="btn btn-secondary py-1 px-2 text-xs" title="View FHIR JSON">
                <Eye :size="14" />
              </button>
              <button @click="hs.id && store.deleteHealthcareService(hs.id)" class="btn btn-danger py-1 px-2 text-xs" title="Delete">
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
          <h3 class="text-lg font-bold text-white">List Healthcare Service</h3>
          <button @click="showCreateModal = false" class="text-slate-400 hover:text-white">
            <X :size="20" />
          </button>
        </div>

        <div class="space-y-4">
          <div class="form-group">
            <label class="form-label">Service Name *</label>
            <input v-model="newService.name" type="text" placeholder="e.g. Cardiovascular Diagnostic Imaging" class="form-input" required />
          </div>

          <div class="grid grid-cols-2 gap-3">
            <div class="form-group">
              <label class="form-label">Specialty Field</label>
              <select v-model="newService.specialty" class="form-select">
                <option value="Cardiology">Cardiology</option>
                <option value="Neurology">Neurology</option>
                <option value="Pediatrics">Pediatrics</option>
                <option value="Emergency Care">Emergency Care</option>
                <option value="Oncology">Oncology</option>
                <option value="Radiology">Radiology</option>
              </select>
            </div>
            <div class="form-group">
              <label class="form-label">Identifier</label>
              <input v-model="newService.identifier" type="text" placeholder="e.g. SRV-CARD-09" class="form-input" />
            </div>
          </div>

          <div class="form-group">
            <label class="form-label">Operational Notes / On-call</label>
            <input v-model="newService.comment" type="text" placeholder="e.g. 24/7 on-call trauma response team" class="form-input" />
          </div>

          <div class="flex justify-end gap-2 pt-4 border-t border-[#27344d]">
            <button @click="showCreateModal = false" class="btn btn-secondary">Cancel</button>
            <button @click="handleCreateService" class="btn btn-primary">Save Service</button>
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
