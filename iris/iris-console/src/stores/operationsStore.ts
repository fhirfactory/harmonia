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

import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import type { 
  SystemStatus, OperationResource, QueueStatus, CacheStatus, ModuleStatus,
  OperationalSummary, OperationalSubsystem, OperationalInstance,
  OperationalHealth, TimeSeries, OperationalAlert
} from '../models/operations';
import { operationsApi } from '../api/operationsClient';
import { findAuthoritativeSubsystem } from '../models/subsystemHierarchy';
import {
  IDLE_STATE,
  describeFailure,
  empty as emptyState,
  loaded as loadedState,
  loading as loadingState,
  unavailable as unavailableState,
  type LoadState
} from '../models/loadState';

// --------------------------------------------------------------------------
// TEST / FIXTURE / DEMONSTRATION MODEL ONLY.
//
// This declared model is retained explicitly for unit tests, fixtures, or
// development demonstrations. It is NEVER used as a production runtime
// fallback when the Operations API is unavailable or empty.
// --------------------------------------------------------------------------
export function getDefaultSubsystems(): OperationalSubsystem[] {
  const now = Date.now();
  return [
    { 
      id: 'pylai', 
      name: 'Pylai', 
      description: 'HL7 MLLP & FHIR Inbound/Outbound Protocol Gateways', 
      state: 'UNKNOWN', 
      instanceCount: 0, 
      version: '1.0.0', 
      lastUpdated: now,
      children: [
        { id: 'pylai-mllp-in', name: 'MLLP Inbound Gateway', description: 'Dual-write ACK gateway on ports 2575 / 8084', state: 'UNKNOWN', instanceCount: 0, version: '1.0.0', lastUpdated: now },
        { id: 'pylai-mllp-out-his', name: 'MLLP Outbound HIS', description: 'Outbound HL7 v2 gateway on port 8087', state: 'UNKNOWN', instanceCount: 0, version: '1.0.0', lastUpdated: now },
        { id: 'pylai-mllp-out-lis', name: 'MLLP Outbound LIS', description: 'Outbound HL7 v2 gateway on port 8088', state: 'UNKNOWN', instanceCount: 0, version: '1.0.0', lastUpdated: now },
        { id: 'pylai-fhir-registry', name: 'FHIR Provider Registry Gateway', description: 'Practitioner & Organization endpoint on port 8089', state: 'UNKNOWN', instanceCount: 0, version: '1.0.0', lastUpdated: now }
      ]
    },
    { id: 'petasos', name: 'Petasos', description: 'Resilient Messaging Abstraction & ActiveMQ Artemis Broker', state: 'UNKNOWN', instanceCount: 0, version: '1.0.0', lastUpdated: now },
    { 
      id: 'energeia', 
      name: 'Energeia', 
      description: 'Task Processing, Ergon Activity & Praxis Workflow Orchestration', 
      state: 'UNKNOWN', 
      instanceCount: 0, 
      version: '1.0.0', 
      lastUpdated: now,
      children: [
        { id: 'ponos', name: 'Ponos', description: 'Ponos Task Processor & Activity Handler workers', state: 'UNKNOWN', instanceCount: 0, version: '1.0.0', lastUpdated: now },
        { id: 'praxis', name: 'Praxis', description: 'Praxis Workflow Engine & Pragma State Coordinator', state: 'UNKNOWN', instanceCount: 0, version: '1.0.0', lastUpdated: now },
        { id: 'ergon', name: 'Ergon', description: 'Task / Work Unit Activities & Payload Transformers', state: 'UNKNOWN', instanceCount: 0, version: '1.0.0', lastUpdated: now },
        { id: 'pragma', name: 'Pragma', description: 'Task Instances & Runtime Checkpoints', state: 'UNKNOWN', instanceCount: 0, version: '1.0.0', lastUpdated: now }
      ]
    },
    { id: 'mneme', name: 'Mneme', description: 'Infinispan Distributed Replicated In-Memory Cache Grid', state: 'UNKNOWN', instanceCount: 0, version: '1.0.0', lastUpdated: now },
    { id: 'mnemosyne', name: 'Mnemosyne', description: 'Clinical & Operational HAPI FHIR R5 Persistence', state: 'UNKNOWN', instanceCount: 0, version: '1.0.0', lastUpdated: now },
    { id: 'calliope', name: 'Calliope', description: 'Canonical Schemas, Transformers & Clinical Models', state: 'UNKNOWN', instanceCount: 0, version: '1.0.0', lastUpdated: now },
    { id: 'themis', name: 'Themis', description: 'Default-Deny Policy Evaluation & RBAC Engine', state: 'UNKNOWN', instanceCount: 0, version: '1.0.0', lastUpdated: now },
    { id: 'agora', name: 'Agora', description: 'Matrix/Synapse Collaboration & Healthcare AS Bridge', state: 'UNKNOWN', instanceCount: 0, version: '1.0.0', lastUpdated: now },
    { 
      id: 'iris', 
      name: 'Iris', 
      description: 'Presentation Tier & BEFE Dual-Port Gateway', 
      state: 'UNKNOWN', 
      instanceCount: 0, 
      version: '1.0.0', 
      lastUpdated: now,
      children: [
        { id: 'iris-befe', name: 'Iris BEFE Gateway', description: 'WildFly 31 Jakarta EE gateway (:8080 Clinical, :8090 Operations)', state: 'UNKNOWN', instanceCount: 0, version: '1.0.0', lastUpdated: now },
        { id: 'iris-clinical', name: 'Iris Clinical SPA', description: 'Vue 3 Clinical FHIR R5 web application on port 3000', state: 'UNKNOWN', instanceCount: 0, version: '1.0.0', lastUpdated: now },
        { id: 'iris-administration', name: 'Iris Administration SPA', description: 'Vue 3 Self-service and registry workbench on port 3002', state: 'UNKNOWN', instanceCount: 0, version: '1.0.0', lastUpdated: now }
      ]
    }
  ];
}

export const useOperationsStore = defineStore('operations', () => {
  // --------------------------------------------------------------------------
  // Normalized Operations State
  // --------------------------------------------------------------------------
  const summary = ref<OperationalSummary | null>(null);
  const subsystems = ref<OperationalSubsystem[]>([]);
  const selectedSubsystemId = ref<string>('petasos');
  const instances = ref<OperationalInstance[]>([]);
  const selectedInstance = ref<OperationalInstance | null>(null);
  const isInstanceDrawerOpen = ref<boolean>(false);
  const currentHealth = ref<OperationalHealth | null>(null);
  const statistics = ref<Record<string, TimeSeries>>({});
  const selectedWindow = ref<'15m' | '1h' | '6h' | '24h'>('1h');
  const alerts = ref<OperationalAlert[]>([]);
  const selectedSeverityFilter = ref<string>('ALL');
  const selectedStatusFilter = ref<string>('ALL');
  const selectedAlertSubsystemFilter = ref<string>('ALL');

  const loading = ref(false);
  const refreshing = ref(false);
  const error = ref<string | null>(null);
  const isStale = ref(false);
  const lastRefreshed = ref<Date | null>(null);
  let pollingInterval: any = null;

  // --------------------------------------------------------------------------
  // Per-slice Load State (additive; existing loading/error consumers unaffected)
  //
  // Recorded so a page can distinguish an empty result set from an unreachable
  // operations API. Data already held is retained and simply flagged.
  // --------------------------------------------------------------------------
  const summaryState = ref<LoadState>(IDLE_STATE);
  const subsystemsState = ref<LoadState>(IDLE_STATE);
  const instancesState = ref<LoadState>(IDLE_STATE);
  const healthState = ref<LoadState>(IDLE_STATE);
  const statisticsState = ref<LoadState>(IDLE_STATE);
  const alertsState = ref<LoadState>(IDLE_STATE);

  /** True when the subsystem tree shown is the declared fallback, not telemetry. */
  const subsystemsAreFallback = ref(false);

  // --------------------------------------------------------------------------
  // Legacy / Backward Compatible State
  // --------------------------------------------------------------------------
  const status = ref<SystemStatus | null>(null);
  const operationalResources = ref<OperationResource[]>([]);
  const modules = ref<ModuleStatus[]>([]);

  const defaultQueues = ref<QueueStatus[]>([
    { queueName: 'task.processing.queue', messageCount: 0, consumerCount: 1, status: 'ACTIVE', targetSequence: 'Standard Processing Queue' },
    { queueName: 'task.event.queue', messageCount: 0, consumerCount: 1, status: 'ACTIVE', targetSequence: 'Global Event Notifications' },
    { queueName: 'task.event.queue.mllp-gateway-default', messageCount: 0, consumerCount: 1, status: 'ACTIVE', gatewayInstance: 'mllp-gateway-default' },
    { queueName: 'task.event.queue.pas-gw', messageCount: 0, consumerCount: 1, status: 'ACTIVE', gatewayInstance: 'pas-gw', targetSequence: 'seq-patient-identity-pipeline' },
    { queueName: 'task.event.queue.lims-gw', messageCount: 0, consumerCount: 1, status: 'ACTIVE', gatewayInstance: 'lims-gw' }
  ]);

  const defaultCaches = ref<CacheStatus[]>([
    { cacheName: 'modulestatus-cache', type: 'OPERATIONS', mode: 'SYNC', size: 0, persistenceStore: 'Operations JPA Server (PostgreSQL)', status: 'HEALTHY' },
    { cacheName: 'tasksequence-cache', type: 'OPERATIONS', mode: 'SYNC', size: 0, persistenceStore: 'Operations JPA Server (PostgreSQL)', status: 'HEALTHY' },
    { cacheName: 'messagequeue-cache', type: 'OPERATIONS', mode: 'SYNC', size: 0, persistenceStore: 'Operations JPA Server (PostgreSQL)', status: 'HEALTHY' },
    { cacheName: 'task-cache', type: 'WORKFLOW', mode: 'SYNC', size: 0, persistenceStore: 'HAPI FHIR JPA (PostgreSQL)', status: 'HEALTHY' },
    { cacheName: 'communication-cache', type: 'WORKFLOW', mode: 'SYNC', size: 0, persistenceStore: 'HAPI FHIR JPA (PostgreSQL)', status: 'HEALTHY' },
    { cacheName: 'provenance-cache', type: 'FHIR', mode: 'SYNC', size: 0, persistenceStore: 'HAPI FHIR JPA (PostgreSQL)', status: 'HEALTHY' },
    { cacheName: 'person-cache', type: 'FHIR', mode: 'SYNC', size: 0, persistenceStore: 'HAPI FHIR JPA (PostgreSQL)', status: 'HEALTHY' },
    { cacheName: 'practitioner-cache', type: 'FHIR', mode: 'SYNC', size: 0, persistenceStore: 'HAPI FHIR JPA (PostgreSQL)', status: 'HEALTHY' },
    { cacheName: 'organization-cache', type: 'FHIR', mode: 'SYNC', size: 0, persistenceStore: 'HAPI FHIR JPA (PostgreSQL)', status: 'HEALTHY' }
  ]);

  // --------------------------------------------------------------------------
  // Computed Properties
  // --------------------------------------------------------------------------
  const selectedSubsystem = computed<OperationalSubsystem | null>(() => {
    const id = selectedSubsystemId.value;
    for (const sub of subsystems.value) {
      if (sub.id === id) return sub;
      if (sub.children) {
        for (const child of sub.children) {
          if (child.id === id) return child;
        }
      }
    }
    const directMatch = subsystems.value.find(s => s.id === id);
    if (directMatch) return directMatch;

    // Honest fallback synthesis for authoritative components when backend telemetry bean is absent
    const auth = findAuthoritativeSubsystem(id);
    if (auth) {
      return {
        id: auth.id,
        name: auth.name,
        description: auth.description,
        state: 'UNKNOWN',
        instanceCount: 0,
        version: '1.0.0',
        lastUpdated: Date.now()
      };
    }

    return null;
  });

  const criticalAlertsCount = computed(() => {
    if (summary.value?.criticalAlerts !== undefined) {
      return summary.value.criticalAlerts;
    }
    return alerts.value.filter(a => a.severity === 'CRITICAL' && a.status === 'ACTIVE').length;
  });

  const warningAlertsCount = computed(() => {
    if (summary.value?.warningAlerts !== undefined) {
      return summary.value.warningAlerts;
    }
    return alerts.value.filter(a => a.severity === 'WARNING' && a.status === 'ACTIVE').length;
  });

  const loadStates = computed<Record<string, LoadState>>(() => ({
    summary: summaryState.value,
    subsystems: subsystemsState.value,
    instances: instancesState.value,
    health: healthState.value,
    statistics: statisticsState.value,
    alerts: alertsState.value
  }));

  const isOperationsApiUnavailable = computed(() =>
    Object.values(loadStates.value).some(state => state.kind === 'unavailable')
  );

  // --------------------------------------------------------------------------
  // Normalized Operations Actions
  // --------------------------------------------------------------------------
  const fetchSummary = async () => {
    summaryState.value = loadingState();
    try {
      const data = await operationsApi.getSummary();
      summary.value = data;
      summaryState.value = data ? loadedState() : emptyState();
    } catch (err: any) {
      // Leave the last known summary untouched. An unavailable API is not a healthy platform.
      summaryState.value = unavailableState(describeFailure(err, 'the platform summary'));
    }
  };

  const fetchSubsystems = async () => {
    subsystemsState.value = loadingState();
    try {
      const data = await operationsApi.getSubsystems();
      if (data && data.length > 0) {
        subsystems.value = data;
        subsystemsAreFallback.value = false;
        subsystemsState.value = loadedState();
      } else {
        subsystems.value = [];
        subsystemsAreFallback.value = false;
        subsystemsState.value = emptyState();
      }
    } catch (err: any) {
      // Do not retain a hard-coded subsystem tree as production fallback.
      // An unavailable Operations API must produce an unavailable/error state.
      if (subsystemsState.value.kind !== 'loaded') {
        subsystems.value = [];
      }
      subsystemsAreFallback.value = false;
      subsystemsState.value = unavailableState(describeFailure(err, 'the subsystem inventory'));
    }
  };

  const loadDemoSubsystems = () => {
    subsystems.value = getDefaultSubsystems();
    subsystemsAreFallback.value = true;
    subsystemsState.value = loadedState();
  };

  const fetchInstances = async (subsystemId: string) => {
    instancesState.value = loadingState();
    try {
      const data = await operationsApi.getSubsystemInstances(subsystemId);
      instances.value = Array.isArray(data) ? data : [];
      instancesState.value = instances.value.length > 0 ? loadedState() : emptyState();
    } catch (err: any) {
      instances.value = [];
      instancesState.value = unavailableState(describeFailure(err, `${subsystemId} instances`));
    }
  };

  const fetchHealth = async (subsystemId: string) => {
    healthState.value = loadingState();
    try {
      currentHealth.value = await operationsApi.getSubsystemHealth(subsystemId);
      isStale.value = Boolean(currentHealth.value?.stale);
      healthState.value = currentHealth.value ? loadedState() : emptyState();
    } catch (err: any) {
      healthState.value = unavailableState(describeFailure(err, `${subsystemId} health`));
      currentHealth.value = {
        subsystemId,
        status: 'UNKNOWN',
        availabilityPercent: null,
        failedOperations: 0,
        restartCount: 0,
        p95LatencyMs: null,
        dependenciesSummary: 'Unavailable',
        stale: true,
        dependencies: []
      };
      isStale.value = true;
    }
  };

  const fetchStatistics = async (subsystemId: string, windowVal: '15m' | '1h' | '6h' | '24h' = selectedWindow.value) => {
    statisticsState.value = loadingState();
    try {
      const data = await operationsApi.getSubsystemStatistics(subsystemId, windowVal);
      statistics.value = data || {};
      statisticsState.value = Object.keys(statistics.value).length > 0 ? loadedState() : emptyState();
    } catch (err: any) {
      statistics.value = {};
      statisticsState.value = unavailableState(describeFailure(err, `${subsystemId} statistics`));
    }
  };

  const fetchAlerts = async (severity?: string, statusVal?: string, subsystem?: string) => {
    alertsState.value = loadingState();
    try {
      const sev = severity !== undefined ? severity : (selectedSeverityFilter.value !== 'ALL' ? selectedSeverityFilter.value : undefined);
      const st = statusVal !== undefined ? statusVal : (selectedStatusFilter.value !== 'ALL' ? selectedStatusFilter.value : undefined);
      const sub = subsystem !== undefined ? subsystem : (selectedAlertSubsystemFilter.value !== 'ALL' ? selectedAlertSubsystemFilter.value : undefined);
      const data = await operationsApi.getAlerts(sev, st, sub);
      alerts.value = Array.isArray(data) ? data : [];
      alertsState.value = alerts.value.length > 0 ? loadedState() : emptyState();
    } catch (err: any) {
      alerts.value = [];
      alertsState.value = unavailableState(describeFailure(err, 'alerts'));
    }
  };

  const acknowledgeAlert = async (alertId: string, operator: string = 'operator') => {
    try {
      const res = await operationsApi.acknowledgeAlert(alertId);
      const existing = alerts.value.find(a => a.alertId === alertId);
      if (existing) {
        existing.status = 'ACKNOWLEDGED';
      }
      return res;
    } catch (err: any) {
      error.value = err.message || 'Failed acknowledging alert';
      throw err;
    }
  };

  const selectSubsystem = async (subsystemId: string) => {
    selectedSubsystemId.value = subsystemId;
    loading.value = true;
    error.value = null;
    try {
      await Promise.allSettled([
        fetchInstances(subsystemId),
        fetchHealth(subsystemId),
        fetchStatistics(subsystemId, selectedWindow.value)
      ]);
    } finally {
      loading.value = false;
      lastRefreshed.value = new Date();
    }
  };

  const setWindow = async (windowVal: '15m' | '1h' | '6h' | '24h') => {
    selectedWindow.value = windowVal;
    if (selectedSubsystemId.value) {
      await fetchStatistics(selectedSubsystemId.value, windowVal);
    }
  };

  const openInstanceDrawer = (instance: OperationalInstance) => {
    selectedInstance.value = instance;
    isInstanceDrawerOpen.value = true;
  };

  const closeInstanceDrawer = () => {
    isInstanceDrawerOpen.value = false;
    selectedInstance.value = null;
  };

  const refreshAll = async () => {
    refreshing.value = true;
    try {
      await Promise.allSettled([
        fetchSummary(),
        fetchSubsystems(),
        selectedSubsystemId.value ? fetchInstances(selectedSubsystemId.value) : Promise.resolve(),
        selectedSubsystemId.value ? fetchHealth(selectedSubsystemId.value) : Promise.resolve(),
        selectedSubsystemId.value ? fetchStatistics(selectedSubsystemId.value, selectedWindow.value) : Promise.resolve(),
        fetchAlerts()
      ]);
      lastRefreshed.value = new Date();
    } finally {
      refreshing.value = false;
    }
  };

  const startPolling = (intervalMs: number = 10000) => {
    if (pollingInterval) clearInterval(pollingInterval);
    pollingInterval = setInterval(() => {
      refreshAll();
    }, intervalMs);
  };

  const stopPolling = () => {
    if (pollingInterval) {
      clearInterval(pollingInterval);
      pollingInterval = null;
    }
  };

  // --------------------------------------------------------------------------
  // Legacy Actions
  // --------------------------------------------------------------------------
  const fetchStatus = async () => {
    loading.value = true;
    error.value = null;
    try {
      status.value = await operationsApi.getSystemStatus();
      if (status.value?.clusterModules) {
        modules.value = status.value.clusterModules;
      }
    } catch (err: any) {
      status.value = {
        systemName: 'Harmonia Platform 5-Tier Architecture',
        version: '1.0.0-SNAPSHOT',
        timestamp: Date.now(),
        infinispanClusterConnected: true,
        infinispanClusterMode: 'SYNC Replicated Distributed Data Grid',
        tiers: {
          tier1_ui: { name: 'Operations UI & FHIR Resource UI', tech: 'Vue 3 + TypeScript', status: 'UP' },
          tier2_befe: { name: 'BEFE Gateway (Dual-Port FHIR & Operations)', tech: 'WildFly Jakarta EE 10', status: 'UP', port: 8090 },
          tier3_infinispan: { name: 'Infinispan 15 HA Cluster', tech: 'Infinispan Hot Rod (JGroups TCP)', status: 'UP', port: 11222 },
          tier4_persistence_spi: { name: 'Persistence Stores (NonBlockingStore SPI)', tech: 'Custom REST Cache Store', status: 'UP' },
          tier5_database: { name: 'HAPI FHIR JPA & Operations JPA', tech: 'PostgreSQL 16', status: 'UP' }
        },
        sequences: { totalSequences: 1 }
      };
    } finally {
      loading.value = false;
    }
  };

  const fetchModules = async () => {
    try {
      const res = await operationsApi.getClusterModules();
      if (res && res.length > 0) {
        modules.value = res;
      }
    } catch {
      // Keep existing
    }
  };

  const fetchOperationalResources = async (objectType: string = 'tasksequence') => {
    loading.value = true;
    error.value = null;
    try {
      operationalResources.value = await operationsApi.getOperationalResources(objectType);
    } catch (err: any) {
      error.value = err.message || 'Failed to fetch operational data';
    } finally {
      loading.value = false;
    }
  };

  return {
    // Operations perspective state
    summary,
    subsystems,
    selectedSubsystemId,
    selectedSubsystem,
    instances,
    selectedInstance,
    isInstanceDrawerOpen,
    currentHealth,
    statistics,
    selectedWindow,
    alerts,
    selectedSeverityFilter,
    selectedStatusFilter,
    selectedAlertSubsystemFilter,
    loading,
    refreshing,
    error,
    isStale,
    lastRefreshed,
    criticalAlertsCount,
    warningAlertsCount,

    // Per-slice load states
    summaryState,
    subsystemsState,
    instancesState,
    healthState,
    statisticsState,
    alertsState,
    loadStates,
    isOperationsApiUnavailable,
    subsystemsAreFallback,

    // Operations perspective actions
    fetchSummary,
    fetchSubsystems,
    loadDemoSubsystems,
    fetchInstances,
    fetchHealth,
    fetchStatistics,
    fetchAlerts,
    acknowledgeAlert,
    selectSubsystem,
    setWindow,
    openInstanceDrawer,
    closeInstanceDrawer,
    refreshAll,
    startPolling,
    stopPolling,

    // Legacy state & actions
    status,
    operationalResources,
    modules,
    queues: defaultQueues,
    caches: defaultCaches,
    fetchStatus,
    fetchModules,
    fetchOperationalResources
  };
});
