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
import QueueTable from '../components/queues/QueueTable.vue';
import QueueDetailDrawer from '../components/queues/QueueDetailDrawer.vue';
import {
  IrisPageHeader,
  IrisToolbar,
  IrisSection,
  IrisStatus,
  IrisEmptyState,
  IrisLoadingState,
  IrisErrorState
} from '@harmonia/iris-befe';
import { isPending } from '../models/loadState';
import { Clock, Info } from 'lucide-vue-next';

/**
 * Messages perspective.
 *
 * The navigation label is "Messages", but the page states plainly that what it
 * currently shows is Petasos queue and broker activity: Harmonia exposes no
 * message-observability API, so no message list, payload view or replay control
 * is rendered here. A single page-level action area is reserved for that future
 * capability and deliberately left empty.
 */
const queueStore = useQueueStore();
const operationsStore = useOperationsStore();

let refreshTimer: ReturnType<typeof setInterval> | null = null;

const NOT_REPORTED = 'Not reported by the operations API';

onMounted(async () => {
  await refreshAll();
  refreshTimer = setInterval(() => {
    queueStore.fetchQueues(true);
  }, 10000);
});

onUnmounted(() => {
  if (refreshTimer) {
    clearInterval(refreshTimer);
    refreshTimer = null;
  }
});

async function refreshAll() {
  await Promise.allSettled([queueStore.fetchQueues(), operationsStore.fetchSubsystems()]);
}

// ---------------------------------------------------------------------------
// Petasos subsystem state — reported, never assumed
// ---------------------------------------------------------------------------
const petasosSubsystem = computed(
  () => operationsStore.subsystems.find(subsystem => subsystem.id === 'petasos') || null
);
const isPetasosStateReported = computed(() => Boolean(petasosSubsystem.value?.state));
const petasosStatus = computed(() => String(petasosSubsystem.value?.state || 'UNKNOWN'));

// ---------------------------------------------------------------------------
// Data states
// ---------------------------------------------------------------------------
const queuesState = computed(() => queueStore.queuesState);
const failureMessage = computed(() => {
  const state = queuesState.value;
  return 'message' in state ? state.message : '';
});
const isLoadingFirstResult = computed(
  () => isPending(queuesState.value) && queueStore.queues.length === 0
);
const isUnavailable = computed(() => queuesState.value.kind === 'unavailable');
const isPartial = computed(() => queuesState.value.kind === 'partial');
const isEmptyResult = computed(
  () => queuesState.value.kind === 'empty' && queueStore.queues.length === 0
);
const hasFilterMismatch = computed(
  () => queueStore.queues.length > 0 && queueStore.filteredQueues.length === 0
);

// ---------------------------------------------------------------------------
// Compact metric strip — every figure is a queueStore rollup of API values
// ---------------------------------------------------------------------------
const metrics = computed(() => [
  { key: 'queues', label: 'Queues', value: queueStore.totalQueues, tone: 'neutral' },
  {
    key: 'depth',
    label: 'Queued depth',
    value: queueStore.messagesInFlight,
    tone: queueStore.messagesInFlight > 50 ? 'warn' : 'neutral'
  },
  {
    key: 'consumers',
    label: 'Consumers',
    value: queueStore.totalConsumers,
    tone: queueStore.totalConsumers > 0 ? 'ok' : 'warn'
  },
  { key: 'producers', label: 'Producers', value: queueStore.totalProducers, tone: 'neutral' },
  {
    key: 'dlq',
    label: 'DLQ depth',
    value: queueStore.totalDlqDepth,
    tone: queueStore.totalDlqDepth > 0 ? 'bad' : 'neutral'
  }
]);

// ---------------------------------------------------------------------------
// Broker facts — only what the queues payload supplied
// ---------------------------------------------------------------------------
const brokerFactRows = computed(() => {
  const facts = queueStore.brokerFacts;
  return [
    { label: 'Broker', value: facts.brokerName },
    { label: 'Version', value: facts.brokerVersion },
    { label: 'Transport', value: facts.brokerUrl },
    { label: 'Addresses', value: facts.addressCount > 0 ? String(facts.addressCount) : null }
  ];
});

const searchQuery = computed({
  get: () => queueStore.searchQuery,
  set: (value: string) => queueStore.setSearchQuery(value)
});

const statusOptions = ['ALL', 'HEALTHY', 'DEGRADED', 'UNHEALTHY'];
</script>

<template>
  <div class="messages-view">
    <IrisPageHeader
      title="Messages"
      subtitle="Petasos queue and broker activity. Harmonia does not yet expose per-message observability, so no message list or payload is shown here."
    >
      <template #badges>
        <IrisStatus
          v-if="isPetasosStateReported"
          :status="petasosStatus"
          size="sm"
          label-format="upper"
        />
        <span v-if="queueStore.isStale" class="messages-view__badge messages-view__badge--stale">
          <Clock :size="12" aria-hidden="true" />
          <span>Stale telemetry</span>
        </span>
      </template>

      <template #actions>
        <!--
          Reserved page-level action area for the future message list / detail /
          replay capability. No replay, retry or resubmit control is rendered.
        -->
        <div
          class="messages-view__action-area"
          data-testid="messages-action-area"
          aria-hidden="true"
        ></div>
      </template>
    </IrisPageHeader>

    <p class="messages-view__scope" role="note">
      <Info :size="14" aria-hidden="true" />
      <span>
        This page reports Petasos queue depths, consumer and producer bindings and dead-letter
        depth. Message browsing, message detail and message replay are not available
        (IRIS-API-GAP-002).
      </span>
    </p>

    <ul class="messages-view__metrics">
      <li
        v-for="metric in metrics"
        :key="metric.key"
        class="messages-view__metric"
        :class="`messages-view__metric--${metric.tone}`"
      >
        <span class="messages-view__metric-label">{{ metric.label }}</span>
        <span class="messages-view__metric-value">{{ metric.value }}</span>
      </li>
    </ul>

    <IrisToolbar
      v-model:searchQuery="searchQuery"
      :show-search="true"
      search-placeholder="Filter queues by name, address or capability..."
      :show-refresh="true"
      :refreshing="queueStore.loading || queueStore.refreshing"
      @refresh="refreshAll"
    >
      <template #filter>
        <div class="messages-view__filter" role="group" aria-label="Filter queues by status">
          <button
            v-for="status in statusOptions"
            :key="status"
            type="button"
            class="messages-view__filter-btn"
            :class="{ 'messages-view__filter-btn--active': queueStore.statusFilter === status }"
            :aria-pressed="queueStore.statusFilter === status"
            @click="queueStore.setStatusFilter(status)"
          >
            {{ status }}
          </button>
        </div>
      </template>
    </IrisToolbar>

    <IrisSection
      title="Broker"
      description="Broker facts are shown only where the queues payload supplies them."
    >
      <ul class="messages-view__facts">
        <li v-for="fact in brokerFactRows" :key="fact.label" class="messages-view__fact">
          <span class="messages-view__fact-label">{{ fact.label }}</span>
          <span v-if="fact.value" class="messages-view__fact-value">{{ fact.value }}</span>
          <span v-else class="messages-view__not-reported">{{ NOT_REPORTED }}</span>
        </li>
      </ul>
    </IrisSection>

    <IrisSection
      title="Queue activity"
      description="Petasos queues and addresses as reported by the operations API."
    >
      <IrisErrorState
        v-if="isUnavailable"
        title="Operations API unavailable"
        :message="failureMessage"
        :retryable="true"
        retry-label="Retry"
        @retry="refreshAll"
      />
      <IrisLoadingState
        v-else-if="isLoadingFirstResult"
        size="sm"
        message="Loading Petasos queue activity..."
      />
      <template v-else>
        <div v-if="isPartial" class="messages-view__partial" role="status">
          {{ failureMessage }}
        </div>

        <IrisEmptyState
          v-if="isEmptyResult"
          title="No Petasos queues present"
          description="The operations API responded successfully and reported no queues."
        />
        <IrisEmptyState
          v-else-if="hasFilterMismatch"
          title="No queue matches the current filter"
          description="Clear the search text or select the ALL status filter to see every reported queue."
        />
        <QueueTable
          v-else
          :queues="queueStore.filteredQueues"
          :loading="queueStore.loading"
          @select="queueStore.selectQueue($event.queueId)"
        />
      </template>
    </IrisSection>

    <QueueDetailDrawer
      :queue="queueStore.selectedQueue"
      :is-open="queueStore.isDrawerOpen"
      :teleport="true"
      @close="queueStore.closeDrawer()"
    />
  </div>
</template>

<style scoped>
.messages-view {
  display: flex;
  flex-direction: column;
  gap: 0.875rem;
  width: 100%;
  font-family: var(--iris-font-sans);
}

.messages-view__badge {
  display: inline-flex;
  align-items: center;
  gap: 0.25rem;
  padding: 2px 8px;
  border-radius: var(--iris-border-radius);
  font-size: 0.6875rem;
  font-weight: 600;
}

.messages-view__badge--stale {
  border: 1px solid var(--iris-status-stale-border);
  background-color: var(--iris-status-stale-bg);
  color: var(--iris-status-stale-text);
}

.messages-view__action-area {
  min-width: 0;
}

.messages-view__scope {
  display: flex;
  align-items: flex-start;
  gap: 0.5rem;
  margin: 0;
  padding: 0.5rem 0.75rem;
  font-size: 0.75rem;
  line-height: 1.45;
  color: var(--iris-text-secondary);
  background-color: var(--iris-bg-subtle);
  border: 1px solid var(--iris-border-default);
  border-radius: var(--iris-border-radius);
}

.messages-view__scope svg {
  flex-shrink: 0;
  margin-top: 1px;
}

.messages-view__metrics {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(130px, 1fr));
  gap: 0.375rem;
  margin: 0;
  padding: 0;
  list-style: none;
}

.messages-view__metric {
  display: flex;
  flex-direction: column;
  gap: 0.125rem;
  padding: 0.375rem 0.625rem;
  border: 1px solid var(--iris-border-default);
  border-radius: var(--iris-border-radius);
  background-color: var(--iris-bg-surface);
}

.messages-view__metric-label {
  font-size: 0.6875rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.04em;
  color: var(--iris-text-muted);
}

.messages-view__metric-value {
  font-family: var(--iris-font-mono);
  font-size: 1.125rem;
  font-weight: 700;
  color: var(--iris-text-primary);
}

.messages-view__metric--ok .messages-view__metric-value {
  color: var(--iris-status-healthy-text);
}

.messages-view__metric--warn .messages-view__metric-value {
  color: var(--iris-status-degraded-text);
}

.messages-view__metric--bad .messages-view__metric-value {
  color: var(--iris-status-unavailable-text);
}

.messages-view__filter {
  display: inline-flex;
  flex-wrap: wrap;
}

.messages-view__filter-btn {
  padding: 0.25rem 0.625rem;
  font-family: inherit;
  font-size: 0.75rem;
  font-weight: 600;
  color: var(--iris-text-secondary);
  background-color: var(--iris-bg-surface);
  border: 1px solid var(--iris-border-default);
  border-right-width: 0;
  cursor: pointer;
}

.messages-view__filter-btn:first-child {
  border-top-left-radius: var(--iris-border-radius);
  border-bottom-left-radius: var(--iris-border-radius);
}

.messages-view__filter-btn:last-child {
  border-right-width: 1px;
  border-top-right-radius: var(--iris-border-radius);
  border-bottom-right-radius: var(--iris-border-radius);
}

.messages-view__filter-btn:hover {
  background-color: var(--iris-bg-hover);
}

.messages-view__filter-btn--active {
  background-color: var(--iris-bg-selected);
  color: var(--iris-text-accent);
}

.messages-view__facts {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(190px, 1fr));
  gap: 0.375rem;
  margin: 0;
  padding: 0;
  list-style: none;
}

.messages-view__fact {
  display: flex;
  flex-direction: column;
  gap: 0.1875rem;
  padding: 0.375rem 0.5rem;
  border: 1px solid var(--iris-border-default);
  border-radius: var(--iris-border-radius);
  background-color: var(--iris-bg-subtle);
}

.messages-view__fact-label {
  font-size: 0.6875rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.04em;
  color: var(--iris-text-muted);
}

.messages-view__fact-value {
  font-family: var(--iris-font-mono);
  font-size: 0.8125rem;
  font-weight: 600;
  color: var(--iris-text-primary);
  word-break: break-all;
}

.messages-view__not-reported {
  font-size: 0.75rem;
  font-style: italic;
  color: var(--iris-text-muted);
}

.messages-view__partial {
  margin-bottom: 0.625rem;
  padding: 0.5rem 0.75rem;
  font-size: 0.8125rem;
  background-color: var(--iris-status-degraded-bg);
  border: 1px solid var(--iris-status-degraded-border);
  border-radius: var(--iris-border-radius);
  color: var(--iris-status-degraded-text);
}

@media (max-width: 900px) {
  .messages-view__metrics {
    grid-template-columns: repeat(auto-fit, minmax(110px, 1fr));
  }
}
</style>
