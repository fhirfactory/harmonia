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
import { SlidersHorizontal, Info } from 'lucide-vue-next';
</script>

<template>
  <div class="space-y-6">
    <div>
      <h1 class="text-2xl font-bold text-white">System Preferences &amp; Configuration</h1>
      <p class="text-slate-400 text-sm mt-1">
        Harmonia Iris Administration console display settings and environment configuration.
      </p>
    </div>

    <div class="card space-y-4">
      <div class="flex items-center gap-2 text-sky-400">
        <SlidersHorizontal :size="18" />
        <h3 class="text-base font-bold text-white">Application Settings</h3>
      </div>

      <div class="p-4 bg-slate-950/70 rounded border border-slate-800 space-y-2 text-sm text-slate-300">
        <div class="flex justify-between">
          <span>Backend REST Gateway:</span>
          <span class="font-mono text-sky-400">/api/fhir &bull; port 8080</span>
        </div>
        <div class="flex justify-between">
          <span>Pylai Asynchronous Gateway:</span>
          <span class="font-mono text-sky-400">/fhir &bull; port 8081</span>
        </div>
        <div class="flex justify-between">
          <span>Safe PHI Console Logging:</span>
          <span class="badge badge-success">ENFORCED (Masked / Redacted)</span>
        </div>
        <div class="flex justify-between">
          <span>Correlation Header Propagation:</span>
          <span class="badge badge-success">ENABLED (X-Correlation-Id)</span>
        </div>
      </div>
    </div>
  </div>
</template>
