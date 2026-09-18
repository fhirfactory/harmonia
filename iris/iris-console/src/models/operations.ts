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

// ============================================================================
// Normalized Harmonia Operations Console Domain Models (BEFE :8090)
// ============================================================================

export type PlatformStatus = 'HEALTHY' | 'DEGRADED' | 'UNAVAILABLE';
export type SubsystemState = 'HEALTHY' | 'DEGRADED' | 'UNAVAILABLE' | 'UNKNOWN';
export type AlertSeverity = 'CRITICAL' | 'WARNING' | 'INFORMATION';
export type AlertStatus = 'ACTIVE' | 'ACKNOWLEDGED' | 'RESOLVED';

export interface OperationalSummary {
  platformStatus: PlatformStatus;
  environment: string;
  cluster: string;
  timestamp: number;
  totalSubsystems: number;
  degradedSubsystems: number;
  criticalAlerts: number;
  warningAlerts: number;
  lastRefreshed: number;
}

export interface OperationalSubsystem {
  id: string;
  name: string;
  description: string;
  state: SubsystemState;
  instanceCount: number;
  version: string;
  lastUpdated: number;
  stale?: boolean;
  children?: OperationalSubsystem[];
}

export interface OperationalInstance {
  instanceId: string;
  subsystemId: string;
  role?: string;
  state: string;
  ready: boolean;
  restartCount: number;
  uptime?: string;
  startedAt?: number;
  cpuPercent?: number | null;
  memoryMb?: number | null;
  podName?: string;
  namespace?: string;
  nodeName?: string;
  ipAddress?: string;
  containerImage?: string;
  appVersion?: string;
  recentErrors?: string[];
  dependencies?: string[];
}

export interface DependencyHealth {
  name: string;
  status: SubsystemState | string;
  latencyMs?: number | null;
  message?: string;
}

export interface OperationalHealth {
  subsystemId: string;
  status: SubsystemState | string;
  availabilityPercent?: number | null;
  failedOperations: number;
  restartCount: number;
  p95LatencyMs?: number | null;
  dependenciesSummary?: string;
  stale?: boolean;
  dependencies?: DependencyHealth[];
  details?: Record<string, any>;
}

export interface TimeSeriesPoint {
  timestamp: number;
  value: number;
}

export interface TimeSeries {
  metricName: string;
  unit?: string;
  timeWindow?: string;
  points: TimeSeriesPoint[];
}

export interface QueueSummary {
  queueId: string;
  queueName: string;
  address?: string;
  status: string;
  depth: number;
  consumerCount: number;
  producerCount: number;
  enqueueRate: number;
  dequeueRate: number;
  oldestMessageAgeSeconds: number;
  redeliveryCount: number;
  dlqDepth: number;
  expiryCount: number;
  associatedCapability?: string;
  depthHistory?: TimeSeriesPoint[];
}

export interface WorkflowSummary {
  workflowId: string;
  name: string;
  description?: string;
  activeExecutions: number;
  queuedWork: number;
  completedWork: number;
  failedWork: number;
  retryingWork: number;
  processingRate: number;
  p95DurationMs: number;
  failureRate: number;
}

export interface ErgonCheckpoint {
  checkpointId?: string;
  ergonId: string;
  ergonName?: string;
  status: string;
  timestamp?: number;
  startedAt?: number;
  completedAt?: number;
  durationMs?: number;
  errorMessage?: string;
  detail?: string;
  details?: Record<string, any>;
}

export interface PragmaSummary {
  pragmaId: string;
  praxisId?: string;
  status: string;
  startedAt: number;
  durationMs: number;
  currentErgon?: string;
  completedErgaCount: number;
  retryCount: number;
  correlationId?: string;
  causationId?: string;
  failureReasonCode?: string;
  checkpoints?: ErgonCheckpoint[];
}

export interface OperationalEvent {
  eventId: string;
  timestamp: number;
  subsystem: string;
  eventType: string;
  operation: string;
  status: string;
  durationMs: number;
  messageId?: string;
  correlationId?: string;
  causationId?: string;
  pragmaId?: string;
  praxisId?: string;
  ergonId?: string;
  interfaceId?: string;
  reasonCode?: string;
}

export interface OperationalAlert {
  alertId: string;
  severity: AlertSeverity;
  subsystem: string;
  component: string;
  condition: string;
  firstObserved: number;
  lastObserved: number;
  duration: string;
  status: AlertStatus;
  relatedResource?: string;
  correlationInfo?: string;
  operatorGuidance?: string;
}
