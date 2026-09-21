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
import { ref, computed, onMounted } from 'vue';
import { ArrowDownLeft, ArrowUpRight, ChevronRight, FileCog, Info } from 'lucide-vue-next';
import {
  IrisPageHeader,
  IrisToolbar,
  IrisSection,
  IrisStatus,
  IrisDataTable,
  IrisEmptyState,
  IrisLoadingState,
  IrisErrorState,
  type DataTableColumn
} from '@harmonia/iris-befe';
import { useOperationsStore } from '../stores/operationsStore';
import { useInterfacesStore } from '../stores/interfacesStore';
import { isPending } from '../models/loadState';
import InterfaceDetailDrawer, { type PylaiGateway } from '../components/interfaces/InterfaceDetailDrawer.vue';

/**
 * Interfaces perspective.
 *
 * The page keeps two bodies of knowledge strictly apart:
 *   - the CONFIGURED gateway inventory, declared in the deployment; and
 *   - the OBSERVED Pylai runtime facts the operations API actually returned.
 *
 * Nothing on this page is derived by mixing the two, and no runtime-sounding
 * value is ever synthesised from configuration.
 */
const operationsStore = useOperationsStore();
const interfacesStore = useInterfacesStore();

const selectedGateway = ref<PylaiGateway | null>(null);
const isDrawerOpen = ref(false);

const NOT_MEASURED = 'Not measured';

onMounted(() => {
  refreshAll();
});

async function refreshAll() {
  await Promise.allSettled([
    operationsStore.fetchSubsystems(),
    interfacesStore.fetchRuntime()
  ]);
}

// ---------------------------------------------------------------------------
// Configured inventory (provenance: CONFIGURED)
// ---------------------------------------------------------------------------
const provenanceNotice = interfacesStore.provenanceNotice;
const configuredCount = computed(() => interfacesStore.configuredCount);
const inboundCount = computed(() => interfacesStore.inboundCount);
const outboundCount = computed(() => interfacesStore.outboundCount);
const filteredInterfaces = computed<PylaiGateway[]>(() => interfacesStore.filteredInterfaces);

const searchQuery = computed({
  get: () => interfacesStore.searchQuery,
  set: (value: string) => interfacesStore.setSearchQuery(value)
});

const directionFilter = computed(() => interfacesStore.directionFilter);

const directionOptions: { value: 'ALL' | 'INBOUND' | 'OUTBOUND'; label: string }[] = [
  { value: 'ALL', label: 'All' },
  { value: 'INBOUND', label: 'Inbound' },
  { value: 'OUTBOUND', label: 'Outbound' }
];

function directionCount(value: 'ALL' | 'INBOUND' | 'OUTBOUND'): number {
  if (value === 'INBOUND') return inboundCount.value;
  if (value === 'OUTBOUND') return outboundCount.value;
  return configuredCount.value;
}

/**
 * Columns are limited to facts the configuration genuinely declares. There is
 * no status, throughput or error column: the platform does not measure those
 * per interface.
 */
const tableColumns: DataTableColumn[] = [
  { field: 'name', header: 'Interface' },
  { field: 'direction', header: 'Direction', width: '130px' },
  { field: 'protocol', header: 'Protocol', width: '170px' },
  { field: 'port', header: 'Port', width: '100px' },
  { field: 'targetQueue', header: 'Target queue' },
  { field: 'actions', header: 'Detail', width: '110px', align: 'right' }
];

function openGatewayDrawer(gateway: PylaiGateway) {
  selectedGateway.value = gateway;
  isDrawerOpen.value = true;
}

function closeGatewayDrawer() {
  isDrawerOpen.value = false;
  selectedGateway.value = null;
}

// ---------------------------------------------------------------------------
// Observed runtime (provenance: OBSERVED) — Pylai instances and health only
// ---------------------------------------------------------------------------
const runtimeState = computed(() => interfacesStore.runtimeState);
const instances = computed(() => interfacesStore.instances);
const health = computed(() => interfacesStore.health);

const runtimeFailureMessage = computed(() => {
  const state = runtimeState.value;
  return 'message' in state ? state.message : '';
});

const isRuntimeLoading = computed(() => isPending(runtimeState.value) && instances.value.length === 0 && !health.value);
const isRuntimeEmpty = computed(() => instances.value.length === 0 && !health.value);

const pylaiSubsystem = computed(() => operationsStore.subsystems.find(s => s.id === 'pylai') || null);

/**
 * The only honest gateway state is the one Pylai health actually reported.
 * When it did not report, the page says so rather than defaulting to healthy.
 */
const observedPylaiStatus = computed<string>(() => {
  if (health.value?.status) return String(health.value.status);
  if (pylaiSubsystem.value?.state) return String(pylaiSubsystem.value.state);
  return 'UNKNOWN';
});

const isPylaiStateMeasured = computed(() => Boolean(health.value?.status || pylaiSubsystem.value?.state));

function measured(value: number | null | undefined, suffix = ''): string {
  return value == null ? NOT_MEASURED : `${value}${suffix}`;
}

const healthFacts = computed(() => [
  { label: 'Availability', value: measured(health.value?.availabilityPercent, '%') },
  { label: 'Failed operations', value: measured(health.value?.failedOperations) },
  { label: 'Restart count', value: measured(health.value?.restartCount) },
  { label: 'P95 latency', value: measured(health.value?.p95LatencyMs, ' ms') },
  { label: 'Dependencies', value: health.value?.dependenciesSummary || NOT_MEASURED }
]);

function instanceStarted(startedAt?: number): string {
  return startedAt ? new Date(startedAt).toLocaleString() : NOT_MEASURED;
}
</script>

<template>
  <div class="interfaces-view">
    <IrisPageHeader
      title="Interfaces"
      subtitle="Configured Pylai gateway inventory, shown separately from the Pylai runtime facts the operations API actually reports."
    >
      <template #badges>
        <span class="interfaces-view__badge interfaces-view__badge--configured">
          <FileCog :size="12" aria-hidden="true" />
          <span>Configured inventory</span>
        </span>
        <IrisStatus
          v-if="isPylaiStateMeasured"
          :status="observedPylaiStatus"
          size="sm"
          label-format="upper"
        />
      </template>
    </IrisPageHeader>

    <IrisToolbar
      v-model:searchQuery="searchQuery"
      :show-search="true"
      search-placeholder="Filter interfaces by name, protocol, port or queue..."
      :show-refresh="true"
      :refreshing="operationsStore.refreshing || isPending(runtimeState)"
      @refresh="refreshAll"
    >
      <template #filter>
        <div class="interfaces-view__filter" role="group" aria-label="Filter by interface direction">
          <button
            v-for="option in directionOptions"
            :key="option.value"
            type="button"
            class="interfaces-view__filter-btn"
            :class="{ 'interfaces-view__filter-btn--active': directionFilter === option.value }"
            :aria-pressed="directionFilter === option.value"
            @click="interfacesStore.setDirectionFilter(option.value)"
          >
            {{ option.label }} ({{ directionCount(option.value) }})
          </button>
        </div>
      </template>
    </IrisToolbar>

    <!-- Configured inventory -->
    <IrisSection
      title="Configured gateway inventory"
      description="Declared in the Harmonia deployment configuration. Every column below is a configured fact."
    >
      <p class="interfaces-view__provenance" role="note">
        <Info :size="14" aria-hidden="true" />
        <span>{{ provenanceNotice }}</span>
      </p>

      <IrisDataTable
        :value="filteredInterfaces"
        :columns="tableColumns"
        data-key="id"
        empty-message="No configured interface matches the current search or direction filter."
        @row-click="openGatewayDrawer($event.data)"
      >
        <template #name="{ data }">
          <div class="interfaces-view__name">
            <span class="interfaces-view__name-primary">{{ data.name }}</span>
            <span class="interfaces-view__name-secondary">{{ data.englishTitle }}</span>
          </div>
        </template>

        <template #direction="{ data }">
          <span
            class="interfaces-view__direction"
            :class="data.direction === 'INBOUND'
              ? 'interfaces-view__direction--inbound'
              : 'interfaces-view__direction--outbound'"
          >
            <ArrowDownLeft v-if="data.direction === 'INBOUND'" :size="11" aria-hidden="true" />
            <ArrowUpRight v-else :size="11" aria-hidden="true" />
            <span>{{ data.direction }}</span>
          </span>
        </template>

        <template #protocol="{ data }">
          <span class="interfaces-view__mono">{{ data.protocol }}</span>
        </template>

        <template #port="{ data }">
          <span class="interfaces-view__mono interfaces-view__mono--strong">:{{ data.port }}</span>
        </template>

        <template #targetQueue="{ data }">
          <span class="interfaces-view__mono interfaces-view__queue">{{ data.targetQueue }}</span>
        </template>

        <template #actions="{ data }">
          <button
            type="button"
            class="iris-inspect-btn interfaces-view__inspect"
            :aria-label="`Inspect ${data.name}`"
            @click.stop="openGatewayDrawer(data)"
          >
            <span>Inspect</span>
            <ChevronRight :size="12" aria-hidden="true" />
          </button>
        </template>
      </IrisDataTable>
    </IrisSection>

    <!-- Observed runtime -->
    <IrisSection
      title="Observed Pylai runtime"
      description="Only what the operations API measured for the Pylai subsystem. Nothing here is derived from configuration."
    >
      <IrisErrorState
        v-if="runtimeState.kind === 'unavailable'"
        title="Pylai runtime observation unavailable"
        :message="runtimeFailureMessage"
        :retryable="true"
        retry-label="Retry"
        @retry="refreshAll"
      />
      <IrisLoadingState v-else-if="isRuntimeLoading" size="sm" message="Loading Pylai runtime state..." />
      <template v-else>
        <div v-if="runtimeState.kind === 'partial'" class="interfaces-view__partial" role="status">
          {{ runtimeFailureMessage }}
        </div>

        <IrisEmptyState
          v-if="isRuntimeEmpty"
          title="No Pylai runtime state reported"
          description="The operations API responded successfully but reported no Pylai instances or health record."
        />
        <template v-else>
          <ul class="interfaces-view__facts">
            <li class="interfaces-view__fact">
              <span class="interfaces-view__fact-label">Reported state</span>
              <IrisStatus
                v-if="health?.status"
                :status="String(health.status)"
                size="sm"
                label-format="upper"
              />
              <span v-else class="interfaces-view__not-measured">{{ NOT_MEASURED }}</span>
            </li>
            <li v-for="fact in healthFacts" :key="fact.label" class="interfaces-view__fact">
              <span class="interfaces-view__fact-label">{{ fact.label }}</span>
              <span
                class="interfaces-view__fact-value"
                :class="{ 'interfaces-view__not-measured': fact.value === NOT_MEASURED }"
              >
                {{ fact.value }}
              </span>
            </li>
          </ul>

          <h3 class="interfaces-view__subheading">Pylai instances</h3>
          <p v-if="instances.length === 0" class="interfaces-view__not-measured">
            The operations API reported no Pylai instances.
          </p>
          <ul v-else class="interfaces-view__instances">
            <li v-for="instance in instances" :key="instance.instanceId" class="interfaces-view__instance">
              <span class="interfaces-view__mono interfaces-view__mono--strong">{{ instance.instanceId }}</span>
              <IrisStatus :status="instance.state" size="sm" label-format="upper" />
              <span class="interfaces-view__instance-meta">Started: {{ instanceStarted(instance.startedAt) }}</span>
            </li>
          </ul>
        </template>
      </template>

      <p class="interfaces-view__gap" role="note">
        Per-interface throughput, connection counts and error rates are not measured by any Harmonia API
        (IRIS-API-GAP-001). They are omitted rather than estimated.
      </p>
    </IrisSection>

    <InterfaceDetailDrawer
      :gateway="selectedGateway"
      :is-open="isDrawerOpen"
      @close="closeGatewayDrawer"
    />
  </div>
</template>

<style scoped>
.interfaces-view {
  display: flex;
  flex-direction: column;
  gap: 0.875rem;
  width: 100%;
  font-family: var(--iris-font-sans);
}

.interfaces-view__badge {
  display: inline-flex;
  align-items: center;
  gap: 0.25rem;
  padding: 2px 8px;
  border-radius: var(--iris-border-radius);
  font-size: 0.6875rem;
  font-weight: 600;
}

.interfaces-view__badge--configured {
  border: 1px solid var(--iris-status-idle-border);
  background-color: var(--iris-status-idle-bg);
  color: var(--iris-status-idle-text);
}

.interfaces-view__filter {
  display: inline-flex;
}

.interfaces-view__filter-btn {
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

.interfaces-view__filter-btn:first-child {
  border-top-left-radius: var(--iris-border-radius);
  border-bottom-left-radius: var(--iris-border-radius);
}

.interfaces-view__filter-btn:last-child {
  border-right-width: 1px;
  border-top-right-radius: var(--iris-border-radius);
  border-bottom-right-radius: var(--iris-border-radius);
}

.interfaces-view__filter-btn:hover {
  background-color: var(--iris-bg-hover);
}

.interfaces-view__filter-btn--active {
  background-color: var(--iris-bg-selected);
  color: var(--iris-text-accent);
}

.interfaces-view__provenance {
  display: flex;
  align-items: flex-start;
  gap: 0.5rem;
  margin: 0 0 0.625rem 0;
  padding: 0.5rem 0.75rem;
  font-size: 0.75rem;
  line-height: 1.45;
  color: var(--iris-text-secondary);
  background-color: var(--iris-bg-subtle);
  border: 1px solid var(--iris-border-default);
  border-radius: var(--iris-border-radius);
}

.interfaces-view__provenance svg {
  flex-shrink: 0;
  margin-top: 1px;
}

.interfaces-view__name {
  display: flex;
  flex-direction: column;
  min-width: 0;
}

.interfaces-view__name-primary {
  font-weight: 600;
  color: var(--iris-text-primary);
}

.interfaces-view__name-secondary {
  font-size: 0.6875rem;
  color: var(--iris-text-muted);
}

.interfaces-view__direction {
  display: inline-flex;
  align-items: center;
  gap: 0.25rem;
  padding: 1px 6px;
  border-radius: var(--iris-border-radius);
  font-family: var(--iris-font-mono);
  font-size: 0.6875rem;
  font-weight: 700;
  letter-spacing: 0.03em;
  border: 1px solid var(--iris-border-default);
  background-color: var(--iris-bg-subtle);
  color: var(--iris-text-secondary);
}

.interfaces-view__direction--inbound {
  background-color: var(--iris-bg-selected);
  color: var(--iris-text-accent);
}

.interfaces-view__mono {
  font-family: var(--iris-font-mono);
  font-size: 0.75rem;
  color: var(--iris-text-secondary);
}

.interfaces-view__mono--strong {
  font-weight: 700;
  color: var(--iris-text-primary);
}

.interfaces-view__queue {
  word-break: break-all;
}

.interfaces-view__inspect {
  display: inline-flex;
  align-items: center;
  gap: 0.25rem;
  padding: 0.1875rem 0.5rem;
  font-family: inherit;
  font-size: 0.75rem;
  font-weight: 600;
  color: var(--iris-text-accent);
  background-color: var(--iris-bg-surface);
  border: 1px solid var(--iris-border-default);
  border-radius: var(--iris-border-radius);
  cursor: pointer;
}

.interfaces-view__inspect:hover {
  background-color: var(--iris-bg-hover);
}

.interfaces-view__partial {
  margin-bottom: 0.625rem;
  padding: 0.5rem 0.75rem;
  font-size: 0.8125rem;
  background-color: var(--iris-status-degraded-bg);
  border: 1px solid var(--iris-status-degraded-border);
  border-radius: var(--iris-border-radius);
  color: var(--iris-status-degraded-text);
}

.interfaces-view__facts {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 0.375rem;
  margin: 0;
  padding: 0;
  list-style: none;
}

.interfaces-view__fact {
  display: flex;
  flex-direction: column;
  gap: 0.1875rem;
  padding: 0.375rem 0.5rem;
  border: 1px solid var(--iris-border-default);
  border-radius: var(--iris-border-radius);
  background-color: var(--iris-bg-subtle);
}

.interfaces-view__fact-label {
  font-size: 0.6875rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.04em;
  color: var(--iris-text-muted);
}

.interfaces-view__fact-value {
  font-family: var(--iris-font-mono);
  font-size: 0.8125rem;
  font-weight: 600;
  color: var(--iris-text-primary);
}

.interfaces-view__not-measured {
  font-style: italic;
  font-weight: 500;
  color: var(--iris-text-muted);
}

.interfaces-view__subheading {
  margin: 0.875rem 0 0.375rem 0;
  font-size: 0.75rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.04em;
  color: var(--iris-text-secondary);
}

.interfaces-view__instances {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
  margin: 0;
  padding: 0;
  list-style: none;
}

.interfaces-view__instance {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 0.5rem;
  padding: 0.3125rem 0.5rem;
  border: 1px solid var(--iris-border-default);
  border-radius: var(--iris-border-radius);
  font-size: 0.8125rem;
}

.interfaces-view__instance-meta {
  font-size: 0.6875rem;
  color: var(--iris-text-muted);
}

.interfaces-view__gap {
  margin: 0.875rem 0 0 0;
  font-size: 0.75rem;
  line-height: 1.45;
  color: var(--iris-text-muted);
}

@media (max-width: 900px) {
  .interfaces-view__filter {
    flex-wrap: wrap;
  }
}
</style>
