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

import axios from 'axios';
import type { TaskSequence, SystemStatus, OperationResource, ModuleStatus } from '../models/operations';

const api = axios.create({
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  }
});

export const operationsApi = {
  // System Status & Architecture Topology
  async getSystemStatus(): Promise<SystemStatus> {
    const res = await api.get<SystemStatus>('/operations/status');
    return res.data;
  },

  // Cluster Modules
  async getClusterModules(): Promise<ModuleStatus[]> {
    try {
      const res = await api.get<ModuleStatus[]>('/operations/modules');
      return res.data;
    } catch {
      return [];
    }
  },

  // Task Sequences
  async getSequences(): Promise<TaskSequence[]> {
    const res = await api.get<TaskSequence[]>('/operations/sequences');
    return res.data;
  },

  async getSequence(sequenceId: string): Promise<TaskSequence> {
    const res = await api.get<TaskSequence>(`/operations/sequences/${sequenceId}`);
    return res.data;
  },

  async createSequence(sequence: Partial<TaskSequence>): Promise<TaskSequence> {
    const res = await api.post<TaskSequence>('/operations/sequences', sequence);
    return res.data;
  },

  async updateSequence(sequenceId: string, sequence: Partial<TaskSequence>): Promise<TaskSequence> {
    const res = await api.put<TaskSequence>(`/operations/sequences/${sequenceId}`, sequence);
    return res.data;
  },

  async deleteSequence(sequenceId: string): Promise<void> {
    await api.delete(`/operations/sequences/${sequenceId}`);
  },

  // Synchronize queues and task-sequences with Task Sequence Processor
  async sync(): Promise<{ status: string; message: string; timestamp: string }> {
    const res = await api.post('/operations/sync');
    return res.data;
  },

  // Operational Resources & TaskSequence Cache
  async getOperationalResources(objectType: string = 'tasksequence'): Promise<OperationResource[]> {
    try {
      const res = await api.get<OperationResource[]>(`/operations/resources/${objectType}`);
      return res.data;
    } catch {
      // Fallback: If querying directly via BEFE sequence cache
      const seqs = await this.getSequences();
      return seqs.map(s => ({
        objectType: 'tasksequence',
        objectId: s.sequenceId,
        versionId: 1,
        dataJson: JSON.stringify(s, null, 2),
        createdDate: new Date().toISOString(),
        lastUpdated: new Date().toISOString()
      }));
    }
  },

  // Test Event Ingestion
  async sendTestEvent(payload: any): Promise<any> {
    const res = await api.post('/queue/event', payload);
    return res.data;
  }
};
