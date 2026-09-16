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

import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router';
import { useSecurityStore } from '../stores/securityStore';
import type { ThemisAuthority } from '../models/themis';

// Home and Security Views
import HomeView from '../views/HomeView.vue';
import AccessDeniedView from '../views/AccessDeniedView.vue';
import ThemisSecurityView from '../views/ThemisSecurityView.vue';
import PreferencesView from '../views/PreferencesView.vue';
import AuditGovernanceView from '../views/AuditGovernanceView.vue';
import ReferenceDataView from '../views/ReferenceDataView.vue';

// Self-Service Views
const MyDetailsView = () => import('../views/self-service/MyDetailsView.vue');
const MyRolesView = () => import('../views/self-service/MyRolesView.vue');
const MyOrganizationsView = () => import('../views/self-service/MyOrganizationsView.vue');
const MyLocationsServicesView = () => import('../views/self-service/MyLocationsServicesView.vue');
const MyEndpointsView = () => import('../views/self-service/MyEndpointsView.vue');
const MyRequestsView = () => import('../views/self-service/MyRequestsView.vue');
const RequestChangeView = () => import('../views/self-service/RequestChangeView.vue');

// Departmental Admin Views
const AdminDashboardView = () => import('../views/admin/AdminDashboardView.vue');
const WorkQueueView = () => import('../views/admin/WorkQueueView.vue');
const ProviderSearchView = () => import('../views/admin/ProviderSearchView.vue');
const PractitionerAdminView = () => import('../views/admin/PractitionerAdminView.vue');
const PractitionerRoleAdminView = () => import('../views/admin/PractitionerRoleAdminView.vue');
const OrganizationAdminView = () => import('../views/admin/OrganizationAdminView.vue');
const LocationAdminView = () => import('../views/admin/LocationAdminView.vue');
const HealthcareServiceAdminView = () => import('../views/admin/HealthcareServiceAdminView.vue');
const EndpointAdminView = () => import('../views/admin/EndpointAdminView.vue');
const GroupAdminView = () => import('../views/admin/GroupAdminView.vue');
const DataQualityView = () => import('../views/admin/DataQualityView.vue');

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'Home',
    component: HomeView
  },
  {
    path: '/access-denied',
    name: 'AccessDenied',
    component: AccessDeniedView
  },

  // Self Service routes (Requires authenticated provider or provider.read)
  {
    path: '/self-service/my-details',
    name: 'MyDetails',
    component: MyDetailsView,
    meta: { requiredAuthority: 'provider.read', requiresSelfService: true }
  },
  {
    path: '/self-service/my-roles',
    name: 'MyRoles',
    component: MyRolesView,
    meta: { requiredAuthority: 'provider.read', requiresSelfService: true }
  },
  {
    path: '/self-service/my-organizations',
    name: 'MyOrganizations',
    component: MyOrganizationsView,
    meta: { requiredAuthority: 'provider.read', requiresSelfService: true }
  },
  {
    path: '/self-service/my-locations-services',
    name: 'MyLocationsServices',
    component: MyLocationsServicesView,
    meta: { requiredAuthority: 'provider.read', requiresSelfService: true }
  },
  {
    path: '/self-service/my-endpoints',
    name: 'MyEndpoints',
    component: MyEndpointsView,
    meta: { requiredAuthority: 'provider.read', requiresSelfService: true }
  },
  {
    path: '/self-service/my-requests',
    name: 'MyRequests',
    component: MyRequestsView,
    meta: { requiredAuthority: 'provider.read', requiresSelfService: true }
  },
  {
    path: '/self-service/request-change',
    name: 'RequestChange',
    component: RequestChangeView,
    meta: { requiredAuthority: 'provider.read', requiresSelfService: true }
  },

  // Departmental Provider Administration routes (Requires provider.search or provider.admin)
  {
    path: '/admin/dashboard',
    name: 'AdminDashboard',
    component: AdminDashboardView,
    meta: { requiredAuthority: 'provider.search', requiresAdmin: true }
  },
  {
    path: '/admin/work-queue',
    name: 'WorkQueue',
    component: WorkQueueView,
    meta: { requiredAuthority: 'provider.change.process', requiresAdmin: true }
  },
  {
    path: '/admin/search',
    name: 'ProviderSearch',
    component: ProviderSearchView,
    meta: { requiredAuthority: 'provider.search' }
  },
  {
    path: '/admin/practitioners',
    name: 'PractitionerAdmin',
    component: PractitionerAdminView,
    meta: { requiredAuthority: 'provider.search', requiresAdmin: true }
  },
  {
    path: '/admin/roles',
    name: 'PractitionerRoleAdmin',
    component: PractitionerRoleAdminView,
    meta: { requiredAuthority: 'provider.search', requiresAdmin: true }
  },
  {
    path: '/admin/organizations',
    name: 'OrganizationAdmin',
    component: OrganizationAdminView,
    meta: { requiredAuthority: 'provider.search', requiresAdmin: true }
  },
  {
    path: '/admin/locations',
    name: 'LocationAdmin',
    component: LocationAdminView,
    meta: { requiredAuthority: 'provider.search', requiresAdmin: true }
  },
  {
    path: '/admin/services',
    name: 'HealthcareServiceAdmin',
    component: HealthcareServiceAdminView,
    meta: { requiredAuthority: 'provider.search', requiresAdmin: true }
  },
  {
    path: '/admin/endpoints',
    name: 'EndpointAdmin',
    component: EndpointAdminView,
    meta: { requiredAuthority: 'provider.search', requiresAdmin: true }
  },
  {
    path: '/admin/groups',
    name: 'GroupAdmin',
    component: GroupAdminView,
    meta: { requiredAuthority: 'provider.search', requiresAdmin: true }
  },
  {
    path: '/admin/data-quality',
    name: 'DataQuality',
    component: DataQualityView,
    meta: { requiredAuthority: 'provider.search', requiresAdmin: true }
  },

  // Administrative Services and Preferences
  {
    path: '/services/audit',
    name: 'AuditGovernance',
    component: AuditGovernanceView
  },
  {
    path: '/services/reference-data',
    name: 'ReferenceData',
    component: ReferenceDataView
  },
  {
    path: '/system/security',
    name: 'ThemisSecurity',
    component: ThemisSecurityView
  },
  {
    path: '/system/preferences',
    name: 'Preferences',
    component: PreferencesView
  },

  // Fallback
  {
    path: '/:pathMatch(.*)*',
    redirect: '/'
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

router.beforeEach((to, from, next) => {
  const securityStore = useSecurityStore();

  if (to.path === '/access-denied' || to.path === '/') {
    return next();
  }

  if (!securityStore.isAuthenticated) {
    return next({ path: '/access-denied' });
  }

  // Check self-service requirements
  if (to.meta.requiresSelfService && !securityStore.canSelfService) {
    return next({ path: '/access-denied' });
  }

  // Check departmental administration requirements
  if (to.meta.requiresAdmin && !securityStore.canAdminister) {
    return next({ path: '/access-denied' });
  }

  // Check explicit authority requirement
  if (to.meta.requiredAuthority) {
    const auth = to.meta.requiredAuthority as ThemisAuthority;
    if (!securityStore.hasAuthority(auth)) {
      return next({ path: '/access-denied' });
    }
  }

  next();
});

export default router;
