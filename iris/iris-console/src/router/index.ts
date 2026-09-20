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
import OverviewView from '../views/OverviewView.vue';
import SubsystemsView from '../views/SubsystemsView.vue';
import MessagesView from '../views/MessagesView.vue';
import WorkView from '../views/WorkView.vue';
import EventsView from '../views/EventsView.vue';
import AlertsView from '../views/AlertsView.vue';
import InterfacesView from '../views/InterfacesView.vue';
import HealthView from '../views/HealthView.vue';
import TaskSequenceListView from '../views/TaskSequenceListView.vue';
import TaskSequenceDetailView from '../views/TaskSequenceDetailView.vue';
import OperationsDataView from '../views/OperationsDataView.vue';
import CacheClusterView from '../views/CacheClusterView.vue';

export const routes = [
  // Canonical Operational Perspectives
  { path: '/', redirect: '/subsystems' },
  { path: '/overview', name: 'overview', component: OverviewView },
  { path: '/subsystems', name: 'subsystems', component: SubsystemsView },
  { path: '/subsystems/:id', name: 'subsystem-detail', component: SubsystemsView },
  { path: '/messages', name: 'messages', component: MessagesView },
  { path: '/queues', name: 'queues', component: MessagesView },
  { path: '/work', name: 'work', component: WorkView },
  { path: '/workflows', name: 'workflows', component: WorkView },
  { path: '/interfaces', name: 'interfaces', component: InterfacesView },
  { path: '/events', name: 'events', component: EventsView },
  { path: '/alerts', name: 'alerts', component: AlertsView },
  { path: '/health', name: 'health', component: HealthView },

  // Secondary & Legacy Routes
  { path: '/dashboard', name: 'dashboard', redirect: '/overview' },
  { path: '/sequences', name: 'sequences', component: TaskSequenceListView },
  { path: '/sequences/:id', name: 'sequence-detail', component: TaskSequenceDetailView },
  { path: '/operations-data', name: 'operations-data', component: OperationsDataView },
  { path: '/caches', name: 'caches', component: CacheClusterView }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;
