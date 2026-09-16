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
import { useProviderAdminStore } from '../../stores/providerAdminStore';
import StatusBadge from '../../components/StatusBadge.vue';
import { 
  Search, User, Briefcase, Building2, 
  MapPin, RefreshCw, X, Filter
} from 'lucide-vue-next';

const adminStore = useProviderAdminStore();

const searchQuery = ref<string>('');
const selectedOrgFilter = ref<string>('all');
const selectedSpecialtyFilter = ref<string>('all');
const activeOnly = ref<boolean>(false);

onMounted(async () => {
  await adminStore.loadAllRegistryData();
});

const specialtiesList = computed(() => {
  const set = new Set<string>();
  adminStore.roles.forEach(r => r.specialty.forEach(s => set.add(s)));
  return Array.from(set);
});

const searchResults = computed(() => {
  let list = adminStore.practitioners;

  if (searchQuery.value.trim()) {
    const q = searchQuery.value.toLowerCase().trim();
    list = list.filter(p => 
      p.name.toLowerCase().includes(q) ||
      p.identifier.toLowerCase().includes(q) ||
      p.id.toLowerCase().includes(q)
    );
  }

  if (activeOnly.value) {
    list = list.filter(p => p.active);
  }

  if (selectedOrgFilter.value !== 'all') {
    const orgId = selectedOrgFilter.value;
    const matchingPractitionerIds = new Set(
      adminStore.roles.filter(r => r.organizationId === orgId).map(r => r.practitionerId)
    );
    list = list.filter(p => matchingPractitionerIds.has(p.id));
  }

  if (selectedSpecialtyFilter.value !== 'all') {
    const spec = selectedSpecialtyFilter.value;
    const matchingPractitionerIds = new Set(
      adminStore.roles.filter(r => r.specialty.includes(spec)).map(r => r.practitionerId)
    );
    list = list.filter(p => matchingPractitionerIds.has(p.id));
  }

  return list;
});
</script>

<template>
  <div class="space-y-6">
    <div>
      <h1 class="text-2xl font-bold text-white">Provider Registry Search</h1>
      <p class="text-slate-400 text-sm mt-1">
        Multi-criteria directory search across practitioners, professional roles, qualifications, and affiliated health organizations.
      </p>
    </div>

    <!-- Search Controls -->
    <div class="card p-5 space-y-4">
      <div class="flex flex-col md:flex-row gap-3">
        <div class="relative flex-1">
          <input 
            v-model="searchQuery" 
            class="form-input pl-9" 
            placeholder="Search by provider name, HPI-I, Medicare number, or practitioner ID..."
          />
          <Search :size="16" class="absolute left-3 top-3 text-slate-500" />
        </div>

        <div class="flex gap-2">
          <select v-model="selectedOrgFilter" class="form-select text-xs">
            <option value="all">All Organisations</option>
            <option v-for="org in adminStore.organizations" :key="org.id" :value="org.id">
              {{ org.name }}
            </option>
          </select>

          <select v-model="selectedSpecialtyFilter" class="form-select text-xs">
            <option value="all">All Specialties</option>
            <option v-for="spec in specialtiesList" :key="spec" :value="spec">
              {{ spec }}
            </option>
          </select>
        </div>
      </div>

      <div class="flex items-center justify-between text-xs text-slate-400 pt-2 border-t border-slate-800">
        <label class="flex items-center gap-2 cursor-pointer text-slate-300">
          <input type="checkbox" v-model="activeOnly" class="rounded bg-slate-900 border-slate-700" />
          <span>Active Providers Only</span>
        </label>

        <span>Found <strong>{{ searchResults.length }}</strong> matching practitioners</span>
      </div>
    </div>

    <!-- Results Table -->
    <div v-if="searchResults.length === 0" class="card text-center py-16 text-slate-500 text-sm">
      <Search :size="32" class="mx-auto text-slate-600 mb-2" />
      <p>No providers match the search criteria.</p>
    </div>

    <div v-else class="table-container">
      <table class="table">
        <thead>
          <tr>
            <th>Practitioner</th>
            <th>Identifier</th>
            <th>Qualifications</th>
            <th>Roles &amp; Affiliations</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="p in searchResults" :key="p.id">
            <td>
              <div class="font-semibold text-white">{{ p.name }}</div>
              <div class="font-mono text-[11px] text-sky-400">{{ p.id }}</div>
            </td>
            <td class="font-mono text-xs text-slate-300">
              {{ p.identifier }}
            </td>
            <td class="text-xs text-slate-300">
              <span v-for="(q, qidx) in p.qualifications" :key="qidx">
                {{ q }}
              </span>
              <span v-if="!p.qualifications.length" class="text-slate-500">-</span>
            </td>
            <td class="text-xs text-slate-300">
              <div v-for="role in adminStore.roles.filter(r => r.practitionerId === p.id)" :key="role.id" class="space-y-0.5">
                <span class="font-semibold text-slate-200">{{ role.code }}</span> &bull; 
                <span class="text-slate-400">{{ role.organizationName }}</span>
              </div>
            </td>
            <td>
              <StatusBadge :status="p.active" />
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>
