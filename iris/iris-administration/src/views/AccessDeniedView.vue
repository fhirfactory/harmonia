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
import { ShieldAlert, ArrowLeft, Key } from 'lucide-vue-next';

const securityStore = useSecurityStore();
</script>

<template>
  <div class="max-w-2xl mx-auto mt-12">
    <div class="card text-center space-y-6 p-8 border-red-500/30 bg-red-950/10">
      <div class="w-16 h-16 rounded-full bg-red-500/20 border border-red-500/40 flex items-center justify-center mx-auto text-red-400">
        <ShieldAlert :size="32" />
      </div>

      <div class="space-y-2">
        <h1 class="text-2xl font-bold text-white">403 — Access Denied</h1>
        <p class="text-slate-400 text-sm">
          Your current Themis security principal does not have sufficient authorities to access this administrative resource.
        </p>
      </div>

      <div class="p-4 bg-slate-950/80 rounded border border-slate-800 text-left space-y-2">
        <div class="text-xs font-bold uppercase tracking-wider text-slate-400">Current Security Context</div>
        <div class="text-sm text-slate-300">
          <strong>Principal:</strong> {{ securityStore.principal?.displayName }} ({{ securityStore.principal?.username }})
        </div>
        <div class="text-xs text-slate-400">
          <strong>Authorities:</strong> 
          <span v-if="securityStore.principal?.authorities?.length" class="text-sky-300 font-mono ml-1">
            {{ securityStore.principal.authorities.join(', ') }}
          </span>
          <span v-else class="text-slate-500 italic ml-1">None</span>
        </div>
        <div class="text-xs text-slate-500 font-mono">
          Correlation ID: {{ securityStore.correlationId }}
        </div>
      </div>

      <div class="flex justify-center gap-4">
        <router-link to="/" class="btn btn-primary">
          <ArrowLeft :size="16" />
          <span>Return to Home</span>
        </router-link>
      </div>
    </div>
  </div>
</template>
