import { createRouter, createWebHistory } from 'vue-router';
import DashboardView from '../views/DashboardView.vue';
import PersonView from '../views/PersonView.vue';
import PractitionerListView from '../views/PractitionerListView.vue';
import OrganizationView from '../views/OrganizationView.vue';
import LocationHierarchyView from '../views/LocationHierarchyView.vue';
import HealthcareServiceView from '../views/HealthcareServiceView.vue';
import GroupManagementView from '../views/GroupManagementView.vue';

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
  ]
});

export default router;
