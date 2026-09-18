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
import { onMounted } from 'vue';
import { useSelfServiceStore } from '../../stores/selfServiceStore';
import StatusBadge from '../../components/StatusBadge.vue';
import { MapPin, Activity, Building2, Phone } from 'lucide-vue-next';

const selfServiceStore = useSelfServiceStore();

onMounted(async () => {
  await selfServiceStore.loadProviderProfile();
});
</script>

<template>
  <div class="space-y-6">
    <div>
      <h1 class="text-2xl font-bold text-white">My Locations &amp; Healthcare Services</h1>
      <p class="text-slate-400 text-sm mt-1">
        Physical service delivery sites, hospital wings, and clinical healthcare programs connected to your roles.
      </p>
    </div>

    <!-- Locations Section -->
    <div class="space-y-4">
      <div class="flex items-center gap-2 text-emerald-400">
        <MapPin :size="18" />
        <h2 class="text-lg font-bold text-white">Physical Locations &amp; Facilities</h2>
      </div>

      <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div 
          v-for="loc in selfServiceStore.locations" 
          :key="loc.id"
          class="card space-y-3"
        >
          <div class="flex justify-between items-start">
            <div>
              <h3 class="text-base font-bold text-white">{{ loc.name }}</h3>
              <span class="font-mono text-xs text-sky-400">{{ loc.id }}</span>
            </div>
            <StatusBadge :status="loc.status || 'active'" />
          </div>

          <div class="space-y-1.5 text-xs text-slate-300">
            <div v-if="loc.managingOrganization?.display" class="flex justify-between">
              <span class="text-slate-400">Managing Entity:</span>
              <span class="font-semibold text-slate-200">{{ loc.managingOrganization.display }}</span>
            </div>
            <div v-if="loc.address" class="flex justify-between">
              <span class="text-slate-400">Address:</span>
              <span>{{ loc.address.line?.join(', ') }}, {{ loc.address.city }} {{ loc.address.state }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Healthcare Services Section -->
    <div class="space-y-4 pt-6 border-t border-slate-800">
      <div class="flex items-center gap-2 text-indigo-400">
        <Activity :size="18" />
        <h2 class="text-lg font-bold text-white">Associated Healthcare Services</h2>
      </div>

      <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div 
          v-for="svc in selfServiceStore.services" 
          :key="svc.id"
          class="card space-y-3"
        >
          <div class="flex justify-between items-start">
            <div>
              <h3 class="text-base font-bold text-white">{{ svc.name }}</h3>
              <span class="font-mono text-xs text-indigo-400">{{ svc.id }}</span>
            </div>
            <StatusBadge :status="svc.active !== false" />
          </div>

          <div class="space-y-1.5 text-xs text-slate-300">
            <div v-if="svc.category?.length" class="flex justify-between">
              <span class="text-slate-400">Category:</span>
              <span class="text-slate-200">{{ svc.category[0].text }}</span>
            </div>
            <div v-if="svc.specialty?.length" class="flex justify-between">
              <span class="text-slate-400">Specialty:</span>
              <span class="badge badge-accent text-[11px]">{{ svc.specialty[0].text }}</span>
            </div>
            <div v-if="svc.providedBy?.display" class="flex justify-between">
              <span class="text-slate-400">Provider Entity:</span>
              <span>{{ svc.providedBy.display }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
