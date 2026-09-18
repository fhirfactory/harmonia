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
import { Database, Code2 } from 'lucide-vue-next';
</script>

<template>
  <div class="space-y-6">
    <div>
      <h1 class="text-2xl font-bold text-white">Reference Data &amp; Value Sets</h1>
      <p class="text-slate-400 text-sm mt-1">
        Code systems, ValueSets, and specialty terminology dictionaries used across Harmonia Provider Registry.
      </p>
    </div>

    <div class="card space-y-4">
      <div class="flex items-center gap-2 text-sky-400">
        <Database :size="18" />
        <h3 class="text-base font-bold text-white">Authoritative Terminology Systems</h3>
      </div>
      <div class="p-4 bg-slate-950/70 rounded border border-slate-800 space-y-2 text-sm text-slate-300">
        <div>&bull; <strong>ANZSCO / SNOMED CT:</strong> Medical Specialties and Practice Roles</div>
        <div>&bull; <strong>HPI-I / HPI-O:</strong> National Provider &amp; Organization Identifiers</div>
        <div>&bull; <strong>FHIR ContactPoint:</strong> Standard Telecom systems (phone, email, secure messaging)</div>
      </div>
    </div>
  </div>
</template>
