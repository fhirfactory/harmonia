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

import { createRouter, createWebHistory } from 'vue-router';
import DashboardView from '../views/DashboardView.vue';
import PersonView from '../views/PersonView.vue';
import PractitionerListView from '../views/PractitionerListView.vue';
import OrganizationView from '../views/OrganizationView.vue';
import LocationHierarchyView from '../views/LocationHierarchyView.vue';
import HealthcareServiceView from '../views/HealthcareServiceView.vue';
import GroupManagementView from '../views/GroupManagementView.vue';
import ProvenanceView from '../views/ProvenanceView.vue';
import AuditEventView from '../views/AuditEventView.vue';
import ConsentView from '../views/ConsentView.vue';
import TaskView from '../views/TaskView.vue';
import CommunicationView from '../views/CommunicationView.vue';
import DocumentReferenceView from '../views/DocumentReferenceView.vue';

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', name: 'dashboard', component: DashboardView },
    { path: '/persons', name: 'persons', component: PersonView },
    { path: '/practitioners', name: 'practitioners', component: PractitionerListView },
    { path: '/organizations', name: 'organizations', component: OrganizationView },
    { path: '/locations', name: 'locations', component: LocationHierarchyView },
    { path: '/services', name: 'services', component: HealthcareServiceView },
    { path: '/groups', name: 'groups', component: GroupManagementView },
    { path: '/provenance', name: 'provenance', component: ProvenanceView },
    { path: '/audit', name: 'audit', component: AuditEventView },
    { path: '/consent', name: 'consent', component: ConsentView },
    { path: '/tasks', name: 'tasks', component: TaskView },
    { path: '/communication', name: 'communication', component: CommunicationView },
    { path: '/documents', name: 'documents', component: DocumentReferenceView },
  ]
});

export default router;
