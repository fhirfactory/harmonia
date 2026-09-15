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
import { ref, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useSequenceStore } from '../stores/sequenceStore';
import type { TaskSequence } from '../models/operations';
import { 
  GitMerge, ArrowLeft, ArrowRight, PlayCircle, PauseCircle, 
  Trash2, Layers, Cpu, Radio, ShieldCheck, Database, CheckCircle2, Code
} from 'lucide-vue-next';

const route = useRoute();
const router = useRouter();
const sequenceStore = useSequenceStore();

const sequenceId = computed(() => route.params.id as string);
const sequence = ref<TaskSequence | null>(null);
const loading = ref(true);
const activeTab = ref<'pipeline' | 'json'>('pipeline');

onMounted(async () => {
  loading.value = true;
  const seq = await sequenceStore.fetchSequence(sequenceId.value);
  if (seq) {
    sequence.value = seq;
  } else {
    // Check in existing list if direct fetch fails
    const found = sequenceStore.sequences.find(s => s.sequenceId === sequenceId.value);
    if (found) sequence.value = found;
  }
  loading.value = false;
});

const toggleSequence = async () => {
  if (sequence.value) {
    await sequenceStore.toggleSequence(sequence.value);
    sequence.value.enabled = !sequence.value.enabled;
  }
};

const deleteSequence = async () => {
  if (confirm(`Are you sure you want to delete sequence ${sequenceId.value}?`)) {
    await sequenceStore.deleteSequence(sequenceId.value);
    router.push('/sequences');
  }
};

const getActivityList = (actIds: any): string[] => {
  if (!actIds) return [];
  if (Array.isArray(actIds)) return actIds;
  if (typeof actIds === 'object') {
    return Object.keys(actIds)
      .map(k => Number(k))
      .sort((a, b) => a - b)
      .map(k => actIds[k]);
  }
  return [];
};
</script>

<template>
  <div class="space-y-6">
    <!-- Top Bar Navigation -->
    <div class="flex items-center justify-between">
      <router-link to="/sequences" class="btn btn-secondary text-xs">
        <ArrowLeft :size="14" /> Back to Sequences
      </router-link>

      <div class="flex items-center gap-2" v-if="sequence">
        <button 
          @click="toggleSequence" 
          class="btn text-xs"
          :class="sequence.enabled ? 'btn-secondary text-emerald-400' : 'btn-secondary text-amber-400'"
        >
          <component :is="sequence.enabled ? PlayCircle : PauseCircle" :size="15" />
          <span>{{ sequence.enabled ? 'Sequence Active' : 'Sequence Disabled' }}</span>
        </button>
        <button @click="deleteSequence" class="btn btn-danger text-xs">
          <Trash2 :size="14" /> Delete
        </button>
      </div>
    </div>

    <!-- Sequence Hero Card -->
    <div v-if="sequence" class="card" style="background: linear-gradient(135deg, #111827 0%, #064e3b 100%);">
      <div class="flex flex-col md:flex-row items-start md:items-center justify-between gap-4">
        <div>
          <div class="flex items-center gap-2 mb-2">
            <span :class="sequence.enabled ? 'badge badge-green' : 'badge badge-amber'">
              {{ sequence.enabled ? 'Live Consumer Active' : 'Consumer Paused' }}
            </span>
            <span class="badge badge-blue font-mono">v{{ sequence.version || '1.0.0' }}</span>
          </div>
          <h1 class="text-2xl font-extrabold text-white">{{ sequence.sequenceName }}</h1>
          <p class="subtitle font-mono text-xs text-sky-400 mt-1">{{ sequence.sequenceId }}</p>
          <p v-if="sequence.description || sequence.sequenceDescription" class="text-xs text-slate-300 mt-2">{{ sequence.description || sequence.sequenceDescription }}</p>
        </div>

        <!-- Tab Switcher -->
        <div class="flex items-center gap-1 bg-slate-950/70 p-1 rounded-lg border border-slate-700">
          <button 
            @click="activeTab = 'pipeline'"
            class="px-3 py-1.5 rounded-md text-xs font-semibold transition"
            :class="activeTab === 'pipeline' ? 'bg-emerald-500/20 text-emerald-400' : 'text-slate-400 hover:text-white'"
          >
            Visual Pipeline
          </button>
          <button 
            @click="activeTab = 'json'"
            class="px-3 py-1.5 rounded-md text-xs font-semibold transition"
            :class="activeTab === 'json' ? 'bg-emerald-500/20 text-emerald-400' : 'text-slate-400 hover:text-white'"
          >
            JSON Spec
          </button>
        </div>
      </div>
    </div>

    <!-- Details Grid -->
    <div v-if="sequence && activeTab === 'pipeline'" class="space-y-6">
      <!-- Routing Criteria Overview -->
      <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
        <div class="card p-4">
          <span class="text-xs font-semibold text-slate-400 uppercase tracking-wider block mb-1">Source JMS Queue</span>
          <div class="font-mono text-sm text-sky-400 font-bold">
            {{ sequence.sourceQueueName || 'task.event.queue.*' }}
          </div>
          <p class="text-[11px] text-slate-400 mt-1">Dedicated per-gateway or shared ActiveMQ Artemis queue</p>
        </div>

        <div class="card p-4">
          <span class="text-xs font-semibold text-slate-400 uppercase tracking-wider block mb-1">Target Gateways</span>
          <div class="flex flex-wrap gap-1 mt-1">
            <span v-for="gw in sequence.targetGatewayInstances" :key="gw" class="badge badge-blue text-xs">
              {{ gw }}
            </span>
            <span v-if="!sequence.targetGatewayInstances || sequence.targetGatewayInstances.length === 0" class="badge badge-purple text-xs">
              * (All Gateways)
            </span>
          </div>
          <p class="text-[11px] text-slate-400 mt-1">Evaluated by TaskSequence.matches(gatewayId, trigger)</p>
        </div>

        <div class="card p-4">
          <span class="text-xs font-semibold text-slate-400 uppercase tracking-wider block mb-1">Target Trigger Events</span>
          <div class="flex flex-wrap gap-1 mt-1">
            <span v-for="trig in sequence.targetTriggerTypes" :key="trig" class="badge badge-amber text-xs">
              {{ trig }}
            </span>
            <span v-if="!sequence.targetTriggerTypes || sequence.targetTriggerTypes.length === 0" class="badge badge-purple text-xs">
              * (All Triggers)
            </span>
          </div>
          <p class="text-[11px] text-slate-400 mt-1">HL7 trigger events filtering execution</p>
        </div>
      </div>

      <!-- Pipeline Visualization Flowchart -->
      <div class="card space-y-4">
        <div class="flex items-center justify-between">
          <h3 class="text-base font-bold text-white flex items-center gap-2">
            <Layers :size="18" class="text-emerald-400" />
            Sequential Activity Execution Flow
          </h3>
          <span class="badge badge-green text-xs font-mono">Apache Camel Direct Route</span>
        </div>

        <div class="flex flex-col lg:flex-row items-stretch lg:items-center gap-3 overflow-x-auto py-4">
          <!-- Step 1: Ingestion -->
          <div class="flex-1 p-4 rounded-lg bg-slate-950 border border-sky-500/40 text-center relative min-w-[200px]">
            <div class="p-2 w-9 h-9 rounded-full bg-sky-500/10 text-sky-400 mx-auto flex items-center justify-center mb-2">
              <Radio :size="18" />
            </div>
            <span class="text-[10px] font-bold text-sky-400 uppercase tracking-wider block">Step 0. Event Ingestion</span>
            <h4 class="text-xs font-bold text-white mt-0.5">JMS Consumer</h4>
            <p class="text-[10px] text-slate-400 mt-1 font-mono">task.event.queue</p>
          </div>

          <div class="hidden lg:flex text-slate-500">
            <ArrowRight :size="20" />
          </div>

          <!-- Step 2: Filtering Predicate -->
          <div class="flex-1 p-4 rounded-lg bg-slate-950 border border-amber-500/40 text-center relative min-w-[200px]">
            <div class="p-2 w-9 h-9 rounded-full bg-amber-500/10 text-amber-400 mx-auto flex items-center justify-center mb-2">
              <ShieldCheck :size="18" />
            </div>
            <span class="text-[10px] font-bold text-amber-400 uppercase tracking-wider block">Filter Predicate</span>
            <h4 class="text-xs font-bold text-white mt-0.5">Gateway &amp; Trigger Match</h4>
            <p class="text-[10px] text-slate-400 mt-1 font-mono">matches(gw, trigger)</p>
          </div>

          <div class="hidden lg:flex text-slate-500">
            <ArrowRight :size="20" />
          </div>

          <!-- Dynamic Activities -->
          <template v-for="(act, idx) in getActivityList(sequence.activityIds)" :key="act">
            <div class="flex-1 p-4 rounded-lg bg-slate-950 border border-emerald-500/40 text-center relative min-w-[220px]">
              <div class="p-2 w-9 h-9 rounded-full bg-emerald-500/10 text-emerald-400 mx-auto flex items-center justify-center mb-2">
                <Cpu :size="18" />
              </div>
              <span class="text-[10px] font-bold text-emerald-400 uppercase tracking-wider block">Activity {{ idx + 1 }}</span>
              <h4 class="text-xs font-bold text-white mt-0.5 font-mono">{{ act }}</h4>
              <p class="text-[10px] text-slate-400 mt-1">direct:activity-{{ act }}</p>
            </div>

            <div v-if="idx < (getActivityList(sequence.activityIds).length - 1)" class="hidden lg:flex text-slate-500">
              <ArrowRight :size="20" />
            </div>
          </template>

          <div class="hidden lg:flex text-slate-500">
            <ArrowRight :size="20" />
          </div>

          <!-- Final Sink -->
          <div class="flex-1 p-4 rounded-lg bg-slate-950 border border-purple-500/40 text-center relative min-w-[200px]">
            <div class="p-2 w-9 h-9 rounded-full bg-purple-500/10 text-purple-400 mx-auto flex items-center justify-center mb-2">
              <Database :size="18" />
            </div>
            <span class="text-[10px] font-bold text-purple-400 uppercase tracking-wider block">Persistence Sink</span>
            <h4 class="text-xs font-bold text-white mt-0.5">Infinispan Cache</h4>
            <p class="text-[10px] text-slate-400 mt-1 font-mono">Hot Rod Remote Cache</p>
          </div>
        </div>
      </div>
    </div>

    <!-- JSON Spec Tab -->
    <div v-if="sequence && activeTab === 'json'" class="card space-y-4">
      <h3 class="text-base font-bold text-white flex items-center gap-2">
        <Code :size="16" class="text-purple-400" />
        TaskSequence JSON Payload
      </h3>
      <pre class="code-view">{{ JSON.stringify(sequence, null, 2) }}</pre>
    </div>
  </div>
</template>
