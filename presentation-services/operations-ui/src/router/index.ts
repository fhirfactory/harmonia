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
import OperationsDashboardView from '../views/OperationsDashboardView.vue';
import TaskSequenceListView from '../views/TaskSequenceListView.vue';
import TaskSequenceDetailView from '../views/TaskSequenceDetailView.vue';
import OperationsDataView from '../views/OperationsDataView.vue';
import MessagingQueuesView from '../views/MessagingQueuesView.vue';
import CacheClusterView from '../views/CacheClusterView.vue';

const routes = [
  { path: '/', name: 'dashboard', component: OperationsDashboardView },
  { path: '/sequences', name: 'sequences', component: TaskSequenceListView },
  { path: '/sequences/:id', name: 'sequence-detail', component: TaskSequenceDetailView },
  { path: '/operations-data', name: 'operations-data', component: OperationsDataView },
  { path: '/queues', name: 'queues', component: MessagingQueuesView },
  { path: '/caches', name: 'caches', component: CacheClusterView }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;
