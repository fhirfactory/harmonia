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
import type { 
  TaskSequence, SystemStatus, OperationResource, ModuleStatus,
  OperationalSummary, OperationalSubsystem, OperationalInstance,
  OperationalHealth, TimeSeries, QueueSummary, WorkflowSummary,
  PragmaSummary, OperationalEvent, OperationalAlert
} from '../models/operations';

const api = axios.create({
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json',
    'X-Harmonia-Role': 'OPS_VIEWER',
    'Authorization': 'Bearer ops_viewer_token'
  }
});

export const operationsApi = {
  // --------------------------------------------------------------------------
  // Normalized Operations Perspective APIs (BEFE :8090 /api/operations/*)
  // --------------------------------------------------------------------------

  async getSummary(): Promise<OperationalSummary> {
    const res = await api.get<OperationalSummary>('/operations/summary');
    return res.data;
  },

  async getSubsystems(): Promise<OperationalSubsystem[]> {
    const res = await api.get<OperationalSubsystem[]>('/operations/subsystems');
    return res.data;
  },

  async getSubsystem(id: string): Promise<OperationalSubsystem> {
    const res = await api.get<OperationalSubsystem>(`/operations/subsystems/${id}`);
    return res.data;
  },

  async getSubsystemInstances(id: string): Promise<OperationalInstance[]> {
    const res = await api.get<OperationalInstance[]>(`/operations/subsystems/${id}/instances`);
    return res.data;
  },

  async getSubsystemHealth(id: string): Promise<OperationalHealth> {
    const res = await api.get<OperationalHealth>(`/operations/subsystems/${id}/health`);
    return res.data;
  },

  async getSubsystemStatistics(id: string, window: string = '1h'): Promise<Record<string, TimeSeries>> {
    const res = await api.get<Record<string, TimeSeries>>(`/operations/subsystems/${id}/statistics`, {
      params: { window }
    });
    return res.data;
  },

  async getQueues(status?: string, search?: string): Promise<QueueSummary[]> {
    const res = await api.get<QueueSummary[]>('/operations/queues', {
      params: { status, search }
    });
    return res.data;
  },

  async getQueue(id: string): Promise<QueueSummary> {
    const res = await api.get<QueueSummary>(`/operations/queues/${id}`);
    return res.data;
  },

  async getWorkflows(search?: string): Promise<WorkflowSummary[]> {
    const res = await api.get<WorkflowSummary[]>('/operations/workflows', {
      params: { search }
    });
    return res.data;
  },

  async getWorkflow(id: string): Promise<WorkflowSummary> {
    const res = await api.get<WorkflowSummary>(`/operations/workflows/${id}`);
    return res.data;
  },

  async getWorkflowPragmas(workflowId: string): Promise<PragmaSummary[]> {
    const res = await api.get<PragmaSummary[]>(`/operations/workflows/${encodeURIComponent(workflowId)}/pragmas`);
    return res.data;
  },

  async getPragma(id: string): Promise<PragmaSummary> {
    const res = await api.get<PragmaSummary>(`/operations/pragmas/${id}`);
    return res.data;
  },

  async getEvents(params?: {
    correlationId?: string;
    causationId?: string;
    messageId?: string;
    pragmaId?: string;
    subsystem?: string;
    eventType?: string;
    status?: string;
    from?: number;
    to?: number;
    page?: number;
    pageSize?: number;
  }): Promise<OperationalEvent[]> {
    const res = await api.get<OperationalEvent[]>('/operations/events', { params });
    return res.data;
  },

  async getEvent(id: string): Promise<OperationalEvent> {
    const res = await api.get<OperationalEvent>(`/operations/events/${id}`);
    return res.data;
  },

  async getAlerts(severity?: string, status?: string, subsystem?: string): Promise<OperationalAlert[]> {
    const res = await api.get<OperationalAlert[]>('/operations/alerts', {
      params: { severity, status, subsystem }
    });
    return res.data;
  },

  async acknowledgeAlert(id: string): Promise<{ status: string; alertId: string; message: string }> {
    const res = await api.post(`/operations/alerts/${id}/acknowledge`);
    return res.data;
  },

  // --------------------------------------------------------------------------
  // Legacy System Status & Architecture Topology (Backward Compatibility)
  // --------------------------------------------------------------------------
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
