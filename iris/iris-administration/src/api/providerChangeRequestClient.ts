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

import { apiClient } from './client';
import { providerRegistryClient } from './providerRegistryClient';
import type { Task, OperationOutcome, BaseResource } from '../models/fhir';
import type { ChangeRequestSummaryView } from '../models/provider';
import { mapTaskToChangeRequest } from '../utils/fhirMapper';
import { safeLogger } from '../utils/safeLogger';

// Initial Change Requests / Tasks Seed
export const INITIAL_TASKS: Task[] = [
  {
    resourceType: 'Task',
    id: 'TSK-9001',
    status: 'in-progress',
    intent: 'order',
    authoredOn: '2026-09-15T09:15:00Z',
    requester: { reference: 'Practitioner/PR-1001', display: 'Dr. Sarah Ying Chen, MD' },
    focus: { reference: 'Practitioner/PR-1001', type: 'Practitioner' },
    identifier: [{ system: 'http://harmonia.net/correlation-id', value: 'CORR-9001-init' }],
    input: [
      { type: { text: 'operation' }, valueCode: 'UPDATE' },
      { type: { text: 'resourceType' }, valueCode: 'Practitioner' },
      { type: { text: 'resourceId' }, valueString: 'PR-1001' },
      {
        type: { text: 'payload' },
        valueResource: {
          resourceType: 'Practitioner',
          id: 'PR-1001',
          active: true,
          name: [{ family: 'Chen', given: ['Sarah', 'Ying'], prefix: ['Dr'], text: 'Dr. Sarah Ying Chen, MD, PhD' }],
          telecom: [{ system: 'email', value: 's.chen.phd@metropolitan-health.org', use: 'work' }]
        }
      }
    ]
  },
  {
    resourceType: 'Task',
    id: 'TSK-9002',
    status: 'completed',
    intent: 'order',
    authoredOn: '2026-09-10T14:30:00Z',
    requester: { reference: 'Practitioner/PR-1002', display: 'Dr. Marcus Vance, FRACP' },
    focus: { reference: 'PractitionerRole/PRR-5002', type: 'PractitionerRole' },
    identifier: [{ system: 'http://harmonia.net/correlation-id', value: 'CORR-9002-init' }],
    input: [
      { type: { text: 'operation' }, valueCode: 'UPDATE' },
      { type: { text: 'resourceType' }, valueCode: 'PractitionerRole' },
      { type: { text: 'resourceId' }, valueString: 'PRR-5002' }
    ]
  },
  {
    resourceType: 'Task',
    id: 'TSK-9003',
    status: 'rejected',
    intent: 'order',
    authoredOn: '2026-09-12T11:00:00Z',
    requester: { reference: 'Practitioner/PR-1001', display: 'Dr. Sarah Ying Chen, MD' },
    statusReason: { text: 'Duplicate HPI-I Identifier detected in registry (PR-VAL-003).' },
    focus: { reference: 'Practitioner/PR-1001', type: 'Practitioner' },
    identifier: [{ system: 'http://harmonia.net/correlation-id', value: 'CORR-9003-init' }],
    input: [
      { type: { text: 'operation' }, valueCode: 'UPDATE' },
      { type: { text: 'resourceType' }, valueCode: 'Practitioner' },
      { type: { text: 'resourceId' }, valueString: 'PR-1001' }
    ],
    output: [
      {
        type: { coding: [{ system: 'http://harmonia.net/metadata', code: 'validationOutcome' }] },
        valueResource: {
          resourceType: 'OperationOutcome',
          issue: [
            {
              severity: 'error',
              code: 'PR-VAL-003',
              details: {
                text: 'The supplied HPI-I identifier is already assigned to another active practitioner in the registry.',
                coding: [{ system: 'http://harmonia.net/codes/validation', code: 'PR-VAL-003' }]
              },
              expression: ['Practitioner.identifier[0].value']
            }
          ]
        }
      }
    ]
  }
];

class ProviderChangeRequestClient {
  private tasks: Map<string, Task> = new Map();

  constructor() {
    this.reset();
  }

  public reset() {
    this.tasks.clear();
    INITIAL_TASKS.forEach(t => this.tasks.set(t.id!, { ...t }));
  }

  /**
   * Submits a governed change request returning a tracking Task.
   */
  public async submitChangeRequest(
    resourceType: string,
    operation: 'CREATE' | 'UPDATE',
    payload: any,
    resourceId?: string,
    requesterName: string = 'Dr. Sarah Chen, MD',
    correlationId?: string
  ): Promise<ChangeRequestSummaryView> {
    const taskId = `TSK-${Date.now().toString().substring(7)}`;
    const corrId = correlationId || `CORR-${Date.now()}-${Math.random().toString(36).substring(2, 6)}`;

    // Client-side simulation of authoritative Pragma change pipeline evaluation
    let status: Task['status'] = 'accepted';
    let output: Task['output'] = undefined;
    let statusReason: Task['statusReason'] = undefined;

    // Check for simulated validation error triggers (e.g. invalid duplicates or missing fields)
    if (payload.identifier && Array.isArray(payload.identifier)) {
      const duplicate = payload.identifier.find((i: any) => i.value === 'DUPLICATE' || i.value === '8003610911223344');
      if (duplicate && resourceId !== 'PR-1002') {
        status = 'rejected';
        statusReason = { text: 'Duplicate identifier detected in registry (PR-VAL-003).' };
        output = [
          {
            type: { coding: [{ system: 'http://harmonia.net/metadata', code: 'validationOutcome' }] },
            valueResource: {
              resourceType: 'OperationOutcome',
              issue: [
                {
                  severity: 'error',
                  code: 'PR-VAL-003',
                  details: {
                    text: 'The identifier is already registered to Dr. Marcus Vance (PR-1002).',
                    coding: [{ code: 'PR-VAL-003' }]
                  },
                  expression: ['identifier[0].value']
                }
              ]
            }
          }
        ];
      }
    }

    const newTask: Task = {
      resourceType: 'Task',
      id: taskId,
      status,
      intent: 'order',
      authoredOn: new Date().toISOString(),
      requester: { display: requesterName },
      focus: { reference: `${resourceType}/${resourceId || 'new'}`, type: resourceType },
      identifier: [{ system: 'http://harmonia.net/correlation-id', value: corrId }],
      statusReason,
      input: [
        { type: { text: 'operation' }, valueCode: operation },
        { type: { text: 'resourceType' }, valueCode: resourceType },
        { type: { text: 'resourceId' }, valueString: resourceId },
        { type: { text: 'payload' }, valueResource: payload }
      ],
      output
    };

    this.tasks.set(taskId, newTask);
    safeLogger.info(`Submitted change request ${taskId} for ${resourceType}/${resourceId || 'NEW'}`);

    // Fetch existing target resource for diffing
    let targetExisting: any = null;
    if (resourceId) {
      targetExisting = await providerRegistryClient.get(resourceType, resourceId);
    }

    return mapTaskToChangeRequest(newTask, targetExisting);
  }

  /**
   * Retrieves all change requests/tasks with optional filtering.
   */
  public async getChangeRequests(filter?: {
    requester?: string;
    status?: string;
    resourceType?: string;
  }): Promise<ChangeRequestSummaryView[]> {
    let list = Array.from(this.tasks.values());

    if (filter?.status) {
      list = list.filter(t => t.status.toLowerCase() === filter.status?.toLowerCase());
    }
    if (filter?.requester) {
      list = list.filter(t => t.requester?.display?.toLowerCase().includes(filter.requester!.toLowerCase()));
    }
    if (filter?.resourceType) {
      list = list.filter(t => t.focus?.type?.toLowerCase() === filter.resourceType?.toLowerCase());
    }

    // Sort descending by authoredOn
    list.sort((a, b) => (b.authoredOn || '').localeCompare(a.authoredOn || ''));

    const summaries: ChangeRequestSummaryView[] = [];
    for (const task of list) {
      const resType = task.focus?.type || 'Practitioner';
      const resId = task.focus?.reference?.split('/')[1];
      let target: any = null;
      if (resId && resId !== 'new') {
        target = await providerRegistryClient.get(resType, resId);
      }
      summaries.push(mapTaskToChangeRequest(task, target));
    }
    return summaries;
  }

  /**
   * Authorizes or approves a pending change request in the departmental work queue.
   */
  public async approveChangeRequest(taskId: string): Promise<ChangeRequestSummaryView> {
    const task = this.tasks.get(taskId);
    if (!task) throw new Error(`Task ${taskId} not found`);

    task.status = 'completed';
    task.lastModified = new Date().toISOString();

    // Commit changes into Provider Registry repository
    const payloadInput = task.input?.find(i => i.type?.text === 'payload');
    const payload = payloadInput?.valueResource;
    if (payload) {
      await providerRegistryClient.save(payload);
    }

    this.tasks.set(taskId, task);
    safeLogger.info(`Approved and committed change request ${taskId}`);
    return mapTaskToChangeRequest(task);
  }

  /**
   * Rejects a change request with authoritative feedback reason.
   */
  public async rejectChangeRequest(taskId: string, reason: string): Promise<ChangeRequestSummaryView> {
    const task = this.tasks.get(taskId);
    if (!task) throw new Error(`Task ${taskId} not found`);

    task.status = 'rejected';
    task.statusReason = { text: reason };
    task.lastModified = new Date().toISOString();

    this.tasks.set(taskId, task);
    safeLogger.info(`Rejected change request ${taskId}: ${reason}`);
    return mapTaskToChangeRequest(task);
  }
}

export const providerChangeRequestClient = new ProviderChangeRequestClient();
