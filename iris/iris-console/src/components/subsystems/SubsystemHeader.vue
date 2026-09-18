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
import { computed } from 'vue';
import { 
  Server, 
  Layers, 
  Clock, 
  Tag, 
  AlertCircle,
  RefreshCw
} from 'lucide-vue-next';
import { useOperationsStore } from '../../stores/operationsStore';
import StatusBadge from '../common/StatusBadge.vue';

const store = useOperationsStore();

const subsystem = computed(() => store.selectedSubsystem);

const lastUpdatedText = computed(() => {
  if (!subsystem.value?.lastUpdated) return 'Recently';
  return new Date(subsystem.value.lastUpdated).toLocaleString();
});
</script>

<template>
  <div class="bg-[#111827] border-b border-[#1f293d] p-4 lg:p-6">
    <div v-if="subsystem" class="flex flex-col md:flex-row md:items-center justify-between gap-4">
      <!-- Title & Description -->
      <div class="space-y-1 min-w-0">
        <div class="flex items-center gap-3 flex-wrap">
          <h1 class="text-xl lg:text-2xl font-extrabold text-white tracking-tight flex items-center gap-2">
            <span>{{ subsystem.name }}</span>
            <span class="text-xs font-normal text-slate-500 font-mono">({{ subsystem.id }})</span>
          </h1>

          <!-- Health Status Badge -->
          <StatusBadge 
            :status="subsystem.state" 
            size="md" 
            :show-pulse="true" 
            :stale="subsystem.stale"
          />

          <!-- Stale Data Warning Pill -->
          <span 
            v-if="subsystem.stale" 
            class="inline-flex items-center gap-1 px-2.5 py-0.5 rounded-full text-xs font-medium bg-purple-500/15 border border-purple-500/30 text-purple-300"
            title="Telemetry from cached snapshot; live probe unacknowledged"
          >
            <Clock :size="12" />
            <span>Cached Snapshot</span>
          </span>
        </div>

        <p class="text-xs lg:text-sm text-slate-400 max-w-3xl leading-relaxed">
          {{ subsystem.description }}
        </p>
      </div>

      <!-- Metadata Badges -->
      <div class="flex items-center gap-3 flex-wrap text-xs font-mono shrink-0">
        <!-- Instance Count -->
        <div class="flex items-center gap-1.5 px-3 py-1.5 rounded-lg bg-slate-800/80 border border-slate-700/80 text-slate-300">
          <Layers :size="14" class="text-sky-400" />
          <span class="font-bold text-white">{{ subsystem.instanceCount }}</span>
          <span class="text-slate-400">{{ subsystem.instanceCount === 1 ? 'Instance' : 'Instances' }}</span>
        </div>

        <!-- Version -->
        <div class="flex items-center gap-1.5 px-3 py-1.5 rounded-lg bg-slate-800/80 border border-slate-700/80 text-slate-300">
          <Tag :size="14" class="text-emerald-400" />
          <span class="text-slate-400">v</span>
          <span class="font-semibold text-white">{{ subsystem.version || '1.0.0' }}</span>
        </div>

        <!-- Last Updated -->
        <div class="hidden sm:flex items-center gap-1.5 px-3 py-1.5 rounded-lg bg-slate-800/80 border border-slate-700/80 text-slate-400">
          <Clock :size="14" class="text-slate-500" />
          <span class="text-[11px]">{{ lastUpdatedText }}</span>
        </div>
      </div>
    </div>

    <!-- Empty/Loading State if subsystem is not yet resolved -->
    <div v-else class="flex items-center gap-3 text-slate-400 py-4">
      <RefreshCw :size="18" class="animate-spin text-sky-400" />
      <span class="text-sm">Loading subsystem telemetry...</span>
    </div>
  </div>
</template>
