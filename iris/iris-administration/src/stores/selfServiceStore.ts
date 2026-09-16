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
import type { Practitioner, PractitionerRole, Organization, Location, HealthcareService, Endpoint } from '../models/fhir';
import type { ProviderSummaryView, ProviderRoleView, ChangeRequestSummaryView } from '../models/provider';
import { providerRegistryClient } from '../api/providerRegistryClient';
import { providerChangeRequestClient } from '../api/providerChangeRequestClient';
import { useSecurityStore } from './securityStore';
import { mapPractitionerToSummary, mapPractitionerRoleToView } from '../utils/fhirMapper';
import { safeLogger } from '../utils/safeLogger';

export const useSelfServiceStore = defineStore('selfService', () => {
  const securityStore = useSecurityStore();

  const practitioner = ref<Practitioner | null>(null);
  const practitionerSummary = ref<ProviderSummaryView | null>(null);
  const roles = ref<ProviderRoleView[]>([]);
  const rawRoles = ref<PractitionerRole[]>([]);
  const organizations = ref<Organization[]>([]);
  const locations = ref<Location[]>([]);
  const services = ref<HealthcareService[]>([]);
  const endpoints = ref<Endpoint[]>([]);
  const changeRequests = ref<ChangeRequestSummaryView[]>([]);
  const loading = ref<boolean>(false);
  const error = ref<string | null>(null);

  async function loadProviderProfile() {
    loading.value = true;
    error.value = null;
    try {
      const practitionerId = securityStore.principal?.practitionerId || 'PR-1001';
      const p = await providerRegistryClient.get<Practitioner>('Practitioner', practitionerId);
      practitioner.value = p;

      // Load associated roles
      const userRoles = await providerRegistryClient.search<PractitionerRole>('PractitionerRole', {
        practitioner: practitionerId
      });
      rawRoles.value = userRoles;

      // Load associated organizations
      const orgs: Organization[] = [];
      const orgMap: Record<string, string> = {};
      for (const role of userRoles) {
        const orgId = role.organization?.reference?.replace(/^Organization\//, '');
        if (orgId && !orgMap[orgId]) {
          const org = await providerRegistryClient.get<Organization>('Organization', orgId);
          if (org) {
            orgs.push(org);
            orgMap[orgId] = org.name || orgId;
          }
        }
      }
      organizations.value = orgs;

      practitionerSummary.value = p ? mapPractitionerToSummary(p, userRoles.length) : null;
      roles.value = userRoles.map(r => mapPractitionerRoleToView(r, orgMap));

      // Load locations and services
      const locList = await providerRegistryClient.search<Location>('Location');
      locations.value = locList;

      const svcList = await providerRegistryClient.search<HealthcareService>('HealthcareService');
      services.value = svcList;

      const epList = await providerRegistryClient.search<Endpoint>('Endpoint');
      endpoints.value = epList;

      // Load change requests submitted by this provider
      await loadChangeRequests();
    } catch (err: any) {
      error.value = err.message || 'Failed to load provider profile';
      safeLogger.error('Failed to load provider profile', err);
    } finally {
      loading.value = false;
    }
  }

  async function loadChangeRequests() {
    try {
      const requester = securityStore.principal?.displayName || 'Dr. Sarah Chen, MD';
      const requests = await providerChangeRequestClient.getChangeRequests({
        requester
      });
      changeRequests.value = requests;
    } catch (err: any) {
      safeLogger.error('Failed to load change requests', err);
    }
  }

  async function submitChangeRequest(
    resourceType: string,
    operation: 'CREATE' | 'UPDATE',
    payload: any,
    resourceId?: string
  ): Promise<ChangeRequestSummaryView> {
    loading.value = true;
    error.value = null;
    try {
      const requester = securityStore.principal?.displayName || 'Dr. Sarah Chen, MD';
      const result = await providerChangeRequestClient.submitChangeRequest(
        resourceType,
        operation,
        payload,
        resourceId,
        requester,
        securityStore.correlationId
      );
      await loadChangeRequests();
      return result;
    } catch (err: any) {
      error.value = err.message || 'Change request submission failed';
      throw err;
    } finally {
      loading.value = false;
    }
  }

  return {
    practitioner,
    practitionerSummary,
    roles,
    rawRoles,
    organizations,
    locations,
    services,
    endpoints,
    changeRequests,
    loading,
    error,
    loadProviderProfile,
    loadChangeRequests,
    submitChangeRequest
  };
});
