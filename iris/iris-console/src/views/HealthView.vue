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
import { ref, computed, onMounted, onUnmounted } from 'vue';
import { useRouter } from 'vue-router';
import { 
  HeartPulse, 
  Activity, 
  Server, 
  ShieldCheck, 
  Database, 
  Radio, 
  GitMerge, 
  Monitor, 
  MessageSquare,
  Search,
  RotateCw,
  ExternalLink,
  ArrowRight,
  Clock,
  Layers,
  AlertTriangle,
  CheckCircle2,
  XCircle,
  HelpCircle,
  X,
  Network
} from 'lucide-vue-next';
import { useOperationsStore } from '../stores/operationsStore';
import { operationsApi } from '../api/operationsClient';
import type { OperationalHealth, DependencyHealth, SubsystemState } from '../models/operations';
import { IrisToolbar, IrisDataTable, IrisStatus, type DataTableColumn } from '@harmonia/iris-befe';

const router = useRouter();
const operationsStore = useOperationsStore();

const searchQuery = ref('');
const statusFilter = ref<'ALL' | 'HEALTHY' | 'DEGRADED' | 'UNAVAILABLE'>('ALL');
const areaFilter = ref<string>('ALL');
const refreshing = ref(false);
const lastRefreshed = ref<Date | null>(null);

// Map of probe telemetry by subsystem id
const healthProbes = ref<Record<string, OperationalHealth>>({});

// Drawer inspection for dependencies
const selectedSubsystemForDependencies = ref<SubsystemHealthRow | null>(null);
const isDependenciesDrawerOpen = ref(false);

interface SubsystemDefinition {
  id: string;
  name: string;
  englishTitle: string;
  areaId: string;
  areaName: string;
  description: string;
  inspectRoute: string;
  probeIds: string[];
  components: string[];
}

export interface SubsystemHealthRow {
  id: string;
  name: string;
  englishTitle: string;
  areaId: string;
  areaName: string;
  description: string;
  status: string;
  availabilityPercent: number | null;
  p95LatencyMs: number | null;
  failedOperations: number;
  restartCount: number;
  dependenciesSummary: string;
  dependencies: DependencyHealth[];
  inspectRoute: string;
  isStale?: boolean;
  components: string[];
  note?: string;
}

// 9 Authoritative Harmonia Subprojects
const CANONICAL_SUBSYSTEMS: SubsystemDefinition[] = [
  {
    id: 'themis',
    name: 'Themis',
    englishTitle: 'Security & Policy Enforcement',
    areaId: 'security-policy',
    areaName: 'Security & Policy',
    description: 'Default-Deny Policy Evaluation & Role-to-Authority RBAC Engine',
    inspectRoute: '/subsystems/themis',
    probeIds: ['themis'],
    components: ['Themis Engine', 'RBAC/ABAC Evaluator', 'Non-PHI Security Audit']
  },
  {
    id: 'calliope',
    name: 'Calliope',
    englishTitle: 'Canonical Models & Schemas',
    areaId: 'information-state',
    areaName: 'Information & State',
    description: 'Canonical Schemas, Transformers & Clinical HL7/FHIR Models',
    inspectRoute: '/subsystems/calliope',
    probeIds: ['calliope'],
    components: ['Canonical Schemas', 'HL7/FHIR Converters', 'Topic Definitions']
  },
  {
    id: 'hestia',
    name: 'Hestia',
    englishTitle: 'Distributed Caching & Relational Persistence',
    areaId: 'information-state',
    areaName: 'Information & State',
    description: 'Infinispan Distributed Replicated Cache Grid & PostgreSQL HAPI FHIR JPA',
    inspectRoute: '/subsystems/mnemosyne',
    probeIds: ['mneme', 'mnemosyne', 'hestia'],
    components: ['Mneme (Cache Grid)', 'Mnemosyne (JPA Persistence)']
  },
  {
    id: 'petasos',
    name: 'Petasos',
    englishTitle: 'Messaging & Transport',
    areaId: 'integration-transport',
    areaName: 'Integration & Transport',
    description: 'Resilient Messaging Abstraction & ActiveMQ Artemis Broker',
    inspectRoute: '/subsystems/petasos',
    probeIds: ['petasos'],
    components: ['Petasos API', 'ActiveMQ Artemis Cluster', 'Dead Letter Queue']
  },
  {
    id: 'energeia',
    name: 'Energeia',
    englishTitle: 'Workflow & Activity Execution',
    areaId: 'execution-processing',
    areaName: 'Execution & Processing',
    description: 'Task Processing (Ponos), Ergon Activity Units & Praxis Workflow Orchestration',
    inspectRoute: '/subsystems/energeia',
    probeIds: ['energeia'],
    components: ['Ponos Worker Pool', 'Praxis Sequences', 'Ergon Activities', 'Pragma Envelopes']
  },
  {
    id: 'pylai',
    name: 'Pylai',
    englishTitle: 'Interface Gateways',
    areaId: 'integration-transport',
    areaName: 'Integration & Transport',
    description: 'Inbound & Outbound HL7 v2 MLLP and FHIR REST Protocol Gateways',
    inspectRoute: '/subsystems/pylai',
    probeIds: ['pylai'],
    components: ['MLLP Inbound (:2575)', 'MLLP Outbound (:8087/:8088)', 'FHIR Registry (:8089)']
  },
  {
    id: 'iris',
    name: 'Iris',
    englishTitle: 'Presentation Services',
    areaId: 'presentation',
    areaName: 'Presentation',
    description: 'Presentation Tier, WildFly BEFE REST Gateway & Vue 3 Operations Workbenches',
    inspectRoute: '/subsystems/iris',
    probeIds: ['iris'],
    components: ['Iris BEFE Gateway (:8090)', 'Iris Monitor SPA', 'Iris Clinical SPA', 'Iris Admin SPA']
  },
  {
    id: 'agora',
    name: 'Agora',
    englishTitle: 'Collaboration & Matrix Gateway',
    areaId: 'collaboration',
    areaName: 'Collaboration',
    description: 'Matrix/Synapse Collaboration & Healthcare Application Service Bridge',
    inspectRoute: '/subsystems/agora',
    probeIds: ['agora'],
    components: ['Synapse Homeserver (:8008)', 'Agora AS Bridge (:8095)', 'Themis Room Governance']
  },
  {
    id: 'paradeigma',
    name: 'Paradeigma',
    englishTitle: 'Synthetic Clinical Simulation',
    areaId: 'simulation-validation',
    areaName: 'Simulation & Validation',
    description: 'Synthetic Clinical Simulators (EMR, LMS, PAS, RIS-PACS) — Invariant 1 Leaf Isolation',
    inspectRoute: '/subsystems/paradeigma',
    probeIds: ['paradeigma'],
    components: ['Synthetic EMR', 'Synthetic PAS/LMS', 'Scenario Engine (Leaf Isolation)']
  }
];

const areaIcons: Record<string, any> = {
  'security-policy': ShieldCheck,
  'information-state': Database,
  'integration-transport': Radio,
  'execution-processing': GitMerge,
  'presentation': Monitor,
  'collaboration': MessageSquare,
  'simulation-validation': Layers
};

// Table Column Definitions
const tableColumns: DataTableColumn[] = [
  { field: 'subsystem', header: 'Subsystem & Area', sortable: true },
  { field: 'status', header: 'Status', width: '135px', sortable: true },
  { field: 'availability', header: 'Availability SLA', width: '135px', sortable: true, align: 'right' },
  { field: 'p95Latency', header: 'P95 Latency', width: '125px', sortable: true, align: 'right' },
  { field: 'operations', header: 'Restarts / Failed', width: '145px', align: 'center' },
  { field: 'dependencies', header: 'Dependencies', width: '180px' },
  { field: 'actions', header: 'Actions', width: '120px', align: 'right' }
];

// Fetch all health probes in parallel with Promise.allSettled
const fetchAllHealth = async () => {
  refreshing.value = true;
  try {
    await operationsStore.fetchSubsystems();

    const probeTargetIds = [
      'themis', 'calliope', 'mneme', 'mnemosyne', 'hestia',
      'petasos', 'energeia', 'pylai', 'iris', 'agora', 'paradeigma'
    ];

    const results = await Promise.allSettled(
      probeTargetIds.map(async (id) => {
        const data = await operationsApi.getSubsystemHealth(id);
        return { id, data };
      })
    );

    const newMap: Record<string, OperationalHealth> = {};
    for (const r of results) {
      if (r.status === 'fulfilled') {
        newMap[r.value.id] = r.value.data;
      }
    }
    healthProbes.value = newMap;
    lastRefreshed.value = new Date();
  } catch (err) {
    console.warn('Error fetching architecture health:', err);
  } finally {
    refreshing.value = false;
  }
};

onMounted(() => {
  fetchAllHealth();
  window.addEventListener('keydown', handleKeydown);
});

onUnmounted(() => {
  window.removeEventListener('keydown', handleKeydown);
});

const handleKeydown = (e: KeyboardEvent) => {
  if (e.key === 'Escape' && isDependenciesDrawerOpen.value) {
    closeDependenciesDrawer();
  }
};

// Synthesize rows for all 9 canonical subsystems
const matrixRows = computed<SubsystemHealthRow[]>(() => {
  return CANONICAL_SUBSYSTEMS.map(def => {
    // Determine live status from store
    const liveSub = operationsStore.subsystems.find(s => s.id.toLowerCase() === def.id.toLowerCase());
    
    // Resolve health probe data
    let status: string = liveSub?.state || 'UNKNOWN';
    let availabilityPercent: number | null = null;
    let p95LatencyMs: number | null = null;
    let failedOperations = 0;
    let restartCount = 0;
    let dependencies: DependencyHealth[] = [];
    let dependenciesSummary = 'None';
    let isStale = liveSub?.stale || false;
    let note: string | undefined = undefined;

    if (def.id === 'hestia') {
      // Hestia aggregates Mneme and Mnemosyne
      const mnemeHealth = healthProbes.value['mneme'];
      const mnemosyneHealth = healthProbes.value['mnemosyne'];
      const hestiaDirect = healthProbes.value['hestia'];

      if (hestiaDirect && hestiaDirect.status && hestiaDirect.status !== 'UNKNOWN') {
        status = hestiaDirect.status;
        availabilityPercent = hestiaDirect.availabilityPercent ?? null;
        p95LatencyMs = hestiaDirect.p95LatencyMs ?? null;
        failedOperations = hestiaDirect.failedOperations ?? 0;
        restartCount = hestiaDirect.restartCount ?? 0;
        dependencies = hestiaDirect.dependencies || [];
        dependenciesSummary = hestiaDirect.dependenciesSummary || `${dependencies.length} Connected`;
      } else {
        const statuses = [mnemeHealth?.status, mnemosyneHealth?.status].filter(Boolean);
        if (statuses.includes('UNAVAILABLE')) {
          status = 'UNAVAILABLE';
        } else if (statuses.includes('DEGRADED')) {
          status = 'DEGRADED';
        } else if (statuses.includes('HEALTHY')) {
          status = 'HEALTHY';
        } else {
          status = liveSub?.state || 'UNKNOWN';
        }

        const availabilities = [mnemeHealth?.availabilityPercent, mnemosyneHealth?.availabilityPercent]
          .filter((v): v is number => v != null && !isNaN(v));
        availabilityPercent = availabilities.length > 0 
          ? Number((availabilities.reduce((a, b) => a + b, 0) / availabilities.length).toFixed(2))
          : null;

        const latencies = [mnemeHealth?.p95LatencyMs, mnemosyneHealth?.p95LatencyMs]
          .filter((v): v is number => v != null && !isNaN(v));
        p95LatencyMs = latencies.length > 0 ? Math.max(...latencies) : null;

        failedOperations = (mnemeHealth?.failedOperations ?? 0) + (mnemosyneHealth?.failedOperations ?? 0);
        restartCount = (mnemeHealth?.restartCount ?? 0) + (mnemosyneHealth?.restartCount ?? 0);

        const depMap = new Map<string, DependencyHealth>();
        for (const dep of [...(mnemeHealth?.dependencies || []), ...(mnemosyneHealth?.dependencies || [])]) {
          depMap.set(dep.name, dep);
        }
        dependencies = Array.from(depMap.values());
        dependenciesSummary = `${dependencies.length} Connected`;
      }
    } else if (def.id === 'paradeigma') {
      // Invariant 1: Paradeigma is leaf simulation only (strictly isolated from production)
      const direct = healthProbes.value['paradeigma'];
      if (direct && direct.status && direct.status !== 'UNKNOWN') {
        status = direct.status;
        availabilityPercent = direct.availabilityPercent ?? null;
        p95LatencyMs = direct.p95LatencyMs ?? null;
        failedOperations = direct.failedOperations ?? 0;
        restartCount = direct.restartCount ?? 0;
        dependencies = direct.dependencies || [];
        dependenciesSummary = direct.dependenciesSummary || 'Production Isolation (Leaf)';
      } else {
        status = 'UNKNOWN';
        availabilityPercent = null;
        p95LatencyMs = null;
        failedOperations = 0;
        restartCount = 0;
        dependenciesSummary = 'Production Isolation (Leaf)';
        note = 'Invariant 1: Isolated simulation test environment';
      }
    } else {
      const probe = healthProbes.value[def.id];
      if (probe) {
        if (probe.status && probe.status !== 'UNKNOWN') {
          status = probe.status;
        }
        availabilityPercent = probe.availabilityPercent ?? null;
        p95LatencyMs = probe.p95LatencyMs ?? null;
        failedOperations = probe.failedOperations ?? 0;
        restartCount = probe.restartCount ?? 0;
        dependencies = probe.dependencies || [];
        dependenciesSummary = probe.dependenciesSummary || (dependencies.length > 0 ? `${dependencies.length} Connected` : 'None');
        isStale = isStale || Boolean(probe.stale);
      } else if (liveSub) {
        status = liveSub.state;
      }
    }

    return {
      id: def.id,
      name: def.name,
      englishTitle: def.englishTitle,
      areaId: def.areaId,
      areaName: def.areaName,
      description: def.description,
      status,
      availabilityPercent,
      p95LatencyMs,
      failedOperations,
      restartCount,
      dependenciesSummary,
      dependencies,
      inspectRoute: def.inspectRoute,
      isStale,
      components: def.components,
      note
    };
  });
});

// Filtered rows based on search, status, and area
const filteredRows = computed(() => {
  return matrixRows.value.filter(row => {
    // Status filter
    if (statusFilter.value !== 'ALL' && row.status !== statusFilter.value) {
      return false;
    }

    // Area filter
    if (areaFilter.value !== 'ALL' && row.areaId !== areaFilter.value) {
      return false;
    }

    // Search query filter
    if (searchQuery.value.trim()) {
      const q = searchQuery.value.toLowerCase().trim();
      const matchName = row.name.toLowerCase().includes(q);
      const matchTitle = row.englishTitle.toLowerCase().includes(q);
      const matchArea = row.areaName.toLowerCase().includes(q);
      const matchDesc = row.description.toLowerCase().includes(q);
      const matchComponents = row.components.some(c => c.toLowerCase().includes(q));
      const matchDeps = row.dependencies.some(d => d.name.toLowerCase().includes(q));
      return matchName || matchTitle || matchArea || matchDesc || matchComponents || matchDeps;
    }

    return true;
  });
});

// Platform Health Summary Metrics
const totalSubsystemsCount = computed(() => matrixRows.value.length);
const healthyCount = computed(() => matrixRows.value.filter(r => r.status === 'HEALTHY').length);
const degradedCount = computed(() => matrixRows.value.filter(r => r.status === 'DEGRADED').length);
const unavailableCount = computed(() => matrixRows.value.filter(r => r.status === 'UNAVAILABLE').length);

const averageAvailability = computed(() => {
  const availabilities = matrixRows.value
    .map(r => r.availabilityPercent)
    .filter((v): v is number => v != null && !isNaN(v));
  if (availabilities.length === 0) return '—';
  const avg = availabilities.reduce((a, b) => a + b, 0) / availabilities.length;
  return `${avg.toFixed(2)}%`;
});

const totalRestarts = computed(() => matrixRows.value.reduce((acc, r) => acc + (r.restartCount || 0), 0));
const totalFailedOps = computed(() => matrixRows.value.reduce((acc, r) => acc + (r.failedOperations || 0), 0));

const lastRefreshedText = computed(() => {
  if (!lastRefreshed.value) return 'Not refreshed';
  return lastRefreshed.value.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', second: '2-digit' });
});

// Inspect subsystem navigation
const inspectSubsystem = (row: SubsystemHealthRow) => {
  router.push(row.inspectRoute);
};

// Open dependencies inspection drawer
const openDependenciesDrawer = (row: SubsystemHealthRow) => {
  selectedSubsystemForDependencies.value = row;
  isDependenciesDrawerOpen.value = true;
};

const closeDependenciesDrawer = () => {
  isDependenciesDrawerOpen.value = false;
  selectedSubsystemForDependencies.value = null;
};
</script>

<template>
  <div class="health-view space-y-4 font-sans">
    <!-- Top Action Toolbar (Single Refresh Control) -->
    <IrisToolbar
      :show-search="false"
      :show-refresh="true"
      :refreshing="refreshing"
      @refresh="fetchAllHealth"
    >
      <template #left>
        <div class="flex items-center gap-2">
          <div class="p-1.5 rounded bg-rose-50 text-rose-700 border border-rose-100 flex items-center justify-center">
            <HeartPulse :size="16" />
          </div>
          <div>
            <h1 class="text-xs font-bold text-slate-900 uppercase tracking-wider">
              Harmonia Architecture Health &amp; Dependency Matrix
            </h1>
            <p class="text-[11px] text-slate-500 font-sans">
              Consolidated operational telemetry, availability SLAs, and upstream/downstream dependency probes
            </p>
          </div>
        </div>
      </template>

      <template #right>
        <div class="flex items-center gap-3">
          <div class="text-[11px] font-mono text-slate-500 hidden sm:flex items-center gap-1">
            <Clock :size="12" class="text-slate-400" />
            <span>Probed: {{ lastRefreshedText }}</span>
          </div>
        </div>
      </template>
    </IrisToolbar>

    <!-- Operational Summary KPI Strip -->
    <div class="bg-white border border-[var(--iris-border-default)] rounded-[var(--iris-border-radius)] p-3.5 shadow-subtle flex flex-wrap items-center justify-between gap-4">
      <div class="flex items-center gap-4 flex-wrap">
        <!-- Fleet Status Indicator -->
        <div class="flex items-center gap-2.5 pr-4 border-r border-slate-200">
          <span class="text-xs font-bold text-slate-500 uppercase tracking-wider">FLEET HEALTH:</span>
          <IrisStatus 
            :status="unavailableCount > 0 ? 'UNAVAILABLE' : (degradedCount > 0 ? 'DEGRADED' : 'HEALTHY')" 
            size="md" 
            :show-pulse="true" 
            label-format="upper" 
          />
        </div>

        <!-- Metric Pills -->
        <div class="flex items-center gap-3 flex-wrap text-xs font-mono">
          <!-- Subsystems Count -->
          <div class="flex items-center gap-1.5 text-slate-700">
            <span class="text-slate-400 font-sans">Subsystems:</span>
            <span class="font-bold text-slate-900">{{ healthyCount }}/{{ totalSubsystemsCount }} Nominal</span>
            <span v-if="degradedCount > 0" class="text-amber-700 font-bold ml-1 font-sans">
              ({{ degradedCount }} Degraded)
            </span>
            <span v-if="unavailableCount > 0" class="text-rose-700 font-bold ml-1 font-sans">
              ({{ unavailableCount }} Down)
            </span>
          </div>

          <span class="text-slate-300">&bull;</span>

          <!-- Average Availability -->
          <div class="flex items-center gap-1.5 text-slate-700">
            <span class="text-slate-400 font-sans">Fleet SLA:</span>
            <span class="font-bold text-emerald-700">{{ averageAvailability }}</span>
          </div>

          <span class="text-slate-300">&bull;</span>

          <!-- Total Restarts -->
          <div class="flex items-center gap-1.5 text-slate-700">
            <span class="text-slate-400 font-sans">Fleet Restarts:</span>
            <span 
              class="font-bold"
              :class="totalRestarts > 0 ? 'text-amber-700' : 'text-slate-900'"
            >
              {{ totalRestarts }}
            </span>
          </div>

          <span class="text-slate-300">&bull;</span>

          <!-- Total Failed Operations -->
          <div class="flex items-center gap-1.5 text-slate-700">
            <span class="text-slate-400 font-sans">Failed Ops:</span>
            <span 
              class="font-bold"
              :class="totalFailedOps > 0 ? 'text-rose-700' : 'text-slate-900'"
            >
              {{ totalFailedOps }}
            </span>
          </div>
        </div>
      </div>

      <!-- Quick Link to Subsystems Workstation -->
      <router-link
        to="/subsystems"
        class="inline-flex items-center gap-1 text-xs font-semibold text-sky-700 hover:text-sky-900 hover:underline cursor-pointer"
      >
        <span>Open Subsystem Workstation</span>
        <ArrowRight :size="13" />
      </router-link>
    </div>

    <!-- Filter & Search Controls -->
    <div class="bg-white border border-[var(--iris-border-default)] rounded-[var(--iris-border-radius)] p-3 shadow-subtle flex flex-wrap items-center justify-between gap-3">
      <!-- Search Input -->
      <div class="relative flex-1 min-w-[240px] max-w-md">
        <Search :size="14" class="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400 pointer-events-none" />
        <input
          id="health-search"
          v-model="searchQuery"
          type="text"
          placeholder="Filter by subsystem, area, component, or dependency..."
          class="w-full pl-9 pr-3 py-1.5 text-xs bg-slate-50 hover:bg-white focus:bg-white border border-slate-300 rounded focus:border-sky-500 focus:outline-none transition"
        />
        <button
          v-if="searchQuery"
          type="button"
          @click="searchQuery = ''"
          class="absolute right-2.5 top-1/2 -translate-y-1/2 text-slate-400 hover:text-slate-600 p-0.5"
          aria-label="Clear search"
        >
          <X :size="12" />
        </button>
      </div>

      <!-- Filter Controls -->
      <div class="flex items-center gap-3 flex-wrap">
        <!-- Area Filter -->
        <div class="flex items-center gap-1.5 text-xs">
          <span class="text-slate-500 font-medium">Area:</span>
          <select
            v-model="areaFilter"
            class="px-2 py-1 text-xs bg-white border border-slate-300 rounded focus:border-sky-500 focus:outline-none cursor-pointer"
          >
            <option value="ALL">All Areas (7)</option>
            <option value="security-policy">Security &amp; Policy</option>
            <option value="information-state">Information &amp; State</option>
            <option value="integration-transport">Integration &amp; Transport</option>
            <option value="execution-processing">Execution &amp; Processing</option>
            <option value="presentation">Presentation</option>
            <option value="collaboration">Collaboration</option>
            <option value="simulation-validation">Simulation &amp; Validation</option>
          </select>
        </div>

        <!-- Status Filter Tabs -->
        <div class="inline-flex rounded-md shadow-xs" role="group">
          <button
            type="button"
            class="px-2.5 py-1 text-xs font-medium border border-slate-300 rounded-l transition"
            :class="statusFilter === 'ALL' ? 'bg-sky-600 text-white border-sky-600' : 'bg-white text-slate-700 hover:bg-slate-50'"
            @click="statusFilter = 'ALL'"
          >
            All
          </button>
          <button
            type="button"
            class="px-2.5 py-1 text-xs font-medium border-t border-b border-slate-300 transition"
            :class="statusFilter === 'HEALTHY' ? 'bg-emerald-600 text-white border-emerald-600' : 'bg-white text-slate-700 hover:bg-slate-50'"
            @click="statusFilter = 'HEALTHY'"
          >
            Nominal
          </button>
          <button
            type="button"
            class="px-2.5 py-1 text-xs font-medium border-t border-b border-l border-slate-300 transition"
            :class="statusFilter === 'DEGRADED' ? 'bg-amber-600 text-white border-amber-600' : 'bg-white text-slate-700 hover:bg-slate-50'"
            @click="statusFilter = 'DEGRADED'"
          >
            Degraded
          </button>
          <button
            type="button"
            class="px-2.5 py-1 text-xs font-medium border border-slate-300 rounded-r transition"
            :class="statusFilter === 'UNAVAILABLE' ? 'bg-rose-600 text-white border-rose-600' : 'bg-white text-slate-700 hover:bg-slate-50'"
            @click="statusFilter = 'UNAVAILABLE'"
          >
            Down
          </button>
        </div>
      </div>
    </div>

    <!-- High-Density Architecture Health Matrix -->
    <div class="bg-white border border-[var(--iris-border-default)] rounded-[var(--iris-border-radius)] shadow-subtle overflow-hidden">
      <IrisDataTable
        :value="filteredRows"
        :columns="tableColumns"
        :loading="refreshing"
        data-key="id"
        empty-message="No subsystems match current filters"
        @row-click="inspectSubsystem($event.data)"
      >
        <!-- Custom Subsystem Identity -->
        <template #subsystem="{ data }">
          <div class="py-1">
            <div class="flex items-center gap-2">
              <span class="font-bold text-slate-900 hover:text-sky-700 text-xs tracking-tight transition cursor-pointer">
                {{ data.name }}
              </span>
              <span class="text-[10px] font-mono px-1.5 py-0.2 rounded bg-slate-100 text-slate-600 border border-slate-200">
                {{ data.areaName }}
              </span>
            </div>
            <div class="text-[11px] text-slate-500 font-sans mt-0.5 line-clamp-1">
              {{ data.englishTitle }} &bull; {{ data.description }}
            </div>
            <!-- Subsystem Component Badges -->
            <div class="flex items-center gap-1.5 mt-1.5 flex-wrap">
              <span
                v-for="(comp, idx) in data.components"
                :key="idx"
                class="text-[10px] font-mono px-1.5 py-0.2 rounded bg-sky-50/70 text-sky-800 border border-sky-100"
              >
                {{ comp }}
              </span>
              <span 
                v-if="data.note" 
                class="text-[10px] font-mono px-1.5 py-0.2 rounded bg-amber-50 text-amber-800 border border-amber-200"
              >
                {{ data.note }}
              </span>
            </div>
          </div>
        </template>

        <!-- Status -->
        <template #status="{ data }">
          <div class="flex items-center gap-1.5">
            <IrisStatus :status="data.status" size="sm" :stale="data.isStale" :show-pulse="true" />
          </div>
        </template>

        <!-- Availability SLA % -->
        <template #availability="{ data }">
          <span 
            class="font-mono text-xs font-semibold"
            :class="data.availabilityPercent && data.availabilityPercent < 99.0 ? 'text-amber-700' : 'text-emerald-700'"
          >
            {{ data.availabilityPercent != null ? `${data.availabilityPercent.toFixed(2)}%` : 'N/A' }}
          </span>
        </template>

        <!-- P95 Latency -->
        <template #p95Latency="{ data }">
          <span 
            v-if="data.p95LatencyMs != null"
            class="font-mono text-xs font-semibold text-sky-700"
          >
            {{ data.p95LatencyMs }} ms
          </span>
          <span v-else class="text-slate-400 font-mono text-xs">
            N/A
          </span>
        </template>

        <!-- Restarts / Failed Operations -->
        <template #operations="{ data }">
          <div class="font-mono text-xs flex items-center justify-center gap-1">
            <span 
              :class="data.restartCount > 0 ? 'text-amber-700 font-bold' : 'text-slate-600'"
              :title="`Restarts: ${data.restartCount}`"
            >
              {{ data.restartCount }}
            </span>
            <span class="text-slate-300">/</span>
            <span 
              :class="data.failedOperations > 0 ? 'text-rose-700 font-bold' : 'text-slate-600'"
              :title="`Failed Operations: ${data.failedOperations}`"
            >
              {{ data.failedOperations }}
            </span>
          </div>
        </template>

        <!-- Dependencies Summary with Popover/Drawer Trigger -->
        <template #dependencies="{ data }">
          <button
            type="button"
            class="inline-flex items-center gap-1.5 px-2 py-0.5 rounded text-xs font-mono transition cursor-pointer"
            :class="data.dependencies.length > 0 
              ? 'bg-slate-100 hover:bg-slate-200 text-slate-800 border border-slate-300' 
              : 'bg-slate-50 text-slate-500 border border-slate-200 cursor-default'"
            :disabled="data.dependencies.length === 0"
            @click.stop="openDependenciesDrawer(data)"
            :title="`Inspect ${data.name} dependencies`"
          >
            <Network :size="12" class="text-slate-500" />
            <span>{{ data.dependenciesSummary }}</span>
          </button>
        </template>

        <!-- Actions -->
        <template #actions="{ data }">
          <button
            type="button"
            class="inline-flex items-center gap-1 px-2.5 py-1 rounded bg-slate-50 hover:bg-slate-100 text-slate-700 hover:text-slate-900 border border-slate-300 text-xs font-medium transition cursor-pointer font-sans"
            :aria-label="`Inspect ${data.name} in Subsystem Workstation`"
            @click.stop="inspectSubsystem(data)"
          >
            <span>Inspect</span>
            <ArrowRight :size="12" class="text-slate-400 group-hover:text-sky-600 transition" />
          </button>
        </template>
      </IrisDataTable>
    </div>

    <!-- Secondary Telemetry Disclosure: Dependencies Detail Drawer -->
    <teleport to="body">
      <div 
        v-if="isDependenciesDrawerOpen" 
        class="fixed inset-0 bg-slate-900/40 backdrop-blur-[2px] z-50 transition-opacity" 
        @click="closeDependenciesDrawer"
        aria-hidden="true"
      ></div>

      <aside
        v-if="isDependenciesDrawerOpen && selectedSubsystemForDependencies"
        class="fixed top-0 bottom-0 right-0 w-full max-w-[500px] bg-white border-l border-slate-200 z-50 flex flex-col shadow-2xl font-sans"
        role="dialog"
        aria-modal="true"
        :aria-label="`Dependencies for ${selectedSubsystemForDependencies.name}`"
      >
        <!-- Header -->
        <div class="px-5 py-4 bg-slate-50 border-b border-slate-200 flex items-center justify-between">
          <div class="flex items-center gap-3">
            <div class="p-2 rounded bg-sky-50 text-sky-700 border border-sky-100">
              <Network :size="18" />
            </div>
            <div>
              <h2 class="text-sm font-bold text-slate-900">
                {{ selectedSubsystemForDependencies.name }} Dependencies
              </h2>
              <p class="text-[11px] text-slate-500 font-sans">
                {{ selectedSubsystemForDependencies.areaName }} &bull; Health &amp; Probe Status
              </p>
            </div>
          </div>
          <button
            type="button"
            @click="closeDependenciesDrawer"
            class="p-1.5 text-slate-400 hover:text-slate-700 rounded hover:bg-slate-100 transition cursor-pointer"
            aria-label="Close drawer"
          >
            <X :size="16" />
          </button>
        </div>

        <!-- Body -->
        <div class="p-5 flex-1 overflow-y-auto space-y-4">
          <div class="p-3 bg-slate-50 rounded-lg border border-slate-200 text-xs space-y-1">
            <div class="font-semibold text-slate-800">Operational Subsystem Summary</div>
            <div class="text-slate-600">{{ selectedSubsystemForDependencies.description }}</div>
            <div class="pt-2 flex items-center justify-between text-[11px] font-mono text-slate-500">
              <span>Availability: {{ selectedSubsystemForDependencies.availabilityPercent != null ? `${selectedSubsystemForDependencies.availabilityPercent.toFixed(2)}%` : 'N/A' }}</span>
              <span>P95 Latency: {{ selectedSubsystemForDependencies.p95LatencyMs != null ? `${selectedSubsystemForDependencies.p95LatencyMs} ms` : 'N/A' }}</span>
            </div>
          </div>

          <div>
            <h3 class="text-xs font-bold text-slate-900 uppercase tracking-wider mb-2 flex items-center gap-1.5">
              <Network :size="13" class="text-slate-500" />
              <span>Upstream &amp; Downstream Target Probes</span>
            </h3>

            <div v-if="selectedSubsystemForDependencies.dependencies.length === 0" class="p-4 text-center text-xs text-slate-500 border border-dashed border-slate-300 rounded-lg">
              No direct external upstream/downstream service dependencies registered.
            </div>

            <div v-else class="space-y-2">
              <div
                v-for="(dep, idx) in selectedSubsystemForDependencies.dependencies"
                :key="idx"
                class="p-3 rounded-lg border border-slate-200 bg-white shadow-xs space-y-1"
              >
                <div class="flex items-center justify-between">
                  <span class="font-bold text-xs text-slate-900 font-mono">{{ dep.name }}</span>
                  <IrisStatus :status="dep.status" size="sm" :show-pulse="true" />
                </div>
                <div v-if="dep.latencyMs != null" class="text-[11px] font-mono text-slate-500">
                  Probe Latency: <span class="font-semibold text-sky-700">{{ dep.latencyMs }} ms</span>
                </div>
                <div v-if="dep.message" class="text-[11px] text-slate-600 font-sans mt-0.5">
                  {{ dep.message }}
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Footer -->
        <div class="px-5 py-3 bg-slate-50 border-t border-slate-200 flex items-center justify-between">
          <button
            type="button"
            class="text-xs text-sky-700 hover:text-sky-900 font-medium hover:underline flex items-center gap-1 cursor-pointer"
            @click="inspectSubsystem(selectedSubsystemForDependencies)"
          >
            <span>Open in Subsystems Workstation</span>
            <ExternalLink :size="12" />
          </button>
          <button
            type="button"
            @click="closeDependenciesDrawer"
            class="px-3 py-1.5 text-xs font-medium text-slate-700 bg-white border border-slate-300 rounded hover:bg-slate-50 transition cursor-pointer"
          >
            Close
          </button>
        </div>
      </aside>
    </teleport>
  </div>
</template>

<style scoped>
.health-view {
  width: 100%;
}
</style>
