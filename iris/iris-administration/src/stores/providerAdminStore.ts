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
import type {
  Practitioner,
  PractitionerRole,
  Organization,
  Location,
  HealthcareService,
  Endpoint,
  Group
} from '../models/fhir';
import type {
  ProviderSummaryView,
  ProviderRoleView,
  OrganizationSummaryView,
  LocationSummaryView,
  HealthcareServiceSummaryView,
  EndpointSummaryView,
  GroupSummaryView,
  ChangeRequestSummaryView
} from '../models/provider';
import { providerRegistryClient } from '../api/providerRegistryClient';
import { providerChangeRequestClient } from '../api/providerChangeRequestClient';
import {
  mapPractitionerToSummary,
  mapPractitionerRoleToView,
  mapOrganizationToSummary,
  mapLocationToSummary,
  mapHealthcareServiceToSummary,
  mapEndpointToSummary,
  mapGroupToSummary
} from '../utils/fhirMapper';
import { safeLogger } from '../utils/safeLogger';

export const useProviderAdminStore = defineStore('providerAdmin', () => {
  const practitioners = ref<ProviderSummaryView[]>([]);
  const rawPractitioners = ref<Practitioner[]>([]);
  const roles = ref<ProviderRoleView[]>([]);
  const organizations = ref<OrganizationSummaryView[]>([]);
  const rawOrganizations = ref<Organization[]>([]);
  const locations = ref<LocationSummaryView[]>([]);
  const services = ref<HealthcareServiceSummaryView[]>([]);
  const endpoints = ref<EndpointSummaryView[]>([]);
  const groups = ref<GroupSummaryView[]>([]);
  const workQueue = ref<ChangeRequestSummaryView[]>([]);
  const loading = ref<boolean>(false);
  const error = ref<string | null>(null);

  async function loadAllRegistryData() {
    loading.value = true;
    error.value = null;
    try {
      // 1. Organizations
      const orgList = await providerRegistryClient.search<Organization>('Organization');
      rawOrganizations.value = orgList;
      const orgMap: Record<string, string> = {};
      orgList.forEach(o => { if (o.id) orgMap[o.id] = o.name || o.id; });
      organizations.value = orgList.map(o => mapOrganizationToSummary(o));

      // 2. Practitioners & Roles
      const pList = await providerRegistryClient.search<Practitioner>('Practitioner');
      rawPractitioners.value = pList;
      const rList = await providerRegistryClient.search<PractitionerRole>('PractitionerRole');

      practitioners.value = pList.map(p => {
        const pRoles = rList.filter(r => r.practitioner?.reference?.includes(p.id!));
        return mapPractitionerToSummary(p, pRoles.length);
      });

      roles.value = rList.map(r => mapPractitionerRoleToView(r, orgMap));

      // 3. Locations, Services, Endpoints, Groups
      const locList = await providerRegistryClient.search<Location>('Location');
      locations.value = locList.map(mapLocationToSummary);

      const svcList = await providerRegistryClient.search<HealthcareService>('HealthcareService');
      services.value = svcList.map(mapHealthcareServiceToSummary);

      const epList = await providerRegistryClient.search<Endpoint>('Endpoint');
      endpoints.value = epList.map(mapEndpointToSummary);

      const grpList = await providerRegistryClient.search<Group>('Group');
      groups.value = grpList.map(mapGroupToSummary);

      // 4. Work Queue
      await loadWorkQueue();
    } catch (err: any) {
      error.value = err.message || 'Failed to load registry master data';
      safeLogger.error('Failed to load registry master data', err);
    } finally {
      loading.value = false;
    }
  }

  async function loadWorkQueue(statusFilter?: string) {
    try {
      const queue = await providerChangeRequestClient.getChangeRequests({
        status: statusFilter
      });
      workQueue.value = queue;
    } catch (err: any) {
      safeLogger.error('Failed to load work queue', err);
    }
  }

  async function approveRequest(taskId: string) {
    loading.value = true;
    try {
      const result = await providerChangeRequestClient.approveChangeRequest(taskId);
      await loadAllRegistryData();
      return result;
    } finally {
      loading.value = false;
    }
  }

  async function rejectRequest(taskId: string, reason: string) {
    loading.value = true;
    try {
      const result = await providerChangeRequestClient.rejectChangeRequest(taskId, reason);
      await loadWorkQueue();
      return result;
    } finally {
      loading.value = false;
    }
  }

  async function saveEntity<T extends { resourceType: string; id?: string }>(resource: T) {
    loading.value = true;
    try {
      const saved = await providerRegistryClient.save(resource as any);
      await loadAllRegistryData();
      return saved;
    } finally {
      loading.value = false;
    }
  }

  async function deleteEntity(resourceType: string, id: string) {
    loading.value = true;
    try {
      const ok = await providerRegistryClient.remove(resourceType, id);
      await loadAllRegistryData();
      return ok;
    } finally {
      loading.value = false;
    }
  }

  return {
    practitioners,
    rawPractitioners,
    roles,
    organizations,
    rawOrganizations,
    locations,
    services,
    endpoints,
    groups,
    workQueue,
    loading,
    error,
    loadAllRegistryData,
    loadWorkQueue,
    approveRequest,
    rejectRequest,
    saveEntity,
    deleteEntity
  };
});
