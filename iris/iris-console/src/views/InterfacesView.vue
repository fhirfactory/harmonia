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
import { 
  Radio, 
  ArrowDownLeft, 
  ArrowUpRight, 
  ShieldCheck, 
  AlertTriangle, 
  Info, 
  ChevronRight,
  Layers
} from 'lucide-vue-next';
import { 
  IrisSubsystemIdentity, 
  IrisStatus, 
  IrisToolbar,
  IrisDataTable,
  type DataTableColumn
} from '@harmonia/iris-befe';
import { useOperationsStore } from '../stores/operationsStore';
import InterfaceDetailDrawer, { type PylaiGateway } from '../components/interfaces/InterfaceDetailDrawer.vue';

const store = useOperationsStore();

const searchQuery = ref('');
const directionFilter = ref<'ALL' | 'INBOUND' | 'OUTBOUND'>('ALL');
const selectedGateway = ref<PylaiGateway | null>(null);
const isDrawerOpen = ref(false);

const authoritativeGateways: PylaiGateway[] = [
  {
    id: 'pylai-mllp-in',
    name: 'MLLP Inbound Gateway',
    englishTitle: 'Ingress Interface',
    description: 'Dual-write ACK gateway translating external clinical wire protocols (hospital ADT/ORM inbound streams) into Petasos events.',
    direction: 'INBOUND',
    protocol: 'HL7 v2 / MLLP',
    port: 2575,
    managementPort: 8084,
    targetQueue: 'petasos.queue.pylai.mllp.in',
    complianceRule: 'Invariant 4 (Dual-Write Safety: AA ACK issued only after downstream Petasos enqueue)',
    activeListeners: 1,
    currentConnections: 1
  },
  {
    id: 'pylai-mllp-out-his',
    name: 'MLLP Outbound HIS',
    englishTitle: 'HIS Distribution Interface',
    description: 'Outbound HL7 v2 clinical messaging gateway distributing messages to Hospital Information System.',
    direction: 'OUTBOUND',
    protocol: 'HL7 v2 / MLLP',
    port: 8087,
    managementPort: 8084,
    targetQueue: 'petasos.queue.mllp.outbound.his',
    complianceRule: 'Invariant 5 (Destination Fan-Out Tracking: Checkpoint on HIS transmission)',
    activeListeners: 1,
    currentConnections: 0
  },
  {
    id: 'pylai-mllp-out-lis',
    name: 'MLLP Outbound LIS',
    englishTitle: 'LIS Distribution Interface',
    description: 'Outbound HL7 v2 pathology/lab distribution gateway to Laboratory Information System.',
    direction: 'OUTBOUND',
    protocol: 'HL7 v2 / MLLP',
    port: 8088,
    managementPort: 8084,
    targetQueue: 'petasos.queue.mllp.outbound.lis',
    complianceRule: 'Invariant 5 (Destination Fan-Out Tracking: Checkpoint on LIS transmission)',
    activeListeners: 1,
    currentConnections: 0
  },
  {
    id: 'pylai-fhir-registry',
    name: 'FHIR Provider Registry Gateway',
    englishTitle: 'REST Registry Ingress',
    description: 'FHIR R5 Practitioner & Organization practitioner self-service and directory interface.',
    direction: 'INBOUND',
    protocol: 'FHIR R5 / REST',
    port: 8089,
    managementPort: 8084,
    targetQueue: 'petasos.queue.ponos.dispatch',
    complianceRule: 'Invariant 3 & 6 (Themis Default-Deny RBAC; zero-JPA presentation boundary)',
    activeListeners: 1,
    currentConnections: 0
  }
];

onMounted(async () => {
  await Promise.all([
    store.fetchSubsystems(),
    store.fetchInstances('pylai'),
    store.fetchHealth('pylai')
  ]);
});

async function handleRefresh() {
  await Promise.all([
    store.fetchSubsystems(),
    store.fetchInstances('pylai'),
    store.fetchHealth('pylai')
  ]);
}

const pylaiSubsystem = computed(() => {
  return store.subsystems.find(s => s.id === 'pylai') || null;
});

const pylaiStatus = computed(() => {
  return pylaiSubsystem.value?.state || 'UNKNOWN';
});

const isStale = computed(() => store.isStale);

const filteredGateways = computed(() => {
  return authoritativeGateways.filter(gw => {
    // Direction filter
    if (directionFilter.value !== 'ALL' && gw.direction !== directionFilter.value) {
      return false;
    }
    // Search query
    if (searchQuery.value.trim()) {
      const q = searchQuery.value.toLowerCase().trim();
      const matchName = gw.name.toLowerCase().includes(q);
      const matchId = gw.id.toLowerCase().includes(q);
      const matchTitle = gw.englishTitle.toLowerCase().includes(q);
      const matchDesc = gw.description.toLowerCase().includes(q);
      const matchProto = gw.protocol.toLowerCase().includes(q);
      const matchPort = String(gw.port).includes(q);
      const matchQueue = gw.targetQueue.toLowerCase().includes(q);
      if (!matchName && !matchId && !matchTitle && !matchDesc && !matchProto && !matchPort && !matchQueue) {
        return false;
      }
    }
    return true;
  });
});

const totalGatewaysCount = computed(() => authoritativeGateways.length);
const inboundCount = computed(() => authoritativeGateways.filter(g => g.direction === 'INBOUND').length);
const outboundCount = computed(() => authoritativeGateways.filter(g => g.direction === 'OUTBOUND').length);
const activeListenersCount = computed(() => authoritativeGateways.reduce((sum, g) => sum + (g.activeListeners || 0), 0));

const tableColumns: DataTableColumn[] = [
  { field: 'name', header: 'Interface Name' },
  { field: 'direction', header: 'Direction', width: '120px' },
  { field: 'protocol', header: 'Protocol', width: '160px' },
  { field: 'port', header: 'Port', width: '100px' },
  { field: 'status', header: 'Status', width: '130px' },
  { field: 'throughput', header: 'Throughput / Activity', width: '180px' },
  { field: 'errorState', header: 'Error State', width: '120px' },
  { field: 'actions', header: 'Action', width: '90px', align: 'right' }
];

function openGatewayDrawer(gw: PylaiGateway) {
  selectedGateway.value = gw;
  isDrawerOpen.value = true;
}

function closeGatewayDrawer() {
  isDrawerOpen.value = false;
  selectedGateway.value = null;
}
</script>

<template>
  <div class="interfaces-view space-y-4 font-sans">
    <!-- View Header with Authoritative Pylai Identity -->
    <IrisSubsystemIdentity
      name="Pylai"
      description="Harmonia Inbound/Outbound protocol gateways translating external clinical wire protocols (HL7 v2 MLLP, FHIR REST) into Petasos messaging events."
      :status="pylaiStatus"
      :stale="isStale"
    >
      <template #icon>
        <Radio :size="22" class="text-sky-700" />
      </template>

      <template #badges>
        <span class="px-2 py-0.5 rounded text-xs font-semibold bg-slate-100 text-slate-700 border border-slate-200">
          Interface Gateways
        </span>
        <span class="inline-flex items-center gap-1 px-2.5 py-0.5 rounded-full text-xs font-mono font-medium bg-emerald-50 text-emerald-800 border border-emerald-200 shadow-xs" title="Invariant 4 Dual-Write Safety active on ingress">
          <ShieldCheck :size="12" class="text-emerald-600" />
          <span>REC-001 Dual-Write Safety</span>
        </span>
        <span class="inline-flex items-center gap-1 px-2.5 py-0.5 rounded-full text-xs font-mono font-medium bg-sky-50 text-sky-800 border border-sky-200 shadow-xs" title="Live message/sec rates pending Camel metrics provider">
          <Info :size="12" class="text-sky-600" />
          <span>IRIS-API-GAP-001</span>
        </span>
      </template>

      <template #metadata>
        <div class="text-xs text-slate-500 mt-0.5">
          Architectural Area: <span class="font-semibold text-slate-700">Integration &amp; Transport</span> &bull; 
          Protocol adaptation, wire validation, dual-write ACK safety, and fan-out delivery.
        </div>
      </template>
    </IrisSubsystemIdentity>

    <!-- Compact Operational Summary Strip -->
    <div class="bg-white border border-[var(--iris-border-default)] rounded-[var(--iris-border-radius)] p-3 shadow-subtle flex flex-wrap items-center justify-between gap-3 text-xs">
      <div class="flex items-center gap-4 flex-wrap font-mono">
        <!-- Configured Interfaces -->
        <div class="flex items-center gap-1.5 text-slate-700">
          <span class="text-slate-400 font-sans">Configured Interfaces:</span>
          <span class="font-bold text-slate-900">{{ totalGatewaysCount }} Interfaces</span>
        </div>

        <span class="text-slate-300">&bull;</span>

        <!-- Inbound Interfaces -->
        <div class="flex items-center gap-1.5 text-slate-700">
          <span class="text-slate-400 font-sans">Inbound Interfaces:</span>
          <span class="font-bold text-sky-800">{{ inboundCount }}</span>
        </div>

        <span class="text-slate-300">&bull;</span>

        <!-- Outbound Interfaces -->
        <div class="flex items-center gap-1.5 text-slate-700">
          <span class="text-slate-400 font-sans">Outbound Interfaces:</span>
          <span class="font-bold text-purple-800">{{ outboundCount }}</span>
        </div>

        <span class="text-slate-300">&bull;</span>

        <!-- Active Listeners -->
        <div class="flex items-center gap-1.5 text-slate-700">
          <span class="text-slate-400 font-sans">Active Listeners:</span>
          <span class="font-bold text-emerald-800">{{ activeListenersCount }}</span>
        </div>
      </div>

      <div class="text-[11px] font-mono text-slate-500 hidden md:block">
        Ports: :2575, :8087, :8088, :8089
      </div>
    </div>

    <!-- Action & Filter Toolbar -->
    <IrisToolbar
      v-model:searchQuery="searchQuery"
      :show-search="true"
      search-placeholder="Filter interfaces by name, port, protocol..."
      :show-refresh="true"
      :refreshing="store.loading"
      @refresh="handleRefresh"
    >
      <template #filter>
        <div class="inline-flex rounded-md shadow-xs" role="group" aria-label="Filter by interface direction">
          <button
            type="button"
            class="px-2.5 py-1 text-xs font-semibold border rounded-l-md transition cursor-pointer"
            :class="directionFilter === 'ALL'
              ? 'bg-sky-50 text-sky-800 border-sky-300 shadow-xs'
              : 'bg-white text-slate-600 border-slate-200 hover:bg-slate-50'"
            @click="directionFilter = 'ALL'"
          >
            All ({{ totalGatewaysCount }})
          </button>
          <button
            type="button"
            class="px-2.5 py-1 text-xs font-semibold border-t border-b border-r transition cursor-pointer"
            :class="directionFilter === 'INBOUND'
              ? 'bg-sky-50 text-sky-800 border-sky-300 shadow-xs'
              : 'bg-white text-slate-600 border-slate-200 hover:bg-slate-50'"
            @click="directionFilter = 'INBOUND'"
          >
            Inbound ({{ inboundCount }})
          </button>
          <button
            type="button"
            class="px-2.5 py-1 text-xs font-semibold border-t border-b border-r rounded-r-md transition cursor-pointer"
            :class="directionFilter === 'OUTBOUND'
              ? 'bg-sky-50 text-sky-800 border-sky-300 shadow-xs'
              : 'bg-white text-slate-600 border-slate-200 hover:bg-slate-50'"
            @click="directionFilter = 'OUTBOUND'"
          >
            Outbound ({{ outboundCount }})
          </button>
        </div>
      </template>
    </IrisToolbar>

    <!-- High-Density IrisDataTable for Interfaces -->
    <IrisDataTable
      :value="filteredGateways"
      :columns="tableColumns"
      data-key="id"
      empty-message="No Pylai gateways match your search or direction filter criteria."
      @row-click="openGatewayDrawer($event.data)"
    >
      <!-- Interface Name & Subtitle -->
      <template #name="{ data }">
        <div>
          <div class="font-bold font-mono text-slate-900">{{ data.name }}</div>
          <div class="text-[11px] text-slate-500 font-sans truncate max-w-sm">{{ data.description }}</div>
        </div>
      </template>

      <!-- Direction Badge -->
      <template #direction="{ data }">
        <span 
          class="px-2 py-0.5 rounded text-[10px] font-bold uppercase tracking-wider border inline-flex items-center gap-1 font-mono"
          :class="data.direction === 'INBOUND' 
            ? 'bg-sky-50 text-sky-800 border-sky-200' 
            : 'bg-purple-50 text-purple-800 border-purple-200'"
        >
          <ArrowDownLeft v-if="data.direction === 'INBOUND'" :size="11" />
          <ArrowUpRight v-else :size="11" />
          <span>{{ data.direction }}</span>
        </span>
      </template>

      <!-- Protocol -->
      <template #protocol="{ data }">
        <span class="px-2 py-0.5 rounded text-[11px] font-mono font-medium bg-slate-100 text-slate-700 border border-slate-200">
          {{ data.protocol }}
        </span>
      </template>

      <!-- Port -->
      <template #port="{ data }">
        <span class="font-mono font-bold text-slate-800 text-xs">
          :{{ data.port }}
        </span>
      </template>

      <!-- Status -->
      <template #status>
        <IrisStatus 
          :status="pylaiStatus" 
          :stale="isStale"
          label-format="upper" 
          size="sm" 
        />
      </template>

      <!-- Throughput (Honest Fallback) -->
      <template #throughput>
        <div class="text-xs font-mono text-slate-500" title="Wire rates pending Camel metrics provider (IRIS-API-GAP-001)">
          &mdash; <span class="text-[11px] text-slate-400 italic">(Telemetry initializing)</span>
        </div>
      </template>

      <!-- Error State -->
      <template #errorState="{ data }">
        <span 
          class="text-xs font-mono"
          :class="pylaiStatus === 'HEALTHY' ? 'text-emerald-700' : 'text-amber-700'"
        >
          {{ pylaiStatus === 'HEALTHY' ? 'Nominal (0 err)' : 'Degraded (1 err)' }}
        </span>
      </template>

      <!-- Action Button -->
      <template #actions="{ data }">
        <button
          type="button"
          class="iris-inspect-btn inline-flex items-center gap-1 px-2.5 py-1 text-xs font-semibold text-sky-700 hover:text-sky-900 bg-sky-50 hover:bg-sky-100 border border-sky-200 rounded transition cursor-pointer"
          @click.stop="openGatewayDrawer(data)"
          :aria-label="`Inspect ${data.name}`"
        >
          <span>Inspect</span>
          <ChevronRight :size="12" />
        </button>
      </template>
    </IrisDataTable>

    <!-- Slide-over Interface Detail Drawer -->
    <InterfaceDetailDrawer
      :gateway="selectedGateway"
      :is-open="isDrawerOpen"
      :pylai-status="pylaiStatus"
      @close="closeGatewayDrawer"
    />
  </div>
</template>

<style scoped>
.interfaces-view {
  width: 100%;
}
</style>
