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
import { useOperationsStore } from '../stores/operationsStore';
import { HardDrive, Server, Zap, Database, ShieldCheck, CheckCircle2 } from 'lucide-vue-next';

const operationsStore = useOperationsStore();
</script>

<template>
  <div class="space-y-6">
    <!-- Header -->
    <div class="flex flex-col md:flex-row items-start md:items-center justify-between gap-4">
      <div>
        <div class="flex items-center gap-2">
          <h1 class="text-2xl font-bold text-white">Infinispan Distributed Cache Grid</h1>
          <span class="badge badge-green">In-Memory Tier 3 &amp; 4</span>
        </div>
        <p class="subtitle mt-1">High-availability replicated caches with asynchronous write-behind persistence stores.</p>
      </div>
    </div>

    <!-- Cluster Nodes Overview -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
      <div class="card p-4">
        <span class="text-xs font-semibold text-slate-400 uppercase tracking-wider block mb-1">Infinispan Grid Status</span>
        <div class="text-lg font-bold text-white flex items-center gap-2">
          <Zap :size="18" class="text-emerald-400" />
          <span>Infinispan 15.0 HA</span>
        </div>
        <p class="text-[11px] text-slate-400 mt-1">Synchronous replicated clustering via JGroups TCP</p>
      </div>

      <div class="card p-4">
        <span class="text-xs font-semibold text-slate-400 uppercase tracking-wider block mb-1">Clustered Nodes</span>
        <div class="flex items-center gap-2 mt-1">
          <span class="badge badge-green">node1 (Port 11222)</span>
          <span class="badge badge-green">node2 (Port 11223)</span>
        </div>
        <p class="text-[11px] text-slate-400 mt-1">Multi-instance high-availability replication</p>
      </div>

      <div class="card p-4">
        <span class="text-xs font-semibold text-slate-400 uppercase tracking-wider block mb-1">Persistence Stores</span>
        <div class="text-sm font-bold text-purple-400 flex items-center gap-1 mt-1">
          <Database :size="14" />
          <span>FHIR &amp; Operations JPA Stores</span>
        </div>
        <p class="text-[11px] text-slate-400 mt-1">NonBlockingStore SPI with write-behind queues</p>
      </div>
    </div>

    <!-- Caches Table -->
    <div class="card space-y-4">
      <h3 class="text-base font-bold text-white flex items-center gap-2">
        <HardDrive :size="18" class="text-purple-400" />
        Configured Distributed Caches
      </h3>

      <div class="table-container">
        <table class="table">
          <thead>
            <tr>
              <th>Status</th>
              <th>Cache Name</th>
              <th>Data Domain</th>
              <th>Clustering Mode</th>
              <th>Persistence Target</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="c in operationsStore.caches" :key="c.cacheName">
              <td>
                <span class="badge badge-green flex items-center gap-1 w-fit">
                  <span class="w-1.5 h-1.5 rounded-full bg-emerald-400 animate-pulse"></span>
                  {{ c.status }}
                </span>
              </td>
              <td class="font-mono text-xs font-bold text-sky-400">{{ c.cacheName }}</td>
              <td>
                <span :class="c.type === 'OPERATIONS' ? 'badge badge-amber' : (c.type === 'WORKFLOW' ? 'badge badge-purple' : 'badge badge-blue')" class="text-[10px]">
                  {{ c.type }}
                </span>
              </td>
              <td>
                <span class="badge badge-green text-[10px] font-mono">{{ c.mode }} Replicated</span>
              </td>
              <td class="text-xs text-slate-300 font-medium">
                {{ c.persistenceStore }}
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>
