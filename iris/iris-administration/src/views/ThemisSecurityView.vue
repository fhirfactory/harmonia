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
import { useSecurityStore } from '../stores/securityStore';
import { Shield, Key, Lock, CheckCircle2, AlertTriangle, RefreshCw } from 'lucide-vue-next';

const securityStore = useSecurityStore();
</script>

<template>
  <div class="space-y-6">
    <div class="flex flex-col md:flex-row md:items-center justify-between gap-4">
      <div>
        <h1 class="text-2xl font-bold text-white">Themis Security Context &amp; ABAC Policies</h1>
        <p class="text-slate-400 text-sm mt-1">
          Inspection of active security domain, principal context, granted authorities, and policy enforcement points.
        </p>
      </div>

      <button @click="securityStore.refreshCorrelationId()" class="btn btn-secondary btn-sm">
        <RefreshCw :size="14" />
        <span>Rotate Correlation ID</span>
      </button>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
      <!-- Active Principal Context -->
      <div class="card space-y-4">
        <div class="flex items-center gap-2 text-sky-400">
          <Shield :size="18" />
          <h3 class="text-base font-bold text-white">Current Principal</h3>
        </div>

        <div class="space-y-3 text-sm">
          <div class="flex justify-between py-1.5 border-b border-slate-800">
            <span class="text-slate-400">Principal ID:</span>
            <span class="font-mono text-slate-200">{{ securityStore.principal?.id }}</span>
          </div>
          <div class="flex justify-between py-1.5 border-b border-slate-800">
            <span class="text-slate-400">Username:</span>
            <span class="font-semibold text-slate-200">{{ securityStore.principal?.username }}</span>
          </div>
          <div class="flex justify-between py-1.5 border-b border-slate-800">
            <span class="text-slate-400">Display Name:</span>
            <span class="text-slate-200">{{ securityStore.principal?.displayName }}</span>
          </div>
          <div class="flex justify-between py-1.5 border-b border-slate-800">
            <span class="text-slate-400">Practitioner Reference:</span>
            <span class="font-mono text-sky-400">{{ securityStore.principal?.practitionerId || 'N/A' }}</span>
          </div>
          <div class="flex justify-between py-1.5 border-b border-slate-800">
            <span class="text-slate-400">Security Domain:</span>
            <span class="font-bold text-emerald-400">PROVIDER_REGISTRY</span>
          </div>
          <div class="flex justify-between py-1.5 border-b border-slate-800">
            <span class="text-slate-400">Classification:</span>
            <span class="font-bold text-emerald-400">INTERNAL</span>
          </div>
          <div class="flex justify-between py-1.5">
            <span class="text-slate-400">Active Correlation ID:</span>
            <span class="font-mono text-xs text-slate-300">{{ securityStore.correlationId }}</span>
          </div>
        </div>
      </div>

      <!-- Granted Authorities & Evaluated Policies -->
      <div class="card space-y-4">
        <div class="flex items-center gap-2 text-indigo-400">
          <Key :size="18" />
          <h3 class="text-base font-bold text-white">Granted Themis Authorities</h3>
        </div>

        <div class="space-y-2">
          <div 
            v-for="auth in ['provider.read', 'provider.search', 'provider.resource.create', 'provider.resource.update', 'provider.resource.delete', 'provider.change.process', 'provider.admin']"
            :key="auth"
            class="flex items-center justify-between p-2.5 rounded bg-slate-950/70 border"
            :class="securityStore.hasAuthority(auth as any) ? 'border-sky-800/60 bg-sky-950/10' : 'border-slate-800/40 opacity-60'"
          >
            <div class="flex items-center gap-2">
              <CheckCircle2 v-if="securityStore.hasAuthority(auth as any)" :size="15" class="text-sky-400" />
              <Lock v-else :size="15" class="text-slate-500" />
              <span class="font-mono text-xs text-slate-200">{{ auth }}</span>
            </div>
            <span 
              class="badge text-[10px]"
              :class="securityStore.hasAuthority(auth as any) ? 'badge-success' : 'badge-neutral'"
            >
              {{ securityStore.hasAuthority(auth as any) ? 'GRANTED' : 'DENIED' }}
            </span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
