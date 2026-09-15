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
import { onMounted } from 'vue';
import { useOperationsStore } from '../stores/operationsStore';
import { useSequenceStore } from '../stores/sequenceStore';
import { 
  Server, Database, Cpu, Layers, HardDrive, 
  Zap, Radio, GitMerge, ShieldCheck, CheckCircle2,
  ArrowRight, Activity, Clock, RefreshCw
} from 'lucide-vue-next';

const operationsStore = useOperationsStore();
const sequenceStore = useSequenceStore();

const refreshAll = async () => {
  await Promise.all([
    operationsStore.fetchStatus(),
    operationsStore.fetchModules(),
    operationsStore.fetchOperationalResources(),
    sequenceStore.fetchSequences()
  ]);
};

onMounted(() => {
  refreshAll();
});
const getActivityCount = (seq: any): number => {
  if (!seq) return 0;
  if (seq.activityIds) {
    if (Array.isArray(seq.activityIds)) return seq.activityIds.length;
    if (typeof seq.activityIds === 'object') return Object.keys(seq.activityIds).length;
  }
  if (seq.activities) {
    if (Array.isArray(seq.activities)) return seq.activities.length;
    if (typeof seq.activities === 'object') return Object.keys(seq.activities).length;
  }
  return 0;
};
</script>

<template>
  <div class="space-y-6">
    <!-- Header Hero -->
    <div class="card flex flex-col md:flex-row items-start md:items-center justify-between gap-4" style="background: linear-gradient(135deg, #064e3b 0%, #0f172a 100%);">
      <div>
        <div class="flex items-center gap-2 mb-1">
          <span class="badge badge-green flex items-center gap-1">
            <Zap :size="12" /> Sub-10ms Clustered Memory Grid
          </span>
          <span class="badge badge-blue flex items-center gap-1">
            <ShieldCheck :size="12" /> Write-Behind Eventual Persistence
          </span>
        </div>
        <h1 class="text-2xl md:text-3xl font-extrabold text-white">5-Tier Clustered Operations &amp; Task Sequences</h1>
        <p class="subtitle mt-1">Real-time health, cluster topology, per-gateway queues, and workflow sequence pipelines.</p>
      </div>
      <div class="flex items-center gap-2">
        <button 
          @click="sequenceStore.syncSequences()" 
          :disabled="sequenceStore.syncing"
          class="btn btn-secondary whitespace-nowrap flex items-center gap-1.5"
          title="Synchronise queues and task sequences with Task Sequence Processor"
        >
          <RefreshCw :size="15" :class="{ 'animate-spin': sequenceStore.syncing }" />
          <span>{{ sequenceStore.syncing ? 'Synchronising...' : 'Synchronise' }}</span>
        </button>
        <button @click="refreshAll" class="btn btn-primary whitespace-nowrap">
          Refresh Operational State
        </button>
      </div>
    </div>

    <!-- 5-Tier Architecture Diagram -->
    <div class="card">
      <h3 class="text-base font-bold text-white mb-4 flex items-center gap-2">
        <Layers :size="18" class="text-emerald-400" />
        Platform Architecture &amp; Data Flow
      </h3>

      <div class="grid grid-cols-1 md:grid-cols-5 gap-3 text-center">
        <!-- Tier 1: UI -->
        <div class="p-3 rounded-lg bg-slate-950 border border-sky-500/40 relative">
          <div class="p-2 w-10 h-10 rounded-full bg-sky-500/10 text-sky-400 mx-auto flex items-center justify-center mb-2">
            <Cpu :size="20" />
          </div>
          <span class="text-xs font-bold text-sky-400 uppercase tracking-wider block">1. UI Tier</span>
          <h4 class="text-sm font-semibold text-white mt-1">Vue 3 + TypeScript</h4>
          <p class="text-[11px] text-slate-400 mt-1">Operations UI &amp; FHIR Resource UI SPAs with Pinia state</p>
          <span class="badge badge-blue mt-2 text-[10px]">Client Web (3000/3001)</span>
        </div>

        <!-- Tier 2: BEFE -->
        <div class="p-3 rounded-lg bg-slate-950 border border-indigo-500/40 relative">
          <div class="p-2 w-10 h-10 rounded-full bg-indigo-500/10 text-indigo-400 mx-auto flex items-center justify-center mb-2">
            <Server :size="20" />
          </div>
          <span class="text-xs font-bold text-indigo-400 uppercase tracking-wider block">2. BEFE Tier</span>
          <h4 class="text-sm font-semibold text-white mt-1">WildFly Jakarta EE</h4>
          <p class="text-[11px] text-slate-400 mt-1">JAX-RS endpoints, JSON-B/P, Hot Rod cache client</p>
          <span class="badge badge-purple mt-2 text-[10px]">Port 8090 (Ops) / 8080 (FHIR)</span>
        </div>

        <!-- Tier 3: Infinispan Cluster -->
        <div class="p-3 rounded-lg bg-slate-950 border border-emerald-500/40 relative">
          <div class="p-2 w-10 h-10 rounded-full bg-emerald-500/10 text-emerald-400 mx-auto flex items-center justify-center mb-2">
            <Zap :size="20" />
          </div>
          <span class="text-xs font-bold text-emerald-400 uppercase tracking-wider block">3. In-Memory Grid</span>
          <h4 class="text-sm font-semibold text-white mt-1">Infinispan 15 HA</h4>
          <p class="text-[11px] text-slate-400 mt-1">Replicated cache grid (tasksequence-cache, task-cache, fhir)</p>
          <span class="badge badge-green mt-2 text-[10px]">Port 11222</span>
        </div>

        <!-- Tier 4: Persistence SPI -->
        <div class="p-3 rounded-lg bg-slate-950 border border-amber-500/40 relative">
          <div class="p-2 w-10 h-10 rounded-full bg-amber-500/10 text-amber-400 mx-auto flex items-center justify-center mb-2">
            <HardDrive :size="20" />
          </div>
          <span class="text-xs font-bold text-amber-400 uppercase tracking-wider block">4. Persistence SPI</span>
          <h4 class="text-sm font-semibold text-white mt-1">NonBlockingStore SPI</h4>
          <p class="text-[11px] text-slate-400 mt-1">Async write-behind store targeting FHIR &amp; Operations JPA</p>
          <span class="badge badge-amber mt-2 text-[10px]">Custom SPI</span>
        </div>

        <!-- Tier 5: Disk Persistence -->
        <div class="p-3 rounded-lg bg-slate-950 border border-rose-500/40 relative">
          <div class="p-2 w-10 h-10 rounded-full bg-rose-500/10 text-rose-400 mx-auto flex items-center justify-center mb-2">
            <Database :size="20" />
          </div>
          <span class="text-xs font-bold text-rose-400 uppercase tracking-wider block">5. Disk Persistence</span>
          <h4 class="text-sm font-semibold text-white mt-1">Dual PostgreSQL Nodes</h4>
          <p class="text-[11px] text-slate-400 mt-1">HAPI FHIR JPA (8081/8082) &amp; Operations JPA (8085/8086)</p>
          <span class="badge badge-red mt-2 text-[10px]">PostgreSQL 16</span>
        </div>
      </div>
    </div>

    <!-- Operations Metrics Cards -->
    <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
      <!-- 1. Task Sequences -->
      <router-link to="/sequences" class="card hover:border-emerald-500 transition block text-left text-decoration-none">
        <div class="flex items-center justify-between mb-2">
          <span class="text-xs font-semibold text-slate-400">Task Sequences</span>
          <GitMerge :size="18" class="text-emerald-400" />
        </div>
        <div class="text-2xl font-extrabold text-white">{{ sequenceStore.sequences.length }}</div>
        <div class="text-xs text-emerald-400 mt-1 font-medium">Configured Pipeline Sequences</div>
      </router-link>

      <!-- 2. Messaging Queues -->
      <router-link to="/queues" class="card hover:border-emerald-500 transition block text-left text-decoration-none">
        <div class="flex items-center justify-between mb-2">
          <span class="text-xs font-semibold text-slate-400">ActiveMQ Artemis</span>
          <Radio :size="18" class="text-sky-400" />
        </div>
        <div class="text-2xl font-extrabold text-white">{{ operationsStore.queues.length }}</div>
        <div class="text-xs text-sky-400 mt-1 font-medium">Active Broker Queues</div>
      </router-link>

      <!-- 3. Clustered Caches -->
      <router-link to="/caches" class="card hover:border-emerald-500 transition block text-left text-decoration-none">
        <div class="flex items-center justify-between mb-2">
          <span class="text-xs font-semibold text-slate-400">Infinispan Grid</span>
          <HardDrive :size="18" class="text-purple-400" />
        </div>
        <div class="text-2xl font-extrabold text-white">{{ operationsStore.caches.length }}</div>
        <div class="text-xs text-purple-400 mt-1 font-medium">Replicated Sync Caches</div>
      </router-link>

      <!-- 4. Operations JPA Entities -->
      <router-link to="/operations-data" class="card hover:border-emerald-500 transition block text-left text-decoration-none">
        <div class="flex items-center justify-between mb-2">
          <span class="text-xs font-semibold text-slate-400">Operational Store</span>
          <Database :size="18" class="text-amber-400" />
        </div>
        <div class="text-2xl font-extrabold text-white">{{ operationsStore.operationalResources.length }}</div>
        <div class="text-xs text-amber-400 mt-1 font-medium">Non-FHIR Data Objects</div>
      </router-link>
    </div>

    <!-- Running Set of Modules in Infinispan Cluster -->
    <div class="card space-y-4">
      <div class="flex items-center justify-between">
        <div>
          <h3 class="text-base font-bold text-white flex items-center gap-2">
            <Radio :size="18" class="text-sky-400" />
            Cluster Module Registry &amp; Operational Status
          </h3>
          <p class="subtitle mt-0.5">Running set of all HIE modules and services registered in the Infinispan cluster cache.</p>
        </div>
        <button @click="operationsStore.fetchModules" class="btn btn-secondary text-xs flex items-center gap-1">
          <RefreshCw :size="12" /> Refresh Modules
        </button>
      </div>

      <div class="grid grid-cols-1 md:grid-cols-3 gap-3">
        <div v-for="mod in operationsStore.modules" :key="mod.moduleId" class="p-3.5 rounded-lg bg-slate-950 border border-slate-800">
          <div class="flex items-center justify-between">
            <span class="font-mono text-xs font-bold text-sky-400">{{ mod.moduleId }}</span>
            <span :class="mod.ready ? 'badge badge-green text-[10px]' : 'badge badge-amber text-[10px]'">
              {{ mod.status || (mod.ready ? 'READY' : 'STARTING') }}
            </span>
          </div>
          <h4 class="text-sm font-semibold text-white mt-1.5">{{ mod.moduleName || mod.moduleId }}</h4>
          <div class="text-xs text-slate-400 mt-0.5">{{ mod.moduleType }}</div>
          <div class="text-[11px] text-slate-500 mt-2 flex items-center justify-between">
            <span>Last Updated:</span>
            <span class="font-mono text-[10px]">{{ mod.lastUpdated ? new Date(mod.lastUpdated).toLocaleTimeString() : '-' }}</span>
          </div>
        </div>
        <div v-if="operationsStore.modules.length === 0" class="col-span-3 text-center py-4 text-slate-400 text-sm">
          No registered cluster modules found. Check Infinispan modulestatus-cache.
        </div>
      </div>
    </div>

    <!-- Active Task Sequences Quick Table -->
    <div class="card space-y-4">
      <div class="flex items-center justify-between">
        <div>
          <h3 class="text-base font-bold text-white flex items-center gap-2">
            <GitMerge :size="18" class="text-emerald-400" />
            Configured Task Sequences
          </h3>
          <p class="subtitle mt-0.5">Automated event listeners and activity pipelines registered across MLLP gateways.</p>
        </div>
        <router-link to="/sequences" class="btn btn-secondary text-xs">
          Manage Sequences &rarr;
        </router-link>
      </div>

      <div class="table-container">
        <table class="table">
          <thead>
            <tr>
              <th>Status</th>
              <th>Sequence ID</th>
              <th>Sequence Name</th>
              <th>Source Queue</th>
              <th>Target Gateways</th>
              <th>Trigger Filters</th>
              <th>Activity Pipeline</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="seq in sequenceStore.sequences" :key="seq.sequenceId">
              <td>
                <span :class="seq.enabled ? 'badge badge-green' : 'badge badge-amber'">
                  {{ seq.enabled ? 'Active' : 'Disabled' }}
                </span>
              </td>
              <td class="font-mono text-xs font-bold text-sky-400">
                <router-link :to="'/sequences/' + seq.sequenceId" class="hover:underline text-sky-400">
                  {{ seq.sequenceId }}
                </router-link>
              </td>
              <td class="font-medium text-white">{{ seq.sequenceName }}</td>
              <td class="font-mono text-xs text-slate-300">{{ seq.sourceQueueName || 'jms:queue:task.event.queue.*' }}</td>
              <td>
                <div class="flex flex-wrap gap-1">
                  <span v-for="gw in seq.targetGatewayInstances" :key="gw" class="badge badge-blue text-[10px]">
                    {{ gw }}
                  </span>
                  <span v-if="!seq.targetGatewayInstances || seq.targetGatewayInstances.length === 0" class="badge badge-purple text-[10px]">
                    * (All Gateways)
                  </span>
                </div>
              </td>
              <td>
                <div class="flex flex-wrap gap-1">
                  <span v-for="trig in seq.targetTriggerTypes" :key="trig" class="badge badge-amber text-[10px]">
                    {{ trig }}
                  </span>
                  <span v-if="!seq.targetTriggerTypes || seq.targetTriggerTypes.length === 0" class="badge badge-purple text-[10px]">
                    * (All Triggers)
                  </span>
                </div>
              </td>
              <td>
                <div class="flex items-center gap-1 text-xs text-emerald-400 font-semibold">
                  <span>{{ getActivityCount(seq) }} Activities</span>
                </div>
              </td>
            </tr>
            <tr v-if="sequenceStore.sequences.length === 0">
              <td colspan="7" class="text-center py-6 text-slate-400">
                No task sequences loaded. Check connection to Infinispan tasksequence-cache.
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>
