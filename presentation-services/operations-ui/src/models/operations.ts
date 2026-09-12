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

export interface Topic {
  domain?: string;
  model?: string;
  modelVersion?: string;
  dataElement?: string;
  dataElementQualifier?: string;
  source?: string;
  target?: string;
  origin?: string;
  destination?: string;
  receivedDate?: string;
}

export interface TopicSubscription {
  domain?: string;
  model?: string;
  modelVersion?: string;
  dataElement?: string;
  dataElementQualifier?: string;
  source?: string;
  target?: string;
  origin?: string;
  destination?: string;
}

export interface TaskSequence {
  sequenceId: string;
  sequenceName: string;
  sequenceDescription?: string;
  description?: string;
  version?: string;
  enabled: boolean;
  sourceQueueName?: string;
  topicSubscriptions?: TopicSubscription[];
  targetGatewayInstances: string[];
  targetTriggerTypes: string[];
  matchAllGateways?: boolean;
  matchAllTriggers?: boolean;
  activityIds: Record<number, string> | string[];
  activities?: Record<number, TaskActivitySummary> | TaskActivitySummary[];
}

export interface TaskActivitySummary {
  activityId: string;
  activityName: string;
  description?: string;
  enabled: boolean;
  inputEndpointUri?: string;
  outputEndpointUri?: string;
  activityOrder?: number;
}

export interface OperationResource {
  id?: number;
  objectType: string;
  objectId: string;
  versionId?: number;
  dataJson: string;
  createdDate?: string;
  lastUpdated?: string;
}

export interface SystemStatus {
  systemName: string;
  version: string;
  timestamp: number;
  infinispanClusterConnected: boolean;
  infinispanClusterMode: string;
  tiers: Record<string, {
    name: string;
    tech: string;
    status: string;
    port?: number;
  }>;
  sequences: {
    totalSequences: number;
  };
  clusterModules?: ModuleStatus[];
  clusterModuleCount?: number;
}

export interface ModuleStatus {
  moduleId: string;
  moduleName?: string;
  moduleType?: string;
  status: string;
  ready: boolean;
  startedAt?: string;
  lastUpdated?: string;
  instanceId?: string;
  host?: string;
  port?: number;
  endpointUrl?: string;
  details?: Record<string, any>;
}

export interface QueueStatus {
  queueName: string;
  messageCount: number;
  consumerCount: number;
  status: 'ACTIVE' | 'IDLE' | 'PAUSED';
  targetSequence?: string;
  gatewayInstance?: string;
}

export interface CacheStatus {
  cacheName: string;
  type: 'FHIR' | 'OPERATIONS' | 'WORKFLOW';
  mode: 'SYNC' | 'ASYNC' | 'LOCAL';
  size: number;
  persistenceStore: string;
  status: 'HEALTHY' | 'DEGRADED';
}
