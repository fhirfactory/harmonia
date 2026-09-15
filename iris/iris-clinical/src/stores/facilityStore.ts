/*
 * Copyright (c) 2026 Mark Hunter
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import { defineStore } from 'pinia';
import { ref } from 'vue';
import type { Organization, Location, HealthcareService } from '../models/fhir';
import { fhirApi } from '../api/fhirClient';

export const useFacilityStore = defineStore('facility', () => {
  const organizations = ref<Organization[]>([]);
  const locations = ref<Location[]>([]);
  const healthcareServices = ref<HealthcareService[]>([]);
  const loading = ref(false);
  const error = ref<string | null>(null);

  async function fetchOrganizations(name?: string) {
    loading.value = true;
    try {
      const params: Record<string, string> = {};
      if (name) params.name = name;
      organizations.value = await fhirApi.search<Organization>('Organization', params);
    } catch (err: any) {
      error.value = err.message || 'Failed to fetch organizations';
    } finally {
      loading.value = false;
    }
  }

  async function fetchLocations(name?: string) {
    loading.value = true;
    try {
      const params: Record<string, string> = {};
      if (name) params.name = name;
      locations.value = await fhirApi.search<Location>('Location', params);
    } catch (err: any) {
      error.value = err.message || 'Failed to fetch locations';
    } finally {
      loading.value = false;
    }
  }

  async function fetchHealthcareServices(name?: string) {
    loading.value = true;
    try {
      const params: Record<string, string> = {};
      if (name) params.name = name;
      healthcareServices.value = await fhirApi.search<HealthcareService>('HealthcareService', params);
    } catch (err: any) {
      error.value = err.message || 'Failed to fetch healthcare services';
    } finally {
      loading.value = false;
    }
  }

  async function createOrganization(org: Partial<Organization>) {
    loading.value = true;
    try {
      const created = await fhirApi.create<Organization>('Organization', org);
      organizations.value.unshift(created);
      return created;
    } finally {
      loading.value = false;
    }
  }

  async function createLocation(loc: Partial<Location>) {
    loading.value = true;
    try {
      const created = await fhirApi.create<Location>('Location', loc);
      locations.value.unshift(created);
      return created;
    } finally {
      loading.value = false;
    }
  }

  async function createHealthcareService(hs: Partial<HealthcareService>) {
    loading.value = true;
    try {
      const created = await fhirApi.create<HealthcareService>('HealthcareService', hs);
      healthcareServices.value.unshift(created);
      return created;
    } finally {
      loading.value = false;
    }
  }

  async function deleteOrganization(id: string) {
    loading.value = true;
    try {
      await fhirApi.delete('Organization', id);
      organizations.value = organizations.value.filter(o => o.id !== id);
    } finally {
      loading.value = false;
    }
  }

  async function deleteLocation(id: string) {
    loading.value = true;
    try {
      await fhirApi.delete('Location', id);
      locations.value = locations.value.filter(l => l.id !== id);
    } finally {
      loading.value = false;
    }
  }

  async function deleteHealthcareService(id: string) {
    loading.value = true;
    try {
      await fhirApi.delete('HealthcareService', id);
      healthcareServices.value = healthcareServices.value.filter(hs => hs.id !== id);
    } finally {
      loading.value = false;
    }
  }

  return {
    organizations,
    locations,
    healthcareServices,
    loading,
    error,
    fetchOrganizations,
    fetchLocations,
    fetchHealthcareServices,
    createOrganization,
    createLocation,
    createHealthcareService,
    deleteOrganization,
    deleteLocation,
    deleteHealthcareService
  };
});
