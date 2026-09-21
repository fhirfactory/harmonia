<!--
  Copyright (c) 2026 Mark Hunter

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program.  If not, see <https://www.gnu.org/licenses/>.
-->

<script setup lang="ts">
import { onMounted, onUnmounted, computed } from 'vue';
import { useOperationsStore } from '../stores/operationsStore';
import { useQueueStore } from '../stores/queueStore';
import {
  IrisPageHeader,
  IrisToolbar,
  IrisSection,
  IrisStatus,
  IrisEmptyState,
  IrisLoadingState,
  IrisErrorState
} from '@harmonia/iris-befe';
import type { OperationalSubsystem } from '../models/operations';
import { isPending } from '../models/loadState';
import { AlertOctagon, AlertTriangle, ArrowRight, CheckCircle2 } from 'lucide-vue-next';

const operationsStore = useOperationsStore();
const queueStore = useQueueStore();

let refreshTimer: ReturnType<typeof setInterval> | null = null;

/**
 * Overview is built only from the four operations endpoints the platform
 * genuinely exposes: summary, subsystems, alerts and queues.
 */
async function refreshAll() {
  await Promise.allSettled([
    operationsStore.fetchSummary(),
    operationsStore.fetchSubsystems(),
    operationsStore.fetchAlerts(),
    queueStore.fetchQueues(true)
  ]);
}

onMounted(() => {
  refreshAll();
  refreshTimer = setInterval(refreshAll, 15000);
});

onUnmounted(() => {
  if (refreshTimer) {
    clearInterval(refreshTimer);
    refreshTimer = null;
  }
});

// ---------------------------------------------------------------------------
// Slice states
// ---------------------------------------------------------------------------
const summaryState = computed(() => operationsStore.summaryState);
const subsystemsState = computed(() => operationsStore.subsystemsState);
const alertsState = computed(() => operationsStore.alertsState);
const queuesState = computed(() => queueStore.queuesState);

const sliceStates = computed(() => [
  summaryState.value,
  subsystemsState.value,
  alertsState.value,
  queuesState.value
]);

const isInitialLoad = computed(() =>
  sliceStates.value.every(state => isPending(state)) && operationsStore.subsystems.length === 0
);

const unavailableSlices = computed(() =>
  [
    { label: 'Platform summary', state: summaryState.value },
    { label: 'Subsystems', state: subsystemsState.value },
    { label: 'Alerts', state: alertsState.value },
    { label: 'Queue activity', state: queuesState.value }
  ].filter(slice => slice.state.kind === 'unavailable')
);

const allSlicesUnavailable = computed(() => unavailableSlices.value.length === sliceStates.value.length);

const isPartial = computed(() =>
  unavailableSlices.value.length > 0 && !allSlicesUnavailable.value
);

const partialMessage = computed(() =>
  `Partial data: ${unavailableSlices.value.map(slice => slice.label).join(', ')} could not be loaded. The remaining sections below are current.`
);

function failureMessage(kind: string, state: { kind: string; message?: string }): string {
  return state.kind === kind && state.message ? state.message : '';
}

// ---------------------------------------------------------------------------
// Platform status strip (getSummary, corroborated by getSubsystems)
// ---------------------------------------------------------------------------
const summary = computed(() => operationsStore.summary);

const platformStatus = computed(() => {
  if (summary.value?.platformStatus) return summary.value.platformStatus;
  if (operationsStore.subsystemsAreFallback || operationsStore.subsystems.length === 0) return 'UNKNOWN';
  if (operationsStore.subsystems.some(s => s.state === 'UNAVAILABLE')) return 'UNAVAILABLE';
  if (operationsStore.subsystems.some(s => s.state === 'DEGRADED')) return 'DEGRADED';
  if (operationsStore.subsystems.some(s => s.state === 'UNKNOWN')) return 'UNKNOWN';
  return 'HEALTHY';
});

const NOT_REPORTED = 'Not reported';

function reported(value: number | null | undefined): string {
  return value == null ? NOT_REPORTED : value.toLocaleString();
}

const totalSubsystems = computed<number | null>(() => {
  if (summary.value?.totalSubsystems != null) return summary.value.totalSubsystems;
  if (operationsStore.subsystemsAreFallback) return null;
  return operationsStore.subsystems.length > 0 ? operationsStore.subsystems.length : null;
});

const degradedSubsystems = computed<number | null>(() => {
  if (summary.value?.degradedSubsystems != null) return summary.value.degradedSubsystems;
  if (operationsStore.subsystemsAreFallback || operationsStore.subsystems.length === 0) return null;
  return operationsStore.subsystems.filter(s => s.state === 'DEGRADED' || s.state === 'UNAVAILABLE').length;
});

const healthySubsystems = computed<number | null>(() => {
  if (totalSubsystems.value == null || degradedSubsystems.value == null) return null;
  return Math.max(0, totalSubsystems.value - degradedSubsystems.value);
});

const environmentText = computed(() => summary.value?.environment || NOT_REPORTED);
const clusterText = computed(() => summary.value?.cluster || NOT_REPORTED);

const lastRefreshedText = computed(() => {
  const at = operationsStore.lastRefreshed || queueStore.lastRefreshed;
  if (!at) return 'Not refreshed yet';
  return at.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', second: '2-digit' });
});

// ---------------------------------------------------------------------------
// Dense subsystem grid (getSubsystems)
// ---------------------------------------------------------------------------
const subsystemEntries = computed(() => {
  const flat: OperationalSubsystem[] = [];
  const visit = (nodes: OperationalSubsystem[]) => {
    for (const node of nodes) {
      flat.push(node);
      if (node.children?.length) visit(node.children);
    }
  };
  visit(operationsStore.subsystems);
  return flat;
});

const problemSubsystems = computed(() =>
  subsystemEntries.value.filter(s => s.state === 'DEGRADED' || s.state === 'UNAVAILABLE')
);

// ---------------------------------------------------------------------------
// Alerts (getAlerts)
// ---------------------------------------------------------------------------
const criticalAlerts = computed(() => operationsStore.criticalAlertsCount);
const warningAlerts = computed(() => operationsStore.warningAlertsCount);
const totalAlerts = computed(() => criticalAlerts.value + warningAlerts.value);
const topAlerts = computed(() => operationsStore.alerts.slice(0, 4));

// ---------------------------------------------------------------------------
// Queue activity (getQueues)
// ---------------------------------------------------------------------------
const queueMetrics = computed(() => [
  { label: 'Queues', value: reported(queueStore.totalQueues) },
  { label: 'Messages in flight', value: reported(queueStore.messagesInFlight) },
  { label: 'Consumers', value: reported(queueStore.totalConsumers) },
  { label: 'Dead letter depth', value: reported(queueStore.totalDlqDepth) }
]);

// ---------------------------------------------------------------------------
// Toolbar time window
// ---------------------------------------------------------------------------
const timeWindow = computed(() => operationsStore.selectedWindow);

function onTimeWindowChange(value: string) {
  operationsStore.setWindow(value as '15m' | '1h' | '6h' | '24h');
}
</script>

<template>
  <div class="overview-view">
    <IrisPageHeader
      title="Overview"
      subtitle="Operational summary of the Harmonia platform, drawn from the operations API only."
    >
      <template #badges>
        <IrisStatus :status="platformStatus" size="sm" label-format="upper" />
        <span v-if="operationsStore.subsystemsAreFallback" class="overview-view__badge">
          Declared inventory — no subsystem telemetry
        </span>
      </template>
    </IrisPageHeader>

    <IrisToolbar
      :show-search="false"
      :show-refresh="true"
      :show-time-window="true"
      :time-window="timeWindow"
      :refreshing="operationsStore.refreshing || queueStore.refreshing"
      @refresh="refreshAll"
      @update:time-window="onTimeWindowChange"
    >
      <template #left>
        <span class="overview-view__toolbar-label">Last refreshed: {{ lastRefreshedText }}</span>
      </template>
    </IrisToolbar>

    <!-- Initial load -->
    <IrisLoadingState v-if="isInitialLoad" message="Loading platform overview..." />

    <template v-else>
      <!-- Whole operations API unreachable -->
      <IrisErrorState
        v-if="allSlicesUnavailable"
        title="Operations API unavailable"
        message="No section of the overview could be loaded. This is not an idle platform — the operations API could not be reached."
        :detail="failureMessage('unavailable', summaryState)"
        :retryable="true"
        retry-label="Retry"
        @retry="refreshAll"
      />

      <template v-else>
        <!-- Partial failure -->
        <div v-if="isPartial" class="overview-view__partial" role="status">
          <AlertTriangle :size="15" aria-hidden="true" />
          <span>{{ partialMessage }}</span>
        </div>

        <!-- Platform status strip -->
        <section class="overview-view__strip" aria-label="Platform status">
          <IrisErrorState
            v-if="summaryState.kind === 'unavailable'"
            title="Platform summary unavailable"
            :message="failureMessage('unavailable', summaryState)"
            :retryable="true"
            @retry="refreshAll"
          />
          <template v-else>
            <div class="overview-view__strip-item overview-view__strip-item--status">
              <span class="overview-view__strip-label">Platform status</span>
              <IrisStatus :status="platformStatus" size="md" label-format="upper" />
            </div>
            <div class="overview-view__strip-item">
              <span class="overview-view__strip-label">Subsystems healthy</span>
              <span class="overview-view__strip-value">
                {{ healthySubsystems == null ? NOT_REPORTED : `${healthySubsystems} of ${totalSubsystems}` }}
              </span>
            </div>
            <div class="overview-view__strip-item">
              <span class="overview-view__strip-label">Degraded</span>
              <span class="overview-view__strip-value">{{ reported(degradedSubsystems) }}</span>
            </div>
            <div class="overview-view__strip-item">
              <span class="overview-view__strip-label">Active alerts</span>
              <span class="overview-view__strip-value">{{ totalAlerts }}</span>
            </div>
            <div class="overview-view__strip-item">
              <span class="overview-view__strip-label">Environment</span>
              <span class="overview-view__strip-value">{{ environmentText }}</span>
            </div>
            <div class="overview-view__strip-item">
              <span class="overview-view__strip-label">Cluster</span>
              <span class="overview-view__strip-value">{{ clusterText }}</span>
            </div>
          </template>
        </section>

        <!-- Subsystem health grid -->
        <IrisSection
          title="Subsystem health"
          description="Every subsystem reported by the operations API. Select one to drill into instances, health and statistics."
        >
          <template #actions>
            <router-link to="/subsystems" class="overview-view__link">
              <span>All subsystems</span>
              <ArrowRight :size="12" aria-hidden="true" />
            </router-link>
          </template>

          <IrisErrorState
            v-if="subsystemsState.kind === 'unavailable'"
            title="Subsystem inventory unavailable"
            :message="failureMessage('unavailable', subsystemsState)"
            :retryable="true"
            @retry="refreshAll"
          />
          <IrisLoadingState v-else-if="isPending(subsystemsState) && subsystemEntries.length === 0" size="sm" />
          <IrisEmptyState
            v-else-if="subsystemEntries.length === 0"
            title="No subsystems present"
            description="The operations API responded successfully but reported no subsystems."
          />
          <template v-else>
            <p v-if="problemSubsystems.length > 0" class="overview-view__note">
              {{ problemSubsystems.length }} subsystem(s) need attention:
              {{ problemSubsystems.map(s => s.name).join(', ') }}.
            </p>
            <ul class="overview-view__grid">
              <li v-for="subsystem in subsystemEntries" :key="subsystem.id" class="overview-view__grid-item">
                <router-link :to="`/subsystems/${subsystem.id}`" class="overview-view__grid-link">
                  <span class="overview-view__grid-name">{{ subsystem.name }}</span>
                  <IrisStatus :status="subsystem.state" size="sm" />
                  <span class="overview-view__grid-meta">
                    {{ subsystem.instanceCount }} instance(s)
                  </span>
                </router-link>
              </li>
            </ul>
          </template>
        </IrisSection>

        <div class="overview-view__columns">
          <!-- Alerts -->
          <IrisSection title="Alerts" description="Active operational conditions reported by the platform.">
            <template #actions>
              <router-link to="/alerts" class="overview-view__link">
                <span>All alerts ({{ totalAlerts }})</span>
                <ArrowRight :size="12" aria-hidden="true" />
              </router-link>
            </template>

            <IrisErrorState
              v-if="alertsState.kind === 'unavailable'"
              title="Alerts unavailable"
              :message="failureMessage('unavailable', alertsState)"
              :retryable="true"
              @retry="refreshAll"
            />
            <IrisLoadingState v-else-if="isPending(alertsState) && topAlerts.length === 0" size="sm" />
            <div v-else-if="topAlerts.length === 0" class="overview-view__clear">
              <CheckCircle2 :size="15" aria-hidden="true" />
              <span>No active alerts present.</span>
            </div>
            <ul v-else class="overview-view__alerts">
              <li v-for="alert in topAlerts" :key="alert.alertId" class="overview-view__alert">
                <component
                  :is="alert.severity === 'CRITICAL' ? AlertOctagon : AlertTriangle"
                  :size="14"
                  aria-hidden="true"
                  class="overview-view__alert-icon"
                />
                <span class="overview-view__alert-severity">{{ alert.severity }}</span>
                <span class="overview-view__alert-condition">{{ alert.condition }}</span>
                <span class="overview-view__alert-subsystem">{{ alert.subsystem }}</span>
              </li>
            </ul>
          </IrisSection>

          <!-- Queue activity -->
          <IrisSection title="Queue activity" description="Petasos queue and broker activity reported by the operations API.">
            <template #actions>
              <router-link to="/messages" class="overview-view__link">
                <span>Messages</span>
                <ArrowRight :size="12" aria-hidden="true" />
              </router-link>
            </template>

            <IrisErrorState
              v-if="queuesState.kind === 'unavailable'"
              title="Queue activity unavailable"
              :message="failureMessage('unavailable', queuesState)"
              :retryable="true"
              @retry="refreshAll"
            />
            <IrisLoadingState v-else-if="isPending(queuesState) && queueStore.totalQueues === 0" size="sm" />
            <IrisEmptyState
              v-else-if="queueStore.totalQueues === 0"
              title="No queues present"
              description="The operations API responded successfully but reported no queues."
            />
            <template v-else>
              <ul class="overview-view__metrics">
                <li v-for="metric in queueMetrics" :key="metric.label" class="overview-view__metric">
                  <span class="overview-view__metric-label">{{ metric.label }}</span>
                  <span class="overview-view__metric-value">{{ metric.value }}</span>
                </li>
              </ul>
              <p class="overview-view__note">
                Broker topology:
                {{ queueStore.isBrokerTopologyKnown ? queueStore.brokerTopology : 'Not reported by the operations API' }}
              </p>
            </template>
          </IrisSection>
        </div>
      </template>
    </template>
  </div>
</template>

<style scoped>
.overview-view {
  display: flex;
  flex-direction: column;
  gap: 0.875rem;
  width: 100%;
  font-family: var(--iris-font-sans);
}

.overview-view__badge {
  padding: 2px 8px;
  border: 1px solid var(--iris-status-idle-border);
  border-radius: var(--iris-border-radius);
  background-color: var(--iris-status-idle-bg);
  color: var(--iris-status-idle-text);
  font-size: 0.6875rem;
  font-weight: 600;
}

.overview-view__toolbar-label {
  font-family: var(--iris-font-mono);
  font-size: 0.75rem;
  color: var(--iris-text-muted);
}

.overview-view__partial {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 0.75rem;
  background-color: var(--iris-status-degraded-bg);
  border: 1px solid var(--iris-status-degraded-border);
  border-radius: var(--iris-border-radius);
  color: var(--iris-status-degraded-text);
  font-size: 0.8125rem;
}

.overview-view__strip {
  display: flex;
  flex-wrap: wrap;
  align-items: stretch;
  gap: 0 1.25rem;
  padding: 0.625rem 0.875rem;
  background-color: var(--iris-bg-surface);
  border: 1px solid var(--iris-border-default);
  border-radius: var(--iris-border-radius);
  box-shadow: var(--iris-shadow-subtle);
}

.overview-view__strip-item {
  display: flex;
  flex-direction: column;
  gap: 0.125rem;
  padding: 0.125rem 1.25rem 0.125rem 0;
  border-right: 1px solid var(--iris-border-default);
  min-width: 8rem;
}

.overview-view__strip-item:last-child {
  border-right: none;
}

.overview-view__strip-label {
  font-size: 0.6875rem;
  font-weight: 600;
  letter-spacing: 0.04em;
  text-transform: uppercase;
  color: var(--iris-text-muted);
}

.overview-view__strip-value {
  font-family: var(--iris-font-mono);
  font-size: 0.8125rem;
  font-weight: 600;
  color: var(--iris-text-primary);
}

.overview-view__link {
  display: inline-flex;
  align-items: center;
  gap: 0.25rem;
  font-size: 0.75rem;
  font-weight: 600;
  color: var(--iris-text-accent);
  text-decoration: none;
}

.overview-view__link:hover,
.overview-view__link:focus-visible {
  text-decoration: underline;
}

.overview-view__note {
  margin: 0.5rem 0 0 0;
  font-size: 0.75rem;
  color: var(--iris-text-secondary);
}

.overview-view__grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(230px, 1fr));
  gap: 0.375rem;
  margin: 0;
  padding: 0;
  list-style: none;
}

.overview-view__grid-item {
  min-width: 0;
}

.overview-view__grid-link {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.3125rem 0.5rem;
  border: 1px solid var(--iris-border-default);
  border-radius: var(--iris-border-radius);
  background-color: var(--iris-bg-surface);
  color: var(--iris-text-primary);
  text-decoration: none;
  font-size: 0.8125rem;
}

.overview-view__grid-link:hover {
  background-color: var(--iris-bg-hover);
}

.overview-view__grid-name {
  flex: 1;
  min-width: 0;
  font-weight: 600;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.overview-view__grid-meta {
  font-family: var(--iris-font-mono);
  font-size: 0.6875rem;
  color: var(--iris-text-muted);
  white-space: nowrap;
}

.overview-view__columns {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(340px, 1fr));
  gap: 0.875rem;
}

.overview-view__clear {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 0.75rem;
  background-color: var(--iris-status-healthy-bg);
  border: 1px solid var(--iris-status-healthy-border);
  border-radius: var(--iris-border-radius);
  color: var(--iris-status-healthy-text);
  font-size: 0.8125rem;
}

.overview-view__alerts {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
  margin: 0;
  padding: 0;
  list-style: none;
}

.overview-view__alert {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.3125rem 0.5rem;
  border: 1px solid var(--iris-border-default);
  border-radius: var(--iris-border-radius);
  font-size: 0.8125rem;
  min-width: 0;
}

.overview-view__alert-icon {
  flex-shrink: 0;
  color: var(--iris-text-secondary);
}

.overview-view__alert-severity {
  font-size: 0.6875rem;
  font-weight: 700;
  letter-spacing: 0.04em;
  color: var(--iris-text-secondary);
}

.overview-view__alert-condition {
  flex: 1;
  min-width: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.overview-view__alert-subsystem {
  font-family: var(--iris-font-mono);
  font-size: 0.6875rem;
  color: var(--iris-text-muted);
}

.overview-view__metrics {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));
  gap: 0.375rem;
  margin: 0;
  padding: 0;
  list-style: none;
}

.overview-view__metric {
  display: flex;
  flex-direction: column;
  gap: 0.125rem;
  padding: 0.375rem 0.5rem;
  border: 1px solid var(--iris-border-default);
  border-radius: var(--iris-border-radius);
  background-color: var(--iris-bg-subtle);
}

.overview-view__metric-label {
  font-size: 0.6875rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.04em;
  color: var(--iris-text-muted);
}

.overview-view__metric-value {
  font-family: var(--iris-font-mono);
  font-size: 0.9375rem;
  font-weight: 700;
  color: var(--iris-text-primary);
}

@media (max-width: 900px) {
  .overview-view__strip-item {
    border-right: none;
  }
}
</style>
